/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.html.parser.neko;

import static org.htmlunit.BrowserVersionFeatures.HTML_COMMAND_TAG;
import static org.htmlunit.BrowserVersionFeatures.JS_SCRIPT_IN_TEMPLATE_EXECUTED_ON_ATTACH;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.ObjectInstantiationException;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import org.htmlunit.cyberneko.HTMLConfiguration;
import org.htmlunit.cyberneko.HTMLElements;
import org.htmlunit.cyberneko.HTMLEventInfo;
import org.htmlunit.cyberneko.HTMLScanner;
import org.htmlunit.cyberneko.HTMLTagBalancingListener;
import org.htmlunit.cyberneko.xerces.parsers.AbstractSAXParser;
import org.htmlunit.cyberneko.xerces.xni.Augmentations;
import org.htmlunit.cyberneko.xerces.xni.QName;
import org.htmlunit.cyberneko.xerces.xni.XMLAttributes;
import org.htmlunit.cyberneko.xerces.xni.XMLString;
import org.htmlunit.cyberneko.xerces.xni.XNIException;
import org.htmlunit.cyberneko.xerces.xni.parser.XMLInputSource;
import org.htmlunit.cyberneko.xerces.xni.parser.XMLParserConfiguration;
import org.htmlunit.html.DomComment;
import org.htmlunit.html.DomDocumentType;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomText;
import org.htmlunit.html.ElementFactory;
import org.htmlunit.html.Html;
import org.htmlunit.html.HtmlBody;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlHiddenInput;
import org.htmlunit.html.HtmlImage;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.HtmlSvg;
import org.htmlunit.html.HtmlTable;
import org.htmlunit.html.HtmlTableRow;
import org.htmlunit.html.HtmlTemplate;
import org.htmlunit.html.ScriptElement;
import org.htmlunit.html.SubmittableElement;
import org.htmlunit.html.XHtmlPage;
import org.htmlunit.html.parser.HTMLParser;
import org.htmlunit.html.parser.HTMLParserDOMBuilder;
import org.htmlunit.html.parser.HTMLParserListener;
import org.htmlunit.javascript.host.html.HTMLBodyElement;
import org.htmlunit.util.StringUtils;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * The parser and DOM builder. This class subclasses Xerces's AbstractSAXParser and implements
 * the ContentHandler interface. Thus all parser APIs are kept private. The ContentHandler methods
 * consume SAX events to build the page DOM
 *
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ethan Glasser-Camp
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 * @author Ronny Shapiro
 * @author Atsushi Nakagawa
 */
final class HtmlUnitNekoDOMBuilder extends AbstractSAXParser
        implements ContentHandler, LexicalHandler, HTMLTagBalancingListener, HTMLParserDOMBuilder {

    // cache Neko Elements for performance and memory efficiency
    private static final HTMLElements HTMLELEMENTS;
    private static final HTMLElements HTMLELEMENTS_WITH_CMD;

    static {
        // continue short code enumeration
        final short commandShortCode = HTMLElements.UNKNOWN + 1;

        final HTMLElements.Element command = new HTMLElements.Element(commandShortCode, "COMMAND",
                HTMLElements.Element.EMPTY, new short[] {HTMLElements.BODY, HTMLElements.HEAD}, null);

        HTMLELEMENTS = new HTMLElements();

        final HTMLElements value = new HTMLElements();
        value.setElement(command);
        HTMLELEMENTS_WITH_CMD = value;
    }

    private enum HeadParsed { YES, SYNTHESIZED, NO }

    private final HTMLParser htmlParser_;
    private final HtmlPage page_;

    private Locator locator_;
    private final Deque<DomNode> stack_ = new ArrayDeque<>();

    /** Did the snippet tried to overwrite the start node? */
    private boolean snippetStartNodeOverwritten_;
    private final int initialSize_;
    private DomNode currentNode_;
    private final boolean createdByJavascript_;
    private final XMLString characters_ = new XMLString();
    private HtmlUnitNekoDOMBuilder.HeadParsed headParsed_ = HeadParsed.NO;
    private HtmlElement body_;
    private boolean lastTagWasSynthesized_;
    private HtmlForm consumingForm_;
    private boolean formEndingIsAdjusting_;
    private boolean insideSvg_;
    private boolean insideTemplate_;

    private static final String FEATURE_AUGMENTATIONS = "http://cyberneko.org/html/features/augmentations";
    private static final String FEATURE_PARSE_NOSCRIPT
        = "http://cyberneko.org/html/features/parse-noscript-content";

    /**
     * Parses and then inserts the specified HTML content into the HTML content currently being parsed.
     * @param html the HTML content to push
     */
    @Override
    public void pushInputString(final String html) {
        page_.registerParsingStart();
        page_.registerInlineSnippetParsingStart();
        try {
            final WebResponse webResponse = page_.getWebResponse();
            final Charset charset = webResponse.getContentCharset();
            final String url = webResponse.getWebRequest().getUrl().toString();
            final XMLInputSource in = new XMLInputSource(null, url, null, new StringReader(html), charset.name());
            ((HTMLConfiguration) parserConfiguration_).evaluateInputSource(in);
        }
        finally {
            page_.registerParsingEnd();
            page_.registerInlineSnippetParsingEnd();
        }
    }

    /**
     * Creates a new builder for parsing the specified response contents.
     * @param node the location at which to insert the new content
     * @param url the page's URL
     * @param createdByJavascript if true the (script) tag was created by javascript
     */
    HtmlUnitNekoDOMBuilder(final HTMLParser htmlParser,
            final DomNode node, final URL url, final String htmlContent, final boolean createdByJavascript) {
        super(createConfiguration(node.getPage().getWebClient().getBrowserVersion()));

        htmlParser_ = htmlParser;
        page_ = (HtmlPage) node.getPage();

        currentNode_ = node;
        for (final Node ancestor : currentNode_.getAncestors()) {
            stack_.push((DomNode) ancestor);
        }
        createdByJavascript_ = createdByJavascript;

        final WebClient webClient = page_.getWebClient();
        final HTMLParserListener listener = webClient.getHTMLParserListener();
        final boolean reportErrors = listener != null;
        if (reportErrors) {
            parserConfiguration_.setErrorHandler(new HtmlUnitNekoHTMLErrorHandler(listener, url, htmlContent));
        }

        try {
            setFeature(FEATURE_AUGMENTATIONS, true);
            setFeature("http://cyberneko.org/html/features/report-errors", reportErrors);
            setFeature(FEATURE_PARSE_NOSCRIPT, !webClient.isJavaScriptEnabled());
            setFeature(HTMLScanner.ALLOW_SELFCLOSING_IFRAME, false);

            setContentHandler(this);
            setLexicalHandler(this); //comments and CDATA
        }
        catch (final SAXException e) {
            throw new ObjectInstantiationException("unable to create HTML parser", e);
        }
        initialSize_ = stack_.size();
    }

    /**
     * Create the configuration depending on the simulated browser
     * @return the configuration
     */
    private static XMLParserConfiguration createConfiguration(final BrowserVersion browserVersion) {
        if (browserVersion.hasFeature(HTML_COMMAND_TAG)) {
            return new HTMLConfiguration(HTMLELEMENTS_WITH_CMD);
        }
        return new HTMLConfiguration(HTMLELEMENTS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDocumentLocator(final Locator locator) {
        locator_ = locator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startDocument() throws SAXException {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void startElement(final QName element, final XMLAttributes attributes, final Augmentations augs)
        throws XNIException {
        // augs might change so we store only the interesting part
        lastTagWasSynthesized_ = isSynthesized(augs);
        super.startElement(element, attributes, augs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startElement(String namespaceURI, final String localName, final String qName, final Attributes atts)
        throws SAXException {

        if (snippetStartNodeOverwritten_) {
            snippetStartNodeOverwritten_ = false;
            return;
        }
        handleCharacters();

        final String tagLower = StringUtils.toRootLowerCase(localName);
        if (page_.isParsingHtmlSnippet() && ("html".equals(tagLower) || "body".equals(tagLower))) {
            // we have to push the current node on the stack to make sure
            // the endElement call is able to remove a node from the stack
            stack_.push(currentNode_);
            return;
        }

        if ("head".equals(tagLower)) {
            if (headParsed_ == HeadParsed.YES || page_.isParsingHtmlSnippet()) {
                // we have to push the current node on the stack to make sure
                // the endElement call is able to remove a node from the stack
                stack_.push(currentNode_);
                return;
            }

            headParsed_ = lastTagWasSynthesized_ ? HeadParsed.SYNTHESIZED : HeadParsed.YES;
        }

        if (namespaceURI != null) {
            namespaceURI = namespaceURI.trim();
        }

        // If we're adding a body element, keep track of any temporary synthetic ones
        // that we may have had to create earlier (for document.write(), for example).
        HtmlBody oldBody = null;
        if ("body".equals(qName) && page_.getBody() instanceof HtmlBody) {
            oldBody = (HtmlBody) page_.getBody();
        }

        // Add the new node.
        if (!(page_ instanceof XHtmlPage) && Html.XHTML_NAMESPACE.equals(namespaceURI)) {
            namespaceURI = null;
        }

        final ElementFactory factory =
                htmlParser_.getElementFactory(page_, namespaceURI, qName, insideSvg_, false);
        if (factory == HtmlUnitNekoHtmlParser.SVG_FACTORY) {
            namespaceURI = Html.SVG_NAMESPACE;
        }
        final DomElement newElement = factory.createElementNS(page_, namespaceURI, qName, atts);
        newElement.setStartLocation(locator_.getLineNumber(), locator_.getColumnNumber());

        // parse can't replace everything as it does not buffer elements while parsing
        addNodeToRightParent(currentNode_, newElement);

        if (newElement instanceof HtmlSvg) {
            insideSvg_ = true;
        }
        else if (newElement instanceof HtmlTemplate) {
            insideTemplate_ = true;
        }

        // Forms own elements simply by enclosing source-wise rather than DOM parent-child relationship
        // Forms without a </form> will keep consuming forever
        if (newElement instanceof HtmlForm) {
            consumingForm_ = (HtmlForm) newElement;
            formEndingIsAdjusting_ = false;
        }
        else if (consumingForm_ != null) {
            // If the current form enclosed a suitable element
            if (newElement instanceof SubmittableElement) {
                // Let these be owned by the form
                if (((HtmlElement) newElement).getEnclosingForm() != consumingForm_) {
                    ((HtmlElement) newElement).setOwningForm(consumingForm_);
                }
            }
        }

        // If we had an old synthetic body and we just added a real body element, quietly
        // remove the old body and move its children to the real body element we just added.
        if (oldBody != null) {
            oldBody.quietlyRemoveAndMoveChildrenTo(newElement);
        }

        if (!insideSvg_ && "body".equals(tagLower)) {
            body_ = (HtmlElement) newElement;
        }
        else if (createdByJavascript_
                && newElement instanceof ScriptElement
                && (!insideTemplate_
                        || !page_.getWebClient().getBrowserVersion()
                                .hasFeature(JS_SCRIPT_IN_TEMPLATE_EXECUTED_ON_ATTACH))) {
            final ScriptElement script = (ScriptElement) newElement;
            script.markAsCreatedByDomParser();
        }

        currentNode_ = newElement;
        stack_.push(currentNode_);
    }

    /**
     * Adds the new node to the right parent that is not necessary the currentNode in case of
     * malformed HTML code. The method tries to emulate the behavior of Firefox.
     */
    private void addNodeToRightParent(final DomNode currentNode, final DomElement newElement) {
        final String currentNodeName = currentNode.getNodeName();
        final String newNodeName = newElement.getNodeName();

        // First ensure table elements are housed correctly
        if (isTableChild(newNodeName)) {
            final DomNode parent =
                    "table".equals(currentNodeName) ? currentNode : findElementOnStack("table");
            appendChild(parent, newElement);
            return;
        }
        if ("tr".equals(newNodeName)) {
            final DomNode parent =
                    isTableChild(currentNodeName) ? currentNode : findElementOnStack("tbody", "thead", "tfoot");
            appendChild(parent, newElement);
            return;
        }
        if (isTableCell(newNodeName)) {
            final DomNode parent =
                    "tr".equals(currentNodeName) ? currentNode : findElementOnStack("tr");
            appendChild(parent, newElement);
            return;
        }

        // Next ensure non-table elements don't appear in tables
        if ("table".equals(currentNodeName) || isTableChild(currentNodeName) || "tr".equals(currentNodeName)) {
            if ("template".equals(newNodeName)) {
                currentNode.appendChild(newElement);
            }

            // Scripts, forms, and styles are exempt
            else if (!"colgroup".equals(currentNodeName)
                    && ("script".equals(newNodeName)
                        || "form".equals(newNodeName)
                        || "style".equals(newNodeName))) {
                currentNode.appendChild(newElement);
            }

            // These are good
            else if ("col".equals(newNodeName) && "colgroup".equals(currentNodeName)) {
                currentNode.appendChild(newElement);
            }
            else if ("caption".equals(currentNodeName)) {
                currentNode.appendChild(newElement);
            }
            else if (newElement instanceof HtmlHiddenInput) {
                currentNode.appendChild(newElement);
            }
            else {
                // Move before the table
                final DomNode parent = findElementOnStack("table");
                parent.insertBefore(newElement);
            }
            return;
        }

        if (formEndingIsAdjusting_ && "form".equals(currentNodeName)) {
            // We cater to HTMLTagBalancer's shortcomings by moving this node out of the <form>
            appendChild(currentNode.getParentNode(), newElement);
            return;
        }

        // Everything else
        appendChild(currentNode, newElement);
    }

    private DomNode findElementOnStack(final String... searchedElementNames) {
        DomNode searchedNode = null;
        for (final DomNode node : stack_) {
            if (ArrayUtils.contains(searchedElementNames, node.getNodeName())) {
                searchedNode = node;
                break;
            }
        }

        if (searchedNode == null) {
            searchedNode = stack_.peek(); // this is surely wrong but at least it won't throw a NPE
        }

        return searchedNode;
    }

    private static boolean isTableChild(final String nodeName) {
        return "thead".equals(nodeName)
                || "tbody".equals(nodeName)
                || "tfoot".equals(nodeName)
                || "caption".equals(nodeName)
                || "colgroup".equals(nodeName);
    }

    private static boolean isTableCell(final String nodeName) {
        if (nodeName == null || nodeName.length() != 2) {
            return false;
        }
        return "td".equals(nodeName) || "th".equals(nodeName);
    }

    /** {@inheritDoc} */
    @Override
    public void endElement(final QName element, final Augmentations augs)
        throws XNIException {
        // augs might change so we store only the interesting part
        lastTagWasSynthesized_ = isSynthesized(augs);
        super.endElement(element, augs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endElement(final String namespaceURI, final String localName, final String qName)
        throws SAXException {

        final String tagLower = StringUtils.toRootLowerCase(localName);

        handleCharacters();

        if (page_.isParsingHtmlSnippet()) {
            if ("html".equals(tagLower) || "body".equals(tagLower)) {
                return;
            }
            if (stack_.size() == initialSize_) {
                // a <p> inside a <p> is valid for innerHTML processing
                // see HTMLParser2Test for more cases
                snippetStartNodeOverwritten_ = !"p".equals(tagLower);
                return;
            }
        }

        if ("svg".equals(tagLower)) {
            insideSvg_ = false;
        }
        else if ("template".equals(tagLower)) {
            insideTemplate_ = false;
        }

        // this only avoids a problem when the stack is empty here
        // but for this case we made the problem before - the balancing
        // is broken already
        if (stack_.isEmpty()) {
            return;
        }

        final DomNode previousNode = stack_.pop(); //remove currentElement from stack
        previousNode.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());

        if ("form".equals(tagLower) && !lastTagWasSynthesized_) {
            // We get here if the </form> was on the same DOM tree depth as the <form> that started it,
            // otherwise HTMLTagBalancer gives us the end through ignoredEndElement()
            consumingForm_ = null;
        }

        if (!stack_.isEmpty()) {
            currentNode_ = stack_.peek();
        }

        final boolean postponed = page_.isParsingInlineHtmlSnippet();
        previousNode.onAllChildrenAddedToPage(postponed);
    }

    /** {@inheritDoc} */
    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        characters_.append(ch, start, length);
    }

    /** {@inheritDoc} */
    @Override
    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        characters_.append(ch, start, length);
    }

    /**
     * Picks up the character data accumulated so far and add it to the current element as a text node.
     */
    private void handleCharacters() {
        // make the code easier to read because we remove a nesting level
        if (characters_.length() == 0) {
            return;
        }

        // Use the normal behavior: append a text node for the accumulated text.
        final String textValue = characters_.toString();
        final DomText textNode = new DomText(page_, textValue);
        characters_.clear();

        if (org.apache.commons.lang3.StringUtils.isNotBlank(textValue)) {
            // malformed HTML: </td>some text</tr> => text comes before the table
            if (currentNode_ instanceof HtmlTableRow) {
                final HtmlTableRow row = (HtmlTableRow) currentNode_;
                final HtmlTable enclosingTable = row.getEnclosingTable();
                if (enclosingTable != null) { // may be null when called from Range.createContextualFragment
                    if (enclosingTable.getPreviousSibling() instanceof DomText) {
                        final DomText domText = (DomText) enclosingTable.getPreviousSibling();
                        domText.setTextContent(domText.getWholeText() + textValue);
                    }
                    else {
                        enclosingTable.insertBefore(textNode);
                    }
                }
            }
            else if (currentNode_ instanceof HtmlTable) {
                final HtmlTable enclosingTable = (HtmlTable) currentNode_;
                if (enclosingTable.getPreviousSibling() instanceof DomText) {
                    final DomText domText = (DomText) enclosingTable.getPreviousSibling();
                    domText.setTextContent(domText.getWholeText() + textValue);
                }
                else {
                    enclosingTable.insertBefore(textNode);
                }
            }
            else if (currentNode_ instanceof HtmlImage) {
                currentNode_.getParentNode().appendChild(textNode);
            }
            else {
                appendChild(currentNode_, textNode);
            }
        }
        else {
            appendChild(currentNode_, textNode);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void endDocument() throws SAXException {
        handleCharacters();
        if (locator_ != null) {
            page_.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());
        }
    }

    /** {@inheritDoc} */
    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void processingInstruction(final String target, final String data) throws SAXException {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void skippedEntity(final String name) throws SAXException {
        // nothing to do
    }

    // LexicalHandler methods

    /** {@inheritDoc} */
    @Override
    public void comment(final char[] ch, final int start, final int length) {
        handleCharacters();
        final String data = new String(ch, start, length);
        final DomComment comment = new DomComment(page_, data);
        appendChild(currentNode_, comment);
    }

    /** {@inheritDoc} */
    @Override
    public void endCDATA() {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void endDTD() {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void endEntity(final String name) {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void startCDATA() {
        // nothing to do
    }

    /** {@inheritDoc} */
    @Override
    public void startDTD(final String name, final String publicId, final String systemId) {
        final DomDocumentType type = new DomDocumentType(page_, name, publicId, systemId);
        page_.setDocumentType(type);

        final Node child;
        child = type;
        page_.appendChild(child);
    }

    /** {@inheritDoc} */
    @Override
    public void startEntity(final String name) {
        // nothing to do
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ignoredEndElement(final QName element, final Augmentations augs) {
        // HTMLTagBalancer brings us here if </form> was found in the source on a different
        // DOM tree depth (either above or below) to the <form> that started it
        if ("form".equals(element.getLocalpart()) && consumingForm_ != null) {
            consumingForm_ = null;

            if (findElementOnStack("table", "form") instanceof HtmlTable) {
                // The </form> just goes missing for these (really? just tables?)
            }
            else {
                /*
                 * This </form> was ignored by HTMLTagBalancer as it generates its own
                 * </form> at the end of the depth with the starting <form>.
                 * e.g. This:
                 * | <form>
                 * |   <div>
                 * |     </form> <!--ignored by HTMLTagBalancer-->
                 * |   </div>
                 * |   <input>
                 *
                 * is turned into:
                 * | <form>
                 * |   <div>
                 * |   </div>
                 * |   <input>
                 * | </form> <!--synthesized by HTMLTagBalancer-->
                 *
                 * but this isn't suitable for us because </form> shouldn't be ignored but
                 * rather moved directly behind the tree it's in to instead become:
                 * | <form>
                 * |   <div>
                 * |   </div>
                 * | </form> <!--moved out of div-->
                 * | <input> <!--proceeding children are not part of form-->
                 */
                // We cater for this by moving out nodes such as the <input> in the above
                // diagram out of the form
                formEndingIsAdjusting_ = true;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ignoredStartElement(final QName elem, final XMLAttributes attrs, final Augmentations augs) {
        // when multiple html/body elements are encountered, the attributes of the discarded
        // elements are used when not previously defined
        if (attrs != null && body_ != null) {
            String lp = elem.getLocalpart();
            if (lp != null && lp.length() == 4) {
                lp = lp.toLowerCase(Locale.ROOT);
                if ("body".equals(lp)) {
                    copyAttributes(body_, attrs);
                }
                else if ("html".equals(lp)) {
                    final DomNode parent = body_.getParentNode();
                    if (parent instanceof DomElement) {
                        copyAttributes((DomElement) parent, attrs);
                    }
                }
            }
        }
    }

    private static void copyAttributes(final DomElement to, final XMLAttributes attrs) {
        final int length = attrs.getLength();

        for (int i = 0; i < length; i++) {
            final String attrName = StringUtils.toRootLowerCase(attrs.getLocalName(i));
            if (to.getAttributes().getNamedItem(attrName) == null) {
                to.setAttribute(attrName, attrs.getValue(i));
                if (attrName.startsWith("on") && to.getPage().getWebClient().isJavaScriptEngineEnabled()
                        && to.getScriptableObject() instanceof HTMLBodyElement) {
                    final HTMLBodyElement jsBody = to.getScriptableObject();
                    jsBody.createEventHandlerFromAttribute(attrName, attrs.getValue(i));
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void parse(final XMLInputSource inputSource) throws XNIException, IOException {
        final HTMLParserDOMBuilder oldBuilder = page_.getDOMBuilder();
        page_.setDOMBuilder(this);
        try {
            super.parse(inputSource);
        }
        finally {
            page_.setDOMBuilder(oldBuilder);
        }
    }

    HtmlElement getBody() {
        return body_;
    }

    private static boolean isSynthesized(final Augmentations augs) {
        return augs instanceof HTMLEventInfo && ((HTMLEventInfo) augs).isSynthesized();
    }

    private static void appendChild(final DomNode parent, final DomNode child) {
        if (parent instanceof HtmlTemplate) {
            ((HtmlTemplate) parent).getContent().appendChild(child);
            return;
        }

        parent.appendChild(child);
    }
}
