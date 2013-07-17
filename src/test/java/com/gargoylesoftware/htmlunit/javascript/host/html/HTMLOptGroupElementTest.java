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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLOptGroupElement}.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLOptGroupElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "true", "true", "false", "true" })
    public void disabledAttribute() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var test1 = document.getElementById('test1');\n"
            + "        alert(test1.disabled);\n"
            + "        test1.disabled = true;\n"
            + "        alert(test1.disabled);\n"
            + "        test1.disabled = true;\n"
            + "        alert(test1.disabled);\n"
            + "        test1.disabled = false;\n"
            + "        alert(test1.disabled);\n"

            + "        var test2 = document.getElementById('test2');\n"
            + "        alert(test2.disabled);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <form name='form1'>\n"
            + "      <select>\n"
            + "        <optgroup id='test1'>\n"
            + "          <option value='group1'>Group1</option>\n"
            + "        </optgroup>\n"
            + "        <optgroup id='test2' disabled>\n"
            + "          <option value='group2'>Group2</option>\n"
            + "        </optgroup>\n"
            + "      </select>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "newLabel", "", "label" })
    public void labelAttribute() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var test1 = document.getElementById('test1');\n"
            + "        alert(test1.label);\n"
            + "        test1.label = 'newLabel';\n"
            + "        alert(test1.label);\n"
            + "        test1.label = '';\n"
            + "        alert(test1.label);\n"

            + "        var test2 = document.getElementById('test2');\n"
            + "        alert(test2.label);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <form name='form1'>\n"
            + "      <select>\n"
            + "        <optgroup id='test1'>\n"
            + "          <option value='group1'>Group1</option>\n"
            + "        </optgroup>\n"
            + "        <optgroup id='test2' label='label'>\n"
            + "          <option value='group2'>Group2</option>\n"
            + "        </optgroup>\n"
            + "      </select>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
