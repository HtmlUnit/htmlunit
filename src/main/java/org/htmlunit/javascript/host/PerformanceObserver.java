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

import static org.htmlunit.BrowserVersionFeatures.JS_PERFORMANCE_OBSERVER_ENTRY_TYPES_FF;
import static org.htmlunit.BrowserVersionFeatures.JS_PERFORMANCE_OBSERVER_ENTRY_TYPES_FROZEN;

import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.SymbolKey;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxStaticGetter;
import org.htmlunit.javascript.host.dom.DOMException;

/**
 * JavaScript host object for {@code PerformanceObserver}.
 * <p>
 * HtmlUnit does not record performance entries, so the callback is never invoked.
 * This implementation provides the API surface and the argument validation of real
 * browsers, so scripts that use the observer (e.g. for feature detection) do not fail.
 * </p>
 *
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/PerformanceObserver">MDN Documentation</a>
 * @see <a href="https://w3c.github.io/performance-timeline/#the-performanceobserver-interface">W3C spec</a>
 */
@JsxClass
public class PerformanceObserver extends HtmlUnitScriptable {

    // Object[] (not String[]) because the array is handed to Rhino, which may store other values

    /** The entry types supported by Chrome/Edge. */
    private static final Object[] SUPPORTED_ENTRY_TYPES = {
        "element", "event", "first-input", "interaction-contentful-paint", "largest-contentful-paint",
        "layout-shift", "long-animation-frame", "longtask", "mark", "measure", "navigation", "paint",
        "resource", "soft-navigation", "visibility-state"};

    /** The entry types supported by Firefox. */
    private static final Object[] SUPPORTED_ENTRY_TYPES_FF = {
        "event", "first-input", "largest-contentful-paint", "mark", "measure", "navigation", "paint",
        "resource"};

    /** The key of the cached (frozen) supportedEntryTypes array. */
    private static final String SUPPORTED_ENTRY_TYPES_KEY = "PerformanceObserver.supportedEntryTypes";

    private enum ObserverType { UNDEFINED, SINGLE, MULTIPLE }

    private ObserverType observerType_ = ObserverType.UNDEFINED;

    /**
     * Creates an instance of this object.
     * @param callback the function called when observed entries are recorded
     */
    @JsxConstructor
    public void jsConstructor(final Object callback) {
        if (!(callback instanceof Function)) {
            throw JavaScriptEngine.typeError(
                    "Failed to construct 'PerformanceObserver': "
                    + "The callback provided as parameter 1 is not a function.");
        }
    }

    /**
     * Starts observing the given entry types.
     * @param options the options ({@code entryTypes}, or {@code type} and {@code buffered})
     */
    @JsxFunction
    public void observe(final Object options) {
        Object entryTypes = null;
        Object type = null;
        if (options instanceof Scriptable optionsScriptable) {
            entryTypes = getOptionValue(optionsScriptable, "entryTypes");
            type = getOptionValue(optionsScriptable, "type");
        }

        // WebIDL sequence<DOMString>: only iterable objects (e.g. arrays, sets) can be converted
        if (entryTypes != null
                && !(entryTypes instanceof Scriptable entryTypesScriptable
                        && ScriptableObject.hasProperty(entryTypesScriptable, SymbolKey.ITERATOR))) {
            throw JavaScriptEngine.typeError(
                    "Failed to execute 'observe' on 'PerformanceObserver': "
                    + "Failed to read the 'entryTypes' property from 'PerformanceObserverInit': "
                    + "The provided value cannot be converted to a sequence.");
        }

        if (entryTypes == null && type == null) {
            throw JavaScriptEngine.typeError(
                    "Failed to execute 'observe' on 'PerformanceObserver': "
                    + "An observe() call must include either entryTypes or type arguments.");
        }
        if (entryTypes != null && type != null) {
            throw JavaScriptEngine.typeError(
                    "Failed to execute 'observe' on 'PerformanceObserver': "
                    + "An observe() call must not include both entryTypes and type arguments.");
        }

        final ObserverType requested = entryTypes != null ? ObserverType.MULTIPLE : ObserverType.SINGLE;
        if (observerType_ != ObserverType.UNDEFINED && observerType_ != requested) {
            throw JavaScriptEngine.asJavaScriptException(this,
                    "Failed to execute 'observe' on 'PerformanceObserver': "
                    + "This observer has already been used with "
                    + (observerType_ == ObserverType.MULTIPLE ? "entryTypes" : "type")
                    + " and cannot be changed.",
                    DOMException.INVALID_MODIFICATION_ERR);
        }
        observerType_ = requested;

        // browsers silently ignore unsupported entry types (console warning only);
        // HtmlUnit does not record any entries at all
    }

    /**
     * Stops the observer from receiving performance entries.
     */
    @JsxFunction
    public void disconnect() {
        // nothing to do, HtmlUnit does not record performance entries
    }

    /**
     * Returns the performance entries stored in the observer, emptying it out.
     * @return an empty array, HtmlUnit does not record performance entries
     */
    @JsxFunction
    public Scriptable takeRecords() {
        return JavaScriptEngine.newArray(getParentScope(), 0);
    }

    /**
     * Returns the entry types supported by the browser.
     *
     * @param thisObj the scriptable
     * @return the entry types supported by the browser
     */
    @JsxStaticGetter
    public static Scriptable getSupportedEntryTypes(final Scriptable thisObj) {
        final Window window = getWindow(thisObj);
        final BrowserVersion browserVersion = window.getBrowserVersion();

        if (!browserVersion.hasFeature(JS_PERFORMANCE_OBSERVER_ENTRY_TYPES_FROZEN)) {
            // a new (modifiable) array for every call
            return newEntryTypesArray(thisObj, browserVersion);
        }

        // FrozenArray<DOMString>; the same object is returned for every call,
        // cached at the PerformanceObserver constructor of the window
        final ScriptableObject cacheHolder = thisObj instanceof ScriptableObject holder ? holder : null;
        if (cacheHolder != null
                && cacheHolder.getAssociatedValue(SUPPORTED_ENTRY_TYPES_KEY) instanceof Scriptable cached) {
            return cached;
        }

        final Scriptable result = newEntryTypesArray(thisObj, browserVersion);
        final Object objectCtor = ScriptableObject.getProperty(window, "Object");
        if (objectCtor instanceof Scriptable objectCtorScriptable) {
            ScriptableObject.callMethod(objectCtorScriptable, "freeze", new Object[] {result});
        }
        if (cacheHolder != null) {
            cacheHolder.associateValue(SUPPORTED_ENTRY_TYPES_KEY, result);
        }
        return result;
    }

    /**
     * Creates a new array containing the entry types supported by the given browser.
     *
     * @param thisObj the scriptable (used to determine the scope)
     * @param browserVersion the browser version
     * @return a new array containing the supported entry types
     */
    private static Scriptable newEntryTypesArray(final Scriptable thisObj, final BrowserVersion browserVersion) {
        final Object[] entryTypes = browserVersion.hasFeature(JS_PERFORMANCE_OBSERVER_ENTRY_TYPES_FF)
                ? SUPPORTED_ENTRY_TYPES_FF
                : SUPPORTED_ENTRY_TYPES;

        final VarScope scope = ScriptableObject.getTopLevelScope(thisObj.getParentScope());
        return JavaScriptEngine.newArray(scope, entryTypes.clone());
    }

    /**
     * Returns the value of the given member of the options dictionary;
     * WebIDL dictionary semantics: a member set to {@code undefined} counts as absent.
     *
     * @param options the options dictionary
     * @param name the name of the member
     * @return the value of the member or {@code null} if the member is absent
     */
    private static Object getOptionValue(final Scriptable options, final String name) {
        final Object value = ScriptableObject.getProperty(options, name);
        if (value == Scriptable.NOT_FOUND || JavaScriptEngine.isUndefined(value)) {
            return null;
        }
        return value;
    }
}
