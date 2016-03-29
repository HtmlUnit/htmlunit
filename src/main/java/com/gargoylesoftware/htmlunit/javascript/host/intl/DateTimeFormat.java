/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxClass;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxConstructor;
import com.gargoylesoftware.htmlunit.javascript.configuration.JsxFunction;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Function;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

/**
 * A JavaScript object for {@code DateTimeFormat}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@JsxClass
public class DateTimeFormat extends SimpleScriptable {

    private static Map<String, String> FF_38_FORMATS_ = new HashMap<>();
    private static Map<String, String> FF_45_FORMATS_ = new HashMap<>();
    private static Map<String, String> CHROME_FORMATS_ = new HashMap<>();
    private static Map<String, String> IE_FORMATS_ = new HashMap<>();

    private DateFormat format_;

    static {
        final String ddSlash = "\u200Edd\u200E/\u200EMM\u200E/\u200EYYYY";
        final String ddDash = "\u200Edd\u200E-\u200EMM\u200E-\u200EYYYY";
        final String ddDot = "\u200Edd\u200E.\u200EMM\u200E.\u200EYYYY";
        final String ddDotDot = "\u200Edd\u200E.\u200EMM\u200E.\u200EYYYY.";
        final String ddDotBlank = "\u200Edd\u200E. \u200EMM\u200E. \u200EYYYY";
        final String ddDotBlankDot = "\u200Edd\u200E. \u200EMM\u200E. \u200EYYYY.";
        final String mmSlash = "\u200EMM\u200E/\u200Edd\u200E/\u200EYYYY";
        final String yyyyMmDdSlash = "\u200EYYYY\u200E/\u200EMM\u200E/\u200Edd";
        final String yyyyMmDdDash = "\u200EYYYY\u200E-\u200EMM\u200E-\u200Edd";

        FF_38_FORMATS_.put("", mmSlash);
        FF_38_FORMATS_.put("ar", "dd\u200F/MM\u200F/YYYY");
        FF_38_FORMATS_.put("be", ddDot);
        FF_38_FORMATS_.put("ca", ddSlash);
        FF_38_FORMATS_.put("cs", ddDotBlank);
        FF_38_FORMATS_.put("da", ddSlash);
        FF_38_FORMATS_.put("de", ddDot);
        FF_38_FORMATS_.put("en-NZ", ddSlash);
        FF_38_FORMATS_.put("en-PA", ddSlash);
        FF_38_FORMATS_.put("en-PR", ddSlash);
        FF_38_FORMATS_.put("en-AU", ddSlash);
        FF_38_FORMATS_.put("en-GB", ddSlash);
        FF_38_FORMATS_.put("en-IE", ddSlash);
        FF_38_FORMATS_.put("en-IN", ddSlash);
        FF_38_FORMATS_.put("en-MT", ddSlash);
        FF_38_FORMATS_.put("en-SG", ddSlash);
        FF_38_FORMATS_.put("en-ZA", yyyyMmDdSlash);

        FF_45_FORMATS_.putAll(FF_38_FORMATS_);
        CHROME_FORMATS_.putAll(FF_38_FORMATS_);
        IE_FORMATS_.putAll(FF_38_FORMATS_);

        FF_38_FORMATS_.put("sr-BA", ddDotBlankDot);
        FF_38_FORMATS_.put("sr-CS", ddDotBlankDot);
        FF_38_FORMATS_.put("sr-ME", ddDotBlankDot);
        FF_38_FORMATS_.put("sr-RS", ddDotBlankDot);

        FF_45_FORMATS_.putAll(FF_38_FORMATS_);
        FF_45_FORMATS_.put("en-CA", yyyyMmDdDash);
        FF_45_FORMATS_.put("en-PH", ddSlash);
        FF_45_FORMATS_.put("es-US", ddSlash);
        FF_45_FORMATS_.put("sr-BA", ddDotDot);
        FF_45_FORMATS_.put("sr-CS", ddDotDot);
        FF_45_FORMATS_.put("sr-ME", ddDotDot);
        FF_45_FORMATS_.put("sr-RS", ddDotDot);

        CHROME_FORMATS_.put("be", yyyyMmDdDash);
        CHROME_FORMATS_.put("en-CA", yyyyMmDdDash);
        CHROME_FORMATS_.put("en-IE", mmSlash);
        CHROME_FORMATS_.put("en-IN", mmSlash);
        CHROME_FORMATS_.put("en-MT", mmSlash);
        CHROME_FORMATS_.put("en-SG", mmSlash);
        CHROME_FORMATS_.put("es-PA", ddSlash);
        CHROME_FORMATS_.put("es-PR", ddSlash);
        CHROME_FORMATS_.put("es-US", ddSlash);
        CHROME_FORMATS_.put("sr-BA", ddDotDot);
        CHROME_FORMATS_.put("sr-CS", ddDotDot);
        CHROME_FORMATS_.put("sr-ME", ddDotDot);
        CHROME_FORMATS_.put("sr-RS", ddDotDot);

        IE_FORMATS_.put("ar-EG", "\u200Fdd\u200F/\u200FMM\u200F/\u200FYYYY");
        IE_FORMATS_.put("cs", ddDot);
        IE_FORMATS_.put("da", ddDash);
        IE_FORMATS_.put("en-IN", ddDash);
        IE_FORMATS_.put("en-MT", mmSlash);
        IE_FORMATS_.put("en-CA", ddSlash);
    }

    /**
     * Default constructor.
     */
    public DateTimeFormat() {
    }

    private DateTimeFormat(final String locale, final BrowserVersion browserVersion) {
        final Map<String, String> formats;
        if (browserVersion.isChrome()) {
            formats = CHROME_FORMATS_;
        }
        else if (browserVersion.isIE()) {
            formats = IE_FORMATS_;
        }
        else if (browserVersion.isFirefox() && browserVersion.getBrowserVersionNumeric() == 45) {
            formats = FF_45_FORMATS_;
        }
        else {
            formats = FF_38_FORMATS_;
        }
        String pattern = formats.get(locale);
        if (pattern == null && locale.indexOf('-') != -1) {
            pattern = formats.get(locale.substring(0,  locale.indexOf('-')));
        }
        if (pattern == null) {
            pattern = formats.get("");
        }
        if (!browserVersion.hasFeature(JS_DATE_WITH_LEFT_TO_RIGHT_MARK) && !locale.startsWith("ar")) {
            pattern = pattern.replace("\u200E", "");
        }

        format_ = new SimpleDateFormat(pattern);
        if (locale.startsWith("ar")) {
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
        final String locale;
        if (args.length != 0) {
            locale = Context.toString(args[0]);
        }
        else {
            locale = "";
        }
        final Window window = getWindow(ctorObj);
        final DateTimeFormat format = new DateTimeFormat(locale, window.getBrowserVersion());
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
        return format_.format(date);
    }

}
