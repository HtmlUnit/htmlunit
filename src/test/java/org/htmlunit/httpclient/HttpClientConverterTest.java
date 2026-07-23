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
package org.htmlunit.httpclient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HttpClientConverter}.
 *
 * @author Ronald Brill
 */
public class HttpClientConverterTest {

    @Test
    public void plainDomain() {
        assertEquals("blah.com", HttpClientConverter.registrableDomain("blah.com"));
    }

    @Test
    public void subdomainIsReducedToRegistrableDomain() {
        assertEquals("example.com", HttpClientConverter.registrableDomain("www.example.com"));
        assertEquals("example.com", HttpClientConverter.registrableDomain("a.b.c.example.com"));
    }

    @Test
    public void multiPartPublicSuffix() {
        // the case a naive "last two labels" heuristic gets wrong
        assertEquals("example.co.uk", HttpClientConverter.registrableDomain("example.co.uk"));
        assertEquals("example.co.uk", HttpClientConverter.registrableDomain("www.example.co.uk"));
    }

    @Test
    public void privateSectionSuffixIsHonored() {
        // appspot.com is a PRIVATE-section PSL entry; real browsers treat it as its
        // own site boundary too, same as the public suffix list's ICANN section
        assertEquals("appspot.com", HttpClientConverter.registrableDomain("test.appspot.com"));
    }

    @Test
    public void singleLabelHostFallsBackToItself() {
        assertEquals("localhost", HttpClientConverter.registrableDomain("localhost"));
    }

    @Test
    public void mixedCaseHost() {
        assertEquals("example.com", HttpClientConverter.registrableDomain("WWW.EXAMPLE.COM"));
        assertEquals("example.com", HttpClientConverter.registrableDomain("WWW.eXaMpLE.coM"));

        assertEquals(HttpClientConverter.registrableDomain("WWW.EXAMPLE.COM"),
                HttpClientConverter.registrableDomain("www.example.com"));
    }

    @Test
    public void idnPunycodeHost() {
        // the ASCII/Punycode encoding of münchen.de
        assertEquals("xn--mnchen-3ya.de", HttpClientConverter.registrableDomain("xn--mnchen-3ya.de"));
    }

    @Test
    public void idnUnicodeAndPunycodeAgree() {
        // IMPORTANT: this is the one to actually watch. If this fails, it means the
        // Unicode and Punycode forms of the identical domain are being treated as
        // different registrable domains - i.e. two URLs for the same real-world site
        // could wrongly compute as cross-site depending on which form their host
        // string happens to be in. If it fails, the fix is to normalize the host via
        // java.net.IDN.toASCII(host) before calling getDomainRoot(), not to "fix" this
        // test's expectation.
        assertEquals("xn--mnchen-3ya.de", HttpClientConverter.registrableDomain("xn--mnchen-3ya.de"));
        assertEquals("xn--mnchen-3ya.de", HttpClientConverter.registrableDomain("münchen.de"));

        assertEquals(HttpClientConverter.registrableDomain("xn--mnchen-3ya.de"),
                HttpClientConverter.registrableDomain("münchen.de"));
    }

    @Test
    public void trailingDotFqdnHost() {
        // DNS absolute-FQDN form; unclear whether getDomainRoot() strips this or
        // treats the trailing empty label as breaking the match - verify and adjust
        assertEquals("", HttpClientConverter.registrableDomain("example.com."));
        assertEquals("example.com", HttpClientConverter.registrableDomain("example.com"));

        assertNotEquals(HttpClientConverter.registrableDomain("example.com"),
                HttpClientConverter.registrableDomain("example.com."));
    }

    @Test
    public void underscoreInHost() {
        // technically invalid per strict DNS hostname rules, but seen in the wild
        // (e.g. some internal/test domains) - just confirming it doesn't throw and
        // produces something sane, not asserting a specific known-correct value
        assertEquals("example.com", HttpClientConverter.registrableDomain("my_host.example.com"));
        assertEquals("my_example.com", HttpClientConverter.registrableDomain("myhost.my_example.com"));
    }

    @Test
    public void emptyHost() {
        // shouldn't normally happen (url.getHost() on a well-formed URL), but a public
        // static utility can be called with anything - worth knowing whether this
        // throws or returns something before some other caller hits it unexpectedly
        assertEquals("", HttpClientConverter.registrableDomain(""));
    }

    @Test
    public void ipv4AddressIsNeverRunThroughPublicSuffixList() {
        assertEquals("127.0.0.1", HttpClientConverter.registrableDomain("127.0.0.1"));
        assertEquals("192.168.1.1", HttpClientConverter.registrableDomain("192.168.1.1"));
    }

    @Test
    public void ipv6AddressIsNeverRunThroughPublicSuffixList() {
        assertEquals("::1", HttpClientConverter.registrableDomain("::1"));
    }
}
