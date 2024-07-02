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

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
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
        // nothing to do
    }

    XPathExpression(final String expression, final PrefixResolver prefixResolver,
            final DomNode node) throws TransformerException {
        PrefixResolver resolver = prefixResolver;
        if (resolver == null) {
            resolver = new HtmlUnitPrefixResolver(node);
        }
        prefixResolver_ = resolver;
        final boolean caseSensitive = node.getPage().hasCaseSensitiveTagNames();
        xpath_ = new XPathAdapter(expression, prefixResolver, caseSensitive);
    }

    /**
     * Executes an XPath expression on the given node or document and returns an XPathResult.
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return the result of the evaluation of the XPath expression
     */
    @JsxFunction
    public static XPathResult evaluate(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (args.length < 1) {
            throw JavaScriptEngine.reportRuntimeError("Missing 'contextNode' parameter");
        }

        int type = 0; // ANY
        if (args.length > 1) {
            type = (int) JavaScriptEngine.toInteger(args[1]);
        }

        Object result = null;
        if (args.length > 2) {
            result = args[2];
        }

        try {
            final Object contextNodeObj = args[0];
            if (!(contextNodeObj instanceof org.htmlunit.javascript.host.dom.Node)) {
                throw JavaScriptEngine.reportRuntimeError("Illegal value for parameter 'context'");
            }

            final Node contextNode = ((org.htmlunit.javascript.host.dom.Node) contextNodeObj).getDomNodeOrDie();
            final XPathExpression expression = (XPathExpression) thisObj;

            final XPathResult xPathResult;
            if (result instanceof XPathResult) {
                xPathResult = (XPathResult) result;
            }
            else {
                xPathResult = new XPathResult();
                xPathResult.setParentScope(expression.getParentScope());
                xPathResult.setPrototype(expression.getPrototype(xPathResult.getClass()));
            }

            xPathResult.init(XPathHelper.getByXPath(contextNode, expression.xpath_, expression.prefixResolver_), type);
            return xPathResult;
        }
        catch (final Exception e) {
            throw JavaScriptEngine.reportRuntimeError("Failed to execute 'evaluate': " + e.getMessage());
        }
    }
}
