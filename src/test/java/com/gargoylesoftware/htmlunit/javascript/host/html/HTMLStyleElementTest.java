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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLStyleElement}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLStyleElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "[object]", "undefined", "[object]" },
            FF = { "[object HTMLStyleElement]", "[object CSSStyleSheet]", "undefined" })
    public void stylesheet() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var f = document.getElementById('myStyle');\n"
            + "  alert(f);\n"
            + "  alert(f.sheet);\n"
            + "  alert(f.styleSheet);\n"
            + "}</script>\n"
            + "<style id='myStyle'>p: vertical-align:top</style>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * As of HtmlUnit-2.5 only the first child node of a STYLE was parsed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void styleChildren() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var doc = document;\n"
            + "  var style = doc.createElement('style');\n"
            + "  doc.documentElement.firstChild.appendChild(style);\n"
            + "  style.appendChild(doc.createTextNode('* { z-index: 0; }\\\n'));\n"
            + "  style.appendChild(doc.createTextNode('DIV { z-index: 10; position: absolute; }\\\n'));\n"
            + "  if (doc.styleSheets[0].cssRules)\n"
            + "    rules = doc.styleSheets[0].cssRules;\n"
            + "  else\n"
            + "    rules = doc.styleSheets[0].rules;\n"
            + "  alert(rules.length);\n"
            + "}</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
