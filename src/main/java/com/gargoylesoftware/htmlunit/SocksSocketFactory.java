/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import org.apache.http.HttpHost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.SchemeSocketFactory;
import org.apache.http.params.HttpParams;

/**
 * SOCKS aware {@link SchemeSocketFactory}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 */
class SocksSocketFactory extends PlainSocketFactory {
    private static final String SOCKS_PROXY = "htmlunit.socksproxy";

    static void setSocksProxy(final HttpParams parameters, final HttpHost socksProxy) {
        parameters.setParameter(SOCKS_PROXY, socksProxy);
    }

    static HttpHost getSocksProxy(final HttpParams parameters) {
        return (HttpHost) parameters.getParameter(SOCKS_PROXY);
    }

    static Socket createSocketWithSocksProxy(final HttpHost socksProxy) {
        final InetSocketAddress address = new InetSocketAddress(socksProxy.getHostName(), socksProxy.getPort());
        final Proxy proxy = new Proxy(Proxy.Type.SOCKS, address);
        return new Socket(proxy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Socket createSocket(final HttpParams params) {
        final HttpHost socksProxy = getSocksProxy(params);
        if (socksProxy != null) {
            return createSocketWithSocksProxy(socksProxy);
        }
        return super.createSocket(params);
    }
}
