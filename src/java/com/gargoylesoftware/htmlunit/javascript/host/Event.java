/*
 * Copyright (c) 2002-2007 Gargoyle Software Inc. All rights reserved.
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

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;

/**
 * JavaScript object representing an event that is passed into event handlers when they are
 * invoked. For general information on which properties and functions should be supported,
 * see <a href="http://developer.mozilla.org/en/docs/DOM:event">the mozilla docs</a>,
 * <a href="http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-Event">the W3C DOM
 * Level 2 Event Documentation</a> or <a href="http://msdn2.microsoft.com/en-us/library/aa703876.aspx">IE's
 * IHTMLEventObj interface</a>.
 *
 * @version $Revision$
 * @author <a href="mailto:chriseldredge@comcast.net">Chris Eldredge</a>
 * @author Mike Bowler
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Brad Murray
 * @author Ahmed Ashour
 * @author Rob Di Marco
 */
public class Event extends SimpleScriptable {

    /**
     * Key to place the event's target in the Context's scope during event processing
     * to compute node coordinates compatible with those of the event.
     */
    static final String KEY_CURRENT_EVENT = "Event#current";

    /** The submit event type, triggered by "onsubmit" event handlers. */
    public static final String TYPE_SUBMIT = "submit";

    /** The change event type, triggered by "onchange" event handlers. */
    public static final String TYPE_CHANGE = "change";

    /** The load event type, triggered by "onload" event handlers. */
    public static final String TYPE_LOAD = "load";

    /** The unload event type, triggered by "onunload" event handlers. */
    public static final String TYPE_UNLOAD = "unload";

    /** The focus event type, triggered by "onfocus" event handlers. */
    public static final String TYPE_FOCUS = "focus";

    /** The blur event type, triggered by "onblur" event handlers. */
    public static final String TYPE_BLUR = "blur";

    /** The key down event type, triggered by "onkeydown" event handlers. */
    public static final String TYPE_KEY_DOWN = "keydown";

    /** The key down event type, triggered by "onkeypress" event handlers. */
    public static final String TYPE_KEY_PRESS = "keypress";

    /** The key down event type, triggered by "onkeyup" event handlers. */
    public static final String TYPE_KEY_UP = "keyup";

    /** The submit event type, triggered by "onreset" event handlers. */
    public static final String TYPE_RESET = "reset";

    /** The beforeunload event type, triggered by "onbeforeunload" event handlers. */
    public static final String TYPE_BEFORE_UNLOAD = "beforeunload";

    /** The first event phase: the capturing phase. */
    public static final short CAPTURING_PHASE = 1;

    /** The second event phase: at the event target. */
    public static final short AT_TARGET = 2;

    /** The third (and final) event phase: the bubbling phase. */
    public static final short BUBBLING_PHASE = 3;

    private static final long serialVersionUID = 4050485607908455730L;

    private Object srcElement_;        // IE-only writeable equivalent of target.
    private Object target_;            // W3C standard read-only equivalent of srcElement.
    private Object currentTarget_;     // Changes during event capturing and bubbling.
    private String type_;              // The event type.
    private Object keyCode_;           // Key code for a keypress
    private boolean shiftKey_;         // Exposed here in IE, only in mouse events in FF.
    private boolean ctrlKey_;          // Exposed here in IE, only in mouse events in FF.
    private boolean altKey_;           // Exposed here in IE, only in mouse events in FF.
    private boolean stopPropagation_;
    private Object returnValue_;

    /**
     * The current event phase. This is a W3C standard attribute not implemented by IE. One of
     * {@link #CAPTURING_PHASE}, {@link #AT_TARGET} or {@link #BUBBLING_PHASE}.
     */
    private short eventPhase_;

    /**
     * Whether or not the event bubbles. The value of this attribute depends on the event type. To
     * determine if a certain event type bubbles, see http://www.w3.org/TR/DOM-Level-2-Events/events.html
     * Most event types do bubble, so this is true by default; event types which do not bubble should
     * overwrite this value in their constructors.
     */
    private boolean bubbles_ = true;

    /**
     * Whether or not the event can be canceled. The value of this attribute depends on the event type. To
     * determine if a certain event type can be canceled, see http://www.w3.org/TR/DOM-Level-2-Events/events.html
     * The more common event types are cancelable, so this is true by default; event types which cannot be
     * canceled should overwrite this value in their constructors.
     */
    private boolean cancelable_ = true;

    /**
     * The time at which the event was created.
     */
    private long timeStamp_ = System.currentTimeMillis();

    /**
     * Creates a new event instance.
     * @param domNode The DOM node that triggered the event.
     * @param type The event type.
     */
    public Event(final DomNode domNode, final String type) {
        this(domNode, type, false, false, false);
    }

    /**
     * Creates a new event instance.
     * @param domNode The DOM node that triggered the event.
     * @param type The event type.
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     */
    public Event(final DomNode domNode, final String type,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        final Object target = domNode.getScriptObject();
        srcElement_ = target;
        target_ = target;
        currentTarget_ = target;
        type_ = type;
        shiftKey_ = shiftKey;
        ctrlKey_ = ctrlKey;
        altKey_ = altKey;
        keyCode_ = Context.getUndefinedValue();
        setParentScope((SimpleScriptable) target);
        setPrototype(getPrototype(getClass()));
        setDomNode(domNode, false);
    }

    /**
     * Creates a new event instance for a keypress event.
     * @param domNode the DOM node that triggered the event.
     * @param type The event type.
     * @param keyCode The key code associated with the event.
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     */
    public Event(final DomNode domNode, final String type, final int keyCode,
            final boolean shiftKey, final boolean ctrlKey, final boolean altKey) {
        this(domNode, type, shiftKey, ctrlKey, altKey);
        keyCode_ = new Integer(keyCode);
    }

    /**
     * Used to build the prototype
     */
    public Event() {
    }

    /**
     * Called when the event starts being fired
     */
    void startFire() {
        Context.getCurrentContext().putThreadLocal(KEY_CURRENT_EVENT, this);
    }

    /**
     * Called when the event starts being fired
     */
    void endFire() {
        Context.getCurrentContext().removeThreadLocal(KEY_CURRENT_EVENT);
    }
    
    /**
     * Returns the object that fired the event. This is an IE-only property.
     * @return The object that fired the event.
     */
    public Object jsxGet_srcElement() {
        return srcElement_;
    }

    /**
     * Sets the object that fired the event. This is an IE-only property.
     * @param srcElement The object that fired the event.
     */
    public void jsxSet_srcElement(final Object srcElement) {
        srcElement_ = srcElement;
    }

    /**
     * Returns the event target to which the event was originally dispatched.
     * @return The event target to which the event was originally dispatched.
     */
    public Object jsxGet_target() {
        return target_;
    }

    /**
     * Returns the event target whose event listeners are currently being processed. This
     * is useful during event capturing and event bubbling.
     * @return The current event target.
     */
    public Object jsxGet_currentTarget() {
        return currentTarget_;
    }

    /**
     * Sets the current target
     * @param target the new value
     */
    public void setCurrentTarget(final Scriptable target) {
        currentTarget_ = target;
    }

    /**
     * Returns the event type.
     * @return The event type.
     */
    public String jsxGet_type() {
        return type_;
    }

    /**
     * Sets the event type.
     * @param eventType The event type.
     */
    public void setEventType(final String eventType) {
        type_ = eventType;
    }

    /**
     * Returns the time at which this event was created.
     * @return the time at which this event was created
     */
    public long jsxGet_timeStamp() {
        return timeStamp_;
    }

    /**
     * Returns the key code associated with the event.
     * @return The key code associated with the event.
     */
    public Object jsxGet_keyCode() {
        return keyCode_;
    }

    /**
     * @return whether SHIFT has been pressed during this event or not.
     */
    public boolean jsxGet_shiftKey() {
        return shiftKey_;
    }

    /**
     * @return whether CTRL has been pressed during this event or not.
     */
    public boolean jsxGet_ctrlKey() {
        return ctrlKey_;
    }

    /**
     * @return whether ALT has been pressed during this event or not.
     */
    public boolean jsxGet_altKey() {
        return altKey_;
    }

    /**
     * @return the current event phase for the event.
     */
    public int jsxGet_eventPhase() {
        return eventPhase_;
    }

    /**
     * Sets the current event phase. Must be one of {@link #CAPTURING_PHASE}, {@link #AT_TARGET} or
     * {@link #BUBBLING_PHASE}.
     *
     * @param phase the phase the event is in
     */
    public void setEventPhase(final short phase) {
        if (phase != CAPTURING_PHASE && phase != AT_TARGET && phase != BUBBLING_PHASE) {
            throw new IllegalArgumentException("Illegal phase specified: " + phase);
        }
        eventPhase_ = phase;
    }

    /**
     * @return whether or not this event bubbles.
     */
    public boolean jsxGet_bubbles() {
        return bubbles_;
    }

    /**
     * @return whether or not this event can be canceled.
     */
    public boolean jsxGet_cancelable() {
        return cancelable_;
    }

    /**
     * @return indicates if event propagation is stopped.
     */
    public boolean jsxGet_cancelBubble() {
        return stopPropagation_;
    }

    /**
     * @param newValue indicates if event propagation is stopped.
     */
    public void jsxSet_cancelBubble(final boolean newValue) {
        stopPropagation_ = newValue;
    }

    /**
     * Stops the event from propagating.
     */
    public void jsxFunction_stopPropagation() {
        stopPropagation_ = true;
    }
    
    /**
     * Indicates if event propagation is stopped.
     * @return the status
     */
    public boolean isPropagationStopped() {
        return stopPropagation_;
    }

    /**
     * Returns the return value associated with the event.
     * @return The return value associated with the event.
     */
    public Object jsxGet_returnValue() {
        return returnValue_;
    }
    
    /**
     * Sets the return value associated with the event.
     * @param returnValue The return value associated with the event.
     */
    public void jsxSet_returnValue(final Object returnValue) {
        returnValue_ = returnValue;
    }

    /**
     * Initializes this event.
     * @param type the event type
     * @param bubbles whether or not the event should bubble
     * @param cancelable whether or not the event the event should be cancelable
     */
    public void jsxFunction_initEvent(final String type, final boolean bubbles, final boolean cancelable) {
        type_ = type;
        bubbles_ = bubbles;
        cancelable_ = cancelable;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        final StringBuffer buffer = new StringBuffer("Event ");
        buffer.append(jsxGet_type());
        buffer.append(" (");
        buffer.append("Current Target: ");
        buffer.append(currentTarget_);
        buffer.append(");");
        return buffer.toString();
    }

}
