/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;

import java.awt.Color;
import java.awt.image.BufferedImage;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.ArrayBuffer;
import com.gargoylesoftware.htmlunit.javascript.host.arrays.Uint8ClampedArray;

/**
 * A JavaScript object for {@code ImageData}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11),
        @WebBrowser(EDGE) })
public class ImageData extends SimpleScriptable {

    private final BufferedImage image_;
    private final int sx_;
    private final int sy_;
    private final int width_;
    private final int height_;
    private Uint8ClampedArray data_;

    /**
     * Default constructor.
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(EDGE) })
    public ImageData() {
        this(null, 0, 0, 0, 0);
    }

    ImageData(final BufferedImage image, final int x, final int y, final int width, final int height) {
        image_ = image;
        sx_ = x;
        sy_ = y;
        width_ = width;
        height_ = height;
    }

    private byte[] getBytes() {
        final byte[] array = new byte[width_ * height_ * 4];
        int index = 0;
        for (int x = 0; x < width_; x++) {
            for (int y = 0; y < height_; y++) {
                final Color c = new Color(image_.getRGB(sx_ + x, sy_ + y), true);
                array[index++] = (byte) c.getRed();
                array[index++] = (byte) c.getGreen();
                array[index++] = (byte) c.getBlue();
                array[index++] = (byte) c.getAlpha();
            }
        }
        return array;
    }

    /**
     * Returns the {@code width} property.
     * @return the {@code width} property
     */
    @JsxGetter
    public int getWidth() {
        return width_;
    }

    /**
     * Returns the {@code height} property.
     * @return the {@code height} property
     */
    @JsxGetter
    public int getHeight() {
        return height_;
    }

    /**
     * Returns a {@link Uint8ClampedArray} representing a one-dimensional array containing
     * the data in the RGBA order, with integer values between 0 and 255 (included).
     * @return the {@code data} property
     */
    @JsxGetter
    public Uint8ClampedArray getData() {
        if (data_ == null) {
            final byte[] bytes = getBytes();
            final ArrayBuffer arrayBuffer = new ArrayBuffer();
            arrayBuffer.constructor(bytes.length);
            arrayBuffer.setBytes(0, bytes);

            data_ = new Uint8ClampedArray();
            data_.setParentScope(getParentScope());
            data_.setPrototype(getPrototype(data_.getClass()));

            data_.constructor(arrayBuffer, 0, bytes.length);
        }

        return data_;
    }

}
