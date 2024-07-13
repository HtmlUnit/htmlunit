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
package org.htmlunit.javascript.host.crypto;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.dom.DOMException;

/**
 * A JavaScript object for {@code SubtleCrypto}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
@JsxClass
public class SubtleCrypto extends HtmlUnitScriptable {

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.reportRuntimeError("Illegal constructor.");
    }

    private Object notImplemented() {
        return setupRejectedPromise(() ->
                new DOMException("Operation is not supported", DOMException.NOT_SUPPORTED_ERR));
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with the encrypted data (also known as "ciphertext")
     */
    @JsxFunction
    public Object encrypt() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with the decrypted data (also known as "plaintext")
     */
    @JsxFunction
    public Object decrypt() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with the signature
     */
    @JsxFunction
    public Object sign() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with a boolean value indicating whether the signature is valid
     */
    @JsxFunction
    public Object verify() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with the digest
     */
    @JsxFunction
    public Object digest() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a new key (for symmetric algorithms) or key pair (for public-key algorithms)
     */
    @JsxFunction
    public Object generateKey() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with a CryptoKey object representing the new key
     */
    @JsxFunction
    public Object deriveKey() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise which will be fulfilled with an ArrayBuffer containing the derived bits
     */
    @JsxFunction
    public Object deriveBits() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a CryptoKey object that you can use in the Web Crypto API
     */
    @JsxFunction
    public Object importKey() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return the key in an external, portable format
     */
    @JsxFunction
    public Object exportKey() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise that fulfills with an ArrayBuffer containing the encrypted exported key
     */
    @JsxFunction
    public Object wrapKey() {
        return notImplemented();
    }

    /**
     * Not yet implemented.
     *
     * @return a Promise that fulfills with the unwrapped key as a CryptoKey object
     */
    @JsxFunction
    public Object unwrapKey() {
        return notImplemented();
    }
}
