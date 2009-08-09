/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link Node}, but with WebDriver.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class Node2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "4", "3", "abc", "def", "123456", "false", "0", "2", "123", "456", "1", "false" },
            FF = { "4", "3", "abc", "def", "123456", "true", "0", "2", "123", "456", "1", "true" })
    public void normalize() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    var root = doc.appendChild(doc.createElement('root'));\n"
            + "    var cdata = root.appendChild(doc.createCDATASection('abcdef'));\n"
            + "    cdata.splitText(3);\n"
            + "    var text = root.appendChild(doc.createTextNode('123456'));\n"
            + "    text.splitText(3);\n"
            + "    alert(root.childNodes.length);\n"
            + "    root.normalize();\n"
            + "    alert(root.childNodes.length);\n"
            + "    alert(root.childNodes.item(0).data);\n"
            + "    alert(root.childNodes.item(1).data);\n"
            + "    alert(root.childNodes.item(2).data);\n"
            + "    alert(root.childNodes.item(2) == text);\n"
            + "\n"
            + "    var body = document.body;\n"
            + "    alert(body.childNodes.length);\n"
            + "    text = body.appendChild(document.createTextNode('123456'));\n"
            + "    text.splitText(3);\n"
            + "    alert(body.childNodes.length);\n"
            + "    alert(body.childNodes.item(0).nodeValue);\n"
            + "    alert(body.childNodes.item(1).nodeValue);\n"
            + "    body.normalize();\n"
            + "    alert(body.childNodes.length);\n"
            + "    alert(body.childNodes.item(0) == text);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";
        loadPageWithAlerts2(html);
    }
}
