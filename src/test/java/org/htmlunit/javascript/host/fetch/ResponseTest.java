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

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

/**
 * Tests for {@link Response}.
 *
 * @author Ronald Brill
 */
public class ResponseTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "", "true", "default", "", "false", "false"})
    public void constructorEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const r = new Response();\n"
            + "  log(r.status);\n"
            + "  log(r.statusText);\n"
            + "  log(r.ok);\n"
            + "  log(r.type);\n"
            + "  log(r.url);\n"
            + "  log(r.redirected);\n"
            + "  log(r.bodyUsed);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"201", "Created", "true", "v", "text"})
    public void constructorBodyAndInit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const r = new Response('text', {\n"
            + "    status: 201,\n"
            + "    statusText: 'Created',\n"
            + "    headers: {'x-a': 'v'}\n"
            + "  });\n"
            + "  log(r.status);\n"
            + "  log(r.statusText);\n"
            + "  log(r.ok);\n"
            + "  log(r.headers.get('x-a'));\n"
            + "  r.text().then(t => log(t));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"object", "1", "3", "3", "v", "y"})
    public void bodyMethods() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  Promise.resolve()\n"
            + "    .then(() => new Response('{\"a\":1}').json())\n"
            + "    .then(v => log(typeof v))\n"
            + "    .then(() => new Response('{\"a\":1}').json())\n"
            + "    .then(v => log(v.a))\n"
            + "    .then(() => new Response('abc').arrayBuffer())\n"
            + "    .then(b => log(b.byteLength))\n"
            + "    .then(() => new Response('abc').blob())\n"
            + "    .then(b => log(b.size))\n"
            + "    .then(() => new Response('q=v&x=y',\n"
            + "      {headers: {'content-type': 'application/x-www-form-urlencoded'}}).formData())\n"
            + "    .then(f => { log(f.get('q')); log(f.get('x')); });\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "abc", "true", "TypeError"})
    public void bodyUsed() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const r = new Response('abc');\n"
            + "  log(r.bodyUsed);\n"
            + "  r.text()\n"
            + "    .then(t => { log(t); log(r.bodyUsed); return r.text(); })\n"
            + "    .catch(e => log(e.name));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "abc", "abc"})
    public void cloneTest() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const r = new Response('abc');\n"
            + "  const c = r.clone();\n"
            + "  log(r !== c);\n"
            + "  Promise.all([r.text(), c.text()]).then(v => { log(v[0]); log(v[1]); });\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"200", "true", "0", "error", "301", "§§URL§§a", "302"})
    public void staticMethods() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const ok = new Response();\n"
            + "  log(ok.status);\n"
            + "  log(ok.ok);\n"
            + "  const err = Response.error();\n"
            + "  log(err.status);\n"
            + "  log(err.type);\n"
            + "  const redir = Response.redirect('/a', 301);\n"
            + "  log(redir.status);\n"
            + "  log(redir.headers.get('location'));\n"
            + "  log(Response.redirect('/b').status);\n"
            + "</script></head><body></body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void notConstructor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try { Response(); log('ok'); } catch (e) { logEx(e); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"RangeError",
             "RangeError",
             "RangeError",
             "204",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError"})
    public void invalidInit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const cases = [\n"
            + "    [null, {status: 199}],\n"
            + "    [null, {status: 600}],\n"
            + "    [null, {status: 0}],\n"
            + "    [null, {status: 204}],\n"
            + "    ['x', {status: 204}],\n"
            + "    ['x', {status: 304}],\n"
            + "    [null, {statusText: 'a\\nb'}],\n"
            + "    [null, {statusText: '\\u0100'}],\n"
            + "    [null, 'string']\n"
            + "  ];\n"
            + "  for (const c of cases) {\n"
            + "    try { const r = new Response(c[0], c[1]); log(r.status); } catch (e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null",
             "text/plain;charset=UTF-8",
             "application/x-www-form-urlencoded;charset=UTF-8",
             "text/html",
             "null",
             "a/b"})
    public void contentType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(new Response().headers.get('content-type'));\n"
            + "  log(new Response('x').headers.get('content-type'));\n"
            + "  log(new Response(new URLSearchParams('a=b')).headers.get('content-type'));\n"
            + "  log(new Response(new Blob(['x'], {type: 'Text/HTML'})).headers.get('content-type'));\n"
            + "  log(new Response(new ArrayBuffer(1)).headers.get('content-type'));\n"
            + "  log(new Response('x', {headers: {'content-type': 'a/b'}}).headers.get('content-type'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"RangeError",
             "TypeError",
             "TypeError",
             "307 default null",
             "TypeError",
             "false null []",
             "TypeError"})
    public void redirectInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try { Response.redirect('/a', 200); log('ok'); } catch (e) { logEx(e); }\n"
            + "  try { Response.redirect('http://[x', 301); log('ok'); } catch (e) { logEx(e); }\n"
            + "  try { Response.redirect(); log('ok'); } catch (e) { logEx(e); }\n"
            + "  const r = Response.redirect('/a', 307);\n"
            + "  log(r.status + ' ' + r.statusText + ' ' + r.type + ' ' + r.body);\n"
            + "  try { r.headers.set('x', 'y'); log('ok'); } catch (e) { logEx(e); }\n"
            + "  const err = Response.error();\n"
            + "  log(err.ok + ' ' + err.statusText + ' ' + err.body + ' [' + err.url + ']');\n"
            + "  try { err.headers.append('x', 'y'); log('ok'); } catch (e) { logEx(e); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"202",
             "application/json",
             "v",
             "TypeError",
             "TypeError",
             "text/plain",
             "{\"a\":1,\"b\":[true,null]}"})
    public void jsonStatic() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const r = Response.json({a: 1, b: [true, null]}, {status: 202, headers: {'x-a': 'v'}});\n"
            + "  log(r.status);\n"
            + "  log(r.headers.get('content-type'));\n"
            + "  log(r.headers.get('x-a'));\n"
            + "  try { Response.json(undefined); log('ok'); } catch (e) { logEx(e); }\n"
            + "  try { Response.json(1, {status: 204}); log('ok'); } catch (e) { logEx(e); }\n"
            + "  log(Response.json('x', {headers: {'content-type': 'text/plain'}}).headers.get('content-type'));\n"
            + "  r.text().then(t => log(t));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"201 C v true",
             "abc",
             "false",
             "TypeError",
             "error 0"})
    public void cloneAfterUse() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const r = new Response('abc', {status: 201, statusText: 'C', headers: {'x-a': 'v'}});\n"
            + "  const c = r.clone();\n"
            + "  log(c.status + ' ' + c.statusText + ' ' + c.headers.get('x-a') + ' ' + (c.headers !== r.headers));\n"
            + "  r.text().then(t => {\n"
            + "    log(t);\n"
            + "    log(c.bodyUsed);\n"
            + "    try { r.clone(); log('ok'); } catch (e) { logEx(e); }\n"
            + "    const e = Response.error().clone();\n"
            + "    log(e.type + ' ' + e.status);\n"
            + "  });\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"null",
             "[object ReadableStream]",
             "true"})
    public void bodyStream() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(new Response().body);\n"
            + "  const r = new Response('x');\n"
            + "  log(r.body);\n"
            + "  log(r.body === r.body);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2 a\u00E4",
             "195,164",
             "SyntaxError"})
    public void textEncoding() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  new Response(new Uint8Array([0xEF, 0xBB, 0xBF, 0x61, 0xC3, 0xA4])).text()\n"
            + "    .then(t => { log(t.length + ' ' + t); return new Response('\\u00e4').bytes(); })\n"
            + "    .then(b => { log(b.join(',')); return new Response('{bad').json(); })\n"
            + "    .then(j => log(j), e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"3 text/plain;charset=utf-8",
             "3 []"})
    public void blobType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  new Response('abc', {headers: {'content-type': 'Text/Plain;Charset=UTF-8'}}).blob()\n"
            + "    .then(b => { log(b.size + ' ' + b.type); return new Response(new ArrayBuffer(3)).blob(); })\n"
            + "    .then(b => log(b.size + ' [' + b.type + ']'));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1|x y",
             "file.txt text/plain 7",
             "content",
             "TypeError",
             "1 2 \u00E4"})
    public void formDataRoundTrip() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const fd = new FormData();\n"
            + "  fd.append('a', '1');\n"
            + "  fd.append('a', 'x y');\n"
            + "  fd.append('f', new Blob(['content'], {type: 'text/plain'}), 'file.txt');\n"
            + "  new Response(fd).formData()\n"
            + "    .then(f => {\n"
            + "      log(f.getAll('a').join('|'));\n"
            + "      const file = f.get('f');\n"
            + "      log(file.name + ' ' + file.type + ' ' + file.size);\n"
            + "      return file.text();\n"
            + "    })\n"
            + "    .then(t => { log(t); return new Response('a=1+2&b=%C3%A4').formData(); })\n"
            + "    .then(f => log('ok'), e => logEx(e))\n"
            + "    .then(() => new Response('a=1+2&b=%C3%A4',\n"
            + "        {headers: {'content-type': 'application/x-www-form-urlencoded'}}).formData())\n"
            + "    .then(f => log(f.get('a') + ' ' + f.get('b')));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }
}
