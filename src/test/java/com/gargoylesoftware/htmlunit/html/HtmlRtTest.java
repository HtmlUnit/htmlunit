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
 * Tests for {@link HtmlRt}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlRtTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline", "block"},
            FF = {"", "ruby-text", "ruby-text"},
            FF78 = {"", "ruby-text", "ruby-text"},
            IE = {"ruby-text", "ruby-text", "ruby-text"})
    public void defaultStyle() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.createElement('rt');\n"
            + "    check(e);\n"
            + "    document.body.appendChild(e);\n"
            + "    check(e);\n"
            + "    check(document.getElementById('myId'));\n"
            + "  }\n"

            + "  function check(e) {\n"
            + "    var cs = window.getComputedStyle(e, null);\n"
            + "    var disp = cs ? cs.display : null;\n"
            + "    log(disp);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<ruby>\n"
            + "  <rt id='myId'></rt>\n"
            + "</ruby>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"", "inline", "block"},
            FF = {"", "ruby-text", "ruby-text"},
            FF78 = {"", "ruby-text", "ruby-text"},
            IE = {"ruby-text", "ruby-text", "ruby-text"})
    public void defaultStyleStandards() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.createElement('rt');\n"
            + "    check(e);\n"
            + "    document.body.appendChild(e);\n"
            + "    check(e);\n"
            + "    check(document.getElementById('myId'));\n"
            + "  }\n"

            + "  function check(e) {\n"
            + "    var cs = window.getComputedStyle(e, null);\n"
            + "    var disp = cs ? cs.display : null;\n"
            + "    log(disp);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<ruby>\n"
            + "  <rt id='myId'></rt>\n"
            + "</ruby>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
