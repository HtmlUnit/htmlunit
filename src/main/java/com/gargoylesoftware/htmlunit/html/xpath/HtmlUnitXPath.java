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

import java.util.List;

import com.gargoylesoftware.htmlunit.html.DomNode;

/**
 * XPath adapter implementation for HtmlUnit.
 *
 * @version $Revision$
 * @author Christian Sell
 * @author Mike Bowler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @deprecated As of 2.0, please use {@link DomNode#getByXPath(String)} instead.
 */
@Deprecated
public class HtmlUnitXPath {
    private String xpathExpr_;

    /**
     * Construct given an XPath expression string.
     * @param xpathExpr the XPath expression
     * @deprecated As of 2.0, please use {@link DomNode#getByXPath(String)} instead.
     */
    @Deprecated
    public HtmlUnitXPath(final String xpathExpr) {
        xpathExpr_ = xpathExpr;
    }

    /**
     * Select only the first node selected by this XPath expression.
     * If multiple nodes match, only one node will be returned.
     * The selected node will be the first selected node in document-order, as defined by the XPath specification.
     * @param node the node, node-set or Context object for evaluation
     * @return the first node selected by this XPath expression
     * @deprecated As of 2.0, please use {@link DomNode#getFirstByXPath(String)} instead.
     */
    @Deprecated
    public Object selectSingleNode(final Object node) {
        if (!(node instanceof DomNode)) {
            throw new IllegalArgumentException("" + node + " must be DomNode.");
        }
        return ((DomNode) node).getFirstByXPath(xpathExpr_);
    }

    /**
     * Select all nodes that are selected by this XPath expression.
     * If multiple nodes match, multiple nodes will be returned. Nodes will be returned in document-order,
     * as defined by the XPath specification. If the expression selects a non-node-set
     * (i.e. a number, boolean, or string) then a List containing just that one object is returned.
     * @param node the node, node-set or Context object for evaluation
     * @return the node-set of all items selected by this XPath expression
     * @deprecated As of 2.0, please use {@link DomNode#getByXPath(String)} instead.
     */
    @Deprecated
    public List< ? extends Object> selectNodes(final Object node) {
        if (!(node instanceof DomNode)) {
            throw new IllegalArgumentException("" + node + " must be DomNode.");
        }
        return ((DomNode) node).getByXPath(xpathExpr_);
    }

    /**
     * Retrieves the string-value of the result of evaluating this XPath expression when evaluated against
     * the specified context.
     * The string-value of the expression is determined per the string(..) core function defined in
     * the XPath specification. This means that an expression that selects zero nodes will return the empty string,
     * while an expression that selects one-or-more nodes will return the string-value of the first node.
     * @param node the node, node-set or Context object for evaluation
     * @return the string-value of the result of evaluating this expression with the specified context node
     * @deprecated As of 2.0, please use {@link DomNode#getByXPath(String)} instead.
     */
    @Deprecated
    public String stringValueOf(final Object node) {
        if (!(node instanceof DomNode)) {
            throw new IllegalArgumentException("" + node + " must be DomNode.");
        }
        return (String) ((DomNode) node).getFirstByXPath("string(" + xpathExpr_ + ')');
    }
}
