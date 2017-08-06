/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF45;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

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
            FF45 = {"1<>5", "5<>2", "1<>2", "1<>9", "5<>1", "1<>1", "2<>1", "2<>9", "5<>9"},
            IE = {"5<>1", "2<>5", "2<>1", "2<>5", "1<>5", "1<>2", "1<>1", "9<>5"})
    @NotYetImplemented({FF45, IE})
    public void sort() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function compare(x, y) {\n"
            + "  alert('' + x + '<>' + y);\n"
            + "  return x - y;\n"
            + "}\n"
            + "function doTest() {\n"
            + "  var t = [1, 5, 2, 1, 9];\n"
            + "  t.sort(compare);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
        loadPageWithAlerts2(html);
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
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "toSource: undefined",
            FF = "toSource: function")
    public void methods_toSource() throws Exception {
        final String[] methods = {"toSource"};
        final String html = NativeDateTest.createHTMLTestMethods("[]", methods);
        loadPageWithAlerts2(html);
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
            + "Array.prototype.foo = function() { alert('hello')};\n"
            + "[].foo();\n"
            + "var x = [];\n"
            + "for (var i in x) {\n"
            + "  alert(i);\n"
            + "  delete x[i];\n"
            + "}\n"
            + "[].foo();\n"
            + "</script></body>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "function Array() { [native code] }",
            FF = "function Array() {\n    [native code]\n}",
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
     * Test for "Comparison method violates its general contract!".
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("finished")
    public void comparisonMethodViolatesContract() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "var results = [1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1,"
            + " -1, -1, 1, -1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 1, -1,"
            + " 1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, -1, -1, 0, -1, -1, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,"
            + " 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1,"
            + " 1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1];\n"
            + "var index = 0;\n"
            + "function test() {\n"
            + "  var arr = new Array(37);\n"
            + "  for (var x = 0; x < arr.length; x++) {\n"
            + "    arr[x] = new Object();\n"
            + "  }\n"
            + "  arr.sort(function (a, b) {\n"
            + "    return results[index++];\n"
            + "  });\n"
            + "  alert('finished');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for "Comparison method violates its general contract!".
     * @throws Exception if the test fails
     */
    @Test
    public void comparisonMethodViolatesContract2() throws Exception {
        final Properties props = System.getProperties();
        props.list(System.out);
        final int[] results = {1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1,
            -1, -1, 1, -1, 1, -1, 1, 1, 1, 1, -1, -1, 1, -1, -1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, -1, 1, -1,
            1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, -1, -1, 0, -1, -1, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0,
            1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, 1, 1, 1, 1,
            1, 1, 1, -1, -1, -1, -1, -1, -1, -1, -1, 1, 1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
        final Integer[] arr = new Integer[37];
        final AtomicInteger index = new AtomicInteger();
        Arrays.sort(arr, (e1, e2) -> results[index.incrementAndGet()]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"angel,clown,mandarin", "clown,mandarin", "angel"})
    public void shift() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  var myFish = ['angel', 'clown', 'mandarin' ];\n"
            + "  alert(myFish);\n"

            + "  var shifted = myFish.shift();\n"
            + "  alert(myFish);\n"
            + "  alert(shifted);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
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
            + "  var myFish = [ 'mandarin' ];\n"
            + "  alert(myFish);\n"

            + "  var shifted = myFish.shift();\n"
            + "  alert(myFish);\n"
            + "  alert(shifted);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "  var myFish = [ ];\n"
            + "  alert(myFish);\n"

            + "  var shifted = myFish.shift();\n"
            + "  alert(myFish);\n"
            + "  alert(shifted);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "  if (Array.from) {\n"
            + "    var arr = Array.from('abc');\n"
            + "    alert(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      alert(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    alert('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "  if (Array.from) {\n"
            + "    var arr = Array.from(['a', 'b', 'c']);\n"
            + "    alert(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      alert(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    alert('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "  if (Array.from) {\n"
            + "    var s = new Set(['abc', window]);\n"
            + "    var arr = Array.from(s);\n"
            + "    alert(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      alert(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    alert('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "  if (Array.from) {\n"
            + "    var m = new Map([[1, 2], [3, 4], [5, 6]]);\n"
            + "    var arr = Array.from(m);\n"
            + "    alert(arr.length);\n"
            + "    for (var i = 0; i < arr.length; i++) {\n"
            + "      alert(arr[i]);\n"
            + "    }\n"
            + "  } else {\n"
            + "    alert('not supported');\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function find() { [native code] }", "20"},
            FF = {"function find() {\n    [native code]\n}", "20"},
            IE = {"\nfunction find() {\n    [native code]\n}\n", "20"})
    public void find() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  alert(arr.find);\n"
            + "  alert(arr.find(isBig));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function find() { [native code] }", "20"},
            FF = {"function find() {\n    [native code]\n}", "20"},
            IE = {"\nfunction find() {\n    [native code]\n}\n", "20"})
    public void findPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  var find = Array.prototype.find;\n"
            + "  alert(find);\n"
            + "  alert(find.call(arr, isBig));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    @NotYetImplemented
    public void findStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  alert(Array.find);\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  try {\n"
            + "    alert(Array.find(arr, isBig));\n"
            + "  } catch(e) { alert('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function findIndex() { [native code] }", "1"},
            FF = {"function findIndex() {\n    [native code]\n}", "1"},
            IE = {"\nfunction findIndex() {\n    [native code]\n}\n", "1"})
    public void findIndex() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  alert(arr.findIndex);\n"
            + "  alert(arr.findIndex(isBig));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function findIndex() { [native code] }", "1"},
            FF = {"function findIndex() {\n    [native code]\n}", "1"},
            IE = {"\nfunction findIndex() {\n    [native code]\n}\n", "1"})
    public void findIndexPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  var findIndex = Array.prototype.findIndex;\n"
            + "  alert(findIndex);\n"
            + "  alert(findIndex.call(arr, isBig));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "TypeError"})
    @NotYetImplemented
    public void findIndexStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  alert(Array.findIndex);\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  try {\n"
            + "    alert(Array.findIndex(arr, isBig));\n"
            + "  } catch(e) { alert('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function filter() { [native code] }", "20,17"},
            FF = {"function filter() {\n    [native code]\n}", "20,17"},
            IE = {"\nfunction filter() {\n    [native code]\n}\n", "20,17"})
    public void filter() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  alert(arr.filter);\n"
            + "  alert(arr.filter(isBig));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function filter() { [native code] }", "20,17"},
            FF = {"function filter() {\n    [native code]\n}", "20,17"},
            IE = {"\nfunction filter() {\n    [native code]\n}\n", "20,17"})
    public void filterPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  var filter = Array.prototype.filter;\n"
            + "  alert(filter);\n"
            + "  alert(filter.call(arr, isBig));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "TypeError"},
            FF = {"function filter() {\n    [native code]\n}", "20,17"})
    @NotYetImplemented
    public void filterStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isBig(n) { return n >= 10; }\n"
            + "\n"
            + "  alert(Array.filter);\n"
            + "  var arr = [1, 20, 7, 17];\n"
            + "  try {\n"
            + "    alert(Array.filter(arr, isBig));\n"
            + "  } catch(e) { alert('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function map() { [native code] }", "1,2,3,4"},
            FF = {"function map() {\n    [native code]\n}", "1,2,3,4"},
            IE = {"\nfunction map() {\n    [native code]\n}\n", "1,2,3,4"})
    public void map() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  alert(arr.map);\n"
            + "  alert(arr.map(Math.sqrt));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function map() { [native code] }", "1,2,3,4"},
            FF = {"function map() {\n    [native code]\n}", "1,2,3,4"},
            IE = {"\nfunction map() {\n    [native code]\n}\n", "1,2,3,4"})
    public void mapPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var map = Array.prototype.map;\n"
            + "  alert(map);\n"
            + "  alert(map.call(arr, Math.sqrt));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "TypeError"},
            FF = {"function map() {\n    [native code]\n}", "1,2,3,4"})
    @NotYetImplemented
    public void mapStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  alert(Array.map);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    alert(Array.map(arr, Math.sqrt));\n"
            + "  } catch(e) { alert('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function every() { [native code] }", "false"},
            FF = {"function every() {\n    [native code]\n}", "false"},
            IE = {"\nfunction every() {\n    [native code]\n}\n", "false"})
    public void every() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  alert(arr.every);\n"
            + "  alert(arr.every(isSmall));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function every() { [native code] }", "false"},
            FF = {"function every() {\n    [native code]\n}", "false"},
            IE = {"\nfunction every() {\n    [native code]\n}\n", "false"})
    public void everyPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var every = Array.prototype.every;\n"
            + "  alert(every);\n"
            + "  alert(every.call(arr, isSmall));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "TypeError"},
            FF = {"function every() {\n    [native code]\n}", "false"})
    @NotYetImplemented
    public void everyStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  alert(Array.every);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    alert(Array.every(arr, isSmall));\n"
            + "  } catch(e) { alert('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function some() { [native code] }", "true"},
            FF = {"function some() {\n    [native code]\n}", "true"},
            IE = {"\nfunction some() {\n    [native code]\n}\n", "true"})
    public void some() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  alert(arr.some);\n"
            + "  alert(arr.some(isSmall));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function some() { [native code] }", "true"},
            FF = {"function some() {\n    [native code]\n}", "true"},
            IE = {"\nfunction some() {\n    [native code]\n}\n", "true"})
    public void somePrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var some = Array.prototype.some;\n"
            + "  alert(some);\n"
            + "  alert(some.call(arr, isSmall));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "TypeError"},
            FF = {"function some() {\n    [native code]\n}", "true"})
    @NotYetImplemented
    public void someStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function isSmall(n) { return n < 10; }\n"
            + "\n"
            + "  alert(Array.some);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    alert(Array.some(arr, isSmall));\n"
            + "  } catch(e) { alert('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function forEach() { [native code] }", "4", "7"},
            FF = {"function forEach() {\n    [native code]\n}", "4", "7"},
            IE = {"\nfunction forEach() {\n    [native code]\n}\n", "4", "7"})
    public void forEach() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  var arr = [4, 7];\n"
            + "  alert(arr.forEach);\n"
            + "  arr.forEach(function(elem) { alert(elem) });\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function forEach() { [native code] }", "4", "7"},
            FF = {"function forEach() {\n    [native code]\n}", "4", "7"},
            IE = {"\nfunction forEach() {\n    [native code]\n}\n", "4", "7"})
    public void forEachPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  var arr = [4, 7];\n"
            + "  var forEach = Array.prototype.forEach;\n"
            + "  alert(forEach);\n"
            + "  forEach.call(arr, function(elem) { alert(elem) });\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "TypeError"},
            FF = {"function forEach() {\n    [native code]\n}", "4", "7"})
    @NotYetImplemented({CHROME, IE})
    public void forEachStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  alert(Array.forEach);\n"
            + "  var arr = [4, 7];\n"
            + "  try {\n"
            + "    Array.forEach(arr, function(elem) { alert(elem) });\n"
            + "  } catch(e) { alert('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function reduce() { [native code] }", "30"},
            FF = {"function reduce() {\n    [native code]\n}", "30"},
            IE = {"\nfunction reduce() {\n    [native code]\n}\n", "30"})
    public void reduce() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function sum(acc, val) { return acc + val; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  alert(arr.reduce);\n"
            + "  alert(arr.reduce(sum));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function reduce() { [native code] }", "30"},
            FF = {"function reduce() {\n    [native code]\n}", "30"},
            IE = {"\nfunction reduce() {\n    [native code]\n}\n", "30"})
    public void reducePrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function sum(acc, val) { return acc + val; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var reduce = Array.prototype.reduce;\n"
            + "  alert(reduce);\n"
            + "  alert(reduce.call(arr, sum));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "TypeError"},
            FF = {"function reduce() {\n    [native code]\n}", "30"})
    @NotYetImplemented({CHROME, IE})
    public void reduceStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function sum(acc, val) { return acc + val; }\n"
            + "\n"
            + "  alert(Array.reduce);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    alert(Array.reduce(arr, sum));\n"
            + "  } catch(e) { alert('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function reduceRight() { [native code] }", "2"},
            FF = {"function reduceRight() {\n    [native code]\n}", "2"},
            IE = {"\nfunction reduceRight() {\n    [native code]\n}\n", "2"})
    public void reduceRight() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function diff(acc, val) { return acc - val; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  alert(arr.reduceRight);\n"
            + "  alert(arr.reduceRight(diff));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function reduceRight() { [native code] }", "2"},
            FF = {"function reduceRight() {\n    [native code]\n}", "2"},
            IE = {"\nfunction reduceRight() {\n    [native code]\n}\n", "2"})
    public void reduceRightPrototype() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function diff(acc, val) { return acc - val; }\n"
            + "\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  var reduceRight = Array.prototype.reduceRight;\n"
            + "  alert(reduceRight);\n"
            + "  alert(reduceRight.call(arr, diff));\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "TypeError"},
            FF = {"function reduceRight() {\n    [native code]\n}", "2"})
    @NotYetImplemented({CHROME, IE})
    public void reduceRightStatic() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function diff(acc, val) { return acc - val; }\n"
            + "\n"
            + "  alert(Array.reduceRight);\n"
            + "  var arr = [1, 4, 9, 16];\n"
            + "  try {\n"
            + "    alert(Array.reduceRight(arr, diff));\n"
            + "  } catch(e) { alert('TypeError'); }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html, URL_FIRST, DEFAULT_WAIT_TIME);
    }
}
