/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.intl;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * An implementation of {@code DateTimeFormatter}, supporting Google App Engine.
 *
 * @author Ahmed Ashour
 */
class GAEDateTimeFormatter implements AbstractDateTimeFormatter {

    private DateFormat format_;

    /**
     * Default constructor.
     */
    GAEDateTimeFormatter(final String locale, final BrowserVersion browserVersion, final String pattern) {
        format_ = new SimpleDateFormat(pattern);
        if (locale.startsWith("ar")
                && (!"ar-DZ".equals(locale)
                        && !"ar-LY".equals(locale)
                        && !"ar-MA".equals(locale)
                        && !"ar-TN".equals(locale))) {
            setZeroDigit('\u0660');
        }
    }

    private void setZeroDigit(final char zeroDigit) {
        final DecimalFormat df = (DecimalFormat) format_.getNumberFormat();
        final DecimalFormatSymbols dfs = df.getDecimalFormatSymbols();
        dfs.setZeroDigit(zeroDigit);
        df.setDecimalFormatSymbols(dfs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(final Date date) {
        return format_.format(date);
    }

}
