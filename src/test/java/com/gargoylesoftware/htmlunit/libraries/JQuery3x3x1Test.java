/*
 * Copyright (c) 2002-2019 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF60;
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
     * Test {1=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ready__jQuery_isReady() throws Exception {
        runTest("ready: jQuery.isReady");
    }

    /**
     * Test {2=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    @NotYetImplemented
    public void ready__jQuery_ready() throws Exception {
        runTest("ready: jQuery ready");
    }

    /**
     * Test {3=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ready__jQuery_when_jQuery_ready_() throws Exception {
        runTest("ready: jQuery.when(jQuery.ready)");
    }

    /**
     * Test {4=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ready__Promise_resolve_jQuery_ready_() throws Exception {
        runTest("ready: Promise.resolve(jQuery.ready)");
    }

    /**
     * Test {5=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void ready__Error_in_ready_callback_does_not_halt_all_future_executions__gh_1823_() throws Exception {
        runTest("ready: Error in ready callback does not halt all future executions (gh-1823)");
    }

    /**
     * Test {6=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ready__holdReady_test_needs_to_be_a_standalone_test_since_it_deals_with_DOM_ready() throws Exception {
        runTest("ready: holdReady test needs to be a standalone test since it deals with DOM ready");
    }

    /**
     * Test {7=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void basic__ajax() throws Exception {
        runTest("basic: ajax");
    }

    /**
     * Test {8=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void basic__attributes() throws Exception {
        runTest("basic: attributes");
    }

    /**
     * Test {9=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void basic__css() throws Exception {
        runTest("basic: css");
    }

    /**
     * Test {10=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void basic__show_hide() throws Exception {
        runTest("basic: show/hide");
    }

    /**
     * Test {11=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void basic__core() throws Exception {
        runTest("basic: core");
    }

    /**
     * Test {12=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void basic__data() throws Exception {
        runTest("basic: data");
    }

    /**
     * Test {13=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void basic__dimensions() throws Exception {
        runTest("basic: dimensions");
    }

    /**
     * Test {14=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void basic__event() throws Exception {
        runTest("basic: event");
    }

    /**
     * Test {15=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void basic__manipulation() throws Exception {
        runTest("basic: manipulation");
    }

    /**
     * Test {16=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented
    public void basic__offset() throws Exception {
        runTest("basic: offset");
    }

    /**
     * Test {17=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void basic__selector() throws Exception {
        runTest("basic: selector");
    }

    /**
     * Test {18=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void basic__serialize() throws Exception {
        runTest("basic: serialize");
    }

    /**
     * Test {19=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void basic__traversing() throws Exception {
        runTest("basic: traversing");
    }

    /**
     * Test {20=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void basic__wrap() throws Exception {
        runTest("basic: wrap");
    }

    /**
     * Test {21=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2")
    public void core__Unit_Testing_Environment() throws Exception {
        runTest("core: Unit Testing Environment");
    }

    /**
     * Test {21=[CHROME, FF60, IE], 22=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void core__Basic_requirements() throws Exception {
        runTest("core: Basic requirements");
    }

    /**
     * Test {22=[CHROME, FF60, IE], 23=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "29",
            FF60 = "29",
            FF68 = "2, 4, 6",
            IE = "29")
    public void core__jQuery__() throws Exception {
        runTest("core: jQuery()");
    }

    /**
     * Test {23=[CHROME, FF60, IE], 24=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__jQuery_selector__context_() throws Exception {
        runTest("core: jQuery(selector, context)");
    }

    /**
     * Test {24=[CHROME, FF60, IE], 26=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__globalEval() throws Exception {
        runTest("core: globalEval");
    }

    /**
     * Test {25=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__globalEval_with__use_strict_() throws Exception {
        runTest("core: globalEval with 'use strict'");
    }

    /**
     * Test {25=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "17, 1, 18")
    public void core__selector_state() throws Exception {
        runTest("core: selector state");
    }

    /**
     * Test {26=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__globalEval_execution_after_script_injection___7862_() throws Exception {
        runTest("core: globalEval execution after script injection (#7862)");
    }

    /**
     * Test {27=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void core__noConflict() throws Exception {
        runTest("core: noConflict");
    }

    /**
     * Test {28=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("13")
    public void core__trim() throws Exception {
        runTest("core: trim");
    }

    /**
     * Test {29=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "28")
    public void core__type() throws Exception {
        runTest("core: type");
    }

    /**
     * Test {29=[CHROME, FF60, IE], 30=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "23",
            FF60 = "23",
            FF68 = "16",
            IE = "23")
    public void core__isPlainObject() throws Exception {
        runTest("core: isPlainObject");
    }

    /**
     * Test {30=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF60 = "2",
            IE = "0")
    public void core__isPlainObject_Symbol_() throws Exception {
        runTest("core: isPlainObject(Symbol)");
    }

    /**
     * Test {31=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "19")
    public void core__isFunction() throws Exception {
        runTest("core: isFunction");
    }

    /**
     * Test {31=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__isPlainObject_localStorage_() throws Exception {
        runTest("core: isPlainObject(localStorage)");
    }

    /**
     * Test {32=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "1, 37, 38")
    public void core__isNumeric() throws Exception {
        runTest("core: isNumeric");
    }

    /**
     * Test {32=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            IE = "0")
    public void core__isPlainObject_Object_assign______() throws Exception {
        runTest("core: isPlainObject(Object.assign(...))");
    }

    /**
     * Test {33=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void core__isXMLDoc___HTML() throws Exception {
        runTest("core: isXMLDoc - HTML");
    }

    /**
     * Test {34=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__XSS_via_location_hash() throws Exception {
        runTest("core: XSS via location.hash");
    }

    /**
     * Test {35=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__isXMLDoc___XML() throws Exception {
        runTest("core: isXMLDoc - XML");
    }

    /**
     * Test {36=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "14")
    public void core__isWindow() throws Exception {
        runTest("core: isWindow");
    }

    /**
     * Test {36=[CHROME, FF60, IE], 37=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void core__jQuery__html__() throws Exception {
        runTest("core: jQuery('html')");
    }

    /**
     * Test {37=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "36",
            FF60 = "36")
    public void core__jQuery_element_with_non_alphanumeric_name_() throws Exception {
        runTest("core: jQuery(element with non-alphanumeric name)");
    }

    /**
     * Test {38=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__jQuery__massive_html__7990__() throws Exception {
        runTest("core: jQuery('massive html #7990')");
    }

    /**
     * Test {39=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__jQuery__html___context_() throws Exception {
        runTest("core: jQuery('html', context)");
    }

    /**
     * Test {40=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__jQuery_selector__xml__text_str____loaded_via_xml_document() throws Exception {
        runTest("core: jQuery(selector, xml).text(str) - loaded via xml document");
    }

    /**
     * Test {41=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__end__() throws Exception {
        runTest("core: end()");
    }

    /**
     * Test {42=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__length() throws Exception {
        runTest("core: length");
    }

    /**
     * Test {43=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__get__() throws Exception {
        runTest("core: get()");
    }

    /**
     * Test {44=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__toArray__() throws Exception {
        runTest("core: toArray()");
    }

    /**
     * Test {45=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void core__inArray__() throws Exception {
        runTest("core: inArray()");
    }

    /**
     * Test {46=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__get_Number_() throws Exception {
        runTest("core: get(Number)");
    }

    /**
     * Test {47=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__get__Number_() throws Exception {
        runTest("core: get(-Number)");
    }

    /**
     * Test {48=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void core__each_Function_() throws Exception {
        runTest("core: each(Function)");
    }

    /**
     * Test {49=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void core__slice__() throws Exception {
        runTest("core: slice()");
    }

    /**
     * Test {50=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void core__first___last__() throws Exception {
        runTest("core: first()/last()");
    }

    /**
     * Test {51=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__map__() throws Exception {
        runTest("core: map()");
    }

    /**
     * Test {52=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("25")
    public void core__jQuery_map() throws Exception {
        runTest("core: jQuery.map");
    }

    /**
     * Test {53=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void core__jQuery_merge__() throws Exception {
        runTest("core: jQuery.merge()");
    }

    /**
     * Test {54=[CHROME, FF60, FF68, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void core__jQuery_grep__() throws Exception {
        runTest("core: jQuery.grep()");
    }

    /**
     * Test {55=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "7",
            FF60 = "7")
    public void core__jQuery_grep_Array_like_() throws Exception {
        runTest("core: jQuery.grep(Array-like)");
    }

    /**
     * Test {55=[FF68], 56=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("28")
    public void core__jQuery_extend_Object__Object_() throws Exception {
        runTest("core: jQuery.extend(Object, Object)");
    }

    /**
     * Test {56=[FF68], 59=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("23")
    public void core__jQuery_each_Object_Function_() throws Exception {
        runTest("core: jQuery.each(Object,Function)");
    }

    /**
     * Test {57=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            FF60 = "2")
    public void core__jQuery_extend_Object__Object__created_with__defineProperties___() throws Exception {
        runTest("core: jQuery.extend(Object, Object {created with \"defineProperties\"})");
    }

    /**
     * Test {57=[FF68], 61=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void core__JIT_compilation_does_not_interfere_with_length_retrieval__gh_2145_() throws Exception {
        runTest("core: JIT compilation does not interfere with length retrieval (gh-2145)");
    }

    /**
     * Test {58=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            FF60 = "2")
    public void core__jQuery_extend_true_____a_____o_______deep_copy_with_array__followed_by_object() throws Exception {
        runTest("core: jQuery.extend(true,{},{a:[], o:{}}); deep copy with array, followed by object");
    }

    /**
     * Test {58=[FF68], 62=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "15",
            CHROME = "1, 14, 15")
    @NotYetImplemented(CHROME)
    public void core__jQuery_makeArray() throws Exception {
        runTest("core: jQuery.makeArray");
    }

    /**
     * Test {59=[FF68], 63=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void core__jQuery_inArray() throws Exception {
        runTest("core: jQuery.inArray");
    }

    /**
     * Test {60=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__jQuery_each_map_undefined_null_Function_() throws Exception {
        runTest("core: jQuery.each/map(undefined/null,Function)");
    }

    /**
     * Test {60=[FF68], 64=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void core__jQuery_isEmptyObject() throws Exception {
        runTest("core: jQuery.isEmptyObject");
    }

    /**
     * Test {61=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "9")
    public void core__jQuery_proxy() throws Exception {
        runTest("core: jQuery.proxy");
    }

    /**
     * Test {62=[FF68], 67=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            FF68 = "3, 14, 17",
            IE = "1")
    @NotYetImplemented
    public void core__jQuery_parseHTML() throws Exception {
        runTest("core: jQuery.parseHTML");
    }

    /**
     * Test {63=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "20")
    public void core__jQuery_parseJSON() throws Exception {
        runTest("core: jQuery.parseJSON");
    }

    /**
     * Test {64=[FF68], 68=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void core__jQuery_parseXML() throws Exception {
        runTest("core: jQuery.parseXML");
    }

    /**
     * Test {65=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "1, 6, 7")
    public void core__jQuery_camelCase__() throws Exception {
        runTest("core: jQuery.camelCase()");
    }

    /**
     * Test {66=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2, 0, 2")
    public void core__global_failure() throws Exception {
        runTest("core: global failure");
    }

    /**
     * Test {66=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__jQuery_parseHTML__a_href_____gh_2965() throws Exception {
        runTest("core: jQuery.parseHTML(<a href>) - gh-2965");
    }

    /**
     * Test {67=[FF68], 76=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - no filter");
    }

    /**
     * Test {68=[FF68], 77=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20",
            FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks__________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - no filter");
    }

    /**
     * Test {69=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "3",
            FF60 = "3")
    public void core__Conditional_compilation_compatibility___13274_() throws Exception {
        runTest("core: Conditional compilation compatibility (#13274)");
    }

    /**
     * Test {69=[FF68], 78=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - filter");
    }

    /**
     * Test {70=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    @NotYetImplemented
    public void core__document_ready_when_jQuery_loaded_asynchronously___13655_() throws Exception {
        runTest("core: document ready when jQuery loaded asynchronously (#13655)");
    }

    /**
     * Test {70=[FF68], 79=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20",
            FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks__________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - filter");
    }

    /**
     * Test {71=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__Tolerating_alias_masked_DOM_properties___14074_() throws Exception {
        runTest("core: Tolerating alias-masked DOM properties (#14074)");
    }

    /**
     * Test {71=[FF68], 80=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - no filter");
    }

    /**
     * Test {72=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__Don_t_call_window_onready___14802_() throws Exception {
        runTest("core: Don't call window.onready (#14802)");
    }

    /**
     * Test {72=[FF68], 81=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - no filter");
    }

    /**
     * Test {73=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__Iterability_of_jQuery_objects__gh_1693_() throws Exception {
        runTest("core: Iterability of jQuery objects (gh-1693)");
    }

    /**
     * Test {73=[FF68], 82=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - filter");
    }

    /**
     * Test {74=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__jQuery_readyException__original_() throws Exception {
        runTest("core: jQuery.readyException (original)");
    }

    /**
     * Test {74=[FF68], 83=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - filter");
    }

    /**
     * Test {75=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            FF60 = "1")
    public void core__jQuery_readyException__custom_() throws Exception {
        runTest("core: jQuery.readyException (custom)");
    }

    /**
     * Test {75=[FF68], 84=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - no filter");
    }

    /**
     * Test {76=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - no filter");
    }

    /**
     * Test {76=[FF68], 85=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - no filter");
    }

    /**
     * Test {77=[FF68], 86=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - filter");
    }

    /**
     * Test {78=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - filter");
    }

    /**
     * Test {78=[FF68], 87=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - filter");
    }

    /**
     * Test {79=[FF68], 88=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - no filter");
    }

    /**
     * Test {80=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - no filter");
    }

    /**
     * Test {80=[FF68], 89=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - no filter");
    }

    /**
     * Test {81=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - no filter");
    }

    /**
     * Test {81=[FF68], 90=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - filter");
    }

    /**
     * Test {82=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - filter");
    }

    /**
     * Test {82=[FF68], 91=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - filter");
    }

    /**
     * Test {83=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - filter");
    }

    /**
     * Test {83=[FF68], 92=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - no filter");
    }

    /**
     * Test {84=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - no filter");
    }

    /**
     * Test {84=[FF68], 93=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {85=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - no filter");
    }

    /**
     * Test {85=[FF68], 94=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - filter");
    }

    /**
     * Test {86=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - filter");
    }

    /**
     * Test {86=[FF68], 95=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {87=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - filter");
    }

    /**
     * Test {87=[FF68], 96=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - no filter");
    }

    /**
     * Test {88=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - no filter");
    }

    /**
     * Test {88=[FF68], 97=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - no filter");
    }

    /**
     * Test {89=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - no filter");
    }

    /**
     * Test {89=[FF68], 98=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - filter");
    }

    /**
     * Test {90=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - filter");
    }

    /**
     * Test {90=[FF68], 99=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - filter");
    }

    /**
     * Test {91=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - filter");
    }

    /**
     * Test {91=[FF68], 100=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - no filter");
    }

    /**
     * Test {92=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - no filter");
    }

    /**
     * Test {92=[FF68], 101=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {93=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {93=[FF68], 102=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - filter");
    }

    /**
     * Test {94=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - filter");
    }

    /**
     * Test {94=[FF68], 103=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - filter");
    }

    /**
     * Test {95=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {95=[FF68], 104=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - no filter");
    }

    /**
     * Test {96=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - no filter");
    }

    /**
     * Test {96=[FF68], 105=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {97=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - no filter");
    }

    /**
     * Test {97=[FF68], 106=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - filter");
    }

    /**
     * Test {98=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - filter");
    }

    /**
     * Test {98=[FF68], 107=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {99=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - filter");
    }

    /**
     * Test {99=[FF68], 108=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - no filter");
    }

    /**
     * Test {100=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - no filter");
    }

    /**
     * Test {100=[FF68], 109=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {101=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {101=[FF68], 110=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - filter");
    }

    /**
     * Test {102=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - filter");
    }

    /**
     * Test {102=[FF68], 111=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - filter");
    }

    /**
     * Test {103=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {103=[FF68], 112=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - no filter");
    }

    /**
     * Test {104=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - no filter");
    }

    /**
     * Test {104=[FF68], 113=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {105=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {105=[FF68], 114=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - filter");
    }

    /**
     * Test {106=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - filter");
    }

    /**
     * Test {106=[FF68], 115=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {107=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {107=[FF68], 116=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - no filter");
    }

    /**
     * Test {108=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - no filter");
    }

    /**
     * Test {108=[FF68], 117=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {109=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {109=[FF68], 118=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - filter");
    }

    /**
     * Test {110=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - filter");
    }

    /**
     * Test {110=[FF68], 119=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "29",
            FF68 = "21",
            IE = "29")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {111=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {111=[FF68], 120=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void callbacks__jQuery_Callbacks__options_____options_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks( options ) - options are copied");
    }

    /**
     * Test {112=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - no filter");
    }

    /**
     * Test {112=[FF68], 121=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void callbacks__jQuery_Callbacks_fireWith___arguments_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks.fireWith - arguments are copied");
    }

    /**
     * Test {113=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {113=[FF68], 122=[CHROME, FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void callbacks__jQuery_Callbacks_remove___should_remove_all_instances() throws Exception {
        runTest("callbacks: jQuery.Callbacks.remove - should remove all instances");
    }

    /**
     * Test {114=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - filter");
    }

    /**
     * Test {114=[FF68], 123=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "13",
            IE = "13")
    public void callbacks__jQuery_Callbacks_has() throws Exception {
        runTest("callbacks: jQuery.Callbacks.has");
    }

    /**
     * Test {115=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {115=[FF68], 123=[CHROME], 124=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void callbacks__jQuery_Callbacks_____adding_a_string_doesn_t_cause_a_stack_overflow() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow");
    }

    /**
     * Test {116=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - no filter");
    }

    /**
     * Test {116=[FF68], 124=[CHROME], 127=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 1, 4",
            FF60 = "23",
            FF68 = "3, 1, 4",
            IE = "23")
    public void deferred__jQuery_Deferred() throws Exception {
        runTest("deferred: jQuery.Deferred");
    }

    /**
     * Test {117=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {117=[FF68], 125=[CHROME], 128=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 1, 4",
            FF60 = "23",
            FF68 = "3, 1, 4",
            IE = "23")
    public void deferred__jQuery_Deferred___new_operator() throws Exception {
        runTest("deferred: jQuery.Deferred - new operator");
    }

    /**
     * Test {118=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - filter");
    }

    /**
     * Test {118=[FF68], 126=[CHROME], 129=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void deferred__jQuery_Deferred___chainability() throws Exception {
        runTest("deferred: jQuery.Deferred - chainability");
    }

    /**
     * Test {119=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {119=[FF68], 127=[CHROME], 130=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 2, 4",
            FF60 = "4",
            FF68 = "2, 2, 4",
            IE = "4")
    public void deferred__jQuery_Deferred_then___filtering__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (done)");
    }

    /**
     * Test {120=[FF68], 128=[CHROME], 131=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 2, 4",
            FF60 = "4",
            FF68 = "2, 2, 4",
            IE = "4")
    @NotYetImplemented({ CHROME, IE })
    public void deferred__jQuery_Deferred_then___filtering__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (fail)");
    }

    /**
     * Test {121=[FF68], 129=[CHROME], 134=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 2, 3",
            FF60 = "3",
            FF68 = "1, 2, 3",
            IE = "3")
    @NotYetImplemented(CHROME)
    public void deferred__jQuery_Deferred_then___filtering__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (progress)");
    }

    /**
     * Test {122=[FF68], 130=[CHROME], 135=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 2, 3",
            FF60 = "3",
            FF68 = "1, 2, 3",
            IE = "3")
    public void deferred__jQuery_Deferred_then___deferred__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (done)");
    }

    /**
     * Test {123=[FF68], 131=[CHROME], 136=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 2, 3",
            FF60 = "3",
            FF68 = "1, 2, 3",
            IE = "3")
    public void deferred__jQuery_Deferred_then___deferred__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (fail)");
    }

    /**
     * Test {124=[FF68], 132=[CHROME], 137=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 2, 3",
            FF60 = "3",
            FF68 = "1, 2, 3",
            IE = "3")
    public void deferred__jQuery_Deferred_then___deferred__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (progress)");
    }

    /**
     * Test {125=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void callbacks__jQuery_Callbacks_____disabled_callback_doesn_t_fire__gh_1790_() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - disabled callback doesn't fire (gh-1790)");
    }

    /**
     * Test {125=[FF68], 133=[CHROME], 139=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1",
            FF60 = "11",
            FF68 = "1, 0, 1",
            IE = "11")
    public void deferred__jQuery_Deferred_then___context() throws Exception {
        runTest("deferred: jQuery.Deferred.then - context");
    }

    /**
     * Test {126=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void callbacks__jQuery_Callbacks_____list_with_memory_stays_locked__gh_3469_() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - list with memory stays locked (gh-3469)");
    }

    /**
     * Test {126=[FF68], 134=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "34",
            FF68 = "37")
    public void deferred__jQuery_when() throws Exception {
        runTest("deferred: jQuery.when");
    }

    /**
     * Test {127=[FF68], 135=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "13, 35, 48",
            FF68 = "13, 35, 48")
    public void deferred__jQuery_when___joined() throws Exception {
        runTest("deferred: jQuery.when - joined");
    }

    /**
     * Test {128=[FF68], 155=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void deprecated__bind_unbind() throws Exception {
        runTest("deprecated: bind/unbind");
    }

    /**
     * Test {129=[FF68], 156=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void deprecated__delegate_undelegate() throws Exception {
        runTest("deprecated: delegate/undelegate");
    }

    /**
     * Test {130=[FF68], 157=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void deprecated__hover___mouseenter_mouseleave() throws Exception {
        runTest("deprecated: hover() mouseenter mouseleave");
    }

    /**
     * Test {131=[FF68], 158=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "5",
            IE = "5")
    public void deprecated__trigger___shortcuts() throws Exception {
        runTest("deprecated: trigger() shortcuts");
    }

    /**
     * Test {132=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    @NotYetImplemented({ CHROME, FF60 })
    public void deferred__jQuery_Deferred_catch() throws Exception {
        runTest("deferred: jQuery.Deferred.catch");
    }

    /**
     * Test {132=[FF68], 159=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "18",
            IE = "18")
    public void deprecated__Event_aliases() throws Exception {
        runTest("deprecated: Event aliases");
    }

    /**
     * Test {133=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void deferred___PIPE_ONLY__jQuery_Deferred_pipe___filtering__fail_() throws Exception {
        runTest("deferred: [PIPE ONLY] jQuery.Deferred.pipe - filtering (fail)");
    }

    /**
     * Test {133=[FF68], 160=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "20",
            IE = "20")
    public void deprecated__jQuery_parseJSON() throws Exception {
        runTest("deprecated: jQuery.parseJSON");
    }

    /**
     * Test {134=[FF68], 161=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void deprecated__jQuery_isArray() throws Exception {
        runTest("deprecated: jQuery.isArray");
    }

    /**
     * Test {135=[FF68], 162=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "8",
            IE = "8")
    public void deprecated__jQuery_nodeName() throws Exception {
        runTest("deprecated: jQuery.nodeName");
    }

    /**
     * Test {136=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void support__boxModel() throws Exception {
        runTest("support: boxModel");
    }

    /**
     * Test {136=[FF68], 163=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "28",
            IE = "28")
    public void deprecated__type() throws Exception {
        runTest("deprecated: type");
    }

    /**
     * Test {137=[FF68], 164=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "0")
    public void deprecated__type_for__Symbol_() throws Exception {
        runTest("deprecated: type for `Symbol`");
    }

    /**
     * Test {137=[CHROME], 150=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void support__global_failure() throws Exception {
        runTest("support: global failure");
    }

    /**
     * Test {138=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void deferred___PIPE_ONLY__jQuery_Deferred_pipe___deferred__progress_() throws Exception {
        runTest("deferred: [PIPE ONLY] jQuery.Deferred.pipe - deferred (progress)");
    }

    /**
     * Test {138=[FF68], 165=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "20",
            IE = "20")
    public void deprecated__isFunction() throws Exception {
        runTest("deprecated: isFunction");
    }

    /**
     * Test {138=[CHROME], 151=[FF68], 179=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__expando() throws Exception {
        runTest("data: expando");
    }

    /**
     * Test {139=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "8, 116, 124")
    public void data__jQuery_data() throws Exception {
        runTest("data: jQuery.data");
    }

    /**
     * Test {139=[FF68], 166=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "7, 6, 13",
            IE = "1")
    public void deprecated__isFunction_cross_realm_function_() throws Exception {
        runTest("deprecated: isFunction(cross-realm function)");
    }

    /**
     * Test {140=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "11",
            IE = "11")
    public void deferred___PIPE_ONLY__jQuery_Deferred_pipe___context() throws Exception {
        runTest("deferred: [PIPE ONLY] jQuery.Deferred.pipe - context");
    }

    /**
     * Test {140=[CHROME], 158=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void data__jQuery_acceptData() throws Exception {
        runTest("data: jQuery.acceptData");
    }

    /**
     * Test {140=[FF68], 167=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "0")
    @NotYetImplemented({ CHROME, FF })
    public void deprecated__isFunction_GeneratorFunction_() throws Exception {
        runTest("deprecated: isFunction(GeneratorFunction)");
    }

    /**
     * Test {141=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    @NotYetImplemented(FF)
    public void deferred__jQuery_Deferred_then___spec_compatibility() throws Exception {
        runTest("deferred: jQuery.Deferred.then - spec compatibility");
    }

    /**
     * Test {141=[FF68], 168=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "0")
    @NotYetImplemented({ CHROME, FF })
    public void deprecated__isFunction_AsyncFunction_() throws Exception {
        runTest("deprecated: isFunction(AsyncFunction)");
    }

    /**
     * Test {141=[CHROME], 160=[FF68], 190=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void data___data__() throws Exception {
        runTest("data: .data()");
    }

    /**
     * Test {142=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "13, 16, 29")
    public void data___data_String__and__data_String__Object_() throws Exception {
        runTest("data: .data(String) and .data(String, Object)");
    }

    /**
     * Test {142=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "0")
    public void deferred__jQuery_Deferred_then___IsCallable_determination__gh_3596_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - IsCallable determination (gh-3596)");
    }

    /**
     * Test {142=[FF68], 169=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "0")
    public void deprecated__isFunction_custom___toStringTag_() throws Exception {
        runTest("deprecated: isFunction(custom @@toStringTag)");
    }

    /**
     * Test {143=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented({ CHROME, FF })
    public void deferred__jQuery_Deferred_exceptionHook() throws Exception {
        runTest("deferred: jQuery.Deferred.exceptionHook");
    }

    /**
     * Test {143=[FF68], 170=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "14",
            IE = "14")
    public void deprecated__jQuery_isWindow() throws Exception {
        runTest("deprecated: jQuery.isWindow");
    }

    /**
     * Test {143=[CHROME], 163=[FF68], 194=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "40",
            FF60 = "46",
            FF68 = "1, 42, 43",
            IE = "46")
    public void data__data___attributes() throws Exception {
        runTest("data: data-* attributes");
    }

    /**
     * Test {144=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void deferred__jQuery_Deferred_exceptionHook_with_stack_hooks() throws Exception {
        runTest("deferred: jQuery.Deferred.exceptionHook with stack hooks");
    }

    /**
     * Test {144=[FF68], 171=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "7",
            IE = "7")
    public void deprecated__jQuery_camelCase__() throws Exception {
        runTest("deprecated: jQuery.camelCase()");
    }

    /**
     * Test {144=[CHROME], 164=[FF68], 195=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void data___data_Object_() throws Exception {
        runTest("data: .data(Object)");
    }

    /**
     * Test {145=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "8",
            IE = "8")
    public void deferred__jQuery_Deferred___1_x_2_x_compatibility() throws Exception {
        runTest("deferred: jQuery.Deferred - 1.x/2.x compatibility");
    }

    /**
     * Test {145=[FF68], 172=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void deprecated__jQuery_now() throws Exception {
        runTest("deprecated: jQuery.now");
    }

    /**
     * Test {145=[CHROME], 165=[FF68], 196=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void data__jQuery_removeData() throws Exception {
        runTest("data: jQuery.removeData");
    }

    /**
     * Test {146=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void deferred__jQuery_Deferred_then___progress_and_thenables() throws Exception {
        runTest("deferred: jQuery.Deferred.then - progress and thenables");
    }

    /**
     * Test {146=[FF68], 173=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "9",
            IE = "9")
    public void deprecated__jQuery_proxy() throws Exception {
        runTest("deprecated: jQuery.proxy");
    }

    /**
     * Test {146=[CHROME], 166=[FF68], 197=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void data___removeData__() throws Exception {
        runTest("data: .removeData()");
    }

    /**
     * Test {147=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "7",
            IE = "7")
    public void deferred__jQuery_Deferred___notify_and_resolve() throws Exception {
        runTest("deferred: jQuery.Deferred - notify and resolve");
    }

    /**
     * Test {147=[FF68], 174=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "43",
            IE = "43")
    public void deprecated__isNumeric() throws Exception {
        runTest("deprecated: isNumeric");
    }

    /**
     * Test {147=[CHROME], 167=[FF68], 198=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void data__JSON_serialization___8108_() throws Exception {
        runTest("data: JSON serialization (#8108)");
    }

    /**
     * Test {148=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void deferred__jQuery_Deferred___resolved_to_a_notifying_deferred() throws Exception {
        runTest("deferred: jQuery.Deferred - resolved to a notifying deferred");
    }

    /**
     * Test {148=[CHROME], 168=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "10",
            FF68 = "10")
    public void data__jQuery_data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: jQuery.data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {148=[FF68], 175=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "0")
    public void deprecated__isNumeric_Symbol_() throws Exception {
        runTest("deprecated: isNumeric(Symbol)");
    }

    /**
     * Test {149=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "44",
            IE = "44")
    public void deferred__jQuery_when_nonThenable____like_Promise_resolve() throws Exception {
        runTest("deferred: jQuery.when(nonThenable) - like Promise.resolve");
    }

    /**
     * Test {149=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "1")
    public void support__zoom_of_doom___13089_() throws Exception {
        runTest("support: zoom of doom (#13089)");
    }

    /**
     * Test {149=[CHROME], 169=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF68 = "2")
    public void data__jQuery_data_should_not_miss_data_with_preset_hyphenated_property_names() throws Exception {
        runTest("data: jQuery.data should not miss data with preset hyphenated property names");
    }

    /**
     * Test {150=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "68",
            IE = "68")
    @NotYetImplemented({ CHROME, FF })
    public void deferred__jQuery_when_thenable____like_Promise_resolve() throws Exception {
        runTest("deferred: jQuery.when(thenable) - like Promise.resolve");
    }

    /**
     * Test {150=[CHROME], 170=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "24",
            FF68 = "24")
    public void data__jQuery_data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {151=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "196",
            IE = "196")
    @NotYetImplemented({ CHROME, FF })
    public void deferred__jQuery_when_a__b____like_Promise_all() throws Exception {
        runTest("deferred: jQuery.when(a, b) - like Promise.all");
    }

    /**
     * Test {151=[CHROME], 171=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "27",
            FF68 = "27")
    public void data__jQuery_data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: jQuery.data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {152=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "42",
            IE = "42")
    public void deferred__jQuery_when___always_returns_a_new_promise() throws Exception {
        runTest("deferred: jQuery.when - always returns a new promise");
    }

    /**
     * Test {152=[CHROME], 173=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 3, 5",
            FF68 = "1")
    public void data__Triggering_the_removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: Triggering the removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {152=[FF68], 183=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "25",
            FF68 = "2, 25, 27",
            IE = "25")
    public void data__jQuery_data_div_() throws Exception {
        runTest("data: jQuery.data(div)");
    }

    /**
     * Test {153=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void deferred__jQuery_when___notify_does_not_affect_resolved() throws Exception {
        runTest("deferred: jQuery.when - notify does not affect resolved");
    }

    /**
     * Test {153=[CHROME], 174=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF68 = "2")
    public void data__Only_check_element_attributes_once_when_calling__data______8909() throws Exception {
        runTest("data: Only check element attributes once when calling .data() - #8909");
    }

    /**
     * Test {153=[FF68], 184=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "25",
            IE = "25")
    public void data__jQuery_data____() throws Exception {
        runTest("data: jQuery.data({})");
    }

    /**
     * Test {154=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            IE = "5")
    public void deferred__jQuery_when________opportunistically_synchronous() throws Exception {
        runTest("deferred: jQuery.when(...) - opportunistically synchronous");
    }

    /**
     * Test {154=[CHROME], 175=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF68 = "1")
    public void data__JSON_data__attributes_can_have_newlines() throws Exception {
        runTest("data: JSON data- attributes can have newlines");
    }

    /**
     * Test {154=[FF68], 185=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "25",
            IE = "25")
    public void data__jQuery_data_window_() throws Exception {
        runTest("data: jQuery.data(window)");
    }

    /**
     * Test {155=[FF68], 186=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "25",
            FF68 = "2, 25, 27",
            IE = "25")
    public void data__jQuery_data_document_() throws Exception {
        runTest("data: jQuery.data(document)");
    }

    /**
     * Test {155=[CHROME], 177=[FF68], 219=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void queue__queue___with_other_types() throws Exception {
        runTest("queue: queue() with other types");
    }

    /**
     * Test {156=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2, 2, 4")
    public void data__Expando_cleanup() throws Exception {
        runTest("data: Expando cleanup");
    }

    /**
     * Test {156=[CHROME], 178=[FF68], 220=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue__queue_name__passes_in_the_next_item_in_the_queue_as_a_parameter() throws Exception {
        runTest("queue: queue(name) passes in the next item in the queue as a parameter");
    }

    /**
     * Test {157=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2")
    public void data__Data_is_not_being_set_on_comment_and_text_nodes() throws Exception {
        runTest("data: Data is not being set on comment and text nodes");
    }

    /**
     * Test {157=[CHROME], 179=[FF68], 221=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void queue__queue___passes_in_the_next_item_in_the_queue_as_a_parameter_to_fx_queues() throws Exception {
        runTest("queue: queue() passes in the next item in the queue as a parameter to fx queues");
    }

    /**
     * Test {158=[CHROME], 180=[FF68], 222=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void queue__callbacks_keep_their_place_in_the_queue() throws Exception {
        runTest("queue: callbacks keep their place in the queue");
    }

    /**
     * Test {159=[FF68], 189=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void data__jQuery___data_______undefined___14101_() throws Exception {
        runTest("data: jQuery().data() === undefined (#14101)");
    }

    /**
     * Test {159=[CHROME], 181=[FF68], 224=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue__delay__() throws Exception {
        runTest("queue: delay()");
    }

    /**
     * Test {160=[CHROME], 182=[FF68], 225=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue__clearQueue_name__clears_the_queue() throws Exception {
        runTest("queue: clearQueue(name) clears the queue");
    }

    /**
     * Test {161=[FF68], 191=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "18",
            IE = "18")
    public void data__jQuery_Element__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(Element).data(String, Object).data(String)");
    }

    /**
     * Test {161=[CHROME], 183=[FF68], 226=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void queue__clearQueue___clears_the_fx_queue() throws Exception {
        runTest("queue: clearQueue() clears the fx queue");
    }

    /**
     * Test {162=[FF68], 192=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "16",
            IE = "16")
    public void data__jQuery_plain_Object__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(plain Object).data(String, Object).data(String)");
    }

    /**
     * Test {162=[CHROME], 184=[FF68], 227=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "3",
            CHROME = "2, 0, 2")
    public void queue__fn_promise_____called_when_fx_queue_is_empty() throws Exception {
        runTest("queue: fn.promise() - called when fx queue is empty");
    }

    /**
     * Test {163=[CHROME], 185=[FF68], 228=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void queue__fn_promise___queue______called_whenever_last_queue_function_is_dequeued() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued");
    }

    /**
     * Test {164=[CHROME], 186=[FF68], 229=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    @NotYetImplemented({ CHROME, FF60 })
    public void queue__fn_promise___queue______waits_for_animation_to_complete_before_resolving() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving");
    }

    /**
     * Test {165=[CHROME], 187=[FF68], 230=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue___promise_obj_() throws Exception {
        runTest("queue: .promise(obj)");
    }

    /**
     * Test {166=[CHROME], 188=[FF68], 231=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void queue__delay___can_be_stopped() throws Exception {
        runTest("queue: delay() can be stopped");
    }

    /**
     * Test {167=[CHROME], 189=[FF68], 232=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void queue__queue_stop_hooks() throws Exception {
        runTest("queue: queue stop hooks");
    }

    /**
     * Test {168=[CHROME], 190=[FF68], 233=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void attributes__jQuery_propFix_integrity_test() throws Exception {
        runTest("attributes: jQuery.propFix integrity test");
    }

    /**
     * Test {169=[CHROME], 191=[FF68], 234=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "50",
            CHROME = "3, 30, 33")
    public void attributes__attr_String_() throws Exception {
        runTest("attributes: attr(String)");
    }

    /**
     * Test {170=[CHROME], 193=[FF68], 236=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void attributes__attr_String__in_XML_Files() throws Exception {
        runTest("attributes: attr(String) in XML Files");
    }

    /**
     * Test {171=[CHROME], 194=[FF68], 237=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void attributes__attr_String__Function_() throws Exception {
        runTest("attributes: attr(String, Function)");
    }

    /**
     * Test {172=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2, 2, 4")
    public void data___removeData_supports_removal_of_hyphenated_properties_via_array___12786_() throws Exception {
        runTest("data: .removeData supports removal of hyphenated properties via array (#12786)");
    }

    /**
     * Test {172=[CHROME], 195=[FF68], 238=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void attributes__attr_Hash_() throws Exception {
        runTest("attributes: attr(Hash)");
    }

    /**
     * Test {173=[CHROME], 196=[FF68], 239=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "71",
            CHROME = "2, 0, 2")
    public void attributes__attr_String__Object_() throws Exception {
        runTest("attributes: attr(String, Object)");
    }

    /**
     * Test {174=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9, 0, 9")
    public void attributes__attr_jquery_method_() throws Exception {
        runTest("attributes: attr(jquery_method)");
    }

    /**
     * Test {175=[CHROME], 198=[FF68], 242=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void attributes__attr_String__Object____Loaded_via_XML_document() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML document");
    }

    /**
     * Test {176=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2, 0, 2")
    public void data__global_failure() throws Exception {
        runTest("data: global failure");
    }

    /**
     * Test {176=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void support__body_background_is_not_lost_if_set_prior_to_loading_jQuery___9239_() throws Exception {
        runTest("support: body background is not lost if set prior to loading jQuery (#9239)");
    }

    /**
     * Test {176=[CHROME], 200=[FF68], 244=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void attributes__attr__tabindex__() throws Exception {
        runTest("attributes: attr('tabindex')");
    }

    /**
     * Test {177=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1, 1, 2",
            IE = "1, 1, 2")
    @NotYetImplemented
    public void support__Check_CSP__https___developer_mozilla_org_en_US_docs_Security_CSP__restrictions() throws Exception {
        runTest("support: Check CSP (https://developer.mozilla.org/en-US/docs/Security/CSP) restrictions");
    }

    /**
     * Test {177=[CHROME], 201=[FF68], 245=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void attributes__attr__tabindex___value_() throws Exception {
        runTest("attributes: attr('tabindex', value)");
    }

    /**
     * Test {178=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "15",
            IE = "15")
    @NotYetImplemented
    public void support__Verify_that_support_tests_resolve_as_expected_per_browser() throws Exception {
        runTest("support: Verify that support tests resolve as expected per browser");
    }

    /**
     * Test {178=[CHROME], 202=[FF68], 246=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 11, 12",
            FF60 = "12",
            FF68 = "1, 11, 12",
            IE = "12")
    public void attributes__removeAttr_String_() throws Exception {
        runTest("attributes: removeAttr(String)");
    }

    /**
     * Test {179=[CHROME], 203=[FF68], 247=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void attributes__removeAttr_String__in_XML() throws Exception {
        runTest("attributes: removeAttr(String) in XML");
    }

    /**
     * Test {180=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void data__jQuery_data___removeData__expected_returns() throws Exception {
        runTest("data: jQuery.data & removeData, expected returns");
    }

    /**
     * Test {180=[CHROME], 204=[FF68], 248=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void attributes__removeAttr_Multi_String__variable_space_width_() throws Exception {
        runTest("attributes: removeAttr(Multi String, variable space width)");
    }

    /**
     * Test {181=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void data__jQuery__data____removeData__expected_returns() throws Exception {
        runTest("data: jQuery._data & _removeData, expected returns");
    }

    /**
     * Test {181=[CHROME], 205=[FF68], 250=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "17",
            CHROME = "1, 30, 31")
    public void attributes__prop_String__Object_() throws Exception {
        runTest("attributes: prop(String, Object)");
    }

    /**
     * Test {182=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void data__jQuery_hasData_no_side_effects() throws Exception {
        runTest("data: jQuery.hasData no side effects");
    }

    /**
     * Test {182=[CHROME], 207=[FF68], 252=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "11",
            CHROME = "2, 6, 8")
    public void attributes__prop__tabindex__() throws Exception {
        runTest("attributes: prop('tabindex')");
    }

    /**
     * Test {183=[CHROME], 208=[FF68], 254=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "10",
            CHROME = "1, 8, 9")
    public void attributes__prop__tabindex___value_() throws Exception {
        runTest("attributes: prop('tabindex', value)");
    }

    /**
     * Test {184=[CHROME], 209=[FF68], 256=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void attributes__removeProp_String_() throws Exception {
        runTest("attributes: removeProp(String)");
    }

    /**
     * Test {185=[CHROME], 211=[FF68], 258=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("26")
    @NotYetImplemented
    public void attributes__val__() throws Exception {
        runTest("attributes: val()");
    }

    /**
     * Test {186=[CHROME], 213=[FF68], 260=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF = "4")
    public void attributes__val___respects_numbers_without_exception__Bug__9319_() throws Exception {
        runTest("attributes: val() respects numbers without exception (Bug #9319)");
    }

    /**
     * Test {187=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "25",
            IE = "25")
    public void data__jQuery_data__embed__() throws Exception {
        runTest("data: jQuery.data(<embed>)");
    }

    /**
     * Test {187=[CHROME], 214=[FF68], 260=[IE], 261=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "9",
            CHROME = "8")
    public void attributes__val_String_Number_() throws Exception {
        runTest("attributes: val(String/Number)");
    }

    /**
     * Test {188=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "25",
            IE = "25")
    public void data__jQuery_data_object_flash_() throws Exception {
        runTest("data: jQuery.data(object/flash)");
    }

    /**
     * Test {188=[CHROME], 215=[FF68], 261=[IE], 262=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "9",
            CHROME = "8")
    public void attributes__val_Function_() throws Exception {
        runTest("attributes: val(Function)");
    }

    /**
     * Test {189=[CHROME], 216=[FF68], 262=[IE], 263=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void attributes__val_Array_of_Numbers___Bug__7123_() throws Exception {
        runTest("attributes: val(Array of Numbers) (Bug #7123)");
    }

    /**
     * Test {190=[CHROME], 217=[FF68], 263=[IE], 264=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("10")
    public void attributes__val_Function__with_incoming_value() throws Exception {
        runTest("attributes: val(Function) with incoming value");
    }

    /**
     * Test {191=[CHROME], 218=[FF68], 264=[IE], 265=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void attributes__val_select__after_form_reset____Bug__2551_() throws Exception {
        runTest("attributes: val(select) after form.reset() (Bug #2551)");
    }

    /**
     * Test {192=[FF68], 235=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void attributes__attr_String__on_cloned_elements___9646() throws Exception {
        runTest("attributes: attr(String) on cloned elements, #9646");
    }

    /**
     * Test {192=[CHROME], 219=[FF68], 266=[IE], 267=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "9",
            CHROME = "2, 0, 2")
    public void attributes__addClass_String_() throws Exception {
        runTest("attributes: addClass(String)");
    }

    /**
     * Test {193=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void data___data_object__does_not_retain_references___13815() throws Exception {
        runTest("data: .data(object) does not retain references. #13815");
    }

    /**
     * Test {193=[CHROME], 220=[FF68], 267=[IE], 268=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "9",
            CHROME = "2, 0, 2")
    public void attributes__addClass_Function_() throws Exception {
        runTest("attributes: addClass(Function)");
    }

    /**
     * Test {194=[CHROME], 221=[FF68], 269=[IE], 270=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "52",
            CHROME = "1, 56, 57")
    public void attributes__addClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: addClass(Function) with incoming value");
    }

    /**
     * Test {195=[CHROME], 222=[FF68], 270=[IE], 271=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "8",
            CHROME = "7")
    public void attributes__removeClass_String____simple() throws Exception {
        runTest("attributes: removeClass(String) - simple");
    }

    /**
     * Test {196=[CHROME], 223=[FF68], 271=[IE], 272=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "8",
            CHROME = "7")
    public void attributes__removeClass_Function____simple() throws Exception {
        runTest("attributes: removeClass(Function) - simple");
    }

    /**
     * Test {197=[FF68], 241=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void attributes__attr___extending_the_boolean_attrHandle() throws Exception {
        runTest("attributes: attr - extending the boolean attrHandle");
    }

    /**
     * Test {197=[CHROME], 224=[FF68], 273=[IE], 274=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "52",
            CHROME = "1, 56, 57")
    public void attributes__removeClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: removeClass(Function) with incoming value");
    }

    /**
     * Test {198=[CHROME], 225=[FF68], 274=[IE], 275=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void attributes__removeClass___removes_duplicates() throws Exception {
        runTest("attributes: removeClass() removes duplicates");
    }

    /**
     * Test {199=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "12",
            IE = "12")
    public void data___data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: .data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {199=[FF68], 243=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void attributes__attr_String__Object____Loaded_via_XML_fragment() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML fragment");
    }

    /**
     * Test {199=[CHROME], 227=[FF68], 276=[IE], 277=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "17",
            FF60 = "19",
            FF68 = "2, 17, 19",
            IE = "19")
    public void attributes__toggleClass_String_boolean_undefined___boolean__() throws Exception {
        runTest("attributes: toggleClass(String|boolean|undefined[, boolean])");
    }

    /**
     * Test {200=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void data___data_should_not_miss_preset_data___w__hyphenated_property_names() throws Exception {
        runTest("data: .data should not miss preset data-* w/ hyphenated property names");
    }

    /**
     * Test {200=[CHROME], 228=[FF68], 277=[IE], 278=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "17",
            FF60 = "19",
            FF68 = "2, 17, 19",
            IE = "19")
    public void attributes__toggleClass_Function___boolean__() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean])");
    }

    /**
     * Test {201=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "14")
    public void attributes__toggleClass_Fucntion___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Fucntion[, boolean]) with incoming value");
    }

    /**
     * Test {201=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void data__jQuery_data_should_not_miss_data___w__hyphenated_property_names__14047() throws Exception {
        runTest("data: jQuery.data should not miss data-* w/ hyphenated property names #14047");
    }

    /**
     * Test {202=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void data___data_should_not_miss_attr___set_data___with_hyphenated_property_names() throws Exception {
        runTest("data: .data should not miss attr() set data-* with hyphenated property names");
    }

    /**
     * Test {202=[CHROME], 230=[FF68], 280=[IE], 281=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("17")
    public void attributes__addClass__removeClass__hasClass() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass");
    }

    /**
     * Test {203=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "18",
            IE = "18")
    public void data___data_always_sets_data_with_the_camelCased_key__gh_2257_() throws Exception {
        runTest("data: .data always sets data with the camelCased key (gh-2257)");
    }

    /**
     * Test {203=[CHROME], 232=[FF68], 284=[IE], 285=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void attributes__contents___hasClass___returns_correct_values() throws Exception {
        runTest("attributes: contents().hasClass() returns correct values");
    }

    /**
     * Test {204=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void data___data_should_not_strip_more_than_one_hyphen_when_camelCasing__gh_2070_() throws Exception {
        runTest("data: .data should not strip more than one hyphen when camelCasing (gh-2070)");
    }

    /**
     * Test {204=[CHROME], 234=[FF68], 286=[IE], 287=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = "2")
    public void attributes__coords_returns_correct_values_in_IE6_IE7__see__10828() throws Exception {
        runTest("attributes: coords returns correct values in IE6/IE7, see #10828");
    }

    /**
     * Test {205=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "24",
            IE = "24")
    public void data___data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: .data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {205=[CHROME], 237=[FF68], 292=[IE], 293=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF60 = "4",
            FF68 = "2",
            IE = "4")
    public void event__null_or_undefined_handler() throws Exception {
        runTest("event: null or undefined handler");
    }

    /**
     * Test {206=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "27",
            IE = "27")
    public void data___data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: .data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {206=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void event__bind___live___delegate___with_non_null_defined_data() throws Exception {
        runTest("event: bind(),live(),delegate() with non-null,defined data");
    }

    /**
     * Test {206=[FF68], 251=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "14",
            IE = "14")
    public void attributes__prop_String__Object__on_null_undefined() throws Exception {
        runTest("attributes: prop(String, Object) on null/undefined");
    }

    /**
     * Test {207=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "9",
            IE = "9")
    public void data___data_supports_interoperable_removal_of_properties_SET_TWICE__13850() throws Exception {
        runTest("data: .data supports interoperable removal of properties SET TWICE #13850");
    }

    /**
     * Test {207=[CHROME], 239=[FF68], 294=[IE], 295=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = "1, 0, 1")
    public void event__Handler_changes_and__trigger___order() throws Exception {
        runTest("event: Handler changes and .trigger() order");
    }

    /**
     * Test {208=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void data___removeData_supports_removal_of_hyphenated_properties_via_array___12786__gh_2257_() throws Exception {
        runTest("data: .removeData supports removal of hyphenated properties via array (#12786, gh-2257)");
    }

    /**
     * Test {208=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4")
    public void event__bind____with_data() throws Exception {
        runTest("event: bind(), with data");
    }

    /**
     * Test {209=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void data___removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: .removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {209=[CHROME], 241=[FF68], 296=[IE], 297=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__click____with_data() throws Exception {
        runTest("event: click(), with data");
    }

    /**
     * Test {210=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void data___data_only_checks_element_attributes_once___8909() throws Exception {
        runTest("data: .data only checks element attributes once. #8909");
    }

    /**
     * Test {210=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4")
    public void event__bind____with_data__trigger_with_data() throws Exception {
        runTest("event: bind(), with data, trigger with data");
    }

    /**
     * Test {210=[FF68], 257=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void attributes__val___after_modification() throws Exception {
        runTest("attributes: val() after modification");
    }

    /**
     * Test {211=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void data__data___with_JSON_value_can_have_newlines() throws Exception {
        runTest("data: data-* with JSON value can have newlines");
    }

    /**
     * Test {211=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void event__bind____multiple_events_at_once() throws Exception {
        runTest("event: bind(), multiple events at once");
    }

    /**
     * Test {212=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void data___data_doesn_t_throw_when_calling_selection_is_empty___13551() throws Exception {
        runTest("data: .data doesn't throw when calling selection is empty. #13551");
    }

    /**
     * Test {212=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void event__bind____five_events_at_once() throws Exception {
        runTest("event: bind(), five events at once");
    }

    /**
     * Test {212=[FF68], 259=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "2, 1, 3",
            IE = "3")
    public void attributes__val___with_non_matching_values_on_dropdown_list() throws Exception {
        runTest("attributes: val() with non-matching values on dropdown list");
    }

    /**
     * Test {213=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "10",
            IE = "10")
    public void data__acceptData() throws Exception {
        runTest("data: acceptData");
    }

    /**
     * Test {213=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "7")
    public void event__bind____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: bind(), multiple events at once and namespaces");
    }

    /**
     * Test {214=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void data__Check_proper_data_removal_of_non_element_descendants_nodes___8335_() throws Exception {
        runTest("data: Check proper data removal of non-element descendants nodes (#8335)");
    }

    /**
     * Test {214=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 24, 27")
    public void event__bind____namespace_with_special_add() throws Exception {
        runTest("event: bind(), namespace with special add");
    }

    /**
     * Test {215=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void data__enumerate_data_attrs_on_body___14894_() throws Exception {
        runTest("data: enumerate data attrs on body (#14894)");
    }

    /**
     * Test {215=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void event__bind____no_data() throws Exception {
        runTest("event: bind(), no data");
    }

    /**
     * Test {216=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void data__Check_that_the_expando_is_removed_when_there_s_no_more_data() throws Exception {
        runTest("data: Check that the expando is removed when there's no more data");
    }

    /**
     * Test {216=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6")
    public void event__bind_one_unbind_Object_() throws Exception {
        runTest("event: bind/one/unbind(Object)");
    }

    /**
     * Test {217=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void data__Check_that_the_expando_is_removed_when_there_s_no_more_data_on_non_nodes() throws Exception {
        runTest("data: Check that the expando is removed when there's no more data on non-nodes");
    }

    /**
     * Test {217=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void event__live_die_Object___delegate_undelegate_String__Object_() throws Exception {
        runTest("event: live/die(Object), delegate/undelegate(String, Object)");
    }

    /**
     * Test {218=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void data___data_prop__does_not_create_expando() throws Exception {
        runTest("data: .data(prop) does not create expando");
    }

    /**
     * Test {218=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void event__live_delegate_immediate_propagation() throws Exception {
        runTest("event: live/delegate immediate propagation");
    }

    /**
     * Test {219=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void event__bind_delegate_bubbling__isDefaultPrevented() throws Exception {
        runTest("event: bind/delegate bubbling, isDefaultPrevented");
    }

    /**
     * Test {220=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 1, 2")
    public void event__bind____iframes() throws Exception {
        runTest("event: bind(), iframes");
    }

    /**
     * Test {221=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void event__bind____trigger_change_on_select() throws Exception {
        runTest("event: bind(), trigger change on select");
    }

    /**
     * Test {222=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "18")
    public void event__bind____namespaced_events__cloned_events() throws Exception {
        runTest("event: bind(), namespaced events, cloned events");
    }

    /**
     * Test {223=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6")
    public void event__bind____multi_namespaced_events() throws Exception {
        runTest("event: bind(), multi-namespaced events");
    }

    /**
     * Test {223=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void queue__jQuery_queue_should_return_array_while_manipulating_the_queue() throws Exception {
        runTest("queue: jQuery.queue should return array while manipulating the queue");
    }

    /**
     * Test {224=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void event__bind____with_same_function() throws Exception {
        runTest("event: bind(), with same function");
    }

    /**
     * Test {225=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void event__bind____make_sure_order_is_maintained() throws Exception {
        runTest("event: bind(), make sure order is maintained");
    }

    /**
     * Test {226=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4")
    public void event__bind____with_different_this_object() throws Exception {
        runTest("event: bind(), with different this object");
    }

    /**
     * Test {226=[FF68], 275=[IE], 276=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void attributes__removeClass_undefined__is_a_no_op() throws Exception {
        runTest("attributes: removeClass(undefined) is a no-op");
    }

    /**
     * Test {227=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3")
    public void event__bind_name__false___unbind_name__false_() throws Exception {
        runTest("event: bind(name, false), unbind(name, false)");
    }

    /**
     * Test {228=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void event__live_name__false___die_name__false_() throws Exception {
        runTest("event: live(name, false), die(name, false)");
    }

    /**
     * Test {229=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3")
    public void event__delegate_selector__name__false___undelegate_selector__name__false_() throws Exception {
        runTest("event: delegate(selector, name, false), undelegate(selector, name, false)");
    }

    /**
     * Test {229=[FF68], 279=[IE], 280=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "14",
            IE = "14")
    public void attributes__toggleClass_Function___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean]) with incoming value");
    }

    /**
     * Test {230=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "7")
    public void event__bind___trigger___unbind___on_plain_object() throws Exception {
        runTest("event: bind()/trigger()/unbind() on plain object");
    }

    /**
     * Test {231=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void event__unbind_type_() throws Exception {
        runTest("event: unbind(type)");
    }

    /**
     * Test {231=[FF68], 281=[IE], 282=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "19",
            IE = "19")
    public void attributes__addClass__removeClass__hasClass_on_many_elements() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass on many elements");
    }

    /**
     * Test {232=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4")
    public void event__unbind_eventObject_() throws Exception {
        runTest("event: unbind(eventObject)");
    }

    /**
     * Test {233=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3")
    public void event__hover___and_hover_pseudo_event() throws Exception {
        runTest("event: hover() and hover pseudo-event");
    }

    /**
     * Test {233=[FF68], 285=[IE], 286=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void attributes__hasClass_correctly_interprets_non_space_separators___13835_() throws Exception {
        runTest("attributes: hasClass correctly interprets non-space separators (#13835)");
    }

    /**
     * Test {234=[CHROME], 266=[FF68], 323=[IE], 324=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__mouseover_triggers_mouseenter() throws Exception {
        runTest("event: mouseover triggers mouseenter");
    }

    /**
     * Test {235=[FF68], 287=[IE], 288=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void attributes__should_not_throw_at___option__val_____14686_() throws Exception {
        runTest("attributes: should not throw at $(option).val() (#14686)");
    }

    /**
     * Test {235=[CHROME], 268=[FF68], 325=[IE], 326=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__withinElement_implemented_with_jQuery_contains__() throws Exception {
        runTest("event: withinElement implemented with jQuery.contains()");
    }

    /**
     * Test {236=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "3")
    public void attributes__Insignificant_white_space_returned_for___option__val_____14858_() throws Exception {
        runTest("attributes: Insignificant white space returned for $(option).val() (#14858)");
    }

    /**
     * Test {236=[CHROME], 269=[FF68], 326=[IE], 327=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__mouseenter__mouseleave_don_t_catch_exceptions() throws Exception {
        runTest("event: mouseenter, mouseleave don't catch exceptions");
    }

    /**
     * Test {237=[CHROME], 270=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 5, 6",
            FF68 = "1, 5, 6")
    public void event__trigger___shortcuts() throws Exception {
        runTest("event: trigger() shortcuts");
    }

    /**
     * Test {238=[FF68], 293=[IE], 294=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__on___with_non_null_defined_data() throws Exception {
        runTest("event: on() with non-null,defined data");
    }

    /**
     * Test {238=[CHROME], 271=[FF68], 327=[IE], 328=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("18")
    public void event__trigger___bubbling() throws Exception {
        runTest("event: trigger() bubbling");
    }

    /**
     * Test {239=[CHROME], 272=[FF68], 328=[IE], 329=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "16",
            CHROME = "2, 0, 2")
    public void event__trigger_type___data____fn__() throws Exception {
        runTest("event: trigger(type, [data], [fn])");
    }

    /**
     * Test {240=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void attributes__attr_non_ASCII_() throws Exception {
        runTest("attributes: attr(non-ASCII)");
    }

    /**
     * Test {240=[FF68], 295=[IE], 296=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void event__on____with_data() throws Exception {
        runTest("event: on(), with data");
    }

    /**
     * Test {240=[CHROME], 273=[FF68], 329=[IE], 330=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented
    public void event__submit_event_bubbles_on_copied_forms___11649_() throws Exception {
        runTest("event: submit event bubbles on copied forms (#11649)");
    }

    /**
     * Test {241=[CHROME], 274=[FF68], 330=[IE], 331=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    @NotYetImplemented
    public void event__change_event_bubbles_on_copied_forms___11796_() throws Exception {
        runTest("event: change event bubbles on copied forms (#11796)");
    }

    /**
     * Test {242=[FF68], 297=[IE], 298=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void event__on____with_data__trigger_with_data() throws Exception {
        runTest("event: on(), with data, trigger with data");
    }

    /**
     * Test {242=[CHROME], 275=[FF68], 331=[IE], 332=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("28")
    public void event__trigger_eventObject___data____fn__() throws Exception {
        runTest("event: trigger(eventObject, [data], [fn])");
    }

    /**
     * Test {243=[FF68], 298=[IE], 299=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__on____multiple_events_at_once() throws Exception {
        runTest("event: on(), multiple events at once");
    }

    /**
     * Test {243=[CHROME], 276=[FF68], 332=[IE], 333=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event___trigger___bubbling_on_disconnected_elements___10489_() throws Exception {
        runTest("event: .trigger() bubbling on disconnected elements (#10489)");
    }

    /**
     * Test {244=[FF68], 299=[IE], 300=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void event__on____five_events_at_once() throws Exception {
        runTest("event: on(), five events at once");
    }

    /**
     * Test {244=[CHROME], 277=[FF68], 333=[IE], 334=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event___trigger___doesn_t_bubble_load_event___10717_() throws Exception {
        runTest("event: .trigger() doesn't bubble load event (#10717)");
    }

    /**
     * Test {245=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void event__Delegated_events_in_SVG___10791_() throws Exception {
        runTest("event: Delegated events in SVG (#10791)");
    }

    /**
     * Test {245=[FF68], 300=[IE], 301=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "7",
            IE = "7")
    public void event__on____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: on(), multiple events at once and namespaces");
    }

    /**
     * Test {246=[FF68], 301=[IE], 302=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "27",
            IE = "27")
    public void event__on____namespace_with_special_add() throws Exception {
        runTest("event: on(), namespace with special add");
    }

    /**
     * Test {246=[CHROME], 279=[FF68], 336=[IE], 337=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void event__Delegated_events_in_forms___10844___11145___8165___11382___11764_() throws Exception {
        runTest("event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764)");
    }

    /**
     * Test {247=[FF68], 302=[IE], 303=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void event__on____no_data() throws Exception {
        runTest("event: on(), no data");
    }

    /**
     * Test {247=[CHROME], 280=[FF68], 337=[IE], 338=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__Submit_event_can_be_stopped___11049_() throws Exception {
        runTest("event: Submit event can be stopped (#11049)");
    }

    /**
     * Test {248=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 2, 3")
    public void event__on_beforeunload__creates_deletes_window_property_instead_of_adding_removing_event_listener() throws Exception {
        runTest("event: on(beforeunload) creates/deletes window property instead of adding/removing event listener");
    }

    /**
     * Test {248=[FF68], 303=[IE], 304=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "6",
            IE = "6")
    public void event__on_one_off_Object_() throws Exception {
        runTest("event: on/one/off(Object)");
    }

    /**
     * Test {249=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "8",
            IE = "8")
    public void attributes__removeAttr_Multi_String__non_HTML_whitespace_is_valid_in_attribute_names__gh_3003_() throws Exception {
        runTest("attributes: removeAttr(Multi String, non-HTML whitespace is valid in attribute names (gh-3003)");
    }

    /**
     * Test {249=[FF68], 304=[IE], 305=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "6",
            IE = "6")
    public void event__on_off_Object___on_off_Object__String_() throws Exception {
        runTest("event: on/off(Object), on/off(Object, String)");
    }

    /**
     * Test {249=[CHROME], 282=[FF68], 339=[IE], 340=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5",
            FF60 = "6",
            FF68 = "5",
            IE = "6")
    public void event__jQuery_Event__type__props__() throws Exception {
        runTest("event: jQuery.Event( type, props )");
    }

    /**
     * Test {250=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void event__jQuery_Event_currentTarget() throws Exception {
        runTest("event: jQuery.Event.currentTarget");
    }

    /**
     * Test {250=[FF68], 305=[IE], 306=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__on_immediate_propagation() throws Exception {
        runTest("event: on immediate propagation");
    }

    /**
     * Test {251=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "13, 2, 15")
    public void event__toggle_Function__Function______() throws Exception {
        runTest("event: toggle(Function, Function, ...)");
    }

    /**
     * Test {251=[FF68], 306=[IE], 307=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "3",
            IE = "3")
    public void event__on_bubbling__isDefaultPrevented__stopImmediatePropagation() throws Exception {
        runTest("event: on bubbling, isDefaultPrevented, stopImmediatePropagation");
    }

    /**
     * Test {252=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void event___live____die__() throws Exception {
        runTest("event: .live()/.die()");
    }

    /**
     * Test {252=[FF68], 309=[IE], 310=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void event__on____iframes() throws Exception {
        runTest("event: on(), iframes");
    }

    /**
     * Test {253=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void attributes__image_prop___tabIndex___() throws Exception {
        runTest("attributes: image.prop( 'tabIndex' )");
    }

    /**
     * Test {253=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void event__die_all_bound_events() throws Exception {
        runTest("event: die all bound events");
    }

    /**
     * Test {253=[FF68], 310=[IE], 311=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "5",
            IE = "5")
    public void event__on____trigger_change_on_select() throws Exception {
        runTest("event: on(), trigger change on select");
    }

    /**
     * Test {254=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void event__live_with_multiple_events() throws Exception {
        runTest("event: live with multiple events");
    }

    /**
     * Test {254=[FF68], 311=[IE], 312=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "18",
            IE = "18")
    public void event__on____namespaced_events__cloned_events() throws Exception {
        runTest("event: on(), namespaced events, cloned events");
    }

    /**
     * Test {255=[FF60, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void attributes__option_prop__selected___true__affects_select_selectedIndex__gh_2732_() throws Exception {
        runTest("attributes: option.prop('selected', true) affects select.selectedIndex (gh-2732)");
    }

    /**
     * Test {255=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void event__live_with_namespaces() throws Exception {
        runTest("event: live with namespaces");
    }

    /**
     * Test {255=[FF68], 312=[IE], 313=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "6",
            IE = "6")
    public void event__on____multi_namespaced_events() throws Exception {
        runTest("event: on(), multi-namespaced events");
    }

    /**
     * Test {256=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void event__live_with_change() throws Exception {
        runTest("event: live with change");
    }

    /**
     * Test {256=[FF68], 313=[IE], 314=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__namespace_only_event_binding_is_a_no_op() throws Exception {
        runTest("event: namespace-only event binding is a no-op");
    }

    /**
     * Test {257=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void event__live_with_submit() throws Exception {
        runTest("event: live with submit");
    }

    /**
     * Test {257=[FF68], 315=[IE], 316=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__on____with_same_function() throws Exception {
        runTest("event: on(), with same function");
    }

    /**
     * Test {258=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void event__live_with_special_events() throws Exception {
        runTest("event: live with special events");
    }

    /**
     * Test {258=[FF68], 316=[IE], 317=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void event__on____make_sure_order_is_maintained() throws Exception {
        runTest("event: on(), make sure order is maintained");
    }

    /**
     * Test {259=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6, 64, 70")
    public void event___delegate____undelegate__() throws Exception {
        runTest("event: .delegate()/.undelegate()");
    }

    /**
     * Test {259=[FF68], 317=[IE], 318=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void event__on____with_different_this_object() throws Exception {
        runTest("event: on(), with different this object");
    }

    /**
     * Test {260=[FF68], 318=[IE], 319=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "3",
            IE = "3")
    public void event__on_name__false___off_name__false_() throws Exception {
        runTest("event: on(name, false), off(name, false)");
    }

    /**
     * Test {260=[CHROME], 285=[FF68], 342=[IE], 343=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__jQuery_off_using_dispatched_jQuery_Event() throws Exception {
        runTest("event: jQuery.off using dispatched jQuery.Event");
    }

    /**
     * Test {261=[FF68], 319=[IE], 320=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "3",
            IE = "3")
    public void event__on_name__selector__false___off_name__selector__false_() throws Exception {
        runTest("event: on(name, selector, false), off(name, selector, false)");
    }

    /**
     * Test {261=[CHROME], 286=[FF68], 343=[IE], 344=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__delegated_event_with_delegateTarget_relative_selector() throws Exception {
        runTest("event: delegated event with delegateTarget-relative selector");
    }

    /**
     * Test {262=[FF68], 320=[IE], 321=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "7",
            IE = "7")
    public void event__on___trigger___off___on_plain_object() throws Exception {
        runTest("event: on()/trigger()/off() on plain object");
    }

    /**
     * Test {262=[CHROME], 289=[FF68], 347=[IE], 348=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__stopPropagation___stops_directly_bound_events_on_delegated_target() throws Exception {
        runTest("event: stopPropagation() stops directly-bound events on delegated target");
    }

    /**
     * Test {263=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void event__undelegate_all_bound_events() throws Exception {
        runTest("event: undelegate all bound events");
    }

    /**
     * Test {263=[FF68], 321=[IE], 322=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void event__off_type_() throws Exception {
        runTest("event: off(type)");
    }

    /**
     * Test {264=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void event__delegate_with_multiple_events() throws Exception {
        runTest("event: delegate with multiple events");
    }

    /**
     * Test {264=[FF68], 322=[IE], 323=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void event__off_eventObject_() throws Exception {
        runTest("event: off(eventObject)");
    }

    /**
     * Test {265=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 4, 6")
    public void event__delegate_with_change() throws Exception {
        runTest("event: delegate with change");
    }

    /**
     * Test {265=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "1")
    public void event__hover___mouseenter_mouseleave() throws Exception {
        runTest("event: hover() mouseenter mouseleave");
    }

    /**
     * Test {265=[IE], 266=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "37",
            IE = "37")
    public void attributes__select_val_space_characters___gh_2978_() throws Exception {
        runTest("attributes: select.val(space characters) (gh-2978)");
    }

    /**
     * Test {266=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 2, 3")
    public void event__delegate_with_submit() throws Exception {
        runTest("event: delegate with submit");
    }

    /**
     * Test {267=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void event__undelegate___with_only_namespaces() throws Exception {
        runTest("event: undelegate() with only namespaces");
    }

    /**
     * Test {267=[FF68], 324=[IE], 325=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void event__pointerover_triggers_pointerenter() throws Exception {
        runTest("event: pointerover triggers pointerenter");
    }

    /**
     * Test {268=[IE], 269=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "9",
            IE = "9")
    public void attributes__addClass_Array_() throws Exception {
        runTest("attributes: addClass(Array)");
    }

    /**
     * Test {268=[CHROME], 295=[FF68], 353=[IE], 354=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__Non_DOM_element_events() throws Exception {
        runTest("event: Non DOM element events");
    }

    /**
     * Test {269=[CHROME], 296=[FF68], 354=[IE], 355=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__inline_handler_returning_false_stops_default() throws Exception {
        runTest("event: inline handler returning false stops default");
    }

    /**
     * Test {270=[CHROME], 297=[FF68], 355=[IE], 356=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__window_resize() throws Exception {
        runTest("event: window resize");
    }

    /**
     * Test {271=[CHROME], 298=[FF68], 356=[IE], 357=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void event__focusin_bubbles() throws Exception {
        runTest("event: focusin bubbles");
    }

    /**
     * Test {272=[IE], 273=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "8",
            IE = "8")
    public void attributes__removeClass_Array____simple() throws Exception {
        runTest("attributes: removeClass(Array) - simple");
    }

    /**
     * Test {272=[CHROME], 299=[FF68], 357=[IE], 358=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void event__custom_events_with_colons___3533___8272_() throws Exception {
        runTest("event: custom events with colons (#3533, #8272)");
    }

    /**
     * Test {273=[CHROME], 300=[FF68], 358=[IE], 359=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void event___on_and__off() throws Exception {
        runTest("event: .on and .off");
    }

    /**
     * Test {274=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "7")
    public void event__special_bind_delegate_name_mapping() throws Exception {
        runTest("event: special bind/delegate name mapping");
    }

    /**
     * Test {275=[CHROME], 302=[FF68], 360=[IE], 361=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void event___on_and__off__selective_mixed_removal___10705_() throws Exception {
        runTest("event: .on and .off, selective mixed removal (#10705)");
    }

    /**
     * Test {276=[CHROME], 303=[FF68], 361=[IE], 362=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = "2, 5, 7")
    public void event___on__event_map__null_selector__data____11130() throws Exception {
        runTest("event: .on( event-map, null-selector, data ) #11130");
    }

    /**
     * Test {277=[CHROME], 304=[FF68], 362=[IE], 363=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void event__clone___delegated_events___11076_() throws Exception {
        runTest("event: clone() delegated events (#11076)");
    }

    /**
     * Test {278=[IE], 279=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "19",
            IE = "19")
    public void attributes__toggleClass_Array___boolean__() throws Exception {
        runTest("attributes: toggleClass(Array[, boolean])");
    }

    /**
     * Test {278=[CHROME], 307=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void event__fixHooks_extensions() throws Exception {
        runTest("event: fixHooks extensions");
    }

    /**
     * Test {278=[FF68], 334=[IE], 335=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__Delegated_events_in_SVG___10791___13180_() throws Exception {
        runTest("event: Delegated events in SVG (#10791; #13180)");
    }

    /**
     * Test {279=[CHROME], 309=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void event__global_failure() throws Exception {
        runTest("event: global failure");
    }

    /**
     * Test {280=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4, 3, 7")
    public void selector___jQuery_only__element___jQuery_only() throws Exception {
        runTest("selector - jQuery only: element - jQuery only");
    }

    /**
     * Test {281=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4")
    public void selector___jQuery_only__class___jQuery_only() throws Exception {
        runTest("selector - jQuery only: class - jQuery only");
    }

    /**
     * Test {281=[FF68], 338=[IE], 339=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "4",
            IE = "1")
    @NotYetImplemented
    public void event__on_beforeunload_() throws Exception {
        runTest("event: on(beforeunload)");
    }

    /**
     * Test {282=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void selector___jQuery_only__attributes___jQuery_only() throws Exception {
        runTest("selector - jQuery only: attributes - jQuery only");
    }

    /**
     * Test {282=[IE], 283=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "16",
            IE = "16")
    public void attributes__addClass__removeClass__hasClass_on_many_elements___Array() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass on many elements - Array");
    }

    /**
     * Test {283=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void selector___jQuery_only__pseudo___visibility() throws Exception {
        runTest("selector - jQuery only: pseudo - visibility");
    }

    /**
     * Test {283=[IE], 284=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "9",
            IE = "9")
    public void attributes__addClass__removeClass__hasClass_on_elements_with_classes_with_non_HTML_whitespace__gh_3072__gh_3003_() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass on elements with classes with non-HTML whitespace (gh-3072, gh-3003)");
    }

    /**
     * Test {283=[FF68], 340=[IE], 341=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "12",
            IE = "12")
    public void event__jQuery_Event_properties() throws Exception {
        runTest("event: jQuery.Event properties");
    }

    /**
     * Test {284=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4")
    public void selector___jQuery_only__disconnected_nodes() throws Exception {
        runTest("selector - jQuery only: disconnected nodes");
    }

    /**
     * Test {284=[FF68], 341=[IE], 342=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "65",
            IE = "65")
    public void event___on____off__() throws Exception {
        runTest("event: .on()/.off()");
    }

    /**
     * Test {285=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void selector___jQuery_only__selector_html5_selector() throws Exception {
        runTest("selector - jQuery only: selector/html5_selector");
    }

    /**
     * Test {286=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void selector___jQuery_only__selector_sizzle_cache() throws Exception {
        runTest("selector - jQuery only: selector/sizzle_cache");
    }

    /**
     * Test {287=[FF68], 344=[IE], 345=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void event__delegated_event_with_selector_matching_Object_prototype_property___13203_() throws Exception {
        runTest("event: delegated event with selector matching Object.prototype property (#13203)");
    }

    /**
     * Test {287=[CHROME], 324=[FF68], 405=[IE], 406=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = "1, 4, 5")
    public void traversing__find_String_() throws Exception {
        runTest("traversing: find(String)");
    }

    /**
     * Test {288=[IE], 289=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void attributes__option_value_not_trimmed_when_setting_via_parent_select() throws Exception {
        runTest("attributes: option value not trimmed when setting via parent select");
    }

    /**
     * Test {288=[FF68], 345=[IE], 346=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void event__delegated_event_with_intermediate_DOM_manipulation___13208_() throws Exception {
        runTest("event: delegated event with intermediate DOM manipulation (#13208)");
    }

    /**
     * Test {288=[CHROME], 327=[FF68], 408=[IE], 409=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "11",
            FF60 = "13",
            FF68 = "1, 12, 13",
            IE = "13")
    public void traversing__find_node_jQuery_object_() throws Exception {
        runTest("traversing: find(node|jQuery object)");
    }

    /**
     * Test {289=[IE], 290=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "16",
            IE = "16")
    public void attributes__Insignificant_white_space_returned_for___option__val_____14858__gh_2978_() throws Exception {
        runTest("attributes: Insignificant white space returned for $(option).val() (#14858, gh-2978)");
    }

    /**
     * Test {289=[CHROME], 328=[FF68], 409=[IE], 410=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "23",
            CHROME = "30")
    public void traversing__is_String_undefined_() throws Exception {
        runTest("traversing: is(String|undefined)");
    }

    /**
     * Test {290=[IE], 291=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "12",
            IE = "12")
    public void attributes__SVG_class_manipulation__gh_2199_() throws Exception {
        runTest("attributes: SVG class manipulation (gh-2199)");
    }

    /**
     * Test {290=[FF68], 348=[IE], 349=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__off_all_bound_delegated_events() throws Exception {
        runTest("event: off all bound delegated events");
    }

    /**
     * Test {290=[CHROME], 330=[FF68], 411=[IE], 412=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 20, 21",
            FF60 = "19",
            FF68 = "1, 18, 19",
            IE = "19")
    public void traversing__is_jQuery_() throws Exception {
        runTest("traversing: is(jQuery)");
    }

    /**
     * Test {291=[IE], 292=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void attributes__non_lowercase_boolean_attribute_getters_should_not_crash() throws Exception {
        runTest("attributes: non-lowercase boolean attribute getters should not crash");
    }

    /**
     * Test {291=[FF68], 349=[IE], 350=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void event__on_with_multiple_delegated_events() throws Exception {
        runTest("event: on with multiple delegated events");
    }

    /**
     * Test {291=[CHROME], 332=[FF68], 413=[IE], 414=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "27",
            CHROME = "23")
    public void traversing__is___with_positional_selectors() throws Exception {
        runTest("traversing: is() with positional selectors");
    }

    /**
     * Test {292=[FF68], 350=[IE], 351=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "8",
            IE = "8")
    public void event__delegated_on_with_change() throws Exception {
        runTest("event: delegated on with change");
    }

    /**
     * Test {292=[CHROME], 333=[FF68], 414=[IE], 415=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__index__() throws Exception {
        runTest("traversing: index()");
    }

    /**
     * Test {293=[FF68], 351=[IE], 352=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__delegated_on_with_submit() throws Exception {
        runTest("event: delegated on with submit");
    }

    /**
     * Test {293=[CHROME], 334=[FF68], 415=[IE], 416=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void traversing__index_Object_String_undefined_() throws Exception {
        runTest("traversing: index(Object|String|undefined)");
    }

    /**
     * Test {294=[FF68], 352=[IE], 353=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__delegated_off___with_only_namespaces() throws Exception {
        runTest("event: delegated off() with only namespaces");
    }

    /**
     * Test {294=[CHROME], 335=[FF68], 416=[IE], 417=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void traversing__filter_Selector_undefined_() throws Exception {
        runTest("traversing: filter(Selector|undefined)");
    }

    /**
     * Test {295=[CHROME], 336=[FF68], 417=[IE], 418=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__filter_Function_() throws Exception {
        runTest("traversing: filter(Function)");
    }

    /**
     * Test {296=[CHROME], 337=[FF68], 418=[IE], 419=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__filter_Element_() throws Exception {
        runTest("traversing: filter(Element)");
    }

    /**
     * Test {297=[CHROME], 338=[FF68], 419=[IE], 420=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__filter_Array_() throws Exception {
        runTest("traversing: filter(Array)");
    }

    /**
     * Test {298=[CHROME], 339=[FF68], 420=[IE], 421=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__filter_jQuery_() throws Exception {
        runTest("traversing: filter(jQuery)");
    }

    /**
     * Test {299=[CHROME], 340=[FF68], 421=[IE], 422=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void traversing__filter___with_positional_selectors() throws Exception {
        runTest("traversing: filter() with positional selectors");
    }

    /**
     * Test {300=[CHROME], 341=[FF68], 422=[IE], 423=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 13, 14",
            FF60 = "14",
            FF68 = "13",
            IE = "14")
    public void traversing__closest__() throws Exception {
        runTest("traversing: closest()");
    }

    /**
     * Test {301=[FF68], 359=[IE], 360=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "7",
            IE = "7")
    public void event__special_on_name_mapping() throws Exception {
        runTest("event: special on name mapping");
    }

    /**
     * Test {301=[CHROME], 343=[FF68], 424=[IE], 425=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "8",
            CHROME = "2, 6, 8")
    public void traversing__closest_jQuery_() throws Exception {
        runTest("traversing: closest(jQuery)");
    }

    /**
     * Test {302=[CHROME], 344=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 8, 11",
            FF68 = "2, 9, 11")
    public void traversing__not_Selector_undefined_() throws Exception {
        runTest("traversing: not(Selector|undefined)");
    }

    /**
     * Test {303=[CHROME], 345=[FF68], 427=[IE], 428=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void traversing__not_Element_() throws Exception {
        runTest("traversing: not(Element)");
    }

    /**
     * Test {304=[CHROME], 346=[FF68], 428=[IE], 429=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = "1, 1, 2")
    public void traversing__not_Function_() throws Exception {
        runTest("traversing: not(Function)");
    }

    /**
     * Test {305=[FF68], 363=[IE], 364=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "9",
            IE = "9")
    public void event__checkbox_state___3827_() throws Exception {
        runTest("event: checkbox state (#3827)");
    }

    /**
     * Test {305=[CHROME], 347=[FF68], 429=[IE], 430=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void traversing__not_Array_() throws Exception {
        runTest("traversing: not(Array)");
    }

    /**
     * Test {306=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "1")
    public void event__hover_event_no_longer_special_since_1_9() throws Exception {
        runTest("event: hover event no longer special since 1.9");
    }

    /**
     * Test {306=[CHROME], 348=[FF68], 430=[IE], 431=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void traversing__not_jQuery_() throws Exception {
        runTest("traversing: not(jQuery)");
    }

    /**
     * Test {307=[IE], 308=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__triggered_events_stopPropagation___for_natively_bound_events() throws Exception {
        runTest("event: triggered events stopPropagation() for natively-bound events");
    }

    /**
     * Test {307=[CHROME], 349=[FF68], 433=[IE], 434=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void traversing__has_Element_() throws Exception {
        runTest("traversing: has(Element)");
    }

    /**
     * Test {308=[IE], 309=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "0",
            IE = "0")
    public void event__trigger___works_with_events_that_were_previously_stopped() throws Exception {
        runTest("event: trigger() works with events that were previously stopped");
    }

    /**
     * Test {308=[FF68], 367=[IE], 368=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void event__focusin_using_non_element_targets() throws Exception {
        runTest("event: focusin using non-element targets");
    }

    /**
     * Test {308=[CHROME], 350=[FF68], 434=[IE], 435=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void traversing__has_Selector_() throws Exception {
        runTest("traversing: has(Selector)");
    }

    /**
     * Test {309=[CHROME], 351=[FF68], 435=[IE], 436=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void traversing__has_Arrayish_() throws Exception {
        runTest("traversing: has(Arrayish)");
    }

    /**
     * Test {310=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "7")
    public void selector__element___jQuery_only() throws Exception {
        runTest("selector: element - jQuery only");
    }

    /**
     * Test {310=[CHROME], 352=[FF68], 436=[IE], 437=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void traversing__addBack__() throws Exception {
        runTest("traversing: addBack()");
    }

    /**
     * Test {311=[FF68], 391=[IE], 392=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "26",
            FF68 = "2, 0, 2",
            IE = "26")
    public void selector__id() throws Exception {
        runTest("selector: id");
    }

    /**
     * Test {311=[CHROME], 353=[FF68], 438=[IE], 439=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "7",
            FF60 = "2",
            FF68 = "6",
            IE = "2")
    @NotYetImplemented
    public void traversing__siblings__String__() throws Exception {
        runTest("traversing: siblings([String])");
    }

    /**
     * Test {312=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "4")
    public void selector__class___jQuery_only() throws Exception {
        runTest("selector: class - jQuery only");
    }

    /**
     * Test {312=[CHROME], 355=[FF68], 440=[IE], 441=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3",
            FF60 = "1",
            FF68 = "2",
            IE = "1")
    @NotYetImplemented
    public void traversing__children__String__() throws Exception {
        runTest("traversing: children([String])");
    }

    /**
     * Test {313=[FF68], 393=[IE], 394=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void selector__name() throws Exception {
        runTest("selector: name");
    }

    /**
     * Test {313=[CHROME], 357=[FF68], 441=[IE], 442=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "6",
            CHROME = "5")
    public void traversing__parent__String__() throws Exception {
        runTest("traversing: parent([String])");
    }

    /**
     * Test {314=[IE], 315=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__Empty_namespace_is_ignored() throws Exception {
        runTest("event: Empty namespace is ignored");
    }

    /**
     * Test {314=[FF68], 394=[IE], 395=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void selector__selectors_with_comma() throws Exception {
        runTest("selector: selectors with comma");
    }

    /**
     * Test {314=[CHROME], 358=[FF68], 442=[IE], 443=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 4, 5",
            FF60 = "6",
            FF68 = "1, 5, 6",
            IE = "6")
    public void traversing__parents__String__() throws Exception {
        runTest("traversing: parents([String])");
    }

    /**
     * Test {315=[FF68], 395=[IE], 396=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "27",
            FF68 = "2, 0, 2",
            IE = "27")
    public void selector__child_and_adjacent() throws Exception {
        runTest("selector: child and adjacent");
    }

    /**
     * Test {315=[CHROME], 359=[FF68], 443=[IE], 444=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 6, 9",
            FF60 = "10",
            FF68 = "3, 7, 10",
            IE = "10")
    public void traversing__parentsUntil__String__() throws Exception {
        runTest("traversing: parentsUntil([String])");
    }

    /**
     * Test {316=[FF68], 396=[IE], 397=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "54",
            FF68 = "2, 0, 2",
            IE = "54")
    public void selector__attributes() throws Exception {
        runTest("selector: attributes");
    }

    /**
     * Test {316=[CHROME], 360=[FF68], 444=[IE], 445=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "6",
            CHROME = "1, 4, 5")
    public void traversing__next__String__() throws Exception {
        runTest("traversing: next([String])");
    }

    /**
     * Test {317=[FF68], 398=[IE], 399=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "1",
            IE = "3")
    @NotYetImplemented
    public void selector__disconnected_nodes() throws Exception {
        runTest("selector: disconnected nodes");
    }

    /**
     * Test {317=[CHROME], 361=[FF68], 445=[IE], 446=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "5",
            CHROME = "4")
    public void traversing__prev__String__() throws Exception {
        runTest("traversing: prev([String])");
    }

    /**
     * Test {318=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "3")
    public void selector__disconnected_nodes___jQuery_only() throws Exception {
        runTest("selector: disconnected nodes - jQuery only");
    }

    /**
     * Test {318=[CHROME], 362=[FF68], 446=[IE], 447=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "5",
            CHROME = "4")
    public void traversing__nextAll__String__() throws Exception {
        runTest("traversing: nextAll([String])");
    }

    /**
     * Test {319=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2, 0, 2")
    public void selector__selector_html5_selector() throws Exception {
        runTest("selector: selector/html5_selector");
    }

    /**
     * Test {319=[CHROME], 363=[FF68], 447=[IE], 448=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "5",
            CHROME = "4")
    public void traversing__prevAll__String__() throws Exception {
        runTest("traversing: prevAll([String])");
    }

    /**
     * Test {320=[FF68], 400=[IE], 401=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "16",
            IE = "16")
    public void selector__jQuery_contains() throws Exception {
        runTest("selector: jQuery.contains");
    }

    /**
     * Test {320=[CHROME], 364=[FF68], 448=[IE], 449=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "12",
            CHROME = "11")
    public void traversing__nextUntil__String__() throws Exception {
        runTest("traversing: nextUntil([String])");
    }

    /**
     * Test {321=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "14")
    public void selector__jQuery_unique() throws Exception {
        runTest("selector: jQuery.unique");
    }

    /**
     * Test {321=[CHROME], 365=[FF68], 449=[IE], 450=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "11",
            CHROME = "10")
    public void traversing__prevUntil__String__() throws Exception {
        runTest("traversing: prevUntil([String])");
    }

    /**
     * Test {322=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2, 0, 2")
    public void selector__selector_sizzle_cache() throws Exception {
        runTest("selector: selector/sizzle_cache");
    }

    /**
     * Test {322=[CHROME], 366=[FF68], 450=[IE], 451=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void traversing__contents__() throws Exception {
        runTest("traversing: contents()");
    }

    /**
     * Test {323=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 14, 16")
    public void traversing__add_String_Element_Array_undefined_() throws Exception {
        runTest("traversing: add(String|Element|Array|undefined)");
    }

    /**
     * Test {323=[FF68], 403=[IE], 404=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void selector__Iframe_dispatch_should_not_affect_jQuery___13936_() throws Exception {
        runTest("selector: Iframe dispatch should not affect jQuery (#13936)");
    }

    /**
     * Test {324=[CHROME], 376=[FF68], 462=[IE], 463=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void traversing__add_String__Context_() throws Exception {
        runTest("traversing: add(String, Context)");
    }

    /**
     * Test {325=[FF68], 406=[IE], 407=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "1, 1, 2",
            IE = "2")
    public void traversing__find_String__under_non_elements() throws Exception {
        runTest("traversing: find(String) under non-elements");
    }

    /**
     * Test {325=[CHROME], 377=[FF68], 463=[IE], 464=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void traversing__eq___1____10616() throws Exception {
        runTest("traversing: eq('-1') #10616");
    }

    /**
     * Test {326=[FF68], 407=[IE], 408=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void traversing__find_leading_combinator_() throws Exception {
        runTest("traversing: find(leading combinator)");
    }

    /**
     * Test {326=[CHROME], 380=[FF68], 466=[IE], 467=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void manipulation__text__() throws Exception {
        runTest("manipulation: text()");
    }

    /**
     * Test {327=[CHROME], 381=[FF68], 467=[IE], 468=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__text_undefined_() throws Exception {
        runTest("manipulation: text(undefined)");
    }

    /**
     * Test {328=[CHROME], 382=[FF68], 468=[IE], 469=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "6",
            FF68 = "7",
            IE = "6")
    public void manipulation__text_String_() throws Exception {
        runTest("manipulation: text(String)");
    }

    /**
     * Test {329=[FF68], 410=[IE], 411=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "14",
            IE = "14")
    public void traversing__is___against_non_elements___10178_() throws Exception {
        runTest("traversing: is() against non-elements (#10178)");
    }

    /**
     * Test {329=[CHROME], 383=[FF68], 469=[IE], 470=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "6",
            FF68 = "7",
            IE = "6")
    public void manipulation__text_Function_() throws Exception {
        runTest("manipulation: text(Function)");
    }

    /**
     * Test {330=[CHROME], 384=[FF68], 470=[IE], 471=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__text_Function__with_incoming_value() throws Exception {
        runTest("manipulation: text(Function) with incoming value");
    }

    /**
     * Test {331=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 17, 19")
    public void manipulation__wrap_String_Element_() throws Exception {
        runTest("manipulation: wrap(String|Element)");
    }

    /**
     * Test {331=[FF68], 412=[IE], 413=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "6",
            IE = "6")
    public void traversing__is___with__has___selectors() throws Exception {
        runTest("traversing: is() with :has() selectors");
    }

    /**
     * Test {332=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 17, 19")
    public void manipulation__wrap_Function_() throws Exception {
        runTest("manipulation: wrap(Function)");
    }

    /**
     * Test {333=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6")
    public void manipulation__wrap_Function__with_index___10177_() throws Exception {
        runTest("manipulation: wrap(Function) with index (#10177)");
    }

    /**
     * Test {334=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "12")
    public void manipulation__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("manipulation: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {335=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "8")
    public void manipulation__wrapAll_String_Element_() throws Exception {
        runTest("manipulation: wrapAll(String|Element)");
    }

    /**
     * Test {335=[IE], 336=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void event__Delegated_events_with_malformed_selectors__gh_3071_() throws Exception {
        runTest("event: Delegated events with malformed selectors (gh-3071)");
    }

    /**
     * Test {336=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "11")
    public void manipulation__wrapInner_String_Element_() throws Exception {
        runTest("manipulation: wrapInner(String|Element)");
    }

    /**
     * Test {337=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "11")
    public void manipulation__wrapInner_Function_() throws Exception {
        runTest("manipulation: wrapInner(Function)");
    }

    /**
     * Test {338=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9")
    public void manipulation__unwrap__() throws Exception {
        runTest("manipulation: unwrap()");
    }

    /**
     * Test {339=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 9, 11")
    public void manipulation__append_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {340=[CHROME], 386=[FF68], 472=[IE], 473=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 9, 11",
            FF60 = "78",
            FF68 = "2, 49, 51",
            IE = "78")
    public void manipulation__append_Function_() throws Exception {
        runTest("manipulation: append(Function)");
    }

    /**
     * Test {341=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "12")
    public void manipulation__append_Function__with_incoming_value() throws Exception {
        runTest("manipulation: append(Function) with incoming value");
    }

    /**
     * Test {342=[CHROME], 394=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF68 = "2")
    public void manipulation__append_the_same_fragment_with_events__Bug__6997__5566_() throws Exception {
        runTest("manipulation: append the same fragment with events (Bug #6997, 5566)");
    }

    /**
     * Test {342=[FF68], 423=[IE], 424=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "1, 1, 2",
            IE = "3")
    public void traversing__closest___with_positional_selectors() throws Exception {
        runTest("traversing: closest() with positional selectors");
    }

    /**
     * Test {343=[CHROME], 395=[FF68], 480=[IE], 481=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__append_HTML5_sectioning_elements__Bug__6485_() throws Exception {
        runTest("manipulation: append HTML5 sectioning elements (Bug #6485)");
    }

    /**
     * Test {344=[CHROME], 396=[FF68], 481=[IE], 482=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__HTML5_Elements_inherit_styles_from_style_rules__Bug__10501_() throws Exception {
        runTest("manipulation: HTML5 Elements inherit styles from style rules (Bug #10501)");
    }

    /**
     * Test {345=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void manipulation__html5_clone___cannot_use_the_fragment_cache_in_IE___6485_() throws Exception {
        runTest("manipulation: html5 clone() cannot use the fragment cache in IE (#6485)");
    }

    /**
     * Test {346=[IE], 347=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    @NotYetImplemented
    public void event__ignore_comment_nodes_in_event_delegation__gh_2055_() throws Exception {
        runTest("event: ignore comment nodes in event delegation (gh-2055)");
    }

    /**
     * Test {346=[CHROME], 397=[FF68], 482=[IE], 483=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__html_String__with_HTML5__Bug__6485_() throws Exception {
        runTest("manipulation: html(String) with HTML5 (Bug #6485)");
    }

    /**
     * Test {347=[CHROME], 398=[FF68], 485=[IE], 486=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__IE8_serialization_bug() throws Exception {
        runTest("manipulation: IE8 serialization bug");
    }

    /**
     * Test {348=[CHROME], 399=[FF68], 486=[IE], 487=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__html___object_element__10324() throws Exception {
        runTest("manipulation: html() object element #10324");
    }

    /**
     * Test {349=[CHROME], 400=[FF68], 487=[IE], 488=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__append_xml_() throws Exception {
        runTest("manipulation: append(xml)");
    }

    /**
     * Test {350=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 8, 10")
    public void manipulation__appendTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: appendTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {351=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6")
    public void manipulation__prepend_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prepend(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {352=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6")
    public void manipulation__prepend_Function_() throws Exception {
        runTest("manipulation: prepend(Function)");
    }

    /**
     * Test {353=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "10")
    public void manipulation__prepend_Function__with_incoming_value() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value");
    }

    /**
     * Test {354=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 5, 6")
    public void manipulation__prependTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prependTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {354=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2")
    public void traversing__siblings__String_____jQuery_only() throws Exception {
        runTest("traversing: siblings([String]) - jQuery only");
    }

    /**
     * Test {355=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 5, 7")
    public void manipulation__before_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: before(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {356=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 5, 7")
    public void manipulation__before_Function_() throws Exception {
        runTest("manipulation: before(Function)");
    }

    /**
     * Test {356=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "1")
    public void traversing__children__String_____jQuery_only() throws Exception {
        runTest("traversing: children([String]) - jQuery only");
    }

    /**
     * Test {357=[CHROME], 429=[FF68], 516=[IE], 517=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1",
            CHROME = "2, 0, 2")
    public void manipulation__before_and_after_w__empty_object___10812_() throws Exception {
        runTest("manipulation: before and after w/ empty object (#10812)");
    }

    /**
     * Test {358=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void manipulation__before_and_after_on_disconnected_node___10517_() throws Exception {
        runTest("manipulation: before and after on disconnected node (#10517)");
    }

    /**
     * Test {359=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4")
    public void manipulation__insertBefore_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertBefore(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {360=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 5, 7")
    public void manipulation__after_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: after(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {361=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 5, 7")
    public void manipulation__after_Function_() throws Exception {
        runTest("manipulation: after(Function)");
    }

    /**
     * Test {362=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4")
    public void manipulation__insertAfter_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertAfter(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {363=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 20, 22")
    public void manipulation__replaceWith_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {364=[IE], 365=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void event__event_object_properties_on_natively_triggered_event() throws Exception {
        runTest("event: event object properties on natively-triggered event");
    }

    /**
     * Test {364=[CHROME], 453=[FF68], 540=[IE], 541=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "30",
            CHROME = "2, 21, 23")
    public void manipulation__replaceWith_Function_() throws Exception {
        runTest("manipulation: replaceWith(Function)");
    }

    /**
     * Test {365=[IE], 366=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void event__addProp_extensions() throws Exception {
        runTest("event: addProp extensions");
    }

    /**
     * Test {365=[CHROME], 454=[FF68], 541=[IE], 542=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__replaceWith_string__for_more_than_one_element() throws Exception {
        runTest("manipulation: replaceWith(string) for more than one element");
    }

    /**
     * Test {366=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "10")
    public void manipulation__replaceAll_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceAll(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {366=[IE], 367=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void event__drag_drop_events_copy_mouse_related_event_properties__gh_1925__gh_2009_() throws Exception {
        runTest("event: drag/drop events copy mouse-related event properties (gh-1925, gh-2009)");
    }

    /**
     * Test {367=[FF68], 453=[IE], 454=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "12",
            IE = "12")
    public void traversing__sort_direction() throws Exception {
        runTest("traversing: sort direction");
    }

    /**
     * Test {367=[CHROME], 460=[FF68], 547=[IE], 548=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__jQuery_clone_____8017_() throws Exception {
        runTest("manipulation: jQuery.clone() (#8017)");
    }

    /**
     * Test {368=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void manipulation__clone_____8070_() throws Exception {
        runTest("manipulation: clone() (#8070)");
    }

    /**
     * Test {368=[IE], 369=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__focusin_from_an_iframe() throws Exception {
        runTest("event: focusin from an iframe");
    }

    /**
     * Test {368=[FF68], 454=[IE], 455=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void traversing__add_String_selector_() throws Exception {
        runTest("traversing: add(String selector)");
    }

    /**
     * Test {369=[IE], 370=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__jQuery_ready_promise() throws Exception {
        runTest("event: jQuery.ready promise");
    }

    /**
     * Test {369=[FF68], 455=[IE], 456=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void traversing__add_String_selector__String_context_() throws Exception {
        runTest("traversing: add(String selector, String context)");
    }

    /**
     * Test {369=[CHROME], 463=[FF68], 550=[IE], 551=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "45",
            CHROME = "1, 43, 44")
    public void manipulation__clone__() throws Exception {
        runTest("manipulation: clone()");
    }

    /**
     * Test {370=[IE], 371=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    @NotYetImplemented
    public void event__jQuery_ready_uses_interactive() throws Exception {
        runTest("event: jQuery.ready uses interactive");
    }

    /**
     * Test {370=[FF68], 456=[IE], 457=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "3",
            IE = "3")
    public void traversing__add_String_html_() throws Exception {
        runTest("traversing: add(String html)");
    }

    /**
     * Test {370=[CHROME], 464=[FF68], 551=[IE], 552=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void manipulation__clone_script_type_non_javascript____11359_() throws Exception {
        runTest("manipulation: clone(script type=non-javascript) (#11359)");
    }

    /**
     * Test {371=[IE], 372=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__Focusing_iframe_element() throws Exception {
        runTest("event: Focusing iframe element");
    }

    /**
     * Test {371=[FF68], 457=[IE], 458=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void traversing__add_jQuery_() throws Exception {
        runTest("traversing: add(jQuery)");
    }

    /**
     * Test {371=[CHROME], 465=[FF68], 552=[IE], 553=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void manipulation__clone_form_element___Bug__3879___6655_() throws Exception {
        runTest("manipulation: clone(form element) (Bug #3879, #6655)");
    }

    /**
     * Test {372=[IE], 373=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__triggerHandler_onbeforeunload_() throws Exception {
        runTest("event: triggerHandler(onbeforeunload)");
    }

    /**
     * Test {372=[FF68], 458=[IE], 459=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void traversing__add_Element_() throws Exception {
        runTest("traversing: add(Element)");
    }

    /**
     * Test {372=[CHROME], 466=[FF68], 553=[IE], 554=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__clone_multiple_selected_options___Bug__8129_() throws Exception {
        runTest("manipulation: clone(multiple selected options) (Bug #8129)");
    }

    /**
     * Test {373=[IE], 374=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    @NotYetImplemented
    public void event__jQuery_ready_synchronous_load_with_long_loading_subresources() throws Exception {
        runTest("event: jQuery.ready synchronous load with long loading subresources");
    }

    /**
     * Test {373=[FF68], 459=[IE], 460=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void traversing__add_Array_elements_() throws Exception {
        runTest("traversing: add(Array elements)");
    }

    /**
     * Test {373=[CHROME], 467=[FF68], 554=[IE], 555=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__clone___on_XML_nodes() throws Exception {
        runTest("manipulation: clone() on XML nodes");
    }

    /**
     * Test {374=[IE], 375=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void event__change_handler_should_be_detached_from_element() throws Exception {
        runTest("event: change handler should be detached from element");
    }

    /**
     * Test {374=[FF68], 460=[IE], 461=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void traversing__add_Window_() throws Exception {
        runTest("traversing: add(Window)");
    }

    /**
     * Test {374=[CHROME], 468=[FF68], 555=[IE], 556=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__clone___on_local_XML_nodes_with_html5_nodename() throws Exception {
        runTest("manipulation: clone() on local XML nodes with html5 nodename");
    }

    /**
     * Test {375=[IE], 376=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__trigger_click_on_checkbox__fires_change_event() throws Exception {
        runTest("event: trigger click on checkbox, fires change event");
    }

    /**
     * Test {375=[FF68], 461=[IE], 462=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void traversing__add_NodeList_undefined_HTMLFormElement_HTMLSelectElement_() throws Exception {
        runTest("traversing: add(NodeList|undefined|HTMLFormElement|HTMLSelectElement)");
    }

    /**
     * Test {375=[CHROME], 469=[FF68], 556=[IE], 557=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__html_undefined_() throws Exception {
        runTest("manipulation: html(undefined)");
    }

    /**
     * Test {376=[IE], 377=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void event__Namespace_preserved_when_passed_an_Event___12739_() throws Exception {
        runTest("event: Namespace preserved when passed an Event (#12739)");
    }

    /**
     * Test {376=[CHROME], 470=[FF68], 557=[IE], 558=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__html___on_empty_set() throws Exception {
        runTest("manipulation: html() on empty set");
    }

    /**
     * Test {377=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void manipulation__html_String_() throws Exception {
        runTest("manipulation: html(String)");
    }

    /**
     * Test {377=[IE], 378=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "18",
            IE = "18")
    public void event__make_sure_events_cloned_correctly() throws Exception {
        runTest("event: make sure events cloned correctly");
    }

    /**
     * Test {378=[IE], 379=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__String_prototype_namespace_does_not_cause_trigger___to_throw___13360_() throws Exception {
        runTest("event: String.prototype.namespace does not cause trigger() to throw (#13360)");
    }

    /**
     * Test {378=[FF68], 464=[IE], 465=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void traversing__index_no_arg___10977() throws Exception {
        runTest("traversing: index(no arg) #10977");
    }

    /**
     * Test {378=[CHROME], 472=[FF68], 559=[IE], 560=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF60 = "40",
            FF68 = "39",
            IE = "40")
    public void manipulation__html_Function_() throws Exception {
        runTest("manipulation: html(Function)");
    }

    /**
     * Test {379=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 22, 23")
    public void manipulation__html_Function__with_incoming_value() throws Exception {
        runTest("manipulation: html(Function) with incoming value");
    }

    /**
     * Test {379=[IE], 380=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__Inline_event_result_is_returned___13993_() throws Exception {
        runTest("event: Inline event result is returned (#13993)");
    }

    /**
     * Test {379=[FF68], 465=[IE], 466=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "5",
            IE = "5")
    public void traversing__traversing_non_elements_with_attribute_filters___12523_() throws Exception {
        runTest("traversing: traversing non-elements with attribute filters (#12523)");
    }

    /**
     * Test {380=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9")
    public void manipulation__remove__() throws Exception {
        runTest("manipulation: remove()");
    }

    /**
     * Test {380=[IE], 381=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void event___off___removes_the_expando_when_there_s_no_more_data() throws Exception {
        runTest("event: .off() removes the expando when there's no more data");
    }

    /**
     * Test {381=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9")
    public void manipulation__detach__() throws Exception {
        runTest("manipulation: detach()");
    }

    /**
     * Test {381=[IE], 382=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void event__jQuery_Event__src___does_not_require_a_target_property() throws Exception {
        runTest("event: jQuery.Event( src ) does not require a target property");
    }

    /**
     * Test {382=[IE], 383=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__preventDefault___on_focusin_does_not_throw_exception() throws Exception {
        runTest("event: preventDefault() on focusin does not throw exception");
    }

    /**
     * Test {382=[CHROME], 485=[FF68], 571=[IE], 572=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3",
            FF60 = "3",
            FF68 = "6",
            IE = "3")
    public void manipulation__empty__() throws Exception {
        runTest("manipulation: empty()");
    }

    /**
     * Test {383=[IE], 384=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "8",
            IE = "8")
    public void event__Donor_event_interference() throws Exception {
        runTest("event: Donor event interference");
    }

    /**
     * Test {383=[CHROME], 486=[FF68], 572=[IE], 573=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("14")
    public void manipulation__jQuery_cleanData() throws Exception {
        runTest("manipulation: jQuery.cleanData");
    }

    /**
     * Test {384=[IE], 385=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void event__simulated_events_shouldn_t_forward_stopPropagation_preventDefault_methods() throws Exception {
        runTest("event: simulated events shouldn't forward stopPropagation/preventDefault methods");
    }

    /**
     * Test {384=[CHROME], 487=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF68 = "1")
    public void manipulation__jQuery_buildFragment___no_plain_text_caching__Bug__6779_() throws Exception {
        runTest("manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779)");
    }

    /**
     * Test {385=[IE], 386=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void event__originalEvent_type_of_simulated_event() throws Exception {
        runTest("event: originalEvent type of simulated event");
    }

    /**
     * Test {385=[CHROME], 488=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3",
            FF68 = "3")
    public void manipulation__jQuery_html___execute_scripts_escaped_with_html_comment_or_CDATA___9221_() throws Exception {
        runTest("manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221)");
    }

    /**
     * Test {385=[FF68], 471=[IE], 472=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "78",
            FF68 = "2, 49, 51",
            IE = "78")
    public void manipulation__append_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {386=[IE], 387=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__trigger__click___on_radio_passes_extra_params() throws Exception {
        runTest("event: trigger('click') on radio passes extra params");
    }

    /**
     * Test {386=[CHROME], 489=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF68 = "1")
    public void manipulation__jQuery_buildFragment___plain_objects_are_not_a_document__8950() throws Exception {
        runTest("manipulation: jQuery.buildFragment - plain objects are not a document #8950");
    }

    /**
     * Test {387=[IE], 388=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void event__VML_with_special_event_handlers__trac_7071_() throws Exception {
        runTest("event: VML with special event handlers (trac-7071)");
    }

    /**
     * Test {387=[FF68], 473=[IE], 474=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "5",
            IE = "5")
    public void manipulation__append_param__to_object__see__11280() throws Exception {
        runTest("manipulation: append(param) to object, see #11280");
    }

    /**
     * Test {387=[CHROME], 490=[FF68], 579=[IE], 580=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__jQuery_clone___no_exceptions_for_object_elements__9587() throws Exception {
        runTest("manipulation: jQuery.clone - no exceptions for object elements #9587");
    }

    /**
     * Test {388=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void manipulation__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("manipulation: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {388=[IE], 389=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void event__Check_order_of_focusin_focusout_events() throws Exception {
        runTest("event: Check order of focusin/focusout events");
    }

    /**
     * Test {388=[FF68], 474=[IE], 475=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void manipulation__append_Function__returns_String() throws Exception {
        runTest("manipulation: append(Function) returns String");
    }

    /**
     * Test {389=[IE], 390=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            IE = "5")
    public void event__focus_blur_order___12868_() throws Exception {
        runTest("event: focus-blur order (#12868)");
    }

    /**
     * Test {389=[FF68], 475=[IE], 476=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__append_Function__returns_Element() throws Exception {
        runTest("manipulation: append(Function) returns Element");
    }

    /**
     * Test {389=[CHROME], 491=[FF68], 580=[IE], 581=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void manipulation__Cloned__detached_HTML5_elems___10667_10670_() throws Exception {
        runTest("manipulation: Cloned, detached HTML5 elems (#10667,10670)");
    }

    /**
     * Test {390=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void manipulation__jQuery_fragments_cache_expectations() throws Exception {
        runTest("manipulation: jQuery.fragments cache expectations");
    }

    /**
     * Test {390=[IE], 391=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "7",
            IE = "7")
    public void selector__element() throws Exception {
        runTest("selector: element");
    }

    /**
     * Test {390=[FF68], 476=[IE], 477=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__append_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: append(Function) returns Array<Element>");
    }

    /**
     * Test {391=[FF68], 477=[IE], 478=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__append_Function__returns_jQuery() throws Exception {
        runTest("manipulation: append(Function) returns jQuery");
    }

    /**
     * Test {391=[CHROME], 492=[FF68], 581=[IE], 582=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void manipulation__Guard_against_exceptions_when_clearing_safeChildNodes() throws Exception {
        runTest("manipulation: Guard against exceptions when clearing safeChildNodes");
    }

    /**
     * Test {392=[IE], 393=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void selector__class() throws Exception {
        runTest("selector: class");
    }

    /**
     * Test {392=[FF68], 478=[IE], 479=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__append_Function__returns_Number() throws Exception {
        runTest("manipulation: append(Function) returns Number");
    }

    /**
     * Test {392=[CHROME], 493=[FF68], 582=[IE], 583=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "5",
            CHROME = "5, 1, 6")
    public void manipulation__Ensure_oldIE_creates_a_new_set_on_appendTo___8894_() throws Exception {
        runTest("manipulation: Ensure oldIE creates a new set on appendTo (#8894)");
    }

    /**
     * Test {393=[FF68], 479=[IE], 480=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "5",
            IE = "5")
    public void manipulation__XML_DOM_manipulation___9960_() throws Exception {
        runTest("manipulation: XML DOM manipulation (#9960)");
    }

    /**
     * Test {393=[CHROME], 494=[FF68], 583=[IE], 584=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5, 0, 5",
            FF60 = "2",
            FF68 = "5, 0, 5",
            IE = "2")
    @NotYetImplemented
    public void manipulation__html_____script_exceptions_bubble___11743_() throws Exception {
        runTest("manipulation: html() - script exceptions bubble (#11743)");
    }

    /**
     * Test {394=[CHROME], 495=[FF68], 584=[IE], 585=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__checked_state_is_cloned_with_clone__() throws Exception {
        runTest("manipulation: checked state is cloned with clone()");
    }

    /**
     * Test {395=[CHROME], 496=[FF68], 585=[IE], 586=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void manipulation__manipulate_mixed_jQuery_and_text___12384___12346_() throws Exception {
        runTest("manipulation: manipulate mixed jQuery and text (#12384, #12346)");
    }

    /**
     * Test {396=[CHROME], 497=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void manipulation__global_failure() throws Exception {
        runTest("manipulation: global failure");
    }

    /**
     * Test {397=[CHROME], 498=[FF68], 601=[IE], 602=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void wrap__wrap_String_Element_() throws Exception {
        runTest("wrap: wrap(String|Element)");
    }

    /**
     * Test {398=[CHROME], 499=[FF68], 602=[IE], 603=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void wrap__wrap_Function_() throws Exception {
        runTest("wrap: wrap(Function)");
    }

    /**
     * Test {399=[IE], 400=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "38",
            IE = "38")
    public void selector__attributes___jQuery_attr() throws Exception {
        runTest("selector: attributes - jQuery.attr");
    }

    /**
     * Test {399=[CHROME], 500=[FF68], 603=[IE], 604=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void wrap__wrap_Function__with_index___10177_() throws Exception {
        runTest("wrap: wrap(Function) with index (#10177)");
    }

    /**
     * Test {400=[CHROME], 501=[FF68], 604=[IE], 605=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("12")
    public void wrap__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("wrap: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {401=[IE], 402=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "15",
            IE = "15")
    @NotYetImplemented
    public void selector__jQuery_uniqueSort() throws Exception {
        runTest("selector: jQuery.uniqueSort");
    }

    /**
     * Test {401=[FF68], 488=[IE], 489=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void manipulation__appendTo_String_() throws Exception {
        runTest("manipulation: appendTo(String)");
    }

    /**
     * Test {401=[CHROME], 502=[FF68], 605=[IE], 606=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void wrap__wrapAll_String_() throws Exception {
        runTest("wrap: wrapAll(String)");
    }

    /**
     * Test {402=[IE], 403=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void selector__Sizzle_cache_collides_with_multiple_Sizzles_on_a_page() throws Exception {
        runTest("selector: Sizzle cache collides with multiple Sizzles on a page");
    }

    /**
     * Test {402=[FF68], 489=[IE], 490=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__appendTo_Element_Array_Element__() throws Exception {
        runTest("manipulation: appendTo(Element|Array<Element>)");
    }

    /**
     * Test {402=[CHROME], 606=[IE], 607=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "5",
            FF60 = "5")
    public void wrap__wrapAll_Function_() throws Exception {
        runTest("wrap: wrapAll(Function)");
    }

    /**
     * Test {403=[FF68], 490=[IE], 491=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "10",
            FF68 = "2, 2, 4",
            IE = "10")
    public void manipulation__appendTo_jQuery_() throws Exception {
        runTest("manipulation: appendTo(jQuery)");
    }

    /**
     * Test {403=[CHROME], 607=[IE], 608=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "3",
            FF60 = "3")
    public void wrap__wrapAll_Function__check_execution_characteristics() throws Exception {
        runTest("wrap: wrapAll(Function) check execution characteristics");
    }

    /**
     * Test {404=[IE], 405=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void selector__Ensure_escapeSelector_exists__escape_tests_in_Sizzle_() throws Exception {
        runTest("selector: Ensure escapeSelector exists (escape tests in Sizzle)");
    }

    /**
     * Test {404=[FF68], 491=[IE], 492=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__prepend_String_() throws Exception {
        runTest("manipulation: prepend(String)");
    }

    /**
     * Test {404=[CHROME], 503=[FF68], 608=[IE], 609=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void wrap__wrapAll_Element_() throws Exception {
        runTest("wrap: wrapAll(Element)");
    }

    /**
     * Test {405=[FF68], 492=[IE], 493=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__prepend_Element_() throws Exception {
        runTest("manipulation: prepend(Element)");
    }

    /**
     * Test {405=[CHROME], 504=[FF68], 609=[IE], 610=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void wrap__wrapInner_String_() throws Exception {
        runTest("wrap: wrapInner(String)");
    }

    /**
     * Test {406=[FF68], 493=[IE], 494=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__prepend_Array_Element__() throws Exception {
        runTest("manipulation: prepend(Array<Element>)");
    }

    /**
     * Test {406=[CHROME], 505=[FF68], 610=[IE], 611=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void wrap__wrapInner_Element_() throws Exception {
        runTest("wrap: wrapInner(Element)");
    }

    /**
     * Test {407=[FF68], 494=[IE], 495=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__prepend_jQuery_() throws Exception {
        runTest("manipulation: prepend(jQuery)");
    }

    /**
     * Test {407=[CHROME], 506=[FF68], 611=[IE], 612=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void wrap__wrapInner_Function__returns_String() throws Exception {
        runTest("wrap: wrapInner(Function) returns String");
    }

    /**
     * Test {408=[FF68], 495=[IE], 496=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__prepend_Array_jQuery__() throws Exception {
        runTest("manipulation: prepend(Array<jQuery>)");
    }

    /**
     * Test {408=[CHROME], 507=[FF68], 612=[IE], 613=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void wrap__wrapInner_Function__returns_Element() throws Exception {
        runTest("wrap: wrapInner(Function) returns Element");
    }

    /**
     * Test {409=[FF68], 496=[IE], 497=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void manipulation__prepend_Function__with_incoming_value____String() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- String");
    }

    /**
     * Test {409=[CHROME], 508=[FF68], 613=[IE], 614=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void wrap__unwrap__() throws Exception {
        runTest("wrap: unwrap()");
    }

    /**
     * Test {410=[FF68], 497=[IE], 498=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__prepend_Function__with_incoming_value____Element() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Element");
    }

    /**
     * Test {410=[CHROME], 614=[IE], 615=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "5",
            FF60 = "5")
    public void wrap__unwrap__selector__() throws Exception {
        runTest("wrap: unwrap( selector )");
    }

    /**
     * Test {411=[FF68], 498=[IE], 499=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__prepend_Function__with_incoming_value____Array_Element_() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Array<Element>");
    }

    /**
     * Test {411=[CHROME], 509=[FF68], 615=[IE], 616=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void wrap__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("wrap: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {412=[FF68], 499=[IE], 500=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__prepend_Function__with_incoming_value____jQuery() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- jQuery");
    }

    /**
     * Test {412=[CHROME], 510=[FF68], 616=[IE], 617=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void wrap__wrapping_scripts___10470_() throws Exception {
        runTest("wrap: wrapping scripts (#10470)");
    }

    /**
     * Test {413=[FF68], 500=[IE], 501=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__prependTo_String_() throws Exception {
        runTest("manipulation: prependTo(String)");
    }

    /**
     * Test {413=[CHROME], 511=[FF68], 617=[IE], 618=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4, 31, 35",
            FF60 = "42",
            FF68 = "2, 28, 30",
            IE = "42")
    @NotYetImplemented(IE)
    public void css__css_String_Hash_() throws Exception {
        runTest("css: css(String|Hash)");
    }

    /**
     * Test {414=[FF68], 501=[IE], 502=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__prependTo_Element_() throws Exception {
        runTest("manipulation: prependTo(Element)");
    }

    /**
     * Test {414=[CHROME], 512=[FF68], 618=[IE], 619=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("29")
    public void css__css___explicit_and_relative_values() throws Exception {
        runTest("css: css() explicit and relative values");
    }

    /**
     * Test {415=[FF68], 502=[IE], 503=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__prependTo_Array_Element__() throws Exception {
        runTest("manipulation: prependTo(Array<Element>)");
    }

    /**
     * Test {415=[CHROME], 513=[FF68], 621=[IE], 622=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "22",
            FF60 = "19",
            FF68 = "20",
            IE = "19")
    @NotYetImplemented(IE)
    public void css__css_String__Object_() throws Exception {
        runTest("css: css(String, Object)");
    }

    /**
     * Test {416=[CHROME], 515=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4, 2, 6",
            FF68 = "3, 2, 5")
    public void css__css_String__Object__for_MSIE() throws Exception {
        runTest("css: css(String, Object) for MSIE");
    }

    /**
     * Test {416=[FF68], 503=[IE], 504=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__prependTo_jQuery_() throws Exception {
        runTest("manipulation: prependTo(jQuery)");
    }

    /**
     * Test {417=[CHROME], 516=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 1, 2",
            FF68 = "1, 1, 2")
    public void css__Setting_opacity_to_1_properly_removes_filter__style___6652_() throws Exception {
        runTest("css: Setting opacity to 1 properly removes filter: style (#6652)");
    }

    /**
     * Test {417=[FF68], 504=[IE], 505=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void manipulation__prependTo_Array_jQuery__() throws Exception {
        runTest("manipulation: prependTo(Array<jQuery>)");
    }

    /**
     * Test {418=[FF68], 505=[IE], 506=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_String_() throws Exception {
        runTest("manipulation: before(String)");
    }

    /**
     * Test {418=[CHROME], 517=[FF68], 624=[IE], 625=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__css_String__Function_() throws Exception {
        runTest("css: css(String, Function)");
    }

    /**
     * Test {419=[FF68], 506=[IE], 507=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_Element_() throws Exception {
        runTest("manipulation: before(Element)");
    }

    /**
     * Test {419=[CHROME], 518=[FF68], 625=[IE], 626=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__css_String__Function__with_incoming_value() throws Exception {
        runTest("css: css(String, Function) with incoming value");
    }

    /**
     * Test {420=[FF68], 507=[IE], 508=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_Array_Element__() throws Exception {
        runTest("manipulation: before(Array<Element>)");
    }

    /**
     * Test {420=[CHROME], 519=[FF68], 626=[IE], 627=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__css_Object__where_values_are_Functions() throws Exception {
        runTest("css: css(Object) where values are Functions");
    }

    /**
     * Test {421=[FF68], 508=[IE], 509=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_jQuery_() throws Exception {
        runTest("manipulation: before(jQuery)");
    }

    /**
     * Test {421=[CHROME], 520=[FF68], 627=[IE], 628=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__css_Object__where_values_are_Functions_with_incoming_values() throws Exception {
        runTest("css: css(Object) where values are Functions with incoming values");
    }

    /**
     * Test {422=[CHROME], 521=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "22",
            FF68 = "4")
    public void css__show____hide__() throws Exception {
        runTest("css: show(); hide()");
    }

    /**
     * Test {422=[FF68], 509=[IE], 510=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_Array_jQuery__() throws Exception {
        runTest("manipulation: before(Array<jQuery>)");
    }

    /**
     * Test {423=[CHROME], 523=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 5, 7",
            FF68 = "2, 5, 7")
    public void css__show___resolves_correct_default_display__8099() throws Exception {
        runTest("css: show() resolves correct default display #8099");
    }

    /**
     * Test {423=[FF68], 510=[IE], 511=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_Function_____Returns_String() throws Exception {
        runTest("manipulation: before(Function) -- Returns String");
    }

    /**
     * Test {424=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5, 6, 11")
    public void css__show___resolves_correct_default_display__detached_nodes___10006_() throws Exception {
        runTest("css: show() resolves correct default display, detached nodes (#10006)");
    }

    /**
     * Test {424=[FF68], 511=[IE], 512=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_Function_____Returns_Element() throws Exception {
        runTest("manipulation: before(Function) -- Returns Element");
    }

    /**
     * Test {425=[IE], 426=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "7",
            IE = "7")
    public void traversing__not_Selector_() throws Exception {
        runTest("traversing: not(Selector)");
    }

    /**
     * Test {425=[FF68], 512=[IE], 513=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_Function_____Returns_Array_Element_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<Element>");
    }

    /**
     * Test {425=[CHROME], 527=[FF68], 638=[IE], 639=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("9")
    public void css__toggle__() throws Exception {
        runTest("css: toggle()");
    }

    /**
     * Test {426=[IE], 427=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void traversing__not_undefined_() throws Exception {
        runTest("traversing: not(undefined)");
    }

    /**
     * Test {426=[FF68], 513=[IE], 514=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_Function_____Returns_jQuery() throws Exception {
        runTest("manipulation: before(Function) -- Returns jQuery");
    }

    /**
     * Test {426=[CHROME], 528=[FF68], 630=[IE], 631=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void css__hide_hidden_elements__bug__7141_() throws Exception {
        runTest("css: hide hidden elements (bug #7141)");
    }

    /**
     * Test {427=[FF68], 514=[IE], 515=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__before_Function_____Returns_Array_jQuery_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<jQuery>");
    }

    /**
     * Test {427=[CHROME], 529=[FF68], 640=[IE], 641=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void css__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {428=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void css___visible_selector_works_properly_on_table_elements__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on table elements (bug #4512)");
    }

    /**
     * Test {428=[FF68], 515=[IE], 516=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__before_no_op_() throws Exception {
        runTest("manipulation: before(no-op)");
    }

    /**
     * Test {429=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void css___visible_selector_works_properly_on_children_with_a_hidden_parent__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on children with a hidden parent (bug #4512)");
    }

    /**
     * Test {430=[FF68], 517=[IE], 518=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation___before___and__after___disconnected_node() throws Exception {
        runTest("manipulation: .before() and .after() disconnected node");
    }

    /**
     * Test {430=[CHROME], 530=[FF68], 641=[IE], 642=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void css__internal_ref_to_elem_runtimeStyle__bug__7608_() throws Exception {
        runTest("css: internal ref to elem.runtimeStyle (bug #7608)");
    }

    /**
     * Test {431=[CHROME], 531=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF68 = "1")
    public void css__marginRight_computed_style__bug__3333_() throws Exception {
        runTest("css: marginRight computed style (bug #3333)");
    }

    /**
     * Test {431=[IE], 432=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void traversing__not_Selector__excludes_non_element_nodes__gh_2808_() throws Exception {
        runTest("traversing: not(Selector) excludes non-element nodes (gh-2808)");
    }

    /**
     * Test {432=[IE], 433=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            IE = "5")
    public void traversing__not_arraylike__passes_non_element_nodes__gh_3226_() throws Exception {
        runTest("traversing: not(arraylike) passes non-element nodes (gh-3226)");
    }

    /**
     * Test {432=[FF68], 519=[IE], 520=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insert_with__before___on_disconnected_node_first() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node first");
    }

    /**
     * Test {432=[CHROME], 532=[FF68], 643=[IE], 644=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            CHROME = "1, 2, 3")
    public void css__box_model_properties_incorrectly_returning___instead_of_px__see__10639_and__12088() throws Exception {
        runTest("css: box model properties incorrectly returning % instead of px, see #10639 and #12088");
    }

    /**
     * Test {433=[CHROME], 533=[FF68], 644=[IE], 645=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            CHROME = "1, 2, 3")
    public void css__jQuery_cssProps_behavior___bug__8402_() throws Exception {
        runTest("css: jQuery.cssProps behavior, (bug #8402)");
    }

    /**
     * Test {434=[FF68], 521=[IE], 522=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insert_with__before___on_disconnected_node_last() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node last");
    }

    /**
     * Test {434=[CHROME], 534=[FF68], 645=[IE], 646=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 2, 4",
            FF60 = "2",
            FF68 = "1",
            IE = "2")
    public void css__widows___orphans__8936() throws Exception {
        runTest("css: widows & orphans #8936");
    }

    /**
     * Test {435=[FF68], 522=[IE], 523=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insertBefore_String_() throws Exception {
        runTest("manipulation: insertBefore(String)");
    }

    /**
     * Test {435=[CHROME], 535=[FF68], 646=[IE], 647=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void css__can_t_get_css_for_disconnected_in_IE_9__see__10254_and__8388() throws Exception {
        runTest("css: can't get css for disconnected in IE<9, see #10254 and #8388");
    }

    /**
     * Test {436=[FF68], 523=[IE], 524=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insertBefore_Element_() throws Exception {
        runTest("manipulation: insertBefore(Element)");
    }

    /**
     * Test {436=[CHROME], 536=[FF68], 648=[IE], 649=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void css__can_t_get_background_position_in_IE_9__see__10796() throws Exception {
        runTest("css: can't get background-position in IE<9, see #10796");
    }

    /**
     * Test {437=[CHROME], 537=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF68 = "1")
    public void css__percentage_properties_for_bottom_and_right_in_IE_9_should_not_be_incorrectly_transformed_to_pixels__see__11311() throws Exception {
        runTest("css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pixels, see #11311");
    }

    /**
     * Test {437=[FF68], 524=[IE], 525=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insertBefore_Array_Element__() throws Exception {
        runTest("manipulation: insertBefore(Array<Element>)");
    }

    /**
     * Test {438=[FF68], 525=[IE], 526=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insertBefore_jQuery_() throws Exception {
        runTest("manipulation: insertBefore(jQuery)");
    }

    /**
     * Test {438=[CHROME], 538=[FF68], 649=[IE], 650=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void css__percentage_properties_for_left_and_top_should_be_transformed_to_pixels__see__9505() throws Exception {
        runTest("css: percentage properties for left and top should be transformed to pixels, see #9505");
    }

    /**
     * Test {439=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void css__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("css: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {439=[FF68], 526=[IE], 527=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation___after_String_() throws Exception {
        runTest("manipulation: .after(String)");
    }

    /**
     * Test {440=[FF68], 527=[IE], 528=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation___after_Element_() throws Exception {
        runTest("manipulation: .after(Element)");
    }

    /**
     * Test {440=[CHROME], 540=[FF68], 651=[IE], 652=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "4",
            CHROME = "1, 4, 5")
    public void css__css__width___and_css__height___should_respect_box_sizing__see__11004() throws Exception {
        runTest("css: css('width') and css('height') should respect box-sizing, see #11004");
    }

    /**
     * Test {441=[CHROME], 656=[IE], 657=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 2, 3",
            FF60 = "3",
            IE = "3")
    public void css__certain_css_values_of__normal__should_be_convertable_to_a_number__see__8627() throws Exception {
        runTest("css: certain css values of 'normal' should be convertable to a number, see #8627");
    }

    /**
     * Test {441=[FF68], 528=[IE], 529=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation___after_Array_Element__() throws Exception {
        runTest("manipulation: .after(Array<Element>)");
    }

    /**
     * Test {442=[CHROME], 657=[IE], 658=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "15",
            FF60 = "15")
    public void css__cssHooks___expand() throws Exception {
        runTest("css: cssHooks - expand");
    }

    /**
     * Test {442=[FF68], 529=[IE], 530=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation___after_jQuery_() throws Exception {
        runTest("manipulation: .after(jQuery)");
    }

    /**
     * Test {443=[FF68], 530=[IE], 531=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation___after_Function__returns_String() throws Exception {
        runTest("manipulation: .after(Function) returns String");
    }

    /**
     * Test {443=[CHROME], 542=[FF68], 669=[IE], 670=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "13, 9, 22",
            FF60 = "23",
            FF68 = "13, 9, 22",
            IE = "23")
    public void serialize__jQuery_param__() throws Exception {
        runTest("serialize: jQuery.param()");
    }

    /**
     * Test {444=[FF68], 531=[IE], 532=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation___after_Function__returns_Element() throws Exception {
        runTest("manipulation: .after(Function) returns Element");
    }

    /**
     * Test {444=[CHROME], 543=[FF68], 671=[IE], 672=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void serialize__jQuery_param___Constructed_prop_values() throws Exception {
        runTest("serialize: jQuery.param() Constructed prop values");
    }

    /**
     * Test {445=[FF68], 532=[IE], 533=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation___after_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: .after(Function) returns Array<Element>");
    }

    /**
     * Test {445=[CHROME], 544=[FF68], 672=[IE], 673=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 2, 5",
            FF60 = "6",
            FF68 = "3, 2, 5",
            IE = "6")
    public void serialize__serialize__() throws Exception {
        runTest("serialize: serialize()");
    }

    /**
     * Test {446=[FF68], 533=[IE], 534=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation___after_Function__returns_jQuery() throws Exception {
        runTest("manipulation: .after(Function) returns jQuery");
    }

    /**
     * Test {446=[CHROME], 545=[FF68], 675=[IE], 676=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 2, 5",
            FF60 = "8",
            FF68 = "2, 6, 8",
            IE = "8")
    public void ajax__jQuery_ajax_____success_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks");
    }

    /**
     * Test {447=[FF68], 534=[IE], 535=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation___after_disconnected_node_() throws Exception {
        runTest("manipulation: .after(disconnected node)");
    }

    /**
     * Test {447=[CHROME], 546=[FF68], 676=[IE], 677=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 2, 5",
            FF60 = "8",
            FF68 = "2, 6, 8",
            IE = "8")
    public void ajax__jQuery_ajax_____success_callbacks____url__options__syntax() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks - (url, options) syntax");
    }

    /**
     * Test {448=[FF68], 535=[IE], 536=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insertAfter_String_() throws Exception {
        runTest("manipulation: insertAfter(String)");
    }

    /**
     * Test {448=[CHROME], 547=[FF68], 679=[IE], 680=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 1, 4",
            FF60 = "8",
            FF68 = "3, 3, 6",
            IE = "8")
    public void ajax__jQuery_ajax_____success_callbacks__late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (late binding)");
    }

    /**
     * Test {449=[FF68], 536=[IE], 537=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insertAfter_Element_() throws Exception {
        runTest("manipulation: insertAfter(Element)");
    }

    /**
     * Test {449=[CHROME], 548=[FF68], 680=[IE], 681=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4, 1, 5",
            FF60 = "8",
            FF68 = "5, 3, 8",
            IE = "8")
    public void ajax__jQuery_ajax_____success_callbacks__oncomplete_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (oncomplete binding)");
    }

    /**
     * Test {450=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 1, 4")
    public void ajax__jQuery_ajax_____success_callbacks__very_late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (very late binding)");
    }

    /**
     * Test {450=[FF68], 537=[IE], 538=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insertAfter_Array_Element__() throws Exception {
        runTest("manipulation: insertAfter(Array<Element>)");
    }

    /**
     * Test {451=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__jQuery_ajax_____success_callbacks__order_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (order)");
    }

    /**
     * Test {451=[IE], 452=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void traversing__contents___for__template___() throws Exception {
        runTest("traversing: contents() for <template />");
    }

    /**
     * Test {451=[FF68], 538=[IE], 539=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__insertAfter_jQuery_() throws Exception {
        runTest("manipulation: insertAfter(jQuery)");
    }

    /**
     * Test {452=[IE], 453=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "0")
    public void traversing__contents___for__template____remains_inert() throws Exception {
        runTest("traversing: contents() for <template /> remains inert");
    }

    /**
     * Test {452=[FF68], 539=[IE], 540=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "29",
            IE = "29")
    public void manipulation__replaceWith_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {452=[CHROME], 549=[FF68], 681=[IE], 682=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 3, 5",
            FF60 = "8",
            FF68 = "1, 6, 7",
            IE = "8")
    public void ajax__jQuery_ajax_____error_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - error callbacks");
    }

    /**
     * Test {453=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___multiple_method_signatures_introduced_in_1_5____8107_() throws Exception {
        runTest("ajax: jQuery.ajax - multiple method signatures introduced in 1.5 ( #8107)");
    }

    /**
     * Test {454=[CHROME], 550=[FF68], 682=[IE], 683=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void ajax__jQuery_ajax_____textStatus_and_errorThrown_values() throws Exception {
        runTest("ajax: jQuery.ajax() - textStatus and errorThrown values");
    }

    /**
     * Test {455=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "8")
    public void manipulation__Empty_replaceWith___13401___13596_() throws Exception {
        runTest("manipulation: Empty replaceWith (#13401; #13596)");
    }

    /**
     * Test {455=[CHROME], 551=[FF68], 683=[IE], 684=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax__jQuery_ajax_____responseText_on_error() throws Exception {
        runTest("ajax: jQuery.ajax() - responseText on error");
    }

    /**
     * Test {456=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void ajax___ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: .ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {456=[FF68], 543=[IE], 544=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__replaceAll_String_() throws Exception {
        runTest("manipulation: replaceAll(String)");
    }

    /**
     * Test {457=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax___ajax_____headers() throws Exception {
        runTest("ajax: .ajax() - headers");
    }

    /**
     * Test {457=[FF68], 544=[IE], 545=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__replaceAll_Element_() throws Exception {
        runTest("manipulation: replaceAll(Element)");
    }

    /**
     * Test {458=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax___ajax_____Accept_header() throws Exception {
        runTest("ajax: .ajax() - Accept header");
    }

    /**
     * Test {458=[FF68], 545=[IE], 546=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "3",
            IE = "3")
    public void manipulation__replaceAll_Array_Element__() throws Exception {
        runTest("manipulation: replaceAll(Array<Element>)");
    }

    /**
     * Test {459=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax___ajax_____contentType() throws Exception {
        runTest("ajax: .ajax() - contentType");
    }

    /**
     * Test {459=[FF68], 546=[IE], 547=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "3",
            IE = "3")
    public void manipulation__replaceAll_jQuery_() throws Exception {
        runTest("manipulation: replaceAll(jQuery)");
    }

    /**
     * Test {460=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void ajax___ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: .ajax() - protocol-less urls");
    }

    /**
     * Test {461=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax___ajax_____hash() throws Exception {
        runTest("ajax: .ajax() - hash");
    }

    /**
     * Test {461=[FF68], 548=[IE], 549=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__append_to_multiple_elements___8070_() throws Exception {
        runTest("manipulation: append to multiple elements (#8070)");
    }

    /**
     * Test {462=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6")
    public void ajax__jQuery_ajax___cross_domain_detection() throws Exception {
        runTest("ajax: jQuery ajax - cross-domain detection");
    }

    /**
     * Test {462=[FF68], 549=[IE], 550=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    @NotYetImplemented
    public void manipulation__table_manipulation() throws Exception {
        runTest("manipulation: table manipulation");
    }

    /**
     * Test {463=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 1, 3")
    public void ajax___load_____404_error_callbacks() throws Exception {
        runTest("ajax: .load() - 404 error callbacks");
    }

    /**
     * Test {464=[CHROME], 559=[FF68], 692=[IE], 693=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 4, 6",
            FF60 = "9",
            FF68 = "1, 7, 8",
            IE = "9")
    public void ajax__jQuery_ajax_____abort() throws Exception {
        runTest("ajax: jQuery.ajax() - abort");
    }

    /**
     * Test {465=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "14")
    public void ajax__Ajax_events_with_context() throws Exception {
        runTest("ajax: Ajax events with context");
    }

    /**
     * Test {466=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void ajax__jQuery_ajax_context_modification() throws Exception {
        runTest("ajax: jQuery.ajax context modification");
    }

    /**
     * Test {467=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4")
    public void ajax__jQuery_ajax_context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax context modification through ajaxSetup");
    }

    /**
     * Test {468=[CHROME], 565=[FF68], 701=[IE], 702=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 2, 3",
            FF60 = "3",
            FF68 = "1, 2, 3",
            IE = "3")
    public void ajax__jQuery_ajax_____disabled_globals() throws Exception {
        runTest("ajax: jQuery.ajax() - disabled globals");
    }

    /**
     * Test {469=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {470=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {471=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax - HEAD requests");
    }

    /**
     * Test {471=[FF68], 558=[IE], 559=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "40",
            FF68 = "39",
            IE = "40")
    public void manipulation__html_String_Number_() throws Exception {
        runTest("manipulation: html(String|Number)");
    }

    /**
     * Test {472=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__jQuery_ajax___beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend");
    }

    /**
     * Test {473=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request___2688_() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request (#2688)");
    }

    /**
     * Test {473=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "1")
    public void manipulation__html____text____() throws Exception {
        runTest("manipulation: html( $.text() )");
    }

    /**
     * Test {474=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request manually");
    }

    /**
     * Test {474=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "1")
    public void manipulation__html__fn___returns___text__() throws Exception {
        runTest("manipulation: html( fn ) returns $.text()");
    }

    /**
     * Test {475=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax - dataType html");
    }

    /**
     * Test {475=[FF68], 561=[IE], 562=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void manipulation__html_Function__with_incoming_value____direct_selection() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- direct selection");
    }

    /**
     * Test {476=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__synchronous_request() throws Exception {
        runTest("ajax: synchronous request");
    }

    /**
     * Test {476=[FF68], 562=[IE], 563=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "14",
            IE = "14")
    public void manipulation__html_Function__with_incoming_value____jQuery_contents__() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- jQuery.contents()");
    }

    /**
     * Test {477=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: synchronous request with callbacks");
    }

    /**
     * Test {477=[FF68], 563=[IE], 564=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void manipulation__clone___html___don_t_expose_jQuery_Sizzle_expandos___12858_() throws Exception {
        runTest("manipulation: clone()/html() don't expose jQuery/Sizzle expandos (#12858)");
    }

    /**
     * Test {478=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 5, 8")
    public void ajax__pass_through_request_object() throws Exception {
        runTest("ajax: pass-through request object");
    }

    /**
     * Test {478=[FF68], 564=[IE], 565=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "3",
            IE = "2")
    public void manipulation__remove___no_filters() throws Exception {
        runTest("manipulation: remove() no filters");
    }

    /**
     * Test {479=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 6, 8")
    public void ajax__ajax_cache() throws Exception {
        runTest("ajax: ajax cache");
    }

    /**
     * Test {479=[FF68], 565=[IE], 566=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "8",
            IE = "8")
    public void manipulation__remove___with_filters() throws Exception {
        runTest("manipulation: remove() with filters");
    }

    /**
     * Test {480=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void ajax__load_String_() throws Exception {
        runTest("ajax: load(String)");
    }

    /**
     * Test {480=[FF68], 566=[IE], 567=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__remove___event_cleaning() throws Exception {
        runTest("manipulation: remove() event cleaning");
    }

    /**
     * Test {481=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void ajax__load_String_null_() throws Exception {
        runTest("ajax: load(String,null)");
    }

    /**
     * Test {481=[FF68], 567=[IE], 568=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__remove___in_document_order__13779() throws Exception {
        runTest("manipulation: remove() in document order #13779");
    }

    /**
     * Test {482=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void ajax__load_String_undefined_() throws Exception {
        runTest("ajax: load(String,undefined)");
    }

    /**
     * Test {482=[FF68], 568=[IE], 569=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "3",
            IE = "3")
    public void manipulation__detach___no_filters() throws Exception {
        runTest("manipulation: detach() no filters");
    }

    /**
     * Test {483=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void ajax__load__url_selector__() throws Exception {
        runTest("ajax: load('url selector')");
    }

    /**
     * Test {483=[IE], 484=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "27",
            IE = "27")
    public void manipulation__html_String__tag_hyphenated_elements__Bug__1987_() throws Exception {
        runTest("manipulation: html(String) tag-hyphenated elements (Bug #1987)");
    }

    /**
     * Test {483=[FF68], 569=[IE], 570=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "8",
            IE = "8")
    public void manipulation__detach___with_filters() throws Exception {
        runTest("manipulation: detach() with filters");
    }

    /**
     * Test {484=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__load_String__Function__with_ajaxSetup_on_dataType_json__see__2046() throws Exception {
        runTest("ajax: load(String, Function) with ajaxSetup on dataType json, see #2046");
    }

    /**
     * Test {484=[IE], 485=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "240",
            IE = "240")
    @NotYetImplemented
    public void manipulation__Tag_name_processing_respects_the_HTML_Standard__gh_2005_() throws Exception {
        runTest("manipulation: Tag name processing respects the HTML Standard (gh-2005)");
    }

    /**
     * Test {484=[FF68], 570=[IE], 571=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void manipulation__detach___event_cleaning() throws Exception {
        runTest("manipulation: detach() event cleaning");
    }

    /**
     * Test {485=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__load_String__Function____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: load(String, Function) - simple: inject text into DOM");
    }

    /**
     * Test {486=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6, 0, 6")
    public void ajax__load_String__Function____check_scripts() throws Exception {
        runTest("ajax: load(String, Function) - check scripts");
    }

    /**
     * Test {487=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__load_String__Function____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: load(String, Function) - check file with only a script tag");
    }

    /**
     * Test {488=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__load_String__Function____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: load(String, Function) - dataFilter in ajaxSettings");
    }

    /**
     * Test {489=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__load_String__Object__Function_() throws Exception {
        runTest("ajax: load(String, Object, Function)");
    }

    /**
     * Test {490=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__load_String__String__Function_() throws Exception {
        runTest("ajax: load(String, String, Function)");
    }

    /**
     * Test {491=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__load_____data_specified_in_ajaxSettings_is_merged_in___10524_() throws Exception {
        runTest("ajax: load() - data specified in ajaxSettings is merged in (#10524)");
    }

    /**
     * Test {492=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "8")
    public void ajax__load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: load() - callbacks get the correct parameters");
    }

    /**
     * Test {493=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__jQuery_get_String__Function____data_in_ajaxSettings___8277_() throws Exception {
        runTest("ajax: jQuery.get(String, Function) - data in ajaxSettings (#8277)");
    }

    /**
     * Test {494=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_get_String__Hash__Function____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get(String, Hash, Function) - parse xml and use text() on nodes");
    }

    /**
     * Test {495=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__jQuery_getScript_String__Function____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - with callback");
    }

    /**
     * Test {496=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__jQuery_getScript_String__Function____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - no callback");
    }

    /**
     * Test {497=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "33, 2, 35")
    public void ajax__jQuery_ajax_____JSONP__Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Same Domain");
    }

    /**
     * Test {498=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "17, 2, 19")
    public void ajax__jQuery_ajax_____JSONP__Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Cross Domain");
    }

    /**
     * Test {499=[CHROME], 586=[FF68], 722=[IE], 723=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void ajax__jQuery_ajax_____script__Remote() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote");
    }

    /**
     * Test {500=[CHROME], 587=[FF68], 723=[IE], 724=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3",
            FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void ajax__jQuery_ajax_____script__Remote_with_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with POST");
    }

    /**
     * Test {501=[CHROME], 588=[FF68], 724=[IE], 725=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void ajax__jQuery_ajax_____script__Remote_with_scheme_less_URL() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with scheme-less URL");
    }

    /**
     * Test {502=[CHROME], 589=[FF68], 725=[IE], 726=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void ajax__jQuery_ajax_____malformed_JSON() throws Exception {
        runTest("ajax: jQuery.ajax() - malformed JSON");
    }

    /**
     * Test {503=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__jQuery_ajax_____script__throws_exception___11743_() throws Exception {
        runTest("ajax: jQuery.ajax() - script, throws exception (#11743)");
    }

    /**
     * Test {504=[CHROME], 590=[FF68], 726=[IE], 727=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void ajax__jQuery_ajax_____script_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - script by content-type");
    }

    /**
     * Test {505=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__jQuery_ajax_____json_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type");
    }

    /**
     * Test {506=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax_____json_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type disabled with options");
    }

    /**
     * Test {507=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_getJSON_String__Hash__Function____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Hash, Function) - JSON array");
    }

    /**
     * Test {508=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object");
    }

    /**
     * Test {509=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_getJSON___Using_Native_JSON() throws Exception {
        runTest("ajax: jQuery.getJSON - Using Native JSON");
    }

    /**
     * Test {510=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object with absolute url to local content");
    }

    /**
     * Test {511=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__jQuery_post___data() throws Exception {
        runTest("ajax: jQuery.post - data");
    }

    /**
     * Test {512=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_post_String__Hash__Function____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post(String, Hash, Function) - simple with xml");
    }

    /**
     * Test {513=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajaxSetup__timeout__Number_____with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) - with global timeout");
    }

    /**
     * Test {514=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajaxSetup__timeout__Number___with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) with localtimeout");
    }

    /**
     * Test {514=[FF68], 623=[IE], 624=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void css__css_Array_() throws Exception {
        runTest("css: css(Array)");
    }

    /**
     * Test {515=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__jQuery_ajax___simple_get() throws Exception {
        runTest("ajax: jQuery.ajax - simple get");
    }

    /**
     * Test {516=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__jQuery_ajax___simple_post() throws Exception {
        runTest("ajax: jQuery.ajax - simple post");
    }

    /**
     * Test {517=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__ajaxSetup__() throws Exception {
        runTest("ajax: ajaxSetup()");
    }

    /**
     * Test {518=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__data_option__evaluate_function_values___2806_() throws Exception {
        runTest("ajax: data option: evaluate function values (#2806)");
    }

    /**
     * Test {519=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__data_option__empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: data option: empty bodies for non-GET requests");
    }

    /**
     * Test {520=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (cache)");
    }

    /**
     * Test {521=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (cache)");
    }

    /**
     * Test {522=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (no cache)");
    }

    /**
     * Test {522=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "18")
    public void css__show___() throws Exception {
        runTest("css: show();");
    }

    /**
     * Test {523=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (no cache)");
    }

    /**
     * Test {524=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void ajax__jQuery_ajax___failing_cross_domain() throws Exception {
        runTest("ajax: jQuery ajax - failing cross-domain");
    }

    /**
     * Test {524=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "7, 6, 13")
    public void css__show___resolves_correct_default_display_for_detached_nodes() throws Exception {
        runTest("css: show() resolves correct default display for detached nodes");
    }

    /**
     * Test {525=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___atom_xml() throws Exception {
        runTest("ajax: jQuery ajax - atom+xml");
    }

    /**
     * Test {525=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "4")
    public void css__show___resolves_correct_default_display__10227() throws Exception {
        runTest("css: show() resolves correct default display #10227");
    }

    /**
     * Test {526=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void ajax__jQuery_ajax___Location_object_as_url___7531_() throws Exception {
        runTest("ajax: jQuery.ajax - Location object as url (#7531)");
    }

    /**
     * Test {526=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2")
    public void css__show___resolves_correct_default_display_when_iframe_display_none__12904() throws Exception {
        runTest("css: show() resolves correct default display when iframe display:none #12904");
    }

    /**
     * Test {527=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void ajax__jQuery_ajax___Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: jQuery.ajax - Context with circular references (#9887)");
    }

    /**
     * Test {528=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___statusText() throws Exception {
        runTest("ajax: jQuery.ajax - statusText");
    }

    /**
     * Test {529=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9, 11, 20")
    public void ajax__jQuery_ajax___statusCode() throws Exception {
        runTest("ajax: jQuery.ajax - statusCode");
    }

    /**
     * Test {530=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax - transitive conversions");
    }

    /**
     * Test {531=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__jQuery_ajax___overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax - overrideMimeType");
    }

    /**
     * Test {532=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void ajax__jQuery_ajax___abort_in_prefilter() throws Exception {
        runTest("ajax: jQuery.ajax - abort in prefilter");
    }

    /**
     * Test {533=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3")
    public void ajax__jQuery_ajax___loading_binary_data_shouldn_t_throw_an_exception_in_IE___11426_() throws Exception {
        runTest("ajax: jQuery.ajax - loading binary data shouldn't throw an exception in IE (#11426)");
    }

    /**
     * Test {534=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1")
    public void ajax__jQuery_domManip___no_side_effect_because_of_ajaxSetup_or_global_events___11264_() throws Exception {
        runTest("ajax: jQuery.domManip - no side effect because of ajaxSetup or global events (#11264)");
    }

    /**
     * Test {535=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void ajax__jQuery_domManip___script_in_comments_are_properly_evaluated___11402_() throws Exception {
        runTest("ajax: jQuery.domManip - script in comments are properly evaluated (#11402)");
    }

    /**
     * Test {536=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2")
    public void ajax__jQuery_ajax___active_counter() throws Exception {
        runTest("ajax: jQuery.ajax - active counter");
    }

    /**
     * Test {537=[CHROME], 630=[FF68], 816=[IE], 817=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void effects__sanity_check() throws Exception {
        runTest("effects: sanity check");
    }

    /**
     * Test {538=[CHROME], 632=[FF68], 818=[IE], 819=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "26",
            FF60 = "27",
            FF68 = "2, 13, 15",
            IE = "27")
    @NotYetImplemented({ CHROME, FF })
    public void effects__show__() throws Exception {
        runTest("effects: show()");
    }

    /**
     * Test {539=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2")
    public void css__Do_not_append_px___9548___12990_() throws Exception {
        runTest("css: Do not append px (#9548, #12990)");
    }

    /**
     * Test {539=[CHROME], 633=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "3, 0, 3")
    public void effects__show_Number____other_displays() throws Exception {
        runTest("effects: show(Number) - other displays");
    }

    /**
     * Test {540=[CHROME], 634=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 2, 3",
            FF68 = "3, 0, 3")
    public void effects__Persist_correct_display_value() throws Exception {
        runTest("effects: Persist correct display value");
    }

    /**
     * Test {541=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2, 0, 2")
    public void css__global_failure() throws Exception {
        runTest("css: global failure");
    }

    /**
     * Test {541=[CHROME], 635=[FF68], 823=[IE], 824=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__animate_Hash__Object__Function_() throws Exception {
        runTest("effects: animate(Hash, Object, Function)");
    }

    /**
     * Test {542=[IE], 543=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "25",
            IE = "25")
    public void manipulation__Empty_replaceWith__trac_13401__trac_13596__gh_2204_() throws Exception {
        runTest("manipulation: Empty replaceWith (trac-13401; trac-13596; gh-2204)");
    }

    /**
     * Test {542=[CHROME], 637=[FF68], 825=[IE], 826=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void effects__animate_negative_height() throws Exception {
        runTest("effects: animate negative height");
    }

    /**
     * Test {543=[CHROME], 638=[FF68], 826=[IE], 827=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void effects__animate_negative_margin() throws Exception {
        runTest("effects: animate negative margin");
    }

    /**
     * Test {544=[CHROME], 639=[FF68], 827=[IE], 828=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void effects__animate_negative_margin_with_px() throws Exception {
        runTest("effects: animate negative margin with px");
    }

    /**
     * Test {545=[CHROME], 640=[FF68], 828=[IE], 829=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    @NotYetImplemented
    public void effects__animate_negative_padding() throws Exception {
        runTest("effects: animate negative padding");
    }

    /**
     * Test {546=[CHROME], 641=[FF68], 829=[IE], 830=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3",
            FF60 = "3",
            FF68 = "2, 0, 2",
            IE = "3")
    public void effects__animate_block_as_inline_width_height() throws Exception {
        runTest("effects: animate block as inline width/height");
    }

    /**
     * Test {547=[CHROME], 642=[FF68], 830=[IE], 831=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3",
            FF60 = "3",
            FF68 = "2, 0, 2",
            IE = "3")
    @NotYetImplemented(IE)
    public void effects__animate_native_inline_width_height() throws Exception {
        runTest("effects: animate native inline width/height");
    }

    /**
     * Test {548=[CHROME], 643=[FF68], 831=[IE], 832=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3",
            FF60 = "3",
            FF68 = "2, 0, 2",
            IE = "3")
    public void effects__animate_block_width_height() throws Exception {
        runTest("effects: animate block width/height");
    }

    /**
     * Test {549=[CHROME], 644=[FF68], 832=[IE], 833=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void effects__animate_table_width_height() throws Exception {
        runTest("effects: animate table width/height");
    }

    /**
     * Test {550=[CHROME], 645=[FF68], 833=[IE], 834=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3",
            FF60 = "3",
            FF68 = "2, 0, 2",
            IE = "3")
    @NotYetImplemented
    public void effects__animate_table_row_width_height() throws Exception {
        runTest("effects: animate table-row width/height");
    }

    /**
     * Test {551=[CHROME], 646=[FF68], 834=[IE], 835=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3",
            FF60 = "3",
            FF68 = "2, 0, 2",
            IE = "3")
    @NotYetImplemented
    public void effects__animate_table_cell_width_height() throws Exception {
        runTest("effects: animate table-cell width/height");
    }

    /**
     * Test {552=[FF68], 684=[IE], 685=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void ajax__jQuery_ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: jQuery.ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {552=[CHROME], 647=[FF68], 835=[IE], 836=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void effects__animate_percentage____on_width_height() throws Exception {
        runTest("effects: animate percentage(%) on width/height");
    }

    /**
     * Test {553=[FF68], 685=[IE], 686=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void ajax__jQuery_ajax_____headers() throws Exception {
        runTest("ajax: jQuery.ajax() - headers");
    }

    /**
     * Test {553=[CHROME], 648=[FF68], 836=[IE], 837=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void effects__animate_resets_overflow_x_and_overflow_y_when_finished() throws Exception {
        runTest("effects: animate resets overflow-x and overflow-y when finished");
    }

    /**
     * Test {554=[FF68], 686=[IE], 687=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax__jQuery_ajax_____Accept_header() throws Exception {
        runTest("ajax: jQuery.ajax() - Accept header");
    }

    /**
     * Test {554=[CHROME], 649=[FF68], 837=[IE], 838=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF60 = "2",
            FF68 = "1, 1, 2",
            IE = "2")
    public void effects__animate_option___queue__false__() throws Exception {
        runTest("effects: animate option { queue: false }");
    }

    /**
     * Test {555=[FF68], 687=[IE], 688=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void ajax__jQuery_ajax_____contentType() throws Exception {
        runTest("ajax: jQuery.ajax() - contentType");
    }

    /**
     * Test {555=[CHROME], 650=[FF68], 838=[IE], 839=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF60 = "2",
            FF68 = "1, 1, 2",
            IE = "2")
    public void effects__animate_option___queue__true__() throws Exception {
        runTest("effects: animate option { queue: true }");
    }

    /**
     * Test {556=[FF68], 688=[IE], 689=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void ajax__jQuery_ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: jQuery.ajax() - protocol-less urls");
    }

    /**
     * Test {556=[CHROME], 651=[FF68], 839=[IE], 840=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5",
            FF60 = "5",
            FF68 = "2, 2, 4",
            IE = "5")
    public void effects__animate_option___queue___name___() throws Exception {
        runTest("effects: animate option { queue: 'name' }");
    }

    /**
     * Test {557=[FF68], 689=[IE], 690=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "3, 0, 3",
            IE = "4")
    public void ajax__jQuery_ajax_____hash() throws Exception {
        runTest("ajax: jQuery.ajax() - hash");
    }

    /**
     * Test {557=[CHROME], 652=[FF68], 840=[IE], 841=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF60 = "2",
            FF68 = "1, 1, 2",
            IE = "2")
    public void effects__animate_with_no_properties() throws Exception {
        runTest("effects: animate with no properties");
    }

    /**
     * Test {558=[FF68], 691=[IE], 692=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "8",
            FF68 = "1, 6, 7",
            IE = "8")
    public void ajax__jQuery_ajax_____cross_domain_detection() throws Exception {
        runTest("ajax: jQuery.ajax() - cross-domain detection");
    }

    /**
     * Test {558=[CHROME], 653=[FF68], 841=[IE], 842=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 10, 11",
            FF60 = "11",
            FF68 = "2, 8, 10",
            IE = "11")
    public void effects__animate_duration_0() throws Exception {
        runTest("effects: animate duration 0");
    }

    /**
     * Test {559=[CHROME], 654=[FF68], 842=[IE], 843=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void effects__animate_hyphenated_properties() throws Exception {
        runTest("effects: animate hyphenated properties");
    }

    /**
     * Test {560=[IE], 561=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "0")
    @NotYetImplemented({ CHROME, FF60 })
    public void manipulation__html_script_type_module_() throws Exception {
        runTest("manipulation: html(script type module)");
    }

    /**
     * Test {560=[FF68], 695=[IE], 696=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "12",
            FF68 = "1, 11, 12",
            IE = "12")
    public void ajax__jQuery_ajax_____events_with_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events with context");
    }

    /**
     * Test {560=[CHROME], 655=[FF68], 843=[IE], 844=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void effects__animate_non_element() throws Exception {
        runTest("effects: animate non-element");
    }

    /**
     * Test {561=[FF68], 696=[IE], 697=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "3",
            IE = "3")
    public void ajax__jQuery_ajax_____events_without_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events without context");
    }

    /**
     * Test {561=[CHROME], 656=[FF68], 844=[IE], 845=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "3, 1, 4",
            IE = "4")
    public void effects__stop__() throws Exception {
        runTest("effects: stop()");
    }

    /**
     * Test {562=[FF68], 697=[IE], 698=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "2, 1, 3",
            IE = "1")
    public void ajax___15118___jQuery_ajax_____function_without_jQuery_event() throws Exception {
        runTest("ajax: #15118 - jQuery.ajax() - function without jQuery.event");
    }

    /**
     * Test {562=[CHROME], 657=[FF68], 845=[IE], 846=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "5",
            CHROME = "3")
    public void effects__stop_____several_in_queue() throws Exception {
        runTest("effects: stop() - several in queue");
    }

    /**
     * Test {563=[FF68], 699=[IE], 700=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "2, 1, 3",
            IE = "1")
    public void ajax__jQuery_ajax_____context_modification() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification");
    }

    /**
     * Test {563=[CHROME], 658=[FF68], 846=[IE], 847=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 2, 4",
            IE = "4")
    public void effects__stop_clearQueue_() throws Exception {
        runTest("effects: stop(clearQueue)");
    }

    /**
     * Test {564=[FF68], 700=[IE], 701=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "2, 1, 3",
            IE = "3")
    public void ajax__jQuery_ajax_____context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification through ajaxSetup");
    }

    /**
     * Test {564=[CHROME], 659=[FF68], 847=[IE], 848=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1",
            FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void effects__stop_clearQueue__gotoEnd_() throws Exception {
        runTest("effects: stop(clearQueue, gotoEnd)");
    }

    /**
     * Test {565=[CHROME], 660=[FF68], 848=[IE], 849=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3",
            FF60 = "3",
            FF68 = "2, 1, 3",
            IE = "3")
    public void effects__stop__queue_______________Stop_single_queues() throws Exception {
        runTest("effects: stop( queue, ..., ... ) - Stop single queues");
    }

    /**
     * Test {566=[FF68], 702=[IE], 703=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "2, 0, 2",
            IE = "3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {566=[CHROME], 661=[FF68], 849=[IE], 850=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("6")
    public void effects__toggle__() throws Exception {
        runTest("effects: toggle()");
    }

    /**
     * Test {567=[FF68], 703=[IE], 704=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {567=[CHROME], 662=[FF68], 850=[IE], 851=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("7")
    public void effects__jQuery_fx_prototype_cur______1_8_Back_Compat() throws Exception {
        runTest("effects: jQuery.fx.prototype.cur() - <1.8 Back Compat");
    }

    /**
     * Test {568=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void effects__JS_Overflow_and_Display() throws Exception {
        runTest("effects: JS Overflow and Display");
    }

    /**
     * Test {568=[FF68], 704=[IE], 705=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void ajax__jQuery_ajax_____HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - HEAD requests");
    }

    /**
     * Test {569=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2")
    public void effects__CSS_Overflow_and_Display() throws Exception {
        runTest("effects: CSS Overflow and Display");
    }

    /**
     * Test {569=[FF68], 705=[IE], 706=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax__jQuery_ajax_____beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend");
    }

    /**
     * Test {570=[FF68], 706=[IE], 707=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void ajax__jQuery_ajax_____beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend, cancel request manually");
    }

    /**
     * Test {570=[CHROME], 664=[FF68], 852=[IE], 853=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_Auto_to_0() throws Exception {
        runTest("effects: CSS Auto to 0");
    }

    /**
     * Test {571=[FF68], 707=[IE], 708=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void ajax__jQuery_ajax_____dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax() - dataType html");
    }

    /**
     * Test {571=[CHROME], 665=[FF68], 853=[IE], 854=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_Auto_to_50() throws Exception {
        runTest("effects: CSS Auto to 50");
    }

    /**
     * Test {572=[FF68], 708=[IE], 709=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "3, 0, 3",
            IE = "1")
    public void ajax__jQuery_ajax_____synchronous_request() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request");
    }

    /**
     * Test {572=[CHROME], 666=[FF68], 854=[IE], 855=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_Auto_to_100() throws Exception {
        runTest("effects: CSS Auto to 100");
    }

    /**
     * Test {573=[IE], 574=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void manipulation__jQuery_cleanData_eliminates_all_private_data__gh_2127_() throws Exception {
        runTest("manipulation: jQuery.cleanData eliminates all private data (gh-2127)");
    }

    /**
     * Test {573=[FF68], 709=[IE], 710=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void ajax__jQuery_ajax_____synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request with callbacks");
    }

    /**
     * Test {573=[CHROME], 667=[FF68], 855=[IE], 856=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 4, 5",
            FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    @NotYetImplemented
    public void effects__CSS_Auto_to_show() throws Exception {
        runTest("effects: CSS Auto to show");
    }

    /**
     * Test {574=[IE], 575=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void manipulation__jQuery_cleanData_eliminates_all_public_data() throws Exception {
        runTest("manipulation: jQuery.cleanData eliminates all public data");
    }

    /**
     * Test {574=[FF68], 710=[IE], 711=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "8",
            FF68 = "3, 5, 8",
            IE = "8")
    public void ajax__jQuery_ajax____jQuery_get_Script_JSON_____jQuery_post____pass_through_request_object() throws Exception {
        runTest("ajax: jQuery.ajax(), jQuery.get[Script|JSON](), jQuery.post(), pass-through request object");
    }

    /**
     * Test {574=[CHROME], 668=[FF68], 856=[IE], 857=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__CSS_Auto_to_hide() throws Exception {
        runTest("effects: CSS Auto to hide");
    }

    /**
     * Test {575=[IE], 576=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void manipulation__domManip_plain_text_caching__trac_6779_() throws Exception {
        runTest("manipulation: domManip plain-text caching (trac-6779)");
    }

    /**
     * Test {575=[FF68], 711=[IE], 712=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "28",
            FF68 = "12",
            IE = "28")
    public void ajax__jQuery_ajax_____cache() throws Exception {
        runTest("ajax: jQuery.ajax() - cache");
    }

    /**
     * Test {575=[CHROME], 669=[FF68], 857=[IE], 858=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_Auto_to_0() throws Exception {
        runTest("effects: JS Auto to 0");
    }

    /**
     * Test {576=[IE], 577=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void manipulation__domManip_executes_scripts_containing_html_comments_or_CDATA__trac_9221_() throws Exception {
        runTest("manipulation: domManip executes scripts containing html comments or CDATA (trac-9221)");
    }

    /**
     * Test {576=[FF68], 712=[IE], 713=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5, 2, 7",
            FF68 = "9, 0, 9",
            IE = "5, 2, 7")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Same Domain");
    }

    /**
     * Test {576=[CHROME], 670=[FF68], 858=[IE], 859=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_Auto_to_50() throws Exception {
        runTest("effects: JS Auto to 50");
    }

    /**
     * Test {577=[IE], 578=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void manipulation__domManip_tolerates_window_valued_document_0__in_IE9_10__trac_12266_() throws Exception {
        runTest("manipulation: domManip tolerates window-valued document[0] in IE9/10 (trac-12266)");
    }

    /**
     * Test {577=[FF68], 713=[IE], 714=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "10",
            FF68 = "8, 1, 9",
            IE = "10")
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Same Domain");
    }

    /**
     * Test {577=[CHROME], 671=[FF68], 859=[IE], 860=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_Auto_to_100() throws Exception {
        runTest("effects: JS Auto to 100");
    }

    /**
     * Test {578=[IE], 579=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void manipulation__domManip_executes_scripts_in_iframes_in_the_iframes__context() throws Exception {
        runTest("manipulation: domManip executes scripts in iframes in the iframes' context");
    }

    /**
     * Test {578=[FF68], 714=[IE], 715=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "5, 0, 5",
            IE = "2")
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Same Domain");
    }

    /**
     * Test {578=[CHROME], 672=[FF68], 860=[IE], 861=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 4, 5",
            FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    @NotYetImplemented
    public void effects__JS_Auto_to_show() throws Exception {
        runTest("effects: JS Auto to show");
    }

    /**
     * Test {579=[FF68], 715=[IE], 716=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "7, 0, 7",
            IE = "3")
    public void ajax__jQuery_ajax_____JSONP___POST___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Same Domain");
    }

    /**
     * Test {579=[CHROME], 673=[FF68], 861=[IE], 862=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__JS_Auto_to_hide() throws Exception {
        runTest("effects: JS Auto to hide");
    }

    /**
     * Test {580=[FF68], 716=[IE], 717=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "5, 0, 5",
            IE = "3")
    public void ajax__jQuery_ajax_____JSONP___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Same Domain");
    }

    /**
     * Test {580=[CHROME], 674=[FF68], 862=[IE], 863=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_100_to_0() throws Exception {
        runTest("effects: CSS 100 to 0");
    }

    /**
     * Test {581=[FF68], 717=[IE], 718=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2, 2, 4",
            FF68 = "4, 0, 4",
            IE = "2, 2, 4")
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Cross Domain");
    }

    /**
     * Test {581=[CHROME], 675=[FF68], 863=[IE], 864=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_100_to_50() throws Exception {
        runTest("effects: CSS 100 to 50");
    }

    /**
     * Test {582=[FF68], 718=[IE], 719=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "10",
            FF68 = "5, 1, 6",
            IE = "10")
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Cross Domain");
    }

    /**
     * Test {582=[CHROME], 676=[FF68], 864=[IE], 865=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_100_to_100() throws Exception {
        runTest("effects: CSS 100 to 100");
    }

    /**
     * Test {583=[FF68], 719=[IE], 720=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Cross Domain");
    }

    /**
     * Test {583=[CHROME], 677=[FF68], 865=[IE], 866=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 4, 5",
            FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void effects__CSS_100_to_show() throws Exception {
        runTest("effects: CSS 100 to show");
    }

    /**
     * Test {584=[FF68], 720=[IE], 721=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void ajax__jQuery_ajax_____JSONP___POST___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Cross Domain");
    }

    /**
     * Test {584=[CHROME], 678=[FF68], 866=[IE], 867=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__CSS_100_to_hide() throws Exception {
        runTest("effects: CSS 100 to hide");
    }

    /**
     * Test {585=[FF68], 721=[IE], 722=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void ajax__jQuery_ajax_____JSONP___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Cross Domain");
    }

    /**
     * Test {585=[CHROME], 679=[FF68], 867=[IE], 868=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_100_to_0() throws Exception {
        runTest("effects: JS 100 to 0");
    }

    /**
     * Test {586=[IE], 587=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "13",
            IE = "13")
    @NotYetImplemented
    public void manipulation__script_evaluation___11795_() throws Exception {
        runTest("manipulation: script evaluation (#11795)");
    }

    /**
     * Test {586=[CHROME], 680=[FF68], 868=[IE], 869=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_100_to_50() throws Exception {
        runTest("effects: JS 100 to 50");
    }

    /**
     * Test {587=[IE], 588=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            IE = "5")
    public void manipulation__jQuery__evalUrl___12838_() throws Exception {
        runTest("manipulation: jQuery._evalUrl (#12838)");
    }

    /**
     * Test {587=[CHROME], 681=[FF68], 869=[IE], 870=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_100_to_100() throws Exception {
        runTest("effects: JS 100 to 100");
    }

    /**
     * Test {588=[IE], 589=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            IE = "5")
    public void manipulation__jQuery_htmlPrefilter__gh_1747_() throws Exception {
        runTest("manipulation: jQuery.htmlPrefilter (gh-1747)");
    }

    /**
     * Test {588=[CHROME], 682=[FF68], 870=[IE], 871=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 4, 5",
            FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void effects__JS_100_to_show() throws Exception {
        runTest("effects: JS 100 to show");
    }

    /**
     * Test {589=[IE], 590=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "10",
            IE = "10")
    public void manipulation__insertAfter__insertBefore__etc_do_not_work_when_destination_is_original_element__Element_is_removed___4087_() throws Exception {
        runTest("manipulation: insertAfter, insertBefore, etc do not work when destination is original element. Element is removed (#4087)");
    }

    /**
     * Test {589=[CHROME], 683=[FF68], 871=[IE], 872=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__JS_100_to_hide() throws Exception {
        runTest("effects: JS 100 to hide");
    }

    /**
     * Test {590=[IE], 591=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void manipulation__Index_for_function_argument_should_be_received___13094_() throws Exception {
        runTest("manipulation: Index for function argument should be received (#13094)");
    }

    /**
     * Test {590=[CHROME], 684=[FF68], 872=[IE], 873=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_50_to_0() throws Exception {
        runTest("effects: CSS 50 to 0");
    }

    /**
     * Test {591=[IE], 592=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void manipulation__Make_sure_jQuery_fn_remove_can_work_on_elements_in_documentFragment() throws Exception {
        runTest("manipulation: Make sure jQuery.fn.remove can work on elements in documentFragment");
    }

    /**
     * Test {591=[FF68], 727=[IE], 728=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void ajax__jQuery_ajax_____JSON_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type");
    }

    /**
     * Test {591=[CHROME], 685=[FF68], 873=[IE], 874=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_50_to_50() throws Exception {
        runTest("effects: CSS 50 to 50");
    }

    /**
     * Test {592=[IE], 593=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "20",
            IE = "20")
    public void manipulation__Make_sure_specific_elements_with_content_created_correctly___13232_() throws Exception {
        runTest("manipulation: Make sure specific elements with content created correctly (#13232)");
    }

    /**
     * Test {592=[FF68], 728=[IE], 729=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void ajax__jQuery_ajax_____JSON_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type disabled with options");
    }

    /**
     * Test {592=[CHROME], 686=[FF68], 874=[IE], 875=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_50_to_100() throws Exception {
        runTest("effects: CSS 50 to 100");
    }

    /**
     * Test {593=[IE], 594=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "44",
            IE = "44")
    public void manipulation__Validate_creation_of_multiple_quantities_of_certain_elements___13818_() throws Exception {
        runTest("manipulation: Validate creation of multiple quantities of certain elements (#13818)");
    }

    /**
     * Test {593=[FF68], 729=[IE], 730=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax__jQuery_ajax_____simple_get() throws Exception {
        runTest("ajax: jQuery.ajax() - simple get");
    }

    /**
     * Test {593=[CHROME], 687=[FF68], 875=[IE], 876=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 4, 5",
            FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void effects__CSS_50_to_show() throws Exception {
        runTest("effects: CSS 50 to show");
    }

    /**
     * Test {594=[IE], 595=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void manipulation__Make_sure_tr_element_will_be_appended_to_tbody_element_of_table_when_present() throws Exception {
        runTest("manipulation: Make sure tr element will be appended to tbody element of table when present");
    }

    /**
     * Test {594=[FF68], 730=[IE], 731=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax__jQuery_ajax_____simple_post() throws Exception {
        runTest("ajax: jQuery.ajax() - simple post");
    }

    /**
     * Test {594=[CHROME], 688=[FF68], 876=[IE], 877=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__CSS_50_to_hide() throws Exception {
        runTest("effects: CSS 50 to hide");
    }

    /**
     * Test {595=[IE], 596=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void manipulation__Make_sure_tr_elements_will_be_appended_to_tbody_element_of_table_when_present() throws Exception {
        runTest("manipulation: Make sure tr elements will be appended to tbody element of table when present");
    }

    /**
     * Test {595=[FF68], 731=[IE], 732=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax__jQuery_ajax_____data_option___empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - data option - empty bodies for non-GET requests");
    }

    /**
     * Test {595=[CHROME], 689=[FF68], 877=[IE], 878=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_50_to_0() throws Exception {
        runTest("effects: JS 50 to 0");
    }

    /**
     * Test {596=[IE], 597=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void manipulation__Make_sure_tfoot_element_will_not_be_appended_to_tbody_element_of_table_when_present() throws Exception {
        runTest("manipulation: Make sure tfoot element will not be appended to tbody element of table when present");
    }

    /**
     * Test {596=[FF68], 737=[IE], 738=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "3, 0, 3",
            IE = "4")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (cache)");
    }

    /**
     * Test {596=[CHROME], 690=[FF68], 878=[IE], 879=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_50_to_50() throws Exception {
        runTest("effects: JS 50 to 50");
    }

    /**
     * Test {597=[IE], 598=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void manipulation__Make_sure_document_fragment_will_be_appended_to_tbody_element_of_table_when_present() throws Exception {
        runTest("manipulation: Make sure document fragment will be appended to tbody element of table when present");
    }

    /**
     * Test {597=[FF68], 738=[IE], 739=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void ajax__jQuery_ajax_____Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (cache)");
    }

    /**
     * Test {597=[CHROME], 691=[FF68], 879=[IE], 880=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_50_to_100() throws Exception {
        runTest("effects: JS 50 to 100");
    }

    /**
     * Test {598=[IE], 599=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    @NotYetImplemented
    public void manipulation__Make_sure_col_element_is_appended_correctly() throws Exception {
        runTest("manipulation: Make sure col element is appended correctly");
    }

    /**
     * Test {598=[FF68], 739=[IE], 740=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "3, 0, 3",
            IE = "4")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (no cache)");
    }

    /**
     * Test {598=[CHROME], 692=[FF68], 880=[IE], 881=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 4, 5",
            FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void effects__JS_50_to_show() throws Exception {
        runTest("effects: JS 50 to show");
    }

    /**
     * Test {599=[IE], 600=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void manipulation__Make_sure_tr_is_not_appended_to_the_wrong_tbody__gh_3439_() throws Exception {
        runTest("manipulation: Make sure tr is not appended to the wrong tbody (gh-3439)");
    }

    /**
     * Test {599=[FF68], 740=[IE], 741=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void ajax__jQuery_ajax_____Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (no cache)");
    }

    /**
     * Test {599=[CHROME], 693=[FF68], 881=[IE], 882=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__JS_50_to_hide() throws Exception {
        runTest("effects: JS 50 to hide");
    }

    /**
     * Test {600=[IE], 601=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void manipulation__Insert_script_with_data_URI__gh_1887_() throws Exception {
        runTest("manipulation: Insert script with data-URI (gh-1887)");
    }

    /**
     * Test {600=[FF68], 741=[IE], 742=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____failing_cross_domain__non_existing_() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain (non-existing)");
    }

    /**
     * Test {600=[CHROME], 694=[FF68], 882=[IE], 883=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_0_to_0() throws Exception {
        runTest("effects: CSS 0 to 0");
    }

    /**
     * Test {601=[FF68], 742=[IE], 743=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____failing_cross_domain() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain");
    }

    /**
     * Test {601=[CHROME], 695=[FF68], 883=[IE], 884=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_0_to_50() throws Exception {
        runTest("effects: CSS 0 to 50");
    }

    /**
     * Test {602=[FF68], 743=[IE], 744=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax__jQuery_ajax_____atom_xml() throws Exception {
        runTest("ajax: jQuery.ajax() - atom+xml");
    }

    /**
     * Test {602=[CHROME], 696=[FF68], 884=[IE], 885=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__CSS_0_to_100() throws Exception {
        runTest("effects: CSS 0 to 100");
    }

    /**
     * Test {603=[FF68], 744=[IE], 745=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "2, 0, 2",
            IE = "3")
    public void ajax__jQuery_ajax_____statusText() throws Exception {
        runTest("ajax: jQuery.ajax() - statusText");
    }

    /**
     * Test {603=[CHROME], 697=[FF68], 885=[IE], 886=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 4, 5",
            FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void effects__CSS_0_to_show() throws Exception {
        runTest("effects: CSS 0 to show");
    }

    /**
     * Test {604=[FF68], 745=[IE], 746=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "20",
            FF68 = "9, 11, 20",
            IE = "20")
    public void ajax__jQuery_ajax_____statusCode() throws Exception {
        runTest("ajax: jQuery.ajax() - statusCode");
    }

    /**
     * Test {604=[CHROME], 698=[FF68], 886=[IE], 887=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__CSS_0_to_hide() throws Exception {
        runTest("effects: CSS 0 to hide");
    }

    /**
     * Test {605=[FF68], 746=[IE], 747=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "8",
            FF68 = "3, 0, 3",
            IE = "8")
    public void ajax__jQuery_ajax_____transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax() - transitive conversions");
    }

    /**
     * Test {605=[CHROME], 699=[FF68], 887=[IE], 888=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_0_to_0() throws Exception {
        runTest("effects: JS 0 to 0");
    }

    /**
     * Test {606=[FF68], 747=[IE], 748=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void ajax__jQuery_ajax_____overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax() - overrideMimeType");
    }

    /**
     * Test {606=[CHROME], 700=[FF68], 888=[IE], 889=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_0_to_50() throws Exception {
        runTest("effects: JS 0 to 50");
    }

    /**
     * Test {607=[FF68], 748=[IE], 749=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax__jQuery_ajax_____empty_json_gets_to_error_callback_instead_of_success_callback_() throws Exception {
        runTest("ajax: jQuery.ajax() - empty json gets to error callback instead of success callback.");
    }

    /**
     * Test {607=[CHROME], 701=[FF68], 889=[IE], 890=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "6",
            FF68 = "2, 0, 2",
            IE = "6")
    public void effects__JS_0_to_100() throws Exception {
        runTest("effects: JS 0 to 100");
    }

    /**
     * Test {608=[FF68], 749=[IE], 750=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void ajax___2688___jQuery_ajax_____beforeSend__cancel_request() throws Exception {
        runTest("ajax: #2688 - jQuery.ajax() - beforeSend, cancel request");
    }

    /**
     * Test {608=[CHROME], 702=[FF68], 890=[IE], 891=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 4, 5",
            FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void effects__JS_0_to_show() throws Exception {
        runTest("effects: JS 0 to show");
    }

    /**
     * Test {609=[FF68], 750=[IE], 751=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax___2806___jQuery_ajax_____data_option___evaluate_function_values() throws Exception {
        runTest("ajax: #2806 - jQuery.ajax() - data option - evaluate function values");
    }

    /**
     * Test {609=[CHROME], 703=[FF68], 891=[IE], 892=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__JS_0_to_hide() throws Exception {
        runTest("effects: JS 0 to hide");
    }

    /**
     * Test {610=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_fadeOut_fadeIn() throws Exception {
        runTest("effects: Chain fadeOut fadeIn");
    }

    /**
     * Test {610=[FF68], 751=[IE], 752=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void ajax___7531___jQuery_ajax_____Location_object_as_url() throws Exception {
        runTest("ajax: #7531 - jQuery.ajax() - Location object as url");
    }

    /**
     * Test {611=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_fadeIn_fadeOut() throws Exception {
        runTest("effects: Chain fadeIn fadeOut");
    }

    /**
     * Test {611=[FF68], 752=[IE], 753=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Same_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Same Domain");
    }

    /**
     * Test {612=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_hide_show() throws Exception {
        runTest("effects: Chain hide show");
    }

    /**
     * Test {612=[FF68], 753=[IE], 754=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Cross_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Cross Domain");
    }

    /**
     * Test {613=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_show_hide() throws Exception {
        runTest("effects: Chain show hide");
    }

    /**
     * Test {613=[FF68], 754=[IE], 755=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void ajax___8107___jQuery_ajax_____multiple_method_signatures_introduced_in_1_5() throws Exception {
        runTest("ajax: #8107 - jQuery.ajax() - multiple method signatures introduced in 1.5");
    }

    /**
     * Test {614=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_show_hide_with_easing_and_callback() throws Exception {
        runTest("effects: Chain show hide with easing and callback");
    }

    /**
     * Test {614=[FF68], 755=[IE], 756=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Same_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Same Domain");
    }

    /**
     * Test {615=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_toggle_in() throws Exception {
        runTest("effects: Chain toggle in");
    }

    /**
     * Test {615=[FF68], 756=[IE], 757=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Cross_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Cross Domain");
    }

    /**
     * Test {616=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_toggle_out() throws Exception {
        runTest("effects: Chain toggle out");
    }

    /**
     * Test {616=[FF68], 757=[IE], 758=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "2",
            IE = "2")
    public void ajax___9887___jQuery_ajax_____Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: #9887 - jQuery.ajax() - Context with circular references (#9887)");
    }

    /**
     * Test {617=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_toggle_out_with_easing_and_callback() throws Exception {
        runTest("effects: Chain toggle out with easing and callback");
    }

    /**
     * Test {617=[FF68], 758=[IE], 759=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void ajax___10093___jQuery_ajax_____falsy_url_as_argument() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url as argument");
    }

    /**
     * Test {618=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_slideDown_slideUp() throws Exception {
        runTest("effects: Chain slideDown slideUp");
    }

    /**
     * Test {618=[FF68], 759=[IE], 760=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "4",
            IE = "4")
    public void ajax___10093___jQuery_ajax_____falsy_url_in_settings_object() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url in settings object");
    }

    /**
     * Test {619=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_slideUp_slideDown() throws Exception {
        runTest("effects: Chain slideUp slideDown");
    }

    /**
     * Test {619=[IE], 620=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "17",
            IE = "17")
    @NotYetImplemented
    public void css__css___non_px_relative_values__gh_1711_() throws Exception {
        runTest("css: css() non-px relative values (gh-1711)");
    }

    /**
     * Test {619=[FF68], 760=[IE], 761=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void ajax___11151___jQuery_ajax_____parse_error_body() throws Exception {
        runTest("ajax: #11151 - jQuery.ajax() - parse error body");
    }

    /**
     * Test {620=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_slideUp_slideDown_with_easing_and_callback() throws Exception {
        runTest("effects: Chain slideUp slideDown with easing and callback");
    }

    /**
     * Test {620=[IE], 621=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    @NotYetImplemented
    public void css__css___mismatched_relative_values_with_bounded_styles__gh_2144_() throws Exception {
        runTest("css: css() mismatched relative values with bounded styles (gh-2144)");
    }

    /**
     * Test {620=[FF68], 761=[IE], 762=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "1, 0, 1",
            IE = "1")
    public void ajax___11426___jQuery_ajax_____loading_binary_data_shouldn_t_throw_an_exception_in_IE() throws Exception {
        runTest("ajax: #11426 - jQuery.ajax() - loading binary data shouldn't throw an exception in IE");
    }

    /**
     * Test {621=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_slideToggle_in() throws Exception {
        runTest("effects: Chain slideToggle in");
    }

    /**
     * Test {621=[FF68], 763=[IE], 764=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "3, 0, 3",
            IE = "1")
    @NotYetImplemented
    public void ajax___11743___jQuery_ajax_____script__throws_exception() throws Exception {
        runTest("ajax: #11743 - jQuery.ajax() - script, throws exception");
    }

    /**
     * Test {622=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_slideToggle_out() throws Exception {
        runTest("effects: Chain slideToggle out");
    }

    /**
     * Test {622=[IE], 623=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void css__css_String__Object__with_negative_values() throws Exception {
        runTest("css: css(String, Object) with negative values");
    }

    /**
     * Test {622=[FF68], 764=[IE], 765=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___method_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - method set globally");
    }

    /**
     * Test {623=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_fadeToggle_in() throws Exception {
        runTest("effects: Chain fadeToggle in");
    }

    /**
     * Test {623=[FF68], 765=[IE], 766=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___type_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - type set globally");
    }

    /**
     * Test {624=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_fadeToggle_out() throws Exception {
        runTest("effects: Chain fadeToggle out");
    }

    /**
     * Test {624=[FF68], 766=[IE], 767=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void ajax___13276___jQuery_ajax_____compatibility_between_XML_documents_from_ajax_requests_and_parsed_string() throws Exception {
        runTest("ajax: #13276 - jQuery.ajax() - compatibility between XML documents from ajax requests and parsed string");
    }

    /**
     * Test {625=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5")
    public void effects__Chain_fadeTo_0_5_1_0_with_easing_and_callback_() throws Exception {
        runTest("effects: Chain fadeTo 0.5 1.0 with easing and callback)");
    }

    /**
     * Test {625=[FF68], 767=[IE], 768=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void ajax___13292___jQuery_ajax_____converter_is_bypassed_for_204_requests() throws Exception {
        runTest("ajax: #13292 - jQuery.ajax() - converter is bypassed for 204 requests");
    }

    /**
     * Test {626=[FF68], 768=[IE], 769=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "2, 0, 2",
            IE = "3")
    public void ajax___13388___jQuery_ajax_____responseXML() throws Exception {
        runTest("ajax: #13388 - jQuery.ajax() - responseXML");
    }

    /**
     * Test {626=[CHROME], 705=[FF68], 893=[IE], 894=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4",
            FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__jQuery_show__fast___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("effects: jQuery.show('fast') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {627=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "8")
    public void effects__slideToggle___stop___slideToggle__() throws Exception {
        runTest("effects: slideToggle().stop().slideToggle()");
    }

    /**
     * Test {627=[FF68], 769=[IE], 770=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void ajax___13922___jQuery_ajax_____converter_is_bypassed_for_HEAD_requests() throws Exception {
        runTest("ajax: #13922 - jQuery.ajax() - converter is bypassed for HEAD requests");
    }

    /**
     * Test {628=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "1, 0, 1")
    public void ajax___13240___jQuery_ajax_____support_non_RFC2616_methods() throws Exception {
        runTest("ajax: #13240 - jQuery.ajax() - support non-RFC2616 methods");
    }

    /**
     * Test {628=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "8")
    public void effects__fadeToggle___stop___fadeToggle__() throws Exception {
        runTest("effects: fadeToggle().stop().fadeToggle()");
    }

    /**
     * Test {628=[IE], 629=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "18",
            IE = "18")
    public void css__show__() throws Exception {
        runTest("css: show()");
    }

    /**
     * Test {629=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF68 = "2, 0, 2")
    public void ajax__global_failure() throws Exception {
        runTest("ajax: global failure");
    }

    /**
     * Test {629=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "8")
    public void effects__toggle___stop___toggle__() throws Exception {
        runTest("effects: toggle().stop().toggle()");
    }

    /**
     * Test {629=[IE], 630=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "19",
            IE = "19")
    public void css__show_hide_detached_nodes() throws Exception {
        runTest("css: show/hide detached nodes");
    }

    /**
     * Test {630=[CHROME], 707=[FF68], 895=[IE], 896=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5",
            FF60 = "5",
            FF68 = "2, 0, 2",
            IE = "5")
    public void effects__animate_with_per_property_easing() throws Exception {
        runTest("effects: animate with per-property easing");
    }

    /**
     * Test {631=[IE], 632=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__show___after_hide___should_always_set_display_to_initial_value___14750_() throws Exception {
        runTest("css: show() after hide() should always set display to initial value (#14750)");
    }

    /**
     * Test {631=[FF68], 817=[IE], 818=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "2, 2, 4",
            IE = "1")
    public void effects__show___basic() throws Exception {
        runTest("effects: show() basic");
    }

    /**
     * Test {631=[CHROME], 708=[FF68], 896=[IE], 897=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "11",
            FF60 = "11",
            FF68 = "2, 0, 2",
            IE = "11")
    public void effects__animate_with_CSS_shorthand_properties() throws Exception {
        runTest("effects: animate with CSS shorthand properties");
    }

    /**
     * Test {632=[IE], 633=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "36",
            IE = "36")
    public void css__show_hide_3_0__default_display() throws Exception {
        runTest("css: show/hide 3.0, default display");
    }

    /**
     * Test {632=[CHROME], 709=[FF68], 897=[IE], 898=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "7, 2, 9",
            FF60 = "4",
            FF68 = "3, 2, 5",
            IE = "4")
    public void effects__hide_hidden_elements__with_animation__bug__7141_() throws Exception {
        runTest("effects: hide hidden elements, with animation (bug #7141)");
    }

    /**
     * Test {633=[IE], 634=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void css__show_hide_3_0__default_body_display() throws Exception {
        runTest("css: show/hide 3.0, default body display");
    }

    /**
     * Test {633=[CHROME], 710=[FF68], 898=[IE], 899=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9, 1, 10",
            FF60 = "2",
            FF68 = "3, 1, 4",
            IE = "2")
    public void effects__animate_unit_less_properties___4966_() throws Exception {
        runTest("effects: animate unit-less properties (#4966)");
    }

    /**
     * Test {634=[IE], 635=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "36",
            IE = "36")
    public void css__show_hide_3_0__cascade_display() throws Exception {
        runTest("css: show/hide 3.0, cascade display");
    }

    /**
     * Test {634=[CHROME], 711=[FF68], 899=[IE], 900=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "7, 2, 9",
            FF60 = "6",
            FF68 = "3, 2, 5",
            IE = "6")
    public void effects__animate_properties_missing_px_w__opacity_as_last___9074_() throws Exception {
        runTest("effects: animate properties missing px w/ opacity as last (#9074)");
    }

    /**
     * Test {635=[IE], 636=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "96",
            IE = "96")
    public void css__show_hide_3_0__inline_display() throws Exception {
        runTest("css: show/hide 3.0, inline display");
    }

    /**
     * Test {635=[CHROME], 712=[FF68], 900=[IE], 901=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "13, 0, 13",
            FF60 = "1",
            FF68 = "3, 0, 3",
            IE = "1")
    public void effects__callbacks_should_fire_in_correct_order___9100_() throws Exception {
        runTest("effects: callbacks should fire in correct order (#9100)");
    }

    /**
     * Test {636=[IE], 637=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "72",
            IE = "72")
    public void css__show_hide_3_0__cascade_hidden() throws Exception {
        runTest("css: show/hide 3.0, cascade hidden");
    }

    /**
     * Test {636=[FF68], 824=[IE], 825=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "12",
            FF68 = "2, 0, 2",
            IE = "12")
    @NotYetImplemented
    public void effects__animate_relative_values() throws Exception {
        runTest("effects: animate relative values");
    }

    /**
     * Test {636=[CHROME], 713=[FF68], 901=[IE], 902=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "15, 0, 15",
            FF60 = "2",
            FF68 = "99, 0, 99",
            IE = "2")
    public void effects__callbacks_that_throw_exceptions_will_be_removed___5684_() throws Exception {
        runTest("effects: callbacks that throw exceptions will be removed (#5684)");
    }

    /**
     * Test {637=[IE], 638=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "84",
            IE = "84")
    public void css__show_hide_3_0__inline_hidden() throws Exception {
        runTest("css: show/hide 3.0, inline hidden");
    }

    /**
     * Test {637=[CHROME], 714=[FF68], 902=[IE], 903=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "17, 1, 18",
            FF60 = "2",
            FF68 = "3, 1, 4",
            IE = "2")
    public void effects__animate_will_scale_margin_properties_individually() throws Exception {
        runTest("effects: animate will scale margin properties individually");
    }

    /**
     * Test {638=[CHROME], 715=[FF68], 903=[IE], 904=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("effects: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {639=[IE], 640=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "6",
            IE = "6")
    public void css__detached_toggle__() throws Exception {
        runTest("css: detached toggle()");
    }

    /**
     * Test {639=[CHROME], 717=[FF68], 905=[IE], 906=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "21, 0, 21",
            FF60 = "4",
            FF68 = "3, 0, 3",
            IE = "4")
    public void effects__jQuery_Animation__object__props__opts__() throws Exception {
        runTest("effects: jQuery.Animation( object, props, opts )");
    }

    /**
     * Test {640=[CHROME], 718=[FF68], 906=[IE], 907=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "13, 0, 13",
            FF60 = "1",
            FF68 = "3, 0, 3",
            IE = "1")
    public void effects__Animate_Option__step__function__percent__tween__() throws Exception {
        runTest("effects: Animate Option: step: function( percent, tween )");
    }

    /**
     * Test {641=[CHROME], 719=[FF68], 907=[IE], 908=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "25, 0, 25",
            FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void effects__Animate_callbacks_have_correct_context() throws Exception {
        runTest("effects: Animate callbacks have correct context");
    }

    /**
     * Test {642=[IE], 643=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented
    public void css__computed_margins__trac_3333__gh_2237_() throws Exception {
        runTest("css: computed margins (trac-3333; gh-2237)");
    }

    /**
     * Test {642=[CHROME], 720=[FF68], 908=[IE], 909=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void effects__User_supplied_callback_called_after_show_when_fx_off___8892_() throws Exception {
        runTest("effects: User supplied callback called after show when fx off (#8892)");
    }

    /**
     * Test {643=[CHROME], 721=[FF68], 909=[IE], 910=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "7, 1, 8",
            FF60 = "20",
            FF68 = "5, 1, 6",
            IE = "20")
    public void effects__animate_should_set_display_for_disconnected_nodes() throws Exception {
        runTest("effects: animate should set display for disconnected nodes");
    }

    /**
     * Test {644=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5, 0, 5")
    public void effects__Animation_callback_should_not_show_animated_element_as_animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as animated (#7157)");
    }

    /**
     * Test {645=[CHROME], 724=[FF68], 912=[IE], 913=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "7, 0, 7",
            FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void effects__hide_called_on_element_within_hidden_parent_should_set_display_to_none___10045_() throws Exception {
        runTest("effects: hide called on element within hidden parent should set display to none (#10045)");
    }

    /**
     * Test {646=[CHROME], 725=[FF68], 913=[IE], 914=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "8, 0, 8",
            FF60 = "5",
            FF68 = "3, 0, 3",
            IE = "5")
    public void effects__hide__fadeOut_and_slideUp_called_on_element_width_height_and_width___0_should_set_display_to_none() throws Exception {
        runTest("effects: hide, fadeOut and slideUp called on element width height and width = 0 should set display to none");
    }

    /**
     * Test {647=[IE], 648=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__Ensure_styles_are_retrieving_from_parsed_html_on_document_fragments() throws Exception {
        runTest("css: Ensure styles are retrieving from parsed html on document fragments");
    }

    /**
     * Test {647=[CHROME], 727=[FF68], 915=[IE], 916=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF60 = "10",
            FF68 = "2, 0, 2",
            IE = "10")
    public void effects__Handle_queue_false_promises() throws Exception {
        runTest("effects: Handle queue:false promises");
    }

    /**
     * Test {648=[CHROME], 728=[FF68], 916=[IE], 917=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "4, 0, 4",
            FF60 = "4",
            FF68 = "3, 0, 3",
            IE = "4")
    public void effects__multiple_unqueued_and_promise() throws Exception {
        runTest("effects: multiple unqueued and promise");
    }

    /**
     * Test {649=[CHROME], 729=[FF68], 917=[IE], 918=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5, 0, 5",
            FF60 = "1",
            FF68 = "3, 0, 3",
            IE = "1")
    @NotYetImplemented
    public void effects__animate_does_not_change_start_value_for_non_px_animation___7109_() throws Exception {
        runTest("effects: animate does not change start value for non-px animation (#7109)");
    }

    /**
     * Test {650=[IE], 651=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void css__Do_not_append_px___9548___12990___2792_() throws Exception {
        runTest("css: Do not append px (#9548, #12990, #2792)");
    }

    /**
     * Test {650=[CHROME], 730=[FF68], 918=[IE], 919=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6, 0, 6",
            FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "1")
    @NotYetImplemented(IE)
    public void effects__non_px_animation_handles_non_numeric_start___11971_() throws Exception {
        runTest("effects: non-px animation handles non-numeric start (#11971)");
    }

    /**
     * Test {651=[CHROME], 731=[FF68], 919=[IE], 920=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "15, 5, 20",
            FF60 = "15",
            FF68 = "4, 6, 10",
            IE = "15")
    public void effects__Animation_callbacks___11797_() throws Exception {
        runTest("effects: Animation callbacks (#11797)");
    }

    /**
     * Test {652=[IE], 653=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__css__width___should_work_correctly_before_document_ready___14084_() throws Exception {
        runTest("css: css('width') should work correctly before document ready (#14084)");
    }

    /**
     * Test {652=[CHROME], 732=[FF68], 921=[IE], 922=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3",
            FF60 = "8",
            FF68 = "3, 0, 3",
            IE = "8")
    public void effects__Animate_properly_sets_overflow_hidden_when_animating_width_height___12117_() throws Exception {
        runTest("effects: Animate properly sets overflow hidden when animating width/height (#12117)");
    }

    /**
     * Test {653=[IE], 654=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__css__width___should_work_correctly_with_browser_zooming() throws Exception {
        runTest("css: css('width') should work correctly with browser zooming");
    }

    /**
     * Test {653=[CHROME], 734=[FF68], 923=[IE], 924=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void effects__Animations_with_0_duration_don_t_ease___12273_() throws Exception {
        runTest("effects: Animations with 0 duration don't ease (#12273)");
    }

    /**
     * Test {654=[IE], 655=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented
    public void css__css__width___and_css__height___should_return_fractional_values_for_nodes_in_the_document() throws Exception {
        runTest("css: css('width') and css('height') should return fractional values for nodes in the document");
    }

    /**
     * Test {654=[CHROME], 746=[FF68], 946=[IE], 947=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void offset__empty_set() throws Exception {
        runTest("offset: empty set");
    }

    /**
     * Test {655=[IE], 656=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented
    public void css__css__width___and_css__height___should_return_fractional_values_for_disconnected_nodes() throws Exception {
        runTest("css: css('width') and css('height') should return fractional values for disconnected nodes");
    }

    /**
     * Test {655=[CHROME], 747=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void offset__object_without_getBoundingClientRect() throws Exception {
        runTest("offset: object without getBoundingClientRect");
    }

    /**
     * Test {656=[CHROME], 748=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2",
            FF68 = "2")
    public void offset__disconnected_node() throws Exception {
        runTest("offset: disconnected node");
    }

    /**
     * Test {658=[IE], 659=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void css__css_opacity_consistency_across_browsers___12685_() throws Exception {
        runTest("css: css opacity consistency across browsers (#12685)");
    }

    /**
     * Test {658=[CHROME], 750=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void offset__offset_absolute() throws Exception {
        runTest("offset: offset/absolute");
    }

    /**
     * Test {659=[IE], 660=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "17",
            IE = "17")
    @NotYetImplemented
    public void css___visible__hidden_selectors() throws Exception {
        runTest("css: :visible/:hidden selectors");
    }

    /**
     * Test {659=[CHROME], 751=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void offset__offset_relative() throws Exception {
        runTest("offset: offset/relative");
    }

    /**
     * Test {660=[IE], 661=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__Keep_the_last_style_if_the_new_one_isn_t_recognized_by_the_browser___14836_() throws Exception {
        runTest("css: Keep the last style if the new one isn't recognized by the browser (#14836)");
    }

    /**
     * Test {660=[CHROME], 752=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void offset__offset_static() throws Exception {
        runTest("offset: offset/static");
    }

    /**
     * Test {661=[IE], 662=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__Keep_the_last_style_if_the_new_one_is_a_non_empty_whitespace__gh_3204_() throws Exception {
        runTest("css: Keep the last style if the new one is a non-empty whitespace (gh-3204)");
    }

    /**
     * Test {661=[CHROME], 753=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void offset__offset_fixed() throws Exception {
        runTest("offset: offset/fixed");
    }

    /**
     * Test {662=[IE], 663=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__Reset_the_style_if_set_to_an_empty_string() throws Exception {
        runTest("css: Reset the style if set to an empty string");
    }

    /**
     * Test {662=[CHROME], 754=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void offset__offset_table() throws Exception {
        runTest("offset: offset/table");
    }

    /**
     * Test {663=[IE], 664=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "24",
            IE = "24")
    public void css__Clearing_a_Cloned_Element_s_Style_Shouldn_t_Clear_the_Original_Element_s_Style___8908_() throws Exception {
        runTest("css: Clearing a Cloned Element's Style Shouldn't Clear the Original Element's Style (#8908)");
    }

    /**
     * Test {663=[CHROME], 755=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void offset__offset_scroll() throws Exception {
        runTest("offset: offset/scroll");
    }

    /**
     * Test {663=[FF68], 851=[IE], 852=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__Overflow_and_Display() throws Exception {
        runTest("effects: Overflow and Display");
    }

    /**
     * Test {664=[IE], 665=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__Don_t_append_px_to_CSS__order__value___14049_() throws Exception {
        runTest("css: Don't append px to CSS \"order\" value (#14049)");
    }

    /**
     * Test {664=[CHROME], 756=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void offset__offset_body() throws Exception {
        runTest("offset: offset/body");
    }

    /**
     * Test {665=[IE], 666=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__Do_not_throw_on_frame_elements_from_css_method___15098_() throws Exception {
        runTest("css: Do not throw on frame elements from css method (#15098)");
    }

    /**
     * Test {665=[CHROME], 757=[FF68], 960=[IE], 961=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3",
            FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void offset__chaining() throws Exception {
        runTest("offset: chaining");
    }

    /**
     * Test {666=[IE], 667=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "2")
    @NotYetImplemented(CHROME)
    public void css__Don_t_default_to_a_cached_previously_used_wrong_prefixed_name__gh_2015_() throws Exception {
        runTest("css: Don't default to a cached previously used wrong prefixed name (gh-2015)");
    }

    /**
     * Test {666=[CHROME], 758=[FF68], 977=[IE], 978=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "5, 7, 12",
            FF60 = "13",
            FF68 = "1, 12, 13",
            IE = "13")
    public void offset__offsetParent() throws Exception {
        runTest("offset: offsetParent");
    }

    /**
     * Test {667=[IE], 668=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void css__Don_t_detect_fake_set_properties_on_a_node_when_caching_the_prefixed_version() throws Exception {
        runTest("css: Don't detect fake set properties on a node when caching the prefixed version");
    }

    /**
     * Test {667=[CHROME], 759=[FF68], 978=[IE], 979=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "2",
            CHROME = "1, 1, 2")
    public void offset__fractions__see__7730_and__7885_() throws Exception {
        runTest("offset: fractions (see #7730 and #7885)");
    }

    /**
     * Test {668=[IE], 669=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "10",
            IE = "0")
    @NotYetImplemented
    public void css__css___customProperty_() throws Exception {
        runTest("css: css(--customProperty)");
    }

    /**
     * Test {668=[CHROME], 760=[FF68], 980=[IE], 981=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9",
            FF60 = "9",
            FF68 = "2, 9, 11",
            IE = "9")
    public void dimensions__width__() throws Exception {
        runTest("dimensions: width()");
    }

    /**
     * Test {669=[CHROME], 761=[FF68], 981=[IE], 982=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9",
            FF60 = "9",
            FF68 = "2, 9, 11",
            IE = "9")
    public void dimensions__width_Function_() throws Exception {
        runTest("dimensions: width(Function)");
    }

    /**
     * Test {670=[IE], 671=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void serialize__jQuery_param___not_affected_by_ajaxSettings() throws Exception {
        runTest("serialize: jQuery.param() not affected by ajaxSettings");
    }

    /**
     * Test {670=[CHROME], 762=[FF68], 982=[IE], 983=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void dimensions__width_Function_args__() throws Exception {
        runTest("dimensions: width(Function(args))");
    }

    /**
     * Test {671=[CHROME], 763=[FF68], 983=[IE], 984=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9",
            FF60 = "9",
            FF68 = "2, 9, 11",
            IE = "9")
    public void dimensions__height__() throws Exception {
        runTest("dimensions: height()");
    }

    /**
     * Test {672=[CHROME], 764=[FF68], 984=[IE], 985=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "9",
            FF60 = "9",
            FF68 = "2, 9, 11",
            IE = "9")
    public void dimensions__height_Function_() throws Exception {
        runTest("dimensions: height(Function)");
    }

    /**
     * Test {673=[IE], 674=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__Unit_Testing_Environment() throws Exception {
        runTest("ajax: Unit Testing Environment");
    }

    /**
     * Test {673=[CHROME], 765=[FF68], 985=[IE], 986=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void dimensions__height_Function_args__() throws Exception {
        runTest("dimensions: height(Function(args))");
    }

    /**
     * Test {674=[IE], 675=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    @NotYetImplemented
    public void ajax__XMLHttpRequest___Attempt_to_block_tests_because_of_dangling_XHR_requests__IE_() throws Exception {
        runTest("ajax: XMLHttpRequest - Attempt to block tests because of dangling XHR requests (IE)");
    }

    /**
     * Test {674=[CHROME], 766=[FF68], 986=[IE], 987=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6",
            FF60 = "7",
            FF68 = "2, 6, 8",
            IE = "7")
    public void dimensions__innerWidth__() throws Exception {
        runTest("dimensions: innerWidth()");
    }

    /**
     * Test {675=[CHROME], 767=[FF68], 987=[IE], 988=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 5, 6",
            FF60 = "7",
            FF68 = "3, 5, 8",
            IE = "7")
    public void dimensions__innerHeight__() throws Exception {
        runTest("dimensions: innerHeight()");
    }

    /**
     * Test {676=[CHROME], 768=[FF68], 988=[IE], 989=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 9, 11",
            FF60 = "12",
            FF68 = "4, 9, 13",
            IE = "12")
    public void dimensions__outerWidth__() throws Exception {
        runTest("dimensions: outerWidth()");
    }

    /**
     * Test {677=[IE], 678=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void ajax__jQuery_ajax_____execute_js_for_crossOrigin_when_dataType_option_is_provided() throws Exception {
        runTest("ajax: jQuery.ajax() - execute js for crossOrigin when dataType option is provided");
    }

    /**
     * Test {677=[CHROME], 769=[FF68], 990=[IE], 991=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void dimensions__child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__9441__9300() throws Exception {
        runTest("dimensions: child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #9441 #9300");
    }

    /**
     * Test {678=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1")
    public void dimensions__getting_dimensions_shouldnt_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldnt modify runtimeStyle see #9233");
    }

    /**
     * Test {678=[IE], 679=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_ajax_____do_not_execute_js__crossOrigin_() throws Exception {
        runTest("ajax: jQuery.ajax() - do not execute js (crossOrigin)");
    }

    /**
     * Test {679=[CHROME], 771=[FF68], 992=[IE], 993=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void dimensions__table_dimensions() throws Exception {
        runTest("dimensions: table dimensions");
    }

    /**
     * Test {680=[CHROME], 772=[FF68], 993=[IE], 994=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("16")
    public void dimensions__box_sizing_border_box_child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__10413() throws Exception {
        runTest("dimensions: box-sizing:border-box child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #10413");
    }

    /**
     * Test {681=[CHROME], 773=[FF68], 989=[IE], 990=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 9, 11",
            FF60 = "12",
            FF68 = "4, 9, 13",
            IE = "12")
    public void dimensions__outerHeight__() throws Exception {
        runTest("dimensions: outerHeight()");
    }

    /**
     * Test {682=[CHROME], 774=[FF68], 994=[IE], 995=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void dimensions__passing_undefined_is_a_setter__5571() throws Exception {
        runTest("dimensions: passing undefined is a setter #5571");
    }

    /**
     * Test {683=[CHROME], 775=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "8, 0, 8",
            FF68 = "8, 0, 8")
    public void dimensions__getters_on_non_elements_should_return_null() throws Exception {
        runTest("dimensions: getters on non elements should return null");
    }

    /**
     * Test {684=[CHROME], 776=[FF68], 995=[IE], 996=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "20",
            FF60 = "120",
            FF68 = "20",
            IE = "120")
    public void dimensions__setters_with_and_without_box_sizing_border_box() throws Exception {
        runTest("dimensions: setters with and without box-sizing:border-box");
    }

    /**
     * Test {685=[CHROME], 777=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void dimensions__dimensions_documentSmall() throws Exception {
        runTest("dimensions: dimensions/documentSmall");
    }

    /**
     * Test {686=[CHROME], 778=[FF68]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 0, 2",
            FF68 = "2, 0, 2")
    public void dimensions__dimensions_documentLarge() throws Exception {
        runTest("dimensions: dimensions/documentLarge");
    }

    /**
     * Test {687=[CHROME], 779=[FF68], 1004=[IE], 1005=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "3, 0, 3",
            FF60 = "20",
            FF68 = "3, 0, 3",
            IE = "20")
    public void animation__Animation__subject__props__opts_____shape() throws Exception {
        runTest("animation: Animation( subject, props, opts ) - shape");
    }

    /**
     * Test {688=[CHROME], 780=[FF68], 1005=[IE], 1006=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void animation__Animation_prefilter__fn_____calls_prefilter_after_defaultPrefilter() throws Exception {
        runTest("animation: Animation.prefilter( fn ) - calls prefilter after defaultPrefilter");
    }

    /**
     * Test {689=[CHROME], 781=[FF68], 1006=[IE], 1007=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1")
    public void animation__Animation_prefilter__fn__true_____calls_prefilter_before_defaultPrefilter() throws Exception {
        runTest("animation: Animation.prefilter( fn, true ) - calls prefilter before defaultPrefilter");
    }

    /**
     * Test {690=[IE], 691=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void ajax__jQuery_ajax_____traditional_param_encoding() throws Exception {
        runTest("ajax: jQuery.ajax() - traditional param encoding");
    }

    /**
     * Test {690=[CHROME], 782=[FF68], 1007=[IE], 1008=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("34")
    public void animation__Animation_prefilter___prefilter_return_hooks() throws Exception {
        runTest("animation: Animation.prefilter - prefilter return hooks");
    }

    /**
     * Test {691=[CHROME], 783=[FF68], 1008=[IE], 1009=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void animation__Animation_tweener__fn_____unshifts_a___tweener() throws Exception {
        runTest("animation: Animation.tweener( fn ) - unshifts a * tweener");
    }

    /**
     * Test {692=[CHROME], 784=[FF68], 1009=[IE], 1010=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void animation__Animation_tweener___prop___fn_____unshifts_a__prop__tweener() throws Exception {
        runTest("animation: Animation.tweener( 'prop', fn ) - unshifts a 'prop' tweener");
    }

    /**
     * Test {693=[IE], 694=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____native_abort() throws Exception {
        runTest("ajax: jQuery.ajax() - native abort");
    }

    /**
     * Test {693=[CHROME], 785=[FF68], 1010=[IE], 1011=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2")
    public void animation__Animation_tweener___list_of_props___fn_____unshifts_a_tweener_to_each_prop() throws Exception {
        runTest("animation: Animation.tweener( 'list of props', fn ) - unshifts a tweener to each prop");
    }

    /**
     * Test {694=[IE], 695=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____native_timeout() throws Exception {
        runTest("ajax: jQuery.ajax() - native timeout");
    }

    /**
     * Test {694=[CHROME], 786=[FF68], 1011=[IE], 1012=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("8")
    public void tween__jQuery_Tween___Default_propHooks_on_plain_objects() throws Exception {
        runTest("tween: jQuery.Tween - Default propHooks on plain objects");
    }

    /**
     * Test {695=[CHROME], 787=[FF68], 1012=[IE], 1013=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("19")
    public void tween__jQuery_Tween___Default_propHooks_on_elements() throws Exception {
        runTest("tween: jQuery.Tween - Default propHooks on elements");
    }

    /**
     * Test {696=[CHROME], 788=[FF68], 1013=[IE], 1014=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("13")
    public void tween__jQuery_Tween___Plain_Object() throws Exception {
        runTest("tween: jQuery.Tween - Plain Object");
    }

    /**
     * Test {697=[CHROME], 789=[FF68], 1014=[IE], 1015=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 8, 10",
            FF60 = "15",
            FF68 = "2, 8, 10",
            IE = "15")
    public void tween__jQuery_Tween___Element() throws Exception {
        runTest("tween: jQuery.Tween - Element");
    }

    /**
     * Test {698=[IE], 699=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void ajax___15160___jQuery_ajax_____request_manually_aborted_in_ajaxSend() throws Exception {
        runTest("ajax: #15160 - jQuery.ajax() - request manually aborted in ajaxSend");
    }

    /**
     * Test {698=[CHROME], 790=[FF68], 1015=[IE], 1016=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void tween__jQuery_Tween___No_duration() throws Exception {
        runTest("tween: jQuery.Tween - No duration");
    }

    /**
     * Test {699=[CHROME], 791=[FF68], 1016=[IE], 1017=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("4")
    public void tween__jQuery_Tween___step_function_option() throws Exception {
        runTest("tween: jQuery.Tween - step function option");
    }

    /**
     * Test {700=[CHROME], 792=[FF68], 1017=[IE], 1018=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("3")
    public void tween__jQuery_Tween___custom_propHooks() throws Exception {
        runTest("tween: jQuery.Tween - custom propHooks");
    }

    /**
     * Test {701=[CHROME], 793=[FF68], 1018=[IE], 1019=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("5")
    public void tween__jQuery_Tween___custom_propHooks___advanced_values() throws Exception {
        runTest("tween: jQuery.Tween - custom propHooks - advanced values");
    }

    /**
     * Test {704=[FF68], 892=[IE], 893=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "16",
            FF68 = "2, 0, 2",
            IE = "16")
    public void effects__Effects_chaining() throws Exception {
        runTest("effects: Effects chaining");
    }

    /**
     * Test {706=[FF68], 894=[IE], 895=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "24",
            FF68 = "2, 0, 2",
            IE = "24")
    public void effects__interrupt_toggle() throws Exception {
        runTest("effects: interrupt toggle");
    }

    /**
     * Test {716=[FF68], 904=[IE], 905=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "12",
            FF68 = "8, 4, 12",
            IE = "12")
    @NotYetImplemented
    public void effects__line_height_animates_correctly___13855_() throws Exception {
        runTest("effects: line-height animates correctly (#13855)");
    }

    /**
     * Test {722=[FF68], 910=[IE], 911=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "3, 0, 3",
            IE = "1")
    public void effects__Animation_callback_should_not_show_animated_element_as__animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as :animated (#7157)");
    }

    /**
     * Test {723=[FF68], 911=[IE], 912=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            FF68 = "3, 0, 3",
            IE = "1")
    public void effects__Initial_step_callback_should_show_element_as__animated___14623_() throws Exception {
        runTest("effects: Initial step callback should show element as :animated (#14623)");
    }

    /**
     * Test {726=[FF68], 914=[IE], 915=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void effects__hide_should_not_leave_hidden_inline_elements_visible___14848_() throws Exception {
        runTest("effects: hide should not leave hidden inline elements visible (#14848)");
    }

    /**
     * Test {732=[IE], 733=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_ajax_____data___x_www_form_urlencoded__gh_2658_() throws Exception {
        runTest("ajax: jQuery.ajax() - data - x-www-form-urlencoded (gh-2658)");
    }

    /**
     * Test {733=[IE], 734=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_ajax_____data___text_plain__gh_2658_() throws Exception {
        runTest("ajax: jQuery.ajax() - data - text/plain (gh-2658)");
    }

    /**
     * Test {733=[FF68], 922=[IE], 923=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void effects__Each_tick_of_the_timer_loop_uses_a_fresh_time___12837_() throws Exception {
        runTest("effects: Each tick of the timer loop uses a fresh time (#12837)");
    }

    /**
     * Test {734=[IE], 735=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_ajax_____data___no_processing_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - data - no processing POST");
    }

    /**
     * Test {735=[IE], 736=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_ajax_____data___no_processing_GET() throws Exception {
        runTest("ajax: jQuery.ajax() - data - no processing GET");
    }

    /**
     * Test {735=[FF68], 924=[IE], 925=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__toggle_state_tests__toggle___8685_() throws Exception {
        runTest("effects: toggle state tests: toggle (#8685)");
    }

    /**
     * Test {736=[IE], 737=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_ajax_____data___process_string_with_GET() throws Exception {
        runTest("ajax: jQuery.ajax() - data - process string with GET");
    }

    /**
     * Test {736=[FF68], 925=[IE], 926=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__toggle_state_tests__slideToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: slideToggle (#8685)");
    }

    /**
     * Test {737=[FF68], 926=[IE], 927=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            FF68 = "2, 0, 2",
            IE = "4")
    public void effects__toggle_state_tests__fadeToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: fadeToggle (#8685)");
    }

    /**
     * Test {738=[FF68], 927=[IE], 928=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "3",
            IE = "3")
    public void effects__jQuery_fx_start___jQuery_fx_stop_hook_points() throws Exception {
        runTest("effects: jQuery.fx.start & jQuery.fx.stop hook points");
    }

    /**
     * Test {739=[FF68], 928=[IE], 929=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "11",
            FF68 = "3, 0, 3",
            IE = "11")
    public void effects___finish___completes_all_queued_animations() throws Exception {
        runTest("effects: .finish() completes all queued animations");
    }

    /**
     * Test {740=[FF68], 929=[IE], 930=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "10",
            FF68 = "3, 0, 3",
            IE = "10")
    public void effects___finish__false_____unqueued_animations() throws Exception {
        runTest("effects: .finish( false ) - unqueued animations");
    }

    /**
     * Test {741=[FF68], 930=[IE], 931=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "11",
            FF68 = "3, 1, 4",
            IE = "11")
    public void effects___finish___custom______custom_queue_animations() throws Exception {
        runTest("effects: .finish( \"custom\" ) - custom queue animations");
    }

    /**
     * Test {742=[FF68], 931=[IE], 932=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "6",
            IE = "6")
    public void effects___finish___calls_finish_of_custom_queue_functions() throws Exception {
        runTest("effects: .finish() calls finish of custom queue functions");
    }

    /**
     * Test {743=[FF68], 932=[IE], 933=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            FF68 = "3, 0, 3",
            IE = "3")
    public void effects___finish___is_applied_correctly_when_multiple_elements_were_animated___13937_() throws Exception {
        runTest("effects: .finish() is applied correctly when multiple elements were animated (#13937)");
    }

    /**
     * Test {744=[FF68], 933=[IE], 934=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void effects__slideDown___after_stop_____13483_() throws Exception {
        runTest("effects: slideDown() after stop() (#13483)");
    }

    /**
     * Test {745=[FF68], 934=[IE], 935=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            FF68 = "2, 0, 2",
            IE = "2")
    public void effects__Respect_display_value_on_inline_elements___14824_() throws Exception {
        runTest("effects: Respect display value on inline elements (#14824)");
    }

    /**
     * Test {762=[IE], 763=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented
    public void ajax__gh_2498___jQuery_ajax_____binary_data_shouldn_t_throw_an_exception() throws Exception {
        runTest("ajax: gh-2498 - jQuery.ajax() - binary data shouldn't throw an exception");
    }

    /**
     * Test {770=[IE], 771=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax___14379___jQuery_ajax___on_unload() throws Exception {
        runTest("ajax: #14379 - jQuery.ajax() on unload");
    }

    /**
     * Test {770=[FF68], 991=[IE], 992=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "1",
            IE = "1")
    public void dimensions__getting_dimensions_shouldn_t_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldn't modify runtimeStyle see #9233");
    }

    /**
     * Test {771=[IE], 772=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void ajax___14683___jQuery_ajax_____Exceptions_thrown_synchronously_by_xhr_send_should_be_caught() throws Exception {
        runTest("ajax: #14683 - jQuery.ajax() - Exceptions thrown synchronously by xhr.send should be caught");
    }

    /**
     * Test {773=[IE], 774=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__gh_2587___when_content_type_not_xml__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not xml, but looks like one");
    }

    /**
     * Test {774=[IE], 775=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__gh_2587___when_content_type_not_json__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not json, but looks like one");
    }

    /**
     * Test {775=[IE], 776=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__gh_2587___when_content_type_not_html__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not html, but looks like one");
    }

    /**
     * Test {776=[IE], 777=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__gh_2587___when_content_type_not_javascript__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not javascript, but looks like one");
    }

    /**
     * Test {777=[IE], 778=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__gh_2587___when_content_type_not_ecmascript__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not ecmascript, but looks like one");
    }

    /**
     * Test {778=[IE], 779=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_ajaxPrefilter_____abort() throws Exception {
        runTest("ajax: jQuery.ajaxPrefilter() - abort");
    }

    /**
     * Test {779=[IE], 780=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_ajaxSetup__() throws Exception {
        runTest("ajax: jQuery.ajaxSetup()");
    }

    /**
     * Test {780=[IE], 781=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented
    public void ajax__jQuery_ajaxSetup___timeout__Number______with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) - with global timeout");
    }

    /**
     * Test {781=[IE], 782=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_ajaxSetup___timeout__Number____with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) with localtimeout");
    }

    /**
     * Test {782=[IE], 783=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    @NotYetImplemented
    public void ajax___11264___jQuery_domManip_____no_side_effect_because_of_ajaxSetup_or_global_events() throws Exception {
        runTest("ajax: #11264 - jQuery.domManip() - no side effect because of ajaxSetup or global events");
    }

    /**
     * Test {783=[IE], 784=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_load_____always_use_GET_method_even_if_it_overrided_through_ajaxSetup___11264_() throws Exception {
        runTest("ajax: jQuery#load() - always use GET method even if it overrided through ajaxSetup (#11264)");
    }

    /**
     * Test {784=[IE], 785=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_load_____should_resolve_with_correct_context() throws Exception {
        runTest("ajax: jQuery#load() - should resolve with correct context");
    }

    /**
     * Test {785=[IE], 786=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax___11402___jQuery_domManip_____script_in_comments_are_properly_evaluated() throws Exception {
        runTest("ajax: #11402 - jQuery.domManip() - script in comments are properly evaluated");
    }

    /**
     * Test {786=[IE], 787=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_get__String__Hash__Function_____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get( String, Hash, Function ) - parse xml and use text() on nodes");
    }

    /**
     * Test {787=[IE], 788=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax___8277___jQuery_get__String__Function_____data_in_ajaxSettings() throws Exception {
        runTest("ajax: #8277 - jQuery.get( String, Function ) - data in ajaxSettings");
    }

    /**
     * Test {788=[IE], 789=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            IE = "5")
    public void ajax__jQuery_getJSON__String__Hash__Function_____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Hash, Function ) - JSON array");
    }

    /**
     * Test {789=[IE], 790=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object");
    }

    /**
     * Test {790=[IE], 791=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object with absolute url to local content");
    }

    /**
     * Test {791=[IE], 792=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_getScript__String__Function_____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - with callback");
    }

    /**
     * Test {792=[IE], 793=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_getScript__String__Function_____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - no callback");
    }

    /**
     * Test {793=[IE], 794=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax___8082___jQuery_getScript__String__Function_____source_as_responseText() throws Exception {
        runTest("ajax: #8082 - jQuery.getScript( String, Function ) - source as responseText");
    }

    /**
     * Test {794=[IE], 795=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_getScript__Object_____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript( Object ) - with callback");
    }

    /**
     * Test {795=[IE], 796=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_getScript__Object_____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript( Object ) - no callback");
    }

    /**
     * Test {796=[IE], 797=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_fn_load__String__() throws Exception {
        runTest("ajax: jQuery.fn.load( String )");
    }

    /**
     * Test {797=[IE], 798=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "6",
            IE = "6")
    public void ajax__jQuery_fn_load_____404_error_callbacks() throws Exception {
        runTest("ajax: jQuery.fn.load() - 404 error callbacks");
    }

    /**
     * Test {798=[IE], 799=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_fn_load__String__null__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, null )");
    }

    /**
     * Test {799=[IE], 800=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_fn_load__String__undefined__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, undefined )");
    }

    /**
     * Test {800=[IE], 801=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_fn_load__URL_SELECTOR__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR )");
    }

    /**
     * Test {801=[IE], 802=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_fn_load__URL_SELECTOR_with_spaces__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR with spaces )");
    }

    /**
     * Test {802=[IE], 803=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_fn_load__URL_SELECTOR_with_non_HTML_whitespace__3003___() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR with non-HTML whitespace(#3003) )");
    }

    /**
     * Test {803=[IE], 804=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_fn_load__String__Function_____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - simple: inject text into DOM");
    }

    /**
     * Test {804=[IE], 805=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "7",
            IE = "7")
    @NotYetImplemented
    public void ajax__jQuery_fn_load__String__Function_____check_scripts() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check scripts");
    }

    /**
     * Test {805=[IE], 806=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void ajax__jQuery_fn_load__String__Function_____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check file with only a script tag");
    }

    /**
     * Test {806=[IE], 807=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_fn_load__String__Function_____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - dataFilter in ajaxSettings");
    }

    /**
     * Test {807=[IE], 808=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_fn_load__String__Object__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Object, Function )");
    }

    /**
     * Test {808=[IE], 809=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_fn_load__String__String__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, String, Function )");
    }

    /**
     * Test {809=[IE], 810=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "8",
            IE = "8")
    public void ajax__jQuery_fn_load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: jQuery.fn.load() - callbacks get the correct parameters");
    }

    /**
     * Test {810=[IE], 811=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax___2046___jQuery_fn_load__String__Function___with_ajaxSetup_on_dataType_json() throws Exception {
        runTest("ajax: #2046 - jQuery.fn.load( String, Function ) with ajaxSetup on dataType json");
    }

    /**
     * Test {811=[IE], 812=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax___10524___jQuery_fn_load_____data_specified_in_ajaxSettings_is_merged_in() throws Exception {
        runTest("ajax: #10524 - jQuery.fn.load() - data specified in ajaxSettings is merged in");
    }

    /**
     * Test {812=[IE], 813=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void ajax__jQuery_post_____data() throws Exception {
        runTest("ajax: jQuery.post() - data");
    }

    /**
     * Test {813=[IE], 814=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void ajax__jQuery_post__String__Hash__Function_____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post( String, Hash, Function ) - simple with xml");
    }

    /**
     * Test {814=[IE], 815=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void ajax__jQuery_get_post___options_____simple_with_xml() throws Exception {
        runTest("ajax: jQuery[get|post]( options ) - simple with xml");
    }

    /**
     * Test {815=[IE], 816=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void ajax__jQuery_active() throws Exception {
        runTest("ajax: jQuery.active");
    }

    /**
     * Test {819=[IE], 820=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "30",
            IE = "30")
    @NotYetImplemented(IE)
    public void effects__show_Number____inline_hidden() throws Exception {
        runTest("effects: show(Number) - inline hidden");
    }

    /**
     * Test {820=[IE], 821=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "30",
            IE = "30")
    @NotYetImplemented(IE)
    public void effects__show_Number____cascade_hidden() throws Exception {
        runTest("effects: show(Number) - cascade hidden");
    }

    /**
     * Test {821=[IE], 822=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void effects__Persist_correct_display_value___inline_hidden() throws Exception {
        runTest("effects: Persist correct display value - inline hidden");
    }

    /**
     * Test {822=[IE], 823=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void effects__Persist_correct_display_value___cascade_hidden() throws Exception {
        runTest("effects: Persist correct display value - cascade hidden");
    }

    /**
     * Test {920=[IE], 921=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "9",
            IE = "9")
    public void effects__Animation_callbacks_in_order___2292_() throws Exception {
        runTest("effects: Animation callbacks in order (#2292)");
    }

    /**
     * Test {935=[IE], 936=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void effects__jQuery_easing__default__gh_2218_() throws Exception {
        runTest("effects: jQuery.easing._default (gh-2218)");
    }

    /**
     * Test {936=[IE], 937=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void effects__jQuery_easing__default_in_Animation__gh_2218() throws Exception {
        runTest("effects: jQuery.easing._default in Animation (gh-2218");
    }

    /**
     * Test {937=[IE], 938=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void effects__jQuery_easing__default_in_Tween__gh_2218_() throws Exception {
        runTest("effects: jQuery.easing._default in Tween (gh-2218)");
    }

    /**
     * Test {938=[IE], 939=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "3",
            IE = "3")
    public void effects__Display_value_is_correct_for_disconnected_nodes__trac_13310_() throws Exception {
        runTest("effects: Display value is correct for disconnected nodes (trac-13310)");
    }

    /**
     * Test {939=[IE], 940=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "40",
            IE = "40")
    @NotYetImplemented(IE)
    public void effects__Show_hide_toggle_and_display__inline() throws Exception {
        runTest("effects: Show/hide/toggle and display: inline");
    }

    /**
     * Test {940=[IE], 941=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void effects__jQuery_speed__speed__easing__complete__() throws Exception {
        runTest("effects: jQuery.speed( speed, easing, complete )");
    }

    /**
     * Test {941=[IE], 942=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void effects__jQuery_speed__speed__easing__complete_____with_easing_function() throws Exception {
        runTest("effects: jQuery.speed( speed, easing, complete ) - with easing function");
    }

    /**
     * Test {942=[IE], 943=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void effects__jQuery_speed__options__() throws Exception {
        runTest("effects: jQuery.speed( options )");
    }

    /**
     * Test {943=[IE], 944=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void effects__jQuery_speed__options_____with_easing_function() throws Exception {
        runTest("effects: jQuery.speed( options ) - with easing function");
    }

    /**
     * Test {944=[IE], 945=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            IE = "5")
    public void effects__jQuery_speed__options_____queue_values() throws Exception {
        runTest("effects: jQuery.speed( options ) - queue values");
    }

    /**
     * Test {945=[IE], 946=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "5",
            IE = "5")
    public void effects__jQuery_speed_____durations() throws Exception {
        runTest("effects: jQuery.speed() - durations");
    }

    /**
     * Test {947=[IE], 948=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void offset__disconnected_element() throws Exception {
        runTest("offset: disconnected element");
    }

    /**
     * Test {948=[IE], 949=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void offset__hidden__display__none__element() throws Exception {
        runTest("offset: hidden (display: none) element");
    }

    /**
     * Test {949=[IE], 950=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void offset__0_sized_element() throws Exception {
        runTest("offset: 0 sized element");
    }

    /**
     * Test {950=[IE], 951=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void offset__hidden__visibility__hidden__element() throws Exception {
        runTest("offset: hidden (visibility: hidden) element");
    }

    /**
     * Test {951=[IE], 952=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    public void offset__normal_element() throws Exception {
        runTest("offset: normal element");
    }

    /**
     * Test {953=[IE], 954=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "178",
            IE = "178")
    @NotYetImplemented
    public void offset__absolute() throws Exception {
        runTest("offset: absolute");
    }

    /**
     * Test {954=[IE], 955=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "64",
            IE = "64")
    @NotYetImplemented
    public void offset__relative() throws Exception {
        runTest("offset: relative");
    }

    /**
     * Test {955=[IE], 956=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "80",
            IE = "80")
    @NotYetImplemented
    public void offset__static() throws Exception {
        runTest("offset: static");
    }

    /**
     * Test {956=[IE], 957=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "38",
            IE = "38")
    public void offset__fixed() throws Exception {
        runTest("offset: fixed");
    }

    /**
     * Test {957=[IE], 958=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    @NotYetImplemented
    public void offset__table() throws Exception {
        runTest("offset: table");
    }

    /**
     * Test {958=[IE], 959=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "26",
            IE = "26")
    @NotYetImplemented
    public void offset__scroll() throws Exception {
        runTest("offset: scroll");
    }

    /**
     * Test {959=[IE], 960=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    @NotYetImplemented
    public void offset__body() throws Exception {
        runTest("offset: body");
    }

    /**
     * Test {961=[IE], 962=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_static_body_static() throws Exception {
        runTest("offset: nonzero box properties - html.static body.static");
    }

    /**
     * Test {962=[IE], 963=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_static_body_relative() throws Exception {
        runTest("offset: nonzero box properties - html.static body.relative");
    }

    /**
     * Test {963=[IE], 964=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_static_body_absolute() throws Exception {
        runTest("offset: nonzero box properties - html.static body.absolute");
    }

    /**
     * Test {964=[IE], 965=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_static_body_fixed() throws Exception {
        runTest("offset: nonzero box properties - html.static body.fixed");
    }

    /**
     * Test {965=[IE], 966=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_relative_body_static() throws Exception {
        runTest("offset: nonzero box properties - html.relative body.static");
    }

    /**
     * Test {966=[IE], 967=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_relative_body_relative() throws Exception {
        runTest("offset: nonzero box properties - html.relative body.relative");
    }

    /**
     * Test {967=[IE], 968=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_relative_body_absolute() throws Exception {
        runTest("offset: nonzero box properties - html.relative body.absolute");
    }

    /**
     * Test {968=[IE], 969=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_relative_body_fixed() throws Exception {
        runTest("offset: nonzero box properties - html.relative body.fixed");
    }

    /**
     * Test {969=[IE], 970=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_absolute_body_static() throws Exception {
        runTest("offset: nonzero box properties - html.absolute body.static");
    }

    /**
     * Test {970=[IE], 971=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_absolute_body_relative() throws Exception {
        runTest("offset: nonzero box properties - html.absolute body.relative");
    }

    /**
     * Test {971=[IE], 972=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_absolute_body_absolute() throws Exception {
        runTest("offset: nonzero box properties - html.absolute body.absolute");
    }

    /**
     * Test {972=[IE], 973=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_absolute_body_fixed() throws Exception {
        runTest("offset: nonzero box properties - html.absolute body.fixed");
    }

    /**
     * Test {973=[IE], 974=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_fixed_body_static() throws Exception {
        runTest("offset: nonzero box properties - html.fixed body.static");
    }

    /**
     * Test {974=[IE], 975=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_fixed_body_relative() throws Exception {
        runTest("offset: nonzero box properties - html.fixed body.relative");
    }

    /**
     * Test {975=[IE], 976=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_fixed_body_absolute() throws Exception {
        runTest("offset: nonzero box properties - html.fixed body.absolute");
    }

    /**
     * Test {976=[IE], 977=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "33",
            IE = "33")
    @NotYetImplemented
    public void offset__nonzero_box_properties___html_fixed_body_fixed() throws Exception {
        runTest("offset: nonzero box properties - html.fixed body.fixed");
    }

    /**
     * Test {979=[IE], 980=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented
    public void offset__iframe_scrollTop_Left__see_gh_1945_() throws Exception {
        runTest("offset: iframe scrollTop/Left (see gh-1945)");
    }

    /**
     * Test {996=[IE], 997=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    @NotYetImplemented
    public void dimensions__window_vs__large_document() throws Exception {
        runTest("dimensions: window vs. large document");
    }

    /**
     * Test {997=[IE], 998=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    public void dimensions__allow_modification_of_coordinates_argument__gh_1848_() throws Exception {
        runTest("dimensions: allow modification of coordinates argument (gh-1848)");
    }

    /**
     * Test {998=[IE], 999=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "1",
            IE = "1")
    @NotYetImplemented
    public void dimensions__outside_view_position__gh_2836_() throws Exception {
        runTest("dimensions: outside view position (gh-2836)");
    }

    /**
     * Test {999=[IE], 1000=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "2",
            IE = "2")
    public void dimensions__width_height_on_element_with_transform__gh_3193_() throws Exception {
        runTest("dimensions: width/height on element with transform (gh-3193)");
    }

    /**
     * Test {1000=[IE], 1001=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "8",
            IE = "8")
    public void dimensions__width_height_on_an_inline_element_with_no_explicitly_set_dimensions__gh_3571_() throws Exception {
        runTest("dimensions: width/height on an inline element with no explicitly-set dimensions (gh-3571)");
    }

    /**
     * Test {1001=[IE], 1002=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    @NotYetImplemented
    public void dimensions__width_height_on_an_inline_element_with_percentage_dimensions__gh_3611_() throws Exception {
        runTest("dimensions: width/height on an inline element with percentage dimensions (gh-3611)");
    }

    /**
     * Test {1002=[IE], 1003=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "4",
            IE = "4")
    @NotYetImplemented
    public void dimensions__width_height_on_a_table_row_with_phantom_borders__gh_3698_() throws Exception {
        runTest("dimensions: width/height on a table row with phantom borders (gh-3698)");
    }

    /**
     * Test {1003=[IE], 1004=[FF60]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF60 = "48",
            IE = "48")
    public void dimensions__interaction_with_scrollbars__gh_3589_() throws Exception {
        runTest("dimensions: interaction with scrollbars (gh-3589)");
    }
}
