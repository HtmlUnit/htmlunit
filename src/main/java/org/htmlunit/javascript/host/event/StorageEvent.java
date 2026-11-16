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
package org.htmlunit.javascript.host.event;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.Storage;

/**
 * A JavaScript object for {@code StorageEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://html.spec.whatwg.org/multipage/webstorage.html#the-storageevent-interface">
 *     HTML Living Standard - The StorageEvent interface</a>
 */
@JsxClass
public class StorageEvent extends Event {

    private String key_;
    private String oldValue_;
    private String newValue_;
    private String url_ = "";
    private Storage storageArea_;

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @Override
    @JsxConstructor
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(type, details);

        if (details != null && !JavaScriptEngine.isUndefined(details)) {
            key_ = toNullableString(details.get("key"));
            oldValue_ = toNullableString(details.get("oldValue"));
            newValue_ = toNullableString(details.get("newValue"));

            final Object url = details.get("url");
            if (url != NOT_FOUND && !JavaScriptEngine.isUndefined(url)) {
                url_ = JavaScriptEngine.toString(url);
            }

            final Object storageArea = details.get("storageArea");
            if (storageArea instanceof Storage storage) {
                storageArea_ = storage;
            }
            else if (storageArea != null && storageArea != NOT_FOUND
                        && !JavaScriptEngine.isUndefined(storageArea)) {
                throw JavaScriptEngine.typeError(
                        "Failed to construct 'StorageEvent': member storageArea is not of type Storage.");
            }
        }
    }

    private static String toNullableString(final Object value) {
        if (value == null || value == NOT_FOUND || JavaScriptEngine.isUndefined(value)) {
            return null;
        }
        return JavaScriptEngine.toString(value);
    }

    /**
     * Initializes this storage event.
     *
     * @param type the event type
     * @param bubbles whether the event bubbles
     * @param cancelable whether the event is cancelable
     * @param key the changed key or {@code null} if the storage was cleared
     * @param oldValue the old value or {@code null}
     * @param newValue the new value or {@code null}
     * @param url the URL of the document whose storage was changed
     * @param storageArea the affected storage
     */
    public void initStorageEvent(final String type, final boolean bubbles, final boolean cancelable,
            final String key, final String oldValue, final String newValue,
            final String url, final Storage storageArea) {
        initEvent(type, bubbles, cancelable);
        key_ = key;
        oldValue_ = oldValue;
        newValue_ = newValue;
        url_ = url;
        storageArea_ = storageArea;
    }

    /**
     * Implementation of the JavaScript function
     * {@code initStorageEvent(type, bubbles, cancelable, key, oldValue, newValue, url, storageArea)}.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction(functionName = "initStorageEvent")
    public static void jsInitStorageEvent(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        if (!(thisObj instanceof StorageEvent event)) {
            throw JavaScriptEngine.typeError("Illegal invocation");
        }
        if (args.length < 1) {
            throw JavaScriptEngine.typeError("Failed to execute 'initStorageEvent' on 'StorageEvent': "
                    + "1 argument required, but only 0 present.");
        }

        final String type = JavaScriptEngine.toString(args[0]);
        final boolean bubbles = args.length > 1 && JavaScriptEngine.toBoolean(args[1]);
        final boolean cancelable = args.length > 2 && JavaScriptEngine.toBoolean(args[2]);
        final String key = args.length > 3 ? toNullableString(args[3]) : null;
        final String oldValue = args.length > 4 ? toNullableString(args[4]) : null;
        final String newValue = args.length > 5 ? toNullableString(args[5]) : null;
        String url = "";
        if (args.length > 6 && !JavaScriptEngine.isUndefined(args[6])) {
            url = JavaScriptEngine.toString(args[6]);
        }
        Storage storageArea = null;
        if (args.length > 7) {
            if (args[7] instanceof Storage storage) {
                storageArea = storage;
            }
            else if (args[7] != null && !JavaScriptEngine.isUndefined(args[7])) {
                throw JavaScriptEngine.typeError("Failed to execute 'initStorageEvent' on 'StorageEvent': "
                        + "parameter 8 is not of type 'Storage'.");
            }
        }
        event.initStorageEvent(type, bubbles, cancelable, key, oldValue, newValue, url, storageArea);
    }

    /**
     * Returns the key that was changed.
     * @return the key or {@code null} if the storage was cleared
     */
    @JsxGetter
    public String getKey() {
        return key_;
    }

    /**
     * Returns the old value of the changed key.
     * @return the old value or {@code null}
     */
    @JsxGetter
    public String getOldValue() {
        return oldValue_;
    }

    /**
     * Returns the new value of the changed key.
     * @return the new value or {@code null}
     */
    @JsxGetter
    public String getNewValue() {
        return newValue_;
    }

    /**
     * Returns the URL of the document whose storage was changed.
     * @return the URL
     */
    @JsxGetter
    public String getUrl() {
        return url_;
    }

    /**
     * Returns the storage that was affected.
     * @return the storage or {@code null}
     */
    @JsxGetter
    public Storage getStorageArea() {
        return storageArea_;
    }
}
