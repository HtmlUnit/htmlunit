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

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_DATE_WITH_LEFT_TO_RIGHT_MARK;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.gae.GAEUtils;
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

    private static Map<String, String> FF_45_FORMATS_ = new HashMap<>();
    private static Map<String, String> FF_52_FORMATS_ = new HashMap<>();
    private static Map<String, String> CHROME_FORMATS_ = new HashMap<>();
    private static Map<String, String> IE_FORMATS_ = new HashMap<>();

    private AbstractDateTimeFormatter formatter_;

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
        final String yyyyDot = "\u200EYYYY\u200E.\u200EMM\u200E.\u200Edd";
        final String yyyyDotBlankDot = "\u200EYYYY\u200E. \u200EMM\u200E. \u200Edd.";
        final String yyyyDotDot = "\u200EYYYY\u200E.\u200EMM\u200E.\u200Edd\u200E.";
        final String rightToLeft = "\u200Fdd\u200F/\u200FMM\u200F/\u200FYYYY";

        FF_45_FORMATS_.put("", mmSlash);
        FF_45_FORMATS_.put("ar", "dd\u200F/MM\u200F/YYYY");
        FF_45_FORMATS_.put("ar-SA", "d\u200F/M\u200F/YYYY هـ");
        FF_45_FORMATS_.put("ban", ddDot);
        FF_45_FORMATS_.put("be", ddDot);
        FF_45_FORMATS_.put("bg", ddDot + "\u200E \u0433.");
        FF_45_FORMATS_.put("ca", ddSlash);
        FF_45_FORMATS_.put("cs", ddDotBlank);
        FF_45_FORMATS_.put("da", ddSlash);
        FF_45_FORMATS_.put("de", ddDot);
        FF_45_FORMATS_.put("el", ddSlash);
        FF_45_FORMATS_.put("en-NZ", ddSlash);
        FF_45_FORMATS_.put("en-PA", ddSlash);
        FF_45_FORMATS_.put("en-PR", ddSlash);
        FF_45_FORMATS_.put("en-AU", ddSlash);
        FF_45_FORMATS_.put("en-GB", ddSlash);
        FF_45_FORMATS_.put("en-IE", ddSlash);
        FF_45_FORMATS_.put("en-IN", ddSlash);
        FF_45_FORMATS_.put("en-MT", ddSlash);
        FF_45_FORMATS_.put("en-SG", ddSlash);
        FF_45_FORMATS_.put("en-ZA", yyyySlash);
        FF_45_FORMATS_.put("es", ddSlash);
        FF_45_FORMATS_.put("es-CL", ddDash);
        FF_45_FORMATS_.put("es-PA", mmSlash);
        FF_45_FORMATS_.put("es-PR", mmSlash);
        FF_45_FORMATS_.put("es-US", mmSlash);
        FF_45_FORMATS_.put("et", ddDot);
        FF_45_FORMATS_.put("fi", ddDot);
        FF_45_FORMATS_.put("fr", ddSlash);
        FF_45_FORMATS_.put("fr-CA", yyyyDash);
        FF_45_FORMATS_.put("ga", yyyyDash);
        FF_45_FORMATS_.put("hi", ddSlash);
        FF_45_FORMATS_.put("hr", ddDotBlankDot);
        FF_45_FORMATS_.put("hu", yyyyDotBlankDot);
        FF_45_FORMATS_.put("id", ddSlash);
        FF_45_FORMATS_.put("in", ddSlash);
        FF_45_FORMATS_.put("is", ddDot);
        FF_45_FORMATS_.put("it", ddSlash);
        FF_45_FORMATS_.put("iw", ddDot);
        FF_45_FORMATS_.put("ja", yyyySlash);
        FF_45_FORMATS_.put("ko", yyyyDotBlankDot);
        FF_45_FORMATS_.put("lt", yyyyDash);
        FF_45_FORMATS_.put("lv", ddDotDot);
        FF_45_FORMATS_.put("mk", ddDot);
        FF_45_FORMATS_.put("ms", ddSlash);
        FF_45_FORMATS_.put("mt", yyyyDash);
        FF_45_FORMATS_.put("nl", ddDash);
        FF_45_FORMATS_.put("pl", ddDot);
        FF_45_FORMATS_.put("pt", ddSlash);
        FF_45_FORMATS_.put("ro", ddDot);
        FF_45_FORMATS_.put("ru", ddDot);
        FF_45_FORMATS_.put("sk", ddDot);
        FF_45_FORMATS_.put("sl", ddDotBlank);
        FF_45_FORMATS_.put("sq", ddSlash);
        FF_45_FORMATS_.put("sr", ddDotBlankDot);
        FF_45_FORMATS_.put("sv", yyyyDash);
        FF_45_FORMATS_.put("th", ddSlash);
        FF_45_FORMATS_.put("tr", ddDot);
        FF_45_FORMATS_.put("uk", ddDot);
        FF_45_FORMATS_.put("vi", ddSlash);
        FF_45_FORMATS_.put("zh", yyyySlash);
        FF_45_FORMATS_.put("zh-HK", ddSlash);
        FF_45_FORMATS_.put("zh-SG", "\u200EYYYY\u200E\u5E74\u200EMM\u200E\u6708\u200Edd\u200E\u65E5");

        CHROME_FORMATS_.putAll(FF_45_FORMATS_);
        IE_FORMATS_.putAll(FF_45_FORMATS_);

        FF_45_FORMATS_.put("en-CA", yyyyDash);
        FF_45_FORMATS_.put("en-PH", ddSlash);
        FF_45_FORMATS_.put("es-US", ddSlash);
        FF_45_FORMATS_.put("ga", ddSlash);
        FF_45_FORMATS_.put("hr", ddDotDot);
        FF_45_FORMATS_.put("ja-JP-u-ca-japanese", "yy/MM/dd");
        FF_45_FORMATS_.put("sk", ddDotBlank);
        FF_45_FORMATS_.put("sr", ddDotDot);
        FF_45_FORMATS_.put("sq", ddDot);

        FF_52_FORMATS_.putAll(FF_45_FORMATS_);
        FF_52_FORMATS_.put("fr", ddSlash);
        FF_52_FORMATS_.put("hr", ddDotBlankDot);
        FF_52_FORMATS_.put("fr-CH", ddDot);
        FF_52_FORMATS_.put("lv", yyyyDotDot);
        FF_52_FORMATS_.put("mt", ddSlash);
        FF_52_FORMATS_.put("nl-BE", ddSlash);

        CHROME_FORMATS_.put("be", yyyyDash);
        CHROME_FORMATS_.put("en-CA", yyyyDash);
        CHROME_FORMATS_.put("en-IE", mmSlash);
        CHROME_FORMATS_.put("en-MT", mmSlash);
        CHROME_FORMATS_.put("es-US", ddSlash);
        CHROME_FORMATS_.put("fr", ddSlash);
        CHROME_FORMATS_.put("fr-CH", ddDot);
        CHROME_FORMATS_.put("hr", ddDotBlankDot);
        CHROME_FORMATS_.put("in", ddDot);
        CHROME_FORMATS_.put("in-ID", ddSlash);
        CHROME_FORMATS_.put("is", yyyyDash);
        CHROME_FORMATS_.put("iw", ddDot);
        CHROME_FORMATS_.put("ja-JP-u-ca-japanese", "平成yy/MM/dd");
        CHROME_FORMATS_.put("lv", yyyyDotDot);
        CHROME_FORMATS_.put("sk", ddDotBlank);
        CHROME_FORMATS_.put("sq", yyyyDash);
        CHROME_FORMATS_.put("sr", ddDotDot);
        CHROME_FORMATS_.put("mk", yyyyDash);

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
        IE_FORMATS_.put("ban", mmSlash);
        IE_FORMATS_.put("cs", ddDot);
        IE_FORMATS_.put("da", ddDash);
        IE_FORMATS_.put("en-IN", ddDash);
        IE_FORMATS_.put("en-MT", mmSlash);
        IE_FORMATS_.put("en-CA", ddSlash);
        IE_FORMATS_.put("es-PR", ddSlash);
        IE_FORMATS_.put("es-US", mmSlash);
        IE_FORMATS_.put("fr-CH", ddDot);
        IE_FORMATS_.put("ga", ddSlash);
        IE_FORMATS_.put("hi", ddDash);
        IE_FORMATS_.put("hr", ddDotDot);
        IE_FORMATS_.put("hu", yyyyDotDot);
        IE_FORMATS_.put("iw", ddSlash);
        IE_FORMATS_.put("it-CH", ddDot);
        IE_FORMATS_.put("ja", "\u200EYYYY\u200E\u5E74\u200EMM\u200E\u6708\u200Edd\u200E\u65E5");
        IE_FORMATS_.put("ja-JP-u-ca-japanese", "\u200E平成\u200E \u200Eyy\u200E年\u200EMM\u200E月\u200Edd\u200E日");
        IE_FORMATS_.put("ko", "\u200EYYYY\u200E\uB144 \u200EMM\u200E\uC6D4 \u200Edd\u200E\uC77C");
        IE_FORMATS_.put("lt", yyyyDot);
        IE_FORMATS_.put("lv", yyyyDotDot);
        IE_FORMATS_.put("mt", ddSlash);
        IE_FORMATS_.put("nl-BE", ddSlash);
        IE_FORMATS_.put("no", ddDot);
        IE_FORMATS_.put("pl", yyyyDash);
        IE_FORMATS_.put("pt-PT", ddDash);
        IE_FORMATS_.put("sk", ddDotBlank);
        IE_FORMATS_.put("sl", ddDot);
        IE_FORMATS_.put("sq", yyyyDash);
        IE_FORMATS_.put("sr", ddDot);
        IE_FORMATS_.put("sr-BA", mmSlash);
        IE_FORMATS_.put("sr-CS", mmSlash);
        IE_FORMATS_.put("sr-ME", mmSlash);
        IE_FORMATS_.put("sr-RS", mmSlash);
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
        else if (browserVersion.isIE()) {
            formats = IE_FORMATS_;
        }
        else if (!browserVersion.isFirefox52()) {
            formats = FF_45_FORMATS_;
        }
        else {
            formats = FF_52_FORMATS_;
        }

        String locale = "";
        String pattern = null;

        for (String l : locales) {
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

        if (GAEUtils.isGaeMode()) {
            formatter_ = new GAEDateTimeFormatter(locale, browserVersion, pattern);
        }
        else {
            formatter_ = new DefaultDateTimeFormatter(locale, browserVersion, pattern);
        }
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

}
