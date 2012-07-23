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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Tries;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

/**
 * Tests for 2.2 version of <a href="http://www.extjs.com/">Ext JS</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ExtJS22Test extends WebTestCase {

    private WebClient webClient_;

    /**
     * After.
     */
    @After
    public void after() {
        webClient_.closeAllWindows();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void core_templates() throws Exception {
        final HtmlPage page = getPage("core", "templates");
        final List<HtmlButton> buttons = (List<HtmlButton>) page.getByXPath("//button");
        final List<HtmlDivision> divs = (List<HtmlDivision>) page.getByXPath("//div[@class='x-panel-body']");
        assertEquals(2, buttons.size());
        assertEquals(2, divs.size());
        assertEquals("Apply the template to see results here", divs.get(0).asText());
        assertEquals("Apply the template to see results here", divs.get(1).asText());
        buttons.get(0).click();
        assertEquals("Name: Jack Slocum" + LINE_SEPARATOR + "Company: Ext JS, LLC" + LINE_SEPARATOR
            + "Location: Cleveland, Ohio", divs.get(0).asText());
        assertEquals("Apply the template to see results here", divs.get(1).asText());
        buttons.get(1).click();
        assertEquals("Name: Jack Slocum" + LINE_SEPARATOR + "Company: Ext JS, LLC" + LINE_SEPARATOR
            + "Location: Cleveland, Ohio", divs.get(0).asText());
        assertEquals("Name: Jack Slocum" + LINE_SEPARATOR + "Company: Ext JS, LLC" + LINE_SEPARATOR
            + "Location: Cleveland, Ohio" + LINE_SEPARATOR
            + "Kids:" + LINE_SEPARATOR + "1. Jack Slocum's kid - Sara Grace" + LINE_SEPARATOR
            + "2. Jack Slocum's kid - Zachary", divs.get(1).asText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @SuppressWarnings("unchecked")
    public void core_spotlight() throws Exception {
        final HtmlPage page = getPage("core", "spotlight");
        final List<HtmlButton> buttons = (List<HtmlButton>) page.getByXPath("//button");
        assertEquals(4, buttons.size());
        assertEquals("Start", buttons.get(0).asText());
        assertEquals("Next Panel", buttons.get(1).asText());
        assertEquals("Next Panel", buttons.get(2).asText());
        assertEquals("Done", buttons.get(3).asText());

        assertTrue(core_spotlight_isDisabled(buttons.get(1)));
        assertTrue(core_spotlight_isDisabled(buttons.get(2)));
        assertTrue(core_spotlight_isDisabled(buttons.get(3)));

        buttons.get(0).click();
        assertFalse(core_spotlight_isDisabled(buttons.get(1)));
        assertTrue(core_spotlight_isDisabled(buttons.get(2)));
        assertTrue(core_spotlight_isDisabled(buttons.get(3)));

        buttons.get(1).click();
        assertTrue(core_spotlight_isDisabled(buttons.get(1)));
        assertFalse(core_spotlight_isDisabled(buttons.get(2)));
        assertTrue(core_spotlight_isDisabled(buttons.get(3)));

        buttons.get(2).click();
        assertTrue(core_spotlight_isDisabled(buttons.get(1)));
        assertTrue(core_spotlight_isDisabled(buttons.get(2)));
        assertFalse(core_spotlight_isDisabled(buttons.get(3)));

        buttons.get(3).click();
        assertTrue(core_spotlight_isDisabled(buttons.get(1)));
        assertTrue(core_spotlight_isDisabled(buttons.get(2)));
        assertTrue(core_spotlight_isDisabled(buttons.get(3)));
    }

    private boolean core_spotlight_isDisabled(final HtmlButton button) {
        final HtmlTable table = (HtmlTable) button.getEnclosingElement("table");
        if (getBrowserVersion().isIE()) {
            return table.hasAttribute("disabled");
        }
        return table.getAttribute("class").contains("disabled");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void debug_console() throws Exception {
        final HtmlPage page = getPage("debug", "debug-console");
        assertEquals(2, page.getAnchors().size());
        page.getAnchors().get(1).click();
        assertEquals("Hello from the Ext console.",
            page.<DomNode>getFirstByXPath("//div[starts-with(text(), 'Hello')]").asText());
    }

    /**
     * Returns the Ext JS version being tested.
     * @return the Ext JS version being tested
     */
    protected String getVersion() {
        return "2.2";
    }

    /**
     * Loads the Ext JS test page using the specified example.
     *
     * @param example the example name
     * @param htmlName the page name
     * @return the loaded page
     * @throws Exception if an error occurs
     */
    protected HtmlPage getPage(final String example, final String htmlName) throws Exception {
        final String resource = "libraries/ExtJS/" + getVersion() + "/examples/" + example + "/" + htmlName + ".html";
        final URL url = getClass().getClassLoader().getResource(resource);
        assertNotNull(url);
        webClient_ = getWebClient();
        return webClient_.getPage(url);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void desktop_desktop() throws Exception {
        final HtmlPage page = getPage("desktop", "desktop");
        page.<HtmlButton>getFirstByXPath("//button").click();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void form_absform() throws Exception {
        final HtmlPage page = getPage("form", "absform");
        final String xml = page.asXml();
        assertTrue(xml.contains("Resize Me"));
        assertTrue(xml.contains("Send To:"));
        assertTrue(xml.contains("Subject:"));
        assertTrue(xml.contains("Cancel"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void form_anchoring() throws Exception {
        final HtmlPage page = getPage("form", "anchoring");
        final String xml = page.asXml();
        assertTrue(xml.contains("Send To:"));
        assertTrue(xml.contains("Subject:"));
        assertTrue(xml.contains("Send"));
        assertTrue(xml.contains("Cancel"));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Tries(3)
    public void grid_binding() throws Exception {
        final HtmlPage page = getPage("grid", "binding");
        page.getWebClient().waitForBackgroundJavaScriptStartingBefore(2000);
        final HtmlElement detailPanel = page.getHtmlElementById("detailPanel");
        final HtmlDivision resultsDiv = detailPanel.getFirstByXPath("div/div");
        assertEquals("Please select a book to see additional details.", resultsDiv.asText());

        final HtmlDivision firstRowDiv = page.getFirstByXPath("//div[@class='x-grid3-body']/div");

        firstRowDiv.click();
        assertEquals("Title: Master of the Game" + LINE_SEPARATOR
                + "Author: Sidney Sheldon" + LINE_SEPARATOR
                + "Manufacturer: Warner Books" + LINE_SEPARATOR
                + "Product Group: Book", resultsDiv.asText());
    }

}
