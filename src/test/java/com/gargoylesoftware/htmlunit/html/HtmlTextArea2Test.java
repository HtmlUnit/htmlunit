/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
 * Tests for {@link HtmlTextInput}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlTextArea2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-", "-", "newValue-", "newValue-", "newValue-newDefault", "newValue-newDefault" })
    public void resetByClick() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    text.value = 'newValue';\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    document.getElementById('testReset').click;\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    text.defaultValue = 'newDefault';\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <textarea id='testId' value='initial'></textarea>\n"
            + "  <input type='reset' id='testReset'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "-", "-", "newValue-", "newValue-", "newValue-newDefault", "newValue-newDefault" })
    public void resetByJS() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    text.value = 'newValue';\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    text.defaultValue = 'newDefault';\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    document.forms[0].reset;\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <textarea id='testId' value='initial'></textarea>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "-", "default-default", "some text-default", "some text-newdefault" },
            IE = { "-", "-default", "some text-default", "some text-newdefault" })
    public void defaultValue() throws Exception {
        final String html = "<!DOCTYPE HTML>\n<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var text = document.getElementById('testId');\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    text.defaultValue = 'default';\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"

            + "    text.value = 'some text';\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"
            + "    text.defaultValue = 'newdefault';\n"
            + "    alert(text.value + '-' + text.defaultValue);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <textarea id='testId' value='initial'></textarea>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
