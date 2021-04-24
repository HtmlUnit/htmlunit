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
 * Tests for ArrayBuffer.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ArrayBufferTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void ctorLengthZero() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var buff = new ArrayBuffer(0);\n"
            + "  alert(buff.byteLength);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception true")
    public void ctorLengthNegative() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var buff = new ArrayBuffer(-1);\n"
            + "    alert(buff.byteLength);\n"
            + "  } catch(e) { alert('exception ' + (e instanceof RangeError)); }"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("5")
    public void byteLength() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var buff = new ArrayBuffer(5);\n"
            + "  alert(buff.byteLength);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1234", "1234", "6789", "1234"})
    public void slice() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var buffer = new ArrayBuffer(12);\n"
            + "  var x = new Int32Array(buffer);\n"
            + "  x[1] = 1234;\n"
            + "  var slice = buffer.slice(4);\n"
            + "  var y = new Int32Array(slice);\n"
            + "  alert(x[1]);\n"
            + "  alert(y[0]);\n"
            + "  x[1] = 6789;\n"
            + "  alert(x[1]);\n"
            + "  alert(y[0]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "1234", "0"})
    public void sliceInvalidStartIndexNumberString() throws Exception {
        sliceInvalidIndex("'4'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void sliceInvalidStartIndexDouble() throws Exception {
        sliceInvalidIndex("2.14");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "0", "1234", "0"})
    public void sliceInvalidStartIndexString() throws Exception {
        sliceInvalidIndex("'four'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void sliceInvalidStartIndexTrue() throws Exception {
        sliceInvalidIndex("true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void sliceInvalidStartIndexPositiveInfinity() throws Exception {
        sliceInvalidIndex("Number.POSITIVE_INFINITY");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "0", "1234", "0"})
    public void sliceInvalidStartIndexNegativeInfinity() throws Exception {
        sliceInvalidIndex("Number.NEGATIVE_INFINITY");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "0", "1234", "0"})
    public void sliceInvalidStartIndexNaN() throws Exception {
        sliceInvalidIndex("NaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3", "0", "1234", "0"})
    public void sliceInvalidStartIndexNull() throws Exception {
        sliceInvalidIndex("null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void sliceInvalidEndIndexNumberString() throws Exception {
        sliceInvalidIndex("4, '4'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void sliceInvalidEndIndexString() throws Exception {
        sliceInvalidIndex("4, 'four'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void sliceInvalidEndIndexTrue() throws Exception {
        sliceInvalidIndex("4, true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void sliceInvalidEndIndexNaN() throws Exception {
        sliceInvalidIndex("4, NaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void sliceInvalidEndIndexNull() throws Exception {
        sliceInvalidIndex("4, null");
    }

    private void sliceInvalidIndex(final String index) throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var buffer = new ArrayBuffer(12);\n"
            + "    var x = new Int32Array(buffer);\n"
            + "    x[1] = 1234;\n"
            + "    var slice = buffer.slice(" + index + ");\n"
            + "    var y = new Int32Array(slice);\n"
            + "    alert(y.length);\n"
            + "    for(var i = 0; i < y.length; i++) {\n"
            + "      alert(y[i]);\n"
            + "    }\n"
            + "  } catch(e) {\n"
            + "    alert('exception');\n"
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
    @Alerts("0")
    public void nullConstructor() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  var array = new ArrayBuffer(null);\n"
            + "  alert(array.byteLength);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

}
