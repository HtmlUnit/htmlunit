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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_CALL_RESULT_IS_LAST_RETURN_VALUE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_EVENT_WINDOW_EXECUTE_IF_DITACHED;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.IE;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomDocumentFragment;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.htmlunit.javascript.host.Window2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument2;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement2;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimpleObjectConstructor;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

/**
 * A JavaScript object for {@code EventTarget}.
 *
 * @author Ahmed Ashour
 */
@ScriptClass
public class EventTarget2 extends SimpleScriptObject {

    private EventListenersContainer2 eventListenersContainer_;

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static EventTarget2 constructor(final boolean newObj, final Object self) {
        final EventTarget2 host = new EventTarget2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Allows the registration of event listeners on the event target.
     *
     * @param self this object
     * @param type the event type to listen for (like "click")
     * @param listener the event listener
     * @param useCapture If {@code true}, indicates that the user wishes to initiate capture
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/element.addEventListener">Mozilla documentation</a>
     */
    @Function
    public static void addEventListener(final ScriptObject self, final String type, final ScriptObject listener,
            final boolean useCapture) {
        final EventTarget2 eventTarget;
        if (self instanceof EventTarget2) {
            eventTarget = (EventTarget2) self;
        }
        else {
            eventTarget = ((Global) self).<Window2>getWindow();
        }
        eventTarget.getEventListenersContainer().addEventListener(type, listener, useCapture);
    }

    /**
     * Gets the container for event listeners.
     * @return the container (newly created if needed)
     */
    public EventListenersContainer2 getEventListenersContainer() {
        if (eventListenersContainer_ == null) {
            eventListenersContainer_ = new EventListenersContainer2(this);
        }
        return eventListenersContainer_;
    }

    /**
     * Executes the event on this object only (needed for instance for onload on (i)frame tags).
     * @param event the event
     * @return the result
     * @see #fireEvent(Event2)
     */
    public ScriptResult executeEventLocally(final Event2 event) {
        final EventListenersContainer2 eventListenersContainer = getEventListenersContainer();
        if (eventListenersContainer != null) {
            final Window2 window = getWindow();
            final Object[] args = new Object[] {event};

            // handlers declared as property on a node don't receive the event as argument for IE
            final Object[] propHandlerArgs = args;

            final Event2 previousEvent = window.getCurrentEvent();
            window.setCurrentEvent(event);
            try {
                return eventListenersContainer.executeListeners(event, args, propHandlerArgs);
            }
            finally {
                window.setCurrentEvent(previousEvent); // reset event
            }
        }
        return null;
    }

    /**
     * Fires the event on the node with capturing and bubbling phase.
     * @param event the event
     * @return the result
     */
    public ScriptResult fireEvent(final Event2 event) {
        final Window2 window = getWindow();
        final Object[] args = new Object[] {event};

        event.startFire();
        ScriptResult result = null;
        final Event2 previousEvent = window.getCurrentEvent();
        window.setCurrentEvent(event);

        try {
            // window's listeners
            final EventListenersContainer2 windowsListeners = window.getEventListenersContainer();

            // capturing phase
            event.setEventPhase(Event.CAPTURING_PHASE);
            final boolean windowEventIfDetached = getBrowserVersion().hasFeature(JS_EVENT_WINDOW_EXECUTE_IF_DITACHED);

            boolean isAttached = false;
            for (DomNode node = getDomNodeOrNull(); node != null; node = node.getParentNode()) {
                if (node instanceof Document || node instanceof DomDocumentFragment) {
                    isAttached = true;
                    break;
                }
            }

            if (isAttached || windowEventIfDetached) {
                result = windowsListeners.executeCapturingListeners(event, args);
                if (event.isPropagationStopped()) {
                    return result;
                }
            }
            final List<EventTarget2> eventTargetList = new ArrayList<>();
            EventTarget2 eventTarget = this;
            while (eventTarget != null) {
                if (isAttached) {
                    eventTargetList.add(eventTarget);
                }
                final DomNode domNode = eventTarget.getDomNodeOrNull();
                eventTarget = null;
                if (domNode != null && domNode.getParentNode() != null) {
                    eventTarget = (EventTarget2) domNode.getParentNode().getScriptObject2();
                }
            }

            final boolean ie = getBrowserVersion().hasFeature(JS_CALL_RESULT_IS_LAST_RETURN_VALUE);
            for (int i = eventTargetList.size() - 1; i >= 0; i--) {
                final EventTarget2 jsNode = eventTargetList.get(i);
                final EventListenersContainer2 elc = jsNode.eventListenersContainer_;
                if (elc != null && isAttached) {
                    final ScriptResult r = elc.executeCapturingListeners(event, args);
                    result = ScriptResult.combine(r, result, ie);
                    if (event.isPropagationStopped()) {
                        return result;
                    }
                }
            }

            // handlers declared as property on a node don't receive the event as argument for IE
            final Object[] propHandlerArgs = args;

            // bubbling phase
            event.setEventPhase(Event.AT_TARGET);
            eventTarget = this;
            while (eventTarget != null) {
                final EventTarget2 jsNode = eventTarget;
                final EventListenersContainer2 elc = jsNode.eventListenersContainer_;
                if (elc != null && !(jsNode instanceof Window2) && (isAttached || !(jsNode instanceof HTMLElement2))) {
                    final ScriptResult r = elc.executeBubblingListeners(event, args, propHandlerArgs);
                    result = ScriptResult.combine(r, result, ie);
                    if (event.isPropagationStopped()) {
                        return result;
                    }
                }
                final DomNode domNode = eventTarget.getDomNodeOrNull();
                eventTarget = null;
                if (domNode != null && domNode.getParentNode() != null) {
                    eventTarget = (EventTarget2) domNode.getParentNode().getScriptObject2();
                }
                event.setEventPhase(Event.BUBBLING_PHASE);
            }

            if (isAttached || windowEventIfDetached) {
                final ScriptResult r = windowsListeners.executeBubblingListeners(event, args, propHandlerArgs);
                result = ScriptResult.combine(r, result, ie);
            }
        }
        finally {
            event.endFire();
            window.setCurrentEvent(previousEvent); // reset event
        }

        return result;
    }

    /**
     * Returns {@code true} if there are any event handlers for the specified event.
     * @param eventName the event name (e.g. "onclick")
     * @return {@code true} if there are any event handlers for the specified event, {@code false} otherwise
     */
    public boolean hasEventHandlers(final String eventName) {
        if (eventListenersContainer_ == null) {
            return false;
        }
        return eventListenersContainer_.hasEventListeners(StringUtils.substring(eventName, 2));
    }

    /**
     * Returns the specified event handler.
     * @param eventName the event name (e.g. "onclick")
     * @return the handler function, or {@code null} if the property is null or not a function
     */
    public ScriptFunction getEventHandler(final String eventName) {
        if (eventListenersContainer_ == null) {
            return null;
        }
        return eventListenersContainer_.getEventHandler(StringUtils.substring(eventName, 2));
    }

    /**
     * Gets the property defined as event handler (not necessary a Function if something else has been set).
     * @param eventName the event name (e.g. "onclick")
     * @return the property
     */
    protected Object getEventHandlerProp(final String eventName) {
        if (eventListenersContainer_ == null) {
            return null;
        }

        final String name = StringUtils.substring(eventName.toLowerCase(Locale.ROOT), 2);
        return eventListenersContainer_.getEventHandlerProp(name);
    }

    /**
     * Dispatches an event into the event system (standards-conformant browsers only). See
     * <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/dispatchEvent">the Gecko
     * DOM reference</a> for more information.
     *
     * @param self this object
     * @param event the event to be dispatched
     * @return {@code false} if at least one of the event handlers which handled the event
     *         called <tt>preventDefault</tt>; {@code true} otherwise
     */
    @Function
    public static boolean dispatchEvent(final Object self, final Event2 event) {
        if (self instanceof Global) {
            return Window2.dispatchEvent(((Global) self).getWindow(), event);
        }
        final EventTarget2 eventTarget;
        if (self instanceof EventTarget2) {
            eventTarget = (EventTarget2) self;
        }
        else {
            final HTMLDocument2 document = (HTMLDocument2) Window2.getDocument(self);
            eventTarget = document.getBody();
        }
        event.setTarget(eventTarget);
        final DomElement element = (DomElement) eventTarget.getDomNodeOrNull();
        ScriptResult result = null;
        if (event.getType().equals(MouseEvent.TYPE_CLICK)) {
            try {
                element.click(event, true);
            }
            catch (final IOException e) {
                throw new RuntimeException("Error calling click(): " + e.getMessage());
            }
        }
        else {
            result = eventTarget.fireEvent(event);
        }
        return !event.isAborted(result);
    }

    /**
     * Allows the removal of event listeners on the event target.
     *
     * @param self this object
     * @param type the event type to listen for (like "click")
     * @param listener the event listener
     * @param useCapture If {@code true}, indicates that the user wishes to initiate capture (not yet implemented)
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/element.removeEventListener">Mozilla
     * documentation</a>
     */
    @Function
    public static void removeEventListener(final ScriptObject self, final String type, final ScriptObject listener,
            final boolean useCapture) {
        final EventTarget2 eventTarget;
        if (self instanceof EventTarget2) {
            eventTarget = (EventTarget2) self;
        }
        else {
            eventTarget = ((Global) self).<Window2>getWindow();
        }
        eventTarget.getEventListenersContainer().removeEventListener(type, listener, useCapture);
    }

    /**
     * Defines an event handler (or maybe any other object).
     * @param eventName the event name (e.g. "onclick")
     * @param value the property ({@code null} to reset it)
     */
    protected void setEventHandler(final String eventName, final Object value) {
        final EventListenersContainer2 container;
        if (isEventHandlerOnWindow()) {
            container = getWindow().getEventListenersContainer();
        }
        else {
            container = getEventListenersContainer();
        }
        container.setEventHandler(
                StringUtils.substring(eventName.toLowerCase(Locale.ROOT), 2), value);
    }

    /**
     * Is setting event handler property, at window-level.
     * @return whether the event handler to be set at window-level
     */
    protected boolean isEventHandlerOnWindow() {
        return false;
    }

    /**
     * Clears the event listener container.
     */
    protected void clearEventListenersContainer() {
        eventListenersContainer_ = null;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(EventTarget2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Function constructor.
     */
    @ClassConstructor({CHROME, FF})
    public static final class FunctionConstructor extends ScriptFunction {
        /**
         * Constructor.
         */
        public FunctionConstructor() {
            super("EventTarget",
                    staticHandle("constructor", EventTarget2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("EventTarget");
        }
    }

    /** Object constructor. */
    @ClassConstructor(IE)
    public static final class ObjectConstructor extends SimpleObjectConstructor {
        /** Constructor. */
        public ObjectConstructor() {
            super("EventTarget");
            ScriptUtils.initialize(this);
            setProto(new Prototype());
        }
    }

}
