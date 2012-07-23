/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
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

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Object is a native JavaScript object and therefore provided by Rhino but some tests are needed here
 * to be sure that we have the expected results (for instance EcmaScript 5 adds methods that are not
 * available in FF2 or FF3).
 *
 * @version $Revision$
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class NativeObjectTest extends WebDriverTestCase {

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "constructor: function", "create: undefined", "defineProperties: undefined", "defineProperty: undefined",
        "freeze: undefined", "getOwnPropertyDescriptor: undefined", "getOwnPropertyNames: undefined",
        "getPrototypeOf: undefined", "hasOwnProperty: function", "isExtensible: undefined", "isFrozen: undefined",
        "isPrototypeOf: function", "isSealed: undefined", "keys: undefined", "preventExtensions: undefined",
        "propertyIsEnumerable: function", "seal: undefined", "toLocaleString: function", "toString: function",
        "valueOf: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"constructor", "create", "defineProperties", "defineProperty", "freeze",
            "getOwnPropertyDescriptor", "getOwnPropertyNames", "getPrototypeOf", "hasOwnProperty", "isExtensible",
            "isFrozen", "isPrototypeOf", "isSealed", "keys", "preventExtensions", "propertyIsEnumerable", "seal",
            "toLocaleString", "toString", "valueOf"};
        final String html = NativeDateTest.createHTMLTestMethods("new Object()", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "__defineGetter__: function", "__defineSetter__: function", "__lookupGetter__: function",
            "__lookupSetter__: function" },
            IE = { "__defineGetter__: undefined", "__defineSetter__: undefined", "__lookupGetter__: undefined",
            "__lookupSetter__: undefined" })
    public void methods_different() throws Exception {
        final String[] methods = {"__defineGetter__", "__defineSetter__", "__lookupGetter__", "__lookupSetter__"};
        final String html = NativeDateTest.createHTMLTestMethods("new Object()", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "toSource: function", DEFAULT = "toSource: undefined")
    public void methods_toSource() throws Exception {
        final String[] methods = {"toSource"};
        final String html = NativeDateTest.createHTMLTestMethods("new Object()", methods);
        loadPageWithAlerts2(html);
    }
}
