/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.junit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link Range}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author James Phillpotts
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class RangeTest extends WebDriverTestCase {

    private static final String contentStart = "<html><head><title></title>\n"
        + "<script>\n"
        + LOG_TITLE_FUNCTION
        + "function safeTagName(o) {\n"
        + "  return o ? (o.tagName ? o.tagName : o) : undefined;\n"
        + "}\n"
        + "function alertRange(r) {\n"
        + "  log(r.collapsed);\n"
        + "  log(safeTagName(r.commonAncestorContainer));\n"
        + "  log(safeTagName(r.startContainer));\n"
        + "  log(r.startOffset);\n"
        + "  log(safeTagName(r.endContainer));\n"
        + "  log(r.endOffset);\n"
        + "}\n"
        + "function test() {\n"
        + "  var r = document.createRange();\n";

    private static final String contentEnd = "\n}\n</script></head>\n"
        + "<body onload='test()'>\n"
        + "<div id='theDiv'>Hello, <span id='theSpan'>this is a test for"
        + "<a id='theA' href='http://htmlunit.sf.net'>HtmlUnit</a> support"
        + "</div>\n"
        + "<p id='theP'>for Range</p>\n"
        + "</body></html>";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "[object HTMLDocument]", "[object HTMLDocument]", "0", "[object HTMLDocument]", "0"})
    public void emptyRange() throws Exception {
        loadPageVerifyTitle2(contentStart + "alertRange(r);" + contentEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "BODY", "BODY", "1", "BODY", "2"})
    public void selectNode() throws Exception {
        final String script = "r.selectNode(document.getElementById('theDiv'));"
            + "alertRange(r);";

        loadPageVerifyTitle2(contentStart + script + contentEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "DIV", "DIV", "0", "DIV", "2"})
    public void selectNodeContents() throws Exception {
        final String script = "r.selectNodeContents(document.getElementById('theDiv'));"
            + "alertRange(r);";

        loadPageVerifyTitle2(contentStart + script + contentEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div id=\"myDiv2\"></div><div>harhar</div><div id=\"myDiv3\"></div>")
    public void createContextualFragment() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('<div>harhar</div>');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    log(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Same fragment should be parsed differently depending on the context.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Text]", "[object HTMLTableRowElement]"})
    public void createContextualFragment2() throws Exception {
        final String html = "<html><body>\n"
            + "<div id ='d'></div>\n"
            + "<table><tr id='t'><td>old</td></tr></table>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test(id) {\n"
            + "  var element = document.getElementById(id);\n"
            + "  var range = element.ownerDocument.createRange();\n"
            + "  range.selectNode(element);\n"
            + "  var str = '<tr>  <td>new</td></tr>';\n" // space between <tr> and <td> is important!
            + "  var fragment = range.createContextualFragment(str);\n"
            + "  log(fragment.firstChild);\n"
            + "}\n"
            + "try {\n"
            + "  test('d');\n"
            + "  test('t');\n"
            + "} catch (e) { log('exception'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"qwerty", "tyxy", "[object DocumentFragment]", "[object HTMLSpanElement] [object Text]", "qwer",
             "[object HTMLSpanElement]"})
    public void extractContents() throws Exception {
        final String html =
              "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var s = document.getElementById('s');\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(s.firstChild, 4);\n"
            + "  r.setEnd(d.childNodes[2], 2);\n"
            + "  log(s.innerHTML);\n"
            + "  log(r);\n"
            + "  var fragment = r.extractContents();\n"
            + "  log(fragment);\n"
            + "  log(fragment.childNodes[0] + ' ' + fragment.childNodes[1]);\n"
            + "  log(s.innerHTML);\n"
            + "  log(document.getElementById('s'));\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1 <p><b id=\"b\">text1<span id=\"s\">inner</span>text2</b></p>",
             "2 text1",
             "3 [object DocumentFragment]",
             "4 1: [object HTMLParagraphElement]: <b id=\"b\">text1</b>",
             "5 <p><b id=\"b\"><span id=\"s\">inner</span>text2</b></p>",
             "6 1: [object HTMLParagraphElement]: <b id=\"b\"><span id=\"s\"></span>text2</b>",
             "7 <p><b id=\"b\"><span id=\"s\">inner</span></b></p>"})
    public void extractContents2() throws Exception {
        final String html =
            "<html><body><div id='d'><p><b id='b'>text1<span id='s'>inner</span>text2</b></p></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var b = document.getElementById('b');\n"
            + "  var s = document.getElementById('s');\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(d, 0);\n"
            + "  r.setEnd(b, 1);\n"
            + "  log('1 ' + d.innerHTML);\n"
            + "  log('2 ' + r);\n"
            + "  var f = r.extractContents();\n"
            + "  log('3 ' + f);\n"
            + "  log('4 ' + f.childNodes.length + ': ' + f.childNodes[0] + ': ' + f.childNodes[0].innerHTML);\n"
            + "  log('5 ' + d.innerHTML);\n"
            + "  var r2 = document.createRange();\n"
            + "  r2.setStart(s, 1);\n"
            + "  r2.setEnd(d, 1);\n"
            + "  var f2 = r2.extractContents();\n"
            + "  log('6 ' + f2.childNodes.length + ': ' + f2.childNodes[0] + ': ' + f2.childNodes[0].innerHTML);\n"
            + "  log('7 ' + d.innerHTML);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "1", "2", "3"})
    public void constants() throws Exception {
        final String html =
              "<html><body>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
            + "  log(Range.START_TO_START);\n"
            + "  log(Range.START_TO_END);\n"
            + "  log(Range.END_TO_END);\n"
            + "  log(Range.END_TO_START);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"-1", "1", "1", "-1", "0"})
    public void compareBoundaryPoints() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='d1'><div id='d2'></div></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var range = document.createRange();\n"
            + "  range.selectNode(document.getElementById('d1'));\n"
            + "  var sourceRange = document.createRange();\n"
            + "  sourceRange.selectNode(document.getElementById('d2'));\n"
            + "  log(range.compareBoundaryPoints(Range.START_TO_START, sourceRange));\n"
            + "  log(range.compareBoundaryPoints(Range.START_TO_END, sourceRange));\n"
            + "  log(range.compareBoundaryPoints(Range.END_TO_END, sourceRange));\n"
            + "  log(range.compareBoundaryPoints(Range.END_TO_START, sourceRange));\n"
            + "  log(range.compareBoundaryPoints(Range.START_TO_START, range));\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"abcd", "bc", "null", "null", "ad", "bc"})
    public void extractContents3() throws Exception {
        final String html =
            "<html><body><div id='d'><span id='a'>a</span><span id='b'>b</span>"
            + "<span id='c'>c</span><span id='d'>d</span></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var s = document.getElementById('s');\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(d, 1);\n"
            + "  r.setEnd(d, 3);\n"
            + "  log(d.textContent);\n"
            + "  log(r.toString());\n"
            + "  var x = r.extractContents();\n"
            + "  log(document.getElementById('b'));\n"
            + "  log(document.getElementById('c'));\n"
            + "  log(d.textContent);\n"
            + "  log(x.textContent);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"qwerty", "tyxy", "[object DocumentFragment]", "[object HTMLSpanElement] [object Text]",
             "qwerty", "[object HTMLSpanElement]"})
    public void cloneContents() throws Exception {
        final String html =
            "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var s = document.getElementById('s');\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(s.firstChild, 4);\n"
            + "  r.setEnd(d.childNodes[2], 2);\n"
            + "  log(s.innerHTML);\n"
            + "  log(r);\n"
            + "  var fragment = r.cloneContents();\n"
            + "  log(fragment);\n"
            + "  log(fragment.childNodes[0] + ' ' + fragment.childNodes[1]);\n"
            + "  log(s.innerHTML);\n"
            + "  log(document.getElementById('s'));\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"qwerty", "bcqwertyxy", "null", "az"})
    public void deleteContents() throws Exception {
        final String html =
            "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var s = document.getElementById('s');\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(d.firstChild, 1);\n"
            + "  r.setEnd(d.childNodes[2], 2);\n"
            + "  log(s.innerHTML);\n"
            + "  log(r.toString());\n"
            + "  r.deleteContents();\n"
            + "  log(document.getElementById('s'));\n"
            + "  log(d.textContent);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"abcd", "bc", "null", "null", "ad"})
    public void deleteContents2() throws Exception {
        final String html =
            "<html><body><div id='d'><span id='a'>a</span><span id='b'>b</span><span id='c'>c</span>"
            + "<span id='d'>d</span></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var s = document.getElementById('s');\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(d, 1);\n"
            + "  r.setEnd(d, 3);\n"
            + "  log(d.textContent);\n"
            + "  log(r.toString());\n"
            + "  r.deleteContents();\n"
            + "  log(document.getElementById('b'));\n"
            + "  log(document.getElementById('c'));\n"
            + "  log(d.textContent);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void getClientRectsEmpty() throws Exception {
        final String html =
            "<html>\n"
            + "<body>\n"
            + "  <div id='d'>a</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var r = document.createRange();\n"
            + "  log(r.getClientRects().length);\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true",
            IE = "false")
    @NotYetImplemented(IE)
    public void getClientRectsMany() throws Exception {
        final String html =
            "<html><body><div id='d'><span id='a'>a</span><span id='b'>b</span><span id='c'>c</span>"
            + "<span id='d'>d</span></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var s = document.getElementById('s');\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(d, 1);\n"
            + "  r.setEnd(d, 3);\n"
            + "  log(r.getClientRects().length > 1);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test for a regression, getBoundingClientRect has detached
     * all elements of the range from the document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLBodyElement]")
    public void getBoundingClientRectDoesNotChangeTheParent() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var range = document.createRange();\n"

            + "  var elem = document.createElement('boundtest');\n"
            + "  document.body.appendChild(elem);\n"

            + "  range.selectNode(elem);\n"
            + "  range.getBoundingClientRect();\n"

            + "  log(elem.parentNode);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for a regression, getClientRects has detached
     * all elements of the range from the document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLBodyElement]")
    public void getClientRectsDoesNotChangeTheParent() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  var range = document.createRange();\n"

            + "  var elem = document.createElement('boundtest');\n"
            + "  document.body.appendChild(elem);\n"

            + "  range.selectNode(elem);\n"
            + "  range.getClientRects();\n"

            + "  log(elem.parentNode);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"tyxy", "tyxy", "tyxy"})
    public void testToString() throws Exception {
        final String html =
              "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div>\n"
              + "<script>\n"
              + LOG_TITLE_FUNCTION
              + "  var d = document.getElementById('d');\n"
              + "  var s = document.getElementById('s');\n"
              + "  var r = document.createRange();\n"
              + "  r.setStart(s.firstChild, 4);\n"
              + "  r.setEnd(d.childNodes[2], 2);\n"
              + "  log(r);\n"
              + "  log('' + r);\n"
              + "  log(r.toString());\n"
              + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
