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

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.serializer.HtmlSerializerNormalizedText;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlSerializerNormalizedText} focusing on HTML list features:
 * ol[reversed], ol[start], dl/dt/dd structures, menu, nested lists.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerNormalizedTextListTest extends SimpleWebTestCase {

    // ── ol[reversed] ─────────────────────────────────────────────────────────

    /**
     * Normalized text of ol[reversed] — DOM order, no numbering.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. eggs (2)\n2. flour (2 cups)\n3. bananas (2)\n4. brown sugar")
    public void normalizedOlReversed() throws Exception {
        getNormalizedTextFormated("<ol id='tester' reversed>"
                + "<li>eggs (2)</li>"
                + "<li>flour (2 cups)</li>"
                + "<li>bananas (2)</li>"
                + "<li>brown sugar</li>"
                + "</ol>");
    }

    /**
     * Normalized text of empty ol[reversed].
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void normalizedOlReversedEmpty() throws Exception {
        getNormalizedTextFormated("<ol id='tester' reversed></ol>");
    }

    // ── ol[start] ────────────────────────────────────────────────────────────

    /**
     * Normalized text of ol[start] — start attribute is visual only.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("5. item a\n6. item b\n7. item c")
    public void normalizedOlStart() throws Exception {
        getNormalizedTextFormated("<ol id='tester' start='5'>"
                + "<li>item a</li>"
                + "<li>item b</li>"
                + "<li>item c</li>"
                + "</ol>");
    }

    /**
     * Normalized text of two ol[start] lists in a div.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. step one\n2. step two\n3. step three")
    public void normalizedOlStartContinuation() throws Exception {
        getNormalizedTextFormated("<div id='tester'>"
                + "<ol start='1'><li>step one</li><li>step two</li></ol>"
                + "<ol start='3'><li>step three</li></ol>"
                + "</div>");
    }

    // ── dl / dt / dd ─────────────────────────────────────────────────────────

    /**
     * Normalized text of a simple dl.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("throw\nVerb. To discard at a high velocity")
    public void normalizedDlSimple() throws Exception {
        getNormalizedTextFormated("<dl id='tester'>"
                + "<dt>throw</dt>"
                + "<dd>Verb. To discard at a high velocity</dd>"
                + "</dl>");
    }

    /**
     * Normalized text of dl with multiple dt per dd (synonyms).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("throw\nyeet\nVerb. To discard at a high velocity")
    public void normalizedDlMultipleDtOneDd() throws Exception {
        getNormalizedTextFormated("<dl id='tester'>"
                + "<dt>throw</dt>"
                + "<dt>yeet</dt>"
                + "<dd>Verb. To discard at a high velocity</dd>"
                + "</dl>");
    }

    /**
     * Normalized text of dl with multiple dt/dd pairs.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("throw\nyeet\nVerb. To discard at a high velocity\nbet\nExpresses agreement and affirmation.")
    public void normalizedDlMultiplePairs() throws Exception {
        getNormalizedTextFormated("<dl id='tester'>"
                + "<dt>throw</dt>"
                + "<dt>yeet</dt>"
                + "<dd>Verb. To discard at a high velocity</dd>"
                + "<dt>bet</dt>"
                + "<dd>Expresses agreement and affirmation.</dd>"
                + "</dl>");
    }

    /**
     * Normalized text of HTML5 dl with div wrappers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Chrome\nOpera\nBlink-based browsers\nFirefox\nTor\nGecko-based browsers")
    public void normalizedDlWithDivWrappers() throws Exception {
        getNormalizedTextFormated("<dl id='tester'>"
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
     * Normalized text of empty dl.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void normalizedDlEmpty() throws Exception {
        getNormalizedTextFormated("<dl id='tester'></dl>");
    }

    /**
     * Normalized text of dl with dt only (no dd).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("orphan term")
    public void normalizedDlDtOnly() throws Exception {
        getNormalizedTextFormated("<dl id='tester'>"
                + "<dt>orphan term</dt>"
                + "</dl>");
    }

    /**
     * Normalized text of dl with dd only (no dt).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("orphan definition")
    public void normalizedDlDdOnly() throws Exception {
        getNormalizedTextFormated("<dl id='tester'>"
                + "<dd>orphan definition</dd>"
                + "</dl>");
    }

    // ── menu ─────────────────────────────────────────────────────────────────

    /**
     * Normalized text of menu toolbar.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("StrongEmphasizeStrike")
    public void normalizedMenuToolbar() throws Exception {
        getNormalizedTextFormated("<menu id='tester'>"
                + "<li><button>Strong</button></li>"
                + "<li><button>Emphasize</button></li>"
                + "<li><button>Strike</button></li>"
                + "</menu>");
    }

    /**
     * Normalized text of empty menu.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void normalizedMenuEmpty() throws Exception {
        getNormalizedTextFormated("<menu id='tester'></menu>");
    }

    /**
     * Normalized text of menu with text-only li (no button).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("PlayMuteFullscreen")
    public void normalizedMenuTextOnly() throws Exception {
        getNormalizedTextFormated("<menu id='tester'>"
                + "<li>Play</li>"
                + "<li>Mute</li>"
                + "<li>Fullscreen</li>"
                + "</menu>");
    }

    // ── Nested lists ──────────────────────────────────────────────────────────

    /**
     * Normalized text of ol containing ul.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. Prepare:\nPreheat oven\nGrease pan\n2. Bake:\nBake for 60 minutes\nOr until done")
    public void normalizedOlContainingUl() throws Exception {
        getNormalizedTextFormated("<ol id='tester'>"
                + "<li>Prepare:\n"
                + "  <ul><li>Preheat oven</li><li>Grease pan</li></ul>"
                + "</li>"
                + "<li>Bake:\n"
                + "  <ul><li>Bake for 60 minutes</li><li>Or until done</li></ul>"
                + "</li>"
                + "</ol>");
    }

    /**
     * Normalized text of ol containing ol (deeply nested).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1. Mix:\n1. Beat butter\n2. Stir in eggs\n2. Combine")
    public void normalizedOlContainingOl() throws Exception {
        getNormalizedTextFormated("<ol id='tester'>"
                + "<li>Mix:"
                + "  <ol>"
                + "    <li>Beat butter</li>"
                + "    <li>Stir in eggs</li>"
                + "  </ol>"
                + "</li>"
                + "<li>Combine</li>"
                + "</ol>");
    }

    /**
     * Normalized text of ul containing dl (mixed list types).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Header\nterm\ndefinition")
    public void normalizedUlContainingDl() throws Exception {
        getNormalizedTextFormated("<ul id='tester'>"
                + "<li>Header</li>"
                + "<li><dl><dt>term</dt><dd>definition</dd></dl></li>"
                + "</ul>");
    }

    // ── helper ────────────────────────────────────────────────────────────────

    private void getNormalizedTextFormated(final String htmlTesterSnipped) throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "  " + htmlTesterSnipped + "\n"
            + "</body></html>";

        final HtmlPage page = loadPage(htmlContent);
        final String text = page.getElementById("tester").asNormalizedText();
        assertEquals(getExpectedAlerts()[0], text);
    }
}