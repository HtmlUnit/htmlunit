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
 * Tests for {@link FontFace}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class FontFaceTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "function\\sFontFace()\\s{\\n\\s\\s\\s\\s[native\\scode]\\n}",
            CHROME = "function\\sFontFace()\\s{\\s[native\\scode]\\s}",
            EDGE = "function\\sFontFace()\\s{\\s[native\\scode]\\s}",
            IE = "undefined")
    public void window() throws Exception {
        final String html
            = "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  log(window.FontFace);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
