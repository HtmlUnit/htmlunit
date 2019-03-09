/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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
 * Tests for {@link HtmlRangeInput}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlRangeInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"50----", "50----", "50----"})
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('range1');\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'range';\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"range\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='range1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"50----", "50----", "50----"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('range1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input = document.createElement('input');\n"
            + "    input.type = 'range';\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"range\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='range1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7-7---", "7-7---", "4-7---", "4-7---"})
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.value = '4';\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    range.defaultValue = '2';\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId' value='7'>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7-7---", "7-7---", "4-7---", "4-7---"})
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.value = '4';\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.defaultValue = '2';\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId' value='7'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7-7---", "4-4---", "2-4---", "2-8---"})
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('testId');\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.defaultValue = '4';\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"

            + "    input.value = '2';\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "    input.defaultValue = '8';\n"
            + "    alert(input.value + '-' + input.defaultValue"
                    + " + '-' + input.max + '-' + input.min"
                    + " + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId' value='7'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"50----", "50--100-0-", "5--10-0-", "4--7-0-",
                "2--7--4-", "4.3--7.01-1.3-"})
    public void valueDependsOnMinMax() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    for (i = 1; i <= 6; i++) {\n"
            + "        var input = document.getElementById('testId' + i);\n"
            + "        alert(input.value + '-' + input.defaultValue"
                            + " + '-' + input.max + '-' + input.min"
                            + " + '-' + input.step);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId1'>\n"
            + "  <input type='range' id='testId2' min='0' max='100'>\n"
            + "  <input type='range' id='testId3' min='0' max='10'>\n"
            + "  <input type='range' id='testId4' min='0' max='7'>\n"
            + "  <input type='range' id='testId5' min='-4' max='7'>\n"
            + "  <input type='range' id='testId6' min='1.3' max='7.01'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"41-42-1234-2-13", "5-5-10-2-1", "6-5-10-2-2"})
    public void properties() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    for (i = 1; i <= 3; i++) {\n"
            + "        var input = document.getElementById('testId' + i);\n"
            + "        alert(input.value + '-' + input.defaultValue"
                            + " + '-' + input.max + '-' + input.min"
                            + " + '-' + input.step);\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='range' id='testId1'"
                        + " min='2' max='1234' value='42' step='13'>\n"
            + "  <input type='range' id='testId2'"
                        + " min='2' max='10' value='5' step='1'>\n"
            + "  <input type='range' id='testId3'"
                        + " min='2' max='10' value='5' step='2'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
