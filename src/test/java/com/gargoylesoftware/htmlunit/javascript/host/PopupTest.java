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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.ClickableElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for {@link Popup}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class PopupTest extends WebTestCase {

    /**
     * Just test that a standard use of popup works without exception.
     * @throws Exception if the test fails
     * TODO: it should fail when simulating FF as createPopup() is only for IE
     */
    @Test
    public void testPopup() throws Exception {
        final String content = "<html><head><title>First</title><body>\n"
            + "<script>\n"
            + "var oPopup = window.createPopup();\n"
            + "var oPopupBody = oPopup.document.body;\n"
            + "oPopupBody.innerHTML = 'bla bla';\n"
            + "oPopup.show(100, 100, 200, 50, document.body);\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(BrowserVersion.INTERNET_EXPLORER_6_0, content, collectedAlerts);
    }

    /**
     * Test that the opened window becomes the current one.
     * @throws Exception if the test fails
     */
    @Test
    public void testPopupWindowBecomesCurrent() throws Exception {
        final String content = "<html><head><title>First</title><body>\n"
            + "<span id='button' onClick='openPopup()'>Push me</span>\n"
            + "<SCRIPT>\n"
            + "function openPopup()  { \n "
            + "window.open('', '_blank', 'width=640, height=600, scrollbars=yes'); "
            + "alert('Pop-up window is Open');\n "
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final ClickableElement button = (ClickableElement) page.getHtmlElementById("button");

        final HtmlPage secondPage = (HtmlPage) button.click();
        final String[] expectedAlerts = {"Pop-up window is Open"};
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals("about:blank", secondPage.getWebResponse().getUrl());
        assertSame(secondPage.getEnclosingWindow(), secondPage.getWebClient().getCurrentWindow());
    }
}
