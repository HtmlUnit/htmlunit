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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLSerializer;
import com.gargoylesoftware.htmlunit.xml.XmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;

/**
 * A JavaScript object for XSLTProcessor.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XSLTProcessor extends SimpleScriptable {

    private Node style_;
    private Node input_;
    private Object output_;
    private Map<String, Object> parameters_ = new HashMap<String, Object>();

    /**
     * JavaScript constructor.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * Imports the specified stylesheet into this XSLTProcessor for transformations. The specified node
     * may be either a document node or an element node. If it is a document node, then the document can
     * contain either a XSLT stylesheet or a LRE stylesheet. If it is an element node, it must be the
     * xsl:stylesheet (or xsl:transform) element of an XSLT stylesheet.
     *
     * @param style the root-node of an XSLT stylesheet (may be a document node or an element node)
     */
    public void jsxFunction_importStylesheet(final Node style) {
        style_ = style;
    }

    /**
     * Transforms the node source applying the stylesheet given by the importStylesheet() function.
     * The owner document of the output node owns the returned document fragment.
     *
     * @param source the node to be transformed
     * @return the result of the transformation
     */
    public XMLDocument jsxFunction_transformToDocument(
            final Node source) {
        final XMLDocument doc = new XMLDocument();
        doc.setPrototype(getPrototype(doc.getClass()));
        doc.setParentScope(getParentScope());

        final Object transformResult = transform(source);
        final org.w3c.dom.Node node;
        if (transformResult instanceof org.w3c.dom.Node) {
            final org.w3c.dom.Node transformedDoc = (org.w3c.dom.Node) transformResult;
            node = transformedDoc.getFirstChild();
        }
        else {
            node = null;
        }
        final XmlPage page = new XmlPage(node, getWindow().getWebWindow());
        doc.setDomNode(page);
        return doc;
    }

    /**
     * @return {@link Node} or {@link String}
     */
    private Object transform(final Node source) {
        try {
            Source xmlSource = new DOMSource(source.getDomNodeOrDie());
            final Source xsltSource = new DOMSource(style_.getDomNodeOrDie());

            final org.w3c.dom.Document containerDocument =
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final org.w3c.dom.Element containerElement = containerDocument.createElement("container");
            containerDocument.appendChild(containerElement);

            final DOMResult result = new DOMResult(containerElement);

            final Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
            for (final String qualifiedName : parameters_.keySet()) {
                transformer.setParameter(qualifiedName, parameters_.get(qualifiedName));
            }
            transformer.transform(xmlSource, result);

            final org.w3c.dom.Node transformedNode = result.getNode();
            if (transformedNode.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
                return transformedNode;
            }
            //output is not DOM (text)
            xmlSource = new DOMSource(source.getDomNodeOrDie());
            final StringWriter writer = new StringWriter();
            final Result streamResult = new StreamResult(writer);
            transformer.transform(xmlSource, streamResult);
            return writer.toString();
        }
        catch (final Exception e) {
            throw Context.reportRuntimeError("Exception: " + e);
        }
    }

    /**
     * Transforms the node source applying the stylesheet given by the importStylesheet() function.
     * The owner document of the output node owns the returned document fragment.
     * @param source the node to be transformed
     * @param output This document is used to generate the output
     * @return the result of the transformation
     */
    public DocumentFragment jsxFunction_transformToFragment(
            final Node source, final Object output) {
        final SgmlPage page = ((Document) output).getDomNodeOrDie();

        final DomDocumentFragment fragment = page.createDomDocumentFragment();
        final DocumentFragment rv = new DocumentFragment();
        rv.setPrototype(getPrototype(rv.getClass()));
        rv.setParentScope(getParentScope());
        rv.setDomNode(fragment);

        transform(source, fragment);
        return rv;
    }

    private void transform(final Node source, final DomNode parent) {
        final Object result = transform(source);
        if (result instanceof org.w3c.dom.Node) {
            final SgmlPage parentPage = parent.getPage();
            final NodeList children = ((org.w3c.dom.Node) result).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                XmlUtil.appendChild(parentPage, parent, children.item(i));
            }
        }
        else {
            final DomText text = new DomText(parent.getPage(), (String) result);
            parent.appendChild(text);
        }
    }

    /**
     * Sets a parameter to be used in subsequent transformations with this nsIXSLTProcessor.
     * If the parameter doesn't exist in the stylesheet the parameter will be ignored.
     * @param namespaceURI the namespaceURI of the XSLT parameter
     * @param localName the local name of the XSLT parameter
     * @param value the new value of the XSLT parameter
     */
    public void jsxFunction_setParameter(final String namespaceURI, final String localName, final Object value) {
        parameters_.put(getQualifiedName(namespaceURI, localName), value);
    }

    /**
     * Gets a parameter if previously set by setParameter. Returns null otherwise.
     * @param namespaceURI the namespaceURI of the XSLT parameter
     * @param localName the local name of the XSLT parameter
     * @return the value of the XSLT parameter
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
     * @param input the input tree
     */
    public void jsxSet_input(final Node input) {
        input_ = input;
    }

    /**
     * Returns which XML input tree to transform.
     * @return which XML input tree to transform
     */
    public Node jsxGet_input() {
        return input_;
    }

    /**
     * Sets the object to which to write the output of the transformation.
     * @param output the object to which to write the output of the transformation
     */
    public void jsxSet_output(final Object output) {
        output_ = output;
    }

    /**
     * Gets a custom output to write the result of the transformation.
     * @return the output of the transformation
     */
    public Object jsxGet_output() {
        return output_;
    }

    /**
     * Adds parameters into an XSL Transformations (XSLT) style sheet.
     *
     * @param baseName the name that will be used inside the style sheet to identify the parameter context
     * @param parameter the parameter value
     *        To remove a parameter previously added to the processor, provide a value of Empty or Null instead.
     * @param namespaceURI an optional namespace
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
        final Node input = input_;
        final SgmlPage page = input.<DomNode>getDomNodeOrDie().getPage();

        if (output_ == null || !(output_ instanceof Node)) {
            final DomDocumentFragment fragment = page.createDomDocumentFragment();
            final DocumentFragment node = new DocumentFragment();
            node.setParentScope(getParentScope());
            node.setPrototype(getPrototype(node.getClass()));
            node.setDomNode(fragment);
            output_ = fragment.getScriptObject();
        }

        transform(input_, ((Node) output_).getDomNodeOrDie());
        final XMLSerializer serializer = new XMLSerializer();
        serializer.setParentScope(getParentScope());
        final StringBuilder output = new StringBuilder();
        for (final DomNode child : ((Node) output_).<DomNode>getDomNodeOrDie().getChildren()) {
            if (child instanceof DomText) {
                //IE: XmlPage ignores all empty text nodes (if 'xml:space' is 'default')
                //Maybe this should be changed for 'xml:space' = preserve
                //See XMLDocumentTest.testLoadXML_XMLSpaceAttribute()
                if (((DomText) child).getData().trim().length() != 0) {
                    output.append(((DomText) child).getData());
                }
            }
            else {
                //remove trailing "\r\n"
                final String serializedString =
                    serializer.jsxFunction_serializeToString((Node) child.getScriptObject());
                output.append(serializedString.substring(0, serializedString.length() - 2));
            }
        }
        output_ = output.toString();
    }
}
