/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.HttpStatus;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link FailingHttpStatusCodeException}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public final class FailingHttpStatusCodeExceptionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testConstructorWithWebResponse() throws Exception {
        final List<NameValuePair> emptyList = Collections.emptyList();
        final WebResponseData webResponseData = new WebResponseData(
                ArrayUtils.EMPTY_BYTE_ARRAY, HttpStatus.SC_NOT_FOUND, "not found",
                emptyList);
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
    @Test(expected = FailingHttpStatusCodeException.class)
    public void failureByGetPage() throws Exception {
        getMockWebConnection().setDefaultResponse("", 404, "Not Found", "text/html");
        getWebClientWithMockWebConnection().getPage(getDefaultUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test(expected = FailingHttpStatusCodeException.class)
    public void failureByClickLink() throws Exception {
        final String html = "<html><body><a href='doesntExist'>go</a></body></html>";
        getMockWebConnection().setDefaultResponse("", 404, "Not Found", "text/html");
        final HtmlPage page = loadPageWithAlerts(html);
        page.getAnchors().get(0).click();
    }
}
