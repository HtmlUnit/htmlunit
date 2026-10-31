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

import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
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

    // entry types reported by Chrome/Edge
    // Object[] (not String[]) because the array is handed to Rhino, which may store other values
    private static final Object[] SUPPORTED_ENTRY_TYPES = {
        "element", "event", "first-input", "largest-contentful-paint", "layout-shift",
        "long-animation-frame", "longtask", "mark", "measure", "navigation", "paint",
        "resource", "visibility-state"};

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
        final VarScope scope = ScriptableObject.getTopLevelScope(thisObj.getParentScope());
        return JavaScriptEngine.newArray(scope, SUPPORTED_ENTRY_TYPES.clone());
    }

    /**
     * WebIDL dictionary semantics: a member set to {@code undefined} counts as absent.
     */
    private static Object getOptionValue(final Scriptable options, final String name) {
        final Object value = ScriptableObject.getProperty(options, name);
        if (value == Scriptable.NOT_FOUND || JavaScriptEngine.isUndefined(value)) {
            return null;
        }
        return value;
    }
}
