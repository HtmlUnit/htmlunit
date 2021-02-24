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
package com.gargoylesoftware.htmlunit.javascript.host.event;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code TextEvent}.
 *
 * @author Ahmed Ashour
 */
@JsxClass({CHROME, EDGE, IE})
public class TextEvent extends UIEvent {

    /** Constant for {@code DOM_INPUT_METHOD_UNKNOWN}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_UNKNOWN = 0;
    /** Constant for {@code DOM_INPUT_METHOD_KEYBOARD}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_KEYBOARD = 1;
    /** Constant for {@code DOM_INPUT_METHOD_PASTE}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_PASTE = 2;
    /** Constant for {@code DOM_INPUT_METHOD_DROP}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_DROP = 3;
    /** Constant for {@code DOM_INPUT_METHOD_IME}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_IME = 4;
    /** Constant for {@code DOM_INPUT_METHOD_OPTION}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_OPTION = 5;
    /** Constant for {@code DOM_INPUT_METHOD_HANDWRITING}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_HANDWRITING = 6;
    /** Constant for {@code DOM_INPUT_METHOD_VOICE}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_VOICE = 7;
    /** Constant for {@code DOM_INPUT_METHOD_MULTIMODAL}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_MULTIMODAL = 8;
    /** Constant for {@code DOM_INPUT_METHOD_SCRIPT}. */
    @JsxConstant(IE)
    public static final int DOM_INPUT_METHOD_SCRIPT = 9;

    /**
     * Default constructor.
     */
    @JsxConstructor({CHROME, EDGE})
    public TextEvent() {
    }
}
