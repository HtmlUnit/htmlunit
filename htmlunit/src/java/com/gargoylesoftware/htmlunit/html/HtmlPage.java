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

import org.apache.commons.httpclient.util.EncodingUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequestSettings;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 *  A representation of an html page returned from a server.  This class is the
 *  DOM Document implementation.
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Alex Nikiforoff
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Chris Erskine
 * @author Marc Guillemot
 */
public final class HtmlPage extends DomNode implements Page {

    private final WebClient webClient_;
    private final URL originatingUrl_;
    private       String originalCharset_ = null;
    private final WebResponse webResponse_;
    private final Map idMap_ = new HashMap();
    private       HtmlElement documentElement_ = null;

    private WebWindow enclosingWindow_;

    private static int FunctionWrapperCount_ = 0;
    private final Log javascriptLog_ = LogFactory.getLog("com.gargoylesoftware.htmlunit.javascript");
    
    private static final int TAB_INDEX_NOT_SPECIFIED = -10;
    private static final int TAB_INDEX_OUT_OF_BOUNDS = -20;

    /**
     *  This constructor should no longer be used
     *
     * @param  webClient NOT USED
     * @param  webResponse The web response that was used to create this page
     * @param  originatingUrl The url that was used to load this page.
     * @param  webWindow The window that this page is being loaded into.
     * @deprecated
     */
    public HtmlPage(
            final WebClient webClient,
            final URL originatingUrl,
            final WebResponse webResponse,
            final WebWindow webWindow ) {
        this(originatingUrl, webResponse, webWindow);
    }    
    /**
     *  Create an instance of HtmlPage
     *
     * @param  originatingUrl The url that was used to load this page.
     * @param  webResponse The web response that was used to create this page
     * @param  webWindow The window that this page is being loaded into.
     */
    public HtmlPage(
            final URL originatingUrl,
            final WebResponse webResponse,
            final WebWindow webWindow ) {

        super(null);

        webClient_ = webWindow.getWebClient();
        originatingUrl_ = originatingUrl;
        webResponse_ = webResponse;
        setEnclosingWindow(webWindow);

        final ScriptEngine engine = getWebClient().getScriptEngine();
        if( engine != null ) {
            engine.initialize(this);
        }
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
     */
    public void initialize() throws IOException {
        insertTbodyTagsAsNeeded();
        documentElement_.setReadyState(READY_STATE_COMPLETE);
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
        if( originalCharset_ != null ) {
            return originalCharset_ ;
        }
        final ArrayList ar = new ArrayList();
        ar.add("meta");
        final List list = getDocumentElement().getHtmlElementsByTagNames(ar);
        for(int i=0; i<list.size();i++ ){
            final HtmlMeta meta = (HtmlMeta) list.get(i);
            final String httpequiv = meta.getHttpEquivAttribute();
            if( "content-type".equals(httpequiv.toLowerCase()) ){
                final String contents = meta.getContentAttribute();
                final int pos = contents.toLowerCase().indexOf("charset=");
                if( pos>=0 ){
                    originalCharset_ = contents.substring(pos+8);
                    getLog().debug("Page Encoding detected: " + originalCharset_);
                    return originalCharset_;
                }
            }
        }
        if( originalCharset_ == null ) {
            originalCharset_ = webResponse_.getContentCharSet();
        }
        return originalCharset_;
    }

    /**
     * Create a new HTML element with the given tag name.
     *
     * @param tagName The tag name, preferrably in lowercase
     * @return the new HTML element.
     */
    public HtmlElement createElement(final String tagName) {
        final String tagLower = tagName.toLowerCase();
        return HTMLParser.getFactory(tagLower).createElement(this, tagLower, null);
    }

    /**
     *  Return the HtmlAnchor with the specified name
     *
     * @param  name The name to search by
     * @return  See above
     * @throws com.gargoylesoftware.htmlunit.ElementNotFoundException If the anchor could not be found.
     */
    public HtmlAnchor getAnchorByName( final String name ) throws ElementNotFoundException {
        return ( HtmlAnchor )getDocumentElement().getOneHtmlElementByAttribute( "a", "name", name );
    }

    /**
    *  Return the {@link HtmlAnchor} with the specified href
     *
     * @param  href The string to search by
     * @return  The HtmlAnchor
     * @throws ElementNotFoundException If the anchor could not be found.
     */
    public HtmlAnchor getAnchorByHref( final String href ) throws ElementNotFoundException {
        return ( HtmlAnchor )getDocumentElement().getOneHtmlElementByAttribute( "a", "href", href );
    }


    /**
     * Return a list of all anchors contained in this page.
     * @return All the anchors in this page.
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
    public List getAllForms() {
        return getDocumentElement().getHtmlElementsByTagNames( Arrays.asList(new String[]{"form"}) );
    }

    /**
     *  Return the WebClient that originally loaded this page
     *
     * @return  See above
     */
    public WebClient getWebClient() {
        return webClient_;
    }

    /**
     *  Given a relative url (ie /foo), return a fully qualified url based on
     *  the url that was used to load this page
     *
     * @param  relativeUrl The relative url
     * @return  See above
     * @exception  MalformedURLException If an error occurred when creating a
     *      URL object
     */
    public URL getFullyQualifiedUrl( final String relativeUrl )
        throws MalformedURLException {

        final List baseElements = getDocumentElement().getHtmlElementsByTagNames( Collections.singletonList("base"));
        final URL baseUrl;
        if( baseElements.isEmpty() ) {
            baseUrl = originatingUrl_;
        }
        else {
            final HtmlBase htmlBase = (HtmlBase)baseElements.get(0);
            final String href = htmlBase.getHrefAttribute();
            if (href == null || href.length() == 0) {
                baseUrl = originatingUrl_;
            }
            else {
                baseUrl = new URL(href);
            }
        }
        return WebClient.expandUrl( baseUrl, relativeUrl );
    }

    /**
     *  Given a target attribute value, resolve the target using a base
     *  target for the page.
     *
     * @param elementTarget The target specified as an attribute of the element.
     * @return  The resolved target to use for the element.
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
     * @return  The web response
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }


    /**
     *  Return a list of ids (strings) that correspond to the tabbable elements
     *  in this page. Return them in the same order specified in {@link
     *  #getTabbableElements}
     *
     * @return  The list of id's
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
     * @return  A list containing all the tabbable elements in proper tab order.
     */
    public List getTabbableElements() {

        final List acceptableTagNames = Arrays.asList(
                new Object[]{"a", "area", "button", "input", "object", "select", "textarea"} );
        final List tabbableElements = new ArrayList();

        final DescendantElementsIterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = iterator.nextElement();
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
     * @param  accessKey The key to look for
     * @return  The html element that is assigned to the specified key or null
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
     * @param  accessKey The key to look for
     * @return  A list of html elements that are assigned to the specified accesskey.
     */
    public List getHtmlElementsByAccessKey( final char accessKey ) {

        final List elements = new ArrayList();

        final String searchString = ( "" + accessKey ).toLowerCase();
        final List acceptableTagNames = Arrays.asList(
                new Object[]{"a", "area", "button", "input", "label", "legend", "textarea"} );

        final DescendantElementsIterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final HtmlElement element = iterator.nextElement();
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

        final DescendantElementsIterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final String id = iterator.nextElement().getAttributeValue("accesskey" );
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

        final DescendantElementsIterator iterator = getAllHtmlChildElements();
        while( iterator.hasNext() ) {
            final String id = iterator.nextElement().getAttributeValue("id");
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
     * @param wrapSourceInFunction True if this snippet of code should be placed inside
     * a javascript function.  This is neccessary for intrinsic event handlers that may
     * try to return a value.
     * @param htmlElement The html element for which this script is being executed.
     * This element will be the context during the javascript execution.  If null,
     * the context will default to the page.
     * @return A ScriptResult which will contain both the current page (which may be different than
     * the previous page and a javascript result object.
     */
    public ScriptResult executeJavaScriptIfPossible(
        String sourceCode, final String sourceName, final boolean wrapSourceInFunction,
        final HtmlElement htmlElement ) {

        final ScriptEngine engine = getWebClient().getScriptEngine();
        if( engine == null ) {
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
        final Object result;
        if( wrapSourceInFunction ) {
            // Something that isn't likely to collide with the name of a function in the
            // loaded javascript
            final String wrapperName = "GargoyleWrapper"+(FunctionWrapperCount_++);
            sourceCode = "function "+wrapperName+"() {"+sourceCode+"\n}";
            getJsLog().debug("Now build JS function\n" + sourceCode);
            engine.execute( this, sourceCode, "Wrapper definition for "+sourceName, htmlElement );
            result = engine.execute( this, wrapperName+"()", sourceName, htmlElement );
        }
        else {
            result = engine.execute( this, sourceCode, sourceName, htmlElement );
        }

        WebWindow firstWindow = getWebClient().popFirstWindow();
        if ( firstWindow == null) {
            firstWindow = window;
        }

        return new ScriptResult( result, firstWindow.getEnclosedPage() );
    }

    /**
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
            final HtmlElement htmlElementScope) {
        
        final WebWindow window = getEnclosingWindow();
        getWebClient().pushClearFirstWindow();

        final ScriptEngine engine = getWebClient().getScriptEngine();
        if( engine == null ) {
            return new ScriptResult(null, this);
        }

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
     * Internal use only.
     * @param srcAttribute The source attribute from the script tag.
     * @param charset The charset attribute from the script tag.
     */
    public void loadExternalJavaScriptFile( final String srcAttribute,
                                            final String charset  ) {
        final ScriptEngine engine = getWebClient().getScriptEngine();
        if( engine != null ) {
            engine.execute( this, loadJavaScriptFromUrl( srcAttribute, charset ), srcAttribute, null );
        }
    }

    /**
     * Internal use only.  Return true if a script with the specified type and language attributes
     * is actually JavaScript.
     * @param typeAttribute The type attribute specified in the script tag.
     * @param languageAttribute The language attribute specified in the script tag.
     * @return true if the script is javascript
     */
    public static boolean isJavaScript( final String typeAttribute, final String languageAttribute ) {
        // Unless otherwise specified, we have to assume that any script is javascript
        final boolean isJavaScript;
        if( languageAttribute != null && languageAttribute.length() != 0  ) {
            isJavaScript = TextUtil.startsWithIgnoreCase(languageAttribute, "javascript");
        }
        else if(  typeAttribute != null && typeAttribute.length() != 0 ) {
            isJavaScript = typeAttribute.equals( "text/javascript" );
        }
        else {
            isJavaScript = true;
        }

        return isJavaScript;
    }

    private String loadJavaScriptFromUrl( final String urlString,
                                          final String charset ) {
        URL url = null;
        String scriptEncoding = charset;
        getPageEncoding();
        try {
            url = getFullyQualifiedUrl(urlString);
            final WebRequestSettings requestSettings = new WebRequestSettings(url);
            final WebResponse webResponse = getWebClient().loadWebResponse(requestSettings);
            if( webResponse.getStatusCode() == 200 ) {
                final String contentType = webResponse.getContentType();
                final String contentCharset = webResponse.getContentCharSet();
                if( contentType.equals("text/javascript") == false
                        && contentType.equals("application/x-javascript") == false ) {
                    getLog().warn(
                        "Expected content type of text/javascript or application/x-javascript for remotely "
                        + "loaded javascript element " + url + " but got [" + webResponse.getContentType()+"]");
                }
                if( scriptEncoding == null || scriptEncoding.length() == 0 ){
                    if( contentCharset.equals("ISO-8859-1")==false ) {
                        scriptEncoding = contentCharset;
                    }
                    else if( originalCharset_.equals("ISO-8859-1")==false ){
                        scriptEncoding = originalCharset_ ;
                    }
                }
                if( scriptEncoding == null || scriptEncoding.length() == 0 ){
                    scriptEncoding = "ISO-8859-1";
                }
                final byte [] data = webResponse.getResponseBody();
                return EncodingUtil.getString(data, 0, data.length, scriptEncoding );
            }
            else {
                getLog().error("Error loading javascript from ["+url.toExternalForm()
                    +"] status=["+webResponse.getStatusCode()+" "
                    +webResponse.getStatusMessage()+"]");
            }
        }
        catch( final MalformedURLException e ) {
            getLog().error("Unable to build url for script src tag ["+urlString+"]");
            return "";
        }
        catch( final Exception e ) {
            getLog().error("Error loading javascript from ["+url.toExternalForm()+"]: ", e);
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
            titleElement = new HtmlTitle(this, Collections.EMPTY_MAP);
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
        if (jsWindow != null && jsWindow.jsxGet_onload() != null) {
            final ScriptEngine engine = getWebClient().getScriptEngine();
            getLog().debug("Executing onload handler for the window");
            engine.callFunction(this, jsWindow.jsxGet_onload(), jsWindow, new Object[]{}, null);
        }

        // the onload of the contained frames or iframe tags
        final List list = getDocumentElement().getHtmlElementsByTagNames( Arrays.asList( new String[]{
            "frame", "iframe" } ));
        for (final Iterator iter = list.iterator(); iter.hasNext();) {
            final BaseFrame frame = (BaseFrame) iter.next();
            getLog().debug("Executing onload handler for " + frame);
            executeOneOnLoadHandler(frame);
        }
    }

    /**
     * Execute a single onload handler.  This will either be a string which
     * will be executed as javascript, or a javascript Function.
     * @param element The element that contains the onload attribute.
     */
    private void executeOneOnLoadHandler(final HtmlElement element) {
        getLog().debug("Executing onload handler, for " + element);
        final Function onloadFunction = element.getEventHandler("onload");
        if (onloadFunction != null) {
            final ScriptEngine engine = getWebClient().getScriptEngine();
            engine.callFunction(this, onloadFunction, element.getScriptObject(), new Object[]{}, element);
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
        boolean timeOnly = ( index == -1 );

        if( timeOnly ) {
            // Format: <meta http-equiv='refresh' content='10'>
            try {
                time = Integer.parseInt( refreshString );
            }
            catch( final NumberFormatException e ) {
                getLog().error( "Malformed refresh string (no ';' but not a number): " + refreshString, e );
                return;
            }
            url = this.originatingUrl_;
        }
        else {
            // Format: <meta http-equiv='refresh' content='10;url=http://www.blah.com'>
            time = Integer.parseInt( refreshString.substring( 0, index ) );
            index = refreshString.indexOf( "URL=", index );
            if( index == -1 ) {
                index = refreshString.indexOf( "url=", index );
            }
            if( index == -1 ) {
                getLog().error( "Malformed refresh string (found ';' but no 'url='): " + refreshString );
                return;
            }
            final StringBuffer buffer = new StringBuffer( refreshString.substring( index + 4 ) );
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

        getWebClient().getRefreshHandler().handleRefresh( this, url, time );
    }

    /**
     * Return an auto-refresh string if specified.  This will look in both the meta
     * tags (taking care of &lt;noscript&gt; if any) and inside the http response headers.
     * @return the auto-refresh string.
     */
    private String getRefreshStringOrNull() {
        final Iterator iterator
            = getDocumentElement().getHtmlElementsByTagNames( Collections.singletonList("meta") ).iterator();
        final boolean javaScriptEnabled = getWebClient().isJavaScriptEnabled();
        while( iterator.hasNext() ) {
            final HtmlMeta meta = (HtmlMeta) iterator.next();
            if( meta.getHttpEquivAttribute().equalsIgnoreCase("refresh") 
                    && (!javaScriptEnabled || getFirstParent(meta, HtmlNoScript.TAG_NAME) == null)) {
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
     * @return a list of {@link BaseFrame.FrameWindow}
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
     * Returns the first frame contained in this page with the specifed name.
     * @param name The name to search for
     * @return The first frame found.
     * @exception ElementNotFoundException If no frame exist in this page with the specified name.
     */
    public BaseFrame.FrameWindow getFrameByName(final String name) throws ElementNotFoundException {
        final List frames = getFrames();
        for (final Iterator iter = frames.iterator(); iter.hasNext();) {
            final BaseFrame.FrameWindow frame = (BaseFrame.FrameWindow) iter.next();
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
        final WebClient webClient = getWebClient();
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

            if( newPage != this && webClient.getElementWithFocus() == element ) {
                // The page was reloaded therefore no element on this page will have the focus.
                webClient.getElementWithFocus().blur();
            }
        }

        return webClient.getElementWithFocus();
    }


    /**
     * Move the focus to the next element in the tab order.  To determine the specified tab
     * order, refer to {@link HtmlPage#getTabbableElements()}
     *
     * @return The element that has focus after calling this method.
     */
    public HtmlElement tabToNextElement() {
        final List elements = getTabbableElements();
        final FocusableElement elementWithFocus = getWebClient().getElementWithFocus();
        if( elements.isEmpty() ) {
            if (elementWithFocus != null) {
                elementWithFocus.blur();
            }
            return null;
        }

        final HtmlElement elementToGiveFocus;
        if( elementWithFocus == null ) {
            elementToGiveFocus = (HtmlElement)elements.get(0);
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
            ((FocusableElement) elementToGiveFocus).focus();
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
        final FocusableElement elementWithFocus = getWebClient().getElementWithFocus();
        if( elements.isEmpty() ) {
            if (elementWithFocus != null) {
                elementWithFocus.blur();
            }
            return null;
        }

        final HtmlElement elementToGiveFocus;
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
            ((FocusableElement) elementToGiveFocus).focus();
        }
        return elementToGiveFocus;
    }

    /**
     *  Return the html element with the specified id. If more than one element
     *  has this id (not allowed by the html spec) then return the first one.
     *
     * @param  id The id value to search by
     * @return  The html element found
     * @exception  ElementNotFoundException If no element was found that matches
     *      the id
     */
    public HtmlElement getHtmlElementById( final String id )
        throws ElementNotFoundException {

        final HtmlElement idElement = (HtmlElement) idMap_.get(id);
        if(idElement != null) {
            return idElement;
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
            idMap_.put(id, idElement);
        }
    }

    /**
     * Remove an element from the ID map.
     *
     * @param idElement the element with an ID attribute to remove.
     */
    void removeIdElement(final HtmlElement idElement) {
        idMap_.remove(idElement.getAttributeValue("id"));
    }
    
    private void insertTbodyTagsAsNeeded() {
        final Iterator iterator = getDocumentElement().getHtmlElementsByTagName("table").iterator();
        while( iterator.hasNext() ) {
            final HtmlTable table = (HtmlTable)iterator.next();
            DomNode child = table.getFirstChild();
            while( child != null && child instanceof HtmlElement == false ) {
                child = child.getNextSibling();
            }
            if( child instanceof HtmlTableRow ) {
                final HtmlTableBody body = new HtmlTableBody(this, Collections.EMPTY_MAP);
                final List nodesToMove = new ArrayList();
                child = table.getFirstChild();
                while( child != null ) {
                    nodesToMove.add(child);
                    child = child.getNextSibling();
                }
                
                final Iterator movingIterator = nodesToMove.iterator();
                while( movingIterator.hasNext() ) {
                    child = (DomNode)movingIterator.next();
                    body.appendChild(child);
                }
                table.appendChild(body);
            }
        }
    }
    
    /**
     * Executes the onchange script code for this element if this is appropriate. 
     * This means that the element must have an onchange script, script must be enabled 
     * and the change in the element must not have been triggered by a script.
     * 
     * @param htmlElement The element that contains the onchange attribute.
     * @return The page that occupies this window after this method completes. It
     * may be this or it may be a freshly loaded page. 
     */
    Page executeOnChangeHandlerIfAppropriate(final HtmlElement htmlElement) {
        final Function onchange = htmlElement.getEventHandler("onchange");
        final ScriptEngine engine = getWebClient().getScriptEngine();
        if (onchange != null && getWebClient().isJavaScriptEnabled()
                && engine != null && !engine.isScriptRunning()) {

            final Event event = new Event(this, this.getScriptObject());
            final Object[] args = new Object[] {event};
            
            final ScriptResult scriptResult =
                executeJavaScriptFunctionIfPossible(
                        onchange, 
                        (Scriptable)htmlElement.getScriptObject(),
                        args,
                        htmlElement);
            
            return scriptResult.getNewPage();
        }

        return this;
    }

    /**
     * For internal used only
     * @param node the node that has just been added to the document.
     */
    void notifyNodeAdded(final DomNode node) {
        if (node instanceof HtmlElement) {
            addIdElement((HtmlElement) node);
        }
        if (node instanceof BaseFrame) {
            ((BaseFrame) node).loadInnerPage();
        }
        if (node instanceof HtmlScript) {
            final HtmlScript scriptNode = (HtmlScript) node;
            getLog().debug("Script node added: " + scriptNode.asXml());
            scriptNode.executeScriptIfNeeded();
        }
    }

    /**
     * For internal used only
     * @param node the node that has just been removed from the tree
     */
    void notifyNodeRemoved(final DomNode node) {
        if (node instanceof HtmlElement) {
            removeIdElement((HtmlElement) node);
        }
    }
}
