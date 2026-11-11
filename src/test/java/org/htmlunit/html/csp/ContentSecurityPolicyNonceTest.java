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
 * Tests for the nonce IDL attribute and the nonce hiding of the Content-Security-Policy.
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and collects all securitypolicyviolation events. The function info(id) logs the nonce
 * property and the nonce attribute of an element. The violations are logged at the end
 * (200ms after the load event) because the browsers fire them async.
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyNonceTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";
    private static final String POLICY = "script-src 'self' 'nonce-abc'; style-src 'self' 'nonce-abc'";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "var violations = [];\n"
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  violations.push('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n"
            + "function info(el) {\n"
            + "  if (typeof el == 'string') { el = document.getElementById(el); }\n"
            + "  log(el.nonce + ' [' + el.getAttribute('nonce') + ']');\n"
            + "}\n"
            + "window.addEventListener('load', function() {\n"
            + "  setTimeout(function() {\n"
            + "    for (var i = 0; i < violations.length; i++) { log(violations[i]); }\n"
            + "    log('done');\n"
            + "  }, 200);\n"
            + "});\n";

    private static final String INLINE = "<script id='s' nonce='abc'>log('inline');</script>\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy the nonce attribute is not hidden.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", "abc [abc]", "done"})
    public void noPolicy() throws Exception {
        loadPageVerify(null, INLINE, "info('s');\n");
    }

    /**
     * With a policy delivered by a header the nonce attribute is hidden.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", "abc []", "done"})
    public void hidden() throws Exception {
        loadPageVerify(POLICY, INLINE, "info('s');\n");
    }

    /**
     * The nonce attribute is hidden also for report only policies (not in Firefox).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"inline", "abc []", "done"},
            FF = {"inline", "abc [abc]", "done"},
            FF_ESR = {"inline", "abc [abc]", "done"})
    public void reportOnly() throws Exception {
        loadPageVerify(HEADER_REPORT_ONLY, POLICY, "", INLINE, "info('s');\n");
    }

    /**
     * Policy delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", "abc [abc]", "done"})
    public void meta() throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"" + POLICY + "\">\n",
                INLINE, "info('s');\n");
    }

    /**
     * The nonce of other elements.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc []", "done"})
    public void div() throws Exception {
        loadPageVerify(POLICY, "<div id='d' nonce='abc'></div>\n", "info('d');\n");
    }

    /**
     * The nonce of svg elements.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc []", "done"})
    public void svg() throws Exception {
        loadPageVerify(POLICY, "<svg><rect id='r' nonce='abc'/></svg>\n", "info('r');\n");
    }

    /**
     * Setting the nonce property does not change the attribute.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", "xyz []", "done"})
    public void setProperty() throws Exception {
        loadPageVerify(POLICY, INLINE,
                "var s = document.getElementById('s');\n"
                + "s.nonce = 'xyz';\n"
                + "info(s);\n");
    }

    /**
     * Setting the nonce attribute after the insertion updates the property; the attribute is not hidden.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", "xyz [xyz]", "done"})
    public void setAttribute() throws Exception {
        loadPageVerify(POLICY, INLINE,
                "var s = document.getElementById('s');\n"
                + "s.setAttribute('nonce', 'xyz');\n"
                + "info(s);\n");
    }

    /**
     * Removing the nonce attribute.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", " [null]", "done"})
    public void removeAttribute() throws Exception {
        loadPageVerify(POLICY, INLINE,
                "var s = document.getElementById('s');\n"
                + "s.removeAttribute('nonce');\n"
                + "info(s);\n");
    }

    /**
     * The nonce of a dynamic script set by the property.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"dynamic", "abc [null]", "done"})
    public void dynamicProperty() throws Exception {
        loadPageVerify(POLICY, "",
                "var s = document.createElement('script');\n"
                + "s.nonce = 'abc';\n"
                + "s.text = \"log('dynamic');\";\n"
                + "document.body.appendChild(s);\n"
                + "info(s);\n");
    }

    /**
     * The nonce of a dynamic script set by the attribute; the attribute is hidden after the insertion.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc [abc]", "dynamic", "abc []", "done"})
    public void dynamicAttribute() throws Exception {
        loadPageVerify(POLICY, "",
                "var s = document.createElement('script');\n"
                + "s.setAttribute('nonce', 'abc');\n"
                + "info(s);\n"
                + "s.text = \"log('dynamic');\";\n"
                + "document.body.appendChild(s);\n"
                + "info(s);\n");
    }

    /**
     * Dynamic script with a wrong nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"wrong [null]", "violation script-src-elem inline enforce", "done"})
    public void dynamicWrongNonce() throws Exception {
        loadPageVerify(POLICY, "",
                "var s = document.createElement('script');\n"
                + "s.nonce = 'wrong';\n"
                + "s.text = \"log('dynamic');\";\n"
                + "document.body.appendChild(s);\n"
                + "info(s);\n");
    }

    /**
     * The clone of an element keeps the nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc []", "abc []", "done"})
    public void cloneNode() throws Exception {
        loadPageVerify(POLICY, "<div id='d' nonce='abc'></div>\n",
                "var c = document.getElementById('d').cloneNode(true);\n"
                + "info(c);\n"
                + "document.body.appendChild(c);\n"
                + "info(c);\n");
    }

    /**
     * Elements having an attribute containing '&lt;script' are not nonceable.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc []", "violation script-src-elem inline enforce", "done"})
    public void notNonceable() throws Exception {
        loadPageVerify(POLICY, "<script id='s' nonce='abc' data-x='<script'>log('inline');</script>\n",
                "info('s');\n");
    }

    /**
     * Style element with nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"abc []", "rgb(255, 0, 0)", "done"})
    public void style() throws Exception {
        loadPageVerify(POLICY, "<style id='st' nonce='abc'>#d { color: red; }</style>\n"
                + "<div id='d'>d</div>\n",
                "info('st');\n"
                + "log(getComputedStyle(document.getElementById('d')).color);\n");
    }

    private void loadPageVerify(final String policy, final String body, final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, "", body, testJs);
    }

    /**
     * Loads a page containing the harness script, the given head and body and the test script;
     * the page is delivered with the given policy header.
     */
    private void loadPageVerify(final String header, final String policy,
            final String head, final String body, final String testJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + head
            + "<script src='" + URL_FIRST + "harness.js'></script>\n"
            + "</head>\n"
            + "<body>\n"
            + body
            + "<script src='" + URL_FIRST + "test.js'></script>\n"
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
