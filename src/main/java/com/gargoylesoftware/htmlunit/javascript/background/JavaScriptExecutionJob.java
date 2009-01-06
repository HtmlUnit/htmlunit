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
package com.gargoylesoftware.htmlunit.javascript.background;

import java.lang.ref.WeakReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
 *
 * <p>A JavaScript-triggered background job representing the execution of some JavaScript code.</p>
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @see MemoryLeakTest
 */
public class JavaScriptExecutionJob extends JavaScriptJob {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptExecutionJob.class);

    /** The label for this job. */
    private final String label_;

    /** The window to which this job belongs (weakly referenced, so as not to leak memory). */
    private final WeakReference<Window> window_;

    /** The JavaScript code to execute, if it is in string format. */
    private final String script_;

    /** The JavaScript code to execute, if it is in function format. */
    private final Function function_;

    /**
     * Creates a new JavaScript execution job, where the JavaScript code to execute is a string.
     * @param label the label for the job
     * @param window the window to which the job belongs
     * @param script the JavaScript code to execute
     */
    public JavaScriptExecutionJob(final String label, final Window window, final String script) {
        label_ = label;
        window_ = new WeakReference<Window>(window);
        script_ = script;
        function_ = null;
    }

    /**
     * Creates a new JavaScript execution job, where the JavaScript code to execute is a function.
     * @param label the label for the job
     * @param window the window to which the job belongs
     * @param function the JavaScript code to execute
     */
    public JavaScriptExecutionJob(final String label, final Window window, final Function function) {
        label_ = label;
        window_ = new WeakReference<Window>(window);
        script_ = null;
        function_ = function;
    }

    /** {@inheritDoc} */
    public void run() {
        final Window w = window_.get();
        if (w == null) {
            // The window has been garbage collected! No need to execute, obviously.
            return;
        }

        final WebWindow ww = w.getWebWindow();
        final Page page = ww.getEnclosedPage();

        if (LOG.isDebugEnabled()) {
            LOG.debug("Executing " + this + ".");
        }

        // Verify that the window is still open and the current page is the same.
        if (!ww.getWebClient().getWebWindows().contains(ww) || ww.getEnclosedPage() != page) {
            LOG.debug("The page that originated this job doesn't exist anymore. Execution cancelled.");
            return;
        }

        final HtmlPage htmlPage = (HtmlPage) page;
        if (function_ == null) {
            htmlPage.executeJavaScriptIfPossible(script_, "JavaScriptExecutionJob", 1);
        }
        else {
            final HtmlElement doc = htmlPage.getDocumentElement();
            htmlPage.executeJavaScriptFunctionIfPossible(function_, w, new Object[0], doc);
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Finished executing " + this + ".");
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "JavaScript Execution Job " + getId() + ": " + label_;
    }

}
