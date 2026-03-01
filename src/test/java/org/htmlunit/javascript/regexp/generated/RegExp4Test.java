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
 * Additional tests for RegExp quantifiers, dot behavior,
 * multiline, and assertion edge cases.
 *
 * @author Ronald Brill
 */
public class RegExp4Test extends AbstractRegExpTest {

    // =================================================================
    // Quantifier edge cases
    // =================================================================

    @Test
    @Alerts("true")
    public void quantifierZeroExact() throws Exception {
        final String script = "log(/ba{0}c/.test('bc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void quantifierZeroMinMax() throws Exception {
        final String script = "log(/ba{0,0}c/.test('bc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void quantifierBraceLiteral() throws Exception {
        final String script = "log(/a{/.test('a{'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void quantifierBraceLiteralIncomplete() throws Exception {
        final String script = "log(/a{1/.test('a{1'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"ab", ""})
    public void quantifierLazyStar() throws Exception {
        // (b*?) is lazy: captures "" (zero b's), then literal b matches the first b
        // full match "ab", capture is ""
        final String script = "var m = /a(b*?)b/.exec('abb');\n"
                            + "log(m[0]);\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"abb", "b"})
    public void quantifierLazyPlus() throws Exception {
        // (b+?) is lazy: captures "b" (one b), then (b*) greedy gets remaining "b"
        // full match "abb", m[2] is "b"
        final String script = "var m = /a(b+?)(b*)/.exec('abb');\n"
                            + "log(m[0]);\n"
                            + "log(m[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void quantifierMinGreaterThanMax() throws Exception {
        final String script = "try { new RegExp('a{3,2}'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("aaaa")
    public void quantifierLargeRepetition() throws Exception {
        final String script = "var m = /a{4}/.exec('aaaaa');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void quantifierZeroOrOneGreedy() throws Exception {
        final String script = "var m = /a?/.exec('a');\n"
                            + "log(m[0] === 'a');";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void quantifierZeroOrOneLazy() throws Exception {
        final String script = "var m = /a??/.exec('a');\n"
                            + "log(m[0] === '');";
        testEvaluate(script);
    }

    @Test
    @Alerts("aaa")
    public void quantifierStarGreedy() throws Exception {
        final String script = "var m = /a*/.exec('aaa');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void quantifierStarLazy() throws Exception {
        final String script = "var m = /a*?/.exec('aaa');\n"
                            + "log(m[0] === '');";
        testEvaluate(script);
    }

    @Test
    @Alerts("a")
    public void quantifierPlusLazy() throws Exception {
        final String script = "var m = /a+?/.exec('aaa');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"booop", "ooo"})
    public void quantifierMinMaxLazy() throws Exception {
        // (o{2,4}?) lazy tries 2 o's first, but then p must match;
        // at 'booop' after 2 o's the next char is 'o' not 'p', so backtracks to 3 o's
        // full match "booop", capture "ooo"
        final String script = "var m = /b(o{2,4}?)p/.exec('booop');\n"
                            + "log(m[0]);\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"boop", "oo"})
    public void quantifierMinMaxLazyMinSuffices() throws Exception {
        // With only 2 o's before p, the lazy minimum of 2 works immediately
        final String script = "var m = /b(o{2,4}?)p/.exec('boop');\n"
                            + "log(m[0]);\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void quantifierOnGroup() throws Exception {
        final String script = "log(/(?:ab){3}/.test('ababab'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void quantifierOnGroupFail() throws Exception {
        final String script = "log(/(?:ab){3}/.test('abab'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("c")
    public void quantifierOnCaptureGroupOverwrites() throws Exception {
        final String script = "var m = /([abc]){3}/.exec('abc');\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void quantifierOneOrZeroOnGroup() throws Exception {
        final String script = "log(/(ab)?c/.test('c'));";
        testEvaluate(script);
    }

    // =================================================================
    // Dot and line terminators
    // =================================================================

    @Test
    @Alerts({"false", "false", "false", "false"})
    public void dotDoesNotMatchLineTerminators() throws Exception {
        final String script = "log(/./.test('\\n'));\n"
                            + "log(/./.test('\\r'));\n"
                            + "log(/./.test('\\u2028'));\n"
                            + "log(/./.test('\\u2029'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true", "true"})
    public void dotAllMatchesLineTerminators() throws Exception {
        final String script = "log(/./s.test('\\n'));\n"
                            + "log(/./s.test('\\r'));\n"
                            + "log(/./s.test('\\u2028'));\n"
                            + "log(/./s.test('\\u2029'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void dotMatchesNullChar() throws Exception {
        final String script = "log(/./.test('\\0'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void dotMatchesHighUnicode() throws Exception {
        final String script = "log(/./.test('\\u00FF'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void dotDoesNotMatchEmptyString() throws Exception {
        final String script = "log(/./.test(''));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void dotMatchesTab() throws Exception {
        final String script = "log(/./.test('\\t'));";
        testEvaluate(script);
    }

    // =================================================================
    // Multiline edge cases
    // =================================================================

    @Test
    @Alerts({"true", "true", "true", "true"})
    public void multilineStartAllTerminators() throws Exception {
        final String script = "log(/^b/m.test('a\\nb'));\n"
                            + "log(/^b/m.test('a\\rb'));\n"
                            + "log(/^b/m.test('a\\u2028b'));\n"
                            + "log(/^b/m.test('a\\u2029b'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true", "true"})
    public void multilineEndAllTerminators() throws Exception {
        final String script = "log(/a$/m.test('a\\nb'));\n"
                            + "log(/a$/m.test('a\\rb'));\n"
                            + "log(/a$/m.test('a\\u2028b'));\n"
                            + "log(/a$/m.test('a\\u2029b'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void multilineStartWithoutFlag() throws Exception {
        final String script = "log(/^b/.test('a\\nb'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void multilineEndWithoutFlag() throws Exception {
        final String script = "log(/a$/.test('a\\nb'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "b", "b", "b"})
    public void multilineGlobalMatchAll() throws Exception {
        final String script = "var m = 'b\\nb\\nb'.match(/^b/gm);\n"
                            + "log(m.length);\n"
                            + "log(m[0]);\n"
                            + "log(m[1]);\n"
                            + "log(m[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "b", "b", "b"})
    public void multilineGlobalEndMatch() throws Exception {
        final String script = "var m = 'b\\nb\\nb'.match(/b$/gm);\n"
                            + "log(m.length);\n"
                            + "log(m[0]);\n"
                            + "log(m[1]);\n"
                            + "log(m[2]);";
        testEvaluate(script);
    }

    // =================================================================
    // Assertion edge cases
    // =================================================================

    @Test
    @Alerts({"true", "true"})
    public void wordBoundaryDigit() throws Exception {
        final String script = "log(/\\b9\\b/.test(' 9 '));\n"
                            + "log(/\\b\\d+\\b/.test('abc 123 xyz'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void wordBoundaryUnderscore() throws Exception {
        final String script = "log(/\\b_\\b/.test(' _ '));\n"
                            + "log(/\\b_foo\\b/.test(' _foo '));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void startEndEmptyString() throws Exception {
        final String script = "log(/^$/.test(''));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void startEndNonEmptyString() throws Exception {
        final String script = "log(/^$/.test('a'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("foo")
    public void lookaheadDoesNotConsume() throws Exception {
        final String script = "var m = /foo(?=bar)/.exec('foobar');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("bar")
    public void lookbehindDoesNotConsume() throws Exception {
        final String script = "var m = /(?<=foo)bar/.exec('foobar');\n"
                            + "log(m[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void lookaheadNested() throws Exception {
        final String script = "log(/(?=a(?=b))ab/.test('ab'));\n"
                            + "log(/(?=a(?=b))ab/.test('ac'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void lookbehindZeroWidth() throws Exception {
        final String script = "log(/(?<=^)a/.test('abc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void lookaheadEndAnchor() throws Exception {
        final String script = "log(/a(?=$)/.test('a'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void wordBoundaryAtStart() throws Exception {
        final String script = "log(/\\bfoo/.test('foo'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void wordBoundaryAtEnd() throws Exception {
        final String script = "log(/foo\\b/.test('foo'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void wordBoundaryBetweenWordAndNonWord() throws Exception {
        final String script = "log(/o\\b!/.test('foo!'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void lookaheadQuantified() throws Exception {
        final String script = "log(/(?=.*foo)bar/.test('barfoo'));\n"
                            + "log(/(?=.*foo)bar/.test('barbaz'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void multipleLookaheads() throws Exception {
        final String script = "log(/(?=.*\\d)(?=.*[a-z])/.test('a1'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void multipleLookaheadsFail() throws Exception {
        final String script = "log(/(?=.*\\d)(?=.*[a-z])/.test('11'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false", EDGE = "false", FF = "false", FF_ESR = "false")
    public void lookbehindWithAlternation() throws Exception {
        final String script = "log(/(?<=cat|dog)s/.test('cats'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "false", EDGE = "false", FF = "false", FF_ESR = "false")
    public void lookbehindWithAlternationSecond() throws Exception {
        final String script = "log(/(?<=cat|dog)s/.test('dogs'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void negativeLookaheadMultiple() throws Exception {
        final String script = "log(/(?!.*\\d)abc/.test('abc1'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void lookbehindInMiddle() throws Exception {
        final String script = "log(/a(?<=a)b/.test('ab'));";
        testEvaluate(script);
    }
}