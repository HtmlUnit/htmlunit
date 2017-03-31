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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_FALSE_RESULT;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.InteractivePage;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptRuntime;
import com.gargoylesoftware.js.nashorn.internal.runtime.Undefined;

/**
 * Container for event listener.
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Frank Danek
 */
public class EventListenersContainer2 implements Serializable {

    private static final Log LOG = LogFactory.getLog(EventListenersContainer2.class);

    static class TypeContainer implements Serializable {
        private final List<ScriptObject> capturingListeners_ = new ArrayList<>();
        private final List<ScriptObject> bubblingListeners_ = new ArrayList<>();
        private Object handler_;
        List<ScriptObject> getListeners(final boolean useCapture) {
            if (useCapture) {
                return capturingListeners_;
            }
            return bubblingListeners_;
        }
        @Override
        protected TypeContainer clone() {
            final TypeContainer clone = new TypeContainer();
            clone.handler_ = handler_;
            clone.capturingListeners_.addAll(capturingListeners_);
            clone.bubblingListeners_.addAll(bubblingListeners_);
            return clone;
        }
    }

    private final Map<String, TypeContainer> typeContainers_ = new HashMap<>();
    private final EventTarget2 jsNode_;

    /**
     * The constructor.
     *
     * @param jsNode the node.
     */
    public EventListenersContainer2(final EventTarget2 jsNode) {
        jsNode_ = jsNode;
    }

    /**
     * Adds an event listener.
     * @param type the event type to listen for (like "load")
     * @param listener the event listener
     * @param useCapture If {@code true}, indicates that the user wishes to initiate capture (not yet implemented)
     * @return {@code true} if the listener has been added
     */
    public boolean addEventListener(final String type, final ScriptObject listener, final boolean useCapture) {
        if (null == listener) {
            return true;
        }

        final List<ScriptObject> listeners = getTypeContainer(type).getListeners(useCapture);
        if (listeners.contains(listener)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(type + " listener already registered, skipping it (" + listener + ")");
            }
            return false;
        }
        listeners.add(listener);
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
     * Returns the relevant handlers.
     * @param eventType the event type
     * @param useCapture whether to use capture of not
     * @return the handlers list
     */
    public List<ScriptObject> getListeners(final String eventType, final boolean useCapture) {
        final TypeContainer container = typeContainers_.get(eventType.toLowerCase(Locale.ROOT));
        if (container != null) {
            return container.getListeners(useCapture);
        }
        return null;
    }

    /**
     * Removes event listener.
     * @param type the type
     * @param listener the listener
     * @param useCapture to use capture or not
     */
    public void removeEventListener(final String type, final ScriptObject listener, final boolean useCapture) {
        final List<ScriptObject> listeners = getListeners(type, useCapture);
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    /**
     * Sets the handler property (with an handler or something else).
     * @param eventName the event name (like "click")
     * @param value the new property
     */
    public void setEventHandlerProp(final String eventName, final Object value) {
        Object handler = value;
        if (handler == ScriptRuntime.UNDEFINED) {
            handler = null;
        }

        final TypeContainer container = getTypeContainer(eventName);
        container.handler_ = handler;
    }

    /**
     * Returns event handler property.
     * @param eventType event type
     * @return the handler, or null if not found
     */
    public Object getEventHandlerProp(final String eventType) {
        final TypeContainer container = typeContainers_.get(eventType);
        if (container == null) {
            return null;
        }
        return container.handler_;
    }

    private ScriptObject getTarget() {
        if (jsNode_ instanceof Window2) {
            return ((Window2) jsNode_).getGlobal();
        }
        return jsNode_;
    }

    private ScriptResult executeEventListeners(final boolean useCapture, final Event2 event, final Object[] args) {
        final DomNode node = jsNode_.getDomNodeOrNull();
        // some event don't apply on all kind of nodes, for instance "blur"
        if (node == null || !node.handles(event)) {
            return null;
        }
        ScriptResult allResult = null;
        final List<ScriptObject> listeners = getListeners(Event2.getType(event), useCapture);
        if (listeners != null && !listeners.isEmpty()) {
            event.setCurrentTarget(getTarget());
            final HtmlPage page = (HtmlPage) node.getPage();
            // make a copy of the list as execution of an handler may (de-)register handlers
            final List<ScriptObject> handlersToExecute = new ArrayList<>(listeners);
            for (final ScriptObject listener : handlersToExecute) {
                ScriptFunction function = null;
                ScriptObject thisObject = null;
                if (listener instanceof ScriptFunction) {
                    function = (ScriptFunction) listener;
                    thisObject = jsNode_;
                }
//                else if (listener instanceof NativeObject) {
//                    final Object handleEvent = ScriptableObject.getProperty(listener, "handleEvent");
//                    if (handleEvent instanceof Function) {
//                        function = (Function) handleEvent;
//                        thisObject = listener;
//                    }
//                }
                if (function != null) {
                    final ScriptResult result =
                            page.executeJavaScriptFunctionIfPossible(function, thisObject, args, node);
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
            }
        }
        return allResult;
    }

    private ScriptResult executeEventHandler(final Event2 event, final Object[] propHandlerArgs) {
        final DomNode node = jsNode_.getDomNodeOrNull();
        // some event don't apply on all kind of nodes, for instance "blur"
        if (node != null && !node.handles(event)) {
            return null;
        }
        final ScriptFunction handler = getEventHandler(Event2.getType(event));
        if (handler != null) {
            event.setCurrentTarget(getTarget());
            final InteractivePage page = (InteractivePage) (node != null
                    ? node.getPage()
                    : jsNode_.getWindow().getWebWindow().getEnclosedPage());
            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing " + Event2.getType(event) + " handler for " + node);
            }
            return page.executeJavaScriptFunctionIfPossible(handler, getTarget(),
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
    public ScriptResult executeBubblingListeners(final Event2 event, final Object[] args,
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
    public ScriptResult executeCapturingListeners(final Event2 event, final Object[] args) {
        return executeEventListeners(true, event, args);
    }

    /**
     * Gets an event handler.
     * @param eventName the event name (e.g. "click")
     * @return the handler function, {@code null} if the property is null or not a function
     */
    public ScriptFunction getEventHandler(final String eventName) {
        final Object handler = getEventHandlerProp(eventName.toLowerCase(Locale.ROOT));
        if (handler instanceof ScriptFunction) {
            return (ScriptFunction) handler;
        }
        return null;
    }

    /**
     * Returns {@code true} if there are any event handlers for the specified event.
     * @param eventType the event type (e.g. "click")
     * @return {@code true} if there are any event handlers for the specified event, {@code false} otherwise
     */
    public boolean hasEventHandlers(final String eventType) {
        final TypeContainer container = typeContainers_.get(eventType);
        return container != null
            && (container.handler_ instanceof ScriptFunction
                    || !container.bubblingListeners_.isEmpty()
                    || !container.capturingListeners_.isEmpty());
    }

    /**
     * Executes listeners.
     * @param event the event
     * @param args the arguments
     * @param propHandlerArgs handler arguments
     * @return the result
     */
    public ScriptResult executeListeners(final Event2 event, final Object[] args, final Object[] propHandlerArgs) {
        // the registered capturing listeners (if any)
        event.setEventPhase(Event.CAPTURING_PHASE);
        ScriptResult result = executeEventListeners(true, event, args);
        if (event.isPropagationStopped()) {
            return result;
        }

        // the handler declared as property (if any)
//        event.setEventPhase(Event.AT_TARGET);
        ScriptResult newResult = executeEventHandler(event, propHandlerArgs);
        if (newResult != null) {
            result = newResult;
        }
//        if (event.isPropagationStopped()) {
//            return result;
//        }

        // the registered bubbling listeners (if any)
//        event.setEventPhase(Event.BUBBLING_PHASE);
        newResult = executeEventListeners(false, event, args);
        if (newResult != null) {
            result = newResult;
        }

        return result;
    }

    /**
     * Copies all the events from the provided container.
     * @param eventListenersContainer where to copy from
     */
    public void copyFrom(final EventListenersContainer2 eventListenersContainer) {
        for (final Map.Entry<String, TypeContainer> entry : eventListenersContainer.typeContainers_.entrySet()) {
            final TypeContainer handlers = entry.getValue().clone();
            typeContainers_.put(entry.getKey(), handlers);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[node=" + jsNode_ + " handlers=" + typeContainers_.keySet() + "]";
    }

}
