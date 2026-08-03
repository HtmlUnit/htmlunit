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

import java.io.Serializable;
import java.util.function.Predicate;

import org.htmlunit.html.DomElement;
import org.htmlunit.html.DomNode;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.html.HtmlFieldSet;
import org.htmlunit.html.HtmlForm;
import org.htmlunit.html.HtmlInput;
import org.htmlunit.html.HtmlObject;
import org.htmlunit.html.HtmlOutput;
import org.htmlunit.html.HtmlSelect;
import org.htmlunit.html.HtmlTextArea;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;

/**
 * The JavaScript object {@code HTMLFieldSetElement}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 *
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLFieldSetElement">MDN Documentation</a>
 */
@JsxClass(domClass = HtmlFieldSet.class)
public class HTMLFieldSetElement extends HTMLElement {

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Returns the {@code name} attribute.
     * @return the {@code name} attribute
     */
    @JsxGetter
    @Override
    public String getName() {
        return getDomNodeOrDie().getAttributeDirect(DomElement.NAME_ATTRIBUTE);
    }

    /**
     * Sets the {@code name} attribute.
     * @param name the {@code name} attribute value
     */
    @JsxSetter
    @Override
    public void setName(final String name) {
        getDomNodeOrDie().setAttribute(DomElement.NAME_ATTRIBUTE, name);
    }

    /**
     * Returns the value of the JavaScript {@code form} attribute.
     *
     * @return the value of the JavaScript {@code form} attribute
     */
    @JsxGetter
    @Override
    public HTMLFormElement getForm() {
        final HtmlForm form = getDomNodeOrDie().getEnclosingForm();
        if (form == null) {
            return null;
        }
        return (HTMLFormElement) getScriptableFor(form);
    }

    /**
     * Returns the {@code type} property; always "fieldset".
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return "fieldset";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlFieldSet getDomNodeOrDie() {
        return (HtmlFieldSet) super.getDomNodeOrDie();
    }

    /**
     * Checks whether the element has any constraints and whether it satisfies them.
     * @return {@code true} if the element is valid
     */
    @JsxFunction
    public boolean checkValidity() {
        return ValidatableHTMLElement.doCheckValidity(getDomNodeOrDie());
    }

    /**
     * Performs the same validity checking steps as the checkValidity() method.
     * @return {@code true} if the element is valid
     */
    @JsxFunction
    public boolean reportValidity() {
        return ValidatableHTMLElement.doReportValidity(getDomNodeOrDie());
    }

    /**
     * Returns the message describing why the element's value fails constraint
     * validation, or "" if it's valid or barred from validation.
     * @return the validation message
     */
    @JsxGetter
    public String getValidationMessage() {
        return ValidatableHTMLElement.getValidationMessage(getDomNodeOrDie());
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public boolean isDisabled() {
        return super.isDisabled();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter
    public void setDisabled(final boolean disabled) {
        super.setDisabled(disabled);
    }

    /**
     * Returns a {@link ValidityState} object representing the validity states of this element.
     * @return a {@link ValidityState} object representing the validity states of this element
     */
    @JsxGetter
    public ValidityState getValidity() {
        final ValidityState validityState = new ValidityState();
        validityState.setPrototype(getPrototype(validityState.getClass()));
        validityState.setParentScope(getParentScope());
        validityState.setDomNode(getDomNodeOrDie());
        return validityState;
    }

    /**
     * Returns whether the element is a candidate for constraint validation.
     * @return whether the element is a candidate for constraint validation
     */
    @JsxGetter
    public boolean isWillValidate() {
        return getDomNodeOrDie().willValidate();
    }

    /**
     * Sets the custom validity message for the element to the specified message.
     * @param message the new message
     */
    @JsxFunction
    public void setCustomValidity(final String message) {
        getDomNodeOrDie().setCustomValidity(message);
    }

    /**
     * Returns the fieldset's associated form controls -- listed elements whose
     * closest fieldset element ancestor is this fieldset.
     * <p>
     * Per spec, this is purely a tree-position question: it does not consult
     * the 'form' attribute or form ownership at all (a control physically
     * inside this fieldset is included even if 'form' points elsewhere), and a
     * nested inner &lt;fieldset&gt;'s own descendants are excluded here even
     * though they're still tree-descendants of this fieldset -- only the inner
     * fieldset ELEMENT itself counts, since it is the listed element whose
     * closest fieldset ancestor is this one.
     * </p>
     *
     * @return the fieldset's associated form controls
     * @see <a href="https://developer.mozilla.org/en-US/docs/Web/API/HTMLFieldSetElement/elements">MDN Documentation</a>
     */
    @JsxGetter
    public HTMLCollection getElements() {
        final DomElement elt = getDomNodeOrDie();

        final HTMLCollection elements = new HTMLCollection(elt, true);

        elements.setIsMatchingPredicate((Predicate<DomNode> & Serializable) node -> isListedElement(node));

        return elements;
    }

    /**
     * Checks whether {@code node} belongs to the HTML "listed" category of
     * form-associated elements (button, fieldset, input except type=image,
     * object, output, select, textarea).
     *
     * @param node the node to check
     * @return {@code true} if {@code node} is a listed element
     */
    private static boolean isListedElement(final DomNode node) {
        return node instanceof HtmlInput
                || node instanceof HtmlButton
                || node instanceof HtmlFieldSet
                || node instanceof HtmlObject
                || node instanceof HtmlOutput
                || node instanceof HtmlSelect
                || node instanceof HtmlTextArea;
    }
}
