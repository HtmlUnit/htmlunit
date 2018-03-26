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
package com.gargoylesoftware.htmlunit.javascript.host.speech;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF52;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for {@code SpeechSynthesisErrorEvent}.
 *
 * @author Ronald Brill
 */
@JsxClass(FF52)
public class SpeechSynthesisErrorEvent extends SimpleScriptable {

    /** Constant. */
    @JsxConstant(FF52)
    public static final int ALT_MASK = 0x1;

    /** Constant. */
    @JsxConstant(FF52)
    public static final int CONTROL_MASK = 0x2;

    /** Constant. */
    @JsxConstant(FF52)
    public static final int SHIFT_MASK = 0x4;

    /** Constant. */
    @JsxConstant(FF52)
    public static final int META_MASK = 0x8;

    /** The first event phase: the capturing phase. */
    @JsxConstant(FF52)
    public static final short CAPTURING_PHASE = 1;

    /** The second event phase: at the event target. */
    @JsxConstant(FF52)
    public static final short AT_TARGET = 2;

    /** The third (and final) event phase: the bubbling phase. */
    @JsxConstant(FF52)
    public static final short BUBBLING_PHASE = 3;

    /** No event phase. */
    @JsxConstant(FF52)
    public static final short NONE = 0;

    /**
     * Creates a new instance.
     */
    @JsxConstructor
    public SpeechSynthesisErrorEvent() {
    }
}
