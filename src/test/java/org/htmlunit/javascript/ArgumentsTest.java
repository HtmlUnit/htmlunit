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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for org.mozilla.javascript.Arguments, which is the object for "function.arguments".
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class ArgumentsTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "1", "0"})
    public void arguments() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  function test() {\n"
            + "    log(test.arguments.length);\n"
            + "    test1('hi');\n"
            + "  }\n"

            + "  function test1(a) {\n"
            + "    log(test.arguments.length);\n"
            + "    log(test1.arguments.length);\n"
            + "    log(arguments.callee.caller.arguments.length);\n"
            + "  }\n"

            + "  test();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void argumentsPrototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  function test() {\n"
            + "    log(arguments.prototype === {}.prototype);\n"
            + "  }\n"

            + "  test();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined/undefined", "undefined/undefined", "W-true", "C-true", "E-false"})
    public void argumentsLengthProperty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "function test() {\n"
                + "  let desc = Object.getOwnPropertyDescriptor(arguments, 'length');\n"
                + "  log(typeof desc.get + '/' + desc.get);\n"
                + "  log(typeof desc.set + '/' + desc.set);\n"
                + "  log('W-' + desc.writable);\n"
                + "  log('C-' + desc.configurable);\n"
                + "  log('E-' + desc.enumerable);\n"
                + "}\n"

                + "test();\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined/undefined", "undefined/undefined", "W-true", "C-true", "E-false"})
    public void argumentsLengthPropertyStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "<script>\n"
                + "'use strict';\n"
                + LOG_TITLE_FUNCTION

                + "function test() {\n"
                + "  let desc = Object.getOwnPropertyDescriptor(arguments, 'length');\n"
                + "  log(typeof desc.get + '/' + desc.get);\n"
                + "  log(typeof desc.set + '/' + desc.set);\n"
                + "  log('W-' + desc.writable);\n"
                + "  log('C-' + desc.configurable);\n"
                + "  log('E-' + desc.enumerable);\n"
                + "}\n"

                + "test();\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined/undefined", "undefined/undefined", "W-true", "C-true", "E-false"})
    public void argumentsCalleeProperty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "function test() {\n"
                + "  let desc = Object.getOwnPropertyDescriptor(arguments, 'callee');\n"
                + "  log(typeof desc.get + '/' + desc.get);\n"
                + "  log(typeof desc.set + '/' + desc.set);\n"
                + "  log('W-' + desc.writable);\n"
                + "  log('C-' + desc.configurable);\n"
                + "  log('E-' + desc.enumerable);\n"
                + "}\n"

                + "test();\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function/function () { [native code] }",
                       "function/function () { [native code] }",
                       "W-undefined", "C-false", "E-false"},
            FF = {"function/function() { [native code] }",
                  "function/function() { [native code] }",
                  "W-undefined", "C-false", "E-false"},
            FF_ESR = {"function/function() { [native code] }",
                      "function/function() { [native code] }",
                      "W-undefined", "C-false", "E-false"})
    @HtmlUnitNYI(FF = {"function/function () { [native code] }",
                       "function/function () { [native code] }",
                       "W-undefined", "C-false", "E-false"},
            FF_ESR = {"function/function () { [native code] }",
                      "function/function () { [native code] }",
                      "W-undefined", "C-false", "E-false"})
    public void argumentsCalleePropertyStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "<script>\n"
                + "'use strict';\n"
                + LOG_TITLE_FUNCTION

                + "function test() {\n"
                + "  let desc = Object.getOwnPropertyDescriptor(arguments, 'callee');\n"
                + "  log(typeof desc.get + '/' + desc.get);\n"
                + "  log(typeof desc.set + '/' + desc.set);\n"
                + "  log('W-' + desc.writable);\n"
                + "  log('C-' + desc.configurable);\n"
                + "  log('E-' + desc.enumerable);\n"
                + "}\n"

                + "test();\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "[object Arguments]", "[object Arguments]", "null"})
    public void argumentsShouldBeNullOutsideFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(arguments);\n"
            + "    log(test.arguments);\n"
            + "  }\n"
            + "  log(test.arguments);\n"
            + "  test();\n"
            + "  log(test.arguments);\n"
            + "</script>"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "[object Arguments]", "TypeError", "TypeError"})
    @HtmlUnitNYI(CHROME = {"null", "[object Arguments]", "[object Arguments]", "null"},
            EDGE = {"null", "[object Arguments]", "[object Arguments]", "null"},
            FF = {"null", "[object Arguments]", "[object Arguments]", "null"},
            FF_ESR = {"null", "[object Arguments]", "[object Arguments]", "null"})
    public void argumentsShouldBeNullOutsideFunctionStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION

            + "  function test() {\n"
            + "    log(arguments);\n"
            + "    try {\n"
            + "      log(test.arguments);\n"
            + "    } catch(e) {\n"
            + "      logEx(e);\n"
            + "    }\n"
            + "  }\n"

            + "  try {\n"
            + "    log(test.arguments);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "  test();\n"
            + "  try {\n"
            + "    log(test.arguments);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("callee,length")
    public void argumentsPropertyNames() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let p = Object.getOwnPropertyNames(arguments);\n"
            + "  p.sort();\n"
            + "  log(p);\n"
            + "}\n"
            + "test();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("callee,length")
    public void argumentsPropertyNamesStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let p = Object.getOwnPropertyNames(arguments);\n"
            + "  p.sort();\n"
            + "  log(p);\n"
            + "}\n"
            + "test();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "2"})
    public void passedCountDifferentFromDeclared() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "  log(test.arguments.length);\n"
            + "}\n"
            + "test('hi', 'there');\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "TypeError"})
    @HtmlUnitNYI(CHROME = {"2", "[object Arguments]"},
            EDGE = {"2", "[object Arguments]"},
            FF = {"2", "[object Arguments]"},
            FF_ESR = {"2", "[object Arguments]"})
    public void passedCountDifferentFromDeclaredStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "  try {\n"
            + "    log(test.arguments);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "}\n"
            + "test('hi', 'there');\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "hello", "world", "undefined", "undefined"})
    public void readOnlyWhenAccessedThroughFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>"
            + "<body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  test.arguments[1] = 'hi';\n"
            + "  test.arguments[3] = 'you';\n"
            + "  log(test.arguments.length);\n"
            + "  log(test.arguments[0]);\n"
            + "  log(test.arguments[1]);\n"
            + "  log(test.arguments[2]);\n"
            + "  log(test.arguments[3]);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "hello", "hi", "undefined", "you"})
    public void writableWithinFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "hello", "hi", "undefined", "you"})
    public void writableWithinFunctionStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "hello", "hi", "undefined", "you",
             "hello", "hi", "undefined"})
    public void writableWithinFunctionAdjustsArgument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test(a, b, c) {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"

            + "  log(a);\n"
            + "  log(b);\n"
            + "  log(c);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "hello", "hi", "undefined", "you",
             "hello", "world", "undefined"})
    public void writableWithinFunctionAdjustsArgumentStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test(a, b, c) {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"

            + "  log(a);\n"
            + "  log(b);\n"
            + "  log(c);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "hello", "hi", "world", "you", "hello", "whole,world"})
    public void writableWithinFunctionRestAdjustsArgument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test(a, ...b) {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"

            + "  log(a);\n"
            + "  log(b);\n"
            + "}\n"
            + "test('hello', 'whole', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "hello", "hi", "world", "you", "hello", "whole,world"})
    public void writableWithinFunctionRestAdjustsArgumentStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test(a, ...b) {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"

            + "  log(a);\n"
            + "  log(b);\n"
            + "}\n"
            + "test('hello', 'whole', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "hello", "hi", "undefined", "you", "hello", "default",
             "2", "hello", "hi", "undefined", "you", "hello", "world"})
    public void writableWithinFunctionDefaultAdjustsArgument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test(a, b='default') {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"

            + "  log(a);\n"
            + "  log(b);\n"
            + "}\n"
            + "test('hello');\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "hello", "hi", "undefined", "you", "hello", "default",
             "2", "hello", "hi", "undefined", "you", "hello", "world"})
    public void writableWithinFunctionDefaultAdjustsArgumentStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test(a, b='default') {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"

            + "  log(a);\n"
            + "  log(b);\n"
            + "}\n"
            + "test('hello');\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "[object Object]", "hi", "undefined", "you", "hello", "world"})
    public void writableWithinFunctionDestructAdjustsArgument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test({a, b}) {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"

            + "  log(a);\n"
            + "  log(b);\n"
            + "}\n"
            + "test({ a: 'hello', b: 'world'});\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "[object Object]", "hi", "undefined", "you", "hello", "world"})
    public void writableWithinFunctionDestructAdjustsArgumentStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test({a, b}) {\n"
            + "  arguments[1] = 'hi';\n"
            + "  arguments[3] = 'you';\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "  log(arguments[3]);\n"

            + "  log(a);\n"
            + "  log(b);\n"
            + "}\n"
            + "test({ a: 'hello', b: 'world'});\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void argumentsEqualsFnArguments() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments == test.arguments);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hi")
    public void argumentsAsParameter() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test(arguments) {\n"
            + "  log(arguments);\n"
            + "}\n"
            + "test('hi');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts({})
    public void argumentsAsParameterStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test(arguments) {\n"
            + "  log(arguments);\n"
            + "}\n"
            + "test('hi');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "true", "undefined/undefined", "undefined/undefined", "true"})
    public void argumentsCallee() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "function calleeFoo() { foo(); }\n"

                + "function foo() {\n"
                + "  log(typeof arguments.callee);\n"
                + "  log(foo === arguments.callee);\n"

                + "  let desc = Object.getOwnPropertyDescriptor(arguments, 'callee');\n"
                + "  log(typeof desc.get + '/' + desc.get);\n"
                + "  log(typeof desc.set + '/' + desc.set);\n"
                + "  log(desc.get === desc.set);\n"
                + "}\n"

                + "calleeFoo();\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "function/", "function/", "true"})
    @HtmlUnitNYI(CHROME = {"TypeError", "function/", "function/", "false"},
            EDGE = {"TypeError", "function/", "function/", "false"},
            FF = {"TypeError", "function/", "function/", "false"},
            FF_ESR = {"TypeError", "function/", "function/", "false"})
    public void argumentsCalleeStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION

                + "function calleeFoo() { foo(); }\n"

                + "function foo() {\n"
                + "  try {\n"
                + "    log(arguments.callee);\n"
                + "  } catch(e) {\n"
                + "    logEx(e);\n"
                + "  }\n"
                + "  let desc = Object.getOwnPropertyDescriptor(arguments, 'callee');\n"
                + "  log(typeof desc.get + '/' + desc.get.name);\n"
                + "  log(typeof desc.set + '/' + desc.set.name);\n"
                + "  log(desc.get === desc.set);\n"
                + "}\n"
                + "calleeFoo();\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void argumentsCalleeDifferentFunctions() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function foo1() {\n"
                + "  return Object.getOwnPropertyDescriptor(arguments, 'callee');\n"
                + "}\n"
                + "function foo2() {\n"
                + "  return Object.getOwnPropertyDescriptor(arguments, 'callee');\n"
                + "}\n"
                + "let desc1 = foo1();\n"
                + "let desc2 = foo2();\n"
                + "log(desc1.get === desc2.get);\n"
                + "log(desc1.set === desc2.set);\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    @HtmlUnitNYI(CHROME = {"false", "false"},
            EDGE = {"false", "false"},
            FF = {"false", "false"},
            FF_ESR = {"false", "false"})
    public void argumentsCalleeDifferentFunctionsStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "function foo1() {\n"
                + "  return Object.getOwnPropertyDescriptor(arguments, 'callee');\n"
                + "}\n"
                + "function foo2() {\n"
                + "  return Object.getOwnPropertyDescriptor(arguments, 'callee');\n"
                + "}\n"
                + "let desc1 = foo1();\n"
                + "let desc2 = foo2();\n"
                + "log(desc1.get === desc2.get);\n"
                + "log(desc1.set === desc2.set);\n"
                + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void argumentsCaller() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.caller);\n"
            + "}\n"
            + "test();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void argumentsCallerStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.caller);\n"
            + "}\n"
            + "test();\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments with zero parameters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void length() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "}\n"
            + "test();\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments with zero parameters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void lengthStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "}\n"
            + "test();\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments with undefined parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "undefined"})
    public void argumentsWithUndefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "}\n"
            + "test(undefined);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments with null parameter.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "null"})
    public void argumentsWithNull() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "}\n"
            + "test(null);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test deleting arguments element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "true", "2", "undefined", "world"})
    public void deleteArgumentsElement() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "  log(delete arguments[0]);\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test deleting arguments element in strict mode.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "true", "2", "undefined", "world"})
    public void deleteArgumentsElementStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "  log(delete arguments[0]);\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments.length is writable.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "5", "hello", "world", "undefined"})
    public void argumentsLengthWritable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "  arguments.length = 5;\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments.length is writable in strict mode.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "5", "hello", "world", "undefined"})
    public void argumentsLengthWritableStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments.length);\n"
            + "  arguments.length = 5;\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments in arrow function (should not exist).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ReferenceError")
    public void argumentsInArrowFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "const test = () => {\n"
            + "  try {\n"
            + "    log(arguments.length);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "};\n"
            + "test('hello');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments in arrow function (should not exist).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ReferenceError")
    public void argumentsInArrowFunctionStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "const test = () => {\n"
            + "  try {\n"
            + "    log(arguments.length);\n"
            + "  } catch(e) {\n"
            + "    logEx(e);\n"
            + "  }\n"
            + "};\n"
            + "test('hello');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments in nested arrow function inherits from parent.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void argumentsInNestedArrowFunction() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  const inner = () => {\n"
            + "    log(arguments.length);\n"
            + "  };\n"
            + "  inner();\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test Array methods on arguments object.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hello,world", "HELLO,WORLD"})
    public void argumentsArrayMethods() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Array.prototype.join.call(arguments, ','));\n"
            + "  let result = Array.prototype.map.call(arguments, x => x.toUpperCase());\n"
            + "  log(result);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test Array methods on arguments object.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hello,world", "HELLO,WORLD"})
    public void argumentsArrayMethodsStric() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Array.prototype.join.call(arguments, ','));\n"
            + "  let result = Array.prototype.map.call(arguments, x => x.toUpperCase());\n"
            + "  log(result);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments is not an Array instance.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void argumentsNotArrayInstance() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments instanceof Array);\n"
            + "  log(arguments instanceof Object);\n"
            + "}\n"
            + "test();\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments is not an Array instance.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true"})
    public void argumentsNotArrayInstanceStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(arguments instanceof Array);\n"
            + "  log(arguments instanceof Object);\n"
            + "}\n"
            + "test();\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments Symbol.iterator.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello,world")
    public void argumentsIterator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let arr = [];\n"
            + "  for (let arg of arguments) {\n"
            + "    arr.push(arg);\n"
            + "  }\n"
            + "  log(arr);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments Symbol.iterator.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello,world")
    public void argumentsIteratorStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let arr = [];\n"
            + "  for (let arg of arguments) {\n"
            + "    arr.push(arg);\n"
            + "  }\n"
            + "  log(arr);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test spread operator on arguments.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts("hello,world")
    public void argumentsSpreadOperator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let arr = [...arguments];\n"
            + "  log(arr);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test spread operator on arguments.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts("hello,world")
    public void argumentsSpreadOperatorStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  let arr = [...arguments];\n"
            + "  log(arr);\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments with rest parameters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "hello", "world", "!"})
    public void argumentsWithRestParameters() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test(first, ...rest) {\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "}\n"
            + "test('hello', 'world', '!');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments with rest parameters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "hello", "world", "!"})
    public void argumentsWithRestParametersStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test(first, ...rest) {\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "  log(arguments[2]);\n"
            + "}\n"
            + "test('hello', 'world', '!');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments with default parameters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "hello", "undefined"})
    public void argumentsWithDefaultParameters() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test(x, y = 'default') {\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "}\n"
            + "test('hello');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments with default parameters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "hello", "undefined"})
    public void argumentsWithDefaultParametersStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test(x, y = 'default') {\n"
            + "  log(arguments.length);\n"
            + "  log(arguments[0]);\n"
            + "  log(arguments[1]);\n"
            + "}\n"
            + "test('hello');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments in eval.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void argumentsInEval() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  eval('log(arguments.length)');\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments in eval.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void argumentsInEvalStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  eval('log(arguments.length)');\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test Object.keys on arguments.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0,1")
    public void argumentsObjectKeys() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Object.keys(arguments));\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test Object.keys on arguments.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0,1")
    public void argumentsObjectKeysStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Object.keys(arguments));\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test JSON.stringify on arguments.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("{\"0\":\"hello\",\"1\":\"world\"}")
    public void argumentsJSONStringify() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(JSON.stringify(arguments));\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test JSON.stringify on arguments.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("{\"0\":\"hello\",\"1\":\"world\"}")
    public void argumentsJSONStringifyStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(JSON.stringify(arguments));\n"
            + "}\n"
            + "test('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments in constructor function.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void argumentsInConstructor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function MyClass() {\n"
            + "  log(arguments.length);\n"
            + "}\n"
            + "new MyClass('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments in constructor function.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void argumentsInConstructorStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function MyClass() {\n"
            + "  log(arguments.length);\n"
            + "}\n"
            + "new MyClass('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments in class method.
     * @throws Exception if the test fails
     */
    @Test
    @Disabled
    @Alerts("2")
    public void argumentsInClassMethod() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "class MyClass {\n"
            + "  method() {\n"
            + "    log(arguments.length);\n"
            + "  }\n"
            + "}\n"
            + "new MyClass().method('hello', 'world');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments toString.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Arguments]")
    public void argumentsToString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Object.prototype.toString.call(arguments));\n"
            + "}\n"
            + "test();\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test arguments toString.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object Arguments]")
    public void argumentsToStringStrict() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>"
            + "<script>\n"
            + "'use strict';\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Object.prototype.toString.call(arguments));\n"
            + "}\n"
            + "test();\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }
}
