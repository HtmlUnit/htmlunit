/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
import java.net.URL;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.HandlerList;
import org.mortbay.jetty.handler.ResourceHandler;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.webapp.WebAppClassLoader;
import org.mortbay.jetty.webapp.WebAppContext;

/**
 * Base class for cases that need real web server.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
public abstract class WebServerTestCase extends WebTestCase {

    private Server server_;

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @throws Exception if the test fails
     */
    protected void startWebServer(final String resourceBase) throws Exception {
        if (server_ != null) {
            throw new IllegalStateException("startWebServer() can not be called twice");
        }
        server_ = new Server(PORT);

        final Context context = new Context();
        context.setContextPath("/");
        context.setResourceBase(resourceBase);

        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(resourceBase);

        final HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});
        server_.setHandler(handlers);
        server_.setHandler(resourceHandler);
        server_.start();
    }

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @param classpath additional classpath entries to add (may be null)
     * @throws Exception if the test fails
     */
    protected void startWebServer(final String resourceBase, final String[] classpath) throws Exception {
        if (server_ != null) {
            throw new IllegalStateException("startWebServer() can not be called twice");
        }
        server_ = new Server(PORT);

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
        server_.setHandler(context);
        server_.start();
    }

    /**
     * Starts the web server on the default {@link #PORT}.
     * The given resourceBase is used to be the ROOT directory that serves the default context.
     * <p><b>Don't forget to stop the returned HttpServer after the test</b>
     *
     * @param resourceBase the base of resources for the default context
     * @param classpath additional classpath entries to add (may be null)
     * @param servlets map of {String, Class} pairs: String is the path spec, while class is the class
     * @throws Exception if the test fails
     */
    protected void startWebServer(final String resourceBase, final String[] classpath,
            final Map<String, Class< ? extends Servlet>> servlets) throws Exception {
        if (server_ != null) {
            throw new IllegalStateException("startWebServer() can not be called twice");
        }
        server_ = new Server(PORT);

        final WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase(resourceBase);

        for (final String pathSpec : servlets.keySet()) {
            final Class< ? extends Servlet> servlet = servlets.get(pathSpec);
            context.addServlet(servlet, pathSpec);
        }
        final WebAppClassLoader loader = new WebAppClassLoader(context);
        if (classpath != null) {
            for (final String path : classpath) {
                loader.addClassPath(path);
            }
        }
        context.setClassLoader(loader);
        server_.setHandler(context);
        server_.start();
    }

    /**
     * Starts the web server delivering response from the provided connection.
     * @param mockConnection the sources for responses
     * @throws Exception if a problem occurs
     */
    protected void startWebServer(final MockWebConnection mockConnection) throws Exception {
        if (server_ != null) {
            throw new IllegalStateException("startWebServer() can not be called twice");
        }
        server_ = new Server(PORT);

        final WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase("./");

        context.addServlet(MockWebConnectionServlet.class, "/*");
        MockWebConnectionServlet.MockConnection_ = mockConnection;
        server_.setHandler(context);
        server_.start();
    }

    /**
     * Servlet delivering content from a MockWebConnection.
     * @author Marc Guillemot
     */
    public static class MockWebConnectionServlet extends HttpServlet {
        private static final long serialVersionUID = -3417522859381706421L;
        private static MockWebConnection MockConnection_;

        /**
         * {@inheritDoc}
         */
        @Override
        protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {

            try {
                doService(request, response);
            }
            catch (final ServletException e) {
                throw e;
            }
            catch (final IOException e) {
                throw e;
            }
            catch (final Exception e) {
                throw new ServletException(e);
            }
        }

        private void doService(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
            final URL requestedUrl = new URL(request.getRequestURL().toString());
            final WebRequestSettings settings = new WebRequestSettings(requestedUrl);
            settings.setHttpMethod(HttpMethod.valueOf(request.getMethod()));
            for (final Enumeration en = request.getHeaderNames(); en.hasMoreElements();) {
                final String headerName = (String) en.nextElement();
                final String headerValue = request.getHeader(headerName);
                settings.setAdditionalHeader(headerName, headerValue);
            }
            final WebResponse resp = MockConnection_.getResponse(settings);

            // write WebResponse to HttpServletResponse
            for (final NameValuePair responseHeader : resp.getResponseHeaders()) {
                response.addHeader(responseHeader.getName(), responseHeader.getValue());
            }

            final String newContent = StringUtils.replace(resp.getContentAsString(), "alert(",
                "(function(t){var x = window.__huCatchedAlerts; x = x ? x : []; "
                + "window.__huCatchedAlerts = x; x.push(String(t))})(");

            response.getWriter().print(newContent);
            response.flushBuffer();
        }
    }

    /**
     * Performs post-test deconstruction.
     * @throws Exception if an error occurs
     */
    @After
    public void tearDown() throws Exception {
        if (server_ != null) {
            server_.stop();
        }
        server_ = null;
    }
}
