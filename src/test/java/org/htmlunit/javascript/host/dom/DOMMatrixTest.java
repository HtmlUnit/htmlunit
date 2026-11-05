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
 * Tests for {@link DOMMatrix}.
 *
 * @author Ronald Brill
 */
public class DOMMatrixTest extends WebDriverTestCase {

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true", "function DOMMatrix() { [native code] }", "function DOMMatrix() { [native code] }"})
    public void webKitCSSMatrixIsAlias() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  log(WebKitCSSMatrix === DOMMatrix);\n"
                + "  log(WebKitCSSMatrix);\n"
                + "  log(DOMMatrix);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"false", "function SVGMatrix() { [native code] }", "function DOMMatrix() { [native code] }"})
    public void svgMatrixIsNotAlias() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function doTest() {\n"
                + "  log(SVGMatrix === DOMMatrix);\n"
                + "  log(SVGMatrix);\n"
                + "  log(DOMMatrix);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='doTest()'>\n"
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
    public void contructor() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "let m = new DOMMatrix();\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "let m = new DOMMatrix([6, 5, 4, 3, 2, 1]);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_2D_FUNCTION
                + "let m = new DOMMatrix([6, true, null, undefined, '2', 'eins']);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix([5, 4, 3, 2, 1]);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix([7, 6, 5, 4, 3, 2, 1]);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "let m = new DOMMatrix([16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix([15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix([17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix(7);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix(null);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix(undefined);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix([]);\n"
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
    @Alerts({"[1, 0, 0, 1, 0, 0]", "true"})
    public void inverse_identity2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_2D_FUNCTION
                + "let m = new DOMMatrix();"
                + "m.invertSelf();"
                + "dump(m);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true",
             "[0.25, 0, 0, 0.2, -2.5, -2.6]",
             "1[0.25, 0, 0, 0]",
             "2[0, 0.2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[-2.5, -2.6, 0, 1]",
             "true"})
    public void inverse_general2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "let m = new DOMMatrix([4, 0, 0, 5, 10, 13]);"
                + "m.invertSelf();"
                + "log(m instanceof DOMMatrix);\n"
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
                + DOMMatrixReadOnlyTest.DUMP_2D_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix([0, 0, 0, 0, 0, 0]);"
                + "  m.invertSelf();"
                + "  dump(m);"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "let m = new DOMMatrix([1,0,0,0,0,1,0,0,0,0,1,0,0,0,0,1]);"
                + "m.invertSelf();"
                + "dump(m);"
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
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {"
                + "  let m = new DOMMatrix([0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0]);"
                + "  m.invertSelf();"
                + "  dump(m);"
                + "} catch(e) { logEx(e); }"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"true",
             "[-0.066, 0.115, 0.295, -0.016, 0, 0]",
             "1[-0.066, 0.115, 0.115, -0.459]",
             "2[0.295, -0.016, -0.516, 0.066]",
             "3[0.148, -0.008, -0.008, 0.033]",
             "4[0, 0, 0, 1]",
             "false"})
    public void inverse_general3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><script>"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "let m = new DOMMatrix([0.5,0,7,0,9,2,0,4,0,-2,4,0,0,0,0,1]);"
                + "m.invertSelf();"
                + "log(m instanceof DOMMatrix);\n"
                + "dump(m);"
                + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[2, 0, 0, 2, 10, 20]",
             "1[2, 0, 0, 0]",
             "2[0, 2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 20, 0, 1]",
             "true"})
    public void contructorString() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix('translate(10px, 20px) scale(2)');\n"
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
    public void contructorMatrix() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m1 = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let m = new DOMMatrix(m1);\n"
                + "  dump(m);\n"
                + "  log(m === m1);\n"
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
             "[21, 32, 13, 20, 10, 14]",
             "1[21, 32, 0, 0]",
             "2[13, 20, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 14, 0, 1]",
             "true"})
    public void multiplySelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.multiplySelf(new DOMMatrix([6, 5, 4, 3, 2, 1]));\n"
                + "  log(r === m);\n"
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
    public void multiplySelfNoArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  m.multiplySelf();\n"
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
    @Alerts({"[2, 4, 6, 8, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[6, 8, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void multiplySelfDictionary() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  m.multiplySelf({a: 2, d: 2});\n"
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
             "3[0, 0, 3, 0]",
             "4[5, 6, 0, 1]",
             "false"})
    public void multiplySelf3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  m.multiplySelf({m33: 3});\n"
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
    public void multiplySelfInvalid() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  m.multiplySelf(42);\n"
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
    @Alerts({"true",
             "[14, 11, 34, 27, 56, 44]",
             "1[14, 11, 0, 0]",
             "2[34, 27, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[56, 44, 0, 1]",
             "true"})
    public void preMultiplySelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.preMultiplySelf(new DOMMatrix([6, 5, 4, 3, 2, 1]));\n"
                + "  log(r === m);\n"
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
    @Alerts({"[2, 0, 0, 2, 15, 7]",
             "1[2, 0, 0, 0]",
             "2[0, 2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[15, 7, 0, 1]",
             "true"})
    public void preMultiplySelfTranslate() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([2, 0, 0, 2, 10, 0]);\n"
                + "  m.preMultiplySelf({e: 5, f: 7});\n"
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
    @Alerts({"true",
             "[1, 2, 3, 4, 75, 106]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[75, 106, 0, 1]",
             "true"})
    public void translateSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.translateSelf(10, 20);\n"
                + "  log(r === m);\n"
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
    public void translateSelf3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.translateSelf(10, 20, 30);\n"
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
    public void translateSelfNoArgument() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  m.translateSelf();\n"
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
    @Alerts({"true",
             "[2, 4, 6, 8, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[6, 8, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void scaleSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.scaleSelf(2);\n"
                + "  log(r === m);\n"
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
    @Alerts({"[2, 0, 0, 3, -10, -40]",
             "1[2, 0, 0, 0]",
             "2[0, 3, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[-10, -40, 0, 1]",
             "true"})
    public void scaleSelfOrigin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.scaleSelf(2, 3, 1, 10, 20);\n"
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
    public void scaleSelf3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.scaleSelf(2, 3, 4);\n"
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
    @Alerts({"true",
             "[2, 4, 6, 8, 5, 6]",
             "1[2, 4, 0, 0]",
             "2[6, 8, 0, 0]",
             "3[0, 0, 2, 0]",
             "4[5, 6, 0, 1]",
             "false"})
    public void scale3dSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.scale3dSelf(2);\n"
                + "  log(r === m);\n"
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
    @Alerts({"[2, 0, 0, 2, -10, -20]",
             "1[2, 0, 0, 0]",
             "2[0, 2, 0, 0]",
             "3[0, 0, 2, 0]",
             "4[-10, -20, -30, 1]",
             "false"})
    public void scale3dSelfOrigin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.scale3dSelf(2, 10, 20, 30);\n"
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
    @Alerts({"true",
             "[3, 4, -1, -2, 5, 6]",
             "1[3, 4, 0, 0]",
             "2[-1, -2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotateSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.rotateSelf(90);\n"
                + "  log(r === m);\n"
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
    @Alerts({"[1, 0, 0, 0, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 0, 1, 0]",
             "3[0, -1, 0, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void rotateSelf3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.rotateSelf(90, 0, 0);\n"
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
    @Alerts({"[0.354, 0.612, -0.573, 0.739, 0, 0]",
             "1[0.354, 0.612, -0.707, 0]",
             "2[-0.573, 0.739, 0.354, 0]",
             "3[0.739, 0.28, 0.612, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void rotateSelfXYZ() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.rotateSelf(30, 45, 60);\n"
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
    @Alerts({"true",
             "[3, 4, -1, -2, 5, 6]",
             "1[3, 4, 0, 0]",
             "2[-1, -2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotateFromVectorSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.rotateFromVectorSelf(0, 1);\n"
                + "  log(r === m);\n"
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
    @Alerts({"true",
             "[3, 4, -1, -2, 5, 6]",
             "1[3, 4, 0, 0]",
             "2[-1, -2, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void rotateAxisAngleSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.rotateAxisAngleSelf(0, 0, 1, 90);\n"
                + "  log(r === m);\n"
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
    @Alerts({"[1, 0, 0, 0, 0, 0]",
             "1[1, 0, 0, 0]",
             "2[0, 0, 1, 0]",
             "3[0, -1, 0, 0]",
             "4[0, 0, 0, 1]",
             "false"})
    public void rotateAxisAngleSelf3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.rotateAxisAngleSelf(1, 0, 0, 90);\n"
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
    @Alerts({"true",
             "[1, 2, 4, 6, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[4, 6, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void skewXSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.skewXSelf(45);\n"
                + "  log(r === m);\n"
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
    @Alerts({"true",
             "[4, 6, 3, 4, 5, 6]",
             "1[4, 6, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void skewYSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.skewYSelf(45);\n"
                + "  log(r === m);\n"
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
    @Alerts({"true",
             "[0.5, 0, 0, 0.25, -5, -5]",
             "1[0.5, 0, 0, 0]",
             "2[0, 0.25, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[-5, -5, 0, 1]",
             "true"})
    public void invertSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([2, 0, 0, 4, 10, 20]);\n"
                + "  let r = m.invertSelf();\n"
                + "  log(r === m);\n"
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
    @Alerts({"[NaN, NaN, NaN, NaN, NaN, NaN]",
             "1[NaN, NaN, NaN, NaN]",
             "2[NaN, NaN, NaN, NaN]",
             "3[NaN, NaN, NaN, NaN]",
             "4[NaN, NaN, NaN, NaN]",
             "false"})
    public void invertSelfSingular() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([0, 0, 0, 0, 10, 20]);\n"
                + "  m.invertSelf();\n"
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
    @Alerts({"true",
             "[1, 0, 0, 1, 10, 20]",
             "1[1, 0, 0, 0]",
             "2[0, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 20, 0, 1]",
             "true"})
    public void setMatrixValue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.setMatrixValue('translate(10px, 20px)');\n"
                + "  log(r === m);\n"
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
    public void setMatrixValue3D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.setMatrixValue('translate3d(10px, 20px, 30px)');\n"
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
    public void setMatrixValueBackTo2D() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16]);\n"
                + "  m.setMatrixValue('scale(2)');\n"
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
    @Alerts(DEFAULT = {"[1, 0, 0, 1, 0, 0]",
                       "1[1, 0, 0, 0]",
                       "2[0, 1, 0, 0]",
                       "3[0, 0, 1, 0]",
                       "4[0, 0, 0, 1]",
                       "true"},
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
    public void setMatrixValueEmpty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  m.setMatrixValue('');\n"
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
    @Alerts({"SyntaxError/DOMException",
             "[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true"})
    public void setMatrixValueInvalid() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  try {\n"
                + "    m.setMatrixValue('invalid');\n"
                + "  } catch(e) { logEx(e); }\n"
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
    @Alerts({"[2, 3, 4, 5, 6, 7]",
             "1[2, 3, 0, 0]",
             "2[4, 5, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[6, 7, 0, 1]",
             "true",
             "[1, 3, 4, 1, 0, 7]",
             "1[1, 3, 0, 0]",
             "2[4, 1, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[0, 7, 0, 1]",
             "true"})
    public void setter2DValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.a = 2; m.b = 3; m.c = 4; m.d = 5; m.e = 6; m.f = 7;\n"
                + "  dump(m);\n"
                + "  m.m11 = 1; m.m22 = 1; m.m41 = 0;\n"
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
    @Alerts({"m13 true false",
             "m14 true false",
             "m23 true false",
             "m24 true false",
             "m31 true false",
             "m32 true false",
             "m34 true false",
             "m43 true false"})
    public void setter3DValues() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let names = ['m13', 'm14', 'm23', 'm24', 'm31', 'm32', 'm34', 'm43'];\n"
                + "  for (let name of names) {\n"
                + "    let m = new DOMMatrix();\n"
                + "    m[name] = 0;\n"
                + "    let before = m.is2D;\n"
                + "    m[name] = 1;\n"
                + "    log(name + ' ' + before + ' ' + m.is2D);\n"
                + "  }\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"m33 true false", "m44 true false"})
    public void setter3DValuesOne() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let names = ['m33', 'm44'];\n"
                + "  for (let name of names) {\n"
                + "    let m = new DOMMatrix();\n"
                + "    m[name] = 1;\n"
                + "    let before = m.is2D;\n"
                + "    m[name] = 2;\n"
                + "    log(name + ' ' + before + ' ' + m.is2D);\n"
                + "  }\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void setter3DValuesBackToZero() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.m13 = 1;\n"
                + "  m.m13 = 0;\n"
                + "  log(m.is2D);\n"
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
             "true",
             "true"})
    public void fromMatrix() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrix.fromMatrix({a: 2, d: 3});\n"
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
    @Alerts({"[1, 2, 3, 4, 5, 6]",
             "1[1, 2, 0, 0]",
             "2[3, 4, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[5, 6, 0, 1]",
             "true",
             "true",
             "1"})
    public void fromMatrixReadOnly() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let other = new DOMMatrixReadOnly([1, 2, 3, 4, 5, 6]);\n"
                + "  let m = DOMMatrix.fromMatrix(other);\n"
                + "  dump(m);\n"
                + "  log(m instanceof DOMMatrix);\n"
                + "  m.a = 7;\n"
                + "  log(other.a);\n"
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
             "true"})
    public void fromFloat32Array() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = DOMMatrix.fromFloat32Array(new Float32Array([1, 2, 3, 4, 5, 6]));\n"
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
    @Alerts({"[1, 2, 5, 6, 13, 14]",
             "1[1, 2, 3, 4]",
             "2[5, 6, 7, 8]",
             "3[9, 10, 11, 12]",
             "4[13, 14, 15, 16]",
             "false",
             "true"})
    public void fromFloat64Array() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let values = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16];\n"
                + "  let m = DOMMatrix.fromFloat64Array(new Float64Array(values));\n"
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
    @Alerts({"[0, 2, -2, 0, 10, 20]",
             "1[0, 2, 0, 0]",
             "2[-2, 0, 0, 0]",
             "3[0, 0, 1, 0]",
             "4[10, 20, 0, 1]",
             "true"})
    public void selfChain() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix();\n"
                + "  m.translateSelf(10, 20).scaleSelf(2).rotateSelf(90).skewXSelf(0);\n"
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
    @Alerts("true")
    public void selfEqualsNotSelf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + DOMMatrixReadOnlyTest.DUMP_FUNCTION
                + "try {\n"
                + "  let m = new DOMMatrix([1, 2, 3, 4, 5, 6]);\n"
                + "  let r = m.translate(5, 6).scale(2).rotate(30);\n"
                + "  m.translateSelf(5, 6).scaleSelf(2).rotateSelf(30);\n"
                + "  log(r.toString() == m.toString());\n"
                + "} catch(e) { logEx(e); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
