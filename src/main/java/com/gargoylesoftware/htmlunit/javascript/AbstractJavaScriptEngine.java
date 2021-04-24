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
package com.gargoylesoftware.htmlunit.javascript;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.configuration.JavaScriptConfiguration;

/**
 * An interface for {@code JavaScriptEngine}.
 *
 * @param <SCRIPT> the script type
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Chen Jun</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author <a href="mailto:bcurren@esomnie.com">Ben Curren</a>
 * @author David D. Kilzer
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Amit Manjhi
 * @author Ronald Brill
 * @author Frank Danek
 */
public interface AbstractJavaScriptEngine<SCRIPT> {

    /**
     * Gets the associated configuration.
     * @return the configuration
     */
    JavaScriptConfiguration getJavaScriptConfiguration();

    /**
     * Adds an action that should be executed first when the script currently being executed has finished.
     * @param action the action
     */
    void addPostponedAction(PostponedAction action);

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * Process postponed actions, if any.
     */
    void processPostponedActions();

    /**
     * Executes the specified JavaScript code in the context of a given page.
     *
     * @param page the page that the code will execute within
     * @param sourceCode the JavaScript code to execute
     * @param sourceName the name that will be displayed on error conditions
     * @param startLine the line at which the script source starts
     * @return the result of executing the specified code
     */
    Object execute(HtmlPage page,
                           String sourceCode,
                           String sourceName,
                           int startLine);

    /**
     * Register WebWindow with the JavaScriptExecutor.
     * @param webWindow the WebWindow to be registered.
     */
    void registerWindowAndMaybeStartEventLoop(WebWindow webWindow);

    /**
     * Performs initialization for the given webWindow and page.
     * @param webWindow the web window to initialize for
     * @param page the page that will become the enclosing page
     */
    void initialize(WebWindow webWindow, Page page);

    /**
     * Sets the number of milliseconds that a script is allowed to execute before being terminated.
     * A value of 0 or less means no timeout.
     *
     * @param timeout the timeout value, in milliseconds
     */
    void setJavaScriptTimeout(long timeout);

    /**
     * Returns the number of milliseconds that a script is allowed to execute before being terminated.
     * A value of 0 or less means no timeout.
     *
     * @return the timeout value, in milliseconds
     */
    long getJavaScriptTimeout();

    /**
     * Shutdown the JavaScriptEngine.
     */
    void shutdown();

    /**
     * Indicates if JavaScript is running in current thread.
     * This allows code to know if there own evaluation is has been triggered by some JS code.
     * @return {@code true} if JavaScript is running
     */
    boolean isScriptRunning();

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     * Indicates that no postponed action should be executed.
     */
    void holdPosponedActions();

    /**
     * Compiles the specified JavaScript code in the context of a given HTML page.
     *
     * @param page the page that the code will execute within
     * @param sourceCode the JavaScript code to execute
     * @param sourceName the name that will be displayed on error conditions
     * @param startLine the line at which the script source starts
     * @return the result of executing the specified code
     */
    SCRIPT compile(HtmlPage page, String sourceCode, String sourceName, int startLine);

    /**
     * Executes the specified JavaScript code in the context of a given page.
     *
     * @param page the page that the code will execute within
     * @param script the script to execute
     * @return the result of executing the specified code
     */
    Object execute(HtmlPage page, SCRIPT script);
}
