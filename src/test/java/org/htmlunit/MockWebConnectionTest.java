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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link MockWebConnection}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class MockWebConnectionTest extends SimpleWebTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void charset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
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
        assertEquals("\u00A3", page.getBody().asNormalizedText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'/>\n"
            + "  <title>Serialized</title>\n"
            + "</head>\n"
            + "<body>\u00A3</body>\n"
            + "</html>";

        final MockWebConnection webConnection = new MockWebConnection();
        webConnection.setResponse(URL_FIRST, html, MimeType.TEXT_HTML, UTF_8);
        webConnection.setResponse(URL_SECOND, new byte[] {1, 2, 3}, 404, "Not Found",
                MimeType.APPLICATION_OCTET_STREAM, null);
        webConnection.setDefaultResponse("default");
        webConnection.setThrowable(URL_THIRD, new IOException("boom"));

        final WebClient client = getWebClient();
        client.setWebConnection(webConnection);
        client.getPage(URL_FIRST);

        final MockWebConnection copy = clone(webConnection);

        assertEquals(1, copy.getRequestCount());
        assertEquals(Collections.singletonList(URL_FIRST), copy.getRequestedUrls());
        assertEquals(URL_FIRST, copy.getLastWebRequest().getUrl());

        client.setWebConnection(copy);
        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals("\u00A3", page.getBody().asNormalizedText());
        assertEquals("Serialized", page.getTitleText());

        final WebResponse second = copy.getResponse(new WebRequest(URL_SECOND));
        assertEquals(404, second.getStatusCode());
        assertEquals("Not Found", second.getStatusMessage());
        assertArrayEquals(new byte[] {1, 2, 3}, IOUtils.toByteArray(second.getContentAsStream()));

        final WebResponse other = copy.getResponse(new WebRequest(new URL("http://localhost/other")));
        assertEquals("default", other.getContentAsString());

        try {
            copy.getResponse(new WebRequest(URL_THIRD));
            fail("IOException expected");
        }
        catch (final IOException e) {
            assertEquals("boom", e.getMessage());
        }
        assertEquals(5, copy.getRequestCount());
    }
}
