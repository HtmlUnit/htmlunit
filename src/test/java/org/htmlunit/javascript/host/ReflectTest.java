/*
 * Copyright (c) 2002-2024 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for Reflect.
 *
 * @author Ronald Brill
 * @author Lai Quang Duong
 */
@RunWith(BrowserRunner.class)
public class ReflectTest extends WebDriverTestCase {

    @Test
    @Alerts("[object Reflect]")
    public void testToString() throws Exception {
        test("log(Reflect.toString())");
    }

    @Test
    @Alerts("1")
    public void apply() throws Exception {
        test("log(Reflect.apply(Math.floor, undefined, [1.75]))");
    }

    @Test
    @Alerts(DEFAULT = {"1", "true", "4", "arg1", "2", "undefined", "null"},
            IE = "no Reflect")
    public void applyDetails() throws Exception {
        final String js =
                "var o = {};\n"
                + "var count = 0;\n"
                + "var results, args;\n"

                + "function fn() {\n"
                + "  count++;\n"
                + "  results = {\n"
                + "    thisArg: this,\n"
                + "    args: arguments\n"
                + "  };\n"
                + "}\n"

                + "Reflect.apply(fn, o, ['arg1', 2, , null]);\n"

                + "log(count);\n"
                + "log(results.thisArg === o);\n"
                + "log(results.args.length);\n"
                + "log(results.args[0]);\n"
                + "log(results.args[1]);\n"
                + "log(results.args[2]);\n"
                + "log(results.args[3]);\n";
        test(js);
    }

    @Test
    @Alerts("exception")
    public void applyMissingArgs() throws Exception {
        final String js =
                "try {\n"
                + "  Reflect.apply();\n"
                + "} catch(e) {\n"
                + "  log('exception');\n"
                + "}";
        test(js);
    }

    @Test
    @Alerts("exception")
    public void applyTargetNotFunction() throws Exception {
        final String js =
                "try {\n"
                + "  Reflect.apply({}, undefined, [1.75]);\n"
                + "} catch(e) {\n"
                + "  log('exception');\n"
                + "}";
        test(js);
    }

    @Test
    @Alerts("exception")
    public void applyArgumentsListNotFunction() throws Exception {
        final String js =
                "var s1 = Symbol('1');"
                + "try {\n"
                + "  Reflect.apply(Math.floor, undefined, s1);\n"
                + "} catch(e) {\n"
                + "  log('exception');\n"
                + "}";
        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"true", "1776"},
            IE = "no Reflect")
    public void construct() throws Exception {
        final String js =
                "var d = Reflect.construct(Date, [1776, 6, 4]);\n"
                + "log(d instanceof Date);\n"
                + "log(d.getFullYear());";
        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"true", "42"},
            IE = "no Reflect")
    public void defineProperty() throws Exception {
        final String js =
                "var o = {};\n"

                + "log(Reflect.defineProperty(o, 'p', { value: 42 }));\n"
                + "log(o.p);";
        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"true", "true", "undefined"},
            IE = "no Reflect")
    public void definePropertyWithoutValue() throws Exception {
        final String js =
                "var o = {};\n"

                + "log(Reflect.defineProperty(o, 'p', {}));\n"
                + "log(Reflect.has(o, 'p'));\n"
                + "log(o.p);";

        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"false", "undefined"},
            IE = "no Reflect")
    public void definePropertyFreezed() throws Exception {
        final String js =
                "var o = {};\n"
                + "Object.freeze(o);\n"

                + "log(Reflect.defineProperty(o, 'p', { value: 42 }));\n"
                + "log(o.p);";
        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"[get,set,enumerable,configurable]", "false", "true", "true", "true"},
            IE = "no Reflect")
    public void getOwnPropertyDescriptor() throws Exception {
        final String js =
                "var o1 = {};\n"
                + "var fn = function() {};\n"
                + "Object.defineProperty(o1, 'p', {\n"
                + "  get: fn,\n"
                + "  configurable: true\n"
                + "});\n"

                + "var result = Reflect.getOwnPropertyDescriptor(o1, 'p');\n"

                + "log('[' + Object.getOwnPropertyNames(result) + ']');\n"
                + "log(result.enumerable);\n"
                + "log(result.configurable);\n"
                + "log(result.get === fn);\n"
                + "log(result.set === undefined);";
        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"true", "false", "false"},
            IE = "no Reflect")
    public void isExtensible() throws Exception {
        final String js =
                "var o1 = {};\n"

                + "log(Reflect.isExtensible(o1));\n"

                + "Reflect.preventExtensions(o1);\n"
                + "log(Reflect.isExtensible(o1));\n"

                + "var o2 = Object.seal({});\n"
                + "log(Reflect.isExtensible(o2));\n";

        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"p1,p2", "length"},
            IE = "no Reflect")
    public void ownKeys() throws Exception {
        final String js =
                "var o1 = {\n"
                + "  p1: 42,\n"
                + "  p2: 'one'\n"
                + "};\n"

                + "var a1 = [];\n"

                + "log(Reflect.ownKeys(o1));\n"
                + "log(Reflect.ownKeys(a1));";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "6", "8", "55", "773", "str", "-1", "str2", "Symbol(foo)", "Symbol(bar)"},
            IE = "no Reflect")
    public void ownKeys2() throws Exception {
        final String js =
                "    var obj = {};\n"
                + "    var s1 = Symbol.for('foo');\n"
                + "    var s2 = Symbol.for('bar');\n"

                + "    obj[s1] = 0;\n"
                + "    obj['str'] = 0;\n"
                + "    obj[773] = 0;\n"
                + "    obj['55'] = 0;\n"
                + "    obj[0] = 0;\n"
                + "    obj['-1'] = 0;\n"
                + "    obj[8] = 0;\n"
                + "    obj['6'] = 0;\n"
                + "    obj[s2] = 0;\n"
                + "    obj['str2'] = 0;\n"

                + "    for (const key of Reflect.ownKeys(obj)){\n"
                + "      log(String(key));\n;"
                + "    }\n";

        test(js);
    }

    @Test
    @Alerts("0")
    public void ownKeysEmptyObj() throws Exception {
        final String js =
                "log(Reflect.ownKeys({}).length)";
        test(js);
    }

    @Test
    @Alerts("0")
    public void ownKeysDeleteObj() throws Exception {
        final String js =
                "var o = { d: 42 };\n"
                + "delete o.d;\n"
                + "log(Reflect.ownKeys(o).length);";
        test(js);
    }

    @Test
    @Alerts("length")
    public void ownKeysEmptyArray() throws Exception {
        final String js =
                "log(Reflect.ownKeys([]));";
        test(js);
    }

    @Test
    @Alerts("2,length")
    public void ownKeysArray() throws Exception {
        final String js =
                "log(Reflect.ownKeys([, , 2]));";
        test(js);
    }

    @Test
    @Alerts("p1,p2")
    public void ownKeysNotEnumerable() throws Exception {
        final String js =
                "var o = {};\n"
                + "Object.defineProperty(o, 'p1', { value: 42, enumerable: false });\n"
                + "Object.defineProperty(o, 'p2', { get: function() {}, enumerable: false });\n"

                + "log(Reflect.ownKeys(o));";
        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"true", "false", "true"},
            IE = "no Reflect")
    public void has() throws Exception {
        final String js =
                "var o1 = { p: 42 }\n"
                + "log(Reflect.has(o1, 'p'));\n"
                + "log(Reflect.has(o1, 'p2'));\n"
                + "log(Reflect.has(o1, 'toString'));";
        test(js);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function () { [native code] }", "true", "false", "true", "true"},
            IE = "no Reflect")
    public void has2() throws Exception {
        final String js =
            "    log(Reflect.has.__proto__);\n"

            + "    log(Reflect.has({x: 0}, 'x'));\n"
            + "    log(Reflect.has({x: 0}, 'y'));\n"
            + "    log(Reflect.has({x: 0}, 'toString'));\n"

            + "    log((Reflect ? Reflect.has : log)({x: 0}, 'x'));\n";

        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"true", "false"},
            IE = "no Reflect")
    public void hasSymbol() throws Exception {
        final String js =
                "var s1 = Symbol('1');\n"
                + "var s2 = Symbol('1');\n"
                + "var o = {};\n"
                + "o[s1] = 42;\n"

                + "log(Reflect.has(o, s1));\n"
                + "log(Reflect.has(o, 2));";
        test(js);
    }

    @Test
    @Alerts("function")
    public void hasProto() throws Exception {
        final String js =
                "var o1 = { p: 42 }\n"
                + "log(typeof Reflect.has.__proto__);";
        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"42", "true", "true", "true"},
            IE = "no Reflect")
    public void getOwnPropertyDescriptorSymbol() throws Exception {
        final String js =
                "var s = Symbol('sym');\n"
                + "var o = {};\n"
                + "o[s] = 42;\n"

                + "var result = Reflect.getOwnPropertyDescriptor(o, s);\n"

                + "log(result.value);\n"
                + "log(result.enumerable);\n"
                + "log(result.configurable);\n"
                + "log(result.writable);";
        test(js);
    }

    @Test
    @Alerts("true")
    public void getOwnPropertyDescriptorUndefinedProperty() throws Exception {
        final String js =
                "var o = Object.create({p: 1});\n"
                + "log(undefined == Reflect.getOwnPropertyDescriptor(o, 'p'));\n";
        test(js);
    }

    @Test
    @Alerts("one")
    public void getPropertyByInt() throws Exception {
        final String js =
                "var a = ['zero', 'one']\n"
                + "log(Reflect.get(a, 1));";
        test(js);
    }


    @Test
    @Alerts(DEFAULT = {"value 1", "undefined", "foo", "42", "undefined"},
            IE = "no Reflect")
    public void getProperty() throws Exception {
        final String js =
                "var o = {};\n"
                + "o.p1 = 'value 1';\n"

                + "log(Reflect.get(o, 'p1'));\n"

                + "Object.defineProperty(o, 'p2', { get: undefined });\n"
                + "log(Reflect.get(o, 'p2'));\n"

                + "Object.defineProperty(o, 'p3', { get: function() { return 'foo'; } });\n"
                + "log(Reflect.get(o, 'p3'));\n"

                + "var o2 = Object.create({ p: 42 });\n"
                + "log(Reflect.get(o2, 'p'));\n"

                + "log(Reflect.get(o2, 'u'));";

        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"true", "true", "false"},
            IE = "no Reflect")
    public void setPrototypeOf() throws Exception {
        final String js =
                "var o1 = {};\n"

                + "log(Reflect.setPrototypeOf(o1, Object.prototype));\n"
                + "log(Reflect.setPrototypeOf(o1, null));\n"

                + "var o2 = {};\n"

                + "log(Reflect.setPrototypeOf(Object.freeze(o2), null));";
        test(js);
    }

    @Test
    @Alerts("false")
    public void setPrototypeOfCycle() throws Exception {
        final String js =
                "var o1 = {};\n"
                + "log(Reflect.setPrototypeOf(o1, o1));";
        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"true", "true", "false"},
            IE = "no Reflect")
    public void setPrototypeOfCycleComplex() throws Exception {
        final String js =
                "var o1 = {};\n"
                + "var o2 = {};\n"
                + "var o3 = {};\n"

                + "log(Reflect.setPrototypeOf(o1, o2));\n"
                + "log(Reflect.setPrototypeOf(o2, o3));\n"
                + "log(Reflect.setPrototypeOf(o3, o1));";
        test(js);
    }

    @Test
    @Alerts(DEFAULT = {"true", "true", "true"},
            IE = "no Reflect")
    public void setPrototypeOfSame() throws Exception {
        final String js =
                "var o1 = {};\n"
                + "Object.preventExtensions(o1);\n"

                + "var o2 = Object.create(null);\n"
                + "Object.preventExtensions(o2);\n"

                + "var proto = {};\n"
                + "var o3 = Object.create(proto);\n"
                + "Object.preventExtensions(o3);\n"

                + "log(Reflect.setPrototypeOf(o1, Object.prototype));\n"
                + "log(Reflect.setPrototypeOf(o2, null));\n"
                + "log(Reflect.setPrototypeOf(o3, proto));";
        test(js);
    }

    private void test(final String js) throws Exception {
        final String html = "<html><head>\n"
                + "<body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  if (typeof Reflect != 'undefined') {\n"
                + js
                + "  } else { log('no Reflect'); }\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
