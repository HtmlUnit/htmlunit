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
 * Simple no-op impl of {@link RenderingBackend}.
 *
 * @author Ronald Brill
 */
public class GaeRenderingBackend implements RenderingBackend {

    /**
     * Constructor.
     * @param imageWidth the width
     * @param imageHeight the height
     */
    public GaeRenderingBackend(final int imageWidth, final int imageHeight) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFillStyle(final String fillStyle) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillRect(final int x, final int y, final int w, final int h) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawImage(final ImageReader imageReader, final int dxI, final int dyI) throws IOException {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(final int width, final int height, final int sx, final int sy) {
        return new byte[width * height * 4];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encodeToString(final String type) throws IOException {
        return "";
    }

}
