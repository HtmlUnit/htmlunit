/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import javax.xml.transform.TransformerException;

import org.htmlunit.html.DomNode;
import org.htmlunit.html.xpath.HtmlUnitPrefixResolver;
import org.htmlunit.html.xpath.XPathAdapter;
import org.htmlunit.html.xpath.XPathHelper;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.xpath.xml.utils.PrefixResolver;
import org.w3c.dom.Node;

/**
 * A JavaScript object for {@code XPathExpression}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class XPathExpression extends HtmlUnitScriptable {

    private final XPathAdapter xpath_;
    private final PrefixResolver prefixResolver_;

    /**
     * Default constructor.
     */
    public XPathExpression() {
        xpath_ = null;
        prefixResolver_ = null;
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
    }

    XPathExpression(final String expression, final PrefixResolver prefixResolver) throws TransformerException {
        final DomNode node = getDomNodeOrDie();
        PrefixResolver resolver = prefixResolver;
        if (resolver == null) {
            final Node xpathExpressionContext = node.getOwnerDocument().getDocumentElement();
            resolver = new HtmlUnitPrefixResolver(xpathExpressionContext);
        }
        prefixResolver_ = resolver;
        final boolean caseSensitive = node.getPage().hasCaseSensitiveTagNames();
        xpath_ = new XPathAdapter(expression, prefixResolver, caseSensitive);
    }

    /**
     * Executes an XPath expression on the given node or document and returns an XPathResult.
     * @param contextNodeObj a {@link Node} representing the context to use for evaluating the expression.
     * @param type type of result to be returned by evaluating the expression. This must be
     * one of the XPathResult.Constants.
     * @param result the result object which may be reused and returned by this method
     * @return the result of the evaluation of the XPath expression
     */
    @JsxFunction
    public XPathResult evaluate(final Object contextNodeObj, final int type, final Object result) {
        try {
            // contextNodeObj can be either a node or an array with the node as the first element.
            if (!(contextNodeObj instanceof Node)) {
                throw JavaScriptEngine.reportRuntimeError("Illegal value for parameter 'context'");
            }

            final Node contextNode = (Node) contextNodeObj;

            final XPathResult xPathResult;
            if (result instanceof XPathResult) {
                xPathResult = (XPathResult) result;
            }
            else {
                xPathResult = new XPathResult();
                xPathResult.setParentScope(getParentScope());
                xPathResult.setPrototype(getPrototype(xPathResult.getClass()));
            }

            xPathResult.init(XPathHelper.getByXPath(contextNode, xpath_, prefixResolver_), type);
            return xPathResult;
        }
        catch (final Exception e) {
            throw JavaScriptEngine.reportRuntimeError("Failed to execute 'evaluate': " + e.getMessage());
        }
    }
}
