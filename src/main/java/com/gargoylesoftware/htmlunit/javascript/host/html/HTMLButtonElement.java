/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.IE;

import com.gargoylesoftware.htmlunit.html.HtmlButton;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.host.dom.AbstractList;
import com.gargoylesoftware.htmlunit.javascript.host.dom.TextRange;

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
    private AbstractList labels_;

    /**
     * Creates an instance.
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public HTMLButtonElement() {
    }

    /**
     * Sets the value of the attribute {@code type}.
     * <p>Note that there is no GUI change in the shape of the button,
     * so we don't treat it like {@link HTMLInputElement#setType(String)}.</p>
     * @param newType the new type to set
     */
    @JsxSetter
    public void setType(final String newType) {
        getDomNodeOrDie().setAttribute("type", newType);
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
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public AbstractList getLabels() {
        if (labels_ == null) {
            labels_ = new LabelsHelper(getDomNodeOrDie());
        }
        return labels_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @JsxFunction(IE)
    public TextRange createTextRange() {
        return super.createTextRange();
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
    @JsxGetter({CHROME, EDGE, FF, FF78})
    public boolean isDisabled() {
        return super.isDisabled();
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxSetter({CHROME, EDGE, FF, FF78})
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
}
