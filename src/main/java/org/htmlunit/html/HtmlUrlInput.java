/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.htmlunit.SgmlPage;
import org.htmlunit.util.StringUtils;

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
        super(qualifiedName, page, attributes);
        setRawValue(getValue().trim());
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

    /**
     * {@inheritDoc}
     * Per spec, a non-empty value must be a valid absolute URL. An empty value
     * is never a type mismatch on its own (that's valueMissing's concern, if
     * 'required' is set). {@link java.net.URI} is used as a practical
     * approximation of the WHATWG URL Standard parser this codebase doesn't
     * implement -- it isn't a perfect match for every edge case a real
     * browser's parser accepts or rejects, but requiring an absolute URI
     * (a URI with a scheme) captures the core constraint correctly.
     */
    @Override
    public boolean hasTypeMismatchValidityState() {
        final String value = getValue();
        if (StringUtils.isEmptyOrNull(value)) {
            return false;
        }

        try {
            return !new URI(value.trim()).isAbsolute();
        }
        catch (final URISyntaxException e) {
            return true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTypeMismatchMessage() {
        return "Please enter a URL.";
    }
}
