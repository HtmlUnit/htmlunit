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

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;

/**
 * A JavaScript object for {@code MSGestureEvent}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass(IE)
public class MSGestureEvent extends UIEvent {

    /** The constant {@code MSGESTURE_FLAG_BEGIN}. */
    @JsxConstant
    public static final int MSGESTURE_FLAG_BEGIN = 1;
    /** The constant {@code MSGESTURE_FLAG_CANCEL}. */
    @JsxConstant
    public static final int MSGESTURE_FLAG_CANCEL = 4;
    /** The constant {@code MSGESTURE_FLAG_END}. */
    @JsxConstant
    public static final int MSGESTURE_FLAG_END = 2;
    /** The constant {@code MSGESTURE_FLAG_INERTIA}. */
    @JsxConstant
    public static final int MSGESTURE_FLAG_INERTIA = 8;
    /** The constant {@code MSGESTURE_FLAG_NONE}. */
    @JsxConstant
    public static final int MSGESTURE_FLAG_NONE = 0;

    /**
     * Creates an instance.
     */
    public MSGestureEvent() {
    }
}
