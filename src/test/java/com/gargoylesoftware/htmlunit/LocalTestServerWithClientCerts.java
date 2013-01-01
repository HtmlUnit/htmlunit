/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpResponseFactory;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.localserver.EchoHandler;
import org.apache.http.localserver.RandomHandler;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpExpectationVerifier;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

/**
 * Local HTTP server for tests that require one, with a client certificate authorization.
 * The code is exactly as {@link org.apache.http.localserver.LocalTestServer},
 * but with {@link SSLServerSocket#setNeedClientAuth(boolean)}.
 *
 * @version $Revision$
 * @author Martin Huber
 */
public class LocalTestServerWithClientCerts {

    /**
     * The local address to bind to.
     * The host is an IP number rather than "localhost" to avoid surprises
     * on hosts that map "localhost" to an IPv6 address or something else.
     * The port is 0 to let the system pick one.
     */
    public static final InetSocketAddress TEST_SERVER_ADDR =
            new InetSocketAddress("127.0.0.1", 0);

    /** The request handler registry. */
    private final HttpRequestHandlerRegistry handlerRegistry_;

    private final HttpService httpservice_;

    /** Optional SSL context */
    private final SSLContext sslcontext_;

    /** The server socket, while being served. */
    private volatile ServerSocket servicedSocket_;

    /** The request listening thread, while listening. */
    private volatile ListenerThread listenerThread_;

    /** Set of active worker threads */
    private final Set<Worker> workers_;

    /** The number of connections this accepted. */
    private final AtomicInteger acceptedConnections_ = new AtomicInteger(0);

    /**
     * Creates a new test server.
     *
     * @param proc      the HTTP processors to be used by the server, or
     *                  <code>null</code> to use a
     *                  {@link #newProcessor default} processor
     * @param reuseStrat the connection reuse strategy to be used by the
     *                  server, or <code>null</code> to use
     *                  {@link #newConnectionReuseStrategy() default}
     *                  strategy.
     * @param params    the parameters to be used by the server, or
     *                  <code>null</code> to use
     *                  {@link #newDefaultParams default} parameters
     * @param sslcontext optional SSL context if the server is to leverage
     *                   SSL/TLS transport security
     * @param responseFactory factory
     * @param expectationVerifier verifier
     */
    public LocalTestServerWithClientCerts(
            final BasicHttpProcessor proc,
            final ConnectionReuseStrategy reuseStrat,
            final HttpResponseFactory responseFactory,
            final HttpExpectationVerifier expectationVerifier,
            final HttpParams params,
            final SSLContext sslcontext) {
        super();
        this.handlerRegistry_ = new HttpRequestHandlerRegistry();
        this.workers_ = Collections.synchronizedSet(new HashSet<Worker>());
        this.httpservice_ = new HttpService(
                proc != null ? proc : newProcessor(),
                reuseStrat != null ? reuseStrat : newConnectionReuseStrategy(),
                responseFactory != null ? responseFactory : newHttpResponseFactory(),
                handlerRegistry_,
                expectationVerifier,
                params != null ? params : newDefaultParams());
        this.sslcontext_ = sslcontext;
    }

    /**
     * Creates a new test server with SSL/TLS encryption.
     *
     * @param sslcontext SSL context
     */
    public LocalTestServerWithClientCerts(final SSLContext sslcontext) {
        this(null, null, null, null, null, sslcontext);
    }

    /**
     * Creates a new test server.
     *
     * @param proc      the HTTP processors to be used by the server, or
     *                  <code>null</code> to use a
     *                  {@link #newProcessor default} processor
     * @param params    the parameters to be used by the server, or
     *                  <code>null</code> to use
     *                  {@link #newDefaultParams default} parameters
     */
    public LocalTestServerWithClientCerts(
            final BasicHttpProcessor proc,
            final HttpParams params) {
        this(proc, null, null, null, params, null);
    }

    /**
     * Obtains an HTTP protocol processor with default interceptors.
     *
     * @return a protocol processor for server-side use
     */
    protected HttpProcessor newProcessor() {
        return new ImmutableHttpProcessor(
                new HttpResponseInterceptor[] {
                    new ResponseDate(),
                    new ResponseServer(),
                    new ResponseContent(),
                    new ResponseConnControl()
                });
    }

    /**
     * Obtains a set of reasonable default parameters for a server.
     *
     * @return default parameters
     */
    protected HttpParams newDefaultParams() {
        final HttpParams params = new SyncBasicHttpParams();
        params
                .setIntParameter(CoreConnectionPNames.SO_TIMEOUT, 60000)
                .setIntParameter(CoreConnectionPNames.SOCKET_BUFFER_SIZE, 8 * 1024)
                .setBooleanParameter(CoreConnectionPNames.STALE_CONNECTION_CHECK, false)
                .setBooleanParameter(CoreConnectionPNames.TCP_NODELAY, true)
                .setParameter(CoreProtocolPNames.ORIGIN_SERVER,
                        "LocalTestServer/1.1");
        return params;
    }

    /**
     * Returns a new ConnectionReuseStrategy.
     * @return a new ConnectionReuseStrategy
     */
    protected ConnectionReuseStrategy newConnectionReuseStrategy() {
        return new DefaultConnectionReuseStrategy();
    }

    /**
     * Returns a new HttpResponseFactory.
     * @return a new HttpResponseFactory
     */
    protected HttpResponseFactory newHttpResponseFactory() {
        return new DefaultHttpResponseFactory();
    }

    /**
     * Returns the number of connections this test server has accepted.
     * @return the count
     */
    public int getAcceptedConnectionCount() {
        return acceptedConnections_.get();
    }

    /**
     * {@link #register Registers} a set of default request handlers.
     * <pre>
     * URI pattern      Handler
     * -----------      -------
     * /echo/*          {@link EchoHandler EchoHandler}
     * /random/*        {@link RandomHandler RandomHandler}
     * </pre>
     */
    public void registerDefaultHandlers() {
        handlerRegistry_.register("/echo/*", new EchoHandler());
        handlerRegistry_.register("/random/*", new RandomHandler());
    }

    /**
     * Registers a handler with the local registry.
     *
     * @param pattern   the URL pattern to match
     * @param handler   the handler to apply
     */
    public void register(final String pattern, final HttpRequestHandler handler) {
        handlerRegistry_.register(pattern, handler);
    }

    /**
     * Unregisters a handler from the local registry.
     *
     * @param pattern   the URL pattern
     */
    public void unregister(final String pattern) {
        handlerRegistry_.unregister(pattern);
    }

    /**
     * Starts this test server.
     * @throws Exception if an error occurs
     */
    public void start() throws Exception {
        if (servicedSocket_ != null) {
            throw new IllegalStateException(this.toString() + " already running");
        }
        final ServerSocket ssock;
        if (sslcontext_ != null) {
            final SSLServerSocketFactory sf = sslcontext_.getServerSocketFactory();
            ssock = sf.createServerSocket();
            ((SSLServerSocket) ssock).setNeedClientAuth(true);
        }
        else {
            ssock = new ServerSocket();
        }

        ssock.setReuseAddress(true); // probably pointless for port '0'
        ssock.bind(TEST_SERVER_ADDR);
        servicedSocket_ = ssock;

        listenerThread_ = new ListenerThread();
        listenerThread_.setDaemon(false);
        listenerThread_.start();
    }

    /**
     * Stops this test server.
     * @throws Exception if an error occurs
     */
    public void stop() throws Exception {
        if (servicedSocket_ == null) {
            return; // not running
        }
        final ListenerThread t = listenerThread_;
        if (t != null) {
            t.shutdown();
        }
        synchronized (workers_) {
            for (final Iterator<Worker> it = workers_.iterator(); it.hasNext();) {
                final Worker worker = it.next();
                worker.shutdown();
            }
        }
    }

    /**
     * Awaits termination.
     * @param timeMs the time in millis
     * @throws InterruptedException if an error occurs
     */
    public void awaitTermination(final long timeMs) throws InterruptedException {
        if (listenerThread_ != null) {
            listenerThread_.join(timeMs);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final ServerSocket ssock = servicedSocket_; // avoid synchronization
        final StringBuilder sb = new StringBuilder(80);
        sb.append("LocalTestServer/");
        if (ssock == null) {
            sb.append("stopped");
        }
        else {
            sb.append(ssock.getLocalSocketAddress());
        }
        return sb.toString();
    }

    /**
     * Obtains the local address the server is listening on.
     *
     * @return the service address
     */
    public InetSocketAddress getServiceAddress() {
        final ServerSocket ssock = servicedSocket_; // avoid synchronization
        if (ssock == null) {
            throw new IllegalStateException("not running");
        }
        return (InetSocketAddress) ssock.getLocalSocketAddress();
    }

    /**
     * The request listener.
     * Accepts incoming connections and launches a service thread.
     */
    class ListenerThread extends Thread {

        private volatile Exception exception_;

        ListenerThread() {
            super();
        }

        @Override
        public void run() {
            try {
                while (!interrupted()) {
                    final Socket socket = servicedSocket_.accept();
                    acceptedConnections_.incrementAndGet();
                    final DefaultHttpServerConnection conn = new DefaultHttpServerConnection();
                    conn.bind(socket, httpservice_.getParams());
                    // Start worker thread
                    final Worker worker = new Worker(conn);
                    workers_.add(worker);
                    worker.setDaemon(true);
                    worker.start();
                }
            }
            catch (final Exception ex) {
                this.exception_ = ex;
            }
            finally {
                try {
                    servicedSocket_.close();
                }
                catch (final IOException ignore) {
                    //nothing
                }
            }
        }

        public void shutdown() {
            interrupt();
            try {
                servicedSocket_.close();
            }
            catch (final IOException ignore) {
                //nothing
            }
        }

        public Exception getException() {
            return this.exception_;
        }
    }

    class Worker extends Thread {

        private final HttpServerConnection conn_;

        private volatile Exception exception_;

        public Worker(final HttpServerConnection conn) {
            this.conn_ = conn;
        }

        @Override
        public void run() {
            final HttpContext context = new BasicHttpContext();
            try {
                while (this.conn_.isOpen() && !Thread.interrupted()) {
                    httpservice_.handleRequest(this.conn_, context);
                }
            }
            catch (final Exception ex) {
                this.exception_ = ex;
            }
            finally {
                workers_.remove(this);
                try {
                    this.conn_.shutdown();
                }
                catch (final IOException ignore) {
                    //nothing
                }
            }
        }

        public void shutdown() {
            interrupt();
            try {
                this.conn_.shutdown();
            }
            catch (final IOException ignore) {
                //nothing
            }
        }

        public Exception getException() {
            return this.exception_;
        }
    }

}
