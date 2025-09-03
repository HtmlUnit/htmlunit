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

import java.util.Set;

import org.htmlunit.Page;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.FunctionObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.PostponedAction;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.event.MessageEvent;
import org.htmlunit.util.UrlUtils;

/**
 * A JavaScript object for {@code BroadcastChannel}.
 *
 * @author Ronald Brill
 */
@JsxClass
public class BroadcastChannel extends EventTarget {

    private String name_;

    /**
     * Default constructor.
     */
    public BroadcastChannel() {
        super();
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static BroadcastChannel jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        if (args.length < 1 || JavaScriptEngine.isUndefined(args[0])) {
            throw JavaScriptEngine.typeError("BroadcastChannel constructor requires a channel name argument");
        }

        final BroadcastChannel broadcastChannel = new BroadcastChannel();
        final Window window = getWindow(ctorObj);
        broadcastChannel.setParentScope(window);
        broadcastChannel.setPrototype(((FunctionObject) ctorObj).getClassPrototype());

        broadcastChannel.name_ = JavaScriptEngine.toString(args[0]);

        final Set<BroadcastChannel> broadcastChannels = window.getWebWindow().getWebClient().getBroadcastChannels();
        synchronized (broadcastChannels) {
            broadcastChannels.add(broadcastChannel);
        }

        return broadcastChannel;
    }

    /**
     * Returns the channel name.
     * @return the channel name
     */
    @JsxGetter
    public String getName() {
        return name_;
    }

    /**
     * Returns the value of the {@code onmessage} property.
     * @return the value of the {@code onmessage} property
     */
    @JsxGetter
    public Function getOnmessage() {
        return getEventListenersContainer().getEventHandler(Event.TYPE_MESSAGE);
    }

    /**
     * Sets the value of the {@code onmessage} property.
     * @param onmessage the value of the {@code onmessage} property
     */
    @JsxSetter
    public void setOnmessage(final Object onmessage) {
        getEventListenersContainer().setEventHandler(Event.TYPE_MESSAGE, onmessage);
    }

    /**
     * Returns the value of the {@code onmessageerror} property.
     * @return the value of the {@code onmessageerror} property
     */
    @JsxGetter
    public Function getOnmessageerror() {
        return getEventListenersContainer().getEventHandler("messageerror");
    }

    /**
     * Sets the value of the {@code onmessageerror} property.
     * @param onmessageerror the value of the {@code onmessageerror} property
     */
    @JsxSetter
    public void setOnmessageerror(final Object onmessageerror) {
        getEventListenersContainer().setEventHandler("messageerror", onmessageerror);
    }

    /**
     * Posts a message to all other BroadcastChannel objects with the same name.
     * @param message the message to send
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/BroadcastChannel/postMessage">MDN documentation</a>
     */
    @JsxFunction
    public void postMessage(final Object message) {
        if (name_ == null) {
            return;
        }

        final Window w = getWindow();
        final WebWindow webWindow = w.getWebWindow();
        final Page page = webWindow.getEnclosedPage();
        final java.net.URL currentURL = page.getUrl();
        final String origin = currentURL.getProtocol() + "://" + currentURL.getHost() + ':' + currentURL.getPort();

        final AbstractJavaScriptEngine<?> jsEngine = webWindow.getWebClient().getJavaScriptEngine();

        final Set<BroadcastChannel> broadcastChannels = webWindow.getWebClient().getBroadcastChannels();

        synchronized (broadcastChannels) {
            for (final BroadcastChannel channel : broadcastChannels) {
                if (channel != this && name_.equals(channel.name_)) {
                    final Window channelWindow = channel.getWindow();
                    final WebWindow channelWebWindow = channelWindow.getWebWindow();
                    final Page channelPage = channelWebWindow.getEnclosedPage();

                    if (UrlUtils.isSameOrigin(currentURL, channelPage.getUrl())) {
                        final Scriptable ports = JavaScriptEngine.newArray(w, 0);

                        final MessageEvent event = new MessageEvent();
                        event.initMessageEvent(Event.TYPE_MESSAGE, false, false, message, origin, "", null, ports);
                        event.setParentScope(channelWindow);
                        event.setPrototype(channelWindow.getPrototype(event.getClass()));

                        final PostponedAction action =
                                new PostponedAction(channelPage, "BroadcastChannel.postMessage") {
                                @Override
                                public void execute() {
                                    final HtmlUnitContextFactory cf = jsEngine.getContextFactory();
                                    cf.call(cx -> channel.dispatchEvent(event));
                                }
                            };
                        jsEngine.addPostponedAction(action);
                    }
                }
            }
        }
    }

    /**
     * Closes the BroadcastChannel object, indicating it won't get any new messages,
     * and allowing it to be garbage collected.
     */
    @JsxFunction
    public void close() {
        final Set<BroadcastChannel> broadcastChannels =
                getWindow().getWebWindow().getWebClient().getBroadcastChannels();
        synchronized (broadcastChannels) {
            broadcastChannels.remove(this);
        }
        name_ = null;
    }
}
