/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_BUBBLES_AND_CANCELABLE_DEFAULT_FALSE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_FOCUS_DOCUMENT_DESCENDANTS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONLOAD_CANCELABLE_FALSE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_EVENT_ABORTED_BY_RETURN_VALUE_FALSE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_EVENT_KEY_CODE_UNDEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.LinkedList;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlArea;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.SubmittableElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * JavaScript object representing an event that is passed into event handlers when they are
 * invoked. For general information on which properties and functions should be supported,
 * see <a href="https://developer.mozilla.org/en-US/docs/DOM/event">the mozilla docs</a>,
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
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass
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

    /** The focus in event type, triggered by "onfocusin" event handlers. */
    public static final String TYPE_FOCUS_IN = "focusin";

    /** The focus out event type, triggered by "onfocusout" event handlers. */
    public static final String TYPE_FOCUS_OUT = "focusout";

    /** The blur event type, triggered by "onblur" event handlers. */
    public static final String TYPE_BLUR = "blur";

    /** The key down event type, triggered by "onkeydown" event handlers. */
    public static final String TYPE_KEY_DOWN = "keydown";

    /** The key down event type, triggered by "onkeypress" event handlers. */
    public static final String TYPE_KEY_PRESS = "keypress";

    /** The input event type, triggered by "oninput" event handlers. */
    public static final String TYPE_INPUT = "input";

    /** The key down event type, triggered by "onkeyup" event handlers. */
    public static final String TYPE_KEY_UP = "keyup";

    /** The submit event type, triggered by "onreset" event handlers. */
    public static final String TYPE_RESET = "reset";

    /** The beforeunload event type, triggered by "onbeforeunload" event handlers. */
    public static final String TYPE_BEFORE_UNLOAD = "beforeunload";

    /** Triggered after the DOM has loaded but before images etc. */
    public static final String TYPE_DOM_DOCUMENT_LOADED = "DOMContentLoaded";

    /** The event type triggered by "onpropertychange" event handlers. */
    public static final String TYPE_PROPERTY_CHANGE = "propertychange";

    /** The event type triggered by "onhashchange" event handlers. */
    public static final String TYPE_HASH_CHANGE = "hashchange";

    /** The event type triggered by "onreadystatechange" event handlers. */
    public static final String TYPE_READY_STATE_CHANGE = "readystatechange";

    /** The event type triggered by "onerror" event handlers. */
    public static final String TYPE_ERROR = "error";

    /** The message event type, triggered by postMessage. */
    public static final String TYPE_MESSAGE = "message";

    /** No event phase. */
    @JsxConstant(@WebBrowser(FF))
    public static final short NONE = 0;

    /** The first event phase: the capturing phase. */
    @JsxConstant({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static final short CAPTURING_PHASE = 1;

    /** The second event phase: at the event target. */
    @JsxConstant({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static final short AT_TARGET = 2;

    /** The third (and final) event phase: the bubbling phase. */
    @JsxConstant({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public static final short BUBBLING_PHASE = 3;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int ABORT = 0x400000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int BACK = 0x20000000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int BLUR = 0x2000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int CHANGE = 0x8000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int CLICK = 0x40;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DBLCLICK = 0x80;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int DRAGDROP = 0x800;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int ERROR = 0x800000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int FOCUS = 0x1000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int FORWARD = 0x8000000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int HELP = 0x10000000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int KEYDOWN = 0x100;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int KEYPRESS = 0x400;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int KEYUP = 0x200;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int LOAD = 0x80000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int LOCATE = 0x1000000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOUSEDOWN = 0x1;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOUSEDRAG = 0x20;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOUSEMOVE = 0x10;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOUSEOUT = 0x8;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOUSEOVER = 0x4;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOUSEUP = 0x2;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int MOVE = 0x2000000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int RESET = 0x10000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int RESIZE = 0x4000000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int SCROLL = 0x40000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int SELECT = 0x4000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int SUBMIT = 0x20000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int TEXT = 0x40000000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int UNLOAD = 0x100000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int XFER_DONE = 0x200000;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int ALT_MASK = 0x1;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int CONTROL_MASK = 0x2;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int SHIFT_MASK = 0x4;

    /** Constant. */
    @JsxConstant(@WebBrowser(FF))
    public static final int META_MASK = 0x8;

    private Object srcElement_;        // IE-only writable equivalent of target.
    private Object target_;            // W3C standard read-only equivalent of srcElement.
    private Object currentTarget_;     // Changes during event capturing and bubbling.
    private String type_ = "";         // The event type.
    private Object keyCode_;           // Key code for a keypress
    private boolean shiftKey_;         // Exposed here in IE, only in mouse events in FF.
    private boolean ctrlKey_;          // Exposed here in IE, only in mouse events in FF.
    private boolean altKey_;           // Exposed here in IE, only in mouse events in FF.
    private String propertyName_;
    private boolean stopPropagation_;
    private Object returnValue_;
    private boolean preventDefault_;

    /**
     * The current event phase. This is a W3C standard attribute. One of {@link #NONE},
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
    private final long timeStamp_ = System.currentTimeMillis();

    /**
     * Creates a new event instance.
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     */
    public Event(final DomNode domNode, final String type) {
        this((SimpleScriptable) domNode.getScriptObject(), type);
        setDomNode(domNode, false);
    }

    /**
     * Creates a new event instance.
     * @param scriptable the SimpleScriptable that triggered the event
     * @param type the event type
     */
    public Event(final SimpleScriptable scriptable, final String type) {
        srcElement_ = scriptable;
        target_ = scriptable;
        currentTarget_ = scriptable;
        type_ = type;
        setParentScope(scriptable);
        setPrototype(getPrototype(getClass()));

        if (TYPE_CHANGE.equals(type)) {
            cancelable_ = false;
        }
        else if (TYPE_LOAD.equals(type)) {
            bubbles_ = false;
            if (getBrowserVersion().hasFeature(EVENT_ONLOAD_CANCELABLE_FALSE)) {
                cancelable_ = false;
            }
        }
    }

    /**
     * Creates a new Event with {@link #TYPE_PROPERTY_CHANGE} type.
     * @param domNode the DOM node that triggered the event
     * @param propertyName the property name that was changed
     * @return the new Event object
     */
    public static Event createPropertyChangeEvent(final DomNode domNode, final String propertyName) {
        final Event event = new Event(domNode, TYPE_PROPERTY_CHANGE);
        event.propertyName_ = propertyName;
        return event;
    }

    /**
     * Used to build the prototype.
     */
    public Event() {
        // Empty.
    }

    /**
     * Called whenever an event is created using <code>Document.createEvent(..)</code>.
     * This method is called after the parent scope was set so you are able to access the browser version.
     */
    public void eventCreated() {
        if (getBrowserVersion().hasFeature(EVENT_BUBBLES_AND_CANCELABLE_DEFAULT_FALSE)) {
            setBubbles(false);
            setCancelable(false);
        }
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public void jsConstructor(final String type, final ScriptableObject details) {
        boolean bubbles = false;
        boolean cancelable = false;

        if (details != null && !Context.getUndefinedValue().equals(details)) {
            final Boolean detailBubbles = (Boolean) details.get("bubbles");
            if (detailBubbles != null) {
                bubbles = detailBubbles.booleanValue();
            }

            final Boolean detailCancelable = (Boolean) details.get("cancelable");
            if (detailCancelable != null) {
                cancelable = detailCancelable.booleanValue();
            }
        }
        initEvent(type, bubbles, cancelable);
    }

    /**
     * Called when the event starts being fired
     */
    @SuppressWarnings("unchecked")
    void startFire() {
        LinkedList<Event> events = (LinkedList<Event>) Context.getCurrentContext().getThreadLocal(KEY_CURRENT_EVENT);
        if (events == null) {
            events = new LinkedList<Event>();
            Context.getCurrentContext().putThreadLocal(KEY_CURRENT_EVENT, events);
        }
        events.add(this);
    }

    /**
     * Called when the event starts being fired
     */
    @SuppressWarnings("unchecked")
    void endFire() {
        ((LinkedList<Event>) Context.getCurrentContext().getThreadLocal(KEY_CURRENT_EVENT)).removeLast();
    }

    /**
     * Returns the object that fired the event. This is an IE-only property.
     * @return the object that fired the event
     */
    @JsxGetter({ @WebBrowser(IE), @WebBrowser(CHROME) })
    public Object getSrcElement() {
        return srcElement_;
    }

    /**
     * Sets the object that fired the event. This is an IE-only property.
     * @param srcElement the object that fired the event
     */
    @JsxSetter(@WebBrowser(IE))
    public void setSrcElement(final Object srcElement) {
        srcElement_ = srcElement;
    }

    /**
     * Returns the event target to which the event was originally dispatched.
     * @return the event target to which the event was originally dispatched
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11), @WebBrowser(CHROME) })
    public Object getTarget() {
        return target_;
    }

    /**
     * Sets the event target.
     * @param target the event target
     */
    public void setTarget(final Object target) {
        target_ = target;
    }

    /**
     * Returns the event target whose event listeners are currently being processed. This
     * is useful during event capturing and event bubbling.
     * @return the current event target
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11), @WebBrowser(CHROME) })
    public Object getCurrentTarget() {
        return currentTarget_;
    }

    /**
     * Sets the current target.
     * @param target the new value
     */
    public void setCurrentTarget(final Scriptable target) {
        currentTarget_ = target;
    }

    /**
     * Returns the event type.
     * @return the event type
     */
    @JsxGetter
    public String getType() {
        return type_;
    }

    /**
     * Sets the event type.
     * @param type the event type
     */
    @JsxSetter
    public void setType(final String type) {
        type_ = type;
    }

    /**
     * Sets the event type.
     * @param eventType the event type
     */
    public void setEventType(final String eventType) {
        type_ = eventType;
    }

    /**
     * Returns the time at which this event was created.
     * @return the time at which this event was created
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11), @WebBrowser(CHROME) })
    public long getTimeStamp() {
        return timeStamp_;
    }

    /**
     * Sets the key code.
     * @param keyCode the virtual key code value of the key which was depressed, otherwise zero
     */
    protected void setKeyCode(final Object keyCode) {
        keyCode_ = keyCode;
    }

    /**
     * Returns the key code associated with the event.
     * @return the key code associated with the event
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 9))
    public Object getKeyCode() {
        if (keyCode_ == null) {
            if (getBrowserVersion().hasFeature(JS_EVENT_KEY_CODE_UNDEFINED)) {
                return Undefined.instance;
            }
            return Integer.valueOf(0);
        }
        return keyCode_;
    }

    /**
     * @return whether SHIFT has been pressed during this event or not
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 9))
    public boolean getShiftKey() {
        return shiftKey_;
    }

    /**
     * @param shiftKey whether SHIFT has been pressed during this event or not
     */
    protected void setShiftKey(final boolean shiftKey) {
        shiftKey_ = shiftKey;
    }

    /**
     * @return whether CTRL has been pressed during this event or not
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 9))
    public boolean getCtrlKey() {
        return ctrlKey_;
    }

    /**
     * @param ctrlKey whether CTRL has been pressed during this event or not
     */
    protected void setCtrlKey(final boolean ctrlKey) {
        ctrlKey_ = ctrlKey;
    }

    /**
     * @return whether ALT has been pressed during this event or not
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 9))
    public boolean getAltKey() {
        return altKey_;
    }

    /**
     * @param altKey whether ALT has been pressed during this event or not
     */
    protected void setAltKey(final boolean altKey) {
        altKey_ = altKey;
    }

    /**
     * @return the current event phase for the event
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getEventPhase() {
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
     * @return whether or not this event bubbles
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public boolean getBubbles() {
        return bubbles_;
    }

    /**
     * @param bubbles the bubbles to set
     */
    protected void setBubbles(final boolean bubbles) {
        bubbles_ = bubbles;
    }

    /**
     * @return whether or not this event can be canceled
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public boolean getCancelable() {
        return cancelable_;
    }

    /**
     * @param cancelable the cancelable to set
     */
    protected void setCancelable(final boolean cancelable) {
        cancelable_ = cancelable;
    }

    /**
     * Returns <tt>true</tt> if both <tt>cancelable</tt> is <tt>true</tt> and <tt>preventDefault()</tt> has been
     * called for this event. Otherwise this attribute must return <tt>false</tt>.
     * @return <tt>true</tt> if this event has been cancelled or not
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public boolean getDefaultPrevented() {
        return cancelable_ && preventDefault_;
    }

    /**
     * @return indicates if event propagation is stopped
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getCancelBubble() {
        return stopPropagation_;
    }

    /**
     * @param newValue indicates if event propagation is stopped
     */
    @JsxSetter(@WebBrowser(IE))
    public void setCancelBubble(final boolean newValue) {
        stopPropagation_ = newValue;
    }

    /**
     * Stops the event from propagating.
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void stopPropagation() {
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
     * @return the return value associated with the event
     */
    @JsxGetter({ @WebBrowser(CHROME), @WebBrowser(value = IE, maxVersion = 9) })
    public Object getReturnValue() {
        return returnValue_;
    }

    /**
     * Sets the return value associated with the event.
     * @param returnValue the return value associated with the event
     */
    @JsxSetter({ @WebBrowser(CHROME), @WebBrowser(value = IE, maxVersion = 9) })
    public void setReturnValue(final Object returnValue) {
        returnValue_ = returnValue;
    }

    /**
     * Returns the property name associated with the event.
     * @return the property name associated with the event
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 9))
    public String getPropertyName() {
        return propertyName_;
    }

    /**
     * Initializes this event.
     * @param type the event type
     * @param bubbles whether or not the event should bubble
     * @param cancelable whether or not the event the event should be cancelable
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11), @WebBrowser(CHROME) })
    public void initEvent(final String type, final boolean bubbles, final boolean cancelable) {
        type_ = type;
        bubbles_ = bubbles;
        cancelable_ = cancelable;
    }

    /**
     * If, during any stage of event flow, this method is called the event is canceled.
     * Any default action associated with the event will not occur.
     * Calling this method for a non-cancelable event has no effect.
     */
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public void preventDefault() {
        preventDefault_ = true;
    }

    /**
     * Returns <tt>true</tt> if this event has been aborted via <tt>preventDefault()</tt> in
     * standards-compliant browsers, or via the event's <tt>returnValue</tt> property in IE, or
     * by the event handler returning <tt>false</tt>.
     *
     * @param result the event handler result (if <tt>false</tt>, the event is considered aborted)
     * @return <tt>true</tt> if this event has been aborted
     */
    public boolean isAborted(final ScriptResult result) {
        final boolean checkReturnValue = getBrowserVersion().hasFeature(JS_EVENT_ABORTED_BY_RETURN_VALUE_FALSE);
        return ScriptResult.isFalse(result)
                || (!checkReturnValue && preventDefault_)
                || (checkReturnValue && Boolean.FALSE.equals(returnValue_));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder buffer = new StringBuilder("Event ");
        buffer.append(getType());
        buffer.append(" (");
        buffer.append("Current Target: ");
        buffer.append(currentTarget_);
        buffer.append(");");
        return buffer.toString();
    }

    /**
     * Indicates if the current event can be applied to the provided node.
     * TODO: investigate systematically ALL nodes and ALL events!
     * @param node the node to test
     * @return <code>false</code> if the event can't be applied
     */
    public boolean applies(final DomNode node) {
        if (TYPE_BLUR.equals(getType()) || TYPE_FOCUS.equals(getType())) {
            if (getBrowserVersion().hasFeature(EVENT_FOCUS_DOCUMENT_DESCENDANTS) && node instanceof HtmlPage) {
                return true;
            }
            return node instanceof SubmittableElement || node instanceof HtmlAnchor
                || node instanceof HtmlArea;
        }
        return true;
    }

}
