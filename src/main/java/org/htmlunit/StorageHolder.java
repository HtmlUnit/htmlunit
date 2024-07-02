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
package org.htmlunit;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Holder for different types of storages.
 * <p><span style="color:red">Experimental API: May be changed in next release!</span></p>
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
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
    private transient Map<String, Map<String, String>> sessionStorage_ = new HashMap<>();

    /**
     * Gets the store of the give type for the page.
     * @param storageType the type
     * @param page the page
     * @return the store
     */
    public Map<String, String> getStore(final Type storageType, final Page page) {
        switch (storageType) {
            case LOCAL_STORAGE:
                return getLocalStorage(page.getUrl());

            case SESSION_STORAGE:
                return getSessionStorage(page.getEnclosingWindow());

            default:
                return null;
        }
    }

    /**
     * Gets the local storage (map).
     * @param url the page origin
     * @return the store
     */
    public Map<String, String> getLocalStorage(final URL url) {
        synchronized (localStorage_) {
            final String key = url.getProtocol() + "://" + url.getHost();
            return localStorage_.computeIfAbsent(key, k -> new LinkedHashMap<>());
        }
    }

    /**
     * Gets the local storage (map).
     * @param webWindow the window
     * @return the store
     */
    public Map<String, String> getSessionStorage(final WebWindow webWindow) {
        synchronized (sessionStorage_) {
            final WebWindow topWindow = webWindow.getTopWindow();
            final String key = Integer.toHexString(topWindow.hashCode());
            return sessionStorage_.computeIfAbsent(key, k -> new LinkedHashMap<>());
        }
    }

    private void readObject(final ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();

        sessionStorage_ = new HashMap<>();
    }
}
