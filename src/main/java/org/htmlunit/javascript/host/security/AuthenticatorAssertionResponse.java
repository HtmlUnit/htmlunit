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
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * JavaScript host object for {@code AuthenticatorAssertionResponse} (Web Authentication);
 * the response of {@code navigator.credentials.get()}.
 *
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/AuthenticatorAssertionResponse">
 *      MDN Documentation</a>
 * @see <a href="https://www.w3.org/TR/webauthn-3/#authenticatorassertionresponse">Web Authentication</a>
 */
@JsxClass
public class AuthenticatorAssertionResponse extends AuthenticatorResponse {

    private byte[] authenticatorData_;
    private NativeArrayBuffer authenticatorDataBuffer_;
    private byte[] signature_;
    private NativeArrayBuffer signatureBuffer_;
    private byte[] userHandle_;
    private NativeArrayBuffer userHandleBuffer_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Initializes this response.
     * @param clientDataJSON the JSON serialization of the client data (utf-8)
     * @param authenticatorData the authenticator data
     * @param signature the signature
     * @param userHandle the user handle or {@code null}
     */
    void init(final byte[] clientDataJSON, final byte[] authenticatorData, final byte[] signature,
            final byte[] userHandle) {
        setClientDataJSON(clientDataJSON);
        authenticatorData_ = ArrayBuffers.copy(authenticatorData);
        signature_ = ArrayBuffers.copy(signature);
        userHandle_ = ArrayBuffers.copy(userHandle);
    }

    /**
     * Returns the authenticator data.
     * @return the authenticator data as ArrayBuffer
     */
    @JsxGetter
    public NativeArrayBuffer getAuthenticatorData() {
        if (authenticatorDataBuffer_ == null) {
            authenticatorDataBuffer_ = ArrayBuffers.create(getParentScope(), authenticatorData_);
        }
        return authenticatorDataBuffer_;
    }

    /**
     * Returns the signature.
     * @return the signature as ArrayBuffer
     */
    @JsxGetter
    public NativeArrayBuffer getSignature() {
        if (signatureBuffer_ == null) {
            signatureBuffer_ = ArrayBuffers.create(getParentScope(), signature_);
        }
        return signatureBuffer_;
    }

    /**
     * Returns the user handle.
     * @return the user handle as ArrayBuffer or {@code null}
     */
    @JsxGetter
    public NativeArrayBuffer getUserHandle() {
        if (userHandleBuffer_ == null) {
            userHandleBuffer_ = ArrayBuffers.create(getParentScope(), userHandle_);
        }
        return userHandleBuffer_;
    }
}
