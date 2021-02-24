/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.FRAME_LOCATION_ABOUT_BLANK_FOR_ABOUT_SCHEME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.URL_MINIMAL_QUERY_ENCODING;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.FrameContentHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.AbstractJavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Base class for frame and iframe.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author David D. Kilzer
 * @author Stefan Anzinger
 * @author Ahmed Ashour
 * @author Dmitri Zoubkov
 * @author Daniel Gredler
 * @author Ronald Brill
 * @author Frank Danek
 */
public abstract class BaseFrameElement extends HtmlElement {

    private static final Log LOG = LogFactory.getLog(BaseFrameElement.class);
    private FrameWindow enclosedWindow_;
    private boolean contentLoaded_;
    private boolean createdByJavascript_;
    private boolean loadSrcWhenAddedToPage_;

    /**
     * Creates an instance of BaseFrame.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    protected BaseFrameElement(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        init();

        if (null != page && page.isHtmlPage() && ((HtmlPage) page).isParsingHtmlSnippet()) {
            // if created by the HTMLParser the src attribute is not set via setAttribute() or some other method but is
            // part of the given attributes already.
            final String src = getSrcAttribute();
            if (src != ATTRIBUTE_NOT_DEFINED && !UrlUtils.ABOUT_BLANK.equals(src)) {
                loadSrcWhenAddedToPage_ = true;
            }
        }
    }

    private void init() {
        FrameWindow enclosedWindow = null;
        try {
            final HtmlPage htmlPage = getHtmlPageOrNull();
            if (null != htmlPage) { // if loaded as part of XHR.responseXML, don't load content
                enclosedWindow = new FrameWindow(this);
                // put about:blank in the window to allow JS to run on this frame before the
                // real content is loaded
                final WebClient webClient = htmlPage.getWebClient();
                final HtmlPage temporaryPage = webClient.getPage(enclosedWindow, WebRequest.newAboutBlankRequest());
                temporaryPage.setReadyState(READY_STATE_LOADING);
            }
        }
        catch (final FailingHttpStatusCodeException | IOException e) {
            // should never occur
        }
        enclosedWindow_ = enclosedWindow;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Called after the node for the {@code frame} or {@code iframe} has been added to the containing page.
     * The node needs to be added first to allow JavaScript in the frame to see the frame in the parent.
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *      {@link com.gargoylesoftware.htmlunit.WebClientOptions#setThrowExceptionOnFailingStatusCode(boolean)} is
     *      set to true
     */

    public void loadInnerPage() throws FailingHttpStatusCodeException {
        String source = getSrcAttribute();
        if (source.isEmpty()) {
            source = UrlUtils.ABOUT_BLANK;
        }
        else if (StringUtils.startsWithIgnoreCase(source, UrlUtils.ABOUT_SCHEME)
                && hasFeature(FRAME_LOCATION_ABOUT_BLANK_FOR_ABOUT_SCHEME)) {
            source = UrlUtils.ABOUT_BLANK;
        }

        loadInnerPageIfPossible(source);

        final Page enclosedPage = getEnclosedPage();
        if (enclosedPage != null && enclosedPage.isHtmlPage()) {
            final HtmlPage htmlPage = (HtmlPage) enclosedPage;

            final AbstractJavaScriptEngine<?> jsEngine = getPage().getWebClient().getJavaScriptEngine();
            if (jsEngine != null && jsEngine.isScriptRunning()) {
                final PostponedAction action = new PostponedAction(getPage()) {
                    @Override
                    public void execute() throws Exception {
                        htmlPage.setReadyState(READY_STATE_COMPLETE);
                    }
                };
                jsEngine.addPostponedAction(action);
            }
            else {
                htmlPage.setReadyState(READY_STATE_COMPLETE);
            }
        }
    }

    /**
     * Indicates if the content specified by the {@code src} attribute has been loaded or not.
     * The initial state of a frame contains an "about:blank" that is not loaded like
     * something specified in {@code src} attribute.
     * @return {@code false} if the frame is still in its initial state.
     */
    boolean isContentLoaded() {
        return contentLoaded_;
    }

    /**
     * Changes the state of the {@code contentLoaded_} attribute to true.
     * This is needed, if the content is set from javascript to avoid
     * later overwriting from method com.gargoylesoftware.htmlunit.html.HtmlPage.loadFrames().
     */
    void setContentLoaded() {
        contentLoaded_ = true;
    }

    /**
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *      {@link WebClient#setThrowExceptionOnFailingStatusCode(boolean)} is set to true
     */
    private void loadInnerPageIfPossible(final String src) throws FailingHttpStatusCodeException {
        setContentLoaded();

        String source = src;
        final WebClient webClient = getPage().getWebClient();
        final FrameContentHandler handler = webClient.getFrameContentHandler();
        if (null != handler && !handler.loadFrameDocument(this)) {
            source = UrlUtils.ABOUT_BLANK;
        }

        if (!source.isEmpty()) {
            final URL url;
            try {
                url = ((HtmlPage) getPage()).getFullyQualifiedUrl(source);
            }
            catch (final MalformedURLException e) {
                notifyIncorrectness("Invalid src attribute of " + getTagName() + ": url=[" + source + "]. Ignored.");
                return;
            }

            final WebRequest request = new WebRequest(url);
            request.setCharset(getPage().getCharset());
            request.setRefererlHeader(getPage().getUrl());

            if (isAlreadyLoadedByAncestor(url, request.getCharset())) {
                notifyIncorrectness("Recursive src attribute of " + getTagName() + ": url=[" + source + "]. Ignored.");
                return;
            }
            try {
                webClient.getPage(enclosedWindow_, request);
            }
            catch (final IOException e) {
                if (LOG.isErrorEnabled()) {
                    LOG.error("IOException when getting content for " + getTagName() + ": url=[" + url + "]", e);
                }
            }
        }
    }

    /**
     * Test if the provided URL is the one of one of the parents which would cause an infinite loop.
     * @param url the URL to test
     * @param charset the request charset
     * @return {@code false} if no parent has already this URL
     */
    private boolean isAlreadyLoadedByAncestor(final URL url, final Charset charset) {
        WebWindow window = getPage().getEnclosingWindow();
        int nesting = 0;
        while (window != null && window instanceof FrameWindow) {
            nesting++;
            if (nesting > 9) {
                return true;
            }

            final URL encUrl = UrlUtils.encodeUrl(url,
                    window.getWebClient().getBrowserVersion().hasFeature(URL_MINIMAL_QUERY_ENCODING),
                    charset);
            if (UrlUtils.sameFile(encUrl, window.getEnclosedPage().getUrl())) {
                return true;
            }

            if (window == window.getParentWindow()) {
                // TODO: should getParentWindow() return null on top windows?
                window = null;
            }
            else {
                window = window.getParentWindow();
            }
        }
        return false;
    }

    /**
     * Returns the value of the attribute {@code longdesc}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code longdesc} or an empty string if that attribute isn't defined
     */
    public final String getLongDescAttribute() {
        return getAttributeDirect("longdesc");
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeDirect("name");
    }

    /**
     * Sets the value of the {@code name} attribute.
     *
     * @param name the new window name
     */
    public final void setNameAttribute(final String name) {
        setAttribute("name", name);
    }

    /**
     * Returns the value of the attribute {@code src}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code src} or an empty string if that attribute isn't defined
     */
    public final String getSrcAttribute() {
        return getSrcAttributeNormalized();
    }

    /**
     * Returns the value of the attribute {@code frameborder}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code frameborder} or an empty string if that attribute isn't defined
     */
    public final String getFrameBorderAttribute() {
        return getAttributeDirect("frameborder");
    }

    /**
     * Returns the value of the attribute {@code marginwidth}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code marginwidth} or an empty string if that attribute isn't defined
     */
    public final String getMarginWidthAttribute() {
        return getAttributeDirect("marginwidth");
    }

    /**
     * Returns the value of the attribute {@code marginheight}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code marginheight} or an empty string if that attribute isn't defined
     */
    public final String getMarginHeightAttribute() {
        return getAttributeDirect("marginheight");
    }

    /**
     * Returns the value of the attribute {@code noresize}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code noresize} or an empty string if that attribute isn't defined
     */
    public final String getNoResizeAttribute() {
        return getAttributeDirect("noresize");
    }

    /**
     * Returns the value of the attribute {@code scrolling}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code scrolling} or an empty string if that attribute isn't defined
     */
    public final String getScrollingAttribute() {
        return getAttributeDirect("scrolling");
    }

    /**
     * Returns the value of the attribute {@code onload}. This attribute is not
     * actually supported by the HTML specification however it is supported
     * by the popular browsers.
     *
     * @return the value of the attribute {@code onload} or an empty string if that attribute isn't defined
     */
    public final String getOnLoadAttribute() {
        return getAttributeDirect("onload");
    }

    /**
     * Returns the currently loaded page in the enclosed window.
     * This is a facility method for <code>getEnclosedWindow().getEnclosedPage()</code>.
     * @see WebWindow#getEnclosedPage()
     * @return the currently loaded page in the enclosed window, or {@code null} if no page has been loaded
     */
    public Page getEnclosedPage() {
        return getEnclosedWindow().getEnclosedPage();
    }

    /**
     * Gets the window enclosed in this frame.
     * @return the window enclosed in this frame
     */
    public FrameWindow getEnclosedWindow() {
        return enclosedWindow_;
    }

    /**
     * Sets the value of the {@code src} attribute. Also loads the frame with the specified URL, if possible.
     * @param attribute the new value of the {@code src} attribute
     */
    public final void setSrcAttribute(final String attribute) {
        setAttribute(SRC_ATTRIBUTE, attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObserver) {
        if (null != attributeValue && SRC_ATTRIBUTE.equals(qualifiedName)) {
            attributeValue = attributeValue.trim();
        }

        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObserver);

        // do not use equals() here
        // see HTMLIFrameElement2Test.documentCreateElement_onLoad_srcAboutBlank()
        if (SRC_ATTRIBUTE.equals(qualifiedName) && UrlUtils.ABOUT_BLANK != attributeValue) {
            if (isAttachedToPage()) {
                loadSrc();
            }
            else {
                loadSrcWhenAddedToPage_ = true;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attr setAttributeNode(final Attr attribute) {
        final String qualifiedName = attribute.getName();
        String attributeValue = null;
        if (SRC_ATTRIBUTE.equals(qualifiedName)) {
            attributeValue = attribute.getValue().trim();
        }

        final Attr result = super.setAttributeNode(attribute);

        if (SRC_ATTRIBUTE.equals(qualifiedName) && !UrlUtils.ABOUT_BLANK.equals(attributeValue)) {
            if (isAttachedToPage()) {
                loadSrc();
            }
            else {
                loadSrcWhenAddedToPage_ = true;
            }
        }

        return result;
    }

    private void loadSrc() {
        loadSrcWhenAddedToPage_ = false;
        final String src = getSrcAttribute();

        // recreate a window if the old one was closed
        if (enclosedWindow_.isClosed()) {
            init();
        }

        final AbstractJavaScriptEngine<?> jsEngine = getPage().getWebClient().getJavaScriptEngine();
        // When src is set from a script, loading is postponed until script finishes
        // in fact this implementation is probably wrong: JavaScript URL should be
        // first evaluated and only loading, when any, should be postponed.
        if (jsEngine == null || !jsEngine.isScriptRunning()
                || src.startsWith(JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
            loadInnerPageIfPossible(src);
        }
        else {
            final Page pageInFrame = getEnclosedPage();
            final PostponedAction action = new PostponedAction(getPage()) {
                @Override
                public void execute() throws Exception {
                    if (!src.isEmpty() && getSrcAttribute().equals(src)) {
                        loadInnerPage();
                    }
                }

                @Override
                public boolean isStillAlive() {
                    // skip if page in frame has already been changed
                    return super.isStillAlive() && pageInFrame == getEnclosedPage();
                }
            };
            jsEngine.addPostponedAction(action);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Marks this frame as created by javascript. This is needed to handle
     * some special IE behavior.
     */
    public void markAsCreatedByJavascript() {
        createdByJavascript_ = true;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Unmarks this frame as created by javascript. This is needed to handle
     * some special IE behavior.
     */
    public void unmarkAsCreatedByJavascript() {
        createdByJavascript_ = false;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Returns true if this frame was created by javascript. This is needed to handle
     * some special IE behavior.
     * @return true or false
     */
    public boolean wasCreatedByJavascript() {
        return createdByJavascript_;
    }

    /**
     * Creates a new {@link WebWindow} for the new clone.
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final BaseFrameElement clone = (BaseFrameElement) super.cloneNode(deep);
        clone.init();
        return clone;
    }

    @Override
    protected void onAddedToPage() {
        super.onAddedToPage();

        if (loadSrcWhenAddedToPage_) {
            loadSrc();
        }
    }

    @Override
    public void remove() {
        super.remove();
        loadSrcWhenAddedToPage_ = true;
        getEnclosedWindow().close();
    }

    @Override
    public final void removeAttribute(final String attributeName) {
        super.removeAttribute(attributeName);

        // TODO find a better implementation without all the code duplication
        if (isAttachedToPage()) {
            loadSrcWhenAddedToPage_ = false;
            final String src = getSrcAttribute();

            final AbstractJavaScriptEngine<?> jsEngine = getPage().getWebClient().getJavaScriptEngine();
            // When src is set from a script, loading is postponed until script finishes
            // in fact this implementation is probably wrong: JavaScript URL should be
            // first evaluated and only loading, when any, should be postponed.
            if (jsEngine == null || !jsEngine.isScriptRunning()) {
                loadInnerPageIfPossible(src);
            }
            else {
                final Page pageInFrame = getEnclosedPage();
                final PostponedAction action = new PostponedAction(getPage()) {
                    @Override
                    public void execute() throws Exception {
                        loadInnerPage();
                    }

                    @Override
                    public boolean isStillAlive() {
                        // skip if page in frame has already been changed
                        return super.isStillAlive() && pageInFrame == getEnclosedPage();
                    }
                };
                jsEngine.addPostponedAction(action);
            }
        }
        else {
            loadSrcWhenAddedToPage_ = true;
        }
    }
}
