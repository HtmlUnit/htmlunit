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
package com.gargoylesoftware.htmlunit;

import java.io.Serializable;

/**
 * {@code Screen}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author cd alexndr
 */
public class Screen implements Serializable {

    private final transient WebClient webClient_;

    /**
     * Creates an instance.
     * @param webClient the client this belongs to
     */
    public Screen(final WebClient webClient) {
        webClient_ = webClient;
    }

    /**
     * @return the {@code availHeight} property
     */
    public int getAvailHeight() {
        return 1040;
    }

    /**
     * Sets the {@code availHeight} property.
     * @param availHeight the {@code availHeight} property
     */
    public void setAvailHeight(final int availHeight) {
        // ignore
    }

    /**
     * @return the {@code availLeft} property
     */
    public int getAvailLeft() {
        return 0;
    }

    /**
     * Sets the {@code availLeft} property.
     * @param availLeft the {@code availLeft} property
     */
    public void setAvailLeft(final int availLeft) {
        // ignore
    }

    /**
     * @return the {@code availTop} property
     */
    public int getAvailTop() {
        return 0;
    }

    /**
     * Sets the {@code availTop} property.
     * @param availTop the {@code availTop} property
     */
    public void setAvailTop(final int availTop) {
        // ignore
    }

    /**
     * @return the {@code availWidth} property
     */
    public int getAvailWidth() {
        return 1920;
    }

    /**
     * Sets the {@code availWidth} property.
     * @param availWidth the {@code availWidth} property
     */
    public void setAvailWidth(final int availWidth) {
        // ignore
    }

    /**
     * @return the {@code bufferDepth} property
     */
    public int getBufferDepth() {
        return 0;
    }

    /**
     * Sets the {@code bufferDepth} property.
     * @param bufferDepth the {@code bufferDepth} property
     */
    public void setBufferDepth(final int bufferDepth) {
        // ignore
    }

    /**
     * @return the {@code colorDepth} property
     */
    public int getColorDepth() {
        return 24;
    }

    /**
     * Sets the {@code colorDepth} property.
     * @param colorDepth the {@code colorDepth} property
     */
    public void setColorDepth(final int colorDepth) {
        // ignore
    }

    /**
     * @return the {@code deviceXDPI} property
     */
    public int getDeviceXDPI() {
        return 96;
    }

    /**
     * Sets the {@code deviceXDPI} property.
     * @param deviceXDPI the {@code deviceXDPI} property
     */
    public void setDeviceXDPI(final int deviceXDPI) {
        // ignore
    }

    /**
     * @return the {@code deviceYDPI} property
     */
    public int getDeviceYDPI() {
        return 96;
    }

    /**
     * Sets the {@code deviceYDPI} property.
     * @param deviceYDPI the {@code deviceYDPI} property
     */
    public void setDeviceYDPI(final int deviceYDPI) {
        // ignore
    }

    /**
     * @return the {@code fontSmoothingEnabled} property
     */
    public boolean isFontSmoothingEnabled() {
        return true;
    }

    /**
     * Sets the {@code fontSmoothingEnabled} property.
     * @param fontSmoothingEnabled the {@code fontSmoothingEnabled} property
     */
    public void setFontSmoothingEnabled(final boolean fontSmoothingEnabled) {
        // ignore
    }

    /**
     * @return the {@code height} property
     */
    public int getHeight() {
        if (webClient_ == null) {
            return WebClientOptions.DEFAULT_SCRREN_HEIGHT;
        }
        return webClient_.getOptions().getScreenHeight();
    }

    /**
     * Sets the {@code height} property.
     * @param height the {@code height} property
     */
    public void setHeight(final int height) {
        // ignore
    }

    /**
     * @return the {@code left} property
     */
    public int getLeft() {
        return 0;
    }

    /**
     * Sets the {@code left} property.
     * @param left the {@code left} property
     */
    public void setLeft(final int left) {
        // ignore
    }

    /**
     * @return the {@code logicalXDPI} property
     */
    public int getLogicalXDPI() {
        return 96;
    }

    /**
     * Sets the {@code logicalXDPI} property.
     * @param logicalXDPI the {@code logicalXDPI} property
     */
    public void setLogicalXDPI(final int logicalXDPI) {
        // ignore
    }

    /**
     * @return the {@code logicalYDPI} property
     */
    public int getLogicalYDPI() {
        return 96;
    }

    /**
     * Sets the {@code logicalYDPI} property.
     * @param logicalYDPI the {@code logicalYDPI} property
     */
    public void setLogicalYDPI(final int logicalYDPI) {
        // ignore
    }

    /**
     * @return the {@code pixelDepth} property
     */
    public int getPixelDepth() {
        return 24;
    }

    /**
     * Sets the {@code pixelDepth} property.
     * @param pixelDepth the {@code pixelDepth} property
     */
    public void setPixelDepth(final int pixelDepth) {
        // ignore
    }

    /**
     * @return the {@code systemXDPI} property
     */
    public int getSystemXDPI() {
        return 96;
    }

    /**
     * Sets the {@code systemXDPI} property.
     * @param systemXDPI the {@code systemXDPI} property
     */
    public void setSystemXDPI(final int systemXDPI) {
        // ignore
    }

    /**
     * @return the {@code systemYDPI} property
     */
    public int getSystemYDPI() {
        return 96;
    }

    /**
     * Sets the {@code systemYDPI} property.
     * @param systemYDPI the {@code systemYDPI} property
     */
    public void setSystemYDPI(final int systemYDPI) {
        // ignore
    }

    /**
     * @return the {@code top} property
     */
    public int getTop() {
        return 0;
    }

    /**
     * Sets the {@code top} property.
     * @param top the {@code top} property
     */
    public void setTop(final int top) {
        // ignore
    }

    /**
     * @return the {@code width} property
     */
    public int getWidth() {
        if (webClient_ == null) {
            return WebClientOptions.DEFAULT_SCRREN_WIDTH;
        }
        return webClient_.getOptions().getScreenWidth();
    }

    /**
     * Sets the {@code width} property.
     * @param width the {@code width} property
     */
    public void setWidth(final int width) {
        // ignore
    }
}
