/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;

/**
 * Base class for frame and iframe.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author David D. Kilzer
 * @author Stefan Anzinger
 * @author Ahmed Ashour
 * @author Dmitri Zoubkov
 * @author Daniel Gredler
 */
public abstract class BaseFrame extends HtmlElement {

    private static final Log LOG = LogFactory.getLog(BaseFrame.class);
    private final WebWindow enclosedWindow_;
    private boolean contentLoaded_ = false;

    /**
     * Creates an instance of BaseFrame.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    protected BaseFrame(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);

        WebWindow enclosedWindow = null;
        try {
            if (getPage() instanceof HtmlPage) { // if loaded as part of XHR.responseXML, don't load content
                enclosedWindow = new FrameWindow(this);
                // put about:blank in the window to allow JS to run on this frame before the
                // real content is loaded
                final WebClient webClient = getPage().getEnclosingWindow().getWebClient();
                final HtmlPage temporaryPage = webClient.getPage(enclosedWindow,
                    new WebRequest(WebClient.URL_ABOUT_BLANK));
                temporaryPage.setReadyState(READY_STATE_LOADING);
            }
        }
        catch (final FailingHttpStatusCodeException e) {
            // should never occur
        }
        catch (final IOException e) {
            // should never occur
        }
        enclosedWindow_ = enclosedWindow;
    }

    /**
     * Called after the node for the &lt;frame&gt; or &lt;iframe&gt; has been added to the containing page.
     * The node needs to be added first to allow JavaScript in the frame to see the frame in the parent.
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *      {@link WebClient#setThrowExceptionOnFailingStatusCode(boolean)} is set to true
     */
    void loadInnerPage() throws FailingHttpStatusCodeException {
        String source = getSrcAttribute();
        if (source.length() == 0) {
            source = "about:blank";
        }

        loadInnerPageIfPossible(source);

        final Page enclosedPage = getEnclosedPage();
        if (enclosedPage instanceof HtmlPage) {
            ((HtmlPage) enclosedPage).setReadyState(READY_STATE_COMPLETE);
        }
    }

    /**
     * Indicates if the content specified by the src attribute has been loaded or not.
     * The initial state of a frame contains an "about:blank" that is not loaded like
     * something specified in src attribute.
     * @return <code>false</code> if the frame is still in its initial state.
     */
    boolean isContentLoaded() {
        return contentLoaded_;
    }

    /**
     * @throws FailingHttpStatusCodeException if the server returns a failing status code AND the property
     *      {@link WebClient#setThrowExceptionOnFailingStatusCode(boolean)} is set to true
     */
    private void loadInnerPageIfPossible(final String src) throws FailingHttpStatusCodeException {
        contentLoaded_ = true;
        if (src.length() != 0) {
            final URL url;
            try {
                url = ((HtmlPage) getPage()).getFullyQualifiedUrl(src);
            }
            catch (final MalformedURLException e) {
                notifyIncorrectness("Invalid src attribute of " + getTagName() + ": url=[" + src + "]. Ignored.");
                return;
            }
            if (isAlreadyLoadedByAncestor(url)) {
                notifyIncorrectness("Recursive src attribute of " + getTagName() + ": url=[" + src + "]. Ignored.");
                return;
            }
            try {
                final WebRequest request = new WebRequest(url);
                request.setAdditionalHeader("Referer", getPage().getWebResponse().getWebRequest().getUrl()
                        .toExternalForm());
                getPage().getEnclosingWindow().getWebClient().getPage(enclosedWindow_, request);
            }
            catch (final IOException e) {
                LOG.error("IOException when getting content for " + getTagName() + ": url=[" + url + "]", e);
            }
        }
    }

    /**
     * Test if the provided URL is the one of one of the parents which would cause an infinite loop.
     * @param url the URL to test
     * @return <code>false</code> if no parent has already this URL
     */
    private boolean isAlreadyLoadedByAncestor(final URL url) {
        WebWindow window = getPage().getEnclosingWindow();
        while (window != null) {
            if (url.sameFile(window.getEnclosedPage().getWebResponse().getWebRequest().getUrl())) {
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
     * Returns the value of the attribute "longdesc". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "longdesc" or an empty string if that attribute isn't defined
     */
    public final String getLongDescAttribute() {
        return getAttribute("longdesc");
    }

    /**
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name" or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Sets the value of the "name" attribute.
     *
     * @param name the new window name
     */
    public final void setNameAttribute(final String name) {
        setAttribute("name", name);
    }

    /**
     * Returns the value of the attribute "src". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "src" or an empty string if that attribute isn't defined
     */
    public final String getSrcAttribute() {
        return getAttribute("src");
    }

    /**
     * Returns the value of the attribute "frameborder". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "frameborder" or an empty string if that attribute isn't defined
     */
    public final String getFrameBorderAttribute() {
        return getAttribute("frameborder");
    }

    /**
     * Returns the value of the attribute "marginwidth". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "marginwidth" or an empty string if that attribute isn't defined
     */
    public final String getMarginWidthAttribute() {
        return getAttribute("marginwidth");
    }

    /**
     * Returns the value of the attribute "marginheight". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "marginheight" or an empty string if that attribute isn't defined
     */
    public final String getMarginHeightAttribute() {
        return getAttribute("marginheight");
    }

    /**
     * Returns the value of the attribute "noresize". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "noresize" or an empty string if that attribute isn't defined
     */
    public final String getNoResizeAttribute() {
        return getAttribute("noresize");
    }

    /**
     * Returns the value of the attribute "scrolling". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "scrolling" or an empty string if that attribute isn't defined
     */
    public final String getScrollingAttribute() {
        return getAttribute("scrolling");
    }

    /**
     * Returns the value of the attribute "onload". This attribute is not
     * actually supported by the HTML specification however it is supported
     * by the popular browsers.
     *
     * @return the value of the attribute "onload" or an empty string if that attribute isn't defined
     */
    public final String getOnLoadAttribute() {
        return getAttribute("onload");
    }

    /**
     * Returns the currently loaded page in the enclosed window.
     * This is a facility method for <code>getEnclosedWindow().getEnclosedPage()</code>.
     * @see WebWindow#getEnclosedPage()
     * @return the currently loaded page in the enclosed window, or <tt>null</tt> if no page has been loaded
     */
    public Page getEnclosedPage() {
        return getEnclosedWindow().getEnclosedPage();
    }

    /**
     * Gets the window enclosed in this frame.
     * @return the window enclosed in this frame
     */
    public WebWindow getEnclosedWindow() {
        return enclosedWindow_;
    }

    /**
     * Sets the value of the "src" attribute. Also loads the frame with the specified URL, if possible.
     * @param attribute the new value of the "src" attribute
     */
    public final void setSrcAttribute(final String attribute) {
        setAttribute("src", attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName,  String attributeValue) {
        if (qualifiedName.equals("src")) {
            attributeValue = attributeValue.trim();
        }

        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue);

        if (qualifiedName.equals("src")) {
            final JavaScriptEngine jsEngine = getPage().getWebClient().getJavaScriptEngine();
            // When src is set from a script, loading is postponed until script finishes
            // in fact this implementation is probably wrong: JavaScript URL should be
            // first evaluated and only loading, when any, should be postponed.
            if (!jsEngine.isScriptRunning() || attributeValue.startsWith("javascript:")) {
                loadInnerPageIfPossible(attributeValue);
            }
            else {
                final String src = attributeValue;
                final PostponedAction action = new PostponedAction(getPage()) {
                    @Override
                    public void execute() throws Exception {
                        if (getSrcAttribute().equals(src)) {
                            loadInnerPage();
                        }
                    }
                };
                jsEngine.addPostponedAction(action);
            }
        }
    }

}
