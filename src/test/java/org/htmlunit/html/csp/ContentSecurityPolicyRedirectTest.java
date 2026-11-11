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
package org.htmlunit.html.csp;

import static java.nio.charset.StandardCharsets.ISO_8859_1;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for redirects of stylesheets, imports, scripts and images; the redirect targets are checked
 * against the Content-Security-Policy and upgraded by upgrade-insecure-requests.
 * <p>
 * The resources 'redirect.xxx' are redirecting (302) to the given target.
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and collects all events and securitypolicyviolation events. The events (sorted) and the violations
 * are logged at the end (200ms after the load event) because the browsers fire them async.
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * The upgrade tests are using host1.htmlunit-dev.org, this requires a special setup in
 * the name resolution (see WebTestCase#HOSTS_DOMAIN).
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyRedirectTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";
    private static final String HTTP_HOST1 = "http://" + HOST1;
    private static final String RED_CSS = "#d { color: rgb(255, 0, 0); }";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "var events = [];\n"
            + "var violations = [];\n"
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  violations.push('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n"
            + "function track(el, name) {\n"
            + "  el.addEventListener('load', function() { events.push('load ' + name); });\n"
            + "  el.addEventListener('error', function() { events.push('error ' + name); });\n"
            + "}\n"
            + "function addLink(href) {\n"
            + "  var link = document.createElement('link');\n"
            + "  link.rel = 'stylesheet';\n"
            + "  track(link, 'link');\n"
            + "  link.href = href;\n"
            + "  document.head.appendChild(link);\n"
            + "}\n"
            + "function addScript(src) {\n"
            + "  var script = document.createElement('script');\n"
            + "  track(script, 'script');\n"
            + "  script.src = src;\n"
            + "  document.head.appendChild(script);\n"
            + "}\n"
            + "function addImage(src) {\n"
            + "  var img = document.createElement('img');\n"
            + "  track(img, 'img');\n"
            + "  img.src = src;\n"
            + "  document.body.appendChild(img);\n"
            + "}\n"
            + "window.addEventListener('load', function() {\n"
            + "  setTimeout(function() {\n"
            + "    events.sort();\n"
            + "    for (var i = 0; i < events.length; i++) { log(events[i]); }\n"
            + "    log(getComputedStyle(document.getElementById('d')).color);\n"
            + "    for (var i = 0; i < violations.length; i++) { log(violations[i]); }\n"
            + "    log('done');\n"
            + "  }, 200);\n"
            + "});\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Stylesheet redirected to an allowed url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load link",
             "rgb(255, 0, 0)",
             "done"})
    public void style_allowed() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        setRedirect("redirect.css", new URL(URL_FIRST, "style.css"));
        setCss(new URL(URL_FIRST, "style.css"));
        loadPageVerify("style-src 'self'", "addLink('redirect.css');\n");
    }

    /**
     * Stylesheet redirected to a blocked url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error link",
             "rgb(0, 0, 0)",
             "violation style-src-elem §§URL§§/redirect.css enforce",
             "done"})
    public void style_blocked() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        setRedirect("redirect.css", new URL(URL_CROSS_ORIGIN_BASE, "style.css"));
        setCss(new URL(URL_CROSS_ORIGIN_BASE, "style.css"));
        loadPageVerify("style-src 'self'", "addLink('redirect.css');\n");
    }

    /**
     * Stylesheet redirected to a blocked url, report only policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load link",
             "rgb(255, 0, 0)",
             "violation style-src-elem §§URL§§/redirect.css report",
             "done"})
    public void style_reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        setRedirect("redirect.css", new URL(URL_CROSS_ORIGIN_BASE, "style.css"));
        setCss(new URL(URL_CROSS_ORIGIN_BASE, "style.css"));
        loadPageVerify(HEADER_REPORT_ONLY, "style-src 'self'", "addLink('redirect.css');\n");
    }

    /**
     * Import of a stylesheet redirected to a blocked url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error link",
             "rgb(0, 0, 0)",
             "violation style-src-elem §§URL§§/redirect.css enforce",
             "done"})
    public void import_blocked() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "style.css"), "@import url('redirect.css');",
                MimeType.TEXT_CSS);
        setRedirect("redirect.css", new URL(URL_CROSS_ORIGIN_BASE, "imported.css"));
        setCss(new URL(URL_CROSS_ORIGIN_BASE, "imported.css"));
        loadPageVerify("style-src 'self'", "addLink('style.css');\n");
    }

    /**
     * Script redirected to a blocked url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error script",
             "rgb(0, 0, 0)",
             "violation script-src-elem §§URL§§/redirect.js enforce",
             "done"})
    public void script_blocked() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        setRedirect("redirect.js", new URL(URL_CROSS_ORIGIN_BASE, "script.js"));
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "script.js"), "log('script');",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self'", "addScript('redirect.js');\n");
    }

    /**
     * Script redirected to an allowed url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"script",
             "load script",
             "rgb(0, 0, 0)",
             "done"})
    public void script_allowed() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        setRedirect("redirect.js", new URL(URL_FIRST, "script.js"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "script.js"), "log('script');",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self'", "addScript('redirect.js');\n");
    }

    /**
     * Image redirected to a blocked url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error img",
             "rgb(0, 0, 0)",
             "violation img-src §§URL§§/redirect.jpg enforce",
             "done"})
    public void image_blocked() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        setRedirect("redirect.jpg", new URL(URL_CROSS_ORIGIN_BASE, "img.jpg"));
        setImage(new URL(URL_CROSS_ORIGIN_BASE, "img.jpg"));
        loadPageVerify("img-src 'self'", "addImage('redirect.jpg');\n");
    }

    /**
     * Image redirected to an http url of host1.htmlunit-dev.org; without upgrade-insecure-requests
     * the image is loaded.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load img",
             "rgb(0, 0, 0)",
             "done"})
    public void upgrade_noPolicy() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        setRedirect("redirect.jpg", new URL(HTTP_HOST1 + ":" + PORT2 + "/img.jpg"));
        setImage(new URL(HTTP_HOST1 + ":" + PORT2 + "/img.jpg"));
        loadPageVerify(null, "addImage('redirect.jpg');\n");
    }

    /**
     * Image redirected to an http url of host1.htmlunit-dev.org; the redirect is upgraded
     * to https and fails (the test server does not support https).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error img",
             "rgb(0, 0, 0)",
             "done"})
    public void upgrade_image() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        setRedirect("redirect.jpg", new URL(HTTP_HOST1 + ":" + PORT2 + "/img.jpg"));
        setImage(new URL(HTTP_HOST1 + ":" + PORT2 + "/img.jpg"));
        getMockWebConnection().setThrowable(new URL("https://" + HOST1 + ":" + PORT2 + "/img.jpg"),
                new IOException("https is not supported by the test server"));
        loadPageVerify("upgrade-insecure-requests", "addImage('redirect.jpg');\n");
    }

    private void setRedirect(final String path, final URL target) throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", target.toExternalForm()));
        getMockWebConnection().setResponse(new URL(URL_FIRST, path), "", 302, "Found", MimeType.TEXT_PLAIN,
                headers);
    }

    private void setCss(final URL url) {
        getMockWebConnection().setResponse(url, RED_CSS, MimeType.TEXT_CSS);
    }

    private void setImage(final URL url) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] bytes = IOUtils.toByteArray(is);
            getMockWebConnection().setResponse(url, bytes, 200, "ok", "image/jpg", Collections.emptyList());
        }
    }

    private void loadPageVerify(final String policy, final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, testJs);
    }

    /**
     * Loads a page containing the harness script, a div and the test script;
     * the page is delivered with the given policy header.
     */
    private void loadPageVerify(final String header, final String policy, final String testJs)
            throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script src='" + URL_FIRST + "harness.js'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='d'>d</div>\n"
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
