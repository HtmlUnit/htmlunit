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
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link HTMLPreElement}.
 *
 * @version $Revision: 7931 $
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLPreElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "0", "number", "100", "77", "number", "123" },
            IE = { "", "string", "100", "77", "string", "123" })
    @NotYetImplemented
    public void testWidth() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var testPre = document.getElementById('testPre');\n"
            + "        alert(testPre.width);\n"
            + "        alert(typeof testPre.width);\n"
            + "        testPre.width = 100;\n"
            + "        alert(testPre.width);\n"

            + "        var testPre = document.getElementById('testPreWidth');\n"
            + "        alert(testPreWidth.width);\n"
            + "        alert(typeof testPreWidth.width);\n"
            + "        testPreWidth.width = 123;\n"
            + "        alert(testPreWidth.width);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <pre id='testPre'>pre content</pre>\n"
            + "    <pre id='testPreWidth' width='77'>pre content</pre>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }
}
