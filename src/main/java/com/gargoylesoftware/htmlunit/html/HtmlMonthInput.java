/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_DATETIME_SUPPORTED;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.HTMLINPUT_TYPE_MONTH_NOT_SUPPORTED;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "input" where type is "month".
 *
 * @author Ahmed Ashour
 */
public class HtmlMonthInput extends HtmlInput {

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
            if (hasFeature(HTMLINPUT_TYPE_DATETIME_SUPPORTED)
                    && !hasFeature(HTMLINPUT_TYPE_MONTH_NOT_SUPPORTED)
                    && StringUtils.isNotEmpty(newValue)) {
                FORMATTER_.parse(newValue);
            }
            super.setValueAttribute(newValue);
        }
        catch (final DateTimeParseException e) {
            // ignore
        }
    }
}
