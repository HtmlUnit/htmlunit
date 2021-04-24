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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;

/**
 * Tests for {@link HtmlLabel}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlLabel2Test extends SimpleWebTestCase {

    /**
     * Verifies that a checkbox is toggled when the related label is clicked.
     * @throws Exception if the test fails
     */
    @Test
    public void test_click() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "  <input type='checkbox' name='checkbox' id='testCheckbox' onclick='alert(\"checkbox\")'/>\n"
            + "  <label for='testCheckbox' id='testLabel' onclick='alert(\"label\")'>Check me</label>\n"
            + "</form></body></html>";
        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(htmlContent, collectedAlerts);
        final HtmlCheckBoxInput checkBox = page.getHtmlElementById("testCheckbox");

        assertFalse(checkBox.isChecked());
        final HtmlLabel label = page.getHtmlElementById("testLabel");
        label.click();
        assertTrue(checkBox.isChecked());
        final String[] expectedAlerts = {"label", "checkbox"};
        assertEquals(expectedAlerts, collectedAlerts);
        label.click();
        assertFalse(checkBox.isChecked());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void triggerRadio() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul>\n"
            + "  <li>\n"
            + "    <label id='radio1Label' for='radio1'>Radio 1</label>\n"
            + "    <input id='radio1' name='radios' value='1' type='radio'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "</body></html>";

        getWebClient().getOptions().setJavaScriptEnabled(false);
        final HtmlPage page = loadPage(html);
        assertFalse(((HtmlRadioButtonInput) page.getElementById("radio1")).isChecked());

        page.getElementById("radio1Label").click();

        assertTrue(((HtmlRadioButtonInput) page.getElementById("radio1")).isChecked());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void triggerRadioComplexCase() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul>\n"
            + "  <li>\n"
            + "    <label id='radio1Label' for='radio1'>\n"
            + "      <span>\n"
            + "        <input id='radio1' name='radios' value='1' type='radio'>\n"
            + "        <span id='radio1Span'>Radio 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "</body></html>";

        getWebClient().getOptions().setJavaScriptEnabled(false);
        final HtmlPage page = loadPage(html);
        assertFalse(((HtmlRadioButtonInput) page.getElementById("radio1")).isChecked());

        page.getElementById("radio1Label").click();

        assertTrue(((HtmlRadioButtonInput) page.getElementById("radio1")).isChecked());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void triggerCheckbox() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul>\n"
            + "  <li>\n"
            + "    <label id='check1Label' for='check1'>Checkbox 1</label>\n"
            + "    <input id='check1' name='checks' value='1' type='checkbox'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "</body></html>";

        getWebClient().getOptions().setJavaScriptEnabled(false);
        final HtmlPage page = loadPage(html);
        assertFalse(((HtmlCheckBoxInput) page.getElementById("check1")).isChecked());

        page.getElementById("check1Label").click();

        assertTrue(((HtmlCheckBoxInput) page.getElementById("check1")).isChecked());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void triggerCheckboxComplexCase() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul>\n"
            + "  <li>\n"
            + "    <label id='check1Label' for='check1'>\n"
            + "      <span>\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox'>\n"
            + "        <span id='check1Span'>Checkbox 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "</body></html>";

        getWebClient().getOptions().setJavaScriptEnabled(false);
        final HtmlPage page = loadPage(html);
        assertFalse(((HtmlCheckBoxInput) page.getElementById("check1")).isChecked());

        page.getElementById("check1Label").click();

        assertTrue(((HtmlCheckBoxInput) page.getElementById("check1")).isChecked());
    }
}
