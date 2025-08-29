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

/**
 * Object is a native JavaScript object and therefore provided by Rhino but some tests are needed here
 * to be sure that we have the expected results.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ahmed Ashour
 * @author Natasha Lazarova
 * @author Ronald Brill
 */
public class NativeObjectTest extends WebDriverTestCase {

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"assign: undefined", "constructor: function", "create: undefined", "defineProperties: undefined",
             "defineProperty: undefined", "freeze: undefined", "getOwnPropertyDescriptor: undefined",
             "getOwnPropertyNames: undefined", "getPrototypeOf: undefined", "hasOwnProperty: function",
             "isExtensible: undefined", "isFrozen: undefined", "isPrototypeOf: function", "isSealed: undefined",
             "keys: undefined", "preventExtensions: undefined", "propertyIsEnumerable: function", "seal: undefined",
             "toLocaleString: function", "toString: function", "valueOf: function", "__defineGetter__: function",
             "__defineSetter__: function", "__lookupGetter__: function", "__lookupSetter__: function"})
    public void common() throws Exception {
        final String[] methods = {
            "assign", "constructor", "create", "defineProperties", "defineProperty", "freeze",
            "getOwnPropertyDescriptor", "getOwnPropertyNames", "getPrototypeOf", "hasOwnProperty", "isExtensible",
            "isFrozen", "isPrototypeOf", "isSealed", "keys", "preventExtensions", "propertyIsEnumerable", "seal",
            "toLocaleString", "toString", "valueOf", "__defineGetter__", "__defineSetter__",
            "__lookupGetter__", "__lookupSetter__"};
        final String html = NativeDateTest.createHTMLTestMethods("new Object()", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("toSource: undefined")
    public void others() throws Exception {
        final String[] methods = {"toSource"};
        final String html = NativeDateTest.createHTMLTestMethods("new Object()", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void assign() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (Object.assign) {\n"
            + "    var obj = { a: 1 };\n"
            + "    var copy = Object.assign({}, obj);\n"
            + "    log(copy.a);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void assignUndefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "function test() {\n"
            + LOG_TITLE_FUNCTION
            + "  if (Object.assign) {\n"
            + "    var obj = { a: 1 };\n"
            + "    var copy = Object.assign({}, undefined, obj);\n"
            + "    log(copy.a);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void assignUndefined2() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + "function test() {\n"
                + LOG_TITLE_FUNCTION
                + "  if (Object.assign) {\n"
                + "    var copy = Object.assign({}, undefined, undefined);\n"
                + "    log(copy.a);\n"
                + "  }\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void assignNull() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + "function test() {\n"
                + LOG_TITLE_FUNCTION
                + "  if (Object.assign) {\n"
                + "    var copy = Object.assign({}, null);\n"
                + "    log(copy.a);\n"
                + "  }\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void assignNull2() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + "function test() {\n"
                + LOG_TITLE_FUNCTION
                + "  if (Object.assign) {\n"
                + "    var copy = Object.assign({}, null, null);\n"
                + "    log(copy.a);\n"
                + "  }\n"
                + "}\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function\\s()\\s{\\s[native\\scode]\\s}",
            FF = "function\\s()\\s{\\n\\s\\s\\s\\s[native\\scode]\\n}",
            FF_ESR = "function\\s()\\s{\\n\\s\\s\\s\\s[native\\scode]\\n}")
    public void proto() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(Object.__proto__);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for Bug #1856.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Object]", "null"})
    public void proto2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log({}.__proto__);\n"
            + "    log({}.__proto__.__proto__);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for #1855.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getPrototypeOfString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(String.prototype === Object.getPrototypeOf(''));\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getPrototypeOfNumber() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(Number.prototype === Object.getPrototypeOf(1));\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getPrototypeOfBoolean() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(Boolean.prototype === Object.getPrototypeOf(true));\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("object")
    public void getTypeOfPrototypeOfNumber() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(typeof Object.getPrototypeOf(1));\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "true", "true"})
    public void getOwnPropertySymbols() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var obj = {};\n"
            + "      var a = Symbol('a');\n"
            + "      var b = Symbol.for('b');\n"
            + "\n"
            + "      obj[a] = 'localSymbol';\n"
            + "      obj[b] = 'globalSymbol';\n"
            + "\n"
            + "      var objectSymbols = Object.getOwnPropertySymbols(obj);\n"
            + "      log(objectSymbols.length);\n"
            + "      log(objectSymbols[0] === a);\n"
            + "      log(objectSymbols[1] === b);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void getOwnPropertySymbolsEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var objectSymbols = Object.getOwnPropertySymbols();\n"
            + "      log(objectSymbols.length);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"[object HTMLInputElement]", "[object HTMLInputElement]", "[object Object]", "function"},
            EDGE = {"[object HTMLInputElement]", "[object HTMLInputElement]", "[object Object]", "function"},
            FF = {"[object HTMLInputElement]", "[object HTMLInputElement]", "[object Object]", "function"},
            FF_ESR = {"[object HTMLInputElement]", "[object HTMLInputElement]", "[object Object]", "function"})
    public void getOwnPropertyDescriptor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var input = document.getElementById('myInput');\n"
            + "      log(input);\n"
            + "      var proto = input.constructor.prototype;\n"
            + "      log(proto);\n"
            + "      var desc = Object.getOwnPropertyDescriptor(proto, 'value');\n"
            + "      log(desc);\n"

            + "      log(typeof desc.get);\n"
            + "    } catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='myInput' value='some test'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[object HTMLInputElement]", "x = [object Object]",
                       "x.get = function get value() { [native code] }",
                       "x.get.call = function call() { [native code] }"},
            FF = {"[object HTMLInputElement]", "x = [object Object]",
                  "x.get = function value() {\n    [native code]\n}",
                  "x.get.call = function call() {\n    [native code]\n}"},
            FF_ESR = {"[object HTMLInputElement]", "x = [object Object]",
                      "x.get = function value() {\n    [native code]\n}",
                      "x.get.call = function call() {\n    [native code]\n}"})
    @HtmlUnitNYI(CHROME = {"[object HTMLInputElement]", "x = [object Object]",
                           "x.get = function value() { [native code] }",
                           "x.get.call = function call() { [native code] }"},
            EDGE = {"[object HTMLInputElement]", "x = [object Object]",
                    "x.get = function value() { [native code] }",
                    "x.get.call = function call() { [native code] }"})
    public void getOwnPropertyDescriptorGetCall() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "function test() {\n"
            + "  var proto = i1.constructor.prototype;\n"
            + "  log(proto);\n"
            + "  var x = Object.getOwnPropertyDescriptor(i1.constructor.prototype, 'value');\n"
            + "  log('x = ' + x);\n"
            + "  log('x.get = ' + x.get);\n"
            + "  log('x.get.call = ' + x.get.call);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='text' id='i1' value='foo' />\n"
            + LOG_TEXTAREA
            + "</body></html>";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before: [object Object]", "after: [object Object]", "true"})
    public void definePropertyUsingConsString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  'use strict';\n"
            + "  var f = function () {};\n"
            + "  var a1='proto';\n"
            + "  var p = a1 + 'type';\n"
            + "  log('before: ' + f.prototype);\n"
            + "  Object.defineProperty(f, p, {});\n"
            + "  log('after: ' + f.prototype);\n"
            + "  var p = new f();\n"
            + "  log(p instanceof f);\n"
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
    @Alerts("val1-val2")
    public void objectAssignCopiesSymbols() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var s1 = Symbol('foo');\n"
            + "var s2 = Symbol('bar');\n"
            + "var source = { [s1]: 'val1', [Object(s2)]: 'val2' };\n"
            + "var target = {};\n"
            + "Object.assign(target, source);\n"
            + "log(target[s1] + '-' + target[s2]);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
