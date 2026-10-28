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
     * The innerText of ol[reversed] — content in DOM order.
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
     * The outerText of ol[reversed].
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
     * The innerText of ol[start] — numbering is visual, content is what matters.
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
     * The innerText of two ol[start] lists inside a div, continuing numbering.
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
     * The innerText of a simple dl.
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
     * The innerText of dl with multiple dt per dd.
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
     * The innerText of HTML5 dl with div wrappers.
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
     * The outerText of dl with div wrappers — should match innerText for block elements.
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
     * The innerText of empty dl.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void innerTextDlEmpty() throws Exception {
        getInnerTextFormated("<dl id='tester'></dl>");
    }

    // ── menu ─────────────────────────────────────────────────────────────────

    /**
     * The innerText of menu toolbar — button labels are visible.
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
     * The outerText of menu toolbar.
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
     * The innerText of empty menu.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void innerTextMenuEmpty() throws Exception {
        getInnerTextFormated("<menu id='tester'></menu>");
    }

    // ── Nested lists ──────────────────────────────────────────────────────────

    /**
     * The innerText of ol containing ul.
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
     * The innerText of ol containing ol.
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

    // ---------- parity with the visible text tests --------------------------------

    /**
     * The ol[reversed] single item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("only item")
    public void innerTextOlReversedSingleItem() throws Exception {
        getInnerTextFormated("<ol id='tester' reversed><li>only item</li></ol>");
    }

    /**
     * Empty ol[reversed].
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void innerTextOlReversedEmpty() throws Exception {
        getInnerTextFormated("<ol id='tester' reversed></ol>");
    }

    /**
     * The ol[start] with the default value.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextOlStartOne() throws Exception {
        getInnerTextFormated("<ol id='tester' start='1'><li>a</li><li>b</li></ol>");
    }

    /**
     * A definition list with multiple pairs.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void innerTextDlMultiplePairs() throws Exception {
        getInnerTextFormated("<dl id='tester'><dt>a</dt><dd>b</dd><dt>c</dt><dd>d</dd></dl>");
    }

    /**
     * A definition list with terms only.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextDlDtOnly() throws Exception {
        getInnerTextFormated("<dl id='tester'><dt>a</dt><dt>b</dt></dl>");
    }

    // ---------- unordered lists ---------------------------------------------------

    /**
     * A simple unordered list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void innerTextUlSimple() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a</li><li>b</li><li>c</li></ul>");
    }

    /**
     * A list with empty items.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void innerTextUlEmptyItems() throws Exception {
        getInnerTextFormated("<ul id='tester'><li></li><li>a</li><li></li></ul>");
    }

    /**
     * A list with items that contain only white space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void innerTextUlWhitespaceOnlyItems() throws Exception {
        getInnerTextFormated("<ul id='tester'><li> </li><li>a</li><li>  </li></ul>");
    }

    /**
     * A nested list after the text of the item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void innerTextUlNestedAfterText() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a<ul><li>b</li><li>c</li></ul></li><li>d</li></ul>");
    }

    /**
     * A nested list before the text of the item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("b\na")
    public void innerTextUlNestedBeforeText() throws Exception {
        getInnerTextFormated("<ul id='tester'><li><ul><li>b</li></ul>a</li></ul>");
    }

    /**
     * Deeply nested lists.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1\n2\n3\n4")
    public void innerTextUlDeeplyNested() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>1<ul><li>2<ul><li>3<ul><li>4</li></ul></li></ul></li></ul></li></ul>");
    }

    /**
     * An unordered list containing an ordered list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void innerTextUlContainingOl() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a<ol><li>b</li><li>c</li></ol></li></ul>");
    }

    /**
     * Text directly inside of the list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void innerTextUlWithTextChild() throws Exception {
        getInnerTextFormated("<ul id='tester'>a<li>b</li>c</ul>");
    }

    /**
     * A div directly inside of the list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void innerTextUlWithDivChild() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a</li><div>b</div><li>c</li></ul>");
    }

    /**
     * Comments and white space between the items.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextUlWithCommentAndWhitespace() throws Exception {
        getInnerTextFormated("<ul id='tester'>\n  <!-- one -->\n  <li>a</li>\n  <!-- two -->\n  <li>b</li>\n</ul>");
    }

    /**
     * Script and template elements inside of the list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextUlWithScriptAndTemplate() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a</li><script>var x = 1;</script><template><li>t</li></template><li>b</li>"
                + "</ul>");
    }

    /**
     * A list with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xy")
    public void innerTextUlDisplayNone() throws Exception {
        getInnerTextFormated("<div id='tester'>x<ul style='display: none'><li>a</li></ul>y</div>");
    }

    /**
     * A list item with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void innerTextUlItemDisplayNone() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a</li><li style='display: none'>b</li><li>c</li></ul>");
    }

    /**
     * A list item with visibility hidden.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void innerTextUlItemVisibilityHidden() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a</li><li style='visibility: hidden'>b</li><li>c</li></ul>");
    }

    /**
     * A list with display inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\na\nb\ny")
    public void innerTextUlDisplayInline() throws Exception {
        getInnerTextFormated("<div id='tester'>x<ul style='display: inline'><li>a</li><li>b</li></ul>y</div>");
    }

    /**
     * List items with display inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void innerTextUlItemsDisplayInline() throws Exception {
        getInnerTextFormated("<ul id='tester'><li style='display: inline'>a</li><li style='display: inline'>b</li></ul>");
    }

    /**
     * List items with display inline-block.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void innerTextUlItemsDisplayInlineBlock() throws Exception {
        getInnerTextFormated("<ul id='tester'><li style='display: inline-block'>a</li><li style='display: inline-block'>"
                + "b</li></ul>");
    }

    /**
     * A list with display flex.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextUlDisplayFlex() throws Exception {
        getInnerTextFormated("<ul id='tester' style='display: flex'><li>a</li><li>b</li></ul>");
    }

    /**
     * A list without markers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextUlListStyleNone() throws Exception {
        getInnerTextFormated("<ul id='tester' style='list-style: none'><li>a</li><li>b</li></ul>");
    }

    /**
     * A list between text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\na\nb\ny")
    public void innerTextUlBetweenText() throws Exception {
        getInnerTextFormated("<div id='tester'>x<ul><li>a</li><li>b</li></ul>y</div>");
    }

    /**
     * Two adjacent lists.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextUlTwoLists() throws Exception {
        getInnerTextFormated("<div id='tester'><ul><li>a</li></ul><ul><li>b</li></ul></div>");
    }

    /**
     * A list inside of an inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\na\ny")
    public void innerTextUlInsideSpan() throws Exception {
        getInnerTextFormated("<span id='tester'>x<ul><li>a</li></ul>y</span>");
    }

    /**
     * A list inside of a paragraph (the parser closes the paragraph).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x\n\na\ny")
    public void innerTextUlInsideParagraph() throws Exception {
        getInnerTextFormated("<div id='tester'><p>x<ul><li>a</li></ul>y</p></div>");
    }

    /**
     * A list inside of a table cell.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\n\tc")
    public void innerTextUlInsideTableCell() throws Exception {
        getInnerTextFormated("<table id='tester'><tr><td><ul><li>a</li><li>b</li></ul></td><td>c</td></tr></table>");
    }

    // ---------- list items --------------------------------------------------------

    /**
     * List items without a list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextLiOutsideList() throws Exception {
        getInnerTextFormated("<div id='tester'><li>a</li><li>b</li></div>");
    }

    /**
     * The value attribute of a list item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void innerTextLiValueAttribute() throws Exception {
        getInnerTextFormated("<ol id='tester'><li value='5'>a</li><li>b</li><li value='1'>c</li></ol>");
    }

    /**
     * A list item with a block child.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextLiWithBlockChild() throws Exception {
        getInnerTextFormated("<ul id='tester'><li><div>a</div></li><li><div>b</div></li></ul>");
    }

    /**
     * A list item with paragraphs.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\n\nb\n\nc")
    public void innerTextLiWithParagraphs() throws Exception {
        getInnerTextFormated("<ul id='tester'><li><p>a</p><p>b</p></li><li>c</li></ul>");
    }

    /**
     * A list item with line breaks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\n")
    public void innerTextLiWithBr() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a<br>b</li><li>c<br></li></ul>");
    }

    /**
     * A list item with inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b c")
    public void innerTextLiWithInlineElements() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a <b>b</b> <i>c</i></li></ul>");
    }

    /**
     * A list item with leading and trailing spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextLiWithLeadingTrailingSpaces() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>   a   </li><li>  b  </li></ul>");
    }

    /**
     * A list item with a pre.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a  b\n  c")
    public void innerTextLiWithPre() throws Exception {
        getInnerTextFormated("<ul id='tester'><li><pre>a  b\n  c</pre></li></ul>");
    }

    /**
     * The list item is the element under test.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void innerTextLiAsTester() throws Exception {
        getInnerTextFormated("<ul><li id='tester'>a</li></ul>");
    }

    // ---------- ordered lists -----------------------------------------------------

    /**
     * The type attribute of an ordered list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextOlTypeAttribute() throws Exception {
        getInnerTextFormated("<ol id='tester' type='a'><li>a</li><li>b</li></ol>");
    }

    /**
     * An ordered list with roman numbers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextOlTypeRoman() throws Exception {
        getInnerTextFormated("<ol id='tester' type='I'><li>a</li><li>b</li></ol>");
    }

    /**
     * An ordered list that starts at zero.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextOlStartZero() throws Exception {
        getInnerTextFormated("<ol id='tester' start='0'><li>a</li><li>b</li></ol>");
    }

    /**
     * An ordered list that starts at a negative number.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextOlStartNegative() throws Exception {
        getInnerTextFormated("<ol id='tester' start='-5'><li>a</li><li>b</li></ol>");
    }

    /**
     * An ordered list with an invalid start.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextOlStartInvalid() throws Exception {
        getInnerTextFormated("<ol id='tester' start='x'><li>a</li><li>b</li></ol>");
    }

    /**
     * A reversed ordered list with a start.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextOlReversedWithStart() throws Exception {
        getInnerTextFormated("<ol id='tester' reversed start='10'><li>a</li><li>b</li></ol>");
    }

    /**
     * A reversed ordered list with a value on an item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void innerTextOlReversedWithLiValue() throws Exception {
        getInnerTextFormated("<ol id='tester' reversed><li>a</li><li value='7'>b</li><li>c</li></ol>");
    }

    /**
     * A nested reversed ordered list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void innerTextOlNestedReversed() throws Exception {
        getInnerTextFormated("<ol id='tester'><li>a<ol reversed><li>b</li><li>c</li></ol></li></ol>");
    }

    // ---------- dir, menu and definition lists ------------------------------------

    /**
     * The deprecated dir element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextDirElement() throws Exception {
        getInnerTextFormated("<dir id='tester'><li>a</li><li>b</li></dir>");
    }

    /**
     * A menu inside of a list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextMenuInsideUl() throws Exception {
        getInnerTextFormated("<ul id='tester'><li>a<menu><li>b</li></menu></li></ul>");
    }

    /**
     * A nested menu.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextMenuNested() throws Exception {
        getInnerTextFormated("<menu id='tester'><li>a</li><menu><li>b</li></menu></menu>");
    }

    /**
     * A menu with buttons.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextMenuWithButtons() throws Exception {
        getInnerTextFormated("<menu id='tester'><li><button>a</button></li><li><button>b</button></li></menu>");
    }

    /**
     * A definition list inside of a dd.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void innerTextDlNestedInDd() throws Exception {
        getInnerTextFormated("<dl id='tester'><dt>a</dt><dd>b<dl><dt>c</dt><dd>d</dd></dl></dd></dl>");
    }

    /**
     * A definition list with only definitions.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextDlDdOnly() throws Exception {
        getInnerTextFormated("<dl id='tester'><dd>a</dd><dd>b</dd></dl>");
    }

    /**
     * A definition with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void innerTextDlDdDisplayNone() throws Exception {
        getInnerTextFormated("<dl id='tester'><dt>a</dt><dd style='display: none'>b</dd><dt>c</dt></dl>");
    }

    /**
     * A definition list inside of a list item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void innerTextDlInsideLi() throws Exception {
        getInnerTextFormated("<ul id='tester'><li><dl><dt>a</dt><dd>b</dd></dl></li></ul>");
    }

    /**
     * Definition terms and descriptions with display inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void innerTextDlDtDdInline() throws Exception {
        getInnerTextFormated("<dl id='tester'><dt style='display: inline'>a</dt><dd style='display: inline'>b</dd></dl>");
    }

    /**
     * Block content inside of dt and dd.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\n\nb\n\nc")
    public void innerTextDlWithBlocks() throws Exception {
        getInnerTextFormated("<dl id='tester'><dt><p>a</p></dt><dd><p>b</p><p>c</p></dd></dl>");
    }

    /**
     * Line breaks inside of dt and dd.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void innerTextDlWithBr() throws Exception {
        getInnerTextFormated("<dl id='tester'><dt>a<br>b</dt><dd>c<br>d</dd></dl>");
    }

    // ---------- outerText ---------------------------------------------------------

    /**
     * A nested list after the text of the item. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void outerTextUlNestedAfterText() throws Exception {
        getOuterTextFormated("<ul id='tester'><li>a<ul><li>b</li><li>c</li></ul></li><li>d</li></ul>");
    }

    /**
     * A list item with display none. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void outerTextUlItemDisplayNone() throws Exception {
        getOuterTextFormated("<ul id='tester'><li>a</li><li style='display: none'>b</li><li>c</li></ul>");
    }

    /**
     * A definition list inside of a dd. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc\nd")
    public void outerTextDlNestedInDd() throws Exception {
        getOuterTextFormated("<dl id='tester'><dt>a</dt><dd>b<dl><dt>c</dt><dd>d</dd></dl></dd></dl>");
    }

    /**
     * The type attribute of an ordered list. (outerText)
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void outerTextOlTypeAttribute() throws Exception {
        getOuterTextFormated("<ol id='tester' type='a'><li>a</li><li>b</li></ol>");
    }
}
