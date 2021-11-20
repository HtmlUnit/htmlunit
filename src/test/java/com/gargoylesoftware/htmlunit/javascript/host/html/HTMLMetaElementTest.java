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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.HttpHeader;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Unit tests for {@link HTMLMetaElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLMetaElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "text/html; charset=utf-8", HttpHeader.CONTENT_TYPE, "", "", "undefined", ""},
            FF = {"undefined", "text/html; charset=utf-8", HttpHeader.CONTENT_TYPE, "", "", "undefined", "undefined"},
            FF_ESR = {"undefined", "text/html; charset=utf-8", HttpHeader.CONTENT_TYPE, "", "", "undefined", "undefined"},
            IE = {"", "text/html; charset=utf-8", HttpHeader.CONTENT_TYPE, "", "", "", "undefined"})
    public void name() throws Exception {
        final String html =
            "<html>\n"
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
    @Alerts(DEFAULT = "only screen and (max-width: 600px)",
            FF = "undefined",
            FF_ESR = "undefined",
            IE = "undefined")
    public void media() throws Exception {
        final String html =
            "<html>\n"
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
