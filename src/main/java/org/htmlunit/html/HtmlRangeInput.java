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
package org.htmlunit.html;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.htmlunit.SgmlPage;
import org.htmlunit.util.StringUtils;

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
                unmarkValueDirty();
                return;
            }

            final double val = min + ((max - min) / 2);
            setValue(val);
            unmarkValueDirty();
        }
        else {
            setValue(value);
            unmarkValueDirty();
        }
    }

    /**
     * Returns the numeric value of the {@code min} attribute.
     *
     * @return the value of the {@code min} attribute as a {@code double}, or
     *         {@code 0} if the attribute is not defined or cannot be parsed
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
     * Returns the numeric value of the {@code max} attribute.
     *
     * @return the value of the {@code max} attribute as a {@code double}, or
     *         {@code 100} if the attribute is not defined or cannot be parsed
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
     * Returns the numeric value of the {@code step} attribute.
     *
     * @return the value of the {@code step} attribute as a {@code double}, or
     *         {@code 1} if the attribute is not defined or cannot be parsed
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
            if (!StringUtils.isEmptyOrNull(newValue)) {
                setValue(Double.parseDouble(newValue));
            }
            else {
                final double min = getMinNumeric();
                final double max = getMaxNumeric();

                // place in the middle
                setValue(min + ((max - min) / 2));
            }
        }
        catch (final NumberFormatException ignored) {
            // ignore
        }
    }

    private void setValue(final double newValue) {
        super.setValue(toValueString(newValue));
    }

    @Override
    protected void valueAttributeChanged(final String attributeValue, final boolean isValueDirty) {
        if (isValueDirty) {
            return;
        }

        try {
            if (!org.htmlunit.util.StringUtils.isEmptyOrNull(attributeValue)) {
                setRawValue(Double.parseDouble(attributeValue));
            }
            else {
                final double min = getMinNumeric();
                final double max = getMaxNumeric();

                // place in the middle
                setRawValue(min + ((max - min) / 2));
            }
        }
        catch (final NumberFormatException ignored) {
            // ignore
        }
    }

    private void setRawValue(final double newValue) {
        setRawValue(toValueString(newValue));
    }

    /**
     * Clamps the given value to min/max, aligns it to the step and converts it into the string
     * representation used as value.
     *
     * @param newValue the value
     * @return the value string
     */
    private String toValueString(final double newValue) {
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
        if (Double.isFinite(value) && Double.isFinite(min) && Double.isFinite(step) && step > 0) {
            // decimal arithmetic; with doubles step 0.1 results in values like 0.30000000000000004
            // and the rounding of 0.25 to the step is wrong (0.2 instead of 0.3)
            final BigDecimal decimalMin = BigDecimal.valueOf(min);
            final BigDecimal decimalStep = BigDecimal.valueOf(step);
            final BigDecimal diff = BigDecimal.valueOf(value).subtract(decimalMin);
            // round to the nearest step (ties up); below min (max < min) truncate like before
            final BigDecimal steps = diff.divide(decimalStep, 0,
                    diff.signum() < 0 ? RoundingMode.DOWN : RoundingMode.HALF_UP);
            BigDecimal result = decimalMin.add(decimalStep.multiply(steps));
            if (Double.isFinite(max) && result.compareTo(BigDecimal.valueOf(max)) > 0 && steps.signum() > 0) {
                // the largest value matching the step that is not greater than max
                result = result.subtract(decimalStep);
            }
            return format(result.doubleValue());
        }

        value = value - min;
        long fact = (long) (value / step);
        final double rest = value % step;
        if (rest >= step / 2) {
            fact++;
        }
        value = min + step * fact;

        return format(value);
    }

    /**
     * Formats the given number; integral values are formatted without fraction digits
     * (also outside of the int range).
     *
     * @param value the number
     * @return the string representation
     */
    private static String format(final double value) {
        if (Double.isFinite(value)) {
            final BigDecimal decimal = BigDecimal.valueOf(value).stripTrailingZeros();
            if (decimal.scale() <= 0) {
                return decimal.toPlainString();
            }
        }
        return Double.toString(value);
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
            catch (final NumberFormatException ignored) {
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
            catch (final NumberFormatException ignored) {
                // ignore
            }
        }
        return true;
    }
}
