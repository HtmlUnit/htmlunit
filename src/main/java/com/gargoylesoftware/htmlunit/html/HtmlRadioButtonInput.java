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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONCHANGE_AFTER_ONCLICK;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_CHECKBOX_DOES_NOT_CLICK_SURROUNDING_ANCHOR;

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.event.Event;

/**
 * Wrapper for the HTML element "input".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Mike Bresnahan
 * @author Daniel Gredler
 * @author Bruce Faulkner
 * @author Ahmed Ashour
 * @author Benoit Heinrich
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlRadioButtonInput extends HtmlInput implements LabelableElement {

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
    HtmlRadioButtonInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, addValueIfNeeded(page, attributes));

        // fix the default value in case we have set it
        if (getAttributeDirect("value") == DEFAULT_VALUE) {
            setDefaultValue(ATTRIBUTE_NOT_DEFINED, false);
        }

        defaultCheckedState_ = hasAttribute(ATTRIBUTE_CHECKED);
        checkedState_ = defaultCheckedState_;
    }

    /**
     * Add missing attribute if needed by fixing attribute map rather to add it afterwards as this second option
     * triggers the instantiation of the script object at a time where the DOM node has not yet been added to its
     * parent.
     */
    private static Map<String, DomAttr> addValueIfNeeded(final SgmlPage page,
            final Map<String, DomAttr> attributes) {

        for (final String key : attributes.keySet()) {
            if ("value".equalsIgnoreCase(key)) {
                return attributes; // value attribute was specified
            }
        }

        // value attribute was not specified, add it
        final DomAttr newAttr = new DomAttr(page, null, "value", DEFAULT_VALUE, true);
        attributes.put("value", newAttr);

        return attributes;
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

    void setCheckedInternal(final boolean isChecked) {
        checkedState_ = isChecked;
    }

    /**
     * Sets the {@code checked} attribute.
     *
     * @param isChecked true if this element is to be selected
     * @return the page that occupies this window after setting checked status
     * It may be the same window or it may be a freshly loaded one.
     */
    @Override
    public Page setChecked(final boolean isChecked) {
        Page page = getPage();

        final boolean changed = isChecked() != isChecked;
        checkedState_ = isChecked;
        if (isChecked) {
            final HtmlForm form = getEnclosingForm();
            if (form != null) {
                form.setCheckedRadioButton(this);
            }
            else if (page != null && page.isHtmlPage()) {
                setCheckedForPage((HtmlPage) page);
            }
        }

        if (changed) {
            final ScriptResult scriptResult = fireEvent(Event.TYPE_CHANGE);
            if (scriptResult != null) {
                page = page.getEnclosingWindow().getWebClient().getCurrentWindow().getEnclosedPage();
            }
        }
        return page;
    }

    /**
     * Override of default clickAction that makes this radio button the selected
     * one when it is clicked.
     * {@inheritDoc}
     *
     * @throws IOException if an IO error occurred
     */
    @Override
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        final HtmlForm form = getEnclosingForm();
        final boolean changed = !isChecked();

        final Page page = getPage();
        if (form != null) {
            form.setCheckedRadioButton(this);
        }
        else if (page != null && page.isHtmlPage()) {
            setCheckedForPage((HtmlPage) page);
        }
        super.doClickStateUpdate(shiftKey, ctrlKey);
        return changed;
    }

    /**
     * Select the specified radio button in the page (outside any &lt;form&gt;).
     */
    private void setCheckedForPage(final HtmlPage htmlPage) {
        final String name = getNameAttribute();
        for (final DomNode domNode : htmlPage.getDescendants()) {
            if (domNode instanceof HtmlRadioButtonInput) {
                final HtmlRadioButtonInput radioInput = (HtmlRadioButtonInput) domNode;
                if (name.equals(radioInput.getAttribute("name")) && radioInput.getEnclosingForm() == null) {
                    if (radioInput == this) {
                        setCheckedInternal(true);
                    }
                    else {
                        radioInput.setCheckedInternal(false);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected ScriptResult doClickFireClickEvent(final Event event) {
        if (!hasFeature(EVENT_ONCHANGE_AFTER_ONCLICK)) {
            executeOnChangeHandlerIfAppropriate(this);
        }

        return super.doClickFireClickEvent(event);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doClickFireChangeEvent() {
        if (hasFeature(EVENT_ONCHANGE_AFTER_ONCLICK)) {
            executeOnChangeHandlerIfAppropriate(this);
        }
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
        setValueAttribute(defaultValue);
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

    /**
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
    protected void onAddedToPage() {
        super.onAddedToPage();
        setChecked(isChecked());
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
        if ("value".equals(qualifiedName)) {
            setDefaultValue(attributeValue, false);
        }
        if (ATTRIBUTE_CHECKED.equals(qualifiedName)) {
            checkedState_ = true;
        }
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean propagateClickStateUpdateToParent() {
        return !hasFeature(HTMLINPUT_CHECKBOX_DOES_NOT_CLICK_SURROUNDING_ANCHOR)
                && super.propagateClickStateUpdateToParent();
    }

    @Override
    public boolean isValueMissingValidityState() {
        if (ATTRIBUTE_NOT_DEFINED == getAttributeDirect(ATTRIBUTE_REQUIRED)) {
            return false;
        }
        if (ATTRIBUTE_NOT_DEFINED == getNameAttribute()) {
            return !isChecked();
        }

        final String name = getNameAttribute();
        for (final DomNode domNode : getPage().getDescendants()) {
            if (domNode instanceof HtmlRadioButtonInput) {
                final HtmlRadioButtonInput radioInput = (HtmlRadioButtonInput) domNode;
                if (name.equals(radioInput.getAttribute("name"))) {
                    if (radioInput.isChecked()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
