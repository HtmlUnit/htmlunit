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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
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
 * A JavaScript object for {@code DateTimeFormat}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DateTimeFormat extends SimpleScriptable {

    private static ConcurrentHashMap<String, String> CHROME_FORMATS_ = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> EDGE_FORMATS_ = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> FF_FORMATS_ = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> FF_78_FORMATS_ = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> IE_FORMATS_ = new ConcurrentHashMap<>();

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
        final String yyyyDotBlankDotIE = "\u200EYYYY\u200E. \u200EMM\u200E. \u200Edd\u200E.";
        final String yyyyDotDot = "\u200EYYYY\u200E.\u200EMM\u200E.\u200Edd\u200E.";
        final String yyyyMinus = "\u200EYYYY\u200E-\u200EMM\u200E-\u200Edd";
        final String rightToLeft = "\u200Fdd\u200F/\u200FMM\u200F/\u200FYYYY";

        final Map<String, String> commonFormats = new HashMap<>();
        commonFormats.put("", mmSlash);
        commonFormats.put("ar", "dd\u200F/MM\u200F/YYYY");
        commonFormats.put("ar-SA", "d\u200F/M\u200F/YYYY هـ");
        commonFormats.put("ban", mmSlash);
        commonFormats.put("be", ddDot);
        commonFormats.put("bg", ddDot + "\u200E \u0433.");
        commonFormats.put("ca", ddSlash);
        commonFormats.put("cs", ddDotBlank);
        commonFormats.put("da", ddSlash);
        commonFormats.put("de", ddDot);
        commonFormats.put("el", ddSlash);
        commonFormats.put("en-NZ", ddSlash);
        commonFormats.put("en-PA", ddSlash);
        commonFormats.put("en-PR", ddSlash);
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
        commonFormats.put("es-US", mmSlash);
        commonFormats.put("et", ddDot);
        commonFormats.put("fi", ddDot);
        commonFormats.put("fr", ddSlash);
        commonFormats.put("fr-CA", yyyyDash);
        commonFormats.put("ga", yyyyDash);
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
        commonFormats.put("lv", yyyyDotDot);
        commonFormats.put("mk", ddDot);
        commonFormats.put("ms", ddSlash);
        commonFormats.put("mt", mmSlash);
        commonFormats.put("nl", ddDash);
        commonFormats.put("pl", ddDot);
        commonFormats.put("pt", ddSlash);
        commonFormats.put("ro", ddDot);
        commonFormats.put("ru", ddDot);
        commonFormats.put("sk", ddDot);
        commonFormats.put("sl", ddDotBlank);
        commonFormats.put("sq", ddSlash);
        commonFormats.put("sr", ddDotBlankDot);
        commonFormats.put("sv", yyyyDash);
        commonFormats.put("th", ddSlash);
        commonFormats.put("tr", ddDot);
        commonFormats.put("uk", ddDot);
        commonFormats.put("vi", ddSlash);
        commonFormats.put("zh", yyyySlash);
        commonFormats.put("zh-HK", ddSlash);
        commonFormats.put("zh-SG", "\u200EYYYY\u200E\u5E74\u200EMM\u200E\u6708\u200Edd\u200E\u65E5");

        CHROME_FORMATS_.putAll(commonFormats);
        EDGE_FORMATS_.putAll(commonFormats);

        IE_FORMATS_.putAll(commonFormats);

        commonFormats.put("en-CA", yyyyDash);
        commonFormats.put("en-PH", ddSlash);
        commonFormats.put("es-US", ddSlash);
        commonFormats.put("ga", ddSlash);
        commonFormats.put("fr-CH", ddDot);
        commonFormats.put("nl-BE", ddSlash);
        commonFormats.put("sk", ddDotBlank);
        commonFormats.put("sr", ddDotDot);
        commonFormats.put("sq", ddDot);

        FF_FORMATS_.putAll(commonFormats);
        FF_FORMATS_.put("ban", ddDot);
        FF_FORMATS_.put("da", ddDot);

        FF_78_FORMATS_.putAll(commonFormats);
        FF_78_FORMATS_.put("da", ddDot);

        CHROME_FORMATS_.put("be", mmSlash);
        CHROME_FORMATS_.put("da", ddDot);
        CHROME_FORMATS_.put("en-CA", yyyyDash);
        CHROME_FORMATS_.put("en-IE", ddSlash);
        CHROME_FORMATS_.put("en-MT", ddSlash);
        CHROME_FORMATS_.put("en-PH", ddSlash);
        CHROME_FORMATS_.put("es-US", ddSlash);
        CHROME_FORMATS_.put("fr-CH", ddDot);
        CHROME_FORMATS_.put("ga", mmSlash);
        CHROME_FORMATS_.put("hr", ddDotBlankDot);
        CHROME_FORMATS_.put("in-ID", ddSlash);
        CHROME_FORMATS_.put("is", mmSlash);
        CHROME_FORMATS_.put("iw", ddDot);
        CHROME_FORMATS_.put("mk", mmSlash);
        CHROME_FORMATS_.put("nl-BE", ddSlash);
        CHROME_FORMATS_.put("sk", ddDotBlank);
        CHROME_FORMATS_.put("sq", mmSlash);
        CHROME_FORMATS_.put("sr", ddDotDot);

        EDGE_FORMATS_.put("be", mmSlash);
        EDGE_FORMATS_.put("da", ddDot);
        EDGE_FORMATS_.put("en-CA", yyyyDash);
        EDGE_FORMATS_.put("en-IE", ddSlash);
        EDGE_FORMATS_.put("en-MT", ddSlash);
        EDGE_FORMATS_.put("en-PH", ddSlash);
        EDGE_FORMATS_.put("es-US", ddSlash);
        EDGE_FORMATS_.put("fr-CH", ddDot);
        EDGE_FORMATS_.put("ga", mmSlash);
        EDGE_FORMATS_.put("hr", ddDotBlankDot);
        EDGE_FORMATS_.put("in-ID", ddSlash);
        EDGE_FORMATS_.put("is", mmSlash);
        EDGE_FORMATS_.put("iw", ddDot);
        EDGE_FORMATS_.put("mk", mmSlash);
        EDGE_FORMATS_.put("nl-BE", ddSlash);
        EDGE_FORMATS_.put("sk", ddDotBlank);
        EDGE_FORMATS_.put("sq", mmSlash);
        EDGE_FORMATS_.put("sr", ddDotDot);

        IE_FORMATS_.put("ar", rightToLeft);
        IE_FORMATS_.put("ar-AE", rightToLeft);
        IE_FORMATS_.put("ar-BH", rightToLeft);
        IE_FORMATS_.put("ar-DZ", "\u200Fdd\u200F-\u200FMM\u200F-\u200FYYYY");
        IE_FORMATS_.put("ar-LY", rightToLeft);
        IE_FORMATS_.put("ar-MA", "\u200Fdd\u200F-\u200FMM\u200F-\u200FYYYY");
        IE_FORMATS_.put("ar-TN", "\u200Fdd\u200F-\u200FMM\u200F-\u200FYYYY");
        IE_FORMATS_.put("ar-EG", rightToLeft);
        IE_FORMATS_.put("ar-IQ", rightToLeft);
        IE_FORMATS_.put("ar-JO", rightToLeft);
        IE_FORMATS_.put("ar-KW", rightToLeft);
        IE_FORMATS_.put("ar-LB", rightToLeft);
        IE_FORMATS_.put("ar-OM", rightToLeft);
        IE_FORMATS_.put("ar-QA", rightToLeft);
        IE_FORMATS_.put("ar-SA", rightToLeft);
        IE_FORMATS_.put("ar-SD", rightToLeft);
        IE_FORMATS_.put("ar-SY", rightToLeft);
        IE_FORMATS_.put("ar-YE", rightToLeft);
        IE_FORMATS_.put("ban", ddDot);
        IE_FORMATS_.put("cs", ddDot);
        IE_FORMATS_.put("da", ddDash);
        IE_FORMATS_.put("en-IN", ddDash);
        IE_FORMATS_.put("en-MT", ddSlash);
        IE_FORMATS_.put("en-CA", yyyyMinus);
        IE_FORMATS_.put("en-PH", ddSlash);
        IE_FORMATS_.put("es-PH", ddSlash);
        IE_FORMATS_.put("es-PR", mmSlash);
        IE_FORMATS_.put("es-US", mmSlash);
        IE_FORMATS_.put("fr-CH", ddDot);
        IE_FORMATS_.put("ga", ddSlash);
        IE_FORMATS_.put("hi", ddDash);
        IE_FORMATS_.put("hr", ddDotDot);
        IE_FORMATS_.put("hu", yyyyDotBlankDotIE);
        IE_FORMATS_.put("hu-HU", yyyyDotBlankDotIE);
        IE_FORMATS_.put("iw", ddSlash);
        IE_FORMATS_.put("it-CH", ddDot);
        IE_FORMATS_.put("ja", "\u200EYYYY\u200E\u5E74\u200EMM\u200E\u6708\u200Edd\u200E\u65E5");
        IE_FORMATS_.put("ja-JP-u-ca-japanese", "\u200Eyy\u200E/\u200EMM\u200E/\u200Edd");
        IE_FORMATS_.put("ko", "\u200EYYYY\u200E\uB144 \u200EMM\u200E\uC6D4 \u200Edd\u200E\uC77C");
        IE_FORMATS_.put("lt", yyyyMinus);
        IE_FORMATS_.put("lv", ddDot);
        IE_FORMATS_.put("mt", ddSlash);
        IE_FORMATS_.put("nl-BE", ddSlash);
        IE_FORMATS_.put("no", ddDot);
        IE_FORMATS_.put("pl", ddDot);
        IE_FORMATS_.put("pt-PT", ddSlash);
        IE_FORMATS_.put("sk", ddDotBlank);
        IE_FORMATS_.put("sl", ddDotBlank);
        IE_FORMATS_.put("sq", ddDot);
        IE_FORMATS_.put("sr", ddDotDot);
        IE_FORMATS_.put("sr-BA", ddDot);
        IE_FORMATS_.put("sr-CS", ddDot);
        IE_FORMATS_.put("sr-ME", ddDot);
        IE_FORMATS_.put("sr-RS", ddDot);
        IE_FORMATS_.put("zh", "\u200EYYYY\u200E\u5E74\u200EMM\u200E\u6708\u200Edd\u200E\u65E5");
        IE_FORMATS_.put("zh-HK", "\u200EYYYY\u200E\u5E74\u200EMM\u200E\u6708\u200Edd\u200E\u65E5");
    }

    /**
     * Default constructor.
     */
    public DateTimeFormat() {
    }

    private DateTimeFormat(final String[] locales, final BrowserVersion browserVersion) {
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
        else if (browserVersion.isFirefox78()) {
            formats = FF_78_FORMATS_;
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
        }
        if (!browserVersion.hasFeature(JS_DATE_WITH_LEFT_TO_RIGHT_MARK) && !locale.startsWith("ar")) {
            pattern = pattern.replace("\u200E", "");
        }

        formatter_ = new DateTimeFormatHelper(locale, browserVersion, pattern);
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
        final DateTimeFormat format = new DateTimeFormat(locales, window.getBrowserVersion());
        format.setParentScope(window);
        format.setPrototype(window.getPrototype(format.getClass()));
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
        return formatter_.format(date);
    }

    /**
     * @return A new object with properties reflecting the locale and date and time formatting options
     * computed during the initialization of the given {@code DateTimeFormat} object.
     */
    @JsxFunction
    public Scriptable resolvedOptions() {
        final Scriptable object = Context.getCurrentContext().newObject(getParentScope());
        return object;
    }

    /**
     * Helper.
     */
    static final class DateTimeFormatHelper {

        private final DateTimeFormatter formatter_;
        private Chronology chronology_;

        DateTimeFormatHelper(final String locale, final BrowserVersion browserVersion, final String pattern) {
            if (locale.startsWith("ar")
                    && (!"ar-DZ".equals(locale)
                                    && !"ar-LY".equals(locale)
                                    && !"ar-MA".equals(locale)
                                    && !"ar-TN".equals(locale))) {
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

                case "ar":
                    if (browserVersion.hasFeature(JS_DATE_WITH_LEFT_TO_RIGHT_MARK)) {
                        chronology_ = HijrahChronology.INSTANCE;
                    }
                    break;

                case "ar-SA":
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
         * @return the dated formated
         */
        String format(final Date date) {
            TemporalAccessor zonedDateTime = date.toInstant().atZone(ZoneId.systemDefault());
            if (chronology_ != null) {
                zonedDateTime = chronology_.date(zonedDateTime);
            }
            return formatter_.format(zonedDateTime);
        }
    }
}
