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
package org.htmlunit;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directives style-src, style-src-elem and style-src-attr.
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and collects all securitypolicyviolation events. The violations are logged at the end
 * (100ms after the load event) because the browsers fire the violation events async and
 * the order relative to the other logs is not stable. The test code is placed in the external
 * script 'test.js'. All policies allow these scripts.
 * The function color() logs the computed color of the div 'd'
 * (rgb(255, 0, 0) if the style was applied, rgb(0, 0, 0) otherwise).
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyStyleTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private static final String RED_CSS = "#d { color: rgb(255, 0, 0); }";
    private static final String DIV = "<div id='d'>text</div>\n";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "var violations = [];\n"
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  violations.push('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n"
            + "window.addEventListener('load', function() {\n"
            + "  setTimeout(function() {\n"
            + "    for (var i = 0; i < violations.length; i++) { log(violations[i]); }\n"
            + "  }, 100);\n"
            + "});\n"
            + "function color() {\n"
            + "  log(getComputedStyle(document.getElementById('d')).color);\n"
            + "}\n"
            + "function addLink(href, nonce) {\n"
            + "  var link = document.createElement('link');\n"
            + "  link.rel = 'stylesheet';\n"
            + "  if (nonce) { link.setAttribute('nonce', nonce); }\n"
            + "  link.onload = function() { log('load'); setTimeout(color, 0); };\n"
            + "  link.onerror = function() { log('error'); setTimeout(color, 0); };\n"
            + "  link.href = href;\n"
            + "  document.head.appendChild(link);\n"
            + "}\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    // style element

    /**
     * Without a policy the style element is applied.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void style_noPolicy() throws Exception {
        loadPageVerify(null, "<style>" + RED_CSS + "</style>\n", DIV, "color();\n");
    }

    /**
     * Inline style blocked by style-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-elem inline enforce"})
    public void style_self() throws Exception {
        loadPageVerify("style-src 'self'", "<style>" + RED_CSS + "</style>\n", DIV, "color();\n");
    }

    /**
     * Inline style allowed by 'unsafe-inline'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void style_unsafeInline() throws Exception {
        loadPageVerify("style-src 'self' 'unsafe-inline'", "<style>" + RED_CSS + "</style>\n", DIV, "color();\n");
    }

    /**
     * Inline style allowed by a nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void style_nonce() throws Exception {
        loadPageVerify("style-src 'nonce-abc123'", "<style nonce='abc123'>" + RED_CSS + "</style>\n",
                DIV, "color();\n");
    }

    /**
     * Inline style with the wrong nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-elem inline enforce"})
    public void style_wrongNonce() throws Exception {
        loadPageVerify("style-src 'nonce-abc123'", "<style nonce='xyz'>" + RED_CSS + "</style>\n",
                DIV, "color();\n");
    }

    /**
     * Inline style allowed by a hash.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void style_hash() throws Exception {
        loadPageVerify("style-src '" + sha256(RED_CSS) + "'", "<style>" + RED_CSS + "</style>\n",
                DIV, "color();\n");
    }

    /**
     * Style-src-elem wins over style-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void style_styleSrcElem() throws Exception {
        loadPageVerify("style-src 'none'; style-src-elem 'unsafe-inline'", "<style>" + RED_CSS + "</style>\n",
                DIV, "color();\n");
    }

    /**
     * Default-src is the fallback for style-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-elem inline enforce"})
    public void style_defaultSrc() throws Exception {
        loadPageVerify("default-src 'self'", "<style>" + RED_CSS + "</style>\n", DIV, "color();\n");
    }

    /**
     * Report only policies do not block the style.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 0, 0)", "violation style-src-elem inline report"})
    public void style_reportOnly() throws Exception {
        loadPageVerify(HEADER_REPORT_ONLY, "style-src 'self'", "<style>" + RED_CSS + "</style>\n",
                DIV, "color();\n");
    }

    /**
     * The policy can be delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-elem inline enforce"})
    public void style_meta() throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"style-src 'self'\">\n"
                + "<style>" + RED_CSS + "</style>\n", DIV, "color();\n");
    }

    /**
     * Style element created by script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-elem inline enforce"})
    public void style_dynamic() throws Exception {
        loadPageVerify("style-src 'self'", "", DIV,
                "var style = document.createElement('style');\n"
                + "style.textContent = '" + RED_CSS + "';\n"
                + "document.head.appendChild(style);\n"
                + "color();\n");
    }

    /**
     * The sample of the violation ('report-sample').
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"sample #d { color: rgb(255, 0, 0); } 6", "violation style-src-elem inline enforce"})
    public void style_reportSample() throws Exception {
        loadPageVerify("style-src 'self' 'report-sample'", "", DIV,
                "document.addEventListener('securitypolicyviolation', function(e) {\n"
                + "  log('sample ' + e.sample + ' ' + e.lineNumber);\n"
                + "});\n"
                + "var style = document.createElement('style');\n"
                + "style.textContent = '" + RED_CSS + "';\n"
                + "document.head.appendChild(style);\n");
    }

    // style attribute

    /**
     * Without a policy the style attribute is applied.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void attr_noPolicy() throws Exception {
        loadPageVerify(null, "", "<div id='d' style='color: red'>text</div>\n", "color();\n");
    }

    /**
     * Style attribute blocked by style-src 'self'; the attribute is still part of the dom
     * (Firefox returns an empty value, HtmlUnit follows Chrome).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"rgb(0, 0, 0)", "color: red", "[]", "violation style-src-attr inline enforce"},
            FF = {"rgb(0, 0, 0)", "", "[]", "violation style-src-attr inline enforce"},
            FF_ESR = {"rgb(0, 0, 0)", "", "[]", "violation style-src-attr inline enforce"})
    @HtmlUnitNYI(FF = {"rgb(0, 0, 0)", "color: red", "[]", "violation style-src-attr inline enforce"},
            FF_ESR = {"rgb(0, 0, 0)", "color: red", "[]", "violation style-src-attr inline enforce"})
    public void attr_self() throws Exception {
        loadPageVerify("style-src 'self'", "", "<div id='d' style='color: red'>text</div>\n",
                "color();\n"
                + "var d = document.getElementById('d');\n"
                + "log(d.getAttribute('style'));\n"
                + "log('[' + d.style.color + ']');\n");
    }

    /**
     * Style attribute allowed by 'unsafe-inline'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void attr_unsafeInline() throws Exception {
        loadPageVerify("style-src 'self' 'unsafe-inline'", "", "<div id='d' style='color: red'>text</div>\n",
                "color();\n");
    }

    /**
     * Style-src-attr wins over style-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void attr_styleSrcAttr() throws Exception {
        loadPageVerify("style-src 'self'; style-src-attr 'unsafe-inline'", "",
                "<div id='d' style='color: red'>text</div>\n", "color();\n");
    }

    /**
     * Style-src-elem does not allow style attributes.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-attr inline enforce"})
    public void attr_styleSrcElem() throws Exception {
        loadPageVerify("style-src 'self'; style-src-elem 'unsafe-inline'", "",
                "<div id='d' style='color: red'>text</div>\n", "color();\n");
    }

    /**
     * Style attribute allowed by a hash and 'unsafe-hashes'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void attr_unsafeHashes() throws Exception {
        loadPageVerify("style-src 'unsafe-hashes' '" + sha256("color: red") + "'", "",
                "<div id='d' style='color: red'>text</div>\n", "color();\n");
    }

    /**
     * Style attribute and a hash without 'unsafe-hashes'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-attr inline enforce"})
    public void attr_hashWithoutUnsafeHashes() throws Exception {
        loadPageVerify("style-src '" + sha256("color: red") + "'", "",
                "<div id='d' style='color: red'>text</div>\n", "color();\n");
    }

    /**
     * Nonces are not used for style attributes.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-attr inline enforce"})
    public void attr_nonce() throws Exception {
        loadPageVerify("style-src 'nonce-abc123'", "",
                "<div id='d' nonce='abc123' style='color: red'>text</div>\n", "color();\n");
    }

    /**
     * Style attribute set by setAttribute().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-attr inline enforce"})
    public void attr_setAttribute() throws Exception {
        loadPageVerify("style-src 'self'", "", DIV,
                "document.getElementById('d').setAttribute('style', 'color: red');\n"
                + "color();\n");
    }

    /**
     * Style changes by the CSSOM are not checked.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(255, 0, 0)", "color: red;"})
    public void attr_cssom() throws Exception {
        loadPageVerify("style-src 'self'", "", DIV,
                "var d = document.getElementById('d');\n"
                + "d.style.color = 'red';\n"
                + "color();\n"
                + "log(d.getAttribute('style'));\n");
    }

    /**
     * Style changes by style.cssText are not checked.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void attr_cssText() throws Exception {
        loadPageVerify("style-src 'self'", "", DIV,
                "document.getElementById('d').style.cssText = 'color: red';\n"
                + "color();\n");
    }

    /**
     * CSSOM change of an element with a blocked style attribute.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "font-weight: bold;", "violation style-src-attr inline enforce"})
    public void attr_cssomAfterBlocked() throws Exception {
        loadPageVerify("style-src 'self'", "",
                "<div id='d' style='color: red; background-color: blue'>text</div>\n",
                "var d = document.getElementById('d');\n"
                + "d.style.fontWeight = 'bold';\n"
                + "color();\n"
                + "log(d.getAttribute('style'));\n");
    }

    /**
     * Style attribute of an element created by innerHTML.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-attr inline enforce"})
    public void attr_innerHTML() throws Exception {
        loadPageVerify("style-src 'self'", "", "<div id='container'></div>\n",
                "document.getElementById('container').innerHTML = "
                + "\"<div id='d' style='color: red'>text</div>\";\n"
                + "color();\n");
    }

    // external stylesheets

    /**
     * Same origin stylesheet allowed by 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "rgb(255, 0, 0)"})
    public void link_self() throws Exception {
        setCss(URL_FIRST, "style.css", RED_CSS);
        loadPageVerify("style-src 'self'", "", DIV, "addLink('style.css');\n");
    }

    /**
     * Cross origin stylesheet blocked by 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "rgb(0, 0, 0)", "violation style-src-elem §§URL§§/style.css enforce"})
    public void link_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCss(URL_CROSS_ORIGIN_BASE, "style.css", RED_CSS);
        loadPageVerify("style-src 'self'", "", DIV, "addLink('" + URL_CROSS_ORIGIN_BASE + "style.css');\n");
    }

    /**
     * Cross origin stylesheet allowed by a nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "rgb(255, 0, 0)"})
    public void link_nonce() throws Exception {
        setCss(URL_CROSS_ORIGIN_BASE, "style.css", RED_CSS);
        loadPageVerify("style-src 'self' 'nonce-abc123'", "", DIV,
                "addLink('" + URL_CROSS_ORIGIN_BASE + "style.css', 'abc123');\n");
    }

    /**
     * Stylesheet link in the html source (parser inserted).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-elem §§URL§§/style.css enforce"})
    public void link_parserInserted() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCss(URL_CROSS_ORIGIN_BASE, "style.css", RED_CSS);
        loadPageVerify("style-src 'self'",
                "<link rel='stylesheet' href='" + URL_CROSS_ORIGIN_BASE + "style.css'>\n", DIV,
                "window.addEventListener('load', color);\n");
    }

    /**
     * Report only policies do not block the stylesheet (Firefox reports the violation twice,
     * HtmlUnit follows Chrome).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"load", "rgb(255, 0, 0)", "violation style-src-elem §§URL§§/style.css report"},
            FF = {"load", "rgb(255, 0, 0)",
                  "violation style-src-elem §§URL§§/style.css report",
                  "violation style-src-elem §§URL§§/style.css report"},
            FF_ESR = {"load", "rgb(255, 0, 0)",
                      "violation style-src-elem §§URL§§/style.css report",
                      "violation style-src-elem §§URL§§/style.css report"})
    @HtmlUnitNYI(FF = {"load", "rgb(255, 0, 0)", "violation style-src-elem §§URL§§/style.css report"},
            FF_ESR = {"load", "rgb(255, 0, 0)", "violation style-src-elem §§URL§§/style.css report"})
    public void link_reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCss(URL_CROSS_ORIGIN_BASE, "style.css", RED_CSS);
        loadPageVerify(HEADER_REPORT_ONLY, "style-src 'self'", "", DIV,
                "addLink('" + URL_CROSS_ORIGIN_BASE + "style.css');\n");
    }

    /**
     * Cross origin @import blocked by 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rgb(0, 0, 0)", "violation style-src-elem §§URL§§/imported.css enforce"})
    public void import_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCss(URL_CROSS_ORIGIN_BASE, "imported.css", RED_CSS);
        loadPageVerify("style-src 'self' 'nonce-abc123'",
                "<style nonce='abc123'>@import url('" + URL_CROSS_ORIGIN_BASE + "imported.css');</style>\n",
                DIV, "window.addEventListener('load', color);\n");
    }

    /**
     * Same origin @import allowed by 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void import_self() throws Exception {
        setCss(URL_FIRST, "imported.css", RED_CSS);
        loadPageVerify("style-src 'self' 'nonce-abc123'",
                "<style nonce='abc123'>@import url('imported.css');</style>\n",
                DIV, "window.addEventListener('load', color);\n");
    }

    /**
     * @import inside an external stylesheet; the browsers fire the error event at the link if
     * the import is blocked. HtmlUnit fires the load event before the stylesheet is parsed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "rgb(0, 0, 0)", "violation style-src-elem §§URL§§/imported.css enforce"})
    @HtmlUnitNYI(CHROME = {"load", "rgb(0, 0, 0)", "violation style-src-elem §§URL§§/imported.css enforce"},
            EDGE = {"load", "rgb(0, 0, 0)", "violation style-src-elem §§URL§§/imported.css enforce"},
            FF = {"load", "rgb(0, 0, 0)", "violation style-src-elem §§URL§§/imported.css enforce"},
            FF_ESR = {"load", "rgb(0, 0, 0)", "violation style-src-elem §§URL§§/imported.css enforce"})
    public void import_fromLink() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setCss(URL_FIRST, "style.css", "@import url('" + URL_CROSS_ORIGIN_BASE + "imported.css');");
        setCss(URL_CROSS_ORIGIN_BASE, "imported.css", RED_CSS);
        loadPageVerify("style-src 'self'", "", DIV, "addLink('style.css');\n");
    }

    private void setCss(final URL base, final String path, final String css) throws Exception {
        getMockWebConnection().setResponse(new URL(base, path), css, MimeType.TEXT_CSS);
    }

    private static String sha256(final String source) throws Exception {
        final byte[] digest = MessageDigest.getInstance("SHA-256").digest(source.getBytes(UTF_8));
        return "sha256-" + Base64.getEncoder().encodeToString(digest);
    }

    private void loadPageVerify(final String policy, final String head, final String body,
            final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, head, body, testJs);
    }

    /**
     * Loads a page containing the harness script, the given head, the given body and the test script;
     * the page is delivered with the given policy header.
     */
    private void loadPageVerify(final String header, final String policy,
            final String head, final String body, final String testJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script src='harness.js'></script>\n"
            + head
            + "</head>\n"
            + "<body>\n"
            + body
            + "<script src='test.js'></script>\n"
            + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        if (policy != null) {
            headers.add(new NameValuePair(header, policy));
        }
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "harness.js"), HARNESS_JS,
                MimeType.TEXT_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"), testJs,
                MimeType.TEXT_JAVASCRIPT);

        final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
