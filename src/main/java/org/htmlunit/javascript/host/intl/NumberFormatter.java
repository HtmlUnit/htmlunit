/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * The formatting engine of {@link NumberFormat}: rounding, grouping, notation, sign and
 * the affixes of the styles, implemented like the abstract operations of the
 * ECMAScript Internationalization API. The locale specific affixes are taken from the jdk;
 * the unit patterns are the English ones.
 *
 * @author Ronald Brill
 * @see <a href="https://tc39.es/ecma402/#sec-formatnumber">spec</a>
 */
final class NumberFormatter {

    /** The resolved options of a {@link NumberFormat}. */
    static final class Options implements java.io.Serializable {
        private String style_ = "decimal";
        private String currency_;
        private String currencyDisplay_ = "symbol";
        private String currencySign_ = "standard";
        private String unit_;
        private String unitDisplay_ = "short";
        private int minimumIntegerDigits_ = 1;
        private int minimumFractionDigits_;
        private int maximumFractionDigits_ = 3;
        private int minimumSignificantDigits_;
        private int maximumSignificantDigits_;
        private String roundingType_ = "fractionDigits";
        private int roundingIncrement_ = 1;
        private String roundingMode_ = "halfExpand";
        private String trailingZeroDisplay_ = "auto";
        private String useGrouping_ = "auto";
        private String notation_ = "standard";
        private String compactDisplay_ = "short";
        private String signDisplay_ = "auto";

        String getStyle() {
            return style_;
        }

        void setStyle(final String style) {
            style_ = style;
        }

        String getCurrency() {
            return currency_;
        }

        void setCurrency(final String currency) {
            currency_ = currency;
        }

        String getCurrencyDisplay() {
            return currencyDisplay_;
        }

        void setCurrencyDisplay(final String currencyDisplay) {
            currencyDisplay_ = currencyDisplay;
        }

        String getCurrencySign() {
            return currencySign_;
        }

        void setCurrencySign(final String currencySign) {
            currencySign_ = currencySign;
        }

        String getUnit() {
            return unit_;
        }

        void setUnit(final String unit) {
            unit_ = unit;
        }

        String getUnitDisplay() {
            return unitDisplay_;
        }

        void setUnitDisplay(final String unitDisplay) {
            unitDisplay_ = unitDisplay;
        }

        int getMinimumIntegerDigits() {
            return minimumIntegerDigits_;
        }

        void setMinimumIntegerDigits(final int minimumIntegerDigits) {
            minimumIntegerDigits_ = minimumIntegerDigits;
        }

        int getMinimumFractionDigits() {
            return minimumFractionDigits_;
        }

        void setMinimumFractionDigits(final int minimumFractionDigits) {
            minimumFractionDigits_ = minimumFractionDigits;
        }

        int getMaximumFractionDigits() {
            return maximumFractionDigits_;
        }

        void setMaximumFractionDigits(final int maximumFractionDigits) {
            maximumFractionDigits_ = maximumFractionDigits;
        }

        int getMinimumSignificantDigits() {
            return minimumSignificantDigits_;
        }

        void setMinimumSignificantDigits(final int minimumSignificantDigits) {
            minimumSignificantDigits_ = minimumSignificantDigits;
        }

        int getMaximumSignificantDigits() {
            return maximumSignificantDigits_;
        }

        void setMaximumSignificantDigits(final int maximumSignificantDigits) {
            maximumSignificantDigits_ = maximumSignificantDigits;
        }

        String getRoundingType() {
            return roundingType_;
        }

        void setRoundingType(final String roundingType) {
            roundingType_ = roundingType;
        }

        int getRoundingIncrement() {
            return roundingIncrement_;
        }

        void setRoundingIncrement(final int roundingIncrement) {
            roundingIncrement_ = roundingIncrement;
        }

        String getRoundingMode() {
            return roundingMode_;
        }

        void setRoundingMode(final String roundingMode) {
            roundingMode_ = roundingMode;
        }

        String getTrailingZeroDisplay() {
            return trailingZeroDisplay_;
        }

        void setTrailingZeroDisplay(final String trailingZeroDisplay) {
            trailingZeroDisplay_ = trailingZeroDisplay;
        }

        String getUseGrouping() {
            return useGrouping_;
        }

        void setUseGrouping(final String useGrouping) {
            useGrouping_ = useGrouping;
        }

        String getNotation() {
            return notation_;
        }

        void setNotation(final String notation) {
            notation_ = notation;
        }

        String getCompactDisplay() {
            return compactDisplay_;
        }

        void setCompactDisplay(final String compactDisplay) {
            compactDisplay_ = compactDisplay;
        }

        String getSignDisplay() {
            return signDisplay_;
        }

        void setSignDisplay(final String signDisplay) {
            signDisplay_ = signDisplay;
        }
    }

    // English currency names without a plural form
    private static final Set<String> INVARIANT_CURRENCY_NAMES = Set.of("yen", "yuan", "won", "baht");

    // the English short forms of the time units used after 'per' (e.g. 'kB/s')
    private static final Map<String, String> PER_UNITS = Map.of(
            "second", "s", "minute", "min", "hour", "h", "day", "d", "week", "w", "month", "m", "year", "y");

    // English unit patterns: short, narrow, long (singular), long (plural); short and narrow
    // may have a plural form separated by '|'
    private static final Map<String, String[]> UNITS = new HashMap<>();

    static {
        unit("acre", "{0} ac", "{0}ac", "acre", "acres");
        unit("bit", "{0} bit", "{0}bit", "bit", "bits");
        unit("byte", "{0} byte", "{0}B", "byte", "bytes");
        unit("celsius", "{0}°C", "{0}°C", "degree Celsius", "degrees Celsius");
        unit("centimeter", "{0} cm", "{0}cm", "centimeter", "centimeters");
        unit("day", "{0} day|{0} days", "{0}d", "day", "days");
        unit("degree", "{0} deg", "{0}°", "degree", "degrees");
        unit("fahrenheit", "{0}°F", "{0}°", "degree Fahrenheit", "degrees Fahrenheit");
        unit("fluid-ounce", "{0} fl oz", "{0}fl oz", "fluid ounce", "fluid ounces");
        unit("foot", "{0} ft", "{0}′", "foot", "feet");
        unit("gallon", "{0} gal", "{0}gal", "gallon", "gallons");
        unit("gigabit", "{0} Gb", "{0}Gb", "gigabit", "gigabits");
        unit("gigabyte", "{0} GB", "{0}GB", "gigabyte", "gigabytes");
        unit("gram", "{0} g", "{0}g", "gram", "grams");
        unit("hectare", "{0} ha", "{0}ha", "hectare", "hectares");
        unit("hour", "{0} hr", "{0}h", "hour", "hours");
        unit("inch", "{0} in", "{0}″", "inch", "inches");
        unit("kilobit", "{0} kb", "{0}kb", "kilobit", "kilobits");
        unit("kilobyte", "{0} kB", "{0}kB", "kilobyte", "kilobytes");
        unit("kilogram", "{0} kg", "{0}kg", "kilogram", "kilograms");
        unit("kilometer", "{0} km", "{0}km", "kilometer", "kilometers");
        unit("liter", "{0} L", "{0}L", "liter", "liters");
        unit("megabit", "{0} Mb", "{0}Mb", "megabit", "megabits");
        unit("megabyte", "{0} MB", "{0}MB", "megabyte", "megabytes");
        unit("meter", "{0} m", "{0}m", "meter", "meters");
        unit("microsecond", "{0} μs", "{0}μs", "microsecond", "microseconds");
        unit("mile", "{0} mi", "{0}mi", "mile", "miles");
        unit("mile-scandinavian", "{0} smi", "{0}smi", "mile-scandinavian", "miles-scandinavian");
        unit("milliliter", "{0} mL", "{0}mL", "milliliter", "milliliters");
        unit("millimeter", "{0} mm", "{0}mm", "millimeter", "millimeters");
        unit("millisecond", "{0} ms", "{0}ms", "millisecond", "milliseconds");
        unit("minute", "{0} min", "{0}m", "minute", "minutes");
        unit("month", "{0} mth|{0} mths", "{0}m", "month", "months");
        unit("nanosecond", "{0} ns", "{0}ns", "nanosecond", "nanoseconds");
        unit("ounce", "{0} oz", "{0}oz", "ounce", "ounces");
        unit("percent", "{0}%", "{0}%", "percent", "percent");
        unit("petabyte", "{0} PB", "{0}PB", "petabyte", "petabytes");
        unit("pound", "{0} lb", "{0}#", "pound", "pounds");
        unit("second", "{0} sec", "{0}s", "second", "seconds");
        unit("stone", "{0} st", "{0}st", "stone", "stones");
        unit("terabit", "{0} Tb", "{0}Tb", "terabit", "terabits");
        unit("terabyte", "{0} TB", "{0}TB", "terabyte", "terabytes");
        unit("week", "{0} wk|{0} wks", "{0}w", "week", "weeks");
        unit("yard", "{0} yd", "{0}yd", "yard", "yards");
        unit("year", "{0} yr|{0} yrs", "{0}y", "year", "years");

        // compound units with patterns of their own
        unit("kilometer-per-hour", "{0} km/h", "{0}km/h", "kilometer per hour", "kilometers per hour");
        unit("meter-per-second", "{0} m/s", "{0}m/s", "meter per second", "meters per second");
        unit("mile-per-hour", "{0} mph", "{0}mph", "mile per hour", "miles per hour");
        unit("mile-per-gallon", "{0} mpg", "{0}mpg", "mile per gallon", "miles per gallon");
        unit("liter-per-kilometer", "{0} L/km", "{0}L/km", "liter per kilometer", "liters per kilometer");
    }

    private static void unit(final String name, final String shortPattern, final String narrowPattern,
            final String longSingular, final String longPlural) {
        UNITS.put(name, new String[] {shortPattern, narrowPattern, "{0} " + longSingular, "{0} " + longPlural});
    }

    /**
     * A part of a formatted number.
     *
     * @param type the type (e.g. 'integer' or 'currency')
     * @param value the value
     * @param source the source of a part of a formatted range (startRange, endRange or shared);
     *        {@code null} for a single number
     */
    record Part(String type, String value, String source) {
        Part(final String partType, final String partValue) {
            this(partType, partValue, null);
        }

        Part withSource(final String newSource) {
            return new Part(type, value, newSource);
        }
    }

    /**
     * A mathematical value to format.
     *
     * @param number the value; a {@link BigDecimal} or a {@link Double} for NaN and the infinities
     * @param negative whether the value is negative (including -0)
     */
    record Value(Number number, boolean negative) {
        boolean isNaN() {
            return number instanceof Double d && d.isNaN();
        }
    }

    private final Options options_;
    private final Locale locale_;

    private final char zeroDigit_;
    private final char groupingSeparator_;
    private final char decimalSeparator_;
    private final String exponentSeparator_;
    private final String nan_;
    private final String infinity_;

    // the affixes of the style for positive and negative numbers
    private final String positivePrefix_;
    private final String positiveSuffix_;
    private final String negativePrefix_;
    private final String negativeSuffix_;
    private final char minusSign_;
    // the text of the currency used in the affixes; null for other styles
    private final String currencyDisplayText_;

    /**
     * Ctor.
     *
     * @param options the resolved options
     * @param locale the locale used for the symbols and affixes
     * @param separators the browser specific separators (grouping, decimal and zero digit; a blank
     *        means the jdk default)
     * @param zeroDigit the zero digit of the numbering system; {@code 0} for the default
     */
    NumberFormatter(final Options options, final Locale locale, final String separators, final char zeroDigit) {
        options_ = options;
        locale_ = locale;

        final DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(locale);
        char grouping = symbols.getGroupingSeparator();
        char decimal = symbols.getDecimalSeparator();
        char zero = symbols.getZeroDigit();
        if (separators.length() > 0 && separators.charAt(0) != ' ') {
            grouping = separators.charAt(0);
        }
        if (separators.length() > 1 && separators.charAt(1) != ' ') {
            decimal = separators.charAt(1);
        }
        if (separators.length() > 2 && separators.charAt(2) != ' ') {
            zero = separators.charAt(2);
        }
        if (zeroDigit != 0) {
            zero = zeroDigit;
            // the arabic numbering systems have separators of their own
            if (zeroDigit == '\u0660' || zeroDigit == '\u06f0') {
                grouping = '\u066c';
                decimal = '\u066b';
            }
        }
        groupingSeparator_ = grouping;
        decimalSeparator_ = decimal;
        zeroDigit_ = zero;
        exponentSeparator_ = symbols.getExponentSeparator();
        nan_ = symbols.getNaN();
        infinity_ = symbols.getInfinity();
        minusSign_ = symbols.getMinusSign();

        final DecimalFormat format;
        if ("percent".equals(options.getStyle())) {
            format = (DecimalFormat) java.text.NumberFormat.getPercentInstance(locale);
        }
        else if ("currency".equals(options.getStyle())) {
            Locale currencyLocale = locale;
            if ("accounting".equals(options.getCurrencySign())) {
                currencyLocale = new Locale.Builder().setLocale(locale)
                                    .setUnicodeLocaleKeyword("cf", "account").build();
            }
            format = (DecimalFormat) java.text.NumberFormat.getCurrencyInstance(currencyLocale);
            final Currency currency = getCurrency(options.getCurrency());
            if (currency != null) {
                format.setCurrency(currency);
            }
        }
        else {
            format = (DecimalFormat) java.text.NumberFormat.getNumberInstance(locale);
        }

        String positivePrefix = format.getPositivePrefix();
        String positiveSuffix = format.getPositiveSuffix();
        String negativePrefix = format.getNegativePrefix();
        String negativeSuffix = format.getNegativeSuffix();

        String currencyDisplayText = null;
        if ("currency".equals(options.getStyle())) {
            final String symbol = currencySymbol(format);
            final String display = currencyDisplay();
            currencyDisplayText = display;
            positivePrefix = currencySpacing(positivePrefix.replace(symbol, display), true);
            positiveSuffix = currencySpacing(positiveSuffix.replace(symbol, display), false);
            negativePrefix = currencySpacing(negativePrefix.replace(symbol, display), true);
            negativeSuffix = currencySpacing(negativeSuffix.replace(symbol, display), false);
        }

        currencyDisplayText_ = currencyDisplayText;
        positivePrefix_ = positivePrefix;
        positiveSuffix_ = positiveSuffix;
        negativePrefix_ = negativePrefix;
        negativeSuffix_ = negativeSuffix;
    }

    private static Currency getCurrency(final String code) {
        try {
            return Currency.getInstance(code);
        }
        catch (final IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Returns the number of fraction digits of the given currency (ISO 4217); 2 for unknown currencies.
     *
     * @param code the currency code
     * @return the number of fraction digits
     */
    static int currencyDigits(final String code) {
        final Currency currency = getCurrency(code);
        if (currency == null || currency.getDefaultFractionDigits() < 0) {
            return 2;
        }
        return currency.getDefaultFractionDigits();
    }

    // the currency symbol used by the jdk format
    private String currencySymbol(final DecimalFormat format) {
        final Currency currency = getCurrency(options_.getCurrency());
        if (currency == null) {
            return format.getDecimalFormatSymbols().getCurrencySymbol();
        }
        return currency.getSymbol(locale_);
    }

    private String currencyDisplay() {
        final Currency currency = getCurrency(options_.getCurrency());
        switch (options_.getCurrencyDisplay()) {
            case "code":
                return options_.getCurrency();
            case "name":
                return currency == null ? options_.getCurrency() : currency.getDisplayName(locale_);
            case "narrowSymbol":
                if (currency == null) {
                    return options_.getCurrency();
                }
                // e.g. 'US$' -> '$'
                final String symbol = currency.getSymbol(locale_);
                final String narrow = symbol.replaceAll("\\p{L}", "");
                return narrow.isEmpty() ? symbol : narrow;
            default:
                return currency == null ? options_.getCurrency() : currency.getSymbol(locale_);
        }
    }

    // the name of the currency; the plural form is only supported for English
    private String currencyName(final String number) {
        final Currency currency = getCurrency(options_.getCurrency());
        if (currency == null) {
            return options_.getCurrency();
        }
        final String name = currency.getDisplayName(locale_);
        if (!"en".equals(locale_.getLanguage())) {
            return name;
        }

        // e.g. 'US Dollar' -> 'US dollars'
        final int pos = name.lastIndexOf(' ') + 1;
        String last = name.substring(pos);
        last = Character.toLowerCase(last.charAt(0)) + last.substring(1);
        // English plural rules: 'one' for the integer 1
        if (!"1".equals(number) && !last.endsWith("s") && !INVARIANT_CURRENCY_NAMES.contains(last)) {
            last = last + "s";
        }
        return name.substring(0, pos) + last;
    }

    // a currency code or name is separated from the number by a no-break space
    private static String currencySpacing(final String affix, final boolean prefix) {
        if (affix.isEmpty()) {
            return affix;
        }
        if (prefix) {
            final char last = affix.charAt(affix.length() - 1);
            if (Character.isLetter(last)) {
                return affix + ' ';
            }
            return affix;
        }
        final char first = affix.charAt(0);
        if (Character.isLetter(first)) {
            return ' ' + affix;
        }
        return affix;
    }

    /**
     * Formats the given value.
     *
     * @param value the value; a {@link BigDecimal} or a {@link Double} for NaN and the infinities
     * @param negative whether the value is negative (including -0)
     * @return the formatted value
     */
    String format(final Number value, final boolean negative) {
        return join(formatToParts(value, negative));
    }

    /**
     * Joins the values of the given parts.
     *
     * @param parts the parts
     * @return the joined values
     */
    static String join(final List<Part> parts) {
        final StringBuilder result = new StringBuilder();
        for (final Part part : parts) {
            result.append(part.value());
        }
        return result.toString();
    }

    /**
     * Formats the given value into parts.
     *
     * @param value the value; a {@link BigDecimal} or a {@link Double} for NaN and the infinities
     * @param negative whether the value is negative (including -0)
     * @return the parts
     * @see <a href="https://tc39.es/ecma402/#sec-partitionnumberpattern">spec</a>
     */
    List<Part> formatToParts(final Number value, final boolean negative) {
        if (value instanceof Double d && d.isNaN()) {
            return affixed(List.of(new Part("nan", nan_)), false, false, true);
        }

        if (value instanceof Double d && d.isInfinite()) {
            return affixed(List.of(new Part("infinity", infinity_)), negative, false, false);
        }

        BigDecimal abs = ((BigDecimal) value).abs();
        if ("percent".equals(options_.getStyle())) {
            abs = abs.movePointRight(2);
        }

        switch (options_.getNotation()) {
            case "scientific":
            case "engineering":
                final List<Part> scientific =
                        formatScientific(abs, "engineering".equals(options_.getNotation()), negative);
                return affixed(scientific, negative, abs.signum() == 0, false);

            case "compact":
                return formatCompact(abs, negative);

            default:
                final BigDecimal rounded = round(abs, negative);
                return affixed(formatDigits(rounded), negative, rounded.signum() == 0, false);
        }
    }

    /**
     * Formats the given range into parts.
     *
     * @param start the start of the range
     * @param end the end of the range
     * @return the parts; every part has a source (startRange, endRange or shared)
     * @see <a href="https://tc39.es/ecma402/#sec-partitionnumberrangepattern">spec</a>
     */
    List<Part> formatRangeToParts(final Value start, final Value end) {
        final List<Part> startParts = formatToParts(start.number(), start.negative());
        final List<Part> endParts = formatToParts(end.number(), end.negative());

        final List<Part> result = new ArrayList<>();
        if (join(startParts).equals(join(endParts))) {
            // FormatApproximately
            result.add(new Part("approximatelySign", "~", "shared"));
            for (final Part part : startParts) {
                result.add(part.withSource("shared"));
            }
            return result;
        }

        // a unit or currency after both numbers is shared (e.g. '3–5 km'); the form of the end is used
        final List<Part> sharedSuffix = new ArrayList<>();
        if ("unit".equals(options_.getStyle()) || "currency".equals(options_.getStyle())) {
            final int startSuffix = suffixStart(startParts);
            final int endSuffix = suffixStart(endParts);
            if (startSuffix < startParts.size() && endSuffix < endParts.size()
                    && sameSuffix(startParts.subList(startSuffix, startParts.size()),
                                    endParts.subList(endSuffix, endParts.size()))) {
                sharedSuffix.addAll(endParts.subList(endSuffix, endParts.size()));
                startParts.subList(startSuffix, startParts.size()).clear();
                endParts.subList(endSuffix, endParts.size()).clear();
            }
        }

        // the range separator is surrounded by spaces if the start has a sign or affixes
        boolean onlyNumbers = true;
        for (final Part part : startParts) {
            onlyNumbers &= isNumberPart(part);
        }

        for (final Part part : startParts) {
            result.add(part.withSource("startRange"));
        }
        result.add(new Part("literal", onlyNumbers ? "\u2013" : " \u2013 ", "shared"));
        for (final Part part : endParts) {
            result.add(part.withSource("endRange"));
        }
        for (final Part part : sharedSuffix) {
            result.add(part.withSource("shared"));
        }
        return result;
    }

    // the index of the unit or currency (with the literals) after the number; the size if there is none
    private static int suffixStart(final List<Part> parts) {
        int index = parts.size();
        while (index > 0) {
            final String type = parts.get(index - 1).type();
            if (!"unit".equals(type) && !"currency".equals(type) && !"literal".equals(type)) {
                break;
            }
            index--;
        }
        return index;
    }

    // units are shared even if the plural forms differ, currencies only if they are equal
    private static boolean sameSuffix(final List<Part> start, final List<Part> end) {
        if (start.size() != end.size()) {
            return false;
        }
        boolean hasUnitOrCurrency = false;
        for (int i = 0; i < start.size(); i++) {
            final Part startPart = start.get(i);
            final Part endPart = end.get(i);
            if (!startPart.type().equals(endPart.type())) {
                return false;
            }
            if ("unit".equals(startPart.type())) {
                hasUnitOrCurrency = true;
            }
            else {
                hasUnitOrCurrency |= "currency".equals(startPart.type());
                if (!startPart.value().equals(endPart.value())) {
                    return false;
                }
            }
        }
        return hasUnitOrCurrency;
    }

    private static boolean isNumberPart(final Part part) {
        switch (part.type()) {
            case "integer":
            case "group":
            case "decimal":
            case "fraction":
                return true;
            default:
                return false;
        }
    }

    // the mantissa with the exponent
    private List<Part> formatScientific(final BigDecimal abs, final boolean engineering, final boolean negative) {
        int exponent = 0;
        BigDecimal rounded = round(abs, negative);
        if (abs.signum() != 0) {
            exponent = abs.precision() - abs.scale() - 1;
            if (engineering) {
                exponent = Math.floorDiv(exponent, 3) * 3;
            }
            rounded = round(abs.movePointLeft(exponent), negative);
            // the rounding may result in a mantissa of 10 (1000)
            final BigDecimal limit = engineering ? BigDecimal.valueOf(1000) : BigDecimal.TEN;
            if (rounded.compareTo(limit) >= 0) {
                exponent += engineering ? 3 : 1;
                rounded = round(abs.movePointLeft(exponent), negative);
            }
        }

        final List<Part> parts = formatDigits(rounded);
        parts.add(new Part("exponentSeparator", exponentSeparator_));
        if (exponent < 0) {
            parts.add(new Part("exponentMinusSign", String.valueOf(minusSign_)));
        }
        parts.add(new Part("exponentInteger", localizeDigits(Integer.toString(Math.abs(exponent)))));
        return parts;
    }

    private List<Part> formatCompact(final BigDecimal abs, final boolean negative) {
        final java.text.NumberFormat compact = java.text.NumberFormat.getCompactNumberInstance(locale_,
                "long".equals(options_.getCompactDisplay())
                    ? java.text.NumberFormat.Style.LONG : java.text.NumberFormat.Style.SHORT);
        compact.setMaximumFractionDigits(0);
        compact.setRoundingMode(RoundingMode.DOWN);
        compact.setGroupingUsed(false);

        // the jdk tells the magnitude of the compact form
        int exponent = compactExponent(compact, abs);
        BigDecimal rounded = round(abs.movePointLeft(exponent), negative);
        final int nextExponent = compactExponent(compact, rounded.movePointRight(exponent));
        if (nextExponent != exponent) {
            exponent = nextExponent;
            rounded = round(abs.movePointLeft(exponent), negative);
        }

        // the affixes of the compact form (they depend on the plural form of the value)
        final String formatted = compact.format(rounded.movePointRight(exponent));
        int start = 0;
        while (start < formatted.length() && !Character.isDigit(formatted.charAt(start))) {
            start++;
        }
        int end = formatted.length();
        while (end > start && !Character.isDigit(formatted.charAt(end - 1))) {
            end--;
        }
        final String prefix = start < formatted.length() ? stripSign(formatted.substring(0, start)) : "";
        final String suffix = start < formatted.length() ? formatted.substring(end) : "";

        final List<Part> parts = new ArrayList<>(textParts(prefix, "compact"));
        parts.addAll(formatDigits(rounded));
        parts.addAll(textParts(suffix, "compact"));
        return affixed(parts, negative, rounded.signum() == 0, false);
    }

    // the exponent of the divisor used by the compact form of the given value
    private static int compactExponent(final java.text.NumberFormat compact, final BigDecimal abs) {
        final int integerDigits = abs.compareTo(BigDecimal.ONE) < 0 ? 1 : abs.precision() - abs.scale();
        final String formatted = compact.format(abs.setScale(0, RoundingMode.DOWN));
        int digits = 0;
        for (int i = 0; i < formatted.length(); i++) {
            if (Character.isDigit(formatted.charAt(i))) {
                digits++;
            }
        }
        if (digits == 0 || digits >= integerDigits) {
            return 0;
        }
        return integerDigits - digits;
    }

    private String stripSign(final String prefix) {
        return prefix.replace(String.valueOf(minusSign_), "");
    }

    /**
     * Splits the given text into the leading and trailing whitespace (literal parts)
     * and the text in between (a part of the given type).
     */
    private static List<Part> textParts(final String text, final String type) {
        final List<Part> parts = new ArrayList<>();
        int start = 0;
        while (start < text.length() && isSpace(text.charAt(start))) {
            start++;
        }
        int end = text.length();
        while (end > start && isSpace(text.charAt(end - 1))) {
            end--;
        }
        if (start > 0) {
            parts.add(new Part("literal", text.substring(0, start)));
        }
        if (end > start) {
            parts.add(new Part(type, text.substring(start, end)));
        }
        if (end < text.length() && end >= start) {
            parts.add(new Part("literal", text.substring(Math.max(start, end))));
        }
        return parts;
    }

    private static boolean isSpace(final char c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c);
    }

    // rounds the absolute value according to the digit options
    private BigDecimal round(final BigDecimal abs, final boolean negative) {
        final RoundingMode mode = roundingMode(negative);
        switch (options_.getRoundingType()) {
            case "significantDigits":
                return toRawPrecision(abs, mode);

            case "morePrecision":
            case "lessPrecision":
                final BigDecimal significant = toRawPrecision(abs, mode);
                final BigDecimal fixed = toRawFixed(abs, mode);
                final int significantMagnitude = magnitude(significant) - options_.getMaximumSignificantDigits() + 1;
                final int fixedMagnitude = -options_.getMaximumFractionDigits();
                final boolean fixedIsMorePrecise = significantMagnitude > fixedMagnitude;
                if ("morePrecision".equals(options_.getRoundingType())) {
                    return fixedIsMorePrecise ? fixed : significant;
                }
                return fixedIsMorePrecise ? significant : fixed;

            default:
                return toRawFixed(abs, mode);
        }
    }

    private static int magnitude(final BigDecimal value) {
        if (value.signum() == 0) {
            return 0;
        }
        return value.precision() - value.scale() - 1;
    }

    private BigDecimal toRawPrecision(final BigDecimal abs, final RoundingMode mode) {
        BigDecimal rounded = abs.round(new MathContext(options_.getMaximumSignificantDigits(), mode));
        if (rounded.scale() < 0) {
            rounded = rounded.setScale(0);
        }
        // remove the trailing zeros of the fraction, but keep the minimum significant digits
        while (rounded.scale() > 0 && rounded.unscaledValue().mod(java.math.BigInteger.TEN).signum() == 0
                && significantDigits(rounded) > options_.getMinimumSignificantDigits()) {
            rounded = rounded.setScale(rounded.scale() - 1);
        }
        // add zeros up to the minimum significant digits
        while (significantDigits(rounded) < options_.getMinimumSignificantDigits()) {
            rounded = rounded.setScale(rounded.scale() + 1);
        }
        return rounded;
    }

    private static int significantDigits(final BigDecimal value) {
        if (value.signum() == 0) {
            // all zeros of the fraction and the leading zero count
            return value.scale() + 1;
        }
        return value.precision();
    }

    private BigDecimal toRawFixed(final BigDecimal abs, final RoundingMode mode) {
        BigDecimal rounded;
        if (options_.getRoundingIncrement() == 1) {
            rounded = abs.setScale(options_.getMaximumFractionDigits(), mode);
        }
        else {
            final BigDecimal increment = BigDecimal.valueOf(options_.getRoundingIncrement())
                    .movePointLeft(options_.getMaximumFractionDigits());
            rounded = abs.divide(increment, 0, mode).multiply(increment)
                        .setScale(options_.getMaximumFractionDigits(), RoundingMode.UNNECESSARY);
        }
        // remove the trailing zeros, but keep the minimum fraction digits
        while (rounded.scale() > options_.getMinimumFractionDigits()
                && rounded.unscaledValue().mod(java.math.BigInteger.TEN).signum() == 0) {
            rounded = rounded.setScale(rounded.scale() - 1);
        }
        return rounded;
    }

    // the rounding mode for the absolute value
    private RoundingMode roundingMode(final boolean negative) {
        switch (options_.getRoundingMode()) {
            case "ceil":
                return negative ? RoundingMode.DOWN : RoundingMode.UP;
            case "floor":
                return negative ? RoundingMode.UP : RoundingMode.DOWN;
            case "expand":
                return RoundingMode.UP;
            case "trunc":
                return RoundingMode.DOWN;
            case "halfCeil":
                return negative ? RoundingMode.HALF_DOWN : RoundingMode.HALF_UP;
            case "halfFloor":
                return negative ? RoundingMode.HALF_UP : RoundingMode.HALF_DOWN;
            case "halfTrunc":
                return RoundingMode.HALF_DOWN;
            case "halfEven":
                return RoundingMode.HALF_EVEN;
            default:
                return RoundingMode.HALF_UP;
        }
    }

    // formats the (rounded, non negative) value: integer digits, grouping and fraction
    private List<Part> formatDigits(final BigDecimal rounded) {
        final String plain = rounded.toPlainString();
        final int dot = plain.indexOf('.');
        String integer = dot == -1 ? plain : plain.substring(0, dot);
        String fraction = dot == -1 ? "" : plain.substring(dot + 1);

        if ("stripIfInteger".equals(options_.getTrailingZeroDisplay()) && fraction.matches("0*")) {
            fraction = "";
        }

        final StringBuilder integerPadded = new StringBuilder(integer);
        while (integerPadded.length() < options_.getMinimumIntegerDigits()) {
            integerPadded.insert(0, '0');
        }
        integer = integerPadded.toString();

        final List<Part> parts = new ArrayList<>();
        final boolean minimumGroupingDigits2 = "min2".equals(options_.getUseGrouping())
                || "auto".equals(options_.getUseGrouping()) && hasMinimumGroupingDigits2();
        final boolean grouping = options_.getUseGrouping() != null
                && !(minimumGroupingDigits2 && integer.length() < 5);
        if (grouping) {
            int groupStart = 0;
            int groupEnd = integer.length() % 3 == 0 ? 3 : integer.length() % 3;
            while (groupStart < integer.length()) {
                if (groupStart > 0) {
                    parts.add(new Part("group", String.valueOf(groupingSeparator_)));
                }
                parts.add(new Part("integer", localizeDigits(integer.substring(groupStart, groupEnd))));
                groupStart = groupEnd;
                groupEnd += 3;
            }
        }
        else {
            parts.add(new Part("integer", localizeDigits(integer)));
        }
        if (!fraction.isEmpty()) {
            parts.add(new Part("decimal", String.valueOf(decimalSeparator_)));
            parts.add(new Part("fraction", localizeDigits(fraction)));
        }
        return parts;
    }

    // some locales group only numbers with at least 5 integer digits (CLDR minimumGroupingDigits)
    private boolean hasMinimumGroupingDigits2() {
        final String language = locale_.getLanguage();
        final String country = locale_.getCountry();
        return "es".equals(language) && (country.isEmpty() || "ES".equals(country))
                || "pl".equals(language)
                || "pt".equals(language) && "PT".equals(country);
    }

    // replaces the ascii digits by the digits of the numbering system
    private String localizeDigits(final String digits) {
        final StringBuilder result = new StringBuilder(digits.length());
        for (int i = 0; i < digits.length(); i++) {
            result.append(localizeDigit(digits.charAt(i)));
        }
        return result.toString();
    }

    private char localizeDigit(final char digit) {
        return (char) (zeroDigit_ + (digit - '0'));
    }

    // adds the sign and the affixes of the style to the formatted number
    private List<Part> affixed(final List<Part> numberParts, final boolean negative, final boolean zero,
            final boolean nan) {
        final int sign;
        switch (options_.getSignDisplay()) {
            case "never":
                sign = 0;
                break;
            case "always":
                sign = negative ? -1 : 1;
                break;
            case "exceptZero":
                sign = zero || nan ? 0 : negative ? -1 : 1;
                break;
            case "negative":
                sign = negative && !zero ? -1 : 0;
                break;
            default:
                sign = negative ? -1 : 0;
        }

        final String minus = String.valueOf(minusSign_);
        final String prefix;
        final String suffix;
        if (sign < 0) {
            prefix = negativePrefix_;
            suffix = negativeSuffix_;
        }
        else if (sign > 0) {
            if (negativePrefix_.contains(minus) || negativeSuffix_.contains(minus)) {
                prefix = negativePrefix_.replace(minus, "+");
                suffix = negativeSuffix_.replace(minus, "+");
            }
            else {
                // e.g. the accounting format uses parentheses for negative values
                prefix = "+" + positivePrefix_;
                suffix = positiveSuffix_;
            }
        }
        else {
            prefix = positivePrefix_;
            suffix = positiveSuffix_;
        }

        final List<Part> parts = new ArrayList<>();
        final String number = join(numberParts);
        if ("currency".equals(options_.getStyle()) && "name".equals(options_.getCurrencyDisplay())
                || "unit".equals(options_.getStyle())) {
            if (sign < 0) {
                parts.add(new Part("minusSign", minus));
            }
            else if (sign > 0) {
                parts.add(new Part("plusSign", "+"));
            }
            parts.addAll(numberParts);
            if ("unit".equals(options_.getStyle())) {
                final String pattern = unitPattern(number);
                final int pos = pattern.indexOf("{0}");
                parts.addAll(0, textParts(pattern.substring(0, pos), "unit"));
                parts.addAll(textParts(pattern.substring(pos + 3), "unit"));
            }
            else {
                parts.add(new Part("literal", " "));
                parts.add(new Part("currency", currencyName(number)));
            }
            return parts;
        }

        parts.addAll(affixParts(prefix));
        parts.addAll(numberParts);
        parts.addAll(affixParts(suffix));
        return parts;
    }

    // splits an affix of the style into the signs, the currency, the percent sign and literals
    private List<Part> affixParts(final String affix) {
        final List<Part> parts = new ArrayList<>();
        final StringBuilder literal = new StringBuilder();
        int i = 0;
        while (i < affix.length()) {
            final String type;
            String value = null;
            final char c = affix.charAt(i);
            if (currencyDisplayText_ != null && !currencyDisplayText_.isEmpty()
                    && affix.startsWith(currencyDisplayText_, i)) {
                type = "currency";
                value = currencyDisplayText_;
            }
            else if (c == minusSign_ || c == '-' || c == '\u2212') {
                type = "minusSign";
            }
            else if (c == '+') {
                type = "plusSign";
            }
            else if (c == '%' || c == '\u066a' || c == '\u2030') {
                type = "percentSign";
            }
            else {
                type = null;
            }

            if (type == null) {
                literal.append(c);
                i++;
                continue;
            }
            if (literal.length() > 0) {
                parts.add(new Part("literal", literal.toString()));
                literal.setLength(0);
            }
            if (value == null) {
                value = String.valueOf(c);
            }
            parts.add(new Part(type, value));
            i += value.length();
        }
        if (literal.length() > 0) {
            parts.add(new Part("literal", literal.toString()));
        }
        return parts;
    }

    private String unitPattern(final String number) {
        final String[] parts = options_.getUnit().split("-per-");
        if (parts.length == 2 && !UNITS.containsKey(options_.getUnit())) {
            final String numerator = unitPattern(parts[0], number);
            final String denominator = unitPattern(parts[1], "1");
            if ("long".equals(options_.getUnitDisplay())) {
                return numerator + " per " + denominator.replace("{0} ", "");
            }
            final String perUnit = PER_UNITS.get(parts[1]);
            return numerator + "/" + (perUnit == null ? denominator.replace("{0}", "").trim() : perUnit);
        }
        return unitPattern(options_.getUnit(), number);
    }

    private String unitPattern(final String unit, final String number) {
        final String[] patterns = UNITS.get(unit);
        if (patterns == null) {
            return "{0} " + unit;
        }
        // English plural rules: 'one' for the integer 1
        final boolean one = "1".equals(number);
        switch (options_.getUnitDisplay()) {
            case "long":
                return one ? patterns[2] : patterns[3];
            case "narrow":
                return pluralPattern(patterns[1], one);
            default:
                return pluralPattern(patterns[0], one);
        }
    }

    private static String pluralPattern(final String pattern, final boolean one) {
        final int pos = pattern.indexOf('|');
        if (pos == -1) {
            return pattern;
        }
        return one ? pattern.substring(0, pos) : pattern.substring(pos + 1);
    }

    /**
     * Returns whether the given unit is a sanctioned unit (or a combination of two
     * sanctioned units with '-per-').
     *
     * @param unit the unit
     * @return true if the unit is well-formed
     * @see <a href="https://tc39.es/ecma402/#sec-iswellformedunitidentifier">spec</a>
     */
    static boolean isWellFormedUnitIdentifier(final String unit) {
        if (UNITS.containsKey(unit) && !unit.contains("-per-")) {
            return true;
        }
        final int pos = unit.indexOf("-per-");
        if (pos == -1 || unit.indexOf("-per-", pos + 1) != -1) {
            return false;
        }
        return UNITS.containsKey(unit.substring(0, pos)) && UNITS.containsKey(unit.substring(pos + 5));
    }
}
