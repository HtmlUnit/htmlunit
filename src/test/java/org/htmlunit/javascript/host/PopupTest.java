/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Popup;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Popup}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public class PopupTest extends SimpleWebTestCase {

    /**
     * Test that the opened window becomes the current one.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Pop-up window is Open")
    public void popupWindowBecomesCurrent() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html><head><title>First</title><body>\n"
            + "<span id='button' onClick='openPopup()'>Push me</span>\n"
            + "<script>\n"
            + "  function openPopup() {\n "
            + "    window.open('', '_blank', 'width=640, height=600, scrollbars=yes');\n"
            + "    alert('Pop-up window is Open');\n "
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<>();
        final HtmlPage page = loadPage(content, collectedAlerts);
        final HtmlElement button = page.getHtmlElementById("button");

        final HtmlPage secondPage = button.click();
        final String[] expectedAlerts = {"Pop-up window is Open"};
        assertEquals(expectedAlerts, collectedAlerts);
        assertEquals("about:blank", secondPage.getUrl());
        assertSame(secondPage.getEnclosingWindow(), secondPage.getWebClient().getCurrentWindow());
    }
}
