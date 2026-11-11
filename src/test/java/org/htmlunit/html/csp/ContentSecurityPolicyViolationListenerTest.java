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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlunit.HttpHeader;
import org.htmlunit.Page;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ContentSecurityPolicyViolationListener}.
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyViolationListenerTest extends SimpleWebTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void noPolicy() throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();
        loadPageWithPolicy(null, null, "<script>document.title = 'executed';</script>\n");

        assertTrue(listener.getViolations().isEmpty());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void inlineScript() throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();
        final HtmlPage page = loadPageWithPolicy(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self' 'report-sample'",
                "<script>document.title = 'executed';</script>\n");

        assertEquals("", page.getTitleText());

        final List<ContentSecurityPolicyViolation> violations = listener.getViolations();
        assertEquals(1, violations.size());

        final ContentSecurityPolicyViolation violation = violations.get(0);
        assertEquals(URL_FIRST.toExternalForm(), violation.getDocumentUri());
        assertEquals("inline", violation.getBlockedUri());
        assertEquals("script-src-elem", violation.getEffectiveDirective());
        assertEquals("script-src-elem", violation.getViolatedDirective());
        assertEquals("script-src 'self' 'report-sample'", violation.getOriginalPolicy());
        assertEquals(URL_FIRST.toExternalForm(), violation.getSourceFile());
        assertEquals("document.title = 'executed';", violation.getSample());
        assertEquals(3, violation.getLineNumber());
        assertEquals(200, violation.getStatusCode());
        assertFalse(violation.isReportOnly());
        assertEquals("enforce", violation.getDisposition());
        assertEquals("'script-src-elem' blocked 'inline' in '" + URL_FIRST
                + "' (policy 'script-src 'self' 'report-sample'')", violation.toString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void reportOnly() throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();
        final HtmlPage page = loadPageWithPolicy(HEADER_REPORT_ONLY, "script-src 'self'",
                "<script>document.title = 'executed';</script>\n");

        assertEquals("executed", page.getTitleText());

        final List<ContentSecurityPolicyViolation> violations = listener.getViolations();
        assertEquals(1, violations.size());
        assertTrue(violations.get(0).isReportOnly());
        assertEquals("report", violations.get(0).getDisposition());
        assertEquals("", violations.get(0).getSample());
        assertTrue(violations.get(0).toString().startsWith("[Report Only] "));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void twoPolicies() throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();

        final String html = DOCTYPE_HTML
            + "<html><head><title>first</title></head><body>\n"
            + "<script>document.title = 'executed';</script>\n"
            + "</body></html>";
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'"));
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "default-src 'none'"));
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        getWebClientWithMockWebConnection().getPage(URL_FIRST);

        final List<ContentSecurityPolicyViolation> violations = listener.getViolations();
        assertEquals(2, violations.size());
        assertEquals("script-src 'self'", violations.get(0).getOriginalPolicy());
        assertEquals("default-src 'none'", violations.get(1).getOriginalPolicy());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void eval() throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"),
                "try { eval('document.title = 1'); } catch(e) { document.title = e.name; }",
                MimeType.TEXT_JAVASCRIPT);
        final HtmlPage page = loadPageWithPolicy(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'",
                "<script src='test.js'></script>\n");

        assertEquals("EvalError", page.getTitleText());

        final List<ContentSecurityPolicyViolation> violations = listener.getViolations();
        assertEquals(1, violations.size());
        assertEquals("eval", violations.get(0).getBlockedUri());
        assertEquals("script-src", violations.get(0).getEffectiveDirective());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void xmlHttpRequest() throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"),
                "var xhr = new XMLHttpRequest();\n"
                + "xhr.open('GET', 'http://localhost:" + PORT2 + "/data.txt', false);\n"
                + "try { xhr.send(); } catch(e) { document.title = e.name; }\n",
                MimeType.TEXT_JAVASCRIPT);
        final HtmlPage page = loadPageWithPolicy(HttpHeader.CONTENT_SECURIRY_POLICY, "connect-src 'self'",
                "<script src='test.js'></script>\n");

        assertEquals("NetworkError", page.getTitleText());

        final List<ContentSecurityPolicyViolation> violations = listener.getViolations();
        assertEquals(1, violations.size());
        assertEquals("http://localhost:" + PORT2 + "/data.txt", violations.get(0).getBlockedUri());
        assertEquals("connect-src", violations.get(0).getEffectiveDirective());
    }

    /**
     * Form submission blocked by form-action; also checked if JavaScript is disabled.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void formAction() throws Exception {
        formAction(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void formActionJavaScriptDisabled() throws Exception {
        formAction(false);
    }

    private void formAction(final boolean javaScriptEnabled) throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();
        getWebClient().getOptions().setJavaScriptEnabled(javaScriptEnabled);
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "action.html"),
                "<html><head><title>submitted</title></head></html>");
        final HtmlPage page = loadPageWithPolicy(HttpHeader.CONTENT_SECURIRY_POLICY, "form-action 'self'",
                "<form action='" + URL_CROSS_ORIGIN_BASE + "action.html' method='post'>\n"
                + "  <input type='submit' id='submitButton'>\n"
                + "</form>\n");

        final HtmlPage result = page.getHtmlElementById("submitButton").click();
        assertSame(page, result);

        final List<ContentSecurityPolicyViolation> violations = listener.getViolations();
        assertEquals(1, violations.size());
        assertEquals(URL_CROSS_ORIGIN_BASE + "action.html", violations.get(0).getBlockedUri());
        assertEquals("form-action", violations.get(0).getEffectiveDirective());
        assertEquals("", violations.get(0).getSample());
        assertFalse(violations.get(0).isReportOnly());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void frameAncestors() throws Exception {
        frameAncestors(true);
    }

    /**
     * The listener is also informed if JavaScript is disabled.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void frameAncestorsJavaScriptDisabled() throws Exception {
        frameAncestors(false);
    }

    private void frameAncestors(final boolean javaScriptEnabled) throws Exception {
        getWebClientWithMockWebConnection().getOptions().setJavaScriptEnabled(javaScriptEnabled);

        final List<Page> pages = new ArrayList<>();
        final List<ContentSecurityPolicyViolation> violations = new ArrayList<>();
        getWebClientWithMockWebConnection().setContentSecurityPolicyViolationListener((page, violation) -> {
            pages.add(page);
            violations.add(violation);
        });

        final URL frameUrl = new URL(URL_FIRST, "frame.html");
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY, "frame-ancestors 'none'"));
        getMockWebConnection().setResponse(frameUrl, DOCTYPE_HTML + "<html><body>frame</body></html>",
                200, "OK", MimeType.TEXT_HTML, headers);

        final String html = DOCTYPE_HTML
            + "<html><head><title>first</title></head><body>\n"
            + "<iframe src='frame.html'></iframe>\n"
            + "</body></html>";
        getMockWebConnection().setResponse(URL_FIRST, html);
        final HtmlPage page = getWebClientWithMockWebConnection().getPage(URL_FIRST);

        assertEquals(1, violations.size());
        assertSame(page, pages.get(0));

        final ContentSecurityPolicyViolation violation = violations.get(0);
        assertEquals(frameUrl.toExternalForm(), violation.getDocumentUri());
        assertEquals("http://localhost:" + PORT, violation.getBlockedUri());
        assertEquals("frame-ancestors", violation.getEffectiveDirective());
        assertEquals(200, violation.getStatusCode());
        assertFalse(violation.isReportOnly());
    }

    /**
     * Without listener violations are only logged.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void noListener() throws Exception {
        assertNull(getWebClientWithMockWebConnection().getContentSecurityPolicyViolationListener());

        final HtmlPage page = loadPageWithPolicy(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'",
                "<script>document.title = 'executed';</script>\n");
        assertEquals("", page.getTitleText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void collectingListenerClear() throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();
        loadPageWithPolicy(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'",
                "<script>document.title = 'executed';</script>\n");
        assertEquals(1, listener.getViolations().size());

        listener.clear();
        assertTrue(listener.getViolations().isEmpty());
    }

    private CollectingContentSecurityPolicyViolationListener setListener() {
        final CollectingContentSecurityPolicyViolationListener listener =
                new CollectingContentSecurityPolicyViolationListener();
        getWebClientWithMockWebConnection().setContentSecurityPolicyViolationListener(listener);
        return listener;
    }

    private HtmlPage loadPageWithPolicy(final String header, final String policy, final String body)
            throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + body
            + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        if (policy != null) {
            headers.add(new NameValuePair(header, policy));
        }
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        return getWebClientWithMockWebConnection().getPage(URL_FIRST);
    }
}
