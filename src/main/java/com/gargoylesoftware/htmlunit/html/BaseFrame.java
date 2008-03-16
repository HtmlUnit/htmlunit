/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindow;

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
 */
public abstract class BaseFrame extends StyledElement {

    private final WebWindow enclosedWindow_ = new FrameWindow(this);

    /**
     * Creates an instance of BaseFrame.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @param page The HtmlPage that contains this element.
     * @param attributes the initial attributes
     */
    protected BaseFrame(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);

        try {
            // put about:blank in the window to allow JS to run on this frame before the
            // real content is loaded
            final WebClient webClient = getPage().getEnclosingWindow().getWebClient();
            webClient.pushClearFirstWindow();
            webClient.getPage(enclosedWindow_, new WebRequestSettings(WebClient.URL_ABOUT_BLANK));
            webClient.popFirstWindow();
        }
        catch (final FailingHttpStatusCodeException e) {
            // should never occur
        }
        catch (final IOException e) {
            // should never occur
        }
    }

    /**
     * Called after the node for <frame...> or <iframe> has been added to the containing page.
     * The node needs to be added first to allow js in the frame to see the frame in the parent
     * @throws FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      {@link WebClient#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     */
    void loadInnerPage() throws FailingHttpStatusCodeException {
        String source = getSrcAttribute();
        if (source.length() == 0) {
            // Nothing to load
            source = "about:blank";
        }
        else {
            getPage().getEnclosingWindow().getWebClient().pushClearFirstWindow();
            loadInnerPageIfPossible(source);
            getPage().getEnclosingWindow().getWebClient().popFirstWindow();
        }
    }

    /**
     * @throws FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      {@link WebClient#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     */
    private void loadInnerPageIfPossible(final String src) throws FailingHttpStatusCodeException {
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
                final WebRequestSettings settings = new WebRequestSettings(url);
                settings.addAdditionalHeader("Referer", getPage().getWebResponse().getUrl().toExternalForm());
                getPage().getEnclosingWindow().getWebClient().getPage(enclosedWindow_, settings);
            }
            catch (final IOException e) {
                getLog().error("IOException when getting content for " + getTagName()
                        + ": url=[" + url.toExternalForm() + "]", e);
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
            if (url.sameFile(window.getEnclosedPage().getWebResponse().getUrl())) {
                return true;
            }
            if (window == window.getParentWindow()) { // should getParentWindow() return null on top windows?
                window = null;
            }
            else {
                window = window.getParentWindow();
            }
        }
        return false;
    }

    /**
     * Returns the value of the attribute "longdesc".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "longdesc"
     * or an empty string if that attribute isn't defined.
     */
    public final String getLongDescAttribute() {
        return getAttributeValue("longdesc");
    }

    /**
     * Returns the value of the attribute "name".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "name"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }

    /**
     * Sets the value of the "name" attribute.
     *
     * @param name The new window name.
     */
    public final void setNameAttribute(final String name) {
        setAttributeValue("name", name);
    }

    /**
     * Returns the value of the attribute "src".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "src"
     * or an empty string if that attribute isn't defined.
     */
    public final String getSrcAttribute() {
        return getAttributeValue("src");
    }

    /**
     * Returns the value of the attribute "frameborder".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "frameborder"
     * or an empty string if that attribute isn't defined.
     */
    public final String getFrameBorderAttribute() {
        return getAttributeValue("frameborder");
    }

    /**
     * Returns the value of the attribute "marginwidth".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "marginwidth"
     * or an empty string if that attribute isn't defined.
     */
    public final String getMarginWidthAttribute() {
        return getAttributeValue("marginwidth");
    }

    /**
     * Returns the value of the attribute "marginheight".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "marginheight"
     * or an empty string if that attribute isn't defined.
     */
    public final String getMarginHeightAttribute() {
        return getAttributeValue("marginheight");
    }

    /**
     * Returns the value of the attribute "noresize".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "noresize"
     * or an empty string if that attribute isn't defined.
     */
    public final String getNoResizeAttribute() {
        return getAttributeValue("noresize");
    }

    /**
     * Returns the value of the attribute "scrolling".  Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return The value of the attribute "scrolling"
     * or an empty string if that attribute isn't defined.
     */
    public final String getScrollingAttribute() {
        return getAttributeValue("scrolling");
    }

    /**
     * Returns the value of the attribute "onload".  This attribute is not
     * actually supported by the HTML specification however it is supported
     * by the popular browsers.
     *
     * @return The value of the attribute "onload"
     * or an empty string if that attribute isn't defined.
     */
    public final String getOnLoadAttribute() {
        return getAttributeValue("onload");
    }

    /**
     * Returns the currently loaded page in the enclosed window.
     * This is a facility method for <code>getEnclosedWindow().getEnclosedPage()</code>.
     * @see WebWindow#getEnclosedPage()
     * @return The currently loaded page in the enclosed window or null if no page has been loaded.
     */
    public Page getEnclosedPage() {
        return getEnclosedWindow().getEnclosedPage();
    }

    /**
     * Gets the window enclosed in this frame.
     * @return the window
     */
    public WebWindow getEnclosedWindow() {
        return enclosedWindow_;
    }

    /**
     * Sets the value of the "src" attribute.  Also load the frame with the specified URL if possible.
     * @param attribute The new value
     */
    public final void setSrcAttribute(final String attribute) {
        setAttributeValue("src", attribute);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setAttributeValue(final String namespaceURI, final String qualifiedName,
            String attributeValue) {
        if (qualifiedName.equals("src")) {
            attributeValue = attributeValue.trim();
        }
        super.setAttributeValue(namespaceURI, qualifiedName, attributeValue);
        if (qualifiedName.equals("src")) {
            loadInnerPageIfPossible(attributeValue);
        }
    }
}
