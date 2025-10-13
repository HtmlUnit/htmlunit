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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for general Rhino problems.
 *
 * @author Ronald Brill
 */
public class RhinoTest extends WebDriverTestCase {

    /**
     * Some samples from
     * <a href="https://stackoverflow.com/questions/10480108/is-there-any-way-to-check-if-strict-mode-is-enforced">
     * https://stackoverflow.com/questions/10480108/is-there-any-way-to-check-if-strict-mode-is-enforced</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    @HtmlUnitNYI(CHROME = {"false", "false"},
            EDGE = {"false", "false"},
            FF = {"false", "false"},
            FF_ESR = {"false", "false"})
    public void isStrict_GlobalThis() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  'use strict'\n"
            + LOG_TITLE_FUNCTION

            + "  var isStrict = (function() { return !this; })();\n"
            + "  log(isStrict);\n"

            + "  isStrict = (function() { return !!!this; })();\n"
            + "  log(isStrict);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false",
            EDGE = "false",
            FF = "false",
            FF_ESR = "false")
    public void isStrict_evalVar() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  'use strict'\n"
            + LOG_TITLE_FUNCTION

            + "  function isStrict() {\n"
            + "    var x = true;\n"
            + "    eval('var x = false');\n"
            + "    return x;\n"
            + "  }\n"

            + "  log(isStrict());\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void isStrict_argumentsCallee() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  'use strict'\n"
            + LOG_TITLE_FUNCTION

            + "  function isStrict() {\n"
            + "    try { arguments.callee } catch(e) { return true; };"
            + "    return false;\n"
            + "  }\n"

            + "  log(isStrict());\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void isStrict_constructor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "  'use strict'\n"
            + LOG_TITLE_FUNCTION

            + "  function Foo () {}\n"

            + "  try {\n"
            + "    true.constructor = Foo;\n"
            + "    log('true.constructor');\n"
            + "  } catch(e) { log(e instanceof TypeError); }\n"

            + "  try {\n"
            + "    var o = 1;\n"
            + "    o.constructor = Foo;\n"
            + "    log('1.constructor');\n"
            + "  } catch(e) { log(e instanceof TypeError); }\n"

            + "  try {\n"
            + "    'test'.constructor = Foo;\n"
            + "    log('test.constructor');\n"
            + "  } catch(e) { log(e instanceof TypeError); }\n"

            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("from script")
    public void consStringAsSetterFunctionParam() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "  <head>\n"
                + "    <script>\n"
                + LOG_TITLE_FUNCTION
                + "      function test() {\n"

                + "        var script = document.createElement('script');\n"
                + "        script.id = 'b';\n"
                + "        script.type = 'text/javascript';\n"
                + "        Object.defineProperty(script, 'source', {\n"
                + "          get: function() { return this.src },\n"
                + "          set: function(source) {\n"
                + "            var srcDesc = Object.getOwnPropertyDescriptor(Object.getPrototypeOf(script), 'src');\n"
                + "            srcDesc.set.call(this, source);\n"
                + "          }\n"
                + "        });\n"

                + "        var part1 = 'sc';\n"
                + "        script.source = part1 + 'r' + 'ipt.js';\n"
                + "        document.body.appendChild(script);\n"
                + "      }\n"
                + "    </script>\n"
                + "  </head>\n"
                + "  <body onload='test()'>\n"
                + "  </body></html>";

        final String js = "log('from script');";
        getMockWebConnection().setDefaultResponse(js, MimeType.TEXT_JAVASCRIPT);

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for issues #1040.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void forSwitchLet() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"

                + "  var sum = 0;\n"
                + "  for (let i = 0; i < 1; i++)\n"
                + "    switch (i) {\n"
                + "      case 0:\n"
                + "        let test = 7;\n"
                + "        sum += 4;\n"
                + "        break;\n"
                + "      }"

                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
