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
 * Tests how a trailing slash on non-void elements ({@code <div/>}, {@code <iframe/>}, {@code <script/>})
 * is handled in the different parsing contexts.
 *
 * <p>Matrix: element (div as control, iframe, script) x context
 * (HTML page, XHTML page, innerHTML in an HTML document, innerHTML in an XHTML document).
 *
 * <p>Every test puts a probe element {@code <b id='child'>} right behind the element under test and logs the
 * node name of the probe's parent, or {@code none} if the probe is not part of the DOM:
 * <ul>
 *   <li>slash ignored, element open: for {@code div} the parent is the div; for the raw text elements
 *       {@code iframe} and {@code script} the probe is only text, so {@code none}</li>
 *   <li>slash honored, element closed: the parent is the surrounding element</li>
 * </ul>
 *
 * <p>The expectations are what real browsers do:
 * <ul>
 *   <li>HTML parser (page or {@code innerHTML} of an HTML document): the slash is ignored on all three
 *       elements.</li>
 *   <li>XML parser (XHTML page or {@code innerHTML} of an XHTML document): the slash is always honored.</li>
 * </ul>
 *
 * @author Ronald Brill
 */
public class SelfClosingTagsTest extends WebDriverTestCase {

    private static final String XHTML_CONTENT_TYPE = "application/xhtml+xml";

    /** Logs the parent of the probe element. */
    private static final String PROBE =
              "var c = document.getElementById('child');\n"
            + "log(c ? c.parentNode.nodeName.toLowerCase() : 'none');\n";

    // probes for the HTML parser; the closing tag is needed to bound the raw text of iframe and script.
    // The script content is a valid js comment, so the script does not break if it is executed.
    private static final String HTML_DIV = "<div/><b id=\"child\">x</b></div>";
    private static final String HTML_IFRAME = "<iframe/><b id=\"child\">x</b></iframe>";
    private static final String HTML_SCRIPT = "<script/>//<b id=\"child\">x</b>\n</script>";

    // probes for the XML parser; a stray closing tag would not be well-formed
    private static final String XML_DIV = "<div/><b id=\"child\">x</b>";
    private static final String XML_IFRAME = "<iframe/><b id=\"child\">x</b>";
    private static final String XML_SCRIPT = "<script/>//<b id=\"child\">x</b>";

    // ---------------------------------------------------------------- HTML page

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("div")
    public void div_htmlPage() throws Exception {
        htmlPage(HTML_DIV);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void iframe_htmlPage() throws Exception {
        htmlPage(HTML_IFRAME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void script_htmlPage() throws Exception {
        htmlPage(HTML_SCRIPT);
    }

    // --------------------------------------------------------------- XHTML page

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void div_xhtmlPage() throws Exception {
        xhtmlPage(XML_DIV);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void iframe_xhtmlPage() throws Exception {
        xhtmlPage(XML_IFRAME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("body")
    public void script_xhtmlPage() throws Exception {
        xhtmlPage(XML_SCRIPT);
    }

    // ------------------------------------------------- innerHTML, HTML document

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("div")
    public void div_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_DIV);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void iframe_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_IFRAME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("none")
    public void script_innerHtml() throws Exception {
        innerHtmlInHtmlPage(HTML_SCRIPT);
    }

    // ------------------------------------------------ innerHTML, XHTML document

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void div_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_DIV);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void iframe_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_IFRAME);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("section")
    public void script_innerHtmlXhtml() throws Exception {
        innerHtmlInXhtmlPage(XML_SCRIPT);
    }

    // ------------------------------------------------------------------ helpers

    /** Probe markup is part of the page source (HTML parser). */
    private void htmlPage(final String markup) throws Exception {
        final String html = "<html><head>\n"
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
        final String xhtml = xhtmlTemplate(PROBE, markup);
        loadXhtml(xhtml);
    }

    /** Probe markup is set via innerHTML in an HTML document (HTML fragment parser). */
    private void innerHtmlInHtmlPage(final String markup) throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            // '</script>' inside the page's own script block would end that block, so escape the slash
            + "    document.getElementById('host').innerHTML = '"
                        + markup.replace("\n", "\\n").replace("</script>", "<\\/script>") + "';\n"
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
        final String xhtml = xhtmlTemplate(test, "<section id=\"host\"></section>");
        loadXhtml(xhtml);
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