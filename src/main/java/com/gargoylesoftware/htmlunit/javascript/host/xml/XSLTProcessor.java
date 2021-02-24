/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_XSLT_TRANSFORM_INDENT;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Document;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.host.dom.Node;
import com.gargoylesoftware.htmlunit.util.XmlUtils;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * A JavaScript object for {@code XSLTProcessor}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF78})
public class XSLTProcessor extends SimpleScriptable {

    private Node style_;
    private Map<String, Object> parameters_ = new HashMap<>();

    /**
     * Default constructor.
     */
    @JsxConstructor
    public XSLTProcessor() {
    }

    /**
     * Imports the specified stylesheet into this XSLTProcessor for transformations. The specified node
     * may be either a document node or an element node. If it is a document node, then the document can
     * contain either a XSLT stylesheet or a LRE stylesheet. If it is an element node, it must be the
     * xsl:stylesheet (or xsl:transform) element of an XSLT stylesheet.
     *
     * @param style the root-node of an XSLT stylesheet (may be a document node or an element node)
     */
    @JsxFunction
    public void importStylesheet(final Node style) {
        style_ = style;
    }

    /**
     * Transforms the node source applying the stylesheet given by the importStylesheet() function.
     * The owner document of the output node owns the returned document fragment.
     *
     * @param source the node to be transformed
     * @return the result of the transformation
     */
    @JsxFunction
    public XMLDocument transformToDocument(final Node source) {
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
            final DomNode sourceDomNode = source.getDomNodeOrDie();
            Source xmlSource = new DOMSource(sourceDomNode);

            final DomNode xsltDomNode = style_.getDomNodeOrDie();
            final Source xsltSource = new DOMSource(xsltDomNode);

            final Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
            for (final Map.Entry<String, Object> entry : parameters_.entrySet()) {
                transformer.setParameter(entry.getKey(), entry.getValue());
            }

            // hack to preserve indention
            // the transformer only accepts the OutputKeys.INDENT setting if
            // the StreamResult is used
            final SgmlPage page = sourceDomNode.getPage();
            if (page != null && page.getWebClient().getBrowserVersion()
                                            .hasFeature(JS_XSLT_TRANSFORM_INDENT)) {
                final DomNode outputNode = findOutputNode(xsltDomNode);
                if (outputNode != null) {
                    final org.w3c.dom.Node indentNode = outputNode.getAttributes().getNamedItem("indent");
                    if (indentNode != null && "yes".equalsIgnoreCase(indentNode.getNodeValue())) {
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

                        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                            transformer.transform(xmlSource, new StreamResult(out));
                            final WebResponseData data =
                                    new WebResponseData(out.toByteArray(), 200, null, Collections.emptyList());
                            final WebResponse response = new WebResponse(data, null, 0);
                            final org.w3c.dom.Document doc = XmlUtils.buildDocument(response);
                            return doc;
                        }
                    }
                }
            }

            final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final org.w3c.dom.Document containerDocument = factory.newDocumentBuilder().newDocument();
            final org.w3c.dom.Element containerElement = containerDocument.createElement("container");
            containerDocument.appendChild(containerElement);

            final DOMResult result = new DOMResult(containerElement);
            transformer.transform(xmlSource, result);

            final org.w3c.dom.Node transformedNode = result.getNode();
            if (transformedNode.getFirstChild().getNodeType() == Node.ELEMENT_NODE) {
                return transformedNode;
            }

            // output is not DOM (text)
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
    @JsxFunction
    public DocumentFragment transformToFragment(final Node source, final Object output) {
        final SgmlPage page = (SgmlPage) ((Document) output).getDomNodeOrDie();

        final DomDocumentFragment fragment = page.createDocumentFragment();
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
                XmlUtils.appendChild(parentPage, parent, children.item(i), true);
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
    @JsxFunction
    public void setParameter(final String namespaceURI, final String localName, final Object value) {
        parameters_.put(getQualifiedName(namespaceURI, localName), value);
    }

    /**
     * Gets a parameter if previously set by setParameter. Returns null otherwise.
     * @param namespaceURI the namespaceURI of the XSLT parameter
     * @param localName the local name of the XSLT parameter
     * @return the value of the XSLT parameter
     */
    @JsxFunction
    public Object getParameter(final String namespaceURI, final String localName) {
        return parameters_.get(getQualifiedName(namespaceURI, localName));
    }

    private static String getQualifiedName(final String namespaceURI, final String localName) {
        final String qualifiedName;
        if (namespaceURI != null && !namespaceURI.isEmpty() && !"null".equals(namespaceURI)) {
            qualifiedName = '{' + namespaceURI + '}' + localName;
        }
        else {
            qualifiedName = localName;
        }
        return qualifiedName;
    }

    private static DomNode findOutputNode(final DomNode xsltDomNode) {
        for (final DomNode child : xsltDomNode.getChildren()) {
            if ("output".equals(child.getLocalName())) {
                return child;
            }

            for (final DomNode child1 : child.getChildren()) {
                if ("output".equals(child1.getLocalName())) {
                    return child1;
                }
            }
        }
        return null;
    }
}
