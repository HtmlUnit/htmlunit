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
package org.htmlunit.html.parser;

import java.nio.charset.StandardCharsets;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests how a trailing slash is handled on the other special elements, see {@link SelfClosingTagsTest}
 * for {@code div}, {@code iframe} and {@code script}.
 *
 * <p>Elements whose content the HTML parser treats as text, so the slash is ignored there
 * (first log line {@code none}):
 * <ul>
 *   <li>raw text: {@code style}, {@code xmp}, {@code noembed}, {@code noframes},
 *       {@code noscript} (with scripting enabled)</li>
 *   <li>escapable raw text: {@code textarea}, {@code title}</li>
 *   <li>{@code plaintext}: no end tag, everything behind it is text</li>
 * </ul>
 * Foreign content ({@code svg}, {@code math}) is the opposite: the HTML parser honors the slash (first log
 * line {@code body} / {@code section}), so these two are regression controls against a rule like
 * "never honor the slash in HTML mode". The XML parser always honors the slash. *
 * <p>Every test puts a probe element {@code <b id='child'>} behind the element under test and logs what
 * the parser made of it, one {@code log()} entry per line. The container is the body for the page contexts
 * and the {@code section} that receives the innerHTML for the fragment contexts:
 * <ol>
 *   <li>node name of the probe's parent, or {@code none} if the probe is not part of the DOM
 *       (it was swallowed as text)</li>
 *   <li>the container's child nodes (whitespace-only text nodes left out)</li>
 *   <li>number of elements with the tested tag name in the document
 *       (includes the page's own {@code script} / {@code title})</li>
 *   <li>number of child nodes of the last such element</li>
 *   <li>text content of the last such element</li>
 *   <li>innerHTML of the container</li>
 * </ol>
 *
 * @author Ronald Brill
 */
public class SelfClosingRawTextElementsTest extends WebDriverTestCase {

    // probes for the HTML parser; the closing tag bounds the text of the raw text elements
    private static final String HTML_STYLE = "<style/><b id=\"child\">x</b></style>";
    private static final String HTML_TEXTAREA = "<textarea/><b id=\"child\">x</b></textarea>";
    private static final String HTML_TITLE = "<title/><b id=\"child\">x</b></title>";
    private static final String HTML_XMP = "<xmp/><b id=\"child\">x</b></xmp>";
    private static final String HTML_NOEMBED = "<noembed/><b id=\"child\">x</b></noembed>";
    private static final String HTML_NOFRAMES = "<noframes/><b id=\"child\">x</b></noframes>";
    private static final String HTML_NOSCRIPT = "<noscript/><b id=\"child\">x</b></noscript>";
    private static final String HTML_PLAINTEXT = "<plaintext/><b id=\"child\">x</b>";
    private static final String HTML_SVG = "<svg/><b id=\"child\">x</b>";
    private static final String HTML_MATH = "<math/><b id=\"child\">x</b>";

    // probes for the XML parser; a stray closing tag would not be well-formed
    private static final String XML_STYLE = "<style/><b id=\"child\">x</b>";
    private static final String XML_TEXTAREA = "<textarea/><b id=\"child\">x</b>";
    private static final String XML_TITLE = "<title/><b id=\"child\">x</b>";
    private static final String XML_XMP = "<xmp/><b id=\"child\">x</b>";
    private static final String XML_NOEMBED = "<noembed/><b id=\"child\">x</b>";
    private static final String XML_NOFRAMES = "<noframes/><b id=\"child\">x</b>";
    private static final String XML_NOSCRIPT = "<noscript/><b id=\"child\">x</b>";
    private static final String XML_PLAINTEXT = "<plaintext/><b id=\"child\">x</b>";
    private static final String XML_SVG = "<svg/><b id=\"child\">x</b>";
    private static final String XML_MATH = "<math/><b id=\"child\">x</b>";

    // ---------- HTML page ---------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "style", "1", "1", "<b id=\"child\">x</b>", "\\n<style><b id=\"child\">x</b></style>\\n"})
    public void style_htmlPage() throws Exception {
        htmlPage("style", HTML_STYLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "textarea", "1", "1", "<b id=\"child\">x</b>", "\\n<textarea>&lt;b id=\"child\"&gt;x&lt;/b&gt;</textarea>\\n"})
    public void textarea_htmlPage() throws Exception {
        htmlPage("textarea", HTML_TEXTAREA);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "title", "2", "1", "<b id=\"child\">x</b>", "\\n<title>&lt;b id=\"child\"&gt;x&lt;/b&gt;</title>\\n"})
    public void title_htmlPage() throws Exception {
        htmlPage("title", HTML_TITLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "xmp", "1", "1", "<b id=\"child\">x</b>", "\\n<xmp><b id=\"child\">x</b></xmp>\\n"})
    public void xmp_htmlPage() throws Exception {
        htmlPage("xmp", HTML_XMP);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "noembed", "1", "1", "<b id=\"child\">x</b>", "\\n<noembed><b id=\"child\">x</b></noembed>\\n"})
    public void noembed_htmlPage() throws Exception {
        htmlPage("noembed", HTML_NOEMBED);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "noframes", "1", "1", "<b id=\"child\">x</b>", "\\n<noframes><b id=\"child\">x</b></noframes>\\n"})
    public void noframes_htmlPage() throws Exception {
        htmlPage("noframes", HTML_NOFRAMES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "noscript", "1", "1", "<b id=\"child\">x</b>", "\\n<noscript><b id=\"child\">x</b></noscript>\\n"})
    public void noscript_htmlPage() throws Exception {
        htmlPage("noscript", HTML_NOSCRIPT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "plaintext", "1", "1", "<b id=\"child\">x</b>\\n</body></html>", "\\n<plaintext><b id=\"child\">x</b>\\n</body></html></plaintext>"})
    public void plaintext_htmlPage() throws Exception {
        htmlPage("plaintext", HTML_PLAINTEXT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "svg,b", "1", "0", "", "\\n<svg></svg><b id=\"child\">x</b>\\n"})
    public void svg_htmlPage() throws Exception {
        htmlPage("svg", HTML_SVG);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "math,b", "1", "0", "", "\\n<math></math><b id=\"child\">x</b>\\n"})
    public void math_htmlPage() throws Exception {
        htmlPage("math", HTML_MATH);
    }

    // ---------- XHTML page --------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "style,b", "1", "0", "", "\\n<style xmlns=\"http://www.w3.org/1999/xhtml\"></style><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "style,b", "1", "0", "", "\\n<style></style><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "style,b", "1", "0", "", "\\n<style></style><b id=\"child\">x</b>\\n"},
            FF = {"body", "style,b", "1", "0", "", "\\n<style></style><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "style,b", "1", "0", "", "\\n<style></style><b id=\"child\">x</b>\\n"})
    public void style_xhtmlPage() throws Exception {
        xhtmlPage("style", XML_STYLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "textarea,b", "1", "0", "", "\\n<textarea xmlns=\"http://www.w3.org/1999/xhtml\"></textarea><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "textarea,b", "1", "0", "", "\\n<textarea></textarea><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "textarea,b", "1", "0", "", "\\n<textarea></textarea><b id=\"child\">x</b>\\n"},
            FF = {"body", "textarea,b", "1", "0", "", "\\n<textarea></textarea><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "textarea,b", "1", "0", "", "\\n<textarea></textarea><b id=\"child\">x</b>\\n"})
    public void textarea_xhtmlPage() throws Exception {
        xhtmlPage("textarea", XML_TEXTAREA);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "title,b", "2", "0", "", "\\n<title xmlns=\"http://www.w3.org/1999/xhtml\"></title><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "title,b", "2", "0", "", "\\n<title></title><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "title,b", "2", "0", "", "\\n<title></title><b id=\"child\">x</b>\\n"},
            FF = {"body", "title,b", "2", "0", "", "\\n<title></title><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "title,b", "2", "0", "", "\\n<title></title><b id=\"child\">x</b>\\n"})
    public void title_xhtmlPage() throws Exception {
        xhtmlPage("title", XML_TITLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "xmp,b", "1", "0", "", "\\n<xmp xmlns=\"http://www.w3.org/1999/xhtml\"></xmp><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "xmp,b", "1", "0", "", "\\n<xmp></xmp><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "xmp,b", "1", "0", "", "\\n<xmp></xmp><b id=\"child\">x</b>\\n"},
            FF = {"body", "xmp,b", "1", "0", "", "\\n<xmp></xmp><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "xmp,b", "1", "0", "", "\\n<xmp></xmp><b id=\"child\">x</b>\\n"})
    public void xmp_xhtmlPage() throws Exception {
        xhtmlPage("xmp", XML_XMP);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "noembed,b", "1", "0", "", "\\n<noembed xmlns=\"http://www.w3.org/1999/xhtml\"></noembed><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "noembed,b", "1", "0", "", "\\n<noembed></noembed><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "noembed,b", "1", "0", "", "\\n<noembed></noembed><b id=\"child\">x</b>\\n"},
            FF = {"body", "noembed,b", "1", "0", "", "\\n<noembed></noembed><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "noembed,b", "1", "0", "", "\\n<noembed></noembed><b id=\"child\">x</b>\\n"})
    public void noembed_xhtmlPage() throws Exception {
        xhtmlPage("noembed", XML_NOEMBED);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "noframes,b", "1", "0", "", "\\n<noframes xmlns=\"http://www.w3.org/1999/xhtml\"></noframes><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "noframes,b", "1", "0", "", "\\n<noframes></noframes><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "noframes,b", "1", "0", "", "\\n<noframes></noframes><b id=\"child\">x</b>\\n"},
            FF = {"body", "noframes,b", "1", "0", "", "\\n<noframes></noframes><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "noframes,b", "1", "0", "", "\\n<noframes></noframes><b id=\"child\">x</b>\\n"})
    public void noframes_xhtmlPage() throws Exception {
        xhtmlPage("noframes", XML_NOFRAMES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "noscript,b", "1", "0", "", "\\n<noscript xmlns=\"http://www.w3.org/1999/xhtml\"></noscript><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "noscript,b", "1", "0", "", "\\n<noscript></noscript><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "noscript,b", "1", "0", "", "\\n<noscript></noscript><b id=\"child\">x</b>\\n"},
            FF = {"body", "noscript,b", "1", "0", "", "\\n<noscript></noscript><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "noscript,b", "1", "0", "", "\\n<noscript></noscript><b id=\"child\">x</b>\\n"})
    public void noscript_xhtmlPage() throws Exception {
        xhtmlPage("noscript", XML_NOSCRIPT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "plaintext,b", "1", "0", "", "\\n<plaintext xmlns=\"http://www.w3.org/1999/xhtml\"></plaintext><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "plaintext,b", "1", "0", "", "\\n<plaintext></plaintext><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "plaintext,b", "1", "0", "", "\\n<plaintext></plaintext><b id=\"child\">x</b>\\n"},
            FF = {"body", "plaintext,b", "1", "0", "", "\\n<plaintext></plaintext><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "plaintext,b", "1", "0", "", "\\n<plaintext></plaintext><b id=\"child\">x</b>\\n"})
    public void plaintext_xhtmlPage() throws Exception {
        xhtmlPage("plaintext", XML_PLAINTEXT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "svg,b", "1", "0", "", "\\n<svg xmlns=\"http://www.w3.org/1999/xhtml\"></svg><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "svg,b", "1", "0", "", "\\n<svg></svg><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "svg,b", "1", "0", "", "\\n<svg></svg><b id=\"child\">x</b>\\n"},
            FF = {"body", "svg,b", "1", "0", "", "\\n<svg></svg><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "svg,b", "1", "0", "", "\\n<svg></svg><b id=\"child\">x</b>\\n"})
    public void svg_xhtmlPage() throws Exception {
        xhtmlPage("svg", XML_SVG);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "math,b", "1", "0", "", "\\n<math xmlns=\"http://www.w3.org/1999/xhtml\"></math><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "math,b", "1", "0", "", "\\n<math></math><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "math,b", "1", "0", "", "\\n<math></math><b id=\"child\">x</b>\\n"},
            FF = {"body", "math,b", "1", "0", "", "\\n<math></math><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "math,b", "1", "0", "", "\\n<math></math><b id=\"child\">x</b>\\n"})
    public void math_xhtmlPage() throws Exception {
        xhtmlPage("math", XML_MATH);
    }

    // ---------- innerHTML, HTML document ------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "style", "1", "1", "<b id=\"child\">x</b>", "<style><b id=\"child\">x</b></style>"})
    public void style_innerHtml() throws Exception {
        innerHtmlInHtmlPage("style", HTML_STYLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "textarea", "1", "1", "<b id=\"child\">x</b>", "<textarea>&lt;b id=\"child\"&gt;x&lt;/b&gt;</textarea>"})
    public void textarea_innerHtml() throws Exception {
        innerHtmlInHtmlPage("textarea", HTML_TEXTAREA);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "title", "2", "1", "<b id=\"child\">x</b>", "<title>&lt;b id=\"child\"&gt;x&lt;/b&gt;</title>"})
    public void title_innerHtml() throws Exception {
        innerHtmlInHtmlPage("title", HTML_TITLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "xmp", "1", "1", "<b id=\"child\">x</b>", "<xmp><b id=\"child\">x</b></xmp>"})
    public void xmp_innerHtml() throws Exception {
        innerHtmlInHtmlPage("xmp", HTML_XMP);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "noembed", "1", "1", "<b id=\"child\">x</b>", "<noembed><b id=\"child\">x</b></noembed>"})
    public void noembed_innerHtml() throws Exception {
        innerHtmlInHtmlPage("noembed", HTML_NOEMBED);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "noframes", "1", "1", "<b id=\"child\">x</b>", "<noframes><b id=\"child\">x</b></noframes>"})
    public void noframes_innerHtml() throws Exception {
        innerHtmlInHtmlPage("noframes", HTML_NOFRAMES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "noscript", "1", "1", "<b id=\"child\">x</b>", "<noscript><b id=\"child\">x</b></noscript>"})
    public void noscript_innerHtml() throws Exception {
        innerHtmlInHtmlPage("noscript", HTML_NOSCRIPT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "plaintext", "1", "1", "<b id=\"child\">x</b>", "<plaintext><b id=\"child\">x</b></plaintext>"})
    public void plaintext_innerHtml() throws Exception {
        innerHtmlInHtmlPage("plaintext", HTML_PLAINTEXT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "svg,b", "1", "0", "", "<svg></svg><b id=\"child\">x</b>"})
    public void svg_innerHtml() throws Exception {
        innerHtmlInHtmlPage("svg", HTML_SVG);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "math,b", "1", "0", "", "<math></math><b id=\"child\">x</b>"})
    public void math_innerHtml() throws Exception {
        innerHtmlInHtmlPage("math", HTML_MATH);
    }

    // ---------- innerHTML, XHTML document -----------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "style,b", "1", "0", "", "<style xmlns=\"http://www.w3.org/1999/xhtml\"></style><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "style,b", "1", "0", "", "<style></style><b id=\"child\">x</b>"},
            EDGE = {"section", "style,b", "1", "0", "", "<style></style><b id=\"child\">x</b>"},
            FF = {"section", "style,b", "1", "0", "", "<style></style><b id=\"child\">x</b>"},
            FF_ESR = {"section", "style,b", "1", "0", "", "<style></style><b id=\"child\">x</b>"})
    public void style_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("style", XML_STYLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "textarea,b", "1", "0", "", "<textarea xmlns=\"http://www.w3.org/1999/xhtml\"></textarea><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "textarea,b", "1", "0", "", "<textarea></textarea><b id=\"child\">x</b>"},
            EDGE = {"section", "textarea,b", "1", "0", "", "<textarea></textarea><b id=\"child\">x</b>"},
            FF = {"section", "textarea,b", "1", "0", "", "<textarea></textarea><b id=\"child\">x</b>"},
            FF_ESR = {"section", "textarea,b", "1", "0", "", "<textarea></textarea><b id=\"child\">x</b>"})
    public void textarea_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("textarea", XML_TEXTAREA);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "title,b", "2", "0", "", "<title xmlns=\"http://www.w3.org/1999/xhtml\"></title><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "title,b", "2", "0", "", "<title></title><b id=\"child\">x</b>"},
            EDGE = {"section", "title,b", "2", "0", "", "<title></title><b id=\"child\">x</b>"},
            FF = {"section", "title,b", "2", "0", "", "<title></title><b id=\"child\">x</b>"},
            FF_ESR = {"section", "title,b", "2", "0", "", "<title></title><b id=\"child\">x</b>"})
    public void title_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("title", XML_TITLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "xmp,b", "1", "0", "", "<xmp xmlns=\"http://www.w3.org/1999/xhtml\"></xmp><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "xmp,b", "1", "0", "", "<xmp></xmp><b id=\"child\">x</b>"},
            EDGE = {"section", "xmp,b", "1", "0", "", "<xmp></xmp><b id=\"child\">x</b>"},
            FF = {"section", "xmp,b", "1", "0", "", "<xmp></xmp><b id=\"child\">x</b>"},
            FF_ESR = {"section", "xmp,b", "1", "0", "", "<xmp></xmp><b id=\"child\">x</b>"})
    public void xmp_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("xmp", XML_XMP);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "noembed,b", "1", "0", "", "<noembed xmlns=\"http://www.w3.org/1999/xhtml\"></noembed><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "noembed,b", "1", "0", "", "<noembed></noembed><b id=\"child\">x</b>"},
            EDGE = {"section", "noembed,b", "1", "0", "", "<noembed></noembed><b id=\"child\">x</b>"},
            FF = {"section", "noembed,b", "1", "0", "", "<noembed></noembed><b id=\"child\">x</b>"},
            FF_ESR = {"section", "noembed,b", "1", "0", "", "<noembed></noembed><b id=\"child\">x</b>"})
    public void noembed_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("noembed", XML_NOEMBED);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "noframes,b", "1", "0", "", "<noframes xmlns=\"http://www.w3.org/1999/xhtml\"></noframes><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "noframes,b", "1", "0", "", "<noframes></noframes><b id=\"child\">x</b>"},
            EDGE = {"section", "noframes,b", "1", "0", "", "<noframes></noframes><b id=\"child\">x</b>"},
            FF = {"section", "noframes,b", "1", "0", "", "<noframes></noframes><b id=\"child\">x</b>"},
            FF_ESR = {"section", "noframes,b", "1", "0", "", "<noframes></noframes><b id=\"child\">x</b>"})
    public void noframes_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("noframes", XML_NOFRAMES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "noscript,b", "1", "0", "", "<noscript xmlns=\"http://www.w3.org/1999/xhtml\"></noscript><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "noscript,b", "1", "0", "", "<noscript></noscript><b id=\"child\">x</b>"},
            EDGE = {"section", "noscript,b", "1", "0", "", "<noscript></noscript><b id=\"child\">x</b>"},
            FF = {"section", "noscript,b", "1", "0", "", "<noscript></noscript><b id=\"child\">x</b>"},
            FF_ESR = {"section", "noscript,b", "1", "0", "", "<noscript></noscript><b id=\"child\">x</b>"})
    public void noscript_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("noscript", XML_NOSCRIPT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "plaintext,b", "1", "0", "", "<plaintext xmlns=\"http://www.w3.org/1999/xhtml\"></plaintext><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "plaintext,b", "1", "0", "", "<plaintext></plaintext><b id=\"child\">x</b>"},
            EDGE = {"section", "plaintext,b", "1", "0", "", "<plaintext></plaintext><b id=\"child\">x</b>"},
            FF = {"section", "plaintext,b", "1", "0", "", "<plaintext></plaintext><b id=\"child\">x</b>"},
            FF_ESR = {"section", "plaintext,b", "1", "0", "", "<plaintext></plaintext><b id=\"child\">x</b>"})
    public void plaintext_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("plaintext", XML_PLAINTEXT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "svg,b", "1", "0", "", "<svg xmlns=\"http://www.w3.org/1999/xhtml\"></svg><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "svg,b", "1", "0", "", "<svg></svg><b id=\"child\">x</b>"},
            EDGE = {"section", "svg,b", "1", "0", "", "<svg></svg><b id=\"child\">x</b>"},
            FF = {"section", "svg,b", "1", "0", "", "<svg></svg><b id=\"child\">x</b>"},
            FF_ESR = {"section", "svg,b", "1", "0", "", "<svg></svg><b id=\"child\">x</b>"})
    public void svg_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("svg", XML_SVG);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "math,b", "1", "0", "", "<math xmlns=\"http://www.w3.org/1999/xhtml\"></math><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "math,b", "1", "0", "", "<math></math><b id=\"child\">x</b>"},
            EDGE = {"section", "math,b", "1", "0", "", "<math></math><b id=\"child\">x</b>"},
            FF = {"section", "math,b", "1", "0", "", "<math></math><b id=\"child\">x</b>"},
            FF_ESR = {"section", "math,b", "1", "0", "", "<math></math><b id=\"child\">x</b>"})
    public void math_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("math", XML_MATH);
    }


    // ------------------------------------------------------------------ helpers

    /**
     * Builds the js that logs what the parser made of the markup.
     *
     * @param tag the tag name of the element under test
     * @param root js expression for the container element
     * @return the js code
     */
    private static String probe(final String tag, final String root) {
        return "var root = " + root + ";\n"
            + "var c = document.getElementById('child');\n"
            + "log(c ? c.parentNode.nodeName.toLowerCase() : 'none');\n"
            + "var names = [];\n"
            + "for (var i = 0; i < root.childNodes.length; i++) {\n"
            + "  var n = root.childNodes[i];\n"
            + "  if (n.nodeType != 3 || /\\S/.test(n.nodeValue)) { names.push(n.nodeName.toLowerCase()); }\n"
            + "}\n"
            + "log(names.join(','));\n"
            + "var els = document.getElementsByTagName('" + tag + "');\n"
            + "var el = els.length > 0 ? els[els.length - 1] : null;\n"
            + "log(els.length);\n"
            + "log(el ? el.childNodes.length : '-');\n"
            + "log(el ? el.textContent.replace(/\\n/g, '\\\\n') : '-');\n"
            + "log(root.innerHTML.replace(/\\n/g, '\\\\n'));\n";
    }

    /** Probe markup is part of the page source (HTML parser). */
    private void htmlPage(final String tag, final String markup) throws Exception {
        // the page needs its own title: a title element in the body must not become document.title
        final String html = "<html><head><title></title>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + probe(tag, "document.body")
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + markup
            + "\n</body></html>";

        loadPageVerifyTitle2(html);
    }

    /** Probe markup is part of the page source (XML parser). */
    private void xhtmlPage(final String tag, final String markup) throws Exception {
        loadXhtml(xhtmlTemplate(probe(tag, "document.body"), markup));
    }

    /** Probe markup is set via innerHTML in an HTML document (HTML fragment parser). */
    private void innerHtmlInHtmlPage(final String tag, final String markup) throws Exception {
        // '</script>' inside the page's own script block would end that block, so escape the slash
        final String js = markup.replace("\n", "\\n").replace("</script>", "<\\/script>");
        final String html = "<html><head><title></title>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    document.getElementById('host').innerHTML = '" + js + "';\n"
            + probe(tag, "document.getElementById('host')")
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<section id='host'></section>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /** Probe markup is set via innerHTML in an XHTML document (XML fragment parser). */
    private void innerHtmlInXhtmlPage(final String tag, final String markup) throws Exception {
        final String test =
                "document.getElementById('host').innerHTML = '" + markup + "';\n"
                + probe(tag, "document.getElementById('host')");
        loadXhtml(xhtmlTemplate(test, "<section id=\"host\"></section>"));
    }

    private static String xhtmlTemplate(final String testBody, final String bodyMarkup) {
        return "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n"
            + "<head><title></title>\n"
            + "<script>//<![CDATA[\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + testBody
            + "  }\n"
            + "//]]></script></head>\n"
            + "<body onload=\"test()\">\n"
            + bodyMarkup
            + "\n</body></html>";
    }

    private void loadXhtml(final String xhtml) throws Exception {
        final WebDriver driver = loadPage2(xhtml, URL_FIRST, "application/xhtml+xml", StandardCharsets.UTF_8);
        verifyTitle2(driver, getExpectedAlerts());
    }
}