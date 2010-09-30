/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.regexp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.RegExpProxy;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.regexp.NativeRegExp;
import net.sourceforge.htmlunit.corejs.javascript.regexp.RegExpImpl;
import net.sourceforge.htmlunit.corejs.javascript.regexp.SubString;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Begins customization of JavaScript RegExp base on JDK regular expression support.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class HtmlUnitRegExpProxy extends RegExpImpl {

    private static final Log LOG = LogFactory.getLog(HtmlUnitRegExpProxy.class);

    private final RegExpProxy wrapped_;

    /**
     * Wraps a proxy to enhance it.
     * @param wrapped the original proxy
     */
    public HtmlUnitRegExpProxy(final RegExpProxy wrapped) {
        wrapped_ = wrapped;
    }

    /**
     * Use the wrapped proxy except for replacement with string arg where it uses Java regular expression.
     * {@inheritDoc}
     */
    @Override
    public Object action(final Context cx, final Scriptable scope, final Scriptable thisObj,
        final Object[] args, final int actionType) {
        try {
            return doAction(cx, scope, thisObj, args, actionType);
        }
        catch (final StackOverflowError e) {
            // TODO: We shouldn't have to catch this exception and fall back to Rhino's regex support!
            // See HtmlUnitRegExpProxyTest.stackOverflow()
            LOG.warn(e.getMessage(), e);
            return wrapped_.action(cx, scope, thisObj, args, actionType);
        }
    }

    private Object doAction(final Context cx, final Scriptable scope, final Scriptable thisObj,
        final Object[] args, final int actionType) {
        // in a first time just improve replacement with a String (not a function)
        if (RA_REPLACE == actionType && args.length == 2 && (args[1] instanceof String)) {
            final String thisString = Context.toString(thisObj);
            String replacement = (String) args[1];
            final Object arg0 = args[0];
            if (arg0 instanceof String) {
                replacement = replacement.replaceAll("\\$\\$", "\\$");
                // arg0 should *not* be interpreted as a RegExp
                return StringUtils.replaceOnce(thisString, (String) arg0, replacement);
            }
            else if (arg0 instanceof NativeRegExp) {
                try {
                    final NativeRegExp regexp = (NativeRegExp) arg0;
                    final RegExpData reData = new RegExpData(regexp);
                    final String regex = reData.getJavaPattern();
                    final int flags = reData.getJavaFlags();
                    final Pattern pattern = Pattern.compile(regex, flags);
                    final Matcher matcher = pattern.matcher(thisString);
                    return doReplacement(thisString, replacement, matcher, reData.hasFlag('g'));
                }
                catch (final PatternSyntaxException e) {
                    LOG.warn(e.getMessage(), e);
                }
            }
        }
        else if (RA_MATCH == actionType || RA_SEARCH == actionType) {
            if (args.length == 0) {
                return null;
            }
            final Object arg0 = args[0];
            final String thisString = Context.toString(thisObj);
            final RegExpData reData;
            if (arg0 instanceof NativeRegExp) {
                reData = new RegExpData((NativeRegExp) arg0);
            }
            else {
                reData = new RegExpData(Context.toString(arg0));
            }

            final Pattern pattern = Pattern.compile(reData.getJavaPattern(), reData.getJavaFlags());
            final Matcher matcher = pattern.matcher(thisString);

            final boolean found = matcher.find();
            if (RA_SEARCH == actionType) {
                if (found) {
                    setProperties(matcher, thisString, matcher.start(), matcher.end());
                    return matcher.start();
                }
                return -1;
            }

            if (!found) {
                return null;
            }
            final int index = matcher.start(0);
            final List<Object> groups = new ArrayList<Object>();
            if (reData.hasFlag('g')) { // has flag g
                groups.add(matcher.group(0));
                setProperties(matcher, thisString, matcher.start(0), matcher.end(0));

                while (matcher.find()) {
                    groups.add(matcher.group(0));
                    setProperties(matcher, thisString, matcher.start(0), matcher.end(0));
                }
            }
            else {
                for (int i = 0; i <= matcher.groupCount(); ++i) {
                    Object group = matcher.group(i);
                    if (group == null) {
                        group = Context.getUndefinedValue();
                    }
                    groups.add(group);
                }

                setProperties(matcher, thisString, matcher.start(), matcher.end());
            }
            final Scriptable response = cx.newArray(scope, groups.toArray());
            // the additional properties (cf ECMA script reference 15.10.6.2 13)
            response.put("index", response, new Integer(index));
            response.put("input", response, thisString);
            return response;
        }

        return wrappedAction(cx, scope, thisObj, args, actionType);
    }

    private String doReplacement(final String originalString, final String replacement, final Matcher matcher,
        final boolean replaceAll) {
//        replacement = replacement.replaceAll("\\\\", "\\\\\\\\"); // \\ -> \\\\
//        replacement = replacement.replaceAll("(?<!\\$)\\$(?!\\d)", "\\\\\\$"); // \$ -> \\\$

        final StringBuffer sb = new StringBuffer();
        int previousIndex = 0;
        while (matcher.find()) {
            sb.append(originalString.substring(previousIndex, matcher.start()));
            String localReplacement = replacement;
            if (replacement.contains("$")) {
                localReplacement = computeReplacementValue(replacement, originalString, matcher);
            }
            sb.append(localReplacement);
            previousIndex = matcher.end();

            setProperties(matcher, originalString, matcher.start(), previousIndex);
            if (!replaceAll) {
                break;
            }
        }
        sb.append(originalString.substring(previousIndex));
        return sb.toString();
    }

    static String computeReplacementValue(final String replacement,
            final String originalString, final Matcher matcher) {

        int lastIndex = 0;
        final StringBuilder result = new StringBuilder();
        int i;
        while ((i = replacement.indexOf('$', lastIndex)) > -1) {
            if (i > 0) {
                result.append(replacement.substring(lastIndex, i));
            }
            String ss = null;
            if (i < replacement.length() - 1 && (i == lastIndex || replacement.charAt(i - 1) != '$')) {
                final char next = replacement.charAt(i + 1);
                // only valid back reference are "evaluated"
                if (next >= '1' && next <= '9') {
                    final int num1digit = next - '0';
                    final char next2 = (i + 2 < replacement.length()) ? replacement.charAt(i + 2) : 'x';
                    final int num2digits;
                    // if there are 2 digits, the second one is considered as part of the group number
                    // only if there is such a group
                    if (next2 >= '1' && next2 <= '9') {
                        num2digits = num1digit * 10 + (next2 - '0');
                    }
                    else {
                        num2digits = Integer.MAX_VALUE;
                    }
                    if (num2digits <= matcher.groupCount()) {
                        ss = matcher.group(num2digits);
                        i++;
                    }
                    else if (num1digit <= matcher.groupCount()) {
                        ss = StringUtils.defaultString(matcher.group(num1digit));
                    }
                }
                else {
                    switch (next) {
                        case '&':
                            ss = matcher.group();
                            break;
                        case '`':
                            ss = originalString.substring(0, matcher.start());
                            break;
                        case '\'':
                            ss = originalString.substring(matcher.end());
                            break;
                        case '$':
                            ss = "$";
                            break;
                        default:
                    }
                }
            }
            if (ss != null) {
                result.append(ss);
                lastIndex = i + 2;
            }
            else {
                result.append('$');
                lastIndex = i + 1;
            }
        }

        result.append(replacement.substring(lastIndex));

        return result.toString();
    }

    /**
     * Indicates if the character at the given position is escaped or not.
     * @param characters the characters to consider
     * @param position the position
     * @return <code>true</code> if escaped
     */
    static boolean isEscaped(final String characters, final int position) {
        int p = position;
        int nbBackslash = 0;
        while (p > 0 && characters.charAt(--p) == '\\') {
            nbBackslash++;
        }
        return (nbBackslash % 2 == 1);
    }

    /**
     * Calls action on the wrapped RegExp proxy.
     */
    private Object wrappedAction(final Context cx, final Scriptable scope, final Scriptable thisObj,
            final Object[] args, final int actionType) {

        // take care to set the context's RegExp proxy to the original one as this is checked
        // (cf net.sourceforge.htmlunit.corejs.javascript.regexp.RegExpImp:334)
        try {
            ScriptRuntime.setRegExpProxy(cx, wrapped_);
            return wrapped_.action(cx, scope, thisObj, args, actionType);
        }
        finally {
            ScriptRuntime.setRegExpProxy(cx, this);
        }
    }

    private void setProperties(final Matcher matcher, final String thisString, final int startPos, final int endPos) {
        // lastMatch
        final String match = matcher.group();
        if (match == null) {
            lastMatch = new SubString();
        }
        else {
            lastMatch = new FixedSubString(match);
        }

        // parens
        final int count = Math.min(9, matcher.groupCount());
        if (count == 0) {
            parens = null;
        }
        else {
            parens = new SubString[count];
            for (int i = 0; i < count; i++) {
                final String group = matcher.group(i + 1);
                if (group == null) {
                    parens[i] = SubString.emptySubString;
                }
                else {
                    parens[i] = new FixedSubString(group);
                }
            }
        }

        // lastParen
        if (matcher.groupCount() > 0) {
            final String last = matcher.group(matcher.groupCount());
            if (last == null) {
                lastParen = new SubString();
            }
            else {
                lastParen = new FixedSubString(last);
            }
        }

        // leftContext
        if (startPos > 0) {
            leftContext = new FixedSubString(thisString.substring(0, startPos));
        }
        else {
            leftContext = new SubString();
        }

        // rightContext
        if (endPos < thisString.length()) {
            rightContext = new FixedSubString(thisString.substring(endPos));
        }
        else {
            rightContext = new SubString();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object compileRegExp(final Context cx, final String source, final String flags) {
        try {
            return wrapped_.compileRegExp(cx, source, flags);
        }
        catch (final Exception e) {
            LOG.warn("compileRegExp() threw for >" + source + "<, flags: >" + flags + "<. "
                + "Replacing with a '####shouldNotFindAnything###'");
            return wrapped_.compileRegExp(cx, "####shouldNotFindAnything###", "");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int find_split(final Context cx, final Scriptable scope, final String target,
            final String separator, final Scriptable re, final int[] ip, final int[] matchlen,
            final boolean[] matched, final String[][] parensp) {
        return wrapped_.find_split(cx, scope, target, separator, re, ip, matchlen, matched, parensp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRegExp(final Scriptable obj) {
        return wrapped_.isRegExp(obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Scriptable wrapRegExp(final Context cx, final Scriptable scope, final Object compiled) {
        return wrapped_.wrapRegExp(cx, scope, compiled);
    }

    private static class RegExpData {
        private final String jsSource_;
        private final String jsFlags_;

        RegExpData(final NativeRegExp re) {
            final String str = re.toString(); // the form is /regex/flags
            jsSource_ = StringUtils.substringBeforeLast(str.substring(1), "/");
            jsFlags_ = StringUtils.substringAfterLast(str, "/");
        }
        public RegExpData(final String string) {
            jsSource_ = string;
            jsFlags_ = "";
        }
        /**
         * Converts the current JavaScript RegExp flags to Java Pattern flags.
         * @return the Java Pattern flags
         */
        public int getJavaFlags() {
            int flags = 0;
            if (jsFlags_.contains("i")) {
                flags |= Pattern.CASE_INSENSITIVE;
            }
            if (jsFlags_.contains("m")) {
                flags |= Pattern.MULTILINE;
            }
            return flags;
        }
        public String getJavaPattern() {
            return jsRegExpToJavaRegExp(jsSource_);
        }

        boolean hasFlag(final char c) {
            return jsFlags_.indexOf(c) != -1;
        }
    }

    /**
     * Transform a JavaScript regular expression to a Java regular expression
     * @param re the JavaScript regular expression to transform
     * @return the transformed expression
     */
    static String jsRegExpToJavaRegExp(String re) {
        re = re.replaceAll("\\[\\^\\\\\\d\\]", ".");
        re = re.replaceAll("\\[([^\\]]*)\\\\b([^\\]]*)\\]", "[$1\\\\cH$2]"); // [...\b...] -> [...\cH...]
        re = re.replaceAll("(?<!\\\\)\\[([^((?<!\\\\)\\[)\\]]*)\\[", "[$1\\\\["); // [...[...] -> [...\[...]

        // back reference in character classes are simply ignored by browsers
        re = re.replaceAll("(?<!\\\\)\\[([^\\]]*)(?<!\\\\)\\\\\\d", "[$1"); // [...ab\5cd...] -> [...abcd...]

        // characters escaped without need should be "un-escaped"
        re = re.replaceAll("(?<!\\\\)\\\\([ACE-RT-VX-Zaeg-mpqyz])", "$1");

        re = escapeJSCurly(re);
        return re;
    }

    /**
     * Escape curly braces that are not used in an expression like "{n}", "{n,}" or "{n,m}"
     * (where n and m are positive integers).
     * @param re the regular expression to escape
     * @return the escaped expression
     */
    static String escapeJSCurly(String re) {
        re = re.replaceAll("(?<!\\\\)\\{(?!\\d)", "\\\\{");
        re = re.replaceAll("(?<!(\\d,?|\\\\))\\}", "\\\\}");
        return re;
    }

    /**
     *  Static version of a SubString that does not fill the
     *  chars array. This helps in some situations to solve
     *  performance issues.
     *  Use this only if you sure, that the chars are no longer
     *  needed.
     */
    private static class FixedSubString extends SubString {

        private String value_;

        /**
         * Constructor.
         *
         * @param str the value
         */
        public FixedSubString(final String str) {
            value_ = str;
        }

        @Override
        public String toString() {
            return value_;
        }
    }
}
