/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
 * Tests for {@link HTMLBaseFontElement}.
 * @author Ronald Brill
 * @author Frank Danek
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLBaseFontElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLSpanElement]", "undefined", "undefined", "undefined" },
            CHROME = { "[object HTMLElement]", "undefined", "undefined", "undefined" },
            FF = { "[object HTMLElement]", "undefined", "undefined", "undefined" },
            IE = { "[object HTMLBaseFontElement]", "", "3", "" })
    public void defaults() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <basefont id='base' />\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var base = document.getElementById('base');\n"
            + "        alert(base);\n"
            + "        alert(base.face);\n"
            + "        alert(base.size);\n"
            + "        alert(base.color);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>foo</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "42" },
            IE = { "4", "42" })
    public void size() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <basefont id='base' color='red' face='swiss' size='4' />\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var base = document.getElementById('base');\n"
            + "        alert(base.size);\n"
            + "        try {\n"
            + "          base.size=42;\n"
            + "          alert(base.size);\n"
            + "        } catch(e) {\n"
            + "          alert('exception');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>foo</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "helvetica" },
            IE = { "swiss", "helvetica" })
    public void face() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <basefont id='base' color='red' face='swiss' size='5' />\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var base = document.getElementById('base');\n"
            + "        alert(base.face);\n"
            + "        try {\n"
            + "          base.face='helvetica';\n"
            + "          alert(base.face);\n"
            + "        } catch(e) {\n"
            + "          alert('exception');\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>foo</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "undefined", "blue" },
            IE = { "red", "blue" })
    public void color() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <basefont id='base' color='red' face='swiss' size='4' />\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var base = document.getElementById('base');\n"
            + "        alert(base.color);\n"
            + "        try {\n"
            + "          base.color='blue';\n"
            + "          alert(base.color);\n"
            + "        } catch(e) {\n"
            + "          alert('exception');\n"
            + "        }\n"
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
    @Alerts(IE = { "[object HTMLBaseFontElement]", "[object HTMLBaseFontElement]" },
            CHROME = { "[object HTMLElement]", "exception" },
            FF = { "[object HTMLElement]", "exception" })
    public void type() throws Exception {
        final String html = ""
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "  var elem = document.getElementById('b1');\n"
            + "    try {\n"
            + "      alert(elem);\n"
            + "      alert(HTMLBaseFontElement);\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "    <basefont id='b1' color='red' face='swiss' size='4' />\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
