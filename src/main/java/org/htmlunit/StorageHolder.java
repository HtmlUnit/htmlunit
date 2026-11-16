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
     * @param webWindow the window
     * @return the store
     */
    public Map<String, String> getSessionStorage(final WebWindow webWindow) {
        return getSessionStorage(webWindow, getOriginUrl(webWindow.getEnclosedPage()));
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
            final WebWindow topWindow = webWindow.getTopWindow();
            return sessionStorage_
                    .computeIfAbsent(topWindow, k -> new HashMap<>())
                    .computeIfAbsent(getOriginKey(url), k -> new LinkedHashMap<>());
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
