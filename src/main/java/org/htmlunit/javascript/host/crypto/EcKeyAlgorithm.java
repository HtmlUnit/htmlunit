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

import java.util.Map;
import java.util.Set;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.javascript.JavaScriptEngine;

/**
 * Internal helper representing EC key algorithm parameters.
 * Used by {@link SubtleCrypto} for ECDSA and ECDH key operations.
 *
 * @author Lai Quang Duong
 * @author Ronald Brill
 */
final class EcKeyAlgorithm {

    static final Set<String> SUPPORTED_NAMES = Set.of("ECDSA", "ECDH");

    private static final Map<String, String> CURVE_TO_JCA = Map.of(
            "P-256", "secp256r1",
            "P-384", "secp384r1",
            "P-521", "secp521r1"
    );

    private final String name_;
    private final String namedCurve_;

    EcKeyAlgorithm(final String name, final String namedCurve) {
        if (!SUPPORTED_NAMES.contains(name)) {
            throw new UnsupportedOperationException("EC " + name);
        }
        name_ = name;

        if (!CURVE_TO_JCA.containsKey(namedCurve)) {
            throw new UnsupportedOperationException("EC curve " + namedCurve);
        }
        namedCurve_ = namedCurve;
    }

    /**
     * Parse EC key algorithm parameters from a JS object.
     *
     * @param keyGenParams the JS algorithm parameters object
     * @return the parsed EcKeyAlgorithm
     */
    static EcKeyAlgorithm from(final Scriptable keyGenParams) {
        final Object nameProp = ScriptableObject.getProperty(keyGenParams, "name");
        if (!(nameProp instanceof String name)) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        final Object curveProp = ScriptableObject.getProperty(keyGenParams, "namedCurve");
        if (!(curveProp instanceof String namedCurve)) {
            throw new IllegalArgumentException("An invalid or illegal string was specified");
        }

        return new EcKeyAlgorithm(name, namedCurve);
    }

    static boolean isSupported(final String name) {
        return SUPPORTED_NAMES.contains(name);
    }

    String getName() {
        return name_;
    }

    String getNamedCurve() {
        return namedCurve_;
    }

    /**
     * @return the JCA curve name (e.g. "secp256r1" for "P-256")
     */
    String getJavaCurveName() {
        return CURVE_TO_JCA.get(namedCurve_);
    }

    /**
     * Converts to a JS object matching the {@code EcKeyAlgorithm} dictionary:
     * {@code {name: "ECDSA", namedCurve: "P-256"}}
     *
     * @param scope the JS scope for prototype/parent setup
     * @return the JS algorithm object
     */
    Scriptable toScriptableObject(final VarScope scope) {
        final Scriptable algorithm = JavaScriptEngine.newObject(scope);
        ScriptableObject.putProperty(algorithm, "name", getName());
        ScriptableObject.putProperty(algorithm, "namedCurve", getNamedCurve());
        return algorithm;
    }
}
