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
package com.gargoylesoftware.htmlunit.util;

import static org.junit.Assert.assertSame;

import java.util.Collections;
import java.util.List;

import org.apache.commons.httpclient.HttpStatus;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.WebResponseData;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link WebConnectionWrapper}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
public class WebConnectionWrapperTest extends WebTestCase {

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
            public WebResponse getResponse(final WebRequest request) {
                assertSame(wrs, request);
                return response;
            }
        };

        final WebConnectionWrapper wrapper = new WebConnectionWrapper(realConnection);
        assertSame(response, wrapper.getResponse(wrs));
    }

}
