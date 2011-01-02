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

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link ImmediateRefreshHandler}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public final class ImmediateRefreshHandlerTest extends WebTestCase {

    /**
     * Regression test for bug 1211980: redirect on the same page after a post.
     * @throws Exception if the test fails
     */
    @Test
    public void testRefreshSamePageAfterPost() throws Exception {
        final WebClient client = getWebClient();
        client.setRefreshHandler(new ImmediateRefreshHandler());

        // connection will return a page with <meta ... refresh> for the first call
        // and the same page without it for the other calls
        final MockWebConnection webConnection = new MockWebConnection() {
            private int nbCalls_ = 0;
            @Override
            public WebResponse getResponse(final WebRequest request) throws IOException {
                String content = "<html><head>\n";
                if (nbCalls_ == 0) {
                    content += "<meta http-equiv='refresh' content='0;url="
                        + getDefaultUrl().toExternalForm()
                        + "'>\n";
                }
                content += "</head><body></body></html>";
                nbCalls_++;
                final StringWebResponse response = new StringWebResponse(content, request.getUrl());
                response.getWebRequest().setHttpMethod(request.getHttpMethod());
                return response;
            }
        };
        client.setWebConnection(webConnection);

        final WebRequest request = new WebRequest(getDefaultUrl());
        request.setHttpMethod(HttpMethod.POST);
        client.getPage(request);
    }
}
