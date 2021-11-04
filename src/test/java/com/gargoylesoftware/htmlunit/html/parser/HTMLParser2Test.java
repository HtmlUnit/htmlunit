/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html.parser;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;

/**
 * Test class for {@link HTMLParser}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLParser2Test extends WebDriverTestCase {

    /**
     * @throws Exception failure
     */
    @Test
    public void qualified_body() throws Exception {
        final String html = "<html><body>\n"
            + "<wicket:body>whatever</wicket:body>\n"
            + "</body></html>";
        loadPage2(html);
    }

    /**
     * Malformed HTML:
     * &lt;/td&gt;some text&lt;/tr&gt; =&gt; text comes before the table.
     *
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"\\nbeforeafter", "undefined", "undefined"})
    @HtmlUnitNYI(CHROME = {"beforeafter", "undefined", "undefined"},
            EDGE = {"beforeafter", "undefined", "undefined"},
            FF = {"beforeafter", "undefined", "undefined"},
            FF78 = {"beforeafter", "undefined", "undefined"},
            IE = {"beforeafter", "undefined", "undefined"})
    public void htmlTableTextAroundTD() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "function test() {\n"
            + "  var tmp = document.getElementById('testDiv');\n"
            + "  tmp = tmp.firstChild;\n"
            + "  log(tmp.data);\n"
            + "  tmp = tmp.nextSibling;\n"
            + "  log(tmp.data);\n"
            + "  tmp = tmp.nextSibling;\n"
            + "  log(tmp.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><div id='testDiv'>\n"
            + "<table><tr>before<td></td>after</tr></table>\n"
            + "</div></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("TABLE")
    public void htmlTableWhitespaceAroundTD() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var tmp = document.getElementById('testDiv');\n"
            + "  tmp = tmp.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><div id='testDiv'><table><tr> <td></td> </tr></table>\n"
            + "</div></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"H2", "TABLE"})
    public void htmlTableMisplacedElementInside() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var tmp = document.body.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = document.body.firstChild.nextSibling;\n"
            + "  log(tmp.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><table><tr><td></td><h2>Wrong Place</h2></tr></table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"H2", "TABLE"})
    public void htmlTableMisplacedElementInside2() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var tmp = document.body.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = document.body.firstChild.nextSibling;\n"
            + "  log(tmp.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><table><tr><td></td><h2>Wrong Place</h2><td></td></tr></table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"H2", "TABLE"})
    public void htmlTableMisplacedElementInside3() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var tmp = document.body.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = document.body.firstChild.nextSibling;\n"
            + "  log(tmp.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><table><tr><td></td></tr><h2>Wrong Place</h2></table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"H2", "TABLE"})
    public void htmlTableMisplacedElementInside4() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var tmp = document.body.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = document.body.firstChild.nextSibling;\n"
            + "  log(tmp.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><table><tr><td></td></tr><h2>Wrong Place</h2><tr><td></td></tr></table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"H2", "TABLE", "H2", "TABLE", "SCRIPT"})
    public void htmlTableMisplacedElementInside5() throws Exception {
        final String html = "<html><head>\n"
                + "</head>\n"
                + "<body>"
                +   "<table>"
                +     "<tr>"
                +       "<h2>X</h2>"
                +       "<td>1st</td>"
                +     "</tr>\n"

                +       "<table>"
                +         "<tr>"
                +           "<h2>Y</h2>"
                +           "<td>1st</td>"
                +         "</tr>"
                +       "</table>\n"
                +   "</table>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "   var tmp = document.body.firstChild;\n"
                + "   while (tmp != null) {if (tmp.tagName) log(tmp.tagName); tmp = tmp.nextSibling;}\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"H2#x", "TABLE", "H2#y", "TABLE", "H2#z", "TABLE", "H2#a", "TABLE", "SCRIPT"})
    public void htmlTableMisplacedElementInside6() throws Exception {
        final String html = "<html><head>\n"
                + "</head>\n"
                + "<body>"
                +   "<table>"
                +     "<tr>"
                +       "<h2 id='x'>X</h2>"
                +       "<td>x</td>"
                +     "</tr>\n"

                +       "<table>"
                +         "<tr>"
                +           "<h2 id='y'>Y</h2>"
                +           "<td>y</td>"
                +         "</tr>"
                +       "</table>\n"

                +     "<h2 id='z'>Z</h2>"
                +     "<table><tr><td>z</td></table>\n"
                +   "</table>\n"

                +   "<h2 id='a'>A</h2>"
                +   "<table><tr><td>a</td></table>\n"
                +   "</table>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "   var tmp = document.body.firstChild;\n"
                + "   while (tmp != null) {\n"
                + "     if (tmp.tagName) {\n"
                + "       log(tmp.tagName + (tmp.id ? '#' + tmp.id : ''));\n"
                + "     }\n"
                + "     tmp = tmp.nextSibling;\n"
                + "   }\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"4", "TABLE", "TABLE", "SPAN", "SCRIPT"})
    public void tableInsideTable() throws Exception {
        final String html = "<html><head>\n"
                + "</head>\n"
                + "<body>"
                +   "<table>"
                +     "<tr>"
                +       "<td>x</td>"
                +     "</tr>\n"

                +       "<table>"
                +         "<tr>"
                +           "<td>y</td>"
                +         "</tr>"
                +       "</table>\n"

                      // the second table has closed the first one
                +     "<tr>"
                +       "<td><span>z</span></td>"
                +     "</tr>\n"
                +   "</table>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.body.children.length);\n"
                + "  log(document.body.children[0].tagName);\n"
                + "  log(document.body.children[1].tagName);\n"
                + "  log(document.body.children[2].tagName);\n"
                + "  log(document.body.children[3].tagName);\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"4", "TABLE", "TABLE", "SPAN", "SCRIPT"})
    public void tableInsideTableTr() throws Exception {
        final String html = "<html><head>\n"
                + "</head>\n"
                + "<body>"
                +   "<table>"
                +     "<tr>"
                +       "<td>x</td>"
                +     "</tr>\n"

                +     "<tr>"
                +       "<table>"
                +         "<tr>"
                +           "<td>y</td>"
                +         "</tr>"
                +       "</table>\n"
                +     "</tr>\n"

                      // the second table has closed the first one
                +     "<tr>"
                +       "<td><span>z</span></td>"
                +     "</tr>\n"
                +   "</table>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.body.children.length);\n"
                + "  log(document.body.children[0].tagName);\n"
                + "  log(document.body.children[1].tagName);\n"
                + "  log(document.body.children[2].tagName);\n"
                + "  log(document.body.children[3].tagName);\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"2", "TABLE", "SCRIPT"})
    public void tableInsideTableTd() throws Exception {
        final String html = "<html><head>\n"
                + "</head>\n"
                + "<body>"
                +   "<table>"
                +     "<tr>"
                +       "<td>x</td>"
                +     "</tr>\n"

                +     "<tr><td>"
                +       "<table>"
                +         "<tr>"
                +           "<td>y</td>"
                +         "</tr>"
                +       "</table>\n"
                +     "</td></tr>\n"

                      // the second table has closed the first one
                +     "<tr>"
                +       "<td><span>z</span></td>"
                +     "</tr>\n"
                +   "</table>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.body.children.length);\n"
                + "  log(document.body.children[0].tagName);\n"
                + "  log(document.body.children[1].tagName);\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"1", "TABLE"})
    public void scriptInsideTable() throws Exception {
        final String html = "<html><head>\n"
                + "</head>\n"
                + "<body>"
                +   "<table>"
                +     "<tr>"
                +       "<td>1st</td>"
                +     "</tr>\n"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.body.childNodes.length);\n"
                + "  var tmp = document.body.firstChild;\n"
                + "  while (tmp != null) {if (tmp.tagName) log(tmp.tagName); tmp = tmp.nextSibling;}\n"
                + "</script>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"1", "TABLE"})
    public void scriptInsideTableRows() throws Exception {
        final String html = "<html><head>\n"
                + "</head>\n"
                + "<body>"
                +   "<table>"
                +     "<tr>"
                +       "<td>1st</td>"
                +     "</tr>\n"
                +     "<tr>"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.body.childNodes.length);\n"
                + "  var tmp = document.body.firstChild;\n"
                + "  while (tmp != null) {if (tmp.tagName) log(tmp.tagName); tmp = tmp.nextSibling;}\n"
                + "</script>\n"

                +     "</tr>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"1", "TABLE"})
    public void scriptInsideTableData() throws Exception {
        final String html = "<html><head>\n"
                + "</head>\n"
                + "<body>"
                +   "<table>"
                +     "<tr>"
                +       "<td>1st</td>"
                +     "</tr>\n"
                +     "<tr>"
                +       "<td>"

                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  log(document.body.childNodes.length);\n"
                + "  var tmp = document.body.firstChild;\n"
                + "  while (tmp != null) {if (tmp.tagName) log(tmp.tagName); tmp = tmp.nextSibling;}\n"
                + "</script>\n"

                +       "</td>"
                +     "</tr>\n"
                + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"TABLE", "TABLE"})
    public void htmlTableClosesAnother() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + LOG_TITLE_FUNCTION
            + "  var tmp = document.body.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = document.body.firstChild.nextSibling;\n"
            + "  log(tmp.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onload='test()'>"
            + "<table>"
            +   "<tr>"
            +     "<td>a</td>"
            +   "</tr>"

            +   "<table>"
            +     "<tr>"
            +       "<td>o</td>"
            +     "</tr>"
            +   "</table>\n"

            + "</table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"TABLE", "TABLE"})
    public void htmlTableClosesAnotherInsideTr() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var tmp = document.body.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = document.body.firstChild.nextSibling;\n"
            + "  log(tmp.tagName);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onload='test()'>"
            + "<table>"
            +   "<tr>"

            +   "<table>"
            +     "<tr>"
            +       "<td>o</td>"
            +     "</tr>"
            +   "</table>\n"

            +   "</tr>"
            + "</table>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"TABLE", "null"})
    public void htmlTableNotClosesOnotherInsideTd() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var tmp = document.body.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = document.body.firstChild.nextSibling;\n"
            + "  log(tmp);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"

            + "<body onload='test()'>"
            + "<table>"
            +   "<tr>"
            +     "<td><span>"

            +   "<table>"
            +     "<tr>"
            +       "<td>o</td>"
            +     "</tr>"
            +   "</table>\n"

            +     "</span></td>"
            +   "</tr>"
            + "</table>"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("Hi!")
    @NotYetImplemented
    public void unclosedCommentsInScript() throws Exception {
        final String html = "<html><body>\n"
            + "<script><!--\n"
            + "alert('Hi!');\n"
            + "</script>\n"
            + "<h1>Ho!</h1>\n"
            + "<!-- some comment -->\n"
            + "<h1>Hu!</h1>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * This tests for a bug in NekoHTML.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"P", "BUTTON", "DIV"})
    public void divInsideButton() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var tmp = document.getElementById('myP');\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = tmp.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = tmp.firstChild.tagName;\n"
            + "  log(tmp);\n"
            + "} catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<p id='myP'><button><div>Test</div></button></p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * This tests for a bug in NekoHTML.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"P", "LABEL", "OBJECT"})
    public void objectInsideLabel() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var tmp = document.getElementById('myP');\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = tmp.firstChild;\n"
            + "  log(tmp.tagName);\n"
            + "  tmp = tmp.firstChild.tagName;\n"
            + "  log(tmp);\n"
            + "} catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<p id='myP'><label><object></object></label></p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * This tests for a bug in NekoHTML.
     * Without setting a property NekoHTML closes all inline tags
     * when a table start tag is detected. This is ok from the spec
     * but different with real browsers.
     *
     * @throws Exception on test failure
     */
    @Test
    public void tableClosesInlineTags() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var myP = document.getElementById('myP');\n"
                + "    for(var i = 0; i < myP.childNodes.length; i++) {\n"
                + "      var myNode = myP.childNodes[i];\n"
                + "      if (myNode.nodeType == 1 && myNode.nodeName != 'TABLE') {\n"
                + "        var hasTable = false;\n"
                + "        for(var j = 0; j<myNode.childNodes.length; j++) {\n"
                + "          if (myNode.childNodes[j].nodeName == 'TABLE') {\n"
                + "            hasTable = true;\n"
                + "          }\n"
                + "        }\n"
                + "        if (!hasTable) {\n"
                + "          log('<' + myNode.nodeName + '>');\n"
                + "        }\n"
                + "      }\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "\n"
                + "<body onload='test()'>\n"
                + "\n"
                + "<p id='myP'>\n"
                + "  <a><table></table></a>\n"
                + "  <abbr><table></table></abbr>\n"
                + "  <acronym><table></table></acronym>\n"
                + "  <b><table></table></b>\n"
                + "  <bdo><table></table></bdo>\n"
                + "  <big><table></table></big>\n"
                + "  <button><table></table></button>\n"
                + "  <cite><table></table></cite>\n"
                + "  <code><table></table></code>\n"
                + "  <del><table></table></del>\n"
                + "  <dfn><table></table></dfn>\n"
                + "  <em><table></table></em>\n"
                + "  <font><table></table></font>\n"
                + "  <i><table></table></i>\n"
                + "  <ins><table></table></ins>\n"
                + "  <kbd><table></table></kbd>\n"
                + "  <label><table></table></label>\n"
                + "  <map><table></table></map>\n"
                + "  <q><table></table></q>\n"
                + "  <samp><table></table></samp>\n"
                + "  <small><table></table></small>\n"
                + "  <span><table></table></span>\n"
                + "  <strong><table></table></strong>\n"
                + "  <sub><table></table></sub>\n"
                + "  <sup><table></table></sup>\n"
                + "  <tt><table></table></tt>\n"
                + "  <var><table></table></var>\n"
                + "</p>\n"
                + "\n"
                + "</body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2"})
    public void childNodes_p_parent() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  for (var i = 1; i <= 12; i++) {\n"
            + "    log(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'><input> </p>\n"
            + "<p id='p2'> <input></p>\n"
            + "<p id='p3'> <input> </p>\n"
            + "<p id='p4'> <a></a> </p>\n"
            + "<p id='p5'><a></a> </p>\n"
            + "<p id='p6'> <a></a></p>\n"
            + "<p id='p7'> <a href='hi'>there</a> </p>\n"
            + "<p id='p8'><a href='hi'>there</a> </p>\n"
            + "<p id='p9'> <a href='hi'>there</a></p>\n"
            + "<p id='p10'> <a href='hi'></a> </p>\n"
            + "<p id='p11'><a href='hi'></a> </p>\n"
            + "<p id='p12'> <a href='hi'></a></p>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2", "3"})
    public void childNodes_f() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  for (var i = 1; i <= 13; i++) {\n"
            + "    log(document.getElementById('f' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form id='f1'><input> </form>\n"
            + "<form id='f2'> <input></form>\n"
            + "<form id='f3'> <input> </form>\n"
            + "<form id='f4'> <a></a> </form>\n"
            + "<form id='f5'><a></a> </form>\n"
            + "<form id='f6'> <a></a></form>\n"
            + "<form id='f7'> <a href='hi'>there</a> </form>\n"
            + "<form id='f8'><a href='hi'>there</a> </form>\n"
            + "<form id='f9'> <a href='hi'>there</a></form>\n"
            + "<form id='f10'> <a href='hi'></a> </form>\n"
            + "<form id='f11'><a href='hi'></a> </form>\n"
            + "<form id='f12'> <a href='hi'></a></form>\n"
            + "<form id='f13'> <div> </div> </form>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Conditional comments are removed from the dom.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"<!--[if gt IE 11]><br><![endif]-->", "<!--[if lt IE 11]><br><![endif]-->"})
    public void ieConditionalCommentsNotInDom() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var tmp = document.getElementById('my1');\n"
            + "  log(tmp.innerHTML);\n"
            + "  tmp = document.getElementById('my2');\n"
            + "  log(tmp.innerHTML);\n"
            + "} catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='my1'><!--[if gt IE 11]><br><![endif]--></div>\n"
            + "  <div id='my2'><!--[if lt IE 11]><br><![endif]--></div>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test incorrect parsing of LABEL within A tag. Fixed in NekoHTML 1.9.19.
     * @see <a href="http://sf.net/p/htmlunit/bugs/1547/">Bug #1547</a>
     * @throws Exception on test failure
     */
    @Test
    @Alerts("1")
    public void acceptLabelWithinAnchor() throws Exception {
        final String html = "<html><body>\n"
            + "<a href='foo'>\n"
            + "<label>XL</label>\n"
            + "</a>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log(document.links.length)\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @see <a href="http://sf.net/p/htmlunit/bugs/1423/">Bug #1423</a>
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"<var data=\"f\"> <a href=\"#\">a</a> <div>d</div> <li>l</li> </var> ", "3"})
    public void varInsideUl() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<ul id='myUl'>\n"
            +   "<var data='f'>\n"
            +     "<a href='#'>a</a>\n"
            +     "<div>d</div>\n"
            +     "<li>l</li>\n"
            +   "</var>\n"
            + "</ul>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var tmp = document.getElementById('myUl');\n"
            + "  log(tmp.innerHTML);\n"
            + "  log(tmp.childNodes.length);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @see <a href="http://sf.net/p/htmlunit/bugs/1046/">Bug #1046</a>
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"<table> <tbody><tr> <td>data</td> </tr> </tbody></table> ", "3"})
    public void tableInsideAnchor() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            +  "<a id='myA' href='#'>\n"
            +   "<table>\n"
            +     "<tr>\n"
            +       "<td>data</td>\n"
            +     "</tr>\n"
            +   "</table>\n"
            +  "</a>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var tmp = document.getElementById('myA');\n"
            + "  log(tmp.innerHTML);\n"
            + "  log(tmp.childNodes.length);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Issue #1825.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = {"<iframe></div></body></html></iframe>", "1",
                       "1", "IFRAME", "null", "1",
                       "3", "#text", "</div></body></html>"},
            IE = {"<iframe>&lt;/div&gt;&lt;/body&gt;&lt;/html&gt;</iframe>", "1",
                  "1", "IFRAME", "null", "1",
                  "3", "#text", "</div></body></html>"})
    @HtmlUnitNYI(CHROME = {"<iframe>&lt;/div&gt;&lt;/body&gt;&lt;/html&gt;</iframe>", "1",
                           "1", "IFRAME", "null", "1",
                           "3", "#text", "</div></body></html>"},
            EDGE = {"<iframe>&lt;/div&gt;&lt;/body&gt;&lt;/html&gt;</iframe>", "1",
                    "1", "IFRAME", "null", "1",
                    "3", "#text", "</div></body></html>"},
            FF = {"<iframe>&lt;/div&gt;&lt;/body&gt;&lt;/html&gt;</iframe>", "1",
                  "1", "IFRAME", "null", "1",
                  "3", "#text", "</div></body></html>"},
            FF78 = {"<iframe>&lt;/div&gt;&lt;/body&gt;&lt;/html&gt;</iframe>", "1",
                    "1", "IFRAME", "null", "1",
                    "3", "#text", "</div></body></html>"})
    public void selfClosingIframe() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var tmp = document.getElementById('myDiv');\n"
            + "  log(tmp.innerHTML);\n"
            + "  log(tmp.childNodes.length);\n"

            + "  var child = tmp.childNodes[0];\n"
            + "  log(child.nodeType);\n"
            + "  log(child.nodeName);\n"
            + "  log(child.nodeValue);\n"

            + "  log(child.childNodes.length);\n"
            + "  var child2 = child.childNodes[0];\n"
            + "  log(child2.nodeType);\n"
            + "  log(child2.nodeName);\n"
            + "  log(child2.nodeValue);\n"

            + "} catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv'><iframe/></div>"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Issue #1842.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"2", "1-1#DL", "0-1#DT", "1-1#DL", "0-1#DT"})
    public void dlShouldCloseDt() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var tmp = document.getElementById('myBody');\n"
            + "  log(tmp.childNodes.length);\n"

            + "  var child = tmp.childNodes[0];\n"
            + "  log(child.childNodes.length + '-' + child.nodeType + '#' +child.nodeName);\n"

            + "  var child2 = child.childNodes[0];\n"
            + "  log(child2.childNodes.length + '-' + child2.nodeType + '#' +child2.nodeName);\n"

            + "  var child = tmp.childNodes[1];\n"
            + "  log(child.childNodes.length + '-' + child.nodeType + '#' +child.nodeName);\n"

            + "  var child2 = child.childNodes[0];\n"
            + "  log(child2.childNodes.length + '-' + child2.nodeType + '#' +child2.nodeName);\n"

            + "} catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body id='myBody' onload='test()'>"
            + "<DL><DT></DL>"
            + "<DL><DT></DL>"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * As of version 2.43.0 this fails with a stack overflow.
     *
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"1", "1-1#P"})
    public void innerHtmlParagraph() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "try {\n"
            + "  var tmp = document.getElementById('myP');\n"
            + "  tmp.innerHTML = '<p>HtmlUnit</p>';\n"
            + "  log(tmp.childNodes.length);\n"

            + "  var child = tmp.childNodes[0];\n"
            + "  log(child.childNodes.length + '-' + child.nodeType + '#' + child.nodeName);\n"

            + "} catch(e) { log('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "  <p id='myP'>Test</p>"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
