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
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for the Content-Security-Policy directive frame-src (and the fallbacks child-src and default-src).
 * <p>
 * Every page includes the external script 'harness.js'; it defines the log() function,
 * logs all securitypolicyviolation events and all messages posted to the window.
 * The test code is placed in the external script 'test.js'. All policies allow these scripts.
 * The pages loaded into the frames post 'loaded' and their path to the parent window.
 * </p>
 * <p>
 * Known differences (HtmlUnitNYI): HtmlUnit delivers the message posted by the frame before the load
 * event of the frame, the content document of cross origin and data: frames is accessible,
 * and like Chrome HtmlUnit fires the load event for blocked frames (Firefox does not).
 * </p>
 *
 * @author Ronald Brill
 */
public class ContentSecurityPolicyFrameTest extends WebDriverTestCase {

    private static final String HEADER_REPORT_ONLY = "Content-Security-Policy-Report-Only";

    private static final String HARNESS_JS = LOG_WINDOW_NAME_FUNCTION
            + "document.addEventListener('securitypolicyviolation', function(e) {\n"
            + "  log('violation ' + e.violatedDirective + ' ' + e.blockedURI + ' ' + e.disposition);\n"
            + "});\n"
            + "window.addEventListener('message', function(e) {\n"
            + "  log(e.data);\n"
            + "});\n"
            + "function addFrame(src) {\n"
            + "  var frame = document.createElement('iframe');\n"
            + "  frame.onload = function() {\n"
            + "    var doc;\n"
            + "    try {\n"
            + "      doc = frame.contentDocument == null ? 'null' : 'doc';\n"
            + "    } catch(e) { doc = 'exception'; }\n"
            + "    log('load ' + doc);\n"
            + "  };\n"
            + "  frame.src = src;\n"
            + "  document.body.appendChild(frame);\n"
            + "  return frame;\n"
            + "}\n";

    @Override
    protected boolean needThreeConnections() {
        return true;
    }

    /**
     * Without a policy all frames are allowed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load null", "loaded /frame.html"})
    @HtmlUnitNYI(CHROME = {"loaded /frame.html", "load doc"},
            EDGE = {"loaded /frame.html", "load doc"},
            FF = {"loaded /frame.html", "load doc"},
            FF_ESR = {"loaded /frame.html", "load doc"})
    public void noPolicy() throws Exception {
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify(null, addFrameJs(URL_CROSS_ORIGIN_BASE + "frame.html"));
    }

    /**
     * Same origin frame, frame-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load doc", "loaded /frame.html"})
    @HtmlUnitNYI(CHROME = {"loaded /frame.html", "load doc"},
            EDGE = {"loaded /frame.html", "load doc"},
            FF = {"loaded /frame.html", "load doc"},
            FF_ESR = {"loaded /frame.html", "load doc"})
    public void frameSrcSelf_sameOrigin() throws Exception {
        setFrameResponse(URL_FIRST, "frame.html");
        loadPageVerify("frame-src 'self'", addFrameJs("frame.html"));
    }

    /**
     * Cross origin frame, frame-src 'self'.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src §§URL§§ enforce", "load null"},
            FF = "violation frame-src §§URL§§ enforce",
            FF_ESR = "violation frame-src §§URL§§ enforce")
    @HtmlUnitNYI(FF = {"violation frame-src §§URL§§ enforce", "load null"},
            FF_ESR = {"violation frame-src §§URL§§ enforce", "load null"})
    public void frameSrcSelf_crossOrigin() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify("frame-src 'self'", addFrameJs(URL_CROSS_ORIGIN_BASE + "frame.html"));
    }

    /**
     * Cross origin frame, the origin is listed in frame-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load null", "loaded /frame.html"})
    @HtmlUnitNYI(CHROME = {"loaded /frame.html", "load doc"},
            EDGE = {"loaded /frame.html", "load doc"},
            FF = {"loaded /frame.html", "load doc"},
            FF_ESR = {"loaded /frame.html", "load doc"})
    public void frameSrcHostSource() throws Exception {
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify("frame-src 'self' http://localhost:" + PORT2,
                addFrameJs(URL_CROSS_ORIGIN_BASE + "frame.html"));
    }

    /**
     * The directive frame-src 'none' blocks also same origin frames.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src §§URL§§/frame.html enforce", "load null"},
            FF = "violation frame-src §§URL§§/frame.html enforce",
            FF_ESR = "violation frame-src §§URL§§/frame.html enforce")
    @HtmlUnitNYI(FF = {"violation frame-src §§URL§§/frame.html enforce", "load null"},
            FF_ESR = {"violation frame-src §§URL§§/frame.html enforce", "load null"})
    public void frameSrcNone() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT);
        setFrameResponse(URL_FIRST, "frame.html");
        loadPageVerify("frame-src 'none'", addFrameJs("frame.html"));
    }

    /**
     * Child-src is the fallback for frame-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src §§URL§§ enforce", "load null"},
            FF = "violation frame-src §§URL§§ enforce",
            FF_ESR = "violation frame-src §§URL§§ enforce")
    @HtmlUnitNYI(FF = {"violation frame-src §§URL§§ enforce", "load null"},
            FF_ESR = {"violation frame-src §§URL§§ enforce", "load null"})
    public void childSrcFallback() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify("child-src 'self'", addFrameJs(URL_CROSS_ORIGIN_BASE + "frame.html"));
    }

    /**
     * Default-src is the fallback for frame-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src §§URL§§ enforce", "load null"},
            FF = "violation frame-src §§URL§§ enforce",
            FF_ESR = "violation frame-src §§URL§§ enforce")
    @HtmlUnitNYI(FF = {"violation frame-src §§URL§§ enforce", "load null"},
            FF_ESR = {"violation frame-src §§URL§§ enforce", "load null"})
    public void defaultSrcFallback() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify("default-src 'self'", addFrameJs(URL_CROSS_ORIGIN_BASE + "frame.html"));
    }

    /**
     * Frame-src wins over child-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"load null", "loaded /frame.html"})
    @HtmlUnitNYI(CHROME = {"loaded /frame.html", "load doc"},
            EDGE = {"loaded /frame.html", "load doc"},
            FF = {"loaded /frame.html", "load doc"},
            FF_ESR = {"loaded /frame.html", "load doc"})
    public void frameSrcOverridesChildSrc() throws Exception {
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify("child-src 'none'; frame-src *", addFrameJs(URL_CROSS_ORIGIN_BASE + "frame.html"));
    }

    /**
     * Report only policies do not block the frame.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src §§URL§§ report",
                       "violation frame-src §§URL§§ report",
                       "load null",
                       "loaded /frame.html"},
            FF = {"violation frame-src §§URL§§ report", "load null", "loaded /frame.html"},
            FF_ESR = {"violation frame-src §§URL§§ report", "load null", "loaded /frame.html"})
    @HtmlUnitNYI(CHROME = {"violation frame-src §§URL§§ report", "loaded /frame.html", "load doc"},
            EDGE = {"violation frame-src §§URL§§ report", "loaded /frame.html", "load doc"},
            FF = {"violation frame-src §§URL§§ report", "loaded /frame.html", "load doc"},
            FF_ESR = {"violation frame-src §§URL§§ report", "loaded /frame.html", "load doc"})
    public void reportOnly() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify(HEADER_REPORT_ONLY, "frame-src 'self'", "",
                "", addFrameJs(URL_CROSS_ORIGIN_BASE + "frame.html"));
    }

    /**
     * The policy can be delivered by a meta element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src §§URL§§ enforce", "load null"},
            FF = "violation frame-src §§URL§§ enforce",
            FF_ESR = "violation frame-src §§URL§§ enforce")
    @HtmlUnitNYI(FF = {"violation frame-src §§URL§§ enforce", "load null"},
            FF_ESR = {"violation frame-src §§URL§§ enforce", "load null"})
    public void meta() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, null,
                "<meta http-equiv='Content-Security-Policy' content=\"frame-src 'self'\">\n",
                "", addFrameJs(URL_CROSS_ORIGIN_BASE + "frame.html"));
    }

    /**
     * A frame defined in the html source (parser inserted).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("violation frame-src §§URL§§ enforce")
    public void parserInserted() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, "frame-src 'self'", "",
                "<iframe src='" + URL_CROSS_ORIGIN_BASE + "frame.html'></iframe>\n", "");
    }

    /**
     * A frame of a frameset.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"violation frame-src §§URL§§ enforce", "loaded /frame1.html"})
    @HtmlUnitNYI(CHROME = {"loaded /frame1.html", "violation frame-src §§URL§§ enforce"},
            EDGE = {"loaded /frame1.html", "violation frame-src §§URL§§ enforce"},
            FF = {"loaded /frame1.html", "violation frame-src §§URL§§ enforce"},
            FF_ESR = {"loaded /frame1.html", "violation frame-src §§URL§§ enforce"})
    public void frameset() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_FIRST, "frame1.html");
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame2.html");

        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script src='harness.js'></script>\n"
                + "</head>\n"
                + "<frameset cols='50%,50%'>\n"
                + "  <frame src='frame1.html'>\n"
                + "  <frame src='" + URL_CROSS_ORIGIN_BASE + "frame2.html'>\n"
                + "</frameset></html>";
        loadHtmlVerify(HttpHeader.CONTENT_SECURIRY_POLICY, "frame-src 'self'", html, "");
    }

    /**
     * About:blank is always allowed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load doc")
    public void aboutBlank() throws Exception {
        loadPageVerify("frame-src 'none'", addFrameJs("about:blank"));
    }

    /**
     * Data urls are only allowed if the scheme is listed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src  enforce", "load null"},
            FF = "violation frame-src data enforce",
            FF_ESR = "violation frame-src data enforce")
    @HtmlUnitNYI(FF = {"violation frame-src  enforce", "load null"},
            FF_ESR = {"violation frame-src  enforce", "load null"})
    public void dataUrl() throws Exception {
        loadPageVerify("frame-src 'self'", addFrameJs("data:text/html,<p>data</p>"));
    }

    /**
     * Data urls are allowed if the scheme is listed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load null")
    @HtmlUnitNYI(CHROME = "load doc",
            EDGE = "load doc",
            FF = "load doc",
            FF_ESR = "load doc")
    public void dataUrlScheme() throws Exception {
        loadPageVerify("frame-src 'self' data:", addFrameJs("data:text/html,<p>data</p>"));
    }

    /**
     * Javascript: urls are not checked against frame-src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load doc")
    public void javascriptUrl() throws Exception {
        loadPageVerify("frame-src 'none'; script-src 'self' 'unsafe-inline'",
                "addFrame(\"javascript:'<p>js</p>'\");\n");
    }

    /**
     * Changing the src of a frame is checked also.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"load doc",
                       "loaded /frame.html",
                       "violation frame-src §§URL§§ enforce",
                       "load null"},
            FF = {"load doc", "loaded /frame.html", "violation frame-src §§URL§§ enforce"},
            FF_ESR = {"load doc", "loaded /frame.html", "violation frame-src §§URL§§ enforce"})
    @HtmlUnitNYI(CHROME = {"loaded /frame.html", "violation frame-src §§URL§§ enforce", "load null"},
            EDGE = {"loaded /frame.html", "violation frame-src §§URL§§ enforce", "load null"},
            FF = {"loaded /frame.html", "violation frame-src §§URL§§ enforce", "load null"},
            FF_ESR = {"loaded /frame.html", "violation frame-src §§URL§§ enforce", "load null"})
    public void srcChanged() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_FIRST, "frame.html");
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame2.html");
        loadPageVerify("frame-src 'self'",
                "var frame = addFrame('frame.html');\n"
                + "window.addEventListener('message', function(e) {\n"
                + "  if (e.data == 'loaded /frame.html') {\n"
                + "    frame.src = '" + URL_CROSS_ORIGIN_BASE + "frame2.html';\n"
                + "  }\n"
                + "});\n");
    }

    /**
     * Redirect from an allowed url to a blocked one.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src http://localhost:§§PORT§§ enforce", "load null"},
            FF = "violation frame-src §§URL§§/redirect.html enforce",
            FF_ESR = "violation frame-src §§URL§§/redirect.html enforce")
    @HtmlUnitNYI(FF = {"violation frame-src http://localhost:§§PORT§§ enforce", "load null"},
            FF_ESR = {"violation frame-src http://localhost:§§PORT§§ enforce", "load null"})
    public void redirect() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT, PORT2);
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", URL_CROSS_ORIGIN_BASE + "frame.html"));
        getMockWebConnection().setResponse(new URL(URL_FIRST, "redirect.html"), "", 302, "Found",
                MimeType.TEXT_HTML, headers);

        loadPageVerify("frame-src 'self'", addFrameJs("redirect.html"));
    }

    /**
     * Redirect from a blocked url to an allowed one.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src §§URL§§ enforce", "load null"},
            FF = "violation frame-src §§URL§§ enforce",
            FF_ESR = "violation frame-src §§URL§§ enforce")
    @HtmlUnitNYI(FF = {"violation frame-src §§URL§§ enforce", "load null"},
            FF_ESR = {"violation frame-src §§URL§§ enforce", "load null"})
    public void redirectToAllowed() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_FIRST, "frame.html");

        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair("Location", URL_FIRST + "frame.html"));
        getMockWebConnection().setResponse(new URL(URL_CROSS_ORIGIN_BASE, "redirect.html"), "", 302, "Found",
                MimeType.TEXT_HTML, headers);

        loadPageVerify("frame-src 'self'", addFrameJs(URL_CROSS_ORIGIN_BASE + "redirect.html"));
    }

    /**
     * The target of the violation event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"violation frame-src §§URL§§ enforce",
                       "target #document frame-src",
                       "load null"},
            FF = {"violation frame-src §§URL§§ enforce", "target #document frame-src"},
            FF_ESR = {"violation frame-src §§URL§§ enforce", "target #document frame-src"})
    @HtmlUnitNYI(FF = {"violation frame-src §§URL§§ enforce", "target #document frame-src", "load null"},
            FF_ESR = {"violation frame-src §§URL§§ enforce", "target #document frame-src", "load null"})
    public void violationEventTarget() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "frame.html");
        loadPageVerify("frame-src 'self'",
                "document.addEventListener('securitypolicyviolation', function(e) {\n"
                + "  log('target ' + e.target.nodeName + ' ' + e.effectiveDirective);\n"
                + "});\n"
                + addFrameJs(URL_CROSS_ORIGIN_BASE + "frame.html"));
    }

    /**
     * The policy of the frame content is independent of the policy of the parent.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("load doc")
    public void nestedFrame() throws Exception {
        expandExpectedAlertsVariables("http://localhost:" + PORT2);
        setFrameResponse(URL_CROSS_ORIGIN_BASE, "inner.html");

        final String outer = DOCTYPE_HTML
                + "<html><head></head><body>\n"
                + "<iframe src='" + URL_CROSS_ORIGIN_BASE + "inner.html'></iframe>\n"
                + "</body></html>";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "outer.html"), outer, MimeType.TEXT_HTML);

        loadPageVerify("frame-src 'self'", addFrameJs("outer.html"));
    }

    private static String addFrameJs(final String src) {
        return "addFrame('" + src + "');\n";
    }

    /**
     * Sets the response of a page loaded into a frame; this page posts
     * 'loaded' and its path to the parent window.
     */
    private void setFrameResponse(final URL base, final String path) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + "  window.parent.postMessage('loaded ' + location.pathname, '*');\n"
                + "</script>\n"
                + "</head><body></body></html>";
        getMockWebConnection().setResponse(new URL(base, path), html, MimeType.TEXT_HTML);
    }

    private void loadPageVerify(final String policy, final String testJs) throws Exception {
        loadPageVerify(HttpHeader.CONTENT_SECURIRY_POLICY, policy, "", "", testJs);
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
        loadHtmlVerify(header, policy, html, testJs);
    }

    private void loadHtmlVerify(final String header, final String policy,
            final String html, final String testJs) throws Exception {
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
