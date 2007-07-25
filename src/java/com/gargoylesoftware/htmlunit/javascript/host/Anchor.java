/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.MalformedURLException;

import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.Context;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.util.UrlUtils;
import java.net.URL;

/**
 * The javascript object that represents an anchor
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:gousseff@netscape.net">Alexei Goussev</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Chris Erskine
 */
public class Anchor extends FocusableHostElement {

    private static final long serialVersionUID = -816365374422492967L;

    /**
     * Create an instance.
     */
    public Anchor() {
    }

    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }

    /**
     * Set the href property.
     * @param href href attribute value.
     */
    public void jsxSet_href( final String href ) {
        getHtmlElementOrDie().setAttributeValue("href", href);
    }

    /**
     * Return the value of the href property of this link.
     * @return The href property.
     * @throws Exception If an error occurs.
     */
    public String jsxGet_href() throws Exception {
        return getUrl().toString();
    }

    /**
     * Set the name property.
     * @param name name attribute value.
     */
    public void jsxSet_name(final String name) {
        getHtmlElementOrDie().setAttributeValue("name", name);
    }

    /**
     * Return the value of the name property of this link.
     * @return The name property.
     * @throws Exception If an error occurs.
     */
    public String jsxGet_name() throws Exception {
        return getHtmlElementOrDie().getAttributeValue("name");
    }

    /**
     * set the target property of this link.
     * @param target target attribute value.
     */
    public void jsxSet_target(final String target) {
        getHtmlElementOrDie().setAttributeValue("target", target);
    }

    /**
     * Return the value of the target property of this link.
     * @return The href property.
     */
    public String jsxGet_target() {
        return getHtmlElementOrDie().getAttributeValue( "target" );
    }

    /**
     * Returns this link's current URL.
     * @return This link's current URL.
     * @throws Exception If an error occurs.
     */
    private URL getUrl() throws Exception {
        final HtmlAnchor anchor = (HtmlAnchor) getHtmlElementOrDie();
        return anchor.getPage().getFullyQualifiedUrl(anchor.getHrefAttribute());
    }

    /**
     * sets the href attribute of this link to the specified URL.
     */
    private void setUrl(final URL url) {
        getHtmlElementOrDie().setAttributeValue("href", url.toString());
    }

    /**
     * Returns the search portion of the link's URL (the portion starting with
     * '?' and up to but not including any '#').
     * @return The search portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/search.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_search() throws Exception {
        final String query = getUrl().getQuery();
        if (query == null) {
            return "";
        }
        else {
            return "?" + query;
        }
    }

    /**
     * Sets the search portion of the link's URL (the portion starting with '?'
     * and up to but not including any '#')..
     * @param search The new search portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/search.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_search(final String search) throws Exception {
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
     * Returns the hash portion of the link's URL (the portion following the '#').
     * @return The hash portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/hash.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_hash() throws Exception {
        final String hash = getUrl().getRef();
        if (hash == null) {
            return "";
        }
        else {
            return hash;
        }
    }

    /**
     * Sets the hash portion of the link's URL (the portion following the '#').
     * @param hash The new hash portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/hash.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_hash( final String hash ) throws Exception {
        setUrl( UrlUtils.getUrlWithNewRef( getUrl(), hash ) );
    }

    /**
     * Returns the host portion of the link's URL (the '[hostname]:[port]' portion).
     * @return The host portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_host() throws Exception {
        final URL url = getUrl();
        final int port = url.getPort();
        final String host = url.getHost();

        if (port == -1) {
            return host;
        }
        else {
            return host + ":" + port;
        }
    }

    /**
     * Sets the host portion of the link's URL (the '[hostname]:[port]' portion).
     * @param host The new host portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_host( final String host ) throws Exception {
        final String hostname;
        final int port;
        final int index = host.indexOf(':');
        if (index != -1) {
            hostname = host.substring(0, index);
            port = Integer.parseInt( host.substring(index + 1) );
        }
        else {
            hostname = host;
            port = -1;
        }
        final URL url1 = UrlUtils.getUrlWithNewHost( getUrl(), hostname );
        final URL url2 = UrlUtils.getUrlWithNewPort( url1, port );
        setUrl( url2 );
    }

    /**
     * Returns the hostname portion of the link's URL.
     * @return The hostname portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_hostname() throws Exception {
        return getUrl().getHost();
    }

    /**
     * Sets the hostname portion of the link's URL.
     * @param hostname The new hostname portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/host.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_hostname( final String hostname ) throws Exception {
        setUrl(UrlUtils.getUrlWithNewHost(getUrl(), hostname));
    }

    /**
     * Returns the pathname portion of the link's URL.
     * @return The pathname portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/pathname.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_pathname() throws Exception {
        return getUrl().getPath();
    }

    /**
     * Sets the pathname portion of the link's URL.
     * @param pathname The new pathname portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/pathname.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_pathname( final String pathname ) throws Exception {
        setUrl( UrlUtils.getUrlWithNewPath( getUrl(), pathname ) );
    }

    /**
     * Returns the port portion of the link's URL.
     * @return The port portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/port.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_port() throws Exception {
        final int port = getUrl().getPort();
        if (port == -1) {
            return "";
        }
        else {
            return String.valueOf(port);
        }
    }

    /**
     * Sets the port portion of the link's URL.
     * @param port The new port portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/port.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_port(final String port) throws Exception {
        setUrl( UrlUtils.getUrlWithNewPort( getUrl(), Integer.parseInt( port ) ) );
    }

    /**
     * Returns the protocol portion of the link's URL, including the trailing ':'.
     * @return The protocol portion of the link's URL, including the trailing ':'.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/protocol.asp">
     * MSDN Documentation</a>
     */
    public String jsxGet_protocol() throws Exception {
        return getUrl().getProtocol() + ":";
    }

    /**
     * Sets the protocol portion of the link's URL.
     * @param protocol The new protocol portion of the link's URL.
     * @throws Exception If an error occurs.
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/protocol.asp">
     * MSDN Documentation</a>
     */
    public void jsxSet_protocol( final String protocol ) throws Exception {
        final String bareProtocol = StringUtils.substringBefore(protocol, ":");
        setUrl(UrlUtils.getUrlWithNewProtocol(getUrl(), bareProtocol));
    }

    /**
     * Calls for instance for implicit conversion to string
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#getDefaultValue(java.lang.Class)
     * @param hint the type hint
     * @return the default value
     */
    public Object getDefaultValue(final Class hint) {
        final HtmlAnchor link = (HtmlAnchor) getHtmlElementOrDie();
        final String href = link.getHrefAttribute();
        
        final String response;
        if (href == HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            response = ""; // for example for named anchors
        }
        else {
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

            try {
                response = link.getPage().getFullyQualifiedUrl(beforeAnchor).toExternalForm() + anchorPart;
            }
            catch (final MalformedURLException e) {
                throw Context.reportRuntimeError("Problem reading url: " + e);
            }
        }
        
        return response;
    }
    
}
