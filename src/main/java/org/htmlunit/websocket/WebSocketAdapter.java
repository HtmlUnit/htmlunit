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
import java.util.List;

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
     * Connects to the given {@link URI} and offers the given sub-protocols in the handshake.
     * The default implementation ignores the sub-protocols, so adapters without support keep working
     * (no sub-protocol is selected then).
     *
     * @param url the target url
     * @param protocols the sub-protocols to offer, may be empty
     * @throws Exception in case of error
     */
    default void connect(final URI url, final List<String> protocols) throws Exception {
        connect(url);
    }

    /**
     * Returns the sub-protocol the server selected in the handshake. Only meaningful once the
     * connection is open.
     *
     * @return the selected sub-protocol, or an empty string if the server did not select one
     */
    default String getSelectedProtocol() {
        return "";
    }

    /**
     * Sends the provided content.
     *
     * @param content the content to be sent
     * @throws IOException in case of error
     */
    void send(Object content) throws IOException;

    /**
     * Close the incoming session.
     *
     * @throws Exception in case of error
     */
    void closeIncomingSession() throws Exception;

    /**
     * Close the outgoing session.
     *
     * @throws Exception in case of error
     */
    void closeOutgoingSession() throws Exception;

    /**
     * Close the outgoing session with the given status code and reason.
     * The status code 1005 means that no status code at all is sent (an empty close frame), the reason
     * is only sent together with a status code.
     * The default implementation ignores the arguments and does the same as {@link #closeOutgoingSession()},
     * so existing implementations keep working.
     *
     * @param statusCode the status code of the close frame
     * @param reason the reason of the close frame, may be empty
     * @throws Exception in case of error
     */
    default void closeOutgoingSession(final int statusCode, final String reason) throws Exception {
        closeOutgoingSession();
    }

    /**
     * Close the client.
     *
     * @throws Exception in case of error
     */
    void closeClient() throws Exception;
}
