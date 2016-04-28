/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.util.Cookie;

/**
 * A helper class for {@link WebSocket}.
 *
 * @author Ahmed Ashour
 */
class WebSocketCookieStore implements CookieStore {

    private final WebClient webClient_;

    WebSocketCookieStore(final WebClient webClient) {
        webClient_ = webClient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(final URI uri, final HttpCookie cookie) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<HttpCookie> get(final URI uri) {
        final List<HttpCookie> cookies = new ArrayList<>();
        try {
            final String urlString = uri.toString().replace("ws://", "http://").replace("wss://", "https://");
            final java.net.URL url = new java.net.URL(urlString);
            for (final Cookie cookie : webClient_.getCookies(url)) {
                final HttpCookie httpCookie = new HttpCookie(cookie.getName(), cookie.getValue());
                httpCookie.setVersion(0);
                cookies.add(httpCookie);
            }
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return cookies;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<HttpCookie> getCookies() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<URI> getURIs() {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(final URI uri, final HttpCookie cookie) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll() {
        return false;
    }
}
