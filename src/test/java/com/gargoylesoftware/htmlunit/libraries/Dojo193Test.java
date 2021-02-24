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
 * Tests for compatibility with version 1.9.3 of the <a href="http://dojotoolkit.org/">Dojo
 * JavaScript library</a>.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Dojo193Test extends DojoTestBase {

    @Override
    String getVersion() {
        return "1.9.3";
    }

    @Override
    String getUrl(final String module) {
        return URL_FIRST + "util/doh/runner.html?test=" + module;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_loader() throws Exception {
        // TODO - Timeout
        // test("dojo/tests/_base/loader");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_array() throws Exception {
        test("dojo/tests/_base/array");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_color() throws Exception {
        test("dojo/tests/_base/Color");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_lang() throws Exception {
        test("dojo/tests/_base/lang");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_declare() throws Exception {
        test("dojo/tests/_base/declare");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_connect() throws Exception {
        test("dojo/tests/_base/connect");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_deferred() throws Exception {
        test("dojo/tests/_base/Deferred");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_json() throws Exception {
        test("dojo/tests/_base/json");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_object() throws Exception {
        test("dojo/tests/_base/object");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_html() throws Exception {
        test("dojo/tests/_base/html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_domstyle() throws Exception {
        // TODO - how to call this
        // test("dojo/tests/_base/dom-style");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_fx() throws Exception {
        test("dojo/tests/_base/fx");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_query() throws Exception {
        test("dojo/tests/_base/query");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_xhr() throws Exception {
        test("dojo/tests/_base/xhr");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests__base_window() throws Exception {
        test("dojo/tests/_base/window");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_aspect() throws Exception {
        test("dojo/tests/aspect");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_back() throws Exception {
        test("dojo/tests/back");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_behavior() throws Exception {
        test("dojo/tests/behavior");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_cache() throws Exception {
        test("dojo/tests/cache");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_cldr() throws Exception {
        test("dojo/tests/cldr");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_colors() throws Exception {
        test("dojo/tests/colors");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_cookie() throws Exception {
        test("dojo/tests/cookie");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_currency() throws Exception {
        test("dojo/tests/currency");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_data() throws Exception {
        // TODO - Timeout
        // test("dojo/tests/data");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_date() throws Exception {
        test("dojo/tests/date");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_Deferred() throws Exception {
        test("dojo/tests/Deferred");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_DeferredList() throws Exception {
        test("dojo/tests/DeferredList");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_domStyle() throws Exception {
        test("dojo/tests/dom-style");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_errors() throws Exception {
        test("dojo/tests/errors");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_fx() throws Exception {
        test("dojo/tests/fx");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_hash() throws Exception {
        test("dojo/tests/hash");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_html() throws Exception {
        test("dojo/tests/html");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_i18n() throws Exception {
        test("dojo/tests/i18n");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_json() throws Exception {
        test("dojo/tests/json");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_mouse() throws Exception {
        test("dojo/tests/mouse");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_node() throws Exception {
        test("dojo/tests/node");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_number() throws Exception {
        test("dojo/tests/number");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_on() throws Exception {
        test("dojo/tests/on");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_parser() throws Exception {
        test("dojo/tests/parser");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_query() throws Exception {
        test("dojo/tests/query");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_regexp() throws Exception {
        test("dojo/tests/regexp");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_request() throws Exception {
        test("dojo/tests/request");
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_router() throws Exception {
        test("dojo/tests/router");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_stateful() throws Exception {
        test("dojo/tests/Stateful");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_string() throws Exception {
        test("dojo/tests/string");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_store() throws Exception {
        test("dojo/tests/store");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_touch() throws Exception {
        // TODO - fails with an exception
        // test("dojo/tests/touch");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_uacss() throws Exception {
        test("dojo/tests/uacss");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_when() throws Exception {
        test("dojo/tests/when");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_window() throws Exception {
        // TODO - Time
        // test("dojo/tests/window", 300);
    }
}
