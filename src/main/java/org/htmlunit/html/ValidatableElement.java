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

/**
 * An element that supports client-side validation using the Constraint Validation API.
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Constraint_validation">
 *      Constraint Validation API</a>
 *
 * @author Ronald Brill
 */
public interface ValidatableElement {

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
}
