/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link HtmlSubmitInput}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HtmlSubmitInputTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSubmit() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' method='post'>\n"
            + "<input type='submit' name='aButton' value='foo'/>\n"
            + "<input type='suBMit' name='button' value='foo'/>\n"
            + "<input type='submit' name='anotherButton' value='foo'/>\n"
            + "</form></body></html>";
        final HtmlPage page = loadPage(htmlContent);
        final MockWebConnection webConnection = getMockConnection(page);

        final HtmlForm form = page.getHtmlElementById("form1");

        final HtmlSubmitInput submitInput = form.getInputByName("button");
        final HtmlPage secondPage = submitInput.click();
        assertEquals("foo", secondPage.getTitleText());

        assertEquals(
            Collections.singletonList(new NameValuePair("button", "foo")),
            webConnection.getLastParameters());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onClick() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1' onSubmit='alert(\"bar\")'>\n"
            + "    <input type='submit' name='button' value='foo' onClick='alert(\"foo\")'/>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);

        final HtmlForm form = page.getHtmlElementById("form1");
        final HtmlSubmitInput submitInput = form.getInputByName("button");

        submitInput.click();

        final String[] expectedAlerts = {"foo", "bar"};
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testClick_onClick_JavascriptReturnsTrue() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head><body>\n"
            + "<form name='form1' method='get' action='" + URL_SECOND + "'>\n"
            + "<input name='button' type='submit' value='PushMe' id='button1'"
            + "onclick='return true'/></form>\n"
            + "</body></html>";
        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = new WebClient();
        final List<String> collectedAlerts = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(collectedAlerts));

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = client.getPage(URL_FIRST);
        final HtmlSubmitInput input = firstPage.getHtmlElementById("button1");
        final HtmlPage secondPage = input.click();
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testDefaultValue() throws Exception {
        final String[] expectedAlertsIE = {"Submit Query"};
        testDefaultValue(BrowserVersion.INTERNET_EXPLORER_6, expectedAlertsIE);
        final String[] expectedAlertsFF = {""};
        testDefaultValue(BrowserVersion.FIREFOX_2, expectedAlertsFF);
    }

    private void testDefaultValue(final BrowserVersion browserVersion, final String[] expectedAlerts)
        throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "  <input type='submit' id='myId'>\n"
            + "</form>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();

        final HtmlPage page = loadPage(browserVersion, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
        assertTrue(page.asText().indexOf("Submit Query") > -1);
        assertFalse(page.asXml().indexOf("Submit Query") > -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEmptyValue() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form action='" + URL_SECOND + "'>\n"
            + "  <input type='submit' id='myId' value=''>\n"
            + "</form>\n"
            + "</body></html>";

        final String[] expectedAlerts = {""};
        final List<String> collectedAlerts = new ArrayList<String>();

        final HtmlPage page = loadPage(html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
        assertFalse(page.asText().indexOf("Submit Query") > -1);
        assertTrue(page.asXml().indexOf("value=\"\"") > -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testOutsideForm() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<input id='myInput' type='submit' onclick='alert(1)'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"1"};
        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(html, collectedAlerts);
        final HtmlSubmitInput input = page.getHtmlElementById("myInput");
        input.click();

        assertEquals(expectedAlerts, collectedAlerts);
    }
}
