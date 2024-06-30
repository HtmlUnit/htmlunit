/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.canvas;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;

/**
 * A JavaScript object for {@code CanvasGradient}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class CanvasGradient extends HtmlUnitScriptable {

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
    }

    /**
     * Adds a new stop, defined by an offset and a color, to the gradient.
     * @param offset A number between 0 and 1. An INDEX_SIZE_ERR is raised,
     * if the number is not in that range.
     * @param color A CSS color. A SYNTAX_ERR is raised, if the value
     * can not be parsed as a CSS color value.
     */
    @JsxFunction
    public void addColorStop(final double offset, final String color) {
        // empty
    }
}
