/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlKeygen}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlKeyGenTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = ", inline-block, inline-block",
            FF = "block, inline, inline-block",
            IE11 = "inline, inline, inline",
            IE8 = "null, inline, inline")
    @NotYetImplemented({ CHROME, FF })
    public void defaultStyle() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.createElement('keygen');\n"
            + "    check(e);\n"
            + "    document.body.appendChild(e);\n"
            + "    check(e);\n"
            + "    check(document.getElementById('myId'));\n"
            + "  }\n"

            + "  function check(e) {\n"
            + "    var cs = window.getComputedStyle ? window.getComputedStyle(e, null) : e.currentStyle;\n"
            + "    var disp = cs ? cs.display : null;\n"
            + "    alert(disp);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<keygen id='myId'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
