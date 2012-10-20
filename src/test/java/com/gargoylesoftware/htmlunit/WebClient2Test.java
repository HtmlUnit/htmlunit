/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link WebClient} that run with BrowserRunner.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class WebClient2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "http://first/?param=\u00A3", FF = "http://first/?param=%A3")
    public void encodeURL() throws Exception {
        final String html = "<body onload='alert(window.location.href)'></body>";
        final WebClient webClient = getWebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(html);

        webClient.setWebConnection(webConnection);
        webClient.getPage("http://first/?param=\u00A3");
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * Test that the path and query string are encoded to be valid.
     * @throws Exception if something goes wrong
     */
    @Test
    public void loadPage_EncodeRequest() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setDefaultResponse(htmlContent);
        client.setWebConnection(webConnection);

        // with query string not encoded
        HtmlPage page = client.getPage("http://first?a=b c&d=\u00E9\u00E8");
        String expected;
        final boolean ie = getBrowserVersion().isIE();
        if (ie) {
            expected = "?a=b%20c&d=\u00E9\u00E8";
        }
        else {
            expected = "?a=b%20c&d=%E9%E8";
        }
        assertEquals("http://first/" + expected, page.getUrl());

        // with query string already encoded
        page = client.getPage("http://first?a=b%20c&d=%C3%A9%C3%A8");
        assertEquals("http://first/?a=b%20c&d=%C3%A9%C3%A8", page.getUrl());

        // with query string partially encoded
        page = client.getPage("http://first?a=b%20c&d=e f");
        assertEquals("http://first/?a=b%20c&d=e%20f", page.getUrl());

        // with anchor
        page = client.getPage("http://first?a=b c#myAnchor");
        assertEquals("http://first/?a=b%20c#myAnchor", page.getUrl());

        // with query string containing encoded "&", "=", "+", ",", and "$"
        page = client.getPage("http://first?a=%26%3D%20%2C%24");
        assertEquals("http://first/?a=%26%3D%20%2C%24", page.getUrl());

        // with character to encode in path
        page = client.getPage("http://first/page 1.html");
        assertEquals("http://first/page%201.html", page.getUrl());

        // with character to encode in path
        page = client.getPage("http://first/page 1.html");
        assertEquals("http://first/page%201.html", page.getUrl());
    }

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
        final WebClient client = getWebClient();
        final HtmlPage page1 = loadPage(client, page1Content, null, URL_FIRST);
        assertEquals("hello 1", page1.asText());

        final String page2Content = "<html><body>hello 2</body></html>";
        final WebClient copy = clone(client);
        final HtmlPage page2 = loadPage(copy, page2Content, null, URL_SECOND);
        assertEquals("hello 2", page2.asText());
        copy.closeAllWindows();
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
        page.getWebClient().closeAllWindows();

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
    public void acceptLanguage() throws Exception {
        final String html = "<html><body></body></html>";
        final HtmlPage p = loadPageWithAlerts(html);
        assertEquals("en-us", p.getWebResponse().getWebRequest().getAdditionalHeaders().get("Accept-Language"));

        final WebClient client = p.getWebClient();
        final String lang = client.getBrowserVersion().getBrowserLanguage();
        try {
            client.getBrowserVersion().setBrowserLanguage("fr");
            final HtmlPage p2 = client.getPage(getDefaultUrl());
            assertEquals("fr", p2.getWebResponse().getWebRequest().getAdditionalHeaders().get("Accept-Language"));
        }
        finally {
            // Restore original language.
            client.getBrowserVersion().setBrowserLanguage(lang);
        }
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

}
