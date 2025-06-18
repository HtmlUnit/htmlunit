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

import static org.junit.jupiter.api.Assertions.fail;

import java.util.Collections;

import org.apache.commons.lang3.ArrayUtils;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.http.HttpStatus;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FailingHttpStatusCodeException}.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 */
public final class FailingHttpStatusCodeExceptionTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void constructorWithWebResponse() throws Exception {
        final WebResponseData webResponseData = new WebResponseData(ArrayUtils.EMPTY_BYTE_ARRAY,
                HttpStatus.NOT_FOUND_404, HttpStatus.NOT_FOUND_404_MSG, Collections.emptyList());
        final WebResponse webResponse = new WebResponse(webResponseData, URL_FIRST, HttpMethod.GET, 10);
        final FailingHttpStatusCodeException e = new FailingHttpStatusCodeException(webResponse);

        assertEquals(webResponse, e.getResponse());
        assertEquals(webResponse.getStatusMessage(), e.getStatusMessage());
        assertEquals(webResponse.getStatusCode(), e.getStatusCode());
        assertTrue("message doesn't contain failing url", e.getMessage().indexOf(URL_FIRST.toExternalForm()) > -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void failureByGetPage() throws Exception {
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);
        final WebClient client = getWebClientWithMockWebConnection();
        try {
            client.getPage(URL_FIRST);
            fail("FailingHttpStatusCodeException expected");
        }
        catch (final FailingHttpStatusCodeException e) {
            // expected
        }
        assertEquals("Error: not found",
                client.getCurrentWindow().getEnclosedPage().getWebResponse().getContentAsString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void failureByClickLink() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body><a href='doesntExist'>go</a></body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebClient client = getWebClientWithMockWebConnection();
        try {
            final HtmlPage page = loadPageWithAlerts(html);
            page.getAnchors().get(0).click();
        }
        catch (final FailingHttpStatusCodeException e) {
            // expected
        }
        assertEquals("Error: not found",
                client.getCurrentWindow().getEnclosedPage().getWebResponse().getContentAsString());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void failureBySubmit() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<form name='form1' method='get' action='foo.html'>\n"
                + "  <input name='button' type='submit' id='mySubmit'/>\n"
                + "</form>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebClient client = getWebClientWithMockWebConnection();
        try {
            final HtmlPage page = loadPageWithAlerts(html);
            page.getElementById("mySubmit").click();
        }
        catch (final FailingHttpStatusCodeException e) {
            // expected
        }
        assertEquals("Error: not found",
                client.getCurrentWindow().getEnclosedPage().getWebResponse().getContentAsString());
    }
}
