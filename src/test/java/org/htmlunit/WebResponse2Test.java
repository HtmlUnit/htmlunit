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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.mocks.WebResponseMock;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WebResponse}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class WebResponse2Test extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void charsetInMetaTag() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><meta content='text/html; charset=utf-8' http-equiv='Content-Type'/></head>\n"
            + "<body>foo</body>\n"
            + "</html>";
        final HtmlPage page = loadPage(html);
        assertSame(UTF_8, page.getWebResponse().getContentCharset());
        assertEquals(html, page.getWebResponse().getContentAsString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getHeaderContentCharset() {
        testHeaderContentCharset(StandardCharsets.UTF_8, MimeType.TEXT_HTML + ";charset=utf-8");

        testHeaderContentCharset(null, "  \t ;charset=utf-8");
        testHeaderContentCharset(null, ";charset=utf-8");
        testHeaderContentCharset(null, "charset=utf-8");
    }

    private static void testHeaderContentCharset(final Charset expected, final String contentTypeHeader) {
        final Map<String, String> headers = new HashMap<>();
        headers.put(HttpHeader.CONTENT_TYPE, contentTypeHeader);

        final WebResponseMock response = new WebResponseMock(null, headers);
        assertEquals(expected, response.getHeaderContentCharset());
    }
}
