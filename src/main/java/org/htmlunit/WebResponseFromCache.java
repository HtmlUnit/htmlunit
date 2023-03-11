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
package org.htmlunit;

import java.util.Collections;
import java.util.List;

import org.htmlunit.util.NameValuePair;
import org.htmlunit.util.WebResponseWrapper;

/**
 * A {@link WebResponse} implementation to deliver with content from cache. The response
 * is the same but the request may have some variation like an anchor.
 *
 * @author Marc Guillemot
 */
class WebResponseFromCache extends WebResponseWrapper {

    private final WebRequest request_;
    private final List<NameValuePair> responseHeaders_;

    /**
     * Wraps the provided cached response for a new request.
     * @param cachedResponse the response from cache
     * @param overwriteHeaders list of headers to overwrite cachedResponse headers
     * @param currentRequest the new request
     */
    WebResponseFromCache(final WebResponse cachedResponse,
            final List<NameValuePair> overwriteHeaders, final WebRequest currentRequest) {
        super(cachedResponse);
        request_ = currentRequest;
        responseHeaders_ = Collections.unmodifiableList(overwriteHeaders);
    }

    /**
     * Wraps the provided response for the given request
     * @param cachedResponse the response from cache
     * @param currentRequest the new request
     */
    WebResponseFromCache(final WebResponse cachedResponse, final WebRequest currentRequest) {
        super(cachedResponse);
        request_ = currentRequest;
        responseHeaders_ = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WebRequest getWebRequest() {
        return request_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<NameValuePair> getResponseHeaders() {
        return responseHeaders_ != null ? responseHeaders_ : super.getResponseHeaders();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getResponseHeaderValue(final String headerName) {
        if (responseHeaders_ == null) {
            return super.getResponseHeaderValue(headerName);
        }

        for (final NameValuePair pair : responseHeaders_) {
            if (pair.getName().equalsIgnoreCase(headerName)) {
                return pair.getValue();
            }
        }
        return null;
    }
}
