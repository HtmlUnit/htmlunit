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
import java.nio.ByteBuffer;
import java.util.function.Consumer;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.htmlunit.WebClient;
import org.htmlunit.WebClientOptions;

/**
 * Jetty9 based impl of the WebSocketAdapter.
 * To avoid conflicts with other jetty versions used by projects, we use
 * our own shaded version of jetty9 (https://github.com/HtmlUnit/htmlunit-websocket-client).
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
                final WebSocketListener webSocketListener) throws Exception {
            return new JettyWebSocketAdapter(webClient, webSocketListener);
        }
    }

    private final Object clientLock_ = new Object();
    private WebSocketClient client_;
    private final WebSocketListener listener_;
    private final JettyWebSocketAdapterImpl adapterImpl_;

    private volatile Session session_;

    /**
     * Ctor.
     * @param webClient the {@link WebClient}
     * @param listener the {@link WebSocketListener}
     * @throws Exception in case of error
     */
    public JettyWebSocketAdapter(final WebClient webClient, final WebSocketListener listener) throws Exception {
        super();
        final WebClientOptions options = webClient.getOptions();

        final HttpClient httpClient = new HttpClient();
        httpClient.setHttpCookieStore(new WebSocketCookieStore(webClient));

        // Initialize client
        if (webClient.getOptions().isUseInsecureSSL()) {
            final SslContextFactory.Client sslContextFactory = new SslContextFactory.Client(true);
            sslContextFactory.setEndpointIdentificationAlgorithm(null);
            sslContextFactory.setTrustAll(true);

            httpClient.setSslContextFactory(sslContextFactory);
        }

        httpClient.start();
        client_ = new WebSocketClient(httpClient);

        adapterImpl_ = new JettyWebSocketAdapterImpl();
        listener_ = listener;

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
            listener_.onWebSocketConnecting();
            client_.connect(adapterImpl_, url);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendText(final String message) throws IOException {
        session_.sendText(message, Callback.from(session_::demand, adapterImpl_));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendBinary(final ByteBuffer buffer) throws IOException {
        session_.sendBinary(buffer, Callback.from(session_::demand, adapterImpl_));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeSession() {
        if (session_ != null) {
            session_.close();
            session_ = null;
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
     * Session.Listener.
     */
    public class JettyWebSocketAdapterImpl implements Session.Listener, Consumer<Throwable> {

        /**
         * Ctor.
         */
        JettyWebSocketAdapterImpl() {
            super();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onWebSocketOpen(final Session session) {
            session_ = session;

            listener_.onWebSocketOpen();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onWebSocketText(final String message) {
            if (session_ == null) {
                return;
            }
            session_.demand();

            try {
                listener_.onWebSocketText(message);
            }
            catch (final Exception e) {
                // TODO: handle exception
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onWebSocketBinary(final ByteBuffer buffer, final Callback callback) {
            if (session_ == null) {
                return;
            }

            byte[] arr = new byte[0];
            try {
                arr = new byte[buffer.remaining()];
                buffer.get(arr);

                callback.succeed();
            }
            catch (final Exception e) {
                callback.fail(e);
                return;
            }

            session_.demand();

            try {
                listener_.onWebSocketBinary(arr);
            }
            catch (final Exception e) {
                // TODO: handle exception
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onWebSocketError(final Throwable cause) {

            final String className = cause.getClass().getName();
            if ("java.nio.channels.ClosedChannelException".equals(className)
                || "java.io.EOFException".equals(className)
                || "java.net.SocketException".equals(className)
                || "ClosedChannelException".contains(className)
                || (cause.getMessage() != null
                    && (cause.getMessage().contains("Connection reset")
                        || cause.getMessage().contains("Broken pipe")
                        || cause.getMessage().contains("Connection closed")))) {
                // TODO
                return;
            }

            accept(cause);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onWebSocketClose(final int statusCode, final String reason) {
            session_ = null;

            try {
                listener_.onWebSocketClose(statusCode, reason);
            }
            catch (final Exception e) {
                // TODO: handle exception
            }
        }

        @Override
        public void accept(final Throwable cause) {
            try {
                listener_.onWebSocketError(cause);
            }
            catch (final Exception e) {
                // TODO: handle exception
            }
        }
    }
}
