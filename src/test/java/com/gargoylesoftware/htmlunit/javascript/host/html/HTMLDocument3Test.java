/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument.EMPTY_COOKIE_NAME;
import static com.gargoylesoftware.htmlunit.util.StringUtils.parseHttpDate;

import java.util.Date;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Tests for {@link HTMLDocument}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HTMLDocument3Test extends WebTestCase {

    /**
     * Test having &lt; and &gt; in attribute values.
     */
    @Test
    public void canAlreadyBeParsed() {
        assertTrue(HTMLDocument.canAlreadyBeParsed("<p>hallo</p>"));
        assertTrue(HTMLDocument.canAlreadyBeParsed("<img src='foo' alt=\"<'>\"></img>"));

        // double close is ok
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script></script></script>"));

        // check for correct string quoting in script
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc';</script>"));
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script>var test =\"abc\";</script>"));
        assertFalse(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc\";</script>"));
        assertFalse(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc;</script>"));
        assertFalse(HTMLDocument.canAlreadyBeParsed("<script>var test =\"abc;</script>"));

        // check quoting only inside script tags
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc';</script><p>it's fun</p>"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void buildCookie() throws Exception {
        final String domain = URL_FIRST.getHost();
        checkCookie(HTMLDocument.buildCookie("", URL_FIRST), EMPTY_COOKIE_NAME, "", "/", domain, false, null);
        checkCookie(HTMLDocument.buildCookie("toto", URL_FIRST), EMPTY_COOKIE_NAME, "toto", "/", domain, false, null);
        checkCookie(HTMLDocument.buildCookie("toto=", URL_FIRST), "toto", "", "/", domain, false, null);
        checkCookie(HTMLDocument.buildCookie("toto=foo", URL_FIRST), "toto", "foo", "/", domain, false, null);
        checkCookie(HTMLDocument.buildCookie("toto=foo;secure", URL_FIRST), "toto", "foo", "/", domain, true, null);
        checkCookie(HTMLDocument.buildCookie("toto=foo;path=/myPath;secure", URL_FIRST),
                "toto", "foo", "/myPath", domain, true, null);

        // Check that leading and trailing whitespaces are ignored
        checkCookie(HTMLDocument.buildCookie("   toto=foo;  path=/myPath  ; secure  ", URL_FIRST),
                "toto", "foo", "/myPath", domain, true, null);

        // Check that we accept reserved attribute names (e.g expires, domain) in any case
        checkCookie(HTMLDocument.buildCookie("toto=foo; PATH=/myPath; SeCURE", URL_FIRST),
                "toto", "foo", "/myPath", domain, true, null);

        // Check that we are able to parse and set the expiration date correctly
        final String dateString = "Fri, 21 Jul 2006 20:47:11 UTC";
        final Date date = parseHttpDate(dateString);
        checkCookie(HTMLDocument.buildCookie("toto=foo; expires=" + dateString, URL_FIRST),
                "toto", "foo", "/", domain, false, date);
    }

    private void checkCookie(final Cookie cookie, final String name, final String value,
            final String path, final String domain, final boolean secure, final Date date) {
        assertEquals(name, cookie.getName());
        assertEquals(value, cookie.getValue());
        assertEquals(path, cookie.getPath());
        assertEquals(domain, cookie.getDomain());
        assertEquals(secure, cookie.isSecure());
        assertEquals(date, cookie.getExpires());
    }
}
