/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.xml;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.ElementFactory;
import com.gargoylesoftware.htmlunit.html.HTMLParser;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 *
 * Provides facility method to work with XML responses.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ronald Brill
 */
public final class XmlUtil {

    /**
     * Default encoding used.
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    private static final Log LOG = LogFactory.getLog(XmlUtil.class);

    private static final ErrorHandler DISCARD_MESSAGES_HANDLER = new ErrorHandler() {
        /**
         * Does nothing as we're not interested in this.
         */
        public void error(final SAXParseException exception) {
            // Does nothing as we're not interested in this.
        }
        /**
         * Does nothing as we're not interested in this.
         */
        public void fatalError(final SAXParseException exception) {
            // Does nothing as we're not interested in this.
        }
        /**
         * Does nothing as we're not interested in this.
         */
        public void warning(final SAXParseException exception) {
            // Does nothing as we're not interested in this.
        }
    };

    /**
     * Utility class, hide constructor.
     */
    private XmlUtil() {
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
        final InputStreamReader reader = new InputStreamReader(webResponse.getContentAsStream(),
                webResponse.getContentCharset());

        // we have to do the blank input check and the parsing in one step
        final TrackBlankContentReader tracker = new TrackBlankContentReader(reader);

        final InputSource source = new InputSource(tracker);
        final DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(DISCARD_MESSAGES_HANDLER);
        builder.setEntityResolver(new EntityResolver() {
            public InputSource resolveEntity(final String publicId, final String systemId)
                throws SAXException, IOException {
                return new InputSource(new StringReader(""));
            }
        });
        try {
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
    private static final class TrackBlankContentReader extends Reader {
        private Reader reader_;
        private boolean wasBlank_ = true;

        public TrackBlankContentReader(final Reader characterStream) {
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
            final int result = reader_.read(cbuf, off, len);

            if (wasBlank_ && result > -1) {
                for (int i = 0; i < result; i++) {
                    final char ch = cbuf[off + i];
                    if (!Character.isWhitespace(ch)) {
                        wasBlank_ = false;
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
     */
    public static void appendChild(final SgmlPage page, final DomNode parent, final Node child) {
        final DocumentType documentType = child.getOwnerDocument().getDoctype();
        if (documentType != null && page instanceof XmlPage) {
            final DomDocumentType domDoctype = new DomDocumentType(
                    page, documentType.getName(), documentType.getPublicId(), documentType.getSystemId());
            ((XmlPage) page).setDocumentType(domDoctype);
        }
        final DomNode childXml = createFrom(page, child);
        parent.appendChild(childXml);
        copy(page, child, childXml);
    }

    private static DomNode createFrom(final SgmlPage page, final Node source) {
        if (source.getNodeType() == Node.TEXT_NODE) {
            return new DomText(page, source.getNodeValue());
        }
        final String ns = source.getNamespaceURI();
        String localName = source.getLocalName();
        if (HTMLParser.XHTML_NAMESPACE.equals(ns)) {
            final ElementFactory factory = HTMLParser.getFactory(localName);
            return factory.createElementNS(page, ns, localName, namedNodeMapToSaxAttributes(source.getAttributes()));
        }
        final Map<String, DomAttr> attributes = new HashMap<String, DomAttr>();
        final NamedNodeMap nodeAttributes = source.getAttributes();
        for (int i = 0; i < nodeAttributes.getLength(); i++) {
            final Attr attribute = (Attr) nodeAttributes.item(i);
            final String namespaceURI = attribute.getNamespaceURI();
            final String qualifiedName;
            if (attribute.getPrefix() != null) {
                qualifiedName = attribute.getPrefix() + ':' + attribute.getLocalName();
            }
            else {
                qualifiedName = attribute.getLocalName();
            }
            final String value = attribute.getNodeValue();
            final boolean specified = attribute.getSpecified();
            final DomAttr xmlAttribute = new DomAttr(page, namespaceURI, qualifiedName, value, specified);
            attributes.put(attribute.getNodeName(), xmlAttribute);
        }
        if (page != null && page.isHtmlPage()) {
            localName = localName.toUpperCase(Locale.ENGLISH);
        }
        final String qualifiedName;
        if (source.getPrefix() == null) {
            qualifiedName = localName;
        }
        else {
            qualifiedName = source.getPrefix() + ':' + localName;
        }
        return new DomElement(source.getNamespaceURI(), qualifiedName, page, attributes);
    }

    private static Attributes namedNodeMapToSaxAttributes(final NamedNodeMap attributesMap) {
        final AttributesImpl attributes = new AttributesImpl();
        final int length = attributesMap.getLength();
        for (int i = 0; i < length; ++i) {
            final Node attr = attributesMap.item(i);
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
     */
    private static void copy(final SgmlPage page, final Node source, final DomNode dest) {
        final NodeList nodeChildren = source.getChildNodes();
        for (int i = 0; i < nodeChildren.getLength(); i++) {
            final Node child = nodeChildren.item(i);
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    final DomNode childXml = createFrom(page, child);
                    dest.appendChild(childXml);
                    copy(page, child, childXml);
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
                    LOG.warn("NodeType " + child.getNodeType()
                        + " (" + child.getNodeName() + ") is not yet supported.");
            }
        }
    }

    /**
     * Search for the namespace URI of the given prefix, starting from the specified element.
     * @param element the element to start searching from
     * @param prefix the namespace prefix
     * @return the namespace URI bound to the prefix; or null if there is no such namespace
     */
    public static String lookupNamespaceURI(final DomElement element, final String prefix) {
        String uri = element.getAttribute("xmlns:" + prefix);
        if (uri == DomElement.ATTRIBUTE_NOT_DEFINED) {
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
}
