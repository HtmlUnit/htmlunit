/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link NiceRefreshHandler}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public final class NiceRefreshHandlerTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void refreshImmediatelyForDelaySmallerThanMax() throws Exception {
        doTest(2, URL_THIRD);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void noRefreshForDelayLargerThanMax() throws Exception {
        doTest(1, URL_FIRST);
    }

    private void doTest(final int handlerMaxDelay, final URL expectedUrl) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>Meta Redirect Page</title>\n"
            + "<meta http-equiv='Refresh' content='2; URL=" + URL_THIRD + "'>\n"
            + "</head>\n"
            + "<body>redirect page body</body>\n"
            + "</html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, html);
        conn.setDefaultResponse("");
        client.setRefreshHandler(new NiceRefreshHandler(handlerMaxDelay));

        final HtmlPage page = client.getPage(URL_FIRST);
        assertEquals(expectedUrl, page.getUrl());
    }
}
