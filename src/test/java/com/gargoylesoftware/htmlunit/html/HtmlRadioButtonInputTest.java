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
package com.gargoylesoftware.htmlunit.html;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlRadioButtonInput}.
 *
 * @author Mike Bresnahan
 * @author Marc Guillemot
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlRadioButtonInputTest extends SimpleWebTestCase {

    /**
     * Verifies that asNormalizedText() returns "checked" or "unchecked" according to the state of the radio.
     * @throws Exception if the test fails
     */
    @Test
    public void asTextWhenNotChecked() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='radio' name='radio' id='radio'>Check me</input>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(html);

        final HtmlRadioButtonInput radio = page.getHtmlElementById("radio");
        assertEquals("unchecked", radio.asNormalizedText());
        assertEquals("uncheckedCheck me", page.asNormalizedText());
        radio.setChecked(true);
        assertEquals("checked", radio.asNormalizedText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("newtrue")
    public void onchangeHandlerInvoked() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='radio' name='radio' id='radio'"
            + "onchange='this.value=\"new\" + this.checked'>Check me</input>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlRadioButtonInput radio = page.getHtmlElementById("radio");

        assertFalse(radio.isChecked());

        radio.setChecked(true);

        assertTrue(radio.isChecked());
        assertEquals(getExpectedAlerts()[0], radio.getValueAttribute());
        assertEquals(getExpectedAlerts()[0], radio.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onchangeHandlerNotInvokedIfNotChanged() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='radio' name='radio' id='radio'"
            + "onchange='this.value=\"new\" + this.checked'>Check me</input>\n"
            + "</form></body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlRadioButtonInput radio = page.getHtmlElementById("radio");

        assertFalse(radio.isChecked());

        radio.setChecked(false);

        assertFalse(radio.isChecked());

        assertEquals("on", radio.getValueAttribute());
        assertEquals("on", radio.getValue());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"oneItem.checked: false twoItems.checked: true", "oneItem.checked: true twoItems.checked: false"})
    public void updateStateFirstForOnclickHandler() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<script type='text/javascript'>\n"
            + "  function itemOnClickHandler() {\n"
            + "    var oneItem = document.getElementById('oneItem');\n"
            + "    var twoItems = document.getElementById('twoItems');\n"
            + "    alert('oneItem.checked: ' + oneItem.checked + ' twoItems.checked: ' + twoItems.checked);\n"
            + "  }\n"
            + "</script>\n"
            + "<form name='testForm'>\n"
            + "Number of items:"
            + "<input type='radio' name='numOfItems' value='1' checked='checked' "
            + "  onclick='itemOnClickHandler()' id='oneItem'>\n"
            + "<label for='oneItem'>1</label>\n"
            + "<input type='radio' name='numOfItems' value='2' onclick='itemOnClickHandler()' id='twoItems'>\n"
            + "<label for='twoItems'>2</label>\n"
            + "</form></body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(html, collectedAlerts);

        final HtmlRadioButtonInput oneItem = page.getHtmlElementById("oneItem");
        final HtmlRadioButtonInput twoItems = page.getHtmlElementById("twoItems");

        assertTrue(oneItem.isChecked());
        assertFalse(twoItems.isChecked());

        twoItems.click();

        assertTrue(twoItems.isChecked());
        assertFalse(oneItem.isChecked());

        oneItem.click();

        assertTrue(oneItem.isChecked());
        assertFalse(twoItems.isChecked());

        assertEquals(getExpectedAlerts(), collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Second")
    public void setChecked() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input id='myRadio' type='radio' onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, firstHtml);
        webConnection.setResponse(URL_SECOND, secondHtml);
        client.setWebConnection(webConnection);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlRadioButtonInput radio = page.getHtmlElementById("myRadio");

        final HtmlPage secondPage = (HtmlPage) radio.setChecked(true);
        assertEquals(getExpectedAlerts()[0], secondPage.getTitleText());
    }

    /**
     * Regression test for bug #851.
     * Clicking an element should force the enclosing window to become the current one.
     * @throws Exception if the test fails
     */
    @Test
    public void clickResponse() throws Exception {
        final String html
            = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form name='myForm'>\n"
            + "  <input type='radio' name='myRadio' id='radio1' value=v1>\n"
            + "  <input type='radio' name='myRadio' value=v2>\n"
            + "  <button onclick='openPopup();' type='button' id='clickMe'>click me</button>\n"
            + "</form>\n"
            + "<script>\n"
            + "function doSomething() {\n"
            + "  // nothing\n"
            + "}\n"
            + "function openPopup() {\n"
            + "  window.open('popup.html');\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final WebClient webClient = page.getWebClient();
        assertSame(page.getEnclosingWindow(), webClient.getCurrentWindow());

        // open popup
        final HtmlPage page2 = page.getHtmlElementById("clickMe").click();
        assertNotSame(page, page2);
        assertSame(page2.getEnclosingWindow(), webClient.getCurrentWindow());

        // click radio buttons in the original page
        final HtmlPage page3 = page.getHtmlElementById("radio1").click();
        assertSame(page, page3);
        assertSame(page3.getEnclosingWindow(), webClient.getCurrentWindow());
    }
}
