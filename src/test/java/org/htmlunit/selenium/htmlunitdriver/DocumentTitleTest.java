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
package org.htmlunit.selenium.htmlunitdriver;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests the document title as seen by the driver ({@code WebDriver.getTitle()}) and by javascript
 * ({@code document.title}, {@code title.text}, {@code title.textContent}).
 *
 * <p>The expectations follow the HTML Standard: {@code document.title} is the child text content of the first
 * title element, with ASCII whitespace (tab, line feed, form feed, carriage return, space) stripped and
 * collapsed. Everything else is kept untouched: the non-breaking space, all other unicode spaces, invisible
 * characters, control characters, astral characters, and the different unicode normalization forms. Browsers
 * deviate in some corners (for example the vertical tab), so adjust single expectations per browser.
 *
 * <p>To not depend on how a transport treats special characters, every string is reported as its UTF-16 code
 * units in hex, separated by blanks: {@code "a" + nbsp + "b"} is {@code "0061 00a0 0062"}. The page itself
 * reports the javascript values in the {@code data-out} attribute of an element, so the title is not touched
 * by the reporting. Each test checks, in this order:
 * <ol>
 *   <li>{@code driver.getTitle()} (not for the tests marked as js only)</li>
 *   <li>the probes of the test; the static title tests report {@code document.title},
 *       {@code title.text} and {@code title.textContent} ({@code -} if there is no title element)</li>
 * </ol>
 *
 * @author Ronald Brill
 */
public class DocumentTitleTest extends WebDriverTestCase {

    private static final String[] STATIC_PROBES = {
        "c(document.title)", "T() ? c(T().text) : '-'", "T() ? c(T().textContent) : '-'"
    };

    private static final String PRELUDE =
              "function c(s) { var r = []; for (var i = 0; i < s.length; i++) "
                    + "{ r.push(('0000' + s.charCodeAt(i).toString(16)).slice(-4)); } return r.join(' '); }\n"
            + "function T() { return document.getElementsByTagName('title')[0]; }\n"
            + "function show(a) { document.getElementById('out').setAttribute('data-out', a.join('|')); }\n";

    // ------ Static title, HTML parser: basics -----------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0062 0063", "0061 0062 0063", "0061 0062 0063", "0061 0062 0063"})
    public void static_plain() throws Exception {
        run(false, true, "<title>abc</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", ""})
    public void static_empty() throws Exception {
        run(false, true, "<title></title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "-", "-"})
    public void static_missing() throws Exception {
        run(false, true, "", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0062 0063", "0061 0062 0063", "0061 0062 0063", "0061 0062 0063"})
    public void static_uppercaseTag() throws Exception {
        run(false, true, "<TITLE>abc</TITLE>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0062 0063", "0061 0062 0063", "0061 0062 0063", "0061 0062 0063"})
    public void static_attributes() throws Exception {
        run(false, true, "<title id=\"t\" lang=\"de\" dir=\"rtl\">abc</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0066 0069 0072 0073 0074", "0066 0069 0072 0073 0074", "0066 0069 0072 0073 0074", "0066 0069 0072 0073 0074"})
    public void static_multipleFirstWins() throws Exception {
        run(false, true, "<title>first</title><title>second</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0062 0063", "0061 0062 0063", "000a 0061 0062 0063", "000a 0061 0062 0063"})
    public void static_leadingNewlineKept() throws Exception {
        run(false, true, "<title>\nabc</title>", "", "", STATIC_PROBES);
    }

    // ------ Static title, HTML parser: ASCII whitespace is stripped and collapsed -----

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "0020 0009 000a 0020", "0020 0009 000a 0020"})
    public void static_onlyWhitespace() throws Exception {
        run(false, true, "<title> \t\n </title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061", "0061", "0020 0020 0061 0020 0020", "0020 0020 0061 0020 0020"})
    public void static_stripLeadingTrailing() throws Exception {
        run(false, true, "<title>  a  </title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 0020 0020 0020 0062", "0061 0020 0020 0020 0062"})
    public void static_collapseSpaces() throws Exception {
        run(false, true, "<title>a   b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 0009 0009 0062", "0061 0009 0009 0062"})
    public void static_collapseTabs() throws Exception {
        run(false, true, "<title>a\t\tb</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 000a 000a 0062", "0061 000a 000a 0062"})
    public void static_collapseLineFeeds() throws Exception {
        run(false, true, "<title>a\n\nb</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 000a 000a 0062", "0061 000a 000a 0062"})
    public void static_collapseCarriageReturns() throws Exception {
        run(false, true, "<title>a\r\rb</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 000a 0062", "0061 000a 0062"})
    public void static_collapseCrLf() throws Exception {
        run(false, true, "<title>a\r\nb</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 000c 000c 0062", "0061 000c 000c 0062"})
    public void static_collapseFormFeeds() throws Exception {
        run(false, true, "<title>a\f\fb</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0020 0009 0020 0061 0020 000a 0009 0020 000c 0020 0062 0020 0009 0020",
             "0020 0009 0020 0061 0020 000a 0009 0020 000c 0020 0062 0020 0009 0020"})
    public void static_collapseMixed() throws Exception {
        run(false, true, "<title> \t a \n\t \f b \t </title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 000b 0062", "0061 000b 0062", "0061 000b 0062", "0061 000b 0062"})
    public void static_verticalTabIsNotAsciiWhitespace() throws Exception {
        run(false, true, "<title>a\u000bb</title>", "", "", STATIC_PROBES);
    }

    // ------ Static title, HTML parser: non-breaking space -----------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 00a0 0062", "0061 00a0 0062", "0061 00a0 0062", "0061 00a0 0062"})
    public void static_nbspEntity() throws Exception {
        run(false, true, "<title>a&nbsp;b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 00a0 0062", "0061 00a0 0062", "0061 00a0 0062", "0061 00a0 0062"})
    public void static_nbspCharacter() throws Exception {
        run(false, true, "<title>a\u00a0b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"00a0 0061 00a0", "00a0 0061 00a0", "00a0 0061 00a0", "00a0 0061 00a0"})
    public void static_nbspAtEdges() throws Exception {
        run(false, true, "<title>\u00a0a\u00a0</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 00a0 00a0 0020 0062", "0061 00a0 00a0 0020 0062", "0061 00a0 00a0 0020 0020 0020 0062", "0061 00a0 00a0 0020 0020 0020 0062"})
    public void static_nbspNotCollapsed() throws Exception {
        run(false, true, "<title>a\u00a0\u00a0   b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 00a0 0020 0062", "0061 0020 00a0 0020 0062", "0061 0020 00a0 0020 0062", "0061 0020 00a0 0020 0062"})
    public void static_nbspBetweenSpaces() throws Exception {
        run(false, true, "<title>a \u00a0 b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"00a0", "00a0", "00a0", "00a0"})
    public void static_onlyNbsp() throws Exception {
        run(false, true, "<title>\u00a0</title>", "", "", STATIC_PROBES);
    }

    // ------ Static title, HTML parser: other unicode spaces and invisible characters are kept ---

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 1680 0062", "0061 1680 0062", "0061 1680 0062", "0061 1680 0062"})
    public void static_ogham() throws Exception {
        run(false, true, "<title>a\u1680b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 2000 2001 2002 2003 2004 2005 2006 2007 2008 2009 200a 0062",
             "0061 2000 2001 2002 2003 2004 2005 2006 2007 2008 2009 200a 0062",
             "0061 2000 2001 2002 2003 2004 2005 2006 2007 2008 2009 200a 0062",
             "0061 2000 2001 2002 2003 2004 2005 2006 2007 2008 2009 200a 0062"})
    public void static_enEmSpaces() throws Exception {
        run(false, true, "<title>a\u2000\u2001\u2002\u2003\u2004\u2005\u2006\u2007\u2008\u2009\u200ab</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 2028 2029 0062", "0061 2028 2029 0062", "0061 2028 2029 0062", "0061 2028 2029 0062"})
    public void static_lineAndParagraphSeparator() throws Exception {
        run(false, true, "<title>a\u2028\u2029b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0085 0062", "0061 0085 0062", "0061 0085 0062", "0061 0085 0062"})
    public void static_nextLine() throws Exception {
        run(false, true, "<title>a\u0085b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 202f 205f 3000 0062", "0061 202f 205f 3000 0062", "0061 202f 205f 3000 0062", "0061 202f 205f 3000 0062"})
    public void static_narrowMediumIdeographicSpace() throws Exception {
        run(false, true, "<title>a\u202f\u205f\u3000b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 200b 200c 200d 2060 feff 0062", "0061 200b 200c 200d 2060 feff 0062", "0061 200b 200c 200d 2060 feff 0062", "0061 200b 200c 200d 2060 feff 0062"})
    public void static_zeroWidth() throws Exception {
        run(false, true, "<title>a\u200b\u200c\u200d\u2060\ufeffb</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 00ad 0062", "0061 00ad 0062", "0061 00ad 0062", "0061 00ad 0062"})
    public void static_softHyphen() throws Exception {
        run(false, true, "<title>a\u00adb</title>", "", "", STATIC_PROBES);
    }

    // ------ Static title, HTML parser: control characters -----------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0061 0020 0062", "0061 0020 0062", "0061 0001 0062", "0061 0001 0062"},
            FF = {"0061 0001 0062", "0061 0001 0062", "0061 0001 0062", "0061 0001 0062"},
            FF_ESR = {"0061 0001 0062", "0061 0001 0062", "0061 0001 0062", "0061 0001 0062"})
    @HtmlUnitNYI(
            CHROME = {"0061 0001 0062", "0061 0001 0062", "0061 0001 0062", "0061 0001 0062"},
            EDGE = {"0061 0001 0062", "0061 0001 0062", "0061 0001 0062", "0061 0001 0062"})
    public void static_controlStartOfHeading() throws Exception {
        run(false, true, "<title>a\u0001b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0061 0020 0062", "0061 0020 0062", "0061 001e 0062", "0061 001e 0062"},
            FF = {"0061 001e 0062", "0061 001e 0062", "0061 001e 0062", "0061 001e 0062"},
            FF_ESR = {"0061 001e 0062", "0061 001e 0062", "0061 001e 0062", "0061 001e 0062"})
    @HtmlUnitNYI(
            CHROME = {"0061 001e 0062", "0061 001e 0062", "0061 001e 0062", "0061 001e 0062"},
            EDGE = {"0061 001e 0062", "0061 001e 0062", "0061 001e 0062", "0061 001e 0062"})
    public void static_controlRecordSeparator() throws Exception {
        run(false, true, "<title>a\u001eb</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0061 0020 0062", "0061 0020 0062", "0061 007f 0062", "0061 007f 0062"},
            FF = {"0061 007f 0062", "0061 007f 0062", "0061 007f 0062", "0061 007f 0062"},
            FF_ESR = {"0061 007f 0062", "0061 007f 0062", "0061 007f 0062", "0061 007f 0062"})
    @HtmlUnitNYI(
            CHROME = {"0061 007f 0062", "0061 007f 0062", "0061 007f 0062", "0061 007f 0062"},
            EDGE = {"0061 007f 0062", "0061 007f 0062", "0061 007f 0062", "0061 007f 0062"})
    public void static_controlDelete() throws Exception {
        run(false, true, "<title>a\u007fb</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 fffd 0062", "0061 fffd 0062", "0061 fffd 0062", "0061 fffd 0062"})
    public void static_nullCharacter() throws Exception {
        run(false, true, "<title>a\u0000b</title>", "", "", STATIC_PROBES);
    }

    // ------ Static title, HTML parser: character references ---------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0026 0020 0062", "0061 0020 0026 0020 0062", "0061 0020 0026 0020 0062", "0061 0020 0026 0020 0062"})
    public void static_entityAmp() throws Exception {
        run(false, true, "<title>a &amp; b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"003c 0062 003e", "003c 0062 003e", "003c 0062 003e", "003c 0062 003e"})
    public void static_entityLtGt() throws Exception {
        run(false, true, "<title>&lt;b&gt;</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0022 0071 0022 0020 0027 0061 0027", "0022 0071 0022 0020 0027 0061 0027",
             "0022 0071 0022 0020 0027 0061 0027", "0022 0071 0022 0020 0027 0061 0027"})
    public void static_entityQuot() throws Exception {
        run(false, true, "<title>&quot;q&quot; &apos;a&apos;</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0041 0042", "0041 0042", "0041 0042", "0041 0042"})
    public void static_entityNumericDecimal() throws Exception {
        run(false, true, "<title>&#65;&#66;</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0041 0042", "0041 0042", "0041 0042", "0041 0042"})
    public void static_entityNumericHex() throws Exception {
        run(false, true, "<title>&#x41;&#x42;</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0026 0020 0062", "0061 0020 0026 0020 0062", "0061 0020 0026 0020 0062", "0061 0020 0026 0020 0062"})
    public void static_entityLegacyWithoutSemicolon() throws Exception {
        run(false, true, "<title>a &amp b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 00ac 0061 006e 0065 006e 0074 0069 0074 0079 003b 0020 0062",
             "0061 0020 00ac 0061 006e 0065 006e 0074 0069 0074 0079 003b 0020 0062",
             "0061 0020 00ac 0061 006e 0065 006e 0074 0069 0074 0079 003b 0020 0062",
             "0061 0020 00ac 0061 006e 0065 006e 0074 0069 0074 0079 003b 0020 0062"})
    public void static_entityUnknown() throws Exception {
        run(false, true, "<title>a &notanentity; b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 000a 000a 0062", "0061 000a 000a 0062"})
    public void static_entityLineFeed() throws Exception {
        run(false, true, "<title>a&#10;&#10;b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 000d 000d 0062", "0061 000d 000d 0062"})
    public void static_entityCarriageReturn() throws Exception {
        run(false, true, "<title>a&#13;&#13;b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 0009 0009 0062", "0061 0009 0009 0062"})
    public void static_entityTab() throws Exception {
        run(false, true, "<title>a&#9;&#9;b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"00ad 200d 2026", "00ad 200d 2026", "00ad 200d 2026", "00ad 200d 2026"})
    public void static_entityShyZwjHellip() throws Exception {
        run(false, true, "<title>&shy;&zwj;&hellip;</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"20ac 0178", "20ac 0178", "20ac 0178", "20ac 0178"})
    public void static_entityWindows1252() throws Exception {
        run(false, true, "<title>&#x80;&#x9f;</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 fffd 0062", "0061 fffd 0062", "0061 fffd 0062", "0061 fffd 0062"})
    public void static_entityZero() throws Exception {
        run(false, true, "<title>a&#0;b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 fffd 0062", "0061 fffd 0062", "0061 fffd 0062", "0061 fffd 0062"})
    public void static_entityOutOfRange() throws Exception {
        run(false, true, "<title>a&#x110000;b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 fffd 0062", "0061 fffd 0062", "0061 fffd 0062", "0061 fffd 0062"})
    public void static_entitySurrogate() throws Exception {
        run(false, true, "<title>a&#xD800;b</title>", "", "", STATIC_PROBES);
    }

    // ------ Static title, HTML parser: markup inside the title is text ----------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 003c 0062 003e 0062 003c 002f 0062 003e 0020 0063",
             "0061 0020 003c 0062 003e 0062 003c 002f 0062 003e 0020 0063",
             "0061 0020 003c 0062 003e 0062 003c 002f 0062 003e 0020 0063",
             "0061 0020 003c 0062 003e 0062 003c 002f 0062 003e 0020 0063"})
    public void static_markupIsText() throws Exception {
        run(false, true, "<title>a <b>b</b> c</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 003c 0021 002d 002d 0020 0078 0020 002d 002d 003e 0020 0062",
             "0061 0020 003c 0021 002d 002d 0020 0078 0020 002d 002d 003e 0020 0062",
             "0061 0020 003c 0021 002d 002d 0020 0078 0020 002d 002d 003e 0020 0062",
             "0061 0020 003c 0021 002d 002d 0020 0078 0020 002d 002d 003e 0020 0062"})
    public void static_commentIsText() throws Exception {
        run(false, true, "<title>a <!-- x --> b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 003c 002f 0074 0069 0074 006c 0065 0078 003e 0062",
             "0061 003c 002f 0074 0069 0074 006c 0065 0078 003e 0062",
             "0061 003c 002f 0074 0069 0074 006c 0065 0078 003e 0062",
             "0061 003c 002f 0074 0069 0074 006c 0065 0078 003e 0062"})
    public void static_otherEndTagIsText() throws Exception {
        run(false, true, "<title>a</titlex>b</title>", "", "", STATIC_PROBES);
    }

    // ------ Static title, HTML parser: unicode is not normalized ----------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0065 0301", "0065 0301", "0065 0301", "0065 0301"})
    public void static_combiningAcute() throws Exception {
        run(false, true, "<title>e\u0301</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"00e9", "00e9", "00e9", "00e9"})
    public void static_precomposedAcute() throws Exception {
        run(false, true, "<title>\u00e9</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"212b", "212b", "212b", "212b"})
    public void static_angstromSign() throws Exception {
        run(false, true, "<title>\u212b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1112 1161 11ab", "1112 1161 11ab", "1112 1161 11ab", "1112 1161 11ab"})
    public void static_hangulJamo() throws Exception {
        run(false, true, "<title>\u1112\u1161\u11ab</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"fb01", "fb01", "fb01", "fb01"})
    public void static_ligature() throws Exception {
        run(false, true, "<title>\ufb01</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ff21 ff22", "ff21 ff22", "ff21 ff22", "ff21 ff22"})
    public void static_fullwidth() throws Exception {
        run(false, true, "<title>\uff21\uff22</title>", "", "", STATIC_PROBES);
    }

    // ------ Static title, HTML parser: astral characters and bidi controls ------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"d83d de00", "d83d de00", "d83d de00", "d83d de00"})
    public void static_emojiCharacter() throws Exception {
        run(false, true, "<title>\ud83d\ude00</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"d83d de00", "d83d de00", "d83d de00", "d83d de00"})
    public void static_emojiEntity() throws Exception {
        run(false, true, "<title>&#x1F600;</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"d83d dc68 200d d83d dc69 200d d83d dc67", "d83d dc68 200d d83d dc69 200d d83d dc67",
             "d83d dc68 200d d83d dc69 200d d83d dc67", "d83d dc68 200d d83d dc69 200d d83d dc67"})
    public void static_zwjSequence() throws Exception {
        run(false, true, "<title>\ud83d\udc68\u200d\ud83d\udc69\u200d\ud83d\udc67</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2764 fe0f", "2764 fe0f", "2764 fe0f", "2764 fe0f"})
    public void static_variationSelector() throws Exception {
        run(false, true, "<title>\u2764\ufe0f</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 202e 0062 0063 202c", "0061 202e 0062 0063 202c", "0061 202e 0062 0063 202c", "0061 202e 0062 0063 202c"})
    public void static_bidiControls() throws Exception {
        run(false, true, "<title>a\u202ebc\u202c</title>", "", "", STATIC_PROBES);
    }

    // ------ Title position in the document --------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0062 006f 0064 0079 0020 0074 0069 0074 006c 0065",
             "0062 006f 0064 0079 0020 0074 0069 0074 006c 0065",
             "0062 006f 0064 0079 0020 0074 0069 0074 006c 0065",
             "0062 006f 0064 0079 0020 0074 0069 0074 006c 0065"})
    public void position_titleInBody() throws Exception {
        run(false, true, "", "<title>body title</title>", "", "c(document.title)", "c(T().text)", "c(T().textContent)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0068 0065 0061 0064", "0068 0065 0061 0064", "0068 0065 0061 0064", "0068 0065 0061 0064"})
    public void position_headWinsOverBody() throws Exception {
        run(false, true, "<title>head</title>", "<title>body</title>", "", "c(document.title)", "c(T().text)", "c(T().textContent)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "1"})
    public void position_svgTitleIsIgnored() throws Exception {
        run(false, true, "", "<svg><title>svg title</title></svg>", "", "c(document.title)", "document.getElementsByTagName('title').length");
    }

    // ------ Setting document.title ----------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"006e 0065 0077", "006e 0065 0077", "006e 0065 0077"})
    public void set_simple() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = 'new';", "c(document.title)", "c(T().text)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0078", "1", "head", "0078"})
    public void set_createsTitleInHead() throws Exception {
        run(false, true, "", "", "document.title = 'x';", "document.getElementsByTagName('title').length", "T().parentNode.nodeName.toLowerCase()", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0020 0020 0061 0020 000a 0009 0020 0062 0020 0020", "0020 0020 0061 0020 000a 0009 0020 0062 0020 0020"})
    public void set_whitespace() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = '  a \\n\\t b  ';", "c(document.title)", "c(T().text)", "c(T().textContent)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0061 000d 000a 0062"})
    public void set_crlfIsNotNormalized() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = 'a\\r\\nb';", "c(document.title)", "c(T().text)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 00a0 0062", "0061 00a0 0062", "0061 00a0 0062"})
    public void set_nbsp() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = 'a\\u00a0b';", "c(document.title)", "c(T().text)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"00a0 0061 00a0", "00a0 0061 00a0"})
    public void set_nbspAtEdges() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = '\\u00a0a\\u00a0';", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 2007 3000 202f 0062", "0061 2007 3000 202f 0062"})
    public void set_otherUnicodeSpaces() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = 'a\\u2007\\u3000\\u202fb';", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"003c 0062 003e 0078 003c 002f 0062 003e 0020 0026 0020 0026 0061 006d 0070 003b",
             "003c 0062 003e 0078 003c 002f 0062 003e 0020 0026 0020 0026 0061 006d 0070 003b",
             "1", "3",
             "0026 006c 0074 003b 0062 0026 0067 0074 003b 0078 0026 006c 0074 003b 002f 0062 0026 0067 0074 003b 0020 0026 0061 006d 0070 003b 0020 0026 0061 006d 0070 003b 0061 006d 0070 003b"})
    public void set_markupCharacters() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = '<b>x</b> & &amp;';", "c(document.title)", "T().childNodes.length", "T().firstChild.nodeType", "c(T().innerHTML)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"006e 0075 006c 006c", "006e 0075 006c 006c"})
    public void set_null() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = null;", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0075 006e 0064 0065 0066 0069 006e 0065 0064", "0075 006e 0064 0065 0066 0069 006e 0065 0064"})
    public void set_undefined() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = undefined;", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0031 0032 0033", "0031 0032 0033"})
    public void set_number() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = 123;", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0031 002c 0032", "0031 002c 0032"})
    public void set_array() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = [1, 2];", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "", "1"})
    public void set_emptyString() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = '';", "c(document.title)", "c(T().text)", "document.getElementsByTagName('title').length");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"d83d de00", "d83d de00"})
    public void set_astral() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = '\\uD83D\\uDE00';", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("d800")
    public void set_loneSurrogateJsOnly() throws Exception {
        run(false, false, "<title>old</title>", "", "document.title = '\\uD800';", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0062", "0062", "1"})
    public void set_twice() throws Exception {
        run(false, true, "<title>old</title>", "", "document.title = 'a'; document.title = 'b';", "c(document.title)", "T().childNodes.length");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"006e 0065 0077", "006e 0065 0077", "0074 0077 006f"})
    public void set_updatesFirstOfTwoTitles() throws Exception {
        run(false, true, "<title>one</title><title>two</title>", "", "document.title = 'new';", "c(document.title)", "c(document.getElementsByTagName('title')[1].text)");
    }

    // ------ Changing the title element ------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0078 0020 0079", "0078 0020 0079", "0078 0020 0020 0079", "0078 0020 0020 0079"})
    public void element_textProperty() throws Exception {
        run(false, true, "<title>old</title>", "", "T().text = 'x  y';", "c(document.title)", "c(T().text)", "c(T().textContent)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"007a", "007a", "0020 007a 0020"})
    public void element_textContentProperty() throws Exception {
        run(false, true, "<title>old</title>", "", "T().textContent = ' z ';", "c(document.title)", "c(T().textContent)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0062", "0061 0062", "0061 0062", "0061 0045 004c 0045 004d 0062"})
    public void element_childTextContentOnly() throws Exception {
        run(false, true, "<title>a</title>", "",
                "var s = document.createElement('span'); s.textContent = 'ELEM'; T().appendChild(s); T().appendChild(document.createTextNode('b'));", "c(document.title)", "c(T().text)", "c(T().textContent)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"007a", "007a", "1"})
    public void element_setterReplacesChildren() throws Exception {
        run(false, true, "<title>a</title>", "", "var s = document.createElement('span'); T().appendChild(s); document.title = 'z';", "c(document.title)", "T().childNodes.length");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0020 0020 0061 0020 0020 0020 0062 0020"})
    public void element_innerText() throws Exception {
        run(false, true, "<title>  a   b </title>", "", "", "c(document.title)", "c(T().innerText)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"006d 0061 0064 0065", "006d 0061 0064 0065"})
    public void element_created() throws Exception {
        run(false, true, "", "", "var t = document.createElement('title'); t.text = 'made'; document.head.appendChild(t);", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"", "", "0"})
    public void element_removed() throws Exception {
        run(false, true, "<title>x</title>", "", "T().parentNode.removeChild(T());", "c(document.title)", "document.getElementsByTagName('title').length");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0074 0077 006f", "0074 0077 006f"})
    public void element_removeFirstOfTwo() throws Exception {
        run(false, true, "<title>one</title><title>two</title>", "", "T().parentNode.removeChild(T());", "c(document.title)");
    }

    // ------ XHTML document (XML parser) -----------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0062 0063", "0061 0062 0063", "0061 0062 0063", "0061 0062 0063"})
    public void xhtml_plain() throws Exception {
        run(true, true, "<title>abc</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0020 0020 0061 0020 000a 0009 0020 0062 0020 0020", "0020 0020 0061 0020 000a 0009 0020 0062 0020 0020"})
    public void xhtml_whitespace() throws Exception {
        run(true, true, "<title>  a \n\t b  </title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 00a0 0062", "0061 00a0 0062", "0061 00a0 0062", "0061 00a0 0062"})
    public void xhtml_nbspCharacterReference() throws Exception {
        run(true, true, "<title>a&#160;b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 00a0 0062", "0061 00a0 0062", "0061 00a0 0062", "0061 00a0 0062"})
    public void xhtml_nbspCharacter() throws Exception {
        run(true, true, "<title>a\u00a0b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 2007 3000 0062", "0061 2007 3000 0062", "0061 2007 3000 0062", "0061 2007 3000 0062"})
    public void xhtml_otherUnicodeSpaces() throws Exception {
        run(true, true, "<title>a\u2007\u3000b</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"d83d de00", "d83d de00", "d83d de00", "d83d de00"})
    public void xhtml_emojiCharacterReference() throws Exception {
        run(true, true, "<title>&#x1F600;</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0026 0020 0062 0020 003c 0020 0063", "0061 0020 0026 0020 0062 0020 003c 0020 0063",
             "0061 0020 0026 0020 0062 0020 003c 0020 0063", "0061 0020 0026 0020 0062 0020 003c 0020 0063"})
    public void xhtml_entities() throws Exception {
        run(true, true, "<title>a &amp; b &lt; c</title>", "", "", STATIC_PROBES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0063", "0061 0063", "0061 0063", "0061 0062 0063"})
    @HtmlUnitNYI(
            CHROME = {"0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                      "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                      "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                      "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063"},
            EDGE = {"0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                    "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                    "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                    "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063"},
            FF = {"0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                  "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                  "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                  "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063"},
            FF_ESR = {"0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                      "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                      "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063",
                      "0061 003c 0062 003e 0062 003c 002f 0062 003e 0063"})
    public void xhtml_childElementIsIgnored() throws Exception {
        run(true, true, "<title>a<b>b</b>c</title>", "", "", "c(document.title)", "c(T().text)", "c(T().textContent)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0062 006f 0064 0079 0020 0074 0069 0074 006c 0065", "0062 006f 0064 0079 0020 0074 0069 0074 006c 0065"})
    public void xhtml_titleInBody() throws Exception {
        run(true, true, "", "<title>body title</title>", "", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"006e 0065 0077", "006e 0065 0077"})
    public void xhtml_set() throws Exception {
        run(true, true, "<title>old</title>", "", "document.title = 'new';", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 0020 0062", "0061 0020 0062", "0020 0020 0061 0020 000a 0009 0020 0062 0020 0020"})
    public void xhtml_setWhitespace() throws Exception {
        run(true, true, "<title>old</title>", "", "document.title = '  a \\n\\t b  ';", "c(document.title)", "c(T().text)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0061 00a0 0062", "0061 00a0 0062"})
    public void xhtml_setNbsp() throws Exception {
        run(true, true, "<title>old</title>", "", "document.title = 'a\\u00a0b';", "c(document.title)");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0078", "1", "head"})
    public void xhtml_setCreatesTitleInHead() throws Exception {
        run(true, true, "", "", "document.title = 'x';", "document.getElementsByTagName('title').length", "T().parentNode.nodeName.toLowerCase()");
    }

    // ------------------------------------------------------------------ other

    /**
     * The title of an iframe document is not the title of the page.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0074 006f 0070", "0074 006f 0070", "0074 006f 0070", "0074 006f 0070"})
    public void iframeTitleIsIgnored() throws Exception {
        getMockWebConnection().setResponse(URL_SECOND,
                "<html><head><title>inner</title></head><body></body></html>");
        run(false, true, "<title>top</title>", "<iframe src='" + URL_SECOND + "'></iframe>", "", STATIC_PROBES);
    }

    /**
     * The log function used by many tests has to make the non-breaking space and the blanks visible.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a\\u00a0b", "c\\sd", "\\u00a0"})
    public void logFunctionNormalizeEscapesNbsp() throws Exception {
        final String html = "<html><head><title></title>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log('a\\u00a0b'); log('c d'); log('\\u00a0');\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    // ------------------------------------------------------------------ helpers

    /**
     * Loads a page, runs the script on load and compares the driver title and the probes.
     *
     * @param xhtml load the page as application/xhtml+xml
     * @param checkDriver compare driver.getTitle() as first value
     * @param head markup added to the head (normally the title element)
     * @param body markup added to the body
     * @param script js statements run before the probes are evaluated
     * @param probes js expressions, evaluated in order and reported
     */
    private void run(final boolean xhtml, final boolean checkDriver, final String head, final String body,
            final String script, final String... probes) throws Exception {
        final String js = PRELUDE
            + "function test() {\n"
            + script + "\n"
            + "show([" + String.join(", ", probes) + "]);\n"
            + "}\n";

        final String page;
        final String contentType;
        if (xhtml) {
            contentType = "application/xhtml+xml";
            page = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head>" + head + "\n"
                + "<script>//<![CDATA[\n" + js + "//]]></script></head>\n"
                + "<body onload=\"test()\">" + body + "<pre id=\"out\"></pre></body></html>";
        }
        else {
            contentType = "text/html";
            page = "<!DOCTYPE html>\n"
                + "<html><head><meta charset=\"utf-8\">" + head + "\n"
                + "<script>\n" + js + "</script></head>\n"
                + "<body onload=\"test()\">" + body + "<pre id=\"out\"></pre></body></html>";
        }

        final WebDriver driver = loadPage2(page, URL_FIRST, contentType, StandardCharsets.UTF_8);

        final List<String> actual = new ArrayList<>();
        if (checkDriver) {
            actual.add(codes(driver.getTitle()));
        }
        final String out = driver.findElement(By.id("out")).getAttribute("data-out");
        assertNotNull(out, "the script did not finish");
        actual.addAll(Arrays.asList(out.split("\\|", -1)));

        assertEquals(Arrays.asList(getExpectedAlerts()), actual);
    }

    /** The UTF-16 code units of the string as hex, separated by blanks (same format as the js function c). */
    private static String codes(final String s) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (i > 0) {
                sb.append(' ');
            }
            sb.append(String.format("%04x", (int) s.charAt(i)));
        }
        return sb.toString();
    }
}