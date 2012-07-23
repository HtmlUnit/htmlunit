/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Array is a native JavaScript object and therefore provided by Rhino but behavior should be
 * different depending on the simulated browser.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class NativeArrayTest extends WebDriverTestCase {

    /**
     * Test for sort algorithm used (when sort is called with callback).
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    @Alerts(DEFAULT = { "1<>5", "5<>2", "1<>2", "5<>1", "2<>1", "1<>1", "5<>9" },
            IE = { "1<>9", "9<>5", "9<>2", "9<>1", "1<>5", "5<>1", "5<>2", "5<>1", "1<>1", "1<>2", "2<>1", "1<>1" })
    public void sort() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function compare(x, y) {\n"
            + "  alert('' + x + '<>' + y);\n"
            + "  return x - y;\n"
            + "}\n"
            + "function doTest() {\n"
            + "    var t = [1, 5, 2, 1, 9];\n"
            + "    t.sort(compare);\n"
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
    @Alerts({ "concat: function", "constructor: function", "isArray: undefined", "join: function", "pop: function",
        "push: function", "reverse: function", "shift: function", "slice: function", "sort: function",
        "splice: function", "toLocaleString: function", "toString: function", "unshift: function" })
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
    @Alerts(DEFAULT = { "every: function", "filter: function", "forEach: function", "indexOf: function",
            "lastIndexOf: function", "map: function", "reduce: function", "reduceRight: function", "some: function" },
            IE = { "every: undefined", "filter: undefined", "forEach: undefined", "indexOf: undefined",
            "lastIndexOf: undefined", "map: undefined", "reduce: undefined", "reduceRight: undefined",
            "some: undefined" })
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
    @Alerts(FF = "toSource: function", DEFAULT = "toSource: undefined")
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
    @Alerts({ "hello", "foo", "hello" })
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
    @Alerts(FF = "function Array() {\n    [native code]\n}",
            IE = "\nfunction Array() {\n    [native code]\n}\n",
            CHROME = "function Array() { [native code] }")
    public void constructorToString() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "alert([].constructor.toString());\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

}
