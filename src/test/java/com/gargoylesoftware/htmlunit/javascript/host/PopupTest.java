/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
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
    @Browsers(Browser.IE)
    public void testPopup() throws Exception {
        final String html = "<html><head><title>First</title><body>\n"
            + "<script>\n"
            + "var oPopup = window.createPopup();\n"
            + "var oPopupBody = oPopup.document.body;\n"
            + "oPopupBody.innerHTML = 'bla bla';\n"
            + "oPopup.show(100, 100, 200, 50, document.body);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
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
        final HtmlPage page = loadPage(getBrowserVersion(), content, collectedAlerts);
        final HtmlElement button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();
        final String[] expectedAlerts = {"Pop-up window is Open"};
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals("about:blank", secondPage.getWebResponse().getWebRequest().getUrl());
        assertSame(secondPage.getEnclosingWindow(), secondPage.getWebClient().getCurrentWindow());
    }
}
