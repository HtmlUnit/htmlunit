/*
 * Copyright (c) 2002, 2003 Gargoyle Software Inc. All rights reserved.
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
import java.io.InputStream;
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

import org.apache.commons.httpclient.HttpConstants;
import org.apache.xerces.xni.parser.XMLDocumentFilter;
import org.cyberneko.html.HTMLConfiguration;
import org.cyberneko.html.parsers.DOMParser;
import org.mozilla.javascript.Function;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.Assert;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.ObjectInstantiationException;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptEngine;
import com.gargoylesoftware.htmlunit.ScriptFilter;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SubmitMethod;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 *  A representation of an html page returned from a server. This is also the
 *  wrapper for the html element "html"
 *
 * @version  $Revision$
 * @author  <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Alex Nikiforoff
 * @author Noboru Sinohara
 * @author David K. Taylor
 * @author Andreas Hangler
 */
public final class HtmlPage
         extends HtmlElement
         implements Page {

    /**
     * Custom subclass used to provide access to some protected variables.
     */
    class MyParser extends DOMParser {
        /**
         * Return the configuration used by this parser.
         * @return The configuration.
         */
        public HTMLConfiguration getConfiguration() {
            return (HTMLConfiguration)fConfiguration;
        }
        /**
         * Return the document used by this parser.
         * @return The document.
         */
        public Document getDocument() {
            return fDocument;
        }
    }
    /** The parser that is used to create the initial dom tree */
    private MyParser xmlParser_;

    private       Document document_;
    private final WebClient webClient_;
    private final URL originatingUrl_;
    private       String originalCharset_ = null;
    private final Map nodes_ = new HashMap( 89 );
    private final WebResponse webResponse_;

    private WebWindow enclosingWindow_;

    private static int FunctionWrapperCount_ = 0;

    private static final Map HTML_ELEMENT_CREATORS = new HashMap();

    private static final int TAB_INDEX_NOT_SPECIFIED = -10;
    private static final int TAB_INDEX_OUT_OF_BOUNDS = -20;
    private ScriptFilter scriptFilter_;

    private Object onLoad_;

    /**
     *  Create an instance
     *
     * @param  webClient The WebClient that was used to load this page
     * @param  webResponse The web response that was used to create this page
     * @param  originatingUrl The url that was used to load this page.
     * @param  webWindow The window that this page is being loaded into.
     */
    public HtmlPage(
            final WebClient webClient,
            final URL originatingUrl,
            final WebResponse webResponse,
            final WebWindow webWindow ) {

        super( null, null );

        Assert.notNull( "webClient", webClient );
        Assert.notNull( "originatingUrl", originatingUrl );
        Assert.notNull( "webResponse", webResponse );
        Assert.notNull( "webWindow", webWindow );

        webClient_ = webClient;
        originatingUrl_ = originatingUrl;
        webResponse_ = webResponse;
        setEnclosingWindow(webWindow);
        webWindow.setEnclosedPage(this);
    }


    /**
     * Initialize this page.
     * @throws IOException If an IO problem occurs.
     */
    public void initialize() throws IOException {
        initializeHtmlElementCreatorsIfNeeded();
        document_ = createDocument( webResponse_ );
        setElement(document_.getDocumentElement());

        executeJavaScriptIfPossible("", "Dummy stub just to get javascript initialized", false, null);

        initializeFramesIfNeeded();
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


    private static void initializeHtmlElementCreatorsIfNeeded() {
        synchronized( HTML_ELEMENT_CREATORS ) {
            if( HTML_ELEMENT_CREATORS.isEmpty() ) {
                HTML_ELEMENT_CREATORS.put( new Integer( Node.TEXT_NODE ),
                    new SimpleHtmlElementCreator( DomText.class ) );
                HTML_ELEMENT_CREATORS.put( "input",
                    new HtmlInputElementCreator() );
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


    private Document createDocument( final WebResponse webResponse )
        throws IOException {

        final InputStream inputStream = webResponse.getContentAsStream();

        xmlParser_ = new MyParser();

        try {
            XMLDocumentFilter[] filters = {
                new ScriptFilter( xmlParser_.getConfiguration(), this )
            };
            xmlParser_.setProperty( "http://cyberneko.org/html/properties/filters", filters );

            xmlParser_.setFeature( "http://cyberneko.org/html/features/augmentations", true );
            xmlParser_.setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
            xmlParser_.setFeature("http://cyberneko.org/html/features/report-errors", true);

            xmlParser_.parse( new InputSource(inputStream) );
            final Document document = xmlParser_.getDocument();
            xmlParser_ = null;
            return document;
        }
        catch( final SAXException e ) {
            throw new ObjectInstantiationException("Unable to parse html input", e);
        }
    }

    /**
     * Return the charset used in the page.
     * The sources of this information are from 1).meta element which
     * http-equiv attribute value is 'content-type', or if not from
     * the response header.
     * @return the value of charset.
     */
    public String getPageEncoding(){
        if( originalCharset_ != null ) {
            return originalCharset_ ;
        }
        ArrayList ar = new ArrayList();
        ar.add("meta");
        List list = getHtmlElementsByTagNames(ar);
        for(int i=0; i<list.size();i++ ){
            HtmlMeta meta = (HtmlMeta) list.get(i);
            String httpequiv = meta.getHttpEquivAttribute();
            if( "content-type".equals(httpequiv.toLowerCase()) ){
                String contents = meta.getContentAttribute();
                int pos = contents.toLowerCase().indexOf("charset=");
                if( pos>=0 ){
                    originalCharset_ = contents.substring(pos+8);
                    getLog().debug("Page Encoding detected:" + originalCharset_);
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
     * Return the xml element corresponding with this page.  This has been
     * overridden to ensure it returns a correct value even if the page
     * hasn't fully been loaded yet.
     *
     * @return the xml element.
     */
    public Element getElement() {
        final Element element = super.getElement();
        if( element == null && xmlParser_ != null ) {
            return xmlParser_.getDocument().getDocumentElement();
        }
        return element;
    }



    /**
     * Return the xml document element corresponding with this page.  This
     * returns a correct value even if the page hasn't fully been loaded yet.
     *
     * @return the xml document element.
     */
    public Document getDocument() {
        if( document_ == null && xmlParser_ != null ) {
            document_ = xmlParser_.getDocument();
        }
        return document_;
    }



    /**
     * Create a new HTML element with the given tag name.  This may be
     * called if the page hasn't fully been loaded yet.
     *
     * @param tagName The tag name
     * @return the new HTML element.
     */
    public HtmlElement createElement( String tagName ) {
        Document document = getDocument();
        final Element xmlElement = document.createElement( tagName );
        final HtmlElement element = getHtmlElement( xmlElement );
        return element;
    }



    /**
     * Create a new text node with the given text data.  This may be
     * called if the page hasn't fully been loaded yet.
     *
     * @param data String text data.
     * @return the new text node.
     */
    public DomText createTextNode( String data ) {
        Document document = getDocument();
        final Text xmlText = document.createTextNode( data );
        final DomText textNode = (DomText) getDomNode( xmlText );
        return textNode;
    }


    /**
     *  Return the HtmlAnchor with the specified name
     *
     * @param  name The name to search by
     * @return  See above
     * @throws ElementNotFoundException If the anchor could not be found.
     */
    public HtmlAnchor getAnchorByName( final String name ) throws ElementNotFoundException {
        return ( HtmlAnchor )getPage().getOneHtmlElementByAttribute( "a", "name", name );
    }


    /**
    *  Return the {@link HtmlAnchor} with the specified href
     *
     * @param  href The string to search by
     * @return  The HtmlAnchor
     * @throws ElementNotFoundException If the anchor could not be found.
     */
    public HtmlAnchor getAnchorByHref( final String href ) throws ElementNotFoundException {
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
     *  Given an XML node, return the HtmlElement object that corresponds to
     *  that node or an instance of UnknownHtmlElement if one cannot be
     *  found. <p />
     *
     *  If a null xmlNode is passed in then null will be returned.  This
     *  deviates somewhat from the normal practice of throwing exceptions
     *  on null parameters but allowing this one case of null makes a
     *  significant amount of code elsewhere much simpler.
     *
     * @param  xmlNode The XML node to match
     * @return  See above
     */
    public HtmlElement getHtmlElement( final Node xmlNode ) {
        return (HtmlElement) getDomNode( xmlNode );
    }


    /**
     *  Given an XML node, return the DomNode object that corresponds to
     *  that node or an instance of UnknownHtmlElement or UnknownDomNode if
     *  one cannot be found. <p />
     *
     *  If a null xmlNode is passed in then null will be returned.  This
     *  deviates somewhat from the normal practice of throwing exceptions
     *  on null parameters but allowing this one case of null makes a
     *  significant amount of code elsewhere much simpler.
     *
     * @param  xmlNode The XML node to match
     * @return  See above
     */
    public DomNode getDomNode( final Node xmlNode ) {
        if( xmlNode == null ) {
            return null;
        }

        final DomNode domNode = ( DomNode )nodes_.get( xmlNode );
        if( domNode != null ) {
            return domNode;
        }

        final HtmlElementCreator creator;
        final DomNode newDomNode;
        if( xmlNode.getNodeType() == Node.ELEMENT_NODE ) {
            final String tagName = getTagName((Element) xmlNode);
            creator = ( HtmlElementCreator )
               HTML_ELEMENT_CREATORS.get( tagName );
        }
        else {
            creator = ( HtmlElementCreator )
               HTML_ELEMENT_CREATORS.get( new Integer( xmlNode.getNodeType() ) );
        }
        if( creator == null ) {
            if( xmlNode.getNodeType() == Node.ELEMENT_NODE ) {
                newDomNode = new UnknownHtmlElement( this, (Element) xmlNode );
            }
            else {
                newDomNode = new UnknownDomNode( this, xmlNode );
            }
        }
        else {
            newDomNode = creator.create( this, xmlNode );
        }
        nodes_.put( xmlNode, newDomNode );
        return newDomNode;
    }



    /**
     *  Given an XML node, remove that node and return the DomNode
     *  object that corresponds to that node or null if one cannot be
     *  found. <p />
     *
     *  If a null xmlNode is passed in then null will be returned.
     *
     * @param  xmlNode The XML node to remove
     * @return  See above
     */
    public DomNode removeDomNode( final Node xmlNode ) {
        if( xmlNode == null ) {
            return null;
        }

        return ( DomNode )nodes_.remove( xmlNode );
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
     * Return a list of all the forms in the page.
     * @return All the forms.
     */
    public List getAllForms() {
        return getHtmlElementsByTagNames( Arrays.asList(new String[]{"form"}) );
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

        final List baseElements = getHtmlElementsByTagNames( Collections.singletonList("base"));
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
        return webClient_.expandUrl( baseUrl, relativeUrl );
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
        getHtmlElementsByTagNames( Collections.singletonList("base"));
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
            final String tagName = getTagName(xmlElement);
            if( acceptableTagNames.contains( tagName ) ) {
                final String disabledAttribute = getAttributeValue(xmlElement, "disabled");
                final boolean isDisabled = (disabledAttribute != ATTRIBUTE_NOT_DEFINED);

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

        final Iterator xmlIterator = getXmlChildElements();
        while( xmlIterator.hasNext() ) {
            final Element xmlElement = ( Element )xmlIterator.next();
            if( acceptableTagNames.contains( getTagName(xmlElement) ) ) {
                final String accessKeyAttribute = getAttributeValue(xmlElement, "accesskey");
                if( searchString.equalsIgnoreCase( accessKeyAttribute ) ) {
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
            final String id = getAttributeValue( ( Element )xmlIterator.next(), "accesskey" );
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
            final String id = getAttributeValue( ( Element )xmlIterator.next(), "id" );
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
        if( wrapSourceInFunction == true ) {
            // Something that isn't likely to collide with the name of a function in the
            // loaded javascript
            final String wrapperName = "GargoyleWrapper"+(FunctionWrapperCount_++);
            sourceCode = "function "+wrapperName+"() {"+sourceCode+"\n}";
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
     * Internal use only.  This is a callback from {@link ScriptFilter} and
     * should not be called by consumers of HtmlUnit. this method assume the
     * charset is null.
     * @param srcAttribute The source attribute from the script tag.
     */
    public void loadExternalJavaScriptFile( final String srcAttribute  ) {
        loadExternalJavaScriptFile( srcAttribute, null );
    }

    /**
     * Internal use only.  This is a callback from {@link ScriptFilter} and
     * should not be called by consumers of HtmlUnit.
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
    public boolean isJavaScript( final String typeAttribute, final String languageAttribute ) {
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
            final WebResponse webResponse= getWebClient().loadWebResponse(
                url, SubmitMethod.GET, Collections.EMPTY_LIST);
            if( webResponse.getStatusCode() == 200 ) {
                final String contentType = webResponse.getContentType();
                final String contentCharset = webResponse.getContentCharSet();
                if( contentType.equals("text/javascript") == false
                        && contentType.equals("application/x-javascript") == false ) {
                    getLog().warn(
                        "Expected content type of text/javascript or application/x-javascript for remotely "
                        + "loaded javascript element but got [" + webResponse.getContentType()+"]");
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
                byte [] data = webResponse.getResponseBody();
                return HttpConstants.getContentString(data, 0, data.length, scriptEncoding );
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


    /**
     * Return the value of the onload attribute of the enclosed body or
     * frameset.
     *
     * @return the value of the onload attribute of the enclosed body or
     * frameset.
     */
    private String getBodyOnLoadAttribute() {
        final String onLoad;
        final List bodyTags = getHtmlElementsByTagNames( Collections.singletonList("body") );
        final int bodyTagCount = bodyTags.size();
        if( bodyTagCount == 0 ) {
            // Must be a frameset
            onLoad = "";
        }
        else if( bodyTagCount == 1 ) {
            final HtmlBody body = (HtmlBody)bodyTags.get(0);
            onLoad = body.getOnLoadAttribute();
        }
        else {
            throw new IllegalStateException(
                "Expected no more than one body tag but found ["+bodyTagCount+"] xml="+asXml());
        }
        return onLoad;
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Return the javascript string for onload.
     * @return The javascript string for onload.
     */
    public Object getOnLoadAttribute() {
        if( onLoad_ != null ) {
            return onLoad_;
        }
        else {
            return getBodyOnLoadAttribute();
        }
    }


    /**
     * Internal use only - subject to change without notice.<p>
     * Set the javascript string for onload.
     * @param newValue The new javascript string for onload.
     */
    public void setOnLoadAttribute(Object newValue) {
        onLoad_ = newValue;
    }


    /**
     * Look for and execute any appropriate onload handlers.  Look for body
     * and frame tags.
     */
    private void executeOnLoadHandlersIfNeeded() {
        executeOneOnLoadHandler( getOnLoadAttribute() );

        final List tagNames = Collections.singletonList("frame");
        final Iterator iterator = getHtmlElementsByTagNames( tagNames ).iterator();
        while( iterator.hasNext() ) {
            final HtmlFrame frame = (HtmlFrame)iterator.next();
            executeOneOnLoadHandler(frame.getOnLoadAttribute());
        }
    }

    /**
     * Execute a single onload handler.  This will either be a string which
     * will be executed as javascript, or a javascript Function.
     * @param onLoad The javascript to execute
     */
    private void executeOneOnLoadHandler( final Object onLoad ) {
        if ( onLoad instanceof String ) {
            String onLoadScript = (String) onLoad;
            if( onLoadScript.length() != 0 ) {
                ScriptResult scriptResult =
                    executeJavaScriptIfPossible(onLoadScript, "body.onLoad", false, null);

                // if script evaluates to a function then call it
                Object javaScriptResult = scriptResult.getJavaScriptResult();
                if (javaScriptResult != null && javaScriptResult instanceof Function) {
                    ScriptEngine engine = getWebClient().getScriptEngine();
                    engine.callFunction(this, javaScriptResult, null, new Object[] {}, null);
                }
            }
        }
        else {
            final ScriptEngine engine = getWebClient().getScriptEngine();
            engine.callFunction( this, onLoad, null, new Object [0], null );
        }
    }
    /**
     * If a refresh has been specified either through a meta tag or an http
     * response header, then perform that refresh.
     */
    private void executeRefreshIfNeeded() {
        // If this page is not in a frame then a refresh has already happened,
        // most likely through the javascript onload handler, so we don't do a
        // second refresh.
        final WebWindow window = getEnclosingWindow();
        if( window == null ) {
            return;
        }

        final String refreshString = getRefreshStringOrNull();
        if( refreshString == null ) {
            return;
        }

        final int index = refreshString.indexOf("URL=");
        if( index == -1 ) {
            if( refreshString.length() != 0 ) {
                getLog().error("Malformed refresh string ["+refreshString+"]");
            }
            return;
        }
        final String newUrl = refreshString.substring(index+4);
        try {
            final URL url = getFullyQualifiedUrl(newUrl);

            getWebClient().getPage(
                window, url, SubmitMethod.GET, Collections.EMPTY_LIST );
        }
        catch( final MalformedURLException e ) {
            getLog().error("HtmlPage refresh to ["+newUrl+"] Got MalformedURLException", e);
        }
        catch( final IOException e ) {
            getLog().error("HtmlPage refresh to ["+newUrl+"] Got IOException", e);
        }
    }

    /**
     * Return an auto-refresh string if specified.  This will look in both the meta
     * tags and inside the http response headers.
     */
    private String getRefreshStringOrNull() {
        final Iterator iterator
            = getHtmlElementsByTagNames( Collections.singletonList("meta") ).iterator();
        while( iterator.hasNext() ) {
            final HtmlMeta meta = (HtmlMeta)iterator.next();
            if( meta.getHttpEquivAttribute().equalsIgnoreCase("refresh") ) {
                return meta.getContentAttribute();
            }
        }

        return getWebResponse().getResponseHeaderValue("Refresh");
    }

    private void initializeFramesIfNeeded() {
        // The act of creating the html element will cause initialization to start
        getHtmlElementsByTagNames( Arrays.asList( new String[]{"frame", "iframe"}) );
    }

    /**
     * Deregister frames that are no longer in use.
     */
    public void deregisterFramesIfNeeded() {
        List list = getHtmlElementsByTagNames( Arrays.asList( new String[]{"frame", "iframe"}) );
        final int listSize = list.size();
        for(int i=0; i<listSize;i++ ){
            final WebWindow window = (WebWindow) list.get(i);
            webClient_.deregisterWebWindow( window );
            HtmlPage page = (HtmlPage) window.getEnclosedPage();
            page.deregisterFramesIfNeeded();
        }
    }


    /**
     * Return a list containing all the frames and iframes in this page.
     * @return a list of all frames and iframes
     */
    public List getFrames() {
        return getHtmlElementsByTagNames( Arrays.asList( new String[]{
            "frame", "iframe" } ) );
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
            webClient.moveFocusToElement(element);

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
                webClient.moveFocusToElement(null);
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
        if( elements.isEmpty() ) {
            getWebClient().moveFocusToElement(null);
            return null;
        }

        final HtmlElement elementWithFocus = getWebClient().getElementWithFocus();
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

        getWebClient().moveFocusToElement( elementToGiveFocus );
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
            getWebClient().moveFocusToElement(null);
            return null;
        }

        final HtmlElement elementWithFocus = getWebClient().getElementWithFocus();
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

        getWebClient().moveFocusToElement( elementToGiveFocus );
        return elementToGiveFocus;
    }


    /**
     * Set the script filter that will be used during processing of the
     * javascript document.write().  This is intended for internal use only.
     * @param scriptFilter The script filter.
     */
    public void setScriptFilter( final ScriptFilter scriptFilter ) {
        scriptFilter_ = scriptFilter;
    }

    /**
     * Return the script filter that will be used during processing of the
     * javascript document.write().  This is intended for internal use only.
     * @return The script filter.
     */
    public ScriptFilter getScriptFilter() {
        return scriptFilter_;
    }
}

