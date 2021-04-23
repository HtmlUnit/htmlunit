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
package com.gargoylesoftware.htmlunit.javascript.host.worker;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

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
        workerScope_ = null;
    }

    private Worker(final Context cx, final Window owningWindow, final String url) throws Exception {
        setParentScope(owningWindow);
        setPrototype(getPrototype(getClass()));

        final WebClient webClient = getWindow().getWebWindow().getWebClient();
        workerScope_ = new DedicatedWorkerGlobalScope(owningWindow, cx, webClient, this);

        workerScope_.loadAndExecute(webClient, url, null, false);
    }

    /**
     * For instantiation in JavaScript.
     * @param cx the current context
     * @param args the URIs
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     * @throws Exception in case of problem
     */
    @JsxConstructor
    public static Scriptable jsConstructor(
            final Context cx, final Object[] args, final Function ctorObj,
            final boolean inNewExpr) throws Exception {
        if (args.length < 1 || args.length > 2) {
            throw Context.reportRuntimeError(
                    "Worker Error: constructor must have one or two String parameters.");
        }

        final String url = Context.toString(args[0]);

        return new Worker(cx, getWindow(ctorObj), url);
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
    public Object getOnmessage() {
        return getEventListenersContainer().getEventHandler(Event.TYPE_MESSAGE);
    }
}
