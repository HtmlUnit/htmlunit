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
package org.htmlunit.javascript.host.fetch;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.htmlunit.HttpMethod;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.WebRequest;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for fetch api host objects.
 *
 * @author Ronald Brill
 */
public class FetchTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function", "function", "function"})
    public void globalsAvailable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof fetch);\n"
            + "  log(typeof Headers);\n"
            + "  log(typeof Request);\n"
            + "  log(typeof Response);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "OK", "true", URL_SECOND + "", "basic", "payload"})
    public void fetchGet() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_SECOND + "')\n"
            + "    .then(r => {\n"
            + "      log(r.status);\n"
            + "      log(r.statusText);\n"
            + "      log(r.ok);\n"
            + "      log(r.url);\n"
            + "      log(r.type);\n"
            + "      return r.text();\n"
            + "    })\n"
            + "    .then(t => log(t))\n"
            + "    .catch(e => log(e.name));\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "payload", MimeType.TEXT_PLAIN);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"404", "false"})
    public void fetchStatus() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_SECOND + "')\n"
            + "    .then(r => { log(r.status); log(r.ok); })\n"
            + "    .catch(e => log(e.name));\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "missing", 404, "Not Found", MimeType.TEXT_PLAIN);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"v1, v2", "true", "false", "v3"})
    public void headersBasics() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const h = new Headers({'X-Test': 'v1'});\n"
            + "  h.append('x-test', 'v2');\n"
            + "  log(h.get('X-TEST'));\n"
            + "  log(h.has('x-test'));\n"
            + "  h.delete('x-test');\n"
            + "  log(h.has('x-test'));\n"
            + "  h.set('x-test', 'v3');\n"
            + "  log(h.get('X-Test'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a=1,b=2", "a,b", "1,2", "a:1|b:2"})
    public void headersIterators() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const h = new Headers([['a', '1'], ['b', '2']]);\n"
            + "  log(Array.from(h.entries()).map(e => e[0] + '=' + e[1]).join(','));\n"
            + "  log(Array.from(h.keys()).join(','));\n"
            + "  log(Array.from(h.values()).join(','));\n"
            + "  let s = [];\n"
            + "  h.forEach((v, k) => s.push(k + ':' + v));\n"
            + "  log(s.join('|'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"POST", "true", "false", "true", "body"})
    public void requestBasics() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const req = new Request('" + URL_SECOND + "', {method: 'post', headers: {'x-a': 'v'}, body: 'body'});\n"
            + "  log(req.method);\n"
            + "  log(req.headers.has('X-A'));\n"
            + "  log(req.bodyUsed);\n"
            + "  const clone = req.clone();\n"
            + "  log(clone.method === req.method);\n"
            + "  req.text().then(t => log(t));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "abc", "true", "TypeError"})
    public void responseBodyUsed() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const r = new Response('abc');\n"
            + "  log(r.bodyUsed);\n"
            + "  r.text()\n"
            + "   .then(t => { log(t); log(r.bodyUsed); return r.text(); })\n"
            + "   .catch(e => log(e.name));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "true", "object", "3", "3", "3"})
    public void responseMethods() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const r1 = new Response('{\"a\":1}');\n"
            + "  log(r1.status);\n"
            + "  log(r1.ok);\n"
            + "  r1.json().then(v => log(typeof v));\n"
            + "  new Response('abc').arrayBuffer().then(b => log(b.byteLength));\n"
            + "  new Response('abc').blob().then(b => log(b.size));\n"
            + "  new Response('abc').clone().text().then(t => log(t.length));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "error", "301", "/a"})
    public void responseStaticMethods() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const err = Response.error();\n"
            + "  log(err.status);\n"
            + "  log(err.type);\n"
            + "  const redir = Response.redirect('/a', 301);\n"
            + "  log(redir.status);\n"
            + "  log(redir.headers.get('location'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "true", "HtmlUnit"})
    public void fetchPostJson() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_SECOND + "', {\n"
            + "    method: 'POST',\n"
            + "    headers: {'Content-Type': 'application/json'},\n"
            + "    body: JSON.stringify({q: 'HtmlUnit'})\n"
            + "  })\n"
            + "   .then(r => { log(r.status); log(r.ok); return r.text(); })\n"
            + "   .then(t => log(t))\n"
            + "   .catch(e => log(e.name));\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "HtmlUnit", MimeType.TEXT_PLAIN);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());

        final WebRequest request = getMockWebConnection().getLastWebRequest();
        assertEquals(HttpMethod.POST, request.getHttpMethod());
        assertEquals("{\"q\":\"HtmlUnit\"}", request.getRequestBody());
        assertEquals("application/json", request.getAdditionalHeader("content-type"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void fetchInvalidUrlRejects() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('ftp://example.com/path')\n"
            + "   .then(() => log('ok'))\n"
            + "   .catch(e => log(e.name));\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "ok", "200"})
    public void fetchThenChainAndAwait() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  fetch('" + URL_SECOND + "')\n"
            + "    .then(r => r.text())\n"
            + "    .then(t => log(t));\n"
            + "  (async function() {\n"
            + "    const r = await fetch('" + URL_SECOND + "');\n"
            + "    log(r.status);\n"
            + "  })();\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, "ok", MimeType.TEXT_PLAIN);
        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
