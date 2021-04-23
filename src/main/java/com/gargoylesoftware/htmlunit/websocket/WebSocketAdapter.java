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

/**
 * Helper to have no direct dependency to the WebSockt client
 * implementation used by HtmlUnit.
 *
 * @author Ronald Brill
 */
public interface WebSocketAdapter {
    void start() throws Exception;

    void connect(URI url) throws Exception;

    void send(Object content) throws IOException;

    void closeIncommingSession() throws Exception;

    void closeOutgoingSession() throws Exception;

    void closeClient() throws Exception;

    void onWebSocketConnecting();

    void onWebSocketConnect();

    void onWebSocketClose(int statusCode, String reason);

    void onWebSocketText(String message);

    void onWebSocketBinary(byte[] data, int offset, int length);

    void onWebSocketConnectError(Throwable cause);

    void onWebSocketError(Throwable cause);
}
