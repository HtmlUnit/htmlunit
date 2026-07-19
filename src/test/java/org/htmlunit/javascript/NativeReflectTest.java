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

    /**
     * Test common workaround for subclassing built-in objects (like Set) without using ES6
     * class/extends syntax, by using Reflect.construct() with a newTarget argument.
     * @throws Exception if the test fails
     */
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

    /**
     * Test common workaround for subclassing built-in objects (like Map) without using ES6
     * class/extends syntax, by using Reflect.construct() with a newTarget argument.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a,b,c", "true", "true", "true"})
    public void constructSubclassBuiltinMap() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function CustomMap() {\n"
                + "  return Reflect.construct(Map, arguments, this.constructor);\n"
                + "}\n"
                + "CustomMap.prototype = Object.create(Map.prototype);\n"
                + "CustomMap.prototype.constructor = CustomMap;\n"
                + "var map = new CustomMap([['a', 1], ['b', 2]]);\n"
                + "map.set('c', 3);\n"
                + "log(Array.from(map.keys()));\n"
                + "log(map instanceof CustomMap);\n"
                + "log(map instanceof Map);\n"
                + "log(Object.getPrototypeOf(map) === CustomMap.prototype);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test the same workaround for Array, which additionally propagates the subclass through
     * derived methods (map/filter/slice/...) via Symbol.species, and has exotic "length" and
     * isArray behavior that Set/Map don't.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1,2,3", "true", "true", "true", "false", "true"})
    public void constructSubclassBuiltinArray() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function CustomArray() {\n"
                + "  return Reflect.construct(Array, arguments, this.constructor);\n"
                + "}\n"
                + "CustomArray.prototype = Object.create(Array.prototype);\n"
                + "CustomArray.prototype.constructor = CustomArray;\n"
                + "var arr = new CustomArray(1, 2, 3);\n"
                + "var mapped = arr.map(function(x) { return x * 2; });\n"
                + "log(Array.from(arr));\n"
                + "log(arr instanceof CustomArray);\n"
                + "log(arr instanceof Array);\n"
                + "log(Array.isArray(arr));\n"
                + "log(mapped instanceof CustomArray);\n"
                + "log(Object.getPrototypeOf(arr) === CustomArray.prototype);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test the workaround for WeakSet, which has no Symbol.iterator/Array.from support, so
     * membership must be verified via has() instead of reading contents out.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true", "true"})
    public void constructSubclassBuiltinWeakSet() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function CustomWeakSet() {\n"
                + "  return Reflect.construct(WeakSet, arguments, this.constructor);\n"
                + "}\n"
                + "CustomWeakSet.prototype = Object.create(WeakSet.prototype);\n"
                + "CustomWeakSet.prototype.constructor = CustomWeakSet;\n"
                + "var key = {};\n"
                + "var ws = new CustomWeakSet([key]);\n"
                + "ws.add({});\n"
                + "log(ws.has(key));\n"
                + "log(ws instanceof CustomWeakSet);\n"
                + "log(ws instanceof WeakSet);\n"
                + "log(Object.getPrototypeOf(ws) === CustomWeakSet.prototype);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test the workaround for a typed array (Uint8Array), which is species-driven like Array
     * (slice/subarray return instances via Symbol.species) and has an overloaded constructor
     * signature (length vs buffer vs iterable) that Set/Map don't have.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1,2,3", "true", "false", "true", "true"})
    public void constructSubclassBuiltinTypedArray() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function CustomUint8Array() {\n"
                + "  return Reflect.construct(Uint8Array, arguments, this.constructor);\n"
                + "}\n"
                + "CustomUint8Array.prototype = Object.create(Uint8Array.prototype);\n"
                + "CustomUint8Array.prototype.constructor = CustomUint8Array;\n"
                + "var ta = new CustomUint8Array([1, 2, 3]);\n"
                + "var sliced = ta.slice(1);\n"
                + "log(Array.from(ta));\n"
                + "log(Object.getPrototypeOf(ta) === CustomUint8Array.prototype);\n"
                + "log(Object.getPrototypeOf(ta) === Uint8Array.prototype);\n"
                + "log(Object.getPrototypeOf(sliced) === Uint8Array.prototype);\n"
                + "log(Object.getPrototypeOf(sliced) !== CustomUint8Array.prototype);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test the workaround applied to Proxy, which is expected to diverge from Set/Map/Array: a
     * Proxy without a getPrototypeOf trap forwards [[GetPrototypeOf]] to its target rather than to
     * whatever prototype Reflect.construct's newTarget would otherwise assign, since ProxyCreate
     * ignores newTarget entirely. So the proxy's actual prototype must be Object.prototype
     * (inherited from its target, a plain object), and must NOT be CustomProxy.prototype, even
     * though construction otherwise looks identical to the Set/Map/Array cases.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void constructSubclassBuiltinProxy() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function CustomProxy() {\n"
                + "  return Reflect.construct(Proxy, arguments, this.constructor);\n"
                + "}\n"
                + "CustomProxy.prototype = Object.create(Proxy.prototype || Object.prototype);\n"
                + "CustomProxy.prototype.constructor = CustomProxy;\n"
                + "var target = {};\n"
                + "var p = new CustomProxy(target, {});\n"
                + "log(Object.getPrototypeOf(p) === Object.prototype);\n"
                + "log(Object.getPrototypeOf(p) === Object.getPrototypeOf(target));\n"
                + "log(Object.getPrototypeOf(p) !== CustomProxy.prototype);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test the workaround for Error, included as a contrast case: unlike Set/Map/Array, naive
     * ES5-style inheritance (Error.call(this, message)) already gets most behavior "for free"
     * without Reflect.construct, since Error does not hold hidden internal slots the way the
     * collection/typed-array types do. This test documents that the Reflect.construct approach
     * still works, and still correctly wires up the prototype chain and instanceof checks.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"boom", "true", "true", "true"})
    public void constructSubclassBuiltinError() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html></head>\n"
                + "<body>"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function CustomError() {\n"
                + "  var err = Reflect.construct(Error, arguments, this.constructor);\n"
                + "  return err;\n"
                + "}\n"
                + "CustomError.prototype = Object.create(Error.prototype);\n"
                + "CustomError.prototype.constructor = CustomError;\n"
                + "var err = new CustomError('boom');\n"
                + "log(err.message);\n"
                + "log(err instanceof CustomError);\n"
                + "log(err instanceof Error);\n"
                + "log(Object.getPrototypeOf(err) === CustomError.prototype);\n"
                + "</script>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
