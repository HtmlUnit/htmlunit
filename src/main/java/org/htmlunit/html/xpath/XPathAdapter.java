/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.html.xpath;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

import org.htmlunit.xpath.Expression;
import org.htmlunit.xpath.XPathContext;
import org.htmlunit.xpath.compiler.Compiler;
import org.htmlunit.xpath.compiler.FunctionTable;
import org.htmlunit.xpath.compiler.XPathParser;
import org.htmlunit.xpath.objects.XObject;
import org.htmlunit.xpath.res.XPATHErrorResources;
import org.htmlunit.xpath.res.XPATHMessages;
import org.htmlunit.xpath.xml.utils.DefaultErrorHandler;
import org.htmlunit.xpath.xml.utils.PrefixResolver;
import org.htmlunit.xpath.xml.utils.WrappedRuntimeException;

/**
 * XPath adapter implementation for HtmlUnit.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class XPathAdapter {

    private enum STATE {
        DEFAULT,
        DOUBLE_QUOTED,
        SINGLE_QUOTED,
        ATTRIB
    }

    private final Expression mainExp_;
    private FunctionTable funcTable_;

    /**
     * Initiates the function table.
     */
    private void initFunctionTable() {
        funcTable_ = new FunctionTable();
    }

    /**
     * Constructor.
     * @param exprString the XPath expression
     * @param prefixResolver a prefix resolver to use to resolve prefixes to namespace URIs
     * @param caseSensitive whether the attributes should be case-sensitive
     * @throws TransformerException if a syntax or other error occurs
     */
    public XPathAdapter(final String exprString, final PrefixResolver prefixResolver, final boolean caseSensitive)
                throws TransformerException {

        initFunctionTable();

        final ErrorListener errorHandler = new DefaultErrorHandler();
        final XPathParser parser = new XPathParser(errorHandler);
        final Compiler compiler = new Compiler(errorHandler, funcTable_);

        final String expression = preProcessXPath(exprString, caseSensitive);
        parser.initXPath(compiler, expression, prefixResolver);

        mainExp_ = compiler.compile(0);
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
    @SuppressWarnings("PMD.PreserveStackTrace")
    XObject execute(final XPathContext xpathContext, final int contextNode,
        final PrefixResolver namespaceContext) throws TransformerException {
        xpathContext.pushNamespaceContext(namespaceContext);

        xpathContext.pushCurrentNodeAndExpression(contextNode);

        XObject xobj = null;

        try {
            xobj = mainExp_.execute(xpathContext);
        }
        catch (final TransformerException ex) {
            ex.setLocator(mainExp_);
            final ErrorListener el = xpathContext.getErrorListener();
            if (null != el) {
                el.error(ex);
            }
            else {
                throw ex;
            }
        }
        catch (final Exception e) {
            Exception unwrapped = e;
            while (unwrapped instanceof WrappedRuntimeException) {
                unwrapped = ((WrappedRuntimeException) unwrapped).getException();
            }
            String msg = unwrapped.getMessage();

            if (msg == null || msg.isEmpty()) {
                msg = XPATHMessages.createXPATHMessage(XPATHErrorResources.ER_XPATH_ERROR, null);
            }
            final TransformerException te = new TransformerException(msg, mainExp_, unwrapped);
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
