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
package com.gargoylesoftware.htmlunit.html;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jaxen.JaxenException;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.NodeImpl;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A representation of an HTML page returned from a server. This class is the
 * DOM Document implementation.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Alex Nikiforoff
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public final class HtmlPage extends DomNode implements Page, Cloneable {

    private static final long serialVersionUID = 1779746292119944291L;

    private final WebClient webClient_;
    private       String originalCharset_ = null;
    private final WebResponse webResponse_;
    private       Map idMap_ = new HashMap(); // a map of (id, List(HtmlElement))
    private       HtmlElement documentElement_ = null;
    private FocusableElement elementWithFocus_ = null;

    private WebWindow enclosingWindow_;

    private final transient Log javascriptLog_ = LogFactory.getLog("com.gargoylesoftware.htmlunit.javascript");

    private static final int TAB_INDEX_NOT_SPECIFIED = -10;
    private static final int TAB_INDEX_OUT_OF_BOUNDS = -20;

    private List/* HtmlAttributeChangeListener */ attributeListeners_;

    /**
     *  Create an instance of HtmlPage
     *
     * @param originatingUrl The url that was used to load this page.
     * @param webResponse The web response that was used to create this page
     * @param webWindow The window that this page is being loaded into.
     */
    public HtmlPage(
            final URL originatingUrl,
            final WebResponse webResponse,
            final WebWindow webWindow ) {

        super(null);

        webClient_ = webWindow.getWebClient();
        webResponse_ = webResponse;
        setEnclosingWindow(webWindow);
    }

    /**
     * @return this page
     */
    public HtmlPage getPage() {
        return this;
    }

    /**
     * Initialize this page.
     * @throws IOException If an IO problem occurs.
     * @throws FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      {@link WebClient#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     */
    public void initialize() throws IOException, FailingHttpStatusCodeException {
        loadFrames();
        getDocumentElement().setReadyState(STATE_COMPLETE);
        executeOnLoadHandlersIfNeeded();
        executeRefreshIfNeeded();
    }

    /**
     * Clean up this page.
     * @throws IOException If an IO problem occurs.
     */
    public void cleanUp() throws IOException {
        deregisterFramesIfNeeded();
        // TODO: executeBodyOnUnloadHandlerIfNeeded();
    }

    /**
     * Get the type of the current node.
     * @return The node type
     */
    public short getNodeType() {
        return DOCUMENT_NODE;
    }

    /**
     * Get the name for the current node.
     * @return The node name
     */
    public String getNodeName() {
        return "#document";
    }

    /**
     * Get the root element of this document.
     * @return The root element
     */
    public HtmlElement getDocumentElement() {
        if (documentElement_ == null) {
            DomNode childNode = getFirstChild();
            while (childNode != null && ! (childNode instanceof HtmlElement)) {
                childNode = childNode.getNextSibling();
            }
            documentElement_ = (HtmlElement) childNode;
        }
        return documentElement_;
    }

    /**
     * Return the charset used in the page.
     * The sources of this information are from 1).meta element which
     * http-equiv attribute value is 'content-type', or if not from
     * the response header.
     * @return the value of charset.
     */
    public String getPageEncoding() {
        if (originalCharset_ != null) {
            return originalCharset_ ;
        }

        final List list = getMetaTags("content-type");
        for (int i=0; i<list.size();i++) {
            final HtmlMeta meta = (HtmlMeta) list.get(i);
            final String contents = meta.getContentAttribute();
            final int pos = contents.toLowerCase().indexOf("charset=");
            if (pos >= 0) {
                originalCharset_ = contents.substring(pos+8);
                getLog().debug("Page Encoding detected: " + originalCharset_);
                return originalCharset_;
            }
        }
        if (originalCharset_ == null) {
            originalCharset_ = webResponse_.getContentCharSet();
        }
        return originalCharset_;
    }

    /**
     * Create a new HTML element with the given tag name.
     *
     * @param tagName The tag name, preferably in lowercase
     * @return the new HTML element.
     */
    public HtmlElement createElement(final String tagName) {
        final String tagLower = tagName.toLowerCase();
        return HTMLParser.getFactory(tagLower).createElement(this, tagLower, null);
    }

    /**
     * Create a new HTML element with the given namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @return the new HTML element.
     */
    public HtmlElement createElementNS(final String namespaceURI, final String qualifiedName) {
        final String tagLower = qualifiedName.toLowerCase().substring( qualifiedName.indexOf( ':' ) + 1 );
        return HTMLParser.getFactory(tagLower).createElementNS(this, namespaceURI, qualifiedName, null);
    }

    /**
     *  Return the HtmlAnchor with the specified name
     *
     * @param name The name to search by
     * @return See above
     * @throws com.gargoylesoftware.htmlunit.ElementNotFoundException If the anchor could not be found.
     */
    public HtmlAnchor getAnchorByName( final String name ) throws ElementNotFoundException {
        return ( HtmlAnchor )getDocumentElement().getOneHtmlElementByAttribute( "a", "name", name );
    }

    /**
    *  Return the {@link HtmlAnchor} with the specified href
     *
     * @param href The string to search by
     * @return The HtmlAnchor
     * @throws ElementNotFoundException If the anchor could not be found.
     */
    public HtmlAnchor getAnchorByHref( final String href ) throws ElementNotFoundException {
        return ( HtmlAnchor )getDocumentElement().getOneHtmlElementByAttribute( "a", "href", href );
    }

    /**
     * Return a list of all anchors contained in this page.
     * @return the list of {@link HtmlAnchor} in this page.
     */
    public List getAnchors() {
        return getDocumentElement().getHtmlElementsByTagNames( Collections.singletonList("a") );
    }

    /**
     * Return the first anchor that contains the specified text.
     * @param text The text to search for
     * @return The first anchor that was found.
     * @throws ElementNotFoundException If no anchors are found with the specified text
     */
    public HtmlAnchor getFirstAnchorByText( final String text ) throws ElementNotFoundException {
        Assert.notNull("text", text);

        final Iterator iterator = getAnchors().iterator();
        while( iterator.hasNext() ) {
            final HtmlAnchor anchor = (HtmlAnchor)iterator.next();
            if( text.equals(anchor.asText()) ) {
                return anchor;
            }
        }
        throw new ElementNotFoundException("a", "<text>", text);
    }

    /**
     * Return the first form that matches the specifed name
     * @param name The name to search for
     * @return The first form.
     * @exception ElementNotFoundException If no forms match the specifed result.
     */
    public HtmlForm getFormByName( final String name ) throws ElementNotFoundException {
        final List forms = getDocumentElement().getHtmlElementsByAttribute( "form", "name", name );
        if( forms.size() == 0 ) {
            throw new ElementNotFoundException( "form", "name", name );
        }
        else {
            return ( HtmlForm )forms.get( 0 );
        }
    }

    /**
     * Return a list of all the forms in the page.
     * @return All the forms.
     */
    public List getForms() {
        return getDocumentElement().getHtmlElementsByTagNames( Arrays.asList(new String[]{"form"}) );
    }

    /**
     *  Return the WebClient that originally loaded this page
     *
     * @return See above
     */
    public WebClient getWebClient() {
        return webClient_;
    }

    /**
     *  Given a relative url (ie /foo), return a fully qualified url based on
     *  the url that was used to load this page
     *
     * @param relativeUrl The relative url
     * @return See above
     * @exception MalformedURLException If an error occurred when creating a
     *      URL object
     */
    public URL getFullyQualifiedUrl( String relativeUrl )
        throws MalformedURLException {

        final List baseElements = getDocumentElement().getHtmlElementsByTagNames(Collections.singletonList("base"));
        URL baseUrl;
        if( baseElements.isEmpty() ) {
            baseUrl = webResponse_.getUrl();
        }
        else {
            if (baseElements.size() > 1) {
                notifyIncorrectness("Multiple 'base' detected, only the first is used.");
            }
            final HtmlBase htmlBase = (HtmlBase) baseElements.get(0);
            boolean insideHead = false;
            for (DomNode parent = htmlBase.getParentNode(); parent != null; parent = parent.getParentNode()) {
                if (parent instanceof HtmlHead) {
                    insideHead = true;
                    break;
                }
            }
            
            //http://www.w3.org/TR/1999/REC-html401-19991224/struct/links.html#edef-BASE
            if (!insideHead) {
                notifyIncorrectness("Element 'base' must appear in <head>, it is ignored.");
            }
            
            final String href = htmlBase.getHrefAttribute();
            if (!insideHead || StringUtils.isEmpty(href)) {
                baseUrl = webResponse_.getUrl();
            }
            else {
                try {
                    baseUrl = new URL(href);
                }
                catch (final MalformedURLException e) {
                    notifyIncorrectness("Invalid base url: \"" + href + "\", ignoring it");
                    baseUrl = webResponse_.getUrl();
                }
            }
        }

        // to handle http: and http:/ in FF (Bug 1714767)
        if( getWebClient().getBrowserVersion().isNetscape() ) {
            boolean incorrectnessNotified = false;
            while( relativeUrl.startsWith( "http:" ) && !relativeUrl.startsWith( "http://" ) ) {
                if( !incorrectnessNotified ) {
                    notifyIncorrectness( "Incorrect URL \"" + relativeUrl + "\" has been corrected" );
                    incorrectnessNotified = true;
                }
                relativeUrl = "http:/" + relativeUrl.substring( 5 );
            }
        }

        return WebClient.expandUrl( baseUrl, relativeUrl );
    }

    /**
     *  Given a target attribute value, resolve the target using a base
     *  target for the page.
     *
     * @param elementTarget The target specified as an attribute of the element.
     * @return The resolved target to use for the element.
     */
    public String getResolvedTarget( final String elementTarget ) {

        final List baseElements =
            getDocumentElement().getHtmlElementsByTagNames( Collections.singletonList("base"));
        final String resolvedTarget;
        if( baseElements.isEmpty() ) {
            resolvedTarget = elementTarget;
        }
        else if( elementTarget != null && elementTarget.length() > 0 ) {
            resolvedTarget = elementTarget;
        }
        else {
            final HtmlBase htmlBase = (HtmlBase)baseElements.get(0);
            resolvedTarget = htmlBase.getTargetAttribute();
        }
        return resolvedTarget;
    }

    /**
     *  Return the web response that was originally used to create this page.
     *
     * @return The web response
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     *  Return a list of ids (strings) that correspond to the tabbable elements
     *  in this page. Return them in the same order specified in {@link
     *  #getTabbableElements}
     *
     * @return The list of id's
     */
    public List getTabbableElementIds() {
        final List list = new ArrayList( getTabbableElements() );
        final int listSize = list.size();

        for( int i = 0; i < listSize; i++ ) {
            list.set( i, ( ( HtmlElement )list.get( i ) ).getAttributeValue( "id" ) );
        }

        return Collections.unmodifiableList( list );
    }

    /**
     *  Return a list of all elements that are tabbable in the order that will
     *  be used for tabbing.<p>
     *
     *  The rules for determing tab order are as follows:
     *  <ol>
     *    <li> Those elements that support the tabindex attribute and assign a
     *    positive value to it are navigated first. Navigation proceeds from the
     *    element with the lowest tabindex value to the element with the highest
     *    value. Values need not be sequential nor must they begin with any
     *    particular value. Elements that have identical tabindex values should
     *    be navigated in the order they appear in the character stream.
     *    <li> Those elements that do not support the tabindex attribute or
     *    support it and assign it a value of "0" are navigated next. These
     *    elements are navigated in the order they appear in the character
     *    stream.
     *    <li> Elements that are disabled do not participate in the tabbing
     *    order.
     *  </ol>
     *  Additionally, the value of tabindex must be within 0 and 32767. Any
     *  values outside this range will be ignored.<p>
     *
     *  The following elements support the tabindex attribute: A, AREA, BUTTON,
     *  INPUT, OBJECT, SELECT, and TEXTAREA.<p>
     *
     * @return A list containing all the tabbable elements in proper tab order.
     */
    public List getTabbableElements() {

        final List acceptableTagNames = Arrays.asList(
                new Object[] {"a", "area", "button", "input", "object", "select", "textarea"} );
        final List tabbableElements = new ArrayList();

        final Iterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = (HtmlElement)iterator.next();
            final String tagName = element.getTagName();
            if( acceptableTagNames.contains( tagName ) ) {
                final boolean isDisabled = element.isAttributeDefined("disabled");

                if( isDisabled == false && getTabIndex(element) != TAB_INDEX_OUT_OF_BOUNDS ) {
                    tabbableElements.add(element);
                }
            }
        }
        Collections.sort( tabbableElements, createTabOrderComparator() );
        return Collections.unmodifiableList( tabbableElements );
    }

    private Comparator createTabOrderComparator() {

        return new Comparator() {
            public int compare( final Object object1, final Object object2 ) {
                final HtmlElement element1 = ( HtmlElement )object1;
                final HtmlElement element2 = ( HtmlElement )object2;

                final int tabIndex1 = getTabIndex( element1 );
                final int tabIndex2 = getTabIndex( element2 );

                final int result;
                if( tabIndex1 > 0 && tabIndex2 > 0 ) {
                    result = tabIndex1 - tabIndex2;
                }
                else if( tabIndex1 > 0 ) {
                    result = -1;
                }
                else if( tabIndex2 > 0 ) {
                    result = +1;
                }
                else if( tabIndex1 == tabIndex2 ) {
                    result = 0;
                }
                else {
                    result = tabIndex2 - tabIndex1;
                }

                return result;
            }
        };
    }

    private int getTabIndex( final HtmlElement element ) {
        final String tabIndexAttribute = element.getAttributeValue( "tabindex" );
        if( tabIndexAttribute == null || tabIndexAttribute.length() == 0 ) {
            return TAB_INDEX_NOT_SPECIFIED;
        }

        final int tabIndex = Integer.parseInt( tabIndexAttribute );
        if( tabIndex >= 0 && tabIndex <= 32767 ) {
            return tabIndex;
        }
        else {
            return TAB_INDEX_OUT_OF_BOUNDS;
        }
    }

    /**
     *  Return the html element that is assigned to the specified access key. An
     *  access key (aka mnemonic key) is used for keyboard navigation of the
     *  page.<p>
     *
     *  Only the following html elements may have accesskey's defined: A, AREA,
     *  BUTTON, INPUT, LABEL, LEGEND, and TEXTAREA.
     *
     * @param accessKey The key to look for
     * @return The html element that is assigned to the specified key or null
     *      if no elements can be found that match the specified key.
     */
    public HtmlElement getHtmlElementByAccessKey( final char accessKey ) {
        final List elements = getHtmlElementsByAccessKey(accessKey);
        if( elements.isEmpty() ) {
            return null;
        }
        else {
            return (HtmlElement)elements.get(0);
        }
    }

    /**
     *  Return all the html elements that are assigned to the specified access key. An
     *  access key (aka mnemonic key) is used for keyboard navigation of the
     *  page.<p>
     *
     *  The html specification seems to indicate that one accesskey cannot be used
     *  for multiple elements however Internet Explorer does seem to support this.
     *  It's worth noting that Mozilla does not support multiple elements with one
     *  access key so you are making your html browser specific if you rely on this
     *  feature.<p>
     *
     *  Only the following html elements may have accesskey's defined: A, AREA,
     *  BUTTON, INPUT, LABEL, LEGEND, and TEXTAREA.
     *
     * @param accessKey The key to look for
     * @return A list of html elements that are assigned to the specified accesskey.
     */
    public List getHtmlElementsByAccessKey( final char accessKey ) {

        final List elements = new ArrayList();

        final String searchString = ( "" + accessKey ).toLowerCase();
        final List acceptableTagNames = Arrays.asList(
                new Object[]{"a", "area", "button", "input", "label", "legend", "textarea"} );

        final Iterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = (HtmlElement)iterator.next();
            if( acceptableTagNames.contains( element.getTagName() ) ) {
                final String accessKeyAttribute = element.getAttributeValue("accesskey");
                if( searchString.equalsIgnoreCase( accessKeyAttribute ) ) {
                    elements.add( element );
                }
            }
        }

        return elements;
    }

    /**
     *  Many html elements are "tabbable" and can have a "tabindex" attribute
     *  that determines the order in which the components are navigated when
     *  pressing the tab key. To ensure good usability for keyboard navigation,
     *  all tabbable elements should have the tabindex attribute set.<p>
     *
     *  Assert that all tabbable elements have a valid value set for "tabindex".
     *  If they don't then throw an exception as per {@link
     *  WebClient#assertionFailed(String)}
     */
    public void assertAllTabIndexAttributesSet() {

        final List acceptableTagNames = Arrays.asList(
                new Object[]{"a", "area", "button", "input", "object", "select", "textarea"} );
        final List tabbableElements = getDocumentElement().getHtmlElementsByTagNames( acceptableTagNames );

        final Iterator iterator = tabbableElements.iterator();
        while( iterator.hasNext() ) {
            final HtmlElement htmlElement = ( HtmlElement )iterator.next();
            final int tabIndex = getTabIndex( htmlElement );
            if( tabIndex == TAB_INDEX_OUT_OF_BOUNDS ) {
                webClient_.assertionFailed(
                    "Illegal value for tab index: "
                    + htmlElement.getAttributeValue( "tabindex" ) );
            }
            else if( tabIndex == TAB_INDEX_NOT_SPECIFIED ) {
                webClient_.assertionFailed( "tabindex not set for " + htmlElement );
            }
        }
    }

    /**
     *  Many html components can have an "accesskey" attribute which defines a
     *  hot key for keyboard navigation. Assert that all access keys (mnemonics)
     *  in this page are unique. If they aren't then throw an exception as per
     *  {@link WebClient#assertionFailed(String)}
     */
    public void assertAllAccessKeyAttributesUnique() {

        final List accessKeyList = new ArrayList();

        final Iterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final String id = ((HtmlElement)iterator.next()).getAttributeValue("accesskey" );
            if( id != null && id.length() != 0 ) {
                if( accessKeyList.contains( id ) ) {
                    webClient_.assertionFailed( "Duplicate access key: " + id );
                }
                else {
                    accessKeyList.add( id );
                }
            }
        }
    }

    /**
     *  Each html element can have an id attribute and by definition, all ids
     *  must be unique within the document. <p>
     *
     *  Assert that all ids in this page are unique. If they aren't then throw
     *  an exception as per {@link WebClient#assertionFailed(String)}
     */
    public void assertAllIdAttributesUnique() {
        final List idList = new ArrayList();

        final Iterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final String id = ((HtmlElement)iterator.next()).getAttributeValue("id");
            if( id != null && id.length() != 0 ) {
                if( idList.contains( id ) ) {
                    webClient_.assertionFailed( "Duplicate ID: " + id );
                }
                else {
                    idList.add( id );
                }
            }
        }
    }

    /**
     * <p>
     * Execute the specified javascript if a javascript engine was successfully
     * instantiated.  If this javascript causes the current page to be reloaded
     * (through location="" or form.submit()) then return the new page.  Otherwise
     * return the current page.
     * </p>
     * <p><b>Please note:</b> Although this method is public, it is not intended for
     * general execution of javascript.  Users of HtmlUnit should interact with the pages
     * as a user would by clicking on buttons or links and having the javascript event
     * handlers execute as needed..
     * </p>
     * 
     * @param sourceCode The javascript code to execute.
     * @param sourceName The name for this chunk of code.  This name will be displayed
     * in any error messages.
     * @param htmlElement The html element for which this script is being executed.
     * This element will be the context during the javascript execution.  If null,
     * the context will default to the window.
     * @return A ScriptResult which will contain both the current page (which may be different than
     * the previous page and a javascript result object.
     */
    public ScriptResult executeJavaScriptIfPossible(
        String sourceCode, final String sourceName, 
        final HtmlElement htmlElement ) {

        final ScriptEngine engine = getWebClient().getScriptEngine();
        if( ! getWebClient().isJavaScriptEnabled() || engine == null ) {
            return new ScriptResult(null, this);
        }

        final String prefix = "javascript:";
        final int prefixLength = prefix.length();

        if( sourceCode.length() > prefixLength
                && sourceCode.substring(0, prefixLength).equalsIgnoreCase(prefix)) {
            sourceCode = sourceCode.substring(prefixLength);
        }

        final WebWindow window = getEnclosingWindow();
        getWebClient().pushClearFirstWindow();
        final Object result = engine.execute( this, sourceCode, sourceName, htmlElement );

        WebWindow firstWindow = getWebClient().popFirstWindow();
        if ( firstWindow == null) {
            firstWindow = window;
        }

        return new ScriptResult( result, firstWindow.getEnclosedPage() );
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * 
     * Execute a Function in the given context.
     *
     * @param function The javascript Function to call.
     * @param thisObject The "this" object to be used during invocation.
     * @param args The arguments to pass into the call. 
     * @param htmlElementScope The html element for which this script is being executed.
     * This element will be the context during the javascript execution.  If null,
     * the context will default to the page.
     * @return A ScriptResult which will contain both the current page (which may be different than
     * the previous page and a javascript result object.
     */
    public ScriptResult executeJavaScriptFunctionIfPossible(
            final Function function,
            final Scriptable thisObject,
            final Object[] args,
            final DomNode htmlElementScope) {

        final WebWindow window = getEnclosingWindow();
        getWebClient().pushClearFirstWindow();

        if (!getWebClient().isJavaScriptEnabled()) {
            return new ScriptResult(null, this);
        }

        final ScriptEngine engine = getWebClient().getScriptEngine();
        final Object result = engine.callFunction(this, function, thisObject, args, htmlElementScope);

        WebWindow firstWindow = getWebClient().popFirstWindow();
        if ( firstWindow == null) {
            firstWindow = window;
        }
        return new ScriptResult(result, firstWindow.getEnclosedPage());
    }

    /**
     * Return the log object for this element.
     * @return The log object for this element.
     */
    protected Log getJsLog() {
        return javascriptLog_;
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * 
     * @param srcAttribute The source attribute from the script tag.
     * @param charset The charset attribute from the script tag.
     */
    void loadExternalJavaScriptFile( final String srcAttribute,
                                            final String charset  ) {
        final ScriptEngine engine = getWebClient().getScriptEngine();
        if (getWebClient().isJavaScriptEnabled() && engine != null) {
            final URL scriptURL;
            try {
                scriptURL = getFullyQualifiedUrl(srcAttribute);
            }
            catch( final MalformedURLException e ) {
                getLog().error("Unable to build url for script src tag [" + srcAttribute + "]");
                if (getWebClient().isThrowExceptionOnScriptError()) {
                    throw new ScriptException(this, e);
                }
                return;
            }

            engine.execute(this, loadJavaScriptFromUrl(scriptURL, charset), scriptURL.toExternalForm(), null);
        }
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>  
     * 
     * Return true if a script with the specified type and language attributes
     * is actually JavaScript.
     * According to <a href="http://www.w3.org/TR/REC-html40/types.html#h-6.7">W3C recommendation</a>
     * are content types case insensitive.
     * @param typeAttribute The type attribute specified in the script tag.
     * @param languageAttribute The language attribute specified in the script tag.
     * @return true if the script is javascript
     */
    public static boolean isJavaScript( final String typeAttribute, final String languageAttribute ) {
        // Unless otherwise specified, we have to assume that any script is javascript
        final boolean isJavaScript;
        if( languageAttribute != null && languageAttribute.length() != 0 ) {
            isJavaScript = TextUtil.startsWithIgnoreCase(languageAttribute, "javascript");
        }
        else if( typeAttribute != null && typeAttribute.length() != 0 ) {
            isJavaScript = typeAttribute.equalsIgnoreCase( "text/javascript" );
        }
        else {
            isJavaScript = true;
        }

        return isJavaScript;
    }

    /**
     * 
     * @param url the url of the script
     * @param charset the charset to use to read the text
     * @return the content of the file
     */
    private String loadJavaScriptFromUrl(final URL url, final String charset ) {
        String scriptEncoding = charset;
        getPageEncoding();
        try {
            final WebRequestSettings requestSettings = new WebRequestSettings(url);
            final WebResponse webResponse = getWebClient().loadWebResponse(requestSettings);
            if( webResponse.getStatusCode() == 200 ) {
                final String contentType = webResponse.getContentType();
                final String contentCharset = webResponse.getContentCharSet();
                if (!contentType.equalsIgnoreCase("text/javascript")
                        && !contentType.equalsIgnoreCase("application/x-javascript")) {
                    getLog().warn(
                        "Expected content type of text/javascript or application/x-javascript for remotely "
                        + "loaded javascript element " + url + " but got [" + webResponse.getContentType()+"]");
                }
                if (StringUtils.isEmpty(scriptEncoding)) {
                    if (!contentCharset.equals(TextUtil.DEFAULT_CHARSET)) {
                        scriptEncoding = contentCharset;
                    }
                    else if (!originalCharset_.equals(TextUtil.DEFAULT_CHARSET)) {
                        scriptEncoding = originalCharset_ ;
                    }
                    else {
                        scriptEncoding = TextUtil.DEFAULT_CHARSET;
                    }
                }
                final byte [] data = webResponse.getResponseBody();
                return EncodingUtil.getString(data, 0, data.length, scriptEncoding);
            }
            else {
                getLog().error("Error loading javascript from [" + url.toExternalForm()
                    + "] status=[" + webResponse.getStatusCode() + " "
                    + webResponse.getStatusMessage() + "]");
                throw new FailingHttpStatusCodeException(webResponse);
            }
        }
        catch( final Exception e ) {
            getLog().error("Error loading javascript from [" + url.toExternalForm() + "]: ", e);
            if (getWebClient().isThrowExceptionOnScriptError()) {
                throw new ScriptException(this, e);
            }
        }
        return "";
    }

    /**
     * Return the window that this page is sitting inside.
     *
     * @return The enclosing frame or null if this page isn't inside a frame.
     */
    public WebWindow getEnclosingWindow() {
        return enclosingWindow_;
    }

    /**
     * Set the window that contains this page.
     *
     * @param window The new frame or null if this page is being removed from a frame.
     */
    public void setEnclosingWindow( final WebWindow window ) {
        enclosingWindow_ = window;
    }

    /**
     * Return the title of this page or an empty string if the title wasn't specified.
     *
     * @return the title of this page or an empty string if the title wasn't specified.
     */
    public String getTitleText() {
        final HtmlTitle titleElement = getTitleElement();
        if (titleElement != null) {
            return titleElement.asText();
        }
        return "";
    }

    /**
     * Set the text for the title of this page.  If there is not a title element
     * on this page, then one has to be generated.
     * @param message The new text 
     */
    public void setTitleText(final String message) {
        HtmlTitle titleElement = getTitleElement();
        if (titleElement == null) {
            getLog().debug("No title element, creating one");
            final HtmlHead head = (HtmlHead) getFirstChildElement(getDocumentElement(), HtmlHead.class);
            if (head == null) {
                // perhaps should we create head too?
                throw new IllegalStateException("Headelement was not defined for this page");
            }
            titleElement = new HtmlTitle(null, HtmlTitle.TAG_NAME, this, Collections.EMPTY_MAP);
            if (head.getFirstChild() != null) {
                head.getFirstChild().insertBefore(titleElement);
            }
            else {
                head.appendChild(titleElement);
            }
        }

        titleElement.setNodeValue(message);
    }

    /**
     * Get the first child of startElement that is an instance of the given class.
     * @param startElement The parent element
     * @param clazz The class to search for.
     * @return <code>null</code> if no child found
     */
    private HtmlElement getFirstChildElement(final HtmlElement startElement, final Class clazz) {
        final Iterator iterator = startElement.getChildElementsIterator();
        while (iterator.hasNext()) {
            final HtmlElement element = (HtmlElement) iterator.next();
            if (clazz.isInstance(element)) {
                return element;
            }
        }

        return null;
    }

    /**
     * Get the title element for this page.  Returns null if one is not found.
     * 
     * @return the title element for this page or null if this is not one.
     */
    private HtmlTitle getTitleElement() {
        final HtmlHead head = (HtmlHead) getFirstChildElement(getDocumentElement(), HtmlHead.class);
        if (head != null) {
            return (HtmlTitle) getFirstChildElement(head, HtmlTitle.class);
        }

        return null;
    }

    /**
     * Look for and execute any appropriate onload handlers.  Look for body
     * and frame tags.
     */
    private void executeOnLoadHandlersIfNeeded() {
        if (!getWebClient().isJavaScriptEnabled()) {
            return;
        }

        // onload for the window
        final Window jsWindow = (Window) getEnclosingWindow().getScriptObject();
        if (jsWindow != null) {
            getDocumentElement().fireEvent(Event.TYPE_LOAD);
        }

        // the onload of the contained frames or iframe tags
        final List frames = getDocumentElement().getHtmlElementsByTagNames( Arrays.asList( new String[]{
            "frame", "iframe" } ));
        for (final Iterator iter = frames.iterator(); iter.hasNext();) {
            final BaseFrame frame = (BaseFrame) iter.next();
            final Function frameTagOnloadHandler = frame.getEventHandler("onload"); 
            if (frameTagOnloadHandler != null) {
                getLog().debug("Executing onload handler for " + frame);
                final Event event = new Event(frame, Event.TYPE_LOAD);
                ((NodeImpl) frame.getScriptObject()).executeEvent(event);
            }
        }
    }

    /**
     * If a refresh has been specified either through a meta tag or an http
     * response header, then perform that refresh.
     * @throws IOException if an IO problem occurs
     */
    private void executeRefreshIfNeeded() throws IOException {
        // If this page is not in a frame then a refresh has already happened,
        // most likely through the javascript onload handler, so we don't do a
        // second refresh.
        final WebWindow window = getEnclosingWindow();
        if( window == null ) {
            return;
        }

        final String refreshString = getRefreshStringOrNull();
        if( refreshString == null || refreshString.length() == 0 ) {
            return;
        }

        final int time;
        final URL url;

        int index = refreshString.indexOf( ";" );
        final boolean timeOnly = ( index == -1 );

        if( timeOnly ) {
            // Format: <meta http-equiv='refresh' content='10'>
            try {
                time = Integer.parseInt( refreshString );
            }
            catch( final NumberFormatException e ) {
                getLog().error( "Malformed refresh string (no ';' but not a number): " + refreshString, e );
                return;
            }
            url = webResponse_.getUrl();
        }
        else {
            // Format: <meta http-equiv='refresh' content='10;url=http://www.blah.com'>
            try {
                time = Integer.parseInt( refreshString.substring( 0, index ).trim() );
            }
            catch (final NumberFormatException e) {
                getLog().error("Malformed refresh string (no valid number before ';') " + refreshString, e);
                return;
            }
            index = refreshString.indexOf( "URL=", index );
            if( index == -1 ) {
                index = refreshString.indexOf( "url=", index );
            }
            if( index == -1 ) {
                getLog().error( "Malformed refresh string (found ';' but no 'url='): " + refreshString );
                return;
            }
            final StringBuffer buffer = new StringBuffer( refreshString.substring( index + 4 ) );
            if( buffer.toString().trim().length() == 0 ) {
                //content='10; URL=' is treated as content='10'
                url = webResponse_.getUrl();
            }
            else {
                if( buffer.charAt( 0 ) == '"' || buffer.charAt( 0 ) == 0x27 ) {
                    buffer.deleteCharAt( 0 );
                }
                if( buffer.charAt( buffer.length() - 1 ) == '"' || buffer.charAt( buffer.length() - 1 ) == 0x27 ) {
                    buffer.deleteCharAt( buffer.length() - 1 );
                }
                final String urlString = buffer.toString();
                try {
                    url = getFullyQualifiedUrl( urlString );
                }
                catch( final MalformedURLException e ) {
                    getLog().error( "Malformed url in refresh string: " + refreshString, e );
                    throw e;
                }
            }
        }

        getWebClient().getRefreshHandler().handleRefresh( this, url, time );
    }

    /**
     * Return an auto-refresh string if specified.  This will look in both the meta
     * tags (taking care of &lt;noscript&gt; if any) and inside the http response headers.
     * @return the auto-refresh string.
     */
    private String getRefreshStringOrNull() {
        final Iterator iterator = getMetaTags("refresh").iterator();
        final boolean javaScriptEnabled = getWebClient().isJavaScriptEnabled();
        while (iterator.hasNext()) {
            final HtmlMeta meta = (HtmlMeta) iterator.next();
            if ((!javaScriptEnabled || getFirstParent(meta, HtmlNoScript.TAG_NAME) == null)) {
                return meta.getContentAttribute();
            }
        }

        return getWebResponse().getResponseHeaderValue("Refresh");
    }

    /**
     * Gets the first parent with the given node name
     * @param node the node to start with
     * @param nodeName the name of the search node
     * @return <code>null</code> if no parent found with this name
     */
    private DomNode getFirstParent(final DomNode node, final String nodeName) {
        DomNode parent = node.getParentNode();
        while (parent != null) {
            if (parent.getNodeName().equals(nodeName)) {
                return parent;
            }
            parent = parent.getParentNode();
        }
        return null;
    }

    /**
     * Deregister frames that are no longer in use.
     */
    public void deregisterFramesIfNeeded() {
        for (final Iterator iter = getFrames().iterator(); iter.hasNext();) {
            final WebWindow window = (WebWindow) iter.next();
            webClient_.deregisterWebWindow( window );
            if (window.getEnclosedPage() instanceof HtmlPage) {
                final HtmlPage page = (HtmlPage) window.getEnclosedPage();
                if (page != null) {
                    // seems quite silly, but for instance if the src attribute of an iframe is not
                    // set, the error only occurs when leaving the page
                    page.deregisterFramesIfNeeded();
                }
            }
        }
    }

    /**
     * Return a list containing all the frames (from frame and iframe tags) in this page.
     * @return a list of {@link FrameWindow}
     */
    public List getFrames() {
        final List list = new ArrayList();
        final WebWindow enclosingWindow = getEnclosingWindow();
        for (final Iterator iter = getWebClient().getWebWindows().iterator(); iter.hasNext();)
        {
            final WebWindow window = (WebWindow) iter.next();
            // quite strange but for a TopLevelWindow parent == self
            if (enclosingWindow == window.getParentWindow()
                    && enclosingWindow != window) {
                list.add(window);
            }
        }
        return list;
    }

    /**
     * Returns the first frame contained in this page with the specified name.
     * @param name The name to search for
     * @return The first frame found.
     * @exception ElementNotFoundException If no frame exist in this page with the specified name.
     */
    public FrameWindow getFrameByName(final String name) throws ElementNotFoundException {
        final List frames = getFrames();
        for (final Iterator iter = frames.iterator(); iter.hasNext();) {
            final FrameWindow frame = (FrameWindow) iter.next();
            if (frame.getName().equals(name)) {
                return frame;
            }
        }

        throw new ElementNotFoundException("frame or iframe", "name", name);
    }

    /**
     * Simulate pressing an access key.  This may change the focus, may click buttons and may invoke
     * javascript.
     *
     * @param accessKey The key that will be pressed.
     * @return The element that has the focus after pressing this access key or null if no element
     * has the focus.
     * @throws IOException If an io error occurs during the processing of this access key.  This
     * would only happen if the access key triggered a button which in turn caused a page load.
     */
    public HtmlElement pressAccessKey( final char accessKey ) throws IOException {
        final HtmlElement element = getHtmlElementByAccessKey(accessKey);
        if( element != null ) {
            if( element instanceof FocusableElement ) {
                ((FocusableElement) element).focus();
            }

            final Page newPage;
            if( element instanceof HtmlAnchor ) {
                newPage = ((HtmlAnchor)element).click();
            }
            else if( element instanceof HtmlArea ) {
                newPage = ((HtmlArea)element).click();
            }
            else if( element instanceof HtmlButton ) {
                newPage = ((HtmlButton)element).click();
            }
            else if( element instanceof HtmlInput ) {
                newPage = ((HtmlInput)element).click();
            }
            else if( element instanceof HtmlLabel ) {
                newPage = ((HtmlLabel)element).click();
            }
            else if( element instanceof HtmlLegend ) {
                newPage = ((HtmlLegend)element).click();
            }
            else if( element instanceof HtmlTextArea ) {
                newPage = ((HtmlTextArea)element).click();
            }
            else {
                newPage = this;
            }

            if( newPage != this && getElementWithFocus() == element ) {
                // The page was reloaded therefore no element on this page will have the focus.
                getElementWithFocus().blur();
            }
        }

        return getElementWithFocus();
    }

    /**
     * Move the focus to the next element in the tab order.  To determine the specified tab
     * order, refer to {@link HtmlPage#getTabbableElements()}
     *
     * @return The element that has focus after calling this method.
     */
    public HtmlElement tabToNextElement() {
        final List elements = getTabbableElements();
        if (elements.isEmpty()) {
            moveFocusToElement(null);
            return null;
        }

        final HtmlElement elementToGiveFocus;
        final FocusableElement elementWithFocus = getElementWithFocus();
        if( elementWithFocus == null ) {
            elementToGiveFocus = (HtmlElement) elements.get(0);
        }
        else {
            final int index = elements.indexOf( elementWithFocus );
            if( index == -1 ) {
                // The element with focus isn't on this page
                elementToGiveFocus = (HtmlElement)elements.get(0);
            }
            else {
                if( index == elements.size() - 1 ) {
                    elementToGiveFocus = (HtmlElement)elements.get(0);
                }
                else {
                    elementToGiveFocus = (HtmlElement)elements.get(index+1);
                }
            }
        }

        if( elementToGiveFocus instanceof FocusableElement ) {
            moveFocusToElement((FocusableElement) elementToGiveFocus);
        }
        return elementToGiveFocus;
    }

    /**
     * Move the focus to the previous element in the tab order.  To determine the specified tab
     * order, refer to {@link HtmlPage#getTabbableElements()}
     *
     * @return The element that has focus after calling this method.
     */
    public HtmlElement tabToPreviousElement() {
        final List elements = getTabbableElements();
        if( elements.isEmpty() ) {
            moveFocusToElement(null);
            return null;
        }

        final HtmlElement elementToGiveFocus;
        final FocusableElement elementWithFocus = getElementWithFocus();
        if( elementWithFocus == null ) {
            elementToGiveFocus = (HtmlElement)elements.get(elements.size()-1);
        }
        else {
            final int index = elements.indexOf( elementWithFocus );
            if( index == -1 ) {
                // The element with focus isn't on this page
                elementToGiveFocus = (HtmlElement)elements.get(elements.size()-1);
            }
            else {
                if( index == 0 ) {
                    elementToGiveFocus = (HtmlElement)elements.get(elements.size()-1);
                }
                else {
                    elementToGiveFocus = (HtmlElement)elements.get(index-1);
                }
            }
        }

        if( elementToGiveFocus instanceof FocusableElement ) {
            moveFocusToElement((FocusableElement) elementToGiveFocus);
        }
        return elementToGiveFocus;
    }

    /**
     *  Return the html element with the specified id. If more than one element
     *  has this id (not allowed by the html spec) then return the first one.
     *
     * @param id The id value to search by
     * @return The html element found
     * @exception ElementNotFoundException If no element was found that matches
     *      the id
     */
    public HtmlElement getHtmlElementById( final String id )
        throws ElementNotFoundException {

        final List elements = (List) idMap_.get(id);
        if (elements != null) {
            return (HtmlElement) elements.get(0);
        }
        throw new ElementNotFoundException( "*", "id", id );
    }

    /**
     * Add an element to the ID map.
     *
     * @param idElement the element with an ID attribute to add.
     */
    void addIdElement(final HtmlElement idElement) {
        final String id = idElement.getId();
        if (!StringUtils.isEmpty(id)) {
            List elements = (List) idMap_.get(id);
            if (elements == null) {
                elements = new Vector();
                elements.add(idElement);
                idMap_.put(id, elements);
            }
            else if (!elements.contains(idElement)) {
                elements.add(idElement);
            }
        }
    }

    /**
     * Remove an element and optionally its children from the ID map.
     * @param idElement the element with an ID attribute to remove.
     * @param recursive indicates if children must be removed too
     */
    void removeIdElement(final HtmlElement idElement, final boolean recursive) {
        final List elements = (List) idMap_.remove(idElement.getAttributeValue("id"));
        // if other elements have the same id (not legal for spec, but happens), then
        // only one element to remove
        if (elements != null && elements.size() != 1) {
            elements.remove(idElement);
            idMap_.put(idElement.getAttributeValue("id"), elements);
        }

        // remove ids from children
        if (recursive) {
            for (final Iterator iter=idElement.getChildElementsIterator(); iter.hasNext();) {
                final HtmlElement child = (HtmlElement) iter.next();
                removeIdElement(child, true);
            }
        }
    }
    
    /**
     * Remove an element from the ID map.
     *
     * @param idElement the element with an ID attribute to remove.
     */
    void removeIdElement(final HtmlElement idElement) {
        removeIdElement(idElement, false);
    }
    
    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * 
     * @param node the node that has just been added to the document.
     */
    void notifyNodeAdded(final DomNode node) {
        if (node instanceof HtmlElement) {
            boolean insideNoScript = false;
            if (getWebClient().isJavaScriptEnabled()) {
                for( DomNode parent = node.getParentNode(); parent != null; parent = parent.getParentNode()) {
                    if (parent instanceof HtmlNoScript) {
                        insideNoScript = true;
                        break;
                    }
                }
            }
            if (!insideNoScript) {
                addIdElement((HtmlElement) node);
            }
        }
        node.onAddedToPage();
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
     * 
     * @param node the node that has just been removed from the tree
     */
    void notifyNodeRemoved(final DomNode node) {
        if (node instanceof HtmlElement) {
            removeIdElement((HtmlElement) node, true);
        }
    }

    /**
     * Loads the content of the contained frames. This is done after the page is completely
     * loaded to allow script contained in the frames to reference elements from the
     * page located after the closing </frame> tag. 
     * @throws FailingHttpStatusCodeException If the server returns a
     *      failing status code AND the property
     *      {@link WebClient#setThrowExceptionOnFailingStatusCode(boolean)} is set to true.
     */
    void loadFrames() throws FailingHttpStatusCodeException{
        final List frameTags = Arrays.asList(new String[] {"frame", "iframe"});
        final List frames = getDocumentElement().getHtmlElementsByTagNames(frameTags);
        for (final Iterator iter=frames.iterator(); iter.hasNext();) {
            ((BaseFrame) iter.next()).loadInnerPage();
        }
    }

    /**
     * {@inheritDoc}
     */
    public String asXml() {
        return getDocumentElement().asXml();
    }

    /**
     * Gives a basic representation for debugging purposes
     * @return a basic representation
     */
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("HtmlPage(");
        buffer.append(getWebResponse().getUrl());
        buffer.append(")@");
        buffer.append(hashCode());
        return buffer.toString();
    }

    /**
     * Move the focus to the specified component.  This will trigger any relevant javascript
     * event handlers.
     *
     * @param newElement The element that will receive the focus, use <code>null</code> to remove focus from any element
     * @return true if the specified element now has the focus.
     * @see #getElementWithFocus()
     * @see #tabToNextElement()
     * @see #tabToPreviousElement()
     * @see #pressAccessKey(char)
     * @see #assertAllTabIndexAttributesSet()
     */
    public boolean moveFocusToElement(final FocusableElement newElement) {

        if (elementWithFocus_ == newElement) {
            // nothing to do
            return true;
        }
        else if (newElement != null && newElement.getPage() != this) {
            throw new IllegalArgumentException("Can't move focus to an element from an other page");
        }

        if (elementWithFocus_ != null) {
            elementWithFocus_.fireEvent(Event.TYPE_BLUR);
        }

        elementWithFocus_ = newElement;
        if (newElement != null) {
            newElement.fireEvent(Event.TYPE_FOCUS);
        }

        // If a page reload happened as a result of the focus change then obviously this
        // element will not have the focus because its page has gone away.
        return this == getEnclosingWindow().getEnclosedPage();
    }

    /**
     * Return the element with the focus or null if no element has the focus.
     * @return The element with focus or null.
     * @see #moveFocusToElement(FocusableElement)
     */
    public FocusableElement getElementWithFocus() {
        return elementWithFocus_;
    }

    /**
     * Gets the meta tag for a given http-equiv value.
     * @param httpEquiv the http-equiv value
     * @return a list of {@link HtmlMeta}
     */
    protected List getMetaTags(final String httpEquiv) {
        final String nameLC = httpEquiv.toLowerCase();
        final List tags = getDocumentElement().getHtmlElementsByTagNames(Collections.singletonList("meta"));
        for (final Iterator iter = tags.iterator(); iter.hasNext();) {
            final HtmlMeta element = (HtmlMeta) iter.next();
            if (!nameLC.equals(element.getHttpEquivAttribute().toLowerCase())) {
                iter.remove();
            }
        }
        return tags;
    }

    /**
     * Select the specified radio button in the page (outside any &lt;form&gt;).
     *
     * @param radioButtonInput The radio Button
     */
    void setCheckedRadioButton( final HtmlRadioButtonInput radioButtonInput ) {
        try {
            //May be done in single xpath search?
            final List pageInputs = getByXPath("//input[lower-case(@type)='radio' "
                    + "and @name='" + radioButtonInput.getNameAttribute() + "']");
            final List formInputs = getByXPath("//form//input[lower-case(@type)='radio' "
                    + "and @name='" + radioButtonInput.getNameAttribute() + "']");
            
            pageInputs.removeAll(formInputs);

            for( final Iterator iterator = pageInputs.iterator(); iterator.hasNext();) {
                final HtmlRadioButtonInput input = (HtmlRadioButtonInput)iterator.next();
                if( input == radioButtonInput ) {
                    input.setAttributeValue("checked", "checked");
                }
                else {
                    input.removeAttribute("checked");
                }
            }
        }
        catch( final JaxenException e ) {
            getLog().error( e );
        }
    }

    /**
     * Creates a clone of this instance, and clears cached state 
     * to be not shared with the original.
     * 
     * @return a clone of this instance.
     */
    protected Object clone() {
        try {
            final HtmlPage result = (HtmlPage)super.clone();
            result.documentElement_ = null;
            result.elementWithFocus_ = null;
            result.idMap_ = new HashMap();
            return result;
        }
        catch( final CloneNotSupportedException e ) {
            throw new IllegalStateException("Clone not supported");
        }
    }
    
    /**
     * Override cloneNode to add cloned elements to the clone, not to the original. 
     * {@inheritDoc}
     */
    public DomNode cloneNode(final boolean deep) {
        final HtmlPage result = (HtmlPage)super.cloneNode(deep);
        if(deep) {
            // fix up idMap_ and result's idMap_s 
            final Iterator it = result.getAllHtmlChildElements();
            while(it.hasNext()) {
                final HtmlElement child = (HtmlElement)it.next();
                removeIdElement(child);
                result.addIdElement(child);
            }
        }
        return result;
    }

    /**
     * Adds an HtmlAttributeChangeListener to the listener list.
     * The listener is registered for all attributes of all HtmlElements contained in this page.
     * 
     * @param listener the attribute change listener to be added.
     * @see #removeHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void addHtmlAttributeChangeListener( final HtmlAttributeChangeListener listener ) {
        Assert.notNull("listener", listener);
        synchronized( this ) {
            if( attributeListeners_ == null ) {
                attributeListeners_ = new ArrayList();
            }
            attributeListeners_.add( listener );
        }
    }

    /**
     * Removes an HtmlAttributeChangeListener from the listener list.
     * This method should be used to remove HtmlAttributeChangeListener that were registered
     * for all attributes of all HtmlElements contained in this page.
     * 
     * @param listener the attribute change listener to be removed.
     * @see #addHtmlAttributeChangeListener(HtmlAttributeChangeListener)
     */
    public void removeHtmlAttributeChangeListener( final HtmlAttributeChangeListener listener ) {
        Assert.notNull("listener", listener);
        synchronized( this ) {
            if( attributeListeners_ != null ) {
                attributeListeners_.remove( listener );
            }
        }
    }
    
    /**
     * Notifies all registered listeners for the given event to add an attribute.
     * @param event the event to fire
     */
    void fireHtmlAttributeAdded(final HtmlAttributeChangeEvent event) {
        synchronized (this) {
            if( attributeListeners_ != null ) {
                for( final Iterator iterator = attributeListeners_.iterator(); iterator.hasNext();) {
                    final HtmlAttributeChangeListener listener = (HtmlAttributeChangeListener)iterator.next();
                    listener.attributeAdded(event);
                }
            }
        }
    }

    /**
     * Notifies all registered listeners for the given event to replace an attribute.
     * @param event the event to fire
     */
    void fireHtmlAttributeReplaced(final HtmlAttributeChangeEvent event) {
        synchronized (this) {
            if( attributeListeners_ != null ) {
                for( final Iterator iterator = attributeListeners_.iterator(); iterator.hasNext();) {
                    final HtmlAttributeChangeListener listener = (HtmlAttributeChangeListener)iterator.next();
                    listener.attributeReplaced(event);
                }
            }
        }
    }
    
    /**
     * Notifies all registered listeners for the given event to remove an attribute.
     * @param event the event to fire
     */
    void fireHtmlAttributeRemoved(final HtmlAttributeChangeEvent event) {
        synchronized (this) {
            if( attributeListeners_ != null ) {
                for( final Iterator iterator = attributeListeners_.iterator(); iterator.hasNext();) {
                    final HtmlAttributeChangeListener listener = (HtmlAttributeChangeListener)iterator.next();
                    listener.attributeRemoved(event);
                }
            }
        }
    }
}
