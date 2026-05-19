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

/**
 * Tests for {@link Headers}.
 *
 * @author Ronald Brill
 */
public class HeadersTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "false"})
    public void constructorEmpty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const h = new Headers();\n"
            + "  log(Array.from(h.entries()).length);\n"
            + "  log(h.has('x-test'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"v1", "v2", "v3", "v4", "v5"})
    public void constructorFromObjectSequenceAndHeaders() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const h1 = new Headers({'A': 'v1'});\n"
            + "  const h2 = new Headers([['B', 'v2'], ['C', 'v3']]);\n"
            + "  const h3 = new Headers(h1);\n"
            + "  h3.append('d', 'v4');\n"
            + "  const h4 = new Headers(h3);\n"
            + "  h4.set('d', 'v5');\n"
            + "  log(h1.get('a'));\n"
            + "  log(h2.get('b'));\n"
            + "  log(h2.get('c'));\n"
            + "  log(h3.get('d'));\n"
            + "  log(h4.get('d'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"v1, v2", "true", "v3", "false"})
    public void appendSetHasDeleteCaseInsensitive() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const h = new Headers();\n"
            + "  h.append('X-Test', 'v1');\n"
            + "  h.append('x-test', 'v2');\n"
            + "  log(h.get('X-TEST'));\n"
            + "  log(h.has('x-Test'));\n"
            + "  h.set('x-test', 'v3');\n"
            + "  log(h.get('X-TEST'));\n"
            + "  h.delete('X-tEsT');\n"
            + "  log(h.has('x-test'));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a=1,b=2", "a,b", "1,2", "a:1|b:2", "a=1,b=2"})
    public void iteratorsAndForEach() throws Exception {
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
            + "  log(Array.from(h).map(e => e[0] + '=' + e[1]).join(','));\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "c1", "c2"})
    public void getSetCookie() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  const h = new Headers();\n"
            + "  h.append('Set-Cookie', 'c1');\n"
            + "  h.append('set-cookie', 'c2');\n"
            + "  const c = h.getSetCookie();\n"
            + "  log(c.length);\n"
            + "  log(c[0]);\n"
            + "  log(c[1]);\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void constructorInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    new Headers(1);\n"
            + "  } catch (e) { log(e.name); }\n"
            + "</script></head><body></body></html>";

        loadPageVerifyTitle2(html);
    }
}
