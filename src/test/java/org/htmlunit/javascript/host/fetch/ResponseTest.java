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
    public void clone() throws Exception {
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
    @Alerts({"200", "true", "0", "error", "301", "/a", "302"})
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

        loadPageVerifyTitle2(html);
    }
}
