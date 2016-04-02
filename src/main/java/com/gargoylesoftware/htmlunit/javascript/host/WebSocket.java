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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.Future;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.event.CloseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code WebSocket}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Madis Pärn
 *
 * @see <a href="https://developer.mozilla.org/en/WebSockets/WebSockets_reference/WebSocket">Mozilla documentation</a>
 */
@JsxClass
public class WebSocket extends EventTarget implements AutoCloseable {

    private static final Log LOG = LogFactory.getLog(WebSocket.class);

    /** The connection has not yet been established. */
    @JsxConstant
    public static final int CONNECTING = 0;
    /** The WebSocket connection is established and communication is possible. */
    @JsxConstant
    public static final int OPEN = 1;
    /** The connection is going through the closing handshake. */
    @JsxConstant
    public static final int CLOSING = 2;
    /** The connection has been closed or could not be opened. */
    @JsxConstant
    public static final int CLOSED = 3;

    private Function closeHandler_;
    private Function errorHandler_;
    private Function messageHandler_;
    private Function openHandler_;
    private URI url_;
    private int readyState_ = CONNECTING;
    private String binaryType_ = "blob";

    private HtmlPage containingPage_;
    private WebSocketClient client_;
    private volatile Session incomingSession_;
    private Session outgoingSession_;

    /**
     * Creates a new instance.
     */
    public WebSocket() {
    }

    /**
     * Creates a new instance.
     * @param url the URL to which to connect
     * @param protocols if present, is either a string or an array of strings
     * @param window the top level window
     */
    private WebSocket(final String url, final Object protocols, final Window window) {
        try {
            containingPage_ = (HtmlPage) window.getWebWindow().getEnclosedPage();
            setParentScope(window);
            setDomNode(containingPage_.getBody(), false);

            if (containingPage_.getWebClient().getOptions().isUseInsecureSSL()) {
                client_ = new WebSocketClient(new SslContextFactory(true));
            }
            else {
                client_ = new WebSocketClient();
            }
            client_.setCookieStore(new WebSocketCookieStore(window.getWebWindow().getWebClient()));
            client_.start();
            containingPage_.addAutoCloseable(this);
            url_ = new URI(url);

            // there seem to be a bug in jetty, this constructor sets the cookies twice
            // incomingSession_ = client_.connect(new WebSocketImpl(), new URI(url)).get();
            final Future<Session> connectFuture = client_.connect(new WebSocketImpl(),
                                                                    url_, new ClientUpgradeRequest());
            client_.getExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        readyState_ = CONNECTING;
                        incomingSession_ = connectFuture.get();
                    }
                    catch (final Exception e) {
                        LOG.error("WS connect error", e);
                    }
                }
            });
        }
        catch (final Exception e) {
            LOG.error("WS constructor error", e);
        }
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(
            final Context cx, final Object[] args, final Function ctorObj,
            final boolean inNewExpr) {
        if (args.length < 1 || args.length > 2) {
            throw Context.reportRuntimeError(
                    "WebSocket Error: constructor must have one or two String parameters.");
        }
        if (args[0] == Context.getUndefinedValue()) {
            throw Context.reportRuntimeError("WebSocket Error: 'url' parameter is undefined.");
        }
        if (!(args[0] instanceof String)) {
            throw Context.reportRuntimeError("WebSocket Error: 'url' parameter must be a String.");
        }
        return new WebSocket((String) args[0], null, getWindow(ctorObj));
    }

    /**
     * Returns the event handler that fires on close.
     * @return the event handler that fires on close
     */
    @JsxGetter
    public Function getOnclose() {
        return closeHandler_;
    }

    /**
     * Sets the event handler that fires on close.
     * @param closeHandler the event handler that fires on close
     */
    @JsxSetter
    public void setOnclose(final Function closeHandler) {
        closeHandler_ = closeHandler;
    }

    /**
     * Returns the event handler that fires on error.
     * @return the event handler that fires on error
     */
    @JsxGetter
    public Function getOnerror() {
        return errorHandler_;
    }

    /**
     * Sets the event handler that fires on error.
     * @param errorHandler the event handler that fires on error
     */
    @JsxSetter
    public void setOnerror(final Function errorHandler) {
        errorHandler_ = errorHandler;
    }

    /**
     * Returns the event handler that fires on message.
     * @return the event handler that fires on message
     */
    @JsxGetter
    public Function getOnmessage() {
        return messageHandler_;
    }

    /**
     * Sets the event handler that fires on message.
     * @param messageHandler the event handler that fires on message
     */
    @JsxSetter
    public void setOnmessage(final Function messageHandler) {
        messageHandler_ = messageHandler;
    }

    /**
     * Returns the event handler that fires on open.
     * @return the event handler that fires on open
     */
    @JsxGetter
    public Function getOnopen() {
        return openHandler_;
    }

    /**
     * Sets the event handler that fires on open.
     * @param openHandler the event handler that fires on open
     */
    @JsxSetter
    public void setOnopen(final Function openHandler) {
        openHandler_ = openHandler;
    }

    /**
     * Returns The current state of the connection. The possible values are: {@link #CONNECTING}, {@link #OPEN},
     * {@link #CLOSING} or {@link #CLOSED}.
     * @return the current state of the connection
     */
    @JsxGetter
    public int getReadyState() {
        return readyState_;
    }

    /**
     * @return the url
     */
    @JsxGetter
    public String getUrl() {
        return url_.toString();
    }

    /**
     * @return the sub protocol used
     */
    @JsxGetter
    public String getProtocol() {
        return "";
    }

    /**
     * @return the sub protocol used
     */
    @JsxGetter
    public long getBufferedAmount() {
        return 0L;
    }

    /**
     * @return the used binary type
     */
    @JsxGetter
    public String getBinaryType() {
        return binaryType_;
    }

    /**
     * Sets the used binary type.
     * @param type the type
     */
    @JsxSetter
    public void setBinaryType(final String type) {
        if ("arraybuffer".equals(type)
            || "blob".equals(type)) {
            binaryType_ = type;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws Exception {
        close(null, null);
    }

    /**
     * Closes the WebSocket connection or connection attempt, if any.
     * If the connection is already {@link #CLOSED}, this method does nothing.
     * @param code A numeric value indicating the status code explaining why the connection is being closed
     * @param reason A human-readable string explaining why the connection is closing
     */
    @JsxFunction
    public void close(final Object code, final Object reason) {
        if (readyState_ != CLOSED) {
            if (incomingSession_ != null) {
                incomingSession_.close();
            }
            if (outgoingSession_ != null) {
                outgoingSession_.close();
            }
        }

        try {
            if (client_ != null) {
                client_.stop();
                client_ = null;
            }
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Transmits data to the server over the WebSocket connection.
     * @param content the body of the message being sent with the request
     */
    @JsxFunction
    public void send(final Object content) {
        try {
            if (content instanceof String) {
                outgoingSession_.getRemote().sendString((String) content);
            }
            else if (content instanceof ArrayBuffer) {
                final byte[] bytes = ((ArrayBuffer) content).getBytes();
                final ByteBuffer buffer = ByteBuffer.wrap(bytes);
                outgoingSession_.getRemote().sendBytes(buffer);
            }
            else {
                throw new IllegalStateException(
                        "Not Yet Implemented: WebSocket.send() was used to send non-string value");
            }
        }
        catch (final IOException e) {
            LOG.error("WS send error", e);
        }
    }

    private void fire(final Event evt) {
        evt.setTarget(this);
        evt.setParentScope(getParentScope());
        evt.setPrototype(getPrototype(evt.getClass()));

        final JavaScriptEngine engine = containingPage_.getWebClient().getJavaScriptEngine();
        engine.getContextFactory().call(new ContextAction() {
            @Override
            public ScriptResult run(final Context cx) {
                return executeEventLocally(evt);
            }
        });
    }

    private void callFunction(final Function function, final Object[] args) {
        if (function == null) {
            return;
        }
        final Scriptable scope = function.getParentScope();
        final JavaScriptEngine engine = containingPage_.getWebClient().getJavaScriptEngine();
        engine.callFunction(containingPage_, function, scope, this, args);
    }

    private class WebSocketImpl extends WebSocketAdapter {

        @Override
        public void onWebSocketConnect(final Session session) {
            super.onWebSocketConnect(session);
            readyState_ = OPEN;
            outgoingSession_ = session;

            final Event openEvent = new Event();
            openEvent.setType(Event.TYPE_OPEN);
            fire(openEvent);
            callFunction(openHandler_, ArrayUtils.EMPTY_OBJECT_ARRAY);
        }

        @Override
        public void onWebSocketClose(final int statusCode, final String reason) {
            super.onWebSocketClose(statusCode, reason);
            readyState_ = CLOSED;
            outgoingSession_ = null;

            final CloseEvent closeEvent = new CloseEvent();
            closeEvent.setCode(statusCode);
            closeEvent.setReason(reason);
            closeEvent.setWasClean(true);
            fire(closeEvent);
            callFunction(closeHandler_, new Object[] {closeEvent});
        }

        @Override
        public void onWebSocketText(final String message) {
            super.onWebSocketText(message);

            final MessageEvent msgEvent = new MessageEvent(message);
            msgEvent.setOrigin(getUrl());
            fire(msgEvent);
            callFunction(messageHandler_, new Object[] {msgEvent});
        }

        @Override
        public void onWebSocketBinary(final byte[] data, final int offset, final int length) {
            super.onWebSocketBinary(data, offset, length);

            final ArrayBuffer buffer = new ArrayBuffer();
            buffer.setParentScope(getParentScope());
            buffer.setPrototype(getPrototype(buffer.getClass()));

            buffer.constructor(length);
            buffer.setBytes(0, Arrays.copyOfRange(data, offset, length));

            final MessageEvent msgEvent = new MessageEvent(buffer);
            msgEvent.setOrigin(getUrl());
            fire(msgEvent);
            callFunction(messageHandler_, new Object[] {msgEvent});
        }
    }
}
