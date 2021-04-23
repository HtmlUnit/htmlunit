/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.canvas;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.rendering.RenderingBackend;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
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
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public static Scriptable jsConstructor(
            final Context cx, final Object[] args, final Function ctorObj,
            final boolean inNewExpr) {

        if (args.length < 2) {
            throw Context.reportRuntimeError("ImageData ctor - too less arguments");
        }

        NativeUint8ClampedArray data = null;
        final int width;
        final int height;
        if (args[0] instanceof NativeUint8ClampedArray) {
            data = (NativeUint8ClampedArray) args[0];
            if (data.getArrayLength() % 4 != 0) {
                throw Context.reportRuntimeError("ImageData ctor - data length mod 4 not zero");
            }

            width = (int) ScriptRuntime.toInteger(args[1]);
            if (args.length < 3) {
                height = data.getArrayLength() / 4 / width;

                if (data.getArrayLength() != 4 * width * height) {
                    throw Context.reportRuntimeError("ImageData ctor - width not correct");
                }
            }
            else {
                height = (int) ScriptRuntime.toInteger(args[2]);
            }

            if (data.getArrayLength() != 4 * width * height) {
                throw Context.reportRuntimeError("ImageData ctor - width/height not correct");
            }
        }
        else {
            width = (int) ScriptRuntime.toInteger(args[0]);
            height = (int) ScriptRuntime.toInteger(args[1]);
        }

        if (width < 0) {
            throw Context.reportRuntimeError("ImageData ctor - width negative");
        }
        if (height < 0) {
            throw Context.reportRuntimeError("ImageData ctor - height negative");
        }

        final ImageData result = new ImageData(null, 0, 0, width, height);
        if (data != null) {
            final byte[] bytes = data.getBuffer().getBuffer();
            System.arraycopy(bytes, 0, result.bytes_, 0, Math.min(bytes.length, result.bytes_.length));
        }
        return result;
    }

    ImageData(final RenderingBackend context, final int x, final int y, final int width, final int height) {
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
