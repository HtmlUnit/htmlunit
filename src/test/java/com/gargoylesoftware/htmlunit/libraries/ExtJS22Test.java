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
package com.gargoylesoftware.htmlunit.libraries;

import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
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

    /** System-specific line separator. */
    private static final String LS = System.getProperty("line.separator");

    private WebClient webClient_;

    /**
     * After.
     */
    @After
    public void After() {
        webClient_.closeAllWindows();
    }

    /**
     * @throws Exception if an error occurs
     */
//    @Test
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
        assertEquals("Name: Jack Slocum" + LS + "Company: Ext JS, LLC" + LS
            + "Location: Cleveland, Ohio", divs.get(0).asText());
        assertEquals("Apply the template to see results here", divs.get(1).asText());
        buttons.get(1).click();
        assertEquals("Name: Jack Slocum" + LS + "Company: Ext JS, LLC" + LS
            + "Location: Cleveland, Ohio", divs.get(0).asText());
        assertEquals("Name: Jack Slocum" + LS + "Company: Ext JS, LLC" + LS + "Location: Cleveland, Ohio" + LS
            + "Kids:" + LS + "1. Jack Slocum's kid - Sara Grace" + LS
            + "2. Jack Slocum's kid - Zachary", divs.get(1).asText());
    }

    /**
     * @throws Exception if an error occurs
     */
//    @Test
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
        final HtmlTable table = (HtmlTable) button.getParentNode().getParentNode().getParentNode().getParentNode();
        if (getBrowserVersion().isIE()) {
            return table.hasAttribute("disabled");
        }
        return table.getAttribute("class").contains("disabled");
    }

    /**
     * @throws Exception if an error occurs
     */
//    @Test
    public void debug_console() throws Exception {
        final HtmlPage page = getPage("debug", "debug-console");
        assertEquals(2, page.getAnchors().size());
        page.getAnchors().get(1).click();
        assertEquals("Hello from the Ext console.",
            page.<DomNode>getFirstByXPath("//div[starts-with(text(), 'Hello')]").asText());
    }

    /**
     * Returns the Ext JS directory being tested.
     * @return the Ext JS directory being tested
     */
    protected String getDirectory() {
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
        final String resource = "ExtJS/" + getDirectory() + "/examples/" + example + "/" + htmlName + ".html";
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

}
