/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.html;

import static org.htmlunit.BrowserVersionFeatures.HTMLIMAGE_BLANK_SRC_AS_EMPTY;
import static org.htmlunit.BrowserVersionFeatures.HTMLIMAGE_EMPTY_SRC_DISPLAY_FALSE;
import static org.htmlunit.BrowserVersionFeatures.HTMLIMAGE_HTMLELEMENT;
import static org.htmlunit.BrowserVersionFeatures.HTMLIMAGE_HTMLUNKNOWNELEMENT;
import static org.htmlunit.BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0;
import static org.htmlunit.BrowserVersionFeatures.JS_IMAGE_WIDTH_HEIGHT_RETURNS_24x24_0x0;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.BrowserVersion;
import org.htmlunit.Page;
import org.htmlunit.ScriptResult;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.http.HttpStatus;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.host.dom.Document;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.MouseEvent;
import org.htmlunit.javascript.host.html.HTMLElement;
import org.htmlunit.platform.Platform;
import org.htmlunit.platform.geom.IntDimension2D;
import org.htmlunit.platform.image.ImageData;
import org.htmlunit.util.UrlUtils;

/**
 * Wrapper for the HTML element "img".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author <a href="mailto:knut.johannes.dahle@gmail.com">Knut Johannes Dahle</a>
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 * @author Alex Gorbatovsky
 */
public class HtmlImage extends HtmlElement {

    private static final Log LOG = LogFactory.getLog(HtmlImage.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "img";
    /** Another HTML tag represented by this element. */
    public static final String TAG_NAME2 = "image";

    private final String originalQualifiedName_;

    private int lastClickX_ = -1;
    private int lastClickY_ = -1;
    private WebResponse imageWebResponse_;
    private transient ImageData imageData_;
    private int width_ = -1;
    private int height_ = -1;
    private boolean downloaded_;
    private boolean isComplete_;
    private boolean onloadProcessed_;
    private boolean createdByJavascript_;

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlImage(final String qualifiedName, final SgmlPage page, final Map<String, DomAttr> attributes) {
        super(unifyLocalName(qualifiedName), page, attributes);
        originalQualifiedName_ = qualifiedName;
        if (page.getWebClient().getOptions().isDownloadImages()) {
            try {
                downloadImageIfNeeded();
            }
            catch (final IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Unable to download image for element " + this);
                }
            }
        }
    }

    private static String unifyLocalName(final String qualifiedName) {
        if (qualifiedName != null && qualifiedName.endsWith(TAG_NAME2)) {
            final int pos = qualifiedName.lastIndexOf(TAG_NAME2);
            return qualifiedName.substring(0, pos) + TAG_NAME;
        }
        return qualifiedName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onAddedToPage() {
        doOnLoad();
        super.onAddedToPage();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String value,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {

        final HtmlPage htmlPage = getHtmlPageOrNull();
        final String qualifiedNameLC = org.htmlunit.util.StringUtils.toRootLowerCase(qualifiedName);
        if (SRC_ATTRIBUTE.equals(qualifiedNameLC) && value != ATTRIBUTE_NOT_DEFINED && htmlPage != null) {
            final String oldValue = getAttributeNS(namespaceURI, qualifiedNameLC);
            if (!oldValue.equals(value)) {
                super.setAttributeNS(namespaceURI, qualifiedNameLC, value, notifyAttributeChangeListeners,
                        notifyMutationObservers);

                // onload handlers may need to be invoked again, and a new image may need to be downloaded
                onloadProcessed_ = false;
                downloaded_ = false;
                isComplete_ = false;
                width_ = -1;
                height_ = -1;
                try {
                    closeImageData();
                }
                catch (final Exception e) {
                    LOG.error(e.getMessage(), e);
                }

                final String readyState = htmlPage.getReadyState();
                if (READY_STATE_LOADING.equals(readyState)) {
                    final PostponedAction action = new PostponedAction(getPage(), "HtmlImage.setAttributeNS") {
                        @Override
                        public void execute() {
                            doOnLoad();
                        }
                    };
                    htmlPage.getWebClient().getJavaScriptEngine().addPostponedAction(action);
                    return;
                }
                doOnLoad();
                return;
            }
        }

        super.setAttributeNS(namespaceURI, qualifiedNameLC, value, notifyAttributeChangeListeners,
                notifyMutationObservers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processImportNode(final Document doc) {
        URL oldUrl = null;
        final String src = getSrcAttribute();
        HtmlPage htmlPage = getHtmlPageOrNull();
        try {
            if (htmlPage != null) {
                oldUrl = htmlPage.getFullyQualifiedUrl(src);
            }
        }
        catch (final MalformedURLException ignored) {
            // ignore
        }

        super.processImportNode(doc);

        URL url = null;
        htmlPage = getHtmlPageOrNull();
        try {
            if (htmlPage != null) {
                url = htmlPage.getFullyQualifiedUrl(src);
            }
        }
        catch (final MalformedURLException ignored) {
            // ignore
        }

        if (oldUrl == null || !UrlUtils.sameFile(oldUrl, url)) {
            // image has to be reloaded
            lastClickX_ = -1;
            lastClickY_ = -1;
            imageWebResponse_ = null;
            imageData_ = null;
            width_ = -1;
            height_ = -1;
            downloaded_ = false;
            isComplete_ = false;
            onloadProcessed_ = false;
            createdByJavascript_ = true;
        }

        if (htmlPage == null) {
            return; // nothing to do if embedded in XML code
        }

        if (htmlPage.getWebClient().getOptions().isDownloadImages()) {
            try {
                downloadImageIfNeeded();
            }
            catch (final IOException e) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Unable to download image for element " + this);
                }
            }
        }
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * <p>Executes this element's <code>onload</code> or <code>onerror</code> handler. This method downloads the image
     * if either of these handlers are present (prior to invoking the resulting handler), because applications
     * sometimes use images to send information to the server and use these handlers to get notified when the
     * information has been received by the server.</p>
     *
     * <p>See <a href="http://www.nabble.com/How-should-we-handle-image.onload--tt9850876.html">here</a> and
     * <a href="http://www.nabble.com/Image-Onload-Support-td18895781.html">here</a> for the discussion which
     * lead up to this method.</p>
     *
     * <p>This method may be called multiple times, but will only attempt to execute the <code>onload</code> or
     * <code>onerror</code> handler the first time it is invoked.</p>
     */
    public void doOnLoad() {
        if (onloadProcessed_) {
            return;
        }

        final HtmlPage htmlPage = getHtmlPageOrNull();
        if (htmlPage == null) {
            return; // nothing to do if embedded in XML code
        }

        final WebClient client = htmlPage.getWebClient();

        final boolean hasEventHandler = hasEventHandlers("onload") || hasEventHandlers("onerror");
        if (((hasEventHandler && client.isJavaScriptEnabled())
                || client.getOptions().isDownloadImages()) && hasAttribute(SRC_ATTRIBUTE)) {
            boolean loadSuccessful = false;
            final boolean tryDownload;
            if (hasFeature(HTMLIMAGE_BLANK_SRC_AS_EMPTY)) {
                tryDownload = !StringUtils.isBlank(getSrcAttribute());
            }
            else {
                tryDownload = !getSrcAttribute().isEmpty();
            }
            if (tryDownload) {
                // We need to download the image and then call the resulting handler.
                try {
                    downloadImageIfNeeded();
                    // if the download was a success
                    if (imageWebResponse_.isSuccess()) {
                        if (imageWebResponse_.getStatusCode() != HttpStatus.NO_CONTENT_204) {
                            loadSuccessful = true; // Trigger the onload handler
                        }
                    }
                }
                catch (final IOException e) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("IOException while downloading image for '" + this + "'", e);
                    }
                }
            }

            if (!client.isJavaScriptEnabled()) {
                onloadProcessed_ = true;
                return;
            }

            if (!hasEventHandler) {
                return;
            }

            onloadProcessed_ = true;
            final Event event = new Event(this, loadSuccessful ? Event.TYPE_LOAD : Event.TYPE_ERROR);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Firing the " + event.getType() + " event for '" + this + "'.");
            }

            if (READY_STATE_LOADING.equals(htmlPage.getReadyState())) {
                final PostponedAction action = new PostponedAction(getPage(), "HtmlImage.doOnLoad") {
                    @Override
                    public void execute() {
                        HtmlImage.this.fireEvent(event);
                    }
                };
                htmlPage.getWebClient().getJavaScriptEngine().addPostponedAction(action);
            }
            else {
                final AbstractJavaScriptEngine<?> jsEngine = client.getJavaScriptEngine();
                if (jsEngine.isScriptRunning()) {
                    final PostponedAction action = new PostponedAction(getPage(), "HtmlImage.doOnLoad") {
                        @Override
                        public void execute() {
                            HtmlImage.this.fireEvent(event);
                        }
                    };
                    jsEngine.addPostponedAction(action);
                }
                else {
                    fireEvent(event);
                }
            }
        }
    }

    /**
     * Returns the value of the attribute {@code src}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code src} or an empty string if that attribute isn't defined
     */
    public final String getSrcAttribute() {
        return getSrcAttributeNormalized();
    }

    /**
     * Returns the value of the {@code src} value.
     * @return the value of the {@code src} value
     */
    public String getSrc() {
        final String src = getSrcAttribute();
        if (org.htmlunit.util.StringUtils.isEmptyString(src)) {
            return src;
        }
        try {
            final HtmlPage page = (HtmlPage) getPage();
            return page.getFullyQualifiedUrl(src).toExternalForm();
        }
        catch (final MalformedURLException e) {
            final String msg = "Unable to create fully qualified URL for src attribute of image " + e.getMessage();
            throw new RuntimeException(msg, e);
        }
    }

    /**
     * Returns the value of the attribute {@code alt}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code alt} or an empty string if that attribute isn't defined
     */
    public final String getAltAttribute() {
        return getAttributeDirect("alt");
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeDirect(NAME_ATTRIBUTE);
    }

    /**
     * Returns the value of the attribute {@code longdesc}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code longdesc} or an empty string if that attribute isn't defined
     */
    public final String getLongDescAttribute() {
        return getAttributeDirect("longdesc");
    }

    /**
     * Returns the value of the attribute {@code height}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code height} or an empty string if that attribute isn't defined
     */
    public final String getHeightAttribute() {
        return getAttributeDirect("height");
    }

    /**
     * Returns the value of the attribute {@code width}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code width} or an empty string if that attribute isn't defined
     */
    public final String getWidthAttribute() {
        return getAttributeDirect("width");
    }

    /**
     * Returns the value of the attribute {@code usemap}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code usemap} or an empty string if that attribute isn't defined
     */
    public final String getUseMapAttribute() {
        return getAttributeDirect("usemap");
    }

    /**
     * Returns the value of the attribute {@code ismap}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code ismap} or an empty string if that attribute isn't defined
     */
    public final String getIsmapAttribute() {
        return getAttributeDirect("ismap");
    }

    /**
     * Returns the value of the attribute {@code align}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code align} or an empty string if that attribute isn't defined
     */
    public final String getAlignAttribute() {
        return getAttributeDirect("align");
    }

    /**
     * Returns the value of the attribute {@code border}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code border} or an empty string if that attribute isn't defined
     */
    public final String getBorderAttribute() {
        return getAttributeDirect("border");
    }

    /**
     * Returns the value of the attribute {@code hspace}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code hspace} or an empty string if that attribute isn't defined
     */
    public final String getHspaceAttribute() {
        return getAttributeDirect("hspace");
    }

    /**
     * Returns the value of the attribute {@code vspace}. Refer to the
     * <a href="http://www.w3.org/TR/html401/">HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code vspace} or an empty string if that attribute isn't defined
     */
    public final String getVspaceAttribute() {
        return getAttributeDirect("vspace");
    }

    /**
     * <p>Returns the image's actual height (<b>not</b> the image's {@link #getHeightAttribute() height attribute}).</p>
     * <p><span style="color:red">POTENTIAL PERFORMANCE KILLER - DOWNLOADS THE IMAGE - USE AT YOUR OWN RISK</span></p>
     * <p>If the image has not already been downloaded, this method triggers a download and caches the image.</p>
     *
     * @return the image's actual height
     * @throws IOException if an error occurs while downloading or reading the image
     */
    public int getHeight() throws IOException {
        if (height_ < 0) {
            determineWidthAndHeight();
        }
        return height_;
    }

    /**
     * Returns the value same value as the js height property.
     * @return the value of the {@code height} property
     */
    public int getHeightOrDefault() {
        final String height = getHeightAttribute();

        if (ATTRIBUTE_NOT_DEFINED != height) {
            try {
                return Integer.parseInt(height);
            }
            catch (final NumberFormatException ignored) {
                // ignore
            }
        }

        final String src = getSrcAttribute();
        if (ATTRIBUTE_NOT_DEFINED == src) {
            final BrowserVersion browserVersion = getPage().getWebClient().getBrowserVersion();
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0)
                    || browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_24x24_0x0)) {
                return 0;
            }
            return 24;
        }

        final WebClient webClient = getPage().getWebClient();
        final BrowserVersion browserVersion = webClient.getBrowserVersion();
        if (StringUtils.isEmpty(src)) {
            return 0;
        }
        if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0) && StringUtils.isBlank(src)) {
            return 0;
        }

        try {
            return getHeight();
        }
        catch (final IOException e) {
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0)) {
                return 16;
            }
            return 24;
        }
    }

    /**
     * <p>Returns the image's actual width (<b>not</b> the image's {@link #getWidthAttribute() width attribute}).</p>
     * <p><span style="color:red">POTENTIAL PERFORMANCE KILLER - DOWNLOADS THE IMAGE - USE AT YOUR OWN RISK</span></p>
     * <p>If the image has not already been downloaded, this method triggers a download and caches the image.</p>
     *
     * @return the image's actual width
     * @throws IOException if an error occurs while downloading or reading the image
     */
    public int getWidth() throws IOException {
        if (width_ < 0) {
            determineWidthAndHeight();
        }
        return width_;
    }

    /**
     * Returns the value same value as the js width property.
     * @return the value of the {@code width} property
     */
    public int getWidthOrDefault() {
        final String widthAttrib = getWidthAttribute();

        if (ATTRIBUTE_NOT_DEFINED != widthAttrib) {
            try {
                return Integer.parseInt(widthAttrib);
            }
            catch (final NumberFormatException ignored) {
                // ignore
            }
        }

        final String src = getSrcAttribute();
        if (ATTRIBUTE_NOT_DEFINED == src) {
            final BrowserVersion browserVersion = getPage().getWebClient().getBrowserVersion();
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0)
                    || browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_24x24_0x0)) {
                return 0;
            }
            return 24;
        }

        final WebClient webClient = getPage().getWebClient();
        final BrowserVersion browserVersion = webClient.getBrowserVersion();
        if (StringUtils.isEmpty(src)) {
            return 0;
        }
        if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0) && StringUtils.isBlank(src)) {
            return 0;
        }

        try {
            return getWidth();
        }
        catch (final IOException e) {
            if (browserVersion.hasFeature(JS_IMAGE_WIDTH_HEIGHT_RETURNS_16x16_0x0)) {
                return 16;
            }
            return 24;
        }
    }

    /**
     * @return the {@link ImageData} of this image
     * @throws IOException in case of error
     */
    public ImageData getImageData() throws IOException {
        readImageIfNeeded();
        return imageData_;
    }

    private void determineWidthAndHeight() throws IOException {
        readImageIfNeeded();

        final IntDimension2D dim = imageData_.getWidthHeight();
        width_ = dim.getWidth();
        height_ = dim.getHeight();

        // ImageIO creates temp files; to save file handles
        // we will cache the values and close this directly to free the resources
        closeImageData();
    }

    private void closeImageData() throws IOException {
        if (imageData_ != null) {
            try {
                imageData_.close();
            }
            catch (final IOException e) {
                throw e;
            }
            catch (final Exception ex) {
                throw new IOException("Exception during close()", ex);
            }
            imageData_ = null;
        }
    }

    /**
     * <p>Returns the <code>WebResponse</code> for the image contained by this image element.</p>
     * <p><span style="color:red">POTENTIAL PERFORMANCE KILLER - DOWNLOADS THE IMAGE - USE AT YOUR OWN RISK</span></p>
     * <p>If the image has not already been downloaded and <code>downloadIfNeeded</code> is {@code true}, this method
     * triggers a download and caches the image.</p>
     *
     * @param downloadIfNeeded whether or not the image should be downloaded (if it hasn't already been downloaded)
     * @return {@code null} if no download should be performed and one hasn't already been triggered; otherwise,
     *         the response received when performing a request for the image referenced by this element
     * @throws IOException if an error occurs while downloading the image
     */
    public WebResponse getWebResponse(final boolean downloadIfNeeded) throws IOException {
        if (downloadIfNeeded) {
            downloadImageIfNeeded();
        }
        return imageWebResponse_;
    }

    /**
     * <p>Downloads the image contained by this image element.</p>
     * <p><span style="color:red">POTENTIAL PERFORMANCE KILLER - DOWNLOADS THE IMAGE - USE AT YOUR OWN RISK</span></p>
     * <p>If the image has not already been downloaded, this method triggers a download and caches the image.</p>
     *
     * @throws IOException if an error occurs while downloading the image
     */
    private void downloadImageIfNeeded() throws IOException {
        if (!downloaded_) {
            // HTMLIMAGE_BLANK_SRC_AS_EMPTY
            final String src = getSrcAttribute();

            if (!org.htmlunit.util.StringUtils.isEmptyString(src)) {
                final HtmlPage page = (HtmlPage) getPage();
                final WebClient webClient = page.getWebClient();
                final BrowserVersion browser = webClient.getBrowserVersion();

                if (!(browser.hasFeature(HTMLIMAGE_BLANK_SRC_AS_EMPTY)
                        && StringUtils.isBlank(src))) {
                    final URL url = page.getFullyQualifiedUrl(src);
                    final WebRequest request = new WebRequest(url, browser.getImgAcceptHeader(),
                                                                    browser.getAcceptEncodingHeader());
                    request.setCharset(page.getCharset());
                    request.setRefererHeader(page.getUrl());
                    imageWebResponse_ = webClient.loadWebResponse(request);
                }
            }

            closeImageData();

            downloaded_ = true;
            isComplete_ = true;

            width_ = -1;
            height_ = -1;
        }
    }

    private void readImageIfNeeded() throws IOException {
        downloadImageIfNeeded();
        if (imageData_ == null) {
            if (null == imageWebResponse_) {
                throw new IOException("No image response available (src='" + getSrcAttribute() + "')");
            }
            imageData_ = Platform.buildImageData(imageWebResponse_.getContentAsStream());
        }
    }

    /**
     * Simulates clicking this element at the specified position. This only makes sense for
     * an image map (currently only server side), where the position matters. This method
     * returns the page contained by this image's window after the click, which may or may not
     * be the same as the original page, depending on JavaScript event handlers, etc.
     *
     * @param x the x position of the click
     * @param y the y position of the click
     * @return the page contained by this image's window after the click
     * @exception IOException if an IO error occurs
     */
    public Page click(final int x, final int y) throws IOException {
        lastClickX_ = x;
        lastClickY_ = y;
        try {
            return super.click();
        }
        finally {
            lastClickX_ = -1;
            lastClickY_ = -1;
        }
    }

    /**
     * Simulates clicking this element at the position <code>(0, 0)</code>. This method returns
     * the page contained by this image's window after the click, which may or may not be the
     * same as the original page, depending on JavaScript event handlers, etc.
     *
     * @return the page contained by this image's window after the click
     * @exception IOException if an IO error occurs
     */
    @Override
    @SuppressWarnings("unchecked")
    public Page click() throws IOException {
        return click(0, 0);
    }

    /**
     * Performs the click action on the enclosing A tag (if any).
     * {@inheritDoc}
     * @throws IOException if an IO error occurred
     */
    @Override
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        if (ATTRIBUTE_NOT_DEFINED != getUseMapAttribute()) {
            // remove initial '#'
            final String mapName = getUseMapAttribute().substring(1);
            final HtmlElement doc = ((HtmlPage) getPage()).getDocumentElement();
            final HtmlMap map = doc.getOneHtmlElementByAttribute("map", NAME_ATTRIBUTE, mapName);
            for (final DomElement element : map.getChildElements()) {
                if (element instanceof HtmlArea) {
                    final HtmlArea area = (HtmlArea) element;
                    if (area.containsPoint(Math.max(lastClickX_, 0), Math.max(lastClickY_, 0))) {
                        area.doClickStateUpdate(shiftKey, ctrlKey);
                        return false;
                    }
                }
            }
        }
        final HtmlAnchor anchor = (HtmlAnchor) getEnclosingElement("a");
        if (anchor == null) {
            return false;
        }
        if (ATTRIBUTE_NOT_DEFINED != getIsmapAttribute()) {
            final String suffix = "?" + Math.max(lastClickX_, 0) + "," + Math.max(lastClickY_, 0);
            anchor.doClickStateUpdate(false, false, suffix);
            return false;
        }
        anchor.doClickStateUpdate(shiftKey, ctrlKey);
        return false;
    }

    /**
     * Saves this image as the specified file.
     * @param file the file to save to
     * @throws IOException if an IO error occurs
     */
    public void saveAs(final File file) throws IOException {
        downloadImageIfNeeded();
        if (null != imageWebResponse_) {
            try (OutputStream fos = Files.newOutputStream(file.toPath());
                    InputStream inputStream = imageWebResponse_.getContentAsStream()) {
                IOUtils.copy(inputStream, fos);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE;
    }

    /**
     * @return true if the image was successfully downloaded
     */
    public boolean isComplete() {
        return isComplete_ || ATTRIBUTE_NOT_DEFINED == getSrcAttribute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisplayed() {
        final String src = getSrcAttribute();
        if (ATTRIBUTE_NOT_DEFINED == src) {
            return false;
        }
        if (hasFeature(HTMLIMAGE_BLANK_SRC_AS_EMPTY) && StringUtils.isBlank(src)) {
            return false;
        }
        if (hasFeature(HTMLIMAGE_EMPTY_SRC_DISPLAY_FALSE) && StringUtils.isEmpty(src)) {
            return false;
        }

        return super.isDisplayed();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Marks this frame as created by javascript.
     */
    public void markAsCreatedByJavascript() {
        createdByJavascript_ = true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns true if this frame was created by javascript.
     * @return true or false
     */
    public boolean wasCreatedByJavascript() {
        return createdByJavascript_;
    }

    /**
     * Returns the original element qualified name,
     * this is needed to differentiate between <code>img</code> and <code>image</code>.
     * @return the original element qualified name
     */
    public String getOriginalQualifiedName() {
        return originalQualifiedName_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        if (wasCreatedByJavascript()
                && (hasFeature(HTMLIMAGE_HTMLELEMENT) || hasFeature(HTMLIMAGE_HTMLUNKNOWNELEMENT))) {
            return originalQualifiedName_;
        }
        return super.getLocalName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScriptResult fireEvent(final Event event) {
        if (event instanceof MouseEvent) {
            final MouseEvent mouseEvent = (MouseEvent) event;
            final HTMLElement scriptableObject = getScriptableObject();
            if (lastClickX_ >= 0) {
                mouseEvent.setClientX(scriptableObject.getPosX() + lastClickX_);
            }
            if (lastClickY_ >= 0) {
                mouseEvent.setClientY(scriptableObject.getPosX() + lastClickY_);
            }
        }

        return super.fireEvent(event);
    }
}
