/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
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
                            "JavaScriptBackgroundJob",
                            htmlPage.getDocumentHtmlElement());
                }
                else {
                    htmlPage.executeJavaScriptFunctionIfPossible(
                            function_,
                            window_,
                            new Object[0],
                            htmlPage.getDocumentHtmlElement());
                }
                if (Thread.currentThread().isInterrupted()) {
                    getLog().debug("JavaScript timeout thread interrupted; clearTimeout() probably called.");
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
