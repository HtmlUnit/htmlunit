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
package org.htmlunit.html.serializer.paragraph;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.html.serializer.HtmlSerializerVisibleText;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlSerializerVisibleText} focusing on paragraph
 * text formatting tags: b, i, u, s, strong, em, mark, small, del, ins,
 * sub, sup, abbr, cite, q, code, kbd, samp, var, time, dfn, bdi, bdo,
 * ruby/rt/rp, wbr, span.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerVisibleTextParagraphTest extends WebDriverTestCase {

    // ════════════════════════════════════════════════════════════════════════
    // Single tag tests
    // ════════════════════════════════════════════════════════════════════════

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bold world")
    public void b() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <b>bold</b> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello italic world")
    public void i() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <i>italic</i> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello underline world")
    public void u() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <u>underline</u> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello strike world")
    public void s() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <s>strike</s> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello strong world")
    public void strong() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <strong>strong</strong> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello emphasis world")
    public void em() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <em>emphasis</em> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello marked world")
    public void mark() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <mark>marked</mark> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello small world")
    public void small() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <small>small</small> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello deleted world")
    public void del() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <del>deleted</del> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello inserted world")
    public void ins() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <ins>inserted</ins> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello sub world")
    public void sub() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <sub>sub</sub> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello sup world")
    public void sup() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <sup>sup</sup> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello abbr world")
    public void abbr() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <abbr title='abbreviation'>abbr</abbr> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello cite world")
    public void cite() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <cite>cite</cite> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello quoted world")
    public void q() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <q>quoted</q> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello code world")
    public void code() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <code>code</code> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello kbd world")
    public void kbd() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <kbd>kbd</kbd> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello samp world")
    public void samp() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <samp>samp</samp> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello var world")
    public void var_() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <var>var</var> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello time world")
    public void time() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <time datetime='2026-01-01'>time</time> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello dfn world")
    public void dfn() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <dfn>dfn</dfn> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bdi world")
    public void bdi() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <bdi>bdi</bdi> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bdo world")
    public void bdo() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <bdo dir='rtl'>bdo</bdo> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello span world")
    public void span() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <span>span</span> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello world")
    public void wbr() throws Exception {
        // wbr is a zero-width break opportunity — no visible text
        getVisibleTextFormated("<p id='tester'>hello<wbr> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello\nruby\nannotation\nworld")
    @HtmlUnitNYI(CHROME = "hello rubyannotation world",
            EDGE = "hello rubyannotation world",
            FF = "hello rubyannotation world",
            FF_ESR = "hello rubyannotation world")
    public void ruby() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <ruby>ruby<rt>annotation</rt></ruby> world</p>");
    }

    // ════════════════════════════════════════════════════════════════════════
    // Combination of two formatting tags
    // ════════════════════════════════════════════════════════════════════════

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bold italic world")
    public void bAndI() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <b><i>bold italic</i></b> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bold strong world")
    public void bAndStrong() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <b><strong>bold strong</strong></b> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello strong em world")
    public void strongAndEm() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <strong><em>strong em</em></strong> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello marked code world")
    public void markAndCode() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <mark><code>marked code</code></mark> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello del ins world")
    public void delAndIns() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <del>del</del> <ins>ins</ins> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello sub sup world")
    public void subAndSup() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <sub>sub</sub> <sup>sup</sup> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello small mark world")
    public void smallAndMark() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <small><mark>small mark</mark></small> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello abbr cite world")
    public void abbrAndCite() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello "
                + "<abbr title='HyperText'>abbr</abbr> <cite>cite</cite> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello kbd code world")
    public void kbdAndCode() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <kbd><code>kbd code</code></kbd> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello samp var world")
    public void sampAndVar() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <samp>samp</samp> <var>var</var> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello time dfn world")
    public void timeAndDfn() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello "
                + "<time datetime='2026-01-01'>time</time> <dfn>dfn</dfn> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello span strong world")
    public void spanAndStrong() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <span><strong>span strong</strong></span> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello q cite world")
    public void qAndCite() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <q><cite>q cite</cite></q> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bdi bdo world")
    public void bdiAndBdo() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <bdi>bdi</bdi> <bdo dir='ltr'>bdo</bdo> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello u s world")
    public void uAndS() throws Exception {
        getVisibleTextFormated("<p id='tester'>hello <u>u</u> <s>s</s> world</p>");
    }

    // ════════════════════════════════════════════════════════════════════════
    // Real-world formatting samples
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Article paragraph with emphasis and strong — common editorial pattern.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("This is very important and should not be ignored.")
    public void realWorldEditorial() throws Exception {
        getVisibleTextFormated("<p id='tester'>"
                + "This is <strong>very <em>important</em></strong>"
                + " and should <u>not</u> be ignored."
                + "</p>");
    }

    /**
     * Code documentation paragraph — inline code and keyboard shortcuts.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Press Ctrl+C to copy the result to the clipboard.")
    public void realWorldCodeDoc() throws Exception {
        getVisibleTextFormated("<p id='tester'>"
                + "Press <kbd>Ctrl+C</kbd> to copy the <code>result</code>"
                + " to the clipboard."
                + "</p>");
    }

    /**
     * Price change paragraph — del (old price) and ins (new price).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Was: $99.99 Now: $49.99")
    public void realWorldPriceChange() throws Exception {
        getVisibleTextFormated("<p id='tester'>"
                + "Was: <del>$99.99</del> Now: <ins>$49.99</ins>"
                + "</p>");
    }

    /**
     * Scientific notation with sub and sup.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("H2O and E=mc2")
    public void realWorldScientific() throws Exception {
        getVisibleTextFormated("<p id='tester'>"
                + "H<sub>2</sub>O and E=mc<sup>2</sup>"
                + "</p>");
    }

    /**
     * Abbreviation with full title in a sentence.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The HTML spec is maintained by the W3C.")
    public void realWorldAbbreviation() throws Exception {
        getVisibleTextFormated("<p id='tester'>"
                + "The <abbr title='HyperText Markup Language'>HTML</abbr> spec"
                + " is maintained by the <abbr title='World Wide Web Consortium'>W3C</abbr>."
                + "</p>");
    }

    /**
     * Quote with citation — book reference pattern.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("To be or not to be from Hamlet")
    public void realWorldQuoteWithCitation() throws Exception {
        getVisibleTextFormated("<p id='tester'>"
                + "<q>To be or not to be</q>"
                + " from <cite>Hamlet</cite>"
                + "</p>");
    }

    /**
     * Marked search result highlight in a sentence.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("The quick brown fox jumps over the lazy dog.")
    public void realWorldSearchHighlight() throws Exception {
        getVisibleTextFormated("<p id='tester'>"
                + "The quick <mark>brown fox</mark> jumps over the lazy dog."
                + "</p>");
    }

    /**
     * Mixed: small print legal disclaimer with strong and em.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Terms apply. Not valid with other offers. See details.")
    public void realWorldSmallPrint() throws Exception {
        getVisibleTextFormated("<p id='tester'>"
                + "<small>"
                + "<strong>Terms apply.</strong> "
                + "<em>Not valid</em> with other offers. "
                + "See <span>details</span>."
                + "</small>"
                + "</p>");
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

    // ---------- inline elements that are not covered yet --------------------------

    /**
     * The a element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("link")
    public void tagA() throws Exception {
        getVisibleTextFormated("<a id='tester' href='#x'>link</a>");
    }

    /**
     * The data element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("one")
    public void tagData() throws Exception {
        getVisibleTextFormated("<data id='tester' value='1'>one</data>");
    }

    /**
     * The label element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("label")
    public void tagLabel() throws Exception {
        getVisibleTextFormated("<label id='tester'>label</label>");
    }

    /**
     * The nobr element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("no break")
    public void tagNobr() throws Exception {
        getVisibleTextFormated("<nobr id='tester'>no break</nobr>");
    }

    /**
     * The font element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("font")
    public void tagFont() throws Exception {
        getVisibleTextFormated("<font id='tester' color='red'>font</font>");
    }

    /**
     * The big element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("big")
    public void tagBig() throws Exception {
        getVisibleTextFormated("<big id='tester'>big</big>");
    }

    /**
     * The tt element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("tt")
    public void tagTt() throws Exception {
        getVisibleTextFormated("<tt id='tester'>tt</tt>");
    }

    /**
     * The strike element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("strike")
    public void tagStrike() throws Exception {
        getVisibleTextFormated("<strike id='tester'>strike</strike>");
    }

    /**
     * The acronym element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("acronym")
    public void tagAcronym() throws Exception {
        getVisibleTextFormated("<acronym id='tester' title='t'>acronym</acronym>");
    }

    /**
     * The blink element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("blink")
    public void tagBlink() throws Exception {
        getVisibleTextFormated("<blink id='tester'>blink</blink>");
    }

    /**
     * The marquee element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("marquee")
    public void tagMarquee() throws Exception {
        getVisibleTextFormated("<marquee id='tester'>marquee</marquee>");
    }

    /**
     * The output element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("output")
    public void tagOutput() throws Exception {
        getVisibleTextFormated("<output id='tester'>output</output>");
    }

    /**
     * The meter element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("meter")
    public void tagMeter() throws Exception {
        getVisibleTextFormated("<meter id='tester' value='1'>meter</meter>");
    }

    /**
     * The progress element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("progress")
    public void tagProgress() throws Exception {
        getVisibleTextFormated("<progress id='tester' value='1'>progress</progress>");
    }

    /**
     * The button element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("button")
    public void tagButton() throws Exception {
        getVisibleTextFormated("<button id='tester'>button</button>");
    }

    /**
     * The center element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("center")
    public void tagCenter() throws Exception {
        getVisibleTextFormated("<center id='tester'>center</center>");
    }

    /**
     * The rp element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void tagRp() throws Exception {
        getVisibleTextFormated("<ruby id='tester'>a<rp>(</rp>b<rp>)</rp></ruby>");
    }

    /**
     * The rb element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void tagRb() throws Exception {
        getVisibleTextFormated("<ruby id='tester'><rb>a</rb><rt>b</rt></ruby>");
    }

    /**
     * The rtc element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "a\nb",
            CHROME = "ab",
            EDGE = "ab")
    public void tagRtc() throws Exception {
        getVisibleTextFormated("<ruby id='tester'>a<rtc><rt>b</rt></rtc></ruby>");
    }

    /**
     * The slot element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fallback")
    public void tagSlot() throws Exception {
        getVisibleTextFormated("<slot id='tester'>fallback</slot>");
    }

    /**
     * The picture element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void tagPicture() throws Exception {
        getVisibleTextFormated("<picture id='tester'><source srcset='x.png'><img alt='alt'></picture>");
    }

    /**
     * The img element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void tagImg() throws Exception {
        getVisibleTextFormated("<img id='tester' alt='alt'>");
    }

    /**
     * The canvas element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fallback")
    public void tagCanvas() throws Exception {
        getVisibleTextFormated("<canvas id='tester'>fallback</canvas>");
    }

    /**
     * The object element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("fallback")
    public void tagObject() throws Exception {
        getVisibleTextFormated("<object id='tester' data='x.bin'>fallback</object>");
    }

    /**
     * The svg element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("svg")
    public void tagSvg() throws Exception {
        getVisibleTextFormated("<svg id='tester' width='50' height='10'><text x='0' y='10'>svg</text></svg>");
    }

    /**
     * The math element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("x")
    public void tagMath() throws Exception {
        getVisibleTextFormated("<math id='tester'><mi>x</mi></math>");
    }

    /**
     * The input element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void tagInput() throws Exception {
        getVisibleTextFormated("<input id='tester' value='input'>");
    }

    /**
     * The select element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("one\ntwo")
    public void tagSelect() throws Exception {
        getVisibleTextFormated("<select id='tester'><option>one</option><option>two</option></select>");
    }

    /**
     * The textarea element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("textarea")
    public void tagTextarea() throws Exception {
        getVisibleTextFormated("<textarea id='tester'>textarea</textarea>");
    }

    /**
     * The bdoRtl element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void tagBdoRtl() throws Exception {
        getVisibleTextFormated("<bdo id='tester' dir='rtl'>abc</bdo>");
    }

    /**
     * The qNested element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("outer inner")
    public void tagQNested() throws Exception {
        getVisibleTextFormated("<q id='tester'>outer <q>inner</q></q>");
    }

    /**
     * The listing element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a  b")
    public void tagListing() throws Exception {
        getVisibleTextFormated("<listing id='tester'>a  b</listing>");
    }

    /**
     * The xmp element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<b>a</b>")
    public void tagXmp() throws Exception {
        getVisibleTextFormated("<xmp id='tester'><b>a</b></xmp>");
    }

    /**
     * The plaintext element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a<b>b</b></div>\n</body></html>")
    public void tagPlaintext() throws Exception {
        getVisibleTextFormated("<div id='tester'><plaintext>a<b>b</b></div>");
    }

    /**
     * The customElement element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("custom")
    public void tagCustomElement() throws Exception {
        getVisibleTextFormated("<my-element id='tester'>custom</my-element>");
    }

    // ---------- paragraph structure -----------------------------------------------

    /**
     * An empty paragraph.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void emptyParagraph() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<p></p>b</div>");
    }

    /**
     * A paragraph that contains only white space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void whitespaceOnlyParagraph() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<p>   </p>b</div>");
    }

    /**
     * A paragraph that starts with a line break.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void paragraphWithBrStart() throws Exception {
        getVisibleTextFormated("<p id='tester'><br>a</p>");
    }

    /**
     * A paragraph with a line break in the middle.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void paragraphWithBrMiddle() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<br>b</p>");
    }

    /**
     * A paragraph that ends with a line break.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void paragraphWithBrEnd() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<br></p>");
    }

    /**
     * A paragraph with two line breaks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\n\nb")
    public void paragraphWithTwoBr() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<br><br>b</p>");
    }

    /**
     * Consecutive paragraphs.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphsConsecutive() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>a</p><p>b</p><p>c</p></div>");
    }

    /**
     * An empty paragraph between paragraphs.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void paragraphsWithEmptyBetween() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>a</p><p></p><p>b</p></div>");
    }

    /**
     * Text between paragraphs.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nx\nb\ny")
    public void paragraphsWithTextBetween() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>a</p>x<p>b</p>y</div>");
    }

    /**
     * Paragraphs that are closed by the next paragraph.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphImplicitlyClosed() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>a<p>b<p>c</div>");
    }

    /**
     * A paragraph inside of a div with text.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphInDiv() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<p>b</p>c</div>");
    }

    /**
     * A paragraph inside of a list item.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphInListItem() throws Exception {
        getVisibleTextFormated("<ul id='tester'><li>a<p>b</p>c</li></ul>");
    }

    /**
     * A paragraph inside of a table cell.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphInTableCell() throws Exception {
        getVisibleTextFormated("<table id='tester'><tr><td>a<p>b</p>c</td></tr></table>");
    }

    /**
     * A paragraph inside of a blockquote.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void paragraphInBlockquote() throws Exception {
        getVisibleTextFormated("<blockquote id='tester'><p>a</p><p>b</p></blockquote>");
    }

    /**
     * A paragraph inside of an inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphInSpan() throws Exception {
        getVisibleTextFormated("<span id='tester'>a<p>b</p>c</span>");
    }

    /**
     * A paragraph inside of a pre.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphInPre() throws Exception {
        getVisibleTextFormated("<pre id='tester'>a<p>b</p>c</pre>");
    }

    // ---------- paragraph and css -------------------------------------------------

    /**
     * A paragraph with display inline.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void paragraphDisplayInline() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<p style='display: inline'>b</p>c</div>");
    }

    /**
     * A paragraph with display inline-block.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void paragraphDisplayInlineBlock() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<p style='display: inline-block'>b</p>c</div>");
    }

    /**
     * A paragraph with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void paragraphDisplayNone() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<p style='display: none'>b</p>c</div>");
    }

    /**
     * A paragraph with visibility hidden.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nc")
    public void paragraphVisibilityHidden() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<p style='visibility: hidden'>b</p>c</div>");
    }

    /**
     * A paragraph with the hidden attribute.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void paragraphHiddenAttribute() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<p hidden>b</p>c</div>");
    }

    /**
     * A paragraph without margin.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphMargin() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<p style='margin: 0'>b</p>c</div>");
    }

    /**
     * A paragraph with white-space pre.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a   b\n  c")
    public void paragraphWhiteSpacePre() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: pre'>a   b\n  c</p>");
    }

    /**
     * A paragraph with white-space pre-line.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b\n c")
    public void paragraphWhiteSpacePreLine() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: pre-line'>a   b\n  c</p>");
    }

    /**
     * A paragraph with white-space nowrap.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b c")
    public void paragraphWhiteSpaceNowrap() throws Exception {
        getVisibleTextFormated("<p id='tester' style='white-space: nowrap'>a   b\n  c</p>");
    }

    /**
     * A paragraph with text-transform.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("A B C")
    public void paragraphTextTransform() throws Exception {
        getVisibleTextFormated("<p id='tester' style='text-transform: uppercase'>a <b>b</b> c</p>");
    }

    /**
     * A paragraph and headings.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphAndHeading() throws Exception {
        getVisibleTextFormated("<div id='tester'><h1>a</h1><p>b</p><h2>c</h2></div>");
    }

    /**
     * A paragraph and a horizontal rule.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void paragraphAndHr() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>a</p><hr><p>b</p></div>");
    }

    /**
     * A paragraph and a pre.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb  c\nd")
    public void paragraphAndPre() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>a</p><pre>b  c</pre><p>d</p></div>");
    }

    /**
     * A paragraph and a table.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphAndTable() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>a</p><table><tr><td>b</td></tr></table><p>c</p></div>");
    }

    /**
     * A paragraph and a list.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void paragraphAndList() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>a</p><ul><li>b</li></ul><p>c</p></div>");
    }

    /**
     * A line break directly before a paragraph.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb")
    public void paragraphBrBeforeP() throws Exception {
        getVisibleTextFormated("<div id='tester'>a<br><p>b</p></div>");
    }

    /**
     * A line break directly after a paragraph.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\n\nb")
    public void paragraphBrAfterP() throws Exception {
        getVisibleTextFormated("<div id='tester'><p>a</p><br>b</div>");
    }

    /**
     * A paragraph with comments.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ab")
    public void paragraphWithComment() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<!-- c -->b</p>");
    }

    /**
     * A paragraph with entities.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<a> & \u00A9 \u20AC")
    public void paragraphWithEntities() throws Exception {
        getVisibleTextFormated("<p id='tester'>&lt;a&gt; &amp; &copy; &euro;</p>");
    }

    /**
     * A paragraph with non-breaking spaces between inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b  c")
    public void paragraphWithNbspRuns() throws Exception {
        getVisibleTextFormated("<p id='tester'><b>a</b>&nbsp;<i>b</i>&nbsp;&nbsp;<u>c</u></p>");
    }

    // ---------- inline elements and white space -----------------------------------

    /**
     * Spaces across the boundaries of inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b c d")
    public void inlineSpacesAcrossBoundaries() throws Exception {
        getVisibleTextFormated("<p id='tester'>a <b> b </b> <i> c </i> d</p>");
    }

    /**
     * An empty inline element between spaces.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void inlineEmptyBetweenSpaces() throws Exception {
        getVisibleTextFormated("<p id='tester'>a <b></b> b</p>");
    }

    /**
     * An inline element that contains only a space.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b")
    public void inlineSpaceOnlyElement() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<b> </b>b</p>");
    }

    /**
     * Inline elements separated by new lines.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b c")
    public void inlineWithNewlinesBetween() throws Exception {
        getVisibleTextFormated("<p id='tester'><b>a</b>\n<i>b</i>\n\n<u>c</u></p>");
    }

    /**
     * Deeply nested inline elements.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcde")
    public void inlineDeeplyNested() throws Exception {
        getVisibleTextFormated("<p id='tester'><b>a<i>b<u>c<s>d<em>e</em></s></u></i></b></p>");
    }

    /**
     * An inline element with display block.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void inlineDisplayBlock() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<b style='display: block'>b</b>c</p>");
    }

    /**
     * An inline element with display none.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void inlineDisplayNone() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<b style='display: none'>b</b>c</p>");
    }

    /**
     * An inline element with display inline-block.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void inlineDisplayInlineBlock() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<b style='display: inline-block'>b</b>c</p>");
    }

    /**
     * An inline element with visibility hidden.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ac")
    public void inlineVisibilityHidden() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<b style='visibility: hidden'>b</b>c</p>");
    }

    /**
     * An inline element with white-space pre.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b   c d")
    public void inlineWhiteSpacePre() throws Exception {
        getVisibleTextFormated("<p id='tester'>a <b style='white-space: pre'>b   c</b> d</p>");
    }

    /**
     * An inline element with text-transform.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a B c")
    public void inlineTextTransform() throws Exception {
        getVisibleTextFormated("<p id='tester'>a <b style='text-transform: uppercase'>b</b> c</p>");
    }

    /**
     * A floating inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void inlineFloat() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<b style='float: left'>b</b>c</p>");
    }

    /**
     * An absolutely positioned inline element.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a\nb\nc")
    public void inlinePositionAbsolute() throws Exception {
        getVisibleTextFormated("<p id='tester'>a<b style='position: absolute'>b</b>c</p>");
    }

    /**
     * A wbr between words.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("longwordhere")
    public void wbrBetweenWords() throws Exception {
        getVisibleTextFormated("<p id='tester'>long<wbr>word<wbr>here</p>");
    }

    /**
     * A paragraph with links and punctuation.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("See the docs, the FAQ and the blog.")
    public void realWorldLinkList() throws Exception {
        getVisibleTextFormated("<p id='tester'>See <a href='#a'>the docs</a>, <a href='#b'>the FAQ</a> and <a href='#c'>"
                + "the blog</a>.</p>");
    }

    /**
     * A paragraph with a button.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Press OK to continue.")
    public void realWorldButtonInText() throws Exception {
        getVisibleTextFormated("<p id='tester'>Press <button>OK</button> to continue.</p>");
    }

    /**
     * A paragraph with form controls.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Name: Choice:\none\nL")
    public void realWorldFormInline() throws Exception {
        getVisibleTextFormated("<p id='tester'>Name: <input value='x'> Choice: <select><option>one</option></select> <label>"
                + "L</label></p>");
    }

    /**
     * A paragraph with an image.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Look: done")
    public void realWorldImageInText() throws Exception {
        getVisibleTextFormated("<p id='tester'>Look: <img alt='a cat'> done</p>");
    }
}
