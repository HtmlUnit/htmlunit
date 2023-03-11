/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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
package org.htmlunit.html;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" where type is "range".
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 */
public class HtmlRangeInput extends HtmlInput implements LabelableElement {

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlRangeInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        final String value = getValueAttribute();
        if (value == ATTRIBUTE_NOT_DEFINED) {
            final double min = getMinNumeric();
            final double max = getMaxNumeric();
            if (max < min) {
                setValue(min);
                return;
            }

            final double val = min + ((max - min) / 2);
            setValue(val);
        }
        else {
            setValue(value);
        }
    }

    /**
     * @return the min as double
     */
    public double getMinNumeric() {
        final String min = getAttributeDirect("min");
        if (min == ATTRIBUTE_NOT_DEFINED) {
            return 0;
        }
        try {
            return Double.parseDouble(min);
        }
        catch (final NumberFormatException e) {
            return 0;
        }
    }

    /**
     * @return the max as double
     */
    public double getMaxNumeric() {
        final String max = getAttributeDirect("max");
        if (max == ATTRIBUTE_NOT_DEFINED) {
            return 100;
        }
        try {
            return Double.parseDouble(max);
        }
        catch (final NumberFormatException e) {
            return 100;
        }
    }

    /**
     * @return the max as double
     */
    public double getStepNumeric() {
        final String step = getAttributeDirect("step");
        if (step == ATTRIBUTE_NOT_DEFINED) {
            return 1;
        }
        try {
            return Double.parseDouble(step);
        }
        catch (final NumberFormatException e) {
            return 1;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(final String newValue) {
        try {
            if (StringUtils.isNotEmpty(newValue)) {
                setValue(Double.parseDouble(newValue));
            }
            else {
                final double min = getMinNumeric();
                final double max = getMaxNumeric();

                // place in the middle
                setValue(min + ((max - min) / 2));
            }
        }
        catch (final NumberFormatException e) {
            // ignore
        }
    }

    private void setValue(final double newValue) {
        double value = newValue;

        final double min = getMinNumeric();
        final double max = getMaxNumeric();

        if (value > max) {
            value = max;
        }
        else {
            if (value < min) {
                value = min;
            }
        }

        final double step = getStepNumeric();
        value = value - min;
        int fact = (int) (value / step);
        final double rest = value % step;
        if (rest >= step / 2) {
            fact++;
        }
        value = min + step * fact;

        if (!Double.isInfinite(value) && (value == Math.floor(value))) {
            super.setValue(Integer.toString((int) value));
        }
        else {
            super.setValue(Double.toString(value));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isRequiredSupported() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return super.isValid() && isMaxValid() && isMinValid();
    }

    /**
     * Returns if the input element has a valid min value. Refer to the
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a valid min value
     */
    private boolean isMinValid() {
        if (!getRawValue().isEmpty() && !getMin().isEmpty()) {
            try {
                final double value = Double.parseDouble(getRawValue());
                return getMinNumeric() <= value;
            }
            catch (final NumberFormatException e) {
                // ignore
            }
        }
        return true;
    }

    /**
     * Returns if the input element has a valid max value. Refer to the
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a valid max value
     */
    private boolean isMaxValid() {
        if (!getRawValue().isEmpty() && !getMax().isEmpty()) {
            try {
                final int value = Integer.parseInt(getRawValue());
                return getMaxNumeric() >= value;
            }
            catch (final NumberFormatException e) {
                // ignore
            }
        }
        return true;
    }
}
