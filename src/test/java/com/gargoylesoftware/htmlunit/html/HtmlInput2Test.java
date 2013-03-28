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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlInput}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public final class HtmlInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "null", "error", "handler", "null", "error" },
            FF3_6 = { "undefined", "error", "handler", "null", "error" })
    @NotYetImplemented(Browser.FF3_6)
    public void onchangeDirectCall() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + "  function handler() { alert('handler');}\n"
            + "  function test() {\n"
            + "    var elem = document.getElementById('myInput');\n"
            + "    try {\n"
            + "      alert(elem.onchange);\n"
            + "      elem.onchange();\n"
            + "      alert('onchange called');\n"
            + "    } catch (e) {alert('error')}\n"

            + "    elem.onchange = handler;\n"
            + "    elem.onchange();\n"

            + "    elem.onchange = null;\n"
            + "    try {\n"
            + "      alert(elem.onchange);\n"
            + "      elem.onchange();\n"
            + "      alert('onchange called');\n"
            + "    } catch (e) {alert('error')}\n"

            + "  }\n"
            + "</script>\n"
            + "<body onload=test()>\n"
            + "  <input id='myInput'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
