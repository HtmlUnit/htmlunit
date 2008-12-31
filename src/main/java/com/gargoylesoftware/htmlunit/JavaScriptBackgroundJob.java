/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import java.lang.ref.WeakReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * <p>A JavaScript background job (eg setTimeout(), setInterval(), etc).</p>
 *
 * <p>This background job, once started, is guaranteed not to keep old windows in memory (no window
 * memory leaks).</p>
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @see MemoryLeakTest
 */
class JavaScriptBackgroundJob implements Runnable {

    private final WeakReference<Window> window_;
    private final int timeout_;
    private final String script_;
    private final boolean loopForever_;
    private final Function function_;
    private final String label_;

    JavaScriptBackgroundJob(final Window window, final int timeout, final String script,
            final boolean loopForever, final String label) {
        window_ = new WeakReference<Window>(window);
        timeout_ = timeout;
        loopForever_ = loopForever;
        script_ = script;
        function_ = null;
        label_ = label;
    }

    JavaScriptBackgroundJob(final Window window, final int timeout, final Function function,
            final boolean loopForever, final String label) {
        window_ = new WeakReference<Window>(window);
        timeout_ = timeout;
        loopForever_ = loopForever;
        script_ = null;
        function_ = function;
        label_ = label;
    }

    public void run() {
        try {
            do {
                Thread.sleep(timeout_);

                final Window w = window_.get();
                if (w == null) {
                    // The window has been garbage collected! No need to execute, obviously.
                    break;
                }

                final WebWindow ww = w.getWebWindow();
                final Page page = ww.getEnclosedPage();

                if (getLog().isDebugEnabled()) {
                    String message = "Executing JavaScriptBackgroundJob (" + label_ + "):";
                    if (function_ == null) {
                        message += script_;
                    }
                    else {
                        message += "(function reference)";
                    }
                    getLog().debug(message);
                }

                // Verify that the window is still open and the current page is the same.
                if (!ww.getWebClient().getWebWindows().contains(ww) || ww.getEnclosedPage() != page) {
                    getLog().debug("The page that originated this job doesn't exist anymore. Execution cancelled.");
                    break;
                }

                final HtmlPage htmlPage = (HtmlPage) page;
                if (function_ == null) {
                    htmlPage.executeJavaScriptIfPossible(script_, "JavaScriptBackgroundJob", 1);
                }
                else {
                    final HtmlElement doc = htmlPage.getDocumentElement();
                    htmlPage.executeJavaScriptFunctionIfPossible(function_, w, new Object[0], doc);
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
            getLog().error("Caught exception in Window.setTimeout().", e);
        }
    }

    private Log getLog() {
        return LogFactory.getLog(getClass());
    }

}
