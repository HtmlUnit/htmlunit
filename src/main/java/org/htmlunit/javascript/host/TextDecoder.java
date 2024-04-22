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
package org.htmlunit.javascript.host;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;
import org.htmlunit.cyberneko.xerces.util.StandardEncodingTranslator;
import org.htmlunit.cyberneko.xerces.util.XUserDefinedInputStreamReader;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

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
     * Ctor.
     */
    public TextDecoder() {
    }

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

        NativeArrayBuffer arrayBuffer = null;
        if (buffer instanceof NativeArrayBuffer) {
            arrayBuffer = (NativeArrayBuffer) buffer;
        }
        else if (buffer instanceof NativeArrayBufferView) {
            arrayBuffer = ((NativeArrayBufferView) buffer).getBuffer();
        }

        if (arrayBuffer != null) {
            if (XUserDefinedInputStreamReader.USER_DEFINED.equalsIgnoreCase(whatwgEncoding_)) {
                try (ByteArrayInputStream bis = new ByteArrayInputStream(arrayBuffer.getBuffer())) {
                    try (Reader reader = new XUserDefinedInputStreamReader(bis)) {
                        return IOUtils.toString(reader);
                    }
                }
                catch (final IOException e) {
                    return "";
                }
            }

            final String ianaEncoding = StandardEncodingTranslator
                    .ENCODING_TO_IANA_ENCODING.getOrDefault(whatwgEncoding_, whatwgEncoding_);
            // Convert our IANA encoding names to Java charset names
            final String javaEncoding = StandardEncodingTranslator
                    .IANA_TO_JAVA_ENCODINGS.getOrDefault(ianaEncoding, ianaEncoding);

            return new String(arrayBuffer.getBuffer(), Charset.forName(javaEncoding));
        }

        throw JavaScriptEngine.typeError("Argument 1 of TextDecoder.decode could not be"
                                + " converted to any of: ArrayBufferView, ArrayBuffer.");
    }
}
