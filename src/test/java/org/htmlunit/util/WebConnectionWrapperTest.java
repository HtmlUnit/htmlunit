/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.util;

import java.util.Collections;
import java.util.List;

import org.htmlunit.HttpMethod;
import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebConnection;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.WebResponseData;
import org.junit.Test;

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
        final WebResponseData data = new WebResponseData(new byte[]{}, WebResponse.OK, "", emptyList);
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
