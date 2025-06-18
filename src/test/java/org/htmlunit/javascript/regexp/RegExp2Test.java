/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.regexp;

import org.htmlunit.SimpleWebTestCase;
import org.htmlunit.WebClient;
import org.htmlunit.corejs.javascript.Context;
import org.htmlunit.corejs.javascript.JavaScriptException;
import org.htmlunit.corejs.javascript.ScriptableObject;
import org.htmlunit.html.HtmlPage;
import org.htmlunit.javascript.HtmlUnitContextFactory;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HtmlUnitRegExpProxy}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Carsten Steul
 */
public class RegExp2Test extends SimpleWebTestCase {

    private static final String STR = "(?:<script.*?>)((\\n|\\r|.)*?)(?:<\\/script>)";
    private static final String BEGIN = "<div>bla</div>";
    private static final String END = "foo\n<span>bla2</span>";
    private static final String TEXT = BEGIN + "<script>var a = 123;</script>" + END;
    private static final String EXPECTED = BEGIN + END;
    private static final String SRC = "var re = new RegExp(str, 'img');\n"
        + "var s = text.replace(re, '');\n"
        + "if (s != expected)\n"
        + "  throw 'Expected >' + expected + '< but got >' + s + '<';";

    private static final String SCRIPT_TEST_MATCH = "function arrayToString(_arr) {\n"
            + "  if (_arr == null) return null;\n"
            + "  var s = '[';\n"
            + "  for (var i = 0; i < _arr.length; i++)\n"
            + "  {\n"
            + "    if (i != 0) s += ', ';\n"
            + "    s += _arr[i];\n"
            + "  }\n"
            + "  s += ']';\n"
            + "  return s;\n"
            + "}\n"

            + "function assertArrEquals(actual, expected) {\n"
            + "  if (expected == null) {\n"
            + "    if (actual != null)\n"
            + "      throw 'Expected >null< got >' + actual + '<';\n"
            + "    else return;\n"
            + "  }\n"
            + "  var expectedStr = arrayToString(expected);\n"
            + "  var actualStr = arrayToString(actual);\n"
            + "  if (expectedStr != actualStr)\n"
            + "    throw 'Expected >' + expectedStr + '< got >' + actualStr + '<';\n"
            + "}\n"

            + "assertArrEquals('ab'.match(), ['']);\n"
            + "assertArrEquals('ab'.match('foo'), null);\n"
            + "assertArrEquals('ab'.match('a'), ['a']);\n"
            + "assertArrEquals('abab'.match('a'), ['a']);\n"
            + "assertArrEquals('abab'.match('.a'), ['ba']);\n"
            + "assertArrEquals('abab'.match(/.a/), ['ba']);\n"
            + "assertArrEquals('li'.match(/^([^a-z0-9_-])?([a-z0-9_-]+)(.*)/i), ['li', undefined, 'li', '']);\n"
            + "assertArrEquals('abab'.match(new RegExp('.a')), ['ba']);\n"
            + "var s = '<script>var a = 1;</' + 'script>';\n"
            + "var re = '(?:<script.*?>)((\\n|\\r|.)*?)(?:<\\/script>)';\n"
            + "assertArrEquals(s.match(re), [s, 'var a = 1;', ';']);\n";

    /**
     * Test that string.replace works correctly (?) in HtmlUnit.
     * @throws Exception if the test fails
     */
    @Test
    public void fixedInHtmlUnit() throws Exception {
        final String html = "<html></html>";
        final HtmlPage page = loadPage(html);
        final ScriptableObject topScope = page.getEnclosingWindow().getScriptableObject();
        topScope.put("str", topScope, STR);
        topScope.put("text", topScope, TEXT);
        topScope.put("expected", topScope, EXPECTED);
        page.executeJavaScript(SRC);
    }

    /**
     * Tests if custom patch is still needed.
     */
    @Test
    public void needCustomFix() {
        final WebClient client = getWebClient();
        final HtmlUnitContextFactory cf = client.getJavaScriptEngine().getContextFactory();
        final Context ctx = cf.enterContext();
        try {
            final ScriptableObject topScope = ctx.initStandardObjects();
            topScope.put("str", topScope, STR);
            topScope.put("text", topScope, TEXT);
            topScope.put("expected", topScope, EXPECTED);
            assertEquals(BEGIN + END, TEXT.replaceAll(STR, ""));
            try {
                ctx.evaluateString(topScope, SRC, "test script", 0, null);
            }
            catch (final JavaScriptException e) {
                assertTrue(e.getMessage().indexOf("Expected >") == 0);
            }
        }
        finally {
            Context.exit();
        }
    }

    /**
     * Test if the custom fix is needed or not. When this test fails, then it means that the problem is solved in
     * Rhino and that custom fix for String.match in {@link HtmlUnitRegExpProxy} is not needed anymore (and that
     * this test can be removed, or turned positive).
     * @throws Exception if the test fails
     */
    @Test
    public void matchFixNeeded() throws Exception {
        final WebClient client = getWebClient();
        final HtmlUnitContextFactory cf = client.getJavaScriptEngine().getContextFactory();
        final Context cx = cf.enterContext();
        try {
            final ScriptableObject topScope = cx.initStandardObjects();
            cx.evaluateString(topScope, SCRIPT_TEST_MATCH, "test script String.match", 0, null);
            try {
                cx.evaluateString(topScope, SCRIPT_TEST_MATCH, "test script String.match", 0, null);
            }
            catch (final JavaScriptException e) {
                assertTrue(e.getMessage().indexOf("Expected >") == 0);
            }
        }
        finally {
            Context.exit();
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void emptySubStringChanged() throws Exception {
        final String html = "<html></html>";
        final HtmlPage page = loadPage(html);
        page.executeJavaScript("'alpha'.replace(/alpha/, '');/beta/.test('abc beta def');");
    }
}
