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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLOPTION_REMOVE_SELECTED_ATTRIB_DESELECTS;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.EDGE;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.SupportedBrowser.FF78;

import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlOptionGroup;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;

/**
 * The JavaScript object that represents an option.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClass(domClass = HtmlOption.class)
public class HTMLOptionElement extends HTMLElement {

    /**
     * JavaScript constructor.
     * @param newText the text
     * @param newValue the value
     * @param defaultSelected Whether the option is initially selected
     * @param selected the current selection state of the option
     */
    @JsxConstructor({CHROME, EDGE, FF, FF78})
    public void jsConstructor(final Object newText, final String newValue,
            final boolean defaultSelected, final boolean selected) {
        final SgmlPage page = (SgmlPage) getWindow().getWebWindow().getEnclosedPage();
        AttributesImpl attributes = null;
        if (defaultSelected) {
            attributes = new AttributesImpl();
            attributes.addAttribute(null, "selected", "selected", null, "selected");
        }

        final HtmlOption htmlOption = (HtmlOption) page.getWebClient().getPageCreator().getHtmlParser()
                                                    .getFactory(HtmlOption.TAG_NAME)
                                                    .createElement(page, HtmlOption.TAG_NAME, attributes);
        htmlOption.setSelected(selected);
        setDomNode(htmlOption);

        if (!Undefined.isUndefined(newText)) {
            final String newTextString = Context.toString(newText);
            htmlOption.appendChild(new DomText(page, newTextString));
            htmlOption.setLabelAttribute(newTextString);
        }
        if (!"undefined".equals(newValue)) {
            htmlOption.setValueAttribute(newValue);
        }
    }

    /**
     * Returns the value of the {@code value} property.
     * @return the value property
     */
    @JsxGetter
    @Override
    public String getValue() {
        String value = getDomNodeOrNull().getAttributeDirect("value");
        if (value == DomElement.ATTRIBUTE_NOT_DEFINED) {
            value = ((HtmlOption) getDomNodeOrNull()).getText();
        }
        return value;
    }

    /**
     * Sets the value of the {@code value} property.
     * @param newValue the value property
     */
    @JsxSetter
    public void setValue(final String newValue) {
        final DomNode dom = getDomNodeOrNull();
        if (dom instanceof HtmlOption) {
            ((HtmlOption) dom).setValueAttribute(newValue);
        }
    }

    /**
     * Returns the value of the {@code text} property.
     * @return the text property
     */
    @JsxGetter
    public String getText() {
        final DomNode dom = getDomNodeOrNull();
        if (dom instanceof HtmlOption) {
            return ((HtmlOption) dom).getText();
        }
        return null;
    }

    /**
     * Sets the value of the {@code text} property.
     * @param newText the text property
     */
    @JsxSetter
    public void setText(final String newText) {
        final DomNode dom = getDomNodeOrNull();
        if (dom instanceof HtmlOption) {
            ((HtmlOption) dom).setText(newText);

            if (!hasAttribute("label")) {
                setLabel(newText);
            }
        }
    }

    /**
     * Returns the value of the {@code selected} property.
     * @return the text property
     */
    @JsxGetter
    public boolean isSelected() {
        final DomNode dom = getDomNodeOrNull();
        if (dom instanceof HtmlOption) {
            return ((HtmlOption) dom).isSelected();
        }
        return false;
    }

    /**
     * Sets the value of the {@code selected} property.
     * @param selected the new selected property
     */
    @JsxSetter
    public void setSelected(final boolean selected) {
        final HtmlOption optionNode = (HtmlOption) getDomNodeOrNull();
        final HtmlSelect enclosingSelect = optionNode.getEnclosingSelect();
        if (!selected && optionNode.isSelected()
                && enclosingSelect != null && !enclosingSelect.isMultipleSelectEnabled()) {
            enclosingSelect.getOption(0).setSelectedFromJavaScript(true);
        }
        else {
            optionNode.setSelectedFromJavaScript(selected);
        }
    }

    /**
     * Returns the value of the {@code defaultSelected} property.
     * @return the text property
     */
    @JsxGetter
    public boolean isDefaultSelected() {
        final DomNode dom = getDomNodeOrNull();
        if (dom instanceof HtmlOption) {
            return ((HtmlOption) dom).isDefaultSelected();
        }
        return false;
    }

    /**
     * Returns the value of the {@code label} property.
     * @return the label property
     */
    @JsxGetter
    public String getLabel() {
        final DomNode domNode = getDomNodeOrNull();
        if (domNode instanceof HtmlOption) {
            return ((HtmlOption) domNode).getLabelAttribute();
        }
        return ((HtmlOptionGroup) domNode).getLabelAttribute();
    }

    /**
     * Sets the value of the {@code label} property.
     * @param label the new label property
     */
    @JsxSetter
    public void setLabel(final String label) {
        final DomNode domNode = getDomNodeOrNull();
        if (domNode instanceof HtmlOption) {
            ((HtmlOption) domNode).setLabelAttribute(label);
        }
        else {
            ((HtmlOptionGroup) domNode).setLabelAttribute(label);
        }
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
     * Returns the {@code index} property.
     * @return the {@code index} property
     */
    @JsxGetter
    public int getIndex() {
        final HtmlOption optionNode = (HtmlOption) getDomNodeOrNull();
        if (optionNode != null) {
            final HtmlSelect enclosingSelect = optionNode.getEnclosingSelect();
            if (enclosingSelect != null) {
                return enclosingSelect.indexOf(optionNode);
            }
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAttribute(final String name, final String value) {
        super.setAttribute(name, value);
        if ("selected".equals(name)) {
            setSelected(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAttribute(final String name) {
        super.removeAttribute(name);
        if (getBrowserVersion().hasFeature(HTMLOPTION_REMOVE_SELECTED_ATTRIB_DESELECTS)) {
            if ("selected".equals(name)) {
                setSelected(false);
            }
        }
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
}
