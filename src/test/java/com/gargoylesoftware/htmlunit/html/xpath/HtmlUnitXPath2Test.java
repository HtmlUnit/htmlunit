/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.xpath;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;

/**
 * Tests for XPath evaluation on HtmlUnit DOM.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlUnitXPath2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"102", "111", "111", "160", "97", "110", "100", "160", "102", "111", "111"},
            IE = "error")
    public void optionText() throws Exception {
        final String content = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var expr = 'string(//option)';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    var value = result.stringValue;\n"
            + "    for (var i = 0; i < value.length; i++) {\n"
            + "      log(value.charCodeAt(i));\n"
            + "    }\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <select name='test'><option value='1'>foo&nbsp;and&nbsp;foo</option></select>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLParagraphElement][object HTMLDivElement]",
            IE = "error")
    public void pipe() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';"
            + "    var expr = '//p | //div';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <p></p>\n"
            + "  <div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "a",
            IE = "error")
    public void math() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';"
            + "    var expr = '//p[position()=(1+5-(2*2))div 2]';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div>\n"
            + "    <p id='a'></p>\n"
            + "    <p id='b'></p>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b",
            IE = "error")
    public void gt() throws Exception {
        compare("//p[position()>1]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "ab",
            IE = "error")
    public void gte() throws Exception {
        compare("//p[position()>=1]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "a",
            IE = "error")
    public void lt() throws Exception {
        compare("//p[position()<2]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "ab",
            IE = "error")
    public void lte() throws Exception {
        compare("//p[position()<=2]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b",
            IE = "error")
    public void eq() throws Exception {
        compare("//p[position()=2]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b",
            IE = "error")
    public void neq() throws Exception {
        compare("//p[position()!=1]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "a",
            IE = "error")
    public void and() throws Exception {
        compare("//p[@x>= 0 and @y=7]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "ab",
            IE = "error")
    public void or() throws Exception {
        compare("//p[@x>= 0 or @y>4]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "ab",
            IE = "error")
    public void mod() throws Exception {
        compare("//p[@y mod 6 = 1]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "'adc'",
            IE = "error")
    public void translate() throws Exception {
        compareStringValue("translate(\"abc\", \"b\", \"d\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "error")
    public void trueTest() throws Exception {
        compareBooleanValue("true()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "error")
    public void falseTest() throws Exception {
        compareBooleanValue("false()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "error")
    public void booleanTest() throws Exception {
        compareBooleanValue("boolean(7)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "error")
    public void booleanTestFalse() throws Exception {
        compareBooleanValue("boolean(0)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "b",
            IE = "error")
    public void number() throws Exception {
        compare("//p[@y=number(\"  13\t \")]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "'tml'",
            IE = "error")
    public void substring() throws Exception {
        compareStringValue("substring(\"HtmlUnit\", 2, 3)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "''",
            IE = "error")
    public void substringWithNegativeLength() throws Exception {
        compareStringValue("substring(\"HtmlUnit\", 2, -1)");
    }

    /** @throws Exception in case of problems */
    @Test
    @Alerts(DEFAULT = "''",
            IE = "error")
    public void substringNegativeStartWithLength() throws Exception {
        compareStringValue("substring(\"HtmlUnit\", -50, 20)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "'Unit'",
            IE = "error")
    public void substringAfter() throws Exception {
        compareStringValue("substring-after(\"HtmlUnit\", \"tml\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "'Html'",
            IE = "error")
    public void substringBefore() throws Exception {
        compareStringValue("substring-before(\"HtmlUnit\", \"Uni\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "false",
            IE = "error")
    public void not() throws Exception {
        compareBooleanValue("not(\"HtmlUnit\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "a",
            IE = "error")
    public void attrib() throws Exception {
        compare("//p[@x=1]");
    }

    private void compare(final String xpath) throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var expr = '" + xpath + "';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div>\n"
            + "    <p id='a' x='1' y='7'></p>\n"
            + "    <p id='b' y='13'></p>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    private void compareStringValue(final String xpath) throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var expr = '" + xpath + "';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    log(\"'\" + result.stringValue + \"'\");\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div>\n"
            + "    <p id='a' x='1' y='7'></p>\n"
            + "    <p id='b' y='13'></p>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    private void compareBooleanValue(final String xpath) throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var expr = '" + xpath + "';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    log(result.booleanValue);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div>\n"
            + "    <p id='a' x='1' y='7'></p>\n"
            + "    <p id='b' y='13'></p>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }
}
