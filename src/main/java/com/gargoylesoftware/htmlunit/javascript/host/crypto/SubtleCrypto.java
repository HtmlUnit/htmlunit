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
package com.gargoylesoftware.htmlunit.javascript.host.crypto;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Promise;
import com.gargoylesoftware.htmlunit.javascript.host.dom.DOMException;

/**
 * A JavaScript object for {@code SubtleCrypto}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
@JsxClass
public class SubtleCrypto extends SimpleScriptable {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public SubtleCrypto() {
    }

    private Promise notImplemented() {
        return Promise.reject(null, this,
                new Object[] {new DOMException("Operation is not supported", DOMException.NOT_SUPPORTED_ERR)}, null);
    }

    @JsxFunction
    public Promise encrypt() {
        return notImplemented();
    }

    @JsxFunction
    public Promise decrypt() {
        return notImplemented();
    }

    @JsxFunction
    public Promise sign() {
        return notImplemented();
    }

    @JsxFunction
    public Promise verify() {
        return notImplemented();
    }

    @JsxFunction
    public Promise digest() {
        return notImplemented();
    }

    @JsxFunction
    public Promise generateKey() {
        return notImplemented();
    }

    @JsxFunction
    public Promise deriveKey() {
        return notImplemented();
    }

    @JsxFunction
    public Promise importKey() {
        return notImplemented();
    }

    @JsxFunction
    public Promise exportKey() {
        return notImplemented();
    }

    @JsxFunction
    public Promise wrapKey() {
        return notImplemented();
    }

    @JsxFunction
    public Promise unwrapKey() {
        return notImplemented();
    }
}
