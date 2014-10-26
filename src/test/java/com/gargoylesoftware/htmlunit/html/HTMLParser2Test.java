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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Test class for {@link HTMLParser}.
 *
 * @version $Revision$
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
    @Alerts(DEFAULT = { "beforeafter", "undefined" },
            IE8 = { "before", "after", "TABLE" })
    @NotYetImplemented({ FF, IE11 })
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
    @Alerts(DEFAULT = "Hi!", IE8 = { })
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
    @Alerts(DEFAULT = { })
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
    @Alerts(DEFAULT = { "2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "1", "2", "1", "1", "1", "2", "2", "1", "1", "1", "1" })
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
    @Alerts(DEFAULT = { "2", "2", "3", "3", "2", "2", "3", "2", "2", "3", "2", "2", "3" },
            IE8 = { "2", "1", "2", "1", "1", "1", "2", "2", "1", "1", "1", "1", "1" })
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

    private String createHtmlForChildNodes(final String tagName) {
        return "<html><head><title>test_getChildNodes</title>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  for (var i = 1; i <= 6; i++) {\n"
                + "    alert(document.getElementById('p' + i).childNodes.length);\n"
                + "  }\n"
                + "}\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "<p id='p1'> <" + tagName + "></" + tagName + "> </p>\n"
                + "<p id='p2'><" + tagName + "></" + tagName + "> </p>\n"
                + "<p id='p3'> <" + tagName + "></" + tagName + "></p>\n"
                + "<p id='p4'> <" + tagName + ">something</" + tagName + "> </p>\n"
                + "<p id='p5'><" + tagName + ">something</" + tagName + "> </p>\n"
                + "<p id='p6'> <" + tagName + ">something</" + tagName + "></p>\n"
                + "</body></html>";
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_abbr() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("abbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_acronym() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("acronym"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_a() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("a"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_address() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("address"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_applet() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("applet"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_area() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("area"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_article() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("article"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_aside() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("aside"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_audio() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("audio"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_bgsound() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("bgsound"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "1", "1", "1", "2", "2", "2" })
    public void childNodes_base() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("base"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    @NotYetImplemented({ FF, IE11 })
    public void childNodes_basefont() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("basefont"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_bdo() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("bdo"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_big() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("big"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_blink() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("blink"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_blockquote() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("blockquote"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "0", "0", "0", "1", "1", "1" })
    @NotYetImplemented({ FF, IE11 })
    public void childNodes_body() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("body"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_b() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("b"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "4", "3", "3", "5", "4", "4" },
            IE8 = { "2", "2", "2", "3", "3", "3" })
    @NotYetImplemented
    public void childNodes_br() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("br"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_button() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("button"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_canvas() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("canvas"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_caption() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("caption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_center() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("center"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_cite() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("cite"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_code() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("code"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_datalist() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("datalist"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_dfn() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("dfn"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_dd() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("dd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_del() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("del"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_dir() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("dir"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_div() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("div"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_dl() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("dl"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_dt() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("dt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "3", "3", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_embed() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("embed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_em() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("em"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_fieldset() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("fieldset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_figcaption() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("figcaption"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_figure() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("figure"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_font() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("font"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_form() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("form"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_footer() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("footer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_frame() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("frame"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "1", "1", "3", "3", "2" })
    @NotYetImplemented
    public void childNodes_frameset() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("frameset"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h1() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("h1"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h2() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("h2"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h3() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("h3"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h4() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("h4"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h5() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("h5"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_h6() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("h6"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "0", "0", "0", "1", "1", "1" })
    public void childNodes_head() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("head"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_header() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("header"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_hr() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("hr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "0", "0", "0", "1", "1", "1" })
    public void childNodes_html() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("html"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_iframe() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("iframe"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "3", "3", "2", "4", "4", "3" })
    @NotYetImplemented({ IE8, IE11, FF })
    public void childNodes_image() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("image"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "3", "3", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_img() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("img"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_ins() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("ins"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            CHROME = { "3", "2", "2", "3", "2", "2" },
            FF31 = { "1", "0", "1", "1", "0", "1" },
            IE11 = { "1", "0", "1", "1", "0", "1" })
    @NotYetImplemented({ FF, IE11 })
    public void childNodes_isindex() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("isindex"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_i() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("i"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_kbd() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("kbd"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_keygen() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("keygen"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_label() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("label"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_legend() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("legend"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_listing() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("listing"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_li() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("li"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_link() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("link"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_map() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("map"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_marquee() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("marquee"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_mark() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("mark"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_menu() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("menu"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_meta() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("meta"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_meter() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("meter"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_multicol() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("multicol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_nav() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("nav"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_nextid() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("nextid"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_nobr() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("nobr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_noembed() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("noembed"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_noframes() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("noframes"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_noscript() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("noscript"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_object() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("object"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_ol() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("ol"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_optgroup() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("optgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_option() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("option"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_output() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("output"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_p() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("p"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_param() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("param"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1" },
            IE8 = { "0" })
    @NotYetImplemented
    public void childNodes_plaintext() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("plaintext"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_pre() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("pre"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_progress() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("progress"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_q() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("q"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_ruby() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("ruby"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented()
    public void childNodes_rt() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("rt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented()
    public void childNodes_rp() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("rp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_s() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("s"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_samp() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("samp"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    @NotYetImplemented
    public void childNodes_script() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("script"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_section() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("section"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_select() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("select"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_small() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("small"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_source() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("source"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented
    public void childNodes_spacer() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("spacer"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_span() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("span"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_strike() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("strike"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_strong() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("strong"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    public void childNodes_style() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("style"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_sub() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("sub"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_sup() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("sup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "3", "2" },
            IE8 = { "1", "1", "1", "1", "1", "1" })
    @NotYetImplemented({ FF, IE11 })
    public void childNodes_table() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("table"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_col() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("col"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_colgroup() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("colgroup"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_tbody() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("tbody"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_td() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("td"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_th() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("th"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_tr() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("tr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "1", "2", "2", "1" })
    @NotYetImplemented(IE8)
    public void childNodes_textarea() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("textarea"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_tfoot() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("tfoot"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "1", "1", "1", "1", "1" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_thead() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("thead"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_tt() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("tt"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_time() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("time"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    @NotYetImplemented({ FF, IE11 })
    public void childNodes_title() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("title"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_u() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("u"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_ul() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("ul"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "1", "1", "1", "2", "2", "1" })
    public void childNodes_var() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("var"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "2" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_video() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("video"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "2", "2", "3", "2", "3" },
            IE8 = { "2", "2", "2", "4", "4", "3" })
    @NotYetImplemented(IE8)
    public void childNodes_wbr() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("wbr"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "0", "1", "1", "0", "1" },
            IE8 = { "0", "0", "0", "0", "0", "0" })
    public void childNodes_xmp() throws Exception {
        loadPageWithAlerts2(createHtmlForChildNodes("xmp"));
    }

    /**
     * Conditional comments are removed from the dom.
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = { "<!--[if gt IE 11]><br><![endif]-->", "<!--[if lt IE 11]><br><![endif]-->" },
            IE8 = { "", "<BR>" })
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
}
