/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import java.util.ArrayList;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * JavaScript object representing a Mouse Event.
 * For general information on which properties and functions should be supported, see
 * <a href="http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-MouseEvent">DOM Level 2 Events</a>.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class MouseEvent extends UIEvent {

    /** Constant for {@code MOZ_SOURCE_UNKNOWN}. */
    @JsxConstant({FF, FF78})
    public static final int MOZ_SOURCE_UNKNOWN = 0;
    /** Constant for {@code MOZ_SOURCE_MOUSE}. */
    @JsxConstant({FF, FF78})
    public static final int MOZ_SOURCE_MOUSE = 1;
    /** Constant for {@code MOZ_SOURCE_PEN}. */
    @JsxConstant({FF, FF78})
    public static final int MOZ_SOURCE_PEN = 2;
    /** Constant for {@code MOZ_SOURCE_ERASER}. */
    @JsxConstant({FF, FF78})
    public static final int MOZ_SOURCE_ERASER = 3;
    /** Constant for {@code MOZ_SOURCE_CURSOR}. */
    @JsxConstant({FF, FF78})
    public static final int MOZ_SOURCE_CURSOR = 4;
    /** Constant for {@code MOZ_SOURCE_TOUCH}. */
    @JsxConstant({FF, FF78})
    public static final int MOZ_SOURCE_TOUCH = 5;
    /** Constant for {@code MOZ_SOURCE_KEYBOARD}. */
    @JsxConstant({FF, FF78})
    public static final int MOZ_SOURCE_KEYBOARD = 6;

    /** The click event type, triggered by {@code onclick} event handlers. */
    public static final String TYPE_CLICK = "click";

    /** The dblclick event type, triggered by {@code ondblclick} event handlers. */
    public static final String TYPE_DBL_CLICK = "dblclick";

    /** The mouse over event type, triggered by {@code onmouseover} event handlers. */
    public static final String TYPE_MOUSE_OVER = "mouseover";

    /** The mouse move event type, triggered by {@code onmousemove} event handlers. */
    public static final String TYPE_MOUSE_MOVE = "mousemove";

    /** The mouse out event type, triggered by {@code onmouseout} event handlers. */
    public static final String TYPE_MOUSE_OUT = "mouseout";

    /** The mouse down event type, triggered by {@code onmousedown} event handlers. */
    public static final String TYPE_MOUSE_DOWN = "mousedown";

    /** The mouse up event type, triggered by {@code onmouseup} event handlers. */
    public static final String TYPE_MOUSE_UP = "mouseup";

    /** The context menu event type, triggered by {@code oncontextmenu} event handlers. */
    public static final String TYPE_CONTEXT_MENU = "contextmenu";

    /** The code for left mouse button. */
    public static final int BUTTON_LEFT = 0;

    /** The code for middle mouse button. */
    public static final int BUTTON_MIDDLE = 1;

    /** The code for right mouse button. */
    public static final int BUTTON_RIGHT = 2;

    /** The event's screen coordinates; initially {@code null} and lazily initialized for performance reasons. */
    private Integer screenX_;
    private Integer screenY_;

    /** The event's client coordinates; initially {@code null} and lazily initialized for performance reasons. */
    private Integer clientX_;
    private Integer clientY_;

    /** The button code according to W3C (0: left button, 1: middle button, 2: right button). */
    private int button_;

    /** The buttons being depressed (if any) when the mouse event was fired. */
    private int buttons_;

    /** Switch to disable label handling if we already processing the event triggered from label processing */
    private boolean processLabelAfterBubbling_ = true;

    /** Whether or not the "meta" key was pressed during the firing of the event. */
    private boolean metaKey_;

    /**
     * Used to build the prototype.
     */
    public MouseEvent() {
        screenX_ = Integer.valueOf(0);
        screenY_ = Integer.valueOf(0);
        setDetail(1);
    }

    /**
     * JavaScript constructor.
     *
     * @param type the event type
     * @param details the event details (optional)
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    @Override
    public void jsConstructor(final String type, final ScriptableObject details) {
        super.jsConstructor(ScriptRuntime.toString(type), details);
        if (details != null && !Undefined.isUndefined(details)) {
            final Object screenX = details.get("screenX", details);
            if (NOT_FOUND != screenX) {
                screenX_ = ScriptRuntime.toInt32(screenX);
            }

            final Object screenY = details.get("screenY", details);
            if (NOT_FOUND != screenX) {
                screenY_ = ScriptRuntime.toInt32(screenY);
            }

            final Object clientX = details.get("clientX", details);
            if (NOT_FOUND != clientX) {
                clientX_ = ScriptRuntime.toInt32(clientX);
            }

            final Object clientY = details.get("clientY", details);
            if (NOT_FOUND != clientX) {
                clientY_ = ScriptRuntime.toInt32(clientY);
            }

            final Object button = details.get("button", details);
            if (NOT_FOUND != button) {
                button_ = ScriptRuntime.toInt32(button);
            }

            final Object buttons = details.get("buttons", details);
            if (NOT_FOUND != buttons) {
                buttons_ = ScriptRuntime.toInt32(buttons);
            }

            setAltKey(ScriptRuntime.toBoolean(details.get("altKey")));
            setCtrlKey(ScriptRuntime.toBoolean(details.get("ctrlKey")));
            setMetaKey(ScriptRuntime.toBoolean(details.get("metaKey")));
            setShiftKey(ScriptRuntime.toBoolean(details.get("shiftKey")));
        }
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

        if (button != BUTTON_LEFT && button != BUTTON_MIDDLE && button != BUTTON_RIGHT) {
            throw new IllegalArgumentException("Invalid button code: " + button);
        }
        button_ = button;

        if (TYPE_DBL_CLICK.equals(type) || TYPE_CONTEXT_MENU.equals(type)) {
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
    @JsxGetter
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
    @JsxGetter
    public int getPageY() {
        return getScreenY();
    }

    /**
     * Gets the button code.
     * @return the button code
     */
    @JsxGetter
    public int getButton() {
        return button_;
    }

    /**
     * Sets the button code.
     * @param value the button code
     */
    public void setButton(final int value) {
        button_ = value;
    }

    /**
     * Gets the button code.
     * @return the button code
     */
    @JsxGetter
    public int getButtons() {
        return buttons_;
    }

    /**
     * Sets the button code.
     * @param value the button code
     */
    public void setButtons(final int value) {
        buttons_ = value;
    }

    /**
     * Special for FF (old stuff from Netscape time).
     * @see <a href="http://unixpapa.com/js/mouse.html">Javascript Madness: Mouse Events</a>
     * @return the button code
     */
    @JsxGetter
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
    @JsxFunction
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
     * Returns the mouse event currently firing, or {@code null} if no mouse event is being processed.
     * @return the mouse event currently firing
     */
    @SuppressWarnings("unchecked")
    public static MouseEvent getCurrentMouseEvent() {
        final Context context = Context.getCurrentContext();
        if (context != null) {
            final ArrayList<Event> events = (ArrayList<Event>) context.getThreadLocal(KEY_CURRENT_EVENT);
            if (events != null && events.size() > 0) {
                final int lastIdx = events.size() - 1;
                final Event lastEvent = events.get(lastIdx);
                if (lastEvent instanceof MouseEvent) {
                    return (MouseEvent) lastEvent;
                }
            }
        }
        return null;
    }

    /**
     * Returns {@code true} if the specified event type should be managed as a mouse event.
     * @param type the type of event to check
     * @return {@code true} if the specified event type should be managed as a mouse event
     */
    public static boolean isMouseEvent(final String type) {
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
    @JsxGetter
    public boolean isAltKey() {
        return super.isAltKey();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public boolean isCtrlKey() {
        return super.isCtrlKey();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public boolean isShiftKey() {
        return super.isShiftKey();
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

    /**
     * Returns whether or not the "meta" key was pressed during the event firing.
     * @return whether or not the "meta" key was pressed during the event firing
     */
    @JsxGetter
    public boolean getMetaKey() {
        return metaKey_;
    }

    /**
     * @param metaKey whether Meta has been pressed during this event or not
     */
    protected void setMetaKey(final boolean metaKey) {
        metaKey_ = metaKey;
    }
}
