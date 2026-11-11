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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.HttpHeader;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directive form-action.
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and logs all securitypolicyviolation events. The test code is placed in the external
 * script 'test.js'. All policies allow these scripts.
 * The pages the forms are submitted to log 'submitted' and their path.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyFormActionTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  log('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n";

    private static final String SUBMIT_JS = "document.getElementById('submitButton').click();\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy all submissions are allowed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submitted /action.html")
    public void noPolicy() throws Exception {
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify(null, form(URL_CROSS_ORIGIN_BASE + "action.html", "post"), SUBMIT_JS);
    }

    /**
     * Same origin submission, form-action 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submitted /action.html")
    public void formActionSelf_sameOrigin() throws Exception {
        setTargetResponse(URL_FIRST, "action.html");
        loadPageVerify("form-action 'self'", form("action.html", "post"), SUBMIT_JS);
    }

    /**
     * Cross origin submission, form-action 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation form-action §§URL§§/action.html enforce")
    public void formActionSelf_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify("form-action 'self'", form(URL_CROSS_ORIGIN_BASE + "action.html", "post"), SUBMIT_JS);
    }

    /**
     * Cross origin submission, the origin is listed in form-action.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submitted /action.html")
    public void formActionHostSource() throws Exception {
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify("form-action 'self' http://localhost:" + PORT2,
                form(URL_CROSS_ORIGIN_BASE + "action.html", "post"), SUBMIT_JS);
    }

    /**
     * The directive form-action 'none' blocks all submissions.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation form-action §§URL§§/action.html enforce")
    public void formActionNone() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        setTargetResponse(URL_FIRST, "action.html");
        loadPageVerify("form-action 'none'", form("action.html", "post"), SUBMIT_JS);
    }

    /**
     * Form-action does not fall back to default-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submitted /action.html")
    public void defaultSrcIsNoFallback() throws Exception {
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify("default-src 'self'", form(URL_CROSS_ORIGIN_BASE + "action.html", "post"), SUBMIT_JS);
    }

    /**
     * Report only policies do not block the submission; the violation event is
     * not delivered because the page is already replaced.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submitted /action.html")
    public void reportOnly() throws Exception {
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify(HEADER_REPORT_ONLY, "form-action 'self'", "",
                form(URL_CROSS_ORIGIN_BASE + "action.html", "post"), SUBMIT_JS);
    }

    /**
     * The policy can be delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation form-action §§URL§§/action.html enforce")
    public void meta() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"form-action 'self'\">\n",
                form(URL_CROSS_ORIGIN_BASE + "action.html", "post"), SUBMIT_JS);
    }

    /**
     * Get submission; Chrome reports the query as part of the blockedURI, Firefox does not.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "violation form-action §§URL§§/action.html?q=1 enforce",
            FF = "violation form-action §§URL§§/action.html enforce",
            FF_ESR = "violation form-action §§URL§§/action.html enforce")
    @HtmlUnitNYI(FF = "violation form-action §§URL§§/action.html?q=1 enforce",
            FF_ESR = "violation form-action §§URL§§/action.html?q=1 enforce")
    public void get() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html?q=1");
        loadPageVerify("form-action 'self'",
                "<form id='form1' action='" + URL_CROSS_ORIGIN_BASE + "action.html#ref' method='get'>\n"
                + "  <input type='hidden' name='q' value='1'>\n"
                + "  <input type='submit' id='submitButton'>\n"
                + "</form>\n", SUBMIT_JS);
    }

    /**
     * The formaction attribute of the submit button is checked.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation form-action §§URL§§/button.html enforce")
    public void formactionAttribute() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setTargetResponse(URL_FIRST, "action.html");
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "button.html");
        loadPageVerify("form-action 'self'",
                "<form id='form1' action='action.html' method='post'>\n"
                + "  <button type='submit' id='submitButton' formaction='"
                        + URL_CROSS_ORIGIN_BASE + "button.html'>submit</button>\n"
                + "</form>\n", SUBMIT_JS);
    }

    /**
     * The formaction attribute of the submit button is allowed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("submitted /button.html")
    public void formactionAttribute_allowed() throws Exception {
        setTargetResponse(URL_FIRST, "button.html");
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify("form-action 'self'",
                "<form id='form1' action='" + URL_CROSS_ORIGIN_BASE + "action.html' method='post'>\n"
                + "  <button type='submit' id='submitButton' formaction='button.html'>submit</button>\n"
                + "</form>\n", SUBMIT_JS);
    }

    /**
     * Form.submit() is checked also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation form-action §§URL§§/action.html enforce")
    public void formSubmit() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify("form-action 'self'", form(URL_CROSS_ORIGIN_BASE + "action.html", "post"),
                "document.getElementById('form1').submit();\n");
    }

    /**
     * Form.requestSubmit() is checked also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation form-action §§URL§§/action.html enforce")
    public void formRequestSubmit() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify("form-action 'self'", form(URL_CROSS_ORIGIN_BASE + "action.html", "post"),
                "document.getElementById('form1').requestSubmit();\n");
    }

    /**
     * Form submission into a different (i)frame is checked also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation form-action §§URL§§/action.html enforce")
    public void targetIframe() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify("form-action 'self'",
                "<iframe name='frame1' src='about:blank'></iframe>\n"
                + "<form id='form1' action='" + URL_CROSS_ORIGIN_BASE + "action.html' "
                        + "method='post' target='frame1'>\n"
                + "  <input type='submit' id='submitButton'>\n"
                + "</form>\n", SUBMIT_JS);
    }

    /**
     * The submit event is fired before the form-action check.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"submit", "violation form-action §§URL§§/action.html enforce"})
    public void submitEvent() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");
        loadPageVerify("form-action 'self'", form(URL_CROSS_ORIGIN_BASE + "action.html", "post"),
                "document.getElementById('form1').addEventListener('submit', function() { log('submit'); });\n"
                + SUBMIT_JS);
    }

    /**
     * Method dialog does not navigate; form-action is not checked.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("close")
    public void methodDialog() throws Exception {
        loadPageVerify("form-action 'none'",
                "<dialog id='dialog1' open>\n"
                + "  <form id='form1' method='dialog'>\n"
                + "    <input type='submit' id='submitButton'>\n"
                + "  </form>\n"
                + "</dialog>\n",
                "document.getElementById('dialog1').addEventListener('close', function() { log('close'); });\n"
                + SUBMIT_JS);
    }

    /**
     * A javascript: url as action is checked against form-action ('self' does not match).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation form-action javascript enforce")
    public void javascriptAction_self() throws Exception {
        loadPageVerify("form-action 'self'; script-src 'self' 'unsafe-inline'",
                form("javascript:log('javascript action')", "post"), SUBMIT_JS);
    }

    /**
     * A javascript: url as action, form-action allows the javascript: scheme.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("javascript action")
    public void javascriptAction_scheme() throws Exception {
        loadPageVerify("form-action javascript:; script-src 'self' 'unsafe-inline'",
                form("javascript:log('javascript action')", "post"), SUBMIT_JS);
    }

    /**
     * A javascript: url as action, form-action allows it but script-src does not.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void javascriptAction_scriptSrcBlocked() throws Exception {
        loadPageVerify("form-action javascript:; script-src 'self'",
                form("javascript:log('javascript action')", "post"), SUBMIT_JS);
    }

    /**
     * Redirect from an allowed url to a blocked one; Chrome checks the redirect
     * and reports the url of the form submission, Firefox does not check redirects.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "violation form-action §§URL§§/redirect.html enforce",
            FF = "submitted /action.html",
            FF_ESR = "submitted /action.html")
    @HtmlUnitNYI(FF = "violation form-action §§URL§§/redirect.html enforce",
            FF_ESR = "violation form-action §§URL§§/redirect.html enforce")
    public void redirect() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        setTargetResponse(URL_CROSS_ORIGIN_BASE, "action.html");

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", URL_CROSS_ORIGIN_BASE + "action.html"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "redirect.html"), "", 302, "Found",
                MimeType.TEXT_HTML, headers);

        loadPageVerify("form-action 'self'", form("redirect.html", "post"), SUBMIT_JS);
    }

    private static String form(final String action, final String method) {
        return "<form id='form1' action=\"" + action + "\" method='" + method + "'>\n"
                + "  <input type='submit' id='submitButton'>\n"
                + "</form>\n";
    }

    /**
     * Sets the response of a page the form is submitted to; this page logs
     * 'submitted' and its path (without resetting the log).
     */
    private void setTargetResponse(final URL base, final String path) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + "  window.top.name += 'submitted ' + location.pathname + '\\u00a7';\n"
                + "</script>\n"
                + "</head><body></body></html>";
        getMockWebConnection().setResponse(new URL(base, path), html, MimeType.TEXT_HTML);
    }

    private void loadPageVerify(final String policy, final String body, final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, "", body, testJs);
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
