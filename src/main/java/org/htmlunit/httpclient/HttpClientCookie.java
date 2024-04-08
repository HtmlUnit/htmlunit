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
package org.htmlunit.httpclient;

import java.util.Date;

import org.apache.http.cookie.ClientCookie;
import org.htmlunit.http.Cookie;

/**
 * A cookie. This class is immutable.
 *
 * @author Daniel Gredler
 * @author Nicolas Belisle
 * @author Ahmed Ashour
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
                null, clientCookie.isSecure(), false, "");
        httpClientCookie_ = clientCookie;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return httpClientCookie_.getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        return httpClientCookie_.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDomain() {
        return httpClientCookie_.getDomain();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPath() {
        return httpClientCookie_.getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getExpires() {
        return httpClientCookie_.getExpiryDate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSecure() {
        return httpClientCookie_.isSecure();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHttpOnly() {
        return httpClientCookie_.getAttribute("httponly") != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSameSite() {
        return httpClientCookie_.getAttribute("samesite");
    }

    /**
     * @return an HttpClient ClientCookie version of this cookie
     */
    public ClientCookie getHttpClientCookie() {
        return httpClientCookie_;
    }
}
