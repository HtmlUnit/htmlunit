/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomDocumentType;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * A basic class to be implemented by {@link com.gargoylesoftware.htmlunit.html.HtmlPage} and
 * {@link com.gargoylesoftware.htmlunit.xml.XmlPage}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public abstract class SgmlPage extends DomNode implements Page, Document {

    private static final long serialVersionUID = -8803248938782701094L;
    private DomDocumentType documentType_;
    private final WebResponse webResponse_;
    private WebWindow enclosingWindow_;
    private final WebClient webClient_;

    /**
     * Creates an instance of SgmlPage.
     *
     * @param webResponse the web response that was used to create this page
     * @param webWindow the window that this page is being loaded into
     */
    public SgmlPage(final WebResponse webResponse, final WebWindow webWindow) {
        super(null);
        webResponse_ = webResponse;
        enclosingWindow_ = webWindow;
        webClient_ = webWindow.getWebClient();
    }

    /**
     * {@inheritDoc}
     */
    public void cleanUp() throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    public WebResponse getWebResponse() {
        return webResponse_;
    }

    /**
     * {@inheritDoc}
     */
    public void initialize() throws IOException {
    }

    /**
     * Gets the name for the current node.
     * @return the node name
     */
    @Override
    public String getNodeName() {
        return "#document";
    }

    /**
     * Gets the type of the current node.
     * @return the node type
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.DOCUMENT_NODE;
    }

    /**
     * Returns the window that this page is sitting inside.
     *
     * @return the enclosing frame or null if this page isn't inside a frame
     */
    public WebWindow getEnclosingWindow() {
        return enclosingWindow_;
    }

    /**
     * Sets the window that contains this page.
     *
     * @param window the new frame or null if this page is being removed from a frame
     */
    public void setEnclosingWindow(final WebWindow window) {
        enclosingWindow_ = window;
    }

    /**
     * Returns the WebClient that originally loaded this page.
     *
     * @return the WebClient that originally loaded this page
     */
    public WebClient getWebClient() {
        return webClient_;
    }

    /**
     * Creates an empty {@link DomDocumentFragment} object.
     * @return a newly created {@link DomDocumentFragment}
     */
    public DomDocumentFragment createDomDocumentFragment() {
        return new DomDocumentFragment(this);
    }

    /**
     * Returns the document type.
     * @return the document type
     */
    public final DocumentType getDoctype() {
        return documentType_;
    }

    /**
     * Sets the document type.
     * @param type the document type
     */
    protected void setDocumentType(final DomDocumentType type) {
        documentType_ = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SgmlPage getPage() {
        return this;
    }

    /**
     * Creates an element, the type of which depends on the specified tag name.
     * @param tagName the tag name which determines the type of element to be created
     * @return an element, the type of which depends on the specified tag name
     */
    public abstract Element createElement(final String tagName);

    /**
     * Create a new Element with the given namespace and qualified name.
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @return the new element
     */
    public abstract Element createElementNS(final String namespaceURI, final String qualifiedName);

    /**
     * Returns the page encoding.
     * @return the page encoding
     */
    public abstract String getPageEncoding();

    /**
     * Returns the document element.
     * @return the document element
     */
    public DomElement getDocumentElement() {
        DomNode childNode = getFirstChild();
        while (childNode != null && !(childNode instanceof DomElement)) {
            childNode = childNode.getNextSibling();
        }
        return (DomElement) childNode;
    }

    /**
     * Creates a clone of this instance.
     * @return a clone of this instance
     */
    @Override
    protected SgmlPage clone() {
        try {
            final SgmlPage result = (SgmlPage) super.clone();
            return result;
        }
        catch (final CloneNotSupportedException e) {
            throw new IllegalStateException("Clone not supported");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String asXml() {
        return getDocumentElement().asXml();
    }

    /**
     * Returns <tt>true</tt> if this page has case-sensitive tag names, <tt>false</tt> otherwise. In general,
     * XML has case-sensitive tag names, and HTML doesn't. This is especially important during XPath matching.
     * @return <tt>true</tt> if this page has case-sensitive tag names, <tt>false</tt> otherwise
     */
    public abstract boolean hasCaseSensitiveTagNames();

    /**
     * {@inheritDoc}
     * The current implementation just {@link DomNode#normalize()}s the document element.
     */
    public void normalizeDocument() {
        getDocumentElement().normalize();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCanonicalXPath() {
        return "/";
    }

    /**
     * {@inheritDoc}
     */
    public DomAttr createAttribute(final String name) {
        return new DomAttr(getPage(), null, name, "");
    }

}
