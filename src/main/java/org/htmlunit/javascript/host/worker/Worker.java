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
package org.htmlunit.javascript.host.worker;

import java.net.URL;

import org.htmlunit.WebClient;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.ContextAction;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.html.ContentSecurityPolicy;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;

/**
 * JavaScript host object for {@code Worker}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Worker">MDN Documentation</a>
 */
@JsxClass
public class Worker extends EventTarget {

    private final DedicatedWorkerGlobalScope workerScope_;

    /**
     * Default constructor.
     */
    public Worker() {
        // prototype constructor
        super();
        workerScope_ = null;
    }

    private Worker(final Context cx, final VarScope scope,
                    final Window owningWindow, final String url,
                    final Scriptable options) throws Exception {
        super();
        setParentScope(getTopLevelScope(scope));
        setPrototype(getPrototype(getClass()));

        final WebClient webClient = getWindow().getWebWindow().getWebClient();
        String name = null;
        if (options != null && options.has("name", options)) {
            name = JavaScriptEngine.toString(options.get("name", options));
        }
        workerScope_ = new DedicatedWorkerGlobalScope(owningWindow, cx, webClient, name, this);

        final HtmlPage page = (HtmlPage) owningWindow.getDocument().getPage();
        final URL fullUrl = page.getFullyQualifiedUrl(url);
        if (!ContentSecurityPolicy.allowsWorker(page, fullUrl)) {
            // like the browsers, the worker is not started and the error event is fired (async)
            final ContextAction<Object> action = c -> {
                dispatchEvent(new Event(this, Event.TYPE_ERROR));
                return null;
            };
            final HtmlUnitContextFactory cf = webClient.getJavaScriptEngine().getContextFactory();
            owningWindow.getWebWindow().getJobManager()
                    .addJob(new WorkerJob(cf, action, "worker blocked by the Content-Security-Policy"), page);
            return;
        }

        workerScope_.loadAndExecute(webClient, url, null, false);
    }

    /**
     * Creates an instance of this object.
     *
     * @param cx the current context
     * @param scope the scope
     * @param args the constructor arguments
     * @param ctorObj the function object
     * @param inNewExpr whether invoked via {@code new}
     * @return the new {@code Worker} instance
     * @throws Exception in case of problem
     */
    @JsxConstructor
    public static Worker jsConstructor(final Context cx, final VarScope scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) throws Exception {
        if (args.length < 1 || args.length > 2) {
            throw JavaScriptEngine.reportRuntimeError(
                    "Worker Error: constructor must have one or two parameters.");
        }

        final String url = JavaScriptEngine.toString(args[0]);
        Scriptable options = null;
        if (args.length > 1 && args[1] instanceof Scriptable scriptable) {
            options = scriptable;
        }
        return new Worker(cx, scope, getWindow(ctorObj), url, options);
    }

    /**
     * Posts the provided message to the worker's execution context.
     *
     * @param message the message to post
     */
    @JsxFunction
    public void postMessage(final Object message) {
        workerScope_.messagePosted(message);
    }

    /**
     * Immediately terminates the worker without giving it the opportunity to
     * finish any currently executing operations.
     */
    @JsxFunction
    public void terminate() {
        // TODO
    }

    /**
     * Sets the {@code onmessage} event handler.
     *
     * @param onmessage the new {@code onmessage} event handler
     */
    @JsxSetter
    public void setOnmessage(final Object onmessage) {
        getEventListenersContainer().setEventHandler(Event.TYPE_MESSAGE, onmessage);
    }

    /**
     * Returns the {@code onmessage} event handler.
     *
     * @return the {@code onmessage} event handler
     */
    @JsxGetter
    public Function getOnmessage() {
        return getEventListenersContainer().getEventHandler(Event.TYPE_MESSAGE);
    }

    /**
     * Sets the {@code onerror} event handler.
     *
     * @param onerror the new {@code onerror} event handler
     */
    @JsxSetter
    public void setOnerror(final Object onerror) {
        getEventListenersContainer().setEventHandler(Event.TYPE_ERROR, onerror);
    }

    /**
     * Returns the {@code onerror} event handler.
     *
     * @return the {@code onerror} event handler
     */
    @JsxGetter
    public Function getOnerror() {
        return getEventListenersContainer().getEventHandler(Event.TYPE_ERROR);
    }
}
