/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.htmlunit.corejs.javascript.Function;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * Container for event listener.
 * @version $Revision$
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class EventListenersContainer implements Serializable {

    private static final long serialVersionUID = -4612648636521726975L;
    private static final Log LOG = LogFactory.getLog(EventListenersContainer.class);

    static class Handlers implements Serializable {
        private static final long serialVersionUID = -5322935816539773122L;
        private final List<Function> capturingHandlers_ = new ArrayList<Function>();
        private final List<Function> bubblingHandlers_ = new ArrayList<Function>();
        private Object handler_;
        List<Function> getHandlers(final boolean useCapture) {
            if (useCapture) {
                return capturingHandlers_;
            }
            return bubblingHandlers_;
        }
        @Override
        protected Handlers clone() {
            final Handlers clone = new Handlers();
            clone.handler_ = handler_;
            clone.capturingHandlers_.addAll(capturingHandlers_);
            clone.bubblingHandlers_.addAll(bubblingHandlers_);
            return clone;
        }
    }

    private final Map<String, Handlers> eventHandlers_ = new HashMap<String, Handlers>();
    private final SimpleScriptable jsNode_;

    EventListenersContainer(final SimpleScriptable jsNode) {
        jsNode_ = jsNode;
    }

    /**
     * Adds an event listener.
     * @param type the event type to listen for (like "load")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @return <code>true</code> if the listener has been added
     */
    public boolean addEventListener(final String type, final Function listener, final boolean useCapture) {
        final List<Function> listeners = getHandlersOrCreateIt(type).getHandlers(useCapture);
        if (listeners.contains(listener)) {
            if (LOG.isDebugEnabled()) {
                LOG.debug(type + " listener already registered, skipping it (" + listener + ")");
            }
            return false;
        }
        listeners.add(listener);
        return true;
    }

    private Handlers getHandlersOrCreateIt(final String type) {
        Handlers handlers = eventHandlers_.get(type.toLowerCase());
        if (handlers == null) {
            handlers = new Handlers();
            eventHandlers_.put(type.toLowerCase(), handlers);
        }
        return handlers;
    }

    private List<Function> getHandlers(final String eventType, final boolean useCapture) {
        final Handlers handlers = eventHandlers_.get(eventType.toLowerCase());
        if (handlers != null) {
            return handlers.getHandlers(useCapture);
        }
        return null;
    }

    /**
     * Removes event listener.
     * @param type the type
     * @param listener the listener
     * @param useCapture to use capture or not
     */
    public void removeEventListener(final String type, final Function listener, final boolean useCapture) {
        final List<Function> handlers = getHandlers(type, useCapture);
        if (handlers != null) {
            handlers.remove(listener);
        }
    }

    /**
     * Sets the handler property (with an handler or something else).
     * @param eventName the event name (like "click")
     * @param value the new property
     */
    public void setEventHandlerProp(final String eventName, final Object value) {
        final Handlers handlers = getHandlersOrCreateIt(eventName);
        handlers.handler_ = value;
    }

    /**
     * Returns event handler property.
     * @param eventName event name
     * @return the handler, or null if not found
     */
    public Object getEventHandlerProp(final String eventName) {
        final Handlers handlers = eventHandlers_.get(eventName);
        if (handlers == null) {
            return null;
        }
        // TODO: handle differences between IE and FF: null vs undefined
        return handlers.handler_;
    }

    private ScriptResult executeEventListeners(final boolean useCapture, final Event event, final Object[] args) {
        final DomNode node = jsNode_.getDomNodeOrDie();
        // some event don't apply on all kind of nodes, for instance "blur"
        if (!event.applies(node)) {
            return null;
        }
        final boolean ie = jsNode_.getWindow().getWebWindow().getWebClient()
            .getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_40);
        ScriptResult allResult = null;
        final List<Function> handlers = getHandlers(event.jsxGet_type(), useCapture);
        if (handlers != null && !handlers.isEmpty()) {
            event.setCurrentTarget(jsNode_);
            final HtmlPage page = (HtmlPage) node.getPage();
            // make a copy of the list as execution of an handler may (de-)register handlers
            final List<Function> handlersToExecute = new ArrayList<Function>(handlers);
            for (final Function listener : handlersToExecute) {
                final ScriptResult result = page.executeJavaScriptFunctionIfPossible(listener, jsNode_, args, node);
                if (event.isPropagationStopped()) {
                    allResult = result;
                }
                if (ie) {
                    if (ScriptResult.isFalse(result)) {
                        allResult = result;
                    }
                    else {
                        final Object eventReturnValue = event.jsxGet_returnValue();
                        if (eventReturnValue instanceof Boolean && !((Boolean) eventReturnValue).booleanValue()) {
                            allResult = new ScriptResult(false, page);
                        }
                    }
                }
            }
        }
        return allResult;
    }

    private ScriptResult executeEventHandler(final Event event, final Object[] propHandlerArgs) {
        final DomNode node = jsNode_.getDomNodeOrDie();
        // some event don't apply on all kind of nodes, for instance "blur"
        if (!event.applies(node)) {
            return null;
        }
        final Function handler = getEventHandler(event.jsxGet_type());
        if (handler != null) {
            event.setCurrentTarget(jsNode_);
            final HtmlPage page = (HtmlPage) node.getPage();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Executing " + event.jsxGet_type() + " handler for " + node);
            }
            return page.executeJavaScriptFunctionIfPossible(handler, jsNode_, propHandlerArgs, node);
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
        final DomNode domNode = jsNode_.getDomNodeOrDie();
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
     * Gets an event handler.
     * @param eventName the event name (e.g. "click")
     * @return the handler function, <code>null</code> if the property is null or not a function
     */
    public Function getEventHandler(final String eventName) {
        final Object handler = getEventHandlerProp(eventName.toLowerCase());
        if (handler instanceof Function) {
            return (Function) handler;
        }
        return null;
    }

    /**
     * Returns <tt>true</tt> if there are any event handlers for the specified event.
     * @param eventName the event name (e.g. "click")
     * @return <tt>true</tt> if there are any event handlers for the specified event, <tt>false</tt> otherwise
     */
    public boolean hasEventHandlers(final String eventName) {
        final Handlers h = eventHandlers_.get(eventName);
        return (h != null
            && (h.handler_ instanceof Function || !h.bubblingHandlers_.isEmpty() || !h.capturingHandlers_.isEmpty()));
    }

    /**
     * Executes listeners.
     * @param event the event
     * @param args the arguments
     * @param propHandlerArgs handler arguments
     * @return the result
     */
    public ScriptResult executeListeners(final Event event, final Object[] args, final Object[] propHandlerArgs) {
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
     * Copies all the events from the provided container.
     * @param eventListenersContainer where to copy from
     */
    void copyFrom(final EventListenersContainer eventListenersContainer) {
        for (final Map.Entry<String, Handlers> entry : eventListenersContainer.eventHandlers_.entrySet()) {
            final Handlers handlers = entry.getValue().clone();
            eventHandlers_.put(entry.getKey(), handlers);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "[node=" + jsNode_ + " handlers=" + eventHandlers_.keySet() + "]";
    }

}
