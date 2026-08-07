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

import org.htmlunit.html.HtmlElement;
import org.htmlunit.html.ValidatableHtmlElement;
import org.htmlunit.javascript.host.event.Event;

/**
 * An {@link HTMLElement} that supports client-side validation using the Constraint Validation API.
 * This works together with {@link ValidatableHtmlElement}.
 * <p>
 * Shared implementation of the HTML Constraint Validation API's
 * checkValidity()/reportValidity() behavior, for JS host objects wrapping
 * an {@link ValidatableHtmlElement}. Centralizes 'invalid'
 * event dispatch and the interactive-vs-static distinction between the two
 * methods (reportValidity() additionally focuses the element on failure)
 * so this isn't duplicated per element type -- each implementing class
 * only needs to supply the underlying DOM element.
 * </p>
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/Constraint_validation">
 *      Constraint Validation API</a>
 *
 * @author Ronald Brill
 */
public interface ValidatableHTMLElement {

    /**
     * Statically validates the underlying element's constraints, firing a
     * cancelable 'invalid' event on it if invalid. Does not move focus.
     * @param elem the {@link HtmlElement} to work on
     * @return {@code true} if valid or barred from validation, {@code false} otherwise
     */
    static boolean doCheckValidity(final HtmlElement elem) {
        if (!(elem instanceof ValidatableHtmlElement valElem)) {
            return true;
        }

        if (!valElem.willValidate() || elem.isValid()) {
            return true;
        }

        elem.fireEvent(Event.TYPE_INVALID);
        return false;
    }

    /**
     * Interactively validates -- same static check and 'invalid' event as
     * {@link #doCheckValidity(HtmlElement)}, but additionally focuses the element if
     * it's invalid.
     * @param elem the {@link HtmlElement} to work on
     * @return {@code true} if valid or barred from validation, {@code false} otherwise
     */
    static boolean doReportValidity(final HtmlElement elem) {
        final boolean valid = doCheckValidity(elem);
        if (!valid) {
            elem.focus();
        }
        return valid;
    }

    /**
     * Returns the validation message describing the currently failing
     * constraint, or "" if valid or barred from validation.
     * @param elem the {@link HtmlElement} to work on
     * @return the validation message
     */
    static String getValidationMessage(final HtmlElement elem) {
        if (!(elem instanceof ValidatableHtmlElement valElem)) {
            return "";
        }
        return valElem.getValidationMessage();
    }
}
