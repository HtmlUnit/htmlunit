/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

/**
 * Tests for the object for "function.arguments".
 *
 * @author Ronald Brill
 */
public class CallerTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "true"})
    public void basicCaller() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "  function test() {\n"
                + "    log(test1.caller);\n"
                + "    test1('hi');\n"
                + "  }\n"

                + "  function test1(a) {\n"
                + "    log(test1.caller === test);\n"
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
    @Alerts("null")
    public void callerIsNullAtTopLevel() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function topLevelFunc() {\n"
                + "    log(topLevelFunc.caller);\n"
                + "  }\n"
                + "  topLevelFunc();\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void callerIsNullFromStrictMode() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function callee() {\n"
                + "    log(callee.caller);\n"
                + "  }\n"
                + "  function strictCaller() {\n"
                + "    'use strict';\n"
                + "    callee();\n"
                + "  }\n"
                + "  strictCaller();\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void callerInStrictModeThrows() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION

                + "  function strictFunc() {\n"
                + "    log(strictFunc.caller);\n"
                + "  }\n"

                + "  try {\n"
                + "    log(strictFunc());\n"
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
    @Alerts("TypeError")
    public void setCallerInStrictModeThrows() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  function strictFunc() {\n"
                + "    strictFunc.caller = function() {};\n"
                + "  }\n"
                + "  try {\n"
                + "    strictFunc();\n"
                + "    log('no-exception');\n"
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
    @Alerts("true")
    public void callerExistsOnPrototype() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log('caller' in Function.prototype);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void accessingFunctionPrototypeCallerThrows() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    log(Function.prototype.caller);\n"
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
    @Alerts("TypeError")
    public void settingFunctionPrototypeCallerThrows() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  try {\n"
                + "    Function.prototype.caller = function() {};\n"
                + "    log(Function.prototype.caller);\n"
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
    @Alerts(
            DEFAULT = {"function/function get caller() { [native code] }",
                       "function/function set caller() { [native code] }",
                       "W-undefined", "C-true", "E-false"},
            FF = {"function/function caller() { [native code] }",
                  "function/function caller() { [native code] }",
                  "W-undefined", "C-true", "E-false"},
            FF_ESR = {"function/function caller() { [native code] }",
                      "function/function caller() { [native code] }",
                      "W-undefined", "C-true", "E-false"})
    @HtmlUnitNYI(FF = {"function/function get caller() { [native code] }",
                       "function/function set caller() { [native code] }",
                       "W-undefined", "C-true", "E-false"},
            FF_ESR = {"function/function get caller() { [native code] }",
                      "function/function set caller() { [native code] }",
                      "W-undefined", "C-true", "E-false"})
    public void callerPropertyDescriptor() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function testFunc() {}\n"
                + "  let desc = Object.getOwnPropertyDescriptor(Function.prototype, 'caller');\n"
                + "  if (desc) {\n"
                + "    log(typeof desc.get + '/' + desc.get);\n"
                + "    log(typeof desc.set + '/' + desc.set);\n"
                + "    log('W-' + desc.writable);\n"
                + "    log('C-' + desc.configurable);\n"
                + "    log('E-' + desc.enumerable);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerNotEnumerable() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function testFunc() {}\n"
                + "  log(Object.keys(testFunc).indexOf('caller') === -1);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void callerNotOwnProperty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function testFunc() {}\n"
                + "  log(Object.prototype.hasOwnProperty.call(testFunc, 'caller'));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void nestedCalls() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller1, capturedCaller2;\n"
                + "  function level3() {\n"
                + "    capturedCaller1 = level3.caller;\n"
                + "  }\n"
                + "  function level2() {\n"
                + "    capturedCaller2 = level2.caller;\n"
                + "    level3();\n"
                + "  }\n"
                + "  function level1() {\n"
                + "    level2();\n"
                + "  }\n"
                + "  level1();\n"
                + "  log(capturedCaller2 === level1);\n"
                + "  log(capturedCaller1 === level2);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void recursiveCaller() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let callerAtDepth2;\n"
                + "  function recursive(n) {\n"
                + "    if (n === 2) {\n"
                + "      callerAtDepth2 = recursive.caller;\n"
                + "    }\n"
                + "    if (n > 0) {\n"
                + "      recursive(n - 1);\n"
                + "    }\n"
                + "  }\n"
                + "  recursive(3);\n"
                + "  log(callerAtDepth2 === recursive);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void mutualRecursion() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCallerInG;\n"
                + "  function f(n) {\n"
                + "    if (n > 0) {\n"
                + "      g(n - 1);\n"
                + "    }\n"
                + "  }\n"
                + "  function g(n) {\n"
                + "    capturedCallerInG = g.caller;\n"
                + "    if (n > 0) {\n"
                + "      f(n);\n"
                + "    }\n"
                + "  }\n"
                + "  f(2);\n"
                + "  log(capturedCallerInG === f);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void callerUpdatesOnMultipleCalls() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let firstCaller, secondCaller;\n"
                + "  function callee() {\n"
                + "    return callee.caller;\n"
                + "  }\n"
                + "  function caller1() {\n"
                + "    firstCaller = callee();\n"
                + "  }\n"
                + "  function caller2() {\n"
                + "    secondCaller = callee();\n"
                + "  }\n"
                + "  caller1();\n"
                + "  caller2();\n"
                + "  log(firstCaller === caller1);\n"
                + "  log(secondCaller === caller2);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void callerIsNullAfterExecution() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let callerDuringExecution, callerAfterExecution;\n"
                + "  let calleeRef;\n"
                + "  function callee() {\n"
                + "    calleeRef = callee;\n"
                + "    callerDuringExecution = callee.caller;\n"
                + "  }\n"
                + "  function caller() {\n"
                + "    callee();\n"
                + "  }\n"
                + "  caller();\n"
                + "  callerAfterExecution = calleeRef.caller;\n"
                + "  log(callerDuringExecution === caller);\n"
                + "  log(callerAfterExecution === null);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerWithApply() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  function callee() {\n"
                + "    capturedCaller = callee.caller;\n"
                + "  }\n"
                + "  function caller() {\n"
                + "    callee.apply(null);\n"
                + "  }\n"
                + "  caller();\n"
                + "  log(capturedCaller === caller);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerWithCall() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  function callee() {\n"
                + "    capturedCaller = callee.caller;\n"
                + "  }\n"
                + "  function caller() {\n"
                + "    callee.call(null);\n"
                + "  }\n"
                + "  caller();\n"
                + "  log(capturedCaller === caller);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerWithBind() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  function callee() {\n"
                + "    capturedCaller = callee.caller;\n"
                + "  }\n"
                + "  function caller() {\n"
                + "    let boundCallee = callee.bind(null);\n"
                + "    boundCallee();\n"
                + "  }\n"
                + "  caller();\n"
                + "  log(capturedCaller !== undefined && capturedCaller !== null);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerInConstructor() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  function Constructor() {\n"
                + "    capturedCaller = Constructor.caller;\n"
                + "  }\n"
                + "  function createInstance() {\n"
                + "    new Constructor();\n"
                + "  }\n"
                + "  createInstance();\n"
                + "  log(capturedCaller === createInstance);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerInMethod() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  let obj = {\n"
                + "    method: function() {\n"
                + "      capturedCaller = obj.method.caller;\n"
                + "    }\n"
                + "  };\n"
                + "  function caller() {\n"
                + "    obj.method();\n"
                + "  }\n"
                + "  caller();\n"
                + "  log(capturedCaller === caller);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerWithFunctionConstructor() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  let dynamicFunc = new Function('capturedCaller = arguments.callee.caller');\n"
                + "  function caller() {\n"
                + "    dynamicFunc();\n"
                + "  }\n"
                + "  caller();\n"
                + "  log(capturedCaller === caller);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerWithEval() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  function outerFunc() {\n"
                + "    eval('capturedCaller = (function inner() { return inner.caller; })()');\n"
                + "  }\n"
                + "  outerFunc();\n"
                + "  log(capturedCaller !== undefined);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerInIIFE() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller = (function() {\n"
                + "    return arguments.callee.caller;\n"
                + "  })();\n"
                + "  log(capturedCaller === null);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void callerWithClosures() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  function outer() {\n"
                + "    function inner() {\n"
                + "      capturedCaller = inner.caller;\n"
                + "    }\n"
                + "    return inner;\n"
                + "  }\n"
                + "  let innerFunc = outer();\n"
                + "  function caller() {\n"
                + "    innerFunc();\n"
                + "  }\n"
                + "  caller();\n"
                + "  log(capturedCaller === caller);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void nonStrictCalleeFromStrictCaller() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  function nonStrictCallee() {\n"
                + "    capturedCaller = nonStrictCallee.caller;\n"
                + "  }\n"
                + "  function strictCaller() {\n"
                + "    'use strict';\n"
                + "    nonStrictCallee();\n"
                + "  }\n"
                + "  strictCaller();\n"
                + "  log(capturedCaller === null);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("error")
    public void strictCalleeFromNonStrictCaller() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function strictCallee() {\n"
                + "    'use strict';\n"
                + "    return strictCallee.caller;\n"
                + "  }\n"
                + "  function nonStrictCaller() {\n"
                + "    try {\n"
                + "      return strictCallee();\n"
                + "    } catch(e) {\n"
                + "      return 'error';\n"
                + "    }\n"
                + "  }\n"
                + "  log(nonStrictCaller());\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void callerPropertyDescriptorSameForDifferentFunctions() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function foo1() {}\n"
                + "  function foo2() {}\n"
                + "  let desc1 = Object.getOwnPropertyDescriptor(Function.prototype, 'caller');\n"
                + "  let desc2 = Object.getOwnPropertyDescriptor(Function.prototype, 'caller');\n"
                + "  if (desc1 && desc2) {\n"
                + "    log(desc1.get === desc2.get);\n"
                + "    log(desc1.set === desc2.set);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("length,name,prototype")
    @HtmlUnitNYI(CHROME = "arity,length,name,prototype",
            EDGE = "arity,length,name,prototype",
            FF = "arity,length,name,prototype",
            FF_ESR = "arity,length,name,prototype")
    public void callerPropertyNames() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {}\n"
                + "  let p = Object.getOwnPropertyNames(test);\n"
                + "  p.sort();\n"
                + "  log(p);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("length,name,prototype")
    @HtmlUnitNYI(CHROME = "arity,length,name,prototype",
            EDGE = "arity,length,name,prototype",
            FF = "arity,length,name,prototype",
            FF_ESR = "arity,length,name,prototype")
    public void callerPropertyNamesStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {}\n"
                + "  let p = Object.getOwnPropertyNames(test);\n"
                + "  p.sort();\n"
                + "  log(p);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "null", "null"})
    public void callerShouldBeNullOutsideFunction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(test.caller);\n"
                + "  }\n"
                + "  log(test.caller);\n"
                + "  test();\n"
                + "  log(test.caller);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError", "TypeError"})
    public void callerShouldThrowOutsideFunctionStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    try {\n"
                + "      log(test.caller);\n"
                + "    } catch(e) { logEx(e); }\n"
                + "  }\n"
                + "  try {\n"
                + "    log(test.caller);\n"
                + "  } catch(e) { logEx(e); }\n"
                + "  test();\n"
                + "  try {\n"
                + "    log(test.caller);\n"
                + "  } catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void arrowFunctionCallerThrows() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let arrowFunc = () => {\n"
                + "    return arrowFunc.caller;\n"
                + "  };\n"
                + "  try {\n"
                + "    log(arrowFunc());\n"
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
    @Alerts("error")
    public void arrowFunctionAsCallee() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  let callee = () => {\n"
                + "    capturedCaller = callee.caller;\n"
                + "  };\n"
                + "  function caller() {\n"
                + "    try {\n"
                + "      callee();\n"
                + "      return 'no-error';\n"
                + "    } catch(e) {\n"
                + "      return 'error';\n"
                + "    }\n"
                + "  }\n"
                + "  log(caller());\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "done"})
    @HtmlUnitNYI(CHROME = "done",
            EDGE = "done",
            FF = "done",
            FF_ESR = "done")
    public void generatorFunctionCallerThrows() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function* generatorFunc() {\n"
                + "    return generatorFunc.caller;\n"
                + "  }\n"
                + "  let gen = generatorFunc();\n"
                + "  try {\n"
                + "    gen.next();\n"
                + "  } catch(e) {\n"
                + "    logEx(e);\n"
                + "  }\n"
                + "  log('done');\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void deepCallChain() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller;\n"
                + "  function level10() { capturedCaller = level10.caller; }\n"
                + "  function level9() { level10(); }\n"
                + "  function level8() { level9(); }\n"
                + "  function level7() { level8(); }\n"
                + "  function level6() { level7(); }\n"
                + "  function level5() { level6(); }\n"
                + "  function level4() { level5(); }\n"
                + "  function level3() { level4(); }\n"
                + "  function level2() { level3(); }\n"
                + "  function level1() { level2(); }\n"
                + "  level1();\n"
                + "  log(capturedCaller === level9);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void callerAndArgumentsIndependence() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let capturedCaller, capturedArguments;\n"
                + "  function callee(a, b, c) {\n"
                + "    capturedCaller = callee.caller;\n"
                + "    capturedArguments = callee.arguments;\n"
                + "  }\n"
                + "  function caller() {\n"
                + "    callee(1, 2, 3);\n"
                + "  }\n"
                + "  caller();\n"
                + "  log(capturedCaller === caller);\n"
                + "  log(capturedArguments !== null && capturedArguments !== undefined);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void callerDescriptorGetterAndSetterSame() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let desc = Object.getOwnPropertyDescriptor(Function.prototype, 'caller');\n"
                + "  if (desc) {\n"
                + "    log(desc.get === desc.set);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void deleteCaller() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function testFunc() {}\n"
                + "  try {\n"
                + "    log(delete testFunc.caller);\n"
                + "    log('caller' in testFunc);\n"
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
    @Alerts("no-error")
    public void deleteCallerStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  function testFunc() {}\n"
                + "  try {\n"
                + "    delete testFunc.caller;\n"
                + "    log('no-error');\n"
                + "  } catch(e) {\n"
                + "    logEx(e);\n"
                + "  }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
