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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlButton}.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Brad Clarke
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlButton2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLButtonElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<button id='myId'>OK</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlButton.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "-undefined", "-", "-" },
            IE = { "OK-undefined", "-", "-" })
    @NotYetImplemented(Browser.IE)
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('button1');\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'button';\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"button\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='button1'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-undefined", "-", "-" })
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('button1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'button';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"button\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='button1'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-undefined", "initial-undefined", "newValue-undefined", "newValue-undefined",
                "newValue-newDefault", "newValue-newDefault" },
            IE = { "OK-undefined", "OK-undefined", "newValue-undefined", "newValue-undefined",
                "newValue-newDefault", "newValue-newDefault" })
    @NotYetImplemented(Browser.IE)
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'newValue';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'newDefault';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId' value='initial'>OK</button>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-undefined", "initial-undefined", "newValue-undefined", "newValue-undefined",
                "newValue-newDefault", "newValue-newDefault" },
            IE = { "OK-undefined", "OK-undefined", "newValue-undefined", "newValue-undefined",
                "newValue-newDefault", "newValue-newDefault" })
    @NotYetImplemented(Browser.IE)
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'newValue';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'newDefault';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId' value='initial'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-undefined", "initial-default", "newValue-default", "newValue-newDefault" },
            IE8 = { "OK-undefined", "OK-default", "newValue-default", "newValue-newDefault" })
    @NotYetImplemented(Browser.IE)
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var button = document.getElementById('testId');\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    button.defaultValue = 'default';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"

            + "    button.value = 'newValue';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"
            + "    button.defaultValue = 'newDefault';\n"
            + "    alert(button.value + '-' + button.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <button id='testId' value='initial'>OK</button>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
