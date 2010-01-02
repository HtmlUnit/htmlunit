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

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;

/**
 * Unit tests for {@link Selection}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class SelectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts(IE = "true")
    public void equality_IE() throws Exception {
        final String html = "<html><body><script>alert(document.selection==document.selection);</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = "true")
    public void equality_FF() throws Exception {
        final String html = "<html><body><script>\n"
            + "alert(window.getSelection()==window.getSelection());\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void stringValue_FF() throws Exception {
        test("", "selection", "x", "", "cx");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    public void stringValue_IE() throws Exception {
        test("", "selection", "x", "[object]", "[object]");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void anchorNode() throws Exception {
        test("", "selection.anchorNode", "x ? x.parentNode.id : x", "null", "s1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void anchorOffset() throws Exception {
        test("", "selection.anchorOffset", "x", "0", "2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void focusNode() throws Exception {
        test("", "selection.focusNode", "x ? x.parentNode.id : x", "null", "s2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void focusOffset() throws Exception {
        test("", "selection.focusOffset", "x", "0", "1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void isCollapsed() throws Exception {
        test("", "selection.isCollapsed", "x", "true", "false");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void rangeCount() throws Exception {
        test("", "selection.rangeCount", "x", "0", "1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void rangeCount2() throws Exception {
        test("selection.collapseToEnd()", "selection.rangeCount", "x", "0", "1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void removeAllRanges() throws Exception {
        test("selection.removeAllRanges()", "selection.anchorNode", "x ? x.parentNode.id : x", "null", "null");
        test("selection.removeAllRanges()", "selection.anchorOffset", "x ? x.parentNode.id : x", "0", "0");
        test("selection.removeAllRanges()", "selection.focusNode", "x ? x.parentNode.id : x", "null", "null");
        test("selection.removeAllRanges()", "selection.focusOffset", "x ? x.parentNode.id : x", "0", "0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void collapse() throws Exception {
        test("selection.collapse(s1, 1)", "selection.focusNode", "x ? x.id : x", "null", "s1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void collapseToEnd() throws Exception {
        test("selection.collapseToEnd()", "selection.anchorNode", "x ? x.parentNode.id : x", "null", "s2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void collapseToStart() throws Exception {
        test("selection.collapseToStart()", "selection.focusNode", "x ? x.parentNode.id : x", "null", "s1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void extend() throws Exception {
        test("selection.extend(s2, 2)", "selection.focusOffset", "x", "0", "2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void selectAllChildren() throws Exception {
        test("selection.selectAllChildren(document.body)", "selection.anchorOffset", "x", "0", "0");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    public void getRangeAt() throws Exception {
        test("", "selection.rangeCount > 0 ? selection.getRangeAt(0) : 'none'", "x", "none", "cx");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    public void empty() throws Exception {
        test("selection.empty()", "selection.type", "x", "None", "None");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    public void createRange() throws Exception {
        test("", "selection.createRange()", "x", "[object]", "[object]");
    }

    private void test(final String action, final String x, final String alert, final String... expected)
        throws Exception {

        final String html = "<html><body onload='test()'>\n"
            + "<span id='s1'>abc</span><span id='s2'>xyz</span><span id='s3'>foo</span>\n"
            + "<input type='button' id='b' onclick='" + action + ";test();' value='click'></input>\n"
            + "<script>\n"
            + "  var selection = document.selection; // IE\n"
            + "  if(!selection) selection = window.getSelection(); // FF\n"
            + "  var s1 = document.getElementById('s1');\n"
            + "  var s2 = document.getElementById('s2');\n"
            + "  function test() {\n"
            + "    var x = " + x + ";\n"
            + "    alert(" + alert + ");\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        final WebClient client = getWebClient();

        final MockWebConnection conn = new MockWebConnection();
        conn.setDefaultResponse(html);
        client.setWebConnection(conn);

        final List<String> actual = new ArrayList<String>();
        final AlertHandler alertHandler = new CollectingAlertHandler(actual);
        client.setAlertHandler(alertHandler);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlSpan s1 = page.getHtmlElementById("s1");
        final DomNode s1Text = s1.getFirstChild();
        final HtmlSpan s2 = page.getHtmlElementById("s2");
        final DomNode s2Text = s2.getFirstChild();
        final HtmlInput input = page.getHtmlElementById("b");

        final org.w3c.dom.ranges.Range range = new SimpleRange();
        range.setStart(s1Text, 2);
        range.setEnd(s2Text, 1);
        page.setSelectionRange(range);
        input.click();

        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = { "0", "0", "0", "cdefg" })
    public void inputSelectionsAreIndependent() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<input id='i' value='abcdefghi'/>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    var s = window.getSelection();\n"
            + "    alert(s.rangeCount);\n"
            + "    i.selectionStart = 2;\n"
            + "    alert(s.rangeCount);\n"
            + "    i.selectionEnd = 7;\n"
            + "    alert(s.rangeCount);\n"
            + "    alert(i.value.substring(i.selectionStart, i.selectionEnd));\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = {
            "1:null/0/null/0/true/undefined/0/",
            "2:s2/0/s2/1/false/undefined/1/xyz/xyz",
            "3:s2/0/s3/1/false/undefined/1/xyzfoo/xyzfoo",
            "4:s2/0/s3/2/false/undefined/1/xyzfoo---/xyzfoo---",
            "5:s2/0/s3/3/false/undefined/1/xyzfoo---foo/xyzfoo---foo",
            "6:s3/3/s3/3/true/undefined/1//",
            "7:s1/0/s1/1/false/undefined/1/abc/abc",
            "8:s1/0/s1/1/false/undefined/1/abc/abc",
            "9:s2/1/s3/1/false/undefined/2/abcfoo/abc/foo",
            "10:s2/1/s3/3/false/undefined/2/abcfoo---foo/abc/foo---foo",
            "11:s1/0/s1/0/true/undefined/1//",
            "12:null/0/null/0/true/undefined/0/" })
    public void aLittleBitOfEverything() throws Exception {
        final String jsSnippet = ""
            + "    alertSelection(selection);\n"
            + "    selection.selectAllChildren(s2);\n"
            + "    alertSelection(selection);\n"
            + "    selection.extend(s3, 1);\n"
            + "    alertSelection(selection);\n"
            + "    selection.extend(s3, 2);\n"
            + "    alertSelection(selection);\n"
            + "    selection.extend(s3, 3);\n"
            + "    alertSelection(selection);\n"
            + "    selection.collapseToEnd();\n"
            + "    alertSelection(selection);\n"
            + "    selection.selectAllChildren(s1);\n"
            + "    alertSelection(selection);\n"
            + "    var range = document.createRange();\n"
            + "    range.setStart(s2, 1);\n"
            + "    range.setEnd(s3, 1);\n"
            + "    alertSelection(selection);\n"
            + "    selection.addRange(range);\n"
            + "    alertSelection(selection);\n"
            + "    selection.extend(s3, 3);\n"
            + "    alertSelection(selection);\n"
            + "    selection.collapseToStart();\n"
            + "    alertSelection(selection);\n"
            + "    selection.removeAllRanges();\n"
            + "    alertSelection(selection);\n";

        aLittleBitOfEverything(jsSnippet);
    }

    /**
     * Test selection's anchorNode and focusNode after call to removeRange. Surprisingly, this is
     * not null.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @NotYetImplemented
    @Alerts(FF = {
            "1:[object Text]/1/[object Text]/2/false/undefined/1/yzfo/yzfo",
            "2:[object Text]/1/[object Text]/2/true/undefined/0/",
            "false", "true" })
    public void aLittleBitOfEverything_removeRange() throws Exception {
        final String jsSnippet = ""
            + "    var range = document.createRange();\n"
            + "    range.setStart(s2.firstChild, 1);\n"
            + "    range.setEnd(s3.firstChild, 2);\n"
            + "    selection.addRange(range);\n"
            + "    alertSelection(selection);\n"
            + "    selection.removeRange(range);\n"
            + "    alertSelection(selection);\n"
            + "    alert(range.collapsed);\n"
            + "    selection.addRange(range);\n"
            + "    alert(selection.getRangeAt(0) == selection.getRangeAt(0));\n";

        aLittleBitOfEverything(jsSnippet);
    }

    private void aLittleBitOfEverything(final String jsSnippet) throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<span id='s1'>abc</span><span id='s2'>xyz</span><span id='s3'>foo<span>---</span>foo</span>\n"
            + "<script>\n"
            + "  var x = 1;\n"
            + "  function test() {\n"
            + "    var selection = window.getSelection();\n"
            + "    var s1 = document.getElementById('s1');\n"
            + "    var s2 = document.getElementById('s2');\n"
            + "    var s3 = document.getElementById('s3');\n"
            + jsSnippet
            + "  }\n"
            + "  function alertSelection(s) {\n"
            + "    var anchorNode = (s.anchorNode && s.anchorNode.id ? s.anchorNode.id : s.anchorNode);\n"
            + "    var focusNode = (s.focusNode && s.focusNode.id ? s.focusNode.id : s.focusNode);\n"
            + "    var msg = (x++) + ':' + anchorNode + '/' + s.anchorOffset + '/' + focusNode + '/' +\n"
            + "       s.focusOffset + '/' + s.isCollapsed + '/' + s.type + '/' + s.rangeCount + '/' + s;\n"
            + "    for(var i = 0; i < s.rangeCount; i++) {\n"
            + "      msg += '/' + s.getRangeAt(i);\n"
            + "    }\n"
            + "    alert(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts(FF = { "", "", "null", "null" })
    public void getSelection_display() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<iframe id='frame1' src='about:blank'></iframe>\n"
            + "<iframe id='frame2' src='about:blank' style='display: none'></iframe>\n"
            + "<div style='display: none'><iframe id='frame3' src='about:blank'></iframe></div>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(window.getSelection());\n"
            + "    alert(document.getElementById('frame1').contentWindow.getSelection());"
            + "    alert(document.getElementById('frame2').contentWindow.getSelection());"
            + "    alert(document.getElementById('frame3').contentWindow.getSelection());"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

}
