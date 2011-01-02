/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
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

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for compatibility with version 1.6.1 of
 * <a href="http://http://www.prototypejs.org/">Prototype JavaScript library</a>.
 *
 * @version $Revision$
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Prototype161Test extends PrototypeTestBase {

    /**
     * @throws Exception if test fails
     * For IE: 2 assertions pass whereas they shouldn't in testResponders
     */
    @Test
    @NotYetImplemented
    public void ajax() throws Exception {
        test("ajax.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void array() throws Exception {
        test("array.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void base() throws Exception {
        test("base.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void clazz() throws Exception {
        test("class.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void date() throws Exception {
        test("date.html");
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
        test("dom.html");
    }

    /**
     * Depends on {@link com.gargoylesoftware.htmlunit.javascript.SimpleScriptableTest#parentProtoFeature()}.
     *
     * @throws Exception if test fails
     */
    @Test
    public void elementMixins() throws Exception {
        test("element_mixins.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void enumerable() throws Exception {
        test("enumerable.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void event() throws Exception {
        test("event.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    public void form() throws Exception {
        test("form.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void function() throws Exception {
        test("function.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void hash() throws Exception {
        test("hash.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void number() throws Exception {
        test("number.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void periodicalExecuter() throws Exception {
        test("periodical_executer.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void position() throws Exception {
        test("position.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void prototype() throws Exception {
        test("prototype.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void range() throws Exception {
        test("range.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void regexp() throws Exception {
        test("regexp.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void selector() throws Exception {
        test("selector.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void string() throws Exception {
        test("string.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void object() throws Exception {
        test("object.html");
    }

    /**
     * Depends on {@link com.gargoylesoftware.htmlunit.javascript.regexp.HtmlUnitRegExpProxyTest#test()}.
     * 1 expected failure is because the server port is other than 4711
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void unitTests() throws Exception {
        test("unittest.html");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getVersion() {
        return "1.6.1";
    }
}
