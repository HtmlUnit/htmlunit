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
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A data type that is used to represent a generic, fixed-length binary data buffer.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClass(browsers = { @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME),
        @WebBrowser(value = IE, minVersion = 10) })
public class ArrayBuffer extends SimpleScriptable {

    private byte[] bytes_;

    /**
     * The constructor.
     * @param length the size, in bytes, of the array buffer to create.
     */
    @JsxConstructor
    public void constructor(final int length) {
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
    @JsxFunction({ @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME) })
    public ArrayBuffer slice(final int begin, Object end) {
        if (end == Undefined.instance) {
            end = getByteLength();
        }
        final byte[] byteArray = new byte[((Number) end).intValue() - begin];
        System.arraycopy(bytes_, begin, byteArray, 0, byteArray.length);
        final ArrayBuffer arrayBuffer = new ArrayBuffer();
        arrayBuffer.bytes_ = byteArray;
        return arrayBuffer;
    }

    byte getByte(final int index) {
        return bytes_[index];
    }

    void setBytes(final int index, final byte[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            bytes_[index + i] = array[i];
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return "ArrayBuffer";
    }

    byte[] getBytes() {
        return bytes_;
    }
}
