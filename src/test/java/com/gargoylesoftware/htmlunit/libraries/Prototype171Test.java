/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for compatibility with version 1.7.1 of
 * <a href="http://http://www.prototypejs.org/">Prototype JavaScript library</a>.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Prototype171Test extends PrototypeTestBase {

    /**
     * @throws Exception if test fails
     * For IE: 2 assertions pass whereas they shouldn't in testResponders
     */
    @Test
    @NotYetImplemented
    public void ajax() throws Exception {
        test("ajax_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void array() throws Exception {
        test("array_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void base() throws Exception {
        test("base_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void clazz() throws Exception {
        test("class_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void date() throws Exception {
        test("date_test.html");
    }

    /**
     * Note: <tt>testElementGetDimensions:</tt>, <tt>testElementGetStyle</tt>, <tt>testElementGetHeight</tt>,
     *       <tt>testElementScrollTo:</tt>, <tt>testPositionedOffset</tt>, <tt>testViewportOffset</tt>,
     *       <tt>testViewportDimensions</tt>, <tt>testViewportScrollOffsets</tt>
     *       and <tt>testElementGetWidth</tt> are expected to fail with HtmlUnit,
     *       as they need calculating width and height of all elements.
     *
     * Other tests succeed.
     *
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void dom() throws Exception {
        test("dom_test.html");
    }

    /**
     * Depends on {@link com.gargoylesoftware.htmlunit.javascript.SimpleScriptableTest#parentProtoFeature()}.
     *
     * @throws Exception if test fails
     */
    @Test
    public void elementMixins() throws Exception {
        test("element_mixins_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void enumerable() throws Exception {
        test("enumerable_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void eventHandler() throws Exception {
        test("event_handler_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void event() throws Exception {
        test("event_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE)
    public void form() throws Exception {
        test("form_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void function() throws Exception {
        test("function_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void hash() throws Exception {
        test("hash_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void layout() throws Exception {
        test("layout_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void number() throws Exception {
        test("number_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void object() throws Exception {
        test("object_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void periodicalExecuter() throws Exception {
        test("periodical_executer_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void position() throws Exception {
        test("position_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void prototype() throws Exception {
        test("prototype_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void range() throws Exception {
        test("range_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void regexp() throws Exception {
        test("regexp_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void selectorEngine() throws Exception {
        test("selector_engine_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void selector() throws Exception {
        test("selector_test.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void string() throws Exception {
        test("string_test.html");
    }

    /**
     * Depends on {@link com.gargoylesoftware.htmlunit.javascript.regexp.HtmlUnitRegExpProxyTest#test()}.
     * 1 expected failure is because the server port is other than 4711
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void unitTests() throws Exception {
        test("unittest_test.html");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getVersion() {
        return "1.7.1";
    }
}
