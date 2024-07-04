/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "fieldset".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
public class HtmlFieldSet extends HtmlElement implements ValidatableElement, FormFieldWithNameHistory {

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "fieldset";

    private final String originalName_;
    private Collection<String> newNames_ = Collections.emptySet();
    private String customValidity_;

    /**
     * Creates an instance of HtmlFieldSet
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the HtmlPage that contains this element
     * @param attributes the initial attributes
     */
    HtmlFieldSet(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
        originalName_ = getAttributeDirect(NAME_ATTRIBUTE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willValidate() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCustomValidity(final String message) {
        customValidity_ = message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCustomErrorValidityState() {
        return !StringUtils.isEmpty(customValidity_);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean isValidValidityState() {
        return !isCustomErrorValidityState();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setAttributeNS(final String namespaceURI, final String qualifiedName, final String attributeValue,
            final boolean notifyAttributeChangeListeners, final boolean notifyMutationObservers) {
        final String qualifiedNameLC = StringUtils.toRootLowerCase(qualifiedName);
        if (NAME_ATTRIBUTE.equals(qualifiedNameLC)) {
            if (newNames_.isEmpty()) {
                newNames_ = new HashSet<>();
            }
            newNames_.add(attributeValue);
        }
        super.setAttributeNS(namespaceURI, qualifiedNameLC, attributeValue, notifyAttributeChangeListeners,
                notifyMutationObservers);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getOriginalName() {
        return originalName_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<String> getNewNames() {
        return newNames_;
    }
}
