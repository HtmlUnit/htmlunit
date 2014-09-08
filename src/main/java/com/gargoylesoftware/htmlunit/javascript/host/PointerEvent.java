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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * JavaScript object representing the PointerEvent.
 * @see <a href="http://www.w3.org/TR/pointerevents/">W3C Spec</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ie/hh772103.aspx">MSDN</a>
 *
 * @version $Revision$
 * @author Frank Danek
 */
@JsxClass(browsers = { @WebBrowser(value = IE, minVersion = 11) })
public class PointerEvent extends MouseEvent {

    private int pointerId_;
    private int width_;
    private int height_;
    private double pressure_;
    private int tiltX_;
    private int tiltY_;
    private String pointerType_ = "";
    private boolean isPrimary_;

    /**
     * Creates a new event instance.
     */
    public PointerEvent() {
    }

    /**
     * Creates a new event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param button the button code, must be {@link #BUTTON_LEFT}, {@link #BUTTON_MIDDLE} or {@link #BUTTON_RIGHT}
     */
    public PointerEvent(final DomNode domNode, final String type, final boolean shiftKey,
            final boolean ctrlKey, final boolean altKey, final int button) {
        super(domNode, type, shiftKey, ctrlKey, altKey, button);
        setDetail(0);

        pointerId_ = 1;
        width_ = 1;
        height_ = 1;
        pointerType_ = "mouse";
        isPrimary_ = true;
    }

    /**
     * Used for initializing the pointer event.
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
     * @param offsetX the initial value of offsetX
     * @param offsetY the initial value of offsetY
     * @param width the initial value of width
     * @param height the initial value of height
     * @param pressure the initial value of pressure
     * @param rotation the initial value of rotation
     * @param tiltX the initial value of tiltX
     * @param tiltY the initial value of tiltY
     * @param pointerId the pointerId
     * @param pointerType the pointer type
     * @param hwTimestamp the initial value of hwTimestamp
     * @param isPrimary the initial value of isPrimary
     */
    @JsxFunction
    public void initPointerEvent(final String type,
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
            final Object relatedTarget,
            final int offsetX,
            final int offsetY,
            final int width,
            final int height,
            final Double pressure,
            final int rotation,
            final int tiltX,
            final int tiltY,
            final int pointerId,
            final String pointerType,
            final int hwTimestamp,
            final boolean isPrimary) {
        super.initMouseEvent(type, bubbles, cancelable, view, detail, screenX, screenY, clientX, clientY, ctrlKey,
            altKey, shiftKey, metaKey, button, relatedTarget);
        width_ = width;
        height_ = height;
        pressure_ = pressure.doubleValue();
        tiltX_ = tiltX;
        tiltY_ = tiltY;
        pointerId_ = pointerId;
        pointerType_ = pointerType;
        isPrimary_ = isPrimary;
    }

    /**
     * @return the pointerId
     */
    @JsxGetter
    public long getPointerId() {
        return pointerId_;
    }

    /**
     * @return the width
     */
    @JsxGetter
    public long getWidth() {
        return width_;
    }

    /**
     * @return the height
     */
    @JsxGetter
    public long getHeight() {
        return height_;
    }

    /**
     * @return the pressure
     */
    @JsxGetter
    public double getPressure() {
        return pressure_;
    }

    /**
     * @return the tiltX
     */
    @JsxGetter
    public long getTiltX() {
        return tiltX_;
    }

    /**
     * @return the tiltY
     */
    @JsxGetter
    public long getTiltY() {
        return tiltY_;
    }

    /**
     * @return the pointerType
     */
    @JsxGetter
    public String getPointerType() {
        return pointerType_;
    }

    /**
     * @return the isPrimary
     */
    @JsxGetter(propertyName = "isPrimary")
    public boolean isPrimary() {
        return isPrimary_;
    }
}
