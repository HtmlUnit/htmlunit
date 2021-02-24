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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLIMAGE_NAME_VALUE_PARAMS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_IMAGE_COMPLETE_RETURNS_TRUE_FOR_NO_REQUEST;

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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Wrapper for the HTML element "input".
 * HtmlUnit does not download the associated image for performance reasons.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlImageInput extends HtmlInput implements LabelableElement {

    private static final Log LOG = LogFactory.getLog(HtmlImageInput.class);

    // For click with x, y position.
    private boolean wasPositionSpecified_;
    private int xPosition_;
    private int yPosition_;
    private WebResponse imageWebResponse_;
    private boolean downloaded_;

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlImageInput(final String qualifiedName, final SgmlPage page, final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NameValuePair[] getSubmitNameValuePairs() {
        final String name = getNameAttribute();
        final String prefix;
        // a clicked image without name sends parameter x and y
        if (StringUtils.isEmpty(name)) {
            prefix = "";
        }
        else {
            prefix = name + ".";
        }

        if (wasPositionSpecified_) {
            final NameValuePair valueX = new NameValuePair(prefix + 'x', Integer.toString(xPosition_));
            final NameValuePair valueY = new NameValuePair(prefix + 'y', Integer.toString(yPosition_));
            if (!prefix.isEmpty() && hasFeature(HTMLIMAGE_NAME_VALUE_PARAMS) && !getValueAttribute().isEmpty()) {
                return new NameValuePair[] {valueX, valueY,
                    new NameValuePair(getNameAttribute(), getValueAttribute()) };
            }
            return new NameValuePair[] {valueX, valueY};
        }
        return new NameValuePair[]{new NameValuePair(getNameAttribute(), getValueAttribute())};
    }

    /**
     * Submit the form that contains this input. Only a couple of the inputs
     * support this method so it is made protected here. Those subclasses
     * that wish to expose it will override and make it public.
     *
     * @return the Page that is the result of submitting this page to the server
     * @exception IOException If an IO error occurs
     */
    @Override
    @SuppressWarnings("unchecked")
    public Page click() throws IOException {
        return click(0, 0);
    }

    /**
     * {@inheritDoc}
     * @throws IOException if an IO error occurred
     */
    @Override
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        final HtmlForm form = getEnclosingForm();
        if (form != null) {
            form.submit(this);
            return false;
        }
        super.doClickStateUpdate(shiftKey, ctrlKey);
        return false;
    }

    /**
     * Simulate clicking this input with a pointing device. The x and y coordinates
     * of the pointing device will be sent to the server.
     *
     * @param <P> the page type
     * @param x the x coordinate of the pointing device at the time of clicking
     * @param y the y coordinate of the pointing device at the time of clicking
     * @return the page that is loaded after the click has taken place
     * @exception IOException If an IO error occurs
     * @exception ElementNotFoundException If a particular XML element could not be found in the DOM model
     */
    public <P extends Page> P click(final int x, final int y) throws IOException, ElementNotFoundException {
        wasPositionSpecified_ = true;
        xPosition_ = x;
        yPosition_ = y;
        return super.click();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Simulates clicking on this element, returning the page in the window that has the focus
     * after the element has been clicked. Note that the returned page may or may not be the same
     * as the original page, depending on the type of element being clicked, the presence of JavaScript
     * action listeners, etc.
     *
     * @param event the click event used
     * @param <P> the page type
     * @return the page contained in the current window as returned by
     *         {@link com.gargoylesoftware.htmlunit.WebClient#getCurrentWindow()}
     * @exception IOException if an IO error occurs
     */
    @Override
    public <P extends Page> P click(final Event event,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey,
            final boolean ignoreVisibility) throws IOException {
        wasPositionSpecified_ = true;
        return super.click(event, shiftKey, ctrlKey, altKey, ignoreVisibility);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc} Also sets the value to the new default value, just like IE.
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        super.setDefaultValue(defaultValue);
        setValueAttribute(defaultValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        if ("value".equals(qualifiedName)) {
            setDefaultValue(attributeValue, false);
        }
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isRequiredSupported() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getSrcAttribute() {
        final String src = getSrcAttributeNormalized();
        if (ATTRIBUTE_NOT_DEFINED == src) {
            return src;
        }

        final HtmlPage page = getHtmlPageOrNull();
        if (page != null) {
            try {
                return page.getFullyQualifiedUrl(src).toExternalForm();
            }
            catch (final MalformedURLException e) {
                // Log the error and fall through to the return values below.
                LOG.warn(e.getMessage(), e);
            }
        }
        return src;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSrcAttribute(final String src) {
        super.setSrcAttribute(src);
        downloaded_ = false;
        imageWebResponse_ = null;
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
            final String src = getSrcAttribute();
            if (!"".equals(src)) {
                final HtmlPage page = (HtmlPage) getPage();
                final WebClient webClient = page.getWebClient();

                final BrowserVersion browser = webClient.getBrowserVersion();
                final WebRequest request = new WebRequest(new URL(src), browser.getImgAcceptHeader(),
                                                                browser.getAcceptEncodingHeader());
                request.setCharset(page.getCharset());
                request.setRefererlHeader(page.getUrl());
                imageWebResponse_ = webClient.loadWebResponse(request);
            }

            downloaded_ = hasFeature(JS_IMAGE_COMPLETE_RETURNS_TRUE_FOR_NO_REQUEST)
                    || (imageWebResponse_ != null && imageWebResponse_.getContentType().contains("image"));
        }
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
    protected boolean propagateClickStateUpdateToParent() {
        return true;
    }
}
