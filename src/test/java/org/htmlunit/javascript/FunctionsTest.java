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
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriverException;

/**
 * Tests for Functions.
 *
 * @author Ronald Brill
 */
public class FunctionsTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function\\sfoo()\\s{\\s\\s\\s\\sreturn\\s\\t'a'\\s+\\s'b'\\s}")
    public void function_toString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"foo = undefined", "1"})
    @HtmlUnitNYI(CHROME = "org.htmlunit.ScriptException: ReferenceError: \"foo\" is not defined.",
            EDGE = "org.htmlunit.ScriptException: ReferenceError: \"foo\" is not defined.",
            FF = "org.htmlunit.ScriptException: ReferenceError: \"foo\" is not defined.",
            FF_ESR = "org.htmlunit.ScriptException: ReferenceError: \"foo\" is not defined.")
    public void conditionallyCreatedFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('foo = ' + foo);\n"
            + "  if (true) {\n"
            + "    log(foo());\n"
            + "    function foo() { return 1; }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        try {
            loadPageVerifyTitle2(html);
        }
        catch (final WebDriverException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith(getExpectedAlerts()[0]));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ReferenceError", "1"})
    public void conditionallyCreatedFunctionStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log('foo = ' + foo);\n"
            + "  } catch(e) { logEx(e); }\n"
            + "  if (true) {\n"
            + "    log(foo());\n"
            + "    function foo() { return 1; }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello!")
    public void applyThisFromBoundArgs() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  var f = function(x) { return this.toString(); };\n"
                + "  var a = f.apply;\n"
                + "  var b = a.bind(f, 'Hello!');\n"
                + "  log(b([1,2]));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello!")
    public void applyToApplyCallsCorrectFunction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  function foo(x) {return x;};\n"
                + "  var r = Function.prototype.apply.apply(foo, ['b', ['Hello!', 'Goodbye!']]);\n"
                + "  log(r);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b")
    public void applyToApplySetsCorrectFunctionThis() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  function foo(x) {return this.toString();};\n"
                + "  var r = Function.prototype.apply.apply(foo, ['b', ['Hello!', 'Goodbye!']]);\n"
                + "  log(r);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Hello!")
    public void applyToCallCallsCorrectFunction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  function foo(x) {return x;};\n"
                + "  var r = foo.call.apply(foo, ['b', 'Hello!']);\n"
                + "  log(r);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b")
    public void applyToCallSetsCorrectFunctionThis() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  function foo(x) {return this.toString();};\n"
                + "  var r = foo.call.apply(foo, ['b', 'Hello!']);\n"
                + "  log(r);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcd / undefined")
    public void boundWithoutArgs() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function foo(x) { return this.toString() + ' / ' + x; };\n"
                + "  var boundThis = 'abcd';\n"
                + "  var boundFoo = Function.prototype.bind.call(foo, boundThis);\n"
                + "  var r = boundFoo.call('Hello!');\n"
                + "  log(r);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcd / world")
    public void boundWithArg() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function foo(x) { return this.toString() + ' / ' + x; };\n"
                + "  var boundThis = 'abcd';\n"
                + "  var boundFoo = Function.prototype.bind.call(foo, boundThis, 'world');\n"
                + "  var r = boundFoo.call('Hello!');\n"
                + "  log(r);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("world")
    public void bound() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function foo() { return 'world'; };\n"
                + "  var boundThis = 'abcd';\n"
                + "  var boundFoo = Function.prototype.bind.call(foo, boundThis);\n"
                + "  var r = boundFoo.call(' dd');\n"
                + "  log(r);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"f1", "f2", "f3", "f4", "f5",
             "f6", "g", "3", "[foo]", ""})
    public void name() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "let f1 = function() {}; log(f1.name);\n"
                + "let f2= () => {}; log(f2.name);\n"
                + "let f3 = function() {}; log(f3.name);\n"
                + "let f4 = () => {}; log(f4.name);\n"
                + "let f5 = function() {}; log(f5.name);\n"

                + "let f6 = () => {}; log(f6.name);\n"
                + "let f7 = function g() {}; log(f7.name);\n"
                + "let f8 = { [1 + 2]() {} }; log(f8['3'].name);\n"
                + "let s = Symbol('foo'); let f9 = { [s]() {} }; log(f9[s].name);\n"
                + "let s2 = Symbol(); f10 = { [s2]() {} }; log(f10[s2].name);\n"

                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
