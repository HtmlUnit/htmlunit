/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
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
    private final Set<String> setVariables_ = new HashSet<String>();

    /**
     * {@inheritDoc}
     */
    public String preProcess(final HtmlPage htmlPage, final String sourceCode,
            final String sourceName, final HtmlElement htmlElement) {
        
        final int startPos = sourceCode.indexOf("/*@cc_on");
        if (startPos == -1) {
            return sourceCode;
        }
        final int endPos = sourceCode.indexOf("@*/", startPos);
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
            final String remaining = sourceCode.substring(endPos + 3);
            sb.append(preProcess(htmlPage, remaining, sourceName, htmlElement));
        }
        return sb.toString();
    }
    
    private String processConditionalCompilation(final String precompilationBody,
            final BrowserVersion browserVersion) {
        String body = processIfs(precompilationBody);
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

    private String processIfs(String code) {
        code = code.replaceAll("@if\\s*\\(([^\\)]+)\\)", "if ($1) {");
        code = code.replaceAll("@elif\\s*\\(([^\\)]+)\\)", "} else if ($1) {");
        code = code.replaceAll("@else", "} else {");
        code = code.replaceAll("@end", "}");
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
    private String replaceOneVariable(final String variable, final BrowserVersion browserVersion) {
        final String[] varNaN = {"@_win16", "@_mac", "@_alpha", "@_mc680x0", "@_PowerPC", "@_debug", "@_fast"};
        final String[] varTrue = {"@_win32", "@_x86", "@_jscript"};
        
        if (ArrayUtils.contains(varTrue, variable)) {
            return "true";
        }
        else if ("@_jscript_version".equals(variable)) {
            if (browserVersion.getBrowserVersionNumeric() <= 6) {
                return "5.6";
            }
            else {
                return "5.7";
            }
        }
        else if ("@_jscript_build".equals(variable)) {
            if (browserVersion.getBrowserVersionNumeric() <= 6) {
                return "6626"; // that's what my IE6 currently returns
            }
            else {
                return "5730";
            }
        }
        else if (ArrayUtils.contains(varNaN, variable)) {
            return "NaN";
        }
        return variable;
    }
}
