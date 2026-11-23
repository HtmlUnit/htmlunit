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
package org.htmlunit.javascript.host.fetch;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

/**
 * The check of the integrity metadata (subresource integrity).
 *
 * @see <a href="https://w3c.github.io/webappsec-subresource-integrity/">Subresource Integrity</a>
 * @author Ronald Brill
 */
final class SubresourceIntegrity {

    // ordered by strength
    private static final String[] ALGORITHMS = {"sha256", "sha384", "sha512"};
    private static final String[] JAVA_ALGORITHMS = {"SHA-256", "SHA-384", "SHA-512"};

    private SubresourceIntegrity() {
        // util class
    }

    /**
     * Checks the content against the integrity metadata.
     *
     * @param metadata the integrity metadata (e.g. 'sha384-abc... sha512-def...')
     * @param content the content
     * @return {@code true} if the content matches the metadata (or the metadata has no supported hash)
     * @see <a href="https://w3c.github.io/webappsec-subresource-integrity/#does-response-match-metadatalist">
     *     Subresource Integrity - Do bytes match metadataList?</a>
     */
    static boolean matches(final String metadata, final byte[] content) {
        // the hashes of the strongest algorithm
        int strongest = -1;
        final List<String> expected = new ArrayList<>();

        for (final String token : metadata.trim().split("[ \\t\\n\\f\\r]+")) {
            String expression = token;
            final int question = expression.indexOf('?');
            if (question > -1) {
                expression = expression.substring(0, question);
            }
            final int dash = expression.indexOf('-');
            if (dash < 1) {
                continue;
            }

            final String algorithm = expression.substring(0, dash).toLowerCase(Locale.ROOT);
            int index = -1;
            for (int i = 0; i < ALGORITHMS.length; i++) {
                if (ALGORITHMS[i].equals(algorithm)) {
                    index = i;
                    break;
                }
            }
            if (index < 0) {
                continue;
            }

            if (index > strongest) {
                strongest = index;
                expected.clear();
            }
            if (index == strongest) {
                expected.add(normalizeBase64(expression.substring(dash + 1)));
            }
        }

        if (strongest < 0) {
            // no supported metadata
            return true;
        }

        final String actual;
        try {
            final MessageDigest digest = MessageDigest.getInstance(JAVA_ALGORITHMS[strongest]);
            actual = normalizeBase64(Base64.getEncoder().encodeToString(
                    digest.digest(content == null ? new byte[0] : content)));
        }
        catch (final NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
        return expected.contains(actual);
    }

    /**
     * Normalizes the hash value; the value may be base64 or base64url encoded and the padding is optional.
     *
     * @param value the value
     * @return the normalized value
     */
    private static String normalizeBase64(final String value) {
        String result = value.replace('-', '+').replace('_', '/');
        while (result.endsWith("=")) {
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }
}
