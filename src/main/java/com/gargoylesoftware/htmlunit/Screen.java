/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import java.io.Serializable;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

public class Screen implements Serializable {
    private int width;
    private int height;

    public Screen(WebClient webClient) {
        width = webClient.getOptions().getScreenWidth();
        height = webClient.getOptions().getScreenHeight();
    }

    /**
     * Creates an instance.
     */
    public Screen() {
    }

    /**
     * Returns the {@code availHeight} property.
     * @return the {@code availHeight} property
     */
    public int getAvailHeight() {
        return 1040;
    }

    /**
     * Returns the {@code availLeft} property.
     * @return the {@code availLeft} property
     */
    public int getAvailLeft() {
        return 0;
    }

    /**
     * Returns the {@code availTop} property.
     * @return the {@code availTop} property
     */
    public int getAvailTop() {
        return 0;
    }

    /**
     * Returns the {@code availWidth} property.
     * @return the {@code availWidth} property
     */
    public int getAvailWidth() {
        return 1920;
    }

    /**
     * Returns the {@code bufferDepth} property.
     * @return the {@code bufferDepth} property
     */
    public int getBufferDepth() {
        return 0;
    }

    /**
     * Returns the {@code colorDepth} property.
     * @return the {@code colorDepth} property
     */
    public int getColorDepth() {
        return 24;
    }

    /**
     * Returns the {@code deviceXDPI} property.
     * @return the {@code deviceXDPI} property
     */
    public int getDeviceXDPI() {
        return 96;
    }

    /**
     * Returns the {@code deviceYDPI} property.
     * @return the {@code deviceYDPI} property
     */
    public int getDeviceYDPI() {
        return 96;
    }

    /**
     * Returns the {@code fontSmoothingEnabled} property.
     * @return the {@code fontSmoothingEnabled} property
     */
    public boolean isFontSmoothingEnabled() {
        return true;
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the {@code left} property.
     * @return the {@code left} property
     */
    public int getLeft() {
        return 0;
    }

    /**
     * Returns the {@code logicalXDPI} property.
     * @return the {@code logicalXDPI} property
     */
    public int getLogicalXDPI() {
        return 96;
    }

    /**
     * Returns the {@code logicalYDPI} property.
     * @return the {@code logicalYDPI} property
     */
    public int getLogicalYDPI() {
        return 96;
    }

    /**
     * Returns the {@code pixelDepth} property.
     * @return the {@code pixelDepth} property
     */
    public int getPixelDepth() {
        return 24;
    }

    /**
     * Returns the {@code systemXDPI} property.
     * @return the {@code systemXDPI} property
     */
    public int getSystemXDPI() {
        return 96;
    }

    /**
     * Returns the {@code systemYDPI} property.
     * @return the {@code systemYDPI} property
     */
    public int getSystemYDPI() {
        return 96;
    }

    /**
     * Returns the {@code top} property.
     * @return the {@code top} property
     */
    public int getTop() {
        return 0;
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    public int getWidth() {
        return width;
    }
}
