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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.WEBSOCKET_ORIGIN_SET;

import java.io.IOException;
import java.net.URI;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.CloseEvent;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import com.gargoylesoftware.htmlunit.websocket.JettyWebSocketAdapter;
import com.gargoylesoftware.htmlunit.websocket.WebSocketAdapter;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;

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
    private WebSocketAdapter webSocketImpl_;
    private boolean originSet_;

    /**
     * Creates a new instance.
     */
    public WebSocket() {
    }

    /**
     * Creates a new instance.
     * @param url the URL to which to connect
     * @param window the top level window
     */
    private WebSocket(final String url, final Window window) {
        try {
            final WebWindow webWindow = window.getWebWindow();
            containingPage_ = (HtmlPage) webWindow.getEnclosedPage();
            setParentScope(window);
            setDomNode(containingPage_.getDocumentElement(), false);

            final WebClient webClient = webWindow.getWebClient();
            originSet_ = webClient.getBrowserVersion().hasFeature(WEBSOCKET_ORIGIN_SET);

            webSocketImpl_ = new JettyWebSocketAdapter(webClient) {

                @Override
                public void onWebSocketConnecting() {
                    setReadyState(CONNECTING);
                }

                @Override
                public void onWebSocketConnect() {
                    setReadyState(OPEN);

                    final Event openEvent = new Event();
                    openEvent.setType(Event.TYPE_OPEN);
                    openEvent.setSrcElement(WebSocket.this);
                    fire(openEvent);
                    callFunction(openHandler_, new Object[] {openEvent});
                }

                @Override
                public void onWebSocketClose(final int statusCode, final String reason) {
                    setReadyState(CLOSED);

                    final CloseEvent closeEvent = new CloseEvent();
                    closeEvent.setCode(statusCode);
                    closeEvent.setReason(reason);
                    closeEvent.setWasClean(true);
                    fire(closeEvent);
                    callFunction(closeHandler_, new Object[] {closeEvent});
                }

                @Override
                public void onWebSocketText(final String message) {
                    final MessageEvent msgEvent = new MessageEvent(message);
                    if (originSet_) {
                        msgEvent.setOrigin(getUrl());
                    }
                    msgEvent.setSrcElement(WebSocket.this);
                    fire(msgEvent);
                    callFunction(messageHandler_, new Object[] {msgEvent});
                }

                @Override
                public void onWebSocketBinary(final byte[] data, final int offset, final int length) {
                    final NativeArrayBuffer buffer = new NativeArrayBuffer(length);
                    System.arraycopy(data, offset, buffer.getBuffer(), 0, length);
                    buffer.setParentScope(getParentScope());
                    buffer.setPrototype(ScriptableObject.getClassPrototype(getWindow(), buffer.getClassName()));

                    final MessageEvent msgEvent = new MessageEvent(buffer);
                    if (originSet_) {
                        msgEvent.setOrigin(getUrl());
                    }
                    msgEvent.setSrcElement(WebSocket.this);
                    fire(msgEvent);
                    callFunction(messageHandler_, new Object[] {msgEvent});
                }

                @Override
                public void onWebSocketConnectError(final Throwable cause) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("WS connect error for url '" + url + "':", cause);
                    }
                }

                @Override
                public void onWebSocketError(final Throwable cause) {
                    setReadyState(CLOSED);

                    final Event errorEvent = new Event();
                    errorEvent.setType(Event.TYPE_ERROR);
                    errorEvent.setSrcElement(WebSocket.this);
                    fire(errorEvent);
                    callFunction(errorHandler_, new Object[] {errorEvent});

                    final CloseEvent closeEvent = new CloseEvent();
                    closeEvent.setCode(1006);
                    closeEvent.setReason(cause.getMessage());
                    closeEvent.setWasClean(false);
                    fire(closeEvent);
                    callFunction(closeHandler_, new Object[] {closeEvent});
                }
            };

            webSocketImpl_.start();
            containingPage_.addAutoCloseable(this);
            url_ = new URI(url);

            webClient.getInternals().created(this);

            webSocketImpl_.connect(url_);
        }
        catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("WebSocket Error: 'url' parameter '" + url + "' is invalid.", e);
            }
            throw Context.reportRuntimeError("WebSocket Error: 'url' parameter '" + url + "' is invalid.");
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
        if (Undefined.isUndefined(args[0])) {
            throw Context.reportRuntimeError("WebSocket Error: 'url' parameter is undefined.");
        }
        if (!(args[0] instanceof String)) {
            throw Context.reportRuntimeError("WebSocket Error: 'url' parameter must be a String.");
        }
        final String url = (String) args[0];
        if (StringUtils.isBlank(url)) {
            throw Context.reportRuntimeError("WebSocket Error: 'url' parameter must be not empty.");
        }
        return new WebSocket(url, getWindow(ctorObj));
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

    void setReadyState(final int readyState) {
        readyState_ = readyState;
    }

    /**
     * @return the url
     */
    @JsxGetter
    public String getUrl() {
        if (url_ == null) {
            throw ScriptRuntime.typeError("invalid call");
        }
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
    public void close() throws IOException {
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
            try {
                webSocketImpl_.closeIncommingSession();
            }
            catch (final Throwable e) {
                LOG.error("WS close error - incomingSession_.close() failed", e);
            }

            try {
                webSocketImpl_.closeOutgoingSession();
            }
            catch (final Throwable e) {
                LOG.error("WS close error - outgoingSession_.close() failed", e);
            }
        }

        try {
            webSocketImpl_.closeClient();
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
            webSocketImpl_.send(content);
        }
        catch (final IOException e) {
            LOG.error("WS send error", e);
        }
    }

    void fire(final Event evt) {
        evt.setTarget(this);
        evt.setParentScope(getParentScope());
        evt.setPrototype(getPrototype(evt.getClass()));

        final JavaScriptEngine engine = (JavaScriptEngine) containingPage_.getWebClient().getJavaScriptEngine();
        engine.getContextFactory().call(cx -> {
            executeEventLocally(evt);
            return null;
        });
    }

    void callFunction(final Function function, final Object[] args) {
        if (function == null) {
            return;
        }
        final Scriptable scope = function.getParentScope();
        final JavaScriptEngine engine
            = (JavaScriptEngine) containingPage_.getWebClient().getJavaScriptEngine();
        engine.callFunction(containingPage_, function, scope, this, args);
    }
}
