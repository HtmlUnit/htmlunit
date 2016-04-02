/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

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
        final String html = "<html><body>"
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
    @Alerts({ "beforeafter", "undefined" })
    @NotYetImplemented
    public void testHtmlTableTextAroundTD() throws Exception {
        final String html = "<html><head><title>test_Table</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var tmp = document.getElementById('testDiv');\n"
            + "  tmp = tmp.firstChild;\n"
            + "  alert(tmp.data)\n"
            + "  tmp = tmp.nextSibling;\n"
            + "  alert(tmp.data)\n"
            + "  tmp = tmp.nextSibling;\n"
            + "  alert(tmp.tagName)\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><div id='testDiv'>"
            + "<table><tr>before<td></td>after</tr></table>"
            + "</div></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("TABLE")
    public void testHtmlTableWhitespaceAroundTD() throws Exception {
        final String html = "<html><head><title>test_Table</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var tmp = document.getElementById('testDiv');\n"
            + "  tmp = tmp.firstChild;\n"
            + "  alert(tmp.tagName)\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'><div id='testDiv'>"
            + "<table><tr> <td></td> </tr></table>"
            + "</div></body></html>";

        loadPageWithAlerts2(html);
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
     * This tests for an bug in NekoHTML.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "P", "BUTTON", "DIV" })
    public void divInsideButton() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var tmp = document.getElementById('myP');\n"
            + "  alert(tmp.tagName)\n"
            + "  tmp = tmp.firstChild;\n"
            + "  alert(tmp.tagName)\n"
            + "  tmp = tmp.firstChild.tagName;\n"
            + "  alert(tmp)\n"
            + "} catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<p id='myP'><button><div>Test</div></button></p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * This tests for an bug in NekoHTML.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "P", "LABEL", "OBJECT" })
    public void objectInsideLabel() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var tmp = document.getElementById('myP');\n"
            + "  alert(tmp.tagName)\n"
            + "  tmp = tmp.firstChild;\n"
            + "  alert(tmp.tagName)\n"
            + "  tmp = tmp.firstChild.tagName;\n"
            + "  alert(tmp)\n"
            + "} catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<p id='myP'><label><object></object></label></p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
                + "   function test() {\n"
                + "     var myP = document.getElementById('myP');\n"
                + "     for(var i = 0; i < myP.childNodes.length; i++) {\n"
                + "       var myNode = myP.childNodes[i];\n"
                + "       if (myNode.nodeType == 1 && myNode.nodeName != 'TABLE') {\n"
                + "         var hasTable = false;\n"
                + "         for(var j = 0; j<myNode.childNodes.length; j++) {\n"
                + "           if (myNode.childNodes[j].nodeName == 'TABLE') {\n"
                + "             hasTable = true;\n"
                + "           }\n"
                + "         }\n"
                + "         if (!hasTable) {\n"
                + "           alert('<' + myNode.nodeName + '>');\n"
                + "         }\n"
                + "       }\n"
                + "     }\n"
                + "   }\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2" })
    public void childNodes_p_parent() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 12; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
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

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2", "3" })
    public void childNodes_f() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 13; i++) {\n"
            + "    alert(document.getElementById('f' + i).childNodes.length);\n"
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
        loadPageWithAlerts2(html);
    }

    /**
     * Conditional comments are removed from the dom.
     * @throws Exception on test failure
     */
    @Test
    @Alerts({ "<!--[if gt IE 11]><br><![endif]-->", "<!--[if lt IE 11]><br><![endif]-->" })
    public void ieConditionalCommentsNotInDom() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "try {\n"
            + "  var tmp = document.getElementById('my1');\n"
            + "  alert(tmp.innerHTML)\n"
            + "  tmp = document.getElementById('my2');\n"
            + "  alert(tmp.innerHTML)\n"
            + "} catch(e) { alert('exception'); }\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='my1'><!--[if gt IE 11]><br><![endif]--></div>\n"
            + "  <div id='my2'><!--[if lt IE 11]><br><![endif]--></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test incorrect parsing of LABEL within A tag. Fixed in NekoHTML 1.9.19.
     * @see <a href="http://sf.net/p/htmlunit/bugs/1547/">Bug 1547</a>
     * @throws Exception on test failure
     */
    @Test
    @Alerts("1")
    public void acceptLabelWithinAnchor() throws Exception {
        final String html = "<html><body>\n"
            + "<a href='foo'>\n"
            + "<label>XL</label>\n"
            + "</a>\n"
            + "<script>alert(document.links.length)</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @see <a href="http://sf.net/p/htmlunit/bugs/1423/">Bug 1423</a>
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"\n<var data=\"f\">\n<a href=\"#\">a</a>\n<div>d</div>\n<li>l</li>\n</var>\n", "3"})
    public void varInsideUl() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head></head>"
            + "<body>\n"
            + "<ul id='myUl'>\n"
            +   "<var data='f'>\n"
            +     "<a href='#'>a</a>\n"
            +     "<div>d</div>\n"
            +     "<li>l</li>\n"
            +   "</var>\n"
            + "</ul>\n"
            + "<script>"
            + "  var tmp = document.getElementById('myUl');\n"
            + "  alert(tmp.innerHTML);\n"
            + "  alert(tmp.childNodes.length);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @see <a href="http://sf.net/p/htmlunit/bugs/1046/">Bug 1046</a>
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"\n<table>\n<tbody><tr>\n<td>data</td>\n</tr>\n</tbody></table>\n", "3"})
    public void tableInsideAnchor() throws Exception {
        final String html =
            HtmlPageTest.STANDARDS_MODE_PREFIX_
            + "<html>\n"
            + "<head></head>"
            + "<body>\n"
            +  "<a id='myA' href='#'>\n"
            +   "<table>\n"
            +     "<tr>\n"
            +       "<td>data</td>\n"
            +     "</tr>\n"
            +   "</table>\n"
            +  "</a>\n"
            + "<script>"
            + "  var tmp = document.getElementById('myA');\n"
            + "  alert(tmp.innerHTML);\n"
            + "  alert(tmp.childNodes.length);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
