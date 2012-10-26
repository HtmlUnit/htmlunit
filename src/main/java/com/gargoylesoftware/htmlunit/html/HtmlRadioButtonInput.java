/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_DEFAULT_IS_CHECKED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLRADIOINPUT_SET_CHECKED_TO_DEFAULT_WHEN_ADDED;

import java.io.IOException;
import java.util.Map;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.ScriptResult;
import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.Event;

/**
 * Wrapper for the HTML element "input".
 *
 * @version $Revision$
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
 */
public class HtmlRadioButtonInput extends HtmlInput {

    private boolean defaultCheckedState_;
    private boolean valueAtFocus_;

    /**
     * Creates an instance.
     * If no value is specified, it is set to "on" as browsers do (eg IE6 and Mozilla 1.7)
     * even if spec says that it is not allowed
     * (<a href="http://www.w3.org/TR/REC-html40/interact/forms.html#adef-value-INPUT">W3C</a>).
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlRadioButtonInput(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(namespaceURI, qualifiedName, page, attributes);

        // default value for both IE6 and Mozilla 1.7 even if spec says it is unspecified
        if (getAttribute("value") == ATTRIBUTE_NOT_DEFINED) {
            setAttribute("value", "on");
        }

        defaultCheckedState_ = hasAttribute("checked");
    }

    /**
     * {@inheritDoc}
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        if (defaultCheckedState_) {
            setAttribute("checked", "checked");
        }
        else {
            removeAttribute("checked");
        }
    }

    /**
     * Sets the "checked" attribute.
     *
     * @param isChecked true if this element is to be selected
     * @return the page that occupies this window after setting checked status
     * It may be the same window or it may be a freshly loaded one.
     */
    @Override
    public Page setChecked(final boolean isChecked) {
        final HtmlForm form = getEnclosingForm();
        final boolean changed = isChecked() != isChecked;

        Page page = getPage();
        if (isChecked) {
            if (form != null) {
                form.setCheckedRadioButton(this);
            }
            else if (page instanceof HtmlPage) {
                ((HtmlPage) page).setCheckedRadioButton(this);
            }
        }
        else {
            removeAttribute("checked");
        }

        if (changed && !hasFeature(EVENT_ONCHANGE_LOSING_FOCUS)) {
            final ScriptResult scriptResult = fireEvent(Event.TYPE_CHANGE);
            if (scriptResult != null) {
                page = scriptResult.getNewPage();
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
    protected boolean doClickStateUpdate() throws IOException {
        final HtmlForm form = getEnclosingForm();
        final boolean changed = isChecked() != true;

        final Page page = getPage();
        if (form != null) {
            form.setCheckedRadioButton(this);
        }
        else if (page instanceof HtmlPage) {
            ((HtmlPage) page).setCheckedRadioButton(this);
        }
        super.doClickStateUpdate();
        return changed;
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
     * A radio button does not have a textual representation,
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
        if (hasFeature(HTMLINPUT_DEFAULT_IS_CHECKED)) {
            setChecked(defaultChecked);
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
    protected boolean isStateUpdateFirst() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onAddedToPage() {
        if (hasFeature(HTMLRADIOINPUT_SET_CHECKED_TO_DEFAULT_WHEN_ADDED)) {
            setChecked(isDefaultChecked());
        }
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
}
