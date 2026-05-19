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

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.htmlunit.WebClient;
import org.htmlunit.WebClientOptions;
import org.htmlunit.http.Cookie;
import org.htmlunit.jetty.client.HttpClient;
import org.htmlunit.jetty.http.HttpCookie;
import org.htmlunit.jetty.http.HttpCookieStore;
import org.htmlunit.jetty.util.ssl.SslContextFactory;
import org.htmlunit.jetty.websocket.api.Callback;
import org.htmlunit.jetty.websocket.api.Session;
import org.htmlunit.jetty.websocket.api.Session.Listener.AutoDemanding;
import org.htmlunit.jetty.websocket.client.WebSocketClient;
/**
 * Jetty12 based impl of the WebSocketAdapter.
 * To avoid conflicts with other jetty versions used by projects, we use
 * our own shaded version of jetty12 (https://github.com/HtmlUnit/htmlunit-websocket-client).
 *
 * @author Ronald Brill
 */
public final class JettyWebSocketAdapter implements WebSocketAdapter {

    /**
     * Our {@link WebSocketAdapterFactory}.
     */
    public static final class JettyWebSocketAdapterFactory implements WebSocketAdapterFactory {
        /**
         * {@inheritDoc}
         */
        @Override
        public WebSocketAdapter buildWebSocketAdapter(final WebClient webClient,
                final WebSocketListener webSocketListener) {
            return new JettyWebSocketAdapter(webClient, webSocketListener);
        }
    }

    private static class WebSocketCookieStore implements HttpCookieStore {

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
            throw new UnsupportedOperationException();
        }

        @Override
        public List<HttpCookie> match(final URI uri) {
            final List<HttpCookie> cookies = new ArrayList<>();
            try {
                final String urlString = uri.toString()
                        .replace("ws://", "http://")
                        .replace("wss://", "https://");
                final URL url = new URL(urlString);
                for (final Cookie cookie : webClient_.getCookies(url)) {
                    final HttpCookie.Builder builder = HttpCookie.build(cookie.getName(), cookie.getValue());
                    if (cookie.getDomain() != null) {
                        builder.domain(cookie.getDomain());
                    }
                    if (cookie.getPath() != null) {
                        builder.path(cookie.getPath());
                    }
                    if (cookie.getExpires() != null) {
                        builder.expires(cookie.getExpires().toInstant());
                    }
                    builder.secure(cookie.isSecure());
                    builder.httpOnly(cookie.isHttpOnly());
                    if (cookie.getSameSite() != null) {
                        final HttpCookie.SameSite sameSite = HttpCookie.SameSite.from(cookie.getSameSite());
                        if (sameSite != null) {
                            builder.sameSite(sameSite);
                        }
                    }
                    cookies.add(builder.build());
                }
            }
            catch (final Exception e) {
                throw new RuntimeException(e);
            }
            return cookies;
        }

        @Override
        public boolean remove(final URI uri, final HttpCookie cookie) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean clear() {
            return false;
        }
    }

    private final Object clientLock_ = new Object();
    private WebSocketClient client_;
    private final WebSocketListener listener_;

    private volatile Session incomingSession_;
    private Session outgoingSession_;

    /**
     * Ctor.
     * @param webClient the {@link WebClient}
     * @param listener the {@link WebSocketListener}
     */
    public JettyWebSocketAdapter(final WebClient webClient, final WebSocketListener listener) {
        super();
        final WebClientOptions options = webClient.getOptions();

        if (webClient.getOptions().isUseInsecureSSL()) {
            final HttpClient httpClient = new HttpClient();
            httpClient.setSslContextFactory(new SslContextFactory.Client(true));
            client_ = new WebSocketClient(httpClient);
        }
        else {
            client_ = new WebSocketClient();
        }

        listener_ = listener;

        // use the same executor as the rest
        client_.getHttpClient().setExecutor(webClient.getExecutor());

        client_.getHttpClient().setHttpCookieStore(new WebSocketCookieStore(webClient));

        int size = options.getWebSocketMaxBinaryMessageSize();
        if (size > 0) {
            client_.setMaxBinaryMessageSize(size);
        }
        size = options.getWebSocketMaxTextMessageSize();
        if (size > 0) {
            client_.setMaxTextMessageSize(size);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws Exception {
        synchronized (clientLock_) {
            client_.start();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(final URI url) throws Exception {
        synchronized (clientLock_) {
            final CompletableFuture<Session> connectFuture = client_.connect(new JettyWebSocketAdapterImpl(), url);
            client_.getExecutor().execute(() -> {
                try {
                    listener_.onWebSocketConnecting();
                    incomingSession_ = connectFuture.get();
                }
                catch (final Exception e) {
                    if (!(e instanceof ExecutionException)
                            || !(e.getCause() instanceof AsynchronousCloseException)) {
                        listener_.onWebSocketConnectError(e);
                    }
                }
            });
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final Object content) throws IOException {
        if (content instanceof String string) {
            outgoingSession_.sendText(string, Callback.NOOP);
        }
        else if (content instanceof ByteBuffer buffer) {
            outgoingSession_.sendBinary(buffer, Callback.NOOP);
        }
        else {
            throw new IllegalStateException("Not Yet Implemented: WebSocket.send() was used to send non-string value");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeIncomingSession() {
        if (incomingSession_ != null) {
            incomingSession_.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeOutgoingSession() {
        if (outgoingSession_ != null) {
            outgoingSession_.close();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeClient() throws Exception {
        synchronized (clientLock_) {
            if (client_ != null) {
                client_.stop();
                client_.destroy();

                // TODO finally ?
                client_ = null;
            }
        }
    }

    /**
     * Jetty12 based implementation of the WebSocket listener.
     * Bridges Jetty12 {@link org.htmlunit.jetty.websocket.api.Session.Listener.AutoDemanding}
     * callbacks to the HtmlUnit {@link WebSocketListener} interface.
     */
    public class JettyWebSocketAdapterImpl implements AutoDemanding {

        /** Ctor. */
        JettyWebSocketAdapterImpl() {
            super();
        }

        @Override
        public void onWebSocketOpen(final Session session) {
            outgoingSession_ = session;
            listener_.onWebSocketOpen();
        }

        @Override
        public void onWebSocketClose(final int statusCode, final String reason, final Callback callback) {
            outgoingSession_ = null;
            listener_.onWebSocketClose(statusCode, reason);
            callback.succeed();
        }

        @Override
        public void onWebSocketText(final String message) {
            listener_.onWebSocketText(message);
        }

        @Override
        public void onWebSocketBinary(final ByteBuffer payload, final Callback callback) {
            listener_.onWebSocketBinary(payload);
            callback.succeed();
        }

        @Override
        public void onWebSocketError(final Throwable cause) {
            outgoingSession_ = null;

            // cause.printStackTrace();
            if (cause instanceof ClosedChannelException) {
                // todo log
                return;
            }

            listener_.onWebSocketError(cause);
        }
    }
}
