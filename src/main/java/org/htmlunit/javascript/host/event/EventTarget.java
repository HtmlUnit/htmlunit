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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.Page;
import org.htmlunit.ScriptResult;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.HtmlLabel;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.Window;
import org.htmlunit.javascript.host.dom.Document;

/**
 * JavaScript host object for {@code EventTarget}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Atsushi Nakagawa
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventTarget">MDN Documentation</a>
 */
@JsxClass
public class EventTarget extends HtmlUnitScriptable {

    private EventListenersContainer eventListenersContainer_;

    /**
     * Creates an instance of this object.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    /**
     * Registers an event listener on this event target.
     *
     * @param type the event type to listen for (e.g. {@code "click"})
     * @param listener the event listener
     * @param useCapture if {@code true}, indicates that the listener should be added for the capture phase
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/addEventListener">MDN Documentation</a>
     */
    @JsxFunction
    public void addEventListener(final String type, final Scriptable listener, final boolean useCapture) {
        getEventListenersContainer().addEventListener(type, listener, useCapture);
    }

    /**
     * Returns the container for event listeners, creating it if necessary.
     *
     * @return the event listeners container
     */
    public final EventListenersContainer getEventListenersContainer() {
        if (eventListenersContainer_ == null) {
            eventListenersContainer_ = new EventListenersContainer(this);
        }
        return eventListenersContainer_;
    }

    /**
     * Executes the event on this object only (needed for instance for {@code onload} on {@code (i)frame} tags).
     *
     * @param event the event to execute
     * @see #fireEvent(Event)
     */
    public void executeEventLocally(final Event event) {
        final EventListenersContainer eventListenersContainer = getEventListenersContainer();
        final Window window = getWindow();
        final Object[] args = {event};

        final Event previousEvent = window.getCurrentEvent();
        window.setCurrentEvent(event);
        try {
            event.setEventPhase(Event.AT_TARGET);
            eventListenersContainer.executeAtTargetListeners(event, args);
        }
        finally {
            window.setCurrentEvent(previousEvent); // reset event
        }
    }

    /**
     * Fires the event on this node with capturing and bubbling phases.
     *
     * @param event the event to fire
     * @return the script result
     */
    public ScriptResult fireEvent(final Event event) {
        final Window window = getWindow();

        event.startFire();
        final Event previousEvent = window.getCurrentEvent();
        window.setCurrentEvent(event);

        try {
            // These can be null if we aren't tied to a DOM node
            final DomNode ourNode = getDomNodeOrNull();
            final DomNode ourParentNode = (ourNode != null) ? ourNode.getParentNode() : null;

            // Determine the propagation path which is fixed here and not affected by
            // DOM tree modification from intermediate listeners (tested in Chrome)
            final List<EventTarget> propagationPath = new ArrayList<>();

            // We're added to the propagation path first
            propagationPath.add(this);

            // Then add all our parents if we have any (pure JS object such as XMLHttpRequest
            // and MessagePort, etc. will not have any parents)
            for (DomNode parent = ourParentNode; parent != null; parent = parent.getParentNode()) {
                // scroll does not bubble into the document/window
                if (Event.TYPE_SCROLL.equals(event.getType()) && parent instanceof Page) {
                    break;
                }

                propagationPath.add(parent.getScriptableObject());
            }

            // The load event has some unnatural behavior that we need to handle specially
            // The load event for other elements target that element but path only
            // up to Document and not Window, so do nothing here
            // (see Note in https://www.w3.org/TR/DOM-Level-3-Events/#event-type-load)
            if (!Event.TYPE_LOAD.equals(event.getType())) {
                // Add Window if the propagation path reached Document
                if (propagationPath.get(propagationPath.size() - 1) instanceof Document) {
                    propagationPath.add(window);
                }
            }

            // capturing phase
            event.setEventPhase(Event.CAPTURING_PHASE);

            for (int i = propagationPath.size() - 1; i >= 1; i--) {
                final EventTarget jsNode = propagationPath.get(i);
                final EventListenersContainer elc = jsNode.eventListenersContainer_;
                if (elc != null) {
                    elc.executeCapturingListeners(event, new Object[] {event});
                    if (event.isPropagationStopped()) {
                        return new ScriptResult(null);
                    }
                }
            }

            // at target phase
            event.setEventPhase(Event.AT_TARGET);

            if (!propagationPath.isEmpty()) {
                // Note: This element is not always the same as event.getTarget():
                // e.g. the 'load' event targets Document but "at target" is on Window.
                final EventTarget jsNode = propagationPath.get(0);
                final EventListenersContainer elc = jsNode.eventListenersContainer_;
                if (elc != null) {
                    elc.executeAtTargetListeners(event, new Object[] {event});
                    if (event.isPropagationStopped()) {
                        return new ScriptResult(null);
                    }
                }
            }

            // bubbling phase
            if (event.isBubbles()) {
                // This belongs here inside the block because events that don't bubble never set
                // eventPhase = 3 (tested in Chrome)
                event.setEventPhase(Event.BUBBLING_PHASE);

                final int size = propagationPath.size();
                for (int i = 1; i < size; i++) {
                    final EventTarget jsNode = propagationPath.get(i);
                    final EventListenersContainer elc = jsNode.eventListenersContainer_;
                    if (elc != null) {
                        elc.executeBubblingListeners(event, new Object[] {event});
                        if (event.isPropagationStopped()) {
                            return new ScriptResult(null);
                        }
                    }
                }
            }

            HtmlLabel label = null;
            if (event.processLabelAfterBubbling()) {
                for (DomNode parent = ourParentNode; parent != null; parent = parent.getParentNode()) {
                    if (parent instanceof HtmlLabel htmlLabel) {
                        label = htmlLabel;
                        break;
                    }
                }
            }

            if (label != null) {
                final HtmlElement element = label.getLabeledElement();
                if (element != null && element != getDomNodeOrNull()) {
                    try {
                        element.click(event.isShiftKey(), event.isCtrlKey(), event.isAltKey(), false, true, true, true);
                    }
                    catch (final IOException ignored) {
                        // ignore for now
                    }
                }
            }

        }
        finally {
            event.endFire();
            window.setCurrentEvent(previousEvent); // reset event
        }

        return new ScriptResult(null);
    }

    /**
     * Returns whether there are any event handlers for the specified event name.
     *
     * @param eventName the event name (e.g. {@code "onclick"})
     * @return {@code true} if there are any event handlers, {@code false} otherwise
     */
    public boolean hasEventHandlers(final String eventName) {
        if (eventListenersContainer_ == null) {
            return false;
        }
        return eventListenersContainer_.hasEventListeners(StringUtils.substring(eventName, 2));
    }

    /**
     * Returns the specified event handler function.
     *
     * @param eventType the event type (e.g. {@code "click"})
     * @return the handler function, or {@code null} if not set
     */
    public Function getEventHandler(final String eventType) {
        if (eventListenersContainer_ == null) {
            return null;
        }
        return eventListenersContainer_.getEventHandler(eventType);
    }

    /**
     * Dispatches an event into the event system.
     *
     * @param event the event to be dispatched
     * @return {@code false} if at least one of the event handlers called {@code preventDefault()};
     *         {@code true} otherwise
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/dispatchEvent">MDN Documentation</a>
     */
    @JsxFunction
    public boolean dispatchEvent(final Event event) {
        event.setTarget(this);

        ScriptResult result = null;
        final DomNode domNode = getDomNodeOrNull();
        if (MouseEvent.TYPE_CLICK.equals(event.getType()) && (domNode instanceof DomElement element)) {
            try {
                element.click(event, event.isShiftKey(), event.isCtrlKey(), event.isAltKey(), true);
            }
            catch (final IOException e) {
                throw JavaScriptEngine.reportRuntimeError("Error calling click(): " + e.getMessage());
            }
        }
        else {
            result = fireEvent(event);
        }
        return !event.isAborted(result);
    }

    /**
     * Removes a previously registered event listener from this event target.
     *
     * @param type the event type (e.g. {@code "click"})
     * @param listener the listener to remove
     * @param useCapture if {@code true}, the listener is removed from the capture phase
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/removeEventListener">MDN Documentation</a>
     */
    @JsxFunction
    public void removeEventListener(final String type, final Scriptable listener, final boolean useCapture) {
        if (eventListenersContainer_ == null) {
            return;
        }
        eventListenersContainer_.removeEventListener(type, listener, useCapture);
    }

    /**
     * Defines an event handler (or any other object) for the given event name.
     *
     * @param eventName the event name (e.g. {@code "click"})
     * @param value the handler ({@code null} to reset it)
     */
    public void setEventHandler(final String eventName, final Object value) {
        if (isEventHandlerOnWindow()) {
            getWindow().getEventListenersContainer().setEventHandler(eventName, value);
            return;
        }
        getEventListenersContainer().setEventHandler(eventName, value);
    }

    /**
     * Returns whether the event handler property should be set at the window level.
     *
     * @return {@code true} if the event handler should be set at window level
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
}
