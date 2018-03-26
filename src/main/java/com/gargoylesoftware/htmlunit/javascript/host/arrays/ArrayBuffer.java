/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * A data type that is used to represent a generic, fixed-length binary data buffer.
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@JsxClass
public class ArrayBuffer extends SimpleScriptable {

    private byte[] bytes_;

    /**
     * Ctor.
     */
    public ArrayBuffer() {
    }

    /**
     * Ctor with given bytes.
     * @param bytes the initial bytes
     */
    public ArrayBuffer(final byte[] bytes) {
        bytes_ = bytes;
    }

    /**
     * The constructor.
     * @param length the size, in bytes, of the array buffer to create.
     */
    @JsxConstructor
    public void constructor(final int length) {
        if (length < 0) {
            throw ScriptRuntime.rangeError("invalid array length '" + length + "'.");
        }
        bytes_ = new byte[length];
    }

    /**
     * Returns the size, in bytes, of the array. This is established during construction and cannot be changed.
     * @return the byte length.
     */
    @JsxGetter
    public int getByteLength() {
        return bytes_.length;
    }

    /**
     * Returns a new ArrayBuffer whose contents are a copy of this ArrayBuffer's bytes
     * from begin, inclusive, up to end, exclusive.
     * @param begin byte index to start slicing
     * @param end (optional) byte index to end slicing
     * @return the newly created ArrayBuffer
     */
    @JsxFunction
    public ArrayBuffer slice(final Object begin, final Object end) {
        if (begin == Undefined.instance || begin instanceof Boolean) {
            throw Context.reportRuntimeError("Invalid type " + begin.getClass().getName());
        }

        final double beginNumber = Context.toNumber(begin);
        final int beginInt;
        if (Double.isNaN(beginNumber)) {
            beginInt = 0;
        }
        else if (Double.isInfinite(beginNumber)) {
            if (beginNumber > 0) {
                final byte[] byteArray = new byte[0];
                final ArrayBuffer arrayBuffer = new ArrayBuffer(byteArray);
                return arrayBuffer;
            }
            beginInt = 0;
        }
        else {
            beginInt = (int) beginNumber;
            if (beginInt != beginNumber) {
                throw Context.reportRuntimeError("Invalid type " + begin.getClass().getName());
            }
        }

        double endNumber;
        if (end == Undefined.instance) {
            endNumber = getByteLength();
        }
        else {
            endNumber = Context.toNumber(end);
        }

        if (Double.isNaN(endNumber) || Double.isInfinite(endNumber) || endNumber < beginInt) {
            endNumber = beginInt;
        }

        final byte[] byteArray = new byte[(int) endNumber - beginInt];
        System.arraycopy(bytes_, beginInt, byteArray, 0, byteArray.length);
        final ArrayBuffer arrayBuffer = new ArrayBuffer(byteArray);
        return arrayBuffer;
    }

    byte getByte(final int index) {
        return bytes_[index];
    }

    /**
     * Sets the bytes.
     * @param index the starting index
     * @param array the array
     */
    public void setBytes(final int index, final byte[] array) {
        int i = array.length - 1;
        if (index + i >= bytes_.length) {
            i = bytes_.length - index - 1;
        }
        for ( ; i >= 0; i--) {
            bytes_[index + i] = array[i];
        }
    }

    /**
     * @return the bytes
     */
    public byte[] getBytes() {
        return bytes_;
    }
}
