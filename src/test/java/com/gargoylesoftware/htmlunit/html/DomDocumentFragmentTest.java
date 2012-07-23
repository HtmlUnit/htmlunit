/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link DomDocumentFragment}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DomDocumentFragmentTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "<div/>", "undefined" }, DEFAULT = { "undefined", "undefined" })
    public void xml() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var doc = createXmlDocument();\n"
            + "    testFragment(doc);\n"
            + "    testFragment(document);\n"
            + "  }\n"
            + "  function testFragment(doc) {\n"
            + "    var fragment = doc.createDocumentFragment();\n"
            + "    var div = doc.createElement('div');\n"
            + "    fragment.appendChild(div);\n"
            + "    alert(fragment.xml);\n"
            + "  }\n"
            + "  function createXmlDocument() {\n"
            + "    if (document.implementation && document.implementation.createDocument)\n"
            + "      return document.implementation.createDocument('', '', null);\n"
            + "    else if (window.ActiveXObject)\n"
            + "      return new ActiveXObject('Microsoft.XMLDOM');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
