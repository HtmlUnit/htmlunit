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
package org.htmlunit;

import java.net.URL;

import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WebWindowImpl}.
 *
 * @author Ronald Brill
 */
public class WebWindowImplTest extends SimpleWebTestCase {

    /**
     * The click on the link holds the script lock of the page while navigating; the request started
     * by the click handler is running and waits for this lock to process its events.
     * Replacing the page must not wait for this job (it would wait until the timeout of 10s).
     *
     * @throws Exception if the test fails
     */
    @Test
    public void navigationByClickDoesNotWaitForJobWaitingForTheScriptLock() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + "  function send() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.open('GET', 'foo.txt', true);\n"
            + "    xhr.send();\n"
            // give the event loop the time to start the request and to wait for the lock
            + "    var start = Date.now(); while (Date.now() - start < 300) {}\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <a id='link' href='" + URL_SECOND + "' onclick='send()'>next</a>\n"
            + "</body></html>";

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(new URL(URL_FIRST, "foo.txt"), "Hello", MimeType.TEXT_PLAIN);
        conn.setResponse(URL_SECOND, DOCTYPE_HTML + "<html><head><title>second</title></head></html>");

        final HtmlPage page = loadPage(html);

        final long start = System.currentTimeMillis();
        final HtmlPage secondPage = page.getHtmlElementById("link").click();
        final long duration = System.currentTimeMillis() - start;

        assertEquals("second", secondPage.getTitleText());
        assertTrue("click took " + duration + "ms", duration < 5_000);
    }
}
