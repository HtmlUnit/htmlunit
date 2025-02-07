/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.geo;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstant;
import org.htmlunit.javascript.configuration.JsxConstructor;

/**
 * A JavaScript object for GeolocationPositionError.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class GeolocationPositionError extends HtmlUnitScriptable {

    /** The constant for {@code PERMISSION_DENIED}. */
    @JsxConstant
    public static final int PERMISSION_DENIED = 1;
    /** The constant for {@code POSITION_UNAVAILABLE}. */
    @JsxConstant
    public static final int POSITION_UNAVAILABLE = 2;
    /** The constant for {@code TIMEOUT}. */
    @JsxConstant
    public static final int TIMEOUT = 3;

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }
}
