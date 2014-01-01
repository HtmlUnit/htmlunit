/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.util.UrlUtils;

/**
 * The JavaScript object that represents an anchor.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gousseff@netscape.net">Alexei Goussev</a>
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

    /**
     * Sets the <tt>href</tt> property.
     * @param href the <tt>href</tt> property value
     */
    @JsxSetter
    public void setHref(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of this link's <tt>href</tt> property.
     * @return the value of this link's <tt>href</tt> property
     */
    @JsxGetter
    public String getHref() {
        final HtmlAnchor anchor = (HtmlAnchor) getDomNodeOrDie();
        final String hrefAttr = anchor.getHrefAttribute();

        if (hrefAttr == DomElement.ATTRIBUTE_NOT_DEFINED) {
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
     * Sets the name property.
     * @param name name attribute value
     */
    @JsxSetter
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute("name", name);
    }

    /**
     * Returns the value of the name property of this link.
     * @return the name property
     */
    @JsxGetter
    public String getName() {
        return getDomNodeOrDie().getAttribute("name");
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
        return getDomNodeOrDie().getAttribute("target");
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
     * Sets the <tt>href</tt> attribute of this link to the specified URL.
     * @param url the new value of the <tt>href</tt> attribute
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
     * Sets the rev property.
     * @param rel rev attribute value
     */
    @JsxSetter
    public void setRev(final String rel) {
        getDomNodeOrDie().setAttribute("rev", rel);
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
     * Returns the search portion of the link's URL (the portion starting with
     * '?' and up to but not including any '#').
     * @return the search portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534620.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getSearch() throws Exception {
        final String query = getUrl().getQuery();
        if (query == null) {
            return "";
        }
        return "?" + query;
    }

    /**
     * Sets the search portion of the link's URL (the portion starting with '?'
     * and up to but not including any '#')..
     * @param search the new search portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534620.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setSearch(final String search) throws Exception {
        final String query;
        if (search == null || "?".equals(search) || "".equals(search)) {
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
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533775.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getHash() throws Exception {
        final String hash = getUrl().getRef();
        if (hash == null) {
            return "";
        }
        return "#" + hash;
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
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533784.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getHost() throws Exception {
        final URL url = getUrl();
        final int port = url.getPort();
        final String host = url.getHost();

        if (port == -1) {
            return host;
        }
        return host + ":" + port;
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
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533785.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getHostname() throws Exception {
        return getUrl().getHost();
    }

    /**
     * Sets the hostname portion of the link's URL.
     * @param hostname the new hostname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533785.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setHostname(final String hostname) throws Exception {
        setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
    }

    /**
     * Returns the pathname portion of the link's URL.
     * @return the pathname portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534332.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getPathname() throws Exception {
        return getUrl().getPath();
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
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534342.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getPort() throws Exception {
        final int port = getUrl().getPort();
        if (port == -1) {
            return "";
        }
        return Integer.toString(port);
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
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    @JsxGetter
    public String getProtocol() throws Exception {
        return getUrl().getProtocol() + ":";
    }

    /**
     * Sets the protocol portion of the link's URL.
     * @param protocol the new protocol portion of the link's URL
     * @throws Exception if an error occurs
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534353.aspx">MSDN Documentation</a>
     */
    @JsxSetter
    public void setProtocol(final String protocol) throws Exception {
        final String bareProtocol = StringUtils.substringBefore(protocol, ":");
        setUrl(UrlUtils.getUrlWithNewProtocol(getUrl(), bareProtocol));
    }

    /**
     * Calls for instance for implicit conversion to string.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
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
        String href = element.getAttribute("href");

        if (DomElement.ATTRIBUTE_NOT_DEFINED == href) {
            return ""; // for example for named anchors
        }

        href = href.trim();

        final SgmlPage page = element.getPage();
        if (page == null || !page.isHtmlPage()) {
            return href;
        }

        final int indexAnchor = href.indexOf('#');
        final String beforeAnchor;
        final String anchorPart;
        if (indexAnchor == -1) {
            beforeAnchor = href;
            anchorPart = "";
        }
        else {
            beforeAnchor = href.substring(0, indexAnchor);
            anchorPart = href.substring(indexAnchor);
        }

        final HtmlPage htmlPage = (HtmlPage) page;
        try {
            final String response =
                htmlPage.getFullyQualifiedUrl(beforeAnchor).toExternalForm() + anchorPart;
            return response;
        }
        catch (final MalformedURLException e) {
            return href;
        }
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter(@WebBrowser(FF))
    public String getAccessKey() {
        return super.getAccessKey();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter(@WebBrowser(FF))
    public void setAccessKey(final String accessKey) {
        super.setAccessKey(accessKey);
    }
}
