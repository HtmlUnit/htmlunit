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
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public Crypto() {
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
}
