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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the referrer policy: the Referer header and document.referrer of requests of a page
 * (http://localhost:PORT/page.html?p=1) to the same origin and to another origin (127.0.0.1:PORT).
 * <p>The frame tests log the document.referrer of a same-origin and a cross-origin iframe
 * ('same [...]' and 'cross [...]').</p>
 *
 * @author Ronald Brill
 * @see <a href="https://w3c.github.io/webappsec-referrer-policy/">Referrer Policy</a>
 */
public class ReferrerPolicyTest extends WebDriverTestCase {

    private static final String CROSS_ORIGIN = "http://127.0.0.1:" + PORT + "/";

    /**
     * Without a policy, the default 'strict-origin-when-cross-origin' is used.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void defaultPolicy() throws Exception {
        frames("", null, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross []", "same []"})
    public void metaNoReferrer() throws Exception {
        frames(meta("no-referrer"), null, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaNoReferrerWhenDowngrade() throws Exception {
        frames(meta("no-referrer-when-downgrade"), null, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross [§§URL§§]", "same [§§URL§§]"})
    public void metaOrigin() throws Exception {
        frames(meta("origin"), null, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaOriginWhenCrossOrigin() throws Exception {
        frames(meta("origin-when-cross-origin"), null, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross []", "same [§§URL§§page.html?p=1]"})
    public void metaSameOrigin() throws Exception {
        frames(meta("same-origin"), null, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross [§§URL§§]", "same [§§URL§§]"})
    public void metaStrictOrigin() throws Exception {
        frames(meta("strict-origin"), null, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaStrictOriginWhenCrossOrigin() throws Exception {
        frames(meta("strict-origin-when-cross-origin"), null, "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaUnsafeUrl() throws Exception {
        frames(meta("unsafe-url"), null, "");
    }

    /**
     * The value is case-insensitive.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross []", "same []"})
    public void metaUpperCase() throws Exception {
        frames(meta("NO-REFERRER"), null, "");
    }

    /**
     * An unknown value is ignored.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaInvalid() throws Exception {
        frames(meta("unsafe-url") + meta("foo"), null, "");
    }

    /**
     * The legacy value 'never' (no-referrer).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross []", "same []"})
    public void metaLegacyNever() throws Exception {
        frames(meta("never"), null, "");
    }

    /**
     * The legacy value 'always' (unsafe-url).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaLegacyAlways() throws Exception {
        frames(meta("always"), null, "");
    }

    /**
     * The legacy value 'origin-when-crossorigin' (origin-when-cross-origin).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaLegacyOriginWhenCrossorigin() throws Exception {
        frames(meta("origin-when-crossorigin"), null, "");
    }

    /**
     * The last meta element wins.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaTwice() throws Exception {
        frames(meta("no-referrer") + meta("unsafe-url"), null, "");
    }

    /**
     * A meta element in the body.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaInBody() throws Exception {
        frames("", null, "", meta("unsafe-url"));
    }

    /**
     * A meta element added by script.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void metaAddedByScript() throws Exception {
        frames("<script>\n"
                + "  var m = document.createElement('meta');\n"
                + "  m.name = 'referrer';\n"
                + "  m.content = 'unsafe-url';\n"
                + "  document.head.appendChild(m);\n"
                + "</script>\n", null, "");
    }

    /**
     * The policy from the Referrer-Policy response header.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void headerUnsafeUrl() throws Exception {
        frames("", "unsafe-url", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross []", "same []"})
    public void headerNoReferrer() throws Exception {
        frames("", "no-referrer", "");
    }

    /**
     * The header may contain a list; the last known value is used.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void headerList() throws Exception {
        frames("", "no-referrer, unsafe-url, foo", "");
    }

    /**
     * Legacy values are not supported by the header.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void headerLegacyAlways() throws Exception {
        frames("", "always", "");
    }

    /**
     * A meta element overrides the header.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross []", "same []"})
    public void metaOverridesHeader() throws Exception {
        frames(meta("no-referrer"), "unsafe-url", "");
    }

    /**
     * The referrerpolicy attribute of the iframe overrides the policy of the document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"cross []", "same []"})
    public void iframeAttributeNoReferrer() throws Exception {
        frames("", null, " referrerpolicy='no-referrer'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void iframeAttributeUnsafeUrl() throws Exception {
        frames(meta("no-referrer"), null, " referrerpolicy='unsafe-url'");
    }

    /**
     * An invalid attribute value is ignored (the policy of the document is used).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"cross [§§URL§§page.html?p=1]", "same [§§URL§§page.html?p=1]"},
            FF = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"},
            FF_ESR = {"cross [§§URL§§]", "same [§§URL§§page.html?p=1]"})
    public void iframeAttributeInvalid() throws Exception {
        frames(meta("unsafe-url"), null, " referrerpolicy='foo'");
    }

    /**
     * The Referer header of a cross-origin image request (default policy).
     * The alerts are the image events and the Referer header received by the server.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "§§URL§§"})
    public void imageCrossOrigin() throws Exception {
        image("", "");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"load", "§§URL§§page.html?p=1"},
            FF = {"load", "§§URL§§"},
            FF_ESR = {"load", "§§URL§§"})
    public void imageCrossOriginUnsafeUrl() throws Exception {
        image(meta("unsafe-url"), "");
    }

    /**
     * The referrerpolicy attribute of the image.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"load", "§§URL§§page.html?p=1"},
            FF = {"load", "§§URL§§"},
            FF_ESR = {"load", "§§URL§§"})
    public void imageAttributeUnsafeUrl() throws Exception {
        image("", " referrerpolicy='unsafe-url'");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "null"})
    public void imageAttributeNoReferrer() throws Exception {
        image(meta("unsafe-url"), " referrerpolicy='no-referrer'");
    }

    private static String meta(final String policy) {
        return "<meta name='referrer' content='" + policy + "'>\n";
    }

    private void frames(final String head, final String header, final String frameAttributes) throws Exception {
        frames(head, header, frameAttributes, "");
    }

    /**
     * Loads the page with the given head and Referrer-Policy header; the page contains
     * a same-origin and a cross-origin iframe (with the given attributes). Every frame posts its
     * document.referrer to the page, the page logs both (sorted).
     */
    private void frames(final String head, final String header, final String frameAttributes,
            final String body) throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);

        final String frameScript = "<script>\n"
                + "  var kind = location.host.indexOf('127.0.0.1') == 0 ? 'cross' : 'same';\n"
                + "  parent.postMessage(kind + ' [' + document.referrer + ']', '*');\n"
                + "</script>";
        final String frameHtml = DOCTYPE_HTML + "<html><head>" + frameScript + "</head><body></body></html>";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), frameHtml);
        getMockWebConnection().setResponse(new URL(CROSS_ORIGIN + "frame.html"), frameHtml);

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + head
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var msgs = [];\n"
                + "  window.addEventListener('message', function(e) {\n"
                + "    msgs.push(e.data);\n"
                + "    if (msgs.length == 2) {\n"
                + "      msgs.sort();\n"
                + "      log(msgs[0]);\n"
                + "      log(msgs[1]);\n"
                + "    }\n"
                + "  });\n"
                + "</script>\n"
                + "</head><body>\n"
                + body
                + "  <iframe src='" + URL_FIRST + "frame.html'" + frameAttributes + "></iframe>\n"
                + "  <iframe src='" + CROSS_ORIGIN + "frame.html'" + frameAttributes + "></iframe>\n"
                + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        if (header != null) {
            headers.add(new NameValuePair("Referrer-Policy", header));
        }
        final URL pageUrl = new URL(URL_FIRST, "page.html?p=1");
        getMockWebConnection().setResponse(pageUrl, html, 200, "OK", MimeType.TEXT_HTML, UTF_8, headers);

        final WebDriver driver = loadPage2(pageUrl, null);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Loads the page with a cross-origin image (with the given attributes). The alerts are
     * the image event ('load' or 'error') and the Referer header received by the server ('null' if not sent).
     */
    private void image(final String head, final String imageAttributes) throws Exception {
        expandExpectedAlertsVariables(URL_FIRST);

        final URL imageUrl = new URL(CROSS_ORIGIN + "img" + System.nanoTime() + ".png");
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-png.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            getMockWebConnection().setResponse(imageUrl, directBytes, 200, "ok",
                    MimeType.IMAGE_PNG, Collections.emptyList());
        }

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + head
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "</script>\n"
                + "</head><body>\n"
                + "  <img src='" + imageUrl + "'" + imageAttributes
                        + " onload='log(\"load\")' onerror='log(\"error\")'>\n"
                + "</body></html>";

        final URL pageUrl = new URL(URL_FIRST, "page.html?p=1");
        getMockWebConnection().setResponse(pageUrl, html);

        final WebDriver driver = loadPage2(pageUrl, null);

        // wait for the image event
        final long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis();
        while (driver.getTitle().isEmpty() && System.currentTimeMillis() < maxWait) {
            Thread.sleep(50);
        }

        final WebRequest lastRequest = getMockWebConnection().getLastWebRequest();
        final String referer = imageUrl.toExternalForm().equals(lastRequest.getUrl().toExternalForm())
                ? String.valueOf(getMockWebConnection().getLastAdditionalHeaders().get(HttpHeader.REFERER))
                : "no image request";
        assertEquals(String.join("\u00a7", getExpectedAlerts()) + "\u00a7", driver.getTitle() + referer + "\u00a7");
    }
}
