/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

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
public class Screen extends HtmlUnitScriptable {

    private com.gargoylesoftware.htmlunit.Screen screen_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public Screen() {
    }

    /**
     * Creates an instance.
     * @param screen the backend
     */
    public Screen(final com.gargoylesoftware.htmlunit.Screen screen) {
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
     * Sets the {@code availHeight} property.
     * @param availHeight the {@code availHeight} property
     */
    @JsxSetter
    public void setAvailHeight(final int availHeight) {
        screen_.setAvailHeight(availHeight);
    }

    /**
     * Returns the {@code availLeft} property.
     * @return the {@code availLeft} property
     */
    @JsxGetter({CHROME, EDGE, FF, FF_ESR})
    public int getAvailLeft() {
        return screen_.getAvailLeft();
    }

    /**
     * Sets the {@code availLeft} property.
     * @param availLeft the {@code availLeft} property
     */
    @JsxSetter({CHROME, EDGE, FF, FF_ESR})
    public void setAvailLeft(final int availLeft) {
        screen_.setAvailLeft(availLeft);
    }

    /**
     * Returns the {@code availTop} property.
     * @return the {@code availTop} property
     */
    @JsxGetter({CHROME, EDGE, FF, FF_ESR})
    public int getAvailTop() {
        return screen_.getAvailTop();
    }

    /**
     * Sets the {@code availTop} property.
     * @param availTop the {@code availTop} property
     */
    @JsxSetter({CHROME, EDGE, FF, FF_ESR})
    public void setAvailTop(final int availTop) {
        screen_.setAvailTop(availTop);
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
     * Sets the {@code availWidth} property.
     * @param availWidth the {@code availWidth} property
     */
    @JsxSetter
    public void setAvailWidth(final int availWidth) {
        screen_.setAvailWidth(availWidth);
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
     * Sets the {@code colorDepth} property.
     * @param colorDepth the {@code colorDepth} property
     */
    @JsxSetter
    public void setColorDepth(final int colorDepth) {
        screen_.setColorDepth(colorDepth);
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
     * Sets the {@code deviceXDPI} property.
     * @param deviceXDPI the {@code deviceXDPI} property
     */
    @JsxSetter(IE)
    public void setDeviceXDPI(final int deviceXDPI) {
        screen_.setDeviceXDPI(deviceXDPI);
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
     * Sets the {@code deviceYDPI} property.
     * @param deviceYDPI the {@code deviceYDPI} property
     */
    @JsxSetter(IE)
    public void setDeviceYDPI(final int deviceYDPI) {
        screen_.setDeviceYDPI(deviceYDPI);
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
     * Sets the {@code fontSmoothingEnabled} property.
     * @param fontSmoothingEnabled the {@code fontSmoothingEnabled} property
     */
    @JsxSetter(IE)
    public void setFontSmoothingEnabled(final boolean fontSmoothingEnabled) {
        screen_.setFontSmoothingEnabled(fontSmoothingEnabled);
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
     * Sets the {@code height} property.
     * @param height the {@code height} property
     */
    @JsxSetter
    public void setHeight(final int height) {
        screen_.setHeight(height);
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
     * Sets the {@code left} property.
     * @param left the {@code left} property
     */
    @JsxSetter({FF, FF_ESR})
    public void setLeft(final int left) {
        screen_.setLeft(left);
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
     * Sets the {@code logicalXDPI} property.
     * @param logicalXDPI the {@code logicalXDPI} property
     */
    @JsxSetter(IE)
    public void setLogicalXDPI(final int logicalXDPI) {
        screen_.setLogicalXDPI(logicalXDPI);
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
     * Sets the {@code logicalYDPI} property.
     * @param logicalYDPI the {@code logicalYDPI} property
     */
    @JsxSetter(IE)
    public void setLogicalYDPI(final int logicalYDPI) {
        screen_.setLogicalYDPI(logicalYDPI);
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
     * Sets the {@code pixelDepth} property.
     * @param pixelDepth the {@code pixelDepth} property
     */
    @JsxSetter
    public void setPixelDepth(final int pixelDepth) {
        screen_.setPixelDepth(pixelDepth);
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
     * Sets the {@code systemXDPI} property.
     * @param systemXDPI the {@code systemXDPI} property
     */
    @JsxSetter(IE)
    public void setSystemXDPI(final int systemXDPI) {
        screen_.setSystemXDPI(systemXDPI);
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
     * Sets the {@code systemYDPI} property.
     * @param systemYDPI the {@code systemYDPI} property
     */
    @JsxSetter(IE)
    public void setSystemYDPI(final int systemYDPI) {
        screen_.setSystemYDPI(systemYDPI);
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
     * Sets the {@code top} property.
     * @param top the {@code top} property
     */
    @JsxSetter({FF, FF_ESR})
    public void setTop(final int top) {
        screen_.setTop(top);
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
     * Sets the {@code width} property.
     * @param width the {@code width} property
     */
    @JsxSetter
    public void setWidth(final int width) {
        screen_.setWidth(width);
    }
}
