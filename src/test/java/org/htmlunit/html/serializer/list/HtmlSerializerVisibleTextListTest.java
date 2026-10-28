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
     * The ol[reversed] reverses the numbering but not the DOM order.
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
     * The ol[reversed] single item.
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
     * The ol[start] sets the starting number; visible text shows item content only.
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
     * The ol[start='1'] is just a normal list.
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
     * The dl with multiple dt for a single dd (synonyms).
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
     * The dl with multiple dt/dd pairs.
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
     * The dl with dt only (no dd) — orphaned term.
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
     * The menu as toolbar — visible text shows button labels.
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
     * The ol containing ul (recipe-style: ordered steps with unordered sub-items).
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
     * The ol containing ol (recipe with sub-steps).
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

    // ---------- unordered lists ---------------------------------------------------

    /**
     * A simple unordered list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void ulSimple() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a</li><li>b</li><li>c</li></ul>");
    }

    /**
     * A list with empty items.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void ulEmptyItems() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li></li><li>a</li><li></li></ul>");
    }

    /**
     * A list with items that contain only white space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void ulWhitespaceOnlyItems() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li> </li><li>a</li><li>  </li></ul>");
    }

    /**
     * A nested list after the text of the item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void ulNestedAfterText() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a<ul><li>b</li><li>c</li></ul></li><li>d</li></ul>");
    }

    /**
     * A nested list before the text of the item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b\na")
    public void ulNestedBeforeText() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li><ul><li>b</li></ul>a</li></ul>");
    }

    /**
     * Deeply nested lists.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1\n2\n3\n4")
    public void ulDeeplyNested() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>1<ul><li>2<ul><li>3<ul><li>4</li></ul></li></ul></li></ul></li></ul>");
    }

    /**
     * An unordered list containing an ordered list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void ulContainingOl() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a<ol><li>b</li><li>c</li></ol></li></ul>");
    }

    /**
     * Text directly inside of the list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void ulWithTextChild() throws Exception {
        getVisibleTextFormated("<ul id='tester'>a<li>b</li>c</ul>");
    }

    /**
     * A div directly inside of the list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void ulWithDivChild() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a</li><div>b</div><li>c</li></ul>");
    }

    /**
     * Comments and white space between the items.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void ulWithCommentAndWhitespace() throws Exception {
        getVisibleTextFormated("<ul id='tester'>\n  <!-- one -->\n  <li>a</li>\n  <!-- two -->\n  <li>b</li>\n</ul>");
    }

    /**
     * Script and template elements inside of the list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void ulWithScriptAndTemplate() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a</li><script>var x = 1;</script><template><li>t</li></template><li>b</li>"
                + "</ul>");
    }

    /**
     * A list with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\ny")
    public void ulDisplayNone() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<ul style='display: none'><li>a</li></ul>y</div>");
    }

    /**
     * A list item with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void ulItemDisplayNone() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a</li><li style='display: none'>b</li><li>c</li></ul>");
    }

    /**
     * A list item with visibility hidden.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void ulItemVisibilityHidden() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a</li><li style='visibility: hidden'>b</li><li>c</li></ul>");
    }

    /**
     * A list with display inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\na\nb\ny")
    public void ulDisplayInline() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<ul style='display: inline'><li>a</li><li>b</li></ul>y</div>");
    }

    /**
     * List items with display inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void ulItemsDisplayInline() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li style='display: inline'>a</li><li style='display: inline'>b</li></ul>");
    }

    /**
     * List items with display inline-block.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void ulItemsDisplayInlineBlock() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li style='display: inline-block'>a</li><li style='display: inline-block'>"
                + "b</li></ul>");
    }

    /**
     * A list with display flex.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void ulDisplayFlex() throws Exception {
        getVisibleTextFormated("<ul id='tester' style='display: flex'><li>a</li><li>b</li></ul>");
    }

    /**
     * A list without markers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void ulListStyleNone() throws Exception {
        getVisibleTextFormated("<ul id='tester' style='list-style: none'><li>a</li><li>b</li></ul>");
    }

    /**
     * A list between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\na\nb\ny")
    public void ulBetweenText() throws Exception {
        getVisibleTextFormated("<div id='tester'>x<ul><li>a</li><li>b</li></ul>y</div>");
    }

    /**
     * Two adjacent lists.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void ulTwoLists() throws Exception {
        getVisibleTextFormated("<div id='tester'><ul><li>a</li></ul><ul><li>b</li></ul></div>");
    }

    /**
     * A list inside of an inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\na\ny")
    public void ulInsideSpan() throws Exception {
        getVisibleTextFormated("<span id='tester'>x<ul><li>a</li></ul>y</span>");
    }

    /**
     * A list inside of a paragraph (the parser closes the paragraph).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\na\ny")
    public void ulInsideParagraph() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>x<ul><li>a</li></ul>y</p></div>");
    }

    /**
     * A list inside of a table cell.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void ulInsideTableCell() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td><ul><li>a</li><li>b</li></ul></td><td>c</td></tr></table>");
    }

    // ---------- list items --------------------------------------------------------

    /**
     * List items without a list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void liOutsideList() throws Exception {
        getVisibleTextFormated("<div id='tester'><li>a</li><li>b</li></div>");
    }

    /**
     * The value attribute of a list item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void liValueAttribute() throws Exception {
        getVisibleTextFormated("<ol id='tester'><li value='5'>a</li><li>b</li><li value='1'>c</li></ol>");
    }

    /**
     * A list item with a block child.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void liWithBlockChild() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li><div>a</div></li><li><div>b</div></li></ul>");
    }

    /**
     * A list item with paragraphs.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void liWithParagraphs() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li><p>a</p><p>b</p></li><li>c</li></ul>");
    }

    /**
     * A list item with line breaks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void liWithBr() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a<br>b</li><li>c<br></li></ul>");
    }

    /**
     * A list item with inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b c")
    public void liWithInlineElements() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a <b>b</b> <i>c</i></li></ul>");
    }

    /**
     * A list item with leading and trailing spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void liWithLeadingTrailingSpaces() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>   a   </li><li>  b  </li></ul>");
    }

    /**
     * A list item with a pre.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a  b\n  c")
    public void liWithPre() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li><pre>a  b\n  c</pre></li></ul>");
    }

    /**
     * The list item is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void liAsTester() throws Exception {
        getVisibleTextFormated("<ul><li id='tester'>a</li></ul>");
    }

    // ---------- ordered lists -----------------------------------------------------

    /**
     * The type attribute of an ordered list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void olTypeAttribute() throws Exception {
        getVisibleTextFormated("<ol id='tester' type='a'><li>a</li><li>b</li></ol>");
    }

    /**
     * An ordered list with roman numbers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void olTypeRoman() throws Exception {
        getVisibleTextFormated("<ol id='tester' type='I'><li>a</li><li>b</li></ol>");
    }

    /**
     * An ordered list that starts at zero.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void olStartZero() throws Exception {
        getVisibleTextFormated("<ol id='tester' start='0'><li>a</li><li>b</li></ol>");
    }

    /**
     * An ordered list that starts at a negative number.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void olStartNegative() throws Exception {
        getVisibleTextFormated("<ol id='tester' start='-5'><li>a</li><li>b</li></ol>");
    }

    /**
     * An ordered list with an invalid start.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void olStartInvalid() throws Exception {
        getVisibleTextFormated("<ol id='tester' start='x'><li>a</li><li>b</li></ol>");
    }

    /**
     * A reversed ordered list with a start.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void olReversedWithStart() throws Exception {
        getVisibleTextFormated("<ol id='tester' reversed start='10'><li>a</li><li>b</li></ol>");
    }

    /**
     * A reversed ordered list with a value on an item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void olReversedWithLiValue() throws Exception {
        getVisibleTextFormated("<ol id='tester' reversed><li>a</li><li value='7'>b</li><li>c</li></ol>");
    }

    /**
     * A nested reversed ordered list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void olNestedReversed() throws Exception {
        getVisibleTextFormated("<ol id='tester'><li>a<ol reversed><li>b</li><li>c</li></ol></li></ol>");
    }

    // ---------- dir, menu and definition lists ------------------------------------

    /**
     * The deprecated dir element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void dirElement() throws Exception {
        getVisibleTextFormated("<dir id='tester'><li>a</li><li>b</li></dir>");
    }

    /**
     * A menu inside of a list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void menuInsideUl() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a<menu><li>b</li></menu></li></ul>");
    }

    /**
     * A nested menu.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void menuNested() throws Exception {
        getVisibleTextFormated("<menu id='tester'><li>a</li><menu><li>b</li></menu></menu>");
    }

    /**
     * A menu with buttons.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void menuWithButtons() throws Exception {
        getVisibleTextFormated("<menu id='tester'><li><button>a</button></li><li><button>b</button></li></menu>");
    }

    /**
     * A definition list inside of a dd.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void dlNestedInDd() throws Exception {
        getVisibleTextFormated("<dl id='tester'><dt>a</dt><dd>b<dl><dt>c</dt><dd>d</dd></dl></dd></dl>");
    }

    /**
     * A definition list with only definitions.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void dlDdOnly() throws Exception {
        getVisibleTextFormated("<dl id='tester'><dd>a</dd><dd>b</dd></dl>");
    }

    /**
     * A definition with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void dlDdDisplayNone() throws Exception {
        getVisibleTextFormated("<dl id='tester'><dt>a</dt><dd style='display: none'>b</dd><dt>c</dt></dl>");
    }

    /**
     * A definition list inside of a list item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void dlInsideLi() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li><dl><dt>a</dt><dd>b</dd></dl></li></ul>");
    }

    /**
     * Definition terms and descriptions with display inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void dlDtDdInline() throws Exception {
        getVisibleTextFormated("<dl id='tester'><dt style='display: inline'>a</dt><dd style='display: inline'>b</dd></dl>");
    }

    /**
     * Block content inside of dt and dd.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void dlWithBlocks() throws Exception {
        getVisibleTextFormated("<dl id='tester'><dt><p>a</p></dt><dd><p>b</p><p>c</p></dd></dl>");
    }

    /**
     * Line breaks inside of dt and dd.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void dlWithBr() throws Exception {
        getVisibleTextFormated("<dl id='tester'><dt>a<br>b</dt><dd>c<br>d</dd></dl>");
    }
}
