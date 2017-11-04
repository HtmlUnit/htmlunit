/*
 * Copyright (c) 2002-2017 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DATE_WITH_LEFT_TO_RIGHT_MARK;

import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

import com.gargoylesoftware.htmlunit.BrowserVersion;

/**
 * An implementation of {@code DateTimeFormatter}, supporting Java 8 Date and Time.
 *
 * @author Ahmed Ashour
 */
class DefaultDateTimeFormatter implements AbstractDateTimeFormatter {

    private DateTimeFormatter formatter_;
    private Chronology chronology_;

    DefaultDateTimeFormatter(final String locale, final BrowserVersion browserVersion, final String pattern) {
        formatter_ = DateTimeFormatter.ofPattern(pattern);
        if (locale.startsWith("ar")
                && (!"ar-DZ".equals(locale)
                                && !"ar-LY".equals(locale)
                                && !"ar-MA".equals(locale)
                                && !"ar-TN".equals(locale))) {
            final DecimalStyle decimalStyle = DecimalStyle.STANDARD.withZeroDigit('\u0660');
            formatter_ = formatter_.withDecimalStyle(decimalStyle);
        }

        switch (locale) {
            case "ja-JP-u-ca-japanese":
                chronology_ = JapaneseChronology.INSTANCE;
                break;

            case "ar":
                if (browserVersion.hasFeature(JS_DATE_WITH_LEFT_TO_RIGHT_MARK)) {
                    chronology_ = HijrahChronology.INSTANCE;
                }
                break;

            case "ar-SA":
                chronology_ = HijrahChronology.INSTANCE;
                break;

            case "ar-SD":
                if (browserVersion.hasFeature(JS_DATE_WITH_LEFT_TO_RIGHT_MARK)) {
                    chronology_ = HijrahChronology.INSTANCE;
                }
                break;

            case "th":
            case "th-TH":
                chronology_ = ThaiBuddhistChronology.INSTANCE;
                break;

            default:
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String format(final Date date) {
        TemporalAccessor zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
        if (chronology_ != null) {
            zonedDateTime = chronology_.date(zonedDateTime);
        }
        return formatter_.format(zonedDateTime);
    }

}
