/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.DomCData;
import com.gargoylesoftware.htmlunit.html.DomComment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 *
 * Provides facility method to work with xml responses.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public final class XmlUtil {
    private static final ErrorHandler DISCARD_MESSAGES_HANDLER = new ErrorHandler() {
        /**
         * Does nothing as we're not interested in.
         * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
         */
        public void error(final SAXParseException exception) throws SAXException {
            // Does nothing as we're not interested in.
        }
        
        /**
         * Does nothing as we're not interested in.
         * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
         */
        public void fatalError(final SAXParseException exception)
            throws SAXException {

            // Does nothing as we're not interested in.
        }

        /**
         * Does nothing as we're not interested in.
         * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
         */
        public void warning(final SAXParseException exception)
            throws SAXException {

            // Does nothing as we're not interested in.
        }
    };

    /**
     * Utility class, hide constructor
     */
    private XmlUtil() {
        // nothing
    }

    /**
     * Builds a document from the content of the webresponse.
     * A warning is logged if an exception is thrown while parsing the xml content
     * (for instance when the content is not a valid xml and can't be parsed).
     *
     * @param webResponse The response from the server
     * @throws IOException If the page could not be created
     * @return the parse result
     * @throws SAXException if the parsing fails
     * @throws ParserConfigurationException if a DocumentBuilder cannot be created
     */
    public static Document buildDocument(final WebResponse webResponse)
        throws IOException, SAXException, ParserConfigurationException {

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        final InputSource source = new InputSource(new StringReader(webResponse.getContentAsString()));
        final DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(DISCARD_MESSAGES_HANDLER);
        return builder.parse(source);
    }

    /**
     * Return the log object for this web client
     * @return The log object
     */
    protected static Log getLog() {
        return LogFactory.getLog(XmlUtil.class);
    }

    /**
     * Recursively appends a {@link Node} child to {@link DomNode} parent.
     *
     * @param page the owner page of {@link XmlElement}s to be created.
     * @param parent the parent DomNode.
     * @param child the child Node.
     */
    public static void appendChild(final Page page, final DomNode parent, final Node child) {
        final DomNode childXml = createFrom(page, child);
        parent.appendDomChild(childXml);
        copy(page, child, childXml);
    }

    private static DomNode createFrom(final Page page, final org.w3c.dom.Node source) {
        if (source.getNodeType() == Node.TEXT_NODE) {
            return new DomText(page, source.getNodeValue());
        }
        final Map attributes/* String, XmlAttr*/ = new HashMap();
        final NamedNodeMap nodeAttributes = source.getAttributes();
        for (int i = 0; i < nodeAttributes.getLength(); i++) {
            final Node attribute = nodeAttributes.item(i);
            final String qualifiedName;
            if (attribute.getPrefix() != null) {
                qualifiedName = attribute.getPrefix() + ':' + attribute.getLocalName();
            }
            else {
                qualifiedName = attribute.getLocalName();
            }
            final XmlAttr xmlAttribute =
                new XmlAttr(page, attribute.getNamespaceURI(), qualifiedName, attribute.getNodeValue());
            attributes.put(attribute.getNodeName(), xmlAttribute);
        }
        String localName = source.getLocalName();
        if (page instanceof HtmlPage) {
            localName = localName.toUpperCase();
        }
        final String qualifiedName;
        if (source.getPrefix() == null) {
            qualifiedName = localName;
        }
        else {
            qualifiedName = source.getPrefix() + ':' + localName;
        }
        return new XmlElement(source.getNamespaceURI(), qualifiedName, page, attributes);
    }

    /**
     * Copy all children from 'source' to 'dest'
     * @param source The Node to copy from.
     * @param dest The DomNode to copy to.
     */
    private static void copy(final Page page, final org.w3c.dom.Node source, final DomNode dest) {
        final NodeList nodeChildren = source.getChildNodes();
        for (int i = 0; i < nodeChildren.getLength(); i++) {
            final Node child = nodeChildren.item(i);
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    final DomNode childXml = createFrom(page, child);
                    dest.appendDomChild(childXml);
                    copy(page, child, childXml);
                    break;

                case Node.TEXT_NODE:
                    final DomText text = new DomText(page, child.getNodeValue());
                    dest.appendDomChild(text);
                    break;

                case Node.CDATA_SECTION_NODE:
                    final DomCData cdata = new DomCData(page, child.getNodeValue());
                    dest.appendDomChild(cdata);
                    break;

                case Node.COMMENT_NODE:
                    final DomComment comment = new DomComment(page, child.getNodeValue());
                    dest.appendDomChild(comment);
                    break;

                default:
                    getLog().warn("NodeType " + child.getNodeType()
                            + " (" + child.getNodeName() + ") is not yet supported.");
            }
        }
    }

    /**
     * Search for the namespace URI of the given prefix, starting from the specified element.
     * @param element The element to start searching from.
     * @param prefix The namespace prefix.
     * @return the namespace URI bound to the prefix; or null if there is no such namespace.
     * @see #lookupNamespaceURI(HtmlElement, String)
     */
    public static String lookupNamespaceURI(final XmlElement element, final String prefix) {
        String uri = element.getAttributeValue("xmlns:" + prefix);
        if (uri == XmlElement.ATTRIBUTE_NOT_DEFINED) {
            final DomNode parentNode = element.getParentDomNode();
            if (parentNode instanceof XmlElement) {
                uri = lookupNamespaceURI((XmlElement) parentNode, prefix);
            }
        }
        return uri;
    }

    /**
     * Search for the namespace URI of the given prefix, starting from the specified element.
     * @param element The element to start searching from.
     * @param prefix The namespace prefix.
     * @return the namespace URI bound to the prefix; or null if there is no such namespace.
     * @see #lookupNamespaceURI(XmlElement, String)
     */
    public static String lookupNamespaceURI(final HtmlElement element, final String prefix) {
        String uri = element.getAttributeValue("xmlns:" + prefix);
        if (uri == HtmlElement.ATTRIBUTE_NOT_DEFINED) {
            final DomNode parentNode = element.getParentDomNode();
            if (parentNode instanceof HtmlElement) {
                uri = lookupNamespaceURI((HtmlElement) parentNode, prefix);
            }
        }
        return uri;
    }
}
