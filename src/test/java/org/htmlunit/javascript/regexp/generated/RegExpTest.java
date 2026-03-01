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
 * Comprehensive test suite for JavaScript regular expression features.
 *
 * @author Ronald Brill
 */
public class RegExpTest extends AbstractRegExpTest {

    // =================================================================
    // Constructor
    // =================================================================

    @Test
    @Alerts({"(?:)", "true"})
    @HtmlUnitNYI(CHROME = {"", "true"}, EDGE = {"", "true"}, FF = {"", "true"}, FF_ESR = {"", "true"})
    public void constructorEmpty() throws Exception {
        final String script = "log(new RegExp().source);\n"
                            + "log(new RegExp() instanceof RegExp);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"foo", "g", "foo", "i"})
    public void constructorFromExistingRegExp() throws Exception {
        final String script = "var r1 = /foo/g;\n"
                            + "var r2 = new RegExp(r1);\n"
                            + "log(r2.source);\n"
                            + "log(r2.flags);\n"
                            + "var r3 = new RegExp(r1, 'i');\n"
                            + "log(r3.source);\n"
                            + "log(r3.flags);";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorDuplicateFlagsThrows() throws Exception {
        final String script = "try { new RegExp('a', 'gg'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    // =================================================================
    // Properties
    // =================================================================

    @Test
    @Alerts("dgimsuy")
    @HtmlUnitNYI(CHROME = "SyntaxError: invalid flag 'd' after regular expression",
        EDGE = "SyntaxError: invalid flag 'd' after regular expression",
        FF = "SyntaxError: invalid flag 'd' after regular expression",
        FF_ESR = "SyntaxError: invalid flag 'd' after regular expression")
    public void flagsPropertySortedOrder() throws Exception {
        final String script = "log(new RegExp('', 'dgimsuy').flags);";
        testEvaluate(script);
    }

    // =================================================================
    // test() edge cases
    // =================================================================

    @Test
    @Alerts("true")
    public void testUndefinedArg() throws Exception {
        // Without args, .test() stringifies undefined to "undefined"
        final String script = "log(/undef/.test());";
        testEvaluate(script);
    }

    // =================================================================
    // Character class escapes
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void characterClassDigit() throws Exception {
        final String script = "log(/\\d/.test('5'));\n"
                            + "log(/\\d/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void characterClassNonDigit() throws Exception {
        final String script = "log(/\\D/.test('x'));\n"
                            + "log(/\\D/.test('5'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "false"})
    public void characterClassWord() throws Exception {
        final String script = "log(/\\w/.test('A'));\n"
                            + "log(/\\w/.test('_'));\n"
                            + "log(/\\w/.test('-'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void characterClassNonWord() throws Exception {
        final String script = "log(/\\W/.test('-'));\n"
                            + "log(/\\W/.test('A'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "false"})
    public void characterClassWhitespace() throws Exception {
        final String script = "log(/\\s/.test(' '));\n"
                            + "log(/\\s/.test('\\t'));\n"
                            + "log(/\\s/.test('A'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void characterClassNonWhitespace() throws Exception {
        final String script = "log(/\\S/.test('A'));\n"
                            + "log(/\\S/.test(' '));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void characterClassBackspace() throws Exception {
        final String script = "log(/[\\b]/.test('\\b'));\n"
                            + "log(/[\\b]/.test('b'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void characterClassIdentityEscape() throws Exception {
        final String script = "log(/^\\z$/.test('z'));\n"
                            + "log(/^\\z$/.test('\\\\z'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void characterClassLegacyOctal() throws Exception {
        final String script = "log(/\\012/.test('\\n'));";
        testEvaluate(script);
    }

    // =================================================================
    // Character sets
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void characterSet() throws Exception {
        final String script = "log(/[aeiou]/.test('e'));\n"
                            + "log(/[aeiou]/.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void characterSetNegated() throws Exception {
        final String script = "log(/[^aeiou]/.test('x'));\n"
                            + "log(/[^aeiou]/.test('e'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void characterSetRange() throws Exception {
        final String script = "log(/[a-m]/.test('c'));\n"
                            + "log(/[a-m]/.test('z'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void characterClassInvalidRangeThrows() throws Exception {
        final String script = "try { new RegExp('[z-a]'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void characterSetHyphen() throws Exception {
        final String script = "log(/[a-z-]/i.test('-'));\n"
                            + "log(/[a-z-]/i.test('1'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void characterSetHyphenEscape() throws Exception {
        final String script = "log(/[a\\-c]/.test('-'));\n"
                            + "log(/[a\\-c]/.test('c'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void characterSetCaret() throws Exception {
        final String script = "log(/[a^b]/.test('^'));\n"
                            + "log(/[a^b]/.test('c'));";
        testEvaluate(script);
    }

    // =================================================================
    // Quantifiers
    // =================================================================

    @Test
    @Alerts({"true", "true", "true", "false"})
    public void quantifierZeroOrMore() throws Exception {
        final String script = "log(/bo*p/.test('bp'));\n"
                            + "log(/bo*p/.test('bop'));\n"
                            + "log(/bo*p/.test('booop'));\n"
                            + "log(/bo*p/.test('bxp'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"false", "true", "true"})
    public void quantifierOneOrMore() throws Exception {
        final String script = "log(/bo+p/.test('bp'));\n"
                            + "log(/bo+p/.test('bop'));\n"
                            + "log(/bo+p/.test('booop'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "false"})
    public void quantifierZeroOrOne() throws Exception {
        final String script = "log(/bo?p/.test('bp'));\n"
                            + "log(/bo?p/.test('bop'));\n"
                            + "log(/bo?p/.test('boop'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"false", "true", "false"})
    public void quantifierExact() throws Exception {
        final String script = "log(/bo{2}p/.test('bop'));\n"
                            + "log(/bo{2}p/.test('boop'));\n"
                            + "log(/bo{2}p/.test('booop'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"false", "true", "true"})
    public void quantifierMin() throws Exception {
        final String script = "log(/bo{2,}p/.test('bop'));\n"
                            + "log(/bo{2,}p/.test('boop'));\n"
                            + "log(/bo{2,}p/.test('booop'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"false", "true", "true", "false"})
    public void quantifierMinMax() throws Exception {
        final String script = "log(/bo{2,3}p/.test('bop'));\n"
                            + "log(/bo{2,3}p/.test('boop'));\n"
                            + "log(/bo{2,3}p/.test('booop'));\n"
                            + "log(/bo{2,3}p/.test('boooop'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"aa", "a"})
    public void quantifierGreedyBacktracking() throws Exception {
        final String script = "var m = /(a+)(a+)/.exec('aaa');\n"
                            + "log(m[1]);\n"
                            + "log(m[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"a", "aa"})
    public void quantifierLazyBacktracking() throws Exception {
        final String script = "var m = /(a+?)(a+)/.exec('aaa');\n"
                            + "log(m[1]);\n"
                            + "log(m[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void quantifierEmptyMatch() throws Exception {
        final String script = "var m = /a??/.exec('a');\n"
                            + "log(m[0] === '');";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "2", "undefined"})
    public void quantifierOnEmptyGroup() throws Exception {
        final String script = "var m = /()*/.exec('a');\n"
                            + "log(m[0] === '');\n"
                            + "log(m.length);\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    // =================================================================
    // Assertions: anchors and word boundaries
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void assertionStartLine() throws Exception {
        final String script = "log(/^foo/.test('foobar'));\n"
                            + "log(/^foo/.test('barfoo'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void assertionEndLine() throws Exception {
        final String script = "log(/foo$/.test('barfoo'));\n"
                            + "log(/foo$/.test('foobar'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void assertionWordBoundary() throws Exception {
        final String script = "log(/\\bfoo\\b/.test('foo bar'));\n"
                            + "log(/\\bfoo\\b/.test('foobar'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void assertionNonWordBoundary() throws Exception {
        final String script = "log(/\\Bfoo\\B/.test('xfoox'));\n"
                            + "log(/\\Bfoo\\B/.test('foo'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void assertionWordBoundaryNonAscii() throws Exception {
        final String script = "log(/\\b/.test('\\u00E9'));";
        testEvaluate(script);
    }

    // =================================================================
    // Assertions: lookahead and lookbehind
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void assertionLookahead() throws Exception {
        final String script = "log(/foo(?=bar)/.test('foobar'));\n"
                            + "log(/foo(?=bar)/.test('foobaz'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("aa")
    public void assertionLookaheadWithCapture() throws Exception {
        final String script = "var m = /(?=(a))a\\1/.exec('aa');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void assertionNegativeLookahead() throws Exception {
        final String script = "log(/foo(?!bar)/.test('foobaz'));\n"
                            + "log(/foo(?!bar)/.test('foobar'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("undefined")
    public void assertionNegativeLookaheadWithCapture() throws Exception {
        final String script = "var m = /(?!(a)b)a/.exec('a');\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void assertionLookbehind() throws Exception {
        final String script = "log(/(?<=foo)bar/.test('foobar'));\n"
                            + "log(/(?<=foo)bar/.test('bazbar'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void assertionLookbehindVariableLength() throws Exception {
        final String script = "log(/(?<=a+)b/.test('aaab'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("a")
    public void assertionLookbehindWithCapture() throws Exception {
        final String script = "var m = /(?<=(a))b/.exec('ab');\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void assertionNegativeLookbehind() throws Exception {
        final String script = "log(/(?<!foo)bar/.test('bazbar'));\n"
                            + "log(/(?<!foo)bar/.test('foobar'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("b")
    public void assertionNegativeLookbehindWithCapture() throws Exception {
        final String script = "var m = /(?<!(a))b/.exec('b');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    // =================================================================
    // Groups: capture, non-capture, named
    // =================================================================

    @Test
    @Alerts({"foobar", "foo"})
    public void groupCapture() throws Exception {
        final String script = "var m = /(foo)bar/.exec('foobar');\n"
                            + "log(m[0]);\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"foobar", "undefined"})
    public void groupNonCapture() throws Exception {
        final String script = "var m = /(?:foo)bar/.exec('foobar');\n"
                            + "log(m[0]);\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"2023", "10"})
    public void groupNamedCapture() throws Exception {
        final String script = "var m = /(?<year>\\d{4})-(?<month>\\d{2})/.exec('2023-10');\n"
                            + "log(m.groups.year);\n"
                            + "log(m.groups.month);";
        testEvaluate(script);
    }

    @Test
    @Alerts("a")
    public void groupOverwriting() throws Exception {
        final String script = "var m = /(a)+/.exec('aaa');\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void groupNamedDuplicateThrows() throws Exception {
        final String script = "try { new RegExp('(?<n>a)(?<n>b)'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("b")
    public void groupNamedSameNameInAlternation() throws Exception {
        final String script = "var m = /(?<name>a)|(?<name>b)/.exec('b');\n"
                            + "log(m.groups.name);";
        testEvaluate(script);
    }

    // =================================================================
    // Backreferences
    // =================================================================

    @Test
    @Alerts({"true", "true", "false"})
    public void backReference() throws Exception {
        final String script = "log(/(foo|bar)\\1/.test('foofoo'));\n"
                            + "log(/(foo|bar)\\1/.test('barbar'));\n"
                            + "log(/(foo|bar)\\1/.test('foobar'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void namedBackReference() throws Exception {
        final String script = "log(/(?<word>foo|bar)\\k<word>/.test('foofoo'));\n"
                            + "log(/(?<word>foo|bar)\\k<word>/.test('foobar'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void namedBackReferenceInvalidThrows() throws Exception {
        final String script = "try { new RegExp('(?<word>a)\\\\k<invalid>'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("b")
    public void backReferenceToUndefinedGroup() throws Exception {
        final String script = "var m = /(a)?\\1b/.exec('b');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("A")
    public void backreferenceToFutureGroup() throws Exception {
        final String script = "var m = /\\1(A)/.exec('A');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("a")
    public void backreferenceInsideOwnGroup() throws Exception {
        final String script = "var m = /(\\1a)/.exec('a');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    // =================================================================
    // Alternation
    // =================================================================

    @Test
    @Alerts({"true", "true", "false"})
    public void alternation() throws Exception {
        final String script = "log(/foo|bar/.test('foo'));\n"
                            + "log(/foo|bar/.test('bar'));\n"
                            + "log(/foo|bar/.test('baz'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void alternationEmptyBranch() throws Exception {
        final String script = "var m = /a||b/.exec('');\n"
                            + "log(m[0] === '');";
        testEvaluate(script);
    }

    // =================================================================
    // Dot behavior
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void dotMatchesEverythingButNewline() throws Exception {
        final String script = "log(/f.o/.test('fxo'));\n"
                            + "log(/f.o/.test('f\\no'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("2")
    public void dotWithoutUnicodeFlag() throws Exception {
        final String script = "log('\\uD842\\uDFB7'.match(/./g).length);";
        testEvaluate(script);
    }

    @Test
    @Alerts("1")
    public void dotWithUnicodeFlag() throws Exception {
        final String script = "log('\\uD842\\uDFB7'.match(/./gu).length);";
        testEvaluate(script);
    }

    // =================================================================
    // Escape sequences and toString
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void escapeSpecial() throws Exception {
        final String script = "log(/\\./.test('.'));\n"
                            + "log(/\\./.test('x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("/a\\/b/")
    public void toStringEscapesSlash() throws Exception {
        final String script = "log(/a\\/b/.toString());";
        testEvaluate(script);
    }

    // =================================================================
    // Flag: dotAll (s)
    // =================================================================

    @Test
    @Alerts("true")
    public void flagDotAll() throws Exception {
        final String script = "log(/f.o/s.test('f\\no'));";
        testEvaluate(script);
    }

    // =================================================================
    // Flag: ignoreCase (i)
    // =================================================================

    @Test
    @Alerts("true")
    public void flagIgnoreCase() throws Exception {
        final String script = "log(/a/i.test('A'));";
        testEvaluate(script);
    }

    // =================================================================
    // Flag: multiline (m)
    // =================================================================

    @Test
    @Alerts("true")
    public void flagMultilineStart() throws Exception {
        final String script = "log(/^b/m.test('a\\nb\\nc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void flagMultilineEnd() throws Exception {
        final String script = "log(/b$/m.test('a\\nb\\nc'));";
        testEvaluate(script);
    }

    // =================================================================
    // Flag: sticky (y)
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void flagSticky() throws Exception {
        final String script = "var r = /b/y;\n"
                            + "r.lastIndex = 1;\n"
                            + "log(r.test('abc'));\n"
                            + "log(r.test('abc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "2"})
    public void flagStickyLastIndex() throws Exception {
        final String script = "var r = /b/y; r.lastIndex = 1;\n"
                            + "log(r.test('abc'));\n"
                            + "log(r.lastIndex);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"false", "0"})
    public void stickyFailureResetsLastIndex() throws Exception {
        final String script = "var r = /b/y;\n"
                            + "r.lastIndex = 1;\n"
                            + "log(r.test('axc'));\n"
                            + "log(r.lastIndex);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"false", "true"})
    public void flagStickyStartAnchor() throws Exception {
        final String script = "var r = /^b/y; r.lastIndex = 1;\n"
                            + "log(r.test('abc'));\n"
                            + "var r2 = /^b/my; r2.lastIndex = 2;\n"
                            + "log(r2.test('a\\nbc'));";
        testEvaluate(script);
    }

    // =================================================================
    // Flag: global (g) and lastIndex behavior
    // =================================================================

    @Test
    @Alerts({"2", "a", "a"})
    public void flagGlobal() throws Exception {
        final String script = "var m = 'aba'.match(/a/g);\n"
                            + "log(m.length);\n"
                            + "log(m[0]);\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "2"})
    public void flagGlobalLastIndex() throws Exception {
        final String script = "var r = /b/g; r.lastIndex = 1;\n"
                            + "log(r.test('abc'));\n"
                            + "log(r.lastIndex);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"false", "0"})
    public void globalFailureResetsLastIndex() throws Exception {
        final String script = "var r = /b/g; r.lastIndex = 1;\n"
                            + "log(r.test('axc'));\n"
                            + "log(r.lastIndex);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"1", "true"})
    public void flagGlobalZeroWidthStart() throws Exception {
        final String script = "var m = 'abc'.match(/^/g);\n"
                            + "log(m.length);\n"
                            + "log(m[0] === '');";
        testEvaluate(script);
    }

    @Test
    @Alerts({"0", "0", "0", "0"})
    public void execGlobalZeroWidthAdvancesLastIndex() throws Exception {
        final String script = "var r = /^/g;\n"
                            + "var m1 = r.exec('ab');\n"
                            + "log(m1.index);\n"
                            + "log(r.lastIndex);\n"
                            + "var m2 = r.exec('ab');\n"
                            + "log(m2.index);\n"
                            + "log(r.lastIndex);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "1"})
    public void flagNonGlobalLastIndex() throws Exception {
        final String script = "var r = /b/; r.lastIndex = 1;\n"
                            + "log(r.test('abc'));\n"
                            + "log(r.lastIndex);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"2", "a", "true"})
    public void flagGlobalEmptyMatches() throws Exception {
        final String script = "var m = 'a'.match(/a*/g);\n"
                            + "log(m.length);\n"
                            + "log(m[0]);\n"
                            + "log(m[1] === '');";
        testEvaluate(script);
    }

    @Test
    @Alerts("4")
    public void flagGlobalEmptyMatchesOnly() throws Exception {
        final String script = "var m = 'abc'.match(/(?:)/g);\n"
                            + "log(m.length);";
        testEvaluate(script);
    }

    // =================================================================
    // Flag: hasIndices (d)
    // =================================================================

    @Test
    @Alerts({"1", "4"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void flagIndices() throws Exception {
        final String script = "var m = /foo/d.exec('xfooy');\n"
                            + "log(m.indices[0][0]);\n"
                            + "log(m.indices[0][1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"1", "2"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void flagIndicesCaptureGroups() throws Exception {
        final String script = "var m = /a(b)c/d.exec('abc');\n"
                            + "log(m.indices[1][0]);\n"
                            + "log(m.indices[1][1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("undefined")
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void flagIndicesUndefinedGroup() throws Exception {
        final String script = "var m = /a(b)?/d.exec('a');\n"
                            + "log(m.indices[1]);";
        testEvaluate(script);
    }

    // =================================================================
    // Flag: unicode (u) and strict mode
    // =================================================================

    @Test
    @Alerts("error")
    public void flagUnicodeInvalidEscapeThrows() throws Exception {
        final String script = "try { new RegExp('\\\\z', 'u'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    // =================================================================
    // Unicode: property escapes
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void unicodePropertyEscape() throws Exception {
        final String script = "log(/\\p{Script=Greek}/u.test('\\u03B1'));\n"
                            + "log(/\\p{Script=Greek}/u.test('a'));";
        testEvaluate(script);
    }

    // =================================================================
    // Unicode: surrogate pairs
    // =================================================================

    @Test
    @Alerts({"true", "false"})
    public void unicodeSurrogatePairWithoutU() throws Exception {
        final String script = "log(/^\\uD83D\\uDE00$/.test('\\uD83D\\uDE00'));\n"
                            + "log(/^.$/.test('\\uD83D\\uDE00'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void unicodeSurrogatePairWithU() throws Exception {
        final String script = "log(/^.$/u.test('\\uD83D\\uDE00'));";
        testEvaluate(script);
    }

    // =================================================================
    // Unicode: case folding
    // =================================================================

    @Test
    @Alerts({"true", "true"})
    public void unicodeCaseFoldingLongS() throws Exception {
        final String script = "log(/\\u017F/iu.test('s'));\n"
                            + "log(/s/iu.test('\\u017F'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void unicodeCaseFoldingKelvin() throws Exception {
        final String script = "log(/\\u212A/iu.test('k'));\n"
                            + "log(/k/iu.test('\\u212A'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true"})
    public void unicodeCaseFoldingGreek() throws Exception {
        final String script = "log(/\\u03A3/iu.test('\\u03C2'));\n"
                            + "log(/\\u03C3/iu.test('\\u03C2'));\n"
                            + "log(/\\u03C2/iu.test('\\u03A3'));";
        testEvaluate(script);
    }

    // =================================================================
    // Flag: unicodeSets (v)
    // =================================================================

    @Test
    @Alerts("error")
    public void flagUnicodeSetsInvalidPropertyThrows() throws Exception {
        final String script = "try { new RegExp('\\\\p{Invalid_Property}', 'v'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void flagUnicodeSetsIntersection() throws Exception {
        final String script = "log(/[\\p{Letter}&&\\p{ASCII}]/v.test('A'));\n"
                            + "log(/[\\p{Letter}&&\\p{ASCII}]/v.test('\\u03B1'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void flagUnicodeSetsDifference() throws Exception {
        final String script = "log(/[\\p{Letter}--\\p{ASCII}]/v.test('\\u03B1'));\n"
                            + "log(/[\\p{Letter}--\\p{ASCII}]/v.test('A'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void flagUnicodeSetsNested() throws Exception {
        final String script = "log(/[\\p{Decimal_Number}--[0-9]]/v.test('\\u0664'));\n"
                            + "log(/[\\p{Decimal_Number}--[0-9]]/v.test('5'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    @HtmlUnitNYI(CHROME = {}, EDGE = {}, FF = {}, FF_ESR = {})
    public void flagUnicodeSetsStringPropertyEmoji() throws Exception {
        final String script = "log(/^\\p{RGI_Emoji}$/v.test('\\u{1F600}'));\n"
                            + "log(/^\\p{RGI_Emoji}$/v.test('\\u{1F44D}\\u{1F3FD}'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void flagUnicodeSetsUnion() throws Exception {
        final String script = "try { new RegExp('[\\p{ASCII}\\p{Greek}]', 'v'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    // =================================================================
    // Empty regex
    // =================================================================

    @Test
    @Alerts({"true", "0"})
    public void regexEmpty() throws Exception {
        final String script = "var m = new RegExp('').exec('abc');\n"
                            + "log(m[0] === '');\n"
                            + "log(m.index);";
        testEvaluate(script);
    }
}