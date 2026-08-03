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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import org.htmlunit.SgmlPage;
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
        if (!value.isEmpty() && !StringUtils.containsOnly(value, VALID_CHARS)) {
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
        final String raw = getRawValue();

        if (StringUtils.isBlank(raw)) {
            return "";
        }

        if (StringUtils.equalsChar('-', raw)
                || StringUtils.equalsChar('+', raw)) {
            return raw;
        }

        try {
            final String lang = getPage().getWebClient().getBrowserVersion().getBrowserLanguage();
            final NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag(lang));
            format.parse(raw);

            return raw.trim();
        }
        catch (final ParseException ignored) {
            // ignore
        }

        if (hasFeature(JS_INPUT_NUMBER_ACCEPT_ALL)) {
            return raw;
        }

        return "";
    }

    /**
     * Attempts to parse the current raw value as a well-formed number per
     * this input's syntax rules (sign-only values, missing-step non-integer
     * values, and anything BigDecimal itself rejects all count as
     * unparseable). Centralizes what used to be duplicated, near-identically,
     * across hasRangeOverflowValidityState(), hasRangeUnderflowValidityState(),
     * and isStepMismatchValidityState().
     *
     * @return the parsed value, or {@code null} if the raw value is blank
     *     OR is non-blank but not a well-formed number (the latter case is
     *     what {@link #hasBadInputValidityState()} reports, NOT any of the
     *     range/step methods -- callers here must not treat "unparseable"
     *     as a range/step violation)
     */
    private BigDecimal parseNumericValue() {
        String rawValue = getRawValue();
        if (StringUtils.isBlank(rawValue)) {
            return null;
        }

        if (!hasFeature(JS_INPUT_NUMBER_ACCEPT_ALL)) {
            rawValue = rawValue.replaceAll("\\s", "");
        }
        if (rawValue.isEmpty()) {
            return null;
        }

        if (StringUtils.equalsChar('-', rawValue) || StringUtils.equalsChar('+', rawValue)) {
            return null;
        }

        // if we have no step, the value has to be an integer
        if (getStep().isEmpty()) {
            String val = rawValue;
            final int lastPos = val.length() - 1;
            if (lastPos >= 0 && val.charAt(lastPos) == '.') {
                if (hasFeature(JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE)) {
                    return null;
                }
                val = val.substring(0, lastPos);
            }
            if (!StringUtils.containsOnly(val, VALID_INT_CHARS)) {
                return null;
            }
        }

        try {
            return new BigDecimal(rawValue);
        }
        catch (final NumberFormatException e) {
            return null;
        }
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
     * {@link #hasBadInputValidityState()}'s concern, not a range violation
     * -- distinguished here via {@link #parseNumericValue()} returning
     * {@code null} only for genuinely malformed (non-blank) input.
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

        final BigDecimal value = parseNumericValue();
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

        final BigDecimal value = parseNumericValue();
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

        final BigDecimal value = parseNumericValue();
        if (value == null) {
            return false;
        }

        final BigDecimal step = parseAttributeAsBigDecimal(getStep());
        if (step == null) {
            return false;
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
        return parseNumericValue() == null;
    }
}
