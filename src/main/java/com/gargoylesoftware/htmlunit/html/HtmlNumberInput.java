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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_NUMBER_ACCEPT_ALL;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_NUMBER_DOT_AT_END_IS_DOUBLE;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" with type is "number".
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 * @author Raik Bieniek
 * @author Michael Lueck
 */
public class HtmlNumberInput extends HtmlSelectableTextInput implements LabelableElement {

    private static final char[] VALID_INT_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-'};

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
            if (!NumberUtils.isCreatable(value)) {
                setValueAttribute("");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void typeDone(final String newValue, final boolean notifyAttributeChangeListeners) {
        if (hasFeature(JS_INPUT_NUMBER_ACCEPT_ALL)) {
            setAttributeNS(null, "value", newValue, notifyAttributeChangeListeners, false);
            return;
        }

        if (StringUtils.isBlank(newValue)) {
            setAttributeNS(null, "value", "", notifyAttributeChangeListeners, false);
            return;
        }

        if ("-".equals(newValue) || "+".equals(newValue)) {
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
            setAttributeNS(null, "value", newValue.trim(), notifyAttributeChangeListeners, false);
        }
        catch (final NumberFormatException e) {
            // ignore
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
                            if (value.subtract(min).abs().remainder(step).doubleValue() > 0.0) {
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
