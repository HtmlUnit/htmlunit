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
import org.junit.jupiter.api.Test;

/**
 * Additional tests for Symbol.match/replace/search/split,
 * RegExp well-known symbol protocols, and misc edge cases.
 *
 * @author Ronald Brill
 */
public class RegExp7Test extends AbstractRegExpTest {

    // =================================================================
    // Symbol.match
    // =================================================================

    @Test
    @Alerts("function")
    public void symbolMatchExists() throws Exception {
        final String script =
            "log(typeof RegExp.prototype[Symbol.match]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"abc", "1"})
    public void symbolMatchInvocation() throws Exception {
        // 'xabcy': 'abc' starts at index 1
        final String script =
              "var m = /abc/[Symbol.match]('xabcy');\n"
            + "log(m[0]);\n"
            + "log(m.index);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"abc", "0"})
    public void symbolMatchInvocationAtStart() throws Exception {
        final String script =
              "var m = /abc/[Symbol.match]('abcxy');\n"
            + "log(m[0]);\n"
            + "log(m.index);";
        testEvaluate(script);
    }

    @Test
    @Alerts("null")
    public void symbolMatchNoResult() throws Exception {
        final String script =
            "log(/xyz/[Symbol.match]('abc'));";
        testEvaluate(script);
    }

    // =================================================================
    // Symbol.replace
    // =================================================================

    @Test
    @Alerts("function")
    public void symbolReplaceExists() throws Exception {
        final String script =
            "log(typeof RegExp.prototype[Symbol.replace]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("xbc")
    public void symbolReplaceInvocation() throws Exception {
        final String script =
            "log(/a/[Symbol.replace]('abc', 'x'));";
        testEvaluate(script);
    }

    // =================================================================
    // Symbol.search
    // =================================================================

    @Test
    @Alerts("function")
    public void symbolSearchExists() throws Exception {
        final String script =
            "log(typeof RegExp.prototype[Symbol.search]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("1")
    public void symbolSearchInvocation() throws Exception {
        final String script =
            "log(/b/[Symbol.search]('abc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("-1")
    public void symbolSearchNoResult() throws Exception {
        final String script =
            "log(/x/[Symbol.search]('abc'));";
        testEvaluate(script);
    }

    // =================================================================
    // Symbol.split
    // =================================================================

    @Test
    @Alerts("function")
    public void symbolSplitExists() throws Exception {
        final String script =
            "log(typeof RegExp.prototype[Symbol.split]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "a", "b", "c"})
    public void symbolSplitInvocation() throws Exception {
        final String script =
              "var a = /,/[Symbol.split]('a,b,c');\n"
            + "log(a.length);\n"
            + "log(a[0]);\n"
            + "log(a[1]);\n"
            + "log(a[2]);";
        testEvaluate(script);
    }

    // =================================================================
    // Symbol.matchAll
    // =================================================================

    @Test
    @Alerts("function")
    public void symbolMatchAllExists() throws Exception {
        final String script =
            "log(typeof RegExp.prototype[Symbol.matchAll]);";
        testEvaluate(script);
    }

    // =================================================================
    // RegExp.prototype properties behavior
    // =================================================================

    @Test
    @Alerts("ok")
    public void sourceCanBeShadowedOnInstance() throws Exception {
        // source is a getter on RegExp.prototype, not an own property;
        // Object.defineProperty on the instance creates a new own data property
        final String script =
              "var r = /foo/;\n"
            + "try { Object.defineProperty(r, 'source',"
            + " { value: 'bar' }); log('ok'); }"
            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("bar")
    public void sourceCanBeShadowedValue() throws Exception {
        final String script =
              "var r = /foo/;\n"
            + "Object.defineProperty(r, 'source', { value: 'bar' });\n"
            + "log(r.source);";
        testEvaluate(script);
    }

    @Test
    @Alerts("ok")
    public void flagsCanBeShadowedOnInstance() throws Exception {
        // flags is a getter on RegExp.prototype, not an own property;
        // Object.defineProperty on the instance creates a new own data property
        final String script =
              "var r = /foo/;\n"
            + "try { Object.defineProperty(r, 'flags',"
            + " { value: 'gi' }); log('ok'); }"
            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("gi")
    public void flagsCanBeShadowedValue() throws Exception {
        final String script =
              "var r = /foo/;\n"
            + "Object.defineProperty(r, 'flags', { value: 'gi' });\n"
            + "log(r.flags);";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void prototypeSourceGetterThrowsOnNonRegExp() throws Exception {
        // Calling the source getter on a non-RegExp object throws
        final String script =
              "var desc = Object.getOwnPropertyDescriptor("
            + "RegExp.prototype, 'source');\n"
            + "try { desc.get.call({}); log('ok'); }"
            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void prototypeGlobalGetterThrowsOnNonRegExp() throws Exception {
        final String script =
              "var desc = Object.getOwnPropertyDescriptor("
            + "RegExp.prototype, 'global');\n"
            + "try { desc.get.call({}); log('ok'); }"
            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    // =================================================================
    // Ignore case edge cases
    // =================================================================

    @Test
    @Alerts({"true", "true", "true"})
    public void ignoreCaseRange() throws Exception {
        final String script =
              "log(/[a-z]/i.test('A'));\n"
            + "log(/[a-z]/i.test('Z'));\n"
            + "log(/[a-z]/i.test('m'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void ignoreCaseSpecificChars() throws Exception {
        final String script =
              "log(/abc/i.test('ABC'));\n"
            + "log(/abc/i.test('AbC'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void ignoreCaseNotAffectNonAlpha() throws Exception {
        final String script =
              "log(/123/i.test('123'));\n"
            + "log(/123/i.test('456'));";
        testEvaluate(script);
    }

    // =================================================================
    // Newline and whitespace in pattern
    // =================================================================

    @Test
    @Alerts("true")
    public void literalNewlineInPattern() throws Exception {
        final String script =
            "log(new RegExp('a\\\\nb').test('a\\nb'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void literalTabInPattern() throws Exception {
        final String script =
            "log(new RegExp('a\\\\tb').test('a\\tb'));";
        testEvaluate(script);
    }

    // =================================================================
    // Complex patterns
    // =================================================================

    @Test
    @Alerts("true")
    public void emailLikePattern() throws Exception {
        final String script =
            "log(/^[\\w.]+@[\\w.]+\\.[a-z]{2,}$/i"
            + ".test('user@example.com'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void emailLikePatternFail() throws Exception {
        final String script =
            "log(/^[\\w.]+@[\\w.]+\\.[a-z]{2,}$/i"
            + ".test('not-an-email'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"192", "168", "1", "1"})
    public void ipAddressCapture() throws Exception {
        final String script =
              "var m = /^(\\d+)\\.(\\d+)\\.(\\d+)\\.(\\d+)$/"
            + ".exec('192.168.1.1');\n"
            + "log(m[1]);\n"
            + "log(m[2]);\n"
            + "log(m[3]);\n"
            + "log(m[4]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "false"})
    public void passwordStrengthPattern() throws Exception {
        final String script =
              "var r = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$/;\n"
            + "log(r.test('Password1'));\n"
            + "log(r.test('short1'));";
        testEvaluate(script);
    }

    // =================================================================
    // Boundary conditions
    // =================================================================

    @Test
    @Alerts({"true", "true"})
    public void matchAtStringBoundaries() throws Exception {
        final String script =
              "log(/^a/.test('abc'));\n"
            + "log(/c$/.test('abc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("null")
    public void execOnEmptyString() throws Exception {
        final String script = "log(/a/.exec(''));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void testOnEmptyStringEmptyRegex() throws Exception {
        final String script = "log(new RegExp('').test(''));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "0"})
    public void execEmptyPatternOnNonEmpty() throws Exception {
        final String script =
              "var m = new RegExp('').exec('abc');\n"
            + "log(m[0] === '');\n"
            + "log(m.index);";
        testEvaluate(script);
    }

    // =================================================================
    // Repeated exec with global
    // =================================================================

    @Test
    @Alerts({"a", "0", "a", "2", "a", "4", "null"})
    public void execGlobalLoopThreeMatches() throws Exception {
        final String script =
              "var r = /a/g;\n"
            + "var s = 'ababa';\n"
            + "var m;\n"
            + "m = r.exec(s); log(m[0]); log(m.index);\n"
            + "m = r.exec(s); log(m[0]); log(m.index);\n"
            + "m = r.exec(s); log(m[0]); log(m.index);\n"
            + "m = r.exec(s); log(m);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"a", "0", "a", "2", "null", "a", "0"})
    public void execGlobalWrapsAround() throws Exception {
        final String script =
              "var r = /a/g;\n"
            + "var s = 'a a';\n"
            + "var m;\n"
            + "m = r.exec(s); log(m[0]); log(m.index);\n"
            + "m = r.exec(s); log(m[0]); log(m.index);\n"
            + "m = r.exec(s); log(m);\n"
            + "m = r.exec(s); log(m[0]); log(m.index);";
        testEvaluate(script);
    }

    // =================================================================
    // Repeated exec with sticky
    // =================================================================

    @Test
    @Alerts({"a", "0", "a", "1", "null"})
    public void execStickyConsecutiveMatches() throws Exception {
        final String script =
              "var r = /a/y;\n"
            + "var s = 'aab';\n"
            + "var m;\n"
            + "m = r.exec(s); log(m[0]); log(m.index);\n"
            + "m = r.exec(s); log(m[0]); log(m.index);\n"
            + "m = r.exec(s); log(m);";
        testEvaluate(script);
    }

    // =================================================================
    // RegExp used in boolean context
    // =================================================================

    @Test
    @Alerts("true")
    public void regexpIsTruthy() throws Exception {
        final String script = "log(!!(/foo/));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void emptyRegexpIsTruthy() throws Exception {
        final String script = "log(!!(new RegExp('')));";
        testEvaluate(script);
    }

    // =================================================================
    // RegExp special string patterns
    // =================================================================

    @Test
    @Alerts("true")
    public void matchDotStar() throws Exception {
        final String script = "log(/.*/.test('anything'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void matchDotStarEmpty() throws Exception {
        final String script = "log(/.*/.test(''));";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void matchDotPlusEmpty() throws Exception {
        final String script = "log(/.+/.test(''));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void matchDotPlusNonEmpty() throws Exception {
        final String script = "log(/.+/.test('x'));";
        testEvaluate(script);
    }
}