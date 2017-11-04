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

import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.CHROME;
import static com.gargoylesoftware.js.nashorn.internal.objects.annotations.SupportedBrowser.FF;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.js.nashorn.ScriptUtils;
import com.gargoylesoftware.js.nashorn.SimplePrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.objects.Global;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ClassConstructor;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.Function;
import com.gargoylesoftware.js.nashorn.internal.objects.annotations.ScriptClass;
import com.gargoylesoftware.js.nashorn.internal.runtime.PrototypeObject;
import com.gargoylesoftware.js.nashorn.internal.runtime.ScriptFunction;

/**
 * JavaScript object representing a Mouse Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-MouseEvent">DOM Level 2 Events</a>.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@ScriptClass
public class MouseEvent2 extends UIEvent2 {

    /** The click event type, triggered by "onclick" event handlers. */
    public static final String TYPE_CLICK = "click";

    /** The dblclick event type, triggered by "ondblclick" event handlers. */
    public static final String TYPE_DBL_CLICK = "dblclick";

    /** The mouse over event type, triggered by "onmouseover" event handlers. */
    public static final String TYPE_MOUSE_OVER = "mouseover";

    /** The mouse move event type, triggered by "onmousemove" event handlers. */
    public static final String TYPE_MOUSE_MOVE = "mousemove";

    /** The mouse out event type, triggered by "onmouseout" event handlers. */
    public static final String TYPE_MOUSE_OUT = "mouseout";

    /** The mouse down event type, triggered by "onmousedown" event handlers. */
    public static final String TYPE_MOUSE_DOWN = "mousedown";

    /** The mouse up event type, triggered by "onmouseup" event handlers. */
    public static final String TYPE_MOUSE_UP = "mouseup";

    /** The context menu event type, triggered by "oncontextmenu" event handlers. */
    public static final String TYPE_CONTEXT_MENU = "contextmenu";

    /** The code for left mouse button. */
    public static final int BUTTON_LEFT = 0;

    /** The code for middle mouse button. */
    public static final int BUTTON_MIDDLE = 1;

    /** The code for right mouse button. */
    public static final int BUTTON_RIGHT = 2;

    /** The event's screen coordinates; initially {@code null} and lazily initialized for performance reasons. */
    private Integer screenX_, screenY_;

    /** The event's client coordinates; initially {@code null} and lazily initialized for performance reasons. */
    private Integer clientX_, clientY_;

    /** The button code according to W3C (0: left button, 1: middle button, 2: right button). */
    private int button_;

    /** Switch to disable label handling if we already processing the event triggered from label processing */
    private boolean processLabelAfterBubbling_ = true;

    /**
     * Used to build the prototype.
     */
    protected MouseEvent2() {
        screenX_ = Integer.valueOf(0);
        screenY_ = Integer.valueOf(0);
        setDetail(1);
    }

    /**
     * Creates a new instance.
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link #BUTTON_LEFT}, {@link #BUTTON_MIDDLE} or {@link #BUTTON_RIGHT}
     */
    public MouseEvent2(final DomNode domNode, final String type, final boolean shiftKey,
        final boolean ctrlKey, final boolean altKey, final int button) {

        super(domNode, type);
        setShiftKey(shiftKey);
        setCtrlKey(ctrlKey);
        setAltKey(altKey);
        setMetaKey(false);

        if (button != BUTTON_LEFT && button != BUTTON_MIDDLE && button != BUTTON_RIGHT) {
            throw new IllegalArgumentException("Invalid button code: " + button);
        }
        button_ = button;

        if (TYPE_DBL_CLICK.equals(type)) {
            setDetail(2);
        }
        else {
            setDetail(1);
        }
    }

    /**
     * Constructs a new object.
     *
     * @param newObj is {@code new} used
     * @param self the {@link Global}
     * @return the created object
     */
    public static MouseEvent2 constructor(final boolean newObj, final Object self) {
        final MouseEvent2 host = new MouseEvent2();
        host.setProto(((Global) self).getPrototype(host.getClass()));
        ScriptUtils.initialize(host);
        return host;
    }

    /**
     * Implementation of the DOM Level 2 Event method for initializing the mouse event.
     *
     * @param type the event type
     * @param bubbles can the event bubble
     * @param cancelable can the event be canceled
     * @param view the view to use for this event
     * @param detail the detail to set for the event
     * @param screenX the initial value of screenX
     * @param screenY the initial value of screenY
     * @param clientX the initial value of clientX
     * @param clientY the initial value of clientY
     * @param ctrlKey is the control key pressed
     * @param altKey is the alt key pressed
     * @param shiftKey is the shift key pressed
     * @param metaKey is the meta key pressed
     * @param button what mouse button is pressed
     * @param relatedTarget is there a related target for the event
     */
    @Function
    public void initMouseEvent(
            final String type,
            final boolean bubbles,
            final boolean cancelable,
            final Object view,
            final int detail,
            final int screenX,
            final int screenY,
            final int clientX,
            final int clientY,
            final boolean ctrlKey,
            final boolean altKey,
            final boolean shiftKey,
            final boolean metaKey,
            final int button,
            final Object relatedTarget) {
        initUIEvent(type, bubbles, cancelable, view, detail);
        screenX_ = Integer.valueOf(screenX);
        screenY_ = Integer.valueOf(screenY);
        clientX_ = Integer.valueOf(clientX);
        clientY_ = Integer.valueOf(clientY);
        setCtrlKey(ctrlKey);
        setAltKey(altKey);
        setShiftKey(shiftKey);
        setMetaKey(metaKey);
        button_ = button;
        // Ignore the relatedTarget parameter; we don't support it yet.
    }

    private static MethodHandle staticHandle(final String name, final Class<?> rtype, final Class<?>... ptypes) {
        try {
            return MethodHandles.lookup().findStatic(MouseEvent2.class,
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
            super("MouseEvent",
                    staticHandle("constructor", MouseEvent2.class, boolean.class, Object.class),
                    null);
            final Prototype prototype = new Prototype();
            PrototypeObject.setConstructor(prototype, this);
            setPrototype(prototype);
        }
    }

    /** Prototype. */
    public static final class Prototype extends SimplePrototypeObject {
        Prototype() {
            super("MouseEvent");
        }
    }

    /**
     * {@inheritDoc} Overridden take care of click events.
     */
    @Override
    public boolean processLabelAfterBubbling() {
        return MouseEvent.TYPE_CLICK  == getType() && processLabelAfterBubbling_;
    }

    /**
     * Disable the lable processing if we are already processing one.
     */
    public void disableProcessLabelAfterBubbling() {
        processLabelAfterBubbling_ = false;
    }
}
