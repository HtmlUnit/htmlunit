/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc.
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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;

/**
 * Unit tests for {@link Selection}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
@RunWith(BrowserRunner.class)
public class SelectionTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void anchorNode() throws Exception {
        test("", "selection.anchorNode", "x ? x.parentNode.id : x", "null", "s1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void anchorOffset() throws Exception {
        test("", "selection.anchorOffset", "x", "0", "2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void focusNode() throws Exception {
        test("", "selection.focusNode", "x ? x.parentNode.id : x", "null", "s2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void focusOffset() throws Exception {
        test("", "selection.focusOffset", "x", "0", "1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void isCollapsed() throws Exception {
        test("", "selection.isCollapsed", "x", "true", "false");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void rangeCount() throws Exception {
        test("", "selection.rangeCount", "x", "0", "1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void collapse() throws Exception {
        test("selection.collapse(s1, 1)", "selection.focusNode", "x ? x.id : x", "null", "s1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void collapseToEnd() throws Exception {
        test("selection.collapseToEnd()", "selection.anchorNode", "x ? x.parentNode.id : x", "null", "s2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void collapseToStart() throws Exception {
        test("selection.collapseToStart()", "selection.focusNode", "x ? x.parentNode.id : x", "null", "s1");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void extend() throws Exception {
        test("selection.extend(s2, 2)", "selection.focusOffset", "x", "0", "2");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.FF2, Browser.FF3 })
    public void selectAllChildren() throws Exception {
        test("selection.selectAllChildren(document.body)", "selection.anchorOffset", "x", "0", "0");
    }

    private void test(final String action, final String x, final String alert, final String... expected)
        throws Exception {

        final String html = "<html><body onload='test()'>\n"
            + "<span id='s1'>abc</span><span id='s2'>xyz</span>\n"
            + "<input type='button' id='b' onclick='" + action + ";test();' value='click'></input>\n"
            + "<script>\n"
            + "  var selection = window.getSelection();\n"
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

        page.getSelection().setStart(s1Text, 2);
        page.getSelection().setEnd(s2Text, 1);
        input.click();

        assertEquals(expected, actual);
    }

}
