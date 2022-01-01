/*
 * Copyright (c) 2002-2022 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.junit.BrowserRunner.NotYetImplemented;

/**
 * Tests for general scriptable objects in the browser context.
 *
 * @author Jake Cobb
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class ScriptableObjectTest extends WebDriverTestCase {

    /**
     * Tests that writing a property which is a read-only in the prototype
     * behaves as expected.
     *
     * @see https://sourceforge.net/p/htmlunit/bugs/1633/
     * @throws Exception on failure
     */
    @Test
    @Alerts({"default", "default", "default"})
    public void readOnlyPrototype() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var proto = Object.create(Object.prototype, {\n"
            + "    myProp: {\n"
            + "        get: function() { return 'default'; }\n"
            + "    }\n"
            + "  });\n"
            + "  var o1 = Object.create(proto);\n"
            + "  var o2 = Object.create(proto);\n"
            + "  o2.myProp = 'bar';\n"
            + "  log(o2.myProp);\n"
            + "  log(o1.myProp);\n"
            + "  log(proto.myProp)"
            + "</script>\n"
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts(DEFAULT = {"2", "symbol", "symbol", "1", "c"},
            IE = "not defined")
    public void getOwnPropertySymbols() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  if (Object.getOwnPropertySymbols) {\n"

                + "    var obj = {};\n"
                + "    var a = Symbol('a');\n"
                + "    var b = Symbol.for('b');\n"

                + "    obj[a] = 'localSymbol';\n"
                + "    obj[b] = 'globalSymbol';\n"
                + "    obj['c'] = 'something else';\n"

                + "    var objectSymbols = Object.getOwnPropertySymbols(obj);\n"
                + "    log(objectSymbols.length);\n"
                + "    log(typeof objectSymbols[0]);\n"
                + "    log(typeof objectSymbols[1]);\n"

                + "    var objectNames = Object.getOwnPropertyNames(obj);\n"
                + "    log(objectNames.length);\n"
                + "    log(objectNames[0]);\n"

                + "  } else { log('not defined'); }\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts({"exception", "true", "true"})
    public void ctorNotChangeableForPrimitives() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let val = null;\n"
                + "  try {\n"
                + "    val.constructor = 1;\n"
                + "  } catch (e) { log('exception'); }\n"

                + "  val = 'abc';\n"
                + "  val.constructor = Number;"
                + "  log(val.constructor === String)\n"

                // An implicit instance of String('abc') was created and assigned the prop foo
                + "  val.foo = 'bar';\n"
                // true, since a new instance of String('abc') was created for this comparison,
                // which doesn't have the foo property
                + "  log (val.foo === undefined);\n"

                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts({"exception", "true", "true"})
    @NotYetImplemented
    public void ctorNotChangeableForPrimitivesStrict() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "  'use strict';\n"

                + "  let val = null;\n"
                + "  try {\n"
                + "    val.constructor = 1;\n"
                + "  } catch (e) { log('exception'); }\n"

                + "  val = 'abc';\n"
                + "  val.constructor = Number;"
                + "  log(val.constructor === String)\n"

                // An implicit instance of String('abc') was created and assigned the prop foo
                + "  val.foo = 'bar';\n"
                // true, since a new instance of String('abc') was created for this comparison,
                // which doesn't have the foo property
                + "  log (val.foo === undefined);\n"

                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true", "ctor", "true"},
            IE = {"true", "false", "true", "exception"})
    @HtmlUnitNYI(CHROME = {"true", "false", "true", "ctor", "false"},
            EDGE = {"true", "false", "true", "ctor", "false"},
            FF = {"true", "false", "true", "ctor", "false"},
            FF_ESR = {"true", "false", "true", "ctor", "false"})
    public void ctorChangeableHasNoEffectForTypeOf() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "  let a = [];\n"
                + "  a.constructor = String\n"
                + "  log(a.constructor === String);\n"
                + "  log(a instanceof String);\n"
                + "  log(a instanceof Array);\n"

                + "  try {\n"
                + "    a = new Event('test');\n"
                + "    log('ctor');\n"
                + "    a.constructor = 'bar';\n"
                + "    log(a.constructor === 'bar');\n"
                + "  } catch (e) { log('exception') }\n"

                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts(DEFAULT = {"true", "false", "true", "ctor", "true"},
            IE = {"true", "false", "true", "exception"})
    @HtmlUnitNYI(CHROME = {"true", "false", "true", "ctor", "exception"},
            EDGE = {"true", "false", "true", "ctor", "exception"},
            FF = {"true", "false", "true", "ctor", "exception"},
            FF_ESR = {"true", "false", "true", "ctor", "exception"})
    public void ctorChangeableHasNoEffectForTypeOfStrict() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION

                + "  let a = [];\n"
                + "  a.constructor = String\n"
                + "  log(a.constructor === String);\n"
                + "  log(a instanceof String);\n"
                + "  log(a instanceof Array);\n"

                + "  try {\n"
                + "    a = new Event('test');\n"
                + "    log('ctor');\n"
                + "    a.constructor = 'bar';\n"
                + "    log(a.constructor === 'bar');\n"
                + "  } catch (e) { log('exception') }\n"

                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts("true")
    public void ctorChangeableHasNoEffectForSealed() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "  let a = Object.seal({});\n"
                + "  a.constructor = Number;\n"
                + "  log(a.constructor === Object);\n"

                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts("true")
    @HtmlUnitNYI(CHROME = "exception",
            EDGE = "exception",
            FF = "exception",
            FF_ESR = "exception",
            IE = "exception")
    public void ctorChangeableHasNoEffectForSealedStrict() throws Exception {
        final String html = "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION

                + "  'use strict';\n"

                + "  let a = Object.seal({});\n"
                + "  try {\n"
                + "    a.constructor = Number;\n"
                + "    log(a.constructor === Object);\n"
                + "  } catch (e) { log('exception') }\n"

                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }
}
