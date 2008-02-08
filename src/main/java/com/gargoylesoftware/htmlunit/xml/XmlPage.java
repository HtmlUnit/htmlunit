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
package com.gargoylesoftware.htmlunit.xml;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * A page that will be returned for response with content type "text/xml".
 * It doesn't implement itself {@link org.w3c.dom.Document} to allow to see the source of badly formed
 * xml responses.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public class XmlPage extends SgmlPage {
    private static final long serialVersionUID = -1430136241030261308L;
    private Node node_;

    /**
     * Create an instance.
     * A warning is logged if an exception is thrown while parsing the xml content
     * (for instance when the content is not a valid xml and can't be parsed).
     *
     * @param webResponse The response from the server
     * @param enclosingWindow The window that holds the page.
     * @throws IOException If the page could not be created
     */
    public XmlPage(final WebResponse webResponse, final WebWindow enclosingWindow) throws IOException {
        this(webResponse, enclosingWindow, true);
    }

    /**
     * Create an instance.
     * A warning is logged if an exception is thrown while parsing the xml content
     * (for instance when the content is not a valid xml and can't be parsed).
     *
     * @param node The node to initialize this page with.
     * @param enclosingWindow The window that holds the page.
     */
    public XmlPage(final Node node, final WebWindow enclosingWindow) {
        super(null, enclosingWindow);
        node_ = node;
        if (node_ != null) {
            XmlUtil.appendChild(this, this, node_);
        }
    }

    /**
     * Create an instance.
     * A warning is logged if an exception is thrown while parsing the xml content
     * (for instance when the content is not a valid xml and can't be parsed).
     *
     * @param webResponse The response from the server
     * @param enclosingWindow The window that holds the page.
     * @param ignoreSAXException Whether to ignore {@link SAXException} or throw it as {@link IOException}.
     * @throws IOException If the page could not be created
     */
    public XmlPage(final WebResponse webResponse, final WebWindow enclosingWindow, final boolean ignoreSAXException)
        throws IOException {
        super(webResponse, enclosingWindow);

        try {
            if (webResponse == null || webResponse.getContentAsString().trim().length() == 0) {
                node_ =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().getDocumentElement();
            }
            else {
                node_ = XmlUtil.buildDocument(webResponse).getDocumentElement();
            }
            if (node_ != null) {
                XmlUtil.appendChild(this, this, node_);
            }
        }
        catch (final SAXException e) {
            getLog().warn("Failed parsing xml document " + webResponse.getUrl() + ": " + e.getMessage());
            if (!ignoreSAXException) {
                throw new IOException(e.getMessage());
            }
        }
        catch (final ParserConfigurationException e) {
            getLog().warn("Failed parsing xml document " + webResponse.getUrl() + ": " + e.getMessage());
        }
    }

    /**
     * Return the content of the page
     *
     * @return See above
     */
    public String getContent() {
        return getWebResponse().getContentAsString();
    }

    /**
     * Gets the DOM representation of the xml content
     * @return <code>null</code> if the content couldn't be parsed.
     */
    public Document getXmlDocument() {
        if (node_ != null) {
            return node_.getOwnerDocument();
        }
        else {
            return null;
        }
    }

    /**
     * Get the root XmlElement of this document.
     * @return The root element
     */
    //TODO: should be removed later to SgmlPage
    public XmlElement getDocumentXmlElement() {
        DomNode childNode = getFirstDomChild();
        while (childNode != null && !(childNode instanceof XmlElement)) {
            childNode = childNode.getNextDomSibling();
        }
        return (XmlElement) childNode;
    }

    /**
     * Create a new XML element with the given tag name.
     *
     * @param tagName The tag name.
     * @return the new XML element.
     */
    public XmlElement createXmlElement(final String tagName) {
        return createXmlElementNS(null, tagName);
    }

    /**
     * Create a new HtmlElement with the given namespace and qualified name.
     *
     * @param namespaceURI the URI that identifies an XML namespace.
     * @param qualifiedName The qualified name of the element type to instantiate
     * @return the new HTML element.
     */
    public XmlElement createXmlElementNS(final String namespaceURI, final String qualifiedName) {
        return new XmlElement(namespaceURI, qualifiedName, this, new HashMap<String, XmlAttr>());
    }

}
