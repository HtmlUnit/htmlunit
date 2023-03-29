/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.javascript.HtmlUnitScriptable;
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
    public SubtleCrypto() {
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public void jsConstructor() {
        throw Context.reportRuntimeError("Illegal constructor.");
    }

    private Object notImplemented() {
        return setupRejectedPromise(() ->
                new DOMException("Operation is not supported", DOMException.NOT_SUPPORTED_ERR));
    }

    @JsxFunction
    public Object encrypt() {
        return notImplemented();
    }

    @JsxFunction
    public Object decrypt() {
        return notImplemented();
    }

    @JsxFunction
    public Object sign() {
        return notImplemented();
    }

    @JsxFunction
    public Object verify() {
        return notImplemented();
    }

    @JsxFunction
    public Object digest() {
        return notImplemented();
    }

    @JsxFunction
    public Object generateKey() {
        return notImplemented();
    }

    @JsxFunction
    public Object deriveKey() {
        return notImplemented();
    }

    @JsxFunction
    public Object deriveBits() {
        return notImplemented();
    }

    @JsxFunction
    public Object importKey() {
        return notImplemented();
    }

    @JsxFunction
    public Object exportKey() {
        return notImplemented();
    }

    @JsxFunction
    public Object wrapKey() {
        return notImplemented();
    }

    @JsxFunction
    public Object unwrapKey() {
        return notImplemented();
    }
}
