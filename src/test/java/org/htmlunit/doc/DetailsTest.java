/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.doc;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.htmlunit.HttpHeader;
import org.htmlunit.HttpWebConnection;
import org.htmlunit.MockWebConnection;
import org.htmlunit.WebClient;
import org.htmlunit.WebRequest;
import org.htmlunit.WebResponse;
import org.htmlunit.WebResponseData;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.util.MimeType;
import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.WebConnectionWrapper;
import org.junit.Test;

import com.google.common.base.Charsets;

/**
 * Tests for the sample code from the documentation to make sure
 * we adapt the docu or do not break the samples.
 *
 * @author Ronald Brill
 */
public class DetailsTest extends WebServerTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentBlockingRequest() throws Exception {
        final URL url = new URL("http://localhost/");

        try (WebClient webClient = new WebClient()) {
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            // set more options

            // create a WebConnectionWrapper with an (subclassed) getResponse() impl
            new WebConnectionWrapper(webClient) {

                @Override
                public WebResponse getResponse(final WebRequest request) throws IOException {
                    final URL requestUrl = request.getUrl();
                    // check the request url
                    // if is allowed simple call super.

                    if (!isBlocked(requestUrl)) {
                        return super.getResponse(request);
                    }

                    // construct alternative response
                    final String content = "<html><html>";
                    final WebResponseData data = new WebResponseData(content.getBytes(Charsets.UTF_8),
                            200, "blocked", Collections.emptyList());
                    final WebResponse blocked = new WebResponse(data, request, 0L);
                    // if you like to check later on for blocked responses
                    blocked.markAsBlocked("Blocked URL: '" + requestUrl.toExternalForm() + "'");
                    return blocked;
                }

                private boolean isBlocked(final URL requestUrl) {
                    return true;
                }
            };

            // use the client as usual
            final HtmlPage page = webClient.getPage(url);
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void contentBlockingResponse() throws Exception {
        final byte[] content = new byte[] {};
        final List<NameValuePair> headers = new ArrayList<>();
        headers.add(new NameValuePair(HttpHeader.CONTENT_LENGTH, String.valueOf(content.length)));

        final MockWebConnection conn = getMockWebConnection();
        conn.setResponse(URL_FIRST, content, 200, "OK", MimeType.TEXT_HTML, headers);

        startWebServer(conn);
        final URL url = URL_FIRST;

        try (WebClient webClient = new WebClient()) {
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            // set more options

            // use our own sublcass of HttpWebConnection
            webClient.setWebConnection(new HttpWebConnection(webClient) {
                @Override
                protected WebResponse downloadResponse(final HttpUriRequest httpMethod,
                        final WebRequest webRequest, final HttpResponse httpResponse,
                        final long startTime) throws IOException {

                    // check content length header
                    final int contentLenght = Integer.parseInt(
                            httpResponse.getFirstHeader(HttpHeader.CONTENT_LENGTH).getValue());

                    // if not too big - done
                    if (contentLenght < 1_000) {
                        return super.downloadResponse(httpMethod, webRequest, httpResponse, startTime);
                    }

                    // abort downloading of the content
                    httpMethod.abort();

                    // construct alternative response
                    final String alternativeContent = "<html><html>";
                    final WebResponseData data = new WebResponseData(alternativeContent.getBytes(Charsets.UTF_8),
                            200, "blocked", Collections.emptyList());
                    final WebResponse blocked = new WebResponse(data, webRequest, 0L);
                    // if you like to check later on for blocked responses
                    blocked.markAsBlocked("Blocked URL: '" + url.toExternalForm()
                                + "' content length: " + contentLenght);
                    return blocked;
                }
            });

            // use the client as usual
            final HtmlPage page = webClient.getPage(url);
        }
    }
}
