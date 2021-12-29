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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLOutputElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLOutputElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"0", "2", "1", "2", "1", "1"},
            IE = {"undefined", "undefined", "undefined", "undefined", "undefined", "undefined"})
    public void labels() throws Exception {
        final String html =
            "<html><head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      debug(document.getElementById('e1'));\n"
            + "      debug(document.getElementById('e2'));\n"
            + "      debug(document.getElementById('e3'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      var labels = document.getElementById('e4').labels;\n"
            + "      document.body.removeChild(document.getElementById('l4'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      log(labels ? labels.length : labels);\n"
            + "    }\n"
            + "    function debug(e) {\n"
            + "      log(e.labels ? e.labels.length : e.labels);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <output id='e1'>e 1</output><br>\n"
            + "  <label>something <label> click here <output id='e2'>e 2</output></label></label><br>\n"
            + "  <label for='e3'> and here</label>\n"
            + "  <output id='e3'>e 3</output><br>\n"
            + "  <label id='l4' for='e4'> what about</label>\n"
            + "  <label> this<output id='e4'>e 4</output></label><br>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"false", "false", "false", "false", "false"},
            FF_ESR = {"true", "true", "true", "true", "true"},
            IE = {"undefined", "undefined", "undefined", "undefined", "undefined"})
    public void willValidate() throws Exception {
        final String html =
                "<html><head>\n"
                + "  <script>\n"
                + LOG_TITLE_FUNCTION
                + "    function test() {\n"
                + "      log(document.getElementById('o1').willValidate);\n"
                + "      log(document.getElementById('o2').willValidate);\n"
                + "      log(document.getElementById('o3').willValidate);\n"
                + "      log(document.getElementById('o4').willValidate);\n"
                + "      log(document.getElementById('o5').willValidate);\n"
                + "    }\n"
                + "  </script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "  <form>\n"
                + "    <output id='o1'>o1</output>\n"
                + "    <output id='o2' disabled>o2</output>\n"
                + "    <output id='o3' hidden>o3</output>\n"
                + "    <output id='o4' readonly>o4</output>\n"
                + "    <output id='o5' style='display: none'>o5</output>\n"
                + "  </form>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
