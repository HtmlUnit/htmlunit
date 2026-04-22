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
package org.htmlunit.javascript.host.crypto;

import java.math.BigInteger;
import java.util.Set;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;
import org.htmlunit.corejs.javascript.typedarrays.NativeUint8Array;
import org.htmlunit.javascript.JavaScriptEngine;

/**
 * Internal helper representing RSA hashed key algorithm parameters.
 * Used by {@link SubtleCrypto} for RSA key operations.
 *
 * @author Lai Quang Duong
 * @author Ronald Brill
 */
final class RsaHashedKeyAlgorithm {

    static final Set<String> SUPPORTED_NAMES = Set.of("RSASSA-PKCS1-v1_5", "RSA-PSS", "RSA-OAEP");
    static final Set<String> SUPPORTED_HASH_ALGORITHMS = Set.of("SHA-1", "SHA-256", "SHA-384", "SHA-512");

    private final String name_;
    private final int modulusLength_;
    private final byte[] publicExponent_;
    private final String hash_;

    RsaHashedKeyAlgorithm(final String name, final int modulusLength,
            final byte[] publicExponent, final String hash) {
        if (!SUPPORTED_NAMES.contains(name)) {
            throw new UnsupportedOperationException("RSA " + name);
        }
        name_ = name;

        if (modulusLength <= 0) {
            throw new IllegalArgumentException("Data provided to an operation does not meet requirements");
        }
        modulusLength_ = modulusLength;

        if (publicExponent == null || publicExponent.length == 0) {
            throw new IllegalArgumentException("Data provided to an operation does not meet requirements");
        }
        publicExponent_ = publicExponent.clone();

        if (!SUPPORTED_HASH_ALGORITHMS.contains(hash)) {
            throw new UnsupportedOperationException("RSA hash " + hash);
        }
        hash_ = hash;
    }

    /**
     * Parse RSA hashed key algorithm parameters from a JS object.
     *
     * @param keyGenParams the JS algorithm parameters object
     * @return the parsed RsaHashedKeyAlgorithm
     */
    static RsaHashedKeyAlgorithm from(final Scriptable keyGenParams) {
        final Object nameProp = ScriptableObject.getProperty(keyGenParams, "name");
        if (!(nameProp instanceof String name)) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        final Object modulusLengthProp = ScriptableObject.getProperty(keyGenParams, "modulusLength");
        if (!(modulusLengthProp instanceof Number numModulusLength)) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        final Object publicExponentProp = ScriptableObject.getProperty(keyGenParams, "publicExponent");
        final byte[] publicExponent = extractBytes(publicExponentProp);
        if (publicExponent == null) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        final Object hashProp = ScriptableObject.getProperty(keyGenParams, "hash");
        final String hash = SubtleCrypto.resolveAlgorithmName(hashProp);

        return new RsaHashedKeyAlgorithm(name, numModulusLength.intValue(), publicExponent, hash);
    }

    private static byte[] extractBytes(final Object value) {
        if (value instanceof NativeArrayBufferView view) {
            final NativeArrayBuffer buf = view.getBuffer();
            final byte[] result = new byte[view.getByteLength()];
            System.arraycopy(buf.getBuffer(), view.getByteOffset(), result, 0, result.length);
            return result;
        }
        if (value instanceof NativeArrayBuffer buf) {
            return buf.getBuffer().clone();
        }
        return null;
    }

    static boolean isSupported(final String name) {
        return SUPPORTED_NAMES.contains(name);
    }

    String getName() {
        return name_;
    }

    int getModulusLength() {
        return modulusLength_;
    }

    byte[] getPublicExponent() {
        return publicExponent_.clone();
    }

    /**
     * @return the public exponent as a BigInteger
     */
    BigInteger getPublicExponentAsBigInteger() {
        return new BigInteger(1, publicExponent_);
    }

    String getHash() {
        return hash_;
    }

    /**
     * @return the Java hash algorithm name (e.g. "SHA256" from "SHA-256")
     */
    String getJavaHash() {
        return hash_.replace("-", "");
    }

    /**
     * Converts to a JS object matching the {@code RsaHashedKeyAlgorithm} dictionary:
     * {@code {name: "RSASSA-PKCS1-v1_5", modulusLength: 2048, publicExponent: Uint8Array, hash: {name: "SHA-256"}}}
     *
     * @param scope the JS scope for prototype/parent setup
     * @return the JS algorithm object
     */
    Scriptable toScriptableObject(final Scriptable scope) {
        final Scriptable hashObj = JavaScriptEngine.newObject(scope);
        ScriptableObject.putProperty(hashObj, "name", getHash());

        final NativeUint8Array uint8Array = JavaScriptEngine.newUint8Array(scope, publicExponent_);

        final Scriptable algorithm = JavaScriptEngine.newObject(scope);
        ScriptableObject.putProperty(algorithm, "name", getName());
        ScriptableObject.putProperty(algorithm, "hash", hashObj);
        ScriptableObject.putProperty(algorithm, "modulusLength", getModulusLength());
        ScriptableObject.putProperty(algorithm, "publicExponent", uint8Array);
        return algorithm;
    }
}
