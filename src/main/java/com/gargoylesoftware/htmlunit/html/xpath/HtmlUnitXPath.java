/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
