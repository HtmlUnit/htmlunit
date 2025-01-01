/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;
import org.htmlunit.cyberneko.xerces.util.StandardEncodingTranslator;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.util.XUserDefinedCharset;

/**
 * A JavaScript object for {@code TextDecoder}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class TextDecoder extends HtmlUnitScriptable {
    private String whatwgEncoding_ = "utf-8";

    /**
     * Creates an instance.
     * @param encodingLabel the encoding
     */
    @JsxConstructor
    public void jsConstructor(final Object encodingLabel) {
        if (JavaScriptEngine.isUndefined(encodingLabel)) {
            return;
        }

        String enc = JavaScriptEngine.toString(encodingLabel);
        enc = enc.trim().toLowerCase(Locale.ROOT);
        final String whatwgEncoding = StandardEncodingTranslator.ENCODING_FROM_LABEL.get(enc);

        if (whatwgEncoding == null
                || StandardEncodingTranslator.REPLACEMENT.equalsIgnoreCase(whatwgEncoding)) {
            throw JavaScriptEngine.rangeError("Failed to construct 'TextDecoder': The encoding label provided ('"
                        + encodingLabel + "') is invalid.");
        }

        whatwgEncoding_ = whatwgEncoding;
    }

    /**
     * @return the encoding - default is "utf-8"
     */
    @JsxGetter
    public String getEncoding() {
        return whatwgEncoding_;
    }

    /**
     * @param buffer to be decoded
     * @return returns the decoded string
     */
    @JsxFunction
    public String decode(final Object buffer) {
        if (JavaScriptEngine.isUndefined(buffer)) {
            return "";
        }

        if (buffer instanceof NativeArrayBuffer) {
            return new String(((NativeArrayBuffer) buffer).getBuffer(), getEncoding(whatwgEncoding_));
        }

        if (buffer instanceof NativeArrayBufferView) {
            final NativeArrayBufferView arrayBufferView = (NativeArrayBufferView) buffer;
            final NativeArrayBuffer arrayBuffer = arrayBufferView.getBuffer();
            if (arrayBuffer != null) {
                final int byteLength = arrayBufferView.getByteLength();
                final int byteOffset = arrayBufferView.getByteOffset();
                final byte[] backedBytes = arrayBuffer.getBuffer();
                final byte[] bytes = Arrays.copyOfRange(backedBytes, byteOffset, byteOffset + byteLength);
                return new String(bytes, getEncoding(whatwgEncoding_));
            }
        }

        throw JavaScriptEngine.typeError("Argument 1 of TextDecoder.decode could not be"
                                + " converted to any of: ArrayBufferView, ArrayBuffer.");
    }

    private Charset getEncoding(final String encodingLabel) {
        if (XUserDefinedCharset.NAME.equalsIgnoreCase(encodingLabel)) {
            return XUserDefinedCharset.INSTANCE;
        }

        final String ianaEncoding = StandardEncodingTranslator
                .ENCODING_TO_IANA_ENCODING.getOrDefault(encodingLabel, encodingLabel);
        // Convert our IANA encoding names to Java charset names
        final String javaEncoding = StandardEncodingTranslator
                .IANA_TO_JAVA_ENCODINGS.getOrDefault(ianaEncoding, ianaEncoding);

        return Charset.forName(javaEncoding);
    }
}
