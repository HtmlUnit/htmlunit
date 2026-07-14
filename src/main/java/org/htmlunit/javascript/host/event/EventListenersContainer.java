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
package org.htmlunit.javascript.host.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlunit.ScriptResult;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.TopLevel;
import org.htmlunit.corejs.javascript.VarScope;
import org.htmlunit.corejs.javascript.WithScope;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.html.HTMLDocument;
import org.htmlunit.javascript.host.html.HTMLElement;

/**
 * Container for event listeners.
 *
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 */
public class EventListenersContainer implements Serializable {

    private static final Log LOG = LogFactory.getLog(EventListenersContainer.class);

    // Refactoring note: This seems ad-hoc. Shouldn't synchronization be orchestrated between
    // JS thread and main thread at a much higher layer?  Anyway, to preserve behaviour of prior
    // coding where 'synchronized' was used more explicitly, we're using a ConcurrentHashMap here
    // and using ConcurrentMap.compute() to mutate below so that mutations are atomic.  This for
    // example avoids the case where two concurrent addListener()s can result in either being lost.
    private final ConcurrentMap<String, TypeContainer> typeContainers_ = new ConcurrentHashMap<>();
    private final EventTarget jsNode_;

    private record TypeContainer(List<Scriptable> capturingListeners_, List<Scriptable> bubblingListeners_,
                                 List<Scriptable> atTargetListeners_, Function handler_) implements Serializable {
        public static final TypeContainer EMPTY = new TypeContainer();

        // This sentinel value could be some singleton instance but null
        // isn't used for anything else so why not.
        private static final Scriptable EVENT_HANDLER_PLACEHOLDER = null;

        TypeContainer() {
            this(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), null);
        }

        List<Scriptable> getListeners(final int eventPhase) {
            return switch (eventPhase) {
                case Event.CAPTURING_PHASE -> capturingListeners_;
                case Event.AT_TARGET -> atTargetListeners_;
                case Event.BUBBLING_PHASE -> bubblingListeners_;
                default -> throw new UnsupportedOperationException("eventPhase: " + eventPhase);
            };
        }

        public TypeContainer setPropertyHandler(final Function propertyHandler) {
            if (propertyHandler != null) {
                // If we already have a handler then the position of the existing
                // placeholder should not be changed so just change the handler
                if (handler_ != null) {
                    if (propertyHandler == handler_) {
                        return this;
                    }
                    return withPropertyHandler(propertyHandler);
                }

                // Insert the placeholder and set the handler
                return withPropertyHandler(propertyHandler).addListener(EVENT_HANDLER_PLACEHOLDER, false);
            }
            if (handler_ == null) {
                return this;
            }
            return removeListener(EVENT_HANDLER_PLACEHOLDER, false).withPropertyHandler(null);
        }

        private TypeContainer withPropertyHandler(final Function propertyHandler) {
            return new TypeContainer(capturingListeners_, bubblingListeners_, atTargetListeners_, propertyHandler);
        }

        public TypeContainer addListener(final Scriptable listener, final boolean useCapture) {
            List<Scriptable> capturingListeners = capturingListeners_;
            List<Scriptable> bubblingListeners = bubblingListeners_;
            final List<Scriptable> listeners = useCapture ? capturingListeners : bubblingListeners;

            if (listeners.contains(listener)) {
                return this;
            }

            List<Scriptable> newListeners = new ArrayList<>(listeners.size() + 1);
            newListeners.addAll(listeners);
            newListeners.add(listener);
            newListeners = Collections.unmodifiableList(newListeners);

            if (useCapture) {
                capturingListeners = newListeners;
            }
            else {
                bubblingListeners = newListeners;
            }

            List<Scriptable> atTargetListeners = new ArrayList<>(atTargetListeners_.size() + 1);
            atTargetListeners.addAll(atTargetListeners_);
            atTargetListeners.add(listener);
            atTargetListeners = Collections.unmodifiableList(atTargetListeners);

            return new TypeContainer(capturingListeners, bubblingListeners, atTargetListeners, handler_);
        }

        public TypeContainer removeListener(final Scriptable listener, final boolean useCapture) {
            List<Scriptable> capturingListeners = capturingListeners_;
            List<Scriptable> bubblingListeners = bubblingListeners_;
            final List<Scriptable> listeners = useCapture ? capturingListeners : bubblingListeners;

            final int idx = listeners.indexOf(listener);
            if (idx < 0) {
                return this;
            }

            List<Scriptable> newListeners = new ArrayList<>(listeners);
            newListeners.remove(idx);
            newListeners = Collections.unmodifiableList(newListeners);

            if (useCapture) {
                capturingListeners = newListeners;
            }
            else {
                bubblingListeners = newListeners;
            }

            List<Scriptable> atTargetListeners = new ArrayList<>(atTargetListeners_);
            atTargetListeners.remove(listener);
            atTargetListeners = Collections.unmodifiableList(atTargetListeners);

            return new TypeContainer(capturingListeners, bubblingListeners, atTargetListeners, handler_);
        }

        // Refactoring note: This method doesn't appear to be used
        @Override
        protected TypeContainer clone() {
            return new TypeContainer(capturingListeners_, bubblingListeners_, atTargetListeners_, handler_);
        }
    }

    /**
     * Creates a new container for the given event target node.
     *
     * @param jsNode the node
     */
    public EventListenersContainer(final EventTarget jsNode) {
        jsNode_ = jsNode;
    }

    /**
     * Adds an event listener.
     *
     * @param type the event type to listen for (e.g. {@code "load"})
     * @param listener the event listener
     * @param useCapture if {@code true}, the listener is added for the capture phase
     * @return {@code true} if the listener was added; {@code false} if it was already registered
     */
    public boolean addEventListener(final String type, final Scriptable listener, final boolean useCapture) {
        if (null == listener) {
            return true;
        }

        final boolean[] added = {false};
        typeContainers_.compute(type.toLowerCase(Locale.ROOT), (k, container) -> {
            if (container == null) {
                container = TypeContainer.EMPTY;
            }
            final TypeContainer newContainer = container.addListener(listener, useCapture);
            added[0] = newContainer != container;
            return newContainer;
        });

        if (!added[0]) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(type + " listener already registered, skipping it (" + listener + ")");
            }
            return false;
        }
        return true;
    }

    private TypeContainer getTypeContainer(final String type) {
        final String typeLC = type.toLowerCase(Locale.ROOT);
        return typeContainers_.getOrDefault(typeLC, TypeContainer.EMPTY);
    }

    /**
     * Returns the listeners for the given event type and capture mode.
     *
     * @param eventType the event type
     * @param useCapture whether to return capture-phase listeners
     * @return the list of listeners (empty list if none)
     */
    public List<Scriptable> getListeners(final String eventType, final boolean useCapture) {
        return getTypeContainer(eventType).getListeners(useCapture ? Event.CAPTURING_PHASE : Event.BUBBLING_PHASE);
    }

    /**
     * Removes an event listener.
     *
     * @param eventType the event type
     * @param listener the listener to remove
     * @param useCapture whether to remove from the capture phase
     */
    void removeEventListener(final String eventType, final Scriptable listener, final boolean useCapture) {
        if (listener == null) {
            return;
        }

        typeContainers_.computeIfPresent(eventType.toLowerCase(Locale.ROOT),
            (k, container) -> container.removeListener(listener, useCapture));
    }

    /**
     * Sets the property handler for the given event type.
     *
     * @param eventType the event type (e.g. {@code "click"})
     * @param value the new handler, or {@code null} to remove it
     */
    public void setEventHandler(final String eventType, final Object value) {
        final Function handler;

        // Otherwise, ignore silently.
        if (JavaScriptEngine.isUndefined(value) || !(value instanceof Function)) {
            handler = null;
        }
        else {
            handler = (Function) value;
        }

        typeContainers_.compute(eventType.toLowerCase(Locale.ROOT), (k, container) -> {
            if (container == null) {
                container = TypeContainer.EMPTY;
            }
            return container.setPropertyHandler(handler);
        });
    }

    private void executeEventListeners(final int eventPhase, final Event event, final Object[] args) {
        final DomNode node = jsNode_.getDomNodeOrNull();
        // some event don't apply on all kind of nodes, for instance "blur"
        if (node != null && !node.handles(event)) {
            return;
        }

        final TypeContainer container = getTypeContainer(event.getType());
        final List<Scriptable> listeners = container.getListeners(eventPhase);
        if (!listeners.isEmpty()) {
            event.setCurrentTarget(jsNode_);

            final HtmlPage page;
            if (jsNode_ instanceof Window) {
                page = (HtmlPage) jsNode_.getDomNodeOrDie();
            }
            else {
                Scriptable scriptableObj = null;
                final VarScope parentScope = jsNode_.getParentScope();
                if (parentScope instanceof TopLevel topLevel) {
                    scriptableObj = topLevel.getGlobalThis();
                }
                else if (parentScope instanceof WithScope withScope) {
                    scriptableObj = withScope.getObject();
                }

                if (scriptableObj instanceof Window window) {
                    page = (HtmlPage) window.getDomNodeOrDie();
                }
                else if (scriptableObj instanceof HTMLDocument document) {
                    page = document.getPage();
                }
                else if (scriptableObj != null) {
                    page = ((HTMLElement) scriptableObj).getDomNodeOrDie().getHtmlPageOrNull();
                }
                else {
                    page = null;
                    throw new UnsupportedOperationException("TODO ");
                }
            }

            // no need for a copy, listeners are copy on write
            for (Scriptable listener : listeners) {
                boolean isPropertyHandler = false;
                if (listener == TypeContainer.EVENT_HANDLER_PLACEHOLDER) {
                    listener = container.handler_;
                    isPropertyHandler = true;
                }
                Function function = null;
                Scriptable thisObject = null;
                if (listener instanceof Function function2) {
                    function = function2;
                    thisObject = jsNode_;
                }
                else if (listener instanceof NativeObject) {
                    final Object handleEvent = ScriptableObject.getProperty(listener, "handleEvent");
                    if (handleEvent instanceof Function function1) {
                        function = function1;
                        thisObject = listener;
                    }
                }
                if (function != null) {
                    final ScriptResult result =
                            page.executeJavaScriptFunction(function, thisObject, args, node);
                    // Return value is only honored for property handlers (Tested in Chrome/FF/IE11)
                    if (isPropertyHandler && !ScriptResult.isUndefined(result)) {
                        event.handlePropertyHandlerReturnValue(result.getJavaScriptResult());
                    }
                }
                if (event.isImmediatePropagationStopped()) {
                    return;
                }
            }
        }
    }

    /**
     * Executes bubbling listeners for the given event.
     *
     * @param event the event
     * @param args the arguments
     */
    public void executeBubblingListeners(final Event event, final Object[] args) {
        executeEventListeners(Event.BUBBLING_PHASE, event, args);
    }

    /**
     * Executes capturing listeners for the given event.
     *
     * @param event the event
     * @param args the arguments
     */
    public void executeCapturingListeners(final Event event, final Object[] args) {
        executeEventListeners(Event.CAPTURING_PHASE, event, args);
    }

    /**
     * Executes listeners for events targeting this node (non-propagation phase).
     *
     * @param event the event
     * @param args the arguments
     */
    public void executeAtTargetListeners(final Event event, final Object[] args) {
        executeEventListeners(Event.AT_TARGET, event, args);
    }

    /**
     * Returns the event handler function for the given event type.
     *
     * @param eventType the event type (e.g. {@code "click"})
     * @return the handler function, or {@code null} if not set
     */
    public Function getEventHandler(final String eventType) {
        return getTypeContainer(eventType).handler_;
    }

    /**
     * Returns whether there are any event listeners registered for the given event type.
     *
     * @param eventType the event type (e.g. {@code "click"})
     * @return {@code true} if there are any listeners, {@code false} otherwise
     */
    boolean hasEventListeners(final String eventType) {
        return !getTypeContainer(eventType).atTargetListeners_.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[node=" + jsNode_ + " handlers=" + typeContainers_.keySet() + "]";
    }
}
