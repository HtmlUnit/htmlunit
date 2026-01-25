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
package org.htmlunit.util;

import static org.eclipse.jetty.http.HttpVersion.HTTP_1_1;

import java.io.IOException;
import java.net.BindException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.EnumSet;
import java.util.Map;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.htmlunit.HttpWebConnectionInsecureSSLWithClientCertificateTest;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.WebServerTestCase.SSLVariant;
import org.htmlunit.WebTestCase;

/**
 * Helpers to centralize Jetty access.
 *
 * @author Ronald Brill
 */
public final class JettyServerUtils {

    /**
     * Starts a web server with the given configuration.
     *
     * @param port the port to bind to
     * @param resourceBase the resource base directory
     * @param servlets map of servlet path specs to servlet classes
     * @param serverCharset the charset for the server (can be null)
     * @param isBasicAuthentication whether to enable basic authentication
     * @param sslVariant the SSL variant to use (can be null for no SSL)
     * @return the started server
     * @throws Exception if server startup fails
     */
    public static Server startWebServer(final int port, final String resourceBase,
            final Map<String, Class<? extends Servlet>> servlets,
            final Charset serverCharset,
            final boolean isBasicAuthentication,
            final WebServerTestCase.SSLVariant sslVariant) throws Exception {
        final Server server = buildServer(port);

        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.setResourceBase(resourceBase);

        if (serverCharset != null) {
            AsciiEncodingFilter.CHARSET_ = serverCharset;
            context.addFilter(AsciiEncodingFilter.class, "/*",
                    EnumSet.of(DispatcherType.INCLUDE, DispatcherType.REQUEST));
        }

        if (isBasicAuthentication) {
            final Constraint constraint = new Constraint(Constraint.__BASIC_AUTH, "user");
            constraint.setAuthenticate(true);

            final ConstraintMapping constraintMapping = new ConstraintMapping();
            constraintMapping.setConstraint(constraint);
            constraintMapping.setPathSpec("/*");

            final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
            securityHandler.setLoginService(new HashLoginService("MyRealm", "./src/test/resources/realm.properties"));
            securityHandler.setAuthMethod(Constraint.__BASIC_AUTH);
            securityHandler.setConstraintMappings(new ConstraintMapping[]{constraintMapping});

            context.setSecurityHandler(securityHandler);
        }

        boolean overwritesDefaultPath = false;
        if (servlets != null) {
            for (final Map.Entry<String, Class<? extends Servlet>> entry : servlets.entrySet()) {
                final String pathSpec = entry.getKey();
                final Class<? extends Servlet> servlet = entry.getValue();
                context.addServlet(servlet, pathSpec);

                // disable defaults if someone likes to register his own root servlet
                overwritesDefaultPath |= "/".equals(pathSpec);
            }
        }

        if (overwritesDefaultPath) {
            context.addServlet(DefaultServlet.class, "/favicon.ico");
        }
        else {
            // For static resources
            final ServletHolder defaultHolder = new ServletHolder("default", DefaultServlet.class);
            defaultHolder.setInitParameter("resourceBase", resourceBase);
            defaultHolder.setInitParameter("dirAllowed", "true");
            context.addServlet(defaultHolder, "/");
        }

        server.setHandler(context);

        if (sslVariant != null && sslVariant != SSLVariant.NONE) {
            org.eclipse.jetty.util.ssl.SslContextFactory.Server contextFactory = null;

            switch (sslVariant) {
                case INSECURE: {
                    final URL url = HttpWebConnectionInsecureSSLWithClientCertificateTest.class
                            .getClassLoader().getResource("insecureSSL.pfx");

                    contextFactory = new org.eclipse.jetty.util.ssl.SslContextFactory.Server();
                    contextFactory.setKeyStorePath(url.toExternalForm());
                    contextFactory.setKeyStorePassword("nopassword");
                    contextFactory.setEndpointIdentificationAlgorithm(null);
                    break;
                }
                case SELF_SIGNED: {
                    final URL url = HttpWebConnectionInsecureSSLWithClientCertificateTest.class
                            .getClassLoader().getResource("self-signed-cert.keystore");

                    contextFactory = new org.eclipse.jetty.util.ssl.SslContextFactory.Server();
                    contextFactory.setKeyStoreType("jks");
                    contextFactory.setKeyStorePath(url.toExternalForm());
                    contextFactory.setKeyStorePassword("nopassword");
                    contextFactory.setEndpointIdentificationAlgorithm(null);
                    break;
                }
                default:
                    break;
            }

            if (contextFactory != null) {
                final HttpConfiguration sslConfiguration = new HttpConfiguration();
                final SecureRequestCustomizer secureRequestCustomizer = new SecureRequestCustomizer();

                // Jetty 10, SecureRequestCustomizer performs SNI (Server Name Indication) host checking by default,
                // which causes issues with localhost and self-signed certificates.
                // without this we see 400 Bad Request error's
                secureRequestCustomizer.setSniHostCheck(false);
                sslConfiguration.addCustomizer(secureRequestCustomizer);

                final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(sslConfiguration);

                final SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(contextFactory, HTTP_1_1.toString());
                final ServerConnector connector = new ServerConnector(server, sslConnectionFactory, httpConnectionFactory);
                connector.setPort(WebTestCase.PORT2);
                server.addConnector(connector);
            }
        }

        tryStart(port, server);

        return server;
    }

    /**
     * A special version of the server; more or less a complete EE server supporting WEB-INF.
     *
     * Creates and starts a web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned Server after the test</b>
     *
     * @param port the port to which the server is bound
     * @param resourceBase the base of resources for the default context
     * @param classpath additional classpath entries to add (may be null)
     * @return the newly created server
     * @throws Exception if an error occurs
     */
    public static Server startWebAppServer(final int port, final String resourceBase, final String[] classpath) throws Exception {

        final Server server = buildServer(port);

        final WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase(resourceBase);

        final WebAppClassLoader loader = new WebAppClassLoader(context);
        if (classpath != null) {
            for (final String path : classpath) {
                loader.addClassPath(path);
            }
        }
        context.setClassLoader(loader);
        server.setHandler(context);

        tryStart(port, server);
        return server;
    }

    private static Server buildServer(final int port) {
        final QueuedThreadPool threadPool = new QueuedThreadPool(10, 2);

        final Server server = new Server(threadPool);

        final ServerConnector connector = new ServerConnector(server, 1, -1, new HttpConnectionFactory());
        connector.setPort(port);
        server.setConnectors(new Connector[] {connector});

        return server;
    }

    /**
     * Starts the server; handles BindExceptions and retries.
     * @param port the port only used for the error message
     * @param server the server to start
     * @throws Exception in case of error
     */
    private static void tryStart(final int port, final Server server) throws Exception {
        final long maxWait = System.currentTimeMillis() + WebServerTestCase.BIND_TIMEOUT;

        while (true) {
            try {
                server.start();
                return;
            }
            catch (final BindException e) {
                if (System.currentTimeMillis() > maxWait) {
                    // destroy the server to free all associated resources
                    stopServer(server);

                    throw (BindException) new BindException("Port " + port + " is already in use").initCause(e);
                }
                Thread.sleep(200);
            }
            catch (final IOException e) {
                // looks like newer jetty already catches the bind exception
                if (e.getCause() instanceof BindException) {
                    if (System.currentTimeMillis() > maxWait) {
                        // destroy the server to free all associated resources
                        server.stop();
                        server.destroy();

                        throw (BindException) new BindException("Port " + port + " is already in use").initCause(e);
                    }
                    Thread.sleep(200);
                }
                else {
                    // destroy the server to free all associated resources
                    server.stop();
                    server.destroy();

                    throw e;
                }
            }
        }
    }

    public static void stopServer(Server server) throws Exception {
        if (server != null) {
            server.stop();
            server.destroy();
        }
    }

    /**
     * Needed as Jetty starting from 9.4.4 expects UTF-8 encoding by default.
     */
    public static class AsciiEncodingFilter implements Filter {

        private static Charset CHARSET_;

        /**
         * {@inheritDoc}
         */
        @Override
        public void init(final FilterConfig filterConfig) throws ServletException {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
                throws IOException, ServletException {
            if (request instanceof Request) {
                ((Request) request).setQueryEncoding(CHARSET_.name());
            }
            chain.doFilter(request, response);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void destroy() {
        }
    }
}
