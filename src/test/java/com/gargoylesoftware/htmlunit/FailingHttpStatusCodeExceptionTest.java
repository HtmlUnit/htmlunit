/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.ArrayUtils;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link FailingHttpStatusCodeException}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public final class FailingHttpStatusCodeExceptionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testConstructorWitWebResponse() throws Exception {
        final List<NameValuePair> emptyList = Collections.emptyList();
        final WebResponseData webResponseData = new WebResponseData(
                ArrayUtils.EMPTY_BYTE_ARRAY, HttpStatus.SC_NOT_FOUND, "not found",
                emptyList);
        final WebResponse webResponse = new WebResponseImpl(webResponseData, URL_FIRST, HttpMethod.GET, 10);
        final FailingHttpStatusCodeException e = new FailingHttpStatusCodeException(webResponse);

        assertEquals(webResponse, e.getResponse());
        assertEquals(webResponse.getStatusMessage(), e.getStatusMessage());
        assertEquals(webResponse.getStatusCode(), e.getStatusCode());
        assertTrue("message doesn't contain failing url", e.getMessage().indexOf(URL_FIRST.toExternalForm()) > -1);
    }
}
