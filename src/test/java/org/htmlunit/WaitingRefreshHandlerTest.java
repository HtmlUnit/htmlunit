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

import org.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WaitingRefreshHandlerTest}.
 *
 * @author Brad Clarke
 */
public final class WaitingRefreshHandlerTest extends SimpleWebTestCase {

    /**
     * Trying to cause an interrupt on a JavaScript thread due to meta redirect navigation.
     * @throws Exception if the test fails
     */
    @Test
    public void refreshOnJavascriptThread() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>First Page</title>\n"
            + "<script>\n"
            + "function doRedirect() {\n"
            + "  window.location.href = '" + URL_SECOND + "';\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='setTimeout(doRedirect, 1);'>first page body</body>\n"
            + "</html>";
        final String secondContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>Meta Redirect Page</title>\n"
            + "<meta http-equiv='Refresh' content='1; URL=" + URL_THIRD + "'>\n"
            + "</head>\n"
            + "<body>redirect page body</body>\n"
            + "</html>";
        final String thirdContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>Expected Last Page</title></head>\n"
            + "<body>Success!</body>\n"
            + "</html>";

        final WebClient client = getWebClientWithMockWebConnection();
        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, firstContent);
        conn.setResponse(URL_SECOND, secondContent);
        conn.setResponse(URL_THIRD, thirdContent);
        client.setRefreshHandler(new WaitingRefreshHandler(0));

        client.getPage(URL_FIRST);
        assertEquals(0, client.waitForBackgroundJavaScriptStartingBefore(1000));
        final HtmlPage pageAfterWait = (HtmlPage) client.getCurrentWindow().getEnclosedPage();
        assertEquals("Expected Last Page", pageAfterWait.getTitleText());
    }
}
