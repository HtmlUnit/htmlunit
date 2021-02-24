/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.parser.neko;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_ATTRIBUTE_LOWER_CASE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_COMMAND_TAG;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_ISINDEX_TAG;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTML_MAIN_TAG;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.META_X_UA_COMPATIBLE;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.xerces.parsers.AbstractSAXParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParserConfiguration;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ObjectInstantiationException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.ElementFactory;
import com.gargoylesoftware.htmlunit.html.Html;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlMeta;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import com.gargoylesoftware.htmlunit.html.SubmittableElement;
import com.gargoylesoftware.htmlunit.html.XHtmlPage;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParser;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParserDOMBuilder;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParserListener;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLBodyElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;

import net.sourceforge.htmlunit.cyberneko.HTMLConfiguration;
import net.sourceforge.htmlunit.cyberneko.HTMLElements;
import net.sourceforge.htmlunit.cyberneko.HTMLEventInfo;
import net.sourceforge.htmlunit.cyberneko.HTMLScanner;
import net.sourceforge.htmlunit.cyberneko.HTMLTagBalancingListener;

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
 */
final class HtmlUnitNekoDOMBuilder extends AbstractSAXParser
        implements ContentHandler, LexicalHandler, HTMLTagBalancingListener, HTMLParserDOMBuilder {

    // cache Neko Elements for performance and memory
    private static final Map<Triple<Boolean, Boolean, Boolean>, HTMLElements> ELEMENTS;
    static {
        ELEMENTS = new HashMap<>();

        Triple<Boolean, Boolean, Boolean> key;
        HTMLElements value;

        final HTMLElements.Element command = new HTMLElements.Element(HTMLElements.COMMAND, "COMMAND",
                HTMLElements.Element.EMPTY, HTMLElements.BODY, null);
        final HTMLElements.Element isIndex = new HTMLElements.Element(HTMLElements.ISINDEX, "ISINDEX",
                HTMLElements.Element.INLINE, HTMLElements.BODY, null);
        final HTMLElements.Element main = new HTMLElements.Element(HTMLElements.MAIN, "MAIN",
                HTMLElements.Element.INLINE, HTMLElements.BODY, null);

        key = Triple.of(Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
        value = new HTMLElements();
        ELEMENTS.put(key, value);

        key = Triple.of(Boolean.FALSE, Boolean.FALSE, Boolean.TRUE);
        value = new HTMLElements();
        value.setElement(main);
        ELEMENTS.put(key, value);

        key = Triple.of(Boolean.FALSE, Boolean.TRUE, Boolean.FALSE);
        value = new HTMLElements();
        value.setElement(isIndex);
        ELEMENTS.put(key, value);

        key = Triple.of(Boolean.FALSE, Boolean.TRUE, Boolean.TRUE);
        value = new HTMLElements();
        value.setElement(isIndex);
        value.setElement(main);
        ELEMENTS.put(key, value);

        key = Triple.of(Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
        value = new HTMLElements();
        value.setElement(command);
        ELEMENTS.put(key, value);

        key = Triple.of(Boolean.TRUE, Boolean.FALSE, Boolean.TRUE);
        value = new HTMLElements();
        value.setElement(command);
        value.setElement(main);
        ELEMENTS.put(key, value);

        key = Triple.of(Boolean.TRUE, Boolean.TRUE, Boolean.FALSE);
        value = new HTMLElements();
        value.setElement(command);
        value.setElement(isIndex);
        ELEMENTS.put(key, value);

        key = Triple.of(Boolean.TRUE, Boolean.TRUE, Boolean.TRUE);
        value = new HTMLElements();
        value.setElement(command);
        value.setElement(isIndex);
        value.setElement(main);
        ELEMENTS.put(key, value);
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
    private StringBuilder characters_;
    private HtmlUnitNekoDOMBuilder.HeadParsed headParsed_ = HeadParsed.NO;
    private HtmlElement body_;
    private boolean lastTagWasSynthesized_;
    private HtmlForm formWaitingForLostChildren_;
    private boolean insideSvg_;

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
            ((HTMLConfiguration) fConfiguration).evaluateInputSource(in);
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
     */
    HtmlUnitNekoDOMBuilder(final HTMLParser htmlParser,
                                final DomNode node, final URL url, final String htmlContent) {
        super(createConfiguration(node.getPage().getWebClient().getBrowserVersion()));

        htmlParser_ = htmlParser;
        page_ = (HtmlPage) node.getPage();

        currentNode_ = node;
        for (final Node ancestor : currentNode_.getAncestors()) {
            stack_.push((DomNode) ancestor);
        }

        final WebClient webClient = page_.getWebClient();
        final HTMLParserListener listener = webClient.getHTMLParserListener();
        final boolean reportErrors = listener != null;
        if (reportErrors) {
            fConfiguration.setErrorHandler(new HtmlUnitNekoHTMLErrorHandler(listener, url, htmlContent));
        }

        try {
            setFeature(FEATURE_AUGMENTATIONS, true);
            if (!webClient.getBrowserVersion().hasFeature(HTML_ATTRIBUTE_LOWER_CASE)) {
                setProperty("http://cyberneko.org/html/properties/names/attrs", "no-change");
            }
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
     * @param webClient the current WebClient
     * @return the configuration
     */
    private static XMLParserConfiguration createConfiguration(final BrowserVersion browserVersion) {
        final HTMLElements elements = ELEMENTS.get(
                Triple.of(browserVersion.hasFeature(HTML_COMMAND_TAG),
                        browserVersion.hasFeature(HTML_ISINDEX_TAG),
                        browserVersion.hasFeature(HTML_MAIN_TAG)));
        return new HTMLConfiguration(elements);
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

        final String tagLower = localName.toLowerCase(Locale.ROOT);
        if (page_.isParsingHtmlSnippet() && ("html".equals(tagLower) || "body".equals(tagLower))) {
            return;
        }

        if ("head".equals(tagLower)) {
            if (headParsed_ == HeadParsed.YES || page_.isParsingHtmlSnippet()) {
                return;
            }

            headParsed_ = lastTagWasSynthesized_ ? HeadParsed.SYNTHESIZED : HeadParsed.YES;
        }

        if (namespaceURI != null) {
            namespaceURI = namespaceURI.trim();
        }

        // add a head if none was there
        else if (headParsed_ == HeadParsed.NO && ("body".equals(tagLower) || "frameset".equals(tagLower))) {
            final ElementFactory factory = htmlParser_.getElementFactory(page_, null, "head", insideSvg_, false);
            final DomElement newElement = factory.createElement(page_, "head", null);
            currentNode_.appendChild(newElement);
            headParsed_ = HeadParsed.SYNTHESIZED;
        }

        // If we're adding a body element, keep track of any temporary synthetic ones
        // that we may have had to create earlier (for document.write(), for example).
        HtmlBody oldBody = null;
        if ("body".equals(qName) && page_.getBody() instanceof HtmlBody) {
            oldBody = (HtmlBody) page_.getBody();
        }

        // Need to reset this at each starting form tag because it could be set from a synthesized
        // end tag.
        if ("form".equals(tagLower)) {
            formWaitingForLostChildren_ = null;
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
        final DomElement newElement = factory.createElementNS(page_, namespaceURI, qName, atts, true);
        newElement.setStartLocation(locator_.getLineNumber(), locator_.getColumnNumber());

        // parse can't replace everything as it does not buffer elements while parsing
        addNodeToRightParent(currentNode_, newElement);

        if ("svg".equals(tagLower)) {
            insideSvg_ = true;
        }

        // If we had an old synthetic body and we just added a real body element, quietly
        // remove the old body and move its children to the real body element we just added.
        if (oldBody != null) {
            oldBody.quietlyRemoveAndMoveChildrenTo(newElement);
        }

        if ("body".equals(tagLower)) {
            body_ = (HtmlElement) newElement;
        }
        else if ("meta".equals(tagLower) && page_.hasFeature(META_X_UA_COMPATIBLE)) {
            final HtmlMeta meta = (HtmlMeta) newElement;
            if ("X-UA-Compatible".equals(meta.getHttpEquivAttribute())) {
                final String content = meta.getContentAttribute();
                if (content.startsWith("IE=")) {
                    final String mode = content.substring(3).trim();
                    final int version = page_.getWebClient().getBrowserVersion().getBrowserVersionNumeric();
                    try {
                        int value = Integer.parseInt(mode);
                        if (value > version) {
                            value = version;
                        }
                        ((HTMLDocument) page_.getScriptableObject()).forceDocumentMode(value);
                    }
                    catch (final Exception e) {
                        // ignore
                    }
                }
            }
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

        DomNode parent = currentNode;

        // If the new node is a table element and the current node isn't one search the stack for the
        // correct parent.
        if ("tr".equals(newNodeName) && !isTableChild(currentNodeName)) {
            parent = findElementOnStack("tbody", "thead", "tfoot");
        }
        else if (isTableChild(newNodeName) && !"table".equals(currentNodeName)) {
            parent = findElementOnStack("table");
        }
        else if (isTableCell(newNodeName) && !"tr".equals(currentNodeName)) {
            parent = findElementOnStack("tr");
        }

        // If the parent changed and the old parent was a form it is now waiting for lost children.
        if (parent != currentNode && "form".equals(currentNodeName)) {
            formWaitingForLostChildren_ = (HtmlForm) currentNode;
        }

        final String parentNodeName = parent.getNodeName();

        if (!"script".equals(newNodeName) // scripts are valid inside tables
                && (
                    ("table".equals(parentNodeName) && !isTableChild(newNodeName))
                        || (!"tr".equals(newNodeName)
                                && ("thead".equals(parentNodeName)
                                        || "tbody".equals(parentNodeName)
                                        || "tfoot".equals(parentNodeName)))
                        || ("colgroup".equals(parentNodeName) && !"col".equals(newNodeName))
                        || ("tr".equals(parentNodeName) && !isTableCell(newNodeName)))) {
            // If its a form or submitable just add it even though the resulting DOM is incorrect.
            // Otherwise insert the element before the table.
            if ("form".equals(newNodeName)) {
                formWaitingForLostChildren_ = (HtmlForm) newElement;
                parent.appendChild(newElement);
            }
            else if (newElement instanceof SubmittableElement) {
                if (formWaitingForLostChildren_ != null) {
                    formWaitingForLostChildren_.addLostChild((HtmlElement) newElement);
                }
                parent.appendChild(newElement);
            }
            else {
                parent = findElementOnStack("table");
                parent.insertBefore(newElement);
            }
        }
        else if (formWaitingForLostChildren_ != null && "form".equals(parentNodeName)) {
            // Do not append any children to invalid form. Submittable are inserted after the form,
            // everything else before the table.
            if (newElement instanceof SubmittableElement) {
                formWaitingForLostChildren_.addLostChild((HtmlElement) newElement);
                parent.getParentNode().appendChild(newElement);
            }
            else {
                parent = findElementOnStack("table");
                parent.insertBefore(newElement);
            }
        }
        else if (formWaitingForLostChildren_ != null && newElement instanceof SubmittableElement) {
            formWaitingForLostChildren_.addLostChild((HtmlElement) newElement);
            parent.appendChild(newElement);
        }
        else {
            parent.appendChild(newElement);
        }
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

        handleCharacters();

        final String tagLower = localName.toLowerCase(Locale.ROOT);

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

        // Need to reset this at each closing form tag because a valid form could start afterwards.
        if ("form".equals(tagLower)) {
            formWaitingForLostChildren_ = null;
        }

        final DomNode previousNode = stack_.pop(); //remove currentElement from stack
        previousNode.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());

        // special handling for form lost children (malformed HTML code where </form> is synthesized)
        if (previousNode instanceof HtmlForm && lastTagWasSynthesized_) {
            formWaitingForLostChildren_ = (HtmlForm) previousNode;
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
        if (characters_ == null) {
            characters_ = new StringBuilder();
        }
        characters_.append(ch, start, length);
    }

    /** {@inheritDoc} */
    @Override
    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        if (characters_ == null) {
            characters_ = new StringBuilder();
        }
        characters_.append(ch, start, length);
    }

    /**
     * Picks up the character data accumulated so far and add it to the current element as a text node.
     */
    private void handleCharacters() {
        if (characters_ != null && characters_.length() != 0) {
            if (currentNode_ instanceof HtmlHtml) {
                // In HTML, the <html> node only has two possible children:
                // the <head> and the <body>; any text is ignored.
                characters_.setLength(0);
            }
            else {
                // Use the normal behavior: append a text node for the accumulated text.
                final String textValue = characters_.toString();
                final DomText text = new DomText(page_, textValue);
                characters_.setLength(0);

                if (StringUtils.isNotBlank(textValue)) {
                    // malformed HTML: </td>some text</tr> => text comes before the table
                    if (currentNode_ instanceof HtmlTableRow) {
                        final HtmlTableRow row = (HtmlTableRow) currentNode_;
                        final HtmlTable enclosingTable = row.getEnclosingTable();
                        if (enclosingTable != null) { // may be null when called from Range.createContextualFragment
                            if (enclosingTable.getPreviousSibling() instanceof DomText) {
                                final DomText domText = (DomText) enclosingTable.getPreviousSibling();
                                domText.setTextContent(domText + textValue);
                            }
                            else {
                                enclosingTable.insertBefore(text);
                            }
                        }
                    }
                    else if (currentNode_ instanceof HtmlTable) {
                        final HtmlTable enclosingTable = (HtmlTable) currentNode_;
                        if (enclosingTable.getPreviousSibling() instanceof DomText) {
                            final DomText domText = (DomText) enclosingTable.getPreviousSibling();
                            domText.setTextContent(domText + textValue);
                        }
                        else {
                            enclosingTable.insertBefore(text);
                        }
                    }
                    else if (currentNode_ instanceof HtmlImage) {
                        currentNode_.setNextSibling(text);
                    }
                    else {
                        currentNode_.appendChild(text);
                    }
                }
                else {
                    currentNode_.appendChild(text);
                }
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void endDocument() throws SAXException {
        handleCharacters();
        final DomNode currentPage = page_;
        currentPage.setEndLocation(locator_.getLineNumber(), locator_.getColumnNumber());
    }

    /** {@inheritDoc} */
    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
    }

    /** {@inheritDoc} */
    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
    }

    /** {@inheritDoc} */
    @Override
    public void processingInstruction(final String target, final String data) throws SAXException {
    }

    /** {@inheritDoc} */
    @Override
    public void skippedEntity(final String name) throws SAXException {
    }

    // LexicalHandler methods

    /** {@inheritDoc} */
    @Override
    public void comment(final char[] ch, final int start, final int length) {
        handleCharacters();
        final String data = new String(ch, start, length);
        final DomComment comment = new DomComment(page_, data);
        currentNode_.appendChild(comment);
    }

    /** {@inheritDoc} */
    @Override
    public void endCDATA() {
    }

    /** {@inheritDoc} */
    @Override
    public void endDTD() {
    }

    /** {@inheritDoc} */
    @Override
    public void endEntity(final String name) {
    }

    /** {@inheritDoc} */
    @Override
    public void startCDATA() {
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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ignoredEndElement(final QName element, final Augmentations augs) {
        // if real </form> is reached, don't accept fields anymore as lost children
        if ("form".equals(element.localpart)) {
            formWaitingForLostChildren_ = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ignoredStartElement(final QName elem, final XMLAttributes attrs, final Augmentations augs) {
        // when multiple body elements are encountered, the attributes of the discarded
        // elements are used when not previously defined
        if (body_ != null && "body".equalsIgnoreCase(elem.localpart) && attrs != null) {
            copyAttributes(body_, attrs);
        }
        if (body_ != null && "html".equalsIgnoreCase(elem.localpart) && attrs != null) {
            copyAttributes((DomElement) body_.getParentNode(), attrs);
        }
    }

    private static void copyAttributes(final DomElement to, final XMLAttributes attrs) {
        final int length = attrs.getLength();
        for (int i = 0; i < length; i++) {
            final String attrName = attrs.getLocalName(i).toLowerCase(Locale.ROOT);
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
        final HTMLEventInfo info = (augs == null) ? null
                : (HTMLEventInfo) augs.getItem(FEATURE_AUGMENTATIONS);
        return info != null && info.isSynthesized();
    }
}
