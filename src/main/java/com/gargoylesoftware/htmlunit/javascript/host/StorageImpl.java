/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Storage.Type;

/**
 * The actual implementation of different types of Storage.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
final class StorageImpl implements Serializable {

    private static final Log LOG = LogFactory.getLog(Storage.class);

    private static StorageImpl SINGLETON_ = new StorageImpl();

    static {
        load();
    }

    private Map<String, Map<String, String>> globalStorage_
        = new HashMap<String, Map<String, String>>();

    private Map<String, Map<String, String>> localStorage_
        = new HashMap<String, Map<String, String>>();

    private transient Map<String, Map<String, String>> sessionStorage_
        = new HashMap<String, Map<String, String>>();

    /**
     * Private constructor; singleton.
     */
    private StorageImpl() {
        super();
    }

    static StorageImpl getInstance() {
        return SINGLETON_;
    }

    protected Map<String, String> getMap(final Type type, final HtmlPage page) {
        final String key = getKey(type, page);
        final Map<String, Map<String, String>> storage = getStorage(type);

        synchronized (storage) {
            Map<String, String> map = storage.get(key);
            if (map == null) {
                map = new LinkedHashMap<String, String>();
                storage.put(key, map);
            }
            return map;
        }
    }

    void clear(final Type type, final HtmlPage page) {
        final String key = getKey(type, page);
        final Map<String, Map<String, String>> storage = getStorage(type);

        synchronized (storage) {
            storage.remove(key);
        }
    }

    private String getKey(final Type type, final HtmlPage page) {
        switch (type) {
            case GLOBAL_STORAGE:
                return page.getUrl().getHost();

            case LOCAL_STORAGE:
                final URL url = page.getUrl();
                return url.getProtocol() + "://" + url.getHost() + ':' + url.getProtocol();

            case SESSION_STORAGE:
                final WebWindow topWindow = page.getEnclosingWindow().getTopWindow();
                return Integer.toHexString(topWindow.hashCode());

            default:
                return null;
        }
    }

    private Map<String, Map<String, String>> getStorage(final Type type) {
        switch (type) {
            case GLOBAL_STORAGE:
                return globalStorage_;

            case LOCAL_STORAGE:
                return localStorage_;

            case SESSION_STORAGE:
                return sessionStorage_;

            default:
                return null;
        }
    }

    private static void save() {
        try {
            final File file = new File(System.getProperty("user.home"), "htmlunit.storage");
            final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(SINGLETON_);
            out.close();
        }
        catch (final IOException e) {
            LOG.info("Could not save storage", e);
        }
    }

    private static void load() {
        try {
            final File file = new File(System.getProperty("user.home"), "htmlunit.storage");
            if (file.exists()) {
                final ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
                SINGLETON_ = (StorageImpl) in.readObject();
                SINGLETON_.sessionStorage_ = new HashMap<String, Map<String, String>>();
                in.close();
            }
        }
        catch (final Exception e) {
            LOG.info("Could not load storage", e);
        }
    }
}
