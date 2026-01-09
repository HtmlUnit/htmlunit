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
}
