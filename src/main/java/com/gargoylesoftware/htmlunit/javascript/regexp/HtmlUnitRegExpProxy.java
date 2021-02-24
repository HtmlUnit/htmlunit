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
package com.gargoylesoftware.htmlunit.javascript.regexp;

import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_REGEXP_EMPTY_LASTPAREN_IF_TOO_MANY_GROUPS;
import static com.gargoylesoftware.htmlunit.BrowserVersionFeatures.JS_REGEXP_GROUP0_RETURNS_WHOLE_MATCH;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gargoylesoftware.htmlunit.BrowserVersion;

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.RegExpProxy;
import net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;
import net.sourceforge.htmlunit.corejs.javascript.Undefined;
import net.sourceforge.htmlunit.corejs.javascript.regexp.NativeRegExp;
import net.sourceforge.htmlunit.corejs.javascript.regexp.RegExpImpl;
import net.sourceforge.htmlunit.corejs.javascript.regexp.SubString;

/**
 * Begins customization of JavaScript RegExp base on JDK regular expression support.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Carsten Steul
 */
public class HtmlUnitRegExpProxy extends RegExpImpl {

    private static final Log LOG = LogFactory.getLog(HtmlUnitRegExpProxy.class);
    /** Pattern cache */
    private static final Map<String, Pattern> PATTENS = new HashMap<>();

    private final RegExpProxy wrapped_;
    private final BrowserVersion browserVersion_;

    /**
     * Wraps a proxy to enhance it.
     * @param wrapped the original proxy
     * @param browserVersion the current browser version
     */
    public HtmlUnitRegExpProxy(final RegExpProxy wrapped, final BrowserVersion browserVersion) {
        wrapped_ = wrapped;
        browserVersion_ = browserVersion;
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
        if (RA_REPLACE == actionType && args.length == 2 && args[1] instanceof String) {
            final String thisString = Context.toString(thisObj);
            final String replacement = (String) args[1];
            final Object arg0 = args[0];
            if (arg0 instanceof String) {
                // arg0 should *not* be interpreted as a RegExp
                return doStringReplacement(thisString, (String) arg0, replacement);
            }

            if (arg0 instanceof NativeRegExp) {
                try {
                    final NativeRegExp regexp = (NativeRegExp) arg0;
                    final RegExpData reData = new RegExpData(regexp);
                    final Matcher matcher = reData.getPattern().matcher(thisString);
                    return doReplacement(thisString, replacement, matcher, reData.isGlobal());
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

            final Matcher matcher = reData.getPattern().matcher(thisString);

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
            final List<Object> groups = new ArrayList<>();
            if (reData.isGlobal()) { // has flag g
                groups.add(matcher.group(0));
                setProperties(matcher, thisString, matcher.start(0), matcher.end(0));

                while (matcher.find()) {
                    groups.add(matcher.group(0));
                    setProperties(matcher, thisString, matcher.start(0), matcher.end(0));
                }
            }
            else {
                for (int i = 0; i <= matcher.groupCount(); i++) {
                    Object group = matcher.group(i);
                    if (group == null) {
                        group = Undefined.instance;
                    }
                    groups.add(group);
                }

                setProperties(matcher, thisString, matcher.start(), matcher.end());
            }
            final Scriptable response = cx.newArray(scope, groups.toArray());
            // the additional properties (cf ECMA script reference 15.10.6.2 13)
            response.put("index", response, Integer.valueOf(index));
            response.put("input", response, thisString);
            return response;
        }

        return wrappedAction(cx, scope, thisObj, args, actionType);
    }

    private String doStringReplacement(final String originalString,
                        final String searchString, final String replacement) {
        if (originalString == null) {
            return "";
        }

        final StaticStringMatcher matcher = new StaticStringMatcher(originalString, searchString);
        if (matcher.start() > -1) {
            final StringBuilder sb = new StringBuilder()
                .append(originalString, 0, matcher.start_);

            String localReplacement = replacement;
            if (replacement.contains("$")) {
                localReplacement = computeReplacementValue(localReplacement, originalString, matcher, false);
            }
            sb.append(localReplacement)
                .append(originalString, matcher.end_, originalString.length());
            return sb.toString();
        }
        return originalString;
    }

    private String doReplacement(final String originalString, final String replacement, final Matcher matcher,
        final boolean replaceAll) {

        final StringBuilder sb = new StringBuilder();
        int previousIndex = 0;
        while (matcher.find()) {
            sb.append(originalString, previousIndex, matcher.start());
            String localReplacement = replacement;
            if (replacement.contains("$")) {
                localReplacement = computeReplacementValue(replacement, originalString, matcher,
                        browserVersion_.hasFeature(JS_REGEXP_GROUP0_RETURNS_WHOLE_MATCH));
            }
            sb.append(localReplacement);
            previousIndex = matcher.end();

            setProperties(matcher, originalString, matcher.start(), previousIndex);
            if (!replaceAll) {
                break;
            }
        }
        sb.append(originalString, previousIndex, originalString.length());
        return sb.toString();
    }

    String computeReplacementValue(final String replacement, final String originalString,
            final MatchResult matcher, final boolean group0ReturnsWholeMatch) {

        int lastIndex = 0;
        final StringBuilder result = new StringBuilder();
        int i;
        while ((i = replacement.indexOf('$', lastIndex)) > -1) {
            if (i > 0) {
                result.append(replacement, lastIndex, i);
            }
            String ss = null;
            if (i < replacement.length() - 1 && (i == lastIndex || replacement.charAt(i - 1) != '$')) {
                final char next = replacement.charAt(i + 1);
                // only valid back reference are "evaluated"
                if (next >= '1' && next <= '9') {
                    final int num1digit = next - '0';
                    final char next2 = i + 2 < replacement.length() ? replacement.charAt(i + 2) : 'x';
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
                        case '0':
                            if (group0ReturnsWholeMatch) {
                                ss = matcher.group();
                            }
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
            if (ss == null) {
                result.append('$');
                lastIndex = i + 1;
            }
            else {
                result.append(ss);
                lastIndex = i + 2;
            }
        }

        result.append(replacement, lastIndex, replacement.length());

        return result.toString();
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
            lastMatch = new SubString(match, 0, match.length());
        }

        // parens
        final int groupCount = matcher.groupCount();
        if (groupCount == 0) {
            parens = null;
        }
        else {
            final int count = Math.min(9, groupCount);
            parens = new SubString[count];
            for (int i = 0; i < count; i++) {
                final String group = matcher.group(i + 1);
                if (group == null) {
                    parens[i] = new SubString();
                }
                else {
                    parens[i] = new SubString(group, 0, group.length());
                }
            }
        }

        // lastParen
        if (groupCount > 0) {
            if (groupCount > 9 && browserVersion_.hasFeature(JS_REGEXP_EMPTY_LASTPAREN_IF_TOO_MANY_GROUPS)) {
                lastParen = new SubString();
            }
            else {
                final String last = matcher.group(groupCount);
                if (last == null) {
                    lastParen = new SubString();
                }
                else {
                    lastParen = new SubString(last, 0, last.length());
                }
            }
        }

        // leftContext
        if (startPos > 0) {
            leftContext = new SubString(thisString, 0, startPos);
        }
        else {
            leftContext = new SubString();
        }

        // rightContext
        final int length = thisString.length();
        if (endPos < length) {
            rightContext = new SubString(thisString, endPos, length - endPos);
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
            if (LOG.isWarnEnabled()) {
                LOG.warn("compileRegExp() threw for >" + source + "<, flags: >" + flags + "<. "
                    + "Replacing with a '####shouldNotFindAnything###'");
            }
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
        private final boolean global_;
        private Pattern pattern_;

        RegExpData(final NativeRegExp re) {
            final String str = re.toString(); // the form is /regex/flags
            final String jsSource = StringUtils.substringBeforeLast(str.substring(1), "/");
            final String jsFlags = StringUtils.substringAfterLast(str, "/");

            global_ = jsFlags.indexOf('g') != -1;

            pattern_ = PATTENS.get(str);
            if (pattern_ == null) {
                pattern_ = Pattern.compile(jsRegExpToJavaRegExp(jsSource), getJavaFlags(jsFlags));
                PATTENS.put(str, pattern_);
            }
        }

        RegExpData(final String string) {
            global_ = false;

            pattern_ = PATTENS.get(string);
            if (pattern_ == null) {
                pattern_ = Pattern.compile(jsRegExpToJavaRegExp(string), 0);
                PATTENS.put(string, pattern_);
            }
        }

        /**
         * Converts the current JavaScript RegExp flags to Java Pattern flags.
         * @return the Java Pattern flags
         */
        private static int getJavaFlags(final String jsFlags) {
            int flags = 0;
            if (jsFlags.contains("i")) {
                flags |= Pattern.CASE_INSENSITIVE;
            }
            if (jsFlags.contains("m")) {
                flags |= Pattern.MULTILINE;
            }
            return flags;
        }

        boolean isGlobal() {
            return global_;
        }

        Pattern getPattern() {
            return pattern_;
        }
    }

    /**
     * Transform a JavaScript regular expression to a Java regular expression
     * @param re the JavaScript regular expression to transform
     * @return the transformed expression
     */
    static String jsRegExpToJavaRegExp(final String re) {
        final RegExpJsToJavaConverter regExpJsToJavaFSM = new RegExpJsToJavaConverter();
        return regExpJsToJavaFSM.convert(re);
    }

    /**
     * Simple helper.
     */
    private static final class StaticStringMatcher implements MatchResult {
        private final String group_;
        private final int start_;
        private final int end_;

        StaticStringMatcher(final String originalString, final String searchString) {
            final int pos = originalString.indexOf(searchString);
            group_ = searchString;
            start_ = pos;
            end_ = pos + searchString.length();
        }

        @Override
        public String group() {
            return group_;
        }

        @Override
        public int start() {
            return start_;
        }

        @Override
        public int end() {
            return end_;
        }

        @Override
        public int start(final int group) {
            // not used so far
            return 0;
        }

        @Override
        public int end(final int group) {
            // not used so far
            return 0;
        }

        @Override
        public String group(final int group) {
            // not used so far
            return null;
        }

        @Override
        public int groupCount() {
            // not used so far
            return 0;
        }
    }
}
