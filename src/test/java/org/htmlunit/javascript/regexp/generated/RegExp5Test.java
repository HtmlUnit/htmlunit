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
package org.htmlunit.javascript.regexp.generated;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Additional tests for RegExp groups, backreferences,
 * alternation, escape sequences, and flag interactions.
 *
 * @author Ronald Brill
 */
public class RegExp5Test extends AbstractRegExpTest {

    // =================================================================
    // Group edge cases
    // =================================================================

    @Test
    @Alerts({"abcdef", "ab", "cd", "ef"})
    public void multipleCaptures() throws Exception {
        final String script =
              "var m = /(ab)(cd)(ef)/.exec('abcdef');\n"
            + "log(m[0]);\n"
            + "log(m[1]);\n"
            + "log(m[2]);\n"
            + "log(m[3]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"abc", "bc", "c"})
    public void nestedCaptures() throws Exception {
        final String script =
              "var m = /(a(b(c)))/.exec('abc');\n"
            + "log(m[1]);\n"
            + "log(m[2]);\n"
            + "log(m[3]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"ac", "undefined"})
    public void optionalNonParticipating() throws Exception {
        final String script =
              "var m = /a(b)?c/.exec('ac');\n"
            + "log(m[0]);\n"
            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"foobar", "foo", "bar"})
    public void nonCaptureDoesNotCount() throws Exception {
        final String script =
              "var m = /(foo)(?:xxx)?(bar)/.exec('foobar');\n"
            + "log(m[0]);\n"
            + "log(m[1]);\n"
            + "log(m[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("4")
    public void captureCountInLength() throws Exception {
        final String script =
              "var m = /(a)(b)(c)/.exec('abc');\n"
            + "log(m.length);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"c", "c"})
    public void repeatedCaptureLastValue() throws Exception {
        final String script =
              "var m = /(\\w)+/.exec('abc');\n"
            + "log(m[1]);\n"
            + "var m2 = /([abc])+/.exec('abc');\n"
            + "log(m2[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"a", "undefined"})
    public void namedGroupNonParticipating() throws Exception {
        final String script =
              "var m = /(?<x>a)(?<y>b)?/.exec('a');\n"
            + "log(m.groups.x);\n"
            + "log(m.groups.y);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"ab", "a", "b"})
    public void namedGroupAccessByName() throws Exception {
        final String script =
              "var m = /(?<first>a)(?<second>b)/.exec('ab');\n"
            + "log(m[0]);\n"
            + "log(m.groups.first);\n"
            + "log(m.groups.second);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"a", "a"})
    public void namedGroupIndexAndNameMatch() throws Exception {
        final String script =
              "var m = /(?<x>a)/.exec('a');\n"
            + "log(m[1]);\n"
            + "log(m.groups.x);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"abbc", "bb"})
    public void captureGroupInMiddle() throws Exception {
        // /a(b+)c/ in 'aabbc': first 'a' that allows a full match is at index 1
        // match is "abbc", capture is "bb"
        final String script =
              "var m = /a(b+)c/.exec('aabbc');\n"
            + "log(m[0]);\n"
            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"ab", "a", "b", "undefined"})
    public void alternationInsideGroup() throws Exception {
        final String script =
              "var m = /(a)(b|c)(d)?/.exec('ab');\n"
            + "log(m[0]);\n"
            + "log(m[1]);\n"
            + "log(m[2]);\n"
            + "log(m[3]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("c")
    public void quantifierOnCaptureOverwrites() throws Exception {
        final String script =
              "var m = /([abc]){3}/.exec('abc');\n"
            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void optionalGroupOnGroup() throws Exception {
        final String script = "log(/(ab)?c/.test('c'));";
        testEvaluate(script);
    }

    // =================================================================
    // Backreference edge cases
    // =================================================================

    @Test
    @Alerts("true")
    public void backreferenceIgnoreCase() throws Exception {
        final String script = "log(/(a)\\1/i.test('aA'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void backreferenceMultipleGroups() throws Exception {
        final String script = "log(/(a)(b)\\2\\1/.test('abba'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void backreferenceInAlternation() throws Exception {
        final String script = "log(/(a)\\1|bb/.test('aa'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void backreferenceInAlternationSecond() throws Exception {
        final String script = "log(/(a)\\1|bb/.test('bb'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void namedBackreferenceQuotedString() throws Exception {
        final String script =
            "log(/(?<q>['\"]).*?\\k<q>/.test('\"hello\"'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void namedBackreferenceMismatch() throws Exception {
        final String script =
            "log(/(?<q>['\"]).*?\\k<q>/.test('\"hello\\''));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void backreferenceEmptyCapture() throws Exception {
        final String script = "log(/(a?)\\1/.test(''));";
        testEvaluate(script);
    }

    @Test
    @Alerts("aba")
    public void backreferenceRepeated() throws Exception {
        final String script =
              "var m = /(a)b\\1/.exec('aba');\n"
            + "log(m[0]);";
        testEvaluate(script);
    }

    // =================================================================
    // Alternation edge cases
    // =================================================================

    @Test
    @Alerts("foo")
    public void alternationFirstMatchWins() throws Exception {
        final String script =
              "var m = /foo|foobar/.exec('foobar');\n"
            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("foobar")
    public void alternationLongerInGroup() throws Exception {
        final String script =
              "var m = /(?:foobar|foo)/.exec('foobar');\n"
            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true"})
    public void alternationMultiple() throws Exception {
        final String script =
              "log(/a|b|c/.test('a'));\n"
            + "log(/a|b|c/.test('b'));\n"
            + "log(/a|b|c/.test('c'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void alternationWithCaptureUndefined() throws Exception {
        final String script =
              "var m = /(a)|(b)/.exec('b');\n"
            + "log(m[1] === undefined);";
        testEvaluate(script);
    }

    @Test
    @Alerts("b")
    public void alternationWithCaptureSecondBranch() throws Exception {
        final String script =
              "var m = /(a)|(b)/.exec('b');\n"
            + "log(m[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void alternationInGroup() throws Exception {
        final String script =
              "log(/^(a|b)$/.test('a'));\n"
            + "log(/^(a|b)$/.test('ab'));";
        testEvaluate(script);
    }

    // =================================================================
    // Escape special characters
    // =================================================================

    @Test
    @Alerts({"true", "true", "true"})
    public void escapeAnchorsAndBrackets() throws Exception {
        final String script =
              "log(/\\^/.test('^'));\n"
            + "log(/\\$/.test('$'));\n"
            + "log(/\\[/.test('['));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true"})
    public void escapeParensAndBrace() throws Exception {
        final String script =
              "log(/\\(/.test('('));\n"
            + "log(/\\)/.test(')'));\n"
            + "log(/\\{/.test('{'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true", "true"})
    public void escapeQuantifiersAndPipe() throws Exception {
        final String script =
              "log(/\\+/.test('+'));\n"
            + "log(/\\*/.test('*'));\n"
            + "log(/\\?/.test('?'));\n"
            + "log(/\\|/.test('|'));";
        testEvaluate(script);
    }

    // =================================================================
    // Sticky and global interaction
    // =================================================================

    @Test
    @Alerts({"a", "0", "a", "2", "null"})
    public void stickyAndGlobalCombined() throws Exception {
        final String script =
              "var r = /a/gy;\n"
            + "var m1 = r.exec('a a');\n"
            + "log(m1[0]);\n"
            + "log(m1.index);\n"
            + "r.lastIndex = 2;\n"
            + "var m2 = r.exec('a a');\n"
            + "log(m2[0]);\n"
            + "log(m2.index);\n"
            + "var m3 = r.exec('a a');\n"
            + "log(m3);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void stickyDoesNotSearchForward() throws Exception {
        final String script =
              "var r = /b/y;\n"
            + "r.lastIndex = 0;\n"
            + "log(r.test('b'));\n"
            + "r.lastIndex = 0;\n"
            + "log(r.test('ab'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void globalSearchesForward() throws Exception {
        final String script =
              "var r = /b/g;\n"
            + "r.lastIndex = 0;\n"
            + "log(r.test('b'));\n"
            + "r.lastIndex = 0;\n"
            + "log(r.test('ab'));";
        testEvaluate(script);
    }

    // =================================================================
    // Flag d (hasIndices) edge cases
    // =================================================================

    @Test
    @Alerts({"0", "3"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void indicesFullMatch() throws Exception {
        final String script =
              "var m = /abc/d.exec('abcdef');\n"
            + "log(m.indices[0][0]);\n"
            + "log(m.indices[0][1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"0", "1", "1", "2", "2", "3"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void indicesMultipleGroups() throws Exception {
        final String script =
              "var m = /(a)(b)(c)/d.exec('abc');\n"
            + "log(m.indices[1][0]);\n"
            + "log(m.indices[1][1]);\n"
            + "log(m.indices[2][0]);\n"
            + "log(m.indices[2][1]);\n"
            + "log(m.indices[3][0]);\n"
            + "log(m.indices[3][1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"0", "1"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void indicesNamedGroup() throws Exception {
        final String script =
              "var m = /(?<x>a)/d.exec('a');\n"
            + "log(m.indices.groups.x[0]);\n"
            + "log(m.indices.groups.x[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "6"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void indicesOffset() throws Exception {
        final String script =
              "var m = /bar/d.exec('foobar');\n"
            + "log(m.indices[0][0]);\n"
            + "log(m.indices[0][1]);";
        testEvaluate(script);
    }

    // =================================================================
    // Unicode property escapes
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void unicodePropertyLetter() throws Exception {
        final String script =
              "log(/\\p{Letter}/u.test('a'));\n"
            + "log(/\\p{Letter}/u.test('1'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void unicodePropertyNumber() throws Exception {
        final String script =
              "log(/\\p{Number}/u.test('3'));\n"
            + "log(/\\p{Number}/u.test('a'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void unicodePropertyNegated() throws Exception {
        final String script =
              "log(/\\P{Letter}/u.test('1'));\n"
            + "log(/\\P{Letter}/u.test('a'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void unicodePropertyUpperCase() throws Exception {
        final String script =
              "log(/\\p{Uppercase_Letter}/u.test('A'));\n"
            + "log(/\\p{Uppercase_Letter}/u.test('a'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void unicodePropertyLowerCase() throws Exception {
        final String script =
              "log(/\\p{Lowercase_Letter}/u.test('a'));\n"
            + "log(/\\p{Lowercase_Letter}/u.test('A'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void unicodePropertyWhitespace() throws Exception {
        final String script =
              "log(/\\p{White_Space}/u.test(' '));\n"
            + "log(/\\p{White_Space}/u.test('a'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void unicodePropertyScriptLatin() throws Exception {
        final String script =
              "log(/\\p{Script=Latin}/u.test('a'));\n"
            + "log(/\\p{Script=Latin}/u.test('Z'));";
        testEvaluate(script);
    }

    // =================================================================
    // Unicode flag edge cases
    // =================================================================

    @Test
    @Alerts("2")
    public void unicodeFlagQuantifiesCodePoints() throws Exception {
        final String script =
            "log('\\uD83D\\uDE00\\uD83D\\uDE01'.match(/./gu).length);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void unicodeFlagDotMatchesAstralPlane() throws Exception {
        final String script =
            "log(/^.$/u.test('\\uD83D\\uDE00'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void unicodeFlagDotMatchesLoneSurrogate() throws Exception {
        // A lone surrogate is a single code unit; /./u matches it as one element
        final String script = "log(/^.$/u.test('\\uD83D'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void unicodeFlagDotNotMatchTwoLoneSurrogates() throws Exception {
        // Two lone surrogates are two code units; /^.$/u matches exactly one
        final String script = "log(/^.$/u.test('\\uD83D\\uD83D'));";
        testEvaluate(script);
    }
}