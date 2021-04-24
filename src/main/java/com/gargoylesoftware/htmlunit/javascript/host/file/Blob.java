/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_BLOB_CONTENT_TYPE_CASE_SENSITIVE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.XHR_SEND_IGNORES_BLOB_MIMETYPE_AS_CONTENTTYPE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Promise;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
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

        abstract byte[] getBytes(int start, int end);

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

        protected InMemoryBackend(final byte[] bytes, final String fileName,
                final String type, final long lastModified) {
            fileName_ = fileName;
            type_ = type;
            lastModified_ = lastModified;
            bytes_ = bytes;
        }

        protected static InMemoryBackend create(final NativeArray fileBits, final String fileName,
                final String type, final long lastModified) {
            if (fileBits == null) {
                return new InMemoryBackend(new byte[0], fileName, type, lastModified);
            }

            final ByteArrayOutputStream out = new ByteArrayOutputStream();
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
                else if (fileBit instanceof Blob) {
                    final Blob blob = (Blob) fileBit;
                    final byte[] bytes = blob.getBackend().getBytes(0, (int) blob.getSize());
                    out.write(bytes, 0, bytes.length);
                }
                else {
                    final String bits = Context.toString(fileBits.get(i));
                    // Todo normalize line breaks
                    final byte[] bytes = bits.getBytes(StandardCharsets.UTF_8);
                    out.write(bytes, 0, bytes.length);
                }
            }
            return new InMemoryBackend(out.toByteArray(), fileName, type, lastModified);
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
            if (!browserVersion.hasFeature(JS_BLOB_CONTENT_TYPE_CASE_SENSITIVE)) {
                return type_.toLowerCase(Locale.ROOT);
            }
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

        @Override
        public byte[] getBytes(final int start, final int end) {
            final byte[] result = new byte[end - start];
            System.arraycopy(bytes_, start, result, 0, result.length);
            return result;
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

        setBackend(InMemoryBackend.create(nativeBits, null,
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

    @JsxFunction
    public Blob slice(final Object start, final Object end, final Object contentType) {
        final Blob blob = new Blob();
        blob.setParentScope(getParentScope());
        blob.setPrototype(getPrototype(Blob.class));

        final int size = (int) getSize();
        int usedStart = 0;
        int usedEnd = size;
        if (start != null && !Undefined.isUndefined(start)) {
            usedStart = ScriptRuntime.toInt32(start);
            if (usedStart < 0) {
                usedStart = size + usedStart;
            }
            usedStart = Math.max(0, usedStart);
        }

        if (end != null && !Undefined.isUndefined(end)) {
            usedEnd = ScriptRuntime.toInt32(end);
            if (usedEnd < 0) {
                usedEnd = size + usedEnd;
            }
            usedEnd = Math.min(size, usedEnd);
        }

        String usedContentType = "";
        if (contentType != null && !Undefined.isUndefined(contentType)) {
            usedContentType = ScriptRuntime.toString(contentType).toLowerCase(Locale.ROOT);
        }

        if (usedEnd <= usedStart || usedStart >= getSize()) {
            blob.setBackend(new InMemoryBackend(new byte[0], null, usedContentType, 0L));
            return blob;
        }

        blob.setBackend(new InMemoryBackend(getBackend().getBytes(usedStart, usedEnd), null, usedContentType, 0L));
        return blob;
    }

    /**
     * @return a Promise that resolves with a string containing the
     * contents of the blob, interpreted as UTF-8.
     */
    @JsxFunction({CHROME, EDGE, FF, FF78})
    public Promise text() {
        try {
            return Promise.resolve(null, this, new Object[] {getBackend().getText()}, null);
        }
        catch (final IOException e) {
            return Promise.reject(null, this, new Object[] {e.getMessage()}, null);
        }
    }

    public byte[] getBytes() {
        return getBackend().getBytes(0, (int) getBackend().getSize());
    }

    /**
     * Sets the specified request with the parameters in this {@code FormData}.
     * @param webRequest the web request to fill
     */
    public void fillRequest(final WebRequest webRequest) {
        webRequest.setRequestBody(new String(getBytes(), UTF_8));

        if (!getBrowserVersion().hasFeature(XHR_SEND_IGNORES_BLOB_MIMETYPE_AS_CONTENTTYPE)) {
            final String mimeType = getType();
            if (StringUtils.isNotBlank(mimeType)) {
                webRequest.setAdditionalHeader(HttpHeader.CONTENT_TYPE, mimeType);
            }
            webRequest.setEncodingType(null);
        }
    }

    protected Backend getBackend() {
        return backend_;
    }

    protected void setBackend(final Backend backend) {
        backend_ = backend;
    }

}
