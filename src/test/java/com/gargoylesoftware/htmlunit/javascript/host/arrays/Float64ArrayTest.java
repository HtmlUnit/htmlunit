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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for Float64Array.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author Michael Rimov
 */
@RunWith(BrowserRunner.class)
public class Float64ArrayTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"117", "107", "126", "84", "52", "111", "-99", "65"})
    public void bufferConstructor() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var array = new Float64Array([123456789.123456789]);\n"
            + "    var array2 = new Int8Array(array.buffer);\n"
            + "    for (var i = 0; i < array2.length; i++)\n"
            + "      log(array2[i]);\n"
            + "  } catch(e) {\n"
            + "    log('exception');\n"
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
    @Alerts({"undefined", "1234567.8901", "undefined", "undefined"})
    public void index() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var array = new Float64Array([1234567.8901]);\n"
            + "  log(array[-1]);\n"
            + "  log(array[0]);\n"
            + "  log(array[1]);\n"
            + "  log(array[21]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "true", "false", "false"})
    public void in() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var array = new Float64Array([1234567.8901]);\n"
            + "  log(-1 in array);\n"
            + "  log(0 in array);\n"
            + "  log(1 in array);\n"
            + "  log(42 in array);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "6", "NaN", "0", "NaN", "Infinity", "-Infinity", "4", "undefined"})
    public void undefinedValueInArray() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var array = [];\n"
            + "  array[1] = null;\n"
            + "  array[2] = Number.NaN;\n"
            + "  array[3] = Number.POSITIVE_INFINITY;\n"
            + "  array[4] = Number.NEGATIVE_INFINITY;\n"
            + "  array[5] = 4;\n"
            + "  log(array[0]);\n"

            + "  var nativeArray = new Float64Array(array);\n"
            + "  log(nativeArray.length);\n"
            + "  log(nativeArray[0]);\n"
            + "  log(nativeArray[1]);\n"
            + "  log(nativeArray[2]);\n"
            + "  log(nativeArray[3]);\n"
            + "  log(nativeArray[4]);\n"
            + "  log(nativeArray[5]);\n"
            + "  log(nativeArray[6]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"NaN", "1", "0", "17"})
    public void specialValueInArray() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var array = [];\n"
            + "  array[0] = NaN;\n"
            + "  array[1] = true;\n"
            + "  array[2] = false;\n"
            + "  array[3] = '17';\n"
            + "  var nativeArray = new Float64Array(array);\n"
            + "  log(nativeArray[0]);\n"
            + "  log(nativeArray[1]);\n"
            + "  log(nativeArray[2]);\n"
            + "  log(nativeArray[3]);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "0",
            IE = "exception")
    @NotYetImplemented(IE)
    public void nullConstructor() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var array = new Float64Array(null);\n"
            + "    log(array.length);\n"
            + "  } catch(e) {\n"
            + "    log('exception');\n"
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
    @Alerts(DEFAULT = {"", "0", "1", "1,3", "1,3,4,7,11,0,123"},
            IE = {"[object Float64Array]", "[object Float64Array]",
                  "[object Float64Array]", "[object Float64Array]",
                  "[object Float64Array]"})
    @NotYetImplemented(IE)
    public void asString() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var array = new Float64Array(0);\n"
            + "  log(array.toString());\n"

            + "  array = new Float64Array(1);\n"
            + "  log(array.toString());\n"

            + "  array = new Float64Array([1]);\n"
            + "  log(array.toString());\n"

            + "  array = new Float64Array([1,3]);\n"
            + "  log(array.toString());\n"

            + "  array = new Float64Array([1,3,4,7,11,0,123]);\n"
            + "  log(array.toString());\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Float64Array",
            IE = "undefined")
    @NotYetImplemented(IE)
    public void name() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Float64Array.name);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
