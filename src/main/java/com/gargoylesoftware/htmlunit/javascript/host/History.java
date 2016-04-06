/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

import net.sourceforge.htmlunit.corejs.javascript.Context;

/**
 * A JavaScript object for the client's browsing history.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Adam Afeltowicz
 */
@JsxClass
public class History extends SimpleScriptable {

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public History() {
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
        getWindow().getWebWindow().getHistory().back();
    }

    /**
     * JavaScript function "forward".
     */
    @JsxFunction
    public void forward() {
        getWindow().getWebWindow().getHistory().forward();
    }

    /**
     * JavaScript function "go".
     * @param relativeIndex the relative index
     */
    @JsxFunction
    public void go(final int relativeIndex) {
        getWindow().getWebWindow().getHistory().go(relativeIndex);
    }

    /**
     * Replaces a state.
     * @param object the state object
     * @param title the title
     * @param url an optional URL
     */
    @JsxFunction
    public void replaceState(final Object object, final String title, final String url) {
        final WebWindow w = getWindow().getWebWindow();
        try {
            URL newStateUrl = null;
            final HtmlPage page = (HtmlPage) w.getEnclosedPage();
            if (StringUtils.isNotBlank(url)) {
                newStateUrl = page.getFullyQualifiedUrl(url);
            }
            w.getHistory().replaceState(object, newStateUrl);
        }
        catch (final MalformedURLException e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Pushes a state.
     * @param object the state object
     * @param title the title
     * @param url an optional URL
     */
    @JsxFunction
    public void pushState(final Object object, final String title, final String url) {
        final WebWindow w = getWindow().getWebWindow();
        try {
            URL newStateUrl = null;
            final HtmlPage page = (HtmlPage) w.getEnclosedPage();
            if (StringUtils.isNotBlank(url)) {
                newStateUrl = page.getFullyQualifiedUrl(url);
            }
            w.getHistory().pushState(object, newStateUrl);
        }
        catch (final IOException e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Returns the {@code scrollRestoration} property.
     * @return the {@code scrollRestoration} property
     */
    @JsxGetter(@WebBrowser(CHROME))
    public String getScrollRestoration() {
        return "auto";
    }

}
