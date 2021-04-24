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

import java.text.NumberFormat;
import java.util.IllformedLocaleException;
import java.util.Locale;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * Contains some missing features of Rhino NativeNumber.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public final class NumberCustom {

    private NumberCustom() {
    }

    /**
     * Returns a string with a language sensitive representation of this number.
     * @param context the JavaScript context
     * @param thisObj the scriptable
     * @param args the arguments passed into the method
     * @param function the function
     * @return the string
     */
    public static String toLocaleString(
            final Context context, final Scriptable thisObj, final Object[] args, final Function function) {
        if (args.length != 0 && args[0] instanceof String) {
            final String localeStr = (String) args[0];
            try {
                final Locale locale = new Locale.Builder().setLanguageTag(localeStr).build();
                return NumberFormat.getInstance(locale).format(Double.parseDouble(thisObj.toString()));
            }
            catch (final IllformedLocaleException e) {
                throw ScriptRuntime.rangeError("Invalid language tag: " + localeStr);
            }
        }

        final BrowserVersion browserVersion = ((Window) thisObj.getParentScope()).getBrowserVersion();
        final Locale locale = Locale.forLanguageTag(browserVersion.getBrowserLanguage());
        return NumberFormat.getInstance(locale).format(Double.parseDouble(thisObj.toString()));
    }
}
