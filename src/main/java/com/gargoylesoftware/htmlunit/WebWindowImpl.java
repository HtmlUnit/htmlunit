/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br/>
 *
 * Base class for common WebWindow functionality. While public, this class is not
 * exposed in any other places of the API. Internally we can cast to this class
 * when we need access to functionality that is not present in {@link WebWindow}
 *
 * @version $Revision$
 * @author Brad Clarke
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public abstract class WebWindowImpl implements WebWindow {
    private static final long serialVersionUID = 1272747208915903553L;

    private static final Log LOG = LogFactory.getLog(WebWindowImpl.class);

    private WebClient webClient_;
    private Page enclosedPage_;
    private Object scriptObject_;
    private JavaScriptJobManager jobManager_;
    private final List<WebWindowImpl> childWindows_ = new ArrayList<WebWindowImpl>();
    private String name_ = "";
    private final History history_ = new History(this);
    private boolean closed_;

    /**
     * Never call this, used for Serialization.
     * @deprecated As of 1.12
     */
    @Deprecated
    protected WebWindowImpl() {
        // Empty.
    }

    /**
     * Creates a window and associates it with the client.
     *
     * @param webClient the web client that "owns" this window
     */
    public WebWindowImpl(final WebClient webClient) {
        WebAssert.notNull("webClient", webClient);
        webClient_ = webClient;
        jobManager_ = BackgroundJavaScriptFactory.theFactory().createJavaScriptJobManager(this);
    }

    /**
     * Registers the window with the client.
     */
    protected void performRegistration() {
        webClient_.registerWebWindow(this);
    }

    /**
     * {@inheritDoc}
     */
    public WebClient getWebClient() {
        return webClient_;
    }

    /**
     * {@inheritDoc}
     */
    public Page getEnclosedPage() {
        return enclosedPage_;
    }

    /**
     * {@inheritDoc}
     */
    public void setEnclosedPage(final Page page) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("setEnclosedPage: " + page);
        }
        if (page == enclosedPage_) {
            return;
        }
        destroyChildren();
        enclosedPage_ = page;
        history_.addPage(page);
        if (isJavaScriptInitializationNeeded()) {
            webClient_.initialize(this);
        }
        webClient_.initialize(page);
    }

    /**
     * Returns <tt>true</tt> if this window needs JavaScript initialization to occur when the enclosed page is set.
     * @return <tt>true</tt> if this window needs JavaScript initialization to occur when the enclosed page is set
     */
    protected abstract boolean isJavaScriptInitializationNeeded();

    /**
     * {@inheritDoc}
     */
    public void setScriptObject(final Object scriptObject) {
        scriptObject_ = scriptObject;
    }

    /**
     * {@inheritDoc}
     */
    public Object getScriptObject() {
        return scriptObject_;
    }

    /**
     * {@inheritDoc}
     */
    public JavaScriptJobManager getJobManager() {
        return jobManager_;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * <p>Sets the JavaScript job manager for this window.</p>
     *
     * @param jobManager the JavaScript job manager to use
     */
    public void setJobManager(final JavaScriptJobManager jobManager) {
        jobManager_ = jobManager;
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * <p>Adds a child to this window, for shutdown purposes.</p>
     *
     * @param child the child window to associate with this window
     */
    public void addChildWindow(final FrameWindow child) {
        childWindows_.add(child);
    }

    void destroyChildren() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("destroyChildren");
        }
        getJobManager().removeAllJobs();
        for (final ListIterator<WebWindowImpl> iter = childWindows_.listIterator(); iter.hasNext();) {
            final WebWindowImpl window = iter.next();
            if (LOG.isDebugEnabled()) {
                LOG.debug("closing child window: " + window);
            }
            window.setClosed();
            window.getJobManager().shutdown();
            final Page page = window.getEnclosedPage();
            if (page instanceof HtmlPage) {
                ((HtmlPage) page).cleanUp();
            }
            window.destroyChildren();
            iter.remove();
        }
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name_;
    }

    /**
     * {@inheritDoc}
     */
    public void setName(final String name) {
        name_ = name;
    }

    /**
     * Returns this window's navigation history.
     * @return this window's navigation history
     */
    public History getHistory() {
        return history_;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isClosed() {
        return closed_;
    }

    /**
     * Sets this window as closed.
     */
    protected void setClosed() {
        closed_ = true;
    }
}
