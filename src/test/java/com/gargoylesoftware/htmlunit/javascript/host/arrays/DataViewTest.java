/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link DataView}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class DataViewTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "22", "3.1415927410125732" },
            IE8 = "exception")
    public void arrayConstruction() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var buffer = new ArrayBuffer(12);\n"
            + "    var x = new DataView(buffer);\n"
            + "    x.setInt8(0, 22);\n"
            + "    x.setFloat32(1, Math.PI);\n"
            + "    alert(x.getInt8(0));\n"
            + "    alert(x.getFloat32(1));\n"
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
    @Alerts(DEFAULT = { "570119236", "2.1426990032196045", "0", "0", "0", "0", "64", "9",
                            "33", "-5", "84", "68", "45", "24" },
            IE8 = "exception")
    public void endian() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var array = new DataView(new ArrayBuffer(12), 4)\n"
            + "    array.setFloat64(0, Math.PI);\n"
            + "    alert(array.getInt32(2), true);\n"
            + "    alert(array.getFloat32(0), false);\n"
            + "    var array2 = new Int8Array(array.buffer);\n"
            + "    for (var i = 0; i < array2.length; i++)\n"
            + "      alert(array2[i]);\n"
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
    @Alerts(DEFAULT = { "1234", "0", "4", "-46", "0", "0", "0" },
            IE8 = "exception")
    public void uint16() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var array = new DataView(new ArrayBuffer(6), 0)\n"
            + "    array.setUint16(1, 1234);\n"
            + "    alert(array.getUint16(1));\n"
            + "    var array2 = new Int8Array(array.buffer);\n"
            + "    for (var i = 0; i < array2.length; i++)\n"
            + "      alert(array2[i]);\n"
            + "  } catch(e) {\n"
            + "    alert('exception');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
