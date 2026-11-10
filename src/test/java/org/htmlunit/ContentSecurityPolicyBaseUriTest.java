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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directive base-uri.
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and collects all securitypolicyviolation events. The violations are logged at the end
 * (100ms after the load event) because the browsers fire the violation events async.
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * The function base() logs document.baseURI and the resolved href of the anchor 'a'.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyBaseUriTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

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
            + "function base() {\n"
            + "  log(document.baseURI);\n"
            + "  log(document.getElementById('a').href);\n"
            + "}\n";

    private static final String ANCHOR = "<a id='a' href='page.html'>link</a>\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy the base element is used.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://localhost:§§PORT§§/base/", "http://localhost:§§PORT§§/base/page.html"})
    public void noPolicy() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(null, baseElement(URL_CROSS_ORIGIN_BASE + "base/"), "base();\n");
    }

    /**
     * Same origin base url, base-uri 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§/sub/", "§§URL§§/sub/page.html"})
    public void self_sameOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("base-uri 'self'", baseElement("/sub/"), "base();\n");
    }

    /**
     * Cross origin base url, base-uri 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"§§URL§§/",
                       "§§URL§§/page.html",
                       "violation base-uri http://localhost:§§PORT§§/base/ enforce"})
    public void self_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("base-uri 'self'", baseElement(URL_CROSS_ORIGIN_BASE + "base/"), "base();\n");
    }

    /**
     * The directive base-uri 'none' blocks all base urls.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§/",
             "§§URL§§/page.html",
             "violation base-uri §§URL§§/sub/ enforce"})
    public void none() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("base-uri 'none'", baseElement("/sub/"), "base();\n");
    }

    /**
     * Cross origin base url, the origin is listed in base-uri.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://localhost:§§PORT§§/base/",
             "http://localhost:§§PORT§§/base/page.html"})
    public void hostSource() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("base-uri 'self' http://localhost:" + PORT2,
                baseElement(URL_CROSS_ORIGIN_BASE + "base/"), "base();\n");
    }

    /**
     * Base-uri does not fall back to default-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://localhost:§§PORT§§/base/",
             "http://localhost:§§PORT§§/base/page.html"})
    public void defaultSrcIsNoFallback() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("default-src 'self'", baseElement(URL_CROSS_ORIGIN_BASE + "base/"), "base();\n");
    }

    /**
     * Report only policies do not block the base url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"http://localhost:§§PORT§§/base/",
             "http://localhost:§§PORT§§/base/page.html",
             "violation base-uri http://localhost:§§PORT§§/base/ report"})
    public void reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HEADER_REPORT_ONLY, "base-uri 'self'",
                baseElement(URL_CROSS_ORIGIN_BASE + "base/"), "base();\n");
    }

    /**
     * The policy can be delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§/",
             "§§URL§§/page.html",
             "violation base-uri http://localhost:§§PORT§§/base/ enforce"})
    public void meta() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"base-uri 'self'\">\n"
                + baseElement(URL_CROSS_ORIGIN_BASE + "base/"), "base();\n");
    }

    /**
     * Base element created by script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§/",
             "§§URL§§/page.html",
             "violation base-uri http://localhost:§§PORT§§/base/ enforce"})
    public void dynamic() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("base-uri 'self'", "",
                "var b = document.createElement('base');\n"
                + "b.href = '" + URL_CROSS_ORIGIN_BASE + "base/';\n"
                + "document.head.appendChild(b);\n"
                + "base();\n");
    }

    /**
     * The href of an allowed base element is changed to a blocked url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§/sub/",
             "§§URL§§/sub/page.html",
             "§§URL§§/",
             "§§URL§§/page.html",
             "violation base-uri http://localhost:§§PORT§§/base/ enforce"})
    public void hrefChanged() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("base-uri 'self'", "<base id='b' href='/sub/'>\n",
                "base();\n"
                + "document.getElementById('b').href = '" + URL_CROSS_ORIGIN_BASE + "base/';\n"
                + "base();\n");
    }

    /**
     * Two base elements; only the first one is used. Chrome checks the first base element
     * again when the second one is added (two violations), Firefox only once; HtmlUnit follows Chrome.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"§§URL§§/",
                       "§§URL§§/page.html",
                       "violation base-uri http://localhost:§§PORT§§/base/ enforce",
                       "violation base-uri http://localhost:§§PORT§§/base/ enforce"},
            FF = {"§§URL§§/",
                  "§§URL§§/page.html",
                  "violation base-uri http://localhost:§§PORT§§/base/ enforce"},
            FF_ESR = {"§§URL§§/",
                      "§§URL§§/page.html",
                      "violation base-uri http://localhost:§§PORT§§/base/ enforce"})
    @HtmlUnitNYI(FF = {"§§URL§§/",
                       "§§URL§§/page.html",
                       "violation base-uri http://localhost:§§PORT§§/base/ enforce",
                       "violation base-uri http://localhost:§§PORT§§/base/ enforce"},
            FF_ESR = {"§§URL§§/",
                      "§§URL§§/page.html",
                      "violation base-uri http://localhost:§§PORT§§/base/ enforce",
                      "violation base-uri http://localhost:§§PORT§§/base/ enforce"})
    public void twoBaseElements() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("base-uri 'self'",
                baseElement(URL_CROSS_ORIGIN_BASE + "base/") + baseElement("/sub/"), "base();\n");
    }

    private static String baseElement(final String href) {
        return "<base href='" + href + "'>\n";
    }

    private void loadPageVerify(final String policy, final String head, final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, head, testJs);
    }

    /**
     * Loads a page containing the harness script, the given head, an anchor and the test script;
     * the page is delivered with the given policy header.
     */
    private void loadPageVerify(final String header, final String policy,
            final String head, final String testJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script src='" + URL_FIRST + "harness.js'></script>\n"
            + head
            + "</head>\n"
            + "<body>\n"
            + ANCHOR
            + "<script src='" + URL_FIRST + "test.js'></script>\n"
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
