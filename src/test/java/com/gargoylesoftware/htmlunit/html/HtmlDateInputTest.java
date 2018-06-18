/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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
 * Tests for {@link HtmlDateInput}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlDateInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"text-datetime", "text-Date"},
            CHROME = {"text-datetime", "date-Date"},
            FF60 = {"text-datetime", "date-Date"})
    public void type() throws Exception {
        final String html =
              "<html>\n"
              + "<head>\n"
              + "<script>\n"
              + "  function test() {\n"
              + "    var input = document.getElementById('input1');\n"
              + "    alert(input.type + '-' + input.getAttribute('type'));\n"
              + "    input = document.getElementById('input2');\n"
              + "    alert(input.type + '-' + input.getAttribute('type'));\n"
              + "  }\n"
              + "</script>\n"
              + "</head>\n"
              + "<body onload='test()'>\n"
              + "  <input id='input1' type='datetime'>\n"
              + "  <input id='input2' type='Date'>\n"
              + "</body></html>";

        loadPageWithAlerts2(html);
    }

}
