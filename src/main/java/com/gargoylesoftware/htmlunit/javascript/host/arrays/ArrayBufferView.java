/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The ArrayBufferView type describes a particular view on the contents of an {@link ArrayBuffer}'s data.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Frank Danek
 */
@JsxClasses(
        isJSObject = false,
        browsers = { @WebBrowser(value = FF, minVersion = 10), @WebBrowser(CHROME),
            @WebBrowser(value = IE, minVersion = 11) })
public class ArrayBufferView extends SimpleScriptable {

    private ArrayBuffer buffer_;
    private int byteLength_;
    private int byteOffset_;

    /**
     * The constructor.
     * @param buffer the array buffer
     * @param byteOffset the byte offset
     * @param length the length
     */
    protected void constructor(final ArrayBuffer buffer, final int byteOffset, final int length) {
        buffer_ = buffer;
        byteOffset_ = byteOffset;
        byteLength_ = length;
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
     * Sets the buffer.
     * @param buffer the buffer
     */
    protected void setBuffer(final ArrayBuffer buffer) {
        buffer_ = buffer;
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
     * Sets the bytes length.
     * @param byteLength the bytes length
     */
    protected void setByteLength(final int byteLength) {
        byteLength_ = byteLength;
    }

    /**
     * Returns the offset, in bytes, to the first byte of the view within the {@link ArrayBuffer}.
     * @return the offset
     */
    @JsxGetter
    public int getByteOffset() {
        return byteOffset_;
    }
}
