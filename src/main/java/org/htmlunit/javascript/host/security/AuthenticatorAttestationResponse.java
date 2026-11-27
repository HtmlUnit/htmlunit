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

import java.util.List;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * JavaScript host object for {@code AuthenticatorAttestationResponse} (Web Authentication);
 * the response of {@code navigator.credentials.create()}.
 *
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/AuthenticatorAttestationResponse">
 *      MDN Documentation</a>
 * @see <a href="https://www.w3.org/TR/webauthn-3/#authenticatorattestationresponse">Web Authentication</a>
 */
@JsxClass
public class AuthenticatorAttestationResponse extends AuthenticatorResponse {

    private byte[] attestationObject_;
    private NativeArrayBuffer attestationObjectBuffer_;
    private byte[] authenticatorData_;
    private byte[] publicKey_;
    private long publicKeyAlgorithm_;
    private List<String> transports_ = List.of();

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
     * @param attestationObject the CBOR encoded attestation object
     * @param authenticatorData the authenticator data (part of the attestation object)
     * @param publicKey the DER encoded SubjectPublicKeyInfo of the new credential or {@code null}
     * @param publicKeyAlgorithm the COSE algorithm identifier of the new credential
     * @param transports the transports of the authenticator
     */
    void init(final byte[] clientDataJSON, final byte[] attestationObject, final byte[] authenticatorData,
            final byte[] publicKey, final long publicKeyAlgorithm, final List<String> transports) {
        setClientDataJSON(clientDataJSON);
        attestationObject_ = ArrayBuffers.copy(attestationObject);
        authenticatorData_ = ArrayBuffers.copy(authenticatorData);
        publicKey_ = ArrayBuffers.copy(publicKey);
        publicKeyAlgorithm_ = publicKeyAlgorithm;
        transports_ = List.copyOf(transports);
    }

    /**
     * Returns the attestation object (CBOR encoded).
     * @return the attestation object as ArrayBuffer
     */
    @JsxGetter
    public NativeArrayBuffer getAttestationObject() {
        if (attestationObjectBuffer_ == null) {
            attestationObjectBuffer_ = ArrayBuffers.create(getParentScope(), attestationObject_);
        }
        return attestationObjectBuffer_;
    }

    /**
     * Returns the authenticator data.
     * @return a new ArrayBuffer with the authenticator data
     */
    @JsxFunction
    public NativeArrayBuffer getAuthenticatorData() {
        return ArrayBuffers.create(getParentScope(), authenticatorData_);
    }

    /**
     * Returns the public key of the new credential.
     * @return a new ArrayBuffer with the DER encoded SubjectPublicKeyInfo or {@code null}
     */
    @JsxFunction
    public NativeArrayBuffer getPublicKey() {
        return ArrayBuffers.create(getParentScope(), publicKey_);
    }

    /**
     * Returns the COSE algorithm identifier of the new credential (e.g. -7 for ES256).
     * @return the algorithm identifier
     */
    @JsxFunction
    public long getPublicKeyAlgorithm() {
        return publicKeyAlgorithm_;
    }

    /**
     * Returns the transports of the authenticator (e.g. {@code usb}, {@code internal}).
     * @return a new array with the transports
     */
    @JsxFunction
    public Scriptable getTransports() {
        return JavaScriptEngine.newArray(getParentScope(), transports_.toArray());
    }
}
