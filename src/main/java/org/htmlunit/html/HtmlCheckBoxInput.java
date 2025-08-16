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
package org.htmlunit.html;

import java.io.IOException;
import java.util.Map;

import org.htmlunit.Page;
import org.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input".
 *
 * @author Mike Bowler
 * @author David K. Taylor
 * @author Jun Chen
 * @author Christian Sell
 * @author Marc Guillemot
 * @author Mike Bresnahan
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlCheckBoxInput extends HtmlInput implements LabelableElement {

    /**
     * Value to use if no specified <code>value</code> attribute.
     */
    private static final String DEFAULT_VALUE = "on";

    private boolean defaultCheckedState_;
    private boolean checkedState_;

    /**
     * Creates an instance.
     * If no value is specified, it is set to "on" as browsers do
     * even if spec says that it is not allowed
     * (<a href="http://www.w3.org/TR/REC-html40/interact/forms.html#adef-value-INPUT">W3C</a>).
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlCheckBoxInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        if (getAttributeDirect(VALUE_ATTRIBUTE) == ATTRIBUTE_NOT_DEFINED) {
            setRawValue(DEFAULT_VALUE);
        }

        defaultCheckedState_ = hasAttribute(ATTRIBUTE_CHECKED);
        checkedState_ = defaultCheckedState_;
    }

    /**
     * Returns {@code true} if this element is currently selected.
     * @return {@code true} if this element is currently selected
     */
    @Override
    public boolean isChecked() {
        return checkedState_;
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        setChecked(defaultCheckedState_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page setChecked(final boolean isChecked) {
        checkedState_ = isChecked;

        return executeOnChangeHandlerIfAppropriate(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        checkedState_ = !isChecked();
        super.doClickStateUpdate(shiftKey, ctrlKey);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doClickFireChangeEvent() {
        executeOnChangeHandlerIfAppropriate(this);
    }

    /**
     * First update the internal state of checkbox and then handle "onclick" event.
     * {@inheritDoc}
     */
    @Override
    protected boolean isStateUpdateFirst() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preventDefault() {
        checkedState_ = !checkedState_;
    }

    /**
     * {@inheritDoc}
     * Also sets the value to the new default value.
     * @see SubmittableElement#setDefaultValue(String)
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        super.setDefaultValue(defaultValue);
        setValue(defaultValue);
    }

    /**
     * {@inheritDoc}
     * Also sets the default value.
     */
    @Override
    public void setValue(final String newValue) {
        super.setValue(newValue);
        super.setDefaultValue(newValue);
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#setDefaultChecked(boolean)
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        defaultCheckedState_ = defaultChecked;
        setChecked(isDefaultChecked());
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#isDefaultChecked()
     */
    @Override
    public boolean isDefaultChecked() {
        return defaultCheckedState_;
    }

    @Override
    protected Object getInternalValue() {
        return isChecked();
    }

    @Override
    void handleFocusLostValueChanged() {
        // ignore
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        final String qualifiedNameLC = org.htmlunit.util.StringUtils.toRootLowerCase(qualifiedName);

        if (VALUE_ATTRIBUTE.equals(qualifiedNameLC)) {
            super.setAttributeNS(namespaceURI, qualifiedNameLC, attributeValue, notifyAttributeChangeListeners,
                    notifyMutationObservers);
            setRawValue(attributeValue);
            return;
        }

        if (ATTRIBUTE_CHECKED.equals(qualifiedNameLC)) {
            checkedState_ = true;
        }
        super.setAttributeNS(namespaceURI, qualifiedNameLC, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean propagateClickStateUpdateToParent() {
        return false;
    }

    @Override
    public boolean isValueMissingValidityState() {
        return ATTRIBUTE_NOT_DEFINED != getAttributeDirect(ATTRIBUTE_REQUIRED)
                && !isChecked();
    }
}
