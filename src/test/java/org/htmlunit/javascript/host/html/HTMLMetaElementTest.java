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
package org.htmlunit.javascript.host.html;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link HTMLMetaElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HTMLMetaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"undefined", "text/html; charset=utf-8", HttpHeader.CONTENT_TYPE, "", "", "undefined", ""})
    public void name() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <meta http-equiv='Content-Type' content='text/html; charset=utf-8'>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var meta = document.getElementsByTagName('meta')[0];\n"
            + "        log(meta.charset);\n"
            + "        log(meta.content);\n"
            + "        log(meta.httpEquiv);\n"
            + "        log(meta.name);\n"
            + "        log(meta.scheme);\n"
            + "        log(meta.url);\n"
            + "        log(meta.media);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("only screen and (max-width: 600px)")
    public void media() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <meta http-equiv='Content-Type' media='only screen and (max-width: 600px)'>\n"
            + "    <script>\n"
            + LOG_TITLE_FUNCTION
            + "      function test() {\n"
            + "        var meta = document.getElementsByTagName('meta')[0];\n"
            + "        log(meta.media);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }
}
