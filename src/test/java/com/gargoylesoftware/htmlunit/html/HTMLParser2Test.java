/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Test class for {@link HTMLParser}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
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
    @Alerts(DEFAULT = { "beforeafter", "undefined" },
            FF3_6 = { "before", "after", "TABLE" },
            IE = { "before", "after", "TABLE" })
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
    @Alerts(FF = "Hi!")
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
     * Without setting a property NekoHTML closes all inline tags
     * when a table start tag is detected. This is ok from the spec
     * but different with real browsers.
     *
     * @throws Exception on test failure
     */
    @Test
    @Alerts(FF3_6 = { "<ABBR>", "<ACRONYM>", "<CITE>", "<CODE>",
                    "<DFN>", "<KBD>", "<LABEL>", "<SAMP>", "<VAR>" },
            DEFAULT = { }
    )
    @NotYetImplemented(Browser.FF3_6)
    public void tableClosesInlineTags() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "   function test() {\n"
                + "     var myP = document.getElementById('myP');\n"
                + "     for(var i=0; i<myP.childNodes.length; i++) {\n"
                + "       var myNode = myP.childNodes[i];\n"
                + "       if (myNode.nodeType == 1 && myNode.nodeName != 'TABLE') {\n"
                + "         var hasTable = false;\n"
                + "         for(var j=0; j<myNode.childNodes.length; j++) {\n"
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
    @Alerts(IE = { "2", "1", "2", "1", "1", "1", "2", "2", "1", "1", "1", "1" },
            FF = { "2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2" })
    public void childNodes_p() throws Exception {
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
    @Alerts(IE = { "2", "1", "2", "1", "1", "1", "2", "2", "1", "1", "1", "1", "1" },
            FF = { "2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2", "3" })
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
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "1", "1", "1", "2", "2", "1" }, FF = { "3", "2", "2", "3", "2", "2" })
    public void childNodes_span() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'> <span></span> </p>\n"
            + "<p id='p2'><span></span> </p>\n"
            + "<p id='p3'> <span></span></p>\n"
            + "<p id='p4'> <span>something</span> </p>\n"
            + "<p id='p5'><span>something</span> </p>\n"
            + "<p id='p6'> <span>something</span></p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "1", "1", "1", "2", "2", "1" }, FF = { "3", "2", "2", "3", "2", "2" })
    public void childNodes_strong() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'> <strong></strong> </p>\n"
            + "<p id='p2'><strong></strong> </p>\n"
            + "<p id='p3'> <strong></strong></p>\n"
            + "<p id='p4'> <strong>something</strong> </p>\n"
            + "<p id='p5'><strong>something</strong> </p>\n"
            + "<p id='p6'> <strong>something</strong></p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "1", "1", "1", "2", "2", "1" }, FF = { "3", "2", "2", "3", "2", "2" })
    public void childNodes_i() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'> <i></i> </p>\n"
            + "<p id='p2'><i></i> </p>\n"
            + "<p id='p3'> <i></i></p>\n"
            + "<p id='p4'> <i>something</i> </p>\n"
            + "<p id='p5'><i>something</i> </p>\n"
            + "<p id='p6'> <i>something</i></p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "1", "1", "1", "2", "2", "1" }, FF = { "3", "2", "2", "3", "2", "2" })
    public void childNodes_b() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'> <b></b> </p>\n"
            + "<p id='p2'><b></b> </p>\n"
            + "<p id='p3'> <b></b></p>\n"
            + "<p id='p4'> <b>something</b> </p>\n"
            + "<p id='p5'><b>something</b> </p>\n"
            + "<p id='p6'> <b>something</b></p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "1", "1", "1", "2", "2", "1" }, FF = { "3", "2", "2", "3", "2", "2" })
    public void childNodes_u() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'> <u></u> </p>\n"
            + "<p id='p2'><u></u> </p>\n"
            + "<p id='p3'> <u></u></p>\n"
            + "<p id='p4'> <u>something</u> </p>\n"
            + "<p id='p5'><u>something</u> </p>\n"
            + "<p id='p6'> <u>something</u></p>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(IE = { "1", "1", "1", "2", "2", "1" }, FF = { "3", "2", "2", "3", "2", "2" })
    public void childNodes_font() throws Exception {
        final String html = "<html><head><title>test_getChildNodes</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  for (var i = 1; i <= 6; i++) {\n"
            + "    alert(document.getElementById('p' + i).childNodes.length);\n"
            + "  }\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<p id='p1'> <font></font> </p>\n"
            + "<p id='p2'><font></font> </p>\n"
            + "<p id='p3'> <font></font></p>\n"
            + "<p id='p4'> <font>something</font> </p>\n"
            + "<p id='p5'><font>something</font> </p>\n"
            + "<p id='p6'> <font>something</font></p>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

}
