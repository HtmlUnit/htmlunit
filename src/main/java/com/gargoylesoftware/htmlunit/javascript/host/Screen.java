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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_SCREEN_SETTER_THROWS_ERROR;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A JavaScript object for a Screen. Combines properties from both Mozilla's DOM
 * and IE's DOM.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ronald Brill
 *
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms535868.aspx">
 * MSDN documentation</a>
 * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_window_ref.html">Mozilla documentation</a>
 */
@JsxClass
public class Screen extends SimpleScriptable {
    private int bufferDepth_;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public Screen() {
        bufferDepth_ = 0;
    }

    /**
     * Returns the <tt>availHeight</tt> property.
     * @return the <tt>availHeight</tt> property
     */
    @JsxGetter
    public int getAvailHeight() {
        return 768;
    }

    /**
     * Sets the <tt>availHeight</tt> property.
     * @param availHeight the <tt>availHeight</tt> property
     */
    @JsxSetter
    public void setAvailHeight(final int availHeight) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.availHeight is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>availLeft</tt> property.
     * @return the <tt>availLeft</tt> property
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public int getAvailLeft() {
        return 0;
    }

    /**
     * Sets the <tt>availLeft</tt> property.
     * @param availLeft the <tt>availLeft</tt> property
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public void setAvailLeft(final int availLeft) {
        // otherwise ignore
    }

    /**
     * Returns the <tt>availTop</tt> property.
     * @return the <tt>availTop</tt> property
     */
    @JsxGetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public int getAvailTop() {
        return 0;
    }

    /**
     * Sets the <tt>availTop</tt> property.
     * @param availTop the <tt>availTop</tt> property
     */
    @JsxSetter({ @WebBrowser(FF), @WebBrowser(CHROME) })
    public void setAvailTop(final int availTop) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.availTop is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>availWidth</tt> property.
     * @return the <tt>availWidth</tt> property
     */
    @JsxGetter
    public int getAvailWidth() {
        return 1024;
    }

    /**
     * Sets the <tt>availWidth</tt> property.
     * @param availWidth the <tt>availWidth</tt> property
     */
    @JsxSetter
    public void setAvailWidth(final int availWidth) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.availWidth is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>bufferDepth</tt> property.
     * @return the <tt>bufferDepth</tt> property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getBufferDepth() {
        return bufferDepth_;
    }

    /**
     * Sets the <tt>bufferDepth</tt> property.
     * @param bufferDepth the <tt>bufferDepth</tt> property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setBufferDepth(final int bufferDepth) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            bufferDepth_ = -1;
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>colorDepth</tt> property.
     * @return the <tt>colorDepth</tt> property
     */
    @JsxGetter
    public int getColorDepth() {
        return 24;
    }

    /**
     * Sets the <tt>colorDepth</tt> property.
     * @param colorDepth the <tt>colorDepth</tt> property
     */
    @JsxSetter
    public void setColorDepth(final int colorDepth) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.colorDepth is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>deviceXDPI</tt> property.
     * @return the <tt>deviceXDPI</tt> property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getDeviceXDPI() {
        return 96;
    }

    /**
     * Sets the <tt>deviceXDPI</tt> property.
     * @param deviceXDPI the <tt>deviceXDPI</tt> property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setDeviceXDPI(final int deviceXDPI) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.deviceXDPI is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>deviceYDPI</tt> property.
     * @return the <tt>deviceYDPI</tt> property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getDeviceYDPI() {
        return 96;
    }

    /**
     * Sets the <tt>deviceYDPI</tt> property.
     * @param deviceYDPI the <tt>deviceYDPI</tt> property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setDeviceYDPI(final int deviceYDPI) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.deviceYDPI is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>fontSmoothingEnabled</tt> property.
     * @return the <tt>fontSmoothingEnabled</tt> property
     */
    @JsxGetter(@WebBrowser(IE))
    public boolean getFontSmoothingEnabled() {
        return true;
    }

    /**
     * Sets the <tt>fontSmoothingEnabled</tt> property.
     * @param fontSmoothingEnabled the <tt>fontSmoothingEnabled</tt> property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setFontSmoothingEnabled(final boolean fontSmoothingEnabled) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.fontSmoothingEnabled is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>height</tt> property.
     * @return the <tt>height</tt> property
     */
    @JsxGetter
    public int getHeight() {
        return 768;
    }

    /**
     * Sets the <tt>height</tt> property.
     * @param height the <tt>height</tt> property
     */
    @JsxSetter
    public void setHeight(final int height) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.height is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>left</tt> property.
     * @return the <tt>left</tt> property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getLeft() {
        return 0;
    }

    /**
     * Sets the <tt>left</tt> property.
     * @param left the <tt>left</tt> property
     */
    @JsxSetter(@WebBrowser(FF))
    public void setLeft(final int left) {
        // ignore
    }

    /**
     * Returns the <tt>logicalXDPI</tt> property.
     * @return the <tt>logicalXDPI</tt> property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getLogicalXDPI() {
        return 96;
    }

    /**
     * Sets the <tt>logicalXDPI</tt> property.
     * @param logicalXDPI the <tt>logicalXDPI</tt> property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLogicalXDPI(final int logicalXDPI) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.logicalXDPI is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>logicalYDPI</tt> property.
     * @return the <tt>logicalYDPI</tt> property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getLogicalYDPI() {
        return 96;
    }

    /**
     * Sets the <tt>logicalYDPI</tt> property.
     * @param logicalYDPI the <tt>logicalYDPI</tt> property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setLogicalYDPI(final int logicalYDPI) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.logicalYDPI is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>pixelDepth</tt> property.
     * @return the <tt>pixelDepth</tt> property
     */
    @JsxGetter
    public int getPixelDepth() {
        return 24;
    }

    /**
     * Sets the <tt>pixelDepth</tt> property.
     * @param pixelDepth the <tt>pixelDepth</tt> property
     */
    @JsxSetter
    public void setPixelDepth(final int pixelDepth) {
        // ignore
    }

    /**
     * Returns the <tt>systemXDPI</tt> property.
     * @return the <tt>systemXDPI</tt> property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getSystemXDPI() {
        return 96;
    }

    /**
     * Sets the <tt>systemXDPI</tt> property.
     * @param systemXDPI the <tt>systemXDPI</tt> property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setSystemXDPI(final int systemXDPI) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.systemYDPI is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>systemYDPI</tt> property.
     * @return the <tt>systemYDPI</tt> property
     */
    @JsxGetter(@WebBrowser(IE))
    public int getSystemYDPI() {
        return 96;
    }

    /**
     * Sets the <tt>systemYDPI</tt> property.
     * @param systemYDPI the <tt>systemYDPI</tt> property
     */
    @JsxSetter(@WebBrowser(IE))
    public void setSystemYDPI(final int systemYDPI) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.systemYDPI is read only");
        }
        // otherwise ignore
    }

    /**
     * Returns the <tt>top</tt> property.
     * @return the <tt>top</tt> property
     */
    @JsxGetter(@WebBrowser(FF))
    public int getTop() {
        return 0;
    }

    /**
     * Sets the <tt>top</tt> property.
     * @param top the <tt>top</tt> property
     */
    @JsxSetter(@WebBrowser(FF))
    public void setTop(final int top) {
        // ignore
    }

    /**
     * Returns the <tt>updateInterval</tt> property.
     * @return the <tt>updateInterval</tt> property
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public int getUpdateInterval() {
        return 0;
    }

    /**
     * Returns the <tt>width</tt> property.
     * @return the <tt>width</tt> property
     */
    @JsxGetter
    public int getWidth() {
        return 1024;
    }

    /**
     * Sets the <tt>width</tt> property.
     * @param width the <tt>width</tt> property
     */
    @JsxSetter
    public void setWidth(final int width) {
        if (getBrowserVersion().hasFeature(JS_SCREEN_SETTER_THROWS_ERROR)) {
            throw Context.reportRuntimeError("screen.width is read only");
        }
        // otherwise ignore
    }
}
