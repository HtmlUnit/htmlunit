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
 * Tests for {@link ArrayBuffer}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ArrayBufferTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "exception", FF3_6 = "exception", DEFAULT = "5")
    public void byteLength() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var buff = new ArrayBuffer(5);\n"
            + "    alert(buff.byteLength);\n"
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
    @Alerts(IE = "exception", FF3_6 = "exception", DEFAULT = { "1234", "1234", "6789", "1234" })
    public void slice() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var buffer = new ArrayBuffer(12);\n"
            + "    var x = new Int32Array(buffer);\n"
            + "    x[1] = 1234;\n"
            + "    var slice = buffer.slice(4);\n"
            + "    var y = new Int32Array(slice);\n"
            + "    alert(x[1]);\n"
            + "    alert(y[0]);\n"
            + "    x[1] = 6789;\n"
            + "    alert(x[1]);\n"
            + "    alert(y[0]);\n"
            + "  } catch(e) {\n"
            + "    alert('exception');\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
