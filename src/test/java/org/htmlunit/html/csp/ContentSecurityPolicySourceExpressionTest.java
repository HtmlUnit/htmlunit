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
 * Tests for the matching of the source expressions (host, port, scheme, path, wildcards
 * and keywords) of the Content-Security-Policy; the script-src directive is used for all tests.
 * <p>
 * Every page includes the external script 'harness.js' (allowed by 'nonce-harness'); it defines
 * the log() function and logs all securitypolicyviolation events. The tested script logs 'external'
 * (or 'data' for data urls).
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicySourceExpressionTest extends WebDriverTestCase {

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  log('violation ' + e.violatedDirective + ' ' + e.blockedURI);\n"
            + "});\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Host source without port matches only the default port of the scheme.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22222/test.js")
    public void hostWithoutPort() throws Exception {
        loadPageVerify("localhost", URL_FIRST + "test.js");
    }

    /**
     * Host source with scheme but without port matches only the default port.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22222/test.js")
    public void hostWithoutPortWithScheme() throws Exception {
        loadPageVerify("http://localhost", URL_FIRST + "test.js");
    }

    /**
     * Host source with port but without scheme.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void hostWithPort() throws Exception {
        loadPageVerify("localhost:" + PORT, URL_FIRST + "test.js");
    }

    /**
     * Host source with a different port.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22222/test.js")
    public void hostWithOtherPort() throws Exception {
        loadPageVerify("localhost:" + PORT2, URL_FIRST + "test.js");
    }

    /**
     * Host source with port wildcard.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void hostWithPortWildcard() throws Exception {
        loadPageVerify("localhost:*", URL_CROSS_ORIGIN_BASE + "test.js");
    }

    /**
     * Host source with scheme and port wildcard.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void hostWithSchemeAndPortWildcard() throws Exception {
        loadPageVerify("http://localhost:*", URL_CROSS_ORIGIN_BASE + "test.js");
    }

    /**
     * Host names are matched case-insensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void hostUpperCase() throws Exception {
        loadPageVerify("LOCALHOST:" + PORT, URL_FIRST + "test.js");
    }

    /**
     * Host source with https scheme does not match a http url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22222/test.js")
    public void hostHttpsScheme() throws Exception {
        loadPageVerify("https://localhost:" + PORT, URL_FIRST + "test.js");
    }

    /**
     * Scheme source.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void schemeSource() throws Exception {
        loadPageVerify("http:", URL_CROSS_ORIGIN_BASE + "test.js");
    }

    /**
     * Scheme sources are matched case-insensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void schemeSourceUpperCase() throws Exception {
        loadPageVerify("HTTP:", URL_CROSS_ORIGIN_BASE + "test.js");
    }

    /**
     * A scheme without colon is a host source.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22223/test.js")
    public void schemeWithoutColon() throws Exception {
        loadPageVerify("http", URL_CROSS_ORIGIN_BASE + "test.js");
    }

    /**
     * The wildcard matches network urls.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void star() throws Exception {
        loadPageVerify("*", URL_CROSS_ORIGIN_BASE + "test.js");
    }

    /**
     * The wildcard does not match data urls.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem data")
    public void starDataUrl() throws Exception {
        loadPageVerify("*", "data:text/javascript,log('data')");
    }

    /**
     * The data: scheme source matches data urls.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("data")
    public void dataScheme() throws Exception {
        loadPageVerify("data:", "data:text/javascript,log('data')");
    }

    /**
     * Host source with the path of a file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void pathFile() throws Exception {
        loadPageVerify(URL_FIRST + "js/test.js", URL_FIRST + "js/test.js");
    }

    /**
     * Host source with the path of a file does not match an other file.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22222/js/other.js")
    public void pathFileOther() throws Exception {
        loadPageVerify(URL_FIRST + "js/test.js", URL_FIRST + "js/other.js");
    }

    /**
     * Host source with a directory path (trailing slash).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void pathDirectory() throws Exception {
        loadPageVerify(URL_FIRST + "js/", URL_FIRST + "js/test.js");
    }

    /**
     * Host source with a directory path matches nested directories.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void pathDirectoryNested() throws Exception {
        loadPageVerify(URL_FIRST + "js/", URL_FIRST + "js/sub/test.js");
    }

    /**
     * Host source with a path without trailing slash requires an exact match.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22222/js/test.js")
    public void pathWithoutTrailingSlash() throws Exception {
        loadPageVerify(URL_FIRST + "js", URL_FIRST + "js/test.js");
    }

    /**
     * The query of the url is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void pathQueryIgnored() throws Exception {
        loadPageVerify(URL_FIRST + "js/test.js", URL_FIRST + "js/test.js?v=1");
    }

    /**
     * Paths are matched case-sensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22222/js/test.js")
    public void pathCaseSensitive() throws Exception {
        loadPageVerify(URL_FIRST + "JS/test.js", URL_FIRST + "js/test.js");
    }

    /**
     * Percent encoded characters of the path are decoded.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void pathPercentEncoded() throws Exception {
        loadPageVerify(URL_FIRST + "js/te%73t.js", URL_FIRST + "js/test.js");
    }

    /**
     * Keywords are matched case-insensitive.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void selfUpperCase() throws Exception {
        loadPageVerify("'SELF'", URL_FIRST + "test.js");
    }

    /**
     * Unquoted self is a host source (and not the keyword).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22222/test.js")
    public void selfUnquoted() throws Exception {
        loadPageVerify("self", URL_FIRST + "test.js");
    }

    /**
     * The keyword 'none' together with other sources.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void noneWithOtherSources() throws Exception {
        loadPageVerify("'none' 'self'", URL_FIRST + "test.js");
    }

    /**
     * The keyword 'self' (localhost) does not match the ip address.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://127.0.0.1:22222/test.js")
    public void selfIpAddress() throws Exception {
        loadPageVerify("'self'", "http://127.0.0.1:" + PORT + "/test.js");
    }

    /**
     * Host source with an ip address.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void ipAddress() throws Exception {
        loadPageVerify("127.0.0.1:" + PORT, "http://127.0.0.1:" + PORT + "/test.js");
    }

    /**
     * Host source localhost does not match the ip address.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://127.0.0.1:22222/test.js")
    public void localhostIpAddress() throws Exception {
        loadPageVerify("localhost:" + PORT, "http://127.0.0.1:" + PORT + "/test.js");
    }

    /**
     * Host source with subdomain wildcard.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void wildcardSubdomain() throws Exception {
        assertHostsConfigured();
        loadPageVerify("*." + HOSTS_DOMAIN + ":" + PORT, URL_HOST1 + "test.js");
    }

    /**
     * Host source with subdomain wildcard does not match the base domain.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://htmlunit-dev.org:22222/test.js")
    public void wildcardSubdomainBaseDomain() throws Exception {
        assertHostsConfigured();
        loadPageVerify("*." + HOSTS_DOMAIN + ":" + PORT, URL_HOST3 + "test.js");
    }

    /**
     * Host source with subdomain wildcard but without port.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://host1.htmlunit-dev.org:22222/test.js")
    public void wildcardSubdomainWithoutPort() throws Exception {
        assertHostsConfigured();
        loadPageVerify("*." + HOSTS_DOMAIN, URL_HOST1 + "test.js");
    }

    /**
     * Loads a page with the policy "script-src 'nonce-harness' " + sourceExpression; the page
     * contains the harness script and an external script with the given url.
     */
    private void loadPageVerify(final String sourceExpression, final String scriptUrl) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script src='harness.js' nonce='harness'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script src=\"" + scriptUrl + "\"></script>\n"
            + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'nonce-harness' " + sourceExpression));
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "harness.js"), HARNESS_JS,
                MimeType.TEXT_JAVASCRIPT);
        if (!scriptUrl.startsWith("data:")) {
            getMockWebConnection().setResponse(new URL(scriptUrl), "log('external');",
                    MimeType.TEXT_JAVASCRIPT);
        }

        final WebDriver driver = loadPage2(URL_FIRST, ISO_8859_1);
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
