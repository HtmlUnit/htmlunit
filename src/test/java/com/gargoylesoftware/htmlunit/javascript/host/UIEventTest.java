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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Unit tests for {@link UIEvent}.
 *
 * @version $Revision$
 * @author Daniel Gredler
 */
public class UIEventTest extends WebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testDetail() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function alertDetail(e) {\n"
            + "    alert(e.detail);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='alertDetail(event)'>\n"
            + "<div id='a' onclick='alertDetail(event)'>abc</div>\n"
            + "<div id='b' ondblclick='alertDetail(event)'>xyz</div>\n"
            + "</body></html>";
        final String[] expected = {"undefined", "1", "2"};
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_3, html, actual);
        page.<HtmlDivision>getHtmlElementById("a").click();
        page.<HtmlDivision>getHtmlElementById("b").dblClick();
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testView() throws Exception {
        final String html =
              "<html><body onload='alertView(event)'><script>\n"
            + "  function alertView(e) {\n"
            + "    alert(e.view);\n"
            + "  }\n"
            + "</script>\n"
            + "<form><input type='button' id='b' onclick='alertView(event)'></form>\n"
            + "</body></html>";
        final String[] expected = {"undefined", "[object Window]"};
        final List<String> actual = new ArrayList<String>();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_3, html, actual);
        final HtmlButtonInput button = page.getHtmlElementById("b");
        button.click();
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testInitUIEvent() throws Exception {
        final String html = "<html><body><script>\n"
            + "  var e = document.createEvent('UIEvents');\n"
            + "  e.initUIEvent('click', true, true, window, 7);\n"
            + "  alert(e.type);\n"
            + "  alert(e.bubbles);\n"
            + "  alert(e.cancelable);\n"
            + "  alert(e.view == window);\n"
            + "  alert(e.detail);\n"
            + "</script></body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_3, html, actual);
        final String[] expected = {"click", "true", "true", "true", "7"};
        assertEquals(expected, actual);
    }
}
