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
 * Additional tests for String methods that interact with RegExp:
 * match, search, replace, split, matchAll, and replaceAll.
 *
 * @author Ronald Brill
 */
public class RegExp6Test extends AbstractRegExpTest {

    // =================================================================
    // String.prototype.match
    // =================================================================

    @Test
    @Alerts("null")
    public void matchReturnsNull() throws Exception {
        final String script = "log('abc'.match(/xyz/));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"abc", "0", "abc"})
    public void matchNonGlobalResultStructure() throws Exception {
        final String script =
              "var m = 'abc'.match(/abc/);\n"
            + "log(m[0]);\n"
            + "log(m.index);\n"
            + "log(m.input);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "a", "a", "a"})
    public void matchGlobalReturnsAllMatches() throws Exception {
        final String script =
              "var m = 'abacad'.match(/a/g);\n"
            + "log(m.length);\n"
            + "log(m[0]);\n"
            + "log(m[1]);\n"
            + "log(m[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("null")
    public void matchGlobalNoMatch() throws Exception {
        final String script = "log('abc'.match(/xyz/g));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"abc", "b"})
    public void matchWithCaptureGroup() throws Exception {
        final String script =
              "var m = 'abc'.match(/a(b)c/);\n"
            + "log(m[0]);\n"
            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"2", "ab", "ab"})
    public void matchGlobalIgnoresCaptureGroups() throws Exception {
        final String script =
              "var m = 'ab ab'.match(/(a)(b)/g);\n"
            + "log(m.length);\n"
            + "log(m[0]);\n"
            + "log(m[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"foo", "2023"})
    public void matchWithNamedGroup() throws Exception {
        final String script =
              "var m = 'foo2023'.match(/(?<word>[a-z]+)(?<num>\\d+)/);\n"
            + "log(m.groups.word);\n"
            + "log(m.groups.num);";
        testEvaluate(script);
    }

    // =================================================================
    // String.prototype.search
    // =================================================================

    @Test
    @Alerts("3")
    public void searchFindsIndex() throws Exception {
        final String script = "log('abcdef'.search(/def/));";
        testEvaluate(script);
    }

    @Test
    @Alerts("-1")
    public void searchNotFound() throws Exception {
        final String script = "log('abcdef'.search(/xyz/));";
        testEvaluate(script);
    }

    @Test
    @Alerts("0")
    public void searchAtStart() throws Exception {
        final String script = "log('abc'.search(/abc/));";
        testEvaluate(script);
    }

    @Test
    @Alerts("0")
    public void searchIgnoresLastIndex() throws Exception {
        final String script =
              "var r = /a/g;\n"
            + "r.lastIndex = 2;\n"
            + "log('abc'.search(r));";
        testEvaluate(script);
    }

    @Test
    @Alerts("0")
    public void searchIgnoresGlobalFlag() throws Exception {
        final String script = "log('abc'.search(/a/g));";
        testEvaluate(script);
    }

    @Test
    @Alerts("0")
    public void searchWithIgnoreCase() throws Exception {
        final String script = "log('ABC'.search(/abc/i));";
        testEvaluate(script);
    }

    @Test
    @Alerts("0")
    public void searchEmptyRegex() throws Exception {
        final String script = "log('abc'.search(new RegExp('')));";
        testEvaluate(script);
    }

    // =================================================================
    // String.prototype.replace
    // =================================================================

    @Test
    @Alerts("xbc")
    public void replaceFirstMatch() throws Exception {
        final String script = "log('abc'.replace(/a/, 'x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("xbx")
    public void replaceGlobal() throws Exception {
        final String script = "log('aba'.replace(/a/g, 'x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("[a]bc")
    public void replaceWithDollarAmpersand() throws Exception {
        final String script = "log('abc'.replace(/a/, '[$&]'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("bc")
    public void replaceWithDollarBacktick() throws Exception {
        // $` = portion before the match; 'a' at index 0 has empty left context
        final String script = "log('abc'.replace(/a/, '$`'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("xyzxyzc")
    public void replaceWithDollarBacktickNonZeroIndex() throws Exception {
        // $` = portion before the match; 'b' at index 3 in 'xyzbc', left context is 'xyz'
        final String script = "log('xyzbc'.replace(/b/, '$`'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("bcbc")
    public void replaceWithDollarTick() throws Exception {
        // $' = portion after the match; 'a' at index 0, right context is "bc"
        final String script = "log('abc'.replace(/a/, \"$'\"));";
        testEvaluate(script);
    }

    @Test
    @Alerts("$abc")
    public void replaceWithDoubleDollar() throws Exception {
        final String script = "log('abc'.replace(/a/, '$$a'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("xBc")
    public void replaceWithCaptureRef() throws Exception {
        final String script =
            "log('aBc'.replace(/(a)/, 'x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("bc")
    public void replaceWithDollarOne() throws Exception {
        // /a(b)/ matches "ab" capturing "b" as $1; replacement is "b"; remainder is "c"
        final String script =
            "log('abc'.replace(/a(b)/, '$1'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("AB")
    public void replaceWithDollarOneAndTwo() throws Exception {
        // swap captured groups
        final String script =
            "log('BA'.replace(/(B)(A)/, '$2$1'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("A")
    public void replaceWithFunction() throws Exception {
        final String script =
            "log('a'.replace(/a/, function(m) {"
            + " return m.toUpperCase(); }));";
        testEvaluate(script);
    }

    @Test
    @Alerts("XbXd")
    public void replaceGlobalWithFunction() throws Exception {
        final String script =
            "log('abcd'.replace(/[ac]/g, function(m) {"
            + " return 'X'; }));";
        testEvaluate(script);
    }

    @Test
    @Alerts("2023/10/05")
    public void replaceNamedCaptureReference() throws Exception {
        final String script =
            "log('2023-10-05'.replace("
            + "/(?<y>\\d{4})-(?<m>\\d{2})-(?<d>\\d{2})/, "
            + "'$<y>/$<m>/$<d>'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("abc")
    public void replaceNoMatch() throws Exception {
        final String script = "log('abc'.replace(/xyz/, 'Q'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("axc")
    public void replaceOnlyFirstWithoutGlobal() throws Exception {
        final String script = "log('abc'.replace(/b/, 'x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts({"a", "0", "abc"})
    public void replaceFunctionArguments() throws Exception {
        final String script =
              "var args;\n"
            + "'abc'.replace(/a/, function() {"
            + " args = arguments; return 'x'; });\n"
            + "log(args[0]);\n"
            + "log(args[1]);\n"
            + "log(args[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"ab", "a", "b", "0", "abc"})
    public void replaceFunctionArgumentsWithGroups() throws Exception {
        final String script =
              "var args;\n"
            + "'abc'.replace(/(a)(b)/, function() {"
            + " args = arguments; return 'x'; });\n"
            + "log(args[0]);\n"
            + "log(args[1]);\n"
            + "log(args[2]);\n"
            + "log(args[3]);\n"
            + "log(args[4]);";
        testEvaluate(script);
    }

    // =================================================================
    // String.prototype.replaceAll
    // =================================================================

    @Test
    @Alerts("xbxcx")
    public void replaceAllGlobal() throws Exception {
        final String script = "log('abaca'.replaceAll(/a/g, 'x'));";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void replaceAllRequiresGlobal() throws Exception {
        final String script =
            "try { 'abc'.replaceAll(/a/, 'x'); log('ok'); }"
            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    // =================================================================
    // String.prototype.split
    // =================================================================

    @Test
    @Alerts({"3", "a", "b", "c"})
    public void splitBasic() throws Exception {
        final String script =
              "var a = 'a,b,c'.split(/,/);\n"
            + "log(a.length);\n"
            + "log(a[0]);\n"
            + "log(a[1]);\n"
            + "log(a[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "a", "b", "c"})
    public void splitMultipleDelimiters() throws Exception {
        final String script =
              "var a = 'a,b;c'.split(/[,;]/);\n"
            + "log(a.length);\n"
            + "log(a[0]);\n"
            + "log(a[1]);\n"
            + "log(a[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"5", "a", ",", "b", ";", "c"})
    public void splitWithCaptureGroup() throws Exception {
        final String script =
              "var a = 'a,b;c'.split(/([,;])/);\n"
            + "log(a.length);\n"
            + "log(a[0]);\n"
            + "log(a[1]);\n"
            + "log(a[2]);\n"
            + "log(a[3]);\n"
            + "log(a[4]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"2", "a", "b"})
    public void splitWithLimit() throws Exception {
        // limit truncates the result array; does NOT put remainder in last element
        final String script =
              "var a = 'a,b,c'.split(/,/, 2);\n"
            + "log(a.length);\n"
            + "log(a[0]);\n"
            + "log(a[1]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"1", "abc"})
    public void splitNoMatch() throws Exception {
        final String script =
              "var a = 'abc'.split(/x/);\n"
            + "log(a.length);\n"
            + "log(a[0]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "a", "b", "c"})
    public void splitEmptyRegex() throws Exception {
        final String script =
              "var a = 'abc'.split(new RegExp(''));\n"
            + "log(a.length);\n"
            + "log(a[0]);\n"
            + "log(a[1]);\n"
            + "log(a[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "", "b", ""})
    public void splitAtStartAndEnd() throws Exception {
        final String script =
              "var a = 'aba'.split(/a/);\n"
            + "log(a.length);\n"
            + "log(a[0]);\n"
            + "log(a[1]);\n"
            + "log(a[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("0")
    public void splitLimitZero() throws Exception {
        final String script =
              "var a = 'a,b,c'.split(/,/, 0);\n"
            + "log(a.length);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "a", "b", "c"})
    public void splitWhitespace() throws Exception {
        final String script =
              "var a = 'a b c'.split(/\\s/);\n"
            + "log(a.length);\n"
            + "log(a[0]);\n"
            + "log(a[1]);\n"
            + "log(a[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"3", "a", "b", "c"})
    public void splitMultipleWhitespace() throws Exception {
        final String script =
              "var a = 'a  b  c'.split(/\\s+/);\n"
            + "log(a.length);\n"
            + "log(a[0]);\n"
            + "log(a[1]);\n"
            + "log(a[2]);";
        testEvaluate(script);
    }

    // =================================================================
    // String.prototype.matchAll
    // =================================================================

    @Test
    @Alerts({"a", "0", "a", "2"})
    public void matchAllBasic() throws Exception {
        final String script =
              "var it = 'abac'.matchAll(/a/g);\n"
            + "var m1 = it.next().value;\n"
            + "log(m1[0]);\n"
            + "log(m1.index);\n"
            + "var m2 = it.next().value;\n"
            + "log(m2[0]);\n"
            + "log(m2.index);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void matchAllIteratorDone() throws Exception {
        final String script =
              "var it = 'a'.matchAll(/a/g);\n"
            + "it.next();\n"
            + "log(it.next().done);";
        testEvaluate(script);
    }

    @Test
    @Alerts("error")
    public void matchAllRequiresGlobal() throws Exception {
        final String script =
            "try { 'abc'.matchAll(/a/); log('ok'); }"
            + " catch(e) { log('error'); }";
        testEvaluate(script);
    }

    @Test
    @Alerts({"ab", "a", "b"})
    public void matchAllWithCaptureGroups() throws Exception {
        final String script =
              "var it = 'ab cd'.matchAll(/(a)(b)/g);\n"
            + "var m = it.next().value;\n"
            + "log(m[0]);\n"
            + "log(m[1]);\n"
            + "log(m[2]);";
        testEvaluate(script);
    }

    @Test
    @Alerts("true")
    public void matchAllNoMatchDone() throws Exception {
        final String script =
              "var it = 'abc'.matchAll(/xyz/g);\n"
            + "log(it.next().done);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"a", "b"})
    public void matchAllNamedGroups() throws Exception {
        final String script =
              "var it = 'ab'.matchAll(/(?<x>.)/g);\n"
            + "log(it.next().value.groups.x);\n"
            + "log(it.next().value.groups.x);";
        testEvaluate(script);
    }

    @Test
    @Alerts({"true", "0"})
    public void matchAllDoesNotModifyLastIndex() throws Exception {
        final String script =
              "var r = /a/g;\n"
            + "r.lastIndex = 0;\n"
            + "var it = 'abc'.matchAll(r);\n"
            + "it.next();\n"
            + "log(it.next().done);\n"
            + "log(r.lastIndex);";
        testEvaluate(script);
    }
}