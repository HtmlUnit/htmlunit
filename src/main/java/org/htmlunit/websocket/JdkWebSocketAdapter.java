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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Builder;
import java.nio.ByteBuffer;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.Executor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.htmlunit.WebClient;
import org.htmlunit.WebClientOptions;

/**
 * JDK based implementation of the {@link WebSocketAdapter}.
 * Uses the {@link java.net.http.HttpClient} and {@link java.net.http.WebSocket}
 * APIs available since JDK 11.
 *
 * @author Ronald Brill
 */
public final class JdkWebSocketAdapter implements WebSocketAdapter {

    /**
     * Our {@link WebSocketAdapterFactory}.
     */
    public static final class JdkWebSocketAdapterFactory implements WebSocketAdapterFactory {
        /**
         * {@inheritDoc}
         */
        @Override
        public WebSocketAdapter buildWebSocketAdapter(final WebClient webClient,
                final WebSocketListener webSocketListener) {
            return new JdkWebSocketAdapter(webClient, webSocketListener);
        }
    }

    private final Object clientLock_ = new Object();
    private HttpClient httpClient_;
    private final WebClient webClient_;
    private final WebSocketListener listener_;

    private volatile java.net.http.WebSocket incomingSession_;
    private java.net.http.WebSocket outgoingSession_;

    /**
     * Ctor.
     * @param webClient the {@link WebClient}
     * @param listener the {@link WebSocketListener}
     */
    public JdkWebSocketAdapter(final WebClient webClient, final WebSocketListener listener) {
        super();
        webClient_ = webClient;
        listener_ = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start() throws Exception {
        synchronized (clientLock_) {
            final WebClientOptions options = webClient_.getOptions();
            final Executor executor = webClient_.getExecutor();

            final Builder builder = HttpClient.newBuilder()
                    .executor(executor)
                    .cookieHandler(new WebSocketCookieHandler(webClient_));

            if (options.isUseInsecureSSL()) {
                builder.sslContext(createInsecureSslContext());
            }

            httpClient_ = builder.build();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect(final URI url) throws Exception {
        synchronized (clientLock_) {
            final Executor executor = webClient_.getExecutor();
            final CompletableFuture<java.net.http.WebSocket> connectFuture =
                    httpClient_.newWebSocketBuilder()
                        .buildAsync(url, new JdkWebSocketListenerImpl());

            executor.execute(() -> {
                try {
                    listener_.onWebSocketConnecting();
                    incomingSession_ = connectFuture.join();
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
        try {
            if (content instanceof String string) {
                outgoingSession_.sendText(string, true).join();
            }
            else if (content instanceof ByteBuffer buffer) {
                outgoingSession_.sendBinary(buffer, true).join();
            }
            else {
                throw new IllegalStateException(
                        "Unsupported content type for WebSocket.send(): expected String or ByteBuffer");
            }
        }
        catch (final IllegalStateException e) {
            throw e;
        }
        catch (final Exception e) {
            throw new IOException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeIncomingSession() {
        if (incomingSession_ != null) {
            incomingSession_.sendClose(java.net.http.WebSocket.NORMAL_CLOSURE, "").join();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeOutgoingSession() {
        if (outgoingSession_ != null) {
            outgoingSession_.sendClose(java.net.http.WebSocket.NORMAL_CLOSURE, "").join();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void closeClient() throws Exception {
        synchronized (clientLock_) {
            // HttpClient does not have a close() in Java 17;
            // simply drop the reference so it can be garbage-collected
            httpClient_ = null;
        }
    }

    private static SSLContext createInsecureSslContext()
            throws NoSuchAlgorithmException, KeyManagementException {
        final TrustManager[] trustAllCerts = {
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                @Override
                public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
                    // trust all
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
                    // trust all
                }
            }
        };
        final SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new SecureRandom());
        return sslContext;
    }

    /**
     * A {@link CookieHandler} that bridges to the {@link WebClient} cookie store.
     */
    private static class WebSocketCookieHandler extends CookieHandler {
        private final WebSocketCookieStore cookieStore_;

        WebSocketCookieHandler(final WebClient webClient) {
            cookieStore_ = new WebSocketCookieStore(webClient);
        }

        @Override
        public java.util.Map<String, java.util.List<String>> get(
                final URI uri,
                final java.util.Map<String, java.util.List<String>> requestHeaders) {
            final java.util.List<java.net.HttpCookie> cookies = cookieStore_.get(uri);
            final java.util.Map<String, java.util.List<String>> result = new java.util.HashMap<>();
            if (!cookies.isEmpty()) {
                final java.util.List<String> cookieValues = new java.util.ArrayList<>();
                for (final java.net.HttpCookie cookie : cookies) {
                    cookieValues.add(cookie.toString());
                }
                result.put("Cookie", cookieValues);
            }
            return result;
        }

        @Override
        public void put(final URI uri,
                final java.util.Map<String, java.util.List<String>> responseHeaders) {
            // not needed for WebSocket connections
        }
    }

    private class JdkWebSocketListenerImpl implements java.net.http.WebSocket.Listener {

        private StringBuilder textAccumulator_;
        private ByteArrayOutputStream binaryAccumulator_;

        JdkWebSocketListenerImpl() {
            super();
        }

        @Override
        public void onOpen(final java.net.http.WebSocket webSocket) {
            outgoingSession_ = webSocket;
            listener_.onWebSocketConnect();
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(final java.net.http.WebSocket webSocket,
                final CharSequence data, final boolean last) {
            if (textAccumulator_ == null) {
                textAccumulator_ = new StringBuilder();
            }
            textAccumulator_.append(data);

            if (last) {
                final String message = textAccumulator_.toString();
                textAccumulator_ = null;
                listener_.onWebSocketText(message);
            }

            webSocket.request(1);
            return null;
        }

        @Override
        public CompletionStage<?> onBinary(final java.net.http.WebSocket webSocket,
                final ByteBuffer data, final boolean last) {
            if (binaryAccumulator_ == null) {
                binaryAccumulator_ = new ByteArrayOutputStream();
            }
            if (data.hasArray()) {
                binaryAccumulator_.write(data.array(),
                        data.arrayOffset() + data.position(), data.remaining());
            }
            else {
                final byte[] temp = new byte[data.remaining()];
                data.get(temp);
                binaryAccumulator_.write(temp, 0, temp.length);
            }

            if (last) {
                final byte[] bytes = binaryAccumulator_.toByteArray();
                binaryAccumulator_ = null;
                listener_.onWebSocketBinary(bytes, 0, bytes.length);
            }

            webSocket.request(1);
            return null;
        }

        @Override
        public CompletionStage<?> onClose(final java.net.http.WebSocket webSocket,
                final int statusCode, final String reason) {
            outgoingSession_ = null;
            listener_.onWebSocketClose(statusCode, reason);
            return null;
        }

        @Override
        public void onError(final java.net.http.WebSocket webSocket, final Throwable error) {
            outgoingSession_ = null;
            listener_.onWebSocketError(error);
        }
    }
}
