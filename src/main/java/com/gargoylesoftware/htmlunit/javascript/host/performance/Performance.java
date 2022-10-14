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
package com.gargoylesoftware.htmlunit.javascript.host.performance;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code Performance}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class Performance extends EventTarget {
    private PerformanceTiming timing_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public Performance() {
    }

    /**
     * Returns the {@code navigation} property.
     * @return the {@code navigation} property
     */
    @JsxGetter
    public PerformanceNavigation getNavigation() {
        final PerformanceNavigation navigation = new PerformanceNavigation();
        navigation.setParentScope(getParentScope());
        navigation.setPrototype(getPrototype(navigation.getClass()));
        return navigation;
    }

    /**
     * Returns the {@code timing} property.
     * @return the {@code timing} property
     */
    @JsxGetter
    public PerformanceTiming getTiming() {
        if (timing_ == null) {
            final PerformanceTiming timing = new PerformanceTiming();
            timing.setParentScope(getParentScope());
            timing.setPrototype(getPrototype(timing.getClass()));
            timing_ = timing;
        }

        return timing_;
    }

    /**
     * @return a timestamp
     */
    @JsxFunction
    public double now() {
        return System.nanoTime() / 1_000_000d;
    }

    /**
     * @return a list of all PerformanceEntry objects for the page.
     * The list's members (entries) can be created by making performance marks
     * or measures (for example by calling the mark() method) at explicit points in time.
     * If you are only interested in performance entries of certain types or that have
     * certain names, see getEntriesByType() and getEntriesByName().
     */
    @JsxFunction
    public Scriptable getEntries() {
        return Context.getCurrentContext().newArray(this, 0);
    }

    /**
     * @return a list of all PerformanceEntry objects for the page.
     * The list's members (entries) can be created by making performance marks
     * or measures (for example by calling the mark() method) at explicit points in time.
     * If you are only interested in performance entries of certain types or that have
     * certain names, see getEntriesByType() and getEntriesByName().
     */
    @JsxFunction
    public Scriptable getEntriesByName() {
        return Context.getCurrentContext().newArray(this, 0);
    }

    /**
     * @return a list of PerformanceEntry objects for a given type. The list's
     * members (entries) can be created by making performance marks or measures
     * (for example by calling the mark() method) at explicit points in time.
     */
    @JsxFunction
    public Scriptable getEntriesByType() {
        return Context.getCurrentContext().newArray(this, 0);
    }
}
