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
        final String content = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + "  try {\n"
            + "    var expr = 'string(//option)';\n"
            + "    var result = document.evaluate(expr, document.documentElement, null, XPathResult.ANY_TYPE, null);\n"
            + "    var value = result.stringValue;\n"
            + "    for (var i = 0; i < value.length; i++) {\n"
            + "      alert(value.charCodeAt(i));\n"
            + "    }\n"
            + "  } catch (e) {alert('error')}\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <select name='test'><option value='1'>foo&nbsp;and&nbsp;foo</option></select>\n"
            + "</body></html>";

        loadPageWithAlerts2(content);
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

    private void compare(final String xpath) throws Exception {
        final String content = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  try {\n"
            + "    var res = '';"
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
            + "    <p id='a'></p>\n"
            + "    <p id='b'></p>\n"
            + "  </div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(content);
    }

}
