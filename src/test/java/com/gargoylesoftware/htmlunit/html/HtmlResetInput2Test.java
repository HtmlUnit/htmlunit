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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlResetInput}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlResetInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "-", "-", "-", "-" },
            IE = { "Reset-", "Reset-", "Reset-", "Reset-" })
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('reset1');\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'reset';\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"reset\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"

            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='reset' id='reset1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault" },
            IE = { "initial-initial", "initial-initial", "newValue-initial", "newValue-initial",
                "newValue-newDefault", "newValue-newDefault" })
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var reset = document.getElementById('testId');\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    reset.value = 'newValue';\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    reset.defaultValue = 'newDefault';\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='reset' id='testId' value='initial'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-initial", "initial-initial", "newValue-newValue", "newValue-newValue",
                "newDefault-newDefault", "newDefault-newDefault" },
            IE = { "initial-initial", "initial-initial", "newValue-initial", "newValue-initial",
                "newValue-newDefault", "newValue-newDefault" })
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var reset = document.getElementById('testId');\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    reset.value = 'newValue';\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    reset.defaultValue = 'newDefault';\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='reset' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "initial-initial", "default-default", "newValue-newValue", "newdefault-newdefault" },
            IE8 = { "initial-initial", "initial-default", "newValue-default", "newValue-newdefault" })
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var reset = document.getElementById('testId');\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    reset.defaultValue = 'default';\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"

            + "    reset.value = 'newValue';\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"
            + "    reset.defaultValue = 'newdefault';\n"
            + "    alert(reset.value + '-' + reset.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='reset' id='testId' value='initial'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
