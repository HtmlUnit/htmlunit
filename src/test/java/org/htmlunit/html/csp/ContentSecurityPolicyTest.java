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
package org.htmlunit.html.csp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.htmlunit.csp.url.GUID;
import org.htmlunit.csp.url.URI;
import org.htmlunit.csp.url.URLWithScheme;
import org.htmlunit.util.UrlUtils;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ContentSecurityPolicy}.
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyTest {

    /**
     * Test for {@link ContentSecurityPolicy#toUrlWithScheme(java.net.URL)}; the result has to be
     * the same as the one of the parse methods of the csp library.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void toUrlWithScheme() throws Exception {
        assertToUri("http://localhost:22222/api/data.txt?x=1#ref");
        assertToUri("http://LocalHost:22222/api/data.txt");
        assertToUri("https://example.com/");
        assertToUri("http://example.com");
        assertToUri("https://example.com:8443/path/to/");
        assertToUri("ws://example.com/socket");
        assertToUri("wss://example.com:9443/socket");

        assertToGuid("data:text/plain,data");
        assertToGuid("file:///tmp/test.txt");
        assertToGuid("about:blank");
    }

    private static void assertToUri(final String url) throws Exception {
        final URLWithScheme expected = URI.parseURI(url).get();
        final URLWithScheme actual = ContentSecurityPolicy.toUrlWithScheme(UrlUtils.toUrlUnsafe(url));
        assertTrue(actual instanceof URI, url);
        assertEquals(expected, actual, url);
    }

    private static void assertToGuid(final String url) throws Exception {
        final URLWithScheme expected = GUID.parseGUID(url).get();
        final URLWithScheme actual = ContentSecurityPolicy.toUrlWithScheme(UrlUtils.toUrlUnsafe(url));
        assertTrue(actual instanceof GUID, url);
        assertEquals(expected.getScheme(), actual.getScheme(), url);
    }
}
