/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
    @Alerts({"[0, 1, -1, 0, 0, 0]",
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
    @Alerts({"[5, 4, -7, -6, 3, 2]",
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
    @Alerts({"[1, 0, 0, 1, 0, 0]",
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
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            CHROME = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"},
            EDGE = {"[NaN, NaN, NaN, NaN, 0, 0]",
                    "1[NaN, NaN, 0, 0]",
                    "2[NaN, NaN, 0, 0]",
                    "3[0, 0, 1, 0]",
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
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            CHROME = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"},
            EDGE = {"[NaN, NaN, NaN, NaN, 0, 0]",
                    "1[NaN, NaN, 0, 0]",
                    "2[NaN, NaN, 0, 0]",
                    "3[0, 0, 1, 0]",
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
                      "4[NaN, NaN, 0, 1]",
                      "true"},
            EDGE = {"[1, 0, 0, 1, NaN, NaN]",
                    "1[1, 0, 0, 0]",
                    "2[0, 1, 0, 0]",
                    "3[0, 0, 1, 0]",
                    "4[NaN, NaN, 0, 1]",
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

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true",
             "false",
             "false",
             "false",
             "false",
             "false",
             "false",
             "false",
             "true",
             "true",
             "false",
             "true"})
    public void isIdentity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "let m = new DOMMatrixReadOnly();\n"
                + "log(m.isIdentity);\n"

                + "m = new DOMMatrixReadOnly([0, 0, 0, 0, 0, 0]);"
                + "log(m.isIdentity);\n"

                + "m = new DOMMatrixReadOnly([1, 0, 0, 0, 0, 0]);"
                + "log(m.isIdentity);\n"
                + "m = new DOMMatrixReadOnly([0, 1, 0, 0, 0, 0]);"
                + "log(m.isIdentity);\n"
                + "m = new DOMMatrixReadOnly([0, 0, 1, 0, 0, 0]);"
                + "log(m.isIdentity);\n"
                + "m = new DOMMatrixReadOnly([0, 0, 0, 1, 0, 0]);"
                + "log(m.isIdentity);\n"
                + "m = new DOMMatrixReadOnly([0, 0, 0, 0, 1, 0]);"
                + "log(m.isIdentity);\n"
                + "m = new DOMMatrixReadOnly([0, 0, 0, 0, 0, 1]);"
                + "log(m.isIdentity);\n"

                + "m = new DOMMatrixReadOnly([1, 0, 0, 1, 0, 0]);"
                + "log(m.isIdentity);\n"
                + "m = new DOMMatrixReadOnly([1, -0, -0, 1, -0, -0]);"
                + "log(m.isIdentity);\n"

                + "m = new DOMMatrixReadOnly([0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]);\n"
                + "log(m.isIdentity);\n"
                + "m = new DOMMatrixReadOnly([1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1]);\n"
                + "log(m.isIdentity);\n"

                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("matrix(1, 0, 0, 1, 20, 30)")
    public void toString2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "let m = new DOMMatrixReadOnly();\n"
                + "m = m.translate(20, 30);"
                + "log(m.toString());\n"

                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 30, 40, 50, 1)")
    public void toString3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "let m = new DOMMatrixReadOnly();\n"
                + "m = m.translate(30, 40, 50);"
                + "log(m.toString());\n"

                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"{\"a\":1,\"b\":0,\"c\":0,\"d\":1,\"e\":20,\"f\":30,"
                + "\"m11\":1,\"m12\":0,\"m13\":0,\"m14\":0,"
                + "\"m21\":0,\"m22\":1,\"m23\":0,\"m24\":0,"
                + "\"m31\":0,\"m32\":0,\"m33\":1,\"m34\":0,"
                + "\"m41\":20,\"m42\":30,\"m43\":0,\"m44\":1,"
                + "\"is2D\":true,\"isIdentity\":false}",
             "{\"a\":1,\"b\":0,\"c\":0,\"d\":1,\"e\":20,\"f\":30,"
                + "\"m11\":1,\"m12\":0,\"m13\":0,\"m14\":0,"
                + "\"m21\":0,\"m22\":1,\"m23\":0,\"m24\":0,"
                + "\"m31\":0,\"m32\":0,\"m33\":1,\"m34\":0,"
                + "\"m41\":20,\"m42\":30,\"m43\":0,\"m44\":1,"
                + "\"is2D\":true,\"isIdentity\":false}",
             "false"})
    public void toJSON2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION_NORMALIZE

                + "let m = new DOMMatrixReadOnly();\n"
                + "m = m.translate(20, 30);"
                + "log(JSON.stringify(m));\n"
                + "log(JSON.stringify(m.toJSON()));\n"
                + "log(m === m.toJSON());\n"

                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"{\"a\":1,\"b\":0,\"c\":0,\"d\":1,\"e\":30,\"f\":40,"
                + "\"m11\":1,\"m12\":0,\"m13\":0,\"m14\":0,"
                + "\"m21\":0,\"m22\":1,\"m23\":0,\"m24\":0,"
                + "\"m31\":0,\"m32\":0,\"m33\":1,\"m34\":0,"
                + "\"m41\":30,\"m42\":40,\"m43\":50,\"m44\":1,"
                + "\"is2D\":false,\"isIdentity\":false}",
             "{\"a\":1,\"b\":0,\"c\":0,\"d\":1,\"e\":30,\"f\":40,"
                + "\"m11\":1,\"m12\":0,\"m13\":0,\"m14\":0,"
                + "\"m21\":0,\"m22\":1,\"m23\":0,\"m24\":0,"
                + "\"m31\":0,\"m32\":0,\"m33\":1,\"m34\":0,"
                + "\"m41\":30,\"m42\":40,\"m43\":50,\"m44\":1,"
                + "\"is2D\":false,\"isIdentity\":false}",
             "false"})
    public void toJSON3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION_NORMALIZE

                + "let m = new DOMMatrixReadOnly();\n"
                + "m = m.translate(30, 40, 50);"
                + "log(JSON.stringify(m));\n"
                + "log(JSON.stringify(m.toJSON()));\n"
                + "log(m === m.toJSON());\n"

                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]", "1[1, 2, 0, 0]", "2[3, 4, 0, 0]", "3[0, 0, 1, 0]", "4[5, 6, 0, 1]", "true"})
    public void contructorFloat32Array() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly(new Float32Array([1, 2, 3, 4, 5, 6]));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 5, 6, 13, 14]", "1[1, 2, 3, 4]", "2[5, 6, 7, 8]", "3[9, 10, 11, 12]", "4[13, 14, 15, 16]", "false"})
    public void contructorFloat64Array16() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let values = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16];\n"
                + "  let m = new DOMMatrixReadOnly(new Float64Array(values));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]", "1[1, 2, 0, 0]", "2[3, 4, 0, 0]", "3[0, 0, 1, 0]", "4[5, 6, 0, 1]", "true"})
    public void contructorSet() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly(new Set([1, 2, 3, 4, 5, 6]));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void contructorTypedArrayInvalidLength() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly(new Float32Array([1, 2, 3]));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorObject() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly({});\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]", "1[1, 2, 0, 0]", "2[3, 4, 0, 0]", "3[0, 0, 1, 0]", "4[5, 6, 0, 1]", "true"})
    public void contructorMatrix() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  let m = new DOMMatrixReadOnly(m1);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 5, 6, 13, 14]", "1[1, 2, 3, 4]", "2[5, 6, 7, 8]", "3[9, 10, 11, 12]", "4[13, 14, 15, 16]", "false"})
    public void contructorMatrix3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "  let m = new DOMMatrixReadOnly(m1);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]", "1[1, 0, 0, 0]", "2[0, 1, 0, 0]", "3[0, 0, 1, 0]", "4[0, 0, 0, 1]", "true"})
    public void contructorStringEmpty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]", "1[1, 0, 0, 0]", "2[0, 1, 0, 0]", "3[0, 0, 1, 0]", "4[0, 0, 0, 1]", "true"})
    public void contructorStringNone() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('none');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]", "1[1, 2, 0, 0]", "2[3, 4, 0, 0]", "3[0, 0, 1, 0]", "4[5, 6, 0, 1]", "true"})
    public void contructorStringMatrix() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('matrix(1, 2, 3, 4, 5, 6)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 5, 6, 13, 14]", "1[1, 2, 3, 4]", "2[5, 6, 7, 8]", "3[9, 10, 11, 12]", "4[13, 14, 15, 16]", "false"})
    public void contructorStringMatrix3d() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('matrix3d(1, 2, 3, 4, 5, 6, 7, 8, '\n"
                + "      + '9, 10, 11, 12, 13, 14, 15, 16)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]", "1[1, 0, 0, 0]", "2[0, 1, 0, 0]", "3[0, 0, 1, 0]", "4[0, 0, 0, 1]", "false"})
    public void contructorStringMatrix3d2DValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('matrix3d(1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 10, 20]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 20, 0, 1]",
             "true"})
    public void contructorStringTranslate() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(10px, 20px)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 10, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 0, 0, 1]",
             "true"})
    public void contructorStringTranslateOneArg() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(10px)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 192, 133.795]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[192, 133.795, 0, 1]",
             "true"})
    public void contructorStringTranslateUnits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(1in, 2.54cm) translateX(72pt) translateY(10mm)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorStringTranslateZero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(0, 0)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorStringTranslateNoUnit() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(10, 20)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorStringTranslateRelativeUnit() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(1em, 2px)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorStringTranslatePercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(10%, 2px)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 10, 20]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 20, 30, 1]",
             "false"})
    public void contructorStringTranslate3d() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate3d(10px, 20px, 30px)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]", "1[1, 0, 0, 0]", "2[0, 1, 0, 0]", "3[0, 0, 1, 0]", "4[0, 0, 0, 1]", "false"})
    public void contructorStringTranslateZZero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translateZ(0px)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 3, 0, 0]",
             "1[2, 0, 0, 0]",
             "2[0, 3, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorStringScale() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('scale(2, 3)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 2, 0, 0]",
             "1[2, 0, 0, 0]",
             "2[0, 2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorStringScaleOneArg() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('scale(2)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[0.5, 0, 0, 0.5, 0, 0]",
             "1[0.5, 0, 0, 0]",
             "2[0, 0.5, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorStringScalePercent() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('scale(50%)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 3, 0, 0]",
             "1[2, 0, 0, 0]",
             "2[0, 3, 0, 0]",
             "3[0, 0, 4, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void contructorStringScaleXYZ() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('scaleX(2) scaleY(3) scaleZ(4)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 3, 0, 0]",
             "1[2, 0, 0, 0]",
             "2[0, 3, 0, 0]",
             "3[0, 0, 4, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void contructorStringScale3d() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('scale3d(2, 3, 4)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[0, 1, -1, 0, 0, 0]",
             "1[0, 1, 0, 0]",
             "2[-1, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorStringRotate() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('rotate(90deg)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[0, 1, -1, 0, 0, 0]",
             "1[0, 1, 0, 0]",
             "2[-1, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true",
             "[0, 1, -1, 0, 0, 0]",
             "1[0, 1, 0, 0]",
             "2[-1, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true",
             "[0, 1, -1, 0, 0, 0]",
             "1[0, 1, 0, 0]",
             "2[-1, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorStringRotateUnits() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly('rotate(0.25turn)');\n"
                + "  dump(m1);\n"
                + "  let m2 = new DOMMatrixReadOnly('rotate(100grad)');\n"
                + "  dump(m2);\n"
                + "  let m3 = new DOMMatrixReadOnly('rotate(1.5707963267948966rad)');\n"
                + "  dump(m3);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorStringRotateNoUnit() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('rotate(90)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorStringRotateZero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('rotate(0)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[1, 0, 0, 0, 0, 0]",
                       "1[1, 0, 0, 0]",
                       "2[0, 0, 1, 0]",
                       "3[0, -1, 0, 0]",
                       "4[0, 0, 0, 1]",
                       "false",
                       "[0, 0, 0, 1, 0, 0]",
                       "1[0, 0, -1, 0]",
                       "2[0, 1, 0, 0]",
                       "3[1, 0, 0, 0]",
                       "4[0, 0, 0, 1]",
                       "false",
                       "[0, 1, -1, 0, 0, 0]",
                       "1[0, 1, 0, 0]",
                       "2[-1, 0, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[0, 0, 0, 1]",
                       "true"},
            FF = {"[1, 0, 0, 0, 0, 0]",
                  "1[1, 0, 0, 0]",
                  "2[0, 0, 1, 0]",
                  "3[0, -1, 0, 0]",
                  "4[0, 0, 0, 1]",
                  "false",
                  "[0, 0, 0, 1, 0, 0]",
                  "1[0, 0, -1, 0]",
                  "2[0, 1, 0, 0]",
                  "3[1, 0, 0, 0]",
                  "4[0, 0, 0, 1]",
                  "false",
                  "[0, 1, -1, 0, 0, 0]",
                  "1[0, 1, 0, 0]",
                  "2[-1, 0, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[0, 0, 0, 1]",
                  "false"},
            FF_ESR = {"[1, 0, 0, 0, 0, 0]",
                      "1[1, 0, 0, 0]",
                      "2[0, 0, 1, 0]",
                      "3[0, -1, 0, 0]",
                      "4[0, 0, 0, 1]",
                      "false",
                      "[0, 0, 0, 1, 0, 0]",
                      "1[0, 0, -1, 0]",
                      "2[0, 1, 0, 0]",
                      "3[1, 0, 0, 0]",
                      "4[0, 0, 0, 1]",
                      "false",
                      "[0, 1, -1, 0, 0, 0]",
                      "1[0, 1, 0, 0]",
                      "2[-1, 0, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "false"})
    @HtmlUnitNYI(
            FF = {"[1, 0, 0, 0, 0, 0]",
                  "1[1, 0, 0, 0]",
                  "2[0, 0, 1, 0]",
                  "3[0, -1, 0, 0]",
                  "4[0, 0, 0, 1]",
                  "false",
                  "[0, 0, 0, 1, 0, 0]",
                  "1[0, 0, -1, 0]",
                  "2[0, 1, 0, 0]",
                  "3[1, 0, 0, 0]",
                  "4[0, 0, 0, 1]",
                  "false",
                  "[0, 1, -1, 0, 0, 0]",
                  "1[0, 1, 0, 0]",
                  "2[-1, 0, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[0, 0, 0, 1]",
                  "true"},
            FF_ESR = {"[1, 0, 0, 0, 0, 0]",
                      "1[1, 0, 0, 0]",
                      "2[0, 0, 1, 0]",
                      "3[0, -1, 0, 0]",
                      "4[0, 0, 0, 1]",
                      "false",
                      "[0, 0, 0, 1, 0, 0]",
                      "1[0, 0, -1, 0]",
                      "2[0, 1, 0, 0]",
                      "3[1, 0, 0, 0]",
                      "4[0, 0, 0, 1]",
                      "false",
                      "[0, 1, -1, 0, 0, 0]",
                      "1[0, 1, 0, 0]",
                      "2[-1, 0, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"})
    public void contructorStringRotateXYZ() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly('rotateX(90deg)');\n"
                + "  dump(m1);\n"
                + "  let m2 = new DOMMatrixReadOnly('rotateY(90deg)');\n"
                + "  dump(m2);\n"
                + "  let m3 = new DOMMatrixReadOnly('rotateZ(90deg)');\n"
                + "  dump(m3);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[0.854, 0.146, 0.146, 0.854, 0, 0]",
             "1[0.854, 0.146, -0.5, 0]",
             "2[0.146, 0.854, 0.5, 0]",
             "3[0.5, -0.5, 0.707, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void contructorStringRotate3d() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('rotate3d(1, 1, 0, 45deg)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0.364, 0.577, 1, 0, 0]",
             "1[1, 0.364, 0, 0]",
             "2[0.577, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorStringSkew() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('skew(30deg, 20deg)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0.577, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0.577, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true",
             "[1, 0.577, 0, 1, 0, 0]",
             "1[1, 0.577, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void contructorStringSkewXY() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrixReadOnly('skewX(30deg)');\n"
                + "  dump(m1);\n"
                + "  let m2 = new DOMMatrixReadOnly('skewY(30deg)');\n"
                + "  dump(m2);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, -0.01]",
             "4[0, 0, 0, 1]",
             "false"})
    public void contructorStringPerspective() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('perspective(100px)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]", "1[1, 0, 0, 0]", "2[0, 1, 0, 0]", "3[0, 0, 1, 0]", "4[0, 0, 0, 1]", "false"})
    public void contructorStringPerspectiveNone() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('perspective(none)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[0, 2, -2, 0, 10, 20]", "1[0, 2, 0, 0]", "2[-2, 0, 0, 0]", "3[0, 0, 1, 0]", "4[10, 20, 0, 1]", "true"})
    public void contructorStringList() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(10px, 20px) rotate(90deg) scale(2)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 2, 10, 20]", "1[2, 0, 0, 0]", "2[0, 2, 0, 0]", "3[0, 0, 1, 0]", "4[10, 20, 0, 1]", "true"})
    public void contructorStringListNoSpace() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(10px,20px)scale(2)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 2, 10, 20]", "1[2, 0, 0, 0]", "2[0, 2, 0, 0]", "3[0, 0, 1, 0]", "4[10, 20, 0, 1]", "true"})
    public void contructorStringCaseInsensitive() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('TRANSLATE(10PX, 20Px) Scale(2)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 10, 20]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 20, 0, 1]",
             "true"})
    public void contructorStringWhitespace() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('  translate( 10px , 20px )  ');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorStringCommaSeparatedList() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(10px), scale(2)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorStringUnknownFunction() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('foo(10px)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorStringWrongArgumentCount() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('matrix(1, 2, 3)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 10, 0]", "1[1, 0, 0, 0]", "2[0, 1, 0, 0]", "3[0, 0, 1, 0]", "4[10, 0, 0, 1]", "true"})
    public void contructorStringNotClosed() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('translate(10px');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void contructorStringInvalid() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('invalid');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, -2, 0.5, 10, 0.15, 0]",
             "1[1, -2, 0, 0]",
             "2[0.5, 10, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0.15, 0, 0, 1]",
             "true"})
    public void contructorStringNumbers() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('matrix(+1, -2, .5, 1e1, 1.5E-1, 0)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void multiplyNoArgumentNotIdentity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply());\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void multiplyEmptyDictionary() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply({}));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 9, 12, 15, 26]",
             "1[2, 4, 0, 0]",
             "2[9, 12, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[15, 26, 0, 1]",
             "true"})
    public void multiplyDictionary() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply({a: 2, d: 3, e: 10}));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 9, 12, 15, 26]",
             "1[2, 4, 0, 0]",
             "2[9, 12, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[15, 26, 0, 1]",
             "true"})
    public void multiplyDictionaryLongNames() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply({m11: 2, m22: 3, m41: 10}));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 3, 4, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void multiplyDictionarySameValue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply({a: 2, m11: 2}));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void multiplyDictionaryDifferentValue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply({a: 2, m11: 3}));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[NaN, 0, 0, 1, 0, 0]",
                       "1[NaN, 0, 0, 0]",
                       "2[0, 1, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[0, 0, 0, 1]",
                       "true"},
            FF = {"[NaN, NaN, 0, 1, 0, 0]",
                  "1[NaN, NaN, 0, 0]",
                  "2[0, 1, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[0, 0, 0, 1]",
                  "true"},
            FF_ESR = {"[NaN, NaN, 0, 1, 0, 0]",
                      "1[NaN, NaN, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            CHROME = {"[NaN, NaN, 0, 1, 0, 0]",
                      "1[NaN, NaN, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"},
              EDGE = {"[NaN, NaN, 0, 1, 0, 0]",
                      "1[NaN, NaN, 0, 0]",
                      "2[0, 1, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"})
    public void multiplyDictionaryNaN() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  dump(m.multiply({a: NaN, m11: NaN}));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 2, 0]",
             "4[5, 6, 5, 1]",
             "false"})
    public void multiplyDictionary3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply({m33: 2, m43: 5}));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[1, 2, 3, 4, 5, 6]",
                       "1[1, 2, 0, 0]",
                       "2[3, 4, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[5, 6, 0, 1]",
                       "false"},
            FF = {"[1, 2, 3, 4, 5, 6]",
                  "1[1, 2, 0, 0]",
                  "2[3, 4, 0, 0]",
                  "3[0, 0, 1, 0]",
                  "4[5, 6, 0, 1]",
                  "true"},
            FF_ESR = {"[1, 2, 3, 4, 5, 6]",
                      "1[1, 2, 0, 0]",
                      "2[3, 4, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[5, 6, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            CHROME = {"[1, 2, 3, 4, 5, 6]",
                      "1[1, 2, 0, 0]",
                      "2[3, 4, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[5, 6, 0, 1]",
                      "true"},
            EDGE = {"[1, 2, 3, 4, 5, 6]",
                    "1[1, 2, 0, 0]",
                    "2[3, 4, 0, 0]",
                    "3[0, 0, 1, 0]",
                    "4[5, 6, 0, 1]",
                    "true"})
    public void multiplyDictionaryIs2DFalse() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply({is2D: false}));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void multiplyDictionaryIs2DConflict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply({is2D: true, m33: 2}));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void multiplyArray() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.multiply([2, 0, 0, 2, 0, 0]));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void multiplyResultType() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  let r = m.multiply();\n"
                + "  log(r instanceof DOMMatrix);\n"
                + "  log(r === m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[3, 4, -1, -2, 5, 6]",
             "1[3, 4, 0, 0]",
             "2[-1, -2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotate_three_arguments_z() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotate(0, 0, 90));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 0, 0, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[0, 0, 1, 0]",
             "3[-3, -4, 0, 0]",
             "4[5, 6, 0, 1]",
             "false"})
    public void rotate_two_arguments() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotate(90, 0));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 0, 0, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[0, 0, 1, 0]",
             "3[-3, -4, 0, 0]",
             "4[5, 6, 0, 1]",
             "false"})
    public void rotate_x() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotate(90, 0, 0));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[0, 0, 3, 4, 5, 6]",
             "1[0, 0, -1, 0]",
             "2[3, 4, 0, 0]",
             "3[1, 2, 0, 0]",
             "4[5, 6, 0, 1]",
             "false"})
    public void rotate_y() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotate(0, 90, 0));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2.191, 3.157, 1.644, 1.81, 5, 6]",
             "1[2.191, 3.157, -0.707, 0]",
             "2[1.644, 1.81, 0.354, 0]",
             "3[1.58, 2.6, 0.612, 0]",
             "4[5, 6, 0, 1]",
             "false"})
    public void rotate_xyz() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotate(30, 45, 60));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotate_x_zero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotate(0, 0));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2.828, 4.243, 1.414, 1.414, 5, 6]",
             "1[2.828, 4.243, 0, 0]",
             "2[1.414, 1.414, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotate_y_undefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotate(45, undefined, undefined));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2.828, 4.243, 1.414, 1.414, 5, 6]",
             "1[2.828, 4.243, 0, 0]",
             "2[1.414, 1.414, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotateFromVector() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotateFromVector(1, 1));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[-1, 0, 0, -1, 0, 0]",
             "1[-1, 0, 0, 0]",
             "2[0, -1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotateFromVectorNegative() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  dump(m.rotateFromVector(-1, 0));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotateFromVectorZero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotateFromVector(0, 0));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotateFromVectorNoArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotateFromVector());\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[0, 1, -1, 0, 0, 0]",
             "1[0, 1, 0, 0]",
             "2[-1, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void rotateFromVector3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1]);\n"
                + "  dump(m.rotateFromVector(0, 1));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotateAxisAngle_zero_axis_not_identity() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.rotateAxisAngle(0, 0, 0, 90));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[0, -1, 1, 0, 0, 0]",
             "1[0, -1, 0, 0]",
             "2[1, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true"})
    public void rotateAxisAngle_negative_z() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  dump(m.rotateAxisAngle(0, 0, -1, 90));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[0, 0, 0, 1, 0, 0]",
             "1[0, 0, -1, 0]",
             "2[0, 1, 0, 0]",
             "3[1, 0, 0, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void rotateAxisAngle_y_90() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  dump(m.rotateAxisAngle(0, 1, 0, 90));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scale_noArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scale());\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 6, 8, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[6, 8, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scale_oneArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scale(2));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 9, 12, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[9, 12, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scale_twoArguments() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scale(2, 3));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 9, 12, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[9, 12, 0, 0]",
             "3[0, 0, 4, 0]",
             "4[5, 6, 0, 1]",
             "false"})
    public void scale_threeArguments() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scale(2, 3, 4));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 9, 12, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[9, 12, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scale_z1() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scale(2, 3, 1));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 2, -10, -20]",
             "1[2, 0, 0, 0]",
             "2[0, 2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[-10, -20, 0, 1]",
             "true"})
    public void scale_origin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  dump(m.scale(2, 2, 1, 10, 20));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 2, -10, -20]",
             "1[2, 0, 0, 0]",
             "2[0, 2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[-10, -20, 0, 1]",
             "false"})
    public void scale_originZ() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  dump(m.scale(2, 2, 1, 10, 20, 30));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"[NaN, 0, 0, NaN, 0, 0]",
                       "1[NaN, 0, 0, 0]",
                       "2[0, NaN, 0, 0]",
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
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"})
    @HtmlUnitNYI(
            CHROME = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"},
              EDGE = {"[NaN, NaN, NaN, NaN, 0, 0]",
                      "1[NaN, NaN, 0, 0]",
                      "2[NaN, NaN, 0, 0]",
                      "3[0, 0, 1, 0]",
                      "4[0, 0, 0, 1]",
                      "true"})
    public void scale_NaN() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  dump(m.scale(NaN));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scale3d_noArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scale3d());\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scale3d_one() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scale3d(1));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 6, 8, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[6, 8, 0, 0]",
             "3[0, 0, 2, 0]",
             "4[5, 6, 0, 1]",
             "false"})
    public void scale3d() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scale3d(2));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 2, -10, -20]",
             "1[2, 0, 0, 0]",
             "2[0, 2, 0, 0]",
             "3[0, 0, 2, 0]",
             "4[-10, -20, -30, 1]",
             "false"})
    public void scale3d_origin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  dump(m.scale3d(2, 10, 20, 30));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 9, 12, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[9, 12, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scaleNonUniform() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scaleNonUniform(2, 3));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 4, 3, 4, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scaleNonUniform_oneArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scaleNonUniform(2));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scaleNonUniform_noArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m.scaleNonUniform());\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("5, 6, 0, 1")
    public void transformPoint_noArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  let p = m.transformPoint();\n"
                + "  log(p.x + ', ' + p.y + ', ' + p.z + ', ' + p.w);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12, 16, 0, 1")
    public void transformPoint_dictionary() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  let p = m.transformPoint({x: 1, y: 2});\n"
                + "  log(p.x + ', ' + p.y + ', ' + p.z + ', ' + p.w);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("27, 34, 3, 4")
    public void transformPoint_point() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  let p = m.transformPoint(new DOMPoint(1, 2, 3, 4));\n"
                + "  log(p.x + ', ' + p.y + ', ' + p.z + ', ' + p.w);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("90, 100, 110, 120")
    public void transformPoint_3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "  let p = m.transformPoint({x: 1, y: 2, z: 3, w: 4});\n"
                + "  log(p.x + ', ' + p.y + ', ' + p.z + ', ' + p.w);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("10, 20, 50, 0.5")
    public void transformPoint_perspective() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly('perspective(100px)');\n"
                + "  let p = m.transformPoint({x: 10, y: 20, z: 50});\n"
                + "  log(p.x + ', ' + p.y + ', ' + p.z + ', ' + p.w);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true",
                      "[object DOMPoint]"})
    public void transformPoint_resultType() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  let p = m.transformPoint({x: 1});\n"
                + "  log(p instanceof DOMPoint);\n"
                + "  log(Object.prototype.toString.call(p));\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void transformPoint_number() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  let p = m.transformPoint(42);\n"
                + "  log(p.x + ', ' + p.y + ', ' + p.z + ', ' + p.w);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 0, 0, 1, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 0, 1]",
             "true",
             "false",
             "true"})
    public void fromMatrix_noArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromMatrix();\n"
                + "  dump(m);\n"
                + "  log(m instanceof DOMMatrix);\n"
                + "  log(m instanceof DOMMatrixReadOnly);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void fromMatrix_dictionary() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromMatrix({a: 1, b: 2, c: 3, d: 4, e: 5, f: 6});\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 1, 0, 0]",
             "1[2, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 0, 7, 1]",
             "false"})
    public void fromMatrix_dictionary3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromMatrix({m11: 2, m43: 7});\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true",
             "false",
             "false"})
    public void fromMatrix_matrix() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let other = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let m = DOMMatrixReadOnly.fromMatrix(other);\n"
                + "  dump(m);\n"
                + "  log(m === other);\n"
                + "  log(m instanceof DOMMatrix);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fromMatrix_invalid() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromMatrix({a: 1, m11: 2});\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fromMatrix_string() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromMatrix('matrix(1, 2, 3, 4, 5, 6)');\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true",
             "false"})
    public void fromFloat32Array_6() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromFloat32Array(new Float32Array([1, 2, 3, 4, 5, 6]));\n"
                + "  dump(m);\n"
                + "  log(m instanceof DOMMatrix);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 5, 6, 13, 14]", "1[1, 2, 3, 4]", "2[5, 6, 7, 8]", "3[9, 10, 11, 12]", "4[13, 14, 15, 16]", "false"})
    public void fromFloat32Array_16() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let values = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16];\n"
                + "  let m = DOMMatrixReadOnly.fromFloat32Array(new Float32Array(values));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fromFloat32Array_invalidLength() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromFloat32Array(new Float32Array([1, 2, 3, 4, 5]));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fromFloat32Array_wrongType() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromFloat32Array(new Float64Array([1, 2, 3, 4, 5, 6]));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fromFloat32Array_array() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromFloat32Array([1, 2, 3, 4, 5, 6]);\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void fromFloat64Array_6() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromFloat64Array(new Float64Array([1, 2, 3, 4, 5, 6]));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[1, 2, 5, 6, 13, 14]",
             "1[1, 2, 3, 4]",
             "2[5, 6, 7, 8]",
             "3[9, 10, 11, 12]",
             "4[13, 14, 15, 16]",
             "false"})
    public void fromFloat64Array_16() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let values = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16];\n"
                + "  let m = DOMMatrixReadOnly.fromFloat64Array(new Float64Array(values));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fromFloat64Array_wrongType() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrixReadOnly.fromFloat64Array(new Float32Array([1, 2, 3, 4, 5, 6]));\n"
                + "  dump(m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"matrix(1, 2, 3, 4, 5, 6)",
             "matrix(1, 2, 3, 4, 5, 6)",
             "matrix(1, 2, 3, 4, 5, 6)"})
    public void stringConversion() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  log('' + m);\n"
                + "  log(String(m));\n"
                + "  log(`${m}`);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("matrix3d(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16)")
    public void stringConversion3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "  log('' + m);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true",
             "true",
             "true",
             "true",
             "true",
             "true",
             "true",
             "true",
             "true",
             "true",
             "true"})
    public void resultTypes() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  log(m.translate(1) instanceof DOMMatrix);\n"
                + "  log(m.scale(2) instanceof DOMMatrix);\n"
                + "  log(m.scale3d(2) instanceof DOMMatrix);\n"
                + "  log(m.scaleNonUniform(2) instanceof DOMMatrix);\n"
                + "  log(m.rotate(2) instanceof DOMMatrix);\n"
                + "  log(m.rotateFromVector(1, 1) instanceof DOMMatrix);\n"
                + "  log(m.rotateAxisAngle(1, 0, 0, 2) instanceof DOMMatrix);\n"
                + "  log(m.skewX(2) instanceof DOMMatrix);\n"
                + "  log(m.skewY(2) instanceof DOMMatrix);\n"
                + "  log(m.flipX() instanceof DOMMatrix);\n"
                + "  log(m.inverse() instanceof DOMMatrix);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined",
             "undefined",
             "undefined"})
    public void noSelfFunctions() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrixReadOnly();\n"
                + "  log(typeof m.translateSelf);\n"
                + "  log(typeof m.multiplySelf);\n"
                + "  log(typeof m.setMatrixValue);\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
