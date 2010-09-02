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

import java.util.LinkedList;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * JavaScript object representing a Mouse Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-MouseEvent">DOM Level 2 Events</a>.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class MouseEvent extends UIEvent {

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

    /** The button code for IE (1: left button, 4: middle button, 2: right button). */
    private static final int[] buttonCodeToIE = {1, 4, 2};

    /** The event's screen coordinates; initially <tt>null</tt> and lazily initialized for performance reasons. */
    private Integer screenX_, screenY_;

    /** The event's client coordinates; initially <tt>null</tt> and lazily initialized for performance reasons. */
    private Integer clientX_, clientY_;

    /** The button code according to W3C (0: left button, 1: middle button, 2: right button). */
    private int button_;

    /**
     * Used to build the prototype.
     */
    public MouseEvent() {
        screenX_ = 0;
        screenY_ = 0;
        setDetail(1);
    }

    /**
     * Creates a new event instance.
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link #BUTTON_LEFT}, {@link #BUTTON_MIDDLE} or {@link #BUTTON_RIGHT}
     */
    public MouseEvent(final DomNode domNode, final String type, final boolean shiftKey,
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
     * The horizontal coordinate at which the event occurred relative to the DOM implementation's client area.
     * @return the horizontal coordinate
     */
    public int jsxGet_clientX() {
        if (clientX_ == null) {
            clientX_ = jsxGet_screenX();
        }
        return clientX_;
    }

    /**
     * The horizontal coordinate at which the event occurred relative to the origin of the screen
     * coordinate system. The value of this attribute is initialized lazily, in order to optimize
     * performance (it requires CSS parsing).
     *
     * @return the horizontal coordinate
     */
    public int jsxGet_screenX() {
        if (screenX_ == null) {
            final HTMLElement target = (HTMLElement) jsxGet_target();
            screenX_ = target.getPosX() + 10;
        }
        return screenX_;
    }

    /**
     * Returns the horizontal coordinate of the event relative to whole document..
     * @return the horizontal coordinate (currently the same as {@link #jsxGet_screenX()})
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:event.pageX">Mozilla doc</a>
     */
    public int jsxGet_pageX() {
        return jsxGet_screenX();
    }

    /**
     * The vertical coordinate at which the event occurred relative to the DOM implementation's client area.
     * @return the horizontal coordinate
     */
    public int jsxGet_clientY() {
        if (clientY_ == null) {
            clientY_ = jsxGet_screenY();
        }
        return clientY_;
    }

    /**
     * The vertical coordinate at which the event occurred relative to the origin of the screen
     * coordinate system. The value of this attribute is initialized lazily, in order to optimize
     * performance (it requires CSS parsing).
     *
     * @return the vertical coordinate
     */
    public int jsxGet_screenY() {
        if (screenY_ == null) {
            final HTMLElement target = (HTMLElement) jsxGet_target();
            screenY_ = target.getPosY() + 10;
        }
        return screenY_;
    }

    /**
     * Returns the vertical coordinate of the event relative to the whole document.
     * @return the horizontal coordinate (currently the same as {@link #jsxGet_screenY()})
     * @see <a href="http://developer.mozilla.org/en/docs/DOM:event.pageY">Mozilla doc</a>
     */
    public int jsxGet_pageY() {
        return jsxGet_screenY();
    }

    /**
     * Gets the button code.
     * @return the button code
     */
    public int jsxGet_button() {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_116)) {
            //In IE7: oncontextmenu event.button is 0
            if (jsxGet_type().equals(TYPE_CONTEXT_MENU)) {
                return 0;
            }
            else if (jsxGet_type().equals(TYPE_CLICK)) {
                return button_;
            }
            return buttonCodeToIE[button_];
        }
        return button_;
    }

    /**
     * Special for FF (old stuff from Netscape time).
     * @see <a href="http://unixpapa.com/js/mouse.html">Javascript Madness: Mouse Events</a>
     * @return the button code
     */
    public int jsxGet_which() {
        return button_ + 1;
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
    public void jsxFunction_initMouseEvent(
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
        jsxFunction_initUIEvent(type, bubbles, cancelable, view, detail);
        screenX_ = screenX;
        screenY_ = screenY;
        clientX_ = clientX;
        clientY_ = clientY;
        setCtrlKey(ctrlKey);
        setAltKey(altKey);
        setShiftKey(shiftKey);
        setMetaKey(metaKey);
        button_ = button;
        // Ignore the relatedTarget parameter; we don't support it yet.
    }

    /**
     * Returns the mouse event currently firing, or <tt>null</tt> if no mouse event is being processed.
     * @return the mouse event currently firing
     */
    @SuppressWarnings("unchecked")
    public static MouseEvent getCurrentMouseEvent() {
        final LinkedList<Event> events = (LinkedList<Event>) Context.getCurrentContext()
            .getThreadLocal(KEY_CURRENT_EVENT);
        if (events != null && !events.isEmpty() && events.getLast() instanceof MouseEvent) {
            return (MouseEvent) events.getLast();
        }
        return null;
    }

    /**
     * Returns <tt>true</tt> if the specified event type should be managed as a mouse event.
     * @param type the type of event to check
     * @return <tt>true</tt> if the specified event type should be managed as a mouse event
     */
    static boolean isMouseEvent(final String type) {
        return TYPE_CLICK.equals(type)
            || TYPE_MOUSE_OVER.equals(type)
            || TYPE_MOUSE_MOVE.equals(type)
            || TYPE_MOUSE_OUT.equals(type)
            || TYPE_MOUSE_DOWN.equals(type)
            || TYPE_MOUSE_UP.equals(type)
            || TYPE_CONTEXT_MENU.equals(type);
    }

}
