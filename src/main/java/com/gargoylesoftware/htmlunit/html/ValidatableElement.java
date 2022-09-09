/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

/**
 * An element that supports client side validation based on
 * the Constraint validation API.
 * see https://developer.mozilla.org/en-US/docs/Web/API/Constraint_validation
 *
 * @author Ronald Brill
 */
public interface ValidatableElement {

    /**
     * @return whether the element is a candidate for constraint validation
     */
    boolean willValidate();

    /**
     * Sets the custom validity message for the element to the specified message.
     * @param message the new message
     */
    void setCustomValidity(String message);

    /**
     * @return a boolean value boolean value that is true if the user
     * has provided input that the browser is unable to convert.
     */
    default boolean hasBadInputValidityState() {
        return false;
    }

    /**
     * @return a boolean value indicating whether the element's custom validity message
     * has been set to a non-empty string by calling the element's setCustomValidity() method.
     */
    boolean isCustomErrorValidityState();

    /**
     * @return true if the value does not match the specified pattern,
     * and false if it does match.
     * If true, the element matches the :invalid CSS pseudo-class
     */
    default boolean hasPatternMismatchValidityState() {
        return false;
    }

    /**
     * @return true if the value does not fit the rules determined by the step attribute
     * (that is, it's not evenly divisible by the step value),
     * or false if it does fit the step rule.
     * If true, the element matches the :invalid and :out-of-range CSS pseudo-classes.
     */
    default boolean isStepMismatchValidityState() {
        return false;
    }

    /**
     * @return true if the value is longer than the maximum length specified
     * by the maxlength attribute, or false if it is shorter than or equal to the maximum.
     * If true, the element matches the :invalid CSS pseudo-class
     */
    default boolean isTooLongValidityState() {
        return false;
    }

    /**
     * @return true if the value is shorter than the minimum length specified
     * by the minlength attribute, or false if it is greater than or equal to the minimum.
     * If true, the element matches the :invalid CSS pseudo-class
     */
    default boolean isTooShortValidityState() {
        return false;
    }

    /**
     * @return true if the value is not in the required syntax (when type is email or url),
     * or false if the syntax is correct.
     * If true, the element matches the :invalid CSS pseudo-class.
     */
    default boolean hasTypeMismatchValidityState() {
        return false;
    }

    /**
     * @return true if the value is greater than the maximum specified by the max attribute,
     * or false if it is less than or equal to the maximum.
     * If true, the element matches the :invalid and :out-of-range CSS pseudo-classes.
     */
    default boolean hasRangeOverflowValidityState() {
        return false;
    }

    /**
     * @return true if the value is less than the minimum specified by the min attribute,
     * or false if it is greater than or equal to the minimum.
     * If true, the element matches the :invalid and :out-of-range CSS pseudo-classes.
     */
    default boolean hasRangeUnderflowValidityState() {
        return false;
    }

    /**
     * @return true if the element meets all its validation constraints, and is therefore
     * considered to be valid, or false if it fails any constraint.
     * If true, the element matches the :valid CSS pseudo-class; the :invalid CSS pseudo-class otherwise.
     */
    boolean isValidValidityState();

    /**
     * @return true if the element has a required attribute, but no value, or false otherwise.
     * If true, the element matches the :invalid CSS pseudo-class.
     */
    default boolean isValueMissingValidityState() {
        return false;
    }
}
