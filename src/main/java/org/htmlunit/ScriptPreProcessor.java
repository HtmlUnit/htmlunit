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
package org.htmlunit;

import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;

/**
 * A script pre processor call back. This interface is called when ScriptEngine.execute()
 * is called. It gives developers the opportunity to modify the script to be executed.
 *
 * @author Ben Curren
 */
public interface ScriptPreProcessor {

    /**
     * Pre process the specified source code in the context of the given page.
     * @param htmlPage the page
     * @param sourceCode the code to execute
     * @param sourceName a name for the chunk of code that is going to be executed (used in error messages)
     * @param lineNumber the line number of the source code
     * @param htmlElement the HTML element that will act as the context
     * @return the source code after pre processing
     */
    String preProcess(HtmlPage htmlPage, String sourceCode, String sourceName, int lineNumber, HtmlElement htmlElement);
}
