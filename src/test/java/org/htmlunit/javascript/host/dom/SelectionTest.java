/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
 * Unit tests for {@link Selection}.
 *
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class SelectionTest extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void equality_selection() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<body>\n"
                + "<script>"
                + LOG_TITLE_FUNCTION
                + "log(document.selection==document.selection);\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void equality_getSelection() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(window.getSelection()==window.getSelection());\n"
            + "} catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"0", "0", "0", "cdefg"})
    public void inputSelectionsAreIndependent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'>\n"
            + "<input id='i' value='abcdefghi'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    var s = window.getSelection();\n"
            + "    log(s.rangeCount);\n"
            + "    i.selectionStart = 2;\n"
            + "    log(s.rangeCount);\n"
            + "    i.selectionEnd = 7;\n"
            + "    log(s.rangeCount);\n"
            + "    log(i.value.substring(i.selectionStart, i.selectionEnd));\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1:null/0/null/0/true/None/0/",
             "2:s2/0/s2/1/false/Range/1/xyz[xyz"})
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
    @Alerts({"1:s2/0/s2/1/false/Range/1/xyz[xyz",
             "2:s2/0/s3/1/false/Range/1/xyzfoo[xyzfoo",
             "3:s2/0/s3/2/false/Range/1/xyzfoo---[xyzfoo---",
             "4:s2/0/s3/3/false/Range/1/xyzfoo---foo[xyzfoo---foo"})
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
            + "    } else { log('selection.extend not available'); }\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1:s2/0/s2/1/false/Range/1/xyz[xyz",
             "2:s2/0/s2/0/true/Caret/1/["})
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
    @Alerts({"1:s2/0/s2/1/false/Range/1/xyz[xyz",
             "2:s2/1/s2/1/true/Caret/1/["})
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
    @Alerts({"1:s2/0/s2/1/false/Range/1/xyz[xyz",
             "2:null/0/null/0/true/None/0/"})
    public void empty() throws Exception {
        final String jsSnippet = ""
            + "    selection.selectAllChildren(s2);\n"
            + "    alertSelection(selection);\n"
            + "    selection.empty();\n"
            + "    alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1:null/0/null/0/true/None/0/",
             "2:null/0/null/0/true/None/0/",
             "3:s2/1/s3/1/false/Range/1/foo[foo"})
    public void addRange() throws Exception {
        final String jsSnippet = ""
            + "      alertSelection(selection);\n"

            + "      var range = document.createRange();\n"
            + "      range.setStart(s2, 1);\n"
            + "      range.setEnd(s3, 1);\n"
            + "      alertSelection(selection);\n"

            + "      selection.addRange(range);\n"
            + "      alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1:null/0/null/0/true/None/0/",
             "2:s1/1/s3/1/false/Range/1/xyzfoo[xyzfoo",
             "3:null/0/null/0/true/None/0/"})
    public void removeAllRanges() throws Exception {
        final String jsSnippet = ""
            + "      alertSelection(selection);\n"

            + "      var range = document.createRange();\n"
            + "      range.setStart(s1, 1);\n"
            + "      range.setEnd(s3, 1);\n"
            + "      selection.addRange(range);\n"
            + "      alertSelection(selection);\n"

            + "      selection.removeAllRanges();\n"
            + "      alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1:s1/1/s3/1/false/Range/1/xyzfoo[xyzfoo",
             "2:null/0/null/0/true/None/0/"})
    public void removeAllRanges2() throws Exception {
        final String jsSnippet = ""
            + "      var range = document.createRange();\n"
            + "      range.setStart(s1, 1);\n"
            + "      range.setEnd(s3, 1);\n"
            + "      selection.addRange(range);\n"
            + "      alertSelection(selection);\n"

            + "      selection.removeAllRanges();\n"
            + "      alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1:null/0/null/0/true/None/0/",
                       "2:s1/0/s1/1/false/Range/1/abc[abc",
                       "3:null/0/null/0/true/None/0/"},
            FF = {"1:null/0/null/0/true/None/0/",
                  "2:s1/1/s3/1/false/Range/2/abcxyzfoo[abc[xyzfoo",
                  "3:null/0/null/0/true/None/0/"},
            FF_ESR = {"1:null/0/null/0/true/None/0/",
                      "2:s1/1/s3/1/false/Range/2/abcxyzfoo[abc[xyzfoo",
                      "3:null/0/null/0/true/None/0/"})
    @HtmlUnitNYI(CHROME = {"1:null/0/null/0/true/None/0/",
                           "2:s1/0/s1/1/false/Range/2/abcxyzfoo[abc[xyzfoo",
                           "3:null/0/null/0/true/None/0/"},
            EDGE = {"1:null/0/null/0/true/None/0/",
                    "2:s1/0/s1/1/false/Range/2/abcxyzfoo[abc[xyzfoo",
                    "3:null/0/null/0/true/None/0/"},
            FF = {"1:null/0/null/0/true/None/0/",
                  "2:s1/0/s1/1/false/Range/2/abcxyzfoo[abc[xyzfoo",
                  "3:null/0/null/0/true/None/0/"},
            FF_ESR = {"1:null/0/null/0/true/None/0/",
                      "2:s1/0/s1/1/false/Range/2/abcxyzfoo[abc[xyzfoo",
                      "3:null/0/null/0/true/None/0/"})
    public void selectAllChildrenAddRange() throws Exception {
        final String jsSnippet = ""
            + "      alertSelection(selection);\n"

            + "      selection.selectAllChildren(s1);\n"
            + "      var range = document.createRange();\n"
            + "      range.setStart(s1, 1);\n"
            + "      range.setEnd(s3, 1);\n"
            + "      selection.addRange(range);\n"
            + "      alertSelection(selection);\n"

            + "      selection.removeAllRanges();\n"
            + "      alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1:null/0/null/0/true/None/0/",
             "2:s1/0/s1/1/false/Range/1/abc[abc",
             "3:null/0/null/0/true/None/0/"})
    public void addRangeSelectAllChildren() throws Exception {
        final String jsSnippet = ""
            + "      alertSelection(selection);\n"

            + "      var range = document.createRange();\n"
            + "      range.setStart(s1, 1);\n"
            + "      range.setEnd(s3, 1);\n"
            + "      selection.addRange(range);\n"
            + "      selection.selectAllChildren(s1);\n"
            + "      alertSelection(selection);\n"

            + "      selection.removeAllRanges();\n"
            + "      alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"1:null/0/null/0/true/None/0/",
                       "2:s1/0/s1/1/false/Range/1/abc[abc",
                       "3:s1/0/s1/1/false/Range/1/abc[abc",
                       "4:null/0/null/0/true/None/0/"},
            FF = {"1:null/0/null/0/true/None/0/",
                  "2:s1/1/s2/1/false/Range/2/abcxyz[abc[xyz",
                  "3:s2/1/s3/3/false/Range/3/abcxyzfoo---foo[abc[xyz[foo---foo",
                  "4:null/0/null/0/true/None/0/"},
            FF_ESR = {"1:null/0/null/0/true/None/0/",
                      "2:s1/1/s2/1/false/Range/2/abcxyz[abc[xyz",
                      "3:s2/1/s3/3/false/Range/3/abcxyzfoo---foo[abc[xyz[foo---foo",
                      "4:null/0/null/0/true/None/0/"})
    @HtmlUnitNYI(CHROME = {"1:null/0/null/0/true/None/0/",
                           "2:s1/0/s1/1/false/Range/2/abcxyz[abc[xyz",
                           "3:s2/1/s3/3/false/Range/3/abcxyzfoo---foo[abc[xyz[foo---foo",
                           "4:null/0/null/0/true/None/0/"},
            EDGE = {"1:null/0/null/0/true/None/0/",
                    "2:s1/0/s1/1/false/Range/2/abcxyz[abc[xyz",
                    "3:s2/1/s3/3/false/Range/3/abcxyzfoo---foo[abc[xyz[foo---foo",
                    "4:null/0/null/0/true/None/0/"},
            FF = {"1:null/0/null/0/true/None/0/",
                  "2:s1/0/s1/1/false/Range/2/abcxyz[abc[xyz",
                  "3:s2/1/s3/3/false/Range/3/abcxyzfoo---foo[abc[xyz[foo---foo",
                  "4:null/0/null/0/true/None/0/"},
            FF_ESR = {"1:null/0/null/0/true/None/0/",
                      "2:s1/0/s1/1/false/Range/2/abcxyz[abc[xyz",
                      "3:s2/1/s3/3/false/Range/3/abcxyzfoo---foo[abc[xyz[foo---foo",
                      "4:null/0/null/0/true/None/0/"})
    public void addRangeAddRange() throws Exception {
        final String jsSnippet = ""
            + "      alertSelection(selection);\n"

            + "      selection.selectAllChildren(s1);\n"
            + "      var range = document.createRange();\n"
            + "      range.setStart(s1, 1);\n"
            + "      range.setEnd(s2, 1);\n"
            + "      selection.addRange(range);\n"
            + "      alertSelection(selection);\n"

            + "      var range = document.createRange();\n"
            + "      range.setStart(s2, 1);\n"
            + "      range.setEnd(s3, 3);\n"
            + "      selection.addRange(range);\n"
            + "      alertSelection(selection);\n"

            + "      selection.removeAllRanges();\n"
            + "      alertSelection(selection);\n";

        tester(jsSnippet);
    }

    /**
     * Test selection's anchorNode and focusNode after call to removeRange. Surprisingly, this is
     * not null.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1:[object Text]/1/[object Text]/2/false/Range/1/yzfo[yzfo",
             "2:null/0/null/0/true/None/0/",
             "false", "true"})
    public void aLittleBitOfEverything_removeRange() throws Exception {
        final String jsSnippet = ""
            + "    var range = document.createRange();\n"
            + "    range.setStart(s2.firstChild, 1);\n"
            + "    range.setEnd(s3.firstChild, 2);\n"
            + "    selection.addRange(range);\n"
            + "    alertSelection(selection);\n"
            + "    selection.removeRange(range);\n"
            + "    alertSelection(selection);\n"
            + "    log(range.collapsed);\n"
            + "    selection.addRange(range);\n"
            + "    log(selection.getRangeAt(0) == selection.getRangeAt(0));\n";

        tester(jsSnippet);
    }

    private void tester(final String jsSnippet) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload='test()'>\n"
            + "  <span id='s1'>abc</span><span id='s2'>xyz</span><span id='s3'>foo<span>---</span>foo</span>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var x = 1;\n"
            + "  function test() {\n"
            + "    var selection = window.getSelection();\n"
            + "    var s1 = document.getElementById('s1');\n"
            + "    var s2 = document.getElementById('s2');\n"
            + "    var s3 = document.getElementById('s3');\n"
            + "    try {\n"
                        + jsSnippet
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"

            + "  function alertSelection(s) {\n"
            + "    var anchorNode = (s.anchorNode && s.anchorNode.id ? s.anchorNode.id : s.anchorNode);\n"
            + "    var focusNode = (s.focusNode && s.focusNode.id ? s.focusNode.id : s.focusNode);\n"
            + "    var msg = (x++) + ':' + anchorNode + '/' + s.anchorOffset + '/' + focusNode + '/' +\n"
            + "       s.focusOffset + '/' + s.isCollapsed + '/' + s.type + '/' + s.rangeCount + '/' + s;\n"
            + "    for(var i = 0; i < s.rangeCount; i++) {\n"
            + "      msg += '[' + s.getRangeAt(i);\n"
            + "    }\n"
            + "    log(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"", "null-0", "", "null-0", "", "null-0", "", "null-0"},
            FF = {"", "null-0", "", "null-0", "null", "null"},
            FF_ESR = {"", "null-0", "", "null-0", "null", "null"})
    public void getSelection_display() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload='test()'>\n"
            + "  <iframe id='frame1' src='about:blank'></iframe>\n"
            + "  <iframe id='frame2' src='about:blank' style='display: none'></iframe>\n"
            + "  <div style='display: none'>\n"
            + "    <iframe id='frame3' src='about:blank'></iframe>\n"
            + "  </div>\n"

            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function sel(win) {\n"
            + "      if (win.getSelection) {\n"
            + "        var range = win.getSelection();\n"
            + "        log(range);\n"
            + "        if (range) {\n"
            + "          log(range.anchorNode + '-' + range.rangeCount);\n"
            + "        }\n"
            + "      }\n"
            + "    }\n"

            + "    function test() {\n"
            + "      sel(window);\n"
            + "      sel(document.getElementById('frame1').contentWindow);\n"
            + "      sel(document.getElementById('frame2').contentWindow);\n"
            + "      sel(document.getElementById('frame3').contentWindow);\n"
            + "    }\n"
            + "  </script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"", "", ""})
    public void testToString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload='test()'>\n"
            + "<input id='i' value='abcdefghi'/>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var i = document.getElementById('i');\n"
            + "    var s = window.getSelection();\n"
            + "    log(s);\n"
            + "    log('' + s);\n"
            + "    log(s.toString());\n"
            + "  }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }
}
