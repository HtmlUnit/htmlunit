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
    @Alerts({"GET", URL_SECOND + "", "cors", "same-origin", "default", "follow", "about:client", "", "", "false"})
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

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"POST", "v", "body", "false", "cors", "include", "no-cache", "manual", "", "strict-origin", "abc", "true", "true"})
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
            + "  req.text().then(t => log(t));\n"
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
            + "    .catch(e => log(e.name));\n"
            + "</script></head><body></body></html>";

        final WebDriver driver = loadPage2(html);
        verifyTitle2(DEFAULT_WAIT_TIME, driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "3", "3", "1", "v", "x"})
    public void bodyMethods() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  new Request('" + URL_SECOND + "', {method: 'POST', body: '{\"a\":1}'}).json().then(v => log(v.a));\n"
            + "  new Request('" + URL_SECOND + "', {method: 'POST', body: 'abc'}).arrayBuffer().then(b => log(b.byteLength));\n"
            + "  new Request('" + URL_SECOND + "', {method: 'POST', body: 'abc'}).blob().then(b => log(b.size));\n"
            + "  new Request('" + URL_SECOND + "', {method: 'POST', body: 'x'}).clone().text().then(t => log(t.length));\n"
            + "  new Request('" + URL_SECOND + "', {method: 'POST', body: 'q=v&x=y',\n"
            + "      headers: {'content-type': 'application/x-www-form-urlencoded'}})\n"
            + "      .formData().then(f => { log(f.get('q')); log(f.get('x')); });\n"
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
            + "  try { new Request('%%%'); log('ok'); } catch (e) { log(e.name); }\n"
            + "  try { new Request('" + URL_SECOND + "', {method: 'GET', body: 'x'}); log('ok'); }\n"
            + "  catch (e) { log(e.name); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }
}
