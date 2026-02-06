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

/**
 * Helper to have no direct dependency to the WebSockt client
 * implementation used by HtmlUnit.
 *
 * @author Ronald Brill
 */
public interface WebSocketListener {

    /**
     * Callback to be called when connecting.
     */
    void onWebSocketConnecting();

    /**
     * Callback to be called when opened.
     */
    void onWebSocketOpen();

    /**
     * Callback to be called when closed.
     *
     * @param statusCode the status code
     * @param reason the reason
     */
    void onWebSocketClose(int statusCode, String reason);

    /**
     * Callback to be called when closed.
     *
     * @param message the message
     */
    void onWebSocketText(String message);

    /**
     * Callback to be called when binary data retrieved.
     *
     * @param buffer the bytes
     */
    void onWebSocketBinary(byte[] buffer);

    /**
     * Callback to be called on connect error.
     *
     * @param cause the cause
     */
    void onWebSocketConnectError(Throwable cause);

    /**
     * Callback to be called on error.
     *
     * @param cause the cause
     */
    void onWebSocketError(Throwable cause);
}
