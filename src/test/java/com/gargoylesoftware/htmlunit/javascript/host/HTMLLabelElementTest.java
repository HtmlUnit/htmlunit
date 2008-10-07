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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlLabel;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link HTMLLabelElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
public class HTMLLabelElementTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testHtmlFor() throws Exception {
        testHtmlFor(BrowserVersion.INTERNET_EXPLORER_7);
        testHtmlFor(BrowserVersion.FIREFOX_2);
    }

    private void testHtmlFor(final BrowserVersion browserVersion) throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<label id='label1'>My Label</label>\n"
            + "<input type='checkbox' id='checkbox1'><br>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlLabel label = page.getHtmlElementById("label1");
        final HtmlCheckBoxInput checkbox = page.getHtmlElementById("checkbox1");
        assertFalse(checkbox.isChecked());
        label.click();
        assertTrue(checkbox.isChecked());
    }

    /**
     * Tests that clicking the label by JavaScript does not change 'htmlFor' attribute in FF!!
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testHtmlFor_click() throws Exception {
        if (notYetImplemented()) {
            return;
        }
        testHtmlFor_click(BrowserVersion.INTERNET_EXPLORER_7, true);
        testHtmlFor_click(BrowserVersion.FIREFOX_2, false);
    }

    /**
     * @param changedByClick if 'label.click()' JavaScript causes the associated 'htmlFor' element to be changed
     */
    private void testHtmlFor_click(final BrowserVersion browserVersion, final boolean changedByClick) throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    document.getElementById('label1').htmlFor = 'checkbox1';\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<label id='label1'>My Label</label>\n"
            + "<input type='checkbox' id='checkbox1'><br>\n"
            + "<input type=button id='button1' value='Test' onclick='document.getElementById(\"label1\").click()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final HtmlCheckBoxInput checkbox = page.getHtmlElementById("checkbox1");
        final HtmlButtonInput button = page.getHtmlElementById("button1");
        assertFalse(checkbox.isChecked());
        button.click();
        assertTrue(checkbox.isChecked() == changedByClick);
    }
}
