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
 * Tests for the Content-Security-Policy directive object-src (object and embed elements).
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and collects all securitypolicyviolation events. The function track(id) collects the load
 * and error events of an element. The events and the violations are logged at the end
 * (200ms after the load event) because the browsers fire them async.
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * </p>
 * <p>
 * HtmlUnit does not load the content of object elements; therefore there are no load
 * events (HtmlUnitNYI). Like Firefox, HtmlUnit fires the error event if the object is blocked
 * when simulating Firefox.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyObjectTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "var events = [];\n"
            + "var violations = [];\n"
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  violations.push('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n"
            + "function track(id) {\n"
            + "  var el = document.getElementById(id);\n"
            + "  el.addEventListener('load', function() { events.push('load ' + id); });\n"
            + "  el.addEventListener('error', function() { events.push('error ' + id); });\n"
            + "}\n"
            + "window.addEventListener('load', function() {\n"
            + "  setTimeout(function() {\n"
            + "    for (var i = 0; i < events.length; i++) { log(events[i]); }\n"
            + "    for (var i = 0; i < violations.length; i++) { log(violations[i]); }\n"
            + "    log('done');\n"
            + "  }, 200);\n"
            + "});\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load o", "done"})
    @HtmlUnitNYI(CHROME = {"done"},
            EDGE = {"done"},
            FF = {"done"},
            FF_ESR = {"done"})
    public void noPolicy() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(null, object("o", URL_CROSS_ORIGIN_BASE + "obj.html"), "track('o');\n");
    }

    /**
     * The directive object-src 'none' blocks all objects.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation object-src §§URL§§/obj.html enforce", "done"},
            FF = {"error o", "violation object-src §§URL§§/obj.html enforce", "done"},
            FF_ESR = {"error o", "violation object-src §§URL§§/obj.html enforce", "done"})
    public void none() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'none'", object("o", "obj.html"), "track('o');\n");
    }

    /**
     * Same origin object, object-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load o", "done"})
    @HtmlUnitNYI(CHROME = {"done"},
            EDGE = {"done"},
            FF = {"done"},
            FF_ESR = {"done"})
    public void self_sameOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'self'", object("o", "obj.html"), "track('o');\n");
    }

    /**
     * Cross origin object, object-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF_ESR = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"})
    public void self_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'self'", object("o", URL_CROSS_ORIGIN_BASE + "obj.html"), "track('o');\n");
    }

    /**
     * Cross origin object, the origin is listed in object-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load o", "done"})
    @HtmlUnitNYI(CHROME = {"done"},
            EDGE = {"done"},
            FF = {"done"},
            FF_ESR = {"done"})
    public void hostSource() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'self' http://localhost:" + PORT2,
                object("o", URL_CROSS_ORIGIN_BASE + "obj.html"), "track('o');\n");
    }

    /**
     * Object-src falls back to default-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF_ESR = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"})
    public void defaultSrcFallback() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("default-src 'self'", object("o", URL_CROSS_ORIGIN_BASE + "obj.html"), "track('o');\n");
    }

    /**
     * The embed element is controlled by object-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation object-src §§URL§§/obj.html enforce", "done"})
    public void embed() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'none'", "<embed id='e' src='obj.html'>\n", "track('e');\n");
    }

    /**
     * Report only policies do not block the object.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load o", "violation object-src http://localhost:§§PORT§§ report", "done"})
    @HtmlUnitNYI(CHROME = {"violation object-src http://localhost:§§PORT§§ report", "done"},
            EDGE = {"violation object-src http://localhost:§§PORT§§ report", "done"},
            FF = {"violation object-src http://localhost:§§PORT§§ report", "done"},
            FF_ESR = {"violation object-src http://localhost:§§PORT§§ report", "done"})
    public void reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HEADER_REPORT_ONLY, "object-src 'self'",
                object("o", URL_CROSS_ORIGIN_BASE + "obj.html"), "track('o');\n");
    }

    /**
     * The policy can be delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF_ESR = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"})
    public void meta() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"object-src 'self'\">\n",
                object("o", URL_CROSS_ORIGIN_BASE + "obj.html"), "track('o');\n");
    }

    /**
     * Object element created by script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF_ESR = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"})
    public void dynamic() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'self'", "",
                "var o = document.createElement('object');\n"
                + "o.id = 'o';\n"
                + "o.setAttribute('data', '" + URL_CROSS_ORIGIN_BASE + "obj.html');\n"
                + "document.body.appendChild(o);\n"
                + "track('o');\n");
    }

    /**
     * Object element created by script but not added to the document.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("done")
    public void detached() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'self'", "",
                "var o = document.createElement('object');\n"
                + "o.setAttribute('data', '" + URL_CROSS_ORIGIN_BASE + "obj.html');\n");
    }

    /**
     * The data of an allowed object is changed to a blocked url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error o", "load o", "violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"},
            FF_ESR = {"error o", "violation object-src http://localhost:§§PORT§§ enforce", "done"})
    @HtmlUnitNYI(CHROME = {"violation object-src http://localhost:§§PORT§§ enforce", "done"},
            EDGE = {"violation object-src http://localhost:§§PORT§§ enforce", "done"})
    public void dataChanged() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'self'", object("o", "obj.html"),
                "track('o');\n"
                + "document.getElementById('o').setAttribute('data', '" + URL_CROSS_ORIGIN_BASE + "obj.html');\n");
    }

    /**
     * Object without data.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation object-src  enforce", "done"},
            FF = {"error o", "done"},
            FF_ESR = {"error o", "done"})
    @HtmlUnitNYI(CHROME = {"done"},
            EDGE = {"done"},
            FF = {"done"},
            FF_ESR = {"done"})
    public void noData() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'none'", "<object id='o' type='application/x-shockwave-flash'></object>\n",
                "track('o');\n");
    }

    /**
     * Data url; 'self' does not match data urls.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation object-src data enforce", "done"},
            FF = {"error o", "violation object-src data enforce", "done"},
            FF_ESR = {"error o", "violation object-src data enforce", "done"})
    public void dataUrl() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerify("object-src 'self'", object("o", "data:text/html,<p>data</p>"), "track('o');\n");
    }

    private static String object(final String id, final String data) {
        return "<object id='" + id + "' data='" + data + "'></object>\n";
    }

    private void loadPageVerify(final String policy, final String body, final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, "", body, testJs);
    }

    private void loadPageVerify(final String header, final String policy,
            final String body, final String testJs) throws Exception {
        loadPageVerify(header, policy, "", body, testJs);
    }

    /**
     * Loads a page containing the harness script, the given head and body and the test script;
     * the page is delivered with the given policy header.
     */
    private void loadPageVerify(final String header, final String policy,
            final String head, final String body, final String testJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + head
            + "<script src='" + URL_FIRST + "harness.js'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + body
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
        getMockWebConnection().setResponse(new URL(URL_FIRST, "obj.html"), "<p>object</p>",
                MimeType.TEXT_HTML);
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "obj.html"), "<p>object</p>",
                MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
