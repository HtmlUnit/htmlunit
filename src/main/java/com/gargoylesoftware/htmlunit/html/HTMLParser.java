/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
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
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.apache.xerces.util.DefaultErrorHandler;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.cyberneko.html.HTMLConfiguration;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import com.gargoylesoftware.htmlunit.ObjectInstantiationException;
import com.gargoylesoftware.htmlunit.TextUtil;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;

/**
 * SAX parser implementation that uses the neko {@link org.cyberneko.html.HTMLConfiguration}
 * to parse HTML into a HtmlUnit-specific DOM (HU-DOM) tree.
 * <p>
 * <em>Note that the parser currently does not handle CDATA or comment sections, i.e. these
 * do not appear in the resulting DOM tree</em>
 *
 * @version $Revision$
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public final class HTMLParser {

    private static final Map<String, IElementFactory> ELEMENT_FACTORIES = new HashMap<String, IElementFactory>();
    private static boolean IgnoreOutsideContent_;

    static {
        ELEMENT_FACTORIES.put("input", InputElementFactory.instance);

        final DefaultElementFactory defaultElementFactory = new DefaultElementFactory();
        ELEMENT_FACTORIES.put(HtmlAnchor.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlApplet.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlAddress.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlArea.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlBase.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlBaseFont.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlBidirectionalOverride.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlBlockQuote.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlBody.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlBreak.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlButton.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlCaption.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlCenter.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlTableColumn.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlTableColumnGroup.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlDefinitionDescription.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlDeletedText.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlDirectory.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlDivision.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlDefinitionList.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlDefinitionTerm.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlFieldSet.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlFont.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlForm.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlFrame.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlFrameSet.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlHeading1.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlHeading2.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlHeading3.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlHeading4.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlHeading5.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlHeading6.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlHead.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlHorizontalRule.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlHtml.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlInlineFrame.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlImage.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlInsertedText.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlIsIndex.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlLabel.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlLegend.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlListItem.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlLink.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlMap.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlMenu.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlMeta.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlNoFrames.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlNoScript.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlObject.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlOrderedList.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlOptionGroup.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlOption.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlParagraph.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlParameter.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlPreformattedText.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlInlineQuotation.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlScript.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlSelect.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlSpan.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlStyle.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlTitle.TAG_NAME, defaultElementFactory);

        ELEMENT_FACTORIES.put(HtmlTable.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlTableBody.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlTableDataCell.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlTableHeaderCell.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlTableRow.TAG_NAME, defaultElementFactory);

        ELEMENT_FACTORIES.put(HtmlTextArea.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlTableFooter.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlTableHeader.TAG_NAME, defaultElementFactory);
        ELEMENT_FACTORIES.put(HtmlUnorderedList.TAG_NAME, defaultElementFactory);
    }

    /**
     * Set the flag to control validation of the HTML content that is outside of the
     * BODY and HTML tags.  This flag is false by default to maintain compatibility with
     * current NekoHTML defaults.
     * @param ignoreOutsideContent - boolean flag to set
     */
    public static void setIgnoreOutsideContent(final boolean ignoreOutsideContent) {
        IgnoreOutsideContent_ = ignoreOutsideContent;
    }

    /**
     * Get the state of the flag to ignore content outside the BODY and HTML tags
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
        final IElementFactory result = (IElementFactory) ELEMENT_FACTORIES.get(tagName);

        if (result != null) {
            return result;
        }
        else {
            return UnknownElementFactory.instance;
        }
    }

    /**
     * You should never need to create one of these!
     */
    private HTMLParser() {
    }

    /**
      * Parses the HTML content from the given string into an object tree representation.
      *
      * @param parent the parent for the new nodes
      * @param source the (X)HTML to be parsed
      * @throws SAXException if a SAX error occurs
      * @throws IOException if an IO error occurs
      */
    public static void parseFragment(final DomNode parent, final String source)
        throws SAXException, IOException {

        final URL url = parent.getPage().getWebResponse().getUrl();
        final HtmlUnitDOMBuilder domBuilder = new HtmlUnitDOMBuilder(parent, url);
        domBuilder.setFeature("http://cyberneko.org/html/features/balance-tags/document-fragment", true);
        final XMLInputSource in = new XMLInputSource(
                null,
                parent.getPage().getWebResponse().getUrl().toString(),
                null,
                new StringReader(source),
                null);

        domBuilder.parse(in);
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
        final HtmlPage page = new HtmlPage(webResponse.getUrl(), webResponse, webWindow);
        webWindow.setEnclosedPage(page);
                
        final HtmlUnitDOMBuilder domBuilder = new HtmlUnitDOMBuilder(page, webResponse.getUrl());
        String charSet = webResponse.getContentCharSet();
        if (!Charset.isSupported(charSet)) {
            charSet = TextUtil.DEFAULT_CHARSET;
        }
        final XMLInputSource in = new XMLInputSource(
                null,
                webResponse.getUrl().toString(),
                null,
                webResponse.getContentAsStream(),
                charSet);

        try {
            domBuilder.parse(in);
        }
        catch (final XNIException e) {
            // extract enclosed exception
            final Throwable origin = extractNestedException(e);
            throw new RuntimeException("Failed parsing content from " + webResponse.getUrl(), origin);
        }
        return domBuilder.page_;
    }

    /**
     * Extract nested exception within an XNIException
     * (Nekohtml uses reflection and generated exceptions are wrapped many times
     * within XNIException and InvocationTargetException)
     * @param e the original XNIException
     * @return the cause exception
     */
    static Throwable extractNestedException(final Throwable e) {
        Throwable originalException = e;
        Throwable cause = ((XNIException) e).getException();
        while (cause != null) {
            originalException = cause;
            if (cause instanceof XNIException) {
                cause = ((XNIException) cause).getException();
            }
            else if (cause instanceof InvocationTargetException) {
                cause = cause.getCause();
            }
            else {
                cause = null;
            }
        }
        return originalException;
    }
    
    /**
     * The parser and DOM builder. This class subclasses Xerces's AbstractSAXParser and implements
     * the ContentHandler interface. Thus all parser APIs are kept private. The ContentHandler methods
     * consume SAX events to build the page DOM
     */
    private static final class HtmlUnitDOMBuilder extends AbstractSAXParser implements ContentHandler, LexicalHandler {
        private final HtmlPage page_;

        private Locator locator_;
        private final Stack<DomNode> stack_ = new Stack<DomNode>();

        private DomNode currentNode_;
        private StringBuilder characters_;
        private boolean headParsed_ = false;

        /**
         * create a new builder for parsing the given response contents
         * @param webResponse the response data
         * @param webWindow the web window into which the page is to be loaded
         */
        private HtmlUnitDOMBuilder(final DomNode page, final URL url) {
            super(new HTMLConfiguration());
            this.page_ = (HtmlPage) page.getPage();

            currentNode_ = page;
            stack_.push(currentNode_);
            
            final HTMLParserListener listener = page_.getWebClient().getHTMLParserListener();
            final boolean reportErrors;
            if (listener != null) {
                reportErrors = true;
                fConfiguration.setErrorHandler(new HTMLErrorHandler(listener, url));
            }
            else {
                reportErrors = false;
            }

            try {
                setFeature("http://cyberneko.org/html/features/augmentations", true);
                setProperty("http://cyberneko.org/html/properties/names/elems", "lower");
                setFeature("http://cyberneko.org/html/features/report-errors", reportErrors);
                setFeature("http://cyberneko.org/html/features/balance-tags/ignore-outside-content",
                    IgnoreOutsideContent_);

                setContentHandler(this);
                setLexicalHandler(this); //comments and CDATA

            }
            catch (final SAXException e) {
                throw new ObjectInstantiationException("unable to create HTML parser", e);
            }
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
        public void setDocumentLocator(final Locator locator) {
            locator_ = locator;
        }

        /** @inheritDoc ContentHandler#startDocument() */
        public void startDocument() throws SAXException {
        }

        /** @inheritDoc ContentHandler#startElement(String,String,String,Attributes) */
        public void startElement(
                final String namespaceURI, final String localName,
                final String qName, final Attributes atts)
            throws SAXException {

            handleCharacters();

            final String tagLower = localName.toLowerCase();

            if (tagLower.equals("head")) {
                headParsed_ = true;
            }
            // add a head if none was there
            else if (!headParsed_ && (tagLower.equals("body") || tagLower.equals("frameset"))) {
                final IElementFactory factory = getElementFactory("head");
                final HtmlElement newElement = factory.createElement(page_, "head", null);
                currentNode_.appendDomChild(newElement);
                headParsed_ = true;
            }
            // add a <tbody> if a <tr> is directly in <table>
            else if (tagLower.equals("tr") && currentNode_.getNodeName().equals("table")) {
                final IElementFactory factory = getElementFactory("tbody");
                final HtmlElement newElement = factory.createElement(page_, "tbody", null);
                currentNode_.appendDomChild(newElement);
                currentNode_ = newElement;
                stack_.push(currentNode_);
            }

            final IElementFactory factory = getElementFactory(tagLower);
            final HtmlElement newElement = factory.createElement(page_, tagLower, atts);
            newElement.setStartLocation(locator_.getLineNumber(), locator_.getColumnNumber());
            currentNode_.appendDomChild(newElement);
            currentNode_ = newElement;
            stack_.push(currentNode_);
        }

        /** @inheritDoc ContentHandler@endElement(String,String,String) */
        public void endElement(final String namespaceURI, final String localName, final String qName)
            throws SAXException {

            handleCharacters();

            final DomNode previousNode = (DomNode) stack_.pop(); //remove currentElement from stack
            previousNode.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());
            previousNode.onAllChildrenAddedToPage();

            // if we have added a extra node (tbody), we should remove it
            if (!currentNode_.getNodeName().equalsIgnoreCase(localName)) {
                stack_.pop(); //remove extra node from stack
            }

            if (!stack_.isEmpty()) {
                currentNode_ = (DomNode) stack_.peek();
            }
        }

        /** @inheritDoc ContentHandler#characters(char,int,int) */
        public void characters(final char ch[], final int start, final int length) throws SAXException {

            if (characters_ == null) {
                characters_ = new StringBuilder();
            }
            characters_.append(ch, start, length);
        }

        /** @inheritDoc ContentHandler#ignorableWhitespace(char,int,int) */
        public void ignorableWhitespace(final char ch[], final int start, final int length) throws SAXException {

            if (characters_ == null) {
                characters_ = new StringBuilder();
            }
            characters_.append(ch, start, length);
        }

        /**
         * pick up the character data accumulated so far and add it to the
         * current element as a text node
         */
        private void handleCharacters() {

            if (characters_ != null && characters_.length() > 0) {
                final DomText text = new DomText(page_, characters_.toString());
                currentNode_.appendDomChild(text);
                characters_.setLength(0);
            }
        }

        /**
         * @param tagName an HTML tag name, in lowercase
         * @return the pre-registered element factory for the tag, or an UnknownElementFactory
         */
        private IElementFactory getElementFactory(final String tagName) {

            final IElementFactory factory = (IElementFactory) ELEMENT_FACTORIES.get(tagName);

            if (factory != null) {
                return factory;
            }
            else {
                return UnknownElementFactory.instance;
            }
        }

        /** @inheritDoc ContentHandler#endDocument() */
        public void endDocument() throws SAXException {
            handleCharacters();
            final DomNode currentPage = page_;
            currentPage.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());
            addBodyToPageIfNecessary();
        }

        /**
         * Adds a body element to the current page, if necessary. Strictly speaking, this should
         * probably be done by NekoHTML. See the bug linked below. If and when that bug is fixed,
         * we can get rid of this code.
         *
         * http://sourceforge.net/tracker/index.php?func=detail&aid=1898038&group_id=195122&atid=952178
         */
        private void addBodyToPageIfNecessary() {
            final Element doc = page_.getDocumentElement();
            boolean hasBody = false;
            for (Node child = doc.getFirstChild(); child != null; child = child.getNextSibling()) {
                if (child instanceof HtmlBody || child instanceof HtmlFrameSet) {
                    hasBody = true;
                    break;
                }
            }
            if (!hasBody) {
                final HtmlBody body = new HtmlBody(null, "body", page_, null);
                doc.appendChild(body);
            }
        }

        /** @inheritDoc ContentHandler#startPrefixMapping(String,String) */
        public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        }

        /** @inheritDoc ContentHandler#endPrefixMapping(String) */
        public void endPrefixMapping(final String prefix) throws SAXException {
        }

        /** @inheritDoc ContentHandler#processingInstrucction(String,String) */
        public void processingInstruction(final String target, final String data) throws SAXException {
        }

        /** @inheritDoc ContentHandler#skippedEntity(String) */
        public void skippedEntity(final String name) throws SAXException {
        }

        // LexicalHandler methods

        /** @inheritDoc LexicalHandler#comment(char[],int,int) */
        public void comment(final char[] ch, final int start, final int length) {
            handleCharacters();
            final DomComment comment = new DomComment(page_, String.valueOf(ch, start, length));
            currentNode_.appendDomChild(comment);
        }
        
        /** @inheritDoc LexicalHandler#endCDATA() */
        public void endCDATA() {
        }

        /** @inheritDoc LexicalHandler#endDTD() */
        public void endDTD() {
        }

        /** @inheritDoc LexicalHandler#endEntity() */
        public void endEntity(final String name) {
        }

        /** @inheritDoc LexicalHandler#startCDATA() */
        public void startCDATA() {
        }

        /** @inheritDoc LexicalHandler#startDTD(String,String,String) */
        public void startDTD(final String name, final String publicId, final String systemId) {
        }

        /** @inheritDoc LexicalHandler#startEntity(String) */
        public void startEntity(final String name) {
        }
    }
}

/**
 * Utility to transmit parsing errors to a {@link HTMLParserListener}.
 */
class HTMLErrorHandler extends DefaultErrorHandler {
    private final HTMLParserListener listener_;
    private final URL url_;

    HTMLErrorHandler(final HTMLParserListener listener, final URL url) {
        WebAssert.notNull("listener", listener);
        WebAssert.notNull("url", url);
        listener_ = listener;
        url_ = url;
    }

    /** @see DefaultErrorHandler#error(String,String,XMLParseException) */
    @Override
    public void error(final String domain, final String key,
            final XMLParseException exception) throws XNIException {
        listener_.error(exception.getMessage(),
                url_,
                exception.getLineNumber(),
                exception.getColumnNumber(),
                key);
    }

    /** @see DefaultErrorHandler#warning(String,String,XMLParseException) */
    @Override
    public void warning(final String domain, final String key,
            final XMLParseException exception) throws XNIException {
        listener_.warning(exception.getMessage(),
                url_,
                exception.getLineNumber(),
                exception.getColumnNumber(),
                key);
    }
}
