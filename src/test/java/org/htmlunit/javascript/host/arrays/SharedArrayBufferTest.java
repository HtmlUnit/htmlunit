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
package org.htmlunit.javascript.host.arrays;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for SharedArrayBuffer.
 *
 * @author Ronald Brill
 */
public class SharedArrayBufferTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void byteLength() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (typeof SharedArrayBuffer === 'undefined') { log('no SharedArrayBuffer'); return; }\n"
            + "  var buff = new SharedArrayBuffer(5);\n"
            + "  log(buff.byteLength);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void slice() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (typeof SharedArrayBuffer === 'undefined') { log('no SharedArrayBuffer'); return; }\n"
            + "  var buffer = new SharedArrayBuffer(12);\n"
            + "  var x = new Int32Array(buffer);\n"
            + "  x[1] = 1234;\n"
            + "  var slice = buffer.slice(4);\n"
            + "  var y = new Int32Array(slice);\n"
            + "  log(x[1]);\n"
            + "  log(y[0]);\n"
            + "  x[1] = 6789;\n"
            + "  log(x[1]);\n"
            + "  log(y[0]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidStartIndexNumberString() throws Exception {
        sliceInvalidIndex("'4'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidStartIndexDouble() throws Exception {
        sliceInvalidIndex("2.14");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidStartIndexString() throws Exception {
        sliceInvalidIndex("'four'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidStartIndexTrue() throws Exception {
        sliceInvalidIndex("true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidStartIndexPositiveInfinity() throws Exception {
        sliceInvalidIndex("Number.POSITIVE_INFINITY");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidStartIndexNegativeInfinity() throws Exception {
        sliceInvalidIndex("Number.NEGATIVE_INFINITY");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidStartIndexNaN() throws Exception {
        sliceInvalidIndex("NaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidStartIndexNull() throws Exception {
        sliceInvalidIndex("null");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidEndIndexNumberString() throws Exception {
        sliceInvalidIndex("4, '4'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidEndIndexString() throws Exception {
        sliceInvalidIndex("4, 'four'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidEndIndexTrue() throws Exception {
        sliceInvalidIndex("4, true");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidEndIndexNaN() throws Exception {
        sliceInvalidIndex("4, NaN");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void sliceInvalidEndIndexNull() throws Exception {
        sliceInvalidIndex("4, null");
    }

    private void sliceInvalidIndex(final String index) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "  if (typeof SharedArrayBuffer === 'undefined') { log('no SharedArrayBuffer'); return; }\n"
            + "    var buffer = new SharedArrayBuffer(12);\n"
            + "    var x = new Int32Array(buffer);\n"
            + "    x[1] = 1234;\n"
            + "    var slice = buffer.slice(" + index + ");\n"
            + "    var y = new Int32Array(slice);\n"
            + "    log(y.length);\n"
            + "    for(var i = 0; i < y.length; i++) {\n"
            + "      log(y[i]);\n"
            + "    }\n"
            + "  } catch(e) {logEx(e);}\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no SharedArrayBuffer")
    public void nullConstructor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  if (typeof SharedArrayBuffer === 'undefined') { log('no SharedArrayBuffer'); return; }\n"
            + "  var array = new SharedArrayBuffer(null);\n"
            + "  log(array.byteLength);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

}
