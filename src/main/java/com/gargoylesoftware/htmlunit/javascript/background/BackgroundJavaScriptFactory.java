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

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;

import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;

/**
 * A factory for all the things we have to construct from outside of the
 * JavaScript engine.
 *
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
public class BackgroundJavaScriptFactory {

    private static BackgroundJavaScriptFactory Factory_ = new BackgroundJavaScriptFactory();

    /**
     * Returns the current factory.
     *
     * @return the active factory
     */
    public static BackgroundJavaScriptFactory theFactory() {
        return Factory_;
    }

    /**
     * Set the factory to a new one.
     *
     * @param factory the new factory.
     */
    public static void setFactory(final BackgroundJavaScriptFactory factory) {
        Factory_ = factory;
    }

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
    public JavaScriptJob createJavaScriptJob(final int initialDelay, final Integer period, final String label,
            final WebWindow window, final String script) {
        return new JavaScriptStringJob(initialDelay, period, label, window, script);
    }

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
    public JavaScriptFunctionJob createJavaScriptJob(final int initialDelay,
            final Integer period, final String label,
            final WebWindow window, final Function function, final Object[] args) {
        return new JavaScriptFunctionJob(initialDelay, period, label, window, function, args);
    }

    /**
     * Creates a new job for XMLHttpRequestProcessing.
     * @param contextFactory the ContextFactory
     * @param action the action
     *
     * @return JavaScriptJob the created job
     */
    public JavaScriptJob createJavascriptXMLHttpRequestJob(final ContextFactory contextFactory,
            final ContextAction<Object> action) {
        return new JavascriptXMLHttpRequestJob(contextFactory, action);
    }

    /**
     * Creates a new job.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be {@code null})
     * @param runnable the runnable to run
     *
     * @return JavaScriptJob the created job
     */
    public JavaScriptJob createJavaScriptJob(final int initialDelay, final Integer period, final Runnable runnable) {
        return new BasicJavaScriptJob(initialDelay, period) {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    /**
     * Creates the {@link JavaScriptExecutor} that will be used to handle JS.
     * @param webClient the WebClient of the executor
     * @return the executor.
     */
    public JavaScriptExecutor createJavaScriptExecutor(final WebClient webClient) {
        return new DefaultJavaScriptExecutor(webClient);
    }

    /**
     * Creates a new JavaScriptJobManager for the given window.
     * @param webWindow the window the JavaScriptJobManager will work for
     * @return the new JavaScriptJobManager
     */
    public JavaScriptJobManager createJavaScriptJobManager(final WebWindow webWindow) {
        return new JavaScriptJobManagerImpl(webWindow);
    }

    /**
     * The constructor.
     */
    protected BackgroundJavaScriptFactory() {
        super();
    }
}
