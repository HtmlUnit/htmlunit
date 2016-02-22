/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for {@code Screen}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ronald Brill
 * @author Ahmed Ashour
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535868.aspx">
 * MSDN documentation</a>
 * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref.html">Mozilla documentation</a>
 */
@JsxClass
public class Screen extends SimpleScriptable {

    private int bufferDepth_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public Screen() {
        bufferDepth_ = 0;
    }

    /**
     * Returns the {@code availHeight} property.
     * @return the {@code availHeight} property
     */
    @JsxGetter
    public int getAvailHeight() {
        return 768;
    }

    /**
     * Sets the {@code availHeight} property.
     * @param availHeight the {@code availHeight} property
     */
    @JsxSetter
    public void setAvailHeight(final int availHeight) {
        // ignore
    }

    /**
     * Returns the {@code availLeft} property.
     * @return the {@code availLeft} property
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public int getAvailLeft() {
        return 0;
    }

    /**
     * Sets the {@code availLeft} property.
     * @param availLeft the {@code availLeft} property
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public void setAvailLeft(final int availLeft) {
        // otherwise ignore
    }

    /**
     * Returns the {@code availTop} property.
     * @return the {@code availTop} property
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public int getAvailTop() {
        return 0;
    }

    /**
     * Sets the {@code availTop} property.
     * @param availTop the {@code availTop} property
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public void setAvailTop(final int availTop) {
        // ignore
    }

    /**
     * Returns the {@code availWidth} property.
     * @return the {@code availWidth} property
     */
    @JsxGetter
    public int getAvailWidth() {
        return 1024;
    }

    /**
     * Sets the {@code availWidth} property.
     * @param availWidth the {@code availWidth} property
     */
    @JsxSetter
    public void setAvailWidth(final int availWidth) {
        // ignore
    }

    /**
     * Returns the {@code bufferDepth} property.
     * @return the {@code bufferDepth} property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getBufferDepth() {
        return bufferDepth_;
    }

    /**
     * Sets the {@code bufferDepth} property.
     * @param bufferDepth the {@code bufferDepth} property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBufferDepth(final int bufferDepth) {
        // ignore
    }

    /**
     * Returns the {@code colorDepth} property.
     * @return the {@code colorDepth} property
     */
    @JsxGetter
    public int getColorDepth() {
        return 24;
    }

    /**
     * Sets the {@code colorDepth} property.
     * @param colorDepth the {@code colorDepth} property
     */
    @JsxSetter
    public void setColorDepth(final int colorDepth) {
        // ignore
    }

    /**
     * Returns the {@code deviceXDPI} property.
     * @return the {@code deviceXDPI} property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getDeviceXDPI() {
        return 96;
    }

    /**
     * Sets the {@code deviceXDPI} property.
     * @param deviceXDPI the {@code deviceXDPI} property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setDeviceXDPI(final int deviceXDPI) {
        // ignore
    }

    /**
     * Returns the {@code deviceYDPI} property.
     * @return the {@code deviceYDPI} property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getDeviceYDPI() {
        return 96;
    }

    /**
     * Sets the {@code deviceYDPI} property.
     * @param deviceYDPI the {@code deviceYDPI} property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setDeviceYDPI(final int deviceYDPI) {
        // ignore
    }

    /**
     * Returns the {@code fontSmoothingEnabled} property.
     * @return the {@code fontSmoothingEnabled} property
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getFontSmoothingEnabled() {
        return true;
    }

    /**
     * Sets the {@code fontSmoothingEnabled} property.
     * @param fontSmoothingEnabled the {@code fontSmoothingEnabled} property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setFontSmoothingEnabled(final boolean fontSmoothingEnabled) {
        // ignore
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    @JsxGetter
    public int getHeight() {
        return 768;
    }

    /**
     * Sets the {@code height} property.
     * @param height the {@code height} property
     */
    @JsxSetter
    public void setHeight(final int height) {
        // ignore
    }

    /**
     * Returns the {@code left} property.
     * @return the {@code left} property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getLeft() {
        return 0;
    }

    /**
     * Sets the {@code left} property.
     * @param left the {@code left} property
     */
    @JsxSetter(@WebBrowser(FF))
    public void setLeft(final int left) {
        // ignore
    }

    /**
     * Returns the {@code logicalXDPI} property.
     * @return the {@code logicalXDPI} property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getLogicalXDPI() {
        return 96;
    }

    /**
     * Sets the {@code logicalXDPI} property.
     * @param logicalXDPI the {@code logicalXDPI} property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLogicalXDPI(final int logicalXDPI) {
        // ignore
    }

    /**
     * Returns the {@code logicalYDPI} property.
     * @return the {@code logicalYDPI} property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getLogicalYDPI() {
        return 96;
    }

    /**
     * Sets the {@code logicalYDPI} property.
     * @param logicalYDPI the {@code logicalYDPI} property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLogicalYDPI(final int logicalYDPI) {
        // ignore
    }

    /**
     * Returns the {@code pixelDepth} property.
     * @return the {@code pixelDepth} property
     */
    @JsxGetter
    public int getPixelDepth() {
        return 24;
    }

    /**
     * Sets the {@code pixelDepth} property.
     * @param pixelDepth the {@code pixelDepth} property
     */
    @JsxSetter
    public void setPixelDepth(final int pixelDepth) {
        // ignore
    }

    /**
     * Returns the {@code systemXDPI} property.
     * @return the {@code systemXDPI} property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getSystemXDPI() {
        return 96;
    }

    /**
     * Sets the {@code systemXDPI} property.
     * @param systemXDPI the {@code systemXDPI} property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setSystemXDPI(final int systemXDPI) {
        // ignore
    }

    /**
     * Returns the {@code systemYDPI} property.
     * @return the {@code systemYDPI} property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getSystemYDPI() {
        return 96;
    }

    /**
     * Sets the {@code systemYDPI} property.
     * @param systemYDPI the {@code systemYDPI} property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setSystemYDPI(final int systemYDPI) {
        // ignore
    }

    /**
     * Returns the {@code top} property.
     * @return the {@code top} property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getTop() {
        return 0;
    }

    /**
     * Sets the {@code top} property.
     * @param top the {@code top} property
     */
    @JsxSetter(@WebBrowser(FF))
    public void setTop(final int top) {
        // ignore
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @JsxGetter
    public int getWidth() {
        return 1024;
    }

    /**
     * Sets the {@code width} property.
     * @param width the {@code width} property
     */
    @JsxSetter
    public void setWidth(final int width) {
        // ignore
    }
}
