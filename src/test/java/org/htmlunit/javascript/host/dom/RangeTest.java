/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.dom;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Range}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author James Phillpotts
 * @author Frank Danek
 * @author Ronald Brill
 */
public class RangeTest extends WebDriverTestCase {

    private static final String CONTENT_START = DOCTYPE_HTML
        + "<html><head><title></title>\n"
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

    private static final String CONTENT_END = "\n}\n</script></head>\n"
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
        loadPageVerifyTitle2(CONTENT_START + "alertRange(r);" + CONTENT_END);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "BODY", "BODY", "1", "BODY", "2"})
    public void selectNode() throws Exception {
        final String script = "r.selectNode(document.getElementById('theDiv'));"
            + "alertRange(r);";

        loadPageVerifyTitle2(CONTENT_START + script + CONTENT_END);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "DIV", "DIV", "0", "DIV", "2"})
    public void selectNodeContents() throws Exception {
        final String script = "r.selectNodeContents(document.getElementById('theDiv'));"
            + "alertRange(r);";

        loadPageVerifyTitle2(CONTENT_START + script + CONTENT_END);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div id=\"myDiv2\"></div><div>harhar</div><div id=\"myDiv3\"></div>")
    public void createContextualFragment() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
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
            + "} catch(e) { logEx(e); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<div id=\"myDiv2\"></div>hello:<div id=\"myDiv3\"></div>")
    public void createContextualStrangeCode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('hello:<world');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    log(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'><p><b id='b'>text1<span id='s'>inner</span>text2</b></p></div>\n"
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
        final String html = DOCTYPE_HTML
              + "<html><body>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'><span id='a'>a</span><span id='b'>b</span>"
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
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'><span id='a'>a</span><span id='b'>b</span><span id='c'>c</span>"
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
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
    @Alerts(
            DEFAULT = {"4",
                       "x=15.104166984558105 y=8 w=8.000000953674316 h=17.33333396911621",
                       "x=15.104166984558105 y=8 w=8.000000953674316 h=17.33333396911621",
                       "x=23.104167938232422 y=8 w=7.104166030883789 h=17.33333396911621",
                       "x=23.104167938232422 y=8 w=7.104166030883789 h=17.33333396911621"},
            FF = {"4",
                  "x=15.100006103515625 y=8.666671752929688 w=8 h=17.333328247070312",
                  "x=15.100006103515625 y=8.666671752929688 w=8 h=17.333328247070312",
                  "x=23.100006103515625 y=8.666671752929688 w=7.0999908447265625 h=17.333328247070312",
                  "x=23.100006103515625 y=8.666671752929688 w=7.0999908447265625 h=17.333328247070312"},
            FF_ESR = {"4",
                      "x=15.100006103515625 y=8.666671752929688 w=8 h=17.333328247070312",
                      "x=15.100006103515625 y=8.666671752929688 w=8 h=17.333328247070312",
                      "x=23.100006103515625 y=8.666671752929688 w=7.0999908447265625 h=17.333328247070312",
                      "x=23.100006103515625 y=8.666671752929688 w=7.0999908447265625 h=17.333328247070312"})
    @HtmlUnitNYI(
            CHROME = {"4", "x=18 y=8 w=10 h=18", "x=18 y=8 w=10 h=18", "x=28 y=8 w=10 h=18", "x=28 y=8 w=10 h=18"},
            EDGE = {"4", "x=18 y=8 w=10 h=18", "x=18 y=8 w=10 h=18", "x=28 y=8 w=10 h=18", "x=28 y=8 w=10 h=18"},
            FF = {"4", "x=18 y=8 w=10 h=18", "x=18 y=8 w=10 h=18", "x=28 y=8 w=10 h=18", "x=28 y=8 w=10 h=18"},
            FF_ESR = {"4", "x=18 y=8 w=10 h=18", "x=18 y=8 w=10 h=18", "x=28 y=8 w=10 h=18", "x=28 y=8 w=10 h=18"})
    public void getClientRectsMany() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'><span id='a'>a</span><span id='b'>b</span><span id='c'>c</span>"
            + "<span id='d'>d</span></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var s = document.getElementById('s');\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(d, 1);\n"
            + "  r.setEnd(d, 3);\n"

            + "  log(r.getClientRects().length);\n"
            + "  let rect = r.getClientRects()[0];\n"
            + "  log('x=' + rect.x + ' y=' + rect.y + ' w=' + rect.width + ' h=' + rect.height);\n"
            + "  rect = r.getClientRects()[1];\n"
            + "  log('x=' + rect.x + ' y=' + rect.y + ' w=' + rect.width + ' h=' + rect.height);\n"
            + "  rect = r.getClientRects()[2];\n"
            + "  log('x=' + rect.x + ' y=' + rect.y + ' w=' + rect.width + ' h=' + rect.height);\n"
            + "  rect = r.getClientRects()[3];\n"
            + "  log('x=' + rect.x + ' y=' + rect.y + ' w=' + rect.width + ' h=' + rect.height);\n"
            + ""

            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Tests getClientRects() for a range over a text node (setStart/setEnd with char offsets).
     * Previously returned an empty list because DomText nodes were skipped in the
     * HTMLElement instanceof check, and containedNodes() returned empty for same-node ranges.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(
            DEFAULT = {"1", "x=8 y=8 w=35.552085876464844 h=17.33333396911621"},
            FF = {"1", "x=8 y=8.666671752929688 w=35.55000305175781 h=17.333328247070312"},
            FF_ESR = {"1", "x=8 y=8.666671752929688 w=35.55000305175781 h=17.333328247070312"})
    @HtmlUnitNYI(
            CHROME = {"1", "x=8 y=8 w=1240 h=18"},
            EDGE = {"1", "x=8 y=8 w=1240 h=18"},
            FF = {"1", "x=8 y=8 w=1240 h=18"},
            FF_ESR = {"1", "x=8 y=8 w=1240 h=18"})
    public void getClientRectsOnTextNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <div id='d'>Hello World</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var textNode = document.getElementById('d').firstChild;\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(textNode, 0);\n"
            + "  r.setEnd(textNode, 5);\n"
            + "  log(r.getClientRects().length);\n"
            + "  let rect = r.getClientRects()[0];\n"
            + "  log('x=' + rect.x + ' y=' + rect.y + ' w=' + rect.width + ' h=' + rect.height);\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        final String html = DOCTYPE_HTML
              + "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div>\n"
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

    /**
     * Test that getClientRects does not mutate the text content of the DOM.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"Hello World", "Hello World"})
    public void getClientRectsDoesNotMutateTextContent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'>Hello World</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var textNode = d.firstChild;\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(textNode, 0);\n"
            + "  r.setEnd(textNode, 5);\n"
            + "  log(d.textContent);\n"
            + "  r.getClientRects();\n"
            + "  log(d.textContent);\n"  // must be unchanged
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test getBoundingClientRect for a range over a text node.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void getBoundingClientRectOnTextNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'>Hello World</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var textNode = d.firstChild;\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(textNode, 0);\n"
            + "  r.setEnd(textNode, 5);\n"
            + "  var rect = r.getBoundingClientRect();\n"
            + "  log(rect.width > 0 && rect.height > 0);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test that getBoundingClientRect does not mutate text content of the DOM.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"Hello World", "Hello World"})
    public void getBoundingClientRectDoesNotMutateTextContent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'>Hello World</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var textNode = d.firstChild;\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(textNode, 0);\n"
            + "  r.setEnd(textNode, 5);\n"
            + "  log(d.textContent);\n"
            + "  r.getBoundingClientRect();\n"
            + "  log(d.textContent);\n"  // must be unchanged
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test getClientRects for a mid-string text range (non-zero start offset).
     * Verifies containedNodes() handles same-node text ranges at any offset.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void getClientRectsOnTextNodeMidString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'>Hello World</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var textNode = d.firstChild;\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(textNode, 3);\n"
            + "  r.setEnd(textNode, 8);\n"
            + "  log(r.getClientRects().length);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Tests all six boundary-point setters: setStart, setStartBefore, setStartAfter,
     * setEnd, setEndBefore, setEndAfter.
     * Verifies that each method sets the correct boundary (start vs end) and the
     * correct container/offset. In particular, setEndBefore was bugged: it called
     * internSetStartContainer/internSetStartOffset instead of the End variants.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({
        // setStart(b, 1) -> startContainer=DIV, startOffset=1
        "setStart: DIV 1 DIV 4",
        // setStartBefore(b) -> startContainer=DIV, startOffset=1 (b is at index 1)
        "setStartBefore: DIV 1 DIV 4",
        // setStartAfter(b) -> startContainer=DIV, startOffset=2 (after b = index 2)
        "setStartAfter: DIV 2 DIV 4",
        // setEnd(c, 2) -> endContainer=DIV, endOffset=2
        "setEnd: DIV 0 DIV 2",
        // setEndBefore(c) -> endContainer=DIV, endOffset=2 (c is at index 2)
        "setEndBefore: DIV 0 DIV 2",
        // setEndAfter(c) -> endContainer=DIV, endOffset=3 (after c = index 3)
        "setEndAfter: DIV 0 DIV 3"
    })
    public void setBoundaryPoints() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <div id='d'>"
            +      "<span id='a'>a</span>"
            +      "<span id='b'>b</span>"
            +      "<span id='c'>c</span>"
            +      "<span id='e'>e</span>"
            + "</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function rangeInfo(label, r) {\n"
            + "  var sc = r.startContainer.nodeName;\n"
            + "  var ec = r.endContainer.nodeName;\n"
            + "  log(label + ': ' + sc + ' ' + r.startOffset + ' ' + ec + ' ' + r.endOffset);\n"
            + "}\n"
            + "var d = document.getElementById('d');\n"
            + "var a = document.getElementById('a');\n"
            + "var b = document.getElementById('b');\n"
            + "var c = document.getElementById('c');\n"

            // setStart: sets startContainer=d, startOffset=1; end untouched (stays at d,3 after selectNodeContents)
            + "var r = document.createRange();\n"
            + "r.selectNodeContents(d);\n"          // start=d/0, end=d/4
            + "r.setStart(d, 1);\n"
            + "rangeInfo('setStart', r);\n"

            // setStartBefore(b): b is child index 1 of d -> startContainer=d, startOffset=1
            + "r = document.createRange();\n"
            + "r.selectNodeContents(d);\n"
            + "r.setStartBefore(b);\n"
            + "rangeInfo('setStartBefore', r);\n"

            // setStartAfter(b): b is child index 1, so after = offset 2 -> startContainer=d, startOffset=2
            + "r = document.createRange();\n"
            + "r.selectNodeContents(d);\n"
            + "r.setStartAfter(b);\n"
            + "rangeInfo('setStartAfter', r);\n"

            // setEnd: sets endContainer=d, endOffset=2; start untouched (stays at d,0)
            + "r = document.createRange();\n"
            + "r.selectNodeContents(d);\n"
            + "r.setEnd(d, 2);\n"
            + "rangeInfo('setEnd', r);\n"

            // setEndBefore(c): c is child index 2 -> endContainer=d, endOffset=2; start must stay at d,0
            // BUG: setEndBefore was calling internSetStartContainer instead of internSetEndContainer
            + "r = document.createRange();\n"
            + "r.selectNodeContents(d);\n"
            + "r.setEndBefore(c);\n"
            + "rangeInfo('setEndBefore', r);\n"

            // setEndAfter(c): c is child index 2, so after = offset 3 -> endContainer=d, endOffset=3; start stays at d,0
            + "r = document.createRange();\n"
            + "r.selectNodeContents(d);\n"
            + "r.setEndAfter(c);\n"
            + "rangeInfo('setEndAfter', r);\n"

            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"ello", "Hello World"})
    public void cloneContentsSameTextNode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><div id='d'>Hello World</div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var d = document.getElementById('d');\n"
            + "  var textNode = d.firstChild;\n"
            + "  var r = document.createRange();\n"
            + "  r.setStart(textNode, 1);\n"   // 'e' in Hello
            + "  r.setEnd(textNode, 5);\n"     // up to space -> "ello"
            + "  var fragment = r.cloneContents();\n"
            + "  log(fragment.textContent);\n"       // must be "ello"
            + "  log(d.textContent);\n"              // must be unchanged
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }
}
