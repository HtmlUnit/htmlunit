/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.websocket;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;

import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;

/**
 * Jetty based impl of the WebSocketAdapter.
 *
 * @author Ronald Brill
 */
public abstract class JettyWebSocketAdapter implements WebSocketAdapter {
    private WebSocketClient client_;
    private volatile Session incomingSession_;
    private Session outgoingSession_;

    public JettyWebSocketAdapter(final WebClient webClient) {
        final WebClientOptions options = webClient.getOptions();

        if (webClient.getOptions().isUseInsecureSSL()) {
            client_ = new WebSocketClient(new SslContextFactory(true), null, null);
            // still use the deprecated method here to be backward compatible with older jersey versions
            // see https://github.com/HtmlUnit/htmlunit/issues/36
            // client_ = new WebSocketClient(new SslContextFactory.Client(true), null, null);
        }
        else {
            client_ = new WebSocketClient();
        }

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

    @Override
    public void start() throws Exception {
        client_.start();
    }

    @Override
    public void connect(final URI url) throws Exception {
        final Future<Session> connectFuture = client_.connect(new JettyWebSocketAdapterImpl(), url);
        client_.getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    onWebSocketConnecting();
                    incomingSession_ = connectFuture.get();
                }
                catch (final Exception e) {
                    onWebSocketConnectError(e);
                }
            }
        });
    }

    @Override
    public void send(final Object content) throws IOException {
        if (content instanceof String) {
            outgoingSession_.getRemote().sendString((String) content);
        }
        else if (content instanceof NativeArrayBuffer) {
            final byte[] bytes = ((NativeArrayBuffer) content).getBuffer();
            final ByteBuffer buffer = ByteBuffer.wrap(bytes);
            outgoingSession_.getRemote().sendBytes(buffer);
        }
        else {
            throw new IllegalStateException(
                    "Not Yet Implemented: WebSocket.send() was used to send non-string value");
        }
    }

    @Override
    public void closeIncommingSession() throws Exception {
        if (incomingSession_ != null) {
            incomingSession_.close();
        }
    }

    @Override
    public void closeOutgoingSession() throws Exception {
        if (outgoingSession_ != null) {
            outgoingSession_.close();
        }
    }

    @Override
    public void closeClient() throws Exception {
        if (client_ != null) {
            client_.stop();
            client_.destroy();

            // TODO finally ?
            client_ = null;
        }
    }

    private class JettyWebSocketAdapterImpl extends org.eclipse.jetty.websocket.api.WebSocketAdapter {

        JettyWebSocketAdapterImpl() {
        }

        @Override
        public void onWebSocketConnect(final Session session) {
            super.onWebSocketConnect(session);
            outgoingSession_ = session;

            JettyWebSocketAdapter.this.onWebSocketConnect();
        }

        @Override
        public void onWebSocketClose(final int statusCode, final String reason) {
            super.onWebSocketClose(statusCode, reason);
            outgoingSession_ = null;

            JettyWebSocketAdapter.this.onWebSocketClose(statusCode, reason);
        }

        @Override
        public void onWebSocketText(final String message) {
            super.onWebSocketText(message);

            JettyWebSocketAdapter.this.onWebSocketText(message);
        }

        @Override
        public void onWebSocketBinary(final byte[] data, final int offset, final int length) {
            super.onWebSocketBinary(data, offset, length);

            JettyWebSocketAdapter.this.onWebSocketBinary(data, offset, length);
        }

        @Override
        public void onWebSocketError(final Throwable cause) {
            super.onWebSocketError(cause);
            outgoingSession_ = null;

            JettyWebSocketAdapter.this.onWebSocketError(cause);
        }
    }
}
