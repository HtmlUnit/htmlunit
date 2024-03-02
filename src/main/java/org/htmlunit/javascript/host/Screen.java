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
package org.htmlunit.javascript.host;

import static org.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static org.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static org.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static org.htmlunit.javascript.configuration.SupportedBrowser.IE;

import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.event.Event;
import org.htmlunit.javascript.host.event.EventTarget;

/**
 * A JavaScript object for {@code Screen}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author cd alexndr
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535868.aspx">
 * MSDN documentation</a>
 * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref.html">Mozilla documentation</a>
 */
@JsxClass
public class Screen extends EventTarget {

    private org.htmlunit.Screen screen_;

    /**
     * Creates an instance.
     */
    public Screen() {
    }

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Creates an instance.
     * @param screen the backend
     */
    public Screen(final org.htmlunit.Screen screen) {
        screen_ = screen;
    }

    /**
     * Returns the {@code availHeight} property.
     * @return the {@code availHeight} property
     */
    @JsxGetter
    public int getAvailHeight() {
        return screen_.getAvailHeight();
    }

    /**
     * Returns the {@code availLeft} property.
     * @return the {@code availLeft} property
     */
    @JsxGetter
    public int getAvailLeft() {
        return screen_.getAvailLeft();
    }

    /**
     * Returns the {@code availTop} property.
     * @return the {@code availTop} property
     */
    @JsxGetter
    public int getAvailTop() {
        return screen_.getAvailTop();
    }

    /**
     * Returns the {@code availWidth} property.
     * @return the {@code availWidth} property
     */
    @JsxGetter
    public int getAvailWidth() {
        return screen_.getAvailWidth();
    }

    /**
     * Returns the {@code bufferDepth} property.
     * @return the {@code bufferDepth} property
     */
    @JsxGetter(IE)
    public int getBufferDepth() {
        return screen_.getBufferDepth();
    }

    /**
     * Sets the {@code bufferDepth} property.
     * @param bufferDepth the {@code bufferDepth} property
     */
    @JsxSetter(IE)
    public void setBufferDepth(final int bufferDepth) {
        screen_.setBufferDepth(bufferDepth);
    }

    /**
     * Returns the {@code colorDepth} property.
     * @return the {@code colorDepth} property
     */
    @JsxGetter
    public int getColorDepth() {
        return screen_.getColorDepth();
    }

    /**
     * Returns the {@code deviceXDPI} property.
     * @return the {@code deviceXDPI} property
     */
    @JsxGetter(IE)
    public int getDeviceXDPI() {
        return screen_.getDeviceXDPI();
    }

    /**
     * Returns the {@code deviceYDPI} property.
     * @return the {@code deviceYDPI} property
     */
    @JsxGetter(IE)
    public int getDeviceYDPI() {
        return screen_.getDeviceYDPI();
    }

    /**
     * Returns the {@code fontSmoothingEnabled} property.
     * @return the {@code fontSmoothingEnabled} property
     */
    @JsxGetter(IE)
    public boolean isFontSmoothingEnabled() {
        return screen_.isFontSmoothingEnabled();
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    @JsxGetter
    public int getHeight() {
        return screen_.getHeight();
    }

    /**
     * Returns the {@code left} property.
     * @return the {@code left} property
     */
    @JsxGetter({FF, FF_ESR})
    public int getLeft() {
        return screen_.getLeft();
    }

    /**
     * Returns the {@code logicalXDPI} property.
     * @return the {@code logicalXDPI} property
     */
    @JsxGetter(IE)
    public int getLogicalXDPI() {
        return screen_.getLogicalXDPI();
    }

    /**
     * Returns the {@code logicalYDPI} property.
     * @return the {@code logicalYDPI} property
     */
    @JsxGetter(IE)
    public int getLogicalYDPI() {
        return screen_.getLogicalXDPI();
    }

    /**
     * Returns the {@code pixelDepth} property.
     * @return the {@code pixelDepth} property
     */
    @JsxGetter
    public int getPixelDepth() {
        return screen_.getPixelDepth();
    }

    /**
     * Returns the {@code systemXDPI} property.
     * @return the {@code systemXDPI} property
     */
    @JsxGetter(IE)
    public int getSystemXDPI() {
        return screen_.getSystemXDPI();
    }

    /**
     * Returns the {@code systemYDPI} property.
     * @return the {@code systemYDPI} property
     */
    @JsxGetter(IE)
    public int getSystemYDPI() {
        return screen_.getSystemYDPI();
    }

    /**
     * Returns the {@code top} property.
     * @return the {@code top} property
     */
    @JsxGetter({FF, FF_ESR})
    public int getTop() {
        return screen_.getTop();
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @JsxGetter
    public int getWidth() {
        return screen_.getWidth();
    }

    /**
     * Returns the {@code orientation} property.
     * @return the {@code orientation} property
     */
    @JsxGetter
    public ScreenOrientation getOrientation() {
        final ScreenOrientation screenOrientation = new ScreenOrientation();
        screenOrientation.setPrototype(getPrototype(screenOrientation.getClass()));
        screenOrientation.setParentScope(getParentScope());

        return screenOrientation;
    }

    /**
     * Returns the {@code orientation} property.
     * @return the {@code orientation} property
     */
    @JsxGetter({FF, FF_ESR})
    public String getMozOrientation() {
        return "landscape-primary";
    }

    /**
     * Returns the {@code orientation} property.
     * @return the {@code orientation} property
     */
    @JsxGetter({CHROME, EDGE})
    public boolean getIsExtended() {
        return false;
    }

    /**
     * Returns the {@code onchange} event handler for this element.
     * @return the {@code onchange} event handler for this element
     */
    @JsxGetter({CHROME, EDGE})
    public Function getOnchange() {
        return getEventHandler(Event.TYPE_CHANGE);
    }

    /**
     * Setter for the {@code onchange} event handler.
     * @param change the handler
     */
    @JsxSetter({CHROME, EDGE})
    public void setOnchange(final Object change) {
        setEventHandler(Event.TYPE_CHANGE, change);
    }
}
