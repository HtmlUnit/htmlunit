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
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code Position}.
 *
 * @author Ahmed Ashour
 */
@JsxClass(IE)
public class Position extends SimpleScriptable {

    private Coordinates coordinates_;

    /**
     * Creates an instance.
     */
    public Position() {
    }

    Position(final Coordinates coordinates) {
        coordinates_ = coordinates;
    }

    /**
     * Returns the coordinates.
     * @return the coordinates
     */
    @JsxGetter
    public Coordinates getCoords() {
        return coordinates_;
    }
}
