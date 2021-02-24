/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.httpclient.HtmlUnitBrowserCompatCookieSpec.EMPTY_COOKIE_NAME;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * Tests for {@link WebClient} that run with BrowserRunner.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class WebClient2Test extends SimpleWebTestCase {

    /**
     * Test for 3151939. The Browser removes leading '/..' from the path.
     * @throws Exception if something goes wrong
     */
    @Test
    public void loadPage_HandleDoubleDotsAtRoot() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        HtmlPage page = client.getPage("http://www.somewhere.org/..");
        assertEquals("http://www.somewhere.org/", page.getUrl());

        page = client.getPage("http://www.somewhere.org/../test");
        assertEquals("http://www.somewhere.org/test", page.getUrl());

        // many
        page = client.getPage("http://www.somewhere.org/../../..");
        assertEquals("http://www.somewhere.org/", page.getUrl());
    }

    /**
     * Verifies that a WebClient can be serialized and deserialized before it has been used.
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization_beforeUse() throws Exception {
        final WebClient client = getWebClient();
        final WebClient copy = clone(client);
        assertNotNull(copy);
    }

    /**
     * Regression test for bug 2833433.
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization_pageLoad() throws Exception {
        final String page1Content = "<html><body>hello 1</body></html>";
        try (WebClient client = getWebClient()) {
            final HtmlPage page1 = loadPage(client, page1Content, null, URL_FIRST);
            assertEquals("hello 1", page1.asText());

            final String page2Content = "<html><body>hello 2</body></html>";
            try (WebClient copy = clone(client)) {
                final HtmlPage page2 = loadPage(copy, page2Content, null, URL_SECOND);
                assertEquals("hello 2", page2.asText());
            }
        }
    }

    /**
     * Regression test for bug 2836355.
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization_withClickAfterwards() throws Exception {
        final String html =
              "<html><head>\n"
            + "<script>\n"
            + "  function foo() {\n"
            + "    document.getElementById('mybox').innerHTML='hello world';\n"
            + "    return false;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body><div id='mybox'></div>\n"
            + "<a href='#' onclick='foo()' id='clicklink'>say hello world</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("", page.getElementById("mybox").getTextContent());

        final WebClient clientCopy = clone(page.getWebClient());
        final HtmlPage pageCopy = (HtmlPage) clientCopy.getCurrentWindow().getTopWindow().getEnclosedPage();
        pageCopy.getHtmlElementById("clicklink").click();
        assertEquals("hello world", pageCopy.getElementById("mybox").getTextContent());
    }

    /**
     * Background tasks that have been registered before the serialization should
     * wake up and run normally after the deserialization.
     * Until now (2.7-SNAPSHOT 17.09.09) HtmlUnit has probably never supported it.
     * This is currently not requested and this test is just to document the current status.
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void serialization_withJSBackgroundTasks() throws Exception {
        final String html =
              "<html><head>\n"
            + "<script>\n"
            + "  function foo() {\n"
            + "    if (window.name == 'hello') {\n"
            + "      alert('exiting');\n"
            + "      clearInterval(intervalId);\n"
            + "    }\n"
            + "  }\n"
            + "  var intervalId = setInterval(foo, 10);\n"
            + "</script></head>\n"
            + "<body></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        // verify that 1 background job exists
        assertEquals(1, page.getEnclosingWindow().getJobManager().getJobCount());

        final byte[] bytes = SerializationUtils.serialize(page);
        page.getWebClient().close();

        // deserialize page and verify that 1 background job exists
        final HtmlPage clonedPage = (HtmlPage) SerializationUtils.deserialize(bytes);
        assertEquals(1, clonedPage.getEnclosingWindow().getJobManager().getJobCount());

        // configure a new CollectingAlertHandler (in fact it has surely already one and we could get and cast it)
        final List<String> collectedAlerts = Collections.synchronizedList(new ArrayList<String>());
        final AlertHandler alertHandler = new CollectingAlertHandler(collectedAlerts);
        clonedPage.getWebClient().setAlertHandler(alertHandler);

        // make some change in the page on which background script reacts
        clonedPage.getEnclosingWindow().setName("hello");

        clonedPage.getWebClient().waitForBackgroundJavaScriptStartingBefore(100);
        assertEquals(0, clonedPage.getEnclosingWindow().getJobManager().getJobCount());
        final String[] expectedAlerts = {"exiting"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * Regression test for bug 2812769.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "en-US,en;q=0.9",
            FF = "en-US,en;q=0.5",
            FF78 = "en-US,en;q=0.5")
    public void acceptLanguage() throws Exception {
        final String html = "<html><body></body></html>";
        loadPage(html);
        assertEquals(getExpectedAlerts()[0],
                getMockWebConnection().getLastAdditionalHeaders().get(HttpHeader.ACCEPT_LANGUAGE));
    }

    /**
     * Regression test for bug 2812769.
     * @throws Exception if an error occurs
     */
    @Test
    public void acceptLanguageFr() throws Exception {
        final String html = "<html><body></body></html>";

        final BrowserVersion frBrowser =
                new BrowserVersion.BrowserVersionBuilder(getBrowserVersion())
                        .setAcceptLanguageHeader("fr")
                        .build();

        setBrowserVersion(frBrowser);
        loadPageWithAlerts(html);
        // browsers are using different casing, but this is not relevant for this test
        assertEquals("fr",
                getMockWebConnection().getLastAdditionalHeaders()
                    .get(HttpHeader.ACCEPT_LANGUAGE).toLowerCase(Locale.ROOT));
    }

    /**
     * As of HtmlUnit-2.7-SNAPSHOT from 24.09.09, loading about:blank in a page didn't
     * reinitialized the window host object.
     * @throws Exception if an error occurs
     */
    @Test
    public void newWindowScopeForAboutBlank() throws Exception {
        final HtmlPage p = loadPage("<html><body></body></html>");
        p.executeJavaScript("top.foo = 'hello';");
        final ScriptResult result = p.executeJavaScript("top.foo");
        assertEquals("hello", result.getJavaScriptResult());

        final HtmlPage page2 = p.getWebClient().getPage("about:blank");
        final ScriptResult result2 = page2.executeJavaScript("String(top.foo)");
        assertEquals("undefined", result2.getJavaScriptResult());
    }

  /**
   * @throws Exception if the test fails
   */
    @Test
    public void buildCookie() throws Exception {
        checkCookie("", EMPTY_COOKIE_NAME, "", "/", false, null);
        checkCookie("toto", EMPTY_COOKIE_NAME, "toto", "/", false, null);
        checkCookie("toto=", "toto", "", "/", false, null);
        checkCookie("toto=foo", "toto", "foo", "/", false, null);
        checkCookie("toto=foo;secure", "toto", "foo", "/", true, null);
        checkCookie("toto=foo;path=/myPath;secure", "toto", "foo", "/myPath", true, null);

        // Check that leading and trailing whitespaces are ignored
        checkCookie("  toto", EMPTY_COOKIE_NAME, "toto", "/", false, null);
        checkCookie("  = toto", EMPTY_COOKIE_NAME, "toto", "/", false, null);
        checkCookie("   toto=foo;  path=/myPath  ; secure  ",
              "toto", "foo", "/myPath", true, null);

        // Check that we accept reserved attribute names (e.g expires, domain) in any case
        checkCookie("toto=foo; PATH=/myPath; SeCURE",
              "toto", "foo", "/myPath", true, null);

        // Check that we are able to parse and set the expiration date correctly
        final String dateString = "Fri, 21 Jul 2023 20:47:11 UTC";
        final Date date = DateUtils.parseDate(dateString, "EEE, dd MMM yyyy HH:mm:ss z");
        checkCookie("toto=foo; expires=" + dateString, "toto", "foo", "/", false, date);
    }

    private void checkCookie(final String cookieString, final String name, final String value,
            final String path, final boolean secure, final Date date) {

        final String domain = URL_FIRST.getHost();

        getWebClient().getCookieManager().clearCookies();
        getWebClient().addCookie(cookieString, URL_FIRST, this);
        final Cookie cookie = getWebClient().getCookieManager().getCookies().iterator().next();

        assertEquals(name, cookie.getName());
        assertEquals(value, cookie.getValue());
        assertEquals(path, cookie.getPath());
        assertEquals(domain, cookie.getDomain());
        assertEquals(secure, cookie.isSecure());
        assertEquals(date, cookie.getExpires());
    }

    /**
     * @throws Exception if something goes wrong
     */
    @Test
    @NotYetImplemented
    public void makeSureTheCurrentJobHasEndedBeforeReplaceWindowPage() throws Exception {
        final String htmlContent1
            = "<html>\n"
            + "<head>"
            + "  <title>Page 1</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <script>\n"
            + "    function loadExtraContent() {\n"
            + "      for (var i = 0; i < 1000; i++) {\n"
            + "        var p = document.createElement('p');\n"
            + "        p.innerHTML = 'new content';\n"
            + "        var body = document.querySelector('body');\n"
            + "        if (body) { body.appendChild(p); }\n"
            + "      }\n"
            + "    }\n"

            + "    setTimeout(loadExtraContent, 1);"
            + "  </script>\n"
            + "</body>\n"
            + "</html>";

        final String htmlContent2
            = "<html>\n"
            + "<head>"
            + "  <title>Page 2</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <h1>Page2</h1>\n"
            + "  <p>This is page 2</p>\n"
            + "</body>\n"
            + "</html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent1);
        webConnection.setResponse(URL_SECOND, htmlContent2);
        client.setWebConnection(webConnection);

        // Load page 1. Has a setTimeout(...) function
        HtmlPage page = client.getPage(URL_FIRST);
        Thread.sleep(100);

        // Immediately load page 2. Timeout function was triggered already
        page = client.getPage(URL_SECOND);
        client.waitForBackgroundJavaScriptStartingBefore(100);

        // Fails: return 98 (about) instead of 1
        assertEquals(1, page.querySelectorAll("p").size());
    }
}
