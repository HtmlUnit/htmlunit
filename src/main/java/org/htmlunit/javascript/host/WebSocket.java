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
package org.htmlunit.javascript.host;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.WebWindow;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.IteratorLikeIterable;
import org.htmlunit.corejs.javascript.ScriptRuntime;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.SymbolKey;
import org.htmlunit.corejs.javascript.Undefined;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBufferView;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.AbstractJavaScriptEngine;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.background.BasicJavaScriptJob;
import org.htmlunit.javascript.background.JavaScriptJob;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.javascript.host.event.CloseEvent;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;
import org.htmlunit.javascript.host.event.MessageEvent;
import org.htmlunit.javascript.host.file.Blob;
import org.htmlunit.util.UrlUtils;
import org.htmlunit.websocket.WebSocketAdapter;
import org.htmlunit.websocket.WebSocketListener;

/**
 * JavaScript host object for {@code WebSocket}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Madis Pärn
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WebSocket">MDN Documentation</a>
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

    /**
     * Internal state, never visible to scripts (reported as {@link #CLOSING}): close() was called while
     * the connection was still CONNECTING. Such a connection fails: error event, then close event 1006.
     */
    private static final int ABORTED = 4;

    /** The close frame has 125 bytes of payload, two of them are the status code. */
    private static final int MAX_CLOSE_REASON_BYTES = 123;

    /** The status code for "no status code": the close frame is sent without any. */
    private static final int NO_STATUS_CODE = 1005;

    private URI url_;
    private final AtomicInteger readyState_ = new AtomicInteger(CONNECTING);
    // bytes handed to send() after the connection was closed; see getBufferedAmount()
    private final AtomicLong bufferedAmount_ = new AtomicLong();
    private String binaryType_ = "blob";

    private HtmlPage containingPage_;
    private WebSocketAdapter webSocketImpl_;
    private boolean originSet_;

    /**
     * Creates a new instance.
     */
    public WebSocket() {
        super();
    }

    /**
     * Creates a new instance connected to the given URL.
     *
     * @param url the URL to connect to
     * @param scope the scope
     * @param window the top-level window
     */
    private WebSocket(final String url, final VarScope scope, final Window window) {
        super();
        try {
            final WebWindow webWindow = window.getWebWindow();
            containingPage_ = (HtmlPage) webWindow.getEnclosedPage();

            setParentScope(scope);
            setDomNode(containingPage_.getDocumentElement(), false);

            final WebClient webClient = webWindow.getWebClient();
            originSet_ = true;

            final WebSocketListener webSocketListener = new WebSocketListener() {

                @Override
                public void onWebSocketConnecting() {
                    // nothing to do, CONNECTING is the initial state and is never entered again
                }

                @Override
                public void onWebSocketOpen() {
                    if (!switchToOpen()) {
                        // close() was called while the handshake was in flight: the connection must
                        // not stay open (the first shutdown may have run before there was a session)
                        // and no open event is fired
                        shutdownAdapter();
                        return;
                    }

                    final AbstractJavaScriptEngine<?> engine = containingPage_.getWebClient().getJavaScriptEngine();
                    if (engine != null) {
                        engine.getContextFactory().call(cx -> {
                            final Event openEvent = new Event(Event.TYPE_OPEN);
                            openEvent.setParentScope(scope);
                            openEvent.setPrototype(getPrototype(openEvent.getClass()));
                            openEvent.setSrcElement(WebSocket.this);
                            openEvent.setTarget(WebSocket.this);
                            executeEventLocally(openEvent);

                            return null;
                        });
                    }
                }

                @Override
                public void onWebSocketClose(final int statusCode, final String reason) {
                    connectionEnded(statusCode, reason, false);
                }

                @Override
                public void onWebSocketText(final String message) {
                    if (readyState_.get() != OPEN) {
                        // messages received after close() was called are dropped
                        return;
                    }

                    final AbstractJavaScriptEngine<?> engine = containingPage_.getWebClient().getJavaScriptEngine();
                    if (engine != null) {
                        engine.getContextFactory().call(cx -> {
                            final MessageEvent msgEvent = new MessageEvent(message);
                            msgEvent.setParentScope(scope);
                            msgEvent.setPrototype(getPrototype(msgEvent.getClass()));
                            if (originSet_) {
                                try {
                                    URL originUrl = UrlUtils.toUrlUnsafe(getUrl());
                                    originUrl = UrlUtils.getUrlWithoutPathRefQuery(originUrl);
                                    msgEvent.setOrigin(originUrl.toExternalForm());
                                }
                                catch (final MalformedURLException e) {
                                    // ignore
                                }
                            }
                            msgEvent.setSrcElement(WebSocket.this);
                            msgEvent.setTarget(WebSocket.this);
                            executeEventLocally(msgEvent);

                            return null;
                        });
                    }
                }

                @Override
                public void onWebSocketBinary(final ByteBuffer payload) {
                    if (readyState_.get() != OPEN) {
                        // messages received after close() was called are dropped
                        return;
                    }

                    final AbstractJavaScriptEngine<?> engine = containingPage_.getWebClient().getJavaScriptEngine();
                    if (engine != null) {
                        engine.getContextFactory().call(cx -> {
                            final MessageEvent msgEvent;

                            if ("blob".equals(binaryType_)) {
                                final byte[] bytes = new byte[payload.remaining()];
                                payload.get(bytes);
                                final Blob blob = new Blob(bytes, "application/octet-stream");

                                blob.setParentScope(getParentScope());
                                blob.setPrototype(ScriptableObject.getClassPrototype(getParentScope(),
                                                    blob.getClassName()));

                                msgEvent = new MessageEvent(blob);
                            }
                            else {
                                final NativeArrayBuffer buffer = new NativeArrayBuffer(payload.remaining());
                                payload.get(buffer.getBuffer());

                                buffer.setParentScope(getParentScope());
                                buffer.setPrototype(ScriptableObject.getClassPrototype(getParentScope(),
                                                        buffer.getClassName()));

                                msgEvent = new MessageEvent(buffer);
                            }

                            msgEvent.setParentScope(scope);
                            msgEvent.setPrototype(getPrototype(msgEvent.getClass()));
                            if (originSet_) {
                                try {
                                    URL originUrl = UrlUtils.toUrlUnsafe(getUrl());
                                    originUrl = UrlUtils.getUrlWithoutPathRefQuery(originUrl);
                                    msgEvent.setOrigin(originUrl.toExternalForm());
                                }
                                catch (final MalformedURLException e) {
                                    // ignore
                                }
                            }
                            msgEvent.setSrcElement(WebSocket.this);
                            msgEvent.setTarget(WebSocket.this);
                            executeEventLocally(msgEvent);

                            return null;
                        });
                    }
                }

                @Override
                public void onWebSocketConnectError(final Throwable cause) {
                    if (LOG.isErrorEnabled()) {
                        LOG.error("WS connect error for url '" + url + "':", cause);
                    }
                    onWebSocketError(cause);
                }

                @Override
                public void onWebSocketError(final Throwable cause) {
                    connectionEnded(1006, "", true);
                }
            };

            webSocketImpl_ = webClient.buildWebSocketAdapter(webSocketListener);

            webSocketImpl_.start();
            containingPage_.addAutoCloseable(this);
            url_ = new URI(url);

            webSocketImpl_.connect(url_);
        }
        catch (final Exception e) {
            if (LOG.isErrorEnabled()) {
                LOG.error("WebSocket Error: 'url' parameter '" + url + "' is invalid.", e);
            }
            throw JavaScriptEngine.reportRuntimeError("WebSocket Error: 'url' parameter '" + url + "' is invalid.");
        }
    }

    /**
     * Creates an instance of this object.
     *
     * @param cx the current context
     * @param scope the scope
     * @param args the constructor arguments
     * @param ctorObj the function object
     * @param inNewExpr whether invoked via {@code new}
     * @return the new {@code WebSocket} instance
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final VarScope scope, final Object[] args,
            final Function ctorObj, final boolean inNewExpr) {
        if (!inNewExpr) {
            throw JavaScriptEngine
                    .typeError("Failed to construct 'WebSocket': Please use the 'new' operator, "
                            + "this DOM object constructor cannot be called as a function.");
        }

        // the url is required, the protocols are optional, additional arguments are ignored
        if (args.length < 1) {
            throw JavaScriptEngine
                    .typeError("WebSocket Error: constructor needs at least the url as parameter.");
        }

        final Window win = getWindow(ctorObj);
        String urlString = JavaScriptEngine.toString(args[0]);
        try {
            final Page page = win.getWebWindow().getEnclosedPage();
            if (page instanceof HtmlPage htmlPage) {
                URL url = htmlPage.getFullyQualifiedUrl(urlString);

                if (url.getRef() != null) {
                    throw JavaScriptEngine.asJavaScriptException(
                            win,
                            "WebSocket Error: 'url' parameter '" + urlString + "' contains a fragment identifier.",
                            DOMException.SYNTAX_ERR);
                }

                // Per spec: only ws/wss are valid; convert http/https (relative resolution), reject everything else
                final String scheme = url.getProtocol();
                if ("http".equals(scheme)) {
                    url = UrlUtils.getUrlWithNewProtocol(url, "ws");
                }
                else if ("https".equals(scheme)) {
                    url = UrlUtils.getUrlWithNewProtocol(url, "wss");
                }
                else if (!"ws".equals(scheme) && !"wss".equals(scheme)) {
                    throw JavaScriptEngine.asJavaScriptException(
                            win,
                            "WebSocket Error: 'url' parameter '" + urlString + "' is not a valid url.",
                            DOMException.SYNTAX_ERR);
                }

                urlString = url.toExternalForm();
            }
        }
        catch (final MalformedURLException e) {
            throw JavaScriptEngine.asJavaScriptException(
                    win,
                    "WebSocket Error: 'url' parameter '" + urlString + "' is not a valid url.",
                    DOMException.SYNTAX_ERR);
        }

        // TODO send the protocols with the handshake and use the one selected by the server for protocol
        final List<String> protocols = toProtocols(cx, scope, args.length > 1 ? args[1] : JavaScriptEngine.UNDEFINED);
        if (!areValidProtocols(protocols)) {
            throw JavaScriptEngine.asJavaScriptException(
                    win,
                    "WebSocket Error: the protocols must be unique and may only contain token characters.",
                    DOMException.SYNTAX_ERR);
        }

        return new WebSocket(urlString, getTopLevelScope(scope), win);
    }

    /**
     * Converts the protocols argument, which is a {@code (DOMString or sequence<DOMString>)}: undefined (or no
     * argument) is the empty list, an array is a sequence, anything else - null and objects that are no array
     * included - is converted to a single string.
     *
     * @param protocols the argument
     * @return the protocols
     */
    private static List<String> toProtocols(final Context cx, final VarScope scope, final Object protocols) {
        if (JavaScriptEngine.isUndefined(protocols)) {
            return Collections.emptyList();
        }

        if (protocols instanceof Scriptable protoScriptable) {
            if (hasProperty(protoScriptable, SymbolKey.ITERATOR)) {
                final List<String> result = new ArrayList<>();

                final Object iterator = ScriptRuntime.callIterator(protoScriptable, cx, scope);
                try (IteratorLikeIterable itr = new IteratorLikeIterable(cx, scope, iterator)) {
                    for (final Object elem : itr) {
                        if (elem  == Scriptable.NOT_FOUND) {
                            // a hole in the array
                            result.add(JavaScriptEngine.toString(JavaScriptEngine.UNDEFINED));
                        }
//                        else if (elem instanceof String s) {
//                            result.add(s);
//                        }
                        else {
                            result.add(JavaScriptEngine.toString(elem));
                        }
                    }
                }

                return result;
            }

//            if (JavaScriptEngine.isArrayLike(protoScriptable)) {
//                final List<String> result = new ArrayList<>();
//
//                JavaScriptEngine.iterateArrayLike(cx, protoScriptable, elem -> {
//                    if (elem  == Scriptable.NOT_FOUND) {
//                        // a hole in the array
//                        result.add(JavaScriptEngine.toString(JavaScriptEngine.UNDEFINED));
//                    }
//                    else if (elem instanceof String s) {
//                        result.add(s);
//                    }
//                    else if (elem instanceof ScriptableObject) {
//                        result.add(JavaScriptEngine.toString(elem));
//                    }
//                    else {
//                        throw JavaScriptEngine.typeError("Invalid element in WebSocket ctor protocols argument");
//                    }
//                });
//
//                return result;
//            }
        }

        return Collections.singletonList(JavaScriptEngine.toString(protocols));
    }

    /**
     * Checks the protocols: every one has to be a token, and none may be there more than once
     * (compared case sensitive).
     *
     * @param protocols the protocols
     * @return {@code true} if the protocols are valid
     */
    private static boolean areValidProtocols(final List<String> protocols) {
        final HashSet<String> seen = new HashSet<>();
        for (final String protocol : protocols) {
            if (!isValidProtocol(protocol) || !seen.add(protocol)) {
                return false;
            }
        }
        return true;
    }

    /**
     * A protocol has to match the token production of RFC 7230: at least one character, only letters, digits
     * and {@code !#$%&'*+-.^_`|~}. That excludes empty strings, blanks, separators like the comma and all non
     * ASCII characters.
     *
     * @param protocol the protocol
     * @return {@code true} if the protocol is a token
     */
    private static boolean isValidProtocol(final String protocol) {
        if (protocol.isEmpty()) {
            return false;
        }
        for (int i = 0; i < protocol.length(); i++) {
            final char c = protocol.charAt(i);
            final boolean token = (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9')
                    || "!#$%&'*+-.^_`|~".indexOf(c) >= 0;
            if (!token) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the event handler that fires on close.
     *
     * @return the event handler that fires on close
     */
    @JsxGetter
    public Function getOnclose() {
        return getEventHandler(Event.TYPE_CLOSE);
    }

    /**
     * Sets the event handler that fires on close.
     *
     * @param closeHandler the event handler that fires on close
     */
    @JsxSetter
    public void setOnclose(final Function closeHandler) {
        setEventHandler(Event.TYPE_CLOSE, closeHandler);
    }

    /**
     * Returns the event handler that fires on error.
     *
     * @return the event handler that fires on error
     */
    @JsxGetter
    public Function getOnerror() {
        return getEventHandler(Event.TYPE_ERROR);
    }

    /**
     * Sets the event handler that fires on error.
     *
     * @param errorHandler the event handler that fires on error
     */
    @JsxSetter
    public void setOnerror(final Function errorHandler) {
        setEventHandler(Event.TYPE_ERROR, errorHandler);
    }

    /**
     * Returns the event handler that fires on message.
     *
     * @return the event handler that fires on message
     */
    @JsxGetter
    public Function getOnmessage() {
        return getEventHandler(Event.TYPE_MESSAGE);
    }

    /**
     * Sets the event handler that fires on message.
     *
     * @param messageHandler the event handler that fires on message
     */
    @JsxSetter
    public void setOnmessage(final Function messageHandler) {
        setEventHandler(Event.TYPE_MESSAGE, messageHandler);
    }

    /**
     * Returns the event handler that fires on open.
     *
     * @return the event handler that fires on open
     */
    @JsxGetter
    public Function getOnopen() {
        return getEventHandler(Event.TYPE_OPEN);
    }

    /**
     * Sets the event handler that fires on open.
     *
     * @param openHandler the event handler that fires on open
     */
    @JsxSetter
    public void setOnopen(final Function openHandler) {
        setEventHandler(Event.TYPE_OPEN, openHandler);
    }

    /**
     * Ready state handling.
     *
     * readyState_ is read and written by the JS thread (close(), send(), getReadyState()), by the threads
     * of the websocket adapter and by queued tasks. Every change goes through one of the three methods
     * below. Each is a single atomic operation and tells the caller which state was left, so only the
     * caller that really performed a transition fires the matching event. That gives at most one open
     * event and exactly one close event whatever the interleaving.
     *
     *   CONNECTING                  -> OPEN     switchToOpen()      handshake finished
     *   OPEN                        -> CLOSING  switchToClosing()   close() called
     *   CONNECTING                  -> ABORTED  switchToClosing()   close() called, reported as CLOSING
     *   any but CLOSED              -> CLOSED   switchToClosed()    connection ended, see connectionEnded()
     *
     * The state never moves backwards and CLOSED is final.
     */
    private boolean switchToOpen() {
        return readyState_.compareAndSet(CONNECTING, OPEN);
    }

    /**
     * Return the state that was left, or -1 if the connection already is CLOSING or CLOSED.
     *
     * @return the state that was left, or -1 if the connection already is CLOSING or CLOSED
     */
    private int switchToClosing() {
        int current = readyState_.get();
        while (current == CONNECTING || current == OPEN) {
            final int target = current == CONNECTING ? ABORTED : CLOSING;
            if (readyState_.compareAndSet(current, target)) {
                return current;
            }
            current = readyState_.get();
        }
        return -1;
    }

    /**
     * Return the state that was left, {@link #CLOSED} if the connection was closed before.
     *
     * @return the state that was left, {@link #CLOSED} if the connection was closed before
     */
    private int switchToClosed() {
        return readyState_.getAndSet(CLOSED);
    }

    /**
     * A connection was closed cleanly if the closing handshake was completed, whatever status code it carried.
     * The codes 1006 and 1015 are never sent, they are reported locally for a connection that was lost or failed.
     *
     * @param failed whether the connection failed
     * @param statusCode the status code of the close event
     * @return the value of {@code wasClean}
     */
    private static boolean wasClean(final boolean failed, final int statusCode) {
        return !failed && statusCode != 1006 && statusCode != 1015;
    }

    private static boolean connectionFailed(final int previousState, final boolean reportedAsFailure) {
        return reportedAsFailure || previousState == CONNECTING || previousState == ABORTED;
    }

    /**
     * Starts the closing handshake with the default status code of the adapter.
     *
     * @return {@code false} if this failed, so that no answer of the server has to be waited for
     */
    private boolean closeSessions() {
        return closeSessions(-1, null);
    }

    /**
     * Starts the closing handshake: the sessions are asked to close, which sends the close frame. The outgoing
     * session comes first because it is the one that takes the status code and the reason; the incoming session
     * is the same connection and ignores the second close.
     *
     * @param statusCode the status code of the close frame, {@link #NO_STATUS_CODE} for a close frame without
     *        status code, or -1 for the default of the adapter
     * @param reason the reason of the close frame
     * @return {@code false} if this failed, so that no answer of the server has to be waited for
     */
    private boolean closeSessions(final int statusCode, final String reason) {
        if (webSocketImpl_ == null) {
            return false;
        }

        boolean success = true;
        try {
            if (statusCode < 0) {
                webSocketImpl_.closeOutgoingSession();
            }
            else {
                webSocketImpl_.closeOutgoingSession(statusCode, reason);
            }
        }
        catch (final Exception e) {
            LOG.error("WS close error - outgoingSession_.close() failed", e);
            success = false;
        }

        try {
            webSocketImpl_.closeIncomingSession();
        }
        catch (final Exception e) {
            LOG.error("WS close error - incomingSession_.close() failed", e);
            success = false;
        }
        return success;
    }

    private void releaseClient() {
        if (webSocketImpl_ == null) {
            return;
        }

        try {
            webSocketImpl_.closeClient();
        }
        catch (final Exception e) {
            LOG.error("WS close error - closeClient() failed", e);
        }
    }

    private void shutdownAdapter() {
        closeSessions();
        releaseClient();
    }

    /**
     * The WebIDL conversion to {@code [Clamp] unsigned short}: NaN is 0, the value is clamped to 0..65535
     * and rounded to the nearest integer, ties going to the even one (so 1000.5 is 1000).
     *
     * @param number the number to convert
     * @return the converted value
     */
    private static int clampToUnsignedShort(final double number) {
        if (Double.isNaN(number)) {
            return 0;
        }
        return (int) Math.rint(Math.min(Math.max(number, 0d), 65535d));
    }

    private static boolean isValidCloseCode(final int code) {
        return code == 1000 || (code >= 3000 && code <= 4999);
    }

    /**
     * Returns the USVString of the given text: every unpaired surrogate is replaced by U+FFFD.
     *
     * @param text the text
     * @return the text itself if there is nothing to replace, a converted copy otherwise
     */
    private static String toUsvString(final String text) {
        StringBuilder result = null;
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (Character.isHighSurrogate(c) && i + 1 < text.length()
                    && Character.isLowSurrogate(text.charAt(i + 1))) {
                if (result != null) {
                    result.append(c).append(text.charAt(i + 1));
                }
                i++;
            }
            else if (Character.isSurrogate(c)) {
                if (result == null) {
                    result = new StringBuilder(text.length()).append(text, 0, i);
                }
                result.append('\uFFFD');
            }
            else if (result != null) {
                result.append(c);
            }
        }
        return result == null ? text : result.toString();
    }

    /**
     * Returns the length in bytes of the UTF-8 encoding of the USVString of the given text. An unpaired
     * surrogate is replaced by U+FFFD, which needs three bytes (String.getBytes would use one byte for '?').
     *
     * @param text the text
     * @return the number of bytes
     */
    private static int usvUtf8Length(final String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            final char c = text.charAt(i);
            if (c < 0x80) {
                length += 1;
            }
            else if (c < 0x800) {
                length += 2;
            }
            else if (Character.isHighSurrogate(c) && i + 1 < text.length()
                    && Character.isLowSurrogate(text.charAt(i + 1))) {
                length += 4;
                i++;
            }
            else {
                // BMP character, or an unpaired surrogate that becomes U+FFFD
                length += 3;
            }
        }
        return length;
    }

    /**
     * Returns a copy of the bytes of the given binary data: the content of an ArrayBuffer, the part of its
     * buffer a typed array or DataView views (offset and length, not the whole buffer), or the content of
     * a Blob.
     *
     * @param content the argument of send()
     * @return the copy, or {@code null} if the content is not binary data
     */
    private static byte[] copyOfBinaryData(final Object content) {
        if (content instanceof NativeArrayBuffer buffer) {
            return buffer.getBuffer().clone();
        }
        if (content instanceof NativeArrayBufferView view) {
            final int offset = view.getByteOffset();
            return Arrays.copyOfRange(view.getBuffer().getBuffer(), offset, offset + view.getByteLength());
        }
        if (content instanceof Blob blob) {
            return blob.getBytes().clone();
        }
        return null;
    }

    /**
     * Returns the number of bytes send() has to count for the given data: the byte length of an ArrayBuffer or
     * a view on it, the size of a Blob and, for everything else, the UTF-8 length of the converted string.
     *
     * @param content the argument of send()
     * @return the number of bytes
     */
    private static long byteLengthOf(final Object content) {
        if (content instanceof NativeArrayBuffer buffer) {
            return buffer.getLength();
        }
        if (content instanceof NativeArrayBufferView view) {
            return view.getByteLength();
        }
        if (content instanceof Blob blob) {
            return blob.getSize();
        }
        return usvUtf8Length(JavaScriptEngine.toString(content));
    }

    /**
     * Ends the connection: switches to CLOSED and fires the close event (preceded by an error event if the
     * connection failed). May be called from any thread and any number of times, only the first call fires.
     * A connection that never was established, or that was aborted by close() while connecting, has failed
     * whatever the caller reports: error event, close event with code 1006 and an empty reason.
     *
     * @param statusCode the status code reported by the adapter
     * @param reason the reason reported by the adapter
     * @param reportedAsFailure whether the adapter reported an error
     */
    private void connectionEnded(final int statusCode, final String reason, final boolean reportedAsFailure) {
        final int previous = switchToClosed();
        if (previous == CLOSED) {
            return;
        }

        // The connection is over, release the client. close() does not do this for an open connection,
        // because stopping the client right after the close frame was sent cuts the closing handshake.
        // Not on this thread: it may be one of the adapter's own, and stopping a client from its own
        // threads can stall.
        CompletableFuture.runAsync(this::releaseClient);

        final boolean failed = connectionFailed(previous, reportedAsFailure);

        final AbstractJavaScriptEngine<?> engine = containingPage_.getWebClient().getJavaScriptEngine();
        if (engine == null) {
            return;
        }

        engine.getContextFactory().call(cx -> {
            if (failed) {
                final Event errorEvent = new Event(Event.TYPE_ERROR);
                errorEvent.setParentScope(getParentScope());
                errorEvent.setPrototype(getPrototype(errorEvent.getClass()));
                errorEvent.setSrcElement(WebSocket.this);
                errorEvent.setTarget(WebSocket.this);
                executeEventLocally(errorEvent);
            }

            final CloseEvent closeEvent = new CloseEvent();
            closeEvent.setParentScope(getParentScope());
            closeEvent.setPrototype(getPrototype(closeEvent.getClass()));
            closeEvent.setCode(failed ? 1006 : statusCode);
            closeEvent.setReason(failed || reason == null ? "" : reason);
            closeEvent.setWasClean(wasClean(failed, statusCode));
            closeEvent.setTarget(WebSocket.this);
            executeEventLocally(closeEvent);

            return null;
        });
    }

    /**
     * Runs the task later on the JS thread of the page, like the task queue of a browser does. Used for
     * events that have to follow the running script, e.g. after close() while connecting.
     *
     * @param task the task
     */
    private void queueTask(final Runnable task) {
        final JavaScriptJob job = new BasicJavaScriptJob() {
            @Override
            public void run() {
                task.run();
            }
        };
        containingPage_.getEnclosingWindow().getJobManager().addJob(job, containingPage_);
    }

    /**
     * Returns the current state of the connection.
     * Possible values are {@link #CONNECTING}, {@link #OPEN}, {@link #CLOSING}, or {@link #CLOSED}.
     *
     * @return the current ready state
     */
    @JsxGetter
    public int getReadyState() {
        final int state = readyState_.get();
        return state == ABORTED ? CLOSING : state;
    }

    /**
     * Returns the URL of the WebSocket connection.
     *
     * @return the URL string
     */
    @JsxGetter
    public String getUrl() {
        if (url_ == null) {
            throw JavaScriptEngine.typeError("invalid call");
        }
        return url_.toString();
    }

    /**
     * Returns the sub-protocol in use, or an empty string if no sub-protocol was selected.
     *
     * @return the sub-protocol
     */
    @JsxGetter
    public String getProtocol() {
        return "";
    }

    /**
     * Returns the extensions negotiated with the server, or an empty string if no extensions are in use.
     * The adapter does not report the negotiated extensions, so this is always an empty string.
     *
     * @return the extensions in use
     */
    @JsxGetter
    public String getExtensions() {
        return "";
    }

    /**
     * Returns the number of bytes of data that have been queued but not yet transmitted.
     * Once the connection is closing or closed this value only increases: every send() adds the size of its
     * data, which is discarded. Data sent while the connection is open is handed over to the adapter at once
     * and is not counted, because the adapter does not tell when the data has left.
     *
     * @return the buffered amount in bytes
     */
    @JsxGetter
    public long getBufferedAmount() {
        return bufferedAmount_.get();
    }

    /**
     * Returns the binary data type used when receiving binary messages.
     *
     * @return the binary type ({@code "blob"} or {@code "arraybuffer"})
     */
    @JsxGetter
    public String getBinaryType() {
        return binaryType_;
    }

    /**
     * Sets the binary data type used when receiving binary messages.
     *
     * @param type the new binary type; must be {@code "blob"} or {@code "arraybuffer"}
     */
    @JsxSetter
    public void setBinaryType(final String type) {
        if ("arraybuffer".equals(type) || "blob".equals(type)) {
            binaryType_ = type;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        // called when the page is unloaded: whatever the state is, the client has to be released
        if (switchToClosing() != -1) {
            closeSessions();
        }
        releaseClient();
    }

    /**
     * Closes the WebSocket connection or connection attempt, if any.
     * If the connection is already {@link #CLOSING} or {@link #CLOSED}, this method does nothing.
     * The arguments are validated first, whatever the state is: a code that is neither 1000 nor in the range
     * 3000 to 4999 results in an {@code InvalidAccessError}, a reason longer than 123 bytes (UTF-8) in a
     * {@code SyntaxError}.
     *
     * @param code a numeric value indicating the status code explaining why the connection is being closed
     * @param reason a human-readable string explaining why the connection is closing
     */
    @JsxFunction
    public void close(final Object code, final Object reason) {
        int statusCode = NO_STATUS_CODE;
        if (!Undefined.isUndefined(code)) {
            statusCode = clampToUnsignedShort(Context.toNumber(code));
            if (!isValidCloseCode(statusCode)) {
                throw JavaScriptEngine.asJavaScriptException(
                        getWindow(),
                        "WebSocket Error: the close code must be 1000 or in the range 3000 to 4999.",
                        DOMException.INVALID_ACCESS_ERR);
            }
        }

        String closeReason = "";
        if (!Undefined.isUndefined(reason)) {
            final String usvReason = toUsvString(JavaScriptEngine.toString(reason));
            if (usvUtf8Length(usvReason) > MAX_CLOSE_REASON_BYTES) {
                throw JavaScriptEngine.asJavaScriptException(
                        getWindow(),
                        "WebSocket Error: the close reason must not be longer than 123 bytes.",
                        DOMException.SYNTAX_ERR);
            }
            if (statusCode != NO_STATUS_CODE) {
                // a reason can only be sent together with a status code
                closeReason = usvReason;
            }
        }

        final int previous = switchToClosing();
        if (previous == -1) {
            return;
        }

        if (previous == CONNECTING) {
            // there is no connection to wait for
            shutdownAdapter();
        }
        else if (closeSessions(statusCode, closeReason)) {
            // OPEN: the closing handshake is under way. The adapter reports the end of the connection with the
            // status code of the server, and connectionEnded() releases the client then.
            return;
        }
        else {
            // the close frame could not be sent, so there is no answer to wait for
            releaseClient();
        }

        // the adapter has nothing to report for a cancelled attempt: fail the connection ourselves, after
        // the running script (error + close 1006)
        queueTask(() -> connectionEnded(1006, "", true));
    }

    /**
     * Transmits data to the server over the WebSocket connection.
     * Throws an {@code InvalidStateError} while {@link #CONNECTING}; the data is discarded when
     * {@link #CLOSING} or {@link #CLOSED}. Anything that is not binary
     * is converted to a string and sent as text message. Binary data (an ArrayBuffer, a typed array or
     * DataView, which contribute only the part they view, or a Blob) is sent as binary message; it is copied
     * before this method returns, so later changes of the buffer do not change what is sent.
     *
     * @param content the data to send
     */
    @JsxFunction
    public void send(final Object content) {
        final int state = readyState_.get();
        if (state == CONNECTING) {
            throw JavaScriptEngine.asJavaScriptException(
                    getWindow(),
                    "WebSocket Error: send() is not allowed while the connection is CONNECTING.",
                    DOMException.INVALID_STATE_ERR);
        }
        if (state != OPEN) {
            // CLOSING or CLOSED: the data is discarded, but its size is added to bufferedAmount
            bufferedAmount_.addAndGet(byteLengthOf(content));
            return;
        }

        try {
            final byte[] bytes = copyOfBinaryData(content);
            if (bytes != null) {
                webSocketImpl_.send(ByteBuffer.wrap(bytes));
                return;
            }

            // everything else is sent as text, converted like a USVString: ToString (so 42, null, undefined
            // and objects become "42", "null", "undefined" and "[object Object]") and every unpaired
            // surrogate replaced by U+FFFD (the UTF-8 encoder of Java would write '?' for it)
            webSocketImpl_.send(toUsvString(JavaScriptEngine.toString(content)));
        }
        catch (final IOException e) {
            LOG.error("WS send error", e);
        }
    }
}
