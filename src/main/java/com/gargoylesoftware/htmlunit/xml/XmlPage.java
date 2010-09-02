/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomCDataSection;
import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;

/**
 * A page that will be returned for response with content type "text/xml".
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author David K. Taylor
 * @author Ahmed Ashour
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
            XmlUtil.appendChild(this, this, node_);
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
        super(webResponse, enclosingWindow);

        try {
            if (webResponse == null || webResponse.getContentAsString() == null
                    || webResponse.getContentAsString().trim().length() == 0) {
                node_ = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().getDocumentElement();
            }
            else {
                node_ = XmlUtil.buildDocument(webResponse).getDocumentElement();
            }
            if (node_ != null) {
                XmlUtil.appendChild(this, this, node_);
            }
        }
        catch (final SAXException e) {
            LOG.warn("Failed parsing XML document " + webResponse.getWebRequest().getUrl()
                    + ": " + e.getMessage());
            if (!ignoreSAXException) {
                throw new IOException(e.getMessage());
            }
        }
        catch (final ParserConfigurationException e) {
            LOG.warn("Failed parsing XML document " + webResponse.getWebRequest().getUrl()
                    + ": " + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasCaseSensitiveTagNames() {
        return true;
    }

    /**
     * Returns the content of the page.
     * @return the content of the page
     */
    public String getContent() {
        return getWebResponse().getContentAsString();
    }

    /**
     * Returns the DOM representation of the XML content.
     * @return <code>null</code> if the content couldn't be parsed
     */
    public Document getXmlDocument() {
        if (node_ != null) {
            return node_.getOwnerDocument();
        }
        return null;
    }

    /**
     * Creates a new XML element with the given tag name.
     *
     * @param tagName the tag name
     * @return the new XML element
     */
    public DomElement createXmlElement(final String tagName) {
        return createXmlElementNS(null, tagName);
    }

    /**
     * Creates a new XML element with the given namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @return the new XML element
     */
    public DomElement createXmlElementNS(final String namespaceURI, final String qualifiedName) {
        return new DomElement(namespaceURI, qualifiedName, this, new HashMap<String, DomAttr>());
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Node adoptNode(final Node source) {
        throw new UnsupportedOperationException("XmlPage.adoptNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Attr createAttributeNS(final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("XmlPage.createAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public DomCDataSection createCDATASection(final String data) {
        return new DomCDataSection(this, data);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Comment createComment(final String data) {
        throw new UnsupportedOperationException("XmlPage.createComment is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public DocumentFragment createDocumentFragment() {
        throw new UnsupportedOperationException("XmlPage.createDocumentFragment is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element createElement(final String tagName) {
        return createXmlElement(tagName);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Element createElementNS(final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("XmlPage.createElementNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public EntityReference createEntityReference(final String name) {
        throw new UnsupportedOperationException("XmlPage.createEntityReference is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    public DomProcessingInstruction createProcessingInstruction(final String target, final String data) {
        return new DomProcessingInstruction(this, target, data);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Text createTextNode(final String data) {
        throw new UnsupportedOperationException("XmlPage.createTextNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getDocumentURI() {
        throw new UnsupportedOperationException("XmlPage.getDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public DOMConfiguration getDomConfig() {
        throw new UnsupportedOperationException("XmlPage.getDomConfig is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Element getElementById(final String elementId) {
        throw new UnsupportedOperationException("XmlPage.getElementById is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public NodeList getElementsByTagName(final String tagname) {
        throw new UnsupportedOperationException("XmlPage.getElementsByTagName is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public NodeList getElementsByTagNameNS(final String namespace, final String name) {
        throw new UnsupportedOperationException("XmlPage.getElementsByTagNameNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public DOMImplementation getImplementation() {
        throw new UnsupportedOperationException("XmlPage.getImplementation is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getInputEncoding() {
        throw new UnsupportedOperationException("XmlPage.getInputEncoding is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean getStrictErrorChecking() {
        throw new UnsupportedOperationException("XmlPage.getStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getXmlEncoding() {
        throw new UnsupportedOperationException("XmlPage.getXmlEncoding is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean getXmlStandalone() {
        throw new UnsupportedOperationException("XmlPage.getXmlStandalone is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getXmlVersion() {
        throw new UnsupportedOperationException("XmlPage.getXmlVersion is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Node importNode(final Node importedNode, final boolean deep) {
        throw new UnsupportedOperationException("XmlPage.importNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Node renameNode(final Node n, final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("XmlPage.renameNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setDocumentURI(final String documentURI) {
        throw new UnsupportedOperationException("XmlPage.setDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setStrictErrorChecking(final boolean strictErrorChecking) {
        throw new UnsupportedOperationException("XmlPage.setStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setXmlStandalone(final boolean xmlStandalone) {
        throw new UnsupportedOperationException("XmlPage.setXmlStandalone is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public void setXmlVersion(final String xmlVersion) {
        throw new UnsupportedOperationException("XmlPage.setXmlVersion is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String getPageEncoding() {
        throw new UnsupportedOperationException("XmlPage.getPageEncoding is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setDocumentType(final DomDocumentType type) {
        super.setDocumentType(type);
    }
}
