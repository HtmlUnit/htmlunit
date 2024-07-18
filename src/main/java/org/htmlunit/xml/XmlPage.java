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
package org.htmlunit.xml;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.SgmlPage;
import org.htmlunit.WebResponse;
import org.htmlunit.WebWindow;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomProcessingInstruction;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.XmlUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * A page that will be returned for response with content type "text/xml".
 *
 * @author Marc Guillemot
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class XmlPage extends SgmlPage {

    private static final Log LOG = LogFactory.getLog(XmlPage.class);

    private Node node_;

    /**
     * Creates an instance.
     * A warning is logged if an exception is thrown while parsing the XML content
     * (for instance when the content is not a valid XML and can't be parsed).
     *
     * @param webResponse the response from the server
     * @param enclosingWindow the window that holds the page
     * @throws IOException if the page could not be created
     */
    public XmlPage(final WebResponse webResponse, final WebWindow enclosingWindow) throws IOException {
        this(webResponse, enclosingWindow, true);
    }

    /**
     * Creates an instance.
     * A warning is logged if an exception is thrown while parsing the XML content
     * (for instance when the content is not a valid XML and can't be parsed).
     *
     * @param node the node to initialize this page with
     * @param enclosingWindow the window that holds the page
     */
    public XmlPage(final Node node, final WebWindow enclosingWindow) {
        super(null, enclosingWindow);
        node_ = node;
        if (node_ != null) {
            XmlUtils.appendChild(this, this, node_, true);
        }
    }

    /**
     * Creates an instance.
     * A warning is logged if an exception is thrown while parsing the XML content
     * (for instance when the content is not a valid XML and can't be parsed).
     *
     * @param webResponse the response from the server
     * @param enclosingWindow the window that holds the page
     * @param ignoreSAXException Whether to ignore {@link SAXException} or throw it as {@link IOException}
     * @throws IOException if the page could not be created
     */
    public XmlPage(final WebResponse webResponse, final WebWindow enclosingWindow, final boolean ignoreSAXException)
        throws IOException {
        this(webResponse, enclosingWindow, ignoreSAXException, true);
    }

    /**
     * Creates an instance.
     * A warning is logged if an exception is thrown while parsing the XML content
     * (for instance when the content is not a valid XML and can't be parsed).
     *
     * @param webResponse the response from the server
     * @param enclosingWindow the window that holds the page
     * @param ignoreSAXException Whether to ignore {@link SAXException} or throw it as {@link IOException}
     * @param handleXHTMLAsHTML if true elements from the XHTML namespace are handled as HTML elements instead of
     *     DOM elements
     * @throws IOException if the page could not be created
     */
    public XmlPage(final WebResponse webResponse, final WebWindow enclosingWindow, final boolean ignoreSAXException,
        final boolean handleXHTMLAsHTML) throws IOException {
        super(webResponse, enclosingWindow);

        try {
            try {
                final Document document = XmlUtils.buildDocument(webResponse);
                node_ = document.getFirstChild();
            }
            catch (final SAXException e) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn("Failed parsing XML document '" + webResponse.getWebRequest().getUrl() + "'", e);
                }
                if (!ignoreSAXException) {
                    throw new IOException(e.getMessage());
                }
            }
        }
        catch (final ParserConfigurationException e) {
            if (LOG.isWarnEnabled()) {
                if (null == webResponse) {
                    LOG.warn("Failed parsing XML empty document:", e);
                }
                else {
                    LOG.warn("Failed parsing XML empty document '" + webResponse.getWebRequest().getUrl() + "'", e);
                }
            }
        }

        final Map<Integer, List<String>> attributesOrderMap;
        if (node_ != null) {
            attributesOrderMap = XmlUtils.getAttributesOrderMap(node_.getOwnerDocument());
        }
        else {
            attributesOrderMap = null;
        }
        for (Node node = node_; node != null; node = node.getNextSibling()) {
            XmlUtils.appendChild(this, this, node, handleXHTMLAsHTML, attributesOrderMap);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize() throws IOException {
        // nothing to do here
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCaseSensitiveTagNames() {
        return true;
    }

    /**
     * Returns the DOM representation of the XML content.
     * @return {@code null} if the content couldn't be parsed
     */
    public Document getXmlDocument() {
        if (node_ != null) {
            return node_.getOwnerDocument();
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Node adoptNode(final Node source) {
        throw new UnsupportedOperationException("XmlPage.adoptNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Attr createAttributeNS(final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("XmlPage.createAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement createElement(final String tagName) {
        return createElementNS(null, tagName);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement createElementNS(final String namespaceURI, final String qualifiedName) {
        return new DomElement(namespaceURI, qualifiedName, this, new HashMap<>());
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public EntityReference createEntityReference(final String name) {
        throw new UnsupportedOperationException("XmlPage.createEntityReference is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomProcessingInstruction createProcessingInstruction(final String target, final String data) {
        return new DomProcessingInstruction(this, target, data);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String getDocumentURI() {
        throw new UnsupportedOperationException("XmlPage.getDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public DOMConfiguration getDomConfig() {
        throw new UnsupportedOperationException("XmlPage.getDomConfig is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Element getElementById(final String elementId) {
        throw new UnsupportedOperationException("XmlPage.getElementById is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public DOMImplementation getImplementation() {
        throw new UnsupportedOperationException("XmlPage.getImplementation is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String getInputEncoding() {
        throw new UnsupportedOperationException("XmlPage.getInputEncoding is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public boolean getStrictErrorChecking() {
        throw new UnsupportedOperationException("XmlPage.getStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXmlEncoding() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getXmlStandalone() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getXmlVersion() {
        return "1.0";
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Node importNode(final Node importedNode, final boolean deep) {
        throw new UnsupportedOperationException("XmlPage.importNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Node renameNode(final Node n, final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("XmlPage.renameNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setDocumentURI(final String documentURI) {
        throw new UnsupportedOperationException("XmlPage.setDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setStrictErrorChecking(final boolean strictErrorChecking) {
        throw new UnsupportedOperationException("XmlPage.setStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setXmlStandalone(final boolean xmlStandalone) {
        throw new UnsupportedOperationException("XmlPage.setXmlStandalone is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setXmlVersion(final String xmlVersion) {
        throw new UnsupportedOperationException("XmlPage.setXmlVersion is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Charset getCharset() {
        return UTF_8;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentType() {
        return MimeType.APPLICATION_XML;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDocumentType(final DocumentType type) {
        super.setDocumentType(type);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNodeValue(final String value) {
        // Default behavior is to do nothing, overridden in some subclasses
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix(final String prefix) {
        // Empty.
    }
}
