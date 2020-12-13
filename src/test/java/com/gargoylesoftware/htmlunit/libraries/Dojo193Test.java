/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
    public void dojo_tests__base_html() throws Exception {
        test("dojo/tests/_base/html");
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
    public void dojo_tests_store_jsonrest() throws Exception {
        test("dojo/tests/store/JsonRest");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void dojo_tests_data_objectstore() throws Exception {
        test("dojo/tests/data/ObjectStore");
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
    public void dojo_tests_request_handlers() throws Exception {
        test("dojo/tests/request/handlers");
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
    public void dojo_parser() throws Exception {
        test("dojo/tests/html");
    }
/*
tests.html

    778ms
►
tests.fx

    20.1s
►
tests.NodeList-fx

    5.4s
►
tests.io.script

    2s
►
tests.io.iframe

    3.3s
►
tests.back.hash

    1ms
►
tests.hash

    5ms
►
tests.rpc

    10s
►
tests.cookie

    329ms
►
tests.NodeList-traverse

    597ms
►
tests.NodeList-manipulate

    781ms
►
tests.NodeList-data

    524ms
►
tests.uacss.sniffQuirks

    187ms
►
tests.uacss.sniffStandards

    145ms
►
tests.window.viewport

    180ms
►
tests.window.viewportQuirks

    10s
►
tests.window.test_scroll

    1.1m
►
tests.touch

    488ms
►
tests._base._loader.bootstrap

    50ms
►
tests._base.loader.asyncWithDojoRequire

    10s
►
tests._base.loader.config?dojoConfig-djConfig-require

    141ms
►
tests._base.loader.config?dojoConfig-require

    121ms
►
tests._base.loader.config?dojoConfig-djConfig

    125ms
►
tests._base.loader.config?dojoConfig

    122ms
►
tests._base.loader.config?djConfig-require

    119ms
►
tests._base.loader.config?djConfig

    112ms
►
tests._base.loader.config?require

    122ms
►
tests._base.loader.config?configApi.html

    142ms
►
tests._base.loader.config?config-sniff.html

    125ms
►
tests._base.loader.config?config-sniff-djConfig.html

    142ms
►
tests._base.loader.config?config-has.html

    234ms
►
tests._base.loader.loader-declareStepsOnProvide

    172ms
►
tests._base.loader.publish-require-result

    155ms
►
tests._base.loader.no-publish-require-result

    158ms
►
tests._base.loader.top-level-module-by-paths

    203ms
►
tests._base.loader.xdomin-sync-1

    200ms
►
tests._base.loader.xdomin-sync-2

    197ms
►
tests._base.loader.xdomin-async-1

    205ms
►
tests._base.loader.xdomin-async-2

    202ms
►
tests._base.loader.requirejs-simple-sync

    220ms
►
tests._base.loader.requirejs-simple-async

    170ms
►
tests._base.loader.requirejs-config-sync

    153ms
►
tests._base.loader.requirejs-config-async

    107ms
►
tests._base.loader.requirejs-simple-nohead-sync

    174ms
►
tests._base.loader.requirejs-simple-nohead-async

    109ms
►
tests._base.loader.requirejs-simple-badbase-async

    10s
►
tests._base.loader.requirejs-circular-async

    110ms
►
tests._base.loader.requirejs-urlfetch-sync

    173ms
►
tests._base.loader.requirejs-urlfetch-async

    106ms
►
tests._base.loader.requirejs-uniques-sync

    171ms
►
tests._base.loader.requirejs-uniques-async

    109ms
►
tests._base.loader.requirejs-i18nlocaleunknown-sync

    171ms
►
tests._base.loader.requirejs-i18nlocaleunknown-async

    127ms
►
tests._base.loader.requirejs-i18n-sync

    266ms
►
tests._base.loader.requirejs-i18n-async

    127ms
►
tests._base.loader.requirejs-i18nlocale-sync

    188ms
►
tests._base.loader.requirejs-i18nlocale-async

    109ms
►
tests._base.loader.requirejs-i18nbundle-sync

    173ms
►
tests._base.loader.requirejs-i18nbundle-async

    112ms
►
tests._base.loader.requirejs-i18ncommon-sync

    172ms
►
tests._base.loader.requirejs-i18ncommon-async

    122ms
►
tests._base.loader.requirejs-i18ncommonlocale-sync

    169ms
►
tests._base.loader.requirejs-i18ncommonlocale-async

    123ms
►
tests._base.loader.requirejs-paths-sync

    170ms
►
tests._base.loader.requirejs-paths-async

    125ms
►
tests._base.loader.requirejs-relative-sync

    314ms
►
tests._base.loader.requirejs-relative-async

    123ms
►
tests._base.loader.requirejs-text-sync

    397ms
►
tests._base.loader.requirejs-text-async

    239ms
►
tests._base.loader.requirejs-textOnly-sync

    154ms
►
tests._base.loader.requirejs-textOnly-async

    109ms
►
tests._base.loader.requirejs-exports-sync

    169ms
►
tests._base.loader.requirejs-exports-async

    109ms
►
tests._base.loader.require-config

    63ms
►
tests._base.loader.mapping

    50ms
►
tests._base.array

    1ms
►
tests._base.Color

    0ms
►
tests._base.lang

    4ms
►
tests._base.declare

    9ms
►
tests._base.object

    0ms
►
tests.Deferred

    75ms
►
tests.promise.Promise

    2ms
►
tests.when

    1ms
►
tests.promise.all

    3ms
►
tests.promise.first

    4ms
►
tests.promise.tracer

    280ms
►
tests.cache

    69ms
►
tests.i18n

    251ms
►
tests.i18n.extra.sync

    452ms
►
tests.i18n.extra.async

    175ms
►
tests.cldr

    1ms
►
dojo.tests.store.Memory

    1ms
►
dojo.tests.store.DataStore

    4ms
►
dojo.tests.store.Observable

    3ms
►
dojo.tests.store.Cache

    2ms
►
tests.data.utils

    3ms
►
IFSCommonTests: dojo.data.ItemFileReadStore

    1.3s
►
IFSCommonTests: dojo.data.ItemFileWriteStore

    1.2s
►
tests.data.ItemFileWriteStore

    1.1s
►
tests.date.util

    1ms
►
tests.date.math

    1ms
►
tests.date.locale

    142ms
►
tests.date.stamp

    1ms
►
tests.on

    6ms
►
tests.on.event-focusin

    82ms
►
tests.json

    3ms
►
tests.json.circular

    0ms
►
tests.json.performance

    245ms
►
tests.aspect

    0ms
►
tests.number

    70ms
►
tests.currency

    3ms
►
tests.AdapterRegistry

    0ms
►
tests.query-lite

    3.1s
►
tests.query-css2

    3.2s
►
tests.query-css2.1

    3.3s
►
tests.query-css3

    4.6s
►
tests.query-acme

    5.1s
►
tests.query-unspecified

    5.1s
►
tests.query-lite-quirks

    2.9s
►
tests.query-css2-quirks

    2.9s
►
tests.query-css2.1-quirks

    3.3s
►
tests.query-css3-quirks

    4.9s
►
tests.query-acme-quirks

    5.3s
►
tests.query-unspecified-quirks

    5.2s
►
tests.query-xml

    145ms
►
tests.regexp

    0ms
►
tests.string

    0ms
►
tests.colors

    1ms
►
tests.DeferredList

    0ms
►
tests.Stateful

    62ms
►
tests.errors
  */
}
