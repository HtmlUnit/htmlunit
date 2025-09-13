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
package org.htmlunit.javascript.host.css;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CSS}.
 *
 * @author Ronald Brill
 */
public class CSSTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object CSS]", "undefined"})
    public void global() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<style>@charset 'UTF-8';</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(CSS);"
            + "    log(CSS.prototype);"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("TypeError")
    public void constructor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<style>@charset 'UTF-8';</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    var o = Object.create(CSS.prototype);\n"
            + "    log(o);"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void supports() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(CSS.supports('display', 'flex'));"
            + "    log(CSS.supports('display', 'grid'));"
            + "    log(CSS.supports('color', 'red'));"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true"})
    public void supportsCondition() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(CSS.supports('display: flex'));"
            + "    log(CSS.supports('color: red'));"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "false"})
    @HtmlUnitNYI(CHROME = {"true", "true"},
            EDGE = {"true", "true"},
            FF = {"true", "true"},
            FF_ESR = {"true", "true"})
    public void supportsSelector() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(CSS.supports('selector(div)'));"
            + "    log(CSS.supports('selector(div, span)'));"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"\\.foo\\#bar", "\\(\\)\\[\\]\\{\\}", "--a", "\\30\\s", "\ufffd"})
    public void escape() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  try {\n"
            + "    log(CSS.escape('.foo#bar'));\n"
            + "    log(CSS.escape('()[]{}'));\n"
            + "    log(CSS.escape('--a'));\n"
            + "    log(CSS.escape(0));\n"
            + "    log(CSS.escape('\0'));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"\\1\\s", "\\2\\s", "\\1e\\s", "\\1f\\s"})
    public void escape0001_001F() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  try {\n"
            + "    log(CSS.escape('\u0001'));\n"
            + "    log(CSS.escape('\u0002'));\n"
            + "    log(CSS.escape('\u001e'));\n"
            + "    log(CSS.escape('\u001f'));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("\\7f\\s")
    public void escape007F() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  try {\n"
            + "    log(CSS.escape('\u007f'));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"\\30\\s", "\\31\\s", "\\37\\s", "\\39\\sab", "\\39\\s"})
    public void escapeStart0030_0039() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  try {\n"
            + "    log(CSS.escape('0'));\n"
            + "    log(CSS.escape('1'));\n"
            + "    log(CSS.escape('7'));\n"
            + "    log(CSS.escape('9ab'));\n"
            + "    log(CSS.escape('\u0039'));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"-\\30\\s", "-\\31\\s", "-\\37\\s", "-\\39\\sab", "-\\39\\s"})
    public void escapeStartMinus0030_0039() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  try {\n"
            + "    log(CSS.escape('-0'));\n"
            + "    log(CSS.escape('-1'));\n"
            + "    log(CSS.escape('-7'));\n"
            + "    log(CSS.escape('-9ab'));\n"
            + "    log(CSS.escape('-\u0039'));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("\\-")
    public void escapeStartMinus() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  try {\n"
            + "    log(CSS.escape('-'));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"€", "-_", "\\\\s0123456789", "ABC\\\\sUVWXYZ", "abcd\\\\sxyz"})
    public void escapePass() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  try {\n"
            + "    log(CSS.escape('\u0080'));\n"
            + "    log(CSS.escape('-_'));\n"
            + "    log(CSS.escape(' 0123456789'));\n"
            + "    log(CSS.escape('ABC UVWXYZ'));\n"
            + "    log(CSS.escape('abcd xyz'));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    // If the character is not handled by one of the above rules
    // and is greater than or equal to U+0080, is "-" (U+002D) or "_" (U+005F),
    // or is in one of the ranges [0-9] (U+0030 to U+0039),
    // [A-Z] (U+0041 to U+005A), or [a-z] (U+0061 to U+007A),
    // then the character itself.


}
