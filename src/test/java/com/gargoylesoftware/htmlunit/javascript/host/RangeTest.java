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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link Range}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class RangeTest extends WebTestCase {

    private static final String contentStart = "<html><head><title>Range Test</title>\n"
        + "<script>\n"
        + "function safeTagName(o) {\n"
        + "  return o ? (o.tagName ? o.tagName : o) : undefined;\n"
        + "}\n"
        + "function alertRange(r) {\n"
        + "  alert(r.collapsed);\n"
        + "  alert(safeTagName(r.commonAncestorContainer));\n"
        + "  alert(safeTagName(r.startContainer));\n"
        + "  alert(r.startOffset);\n"
        + "  alert(safeTagName(r.endContainer));\n"
        + "  alert(r.endOffset);\n"
        + "}\n"
        + "function test() {\n"
        + "var r = document.createRange();\n";

    private static final String contentEnd = "\n}\n</script></head>\n"
        + "<body onload='test()'>\n"
        + "<div id='theDiv'>Hello, <span id='theSpan'>this is a test for"
        + "<a  id='theA' href='http://htmlunit.sf.net'>HtmlUnit</a> support"
        +  "</div>\n"
        + "<p id='theP'>for Range</p>\n"
        + "</body></html>";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "true", "[object HTMLDocument]", "[object HTMLDocument]", "0", "[object HTMLDocument]", "0" })
    public void testEmptyRange() throws Exception {
        loadPageWithAlerts(contentStart + "alertRange(r);" + contentEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "false", "BODY", "BODY", "1", "BODY", "2" })
    public void testSelectNode() throws Exception {
        final String script = "r.selectNode(document.getElementById('theDiv'));"
            + "alertRange(r);";

        loadPageWithAlerts(contentStart + script + contentEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "false", "DIV", "DIV", "0", "DIV", "2" })
    public void testSelectNodeContents() throws Exception {
        final String script = "r.selectNodeContents(document.getElementById('theDiv'));"
            + "alertRange(r);";

        loadPageWithAlerts(contentStart + script + contentEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts("<div id=\"myDiv2\"></div><div>harhar</div><div id=\"myDiv3\"></div>")
    public void testCreateContextualFragment() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('<div>harhar</div>');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    alert(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }
}
