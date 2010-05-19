/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.ssl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.params.HttpParams;

/**
 * A completely insecure (yet very easy to use) SSL socket factory. This socket factory will
 * establish connections to any server from any client, regardless of credentials or the lack
 * thereof. This is especially useful when you are trying to connect to a server with expired or
 * corrupt certificates... this class doesn't care!
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Nicolas Belisle
 * @see com.gargoylesoftware.htmlunit.WebClient#setUseInsecureSSL(boolean)
 */
public class InsecureSSLProtocolSocketFactory implements SocketFactory {

    private SSLSocketFactory decoratedSSLSocketFactory_;

    /**
     * Creates a new insecure SSL protocol socket factory.
     *
     * @throws GeneralSecurityException if a security error occurs
     */
    public InsecureSSLProtocolSocketFactory() throws GeneralSecurityException {
        final SSLContext context = SSLContext.getInstance("SSL");
        context.init(null, new TrustManager[] {new InsecureTrustManager()}, null);
        decoratedSSLSocketFactory_ = new SSLSocketFactory(SSLContext.getInstance("SSL"));
    }

    /**
     * {@inheritDoc}
     */
    public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
        throws IOException, UnknownHostException {
        return decoratedSSLSocketFactory_.createSocket(socket, host, port, autoClose);
    }

    /**
     * {@inheritDoc}
     */
    public Socket connectSocket(final Socket sock, final String host, final int port, final InetAddress localAddress,
        final int localPort, final HttpParams params)
        throws IOException, UnknownHostException, ConnectTimeoutException {
        return decoratedSSLSocketFactory_.connectSocket(sock, host, port, localAddress, localPort, params);
    }

    /**
     * {@inheritDoc}
     */
    public Socket createSocket() throws IOException {
        return decoratedSSLSocketFactory_.createSocket();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isSecure(final Socket sock) throws IllegalArgumentException {
        return decoratedSSLSocketFactory_.isSecure(sock);
    }

}
