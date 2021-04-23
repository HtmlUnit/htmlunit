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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DATE_LOCALE_DATE_SHORT;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DATE_WITH_LEFT_TO_RIGHT_MARK;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.time.FastDateFormat;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Contains some missing features of Rhino NativeDate.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
public final class DateCustom {

    private DateCustom() {
    }

    /**
     * Converts a date to a string, returning the "date" portion using the operating system's locale's conventions.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return converted string
     */
    public static String toLocaleDateString(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final String formatString;
        final BrowserVersion browserVersion = ((Window) thisObj.getParentScope()).getBrowserVersion();

        if (browserVersion.hasFeature(JS_DATE_WITH_LEFT_TO_RIGHT_MARK)) {
            // [U+200E] -> Unicode Character 'LEFT-TO-RIGHT MARK'
            formatString = "\u200EM\u200E/\u200Ed\u200E/\u200Eyyyy";
        }
        else if (browserVersion.hasFeature(JS_DATE_LOCALE_DATE_SHORT)) {
            formatString = "M/d/yyyy";
        }
        else {
            formatString = "EEEE, MMMM dd, yyyy";
        }
        final FastDateFormat format =  FastDateFormat.getInstance(formatString, getLocale(browserVersion));
        return format.format(getDateValue(thisObj));
    }

    /**
     * Converts a date to a string, returning the "time" portion using the current locale's conventions.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return converted string
     */
    public static String toLocaleTimeString(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        final String formatString;
        final BrowserVersion browserVersion = ((Window) thisObj.getParentScope()).getBrowserVersion();

        if (browserVersion.hasFeature(JS_DATE_WITH_LEFT_TO_RIGHT_MARK)) {
            // [U+200E] -> Unicode Character 'LEFT-TO-RIGHT MARK'
            formatString = "\u200Eh\u200E:\u200Emm\u200E:\u200Ess\u200E \u200Ea";
        }
        else {
            formatString = "h:mm:ss a";
        }
        final FastDateFormat format =  FastDateFormat.getInstance(formatString, getLocale(browserVersion));
        return format.format(getDateValue(thisObj));
    }

    private static long getDateValue(final Scriptable thisObj) {
        final Date date = (Date) Context.jsToJava(thisObj, Date.class);
        return date.getTime();
    }

    private static Locale getLocale(final BrowserVersion browserVersion) {
        return new Locale(browserVersion.getSystemLanguage());
    }
}
