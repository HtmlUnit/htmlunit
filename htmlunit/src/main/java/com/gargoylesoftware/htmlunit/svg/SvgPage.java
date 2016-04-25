/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.svg;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.InteractivePage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomProcessingInstruction;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;

/**
 * A representation of a Scalable Vector Graphics (SVG) page returned from a server.
 *
 * @author Ahmed Ashour
 */
public class SvgPage extends InteractivePage {

    private Node node_;

    /**
     * Creates an instance.
     *
     * @param webResponse the response from the server
     * @param node the node to initialize this page with
     * @param enclosingWindow the window that holds the page
     */
    public SvgPage(final WebResponse webResponse, final Node node, final WebWindow enclosingWindow) {
        super(webResponse, enclosingWindow);
        node_ = node;
        if (node_ != null) {
            XmlUtil.appendChild(this, this, node_, true);
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
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Node adoptNode(final Node source) {
        throw new UnsupportedOperationException("SvgPage.adoptNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Attr createAttributeNS(final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("SvgPage.createAttributeNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Element createElement(final String tagName) {
        return createElementNS(HTMLParser.SVG_NAMESPACE, tagName);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Element createElementNS(final String namespaceURI, final String qualifiedName) {
        return HTMLParser.SVG_FACTORY.createElementNS(this, null, qualifiedName, null, true);
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public EntityReference createEntityReference(final String name) {
        throw new UnsupportedOperationException("SvgPage.createEntityReference is not yet implemented.");
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
        throw new UnsupportedOperationException("SvgPage.getDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public DOMConfiguration getDomConfig() {
        throw new UnsupportedOperationException("SvgPage.getDomConfig is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomElement getElementById(final String elementId) {
        return getFirstByXPath("//*[@id='" + elementId + "']");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public NodeList getElementsByTagNameNS(final String namespace, final String name) {
        throw new UnsupportedOperationException("SvgPage.getElementsByTagNameNS is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public DOMImplementation getImplementation() {
        throw new UnsupportedOperationException("SvgPage.getImplementation is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String getInputEncoding() {
        throw new UnsupportedOperationException("SvgPage.getInputEncoding is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public boolean getStrictErrorChecking() {
        throw new UnsupportedOperationException("SvgPage.getStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String getXmlEncoding() {
        throw new UnsupportedOperationException("SvgPage.getXmlEncoding is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public boolean getXmlStandalone() {
        throw new UnsupportedOperationException("SvgPage.getXmlStandalone is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String getXmlVersion() {
        throw new UnsupportedOperationException("SvgPage.getXmlVersion is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Node importNode(final Node importedNode, final boolean deep) {
        throw new UnsupportedOperationException("SvgPage.importNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public Node renameNode(final Node n, final String namespaceURI, final String qualifiedName) {
        throw new UnsupportedOperationException("SvgPage.renameNode is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setDocumentURI(final String documentURI) {
        throw new UnsupportedOperationException("SvgPage.setDocumentURI is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setStrictErrorChecking(final boolean strictErrorChecking) {
        throw new UnsupportedOperationException("SvgPage.setStrictErrorChecking is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setXmlStandalone(final boolean xmlStandalone) {
        throw new UnsupportedOperationException("SvgPage.setXmlStandalone is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public void setXmlVersion(final String xmlVersion) {
        throw new UnsupportedOperationException("SvgPage.setXmlVersion is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    @Override
    public String getPageEncoding() {
        throw new UnsupportedOperationException("SvgPage.getPageEncoding is not yet implemented.");
    }
}
