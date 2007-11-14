/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import java.util.ArrayList;
import java.util.List;

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
     * Creates an instance.
     *
     * @param name name of the test
     */
    public UIEventTest(final String name) {
        super(name);
    }

    /**
     * @throws Exception if an error occurs
     */
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
        final List actual = new ArrayList();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, actual);
        ((HtmlDivision) page.getHtmlElementById("a")).click();
        ((HtmlDivision) page.getHtmlElementById("b")).dblClick();
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
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
        final List actual = new ArrayList();
        final HtmlPage page = loadPage(BrowserVersion.FIREFOX_2, html, actual);
        final HtmlButtonInput button = (HtmlButtonInput) page.getHtmlElementById("b");
        button.click();
        assertEquals(expected, actual);
    }

    /**
     * @throws Exception if an error occurs
     */
    public void testInitUIEvent() throws Exception {
        final String html = "<html><body><script>\n"
            + "  var e = document.createEvent('UIEvents');\n"
            + "  e.initUIEvent('click', true, true, window, 7);\n"
            + "  alert(e.type);\n"
            + "  alert(e.bubbles);\n"
            + "  alert(e.cancelable);\n"
            + "  alert(e.view == window);\n"
            + "  alert(e.detail);\n"
            + "</script></body></html>\n";
        final List actual = new ArrayList();
        loadPage(BrowserVersion.FIREFOX_2, html, actual);
        final String[] expected = {"click", "true", "true", "true", "7"};
        assertEquals(expected, actual);
    }

}
