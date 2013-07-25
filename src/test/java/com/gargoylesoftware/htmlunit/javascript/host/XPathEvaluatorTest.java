/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link XPathEvaluator}.
 *
 * @version $Revision$
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
    @Alerts(DEFAULT = { "function", "[object XPathEvaluator]", "[object XPathNSResolver]", "first", "second" },
            IE = { "undefined", "exception" }, FF3_6 = { "undefined", "exception" })
    public void simple() throws Exception {
        final String html = "<html><body>\n"
            + "<span id='first'>hello</span>\n"
            + "<div><span id='second'>world</span></div>\n"
            + "<script>\n"
            + "try {\n"
            + "  alert(typeof window.XPathEvaluator);\n"
            + "  var xpe = new XPathEvaluator();\n"
            + "  alert(xpe);\n"
            + "  var nsResolver = xpe.createNSResolver(document.documentElement);\n"
            + "  alert(nsResolver);\n"
            + "  var result = xpe.evaluate('//span', document.body, nsResolver, 0, null);\n"
            + "  var found = [];\n"
            + "  var res;\n"
            + "  while (res = result.iterateNext()) {\n"
            + "    alert(res.id);\n"
            + "  }\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Immortality" }, IE = { }, FF3_6 = { })
    public void namespacesWithNodeInArray() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
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
            + "    if (window.XPathEvaluator) {"
            + "    var doc = (new DOMParser).parseFromString(xml);"
            + "    var xpe = new XPathEvaluator();\n"
            + "    var nsResolver = xpe.createNSResolver(doc.documentElement);\n"
            + "    var result = xpe.evaluate('/soap:Envelope/soap:books/soap:book/title/text()', "
                            + "[doc.documentElement], nsResolver, XPathResult.STRING_TYPE, null);\n"
            + "    alert(result.stringValue);\n"
            + "  }}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "Immortality" }, IE = { }, FF3_6 = { })
    public void namespacesWithCustomNSResolver() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "     function nsResolver(prefix) {"
            + "    return {s : 'http://schemas.xmlsoap.org/soap/envelope/'}[prefix] || null;"
            + "  }"
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
            + "    if (window.XPathEvaluator) {"
            + "    var doc = (new DOMParser).parseFromString(xml);"
            + "    var xpe = new XPathEvaluator();\n"
            + "    var result = xpe.evaluate('/s:Envelope/s:books/s:book/title/text()', "
                            + "doc.documentElement, nsResolver, XPathResult.STRING_TYPE, null);\n"
            + "    alert(result.stringValue);\n"
            + "  }}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
