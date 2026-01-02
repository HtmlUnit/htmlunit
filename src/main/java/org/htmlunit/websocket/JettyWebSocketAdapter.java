/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
import java.util.concurrent.Future;

import org.htmlunit.WebClient;
import org.htmlunit.WebClientOptions;
import org.htmlunit.jetty.util.ssl.SslContextFactory;
import org.htmlunit.jetty.websocket.api.Session;
import org.htmlunit.jetty.websocket.api.WebSocketPolicy;
import org.htmlunit.jetty.websocket.client.WebSocketClient;

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
                final WebSocketListener webSocketListener) {
            return new JettyWebSocketAdapter(webClient, webSocketListener);
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
            client_ = new WebSocketClient(new SslContextFactory(true), null, null);
            // still use the deprecated method here to be backward compatible with older jetty versions
            // see https://github.com/HtmlUnit/htmlunit/issues/36
            // client_ = new WebSocketClient(new SslContextFactory.Client(true), null, null);
        }
        else {
            client_ = new WebSocketClient();
        }

        listener_ = listener;

        // use the same executor as the rest
        client_.setExecutor(webClient.getExecutor());

        client_.getHttpClient().setCookieStore(new WebSocketCookieStore(webClient));

        final WebSocketPolicy policy = client_.getPolicy();
        int size = options.getWebSocketMaxBinaryMessageSize();
        if (size > 0) {
            policy.setMaxBinaryMessageSize(size);
        }
        size = options.getWebSocketMaxBinaryMessageBufferSize();
        if (size > 0) {
            policy.setMaxBinaryMessageBufferSize(size);
        }
        size = options.getWebSocketMaxTextMessageSize();
        if (size > 0) {
            policy.setMaxTextMessageSize(size);
        }
        size = options.getWebSocketMaxTextMessageBufferSize();
        if (size > 0) {
            policy.setMaxTextMessageBufferSize(size);
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
            final Future<Session> connectFuture = client_.connect(new JettyWebSocketAdapterImpl(), url);
            client_.getExecutor().execute(() -> {
                try {
                    listener_.onWebSocketConnecting();
                    incomingSession_ = connectFuture.get();
                }
                catch (final Exception e) {
                    listener_.onWebSocketConnectError(e);
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
            outgoingSession_.getRemote().sendString(string);
        }
        else if (content instanceof ByteBuffer buffer) {
            outgoingSession_.getRemote().sendBytes(buffer);
        }
        else {
            throw new IllegalStateException(
                    "Not Yet Implemented: WebSocket.send() was used to send non-string value");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeIncommingSession() {
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

    private class JettyWebSocketAdapterImpl extends org.htmlunit.jetty.websocket.api.WebSocketAdapter {

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
        public void onWebSocketConnect(final Session session) {
            super.onWebSocketConnect(session);
            outgoingSession_ = session;

            listener_.onWebSocketConnect();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onWebSocketClose(final int statusCode, final String reason) {
            super.onWebSocketClose(statusCode, reason);
            outgoingSession_ = null;

            listener_.onWebSocketClose(statusCode, reason);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onWebSocketText(final String message) {
            super.onWebSocketText(message);

            listener_.onWebSocketText(message);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onWebSocketBinary(final byte[] data, final int offset, final int length) {
            super.onWebSocketBinary(data, offset, length);

            listener_.onWebSocketBinary(data, offset, length);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onWebSocketError(final Throwable cause) {
            super.onWebSocketError(cause);
            outgoingSession_ = null;

            listener_.onWebSocketError(cause);
        }
    }
}
