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
package org.htmlunit.javascript.host.security;

import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * JavaScript host object for {@code AuthenticatorResponse} (Web Authentication).
 *
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/AuthenticatorResponse">MDN Documentation</a>
 * @see <a href="https://www.w3.org/TR/webauthn-3/#authenticatorresponse">Web Authentication</a>
 */
@JsxClass
public class AuthenticatorResponse extends HtmlUnitScriptable {

    private byte[] clientDataJSON_;
    private NativeArrayBuffer clientDataJSONBuffer_;

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Sets the client data.
     * @param clientDataJSON the JSON serialization of the client data (utf-8)
     */
    protected void setClientDataJSON(final byte[] clientDataJSON) {
        clientDataJSON_ = ArrayBuffers.copy(clientDataJSON);
    }

    /**
     * Returns the JSON serialization of the client data passed to the authenticator.
     * @return the client data as ArrayBuffer
     */
    @JsxGetter
    public NativeArrayBuffer getClientDataJSON() {
        if (clientDataJSONBuffer_ == null) {
            clientDataJSONBuffer_ = ArrayBuffers.create(getParentScope(), clientDataJSON_);
        }
        return clientDataJSONBuffer_;
    }
}
