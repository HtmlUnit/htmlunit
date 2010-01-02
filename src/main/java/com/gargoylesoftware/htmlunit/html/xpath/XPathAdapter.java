/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

import org.apache.xalan.res.XSLMessages;
import org.apache.xml.utils.DefaultErrorHandler;
import org.apache.xml.utils.PrefixResolver;
import org.apache.xml.utils.WrappedRuntimeException;
import org.apache.xpath.Expression;
import org.apache.xpath.ExpressionNode;
import org.apache.xpath.XPathContext;
import org.apache.xpath.compiler.Compiler;
import org.apache.xpath.compiler.FunctionTable;
import org.apache.xpath.compiler.XPathParser;
import org.apache.xpath.objects.XObject;
import org.apache.xpath.res.XPATHErrorResources;

/**
 * XPath adapter implementation for HtmlUnit.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
class XPathAdapter {
    private Expression mainExp_;
    private FunctionTable funcTable_;

    /**
     * Initiates the function table.
     */
    private void initFunctionTable() {
        funcTable_ = new FunctionTable();
        funcTable_.installFunction("lower-case", LowerCaseFunction.class);
        funcTable_.installFunction("is-descendant-of-contextual-form", IsDescendantOfContextualFormFunction.class);
    }

    /**
     * Constructor.
     * @param exprString the XPath expression
     * @param locator the location of the expression, may be <tt>null</tt>
     * @param prefixResolver a prefix resolver to use to resolve prefixes to namespace URIs
     * @param errorListener the error listener, or <tt>null</tt> if default should be used
     * @param caseSensitive whether or not the XPath expression should be case-sensitive
     * @throws TransformerException if a syntax or other error occurs
     */
    XPathAdapter(String exprString, final SourceLocator locator, final PrefixResolver prefixResolver,
        ErrorListener errorListener, final boolean caseSensitive) throws TransformerException {

        initFunctionTable();

        if (errorListener == null) {
            errorListener = new DefaultErrorHandler();
        }

        if (!caseSensitive) {
            exprString = preProcessXPath(exprString);
        }

        final XPathParser parser = new XPathParser(errorListener, locator);
        final Compiler compiler = new Compiler(errorListener, locator, funcTable_);

        parser.initXPath(compiler, exprString, prefixResolver);

        final Expression expr = compiler.compile(0);

        mainExp_ = expr;

        if (locator != null && locator instanceof ExpressionNode) {
            expr.exprSetParent((ExpressionNode) locator);
        }
    }

    /**
     * Pre-processes the specified case-insensitive XPath expression before passing it to the engine.
     * The current implementation lower-cases the attribute name, and anything outside the brackets.
     *
     * @param xpath the XPath expression to pre-process
     * @return the processed XPath expression
     */
    private static String preProcessXPath(String xpath) {
        final char[] charArray = xpath.toCharArray();
        processOutsideBrackets(charArray);
        xpath = new String(charArray);

        final Pattern pattern = Pattern.compile("(@[a-zA-Z]+)");
        final Matcher matcher = pattern.matcher(xpath);
        while (matcher.find()) {
            final String attribute = matcher.group(1);
            xpath = xpath.replace(attribute, attribute.toLowerCase());
        }
        return xpath;
    }

    /**
     * Lower case any character outside the brackets.
     * @param array the array to change
     */
    private static void processOutsideBrackets(final char[] array) {
        final int length = array.length;
        int insideBrackets = 0;
        for (int i = 0; i < length; i++) {
            final char ch = array[i];
            switch (ch) {
                case '[':
                case '(':
                    insideBrackets++;
                    break;

                case ']':
                case ')':
                    insideBrackets--;
                    break;

                default:
                    if (insideBrackets == 0) {
                        array[i] = Character.toLowerCase(ch);
                    }
            }
        }
    }

    /**
     * Given an expression and a context, evaluate the XPath and return the result.
     *
     * @param xpathContext the execution context
     * @param contextNode the node that "." expresses
     * @param namespaceContext the context in which namespaces in the XPath are supposed to be expanded
     * @return the result of the XPath or null if callbacks are used
     * @throws TransformerException if the error condition is severe enough to halt processing
     */
    XObject execute(final XPathContext xpathContext, final int contextNode,
        final PrefixResolver namespaceContext) throws TransformerException {
        xpathContext.pushNamespaceContext(namespaceContext);

        xpathContext.pushCurrentNodeAndExpression(contextNode, contextNode);

        XObject xobj = null;

        try {
            xobj = mainExp_.execute(xpathContext);
        }
        catch (final TransformerException te) {
            te.setLocator(mainExp_);
            final ErrorListener el = xpathContext.getErrorListener();
            if (null != el) {
                el.error(te);
            }
            else {
                throw te;
            }
        }
        catch (Exception e) {
            while (e instanceof WrappedRuntimeException) {
                e = ((WrappedRuntimeException) e).getException();
            }
            String msg = e.getMessage();

            if (msg == null || msg.length() == 0) {
                msg = XSLMessages.createXPATHMessage(XPATHErrorResources.ER_XPATH_ERROR, null);
            }
            final TransformerException te = new TransformerException(msg, mainExp_, e);
            final ErrorListener el = xpathContext.getErrorListener();
            if (null != el) {
                el.fatalError(te);
            }
            else {
                throw te;
            }
        }
        finally {
            xpathContext.popNamespaceContext();
            xpathContext.popCurrentNodeAndExpression();
        }

        return xobj;
    }
}
