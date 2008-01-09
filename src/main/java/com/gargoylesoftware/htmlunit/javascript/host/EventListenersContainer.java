/*
 * Copyright (c) 2002-2008 Gargoyle Software Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment:
 *
 *       "This product includes software developed by Gargoyle Software Inc.
 *        (http://www.GargoyleSoftware.com/)."
 *
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 4. The name "Gargoyle Software" must not be used to endorse or promote
 *    products derived from this software without prior written permission.
 *    For written permission, please contact info@GargoyleSoftware.com.
 * 5. Products derived from this software may not be called "HtmlUnit", nor may
 *    "HtmlUnit" appear in their name, without prior written permission of
 *    Gargoyle Software Inc.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL GARGOYLE
 * SOFTWARE INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gargoylesoftware.htmlunit.javascript.host;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Function;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlBody;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

class EventListenersContainer {
    static class Handlers {
        private final List capturingHandlers_ = new ArrayList();
        private final List bubblingHandlers_ = new ArrayList();
        private Object handler_;

        List getHandlers(final boolean useCapture) {
            if (useCapture) {
                return capturingHandlers_;
            }
            else {
                return bubblingHandlers_;
            }
        }
    }
    
    private final Map eventHandlers_ = new HashMap();
    private final SimpleScriptable jsNode_;

    EventListenersContainer(final SimpleScriptable jsNode) {
        jsNode_ = jsNode;
    }

    /**
     * Adds an event listener
     * @param type the event type to listen for (like "load")
     * @param listener the event listener
     * @param useCapture If <code>true</code>, indicates that the user wishes to initiate capture (not yet implemented)
     * @return <code>true</code> if the listener has been added
     */
    public boolean addEventListener(final String type, final Function listener, final boolean useCapture)
    {
        final List listeners = getHandlersOrCreateIt(type).getHandlers(useCapture);
        if (listeners.contains(listener)) {
            getLog().debug(type + " listener already registered, skipping it (" + listener + ")");
            return false;
        }
        else {
            listeners.add(listener);
            return true;
        }
    }

    private Handlers getHandlersOrCreateIt(final String type) {
        Handlers handlers = (Handlers) eventHandlers_.get(type.toLowerCase());
        if (handlers == null) {
            handlers = new Handlers();
            eventHandlers_.put(type.toLowerCase(), handlers);
        }
        return handlers;
    }

    private List getHandlers(final String eventType, final boolean useCapture) {
        final Handlers handlers = (Handlers) eventHandlers_.get(eventType.toLowerCase());
        if (handlers != null) {
            return handlers.getHandlers(useCapture);
        }
        return null;
    }

    public void removeEventListener(final String type, final Function listener, final boolean useCapture) {
        final List handlers = getHandlers(type, useCapture);
        if (handlers != null) {
            handlers.remove(listener);
        }
    }

    /**
     * Sets the handler property (with an handler or something else)
     * @param eventName the event name (like "click")
     * @param value the new property
     */
    public void setEventHandlerProp(final String eventName, final Object value) {
        final Handlers handlers = getHandlersOrCreateIt(eventName);
        handlers.handler_ = value;
    }

    public Object getEventHandlerProp(final String eventName) {
        final Handlers handlers = (Handlers) eventHandlers_.get(eventName);
        if (handlers == null) {
            return null;
        }
        else {
            // TODO: handle differences between IE and FF: null vs undefined
            return handlers.handler_;
        }
    }

    private ScriptResult executeEventListeners(final boolean useCapture, final Event event,
            final Object[] args) {

        final List handlers = getHandlers(event.jsxGet_type(), useCapture);
        if (handlers != null && !handlers.isEmpty()) {
            event.setCurrentTarget(jsNode_);

            ScriptResult result;
            final DomNode node = jsNode_.getDomNodeOrDie();
            final HtmlPage page = (HtmlPage) node.getPage();
            // make a copy of the list as execution of an handler may (de-)register handlers
            final List handlersToExecute = new ArrayList(handlers);
            for (final Iterator iter = handlersToExecute.iterator(); iter.hasNext();) {
                final Function listener = (Function) iter.next();
                result = page.executeJavaScriptFunctionIfPossible(
                        listener, jsNode_, args, node);
                if (event.isPropagationStopped()) {
                    return result;
                }
            }
        }
        
        return null;
    }

    private ScriptResult executeEventHandler(final Event event, final Object[] propHandlerArgs) {
        final Function handler = getEventHandler(event.jsxGet_type());
        if (handler != null) {
            final DomNode node = jsNode_.getDomNodeOrDie();
            final HtmlPage page = (HtmlPage) node.getPage();
            getLog().debug("Executing " + event.jsxGet_type() + " handler for " + node);
            return page.executeJavaScriptFunctionIfPossible(
                    handler, jsNode_, propHandlerArgs, node);
        }
        
        return null;
    }

    public ScriptResult executeBubblingListeners(final Event event, final Object[] args,
            final Object[] propHandlerArgs) {
        ScriptResult result = null;

        // the handler declared as property if any (not on body, as handler declared on body goes to the window)
        if (!(jsNode_.getDomNodeOrDie() instanceof HtmlBody)) {
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

    public ScriptResult executeCapturingListeners(final Event event, final Object[] args) {
        return executeEventListeners(true, event, args);
    }

    /**
     * Gets an event handler
     * @param eventName the event name (ex: "click")
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
     * Return the log that is being used for all scripting objects
     * @return The log.
     */
    protected final Log getLog() {
        return LogFactory.getLog(getClass());
    }

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

}
