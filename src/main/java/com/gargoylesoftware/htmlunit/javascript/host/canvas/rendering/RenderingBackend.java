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
package com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering;

import java.io.IOException;

import javax.imageio.ImageReader;

/**
 * Interface to the rendering context used by
 * {@link com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D}.
 * We have this abstraction to support GAE also.
 *
 * @author Ronald Brill
 */
public interface RenderingBackend {

    /**
     * Sets the {@code fillStyle} property.
     * @param fillStyle the {@code fillStyle} property
     */
    void setFillStyle(final String fillStyle);

    /**
     * Paints the specified rectangular area.
     * @param x the x
     * @param y the y
     * @param w the width
     * @param h the height
     */
    void fillRect(final int x, final int y, final int w, final int h);

    /**
     * Draws images onto the context.
     *
     * @param imageReader the reader to read the image from 8the first one)
     * @param dxI the x coordinate of the starting point (top left)
     * @param dyI the y coordinate of the starting point (top left)
     * @throws IOException in case o problems
     */
    void drawImage(final ImageReader imageReader, final int dxI, final int dyI) throws IOException;

    /**
     * Creates a byte array containing the (4) color values of all pixels.
     *
     * @param width the width
     * @param height the height
     * @param sx start point x
     * @param sy start point y
     * @return the bytes
     */
    byte[] getBytes(final int width, final int height, final int sx, final int sy);

    /**
     * Constructs a base64 encoded string out of the image data.
     *
     * @param type the name of the image format
     * @return the base64 encoded string
     * @throws IOException in case o problems
     */
    String encodeToString(final String type) throws IOException;
}
