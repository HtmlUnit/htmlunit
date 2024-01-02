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
package org.htmlunit.javascript.host;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.WebWindow;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for the client's browsing history.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Adam Afeltowicz
 * @author Lai Quang Duong
 */
@JsxClass
public class History extends HtmlUnitScriptable {
    private static final String SCROLL_RESTAURATION_AUTO = "auto";
    private static final String SCROLL_RESTAURATION_MANUAL = "manual";

    private String scrollRestoration_ = SCROLL_RESTAURATION_AUTO;

    /**
     * Creates an instance.
     */
    public History() {
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public void jsConstructor() {
    }

    /**
     * Returns the {@code length} property.
     * @return the {@code length} property
     */
    @JsxGetter
    public int getLength() {
        final WebWindow w = getWindow().getWebWindow();
        return w.getHistory().getLength();
    }

    /**
     * Returns the {@code state} property.
     * @return the {@code state} property
     */
    @JsxGetter
    public Object getState() {
        final WebWindow w = getWindow().getWebWindow();
        return w.getHistory().getCurrentState();
    }

    /**
     * JavaScript function "back".
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
     * JavaScript function "forward".
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
     * JavaScript function "go".
     * @param relativeIndex the relative index
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
     * Replaces a state.
     * @param object the state object
     * @param title the title
     * @param url an optional URL
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
     * Pushes a state.
     * @param object the state object
     * @param title the title
     * @param url an optional URL
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
     * @return the {@code scrollRestoration} property
     */
    @JsxGetter({CHROME, EDGE, FF, FF_ESR})
    public String getScrollRestoration() {
        return scrollRestoration_;
    }

    /**
     * @param scrollRestoration the new value
     */
    @JsxSetter({CHROME, EDGE, FF, FF_ESR})
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
