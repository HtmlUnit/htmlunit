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
package org.htmlunit.javascript.host;

import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.Scriptable;

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
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return converted string
     */
    public static String toLocaleDateString(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        final BrowserVersion browserVersion = ((Window) thisObj.getParentScope()).getBrowserVersion();
        final String formatString = "M/d/yyyy";
        final FastDateFormat format =  FastDateFormat.getInstance(formatString, browserVersion.getBrowserLocale());
        return format.format(getDateValue(thisObj));
    }

    /**
     * Converts a date to a string, returning the "time" portion using the current locale's conventions.
     * @param context the JavaScript context
     * @param scope the scope
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return converted string
     */
    public static String toLocaleTimeString(final Context context, final Scriptable scope,
            final Scriptable thisObj, final Object[] args, final Function function) {
        final String formatString;
        final BrowserVersion browserVersion = ((Window) thisObj.getParentScope()).getBrowserVersion();
        formatString = "h:mm:ss a";
        final FastDateFormat format =  FastDateFormat.getInstance(formatString, browserVersion.getBrowserLocale());
        return format.format(getDateValue(thisObj));
    }

    private static long getDateValue(final Scriptable thisObj) {
        final Date date = (Date) Context.jsToJava(thisObj, Date.class);
        return date.getTime();
    }
}
