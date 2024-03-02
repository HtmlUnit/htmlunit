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

import java.nio.charset.StandardCharsets;

import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.corejs.javascript.typedarrays.NativeUint8Array;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code TextEncoder}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class TextEncoder extends HtmlUnitScriptable {

    /**
     * Creates an instance.
     */
    public TextEncoder() {
    }

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
    }

    /**
     * @return always "utf-8"
     */
    @JsxGetter
    public String getEncoding() {
        return "utf-8";
    }

    /**
     * @param toEncode the string to encode
     * @return returns a Uint8Array containing the text given encoded .
     */
    @JsxFunction
    public NativeUint8Array encode(final Object toEncode) {
        if (JavaScriptEngine.isUndefined(toEncode)) {
            final NativeUint8Array result = new NativeUint8Array(0);
            result.setParentScope(getParentScope());
            result.setPrototype(ScriptableObject.getClassPrototype(getWindow(this), result.getClassName()));
            return result;
        }

        final String txt;
        if (toEncode == null) {
            txt = "null";
        }
        else {
            txt = JavaScriptEngine.toString(toEncode);
        }

        final byte[] bytes = txt.getBytes(StandardCharsets.UTF_8);

        final NativeArrayBuffer arrayBuffer = new NativeArrayBuffer(bytes.length);
        System.arraycopy(bytes, 0, arrayBuffer.getBuffer(), 0, bytes.length);

        final NativeUint8Array result = new NativeUint8Array(arrayBuffer, 0, bytes.length);
        result.setParentScope(getParentScope());
        result.setPrototype(ScriptableObject.getClassPrototype(getWindow(this), result.getClassName()));
        return result;
    }
}
