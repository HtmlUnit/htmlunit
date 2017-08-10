/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.file;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.event.EventTarget;

import net.sourceforge.htmlunit.corejs.javascript.Function;

/**
 * A JavaScript object for {@code FileReader}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class FileReader extends EventTarget {

    /** No data has been loaded yet. */
    @JsxConstant
    public static final short EMPTY = 0;

    /** Data is currently being loaded. */
    @JsxConstant
    public static final short LOADING = 1;

    /** The entire read request has been completed. */
    @JsxConstant
    public static final short DONE = 2;

    private int readyState_;

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public FileReader() {
        readyState_ = EMPTY;
    }

    /**
     * Returns the {@code onload} event handler for this element.
     * @return the {@code onload} event handler for this element
     */
    @JsxGetter
    public int getReadyState() {
        return readyState_;
    }

    /**
     * Returns the {@code onload} event handler for this element.
     * @return the {@code onload} event handler for this element
     */
    @JsxGetter
    public Function getOnload() {
        return getEventHandler("load");
    }

    /**
     * Sets the {@code onload} event handler for this element.
     * @param onload the {@code onload} event handler for this element
     */
    @JsxSetter
    public void setOnload(final Object onload) {
        setEventHandler("load", onload);
    }

    /**
     * Returns the {@code onerror} event handler for this element.
     * @return the {@code onerror} event handler for this element
     */
    @JsxGetter
    public Function getOnerror() {
        return getEventHandler("error");
    }

    /**
     * Sets the {@code onerror} event handler for this element.
     * @param onerror the {@code onerror} event handler for this element
     */
    @JsxSetter
    public void setOnerror(final Object onerror) {
        setEventHandler("error", onerror);
    }

    /**
     * Function readAsDataURL.
     * @param file the file
     */
    @JsxFunction
    public void readAsDataURL(final File file) {
    }
}
