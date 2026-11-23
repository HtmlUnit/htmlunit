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

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Request}.
 *
 * @author Ronald Brill
 */
public class Request2Test extends SimpleWebTestCase {

    /**
     * The body of a request has to survive the serialization.
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  var req = new Request('test.txt', { method: 'POST', body: 'hello' });\n"
            + "</script></head><body></body></html>";

        try (WebClient client = getWebClient()) {
            loadPage(client, html, null, URL_FIRST);

            try (WebClient copy = clone(client)) {
                final HtmlPage copyPage = (HtmlPage) copy.getCurrentWindow().getEnclosedPage();
                assertEquals("false text/plain;charset=UTF-8", copyPage.executeJavaScript(
                        "req.bodyUsed + ' ' + req.headers.get('content-type')").getJavaScriptResult());

                copyPage.executeJavaScript("req.text().then(function(t) { document.title = t; });");
                assertEquals("hello", copyPage.getTitleText());
                assertEquals(Boolean.TRUE, copyPage.executeJavaScript("req.bodyUsed").getJavaScriptResult());
            }
        }
    }
}
