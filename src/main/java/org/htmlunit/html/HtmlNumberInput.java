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

import static org.htmlunit.BrowserVersionFeatures.JS_INPUT_NUMBER_ACCEPT_ALL;
import static org.htmlunit.BrowserVersionFeatures.JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE;

import java.math.BigDecimal;
import java.util.Map;

import org.htmlunit.SgmlPage;
import org.htmlunit.html.parser.HtmlNumberParser;
import org.htmlunit.util.ArrayUtils;
import org.htmlunit.util.StringUtils;

/**
 * Wrapper for the HTML element "input" with type is "number".
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 * @author Raik Bieniek
 * @author Michael Lueck
 */
public class HtmlNumberInput extends HtmlSelectableTextInput implements LabelableElement {

    private static final char[] VALID_INT_CHARS = "0123456789-".toCharArray();
    private static final char[] VALID_CHARS = "0123456789-+.eE".toCharArray();

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlNumberInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        final String value = getValueAttribute();
        if (!value.isEmpty()
                // Firefox is more lenient when parsing the INITIAL 'value' ATTRIBUTE specifically
                && !HtmlNumberParser.isValid(value, false, hasFeature(JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE))) {

            setRawValue("");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isSubmittableByEnter() {
        return true;
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
    protected void doType(final char c, final boolean lastType) {
        if (!hasFeature(JS_INPUT_NUMBER_ACCEPT_ALL) && !ArrayUtils.contains(VALID_CHARS, c)) {
            return;
        }
        super.doType(c, lastType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        String raw = getRawValue();

        if (StringUtils.isBlank(raw)) {
            return "";
        }

        if (StringUtils.equalsChar('-', raw) || StringUtils.equalsChar('+', raw)) {
            return raw;
        }

        // Confirmed via real-browser testing (typeIntegerWithDot): mid-typing a
        // trailing '.' with otherwise-valid digits before it (e.g. "1.") reports
        // the pre-dot value ("1"), not "" and not the raw "1."
        if (raw.endsWith(".")) {
            raw = raw.substring(0, raw.length() - 1);
        }

        if (!HtmlNumberParser.isValid(raw, !hasFeature(JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE), false)) {
            return "";
        }

        if (raw.length() > 1 && raw.charAt(0) == '+') {
            raw = raw.substring(1, raw.length());
        }

        return raw;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(final String newValue) {
        if (StringUtils.isBlank(newValue) || !HtmlNumberParser.isValid(newValue, false, false)) {
            super.setValue("");
            return;
        }

        super.setValue(newValue);
    }

    /**
     * Parses an attribute string (min/max/step) as a BigDecimal, or
     * {@code null} if absent or malformed -- consolidates the several
     * separately-inlined try/catch blocks that used to parse min/max/step
     * individually in each constraint method.
     */
    private static BigDecimal parseAttributeAsBigDecimal(final String attributeValue) {
        if (attributeValue.isEmpty()) {
            return null;
        }
        try {
            return new BigDecimal(attributeValue);
        }
        catch (final NumberFormatException ignored) {
            return null;
        }
    }

    /**
     * {@inheritDoc}
     * A raw value that isn't even a well-formed number is
     * {@link #hasBadInputValidityState()}'s concern, not a range violation.
     */
    @Override
    public boolean hasRangeOverflowValidityState() {
        if (super.hasRangeOverflowValidityState()) {
            return true;
        }

        final String rawValue = getRawValue();
        if (StringUtils.isBlank(rawValue)) {
            return false;
        }

        final BigDecimal value = HtmlNumberParser.parse(rawValue, true, true);
        if (value == null) {
            return false;
        }

        final BigDecimal max = parseAttributeAsBigDecimal(getMax());
        return max != null && value.compareTo(max) > 0;
    }

    /**
     * {@inheritDoc}
     * See {@link #hasRangeOverflowValidityState()} for the malformed-value
     * / badInput distinction.
     */
    @Override
    public boolean hasRangeUnderflowValidityState() {
        if (super.hasRangeUnderflowValidityState()) {
            return true;
        }

        final String rawValue = getRawValue();
        if (StringUtils.isBlank(rawValue)) {
            return false;
        }

        final BigDecimal value = HtmlNumberParser.parse(rawValue, true, true);
        if (value == null) {
            return false;
        }

        final BigDecimal min = parseAttributeAsBigDecimal(getMin());
        return min != null && value.compareTo(min) < 0;
    }

    /**
     * {@inheritDoc}
     * See {@link #hasRangeOverflowValidityState()} for the malformed-value
     * / badInput distinction.
     */
    @Override
    public boolean isStepMismatchValidityState() {
        if (super.isStepMismatchValidityState()) {
            return true;
        }

        final String rawValue = getRawValue();
        if (StringUtils.isBlank(rawValue)) {
            return false;
        }

        final BigDecimal value = HtmlNumberParser.parse(rawValue, true, true);
        if (value == null) {
            return false;
        }

        BigDecimal step = parseAttributeAsBigDecimal(getStep());
        if (step == null) {
            step = BigDecimal.ONE;
        }

        BigDecimal min = parseAttributeAsBigDecimal(getMin());
        if (min == null) {
            min = BigDecimal.ZERO;
        }

        return value.subtract(min).abs().remainder(step).doubleValue() > 0.0;
    }

    /**
     * {@inheritDoc}
     * The new home for the malformed-value case that used to make
     * hasRangeOverflowValidityState()/hasRangeUnderflowValidityState()/
     * isStepMismatchValidityState() all incorrectly return {@code true}
     * simultaneously.
     */
    @Override
    public boolean hasBadInputValidityState() {
        final String rawValue = getRawValue();
        if (StringUtils.isBlank(rawValue)) {
            return false;
        }

        return !HtmlNumberParser.isValid(rawValue, !hasFeature(JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE), true);
    }
}
