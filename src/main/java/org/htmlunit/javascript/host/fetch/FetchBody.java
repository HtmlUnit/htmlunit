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
package org.htmlunit.javascript.host.fetch;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Locale;
import java.util.UUID;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.json.JsonParser;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.host.ReadableStream;
import org.htmlunit.javascript.host.URLSearchParams;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.javascript.host.xml.FormData;
import org.htmlunit.util.KeyDataPair;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.UrlUtils;

/**
 * The body of a {@link Request} or a {@link Response} (the 'Body' mixin of the Fetch Standard):
 * the bytes, the content type determined when the body was extracted and the used state.
 *
 * @author Ronald Brill
 * @see <a href="https://fetch.spec.whatwg.org/#body-mixin">Fetch Standard - Body mixin</a>
 */
final class FetchBody implements Serializable {

    /** The kinds of consuming the body. */
    enum Kind { TEXT, JSON, ARRAY_BUFFER, BYTES, BLOB, FORM_DATA }

    private final byte[] bytes_;
    private final String contentType_;
    private boolean used_;
    private ReadableStream stream_;

    private FetchBody(final byte[] bytes, final String contentType) {
        bytes_ = bytes;
        contentType_ = contentType;
    }

    /**
     * Creates a body from the given bytes.
     *
     * @param bytes the bytes
     * @param contentType the content type or {@code null}
     * @return the body
     */
    static FetchBody of(final byte[] bytes, final String contentType) {
        return new FetchBody(bytes, contentType);
    }

    /**
     * Extracts a body from the given object (a {@code BodyInit}).
     *
     * @param init the object (string, URLSearchParams, Blob, ArrayBuffer, ArrayBufferView, FormData)
     * @return the body or {@code null} if init is {@code null} or {@code undefined}
     * @see <a href="https://fetch.spec.whatwg.org/#concept-bodyinit-extract">Fetch Standard - extract a body</a>
     */
    static FetchBody extract(final Object init) {
        if (init == null || JavaScriptEngine.isUndefined(init)) {
            return null;
        }

        if (init instanceof Blob blob) {
            final String type = blob.getType();
            return new FetchBody(blob.getBytes(), type == null || type.isEmpty() ? null : type);
        }
        if (init instanceof NativeArrayBuffer buffer) {
            return new FetchBody(toBytes(buffer.getByteBuffer(), 0, buffer.getByteBuffer().capacity()), null);
        }
        if (init instanceof NativeArrayBufferView view) {
            return new FetchBody(toBytes(view.getBuffer().getByteBuffer(),
                                        view.getByteOffset(), view.getByteLength()), null);
        }
        if (init instanceof URLSearchParams params) {
            return new FetchBody(params.jsToString().getBytes(UTF_8),
                                    "application/x-www-form-urlencoded;charset=UTF-8");
        }
        if (init instanceof FormData formData) {
            final String boundary = "----HtmlUnitFormBoundary" + UUID.randomUUID().toString().replace("-", "");
            return new FetchBody(toMultipart(formData, boundary), "multipart/form-data; boundary=" + boundary);
        }
        return new FetchBody(JavaScriptEngine.toString(init).getBytes(UTF_8), "text/plain;charset=UTF-8");
    }

    private static byte[] toBytes(final ByteBuffer buffer, final int offset, final int length) {
        final byte[] result = new byte[length];
        final ByteBuffer copy = buffer.duplicate();
        copy.position(offset);
        copy.get(result, 0, length);
        return result;
    }

    private static byte[] toMultipart(final FormData formData, final String boundary) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            for (final NameValuePair entry : formData.getEntries()) {
                out.write(("--" + boundary + "\r\n").getBytes(UTF_8));
                if (entry instanceof KeyDataPair file) {
                    final String fileName = file.getFileName() == null ? "blob" : file.getFileName();
                    out.write(("Content-Disposition: form-data; name=\"" + escapeName(entry.getName())
                            + "\"; filename=\"" + escapeName(fileName) + "\"\r\n").getBytes(UTF_8));
                    final String mimeType = file.getMimeType() == null || file.getMimeType().isEmpty()
                            ? "application/octet-stream" : file.getMimeType();
                    out.write(("Content-Type: " + mimeType + "\r\n\r\n").getBytes(UTF_8));
                    if (file.getData() != null) {
                        out.write(file.getData());
                    }
                    else if (file.getFile() != null) {
                        out.write(Files.readAllBytes(file.getFile().toPath()));
                    }
                }
                else {
                    out.write(("Content-Disposition: form-data; name=\"" + escapeName(entry.getName())
                            + "\"\r\n\r\n").getBytes(UTF_8));
                    out.write(entry.getValue().getBytes(UTF_8));
                }
                out.write("\r\n".getBytes(UTF_8));
            }
            out.write(("--" + boundary + "--\r\n").getBytes(UTF_8));
            return out.toByteArray();
        }
        catch (final IOException e) {
            throw JavaScriptEngine.throwAsScriptRuntimeEx(e);
        }
    }

    private static String escapeName(final String name) {
        return name.replace("\n", "%0A").replace("\r", "%0D").replace("\"", "%22");
    }

    /**
     * Returns the bytes.
     *
     * @return the bytes
     */
    byte[] getBytes() {
        return bytes_;
    }

    /**
     * Returns the content type determined when the body was extracted.
     *
     * @return the content type or {@code null}
     */
    String getContentType() {
        return contentType_;
    }

    /**
     * Returns whether the body was used (consumed).
     *
     * @return whether the body was used
     */
    boolean isUsed() {
        return used_;
    }

    /**
     * Marks the body as used (e.g. if the body was taken by another request).
     */
    void markUsed() {
        used_ = true;
    }

    /**
     * Returns a copy of this body (with the used state not set).
     *
     * @return the copy
     */
    FetchBody copy() {
        return new FetchBody(bytes_, contentType_);
    }

    /**
     * Returns the {@link ReadableStream} of this body; always the same object.
     *
     * @param owner the request or the response (for the scope and the prototype)
     * @return the stream
     */
    ReadableStream getStream(final HtmlUnitScriptable owner) {
        if (stream_ == null) {
            stream_ = new ReadableStream();
            stream_.setParentScope(owner.getParentScope());
            stream_.setPrototype(owner.getPrototype(ReadableStream.class));
        }
        return stream_;
    }

    /**
     * Consumes the body.
     *
     * @param body the body (may be {@code null})
     * @param kind the kind
     * @param owner the request or the response (for the scope and the prototypes)
     * @param mimeType the MIME type of the request or the response (from the Content-Type header)
     * @return the result
     * @throws org.htmlunit.corejs.javascript.EcmaError if the body was used already or can't be consumed
     * @see <a href="https://fetch.spec.whatwg.org/#concept-body-consume-body">Fetch Standard - consume body</a>
     */
    static Object consume(final FetchBody body, final Kind kind, final HtmlUnitScriptable owner,
            final String mimeType) {
        final byte[] bytes;
        if (body == null) {
            bytes = new byte[0];
        }
        else {
            if (body.used_) {
                throw JavaScriptEngine.typeError("Body has already been consumed.");
            }
            body.used_ = true;
            bytes = body.bytes_;
        }

        switch (kind) {
            case TEXT:
                return decodeUtf8(bytes);

            case JSON:
                try {
                    return new JsonParser(Context.getCurrentContext(), owner.getParentScope())
                            .parseValue(decodeUtf8(bytes));
                }
                catch (final JsonParser.ParseException e) {
                    throw JavaScriptEngine.syntaxError("Unexpected token in JSON: " + e.getMessage());
                }

            case ARRAY_BUFFER:
                final NativeArrayBuffer buffer = new NativeArrayBuffer(bytes.length);
                buffer.getByteBuffer().put(bytes);
                buffer.setParentScope(owner.getParentScope());
                buffer.setPrototype(ScriptableObject.getClassPrototype(owner.getParentScope(), buffer.getClassName()));
                return buffer;

            case BYTES:
                return JavaScriptEngine.newUint8Array(ScriptableObject.getTopLevelScope(owner.getParentScope()),
                                                        bytes);

            case BLOB:
                final Blob blob = new Blob(bytes, mimeType == null ? "" : mimeType.toLowerCase(Locale.ROOT));
                blob.setParentScope(owner.getParentScope());
                blob.setPrototype(owner.getPrototype(Blob.class));
                return blob;

            case FORM_DATA:
            default:
                return toFormData(bytes, mimeType, owner);
        }
    }

    /**
     * UTF-8 decode: a leading BOM is removed, invalid sequences are replaced.
     *
     * @param bytes the bytes
     * @return the decoded string
     */
    private static String decodeUtf8(final byte[] bytes) {
        final String text = new String(bytes, UTF_8);
        if (!text.isEmpty() && text.charAt(0) == '\uFEFF') {
            return text.substring(1);
        }
        return text;
    }

    private static FormData toFormData(final byte[] bytes, final String mimeType, final HtmlUnitScriptable owner) {
        final String lcMimeType = mimeType == null ? "" : mimeType.toLowerCase(Locale.ROOT);

        final FormData formData = new FormData();
        formData.setParentScope(owner.getParentScope());
        formData.setPrototype(owner.getPrototype(FormData.class));

        if (lcMimeType.startsWith("application/x-www-form-urlencoded")) {
            final String text = decodeUtf8(bytes);
            for (final String part : text.split("&")) {
                if (part.isEmpty()) {
                    continue;
                }
                final int idx = part.indexOf('=');
                final String name = idx < 0 ? part : part.substring(0, idx);
                final String value = idx < 0 ? "" : part.substring(idx + 1);
                formData.append(UrlUtils.decode(name.replace('+', ' ')),
                        UrlUtils.decode(value.replace('+', ' ')), JavaScriptEngine.UNDEFINED);
            }
            return formData;
        }

        if (lcMimeType.startsWith("multipart/form-data")) {
            final String boundary = getBoundary(mimeType);
            if (boundary != null) {
                parseMultipart(bytes, boundary, formData, owner);
                return formData;
            }
        }

        throw JavaScriptEngine.typeError("Could not parse content as FormData.");
    }

    private static String getBoundary(final String mimeType) {
        for (final String param : mimeType.split(";")) {
            final String trimmed = param.trim();
            if (trimmed.toLowerCase(Locale.ROOT).startsWith("boundary=")) {
                String boundary = trimmed.substring("boundary=".length());
                if (boundary.length() > 1 && boundary.startsWith("\"") && boundary.endsWith("\"")) {
                    boundary = boundary.substring(1, boundary.length() - 1);
                }
                return boundary.isEmpty() ? null : boundary;
            }
        }
        return null;
    }

    /**
     * A simple multipart/form-data parser; the parts are separated by the boundary, every part
     * has headers (Content-Disposition, Content-Type) and the content.
     *
     * @param bytes the bytes
     * @param boundary the boundary
     * @param formData the form data to fill
     * @param owner the request or the response (for the scope and the prototypes)
     */
    private static void parseMultipart(final byte[] bytes, final String boundary, final FormData formData,
            final HtmlUnitScriptable owner) {
        // ISO-8859-1 keeps the bytes 1:1
        final String content = new String(bytes, ISO_8859_1);
        final String delimiter = "--" + boundary;

        int pos = content.indexOf(delimiter);
        if (pos < 0) {
            throw JavaScriptEngine.typeError("Could not parse content as FormData.");
        }
        while (true) {
            pos += delimiter.length();
            if (content.startsWith("--", pos)) {
                return;
            }
            if (content.startsWith("\r\n", pos)) {
                pos += 2;
            }
            final int headerEnd = content.indexOf("\r\n\r\n", pos);
            final int next = content.indexOf("\r\n" + delimiter, pos);
            if (headerEnd < 0 || next < 0 || headerEnd > next) {
                throw JavaScriptEngine.typeError("Could not parse content as FormData.");
            }

            String name = null;
            String fileName = null;
            String partType = "";
            for (final String header : content.substring(pos, headerEnd).split("\r\n")) {
                final int colon = header.indexOf(':');
                if (colon < 0) {
                    continue;
                }
                final String headerName = header.substring(0, colon).trim().toLowerCase(Locale.ROOT);
                final String headerValue = header.substring(colon + 1).trim();
                if ("content-disposition".equals(headerName)) {
                    name = getDispositionParameter(headerValue, "name");
                    fileName = getDispositionParameter(headerValue, "filename");
                }
                else if ("content-type".equals(headerName)) {
                    partType = headerValue;
                }
            }
            if (name == null) {
                throw JavaScriptEngine.typeError("Could not parse content as FormData.");
            }

            final byte[] partBytes = content.substring(headerEnd + 4, next).getBytes(
                                        ISO_8859_1);
            final String decodedName = new String(name.getBytes(ISO_8859_1), UTF_8);
            if (fileName == null) {
                formData.append(decodedName, new String(partBytes, UTF_8), JavaScriptEngine.UNDEFINED);
            }
            else {
                final Blob blob = new Blob(partBytes, partType);
                blob.setParentScope(owner.getParentScope());
                blob.setPrototype(owner.getPrototype(Blob.class));
                formData.append(decodedName, blob,
                        new String(fileName.getBytes(ISO_8859_1), UTF_8));
            }

            pos = next + 2;
        }
    }

    private static String getDispositionParameter(final String disposition, final String parameter) {
        for (final String param : disposition.split(";")) {
            final String trimmed = param.trim();
            final int eq = trimmed.indexOf('=');
            if (eq > 0 && parameter.equalsIgnoreCase(trimmed.substring(0, eq).trim())) {
                String value = trimmed.substring(eq + 1).trim();
                if (value.length() > 1 && value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }
                return value;
            }
        }
        return null;
    }
}
