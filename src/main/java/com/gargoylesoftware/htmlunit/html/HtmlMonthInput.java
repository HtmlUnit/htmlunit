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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_MONTH_SUPPORTED;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" where type is "month".
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Anton Demydenko
 */
public class HtmlMonthInput extends HtmlInput implements LabelableElement {

    private static DateTimeFormatter FORMATTER_ = DateTimeFormatter.ofPattern("yyyy-MM");

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlMonthInput(final String qualifiedName, final SgmlPage page,
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
    public void setValueAttribute(final String newValue) {
        try {
            if (hasFeature(HTMLINPUT_TYPE_MONTH_SUPPORTED) && StringUtils.isNotEmpty(newValue)) {
                FORMATTER_.parse(newValue);
            }
            super.setValueAttribute(newValue);
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
     * <a href='https://www.w3.org/TR/html5/sec-forms.html'>HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a valid min value
     */
    private boolean isMinValid() {
        if (hasFeature(HTMLINPUT_TYPE_MONTH_SUPPORTED) && !getMin().isEmpty()) {
            try {
                final YearMonth dateValue = YearMonth.parse(getValueAttribute(), FORMATTER_);
                final YearMonth minDate = YearMonth.parse(getMin(), FORMATTER_);
                return minDate.equals(dateValue) || minDate.isBefore(dateValue);
            }
            catch (final DateTimeParseException e) {
                // ignore
            }
        }
        return true;
    }

    /**
     * Returns if the input element has a valid max value. Refer to the
     * <a href='https://www.w3.org/TR/html5/sec-forms.html'>HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a valid max value
     */
    private boolean isMaxValid() {
        if (hasFeature(HTMLINPUT_TYPE_MONTH_SUPPORTED) && !getMax().isEmpty()) {
            try {
                final YearMonth dateValue = YearMonth.parse(getValueAttribute(), FORMATTER_);
                final YearMonth maxDate = YearMonth.parse(getMax(), FORMATTER_);
                return maxDate.equals(dateValue) || maxDate.isAfter(dateValue);
            }
            catch (final DateTimeParseException e) {
                // ignore
            }
        }
        return true;
    }
}
