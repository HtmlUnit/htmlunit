/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * Background job.
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Ahmed Ashour
 */
class JavaScriptBackgroundJob implements Runnable {

    private Log getLog() {
        return LogFactory.getLog(getClass());
    }

    private final Window window_;
    private final int timeout_;
    private final String script_;
    private final boolean loopForever_;
    private final Function function_;
    private final String label_;

    JavaScriptBackgroundJob(final Window window, final int timeout, final String script,
            final boolean loopForever, final String label) {
        window_ = window;
        timeout_ = timeout;
        loopForever_ = loopForever;
        script_ = script;
        function_ = null;
        label_ = label;
    }

    JavaScriptBackgroundJob(final Window window, final int timeout, final Function function,
            final boolean loopForever, final String label) {
        window_ = window;
        timeout_ = timeout;
        loopForever_ = loopForever;
        script_ = null;
        function_ = function;
        label_ = label;
    }

    public void run() {
        final Page page = window_.getWebWindow().getEnclosedPage();
        try {
            do {
                Thread.sleep(timeout_);
                String message = "Executing JavaScriptBackgroundJob (" + label_ + "):";
                if (function_ == null) {
                    message += script_;
                }
                else {
                    message += "(function reference)";
                }
                getLog().debug(message);

                final WebWindow webWindow = window_.getWebWindow();
                // test that the window is always opened and the page the same
                if (!webWindow.getWebClient().getWebWindows().contains(webWindow)
                    || webWindow.getEnclosedPage() != page) {

                    getLog().debug(
                            "the page that originated this job doesnt exist anymore. "
                                + "Execution cancelled.");
                    return;
                }

                final HtmlPage htmlPage = (HtmlPage) window_.getWebWindow().getEnclosedPage();
                if (function_ == null) {
                    htmlPage.executeJavaScriptIfPossible(
                            script_,
                            "JavaScriptBackgroundJob", 1);
                }
                else {
                    htmlPage.executeJavaScriptFunctionIfPossible(
                            function_,
                            window_,
                            new Object[0],
                            htmlPage.getDocumentElement());
                }
                if (Thread.currentThread().isInterrupted()) {
                    getLog().debug("JavaScript " + label_ + " thread interrupted; clearTimeout() probably called.");
                    break;
                }
            }
            while (loopForever_);
        }
        catch (final InterruptedException e) {
            getLog().debug("JavaScript timeout thread interrupted; clearTimeout() probably called.");
        }
        catch (final Exception e) {
            getLog().error("Caught exception in Window.setTimeout()", e);
        }
    }
}
