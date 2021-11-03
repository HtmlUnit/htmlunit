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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLMeterElement}.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLMeterElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLMeterElement]",
            IE = "[object HTMLUnknownElement]")
    public void tag() throws Exception {
        final String html = "<html><body>\n"
            + "<meter id='it' min='200' max='500' value='350'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(document.getElementById('it'));\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"number200", "number500", "number200", "number500", "number350", "number350"},
            IE = {})
    public void properties() throws Exception {
        final String html = "<html><body>\n"
            + "<meter id='it' min='200' max='500' value='350'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var elt = document.getElementById('it');\n"
            + "if (window.HTMLMeterElement) {\n"
            + "  log(typeof(elt.min) + elt.min);\n"
            + "  log(typeof(elt.max) + elt.max);\n"
            + "  log(typeof(elt.low) + elt.low);\n"
            + "  log(typeof(elt.high) + elt.high);\n"
            + "  log(typeof(elt.value) + elt.value);\n"
            + "  log(typeof(elt.optimum) + elt.optimum);\n"
            + "}\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

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
            + "  <meter id='e1'>e 1</meter><br>\n"
            + "  <label>something <label> click here <meter id='e2'>e 2</meter></label></label><br>\n"
            + "  <label for='e3'> and here</label>\n"
            + "  <meter id='e3'>e 3</meter><br>\n"
            + "  <label id='l4' for='e4'> what about</label>\n"
            + "  <label> this<meter id='e4'>e 4</meter></label><br>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
