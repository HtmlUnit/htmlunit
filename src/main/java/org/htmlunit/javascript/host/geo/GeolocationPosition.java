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
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for GeolocationPosition.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class GeolocationPosition extends HtmlUnitScriptable {

    private GeolocationCoordinates coordinates_;

    /**
     * Creates an instance.
     */
    public GeolocationPosition() {
        super();
    }

    GeolocationPosition(final GeolocationCoordinates coordinates) {
        super();
        coordinates_ = coordinates;
    }

    /**
     * Creates an instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        throw JavaScriptEngine.typeErrorIllegalConstructor();
    }

    /**
     * Returns the coordinates.
     * @return the coordinates
     */
    @JsxGetter
    public GeolocationCoordinates getCoords() {
        return coordinates_;
    }
}
