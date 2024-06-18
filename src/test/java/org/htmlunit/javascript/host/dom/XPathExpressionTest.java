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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link XPathExpression}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XPathExpressionTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "[object XPathEvaluator]", "[object XPathExpression]", "first", "second", ""})
    public void simple() throws Exception {
        final String html = "<html><body>\n"
            + "<span id='first'>hello</span>\n"
            + "<div><span id='second'>world</span></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var res = '';\n"
            + "try {\n"
            + "  res += typeof window.XPathEvaluator + '§';\n"
            + "  var xpe = new XPathEvaluator();\n"
            + "  res += xpe + '§';\n"
            + "  var expression = xpe.createExpression('//span');\n"
            + "  res += expression + '§';\n"
            + "  var result = expression.evaluate(document.body);\n"
            + "  var found = [];\n"
            + "  var next;\n"
            + "  while (next = result.iterateNext()) {\n"
            + "    res += next.id + '§';\n"
            + "  }\n"
            + "} catch(e) { res += 'exception' + '§'; }\n"
            + "log(res);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "[object HTMLHtmlElement]", "[object XPathEvaluator]",
             "[object XPathExpression]", "first", "second", ""})
    public void withResolver() throws Exception {
        final String html = "<html><body>\n"
            + "<span id='first'>hello</span>\n"
            + "<div><span id='second'>world</span></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var res = '';\n"
            + "try {\n"
            + "  res += typeof window.XPathEvaluator + '§';\n"
            + "  var xpe = new XPathEvaluator();\n"
            + "  var nsResolver = xpe.createNSResolver(document.documentElement);\n"
            + "  res += nsResolver + '§';\n"
            + "  res += xpe + '§';\n"
            + "  var expression = xpe.createExpression('//span', nsResolver);\n"
            + "  res += expression + '§';\n"
            + "  var result = expression.evaluate(document.body);\n"
            + "  var found = [];\n"
            + "  var next;\n"
            + "  while (next = result.iterateNext()) {\n"
            + "    res += next.id + '§';\n"
            + "  }\n"
            + "} catch(e) { res += 'exception' + e + '§'; }\n"
            + "log(res);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }
}
