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
package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for ArrayBufferView.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ArrayBufferViewTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"18", "93", "42"})
    public void set_int8() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var array = new Int8Array(10);\n"
            + "  array.set(new Int8Array([18, 93, 42]), 3);\n"
            + "  alert(array[3]);\n"
            + "  alert(array[4]);\n"
            + "  alert(array[5]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("10")
    public void set_empty() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var array = new Int8Array(10);\n"
            + "  array.set({});\n"
            + "  alert(array.length);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "2", "3", "-1"})
    public void subarray_int8() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var x = new Int8Array([0, 1, 2, 3, 4, 5]);\n"
            + "  var y = x.subarray(2, 5);\n"
            + "  alert(y.length);\n"
            + "  alert(y[0]);\n"
            + "  alert(y[1]);\n"
            + "  y[0] = -1;\n"
            + "  alert(x[2]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"8", "0", "0", "0", "10", "1", "2", "2", "2"})
    public void ctorInvalidValuesInt() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var x = new Int8Array([null, 'null', undefined, '10', true, 2.4, 2.5, '2.6']);\n"
            + "  alert(x.length);\n"
            + "  for(var i = 0; i < x.length; i++) {\n"
            + "    alert(x[i]);\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7", "0", "NaN", "NaN", "10", "1", "2.5", "2.75"})
    public void ctorInvalidValuesFloat() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var x = new Float32Array([null, 'null', undefined, '10', true, 2.5, '2.75']);\n"
            + "    alert(x.length);\n"
            + "    for(var i = 0; i < x.length; i++) {\n"
            + "      alert(x[i]);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    alert('exception');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
