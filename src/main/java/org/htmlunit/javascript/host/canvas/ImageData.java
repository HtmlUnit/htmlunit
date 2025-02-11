/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.canvas;

import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.corejs.javascript.typedarrays.NativeArrayBuffer;
import org.htmlunit.corejs.javascript.typedarrays.NativeUint8ClampedArray;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.host.dom.DOMException;
import org.htmlunit.platform.canvas.rendering.RenderingBackend;

/**
 * A JavaScript object for {@code ImageData}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class ImageData extends HtmlUnitScriptable {

    private final byte[] bytes_;
    private final int width_;
    private final int height_;
    private NativeUint8ClampedArray data_;

    /**
     * Default constructor.
     */
    public ImageData() {
        this(null, 0, 0, 0, 0);
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param scope the scope
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static ImageData jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        if (args.length < 2) {
            throw JavaScriptEngine.typeError("ImageData ctor - too less arguments");
        }

        NativeUint8ClampedArray data = null;
        final int width;
        final int height;
        if (args[0] instanceof NativeUint8ClampedArray) {
            data = (NativeUint8ClampedArray) args[0];
            if (data.getArrayLength() % 4 != 0) {
                throw JavaScriptEngine.asJavaScriptException(
                        (HtmlUnitScriptable) JavaScriptEngine.getTopCallScope(),
                        "ImageData ctor - data length mod 4 not zero",
                        DOMException.INVALID_STATE_ERR);
            }

            width = (int) JavaScriptEngine.toInteger(args[1]);
            if (args.length < 3) {
                height = data.getArrayLength() / 4 / width;

                if (data.getArrayLength() != 4 * width * height) {
                    throw JavaScriptEngine.asJavaScriptException(
                            (HtmlUnitScriptable) JavaScriptEngine.getTopCallScope(),
                            "ImageData ctor - width not correct",
                            DOMException.INDEX_SIZE_ERR);
                }
            }
            else {
                height = (int) JavaScriptEngine.toInteger(args[2]);
            }

            if (data.getArrayLength() != 4 * width * height) {
                throw JavaScriptEngine.asJavaScriptException(
                        (HtmlUnitScriptable) JavaScriptEngine.getTopCallScope(),
                        "ImageData ctor - width/height not correct",
                        DOMException.INDEX_SIZE_ERR);
            }
        }
        else {
            width = (int) JavaScriptEngine.toInteger(args[0]);
            height = (int) JavaScriptEngine.toInteger(args[1]);
        }

        if (width < 0) {
            throw JavaScriptEngine.asJavaScriptException(
                    (HtmlUnitScriptable) JavaScriptEngine.getTopCallScope(),
                    "ImageData ctor - width negative",
                    DOMException.INDEX_SIZE_ERR);
        }
        if (height < 0) {
            throw JavaScriptEngine.asJavaScriptException(
                    (HtmlUnitScriptable) JavaScriptEngine.getTopCallScope(),
                    "ImageData ctor - height negative",
                    DOMException.INDEX_SIZE_ERR);
        }

        final ImageData result = new ImageData(null, 0, 0, width, height);
        if (data != null) {
            final byte[] bytes = data.getBuffer().getBuffer();
            System.arraycopy(bytes, 0, result.bytes_, 0, Math.min(bytes.length, result.bytes_.length));
        }
        return result;
    }

    ImageData(final RenderingBackend context, final int x, final int y, final int width, final int height) {
        super();
        if (context == null) {
            bytes_ = new byte[width * height * 4];
        }
        else {
            bytes_ = context.getBytes(width, height, x, y);
        }

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
