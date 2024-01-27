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
package org.htmlunit.util;

import static org.htmlunit.html.DomElement.ATTRIBUTE_NOT_DEFINED;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.input.BOMInputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebResponse;
import org.htmlunit.html.DomAttr;
import org.htmlunit.html.DomCDataSection;
import org.htmlunit.html.DomComment;
import org.htmlunit.html.DomDocumentType;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.DomProcessingInstruction;
import org.htmlunit.html.DomText;
import org.htmlunit.html.ElementFactory;
import org.htmlunit.html.Html;
import org.htmlunit.platform.Platform;
import org.htmlunit.xml.XmlPage;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * Provides facility method to work with XML responses.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ronald Brill
 * @author Chuck Dumont
 * @author Frank Danek
 */
public final class XmlUtils {

    private static final Log LOG = LogFactory.getLog(XmlUtils.class);

    private static final ErrorHandler DISCARD_MESSAGES_HANDLER = new ErrorHandler() {
        /**
         * Does nothing as we're not interested in this.
         */
        @Override
        public void error(final SAXParseException exception) {
            // Does nothing as we're not interested in this.
        }

        /**
         * Does nothing as we're not interested in this.
         */
        @Override
        public void fatalError(final SAXParseException exception) {
            // Does nothing as we're not interested in this.
        }

        /**
         * Does nothing as we're not interested in this.
         */
        @Override
        public void warning(final SAXParseException exception) {
            // Does nothing as we're not interested in this.
        }
    };

    /**
     * Utility class, hide constructor.
     */
    private XmlUtils() {
        // Empty.
    }

    /**
     * Builds a document from the content of the web response.
     * A warning is logged if an exception is thrown while parsing the XML content
     * (for instance when the content is not a valid XML and can't be parsed).
     *
     * @param webResponse the response from the server
     * @throws IOException if the page could not be created
     * @return the parse result
     * @throws SAXException if the parsing fails
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created
     */
    public static Document buildDocument(final WebResponse webResponse)
        throws IOException, SAXException, ParserConfigurationException {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        if (webResponse == null) {
            return factory.newDocumentBuilder().newDocument();
        }

        factory.setNamespaceAware(true);
        final InputStreamReader reader = new InputStreamReader(
                new BOMInputStream(webResponse.getContentAsStream()),
                webResponse.getContentCharset());

        // we have to do the blank input check and the parsing in one step
        final TrackBlankContentAndSkipLeadingWhitespaceReader tracker
                = new TrackBlankContentAndSkipLeadingWhitespaceReader(reader);

        final InputSource source = new InputSource(tracker);
        final DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(DISCARD_MESSAGES_HANDLER);
        builder.setEntityResolver((publicId, systemId) -> new InputSource(new StringReader("")));
        try {
            // this closes the input source/stream
            return builder.parse(source);
        }
        catch (final SAXException e) {
            if (tracker.wasBlank()) {
                return factory.newDocumentBuilder().newDocument();
            }
            throw e;
        }
    }

    /**
     * Helper for memory and performance optimization.
     */
    private static final class TrackBlankContentAndSkipLeadingWhitespaceReader extends Reader {
        private final Reader reader_;
        private boolean wasBlank_ = true;

        TrackBlankContentAndSkipLeadingWhitespaceReader(final Reader characterStream) {
            reader_ = characterStream;
        }

        public boolean wasBlank() {
            return wasBlank_;
        }

        @Override
        public void close() throws IOException {
            reader_.close();
        }

        @Override
        public int read(final char[] cbuf, final int off, final int len) throws IOException {
            int result = reader_.read(cbuf, off, len);

            if (wasBlank_ && result > -1) {
                for (int i = 0; i < result; i++) {
                    final char ch = cbuf[off + i];
                    if (!Character.isWhitespace(ch)) {
                        wasBlank_ = false;
                        if (i > 0) {
                            // skipt the leading whitespace
                            System.arraycopy(cbuf, i, cbuf, off, len - i);
                            result -= i;
                        }
                        break;
                    }
                }
            }
            return result;
        }
    }

    /**
     * Recursively appends a {@link Node} child to {@link DomNode} parent.
     *
     * @param page the owner page of {@link DomElement}s to be created
     * @param parent the parent DomNode
     * @param child the child Node
     * @param handleXHTMLAsHTML if true elements from the XHTML namespace are handled as HTML elements instead of
     *     DOM elements
     */
    public static void appendChild(final SgmlPage page, final DomNode parent, final Node child,
        final boolean handleXHTMLAsHTML) {
        appendChild(page, parent, child, handleXHTMLAsHTML, null);
    }

    /**
     * Recursively appends a {@link Node} child to {@link DomNode} parent.
     *
     * @param page the owner page of {@link DomElement}s to be created
     * @param parent the parent DomNode
     * @param child the child Node
     * @param handleXHTMLAsHTML if true elements from the XHTML namespace are handled as HTML elements instead of
     *     DOM elements
     * @param attributesOrderMap (optional) the one returned by {@link #getAttributesOrderMap(Document)}
     */
    public static void appendChild(final SgmlPage page, final DomNode parent, final Node child,
        final boolean handleXHTMLAsHTML, final Map<Integer, List<String>> attributesOrderMap) {
        final DocumentType documentType = child.getOwnerDocument().getDoctype();
        if (documentType != null && page instanceof XmlPage) {
            final DomDocumentType domDoctype = new DomDocumentType(
                    page, documentType.getName(), documentType.getPublicId(), documentType.getSystemId());
            ((XmlPage) page).setDocumentType(domDoctype);
        }
        final DomNode childXml = createFrom(page, child, handleXHTMLAsHTML, attributesOrderMap);
        parent.appendChild(childXml);
        copy(page, child, childXml, handleXHTMLAsHTML, attributesOrderMap);
    }

    private static DomNode createFrom(final SgmlPage page, final Node source, final boolean handleXHTMLAsHTML,
            final Map<Integer, List<String>> attributesOrderMap) {
        if (source.getNodeType() == Node.TEXT_NODE) {
            return new DomText(page, source.getNodeValue());
        }
        if (source.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
            return new DomProcessingInstruction(page, source.getNodeName(), source.getNodeValue());
        }
        if (source.getNodeType() == Node.COMMENT_NODE) {
            return new DomComment(page, source.getNodeValue());
        }
        if (source.getNodeType() == Node.DOCUMENT_TYPE_NODE) {
            final DocumentType documentType = (DocumentType) source;
            return new DomDocumentType(page, documentType.getName(), documentType.getPublicId(),
                    documentType.getSystemId());
        }
        final String ns = source.getNamespaceURI();
        String localName = source.getLocalName();
        if (handleXHTMLAsHTML && Html.XHTML_NAMESPACE.equals(ns)) {
            final ElementFactory factory = page.getWebClient().getPageCreator().getHtmlParser().getFactory(localName);
            return factory.createElementNS(page, ns, localName,
                    namedNodeMapToSaxAttributes(source.getAttributes(), attributesOrderMap, source));
        }
        final NamedNodeMap nodeAttributes = source.getAttributes();
        if (page != null && page.isHtmlPage()) {
            localName = localName.toUpperCase(Locale.ROOT);
        }
        final String qualifiedName;
        if (source.getPrefix() == null) {
            qualifiedName = localName;
        }
        else {
            qualifiedName = source.getPrefix() + ':' + localName;
        }

        final String namespaceURI = source.getNamespaceURI();
        if (Html.SVG_NAMESPACE.equals(namespaceURI)) {
            return page.getWebClient().getPageCreator().getHtmlParser().getSvgFactory()
                    .createElementNS(page, namespaceURI, qualifiedName,
                            namedNodeMapToSaxAttributes(nodeAttributes, attributesOrderMap, source));
        }

        final OrderedFastHashMap<String, DomAttr> attributes = new OrderedFastHashMap<>();
        for (int i = 0; i < nodeAttributes.getLength(); i++) {
            final int orderedIndex = Platform.getIndex(nodeAttributes, attributesOrderMap, source, i);
            final Attr attribute = (Attr) nodeAttributes.item(orderedIndex);
            final String attributeNamespaceURI = attribute.getNamespaceURI();
            final String attributeQualifiedName;
            if (attribute.getPrefix() == null) {
                attributeQualifiedName = attribute.getLocalName();
            }
            else {
                attributeQualifiedName = attribute.getPrefix() + ':' + attribute.getLocalName();
            }
            final String value = attribute.getNodeValue();
            final boolean specified = attribute.getSpecified();
            final DomAttr xmlAttribute =
                    new DomAttr(page, attributeNamespaceURI, attributeQualifiedName, value, specified);
            attributes.put(attribute.getNodeName(), xmlAttribute);
        }
        return new DomElement(namespaceURI, qualifiedName, page, attributes);
    }

    private static Attributes namedNodeMapToSaxAttributes(final NamedNodeMap attributesMap,
            final Map<Integer, List<String>> attributesOrderMap, final Node element) {
        final AttributesImpl attributes = new AttributesImpl();
        final int length = attributesMap.getLength();
        for (int i = 0; i < length; i++) {
            final int orderedIndex = Platform.getIndex(attributesMap, attributesOrderMap, element, i);
            final Node attr = attributesMap.item(orderedIndex);
            attributes.addAttribute(attr.getNamespaceURI(), attr.getLocalName(),
                attr.getNodeName(), null, attr.getNodeValue());
        }

        return attributes;
    }

    /**
     * Copy all children from 'source' to 'dest', within the context of the specified page.
     * @param page the page which the nodes belong to
     * @param source the node to copy from
     * @param dest the node to copy to
     * @param handleXHTMLAsHTML if true elements from the XHTML namespace are handled as HTML elements instead of
     *     DOM elements
     */
    private static void copy(final SgmlPage page, final Node source, final DomNode dest,
        final boolean handleXHTMLAsHTML, final Map<Integer, List<String>> attributesOrderMap) {
        final NodeList nodeChildren = source.getChildNodes();
        for (int i = 0; i < nodeChildren.getLength(); i++) {
            final Node child = nodeChildren.item(i);
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    final DomNode childXml = createFrom(page, child, handleXHTMLAsHTML, attributesOrderMap);
                    dest.appendChild(childXml);
                    copy(page, child, childXml, handleXHTMLAsHTML, attributesOrderMap);
                    break;

                case Node.TEXT_NODE:
                    dest.appendChild(new DomText(page, child.getNodeValue()));
                    break;

                case Node.CDATA_SECTION_NODE:
                    dest.appendChild(new DomCDataSection(page, child.getNodeValue()));
                    break;

                case Node.COMMENT_NODE:
                    dest.appendChild(new DomComment(page, child.getNodeValue()));
                    break;

                case Node.PROCESSING_INSTRUCTION_NODE:
                    dest.appendChild(new DomProcessingInstruction(page, child.getNodeName(), child.getNodeValue()));
                    break;

                default:
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("NodeType " + child.getNodeType()
                            + " (" + child.getNodeName() + ") is not yet supported.");
                    }
            }
        }
    }

    /**
     * Search for the namespace URI of the given prefix, starting from the specified element.
     * The default namespace can be searched for by specifying "" as the prefix.
     * @param element the element to start searching from
     * @param prefix the namespace prefix
     * @return the namespace URI bound to the prefix; or null if there is no such namespace
     */
    public static String lookupNamespaceURI(final DomElement element, final String prefix) {
        String uri;
        if (prefix.isEmpty()) {
            uri = element.getAttributeDirect("xmlns");
        }
        else {
            uri = element.getAttribute("xmlns:" + prefix);
        }
        if (ATTRIBUTE_NOT_DEFINED == uri) {
            final DomNode parentNode = element.getParentNode();
            if (parentNode instanceof DomElement) {
                uri = lookupNamespaceURI((DomElement) parentNode, prefix);
            }
        }
        return uri;
    }

    /**
     * Search for the prefix associated with specified namespace URI.
     * @param element the element to start searching from
     * @param namespace the namespace prefix
     * @return the prefix bound to the namespace URI; or null if there is no such namespace
     */
    public static String lookupPrefix(final DomElement element, final String namespace) {
        final Map<String, DomAttr> attributes = element.getAttributesMap();
        for (final Map.Entry<String, DomAttr> entry : attributes.entrySet()) {
            final String name = entry.getKey();
            final DomAttr value = entry.getValue();
            if (name.startsWith("xmlns:") && value.getValue().equals(namespace)) {
                return name.substring(6);
            }
        }
        for (final DomNode child : element.getChildren()) {
            if (child instanceof DomElement) {
                final String prefix = lookupPrefix((DomElement) child, namespace);
                if (prefix != null) {
                    return prefix;
                }
            }
        }
        return null;
    }

    /**
     * Returns internal Xerces details about all elements in the specified document.
     * The id of the returned {@link Map} is the {@code nodeIndex} of an element, and the list
     * is the array of ordered attributes names.
     * @param document the document
     * @return the map of an element index with its ordered attribute names
     */
    public static Map<Integer, List<String>> getAttributesOrderMap(final Document document) {
        return Platform.getAttributesOrderMap(document);
    }
}
