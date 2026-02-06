/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.httpclient;

import org.apache.hc.client5.http.cookie.ClientCookie;
import org.htmlunit.http.Cookie;

/**
 * Wrapper for {@link ClientCookie}.
 *
 * @author Ronald Brill
 */
public class HttpClientCookie extends Cookie {

    private final ClientCookie httpClientCookie_;

    /**
     * Creates a new HtmlUnit cookie from the HttpClient cookie provided.
     * @param clientCookie the HttpClient cookie
     */
    public HttpClientCookie(final ClientCookie clientCookie) {
        // just use this ctor but in fact we will overwrite by setting the httpClient Cookie
        super(clientCookie.getDomain(), clientCookie.getName(),
                clientCookie.getValue(), clientCookie.getPath(),
                clientCookie.getExpiryDate(), clientCookie.isSecure(),
                clientCookie.getAttribute("httponly") != null, clientCookie.getAttribute("samesite"));

        httpClientCookie_ = clientCookie;
    }

    /**
     * @return an HttpClient ClientCookie version of this cookie
     */
    public ClientCookie getHttpClientCookie() {
        return httpClientCookie_;
    }
}
