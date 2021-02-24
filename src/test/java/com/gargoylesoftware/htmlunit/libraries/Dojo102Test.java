/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;

/**
 * Tests for compatibility with version 1.0.2 of the <a href="http://dojotoolkit.org/">Dojo
 * JavaScript library</a>.
 *
 * @author Ahmed Ashour
 * @author Daniel Gredler
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Dojo102Test extends DojoTestBase {

    @Override
    String getVersion() {
        return "1.0.2";
    }

    @Override
    String getUrl(final String module) {
        return URL_FIRST + "util/doh/runner.html?testModule=" + module;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_base_loaderbootstrap() throws Exception {
        test("tests._base._loader.bootstrap");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_base_loaderloader() throws Exception {
        test("tests._base._loader.loader");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_base_loaderhostenv_browser() throws Exception {
        test("tests._base._loader.hostenv_browser");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_basearray() throws Exception {
        test("tests._base.array");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_baseColor() throws Exception {
        test("tests._base.Color");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_baselang() throws Exception {
        test("tests._base.lang");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_basedeclare() throws Exception {
        test("tests._base.declare");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_baseconnect() throws Exception {
        test("tests._base.connect");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_baseDeferred() throws Exception {
        test("tests._base.Deferred");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_basejson() throws Exception {
        test("tests._base.json");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_basehtml() throws Exception {
        test("tests._base.html");
        // tests._base.html_rtl
        // tests._base.html_quirks
        // tests._base.html_box
        // tests._base.html_box_quirks
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_basefx() throws Exception {
        test("tests._base.fx");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_basequery() throws Exception {
        test("tests._base.query");
        // tests._base.NodeList
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void tests_basexhr() throws Exception {
        test("tests._base.xhr");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsi18n() throws Exception {
        test("tests.i18n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsbackhash() throws Exception {
        // test("tests.back.hash");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testscldr() throws Exception {
        test("tests.cldr");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsdatautils() throws Exception {
        test("tests.data.utils");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsdataItemFileReadStore() throws Exception {
        test("tests.data.ItemFileReadStore");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsdataItemFileWriteStore() throws Exception {
        test("tests.data.ItemFileWriteStore");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsdate() throws Exception {
        test("tests.date");
        // tests.date.util
        // tests.date.math
        // tests.date.locale
        // tests.date.stamp
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testscurrency() throws Exception {
        test("tests.currency");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsAdapterRegistry() throws Exception {
        test("tests.AdapterRegistry");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsioscript() throws Exception {
        test("tests.io.script");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsioiframe() throws Exception {
        test("tests.io.iframe");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsrpc() throws Exception {
        test("tests.rpc");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsstring() throws Exception {
        test("tests.string");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsbehavior() throws Exception {
        shutDownAll();

        test("tests.behavior");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsparser() throws Exception {
        test("tests.parser");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testscolors() throws Exception {
        test("tests.colors");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void testsfx() throws Exception {
        test("tests.fx");
    }
}
