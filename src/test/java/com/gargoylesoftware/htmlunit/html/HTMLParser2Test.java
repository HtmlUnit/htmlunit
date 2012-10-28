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
    @NotYetImplemented
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
}
