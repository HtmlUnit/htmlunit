/*
 * Copyright (c) 2002, 2005 Gargoyle Software Inc. All rights reserved.
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

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.lang.StringUtils;
import org.jaxen.JaxenException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.UniqueTag;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.StatusHandler;
import com.gargoylesoftware.htmlunit.StringWebResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.xpath.HtmlUnitXPath;
import com.gargoylesoftware.htmlunit.javascript.ElementArray;

/**
 * A JavaScript object for a Document.
 * 
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/objects/obj_document.asp">
 * MSDN documentation</a>
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-html.html#ID-7068919">
 * W3C Dom Level 1</a>
 */
public final class Document extends NodeImpl {

    private static final long serialVersionUID = -7646789903352066465L;
    private String status_ = "";
    private ElementArray all_; // has to be a member to have equality (==) working
    private ElementArray forms_; // has to be a member to have equality (==) working
    private ElementArray links_; // has to be a member to have equality (==) working
    private ElementArray images_; // has to be a member to have equality (==) working

    /** The buffer that will be used for calls to document.write() */
    private StringBuffer writeBuffer_;

    /**
     * Create an instance.  Javascript objects must have a default constructor.
     */
    public Document() {
    }


    /**
     * Javascript constructor.  This must be declared in every javascript file because
     * the rhino engine won't walk up the hierarchy looking for constructors.
     */
    public void jsConstructor() {
    }


    /**
     * Return the html page that this document is modeling..
     * @return The page.
     */
    public HtmlPage getHtmlPage() {
        return (HtmlPage)getDomNodeOrDie();
    }


    /**
     * Return the value of the javascript attribute "forms".
     * @return The value of this attribute.
     */
    public Object jsGet_forms() {
        if (forms_ == null) {
            forms_ = (ElementArray) makeJavaScriptObject(ElementArray.JS_OBJECT_NAME);
            try {
                forms_.init(getHtmlPage(), new HtmlUnitXPath("//form"));
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize collection document.forms: " + e.getMessage());
            }
        }
        return forms_;
    }


    /**
     * Return the value of the javascript attribute "links".  Refer also to the
     * <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/collections/links.asp"
     * MSDN documentation</a>
     * @return The value of this attribute.
     */
    public Object jsGet_links() {
        if (links_ == null) {
            links_ = (ElementArray) makeJavaScriptObject(ElementArray.JS_OBJECT_NAME);
            try {
                links_.init(getHtmlPage(), 
                        new HtmlUnitXPath("//*[(name() = 'a' or name() = 'area') "
                                + "and string-length(@href) > 0]"));
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize collection document.links: " + e.getMessage());
            }
        }
        return links_;
    }


    /**
     * javascript function "write".
     * @param content the content to write
     */
    public void jsFunction_write(final String content) {
        if (writeBuffer_ == null) {
            // open() hasn't been called
            final HtmlPage page = getHtmlPage();
            page.getScriptFilter().write(content);
        }
        else {
            writeBuffer_.append(content);
        }
    }


    /**
     * javascript function "writeln".
     * @param content the content to write
     */
    public void jsFunction_writeln(final String content) {
        jsFunction_write(content + "\n");
    }


    private HttpState getHttpState() {
        final HtmlPage htmlPage = getHtmlPage();
        final WebConnection connection = htmlPage.getWebClient().getWebConnection();
        final URL url = htmlPage.getWebResponse().getUrl();

        return connection.getStateForUrl( url );
    }

    /**
     * Return the cookie attribute.
     * @return The cookie attribute
     */
    public String jsGet_cookie() {
        final HttpState state = getHttpState();
        final Cookie[] cookies = state.getCookies();
        if( cookies == null ) {
            return "";
        }

        final StringBuffer buffer = new StringBuffer();

        for( int i=0; i<cookies.length; i++ ) {
            if( i != 0 ) {
                buffer.append(";");
            }
            buffer.append( cookies[i].getName() );
            buffer.append( "=" );
            buffer.append( cookies[i].getValue() );
        }
        return buffer.toString();
    }

    /**
     * Adds a cookie
     * @see <a href="http://msdn.microsoft.com/workshop/author/dhtml/reference/properties/cookie.asp">
     * MSDN documentation</a> 
     * @param newCookie in the format "name=value[;expires=date][;domain=domainname][;path=path][;secure]
     */
    public void jsSet_cookie(final String newCookie) {
        final HttpState state = getHttpState();

        final Cookie cookie = buildCookie(newCookie, getHtmlPage().getWebResponse().getUrl());
        state.addCookie(cookie);
        getLog().info("Added cookie: " + cookie);
    }


    /**
     * Builds a cookie object from the string representation allowed in JS
     * @param newCookie in the format "name=value[;expires=date][;domain=domainname][;path=path][;secure]
     * @param currentURL the url of the current page
     * @return the cookie
     */
    static Cookie buildCookie(final String newCookie, final URL currentURL) {
        final StringTokenizer st = new StringTokenizer(newCookie, ";");
        final String nameValue = st.nextToken();

        final String name = StringUtils.substringBefore(nameValue, "=");
        final String value = StringUtils.substringAfter(nameValue, "=");

        final Map attributes = new HashMap(); 
        // default values
        attributes.put("domain", currentURL.getHost());
        // default value "" as it seems that org.apache.commons.httpclient.cookie.CookieSpec 
        // doesn't like null as path 
        attributes.put("path", "");

        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            final int indexEqual = token.indexOf("=");
            if (indexEqual > -1) {
                attributes.put(token.substring(0, indexEqual), token.substring(indexEqual+1));
            }
            else {
                attributes.put(token, Boolean.TRUE);
            }
        }
        
        final String domain = (String) attributes.get("domain");
        final String path = (String) attributes.get("path");
        final Date expires = null;
        final boolean secure = (attributes.get("secure") != null);
        final Cookie cookie = new Cookie(domain, name, value, path, expires, secure);

        return cookie;
    }


    /**
     * Return the value of the "location" property.
     * @return The value of the "location" property
     */
    public Location jsGet_location() {
        final WebWindow webWindow = ((HtmlPage)getDomNodeOrDie()).getEnclosingWindow();
        return ((Window)webWindow.getScriptObject()).jsGet_location();
    }


    /**
     * Return the value of the "images" property.
     * @return The value of the "images" property
     */
    public Object jsGet_images() {
        if (images_ == null) {
            images_ = (ElementArray) makeJavaScriptObject(ElementArray.JS_OBJECT_NAME);
            try {
                images_.init(getHtmlPage(), new HtmlUnitXPath("//img"));
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize collection document.images: " + e.getMessage());
            }
        }
        return images_;
    }


    /**
     * Return the value of the "referrer" property.
     * @return The value of the "referrer" property
     */
    public String jsGet_referrer() {
        final String referrer = getHtmlPage().getWebResponse().getResponseHeaderValue("referrer");
        if( referrer == null ) {
            return "";
        }
        else {
            return referrer;
        }
    }


    /**
     * Return the value of the "URL" property.
     * @return The value of the "URL" property
     */
    public String jsGet_URL() {
        return getHtmlPage().getWebResponse().getUrl().toExternalForm();
    }


    /**
     * Return the value of the "all" property.
     * @return The value of the "all" property
     */
    public ElementArray jsGet_all() {
        if (all_ == null) {
            all_ = (ElementArray) makeJavaScriptObject(ElementArray.JS_OBJECT_NAME);
            try {
                all_.init(getHtmlPage(), new HtmlUnitXPath("//*"));
            }
            catch (final JaxenException e) {
                throw Context.reportRuntimeError("Failed to initialize collection document.all: " + e.getMessage());
            }
        }
        return all_;
    }


    /**
     * javascript function "open".
     * @param context The javascript context
     * @param scriptable The scriptable
     * @param args The arguments passed into the method.
     * @param function The function.
     * @return Nothing
     */
    public static Object jsFunction_open(
        final Context context, final Scriptable scriptable, final Object[] args,  final Function function ) {

        final Document document = (Document)scriptable;
        if( document.writeBuffer_ == null ) {
            document.writeBuffer_ = new StringBuffer();
        }
        else {
            document.getLog().warn("open() called when document is already open.");
        }
        return null;
    }


    /**
     * javascript function "close".
     * @param context The javascript context
     * @param scriptable The scriptable
     * @param args The arguments passed into the method.
     * @param function The function.
     * @return Nothing
     * @throws IOException If an IO problem occurs.
     */
    public static Object jsFunction_close(
            final Context context,
            final Scriptable scriptable,
            final Object[] args,
            final Function function )
        throws
            IOException {

        final Document document = (Document)scriptable;
        if( document.writeBuffer_  == null ) {
            document.getLog().warn("close() called when document is not open.");
        }
        else {
            final WebResponse webResponse
                = new StringWebResponse(document.writeBuffer_.toString());
            final HtmlPage page = document.getDomNodeOrDie().getPage();
            final WebClient webClient = page.getWebClient();
            final WebWindow window = page.getEnclosingWindow();

            webClient.loadWebResponseInto(webResponse, window);

            document.writeBuffer_ = null;
        }
        return null;
    }

    /**
     * Get the JavaScript property "documentElement" for the document.
     * @return The root node for the document.
     */
    public Object jsGet_documentElement() {
        return getScriptableFor(((HtmlPage)getDomNodeOrDie()).getDocumentElement());
    }


    /**
     * Create a new HTML element with the given tag name.
     *
     * @param tagName The tag name
     * @return the new HTML element, or NOT_FOUND if the tag is not supported.
     */
    public Object jsFunction_createElement( final String tagName ) {
        Object result = NOT_FOUND;
        try {
            final HtmlElement htmlElement = getDomNodeOrDie().getPage().createElement(tagName);
            final Object jsElement = getScriptableFor(htmlElement);

            if( jsElement == NOT_FOUND ) {
                getLog().debug("createElement("+tagName
                    +") cannot return a result as there isn't a javascript object for the html element "
                    + htmlElement.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch( final ElementNotFoundException e ) {
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }


    /**
     * Create a new DOM text node with the given data.
     *
     * @param newData The string value for the text node.
     * @return the new text node or NOT_FOUND if there is an error.
     */
    public Object jsFunction_createTextNode( final String newData ) {
        Object result = NOT_FOUND;
        try {
            final DomNode domNode = new DomText(getDomNodeOrDie().getPage(), newData);
            final Object jsElement = getScriptableFor(domNode);

            if( jsElement == NOT_FOUND ) {
                getLog().debug("createTextNode("+newData
                    +") cannot return a result as there isn't a javascript object for the DOM node "
                    + domNode.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch( final ElementNotFoundException e ) {
            // Just fall through - result is already set to NOT_FOUND
        }
        return result;
    }


    /**
     * Return the element with the specified id or null if that element could
     * not be found
     * @param id The ID to search for
     * @return the element or null
     */
    public Object jsFunction_getElementById( final String id ) {
        Object result = null;
        try {
            final HtmlElement htmlElement = ((HtmlPage)getDomNodeOrDie()).getDocumentElement().getHtmlElementById(id);
            final Object jsElement = getScriptableFor(htmlElement);

            if( jsElement == NOT_FOUND ) {
                getLog().debug("getElementById("+id
                    +") cannot return a result as there isn't a javascript object for the html element "
                    + htmlElement.getClass().getName());
            }
            else {
                result = jsElement;
            }
        }
        catch (final ElementNotFoundException e) {
            // Just fall through - result is already set to null
            
            final BrowserVersion browser = this.getHtmlPage().getWebClient().getBrowserVersion();
            if (browser.isIE()) {
                NativeArray elements = (NativeArray) jsFunction_getElementsByName(id);
                result = elements.get(0, this);
                if (result instanceof UniqueTag) {
                    return null;
                }
                getLog().warn("getElementById(" + id + ") did a getElementByName for Internet Explorer");
                return result;
            }
            getLog().warn("getElementById(" + id 
                + ") cannot return a result as there isn't a javascript object for the html element ");
        }
        return result;
    }


    /**
     * Return all the elements with the specified tag name
     * @param tagName The name to search for
     * @return the list of elements
     */
    public Object jsFunction_getElementsByTagName( final String tagName ) {
        final HtmlPage page = (HtmlPage)getDomNodeOrDie();
        final List list = page.getDocumentElement().getHtmlElementsByTagNames(
            Collections.singletonList(tagName.toLowerCase()));
        CollectionUtils.transform(list, getTransformerScriptableFor());

        return new NativeArray( list.toArray() );
    }

    /**
     * Returns all HTML elements that have a "name" attribute with the given value
     * 
     * Refer to <a href="http://www.w3.org/TR/DOM-Level-2-HTML/html.html#ID-71555259">
     * The DOM spec</a> for details.
     * 
     * @param elementName - value of the "name" attribute to look for
     * @return NodeList of elements
     */
    public Object jsFunction_getElementsByName( final String elementName ) {
        final HtmlPage page = (HtmlPage)getDomNodeOrDie();
        final String exp = "//*[@name='" + elementName + "']";
        final List list;
        try {
            final HtmlUnitXPath xpath = new HtmlUnitXPath(exp);
            list = xpath.selectNodes(page);
        } 
        catch (JaxenException e) {
            return new NativeArray(0);
        }
        CollectionUtils.transform(list, getTransformerScriptableFor());
        return new NativeArray( list.toArray() );
    }


    /**
     * Return the specified property or NOT_FOUND if it could not be found. This is
     * overridden to get elements by name within this document.
     * @param name The name of the property
     * @param start The scriptable object that was originally queried for this property
     * @return The property.
     */
    public Object get( final String name, final Scriptable start ) {
        // Some calls to get will happen during the initialization of the superclass.
        // At this point, we don't have enough information to do our own initialization
        // so we have to just pass this call through to the superclass.
        final HtmlPage htmlPage = (HtmlPage)getDomNodeOrNull();
        if( htmlPage == null ) {
            return super.get(name, start);
        }

        final Document document = (Document) start;
        NativeArray array = (NativeArray) document.jsFunction_getElementsByName(name);
        if (array.getLength() == 1) {
            return array.get(0, array);
        } 
        else if (array.getLength() > 1) {
            return array;
        }

        return super.get(name, start);
    }

    /**
     * Return the text from the status line.
     * @return the status line text
     */
    public Object jsGet_status() {
        return status_;
    }

    /**
     * Set the text from the status line.
     * @param message the status line text
     */
    public void jsSet_status( final String message ) {
        status_ = message;

        final HtmlPage page = getHtmlPage();
        final StatusHandler statusHandler = page.getWebClient().getStatusHandler();
        if( statusHandler != null ) {
            statusHandler.statusMessageChanged(page, message);
        }
    }

    /**
     * Gets the body element this document
     * @return body element
     */
    public Object jsGet_body() {
        final List list = getHtmlPage().getDocumentElement().getHtmlElementsByTagName("body");
        if( list.size() == 0 ) {
            return NOT_FOUND;
        }
        else {
            final DomNode bodyElement = (DomNode)list.get(0);
            return getScriptableFor(bodyElement);
        }
    }
    

    /**
     * Gets the title of this document
     * @return body element
     */
    public String jsGet_title() {
        return getHtmlPage().getTitleText();
    }
    
    /**
     * Set the title.
     * @param message The new title
     */
    public void jsSet_title(final String message ) {
        getHtmlPage().setTitleText(message);
    }
    
    /**
     * Get the readyState of this document.  This is an IE only function
     * @return the state - uninitilized, loading or complete - The interactive state is not returned
     * 
     */
    public String jsGet_readyState() {
        final DomNode node = getDomNodeOrDie();
        if (node instanceof HtmlPage) {
            return ((HtmlPage) node).getDocumentElement().getReadyState();
        }
        return getDomNodeOrDie().getReadyState();
    }
}

