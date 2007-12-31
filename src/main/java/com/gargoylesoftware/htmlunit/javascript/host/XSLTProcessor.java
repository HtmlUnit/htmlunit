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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.mozilla.javascript.Context;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;

/**
 * A JavaScript object for XSLTProcessor.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XSLTProcessor extends SimpleScriptable {

    private static final long serialVersionUID = -5870183094839129375L;

    private NodeImpl style_;
    private NodeImpl input_;
    private Object output_;
    private Map/*String,Object*/ parameters_ = new HashMap();
    
    /**
     * Javascript constructor.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * Import the stylesheet into this XSLTProcessor for transformations.
     *
     * @param style The root-node of a XSLT stylesheet. This can be either a document node or an element node.
     *              If a document node then the document can contain either a XSLT stylesheet or a LRE stylesheet.
     *              If the argument is an element node it must be the xsl:stylesheet (or xsl:transform) element
     *              of an XSLT stylesheet.
     */
    public void jsxFunction_importStylesheet(final NodeImpl style) {
        style_ = style;
    }

    /**
     * Transforms the node source applying the stylesheet given by the importStylesheet() function.
     * The owner document of the output node owns the returned document fragment.
     *
     * @param source The node to be transformed.
     * @return The result of the transformation.
     */
    public XMLDocument jsxFunction_transformToDocument(final NodeImpl source) {
        final XMLDocument doc = new XMLDocument();
        doc.setPrototype(getPrototype(doc.getClass()));
        doc.setParentScope(getParentScope());

        final org.w3c.dom.Node transformedDoc = (org.w3c.dom.Node) transform(source);
        final XmlPage page = new XmlPage(transformedDoc.getFirstChild(), getWindow().getWebWindow());
        doc.setDomNode(page);
        return doc;
    }

    /**
     * @return {@link Node} or {@link String}.
     */
    private Object transform(final NodeImpl source) {
        try {
            Source xmlSource = new StreamSource(new StringReader(((XMLDocument) source).jsxGet_xml()));
            final Source xsltSource = new StreamSource(new StringReader(((XMLDocument) style_).jsxGet_xml()));

            final org.w3c.dom.Document containerDocument =
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final org.w3c.dom.Element containerElement = containerDocument.createElement("container");
            containerDocument.appendChild(containerElement);

            final DOMResult result = new DOMResult(containerElement);
 
            final Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
            for (final Iterator keys = parameters_.keySet().iterator(); keys.hasNext();) {
                final String qualifiedName = (String) keys.next();
                transformer.setParameter(qualifiedName, parameters_.get(qualifiedName));
            }
            transformer.transform(xmlSource, result);
            
            final Node transformedNode = (Node) result.getNode();
            if (transformedNode.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
                return transformedNode;
            }
            else {
                //output is not DOM (text)
                xmlSource = new StreamSource(new StringReader(((XMLDocument) source).jsxGet_xml()));
                final StringWriter writer = new StringWriter();
                final Result streamResult = new StreamResult(writer);
                transformer.transform(xmlSource, streamResult);
                return writer.toString();
            }
        }
        catch (final Exception e) {
            throw Context.reportRuntimeError("Exception: " + e);
        }
    }
    /**
     * Transforms the node source applying the stylesheet given by the importStylesheet() function.
     * The owner document of the output node owns the returned document fragment.
     * @param source The node to be transformed.
     * @param output This document is used to generate the output.
     * @return The result of the transformation.
     */
    public DocumentFragment jsxFunction_transformToFragment(final NodeImpl source, final Object output) {
        final SgmlPage page = (SgmlPage) ((Document) output).getDomNodeOrDie();

        final DomDocumentFragment fragment = page.createDomDocumentFragment();
        final DocumentFragment rv = new DocumentFragment();
        rv.setPrototype(getPrototype(rv.getClass()));
        rv.setParentScope(getParentScope());
        rv.setDomNode(fragment);

        transform(page, source, fragment);
        return rv;
    }

    private void transform(final SgmlPage page, final NodeImpl source, final DomNode parent) {
        final Object result = transform(source);
        if (result instanceof Node) {
            final NodeList children = (NodeList) ((Node) result).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                XmlUtil.appendChild(page, parent, children.item(i));
            }
        }
        else {
            final DomText text = new DomText(page, (String) result);
            parent.appendDomChild(text);
        }
    }

    /**
     * Sets a parameter to be used in subsequent transformations with this nsIXSLTProcessor.
     * If the parameter doesn't exist in the stylesheet the parameter will be ignored.
     * @param namespaceURI The namespaceURI of the XSLT parameter.
     * @param localName The local name of the XSLT parameter.
     * @param value The new value of the XSLT parameter.
     * @return
     */
    public void jsxFunction_setParameter(final String namespaceURI, final String localName, final Object value) {
        parameters_.put(getQualifiedName(namespaceURI, localName), value);
    }

    /**
     * Gets a parameter if previously set by setParameter. Returns null otherwise.
     * @param namespaceURI The namespaceURI of the XSLT parameter.
     * @param localName The local name of the XSLT parameter.
     * @return The value of the XSLT parameter.
     */
    public Object jsxFunction_getParameter(final String namespaceURI, final String localName) {
        return parameters_.get(getQualifiedName(namespaceURI, localName));
    }

    private String getQualifiedName(final String namespaceURI, final String localName) {
        final String qualifiedName;
        if (namespaceURI != null && namespaceURI.length() != 0 && !namespaceURI.equals("null")) {
            qualifiedName = '{' + namespaceURI + '}' + localName;
        }
        else {
            qualifiedName = localName;
        }
        return qualifiedName;
    }

    /**
     * Specifies which XML input tree to transform.
     * @param input the input tree.
     */
    public void jsxSet_input(final NodeImpl input) {
        input_ = input;
    }

    /**
     * Returns which XML input tree to transform.
     * @return which XML input tree to transform.
     */
    public NodeImpl jsxGet_input() {
        return input_;
    }

    /**
     * Sets the object to which to write the output of the transformation.
     * @param output The object to which to write the output of the transformation.
     */
    public void jsxSet_output(final Object output) {
        output_ = output;
    }

    /**
     * Gets a custom output to write the result of the transformation.
     * @return the output of the transformation.
     */
    public Object jsxGet_output() {
        return output_;
    }
    
    /**
     * Adds parameters into an XSL Transformations (XSLT) style sheet.
     *
     * @param baseName The name that will be used inside the style sheet to identify the parameter context.
     * @param parameter The parameter value.
     *        To remove a parameter previously added to the processor, provide a value of Empty or Null instead.
     * @param namespaceURI An optional namespace.
     */
    public void jsxFunction_addParameter(final String baseName, final Object parameter, final Object namespaceURI) {
        final String nsString;
        if (namespaceURI instanceof String) {
            nsString = (String) namespaceURI;
        }
        else {
            nsString = null;
        }
        jsxFunction_setParameter(nsString, baseName, parameter);
    }
    
    /**
     * Starts the transformation process or resumes a previously failed transformation.
     */
    public void jsxFunction_transform() {
        final SgmlPage page = (SgmlPage) ((Document) input_).getDomNodeOrDie();
        if (output_ == null) {
            final DomDocumentFragment fragment = page.createDomDocumentFragment();
            final DocumentFragment node = new DocumentFragment();
            node.setParentScope(getParentScope());
            node.setPrototype(getPrototype(node.getClass()));
            node.setDomNode(fragment);
            output_ = (NodeImpl) fragment.getScriptObject();
        }
        transform(page, input_, ((NodeImpl) output_).getDomNodeOrDie());
        final XMLSerializer serializer = new XMLSerializer();
        serializer.setParentScope(getParentScope());
        String output = "";
        for (final Iterator it = ((NodeImpl) output_).getDomNodeOrDie().getChildIterator(); it.hasNext();) {
            final DomNode child = (DomNode) it.next();
            if (child instanceof DomText) {
                //IE: XmlPage ignores all empty text nodes (if 'xml:space' is 'default')
                //Maybe this should be changed for 'xml:space' = preserve
                //See XMLDocumentTest.testLoadXML_XMLSpaceAttribute()
                if (((DomText) child).getData().trim().length() != 0) {
                    output += ((DomText) child).getData();
                }
            }
            else {
                //remove trailing "\r\n"
                final String serializedString =
                    serializer.jsxFunction_serializeToString((NodeImpl) child.getScriptObject());
                output += serializedString.substring(0, serializedString.length() - 2);
            }
        }
        output_ = output;
    }
}
