/*
 *  Copyright (C) 2002 Gargoyle Software. All rights reserved.
 *
 *  This file is part of HtmlUnit. For details on use and redistribution
 *  please refer to the license.html file included with these sources.
 */
package com.gargoylesoftware.htmlunit.html;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
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
import java.util.StringTokenizer;
import java.util.TreeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.tidy.Tidy;

/**
 *  A representation of an html page returned from a server. This is also the
 *  wrapper for the html element "html"
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Alex Nikiforoff
 */
public final class HtmlPage
         extends HtmlElement
         implements Page {

    private final WebClient webClient_;
    private final URL originatingUrl_;
    private final Map elements_ = new HashMap( 89 );
    private final WebResponse webResponse_;

    private WebWindow enclosingWindow_;

    private static int FunctionWrapperCount_ = 0;

    private static final Map HTML_ELEMENT_CREATORS = new TreeMap();

    private static final int TAB_INDEX_NOT_SPECIFIED = -10;
    private static final int TAB_INDEX_OUT_OF_BOUNDS = -20;


    /**
     *  Create an instance
     *
     * @param  webClient The WebClient that was used to load this page
     * @param  webResponse The web response that was used to create this page
     * @param  originatingUrl The url that was used to load this page.
     * @param  webWindow The window that this page is being loaded into.
     * @exception  IOException If an IO error occurs
     */
    public HtmlPage(
            final WebClient webClient,
            final URL originatingUrl,
            final WebResponse webResponse,
            final WebWindow webWindow )
        throws
            IOException {

        super( null, createDocument( webResponse ).getDocumentElement() );

        assertNotNull( "webClient", webClient );
        assertNotNull( "originatingUrl", originatingUrl );
        assertNotNull( "webResponse", webResponse );
        assertNotNull( "webWindow", webWindow );

        webClient_ = webClient;
        originatingUrl_ = originatingUrl;
        webResponse_ = webResponse;
        setEnclosingWindow(webWindow);
        webWindow.setEnclosedPage(this);

        initializeHtmlElementCreatorsIfNeeded();
        executeScriptTagsIfNeeded();
        initializeFramesIfNeeded();
        executeBodyOnLoadHandlerIfNeeded();
    }


    private static void initializeHtmlElementCreatorsIfNeeded() {
        synchronized( HTML_ELEMENT_CREATORS ) {
            if( HTML_ELEMENT_CREATORS.isEmpty() ) {
                HTML_ELEMENT_CREATORS.put( "input", new HtmlInputElementCreator() );
                HTML_ELEMENT_CREATORS.put( "tr", new TableElementCreator() );
                HTML_ELEMENT_CREATORS.put( "td", new TableElementCreator() );
                HTML_ELEMENT_CREATORS.put( "th", new TableElementCreator() );

                final String[][] simpleHtmlElementCreatorData = {
                    {"a", "HtmlAnchor"},
                    {"applet", "HtmlApplet"},
                    {"address", "HtmlAddress"},
                    {"area", "HtmlArea"},
                    {"base", "HtmlBase"},
                    {"basefont", "HtmlBaseFont"},
                    {"bdo", "HtmlBidirectionalOverride"},
                    {"blockquote", "HtmlBlockQuote"},
                    {"body", "HtmlBody"},
                    {"br", "HtmlBreak"},
                    {"button", "HtmlButton"},
                    {"caption", "HtmlCaption"},
                    {"center", "HtmlCenter"},
                    {"col", "HtmlTableColumn"},
                    {"colgroup", "HtmlTableColumnGroup"},
                    {"dd", "HtmlDefinitionDescription"},
                    {"del", "HtmlDeletedText"},
                    {"dir", "HtmlTextDirection"},
                    {"div", "HtmlDivision"},
                    {"dl", "HtmlDefinitionList"},
                    {"dt", "HtmlDefinitionTerm"},
                    {"fieldset", "HtmlFieldSet"},
                    {"font", "HtmlFont"},
                    {"form", "HtmlForm"},
                    {"frame", "HtmlFrame"},
                    {"frameset", "HtmlFrameSet"},
                    {"h1", "HtmlHeader1"},
                    {"h2", "HtmlHeader2"},
                    {"h3", "HtmlHeader3"},
                    {"h4", "HtmlHeader4"},
                    {"h5", "HtmlHeader5"},
                    {"h6", "HtmlHeader6"},
                    {"head", "HtmlHead"},
                    {"hr", "HtmlHorizontalRule"},
                    {"iframe", "HtmlInlineFrame"},
                    {"img", "HtmlImage"},
                    {"ins", "HtmlInsertedText"},
                    {"isindex", "HtmlIsIndex"},
                    {"label", "HtmlLabel"},
                    {"legend", "HtmlLegend"},
                    {"li", "HtmlListItem"},
                    {"link", "HtmlLink"},
                    {"map", "HtmlMap"},
                    {"menu", "HtmlMenu"},
                    {"meta", "HtmlMeta"},
                    {"noframes", "HtmlNoFrames"},
                    {"noscript", "HtmlNoScript"},
                    {"object", "HtmlObject"},
                    {"ol", "HtmlOrderedList"},
                    {"optgroup", "HtmlOptionGroup"},
                    {"option", "HtmlOption"},
                    {"p", "HtmlParagraph"},
                    {"param", "HtmlParameter"},
                    {"pre", "HtmlPreformattedText"},
                    {"q", "HtmlInlineQuotation"},
                    {"script", "HtmlScript"},
                    {"select", "HtmlSelect"},
                    {"span", "HtmlSpan"},
                    {"style", "HtmlStyle"},
                    {"title", "HtmlTitle"},
                    {"table", "HtmlTable"},
                    {"tbody", "HtmlTableBody"},
                    {"textarea", "HtmlTextArea"},
                    {"tfoot", "HtmlTableFooter"},
                    {"thead", "HtmlTableHeader"},
                    {"ul", "HtmlUnorderedList"},
                };

                for( int i=0; i<simpleHtmlElementCreatorData.length; i++ ) {
                    final String tagName = simpleHtmlElementCreatorData[i][0];
                    final Class clazz;
                    try {
                        clazz = Class.forName("com.gargoylesoftware.htmlunit.html."
                            + simpleHtmlElementCreatorData[i][1] );
                    }
                    catch( final ClassNotFoundException e ) {
                        throw new NoClassDefFoundError(simpleHtmlElementCreatorData[i][1]);
                    }
                    HTML_ELEMENT_CREATORS.put( tagName,
                        new SimpleHtmlElementCreator(clazz) );
                }
            }
        }
    }


    private static Document createDocument( final WebResponse webResponse )
        throws IOException {

        final InputStream inputStream = webResponse.getContentAsStream();

        final Tidy tidy = new Tidy();
        tidy.setErrout( createPrintWriterForTidy() );
        final OutputStream outputStream = null;

        final Document document = tidy.parseDOM( inputStream, outputStream );
        return document;
    }


    /**
     *  Return the HtmlAnchor with the specified name
     *
     * @param  name The name to search by
     * @return  See above
     */
    public HtmlAnchor getAnchorByName( final String name ) {
        return ( HtmlAnchor )getPage().getOneHtmlElementByAttribute( "a", "name", name );
    }


    /**
    *  Return the {@link HtmlAnchor} with the specified href
     *
     * @param  href The string to search by
     * @return  The HtmlAnchor
     */
    public HtmlAnchor getAnchorByHref( final String href ) {
        return ( HtmlAnchor )getPage().getOneHtmlElementByAttribute( "a", "href", href );
    }


    /**
     * Return a list of all anchors contained in this page.
     * @return All the anchors in this page.
     */
    public List getAnchors() {
        return getHtmlElementsByTagNames( Collections.singletonList("a") );
    }


    /**
     * Return the first anchor that contains the specified text.
     * @param text The text to search for
     * @return The first anchor that was found.
     * @throws ElementNotFoundException If no anchors are found with the specified text
     */
    public HtmlAnchor getFirstAnchorByText( final String text ) throws ElementNotFoundException {
        assertNotNull("text", text);

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
     *  Given an xml element, return the HtmlElement object that corresponds to
     *  that element or an instance of UnknownHtmlElement if one cannot be
     *  found. <p />
     *
     *  If a null xmlElement is passed in then null will be returned.  This
     *  deviates somewhat from the normal practice of throwing exceptions
     *  on null parameters but allowing this one case of null makes a
     *  significant amount of code elsewhere much simpler.
     *
     * @param  xmlElement The xml element to match
     * @return  See above
     */
    public HtmlElement getHtmlElement( final Element xmlElement ) {
        if( xmlElement == null ) {
            return null;
        }

        final HtmlElement htmlElement = ( HtmlElement )elements_.get( xmlElement );
        if( htmlElement != null ) {
            return htmlElement;
        }

        final String tagName = xmlElement.getTagName();
        final HtmlElement newHtmlElement;
        final HtmlElementCreator creator
            = ( HtmlElementCreator )HTML_ELEMENT_CREATORS.get( tagName );
        if( creator == null ) {
            newHtmlElement = new UnknownHtmlElement( this, xmlElement );
        }
        else {
            newHtmlElement = creator.create( this, xmlElement );
        }
        elements_.put( xmlElement, newHtmlElement );
        return newHtmlElement;
    }


    /**
     * Return the first form that matches the specifed name
     * @param name The name to search for
     * @return The first form.
     * @exception ElementNotFoundException If no forms match the specifed result.
     */
    public HtmlForm getFormByName( final String name ) throws ElementNotFoundException {
        final List forms = getHtmlElementsByAttribute( "form", "name", name );
        if( forms.size() == 0 ) {
            throw new ElementNotFoundException( "form", "name", name );
        }
        else {
            return ( HtmlForm )forms.get( 0 );
        }
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

        // Was a protocol specified?
        if( relativeUrl.indexOf( ":" ) != -1 ) {
            return new URL( relativeUrl );
        }

        if( relativeUrl.startsWith("//") ) {
            return new URL(originatingUrl_.getProtocol()+":"+relativeUrl);
        }

        final List tokens = new ArrayList();
        final String stringToTokenize;
        if( relativeUrl.length() == 0 ) {
            stringToTokenize = originatingUrl_.getPath();
        }
        else if( relativeUrl.startsWith("/") ) {
            stringToTokenize = relativeUrl;
        }
        else {
            String path = originatingUrl_.getPath();
            if( path.endsWith("/") == false ) {
                path += "/..";
            }
            stringToTokenize = path+"/"+relativeUrl;
        }
        final StringTokenizer tokenizer = new StringTokenizer(stringToTokenize,"/");
        while( tokenizer.hasMoreTokens() ) {
            tokens.add( tokenizer.nextToken() );
        }

        for( int i=0; i<tokens.size(); i++ ) {
            String oneToken = (String)tokens.get(i);
            if( oneToken.length() == 0 || oneToken.equals(".") ) {
                tokens.remove(i--);
            }
            else if( oneToken.equals("..") ) {
                tokens.remove(i--);
                if( i >= 0 ) {
                    tokens.remove(i--);
                }
            }
        }

        final StringBuffer buffer = new StringBuffer();
        buffer.append( originatingUrl_.getProtocol() );
        buffer.append( "://" );
        buffer.append( originatingUrl_.getHost() );
        final int port = originatingUrl_.getPort();
        if( port != -1 ) {
            buffer.append( ":" );
            buffer.append( port );
        }

        final Iterator iterator = tokens.iterator();
        while( iterator.hasNext() ) {
            buffer.append("/");
            buffer.append(iterator.next());
        }

        if( tokens.isEmpty() ) {
            buffer.append("/");
        }
        return new URL( buffer.toString() );
    }


    /**
     *  Return the web response that was originally used to create this page.
     *
     * @return  The web response
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }


    private static PrintWriter createPrintWriterForTidy() {
        return new PrintWriter(
            new Writer() {
                private StringBuffer buffer = new StringBuffer();


                public void write( char[] cbuf, int off, int len ) {
                    for( int i = off; i < off + len; i++ ) {
                        write( cbuf[i] );
                    }
                    if( true ) {
                        return;
                    }
                    final String string = new String( cbuf, off, len );
                    if( string.trim().length() == 0 ) {
                        return;
                    }

                    if( string.endsWith( "\n" ) || string.endsWith( "\r" ) ) {
                        buffer.append( string.trim() );
                        maybePrint( buffer.toString() );
                        buffer = new StringBuffer();
                    }
                    buffer.append( string );
                }


                private void write( final char aChar ) {
                    if( aChar == '\n' ) {
                        maybePrint( buffer.toString() );
                        buffer = new StringBuffer();
                    }
                    else if( aChar == '\r' ) {
                        // Ignore this
                    }
                    else {
                        buffer.append( aChar );
                    }
                }


                public void flush() {
                }


                public void close() {
                }
            } );
    }


    private static void maybePrint( final String string ) {
        if( string.length() == 0
                 || string.equals( "Tidy (vers 4th August 2000) Parsing \"InputStream\"" )
                 || string.equals( "no warnings or errors were found" )
                 || string.startsWith( "InputStream: Document content looks like HTML" ) ) {
            return;
        }
    }


    /**
     *  Return the value of the attribute "lang". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "lang" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getLangAttribute() {
        return getAttributeValue( "lang" );
    }


    /**
     *  Return the value of the attribute "xml:lang". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "xml:lang" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getXmlLangAttribute() {
        return getAttributeValue( "xml:lang" );
    }


    /**
     *  Return the value of the attribute "dir". Refer to the <a
     *  href='http://www.w3.org/TR/html401/'>HTML 4.01</a> documentation for
     *  details on the use of this attribute.
     *
     * @return  The value of the attribute "dir" or an empty string if that
     *      attribute isn't defined.
     */
    public final String getTextDirectionAttribute() {
        return getAttributeValue( "dir" );
    }


    /**
     *  Return a list of ids (strings) that correspond to the tabbable elements
     *  in this page. Return them in the same order specified in {@link
     *  #getTabbableElements}
     *
     * @return  The list of id's
     */
    public final List getTabbableElementIds() {
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
        final HtmlPage page = getPage();

        final Iterator xmlIterator = getXmlChildElements();
        while( xmlIterator.hasNext() ) {
            final Element xmlElement = ( Element )xmlIterator.next();
            final String tagName = xmlElement.getTagName();
            if( acceptableTagNames.contains( tagName ) ) {
                final String disabledAttribute = xmlElement.getAttribute( "disabled" );
                final boolean isDisabled
                         = disabledAttribute != null && disabledAttribute.length() != 0;

                final HtmlElement htmlElement = page.getHtmlElement( xmlElement );
                if( isDisabled == false && getTabIndex( htmlElement ) != TAB_INDEX_OUT_OF_BOUNDS ) {
                    tabbableElements.add( htmlElement );
                }
            }
        }

        Collections.sort( tabbableElements, createTabOrderComparator() );
        return Collections.unmodifiableList( tabbableElements );
    }


    private Comparator createTabOrderComparator() {
        return
            new Comparator() {
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
        final HtmlPage page = getPage();

        final Iterator xmlIterator = getXmlChildElements();
        while( xmlIterator.hasNext() ) {
            final Element xmlElement = ( Element )xmlIterator.next();
            if( acceptableTagNames.contains( xmlElement.getTagName() ) ) {
                final String accessKeyAttribute = xmlElement.getAttribute( "accesskey" );
                if( searchString.equals( accessKeyAttribute ) ) {
                    elements.add( getPage().getHtmlElement( xmlElement ) );
                }
            }
        }

        return elements;
    }


    /**
     *  Many html components can have an "accesskey" attribute which defines a
     *  hot key for keyboard navigation. Assert that all access keys (mnemonics)
     *  in this page are unique. If they aren't then throw an exception as per
     *  {@link WebClient#assertionFailed(String)}
     */
    public void assertAllAccessKeyAttributesUnique() {
        final List accessKeyList = new ArrayList();

        final Iterator xmlIterator = getXmlChildElements();
        while( xmlIterator.hasNext() ) {
            final String id = ( ( Element )xmlIterator.next() ).getAttribute( "accesskey" );
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

        final Iterator xmlIterator = getXmlChildElements();
        while( xmlIterator.hasNext() ) {
            final String id = ( ( Element )xmlIterator.next() ).getAttribute( "id" );
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
        final List tabbableElements = getHtmlElementsByTagNames( acceptableTagNames );

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
     * Execute the specified javascript if a javascript engine was successfully
     * instantiated.  If this javascript causes the current page to be reloaded
     * (through location="" or form.submit()) then return the new page.  Otherwise
     * return the current page.
     *
     * @param sourceCode The javascript code to execute.
     * @param sourceName The name for this chunk of code.  This name will be displayed
     * in any error messages.
     * @return A page
     * @deprecated use {@link #executeJavaScriptIfPossible(String, String, boolean)} instead.
     * This method will be removed before 1.1 goes final!
     */
    public Page executeJavascriptIfPossible( String sourceCode, final String sourceName ) {
        final ScriptResult scriptResult
            = executeJavaScriptIfPossible(sourceCode, sourceName, false);
        return scriptResult.getNewPage();
    }


    /**
     * Execute the specified javascript if a javascript engine was successfully
     * instantiated.  If this javascript causes the current page to be reloaded
     * (through location="" or form.submit()) then return the new page.  Otherwise
     * return the current page.
     *
     * @param sourceCode The javascript code to execute.
     * @param sourceName The name for this chunk of code.  This name will be displayed
     * in any error messages.
     * @param wrapSourceInFunction True if this snippet of code should be placed inside
     * a javascript function.  This is neccessary for intrinsic event handlers that may
     * try to return a value.
     * @return A page
     */
    public ScriptResult executeJavaScriptIfPossible(
        String sourceCode, final String sourceName, final boolean wrapSourceInFunction ) {

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
        final Object result;
        if( wrapSourceInFunction == true ) {
            // Something that isn't likely to collide with the name of a function in the
            // loaded javascript
            final String wrapperName = "GargoyleWrapper"+(FunctionWrapperCount_++);
            sourceCode = "function "+wrapperName+"() {"+sourceCode+"\n}";
            engine.execute( this, sourceCode, "Wrapper definition for "+sourceName );
            result = engine.execute( this, wrapperName+"()", sourceName );
        }
        else {
            result = engine.execute( this, sourceCode, sourceName );
        }

        return new ScriptResult( result, window.getEnclosedPage() );
    }


    /**
     *  Walk through all the elements looking for script tags. Execute any
     *  script tags that contain javascript.
     */
    private void executeScriptTagsIfNeeded() {
        final ScriptEngine engine = getWebClient().getScriptEngine();
        if( engine == null ) {
            return;
        }

        executeJavaScriptIfPossible("", "Dummy stub just to get javascript initialized", false);

        final Iterator iterator = getXmlChildElements();
        while( iterator.hasNext() ) {
            final Element element = ( Element )iterator.next();
            if( element.getTagName().equals( "script" ) ) {
                final HtmlScript htmlScript = ( HtmlScript )getHtmlElement( element );
                final String languageAttribute = htmlScript.getLanguageAttribute();
                final String typeAttribute = htmlScript.getTypeAttribute();

                // Unless otherwise specified, we have to assume that any script is javascript
                boolean isJavaScript = true;
                if( languageAttribute.length() != 0  ) {
                    isJavaScript = languageAttribute.toLowerCase().startsWith("javascript");
                }
                else if(  typeAttribute.length() != 0 ) {
                    isJavaScript = typeAttribute.equals( "text/javascript" );
                }

                if( isJavaScript ) {
                    final String sourceUrl = htmlScript.getSrcAttribute();
                    if( sourceUrl.length() == 0 ) {
                        engine.execute( this, htmlScript.asText(), "Embedded script" );
                    }
                    else {
                        engine.execute( this, loadJavaScriptFromUrl( sourceUrl ), sourceUrl );
                    }
                }
            }
        }
    }


    private String loadJavaScriptFromUrl( final String urlString ) {
        URL url = null;
        try {
            url = getFullyQualifiedUrl(urlString);
            final Page page = getWebClient().getPage( url );
            final WebResponse webResponse = page.getWebResponse();
            if( webResponse.getStatusCode() == 200 ) {
                return webResponse.getContentAsString();
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
        catch( final FailingHttpStatusCodeException e ) {
            getLog().error("Error loading javascript from ["+url.toExternalForm()
                +"] status=["+e.getStatusCode()+" "+e.getStatusMessage()+"]");
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
     * @deprecated Use {@link #getTitleText()} instead.  This method will be removed
     * prior to the final release of 1.1
     */
    public String getTitle() {
        return getTitleText();
    }


    /**
     * Return the title of this page or an empty string if the title wasn't specified.
     *
     * @return the title of this page or an empty string if the title wasn't specified.
     */
    public String getTitleText() {
        final Iterator topIterator = getChildElements().iterator();
        while( topIterator.hasNext() ) {
            final HtmlElement topElement = (HtmlElement)topIterator.next();
            if( topElement instanceof HtmlHead ) {
                final Iterator headIterator = topElement.getChildElements().iterator();
                while( headIterator.hasNext() ) {
                    final HtmlElement headElement = (HtmlElement)headIterator.next();
                    if( headElement instanceof HtmlTitle ) {
                        return headElement.asText();
                    }
                }
            }
        }
        return "";
    }


    private void executeBodyOnLoadHandlerIfNeeded() {
        final List bodyTags = getHtmlElementsByTagNames( Collections.singletonList("body") );
        if( bodyTags.size() != 1 ) {
            throw new IllegalStateException(
                "Expected exactly one body tag but found ["+bodyTags.size()+"]");
        }

        final HtmlBody body = (HtmlBody)bodyTags.get(0);
        final String onLoad = body.getOnLoadAttribute();
        if( onLoad.length() != 0 ) {
            executeJavascriptIfPossible(onLoad, "body.onLoad");
        }
    }


    private void initializeFramesIfNeeded() {
        // The act of creating the html element will cause initialization to start
        getHtmlElementsByTagNames( Arrays.asList( new String[]{"frame", "iframe"}) );
    }


    /**
     * Return a list containing all the frames and iframes in this page.
     * @return a list of all frames and iframes
     */
    public List getFrames() {
        return getHtmlElementsByTagNames( Arrays.asList( new String[]{
            "frame", "iframe" } ) );
    }
}

