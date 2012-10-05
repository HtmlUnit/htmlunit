/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.ScriptableObject;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The ArrayBufferView type describes a particular view on the contents of an {@link ArrayBuffer}'s data.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(isJSObject = false, browsers = { @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME) })
public class ArrayBufferView extends SimpleScriptable {

    private ArrayBuffer buffer_;
    private int byteLength_;
    private int byteOffset_;

    /**
     * The constructor.
     *
     * @param object the object
     * @param byteOffset optional byteOffset
     * @param length optional length
     */
    public void constructor(final Object object, final Object byteOffset, final Object length) {
        if (object instanceof Number) {
            constructor(((Number) object).intValue());
        }
        else if (object instanceof NativeArray) {
            constructor((NativeArray) object);
        }
        else if (object instanceof ArrayBufferView) {
            constructor((ArrayBufferView) object);
        }
        else if (object instanceof ArrayBuffer) {
            constructorAll((ArrayBuffer) object, (Integer) byteOffset, (Integer) length);
        }
        else {
            Context.reportRuntimeError("Invalid type " + object.getClass().getName());
        }
    }

    private void constructor(final int length) {
        initBuffer(length);
        byteLength_ = length;
    }

    private void constructor(final NativeArray array) {
        final int length = (int) array.getLength();
        initBuffer(length);
        set(array, 0);
        byteLength_ = length;
    }

    private void constructor(final ArrayBufferView array) {
        final int length = array.getLength();
        initBuffer(length);
        set(array, 0);
        byteLength_ = length;
    }

    private void constructorAll(final ArrayBuffer buffer, final int byteOffset, final int length) {
        buffer_ = buffer;
        byteOffset_ = byteOffset;
        byteLength_ = length;
    }

    private void initBuffer(final int length) {
        buffer_ = new ArrayBuffer();
        buffer_.constructor(length);
        buffer_.setPrototype(getPrototype(buffer_.getClass()));
        buffer_.setParentScope(getParentScope());
    }

    /**
     * Returns the buffer this view references.
     * @return the buffer
     */
    @JsxGetter
    public ArrayBuffer getBuffer() {
        return buffer_;
    }

    /**
     * Returns the length, in bytes, of the view.
     * @return the length
     */
    @JsxGetter
    public int getByteLength() {
        return byteLength_;
    }

    /**
     * Returns the number of entries in the array.
     * @return the number of entries
     */
    @JsxGetter
    public int getLength() {
        return byteLength_;
    }

    /**
     * Returns the offset, in bytes, to the first byte of the view within the {@link ArrayBuffer}.
     * @return the offset
     */
    @JsxGetter
    public int getByteOffset() {
        return byteOffset_;
    }

    /**
     * Sets multiple values in the typed array, reading input values from a specified array.
     * @param sourceArray the source array
     * @param offset the offset into the target array at which to begin writing values from the source one
     */
    @JsxFunction
    public void set(final ScriptableObject sourceArray, final int offset) {
        final int length = ((Number) ScriptableObject.getProperty(sourceArray, "length")).intValue();
        for (int i = 0; i < length; i++) {
            put(i + offset, this, sourceArray.get(i));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        return buffer_.getByte(index + byteOffset_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final int index, final Scriptable start, final Object value) {
        buffer_.setByte(index + byteOffset_, (Byte) ensureType(value));
    }

    /**
     * Ensure the type.
     * @param o the object
     * @return the correct type
     */
    protected Object ensureType(final Object o) {
        return o;
    }

    /**
     * Returns a new view on the ArrayBuffer store for this object.
     * @param begin the offset to the first element in the array to be referenced by the new object
     * @param end the end offset (exclusive), optional to return at the end.
     * @return the newly created array
     */
    @JsxFunction
    public ArrayBufferView subarray(final int begin, Object end) {
        if (end == Undefined.instance) {
            end = getLength();
        }
        try {
            final ArrayBufferView object = getClass().newInstance();
            object.setPrototype(getPrototype());
            object.setParentScope(getParentScope());
            object.constructorAll(getBuffer(), begin, ((Number) end).intValue() - begin);
            return object;
        }
        catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
