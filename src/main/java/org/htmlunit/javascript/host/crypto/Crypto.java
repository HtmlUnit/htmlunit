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
package org.htmlunit.javascript.host.crypto;

import java.security.SecureRandom;
import java.util.UUID;

import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;
import org.htmlunit.corejs.javascript.typedarrays.NativeBigInt64Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeBigUint64Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeInt16Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeInt32Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeInt8Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeUint16Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeUint32Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeUint8Array;
import org.htmlunit.corejs.javascript.typedarrays.NativeUint8ClampedArray;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.dom.DOMException;

/**
 * A JavaScript object for {@code Crypto}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Crypto">MDN Documentation</a>
 */
@JsxClass
public class Crypto extends HtmlUnitScriptable {

    static final SecureRandom RANDOM = new SecureRandom();
    private SubtleCrypto subtle_;

    /**
     * Creates an instance.
     */
    public Crypto() {
        super();
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Facility constructor.
     * @param window the owning window
     */
    public Crypto(final Window window) {
        this();
        setParentScope(getTopLevelScope(window.getParentScope()));
        setPrototype(window.getPrototype(Crypto.class));
    }

    /**
     * Fills array with random values.
     * @param array the array to fill
     * @return the modified array
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/RandomSource/getRandomValues">MDN Doc</a>
     */
    @JsxFunction
    public NativeArrayBufferView getRandomValues(final Object array) {
        if (!(array instanceof NativeArrayBufferView view)) {
            throw JavaScriptEngine.typeError(
                    "Argument 1 of Crypto.getRandomValues does not implement interface ArrayBufferView.");
        }
        if (!isIntegerArray(view)) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "Failed to execute 'getRandomValues' on 'Crypto': "
                            + "The provided ArrayBufferView is of type '" + view.getClassName()
                            + "', which is not an integer array type.",
                    DOMException.TYPE_MISMATCH_ERR);
        }

        // a view on a detached buffer has a byte length of 0; there is nothing to fill
        // (getByteLength() still reports the original length and getByteBuffer() is null)
        if (view.getBuffer().isDetached()) {
            return view;
        }

        if (view.getByteLength() > 65_536) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "Error: Failed to execute 'getRandomValues' on 'Crypto': "
                            + "The ArrayBufferView's byte length "
                            + "(" + view.getByteLength() + ") exceeds the number of bytes "
                            + "of entropy available via this API (65536).",
                    DOMException.QUOTA_EXCEEDED_ERR);
        }

        // fill the underlying bytes; writing the elements would convert (e.g. clamp) the values
        final byte[] bytes = new byte[view.getByteLength()];
        if (bytes.length > 0) {
            RANDOM.nextBytes(bytes);
            view.getBuffer().getByteBuffer().put(view.getByteOffset(), bytes);
        }
        return view;
    }

    private static boolean isIntegerArray(final NativeArrayBufferView view) {
        return view instanceof NativeInt8Array
                || view instanceof NativeUint8Array
                || view instanceof NativeUint8ClampedArray
                || view instanceof NativeInt16Array
                || view instanceof NativeUint16Array
                || view instanceof NativeInt32Array
                || view instanceof NativeUint32Array
                || view instanceof NativeBigInt64Array
                || view instanceof NativeBigUint64Array;
    }

    /**
     * Returns the {@code subtle} property.
     * @return the {@code subtle} property
     */
    @JsxGetter
    public SubtleCrypto getSubtle() {
        if (subtle_ != null) {
            return subtle_;
        }
        final SubtleCrypto subtle = new SubtleCrypto();
        subtle.setParentScope(getParentScope());
        subtle.setPrototype(getWindow().getPrototype(SubtleCrypto.class));
        subtle_ = subtle;
        return subtle_;
    }

    /**
     * Generates a random UUID.
     * @return a v4 UUID generated using a cryptographically secure random number generator
     */
    @JsxFunction
    public String randomUUID() {
        // java.util.UUID creates a version 4 (random) UUID using a cryptographically
        // secure random number generator, formatted with lowercase hex digits
        return UUID.randomUUID().toString();
    }
}
