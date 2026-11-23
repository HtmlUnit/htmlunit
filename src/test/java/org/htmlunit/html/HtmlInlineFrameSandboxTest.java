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
package org.htmlunit.html;

import java.net.URL;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the sandbox attribute of {@link HtmlInlineFrame}.
 * <p>A sandboxed frame without allow-same-origin has an opaque origin; the frame reports
 * its results to the parent using postMessage().</p>
 *
 * @author Ronald Brill
 * @see <a href="https://html.spec.whatwg.org/multipage/iframe-embed-object.html#attr-iframe-sandbox">
 *     HTML Living Standard - sandbox</a>
 */
public class HtmlInlineFrameSandboxTest extends WebDriverTestCase {

    // functions of the frame: r() reports a value to the parent,
    // t() reports the result of the function or the name of the exception
    private static final String REPORT = "function r(v) { parent.postMessage(String(v), '*'); }"
            + "function t(f) { try { r(f()); } catch(e) { r(e.name); } }";

    /**
     * The src of some tests.
     *
     * @throws Exception if the setup fails
     */
    @BeforeEach
    public void setupSrc() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "src.html"),
                DOCTYPE_HTML + "<html><body><p>src</p></body></html>");
    }

    /**
     * Without allow-scripts the scripts of the frame are not executed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load", "true"})
    public void sandboxNoScripts() throws Exception {
        loadPageVerify("<iframe id='f' sandbox='' srcdoc=\"<script>" + REPORT
                + "r('script executed');</script>\""
                + " onload='log(\"load\"); log(this.contentDocument === null)'></iframe>\n");
    }

    /**
     * CSP allow-scripts: the frame has an opaque origin.
     * <p>HtmlUnit has no cross-origin restrictions for parent.document.</p>
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "about:srcdoc", "", "SecurityError", "SecurityError", "SecurityError", "done"})
    @HtmlUnitNYI(CHROME = {"null", "about:srcdoc", "", "SecurityError", "SecurityError", "", "done"},
            EDGE = {"null", "about:srcdoc", "", "SecurityError", "SecurityError", "", "done"},
            FF = {"null", "about:srcdoc", "", "SecurityError", "SecurityError", "", "done"},
            FF_ESR = {"null", "about:srcdoc", "", "SecurityError", "SecurityError", "", "done"})
    public void sandboxAllowScripts() throws Exception {
        loadPageVerify("<iframe id='f' sandbox='allow-scripts' srcdoc=\"<script>" + REPORT
                + "r(window.origin);"
                + "r(location.href);"
                + "t(function() { return document.domain; });"
                + "t(function() { return document.cookie; });"
                + "t(function() { return localStorage.length; });"
                + "t(function() { return parent.document.title; });"
                + "r('done');"
                + "</script>\"></iframe>\n");
    }

    /**
     * CSP allow-scripts allow-same-origin: the frame has the origin of the parent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "", "0", "", "done"})
    public void sandboxAllowScriptsSameOrigin() throws Exception {
        loadPageVerify("<iframe id='f' sandbox='allow-scripts allow-same-origin' srcdoc=\"<script>" + REPORT
                + "r(window.origin == parent.origin);"
                + "t(function() { return document.cookie; });"
                + "t(function() { return localStorage.length; });"
                + "t(function() { return parent.document.title; });"
                + "r('done');"
                + "</script>\"></iframe>\n");
    }

    /**
     * The parent can't access the document of a sandboxed frame (opaque origin).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "object"})
    public void sandboxParentAccessFrame() throws Exception {
        loadPageVerify("<iframe id='f' sandbox='allow-scripts' srcdoc='<p>hello</p>'"
                + " onload='log(this.contentDocument === null); log(typeof this.contentWindow)'></iframe>\n");
    }

    /**
     * The origin of a message from a sandboxed frame is 'null'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hi null")
    public void sandboxMessageOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  window.addEventListener('message', function(e) { log(e.data + ' ' + e.origin); });\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' sandbox='allow-scripts' srcdoc=\"<script>parent.postMessage('hi', '*');</script>\">"
            + "</iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * A sandboxed frame with src has an opaque origin also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null", "SecurityError", "done"})
    public void sandboxSrc() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "sandboxed.html"),
                DOCTYPE_HTML + "<html><body><script>" + REPORT
                + "r(window.origin);"
                + "t(function() { return document.cookie; });"
                + "r('done');"
                + "</script></body></html>");

        loadPageVerify("<iframe id='f' sandbox='allow-scripts' src='sandboxed.html'></iframe>\n");
    }

    /**
     * Without allow-popups window.open() returns null.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"true", "done"},
            FF = {"InvalidAccessError", "done"},
            FF_ESR = {"InvalidAccessError", "done"})
    public void sandboxPopups() throws Exception {
        loadPageVerify("<iframe id='f' sandbox='allow-scripts' srcdoc=\"<script>" + REPORT
                + "t(function() { return window.open('about:blank') === null; });"
                + "r('done');"
                + "</script>\"></iframe>\n");
    }

    /**
     * Without allow-top-navigation the frame can't navigate the top-level page.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SecurityError", "done"})
    public void sandboxTopNavigation() throws Exception {
        loadPageVerify("<iframe id='f' sandbox='allow-scripts' srcdoc=\"<script>" + REPORT
                + "t(function() { top.location.href = 'src.html'; return 'navigated'; });"
                + "r('done');"
                + "</script>\"></iframe>\n");
    }

    /**
     * Without allow-forms a form in the frame can't be submitted; the iframe logs the
     * location of its document at every load.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:srcdoc")
    public void sandboxForms() throws Exception {
        loadPageVerify("<iframe id='f' sandbox='allow-scripts allow-same-origin'"
                + " srcdoc=\"<form action='src.html'></form><script>document.forms[0].submit();</script>\""
                + " onload='log(this.contentWindow.location.href.replace(/.*\\//, \"\"))'></iframe>\n");
    }

    /**
     * The sandbox property is a DOMTokenList.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"allow-scripts", "allow-scripts", "true", "allow-scripts allow-forms", "true", "false", "0"})
    public void sandboxProperty() throws Exception {
        loadPageVerify("<iframe id='f' sandbox='allow-scripts'></iframe>\n"
                + "<script>\n"
                + "  var f = document.getElementById('f');\n"
                + "  log(f.sandbox);\n"
                + "  log(f.sandbox.value);\n"
                + "  log(f.sandbox.contains('allow-scripts'));\n"
                + "  f.sandbox.add('allow-forms');\n"
                + "  log(f.getAttribute('sandbox'));\n"
                + "  log(f.sandbox.supports('allow-popups'));\n"
                + "  try { log(f.sandbox.supports('foo')); } catch(e) { log(e.name); }\n"
                + "  log(document.createElement('iframe').sandbox.length);\n"
                + "</script>\n");
    }

    /**
     * Setting the sandbox attribute does not affect the loaded document.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"frame true", "true"})
    public void sandboxChangeAfterLoad() throws Exception {
        loadPageVerify("<iframe id='f' srcdoc=\"<script>parent.log('frame ' + (window.origin == parent.origin));"
                + "</script>\""
                + " onload='this.sandbox = \"\"; log(this.contentDocument !== null)'></iframe>\n");
    }

    private void loadPageVerify(final String body) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  window.addEventListener('message', function(e) { log(e.data); });\n"
            + "</script>\n"
            + "</head><body>\n"
            + body
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
