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
package com.gargoylesoftware.htmlunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Unit tests for {@link CookieManager}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class CookieManager2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void resettingCookie() throws Exception {
        final String html
            = "<html><head><title>foo</title>"
            + "<script>\n"
            + "  function createCookie(name, value, days, path) {\n"
            + "    if (days) {\n"
            + "      var date = new Date();\n"
            + "      date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));\n"
            + "      var expires = '; expires=' + date.toGMTString();\n"
            + "    }\n"
            + "    else\n"
            + "      var expires = '';\n"
            + "    document.cookie = name + '=' + value + expires + '; path=' + path;\n"
            + "  }\n"
            + "\n"
            + "  function readCookie(name) {\n"
            + "    var nameEQ = name + '=';\n"
            + "    var ca = document.cookie.split(';');\n"
            + "    for(var i = 0; i < ca.length; i++) {\n"
            + "      var c = ca[i];\n"
            + "      while (c.charAt(0) == ' ')\n"
            + "        c = c.substring(1, c.length);\n"
            + "      if (c.indexOf(nameEQ) == 0)\n"
            + "        return c.substring(nameEQ.length, c.length);\n"
            + "    }\n"
            + "    return null;\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "  <input id='button1' type='button' "
            + "onclick=\"createCookie('button','button1',1,'/'); alert('cookie:' + readCookie('button'));\" "
            + "value='Button 1'>\n"
            + "  <input id='button2' type='button' "
            + "onclick=\"createCookie('button','button2',1,'/'); alert('cookie:' + readCookie('button'));\" "
            + "value='Button 2'>\n"
            + "</form></body></html>";

        final String[] expectedAlerts = {"cookie:button1", "cookie:button2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        page.getHtmlElementById("button1").click();
        page.getHtmlElementById("button2").click();
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void cookie_nullValue() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();

        final URL url = URL_FIRST;
        webConnection.setResponse(url, CookieManagerTest.HTML_ALERT_COOKIE);
        webClient.setWebConnection(webConnection);

        final CookieManager mgr = webClient.getCookieManager();
        mgr.addCookie(new Cookie(URL_FIRST.getHost(), "my_key", null, "/", null, false));

        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        webClient.getPage(URL_FIRST);

        final String[] expectedAlerts = {"my_key="};
        assertEquals(expectedAlerts, collectedAlerts);
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
        final String html = "<html><body>"
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
}
