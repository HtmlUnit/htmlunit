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

import static org.htmlunit.BrowserVersionFeatures.RESETINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED;

import java.io.IOException;
import java.util.Map;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input".
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlResetInput extends HtmlInput implements LabelableElement {

    /**
     * Value to use if no specified <code>value</code> attribute.
     */
    public static final String DEFAULT_VALUE = "Reset";

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlResetInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);

        if (getAttributeDirect(VALUE_ATTRIBUTE) == ATTRIBUTE_NOT_DEFINED) {
            final BrowserVersion browserVersion = page.getWebClient().getBrowserVersion();
            if (browserVersion.hasFeature(RESETINPUT_DEFAULT_VALUE_IF_VALUE_NOT_DEFINED)) {
                setValue(DEFAULT_VALUE);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean doClickStateUpdate(final boolean shiftKey, final boolean ctrlKey) throws IOException {
        if (!isDisabled()) {
            final HtmlForm form = getEnclosingForm();
            if (form != null) {
                form.reset();
                return false;
            }
        }
        super.doClickStateUpdate(shiftKey, ctrlKey);
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(final String newValue) {
        unmarkValueDirty();
        setDefaultValue(newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultChecked(final boolean defaultChecked) {
        // Empty.
    }

    /**
     * {@inheritDoc} This method <b>does nothing</b> for reset input elements.
     * @see SubmittableElement#reset()
     */
    @Override
    public void reset() {
        // Empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean propagateClickStateUpdateToParent() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isRequiredSupported() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean willValidate() {
        return false;
    }
}
