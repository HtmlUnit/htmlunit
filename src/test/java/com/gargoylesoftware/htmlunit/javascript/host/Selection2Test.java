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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.AlertHandler;
import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SimpleRange;

/**
 * Unit tests for {@link Selection}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class Selection2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "", "cx" })
    public void stringValue_FF() throws Exception {
        test("", "selection", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "[object]", "[object]" })
    public void stringValue_IE() throws Exception {
        test("", "selection", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "null", "s1" })
    public void anchorNode() throws Exception {
        test("", "selection.anchorNode", "x ? x.parentNode.id : x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "2" })
    public void anchorOffset() throws Exception {
        test("", "selection.anchorOffset", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "null", "s2" })
    public void focusNode() throws Exception {
        test("", "selection.focusNode", "x ? x.parentNode.id : x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "1" })
    public void focusOffset() throws Exception {
        test("", "selection.focusOffset", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "true", "false" })
    public void isCollapsed() throws Exception {
        test("", "selection.isCollapsed", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "1" })
    public void rangeCount() throws Exception {
        test("", "selection.rangeCount", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "1" })
    public void rangeCount2() throws Exception {
        test("selection.collapseToEnd()", "selection.rangeCount", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "null", "null" })
    public void removeAllRanges_anchorNode() throws Exception {
        test("selection.removeAllRanges()", "selection.anchorNode", "x ? x.parentNode.id : x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "0" })
    public void removeAllRanges_anchorOffset() throws Exception {
        test("selection.removeAllRanges()", "selection.anchorOffset", "x ? x.parentNode.id : x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "null", "null" })
    public void removeAllRanges_focusNode() throws Exception {
        test("selection.removeAllRanges()", "selection.focusNode", "x ? x.parentNode.id : x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "0" })
    public void removeAllRanges_focusOffset() throws Exception {
        test("selection.removeAllRanges()", "selection.focusOffset", "x ? x.parentNode.id : x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "null", "s1" })
    public void collapse() throws Exception {
        test("selection.collapse(s1, 1)", "selection.focusNode", "x ? x.id : x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "null", "s2" })
    public void collapseToEnd() throws Exception {
        test("selection.collapseToEnd()", "selection.anchorNode", "x ? x.parentNode.id : x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "null", "s1" })
    public void collapseToStart() throws Exception {
        test("selection.collapseToStart()", "selection.focusNode", "x ? x.parentNode.id : x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "2" })
    public void extend() throws Exception {
        test("selection.extend(s2, 2)", "selection.focusOffset", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "0", "0" })
    public void selectAllChildren() throws Exception {
        test("selection.selectAllChildren(document.body)", "selection.anchorOffset", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "none", "cx" })
    public void getRangeAt() throws Exception {
        test("", "selection.rangeCount > 0 ? selection.getRangeAt(0) : 'none'", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "", "true" })
    public void getRangeAt_prototype() throws Exception {
        test("", "selection.rangeCount > 0 ? (selection.getRangeAt(0) instanceof Range) : ''", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "None", "None" })
    public void empty() throws Exception {
        test("selection.empty()", "selection.type", "x");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts({ "[object]", "[object]" })
    public void createRange() throws Exception {
        test("", "selection.createRange()", "x");
    }

    private void test(final String action, final String x, final String alert)
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
        final DomNode s1Text = page.getHtmlElementById("s1").getFirstChild();
        final DomNode s2Text = page.getHtmlElementById("s2").getFirstChild();
        final HtmlInput input = page.getHtmlElementById("b");

        final org.w3c.dom.ranges.Range range = new SimpleRange();
        range.setStart(s1Text, 2);
        range.setEnd(s2Text, 1);
        page.setSelectionRange(range);
        input.click();

        assertEquals(getExpectedAlerts(), actual);
    }

}
