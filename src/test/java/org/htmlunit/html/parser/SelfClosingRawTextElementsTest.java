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
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests how a trailing slash is handled on the other special elements, see {@link SelfClosingTagsTest}
 * for {@code div}, {@code iframe} and {@code script}.
 *
 * <p>Elements whose content the HTML parser treats as text, so the slash must be ignored there:
 * <ul>
 *   <li>raw text: {@code style}, {@code xmp}, {@code noembed}, {@code noframes},
 *       {@code noscript} (with scripting enabled)</li>
 *   <li>escapable raw text: {@code textarea}, {@code title}</li>
 *   <li>{@code plaintext}: no end tag, everything behind it is text</li>
 * </ul>
 * Foreign content ({@code svg}, {@code math}) is the opposite: the HTML parser honors the slash, so these
 * two are regression controls against a rule like "never honor the slash in HTML mode".
 *
 * <p>Each test puts a probe element {@code <b id='child'>} behind the element under test and logs the node
 * name of the probe's parent, or {@code none} if the probe is not part of the DOM (it was swallowed as text).
 * The XML parser (XHTML page, innerHTML of an XHTML document) always honors the slash.
 *
 * @author Ronald Brill
 */
public class SelfClosingRawTextElementsTest extends WebDriverTestCase {

    private static final String XHTML_CONTENT_TYPE = "application/xhtml+xml";

    /** Logs the parent of the probe element. */
    private static final String PROBE =
              "var c = document.getElementById('child');\n"
            + "log(c ? c.parentNode.nodeName.toLowerCase() : 'none');\n";

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
    @Alerts("none")
    public void style_htmlPage() throws Exception {
        htmlPage(HTML_STYLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void textarea_htmlPage() throws Exception {
        htmlPage(HTML_TEXTAREA);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void title_htmlPage() throws Exception {
        htmlPage(HTML_TITLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void xmp_htmlPage() throws Exception {
        htmlPage(HTML_XMP);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void noembed_htmlPage() throws Exception {
        htmlPage(HTML_NOEMBED);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void noframes_htmlPage() throws Exception {
        htmlPage(HTML_NOFRAMES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void noscript_htmlPage() throws Exception {
        htmlPage(HTML_NOSCRIPT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void plaintext_htmlPage() throws Exception {
        htmlPage(HTML_PLAINTEXT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void svg_htmlPage() throws Exception {
        htmlPage(HTML_SVG);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void math_htmlPage() throws Exception {
        htmlPage(HTML_MATH);
    }

    // ---------- XHTML page --------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void style_xhtmlPage() throws Exception {
        xhtmlPage(XML_STYLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void textarea_xhtmlPage() throws Exception {
        xhtmlPage(XML_TEXTAREA);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void title_xhtmlPage() throws Exception {
        xhtmlPage(XML_TITLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void xmp_xhtmlPage() throws Exception {
        xhtmlPage(XML_XMP);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void noembed_xhtmlPage() throws Exception {
        xhtmlPage(XML_NOEMBED);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void noframes_xhtmlPage() throws Exception {
        xhtmlPage(XML_NOFRAMES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void noscript_xhtmlPage() throws Exception {
        xhtmlPage(XML_NOSCRIPT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void plaintext_xhtmlPage() throws Exception {
        xhtmlPage(XML_PLAINTEXT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void svg_xhtmlPage() throws Exception {
        xhtmlPage(XML_SVG);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void math_xhtmlPage() throws Exception {
        xhtmlPage(XML_MATH);
    }

    // ---------- innerHTML, HTML document ------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void style_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_STYLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void textarea_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_TEXTAREA);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void title_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_TITLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void xmp_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_XMP);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void noembed_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_NOEMBED);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void noframes_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_NOFRAMES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void noscript_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_NOSCRIPT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void plaintext_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_PLAINTEXT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void svg_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_SVG);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void math_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_MATH);
    }

    // ---------- innerHTML, XHTML document -----------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void style_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_STYLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void textarea_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_TEXTAREA);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void title_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_TITLE);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void xmp_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_XMP);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void noembed_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_NOEMBED);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void noframes_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_NOFRAMES);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void noscript_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_NOSCRIPT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void plaintext_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_PLAINTEXT);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void svg_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_SVG);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void math_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_MATH);
    }

    // ------------------------------------------------------------------ helpers

    /** Probe markup is part of the page source (HTML parser). */
    private void htmlPage(final String markup) throws Exception {
        // the page needs its own title: a title element in the body must not become document.title
        final String html = "<html><head><title></title>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + PROBE
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + markup
            + "\n</body></html>";

        loadPageVerifyTitle2(html);
    }

    /** Probe markup is part of the page source (XML parser). */
    private void xhtmlPage(final String markup) throws Exception {
        loadXhtml(xhtmlTemplate(PROBE, markup));
    }

    /** Probe markup is set via innerHTML in an HTML document (HTML fragment parser). */
    private void innerHtmlInHtmlPage(final String markup) throws Exception {
        final String html = "<html><head><title></title>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    document.getElementById('host').innerHTML = '" + markup + "';\n"
            + PROBE
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<section id='host'></section>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /** Probe markup is set via innerHTML in an XHTML document (XML fragment parser). */
    private void innerHtmlInXhtmlPage(final String markup) throws Exception {
        final String test =
                "document.getElementById('host').innerHTML = '" + markup + "';\n"
                + PROBE;
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
        final WebDriver driver = loadPage2(xhtml, URL_FIRST, XHTML_CONTENT_TYPE, StandardCharsets.UTF_8);
        verifyTitle2(driver, getExpectedAlerts());
    }
}