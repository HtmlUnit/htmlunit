/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for Functions.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class FunctionsTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function\\sfoo()\\s{\\s\\s\\s\\sreturn\\s\\t'a'\\s+\\s'b'\\s}")
    public void function_toString() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "function test() {\n"
            + "  function foo() {    return \t'a' + 'b' };\n"
            + "  log(foo.toString());\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function\\sfoo()\\s\\n{\\s\\n\\sreturn\\s\\t'x'\\s\\n\\n}")
    public void function_toStringNewLines() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "function test() {\n"
            + "  function foo() \n{ \r\n return \t'x' \n\n};\n"
            + "  log(foo.toString());\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("()\\s=>\\s{\\s\\nreturn\\s\\s'=>'\\s\\s\\s}")
    public void arrowFunction_toString() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "function test() {\n"
            + "  var foo = () => { \nreturn  '=>'   };"
            + "  log(foo.toString());\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function()\\s{\\n\\s\\s\\s\\s\\s\\sreturn\\s'X';\\n\\s\\s\\s\\s}",
             "function()\\s{\\n\\s\\s\\s\\s\\s\\sreturn\\s'X';\\n\\s\\s\\s\\s}"})
    public void boundFunction_toString() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "function test() {\n"
            + "  var oobj = {\n"
            + "    getX: function() {\n"
            + "      return 'X';\n"
            + "    }\n"
            + "  };\n"

            + "  log(oobj.getX.toString());\n"

            + "  var boundGetX = oobj.getX.bind(oobj);"
            + "  log(oobj.getX.toString());\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
