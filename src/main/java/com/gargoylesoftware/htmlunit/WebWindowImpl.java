/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_133;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_63;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_94;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;

import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
 *
 * Base class for common WebWindow functionality. While public, this class is not
 * exposed in any other places of the API. Internally we can cast to this class
 * when we need access to functionality that is not present in {@link WebWindow}
 *
 * @author Brad Clarke
 * @author David K. Taylor
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public abstract class WebWindowImpl implements WebWindow {

    private static final Log LOG = LogFactory.getLog(WebWindowImpl.class);

    private WebClient webClient_;
    private Page enclosedPage_;
    private transient Object scriptObject_;
    private JavaScriptJobManager jobManager_;
    private final List<WebWindowImpl> childWindows_ = new ArrayList<>();
    private String name_ = "";
    private final History history_ = new History(this);
    private boolean closed_;
    private Map<Object, Object> threadLocalMap_;

    private int innerHeight_;
    private int outerHeight_;
    private int innerWidth_;
    private int outerWidth_;

    /**
     * Creates a window and associates it with the client.
     *
     * @param webClient the web client that "owns" this window
     */
    public WebWindowImpl(final WebClient webClient) {
        WebAssert.notNull("webClient", webClient);
        webClient_ = webClient;
        jobManager_ = BackgroundJavaScriptFactory.theFactory().createJavaScriptJobManager(this);

        innerHeight_ = 605;
        innerWidth_ = 1256;
        if (webClient.getBrowserVersion().hasFeature(JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_63)) {
            outerHeight_ = innerHeight_ + 63;
            outerWidth_ = innerWidth_ + 16;
        }
        else if (webClient.getBrowserVersion().hasFeature(JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_94)) {
            outerHeight_ = innerHeight_ + 94;
            outerWidth_ = innerWidth_ + 14;
        }
        else if (webClient.getBrowserVersion().hasFeature(JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_133)) {
            outerHeight_ = innerHeight_ + 133;
            outerWidth_ = innerWidth_ + 10;
        }
        else {
            outerHeight_ = innerHeight_ + 115;
            outerWidth_ = innerWidth_ + 14;
        }
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
    @Override
    public WebClient getWebClient() {
        return webClient_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page getEnclosedPage() {
        return enclosedPage_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
     * Returns {@code true} if this window needs JavaScript initialization to occur when the enclosed page is set.
     * @return {@code true} if this window needs JavaScript initialization to occur when the enclosed page is set
     */
    protected abstract boolean isJavaScriptInitializationNeeded();

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void setScriptableObject(final T scriptObject) {
        scriptObject_ = scriptObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getScriptableObject() {
        return (T) scriptObject_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
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
        synchronized (childWindows_) {
            childWindows_.add(child);
        }
    }

    /**
     * Destroy our children.
     */
    protected void destroyChildren() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("destroyChildren");
        }
        // improve the timeout handling here
        long timeout = getWebClient().getJavaScriptTimeout();
        if (timeout < 1) {
            timeout = 10_000;
        }
        getJobManager().removeAllJobs(timeout);

        // try to deal with js thread adding a new window in between
        while (!childWindows_.isEmpty()) {
            final WebWindowImpl window = childWindows_.get(0);
            removeChildWindow(window);
        }
    }

    /**
     * <p><span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span></p>
     *
     * Destroy the child window.
     * @param window the child to destroy
     */
    public void removeChildWindow(final WebWindowImpl window) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("closing child window: " + window);
        }
        window.setClosed();
        window.getJobManager().shutdown();
        final Page page = window.getEnclosedPage();
        if (page != null) {
            page.cleanUp();
        }
        window.destroyChildren();

        synchronized (childWindows_) {
            childWindows_.remove(window);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setName(final String name) {
        name_ = name;
    }

    /**
     * Returns this window's navigation history.
     * @return this window's navigation history
     */
    @Override
    public History getHistory() {
        return history_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isClosed() {
        return closed_;
    }

    /**
     * Sets this window as closed.
     */
    protected void setClosed() {
        closed_ = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInnerWidth() {
        return innerWidth_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInnerWidth(final int innerWidth) {
        innerWidth_ = innerWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOuterWidth() {
        return outerWidth_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOuterWidth(final int outerWidth) {
        outerWidth_ = outerWidth;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInnerHeight() {
        return innerHeight_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInnerHeight(final int innerHeight) {
        innerHeight_ = innerHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getOuterHeight() {
        return outerHeight_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOuterHeight(final int outerHeight) {
        outerHeight_ = outerHeight;
    }

    @Override
    public final Object getThreadLocal(final Object key) {
        if (threadLocalMap_ == null) {
            return null;
        }
        return threadLocalMap_.get(key);
    }

    @Override
    public final synchronized void putThreadLocal(final Object key, final Object value) {
        if (threadLocalMap_ == null) {
            threadLocalMap_ = new HashMap<>();
        }
        threadLocalMap_.put(key, value);
    }

    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(scriptObject_ instanceof Scriptable ? scriptObject_ : null);
    }

    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        scriptObject_ = ois.readObject();
    }
}
