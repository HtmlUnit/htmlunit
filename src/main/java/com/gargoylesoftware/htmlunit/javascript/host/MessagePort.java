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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.net.URL;

import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.PostponedAction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;

import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;

/**
 * A JavaScript object for {@code MessagePort}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class MessagePort extends EventTarget {

    private MessagePort port1_;

    /**
     * Default constructor.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public MessagePort() {
    }

    /**
     * Constructors {@code port2} with the specified {@code port1}.
     * @param port1 the port1
     */
    public MessagePort(final MessagePort port1) {
        port1_ = port1;
    }

    /**
     * Returns the value of the window's {@code onmessage} property.
     * @return the value of the window's {@code onmessage} property
     */
    @JsxGetter
    public Object getOnmessage() {
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

    private Object getHandlerForJavaScript(final String eventName) {
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
        if (port1_ != null) {
            final Window w = getWindow();
            final URL currentURL = w.getWebWindow().getEnclosedPage().getUrl();
            final MessageEvent event = new MessageEvent();
            final String origin = currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort();
            event.initMessageEvent(Event.TYPE_MESSAGE, false, false, message, origin, "", w, transfer);
            event.setParentScope(port1_);
            event.setPrototype(getPrototype(event.getClass()));

            final JavaScriptEngine jsEngine
                = (JavaScriptEngine) w.getWebWindow().getWebClient().getJavaScriptEngine();
            final PostponedAction action = new PostponedAction(w.getWebWindow().getEnclosedPage()) {
                @Override
                public void execute() throws Exception {
                    final ContextFactory cf = jsEngine.getContextFactory();
                    cf.call(cx -> port1_.dispatchEvent(event));
                }
            };
            jsEngine.addPostponedAction(action);
        }
    }

}
