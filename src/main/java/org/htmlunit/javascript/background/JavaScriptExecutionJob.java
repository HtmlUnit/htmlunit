/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import java.lang.ref.WeakReference;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.Page;
import org.htmlunit.WebWindow;
import org.htmlunit.html.HtmlPage;

/**
 * A JavaScript-triggered background job representing the execution of some JavaScript code.
 *
 * @author Daniel Gredler
 * @author Ronald Brill
 */
abstract class JavaScriptExecutionJob extends BasicJavaScriptJob {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(JavaScriptExecutionJob.class);

    /** The label for this job. */
    private final String label_;

    /** The window to which this job belongs (weakly referenced, so as not to leak memory). */
    private final WeakReference<WebWindow> window_;

    /**
     * Creates a new JavaScript execution job, where the JavaScript code to execute is a string.
     * @param initialDelay the initial amount of time to wait before executing this job
     * @param period the amount of time to wait between executions of this job (may be {@code null})
     * @param label the label for the job
     * @param window the window to which the job belongs
     */
    JavaScriptExecutionJob(final int initialDelay, final Integer period, final String label,
        final WebWindow window) {
        super(initialDelay, period);
        label_ = label;
        window_ = new WeakReference<>(window);
    }

    /** {@inheritDoc} */
    @Override
    public void run() {
        final WebWindow w = window_.get();
        if (w == null) {
            // The window has been garbage collected! No need to execute, obviously.
            return;
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Executing " + this + ".");
        }

        try {
            // Verify that the window is still open
            if (w.isClosed()) {
                LOG.debug("Enclosing window is now closed. Execution cancelled.");
                return;
            }
            if (!w.getWebClient().containsWebWindow(w)) {
                LOG.debug("Enclosing window is now closed. Execution cancelled.");
                return;
            }

            // Verify that the current page is still available and a html page
            final Page enclosedPage = w.getEnclosedPage();
            if (enclosedPage == null || !enclosedPage.isHtmlPage()) {
                if (enclosedPage == null) {
                    LOG.debug("The page that originated this job doesn't exist anymore. Execution cancelled.");
                    return;
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("The page that originated this job is no html page ("
                                + enclosedPage.getClass().getName() + "). Execution cancelled.");
                }
                return;
            }

            runJavaScript((HtmlPage) enclosedPage);
        }
        finally {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Finished executing " + this + ".");
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "JavaScript Execution Job " + getId() + ": " + label_;
    }

    /**
     * Run the JavaScript from the concrete class.
     * @param page the {@link HtmlPage} that owns the script
     */
    protected abstract void runJavaScript(HtmlPage page);
}
