/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.xpath;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.SourceLocator;
import javax.xml.transform.TransformerException;

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
import org.apache.xpath.res.XPATHMessages;

/**
 * XPath adapter implementation for HtmlUnit.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
class XPathAdapter {

    private enum STATE {
        DEFAULT,
        DOUBLE_QUOTED,
        SINGLE_QUOTED,
        ATTRIB
    }

    private Expression mainExp_;
    private FunctionTable funcTable_;

    /**
     * Initiates the function table.
     */
    private void initFunctionTable() {
        funcTable_ = new FunctionTable();
        funcTable_.installFunction("lower-case", LowerCaseFunction.class);
    }

    /**
     * Constructor.
     * @param exprString the XPath expression
     * @param locator the location of the expression, may be {@code null}
     * @param prefixResolver a prefix resolver to use to resolve prefixes to namespace URIs
     * @param errorListener the error listener, or {@code null} if default should be used
     * @param attributeCaseSensitive whether or not the attributes should be case-sensitive
     * @throws TransformerException if a syntax or other error occurs
     */
    XPathAdapter(final String exprString, final SourceLocator locator, final PrefixResolver prefixResolver,
        final ErrorListener errorListener, final boolean caseSensitive)
                throws TransformerException {

        initFunctionTable();

        ErrorListener errListener = errorListener;
        if (errListener == null) {
            errListener = new DefaultErrorHandler();
        }
        final XPathParser parser = new XPathParser(errListener, locator);
        final Compiler compiler = new Compiler(errorListener, locator, funcTable_);

        final String expression = preProcessXPath(exprString, caseSensitive);
        parser.initXPath(compiler, expression, prefixResolver);

        final Expression expr = compiler.compile(0);
        mainExp_ = expr;

        if (locator instanceof ExpressionNode) {
            expr.exprSetParent((ExpressionNode) locator);
        }
    }

    /**
     * Pre-processes the specified case-insensitive XPath expression before passing it to the engine.
     * The current implementation lower-cases the attribute name, and anything outside the brackets.
     *
     * @param xpath the XPath expression to pre-process
     * @param caseSensitive whether or not the XPath expression should be case-sensitive
     * @return the processed XPath expression
     */
    private static String preProcessXPath(final String xpath, final boolean caseSensitive) {
        if (caseSensitive) {
            return xpath;
        }

        final char[] charArray = xpath.toCharArray();
        STATE state = STATE.DEFAULT;

        final int length = charArray.length;
        int insideBrackets = 0;
        for (int i = 0; i < length; i++) {
            final char ch = charArray[i];
            switch (ch) {
                case '@':
                    if (state == STATE.DEFAULT) {
                        state = STATE.ATTRIB;
                    }
                    break;

                case '"':
                    if (state == STATE.DEFAULT || state == STATE.ATTRIB) {
                        state = STATE.DOUBLE_QUOTED;
                    }
                    else if (state == STATE.DOUBLE_QUOTED) {
                        state = STATE.DEFAULT;
                    }
                    break;

                case '\'':
                    if (state == STATE.DEFAULT || state == STATE.ATTRIB) {
                        state = STATE.SINGLE_QUOTED;
                    }
                    else if (state == STATE.SINGLE_QUOTED) {
                        state = STATE.DEFAULT;
                    }
                    break;

                case '[':
                case '(':
                    if (state == STATE.ATTRIB) {
                        state = STATE.DEFAULT;
                    }
                    insideBrackets++;
                    break;

                case ']':
                case ')':
                    if (state == STATE.ATTRIB) {
                        state = STATE.DEFAULT;
                    }
                    insideBrackets--;
                    break;

                default:
                    if (insideBrackets == 0
                            && state != STATE.SINGLE_QUOTED
                            && state != STATE.DOUBLE_QUOTED) {
                        charArray[i] = Character.toLowerCase(ch);
                    }
                    else if (state == STATE.ATTRIB) {
                        charArray[i] = Character.toLowerCase(ch);
                    }

                    if (state == STATE.ATTRIB) {
                        final boolean isValidAttribChar =
                                ('a' <= ch && ch <= 'z')
                                || ('A' <= ch && ch <= 'Z')
                                || ('0' <= ch && ch <= '9')
                                || ('\u00C0' <= ch && ch <= '\u00D6')
                                || ('\u00D8' <= ch && ch <= '\u00F6')
                                || ('\u00F8' <= ch && ch <= '\u02FF')
                                || ('\u0370' <= ch && ch <= '\u037D')
                                || ('\u037F' <= ch && ch <= '\u1FFF')
                                || ('\u200C' <= ch && ch <= '\u200D')
                                || ('\u2C00' <= ch && ch <= '\u2FEF')
                                || ('\u3001' <= ch && ch <= '\uD7FF')
                                || ('\uF900' <= ch && ch <= '\uFDCF')
                                || ('\uFDF0' <= ch && ch <= '\uFFFD')
                                // [#x10000-#xEFFFF]
                                || ('\u00B7' == ch)
                                || ('\u0300' <= ch && ch <= '\u036F')
                                || ('\u203F' <= ch && ch <= '\u2040')
                                || ('_' == ch)
                                || ('-' == ch)
                                || ('.' == ch);

                        if (!isValidAttribChar) {
                            state = STATE.DEFAULT;
                        }
                    }
            }
        }
        return new String(charArray);
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

            if (msg == null || msg.isEmpty()) {
                msg = XPATHMessages.createXPATHMessage(XPATHErrorResources.ER_XPATH_ERROR, null);
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
