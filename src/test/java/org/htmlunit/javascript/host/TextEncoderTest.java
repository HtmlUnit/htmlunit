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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TextEncoder}.
 *
 * @author Ronald Brill
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/TextEncoder">TextEncoder() - Web APIs | MDN</a>
 */
public class TextEncoderTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("utf-8")
    public void encoding() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      if (typeof TextEncoder === 'undefined') {\n"
            + "        log('no TextEncoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var enc = new TextEncoder();\n"
            + "      log(enc.encoding);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"0", "8", "72", "116"})
    public void encode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      if (typeof TextEncoder === 'undefined') {\n"
            + "        log('no TextEncoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var enc = new TextEncoder();\n"
            + "      var encoded = enc.encode('');\n"
            + "      log(encoded.length);\n"

            + "      encoded = enc.encode('HtmlUnit');\n"
            + "      log(encoded.length);\n"
            + "      log(encoded[0]);\n"
            + "      log(encoded[encoded.length - 1]);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"0", "0", "4"})
    public void encode2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function doTest() {\n"
            + "      if (typeof TextEncoder === 'undefined') {\n"
            + "        log('no TextEncoder');\n"
            + "        return;\n"
            + "      };\n"
            + "      var enc = new TextEncoder();\n"
            + "      var encoded = enc.encode();\n"
            + "      log(encoded.length);\n"

            + "      var encoded = enc.encode(undefined);\n"
            + "      log(encoded.length);\n"

            + "      var encoded = enc.encode(null);\n"
            + "      log(encoded.length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
