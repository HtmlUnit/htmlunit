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
package org.htmlunit.javascript.host;

import java.net.URL;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@code window.isSecureContext}. The pages are served over http; localhost and
 * the loopback addresses are potentially trustworthy (secure contexts) like in the browsers.
 *
 * @author Ronald Brill
 * @see <a href="https://w3c.github.io/webappsec-secure-contexts/">Secure Contexts</a>
 */
public class WindowIsSecureContextTest extends WebDriverTestCase {

    private static final String LOG_IS_SECURE_CONTEXT = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(window.isSecureContext);\n"
            + "</script></head><body></body></html>";

    /**
     * A page from http://localhost.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void localhost() throws Exception {
        loadPageVerifyTitle2(LOG_IS_SECURE_CONTEXT);
    }

    /**
     * A page from http://127.0.0.1 (loopback address).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void loopbackIPv4() throws Exception {
        final URL url = new URL("http://127.0.0.1:" + PORT + "/");
        final WebDriver driver = loadPage2(LOG_IS_SECURE_CONTEXT, url);
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * An iframe without src (about:blank) of a page from http://localhost.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void aboutBlankIframe() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(document.getElementById('f').contentWindow.isSecureContext);\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'><iframe id='f'></iframe></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * An iframe with srcdoc (about:srcdoc) of a page from http://localhost.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void srcdocIframe() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(document.getElementById('f').contentWindow.isSecureContext);\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'><iframe id='f' srcdoc='<p>x</p>'></iframe></body></html>";

        loadPageVerifyTitle2(html);
    }
}
