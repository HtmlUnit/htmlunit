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
package org.htmlunit.javascript;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;

/**
 * Tests for the Reflect.
 *
 * @author Ronald Brill
 */
public class NativeReflectTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getReceiverDefaultsToTarget() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var o = {};\n"
                + "  Object.defineProperty(o, 'p', {\n"
                + "    get() { return this === o; }\n"
                + "  });\n"
                + "  log('' + Reflect.get(o, 'p'));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getReceiverPassedToGetter() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var target = {};\n"
                + "  var receiver = {};\n"
                + "  Object.defineProperty(target, 'p', {\n"
                + "    get() { return this === receiver; }\n"
                + "  });\n"
                + "  log('' + Reflect.get(target, 'p', receiver));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getReceiverPassedToGetterOnPrototype() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var receiver = {};\n"
                + "  var proto = {};\n"
                + "  Object.defineProperty(proto, 'p', {\n"
                + "    get() { return this === receiver; }\n"
                + "  });\n"
                + "  var target = Object.create(proto);\n"
                + "  log('' + Reflect.get(target, 'p', receiver));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("42")
    public void getReceiverDoesNotAffectDataProperty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var target = { p: 42 };\n"
                + "  var receiver = { p: 99 };\n"
                + "  log('' + Reflect.get(target, 'p', receiver));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void getReceiverWithSymbolKey() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var sym = Symbol('test');\n"
                + "  var target = {};\n"
                + "  var receiver = {};\n"
                + "  Object.defineProperty(target, sym, {\n"
                + "    get() { return this === receiver; }\n"
                + "  });\n"
                + "  log('' + Reflect.get(target, sym, receiver));"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void setReceiverDefaultsToTarget() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var o = {};\n"
                + "  var receivedReceiver;\n"
                + "  Object.defineProperty(o, 'p', {\n"
                + "    set(v) { receivedReceiver = this; }\n"
                + "  });\n"
                + "  Reflect.set(o, 'p', 1);\n"
                + "  log('' + (receivedReceiver === o));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void setReceiverPassedToSetter() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var target = {};\n"
                + "  var receiver = {};\n"
                + "  var receivedReceiver;\n"
                + "  Object.defineProperty(target, 'p', {\n"
                + "    set(v) { receivedReceiver = this; }\n"
                + "  });\n"
                + "  Reflect.set(target, 'p', 1, receiver);\n"
                + "  log('' + (receivedReceiver === receiver));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void setReceiverPassedToSetterOnPrototype() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var receiver = {};\n"
                + "  var proto = {};\n"
                + "  var receivedReceiver;\n"
                + "  Object.defineProperty(proto, 'p', {\n"
                + "    set(v) { receivedReceiver = this; }\n"
                + "  });\n"
                + "  var target = Object.create(proto);\n"
                + "  Reflect.set(target, 'p', 1, receiver);\n"
                + "  log('' + (receivedReceiver === receiver));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("99")
    public void setReceiverAffectsWhereDataPropertyIsWritten() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var target = { p: 1 };\n"
                + "  var receiver = { p: 0 };\n"
                + "  Reflect.set(target, 'p', 99, receiver);\n"
                + "  log('' + receiver.p);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void setReceiverWithSymbolKey() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var sym = Symbol('test');\n"
                + "  var target = {};\n"
                + "  var receiver = {};\n"
                + "  var receivedReceiver;\n"
                + "  Object.defineProperty(target, sym, {\n"
                + "    set(v) { receivedReceiver = this; }\n"
                + "  });\n"
                + "  Reflect.set(target, sym, 1, receiver);\n"
                + "  log('' + (receivedReceiver === receiver));\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true undefined true")
    public void setMissingValueArgumentTreatedAsUndefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let target = {};\n"
                + "  let result = Reflect.set(target, 'p');\n"
                + "  log('' + result + ' ' + target.p + ' ' + (target.p === undefined));"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void setMissingValueWithReceiverTreatedAsUndefined() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let res = [];\n"
                + "  let proxy = new Proxy({}, {\n"
                + "       set: function(t, prop, value, receiver) {\n"
                + "               res.push(value === undefined);\n"
                + "               return true;\n"
                + "             }\n"
                + "     });\n"
                + "  Reflect.set(proxy, 'p');\n"
                + "  log('' + res);"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "0", "true"})
    public void reflectSetTypedArray() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let receiver = {};\n"
                + "  let typedArray = new Int32Array(10);\n"
                + "  let valueOfCalled = 0;\n"
                + "  let value = { valueOf() { valueOfCalled++; return 1; } };\n"
                + "  log(Reflect.set(typedArray, 0, value, receiver));\n"
                + "  log(valueOfCalled);\n"
                + "  log(receiver[0] === value);"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "1"})
    public void reflectSetTypedArrayInvalidIndex() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  let result = '';\n"
                + "  let receiver = new Int32Array(10);\n"
                + "  let obj = Object.create(receiver);\n"
                + "  let valueOfCalled = 0;\n"
                + "  let value = { valueOf() { valueOfCalled++; return 1; } };\n"
                + "  log(Reflect.set(obj, 100, value, receiver));\n"
                + "  log(valueOfCalled);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "1"})
    public void reflectSetTypedArrayReceiverOobCoercesValue() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var receiver = new Int32Array(10);\n"
                + "  var obj = Object.create(receiver);\n"
                + "  var valueOfCalled = 0;\n"
                + "  var value = { valueOf() { valueOfCalled++; return 1; } };\n"
                + "  log(Reflect.set(obj, 100, value, receiver));\n"
                + "  log(valueOfCalled);"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "1", "42"})
    public void reflectSetTypedArrayReceiverInBoundsCoercesAndWrites() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var receiver = new Int32Array(10);\n"
                + "  var obj = Object.create(receiver);\n"
                + "  var valueOfCalled = 0;\n"
                + "  var value = { valueOf() { valueOfCalled++; return 42; } };\n"
                + "  log(Reflect.set(obj, 0, value, receiver));\n"
                + "  log(valueOfCalled);\n"
                + "  log(receiver[0]);"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "0", "true"})
    public void reflectSetTypedArrayTargetPlainReceiverDoesNotCoerce() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var result = '';\n"
                + "  var receiver = {};\n"
                + "  var typedArray = new Int32Array(10);\n"
                + "  var valueOfCalled = 0;\n"
                + "  var value = { valueOf() { valueOfCalled++; return 1; } };\n"
                + "  log(Reflect.set(typedArray, 0, value, receiver));\n"
                + "  log(valueOfCalled);\n"
                + "  log(receiver[0] === value);"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "undefined"})
    public void defineOwnPropertyOutOfBoundsDoesNotThrowViaReflect() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var ta = new Int32Array(4);\n"
                + "  log(Reflect.defineProperty(ta, '10', { value: 1 }));"
                + "  log(ta[10]);"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"false", "undefined"})
    public void defineOwnPropertyOutOfBoundsWriteIsDiscarded() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var ta = new Int32Array(4);\n"
                + "  var value = { valueOf() { return 99; } };\n"
                + "  log(Reflect.defineProperty(ta, '10', { value: value }));\n"
                + "  log(ta[10]);"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    @Test
    @Alerts({"1,2,3,4", "true", "true", "true"})
    public void constructSubclassBuiltin() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function CustomSet() {\n"
                + "    return Reflect.construct(Set, arguments, this.constructor);\n"
                + "  }\n"
                + "  CustomSet.prototype = Object.create(Set.prototype);\n"
                + "  CustomSet.prototype.constructor = CustomSet;\n"
                + "  var set = new CustomSet([1, 2, 3]);\n"
                + "  set.add(4);\n"
                + "  log(Array.from(set));\n"
                + "  log(set instanceof CustomSet);\n"
                + "  log(set instanceof Set);\n"
                + "  log(Object.getPrototypeOf(set) === CustomSet.prototype)\n;"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
