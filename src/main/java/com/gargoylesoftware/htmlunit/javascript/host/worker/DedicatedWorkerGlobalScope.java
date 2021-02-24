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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_WORKER_IMPORT_SCRIPTS_ACCEPTS_ALL;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.io.IOException;
import java.net.URL;
import java.util.List;

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
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.gargoylesoftware.htmlunit.javascript.host.WindowOrWorkerGlobalScope;
import com.gargoylesoftware.htmlunit.javascript.host.WindowOrWorkerGlobalScopeMixin;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;
import com.gargoylesoftware.htmlunit.javascript.host.event.MessageEvent;
import com.gargoylesoftware.htmlunit.util.MimeType;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ContextAction;
import net.sourceforge.htmlunit.corejs.javascript.ContextFactory;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Script;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * The scope for the execution of {@link Worker}s.
 *
 * @author Marc Guillemot
 * @author Ronald Brill
 * @author Rural Hunter
 */
@JsxClass({CHROME, EDGE, FF, FF78})
@JsxClass(className = "WorkerGlobalScope", value = IE)
public class DedicatedWorkerGlobalScope extends EventTarget implements WindowOrWorkerGlobalScope {

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
     * @param webClient the WebClient
     * @param worker the started worker
     * @throws Exception in case of problem
     */
    DedicatedWorkerGlobalScope(final Window owningWindow, final Context context, final WebClient webClient,
            final Worker worker) throws Exception {
        context.initSafeStandardObjects(this);

        final BrowserVersion browserVersion = webClient.getBrowserVersion();
        ClassConfiguration config = AbstractJavaScriptConfiguration.getClassConfiguration(
                (Class<? extends HtmlUnitScriptable>) DedicatedWorkerGlobalScope.class.getSuperclass(),
                browserVersion);
        final HtmlUnitScriptable parentPrototype = JavaScriptEngine.configureClass(config, this, browserVersion);

        config = AbstractJavaScriptConfiguration.getClassConfiguration(
                                DedicatedWorkerGlobalScope.class, browserVersion);
        final HtmlUnitScriptable prototype = JavaScriptEngine.configureClass(config, this, browserVersion);
        prototype.setPrototype(parentPrototype);
        setPrototype(prototype);

        // TODO we have to do more configuration here
        JavaScriptEngine.configureRhino(webClient, browserVersion, this);

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
     * Returns the {@code onmessage} event handler.
     * @return the {@code onmessage} event handler
     */
    @JsxGetter
    public Function getOnmessage() {
        return getEventHandler(Event.TYPE_MESSAGE);
    }

    /**
     * Sets the {@code onmessage} event handler.
     * @param onmessage the {@code onmessage} event handler
     */
    @JsxSetter
    public void setOnmessage(final Object onmessage) {
        setEventHandler(Event.TYPE_MESSAGE, onmessage);
    }

    /**
     * Creates a base-64 encoded ASCII string from a string of binary data.
     * @param stringToEncode string to encode
     * @return the encoded string
     */
    @JsxFunction
    @Override
    public String btoa(final String stringToEncode) {
        return WindowOrWorkerGlobalScopeMixin.btoa(stringToEncode);
    }

    /**
     * Decodes a string of data which has been encoded using base-64 encoding.
     * @param encodedData the encoded string
     * @return the decoded value
     */
    @JsxFunction
    @Override
    public String atob(final String encodedData) {
        return WindowOrWorkerGlobalScopeMixin.atob(encodedData);
    }

    /**
     * Posts a message to the {@link Worker} in the page's context.
     * @param message the message
     */
    @JsxFunction
    public void postMessage(final Object message) {
        final MessageEvent event = new MessageEvent();
        event.initMessageEvent(Event.TYPE_MESSAGE, false, false, message, origin_, "",
                                    owningWindow_, Undefined.instance);
        event.setParentScope(owningWindow_);
        event.setPrototype(owningWindow_.getPrototype(event.getClass()));

        if (LOG.isDebugEnabled()) {
            LOG.debug("[DedicatedWorker] postMessage: {}" + message);
        }
        final JavaScriptEngine jsEngine =
                (JavaScriptEngine) owningWindow_.getWebWindow().getWebClient().getJavaScriptEngine();
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                worker_.getEventListenersContainer().executeCapturingListeners(event, null);
                final Object[] args = {event};
                worker_.getEventListenersContainer().executeBubblingListeners(event, args);
                return null;
            }
        };

        final ContextFactory cf = jsEngine.getContextFactory();

        final JavaScriptJob job = new WorkerJob(cf, action, "postMessage: " + Context.toString(message));

        final HtmlPage page = (HtmlPage) owningWindow_.getDocument().getPage();
        owningWindow_.getWebWindow().getJobManager().addJob(job, page);
    }

    void messagePosted(final Object message) {
        final MessageEvent event = new MessageEvent();
        event.initMessageEvent(Event.TYPE_MESSAGE, false, false, message, origin_, "",
                                    owningWindow_, Undefined.instance);
        event.setParentScope(owningWindow_);
        event.setPrototype(owningWindow_.getPrototype(event.getClass()));

        final JavaScriptEngine jsEngine =
                (JavaScriptEngine) owningWindow_.getWebWindow().getWebClient().getJavaScriptEngine();
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                executeEvent(cx, event);
                return null;
            }
        };

        final ContextFactory cf = jsEngine.getContextFactory();

        final JavaScriptJob job = new WorkerJob(cf, action, "messagePosted: " + Context.toString(message));

        final HtmlPage page = (HtmlPage) owningWindow_.getDocument().getPage();
        owningWindow_.getWebWindow().getJobManager().addJob(job, page);
    }

    void executeEvent(final Context cx, final MessageEvent event) {
        final List<Scriptable> handlers = getEventListenersContainer().getListeners(Event.TYPE_MESSAGE, false);
        if (handlers != null) {
            final Object[] args = {event};
            for (final Scriptable scriptable : handlers) {
                if (scriptable instanceof Function) {
                    final Function handlerFunction = (Function) scriptable;
                    handlerFunction.call(cx, this, this, args);
                }
            }
        }

        final Function handlerFunction = getEventHandler(Event.TYPE_MESSAGE);
        if (handlerFunction != null) {
            final Object[] args = {event};
            handlerFunction.call(cx, this, this, args);
        }
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

        final WebClient webClient = scope.owningWindow_.getWebWindow().getWebClient();
        final boolean checkContentType = !webClient.getBrowserVersion()
                .hasFeature(JS_WORKER_IMPORT_SCRIPTS_ACCEPTS_ALL);

        for (final Object arg : args) {
            final String url = Context.toString(arg);
            scope.loadAndExecute(webClient, url, cx, checkContentType);
        }
    }

    void loadAndExecute(final WebClient webClient, final String url,
            final Context context, final boolean checkMimeType) throws IOException {
        final HtmlPage page = (HtmlPage) owningWindow_.getDocument().getPage();
        final URL fullUrl = page.getFullyQualifiedUrl(url);

        final WebRequest webRequest = new WebRequest(fullUrl);
        final WebResponse response = webClient.loadWebResponse(webRequest);
        if (checkMimeType && !MimeType.isJavascriptMimeType(response.getContentType())) {
            throw Context.reportRuntimeError(
                    "NetworkError: importScripts response is not a javascript response");
        }

        final String scriptCode = response.getContentAsString();
        final JavaScriptEngine javaScriptEngine = (JavaScriptEngine) webClient.getJavaScriptEngine();

        final DedicatedWorkerGlobalScope thisScope = this;
        final ContextAction<Object> action = new ContextAction<Object>() {
            @Override
            public Object run(final Context cx) {
                final Script script = javaScriptEngine.compile(page, thisScope, scriptCode,
                        fullUrl.toExternalForm(), 1);

                // script might be null here e.g. if there is a syntax error)
                if (script != null) {
                    return javaScriptEngine.execute(page, thisScope, script);
                }
                return null;
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

    /**
     * Sets a chunk of JavaScript to be invoked at some specified time later.
     * The invocation occurs only if the window is opened after the delay
     * and does not contain an other page than the one that originated the setTimeout.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setTimeout">
     * MDN web docs</a>
     *
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the id of the created timer
     */
    @JsxFunction
    public static Object setTimeout(final Context context, final Scriptable thisObj,
            final Object[] args, final Function function) {
        return WindowOrWorkerGlobalScopeMixin.setTimeout(context,
                ((DedicatedWorkerGlobalScope) thisObj).owningWindow_, args, function);
    }

    /**
     * Sets a chunk of JavaScript to be invoked each time a specified number of milliseconds has elapsed.
     *
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/WindowOrWorkerGlobalScope/setInterval">
     * MDN web docs</a>
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the id of the created interval
     */
    @JsxFunction
    public static Object setInterval(final Context context, final Scriptable thisObj,
            final Object[] args, final Function function) {
        return WindowOrWorkerGlobalScopeMixin.setInterval(context,
                ((DedicatedWorkerGlobalScope) thisObj).owningWindow_, args, function);
    }
}

class WorkerJob extends BasicJavaScriptJob {
    private final ContextFactory contextFactory_;
    private final ContextAction<Object> action_;
    private final String description_;

    WorkerJob(final ContextFactory contextFactory, final ContextAction<Object> action, final String description) {
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
