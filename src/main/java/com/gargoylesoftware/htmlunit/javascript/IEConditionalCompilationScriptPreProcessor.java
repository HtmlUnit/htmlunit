/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A basic implementation for IE Conditional Compilation.
 *
 * <p>Currently provides (basic) supports for <code>@cc_on</code>, <code>@if</code>,
 * <code>@el_if</code>, <code>@else</code>, <code>@end</code> and <b>@set</b>,
 * as well as for conditional compilation variables:
 * "@_win16", "@_mac", "@_alpha", "@_mc680x0", "@_PowerPC", "@_debug", "@_fast",
 * "@_win32", "@_x86", "@_jscript", "@_jscript_version" and "@_jscript_build"
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 *
 * @see <a href="http://msdn2.microsoft.com/en-us/library/ahx1z4fs.aspx">Microsoft Docs</a>
 */
public class IEConditionalCompilationScriptPreProcessor implements ScriptPreProcessor {

    private static final String CC_VARIABLE_PREFIX = "htmlunit_cc_variable_";
    private enum PARSING_STATUS { NORMAL, IN_MULTI_LINE_COMMENT, IN_SINGLE_LINE_COMMENT, IN_STRING, IN_REG_EXP }

    private final Set<String> setVariables_ = new HashSet<String>();

    /**
     * {@inheritDoc}
     */
    public String preProcess(final HtmlPage htmlPage, final String sourceCode,
            final String sourceName, final int lineNumber, final HtmlElement htmlElement) {

        final int startPos = indexOf(sourceCode, "/*@cc_on", 0);
        if (startPos == -1) {
            return sourceCode;
        }
        final int endPos = indexOf(sourceCode, "@*/", startPos);
        if (endPos == -1) {
            return sourceCode;
        }

        final StringBuilder sb = new StringBuilder();
        if (startPos > 0) {
            sb.append(sourceCode.substring(0, startPos));
        }
        final BrowserVersion browserVersion = htmlPage.getWebClient().getBrowserVersion();
        final String body = sourceCode.substring(startPos + 8, endPos);
        sb.append(processConditionalCompilation(body, browserVersion));
        if (endPos < sourceCode.length() - 3) {
            String remaining = sourceCode.substring(endPos + 3);
            int nextStart = remaining.indexOf("/*@");
            int nextEnd = remaining.indexOf("@*/", nextStart + 3);
            // handle other /*@ @*/ blocks
            while (nextStart >= 0 && nextEnd > 0) {
                sb.append(remaining.substring(0, nextStart));
                final String nextBody = remaining.substring(nextStart + 3, nextEnd);
                sb.append(processConditionalCompilation(nextBody, browserVersion));
                remaining = remaining.substring(nextEnd + 3);
                nextStart = remaining.indexOf("/*@");
                nextEnd = remaining.indexOf("@*/", nextStart + 3);
            }
            sb.append(remaining);
        }
        return sb.toString();
    }

    private String processConditionalCompilation(final String precompilationBody,
            final BrowserVersion browserVersion) {
        String body = precompilationBody;
        if (body.startsWith("cc_on")) {
            body = body.substring(5);
        }
        body = "@" + body;
        //TODO: StringScriptPreProcessor.indexOf() should be used (in order to ignore string literals)
        body = processIfs(body);
        if (body.startsWith("@")) {
            body = body.substring(1);
        }
        body = body.replaceAll("/\\*@end", "");
        body = replaceCompilationVariables(body, browserVersion);
        body = processSet(body);
        body = replaceCustomCompilationVariables(body);
        return body;
    }

    private String replaceCustomCompilationVariables(final String body) {
        final Pattern p = Pattern.compile("@\\w+|'[^']*'|\"[^\"]*\"");
        final Matcher m = p.matcher(body);
        final StringBuffer sb = new StringBuffer();
        while (m.find()) {
            final String match = m.group();
            if (match.startsWith("@")) {
                m.appendReplacement(sb, replaceOneCustomCompilationVariable(match));
            }
            else {
                m.appendReplacement(sb, match);
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private String replaceOneCustomCompilationVariable(final String variable) {
        if (setVariables_.contains(variable)) {
            return CC_VARIABLE_PREFIX + variable.substring(1);
        }
        return "NaN";
    }

    private String processSet(final String body) {
        final Pattern p = Pattern.compile("@set\\s+(@\\w+)(\\s*=\\s*[\\d\\.]+)");
        final Matcher m = p.matcher(body);
        final StringBuffer sb = new StringBuffer();
        while (m.find()) {
            setVariables_.add(m.group(1));
            m.appendReplacement(sb, CC_VARIABLE_PREFIX + m.group(1).substring(1) + m.group(2) + ";");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    private static String processIfs(String code) {
        //TODO: StringScriptPreProcessor.indexOf() should be used (in order to ignore string literals)
        code = code.replaceAll("@if\\s*\\(([^\\)]+)\\)", "if ($1) {");
        code = code.replaceAll("@elif\\s*\\(([^\\)]+)\\)", "} else if ($1) {");
        code = code.replaceAll("@else", "} else {");
        code = code.replaceAll("(/\\*)?@end", "}");
        return code;
    }

    String replaceCompilationVariables(final String source, final BrowserVersion browserVersion) {
        final Pattern p = Pattern.compile("(@_\\w+)|'[^']*'|\"[^\"]*\"");
        final Matcher m = p.matcher(source);
        final StringBuffer sb = new StringBuffer();
        while (m.find()) {
            final String match = m.group();
            if (match.startsWith("@")) {
                m.appendReplacement(sb, replaceOneVariable(match, browserVersion));
            }
            else {
                m.appendReplacement(sb, match);
            }
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * Replace a single conditional compilation variable
     * @param variable something like "@_win32"
     * @return the value
     */
    private static String replaceOneVariable(final String variable, final BrowserVersion browserVersion) {
        final String[] varNaN = {"@_win16", "@_mac", "@_alpha", "@_mc680x0", "@_PowerPC", "@_debug", "@_fast"};
        final String[] varTrue = {"@_win32", "@_x86", "@_jscript"};

        if (ArrayUtils.contains(varTrue, variable)) {
            return "true";
        }
        else if ("@_jscript_version".equals(variable)) {
            if (browserVersion.getBrowserVersionNumeric() <= 6) {
                return "5.6";
            }
            else if (browserVersion.getBrowserVersionNumeric() == 7) {
                return "5.7";
            }
            return "5.8";
        }
        else if ("@_jscript_build".equals(variable)) {
            if (browserVersion.getBrowserVersionNumeric() <= 6) {
                return "6626";
            }
            if (browserVersion.getBrowserVersionNumeric() == 7) {
                return "5730";
            }
            return "18702";
        }
        else if (ArrayUtils.contains(varNaN, variable)) {
            return "NaN";
        }
        return variable;
    }

    /**
     * Returns the index within the JavaScript code of the first occurrence of the specified substring.
     * This method searches inside multi-lines comments, but ignores the string literals and single line comments.
     * @param sourceCode JavaScript source
     * @param str any string
     * @param fromIndex the index from which to start the search
     * @return the index
     */
    private static int indexOf(final String sourceCode, final String str, final int fromIndex) {
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
