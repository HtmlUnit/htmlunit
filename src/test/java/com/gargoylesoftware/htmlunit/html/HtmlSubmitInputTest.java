/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.KeyValuePair;
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
        
        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");

        final HtmlSubmitInput submitInput = (HtmlSubmitInput) form.getInputByName("button");
        final HtmlPage secondPage = (HtmlPage) submitInput.click();
        assertEquals("foo", secondPage.getTitleText());

        assertEquals(
            Collections.singletonList(new KeyValuePair("button", "foo")),
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
        
        final HtmlForm form = (HtmlForm) page.getHtmlElementById("form1");
        final HtmlSubmitInput submitInput = (HtmlSubmitInput) form.getInputByName("button");

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

        final MockWebConnection webConnection = new MockWebConnection(client);
        webConnection.setResponse(URL_FIRST, firstContent);
        webConnection.setDefaultResponse(secondContent);

        client.setWebConnection(webConnection);

        final HtmlPage firstPage = (HtmlPage) client.getPage(URL_FIRST);
        final HtmlSubmitInput input = (HtmlSubmitInput) firstPage.getHtmlElementById("button1");
        final HtmlPage secondPage = (HtmlPage) input.click();
        assertEquals("Second", secondPage.getTitleText());
    }

    /**
     * @throws Exception If the test fails
     */
    @Test
    public void testDefaultValue() throws Exception {
        final String[] expectedAlertsIE = {"Submit Query"};
        testDefaultValue(BrowserVersion.INTERNET_EXPLORER_6_0, expectedAlertsIE);
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
     * @throws Exception If the test fails
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
     * @throws Exception If the test fails
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
        final HtmlSubmitInput input = (HtmlSubmitInput) page.getHtmlElementById("myInput");
        input.click();
        
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
