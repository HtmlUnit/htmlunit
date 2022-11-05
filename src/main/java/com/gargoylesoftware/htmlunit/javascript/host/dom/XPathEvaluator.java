/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.NativeFunctionPrefixResolver;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeFunction;
import net.sourceforge.htmlunit.xpath.xml.utils.PrefixResolver;

/**
 * A JavaScript object for {@code XPathEvaluator}.
 *
 * @author Marc Guillemot
 * @author Chuck Dumont
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE, FF, FF_ESR})
public class XPathEvaluator extends HtmlUnitScriptable {

    /**
     * Default constructor.
     */
    @JsxConstructor
    public XPathEvaluator() {
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
        XPathResult xPathResult = (XPathResult) result;
        if (xPathResult == null) {
            xPathResult = new XPathResult();
            xPathResult.setParentScope(getParentScope());
            xPathResult.setPrototype(getPrototype(xPathResult.getClass()));
        }
        // contextNodeObj can be either a node or an array with the node as the first element.
        if (!(contextNodeObj instanceof Node)) {
            throw Context.reportRuntimeError("Illegal value for parameter 'context'");
        }

        final Node contextNode = (Node) contextNodeObj;
        PrefixResolver prefixResolver = null;
        if (resolver instanceof PrefixResolver) {
            prefixResolver = (PrefixResolver) resolver;
        }
        else if (resolver instanceof NativeFunction) {
            prefixResolver = new NativeFunctionPrefixResolver((NativeFunction) resolver, contextNode.getParentScope());
        }
        xPathResult.init(contextNode.getDomNodeOrDie().getByXPath(expression, prefixResolver), type);
        return xPathResult;
    }

}
