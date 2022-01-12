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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF_ESR;

import com.gargoylesoftware.htmlunit.html.ValidatableElement;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;

/**
 * A JavaScript object for {@code ValidityState}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class ValidityState extends SimpleScriptable {

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF_ESR})
    public ValidityState() {
    }

    private ValidatableElement getValidatableElementOrDie() {
        return (ValidatableElement) getDomNodeOrDie();
    }

    /**
     * @return a boolean value that is true if the user has provided
     * input that the browser is unable to convert.
     */
    @JsxGetter({CHROME, EDGE, FF, FF_ESR})
    public boolean isBadInput() {
        return getValidatableElementOrDie().hasBadInput();
    }

    /**
     * @return whether the customer validity message is set or not
     */
    @JsxGetter
    public boolean isCustomError() {
        return getValidatableElementOrDie().isCustomError();
    }

    /**
     * Returns whether the element value does not match its {@code pattern} attribute.
     * @return whether the element value does not match its {@code pattern} attribute
     */
    @JsxGetter
    public boolean isPatternMismatch() {
        return getValidatableElementOrDie().hasPatternMismatch();
    }

    /**
     * Returns whether the element value is greater than its {@code max} attribute.
     * @return whether the element value is greater than its {@code max} attribute
     */
    @JsxGetter
    public boolean isRangeOverflow() {
        return getValidatableElementOrDie().hasRangeOverflow();
    }

    /**
     * Returns whether the element value is less than its {@code min} attribute.
     * @return whether the element value is less than its {@code min} attribute
     */
    @JsxGetter
    public boolean isRangeUnderflow() {
        return getValidatableElementOrDie().hasRangeUnderflow();
    }

    /**
     * Returns whether the element value is invalid per its {@code step} attribute.
     * @return whether the element value is invalid per its {@code step} attribute
     */
    @JsxGetter
    public boolean isStepMismatch() {
        return getValidatableElementOrDie().isStepMismatch();
    }

    /**
     * Returns whether the element value exceeds its {@code maxLength} attribute.
     * @return whether the element value exceeds its {@code maxLength} attribute
     */
    @JsxGetter
    public boolean isTooLong() {
        return getValidatableElementOrDie().isTooLong();
    }

    /**
     * Returns whether the element value exceeds its {@code minLength} attribute.
     * @return whether the element value exceeds its {@code minLength} attribute
     */
    @JsxGetter({CHROME, EDGE, FF, FF_ESR})
    public boolean isTooShort() {
        return getValidatableElementOrDie().isTooShort();
    }

    /**
     * Returns whether the element value is invalid per its {@code type} attribute.
     * @return whether the element value is invalid per its {@code type} attribute
     */
    @JsxGetter
    public boolean isTypeMismatch() {
        return getValidatableElementOrDie().hasTypeMismatch();
    }

    /**
     * Returns whether the element (with a {@code required} attribute) has no value.
     * @return whether the element (with a {@code required} attribute) has no value
     */
    @JsxGetter
    public boolean isValueMissing() {
        return getValidatableElementOrDie().isValueMissing();
    }

    /**
     * Returns whether the element value is valid.
     * @return whether the element value is valid
     */
    @JsxGetter
    public boolean isValid() {
        return getValidatableElementOrDie().isValid();
    }
}
