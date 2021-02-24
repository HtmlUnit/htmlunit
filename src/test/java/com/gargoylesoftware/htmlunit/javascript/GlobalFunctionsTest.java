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
package com.gargoylesoftware.htmlunit.javascript;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Test for functions/properties of the global object.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class GlobalFunctionsTest extends WebDriverTestCase {

    /**
     * Test for bug <a href="http://sourceforge.net/support/tracker.php?aid=2815674">2815674</a>
     * due to Rhino bug <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=501972">501972</a>
     * and for bug <a href="http://sourceforge.net/support/tracker.php?aid=2903514">2903514</a>
     * due to Rhino bug <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=531436">531436</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"7.89", "7.89"})
    public void parseFloat() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(parseFloat('\\n 7.89 '));\n"
            + "  log(parseFloat('7.89em'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for bug <a href="http://sourceforge.net/p/htmlunit/bugs/1563/">1563</a>.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "-2345", "1", "12", "NaN", "0", "1", "8", "9", "100", "0", "1", "8", "9", "100",
                "100", "NaN", "NaN"})
    public void parseInt() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log(parseInt('0'));\n"
            + "  log(parseInt(' 1 '));\n"
            + "  log(parseInt('-2345'));\n"
            + "  log(parseInt('1.23'));\n"
            + "  log(parseInt('12,3'));\n"
            + "  log(parseInt('abc'));\n"

            + "  log(parseInt('0'));\n"
            + "  log(parseInt(' 01 '));\n"
            + "  log(parseInt('08'));\n"
            + "  log(parseInt('09'));\n"
            + "  log(parseInt('0100'));\n"

            + "  log(parseInt('0', 10));\n"
            + "  log(parseInt(' 01 ', 10));\n"
            + "  log(parseInt('08', 10));\n"
            + "  log(parseInt('09', 10));\n"
            + "  log(parseInt('0100', 10));\n"

            + "  log(parseInt('100', 0));\n"
            + "  log(parseInt('100', -1));\n"
            + "  log(parseInt('100', -7));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"decodeURI: function", "decodeURIComponent: function", "encodeURI: function",
        "encodeURIComponent: function", "escape: function", "eval: function", "isFinite: function", "isNaN: function",
        "parseFloat: function", "parseInt: function", "unescape: function"})
    public void methods_common() throws Exception {
        final String[] methods = {"decodeURI", "decodeURIComponent", "encodeURI", "encodeURIComponent", "escape",
            "eval", "isFinite", "isNaN", "parseFloat", "parseInt", "unescape"};
        final String html = NativeDateTest.createHTMLTestMethods("this", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"isXMLName: undefined", "uneval: undefined"})
    public void methods_different() throws Exception {
        final String[] methods = {"isXMLName", "uneval"};
        final String html = NativeDateTest.createHTMLTestMethods("this", methods);
        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for #1439.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http%3A%2F%2Fw3schools.com%2Fmy%20test.asp%3Fname%3Dst%C3%A5le%26car%3Dsaab",
                "%E6%B5%8B%E8%A9%A6"})
    public void encodeURIComponent() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var uri='http://w3schools.com/my test.asp?name=st\u00E5le&car=saab';\n"
            + "    alert(encodeURIComponent(uri));\n"

            + "    uri='\\u6D4B\\u8A66';\n"
            + "    alert(encodeURIComponent(uri));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test case for #1439.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("%E6%B5%8B%E8%A9%A6")
    public void encodeURIComponentUtf8() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    uri='\u6D4B\u8A66';\n"
            + "    alert(encodeURIComponent(uri));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", UTF_8);
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * Test case for https://github.com/HtmlUnit/htmlunit/issues/94.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"\u00ee\u0010\u0043\u0072\u00f4\u00ef\u00b6\u0062\u0034",
                "\u00ee\u0010\u0043\u0072\u00f4\u00ef\u00b6\u0062\u0034"})
    public void decodeURIComponent() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var uri='%c3%ae%10%43%72%c3%b4%c3%af%c2%b6%62%34';\n"
            + "    alert(decodeURIComponent(uri));\n"

            + "    alert(decodeURIComponent(uri, false));\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
