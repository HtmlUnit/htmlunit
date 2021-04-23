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
package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A {@link JavaScriptJob} created from a {@link Function} object.
 * @author Brad Clarke
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
class JavaScriptFunctionJob extends JavaScriptExecutionJob {

    /** The JavaScript code to execute. */
    private final Function function_;
    private final Object[] args_;

    /**
     * Creates a new JavaScript execution job, where the JavaScript code to execute is a function.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be {@code null})
     * @param label the label for the job
     * @param window the window to which the job belongs
     * @param function the JavaScript code to execute
     * @param args the arguments to pass into the function call
     */
    JavaScriptFunctionJob(final int initialDelay, final Integer period, final String label,
        final WebWindow window, final Function function, final Object[] args) {
        super(initialDelay, period, label, window);
        function_ = function;
        args_ = args;
    }

    /** {@inheritDoc} */
    @Override
    protected void runJavaScript(final HtmlPage page) {
        final DomElement doc = page.getDocumentElement();
        final Scriptable scriptable = page.getEnclosingWindow().getScriptableObject();
        page.executeJavaScriptFunction(function_, scriptable, args_, doc);
    }

}
