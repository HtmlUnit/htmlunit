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
package org.htmlunit.javascript.host.intl;

import java.time.ZoneId;
import java.time.chrono.Chronology;
import java.time.chrono.HijrahChronology;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.ThaiBuddhistChronology;
import java.time.format.DateTimeFormatter;
import java.time.format.DecimalStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.htmlunit.BrowserVersion;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.Function;
import org.htmlunit.corejs.javascript.NativeArray;
import org.htmlunit.corejs.javascript.Scriptable;
import org.htmlunit.javascript.HtmlUnitScriptable;
import org.htmlunit.javascript.JavaScriptEngine;
import org.htmlunit.javascript.RecursiveFunctionObject;
import org.htmlunit.javascript.configuration.JsxClass;
import org.htmlunit.javascript.configuration.JsxConstructor;
import org.htmlunit.javascript.configuration.JsxFunction;
import org.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for {@code DateTimeFormat}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DateTimeFormat extends HtmlUnitScriptable {

    private static final ConcurrentHashMap<String, String> CHROME_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> EDGE_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> FF_FORMATS_ = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> FF_ESR_FORMATS_ = new ConcurrentHashMap<>();

    private transient DateTimeFormatHelper formatter_;

    static {
        final String ddSlash = "\u200Edd\u200E/\u200EMM\u200E/\u200EYYYY";
        final String ddDash = "\u200Edd\u200E-\u200EMM\u200E-\u200EYYYY";
        final String ddDot = "\u200Edd\u200E.\u200EMM\u200E.\u200EYYYY";
        final String ddDotDot = "\u200Edd\u200E.\u200EMM\u200E.\u200EYYYY\u200E.";
        final String ddDotBlank = "\u200Edd\u200E. \u200EMM\u200E. \u200EYYYY";
        final String ddDotBlankDot = "\u200Edd\u200E. \u200EMM\u200E. \u200EYYYY.";
        final String mmSlash = "\u200EMM\u200E/\u200Edd\u200E/\u200EYYYY";
        final String yyyySlash = "\u200EYYYY\u200E/\u200EMM\u200E/\u200Edd";
        final String yyyyDash = "\u200EYYYY\u200E-\u200EMM\u200E-\u200Edd";
        final String yyyyDotBlankDot = "\u200EYYYY\u200E. \u200EMM\u200E. \u200Edd.";

        final Map<String, String> commonFormats = new HashMap<>();
        commonFormats.put("", ddDot);
        commonFormats.put("ar", "dd\u200F/MM\u200F/YYYY");
        commonFormats.put("ban", mmSlash);
        commonFormats.put("be", ddDot);
        commonFormats.put("bg", ddDot + "\u200E \u0433.");
        commonFormats.put("ca", ddSlash);
        commonFormats.put("cs", ddDotBlank);
        commonFormats.put("da", ddDot);
        commonFormats.put("de", ddDot);
        commonFormats.put("el", ddSlash);
        commonFormats.put("en", mmSlash);
        commonFormats.put("en-CA", yyyyDash);
        commonFormats.put("en-NZ", ddSlash);
        commonFormats.put("en-PA", ddSlash);
        commonFormats.put("en-PR", ddSlash);
        commonFormats.put("en-PH", mmSlash);
        commonFormats.put("en-AU", ddSlash);
        commonFormats.put("en-GB", ddSlash);
        commonFormats.put("en-IE", ddSlash);
        commonFormats.put("en-IN", ddSlash);
        commonFormats.put("en-MT", ddSlash);
        commonFormats.put("en-SG", ddSlash);
        commonFormats.put("en-ZA", yyyySlash);
        commonFormats.put("es", ddSlash);
        commonFormats.put("es-CL", ddDash);
        commonFormats.put("es-PA", mmSlash);
        commonFormats.put("es-PR", mmSlash);
        commonFormats.put("es-US", ddSlash);
        commonFormats.put("et", ddDot);
        commonFormats.put("fi", ddDot);
        commonFormats.put("fr", ddSlash);
        commonFormats.put("fr-CA", yyyyDash);
        commonFormats.put("ga", ddSlash);
        commonFormats.put("hi", ddSlash);
        commonFormats.put("hr", ddDotBlankDot);
        commonFormats.put("hu", yyyyDotBlankDot);
        commonFormats.put("id", ddSlash);
        commonFormats.put("in", ddSlash);
        commonFormats.put("is", ddDot);
        commonFormats.put("it", ddSlash);
        commonFormats.put("iw", ddDot);
        commonFormats.put("ja", yyyySlash);
        commonFormats.put("ja-JP-u-ca-japanese", "'H'yy/MM/dd");
        commonFormats.put("ko", yyyyDotBlankDot);
        commonFormats.put("lt", yyyyDash);
        commonFormats.put("lv", ddDotDot);
        commonFormats.put("mk", ddDot);
        commonFormats.put("ms", ddSlash);
        commonFormats.put("mt", mmSlash);
        commonFormats.put("nl", ddDash);
        commonFormats.put("nl-BE", ddSlash);
        commonFormats.put("pl", ddDot);
        commonFormats.put("pt", ddSlash);
        commonFormats.put("ro", ddDot);
        commonFormats.put("ru", ddDot);
        commonFormats.put("sk", ddDotBlank);
        commonFormats.put("sl", ddDotBlank);
        commonFormats.put("sq", ddDot);
        commonFormats.put("sr", ddDotBlankDot);
        commonFormats.put("sv", yyyyDash);
        commonFormats.put("th", ddSlash);
        commonFormats.put("tr", ddDot);
        commonFormats.put("uk", ddDot);
        commonFormats.put("vi", ddSlash);
        commonFormats.put("zh", yyyySlash);
        commonFormats.put("zh-HK", ddSlash);
        commonFormats.put("zh-SG", "\u200EYYYY\u200E\u5E74\u200EMM\u200E\u6708\u200Edd\u200E\u65E5");
        commonFormats.put("fr-CH", ddDot);

        FF_FORMATS_.putAll(commonFormats);
        FF_FORMATS_.put("mk", ddDot + "\u200E \u0433.");

        commonFormats.put("ar-SA", "d\u200F/M\u200F/YYYY هـ");
        FF_ESR_FORMATS_.putAll(commonFormats);

        commonFormats.put("be", mmSlash);
        commonFormats.put("ga", mmSlash);
        commonFormats.put("is", mmSlash);
        commonFormats.put("mk", mmSlash);

        EDGE_FORMATS_.putAll(commonFormats);

        CHROME_FORMATS_.putAll(commonFormats);
        CHROME_FORMATS_.put("sq", mmSlash);
    }

    /**
     * Default constructor.
     */
    public DateTimeFormat() {
        super();
    }

    private DateTimeFormat(final String[] locales, final BrowserVersion browserVersion) {
        super();

        final Map<String, String> formats;
        if (browserVersion.isChrome()) {
            formats = CHROME_FORMATS_;
        }
        else if (browserVersion.isEdge()) {
            formats = EDGE_FORMATS_;
        }
        else if (browserVersion.isFirefoxESR()) {
            formats = FF_ESR_FORMATS_;
        }
        else {
            formats = FF_FORMATS_;
        }

        String locale = browserVersion.getBrowserLocale().toLanguageTag();
        String pattern = getPattern(formats, locale);

        for (final String l : locales) {
            pattern = getPattern(formats, l);
            if (pattern != null) {
                locale = l;
            }
        }

        if (pattern == null) {
            pattern = formats.get("");
        }

        if (!locale.startsWith("ar")) {
            pattern = pattern.replace("\u200E", "");
        }

        formatter_ = new DateTimeFormatHelper(locale, browserVersion, pattern);
    }

    private static String getPattern(final Map<String, String> formats, final String locale) {
        if ("no-NO-NY".equals(locale)) {
            throw JavaScriptEngine.rangeError("Invalid language tag: " + locale);
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
     * @param scope the scope
     * @param args the arguments to the WebSocket constructor
     * @param ctorObj the function object
     * @param inNewExpr Is new or not
     * @return the java object to allow JavaScript to access
     */
    @JsxConstructor
    public static Scriptable jsConstructor(final Context cx, final Scriptable scope,
            final Object[] args, final Function ctorObj, final boolean inNewExpr) {
        final String[] locales;
        if (args.length != 0) {
            if (args[0] instanceof NativeArray) {
                final NativeArray array = (NativeArray) args[0];
                locales = new String[(int) array.getLength()];
                for (int i = 0; i < locales.length; i++) {
                    locales[i] = JavaScriptEngine.toString(array.get(i));
                }
            }
            else {
                locales = new String[] {JavaScriptEngine.toString(args[0])};
            }
        }
        else {
            locales = new String[0];
        }

        final Window window = getWindow(ctorObj);
        final DateTimeFormat format = new DateTimeFormat(locales, window.getBrowserVersion());
        format.setParentScope(window);
        format.setPrototype(((RecursiveFunctionObject) ctorObj).getClassPrototype());
        return format;
    }

    /**
     * Formats a date according to the locale and formatting options of this {@code DateTimeFormat} object.
     * @param object the JavaScript object to convert
     * @return the dated formated
     */
    @JsxFunction
    public String format(final Object object) {
        final Date date = (Date) Context.jsToJava(object, Date.class);
        return formatter_.format(date, Context.getCurrentContext().getTimeZone().toZoneId());
    }

    /**
     * @return A new object with properties reflecting the locale and date and time formatting options
     *         computed during the initialization of the given {@code DateTimeFormat} object.
     */
    @JsxFunction
    public Scriptable resolvedOptions() {
        final Context cx = Context.getCurrentContext();
        final Scriptable options = cx.newObject(getParentScope());
        options.put("timeZone", options, cx.getTimeZone().getID());

        if (StringUtils.isEmpty(formatter_.locale_)) {
            options.put("locale", options, cx.getLocale().toLanguageTag());
        }
        else {
            options.put("locale", options, formatter_.locale_);
        }
        return options;
    }

    /**
     * Helper.
     */
    static final class DateTimeFormatHelper {

        private final DateTimeFormatter formatter_;
        private Chronology chronology_;
        private final String locale_;

        DateTimeFormatHelper(final String locale, final BrowserVersion browserVersion, final String pattern) {
            locale_ = locale;
            if (locale.startsWith("ar")
                    && !"ar-DZ".equals(locale)
                    && !"ar-LY".equals(locale)
                    && !"ar-MA".equals(locale)
                    && !"ar-TN".equals(locale)) {
                final DecimalStyle decimalStyle = DecimalStyle.STANDARD.withZeroDigit('\u0660');
                formatter_ = DateTimeFormatter.ofPattern(pattern).withDecimalStyle(decimalStyle);
            }
            else {
                formatter_ = DateTimeFormatter.ofPattern(pattern);
            }

            switch (locale) {
                case "ja-JP-u-ca-japanese":
                    chronology_ = JapaneseChronology.INSTANCE;
                    break;

                case "ar-SA":
                    if (!browserVersion.isFirefox() || browserVersion.isFirefoxESR())
                    chronology_ = HijrahChronology.INSTANCE;
                    break;

                case "th":
                case "th-TH":
                    chronology_ = ThaiBuddhistChronology.INSTANCE;
                    break;

                default:
            }
        }

        /**
         * Formats a date according to the locale and formatting options of this {@code DateTimeFormat} object.
         * @param date the JavaScript object to convert
         * @param zoneId the current time zone id
         * @return the dated formated
         */
        String format(final Date date, final ZoneId zoneId) {
            TemporalAccessor zonedDateTime = date.toInstant().atZone(zoneId);
            if (chronology_ != null) {
                zonedDateTime = chronology_.date(zonedDateTime);
            }
            return formatter_.format(zonedDateTime);
        }
    }
}
