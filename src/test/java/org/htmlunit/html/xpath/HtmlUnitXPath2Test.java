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
package org.htmlunit.html.xpath;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for XPath evaluation on HtmlUnit DOM.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlUnitXPath2Test extends WebDriverTestCase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isWebClientCached() {
        return true;
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"4", "null"})
    public void xPathNull() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var node = '';"
            + "    var expr = null;\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    node = result.iterateNext();\n"
            + "    log(result.resultType);\n"
            + "    log(node);\n"
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts({"4", "null"})
    public void xPathUndefined() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var node = '';"
            + "    var expr = undefined;\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    node = result.iterateNext();\n"
            + "    log(result.resultType);\n"
            + "    log(node);\n"
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts({"102", "111", "111", "160", "97", "110", "100", "160", "102", "111", "111"})
    public void optionText() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head>\n"
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
            + "  } catch(e) { logEx(e) }\n"
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
        final String content = DOCTYPE_HTML
            + "<html>\n"
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
            + "  } catch(e) { logEx(e) }\n"
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
        final String content = DOCTYPE_HTML
            + "<html>\n"
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
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts("false")
    public void startsWith() throws Exception {
        compareBooleanValue("starts-with(\"haystack\", \"needle\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void startsWithFound() throws Exception {
        compareBooleanValue("starts-with(\"haystack\", \"hay\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void startsWithWhole() throws Exception {
        compareBooleanValue("starts-with(\"haystack\", \"haystack\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void startsWithEmpty() throws Exception {
        compareBooleanValue("starts-with(\"haystack\", \"\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void startsWithEmptyEmpty() throws Exception {
        compareBooleanValue("starts-with(\"\", \"\")");
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

    /**
     * @throws Exception in case of problems
     */
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
    @Alerts("SyntaxError/DOMException")
    public void lowerCaseNotSupported() throws Exception {
        compareError("//*[lower-case(@id) = \"a\"]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void upperCaseNotSupported() throws Exception {
        compareError("//*[upper-case(@id) = \"A\"]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void endsWithNotSupported() throws Exception {
        compareError("ends-with(\"haystack\", \"haystack\")");
    }

    private void compare(final String xpath) throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
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
            + "  } catch(e) { logEx(e) }\n"
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
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var expr = '" + xpath + "';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    log(\"'\" + result.stringValue + \"'\");\n"
            + "  } catch(e) { logEx(e) }\n"
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
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var expr = '" + xpath + "';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    log(result.booleanValue);\n"
            + "  } catch(e) { logEx(e) }\n"
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

    private void compareError(final String xpath) throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var expr = '" + xpath + "';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    log('error expected');\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("mySpan")
    public void minimalParameters() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
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
            + "  } catch(e) { logEx(e) }\n"
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
        final String content = DOCTYPE_HTML
            + "<html>\n"
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
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts("TypeError")
    public void stringResult() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
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
            + "  } catch(e) { logEx(e) }\n"
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
        final String content = DOCTYPE_HTML
            + "<html>\n"
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
            + "  } catch(e) { logEx(e) }\n"
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
        final String content = DOCTYPE_HTML
            + "<html>\n"
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
            + "  } catch(e) { logEx(e) }\n"
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
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//div', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE);\n"
            + "    log(result.singleNodeValue.id);\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv1'>div1</div>\n"
            + "  <div id='myDiv2'>div1</div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    // -----------------------------------------------------------------------
    // String functions
    // -----------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'HtmlUnit'")
    public void concat() throws Exception {
        compareStringValue("concat(\"Html\", \"Unit\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void containsFound() throws Exception {
        compareBooleanValue("contains(\"HtmlUnit\", \"mlU\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void containsNotFound() throws Exception {
        compareBooleanValue("contains(\"HtmlUnit\", \"xyz\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void containsEmpty() throws Exception {
        compareBooleanValue("contains(\"HtmlUnit\", \"\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'8'")
    public void stringLength() throws Exception {
        compareNumberValue("string-length(\"HtmlUnit\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'0'")
    public void stringLengthEmpty() throws Exception {
        compareNumberValue("string-length(\"\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'foo bar'")
    public void normalizeSpace() throws Exception {
        compareStringValue("normalize-space(\"  foo   bar  \")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("''")
    public void normalizeSpaceEmpty() throws Exception {
        compareStringValue("normalize-space(\"\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'42'")
    public void stringOfNumber() throws Exception {
        compareStringValue("string(42)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'true'")
    public void stringOfTrue() throws Exception {
        compareStringValue("string(true())");
    }

    // -----------------------------------------------------------------------
    // Number functions
    // -----------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'20'")
    public void sumNodes() throws Exception {
        compareNumberValue("sum(//p/@y)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'3'")
    public void floorPositive() throws Exception {
        compareNumberValue("floor(3.7)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'-4'")
    public void floorNegative() throws Exception {
        compareNumberValue("floor(-3.2)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'4'")
    public void ceilingPositive() throws Exception {
        compareNumberValue("ceiling(3.2)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'-3'")
    public void ceilingNegative() throws Exception {
        compareNumberValue("ceiling(-3.7)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'4'")
    public void roundHalf() throws Exception {
        compareNumberValue("round(3.5)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'3'")
    public void roundDown() throws Exception {
        compareNumberValue("round(3.4)");
    }

    /**
     * @throws Exception if the test fails
     * XPath 1.0: round(-3.5) returns -3 (closest to positive infinity)
     */
    @Test
    @Alerts("'-3'")
    public void roundNegative() throws Exception {
        compareNumberValue("round(-3.5)");
    }

    // -----------------------------------------------------------------------
    // Node-set functions
    // -----------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'2'")
    public void countNodes() throws Exception {
        compareNumberValue("count(//p)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'0'")
    public void countNodesZero() throws Exception {
        compareNumberValue("count(//span)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b")
    public void lastInContext() throws Exception {
        compare("//p[last()]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "'p'",
            FF = "'P'",
            FF_ESR = "'P'")
    @HtmlUnitNYI(FF = "'p'",
            FF_ESR = "'p'")
    public void nameFunction() throws Exception {
        compareStringValue("name(//p[1])");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'p'")
    public void localNameFunction() throws Exception {
        compareStringValue("local-name(//p[1])");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("'http://www.w3.org/1999/xhtml'")
    @HtmlUnitNYI(CHROME = "''",
            EDGE = "''",
            FF = "''",
            FF_ESR = "''")
    public void namespaceUri() throws Exception {
        compareStringValue("namespace-uri(//p[1])");
    }

    // -----------------------------------------------------------------------
    // Node tests / axes
    // -----------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello")
    public void textNodeTest() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var result = document.evaluate('//p[@id=\"a\"]/text()',"
                        + " document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    var node = result.iterateNext();\n"
            + "    log(node ? node.nodeValue : 'null');\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div>\n"
            + "    <p id='a'>hello</p>\n"
            + "    <p id='b'>world</p>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    /**
     * Function count(@*) counts all attributes including id; p[a] has id,x,y=3, p[b] has id,y=2.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b")
    public void attributeWildcard() throws Exception {
        compare("//p[count(@*)=2]");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void selfAxis() throws Exception {
        compare("//p[@id=\"a\"]/self::p");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("d")
    public void parentAxis() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//p[@id=\"a\"]/parent::div',"
                        + " document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d'>\n"
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
    @Alerts("true")
    public void ancestorAxis() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var result = document.evaluate('//p[@id=\"a\"]/ancestor::div',"
                        + " document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    log(result.iterateNext() != null);\n"
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts("b")
    public void followingSiblingAxis() throws Exception {
        compare("//p[@id=\"a\"]/following-sibling::p");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void precedingSiblingAxis() throws Exception {
        compare("//p[@id=\"b\"]/preceding-sibling::p");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void descendantAxis() throws Exception {
        compare("//div/descendant::p");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void childAxis() throws Exception {
        compare("//div/child::p");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "d3d2d1",
            FF = "d1d2d3",
            FF_ESR = "d1d2d3")
    @HtmlUnitNYI(CHROME = "d1d2d3",
            EDGE= "d1d2d3")
    public void anchestorOrder() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    let res = '';\n"
            + "    let tester = document.getElementById('d4');\n"
            + "    let result = document.evaluate('ancestor::div',"
                        + " tester, null, XPathResult.ANY_TYPE, null);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d1'>\n"
            + "    <div id='d2'>\n"
            + "      <div id='d3'>\n"
            + "        <div id='d4'>Tester</div>\n"
            + "      </div>\n"
            + "    </div>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

    // -----------------------------------------------------------------------
    // XPathResult types
    // -----------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void numberType() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var result = document.evaluate('count(//p)', document.documentElement, null,"
                        + " XPathResult.NUMBER_TYPE, null);\n"
            + "    log(result.numberValue);\n"
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts("a")
    public void stringType() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var result = document.evaluate('string(//p[1]/@id)', document.documentElement, null,"
                        + " XPathResult.STRING_TYPE, null);\n"
            + "    log(result.stringValue);\n"
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts("true")
    public void booleanType() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var result = document.evaluate('count(//p)>1', document.documentElement, null,"
                        + " XPathResult.BOOLEAN_TYPE, null);\n"
            + "    log(result.booleanValue);\n"
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts("ab")
    public void orderedNodeIteratorType() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//p', document.documentElement, null,"
                        + " XPathResult.ORDERED_NODE_ITERATOR_TYPE, null);\n"
            + "    while (node = result.iterateNext()) {\n"
            + "      res += node.id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts("ab")
    public void orderedNodeSnapshotType() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//p', document.documentElement, null,"
                        + " XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n"
            + "    for (var i = 0; i < result.snapshotLength; i++) {\n"
            + "      res += result.snapshotItem(i).id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts("true")
    public void anyUnorderedNodeType() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var result = document.evaluate('//p', document.documentElement, null,"
                        + " XPathResult.ANY_UNORDERED_NODE_TYPE, null);\n"
            + "    log(result.singleNodeValue != null);\n"
            + "  } catch(e) { logEx(e) }\n"
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
    @Alerts({"2", "ab"})
    public void snapshotApi() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var result = document.evaluate('//p', document.documentElement, null,"
                        + " XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);\n"
            + "    log(result.snapshotLength);\n"
            + "    for (var i = 0; i < result.snapshotLength; i++) {\n"
            + "      res += result.snapshotItem(i).id;\n"
            + "    }\n"
            + "    log(res);\n"
            + "  } catch(e) { logEx(e) }\n"
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

    // -----------------------------------------------------------------------
    // XPath 2.0 / unsupported functions
    // -----------------------------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void matchesNotSupported() throws Exception {
        compareError("matches(\"haystack\", \"hay.*\")");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SyntaxError/DOMException")
    public void distinctValuesNotSupported() throws Exception {
        compareError("distinct-values(//p/@y)");
    }

    // -----------------------------------------------------------------------
    // Private helpers
    // -----------------------------------------------------------------------

    private void compareNumberValue(final String xpath) throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';\n"
            + "    var expr = '" + xpath + "';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    log(\"'\" + result.numberValue + \"'\");\n"
            + "  } catch(e) { logEx(e) }\n"
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
