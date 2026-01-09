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
package org.htmlunit.javascript.host.html;

import org.htmlunit.html.DomNode;
import org.htmlunit.html.ValidatableElement;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code ValidityState}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class ValidityState extends HtmlUnitScriptable {

    /**
     * JavaScript constructor.
     */
    @JsxConstructor
    public void jsConstructor() {
        // nothing to do
    }

    private ValidatableElement getValidatableElementOrDie() {
        return (ValidatableElement) getDomNodeOrDie();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        setDomNode(domNode, false);
    }

    /**
     * @return a boolean value that is true if the user has provided
     *         input that the browser is unable to convert.
     */
    @JsxGetter
    public boolean isBadInput() {
        return getValidatableElementOrDie().hasBadInputValidityState();
    }

    /**
     * @return whether the customer validity message is set or not
     */
    @JsxGetter
    public boolean isCustomError() {
        return getValidatableElementOrDie().isCustomErrorValidityState();
    }

    /**
     * Returns whether the element value does not match its {@code pattern} attribute.
     * @return whether the element value does not match its {@code pattern} attribute
     */
    @JsxGetter
    public boolean isPatternMismatch() {
        return getValidatableElementOrDie().hasPatternMismatchValidityState();
    }

    /**
     * Returns whether the element value is greater than its {@code max} attribute.
     * @return whether the element value is greater than its {@code max} attribute
     */
    @JsxGetter
    public boolean isRangeOverflow() {
        return getValidatableElementOrDie().hasRangeOverflowValidityState();
    }

    /**
     * Returns whether the element value is less than its {@code min} attribute.
     * @return whether the element value is less than its {@code min} attribute
     */
    @JsxGetter
    public boolean isRangeUnderflow() {
        return getValidatableElementOrDie().hasRangeUnderflowValidityState();
    }

    /**
     * Returns whether the element value is invalid per its {@code step} attribute.
     * @return whether the element value is invalid per its {@code step} attribute
     */
    @JsxGetter
    public boolean isStepMismatch() {
        return getValidatableElementOrDie().isStepMismatchValidityState();
    }

    /**
     * Returns whether the element value exceeds its {@code maxLength} attribute.
     * @return whether the element value exceeds its {@code maxLength} attribute
     */
    @JsxGetter
    public boolean isTooLong() {
        return getValidatableElementOrDie().isTooLongValidityState();
    }

    /**
     * Returns whether the element value exceeds its {@code minLength} attribute.
     * @return whether the element value exceeds its {@code minLength} attribute
     */
    @JsxGetter
    public boolean isTooShort() {
        return getValidatableElementOrDie().isTooShortValidityState();
    }

    /**
     * Returns whether the element value is invalid per its {@code type} attribute.
     * @return whether the element value is invalid per its {@code type} attribute
     */
    @JsxGetter
    public boolean isTypeMismatch() {
        return getValidatableElementOrDie().hasTypeMismatchValidityState();
    }

    /**
     * Returns whether the element (with a {@code required} attribute) has no value.
     * @return whether the element (with a {@code required} attribute) has no value
     */
    @JsxGetter
    public boolean isValueMissing() {
        return getValidatableElementOrDie().isValueMissingValidityState();
    }

    /**
     * Returns whether the element value is valid.
     * @return whether the element value is valid
     */
    @JsxGetter
    public boolean isValid() {
        return getValidatableElementOrDie().isValidValidityState();
    }
}
