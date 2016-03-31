/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.worker;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.background.BasicJavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.background.JavaScriptJob;
import com.gargoylesoftware.htmlunit.javascript.configuration.AbstractJavaScriptConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.ClassConfiguration;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * The scope for the execution of {@link Worker}s.
 *
 * @author Marc Guillemot
 */
@JsxClasses({
        @JsxClass(browsers = @WebBrowser(FF)),
        @JsxClass(className = "global", browsers = @WebBrowser(CHROME)),
        @JsxClass(className = "WorkerGlobalScope", browsers = @WebBrowser(IE))
    })
public class DedicatedWorkerGlobalScope extends HtmlUnitScriptable {

    private static final Log LOG = LogFactory.getLog(DedicatedWorkerGlobalScope.class);
    private final Window owningWindow_;
    private final String origin_;
    private final Worker worker_;

    /**
     * For prototype instantiation.
     */
    public DedicatedWorkerGlobalScope() {
        // prototype constructor
        owningWindow_ = null;
        origin_ = null;
        worker_ = null;
    }

    /**
     * Constructor.
     * @param browserVersion the simulated browser version
     * @param worker the started worker
     * @throws Exception in case of problem
     */
    DedicatedWorkerGlobalScope(final Window owningWindow, final Context context, final BrowserVersion browserVersion,
            final Worker worker) throws Exception {
        context.initStandardObjects(this);

        final ClassConfiguration config = AbstractJavaScriptConfiguration.getClassConfiguration(
                DedicatedWorkerGlobalScope.class, browserVersion);
        final HtmlUnitScriptable prototype = JavaScriptEngine.configureClass(config, null, browserVersion);
        setPrototype(prototype);

        owningWindow_ = owningWindow;
        final URL currentURL = owningWindow.getWebWindow().getEnclosedPage().getUrl();
        origin_ = currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort();

        worker_ = worker;
    }

    /**
     * Get the scope itself.
     * @return this
     */
    @JsxGetter
    public Object getSelf() {
        return this;
    }

    /**
     * Posts a message to the {@link Worker} in the page's context.
     * @param message the message
     */
    @JsxFunction
    public void postMessage(final Object message) {
        final MessageEvent event = new MessageEvent();
        event.initMessageEvent(Event.TYPE_MESSAGE, false, false, message, origin_, "", owningWindow_, null);
        event.setParentScope(owningWindow_);
        event.setPrototype(owningWindow_.getPrototype(event.getClass()));

        if (LOG.isDebugEnabled()) {
            LOG.debug("[DedicatedWorker] postMessage: {}" + message);
        }
        final JavaScriptEngine jsEngine = owningWindow_.getWebWindow().getWebClient().getJavaScriptEngine();
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                worker_.getEventListenersContainer().executeCapturingListeners(event, null);
                final Object[] args = new Object[] {event};
                return worker_.getEventListenersContainer().executeBubblingListeners(event, args, args);
            }
        };

        final ContextFactory cf = jsEngine.getContextFactory();

        final JavaScriptJob job = new WorkerJob(cf, action, "postMessage: " + Context.toString(message));

        final HtmlPage page = (HtmlPage) owningWindow_.getDocument().getPage();
        owningWindow_.getWebWindow().getJobManager().addJob(job, page);
    }

    void messagePosted(final Object message) {
        final MessageEvent event = new MessageEvent();
        event.initMessageEvent(Event.TYPE_MESSAGE, false, false, message, origin_, "", owningWindow_, null);
        event.setParentScope(owningWindow_);
        event.setPrototype(owningWindow_.getPrototype(event.getClass()));

        final JavaScriptEngine jsEngine = owningWindow_.getWebWindow().getWebClient().getJavaScriptEngine();
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                return executeEvent(cx, event);
            }
        };

        final ContextFactory cf = jsEngine.getContextFactory();

        final JavaScriptJob job = new WorkerJob(cf, action, "messagePosted: " + Context.toString(message));

        final HtmlPage page = (HtmlPage) owningWindow_.getDocument().getPage();
        owningWindow_.getWebWindow().getJobManager().addJob(job, page);
    }

    private Object executeEvent(final Context cx, final MessageEvent event) {
        final Object handler = get("onmessage", this);

        if (handler != null && handler instanceof Function) {
            final Function handlerFunction = (Function) handler;
            final Object[] args = {event};
            handlerFunction.call(cx, this, this, args);
        }
        return null;
    }

    /**
     * Import external script(s).
     * @param cx the current context
     * @param thisObj this object
     * @param args the script(s) to import
     * @param funObj the JS function called
     * @throws IOException in case of problem loading/executing the scripts
     */
    @JsxFunction
    public static void importScripts(final Context cx, final Scriptable thisObj,
        final Object[] args, final Function funObj) throws IOException {
        final DedicatedWorkerGlobalScope scope = (DedicatedWorkerGlobalScope) thisObj;

        for (final Object arg : args) {
            final String url = Context.toString(arg);
            scope.loadAndExecute(url, cx);
        }
    }

    void loadAndExecute(final String url, final Context context) throws IOException {
        final HtmlPage page = (HtmlPage) owningWindow_.getDocument().getPage();
        final URL fullUrl = page.getFullyQualifiedUrl(url);

        final WebClient webClient = owningWindow_.getWebWindow().getWebClient();

        final WebRequest webRequest = new WebRequest(fullUrl);
        final WebResponse response = webClient.loadWebResponse(webRequest);
        final String scriptCode = response.getContentAsString();
        final JavaScriptEngine javaScriptEngine = webClient.getJavaScriptEngine();

        final DedicatedWorkerGlobalScope thisScope = this;
        final ContextAction action = new ContextAction() {
            @Override
            public Object run(final Context cx) {
                final Script script = javaScriptEngine.compile(page, thisScope, scriptCode,
                        fullUrl.toExternalForm(), 1);
                return webClient.getJavaScriptEngine().execute(page, thisScope, script);
            }
        };

        final ContextFactory cf = javaScriptEngine.getContextFactory();

        if (context != null) {
            action.run(context);
        }
        else {
            final JavaScriptJob job = new WorkerJob(cf, action, "loadAndExecute " + url);

            owningWindow_.getWebWindow().getJobManager().addJob(job, page);
        }
    }
}

class WorkerJob extends BasicJavaScriptJob {
    private final ContextFactory contextFactory_;
    private final ContextAction action_;
    private final String description_;

    WorkerJob(final ContextFactory contextFactory, final ContextAction action, final String description) {
        contextFactory_ = contextFactory;
        action_ = action;
        description_ = description;
    }

    @Override
    public void run() {
        contextFactory_.call(action_);
    }

    @Override
    public String toString() {
        return "WorkerJob(" + description_ + ")";
    }
}
