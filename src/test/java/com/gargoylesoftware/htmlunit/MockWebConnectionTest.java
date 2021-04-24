/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.MimeType;

/**
 * Tests for {@link MockWebConnection}.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class MockWebConnectionTest extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void charset() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>\n"
            + "  <title>Pound Test</title>\n"
            + "</head>\n"
            + "<body>\u00A3</body>\n"
            + "</html>";

        final WebClient client = getWebClient();
        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html, MimeType.TEXT_HTML, UTF_8);
        client.setWebConnection(webConnection);
        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("\u00A3", page.getBody().asText());
    }
}
