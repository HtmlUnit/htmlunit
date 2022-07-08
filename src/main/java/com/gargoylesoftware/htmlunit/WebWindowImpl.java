/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_COMPUTED_STYLE_PSEUDO_ACCEPT_WITHOUT_COLON;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_131;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_133;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_86;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_91;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.background.BackgroundJavaScriptFactory;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJobManager;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSS2Properties;
import com.gargoylesoftware.htmlunit.javascript.host.css.CSSStyleSheet;
import com.gargoylesoftware.htmlunit.javascript.host.css.StyleSheetList;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument;

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

    private final WebClient webClient_;
    private final Screen screen_;
    private Page enclosedPage_;
    private transient Object scriptObject_;
    private JavaScriptJobManager jobManager_;
    private final List<WebWindowImpl> childWindows_ = new ArrayList<>();
    private String name_ = "";
    private final History history_ = new History(this);
    private boolean closed_;

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

        screen_ = new Screen(webClient);

        innerHeight_ = 605;
        innerWidth_ = 1256;
        if (webClient.getBrowserVersion().hasFeature(JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_86)) {
            outerHeight_ = innerHeight_ + 86;
            outerWidth_ = innerWidth_ + 16;
        }
        else if (webClient.getBrowserVersion().hasFeature(JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_91)) {
            outerHeight_ = innerHeight_ + 91;
            outerWidth_ = innerWidth_ + 12;
        }
        else if (webClient.getBrowserVersion().hasFeature(JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_131)) {
            outerHeight_ = innerHeight_ + 131;
            outerWidth_ = innerWidth_ + 16;
        }
        else if (webClient.getBrowserVersion().hasFeature(JS_WINDOW_OUTER_INNER_HEIGHT_DIFF_133)) {
            outerHeight_ = innerHeight_ + 133;
            outerWidth_ = innerWidth_ + 16;
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
    public Screen getScreen() {
        return screen_;
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

        if (isJavaScriptInitializationNeeded(page)) {
            webClient_.initialize(this, page);
        }
        if (webClient_.isJavaScriptEngineEnabled()) {
            final Window window = getScriptableObject();
            window.initialize(page);
        }

        // has to be done after page initialization to make sure
        // the enclosedPage has a scriptable object
        enclosedPage_ = page;
        history_.addPage(page);
    }

    /**
     * Returns {@code true} if this window needs JavaScript initialization to occur when the enclosed page is set.
     * @param page the page that will become the enclosing page
     * @return {@code true} if this window needs JavaScript initialization to occur when the enclosed page is set
     */
    protected abstract boolean isJavaScriptInitializationNeeded(Page page);

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
        getJobManager().removeAllJobs();

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

    private void writeObject(final ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(scriptObject_ instanceof Scriptable ? scriptObject_ : null);
    }

    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        scriptObject_ = ois.readObject();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CSS2Properties getComputedStyle(final DomElement element, final String pseudoElement) {
        String normalizedPseudo = pseudoElement;
        if (normalizedPseudo != null) {
            if (normalizedPseudo.startsWith("::")) {
                normalizedPseudo = normalizedPseudo.substring(1);
            }
            else if (getWebClient().getBrowserVersion().hasFeature(JS_WINDOW_COMPUTED_STYLE_PSEUDO_ACCEPT_WITHOUT_COLON)
                    && normalizedPseudo.length() > 0 && normalizedPseudo.charAt(0) != ':') {
                normalizedPseudo = ":" + normalizedPseudo;
            }
        }

        final SgmlPage sgmlPage = element.getPage();
        if (sgmlPage instanceof HtmlPage) {
            final CSS2Properties styleFromCache = ((HtmlPage) sgmlPage).getStyleFromCache(element, normalizedPseudo);
            if (styleFromCache != null) {
                return styleFromCache;
            }
        }

        final Element e = element.getScriptableObject();
        final CSS2Properties style = new CSS2Properties(e);
        final Object ownerDocument = e.getOwnerDocument();
        if (ownerDocument instanceof HTMLDocument) {
            final StyleSheetList sheets = ((HTMLDocument) ownerDocument).getStyleSheets();
            final boolean trace = LOG.isTraceEnabled();
            for (int i = 0; i < sheets.getLength(); i++) {
                final CSSStyleSheet sheet = (CSSStyleSheet) sheets.item(i);
                if (sheet.isActive() && sheet.isEnabled()) {
                    if (trace) {
                        LOG.trace("modifyIfNecessary: " + sheet + ", " + style + ", " + e);
                    }
                    sheet.modifyIfNecessary(style, element, normalizedPseudo);
                }
            }

            ((HtmlPage) element.getPage()).putStyleIntoCache(element, normalizedPseudo, style);
        }
        return style;
    }
}
