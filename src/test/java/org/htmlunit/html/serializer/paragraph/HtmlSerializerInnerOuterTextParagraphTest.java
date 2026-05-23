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
import org.htmlunit.html.serializer.HtmlSerializerVisibleText;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link HtmlSerializerVisibleText} focusing on innerText/outerText
 * for paragraph text formatting tags: b, i, u, s, strong, em, mark, small,
 * del, ins, sub, sup, abbr, cite, q, code, kbd, samp, var, time, dfn,
 * bdi, bdo, ruby/rt, wbr, span.
 *
 * @author Ronald Brill
 */
public class HtmlSerializerInnerOuterTextParagraphTest extends WebDriverTestCase {

    // ════════════════════════════════════════════════════════════════════════
    // Single tag tests
    // ════════════════════════════════════════════════════════════════════════

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bold world")
    public void b() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <b>bold</b> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello italic world")
    public void i() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <i>italic</i> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello underline world")
    public void u() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <u>underline</u> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello strike world")
    public void s() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <s>strike</s> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello strong world")
    public void strong() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <strong>strong</strong> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello emphasis world")
    public void em() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <em>emphasis</em> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello marked world")
    public void mark() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <mark>marked</mark> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello small world")
    public void small() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <small>small</small> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello deleted world")
    public void del() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <del>deleted</del> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello inserted world")
    public void ins() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <ins>inserted</ins> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello sub world")
    public void sub() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <sub>sub</sub> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello sup world")
    public void sup() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <sup>sup</sup> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello abbr world")
    public void abbr() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <abbr title='abbreviation'>abbr</abbr> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello cite world")
    public void cite() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <cite>cite</cite> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello quoted world")
    public void q() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <q>quoted</q> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello code world")
    public void code() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <code>code</code> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello kbd world")
    public void kbd() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <kbd>kbd</kbd> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello samp world")
    public void samp() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <samp>samp</samp> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello var world")
    public void var_() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <var>var</var> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello time world")
    public void time() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <time datetime='2026-01-01'>time</time> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello dfn world")
    public void dfn() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <dfn>dfn</dfn> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bdi world")
    public void bdi() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <bdi>bdi</bdi> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bdo world")
    public void bdo() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <bdo dir='rtl'>bdo</bdo> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello span world")
    public void span() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <span>span</span> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello world")
    public void wbr() throws Exception {
        getInnerTextFormated("<p id='tester'>hello<wbr> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello rubyannotation world")
    public void ruby() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <ruby>ruby<rt>annotation</rt></ruby> world</p>");
    }

    // ════════════════════════════════════════════════════════════════════════
    // Combination of two formatting tags
    // ════════════════════════════════════════════════════════════════════════

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bold italic world")
    public void bAndI() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <b><i>bold italic</i></b> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bold strong world")
    public void bAndStrong() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <b><strong>bold strong</strong></b> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello strong em world")
    public void strongAndEm() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <strong><em>strong em</em></strong> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello marked code world")
    public void markAndCode() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <mark><code>marked code</code></mark> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello del ins world")
    public void delAndIns() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <del>del</del> <ins>ins</ins> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello sub sup world")
    public void subAndSup() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <sub>sub</sub> <sup>sup</sup> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello small mark world")
    public void smallAndMark() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <small><mark>small mark</mark></small> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello abbr cite world")
    public void abbrAndCite() throws Exception {
        getInnerTextFormated("<p id='tester'>hello "
                + "<abbr title='HyperText'>abbr</abbr> <cite>cite</cite> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello kbd code world")
    public void kbdAndCode() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <kbd><code>kbd code</code></kbd> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello samp var world")
    public void sampAndVar() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <samp>samp</samp> <var>var</var> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello time dfn world")
    public void timeAndDfn() throws Exception {
        getInnerTextFormated("<p id='tester'>hello "
                + "<time datetime='2026-01-01'>time</time> <dfn>dfn</dfn> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello span strong world")
    public void spanAndStrong() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <span><strong>span strong</strong></span> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello q cite world")
    public void qAndCite() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <q><cite>q cite</cite></q> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello bdi bdo world")
    public void bdiAndBdo() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <bdi>bdi</bdi> <bdo dir='ltr'>bdo</bdo> world</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("hello u s world")
    public void uAndS() throws Exception {
        getInnerTextFormated("<p id='tester'>hello <u>u</u> <s>s</s> world</p>");
    }

    // ════════════════════════════════════════════════════════════════════════
    // Real-world formatting samples
    // ════════════════════════════════════════════════════════════════════════

    /** @throws Exception if the test fails */
    @Test
    @Alerts("This is very important and should not be ignored.")
    public void realWorldEditorial() throws Exception {
        getInnerTextFormated("<p id='tester'>"
                + "This is <strong>very <em>important</em></strong>"
                + " and should <u>not</u> be ignored."
                + "</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("Press Ctrl+C to copy the result to the clipboard.")
    public void realWorldCodeDoc() throws Exception {
        getInnerTextFormated("<p id='tester'>"
                + "Press <kbd>Ctrl+C</kbd> to copy the <code>result</code>"
                + " to the clipboard."
                + "</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("Was: $99.99 Now: $49.99")
    public void realWorldPriceChange() throws Exception {
        getInnerTextFormated("<p id='tester'>"
                + "Was: <del>$99.99</del> Now: <ins>$49.99</ins>"
                + "</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("H2O and E=mc2")
    public void realWorldScientific() throws Exception {
        getInnerTextFormated("<p id='tester'>"
                + "H<sub>2</sub>O and E=mc<sup>2</sup>"
                + "</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("The HTML spec is maintained by the W3C.")
    public void realWorldAbbreviation() throws Exception {
        getInnerTextFormated("<p id='tester'>"
                + "The <abbr title='HyperText Markup Language'>HTML</abbr> spec"
                + " is maintained by the <abbr title='World Wide Web Consortium'>W3C</abbr>."
                + "</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("To be or not to be from Hamlet")
    public void realWorldQuoteWithCitation() throws Exception {
        getInnerTextFormated("<p id='tester'>"
                + "<q>To be or not to be</q>"
                + " from <cite>Hamlet</cite>"
                + "</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("The quick brown fox jumps over the lazy dog.")
    public void realWorldSearchHighlight() throws Exception {
        getInnerTextFormated("<p id='tester'>"
                + "The quick <mark>brown fox</mark> jumps over the lazy dog."
                + "</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("Terms apply. Not valid with other offers. See details.")
    public void realWorldSmallPrint() throws Exception {
        getInnerTextFormated("<p id='tester'>"
                + "<small>"
                + "<strong>Terms apply.</strong> "
                + "<em>Not valid</em> with other offers. "
                + "See <span>details</span>."
                + "</small>"
                + "</p>");
    }

    /** outerText variants for a selection of real-world samples */

    /** @throws Exception if the test fails */
    @Test
    @Alerts("This is very important and should not be ignored.")
    public void realWorldEditorialOuterText() throws Exception {
        getOuterTextFormated("<p id='tester'>"
                + "This is <strong>very <em>important</em></strong>"
                + " and should <u>not</u> be ignored."
                + "</p>");
    }

    /** @throws Exception if the test fails */
    @Test
    @Alerts("Press Ctrl+C to copy the result to the clipboard.")
    public void realWorldCodeDocOuterText() throws Exception {
        getOuterTextFormated("<p id='tester'>"
                + "Press <kbd>Ctrl+C</kbd> to copy the <code>result</code>"
                + " to the clipboard."
                + "</p>");
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
