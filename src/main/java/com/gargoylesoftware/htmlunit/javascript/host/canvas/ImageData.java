/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF60;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF68;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.RenderingBackend;

import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import net.sourceforge.htmlunit.corejs.javascript.typedarrays.NativeUint8ClampedArray;

/**
 * A JavaScript object for {@code ImageData}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class ImageData extends SimpleScriptable {

    private final byte[] bytes_;
    private final int sx_;
    private final int sy_;
    private final int width_;
    private final int height_;
    private NativeUint8ClampedArray data_;

    /**
     * Default constructor.
     */
    @JsxConstructor({CHROME, FF68, FF60})
    public ImageData() {
        this(null, 0, 0, 0, 0);
    }

    ImageData(final RenderingBackend context, final int x, final int y, final int width, final int height) {
        if (context == null) {
            bytes_ = new byte[width * height * 4];
        }
        else {
            bytes_ = context.getBytes(width, height, x, y);
        }

        sx_ = x;
        sy_ = y;
        width_ = width;
        height_ = height;
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
     * Returns a {@link NativeUint8ClampedArray} representing a one-dimensional array containing
     * the data in the RGBA order, with integer values between 0 and 255 (included).
     * @return the {@code data} property
     */
    @JsxGetter
    public NativeUint8ClampedArray getData() {
        if (data_ == null) {
            final NativeArrayBuffer arrayBuffer = new NativeArrayBuffer(bytes_.length);
            System.arraycopy(bytes_, 0, arrayBuffer.getBuffer(), 0, bytes_.length);

            data_ = new NativeUint8ClampedArray(arrayBuffer, 0, bytes_.length);
            data_.setParentScope(getParentScope());
            data_.setPrototype(ScriptableObject.getClassPrototype(getWindow(this), data_.getClassName()));
        }

        return data_;
    }

}
