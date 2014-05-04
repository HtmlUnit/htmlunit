/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_HISTORY_ENUMS_ENTRIES;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.io.IOException;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for the client's browsing history.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Chris Erskine
 * @author Daniel Gredler
 */
@JsxClass
public class History extends SimpleScriptable {

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public History() {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] getIds() {
        Object[] ids = super.getIds();
        if (getBrowserVersion().hasFeature(JS_HISTORY_ENUMS_ENTRIES)) {
            final int len = getWindow().getWebWindow().getHistory().getLength();
            if (len > 0) {
                final Object[] allIds = new Object[ids.length + len];
                System.arraycopy(ids, 0, allIds, 0, ids.length);
                for (int i = 0; i < len; i++) {
                    allIds[ids.length + i] = Integer.valueOf(i);
                }
                ids = allIds;
            }
        }
        return ids;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean has(final int index, final Scriptable start) {
        if (getBrowserVersion().hasFeature(JS_HISTORY_ENUMS_ENTRIES)) {
            final History h = (History) start;
            if (index >= 0 && index < h.getLength()) {
                return true;
            }
        }
        return super.has(index, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        final History h = (History) start;
        if (index < 0 || index >= h.getLength()) {
            return NOT_FOUND;
        }
        return item(index);
    }

    /**
     * Returns the "length" property.
     * @return the "length" property
     */
    @JsxGetter
    public int getLength() {
        final WebWindow w = getWindow().getWebWindow();
        return w.getHistory().getLength();
    }

    /**
     * JavaScript function "back".
     */
    @JsxFunction
    public void back() {
        final WebWindow w = getWindow().getWebWindow();
        try {
            w.getHistory().back();
        }
        catch (final IOException e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * JavaScript function "forward".
     */
    @JsxFunction
    public void forward() {
        final WebWindow w = getWindow().getWebWindow();
        try {
            w.getHistory().forward();
        }
        catch (final IOException e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * JavaScript function "go".
     * @param relativeIndex the relative index
     */
    @JsxFunction
    public void go(final int relativeIndex) {
        final WebWindow w = getWindow().getWebWindow();
        try {
            w.getHistory().go(relativeIndex);
        }
        catch (final IOException e) {
            Context.throwAsScriptRuntimeEx(e);
        }
    }

    /**
     * Returns the "current" property.
     * @return the "current" property
     */
    @JsxGetter(@WebBrowser(FF))
    public String getCurrent() {
        throw Context.reportRuntimeError("Permission denied to get property History.current");
    }

    /**
     * Returns the "previous" property.
     * @return the "previous" property
     */
    @JsxGetter(@WebBrowser(FF))
    public String getPrevious() {
        throw Context.reportRuntimeError("Permission denied to get property History.previous");
    }

    /**
     * Returns the "next" property.
     * @return the "next" property
     */
    @JsxGetter(@WebBrowser(FF))
    public String getNext() {
        throw Context.reportRuntimeError("Permission denied to get property History.next");
    }

    /**
     * JavaScript function "item".
     * @param index the index
     * @return the URL of the history item at the specified index
     */
    @JsxFunction(@WebBrowser(FF))
    public String item(final int index) {
        throw Context.reportRuntimeError("Permission denied to call method History.item");
    }

}
