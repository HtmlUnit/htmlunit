/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host.media;

import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * A JavaScript object for {@code AudioParam}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/AudioParam">MDN Documentation</a>
 */
@JsxClass
public class AudioParam extends HtmlUnitScriptable {

    private double value_;

    /**
     * Creates a new instance.
     */
    @JsxConstructor
    public void jsConstructor() {
        value_ = getDefaultValue();
    }

    /**
     * Returns the {@code value} property.
     * @return the value
     */
    @JsxGetter
    public double getValue() {
        return value_;
    }

    /**
     * Sets the {@code value} property.
     * @param value the value
     */
    @JsxSetter
    public void setValue(final double value) {
        value_ = value;
    }

    /**
     * Returns the {@code defaultValue} property.
     * @return the defaultValue
     */
    @JsxGetter
    public double getDefaultValue() {
        return 1;
    }

    /**
     * Returns the {@code maxValue} property.
     * @return the maxValue
     */
    @JsxGetter
    @SuppressWarnings("PMD.UseUnderscoresInNumericLiterals")
    public double getMaxValue() {
        return 3.4028234663852886e+38;
    }

    /**
     * Returns the {@code minValue} property.
     * @return the minValue
     */
    @JsxGetter
    @SuppressWarnings("PMD.UseUnderscoresInNumericLiterals")
    public double getMinValue() {
        return -3.4028234663852886e+38;
    }
}
