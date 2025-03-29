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

/**
 * Helper to have no direct dependency to the WebSockt client
 * implementation used by HtmlUnit.
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
     * @param content the content to be sent
     * @throws IOException in case of error
     */
    void send(Object content) throws IOException;

    /**
     * Close the incomming session.
     *
     * @throws Exception in case of error
     */
    void closeIncommingSession() throws Exception;

    /**
     * Close the outgoing session.
     *
     * @throws Exception in case of error
     */
    void closeOutgoingSession() throws Exception;

    /**
     * Close the client.
     *
     * @throws Exception in case of error
     */
    void closeClient() throws Exception;
}
