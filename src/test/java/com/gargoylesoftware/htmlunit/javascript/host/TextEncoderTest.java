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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link TextEncoder}.
 *
 * @author Ronald Brill
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/TextEncoder">TextEncoder() - Web APIs | MDN</a>
 */
@RunWith(BrowserRunner.class)
public class TextEncoderTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "utf-8",
            IE = "no TextEncoder")
    public void encoding() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      if (typeof TextEncoder === 'undefined') {\n"
            + "        alert('no TextEncoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var enc = new TextEncoder();\n"
            + "      alert(enc.encoding);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"0", "8", "72", "116"},
            IE = "no TextEncoder")
    public void encode() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      if (typeof TextEncoder === 'undefined') {\n"
            + "        alert('no TextEncoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var enc = new TextEncoder();\n"
            + "      var encoded = enc.encode('');\n"
            + "      alert(encoded.length);\n"

            + "      encoded = enc.encode('HtmlUnit');\n"
            + "      alert(encoded.length);\n"
            + "      alert(encoded[0]);\n"
            + "      alert(encoded[encoded.length - 1]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"0", "0", "4"},
            IE = "no TextEncoder")
    public void encode2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function doTest() {\n"
            + "      if (typeof TextEncoder === 'undefined') {\n"
            + "        alert('no TextEncoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var enc = new TextEncoder();\n"
            + "      var encoded = enc.encode();\n"
            + "      alert(encoded.length);\n"

            + "      var encoded = enc.encode(undefined);\n"
            + "      alert(encoded.length);\n"

            + "      var encoded = enc.encode(null);\n"
            + "      alert(encoded.length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
