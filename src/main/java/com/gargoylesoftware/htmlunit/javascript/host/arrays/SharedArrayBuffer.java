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
package com.gargoylesoftware.htmlunit.javascript.host.arrays;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A data type that is used to represent a generic, fixed-length binary data buffer.
 *
 * @author Ronald Brill
 */
@JsxClass({CHROME, EDGE})
public class SharedArrayBuffer extends SimpleScriptable {

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
}
