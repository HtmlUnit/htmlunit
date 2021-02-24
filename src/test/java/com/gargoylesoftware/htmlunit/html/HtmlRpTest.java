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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlRp}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlRpTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "none", "none"},
            IE = {"inline", "inline", "inline"})
    public void defaultStyle() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.createElement('rp');\n"
            + "    check(e);\n"
            + "    document.body.appendChild(e);\n"
            + "    check(e);\n"
            + "    check(document.getElementById('myId'));\n"
            + "  }\n"

            + "  function check(e) {\n"
            + "    var cs = window.getComputedStyle(e, null);\n"
            + "    var disp = cs ? cs.display : null;\n"
            + "    alert(disp);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<ruby>\n"
            + "  <rp id='myId'></rt>\n"
            + "</ruby>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
