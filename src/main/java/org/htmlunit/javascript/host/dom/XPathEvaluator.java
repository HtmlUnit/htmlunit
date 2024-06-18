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

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativeFunction;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.NativeFunctionPrefixResolver;
import org.htmlunit.xpath.xml.utils.PrefixResolver;

/**
 * A JavaScript object for {@code XPathEvaluator}.
 *
 * @author Marc Guillemot
 * @author Chuck Dumont
 * @author Ronald Brill
 */
@JsxClass
public class XPathEvaluator extends HtmlUnitScriptable {

    /**
     * Default constructor.
     */
    public XPathEvaluator() {
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
    }

    /**
     * Adapts any DOM node to resolve namespaces so that an XPath expression can be easily
     * evaluated relative to the context of the node where it appeared within the document.
     * @param nodeResolver the node to be used as a context for namespace resolution
     * @return an XPathNSResolver which resolves namespaces with respect to the definitions
     *         in scope for a specified node
     */
    @JsxFunction
    public XPathNSResolver createNSResolver(final Node nodeResolver) {
        final XPathNSResolver resolver = new XPathNSResolver();
        resolver.setElement(nodeResolver);
        resolver.setParentScope(getWindow());
        resolver.setPrototype(getPrototype(resolver.getClass()));
        return resolver;
    }

    /**
     * Evaluates an XPath expression string and returns a result of the specified type if possible.
     * @param expression the XPath expression string to be parsed and evaluated
     * @param contextNodeObj the context node for the evaluation of this XPath expression
     * @param resolver the resolver permits translation of all prefixes, including the XML namespace prefix,
     *        within the XPath expression into appropriate namespace URIs.
     * @param type If a specific type is specified, then the result will be returned as the corresponding type
     * @param result the result object which may be reused and returned by this method
     * @return the result of the evaluation of the XPath expression
     */
    @JsxFunction
    public XPathResult evaluate(final String expression, final Object contextNodeObj,
            final Object resolver, final int type, final Object result) {
        try {
            // contextNodeObj can be either a node or an array with the node as the first element.
            if (!(contextNodeObj instanceof Node)) {
                throw JavaScriptEngine.reportRuntimeError("Illegal value for parameter 'context'");
            }

            final Node contextNode = (Node) contextNodeObj;
            PrefixResolver prefixResolver = null;
            if (resolver instanceof PrefixResolver) {
                prefixResolver = (PrefixResolver) resolver;
            }
            else if (resolver instanceof NativeFunction) {
                prefixResolver = new NativeFunctionPrefixResolver(
                                        (NativeFunction) resolver, contextNode.getParentScope());
            }

            final XPathResult xPathResult;
            if (result instanceof XPathResult) {
                xPathResult = (XPathResult) result;
            }
            else {
                xPathResult = new XPathResult();
                xPathResult.setParentScope(getParentScope());
                xPathResult.setPrototype(getPrototype(xPathResult.getClass()));
            }

            xPathResult.init(contextNode.getDomNodeOrDie().getByXPath(expression, prefixResolver), type);
            return xPathResult;
        }
        catch (final Exception e) {
            throw JavaScriptEngine.reportRuntimeError("Failed to execute 'evaluate': " + e.getMessage());
        }
    }

    /**
     * Compiles an XPathExpression which can then be used for (repeated) evaluations of the XPath expression.
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return a XPathExpression representing the compiled form of the XPath expression.
     */
    @JsxFunction
    public static XPathExpression createExpression(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (args.length < 1) {
            throw JavaScriptEngine.reportRuntimeError("Missing 'expression' parameter");
        }

        PrefixResolver prefixResolver = null;
        if (args.length > 1) {
            final Object resolver = args[1];
            if (resolver instanceof PrefixResolver) {
                prefixResolver = (PrefixResolver) resolver;
            }
            else if (resolver instanceof NativeFunction) {
                prefixResolver = new NativeFunctionPrefixResolver(
                                        (NativeFunction) resolver, scope.getParentScope());
            }
        }

        final XPathEvaluator evaluator = (XPathEvaluator) thisObj;

        try {
            final String xpath = JavaScriptEngine.toString(args[0]);
            final XPathExpression xPathExpression  = new XPathExpression(xpath, prefixResolver);
            xPathExpression.setParentScope(evaluator.getParentScope());
            xPathExpression.setPrototype(evaluator.getPrototype(xPathExpression.getClass()));

            return xPathExpression;
        }
        catch (final Exception e) {
            throw JavaScriptEngine.constructError("SyntaxError",
                    "Failed to compile xpath '" + args[0] + "' (" + e.getMessage() + ")");
        }
    }
}
