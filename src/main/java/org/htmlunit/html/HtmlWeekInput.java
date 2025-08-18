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

import static org.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_WEEK_SUPPORTED;
import static org.htmlunit.BrowserVersionFeatures.JS_INPUT_CHANGE_TYPE_DROPS_VALUE_WEEK_MONTH;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SgmlPage;
import org.htmlunit.util.StringUtils;

/**
 * Wrapper for the HTML element "input" where type is "week".
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Anton Demydenko
 */
public class HtmlWeekInput extends HtmlInput implements LabelableElement {

    private static final DateTimeFormatter FORMATTER_ = DateTimeFormatter.ofPattern("yyyy-'W'ww");

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlWeekInput(final String qualifiedName, final SgmlPage page,
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
        if (!hasFeature(HTMLINPUT_TYPE_WEEK_SUPPORTED)
                || StringUtils.isEmptyOrNull(newValue)) {
            super.setValue(newValue);
            return;
        }

        try {
            FORMATTER_.parse(newValue);
            super.setValue(newValue);
        }
        catch (final DateTimeParseException ignored) {
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
        if (hasFeature(HTMLINPUT_TYPE_WEEK_SUPPORTED) && !getMin().isEmpty()) {
            try {
                final LocalDate dateValue = LocalDate.parse(getRawValue() + "-1",
                        DateTimeFormatter.ISO_WEEK_DATE);
                final LocalDate minDate = LocalDate.parse(getMin() + "-1", DateTimeFormatter.ISO_WEEK_DATE);
                return minDate.isEqual(dateValue) || minDate.isBefore(dateValue);
            }
            catch (final DateTimeParseException ignored) {
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
        if (hasFeature(HTMLINPUT_TYPE_WEEK_SUPPORTED) && !getMax().isEmpty()) {
            try {
                final LocalDate dateValue = LocalDate.parse(getRawValue() + "-1",
                        DateTimeFormatter.ISO_WEEK_DATE);
                final LocalDate maxDate = LocalDate.parse(getMax() + "-1", DateTimeFormatter.ISO_WEEK_DATE);
                return maxDate.isEqual(dateValue) || maxDate.isAfter(dateValue);
            }
            catch (final DateTimeParseException ignored) {
                // ignore
            }
        }
        return true;
    }

    @Override
    protected void adjustValueAfterTypeChange(final HtmlInput oldInput, final BrowserVersion browserVersion) {
        if (browserVersion.hasFeature(JS_INPUT_CHANGE_TYPE_DROPS_VALUE_WEEK_MONTH)) {
            setValue("");
            return;
        }
        super.adjustValueAfterTypeChange(oldInput, browserVersion);
    }
}
