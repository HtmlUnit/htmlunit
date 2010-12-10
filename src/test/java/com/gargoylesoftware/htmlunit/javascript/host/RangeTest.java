/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Range}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class RangeTest extends WebDriverTestCase {

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
        loadPageWithAlerts2(contentStart + "alertRange(r);" + contentEnd);
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

        loadPageWithAlerts2(contentStart + script + contentEnd);
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

        loadPageWithAlerts2(contentStart + script + contentEnd);
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "qwerty", "tyxy", "[object DocumentFragment]", "[object HTMLSpanElement] [object Text]", "qwer",
        "[object HTMLSpanElement]" })
    public void testExtractContents() throws Exception {
        final String html =
              "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div><script>\n"
            + "var d = document.getElementById('d');\n"
            + "var s = document.getElementById('s');\n"
            + "var r = document.createRange();\n"
            + "r.setStart(s.firstChild, 4);\n"
            + "r.setEnd(d.childNodes[2], 2);\n"
            + "alert(s.innerHTML);\n"
            + "alert(r);\n"
            + "var fragment = r.extractContents();\n"
            + "alert(fragment);\n"
            + "alert(fragment.childNodes[0] + ' ' + fragment.childNodes[1]);\n"
            + "alert(s.innerHTML);\n"
            + "alert(document.getElementById('s'));\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({
        "1 <p><b id=\"b\">text1<span id=\"s\">inner</span>text2</b></p>",
        "2 text1",
        "3 [object DocumentFragment]",
        "4 1: [object HTMLParagraphElement]: <b id=\"b\">text1</b>",
        "5 <p><b id=\"b\"><span id=\"s\">inner</span>text2</b></p>",
        "6 1: [object HTMLParagraphElement]: <b id=\"b\"><span id=\"s\"></span>text2</b>",
        "7 <p><b id=\"b\"><span id=\"s\">inner</span></b></p>" })
    public void testExtractContents2() throws Exception {
        final String html =
              "<html><body><div id='d'><p><b id='b'>text1<span id='s'>inner</span>text2</b></p></div><script>\n"
            + "var d = document.getElementById('d');\n"
            + "var b = document.getElementById('b');\n"
            + "var s = document.getElementById('s');\n"
            + "var r = document.createRange();\n"
            + "r.setStart(d, 0);\n"
            + "r.setEnd(b, 1);\n"
            + "alert('1 ' + d.innerHTML);\n"
            + "alert('2 ' + r);\n"
            + "var f = r.extractContents();\n"
            + "alert('3 ' + f);\n"
            + "alert('4 ' + f.childNodes.length + ': ' + f.childNodes[0] + ': ' + f.childNodes[0].innerHTML);\n"
            + "alert('5 ' + d.innerHTML);\n"
            + "var r2 = document.createRange();\n"
            + "r2.setStart(s, 1);\n"
            + "r2.setEnd(d, 1);\n"
            + "var f2 = r2.extractContents();\n"
            + "alert('6 ' + f2.childNodes.length + ': ' + f2.childNodes[0] + ': ' + f2.childNodes[0].innerHTML);\n"
            + "alert('7 ' + d.innerHTML);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "1", "2", "3" })
    public void constants() throws Exception {
        final String html =
              "<html><body><script>\n"
            + "alert(Range.START_TO_START);\n"
            + "alert(Range.START_TO_END);\n"
            + "alert(Range.END_TO_END);\n"
            + "alert(Range.END_TO_START);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "-1", "1", "1", "-1", "0" })
    public void compareBoundaryPoints() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d1'><div id='d2'></div></div>\n"
            + "<script>\n"
            + "var range = document.createRange();\n"
            + "range.selectNode(document.getElementById('d1'));\n"
            + "var sourceRange = document.createRange();\n"
            + "sourceRange.selectNode(document.getElementById('d2'));\n"
            + "alert(range.compareBoundaryPoints(Range.START_TO_START, sourceRange));\n"
            + "alert(range.compareBoundaryPoints(Range.START_TO_END, sourceRange));\n"
            + "alert(range.compareBoundaryPoints(Range.END_TO_END, sourceRange));\n"
            + "alert(range.compareBoundaryPoints(Range.END_TO_START, sourceRange));\n"
            + "alert(range.compareBoundaryPoints(Range.START_TO_START, range));\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
