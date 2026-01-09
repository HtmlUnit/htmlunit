/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit;

import java.io.Serializable;

/**
 * {@code Screen}.
 *
 * @author Mike Bowler
 * @author Daniel Gredler
 * @author Chris Erskine
 * @author Ronald Brill
 * @author Ahmed Ashour
 * @author cd alexndr
 */
public class Screen implements Serializable {

    private final int screenHeight_;
    private final int screenWidth_;

    /**
     * Creates an instance.
     * @param webClient the client this belongs to
     */
    public Screen(final WebClient webClient) {
        super();
        screenHeight_ = webClient.getOptions().getScreenHeight();
        screenWidth_ =  webClient.getOptions().getScreenWidth();
    }

    /**
     * @return the {@code availHeight} property
     */
    public int getAvailHeight() {
        return 1032;
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
     * @return the {@code height} property
     */
    public int getHeight() {
        return screenHeight_;
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
        return screenWidth_;
    }

    /**
     * Sets the {@code width} property.
     * @param width the {@code width} property
     */
    public void setWidth(final int width) {
        // ignore
    }
}
