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
package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerException;

import org.apache.xml.utils.PrefixResolver;
import org.apache.xpath.XPath;
import org.apache.xpath.XPathContext;
import org.apache.xpath.objects.XBoolean;
import org.apache.xpath.objects.XNodeSet;
import org.apache.xpath.objects.XNumber;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.objects.XString;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Collection of XPath utility methods.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public final class XPathUtils {

    /**
     * Private to avoid instantiation.
     */
    private XPathUtils() {
        // Empty.
    }

    /**
     * Evaluates an XPath expression from the specified node, returning the resultant nodes.
     *
     * @param node the node to start searching from
     * @param xpathExpr the XPath expression
     * @return the list of objects found
     */
    public static List<Object> getByXPath(final Node node, final String xpathExpr) {
        if (xpathExpr == null) {
            throw new NullPointerException("Null is not a valid xpath expression");
        }

        final List<Object> list = new ArrayList<Object>();
        try {
            final XObject result = evaluateXPath(node, xpathExpr);
            
            if (result instanceof XNodeSet) {
                final NodeList nodelist = ((XNodeSet) result).nodelist();
                for (int i = 0; i < nodelist.getLength(); i++) {
                    list.add(nodelist.item(i));
                }
            }
            else if (result instanceof XNumber) {
                list.add(result.num());
            }
            else if (result instanceof XBoolean) {
                list.add(result.bool());
            }
            else if (result instanceof XString) {
                list.add(result.str());
            }
            else {
                throw new RuntimeException("Unproccessed " + result.getClass().getName());
            }
        }
        catch (final Exception e) {
            throw new RuntimeException("Could not retrieve XPath >" + xpathExpr + "< on " + node, e);
        }
        return list;
    }

    /**
     * Evaluates an XPath expression to an XObject.
     * @param contextNode the node to start searching from
     * @param str a valid XPath string
     * @return an XObject, which can be used to obtain a string, number, nodelist, etc (should never be <tt>null</tt>)
     */
    private static XObject evaluateXPath(final Node contextNode, final String str) throws TransformerException {
        final XPathContext xpathSupport = new XPathContext(false);

        final Node xpathExpressionContext;
        if (contextNode.getNodeType() == Node.DOCUMENT_NODE) {
            xpathExpressionContext = ((Document) contextNode).getDocumentElement();
        }
        else {
            xpathExpressionContext = contextNode;
        }
        final PrefixResolver prefixResolver = new HtmlUnitPrefixResolver(xpathExpressionContext);
        final XPath xpath = new XPath(str, null, prefixResolver, XPath.SELECT, null);
        final int ctxtNode = xpathSupport.getDTMHandleFromNode(contextNode);
        return xpath.execute(xpathSupport, ctxtNode, prefixResolver);
    }
}
