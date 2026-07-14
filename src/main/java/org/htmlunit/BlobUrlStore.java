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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.htmlunit.javascript.host.file.Blob;

/**
 * The user-agent-wide blob URL store defined by
 * <a href="https://w3c.github.io/FileAPI/#BlobURLStore">Blob URL Store</a>.
 *
 * @author Lai Quang Duong
 */
public class BlobUrlStore {

    private static final class Entry {
        private final Blob object_;
        private final Page owningPage_;

        Entry(final Blob object, final Page owningPage) {
            object_ = object;
            owningPage_ = owningPage;
        }
    }

    private final Map<String, Entry> store_ = new ConcurrentHashMap<>();

    /**
     * Adds an entry.
     * @param blobUrl the generated {@code blob:} URL
     * @param object the {@link Blob} (or {@code File}) the URL refers to
     * @param owningPage the page that created the URL
     */
    public void put(final String blobUrl, final Blob object, final Page owningPage) {
        store_.put(blobUrl, new Entry(object, owningPage));
    }

    /**
     * <a href="https://w3c.github.io/FileAPI/#blob-url-resolve">Resolve a blob URL</a>.
     * @param blobUrl the {@code blob:} URL to resolve
     * @return the stored {@link Blob}, or {@code null} if there is no entry
     */
    public Blob resolve(final String blobUrl) {
        final Entry entry = store_.get(excludeFragment(blobUrl));
        return entry == null ? null : entry.object_;
    }

    /**
     * Removes an entry.
     * @param blobUrl the {@code blob:} URL to remove
     */
    public void remove(final String blobUrl) {
        store_.remove(excludeFragment(blobUrl));
    }

    /**
     * See <a href="https://w3c.github.io/FileAPI/#lifeTime">Lifetime of blob URLs</a>.
     * @param owningPage the unloading page
     */
    void removeForPage(final Page owningPage) {
        store_.values().removeIf(entry -> entry.owningPage_ == owningPage);
    }

    /**
     * <span style="color:red">INTERNAL API - SUBJECT TO CHANGE AT ANY TIME - USE AT YOUR OWN RISK.</span><br>
     *
     * <p>Removes all entries.
     */
    public void clear() {
        store_.clear();
    }

    private static String excludeFragment(final String blobUrl) {
        final int fragmentIndex = blobUrl.indexOf('#');
        return fragmentIndex >= 0 ? blobUrl.substring(0, fragmentIndex) : blobUrl;
    }
}
