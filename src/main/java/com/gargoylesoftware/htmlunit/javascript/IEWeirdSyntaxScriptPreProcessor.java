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

import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A script preprocessor removing weird syntax supported by IE like semicolons before <code>catch</code> or
 * before <code>finally</code> in a try-catch-finally block.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class IEWeirdSyntaxScriptPreProcessor implements ScriptPreProcessor {

    private static final IEWeirdSyntaxScriptPreProcessor instance_ = new IEWeirdSyntaxScriptPreProcessor();
    private static final Pattern patternFinally_
        = Pattern.compile("(\\}(?:\\s*(?://.*\\n)?)*);((?:\\s*(?://.*\\n)?)*finally)");
    private static final Pattern patternCatch_
        = Pattern.compile("(\\}(?:\\s*(?://.*\\n)?)*);((?:\\s*(?://.*\\n)?)*catch)");

    /**
     * Gets an instance of the pre processor.
     * @return an instance
     */
    public static IEWeirdSyntaxScriptPreProcessor getInstance() {
        return instance_;
    }

    /**
     * {@inheritDoc}
     */
    public String preProcess(final HtmlPage htmlPage, String sourceCode,
            final String sourceName, final int lineNumber, final HtmlElement htmlElement) {

        if (sourceCode.contains("catch")) {
            sourceCode = patternCatch_.matcher(sourceCode).replaceAll("$1 $2");
        }
        if (sourceCode.contains("finally")) {
            sourceCode = patternFinally_.matcher(sourceCode).replaceAll("$1 $2");
        }
        return sourceCode;
    }
}
