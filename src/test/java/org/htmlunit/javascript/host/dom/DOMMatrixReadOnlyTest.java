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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DOMMatrixReadOnly}.
 *
 * @author Ronald Brill
 */
public class DOMMatrixReadOnlyTest extends WebDriverTestCase {

    public static final String DUMP_FUNCTION = "function r(num) { return Math.round(num * 1000) / 1000 }\n"
            + "function dump(m) {\n"
            + "  log('[' + r(m.a) + ', ' + r(m.b) + ', ' + r(m.c)"
                        + " + ', ' + r(m.d) + ', ' + r(m.e) + ', ' + r(m.f) + ']');\n"
            + "  log('1[' + r(m.m11) + ', ' + r(m.m12) + ', ' + r(m.m13) + ', ' + r(m.m14) + ']');\n"
            + "  log('2[' + r(m.m21) + ', ' + r(m.m22) + ', ' + r(m.m23) + ', ' + r(m.m24) + ']');\n"
            + "  log('3[' + r(m.m31) + ', ' + r(m.m32) + ', ' + r(m.m33) + ', ' + r(m.m34) + ']');\n"
            + "  log('4[' + r(m.m41) + ', ' + r(m.m42) + ', ' + r(m.m43) + ', ' + r(m.m44) + ']');\n"
            + "  log(m.is2D);\n"
            + "}\n";

    public static final String DUMP_2D_FUNCTION = "function dump(m) {\n"
            + "  log('[' + m.a + ', ' + m.b + ', ' + m.c + ', ' + m.d + ', ' + m.e + ', ' + m.f + ']');\n"
            + "  log(m.is2D);\n"
            + "}\n";

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructor() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "dump(m);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[6, 5, 4, 3, 2, 1]",
             "1[6, 5, 0, 0]",
             "2[4, 3, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[2, 1, 0, 1]",
             "true"})
    public void contructor6Numbers() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([6, 5, 4, 3, 2, 1]);\n"
                + "dump(m);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[6, 1, 0, NaN, 2, NaN]",
             "true"})
    @HtmlUnitNYI(CHROME = {"[6, 1, 0, 0, 2, NaN]", "true"},
            EDGE = {"[6, 1, 0, 0, 2, NaN]", "true"},
            FF = {"[6, 1, 0, 0, 2, NaN]", "true"},
            FF_ESR = {"[6, 1, 0, 0, 2, NaN]", "true"})
    public void contructor6Mixed() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly([6, true, null, undefined, '2', 'eins']);\n"
                + "dump(m);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("TypeError")
    public void contructor5Numbers() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly([5, 4, 3, 2, 1]);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("TypeError")
    public void contructor7Numbers() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly([7, 6, 5, 4, 3, 2, 1]);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[16, 15, 12, 11, 4, 3]",
             "1[16, 15, 14, 13]",
             "2[12, 11, 10, 9]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void contructor16Numbers() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "dump(m);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("TypeError")
    public void contructor15Numbers() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly([15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("TypeError")
    public void contructor17Numbers() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly([17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorSingleNumber() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly(7);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorNull() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly(null);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorUndefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly(undefined);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("TypeError")
    public void contructorEmptyArray() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly([]);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[-1, 0, 0, 1, 0, 0]",
             "true"})
    public void flipX_identity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let flipped = m.flipX();\n"
                + "dump(flipped);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, -1, 0, 0]",
             "true"})
    public void flipY_identity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let flipped = m.flipY();\n"
                + "dump(flipped);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[NaN, 0, 0, 1, 0, 0]", "true"},
            FF = {"[NaN, 0, NaN, 1, NaN, 0]", "true"},
            FF_ESR = {"[NaN, 0, NaN, 1, NaN, 0]", "true"})
    @HtmlUnitNYI(FF = {"[NaN, 0, 0, 1, 0, 0]", "true"},
            FF_ESR = {"[NaN, 0, 0, 1, 0, 0]", "true"})
    public void flipX_NaN() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly([NaN, 0, 0, 1, 0, 0]);\n"
                + "let flipped = m.flipX();\n"
                + "dump(flipped);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[1, 0, 0, -Infinity, 0, 0]", "true"},
            FF = {"[1, NaN, 0, -Infinity, 0, NaN]", "true"},
            FF_ESR = {"[1, NaN, 0, -Infinity, 0, NaN]", "true"})
    @HtmlUnitNYI(FF = {"[1, 0, 0, -Infinity, 0, 0]", "true"},
            FF_ESR = {"[1, 0, 0, -Infinity, 0, 0]", "true"})
    public void flipY_Infinity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 0, 0, Infinity, 0, 0]);\n"
                + "let flipped = m.flipY();\n"
                + "dump(flipped);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[-2, -3, 4, 5, 6, 7]",
             "true",
             "[2, 3, 4, 5, 6, 7]",
             "true"})
    public void flipX_6element2_general() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly([2, 3, 4, 5, 6, 7]);\n"
                + "let flipped = m.flipX();\n"
                + "log(flipped instanceof DOMMatrixReadOnly);\n"
                + "log(flipped instanceof DOMMatrix);\n"
                + "dump(flipped);\n"
                + "dump(m);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[-16, -15, 12, 11, 4, 3]",
             "1[-16, -15, -14, -13]",
             "2[12, 11, 10, 9]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false",
             "[16, 15, 12, 11, 4, 3]",
             "1[16, 15, 14, 13]",
             "2[12, 11, 10, 9]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void flipX_16elements_general() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let flipped = m.flipX();\n"
                + "log(flipped instanceof DOMMatrixReadOnly);\n"
                + "log(flipped instanceof DOMMatrix);\n"
                + "dump(flipped);\n"
                + "dump(m);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[2, 3, -4, -5, 6, 7]",
             "1[2, 3, 0, 0]",
             "2[-4, -5, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[6, 7, 0, 1]",
             "true",
             "[2, 3, 4, 5, 6, 7]",
             "1[2, 3, 0, 0]",
             "2[4, 5, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[6, 7, 0, 1]",
             "true"})
    public void flipY_6elements_general() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([2, 3, 4, 5, 6, 7]);\n"
                + "let flipped = m.flipY();\n"
                + "log(flipped instanceof DOMMatrixReadOnly);\n"
                + "log(flipped instanceof DOMMatrix);\n"
                + "dump(flipped);\n"
                + "dump(m);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[16, 15, -12, -11, 4, 3]",
             "1[16, 15, 14, 13]",
             "2[-12, -11, -10, -9]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false",
             "[16, 15, 12, 11, 4, 3]",
             "1[16, 15, 14, 13]",
             "2[12, 11, 10, 9]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void flipY_16elements_general() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let flipped = m.flipY();\n"
                + "log(flipped instanceof DOMMatrixReadOnly);\n"
                + "log(flipped instanceof DOMMatrix);\n"
                + "dump(flipped);\n"
                + "dump(m);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[-16, -15, 12, 11, 4, 3]",
             "1[-16, -15, -14, -13]",
             "2[12, 11, 10, 9]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void flipX_3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let flipped = m.flipX();\n"
                + "dump(flipped);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[16, 15, -12, -11, 4, 3]",
             "1[16, 15, 14, 13]",
             "2[-12, -11, -10, -9]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void flipY_3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let flipped = m.flipY();\n"
                + "dump(flipped);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]", "true"})
    public void inverse_identity2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly();"
                + "let inv = m.inverse();"
                + "dump(inv);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[0.25, 0, 0, 0.2, -2.5, -2.6]",
             "1[0.25, 0, 0, 0]",
             "2[0, 0.2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[-2.5, -2.6, 0, 1]",
             "true",
             "[4, 0, 0, 5, 10, 13]",
             "1[4, 0, 0, 0]",
             "2[0, 5, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 13, 0, 1]",
             "true"})
    public void inverse_general2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([4, 0, 0, 5, 10, 13]);"
                + "let inv = m.inverse();"
                + "log(inv instanceof DOMMatrixReadOnly);\n"
                + "log(inv instanceof DOMMatrix);\n"
                + "dump(inv);"
                + "dump(m);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[NaN, NaN, NaN, NaN, NaN, NaN]", "false"})
    public void inverse_singular2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly([0, 0, 0, 0, 0, 0]);"
                + "  let inv = m.inverse();"
                + "  dump(inv);"
                + "} catch(e) { logEx(e); }"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void inverse_identity3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1]);"
                + "let inv = m.inverse();"
                + "dump(inv);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[NaN, NaN, NaN, NaN, NaN, NaN]",
             "1[NaN, NaN, NaN, NaN]",
             "2[NaN, NaN, NaN, NaN]",
             "3[NaN, NaN, NaN, NaN]",
             "4[NaN, NaN, NaN, NaN]",
             "false"})
    public void inverse_singular3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly([0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]);"
                + "  let inv = m.inverse();"
                + "  dump(inv);"
                + "} catch(e) { logEx(e); }"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[-0.066, 0.115, 0.295, -0.016, 0, 0]",
             "1[-0.066, 0.115, 0.115, -0.459]",
             "2[0.295, -0.016, -0.516, 0.066]",
             "3[0.148, -0.008, -0.008, 0.033]",
             "4[0, 0, 0, 1]",
             "false",
             "[0.5, 0, 9, 2, 0, 0]",
             "1[0.5, 0, 7, 0]",
             "2[9, 2, 0, 4]",
             "3[0, -2, 4, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void inverse_general3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([0.5,0,7,0,9,2,0,4,0,-2,4,0,0,0,0,1]);"
                + "let inv = m.inverse();"
                + "log(inv instanceof DOMMatrixReadOnly);\n"
                + "log(inv instanceof DOMMatrix);\n"
                + "dump(inv);"
                + "dump(m);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void multiplyIdentity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly();\n"
                + "let m2 = new DOMMatrixReadOnly();\n"
                + "let result = m1.multiply(m2);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[21, 32, 13, 20, 10, 14]",
             "1[21, 32, 0, 0]",
             "2[13, 20, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 14, 0, 1]",
             "true"})
    public void multiply2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let m2 = new DOMMatrixReadOnly([6, 5, 4, 3, 2, 1]);\n"
                + "let result = m1.multiply(m2);\n"
                + "log(result instanceof DOMMatrixReadOnly);\n"
                + "log(result instanceof DOMMatrix);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[386, 444, 274, 316, 50, 60]",
             "1[386, 444, 502, 560]",
             "2[274, 316, 358, 400]",
             "3[162, 188, 214, 240]",
             "4[50, 60, 70, 80]",
             "false"})
    public void multiply3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "let m2 = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let result = m1.multiply(m2);\n"
                + "log(result instanceof DOMMatrixReadOnly);\n"
                + "log(result instanceof DOMMatrix);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[-392, -90, -736, 116, -82, 16]",
             "1[-392, -90, -173, 312]",
             "2[-736, 116, 179, 16]",
             "3[674, 90, 309, 48]",
             "4[-82, 16, 27, 56]",
             "false"})
    public void multiply3D_2() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly([11, 2, 13, 4, -55, 1, -7, 8, 29, 0, 3.5, 12, 3, 9, 15, 0]);\n"
                + "let m2 = new DOMMatrixReadOnly([6, 15, 14, -13, 12, 11, -10, 9, 8, -7, 6, 9, 2, 3, 2, 1]);\n"
                + "let result = m1.multiply(m2);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 5, 6]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void multiplyTranslation() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly();\n"
                + "let m2 = new DOMMatrixReadOnly([1, 0, 0, 1, 5, 6]);\n"
                + "let result = m1.multiply(m2);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[2, 0, 0, 2, 0, 0]",
             "1[2, 0, 0, 0]",
             "2[0, 2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void multiplyScale() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly([2, 0, 0, 2, 0, 0]);\n"
                + "let m2 = new DOMMatrixReadOnly();\n"
                + "let result = m1.multiply(m2);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[0, 0, 0, 0, 0, 0]",
             "1[0, 0, 0, 0]",
             "2[0, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void multiplyZero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly([0, 0, 0, 0, 0, 0]);\n"
                + "let m2 = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let result = m1.multiply(m2);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[21, 32, 13, 20, 10, 14]",
             "1[21, 32, 0, 0]",
             "2[13, 20, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 14, 0, 1]",
             "false"})
    public void multiply2DWith3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let m2 = new DOMMatrixReadOnly([6, 5, 0, 0, 4, 3, 0, 0, 0, 0, 1, 0, 2, 1, 0, 1]);\n"
                + "let result = m1.multiply(m2);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void multiplyNullArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly();\n"
                + "  let result = m1.multiply(null);\n"
                + "  dump(result);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void multiplyUndefinedArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly();\n"
                + "  let result = m1.multiply(undefined);\n"
                + "  dump(result);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("TypeError")
    public void multiplyStringArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly();\n"
                + "  let result = m1.multiply('invalid');\n"
                + "  dump(result);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("TypeError")
    public void multiplyNumberArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly();\n"
                + "  let result = m1.multiply(42);\n"
                + "  dump(result);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void multiplyNoArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly();\n"
                + "  let result = m1.multiply();\n"
                + "  dump(result);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[NaN, 2, NaN, 4, NaN, 6]",
             "1[NaN, 2, 0, 0]",
             "2[NaN, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[NaN, 6, 0, 1]",
             "true"})
    public void multiplyWithNaN() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly([NaN, 0, 0, 1, 0, 0]);\n"
                + "let m2 = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let result = m1.multiply(m2);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[Infinity, 2, Infinity, 4, Infinity, 6]",
             "1[Infinity, 2, 0, 0]",
             "2[Infinity, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[Infinity, 6, 0, 1]",
             "true"})
    public void multiplyWithInfinity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly([Infinity, 0, 0, 1, 0, 0]);\n"
                + "let m2 = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let result = m1.multiply(m2);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void multiplyAssociativity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m1 = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let identity = new DOMMatrixReadOnly();\n"
                + "let result = m1.multiply(identity);\n"
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotate_identity_zero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let rotated = m.rotate(0);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[0, 1, -1, 0, 0, 0]",
             "1[0, 1, 0, 0]",
             "2[-1, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotate_identity_90degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let rotated = m.rotate(90);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[-1, 0, 0, -1, 0, 0]",
             "1[-1, 0, 0, 0]",
             "2[0, -1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotate_identity_180degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let rotated = m.rotate(180);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[0, -1, 1, 0, 0, 0]",
             "1[0, -1, 0, 0]",
             "2[1, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotate_identity_270degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let rotated = m.rotate(270);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotate_identity_360degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let rotated = m.rotate(360);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[0.707, 0.707, -0.707, 0.707, 0, 0]",
             "1[0.707, 0.707, 0, 0]",
             "2[-0.707, 0.707, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotate_identity_45degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let rotated = m.rotate(45);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[0, 1, -1, 0, 0, 0]",
             "1[0, 1, 0, 0]",
             "2[-1, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotate_identity_negative_270degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let rotated = m.rotate(-270);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[3, 4, -1, -2, 5, 6]",
             "1[3, 4, 0, 0]",
             "2[-1, -2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true",
             "[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotate_general_90degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let rotated = m.rotate(90);\n"
                + "log(rotated instanceof DOMMatrixReadOnly);\n"
                + "log(rotated instanceof DOMMatrix);\n"
                + "dump(rotated);\n"
                + "dump(m);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[NaN, NaN, NaN, NaN, 5, 6]",
                       "1[NaN, NaN, NaN, NaN]",
                       "2[NaN, NaN, NaN, NaN]",
                       "3[0, 0, 1, 0]",
                       "4[5, 6, 0, 1]",
                       "true"},
            FF = {"[NaN, NaN, NaN, NaN, 5, 6]",
                  "1[NaN, NaN, 0, 0]",
                  "2[NaN, NaN, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[5, 6, 0, 1]",
                  "true"},
            FF_ESR = {"[NaN, NaN, NaN, NaN, 5, 6]",
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[5, 6, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            CHROME = {"[NaN, NaN, NaN, NaN, 5, 6]",
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[5, 6, 0, 1]",
                      "true"},
            EDGE = {"[NaN, NaN, NaN, NaN, 5, 6]",
                    "1[NaN, NaN, 0, 0]",
                    "2[NaN, NaN, 0, 0]",
                    "3[0, 0, 1, 0]",
                    "4[5, 6, 0, 1]",
                    "true"})
    public void rotate_with_NaN_angle() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let rotated = m.rotate(NaN);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[NaN, NaN, NaN, NaN, 5, 6]",
                       "1[NaN, NaN, NaN, NaN]",
                       "2[NaN, NaN, NaN, NaN]",
                       "3[0, 0, 1, 0]",
                       "4[5, 6, 0, 1]",
                       "true"},
            FF = {"[NaN, NaN, NaN, NaN, 5, 6]",
                  "1[NaN, NaN, 0, 0]",
                  "2[NaN, NaN, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[5, 6, 0, 1]",
                  "true"},
            FF_ESR = {"[NaN, NaN, NaN, NaN, 5, 6]",
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[5, 6, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            CHROME = {"[NaN, NaN, NaN, NaN, 5, 6]",
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[5, 6, 0, 1]",
                      "true"},
            EDGE = {"[NaN, NaN, NaN, NaN, 5, 6]",
                    "1[NaN, NaN, 0, 0]",
                    "2[NaN, NaN, 0, 0]",
                    "3[0, 0, 1, 0]",
                    "4[5, 6, 0, 1]",
                    "true"})
    public void rotate_with_Infinity_angle() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let rotated = m.rotate(Infinity);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[NaN, 4.243, NaN, 1.414, 5, 6]",
                       "1[NaN, 4.243, 0, 0]",
                       "2[NaN, 1.414, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[5, 6, 0, 1]",
                       "true"},
            FF = {"[NaN, 4.243, NaN, 1.414, NaN, 6]",
                  "1[NaN, 4.243, 0, 0]",
                  "2[NaN, 1.414, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[NaN, 6, 0, 1]",
                  "true"},
            FF_ESR = {"[NaN, 4.243, NaN, 1.414, NaN, 6]",
                      "1[NaN, 4.243, 0, 0]",
                      "2[NaN, 1.414, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[NaN, 6, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            FF = {"[NaN, 4.243, NaN, 1.414, 5, 6]",
                  "1[NaN, 4.243, 0, 0]",
                  "2[NaN, 1.414, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[5, 6, 0, 1]",
                  "true"},
            FF_ESR = {"[NaN, 4.243, NaN, 1.414, 5, 6]",
                      "1[NaN, 4.243, 0, 0]",
                      "2[NaN, 1.414, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[5, 6, 0, 1]",
                      "true"})
    public void rotate_matrix_with_NaN_values() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([NaN, 2, 3, 4, 5, 6]);\n"
                + "let rotated = m.rotate(45);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[Infinity, 4.243, -Infinity, 1.414, 5, 6]",
                       "1[Infinity, 4.243, 0, 0]",
                       "2[-Infinity, 1.414, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[5, 6, 0, 1]",
                       "true"},
            FF = {"[Infinity, 4.243, -Infinity, 1.414, NaN, 6]",
                  "1[Infinity, 4.243, 0, 0]",
                  "2[-Infinity, 1.414, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[NaN, 6, 0, 1]",
                  "true"},
            FF_ESR = {"[Infinity, 4.243, -Infinity, 1.414, NaN, 6]",
                      "1[Infinity, 4.243, 0, 0]",
                      "2[-Infinity, 1.414, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[NaN, 6, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            FF = {"[Infinity, 4.243, -Infinity, 1.414, 5, 6]",
                  "1[Infinity, 4.243, 0, 0]",
                  "2[-Infinity, 1.414, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[5, 6, 0, 1]",
                  "true"},
            FF_ESR = {"[Infinity, 4.243, -Infinity, 1.414, 5, 6]",
                      "1[Infinity, 4.243, 0, 0]",
                      "2[-Infinity, 1.414, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[5, 6, 0, 1]",
                      "true"})
    public void rotate_matrix_with_Infinity_values() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([Infinity, 2, 3, 4, 5, 6]);\n"
                + "let rotated = m.rotate(45);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[16, 15, 12, 11, 4, 3]",
             "1[16, 15, 14, 13]",
             "2[12, 11, 10, 9]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void rotate_3D_zero_degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let rotated = m.rotate(0);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[12, 11, -16, -15, 4, 3]",
             "1[12, 11, 10, 9]",
             "2[-16, -15, -14, -13]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void rotate_3D_90_degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let rotated = m.rotate(90);\n"
                + "log(rotated instanceof DOMMatrixReadOnly);\n"
                + "log(rotated instanceof DOMMatrix);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[-16, -15, -12, -11, 4, 3]",
             "1[-16, -15, -14, -13]",
             "2[-12, -11, -10, -9]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void rotate_3D_180_degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let rotated = m.rotate(180);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[-12, -11, 16, 15, 4, 3]",
             "1[-12, -11, -10, -9]",
             "2[16, 15, 14, 13]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void rotate_3D_270_degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let rotated = m.rotate(270);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[19.799, 18.385, -2.828, -2.828, 4, 3]",
             "1[19.799, 18.385, 16.971, 15.556]",
             "2[-2.828, -2.828, -2.828, -2.828]",
             "3[8, 7, 6, 5]",
             "4[4, 3, 2, 1]",
             "false"})
    public void rotate_3D_45_degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
                + "let rotated = m.rotate(45);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotate_no_argument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let rotated = m.rotate();\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotate_null_argument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let rotated = m.rotate(null);\n"
                + "dump(rotated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotateAxisAngle_identity_zero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, 0);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotateAxisAngle_identity_360() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, 360);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[0, 1, -1, 0, 0, 0]",
                       "1[0, 1, 0, 0]",
                       "2[-1, 0, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[0, 0, 0, 1]",
                       "true"},
            FF_ESR = {"[0, 1, -1, 0, 0, 0]",
                      "1[0, 1, 0, 0]",
                      "2[-1, 0, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "false"})
    @HtmlUnitNYI(
            FF_ESR = {"[0, 1, -1, 0, 0, 0]",
                      "1[0, 1, 0, 0]",
                      "2[-1, 0, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"})
    public void rotateAxisAngle_identity_90_z() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, 90);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 0, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 0, 1, 0]",
             "3[0, -1, 0, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void rotateAxisAngle_identity_90_x() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();"
                + "let rotated = m.rotateAxisAngle(1, 0, 0, 90);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 0, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 0, -1, 0]",
             "3[0, 1, 0, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void rotateAxisAngle_identity_270_x() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();"
                + "let rotated = m.rotateAxisAngle(1, 0, 0, 270);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[7, 6, 5, 4, 3, 2]",
             "1[7, 6, 0, 0]",
             "2[5, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[3, 2, 0, 1]",
             "true"})
    public void rotateAxisAngle_6_zero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([7, 6, 5, 4, 3, 2]);"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, 0);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[7, 6, 5, 4, 3, 2]",
             "1[7, 6, 0, 0]",
             "2[5, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[3, 2, 0, 1]",
             "true"})
    public void rotateAxisAngle_6_360() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([7, 6, 5, 4, 3, 2]);"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, 360);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[5, 4, -7, -6, 3, 2]",
                       "1[5, 4, 0, 0]",
                       "2[-7, -6, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[3, 2, 0, 1]",
                       "true"},
            FF_ESR = {"[5, 4, -7, -6, 3, 2]",
                      "1[5, 4, 0, 0]",
                      "2[-7, -6, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[3, 2, 0, 1]",
                      "false"})
    @HtmlUnitNYI(FF_ESR = {"[5, 4, -7, -6, 3, 2]",
                           "1[5, 4, 0, 0]",
                           "2[-7, -6, 0, 0]",
                           "3[0, 0, 1, 0]",
                           "4[3, 2, 0, 1]",
                           "true"})
    public void rotateAxisAngle_6_90_z() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([7, 6, 5, 4, 3, 2]);"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, 90);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[7, 6, 0, 0, 3, 2]",
             "1[7, 6, 0, 0]",
             "2[0, 0, 1, 0]",
             "3[-5, -4, 0, 0]",
             "4[3, 2, 0, 1]",
             "false"})
    public void rotateAxisAngle_6_90_x() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([7, 6, 5, 4, 3, 2]);"
                + "let rotated = m.rotateAxisAngle(1, 0, 0, 90);"
                + "log(rotated instanceof DOMMatrixReadOnly);\n"
                + "log(rotated instanceof DOMMatrix);\n"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[7, 6, 0, 0, 3, 2]",
             "1[7, 6, 0, 0]",
             "2[0, 0, -1, 0]",
             "3[5, 4, 0, 0]",
             "4[3, 2, 0, 1]",
             "false"})
    public void rotateAxisAngle_6_270_x() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([7, 6, 5, 4, 3, 2]);"
                + "let rotated = m.rotateAxisAngle(1, 0, 0, 270);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[2, 3, 6, 7, 14, 15]",
             "1[2, 3, 4, 5]",
             "2[6, 7, 8, 9]",
             "3[10, 11, 12, 13]",
             "4[14, 15, 16, 17]",
             "false"})
    public void rotateAxisAngle_16_zero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]);"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, 0);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[2, 3, 6, 7, 14, 15]",
             "1[2, 3, 4, 5]",
             "2[6, 7, 8, 9]",
             "3[10, 11, 12, 13]",
             "4[14, 15, 16, 17]",
             "false"})
    public void rotateAxisAngle_16_360() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]);"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, 360);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[6, 7, -2, -3, 14, 15]",
             "1[6, 7, 8, 9]",
             "2[-2, -3, -4, -5]",
             "3[10, 11, 12, 13]",
             "4[14, 15, 16, 17]",
             "false"})
    public void rotateAxisAngle_16_90_z() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]);"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, 90);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[2, 3, 10, 11, 14, 15]",
             "1[2, 3, 4, 5]",
             "2[10, 11, 12, 13]",
             "3[-6, -7, -8, -9]",
             "4[14, 15, 16, 17]",
             "false"})
    public void rotateAxisAngle_16_90_x() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]);"
                + "let rotated = m.rotateAxisAngle(1, 0, 0, 90);"
                + "log(rotated instanceof DOMMatrixReadOnly);\n"
                + "log(rotated instanceof DOMMatrix);\n"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[2, 3, -10, -11, 14, 15]",
             "1[2, 3, 4, 5]",
             "2[-10, -11, -12, -13]",
             "3[6, 7, 8, 9]",
             "4[14, 15, 16, 17]",
             "false"})
    public void rotateAxisAngle_16_270_x() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17]);"
                + "let rotated = m.rotateAxisAngle(1, 0, 0, 270);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[1, 0, 0, 1, 0, 0]",
                       "1[1, 0, 0, 0]",
                       "2[0, 1, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[0, 0, 0, 1]",
                       "true"},
            FF_ESR = {"[1, 0, 0, 1, 0, 0]",
                      "1[1, 0, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "false"})
    @HtmlUnitNYI(FF_ESR = {"[1, 0, 0, 1, 0, 0]",
                           "1[1, 0, 0, 0]",
                           "2[0, 1, 0, 0]",
                           "3[0, 0, 1, 0]",
                           "4[0, 0, 0, 1]",
                           "true"})
    public void rotateAxisAngle_invalid_axis() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly();"
                + "  let rotated = m.rotateAxisAngle(0, 0, 0, 90);"
                + "  dump(rotated);"
                + "} catch(e) { logEx(e); }"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[1, 0, 0, 1, 0, 0]",
                       "1[1, 0, 0, 0]",
                       "2[0, 1, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[0, 0, 0, 1]",
                       "false"},
            FF = {"[1, 0, 0, 1, 0, 0]",
                  "1[1, 0, 0, 0]",
                  "2[0, 1, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[0, 0, 0, 1]",
                  "true"},
            FF_ESR = {"[1, 0, 0, 1, 0, 0]",
                      "1[1, 0, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            FF = {"[1, 0, 0, 1, 0, 0]",
                  "1[1, 0, 0, 0]",
                  "2[0, 1, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[0, 0, 0, 1]",
                  "false"},
            FF_ESR = {"[1, 0, 0, 1, 0, 0]",
                      "1[1, 0, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "false"})
    public void rotateAxisAngle_missing_arguments() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrixReadOnly();"
                + "  let rotated = m.rotateAxisAngle(1, 0, 0);"
                + "  dump(rotated);"
                + "} catch(e) { logEx(e); }"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[NaN, NaN, NaN, NaN, 0, 0]",
                       "1[NaN, NaN, NaN, NaN]",
                       "2[NaN, NaN, NaN, NaN]",
                       "3[0, 0, 1, 0]",
                       "4[0, 0, 0, 1]",
                       "true"},
            FF = {"[NaN, NaN, NaN, NaN, 0, 0]",
                  "1[NaN, NaN, 0, 0]",
                  "2[NaN, NaN, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[0, 0, 0, 1]",
                  "true"},
            FF_ESR = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, NaN, NaN]",
                      "2[NaN, NaN, NaN, NaN]",
                      "3[NaN, NaN, NaN, NaN]",
                      "4[0, 0, 0, 1]",
                      "false"})
    @HtmlUnitNYI(
            CHROME = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, NaN, NaN]",
                      "2[NaN, NaN, NaN, NaN]",
                      "3[NaN, NaN, NaN, NaN]",
                      "4[0, 0, 0, 1]",
                      "true"},
            EDGE = {"[NaN, NaN, NaN, NaN, 0, 0]",
                    "1[NaN, NaN, NaN, NaN]",
                    "2[NaN, NaN, NaN, NaN]",
                    "3[NaN, NaN, NaN, NaN]",
                    "4[0, 0, 0, 1]",
                    "true"},
            FF = {"[NaN, NaN, NaN, NaN, 0, 0]",
                  "1[NaN, NaN, NaN, NaN]",
                  "2[NaN, NaN, NaN, NaN]",
                  "3[NaN, NaN, NaN, NaN]",
                  "4[0, 0, 0, 1]",
                  "true"},
            FF_ESR = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, NaN, NaN]",
                      "2[NaN, NaN, NaN, NaN]",
                      "3[NaN, NaN, NaN, NaN]",
                      "4[0, 0, 0, 1]",
                      "true"})
    public void rotateAxisAngle_NaN_angle() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, NaN);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[NaN, NaN, NaN, NaN, 0, 0]",
                       "1[NaN, NaN, NaN, NaN]",
                       "2[NaN, NaN, NaN, NaN]",
                       "3[0, 0, 1, 0]",
                       "4[0, 0, 0, 1]",
                       "true"},
            FF = {"[NaN, NaN, NaN, NaN, 0, 0]",
                  "1[NaN, NaN, 0, 0]",
                  "2[NaN, NaN, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[0, 0, 0, 1]",
                  "true"},
            FF_ESR = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, NaN, NaN]",
                      "2[NaN, NaN, NaN, NaN]",
                      "3[NaN, NaN, NaN, NaN]",
                      "4[0, 0, 0, 1]",
                      "false"})
    @HtmlUnitNYI(
            CHROME = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, NaN, NaN]",
                      "2[NaN, NaN, NaN, NaN]",
                      "3[NaN, NaN, NaN, NaN]",
                      "4[0, 0, 0, 1]",
                      "true"},
            EDGE = {"[NaN, NaN, NaN, NaN, 0, 0]",
                    "1[NaN, NaN, NaN, NaN]",
                    "2[NaN, NaN, NaN, NaN]",
                    "3[NaN, NaN, NaN, NaN]",
                    "4[0, 0, 0, 1]",
                    "true"},
            FF = {"[NaN, NaN, NaN, NaN, 0, 0]",
                  "1[NaN, NaN, NaN, NaN]",
                  "2[NaN, NaN, NaN, NaN]",
                  "3[NaN, NaN, NaN, NaN]",
                  "4[0, 0, 0, 1]",
                  "true"},
            FF_ESR = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, NaN, NaN]",
                      "2[NaN, NaN, NaN, NaN]",
                      "3[NaN, NaN, NaN, NaN]",
                      "4[0, 0, 0, 1]",
                      "true"})
    public void rotateAxisAngle_Infinity_angle() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();"
                + "let rotated = m.rotateAxisAngle(0, 0, 1, Infinity);"
                + "dump(rotated);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1")
    public void toFloat32Array_identity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let array = m.toFloat32Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("1,2,0,0,3,4,0,0,0,0,1,0,5,6,0,1")
    public void toFloat32Array_customValues6() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let array = m.toFloat32Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16")
    public void toFloat32Array_customValues16() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6,7 ,8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "let array = m.toFloat32Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("NaN,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1")
    public void toFloat32Array_withNaN() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly([NaN, 0, 0, 1, 0, 0]);\n"
                + "let array = m.toFloat32Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("Infinity,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1")
    public void toFloat32Array_withInfinity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly([Infinity, 0, 0, 1, 0, 0]);\n"
                + "let array = m.toFloat32Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1")
    public void toFloat64Array_identity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let array = m.toFloat64Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("1,2,0,0,3,4,0,0,0,0,1,0,5,6,0,1")
    public void toFloat64Array_customValues6() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "let array = m.toFloat64Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16")
    public void toFloat64Array_customValues16() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 ,13, 14, 15, 16]);\n"
                + "let array = m.toFloat64Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("NaN,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1")
    public void toFloat64Array_withNaN() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly([NaN, 0, 0, 1, 0, 0]);\n"
                + "let array = m.toFloat64Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("Infinity,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1")
    public void toFloat64Array_withInfinity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly([Infinity, 0, 0, 1, 0, 0]);\n"
                + "let array = m.toFloat64Array();\n"
                + "log(array);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0.577, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0.577, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void skewX_30degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let skewed = m.skewX(30);\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void skewX_0degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let skewed = m.skewX(0);\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void skewX_noParam() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let skewed = m.skewX();\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0.577, 0, 1, 0, 0]",
             "1[1, 0.577, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void skewY_30degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let skewed = m.skewY(30);\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void skewY_0degrees() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let skewed = m.skewY(0);\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void skewY_noParam() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let skewed = m.skewY();\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[1, 0, 0.9, 1, 5, 6]",
             "1[1, 0, 0, 0]",
             "2[0.9, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void skewX_6elements() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 0, 0, 1, 5, 6]);\n"
                + "let skewed = m.skewX(42);\n"
                + "log(skewed instanceof DOMMatrixReadOnly);\n"
                + "log(skewed instanceof DOMMatrix);\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[1, 0.9, 0, 1, 5, 6]",
             "1[1, 0.9, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void skewY_6elements() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 0, 0, 1, 5, 6]);\n"
                + "let skewed = m.skewY(42);\n"
                + "log(skewed instanceof DOMMatrixReadOnly);\n"
                + "log(skewed instanceof DOMMatrix);\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[1, 0, 5.9, 6, 13, 14]",
             "1[1, 0, 0, 1]",
             "2[5.9, 6, 7, 8.9]",
             "3[9, 10, 11, 12]",
             "4[13, 14, 15, 16]",
             "false"})
    public void skewX_16elements() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 0, 0, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "let skewed = m.skewX(42);\n"
                + "log(skewed instanceof DOMMatrixReadOnly);\n"
                + "log(skewed instanceof DOMMatrix);\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[5.502, 5.402, 5, 6, 13, 14]",
             "1[5.502, 5.402, 6.303, 8.203]",
             "2[5, 6, 7, 8]",
             "3[9, 10, 11, 12]",
             "4[13, 14, 15, 16]",
             "false"})
    public void skewY_16elements() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 0, 0, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "let skewed = m.skewY(42);\n"
                + "log(skewed instanceof DOMMatrixReadOnly);\n"
                + "log(skewed instanceof DOMMatrix);\n"
                + "dump(skewed);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[1, 0, 5, 6, 123, 134]",
             "1[1, 0, 0, 1]",
             "2[5, 6, 7, 8]",
             "3[9, 10, 11, 12]",
             "4[123, 134, 155, 186]",
             "false"})
    public void translate_16elements_positiveValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 0, 0, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "let translated = m.translate(10, 20);\n"
                + "log(translated instanceof DOMMatrixReadOnly);\n"
                + "log(translated instanceof DOMMatrix);\n"
                + "dump(translated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "true",
             "[1, 0, 0, 1, 15, 26]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[15, 26, 0, 1]",
             "true"})
    public void translate_6elements_positiveValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 0, 0, 1, 5, 6]);\n"
                + "let translated = m.translate(10, 20);\n"
                + "log(translated instanceof DOMMatrixReadOnly);\n"
                + "log(translated instanceof DOMMatrix);\n"
                + "dump(translated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 5, 6, -97, -106]",
             "1[1, 0, 0, 1]",
             "2[5, 6, 7, 8]",
             "3[9, 10, 11, 12]",
             "4[-97, -106, -125, -154]",
             "false"})
    public void translate_16elements_negativeValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([1, 0, 0, 1, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "let translated = m.translate(-10, -20);\n"
                + "dump(translated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[10, 11, 1, -5, -120, -3]",
             "1[10, 11, 0, 0]",
             "2[1, -5, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[-120, -3, 0, 1]",
             "true"})
    public void translate_6elements_negativeValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([10, 11, 1, -5, 0, 7]);\n"
                + "let translated = m.translate(-10, -20);\n"
                + "dump(translated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void translate_zeroValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let translated = m.translate(0, 0);\n"
                + "dump(translated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, NaN, NaN]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[NaN, NaN, 0, 1]",
             "true"})
    @HtmlUnitNYI(
            CHROME = {"[1, 0, 0, 1, NaN, NaN]",
                      "1[1, 0, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[NaN, NaN, NaN, NaN]",
                      "true"},
            EDGE = {"[1, 0, 0, 1, NaN, NaN]",
                    "1[1, 0, 0, 0]",
                    "2[0, 1, 0, 0]",
                    "3[0, 0, 1, 0]",
                    "4[NaN, NaN, NaN, NaN]",
                    "true"},
            FF = {"[1, 0, 0, 1, NaN, NaN]",
                  "1[1, 0, 0, 0]",
                  "2[0, 1, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[NaN, NaN, NaN, NaN]",
                  "true"},
            FF_ESR = {"[1, 0, 0, 1, NaN, NaN]",
                      "1[1, 0, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[NaN, NaN, NaN, NaN]",
                      "true"})
    public void translate_withNaN() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let translated = m.translate(NaN, NaN);\n"
                + "dump(translated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"[1, 0, 0, 1, Infinity, Infinity]",
                       "1[1, 0, 0, 0]",
                       "2[0, 1, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[Infinity, Infinity, 0, 1]",
                       "true"},
            FF = {"[1, 0, 0, 1, NaN, NaN]",
                  "1[1, 0, 0, 0]",
                  "2[0, 1, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[NaN, NaN, 0, 1]",
                  "true"},
            FF_ESR = {"[1, 0, 0, 1, NaN, NaN]",
                      "1[1, 0, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[NaN, NaN, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            CHROME = {"[1, 0, 0, 1, NaN, NaN]",
                      "1[1, 0, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[NaN, NaN, NaN, NaN]",
                      "true"},
            EDGE = {"[1, 0, 0, 1, NaN, NaN]",
                    "1[1, 0, 0, 0]",
                    "2[0, 1, 0, 0]",
                    "3[0, 0, 1, 0]",
                    "4[NaN, NaN, NaN, NaN]",
                    "true"},
            FF = {"[1, 0, 0, 1, NaN, NaN]",
                  "1[1, 0, 0, 0]",
                  "2[0, 1, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[NaN, NaN, NaN, NaN]",
                  "true"},
            FF_ESR = {"[1, 0, 0, 1, NaN, NaN]",
                      "1[1, 0, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[NaN, NaN, NaN, NaN]",
                      "true"})
    public void translate_withInfinity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let translated = m.translate(Infinity, Infinity);\n"
                + "dump(translated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 10, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 0, 0, 1]",
             "true"})
    public void translate_onlyX() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let translated = m.translate(10);\n"
                + "dump(translated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 10, 20]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 20, 30, 1]",
             "false"})
    public void translate_withZ() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "let translated = m.translate(10, 20, 30);\n"
                + "dump(translated);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
