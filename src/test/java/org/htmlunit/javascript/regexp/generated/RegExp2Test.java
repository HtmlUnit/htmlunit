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
 * Additional tests for RegExp constructor, properties, type checks,
 * and toString behavior.
 *
 * @author Ronald Brill
 */
public class RegExp2Test extends AbstractRegExpTest {

    // =================================================================
    // Constructor edge cases
    // =================================================================

    @Test
    @Alerts("(?:)")
    @HtmlUnitNYI(CHROME = "", EDGE = "", FF = "", FF_ESR = "")
    public void constructorUndefined() throws Exception {
        final String script = "log(new RegExp(undefined).source);";
        testEvaluate(script);
    }

    @Test
    @Alerts("null")
    public void constructorNull() throws Exception {
        final String script = "log(new RegExp(null).source);";
        testEvaluate(script);
    }

    @Test
    @Alerts("123")
    public void constructorNumber() throws Exception {
        final String script = "log(new RegExp(123).source);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void constructorBoolean() throws Exception {
        final String script = "log(new RegExp(true).test('true'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void constructorStringCoercion() throws Exception {
        final String script = "var obj = { toString: function() { return 'abc'; } };\n"
                            + "log(new RegExp(obj).test('abc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorInvalidFlag() throws Exception {
        final String script = "try { new RegExp('a', 'z'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorUnclosedParen() throws Exception {
        final String script = "try { new RegExp('('); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorUnmatchedParen() throws Exception {
        final String script = "try { new RegExp(')'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorUnclosedBracket() throws Exception {
        final String script = "try { new RegExp('['); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorUnclosedGroup() throws Exception {
        final String script = "try { new RegExp('(?:'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorInvalidQuantifier() throws Exception {
        final String script = "try { new RegExp('*'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorInvalidQuantifierPlus() throws Exception {
        final String script = "try { new RegExp('+'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorInvalidQuantifierQuestion() throws Exception {
        final String script = "try { new RegExp('?'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts({"false", "true"})
    public void constructorFromRegExpSeparateIdentity() throws Exception {
        final String script = "var r1 = /foo/g;\n"
                            + "var r2 = new RegExp(r1);\n"
                            + "log(r1 === r2);\n"
                            + "r1.lastIndex = 5;\n"
                            + "log(r2.lastIndex === 0);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void regExpCallWithSameRegExp() throws Exception {
        final String script = "var r = /abc/;\n"
                            + "log(RegExp(r) === r);";
        testEvaluate(script);
    }

    @Test
    @Alerts("false")
    public void regExpCallWithRegExpAndNewFlags() throws Exception {
        final String script = "var r = /abc/;\n"
                            + "log(RegExp(r, 'i') === r);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void constructorEmptyString() throws Exception {
        final String script = "log(new RegExp('').test(''));";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void constructorFlagUAndVMutuallyExclusive() throws Exception {
        final String script = "try { new RegExp('a', 'uv'); log('ok'); }"
                            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    // =================================================================
    // Type checks
    // =================================================================

    @Test
    @Alerts({"true", "true"})
    public void instanceofAndTypeCheck() throws Exception {
        final String script = "log(/foo/ instanceof RegExp);\n"
                            + "log(typeof /foo/ === 'object');";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void prototypeChain() throws Exception {
        final String script = "log(Object.getPrototypeOf(/foo/) === RegExp.prototype);";
        testEvaluate(script);
    }

    @Test
    @Alerts("RegExp")
    public void constructorName() throws Exception {
        final String script = "log(/foo/.constructor.name);";
        testEvaluate(script);
    }

    // =================================================================
    // Properties
    // =================================================================

    @Test
    @Alerts({"foo", "gi"})
    public void sourceAndFlagsProperties() throws Exception {
        final String script = "var r = /foo/gi;\n"
                            + "log(r.source);\n"
                            + "log(r.flags);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true", "true", "true", "true", "true", "true"})
    @HtmlUnitNYI(CHROME = "SyntaxError: invalid flag 'd' after regular expression",
        EDGE = "SyntaxError: invalid flag 'd' after regular expression",
        FF = "SyntaxError: invalid flag 'd' after regular expression",
        FF_ESR = "SyntaxError: invalid flag 'd' after regular expression")
    public void flagBooleanPropertiesAllSet() throws Exception {
        final String script = "var r = new RegExp('', 'dgimsuy');\n"
                            + "log(r.hasIndices);\n"
                            + "log(r.global);\n"
                            + "log(r.ignoreCase);\n"
                            + "log(r.multiline);\n"
                            + "log(r.dotAll);\n"
                            + "log(r.unicode);\n"
                            + "log(r.sticky);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"false", "false", "false", "false", "false"})
    public void flagBooleanPropertiesDefault() throws Exception {
        final String script = "var r = /foo/;\n"
                            + "log(r.global);\n"
                            + "log(r.ignoreCase);\n"
                            + "log(r.multiline);\n"
                            + "log(r.dotAll);\n"
                            + "log(r.sticky);";
        testEvaluate(script);
    }

    @Test
    @Alerts("0")
    public void lastIndexDefault() throws Exception {
        final String script = "log(/foo/.lastIndex);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"5", "5"})
    public void lastIndexWritable() throws Exception {
        final String script = "var r = /foo/;\n"
                            + "r.lastIndex = 5;\n"
                            + "log(r.lastIndex);\n"
                            + "var r2 = /foo/g;\n"
                            + "r2.lastIndex = 5;\n"
                            + "log(r2.lastIndex);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
//    @HtmlUnitNYI(CHROME = "SyntaxError: invalid flag 'v' after regular expression",
//        EDGE = "SyntaxError: invalid flag 'v' after regular expression",
//        FF = "SyntaxError: invalid flag 'v' after regular expression",
//        FF_ESR = "SyntaxError: invalid flag 'v' after regular expression")
    public void unicodeSetsPropertyOnV() throws Exception {
        final String script = "log(new RegExp('.', 'v').unicodeSets);";
        testEvaluate(script);
    }

    @Test
    @Alerts("(?:)")
    @HtmlUnitNYI(CHROME = "", EDGE = "", FF = "", FF_ESR = "")
    public void sourceOfEmptyRegex() throws Exception {
        final String script = "log(new RegExp('').source);";
        testEvaluate(script);
    }

    @Test
    @Alerts("")
    public void flagsOfNoFlagsRegex() throws Exception {
        final String script = "log(/foo/.flags);";
        testEvaluate(script);
    }

    @Test
    @Alerts("a\\/b")
    public void sourceEscapesSlash() throws Exception {
        final String script = "log(/a\\/b/.source);";
        testEvaluate(script);
    }

    // =================================================================
    // toString
    // =================================================================

    @Test
    @Alerts("/(?:)/")
    public void toStringEmptyRegex() throws Exception {
        final String script = "log(new RegExp('').toString());";
        testEvaluate(script);
    }

    @Test
    @Alerts("/foo/gi")
    public void toStringWithFlags() throws Exception {
        final String script = "log(/foo/gi.toString());";
        testEvaluate(script);
    }

    @Test
    @Alerts("/(?:)/")
    public void toStringNoArgConstructor() throws Exception {
        final String script = "log(new RegExp().toString());";
        testEvaluate(script);
    }

    @Test
    @Alerts("/foo/dgimsuy")
    @HtmlUnitNYI(CHROME = "SyntaxError: invalid flag 'd' after regular expression",
        EDGE = "SyntaxError: invalid flag 'd' after regular expression",
        FF = "SyntaxError: invalid flag 'd' after regular expression",
        FF_ESR = "SyntaxError: invalid flag 'd' after regular expression")
    public void toStringAllFlags() throws Exception {
        final String script = "log(new RegExp('foo', 'dgimsuy').toString());";
        testEvaluate(script);
    }

    // =================================================================
    // exec() result structure
    // =================================================================

    @Test
    @Alerts({"foo", "0", "foobar"})
    public void execResultStructure() throws Exception {
        final String script = "var m = /foo/.exec('foobar');\n"
                            + "log(m[0]);\n"
                            + "log(m.index);\n"
                            + "log(m.input);";
        testEvaluate(script);
    }

    @Test
    @Alerts("null")
    public void execReturnsNullOnNoMatch() throws Exception {
        final String script = "log(/xyz/.exec('abc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"bar", "4"})
    public void execIndexProperty() throws Exception {
        final String script = "var m = /bar/.exec('foo bar');\n"
                            + "log(m[0]);\n"
                            + "log(m.index);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"foo", "0", "foo", "4", "null"})
    public void execGlobalMultipleCalls() throws Exception {
        final String script = "var r = /foo/g;\n"
                            + "var m1 = r.exec('foo foo');\n"
                            + "log(m1[0]);\n"
                            + "log(m1.index);\n"
                            + "var m2 = r.exec('foo foo');\n"
                            + "log(m2[0]);\n"
                            + "log(m2.index);\n"
                            + "var m3 = r.exec('foo foo');\n"
                            + "log(m3);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void execResultIsArray() throws Exception {
        final String script = "var m = /a/.exec('a');\n"
                            + "log(Array.isArray(m));";
        testEvaluate(script);
    }

    @Test
    @Alerts("undefined")
    public void execGroupsWithoutNamedCaptures() throws Exception {
        final String script = "var m = /(a)(b)/.exec('ab');\n"
                            + "log(m.groups);";
        testEvaluate(script);
    }

    @Test
    @Alerts("null")
    public void execNamedGroupsPrototypeNull() throws Exception {
        final String script = "var m = /(?<x>a)/.exec('a');\n"
                            + "var proto = Object.getPrototypeOf(m.groups);\n"
                            + "log(proto);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"2", "a", "undefined"})
    public void execNonParticipatingCaptureGroup() throws Exception {
        final String script = "var m = /a(b)?/.exec('a');\n"
                            + "log(m.length);\n"
                            + "log(m[0]);\n"
                            + "log(m[1]);";
        testEvaluate(script);
    }

    // =================================================================
    // test() edge cases
    // =================================================================

    @Test
    @Alerts("true")
    public void testNullCoercion() throws Exception {
        final String script = "log(/null/.test(null));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void testNumberCoercion() throws Exception {
        final String script = "log(/123/.test(123));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void testBooleanCoercion() throws Exception {
        final String script = "log(/true/.test(true));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "true"})
    public void testEmptyRegexEmptyString() throws Exception {
        final String script = "log(new RegExp('').test(''));\n"
                            + "log(new RegExp('').test('abc'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void testReturnsTrueBoolean() throws Exception {
        final String script = "log(/foo/.test('foo') === true);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void testReturnsFalseBoolean() throws Exception {
        final String script = "log(/foo/.test('bar') === false);";
        testEvaluate(script);
    }
}