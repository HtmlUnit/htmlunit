/*
 * Copyright (c) 2002-2015 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLOPTION_UNSELECT_SELECTS_FIRST;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OPTION_CONSTRUCTOR_IGNORES_LABEL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_OPTION_USE_TEXT_AS_VALUE_IF_NOT_DEFINED;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.CHROME;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.FF;
import static com.gargoylesoftware.htmlunit.javascript.configuration.BrowserName.IE;
import net.sourceforge.htmlunit.corejs.javascript.Context;

import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HTMLParser;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlOptionGroup;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClasses;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxGetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxSetter;
import com.gargoylesoftware.htmlunit.javascript.configuration.WebBrowser;

/**
 * The JavaScript object that represents an option.
 *
 * @version $Revision$
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author Chris Erskine
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@JsxClasses({
    @JsxClass(domClass = HtmlOption.class,
            browsers = { @WebBrowser(CHROME), @WebBrowser(FF), @WebBrowser(value = IE, minVersion = 11) }),
    @JsxClass(domClass = HtmlOption.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8)),
    @JsxClass(domClass = HtmlOptionGroup.class, isJSObject = false,
            browsers = @WebBrowser(value = IE, maxVersion = 8))
})
public class HTMLOptionElement extends FormChild {

    /**
     * JavaScript constructor.
     * @param newText the text
     * @param newValue the value
     * @param defaultSelected Whether the option is initially selected
     * @param selected the current selection state of the option
     */
    @JsxConstructor({ @WebBrowser(CHROME), @WebBrowser(FF) })
    public void jsConstructor(final String newText, final String newValue,
            final boolean defaultSelected, final boolean selected) {
        final HtmlPage page = (HtmlPage) getWindow().getWebWindow().getEnclosedPage();
        AttributesImpl attributes = null;
        if (defaultSelected) {
            attributes = new AttributesImpl();
            attributes.addAttribute(null, "selected", "selected", null, "selected");
        }

        final HtmlOption htmlOption = (HtmlOption) HTMLParser.getFactory(HtmlOption.TAG_NAME).createElement(
                page, HtmlOption.TAG_NAME, attributes);
        htmlOption.setSelected(selected);
        setDomNode(htmlOption);

        if (!"undefined".equals(newText)) {
            htmlOption.appendChild(new DomText(page, newText));
            if (!getBrowserVersion().hasFeature(JS_OPTION_CONSTRUCTOR_IGNORES_LABEL)) {
                htmlOption.setLabelAttribute(newText);
            }
        }
        if (!"undefined".equals(newValue)) {
            htmlOption.setValueAttribute(newValue);
        }
    }

    /**
     * Returns the value of the "value" property.
     * @return the value property
     */
    @JsxGetter
    public String getValue() {
        String value = getDomNodeOrNull().getAttribute("value");
        if (value == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(JS_OPTION_USE_TEXT_AS_VALUE_IF_NOT_DEFINED)) {
            value = ((HtmlOption) getDomNodeOrNull()).getText();
        }
        return value;
    }

    /**
     * Sets the value of the "value" property.
     * @param newValue the value property
     */
    @JsxSetter
    public void setValue(final String newValue) {
        if (getDomNodeOrNull() instanceof HtmlOption) {
            ((HtmlOption) getDomNodeOrNull()).setValueAttribute(newValue);
        }
    }

    /**
     * Returns the value of the "text" property.
     * @return the text property
     */
    @Override
    @JsxGetter
    public String getText() {
        if (getDomNodeOrNull() instanceof HtmlOption) {
            return ((HtmlOption) getDomNodeOrNull()).getText();
        }
        return null;
    }

    /**
     * Sets the value of the "text" property.
     * @param newText the text property
     */
    @JsxSetter
    public void setText(final String newText) {
        if (getDomNodeOrNull() instanceof HtmlOption) {
            ((HtmlOption) getDomNodeOrNull()).setText(newText);
        }
    }

    /**
     * Returns the value of the "selected" property.
     * @return the text property
     */
    @JsxGetter
    public boolean getSelected() {
        if (getDomNodeOrNull() instanceof HtmlOption) {
            return ((HtmlOption) getDomNodeOrNull()).isSelected();
        }
        return false;
    }

    /**
     * Sets the value of the "selected" property.
     * @param selected the new selected property
     */
    @JsxSetter
    public void setSelected(final boolean selected) {
        final HtmlOption optionNode = (HtmlOption) getDomNodeOrNull();
        final HtmlSelect enclosingSelect = optionNode.getEnclosingSelect();
        if (!selected && optionNode.isSelected()
                && enclosingSelect != null && !enclosingSelect.isMultipleSelectEnabled()) {

            // un-selecting selected option has no effect in IE and selects first option in FF
            if (getBrowserVersion().hasFeature(HTMLOPTION_UNSELECT_SELECTS_FIRST)) {
                enclosingSelect.getOption(0).setSelected(true, false);
            }
        }
        else {
            optionNode.setSelected(selected, false);
        }
    }

    /**
     * Returns the value of the "defaultSelected" property.
     * @return the text property
     */
    @JsxGetter
    public boolean getDefaultSelected() {
        if (getDomNodeOrNull() instanceof HtmlOption) {
            return ((HtmlOption) getDomNodeOrNull()).isDefaultSelected();
        }
        return false;
    }

    /**
     * Returns the value of the "label" property.
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
     * Sets the value of the "label" property.
     * @param label the new label property
     */
    @JsxSetter
    public void setLabel(final String label) {
        final DomNode domNode = getDomNodeOrNull();
        if (domNode instanceof HtmlOption) {
            ((HtmlOption) domNode).setLabelAttribute(label);
        }
        ((HtmlOptionGroup) domNode).setLabelAttribute(label);
    }

    /**
     * {@inheritDoc} Overridden to modify browser configurations.
     */
    @Override
    @JsxGetter
    public boolean getDisabled() {
        return super.getDisabled();
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
     * Returns the {@code dataFld} attribute.
     * @return the {@code dataFld} attribute
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getDataFld() {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code dataFld} attribute.
     * @param dataFld {@code dataFld} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setDataFld(final String dataFld) {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code dataFormatAs} attribute.
     * @return the {@code dataFormatAs} attribute
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getDataFormatAs() {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code dataFormatAs} attribute.
     * @param dataFormatAs {@code dataFormatAs} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setDataFormatAs(final String dataFormatAs) {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Returns the {@code dataSrc} attribute.
     * @return the {@code dataSrc} attribute
     */
    @JsxGetter(@WebBrowser(value = IE, maxVersion = 8))
    public String getDataSrc() {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }

    /**
     * Sets the {@code dataSrc} attribute.
     * @param dataSrc {@code dataSrc} attribute
     */
    @JsxSetter(@WebBrowser(value = IE, maxVersion = 8))
    public void setDataSrc(final String dataSrc) {
        throw Context.throwAsScriptRuntimeEx(new UnsupportedOperationException());
    }
}
