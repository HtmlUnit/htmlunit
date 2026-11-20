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
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the parsing of the Content-Security-Policy headers (directive names, keywords,
 * nonces, whitespace, duplicates) and the handling of several policies (several headers,
 * comma separated policies, enforced and report only policies).
 * <p>
 * Every page includes the external script 'harness.js' (allowed by 'self'); it defines
 * the log() function and logs all securitypolicyviolation events.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyPolicyListTest extends WebDriverTestCase {

    private static final String INLINE_SCRIPT = "<script>log('inline');</script>\n";

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  log('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Directive names are case-insensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void directiveNameUpperCase() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "SCRIPT-SRC 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Unknown directives are ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void unknownDirective() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "foo-bar baz; script-src 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * For duplicate directives only the first one is used.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void duplicateDirective() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self' 'unsafe-inline'; script-src 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * For duplicate directives only the first one is used (first one blocks).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void duplicateDirectiveFirstBlocks() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self'; script-src 'self' 'unsafe-inline'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * An empty policy header.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void emptyValue() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, ""));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * A policy header without directives.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void onlySemicolons() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, ";;"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Additional whitespace and semicolons.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void whitespaceAndSemicolons() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "  script-src   'self'  ;  ; "));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Tabs separate the directive name and the sources.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void tabSeparated() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src\t'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * An invalid source expression is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void invalidSource() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self' 'unsafe-inline' 'invalid'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Keywords are case-insensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void unsafeInlineUpperCase() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'UNSAFE-INLINE'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * The header name is case-insensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void headerNameLowerCase() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("content-security-policy", "script-src 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Two policy headers; both have to allow the script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void twoHeadersSecondBlocks() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'unsafe-inline'"));
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Two policy headers; both have to allow the script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void twoHeadersFirstBlocks() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'"));
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'unsafe-inline'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Two policy headers allowing the script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void twoHeadersBothAllow() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'unsafe-inline'"));
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "default-src 'self' 'unsafe-inline'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Two policy headers blocking the script; one violation per policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation script-src-elem inline enforce",
             "violation script-src-elem inline enforce"})
    public void twoHeadersBothBlock() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'"));
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "default-src 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Comma separated policies in one header; both have to allow the script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void commaSeparated() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self' 'unsafe-inline', script-src 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Comma separated policies in one header with an empty policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void commaSeparatedEmptyPolicy() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'unsafe-inline', "));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Enforced policy allows, report only policy reports.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline",
             "violation script-src-elem inline report"})
    public void enforcedAndReportOnly() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'unsafe-inline'"));
        headers.add(new NameValuePair(HEADER_REPORT_ONLY, "script-src 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Enforced and report only policy are both violated.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation script-src-elem inline enforce",
             "violation script-src-elem inline report"})
    public void enforcedAndReportOnlyBothBlock() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'"));
        headers.add(new NameValuePair(HEADER_REPORT_ONLY, "script-src 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Two report only policies.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline",
             "violation script-src-elem inline report",
             "violation script-src-elem inline report"})
    public void twoReportOnly() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HEADER_REPORT_ONLY, "script-src 'self'"));
        headers.add(new NameValuePair(HEADER_REPORT_ONLY, "default-src 'self'"));
        loadPageVerify(headers, INLINE_SCRIPT);
    }

    /**
     * Nonces are case-sensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void nonceCaseSensitive() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'nonce-ABC'"));
        loadPageVerify(headers, "<script nonce='abc'>log('inline');</script>\n");
    }

    /**
     * Nonces may contain the base64 characters +, / and =.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void nonceBase64Characters() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'nonce-a+b/c=='"));
        loadPageVerify(headers, "<script nonce='a+b/c=='>log('inline');</script>\n");
    }

    /**
     * Nonces may contain the base64url characters - and _.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void nonceBase64UrlCharacters() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'nonce-a-b_c'"));
        loadPageVerify(headers, "<script nonce='a-b_c'>log('inline');</script>\n");
    }

    /**
     * An empty nonce source is invalid.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline enforce")
    public void nonceEmpty() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'nonce-'"));
        loadPageVerify(headers, "<script nonce=''>log('inline');</script>\n");
    }

    /**
     * The nonce attribute is not trimmed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "inline",
            FF = "violation script-src-elem inline enforce",
            FF_ESR = "violation script-src-elem inline enforce")
    public void nonceWhitespaceInAttribute() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'nonce-abc'"));
        loadPageVerify(headers, "<script nonce=' abc'>log('inline');</script>\n");
    }

    /**
     * The nonce attribute is not trimmed (trailing whitespace).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "inline",
            FF = "violation script-src-elem inline enforce",
            FF_ESR = "violation script-src-elem inline enforce")
    public void nonceTrailingWhitespaceInAttribute() throws Exception {
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self' 'nonce-abc'"));
        loadPageVerify(headers, "<script nonce='abc '>log('inline');</script>\n");
    }

    /**
     * Loads a page containing the harness script and the given body;
     * the page is delivered with the given headers.
     */
    private void loadPageVerify(final List<NameValuePair> headers, final String body) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script src='harness.js'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + body
            + "</body></html>";

        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "harness.js"), HARNESS_JS,
                MimeType.TEXT_JAVASCRIPT);

        final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
