/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Promise;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;

/**
 * A JavaScript object for {@code Blob}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class Blob extends SimpleScriptable {
    private static final String OPTIONS_TYPE_NAME = "type";
    //default according to https://developer.mozilla.org/en-US/docs/Web/API/File/File
    private static final String OPTIONS_TYPE_DEFAULT = "";
    private static final String OPTIONS_LASTMODIFIED = "lastModified";

    protected abstract static class Backend implements Serializable {
        abstract String getName();
        abstract long getLastModified();
        abstract long getSize();
        abstract String getType(BrowserVersion browserVersion);
        abstract String getText() throws IOException;

        Backend() {
        }

        // TODO
        abstract java.io.File getFile();
    }

    protected static class InMemoryBackend extends Backend {
        private final String fileName_;
        private final String type_;
        private final long lastModified_;
        private final byte[] bytes_;

        protected InMemoryBackend(final NativeArray fileBits, final String fileName,
                final String type, final long lastModified) {
            fileName_ = fileName;
            type_ = type;
            lastModified_ = lastModified;

            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            if (fileBits != null) {
                for (long i = 0; i < fileBits.getLength(); i++) {
                    final Object fileBit = fileBits.get(i);
                    if (fileBit instanceof NativeArrayBuffer) {
                        final byte[] bytes = ((NativeArrayBuffer) fileBit).getBuffer();
                        out.write(bytes, 0, bytes.length);
                    }
                    else if (fileBit instanceof NativeArrayBufferView) {
                        final byte[] bytes = ((NativeArrayBufferView) fileBit).getBuffer().getBuffer();
                        out.write(bytes, 0, bytes.length);
                    }
                    else {
                        final String bits = Context.toString(fileBits.get(i));
                        // Todo normalize line breaks
                        final byte[] bytes = bits.getBytes(StandardCharsets.UTF_8);
                        out.write(bytes, 0, bytes.length);
                    }
                }
            }
            bytes_ = out.toByteArray();
        }

        @Override
        public String getName() {
            return fileName_;
        }

        @Override
        public long getLastModified() {
            return lastModified_;
        }

        @Override
        public long getSize() {
            return bytes_.length;
        }

        @Override
        public String getType(final BrowserVersion browserVersion) {
            return type_;
        }

        @Override
        public String getText() throws IOException {
            return new String(bytes_, StandardCharsets.UTF_8);
        }

        @Override
        public java.io.File getFile() {
            throw new UnsupportedOperationException(
                    "com.gargoylesoftware.htmlunit.javascript.host.file.File.InMemoryBackend.getFile()");
        }
    }

    protected static String extractFileTypeOrDefault(final ScriptableObject properties) {
        if (properties == null || Undefined.isUndefined(properties)) {
            return OPTIONS_TYPE_DEFAULT;
        }

        final Object optionsType = properties.get(OPTIONS_TYPE_NAME, properties);
        if (optionsType != null && properties != Scriptable.NOT_FOUND
                && !Undefined.isUndefined(optionsType)) {
            return Context.toString(optionsType);
        }

        return OPTIONS_TYPE_DEFAULT;
    }

    protected static long extractLastModifiedOrDefault(final ScriptableObject properties) {
        if (properties == null || Undefined.isUndefined(properties)) {
            return System.currentTimeMillis();
        }

        final Object optionsType = properties.get(OPTIONS_LASTMODIFIED, properties);
        if (optionsType != null && properties != Scriptable.NOT_FOUND
                && !Undefined.isUndefined(optionsType)) {
            try {
                return Long.parseLong(Context.toString(optionsType));
            }
            catch (final NumberFormatException e) {
                // fall back to default
            }
        }

        return System.currentTimeMillis();
    }

    private Backend backend_;

    /**
     * Creates an instance.
     */
    public Blob() {
    }

    /**
     * Creates an instance.
     * @param fileBits the bits
     * @param properties the properties
     */
    @JsxConstructor
    public Blob(final NativeArray fileBits, final ScriptableObject properties) {
        NativeArray nativeBits = fileBits;
        if (Undefined.isUndefined(fileBits)) {
            nativeBits = null;
        }

        setBackend(new InMemoryBackend(nativeBits, null,
                            extractFileTypeOrDefault(properties),
                            extractLastModifiedOrDefault(properties)));
    }

    /**
     * Returns the {@code size} property.
     * @return the {@code size} property
     */
    @JsxGetter
    public long getSize() {
        return getBackend().getSize();
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return getBackend().getType(getBrowserVersion());
    }

    /**
     * @return a Promise that resolves with a string containing the
     * contents of the blob, interpreted as UTF-8.
     */
    @JsxFunction({CHROME, EDGE, FF})
    public Promise text() {
        try {
            return Promise.resolve(null, this, new Object[] {getBackend().getText()}, null);
        }
        catch (final IOException e) {
            return Promise.reject(null, this, new Object[] {e.getMessage()}, null);
        }
    }

    protected Backend getBackend() {
        return backend_;
    }

    protected void setBackend(final Backend backend) {
        backend_ = backend;
    }

}
