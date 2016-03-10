/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.ANCHOR_EMPTY_HREF_NO_FILENAME;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.ANCHOR_IGNORE_TARGET_FOR_JS_HREF;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.protocol.javascript.JavaScriptURLConnection;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * Wrapper for the HTML element "a".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Dmitri Zoubkov
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlAnchor extends HtmlElement {

    private static final Log LOG = LogFactory.getLog(HtmlAnchor.class);

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "a";

    /**
     * Creates a new instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlAnchor(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
    }

    /**
     * Same as {@link #doClickStateUpdate()}, except that it accepts an {@code href} suffix, needed when a click is
     * performed on an image map to pass information on the click position.
     *
     * @param hrefSuffix the suffix to add to the anchor's {@code href} attribute
     *        (for instance coordinates from an image map)
     * @throws IOException if an IO error occurs
     */
    protected void doClickStateUpdate(final String hrefSuffix) throws IOException {
        final String href = (getHrefAttribute() + hrefSuffix).trim();
        if (LOG.isDebugEnabled()) {
            final String w = getPage().getEnclosingWindow().getName();
            LOG.debug("do click action in window '" + w + "', using href '" + href + "'");
        }
        if (ATTRIBUTE_NOT_DEFINED == getHrefAttribute()) {
            return;
        }
        HtmlPage page = (HtmlPage) getPage();
        if (StringUtils.startsWithIgnoreCase(href, JavaScriptURLConnection.JAVASCRIPT_PREFIX)) {
            final StringBuilder builder = new StringBuilder(href.length());
            builder.append(JavaScriptURLConnection.JAVASCRIPT_PREFIX);
            for (int i = JavaScriptURLConnection.JAVASCRIPT_PREFIX.length(); i < href.length(); i++) {
                final char ch = href.charAt(i);
                if (ch == '%' && i + 2 < href.length()) {
                    final char ch1 = Character.toUpperCase(href.charAt(i + 1));
                    final char ch2 = Character.toUpperCase(href.charAt(i + 2));
                    if ((Character.isDigit(ch1) || ch1 >= 'A' && ch1 <= 'F')
                            && (Character.isDigit(ch2) || ch2 >= 'A' && ch2 <= 'F')) {
                        builder.append((char) Integer.parseInt(href.substring(i + 1, i + 3), 16));
                        i += 2;
                        continue;
                    }
                }
                builder.append(ch);
            }

            if (hasFeature(ANCHOR_IGNORE_TARGET_FOR_JS_HREF)) {
                page.executeJavaScriptIfPossible(builder.toString(), "javascript url", getStartLineNumber());
            }
            else {
                final WebWindow win = page.getWebClient().openTargetWindow(page.getEnclosingWindow(),
                        page.getResolvedTarget(getTargetAttribute()), "_self");
                final Page enclosedPage = win.getEnclosedPage();
                if (enclosedPage != null && enclosedPage.isHtmlPage()) {
                    page = (HtmlPage) enclosedPage;
                    page.executeJavaScriptIfPossible(builder.toString(), "javascript url", getStartLineNumber());
                }
            }
            return;
        }

        final URL url = getTargetUrl(href, page);

        final BrowserVersion browser = page.getWebClient().getBrowserVersion();
        if (ATTRIBUTE_NOT_DEFINED != getPingAttribute() && browser.hasFeature(ANCHOR_IGNORE_TARGET_FOR_JS_HREF)) {
            final URL pingUrl = getTargetUrl(getPingAttribute(), page);
            final WebRequest pingRequest = new WebRequest(pingUrl, HttpMethod.POST);
            pingRequest.setAdditionalHeader("Ping-From", page.getUrl().toExternalForm());
            pingRequest.setAdditionalHeader("Ping-To", url.toExternalForm());
            pingRequest.setRequestBody("PING");
            page.getWebClient().loadWebResponse(pingRequest);
        }

        final WebRequest webRequest = new WebRequest(url, browser.getHtmlAcceptHeader());
        webRequest.setCharset(page.getPageEncoding());
        webRequest.setAdditionalHeader("Referer", page.getUrl().toExternalForm());
        if (LOG.isDebugEnabled()) {
            LOG.debug(
                    "Getting page for " + url.toExternalForm()
                    + ", derived from href '" + href
                    + "', using the originating URL "
                    + page.getUrl());
        }
        page.getWebClient().download(page.getEnclosingWindow(),
                page.getResolvedTarget(getTargetAttribute()),
                webRequest, true, false, "Link click");
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * @param href the href
     * @param page the HtmlPage
     * @return the calculated target url.
     * @throws MalformedURLException if an IO error occurs
     */
    public static URL getTargetUrl(final String href, final HtmlPage page) throws MalformedURLException {
        URL url = page.getFullyQualifiedUrl(href);
        // fix for empty url
        if (StringUtils.isEmpty(href)) {
            final boolean dropFilename = page.getWebClient().getBrowserVersion()
                    .hasFeature(ANCHOR_EMPTY_HREF_NO_FILENAME);
            if (dropFilename) {
                String path = url.getPath();
                path = path.substring(0, path.lastIndexOf('/') + 1);
                url = UrlUtils.getUrlWithNewPath(url, path);
                url = UrlUtils.getUrlWithNewRef(url, null);
            }
            else {
                url = UrlUtils.getUrlWithNewRef(url, null);
            }
        }
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate() throws IOException {
        doClickStateUpdate("");
        return false;
    }

    /**
     * Returns the value of the attribute {@code charset}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code charset} or an empty string if that attribute isn't defined
     */
    public final String getCharsetAttribute() {
        return getAttribute("charset");
    }

    /**
     * Returns the value of the attribute {@code type}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code type} or an empty string if that attribute isn't defined
     */
    public final String getTypeAttribute() {
        return getAttribute("type");
    }

    /**
     * Returns the value of the attribute {@code name}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code name} or an empty string if that attribute isn't defined
     */
    public final String getNameAttribute() {
        return getAttribute("name");
    }

    /**
     * Returns the value of the attribute {@code href}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code href} or an empty string if that attribute isn't defined
     */
    public final String getHrefAttribute() {
        return getAttribute("href").trim();
    }

    /**
     * Returns the value of the attribute {@code hreflang}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code hreflang} or an empty string if that attribute isn't defined
     */
    public final String getHrefLangAttribute() {
        return getAttribute("hreflang");
    }

    /**
     * Returns the value of the attribute {@code rel}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rel} or an empty string if that attribute isn't defined
     */
    public final String getRelAttribute() {
        return getAttribute("rel");
    }

    /**
     * Returns the value of the attribute {@code rev}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code rev} or an empty string if that attribute isn't defined
     */
    public final String getRevAttribute() {
        return getAttribute("rev");
    }

    /**
     * Returns the value of the attribute {@code accesskey}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code accesskey} or an empty string if that attribute isn't defined
     */
    public final String getAccessKeyAttribute() {
        return getAttribute("accesskey");
    }

    /**
     * Returns the value of the attribute {@code shape}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code shape} or an empty string if that attribute isn't defined
     */
    public final String getShapeAttribute() {
        return getAttribute("shape");
    }

    /**
     * Returns the value of the attribute {@code coords}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code coords} or an empty string if that attribute isn't defined
     */
    public final String getCoordsAttribute() {
        return getAttribute("coords");
    }

    /**
     * Returns the value of the attribute {@code tabindex}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code tabindex} or an empty string if that attribute isn't defined
     */
    public final String getTabIndexAttribute() {
        return getAttribute("tabindex");
    }

    /**
     * Returns the value of the attribute {@code onfocus}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onfocus} or an empty string if that attribute isn't defined
     */
    public final String getOnFocusAttribute() {
        return getAttribute("onfocus");
    }

    /**
     * Returns the value of the attribute {@code onblur}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code onblur} or an empty string if that attribute isn't defined
     */
    public final String getOnBlurAttribute() {
        return getAttribute("onblur");
    }

    /**
     * Returns the value of the attribute {@code target}. Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute {@code target} or an empty string if that attribute isn't defined
     */
    public final String getTargetAttribute() {
        return getAttribute("target");
    }

    /**
     * Open this link in a new window, much as web browsers do when you shift-click a link or use the context
     * menu to open in a new window.
     *
     * It should be noted that even web browsers will sometimes not give the expected result when using this
     * method of following links. Links that have no real href and rely on JavaScript to do their work will
     * fail.
     *
     * @return the page opened by this link, nested in a new {@link com.gargoylesoftware.htmlunit.TopLevelWindow}
     * @throws MalformedURLException if the href could not be converted to a valid URL
     */
    public final Page openLinkInNewWindow() throws MalformedURLException {
        final URL target = ((HtmlPage) getPage()).getFullyQualifiedUrl(getHrefAttribute());
        final String windowName = "HtmlAnchor.openLinkInNewWindow() target";
        final WebWindow newWindow = getPage().getWebClient().openWindow(target, windowName);
        return newWindow.getEnclosedPage();
    }

    @Override
    protected boolean isEmptyXmlTagExpanded() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DisplayStyle getDefaultStyleDisplay() {
        return DisplayStyle.INLINE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(final Event event) {
        if (Event.TYPE_BLUR.equals(event.getType()) || Event.TYPE_FOCUS.equals(event.getType())) {
            return true;
        }
        return super.handles(event);
    }

    /**
     * Returns the value of the attribute {@code ping}.
     *
     * @return the value of the attribute {@code ping}
     */
    public final String getPingAttribute() {
        return getAttribute("ping");
    }

}
