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
package com.gargoylesoftware.htmlunit;

import java.util.Map;

import javax.servlet.Servlet;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.After;

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

        final WebAppContext context = new WebAppContext();
        context.setContextPath("/");
        context.setResourceBase(resourceBase);

        final ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setResourceBase(resourceBase);
        resourceHandler.getMimeTypes().addMimeMapping("js", "application/javascript");

        final HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});
        server_.setHandler(handlers);
        server_.setHandler(resourceHandler);
        server_.start();
        server_.join();
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
        server_.join();
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
        server_.join();
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
