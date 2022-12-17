/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.crypto;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import java.security.SecureRandom;
import java.util.Locale;

import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeTypedArrayView;

/**
 * A JavaScript object for {@code Crypto}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@JsxClass
public class Crypto extends HtmlUnitScriptable {

    static final SecureRandom RANDOM = new SecureRandom();

    /**
     * Creates an instance.
     */
    public Crypto() {
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public void jsConstructor() {
        throw Context.reportRuntimeError("Illegal constructor.");
    }

    /**
     * Facility constructor.
     * @param window the owning window
     */
    public Crypto(final Window window) {
        setParentScope(window);
        setPrototype(window.getPrototype(Crypto.class));
    }

    /**
     * Fills array with random values.
     * @param array the array to fill
     * @return the modified array
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/RandomSource/getRandomValues">MDN Doc</a>
     */
    @JsxFunction
    public NativeTypedArrayView<?> getRandomValues(final NativeTypedArrayView<?> array) {
        if (array == null) {
            throw ScriptRuntime.typeError("Argument 1 of Crypto.getRandomValues is not an object.");
        }
        if (array.getByteLength() > 65_536) {
            throw Context.reportRuntimeError("Error: Failed to execute 'getRandomValues' on 'Crypto': "
                    + "The ArrayBufferView's byte length "
                    + "(" + array.getByteLength() + ") exceeds the number of bytes "
                    + "of entropy available via this API (65536).");
        }

        for (int i = 0; i < array.getByteLength() / array.getBytesPerElement(); i++) {
            array.put(i, array, RANDOM.nextInt());
        }
        return array;
    }

    /**
     * Returns the {@code subtle} property.
     * @return the {@code stuble} property
     */
    @JsxGetter({CHROME, EDGE, FF, FF_ESR})
    public SubtleCrypto getSubtle() {
        final SubtleCrypto stuble = new SubtleCrypto();
        final Window window = getWindow();
        stuble.setParentScope(window);
        stuble.setPrototype(window.getPrototype(SubtleCrypto.class));
        return stuble;
    }

    @JsxFunction
    public String randomUUID() {
        // Let bytes be a byte sequence of length 16.
        // Fill bytes with cryptographically secure random bytes.
        final byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);

        // Set the 4 most significant bits of bytes[6], which represent the UUID version, to 0100.
        bytes[6] = (byte) (bytes[6] | 0b01000000);
        bytes[6] = (byte) (bytes[6] & 0b01001111);
        // Set the 2 most significant bits of bytes[8], which represent the UUID variant, to 10.
        bytes[8] = (byte) (bytes[8] | 0b10000000);
        bytes[8] = (byte) (bytes[6] & 0b10111111);

        final StringBuilder result = new StringBuilder()
                                            .append(toHex(bytes[0]))
                                            .append(toHex(bytes[1]))
                                            .append(toHex(bytes[2]))
                                            .append(toHex(bytes[3]))
                                            .append('-')
                                            .append(toHex(bytes[4]))
                                            .append(toHex(bytes[5]))
                                            .append('-')
                                            .append(toHex(bytes[6]))
                                            .append(toHex(bytes[7]))
                                            .append('-')
                                            .append(toHex(bytes[8]))
                                            .append(toHex(bytes[9]))
                                            .append('-')
                                            .append(toHex(bytes[10]))
                                            .append(toHex(bytes[11]))
                                            .append(toHex(bytes[12]))
                                            .append(toHex(bytes[13]))
                                            .append(toHex(bytes[14]))
                                            .append(toHex(bytes[15]));
        return result.toString();
    }

    private static String toHex(final byte b) {
        return String.format("%02X ", b).trim().toLowerCase(Locale.ROOT);
    }
}
