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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Array is a native JavaScript object and therefore provided by Rhino but behavior should be
 * different depending on the simulated browser.
 *
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class NativeArrayTest extends WebDriverTestCase {

    /**
     * Test for sort algorithm used (when sort is called with callback).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1<>5", "5<>2", "1<>2", "5<>1", "2<>1", "1<>1", "5<>9"},
            CHROME = {"5<>1", "2<>5", "2<>5", "2<>1", "1<>2", "1<>1", "9<>2"},
            EDGE = {"5<>1", "2<>5", "2<>5", "2<>1", "1<>2", "1<>1", "9<>2"},
            IE = {"5<>1", "2<>5", "2<>1", "2<>5", "1<>5", "1<>2", "1<>1", "9<>5"})
    @NotYetImplemented({CHROME, EDGE, IE})
    public void sortSteps() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function compare(x, y) {\n"
            + "  log('' + x + '<>' + y);\n"
            + "  return x - y;\n"
            + "}\n"
            + "function doTest() {\n"
            + "  var t = [1, 5, 2, 1, 9];\n"
            + "  t.sort(compare);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"concat: function", "constructor: function", "isArray: undefined", "join: function", "pop: function",
        "push: function", "reverse: function", "shift: function", "slice: function", "sort: function",
        "splice: function", "toLocaleString: function", "toString: function", "unshift: function"})
    public void methods_common() throws Exception {
        final String[] methods = {"concat", "constructor", "isArray", "join", "pop", "push", "reverse", "shift",
            "slice", "sort", "splice", "toLocaleString", "toString", "unshift"};
        final String html = NativeDateTest.createHTMLTestMethods("[]", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"every: function", "filter: function", "forEach: function", "indexOf: function",
            "lastIndexOf: function", "map: function", "reduce: function", "reduceRight: function", "some: function"})
    public void methods_different() throws Exception {
        final String[] methods = {"every", "filter", "forEach", "indexOf", "lastIndexOf", "map", "reduce",
            "reduceRight", "some"};
        final String html = NativeDateTest.createHTMLTestMethods("[]", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("toSource: undefined")
    public void methods_toSource() throws Exception {
        final String[] methods = {"toSource"};
        final String html = NativeDateTest.createHTMLTestMethods("[]", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * Rhino version used in HtmlUnit incorrectly walked the prototype chain while deleting property.
     * @see <a href="http://sf.net/support/tracker.php?aid=2834335">Bug 2834335</a>
     * @see <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=510504">corresponding Rhino bug</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hello", "foo", "hello"})
    public void deleteShouldNotWalkPrototypeChain() throws Exception {
        final String html = "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "Array.prototype.foo = function() { log('hello')};\n"
            + "[].foo();\n"
            + "var x = [];\n"
            + "for (var i in x) {\n"
            + "  log(i);\n"
            + "  delete x[i];\n"
            + "}\n"
            + "[].foo();\n"
            + "</script></body>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function Array() { [native code] }",
            FF = "function Array() {\n    [native code]\n}",
            FF78 = "function Array() {\n    [native code]\n}",
            IE = "\nfunction Array() {\n    [native code]\n}\n")
    public void constructorToString() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "alert([].constructor.toString());\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"mandarin", "", "mandarin"})
    public void shiftOneElement() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var myFish = [ 'mandarin' ];\n"
            + "  log(myFish);\n"

            + "  var shifted = myFish.shift();\n"
            + "  log(myFish);\n"
            + "  log(shifted);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "undefined"})
    public void shiftEmpty() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var myFish = [ ];\n"
            + "  log(myFish);\n"

            + "  var shifted = myFish.shift();\n"
            + "  log(myFish);\n"
            + "  log(shifted);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "a", "b", "c"},
            IE = "not supported")
    public void fromString() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.from) {\n"
            + "    var arr = Array.from('abc');\n"
            + "    log(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "a", "b", "c"},
            IE = "not supported")
    public void fromArray() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.from) {\n"
            + "    var arr = Array.from(['a', 'b', 'c']);\n"
            + "    log(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "a", "b", "c"},
            IE = "not supported")
    public void fromArrayIterator() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.from) {\n"
            + "    var input = ['a', 'b', 'c'];\n"
            + "    var arr = Array.from(input[Symbol.iterator]());\n"
            + "    log(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"4", "T", "e", "s", "t"},
            IE = "not supported")
    public void fromStringIterator() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.from) {\n"
            + "    var arr = Array.from('Test'[Symbol.iterator]());\n"
            + "    log(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"2", "abc", "[object Window]"},
            IE = "not supported")
    public void fromSet() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.from) {\n"
            + "    var s = new Set(['abc', window]);\n"
            + "    var arr = Array.from(s);\n"
            + "    log(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"3", "1,2", "3,4", "5,6"},
            IE = "not supported")
    public void fromMap() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.from) {\n"
            + "    var m = new Map([[1, 2], [3, 4], [5, 6]]);\n"
            + "    var arr = Array.from(m);\n"
            + "    log(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "by"},
            IE = "not supported")
    public void fromUserDefinedIterable() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.from) {\n"
            + "    var myIterable = {};\n"
            + "    myIterable[Symbol.iterator] = function() {\n"
            + "      return {\n"
            + "        next: function(){\n"
            + "          if (this._first) {;\n"
            + "            this._first = false;\n"
            + "            return { value: 'by', done: false };\n"
            + "          }\n"
            + "          return { done: true };\n"
            + "        },\n"
            + "        _first: true\n"
            + "      };\n"
            + "    };\n"

            + "    var arr = Array.from(myIterable);\n"
            + "    log(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "TypeError",
            IE = "not supported")
    public void fromObject() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.from) {\n"
            + "    var myIterable = {};\n"
            + "    myIterable[Symbol.iterator] = function() {\n"
            + "      return { done: true };\n"
            + "    };\n"

            + "    try {\n"
            + "      Array.from(myIterable);\n"
            + "    } catch(e) { log('TypeError'); }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE = "not supported")
    public void fromNativeObject() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.from) {\n"
            + "    var arr = Array.from({firstName: 'Erika', age: 42});\n"
            + "    log(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"1", "abc"},
            IE = "not supported")
    public void fromArguments() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(a) { return Array.from(arguments); }\n"

            + "  if (Array.from) {\n"
            + "    var arr = test('abc') \n"
            + "    log(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      log(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true", "false", "false", "false",
                "false", "false", "false", "false", "false", "false"})
    public void isArray() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(Array.isArray([]));\n"
            + "  log(Array.isArray([1]));\n"
            + "  log(Array.isArray( new Array() ));\n"
            + "  log(Array.isArray( Array.prototype ));\n"
            + "  log(Array.isArray());\n"
            + "  log(Array.isArray({}));\n"
            + "  log(Array.isArray(null));\n"
            + "  log(Array.isArray(undefined));\n"
            + "  log(Array.isArray(17));\n"
            + "  log(Array.isArray('Array'));\n"
            + "  log(Array.isArray(true));\n"
            + "  log(Array.isArray(false));\n"
            + "  log(Array.isArray({ __proto__ : Array.prototype }));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "20"},
            IE = "undefined")
    @NotYetImplemented(IE)
    public void find() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  log(typeof arr.find);\n"
            + "  if (typeof arr.find == 'function') {\n"
            + "    log(arr.find(isBig));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "20"},
            IE = "undefined")
    @NotYetImplemented(IE)
    public void findPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  var find = Array.prototype.find;\n"
            + "  log(typeof find);\n"
            + "  if (typeof find == 'function') {\n"
            + "    log(find.call(arr, isBig));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void findStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  log(typeof Array.find);\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  try {\n"
            + "    log(Array.find(arr, isBig));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "1"},
            IE = "undefined")
    @NotYetImplemented(IE)
    public void findIndex() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  log(typeof arr.findIndex);\n"
            + "  if (typeof arr.findIndex == 'function') {\n"
            + "    log(arr.findIndex(isBig));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "1"},
            IE = "undefined")
    @NotYetImplemented(IE)
    public void findIndexPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  var findIndex = Array.prototype.findIndex;\n"
            + "  log(typeof findIndex);\n"
            + "  if (typeof findIndex == 'function') {\n"
            + "    log(findIndex.call(arr, isBig));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void findIndexStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  log(typeof Array.findIndex);\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  try {\n"
            + "    log(Array.findIndex(arr, isBig));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "20,17"})
    public void filter() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  log(typeof arr.filter);\n"
            + "  log(arr.filter(isBig));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "20,17"})
    public void filterPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  var filter = Array.prototype.filter;\n"
            + "  log(typeof filter);\n"
            + "  log(filter.call(arr, isBig));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void filterStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  log(typeof Array.filter);\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  try {\n"
            + "    log(Array.filter(arr, isBig));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1,2,3,4"})
    public void map() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.map);\n"
            + "  log(arr.map(Math.sqrt));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1,2,3,4"})
    public void mapPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var map = Array.prototype.map;\n"
            + "  log(typeof map);\n"
            + "  log(map.call(arr, Math.sqrt));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void mapStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.map);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.map(arr, Math.sqrt));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "false"})
    public void every() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.every);\n"
            + "  log(arr.every(isSmall));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "false"})
    public void everyPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var every = Array.prototype.every;\n"
            + "  log(typeof every);\n"
            + "  log(every.call(arr, isSmall));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void everyStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  log(typeof Array.every);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.every(arr, isSmall));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "true"})
    public void some() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.some);\n"
            + "  log(arr.some(isSmall));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "true"})
    public void somePrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var some = Array.prototype.some;\n"
            + "  log(typeof some);\n"
            + "  log(some.call(arr, isSmall));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void someStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  log(typeof Array.some);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.some(arr, isSmall));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "4", "7"})
    public void forEach() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [4, 7];\n"
            + "  log(typeof arr.forEach);\n"
            + "  arr.forEach(function(elem) { log(elem) });\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "4", "7"})
    public void forEachPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [4, 7];\n"
            + "  var forEach = Array.prototype.forEach;\n"
            + "  log(typeof forEach);\n"
            + "  forEach.call(arr, function(elem) { log(elem) });\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void forEachStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.forEach);\n"
            + "  var arr = [4, 7];\n"
            + "  try {\n"
            + "    Array.forEach(arr, function(elem) { log(elem) });\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "30"})
    public void reduce() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function sum(acc, val) { return acc + val; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.reduce);\n"
            + "  log(arr.reduce(sum));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "30"})
    public void reducePrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function sum(acc, val) { return acc + val; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var reduce = Array.prototype.reduce;\n"
            + "  log(typeof reduce);\n"
            + "  log(reduce.call(arr, sum));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void reduceStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function sum(acc, val) { return acc + val; }\n"
            + "\n"
            + "  log(typeof Array.reduce);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.reduce(arr, sum));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "2"})
    public void reduceRight() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function diff(acc, val) { return acc - val; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.reduceRight);\n"
            + "  log(arr.reduceRight(diff));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "2"})
    public void reduceRightPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function diff(acc, val) { return acc - val; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var reduceRight = Array.prototype.reduceRight;\n"
            + "  log(typeof reduceRight);\n"
            + "  log(reduceRight.call(arr, diff));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void reduceRightStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function diff(acc, val) { return acc - val; }\n"
            + "\n"
            + "  log(typeof Array.reduceRight);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.reduceRight(arr, diff));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1,4,9,16"})
    public void join() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.join);\n"
            + "  log(arr.join());\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1,4,9,16"})
    public void joinPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var join = Array.prototype.join;\n"
            + "  log(typeof join);\n"
            + "  log(join.call(arr));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void joinStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.join);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.join(arr));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "16,9,4,1"})
    public void reverse() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.reverse);\n"
            + "  log(arr.reverse());\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "16,9,4,1"})
    public void reversePrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var reverse = Array.prototype.reverse;\n"
            + "  log(typeof reverse);\n"
            + "  log(reverse.call(arr));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void reverseStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.reverse);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.reverse(arr));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1,16,4,9"})
    public void sort() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.sort);\n"
            + "  log(arr.sort());\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1,16,4,9"})
    public void sortPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var sort = Array.prototype.sort;\n"
            + "  log(typeof sort);\n"
            + "  log(sort.call(arr));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void sortStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.sort);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.sort(arr));\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "6", "1,4,9,16,3,7"})
    public void push() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.push);\n"
            + "  log(arr.push(3, 7));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "6", "1,4,9,16,3,7"})
    public void pushPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var push = Array.prototype.push;\n"
            + "  log(typeof push);\n"
            + "  log(push.call(arr, 3, 7));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void pushStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.push);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.push(arr, 3, 7));\n"
            + "    log(arr);\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "16", "1,4,9"})
    public void pop() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.pop);\n"
            + "  log(arr.pop());\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "16", "1,4,9"})
    public void popPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var pop = Array.prototype.pop;\n"
            + "  log(typeof pop);\n"
            + "  log(pop.call(arr));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void popStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.pop);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.pop(arr));\n"
            + "    log(arr);\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1", "4,9,16"})
    public void shift() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.shift);\n"
            + "  log(arr.shift());\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1", "4,9,16"})
    public void shiftPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var shift = Array.prototype.shift;\n"
            + "  log(typeof shift);\n"
            + "  log(shift.call(arr));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void shiftStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.shift);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.shift(arr));\n"
            + "    log(arr);\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "6", "3,7,1,4,9,16"})
    public void unshift() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.unshift);\n"
            + "  log(arr.unshift(3, 7));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "6", "3,7,1,4,9,16"})
    public void unshiftPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var unshift = Array.prototype.unshift;\n"
            + "  log(typeof unshift);\n"
            + "  log(unshift.call(arr, 3, 7));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void unshiftStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.unshift);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.unshift(arr, 3, 7));\n"
            + "    log(arr);\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "4,9", "1,16"})
    public void splice() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.splice);\n"
            + "  log(arr.splice(1, 2));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "4,9", "1,16"})
    public void splicePrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var splice = Array.prototype.splice;\n"
            + "  log(typeof splice);\n"
            + "  log(splice.call(arr, 1, 2));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void spliceStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.splice);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.splice(arr, 1, 2));\n"
            + "    log(arr);\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1,4,9,16,1,2", "1,4,9,16"})
    public void concat() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.concat);\n"
            + "  log(arr.concat(1, 2));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "1,4,9,16,1,2", "1,4,9,16"})
    public void concatPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var concat = Array.prototype.concat;\n"
            + "  log(typeof concat);\n"
            + "  log(concat.call(arr, 1, 2));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void concatStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.concat);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.concat(arr, 1, 2));\n"
            + "    log(arr);\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "4", "1,4,9,16"})
    public void slice() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.slice);\n"
            + "  log(arr.slice(1, 2));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "4", "1,4,9,16"})
    public void slicePrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var slice = Array.prototype.slice;\n"
            + "  log(typeof slice);\n"
            + "  log(slice.call(arr, 1, 2));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void sliceStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.concat);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.slice(arr, 1, 2));\n"
            + "    log(arr);\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "2", "1,4,9,16"})
    public void indexOf() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.indexOf);\n"
            + "  log(arr.indexOf(9));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "2", "1,4,9,16"})
    public void indexOfPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var indexOf = Array.prototype.indexOf;\n"
            + "  log(typeof indexOf);\n"
            + "  log(indexOf.call(arr, 9));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void indexOfStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.indexOf);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.indexOf(arr, 9));\n"
            + "    log(arr);\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "2", "1,4,9,16"})
    public void lastIndexOf() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  log(typeof arr.lastIndexOf);\n"
            + "  log(arr.lastIndexOf(9));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "2", "1,4,9,16"})
    public void lastIndexOfPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var lastIndexOf = Array.prototype.lastIndexOf;\n"
            + "  log(typeof lastIndexOf);\n"
            + "  log(lastIndexOf.call(arr, 9));\n"
            + "  log(arr);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    public void lastIndexOfStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof Array.lastIndexOf);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    log(Array.lastIndexOf(arr, 9));\n"
            + "    log(arr);\n"
            + "  } catch(e) { log('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "3",
            IE = "not supported")
    public void of() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (Array.of) {\n"
            + "    var arr = Array.of(1, 2, 3);;\n"
            + "    log(arr.length);\n"
            + "  } else {\n"
            + "    log('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
