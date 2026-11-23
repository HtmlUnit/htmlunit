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
 * Tests for {@link Request}.
 *
 * @author Ronald Brill
 */
public class RequestTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"GET", "§§URL§§", "cors", "same-origin", "default", "follow", "about:client", "", "", "false"})
    public void constructorSimple() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const req = new Request('" + URL_SECOND + "');\n"
            + "  log(req.method);\n"
            + "  log(req.url);\n"
            + "  log(req.mode);\n"
            + "  log(req.credentials);\n"
            + "  log(req.cache);\n"
            + "  log(req.redirect);\n"
            + "  log(req.referrer);\n"
            + "  log(req.referrerPolicy);\n"
            + "  log(req.integrity);\n"
            + "  log(req.keepalive);\n"
            + "</script></head><body></body></html>";

        expandExpectedAlertsVariables(URL_SECOND);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"POST", "v", "false", "cors", "include", "no-cache", "manual",
             "", "strict-origin", "abc", "true", "false", "body"})
    public void constructorWithInit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const sig = new AbortController().signal;\n"
            + "  const req = new Request('" + URL_SECOND + "', {\n"
            + "    method: 'post',\n"
            + "    headers: {'x-a': 'v'},\n"
            + "    body: 'body',\n"
            + "    mode: 'cors',\n"
            + "    credentials: 'include',\n"
            + "    cache: 'no-cache',\n"
            + "    redirect: 'manual',\n"
            + "    referrer: '',\n"
            + "    referrerPolicy: 'strict-origin',\n"
            + "    integrity: 'abc',\n"
            + "    keepalive: true,\n"
            + "    signal: sig\n"
            + "  });\n"
            + "  log(req.method);\n"
            + "  log(req.headers.get('x-a'));\n"
            + "  log(req.bodyUsed);\n"
            + "  log(req.mode);\n"
            + "  log(req.credentials);\n"
            + "  log(req.cache);\n"
            + "  log(req.redirect);\n"
            + "  log(req.referrer);\n"
            + "  log(req.referrerPolicy);\n"
            + "  log(req.integrity);\n"
            + "  log(req.keepalive);\n"
            + "  log(req.signal === sig);\n"
            + "  req.text().then(t => log(t));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"POST", "x", "true", "false", "x"})
    public void constructorFromRequestAndClone() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const base = new Request('" + URL_SECOND + "', {method: 'POST', headers: {'x-a': 'x'}, body: 'x'});\n"
            + "  const req = new Request(base);\n"
            + "  const clone = req.clone();\n"
            + "  log(req.method);\n"
            + "  log(req.headers.get('x-a'));\n"
            + "  log(req.url === base.url);\n"
            + "  log(req === clone);\n"
            + "  clone.text().then(t => log(t));\n"
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
            + "  const req = new Request('" + URL_SECOND + "', {method: 'POST', body: 'abc'});\n"
            + "  log(req.bodyUsed);\n"
            + "  req.text()\n"
            + "    .then(t => { log(t); log(req.bodyUsed); return req.text(); })\n"
            + "    .catch(e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "3", "3", "1", "v", "y"})
    public void bodyMethods() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  Promise.resolve()\n"
            + "      .then(() => new Request('" + URL_SECOND + "', {method: 'POST', body: '{\"a\":1}'}).json())\n"
            + "      .then(v => { log(v.a); return new Request('" + URL_SECOND + "', {method: 'POST', body: 'abc'}).arrayBuffer(); })\n"
            + "      .then(b => { log(b.byteLength); return new Request('" + URL_SECOND + "', {method: 'POST', body: 'abc'}).blob(); })\n"
            + "      .then(b => { log(b.size); return new Request('" + URL_SECOND + "', {method: 'POST', body: 'x'}).clone().text(); })\n"
            + "      .then(t => {\n"
            + "          log(t.length);\n"
            + "          return new Request('" + URL_SECOND + "', {method: 'POST', body: 'q=v&x=y',\n"
            + "              headers: {'content-type': 'application/x-www-form-urlencoded'}}).formData();\n"
            + "      })\n"
            + "      .then(f => { log(f.get('q')); log(f.get('x')); });\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError", "TypeError"})
    public void invalidConstructionCases() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try { new Request(); log('ok'); } catch (e) { log(e.name); }\n"
            + "  try { new Request('" + URL_SECOND + "', {method: 'GET', body: 'x'}); log('ok'); }\n"
            + "  catch (e) { logEx(e); }\n"
            + "</script></head><body></body></html>";

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
            + "  try { Request('" + URL_SECOND + "'); log('ok'); } catch (e) { logEx(e); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§foo/bar?x=1",
             "§§URL§§abc#hash",
             "TypeError",
             "TypeError"})
    public void relativeUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(new Request('foo/bar?x=1').url);\n"
            + "  log(new Request('/abc#hash').url);\n"
            + "  try { new Request('http://user:pwd@localhost/'); log('ok'); } catch (e) { logEx(e); }\n"
            + "  try { new Request('http://[invalid'); log('ok'); } catch (e) { logEx(e); }\n"
            + "</script></head><body></body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"GET HEAD POST PUT DELETE OPTIONS patch Patch custom ",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError"})
    public void methodNormalization() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  const methods = ['get', 'head', 'post', 'put', 'delete', 'options', 'patch', 'Patch', 'custom'];\n"
            + "  let result = '';\n"
            + "  for (const m of methods) { result += new Request(url, {method: m}).method + ' '; }\n"
            + "  log(result);\n"
            + "  for (const m of ['CONNECT', 'trace', 'TRACK', '', 'a b', 'g\u00e9t']) {\n"
            + "    try { new Request(url, {method: m}); log('ok'); } catch (e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"TypeError",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError",
             "ok",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError",
             "TypeError",
             "ok",
             "TypeError"})
    public void invalidInit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  const inits = [\n"
            + "    {mode: 'navigate'},\n"
            + "    {mode: 'unknown'},\n"
            + "    {credentials: 'unknown'},\n"
            + "    {cache: 'unknown'},\n"
            + "    {cache: 'only-if-cached'},\n"
            + "    {cache: 'only-if-cached', mode: 'same-origin'},\n"
            + "    {redirect: 'unknown'},\n"
            + "    {referrerPolicy: 'unknown'},\n"
            + "    {referrerPolicy: 'NO-REFERRER'},\n"
            + "    {method: 'PUT', mode: 'no-cors'},\n"
            + "    {method: 'HEAD', body: 'x'},\n"
            + "    {signal: {}},\n"
            + "    {signal: null},\n"
            + "    'string'\n"
            + "  ];\n"
            + "  for (const init of inits) {\n"
            + "    try { new Request(url, init); log('ok'); } catch (e) { logEx(e); }\n"
            + "  }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"§§URL§§ref",
             "about:client",
             "about:client",
             "",
             "§§URL§§ref",
             "about:client"})
    public void referrer() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  log(new Request(url, {referrer: '/ref'}).referrer);\n"
            + "  log(new Request(url, {referrer: 'about:client'}).referrer);\n"
            + "  log(new Request(url, {referrer: 'http://other.example.com/'}).referrer);\n"
            + "  log(new Request(url, {referrer: ''}).referrer);\n"
            + "  log(new Request(new Request(url, {referrer: '/ref'})).referrer);\n"
            + "  log(new Request(new Request(url, {referrer: '/ref'}), {method: 'GET'}).referrer);\n"
            + "</script></head><body></body></html>";

        expandExpectedAlertsVariables(URL_FIRST);
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"text/plain;charset=UTF-8",
             "application/x-www-form-urlencoded;charset=UTF-8",
             "text/html",
             "null",
             "null",
             "null",
             "text/xml",
             "true"})
    public void contentType() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  function ct(body, headers) {\n"
            + "    const req = new Request(url, {method: 'POST', body: body, headers: headers});\n"
            + "    return req.headers.get('content-type');\n"
            + "  }\n"
            + "  log(ct('x'));\n"
            + "  log(ct(new URLSearchParams('a=b')));\n"
            + "  log(ct(new Blob(['x'], {type: 'text/html'})));\n"
            + "  log(ct(new Blob(['x'])));\n"
            + "  log(ct(new ArrayBuffer(2)));\n"
            + "  log(ct(new Uint8Array(2)));\n"
            + "  log(ct('x', {'Content-Type': 'text/xml'}));\n"
            + "  log(ct(new FormData()).startsWith('multipart/form-data; boundary='));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"x-a,v",
             "x-a,v"})
    public void forbiddenHeaders() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const req = new Request('" + URL_SECOND + "',\n"
            + "      {headers: {'Cookie': 'a=b', 'Host': 'x', 'x-a': 'v'}});\n"
            + "  log([...req.headers].join('|'));\n"
            + "  req.headers.set('Referer', 'x');\n"
            + "  log([...req.headers].join('|'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The method override headers are forbidden if the value contains a forbidden method.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"x-a,v", "x-a,v|x-http-method-override,PUT", "x-a,v|x-http-method-override,PUT"})
    public void forbiddenHeadersMethodOverride() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const req = new Request('" + URL_SECOND + "',\n"
            + "      {headers: {'X-HTTP-Method-Override': 'TRACE', 'X-HTTP-Method': 'connect',\n"
            + "                 'X-Method-Override': 'GET, Track', 'x-a': 'v'}});\n"
            + "  log([...req.headers].join('|'));\n"
            + "  req.headers.set('X-HTTP-Method-Override', 'PUT');\n"
            + "  log([...req.headers].join('|'));\n"
            + "  req.headers.set('X-HTTP-Method-Override', 'trace');\n"
            + "  log([...req.headers].join('|'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true",
             "false false false",
             "true true true",
             "stop stop stop",
             "true",
             "false"})
    public void signal() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  const ctrl = new AbortController();\n"
            + "  const req = new Request(url, {signal: ctrl.signal});\n"
            + "  const req2 = new Request(req);\n"
            + "  const clone = req.clone();\n"
            + "  log(req.signal instanceof AbortSignal);\n"
            + "  log(req.signal.aborted + ' ' + req2.signal.aborted + ' ' + clone.signal.aborted);\n"
            + "  ctrl.abort('stop');\n"
            + "  log(req.signal.aborted + ' ' + req2.signal.aborted + ' ' + clone.signal.aborted);\n"
            + "  log(req.signal.reason + ' ' + req2.signal.reason + ' ' + clone.signal.reason);\n"
            + "  log(new Request(url, {signal: AbortSignal.abort('x')}).signal.aborted);\n"
            + "  log(new Request(req, {signal: null}).signal.aborted);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true false",
             "TypeError",
             "TypeError",
             "false",
             "z"})
    public void usedRequest() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  const base = new Request(url, {method: 'POST', body: 'x'});\n"
            + "  const req = new Request(base);\n"
            + "  log(base.bodyUsed + ' ' + req.bodyUsed);\n"
            + "  try { new Request(base); log('ok'); } catch (e) { logEx(e); }\n"
            + "  try { base.clone(); log('ok'); } catch (e) { logEx(e); }\n"
            + "  const other = new Request(url, {method: 'POST', body: 'y'});\n"
            + "  const replaced = new Request(other, {body: 'z'});\n"
            + "  log(other.bodyUsed);\n"
            + "  replaced.text().then(t => log(t));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"null", "[object ReadableStream]", "true", "half", "", "false", "false"},
            FF = {"undefined", "undefined", "true", "undefined", "", "undefined", "undefined"},
            FF_ESR = {"undefined", "undefined", "true", "undefined", "", "undefined", "undefined"})
    public void bodyStream() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  log(new Request(url).body);\n"
            + "  const req = new Request(url, {method: 'POST', body: 'x'});\n"
            + "  log(req.body);\n"
            + "  log(req.body === req.body);\n"
            + "  log(req.duplex);\n"
            + "  log(req.destination);\n"
            + "  log(req.isHistoryNavigation);\n"
            + "  log(req.isReloadNavigation);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Uint8Array] 1,2,3",
             "[]",
             "0",
             "SyntaxError"})
    public void bytesAndEmptyBody() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const url = '" + URL_SECOND + "';\n"
            + "  new Request(url, {method: 'POST', body: new Uint8Array([1, 2, 3])}).bytes()\n"
            + "    .then(b => { log(Object.prototype.toString.call(b) + ' ' + b.join(','));\n"
            + "                 return new Request(url).text(); })\n"
            + "    .then(t => { log('[' + t + ']'); return new Request(url).arrayBuffer(); })\n"
            + "    .then(b => { log(b.byteLength); return new Request(url).json(); })\n"
            + "    .then(j => log(j), e => logEx(e));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * In mode 'no-cors' only the no-CORS-safelisted request headers are allowed.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"accept,text/plain|accept-language,de-DE",
             "accept,text/plain, text/html|content-type,text/plain;charset=utf-8",
             "text/plain, text/html", "text/plain;charset=utf-8", "text/plain;charset=UTF-8", "null"})
    public void noCorsHeaders() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const req = new Request('" + URL_SECOND + "', {mode: 'no-cors', headers: {\n"
            + "      'X-Custom': 'v', 'Accept': 'text/plain', 'Accept-Language': 'de-DE', 'Content-Language': 'd?',\n"
            + "      'Content-Type': 'application/json'}});\n"
            + "  log(Array.from(req.headers).join('|'));\n"
            + "  req.headers.set('Content-Type', 'text/plain;charset=utf-8');\n"
            + "  req.headers.append('X-Other', 'v');\n"
            + "  req.headers.append('Accept', 'text/html');\n"
            + "  req.headers.delete('Accept-Language');\n"
            + "  log(Array.from(req.headers).join('|'));\n"
            + "  req.headers.set('Accept', 'x'.repeat(129));\n"
            + "  log(req.headers.get('Accept'));\n"
            + "  log(new Request(req).headers.get('Content-Type'));\n"
            + "  const post = new Request('" + URL_SECOND + "', {mode: 'no-cors', method: 'POST', body: 'x'});\n"
            + "  log(post.headers.get('Content-Type'));\n"
            + "  const blob = new Request('" + URL_SECOND + "',\n"
            + "      {mode: 'no-cors', method: 'POST', body: new Blob(['x'], {type: 'image/png'})});\n"
            + "  log(blob.headers.get('Content-Type'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }
}
