/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import org.htmlunit.html.DomElement;
import org.htmlunit.html.HtmlButton;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.host.dom.NodeList;

/**
 * The JavaScript object that represents a {@link HtmlButton} (&lt;button type=...&gt;).
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlButton.class)
public class HTMLButtonElement extends HTMLElement {

    /** "Live" labels collection; has to be a member to have equality (==) working. */
    private NodeList labels_;

    /**
     * JavaScript constructor.
     */
    @Override
    @JsxConstructor
    public void jsConstructor() {
        super.jsConstructor();
    }

    /**
     * Sets the value of the attribute {@code type}.
     * <p>Note that there is no GUI change in the shape of the button,
     * so we don't treat it like {@link HTMLInputElement#setType(String)}.</p>
     * @param newType the new type to set
     */
    @JsxSetter
    public void setType(final String newType) {
        getDomNodeOrDie().setAttribute(DomElement.TYPE_ATTRIBUTE, newType);
    }

    /**
     * Returns the {@code type} property.
     * @return the {@code type} property
     */
    @JsxGetter
    public String getType() {
        return ((HtmlButton) getDomNodeOrDie()).getType();
    }

    /**
     * Returns the labels associated with the element.
     * @return the labels associated with the element
     */
    @JsxGetter
    public NodeList getLabels() {
        if (labels_ == null) {
            labels_ = new LabelsNodeList(getDomNodeOrDie());
        }
        return labels_;
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public String getName() {
        return super.getName();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setName(final String newName) {
        super.setName(newName);
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
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public HTMLFormElement getForm() {
        return super.getForm();
    }

    /**
     * {@inheritDoc}
     */
    @JsxGetter
    @Override
    public Object getValue() {
        return super.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @JsxSetter
    @Override
    public void setValue(final Object newValue) {
        super.setValue(newValue);
    }

    /**
     * Checks whether the element has any constraints and whether it satisfies them.
     * @return if the element is valid
     */
    @JsxFunction
    public boolean checkValidity() {
        return getDomNodeOrDie().isValid();
    }

    /**
     * @return a ValidityState with the validity states that this element is in.
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
     * @return always false
     */
    @JsxGetter
    public boolean getWillValidate() {
        return ((HtmlButton) getDomNodeOrDie()).willValidate();
    }

    /**
     * Sets the custom validity message for the element to the specified message.
     * @param message the new message
     */
    @JsxFunction
    public void setCustomValidity(final String message) {
        ((HtmlButton) getDomNodeOrDie()).setCustomValidity(message);
    }

    /**
     * Returns the value of the property {@code formnovalidate}.
     * @return the value of this property
     */
    @JsxGetter
    public boolean isFormNoValidate() {
        return ((HtmlButton) getDomNodeOrDie()).isFormNoValidate();
    }

    /**
     * Sets the value of the property {@code formnovalidate}.
     * @param value the new value
     */
    @JsxSetter
    public void setFormNoValidate(final boolean value) {
        ((HtmlButton) getDomNodeOrDie()).setFormNoValidate(value);
    }
}
