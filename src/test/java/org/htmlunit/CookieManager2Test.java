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
package org.htmlunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.htmlunit.html.HtmlPage;
import org.htmlunit.http.Cookie;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CookieManager}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class CookieManager2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("my_key=§")
    public void cookie_nullValue() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setResponse(URL_FIRST, CookieManagerTest.HTML_ALERT_COOKIE);
        webClient.setWebConnection(webConnection);

        final CookieManager mgr = webClient.getCookieManager();
        mgr.addCookie(new Cookie(URL_FIRST.getHost(), "my_key", null, "/", null, false));

        final List<String> collectedAlerts = new ArrayList<>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("my_name=my_data§")
    public void cookie_maxAgeMinusOne() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        webConnection.setResponse(URL_FIRST, CookieManagerTest.HTML_ALERT_COOKIE);
        webClient.setWebConnection(webConnection);

        final CookieManager mgr = webClient.getCookieManager();
        mgr.addCookie(new Cookie(URL_FIRST.getHost(), "my_name", "my_data", "/", -1, false));

        final HtmlPage page = webClient.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts()[0], page.getTitleText());
    }

    /**
     * Regression test for bug 3053526: HtmlUnit was throwing an Exception when asking for cookies
     * of "about:blank".
     * @throws Exception if the test fails
     */
    @Test
    public void cookiesForAboutBlank() throws Exception {
        final WebClient webClient = getWebClient();
        final HtmlPage htmlPage = webClient.getPage("about:blank");

        final Set<Cookie> cookies = webClient.getCookies(htmlPage.getUrl());
        assertTrue(cookies.toString(), cookies.isEmpty());
    }

    /**
     * This was causing a ConcurrentModificationException.
     * In "real life" the problem was arising due to changes to the cookies made from
     * the JS processing thread.
     * @throws Exception if the test fails
     */
    @Test
    public void getCookiesShouldReturnACopyOfCurentState() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<button id='it' onclick=\"document.cookie = 'foo=bla'\">click me</button>\n"
                + "<script>\n"
                + "document.cookie = 'cookie1=value1';\n"
                + "</script></body></html>";

        final WebClient webClient = getWebClient();

        final HtmlPage page = loadPage(html);
        final Set<Cookie> initialCookies = webClient.getCookieManager().getCookies();
        assertEquals(1, initialCookies.size());
        final Iterator<Cookie> iterator = initialCookies.iterator();

        page.getHtmlElementById("it").click();
        iterator.next(); // ConcurrentModificationException was here
        assertEquals(1, initialCookies.size());
        assertEquals(2, webClient.getCookieManager().getCookies().size());
    }

    /**
     * Method document.cookie must not set cookies for another domain or for
     * a public suffix (RFC 6265 section 5.3, steps 5 and 6).
     * @throws Exception if the test fails
     */
    @Test
    public void documentCookieForeignDomain() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  document.cookie = 'own=1; domain=attacker.co.uk; path=/';\n"
            + "  document.cookie = 'foreign=1; domain=victim.example; path=/';\n"
            + "  document.cookie = 'suffix=1; domain=co.uk; path=/';\n"
            + "  document.cookie = 'tld=1; domain=uk; path=/';\n"
            + "  document.cookie = 'tldDot=1; domain=.uk; path=/';\n"
            + "</script></head><body></body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(new URL("https://www.attacker.co.uk/"), html);
        webClient.setWebConnection(webConnection);

        webClient.getPage("https://www.attacker.co.uk/");

        assertEquals("[own=1;domain=.attacker.co.uk;path=/]",
                webClient.getCookieManager().getCookies().toString());
        assertTrue(webClient.getCookies(new URL("https://victim.example/")).isEmpty());
        assertTrue(webClient.getCookies(new URL("https://victim.co.uk/")).isEmpty());
        assertTrue(webClient.getCookies(new URL("https://victim.uk/")).isEmpty());
    }

    /**
     * Cookies of a host with a single label name are handled without name resolution;
     * the host 'htmlunit-unresolvable' does not exist (served by the mock connection).
     * <p>The mock connection does not process Set-Cookie headers (only the HttpClient
     * based connection does); therefore the cookies are set by document.cookie.</p>
     *
     * @throws Exception if the test fails
     */
    @Test
    public void unqualifiedHostWithoutNameResolution() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  document.cookie = 'js=1; path=/';\n"
            + "  document.cookie = 'jsDomain=1; domain=htmlunit-unresolvable; path=/';\n"
            + "  document.cookie = 'jsDomainDot=1; domain=.htmlunit-unresolvable; path=/';\n"
            + "  document.title = document.cookie.split('; ').sort().join('; ');\n"
            + "</script></head><body></body></html>";

        final URL url = new URL("http://htmlunit-unresolvable/");
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(url, html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(url);
        assertEquals("js=1; jsDomain=1; jsDomainDot=1", page.getTitleText());
        assertEquals(3, webClient.getCookies(url).size());
        assertTrue(webClient.getCookies(new URL("http://other-host/")).isEmpty());
    }

    /**
     * Cookies for a public suffix added by the api are not sent.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void publicSuffixCookieAddedByApi() throws Exception {
        final WebClient webClient = getWebClient();
        final CookieManager mgr = webClient.getCookieManager();
        mgr.addCookie(new Cookie("com", "tld", "1", "/", null, false));
        mgr.addCookie(new Cookie("co.uk", "suffix", "1", "/", null, false));
        mgr.addCookie(new Cookie("example.com", "own", "1", "/", null, false));

        final Set<Cookie> exampleCom = webClient.getCookies(new URL("https://www.example.com/"));
        assertEquals(1, exampleCom.size());
        assertEquals("own", exampleCom.iterator().next().getName());

        assertTrue(webClient.getCookies(new URL("https://www.example.co.uk/")).isEmpty());
    }

    /**
     * Method document.cookie must not set cookies for a private suffix of the
     * public suffix list (e.g. 'github.io') like in the browsers.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void documentCookiePrivateSuffix() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  document.cookie = 'own=1; domain=attacker.github.io; path=/';\n"
            + "  document.cookie = 'suffix=1; domain=github.io; path=/';\n"
            + "  document.cookie = 'suffixDot=1; domain=.github.io; path=/';\n"
            + "  document.title = document.cookie.split('; ').sort().join('; ');\n"
            + "</script></head><body></body></html>";

        final URL url = new URL("https://attacker.github.io/");
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(url, html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage(url);
        assertEquals("own=1", page.getTitleText());
        assertTrue(webClient.getCookies(new URL("https://victim.github.io/")).isEmpty());
    }

    /**
     * A page from an insecure origin must not overwrite (or shadow) a secure cookie
     * (RFC 6265bis section 5.7, step 16).
     *
     * @throws Exception if the test fails
     */
    @Test
    public void secureCookieNotOverwrittenFromInsecureOrigin() throws Exception {
        final String secureHtml = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  document.cookie = 'sec=1; Secure; path=/';\n"
            + "  document.cookie = 'secPath=1; Secure; path=/app';\n"
            + "  document.cookie = 'plain=1; path=/';\n"
            + "</script></head><body></body></html>";

        final String insecureHtml = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            // same name, domain and path
            + "  document.cookie = 'sec=2; path=/';\n"
            // the path /sub path-matches /
            + "  document.cookie = 'sec=3; path=/sub';\n"
            // the domain example.com domain-matches www.example.com
            + "  document.cookie = 'sec=4; domain=example.com; path=/';\n"
            // the path / does not path-match /app
            + "  document.cookie = 'secPath=2; path=/';\n"
            + "  document.cookie = 'plain=2; path=/';\n"
            + "  document.cookie = 'other=1; path=/';\n"
            + "  document.title = document.cookie.split('; ').sort().join('; ');\n"
            + "</script></head><body></body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(new URL("https://www.example.com/"), secureHtml);
        webConnection.setResponse(new URL("http://www.example.com/"), insecureHtml);
        webClient.setWebConnection(webConnection);

        webClient.getPage("https://www.example.com/");
        final HtmlPage page = webClient.getPage("http://www.example.com/");
        assertEquals("other=1; plain=2; secPath=2", page.getTitleText());

        final CookieManager mgr = webClient.getCookieManager();
        final List<String> cookies = new ArrayList<>();
        for (final Cookie cookie : mgr.getCookies()) {
            cookies.add(cookie.getName() + "=" + cookie.getValue() + " " + cookie.getPath()
                    + (cookie.isSecure() ? " secure" : ""));
        }
        Collections.sort(cookies);
        assertEquals("[other=1 /, plain=2 /, sec=1 / secure, secPath=1 /app secure, secPath=2 /]",
                cookies.toString());
    }

    /**
     * A page from a secure origin can overwrite a secure cookie with a non-secure one.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void secureCookieOverwrittenFromSecureOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  document.cookie = 'sec=1; Secure; path=/';\n"
            + "  document.cookie = 'sec=2; path=/';\n"
            + "  document.title = document.cookie;\n"
            + "</script></head><body></body></html>";

        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(new URL("https://www.example.com/"), html);
        webClient.setWebConnection(webConnection);

        final HtmlPage page = webClient.getPage("https://www.example.com/");
        assertEquals("sec=2", page.getTitleText());
        assertFalse(webClient.getCookieManager().getCookie("sec").isSecure());
    }
}
