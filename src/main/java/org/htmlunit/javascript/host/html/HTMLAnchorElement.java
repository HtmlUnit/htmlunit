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
package org.htmlunit.javascript.host.html;

import static org.htmlunit.BrowserVersionFeatures.JS_ANCHOR_HOSTNAME_IGNORE_BLANK;
import static org.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL;
import static org.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL_REPLACE;
import static org.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL;
import static org.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PROTOCOL_COLON_UPPER_CASE_DRIVE_LETTERS;
import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.HttpHeader;
import org.htmlunit.SgmlPage;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlAnchor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.dom.DOMTokenList;
import org.htmlunit.util.UrlUtils;

/**
 * The JavaScript object that represents an anchor.
 *
 * @author Mike Bowler
 * @author Alexei Goussev
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@JsxClass(domClass = HtmlAnchor.class)
public class HTMLAnchorElement extends HTMLElement {
    private static final List<String> REFERRER_POLICIES = Arrays.asList(
                                        "no-referrer", HttpHeader.ORIGIN_LC, "unsafe-url");

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Sets the {@code href} property.
     * @param href the {@code href} property value
     */
    @JsxSetter
    public void setHref(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of this link's {@code href} property.
     * @return the value of this link's {@code href} property
     */
    @JsxGetter
    public String getHref() {
        final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
        final String hrefAttr = anchor.getHrefAttribute();

        if (ATTRIBUTE_NOT_DEFINED == hrefAttr) {
            return "";
        }

        try {
            return getUrl().toString();
        }
        catch (final MalformedURLException e) {
            return hrefAttr;
        }
    }

    /**
     * Sets the focus to this element.
     */
    @Override
    public void focus() {
        final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
        final String hrefAttr = anchor.getHrefAttribute();

        if (ATTRIBUTE_NOT_DEFINED != hrefAttr) {
            anchor.focus();
        }
    }

    /**
     * Sets the name property.
     * @param name name attribute value
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute(DomElement.NAME_ATTRIBUTE, name);
    }

    /**
     * Returns the value of the name property of this link.
     * @return the name property
     */
    @JsxGetter
    @Override
    public String getName() {
        return getDomNodeOrDie().getAttributeDirect(DomElement.NAME_ATTRIBUTE);
    }

    /**
     * Sets the target property of this link.
     * @param target target attribute value
     */
    @JsxSetter
    public void setTarget(final String target) {
        getDomNodeOrDie().setAttribute("target", target);
    }

    /**
     * Returns the value of the target property of this link.
     * @return the href property
     */
    @JsxGetter
    public String getTarget() {
        return getDomNodeOrDie().getAttributeDirect("target");
    }

    /**
     * Returns this link's current URL.
     * @return this link's current URL
     * @throws MalformedURLException if an error occurs
     */
    private URL getUrl() throws MalformedURLException {
        final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
        return ((HtmlPage) anchor.getPage()).getFullyQualifiedUrl(anchor.getHrefAttribute());
    }

    /**
     * Sets the {@code href} attribute of this link to the specified URL.
     * @param url the new value of the {@code href} attribute
     */
    private void setUrl(final URL url) {
        getDomNodeOrDie().setAttribute("href", url.toString());
    }

    /**
     * Sets the rel property.
     * @param rel rel attribute value
     */
    @JsxSetter
    public void setRel(final String rel) {
        getDomNodeOrDie().setAttribute("rel", rel);
    }

    /**
     * Returns the value of the rel property.
     * @return the rel property
     */
    @JsxGetter
    public String getRel() {
        return ((HtmlAnchor) getDomNodeOrDie()).getRelAttribute();
    }

    /**
     * Returns the value of the rev property.
     * @return the rev property
     */
    @JsxGetter
    public String getRev() {
        return ((HtmlAnchor) getDomNodeOrDie()).getRevAttribute();
    }

    /**
     * Sets the rev property.
     * @param rel rev attribute value
     */
    @JsxSetter
    public void setRev(final String rel) {
        getDomNodeOrDie().setAttribute("rev", rel);
    }

    /**
     * Returns the value of the rev property.
     * @return the referrerPolicy property
     */
    @JsxGetter
    public String getReferrerPolicy() {
        String attrib = getDomNodeOrDie().getAttribute("referrerPolicy");
        if (StringUtils.isEmpty(attrib)) {
            return "";
        }
        attrib = attrib.toLowerCase(Locale.ROOT);
        if (REFERRER_POLICIES.contains(attrib)) {
            return attrib;
        }
        return "";
    }

    /**
     * Sets the rev property.
     * @param referrerPolicy referrerPolicy attribute value
     */
    @JsxSetter
    public void setReferrerPolicy(final String referrerPolicy) {
        getDomNodeOrDie().setAttribute("referrerPolicy", referrerPolicy);
    }

    /**
     * Returns the search portion of the link's URL (the portion starting with
     * '?' and up to but not including any '#').
     * @return the search portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534620.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getSearch() {
        try {
            final String query = getUrl().getQuery();
            if (query == null) {
                return "";
            }
            return "?" + query;
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the search portion of the link's URL (the portion starting with '?'
     * and up to but not including any '#').
     * @param search the new search portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534620.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setSearch(final String search) throws Exception {
        final String query;
        if (search == null
                || org.htmlunit.util.StringUtils.isEmptyString(search)
                || org.htmlunit.util.StringUtils.equalsChar('?', search)) {
            query = null;
        }
        else if (search.charAt(0) == '?') {
            query = search.substring(1);
        }
        else {
            query = search;
        }

        setUrl(UrlUtils.getUrlWithNewQuery(getUrl(), query));
    }

    /**
     * Returns the hash portion of the link's URL (the portion following the '#', including the '#').
     * @return the hash portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getHash() {
        try {
            final String hash = getUrl().getRef();
            if (hash == null) {
                return "";
            }
            return "#" + hash;
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the hash portion of the link's URL (the portion following the '#').
     * @param hash the new hash portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setHash(final String hash) throws Exception {
        setUrl(UrlUtils.getUrlWithNewRef(getUrl(), hash));
    }

    /**
     * Returns the host portion of the link's URL (the '[hostname]:[port]' portion).
     * @return the host portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533784.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getHost() {
        try {
            final URL url = getUrl();
            final int port = url.getPort();
            final String host = url.getHost();

            if (port == -1) {
                return host;
            }
            return host + ":" + port;
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the host portion of the link's URL (the '[hostname]:[port]' portion).
     * @param host the new host portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533784.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setHost(final String host) throws Exception {
        final String hostname;
        final int port;
        final int index = host.indexOf(':');
        if (index != -1) {
            hostname = host.substring(0, index);
            port = Integer.parseInt(host.substring(index + 1));
        }
        else {
            hostname = host;
            port = -1;
        }
        final URL url = UrlUtils.getUrlWithNewHostAndPort(getUrl(), hostname, port);
        setUrl(url);
    }

    /**
     * Returns the hostname portion of the link's URL.
     * @return the hostname portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533785.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getHostname() {
        try {
            return UrlUtils.encodeAnchor(getUrl().getHost());
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the hostname portion of the link's URL.
     * @param hostname the new hostname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533785.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setHostname(final String hostname) throws Exception {
        if (getBrowserVersion().hasFeature(JS_ANCHOR_HOSTNAME_IGNORE_BLANK)) {
            if (!StringUtils.isBlank(hostname)) {
                setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
            }
        }
        else if (!StringUtils.isEmpty(hostname)) {
            setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
        }
    }

    /**
     * Returns the pathname portion of the link's URL.
     * @return the pathname portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534332.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getPathname() {
        final BrowserVersion browser = getBrowserVersion();
        try {
            if (browser.hasFeature(JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL_REPLACE)) {
                final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
                String href = anchor.getHrefAttribute();
                if (href.length() > 1 && Character.isLetter(href.charAt(0)) && ':' == href.charAt(1)) {
                    if (browser.hasFeature(JS_ANCHOR_PROTOCOL_COLON_UPPER_CASE_DRIVE_LETTERS)) {
                        href = StringUtils.capitalize(href);
                    }
                    if (browser.hasFeature(JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL)) {
                        href = "/" + href;
                    }
                    return href;
                }
            }
            else if (browser.hasFeature(JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL)) {
                final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
                final String href = anchor.getHrefAttribute();
                if (href.length() > 1 && Character.isLetter(href.charAt(0)) && ':' == href.charAt(1)) {
                    return href.substring(2);
                }
            }
            return getUrl().getPath();
        }
        catch (final MalformedURLException e) {
            final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
            if (anchor.getHrefAttribute().startsWith("http")) {
                return "";
            }
            return "/";
        }
    }

    /**
     * Sets the pathname portion of the link's URL.
     * @param pathname the new pathname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534332.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setPathname(final String pathname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPath(getUrl(), pathname));
    }

    /**
     * Returns the port portion of the link's URL.
     * @return the port portion of the link's URL
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534342.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getPort() {
        try {
            final int port = getUrl().getPort();
            if (port == -1) {
                return "";
            }
            return Integer.toString(port);
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the port portion of the link's URL.
     * @param port the new port portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534342.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setPort(final String port) throws Exception {
        setUrl(UrlUtils.getUrlWithNewPort(getUrl(), Integer.parseInt(port)));
    }

    /**
     * Returns the protocol portion of the link's URL, including the trailing ':'.
     * @return the protocol portion of the link's URL, including the trailing ':'
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getProtocol() {
        final BrowserVersion browser = getBrowserVersion();
        try {
            if (browser.hasFeature(JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL_REPLACE)) {
                final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
                final String href = anchor.getHrefAttribute().toLowerCase(Locale.ROOT);
                if (href.length() > 1 && Character.isLetter(href.charAt(0)) && ':' == href.charAt(1)) {
                    return "file:";
                }
            }

            return getUrl().getProtocol() + ":";
        }
        catch (final MalformedURLException e) {
            final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
            if (anchor.getHrefAttribute().startsWith("http")) {
                return ":";
            }
            return StringUtils.substringBefore(anchor.getHrefAttribute(), "/");
        }
    }

    /**
     * Sets the protocol portion of the link's URL.
     * @param protocol the new protocol portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setProtocol(final String protocol) throws Exception {
        if (protocol.isEmpty()) {
            return;
        }

        final String bareProtocol = StringUtils.substringBefore(protocol, ":").trim();
        if (!UrlUtils.isValidScheme(bareProtocol)) {
            return;
        }
        if (!UrlUtils.isSpecialScheme(bareProtocol)) {
            return;
        }

        try {
            URL url = UrlUtils.getUrlWithNewProtocol(getUrl(), bareProtocol);
            url = UrlUtils.removeRedundantPort(url);
            setUrl(url);
        }
        catch (final MalformedURLException ignored) {
            // ignore
        }
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see org.htmlunit.javascript.HtmlUnitScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    @Override
    public Object getDefaultValue(final Class<?> hint) {
        final HtmlElement element = getDomNodeOrNull();
        if (element == null) {
            return super.getDefaultValue(null);
        }
        return getDefaultValue(element);
    }

    static String getDefaultValue(final HtmlElement element) {
        String href = element.getAttributeDirect("href");

        if (ATTRIBUTE_NOT_DEFINED == href) {
            return ""; // for example for named anchors
        }

        href = href.trim();

        final SgmlPage page = element.getPage();
        if (page == null || !page.isHtmlPage()) {
            return href;
        }

        try {
            return HtmlAnchor.getTargetUrl(href, (HtmlPage) page).toExternalForm();
        }
        catch (final MalformedURLException e) {
            return href;
        }
    }

    /**
     * Returns the {@code text} attribute.
     * @return the {@code text} attribute
     */
    @JsxGetter
    public String getText() {
        final DomNode htmlElement = getDomNodeOrDie();
        return htmlElement.asNormalizedText();
    }

    /**
     * Sets the {@code text} attribute.
     * @param text the {@code text} attribute
     */
    @JsxSetter
    public void setText(final String text) {
        final DomNode htmlElement = getDomNodeOrDie();
        htmlElement.setTextContent(text);
    }

    /**
     * Returns the {@code charset} attribute.
     * @return the {@code charset} attribute
     */
    @JsxGetter
    public String getCharset() {
        return getDomNodeOrDie().getAttributeDirect("charset");
    }

    /**
     * Sets the {@code charset} attribute.
     * @param charset the {@code charset} attribute
     */
    @JsxSetter
    public void setCharset(final String charset) {
        getDomNodeOrDie().setAttribute("charset", charset);
    }

    /**
     * Returns the {@code coords} attribute.
     * @return the {@code coords} attribute
     */
    @JsxGetter
    public String getCoords() {
        return getDomNodeOrDie().getAttributeDirect("coords");
    }

    /**
     * Sets the {@code coords} attribute.
     * @param coords {@code coords} attribute
     */
    @JsxSetter
    public void setCoords(final String coords) {
        getDomNodeOrDie().setAttribute("coords", coords);
    }

    /**
     * Returns the {@code hreflang} attribute.
     * @return the {@code hreflang} attribute
     */
    @JsxGetter
    public String getHreflang() {
        return getDomNodeOrDie().getAttributeDirect("hreflang");
    }

    /**
     * Sets the {@code hreflang} attribute.
     * @param hreflang {@code hreflang} attribute
     */
    @JsxSetter
    public void setHreflang(final String hreflang) {
        getDomNodeOrDie().setAttribute("hreflang", hreflang);
    }

    /**
     * Returns the {@code origin} attribute.
     * @return the {@code origin} attribute
     */
    @JsxGetter
    public String getOrigin() {
        if (!getDomNodeOrDie().hasAttribute("href")) {
            return "";
        }

        try {
            return getUrl().getProtocol() + "://" + getHost();
        }
        catch (final Exception e) {
            return "";
        }
    }

    /**
     * Returns the {@code username} attribute.
     * @return the {@code username} attribute
     */
    @JsxGetter
    public String getUsername() {
        try {
            final String userInfo = getUrl().getUserInfo();
            if (userInfo == null) {
                return "";
            }
            return StringUtils.substringBefore(userInfo, ':');
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the {@code username} attribute.
     * @param username {@code username} attribute
     */
    @JsxSetter
    public void setUsername(final String username) {
        try {
            final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
            final String href = anchor.getHrefAttribute();
            if (ATTRIBUTE_NOT_DEFINED == href) {
                return;
            }

            final URL url = ((HtmlPage) anchor.getPage()).getFullyQualifiedUrl(href);
            setUrl(UrlUtils.getUrlWithNewUserName(url, username));
        }
        catch (final MalformedURLException ignored) {
            // ignore
        }
    }

    /**
     * Returns the {@code password} attribute.
     * @return the {@code password} attribute
     */
    @JsxGetter
    public String getPassword() {
        try {
            final String userName = getUrl().getUserInfo();
            if (userName == null) {
                return "";
            }
            return StringUtils.substringAfter(userName, ":");
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the {@code password} attribute.
     * @param password {@code password} attribute
     */
    @JsxSetter
    public void setPassword(final String password) {
        try {
            final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
            final String href = anchor.getHrefAttribute();
            if (ATTRIBUTE_NOT_DEFINED == href) {
                return;
            }

            final URL url = ((HtmlPage) anchor.getPage()).getFullyQualifiedUrl(href);
            setUrl(UrlUtils.getUrlWithNewUserPassword(url, password));
        }
        catch (final MalformedURLException ignored) {
            // ignore
        }
    }

    /**
     * Returns the {@code download} attribute.
     * @return the {@code download} attribute
     */
    @JsxGetter
    public String getDownload() {
        return ((HtmlAnchor) getDomNodeOrDie()).getDownloadAttribute();
    }

    /**
     * Sets the {@code download} attribute.
     * @param download {@code download} attribute
     */
    @JsxSetter
    public void setDownload(final String download) {
        getDomNodeOrDie().setAttribute("download", download);
    }

    /**
     * Returns the {@code ping} attribute.
     * @return the {@code ping} attribute
     */
    @JsxGetter
    public String getPing() {
        return ((HtmlAnchor) getDomNodeOrDie()).getPingAttribute();
    }

    /**
     * Sets the {@code ping} attribute.
     * @param ping {@code ping} attribute
     */
    @JsxSetter
    public void setPing(final String ping) {
        getDomNodeOrDie().setAttribute("ping", ping);
    }

    /**
     * Returns the {@code shape} attribute.
     * @return the {@code shape} attribute
     */
    @JsxGetter
    public String getShape() {
        return getDomNodeOrDie().getAttribute("shape");
    }

    /**
     * Sets the {@code shape} attribute.
     * @param shape {@code shape} attribute
     */
    @JsxSetter
    public void setShape(final String shape) {
        getDomNodeOrDie().setAttribute("shape", shape);
    }

    /**
     * Returns the {@code type} attribute.
     * @return the {@code type} attribute
     */
    @JsxGetter
    public String getType() {
        return getDomNodeOrDie().getAttributeDirect(DomElement.TYPE_ATTRIBUTE);
    }

    /**
     * Sets the {@code type} attribute.
     * @param type {@code type} attribute
     */
    @JsxSetter
    public void setType(final String type) {
        getDomNodeOrDie().setAttribute(DomElement.TYPE_ATTRIBUTE, type);
    }

    /**
     * Returns the {@code relList} attribute.
     * @return the {@code relList} attribute
     */
    @JsxGetter
    public DOMTokenList getRelList() {
        return new DOMTokenList(this, "rel");
    }

    /**
     * Sets the relList property.
     * @param rel attribute value
     */
    @JsxSetter
    public void setRelList(final Object rel) {
        setRel(JavaScriptEngine.toString(rel));
    }
}
