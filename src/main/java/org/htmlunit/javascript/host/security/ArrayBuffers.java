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

import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;

/**
 * Helper to create the ArrayBuffers of the Web Authentication objects.
 *
 * @author Ronald Brill
 */
final class ArrayBuffers {

    private ArrayBuffers() {
        // util class
    }

    /**
     * Returns a copy of the given bytes.
     * @param data the bytes or {@code null}
     * @return the copy or {@code null} if data is {@code null}
     */
    @SuppressWarnings("PMD.ReturnEmptyCollectionRatherThanNull") // null is a valid value (e.g. no user handle)
    static byte[] copy(final byte[] data) {
        return data == null ? null : data.clone();
    }

    /**
     * Creates an ArrayBuffer holding a copy of the given bytes.
     * @param scope the scope
     * @param data the bytes or {@code null}
     * @return the ArrayBuffer or {@code null} if data is {@code null}
     */
    static NativeArrayBuffer create(final VarScope scope, final byte[] data) {
        if (data == null) {
            return null;
        }

        final NativeArrayBuffer buffer = new NativeArrayBuffer(data.length);
        buffer.getByteBuffer().put(data);
        buffer.setParentScope(scope);
        buffer.setPrototype(ScriptableObject.getClassPrototype(scope, buffer.getClassName()));
        return buffer;
    }
}
