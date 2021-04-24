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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for compatibility with web server loading of
 * version 3.3.1 of the <a href="http://jquery.com/">jQuery</a> JavaScript library.
 *
 * All test method inside this class are generated. Please have a look
 * at {@link com.gargoylesoftware.htmlunit.source.JQueryExtractor}.
 *
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class JQuery3x3x1Test extends JQueryTestBase {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return "3.3.1";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String readTestNumber(final String testName) throws Exception {
        final String testResults = loadExpectation("/libraries/jQuery/"
                                        + getVersion() + "/expectations/results", ".txt");
        final String[] lines = testResults.split("\n");
        for (int i = 0; i < lines.length; i++) {
            final String line = lines[i];
            final int pos = line.indexOf(testName);
            if (pos != -1 && line.indexOf("skipped") == -1) {
                final int start = line.lastIndexOf('[') + 1;
                return line.substring(start, start + 8);
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String buildUrl(final String testNumber) {
        return URL_FIRST + "jquery/test/index.html?dev&testId=" + testNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getResultDetailElementText(final WebDriver webdriver, final String testNumber) {
        final WebElement output = webdriver.findElement(By.id("qunit-test-output-" + testNumber));
        String result = output.getText();
        result = result.substring(0, result.indexOf("Rerun")).trim();
        if (result.startsWith("skipped")) {
            result = result.substring(7);
        }
        return result;
    }

    /**
     * Test {1=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ready__jQuery_isReady() throws Exception {
        runTest("ready: jQuery.isReady");
    }

    /**
     * Test {2=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    @NotYetImplemented
    public void ready__jQuery_ready() throws Exception {
        runTest("ready: jQuery ready");
    }

    /**
     * Test {3=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ready__jQuery_when_jQuery_ready_() throws Exception {
        runTest("ready: jQuery.when(jQuery.ready)");
    }

    /**
     * Test {4=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ready__Promise_resolve_jQuery_ready_() throws Exception {
        runTest("ready: Promise.resolve(jQuery.ready)");
    }

    /**
     * Test {5=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ready__Error_in_ready_callback_does_not_halt_all_future_executions__gh_1823_() throws Exception {
        runTest("ready: Error in ready callback does not halt all future executions (gh-1823)");
    }

    /**
     * Test {6=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ready__holdReady_test_needs_to_be_a_standalone_test_since_it_deals_with_DOM_ready() throws Exception {
        runTest("ready: holdReady test needs to be a standalone test since it deals with DOM ready");
    }

    /**
     * Test {7=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void basic__ajax() throws Exception {
        runTest("basic: ajax");
    }

    /**
     * Test {8=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void basic__attributes() throws Exception {
        runTest("basic: attributes");
    }

    /**
     * Test {9=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void basic__css() throws Exception {
        runTest("basic: css");
    }

    /**
     * Test {10=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void basic__show_hide() throws Exception {
        runTest("basic: show/hide");
    }

    /**
     * Test {11=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void basic__core() throws Exception {
        runTest("basic: core");
    }

    /**
     * Test {12=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void basic__data() throws Exception {
        runTest("basic: data");
    }

    /**
     * Test {13=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void basic__dimensions() throws Exception {
        runTest("basic: dimensions");
    }

    /**
     * Test {14=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void basic__event() throws Exception {
        runTest("basic: event");
    }

    /**
     * Test {15=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void basic__manipulation() throws Exception {
        runTest("basic: manipulation");
    }

    /**
     * Test {16=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented
    public void basic__offset() throws Exception {
        runTest("basic: offset");
    }

    /**
     * Test {17=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void basic__selector() throws Exception {
        runTest("basic: selector");
    }

    /**
     * Test {18=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void basic__serialize() throws Exception {
        runTest("basic: serialize");
    }

    /**
     * Test {19=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void basic__traversing() throws Exception {
        runTest("basic: traversing");
    }

    /**
     * Test {20=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void basic__wrap() throws Exception {
        runTest("basic: wrap");
    }

    /**
     * Test {21=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void core__Basic_requirements() throws Exception {
        runTest("core: Basic requirements");
    }

    /**
     * Test {22=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void core__jQuery__() throws Exception {
        runTest("core: jQuery()");
    }

    /**
     * Test {23=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__jQuery_selector__context_() throws Exception {
        runTest("core: jQuery(selector, context)");
    }

    /**
     * Test {24=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__globalEval() throws Exception {
        runTest("core: globalEval");
    }

    /**
     * Test {25=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__globalEval_with__use_strict_() throws Exception {
        runTest("core: globalEval with 'use strict'");
    }

    /**
     * Test {26=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__globalEval_execution_after_script_injection___7862_() throws Exception {
        runTest("core: globalEval execution after script injection (#7862)");
    }

    /**
     * Test {27=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void core__noConflict() throws Exception {
        runTest("core: noConflict");
    }

    /**
     * Test {28=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("13")
    public void core__trim() throws Exception {
        runTest("core: trim");
    }

    /**
     * Test {29=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("23")
    public void core__isPlainObject() throws Exception {
        runTest("core: isPlainObject");
    }

    /**
     * Test {30=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "0")
    public void core__isPlainObject_Symbol_() throws Exception {
        runTest("core: isPlainObject(Symbol)");
    }

    /**
     * Test {31=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__isPlainObject_localStorage_() throws Exception {
        runTest("core: isPlainObject(localStorage)");
    }

    /**
     * Test {32=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void core__isPlainObject_Object_assign______() throws Exception {
        runTest("core: isPlainObject(Object.assign(...))");
    }

    /**
     * Test {33=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void core__isXMLDoc___HTML() throws Exception {
        runTest("core: isXMLDoc - HTML");
    }

    /**
     * Test {34=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__XSS_via_location_hash() throws Exception {
        runTest("core: XSS via location.hash");
    }

    /**
     * Test {35=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__isXMLDoc___XML() throws Exception {
        runTest("core: isXMLDoc - XML");
    }

    /**
     * Test {36=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void core__jQuery__html__() throws Exception {
        runTest("core: jQuery('html')");
    }

    /**
     * Test {37=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("36")
    public void core__jQuery_element_with_non_alphanumeric_name_() throws Exception {
        runTest("core: jQuery(element with non-alphanumeric name)");
    }

    /**
     * Test {38=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__jQuery__massive_html__7990__() throws Exception {
        runTest("core: jQuery('massive html #7990')");
    }

    /**
     * Test {39=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__jQuery__html___context_() throws Exception {
        runTest("core: jQuery('html', context)");
    }

    /**
     * Test {40=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__jQuery_selector__xml__text_str____loaded_via_xml_document() throws Exception {
        runTest("core: jQuery(selector, xml).text(str) - loaded via xml document");
    }

    /**
     * Test {41=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__end__() throws Exception {
        runTest("core: end()");
    }

    /**
     * Test {42=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__length() throws Exception {
        runTest("core: length");
    }

    /**
     * Test {43=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__get__() throws Exception {
        runTest("core: get()");
    }

    /**
     * Test {44=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__toArray__() throws Exception {
        runTest("core: toArray()");
    }

    /**
     * Test {45=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void core__inArray__() throws Exception {
        runTest("core: inArray()");
    }

    /**
     * Test {46=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__get_Number_() throws Exception {
        runTest("core: get(Number)");
    }

    /**
     * Test {47=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__get__Number_() throws Exception {
        runTest("core: get(-Number)");
    }

    /**
     * Test {48=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__each_Function_() throws Exception {
        runTest("core: each(Function)");
    }

    /**
     * Test {49=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void core__slice__() throws Exception {
        runTest("core: slice()");
    }

    /**
     * Test {50=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void core__first___last__() throws Exception {
        runTest("core: first()/last()");
    }

    /**
     * Test {51=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__map__() throws Exception {
        runTest("core: map()");
    }

    /**
     * Test {52=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("25")
    public void core__jQuery_map() throws Exception {
        runTest("core: jQuery.map");
    }

    /**
     * Test {53=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void core__jQuery_merge__() throws Exception {
        runTest("core: jQuery.merge()");
    }

    /**
     * Test {54=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void core__jQuery_grep__() throws Exception {
        runTest("core: jQuery.grep()");
    }

    /**
     * Test {55=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void core__jQuery_grep_Array_like_() throws Exception {
        runTest("core: jQuery.grep(Array-like)");
    }

    /**
     * Test {56=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("28")
    public void core__jQuery_extend_Object__Object_() throws Exception {
        runTest("core: jQuery.extend(Object, Object)");
    }

    /**
     * Test {57=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__jQuery_extend_Object__Object__created_with__defineProperties___() throws Exception {
        runTest("core: jQuery.extend(Object, Object {created with \"defineProperties\"})");
    }

    /**
     * Test {58=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__jQuery_extend_true_____a_____o_______deep_copy_with_array__followed_by_object() throws Exception {
        runTest("core: jQuery.extend(true,{},{a:[], o:{}}); deep copy with array, followed by object");
    }

    /**
     * Test {59=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("23")
    public void core__jQuery_each_Object_Function_() throws Exception {
        runTest("core: jQuery.each(Object,Function)");
    }

    /**
     * Test {60=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__jQuery_each_map_undefined_null_Function_() throws Exception {
        runTest("core: jQuery.each/map(undefined/null,Function)");
    }

    /**
     * Test {61=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void core__JIT_compilation_does_not_interfere_with_length_retrieval__gh_2145_() throws Exception {
        runTest("core: JIT compilation does not interfere with length retrieval (gh-2145)");
    }

    /**
     * Test {62=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("15")
    @NotYetImplemented(CHROME)
    public void core__jQuery_makeArray() throws Exception {
        runTest("core: jQuery.makeArray");
    }

    /**
     * Test {63=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__jQuery_inArray() throws Exception {
        runTest("core: jQuery.inArray");
    }

    /**
     * Test {64=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__jQuery_isEmptyObject() throws Exception {
        runTest("core: jQuery.isEmptyObject");
    }

    /**
     * Test {66=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__jQuery_parseHTML__a_href_____gh_2965() throws Exception {
        runTest("core: jQuery.parseHTML(<a href>) - gh-2965");
    }

    /**
     * Test {67=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void core__jQuery_parseHTML() throws Exception {
        runTest("core: jQuery.parseHTML");
    }

    /**
     * Test {68=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void core__jQuery_parseXML() throws Exception {
        runTest("core: jQuery.parseXML");
    }

    /**
     * Test {69=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__Conditional_compilation_compatibility___13274_() throws Exception {
        runTest("core: Conditional compilation compatibility (#13274)");
    }

    /**
     * Test {70=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void core__document_ready_when_jQuery_loaded_asynchronously___13655_() throws Exception {
        runTest("core: document ready when jQuery loaded asynchronously (#13655)");
    }

    /**
     * Test {71=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__Tolerating_alias_masked_DOM_properties___14074_() throws Exception {
        runTest("core: Tolerating alias-masked DOM properties (#14074)");
    }

    /**
     * Test {72=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__Don_t_call_window_onready___14802_() throws Exception {
        runTest("core: Don't call window.onready (#14802)");
    }

    /**
     * Test {73=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__Iterability_of_jQuery_objects__gh_1693_() throws Exception {
        runTest("core: Iterability of jQuery objects (gh-1693)");
    }

    /**
     * Test {74=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__jQuery_readyException__original_() throws Exception {
        runTest("core: jQuery.readyException (original)");
    }

    /**
     * Test {75=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__jQuery_readyException__custom_() throws Exception {
        runTest("core: jQuery.readyException (custom)");
    }

    /**
     * Test {76=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - no filter");
    }

    /**
     * Test {77=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks__________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - no filter");
    }

    /**
     * Test {78=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - filter");
    }

    /**
     * Test {79=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks__________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - filter");
    }

    /**
     * Test {80=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - no filter");
    }

    /**
     * Test {81=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - no filter");
    }

    /**
     * Test {82=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - filter");
    }

    /**
     * Test {83=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - filter");
    }

    /**
     * Test {84=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - no filter");
    }

    /**
     * Test {85=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - no filter");
    }

    /**
     * Test {86=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - filter");
    }

    /**
     * Test {87=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - filter");
    }

    /**
     * Test {88=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - no filter");
    }

    /**
     * Test {89=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - no filter");
    }

    /**
     * Test {90=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - filter");
    }

    /**
     * Test {91=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - filter");
    }

    /**
     * Test {92=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - no filter");
    }

    /**
     * Test {93=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {94=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - filter");
    }

    /**
     * Test {95=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {96=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - no filter");
    }

    /**
     * Test {97=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - no filter");
    }

    /**
     * Test {98=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - filter");
    }

    /**
     * Test {99=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - filter");
    }

    /**
     * Test {100=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - no filter");
    }

    /**
     * Test {101=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {102=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - filter");
    }

    /**
     * Test {103=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - filter");
    }

    /**
     * Test {104=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - no filter");
    }

    /**
     * Test {105=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {106=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - filter");
    }

    /**
     * Test {107=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {108=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - no filter");
    }

    /**
     * Test {109=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {110=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - filter");
    }

    /**
     * Test {111=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - filter");
    }

    /**
     * Test {112=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - no filter");
    }

    /**
     * Test {113=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {114=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - filter");
    }

    /**
     * Test {115=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {116=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - no filter");
    }

    /**
     * Test {117=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {118=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - filter");
    }

    /**
     * Test {119=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {120=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void callbacks__jQuery_Callbacks__options_____options_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks( options ) - options are copied");
    }

    /**
     * Test {121=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void callbacks__jQuery_Callbacks_fireWith___arguments_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks.fireWith - arguments are copied");
    }

    /**
     * Test {122=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void callbacks__jQuery_Callbacks_remove___should_remove_all_instances() throws Exception {
        runTest("callbacks: jQuery.Callbacks.remove - should remove all instances");
    }

    /**
     * Test {123=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("13")
    public void callbacks__jQuery_Callbacks_has() throws Exception {
        runTest("callbacks: jQuery.Callbacks.has");
    }

    /**
     * Test {124=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void callbacks__jQuery_Callbacks_____adding_a_string_doesn_t_cause_a_stack_overflow() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow");
    }

    /**
     * Test {125=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void callbacks__jQuery_Callbacks_____disabled_callback_doesn_t_fire__gh_1790_() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - disabled callback doesn't fire (gh-1790)");
    }

    /**
     * Test {126=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void callbacks__jQuery_Callbacks_____list_with_memory_stays_locked__gh_3469_() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - list with memory stays locked (gh-3469)");
    }

    /**
     * Test {127=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("23")
    public void deferred__jQuery_Deferred() throws Exception {
        runTest("deferred: jQuery.Deferred");
    }

    /**
     * Test {128=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("23")
    public void deferred__jQuery_Deferred___new_operator() throws Exception {
        runTest("deferred: jQuery.Deferred - new operator");
    }

    /**
     * Test {129=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void deferred__jQuery_Deferred___chainability() throws Exception {
        runTest("deferred: jQuery.Deferred - chainability");
    }

    /**
     * Test {130=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void deferred__jQuery_Deferred_then___filtering__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (done)");
    }

    /**
     * Test {131=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    @NotYetImplemented({ CHROME, IE })
    public void deferred__jQuery_Deferred_then___filtering__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (fail)");
    }

    /**
     * Test {132=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    @NotYetImplemented(CHROME)
    public void deferred__jQuery_Deferred_catch() throws Exception {
        runTest("deferred: jQuery.Deferred.catch");
    }

    /**
     * Test {133=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void deferred___PIPE_ONLY__jQuery_Deferred_pipe___filtering__fail_() throws Exception {
        runTest("deferred: [PIPE ONLY] jQuery.Deferred.pipe - filtering (fail)");
    }

    /**
     * Test {134=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented(CHROME)
    public void deferred__jQuery_Deferred_then___filtering__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (progress)");
    }

    /**
     * Test {135=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void deferred__jQuery_Deferred_then___deferred__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (done)");
    }

    /**
     * Test {136=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void deferred__jQuery_Deferred_then___deferred__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (fail)");
    }

    /**
     * Test {137=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void deferred__jQuery_Deferred_then___deferred__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (progress)");
    }

    /**
     * Test {138=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void deferred___PIPE_ONLY__jQuery_Deferred_pipe___deferred__progress_() throws Exception {
        runTest("deferred: [PIPE ONLY] jQuery.Deferred.pipe - deferred (progress)");
    }

    /**
     * Test {139=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("11")
    public void deferred__jQuery_Deferred_then___context() throws Exception {
        runTest("deferred: jQuery.Deferred.then - context");
    }

    /**
     * Test {140=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("11")
    public void deferred___PIPE_ONLY__jQuery_Deferred_pipe___context() throws Exception {
        runTest("deferred: [PIPE ONLY] jQuery.Deferred.pipe - context");
    }

    /**
     * Test {141=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented(FF)
    public void deferred__jQuery_Deferred_then___spec_compatibility() throws Exception {
        runTest("deferred: jQuery.Deferred.then - spec compatibility");
    }

    /**
     * Test {142=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    public void deferred__jQuery_Deferred_then___IsCallable_determination__gh_3596_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - IsCallable determination (gh-3596)");
    }

    /**
     * Test {143=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented({ CHROME, FF })
    public void deferred__jQuery_Deferred_exceptionHook() throws Exception {
        runTest("deferred: jQuery.Deferred.exceptionHook");
    }

    /**
     * Test {144=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void deferred__jQuery_Deferred_exceptionHook_with_stack_hooks() throws Exception {
        runTest("deferred: jQuery.Deferred.exceptionHook with stack hooks");
    }

    /**
     * Test {145=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void deferred__jQuery_Deferred___1_x_2_x_compatibility() throws Exception {
        runTest("deferred: jQuery.Deferred - 1.x/2.x compatibility");
    }

    /**
     * Test {146=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void deferred__jQuery_Deferred_then___progress_and_thenables() throws Exception {
        runTest("deferred: jQuery.Deferred.then - progress and thenables");
    }

    /**
     * Test {147=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void deferred__jQuery_Deferred___notify_and_resolve() throws Exception {
        runTest("deferred: jQuery.Deferred - notify and resolve");
    }

    /**
     * Test {148=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void deferred__jQuery_Deferred___resolved_to_a_notifying_deferred() throws Exception {
        runTest("deferred: jQuery.Deferred - resolved to a notifying deferred");
    }

    /**
     * Test {149=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("44")
    public void deferred__jQuery_when_nonThenable____like_Promise_resolve() throws Exception {
        runTest("deferred: jQuery.when(nonThenable) - like Promise.resolve");
    }

    /**
     * Test {150=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("68")
    @NotYetImplemented({ CHROME, FF })
    public void deferred__jQuery_when_thenable____like_Promise_resolve() throws Exception {
        runTest("deferred: jQuery.when(thenable) - like Promise.resolve");
    }

    /**
     * Test {151=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("196")
    @NotYetImplemented({ CHROME, FF })
    public void deferred__jQuery_when_a__b____like_Promise_all() throws Exception {
        runTest("deferred: jQuery.when(a, b) - like Promise.all");
    }

    /**
     * Test {152=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("42")
    public void deferred__jQuery_when___always_returns_a_new_promise() throws Exception {
        runTest("deferred: jQuery.when - always returns a new promise");
    }

    /**
     * Test {153=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void deferred__jQuery_when___notify_does_not_affect_resolved() throws Exception {
        runTest("deferred: jQuery.when - notify does not affect resolved");
    }

    /**
     * Test {154=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void deferred__jQuery_when________opportunistically_synchronous() throws Exception {
        runTest("deferred: jQuery.when(...) - opportunistically synchronous");
    }

    /**
     * Test {155=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void deprecated__bind_unbind() throws Exception {
        runTest("deprecated: bind/unbind");
    }

    /**
     * Test {156=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void deprecated__delegate_undelegate() throws Exception {
        runTest("deprecated: delegate/undelegate");
    }

    /**
     * Test {157=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void deprecated__hover___mouseenter_mouseleave() throws Exception {
        runTest("deprecated: hover() mouseenter mouseleave");
    }

    /**
     * Test {158=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void deprecated__trigger___shortcuts() throws Exception {
        runTest("deprecated: trigger() shortcuts");
    }

    /**
     * Test {159=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void deprecated__Event_aliases() throws Exception {
        runTest("deprecated: Event aliases");
    }

    /**
     * Test {160=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("20")
    public void deprecated__jQuery_parseJSON() throws Exception {
        runTest("deprecated: jQuery.parseJSON");
    }

    /**
     * Test {161=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void deprecated__jQuery_isArray() throws Exception {
        runTest("deprecated: jQuery.isArray");
    }

    /**
     * Test {162=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void deprecated__jQuery_nodeName() throws Exception {
        runTest("deprecated: jQuery.nodeName");
    }

    /**
     * Test {163=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("28")
    public void deprecated__type() throws Exception {
        runTest("deprecated: type");
    }

    /**
     * Test {164=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "0")
    public void deprecated__type_for__Symbol_() throws Exception {
        runTest("deprecated: type for `Symbol`");
    }

    /**
     * Test {165=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("20")
    public void deprecated__isFunction() throws Exception {
        runTest("deprecated: isFunction");
    }

    /**
     * Test {166=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void deprecated__isFunction_cross_realm_function_() throws Exception {
        runTest("deprecated: isFunction(cross-realm function)");
    }

    /**
     * Test {167=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    @NotYetImplemented({ CHROME, FF })
    public void deprecated__isFunction_GeneratorFunction_() throws Exception {
        runTest("deprecated: isFunction(GeneratorFunction)");
    }

    /**
     * Test {168=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            IE = "0")
    @NotYetImplemented({ CHROME, FF })
    public void deprecated__isFunction_AsyncFunction_() throws Exception {
        runTest("deprecated: isFunction(AsyncFunction)");
    }

    /**
     * Test {169=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "0")
    public void deprecated__isFunction_custom___toStringTag_() throws Exception {
        runTest("deprecated: isFunction(custom @@toStringTag)");
    }

    /**
     * Test {170=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void deprecated__jQuery_isWindow() throws Exception {
        runTest("deprecated: jQuery.isWindow");
    }

    /**
     * Test {171=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void deprecated__jQuery_camelCase__() throws Exception {
        runTest("deprecated: jQuery.camelCase()");
    }

    /**
     * Test {172=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void deprecated__jQuery_now() throws Exception {
        runTest("deprecated: jQuery.now");
    }

    /**
     * Test {173=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void deprecated__jQuery_proxy() throws Exception {
        runTest("deprecated: jQuery.proxy");
    }

    /**
     * Test {174=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("43")
    public void deprecated__isNumeric() throws Exception {
        runTest("deprecated: isNumeric");
    }

    /**
     * Test {175=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "0")
    public void deprecated__isNumeric_Symbol_() throws Exception {
        runTest("deprecated: isNumeric(Symbol)");
    }

    /**
     * Test {176=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void support__body_background_is_not_lost_if_set_prior_to_loading_jQuery___9239_() throws Exception {
        runTest("support: body background is not lost if set prior to loading jQuery (#9239)");
    }

    /**
     * Test {177=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1, 1, 2")
    @NotYetImplemented
    public void support__Check_CSP__https___developer_mozilla_org_en_US_docs_Security_CSP__restrictions() throws Exception {
        runTest("support: Check CSP (https://developer.mozilla.org/en-US/docs/Security/CSP) restrictions");
    }

    /**
     * Test {178=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "15",
            FF = "1, 14, 15",
            FF78 = "1, 14, 15")
    @NotYetImplemented
    public void support__Verify_that_support_tests_resolve_as_expected_per_browser() throws Exception {
        runTest("support: Verify that support tests resolve as expected per browser");
    }

    /**
     * Test {179=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__expando() throws Exception {
        runTest("data: expando");
    }

    /**
     * Test {180=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void data__jQuery_data___removeData__expected_returns() throws Exception {
        runTest("data: jQuery.data & removeData, expected returns");
    }

    /**
     * Test {181=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void data__jQuery__data____removeData__expected_returns() throws Exception {
        runTest("data: jQuery._data & _removeData, expected returns");
    }

    /**
     * Test {182=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__jQuery_hasData_no_side_effects() throws Exception {
        runTest("data: jQuery.hasData no side effects");
    }

    /**
     * Test {183=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("25")
    public void data__jQuery_data_div_() throws Exception {
        runTest("data: jQuery.data(div)");
    }

    /**
     * Test {184=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("25")
    public void data__jQuery_data____() throws Exception {
        runTest("data: jQuery.data({})");
    }

    /**
     * Test {185=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("25")
    public void data__jQuery_data_window_() throws Exception {
        runTest("data: jQuery.data(window)");
    }

    /**
     * Test {186=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("25")
    public void data__jQuery_data_document_() throws Exception {
        runTest("data: jQuery.data(document)");
    }

    /**
     * Test {187=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("25")
    public void data__jQuery_data__embed__() throws Exception {
        runTest("data: jQuery.data(<embed>)");
    }

    /**
     * Test {188=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("25")
    public void data__jQuery_data_object_flash_() throws Exception {
        runTest("data: jQuery.data(object/flash)");
    }

    /**
     * Test {189=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void data__jQuery___data_______undefined___14101_() throws Exception {
        runTest("data: jQuery().data() === undefined (#14101)");
    }

    /**
     * Test {190=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void data___data__() throws Exception {
        runTest("data: .data()");
    }

    /**
     * Test {191=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void data__jQuery_Element__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(Element).data(String, Object).data(String)");
    }

    /**
     * Test {192=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void data__jQuery_plain_Object__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(plain Object).data(String, Object).data(String)");
    }

    /**
     * Test {193=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void data___data_object__does_not_retain_references___13815() throws Exception {
        runTest("data: .data(object) does not retain references. #13815");
    }

    /**
     * Test {194=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("46")
    public void data__data___attributes() throws Exception {
        runTest("data: data-* attributes");
    }

    /**
     * Test {195=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void data___data_Object_() throws Exception {
        runTest("data: .data(Object)");
    }

    /**
     * Test {196=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void data__jQuery_removeData() throws Exception {
        runTest("data: jQuery.removeData");
    }

    /**
     * Test {197=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void data___removeData__() throws Exception {
        runTest("data: .removeData()");
    }

    /**
     * Test {198=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__JSON_serialization___8108_() throws Exception {
        runTest("data: JSON serialization (#8108)");
    }

    /**
     * Test {199=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void data___data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: .data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {200=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void data___data_should_not_miss_preset_data___w__hyphenated_property_names() throws Exception {
        runTest("data: .data should not miss preset data-* w/ hyphenated property names");
    }

    /**
     * Test {201=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__jQuery_data_should_not_miss_data___w__hyphenated_property_names__14047() throws Exception {
        runTest("data: jQuery.data should not miss data-* w/ hyphenated property names #14047");
    }

    /**
     * Test {202=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void data___data_should_not_miss_attr___set_data___with_hyphenated_property_names() throws Exception {
        runTest("data: .data should not miss attr() set data-* with hyphenated property names");
    }

    /**
     * Test {203=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void data___data_always_sets_data_with_the_camelCased_key__gh_2257_() throws Exception {
        runTest("data: .data always sets data with the camelCased key (gh-2257)");
    }

    /**
     * Test {204=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void data___data_should_not_strip_more_than_one_hyphen_when_camelCasing__gh_2070_() throws Exception {
        runTest("data: .data should not strip more than one hyphen when camelCasing (gh-2070)");
    }

    /**
     * Test {205=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("24")
    public void data___data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: .data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {206=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("27")
    public void data___data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: .data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {207=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void data___data_supports_interoperable_removal_of_properties_SET_TWICE__13850() throws Exception {
        runTest("data: .data supports interoperable removal of properties SET TWICE #13850");
    }

    /**
     * Test {208=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void data___removeData_supports_removal_of_hyphenated_properties_via_array___12786__gh_2257_() throws Exception {
        runTest("data: .removeData supports removal of hyphenated properties via array (#12786, gh-2257)");
    }

    /**
     * Test {209=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data___removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: .removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {210=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void data___data_only_checks_element_attributes_once___8909() throws Exception {
        runTest("data: .data only checks element attributes once. #8909");
    }

    /**
     * Test {211=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__data___with_JSON_value_can_have_newlines() throws Exception {
        runTest("data: data-* with JSON value can have newlines");
    }

    /**
     * Test {212=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data___data_doesn_t_throw_when_calling_selection_is_empty___13551() throws Exception {
        runTest("data: .data doesn't throw when calling selection is empty. #13551");
    }

    /**
     * Test {213=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void data__acceptData() throws Exception {
        runTest("data: acceptData");
    }

    /**
     * Test {214=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__Check_proper_data_removal_of_non_element_descendants_nodes___8335_() throws Exception {
        runTest("data: Check proper data removal of non-element descendants nodes (#8335)");
    }

    /**
     * Test {215=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__enumerate_data_attrs_on_body___14894_() throws Exception {
        runTest("data: enumerate data attrs on body (#14894)");
    }

    /**
     * Test {216=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void data__Check_that_the_expando_is_removed_when_there_s_no_more_data() throws Exception {
        runTest("data: Check that the expando is removed when there's no more data");
    }

    /**
     * Test {217=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__Check_that_the_expando_is_removed_when_there_s_no_more_data_on_non_nodes() throws Exception {
        runTest("data: Check that the expando is removed when there's no more data on non-nodes");
    }

    /**
     * Test {218=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data___data_prop__does_not_create_expando() throws Exception {
        runTest("data: .data(prop) does not create expando");
    }

    /**
     * Test {219=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void queue__queue___with_other_types() throws Exception {
        runTest("queue: queue() with other types");
    }

    /**
     * Test {220=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue__queue_name__passes_in_the_next_item_in_the_queue_as_a_parameter() throws Exception {
        runTest("queue: queue(name) passes in the next item in the queue as a parameter");
    }

    /**
     * Test {221=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void queue__queue___passes_in_the_next_item_in_the_queue_as_a_parameter_to_fx_queues() throws Exception {
        runTest("queue: queue() passes in the next item in the queue as a parameter to fx queues");
    }

    /**
     * Test {222=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void queue__callbacks_keep_their_place_in_the_queue() throws Exception {
        runTest("queue: callbacks keep their place in the queue");
    }

    /**
     * Test {223=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void queue__jQuery_queue_should_return_array_while_manipulating_the_queue() throws Exception {
        runTest("queue: jQuery.queue should return array while manipulating the queue");
    }

    /**
     * Test {224=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue__delay__() throws Exception {
        runTest("queue: delay()");
    }

    /**
     * Test {225=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue__clearQueue_name__clears_the_queue() throws Exception {
        runTest("queue: clearQueue(name) clears the queue");
    }

    /**
     * Test {226=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void queue__clearQueue___clears_the_fx_queue() throws Exception {
        runTest("queue: clearQueue() clears the fx queue");
    }

    /**
     * Test {227=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void queue__fn_promise_____called_when_fx_queue_is_empty() throws Exception {
        runTest("queue: fn.promise() - called when fx queue is empty");
    }

    /**
     * Test {228=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void queue__fn_promise___queue______called_whenever_last_queue_function_is_dequeued() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued");
    }

    /**
     * Test {229=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented(CHROME)
    public void queue__fn_promise___queue______waits_for_animation_to_complete_before_resolving() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving");
    }

    /**
     * Test {230=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue___promise_obj_() throws Exception {
        runTest("queue: .promise(obj)");
    }

    /**
     * Test {231=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void queue__delay___can_be_stopped() throws Exception {
        runTest("queue: delay() can be stopped");
    }

    /**
     * Test {232=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue__queue_stop_hooks() throws Exception {
        runTest("queue: queue stop hooks");
    }

    /**
     * Test {233=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__jQuery_propFix_integrity_test() throws Exception {
        runTest("attributes: jQuery.propFix integrity test");
    }

    /**
     * Test {234=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("50")
    public void attributes__attr_String_() throws Exception {
        runTest("attributes: attr(String)");
    }

    /**
     * Test {235=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void attributes__attr_String__on_cloned_elements___9646() throws Exception {
        runTest("attributes: attr(String) on cloned elements, #9646");
    }

    /**
     * Test {236=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void attributes__attr_String__in_XML_Files() throws Exception {
        runTest("attributes: attr(String) in XML Files");
    }

    /**
     * Test {237=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void attributes__attr_String__Function_() throws Exception {
        runTest("attributes: attr(String, Function)");
    }

    /**
     * Test {238=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void attributes__attr_Hash_() throws Exception {
        runTest("attributes: attr(Hash)");
    }

    /**
     * Test {239=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("71")
    public void attributes__attr_String__Object_() throws Exception {
        runTest("attributes: attr(String, Object)");
    }

    /**
     * Test {240=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void attributes__attr_non_ASCII_() throws Exception {
        runTest("attributes: attr(non-ASCII)");
    }

    /**
     * Test {241=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__attr___extending_the_boolean_attrHandle() throws Exception {
        runTest("attributes: attr - extending the boolean attrHandle");
    }

    /**
     * Test {242=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void attributes__attr_String__Object____Loaded_via_XML_document() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML document");
    }

    /**
     * Test {243=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void attributes__attr_String__Object____Loaded_via_XML_fragment() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML fragment");
    }

    /**
     * Test {244=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void attributes__attr__tabindex__() throws Exception {
        runTest("attributes: attr('tabindex')");
    }

    /**
     * Test {245=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void attributes__attr__tabindex___value_() throws Exception {
        runTest("attributes: attr('tabindex', value)");
    }

    /**
     * Test {246=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void attributes__removeAttr_String_() throws Exception {
        runTest("attributes: removeAttr(String)");
    }

    /**
     * Test {247=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void attributes__removeAttr_String__in_XML() throws Exception {
        runTest("attributes: removeAttr(String) in XML");
    }

    /**
     * Test {248=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void attributes__removeAttr_Multi_String__variable_space_width_() throws Exception {
        runTest("attributes: removeAttr(Multi String, variable space width)");
    }

    /**
     * Test {249=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void attributes__removeAttr_Multi_String__non_HTML_whitespace_is_valid_in_attribute_names__gh_3003_() throws Exception {
        runTest("attributes: removeAttr(Multi String, non-HTML whitespace is valid in attribute names (gh-3003)");
    }

    /**
     * Test {250=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("17")
    public void attributes__prop_String__Object_() throws Exception {
        runTest("attributes: prop(String, Object)");
    }

    /**
     * Test {251=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void attributes__prop_String__Object__on_null_undefined() throws Exception {
        runTest("attributes: prop(String, Object) on null/undefined");
    }

    /**
     * Test {252=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("11")
    public void attributes__prop__tabindex__() throws Exception {
        runTest("attributes: prop('tabindex')");
    }

    /**
     * Test {253=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__image_prop___tabIndex___() throws Exception {
        runTest("attributes: image.prop( 'tabIndex' )");
    }

    /**
     * Test {254=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void attributes__prop__tabindex___value_() throws Exception {
        runTest("attributes: prop('tabindex', value)");
    }

    /**
     * Test {255=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void attributes__option_prop__selected___true__affects_select_selectedIndex__gh_2732_() throws Exception {
        runTest("attributes: option.prop('selected', true) affects select.selectedIndex (gh-2732)");
    }

    /**
     * Test {256=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void attributes__removeProp_String_() throws Exception {
        runTest("attributes: removeProp(String)");
    }

    /**
     * Test {257=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__val___after_modification() throws Exception {
        runTest("attributes: val() after modification");
    }

    /**
     * Test {258=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("26")
    @NotYetImplemented
    public void attributes__val__() throws Exception {
        runTest("attributes: val()");
    }

    /**
     * Test {259=[CHROME, EDGE, FF, FF78, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void attributes__val___with_non_matching_values_on_dropdown_list() throws Exception {
        runTest("attributes: val() with non-matching values on dropdown list");
    }

    /**
     * Test {260=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            EDGE = "4",
            FF = "4",
            FF78 = "4")
    public void attributes__val___respects_numbers_without_exception__Bug__9319_() throws Exception {
        runTest("attributes: val() respects numbers without exception (Bug #9319)");
    }

    /**
     * Test {260=[IE], 261=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void attributes__val_String_Number_() throws Exception {
        runTest("attributes: val(String/Number)");
    }

    /**
     * Test {261=[IE], 262=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void attributes__val_Function_() throws Exception {
        runTest("attributes: val(Function)");
    }

    /**
     * Test {262=[IE], 263=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void attributes__val_Array_of_Numbers___Bug__7123_() throws Exception {
        runTest("attributes: val(Array of Numbers) (Bug #7123)");
    }

    /**
     * Test {263=[IE], 264=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void attributes__val_Function__with_incoming_value() throws Exception {
        runTest("attributes: val(Function) with incoming value");
    }

    /**
     * Test {264=[IE], 265=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void attributes__val_select__after_form_reset____Bug__2551_() throws Exception {
        runTest("attributes: val(select) after form.reset() (Bug #2551)");
    }

    /**
     * Test {265=[IE], 266=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("37")
    public void attributes__select_val_space_characters___gh_2978_() throws Exception {
        runTest("attributes: select.val(space characters) (gh-2978)");
    }

    /**
     * Test {266=[IE], 267=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void attributes__addClass_String_() throws Exception {
        runTest("attributes: addClass(String)");
    }

    /**
     * Test {267=[IE], 268=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void attributes__addClass_Function_() throws Exception {
        runTest("attributes: addClass(Function)");
    }

    /**
     * Test {268=[IE], 269=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void attributes__addClass_Array_() throws Exception {
        runTest("attributes: addClass(Array)");
    }

    /**
     * Test {269=[IE], 270=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("52")
    public void attributes__addClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: addClass(Function) with incoming value");
    }

    /**
     * Test {270=[IE], 271=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void attributes__removeClass_String____simple() throws Exception {
        runTest("attributes: removeClass(String) - simple");
    }

    /**
     * Test {271=[IE], 272=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void attributes__removeClass_Function____simple() throws Exception {
        runTest("attributes: removeClass(Function) - simple");
    }

    /**
     * Test {272=[IE], 273=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void attributes__removeClass_Array____simple() throws Exception {
        runTest("attributes: removeClass(Array) - simple");
    }

    /**
     * Test {273=[IE], 274=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("52")
    public void attributes__removeClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: removeClass(Function) with incoming value");
    }

    /**
     * Test {274=[IE], 275=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__removeClass___removes_duplicates() throws Exception {
        runTest("attributes: removeClass() removes duplicates");
    }

    /**
     * Test {275=[IE], 276=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__removeClass_undefined__is_a_no_op() throws Exception {
        runTest("attributes: removeClass(undefined) is a no-op");
    }

    /**
     * Test {276=[IE], 277=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void attributes__toggleClass_String_boolean_undefined___boolean__() throws Exception {
        runTest("attributes: toggleClass(String|boolean|undefined[, boolean])");
    }

    /**
     * Test {277=[IE], 278=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void attributes__toggleClass_Function___boolean__() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean])");
    }

    /**
     * Test {278=[IE], 279=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void attributes__toggleClass_Array___boolean__() throws Exception {
        runTest("attributes: toggleClass(Array[, boolean])");
    }

    /**
     * Test {279=[IE], 280=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void attributes__toggleClass_Function___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean]) with incoming value");
    }

    /**
     * Test {280=[IE], 281=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("17")
    public void attributes__addClass__removeClass__hasClass() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass");
    }

    /**
     * Test {281=[IE], 282=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void attributes__addClass__removeClass__hasClass_on_many_elements() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass on many elements");
    }

    /**
     * Test {282=[IE], 283=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void attributes__addClass__removeClass__hasClass_on_many_elements___Array() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass on many elements - Array");
    }

    /**
     * Test {283=[IE], 284=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void attributes__addClass__removeClass__hasClass_on_elements_with_classes_with_non_HTML_whitespace__gh_3072__gh_3003_() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass on elements with classes with non-HTML whitespace (gh-3072, gh-3003)");
    }

    /**
     * Test {284=[IE], 285=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void attributes__contents___hasClass___returns_correct_values() throws Exception {
        runTest("attributes: contents().hasClass() returns correct values");
    }

    /**
     * Test {285=[IE], 286=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void attributes__hasClass_correctly_interprets_non_space_separators___13835_() throws Exception {
        runTest("attributes: hasClass correctly interprets non-space separators (#13835)");
    }

    /**
     * Test {286=[IE], 287=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__coords_returns_correct_values_in_IE6_IE7__see__10828() throws Exception {
        runTest("attributes: coords returns correct values in IE6/IE7, see #10828");
    }

    /**
     * Test {287=[IE], 288=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__should_not_throw_at___option__val_____14686_() throws Exception {
        runTest("attributes: should not throw at $(option).val() (#14686)");
    }

    /**
     * Test {288=[IE], 289=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__option_value_not_trimmed_when_setting_via_parent_select() throws Exception {
        runTest("attributes: option value not trimmed when setting via parent select");
    }

    /**
     * Test {289=[IE], 290=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void attributes__Insignificant_white_space_returned_for___option__val_____14858__gh_2978_() throws Exception {
        runTest("attributes: Insignificant white space returned for $(option).val() (#14858, gh-2978)");
    }

    /**
     * Test {290=[IE], 291=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void attributes__SVG_class_manipulation__gh_2199_() throws Exception {
        runTest("attributes: SVG class manipulation (gh-2199)");
    }

    /**
     * Test {291=[IE], 292=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void attributes__non_lowercase_boolean_attribute_getters_should_not_crash() throws Exception {
        runTest("attributes: non-lowercase boolean attribute getters should not crash");
    }

    /**
     * Test {292=[IE], 293=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void event__null_or_undefined_handler() throws Exception {
        runTest("event: null or undefined handler");
    }

    /**
     * Test {293=[IE], 294=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__on___with_non_null_defined_data() throws Exception {
        runTest("event: on() with non-null,defined data");
    }

    /**
     * Test {294=[IE], 295=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__Handler_changes_and__trigger___order() throws Exception {
        runTest("event: Handler changes and .trigger() order");
    }

    /**
     * Test {295=[IE], 296=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void event__on____with_data() throws Exception {
        runTest("event: on(), with data");
    }

    /**
     * Test {296=[IE], 297=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__click____with_data() throws Exception {
        runTest("event: click(), with data");
    }

    /**
     * Test {297=[IE], 298=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void event__on____with_data__trigger_with_data() throws Exception {
        runTest("event: on(), with data, trigger with data");
    }

    /**
     * Test {298=[IE], 299=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__on____multiple_events_at_once() throws Exception {
        runTest("event: on(), multiple events at once");
    }

    /**
     * Test {299=[IE], 300=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__on____five_events_at_once() throws Exception {
        runTest("event: on(), five events at once");
    }

    /**
     * Test {300=[IE], 301=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void event__on____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: on(), multiple events at once and namespaces");
    }

    /**
     * Test {301=[IE], 302=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("27")
    public void event__on____namespace_with_special_add() throws Exception {
        runTest("event: on(), namespace with special add");
    }

    /**
     * Test {302=[IE], 303=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__on____no_data() throws Exception {
        runTest("event: on(), no data");
    }

    /**
     * Test {303=[IE], 304=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void event__on_one_off_Object_() throws Exception {
        runTest("event: on/one/off(Object)");
    }

    /**
     * Test {304=[IE], 305=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void event__on_off_Object___on_off_Object__String_() throws Exception {
        runTest("event: on/off(Object), on/off(Object, String)");
    }

    /**
     * Test {305=[IE], 306=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__on_immediate_propagation() throws Exception {
        runTest("event: on immediate propagation");
    }

    /**
     * Test {306=[IE], 307=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__on_bubbling__isDefaultPrevented__stopImmediatePropagation() throws Exception {
        runTest("event: on bubbling, isDefaultPrevented, stopImmediatePropagation");
    }

    /**
     * Test {307=[IE], 308=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__triggered_events_stopPropagation___for_natively_bound_events() throws Exception {
        runTest("event: triggered events stopPropagation() for natively-bound events");
    }

    /**
     * Test {308=[IE], 309=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void event__trigger___works_with_events_that_were_previously_stopped() throws Exception {
        runTest("event: trigger() works with events that were previously stopped");
    }

    /**
     * Test {309=[IE], 310=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__on____iframes() throws Exception {
        runTest("event: on(), iframes");
    }

    /**
     * Test {310=[IE], 311=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void event__on____trigger_change_on_select() throws Exception {
        runTest("event: on(), trigger change on select");
    }

    /**
     * Test {311=[IE], 312=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void event__on____namespaced_events__cloned_events() throws Exception {
        runTest("event: on(), namespaced events, cloned events");
    }

    /**
     * Test {312=[IE], 313=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void event__on____multi_namespaced_events() throws Exception {
        runTest("event: on(), multi-namespaced events");
    }

    /**
     * Test {313=[IE], 314=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__namespace_only_event_binding_is_a_no_op() throws Exception {
        runTest("event: namespace-only event binding is a no-op");
    }

    /**
     * Test {314=[IE], 315=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__Empty_namespace_is_ignored() throws Exception {
        runTest("event: Empty namespace is ignored");
    }

    /**
     * Test {315=[IE], 316=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__on____with_same_function() throws Exception {
        runTest("event: on(), with same function");
    }

    /**
     * Test {316=[IE], 317=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__on____make_sure_order_is_maintained() throws Exception {
        runTest("event: on(), make sure order is maintained");
    }

    /**
     * Test {317=[IE], 318=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void event__on____with_different_this_object() throws Exception {
        runTest("event: on(), with different this object");
    }

    /**
     * Test {318=[IE], 319=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__on_name__false___off_name__false_() throws Exception {
        runTest("event: on(name, false), off(name, false)");
    }

    /**
     * Test {319=[IE], 320=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__on_name__selector__false___off_name__selector__false_() throws Exception {
        runTest("event: on(name, selector, false), off(name, selector, false)");
    }

    /**
     * Test {320=[IE], 321=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void event__on___trigger___off___on_plain_object() throws Exception {
        runTest("event: on()/trigger()/off() on plain object");
    }

    /**
     * Test {321=[IE], 322=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__off_type_() throws Exception {
        runTest("event: off(type)");
    }

    /**
     * Test {322=[IE], 323=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void event__off_eventObject_() throws Exception {
        runTest("event: off(eventObject)");
    }

    /**
     * Test {323=[IE], 324=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__mouseover_triggers_mouseenter() throws Exception {
        runTest("event: mouseover triggers mouseenter");
    }

    /**
     * Test {324=[IE], 325=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__pointerover_triggers_pointerenter() throws Exception {
        runTest("event: pointerover triggers pointerenter");
    }

    /**
     * Test {325=[IE], 326=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__withinElement_implemented_with_jQuery_contains__() throws Exception {
        runTest("event: withinElement implemented with jQuery.contains()");
    }

    /**
     * Test {326=[IE], 327=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__mouseenter__mouseleave_don_t_catch_exceptions() throws Exception {
        runTest("event: mouseenter, mouseleave don't catch exceptions");
    }

    /**
     * Test {327=[IE], 328=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void event__trigger___bubbling() throws Exception {
        runTest("event: trigger() bubbling");
    }

    /**
     * Test {328=[IE], 329=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void event__trigger_type___data____fn__() throws Exception {
        runTest("event: trigger(type, [data], [fn])");
    }

    /**
     * Test {329=[IE], 330=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented
    public void event__submit_event_bubbles_on_copied_forms___11649_() throws Exception {
        runTest("event: submit event bubbles on copied forms (#11649)");
    }

    /**
     * Test {330=[IE], 331=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented
    public void event__change_event_bubbles_on_copied_forms___11796_() throws Exception {
        runTest("event: change event bubbles on copied forms (#11796)");
    }

    /**
     * Test {331=[IE], 332=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("28")
    public void event__trigger_eventObject___data____fn__() throws Exception {
        runTest("event: trigger(eventObject, [data], [fn])");
    }

    /**
     * Test {332=[IE], 333=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event___trigger___bubbling_on_disconnected_elements___10489_() throws Exception {
        runTest("event: .trigger() bubbling on disconnected elements (#10489)");
    }

    /**
     * Test {333=[IE], 334=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event___trigger___doesn_t_bubble_load_event___10717_() throws Exception {
        runTest("event: .trigger() doesn't bubble load event (#10717)");
    }

    /**
     * Test {334=[IE], 335=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__Delegated_events_in_SVG___10791___13180_() throws Exception {
        runTest("event: Delegated events in SVG (#10791; #13180)");
    }

    /**
     * Test {335=[IE], 336=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__Delegated_events_with_malformed_selectors__gh_3071_() throws Exception {
        runTest("event: Delegated events with malformed selectors (gh-3071)");
    }

    /**
     * Test {336=[IE], 337=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void event__Delegated_events_in_forms___10844___11145___8165___11382___11764_() throws Exception {
        runTest("event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764)");
    }

    /**
     * Test {337=[IE], 338=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__Submit_event_can_be_stopped___11049_() throws Exception {
        runTest("event: Submit event can be stopped (#11049)");
    }

    /**
     * Test {338=[IE], 339=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void event__on_beforeunload_() throws Exception {
        runTest("event: on(beforeunload)");
    }

    /**
     * Test {339=[IE], 340=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void event__jQuery_Event__type__props__() throws Exception {
        runTest("event: jQuery.Event( type, props )");
    }

    /**
     * Test {340=[IE], 341=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void event__jQuery_Event_properties() throws Exception {
        runTest("event: jQuery.Event properties");
    }

    /**
     * Test {341=[IE], 342=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("65")
    public void event___on____off__() throws Exception {
        runTest("event: .on()/.off()");
    }

    /**
     * Test {342=[IE], 343=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__jQuery_off_using_dispatched_jQuery_Event() throws Exception {
        runTest("event: jQuery.off using dispatched jQuery.Event");
    }

    /**
     * Test {343=[IE], 344=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__delegated_event_with_delegateTarget_relative_selector() throws Exception {
        runTest("event: delegated event with delegateTarget-relative selector");
    }

    /**
     * Test {344=[IE], 345=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__delegated_event_with_selector_matching_Object_prototype_property___13203_() throws Exception {
        runTest("event: delegated event with selector matching Object.prototype property (#13203)");
    }

    /**
     * Test {345=[IE], 346=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__delegated_event_with_intermediate_DOM_manipulation___13208_() throws Exception {
        runTest("event: delegated event with intermediate DOM manipulation (#13208)");
    }

    /**
     * Test {346=[IE], 347=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void event__ignore_comment_nodes_in_event_delegation__gh_2055_() throws Exception {
        runTest("event: ignore comment nodes in event delegation (gh-2055)");
    }

    /**
     * Test {347=[IE], 348=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__stopPropagation___stops_directly_bound_events_on_delegated_target() throws Exception {
        runTest("event: stopPropagation() stops directly-bound events on delegated target");
    }

    /**
     * Test {348=[IE], 349=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__off_all_bound_delegated_events() throws Exception {
        runTest("event: off all bound delegated events");
    }

    /**
     * Test {349=[IE], 350=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__on_with_multiple_delegated_events() throws Exception {
        runTest("event: on with multiple delegated events");
    }

    /**
     * Test {350=[IE], 351=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void event__delegated_on_with_change() throws Exception {
        runTest("event: delegated on with change");
    }

    /**
     * Test {351=[IE], 352=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__delegated_on_with_submit() throws Exception {
        runTest("event: delegated on with submit");
    }

    /**
     * Test {352=[IE], 353=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__delegated_off___with_only_namespaces() throws Exception {
        runTest("event: delegated off() with only namespaces");
    }

    /**
     * Test {353=[IE], 354=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__Non_DOM_element_events() throws Exception {
        runTest("event: Non DOM element events");
    }

    /**
     * Test {354=[IE], 355=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__inline_handler_returning_false_stops_default() throws Exception {
        runTest("event: inline handler returning false stops default");
    }

    /**
     * Test {355=[IE], 356=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__window_resize() throws Exception {
        runTest("event: window resize");
    }

    /**
     * Test {356=[IE], 357=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__focusin_bubbles() throws Exception {
        runTest("event: focusin bubbles");
    }

    /**
     * Test {357=[IE], 358=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__custom_events_with_colons___3533___8272_() throws Exception {
        runTest("event: custom events with colons (#3533, #8272)");
    }

    /**
     * Test {358=[IE], 359=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void event___on_and__off() throws Exception {
        runTest("event: .on and .off");
    }

    /**
     * Test {359=[IE], 360=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void event__special_on_name_mapping() throws Exception {
        runTest("event: special on name mapping");
    }

    /**
     * Test {360=[IE], 361=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void event___on_and__off__selective_mixed_removal___10705_() throws Exception {
        runTest("event: .on and .off, selective mixed removal (#10705)");
    }

    /**
     * Test {361=[IE], 362=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event___on__event_map__null_selector__data____11130() throws Exception {
        runTest("event: .on( event-map, null-selector, data ) #11130");
    }

    /**
     * Test {362=[IE], 363=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__clone___delegated_events___11076_() throws Exception {
        runTest("event: clone() delegated events (#11076)");
    }

    /**
     * Test {363=[IE], 364=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void event__checkbox_state___3827_() throws Exception {
        runTest("event: checkbox state (#3827)");
    }

    /**
     * Test {364=[IE], 365=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__event_object_properties_on_natively_triggered_event() throws Exception {
        runTest("event: event object properties on natively-triggered event");
    }

    /**
     * Test {365=[IE], 366=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__addProp_extensions() throws Exception {
        runTest("event: addProp extensions");
    }

    /**
     * Test {366=[IE], 367=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void event__drag_drop_events_copy_mouse_related_event_properties__gh_1925__gh_2009_() throws Exception {
        runTest("event: drag/drop events copy mouse-related event properties (gh-1925, gh-2009)");
    }

    /**
     * Test {367=[IE], 368=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__focusin_using_non_element_targets() throws Exception {
        runTest("event: focusin using non-element targets");
    }

    /**
     * Test {368=[IE], 369=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__focusin_from_an_iframe() throws Exception {
        runTest("event: focusin from an iframe");
    }

    /**
     * Test {369=[IE], 370=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__jQuery_ready_promise() throws Exception {
        runTest("event: jQuery.ready promise");
    }

    /**
     * Test {370=[IE], 371=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void event__jQuery_ready_uses_interactive() throws Exception {
        runTest("event: jQuery.ready uses interactive");
    }

    /**
     * Test {371=[IE], 372=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__Focusing_iframe_element() throws Exception {
        runTest("event: Focusing iframe element");
    }

    /**
     * Test {372=[IE], 373=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__triggerHandler_onbeforeunload_() throws Exception {
        runTest("event: triggerHandler(onbeforeunload)");
    }

    /**
     * Test {373=[IE], 374=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void event__jQuery_ready_synchronous_load_with_long_loading_subresources() throws Exception {
        runTest("event: jQuery.ready synchronous load with long loading subresources");
    }

    /**
     * Test {374=[IE], 375=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__change_handler_should_be_detached_from_element() throws Exception {
        runTest("event: change handler should be detached from element");
    }

    /**
     * Test {375=[IE], 376=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__trigger_click_on_checkbox__fires_change_event() throws Exception {
        runTest("event: trigger click on checkbox, fires change event");
    }

    /**
     * Test {376=[IE], 377=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void event__Namespace_preserved_when_passed_an_Event___12739_() throws Exception {
        runTest("event: Namespace preserved when passed an Event (#12739)");
    }

    /**
     * Test {377=[IE], 378=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void event__make_sure_events_cloned_correctly() throws Exception {
        runTest("event: make sure events cloned correctly");
    }

    /**
     * Test {378=[IE], 379=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__String_prototype_namespace_does_not_cause_trigger___to_throw___13360_() throws Exception {
        runTest("event: String.prototype.namespace does not cause trigger() to throw (#13360)");
    }

    /**
     * Test {379=[IE], 380=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__Inline_event_result_is_returned___13993_() throws Exception {
        runTest("event: Inline event result is returned (#13993)");
    }

    /**
     * Test {380=[IE], 381=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event___off___removes_the_expando_when_there_s_no_more_data() throws Exception {
        runTest("event: .off() removes the expando when there's no more data");
    }

    /**
     * Test {381=[IE], 382=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__jQuery_Event__src___does_not_require_a_target_property() throws Exception {
        runTest("event: jQuery.Event( src ) does not require a target property");
    }

    /**
     * Test {382=[IE], 383=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__preventDefault___on_focusin_does_not_throw_exception() throws Exception {
        runTest("event: preventDefault() on focusin does not throw exception");
    }

    /**
     * Test {383=[IE], 384=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void event__Donor_event_interference() throws Exception {
        runTest("event: Donor event interference");
    }

    /**
     * Test {384=[IE], 385=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__simulated_events_shouldn_t_forward_stopPropagation_preventDefault_methods() throws Exception {
        runTest("event: simulated events shouldn't forward stopPropagation/preventDefault methods");
    }

    /**
     * Test {385=[IE], 386=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__originalEvent_type_of_simulated_event() throws Exception {
        runTest("event: originalEvent type of simulated event");
    }

    /**
     * Test {386=[IE], 387=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__trigger__click___on_radio_passes_extra_params() throws Exception {
        runTest("event: trigger('click') on radio passes extra params");
    }

    /**
     * Test {387=[IE], 388=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__VML_with_special_event_handlers__trac_7071_() throws Exception {
        runTest("event: VML with special event handlers (trac-7071)");
    }

    /**
     * Test {388=[IE], 389=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__Check_order_of_focusin_focusout_events() throws Exception {
        runTest("event: Check order of focusin/focusout events");
    }

    /**
     * Test {389=[IE], 390=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void event__focus_blur_order___12868_() throws Exception {
        runTest("event: focus-blur order (#12868)");
    }

    /**
     * Test {390=[IE], 391=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void selector__element() throws Exception {
        runTest("selector: element");
    }

    /**
     * Test {391=[IE], 392=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("26")
    public void selector__id() throws Exception {
        runTest("selector: id");
    }

    /**
     * Test {392=[IE], 393=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void selector__class() throws Exception {
        runTest("selector: class");
    }

    /**
     * Test {393=[IE], 394=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void selector__name() throws Exception {
        runTest("selector: name");
    }

    /**
     * Test {394=[IE], 395=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void selector__selectors_with_comma() throws Exception {
        runTest("selector: selectors with comma");
    }

    /**
     * Test {395=[IE], 396=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("27")
    public void selector__child_and_adjacent() throws Exception {
        runTest("selector: child and adjacent");
    }

    /**
     * Test {396=[IE], 397=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("54")
    public void selector__attributes() throws Exception {
        runTest("selector: attributes");
    }

    /**
     * Test {398=[IE], 399=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented
    public void selector__disconnected_nodes() throws Exception {
        runTest("selector: disconnected nodes");
    }

    /**
     * Test {399=[IE], 400=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("38")
    public void selector__attributes___jQuery_attr() throws Exception {
        runTest("selector: attributes - jQuery.attr");
    }

    /**
     * Test {400=[IE], 401=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void selector__jQuery_contains() throws Exception {
        runTest("selector: jQuery.contains");
    }

    /**
     * Test {401=[IE], 402=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("15")
    @NotYetImplemented
    public void selector__jQuery_uniqueSort() throws Exception {
        runTest("selector: jQuery.uniqueSort");
    }

    /**
     * Test {402=[IE], 403=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void selector__Sizzle_cache_collides_with_multiple_Sizzles_on_a_page() throws Exception {
        runTest("selector: Sizzle cache collides with multiple Sizzles on a page");
    }

    /**
     * Test {403=[IE], 404=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void selector__Iframe_dispatch_should_not_affect_jQuery___13936_() throws Exception {
        runTest("selector: Iframe dispatch should not affect jQuery (#13936)");
    }

    /**
     * Test {404=[IE], 405=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void selector__Ensure_escapeSelector_exists__escape_tests_in_Sizzle_() throws Exception {
        runTest("selector: Ensure escapeSelector exists (escape tests in Sizzle)");
    }

    /**
     * Test {405=[IE], 406=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__find_String_() throws Exception {
        runTest("traversing: find(String)");
    }

    /**
     * Test {406=[IE], 407=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__find_String__under_non_elements() throws Exception {
        runTest("traversing: find(String) under non-elements");
    }

    /**
     * Test {407=[IE], 408=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void traversing__find_leading_combinator_() throws Exception {
        runTest("traversing: find(leading combinator)");
    }

    /**
     * Test {408=[IE], 409=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("13")
    public void traversing__find_node_jQuery_object_() throws Exception {
        runTest("traversing: find(node|jQuery object)");
    }

    /**
     * Test {409=[IE], 410=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("23")
    public void traversing__is_String_undefined_() throws Exception {
        runTest("traversing: is(String|undefined)");
    }

    /**
     * Test {410=[IE], 411=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void traversing__is___against_non_elements___10178_() throws Exception {
        runTest("traversing: is() against non-elements (#10178)");
    }

    /**
     * Test {411=[IE], 412=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void traversing__is_jQuery_() throws Exception {
        runTest("traversing: is(jQuery)");
    }

    /**
     * Test {412=[IE], 413=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void traversing__is___with__has___selectors() throws Exception {
        runTest("traversing: is() with :has() selectors");
    }

    /**
     * Test {413=[IE], 414=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("27")
    public void traversing__is___with_positional_selectors() throws Exception {
        runTest("traversing: is() with positional selectors");
    }

    /**
     * Test {414=[IE], 415=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__index__() throws Exception {
        runTest("traversing: index()");
    }

    /**
     * Test {415=[IE], 416=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void traversing__index_Object_String_undefined_() throws Exception {
        runTest("traversing: index(Object|String|undefined)");
    }

    /**
     * Test {416=[IE], 417=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void traversing__filter_Selector_undefined_() throws Exception {
        runTest("traversing: filter(Selector|undefined)");
    }

    /**
     * Test {417=[IE], 418=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__filter_Function_() throws Exception {
        runTest("traversing: filter(Function)");
    }

    /**
     * Test {418=[IE], 419=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__filter_Element_() throws Exception {
        runTest("traversing: filter(Element)");
    }

    /**
     * Test {419=[IE], 420=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__filter_Array_() throws Exception {
        runTest("traversing: filter(Array)");
    }

    /**
     * Test {420=[IE], 421=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__filter_jQuery_() throws Exception {
        runTest("traversing: filter(jQuery)");
    }

    /**
     * Test {421=[IE], 422=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void traversing__filter___with_positional_selectors() throws Exception {
        runTest("traversing: filter() with positional selectors");
    }

    /**
     * Test {422=[IE], 423=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void traversing__closest__() throws Exception {
        runTest("traversing: closest()");
    }

    /**
     * Test {423=[IE], 424=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void traversing__closest___with_positional_selectors() throws Exception {
        runTest("traversing: closest() with positional selectors");
    }

    /**
     * Test {424=[IE], 425=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void traversing__closest_jQuery_() throws Exception {
        runTest("traversing: closest(jQuery)");
    }

    /**
     * Test {425=[IE], 426=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void traversing__not_Selector_() throws Exception {
        runTest("traversing: not(Selector)");
    }

    /**
     * Test {426=[IE], 427=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void traversing__not_undefined_() throws Exception {
        runTest("traversing: not(undefined)");
    }

    /**
     * Test {427=[IE], 428=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__not_Element_() throws Exception {
        runTest("traversing: not(Element)");
    }

    /**
     * Test {428=[IE], 429=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__not_Function_() throws Exception {
        runTest("traversing: not(Function)");
    }

    /**
     * Test {429=[IE], 430=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__not_Array_() throws Exception {
        runTest("traversing: not(Array)");
    }

    /**
     * Test {430=[IE], 431=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__not_jQuery_() throws Exception {
        runTest("traversing: not(jQuery)");
    }

    /**
     * Test {431=[IE], 432=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void traversing__not_Selector__excludes_non_element_nodes__gh_2808_() throws Exception {
        runTest("traversing: not(Selector) excludes non-element nodes (gh-2808)");
    }

    /**
     * Test {432=[IE], 433=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void traversing__not_arraylike__passes_non_element_nodes__gh_3226_() throws Exception {
        runTest("traversing: not(arraylike) passes non-element nodes (gh-3226)");
    }

    /**
     * Test {433=[IE], 434=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void traversing__has_Element_() throws Exception {
        runTest("traversing: has(Element)");
    }

    /**
     * Test {434=[IE], 435=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void traversing__has_Selector_() throws Exception {
        runTest("traversing: has(Selector)");
    }

    /**
     * Test {435=[IE], 436=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void traversing__has_Arrayish_() throws Exception {
        runTest("traversing: has(Arrayish)");
    }

    /**
     * Test {436=[IE], 437=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void traversing__addBack__() throws Exception {
        runTest("traversing: addBack()");
    }

    /**
     * Test {438=[IE], 439=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void traversing__siblings__String__() throws Exception {
        runTest("traversing: siblings([String])");
    }

    /**
     * Test {440=[IE], 441=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void traversing__children__String__() throws Exception {
        runTest("traversing: children([String])");
    }

    /**
     * Test {441=[IE], 442=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void traversing__parent__String__() throws Exception {
        runTest("traversing: parent([String])");
    }

    /**
     * Test {442=[IE], 443=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void traversing__parents__String__() throws Exception {
        runTest("traversing: parents([String])");
    }

    /**
     * Test {443=[IE], 444=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void traversing__parentsUntil__String__() throws Exception {
        runTest("traversing: parentsUntil([String])");
    }

    /**
     * Test {444=[IE], 445=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void traversing__next__String__() throws Exception {
        runTest("traversing: next([String])");
    }

    /**
     * Test {445=[IE], 446=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void traversing__prev__String__() throws Exception {
        runTest("traversing: prev([String])");
    }

    /**
     * Test {446=[IE], 447=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void traversing__nextAll__String__() throws Exception {
        runTest("traversing: nextAll([String])");
    }

    /**
     * Test {447=[IE], 448=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void traversing__prevAll__String__() throws Exception {
        runTest("traversing: prevAll([String])");
    }

    /**
     * Test {448=[IE], 449=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void traversing__nextUntil__String__() throws Exception {
        runTest("traversing: nextUntil([String])");
    }

    /**
     * Test {449=[IE], 450=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("11")
    public void traversing__prevUntil__String__() throws Exception {
        runTest("traversing: prevUntil([String])");
    }

    /**
     * Test {450=[IE], 451=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void traversing__contents__() throws Exception {
        runTest("traversing: contents()");
    }

    /**
     * Test {451=[IE], 452=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void traversing__contents___for__template___() throws Exception {
        runTest("traversing: contents() for <template />");
    }

    /**
     * Test {452=[IE], 453=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "0")
    public void traversing__contents___for__template____remains_inert() throws Exception {
        runTest("traversing: contents() for <template /> remains inert");
    }

    /**
     * Test {453=[IE], 454=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void traversing__sort_direction() throws Exception {
        runTest("traversing: sort direction");
    }

    /**
     * Test {454=[IE], 455=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__add_String_selector_() throws Exception {
        runTest("traversing: add(String selector)");
    }

    /**
     * Test {455=[IE], 456=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__add_String_selector__String_context_() throws Exception {
        runTest("traversing: add(String selector, String context)");
    }

    /**
     * Test {456=[IE], 457=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void traversing__add_String_html_() throws Exception {
        runTest("traversing: add(String html)");
    }

    /**
     * Test {457=[IE], 458=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void traversing__add_jQuery_() throws Exception {
        runTest("traversing: add(jQuery)");
    }

    /**
     * Test {458=[IE], 459=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__add_Element_() throws Exception {
        runTest("traversing: add(Element)");
    }

    /**
     * Test {459=[IE], 460=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__add_Array_elements_() throws Exception {
        runTest("traversing: add(Array elements)");
    }

    /**
     * Test {460=[IE], 461=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__add_Window_() throws Exception {
        runTest("traversing: add(Window)");
    }

    /**
     * Test {461=[IE], 462=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void traversing__add_NodeList_undefined_HTMLFormElement_HTMLSelectElement_() throws Exception {
        runTest("traversing: add(NodeList|undefined|HTMLFormElement|HTMLSelectElement)");
    }

    /**
     * Test {462=[IE], 463=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void traversing__add_String__Context_() throws Exception {
        runTest("traversing: add(String, Context)");
    }

    /**
     * Test {463=[IE], 464=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void traversing__eq___1____10616() throws Exception {
        runTest("traversing: eq('-1') #10616");
    }

    /**
     * Test {464=[IE], 465=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__index_no_arg___10977() throws Exception {
        runTest("traversing: index(no arg) #10977");
    }

    /**
     * Test {465=[IE], 466=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void traversing__traversing_non_elements_with_attribute_filters___12523_() throws Exception {
        runTest("traversing: traversing non-elements with attribute filters (#12523)");
    }

    /**
     * Test {466=[IE], 467=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void manipulation__text__() throws Exception {
        runTest("manipulation: text()");
    }

    /**
     * Test {467=[IE], 468=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__text_undefined_() throws Exception {
        runTest("manipulation: text(undefined)");
    }

    /**
     * Test {468=[IE], 469=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void manipulation__text_String_() throws Exception {
        runTest("manipulation: text(String)");
    }

    /**
     * Test {469=[IE], 470=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void manipulation__text_Function_() throws Exception {
        runTest("manipulation: text(Function)");
    }

    /**
     * Test {470=[IE], 471=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__text_Function__with_incoming_value() throws Exception {
        runTest("manipulation: text(Function) with incoming value");
    }

    /**
     * Test {471=[IE], 472=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("78")
    public void manipulation__append_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {472=[IE], 473=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("78")
    public void manipulation__append_Function_() throws Exception {
        runTest("manipulation: append(Function)");
    }

    /**
     * Test {473=[IE], 474=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void manipulation__append_param__to_object__see__11280() throws Exception {
        runTest("manipulation: append(param) to object, see #11280");
    }

    /**
     * Test {474=[IE], 475=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void manipulation__append_Function__returns_String() throws Exception {
        runTest("manipulation: append(Function) returns String");
    }

    /**
     * Test {475=[IE], 476=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__append_Function__returns_Element() throws Exception {
        runTest("manipulation: append(Function) returns Element");
    }

    /**
     * Test {476=[IE], 477=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__append_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: append(Function) returns Array<Element>");
    }

    /**
     * Test {477=[IE], 478=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__append_Function__returns_jQuery() throws Exception {
        runTest("manipulation: append(Function) returns jQuery");
    }

    /**
     * Test {478=[IE], 479=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__append_Function__returns_Number() throws Exception {
        runTest("manipulation: append(Function) returns Number");
    }

    /**
     * Test {479=[IE], 480=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void manipulation__XML_DOM_manipulation___9960_() throws Exception {
        runTest("manipulation: XML DOM manipulation (#9960)");
    }

    /**
     * Test {480=[IE], 481=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__append_HTML5_sectioning_elements__Bug__6485_() throws Exception {
        runTest("manipulation: append HTML5 sectioning elements (Bug #6485)");
    }

    /**
     * Test {481=[IE], 482=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__HTML5_Elements_inherit_styles_from_style_rules__Bug__10501_() throws Exception {
        runTest("manipulation: HTML5 Elements inherit styles from style rules (Bug #10501)");
    }

    /**
     * Test {482=[IE], 483=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__html_String__with_HTML5__Bug__6485_() throws Exception {
        runTest("manipulation: html(String) with HTML5 (Bug #6485)");
    }

    /**
     * Test {483=[IE], 484=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("27")
    public void manipulation__html_String__tag_hyphenated_elements__Bug__1987_() throws Exception {
        runTest("manipulation: html(String) tag-hyphenated elements (Bug #1987)");
    }

    /**
     * Test {484=[IE], 485=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("240")
    @NotYetImplemented
    public void manipulation__Tag_name_processing_respects_the_HTML_Standard__gh_2005_() throws Exception {
        runTest("manipulation: Tag name processing respects the HTML Standard (gh-2005)");
    }

    /**
     * Test {485=[IE], 486=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__IE8_serialization_bug() throws Exception {
        runTest("manipulation: IE8 serialization bug");
    }

    /**
     * Test {486=[IE], 487=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__html___object_element__10324() throws Exception {
        runTest("manipulation: html() object element #10324");
    }

    /**
     * Test {487=[IE], 488=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__append_xml_() throws Exception {
        runTest("manipulation: append(xml)");
    }

    /**
     * Test {488=[IE], 489=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void manipulation__appendTo_String_() throws Exception {
        runTest("manipulation: appendTo(String)");
    }

    /**
     * Test {489=[IE], 490=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__appendTo_Element_Array_Element__() throws Exception {
        runTest("manipulation: appendTo(Element|Array<Element>)");
    }

    /**
     * Test {490=[IE], 491=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void manipulation__appendTo_jQuery_() throws Exception {
        runTest("manipulation: appendTo(jQuery)");
    }

    /**
     * Test {491=[IE], 492=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__prepend_String_() throws Exception {
        runTest("manipulation: prepend(String)");
    }

    /**
     * Test {492=[IE], 493=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__prepend_Element_() throws Exception {
        runTest("manipulation: prepend(Element)");
    }

    /**
     * Test {493=[IE], 494=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__prepend_Array_Element__() throws Exception {
        runTest("manipulation: prepend(Array<Element>)");
    }

    /**
     * Test {494=[IE], 495=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__prepend_jQuery_() throws Exception {
        runTest("manipulation: prepend(jQuery)");
    }

    /**
     * Test {495=[IE], 496=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__prepend_Array_jQuery__() throws Exception {
        runTest("manipulation: prepend(Array<jQuery>)");
    }

    /**
     * Test {496=[IE], 497=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void manipulation__prepend_Function__with_incoming_value____String() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- String");
    }

    /**
     * Test {497=[IE], 498=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__prepend_Function__with_incoming_value____Element() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Element");
    }

    /**
     * Test {498=[IE], 499=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__prepend_Function__with_incoming_value____Array_Element_() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Array<Element>");
    }

    /**
     * Test {499=[IE], 500=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__prepend_Function__with_incoming_value____jQuery() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- jQuery");
    }

    /**
     * Test {500=[IE], 501=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__prependTo_String_() throws Exception {
        runTest("manipulation: prependTo(String)");
    }

    /**
     * Test {501=[IE], 502=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__prependTo_Element_() throws Exception {
        runTest("manipulation: prependTo(Element)");
    }

    /**
     * Test {502=[IE], 503=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__prependTo_Array_Element__() throws Exception {
        runTest("manipulation: prependTo(Array<Element>)");
    }

    /**
     * Test {503=[IE], 504=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__prependTo_jQuery_() throws Exception {
        runTest("manipulation: prependTo(jQuery)");
    }

    /**
     * Test {504=[IE], 505=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__prependTo_Array_jQuery__() throws Exception {
        runTest("manipulation: prependTo(Array<jQuery>)");
    }

    /**
     * Test {505=[IE], 506=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_String_() throws Exception {
        runTest("manipulation: before(String)");
    }

    /**
     * Test {506=[IE], 507=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_Element_() throws Exception {
        runTest("manipulation: before(Element)");
    }

    /**
     * Test {507=[IE], 508=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_Array_Element__() throws Exception {
        runTest("manipulation: before(Array<Element>)");
    }

    /**
     * Test {508=[IE], 509=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_jQuery_() throws Exception {
        runTest("manipulation: before(jQuery)");
    }

    /**
     * Test {509=[IE], 510=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_Array_jQuery__() throws Exception {
        runTest("manipulation: before(Array<jQuery>)");
    }

    /**
     * Test {510=[IE], 511=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_Function_____Returns_String() throws Exception {
        runTest("manipulation: before(Function) -- Returns String");
    }

    /**
     * Test {511=[IE], 512=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_Function_____Returns_Element() throws Exception {
        runTest("manipulation: before(Function) -- Returns Element");
    }

    /**
     * Test {512=[IE], 513=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_Function_____Returns_Array_Element_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<Element>");
    }

    /**
     * Test {513=[IE], 514=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_Function_____Returns_jQuery() throws Exception {
        runTest("manipulation: before(Function) -- Returns jQuery");
    }

    /**
     * Test {514=[IE], 515=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_Function_____Returns_Array_jQuery_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<jQuery>");
    }

    /**
     * Test {515=[IE], 516=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__before_no_op_() throws Exception {
        runTest("manipulation: before(no-op)");
    }

    /**
     * Test {516=[IE], 517=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__before_and_after_w__empty_object___10812_() throws Exception {
        runTest("manipulation: before and after w/ empty object (#10812)");
    }

    /**
     * Test {517=[IE], 518=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation___before___and__after___disconnected_node() throws Exception {
        runTest("manipulation: .before() and .after() disconnected node");
    }

    /**
     * Test {519=[IE], 520=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insert_with__before___on_disconnected_node_first() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node first");
    }

    /**
     * Test {521=[IE], 522=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insert_with__before___on_disconnected_node_last() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node last");
    }

    /**
     * Test {522=[IE], 523=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insertBefore_String_() throws Exception {
        runTest("manipulation: insertBefore(String)");
    }

    /**
     * Test {523=[IE], 524=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insertBefore_Element_() throws Exception {
        runTest("manipulation: insertBefore(Element)");
    }

    /**
     * Test {524=[IE], 525=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insertBefore_Array_Element__() throws Exception {
        runTest("manipulation: insertBefore(Array<Element>)");
    }

    /**
     * Test {525=[IE], 526=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insertBefore_jQuery_() throws Exception {
        runTest("manipulation: insertBefore(jQuery)");
    }

    /**
     * Test {526=[IE], 527=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation___after_String_() throws Exception {
        runTest("manipulation: .after(String)");
    }

    /**
     * Test {527=[IE], 528=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation___after_Element_() throws Exception {
        runTest("manipulation: .after(Element)");
    }

    /**
     * Test {528=[IE], 529=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation___after_Array_Element__() throws Exception {
        runTest("manipulation: .after(Array<Element>)");
    }

    /**
     * Test {529=[IE], 530=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation___after_jQuery_() throws Exception {
        runTest("manipulation: .after(jQuery)");
    }

    /**
     * Test {530=[IE], 531=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation___after_Function__returns_String() throws Exception {
        runTest("manipulation: .after(Function) returns String");
    }

    /**
     * Test {531=[IE], 532=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation___after_Function__returns_Element() throws Exception {
        runTest("manipulation: .after(Function) returns Element");
    }

    /**
     * Test {532=[IE], 533=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation___after_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: .after(Function) returns Array<Element>");
    }

    /**
     * Test {533=[IE], 534=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation___after_Function__returns_jQuery() throws Exception {
        runTest("manipulation: .after(Function) returns jQuery");
    }

    /**
     * Test {534=[IE], 535=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation___after_disconnected_node_() throws Exception {
        runTest("manipulation: .after(disconnected node)");
    }

    /**
     * Test {535=[IE], 536=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insertAfter_String_() throws Exception {
        runTest("manipulation: insertAfter(String)");
    }

    /**
     * Test {536=[IE], 537=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insertAfter_Element_() throws Exception {
        runTest("manipulation: insertAfter(Element)");
    }

    /**
     * Test {537=[IE], 538=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insertAfter_Array_Element__() throws Exception {
        runTest("manipulation: insertAfter(Array<Element>)");
    }

    /**
     * Test {538=[IE], 539=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__insertAfter_jQuery_() throws Exception {
        runTest("manipulation: insertAfter(jQuery)");
    }

    /**
     * Test {539=[IE], 540=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void manipulation__replaceWith_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {540=[IE], 541=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("30")
    public void manipulation__replaceWith_Function_() throws Exception {
        runTest("manipulation: replaceWith(Function)");
    }

    /**
     * Test {541=[IE], 542=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__replaceWith_string__for_more_than_one_element() throws Exception {
        runTest("manipulation: replaceWith(string) for more than one element");
    }

    /**
     * Test {542=[IE], 543=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("25")
    public void manipulation__Empty_replaceWith__trac_13401__trac_13596__gh_2204_() throws Exception {
        runTest("manipulation: Empty replaceWith (trac-13401; trac-13596; gh-2204)");
    }

    /**
     * Test {543=[IE], 544=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__replaceAll_String_() throws Exception {
        runTest("manipulation: replaceAll(String)");
    }

    /**
     * Test {544=[IE], 545=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__replaceAll_Element_() throws Exception {
        runTest("manipulation: replaceAll(Element)");
    }

    /**
     * Test {545=[IE], 546=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__replaceAll_Array_Element__() throws Exception {
        runTest("manipulation: replaceAll(Array<Element>)");
    }

    /**
     * Test {546=[IE], 547=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__replaceAll_jQuery_() throws Exception {
        runTest("manipulation: replaceAll(jQuery)");
    }

    /**
     * Test {547=[IE], 548=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__jQuery_clone_____8017_() throws Exception {
        runTest("manipulation: jQuery.clone() (#8017)");
    }

    /**
     * Test {548=[IE], 549=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__append_to_multiple_elements___8070_() throws Exception {
        runTest("manipulation: append to multiple elements (#8070)");
    }

    /**
     * Test {549=[IE], 550=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void manipulation__table_manipulation() throws Exception {
        runTest("manipulation: table manipulation");
    }

    /**
     * Test {550=[IE], 551=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("45")
    public void manipulation__clone__() throws Exception {
        runTest("manipulation: clone()");
    }

    /**
     * Test {551=[IE], 552=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__clone_script_type_non_javascript____11359_() throws Exception {
        runTest("manipulation: clone(script type=non-javascript) (#11359)");
    }

    /**
     * Test {552=[IE], 553=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void manipulation__clone_form_element___Bug__3879___6655_() throws Exception {
        runTest("manipulation: clone(form element) (Bug #3879, #6655)");
    }

    /**
     * Test {553=[IE], 554=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__clone_multiple_selected_options___Bug__8129_() throws Exception {
        runTest("manipulation: clone(multiple selected options) (Bug #8129)");
    }

    /**
     * Test {554=[IE], 555=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__clone___on_XML_nodes() throws Exception {
        runTest("manipulation: clone() on XML nodes");
    }

    /**
     * Test {555=[IE], 556=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__clone___on_local_XML_nodes_with_html5_nodename() throws Exception {
        runTest("manipulation: clone() on local XML nodes with html5 nodename");
    }

    /**
     * Test {556=[IE], 557=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__html_undefined_() throws Exception {
        runTest("manipulation: html(undefined)");
    }

    /**
     * Test {557=[IE], 558=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__html___on_empty_set() throws Exception {
        runTest("manipulation: html() on empty set");
    }

    /**
     * Test {558=[IE], 559=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("40")
    public void manipulation__html_String_Number_() throws Exception {
        runTest("manipulation: html(String|Number)");
    }

    /**
     * Test {559=[IE], 560=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("40")
    public void manipulation__html_Function_() throws Exception {
        runTest("manipulation: html(Function)");
    }

    /**
     * Test {560=[IE], 561=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "4",
            IE = "0")
    @NotYetImplemented(CHROME)
    public void manipulation__html_script_type_module_() throws Exception {
        runTest("manipulation: html(script type module)");
    }

    /**
     * Test {561=[IE], 562=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void manipulation__html_Function__with_incoming_value____direct_selection() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- direct selection");
    }

    /**
     * Test {562=[IE], 563=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void manipulation__html_Function__with_incoming_value____jQuery_contents__() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- jQuery.contents()");
    }

    /**
     * Test {563=[IE], 564=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__clone___html___don_t_expose_jQuery_Sizzle_expandos___12858_() throws Exception {
        runTest("manipulation: clone()/html() don't expose jQuery/Sizzle expandos (#12858)");
    }

    /**
     * Test {564=[IE], 565=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__remove___no_filters() throws Exception {
        runTest("manipulation: remove() no filters");
    }

    /**
     * Test {565=[IE], 566=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void manipulation__remove___with_filters() throws Exception {
        runTest("manipulation: remove() with filters");
    }

    /**
     * Test {566=[IE], 567=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__remove___event_cleaning() throws Exception {
        runTest("manipulation: remove() event cleaning");
    }

    /**
     * Test {567=[IE], 568=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__remove___in_document_order__13779() throws Exception {
        runTest("manipulation: remove() in document order #13779");
    }

    /**
     * Test {568=[IE], 569=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__detach___no_filters() throws Exception {
        runTest("manipulation: detach() no filters");
    }

    /**
     * Test {569=[IE], 570=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void manipulation__detach___with_filters() throws Exception {
        runTest("manipulation: detach() with filters");
    }

    /**
     * Test {570=[IE], 571=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__detach___event_cleaning() throws Exception {
        runTest("manipulation: detach() event cleaning");
    }

    /**
     * Test {571=[IE], 572=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__empty__() throws Exception {
        runTest("manipulation: empty()");
    }

    /**
     * Test {572=[IE], 573=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void manipulation__jQuery_cleanData() throws Exception {
        runTest("manipulation: jQuery.cleanData");
    }

    /**
     * Test {573=[IE], 574=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__jQuery_cleanData_eliminates_all_private_data__gh_2127_() throws Exception {
        runTest("manipulation: jQuery.cleanData eliminates all private data (gh-2127)");
    }

    /**
     * Test {574=[IE], 575=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__jQuery_cleanData_eliminates_all_public_data() throws Exception {
        runTest("manipulation: jQuery.cleanData eliminates all public data");
    }

    /**
     * Test {575=[IE], 576=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__domManip_plain_text_caching__trac_6779_() throws Exception {
        runTest("manipulation: domManip plain-text caching (trac-6779)");
    }

    /**
     * Test {576=[IE], 577=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__domManip_executes_scripts_containing_html_comments_or_CDATA__trac_9221_() throws Exception {
        runTest("manipulation: domManip executes scripts containing html comments or CDATA (trac-9221)");
    }

    /**
     * Test {577=[IE], 578=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__domManip_tolerates_window_valued_document_0__in_IE9_10__trac_12266_() throws Exception {
        runTest("manipulation: domManip tolerates window-valued document[0] in IE9/10 (trac-12266)");
    }

    /**
     * Test {578=[IE], 579=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__domManip_executes_scripts_in_iframes_in_the_iframes__context() throws Exception {
        runTest("manipulation: domManip executes scripts in iframes in the iframes' context");
    }

    /**
     * Test {579=[IE], 580=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__jQuery_clone___no_exceptions_for_object_elements__9587() throws Exception {
        runTest("manipulation: jQuery.clone - no exceptions for object elements #9587");
    }

    /**
     * Test {580=[IE], 581=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void manipulation__Cloned__detached_HTML5_elems___10667_10670_() throws Exception {
        runTest("manipulation: Cloned, detached HTML5 elems (#10667,10670)");
    }

    /**
     * Test {581=[IE], 582=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__Guard_against_exceptions_when_clearing_safeChildNodes() throws Exception {
        runTest("manipulation: Guard against exceptions when clearing safeChildNodes");
    }

    /**
     * Test {582=[IE], 583=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void manipulation__Ensure_oldIE_creates_a_new_set_on_appendTo___8894_() throws Exception {
        runTest("manipulation: Ensure oldIE creates a new set on appendTo (#8894)");
    }

    /**
     * Test {583=[IE], 584=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void manipulation__html_____script_exceptions_bubble___11743_() throws Exception {
        runTest("manipulation: html() - script exceptions bubble (#11743)");
    }

    /**
     * Test {584=[IE], 585=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__checked_state_is_cloned_with_clone__() throws Exception {
        runTest("manipulation: checked state is cloned with clone()");
    }

    /**
     * Test {585=[IE], 586=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__manipulate_mixed_jQuery_and_text___12384___12346_() throws Exception {
        runTest("manipulation: manipulate mixed jQuery and text (#12384, #12346)");
    }

    /**
     * Test {586=[IE], 587=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("13")
    @NotYetImplemented
    public void manipulation__script_evaluation___11795_() throws Exception {
        runTest("manipulation: script evaluation (#11795)");
    }

    /**
     * Test {587=[IE], 588=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void manipulation__jQuery__evalUrl___12838_() throws Exception {
        runTest("manipulation: jQuery._evalUrl (#12838)");
    }

    /**
     * Test {588=[IE], 589=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void manipulation__jQuery_htmlPrefilter__gh_1747_() throws Exception {
        runTest("manipulation: jQuery.htmlPrefilter (gh-1747)");
    }

    /**
     * Test {589=[IE], 590=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void manipulation__insertAfter__insertBefore__etc_do_not_work_when_destination_is_original_element__Element_is_removed___4087_() throws Exception {
        runTest("manipulation: insertAfter, insertBefore, etc do not work when destination is original element. Element is removed (#4087)");
    }

    /**
     * Test {590=[IE], 591=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__Index_for_function_argument_should_be_received___13094_() throws Exception {
        runTest("manipulation: Index for function argument should be received (#13094)");
    }

    /**
     * Test {591=[IE], 592=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__Make_sure_jQuery_fn_remove_can_work_on_elements_in_documentFragment() throws Exception {
        runTest("manipulation: Make sure jQuery.fn.remove can work on elements in documentFragment");
    }

    /**
     * Test {592=[IE], 593=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("20")
    public void manipulation__Make_sure_specific_elements_with_content_created_correctly___13232_() throws Exception {
        runTest("manipulation: Make sure specific elements with content created correctly (#13232)");
    }

    /**
     * Test {593=[IE], 594=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("44")
    public void manipulation__Validate_creation_of_multiple_quantities_of_certain_elements___13818_() throws Exception {
        runTest("manipulation: Validate creation of multiple quantities of certain elements (#13818)");
    }

    /**
     * Test {594=[IE], 595=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__Make_sure_tr_element_will_be_appended_to_tbody_element_of_table_when_present() throws Exception {
        runTest("manipulation: Make sure tr element will be appended to tbody element of table when present");
    }

    /**
     * Test {595=[IE], 596=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__Make_sure_tr_elements_will_be_appended_to_tbody_element_of_table_when_present() throws Exception {
        runTest("manipulation: Make sure tr elements will be appended to tbody element of table when present");
    }

    /**
     * Test {596=[IE], 597=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__Make_sure_tfoot_element_will_not_be_appended_to_tbody_element_of_table_when_present() throws Exception {
        runTest("manipulation: Make sure tfoot element will not be appended to tbody element of table when present");
    }

    /**
     * Test {597=[IE], 598=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__Make_sure_document_fragment_will_be_appended_to_tbody_element_of_table_when_present() throws Exception {
        runTest("manipulation: Make sure document fragment will be appended to tbody element of table when present");
    }

    /**
     * Test {598=[IE], 599=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void manipulation__Make_sure_col_element_is_appended_correctly() throws Exception {
        runTest("manipulation: Make sure col element is appended correctly");
    }

    /**
     * Test {599=[IE], 600=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__Make_sure_tr_is_not_appended_to_the_wrong_tbody__gh_3439_() throws Exception {
        runTest("manipulation: Make sure tr is not appended to the wrong tbody (gh-3439)");
    }

    /**
     * Test {600=[IE], 601=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__Insert_script_with_data_URI__gh_1887_() throws Exception {
        runTest("manipulation: Insert script with data-URI (gh-1887)");
    }

    /**
     * Test {601=[IE], 602=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void wrap__wrap_String_Element_() throws Exception {
        runTest("wrap: wrap(String|Element)");
    }

    /**
     * Test {602=[IE], 603=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void wrap__wrap_Function_() throws Exception {
        runTest("wrap: wrap(Function)");
    }

    /**
     * Test {603=[IE], 604=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void wrap__wrap_Function__with_index___10177_() throws Exception {
        runTest("wrap: wrap(Function) with index (#10177)");
    }

    /**
     * Test {604=[IE], 605=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void wrap__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("wrap: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {605=[IE], 606=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void wrap__wrapAll_String_() throws Exception {
        runTest("wrap: wrapAll(String)");
    }

    /**
     * Test {606=[IE], 607=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void wrap__wrapAll_Function_() throws Exception {
        runTest("wrap: wrapAll(Function)");
    }

    /**
     * Test {607=[IE], 608=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void wrap__wrapAll_Function__check_execution_characteristics() throws Exception {
        runTest("wrap: wrapAll(Function) check execution characteristics");
    }

    /**
     * Test {608=[IE], 609=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void wrap__wrapAll_Element_() throws Exception {
        runTest("wrap: wrapAll(Element)");
    }

    /**
     * Test {609=[IE], 610=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void wrap__wrapInner_String_() throws Exception {
        runTest("wrap: wrapInner(String)");
    }

    /**
     * Test {610=[IE], 611=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void wrap__wrapInner_Element_() throws Exception {
        runTest("wrap: wrapInner(Element)");
    }

    /**
     * Test {611=[IE], 612=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void wrap__wrapInner_Function__returns_String() throws Exception {
        runTest("wrap: wrapInner(Function) returns String");
    }

    /**
     * Test {612=[IE], 613=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void wrap__wrapInner_Function__returns_Element() throws Exception {
        runTest("wrap: wrapInner(Function) returns Element");
    }

    /**
     * Test {613=[IE], 614=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void wrap__unwrap__() throws Exception {
        runTest("wrap: unwrap()");
    }

    /**
     * Test {614=[IE], 615=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void wrap__unwrap__selector__() throws Exception {
        runTest("wrap: unwrap( selector )");
    }

    /**
     * Test {615=[IE], 616=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void wrap__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("wrap: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {616=[IE], 617=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void wrap__wrapping_scripts___10470_() throws Exception {
        runTest("wrap: wrapping scripts (#10470)");
    }

    /**
     * Test {617=[IE], 618=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("42")
    @NotYetImplemented(IE)
    public void css__css_String_Hash_() throws Exception {
        runTest("css: css(String|Hash)");
    }

    /**
     * Test {618=[IE], 619=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void css__css___explicit_and_relative_values() throws Exception {
        runTest("css: css() explicit and relative values");
    }

    /**
     * Test {619=[IE], 620=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("17")
    @NotYetImplemented
    public void css__css___non_px_relative_values__gh_1711_() throws Exception {
        runTest("css: css() non-px relative values (gh-1711)");
    }

    /**
     * Test {620=[IE], 621=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void css__css___mismatched_relative_values_with_bounded_styles__gh_2144_() throws Exception {
        runTest("css: css() mismatched relative values with bounded styles (gh-2144)");
    }

    /**
     * Test {621=[IE], 622=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    @NotYetImplemented(IE)
    public void css__css_String__Object_() throws Exception {
        runTest("css: css(String, Object)");
    }

    /**
     * Test {622=[IE], 623=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void css__css_String__Object__with_negative_values() throws Exception {
        runTest("css: css(String, Object) with negative values");
    }

    /**
     * Test {623=[IE], 624=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void css__css_Array_() throws Exception {
        runTest("css: css(Array)");
    }

    /**
     * Test {624=[IE], 625=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__css_String__Function_() throws Exception {
        runTest("css: css(String, Function)");
    }

    /**
     * Test {625=[IE], 626=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__css_String__Function__with_incoming_value() throws Exception {
        runTest("css: css(String, Function) with incoming value");
    }

    /**
     * Test {626=[IE], 627=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__css_Object__where_values_are_Functions() throws Exception {
        runTest("css: css(Object) where values are Functions");
    }

    /**
     * Test {627=[IE], 628=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__css_Object__where_values_are_Functions_with_incoming_values() throws Exception {
        runTest("css: css(Object) where values are Functions with incoming values");
    }

    /**
     * Test {628=[IE], 629=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void css__show__() throws Exception {
        runTest("css: show()");
    }

    /**
     * Test {629=[IE], 630=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void css__show_hide_detached_nodes() throws Exception {
        runTest("css: show/hide detached nodes");
    }

    /**
     * Test {630=[IE], 631=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__hide_hidden_elements__bug__7141_() throws Exception {
        runTest("css: hide hidden elements (bug #7141)");
    }

    /**
     * Test {631=[IE], 632=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__show___after_hide___should_always_set_display_to_initial_value___14750_() throws Exception {
        runTest("css: show() after hide() should always set display to initial value (#14750)");
    }

    /**
     * Test {632=[IE], 633=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("36")
    public void css__show_hide_3_0__default_display() throws Exception {
        runTest("css: show/hide 3.0, default display");
    }

    /**
     * Test {633=[IE], 634=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void css__show_hide_3_0__default_body_display() throws Exception {
        runTest("css: show/hide 3.0, default body display");
    }

    /**
     * Test {634=[IE], 635=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("36")
    public void css__show_hide_3_0__cascade_display() throws Exception {
        runTest("css: show/hide 3.0, cascade display");
    }

    /**
     * Test {635=[IE], 636=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("96")
    public void css__show_hide_3_0__inline_display() throws Exception {
        runTest("css: show/hide 3.0, inline display");
    }

    /**
     * Test {636=[IE], 637=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("72")
    public void css__show_hide_3_0__cascade_hidden() throws Exception {
        runTest("css: show/hide 3.0, cascade hidden");
    }

    /**
     * Test {637=[IE], 638=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("84")
    public void css__show_hide_3_0__inline_hidden() throws Exception {
        runTest("css: show/hide 3.0, inline hidden");
    }

    /**
     * Test {638=[IE], 639=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void css__toggle__() throws Exception {
        runTest("css: toggle()");
    }

    /**
     * Test {639=[IE], 640=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void css__detached_toggle__() throws Exception {
        runTest("css: detached toggle()");
    }

    /**
     * Test {640=[IE], 641=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void css__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {641=[IE], 642=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__internal_ref_to_elem_runtimeStyle__bug__7608_() throws Exception {
        runTest("css: internal ref to elem.runtimeStyle (bug #7608)");
    }

    /**
     * Test {642=[IE], 643=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void css__computed_margins__trac_3333__gh_2237_() throws Exception {
        runTest("css: computed margins (trac-3333; gh-2237)");
    }

    /**
     * Test {643=[IE], 644=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void css__box_model_properties_incorrectly_returning___instead_of_px__see__10639_and__12088() throws Exception {
        runTest("css: box model properties incorrectly returning % instead of px, see #10639 and #12088");
    }

    /**
     * Test {644=[IE], 645=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void css__jQuery_cssProps_behavior___bug__8402_() throws Exception {
        runTest("css: jQuery.cssProps behavior, (bug #8402)");
    }

    /**
     * Test {645=[IE], 646=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void css__widows___orphans__8936() throws Exception {
        runTest("css: widows & orphans #8936");
    }

    /**
     * Test {646=[IE], 647=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void css__can_t_get_css_for_disconnected_in_IE_9__see__10254_and__8388() throws Exception {
        runTest("css: can't get css for disconnected in IE<9, see #10254 and #8388");
    }

    /**
     * Test {647=[IE], 648=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__Ensure_styles_are_retrieving_from_parsed_html_on_document_fragments() throws Exception {
        runTest("css: Ensure styles are retrieving from parsed html on document fragments");
    }

    /**
     * Test {648=[IE], 649=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void css__can_t_get_background_position_in_IE_9__see__10796() throws Exception {
        runTest("css: can't get background-position in IE<9, see #10796");
    }

    /**
     * Test {649=[IE], 650=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void css__percentage_properties_for_left_and_top_should_be_transformed_to_pixels__see__9505() throws Exception {
        runTest("css: percentage properties for left and top should be transformed to pixels, see #9505");
    }

    /**
     * Test {650=[IE], 651=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__Do_not_append_px___9548___12990___2792_() throws Exception {
        runTest("css: Do not append px (#9548, #12990, #2792)");
    }

    /**
     * Test {651=[IE], 652=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void css__css__width___and_css__height___should_respect_box_sizing__see__11004() throws Exception {
        runTest("css: css('width') and css('height') should respect box-sizing, see #11004");
    }

    /**
     * Test {652=[IE], 653=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__css__width___should_work_correctly_before_document_ready___14084_() throws Exception {
        runTest("css: css('width') should work correctly before document ready (#14084)");
    }

    /**
     * Test {653=[IE], 654=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__css__width___should_work_correctly_with_browser_zooming() throws Exception {
        runTest("css: css('width') should work correctly with browser zooming");
    }

    /**
     * Test {654=[IE], 655=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void css__css__width___and_css__height___should_return_fractional_values_for_nodes_in_the_document() throws Exception {
        runTest("css: css('width') and css('height') should return fractional values for nodes in the document");
    }

    /**
     * Test {655=[IE], 656=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void css__css__width___and_css__height___should_return_fractional_values_for_disconnected_nodes() throws Exception {
        runTest("css: css('width') and css('height') should return fractional values for disconnected nodes");
    }

    /**
     * Test {656=[IE], 657=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__certain_css_values_of__normal__should_be_convertable_to_a_number__see__8627() throws Exception {
        runTest("css: certain css values of 'normal' should be convertable to a number, see #8627");
    }

    /**
     * Test {657=[IE], 658=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("15")
    public void css__cssHooks___expand() throws Exception {
        runTest("css: cssHooks - expand");
    }

    /**
     * Test {658=[IE], 659=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__css_opacity_consistency_across_browsers___12685_() throws Exception {
        runTest("css: css opacity consistency across browsers (#12685)");
    }

    /**
     * Test {659=[IE], 660=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("17")
    @NotYetImplemented
    public void css___visible__hidden_selectors() throws Exception {
        runTest("css: :visible/:hidden selectors");
    }

    /**
     * Test {660=[IE], 661=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__Keep_the_last_style_if_the_new_one_isn_t_recognized_by_the_browser___14836_() throws Exception {
        runTest("css: Keep the last style if the new one isn't recognized by the browser (#14836)");
    }

    /**
     * Test {661=[IE], 662=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__Keep_the_last_style_if_the_new_one_is_a_non_empty_whitespace__gh_3204_() throws Exception {
        runTest("css: Keep the last style if the new one is a non-empty whitespace (gh-3204)");
    }

    /**
     * Test {662=[IE], 663=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__Reset_the_style_if_set_to_an_empty_string() throws Exception {
        runTest("css: Reset the style if set to an empty string");
    }

    /**
     * Test {663=[IE], 664=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("24")
    public void css__Clearing_a_Cloned_Element_s_Style_Shouldn_t_Clear_the_Original_Element_s_Style___8908_() throws Exception {
        runTest("css: Clearing a Cloned Element's Style Shouldn't Clear the Original Element's Style (#8908)");
    }

    /**
     * Test {664=[IE], 665=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__Don_t_append_px_to_CSS__order__value___14049_() throws Exception {
        runTest("css: Don't append px to CSS \"order\" value (#14049)");
    }

    /**
     * Test {665=[IE], 666=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__Do_not_throw_on_frame_elements_from_css_method___15098_() throws Exception {
        runTest("css: Do not throw on frame elements from css method (#15098)");
    }

    /**
     * Test {666=[IE], 667=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "3",
            IE = "2")
    @NotYetImplemented(CHROME)
    public void css__Don_t_default_to_a_cached_previously_used_wrong_prefixed_name__gh_2015_() throws Exception {
        runTest("css: Don't default to a cached previously used wrong prefixed name (gh-2015)");
    }

    /**
     * Test {667=[IE], 668=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__Don_t_detect_fake_set_properties_on_a_node_when_caching_the_prefixed_version() throws Exception {
        runTest("css: Don't detect fake set properties on a node when caching the prefixed version");
    }

    /**
     * Test {668=[IE], 669=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "8",
            EDGE = "8",
            FF = "10",
            FF78 = "10",
            IE = "0")
    @NotYetImplemented
    public void css__css___customProperty_() throws Exception {
        runTest("css: css(--customProperty)");
    }

    /**
     * Test {669=[IE], 670=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("23")
    public void serialize__jQuery_param__() throws Exception {
        runTest("serialize: jQuery.param()");
    }

    /**
     * Test {670=[IE], 671=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void serialize__jQuery_param___not_affected_by_ajaxSettings() throws Exception {
        runTest("serialize: jQuery.param() not affected by ajaxSettings");
    }

    /**
     * Test {671=[IE], 672=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void serialize__jQuery_param___Constructed_prop_values() throws Exception {
        runTest("serialize: jQuery.param() Constructed prop values");
    }

    /**
     * Test {672=[IE], 673=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void serialize__serialize__() throws Exception {
        runTest("serialize: serialize()");
    }

    /**
     * Test {673=[IE], 674=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__Unit_Testing_Environment() throws Exception {
        runTest("ajax: Unit Testing Environment");
    }

    /**
     * Test {674=[IE], 675=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void ajax__XMLHttpRequest___Attempt_to_block_tests_because_of_dangling_XHR_requests__IE_() throws Exception {
        runTest("ajax: XMLHttpRequest - Attempt to block tests because of dangling XHR requests (IE)");
    }

    /**
     * Test {675=[IE], 676=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void ajax__jQuery_ajax_____success_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks");
    }

    /**
     * Test {676=[IE], 677=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void ajax__jQuery_ajax_____success_callbacks____url__options__syntax() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks - (url, options) syntax");
    }

    /**
     * Test {677=[IE], 678=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____execute_js_for_crossOrigin_when_dataType_option_is_provided() throws Exception {
        runTest("ajax: jQuery.ajax() - execute js for crossOrigin when dataType option is provided");
    }

    /**
     * Test {678=[IE], 679=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____do_not_execute_js__crossOrigin_() throws Exception {
        runTest("ajax: jQuery.ajax() - do not execute js (crossOrigin)");
    }

    /**
     * Test {679=[IE], 680=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void ajax__jQuery_ajax_____success_callbacks__late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (late binding)");
    }

    /**
     * Test {680=[IE], 681=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void ajax__jQuery_ajax_____success_callbacks__oncomplete_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (oncomplete binding)");
    }

    /**
     * Test {681=[IE], 682=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void ajax__jQuery_ajax_____error_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - error callbacks");
    }

    /**
     * Test {682=[IE], 683=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax__jQuery_ajax_____textStatus_and_errorThrown_values() throws Exception {
        runTest("ajax: jQuery.ajax() - textStatus and errorThrown values");
    }

    /**
     * Test {683=[IE], 684=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____responseText_on_error() throws Exception {
        runTest("ajax: jQuery.ajax() - responseText on error");
    }

    /**
     * Test {684=[IE], 685=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: jQuery.ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {685=[IE], 686=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void ajax__jQuery_ajax_____headers() throws Exception {
        runTest("ajax: jQuery.ajax() - headers");
    }

    /**
     * Test {686=[IE], 687=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____Accept_header() throws Exception {
        runTest("ajax: jQuery.ajax() - Accept header");
    }

    /**
     * Test {687=[IE], 688=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____contentType() throws Exception {
        runTest("ajax: jQuery.ajax() - contentType");
    }

    /**
     * Test {688=[IE], 689=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: jQuery.ajax() - protocol-less urls");
    }

    /**
     * Test {689=[IE], 690=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax__jQuery_ajax_____hash() throws Exception {
        runTest("ajax: jQuery.ajax() - hash");
    }

    /**
     * Test {690=[IE], 691=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax__jQuery_ajax_____traditional_param_encoding() throws Exception {
        runTest("ajax: jQuery.ajax() - traditional param encoding");
    }

    /**
     * Test {691=[IE], 692=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void ajax__jQuery_ajax_____cross_domain_detection() throws Exception {
        runTest("ajax: jQuery.ajax() - cross-domain detection");
    }

    /**
     * Test {692=[IE], 693=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void ajax__jQuery_ajax_____abort() throws Exception {
        runTest("ajax: jQuery.ajax() - abort");
    }

    /**
     * Test {693=[IE], 694=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____native_abort() throws Exception {
        runTest("ajax: jQuery.ajax() - native abort");
    }

    /**
     * Test {694=[IE], 695=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____native_timeout() throws Exception {
        runTest("ajax: jQuery.ajax() - native timeout");
    }

    /**
     * Test {695=[IE], 696=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void ajax__jQuery_ajax_____events_with_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events with context");
    }

    /**
     * Test {696=[IE], 697=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____events_without_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events without context");
    }

    /**
     * Test {697=[IE], 698=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___15118___jQuery_ajax_____function_without_jQuery_event() throws Exception {
        runTest("ajax: #15118 - jQuery.ajax() - function without jQuery.event");
    }

    /**
     * Test {698=[IE], 699=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax___15160___jQuery_ajax_____request_manually_aborted_in_ajaxSend() throws Exception {
        runTest("ajax: #15160 - jQuery.ajax() - request manually aborted in ajaxSend");
    }

    /**
     * Test {699=[IE], 700=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____context_modification() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification");
    }

    /**
     * Test {700=[IE], 701=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification through ajaxSetup");
    }

    /**
     * Test {701=[IE], 702=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____disabled_globals() throws Exception {
        runTest("ajax: jQuery.ajax() - disabled globals");
    }

    /**
     * Test {702=[IE], 703=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {703=[IE], 704=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {704=[IE], 705=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - HEAD requests");
    }

    /**
     * Test {705=[IE], 706=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend");
    }

    /**
     * Test {706=[IE], 707=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend, cancel request manually");
    }

    /**
     * Test {707=[IE], 708=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void ajax__jQuery_ajax_____dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax() - dataType html");
    }

    /**
     * Test {708=[IE], 709=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____synchronous_request() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request");
    }

    /**
     * Test {709=[IE], 710=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request with callbacks");
    }

    /**
     * Test {710=[IE], 711=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void ajax__jQuery_ajax____jQuery_get_Script_JSON_____jQuery_post____pass_through_request_object() throws Exception {
        runTest("ajax: jQuery.ajax(), jQuery.get[Script|JSON](), jQuery.post(), pass-through request object");
    }

    /**
     * Test {711=[IE], 712=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("28")
    public void ajax__jQuery_ajax_____cache() throws Exception {
        runTest("ajax: jQuery.ajax() - cache");
    }

    /**
     * Test {712=[IE], 713=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5, 2, 7")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Same Domain");
    }

    /**
     * Test {713=[IE], 714=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Same Domain");
    }

    /**
     * Test {714=[IE], 715=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Same Domain");
    }

    /**
     * Test {715=[IE], 716=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____JSONP___POST___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Same Domain");
    }

    /**
     * Test {716=[IE], 717=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____JSONP___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Same Domain");
    }

    /**
     * Test {717=[IE], 718=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2, 2, 4")
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Cross Domain");
    }

    /**
     * Test {718=[IE], 719=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Cross Domain");
    }

    /**
     * Test {719=[IE], 720=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Cross Domain");
    }

    /**
     * Test {720=[IE], 721=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____JSONP___POST___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Cross Domain");
    }

    /**
     * Test {721=[IE], 722=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____JSONP___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Cross Domain");
    }

    /**
     * Test {722=[IE], 723=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____script__Remote() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote");
    }

    /**
     * Test {723=[IE], 724=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____script__Remote_with_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with POST");
    }

    /**
     * Test {724=[IE], 725=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____script__Remote_with_scheme_less_URL() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with scheme-less URL");
    }

    /**
     * Test {725=[IE], 726=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____malformed_JSON() throws Exception {
        runTest("ajax: jQuery.ajax() - malformed JSON");
    }

    /**
     * Test {726=[IE], 727=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____script_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - script by content-type");
    }

    /**
     * Test {727=[IE], 728=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void ajax__jQuery_ajax_____JSON_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type");
    }

    /**
     * Test {728=[IE], 729=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void ajax__jQuery_ajax_____JSON_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type disabled with options");
    }

    /**
     * Test {729=[IE], 730=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____simple_get() throws Exception {
        runTest("ajax: jQuery.ajax() - simple get");
    }

    /**
     * Test {730=[IE], 731=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____simple_post() throws Exception {
        runTest("ajax: jQuery.ajax() - simple post");
    }

    /**
     * Test {731=[IE], 732=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____data_option___empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - data option - empty bodies for non-GET requests");
    }

    /**
     * Test {732=[IE], 733=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____data___x_www_form_urlencoded__gh_2658_() throws Exception {
        runTest("ajax: jQuery.ajax() - data - x-www-form-urlencoded (gh-2658)");
    }

    /**
     * Test {733=[IE], 734=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____data___text_plain__gh_2658_() throws Exception {
        runTest("ajax: jQuery.ajax() - data - text/plain (gh-2658)");
    }

    /**
     * Test {734=[IE], 735=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____data___no_processing_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - data - no processing POST");
    }

    /**
     * Test {735=[IE], 736=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____data___no_processing_GET() throws Exception {
        runTest("ajax: jQuery.ajax() - data - no processing GET");
    }

    /**
     * Test {736=[IE], 737=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____data___process_string_with_GET() throws Exception {
        runTest("ajax: jQuery.ajax() - data - process string with GET");
    }

    /**
     * Test {737=[IE], 738=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (cache)");
    }

    /**
     * Test {738=[IE], 739=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax__jQuery_ajax_____Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (cache)");
    }

    /**
     * Test {739=[IE], 740=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (no cache)");
    }

    /**
     * Test {740=[IE], 741=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax__jQuery_ajax_____Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (no cache)");
    }

    /**
     * Test {741=[IE], 742=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____failing_cross_domain__non_existing_() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain (non-existing)");
    }

    /**
     * Test {742=[IE], 743=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____failing_cross_domain() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain");
    }

    /**
     * Test {743=[IE], 744=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____atom_xml() throws Exception {
        runTest("ajax: jQuery.ajax() - atom+xml");
    }

    /**
     * Test {744=[IE], 745=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_ajax_____statusText() throws Exception {
        runTest("ajax: jQuery.ajax() - statusText");
    }

    /**
     * Test {745=[IE], 746=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("20")
    public void ajax__jQuery_ajax_____statusCode() throws Exception {
        runTest("ajax: jQuery.ajax() - statusCode");
    }

    /**
     * Test {746=[IE], 747=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void ajax__jQuery_ajax_____transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax() - transitive conversions");
    }

    /**
     * Test {747=[IE], 748=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax() - overrideMimeType");
    }

    /**
     * Test {748=[IE], 749=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajax_____empty_json_gets_to_error_callback_instead_of_success_callback_() throws Exception {
        runTest("ajax: jQuery.ajax() - empty json gets to error callback instead of success callback.");
    }

    /**
     * Test {749=[IE], 750=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax___2688___jQuery_ajax_____beforeSend__cancel_request() throws Exception {
        runTest("ajax: #2688 - jQuery.ajax() - beforeSend, cancel request");
    }

    /**
     * Test {750=[IE], 751=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___2806___jQuery_ajax_____data_option___evaluate_function_values() throws Exception {
        runTest("ajax: #2806 - jQuery.ajax() - data option - evaluate function values");
    }

    /**
     * Test {751=[IE], 752=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___7531___jQuery_ajax_____Location_object_as_url() throws Exception {
        runTest("ajax: #7531 - jQuery.ajax() - Location object as url");
    }

    /**
     * Test {752=[IE], 753=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Same_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Same Domain");
    }

    /**
     * Test {753=[IE], 754=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Cross_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Cross Domain");
    }

    /**
     * Test {754=[IE], 755=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax___8107___jQuery_ajax_____multiple_method_signatures_introduced_in_1_5() throws Exception {
        runTest("ajax: #8107 - jQuery.ajax() - multiple method signatures introduced in 1.5");
    }

    /**
     * Test {755=[IE], 756=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Same_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Same Domain");
    }

    /**
     * Test {756=[IE], 757=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Cross_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Cross Domain");
    }

    /**
     * Test {757=[IE], 758=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax___9887___jQuery_ajax_____Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: #9887 - jQuery.ajax() - Context with circular references (#9887)");
    }

    /**
     * Test {758=[IE], 759=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax___10093___jQuery_ajax_____falsy_url_as_argument() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url as argument");
    }

    /**
     * Test {759=[IE], 760=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax___10093___jQuery_ajax_____falsy_url_in_settings_object() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url in settings object");
    }

    /**
     * Test {760=[IE], 761=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax___11151___jQuery_ajax_____parse_error_body() throws Exception {
        runTest("ajax: #11151 - jQuery.ajax() - parse error body");
    }

    /**
     * Test {761=[IE], 762=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___11426___jQuery_ajax_____loading_binary_data_shouldn_t_throw_an_exception_in_IE() throws Exception {
        runTest("ajax: #11426 - jQuery.ajax() - loading binary data shouldn't throw an exception in IE");
    }

    /**
     * Test {762=[IE], 763=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void ajax__gh_2498___jQuery_ajax_____binary_data_shouldn_t_throw_an_exception() throws Exception {
        runTest("ajax: gh-2498 - jQuery.ajax() - binary data shouldn't throw an exception");
    }

    /**
     * Test {763=[IE], 764=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void ajax___11743___jQuery_ajax_____script__throws_exception() throws Exception {
        runTest("ajax: #11743 - jQuery.ajax() - script, throws exception");
    }

    /**
     * Test {764=[IE], 765=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___method_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - method set globally");
    }

    /**
     * Test {765=[IE], 766=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___type_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - type set globally");
    }

    /**
     * Test {766=[IE], 767=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___13276___jQuery_ajax_____compatibility_between_XML_documents_from_ajax_requests_and_parsed_string() throws Exception {
        runTest("ajax: #13276 - jQuery.ajax() - compatibility between XML documents from ajax requests and parsed string");
    }

    /**
     * Test {767=[IE], 768=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax___13292___jQuery_ajax_____converter_is_bypassed_for_204_requests() throws Exception {
        runTest("ajax: #13292 - jQuery.ajax() - converter is bypassed for 204 requests");
    }

    /**
     * Test {768=[IE], 769=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax___13388___jQuery_ajax_____responseXML() throws Exception {
        runTest("ajax: #13388 - jQuery.ajax() - responseXML");
    }

    /**
     * Test {769=[IE], 770=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax___13922___jQuery_ajax_____converter_is_bypassed_for_HEAD_requests() throws Exception {
        runTest("ajax: #13922 - jQuery.ajax() - converter is bypassed for HEAD requests");
    }

    /**
     * Test {770=[IE], 771=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = "1, 0, 1",
            EDGE = "1, 0, 1")
    public void ajax___14379___jQuery_ajax___on_unload() throws Exception {
        runTest("ajax: #14379 - jQuery.ajax() on unload");
    }

    /**
     * Test {771=[IE], 772=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax___14683___jQuery_ajax_____Exceptions_thrown_synchronously_by_xhr_send_should_be_caught() throws Exception {
        runTest("ajax: #14683 - jQuery.ajax() - Exceptions thrown synchronously by xhr.send should be caught");
    }

    /**
     * Test {773=[IE], 774=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__gh_2587___when_content_type_not_xml__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not xml, but looks like one");
    }

    /**
     * Test {774=[IE], 775=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__gh_2587___when_content_type_not_json__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not json, but looks like one");
    }

    /**
     * Test {775=[IE], 776=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__gh_2587___when_content_type_not_html__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not html, but looks like one");
    }

    /**
     * Test {776=[IE], 777=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__gh_2587___when_content_type_not_javascript__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not javascript, but looks like one");
    }

    /**
     * Test {777=[IE], 778=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__gh_2587___when_content_type_not_ecmascript__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not ecmascript, but looks like one");
    }

    /**
     * Test {778=[IE], 779=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajaxPrefilter_____abort() throws Exception {
        runTest("ajax: jQuery.ajaxPrefilter() - abort");
    }

    /**
     * Test {779=[IE], 780=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajaxSetup__() throws Exception {
        runTest("ajax: jQuery.ajaxSetup()");
    }

    /**
     * Test {780=[IE], 781=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void ajax__jQuery_ajaxSetup___timeout__Number______with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) - with global timeout");
    }

    /**
     * Test {781=[IE], 782=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_ajaxSetup___timeout__Number____with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) with localtimeout");
    }

    /**
     * Test {782=[IE], 783=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void ajax___11264___jQuery_domManip_____no_side_effect_because_of_ajaxSetup_or_global_events() throws Exception {
        runTest("ajax: #11264 - jQuery.domManip() - no side effect because of ajaxSetup or global events");
    }

    /**
     * Test {783=[IE], 784=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_load_____always_use_GET_method_even_if_it_overrided_through_ajaxSetup___11264_() throws Exception {
        runTest("ajax: jQuery#load() - always use GET method even if it overrided through ajaxSetup (#11264)");
    }

    /**
     * Test {784=[IE], 785=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_load_____should_resolve_with_correct_context() throws Exception {
        runTest("ajax: jQuery#load() - should resolve with correct context");
    }

    /**
     * Test {785=[IE], 786=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax___11402___jQuery_domManip_____script_in_comments_are_properly_evaluated() throws Exception {
        runTest("ajax: #11402 - jQuery.domManip() - script in comments are properly evaluated");
    }

    /**
     * Test {786=[IE], 787=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_get__String__Hash__Function_____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get( String, Hash, Function ) - parse xml and use text() on nodes");
    }

    /**
     * Test {787=[IE], 788=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___8277___jQuery_get__String__Function_____data_in_ajaxSettings() throws Exception {
        runTest("ajax: #8277 - jQuery.get( String, Function ) - data in ajaxSettings");
    }

    /**
     * Test {788=[IE], 789=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void ajax__jQuery_getJSON__String__Hash__Function_____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Hash, Function ) - JSON array");
    }

    /**
     * Test {789=[IE], 790=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object");
    }

    /**
     * Test {790=[IE], 791=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object with absolute url to local content");
    }

    /**
     * Test {791=[IE], 792=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_getScript__String__Function_____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - with callback");
    }

    /**
     * Test {792=[IE], 793=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_getScript__String__Function_____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - no callback");
    }

    /**
     * Test {793=[IE], 794=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax___8082___jQuery_getScript__String__Function_____source_as_responseText() throws Exception {
        runTest("ajax: #8082 - jQuery.getScript( String, Function ) - source as responseText");
    }

    /**
     * Test {794=[IE], 795=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_getScript__Object_____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript( Object ) - with callback");
    }

    /**
     * Test {795=[IE], 796=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_getScript__Object_____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript( Object ) - no callback");
    }

    /**
     * Test {796=[IE], 797=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_fn_load__String__() throws Exception {
        runTest("ajax: jQuery.fn.load( String )");
    }

    /**
     * Test {797=[IE], 798=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void ajax__jQuery_fn_load_____404_error_callbacks() throws Exception {
        runTest("ajax: jQuery.fn.load() - 404 error callbacks");
    }

    /**
     * Test {798=[IE], 799=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_fn_load__String__null__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, null )");
    }

    /**
     * Test {799=[IE], 800=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_fn_load__String__undefined__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, undefined )");
    }

    /**
     * Test {800=[IE], 801=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_fn_load__URL_SELECTOR__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR )");
    }

    /**
     * Test {801=[IE], 802=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_fn_load__URL_SELECTOR_with_spaces__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR with spaces )");
    }

    /**
     * Test {802=[IE], 803=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_fn_load__URL_SELECTOR_with_non_HTML_whitespace__3003___() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR with non-HTML whitespace(#3003) )");
    }

    /**
     * Test {803=[IE], 804=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_fn_load__String__Function_____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - simple: inject text into DOM");
    }

    /**
     * Test {804=[IE], 805=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    @NotYetImplemented
    public void ajax__jQuery_fn_load__String__Function_____check_scripts() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check scripts");
    }

    /**
     * Test {805=[IE], 806=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_fn_load__String__Function_____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check file with only a script tag");
    }

    /**
     * Test {806=[IE], 807=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_fn_load__String__Function_____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - dataFilter in ajaxSettings");
    }

    /**
     * Test {807=[IE], 808=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_fn_load__String__Object__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Object, Function )");
    }

    /**
     * Test {808=[IE], 809=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_fn_load__String__String__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, String, Function )");
    }

    /**
     * Test {809=[IE], 810=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void ajax__jQuery_fn_load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: jQuery.fn.load() - callbacks get the correct parameters");
    }

    /**
     * Test {810=[IE], 811=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___2046___jQuery_fn_load__String__Function___with_ajaxSetup_on_dataType_json() throws Exception {
        runTest("ajax: #2046 - jQuery.fn.load( String, Function ) with ajaxSetup on dataType json");
    }

    /**
     * Test {811=[IE], 812=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax___10524___jQuery_fn_load_____data_specified_in_ajaxSettings_is_merged_in() throws Exception {
        runTest("ajax: #10524 - jQuery.fn.load() - data specified in ajaxSettings is merged in");
    }

    /**
     * Test {812=[IE], 813=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void ajax__jQuery_post_____data() throws Exception {
        runTest("ajax: jQuery.post() - data");
    }

    /**
     * Test {813=[IE], 814=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax__jQuery_post__String__Hash__Function_____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post( String, Hash, Function ) - simple with xml");
    }

    /**
     * Test {814=[IE], 815=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_get_post___options_____simple_with_xml() throws Exception {
        runTest("ajax: jQuery[get|post]( options ) - simple with xml");
    }

    /**
     * Test {815=[IE], 816=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ajax__jQuery_active() throws Exception {
        runTest("ajax: jQuery.active");
    }

    /**
     * Test {816=[IE], 817=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__sanity_check() throws Exception {
        runTest("effects: sanity check");
    }

    /**
     * Test {817=[IE], 818=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__show___basic() throws Exception {
        runTest("effects: show() basic");
    }

    /**
     * Test {818=[IE], 819=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("27")
    @NotYetImplemented({ CHROME, FF })
    public void effects__show__() throws Exception {
        runTest("effects: show()");
    }

    /**
     * Test {819=[IE], 820=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("30")
    @NotYetImplemented(IE)
    public void effects__show_Number____inline_hidden() throws Exception {
        runTest("effects: show(Number) - inline hidden");
    }

    /**
     * Test {820=[IE], 821=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("30")
    @NotYetImplemented(IE)
    public void effects__show_Number____cascade_hidden() throws Exception {
        runTest("effects: show(Number) - cascade hidden");
    }

    /**
     * Test {821=[IE], 822=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__Persist_correct_display_value___inline_hidden() throws Exception {
        runTest("effects: Persist correct display value - inline hidden");
    }

    /**
     * Test {822=[IE], 823=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__Persist_correct_display_value___cascade_hidden() throws Exception {
        runTest("effects: Persist correct display value - cascade hidden");
    }

    /**
     * Test {823=[IE], 824=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__animate_Hash__Object__Function_() throws Exception {
        runTest("effects: animate(Hash, Object, Function)");
    }

    /**
     * Test {824=[IE], 825=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    @NotYetImplemented
    public void effects__animate_relative_values() throws Exception {
        runTest("effects: animate relative values");
    }

    /**
     * Test {825=[IE], 826=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__animate_negative_height() throws Exception {
        runTest("effects: animate negative height");
    }

    /**
     * Test {826=[IE], 827=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__animate_negative_margin() throws Exception {
        runTest("effects: animate negative margin");
    }

    /**
     * Test {827=[IE], 828=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__animate_negative_margin_with_px() throws Exception {
        runTest("effects: animate negative margin with px");
    }

    /**
     * Test {828=[IE], 829=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void effects__animate_negative_padding() throws Exception {
        runTest("effects: animate negative padding");
    }

    /**
     * Test {829=[IE], 830=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__animate_block_as_inline_width_height() throws Exception {
        runTest("effects: animate block as inline width/height");
    }

    /**
     * Test {830=[IE], 831=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented(IE)
    public void effects__animate_native_inline_width_height() throws Exception {
        runTest("effects: animate native inline width/height");
    }

    /**
     * Test {831=[IE], 832=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__animate_block_width_height() throws Exception {
        runTest("effects: animate block width/height");
    }

    /**
     * Test {832=[IE], 833=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__animate_table_width_height() throws Exception {
        runTest("effects: animate table width/height");
    }

    /**
     * Test {833=[IE], 834=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented
    public void effects__animate_table_row_width_height() throws Exception {
        runTest("effects: animate table-row width/height");
    }

    /**
     * Test {834=[IE], 835=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented
    public void effects__animate_table_cell_width_height() throws Exception {
        runTest("effects: animate table-cell width/height");
    }

    /**
     * Test {835=[IE], 836=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__animate_percentage____on_width_height() throws Exception {
        runTest("effects: animate percentage(%) on width/height");
    }

    /**
     * Test {836=[IE], 837=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__animate_resets_overflow_x_and_overflow_y_when_finished() throws Exception {
        runTest("effects: animate resets overflow-x and overflow-y when finished");
    }

    /**
     * Test {837=[IE], 838=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__animate_option___queue__false__() throws Exception {
        runTest("effects: animate option { queue: false }");
    }

    /**
     * Test {838=[IE], 839=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__animate_option___queue__true__() throws Exception {
        runTest("effects: animate option { queue: true }");
    }

    /**
     * Test {839=[IE], 840=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__animate_option___queue___name___() throws Exception {
        runTest("effects: animate option { queue: 'name' }");
    }

    /**
     * Test {840=[IE], 841=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__animate_with_no_properties() throws Exception {
        runTest("effects: animate with no properties");
    }

    /**
     * Test {841=[IE], 842=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("11")
    public void effects__animate_duration_0() throws Exception {
        runTest("effects: animate duration 0");
    }

    /**
     * Test {842=[IE], 843=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__animate_hyphenated_properties() throws Exception {
        runTest("effects: animate hyphenated properties");
    }

    /**
     * Test {843=[IE], 844=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__animate_non_element() throws Exception {
        runTest("effects: animate non-element");
    }

    /**
     * Test {844=[IE], 845=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__stop__() throws Exception {
        runTest("effects: stop()");
    }

    /**
     * Test {845=[IE], 846=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__stop_____several_in_queue() throws Exception {
        runTest("effects: stop() - several in queue");
    }

    /**
     * Test {846=[IE], 847=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__stop_clearQueue_() throws Exception {
        runTest("effects: stop(clearQueue)");
    }

    /**
     * Test {847=[IE], 848=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__stop_clearQueue__gotoEnd_() throws Exception {
        runTest("effects: stop(clearQueue, gotoEnd)");
    }

    /**
     * Test {848=[IE], 849=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__stop__queue_______________Stop_single_queues() throws Exception {
        runTest("effects: stop( queue, ..., ... ) - Stop single queues");
    }

    /**
     * Test {849=[IE], 850=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__toggle__() throws Exception {
        runTest("effects: toggle()");
    }

    /**
     * Test {850=[IE], 851=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void effects__jQuery_fx_prototype_cur______1_8_Back_Compat() throws Exception {
        runTest("effects: jQuery.fx.prototype.cur() - <1.8 Back Compat");
    }

    /**
     * Test {851=[IE], 852=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__Overflow_and_Display() throws Exception {
        runTest("effects: Overflow and Display");
    }

    /**
     * Test {852=[IE], 853=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_Auto_to_0() throws Exception {
        runTest("effects: CSS Auto to 0");
    }

    /**
     * Test {853=[IE], 854=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_Auto_to_50() throws Exception {
        runTest("effects: CSS Auto to 50");
    }

    /**
     * Test {854=[IE], 855=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_Auto_to_100() throws Exception {
        runTest("effects: CSS Auto to 100");
    }

    /**
     * Test {855=[IE], 856=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    @NotYetImplemented
    public void effects__CSS_Auto_to_show() throws Exception {
        runTest("effects: CSS Auto to show");
    }

    /**
     * Test {856=[IE], 857=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__CSS_Auto_to_hide() throws Exception {
        runTest("effects: CSS Auto to hide");
    }

    /**
     * Test {857=[IE], 858=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_Auto_to_0() throws Exception {
        runTest("effects: JS Auto to 0");
    }

    /**
     * Test {858=[IE], 859=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_Auto_to_50() throws Exception {
        runTest("effects: JS Auto to 50");
    }

    /**
     * Test {859=[IE], 860=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_Auto_to_100() throws Exception {
        runTest("effects: JS Auto to 100");
    }

    /**
     * Test {860=[IE], 861=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    @NotYetImplemented
    public void effects__JS_Auto_to_show() throws Exception {
        runTest("effects: JS Auto to show");
    }

    /**
     * Test {861=[IE], 862=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__JS_Auto_to_hide() throws Exception {
        runTest("effects: JS Auto to hide");
    }

    /**
     * Test {862=[IE], 863=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_100_to_0() throws Exception {
        runTest("effects: CSS 100 to 0");
    }

    /**
     * Test {863=[IE], 864=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_100_to_50() throws Exception {
        runTest("effects: CSS 100 to 50");
    }

    /**
     * Test {864=[IE], 865=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_100_to_100() throws Exception {
        runTest("effects: CSS 100 to 100");
    }

    /**
     * Test {865=[IE], 866=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__CSS_100_to_show() throws Exception {
        runTest("effects: CSS 100 to show");
    }

    /**
     * Test {866=[IE], 867=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__CSS_100_to_hide() throws Exception {
        runTest("effects: CSS 100 to hide");
    }

    /**
     * Test {867=[IE], 868=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_100_to_0() throws Exception {
        runTest("effects: JS 100 to 0");
    }

    /**
     * Test {868=[IE], 869=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_100_to_50() throws Exception {
        runTest("effects: JS 100 to 50");
    }

    /**
     * Test {869=[IE], 870=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_100_to_100() throws Exception {
        runTest("effects: JS 100 to 100");
    }

    /**
     * Test {870=[IE], 871=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__JS_100_to_show() throws Exception {
        runTest("effects: JS 100 to show");
    }

    /**
     * Test {871=[IE], 872=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__JS_100_to_hide() throws Exception {
        runTest("effects: JS 100 to hide");
    }

    /**
     * Test {872=[IE], 873=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_50_to_0() throws Exception {
        runTest("effects: CSS 50 to 0");
    }

    /**
     * Test {873=[IE], 874=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_50_to_50() throws Exception {
        runTest("effects: CSS 50 to 50");
    }

    /**
     * Test {874=[IE], 875=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_50_to_100() throws Exception {
        runTest("effects: CSS 50 to 100");
    }

    /**
     * Test {875=[IE], 876=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__CSS_50_to_show() throws Exception {
        runTest("effects: CSS 50 to show");
    }

    /**
     * Test {876=[IE], 877=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__CSS_50_to_hide() throws Exception {
        runTest("effects: CSS 50 to hide");
    }

    /**
     * Test {877=[IE], 878=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_50_to_0() throws Exception {
        runTest("effects: JS 50 to 0");
    }

    /**
     * Test {878=[IE], 879=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_50_to_50() throws Exception {
        runTest("effects: JS 50 to 50");
    }

    /**
     * Test {879=[IE], 880=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_50_to_100() throws Exception {
        runTest("effects: JS 50 to 100");
    }

    /**
     * Test {880=[IE], 881=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__JS_50_to_show() throws Exception {
        runTest("effects: JS 50 to show");
    }

    /**
     * Test {881=[IE], 882=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__JS_50_to_hide() throws Exception {
        runTest("effects: JS 50 to hide");
    }

    /**
     * Test {882=[IE], 883=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_0_to_0() throws Exception {
        runTest("effects: CSS 0 to 0");
    }

    /**
     * Test {883=[IE], 884=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_0_to_50() throws Exception {
        runTest("effects: CSS 0 to 50");
    }

    /**
     * Test {884=[IE], 885=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__CSS_0_to_100() throws Exception {
        runTest("effects: CSS 0 to 100");
    }

    /**
     * Test {885=[IE], 886=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__CSS_0_to_show() throws Exception {
        runTest("effects: CSS 0 to show");
    }

    /**
     * Test {886=[IE], 887=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__CSS_0_to_hide() throws Exception {
        runTest("effects: CSS 0 to hide");
    }

    /**
     * Test {887=[IE], 888=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_0_to_0() throws Exception {
        runTest("effects: JS 0 to 0");
    }

    /**
     * Test {888=[IE], 889=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_0_to_50() throws Exception {
        runTest("effects: JS 0 to 50");
    }

    /**
     * Test {889=[IE], 890=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__JS_0_to_100() throws Exception {
        runTest("effects: JS 0 to 100");
    }

    /**
     * Test {890=[IE], 891=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__JS_0_to_show() throws Exception {
        runTest("effects: JS 0 to show");
    }

    /**
     * Test {891=[IE], 892=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__JS_0_to_hide() throws Exception {
        runTest("effects: JS 0 to hide");
    }

    /**
     * Test {892=[IE], 893=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void effects__Effects_chaining() throws Exception {
        runTest("effects: Effects chaining");
    }

    /**
     * Test {893=[IE], 894=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__jQuery_show__fast___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("effects: jQuery.show('fast') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {894=[IE], 895=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("24")
    public void effects__interrupt_toggle() throws Exception {
        runTest("effects: interrupt toggle");
    }

    /**
     * Test {895=[IE], 896=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__animate_with_per_property_easing() throws Exception {
        runTest("effects: animate with per-property easing");
    }

    /**
     * Test {896=[IE], 897=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("11")
    public void effects__animate_with_CSS_shorthand_properties() throws Exception {
        runTest("effects: animate with CSS shorthand properties");
    }

    /**
     * Test {897=[IE], 898=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__hide_hidden_elements__with_animation__bug__7141_() throws Exception {
        runTest("effects: hide hidden elements, with animation (bug #7141)");
    }

    /**
     * Test {898=[IE], 899=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__animate_unit_less_properties___4966_() throws Exception {
        runTest("effects: animate unit-less properties (#4966)");
    }

    /**
     * Test {899=[IE], 900=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__animate_properties_missing_px_w__opacity_as_last___9074_() throws Exception {
        runTest("effects: animate properties missing px w/ opacity as last (#9074)");
    }

    /**
     * Test {900=[IE], 901=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__callbacks_should_fire_in_correct_order___9100_() throws Exception {
        runTest("effects: callbacks should fire in correct order (#9100)");
    }

    /**
     * Test {901=[IE], 902=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__callbacks_that_throw_exceptions_will_be_removed___5684_() throws Exception {
        runTest("effects: callbacks that throw exceptions will be removed (#5684)");
    }

    /**
     * Test {902=[IE], 903=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__animate_will_scale_margin_properties_individually() throws Exception {
        runTest("effects: animate will scale margin properties individually");
    }

    /**
     * Test {903=[IE], 904=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("effects: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {904=[IE], 905=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    @NotYetImplemented
    public void effects__line_height_animates_correctly___13855_() throws Exception {
        runTest("effects: line-height animates correctly (#13855)");
    }

    /**
     * Test {905=[IE], 906=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__jQuery_Animation__object__props__opts__() throws Exception {
        runTest("effects: jQuery.Animation( object, props, opts )");
    }

    /**
     * Test {906=[IE], 907=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__Animate_Option__step__function__percent__tween__() throws Exception {
        runTest("effects: Animate Option: step: function( percent, tween )");
    }

    /**
     * Test {907=[IE], 908=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__Animate_callbacks_have_correct_context() throws Exception {
        runTest("effects: Animate callbacks have correct context");
    }

    /**
     * Test {908=[IE], 909=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__User_supplied_callback_called_after_show_when_fx_off___8892_() throws Exception {
        runTest("effects: User supplied callback called after show when fx off (#8892)");
    }

    /**
     * Test {909=[IE], 910=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("20")
    public void effects__animate_should_set_display_for_disconnected_nodes() throws Exception {
        runTest("effects: animate should set display for disconnected nodes");
    }

    /**
     * Test {910=[IE], 911=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__Animation_callback_should_not_show_animated_element_as__animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as :animated (#7157)");
    }

    /**
     * Test {911=[IE], 912=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__Initial_step_callback_should_show_element_as__animated___14623_() throws Exception {
        runTest("effects: Initial step callback should show element as :animated (#14623)");
    }

    /**
     * Test {912=[IE], 913=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__hide_called_on_element_within_hidden_parent_should_set_display_to_none___10045_() throws Exception {
        runTest("effects: hide called on element within hidden parent should set display to none (#10045)");
    }

    /**
     * Test {913=[IE], 914=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__hide__fadeOut_and_slideUp_called_on_element_width_height_and_width___0_should_set_display_to_none() throws Exception {
        runTest("effects: hide, fadeOut and slideUp called on element width height and width = 0 should set display to none");
    }

    /**
     * Test {914=[IE], 915=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__hide_should_not_leave_hidden_inline_elements_visible___14848_() throws Exception {
        runTest("effects: hide should not leave hidden inline elements visible (#14848)");
    }

    /**
     * Test {915=[IE], 916=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void effects__Handle_queue_false_promises() throws Exception {
        runTest("effects: Handle queue:false promises");
    }

    /**
     * Test {916=[IE], 917=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__multiple_unqueued_and_promise() throws Exception {
        runTest("effects: multiple unqueued and promise");
    }

    /**
     * Test {917=[IE], 918=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void effects__animate_does_not_change_start_value_for_non_px_animation___7109_() throws Exception {
        runTest("effects: animate does not change start value for non-px animation (#7109)");
    }

    /**
     * Test {918=[IE], 919=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            IE = "1")
    @NotYetImplemented(IE)
    public void effects__non_px_animation_handles_non_numeric_start___11971_() throws Exception {
        runTest("effects: non-px animation handles non-numeric start (#11971)");
    }

    /**
     * Test {919=[IE], 920=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("15")
    public void effects__Animation_callbacks___11797_() throws Exception {
        runTest("effects: Animation callbacks (#11797)");
    }

    /**
     * Test {920=[IE], 921=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void effects__Animation_callbacks_in_order___2292_() throws Exception {
        runTest("effects: Animation callbacks in order (#2292)");
    }

    /**
     * Test {921=[IE], 922=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void effects__Animate_properly_sets_overflow_hidden_when_animating_width_height___12117_() throws Exception {
        runTest("effects: Animate properly sets overflow hidden when animating width/height (#12117)");
    }

    /**
     * Test {922=[IE], 923=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__Each_tick_of_the_timer_loop_uses_a_fresh_time___12837_() throws Exception {
        runTest("effects: Each tick of the timer loop uses a fresh time (#12837)");
    }

    /**
     * Test {923=[IE], 924=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__Animations_with_0_duration_don_t_ease___12273_() throws Exception {
        runTest("effects: Animations with 0 duration don't ease (#12273)");
    }

    /**
     * Test {924=[IE], 925=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__toggle_state_tests__toggle___8685_() throws Exception {
        runTest("effects: toggle state tests: toggle (#8685)");
    }

    /**
     * Test {925=[IE], 926=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__toggle_state_tests__slideToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: slideToggle (#8685)");
    }

    /**
     * Test {926=[IE], 927=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__toggle_state_tests__fadeToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: fadeToggle (#8685)");
    }

    /**
     * Test {927=[IE], 928=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__jQuery_fx_start___jQuery_fx_stop_hook_points() throws Exception {
        runTest("effects: jQuery.fx.start & jQuery.fx.stop hook points");
    }

    /**
     * Test {928=[IE], 929=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("11")
    public void effects___finish___completes_all_queued_animations() throws Exception {
        runTest("effects: .finish() completes all queued animations");
    }

    /**
     * Test {929=[IE], 930=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void effects___finish__false_____unqueued_animations() throws Exception {
        runTest("effects: .finish( false ) - unqueued animations");
    }

    /**
     * Test {930=[IE], 931=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("11")
    public void effects___finish___custom______custom_queue_animations() throws Exception {
        runTest("effects: .finish( \"custom\" ) - custom queue animations");
    }

    /**
     * Test {931=[IE], 932=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects___finish___calls_finish_of_custom_queue_functions() throws Exception {
        runTest("effects: .finish() calls finish of custom queue functions");
    }

    /**
     * Test {932=[IE], 933=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects___finish___is_applied_correctly_when_multiple_elements_were_animated___13937_() throws Exception {
        runTest("effects: .finish() is applied correctly when multiple elements were animated (#13937)");
    }

    /**
     * Test {933=[IE], 934=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__slideDown___after_stop_____13483_() throws Exception {
        runTest("effects: slideDown() after stop() (#13483)");
    }

    /**
     * Test {934=[IE], 935=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__Respect_display_value_on_inline_elements___14824_() throws Exception {
        runTest("effects: Respect display value on inline elements (#14824)");
    }

    /**
     * Test {935=[IE], 936=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__jQuery_easing__default__gh_2218_() throws Exception {
        runTest("effects: jQuery.easing._default (gh-2218)");
    }

    /**
     * Test {936=[IE], 937=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__jQuery_easing__default_in_Animation__gh_2218() throws Exception {
        runTest("effects: jQuery.easing._default in Animation (gh-2218");
    }

    /**
     * Test {937=[IE], 938=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__jQuery_easing__default_in_Tween__gh_2218_() throws Exception {
        runTest("effects: jQuery.easing._default in Tween (gh-2218)");
    }

    /**
     * Test {938=[IE], 939=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void effects__Display_value_is_correct_for_disconnected_nodes__trac_13310_() throws Exception {
        runTest("effects: Display value is correct for disconnected nodes (trac-13310)");
    }

    /**
     * Test {939=[IE], 940=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("40")
    @NotYetImplemented(IE)
    public void effects__Show_hide_toggle_and_display__inline() throws Exception {
        runTest("effects: Show/hide/toggle and display: inline");
    }

    /**
     * Test {940=[IE], 941=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__jQuery_speed__speed__easing__complete__() throws Exception {
        runTest("effects: jQuery.speed( speed, easing, complete )");
    }

    /**
     * Test {941=[IE], 942=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__jQuery_speed__speed__easing__complete_____with_easing_function() throws Exception {
        runTest("effects: jQuery.speed( speed, easing, complete ) - with easing function");
    }

    /**
     * Test {942=[IE], 943=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__jQuery_speed__options__() throws Exception {
        runTest("effects: jQuery.speed( options )");
    }

    /**
     * Test {943=[IE], 944=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void effects__jQuery_speed__options_____with_easing_function() throws Exception {
        runTest("effects: jQuery.speed( options ) - with easing function");
    }

    /**
     * Test {944=[IE], 945=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__jQuery_speed__options_____queue_values() throws Exception {
        runTest("effects: jQuery.speed( options ) - queue values");
    }

    /**
     * Test {945=[IE], 946=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void effects__jQuery_speed_____durations() throws Exception {
        runTest("effects: jQuery.speed() - durations");
    }

    /**
     * Test {946=[IE], 947=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void offset__empty_set() throws Exception {
        runTest("offset: empty set");
    }

    /**
     * Test {947=[IE], 948=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void offset__disconnected_element() throws Exception {
        runTest("offset: disconnected element");
    }

    /**
     * Test {948=[IE], 949=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void offset__hidden__display__none__element() throws Exception {
        runTest("offset: hidden (display: none) element");
    }

    /**
     * Test {949=[IE], 950=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void offset__0_sized_element() throws Exception {
        runTest("offset: 0 sized element");
    }

    /**
     * Test {950=[IE], 951=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void offset__hidden__visibility__hidden__element() throws Exception {
        runTest("offset: hidden (visibility: hidden) element");
    }

    /**
     * Test {951=[IE], 952=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void offset__normal_element() throws Exception {
        runTest("offset: normal element");
    }

    /**
     * Test {953=[IE], 954=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("178")
    @NotYetImplemented
    public void offset__absolute() throws Exception {
        runTest("offset: absolute");
    }

    /**
     * Test {954=[IE], 955=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("64")
    @NotYetImplemented
    public void offset__relative() throws Exception {
        runTest("offset: relative");
    }

    /**
     * Test {955=[IE], 956=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("80")
    @NotYetImplemented
    public void offset__static() throws Exception {
        runTest("offset: static");
    }

    /**
     * Test {956=[IE], 957=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("38")
    public void offset__fixed() throws Exception {
        runTest("offset: fixed");
    }

    /**
     * Test {957=[IE], 958=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    @NotYetImplemented
    public void offset__table() throws Exception {
        runTest("offset: table");
    }

    /**
     * Test {958=[IE], 959=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("26")
    @NotYetImplemented
    public void offset__scroll() throws Exception {
        runTest("offset: scroll");
    }

    /**
     * Test {959=[IE], 960=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    @NotYetImplemented
    public void offset__body() throws Exception {
        runTest("offset: body");
    }

    /**
     * Test {960=[IE], 961=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void offset__chaining() throws Exception {
        runTest("offset: chaining");
    }

    /**
     * Test {961=[IE], 962=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_static_body_static() throws Exception {
        runTest("offset: nonzero box properties - html.static body.static");
    }

    /**
     * Test {962=[IE], 963=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_static_body_relative() throws Exception {
        runTest("offset: nonzero box properties - html.static body.relative");
    }

    /**
     * Test {963=[IE], 964=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_static_body_absolute() throws Exception {
        runTest("offset: nonzero box properties - html.static body.absolute");
    }

    /**
     * Test {964=[IE], 965=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_static_body_fixed() throws Exception {
        runTest("offset: nonzero box properties - html.static body.fixed");
    }

    /**
     * Test {965=[IE], 966=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_relative_body_static() throws Exception {
        runTest("offset: nonzero box properties - html.relative body.static");
    }

    /**
     * Test {966=[IE], 967=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_relative_body_relative() throws Exception {
        runTest("offset: nonzero box properties - html.relative body.relative");
    }

    /**
     * Test {967=[IE], 968=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_relative_body_absolute() throws Exception {
        runTest("offset: nonzero box properties - html.relative body.absolute");
    }

    /**
     * Test {968=[IE], 969=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_relative_body_fixed() throws Exception {
        runTest("offset: nonzero box properties - html.relative body.fixed");
    }

    /**
     * Test {969=[IE], 970=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_absolute_body_static() throws Exception {
        runTest("offset: nonzero box properties - html.absolute body.static");
    }

    /**
     * Test {970=[IE], 971=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_absolute_body_relative() throws Exception {
        runTest("offset: nonzero box properties - html.absolute body.relative");
    }

    /**
     * Test {971=[IE], 972=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_absolute_body_absolute() throws Exception {
        runTest("offset: nonzero box properties - html.absolute body.absolute");
    }

    /**
     * Test {972=[IE], 973=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_absolute_body_fixed() throws Exception {
        runTest("offset: nonzero box properties - html.absolute body.fixed");
    }

    /**
     * Test {973=[IE], 974=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_fixed_body_static() throws Exception {
        runTest("offset: nonzero box properties - html.fixed body.static");
    }

    /**
     * Test {974=[IE], 975=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_fixed_body_relative() throws Exception {
        runTest("offset: nonzero box properties - html.fixed body.relative");
    }

    /**
     * Test {975=[IE], 976=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_fixed_body_absolute() throws Exception {
        runTest("offset: nonzero box properties - html.fixed body.absolute");
    }

    /**
     * Test {976=[IE], 977=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_fixed_body_fixed() throws Exception {
        runTest("offset: nonzero box properties - html.fixed body.fixed");
    }

    /**
     * Test {977=[IE], 978=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("13")
    public void offset__offsetParent() throws Exception {
        runTest("offset: offsetParent");
    }

    /**
     * Test {978=[IE], 979=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void offset__fractions__see__7730_and__7885_() throws Exception {
        runTest("offset: fractions (see #7730 and #7885)");
    }

    /**
     * Test {979=[IE], 980=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void offset__iframe_scrollTop_Left__see_gh_1945_() throws Exception {
        runTest("offset: iframe scrollTop/Left (see gh-1945)");
    }

    /**
     * Test {980=[IE], 981=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void dimensions__width__() throws Exception {
        runTest("dimensions: width()");
    }

    /**
     * Test {981=[IE], 982=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void dimensions__width_Function_() throws Exception {
        runTest("dimensions: width(Function)");
    }

    /**
     * Test {982=[IE], 983=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void dimensions__width_Function_args__() throws Exception {
        runTest("dimensions: width(Function(args))");
    }

    /**
     * Test {983=[IE], 984=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void dimensions__height__() throws Exception {
        runTest("dimensions: height()");
    }

    /**
     * Test {984=[IE], 985=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void dimensions__height_Function_() throws Exception {
        runTest("dimensions: height(Function)");
    }

    /**
     * Test {985=[IE], 986=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void dimensions__height_Function_args__() throws Exception {
        runTest("dimensions: height(Function(args))");
    }

    /**
     * Test {986=[IE], 987=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void dimensions__innerWidth__() throws Exception {
        runTest("dimensions: innerWidth()");
    }

    /**
     * Test {987=[IE], 988=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void dimensions__innerHeight__() throws Exception {
        runTest("dimensions: innerHeight()");
    }

    /**
     * Test {988=[IE], 989=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void dimensions__outerWidth__() throws Exception {
        runTest("dimensions: outerWidth()");
    }

    /**
     * Test {989=[IE], 990=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void dimensions__outerHeight__() throws Exception {
        runTest("dimensions: outerHeight()");
    }

    /**
     * Test {990=[IE], 991=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void dimensions__child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__9441__9300() throws Exception {
        runTest("dimensions: child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #9441 #9300");
    }

    /**
     * Test {991=[IE], 992=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void dimensions__getting_dimensions_shouldn_t_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldn't modify runtimeStyle see #9233");
    }

    /**
     * Test {992=[IE], 993=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void dimensions__table_dimensions() throws Exception {
        runTest("dimensions: table dimensions");
    }

    /**
     * Test {993=[IE], 994=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void dimensions__box_sizing_border_box_child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__10413() throws Exception {
        runTest("dimensions: box-sizing:border-box child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #10413");
    }

    /**
     * Test {994=[IE], 995=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void dimensions__passing_undefined_is_a_setter__5571() throws Exception {
        runTest("dimensions: passing undefined is a setter #5571");
    }

    /**
     * Test {995=[IE], 996=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("120")
    public void dimensions__setters_with_and_without_box_sizing_border_box() throws Exception {
        runTest("dimensions: setters with and without box-sizing:border-box");
    }

    /**
     * Test {996=[IE], 997=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented
    public void dimensions__window_vs__large_document() throws Exception {
        runTest("dimensions: window vs. large document");
    }

    /**
     * Test {997=[IE], 998=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void dimensions__allow_modification_of_coordinates_argument__gh_1848_() throws Exception {
        runTest("dimensions: allow modification of coordinates argument (gh-1848)");
    }

    /**
     * Test {998=[IE], 999=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    @NotYetImplemented
    public void dimensions__outside_view_position__gh_2836_() throws Exception {
        runTest("dimensions: outside view position (gh-2836)");
    }

    /**
     * Test {999=[IE], 1000=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void dimensions__width_height_on_element_with_transform__gh_3193_() throws Exception {
        runTest("dimensions: width/height on element with transform (gh-3193)");
    }

    /**
     * Test {1000=[IE], 1001=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void dimensions__width_height_on_an_inline_element_with_no_explicitly_set_dimensions__gh_3571_() throws Exception {
        runTest("dimensions: width/height on an inline element with no explicitly-set dimensions (gh-3571)");
    }

    /**
     * Test {1001=[IE], 1002=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    @NotYetImplemented
    public void dimensions__width_height_on_an_inline_element_with_percentage_dimensions__gh_3611_() throws Exception {
        runTest("dimensions: width/height on an inline element with percentage dimensions (gh-3611)");
    }

    /**
     * Test {1002=[IE], 1003=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "4",
            FF = "4, 0, 4",
            FF78 = "4, 0, 4")
    @NotYetImplemented
    public void dimensions__width_height_on_a_table_row_with_phantom_borders__gh_3698_() throws Exception {
        runTest("dimensions: width/height on a table row with phantom borders (gh-3698)");
    }

    /**
     * Test {1003=[IE], 1004=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("48")
    public void dimensions__interaction_with_scrollbars__gh_3589_() throws Exception {
        runTest("dimensions: interaction with scrollbars (gh-3589)");
    }

    /**
     * Test {1004=[IE], 1005=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("20")
    public void animation__Animation__subject__props__opts_____shape() throws Exception {
        runTest("animation: Animation( subject, props, opts ) - shape");
    }

    /**
     * Test {1005=[IE], 1006=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void animation__Animation_prefilter__fn_____calls_prefilter_after_defaultPrefilter() throws Exception {
        runTest("animation: Animation.prefilter( fn ) - calls prefilter after defaultPrefilter");
    }

    /**
     * Test {1006=[IE], 1007=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void animation__Animation_prefilter__fn__true_____calls_prefilter_before_defaultPrefilter() throws Exception {
        runTest("animation: Animation.prefilter( fn, true ) - calls prefilter before defaultPrefilter");
    }

    /**
     * Test {1007=[IE], 1008=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("34")
    public void animation__Animation_prefilter___prefilter_return_hooks() throws Exception {
        runTest("animation: Animation.prefilter - prefilter return hooks");
    }

    /**
     * Test {1008=[IE], 1009=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void animation__Animation_tweener__fn_____unshifts_a___tweener() throws Exception {
        runTest("animation: Animation.tweener( fn ) - unshifts a * tweener");
    }

    /**
     * Test {1009=[IE], 1010=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void animation__Animation_tweener___prop___fn_____unshifts_a__prop__tweener() throws Exception {
        runTest("animation: Animation.tweener( 'prop', fn ) - unshifts a 'prop' tweener");
    }

    /**
     * Test {1010=[IE], 1011=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void animation__Animation_tweener___list_of_props___fn_____unshifts_a_tweener_to_each_prop() throws Exception {
        runTest("animation: Animation.tweener( 'list of props', fn ) - unshifts a tweener to each prop");
    }

    /**
     * Test {1011=[IE], 1012=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void tween__jQuery_Tween___Default_propHooks_on_plain_objects() throws Exception {
        runTest("tween: jQuery.Tween - Default propHooks on plain objects");
    }

    /**
     * Test {1012=[IE], 1013=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void tween__jQuery_Tween___Default_propHooks_on_elements() throws Exception {
        runTest("tween: jQuery.Tween - Default propHooks on elements");
    }

    /**
     * Test {1013=[IE], 1014=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("13")
    public void tween__jQuery_Tween___Plain_Object() throws Exception {
        runTest("tween: jQuery.Tween - Plain Object");
    }

    /**
     * Test {1014=[IE], 1015=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("15")
    public void tween__jQuery_Tween___Element() throws Exception {
        runTest("tween: jQuery.Tween - Element");
    }

    /**
     * Test {1015=[IE], 1016=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void tween__jQuery_Tween___No_duration() throws Exception {
        runTest("tween: jQuery.Tween - No duration");
    }

    /**
     * Test {1016=[IE], 1017=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void tween__jQuery_Tween___step_function_option() throws Exception {
        runTest("tween: jQuery.Tween - step function option");
    }

    /**
     * Test {1017=[IE], 1018=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void tween__jQuery_Tween___custom_propHooks() throws Exception {
        runTest("tween: jQuery.Tween - custom propHooks");
    }

    /**
     * Test {1018=[IE], 1019=[CHROME, EDGE, FF, FF78]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void tween__jQuery_Tween___custom_propHooks___advanced_values() throws Exception {
        runTest("tween: jQuery.Tween - custom propHooks - advanced values");
    }
}
