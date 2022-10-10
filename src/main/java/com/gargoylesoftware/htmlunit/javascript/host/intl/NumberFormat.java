/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.intl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.HtmlUnitScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.NativeArray;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code NumberFormat}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class NumberFormat extends HtmlUnitScriptable {

    private static final ConcurrentHashMap<String, String> CHROME_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> EDGE_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> FF_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> FF_ESR_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> IE_FORMATS_ = new ConcurrentHashMap<>();

    private transient NumberFormatHelper formatter_;

    static {

        final Map<String, String> commonFormats = new HashMap<>();
        commonFormats.put("", "");
        commonFormats.put("ar", "\u066c\u066b\u0660");
        commonFormats.put("ar-DZ", ".,");
        commonFormats.put("ar-LY", ".,");
        commonFormats.put("ar-MA", ".,");
        commonFormats.put("ar-TN", ".,");
        commonFormats.put("id", ".,");
        commonFormats.put("de-AT", "\u00a0");
        commonFormats.put("de-CH", "\u2019");
        commonFormats.put("en-ZA", "\u00a0,");
        commonFormats.put("es-CR", "\u00a0,");
        commonFormats.put("fr-LU", ".,");
        commonFormats.put("hi-IN", ",.0");
        commonFormats.put("it-CH", "\u2019");
        commonFormats.put("pt-PT", "\u00a0,");
        commonFormats.put("sq", "\u00a0,");

        IE_FORMATS_.putAll(commonFormats);
        IE_FORMATS_.put("ar-DZ", ",.");
        IE_FORMATS_.put("ar-LY", ",.");
        IE_FORMATS_.put("ar-MA", ",.");
        IE_FORMATS_.put("ar-TN", ",.");
        IE_FORMATS_.put("fr", "\u00a0,");
        IE_FORMATS_.put("fr-BE", ".");
        IE_FORMATS_.put("ban", ".,");

        commonFormats.put("ar-AE", ",.0");
        commonFormats.put("fr", "\u202f,");
        commonFormats.put("fr-CA", "\u00a0,");

        FF_FORMATS_.putAll(commonFormats);
        FF_ESR_FORMATS_.putAll(commonFormats);

        commonFormats.put("ar", ",.0");
        commonFormats.put("ar-BH", "\u066c\u066b\u0660");
        commonFormats.put("ar-EG", "\u066c\u066b\u0660");
        commonFormats.put("ar-IQ", "\u066c\u066b\u0660");
        commonFormats.put("ar-JO", "\u066c\u066b\u0660");
        commonFormats.put("ar-KW", "\u066c\u066b\u0660");
        commonFormats.put("ar-LB", "\u066c\u066b\u0660");
        commonFormats.put("ar-OM", "\u066c\u066b\u0660");
        commonFormats.put("ar-QA", "\u066c\u066b\u0660");
        commonFormats.put("ar-SA", "\u066c\u066b\u0660");
        commonFormats.put("ar-SD", "\u066c\u066b\u0660");
        commonFormats.put("ar-SY", "\u066c\u066b\u0660");
        commonFormats.put("ar-YE", "\u066c\u066b\u0660");

        CHROME_FORMATS_.putAll(commonFormats);
        EDGE_FORMATS_.putAll(commonFormats);

        CHROME_FORMATS_.put("be", ",.");
        CHROME_FORMATS_.put("mk", ",.");
        CHROME_FORMATS_.put("is", ",.");
        CHROME_FORMATS_.put("sq", ",.");
    }

    /**
     * Default constructor.
     */
    public NumberFormat() {
    }

    private NumberFormat(final String[] locales, final BrowserVersion browserVersion) {
        final Map<String, String> formats;
        if (browserVersion.isChrome()) {
            formats = CHROME_FORMATS_;
        }
        else if (browserVersion.isEdge()) {
            formats = EDGE_FORMATS_;
        }
        else if (browserVersion.isIE()) {
            formats = IE_FORMATS_;
        }
        else if (browserVersion.isFirefoxESR()) {
            formats = FF_ESR_FORMATS_;
        }
        else {
            formats = FF_FORMATS_;
        }

        String locale = "";
        String pattern = null;

        for (final String l : locales) {
            pattern = getPattern(formats, l);
            if (pattern != null) {
                locale = l;
            }
        }

        if (pattern == null) {
            pattern = formats.get("");
            if (locales.length > 0) {
                locale = locales[0];
            }
        }

        formatter_ = new NumberFormatHelper(locale, browserVersion, pattern);
    }

    private static String getPattern(final Map<String, String> formats, final String locale) {
        if ("no-NO-NY".equals(locale)) {
            throw ScriptRuntime.rangeError("Invalid language tag: " + locale);
        }
        String pattern = formats.get(locale);
        if (pattern == null && locale.indexOf('-') != -1) {
            pattern = formats.get(locale.substring(0, locale.indexOf('-')));
        }
        return pattern;
    }

    /**
     * JavaScript constructor.
     * @param cx the current context
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final Object[] args, final Function ctorObj,
            final boolean inNewExpr) {
        final String[] locales;
        if (args.length != 0) {
            if (args[0] instanceof NativeArray) {
                final NativeArray array = (NativeArray) args[0];
                locales = new String[(int) array.getLength()];
                for (int i = 0; i < locales.length; i++) {
                    locales[i] = Context.toString(array.get(i));
                }
            }
            else {
                locales = new String[] {Context.toString(args[0])};
            }
        }
        else {
            locales = new String[] {""};
        }
        final Window window = getWindow(ctorObj);
        final NumberFormat format = new NumberFormat(locales, window.getBrowserVersion());
        format.setParentScope(window);
        format.setPrototype(window.getPrototype(format.getClass()));
        return format;
    }

    /**
     * Formats a number according to the locale and formatting options of this Intl.NumberFormat object.
     * @param object the JavaScript object to convert
     * @return the dated formated
     */
    @JsxFunction
    public String format(final Object object) {
        final double number = Context.toNumber(object);
        return formatter_.format(number);
    }

    /**
     * @return A new object with properties reflecting the locale and date and time formatting options
     * computed during the initialization of the given {@code DateTimeFormat} object.
     */
    @JsxFunction
    public Scriptable resolvedOptions() {
        return Context.getCurrentContext().newObject(getParentScope());
    }

    /**
     * Helper.
     */
    static final class NumberFormatHelper {
        private DecimalFormat formatter_;

        NumberFormatHelper(final String localeName, final BrowserVersion browserVersion, final String pattern) {
            Locale locale = browserVersion.getBrowserLocale();
            if (StringUtils.isNotEmpty(localeName)) {
                locale = Locale.forLanguageTag(localeName);
            }

            final DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

            if (pattern.length() > 0) {
                final char groupingSeparator = pattern.charAt(0);
                if (groupingSeparator != ' ') {
                    symbols.setGroupingSeparator(groupingSeparator);
                }

                if (pattern.length() > 1) {
                    final char decimalSeparator = pattern.charAt(1);
                    if (decimalSeparator != ' ') {
                        symbols.setDecimalSeparator(decimalSeparator);
                    }

                    if (pattern.length() > 2) {
                        final char zeroDigit = pattern.charAt(2);
                        if (zeroDigit != ' ') {
                            symbols.setZeroDigit(zeroDigit);
                        }
                    }
                }
            }

            formatter_ = new DecimalFormat("#,##0.###", symbols);
        }

        String format(final double number) {
            return formatter_.format(number);
        }
    }
}
