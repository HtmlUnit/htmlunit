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
 * Tests for the inheritance of the Content-Security-Policy; documents with a local scheme url
 * (about:blank, data:) inherit the policy of the document creating them.
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function,
 * logs all securitypolicyviolation events of the document and all messages posted to the window.
 * The function listen(doc, prefix) logs the violation events of other documents.
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyInheritanceTest extends WebDriverTestCase {

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "function listen(doc, prefix) {\n"
            + "  doc.addEventListener('securitypolicyviolation', function(e) {\n"
            + "    log(prefix + ' violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "  });\n"
            + "}\n"
            + "listen(document, 'top');\n"
            + "window.addEventListener('message', function(e) {\n"
            + "  log(e.data);\n"
            + "});\n"
            + "function testEval(win) {\n"
            + "  try {\n"
            + "    win.eval('1');\n"
            + "    log('eval ok');\n"
            + "  } catch(e) { log('eval ' + e.name); }\n"
            + "}\n"
            + "function testXhr(win, url) {\n"
            + "  try {\n"
            + "    var xhr = new win.XMLHttpRequest();\n"
            + "    xhr.open('GET', url, false);\n"
            + "    xhr.send();\n"
            + "    log('xhr ' + xhr.responseText);\n"
            + "  } catch(e) { log('xhr ' + e.name); }\n"
            + "}\n"
            + "function addFrame() {\n"
            + "  var frame = document.createElement('iframe');\n"
            + "  document.body.appendChild(frame);\n"
            + "  listen(frame.contentDocument, 'frame');\n"
            + "  return frame;\n"
            + "}\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Eval inside an about:blank iframe; the parent has no policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("eval ok")
    public void aboutBlank_noPolicy() throws Exception {
        loadPageVerify(null, "testEval(addFrame().contentWindow);\n");
    }

    /**
     * Eval inside an about:blank iframe; the policy of the parent blocks eval.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"eval EvalError", "frame violation script-src eval enforce"})
    public void aboutBlank_eval() throws Exception {
        loadPageVerify("script-src 'self'", "testEval(addFrame().contentWindow);\n");
    }

    /**
     * Eval inside an about:blank iframe; the policy of the parent allows eval.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("eval ok")
    public void aboutBlank_unsafeEval() throws Exception {
        loadPageVerify("script-src 'self' 'unsafe-eval'", "testEval(addFrame().contentWindow);\n");
    }

    /**
     * New Function() of the about:blank iframe; the policy of the parent blocks eval.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function EvalError", "frame violation script-src eval enforce"})
    public void aboutBlank_function() throws Exception {
        loadPageVerify("script-src 'self'",
                "var frame = addFrame();\n"
                + "try {\n"
                + "  new frame.contentWindow.Function('return 1');\n"
                + "  log('function ok');\n"
                + "} catch(e) { log('function ' + e.name); }\n");
    }

    /**
     * New Function() of the about:blank iframe; the frame has a more restrictive policy
     * (meta element added to the frame) than the parent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"frame function EvalError", "function ok", "frame violation script-src eval enforce"})
    public void aboutBlank_functionMetaInFrame() throws Exception {
        loadPageVerify("script-src 'self' 'unsafe-eval'",
                "var frame = addFrame();\n"
                + "var meta = frame.contentDocument.createElement('meta');\n"
                + "meta.httpEquiv = 'Content-Security-Policy';\n"
                + "meta.content = \"script-src 'self'\";\n"
                + "frame.contentDocument.head.appendChild(meta);\n"
                + "try {\n"
                + "  new frame.contentWindow.Function('return 1');\n"
                + "  log('frame function ok');\n"
                + "} catch(e) { log('frame function ' + e.name); }\n"
                + "try {\n"
                + "  new Function('return 1');\n"
                + "  log('function ok');\n"
                + "} catch(e) { log('function ' + e.name); }\n");
    }

    /**
     * Javascript: url as src of an iframe; the resulting document keeps the inherited policy.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("js url script")
    public void javascriptUrl_inlineScript() throws Exception {
        loadPageVerify("script-src 'self' 'unsafe-inline'",
                "var frame = document.createElement('iframe');\n"
                + "frame.src = \"javascript:'<script>parent.log(\\\"js url script\\\");<\\/script>'\";\n"
                + "document.body.appendChild(frame);\n");
    }

    /**
     * Inline script written into an about:blank iframe; the listener is registered
     * after document.close() because document.open() removes the listeners.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("frame violation script-src-elem inline enforce")
    public void aboutBlank_inlineScript() throws Exception {
        loadPageVerify("script-src 'self'",
                "var frame = document.createElement('iframe');\n"
                + "document.body.appendChild(frame);\n"
                + "var doc = frame.contentDocument;\n"
                + "doc.open();\n"
                + "doc.write('<html><body><script>parent.log(\"inline\");<\\/script></body></html>');\n"
                + "doc.close();\n"
                + "listen(frame.contentDocument, 'frame');\n");
    }

    /**
     * Inline script appended to an about:blank iframe.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("frame violation script-src-elem inline enforce")
    public void aboutBlank_inlineScriptAppended() throws Exception {
        loadPageVerify("script-src 'self'",
                "var frame = addFrame();\n"
                + "var doc = frame.contentDocument;\n"
                + "var script = doc.createElement('script');\n"
                + "script.text = 'parent.log(\"inline\");';\n"
                + "doc.body.appendChild(script);\n");
    }

    /**
     * 'self' of the inherited policy is the origin of the parent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xhr data")
    public void aboutBlank_xhrSelf() throws Exception {
        getMockWebConnection().setResponse(new URL(URL_FIRST, "data.txt"), "data", MimeType.TEXT_PLAIN);
        loadPageVerify("connect-src 'self'",
                "testXhr(addFrame().contentWindow, '" + URL_FIRST + "data.txt');\n");
    }

    /**
     * Cross origin xhr from an about:blank iframe.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"xhr NetworkError", "frame violation connect-src §§URL§§/data.txt enforce"})
    public void aboutBlank_xhrCrossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "data.txt"), "data",
                MimeType.TEXT_PLAIN);
        loadPageVerify("connect-src 'self'",
                "testXhr(addFrame().contentWindow, '" + URL_CROSS_ORIGIN_BASE + "data.txt');\n");
    }

    /**
     * Report only policies are inherited also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"eval ok", "frame violation script-src eval report"})
    public void aboutBlank_reportOnly() throws Exception {
        loadPageVerify("Content-Security-Policy-Report-Only", "script-src 'self'", "",
                "testEval(addFrame().contentWindow);\n");
    }

    /**
     * A policy defined by a meta element of the parent is inherited.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"eval EvalError", "frame violation script-src eval enforce"})
    public void aboutBlank_meta() throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"script-src 'self'\">\n",
                "testEval(addFrame().contentWindow);\n");
    }

    /**
     * A meta element added to the about:blank iframe does not change the policy of the parent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"eval EvalError", "eval ok", "frame violation script-src eval enforce"})
    public void aboutBlank_metaInFrame() throws Exception {
        loadPageVerify("script-src 'self' 'unsafe-eval'",
                "var frame = addFrame();\n"
                + "var meta = frame.contentDocument.createElement('meta');\n"
                + "meta.httpEquiv = 'Content-Security-Policy';\n"
                + "meta.content = \"script-src 'self'\";\n"
                + "frame.contentDocument.head.appendChild(meta);\n"
                + "testEval(frame.contentWindow);\n"
                + "testEval(window);\n");
    }

    /**
     * Window opened by window.open('') inherits the policy of the opener.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"eval EvalError", "popup violation script-src eval enforce"})
    public void windowOpen_eval() throws Exception {
        loadPageVerify("script-src 'self'",
                "var win = window.open('');\n"
                + "listen(win.document, 'popup');\n"
                + "testEval(win);\n"
                + "win.close();\n");
    }

    /**
     * Window opened by window.open('about:blank') inherits the policy of the opener.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"eval EvalError", "popup violation script-src eval enforce"})
    public void windowOpenAboutBlank_eval() throws Exception {
        loadPageVerify("script-src 'self'",
                "var win = window.open('about:blank');\n"
                + "listen(win.document, 'popup');\n"
                + "testEval(win);\n"
                + "win.close();\n");
    }

    /**
     * A data: url iframe; is the policy inherited?
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({})
    public void dataUrl_inlineScript() throws Exception {
        loadPageVerify("script-src 'self'; frame-src data:",
                "var frame = document.createElement('iframe');\n"
                + "frame.src = 'data:text/html,<script>parent.postMessage(\"data script\", \"*\");<\\/script>';\n"
                + "document.body.appendChild(frame);\n");
    }

    /**
     * A http iframe does not inherit the policy of the parent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("frame eval ok")
    public void httpFrame_notInherited() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + "  try {\n"
                + "    eval('1');\n"
                + "    parent.postMessage('frame eval ok', '*');\n"
                + "  } catch(e) { parent.postMessage('frame eval ' + e.name, '*'); }\n"
                + "</script>\n"
                + "</head><body></body></html>";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "frame.html"), html, MimeType.TEXT_HTML);

        loadPageVerify("script-src 'self' 'unsafe-inline'",
                "var frame = document.createElement('iframe');\n"
                + "frame.src = 'frame.html';\n"
                + "document.body.appendChild(frame);\n");
    }

    /**
     * Nested about:blank iframes; the policy is inherited along the chain.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"eval EvalError", "inner violation script-src eval enforce"})
    public void aboutBlank_nested() throws Exception {
        loadPageVerify("script-src 'self'",
                "var frame = addFrame();\n"
                + "var inner = frame.contentDocument.createElement('iframe');\n"
                + "frame.contentDocument.body.appendChild(inner);\n"
                + "listen(inner.contentDocument, 'inner');\n"
                + "testEval(inner.contentWindow);\n");
    }

    private void loadPageVerify(final String policy, final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, "", testJs);
    }

    /**
     * Loads a page containing the harness script, the given head and the test script;
     * the page is delivered with the given policy header.
     */
    private void loadPageVerify(final String header, final String policy,
            final String head, final String testJs) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script src='harness.js'></script>\n"
            + head
            + "</head>\n"
            + "<body>\n"
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
