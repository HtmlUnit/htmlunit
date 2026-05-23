/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.html.serializer.list;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.serializer.HtmlSerializerVisibleText;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlSerializerVisibleText} focusing on HTML list features:
 * ol[reversed], ol[start], dl/dt/dd structures, menu, nested lists.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerVisibleTextListTest extends WebDriverTestCase {

    // ── ol[reversed] ─────────────────────────────────────────────────────────

    /**
     * ol[reversed] reverses the numbering but not the DOM order.
     * Visible text should reflect the items in DOM order.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("eggs (2)\nflour (2 cups)\nbananas (2)\nbrown sugar")
    public void olReversed() throws Exception {
        getVisibleTextFormated("<ol id='tester' reversed>"
                + "<li>eggs (2)</li>"
                + "<li>flour (2 cups)</li>"
                + "<li>bananas (2)</li>"
                + "<li>brown sugar</li>"
                + "</ol>");
    }

    /**
     * ol[reversed] single item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("only item")
    public void olReversedSingleItem() throws Exception {
        getVisibleTextFormated("<ol id='tester' reversed>"
                + "<li>only item</li>"
                + "</ol>");
    }

    /**
     * Empty ol[reversed].
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void olReversedEmpty() throws Exception {
        getVisibleTextFormated("<ol id='tester' reversed></ol>");
    }

    // ── ol[start] ────────────────────────────────────────────────────────────

    /**
     * ol[start] sets the starting number; visible text shows item content only.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("item a\nitem b\nitem c")
    public void olStart() throws Exception {
        getVisibleTextFormated("<ol id='tester' start='5'>"
                + "<li>item a</li>"
                + "<li>item b</li>"
                + "<li>item c</li>"
                + "</ol>");
    }

    /**
     * ol[start='1'] is just a normal list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("item a\nitem b")
    public void olStartOne() throws Exception {
        getVisibleTextFormated("<ol id='tester' start='1'>"
                + "<li>item a</li>"
                + "<li>item b</li>"
                + "</ol>");
    }

    /**
     * Continuation across two separate ol[start] lists inside a wrapper div.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("step one\nstep two\nstep three")
    public void olStartContinuation() throws Exception {
        getVisibleTextFormated("<div id='tester'>"
                + "<ol start='1'><li>step one</li><li>step two</li></ol>"
                + "<ol start='3'><li>step three</li></ol>"
                + "</div>");
    }

    // ── dl / dt / dd ─────────────────────────────────────────────────────────

    /**
     * Simple dl with one dt and one dd.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("throw\nVerb. To discard at a high velocity")
    public void dlSimple() throws Exception {
        getVisibleTextFormated("<dl id='tester'>"
                + "<dt>throw</dt>"
                + "<dd>Verb. To discard at a high velocity</dd>"
                + "</dl>");
    }

    /**
     * dl with multiple dt for a single dd (synonyms).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("throw\nyeet\nVerb. To discard at a high velocity")
    public void dlMultipleDtOneDd() throws Exception {
        getVisibleTextFormated("<dl id='tester'>"
                + "<dt>throw</dt>"
                + "<dt>yeet</dt>"
                + "<dd>Verb. To discard at a high velocity</dd>"
                + "</dl>");
    }

    /**
     * dl with multiple dt/dd pairs.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("throw\nyeet\nVerb. To discard at a high velocity\nbet\nExpresses agreement and affirmation.")
    public void dlMultiplePairs() throws Exception {
        getVisibleTextFormated("<dl id='tester'>"
                + "<dt>throw</dt>"
                + "<dt>yeet</dt>"
                + "<dd>Verb. To discard at a high velocity</dd>"
                + "<dt>bet</dt>"
                + "<dd>Expresses agreement and affirmation.</dd>"
                + "</dl>");
    }

    /**
     * HTML5 dl with div wrappers grouping dt and dd together.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Chrome\nOpera\nBlink-based browsers\nFirefox\nTor\nGecko-based browsers")
    public void dlWithDivWrappers() throws Exception {
        getVisibleTextFormated("<dl id='tester'>"
                + "<div>"
                + "<dt>Chrome</dt>"
                + "<dt>Opera</dt>"
                + "<dd>Blink-based browsers</dd>"
                + "</div>"
                + "<div>"
                + "<dt>Firefox</dt>"
                + "<dt>Tor</dt>"
                + "<dd>Gecko-based browsers</dd>"
                + "</div>"
                + "</dl>");
    }

    /**
     * Empty dl.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void dlEmpty() throws Exception {
        getVisibleTextFormated("<dl id='tester'></dl>");
    }

    /**
     * dl with dt only (no dd) — orphaned term.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("orphan term")
    public void dlDtOnly() throws Exception {
        getVisibleTextFormated("<dl id='tester'>"
                + "<dt>orphan term</dt>"
                + "</dl>");
    }

    // ── menu ─────────────────────────────────────────────────────────────────

    /**
     * menu as toolbar — visible text shows button labels.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Strong\nEmphasize\nStrike")
    public void menuToolbar() throws Exception {
        getVisibleTextFormated("<menu id='tester'>"
                + "<li><button>Strong</button></li>"
                + "<li><button>Emphasize</button></li>"
                + "<li><button>Strike</button></li>"
                + "</menu>");
    }

    /**
     * Empty menu.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void menuEmpty() throws Exception {
        getVisibleTextFormated("<menu id='tester'></menu>");
    }

    // ── Nested ordered/unordered lists ────────────────────────────────────────

    /**
     * ol containing ul (recipe-style: ordered steps with unordered sub-items).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Prepare:\nPreheat oven\nGrease pan\nBake:\nBake for 60 minutes\nOr until done")
    public void olContainingUl() throws Exception {
        getVisibleTextFormated("<ol id='tester'>"
                + "<li>Prepare:\n"
                + "  <ul><li>Preheat oven</li><li>Grease pan</li></ul>"
                + "</li>"
                + "<li>Bake:\n"
                + "  <ul><li>Bake for 60 minutes</li><li>Or until done</li></ul>"
                + "</li>"
                + "</ol>");
    }

    /**
     * ol containing ol (recipe with sub-steps).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Mix:\nBeat butter\nStir in eggs")
    public void olContainingOl() throws Exception {
        getVisibleTextFormated("<ol id='tester'>"
                + "<li>Mix:"
                + "  <ol><li>Beat butter</li><li>Stir in eggs</li></ol>"
                + "</li>"
                + "</ol>");
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private void getVisibleTextFormated(final String htmlTesterSnipped) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getElementById("tester").getVisibleText());
        }
    }
}
