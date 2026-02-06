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

/**
 * Helper to have no direct dependency to the WebSockt client
 * implementation used by HtmlUnit. This is used from the js code.
 *
 * @author Ronald Brill
 */
public interface WebSocketAdapter {

    /**
     * Starts the client.
     *
     * @throws Exception in case of error
     */
    void start() throws Exception;

    /**
     * Connects to the given {@link URI}.
     *
     * @param url the target url
     * @throws Exception in case of error
     */
    void connect(URI url) throws Exception;

    /**
     * Sends the provided content.
     *
     * @param message the message to be sent
     * @throws IOException in case of error
     */
    void sendText(String message) throws IOException;

    /**
     * Sends the provided content.
     *
     * @param buffer the bytes to be sent
     * @throws IOException in case of error
     */
    void sendBinary(ByteBuffer buffer) throws IOException;

    /**
     * Close the session.
     *
     * @throws Exception in case of error
     */
    void closeSession() throws Exception;

    /**
     * Close the client.
     *
     * @throws Exception in case of error
     */
    void closeClient() throws Exception;
}
