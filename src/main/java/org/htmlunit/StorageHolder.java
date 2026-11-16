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
package org.htmlunit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.UrlUtils;

/**
 * Holder for different types of storages.
 * <p><span style="color:red">Experimental API: May be changed in next release!</span></p>
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Vidya m
 */
public class StorageHolder implements Serializable {

    /**
     * Type for Storage.
     */
    public enum Type {
        /** The type for window.localStorage. */
        LOCAL_STORAGE,
        /** The type for window.sessionStorage. */
        SESSION_STORAGE
    }

    /** Key of the session storage not (yet) bound to an origin. */
    private static final String UNBOUND_KEY = "";

    private final Map<String, Map<String, String>> localStorage_ = new HashMap<>();
    private transient Map<WebWindow, Map<String, Map<String, String>>> sessionStorage_ = new WeakHashMap<>();

    /**
     * Gets the store of the give type for the page.
     * The local storage is shared by all pages of the same origin; the session storage
     * is shared by all pages of the same origin inside the same top level window.
     * Pages without an origin of their own (e.g. about:blank iframes) are using
     * the origin of their creator.
     *
     * @param storageType the type
     * @param page the page
     * @return the store
     */
    public Map<String, String> getStore(final Type storageType, final Page page) {
        final URL originUrl = getOriginUrl(page);
        return switch (storageType) {
            case LOCAL_STORAGE -> getLocalStorage(originUrl);
            case SESSION_STORAGE -> getSessionStorage(page.getEnclosingWindow(), originUrl);
        };
    }

    /**
     * Gets the local storage (map).
     * @param url the page origin
     * @return the store
     */
    public Map<String, String> getLocalStorage(final URL url) {
        synchronized (localStorage_) {
            return localStorage_.computeIfAbsent(getOriginKey(url), k -> new LinkedHashMap<>());
        }
    }

    /**
     * Gets the session storage (map) of the window for the origin of the page
     * currently loaded into the window.
     *
     * <p>If the window has no page with an origin yet (no page or about:blank), a store
     * not bound to an origin is returned; this store is used as session storage
     * for the origin of the first page loaded into this window that accesses the
     * session storage. This allows to pre-populate the session storage before loading
     * the page; {@link #getSessionStorage(WebWindow, URL)} is the preferred way to do this.</p>
     *
     * @param webWindow the window
     * @return the store
     */
    public Map<String, String> getSessionStorage(final WebWindow webWindow) {
        final Page page = webWindow.getEnclosedPage();
        if (page == null || page.getUrl() == null) {
            return getUnboundSessionStorage(webWindow);
        }

        final URL originUrl = getOriginUrl(page);
        if (UrlUtils.ABOUT.equals(originUrl.getProtocol())) {
            return getUnboundSessionStorage(webWindow);
        }
        return getSessionStorage(webWindow, originUrl);
    }

    /**
     * Gets the session storage (map) of the window for the given origin.
     * The session storage is shared by all windows (including frames) of the
     * same top level window.
     * @param webWindow the window
     * @param url the origin
     * @return the store
     */
    public Map<String, String> getSessionStorage(final WebWindow webWindow, final URL url) {
        synchronized (sessionStorage_) {
            final Map<String, Map<String, String>> stores =
                    sessionStorage_.computeIfAbsent(webWindow.getTopWindow(), k -> new HashMap<>());
            final String key = getOriginKey(url);
            Map<String, String> store = stores.get(key);
            if (store == null) {
                // adopt the store pre-populated via getSessionStorage(WebWindow) if there is one
                if (!UrlUtils.ABOUT.equals(url.getProtocol())) {
                    store = stores.remove(UNBOUND_KEY);
                }
                if (store == null) {
                    store = new LinkedHashMap<>();
                }
                stores.put(key, store);
            }
            return store;
        }
    }

    private Map<String, String> getUnboundSessionStorage(final WebWindow webWindow) {
        synchronized (sessionStorage_) {
            return sessionStorage_
                    .computeIfAbsent(webWindow.getTopWindow(), k -> new HashMap<>())
                    .computeIfAbsent(UNBOUND_KEY, k -> new LinkedHashMap<>());
        }
    }

    /**
     * Copies the session storage of the opener's origin into the session storage
     * of a newly opened top level window (e.g. by window.open()).
     * Nothing is copied if the target window already has session storage for this origin.
     * @param opener the window that opened the new one
     * @param newWindow the new window
     */
    public void copySessionStorage(final WebWindow opener, final WebWindow newWindow) {
        final Page openerPage = opener.getEnclosedPage();
        if (openerPage == null || openerPage.getUrl() == null) {
            return;
        }
        final String key = getOriginKey(getOriginUrl(openerPage));

        synchronized (sessionStorage_) {
            final WebWindow openerTop = opener.getTopWindow();
            final WebWindow newTop = newWindow.getTopWindow();
            if (openerTop == newTop) {
                return;
            }

            final Map<String, Map<String, String>> openerStores = sessionStorage_.get(openerTop);
            if (openerStores == null) {
                return;
            }
            final Map<String, String> openerStore = openerStores.get(key);
            if (openerStore == null || openerStore.isEmpty()) {
                return;
            }

            final Map<String, Map<String, String>> newStores =
                    sessionStorage_.computeIfAbsent(newTop, k -> new HashMap<>());
            if (!newStores.containsKey(key)) {
                synchronized (openerStore) {
                    newStores.put(key, new LinkedHashMap<>(openerStore));
                }
            }
        }
    }

    private static URL getOriginUrl(final Page page) {
        if (page instanceof HtmlPage htmlPage) {
            return htmlPage.getOriginUrl();
        }
        return page.getUrl();
    }

    private static String getOriginKey(final URL url) {
        // the storage is per origin (scheme, host and port)
        int port = url.getPort();
        if (port == -1) {
            port = url.getDefaultPort();
        }
        return url.getProtocol() + "://" + url.getHost().toLowerCase(Locale.ROOT) + ":" + port;
    }

    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();

        sessionStorage_ = new WeakHashMap<>();
    }
}
