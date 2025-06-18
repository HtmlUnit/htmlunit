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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Number is a native JavaScript object.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class NativeNumberTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "false", "false", "true", "true", "false", "false"})
    public void isFinite() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    if (Number.isFinite === undefined) {\n"
            + "      log('no Number.isFinite');\n"
            + "    } else {\n"
            + "      log(Number.isFinite(Infinity));\n"
            + "      log(Number.isFinite(NaN));\n"
            + "      log(Number.isFinite(-Infinity));\n"
            + "      log(Number.isFinite(0));\n"
            + "      log(Number.isFinite(2e64));\n"
            + "      log(Number.isFinite('0'));\n"
            + "      log(Number.isFinite(null));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "false", "false", "false", "false",
             "false", "false", "false", "false", "false"})
    public void isInteger() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    if (Number.isInteger === undefined) {\n"
            + "      log('no Number.isInteger');\n"
            + "    } else {\n"
            + "      log(Number.isInteger(0));\n"
            + "      log(Number.isInteger(1));\n"
            + "      log(Number.isInteger(-100000));\n"

            + "      log(Number.isInteger(0.1));\n"
            + "      log(Number.isInteger(Math.PI));\n"

            + "      log(Number.isInteger(NaN));\n"
            + "      log(Number.isInteger(Infinity));\n"
            + "      log(Number.isInteger(-Infinity));\n"
            + "      log(Number.isInteger('10'));\n"
            + "      log(Number.isInteger(true));\n"
            + "      log(Number.isInteger(false));\n"
            + "      log(Number.isInteger([1]));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "false", "false", "false", "false",
             "false", "false", "false", "false", "false", "false", "false"})
    public void isNaN() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    if (Number.isNaN === undefined) {\n"
            + "      log('no Number.isNaN');\n"
            + "    } else {\n"
            + "      log(Number.isNaN(NaN));\n"
            + "      log(Number.isNaN(Number.NaN));\n"
            + "      log(Number.isNaN(0 / 0));\n"

            + "      log(Number.isNaN('NaN'));\n"
            + "      log(Number.isNaN(undefined));\n"
            + "      log(Number.isNaN({}));\n"
            + "      log(Number.isNaN('blabla'));\n"

            + "      log(Number.isNaN(true));\n"
            + "      log(Number.isNaN(null));\n"
            + "      log(Number.isNaN(37));\n"
            + "      log(Number.isNaN('37'));\n"
            + "      log(Number.isNaN('37.37'));\n"
            + "      log(Number.isNaN(''));\n"
            + "      log(Number.isNaN(' '));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false", "true", "false", "false", "false", "false", "true"})
    public void isSafeInteger() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    if (Number.isSafeInteger === undefined) {\n"
            + "      log('no Number.isSafeInteger');\n"
            + "    } else {\n"
            + "      log(Number.isSafeInteger(3));\n"
            + "      log(Number.isSafeInteger(Math.pow(2, 53)));\n"
            + "      log(Number.isSafeInteger(Math.pow(2, 53) - 1));\n"
            + "      log(Number.isSafeInteger(NaN));\n"
            + "      log(Number.isSafeInteger(Infinity));\n"
            + "      log(Number.isSafeInteger('3'));\n"
            + "      log(Number.isSafeInteger(3.1));\n"
            + "      log(Number.isSafeInteger(3.0));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3.14")
    public void parseFloat() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    if (Number.parseFloat === undefined) {\n"
            + "      log('no Number.parseFloat');\n"
            + "    } else {\n"
            + "      log(Number.parseFloat('3.14'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("4")
    public void parseInt() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    if (Number.parseInt === undefined) {\n"
            + "      log('no Number.parseInt');\n"
            + "    } else {\n"
            + "      log(Number.parseInt('4'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"toFixed: function", "toExponential: function", "toLocaleString: function", "toPrecision: function",
        "toString: function", "valueOf: function"})
    public void methods_common() throws Exception {
        final String[] methods = {"toFixed", "toExponential", "toLocaleString", "toPrecision", "toString", "valueOf"};
        final String html = NativeDateTest.createHTMLTestMethods("new Number()", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("toSource: undefined")
    public void methods_different() throws Exception {
        final String html = NativeDateTest.createHTMLTestMethods("new Number()", "toSource");
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for Rhino bug 538172.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2.274341322658976e-309")
    public void toStringRhinoBug538172() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(2.274341322658976E-309);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12,345")
    public void toLocaleString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((12345).toLocaleString('en'));\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12.345")
    public void toLocaleStringDe() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((12345).toLocaleString('de'));\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12,345")
    public void toLocaleStringEnUS() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log((12345).toLocaleString('en-US'));\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12,345")
    public void toLocaleStringNoParam() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log((12345).toLocaleString());\n"
            + "  } catch(e) { log(e); }\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("RangeError")
    public void toLocaleStringHintertupfingen() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log((12345).toLocaleString('Hintertupfingen'));\n"
            + "  } catch(e) { logEx(e); }\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
