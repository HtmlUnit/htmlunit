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
package com.gargoylesoftware.htmlunit.httpclient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;

import org.apache.hc.client5.http.socket.PlainConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.protocol.HttpContext;

/**
 * SOCKS aware {@link org.apache.hc.client5.http.socket.ConnectionSocketFactory}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 */
public class SocksConnectionSocketFactory extends PlainConnectionSocketFactory {
    private static final String SOCKS_PROXY = "htmlunit.socksproxy";

    /**
     * Enables the socks proxy.
     * @param context the HttpContext
     * @param socksProxy the HttpHost
     */
    public static void setSocksProxy(final HttpContext context, final HttpHost socksProxy) {
        context.setAttribute(SOCKS_PROXY, socksProxy);
    }

    static HttpHost getSocksProxy(final HttpContext context) {
        return (HttpHost) context.getAttribute(SOCKS_PROXY);
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
    public Socket createSocket(final HttpContext context) throws IOException {
        final HttpHost socksProxy = getSocksProxy(context);
        if (socksProxy != null) {
            return createSocketWithSocksProxy(socksProxy);
        }
        return super.createSocket(context);
    }
}
