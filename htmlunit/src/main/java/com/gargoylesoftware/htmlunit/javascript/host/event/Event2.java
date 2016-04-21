/*
 * Copyright (c) 2016 Gargoyle Software Inc.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (http://www.gnu.org/licenses/).
 */
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONLOAD_CANCELABLE_FALSE;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptObject;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Getter;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Setter;
import com.gargoylesoftware.js.nashorn.internal.runtime.Context;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptObject;

public class Event2 extends SimpleScriptObject {

    /**
     * Key to place the event's target in the Context's scope during event processing
     * to compute node coordinates compatible with those of the event.
     */
    protected static final String KEY_CURRENT_EVENT = "Event#current";

    /** The submit event type, triggered by "onsubmit" event handlers. */
    public static final String TYPE_SUBMIT = "submit";

    /** The change event type, triggered by "onchange" event handlers. */
    public static final String TYPE_CHANGE = "change";

    /** The load event type, triggered by "onload" event handlers. */
    public static final String TYPE_LOAD = "load";

    /** The unload event type, triggered by "onunload" event handlers. */
    public static final String TYPE_UNLOAD = "unload";

    /** The popstate event type, triggered by "onpopstate" event handlers. */
    public static final String TYPE_POPSTATE = "popstate";

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

    private Object srcElement_;        // IE-only writable equivalent of target.
    private Object target_;            // W3C standard read-only equivalent of srcElement.
    private ScriptObject currentTarget_; // Changes during event capturing and bubbling.
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

    public static Event2 constructor(final boolean newObj, final Object self) {
        final Event2 host = new Event2();
        host.setProto(Context.getGlobal().getPrototype(host.getClass()));
        return host;
    }

    public Event2() {
    }

    /**
     * Creates a new event instance.
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     */
    public Event2(final DomNode domNode, final String type) {
        this((SimpleScriptObject) domNode.getScriptObject2(), type);
        setDomNode(domNode, false);
    }

    /**
     * Creates a new event instance.
     * @param scriptable the SimpleScriptable that triggered the event
     * @param type the event type
     */
    public Event2(final SimpleScriptObject scriptable, final String type) {
        srcElement_ = scriptable;
        target_ = scriptable;
        currentTarget_ = scriptable;
        type_ = type;
//        setParentScope(scriptable);
//        setPrototype(getPrototype(getClass()));

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

    @Getter
    public static String getType(final Object self) {
        return ((Event2) self).type_;
    }

    @Setter
    public static void settype(final Object self, final String value) {
        ((Event2) self).type_ = value;
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(Event2.class,
                    name, MethodType.methodType(rtype, ptypes));
        }
        catch (final ReflectiveOperationException e) {
            throw new IllegalStateException(e);
        }
    }

    public static final class FunctionConstructor extends ScriptFunction {
        public FunctionConstructor() {
            super("Event", 
                    staticHandle("constructor", Event2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    static final class Prototype extends PrototypeObject {
        public String getClassName() {
            return "Event";
        }
    }
}
