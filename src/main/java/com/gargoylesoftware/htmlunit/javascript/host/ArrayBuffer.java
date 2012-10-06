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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * A data type that is used to represent a generic, fixed-length binary data buffer.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@JsxClass(browsers = { @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME) })
public class ArrayBuffer extends SimpleScriptable {

    private byte[] buffer_;

    /**
     * The constructor.
     * @param length the size, in bytes, of the array buffer to create.
     */
    @JsxConstructor
    public void constructor(final int length) {
        buffer_ = new byte[length];
    }

    /**
     * Returns the size, in bytes, of the array. This is established during construction and cannot be changed.
     * @return the byte length.
     */
    @JsxGetter
    public int getByteLength() {
        return buffer_.length;
    }

    byte getByte(final int index) {
        return buffer_[index];
    }

    void setBytes(final int index, final byte[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            buffer_[index + i] = array[i];
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName() {
        return "ArrayBuffer";
    }

    byte[] getBuffer() {
        return buffer_;
    }
}
