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
package com.gargoylesoftware.htmlunit.javascript;

import java.util.Map;
import java.util.TreeMap;

import net.sourceforge.htmlunit.corejs.javascript.Parser;
import net.sourceforge.htmlunit.corejs.javascript.ast.AstNode;
import net.sourceforge.htmlunit.corejs.javascript.ast.NodeVisitor;
import net.sourceforge.htmlunit.corejs.javascript.ast.StringLiteral;

import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Process occurrences of \xDD in string literals.
 * <p>This is automatically done by Rhino, but it is sometimes needed before using other PreProcessors.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class StringScriptPreProcessor implements ScriptPreProcessor {

    private enum PARSING_STATUS { NORMAL, IN_MULTI_LINE_COMMENT, IN_SINGLE_LINE_COMMENT, IN_STRING, IN_REG_EXP }

    /**
     * {@inheritDoc}
     */
    public String preProcess(final HtmlPage htmlPage, String sourceCode, final String sourceName,
                final HtmlElement htmlElement) {

        final AstNode root = new Parser().parse(sourceCode, sourceName, 0);
        final Map<Integer, Integer> strings = new TreeMap<Integer, Integer>();
        root.visit(new NodeVisitor() {

            public boolean visit(final AstNode node) {
                if (node instanceof StringLiteral) {
                    strings.put(node.getAbsolutePosition() + 1, node.getLength() - 2);
                }
                return true;
            }
        });

        int variant = 0;
        for (int index : strings.keySet()) {
            final int length = strings.get(index);
            final String string = sourceCode.substring(index + variant, index +  variant + length);
            final String newString = replace(string);
            if (newString != null) {
                sourceCode = sourceCode.substring(0, index + variant)
                    + newString + sourceCode.substring(index + variant + length);
                variant = newString.length() - string.length();
            }
        }
        return sourceCode;
    }

    /**
     * Replaces \xDD with the corresponding character.
     */
    private String replace(final String string) {
        if (string.indexOf("\\x") == -1) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            final char ch = string.charAt(i);
            if (ch == '\\' && i + 3 < string.length() && string.charAt(i + 1) == 'x') {
                final char ch1 = Character.toUpperCase(string.charAt(i + 2));
                final char ch2 = Character.toUpperCase(string.charAt(i + 3));
                if ((ch1 >= '0' && ch1 <= '9' || ch1 >= 'A' && ch1 <= 'F')
                        && (ch2 >= '0' && ch2 <= '9' || ch2 >= 'A' && ch2 <= 'F')) {
                    final char character = (char) Integer.parseInt(string.substring(i + 2, i + 4), 16);
                    if (character >= ' ') {
                        sb.append(character);
                        i += 3;
                        continue;
                    }
                }
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Returns the index within the JavaScript code of the first occurrence of the specified substring.
     * This method searches inside multi-lines comments, but ignores the string literals and single line comments.
     * @param sourceCode JavaScript source
     * @param str any string
     * @param fromIndex the index from which to start the search
     * @return the index
     */
    static int indexOf(final String sourceCode, final String str, final int fromIndex) {
        // parsing logic should be synchronized with #preProcess()
        PARSING_STATUS parsingStatus = PARSING_STATUS.NORMAL;
        char stringChar = 0;
        for (int i = 0; i < sourceCode.length(); i++) {
            if ((parsingStatus == PARSING_STATUS.NORMAL || parsingStatus == PARSING_STATUS.IN_MULTI_LINE_COMMENT)
                    && i >= fromIndex && i + str.length() <= sourceCode.length()
                    && sourceCode.substring(i, i + str.length()).equals(str)) {
                return i;
            }
            final char ch = sourceCode.charAt(i);
            switch (ch) {
                case '/':
                    if (parsingStatus == PARSING_STATUS.NORMAL && i + 1 < sourceCode.length()) {
                        final char nextCh = sourceCode.charAt(i + 1);
                        if (nextCh == '/') {
                            parsingStatus = PARSING_STATUS.IN_SINGLE_LINE_COMMENT;
                        }
                        else if (nextCh == '*') {
                            parsingStatus = PARSING_STATUS.IN_MULTI_LINE_COMMENT;
                        }
                        else {
                            stringChar = ch;
                            parsingStatus = PARSING_STATUS.IN_REG_EXP;
                        }
                    }
                    else if (parsingStatus == PARSING_STATUS.IN_REG_EXP && ch == stringChar) {
                        stringChar = 0;
                        parsingStatus = PARSING_STATUS.NORMAL;
                    }
                    break;

                case '*':
                    if (parsingStatus == PARSING_STATUS.IN_MULTI_LINE_COMMENT && i + 1 < sourceCode.length()) {
                        final char nextCh = sourceCode.charAt(i + 1);
                        if (nextCh == '/') {
                            parsingStatus = PARSING_STATUS.NORMAL;
                        }
                    }
                    break;

                case '\n':
                    if (parsingStatus == PARSING_STATUS.IN_SINGLE_LINE_COMMENT) {
                        parsingStatus = PARSING_STATUS.NORMAL;
                    }
                    break;

                case '\'':
                case '"':
                    if (parsingStatus == PARSING_STATUS.NORMAL) {
                        stringChar = ch;
                        parsingStatus = PARSING_STATUS.IN_STRING;
                    }
                    else if (parsingStatus == PARSING_STATUS.IN_STRING && ch == stringChar) {
                        stringChar = 0;
                        parsingStatus = PARSING_STATUS.NORMAL;
                    }
                    break;

                case '\\':
                    if (parsingStatus == PARSING_STATUS.IN_STRING) {
                        if (i + 3 < sourceCode.length() && sourceCode.charAt(i + 1) == 'x') {
                            final char ch1 = Character.toUpperCase(sourceCode.charAt(i + 2));
                            final char ch2 = Character.toUpperCase(sourceCode.charAt(i + 3));
                            if ((ch1 >= '0' && ch1 <= '9' || ch1 >= 'A' && ch1 <= 'F')
                                    && (ch2 >= '0' && ch2 <= '9' || ch2 >= 'A' && ch2 <= 'F')) {
                                final char character = (char) Integer.parseInt(sourceCode.substring(i + 2, i + 4), 16);
                                if (character >= ' ') {
                                    i += 3;
                                    continue;
                                }
                            }
                        }
                        else if (i + 1 < sourceCode.length()) {
                            i++;
                            continue;
                        }
                    }

                default:
            }
        }
        return -1;
    }
}
