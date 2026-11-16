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

import static org.htmlunit.BrowserVersionFeatures.JS_STORAGE_PRESERVED_INCLUDED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.htmlunit.Page;
import org.htmlunit.StorageHolder;
import org.htmlunit.StorageHolder.Type;
import org.htmlunit.WebClient;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.CompoundOperationMap;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.StorageEvent;

/**
 * JavaScript host object for {@code Storage}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Kanoko Yamamoto
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Storage">MDN Documentation</a>
 * @see <a href="https://html.spec.whatwg.org/multipage/webstorage.html">HTML Living Standard - Web storage</a>
 */
@JsxClass
public class Storage extends HtmlUnitScriptable {

    private static final HashSet<String> RESERVED_NAMES_ = new HashSet<>(Arrays.asList(
        "clear", "key", "getItem", "length", "removeItem",
        "setItem", "constructor", "toString", "toLocaleString", "valueOf", "hasOwnProperty", "propertyIsEnumerable",
        "isPrototypeOf", "__defineGetter__", "__defineSetter__", "__lookupGetter__", "__lookupSetter__"));

    /** The quota (5 MiB) in characters; keys and values are counted. */
    private static final long STORE_SIZE_LIMIT = 5L * 1024 * 1024;

    private final Map<String, String> store_;
    private final Type type_;

    /**
     * Default constructor for prototype instantiation only.
     */
    public Storage() {
        super();
        store_ = null;
        type_ = null;
    }

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Creates a new {@code Storage} instance backed by the given store.
     * No storage events are fired for changes made through this instance.
     *
     * @param window the parent scope
     * @param store the backing map for this storage
     */
    public Storage(final Window window, final Map<String, String> store) {
        this(window, store, null);
    }

    /**
     * Creates a new {@code Storage} instance backed by the given store.
     *
     * @param window the parent scope
     * @param store the backing map for this storage
     * @param type the type of this storage, used to fire storage events in other windows;
     *        if {@code null} no events are fired
     */
    public Storage(final Window window, final Map<String, String> store, final Type type) {
        super();
        store_ = store;
        type_ = type;
        setParentScope(getTopLevelScope(window.getParentScope()));
        setPrototype(window.getPrototype(Storage.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        final boolean isReserved = RESERVED_NAMES_.contains(name);
        if (store_ == null || isReserved) {
            super.put(name, start, value);
        }
        if (store_ != null && (!isReserved || getBrowserVersion().hasFeature(JS_STORAGE_PRESERVED_INCLUDED))) {
            setItem(name, JavaScriptEngine.toString(value));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (store_ == null || RESERVED_NAMES_.contains(name)) {
            return super.get(name, start);
        }
        final Object value = getItem(name);
        if (value != null) {
            return value;
        }
        return super.get(name, start);
    }

    /**
     * Overridden to make the stored items visible as own properties.
     * {@inheritDoc}
     */
    @Override
    public boolean has(final String name, final Scriptable start) {
        return isItem(name) || super.has(name, start);
    }

    /**
     * Overridden to support the removal of stored items via {@code delete}.
     * {@inheritDoc}
     */
    @Override
    public void delete(final String name) {
        if (isItem(name) && !super.has(name, this)) {
            removeItem(name);
            return;
        }
        super.delete(name);
    }

    /**
     * Overridden to enumerate the stored items as own properties
     * (Object.keys(), Object.getOwnPropertyNames(), for...in, ...).
     * {@inheritDoc}
     */
    @Override
    protected Object[] getIds(final CompoundOperationMap<Scriptable> map,
            final boolean getNonEnumerable, final boolean getSymbols) {
        return addItemIds(super.getIds(map, getNonEnumerable, getSymbols));
    }

    /**
     * Overridden to provide property descriptors for the stored items.
     * {@inheritDoc}
     */
    @Override
    protected DescriptorInfo getOwnPropertyDescriptor(final Context cx, final Object id) {
        final DescriptorInfo descInfo = super.getOwnPropertyDescriptor(cx, id);
        if (descInfo != null) {
            return descInfo;
        }

        if (id instanceof CharSequence) {
            final String name = id.toString();
            if (isItem(name)) {
                final Object value = getItem(name);
                if (value != null) {
                    return ScriptableObject.buildDataDescriptor(value, ScriptableObject.EMPTY);
                }
            }
        }
        return null;
    }

    private boolean isItem(final String name) {
        if (store_ == null || RESERVED_NAMES_.contains(name)) {
            return false;
        }
        synchronized (store_) {
            return store_.containsKey(name);
        }
    }

    private Object[] addItemIds(final Object[] ids) {
        if (store_ == null) {
            return ids;
        }

        final List<Object> result = new ArrayList<>();
        synchronized (store_) {
            for (final String key : store_.keySet()) {
                if (!RESERVED_NAMES_.contains(key)) {
                    result.add(key);
                }
            }
        }
        for (final Object id : ids) {
            if (!result.contains(id)) {
                result.add(id);
            }
        }
        return result.toArray();
    }

    /**
     * Returns the number of items in the storage.
     *
     * @return the number of items
     */
    @JsxGetter
    public int getLength() {
        synchronized (store_) {
            return store_.size();
        }
    }

    /**
     * Removes the item with the specified key.
     *
     * @param key the key of the item to remove
     */
    public void removeItem(final String key) {
        final String removed;
        synchronized (store_) {
            removed = store_.remove(key);
        }
        if (removed != null) {
            fireStorageEvent(key, removed, null);
        }
    }

    /**
     * Implementation of the JavaScript function {@code removeItem(key)}.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction(functionName = "removeItem")
    public static void jsRemoveItem(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        checkArguments(args, 1, "removeItem");
        getStorage(thisObj).removeItem(JavaScriptEngine.toString(args[0]));
    }

    /**
     * Returns the key at the specified index.
     *
     * @param index the index of the key to retrieve
     * @return the key at the given index, or {@code null} if out of range
     */
    public String key(final int index) {
        if (index >= 0) {
            synchronized (store_) {
                int counter = 0;
                for (final String key : store_.keySet()) {
                    if (counter == index) {
                        return key;
                    }
                    counter++;
                }
            }
        }
        return null;
    }

    /**
     * Implementation of the JavaScript function {@code key(index)}.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return the key at the given index, or {@code null} if out of range
     */
    @JsxFunction(functionName = "key")
    public static String jsKey(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        checkArguments(args, 1, "key");
        final Storage storage = getStorage(thisObj);
        final long index = JavaScriptEngine.toUint32(args[0]);
        if (index > Integer.MAX_VALUE) {
            return null;
        }
        return storage.key((int) index);
    }

    /**
     * Returns the value associated with the given key.
     *
     * @param key the key of the item to retrieve
     * @return the value for the given key, or {@code null} if not found
     */
    public Object getItem(final String key) {
        synchronized (store_) {
            return store_.get(key);
        }
    }

    /**
     * Implementation of the JavaScript function {@code getItem(key)}.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     * @return the value for the given key, or {@code null} if not found
     */
    @JsxFunction(functionName = "getItem")
    public static Object jsGetItem(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        checkArguments(args, 1, "getItem");
        return getStorage(thisObj).getItem(JavaScriptEngine.toString(args[0]));
    }

    /**
     * Sets the value for the given key.
     * The quota is shared by all {@code Storage} objects using the same store
     * and counts the length of the keys and of the values.
     *
     * @param key the key of the item to set
     * @param data the new value
     */
    public void setItem(final String key, final String data) {
        final String oldData;
        synchronized (store_) {
            oldData = store_.get(key);
            if (data.equals(oldData)) {
                return;
            }

            long storeSize = key.length() + data.length();
            for (final Map.Entry<String, String> entry : store_.entrySet()) {
                if (!key.equals(entry.getKey())) {
                    storeSize += entry.getKey().length() + entry.getValue().length();
                }
            }
            if (storeSize > STORE_SIZE_LIMIT) {
                throw JavaScriptEngine.asJavaScriptException(this,
                        "Failed to execute 'setItem' on 'Storage': "
                                + "Setting the value of '" + key + "' exceeded the quota.",
                        DOMException.QUOTA_EXCEEDED_ERR);
            }
            store_.put(key, data);
        }
        fireStorageEvent(key, oldData, data);
    }

    /**
     * Implementation of the JavaScript function {@code setItem(key, value)}.
     *
     * @param context the context
     * @param scope the scope
     * @param thisObj this object
     * @param args the arguments
     * @param function the function
     */
    @JsxFunction(functionName = "setItem")
    public static void jsSetItem(final Context context, final VarScope scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        checkArguments(args, 2, "setItem");
        getStorage(thisObj).setItem(JavaScriptEngine.toString(args[0]), JavaScriptEngine.toString(args[1]));
    }

    /**
     * Removes all items from this storage.
     */
    @JsxFunction
    public void clear() {
        synchronized (store_) {
            if (store_.isEmpty()) {
                return;
            }
            store_.clear();
        }
        fireStorageEvent(null, null, null);
    }

    private static Storage getStorage(final Scriptable thisObj) {
        if (thisObj instanceof Storage storage && storage.store_ != null) {
            return storage;
        }
        throw JavaScriptEngine.typeError("Illegal invocation");
    }

    private static void checkArguments(final Object[] args, final int required, final String functionName) {
        if (args.length < required) {
            throw JavaScriptEngine.typeError("Failed to execute '" + functionName + "' on 'Storage': "
                    + required + " argument" + (required > 1 ? "s" : "") + " required, but only "
                    + args.length + " present.");
        }
    }

    /**
     * Queues a {@code storage} event for every other window (including frames)
     * sharing the same storage area.
     *
     * @param key the changed key or {@code null} for clear()
     * @param oldValue the old value or {@code null}
     * @param newValue the new value or {@code null}
     */
    private void fireStorageEvent(final String key, final String oldValue, final String newValue) {
        if (type_ == null) {
            return;
        }

        final Window window = getWindow();
        final WebWindow sourceWindow = window.getWebWindow();
        final Page sourcePage = sourceWindow.getEnclosedPage();
        if (sourcePage == null || sourcePage.getUrl() == null) {
            return;
        }
        final String url = sourcePage.getUrl().toExternalForm();

        final WebClient webClient = sourceWindow.getWebClient();
        final StorageHolder storageHolder = webClient.getStorageHolder();
        final AbstractJavaScriptEngine<?> jsEngine = webClient.getJavaScriptEngine();

        for (final WebWindow webWindow : webClient.getWebWindows()) {
            if (webWindow == sourceWindow) {
                continue;
            }
            if (type_ == Type.SESSION_STORAGE && webWindow.getTopWindow() != sourceWindow.getTopWindow()) {
                continue;
            }
            if (!(webWindow.getEnclosedPage() instanceof HtmlPage page)) {
                continue;
            }
            if (storageHolder.getStore(type_, page) != store_) {
                continue;
            }
            final Window receiver = webWindow.getScriptableObject();
            if (receiver == null) {
                continue;
            }

            final PostponedAction action = new PostponedAction(page, "Storage event") {
                @Override
                public void execute() {
                    final StorageEvent event = new StorageEvent();
                    event.setParentScope(getTopLevelScope(receiver.getParentScope()));
                    event.setPrototype(receiver.getPrototype(StorageEvent.class));
                    event.initStorageEvent(Event.TYPE_STORAGE, false, false,
                            key, oldValue, newValue, url, receiver.getStorage(type_));

                    final HtmlUnitContextFactory cf = jsEngine.getContextFactory();
                    cf.call(cx -> receiver.dispatchEvent(event));
                }
            };
            jsEngine.addPostponedAction(action);
        }
    }
}
