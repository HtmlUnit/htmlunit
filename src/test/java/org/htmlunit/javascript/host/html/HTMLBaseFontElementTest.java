/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.html;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.w3c.dom.html.HTMLBaseFontElement;

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
    @Alerts({"[object HTMLElement]", "undefined", "undefined", "undefined"})
    public void defaults() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <basefont id='base' />\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var base = document.getElementById('base');\n"
            + "        log(base);\n"
            + "        log(base.face);\n"
            + "        log(base.size);\n"
            + "        log(base.color);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>foo</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "42"})
    public void size() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <basefont id='base' color='red' face='swiss' size='4' />\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var base = document.getElementById('base');\n"
            + "        log(base.size);\n"
            + "        try {\n"
            + "          base.size = 42;\n"
            + "          log(base.size);\n"
            + "        } catch(e) {\n"
            + "          logEx(e);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>foo</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "helvetica"})
    public void face() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <basefont id='base' color='red' face='swiss' size='5' />\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var base = document.getElementById('base');\n"
            + "        log(base.face);\n"
            + "        try {\n"
            + "          base.face = 'helvetica';\n"
            + "          log(base.face);\n"
            + "        } catch(e) {\n"
            + "          logEx(e);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>foo</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "blue"})
    public void color() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <basefont id='base' color='red' face='swiss' size='4' />\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var base = document.getElementById('base');\n"
            + "        log(base.color);\n"
            + "        try {\n"
            + "          base.color = 'blue';\n"
            + "          log(base.color);\n"
            + "        } catch(e) {\n"
            + "          logEx(e);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>foo</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object HTMLElement]", "ReferenceError"})
    public void type() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "  var elem = document.getElementById('b1');\n"
            + "    try {\n"
            + "      log(elem);\n"
            + "      log(HTMLBaseFontElement);\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <basefont id='b1' color='red' face='swiss' size='4' />\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
