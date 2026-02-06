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
package org.htmlunit.websocket;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.http.HttpCookieStore;
import org.htmlunit.WebClient;
import org.htmlunit.http.Cookie;
import org.htmlunit.javascript.host.WebSocket;

/**
 * A helper class for {@link WebSocket}.
 *
 * @author Ronald Brill
 */
class WebSocketCookieStore implements HttpCookieStore {

    private final WebClient webClient_;

    WebSocketCookieStore(final WebClient webClient) {
        webClient_ = webClient;
    }

    @Override
    public boolean add(final URI uri, final HttpCookie cookie) {
        return false;
    }

    @Override
    public List<HttpCookie> all() {
        final List<HttpCookie> cookies = new ArrayList<>();
        try {
            for (final Cookie htmlUnitCookie : webClient_.getCookieManager().getCookies()) {
                cookies.add(buildHttpCookie(htmlUnitCookie));
            }
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return cookies;
    }

    @Override
    public List<HttpCookie> match(final URI uri) {
        final List<HttpCookie> cookies = new ArrayList<>();
        try {
            final String urlString = uri.toString()
                    .replace("ws://", "http://")
                    .replace("wss://", "https://");
            final java.net.URL url = new java.net.URL(urlString);

            for (final Cookie htmlUnitCookie : webClient_.getCookies(url)) {
                cookies.add(buildHttpCookie(htmlUnitCookie));
            }
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
        return cookies;
    }

    @Override
    public boolean remove(final URI uri, final HttpCookie cookie) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }

    private static HttpCookie buildHttpCookie(final Cookie htmlUnitCookie) {
        final HttpCookie.Builder builder = HttpCookie.build(
                htmlUnitCookie.getName(),
                htmlUnitCookie.getValue());
        if (htmlUnitCookie.getDomain() != null) {
            builder.domain(htmlUnitCookie.getDomain());
        }

        if (htmlUnitCookie.getPath() != null) {
            builder.path(htmlUnitCookie.getPath());
        }
//
//        if (htmlUnitCookie.getMaxAge() > -1) {
//            builder.maxAge(htmlUnitCookie.getMaxAge());
//        }

        if (htmlUnitCookie.isSecure()) {
            builder.secure(true);
        }

        if (htmlUnitCookie.isHttpOnly()) {
            builder.httpOnly(true);
        }

        return builder.build();
    }
}
