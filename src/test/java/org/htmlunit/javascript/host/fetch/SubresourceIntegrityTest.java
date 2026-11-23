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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SubresourceIntegrity}.
 *
 * @author Ronald Brill
 */
public class SubresourceIntegrityTest {

    private static final byte[] ABC = "abc".getBytes(UTF_8);

    private static final String SHA256 = "sha256-ungWv48Bz+pBQUDeXa4iI7ADYaOWF3qctBD/YfIAFa0=";
    private static final String SHA384 = "sha384-ywB1P0WjXou1oD1pmsZQBycsMqsO3tFjGotgWkP/W+2AhgcroefMI1i67KE0yCWn";
    private static final String SHA512 =
            "sha512-3a81oZNherrMQXNJriBBMRLm+k6JqX6iCp7u5ktV05ohkpkqJ0/BqDa6PCOj/uu9RU1EI2Q86A4qmslPpUyknw==";

    /**
     * Tests the matching hashes.
     */
    @Test
    public void matches() {
        assertTrue(SubresourceIntegrity.matches(SHA256, ABC));
        assertTrue(SubresourceIntegrity.matches(SHA384, ABC));
        assertTrue(SubresourceIntegrity.matches(SHA512, ABC));
        assertTrue(SubresourceIntegrity.matches("  " + SHA256 + "\t", ABC));

        // case-insensitive algorithm, options, base64url, no padding
        assertTrue(SubresourceIntegrity.matches(SHA256.replace("sha256", "SHA256"), ABC));
        assertTrue(SubresourceIntegrity.matches(SHA256 + "?foo=bar", ABC));
        assertTrue(SubresourceIntegrity.matches(SHA256.replace('+', '-').replace('/', '_'), ABC));
        assertTrue(SubresourceIntegrity.matches(SHA256.replace("=", ""), ABC));

        // one of the hashes of the strongest algorithm has to match
        assertTrue(SubresourceIntegrity.matches("sha512-wrong " + SHA512, ABC));
        assertTrue(SubresourceIntegrity.matches("sha256-wrong " + SHA384, ABC));
    }

    /**
     * Tests the not matching hashes.
     */
    @Test
    public void matchesNot() {
        assertFalse(SubresourceIntegrity.matches("sha256-wrong", ABC));
        assertFalse(SubresourceIntegrity.matches(SHA256, "abd".getBytes(UTF_8)));
        assertFalse(SubresourceIntegrity.matches(SHA256, null));

        // only the strongest algorithm counts
        assertFalse(SubresourceIntegrity.matches(SHA256 + " sha512-wrong", ABC));
    }

    /**
     * Metadata without supported hashes is ignored.
     */
    @Test
    public void noSupportedMetadata() {
        assertTrue(SubresourceIntegrity.matches("", ABC));
        assertTrue(SubresourceIntegrity.matches("   ", ABC));
        assertTrue(SubresourceIntegrity.matches("md5-abc", ABC));
        assertTrue(SubresourceIntegrity.matches("sha1-abc", ABC));
        assertTrue(SubresourceIntegrity.matches("invalid", ABC));
    }
}
