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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.cyberneko.html.HTMLConfiguration;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.ObjectInstantiationException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * SAX parser implementation that uses the neko {@link org.cyberneko.html.HTMLConfiguration}
 * to parse HTML into a HtmlUnit-specific DOM (HU-DOM) tree.
 * <p>
 * <em>Note that the parser currently does not handle CDATA or comment sections, i.e. these
 * do not appear in the resulting DOM tree</em>
 *
 * @version  $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David K. Taylor
 * @author Chris Erskine
 */
public class HTMLParser {

    private static final Map ELEMENT_FACTORIES = new HashMap();
    private static boolean ValidateHtml_ = false;
    private static boolean IgnoreOutsideContent_ = false;
    
    static {
        ELEMENT_FACTORIES.put("input", InputElementFactory.instance);

        putFactory( HtmlAnchor.TAG_NAME, HtmlAnchor.class);
        putFactory( HtmlApplet.TAG_NAME, HtmlApplet.class);
        putFactory( HtmlAddress.TAG_NAME, HtmlAddress.class);
        putFactory( HtmlArea.TAG_NAME, HtmlArea.class);
        putFactory( HtmlBase.TAG_NAME, HtmlBase.class);
        putFactory( HtmlBaseFont.TAG_NAME, HtmlBaseFont.class);
        putFactory( HtmlBidirectionalOverride.TAG_NAME, HtmlBidirectionalOverride.class);
        putFactory( HtmlBlockQuote.TAG_NAME, HtmlBlockQuote.class);
        putFactory( HtmlBody.TAG_NAME, HtmlBody.class);
        putFactory( HtmlBreak.TAG_NAME, HtmlBreak.class);
        putFactory( HtmlButton.TAG_NAME, HtmlButton.class);
        putFactory( HtmlCaption.TAG_NAME, HtmlCaption.class);
        putFactory( HtmlCenter.TAG_NAME, HtmlCenter.class);
        putFactory( HtmlTableColumn.TAG_NAME, HtmlTableColumn.class);
        putFactory( HtmlTableColumnGroup.TAG_NAME, HtmlTableColumnGroup.class);
        putFactory( HtmlDefinitionDescription.TAG_NAME, HtmlDefinitionDescription.class);
        putFactory( HtmlDeletedText.TAG_NAME, HtmlDeletedText.class);
        putFactory( HtmlTextDirection.TAG_NAME, HtmlTextDirection.class);
        putFactory( HtmlDivision.TAG_NAME, HtmlDivision.class);
        putFactory( HtmlDefinitionList.TAG_NAME, HtmlDefinitionList.class);
        putFactory( HtmlDefinitionTerm.TAG_NAME, HtmlDefinitionTerm.class);
        putFactory( HtmlFieldSet.TAG_NAME, HtmlFieldSet.class);
        putFactory( HtmlFont.TAG_NAME, HtmlFont.class);
        putFactory( HtmlForm.TAG_NAME, HtmlForm.class);
        putFactory( HtmlFrame.TAG_NAME, HtmlFrame.class);
        putFactory( HtmlFrameSet.TAG_NAME, HtmlFrameSet.class);
        putFactory( HtmlHeader1.TAG_NAME, HtmlHeader1.class);
        putFactory( HtmlHeader2.TAG_NAME, HtmlHeader2.class);
        putFactory( HtmlHeader3.TAG_NAME, HtmlHeader3.class);
        putFactory( HtmlHeader4.TAG_NAME, HtmlHeader4.class);
        putFactory( HtmlHeader5.TAG_NAME, HtmlHeader5.class);
        putFactory( HtmlHeader6.TAG_NAME, HtmlHeader6.class);
        putFactory( HtmlHead.TAG_NAME, HtmlHead.class);
        putFactory( HtmlHorizontalRule.TAG_NAME, HtmlHorizontalRule.class);
        putFactory( HtmlHtml.TAG_NAME, HtmlHtml.class);
        putFactory( HtmlInlineFrame.TAG_NAME, HtmlInlineFrame.class);
        putFactory( HtmlImage.TAG_NAME, HtmlImage.class);
        putFactory( HtmlInsertedText.TAG_NAME, HtmlInsertedText.class);
        putFactory( HtmlIsIndex.TAG_NAME, HtmlIsIndex.class);
        putFactory( HtmlLabel.TAG_NAME, HtmlLabel.class);
        putFactory( HtmlLegend.TAG_NAME, HtmlLegend.class);
        putFactory( HtmlListItem.TAG_NAME, HtmlListItem.class);
        putFactory( HtmlLink.TAG_NAME, HtmlLink.class);
        putFactory( HtmlMap.TAG_NAME, HtmlMap.class);
        putFactory( HtmlMenu.TAG_NAME, HtmlMenu.class);
        putFactory( HtmlMeta.TAG_NAME, HtmlMeta.class);
        putFactory( HtmlNoFrames.TAG_NAME, HtmlNoFrames.class);
        putFactory( HtmlNoScript.TAG_NAME, HtmlNoScript.class);
        putFactory( HtmlObject.TAG_NAME, HtmlObject.class);
        putFactory( HtmlOrderedList.TAG_NAME, HtmlOrderedList.class);
        putFactory( HtmlOptionGroup.TAG_NAME, HtmlOptionGroup.class);
        putFactory( HtmlOption.TAG_NAME, HtmlOption.class);
        putFactory( HtmlParagraph.TAG_NAME, HtmlParagraph.class);
        putFactory( HtmlParameter.TAG_NAME, HtmlParameter.class);
        putFactory( HtmlPreformattedText.TAG_NAME, HtmlPreformattedText.class);
        putFactory( HtmlInlineQuotation.TAG_NAME, HtmlInlineQuotation.class);
        putFactory( HtmlScript.TAG_NAME, HtmlScript.class);
        putFactory( HtmlSelect.TAG_NAME, HtmlSelect.class);
        putFactory( HtmlSpan.TAG_NAME, HtmlSpan.class);
        putFactory( HtmlStyle.TAG_NAME, HtmlStyle.class);
        putFactory( HtmlTitle.TAG_NAME, HtmlTitle.class);

        putFactory( HtmlTable.TAG_NAME, HtmlTable.class);
        putFactory( HtmlTableBody.TAG_NAME, HtmlTableBody.class);
        putFactory( HtmlTableDataCell.TAG_NAME, HtmlTableDataCell.class);
        putFactory( HtmlTableHeaderCell.TAG_NAME, HtmlTableHeaderCell.class);
        putFactory( HtmlTableRow.TAG_NAME, HtmlTableRow.class);

        putFactory( HtmlTextArea.TAG_NAME, HtmlTextArea.class);
        putFactory( HtmlTableFooter.TAG_NAME, HtmlTableFooter.class);
        putFactory( HtmlTableHeader.TAG_NAME, HtmlTableHeader.class);
        putFactory( HtmlUnorderedList.TAG_NAME, HtmlUnorderedList.class);
    }

    private static void putFactory(final String tagName, final Class elementClass) {
        ELEMENT_FACTORIES.put(tagName, new DefaultElementFactory(elementClass));
    }

    /**
     * Set the flag to control logging html errors to the standard error
     * @param validateFlag - boolean flag to set
     */
    public static void setValidateHtml(final boolean validateFlag) {
        ValidateHtml_ = validateFlag;
    }

    /**
     * Get the state of the logging flag for html errors
     * @return - The current state
     */
    public static boolean getValidateHtml() {
        return ValidateHtml_;
    }

    /**
     * Set the flag to control validation of the HTML content that is outside of the
     * BODY and HTML tags.  This flag is false by default to maintain compatability with 
     * current NekoHTML defaults.
     * @param ignoreOutsideContent - boolean flag to set
     */
    public static void setIgnoreOutsideContent(final boolean ignoreOutsideContent) {
        IgnoreOutsideContent_ = ignoreOutsideContent;
    }

    /**
     * Get the state of the flag to ignore contant outside the BODY and HTML tags
     * @return - The current state
     */
    public static boolean getIgnoreOutsideContent() {
        return IgnoreOutsideContent_;
    }

    /**
     * @param tagName an HTML element tag name
     * @return a factory for creating HtmlElements representing the given tag
     */
    public static IElementFactory getFactory(final String tagName) {
        final IElementFactory result = (IElementFactory)ELEMENT_FACTORIES.get(tagName);

        //return result != null ? result : UnknownElementFactory.instance;
        if(result != null) {
            return result;
        }
        else {
            return UnknownElementFactory.instance;
        }
    }

    /**
     * You should never need to create one of these!
     * @deprecated
     */    
    public HTMLParser() {
    }

    /**
     * This method should no longer be used
     *
     * @param webClient NOT USED
     * @param webResponse the response data
     * @param webWindow the web window into which the page is to be loaded
     * @return the page object which forms the root of the DOM tree, or <code>null</code> if the &lt;HTML&gt;
     * tag is missing
     * @throws java.io.IOException io error
     * @deprecated
     */
    public HtmlPage parse(
            final WebClient webClient, 
            final WebResponse webResponse, 
            final WebWindow webWindow)
            throws IOException {
        return parse(webResponse, webWindow);
    }
    /**
     * parse the HTML content from the given WebResponse into an object tree representation
     *
     * @param webResponse the response data
     * @param webWindow the web window into which the page is to be loaded
     * @return the page object which forms the root of the DOM tree, or <code>null</code> if the &lt;HTML&gt;
     * tag is missing
     * @throws java.io.IOException io error
     */
    public static HtmlPage parse(final WebResponse webResponse, final WebWindow webWindow)
            throws IOException {
        final HtmlUnitDOMBuilder domBuilder = new HtmlUnitDOMBuilder(webResponse, webWindow);
        String charSet = webResponse.getContentCharSet();
        if( isSupportedCharacterSet(charSet) == false ) {
            charSet = "ISO-8859-1";
        }
        final XMLInputSource in = new XMLInputSource(
                null,
                webResponse.getUrl().toString(),
                null,
                webResponse.getContentAsStream(),
                charSet);

        domBuilder.parse(in);
        return domBuilder.page_;
    }
    
    /**
     * <p>Return true if the specified charset is supported on this platform.</p>
     * @param charset The charset to check.
     * @return True if this charset is supported.
     */
    private static boolean isSupportedCharacterSet( final String charset ) {
        //TODO: There's got to be a cleaner way to figure out if a given encoding is
        // supported but I couldn't find it.
        try {
            new InputStreamReader( new ByteArrayInputStream(new byte[0]), charset );
            return true;
        }
        catch( final UnsupportedEncodingException e ) {
            return false;
        }
    }

    /**
     * The parser and DOM builder. This class subclasses Xerces's AbstractSAXParser and implements
     * the ContentHandler interface. Thus all parser APIs are kept private. The ContentHandler methods
     * consume SAX events to build the page DOM
     */
    private static class HtmlUnitDOMBuilder extends AbstractSAXParser
            implements ContentHandler /*, LexicalHandler */ {

        private final WebResponse webResponse_;
        private final WebWindow webWindow_;

        // private final ScriptFilter scriptFilter_;

        private HtmlPage page_;

        private Locator locator_;
        private final Stack stack_ = new Stack();

        private DomNode currentNode_;
        private StringBuffer characters_;

        /**
         * create a new builder for parsing the given response contents
         * @param webResponse the response data
         * @param webWindow the web window into which the page is to be loaded
         */
        public HtmlUnitDOMBuilder(final WebResponse webResponse, final WebWindow webWindow) {
            super(new HTMLConfiguration());

            webResponse_ = webResponse;
            webWindow_ = webWindow;

            try {
                setFeature( "http://cyberneko.org/html/features/augmentations", true );
                setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
                setFeature("http://cyberneko.org/html/features/report-errors", ValidateHtml_);
                setFeature("http://cyberneko.org/html/features/balance-tags/ignore-outside-content",
                    IgnoreOutsideContent_);
            }
            catch (final SAXException e) {
                throw new ObjectInstantiationException("unable to create HTML parser", e);
            }
        }

        /**
         * parse the input source. This is the only parse() method that should be called.
         *
         * @param inputSource an XMLInputSource
         * @throws java.io.IOException
         */
        public void parse(final XMLInputSource inputSource) throws IOException {

            setContentHandler(this);
            //setLexicalHandler(this); comments and CDATA

            super.parse(inputSource);
        }

        /**
         * @return the document locator
         */
        public Locator getLocator() {
            return locator_;
        }

        /**
         * set the document locator
         * @param locator
         */
        public void setDocumentLocator(Locator locator) {
            this.locator_ = locator;
        }

        /** @inheritDoc ContentHandler#startDocument() */
        public void startDocument() throws SAXException {
            page_ = new HtmlPage(webResponse_.getUrl(), webResponse_, webWindow_);
            webWindow_.setEnclosedPage(page_);

            currentNode_ = page_;
            stack_.push(currentNode_);
        }

        /** @inheritDoc ContentHandler#startElement(String,String,String,Attributes) */
        public void startElement(
                final String namespaceURI, final String localName,
                final String qName, final Attributes atts)
            throws SAXException {

            handleCharacters();

            final String tagLower = localName.toLowerCase();
            final IElementFactory factory = getElementFactory(tagLower);
            HtmlElement newElement = factory.createElement(page_, tagLower, atts);
            currentNode_.appendChild(newElement);
            currentNode_ = newElement;
            stack_.push(currentNode_);
        }

        /** @inheritDoc ContentHandler@endElement(String,String,String) */
        public void endElement(final String namespaceURI, final String localName, final String qName)
                throws SAXException {

            handleCharacters();
            stack_.pop(); //remove currentElement from stack

            if(!stack_.isEmpty()) {
                currentNode_ = (DomNode)stack_.peek();
            }
        }

        /** @inheritDoc ContentHandler#characters(char,int,int) */
        public void characters(final char ch[], final int start, final int length)
                throws SAXException {

            if(characters_ == null) {
                characters_ = new StringBuffer();
            }
            characters_.append(ch, start, length);
        }

        /** @inheritDoc ContentHandler#ignorableWhitespace(char,int,int) */
        public void ignorableWhitespace(final char ch[], final int start, final int length)
                throws SAXException {

            if(characters_ == null) {
                characters_ = new StringBuffer();
            }
            characters_.append(ch, start, length);
        }

        /**
         * pick up the chacracter data accumulated so far and add it to the
         * current element as a text node
         */
        private void handleCharacters() {

            if(characters_ != null && characters_.length() > 0) {
                final DomText text = new DomText(page_, characters_.toString());
                currentNode_.appendChild(text);
                characters_.setLength(0);
            }
        }

        /**
         * @param tagName an HTML tag name, in lowercase
         * @return the pre-registered element factory for the tag, or an UnknownElementFactory
         */
        private IElementFactory getElementFactory(final String tagName) {

            final IElementFactory factory = (IElementFactory)ELEMENT_FACTORIES.get(tagName);

            //return factory != null ? factory : UnknownElementFactory.instance;
            if(factory != null) {
                return factory;
            }
            else {
                return UnknownElementFactory.instance;
            }
        }

        /** @inheritDoc ContentHandler#endDocument() */
        public void endDocument() throws SAXException {
        }

        /** @inheritDoc ContentHandler#startPrefixMapping(String,String) */
        public void startPrefixMapping(final String prefix, final String uri)
                throws SAXException {
        }

        /** @inheritDoc ContentHandler#endPrefixMapping(String) */
        public void endPrefixMapping(final String prefix)
                throws SAXException {
        }

        /** @inheritDoc ContentHandler#processingInstrucction(String,String) */
        public void processingInstruction(final String target, final String data)
                throws SAXException {
        }

        /** @inheritDoc ContentHandler#skippedEntity(String) */
        public void skippedEntity(final String name)
                throws SAXException {
        }
    }
}
