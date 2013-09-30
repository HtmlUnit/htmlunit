/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
 * Tests for {@link Int8Array}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class Int8ArrayTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object ArrayBuffer]", "5", "0" },
            FF3_6 = "exception",
            IE6 = "exception",
            IE8 = "exception")
    public void buffer() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var array = new Int8Array(5);\n"
            + "    alert(array.buffer);\n"
            + "    alert(array.byteLength);\n"
            + "    alert(array.byteOffset);\n"
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
    @Alerts(DEFAULT = { "17", "-45", "2" },
            FF3_6 = "exception",
            IE6 = "exception",
            IE8 = "exception")
    public void arrayConstructor() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var array = new Int8Array([17, -45.3]);\n"
            + "    alert(array[0]);\n"
            + "    alert(array[1]);\n"
            + "    alert(array.length);\n"
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
    @Alerts(DEFAULT = { "2", "-45", "52" },
            FF3_6 = "exception",
            IE6 = "exception",
            IE8 = "exception")
    public void bufferConstructor() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var array = new Int8Array([17, -45.3, 52, 123]);\n"
            + "    var array2 = new Int8Array(array.buffer, 1, 2);\n"
            + "    alert(array2.length);\n"
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
    @Alerts(DEFAULT = "1",
            FF3_6 = "exception",
            IE6 = "exception",
            IE8 = "exception")
    public void constant() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    alert(Int8Array.BYTES_PER_ELEMENT);\n"
            + "  } catch(e) {\n"
            + "    alert('exception');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
