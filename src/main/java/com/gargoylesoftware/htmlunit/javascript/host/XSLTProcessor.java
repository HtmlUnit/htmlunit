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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.stream.StreamSource;

import org.mozilla.javascript.Context;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * A JavaScript object for XSLTProcessor.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class XSLTProcessor extends SimpleScriptable {

    private static final long serialVersionUID = -5870183094839129375L;

    private NodeImpl style_;
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
        try {
            final Source xmlSource = new StreamSource(new StringReader(((XMLDocument) source).jsxGet_xml()));
            final Source xsltSource = new StreamSource(new StringReader(((XMLDocument) style_).jsxGet_xml()));
            final DOMResult result = new DOMResult();
     
            final TransformerFactory transFact = TransformerFactory.newInstance();
            final Transformer trans = transFact.newTransformer(xsltSource);
            for (final Iterator keys = parameters_.keySet().iterator(); keys.hasNext();) {
                final String qualifiedName = (String) keys.next();
                trans.setParameter(qualifiedName, parameters_.get(qualifiedName));
            }
            trans.transform(xmlSource, result);

            final XMLDocument doc = new XMLDocument();
            doc.setPrototype(getPrototype(doc.getClass()));
            doc.setParentScope(getParentScope());

            final XmlPage page = new XmlPage((org.w3c.dom.Document) result.getNode(), getWindow().getWebWindow());
            doc.setDomNode(page);
            return doc;
        }
        catch (final Exception e) {
            throw Context.reportRuntimeError("Exception: " + e);
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
        final String qualifiedName;
        if (namespaceURI.length() != 0 && !namespaceURI.equals("null")) {
            qualifiedName = '{' + namespaceURI + '}' + localName;
        }
        else {
            qualifiedName = localName;
        }
        parameters_.put(qualifiedName, value);
    }

    /**
     * Gets a parameter if previously set by setParameter. Returns null otherwise.
     * @param namespaceURI The namespaceURI of the XSLT parameter.
     * @param localName The local name of the XSLT parameter.
     * @return The value of the XSLT parameter.
     */
    public Object jsxFunction_getParameter(final String namespaceURI, final String localName) {
        final String qualifiedName;
        if (namespaceURI.length() != 0 && !namespaceURI.equals("null")) {
            qualifiedName = '{' + namespaceURI + '}' + localName;
        }
        else {
            qualifiedName = localName;
        }
        return parameters_.get(qualifiedName);
    }
}
