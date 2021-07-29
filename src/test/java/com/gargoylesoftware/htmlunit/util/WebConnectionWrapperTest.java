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
package com.gargoylesoftware.htmlunit.util;

import java.util.Collections;
import java.util.List;

import org.apache.hc.core5.http.HttpStatus;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.SimpleWebTestCase;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;

/**
 * Tests for {@link WebConnectionWrapper}.
 *
 * @author Marc Guillemot
 */
public class WebConnectionWrapperTest extends SimpleWebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void wrapper() throws Exception {
        final List<NameValuePair> emptyList = Collections.emptyList();
        final WebResponseData data = new WebResponseData(new byte[]{}, HttpStatus.SC_OK, "", emptyList);
        final WebResponse response = new WebResponse(data, URL_FIRST, HttpMethod.GET, 0);
        final WebRequest wrs = new WebRequest(URL_FIRST);

        final WebConnection realConnection = new WebConnection() {
            @Override
            public WebResponse getResponse(final WebRequest request) {
                assertSame(wrs, request);
                return response;
            }
            @Override
            public void close() {
                // nothing
            }
        };

        try (WebConnectionWrapper wrapper = new WebConnectionWrapper(realConnection)) {
            assertSame(response, wrapper.getResponse(wrs));
        }
    }

}
