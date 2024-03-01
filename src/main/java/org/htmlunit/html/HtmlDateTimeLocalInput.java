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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" where type is "datetime-local".
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Anton Demydenko
 */
public class HtmlDateTimeLocalInput extends HtmlInput implements LabelableElement {

    private static final DateTimeFormatter FORMATTER_ = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlDateTimeLocalInput(final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes) {
        super(qualifiedName, page, attributes);
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
        try {
            if (StringUtils.isNotEmpty(newValue)) {
                FORMATTER_.parse(newValue);
            }
            super.setValue(newValue);
        }
        catch (final DateTimeParseException e) {
            // ignore
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isValid() {
        return super.isValid() && isMaxValid() && isMinValid();
    }

    /**
     * Returns if the input element has a valid min value. Refer to the
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a> documentation
     * for details.
     *
     * @return if the input element has a valid min value
     */
    private boolean isMinValid() {
        if (!getMin().isEmpty()) {
            try {
                final LocalDateTime dateValue = LocalDateTime.parse(getRawValue(), FORMATTER_);
                final LocalDateTime minDate = LocalDateTime.parse(getMin(), FORMATTER_);
                return minDate.isEqual(dateValue) || minDate.isBefore(dateValue);
            }
            catch (final DateTimeParseException e) {
                // ignore
            }
        }
        return true;
    }

    /**
     * Returns if the input element has a valid max value. Refer to the
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a> documentation
     * for details.
     *
     * @return if the input element has a valid max value
     */
    private boolean isMaxValid() {
        if (!getMax().isEmpty()) {
            try {
                final LocalDateTime dateValue = LocalDateTime.parse(getRawValue(), FORMATTER_);
                final LocalDateTime maxDate = LocalDateTime.parse(getMax(), FORMATTER_);
                return maxDate.isEqual(dateValue) || maxDate.isAfter(dateValue);
            }
            catch (final DateTimeParseException e) {
                // ignore
            }
        }
        return true;
    }

    @Override
    protected void adjustValueAfterTypeChange(final HtmlInput oldInput, final BrowserVersion browserVersion) {
        setValue("");
    }
}
