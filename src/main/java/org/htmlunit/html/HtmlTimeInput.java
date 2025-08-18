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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.Map;

import org.htmlunit.BrowserVersion;
import org.htmlunit.SgmlPage;
import org.htmlunit.util.StringUtils;

/**
 * Wrapper for the HTML element "input" where type is "time".
 *
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Anton Demydenko
 * @author Ronald Brill
 */
public class HtmlTimeInput extends HtmlSelectableTextInput implements LabelableElement {

    private static final DateTimeFormatter INPUT_FORMATTER_ =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("hh[:]mm[a]")
                    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                    .parseDefaulting(ChronoField.AMPM_OF_DAY, 0)
                    .toFormatter(Locale.US);
    private static final DateTimeFormatter OUTPUT_FORMATTER_ = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Creates an instance.
     *
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     */
    HtmlTimeInput(final String qualifiedName, final SgmlPage page,
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
    public String getValue() {
        String raw = getRawValue();
        if (StringUtils.isBlank(raw)) {
            return "";
        }

        raw = raw.trim();
        try {
            final TemporalAccessor time = INPUT_FORMATTER_.parse(raw);
            return OUTPUT_FORMATTER_.format(time);
        }
        catch (final DateTimeParseException ignored) {
            // ignore
        }
        return "";
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
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a valid min value
     */
    private boolean isMinValid() {
        if (!getMin().isEmpty()) {
            try {
                final LocalTime timeValue = LocalTime.parse(getRawValue(), INPUT_FORMATTER_);
                final LocalTime minTime = LocalTime.parse(getMin(), INPUT_FORMATTER_);
                return minTime.equals(timeValue) || minTime.isBefore(timeValue);
            }
            catch (final DateTimeParseException ignored) {
                // ignore
            }
        }
        return true;
    }

    /**
     * Returns if the input element has a valid max value. Refer to the
     * <a href="https://www.w3.org/TR/html5/sec-forms.html">HTML 5</a>
     * documentation for details.
     *
     * @return if the input element has a valid max value
     */
    private boolean isMaxValid() {
        if (!getMax().isEmpty()) {
            try {
                final LocalTime timeValue = LocalTime.parse(getRawValue(), INPUT_FORMATTER_);
                final LocalTime maxTime = LocalTime.parse(getMax(), INPUT_FORMATTER_);
                return maxTime.equals(timeValue) || maxTime.isAfter(timeValue);
            }
            catch (final DateTimeParseException ignored) {
                // ignore
            }
        }
        return true;
    }

    @Override
    protected void adjustValueAfterTypeChange(final HtmlInput oldInput, final BrowserVersion browserVersion) {
        setRawValue("");
    }
}
