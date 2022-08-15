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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_EMAIL_TRIMMED;

import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" where type is "email".
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 * @author Michael Lueck
 */
public class HtmlEmailInput extends HtmlSelectableTextInput implements LabelableElement {

    // see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/email#validation
    private static final Pattern DEFAULT_PATTERN =
            Pattern.compile("^[a-zA-Z\\d.!#$%&'*+/=?^_`{|\\}~-]+@[a-zA-Z\\d](?:[a-zA-Z\\d-]{0,61}[a-zA-Z\\d])?"
                                + "(?:\\.[a-zA-Z\\d](?:[a-zA-Z\\d-]{0,61}[a-zA-Z\\d])?)*$");

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlEmailInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        final String value = getValueAttribute();
        if (!value.isEmpty()) {
            setValueAttribute(value);
        }
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
        if (hasFeature(JS_INPUT_SET_VALUE_EMAIL_TRIMMED)) {
            if (StringUtils.isBlank(newValue)) {
                super.setValueAttribute("");
                return;
            }
            super.setValueAttribute(newValue.trim());
            return;
        }

        super.setValueAttribute(newValue);
    }

    @Override
    public boolean isValid() {
        final boolean isValid = super.isValid();
        if (!isValid) {
            return false;
        }

        final String val = getValueAttribute();
        if (StringUtils.isNotBlank(val)) {
            return DEFAULT_PATTERN.matcher(val).matches();
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isPatternSupported() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isBlankPatternValidated() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isMinMaxLengthSupported() {
        return true;
    }
}
