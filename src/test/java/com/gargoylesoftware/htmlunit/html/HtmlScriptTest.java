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
package com.gargoylesoftware.htmlunit.html;

import static org.junit.Assert.fail;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HtmlScript}.
 *
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlScriptTest extends SimpleWebTestCase {

    /**
     * Verifies that a failing HTTP status code for a JavaScript file request (like a 404 response)
     * results in a {@link FailingHttpStatusCodeException}, depending on how the client has been
     * configured.
     *
     * @see com.gargoylesoftware.htmlunit.WebClientOptions#isThrowExceptionOnFailingStatusCode()
     * @throws Exception if an error occurs
     */
    @Test
    public void badExternalScriptReference() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
                + "<script src='inexistent.js'></script>\n"
                + "</head><body></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse("inexistent", 404, "Not Found", MimeType.TEXT_HTML);
        webConnection.setResponse(URL_FIRST, html);
        client.setWebConnection(webConnection);

        try {
            client.getPage(URL_FIRST);
            fail("Should throw.");
        }
        catch (final FailingHttpStatusCodeException e) {
            final String url = URL_FIRST.toExternalForm();
            assertTrue("exception contains URL of failing script", e.getMessage().indexOf(url) > -1);
            assertEquals(404, e.getStatusCode());
            assertEquals("Not Found", e.getStatusMessage());
        }

        client.getOptions().setThrowExceptionOnFailingStatusCode(false);

        try {
            client.getPage(URL_FIRST);
        }
        catch (final FailingHttpStatusCodeException e) {
            fail("Should not throw.");
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void asText() throws Exception {
        final String html = "<html><body><script id='s'>var foo = 132;</script></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlScript script = page.getHtmlElementById("s");
        assertEquals("", script.asText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("hello")
    public void asXml() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<script id='script1'>\n"
            + "  alert('hello');\n"
            + "</script></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);

        // asXml() should be reusable
        final String xml = page.asXml();
        loadPageWithAlerts(xml);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void asXml_scriptNestedInCData() throws Exception {
        final String script = "//<![CDATA[\n"
            + "var foo = 132;\n"
            + "//]]>";
        final String html = "<html><body><script id='s'>" + script + "</script></body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlScript scriptElement = page.getHtmlElementById("s");
        assertEquals("<script id=\"s\">\r\n" + script + "\r\n</script>\r\n",
                scriptElement.asXml());
    }

    /**
     * Verifies that cloned script nodes do not reload or re-execute their content (bug 1954869).
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("loaded")
    public void scriptCloneDoesNotReloadScript() throws Exception {
        final String html = "<html><body><script src='" + URL_SECOND + "'></script></body></html>";
        final String js = "alert('loaded')";

        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, js, MimeType.APPLICATION_JAVASCRIPT);
        client.setWebConnection(conn);

        final List<String> actual = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(2, conn.getRequestCount());

        page.cloneNode(true);
        assertEquals(2, conn.getRequestCount());

        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * Verifies that we're lenient about whitespace before and after URLs in the "src" attribute.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("ok")
    public void whitespaceInSrc() throws Exception {
        final String html = "<html><head><script src=' " + URL_SECOND + " '></script></head><body>abc</body></html>";
        final String js = "alert('ok')";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webConnection.setResponse(URL_SECOND, js);
        client.setWebConnection(webConnection);

        final List<String> collectedAlerts = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Verifies that we're lenient about empty "src" attributes.
     * @throws Exception if an error occurs
     */
    @Test
    public void emptySrc() throws Exception {
        final String html1 = "<html><head><script src=''></script></head><body>abc</body></html>";
        final String html2 = "<html><head><script src='  '></script></head><body>abc</body></html>";

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html1);
        webConnection.setResponse(URL_SECOND, html2);
        client.setWebConnection(webConnection);

        client.getPage(URL_FIRST);
        assertEquals(1, webConnection.getRequestCount());

        client.getPage(URL_SECOND);
        assertEquals(2, webConnection.getRequestCount());
    }

    /**
     * Verifies that 204 (No Content) responses for script resources are handled gracefully.
     * @throws Exception on test failure
     * @see <a href="https://sourceforge.net/tracker/?func=detail&atid=448266&aid=2815903&group_id=47038">2815903</a>
     */
    @Test
    public void noContent() throws Exception {
        final String html = "<html><body><script src='" + URL_SECOND + "'/></body></html>";
        final WebClient client = getWebClient();
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        final ArrayList<NameValuePair> headers = new ArrayList<>();
        conn.setResponse(URL_SECOND, (String) null, HttpStatus.SC_NO_CONTENT, "No Content",
                MimeType.APPLICATION_JAVASCRIPT,
                headers);
        client.setWebConnection(conn);
        client.getPage(URL_FIRST);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"c", "f"})
    public void addEventListener_error_clientThrows() throws Exception {
        addEventListener_error(true);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"c", "f"})
    public void addEventListener_error_clientDoesNotThrow() throws Exception {
        addEventListener_error(false);
    }

    private void addEventListener_error(final boolean throwOnFailingStatusCode) throws Exception {
        final URL fourOhFour = new URL(URL_FIRST, "/404");
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var s1 = document.createElement('script');\n"
            + "    s1.text = 'var foo';\n"
            + "    s1.addEventListener('error', function() {alert('a')}, false);\n"
            + "    document.body.insertBefore(s1, document.body.firstChild);\n"
            + "    \n"
            + "    var s2 = document.createElement('script');\n"
            + "    s2.text = 'varrrr foo';\n"
            + "    s2.addEventListener('error', function() {alert('b')}, false);\n"
            + "    document.body.insertBefore(s2, document.body.firstChild);\n"
            + "    \n"
            + "    var s3 = document.createElement('script');\n"
            + "    s3.src = '//:';\n"
            + "    s3.addEventListener('error', function() {alert('c')}, false);\n"
            + "    document.body.insertBefore(s3, document.body.firstChild);\n"
            + "    \n"
            + "    var s4 = document.createElement('script');\n"
            + "    s4.src = '" + URL_SECOND + "';\n"
            + "    s4.addEventListener('error', function() {alert('d')}, false);\n"
            + "    document.body.insertBefore(s4, document.body.firstChild);\n"
            + "    \n"
            + "    var s5 = document.createElement('script');\n"
            + "    s5.src = '" + URL_THIRD + "';\n"
            + "    s5.addEventListener('error', function() {alert('e')}, false);\n"
            + "    document.body.insertBefore(s5, document.body.firstChild);\n"
            + "    \n"
            + "    var s6 = document.createElement('script');\n"
            + "    s6.src = '" + fourOhFour + "';\n"
            + "    s6.addEventListener('error', function() {alert('f')}, false);\n"
            + "    document.body.insertBefore(s6, document.body.firstChild);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        final WebClient client = getWebClient();
        client.getOptions().setThrowExceptionOnFailingStatusCode(throwOnFailingStatusCode);
        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setResponse(URL_SECOND, "var foo;", MimeType.APPLICATION_JAVASCRIPT);
        conn.setResponse(URL_THIRD, "varrrr foo;", MimeType.APPLICATION_JAVASCRIPT);
        conn.setResponse(fourOhFour, "", 404, "Missing", MimeType.APPLICATION_JAVASCRIPT,
                new ArrayList<NameValuePair>());
        client.setWebConnection(conn);
        final List<String> actual = new ArrayList<>();
        client.setAlertHandler(new CollectingAlertHandler(actual));
        client.getOptions().setThrowExceptionOnScriptError(false);
        client.getPage(URL_FIRST);
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isDisplayed() throws Exception {
        final String html = "<html><head><title>Page A</title></head><body><script>var x = 1;</script></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        final HtmlScript script = page.getFirstByXPath("//script");
        assertFalse(script.isDisplayed());
    }

    /**
     * Verifies that if a script element executes "window.location.href=someotherpage", then subsequent
     * script tags, and any onload handler for the original page do not run.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"First script executes", "Second page loading"})
    public void changingLocationSkipsFurtherScriptsOnPage() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body onload='alert(\"body onload executing but should be skipped\")'>\n"
            + "<script>alert('First script executes')</script>\n"
            + "<script>window.location.href='" + URL_SECOND + "'</script>\n"
            + "<script>alert('Third script executing but should be skipped')</script>\n"
            + "</body></html>";

        final String secondPage
            = "<html><head></head><body>\n"
            + "<script>alert('Second page loading')</script>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondPage);
        loadPageWithAlerts(html);
    }
}
