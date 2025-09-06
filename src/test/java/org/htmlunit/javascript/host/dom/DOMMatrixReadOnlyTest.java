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

    public static final String DUMP_FUNCTION = "function dump(m) {\n"
            + "  log('[' + m.a + ', ' + m.b + ', ' + m.c + ', ' + m.d + ', ' + m.e + ', ' + m.f + ']');\n"
            + "  log('1[' + m.m11 + ', ' + m.m12 + ', ' + m.m13 + ', ' + m.m14 + ']');\n"
            + "  log('2[' + m.m21 + ', ' + m.m22 + ', ' + m.m23 + ', ' + m.m24 + ']');\n"
            + "  log('3[' + m.m31 + ', ' + m.m32 + ', ' + m.m33 + ', ' + m.m34 + ']');\n"
            + "  log('4[' + m.m41 + ', ' + m.m42 + ', ' + m.m43 + ', ' + m.m44 + ']');\n"
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
    @Alerts({"[-2, -3, 4, 5, 6, 7]",
             "true",
             "[2, 3, 4, 5, 6, 7]",
             "true"})
    public void flipX_general() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly([2, 3, 4, 5, 6, 7]);\n"
                + "let flipped = m.flipX();\n"
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
    @Alerts({"[2, 3, -4, -5, 6, 7]",
             "true",
             "[2, 3, 4, 5, 6, 7]",
             "true"})
    public void flipY_general() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly([2, 3, 4, 5, 6, 7]);\n"
                + "let flipped = m.flipY();\n"
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
    @Alerts({"[0.25, 0, 0, 0.2, -2.5, -2.6]", "true",
             "[4, 0, 0, 5, 10, 13]", "true"})
    public void inverse_general2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_2D_FUNCTION
                + "let m = new DOMMatrixReadOnly([4, 0, 0, 5, 10, 13]);"
                + "let inv = m.inverse();"
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
    @Alerts({"[2, 0, 0, 0.5, 0, 0]",
             "1[2, 0, 0, 0]",
             "2[0, 0.5, 0, 0]",
             "3[0, 0, 0.25, 0]",
             "4[0, 0, 0, 1]",
             "false",
             "[0.5, 0, 0, 2, 0, 0]",
             "1[0.5, 0, 0, 0]",
             "2[0, 2, 0, 0]",
             "3[0, 0, 4, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void inverse_general3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DUMP_FUNCTION
                + "let m = new DOMMatrixReadOnly([0.5,0,0,0,0,2,0,0,0,0,4,0,0,0,0,1]);"
                + "let inv = m.inverse();"
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
    @Alerts({"[21, 32, 13, 20, 10, 14]",
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
                + "dump(result);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"[386, 444, 274, 316, 50, 60]",
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
}
