/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_FALSE_RESULT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeObject;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * Container for event listener.
 *
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class EventListenersContainer implements Serializable {

    private static final Log LOG = LogFactory.getLog(EventListenersContainer.class);

    static class TypeContainer implements Serializable {
        private List<Scriptable> capturingListeners_;
        private List<Scriptable> bubblingListeners_;
        private Object handler_;

        TypeContainer() {
            capturingListeners_ = Collections.unmodifiableList(new ArrayList<Scriptable>());
            bubblingListeners_ = Collections.unmodifiableList(new ArrayList<Scriptable>());
        }

        private TypeContainer(final List<Scriptable> capturingListeners,
                    final List<Scriptable> bubblingListeners, final Object handler) {
            capturingListeners_ = Collections.unmodifiableList(new ArrayList<>(capturingListeners));
            bubblingListeners_ = Collections.unmodifiableList(new ArrayList<>(bubblingListeners));
            handler_ = handler;
        }

        private List<Scriptable> getListeners(final boolean useCapture) {
            if (useCapture) {
                return capturingListeners_;
            }
            return bubblingListeners_;
        }

        private synchronized boolean addListener(final Scriptable listener, final boolean useCapture) {
            final List<Scriptable> listeners = getListeners(useCapture);

            if (listeners.contains(listener)) {
                return false;
            }

            List<Scriptable> newListeners = new ArrayList<>(listeners.size() + 1);
            newListeners.addAll(listeners);
            newListeners.add(listener);
            newListeners = Collections.unmodifiableList(newListeners);

            if (useCapture) {
                capturingListeners_ = newListeners;
            }
            else {
                bubblingListeners_ = newListeners;
            }

            return true;
        }

        private synchronized void removeListener(final Scriptable listener, final boolean useCapture) {
            final List<Scriptable> listeners = getListeners(useCapture);

            final int idx = listeners.indexOf(listener);
            if (idx < 0) {
                return;
            }

            List<Scriptable> newListeners = new ArrayList<>(listeners);
            newListeners.remove(idx);
            newListeners = Collections.unmodifiableList(newListeners);

            if (useCapture) {
                capturingListeners_ = newListeners;
            }
            else {
                bubblingListeners_ = newListeners;
            }
        }

        @Override
        protected TypeContainer clone() {
            return new TypeContainer(capturingListeners_, bubblingListeners_, handler_);
        }
    }

    private final Map<String, TypeContainer> typeContainers_ = new HashMap<>();
    private final EventTarget jsNode_;

    /**
     * The constructor.
     *
     * @param jsNode the node.
     */
    public EventListenersContainer(final EventTarget jsNode) {
        jsNode_ = jsNode;
    }

    /**
     * Adds an event listener.
     *
     * @param type the event type to listen for (like "load")
     * @param listener the event listener
     * @param useCapture If {@code true}, indicates that the user wishes to initiate capture (not yet implemented)
     * @return {@code true} if the listener has been added
     */
    public boolean addEventListener(final String type, final Scriptable listener, final boolean useCapture) {
        if (null == listener) {
            return true;
        }

        final TypeContainer container = getTypeContainer(type);
        final boolean added = container.addListener(listener, useCapture);
        if (!added) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(type + " listener already registered, skipping it (" + listener + ")");
            }
            return false;
        }
        return true;
    }

    private TypeContainer getTypeContainer(final String type) {
        final String typeLC = type.toLowerCase(Locale.ROOT);
        TypeContainer container = typeContainers_.get(typeLC);
        if (container == null) {
            container = new TypeContainer();
            typeContainers_.put(typeLC, container);
        }
        return container;
    }

    /**
     * Returns the relevant listeners.
     *
     * @param eventType the event type
     * @param useCapture whether to use capture of not
     * @return the listeners list
     */
    public List<Scriptable> getListeners(final String eventType, final boolean useCapture) {
        final TypeContainer container = typeContainers_.get(eventType.toLowerCase(Locale.ROOT));
        if (container != null) {
            return container.getListeners(useCapture);
        }
        return null;
    }

    /**
     * Removes event listener.
     *
     * @param eventType the type
     * @param listener the listener
     * @param useCapture to use capture or not
     */
    void removeEventListener(final String eventType, final Scriptable listener, final boolean useCapture) {
        if (listener == null) {
            return;
        }

        final TypeContainer container = typeContainers_.get(eventType.toLowerCase(Locale.ROOT));
        if (container != null) {
            container.removeListener(listener, useCapture);
        }
    }

    /**
     * Sets the handler property (with a handler or something else).
     * @param eventType the event type (like "click")
     * @param value the new property
     */
    public void setEventHandler(final String eventType, final Object value) {
        Object handler = value;
        if (handler == Undefined.instance) {
            handler = null;
        }

        final TypeContainer container = getTypeContainer(eventType);
        container.handler_ = handler;
    }

    private ScriptResult executeEventListeners(final boolean useCapture, final Event event, final Object[] args) {
        final DomNode node = jsNode_.getDomNodeOrNull();
        // some event don't apply on all kind of nodes, for instance "blur"
        if (node != null && !node.handles(event)) {
            return null;
        }

        ScriptResult allResult = null;
        final List<Scriptable> listeners = getListeners(event.getType(), useCapture);
        if (listeners != null && !listeners.isEmpty()) {
            event.setCurrentTarget(jsNode_);

            final Window window;
            if (jsNode_ instanceof Window) {
                window = (Window) jsNode_;
            }
            else {
                window = (Window) jsNode_.getParentScope();
            }
            final HtmlPage page = (HtmlPage) window.getDomNodeOrDie();

            // no need for a copy, listeners are copy on write
            for (final Scriptable listener : listeners) {
                Function function = null;
                Scriptable thisObject = null;
                if (listener instanceof Function) {
                    function = (Function) listener;
                    thisObject = jsNode_;
                }
                else if (listener instanceof NativeObject) {
                    final Object handleEvent = ScriptableObject.getProperty(listener, "handleEvent");
                    if (handleEvent instanceof Function) {
                        function = (Function) handleEvent;
                        thisObject = listener;
                    }
                }
                if (function != null) {
                    final ScriptResult result =
                            page.executeJavaScriptFunction(function, thisObject, args, node);
                    if (event.isPropagationStopped()) {
                        allResult = result;
                    }
                    if (jsNode_.getBrowserVersion().hasFeature(EVENT_FALSE_RESULT)) {
                        if (ScriptResult.isFalse(result)) {
                            allResult = result;
                        }
                        else {
                            final Object eventReturnValue = event.getReturnValue();
                            if (eventReturnValue instanceof Boolean && !((Boolean) eventReturnValue).booleanValue()) {
                                allResult = new ScriptResult(Boolean.FALSE, page);
                            }
                        }
                    }
                }
                if (event.isImmediatePropagationStopped()) {
                    return allResult;
                }
            }
        }
        return allResult;
    }

    private ScriptResult executeEventHandler(final Event event, final Object[] propHandlerArgs) {
        final DomNode node = jsNode_.getDomNodeOrNull();
        // some event don't apply on all kind of nodes, for instance "blur"
        if (node != null && !node.handles(event)) {
            return null;
        }
        final Function handler = getEventHandler(event.getType());
        if (handler != null) {
            event.setCurrentTarget(jsNode_);
            final HtmlPage page = (HtmlPage) (node != null
                    ? node.getPage()
                    : jsNode_.getWindow().getWebWindow().getEnclosedPage());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing " + event.getType() + " handler for " + node);
            }
            return page.executeJavaScriptFunction(handler, jsNode_,
                    propHandlerArgs, page);
        }
        return null;
    }

    /**
     * Executes bubbling listeners.
     * @param event the event
     * @param args arguments
     * @param propHandlerArgs handler arguments
     * @return the result
     */
    public ScriptResult executeBubblingListeners(final Event event, final Object[] args,
            final Object[] propHandlerArgs) {
        ScriptResult result = null;

        // the handler declared as property if any (not on body, as handler declared on body goes to the window)
        final DomNode domNode = jsNode_.getDomNodeOrNull();
        if (!(domNode instanceof HtmlBody)) {
            result = executeEventHandler(event, propHandlerArgs);
            if (event.isPropagationStopped()) {
                return result;
            }
        }

        // the registered listeners (if any)
        final ScriptResult newResult = executeEventListeners(false, event, args);
        if (newResult != null) {
            result = newResult;
        }
        return result;
    }

    /**
     * Executes capturing listeners.
     * @param event the event
     * @param args the arguments
     * @return the result
     */
    public ScriptResult executeCapturingListeners(final Event event, final Object[] args) {
        return executeEventListeners(true, event, args);
    }

    /**
     * Returns an event handler.
     * @param eventType the event name (e.g. "click")
     * @return the handler function, {@code null} if the property is null or not a function
     */
    public Function getEventHandler(final String eventType) {
        final TypeContainer container = typeContainers_.get(eventType.toLowerCase(Locale.ROOT));
        if (container == null) {
            return null;
        }
        return (Function) container.handler_;
    }

    /**
     * Returns {@code true} if there are any event listeners for the specified event.
     * @param eventType the event type (e.g. "click")
     * @return {@code true} if there are any event listeners for the specified event, {@code false} otherwise
     */
    boolean hasEventListeners(final String eventType) {
        final TypeContainer container = typeContainers_.get(eventType);
        return container != null
            && (container.handler_ instanceof Function
                    || !container.bubblingListeners_.isEmpty()
                    || !container.capturingListeners_.isEmpty());
    }

    /**
     * Executes listeners.
     *
     * @param event the event
     * @param args the arguments
     * @param propHandlerArgs handler arguments
     * @return the result
     */
    ScriptResult executeListeners(final Event event, final Object[] args, final Object[] propHandlerArgs) {
        // the registered capturing listeners (if any)
        event.setEventPhase(Event.CAPTURING_PHASE);
        ScriptResult result = executeEventListeners(true, event, args);
        if (event.isPropagationStopped()) {
            return result;
        }

        // the handler declared as property (if any)
        event.setEventPhase(Event.AT_TARGET);
        ScriptResult newResult = executeEventHandler(event, propHandlerArgs);
        if (newResult != null) {
            result = newResult;
        }
        if (event.isPropagationStopped()) {
            return result;
        }

        // the registered bubbling listeners (if any)
        event.setEventPhase(Event.BUBBLING_PHASE);
        newResult = executeEventListeners(false, event, args);
        if (newResult != null) {
            result = newResult;
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[node=" + jsNode_ + " handlers=" + typeContainers_.keySet() + "]";
    }
}
