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
 * Tests for the Content-Security-Policy directive upgrade-insecure-requests.
 * <p>
 * These tests are covering the cases not observable with real browsers (the upgraded
 * navigations, form submissions and frames are failing at the test server without any visible
 * difference): the url of top level navigations, form submissions and frames, the inheritance of
 * the directive, the port handling, imports and the content security policy check with the upgraded url.
 * The tests use the mock connection and compare the urls requested from the mock connection.
 * The other tests are part of ContentSecurityPolicyUpgradeInsecureRequestsTest.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyUpgradeInsecureRequests2Test extends SimpleWebTestCase {

    private static final String UIR = "upgrade-insecure-requests";

    private static final String PAGE = "http://www.example.com/";
    private static final String OTHER = "http://www.example.org/";

    /**
     * Port 80 is replaced by the default port of https.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void port80() throws Exception {
        getWebClientWithMockWebConnection().getOptions().setDownloadImages(true);
        loadPage(UIR, "<img src='http://www.example.org:80/img.png'>\n");
        assertRequested("https://www.example.org/img.png");
    }

    /**
     * Other ports are kept.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void otherPort() throws Exception {
        getWebClientWithMockWebConnection().getOptions().setDownloadImages(true);
        loadPage(UIR, "<img src='http://www.example.org:8080/img.png'>\n");
        assertRequested("https://www.example.org:8080/img.png");
    }

    /**
     * The CSP checks are done with the upgraded url.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void imgSrcHttps() throws Exception {
        getWebClientWithMockWebConnection().getOptions().setDownloadImages(true);
        loadPage("img-src https:; " + UIR, "<img src='" + OTHER + "img.png'>\n");
        assertRequested("https://www.example.org/img.png");
    }

    /**
     * Stylesheets and imports are upgraded.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void stylesheet() throws Exception {
        getMockWebConnection().setResponse(new URL("https://www.example.org/style.css"),
                "@import url('" + OTHER + "imported.css');", MimeType.TEXT_CSS);
        getMockWebConnection().setResponse(new URL("https://www.example.org/imported.css"),
                "p { color: red; }", MimeType.TEXT_CSS);
        loadPage(UIR, "<link rel='stylesheet' href='" + OTHER + "style.css'>\n");
        assertRequested("https://www.example.org/style.css", "https://www.example.org/imported.css");
    }

    /**
     * Frames (nested navigations) are upgraded; the frame inherits the directive.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void iframe() throws Exception {
        getWebClientWithMockWebConnection().getOptions().setDownloadImages(true);
        getMockWebConnection().setResponse(new URL("https://www.example.org/frame.html"),
                "<html><body><img src='" + OTHER + "img.png'></body></html>");
        loadPage(UIR, "<iframe src='" + OTHER + "frame.html'></iframe>\n");
        assertRequested("https://www.example.org/frame.html", "https://www.example.org/img.png");
    }

    /**
     * Form submissions are upgraded.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void form() throws Exception {
        loadPage(UIR, "<form id='f' action='" + OTHER + "submit' method='get'></form>\n"
                + "<script>document.getElementById('f').submit();</script>\n");
        assertRequested("https://www.example.org/submit");
    }

    /**
     * Top level navigations are only upgraded for the host and port of the page.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void anchor() throws Exception {
        HtmlPage page = loadPage(UIR, "<a id='a' href='" + PAGE + "page2.html'>same</a>\n"
                + "<a id='b' href='" + OTHER + "page2.html'>other</a>\n");
        page.getHtmlElementById("a").click();
        assertRequested("https://www.example.com/page2.html");

        page = loadPage(UIR, "<a id='b' href='" + OTHER + "page2.html'>other</a>\n");
        page.getHtmlElementById("b").click();
        assertRequested("https://www.example.com/page2.html", "http://www.example.org/page2.html");
    }

    /**
     * Location changes are top level navigations.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void location() throws Exception {
        loadPage(UIR, "<script>location.href = '" + PAGE + "page2.html';</script>\n");
        assertRequested("https://www.example.com/page2.html");
    }

    private HtmlPage loadPage(final String policy, final String body) throws Exception {
        return loadPage(HttpHeader.CONTENT_SECURIRY_POLICY, policy, "", body);
    }

    private HtmlPage loadPage(final String header, final String policy, final String head, final String body)
            throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + head
            + "</head><body>\n"
            + body
            + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        if (policy != null) {
            headers.add(new NameValuePair(header, policy));
        }
        getMockWebConnection().setResponse(new URL(PAGE), html, 200, "OK", MimeType.TEXT_HTML, headers);
        getMockWebConnection().setDefaultResponse("<html><body>default</body></html>");
        final HtmlPage page = getWebClientWithMockWebConnection().getPage(PAGE);
        getWebClientWithMockWebConnection().waitForBackgroundJavaScript(1000);
        return page;
    }

    /**
     * Compares the requested urls (without the page itself) with the expected ones.
     */
    private void assertRequested(final String... expected) {
        final List<String> requested = new ArrayList<>();
        for (final URL url : getMockWebConnection().getRequestedUrls()) {
            final String urlString = url.toExternalForm();
            if (!PAGE.equals(urlString)) {
                requested.add(urlString);
            }
        }
        assertEquals(List.of(expected), requested);
    }
}
