/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlHiddenInput}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlHiddenInputTest extends WebDriverTestCase {

    /**
     * Verifies that a asText() returns "checked" or "unchecked" according to the state of the checkbox.
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String htmlContent
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='hidden' name='foo' id='foo' value='bla'>\n"
            + "</form></body></html>";

        final WebDriver driver = loadPage2(htmlContent);

        final WebElement input = driver.findElement(By.id("foo"));
        assertEquals("", input.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-initial", "initial-initial", "some text-some text", "some text-some text" },
    		IE8 = { "initial-initial", "initial-initial", "some text-initial", "some text-initial" })
    public void reset() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var hidden = document.getElementById('testId');\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue);\n"

            + "    hidden.value = 'some text';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-initial", "default-default", "some text-some text", "newdefault-newdefault" },
    		IE8 = { "initial-initial", "initial-default", "some text-default", "some text-newdefault" })
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var hidden = document.getElementById('testId');\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue);\n"

            + "    hidden.defaultValue = 'default';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue);\n"

            + "    hidden.value = 'some text';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue);\n"
            + "    hidden.defaultValue = 'newdefault';\n"
            + "    alert(hidden.value + '-' + hidden.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='hidden' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
