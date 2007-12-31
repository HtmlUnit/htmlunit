/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
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
     * Create an instance
     *
     * @param name Name of the test
     */
    public MouseEventTest(final String name) {
        super(name);
    }
    
    /**
     * @throws Exception if the test fails
     */
    public void testEventCoordinates() throws Exception {
        testEventCoordinates(BrowserVersion.FIREFOX_2);
        testEventCoordinates(BrowserVersion.INTERNET_EXPLORER_6_0);
    }

    private void testEventCoordinates(final BrowserVersion browser) throws Exception {
        final URL url = getClass().getClassLoader().getResource("event_coordinates.html");
        assertNotNull(url);

        final WebClient client = new WebClient(browser);
        final HtmlPage page = (HtmlPage) client.getPage(url);
        assertEquals("Mouse Event coordinates", page.getTitleText());
        
        final HtmlTextArea textarea = (HtmlTextArea) page.getHtmlElementById("myTextarea");
        assertEquals("", textarea.asText());

        ((ClickableElement) page.getHtmlElementById("div1")).click();
        assertEquals("Click on DIV(id=div1): true, true, false, false", textarea.asText());
        textarea.reset();

        ((ClickableElement) page.getHtmlElementById("span1")).click();
        assertEquals("Click on SPAN(id=span1): true, true, true, false", textarea.asText());
        textarea.reset();

        ((ClickableElement) page.getHtmlElementById("span2")).click();
        assertEquals("Click on SPAN(id=span2): true, false, false, true", textarea.asText());
        textarea.reset();

        textarea.click();
        assertEquals("Click on TEXTAREA(id=myTextarea): true, false, false, false", textarea.asText());
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testInitMouseEvent() throws Exception {
        final String html = "<html><body><script>\n"
            + "  var e = document.createEvent('MouseEvents');\n"
            + "  e.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0, true, true, true, true, 0, null);\n"
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
            + "</script></body></html>\n";
        final List actual = new ArrayList();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);
        final String[] expected = {"click", "true", "true", "true", "0", "0", "0", "0", "true", "true", "true"};
        assertEquals(expected, actual);
    }

}
