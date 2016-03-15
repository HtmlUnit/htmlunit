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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The default implementation of {@link RenderingBackend}.
 *
 * @author Ronald Brill
 */
public class AwtRenderingBackend implements RenderingBackend {

    private static final Log LOG = LogFactory.getLog(AwtRenderingBackend.class);

    private final BufferedImage image_;
    private final Graphics2D graphics2D_;

    /**
     * Constructor.
     * @param imageWidth the width
     * @param imageHeight the height
     */
    public AwtRenderingBackend(final int imageWidth, final int imageHeight) {
        image_ = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        graphics2D_ = image_.createGraphics();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFillStyle(final String fillStyle) {
        final String tmpFillStyle = fillStyle.replaceAll("\\s", "");
        Color color = null;
        if (tmpFillStyle.startsWith("rgb(")) {
            final String[] colors = tmpFillStyle.substring(4, tmpFillStyle.length() - 1).split(",");
            color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]));
        }
        else if (tmpFillStyle.startsWith("rgba(")) {
            final String[] colors = tmpFillStyle.substring(5, tmpFillStyle.length() - 1).split(",");
            color = new Color(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]),
                (int) (Float.parseFloat(colors[3]) * 255));
        }
        else if (tmpFillStyle.startsWith("#")) {
            color = Color.decode(tmpFillStyle);
        }
        else {
            try {
                final Field f = Color.class.getField(tmpFillStyle);
                color = (Color) f.get(null);
            }
            catch (final Exception e) {
                LOG.info("Can not find color '" + tmpFillStyle + '\'');
                color = Color.black;
            }
        }
        graphics2D_.setColor(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillRect(final int x, final int y, final int w, final int h) {
        graphics2D_.fillRect(x, y, w, h);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void drawImage(final ImageReader imageReader, final int dxI, final int dyI) throws IOException {
        if (imageReader.getNumImages(true) != 0) {
            final BufferedImage img = imageReader.read(0);
            graphics2D_.drawImage(img, dxI, dyI, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] getBytes(final int width, final int height, final int sx, final int sy) {
        final byte[] array = new byte[width * height * 4];
        int index = 0;
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                final Color c = new Color(image_.getRGB(sx + x, sy + y), true);
                array[index++] = (byte) c.getRed();
                array[index++] = (byte) c.getGreen();
                array[index++] = (byte) c.getBlue();
                array[index++] = (byte) c.getAlpha();
            }
        }
        return array;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String encodeToString(final String type) throws IOException {
        try (final ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ImageIO.write(image_, type, bos);

            final byte[] imageBytes = bos.toByteArray();
            return new String(new Base64().encode(imageBytes));
        }
    }
}
