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

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.VarScope;

/**
 * The algorithm of a {@link CryptoKey} (its {@code [[algorithm]]} internal slot).
 * Implementations are immutable; the JavaScript object is created separately for every key.
 *
 * @author Ronald Brill
 */
interface KeyAlgorithm {

    /**
     * Returns the registered algorithm name.
     * @return the registered algorithm name (e.g. "AES-GCM")
     */
    String getName();

    /**
     * Converts to a new JavaScript object matching the KeyAlgorithm dictionary of the algorithm.
     *
     * @param scope the JS scope for prototype/parent setup
     * @return the JS algorithm object
     */
    Scriptable toScriptableObject(VarScope scope);
}
