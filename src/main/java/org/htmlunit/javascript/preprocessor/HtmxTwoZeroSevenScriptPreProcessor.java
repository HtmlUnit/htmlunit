/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import org.htmlunit.ScriptPreProcessor;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

/**
 * A {@link ScriptPreProcessor} implementation that applies compatibility patches to
 * htmx 2.0.3 - 2.0.7 versions of the htmx JavaScript library.
 * <p>
 * This preprocessor rewrites certain ECMAScript syntax constructs
 * (such as spread operator usage in array push and for-of loops over array copies)
 * to equivalent ES5-compatible code. This is necessary because these features are
 * not yet supported by the JavaScript engine htmlunit-corejs (Rhino).
 * <p>
 * The class can be chained with other {@link ScriptPreProcessor} instances via its constructor.
 * <p>
 * Supported patches include:
 * <ul>
 *   <li>Replacing <code>result.push(...toArray(...))</code> with <code>result.push.apply(result, toArray(...))</code></li>
 *   <li>Replacing <code>result.push(...findAttributeTargets(...))</code> with <code>result.push.apply(result, findAttributeTargets(...))</code></li>
 *   <li>Replacing <code>for (const preservedElt of [...pantry.children])</code> with <code>for (const preservedElt of Array.from(pantry.children))</code></li>
 *   <li>Similar replacements for minified htmx scripts (e.g., <code>htmx.min.js</code>)</li>
 * </ul>
 * <p>
 * <b>Usage Example:</b>
 * <pre>
 * try (WebClient webClient = new WebClient()) {
 *     webClient.setScriptPreProcessor(new HtmxTwoZeroSevenScriptPreProcessor());
 *     // use webClient as needed
 * }
 * </pre>
 *
 * @author Ronald Brill
 * @see ScriptPreProcessor
 */
public class HtmxTwoZeroSevenScriptPreProcessor implements ScriptPreProcessor {

    private final ScriptPreProcessor nextScriptPreProcessor_;

    /**
     * Ctor.
     */
    public HtmxTwoZeroSevenScriptPreProcessor() {
        nextScriptPreProcessor_ = null;
    }

    /**
     * Ctor.
     * @param nextScriptPreProcessor the next {@link ScriptPreProcessor}
     */
    public HtmxTwoZeroSevenScriptPreProcessor(final ScriptPreProcessor nextScriptPreProcessor) {
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
            patchedSourceCode = sourceCode.replace(
                    "result.push(...toArray(rootNode.querySelectorAll(standardSelector)))",
                    "result.push.apply(result, toArray(rootNode.querySelectorAll(standardSelector)))");

            patchedSourceCode = patchedSourceCode.replace(
                    "result.push(...findAttributeTargets(eltToInheritFrom, attrName))",
                    "result.push.apply(result, findAttributeTargets(eltToInheritFrom, attrName))");

            patchedSourceCode = patchedSourceCode.replace(
                    "for (const preservedElt of [...pantry.children]) {",
                    "for (const preservedElt of Array.from(pantry.children)) {");
        }
        else if (sourceName.contains("/htmx.min.js") && !sourceName.contains("/htmx.min.js#")) {
            // 2.0.4
            patchedSourceCode = sourceCode.replace(
                    "i.push(...M(c.querySelectorAll(e)))",
                    "i.push.apply(i,M(c.querySelectorAll(e)))");

            // 2.0.7
            patchedSourceCode = patchedSourceCode.replace(
                    "i.push(...F(c.querySelectorAll(e)))",
                    "i.push.apply(i,F(c.querySelectorAll(e)))");

            patchedSourceCode = patchedSourceCode.replace(
                    "r.push(...we(i,n))",
                    "r.push.apply(r,we(i,n))");

            patchedSourceCode = patchedSourceCode.replace(
                    "for(const t of[...e.children]){",
                    "for(const t of Array.from(e.children)){");
        }

        if (nextScriptPreProcessor_ != null) {
            return nextScriptPreProcessor_.preProcess(htmlPage, patchedSourceCode, sourceName, lineNumber, htmlElement);
        }

        return patchedSourceCode;
    }
}
