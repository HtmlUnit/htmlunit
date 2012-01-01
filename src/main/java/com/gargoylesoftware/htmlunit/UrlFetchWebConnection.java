/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.utils.URLEncodedUtils;

import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * An implementation of {@link WebConnection}, compatible with Google App Engine.
 * <p>
 * Note: this class is experimental and not mature like {@link HttpWebConnection}.
 * It doesn't currently support multipart POST.
 * </p>
 *
 * @version $Revision$
 * @author Amit Manjhi
 * @author Marc Guillemot
 * @since HtmlUnit 2.8
 */
public class UrlFetchWebConnection implements WebConnection {

    /** Logging support. */
    private static final Log LOG = LogFactory.getLog(UrlFetchWebConnection.class);

    private static final String[] GAE_URL_HACKS = {"http://gaeHack_javascript/", "http://gaeHack_data/",
        "http://gaeHack_about/"};

    private final WebClient webClient_;

    /**
     * Creates a new web connection instance.
     * @param webClient the WebClient that is using this connection
     */
    public UrlFetchWebConnection(final WebClient webClient) {
        webClient_ = webClient;
    }

    /**
     * {@inheritDoc}
     */
    public WebResponse getResponse(final WebRequest webRequest) throws IOException {
        final long startTime = System.currentTimeMillis();
        final URL url = webRequest.getUrl();
        if (LOG.isTraceEnabled()) {
            LOG.trace("about to fetch URL " + url);
        }

        // hack for JS, about, and data URLs.
        final WebResponse response = produceWebResponseForGAEProcolHack(url);
        if (response != null) {
            return response;
        }

        // this is a "normal" URL
        try {
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setUseCaches(false);
            connection.setConnectTimeout(webClient_.getTimeout());

            connection.addRequestProperty("User-Agent", webClient_.getBrowserVersion().getUserAgent());

            // copy the headers from WebRequestSettings
            for (final Entry<String, String> header : webRequest.getAdditionalHeaders().entrySet()) {
                connection.addRequestProperty(header.getKey(), header.getValue());
            }

            final HttpMethod httpMethod = webRequest.getHttpMethod();
            connection.setRequestMethod(httpMethod.name());
            if (HttpMethod.POST == httpMethod || HttpMethod.PUT == httpMethod) {
                connection.setDoOutput(true);
                final String charset = webRequest.getCharset();
                connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                final OutputStream outputStream = connection.getOutputStream();
                try {
                    final List<NameValuePair> pairs = webRequest.getRequestParameters();
                    final org.apache.http.NameValuePair[] httpClientPairs = NameValuePair.toHttpClient(pairs);
                    final String query = URLEncodedUtils.format(Arrays.asList(httpClientPairs), charset);
                    outputStream.write(query.getBytes(charset));
                    if (webRequest.getRequestBody() != null) {
                        IOUtils.write(webRequest.getRequestBody().getBytes(charset), outputStream);
                    }
                }
                finally {
                    outputStream.close();
                }
            }

            final int responseCode = connection.getResponseCode();
            if (LOG.isTraceEnabled()) {
                LOG.trace("fetched URL " + url);
            }

            final List<NameValuePair> headers = new ArrayList<NameValuePair>();
            for (final Map.Entry<String, List<String>> headerEntry : connection.getHeaderFields().entrySet()) {
                final String headerKey = headerEntry.getKey();
                if (headerKey != null) { // map contains entry like (null: "HTTP/1.1 200 OK")
                    final StringBuilder sb = new StringBuilder();
                    for (final String headerValue : headerEntry.getValue()) {
                        if (sb.length() > 0) {
                            sb.append(", ");
                        }
                        sb.append(headerValue);
                    }
                    headers.add(new NameValuePair(headerKey, sb.toString()));
                }
            }

            final InputStream is = responseCode < 400 ? connection.getInputStream() : connection.getErrorStream();
            final byte[] byteArray;
            try {
                byteArray = IOUtils.toByteArray(is);
            }
            finally {
                is.close();
            }
            final long duration = System.currentTimeMillis() - startTime;
            final WebResponseData responseData = new WebResponseData(byteArray, responseCode,
                connection.getResponseMessage(), headers);
            return new WebResponse(responseData, webRequest, duration);
        }
        catch (final IOException e) {
            LOG.error("Exception while tyring to fetch " + url, e);
            throw new RuntimeException(e);
        }
    }

    private WebResponse produceWebResponseForGAEProcolHack(final URL url) {
        final String externalForm = url.toExternalForm();
        for (String pattern : GAE_URL_HACKS) {
            final int index = externalForm.indexOf(pattern);
            if (index == 0) {
                String contentString = externalForm.substring(pattern.length());
                if (contentString.startsWith("'") && contentString.endsWith("'")) {
                    contentString = contentString.substring(1, contentString.length() - 1);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("special handling of URL, returning (" + contentString + ") for URL " + url);
                }
                return new StringWebResponse(contentString, url);
            }
        }
        return null;
    }
}
