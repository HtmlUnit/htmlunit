/*
 * Copyright (c) 2002-2023 Gargoyle Software Inc.
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

import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_COLOR_NOT_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.htmlunit.SgmlPage;
import org.htmlunit.html.impl.Color;

/**
 * Wrapper for the HTML element "input" where type is "color".
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlColorInput extends HtmlInput implements LabelableElement {

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlColorInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
        if (getValueAttribute() == ATTRIBUTE_NOT_DEFINED
                && !hasFeature(JS_INPUT_SET_VALUE_MOVE_SELECTION_TO_START)) {
            setValue("#000000");
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
    public void setValue(final String newValue) {
        if (hasFeature(HTMLINPUT_TYPE_COLOR_NOT_SUPPORTED)) {
            super.setValue(newValue);
            return;
        }

        if (StringUtils.isEmpty(newValue)) {
            super.setValue("#000000");
            return;
        }

        if (isValid(newValue)) {
            super.setValue(newValue);
        }
    }

    private static boolean isValid(final String value) {
        boolean valid = false;
        if (value.length() == 7 && value.charAt(0) == '#') {
            try {
                new Color(
                        Integer.valueOf(value.substring(1, 3), 16),
                        Integer.valueOf(value.substring(3, 5), 16),
                        Integer.valueOf(value.substring(5, 7), 16));
                valid = true;
            }
            catch (final NumberFormatException e) {
                // ignore
            }
            catch (final IllegalArgumentException e) {
                // ignore
            }
        }
        return valid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isRequiredSupported() {
        return false;
    }
}
