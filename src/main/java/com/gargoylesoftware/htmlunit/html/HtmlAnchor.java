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

import static com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection.JAVASCRIPT_PREFIX;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * Wrapper for the HTML element "a".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Dmitri Zoubkov
 */
public class HtmlAnchor extends ClickableElement {

    private static final long serialVersionUID = 7968778206454737178L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "a";

    private final transient Log mainLog_ = LogFactory.getLog(getClass());
    
    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlAnchor(final String namespaceURI, final String qualifiedName, final HtmlPage page,
            final Map<String, HtmlAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);
    }

    /**
     * Same as {@link #doClickAction(Page)}, except that it accepts an href suffix, needed when a click is
     * performed on an image map to pass information on the click position.
     *
     * @param <P> the type of the page that is currently loaded after execution of this method
     * @param defaultPage the default page to return if the action does not load a new page
     * @param hrefSuffix the suffix to add to the anchor's href attribute (for instance coordinates from an image map)
     * @return the page that is currently loaded after execution of this method
     * @throws IOException if an IO error occurs
     */
    @SuppressWarnings("unchecked")
    protected <P extends Page> P doClickAction(final P defaultPage, final String hrefSuffix) throws IOException {
        final String href = getHrefAttribute() + hrefSuffix;

        if (mainLog_.isDebugEnabled()) {
            mainLog_.debug(
                "do click action in window '"
                        + defaultPage.getEnclosingWindow().getName()
                        + "', using href '" + href + "'");
        }

        if (href.length() > 0 && !href.startsWith("#")) {
            final HtmlPage page = getPage();
            if (TextUtil.startsWithIgnoreCase(href, JAVASCRIPT_PREFIX)) {
                return (P) page.executeJavaScriptIfPossible(
                    href, "javascript url", getStartLineNumber()).getNewPage();
            }
            final URL url = page.getFullyQualifiedUrl(href);
            final WebRequestSettings settings = new WebRequestSettings(url);
            settings.addAdditionalHeader("Referer", page.getWebResponse().getUrl().toExternalForm());
            if (mainLog_.isDebugEnabled()) {
                mainLog_.debug(
                        "Getting page for " + url.toExternalForm()
                        + ", derived from href '" + href
                        + "', using the originating URL "
                        + page.getWebResponse().getUrl());
            }
            return (P) page.getWebClient().getPage(
                    page.getEnclosingWindow(),
                    page.getResolvedTarget(getTargetAttribute()),
                    settings);
        }
        return defaultPage;
    }

    /**
     * This method will be called if there either wasn't an onclick handler or
     * there was but the result of that handler was true. This is the default
     * behavior of clicking the element. For this anchor element, the default
     * behavior is to open the HREF page, or execute the HREF if it is a
     * javascript: URL.
     *
     * @param <P> the type of the page that is currently loaded after execution of this method
     * @param defaultPage the default page to return if the action does not load a new page
     * @return the page that is currently loaded after execution of this method
     * @throws IOException if an IO error occurs
     */
    @Override
    protected <P extends Page> P doClickAction(final P defaultPage) throws IOException {
        return doClickAction(defaultPage, "");
    }

    /**
     * Returns the value of the attribute "charset". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "charset" or an empty string if that attribute isn't defined
     */
    public final String getCharsetAttribute() {
        return getAttributeValue("charset");
    }

    /**
     * Returns the value of the attribute "type". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "type" or an empty string if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        return getAttributeValue("type");
    }

    /**
     * Returns the value of the attribute "name". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "name" or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttributeValue("name");
    }

    /**
     * Returns the value of the attribute "href". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "href" or an empty string if that attribute isn't defined
     */
    public final String getHrefAttribute() {
        return getAttributeValue("href").trim();
    }

    /**
     * Returns the value of the attribute "hreflang". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "hreflang" or an empty string if that attribute isn't defined
     */
    public final String getHrefLangAttribute() {
        return getAttributeValue("hreflang");
    }

    /**
     * Returns the value of the attribute "rel". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "rel" or an empty string if that attribute isn't defined
     */
    public final String getRelAttribute() {
        return getAttributeValue("rel");
    }

    /**
     * Returns the value of the attribute "rev". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "rev" or an empty string if that attribute isn't defined
     */
    public final String getRevAttribute() {
        return getAttributeValue("rev");
    }

    /**
     * Returns the value of the attribute "accesskey". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "accesskey" or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttributeValue("accesskey");
    }

    /**
     * Returns the value of the attribute "shape". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "shape" or an empty string if that attribute isn't defined
     */
    public final String getShapeAttribute() {
        return getAttributeValue("shape");
    }

    /**
     * Returns the value of the attribute "coords". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "coords" or an empty string if that attribute isn't defined
     */
    public final String getCoordsAttribute() {
        return getAttributeValue("coords");
    }

    /**
     * Returns the value of the attribute "tabindex". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "tabindex" or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttributeValue("tabindex");
    }

    /**
     * Returns the value of the attribute "onfocus". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onfocus" or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttributeValue("onfocus");
    }

    /**
     * Returns the value of the attribute "onblur". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onblur" or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttributeValue("onblur");
    }

    /**
     * Returns the value of the attribute "target". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "target" or an empty string if that attribute isn't defined
     */
    public final String getTargetAttribute() {
        return getAttributeValue("target");
    }
    
    /**
     * Open this link in a new window, much as web browsers do when you shift-click a link or use the context
     * menu to open in a new window.
     *
     * It should be noted that even web browsers will sometimes not give the expected result when using this
     * method of following links. Links that have no real href and rely on JavaScript to do their work will fail.
     *
     * @param <P> the type of the page opened by this link
     * @return the page opened by this link, nested in a new {@link com.gargoylesoftware.htmlunit.TopLevelWindow}
     * @throws MalformedURLException if the href could not be converted to a valid URL
     */
    @SuppressWarnings("unchecked")
    public final <P extends Page> P openLinkInNewWindow() throws MalformedURLException {
        final URL target = getPage().getFullyQualifiedUrl(getHrefAttribute());
        final String windowName = "HtmlAnchor.openLinkInNewWindow() target";
        final WebWindow newWindow = getPage().getWebClient().openWindow(target, windowName);
        return (P) newWindow.getEnclosedPage();
    }
}
