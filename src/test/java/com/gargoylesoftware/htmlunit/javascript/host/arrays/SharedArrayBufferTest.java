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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.EDGE;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link SharedArrayBuffer}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
@Ignore
public class SharedArrayBufferTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "5")
    public void byteLength() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (typeof SharedArrayBuffer === 'undefined') { alert('no SharedArrayBuffer'); return; }\n"
            + "  var buff = new SharedArrayBuffer(5);\n"
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
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = {"1234", "1234", "6789", "1234"})
    @NotYetImplemented({CHROME, EDGE})
    public void slice() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  if (typeof SharedArrayBuffer === 'undefined') { alert('no SharedArrayBuffer'); return; }\n"
            + "  var buffer = new SharedArrayBuffer(12);\n"
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
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = {"2", "1234", "0"})
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidStartIndexNumberString() throws Exception {
        sliceInvalidIndex("'4'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "exception")
    public void sliceInvalidStartIndexDouble() throws Exception {
        sliceInvalidIndex("2.14");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = {"3", "0", "1234", "0"})
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidStartIndexString() throws Exception {
        sliceInvalidIndex("'four'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "exception")
    public void sliceInvalidStartIndexTrue() throws Exception {
        sliceInvalidIndex("true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "0")
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidStartIndexPositiveInfinity() throws Exception {
        sliceInvalidIndex("Number.POSITIVE_INFINITY");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = {"3", "0", "1234", "0"})
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidStartIndexNegativeInfinity() throws Exception {
        sliceInvalidIndex("Number.NEGATIVE_INFINITY");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = {"3", "0", "1234", "0"})
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidStartIndexNaN() throws Exception {
        sliceInvalidIndex("NaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = {"3", "0", "1234", "0"})
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidStartIndexNull() throws Exception {
        sliceInvalidIndex("null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "0")
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidEndIndexNumberString() throws Exception {
        sliceInvalidIndex("4, '4'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "0")
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidEndIndexString() throws Exception {
        sliceInvalidIndex("4, 'four'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "0")
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidEndIndexTrue() throws Exception {
        sliceInvalidIndex("4, true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "0")
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidEndIndexNaN() throws Exception {
        sliceInvalidIndex("4, NaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "0")
    @NotYetImplemented({CHROME, EDGE})
    public void sliceInvalidEndIndexNull() throws Exception {
        sliceInvalidIndex("4, null");
    }

    private void sliceInvalidIndex(final String index) throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "  if (typeof SharedArrayBuffer === 'undefined') { alert('no SharedArrayBuffer'); return; }\n"
            + "    var buffer = new SharedArrayBuffer(12);\n"
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
    @Alerts(DEFAULT = "no SharedArrayBuffer",
            CHROME = "0")
    public void nullConstructor() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  if (typeof SharedArrayBuffer === 'undefined') { alert('no SharedArrayBuffer'); return; }\n"
            + "  var array = new SharedArrayBuffer(null);\n"
            + "  alert(array.byteLength);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

}
