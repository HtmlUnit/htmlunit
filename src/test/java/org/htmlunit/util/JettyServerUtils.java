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

import java.io.File;
import java.io.IOException;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.ee10.servlet.DefaultServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.ee10.servlet.security.ConstraintMapping;
import org.eclipse.jetty.ee10.servlet.security.ConstraintSecurityHandler;
import org.eclipse.jetty.ee10.websocket.server.config.JettyWebSocketServletContainerInitializer;
import org.eclipse.jetty.http.CookieCompliance;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.UriCompliance;
import org.eclipse.jetty.security.Constraint;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ErrorHandler;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.api.Session.Listener.AutoDemanding;
import org.htmlunit.WebServerTestCase;
import org.htmlunit.WebServerTestCase.SSLVariant;
import org.htmlunit.WebTestCase;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;

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

        return startWebServer(port, resourceBase, servlets, null, serverCharset, isBasicAuthentication, sslVariant);
    }

    /**
     * Starts a web server with the given configuration.
     *
     * @param port the port to bind to
     * @param resourceBase the resource base directory
     * @param servlets map of servlet path specs to servlet classes
     * @param socketListeners map of WebSocket path specs to listener classes
     * @param serverCharset the charset for the server (can be null)
     * @param isBasicAuthentication whether to enable basic authentication
     * @param sslVariant the SSL variant to use (can be null for no SSL)
     * @return the started server
     * @throws Exception if server startup fails
     */
    public static Server startWebServer(final int port, final String resourceBase,
            final Map<String, Class<? extends Servlet>> servlets,
            final Map<String, Class<? extends AutoDemanding>> socketListeners,
            final Charset serverCharset,
            final boolean isBasicAuthentication,
            final WebServerTestCase.SSLVariant sslVariant) throws Exception {
        final Server server = buildServer(port, serverCharset);

        final ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        final Resource baseResource = ResourceFactory.of(context).newResource(getResourceBasePath(resourceBase));
        context.setBaseResource(baseResource);

        // context.setErrorHandler(new ConsoleErrorHandler());

        if (isBasicAuthentication) {
            final ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();

            // Create and configure login service
            final Path realmPath = Paths.get("./src/test/resources/realm.properties").toAbsolutePath();
            if (!Files.exists(realmPath)) {
                throw new IOException("Realm file not found: '" + realmPath + "'");
            }

            final Resource realmResource = ResourceFactory.of(context).newResource(realmPath);
            final HashLoginService loginService = new HashLoginService("MyRealm", realmResource);
            securityHandler.setLoginService(loginService);

            // Set authenticator
            securityHandler.setAuthenticator(new BasicAuthenticator());

            // Create constraint that requires "user" role
            final Constraint constraint = new Constraint.Builder()
                                                .authorization(Constraint.Authorization.SPECIFIC_ROLE)
                                                .roles("user")
                                                .build();

            // Apply constraint to all paths
            final ConstraintMapping mapping = new ConstraintMapping();
            mapping.setConstraint(constraint);
            mapping.setPathSpec("/*");

            securityHandler.setConstraintMappings(java.util.List.of(mapping));

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
            // For static resources - use DefaultServlet
            final ServletHolder defaultHolder = new ServletHolder("default", DefaultServlet.class);
            // Don't set resourceBase here - it will use the context's base resource
            // defaultHolder.setInitParameter("resourceBase", resourceBase);
            defaultHolder.setInitParameter("dirAllowed", "true");
            defaultHolder.setInitParameter("pathInfoOnly", "true");
            context.addServlet(defaultHolder, "/");
        }

        server.setHandler(context);

        if (socketListeners != null) {
            JettyWebSocketServletContainerInitializer.configure(context, (servletContext, container) -> {
                // Configure the ServerContainer.
                container.setMaxTextMessageSize(128 * 1024);

                // Add your WebSocket endpoint(s) to the JettyWebSocketServerContainer.
                for (final Map.Entry<String, Class<? extends AutoDemanding>> entry : socketListeners.entrySet()) {
                    container.addMapping(entry.getKey(), entry.getValue());
                }
            });
        }

        if (sslVariant != null && sslVariant != SSLVariant.NONE) {
            SslContextFactory.Server contextFactory = null;

            switch (sslVariant) {
                case INSECURE: {
                    final URL url = WebServerTestCase.class.getClassLoader().getResource("insecureSSL.pfx");

                    contextFactory = new SslContextFactory.Server();
                    contextFactory.setKeyStorePath(url.toExternalForm());
                    contextFactory.setKeyStorePassword("nopassword");
                    contextFactory.setEndpointIdentificationAlgorithm(null);
                    break;
                }
                case SELF_SIGNED: {
                    final URL url = WebServerTestCase.class.getClassLoader().getResource("self-signed-cert.keystore");

                    contextFactory = new SslContextFactory.Server();
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

                // Jetty 12, SecureRequestCustomizer performs SNI (Server Name Indication) host checking by default,
                // which causes issues with localhost and self-signed certificates.
                // without this we see 400 Bad Request error's
                secureRequestCustomizer.setSniHostCheck(false);
                sslConfiguration.addCustomizer(secureRequestCustomizer);

                final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(sslConfiguration);

                final SslConnectionFactory sslConnectionFactory = new SslConnectionFactory(contextFactory, HttpVersion.HTTP_1_1.asString());
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
    public static Server startWebAppEE8Server(final int port, final String resourceBase, final String[] classpath) throws Exception {
        final Server server = buildServer(port, null);

        final org.eclipse.jetty.ee8.webapp.WebAppContext context = new org.eclipse.jetty.ee8.webapp.WebAppContext();
        context.setContextPath("/");
        final Resource baseResource = ResourceFactory.of(context).newResource(getResourceBasePath(resourceBase));
        context.setBaseResource(baseResource);

        // context.setErrorHandler(new ConsoleErrorHandler());

        if (classpath != null && classpath.length > 0) {
            final ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
            final List<URL> urls = new ArrayList<>();

            for (final String path : classpath) {
                if (StringUtils.isBlank(path)) {
                    throw new IllegalArgumentException("Classpath entry cannot be null or empty");
                }

                final File file = new File(path);
                if (!file.exists()) {
                    throw new IOException("Classpath entry does not exist: '" + path + "'");
                }
                if (!file.canRead()) {
                    throw new IOException("Classpath entry is not readable: '" + path + "'");
                }
                if (!file.isDirectory() && !path.toLowerCase().endsWith(".jar")) {
                    throw new IllegalArgumentException(
                        "Classpath entry must be a directory or JAR file: '" + path + "'");
                }

                try {
                    urls.add(file.toURI().toURL());
                } catch (final MalformedURLException e) {
                    throw new RuntimeException("Invalid classpath entry: '" + path + "'", e);
                }
            }

            final URLClassLoader classLoader = new URLClassLoader(
                urls.toArray(new URL[0]),
                parentClassLoader
            );
            context.setClassLoader(classLoader);
        }

        server.setHandler(context);

        tryStart(port, server);
        return server;
    }

    private static Server buildServer(final int port, final Charset queryEncoding) {
        final QueuedThreadPool threadPool = new QueuedThreadPool(10, 2);

        final Server server = new Server(threadPool);

        final HttpConfiguration httpConfig = new HttpConfiguration();

        if (queryEncoding != null) {
            httpConfig.setRequestCookieCompliance(CookieCompliance.RFC6265);
            httpConfig.setResponseCookieCompliance(CookieCompliance.RFC6265);
            // Note: In Jetty 12, query encoding is handled via UriCompliance
            httpConfig.setUriCompliance(UriCompliance.DEFAULT);
        }

        final HttpConnectionFactory httpConnectionFactory = new HttpConnectionFactory(httpConfig);

        final ServerConnector connector = new ServerConnector(server, 1, -1, httpConnectionFactory);
        connector.setPort(port);
        server.addConnector(connector);

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
                        stopServer(server);

                        throw (BindException) new BindException("Port " + port + " is already in use").initCause(e);
                    }
                    Thread.sleep(200);
                }
                else {
                    // destroy the server to free all associated resources
                    stopServer(server);

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

    private static Path getResourceBasePath(final String resourceBase) throws IOException {
        if (StringUtils.isEmptyOrNull(resourceBase)) {
            throw new IllegalArgumentException("Resource base cannot be null or empty");
        }

        final Path resourceBasePath = Paths.get(resourceBase).toAbsolutePath();
        if (!Files.isDirectory(resourceBasePath)) {
            throw new IOException("Resource path is not a directory: '" + resourceBasePath + "'");
        }
        if (!Files.isReadable(resourceBasePath)) {
            throw new IOException("Resource path is not readable: '" + resourceBasePath + "'");
        }

        return resourceBasePath;
    }

    private static final class ConsoleErrorHandler extends ErrorHandler {
        @Override
        public boolean handle(Request request, Response response, Callback callback) throws Exception {
            Throwable errorException = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            if (errorException != null) {
                System.err.println("\n==== Jetty Servlet Error ====");
                System.err.println("URI:       " + request.getHttpURI());
                System.err.println("Method:    " + request.getMethod());
                System.err.println("Exception: " + errorException.getClass().getName());
                System.err.println("Message:   " + errorException.getMessage());
                errorException.printStackTrace(System.err);
                System.err.println("=============================\n");
            }

            return super.handle(request, response, callback);
        }
    }
}