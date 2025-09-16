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
package org.htmlunit.javascript.preprocessor;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.ScriptPreProcessor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

/**
 * PreProzessor to fix one default parameter method.
 *
 * @author Ronald Brill
 */
public class HtmxTwoZeroThreeScriptPreProcessor implements ScriptPreProcessor {

    private final ScriptPreProcessor nextScriptPreProcessor_;

    /**
     * Ctor.
     */
    public HtmxTwoZeroThreeScriptPreProcessor() {
        nextScriptPreProcessor_ = null;
    }

    /**
     * Ctor.
     * @param nextScriptPreProcessor the next {@link ScriptPreProcessor}
     */
    public HtmxTwoZeroThreeScriptPreProcessor(final ScriptPreProcessor nextScriptPreProcessor) {
        nextScriptPreProcessor_ = nextScriptPreProcessor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String preProcess(final HtmlPage htmlPage, final String sourceCode, final String sourceName,
            final int lineNumber, final HtmlElement htmlElement) {

        String patchedSourceCode = sourceCode;

        if (sourceName.contains("/htmx.js") && !sourceName.contains("/htmx.js#")) {
            patchedSourceCode = StringUtils.replace(
                    patchedSourceCode,
                    "for (const preservedElt of [...pantry.children]) {",
                    "for (const preservedElt of Array.from(pantry.children)) {");
        }
        else if (sourceName.contains("/htmx.min.js") && !sourceName.contains("/htmx.min.js#")) {
            patchedSourceCode = StringUtils.replace(
                    patchedSourceCode,
                    "for(const t of[...e.children]){",
                    "for(const t of Array.from(e.children)){");
        }

        if (nextScriptPreProcessor_ != null) {
            return nextScriptPreProcessor_.preProcess(htmlPage, patchedSourceCode, sourceName, lineNumber, htmlElement);
        }

        return patchedSourceCode;
    }
}
