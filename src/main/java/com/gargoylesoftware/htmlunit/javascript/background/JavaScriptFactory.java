/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.background;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;

import java.net.URL;

public interface JavaScriptFactory {
    /**
     * Creates a new JavaScript execution job, where the JavaScript code to execute is a string.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be {@code null})
     * @param label the label for the job
     * @param window the window to which the job belongs
     * @param script the JavaScript code to execute
     *
     * @return JavaScriptJob the created job
     */
    JavaScriptJob createJavaScriptJob(int initialDelay, Integer period, String label,
            WebWindow window, String script);

    /**
     * Creates a new JavaScript execution job, where the JavaScript code to execute is a function.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be {@code null})
     * @param label the label for the job
     * @param window the window to which the job belongs
     * @param function the JavaScript code to execute
     * @param args the arguments to pass into the function call
     *
     * @return JavaScriptJob the created job
     */
    JavaScriptJob createJavaScriptJob(int initialDelay,
            Integer period, String label,
            WebWindow window, Function function, Object[] args);

    /**
     * Creates a new job for XMLHttpRequestProcessing.
     * @param contextFactory the ContextFactory
     * @param action the action
     *
     * @return JavaScriptJob the created job
     */
    JavaScriptJob createJavascriptXMLHttpRequestJob(ContextFactory contextFactory,
            ContextAction<Object> action);

    /**
     * Creates a new job.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be {@code null})
     * @param runnable the runnable to run
     *
     * @return JavaScriptJob the created job
     */
    JavaScriptJob createJavaScriptJob(int initialDelay, Integer period, Runnable runnable);

    /**
     * Creates a new instance.
     * @param url the URL to download
     * @param callback the callback function to call
     * @param client the web client this if for
     *
     * @return JavaScriptJob the created job
     */
    JavaScriptJob createDownloadBehaviorJob(URL url, Function callback, WebClient client);

    /**
     * Creates the {@link JavaScriptExecutor} that will be used to handle JS.
     * @param webClient the WebClient of the executor
     * @return the executor.
     */
    JavaScriptExecutor createJavaScriptExecutor(WebClient webClient);

    /**
     * Creates a new JavaScriptJobManager for the given window.
     * @param webWindow the window the JavaScriptJobManager will work for
     * @return the new JavaScriptJobManager
     */
    JavaScriptJobManager createJavaScriptJobManager(WebWindow webWindow);
}
