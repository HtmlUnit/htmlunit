/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import org.htmlunit.WebClient;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
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
 * A JavaScript object for {@code Worker}.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
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

    private Worker(final Context cx, final Window owningWindow, final String url,
                       final Scriptable options) throws Exception {
        super();
        setParentScope(owningWindow);
        setPrototype(getPrototype(getClass()));

        final WebClient webClient = getWindow().getWebWindow().getWebClient();
        String name = null;
        if (options != null && options.has("name", options)) {
            name = JavaScriptEngine.toString(options.get("name", options));
        }
        workerScope_ = new DedicatedWorkerGlobalScope(owningWindow, cx, webClient, name, this);

        workerScope_.loadAndExecute(webClient, url, null, false);
    }

    /**
     * For instantiation in JavaScript.
     * @param cx the current context
     * @param scope the scope
     * @param args the URIs
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     * @throws Exception in case of problem
     */
    @JsxConstructor
    public static Worker jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) throws Exception {
        if (args.length < 1 || args.length > 2) {
            throw JavaScriptEngine.reportRuntimeError(
                    "Worker Error: constructor must have one or two parameters.");
        }

        final String url = JavaScriptEngine.toString(args[0]);
        Scriptable options = null;
        if (args.length > 1 && args[1] instanceof Scriptable) {
            options = (Scriptable) args[1];
        }
        return new Worker(cx, getWindow(ctorObj), url, options);
    }

    /**
     * Post the provided message to the WebWorker execution.
     * @param message the message
     */
    @JsxFunction
    public void postMessage(final Object message) {
        workerScope_.messagePosted(message);
    }

    /**
     * Immediately terminates the Worker. This does not offer the worker
     * an opportunity to finish its operations; it is stopped at once.
     */
    @JsxFunction
    public void terminate() {
        // TODO
    }

    /**
     * Sets the value of the onmessage event handler.
     * @param onmessage the new handler
     */
    @JsxSetter
    public void setOnmessage(final Object onmessage) {
        getEventListenersContainer().setEventHandler(Event.TYPE_MESSAGE, onmessage);
    }

    /**
     * Gets the value of the onmessage event handler.
     * @return the handler
     */
    @JsxGetter
    public Function getOnmessage() {
        return getEventListenersContainer().getEventHandler(Event.TYPE_MESSAGE);
    }
}
