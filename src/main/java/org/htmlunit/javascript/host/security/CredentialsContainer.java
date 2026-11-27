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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.corejs.javascript.NativePromise;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.abort.AbortSignal;
import org.htmlunit.javascript.host.dom.DOMException;

/**
 * JavaScript host object for {@code CredentialsContainer} ({@code navigator.credentials}).
 * So far no authenticator is available; public key requests are rejected
 * like a browser does when the user cancels the dialog.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/CredentialsContainer">MDN Documentation</a>
 * @see <a href="https://w3c.github.io/webappsec-credential-management/#credentialscontainer">
 *      Credential Management</a>
 */
@JsxClass
public class CredentialsContainer extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(CredentialsContainer.class);

    private static final String PUBLIC_KEY = "publicKey";
    private static final String PASSWORD = "password";
    private static final String FEDERATED = "federated";

    /** The message of the browsers if the user canceled or no authenticator answered in time. */
    static final String NOT_ALLOWED_MESSAGE = "The operation either timed out or was not allowed. "
            + "See: https://www.w3.org/TR/webauthn-2/#sctn-privacy-considerations-client.";

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Creates a new credential.
     * @param options the CredentialCreationOptions
     * @return a Promise that resolves with the new credential
     */
    @JsxFunction
    public NativePromise create(final Object options) {
        final Scriptable dictionary = toDictionary(options);
        if (dictionary == null) {
            return rejectWithTypeError("Failed to execute 'create' on 'CredentialsContainer': "
                    + "The provided value is not of type 'CredentialCreationOptions'.");
        }

        final NativePromise aborted = rejectIfAborted(dictionary);
        if (aborted != null) {
            return aborted;
        }

        final boolean passwordSupported = isPasswordSupported();
        final boolean publicKey = isDefined(dictionary, PUBLIC_KEY);
        final int types = (publicKey ? 1 : 0)
                + (passwordSupported && isDefined(dictionary, PASSWORD) ? 1 : 0)
                + (passwordSupported && isDefined(dictionary, FEDERATED) ? 1 : 0);
        if (types != 1) {
            return rejectWithDOMException("Only exactly one of 'password', 'federated', and 'publicKey' "
                    + "credential types are currently supported.", DOMException.NOT_SUPPORTED_ERR);
        }

        if (publicKey) {
            // no authenticator available; a browser waits for the user and rejects after the timeout
            return rejectWithDOMException(NOT_ALLOWED_MESSAGE, DOMException.NOT_ALLOWED_ERR);
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("CredentialsContainer.create() is not implemented for password and federated credentials.");
        }
        return rejectWithDOMException("The password and federated credential types are not supported.",
                DOMException.NOT_SUPPORTED_ERR);
    }

    /**
     * Requests a credential.
     * @param options the CredentialRequestOptions
     * @return a Promise that resolves with the credential or {@code null}
     */
    @JsxFunction
    @Override
    public NativePromise get(final Object options) {
        final Scriptable dictionary = toDictionary(options);
        if (dictionary == null) {
            return rejectWithTypeError("Failed to execute 'get' on 'CredentialsContainer': "
                    + "The provided value is not of type 'CredentialRequestOptions'.");
        }

        final NativePromise aborted = rejectIfAborted(dictionary);
        if (aborted != null) {
            return aborted;
        }

        final boolean passwordSupported = isPasswordSupported();
        final boolean publicKey = isDefined(dictionary, PUBLIC_KEY);
        final boolean password = passwordSupported && isTrue(dictionary, PASSWORD);
        final boolean federated = passwordSupported && isDefined(dictionary, FEDERATED);
        if (!publicKey && !password && !federated) {
            return rejectWithDOMException("Only exactly one of 'password', 'federated', 'identity', 'otp' "
                    + "and 'publicKey' credential types are currently supported.", DOMException.NOT_SUPPORTED_ERR);
        }

        if (publicKey) {
            final Object mediation = ScriptableObject.getProperty(dictionary, "mediation");
            if ("conditional".equals(mediation)) {
                // see PublicKeyCredential.isConditionalMediationAvailable()
                return rejectWithDOMException("Conditional mediation is not supported.",
                        DOMException.NOT_SUPPORTED_ERR);
            }

            // no authenticator available; a browser waits for the user and rejects after the timeout
            return rejectWithDOMException(NOT_ALLOWED_MESSAGE, DOMException.NOT_ALLOWED_ERR);
        }

        // no stored password or federated credentials
        return setupPromise(() -> null);
    }

    /**
     * Stores a credential.
     * @param credential the credential
     * @return a Promise that resolves when the credential is stored
     */
    @JsxFunction
    public NativePromise store(final Object credential) {
        if (!(credential instanceof Credential)) {
            return rejectWithTypeError("Failed to execute 'store' on 'CredentialsContainer': "
                    + "parameter 1 is not of type 'Credential'.");
        }
        if (credential instanceof PublicKeyCredential) {
            return rejectWithDOMException("Store operation not permitted for PublicKey credentials.",
                    DOMException.NOT_SUPPORTED_ERR);
        }

        if (LOG.isInfoEnabled()) {
            LOG.info("CredentialsContainer.store() does not store the credential.");
        }
        return setupPromise(() -> JavaScriptEngine.UNDEFINED);
    }

    /**
     * Prevents the automatic sign-in on the next visit; HtmlUnit does no automatic sign-in.
     * @return a Promise that resolves when done
     */
    @JsxFunction
    public NativePromise preventSilentAccess() {
        return setupPromise(() -> JavaScriptEngine.UNDEFINED);
    }

    /**
     * Converts the options argument like WebIDL converts a dictionary.
     * @param options the argument
     * @return the dictionary (an empty one for undefined and null) or {@code null} if this is not an object
     */
    private Scriptable toDictionary(final Object options) {
        if (options == null || JavaScriptEngine.isUndefined(options)) {
            return JavaScriptEngine.newObject(getParentScope());
        }
        if (options instanceof Scriptable dictionary) {
            return dictionary;
        }
        return null;
    }

    private NativePromise rejectIfAborted(final Scriptable dictionary) {
        if (ScriptableObject.getProperty(dictionary, "signal") instanceof AbortSignal signal && signal.isAborted()) {
            return setupRejectedPromise(signal::getReason);
        }
        return null;
    }

    /**
     * Returns whether password and federated credentials are supported (only by Chrome and Edge).
     * @return whether password and federated credentials are supported
     */
    private boolean isPasswordSupported() {
        return !getBrowserVersion().isFirefox();
    }

    private static boolean isDefined(final Scriptable dictionary, final String name) {
        final Object value = ScriptableObject.getProperty(dictionary, name);
        return value != Scriptable.NOT_FOUND && !JavaScriptEngine.isUndefined(value);
    }

    private static boolean isTrue(final Scriptable dictionary, final String name) {
        final Object value = ScriptableObject.getProperty(dictionary, name);
        return value != Scriptable.NOT_FOUND && JavaScriptEngine.toBoolean(value);
    }

    private NativePromise rejectWithTypeError(final String message) {
        return setupRejectedPromise(() -> JavaScriptEngine.toErrorObject(
                JavaScriptEngine.typeError(message), getParentScope()));
    }

    private NativePromise rejectWithDOMException(final String message, final int code) {
        return setupRejectedPromise(() -> createDOMException(message, code));
    }
}
