/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" where type is "url".
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Anton Demydenko
 */
public class HtmlUrlInput extends HtmlSelectableTextInput implements LabelableElement {

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlUrlInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, trimValueAttribute(page, attributes));
    }

    private static Map<String, DomAttr> trimValueAttribute(final SgmlPage page, final Map<String, DomAttr> attributes) {
        for (final Map.Entry<String, DomAttr> entry : attributes.entrySet()) {
            if (VALUE_ATTRIBUTE.equalsIgnoreCase(entry.getKey())) {
                entry.getValue().setValue(entry.getValue().getValue().trim());
                break;
            }
        }

        return attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getValue() {
        final String raw = getRawValue();
        if (StringUtils.isBlank(raw)) {
            return "";
        }
        return raw;
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
