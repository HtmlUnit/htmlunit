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
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;

/**
 * Tests for the option {@link org.htmlunit.WebClientOptions#setContentSecurityPolicyReportsEnabled(boolean)}.
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyReportsOptionTest extends SimpleWebTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";
    private static final String INLINE_SCRIPT = "<script>document.title = 'executed';</script>\n";

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void reportsEnabledByDefault() throws Exception {
        assertTrue(getWebClient().getOptions().isContentSecurityPolicyReportsEnabled());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void reportSent() throws Exception {
        final HtmlPage page = loadPageWithPolicy(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self'; report-uri /csp-report", INLINE_SCRIPT);
        assertEquals("", page.getTitleText());
        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals("/csp-report", getMockWebConnection().getLastWebRequest().getUrl().getPath());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void reportSentReportOnly() throws Exception {
        final HtmlPage page = loadPageWithPolicy(HEADER_REPORT_ONLY,
                "script-src 'self'; report-uri /csp-report", INLINE_SCRIPT);
        assertEquals("executed", page.getTitleText());
        assertEquals(2, getMockWebConnection().getRequestCount());
        assertEquals("/csp-report", getMockWebConnection().getLastWebRequest().getUrl().getPath());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void reportsDisabled() throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();
        getWebClientWithMockWebConnection().getOptions().setContentSecurityPolicyReportsEnabled(false);

        final HtmlPage page = loadPageWithPolicy(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self'; report-uri /csp-report", INLINE_SCRIPT);
        assertEquals("", page.getTitleText());
        assertEquals(1, listener.getViolations().size());
        assertEquals(1, getMockWebConnection().getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void reportsDisabledReportOnly() throws Exception {
        final CollectingContentSecurityPolicyViolationListener listener = setListener();
        getWebClientWithMockWebConnection().getOptions().setContentSecurityPolicyReportsEnabled(false);

        final HtmlPage page = loadPageWithPolicy(HEADER_REPORT_ONLY,
                "script-src 'self'; report-uri /csp-report", INLINE_SCRIPT);
        assertEquals("executed", page.getTitleText());
        assertEquals(1, listener.getViolations().size());
        assertTrue(listener.getViolations().get(0).isReportOnly());
        assertEquals(1, getMockWebConnection().getRequestCount());
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
        headers.add(new NameValuePair(header, policy));
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "/csp-report"), "");
        return getWebClientWithMockWebConnection().getPage(URL_FIRST);
    }
}
