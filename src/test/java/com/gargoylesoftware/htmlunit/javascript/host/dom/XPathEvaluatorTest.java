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
package com.gargoylesoftware.htmlunit.javascript.host.dom;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF78;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XPathEvaluator}.
 *
 * @author Marc Guillemot
 * @author Chuck Dumont
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class XPathEvaluatorTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function", "[object XPathEvaluator]", "[object XPathNSResolver]", "first", "second", ""},
            FF = {"function", "[object XPathEvaluator]", "[object HTMLHtmlElement]", "first", "second", ""},
            FF78 = {"function", "[object XPathEvaluator]", "[object HTMLHtmlElement]", "first", "second", ""},
            IE = {"undefined", "exception", ""})
    @NotYetImplemented({FF, FF78})
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
            + "  var nsResolver = xpe.createNSResolver(document.documentElement);\n"
            + "  res += nsResolver + '§';\n"
            + "  var result = xpe.evaluate('//span', document.body, nsResolver, 0, null);\n"
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
    @Alerts(DEFAULT = "exception",
            IE = "window.XPathEvaluator undefined")
    public void namespacesWithNodeInArray() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var xml = "
            + "  '<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            + "    <soap:books>"
            + "      <soap:book>"
            + "        <title>Immortality</title>"
            + "        <author>John Smith</author>"
            + "      </soap:book>"
            + "    </soap:books>"
            + "  </soap:Envelope>';\n"

            + "  function test() {\n"
            + "    if (window.XPathEvaluator) {\n"
            + "      var doc = (new DOMParser).parseFromString(xml, 'text/xml');\n"
            + "      var xpe = new XPathEvaluator();\n"
            + "      var nsResolver = xpe.createNSResolver(doc.documentElement);\n"
            + "      try {\n"
            + "        var result = xpe.evaluate('/soap:Envelope/soap:books/soap:book/title/text()', "
                                     + "[doc.documentElement], nsResolver, XPathResult.STRING_TYPE, null);\n"
            + "        log(result.stringValue);\n"
            + "      } catch(e) { log('exception'); }\n"
            + "    } else {\n"
            + "      log('window.XPathEvaluator undefined');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "Immortality",
            IE = "window.XPathEvaluator undefined")
    public void namespacesWithCustomNSResolver() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function nsResolver(prefix) {\n"
            + "    return {s : 'http://schemas.xmlsoap.org/soap/envelope/'}[prefix] || null;\n"
            + "  }\n"

            + "  var xml = "
            + "  '<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
            + "    <soap:books>"
            + "      <soap:book>"
            + "        <title>Immortality</title>"
            + "        <author>John Smith</author>"
            + "      </soap:book>"
            + "    </soap:books>"
            + "  </soap:Envelope>';\n"

            + "  function test() {\n"
            + "    if (window.XPathEvaluator) {\n"
            + "      var doc = (new DOMParser).parseFromString(xml, 'text/xml');\n"
            + "      var xpe = new XPathEvaluator();\n"
            + "      var result = xpe.evaluate('/s:Envelope/s:books/s:book/title/text()', "
                                   + "doc.documentElement, nsResolver, XPathResult.STRING_TYPE, null);\n"
            + "      log(result.stringValue);\n"
            + "    } else {\n"
            + "      log('window.XPathEvaluator undefined');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
