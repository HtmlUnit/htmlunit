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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Unit tests for {@link Selection}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class SelectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void equality_selection() throws Exception {
        final String html = "<html><body><script>alert(document.selection==document.selection);</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "true", IE8 = "exception")
    public void equality_getSelection() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  alert(window.getSelection()==window.getSelection());\n"
            + "} catch (e) {alert('exception')}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "0", "0", "0", "cdefg" },
            CHROME = { "0", "1", "1", "cdefg" },
            IE8 = { })
    public void inputSelectionsAreIndependent() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<input id='i' value='abcdefghi'/>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    if (window.getSelection) {\n"
            + "      var i = document.getElementById('i');\n"
            + "      var s = window.getSelection();\n"
            + "      alert(s.rangeCount);\n"
            + "      i.selectionStart = 2;\n"
            + "      alert(s.rangeCount);\n"
            + "      i.selectionEnd = 7;\n"
            + "      alert(s.rangeCount);\n"
            + "      alert(i.value.substring(i.selectionStart, i.selectionEnd));\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {
                        "1:null/0/null/0/true/undefined/0/",
                        "2:s2/0/s2/1/false/undefined/1/xyz/xyz" },
            CHROME = {
                        "1:null/0/null/0/true/None/0/",
                        "2:[object Text]/0/[object Text]/3/false/Range/1/xyz/xyz" },
            IE8 = { })
    public void selectAllChildren() throws Exception {
        final String jsSnippet = ""
            + "    alertSelection(selection);\n"
            + "    selection.selectAllChildren(s2);\n"
            + "    alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {
                        "1:s2/0/s2/1/false/undefined/1/xyz/xyz",
                        "2:s2/0/s3/1/false/undefined/1/xyzfoo/xyzfoo",
                        "3:s2/0/s3/2/false/undefined/1/xyzfoo---/xyzfoo---",
                        "4:s2/0/s3/3/false/undefined/1/xyzfoo---foo/xyzfoo---foo" },
            CHROME = {
                    "1:[object Text]/0/[object Text]/3/false/Range/1/xyz/xyz",
                    "2:[object Text]/0/[object Text]/3/false/Range/1/xyzfoo/xyzfoo",
                    "3:[object Text]/0/[object Text]/3/false/Range/1/xyzfoo---/xyzfoo---",
                    "4:[object Text]/0/[object Text]/3/false/Range/1/xyzfoo---foo/xyzfoo---foo" },
            IE8 = { },
            IE11 = { "1:s2/0/s2/1/false/undefined/1/xyz/xyz",
                        "selection.extend not available" })
    public void extend() throws Exception {
        final String jsSnippet = ""
            + "    selection.selectAllChildren(s2);\n"
            + "    alertSelection(selection);\n"
            + "    if (selection.extend) {\n"
            + "      selection.extend(s3, 1);\n"
            + "      alertSelection(selection);\n"
            + "      selection.extend(s3, 2);\n"
            + "      alertSelection(selection);\n"
            + "      selection.extend(s3, 3);\n"
            + "      alertSelection(selection);\n"
            + "    } else { alert('selection.extend not available'); }\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {
                        "1:s2/0/s2/1/false/undefined/1/xyz/xyz",
                        "2:s2/0/s2/0/true/undefined/1//" },
            CHROME = {
                        "1:[object Text]/0/[object Text]/3/false/Range/1/xyz/xyz",
                        "2:[object Text]/3/[object Text]/3/true/Caret/1//" },
            IE8 = { })
    public void collapseToStart() throws Exception {
        final String jsSnippet = ""
            + "    selection.selectAllChildren(s2);\n"
            + "    alertSelection(selection);\n"
            + "    selection.collapseToStart();\n"
            + "    alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {
                        "1:s2/0/s2/1/false/undefined/1/xyz/xyz",
                        "2:s2/1/s2/1/true/undefined/1//" },
            CHROME = {
                        "1:[object Text]/0/[object Text]/3/false/Range/1/xyz/xyz",
                        "2:[object Text]/3/[object Text]/3/true/Caret/1//" },
            IE8 = { })
    public void collapseToEnd() throws Exception {
        final String jsSnippet = ""
            + "    selection.selectAllChildren(s2);\n"
            + "    alertSelection(selection);\n"
            + "    selection.collapseToEnd();\n"
            + "    alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {
                        "1:null/0/null/0/true/undefined/0/",
                        "2:null/0/null/0/true/undefined/0/",
                        "3:s2/1/s3/1/false/undefined/1/foo/foo",
                        "4:null/0/null/0/true/undefined/0/" },
            CHROME = {
                    "1:null/0/null/0/true/None/0/",
                    "2:null/0/null/0/true/None/0/",
                    "3:[object Text]/0/[object Text]/3/false/Range/1/foo/foo",
                    "4:null/0/null/0/true/None/0/" },
            IE8 = { })
    public void range() throws Exception {
        final String jsSnippet = ""
            + "    alertSelection(selection);\n"
            + "    var range = document.createRange();\n"
            + "    range.setStart(s2, 1);\n"
            + "    range.setEnd(s3, 1);\n"
            + "    alertSelection(selection);\n"
            + "    selection.addRange(range);\n"
            + "    alertSelection(selection);\n"
            + "    selection.removeAllRanges();\n"
            + "    alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * Test selection's anchorNode and focusNode after call to removeRange. Surprisingly, this is
     * not null.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {
                    "1:[object Text]/1/[object Text]/2/false/undefined/1/yzfo/yzfo",
                    "2:null/0/null/0/true/undefined/0/",
                    "false", "true" },
            CHROME = {
                    "1:[object Text]/1/[object Text]/2/false/Range/1/yzfo/yzfo",
                    "exception" },
            IE8 = { })
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

        tester(jsSnippet);
    }

    private void tester(final String jsSnippet) throws Exception {
        final String html = "<html>\n"
            + "<body onload='test()'>\n"
            + "  <span id='s1'>abc</span>"
            +   "<span id='s2'>xyz</span>"
            +   "<span id='s3'>foo<span>---</span>foo</span>\n"

            + "<script>\n"
            + "  var x = 1;\n"
            + "  function test() {\n"
            + "    if (!window.getSelection) {\n"
            + "      return;\n"
            + "    }\n"
            + "    var selection = window.getSelection();\n"
            + "    var s1 = document.getElementById('s1');\n"
            + "    var s2 = document.getElementById('s2');\n"
            + "    var s3 = document.getElementById('s3');\n"
            + "    try {\n"
                        + jsSnippet
            + "    } catch(e) { alert('exception'); }\n"
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
    @Alerts(DEFAULT = { "" , "null-0" , "", "null-0" , "", "null-0" , "", "null-0" },
            FF = { "", "null-0" , "", "null-0" , "null", "null" },
            IE8 = { })
    public void getSelection_display() throws Exception {
        final String html = "<html>\n"
            + "<body onload='test()'>\n"
            + "  <iframe id='frame1' src='about:blank'></iframe>\n"
            + "  <iframe id='frame2' src='about:blank' style='display: none'></iframe>\n"
            + "  <div style='display: none'>\n"
            + "    <iframe id='frame3' src='about:blank'></iframe>\n"
            + "  </div>\n"

            + "  <script>\n"
            + "    function sel(win) {\n"
            + "      if (win.getSelection) {\n"
            + "        var range = win.getSelection();\n"
            + "        alert(range);\n"
            + "        if (range) {\n"
            + "          alert(range.anchorNode + '-' + range.rangeCount);\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"

            + "    function test() {\n"
            + "      if (window.getSelection) {\n"
            + "        sel(window);\n"
            + "        sel(document.getElementById('frame1').contentWindow);\n"
            + "        sel(document.getElementById('frame2').contentWindow);\n"
            + "        sel(document.getElementById('frame3').contentWindow);\n"
            + "      }\n"
            + "    }\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

}
