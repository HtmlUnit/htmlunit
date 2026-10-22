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
 * Tests how a trailing slash on non-void elements ({@code <div/>}, {@code <iframe/>}, {@code <script/>})
 * is handled in the different parsing contexts.
 *
 * <p>Matrix: element (div as control, iframe, script) x context
 * (HTML page, XHTML page, innerHTML in an HTML document, innerHTML in an XHTML document).
 * Real browsers: the HTML parser ignores the slash on all three elements, the XML parser always honors it.
 * So the first log line is {@code div} / {@code none} / {@code none} for the HTML contexts and
 * {@code body} (page) or {@code section} (innerHTML) for the XHTML contexts. *
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
public class SelfClosingTagsTest extends WebDriverTestCase {

    // probes for the HTML parser; the closing tag bounds the text of the raw text elements
    private static final String HTML_DIV = "<div/><b id=\"child\">x</b></div>";
    private static final String HTML_IFRAME = "<iframe/><b id=\"child\">x</b></iframe>";
    private static final String HTML_SCRIPT = "<script/>//<b id=\"child\">x</b>\n</script>";

    // probes for the XML parser; a stray closing tag would not be well-formed
    private static final String XML_DIV = "<div/><b id=\"child\">x</b>";
    private static final String XML_IFRAME = "<iframe/><b id=\"child\">x</b>";
    private static final String XML_SCRIPT = "<script/>//<b id=\"child\">x</b>";

    // ---------- HTML page ---------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"div", "div", "1", "1", "x", "\\n<div><b id=\"child\">x</b></div>\\n"})
    public void div_htmlPage() throws Exception {
        htmlPage("div", HTML_DIV);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "iframe", "1", "1", "<b id=\"child\">x</b>", "\\n<iframe><b id=\"child\">x</b></iframe>\\n"})
    public void iframe_htmlPage() throws Exception {
        htmlPage("iframe", HTML_IFRAME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "script", "2", "1", "//<b id=\"child\">x</b>\\n", "\\n<script>//<b id=\"child\">x</b>\\n</script>\\n"})
    public void script_htmlPage() throws Exception {
        htmlPage("script", HTML_SCRIPT);
    }

    // ---------- XHTML page --------------------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "div,b", "1", "0", "", "\\n<div xmlns=\"http://www.w3.org/1999/xhtml\"></div><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "div,b", "1", "0", "", "\\n<div></div><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "div,b", "1", "0", "", "\\n<div></div><b id=\"child\">x</b>\\n"},
            FF = {"body", "div,b", "1", "0", "", "\\n<div></div><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "div,b", "1", "0", "", "\\n<div></div><b id=\"child\">x</b>\\n"})
    public void div_xhtmlPage() throws Exception {
        xhtmlPage("div", XML_DIV);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "iframe,b", "1", "0", "", "\\n<iframe xmlns=\"http://www.w3.org/1999/xhtml\"></iframe><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "iframe,b", "1", "0", "", "\\n<iframe></iframe><b id=\"child\">x</b>\\n"},
            EDGE = {"body", "iframe,b", "1", "0", "", "\\n<iframe></iframe><b id=\"child\">x</b>\\n"},
            FF = {"body", "iframe,b", "1", "0", "", "\\n<iframe></iframe><b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "iframe,b", "1", "0", "", "\\n<iframe></iframe><b id=\"child\">x</b>\\n"})
    public void iframe_xhtmlPage() throws Exception {
        xhtmlPage("iframe", XML_IFRAME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"body", "script,#text,b", "2", "0", "", "\\n<script xmlns=\"http://www.w3.org/1999/xhtml\"></script>//<b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>\\n"})
    @HtmlUnitNYI(
            CHROME = {"body", "script,#text,b", "2", "0", "", "\\n<script></script>//<b id=\"child\">x</b>\\n"},
            EDGE = {"body", "script,#text,b", "2", "0", "", "\\n<script></script>//<b id=\"child\">x</b>\\n"},
            FF = {"body", "script,#text,b", "2", "0", "", "\\n<script></script>//<b id=\"child\">x</b>\\n"},
            FF_ESR = {"body", "script,#text,b", "2", "0", "", "\\n<script></script>//<b id=\"child\">x</b>\\n"})
    public void script_xhtmlPage() throws Exception {
        xhtmlPage("script", XML_SCRIPT);
    }

    // ---------- innerHTML, HTML document ------------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"div", "div", "1", "1", "x", "<div><b id=\"child\">x</b></div>"})
    public void div_innerHtml() throws Exception {
        innerHtmlInHtmlPage("div", HTML_DIV);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "iframe", "1", "1", "<b id=\"child\">x</b>", "<iframe><b id=\"child\">x</b></iframe>"})
    public void iframe_innerHtml() throws Exception {
        innerHtmlInHtmlPage("iframe", HTML_IFRAME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"none", "script", "2", "1", "//<b id=\"child\">x</b>\\n", "<script>//<b id=\"child\">x</b>\\n</script>"})
    public void script_innerHtml() throws Exception {
        innerHtmlInHtmlPage("script", HTML_SCRIPT);
    }

    // ---------- innerHTML, XHTML document -----------------------------------

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "div,b", "1", "0", "", "<div xmlns=\"http://www.w3.org/1999/xhtml\"></div><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "div,b", "1", "0", "", "<div></div><b id=\"child\">x</b>"},
            EDGE = {"section", "div,b", "1", "0", "", "<div></div><b id=\"child\">x</b>"},
            FF = {"section", "div,b", "1", "0", "", "<div></div><b id=\"child\">x</b>"},
            FF_ESR = {"section", "div,b", "1", "0", "", "<div></div><b id=\"child\">x</b>"})
    public void div_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("div", XML_DIV);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "iframe,b", "1", "0", "", "<iframe xmlns=\"http://www.w3.org/1999/xhtml\"></iframe><b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "iframe,b", "1", "0", "", "<iframe></iframe><b id=\"child\">x</b>"},
            EDGE = {"section", "iframe,b", "1", "0", "", "<iframe></iframe><b id=\"child\">x</b>"},
            FF = {"section", "iframe,b", "1", "0", "", "<iframe></iframe><b id=\"child\">x</b>"},
            FF_ESR = {"section", "iframe,b", "1", "0", "", "<iframe></iframe><b id=\"child\">x</b>"})
    public void iframe_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("iframe", XML_IFRAME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"section", "script,#text,b", "2", "0", "", "<script xmlns=\"http://www.w3.org/1999/xhtml\"></script>//<b xmlns=\"http://www.w3.org/1999/xhtml\" id=\"child\">x</b>"})
    @HtmlUnitNYI(
            CHROME = {"section", "script,#text,b", "2", "0", "", "<script></script>//<b id=\"child\">x</b>"},
            EDGE = {"section", "script,#text,b", "2", "0", "", "<script></script>//<b id=\"child\">x</b>"},
            FF = {"section", "script,#text,b", "2", "0", "", "<script></script>//<b id=\"child\">x</b>"},
            FF_ESR = {"section", "script,#text,b", "2", "0", "", "<script></script>//<b id=\"child\">x</b>"})
    public void script_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage("script", XML_SCRIPT);
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
