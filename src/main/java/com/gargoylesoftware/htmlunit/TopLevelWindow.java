/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * A window representing a top level browser window.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author David D. Kilzer
 * @author Ahmed Ashour
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
    public WebWindow getParentWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     * Since this is a top level window, return this window.
     */
    public WebWindow getTopWindow() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isJavaScriptInitializationNeeded() {
        final Page enclosedPage = getEnclosedPage();
        return getScriptObject() == null
            || enclosedPage.getWebResponse().getWebRequest().getUrl() == WebClient.URL_ABOUT_BLANK
            || !(enclosedPage.getWebResponse() instanceof StringWebResponse);
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
        setClosed();
        final Page page = getEnclosedPage();
        if (page instanceof HtmlPage) {
            final HtmlPage htmlPage = (HtmlPage) page;
            if (!htmlPage.isOnbeforeunloadAccepted()) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("The registered OnbeforeunloadHandler rejected the window close event.");
                }
                return;
            }
            htmlPage.cleanUp();
        }
        getJobManager().shutdown();
        destroyChildren();
        getWebClient().deregisterWebWindow(this);
    }

}
