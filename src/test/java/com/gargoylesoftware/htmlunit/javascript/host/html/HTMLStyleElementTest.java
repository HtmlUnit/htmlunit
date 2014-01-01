/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLStyleElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "[object HTMLStyleElement]", "[object CSSStyleSheet]", "undefined" },
            IE8 = { "[object]", "undefined", "[object]" })
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ ".a > .t { }", ".b > .t { }", ".c > .t { }" })
    public void innerHtml() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"

            + "<style id='style_none'>.a > .t { }</style>\n"
            + "<style type='text/test' id='style_text'>.b > .t { }</style>\n"
            + "<style type='text/html' id='style_html'>.c > .t { }</style>\n"

            + "<script>\n"
            + "function doTest() {\n"
            + "  style = document.getElementById('style_none');\n"
            + "  alert(style.innerHTML);\n"
            + "  style = document.getElementById('style_text');\n"
            + "  alert(style.innerHTML);\n"
            + "  style = document.getElementById('style_html');\n"
            + "  alert(style.innerHTML);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "text/test", "text/css" })
    public void type() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"

            + "<style id='style_none'>my { }</style>\n"
            + "<style type='text/test' id='style_text'>my { }</style>\n"
            + "<style type='text/css' id='style_css'>my { }</style>\n"

            + "<script>\n"
            + "function doTest() {\n"
            + "  style = document.getElementById('style_none');\n"
            + "  alert(style.type);\n"
            + "  style = document.getElementById('style_text');\n"
            + "  alert(style.type);\n"
            + "  style = document.getElementById('style_css');\n"
            + "  alert(style.type);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "all", "screen, print,test" })
    public void media() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"

            + "<style id='style_none'>my { }</style>\n"
            + "<style media='all' id='style_all'>my { }</style>\n"
            + "<style media='screen, print,test' id='style_some'>my { }</style>\n"

            + "<script>\n"
            + "function doTest() {\n"
            + "  style = document.getElementById('style_none');\n"
            + "  alert(style.media);\n"
            + "  style = document.getElementById('style_all');\n"
            + "  alert(style.media);\n"
            + "  style = document.getElementById('style_some');\n"
            + "  alert(style.media);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "text/css" })
    public void type_setter() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<style id='style_none'></style>\n"

            + "<script>\n"
            + "function doTest() {\n"
            + "  style = document.getElementById('style_none');\n"
            + "  alert(style.type);\n"
            + "  style.type = 'text/css';\n"
            + "  alert(style.type);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
