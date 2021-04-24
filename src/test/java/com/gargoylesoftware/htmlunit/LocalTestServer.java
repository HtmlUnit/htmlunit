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
package com.gargoylesoftware.htmlunit;

import javax.net.ssl.SSLContext;

import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.localserver.LocalServerTestBase;
import org.apache.http.protocol.HttpRequestHandler;

/**
 * Simple HTTPS server, a class with same name existed in HttpComponents 4.3 test suite.
 *
 * @author Ahmed Ashour
 */
public class LocalTestServer extends LocalServerTestBase {

    /**
     * The constructor.
     *
     * @param sslContext the SSL context
     * @throws Exception if an error occurs
     */
    public LocalTestServer(final SSLContext sslContext) throws Exception {
        super(ProtocolScheme.https);
        super.setUp();
        serverBootstrap.setSslContext(sslContext);
    }

    /**
     * Registers the given {@link HttpRequestHandler} as a handler for URIs matching the given pattern.
     * @param pattern the pattern
     * @param handler the handler
     */
    public void register(final String pattern, final HttpRequestHandler handler) {
        serverBootstrap.registerHandler(pattern, handler);
    }

    /**
     * Returns the underlying server.
     * @return the server
     */
    public HttpServer getServer() {
        return server;
    }
}
