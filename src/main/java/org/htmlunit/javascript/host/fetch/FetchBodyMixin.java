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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.IOException;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativeArrayBuffer;
import org.htmlunit.corejs.javascript.NativePromise;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.corejs.javascript.json.JsonParser;
import org.htmlunit.corejs.javascript.json.JsonParser.ParseException;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.javascript.host.xml.FormData;

/**
 * Shared body handling for fetch Request/Response objects.
 *
 * @author Ronald Brill
 */
public class FetchBodyMixin extends HtmlUnitScriptable {

    private byte[] bodyBytes_ = new byte[0];
    private String contentType_;
    private boolean bodyUsed_;

    @JsxGetter
    public Object getBody() {
        return null;
    }

    @JsxGetter
    public boolean isBodyUsed() {
        return bodyUsed_;
    }

    @JsxFunction
    public NativePromise text() {
        if (bodyUsed_) {
            return rejectedTypeErrorPromise("Body has already been consumed.");
        }
        bodyUsed_ = true;
        return setupPromise(() -> new String(bodyBytes_, UTF_8));
    }

    @JsxFunction
    public NativePromise json() {
        if (bodyUsed_) {
            return rejectedTypeErrorPromise("Body has already been consumed.");
        }
        bodyUsed_ = true;
        try {
            final String json = new String(bodyBytes_, UTF_8);
            final Object parsed = new JsonParser(Context.getCurrentContext(), getParentScope()).parseValue(json);
            return setupPromise(() -> parsed);
        }
        catch (final ParseException e) {
            return rejectedTypeErrorPromise(e.getMessage());
        }
    }

    @JsxFunction
    public NativePromise arrayBuffer() {
        if (bodyUsed_) {
            return rejectedTypeErrorPromise("Body has already been consumed.");
        }
        bodyUsed_ = true;
        return setupPromise(() -> {
            final NativeArrayBuffer buffer = new NativeArrayBuffer(bodyBytes_.length);
            System.arraycopy(bodyBytes_, 0, buffer.getBuffer(), 0, bodyBytes_.length);
            buffer.setParentScope(getParentScope());
            buffer.setPrototype(ScriptableObject.getClassPrototype(getParentScope(), buffer.getClassName()));
            return buffer;
        });
    }

    @JsxFunction
    public NativePromise blob() {
        if (bodyUsed_) {
            return rejectedTypeErrorPromise("Body has already been consumed.");
        }
        bodyUsed_ = true;
        return setupPromise(() -> {
            final Blob blob = new Blob(bodyBytes_, contentType_);
            blob.setParentScope(getParentScope());
            blob.setPrototype(getPrototype(Blob.class));
            return blob;
        });
    }

    @JsxFunction
    public NativePromise formData() {
        if (bodyUsed_) {
            return rejectedTypeErrorPromise("Body has already been consumed.");
        }
        bodyUsed_ = true;
        return setupPromise(this::buildFormData);
    }

    protected byte[] getBodyBytes() {
        return bodyBytes_;
    }

    protected String getBodyContentType() {
        return contentType_;
    }

    protected void setBody(final byte[] bodyBytes, final String contentType) {
        bodyBytes_ = bodyBytes != null ? bodyBytes.clone() : new byte[0];
        contentType_ = contentType;
        bodyUsed_ = false;
    }

    protected void copyBodyTo(final FetchBodyMixin other) {
        other.bodyBytes_ = bodyBytes_.clone();
        other.contentType_ = contentType_;
        other.bodyUsed_ = false;
    }

    protected Object createTypeErrorObject(final String message) {
        final VarScope scope = ScriptableObject.getTopLevelScope(getParentScope());
        final Object typeError = ScriptableObject.getProperty(scope, "TypeError");
        if (typeError instanceof Function function) {
            return function.construct(Context.getCurrentContext(), scope, new Object[] {message});
        }
        return message;
    }

    protected NativePromise rejectedTypeErrorPromise(final String message) {
        return setupRejectedPromise(() -> createTypeErrorObject(message));
    }

    private FormData buildFormData() throws IOException {
        final FormData formData = new FormData();
        formData.setParentScope(getParentScope());
        formData.setPrototype(getPrototype(FormData.class));
        formData.jsConstructor(JavaScriptEngine.UNDEFINED);

        final String contentType = contentType_ != null ? contentType_.toLowerCase() : "";
        if (contentType.startsWith("multipart/form-data;")) {
            final String marker = "boundary=";
            final int boundaryStart = contentType.indexOf(marker);
            if (boundaryStart > -1) {
                final String boundary = contentType.substring(boundaryStart + marker.length()).trim();
                final String body = new String(bodyBytes_, UTF_8);
                final String[] parts = body.split("--" + boundary);
                for (final String part : parts) {
                    final int headerEnd = part.indexOf("\r\n\r\n");
                    if (headerEnd < 0) {
                        continue;
                    }
                    final String headers = part.substring(0, headerEnd);
                    final String data = part.substring(headerEnd + 4).replaceFirst("\r\n$", "");
                    final String name = extractName(headers);
                    if (name != null) {
                        formData.append(name, data, JavaScriptEngine.UNDEFINED);
                    }
                }
                return formData;
            }
        }

        final String body = new String(bodyBytes_, UTF_8);
        if (!body.isEmpty()) {
            final String[] pairs = body.split("&");
            for (final String pair : pairs) {
                final int equals = pair.indexOf('=');
                final String name = equals < 0 ? pair : pair.substring(0, equals);
                final String value = equals < 0 ? "" : pair.substring(equals + 1);
                formData.append(org.htmlunit.util.UrlUtils.decode(name),
                        org.htmlunit.util.UrlUtils.decode(value), JavaScriptEngine.UNDEFINED);
            }
        }

        return formData;
    }

    private static String extractName(final String headers) {
        final String marker = "name=\"";
        final int start = headers.indexOf(marker);
        if (start < 0) {
            return null;
        }
        final int end = headers.indexOf('"', start + marker.length());
        if (end < 0) {
            return null;
        }
        return headers.substring(start + marker.length(), end);
    }
}
