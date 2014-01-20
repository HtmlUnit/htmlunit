/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.activex;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

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

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NodeList;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.xml.XmlUtil;

/**
 * A JavaScript object for MSXML's (ActiveX) XSLProcessor.<br>
 * Used for transformations with compiled style sheets.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762799.aspx">MSDN documentation</a>
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(browsers = @WebBrowser(IE))
public class XSLProcessor extends MSXMLScriptable {

    private XMLDOMNode style_;
    private XMLDOMNode input_;
    private Object output_;
    private Map<String, Object> parameters_ = new HashMap<String, Object>();

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XSLProcessor() {
    }

    /**
     * Specifies which XML input tree to transform.
     * @param input the input tree
     */
    @JsxSetter(@WebBrowser(IE))
    public void setInput(final XMLDOMNode input) {
        input_ = input;
    }

    /**
     * Returns which XML input tree to transform.
     * @return which XML input tree to transform
     */
    @JsxGetter(@WebBrowser(IE))
    public XMLDOMNode getInput() {
        return input_;
    }

    /**
     * Sets the object to which to write the output of the transformation.
     * @param output the object to which to write the output of the transformation
     */
    @JsxSetter(@WebBrowser(IE))
    public void setOutput(final Object output) {
        output_ = output;
    }

    /**
     * Gets a custom output to write the result of the transformation.
     * @return the output of the transformation
     */
    @JsxGetter(@WebBrowser(IE))
    public Object getOutput() {
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
    @JsxFunction(@WebBrowser(IE))
    public void addParameter(final String baseName, final Object parameter, final Object namespaceURI) {
        final String nsString;
        if (namespaceURI instanceof String) {
            nsString = (String) namespaceURI;
        }
        else {
            nsString = null;
        }
        parameters_.put(getQualifiedName(nsString, baseName), parameter);
    }

    private String getQualifiedName(final String namespaceURI, final String localName) {
        final String qualifiedName;
        if (namespaceURI != null && !namespaceURI.isEmpty() && !"null".equals(namespaceURI)) {
            qualifiedName = '{' + namespaceURI + '}' + localName;
        }
        else {
            qualifiedName = localName;
        }
        return qualifiedName;
    }

    /**
     * Starts the transformation process or resumes a previously failed transformation.
     */
    @JsxFunction(@WebBrowser(IE))
    public void transform() {
        final XMLDOMNode input = input_;
        final SgmlPage page = input.getDomNodeOrDie().getPage();

        if (output_ == null || !(output_ instanceof XMLDOMNode)) {
            final DomDocumentFragment fragment = page.createDomDocumentFragment();
            final XMLDOMDocumentFragment node = new XMLDOMDocumentFragment();
            node.setParentScope(getParentScope());
            node.setPrototype(getPrototype(node.getClass()));
            node.setDomNode(fragment);
            output_ = fragment.getScriptObject();
        }

        transform(input_, ((XMLDOMNode) output_).getDomNodeOrDie());
        final XMLSerializer serializer = new XMLSerializer(false);
        final StringBuilder output = new StringBuilder();
        for (final DomNode child : ((XMLDOMNode) output_).getDomNodeOrDie().getChildren()) {
            if (child instanceof DomText) {
                //IE: XmlPage ignores all empty text nodes (if 'xml:space' is 'default')
                //Maybe this should be changed for 'xml:space' = preserve
                //See XMLDocumentTest.testLoadXML_XMLSpaceAttribute()
                if (StringUtils.isNotBlank(((DomText) child).getData())) {
                    output.append(((DomText) child).getData());
                }
            }
            else {
                //remove trailing "\r\n"
                final String serializedString =
                    serializer.serializeToString((XMLDOMNode) child.getScriptObject());
                output.append(serializedString.substring(0, serializedString.length() - 2));
            }
        }
        output_ = output.toString();
    }

    /**
     * @return {@link XMLDOMNode} or {@link String}
     */
    private Object transform(final XMLDOMNode source) {
        try {
            Source xmlSource = new DOMSource(source.getDomNodeOrDie());
            final Source xsltSource = new DOMSource(style_.getDomNodeOrDie());

            final org.w3c.dom.Document containerDocument =
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            final org.w3c.dom.Element containerElement = containerDocument.createElement("container");
            containerDocument.appendChild(containerElement);

            final DOMResult result = new DOMResult(containerElement);

            final Transformer transformer = TransformerFactory.newInstance().newTransformer(xsltSource);
            for (final Map.Entry<String, Object> entry : parameters_.entrySet()) {
                transformer.setParameter(entry.getKey(), entry.getValue());
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

    private void transform(final XMLDOMNode source, final DomNode parent) {
        final Object result = transform(source);
        if (result instanceof org.w3c.dom.Node) {
            final SgmlPage parentPage = parent.getPage();
            final NodeList children = ((org.w3c.dom.Node) result).getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                XmlUtil.appendChild(parentPage, parent, children.item(i), false);
            }
        }
        else {
            final DomText text = new DomText(parent.getPage(), (String) result);
            parent.appendChild(text);
        }
    }

    /**
     * Imports the specified stylesheet into this XSLTProcessor for transformations. The specified node
     * may be either a document node or an element node. If it is a document node, then the document can
     * contain either a XSLT stylesheet or a LRE stylesheet. If it is an element node, it must be the
     * xsl:stylesheet (or xsl:transform) element of an XSLT stylesheet.
     *
     * @param style the root-node of an XSLT stylesheet (may be a document node or an element node)
     */
    public void importStylesheet(final XMLDOMNode style) {
        style_ = style;
    }
}
