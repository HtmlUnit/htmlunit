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
import org.htmlunit.html.serializer.HtmlSerializerVisibleText;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link HtmlSerializerVisibleText} focusing on innerText/outerText
 * for HTML list features: ol[reversed], ol[start], dl/dt/dd, menu, nested lists.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerInnerOuterTextListTest extends WebDriverTestCase {

    // ── ol[reversed] ─────────────────────────────────────────────────────────

    /**
     * innerText of ol[reversed] — content in DOM order.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("eggs (2)\nflour (2 cups)\nbananas (2)\nbrown sugar")
    public void innerTextOlReversed() throws Exception {
        getInnerTextFormated("<ol id='tester' reversed>"
                + "<li>eggs (2)</li>"
                + "<li>flour (2 cups)</li>"
                + "<li>bananas (2)</li>"
                + "<li>brown sugar</li>"
                + "</ol>");
    }

    /**
     * outerText of ol[reversed].
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("eggs (2)\nflour (2 cups)\nbananas (2)\nbrown sugar")
    public void outerTextOlReversed() throws Exception {
        getOuterTextFormated("<ol id='tester' reversed>"
                + "<li>eggs (2)</li>"
                + "<li>flour (2 cups)</li>"
                + "<li>bananas (2)</li>"
                + "<li>brown sugar</li>"
                + "</ol>");
    }

    // ── ol[start] ────────────────────────────────────────────────────────────

    /**
     * innerText of ol[start] — numbering is visual, content is what matters.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("item a\nitem b\nitem c")
    public void innerTextOlStart() throws Exception {
        getInnerTextFormated("<ol id='tester' start='5'>"
                + "<li>item a</li>"
                + "<li>item b</li>"
                + "<li>item c</li>"
                + "</ol>");
    }

    /**
     * innerText of two ol[start] lists inside a div, continuing numbering.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("step one\nstep two\nstep three")
    public void innerTextOlStartContinuation() throws Exception {
        getInnerTextFormated("<div id='tester'>"
                + "<ol start='1'><li>step one</li><li>step two</li></ol>"
                + "<ol start='3'><li>step three</li></ol>"
                + "</div>");
    }

    // ── dl / dt / dd ─────────────────────────────────────────────────────────

    /**
     * innerText of a simple dl.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("throw\nVerb. To discard at a high velocity")
    public void innerTextDlSimple() throws Exception {
        getInnerTextFormated("<dl id='tester'>"
                + "<dt>throw</dt>"
                + "<dd>Verb. To discard at a high velocity</dd>"
                + "</dl>");
    }

    /**
     * innerText of dl with multiple dt per dd.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("throw\nyeet\nVerb. To discard at a high velocity")
    public void innerTextDlMultipleDt() throws Exception {
        getInnerTextFormated("<dl id='tester'>"
                + "<dt>throw</dt>"
                + "<dt>yeet</dt>"
                + "<dd>Verb. To discard at a high velocity</dd>"
                + "</dl>");
    }

    /**
     * innerText of HTML5 dl with div wrappers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Chrome\nOpera\nBlink-based browsers\nFirefox\nTor\nGecko-based browsers")
    public void innerTextDlWithDivWrappers() throws Exception {
        getInnerTextFormated("<dl id='tester'>"
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
     * outerText of dl with div wrappers — should match innerText for block elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Chrome\nOpera\nBlink-based browsers\nFirefox\nTor\nGecko-based browsers")
    public void outerTextDlWithDivWrappers() throws Exception {
        getOuterTextFormated("<dl id='tester'>"
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
     * innerText of empty dl.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void innerTextDlEmpty() throws Exception {
        getInnerTextFormated("<dl id='tester'></dl>");
    }

    // ── menu ─────────────────────────────────────────────────────────────────

    /**
     * innerText of menu toolbar — button labels are visible.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Strong\nEmphasize\nStrike")
    public void innerTextMenuToolbar() throws Exception {
        getInnerTextFormated("<menu id='tester'>"
                + "<li><button>Strong</button></li>"
                + "<li><button>Emphasize</button></li>"
                + "<li><button>Strike</button></li>"
                + "</menu>");
    }

    /**
     * outerText of menu toolbar.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Strong\nEmphasize\nStrike")
    public void outerTextMenuToolbar() throws Exception {
        getOuterTextFormated("<menu id='tester'>"
                + "<li><button>Strong</button></li>"
                + "<li><button>Emphasize</button></li>"
                + "<li><button>Strike</button></li>"
                + "</menu>");
    }

    /**
     * innerText of empty menu.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void innerTextMenuEmpty() throws Exception {
        getInnerTextFormated("<menu id='tester'></menu>");
    }

    // ── Nested lists ──────────────────────────────────────────────────────────

    /**
     * innerText of ol containing ul.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Prepare:\nPreheat oven\nGrease pan\nBake:\nBake for 60 minutes\nOr until done")
    public void innerTextOlContainingUl() throws Exception {
        getInnerTextFormated("<ol id='tester'>"
                + "<li>Prepare:\n"
                + "  <ul><li>Preheat oven</li><li>Grease pan</li></ul>"
                + "</li>"
                + "<li>Bake:\n"
                + "  <ul><li>Bake for 60 minutes</li><li>Or until done</li></ul>"
                + "</li>"
                + "</ol>");
    }

    /**
     * innerText of ol containing ol.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Mix:\nBeat butter\nStir in eggs")
    public void innerTextOlContainingOl() throws Exception {
        getInnerTextFormated("<ol id='tester'>"
                + "<li>Mix:"
                + "  <ol><li>Beat butter</li><li>Stir in eggs</li></ol>"
                + "</li>"
                + "</ol>");
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private void getInnerTextFormated(final String htmlTesterSnipped) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').innerText");
        assertEquals(getExpectedAlerts()[0], text);
    }

    private void getOuterTextFormated(final String htmlTesterSnipped) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = (String) ((JavascriptExecutor) driver)
                .executeScript("return document.getElementById('tester').outerText");
        assertEquals(getExpectedAlerts()[0], text);
    }
}
