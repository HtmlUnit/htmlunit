/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;

/**
 * Tests for mouse events support.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class MouseEventTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testEventCoordinates() throws Exception {
        testEventCoordinates(BrowserVersion.FIREFOX_2);
        testEventCoordinates(BrowserVersion.INTERNET_EXPLORER_6);
    }

    private void testEventCoordinates(final BrowserVersion browser) throws Exception {
        final URL url = getClass().getClassLoader().getResource("event_coordinates.html");
        assertNotNull(url);

        final WebClient client = new WebClient(browser);
        final HtmlPage page = client.getPage(url);
        assertEquals("Mouse Event coordinates", page.getTitleText());

        final HtmlTextArea textarea = page.getHtmlElementById("myTextarea");
        assertEquals("", textarea.asText());

        page.<HtmlElement>getHtmlElementById("div1").click();
        assertEquals("Click on DIV(id=div1): true, true, false, false" + LINE_SEPARATOR, textarea.asText());
        textarea.reset();

        page.<HtmlElement>getHtmlElementById("span1").click();
        assertEquals("Click on SPAN(id=span1): true, true, true, false" + LINE_SEPARATOR, textarea.asText());
        textarea.reset();

        page.<HtmlElement>getHtmlElementById("span2").click();
        assertEquals("Click on SPAN(id=span2): true, false, false, true" + LINE_SEPARATOR, textarea.asText());
        textarea.reset();

        textarea.click();
        assertEquals("Click on TEXTAREA(id=myTextarea): true, false, false, false" + LINE_SEPARATOR, textarea.asText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testInitMouseEvent() throws Exception {
        final String html = "<html><body><script>\n"
            + "  var e = document.createEvent('MouseEvents');\n"
            + "  e.initMouseEvent('click', true, true, window, 0, 1, 2, 3, 4, true, true, true, true, 0, null);\n"
            + "  alert(e.type);\n"
            + "  alert(e.bubbles);\n"
            + "  alert(e.cancelable);\n"
            + "  alert(e.view == window);\n"
            + "  alert(e.screenX);\n"
            + "  alert(e.screenY);\n"
            + "  alert(e.clientX);\n"
            + "  alert(e.clientY);\n"
            + "  alert(e.ctrlKey);\n"
            + "  alert(e.altKey);\n"
            + "  alert(e.shiftKey);\n"
            + "</script></body></html>";
        final List<String> actual = new ArrayList<String>();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);
        final String[] expected = {"click", "true", "true", "true", "1", "2", "3", "4", "true", "true", "true"};
        assertEquals(expected, actual);
    }

}
