/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLKeygenElement}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLKeygenElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "undefined", "undefined", "undefined", "undefined", "undefined" },
            CHROME = { "0", "2", "1", "2", "1", "1" })
    public void labels() throws Exception {
        final String html =
            "<html><head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      debug(document.getElementById('e1'));\n"
            + "      debug(document.getElementById('e2'));\n"
            + "      debug(document.getElementById('e3'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      var labels = document.getElementById('e4').labels;\n"
            + "      document.body.removeChild(document.getElementById('l4'));\n"
            + "      debug(document.getElementById('e4'));\n"
            + "      alert(labels ? labels.length : labels);\n"
            + "    }\n"
            + "    function debug(e) {\n"
            + "      alert(e.labels ? e.labels.length : e.labels);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <keygen id='e1'>e 1</keygen><br>\n"
            + "  <label>something <label> click here <keygen id='e2'>e 2</keygen></label></label><br>\n"
            + "  <label for='e3'> and here</label>\n"
            + "  <keygen id='e3'>e 3</keygen><br>\n"
            + "  <label id='l4' for='e4'> what about</label>\n"
            + "  <label> this<keygen id='e4'>e 4</keygen></label><br>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
