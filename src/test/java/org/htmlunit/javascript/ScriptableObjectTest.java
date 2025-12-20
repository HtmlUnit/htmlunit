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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriverException;

/**
 * Tests for general scriptable objects in the browser context.
 *
 * @author Jake Cobb
 * @author Ronald Brill
 */
public class ScriptableObjectTest extends WebDriverTestCase {

    /**
     * Tests that writing a property which is a read-only in the prototype
     * behaves as expected (<a href="https://sourceforge.net/p/htmlunit/bugs/1633/">
     * https://sourceforge.net/p/htmlunit/bugs/1633/</a>.
     * @throws Exception on failure
     */
    @Test
    @Alerts({"default", "default", "default"})
    public void readOnlyPrototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
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
    @Alerts({"2", "symbol", "symbol", "1", "c"})
    public void getOwnPropertySymbols() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
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
    @Alerts({"TypeError", "true", "true"})
    public void ctorNotChangeableForPrimitives() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let val = null;\n"
                + "  try {\n"
                + "    val.constructor = 1;\n"
                + "  } catch(e) { logEx(e); }\n"

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
    @Alerts({"TypeError", "TypeError", "true", "TypeError", "true"})
    public void ctorNotChangeableForPrimitivesStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  let val = null;\n"
                + "  try {\n"
                + "    val.constructor = 1;\n"
                + "  } catch(e) { logEx(e); }\n"

                + "  val = 'abc';\n"
                + "  try {\n"
                + "    val.constructor = Number;"
                + "  } catch(e) { logEx(e); }\n"
                + "  log(val.constructor === String)\n"

                // An implicit instance of String('abc') was created and assigned the prop foo
                + "  try {\n"
                + "    val.foo = 'bar';\n"
                + "  } catch(e) { logEx(e); }\n"
                // true, since a new instance of String('abc') was created for this comparison,
                // which doesn't have the foo property
                + "  log (val.foo === undefined);\n"

                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        try {
            loadPageVerifyTitle2(html);
        }
        catch (final WebDriverException e) {
            assertTrue(e.getMessage(), e.getMessage().startsWith(getExpectedAlerts()[0]));
        }
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts({"true", "false", "true", "ctor", "true"})
    public void ctorChangeableHasNoEffectForTypeOf() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
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
                + "  } catch(e) { logEx(e) }\n"

                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception on failure
     */
    @Test
    @Alerts({"true", "false", "true", "ctor", "true"})
    public void ctorChangeableHasNoEffectForTypeOfStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
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
                + "  } catch(e) { logEx(e) }\n"

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
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
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
    @Alerts("TypeError")
    public void ctorChangeableHasNoEffectForSealedStrict() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION

                + "  let a = Object.seal({});\n"
                + "  try {\n"
                + "    a.constructor = Number;\n"
                + "    log(a.constructor === Object);\n"
                + "  } catch(e) { logEx(e) }\n"

                + "</script>\n"
                + "</body>\n"
                + "</html>\n";

        loadPageVerifyTitle2(html);
    }
}
