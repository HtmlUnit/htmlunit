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

import org.htmlunit.javascript.host.html.ValidatableHTMLElement;

/**
 * An {@link HtmlElement} that supports client-side validation using the Constraint Validation API.
 * This uses {@link ValidatableHTMLElement}.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Constraint_validation">
 *      Constraint Validation API</a>
 *
 * @author Ronald Brill
 */
public interface ValidatableHtmlElement {

    /**
     * Returns whether the element is a candidate for constraint validation.
     *
     * @return whether the element is a candidate for constraint validation
     */
    boolean willValidate();

    /**
     * Sets the custom validity message for the element.
     *
     * @param message the new message
     */
    void setCustomValidity(String message);

    /**
     * Returns whether the user has provided input that the browser is unable to convert.
     *
     * @return {@code true} if the user has provided input that the browser is unable to convert
     */
    default boolean hasBadInputValidityState() {
        return false;
    }

    /**
     * Returns whether the element has a custom validity message.
     *
     * @return {@code true} if the element's custom validity message has been set to a non-empty
     *         string by calling {@link #setCustomValidity(String)}
     */
    boolean isCustomErrorValidityState();

    /**
     * Returns whether the element's value does not match the specified pattern.
     *
     * @return {@code true} if the value does not match the specified pattern;
     *         if {@code true}, the element matches the {@code :invalid} CSS pseudo-class
     */
    default boolean hasPatternMismatchValidityState() {
        return false;
    }

    /**
     * Returns whether the element's value does not satisfy the {@code step} constraint.
     *
     * @return {@code true} if the value does not fit the rules determined by the
     *         {@code step} attribute; if {@code true}, the element matches the
     *         {@code :invalid} and {@code :out-of-range} CSS pseudo-classes
     */
    default boolean isStepMismatchValidityState() {
        return false;
    }

    /**
     * Returns whether the element's value exceeds the maximum allowed length.
     *
     * @return {@code true} if the value is longer than the maximum length specified by the
     *         {@code maxlength} attribute; if {@code true}, the element matches the
     *         {@code :invalid} CSS pseudo-class
     */
    default boolean isTooLongValidityState() {
        return false;
    }

    /**
     * Returns whether the element's value is shorter than the minimum required length.
     *
     * @return {@code true} if the value is shorter than the minimum length specified by the
     *         {@code minlength} attribute; if {@code true}, the element matches the
     *         {@code :invalid} CSS pseudo-class
     */
    default boolean isTooShortValidityState() {
        return false;
    }

    /**
     * Returns whether the element's value has a type mismatch.
     *
     * @return {@code true} if the value is not in the required syntax (for example,
     *         for {@code email} or {@code url}); if {@code true}, the element matches the
     *         {@code :invalid} CSS pseudo-class
     */
    default boolean hasTypeMismatchValidityState() {
        return false;
    }

    /**
     * Returns whether the element's value exceeds the maximum allowed value.
     *
     * @return {@code true} if the value is greater than the maximum specified by the
     *         {@code max} attribute; if {@code true}, the element matches the
     *         {@code :invalid} and {@code :out-of-range} CSS pseudo-classes
     */
    default boolean hasRangeOverflowValidityState() {
        return false;
    }

    /**
     * Returns whether the element's value is less than the minimum allowed value.
     *
     * @return {@code true} if the value is less than the minimum specified by the
     *         {@code min} attribute; if {@code true}, the element matches the
     *         {@code :invalid} and {@code :out-of-range} CSS pseudo-classes
     */
    default boolean hasRangeUnderflowValidityState() {
        return false;
    }

    /**
     * Returns whether the element satisfies all validation constraints.
     *
     * @return {@code true} if the element is valid; if {@code true}, the element matches the
     *         {@code :valid} CSS pseudo-class, otherwise the {@code :invalid} CSS pseudo-class
     */
    boolean isValidValidityState();

    /**
     * Returns whether the element is missing a required value.
     *
     * @return {@code true} if the element has a required attribute but no value;
     *         if {@code true}, the element matches the {@code :invalid} CSS pseudo-class
     */
    default boolean isValueMissingValidityState() {
        return false;
    }

    /**
     * Returns the raw custom validity message set via {@link #setCustomValidity(String)},
     * or the empty string if none has been set.
     * @return the custom validity message
     */
    String getCustomValidity();

    /**
     * Returns the (potentially control-specific) message describing the
     * currently failing constraint, in spec priority order:
     * customError, patternMismatch, rangeOverflow, rangeUnderflow,
     * stepMismatch, tooLong, tooShort, typeMismatch, badInput, valueMissing.
     * Returns "" if barred from validation or currently valid.
     * @return the validation message
     */
    default String getValidationMessage() {
        if (!willValidate() || isValidValidityState()) {
            return "";
        }
        if (isCustomErrorValidityState()) {
            return getCustomValidity();
        }
        if (hasPatternMismatchValidityState()) {
            return getPatternMismatchMessage();
        }
        if (hasRangeOverflowValidityState()) {
            return getRangeOverflowMessage();
        }
        if (hasRangeUnderflowValidityState()) {
            return getRangeUnderflowMessage();
        }
        if (isStepMismatchValidityState()) {
            return getStepMismatchMessage();
        }
        if (isTooLongValidityState()) {
            return getTooLongMessage();
        }
        if (isTooShortValidityState()) {
            return getTooShortMessage();
        }
        if (hasTypeMismatchValidityState()) {
            return getTypeMismatchMessage();
        }
        if (hasBadInputValidityState()) {
            return getBadInputMessage();
        }
        if (isValueMissingValidityState()) {
            return getValueMissingMessage();
        }
        return "";
    }

    /**
     * Returns the message describing a pattern mismatch (the element's value
     * does not match the constraint specified by the {@code pattern}
     * attribute). Generic, uniform wording -- override in a concrete class
     * that can interpolate control-specific details (e.g. the {@code pattern}
     * itself or a {@code title} hint) into the message.
     * @return the pattern mismatch message
     */
    default String getPatternMismatchMessage() {
        return "Please match the requested format.";
    }

    /**
     * Returns the message describing a range overflow (the element's value is
     * greater than the maximum specified by the {@code max} attribute).
     * Generic, uniform wording -- override in a concrete class that can
     * interpolate the actual {@code max} value into the message.
     * @return the range overflow message
     */
    default String getRangeOverflowMessage() {
        return "Value must be less than or equal to the maximum.";
    }

    /**
     * Returns the message describing a range underflow (the element's value is
     * less than the minimum specified by the {@code min} attribute). Generic,
     * uniform wording -- override in a concrete class that can interpolate the
     * actual {@code min} value into the message.
     * @return the range underflow message
     */
    default String getRangeUnderflowMessage() {
        return "Value must be greater than or equal to the minimum.";
    }

    /**
     * Returns the message describing a step mismatch (the element's value does
     * not satisfy the constraint specified by the {@code step} attribute).
     * Generic fallback wording -- real browsers typically also name the two
     * nearest step-aligned valid values, which this default does not attempt
     * to compute; override in a concrete class if that level of detail is
     * needed.
     * @return the step mismatch message
     */
    default String getStepMismatchMessage() {
        return "Please enter a valid value.";
    }

    /**
     * Returns the message describing a too-long value (the element's value is
     * longer than the maximum length specified by the {@code maxlength}
     * attribute). Generic, uniform wording -- override in a concrete class
     * that can interpolate the actual {@code maxlength} and current length
     * into the message.
     * @return the too-long message
     */
    default String getTooLongMessage() {
        return "Please shorten this text.";
    }

    /**
     * Returns the message describing a too-short value (the element's value is
     * shorter than the minimum length specified by the {@code minlength}
     * attribute). Generic, uniform wording -- override in a concrete class
     * that can interpolate the actual {@code minlength} and current length
     * into the message.
     * @return the too-short message
     */
    default String getTooShortMessage() {
        return "Please lengthen this text.";
    }

    /**
     * Returns the message describing a type mismatch (the element's value is
     * not in the syntax required by its {@code type}, e.g. {@code email} or
     * {@code url}). Generic, uniform wording -- override in a concrete class
     * that can produce a type-specific message.
     * @return the type mismatch message
     */
    default String getTypeMismatchMessage() {
        return "Please enter a valid value.";
    }

    /**
     * Returns the message describing bad input (the user has provided input
     * that the browser is unable to convert, e.g. non-numeric text typed into
     * a {@code number} input). Generic, uniform wording.
     * @return the bad input message
     */
    default String getBadInputMessage() {
        return "Please enter a valid value.";
    }

    /**
     * Returns the message describing a missing required value (the element has
     * a {@code required} attribute but no value). Generic, uniform wording.
     * @return the value missing message
     */
    default String getValueMissingMessage() {
        return "Please fill out this field.";
    }
}
