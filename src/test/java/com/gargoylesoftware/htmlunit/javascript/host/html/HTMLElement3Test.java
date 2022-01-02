/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLElement}.
 *
 * @author Brad Clarke
 * @author Chris Erskine
 * @author David D. Kilzer
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Hans Donner
 * @author <a href="mailto:george@murnock.com">George Murnock</a>
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Ethan Glasser-Camp
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLElement3Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickHashAnchor() throws Exception {
        final String html
            = "<html><head><title>HashAnchor</title></head>\n"
            + "<body>\n"
            + "  <script language='javascript'>\n"
            + "    function test() {alert('test hash');}\n"
            + "  </script>\n"
            + "  <a onClick='javascript:test();' href='#' name='hash'>Click</a>\n"
            + "</body>\n"
            + "</html>";
        final String[] expectedAlerts = {"test hash"};
        // first use direct load
        final List<String> loadCollectedAlerts = new ArrayList<>();
        final HtmlPage loadPage = loadPage(html, loadCollectedAlerts);
        final HtmlAnchor loadHashAnchor = loadPage.getAnchorByName("hash");
        loadHashAnchor.click();

        assertEquals(expectedAlerts, loadCollectedAlerts);

        // finally try via the client
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html);
        webClient.setWebConnection(webConnection);
        final CollectingAlertHandler clientCollectedAlertsHandler = new CollectingAlertHandler();
        webClient.setAlertHandler(clientCollectedAlertsHandler);
        final HtmlPage clientPage = webClient.getPage(URL_FIRST);
        final HtmlAnchor clientHashAnchor = clientPage.getAnchorByName("hash");
        clientHashAnchor.click();

        assertEquals(expectedAlerts, clientCollectedAlertsHandler.getCollectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onfocus text1", "onfocus text2", "onfocus text1", "onfocus text2"})
    public void onFocusOnWindowFocusGain() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstHtml = "<html><head><title>First</title></head>\n"
            + "<body><form name='form1'>\n"
            + "<input id='text1' onfocus='alert(\"onfocus text1\")'>\n"
            + "<button type='button' id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
            + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);

        final String secondHtml = "<html><head><title>Second</title></head>\n"
            + "<body onLoad='doTest()'>\n"
            + "<input id='text2' onfocus='alert(\"onfocus text2\")'>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            + "    opener.document.getElementById('text1').focus();\n"
            + "    document.getElementById('text2').focus();\n"
            + "  }\n"
            + "</script></body></html>";

        webConnection.setResponse(URL_SECOND, secondHtml);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        final HtmlPage secondPage = buttonA.click();
        assertEquals("Second", secondPage.getTitleText());
        webClient.setCurrentWindow(firstPage.getEnclosingWindow());
        webClient.setCurrentWindow(secondPage.getEnclosingWindow());
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"onblur text2", "onblur text1"})
    public void onBlurOnWindowFocusChange() throws Exception {
        final WebClient webClient = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        final List<String> collectedAlerts = new ArrayList<>();

        webClient.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final String firstHtml = "<html><head><title>First</title></head>\n"
            + "<body><form name='form1'>\n"
            + "<input id='text1' onblur='alert(\"onblur text1\")'>\n"
            + "<button type='button' id='clickme' onClick='window.open(\"" + URL_SECOND + "\");'>Click me</a>\n"
            + "</form></body></html>";
        webConnection.setResponse(URL_FIRST, firstHtml);

        final String secondHtml = "<html><head><title>Second</title></head>\n"
            + "<body onLoad='doTest()'>\n"
            + "<input id='text2' onblur='alert(\"onblur text2\")'>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            + "    opener.document.getElementById('text1').focus();\n"
            + "    document.getElementById('text2').focus();\n"
            + "  }\n"
            + "</script></body></html>";

        webConnection.setResponse(URL_SECOND, secondHtml);
        webClient.setWebConnection(webConnection);

        final HtmlPage firstPage = webClient.getPage(URL_FIRST);
        assertEquals("First", firstPage.getTitleText());

        final HtmlButton buttonA = firstPage.getHtmlElementById("clickme");
        final HtmlPage secondPage = buttonA.click();
        assertEquals("Second", secondPage.getTitleText());
        webClient.setCurrentWindow(firstPage.getEnclosingWindow());
        webClient.setCurrentWindow(secondPage.getEnclosingWindow());
        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void offsetHeight() throws Exception {
        final String html
            = "<html><head></head>\n"
            + "<body>\n"
            + "<div>1</div>\n"
            + "</body>\n"
            + "</html>";
        final HtmlPage page = loadPage(html);
        final HTMLElement host = (HTMLElement) page.<HtmlElement>getFirstByXPath("//div").getScriptableObject();
        final int offsetHeight = host.getOffsetHeight();
        assertTrue(offsetHeight > 0);
    }

}
