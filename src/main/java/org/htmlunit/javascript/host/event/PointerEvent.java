/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativeObject;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.html.DomNode;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * JavaScript object representing a {@code PointerEvent}.
 * @see <a href="http://www.w3.org/TR/pointerevents/">W3C Spec</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ie/hh772103.aspx">MSDN</a>
 *
 * @author Frank Danek
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
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
     * Default constructor.
     */
    public PointerEvent() {
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        final PointerEvent event = new PointerEvent();
        if (args.length != 0) {
            event.setType(JavaScriptEngine.toString(args[0]));
            event.setBubbles(false);
            event.setCancelable(false);
            event.width_ = 1;
            event.height_ = 1;
        }

        if (args.length > 1) {
            final NativeObject object = (NativeObject) args[1];
            event.setBubbles((boolean) getValue(object, "bubbles", event.isBubbles()));
            event.pointerId_ = (int) getValue(object, "pointerId", event.pointerId_);
            event.width_ = (int) getValue(object, "width", event.width_);
            event.height_ = (int) getValue(object, "height", event.height_);
            event.pressure_ = (double) getValue(object, "pressure", event.pressure_);
            event.tiltX_ = (int) getValue(object, "tiltX", event.tiltX_);
            event.tiltY_ = (int) getValue(object, "tiltY", event.tiltY_);
            event.pointerType_ = (String) getValue(object, "pointerType", event.pointerType_);
            event.isPrimary_ = (boolean) getValue(object, "isPrimary", event.isPrimary_);
        }
        return event;
    }

    private static Object getValue(final ScriptableObject object, final String name, final Object defaulValue) {
        Object value = object.get(name);
        if (value == null) {
            value = defaulValue;
        }
        else {
            if (defaulValue instanceof String) {
                value = String.valueOf(value);
            }
            else if (defaulValue instanceof Double) {
                value = JavaScriptEngine.toNumber(value);
            }
            else if (defaulValue instanceof Number) {
                value = (int) JavaScriptEngine.toNumber(value);
            }
            else {
                value = JavaScriptEngine.toBoolean(value);
            }
        }
        return value;
    }

    /**
     * Creates a new event instance.
     *
     * @param domNode the DOM node that triggered the event
     * @param type the event type
     * @param shiftKey true if SHIFT is pressed
     * @param ctrlKey true if CTRL is pressed
     * @param altKey true if ALT is pressed
     * @param detail the detail value
     * @param button the button code, must be {@link #BUTTON_LEFT}, {@link #BUTTON_MIDDLE} or {@link #BUTTON_RIGHT}
     */
    public PointerEvent(final DomNode domNode, final String type, final boolean shiftKey,
            final boolean ctrlKey, final boolean altKey, final int button, final int detail) {
        super(domNode, type, shiftKey, ctrlKey, altKey, button, detail);

        pointerId_ = 1;
        width_ = 1;
        height_ = 1;
        pointerType_ = "mouse";
        isPrimary_ = true;
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
    public boolean isPrimary_js() {
        return isPrimary_;
    }

    /**
     * @return the pointerType
     */
    @JsxGetter({CHROME, EDGE})
    @SuppressWarnings("PMD.UseUnderscoresInNumericLiterals")
    public double getAltitudeAngle() {
        return 1.5707963267948966;
    }

    /**
     * @return the pointerType
     */
    @JsxGetter({CHROME, EDGE})
    public double getAzimuthAngle() {
        return 0d;
    }
}
