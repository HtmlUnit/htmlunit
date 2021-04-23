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

import java.io.CharArrayWriter;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

/**
 * A very simple implementation of a Web Server.
 * This covers some cases which are not possible with Jetty.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class PrimitiveWebServer implements Closeable {

    private final int port_;
    private final String firstResponse_;
    private final String otherResponse_;
    private ServerSocket server_;
    private Charset charset_ = StandardCharsets.ISO_8859_1;
    private List<String> requests_ = new ArrayList<>();

    /**
     * Constructs a new SimpleWebServer.
     *
     * @param charset the charset
     * @param firstResponse the first response, must contain the full response (to start with "HTTP/1.1 200 OK")
     * @param otherResponse the subsequent response, must contain the full response (to start with "HTTP/1.1 200 OK")
     * @throws Exception in case of error
     */
    public PrimitiveWebServer(final Charset charset, final String firstResponse, final String otherResponse)
            throws Exception {
        port_ = WebTestCase.PORT_PRIMITIVE_SERVER;
        firstResponse_ = firstResponse;
        otherResponse_ = otherResponse;
        if (charset != null) {
            charset_ = charset;
        }

        start();
    }

    /**
     * Starts the server.
     * @throws Exception if an error occurs
     */
    private void start() throws Exception {
        server_ = new ServerSocket();
        server_.setReuseAddress(true);

        final long maxWait = System.currentTimeMillis() + WebServerTestCase.BIND_TIMEOUT;

        while (true) {
            try {
                server_.bind(new InetSocketAddress(port_));
                break;
            }
            catch (final BindException e) {
                if (System.currentTimeMillis() > maxWait) {
                    throw (BindException) new BindException("Port " + port_ + " is already in use").initCause(e);
                }
                Thread.sleep(200);
            }
        }

        new Thread(new Runnable() {

            @Override
            public void run() {
                boolean first = true;
                try {
                    while (true) {
                        final Socket socket = server_.accept();
                        final InputStream in = socket.getInputStream();
                        final CharArrayWriter writer = new CharArrayWriter();

                        String requestString = writer.toString();
                        int i;

                        while ((i = in.read()) != -1) {
                            writer.append((char) i);
                            requestString = writer.toString();

                            if (i == '\n' && requestString.endsWith("\r\n\r\n")) {
                                break;
                            }
                        }

                        final int contentLenghtPos = StringUtils.indexOfIgnoreCase(requestString, "Content-Length:");
                        if (contentLenghtPos > -1) {
                            final int endPos = requestString.indexOf('\n', contentLenghtPos + 16);
                            final String toParse = requestString.substring(contentLenghtPos + 16, endPos);
                            final int contentLenght = Integer.parseInt(toParse.trim());

                            if (contentLenght > 0) {
                                final byte[] charArray = new byte[contentLenght];
                                in.read(charArray, 0, contentLenght);
                                requestString += new String(charArray);
                            }
                        }

                        final String response;
                        if (requestString.length() < 1
                                || requestString.contains("/favicon.ico")) {
                            response = "HTTP/1.1 404 Not Found\r\n"
                                    + "Content-Length: 0\r\n"
                                    + "Connection: close\r\n"
                                    + "\r\n";
                        }
                        else {
                            requests_.add(requestString);
                            if (first || otherResponse_ == null) {
                                response = firstResponse_;
                            }
                            else {
                                response = otherResponse_;
                            }
                            first = false;
                        }

                        try (OutputStream out = socket.getOutputStream()) {
                            out.write(response.getBytes(charset_));
                        }
                    }
                }
                catch (final SocketException e) {
                    // ignore
                }
                catch (final Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * Stops the server.
     * @throws IOException if an error occurs
     */
    @Override
    public void close() throws IOException {
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
     * Returns the port.
     * @return the port
     */
    public int getPort() {
        return port_;
    }

    /**
     * Clears all requests.
     */
    public void clearRequests() {
        requests_.clear();
    }
}
