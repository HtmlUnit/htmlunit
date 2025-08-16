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

import static org.htmlunit.BrowserVersionFeatures.JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE;

import java.util.List;

import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.html.HtmlOption;
import org.htmlunit.html.HtmlSelect;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.configuration.JsxGetter;
import org.htmlunit.javascript.configuration.JsxSetter;
import org.htmlunit.javascript.configuration.JsxSymbol;
import org.htmlunit.javascript.host.dom.Node;
import org.htmlunit.javascript.host.dom.NodeList;

/**
 * The JavaScript object for {@link HtmlSelect}.
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Carsten Steul
 */
@JsxClass(domClass = HtmlSelect.class)
public class HTMLSelectElement extends HTMLElement {

    private HTMLOptionsCollection optionsArray_;

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
     * Initialize the object.
     *
     */
    public void initialize() {
        final HtmlSelect htmlSelect = getDomNodeOrDie();
        htmlSelect.setScriptableObject(this);
        if (optionsArray_ == null) {
            optionsArray_ = new HTMLOptionsCollection(this);
            optionsArray_.initialize(htmlSelect);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HtmlSelect getDomNodeOrDie() {
        return (HtmlSelect) super.getDomNodeOrDie();
    }

    /**
     * Removes option at the specified index.
     * @param index the index of the item to remove
     */
    @JsxFunction
    public void remove(final int index) {
        if (index < 0 && getBrowserVersion().hasFeature(JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE)) {
            return;
        }
        final HTMLOptionsCollection options = getOptions();
        if (index >= options.getLength() && getBrowserVersion().hasFeature(JS_SELECT_REMOVE_IGNORE_IF_INDEX_OUTSIDE)) {
            return;
        }

        getOptions().remove(index);
    }

    /**
     * Adds a new item to the list (optionally) before the specified item.
     * @param newOptionObject the DomNode to insert
     * @param beforeOptionObject the DomNode to insert the previous element before (null if at end).
     */
    @JsxFunction
    public void add(final HTMLOptionElement newOptionObject, final Object beforeOptionObject) {
        getOptions().add(newOptionObject, beforeOptionObject);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node appendChild(final Object childObject) {
        final Node node = super.appendChild(childObject);
        getDomNodeOrDie().ensureSelectedIndex();
        return node;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Node insertBeforeImpl(final Object[] args) {
        final Node node = super.insertBeforeImpl(args);
        getDomNodeOrDie().ensureSelectedIndex();
        return node;
    }

    /**
     * Gets the item at the specified index.
     * @param index the position of the option to retrieve
     * @return the option
     */
    @JsxFunction
    public Object item(final int index) {
        final Object option = getOptions().item(index);
        if (JavaScriptEngine.isUndefined(option)) {
            return null;
        }
        return option;
    }

    /**
     * Returns the type of this input.
     * @return the type
     */
    @JsxGetter
    public String getType() {
        final String type;
        if (getDomNodeOrDie().isMultipleSelectEnabled()) {
            type = "select-multiple";
        }
        else {
            type = "select-one";
        }
        return type;
    }

    /**
     * Returns the value of the {@code options} property.
     * @return the {@code options} property
     */
    @JsxGetter
    public HTMLOptionsCollection getOptions() {
        if (optionsArray_ == null) {
            initialize();
        }
        return optionsArray_;
    }

    /**
     * Returns the value of the {@code selectedIndex} property.
     * @return the {@code selectedIndex} property
     */
    @JsxGetter
    public int getSelectedIndex() {
        return getDomNodeOrDie().getSelectedIndex();
    }

    /**
     * Sets the value of the {@code selectedIndex} property.
     * @param index the new value
     */
    @JsxSetter
    public void setSelectedIndex(final int index) {
        getDomNodeOrDie().setSelectedIndex(index);
    }

    /**
     * Returns the actual value of the selected Option.
     * @return the value
     */
    @Override
    @JsxGetter
    public String getValue() {
        final List<HtmlOption> selectedOptions = getDomNodeOrDie().getSelectedOptions();
        if (selectedOptions.isEmpty()) {
            return "";
        }
        return ((HTMLOptionElement) selectedOptions.get(0).getScriptableObject()).getValue();
    }

    /**
     * Returns the value of the {@code length} property.
     * @return the {@code length} property
     */
    @JsxGetter
    public int getLength() {
        return getOptions().getLength();
    }

    /**
     * Removes options by reducing the {@code length} property.
     * @param newLength the new {@code length} property value
     */
    @JsxSetter
    public void setLength(final int newLength) {
        getOptions().setLength(newLength);
    }

    /**
     * Returns the specified indexed property.
     * @param index the index of the property
     * @param start the scriptable object that was originally queried for this property
     * @return the property
     */
    @Override
    public Object get(final int index, final Scriptable start) {
        if (getDomNodeOrNull() == null) {
            return NOT_FOUND; // typically for the prototype
        }
        return getOptions().get(index, start);
    }

    /**
     * Sets the index property.
     * @param index the index
     * @param start the scriptable object that was originally invoked for this property
     * @param newValue the new value
     */
    @Override
    public void put(final int index, final Scriptable start, final Object newValue) {
        getOptions().put(index, start, newValue);
    }

    /**
     * Selects the option with the specified value.
     * @param newValue the value of the option to select
     */
    @Override
    @JsxSetter
    public void setValue(final Object newValue) {
        final String val = JavaScriptEngine.toString(newValue);
        getDomNodeOrDie().setSelectedAttribute(val, true, false);
    }

    /**
     * Returns the {@code size} attribute.
     * @return the {@code size} attribute
     */
    @JsxGetter
    public int getSize() {
        return getDomNodeOrDie().getSize();
    }

    /**
     * Sets the {@code size} attribute.
     * @param size the {@code size} attribute
     */
    @JsxSetter
    public void setSize(final String size) {
        getDomNodeOrDie().setAttribute("size", size);
    }

    /**
     * Returns {@code true} if the {@code multiple} attribute is set.
     * @return {@code true} if the {@code multiple} attribute is set
     */
    @JsxGetter
    public boolean isMultiple() {
        return getDomNodeOrDie().hasAttribute("multiple");
    }

    /**
     * Sets or clears the {@code multiple} attribute.
     * @param multiple {@code true} to set the {@code multiple} attribute, {@code false} to clear it
     */
    @JsxSetter
    public void setMultiple(final boolean multiple) {
        if (multiple) {
            getDomNodeOrDie().setAttribute("multiple", "multiple");
        }
        else {
            getDomNodeOrDie().removeAttribute("multiple");
        }
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
     * Returns the {@code required} property.
     * @return the {@code required} property
     */
    @JsxGetter
    public boolean isRequired() {
        return getDomNodeOrDie().isRequired();
    }

    /**
     * Sets the {@code required} property.
     * @param required the new value
     */
    @JsxSetter
    public void setRequired(final boolean required) {
        getDomNodeOrDie().setRequired(required);
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
     * @return whether the element is a candidate for constraint validation
     */
    @JsxGetter
    public boolean getWillValidate() {
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
     * @return the Iterator symbol
     */
    @JsxSymbol
    public Scriptable iterator() {
        return getOptions().iterator();
    }
}
