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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_NUMBER_ACCEPT_ALL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextInput;
import com.gargoylesoftware.htmlunit.html.impl.SelectableTextSelectionDelegate;

/**
 * Wrapper for the HTML element "input" with type is "number".
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 * @author Raik Bieniek
 * @author Michael Lück
 */
public class HtmlNumberInput extends HtmlInput implements SelectableTextInput, LabelableElement {

    private static final char[] VALID_INT_CHARS = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'};

    private SelectableTextSelectionDelegate selectionDelegate_ = new SelectableTextSelectionDelegate(this);
    private DoTypeProcessor doTypeProcessor_ = new DoTypeProcessor(this);

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlNumberInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        final String value = getValueAttribute();
        if (!value.isEmpty()) {
            try {
                Double.parseDouble(value.trim());
            }
            catch (final NumberFormatException e) {
                setValueAttribute("");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doType(final char c, final boolean lastType) {
        doTypeProcessor_.doType(getValueAttribute(), selectionDelegate_, c, this, lastType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void doType(final int keyCode, final boolean lastType) {
        doTypeProcessor_.doType(getValueAttribute(), selectionDelegate_, keyCode, this, lastType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void typeDone(final String newValue, final boolean notifyAttributeChangeListeners) {
        if (newValue.length() <= getMaxLength()) {
            if (hasFeature(JS_INPUT_NUMBER_ACCEPT_ALL)) {
                setAttributeNS(null, "value", newValue, notifyAttributeChangeListeners, false);
                return;
            }

            if (StringUtils.isBlank(newValue) || "-".equals(newValue) || "+".equals(newValue)) {
                setAttributeNS(null, "value", newValue, notifyAttributeChangeListeners, false);
                return;
            }

            String parseValue = newValue;
            final int lastPos = parseValue.length() - 1;
            if (parseValue.charAt(lastPos) == '.') {
                parseValue = parseValue.substring(0, lastPos);
            }

            try {
                Double.parseDouble(parseValue);
                setAttributeNS(null, "value", newValue, notifyAttributeChangeListeners, false);
            }
            catch (final NumberFormatException e) {
                // ignore
            }

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isSubmittableByEnter() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void select() {
        selectionDelegate_.select();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSelectedText() {
        return selectionDelegate_.getSelectedText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getText() {
        return getValueAttribute();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setText(final String text) {
        setValueAttribute(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSelectionStart() {
        return selectionDelegate_.getSelectionStart();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectionStart(final int selectionStart) {
        selectionDelegate_.setSelectionStart(selectionStart);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSelectionEnd() {
        return selectionDelegate_.getSelectionEnd();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelectionEnd(final int selectionEnd) {
        selectionDelegate_.setSelectionEnd(selectionEnd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        super.setAttributeNS(namespaceURI, qualifiedName, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);
        if ("value".equals(qualifiedName)) {
            final SgmlPage page = getPage();
            if (page != null && page.isHtmlPage()) {
                int pos = 0;
                if (!hasFeature(JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START)) {
                    pos = attributeValue.length();
                }
                setSelectionStart(pos);
                setSelectionEnd(pos);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultValue(final String defaultValue) {
        final boolean modifyValue = getValueAttribute().equals(getDefaultValue());
        setDefaultValue(defaultValue, modifyValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueAttribute(final String newValue) {
        try {
            if (!newValue.isEmpty()) {
                final String lang = getPage().getWebClient().getBrowserVersion().getBrowserLanguage();
                final NumberFormat format = NumberFormat.getInstance(Locale.forLanguageTag(lang));
                format.parse(newValue);
            }
            super.setValueAttribute(newValue);
        }
        catch (final ParseException e) {
            // ignore
        }
    }

    /**
     * {@inheritDoc}
     * @see HtmlInput#reset()
     */
    @Override
    public void reset() {
        super.reset();
        setSelectionEnd(0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomNode cloneNode(final boolean deep) {
        final HtmlNumberInput newnode = (HtmlNumberInput) super.cloneNode(deep);
        newnode.selectionDelegate_ = new SelectableTextSelectionDelegate(newnode);
        newnode.doTypeProcessor_ = new DoTypeProcessor(newnode);

        return newnode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        final String valueAttr = getValueAttribute();
        if (!valueAttr.isEmpty()) {
            if ("-".equals(valueAttr) || "+".equals(valueAttr)) {
                return false;
            }

            // if we have no step, the value has to be an integer
            if (getStep().isEmpty()) {
                String val = valueAttr;
                final int lastPos = val.length() - 1;
                if (lastPos >= 0 && val.charAt(lastPos) == '.') {
                    if (hasFeature(JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE)) {
                        return false;
                    }
                    val = val.substring(0, lastPos);
                }
                if (!StringUtils.containsOnly(val, VALID_INT_CHARS)) {
                    return false;
                }
            }

            final BigDecimal value;
            try {
                value = new BigDecimal(valueAttr);
            }
            catch (final NumberFormatException e) {
                return false;
            }

            if (!getMin().isEmpty()) {
                try {
                    final BigDecimal min = new BigDecimal(getMin());
                    if (value.compareTo(min) < 0) {
                        return false;
                    }

                    if (!getStep().isEmpty()) {
                        try {
                            final BigDecimal step = new BigDecimal(getStep());
                            if(value.remainder(step).doubleValue() > 0.0) {
                                return false;
                            }
                        }
                        catch (final NumberFormatException e) {
                            // ignore
                        }
                    }
                }
                catch (final NumberFormatException e) {
                    // ignore
                }
            }
            if (!getMax().isEmpty()) {
                try {
                    final BigDecimal max = new BigDecimal(getMax());
                    if (value.compareTo(max) > 0) {
                        return false;
                    }
                }
                catch (final NumberFormatException e) {
                    // ignore
                }
            }
        }
        return true;
    }
}
