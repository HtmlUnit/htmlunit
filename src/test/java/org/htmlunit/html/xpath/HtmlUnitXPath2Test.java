/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.html.xpath;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

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
    @Alerts({"102", "111", "111", "160", "97", "110", "100", "160", "102", "111", "111"})
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
    @Alerts("[object HTMLParagraphElement][object HTMLDivElement]")
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
    @Alerts("a")
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
    @Alerts("b")
    public void gt() throws Exception {
        compare("//p[position()>1]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void gte() throws Exception {
        compare("//p[position()>=1]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void lt() throws Exception {
        compare("//p[position()<2]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void lte() throws Exception {
        compare("//p[position()<=2]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b")
    public void eq() throws Exception {
        compare("//p[position()=2]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b")
    public void neq() throws Exception {
        compare("//p[position()!=1]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void and() throws Exception {
        compare("//p[@x>= 0 and @y=7]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void or() throws Exception {
        compare("//p[@x>= 0 or @y>4]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void mod() throws Exception {
        compare("//p[@y mod 6 = 1]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'adc'")
    public void translate() throws Exception {
        compareStringValue("translate(\"abc\", \"b\", \"d\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void trueTest() throws Exception {
        compareBooleanValue("true()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void falseTest() throws Exception {
        compareBooleanValue("false()");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void booleanTest() throws Exception {
        compareBooleanValue("boolean(7)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void booleanTestFalse() throws Exception {
        compareBooleanValue("boolean(0)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b")
    public void number() throws Exception {
        compare("//p[@y=number(\"  13\t \")]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'tml'")
    public void substring() throws Exception {
        compareStringValue("substring(\"HtmlUnit\", 2, 3)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("''")
    public void substringWithNegativeLength() throws Exception {
        compareStringValue("substring(\"HtmlUnit\", 2, -1)");
    }

    /** @throws Exception in case of problems */
    @Test
    @Alerts("''")
    public void substringNegativeStartWithLength() throws Exception {
        compareStringValue("substring(\"HtmlUnit\", -50, 20)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'Unit'")
    public void substringAfter() throws Exception {
        compareStringValue("substring-after(\"HtmlUnit\", \"tml\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'Html'")
    public void substringBefore() throws Exception {
        compareStringValue("substring-before(\"HtmlUnit\", \"Uni\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void not() throws Exception {
        compareBooleanValue("not(\"HtmlUnit\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void attrib() throws Exception {
        compare("//p[@x=1]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("error")
    public void lowerCaseNotSupported() throws Exception {
        compare("//*[lower-case(@id) = \"a\"]");
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("mySpan")
    public void minimalParameters() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//span', document.documentElement);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' attr='false'>false</span>\n"
            + "  <span id='mySpan' attr='true'>true</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("mySpan")
    public void undefinedResult() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//span', "
                        + "document.documentElement, null, XPathResult.ANY_TYPE, undefined);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' attr='false'>false</span>\n"
            + "  <span id='mySpan' attr='true'>true</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("error")
    public void stringResult() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//span', "
                        + "document.documentElement, null, XPathResult.ANY_TYPE, 'abcd');\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' attr='false'>false</span>\n"
            + "  <span id='mySpan' attr='true'>true</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("mySpan")
    public void objectResult() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//span', "
                        + "document.documentElement, null, XPathResult.ANY_TYPE, {});\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' attr='false'>false</span>\n"
            + "  <span id='mySpan' attr='true'>true</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "mySpan - - myDiv",
            FF = "mySpan - myDiv - ",
            FF_ESR = "mySpan - myDiv - ")
    public void reuseResult() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//span', "
                        + "document.documentElement, null, XPathResult.ANY_TYPE);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    res += ' - ';\n"

            + "    var result2 = document.evaluate('//div', "
                        + "document.documentElement, null, XPathResult.ANY_TYPE, result);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    res += ' - ';\n"

            + "    while (node = result2.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"

            + "    log(res);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' attr='false'>false</span>\n"
            + "  <span id='mySpan' attr='true'>true</span>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("myDiv1")
    public void documentEvaluateFirst() throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//div', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE);\n"
            + "    log(result.singleNodeValue.id);\n"
            + "  } catch (e) {log('error')}\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv1'>div1</div>\n"
            + "  <div id='myDiv2'>div1</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }
}
