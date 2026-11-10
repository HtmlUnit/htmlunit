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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directive img-src.
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and collects all securitypolicyviolation events. The violations are logged at the end
 * (200ms after the load event) because the browsers fire the violation events async.
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * The function addImage() creates an image and logs the load/error event.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyImageTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "var violations = [];\n"
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  violations.push('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n"
            + "window.addEventListener('load', function() {\n"
            + "  setTimeout(function() {\n"
            + "    for (var i = 0; i < violations.length; i++) { log(violations[i]); }\n"
            + "  }, 200);\n"
            + "});\n"
            + "function addImage(src) {\n"
            + "  var img = document.createElement('img');\n"
            + "  img.onload = function() { log('load'); };\n"
            + "  img.onerror = function() { log('error'); };\n"
            + "  img.src = src;\n"
            + "  document.body.appendChild(img);\n"
            + "  return img;\n"
            + "}\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy the image is loaded.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load")
    public void noPolicy() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify(null, "", "addImage('" + URL_CROSS_ORIGIN_BASE + "img.jpg');\n");
    }

    /**
     * Same origin image, img-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load")
    public void self_sameOrigin() throws Exception {
        setImage(URL_FIRST, "img.jpg");
        loadPageVerify("img-src 'self'", "", "addImage('img.jpg');\n");
    }

    /**
     * Cross origin image, img-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation img-src §§URL§§/img.jpg enforce"})
    public void self_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify("img-src 'self'", "", "addImage('" + URL_CROSS_ORIGIN_BASE + "img.jpg');\n");
    }

    /**
     * Cross origin image, the origin is listed in img-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load")
    public void hostSource() throws Exception {
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify("img-src 'self' http://localhost:" + PORT2, "",
                "addImage('" + URL_CROSS_ORIGIN_BASE + "img.jpg');\n");
    }

    /**
     * The directive img-src 'none' blocks all images (Firefox also reports the favicon,
     * HtmlUnit does not load the favicon).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error", "violation img-src §§URL§§/img.jpg enforce"},
            FF = {"error", "violation img-src §§URL§§/img.jpg enforce",
                  "violation img-src §§URL§§/favicon.ico enforce"},
            FF_ESR = {"error", "violation img-src §§URL§§/img.jpg enforce",
                      "violation img-src §§URL§§/favicon.ico enforce"})
    @HtmlUnitNYI(FF = {"error", "violation img-src §§URL§§/img.jpg enforce"},
            FF_ESR = {"error", "violation img-src §§URL§§/img.jpg enforce"})
    public void none() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        setImage(URL_FIRST, "img.jpg");
        loadPageVerify("img-src 'none'", "", "addImage('img.jpg');\n");
    }

    /**
     * Default-src is the fallback for img-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation img-src §§URL§§/img.jpg enforce"})
    public void defaultSrcFallback() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify("default-src 'self'", "", "addImage('" + URL_CROSS_ORIGIN_BASE + "img.jpg');\n");
    }

    /**
     * Data url images are only allowed if the scheme is listed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation img-src data enforce"})
    public void dataUrl_self() throws Exception {
        loadPageVerify("img-src 'self'", "", "addImage('" + DATA_GIF + "');\n");
    }

    /**
     * Data url images allowed by img-src data:.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load")
    public void dataUrl_data() throws Exception {
        loadPageVerify("img-src 'self' data:", "", "addImage('" + DATA_GIF + "');\n");
    }

    /**
     * Report only policies do not block the image.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "violation img-src §§URL§§/img.jpg report"})
    public void reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify(HEADER_REPORT_ONLY, "img-src 'self'", "", "",
                "addImage('" + URL_CROSS_ORIGIN_BASE + "img.jpg');\n");
    }

    /**
     * The policy can be delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation img-src §§URL§§/img.jpg enforce"})
    public void meta() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"img-src 'self'\">\n", "",
                "addImage('" + URL_CROSS_ORIGIN_BASE + "img.jpg');\n");
    }

    /**
     * Image in the html source without event handlers; the violation is reported also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation img-src §§URL§§/img.jpg enforce")
    public void parserInserted_noHandler() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify("img-src 'self'", "<img src='" + URL_CROSS_ORIGIN_BASE + "img.jpg'>\n", "");
    }

    /**
     * Image created by new Image().
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"error", "violation img-src §§URL§§/img.jpg enforce"})
    public void imageConstructor() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify("img-src 'self'", "",
                "var img = new Image();\n"
                + "img.onload = function() { log('load'); };\n"
                + "img.onerror = function() { log('error'); };\n"
                + "img.src = '" + URL_CROSS_ORIGIN_BASE + "img.jpg';\n");
    }

    /**
     * The src of an allowed image is changed to a blocked url.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "error", "violation img-src §§URL§§/img.jpg enforce"})
    public void srcChanged() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setImage(URL_FIRST, "img.jpg");
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify("img-src 'self'", "",
                "var img = addImage('img.jpg');\n"
                + "img.onload = function() {\n"
                + "  log('load');\n"
                + "  img.onload = function() { log('load2'); };\n"
                + "  img.src = '" + URL_CROSS_ORIGIN_BASE + "img.jpg';\n"
                + "};\n");
    }

    /**
     * Properties of a blocked image.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"error true 0 16", "violation img-src §§URL§§/img.jpg enforce"},
            FF = {"error true 0 24", "violation img-src §§URL§§/img.jpg enforce"},
            FF_ESR = {"error true 0 24", "violation img-src §§URL§§/img.jpg enforce"})
    public void blockedImageProperties() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setImage(URL_CROSS_ORIGIN_BASE, "img.jpg");
        loadPageVerify("img-src 'self'", "", "var img = addImage('" + URL_CROSS_ORIGIN_BASE + "img.jpg');\n"
                + "img.onerror = function() {\n"
                + "  log('error ' + img.complete + ' ' + img.naturalWidth + ' ' + img.width);\n"
                + "};\n");
    }

    private static final String DATA_GIF =
            "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";

    private void setImage(final URL base, final String path) throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] bytes = IOUtils.toByteArray(is);
            getMockWebConnection().setResponse(new URL(base, path), bytes, 200, "ok", "image/jpg",
                    Collections.emptyList());
        }
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
