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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLBaseElement}.
 *
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLBaseElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"http://www.foo.com/images/", "§§URL§§", "", "_blank"},
            IE = {"http://www.foo.com/images/", "", "", "_blank"})
    public void hrefAndTarget() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <base id='b1' href='http://www.foo.com/images/' />\n"
            + "    <base id='b2' target='_blank' />\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        alert(document.getElementById('b1').href);\n"
            + "        alert(document.getElementById('b2').href);\n"
            + "        alert(document.getElementById('b1').target);\n"
            + "        alert(document.getElementById('b2').target);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>foo</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLBaseElement]", "function HTMLBaseElement() { [native code] }"},
            FF = {"[object HTMLBaseElement]", "function HTMLBaseElement() {\n    [native code]\n}"},
            FF78 = {"[object HTMLBaseElement]", "function HTMLBaseElement() {\n    [native code]\n}"},
            IE = {"[object HTMLBaseElement]", "[object HTMLBaseElement]"})
    public void type() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "  var elem = document.getElementById('b1');\n"
            + "    try {\n"
            + "      alert(elem);\n"
            + "      alert(HTMLBaseElement);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <base id='b1' href='http://somehost/images/' />\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
