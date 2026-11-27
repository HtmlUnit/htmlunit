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

import java.util.Base64;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativePromise;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxStaticFunction;

/**
 * JavaScript host object for {@code PublicKeyCredential} (Web Authentication).
 *
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/PublicKeyCredential">MDN Documentation</a>
 * @see <a href="https://www.w3.org/TR/webauthn-3/#iface-pkcredential">Web Authentication</a>
 */
@JsxClass
public class PublicKeyCredential extends Credential {

    /** The type of all public key credentials. */
    public static final String TYPE = "public-key";

    private byte[] rawId_;
    private NativeArrayBuffer rawIdBuffer_;
    private AuthenticatorResponse response_;
    private String authenticatorAttachment_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Initializes this credential.
     * @param rawId the credential id
     * @param response the response of the authenticator
     * @param authenticatorAttachment {@code platform}, {@code cross-platform} or {@code null}
     */
    void init(final byte[] rawId, final AuthenticatorResponse response, final String authenticatorAttachment) {
        setIdAndType(Base64.getUrlEncoder().withoutPadding().encodeToString(rawId), TYPE);
        rawId_ = rawId.clone();
        response_ = response;
        authenticatorAttachment_ = authenticatorAttachment;
    }

    /**
     * Returns the credential id.
     * @return the credential id as ArrayBuffer
     */
    @JsxGetter
    public NativeArrayBuffer getRawId() {
        if (rawIdBuffer_ == null) {
            rawIdBuffer_ = ArrayBuffers.create(getParentScope(), rawId_);
        }
        return rawIdBuffer_;
    }

    /**
     * Returns the response of the authenticator.
     * @return an {@link AuthenticatorAttestationResponse} or an {@link AuthenticatorAssertionResponse}
     */
    @JsxGetter
    public AuthenticatorResponse getResponse() {
        return response_;
    }

    /**
     * Returns the attachment of the authenticator used.
     * @return {@code platform}, {@code cross-platform} or {@code null}
     */
    @JsxGetter
    public String getAuthenticatorAttachment() {
        return authenticatorAttachment_;
    }

    /**
     * Returns the results of the client extensions; extensions are not supported so far.
     * @return a new empty object
     */
    @JsxFunction
    public Scriptable getClientExtensionResults() {
        return JavaScriptEngine.newObject(getParentScope());
    }

    /**
     * Returns whether a user-verifying platform authenticator (e.g. Windows Hello or Touch ID)
     * is available; there is none in HtmlUnit.
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return a Promise resolving to {@code false}
     */
    @JsxStaticFunction(functionName = "isUserVerifyingPlatformAuthenticatorAvailable")
    public static NativePromise userVerifyingPlatformAuthenticatorAvailable(final Context context,
            final VarScope scope, final Scriptable thisObj, final Object[] args, final Function function) {
        return resolvedPromise(scope, Boolean.FALSE);
    }

    /**
     * Returns whether conditional mediation (credentials offered by the autofill ui) is available;
     * HtmlUnit has no autofill ui.
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return a Promise resolving to {@code false}
     */
    @JsxStaticFunction(functionName = "isConditionalMediationAvailable")
    public static NativePromise conditionalMediationAvailable(final Context context,
            final VarScope scope, final Scriptable thisObj, final Object[] args, final Function function) {
        return resolvedPromise(scope, Boolean.FALSE);
    }

    private static NativePromise resolvedPromise(final VarScope scope, final Object value) {
        final VarScope topLevelScope = ScriptableObject.getTopLevelScope(scope);
        final Function ctor = (Function) ScriptableObject.getProperty(topLevelScope, "Promise");
        final Function resolve = (Function) ScriptableObject.getProperty(ctor, "resolve");
        return (NativePromise) resolve.call(Context.getCurrentContext(), topLevelScope, ctor, new Object[] {value});
    }
}
