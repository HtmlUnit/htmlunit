/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The ArrayBufferView type describes a particular view on the contents of an {@link ArrayBuffer}'s data.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(isJSObject = false, browsers = { @WebBrowser(value = FF, minVersion = 15), @WebBrowser(CHROME) })
public class DataView extends ArrayBufferView {

    /**
     * JavaScript constructor.
     * @param array the array
     * @param byteOffset the byte offset
     * @param length the length
     */
    @JsxConstructor
    protected void constructor(final ArrayBuffer array, final int byteOffset, Object length) {
        if (length == Undefined.instance) {
            length = array.getByteLength();
        }
        super.constructor(array, byteOffset, ((Number) length).intValue());
    }

    /**
     * Gets a signed 8-bit integer at the specified byte offset from the start of the view.
     * @param byteOffset the byte offset
     * @return the byte
     */
    @JsxFunction
    public byte getInt8(final int byteOffset) {
        return getBuffer().getBytes()[getByteOffset() + byteOffset];
    }

    /**
     * Sets the given signed 8-bit integer at the specified offset.
     * @param byteOffset the byte offset
     * @param value the value
     */
    @JsxFunction
    public void setInt8(final int byteOffset, final int value) {
        final byte[] array = getBuffer().getBytes();
        array[getByteOffset() + byteOffset] = (byte) value;
    }

    /**
     * Gets a signed 16-bit integer at the specified byte offset from the start of the view.
     * @param byteOffset the byte offset
     * @param littleEndian whether the value is stored in little- or big-endian format
     * @return the value
     */
    @JsxFunction
    public short getInt16(final int byteOffset, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buff.getShort(getByteOffset() + byteOffset);
    }

    /**
     * Sets the given signed 16-bit integer at the specified offset.
     * @param byteOffset the byte offset
     * @param value the value
     * @param littleEndian whether the value is stored in little- or big-endian format
     */
    @JsxFunction
    public void setInt16(final int byteOffset, final int value, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        buff.putShort(getByteOffset() + byteOffset, (short) value);
    }

    /**
     * Gets a signed 32-bit integer at the specified byte offset from the start of the view.
     * @param byteOffset the byte offset
     * @param littleEndian whether the value is stored in little- or big-endian format
     * @return the value
     */
    @JsxFunction
    public int getInt32(final int byteOffset, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buff.getInt(getByteOffset() + byteOffset);
    }

    /**
     * Sets the given signed 32-bit integer at the specified offset.
     * @param byteOffset the byte offset
     * @param value the value
     * @param littleEndian whether the value is stored in little- or big-endian format
     */
    @JsxFunction
    public void setInt32(final int byteOffset, final int value, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        buff.putInt(getByteOffset() + byteOffset, value);
    }

    /**
     * Gets an unsigned 8-bit integer at the specified byte offset from the start of the view.
     * @param byteOffset the byte offset
     * @return the value
     */
    @JsxFunction
    public int getUint8(final int byteOffset) {
        return getBuffer().getBytes()[getByteOffset() + byteOffset] & 0xFF;
    }

    /**
     * Sets the given unsigned 8-bit integer at the specified offset.
     * @param byteOffset the byte offset
     * @param value the value
     */
    @JsxFunction
    public void setUint8(final int byteOffset, final int value) {
        setInt8(byteOffset, value);
    }

    /**
     * Gets an unsigned 16-bit integer at the specified byte offset from the start of the view.
     * @param byteOffset the byte offset
     * @param littleEndian whether the value is stored in little- or big-endian format
     * @return the value
     */
    @JsxFunction
    public int getUint16(final int byteOffset, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buff.getShort(getByteOffset() + byteOffset) & 0xFFFF;
    }

    /**
     * Sets the given unsigned 16-bit integer at the specified offset.
     * @param byteOffset the byte offset
     * @param value the value
     * @param littleEndian whether the value is stored in little- or big-endian format
     */
    @JsxFunction
    public void setUint16(final int byteOffset, final int value, final boolean littleEndian) {
        setInt16(byteOffset, value, littleEndian);
    }

    /**
     * Gets an unsigned 32-bit integer at the specified byte offset from the start of the view.
     * @param byteOffset the byte offset
     * @param littleEndian whether the value is stored in little- or big-endian format
     * @return the value
     */
    @JsxFunction
    public long getUint32(final int byteOffset, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buff.getInt(getByteOffset() + byteOffset) & 0xFFFFFFFFL;
    }

    /**
     * Sets the given unsigned 32-bit integer at the specified offset.
     * @param byteOffset the byte offset
     * @param value the value
     * @param littleEndian whether the value is stored in little- or big-endian format
     */
    @JsxFunction
    public void setUint32(final int byteOffset, final int value, final boolean littleEndian) {
        setInt32(byteOffset, value, littleEndian);
    }

    /**
     * Gets a 32-bit floating point number at the specified byte offset from the start of the view.
     * @param byteOffset the byte offset
     * @param littleEndian whether the value is stored in little- or big-endian format
     * @return the value
     */
    @JsxFunction
    public float getFloat32(final int byteOffset, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buff.getFloat(getByteOffset() + byteOffset);
    }

    /**
     * Sets the given 32-bit floating point number at the specified offset.
     * @param byteOffset the byte offset
     * @param value the value
     * @param littleEndian whether the value is stored in little- or big-endian format
     */
    @JsxFunction
    public void setFloat32(final int byteOffset, final double value, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        buff.putFloat(getByteOffset() + byteOffset, (float) value);
    }

    /**
     * Gets a 64-bit floating point number at the specified byte offset from the start of the view.
     * @param byteOffset the byte offset
     * @param littleEndian whether the value is stored in little- or big-endian format
     * @return the value
     */
    @JsxFunction
    public double getFloat64(final int byteOffset, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        return buff.getDouble(getByteOffset() + byteOffset);
    }

    /**
     * Sets the given 32-bit floating point number at the specified offset.
     * @param byteOffset the byte offset
     * @param value the value
     * @param littleEndian whether the value is stored in little- or big-endian format
     */
    @JsxFunction
    public void setFloat64(final int byteOffset, final double value, final boolean littleEndian) {
        final ByteBuffer buff = ByteBuffer.wrap(getBuffer().getBytes());
        if (littleEndian) {
            buff.order(ByteOrder.LITTLE_ENDIAN);
        }
        buff.putDouble(getByteOffset() + byteOffset, value);
    }
}
