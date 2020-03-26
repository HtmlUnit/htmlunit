/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;

import net.sourceforge.htmlunit.corejs.javascript.EvaluatorException;

/**
 * The implementation of {@code WindowOrWorkerGlobalScope}
 * to be used by the implementers of the mixin.
 *
 * @author Ronald Brill
 */
public final class WindowOrWorkerGlobalScopeMixin {

    private WindowOrWorkerGlobalScopeMixin() {
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding.
     * @param encodedData the encoded string
     * @return the decoded value
     */
    public static String atob(final String encodedData) {
        final int l = encodedData.length();
        for (int i = 0; i < l; i++) {
            if (encodedData.charAt(i) > 255) {
                throw new EvaluatorException("Function atob supports only latin1 characters");
            }
        }
        final byte[] bytes = encodedData.getBytes(StandardCharsets.ISO_8859_1);
        return new String(Base64.decodeBase64(bytes), StandardCharsets.ISO_8859_1);
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    public static String btoa(final String stringToEncode) {
        final int l = stringToEncode.length();
        for (int i = 0; i < l; i++) {
            if (stringToEncode.charAt(i) > 255) {
                throw new EvaluatorException("Function btoa supports only latin1 characters");
            }
        }
        final byte[] bytes = stringToEncode.getBytes(StandardCharsets.ISO_8859_1);
        return new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
    }
}
