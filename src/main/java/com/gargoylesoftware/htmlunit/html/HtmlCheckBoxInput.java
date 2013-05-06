/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.EVENT_ONCHANGE_LOSING_FOCUS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCHECKEDINPUT_SET_CHECKED_TO_FALSE_WHEN_CLONE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCHECKEDINPUT_SET_DEFAULT_CHECKED_UPDATES_CHECKED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLCHECKEDINPUT_SET_DEFAULT_VALUE_WHEN_CLONE;

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input".
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:chen_jun@users.sourceforge.net">Jun Chen</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Mike Bresnahan
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlCheckBoxInput extends HtmlInput {

    private boolean defaultCheckedState_;
    private boolean valueAtFocus_;
    private boolean forceChecked_;

    /**
     * Creates an instance.
     * If no value is specified, it is set to "on" as browsers do (e.g. IE6 and Mozilla 1.7)
     * even if spec says that it is not allowed
     * (<a href="http://www.w3.org/TR/REC-html40/interact/forms.html#adef-value-INPUT">W3C</a>).
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlCheckBoxInput(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);

        //From the checkbox creator
        defaultCheckedState_ = hasAttribute("checked");

        // default value for both IE6 and Mozilla 1.7 even if spec says it is unspecified
        if (getAttribute("value") == ATTRIBUTE_NOT_DEFINED) {
            setAttribute("value", "on");
            setDefaultValue(ATTRIBUTE_NOT_DEFINED, false);
        }
    }

    /**
     * {@inheritDoc}
     *
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
        if (isChecked) {
            setAttribute("checked", "checked");
        }
        else {
            removeAttribute("checked");
        }

        if (hasFeature(EVENT_ONCHANGE_LOSING_FOCUS)) {
            return getPage();
        }
        return executeOnChangeHandlerIfAppropriate(this);
    }

    /**
     * A checkbox does not have a textual representation,
     * but we invent one for it because it is useful for testing.
     * @return "checked" or "unchecked" according to the radio state
     */
    // we need to preserve this method as it is there since many versions with the above documentation.
    @Override
    public String asText() {
        return super.asText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate() throws IOException {
        final boolean isChecked = !isChecked();
        if (isChecked) {
            setAttribute("checked", "checked");
        }
        else {
            removeAttribute("checked");
        }
        super.doClickStateUpdate();
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doClickFireChangeEvent() throws IOException {
        if (!hasFeature(EVENT_ONCHANGE_LOSING_FOCUS)) {
            executeOnChangeHandlerIfAppropriate(this);
        }
    }

    /**
     * Both IE and Mozilla will first update the internal state of checkbox
     * and then handle "onclick" event.
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
        setChecked(!isChecked());
    }

    /**
     * {@inheritDoc} Also sets the value to the new default value.
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
        if (hasFeature(HTMLCHECKEDINPUT_SET_DEFAULT_CHECKED_UPDATES_CHECKED)) {
            setChecked(defaultChecked);
        }
        if (hasFeature(HTMLCHECKEDINPUT_SET_CHECKED_TO_FALSE_WHEN_CLONE)) {
            reset();
            forceChecked_ = true;
        }
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
    protected void onAddedToPage() {
        super.onAddedToPage();

        if (forceChecked_) {
            reset();
            forceChecked_ = wasCreatedByJavascript();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onAddedToDocumentFragment() {
        super.onAddedToDocumentFragment();

        if (forceChecked_) {
            reset();
            forceChecked_ = false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final HtmlCheckBoxInput clone = (HtmlCheckBoxInput) super.cloneNode(deep);
        if (wasCreatedByJavascript() && hasFeature(HTMLCHECKEDINPUT_SET_CHECKED_TO_FALSE_WHEN_CLONE)) {
            clone.removeAttribute("checked");
            clone.forceChecked_ = isDefaultChecked();
        }
        if (hasFeature(HTMLCHECKEDINPUT_SET_DEFAULT_VALUE_WHEN_CLONE)) {
            clone.setDefaultValue(getValueAttribute(), false);
        }
        return clone;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void focus() {
        super.focus();
        valueAtFocus_ = isChecked();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    void removeFocus() {
        super.removeFocus();

        final boolean fireOnChange = hasFeature(EVENT_ONCHANGE_LOSING_FOCUS);
        if (fireOnChange && valueAtFocus_ != isChecked()) {
            executeOnChangeHandlerIfAppropriate(this);
        }
        valueAtFocus_ = isChecked();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue) {
        if (hasFeature(HTMLCHECKEDINPUT_SET_DEFAULT_CHECKED_UPDATES_CHECKED) && "value".equals(qualifiedName)) {
            setDefaultValue(attributeValue, false);
        }
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue);
    }
}
