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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link ClientRect}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ClientRectTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"100", "400", "100", "450", "50", "0"})
    public void properties() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "    var d1 = document.getElementById('div1');\n"
            + "    var pos = d1.getBoundingClientRect();\n"
            + "    log(pos.top);\n"
            + "    log(pos.left);\n"
            + "    log(pos.bottom);\n"
            + "    log(pos.right);\n"
            + "    log(pos.width);\n"
            + "    log(pos.height);\n"
            + "    } catch (e) { log('exception');}\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<div id='outer' style='position: absolute; left: 400px; top: 100px; width: 50px; height: 80px;'>\n"
            + "<div id='div1'></div></div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void getBoundingClientRect_WidthPercent() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var input = document.getElementById('myInput');\n"
            + "      log(input.getBoundingClientRect().height > 10);\n"
            + "      log(input.getBoundingClientRect().width > 100);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<style>.full { width:100%; }</style>\n"
            + "<div class='foo bar'>\n"
            + "  <form action='javascript:void(0)' method='post'>\n"
            + "    <div class='full'>\n"
            + "      <input class='full' type='text' id='myInput'>\n"
            + "    </div>\n"
            + "  </form>\n"
            + "</div>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
