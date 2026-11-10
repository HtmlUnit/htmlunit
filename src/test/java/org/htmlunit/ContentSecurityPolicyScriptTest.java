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
import static java.nio.charset.StandardCharsets.UTF_8;

import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directives controlling scripts
 * (script-src, script-src-elem, default-src, nonces, hashes, 'unsafe-inline', 'unsafe-eval',
 * 'strict-dynamic') and the securitypolicyviolation event.
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function
 * and logs all securitypolicyviolation events. The policies used by the tests
 * allow this script ('self', {@link #HARNESS_SRC} or 'nonce-harness').
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyScriptTest extends WebDriverTestCase {

    private static final String INLINE_SCRIPT = "<script>log('inline');</script>\n";

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    /** Source expression matching only the harness script. */
    private static final String HARNESS_SRC = "http://localhost:" + PORT + "/harness.js";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  log('violation ' + e.violatedDirective + ' ' + e.blockedURI);\n"
            + "});\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy all scripts are executed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void noPolicy() throws Exception {
        loadPageVerify(null, "", INLINE_SCRIPT);
    }

    /**
     * Inline scripts are blocked by script-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void inline_scriptSrcSelf() throws Exception {
        loadPageVerify("script-src 'self'", "", INLINE_SCRIPT);
    }

    /**
     * Inline scripts are allowed by 'unsafe-inline'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void inline_scriptSrcUnsafeInline() throws Exception {
        loadPageVerify("script-src 'self' 'unsafe-inline'", "", INLINE_SCRIPT);
    }

    /**
     * The default-src directive is used if there is no script-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void inline_defaultSrcSelf() throws Exception {
        loadPageVerify("default-src 'self'", "", INLINE_SCRIPT);
    }

    /**
     * The script-src directive overrides default-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void inline_defaultSrcUnsafeInlineScriptSrcSelf() throws Exception {
        loadPageVerify("default-src 'self' 'unsafe-inline'; script-src 'self'", "", INLINE_SCRIPT);
    }

    /**
     * The script-src-elem directive overrides script-src for script elements.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void inline_scriptSrcElem() throws Exception {
        loadPageVerify("script-src 'self'; script-src-elem 'self' 'unsafe-inline'", "", INLINE_SCRIPT);
    }

    /**
     * Inline script with matching nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void inline_nonce() throws Exception {
        loadPageVerify("script-src 'self' 'nonce-abc123'", "",
                "<script nonce='abc123'>log('inline');</script>\n");
    }

    /**
     * Inline script with an invalid nonce (not base64); the source expression is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void inline_nonceInvalid() throws Exception {
        loadPageVerify("script-src 'self' 'nonce-abc!d'", "",
                "<script nonce='abc!d'>log('inline');</script>\n");
    }

    /**
     * Inline script with wrong nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void inline_nonceWrong() throws Exception {
        loadPageVerify("script-src 'self' 'nonce-abc123'", "",
                "<script nonce='xyz'>log('inline');</script>\n");
    }

    /**
     * 'unsafe-inline' is ignored if the policy contains a nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void inline_nonceDisablesUnsafeInline() throws Exception {
        loadPageVerify("script-src 'self' 'unsafe-inline' 'nonce-abc123'", "", INLINE_SCRIPT);
    }

    /**
     * Inline script with matching hash.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void inline_hash() throws Exception {
        final String script = "log('inline');";
        loadPageVerify("script-src 'self' '" + sha256(script) + "'", "",
                "<script>" + script + "</script>\n");
    }

    /**
     * Inline script with not matching hash (whitespace is part of the hash).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void inline_hashWrong() throws Exception {
        final String script = "log('inline');";
        loadPageVerify("script-src 'self' '" + sha256(script) + "'", "",
                "<script> " + script + "</script>\n");
    }

    /**
     * Inline script created by an allowed script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"external", "violation script-src-elem inline"})
    public void inline_dynamicallyCreated() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"),
                "var s = document.createElement('script');\n"
                + "s.text = \"log('dynamic');\";\n"
                + "document.body.appendChild(s);\n"
                + "log('external');\n",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self'", "", "<script src='test.js'></script>\n");
    }

    /**
     * External script from the same origin.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void external_self() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"), "log('external');",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self'", "", "<script src='test.js'></script>\n");
    }

    /**
     * External script from a different origin, 'self' only.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22223/test.js")
    public void external_selfCrossOrigin() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "test.js"), "log('external');",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self'", "",
                "<script src='" + URL_CROSS_ORIGIN_BASE + "test.js'></script>\n");
    }

    /**
     * External script from a different origin listed as host source.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void external_hostSource() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "test.js"), "log('external');",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self' http://localhost:" + PORT2, "",
                "<script src='" + URL_CROSS_ORIGIN_BASE + "test.js'></script>\n");
    }

    /**
     * External script, script-src 'none' (the harness script is allowed by its url).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem http://localhost:22222/test.js")
    public void external_none() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"), "log('external');",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src " + HARNESS_SRC, "", "<script src='test.js'></script>\n");
    }

    /**
     * External script from a different origin with matching nonce.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("external")
    public void external_nonce() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "test.js"), "log('external');",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self' 'nonce-abc123'", "",
                "<script src='" + URL_CROSS_ORIGIN_BASE + "test.js' nonce='abc123'></script>\n");
    }

    /**
     * 'strict-dynamic': scripts created by a trusted script are allowed,
     * parser inserted scripts without nonce are blocked.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation script-src-elem http://localhost:22222/parser.js", "dynamic"})
    public void external_strictDynamic() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "loader.js"),
                "var s = document.createElement('script');\n"
                + "s.src = 'dynamic.js';\n"
                + "document.head.appendChild(s);\n",
                MimeType.TEXT_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "dynamic.js"), "log('dynamic');",
                MimeType.TEXT_JAVASCRIPT);
        getMockWebConnection().setResponse(new URL(URL_FIRST, "parser.js"), "log('parser');",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self' 'nonce-harness' 'nonce-abc123' 'strict-dynamic'", "",
                "<script src='parser.js'></script>\n"
                + "<script src='loader.js' nonce='abc123'></script>\n");
    }

    /**
     * Event handler attributes are blocked by script-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-attr inline")
    public void eventHandlerAttribute_scriptSrcSelf() throws Exception {
        final WebDriver driver = loadPage("script-src 'self'", "",
                "<button id='btn' onclick=\"log('click')\">click</button>\n");
        driver.findElement(By.id("btn")).click();
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Event handler attributes are allowed by 'unsafe-inline'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("click")
    public void eventHandlerAttribute_unsafeInline() throws Exception {
        final WebDriver driver = loadPage("script-src 'self' 'unsafe-inline'", "",
                "<button id='btn' onclick=\"log('click')\">click</button>\n");
        driver.findElement(By.id("btn")).click();
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Event handler attributes are allowed by 'unsafe-hashes' and the hash.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("click")
    public void eventHandlerAttribute_unsafeHashes() throws Exception {
        final String handler = "log('click')";
        final WebDriver driver = loadPage("script-src 'self' 'unsafe-hashes' '" + sha256(handler) + "'", "",
                "<button id='btn' onclick=\"" + handler + "\">click</button>\n");
        driver.findElement(By.id("btn")).click();
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Event handlers set by an allowed script are not affected.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("click")
    public void eventHandlerProperty() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"),
                "document.getElementById('btn').onclick = function() { log('click'); };",
                MimeType.TEXT_JAVASCRIPT);
        final WebDriver driver = loadPage("script-src 'self'", "",
                "<button id='btn'>click</button>\n"
                + "<script src='test.js'></script>\n");
        driver.findElement(By.id("btn")).click();
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Javascript urls are blocked by script-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void javascriptUrl() throws Exception {
        final WebDriver driver = loadPage("script-src 'self'", "",
                "<a id='lnk' href=\"javascript:log('href')\">click</a>\n");
        driver.findElement(By.id("lnk")).click();
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Javascript urls are allowed by 'unsafe-inline'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("href")
    public void javascriptUrl_unsafeInline() throws Exception {
        final WebDriver driver = loadPage("script-src 'self' 'unsafe-inline'", "",
                "<a id='lnk' href=\"javascript:log('href')\">click</a>\n");
        driver.findElement(By.id("lnk")).click();
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * Calls of eval(), new Function() and setTimeout(string) are blocked by script-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"EvalError",
             "EvalError",
             "violation script-src eval",
             "violation script-src eval",
             "violation script-src eval",
             "timeout function"})
    public void eval_scriptSrcSelf() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"),
                "try { eval(\"log('eval')\"); } catch(e) { logEx(e); }\n"
                + "try { new Function(\"log('function')\")(); } catch(e) { logEx(e); }\n"
                + "setTimeout(\"log('timeout string')\", 0);\n"
                + "setTimeout(function() { log('timeout function'); }, 10);\n",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self'", "", "<script src='test.js'></script>\n");
    }

    /**
     * Calls of eval(), new Function() and setTimeout(string) are allowed by 'unsafe-eval'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"eval", "function", "timeout string", "timeout function"})
    public void eval_unsafeEval() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"),
                "try { eval(\"log('eval')\"); } catch(e) { logEx(e); }\n"
                + "try { new Function(\"log('function')\")(); } catch(e) { logEx(e); }\n"
                + "setTimeout(\"log('timeout string')\", 0);\n"
                + "setTimeout(function() { log('timeout function'); }, 10);\n",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self' 'unsafe-eval'", "", "<script src='test.js'></script>\n");
    }

    /**
     * Calling eval() is allowed if the policy has no script-src/default-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"eval", "function", "timeout string", "timeout function"})
    public void eval_noScriptSrc() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"),
                "try { eval(\"log('eval')\"); } catch(e) { logEx(e); }\n"
                + "try { new Function(\"log('function')\")(); } catch(e) { logEx(e); }\n"
                + "setTimeout(\"log('timeout string')\", 0);\n"
                + "setTimeout(function() { log('timeout function'); }, 10);\n",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("img-src 'self'", "", "<script src='test.js'></script>\n");
    }

    /**
     * A Content-Security-Policy-Report-Only policy does not block, but reports.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"inline", "violation script-src-elem inline"})
    public void reportOnly() throws Exception {
        loadPageVerify(HEADER_REPORT_ONLY, "script-src 'self'", "", INLINE_SCRIPT);
    }

    /**
     * Policy defined by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void meta() throws Exception {
        loadPageVerify(null,
                "<meta http-equiv='Content-Security-Policy' content=\"script-src 'self'\">\n",
                INLINE_SCRIPT);
    }

    /**
     * A meta policy is not applied to scripts executed before.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"before", "violation script-src-elem inline"})
    public void metaAfterScript() throws Exception {
        loadPageVerify(null,
                "<script>log('before');</script>\n"
                + "<meta http-equiv='Content-Security-Policy' content=\"script-src 'self'\">\n",
                "<script>log('after');</script>\n");
    }

    /**
     * A meta policy in the body is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void metaInBody() throws Exception {
        loadPageVerify(null, "",
                "<meta http-equiv='Content-Security-Policy' content=\"script-src 'self'\">\n"
                + INLINE_SCRIPT);
    }

    /**
     * Content-Security-Policy-Report-Only is not supported in meta elements.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void metaReportOnly() throws Exception {
        loadPageVerify(null,
                "<meta http-equiv='Content-Security-Policy-Report-Only' content=\"script-src 'self'\">\n",
                INLINE_SCRIPT);
    }

    /**
     * The sandbox directive is ignored when delivered via a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("inline")
    public void metaSandbox() throws Exception {
        loadPageVerify(null,
                "<meta http-equiv='Content-Security-Policy' content=\"sandbox\">\n",
                INLINE_SCRIPT);
    }

    /**
     * The sandbox directive is ignored when delivered via a meta element,
     * the other directives are still enforced.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void metaSandboxAndScriptSrc() throws Exception {
        loadPageVerify(null,
                "<meta http-equiv='Content-Security-Policy' content=\"sandbox; script-src 'self'\">\n",
                INLINE_SCRIPT);
    }

    /**
     * Commas in the content of a meta element; Chrome splits the content into
     * two policies, Firefox parses one policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "violation script-src-elem inline",
            FF = "inline",
            FF_ESR = "inline")
    public void metaComma() throws Exception {
        loadPageVerify(null,
                "<meta http-equiv='Content-Security-Policy' "
                        + "content=\"script-src 'self' 'unsafe-inline' ,script-src 'self'\">\n",
                INLINE_SCRIPT);
    }

    /**
     * Commas in the content of a meta element (without whitespace).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void metaCommaNoWhitespace() throws Exception {
        loadPageVerify(null,
                "<meta http-equiv='Content-Security-Policy' "
                        + "content=\"script-src 'self' 'unsafe-inline',script-src 'self'\">\n",
                INLINE_SCRIPT);
    }

    /**
     * Commas in the content of a meta element; the second part is no policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void metaCommaTrailing() throws Exception {
        loadPageVerify(null,
                "<meta http-equiv='Content-Security-Policy' content=\"script-src 'self',\">\n",
                INLINE_SCRIPT);
    }

    /**
     * Header and meta policy, both have to allow the script.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation script-src-elem inline")
    public void headerAndMeta() throws Exception {
        loadPageVerify("script-src 'self' 'unsafe-inline'",
                "<meta http-equiv='Content-Security-Policy' content=\"script-src 'self'\">\n",
                INLINE_SCRIPT);
    }

    /**
     * Properties of the securitypolicyviolation event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation script-src-elem inline",
             "script-src-elem script-src-elem inline enforce log('inline'); 7"})
    public void violationEvent() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "test.js"),
                "document.addEventListener('securitypolicyviolation', function(e) {\n"
                + "  log(e.violatedDirective + ' ' + e.effectiveDirective + ' ' + e.blockedURI"
                + " + ' ' + e.disposition + ' ' + e.sample + ' ' + e.lineNumber);\n"
                + "});\n",
                MimeType.TEXT_JAVASCRIPT);
        loadPageVerify("script-src 'self' 'report-sample'", "<script src='test.js'></script>\n",
                INLINE_SCRIPT);
    }

    private void loadPageVerify(final String policy, final String head, final String body) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, head, body);
    }

    private void loadPageVerify(final String header, final String policy,
            final String head, final String body) throws Exception {
        final WebDriver driver = loadPage(header, policy, head, body);
        verifyWindowName2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    private WebDriver loadPage(final String policy, final String head, final String body) throws Exception {
        return loadPage(HttpHeader.CONTENT_SECURIRY_POLICY, policy, head, body);
    }

    /**
     * Loads a page containing the harness script, the given head and the given body;
     * the page is delivered with the given policy header.
     */
    private WebDriver loadPage(final String header, final String policy,
            final String head, final String body) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script src='harness.js' nonce='harness'></script>\n"
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
        getMockWebConnection().setResponse(new URL(URL_FIRST, "harness.js"), HARNESS_JS,
                MimeType.TEXT_JAVASCRIPT);

        return loadPage2(URL_FIRST, ISO_8859_1);
    }

    /**
     * Returns the CSP hash source (without quotes) of the given script.
     */
    private static String sha256(final String script) throws Exception {
        final byte[] digest = MessageDigest.getInstance("SHA-256").digest(script.getBytes(UTF_8));
        return "sha256-" + Base64.getEncoder().encodeToString(digest);
    }
}
