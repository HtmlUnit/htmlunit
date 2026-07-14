/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.htmlunit.WebWindow;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.util.StringUtils;

/**
 * JavaScript host object for the client's browsing history.
 *
 * @author Mike Bowler
 * @author Chris Erskine
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Adam Afeltowicz
 * @author Lai Quang Duong
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/History">MDN Documentation</a>
 */
@JsxClass
public class History extends HtmlUnitScriptable {
    private static final String SCROLL_RESTAURATION_AUTO = "auto";
    private static final String SCROLL_RESTAURATION_MANUAL = "manual";

    private String scrollRestoration_ = SCROLL_RESTAURATION_AUTO;

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Returns the {@code length} property.
     *
     * @return the number of entries in the session history
     */
    @JsxGetter
    public int getLength() {
        final WebWindow w = getWindow().getWebWindow();
        return w.getHistory().getLength();
    }

    /**
     * Returns the {@code state} property.
     *
     * @return the current history state object
     */
    @JsxGetter
    public Object getState() {
        final WebWindow w = getWindow().getWebWindow();
        return w.getHistory().getCurrentState();
    }

    /**
     * Navigates one step back in the session history.
     */
    @JsxFunction
    public void back() {
        try {
            getWindow().getWebWindow().getHistory().back();
        }
        catch (final IOException e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Navigates one step forward in the session history.
     */
    @JsxFunction
    public void forward() {
        try {
            getWindow().getWebWindow().getHistory().forward();
        }
        catch (final IOException e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Navigates by the given number of steps relative to the current position in the session history.
     *
     * @param relativeIndex the relative number of steps to navigate (positive or negative)
     */
    @JsxFunction
    public void go(final int relativeIndex) {
        try {
            getWindow().getWebWindow().getHistory().go(relativeIndex);
        }
        catch (final IOException e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Replaces the current history entry with the given state.
     *
     * @param object the new state object
     * @param title the title (currently ignored by most browsers)
     * @param url an optional new URL for the history entry
     */
    @JsxFunction
    public void replaceState(final Object object, final String title, final Object url) {
        final WebWindow w = getWindow().getWebWindow();
        try {
            w.getHistory().replaceState(object, buildNewStateUrl(w, url));
        }
        catch (final MalformedURLException e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Pushes a new state onto the session history stack.
     *
     * @param object the new state object
     * @param title the title (currently ignored by most browsers)
     * @param url an optional new URL for the new history entry
     */
    @JsxFunction
    public void pushState(final Object object, final String title, final Object url) {
        final WebWindow w = getWindow().getWebWindow();
        try {
            w.getHistory().pushState(object, buildNewStateUrl(w, url));
        }
        catch (final IOException e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    private static URL buildNewStateUrl(final WebWindow webWindow, final Object url) throws MalformedURLException {
        URL newStateUrl = null;
        if (url != null && !JavaScriptEngine.isUndefined(url)) {
            final String urlString = JavaScriptEngine.toString(url);
            if (StringUtils.isNotBlank(urlString)) {
                final HtmlPage page = (HtmlPage) webWindow.getEnclosedPage();
                newStateUrl = page.getFullyQualifiedUrl(urlString);
            }
        }
        return newStateUrl;
    }

    /**
     * Returns the {@code scrollRestoration} property.
     *
     * @return the {@code scrollRestoration} property
     */
    @JsxGetter
    public String getScrollRestoration() {
        return scrollRestoration_;
    }

    /**
     * Sets the {@code scrollRestoration} property.
     *
     * @param scrollRestoration the new value; either {@code "auto"} or {@code "manual"}
     */
    @JsxSetter
    public void setScrollRestoration(final String scrollRestoration) {
        if (SCROLL_RESTAURATION_AUTO.equals(scrollRestoration)) {
            scrollRestoration_ = SCROLL_RESTAURATION_AUTO;
            return;
        }
        if (SCROLL_RESTAURATION_MANUAL.equals(scrollRestoration)) {
            scrollRestoration_ = SCROLL_RESTAURATION_MANUAL;
        }
    }
}
