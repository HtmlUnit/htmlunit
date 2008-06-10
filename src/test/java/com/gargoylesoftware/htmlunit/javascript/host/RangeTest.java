/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link Range}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class RangeTest extends WebTestCase {

    private static final String contentStart = "<html><head><title>Range Test</title>\n"
        + "<script>\n"
        + "function safeTagName(o)\n"
        + "{\n"
        + "  return o ? o.tagName : undefined\n"
        + "}\n"
        + "function alertRange(r)\n"
        + "{\n"
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

    private void test(final String script, final String[] expectedAlerts) throws Exception {
        final String content = contentStart + script + contentEnd;

        createTestPageForRealBrowserIfNeeded(content, expectedAlerts);

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, content, collectedAlerts);

        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEmptyRange() throws Exception {
        final String[] expectedAlerts = {"true", "undefined", "undefined", "0", "undefined", "0"};
        final String script = "alertRange(r);";

        test(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSelectNode() throws Exception {
        final String script = "r.selectNode(document.getElementById('theDiv'));"
            + "alertRange(r);";
        final String[] expectedAlerts = {"false", "BODY", "BODY", "1", "BODY", "2"};

        test(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testSelectNodeContents() throws Exception {
        final String script = "r.selectNodeContents(document.getElementById('theDiv'));"
            + "alertRange(r);";
        final String[] expectedAlerts = {"false", "DIV", "DIV", "0", "DIV", "2"};

        test(script, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
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

        final String[] expectedAlerts = {"<div id=\"myDiv2\"></div><div>harhar</div><div id=\"myDiv3\"></div>"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
