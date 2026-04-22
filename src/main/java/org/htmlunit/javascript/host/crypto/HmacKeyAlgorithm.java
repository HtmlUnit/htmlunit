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

import java.util.Set;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.JavaScriptEngine;

/**
 * Internal helper representing HMAC key algorithm parameters.
 * Used by {@link SubtleCrypto} for HMAC key operations.
 *
 * @author Lai Quang Duong
 * @author Ronald Brill
 */
final class HmacKeyAlgorithm {

    static final Set<String> SUPPORTED_HASH_ALGORITHMS = Set.of("SHA-1", "SHA-256", "SHA-384", "SHA-512");

    private final String hash_;
    private final int length_;

    HmacKeyAlgorithm(final String hash, final int length) {
        if (!SUPPORTED_HASH_ALGORITHMS.contains(hash)) {
            throw new UnsupportedOperationException("HMAC " + hash);
        }
        hash_ = hash;

        if (length <= 0) {
            throw new IllegalArgumentException("Data provided to an operation does not meet requirements");
        }
        length_ = length;
    }

    /**
     * Parse HMAC key algorithm parameters from a JS object.
     *
     * @param keyGenParams the JS algorithm parameters object
     * @return the parsed HmacKeyAlgorithm
     */
    static HmacKeyAlgorithm from(final Scriptable keyGenParams) {
        return from(keyGenParams, null);
    }

    /**
     * Parse HMAC key algorithm parameters from a JS object, with an optional fallback length.
     *
     * @param keyGenParams the JS algorithm parameters object
     * @param fallbackLength optional length to use when not specified in params;
     *     if null, defaults to the hash block size
     * @return the parsed HmacKeyAlgorithm
     */
    static HmacKeyAlgorithm from(final Scriptable keyGenParams, final Integer fallbackLength) {
        final Object hashProp = ScriptableObject.getProperty(keyGenParams, "hash");
        final String hash = SubtleCrypto.resolveAlgorithmName(hashProp);

        final int length;
        final Object lengthProp = ScriptableObject.getProperty(keyGenParams, "length");
        if (lengthProp == Scriptable.NOT_FOUND) {
            if (fallbackLength != null) {
                length = fallbackLength;
            }
            else {
                // default to the block size of the hash algorithm
                length = switch (hash) {
                    case "SHA-1", "SHA-256" -> 512;
                    case "SHA-384", "SHA-512" -> 1024;
                    default -> throw new UnsupportedOperationException("HMAC " + hash);
                };
            }
        }
        else {
            if (!(lengthProp instanceof Number numLength)) {
                throw new IllegalArgumentException("An invalid or illegal string was specified");
            }
            length = numLength.intValue();
        }

        return new HmacKeyAlgorithm(hash, length);
    }

    String getHash() {
        return hash_;
    }

    int getLength() {
        return length_;
    }

    /**
     * @return the Java algorithm name for {@link javax.crypto.Mac} (e.g. "HmacSHA256")
     */
    String getJavaName() {
        return "Hmac" + hash_.replace("-", "");
    }

    /**
     * Converts to a JS object matching the {@code HmacKeyAlgorithm} dictionary:
     * {@code {name: "HMAC", hash: {name: "SHA-256"}, length: N}}
     *
     * @param scope the JS scope for prototype/parent setup
     * @return the JS algorithm object
     */
    Scriptable toScriptableObject(final VarScope scope) {
        final Scriptable hashObj = JavaScriptEngine.newObject(scope);
        ScriptableObject.putProperty(hashObj, "name", getHash());

        final Scriptable algorithm = JavaScriptEngine.newObject(scope);
        ScriptableObject.putProperty(algorithm, "name", "HMAC");
        ScriptableObject.putProperty(algorithm, "hash", hashObj);
        ScriptableObject.putProperty(algorithm, "length", getLength());
        return algorithm;
    }
}
