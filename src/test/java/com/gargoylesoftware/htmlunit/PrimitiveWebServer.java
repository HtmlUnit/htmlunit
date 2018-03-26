/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A very simple implementation of a Web Server.
 * This covers some cases which are not possible with Jetty.
 *
 * @author Ahmed Ashour
 */
public class PrimitiveWebServer {

    private final int port_;
    private final String firstResponse_;
    private final String otherResponse_;
    private ServerSocket server_;
    private List<String> requests_ = new ArrayList<>();

    /**
     * Constructs a new SimpleWebServer.
     *
     * @param port the port
     * @param defaultResponse the default response, must contain the full response (to start with "HTTP/1.1 200 OK")
     */
    public PrimitiveWebServer(final int port, final String defaultResponse) {
        this(port, defaultResponse, null);
    }

    /**
     * Constructs a new SimpleWebServer.
     *
     * @param port the port
     * @param firstResponse the first response, must contain the full response (to start with "HTTP/1.1 200 OK")
     * @param otherResponse the sebsequent response, must contain the full response (to start with "HTTP/1.1 200 OK")
     */
    public PrimitiveWebServer(final int port, final String firstResponse, final String otherResponse) {
        port_ = port;
        firstResponse_ = firstResponse;
        otherResponse_ = otherResponse;
    }

    /**
     * Starts the server.
     * @throws IOException if an error occurs
     */
    public void start() throws IOException {
        server_ = new ServerSocket(port_);
        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean first = true;
                try {
                    while (true) {
                        final Socket socket = server_.accept();
                        final InputStream in = socket.getInputStream();
                        final CharArrayWriter writer = new CharArrayWriter();
                        int i;
                        while ((i = in.read()) != -1) {
                            writer.append((char) i);
                            if (i == '\n' && writer.toString().endsWith("\r\n\r\n")) {
                                break;
                            }
                        }
                        requests_.add(writer.toString());
                        try (OutputStream out = socket.getOutputStream()) {
                            final String response;
                            if (first || otherResponse_ == null) {
                                response = firstResponse_;
                            }
                            else {
                                response = otherResponse_;
                            }
                            first = false;
                            out.write(response.getBytes(StandardCharsets.UTF_8));
                        }
                    }
                }
                catch (final SocketException e) {
                    //ignore
                }
                catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Stops the server.
     * @throws IOException if an error occurs
     */
    public void stop() throws IOException {
        server_.close();
    }

    /**
     * Returns the saved requests.
     * @return the requests
     */
    public List<String> getRequests() {
        return requests_;
    }

    /**
     * Clears all requests.
     */
    public void clearRequests() {
        requests_.clear();
    }
}
