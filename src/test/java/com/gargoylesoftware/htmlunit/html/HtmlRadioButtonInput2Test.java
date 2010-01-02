/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlRadioButtonInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlRadioButtonInput2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "true", "false", "false", "false", "true", "true" },
            FF = { "true", "true", "true", "true", "true", "true" })
    public void checked_on_attachment() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var input = document.createElement('input');\n"
            + "      input.type = 'radio';\n"
            + "      input.checked = true;\n"
            + "      alert(input.checked);\n"
            + "      document.body.appendChild(input);\n"
            + "      alert(input.checked);\n"
            + "      document.body.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "\n"
            + "      input.defaultChecked = true;\n"
            + "      alert(input.checked);\n"
            + "      document.body.appendChild(input);\n"
            + "      alert(input.checked);\n"
            + "      document.body.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
