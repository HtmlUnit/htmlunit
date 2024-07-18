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
package org.htmlunit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.UrlUtils;

/**
 * A window representing a top level browser window.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author David D. Kilzer
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class TopLevelWindow extends WebWindowImpl {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(TopLevelWindow.class);

    /** The window which caused this window to be opened, if any. */
    private WebWindow opener_;

    /**
     * Creates an instance.
     * @param name the name of the new window
     * @param webClient the web client that "owns" this window
     */
    protected TopLevelWindow(final String name, final WebClient webClient) {
        super(webClient);
        WebAssert.notNull("name", name);
        setName(name);
        performRegistration();
    }

    /**
     * {@inheritDoc}
     * Since this is a top level window, return this window.
     */
    @Override
    public WebWindow getParentWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     * Since this is a top level window, return this window.
     */
    @Override
    public WebWindow getTopWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isJavaScriptInitializationNeeded(final Page page) {
        return getScriptableObject() == null
            || page.getUrl() == UrlUtils.URL_ABOUT_BLANK
            || !(page.getWebResponse() instanceof StringWebResponse);
        // TODO: find a better way to distinguish content written by document.open(),...
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object
     */
    @Override
    public String toString() {
        return "TopLevelWindow[name=\"" + getName() + "\"]";
    }

    /**
     * Sets the opener property. This is the WebWindow that caused this new window to be opened.
     * @param opener the new opener
     */
    public void setOpener(final WebWindow opener) {
        opener_ = opener;
    }

    /**
     * Returns the opener property. This is the WebWindow that caused this new window to be opened.
     * @return the opener
     */
    public WebWindow getOpener() {
        return opener_;
    }

    /**
     * Closes this window.
     */
    public void close() {
        close(false);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * Closes this window.
     * @param ignoreOnbeforeunloadAccepted if true the result of triggering the OnbeforeunloadAccepted event
     * will be ignored
     */
    public void close(final boolean ignoreOnbeforeunloadAccepted) {
        final Page page = getEnclosedPage();

        // a bit strange code but we like to make sure that all steps are processed
        // also if one throws
        boolean rejected = false;
        try {
            if (page != null && page.isHtmlPage()) {
                final HtmlPage htmlPage = (HtmlPage) page;
                final boolean accepted = ignoreOnbeforeunloadAccepted || htmlPage.isOnbeforeunloadAccepted();
                if (!accepted) {
                    LOG.debug("The registered OnbeforeunloadHandler rejected the window close event.");
                    rejected = true;
                }
            }
        }
        finally {
            if (!rejected) {
                try {
                    setClosed();
                }
                finally {
                    if (page != null) {
                        page.cleanUp();
                    }

                    try {
                        getJobManager().shutdown();
                    }
                    finally {
                        try {
                            destroyChildren();
                        }
                        finally {
                            getWebClient().deregisterWebWindow(this);
                        }
                    }
                }
            }
        }
    }
}
