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
package org.htmlunit.javascript.host;

import java.net.URL;

import org.htmlunit.Page;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.event.MessageEvent;

/**
 * A JavaScript object for {@code MessagePort}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class MessagePort extends EventTarget {

    private MessagePort port_;

    /**
     * Default constructor.
     */
    public MessagePort() {
        super();
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Ctor with the specified {@code port}.
     * @param port the port
     */
    public MessagePort(final MessagePort port) {
        super();
        port_ = port;
    }

    /**
     * Returns the value of the window's {@code onmessage} property.
     * @return the value of the window's {@code onmessage} property
     */
    @JsxGetter
    public Function getOnmessage() {
        return getHandlerForJavaScript(Event.TYPE_MESSAGE);
    }

    /**
     * Sets the value of the window's {@code onmessage} property.
     * @param onmessage the value of the window's {@code onmessage} property
     */
    @JsxSetter
    public void setOnmessage(final Object onmessage) {
        setHandlerForJavaScript(Event.TYPE_MESSAGE, onmessage);
    }

    private Function getHandlerForJavaScript(final String eventName) {
        return getEventListenersContainer().getEventHandler(eventName);
    }

    private void setHandlerForJavaScript(final String eventName, final Object handler) {
        getEventListenersContainer().setEventHandler(eventName, handler);
    }

    /**
     * Posts a message.
     * @param message the object passed to the window
     * @param transfer an optional sequence of Transferable objects
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/window.postMessage">MDN documentation</a>
     */
    @JsxFunction
    public void postMessage(final String message, final Object transfer) {
        if (port_ != null) {
            final Window w = getWindow();
            final WebWindow webWindow = w.getWebWindow();
            final Page page = webWindow.getEnclosedPage();

            final MessageEvent event = new MessageEvent();
            final URL currentURL = page.getUrl();
            final String origin = currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort();
            event.initMessageEvent(Event.TYPE_MESSAGE, false, false, message, origin, "", w, transfer);
            event.setParentScope(port_);
            event.setPrototype(getPrototype(event.getClass()));

            final AbstractJavaScriptEngine<?> jsEngine = webWindow.getWebClient().getJavaScriptEngine();
            final PostponedAction action = new PostponedAction(page, "MessagePort.postMessage") {
                @Override
                public void execute() {
                    final HtmlUnitContextFactory cf = jsEngine.getContextFactory();
                    cf.call(cx -> port_.dispatchEvent(event));
                }
            };
            jsEngine.addPostponedAction(action);
        }
    }

    /**
     * Starts the sending of messages queued on the port
     * (only needed when using EventTarget.addEventListener; it is implied when using onmessage).
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/MessagePort/start">MDN documentation</a>
     */
    @JsxFunction
    public void start() {
        // dummy for the moment
    }

    /**
     * Disconnects the port, so it is no longer active.
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/MessagePort/close">MDN documentation</a>
     */
    @JsxFunction
    public void close() {
        // dummy for the moment
    }
}
