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
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.htmlunit.HttpHeader;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebRequest;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Content-Security-Policy directive report-uri; the reports are sent as
 * POST request to the report uri.
 * <p>
 * The mock connection records all requests to urls starting with '/csp-report'; for every
 * report the method, the content type and the body are compared with the expected alerts.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyReportUriTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private final List<WebRequest> reports_ = Collections.synchronizedList(new ArrayList<>());
    private MockWebConnection connection_;

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * {@inheritDoc}
     * Records all requests to the report uris.
     */
    @Override
    protected MockWebConnection getMockWebConnection() {
        if (connection_ == null) {
            connection_ = new MockWebConnection() {
                @Override
                public RawResponseData getRawResponse(final WebRequest request) throws IOException {
                    if (request.getUrl().getPath().startsWith("/csp-report")) {
                        reports_.add(request);
                    }
                    return super.getRawResponse(request);
                }
            };
        }
        return connection_;
    }

    /**
     * Inline script blocked; the report is sent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"document-uri\":\"§§URL§§/\",\"referrer\":\"\",\"viola"
                + "ted-directive\":\"script-src-elem\",\"effective-directive\":\"script-src"
                + "-elem\",\"original-policy\":\"script-src 'self'; report-uri /csp-report"
                + "\",\"disposition\":\"enforce\",\"blocked-uri\":\"inline\",\"line-number"
                + "\":5,\"column-number\":9,\"source-file\":\"§§URL§§/\",\"status-code\":20"
                + "0,\"script-sample\":\"\"}}"},
            FF = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"source-fil"
                + "e\":\"§§URL§§/\",\"status-code\":200,\"violated-directive\":\"script-src"
                + "-elem\"}}"},
            FF_ESR = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"source-fil"
                + "e\":\"§§URL§§/\",\"status-code\":200,\"violated-directive\":\"script-src"
                + "-elem\"}}"})
    public void inlineScript() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerifyReports(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'; report-uri /csp-report",
                "", "<script>var x = 1;</script>\n");
    }

    /**
     * Inline script blocked with 'report-sample'; the sample is part of the report.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"document-uri\":\"§§URL§§/\",\"referrer\":\"\",\"viola"
                + "ted-directive\":\"script-src-elem\",\"effective-directive\":\"script-src"
                + "-elem\",\"original-policy\":\"script-src 'self' 'report-sample'; report-"
                + "uri /csp-report\",\"disposition\":\"enforce\",\"blocked-uri\":\"inline\""
                + ",\"line-number\":5,\"column-number\":9,\"source-file\":\"§§URL§§/\",\"st"
                + "atus-code\":200,\"script-sample\":\"var x = 1;\"}}"},
            FF = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self' 'report-sample'; report-uri §§URL§§/csp-report\",\"referrer\":"
                + "\"\",\"script-sample\":\"var x = 1;\",\"source-file\":\"§§URL§§/\",\"sta"
                + "tus-code\":200,\"violated-directive\":\"script-src-elem\"}}"},
            FF_ESR = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self' 'report-sample'; report-uri §§URL§§/csp-report\",\"referrer\":"
                + "\"\",\"script-sample\":\"var x = 1;\",\"source-file\":\"§§URL§§/\",\"sta"
                + "tus-code\":200,\"violated-directive\":\"script-src-elem\"}}"})
    public void reportSample() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerifyReports(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self' 'report-sample'; report-uri /csp-report",
                "", "<script>var x = 1;</script>\n");
    }

    /**
     * External script blocked.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"document-uri\":\"§§URL§§/\",\"referrer\":\"\",\"viola"
                + "ted-directive\":\"script-src-elem\",\"effective-directive\":\"script-src"
                + "-elem\",\"original-policy\":\"script-src 'self'; report-uri /csp-report"
                + "\",\"disposition\":\"enforce\",\"blocked-uri\":\"http://localhost:§§PORT"
                + "§§/script.js\",\"status-code\":200,\"script-sample\":\"\"}}"},
            FF = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"http://localhost:§§PORT§§/script.js\""
                + ",\"column-number\":1,\"disposition\":\"enforce\",\"document-uri\":\"§§UR"
                + "L§§/\",\"effective-directive\":\"script-src-elem\",\"original-policy\":"
                + "\"script-src 'self'; report-uri §§URL§§/csp-report\",\"referrer\":\"\","
                + "\"status-code\":200,\"violated-directive\":\"script-src-elem\"}}"},
            FF_ESR = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"http://localhost:§§PORT§§/script.js\""
                + ",\"column-number\":1,\"disposition\":\"enforce\",\"document-uri\":\"§§UR"
                + "L§§/\",\"effective-directive\":\"script-src-elem\",\"original-policy\":"
                + "\"script-src 'self'; report-uri §§URL§§/csp-report\",\"referrer\":\"\","
                + "\"status-code\":200,\"violated-directive\":\"script-src-elem\"}}"})
    public void externalScript() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "script.js"), "var x = 1;",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerifyReports(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'; report-uri /csp-report",
                "", "<script src='" + URL_CROSS_ORIGIN_BASE + "script.js'></script>\n");
    }

    /**
     * Report only policy; the report is sent also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"document-uri\":\"§§URL§§/\",\"referrer\":\"\",\"viola"
                + "ted-directive\":\"script-src-elem\",\"effective-directive\":\"script-src"
                + "-elem\",\"original-policy\":\"script-src 'self'; report-uri /csp-report"
                + "\",\"disposition\":\"report\",\"blocked-uri\":\"inline\",\"line-number\""
                + ":5,\"column-number\":9,\"source-file\":\"§§URL§§/\",\"status-code\":200,"
                + "\"script-sample\":\"\"}}"},
            FF = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"report\",\"document-uri\":\"§§URL§§/\",\"effective-directive\""
                + ":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-src "
                + "'self'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"source-file"
                + "\":\"§§URL§§/\",\"status-code\":200,\"violated-directive\":\"script-src-"
                + "elem\"}}"},
            FF_ESR = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"report\",\"document-uri\":\"§§URL§§/\",\"effective-directive\""
                + ":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-src "
                + "'self'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"source-file"
                + "\":\"§§URL§§/\",\"status-code\":200,\"violated-directive\":\"script-src-"
                + "elem\"}}"})
    public void reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerifyReports(HEADER_REPORT_ONLY, "script-src 'self'; report-uri /csp-report",
                "", "<script>var x = 1;</script>\n");
    }

    /**
     * Absolute report uri.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"document-uri\":\"§§URL§§/\",\"referrer\":\"\",\"viola"
                + "ted-directive\":\"script-src-elem\",\"effective-directive\":\"script-src"
                + "-elem\",\"original-policy\":\"script-src 'self'; report-uri §§URL§§/csp-"
                + "report\",\"disposition\":\"enforce\",\"blocked-uri\":\"inline\",\"line-n"
                + "umber\":5,\"column-number\":9,\"source-file\":\"§§URL§§/\",\"status-code"
                + "\":200,\"script-sample\":\"\"}}"},
            FF = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"source-fil"
                + "e\":\"§§URL§§/\",\"status-code\":200,\"violated-directive\":\"script-src"
                + "-elem\"}}"},
            FF_ESR = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"source-fil"
                + "e\":\"§§URL§§/\",\"status-code\":200,\"violated-directive\":\"script-src"
                + "-elem\"}}"})
    public void absoluteUri() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerifyReports(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self'; report-uri " + URL_FIRST + "csp-report",
                "", "<script>var x = 1;</script>\n");
    }

    /**
     * Two report uris; the report is sent to both.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"POST /csp-report1",
            "application/csp-report",
            "{\"csp-report\":{\"document-uri\":\"§§URL§§/\",\"referrer\":\"\",\"viola"
                + "ted-directive\":\"script-src-elem\",\"effective-directive\":\"script-src"
                + "-elem\",\"original-policy\":\"script-src 'self'; report-uri /csp-report1"
                + " /csp-report2\",\"disposition\":\"enforce\",\"blocked-uri\":\"inline\","
                + "\"line-number\":5,\"column-number\":9,\"source-file\":\"§§URL§§/\",\"sta"
                + "tus-code\":200,\"script-sample\":\"\"}}",
            "POST /csp-report2",
            "application/csp-report",
            "{\"csp-report\":{\"document-uri\":\"§§URL§§/\",\"referrer\":\"\",\"viola"
                + "ted-directive\":\"script-src-elem\",\"effective-directive\":\"script-src"
                + "-elem\",\"original-policy\":\"script-src 'self'; report-uri /csp-report1"
                + " /csp-report2\",\"disposition\":\"enforce\",\"blocked-uri\":\"inline\","
                + "\"line-number\":5,\"column-number\":9,\"source-file\":\"§§URL§§/\",\"sta"
                + "tus-code\":200,\"script-sample\":\"\"}}"},
            FF = {"POST /csp-report1",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; report-uri §§URL§§/csp-report1 §§URL§§/csp-report2\",\"referre"
                + "r\":\"\",\"source-file\":\"§§URL§§/\",\"status-code\":200,\"violated-dir"
                + "ective\":\"script-src-elem\"}}",
            "POST /csp-report2",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; report-uri §§URL§§/csp-report1 §§URL§§/csp-report2\",\"referre"
                + "r\":\"\",\"source-file\":\"§§URL§§/\",\"status-code\":200,\"violated-dir"
                + "ective\":\"script-src-elem\"}}"},
            FF_ESR = {"POST /csp-report1",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; report-uri §§URL§§/csp-report1 §§URL§§/csp-report2\",\"referre"
                + "r\":\"\",\"source-file\":\"§§URL§§/\",\"status-code\":200,\"violated-dir"
                + "ective\":\"script-src-elem\"}}",
            "POST /csp-report2",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; report-uri §§URL§§/csp-report1 §§URL§§/csp-report2\",\"referre"
                + "r\":\"\",\"source-file\":\"§§URL§§/\",\"status-code\":200,\"violated-dir"
                + "ective\":\"script-src-elem\"}}"})
    public void twoUris() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerifyReports(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self'; report-uri /csp-report1 /csp-report2",
                "", "<script>var x = 1;</script>\n");
    }

    /**
     * Two violations; two reports are sent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"document-uri\":\"§§URL§§/\",\"referrer\":\"\",\"viola"
                + "ted-directive\":\"script-src-elem\",\"effective-directive\":\"script-src"
                + "-elem\",\"original-policy\":\"script-src 'self'; img-src 'none'; report-"
                + "uri /csp-report\",\"disposition\":\"enforce\",\"blocked-uri\":\"inline\""
                + ",\"line-number\":5,\"column-number\":9,\"source-file\":\"§§URL§§/\",\"st"
                + "atus-code\":200,\"script-sample\":\"\"}}",
            "POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"document-uri\":\"§§URL§§/\",\"referrer\":\"\",\"viola"
                + "ted-directive\":\"img-src\",\"effective-directive\":\"img-src\",\"origin"
                + "al-policy\":\"script-src 'self'; img-src 'none'; report-uri /csp-report"
                + "\",\"disposition\":\"enforce\",\"blocked-uri\":\"§§URL§§/img.png\",\"sta"
                + "tus-code\":200,\"script-sample\":\"\"}}"},
            FF = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; img-src 'none'; report-uri §§URL§§/csp-report\",\"referrer\":"
                + "\"\",\"source-file\":\"§§URL§§/\",\"status-code\":200,\"violated-directi"
                + "ve\":\"script-src-elem\"}}",
            "POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"§§URL§§/img.png\",\"column-number\":1"
                + ",\"disposition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-d"
                + "irective\":\"img-src\",\"original-policy\":\"script-src 'self'; img-src "
                + "'none'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"status-code"
                + "\":200,\"violated-directive\":\"img-src\"}}",
            "POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"§§URL§§/favicon.ico\",\"column-number"
                + "\":1,\"disposition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effecti"
                + "ve-directive\":\"img-src\",\"original-policy\":\"script-src 'self'; img-"
                + "src 'none'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"status-c"
                + "ode\":200,\"violated-directive\":\"img-src\"}}"},
            FF_ESR = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; img-src 'none'; report-uri §§URL§§/csp-report\",\"referrer\":"
                + "\"\",\"source-file\":\"§§URL§§/\",\"status-code\":200,\"violated-directi"
                + "ve\":\"script-src-elem\"}}",
            "POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"§§URL§§/img.png\",\"column-number\":1"
                + ",\"disposition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-d"
                + "irective\":\"img-src\",\"original-policy\":\"script-src 'self'; img-src "
                + "'none'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"status-code"
                + "\":200,\"violated-directive\":\"img-src\"}}",
            "POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"§§URL§§/favicon.ico\",\"column-number"
                + "\":1,\"disposition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effecti"
                + "ve-directive\":\"img-src\",\"original-policy\":\"script-src 'self'; img-"
                + "src 'none'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"status-c"
                + "ode\":200,\"violated-directive\":\"img-src\"}}"})
    @HtmlUnitNYI(FF = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; img-src 'none'; report-uri §§URL§§/csp-report\",\"referrer\":"
                + "\"\",\"source-file\":\"§§URL§§/\",\"status-code\":200,\"violated-directi"
                + "ve\":\"script-src-elem\"}}",
            "POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"§§URL§§/img.png\",\"column-number\":1"
                + ",\"disposition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-d"
                + "irective\":\"img-src\",\"original-policy\":\"script-src 'self'; img-src "
                + "'none'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"status-code"
                + "\":200,\"violated-directive\":\"img-src\"}}"},
            FF_ESR = {"POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"inline\",\"column-number\":9,\"dispos"
                + "ition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-directive"
                + "\":\"script-src-elem\",\"line-number\":5,\"original-policy\":\"script-sr"
                + "c 'self'; img-src 'none'; report-uri §§URL§§/csp-report\",\"referrer\":"
                + "\"\",\"source-file\":\"§§URL§§/\",\"status-code\":200,\"violated-directi"
                + "ve\":\"script-src-elem\"}}",
            "POST /csp-report",
            "application/csp-report",
            "{\"csp-report\":{\"blocked-uri\":\"§§URL§§/img.png\",\"column-number\":1"
                + ",\"disposition\":\"enforce\",\"document-uri\":\"§§URL§§/\",\"effective-d"
                + "irective\":\"img-src\",\"original-policy\":\"script-src 'self'; img-src "
                + "'none'; report-uri §§URL§§/csp-report\",\"referrer\":\"\",\"status-code"
                + "\":200,\"violated-directive\":\"img-src\"}}"})
    public void twoViolations() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerifyReports(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self'; img-src 'none'; report-uri /csp-report",
                "", "<script>var x = 1;</script>\n<img src='img.png'>\n");
    }

    /**
     * Without report-uri no report is sent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void noReportUri() throws Exception {
        loadPageVerifyReports(HttpHeader.CONTENT_SECURIRY_POLICY, "script-src 'self'",
                "", "<script>var x = 1;</script>\n");
    }

    /**
     * Report-uri is ignored if delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void meta() throws Exception {
        loadPageVerifyReports(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' "
                        + "content=\"script-src 'self'; report-uri /csp-report\">\n",
                "<script>var x = 1;</script>\n");
    }

    /**
     * Report-uri together with report-to (without a Reporting-Endpoints header).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void withReportTo() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        loadPageVerifyReports(HttpHeader.CONTENT_SECURIRY_POLICY,
                "script-src 'self'; report-uri /csp-report; report-to csp-group",
                "", "<script>var x = 1;</script>\n");
    }

    /**
     * Loads the page with the given policy and compares the recorded reports with the
     * expected alerts (method, content type and body for every report).
     */
    private void loadPageVerifyReports(final String header, final String policy,
            final String head, final String body) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + head
            + "</head>\n"
            + "<body>\n"
            + body
            + "</body></html>";

        final List<NameValuePair> headers = new ArrayList<>();
        if (policy != null) {
            headers.add(new NameValuePair(header, policy));
        }
        getMockWebConnection().setResponse(URL_FIRST, html, 200, "OK", MimeType.TEXT_HTML, headers);
        getMockWebConnection().setDefaultResponse("", 204, "No Content", MimeType.TEXT_PLAIN);

        loadPage2(URL_FIRST, ISO_8859_1);

        // the browsers send the reports async
        int expected = 0;
        for (final String alert : getExpectedAlerts()) {
            if (alert.startsWith("POST /csp-report")) {
                expected++;
            }
        }
        final long maxWait = System.currentTimeMillis() + DEFAULT_WAIT_TIME.toMillis() * 3;
        while (reports_.size() < expected && System.currentTimeMillis() < maxWait) {
            Thread.sleep(50);
        }
        // real browsers: wait a bit more to detect additional reports
        sleepRealBrowser(200);

        final List<String> actual = new ArrayList<>();
        synchronized (reports_) {
            for (final WebRequest report : reports_) {
                actual.add(report.getHttpMethod() + " " + report.getUrl().getPath());
                final String contentType = report.getAdditionalHeader(HttpHeader.CONTENT_TYPE);
                actual.add(contentType == null ? "no content type" : contentType);
                actual.add(report.getRequestBody());
            }
        }
        assertEquals(String.join("\n", getExpectedAlerts()), String.join("\n", actual));
    }
}
