/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PATHNAME_DETECT_WIN_DRIVES_URL_REPLACE;
import static org.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL;
import static org.htmlunit.BrowserVersionFeatures.JS_ANCHOR_PROTOCOL_COLON_UPPER_CASE_DRIVE_LETTERS;
import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
import org.htmlunit.util.StringUtils;
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
 * @author Lai Quang Duong
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement">MDN Documentation</a>
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
     * Sets the {@code name} property.
     * @param name the {@code name} property value
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute(DomElement.NAME_ATTRIBUTE, name);
    }

    /**
     * Returns the value of this link's {@code name} property.
     * @return the value of this link's {@code name} property
     */
    @JsxGetter
    @Override
    public String getName() {
        return getDomNodeOrDie().getAttributeDirect(DomElement.NAME_ATTRIBUTE);
    }

    /**
     * Sets the {@code target} property of this link.
     * @param target the {@code target} property value
     */
    @JsxSetter
    public void setTarget(final String target) {
        getDomNodeOrDie().setAttribute("target", target);
    }

    /**
     * Returns the value of this link's {@code target} property.
     * @return the value of this link's {@code target} property
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
     * Sets the {@code rel} property.
     * @param rel the {@code rel} property value
     */
    @JsxSetter
    public void setRel(final String rel) {
        getDomNodeOrDie().setAttribute("rel", rel);
    }

    /**
     * Returns the value of this link's {@code rel} property.
     * @return the value of this link's {@code rel} property
     */
    @JsxGetter
    public String getRel() {
        return ((HtmlAnchor) getDomNodeOrDie()).getRelAttribute();
    }

    /**
     * Returns the value of this link's {@code rev} property.
     * @return the value of this link's {@code rev} property
     */
    @JsxGetter
    public String getRev() {
        return ((HtmlAnchor) getDomNodeOrDie()).getRevAttribute();
    }

    /**
     * Sets the {@code rev} property.
     * @param rel the {@code rev} property value
     */
    @JsxSetter
    public void setRev(final String rel) {
        getDomNodeOrDie().setAttribute("rev", rel);
    }

    /**
     * Returns the value of this link's {@code referrerPolicy} property.
     * @return the value of this link's {@code referrerPolicy} property
     */
    @JsxGetter
    public String getReferrerPolicy() {
        String attrib = getDomNodeOrDie().getAttribute("referrerPolicy");
        if (StringUtils.isEmptyOrNull(attrib)) {
            return "";
        }
        attrib = attrib.toLowerCase(Locale.ROOT);
        if (REFERRER_POLICIES.contains(attrib)) {
            return attrib;
        }
        return "";
    }

    /**
     * Sets the {@code referrerPolicy} property.
     * @param referrerPolicy the {@code referrerPolicy} property value
     */
    @JsxSetter
    public void setReferrerPolicy(final String referrerPolicy) {
        getDomNodeOrDie().setAttribute("referrerPolicy", referrerPolicy);
    }

    /**
     * Returns the search portion of the link's URL (the portion starting with
     * '?' and up to but not including any '#').
     * @return the search portion of the link's URL
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/search">MDN Documentation</a>
     */
    @JsxGetter
    public String getSearch() {
        try {
            return HTMLHyperlinkElementUtils.getSearch(getUrl());
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
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/search">MDN Documentation</a>
     */
    @JsxSetter
    public void setSearch(final String search) throws Exception {
        setUrl(HTMLHyperlinkElementUtils.setSearch(getUrl(), search));
    }

    /**
     * Returns the hash portion of the link's URL (the portion following the '#', including the '#').
     * @return the hash portion of the link's URL
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/hash">MDN Documentation</a>
     */
    @JsxGetter
    public String getHash() {
        try {
            return HTMLHyperlinkElementUtils.getHash(getUrl());
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the hash portion of the link's URL (the portion following the '#').
     * @param hash the new hash portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/hash">MDN Documentation</a>
     */
    @JsxSetter
    public void setHash(final String hash) throws Exception {
        setUrl(HTMLHyperlinkElementUtils.setHash(getUrl(), hash));
    }

    /**
     * Returns the host portion of the link's URL (the '[hostname]:[port]' portion).
     * @return the host portion of the link's URL
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/host">MDN Documentation</a>
     */
    @JsxGetter
    public String getHost() {
        try {
            final URL url = getUrl();
            final int port = url.getPort();
            final String host = url.getHost();

            if (port == -1 || HTMLHyperlinkElementUtils.isDefaultPort(url.getProtocol(), port)) {
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
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/host">MDN Documentation</a>
     */
    @JsxSetter
    public void setHost(final String host) throws Exception {
        setUrl(HTMLHyperlinkElementUtils.setHost(getUrl(), host));
    }

    /**
     * Returns the hostname portion of the link's URL.
     * @return the hostname portion of the link's URL
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/hostname">MDN Documentation</a>
     */
    @JsxGetter
    public String getHostname() {
        try {
            return HTMLHyperlinkElementUtils.getHostname(getUrl());
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the hostname portion of the link's URL.
     * @param hostname the new hostname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/hostname">MDN Documentation</a>
     */
    @JsxSetter
    public void setHostname(String hostname) throws Exception {
        if (hostname != null) {
            if (hostname.indexOf(' ') > -1) {
                return;
            }

            final int idx = hostname.indexOf('#');
            if (idx > -1) {
                hostname = hostname.substring(0, idx);
            }
        }

        if (org.htmlunit.util.StringUtils.isEmptyOrNull(hostname)) {
            return;
        }

        try {
            setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
        }
        catch (final MalformedURLException  e) {
            // do nothing
        }
    }

    /**
     * Returns the pathname portion of the link's URL.
     * @return the pathname portion of the link's URL
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/pathname">MDN Documentation</a>
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
                        href = org.apache.commons.lang3.StringUtils.capitalize(href);
                    }
                    if (browser.hasFeature(JS_ANCHOR_PATHNAME_PREFIX_WIN_DRIVES_URL)) {
                        href = "/" + href;
                    }
                    return href;
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
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/pathname">MDN Documentation</a>
     */
    @JsxSetter
    public void setPathname(final String pathname) throws Exception {
        setUrl(HTMLHyperlinkElementUtils.setPathname(getUrl(), pathname));
    }

    /**
     * Returns the port portion of the link's URL.
     * @return the port portion of the link's URL
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/port">MDN Documentation</a>
     */
    @JsxGetter
    public String getPort() {
        try {
            final URL url = getUrl();
            final int port = url.getPort();
            if (port == -1 || HTMLHyperlinkElementUtils.isDefaultPort(url.getProtocol(), port)) {
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
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/port">MDN Documentation</a>
     */
    @JsxSetter
    public void setPort(final String port) throws Exception {
        final URL url = getUrl();
        final int newPort = Integer.parseInt(port);
        if (HTMLHyperlinkElementUtils.isDefaultPort(url.getProtocol(), newPort)) {
            setUrl(UrlUtils.getUrlWithNewPort(url, -1));
        }
        else {
            setUrl(UrlUtils.getUrlWithNewPort(url, newPort));
        }
    }

    /**
     * Returns the protocol portion of the link's URL, including the trailing ':'.
     * @return the protocol portion of the link's URL, including the trailing ':'
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/protocol">MDN Documentation</a>
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
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLAnchorElement/protocol">MDN Documentation</a>
     */
    @JsxSetter
    public void setProtocol(final String protocol) throws Exception {
        final URL result = HTMLHyperlinkElementUtils.setProtocol(getUrl(), protocol);
        if (result != null) {
            setUrl(result);
        }
    }

    /**
     * Called, for instance, for implicit conversion to a string.
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
     * @param coords the {@code coords} attribute value
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
     * @param hreflang the {@code hreflang} attribute value
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
            return HTMLHyperlinkElementUtils.getUsername(getUrl());
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the {@code username} attribute.
     * @param username the {@code username} attribute value
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
            return HTMLHyperlinkElementUtils.getPassword(getUrl());
        }
        catch (final MalformedURLException e) {
            return "";
        }
    }

    /**
     * Sets the {@code password} attribute.
     * @param password the {@code password} attribute value
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
     * @param download the {@code download} attribute value
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
     * @param ping the {@code ping} attribute value
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
     * @param shape the {@code shape} attribute value
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
     * @param type the {@code type} attribute value
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
     * Sets the {@code relList} attribute.
     * @param rel the {@code relList} attribute value
     */
    @JsxSetter
    public void setRelList(final Object rel) {
        setRel(JavaScriptEngine.toString(rel));
    }
}
