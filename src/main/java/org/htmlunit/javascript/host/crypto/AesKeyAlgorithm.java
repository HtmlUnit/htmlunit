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

import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.ScriptRuntime;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.TopLevel;

/**
 * Internal helper representing AES key algorithm parameters.
 * Used by {@link SubtleCrypto} for AES key operations.
 *
 * @author Lai Quang Duong
 */
final class AesKeyAlgorithm {

    static final Set<String> SUPPORTED_NAMES = Set.of("AES-CBC", "AES-CTR", "AES-GCM", "AES-KW");
    static final Set<Integer> SUPPORTED_LENGTHS = Set.of(128, 192, 256);

    private final String name_;
    private final int length_;

    AesKeyAlgorithm(final String name, final int length) {
        if (!SUPPORTED_NAMES.contains(name)) {
            throw new UnsupportedOperationException("AES " + name);
        }
        name_ = name;

        if (!SUPPORTED_LENGTHS.contains(length)) {
            throw new IllegalArgumentException("Data provided to an operation does not meet requirements");
        }
        length_ = length;
    }

    /**
     * Parse AES key algorithm parameters from a JS object.
     *
     * @param keyGenParams the JS algorithm parameters object
     * @return the parsed AesKeyAlgorithm
     */
    static AesKeyAlgorithm from(final Scriptable keyGenParams) {
        final Object nameProp = ScriptableObject.getProperty(keyGenParams, "name");
        if (!(nameProp instanceof String name)) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        final Object lengthProp = ScriptableObject.getProperty(keyGenParams, "length");
        if (!(lengthProp instanceof Number numLength)) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        return new AesKeyAlgorithm(name, numLength.intValue());
    }

    static boolean isSupported(final String name) {
        return SUPPORTED_NAMES.contains(name);
    }

    String getName() {
        return name_;
    }

    int getLength() {
        return length_;
    }

    /**
     * Converts to a JS object matching the {@code AesKeyAlgorithm} dictionary:
     * {@code {name: "AES-GCM", length: 256}}
     *
     * @param scope the JS scope for prototype/parent setup
     * @return the JS algorithm object
     */
    Scriptable toScriptableObject(final Scriptable scope) {
        final NativeObject algorithm = new NativeObject();
        ScriptRuntime.setBuiltinProtoAndParent(algorithm, scope, TopLevel.Builtins.Object);
        ScriptableObject.putProperty(algorithm, "name", getName());
        ScriptableObject.putProperty(algorithm, "length", getLength());
        return algorithm;
    }
}
