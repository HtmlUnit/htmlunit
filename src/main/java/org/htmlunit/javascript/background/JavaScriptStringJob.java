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
package org.htmlunit.javascript.background;

import org.htmlunit.WebWindow;
import org.htmlunit.html.HtmlPage;

/**
 * A {@link JavaScriptJob} created from a string of code.
 * @author Brad Clarke
 */
class JavaScriptStringJob extends JavaScriptExecutionJob {

    /** The JavaScript code to execute. */
    private final String script_;

    /**
     * Creates a new JavaScript execution job, where the JavaScript code to execute is a string.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (perhaps {@code null})
     * @param label the label for the job
     * @param window the window to which the job belongs
     * @param script the JavaScript code to execute
     */
    JavaScriptStringJob(final int initialDelay, final Integer period, final String label,
        final WebWindow window, final String script) {
        super(initialDelay, period, label, window);
        script_ = script;
    }

    /** {@inheritDoc} */
    @Override
    protected void runJavaScript(final HtmlPage page) {
        if (script_ == null) {
            return;
        }
        page.executeJavaScript(script_, "JavaScriptStringJob", 1);
    }

}
