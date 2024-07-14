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
package org.htmlunit.javascript.host.file;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.commons.io.FileUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.NativeArray;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code File}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class File extends Blob {
    private static final DateTimeFormatter LAST_MODIFIED_DATE_FORMATTER
                            = DateTimeFormatter.ofPattern("EEE MMM dd yyyy HH:mm:ss 'GMT'Z");

    private static class FileBackend extends Backend {
        private final java.io.File file_;

        FileBackend(final String pathname) {
            super();
            file_ = new java.io.File(pathname);
        }

        @Override
        public String getName() {
            return file_.getName();
        }

        @Override
        public long getLastModified() {
            return file_.lastModified();
        }

        @Override
        public long getSize() {
            return file_.length();
        }

        @Override
        public String getType(final BrowserVersion browserVersion) {
            return browserVersion.getUploadMimeType(file_);
        }

        @Override
        public String getText() throws IOException {
            return FileUtils.readFileToString(file_, StandardCharsets.UTF_8);
        }

        @Override
        public java.io.File getFile() {
            return file_;
        }

        @Override
        byte[] getBytes(final int start, final int end) {
            final byte[] result = new byte[end - start];
            try {
                System.arraycopy(FileUtils.readFileToByteArray(file_), start, result, 0, result.length);
            }
            catch (final IOException e) {
                // TODO
            }
            return result;
        }
    }

    /**
     * Prototye ctor.
     */
    public File() {
        super();
    }

    /**
     * Creates an instance.
     * @param fileBits the bits
     * @param fileName the Name
     * @param properties the properties
     */
    @JsxConstructor
    public void jsConstructor(final NativeArray fileBits, final String fileName, final ScriptableObject properties) {
        if (fileBits == null
                || JavaScriptEngine.isUndefined(fileBits)
                || fileName == null
                || JavaScriptEngine.isUndefined(fileName)) {
            throw JavaScriptEngine.typeError("Failed to construct 'File': 2 arguments required.");
        }

        setBackend(InMemoryBackend.create(fileBits, fileName,
                            extractFileTypeOrDefault(properties),
                            extractLastModifiedOrDefault(properties)));
    }

    File(final String pathname) {
        super();
        setBackend(new FileBackend(pathname));
    }

    /**
     * Returns the {@code name} property.
     * @return the {@code name} property
     */
    @JsxGetter
    public String getName() {
        return getBackend().getName();
    }

    /**
     * Returns the {@code lastModifiedDate} property.
     * @return the {@code lastModifiedDate} property
     */
    @JsxGetter({CHROME, EDGE})
    public String getLastModifiedDate() {
        final Context cx = Context.getCurrentContext();
        final ZoneId zoneid = cx.getTimeZone().toZoneId();

        // strange only the time zone is locale dependent
        String date = LAST_MODIFIED_DATE_FORMATTER.format(Instant.ofEpochMilli(getLastModified()).atZone(zoneid));
        date += DateTimeFormatter.ofPattern(" (zzzz)", cx.getLocale())
                    .format(Instant.ofEpochMilli(getLastModified()).atZone(zoneid));
        return date;
    }

    /**
     * Returns the {@code lastModified} property.
     * @return the {@code lastModified} property
     */
    @JsxGetter
    public long getLastModified() {
        return getBackend().getLastModified();
    }

    /**
     * Returns the {@code webkitRelativePath} property.
     * @return the {@code webkitRelativePath} property
     */
    @JsxGetter
    public String getWebkitRelativePath() {
        return "";
    }

    /**
     * Slices the file.
     */
    @JsxFunction
    public void slice() {
        // nothing to do
    }

    /**
     * Returns the underlying file.
     * @return the underlying file
     */
    public java.io.File getFile() {
        return getBackend().getFile();
    }
}
