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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the srcdoc attribute of {@link HtmlInlineFrame}.
 *
 * @author Ronald Brill
 * @see <a href="https://html.spec.whatwg.org/multipage/iframe-embed-object.html#attr-iframe-srcdoc">
 *     HTML Living Standard - srcdoc</a>
 */
public class HtmlInlineFrameSrcdocTest extends WebDriverTestCase {

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
     * The content of the srcdoc attribute is the document of the iframe.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"hello", "<p id=\"p\">hello</p>"})
    public void srcdocContent() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(f) {\n"
            + "    var doc = f.contentDocument;\n"
            + "    log(doc.getElementById('p').textContent);\n"
            + "    log(doc.body.innerHTML);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' srcdoc=\"<p id='p'>hello</p>\" onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * The url of a srcdoc document is about:srcdoc.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"about:srcdoc", "about:srcdoc", "about:srcdoc"})
    public void srcdocUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(f) {\n"
            + "    var win = f.contentWindow;\n"
            + "    log(win.location.href);\n"
            + "    log(f.contentDocument.URL);\n"
            + "    log(f.contentDocument.documentURI);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' srcdoc='<p>hello</p>' onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * The base url of a srcdoc document is the one of the parent document.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "§§URL§§foo.html"})
    public void srcdocBaseUri() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(f) {\n"
            + "    log(f.contentDocument.baseURI == document.baseURI);\n"
            + "    log(f.contentDocument.getElementById('a').href);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' srcdoc=\"<a id='a' href='foo.html'>foo</a>\" onload='test(this)'></iframe>\n"
            + "</body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * A srcdoc document has the origin of the parent document (same-origin access).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void srcdocOrigin() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(f) {\n"
            + "    log(f.contentWindow.origin == window.origin);\n"
            + "    try {\n"
            + "      log(f.contentWindow.parent.document.title.length >= 0);\n"
            + "    } catch(e) { log(e.name); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' srcdoc='<p>hello</p>' onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * The srcdoc attribute has precedence over the src attribute.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"about:srcdoc", "srcdoc"})
    public void srcdocPrecedence() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(f) {\n"
            + "    log(f.contentWindow.location.href);\n"
            + "    log(f.contentDocument.body.textContent);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' src='src.html' srcdoc='<p>srcdoc</p>' onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * An empty srcdoc attribute; the src attribute is ignored.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"about:srcdoc", "[]"})
    public void srcdocEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(f) {\n"
            + "    log(f.contentWindow.location.href);\n"
            + "    log('[' + f.contentDocument.body.innerHTML + ']');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' src='src.html' srcdoc='' onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Scripts of the srcdoc document are executed.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"in srcdoc about:srcdoc", "loaded"})
    public void srcdocScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(f) {\n"
            + "    log('loaded');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' srcdoc=\"<script>parent.log('in srcdoc ' + location.href);</script>\""
            + " onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * The srcdoc attribute value is html with character references.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<b>bold</b> &amp;")
    public void srcdocEntities() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test(f) {\n"
            + "    log(f.contentDocument.body.innerHTML);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' srcdoc='&lt;b&gt;bold&lt;/b&gt; &amp;amp;' onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * The srcdoc property reflects the attribute.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "<p>hello</p>", "<i>new</i>", "[]"})
    public void srcdocProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var f = document.getElementById('f');\n"
            + "    log('srcdoc' in f);\n"
            + "    log(f.srcdoc);\n"
            + "    f.srcdoc = '<i>new</i>';\n"
            + "    log(f.getAttribute('srcdoc'));\n"
            + "    var f2 = document.createElement('iframe');\n"
            + "    log('[' + f2.srcdoc + ']');\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' srcdoc='<p>hello</p>'></iframe>\n"
            + "<script>test();</script>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * An iframe created by script with srcdoc.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("<p>created</p>")
    public void srcdocSetByScript() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var f = document.createElement('iframe');\n"
            + "    f.onload = function() { log(f.contentDocument.body.innerHTML); };\n"
            + "    f.srcdoc = '<p>created</p>';\n"
            + "    document.body.appendChild(f);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<script>test();</script>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Changing the srcdoc attribute loads the new content.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<p>first</p>", "<p>second</p>"})
    public void srcdocChange() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var count = 0;\n"
            + "  function test(f) {\n"
            + "    log(f.contentDocument.body.innerHTML);\n"
            + "    if (count++ == 0) {\n"
            + "      f.srcdoc = '<p>second</p>';\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' srcdoc='<p>first</p>' onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * Removing the srcdoc attribute loads the src.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"about:srcdoc", "src.html"})
    public void srcdocRemove() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var count = 0;\n"
            + "  function test(f) {\n"
            + "    log(f.contentWindow.location.href.replace(/.*\\//, ''));\n"
            + "    if (count++ == 0) {\n"
            + "      f.removeAttribute('srcdoc');\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' src='src.html' srcdoc='<p>srcdoc</p>' onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * The method location.reload() of a srcdoc document.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"<p>first</p>", "<p>first</p>"})
    public void srcdocReload() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var count = 0;\n"
            + "  function test(f) {\n"
            + "    log(f.contentDocument.body.innerHTML);\n"
            + "    if (count++ == 0) {\n"
            + "      f.contentDocument.body.innerHTML = 'changed';\n"
            + "      f.contentWindow.location.reload();\n"
            + "    }\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<iframe id='f' srcdoc='<p>first</p>' onload='test(this)'></iframe>\n"
            + "</body></html>";

        loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }
}
