/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.GENERATED_116;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.util.LinkedList;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * JavaScript object representing a Mouse Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-MouseEvent">DOM Level 2 Events</a>.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass
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
        screenX_ = Integer.valueOf(0);
        screenY_ = Integer.valueOf(0);
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
    @JsxGetter
    public int getClientX() {
        if (clientX_ == null) {
            clientX_ = Integer.valueOf(getScreenX());
        }
        return clientX_.intValue();
    }

    /**
     * Sets the clientX value.
     * @param value the clientX value
     */
    @JsxSetter
    public void setClientX(final int value) {
        clientX_ = value;
    }

    /**
     * The horizontal coordinate at which the event occurred relative to the origin of the screen
     * coordinate system. The value of this attribute is initialized lazily, in order to optimize
     * performance (it requires CSS parsing).
     *
     * @return the horizontal coordinate
     */
    @JsxGetter
    public int getScreenX() {
        if (screenX_ == null) {
            final HTMLElement target = (HTMLElement) getTarget();
            screenX_ = Integer.valueOf(target.getPosX() + 10);
        }
        return screenX_.intValue();
    }

    /**
     * Returns the horizontal coordinate of the event relative to whole document..
     * @return the horizontal coordinate (currently the same as {@link #getScreenX()})
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/event.pageX">Mozilla doc</a>
     */
    @JsxGetter(@WebBrowser(FF))
    public int getPageX() {
        return getScreenX();
    }

    /**
     * The vertical coordinate at which the event occurred relative to the DOM implementation's client area.
     * @return the horizontal coordinate
     */
    @JsxGetter
    public int getClientY() {
        if (clientY_ == null) {
            clientY_ = Integer.valueOf(getScreenY());
        }
        return clientY_.intValue();
    }

    /**
     * Sets the clientY value.
     * @param value the clientY value
     */
    @JsxSetter
    public void setClientY(final int value) {
        clientY_ = value;
    }

    /**
     * The vertical coordinate at which the event occurred relative to the origin of the screen
     * coordinate system. The value of this attribute is initialized lazily, in order to optimize
     * performance (it requires CSS parsing).
     *
     * @return the vertical coordinate
     */
    @JsxGetter
    public int getScreenY() {
        if (screenY_ == null) {
            final HTMLElement target = (HTMLElement) getTarget();
            screenY_ = Integer.valueOf(target.getPosY() + 10);
        }
        return screenY_.intValue();
    }

    /**
     * Returns the vertical coordinate of the event relative to the whole document.
     * @return the horizontal coordinate (currently the same as {@link #getScreenY()})
     * @see <a href="https://developer.mozilla.org/en-US/docs/DOM/event.pageY">Mozilla doc</a>
     */
    @JsxGetter(@WebBrowser(FF))
    public int getPageY() {
        return getScreenY();
    }

    /**
     * Gets the button code.
     * @return the button code
     */
    @JsxGetter
    public int getButton() {
        if (getBrowserVersion().hasFeature(GENERATED_116)) {
            // In IE7: oncontextmenu event.button is 0
            if (getType().equals(TYPE_CONTEXT_MENU)) {
                return 0;
            }
            if (getType().equals(TYPE_CLICK)) {
                return button_;
            }
            return buttonCodeToIE[button_];
        }
        return button_;
    }

    /**
     * Sets the button code.
     * @param value the button code
     */
    @JsxSetter
    public void setButton(int value) {
        if (getBrowserVersion().hasFeature(GENERATED_116)
                && !TYPE_CLICK.equals(getType())) {
            switch (value) {
                case 1:
                    value = 0;
                    break;

                case 4:
                    value = 1;
                    break;

                case 2:
                    value = 2;
                    break;

                default:
            }
        }
        button_ = value;
    }

    /**
     * Special for FF (old stuff from Netscape time).
     * @see <a href="http://unixpapa.com/js/mouse.html">Javascript Madness: Mouse Events</a>
     * @return the button code
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) })
    public int getWhich() {
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
    @JsxFunction({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
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

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public boolean getAltKey() {
        return super.getAltKey();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public boolean getCtrlKey() {
        return super.getCtrlKey();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME), @WebBrowser(value = IE, minVersion = 11) })
    public boolean getShiftKey() {
        return super.getShiftKey();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    public Object getKeyCode() {
        return Undefined.instance;
    }
}
