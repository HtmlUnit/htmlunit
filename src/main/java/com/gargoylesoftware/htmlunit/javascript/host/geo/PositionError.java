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
package com.gargoylesoftware.htmlunit.javascript.host.geo;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstant;

/**
 * A JavaScript object for {@code PositionError}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(IE)
public class PositionError extends SimpleScriptable {

    /** The constant for {@code PERMISSION_DENIED}. */
    @JsxConstant
    public static final short PERMISSION_DENIED = 1;
    /** The constant for {@code POSITION_UNAVAILABLE}. */
    @JsxConstant
    public static final short POSITION_UNAVAILABLE = 2;
    /** The constant for {@code TIMEOUT}. */
    @JsxConstant
    public static final short TIMEOUT = 3;

    /**
     * Default constructor.
     */
    public PositionError() {
    }
}
