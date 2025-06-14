/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.libraries;

import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for compatibility with web server loading of
 * version 1.11.3 of the <a href="http://jquery.com/">jQuery</a> JavaScript library.
 *
 * All test method inside this class are generated. Please have a look
 * at {@link org.htmlunit.source.JQueryExtractor}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class JQuery1x11x3Test extends JQueryTestBase {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return "1.11.3";
    }

    /**
     * Test {1=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_isReady() throws Exception {
        runTest("event: jQuery.isReady");
    }

    /**
     * Test {2=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void event__jQuery_ready() throws Exception {
        runTest("event: jQuery ready");
    }

    /**
     * Test {3=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__Unit_Testing_Environment() throws Exception {
        runTest("core: Unit Testing Environment");
    }

    /**
     * Test {4=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__Basic_requirements() throws Exception {
        runTest("core: Basic requirements");
    }

    /**
     * Test {5=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__jQuery__() throws Exception {
        runTest("core: jQuery()");
    }

    /**
     * Test {6=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery_selector__context_() throws Exception {
        runTest("core: jQuery(selector, context)");
    }

    /**
     * Test {7=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__selector_state() throws Exception {
        runTest("core: selector state");
    }

    /**
     * Test {8=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__globalEval() throws Exception {
        runTest("core: globalEval");
    }

    /**
     * Test {9=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__noConflict() throws Exception {
        runTest("core: noConflict");
    }

    /**
     * Test {10=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void core__trim() throws Exception {
        runTest("core: trim");
    }

    /**
     * Test {11=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__type() throws Exception {
        runTest("core: type");
    }

    /**
     * Test {12=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void core__isPlainObject() throws Exception {
        runTest("core: isPlainObject");
    }

    /**
     * Test {13=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__isFunction() throws Exception {
        runTest("core: isFunction");
    }

    /**
     * Test {14=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 38, 38")
    public void core__isNumeric() throws Exception {
        runTest("core: isNumeric");
    }

    /**
     * Test {15=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__isXMLDoc___HTML() throws Exception {
        runTest("core: isXMLDoc - HTML");
    }

    /**
     * Test {16=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__XSS_via_location_hash() throws Exception {
        runTest("core: XSS via location.hash");
    }

    /**
     * Test {17=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__isXMLDoc___XML() throws Exception {
        runTest("core: isXMLDoc - XML");
    }

    /**
     * Test {18=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__isWindow() throws Exception {
        runTest("core: isWindow");
    }

    /**
     * Test {19=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery__html__() throws Exception {
        runTest("core: jQuery('html')");
    }

    /**
     * Test {20=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery__massive_html__7990__() throws Exception {
        runTest("core: jQuery('massive html #7990')");
    }

    /**
     * Test {21=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__jQuery__html___context_() throws Exception {
        runTest("core: jQuery('html', context)");
    }

    /**
     * Test {22=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_selector__xml__text_str____loaded_via_xml_document() throws Exception {
        runTest("core: jQuery(selector, xml).text(str) - loaded via xml document");
    }

    /**
     * Test {23=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__end__() throws Exception {
        runTest("core: end()");
    }

    /**
     * Test {24=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__length() throws Exception {
        runTest("core: length");
    }

    /**
     * Test {25=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__get__() throws Exception {
        runTest("core: get()");
    }

    /**
     * Test {26=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__toArray__() throws Exception {
        runTest("core: toArray()");
    }

    /**
     * Test {27=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__inArray__() throws Exception {
        runTest("core: inArray()");
    }

    /**
     * Test {28=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get_Number_() throws Exception {
        runTest("core: get(Number)");
    }

    /**
     * Test {29=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get__Number_() throws Exception {
        runTest("core: get(-Number)");
    }

    /**
     * Test {30=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__each_Function_() throws Exception {
        runTest("core: each(Function)");
    }

    /**
     * Test {31=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__slice__() throws Exception {
        runTest("core: slice()");
    }

    /**
     * Test {32=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__first___last__() throws Exception {
        runTest("core: first()/last()");
    }

    /**
     * Test {33=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__map__() throws Exception {
        runTest("core: map()");
    }

    /**
     * Test {34=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void core__jQuery_map() throws Exception {
        runTest("core: jQuery.map");
    }

    /**
     * Test {35=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void core__jQuery_merge__() throws Exception {
        runTest("core: jQuery.merge()");
    }

    /**
     * Test {36=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_grep__() throws Exception {
        runTest("core: jQuery.grep()");
    }

    /**
     * Test {37=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__jQuery_extend_Object__Object_() throws Exception {
        runTest("core: jQuery.extend(Object, Object)");
    }

    /**
     * Test {38=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void core__jQuery_each_Object_Function_() throws Exception {
        runTest("core: jQuery.each(Object,Function)");
    }

    /**
     * Test {39=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__JIT_compilation_does_not_interfere_with_length_retrieval__gh_2145_() throws Exception {
        runTest("core: JIT compilation does not interfere with length retrieval (gh-2145)");
    }

    /**
     * Test {40=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void core__jQuery_makeArray() throws Exception {
        runTest("core: jQuery.makeArray");
    }

    /**
     * Test {41=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery_inArray() throws Exception {
        runTest("core: jQuery.inArray");
    }

    /**
     * Test {42=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_isEmptyObject() throws Exception {
        runTest("core: jQuery.isEmptyObject");
    }

    /**
     * Test {43=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void core__jQuery_proxy() throws Exception {
        runTest("core: jQuery.proxy");
    }

    /**
     * Test {44=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void core__jQuery_parseHTML() throws Exception {
        runTest("core: jQuery.parseHTML");
    }

    /**
     * Test {45=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void core__jQuery_parseJSON() throws Exception {
        runTest("core: jQuery.parseJSON");
    }

    /**
     * Test {46=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseXML() throws Exception {
        runTest("core: jQuery.parseXML");
    }

    /**
     * Test {47=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_camelCase__() throws Exception {
        runTest("core: jQuery.camelCase()");
    }

    /**
     * Test {48=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__Conditional_compilation_compatibility___13274_() throws Exception {
        runTest("core: Conditional compilation compatibility (#13274)");
    }

    /**
     * Test {49=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @HtmlUnitNYI(CHROME = "3, 1, 4",
            EDGE = "3, 1, 4",
            FF = "3, 1, 4",
            FF_ESR = "3, 1, 4")
    public void core__document_ready_when_jQuery_loaded_asynchronously___13655_() throws Exception {
        runTest("core: document ready when jQuery loaded asynchronously (#13655)");
    }

    /**
     * Test {50=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__Tolerating_alias_masked_DOM_properties___14074_() throws Exception {
        runTest("core: Tolerating alias-masked DOM properties (#14074)");
    }

    /**
     * Test {51=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__Don_t_call_window_onready___14802_() throws Exception {
        runTest("core: Don't call window.onready (#14802)");
    }

    /**
     * Test {52=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - no filter");
    }

    /**
     * Test {53=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks__________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - no filter");
    }

    /**
     * Test {54=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - filter");
    }

    /**
     * Test {55=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks__________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - filter");
    }

    /**
     * Test {56=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - no filter");
    }

    /**
     * Test {57=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - no filter");
    }

    /**
     * Test {58=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - filter");
    }

    /**
     * Test {59=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - filter");
    }

    /**
     * Test {60=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - no filter");
    }

    /**
     * Test {61=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - no filter");
    }

    /**
     * Test {62=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - filter");
    }

    /**
     * Test {63=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - filter");
    }

    /**
     * Test {64=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - no filter");
    }

    /**
     * Test {65=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - no filter");
    }

    /**
     * Test {66=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - filter");
    }

    /**
     * Test {67=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - filter");
    }

    /**
     * Test {68=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - no filter");
    }

    /**
     * Test {69=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {70=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - filter");
    }

    /**
     * Test {71=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {72=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - no filter");
    }

    /**
     * Test {73=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - no filter");
    }

    /**
     * Test {74=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - filter");
    }

    /**
     * Test {75=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - filter");
    }

    /**
     * Test {76=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - no filter");
    }

    /**
     * Test {77=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {78=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - filter");
    }

    /**
     * Test {79=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - filter");
    }

    /**
     * Test {80=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - no filter");
    }

    /**
     * Test {81=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {82=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - filter");
    }

    /**
     * Test {83=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {84=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - no filter");
    }

    /**
     * Test {85=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {86=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - filter");
    }

    /**
     * Test {87=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - filter");
    }

    /**
     * Test {88=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - no filter");
    }

    /**
     * Test {89=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {90=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - filter");
    }

    /**
     * Test {91=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {92=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - no filter");
    }

    /**
     * Test {93=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {94=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - filter");
    }

    /**
     * Test {95=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {96=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks__options_____options_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks( options ) - options are copied");
    }

    /**
     * Test {97=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_fireWith___arguments_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks.fireWith - arguments are copied");
    }

    /**
     * Test {98=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_remove___should_remove_all_instances() throws Exception {
        runTest("callbacks: jQuery.Callbacks.remove - should remove all instances");
    }

    /**
     * Test {99=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void callbacks__jQuery_Callbacks_has() throws Exception {
        runTest("callbacks: jQuery.Callbacks.has");
    }

    /**
     * Test {100=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_____adding_a_string_doesn_t_cause_a_stack_overflow() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow");
    }

    /**
     * Test {101=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred() throws Exception {
        runTest("deferred: jQuery.Deferred");
    }

    /**
     * Test {102=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred___new_operator() throws Exception {
        runTest("deferred: jQuery.Deferred - new operator");
    }

    /**
     * Test {103=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void deferred__jQuery_Deferred___chainability() throws Exception {
        runTest("deferred: jQuery.Deferred - chainability");
    }

    /**
     * Test {104=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (done)");
    }

    /**
     * Test {105=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (fail)");
    }

    /**
     * Test {106=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___filtering__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (progress)");
    }

    /**
     * Test {107=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (done)");
    }

    /**
     * Test {108=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (fail)");
    }

    /**
     * Test {109=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (progress)");
    }

    /**
     * Test {110=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void deferred__jQuery_Deferred_then___context() throws Exception {
        runTest("deferred: jQuery.Deferred.then - context");
    }

    /**
     * Test {111=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 37, 37")
    public void deferred__jQuery_when() throws Exception {
        runTest("deferred: jQuery.when");
    }

    /**
     * Test {112=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 119, 119")
    public void deferred__jQuery_when___joined() throws Exception {
        runTest("deferred: jQuery.when - joined");
    }

    /**
     * Test {113=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__zoom_of_doom___13089_() throws Exception {
        runTest("support: zoom of doom (#13089)");
    }

    /**
     * Test {114=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void support__body_background_is_not_lost_if_set_prior_to_loading_jQuery___9239_() throws Exception {
        runTest("support: body background is not lost if set prior to loading jQuery (#9239)");
    }

    /**
     * Test {115=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__A_background_on_the_testElement_does_not_cause_IE8_to_crash___9823_() throws Exception {
        runTest("support: A background on the testElement does not cause IE8 to crash (#9823)");
    }

    /**
     * Test {116=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__box_sizing_does_not_affect_jQuery_support_shrinkWrapBlocks() throws Exception {
        runTest("support: box-sizing does not affect jQuery.support.shrinkWrapBlocks");
    }

    /**
     * Test {117=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1, 1, 2")
    public void support__Check_CSP__https___developer_mozilla_org_en_US_docs_Security_CSP__restrictions() throws Exception {
        runTest("support: Check CSP (https://developer.mozilla.org/en-US/docs/Security/CSP) restrictions");
    }

    /**
     * Test {118=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 34, 34")
    public void support__Verify_that_the_support_tests_resolve_as_expected_per_browser() throws Exception {
        runTest("support: Verify that the support tests resolve as expected per browser");
    }

    /**
     * Test {119=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__expando() throws Exception {
        runTest("data: expando");
    }

    /**
     * Test {120=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void data__jQuery_data_div_() throws Exception {
        runTest("data: jQuery.data(div)");
    }

    /**
     * Test {121=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void data__jQuery_data____() throws Exception {
        runTest("data: jQuery.data({})");
    }

    /**
     * Test {122=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void data__jQuery_data_window_() throws Exception {
        runTest("data: jQuery.data(window)");
    }

    /**
     * Test {123=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void data__jQuery_data_document_() throws Exception {
        runTest("data: jQuery.data(document)");
    }

    /**
     * Test {124=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data__Expando_cleanup() throws Exception {
        runTest("data: Expando cleanup");
    }

    /**
     * Test {125=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__Data_is_not_being_set_on_comment_and_text_nodes() throws Exception {
        runTest("data: Data is not being set on comment and text nodes");
    }

    /**
     * Test {126=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_acceptData() throws Exception {
        runTest("data: jQuery.acceptData");
    }

    /**
     * Test {127=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__jQuery___data_______undefined___14101_() throws Exception {
        runTest("data: jQuery().data() === undefined (#14101)");
    }

    /**
     * Test {128=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void data___data__() throws Exception {
        runTest("data: .data()");
    }

    /**
     * Test {129=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void data__jQuery_Element__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(Element).data(String, Object).data(String)");
    }

    /**
     * Test {130=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void data__jQuery_plain_Object__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(plain Object).data(String, Object).data(String)");
    }

    /**
     * Test {131=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 43, 43")
    public void data__data___attributes() throws Exception {
        runTest("data: data-* attributes");
    }

    /**
     * Test {132=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data___data_Object_() throws Exception {
        runTest("data: .data(Object)");
    }

    /**
     * Test {133=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_removeData() throws Exception {
        runTest("data: jQuery.removeData");
    }

    /**
     * Test {134=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void data___removeData__() throws Exception {
        runTest("data: .removeData()");
    }

    /**
     * Test {135=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_serialization___8108_() throws Exception {
        runTest("data: JSON serialization (#8108)");
    }

    /**
     * Test {136=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: jQuery.data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {137=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__jQuery_data_should_not_miss_data_with_preset_hyphenated_property_names() throws Exception {
        runTest("data: jQuery.data should not miss data with preset hyphenated property names");
    }

    /**
     * Test {138=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void data__jQuery_data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {139=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void data__jQuery_data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: jQuery.data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {140=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data___removeData_supports_removal_of_hyphenated_properties_via_array___12786_() throws Exception {
        runTest("data: .removeData supports removal of hyphenated properties via array (#12786)");
    }

    /**
     * Test {141=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__Triggering_the_removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: Triggering the removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {142=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__Only_check_element_attributes_once_when_calling__data______8909() throws Exception {
        runTest("data: Only check element attributes once when calling .data() - #8909");
    }

    /**
     * Test {143=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_data__attributes_can_have_newlines() throws Exception {
        runTest("data: JSON data- attributes can have newlines");
    }

    /**
     * Test {144=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__enumerate_data_attrs_on_body___14894_() throws Exception {
        runTest("data: enumerate data attrs on body (#14894)");
    }

    /**
     * Test {145=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void queue__queue___with_other_types() throws Exception {
        runTest("queue: queue() with other types");
    }

    /**
     * Test {146=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_name__passes_in_the_next_item_in_the_queue_as_a_parameter() throws Exception {
        runTest("queue: queue(name) passes in the next item in the queue as a parameter");
    }

    /**
     * Test {147=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__queue___passes_in_the_next_item_in_the_queue_as_a_parameter_to_fx_queues() throws Exception {
        runTest("queue: queue() passes in the next item in the queue as a parameter to fx queues");
    }

    /**
     * Test {148=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__callbacks_keep_their_place_in_the_queue() throws Exception {
        runTest("queue: callbacks keep their place in the queue");
    }

    /**
     * Test {149=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__delay__() throws Exception {
        runTest("queue: delay()");
    }

    /**
     * Test {150=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__clearQueue_name__clears_the_queue() throws Exception {
        runTest("queue: clearQueue(name) clears the queue");
    }

    /**
     * Test {151=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void queue__clearQueue___clears_the_fx_queue() throws Exception {
        runTest("queue: clearQueue() clears the fx queue");
    }

    /**
     * Test {152=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__fn_promise_____called_when_fx_queue_is_empty() throws Exception {
        runTest("queue: fn.promise() - called when fx queue is empty");
    }

    /**
     * Test {153=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__fn_promise___queue______called_whenever_last_queue_function_is_dequeued() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued");
    }

    /**
     * Test {154=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__fn_promise___queue______waits_for_animation_to_complete_before_resolving() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving");
    }

    /**
     * Test {155=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue___promise_obj_() throws Exception {
        runTest("queue: .promise(obj)");
    }

    /**
     * Test {156=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__delay___can_be_stopped() throws Exception {
        runTest("queue: delay() can be stopped");
    }

    /**
     * Test {157=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_stop_hooks() throws Exception {
        runTest("queue: queue stop hooks");
    }

    /**
     * Test {158=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__jQuery_propFix_integrity_test() throws Exception {
        runTest("attributes: jQuery.propFix integrity test");
    }

    /**
     * Test {159=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 50, 50")
    public void attributes__attr_String_() throws Exception {
        runTest("attributes: attr(String)");
    }

    /**
     * Test {160=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__attr_String__on_cloned_elements___9646() throws Exception {
        runTest("attributes: attr(String) on cloned elements, #9646");
    }

    /**
     * Test {161=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_String__in_XML_Files() throws Exception {
        runTest("attributes: attr(String) in XML Files");
    }

    /**
     * Test {162=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Function_() throws Exception {
        runTest("attributes: attr(String, Function)");
    }

    /**
     * Test {163=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_Hash_() throws Exception {
        runTest("attributes: attr(Hash)");
    }

    /**
     * Test {164=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 71, 71")
    public void attributes__attr_String__Object_() throws Exception {
        runTest("attributes: attr(String, Object)");
    }

    /**
     * Test {165=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__attr___extending_the_boolean_attrHandle() throws Exception {
        runTest("attributes: attr - extending the boolean attrHandle");
    }

    /**
     * Test {166=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_document() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML document");
    }

    /**
     * Test {167=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_fragment() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML fragment");
    }

    /**
     * Test {168=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__attr__tabindex__() throws Exception {
        runTest("attributes: attr('tabindex')");
    }

    /**
     * Test {169=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr__tabindex___value_() throws Exception {
        runTest("attributes: attr('tabindex', value)");
    }

    /**
     * Test {170=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void attributes__removeAttr_String_() throws Exception {
        runTest("attributes: removeAttr(String)");
    }

    /**
     * Test {171=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeAttr_String__in_XML() throws Exception {
        runTest("attributes: removeAttr(String) in XML");
    }

    /**
     * Test {172=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeAttr_Multi_String__variable_space_width_() throws Exception {
        runTest("attributes: removeAttr(Multi String, variable space width)");
    }

    /**
     * Test {173=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__prop_String__Object_() throws Exception {
        runTest("attributes: prop(String, Object)");
    }

    /**
     * Test {174=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void attributes__prop_String__Object__on_null_undefined() throws Exception {
        runTest("attributes: prop(String, Object) on null/undefined");
    }

    /**
     * Test {175=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void attributes__prop__tabindex__() throws Exception {
        runTest("attributes: prop('tabindex')");
    }

    /**
     * Test {176=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void attributes__prop__tabindex___value_() throws Exception {
        runTest("attributes: prop('tabindex', value)");
    }

    /**
     * Test {177=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void attributes__removeProp_String_() throws Exception {
        runTest("attributes: removeProp(String)");
    }

    /**
     * Test {178=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__val___after_modification() throws Exception {
        runTest("attributes: val() after modification");
    }

    /**
     * Test {179=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void attributes__val__() throws Exception {
        runTest("attributes: val()");
    }

    /**
     * Test {180=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__val___with_non_matching_values_on_dropdown_list() throws Exception {
        runTest("attributes: val() with non-matching values on dropdown list");
    }

    /**
     * Test {181=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__val___respects_numbers_without_exception__Bug__9319_() throws Exception {
        runTest("attributes: val() respects numbers without exception (Bug #9319)");
    }

    /**
     * Test {182=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__val_String_Number_() throws Exception {
        runTest("attributes: val(String/Number)");
    }

    /**
     * Test {183=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__val_Function_() throws Exception {
        runTest("attributes: val(Function)");
    }

    /**
     * Test {184=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__val_Array_of_Numbers___Bug__7123_() throws Exception {
        runTest("attributes: val(Array of Numbers) (Bug #7123)");
    }

    /**
     * Test {185=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void attributes__val_Function__with_incoming_value() throws Exception {
        runTest("attributes: val(Function) with incoming value");
    }

    /**
     * Test {186=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__val_select__after_form_reset____Bug__2551_() throws Exception {
        runTest("attributes: val(select) after form.reset() (Bug #2551)");
    }

    /**
     * Test {187=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_String_() throws Exception {
        runTest("attributes: addClass(String)");
    }

    /**
     * Test {188=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_Function_() throws Exception {
        runTest("attributes: addClass(Function)");
    }

    /**
     * Test {189=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 52, 52")
    public void attributes__addClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: addClass(Function) with incoming value");
    }

    /**
     * Test {190=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeClass_String____simple() throws Exception {
        runTest("attributes: removeClass(String) - simple");
    }

    /**
     * Test {191=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeClass_Function____simple() throws Exception {
        runTest("attributes: removeClass(Function) - simple");
    }

    /**
     * Test {192=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 52, 52")
    public void attributes__removeClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: removeClass(Function) with incoming value");
    }

    /**
     * Test {193=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__removeClass___removes_duplicates() throws Exception {
        runTest("attributes: removeClass() removes duplicates");
    }

    /**
     * Test {194=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__removeClass_undefined__is_a_no_op() throws Exception {
        runTest("attributes: removeClass(undefined) is a no-op");
    }

    /**
     * Test {195=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_String_boolean_undefined___boolean__() throws Exception {
        runTest("attributes: toggleClass(String|boolean|undefined[, boolean])");
    }

    /**
     * Test {196=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_Function___boolean__() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean])");
    }

    /**
     * Test {197=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void attributes__toggleClass_Function___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean]) with incoming value");
    }

    /**
     * Test {198=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__addClass__removeClass__hasClass() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass");
    }

    /**
     * Test {199=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void attributes__addClass__removeClass__hasClass_on_many_elements() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass on many elements");
    }

    /**
     * Test {200=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__contents___hasClass___returns_correct_values() throws Exception {
        runTest("attributes: contents().hasClass() returns correct values");
    }

    /**
     * Test {201=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__hasClass_correctly_interprets_non_space_separators___13835_() throws Exception {
        runTest("attributes: hasClass correctly interprets non-space separators (#13835)");
    }

    /**
     * Test {202=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__coords_returns_correct_values_in_IE6_IE7__see__10828() throws Exception {
        runTest("attributes: coords returns correct values in IE6/IE7, see #10828");
    }

    /**
     * Test {203=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__should_not_throw_at___option__val_____14686_() throws Exception {
        runTest("attributes: should not throw at $(option).val() (#14686)");
    }

    /**
     * Test {204=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__Insignificant_white_space_returned_for___option__val_____14858_() throws Exception {
        runTest("attributes: Insignificant white space returned for $(option).val() (#14858)");
    }

    /**
     * Test {205=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__null_or_undefined_handler() throws Exception {
        runTest("event: null or undefined handler");
    }

    /**
     * Test {206=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__on___with_non_null_defined_data() throws Exception {
        runTest("event: on() with non-null,defined data");
    }

    /**
     * Test {207=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Handler_changes_and__trigger___order() throws Exception {
        runTest("event: Handler changes and .trigger() order");
    }

    /**
     * Test {208=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__on____with_data() throws Exception {
        runTest("event: on(), with data");
    }

    /**
     * Test {209=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__click____with_data() throws Exception {
        runTest("event: click(), with data");
    }

    /**
     * Test {210=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__on____with_data__trigger_with_data() throws Exception {
        runTest("event: on(), with data, trigger with data");
    }

    /**
     * Test {211=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__on____multiple_events_at_once() throws Exception {
        runTest("event: on(), multiple events at once");
    }

    /**
     * Test {212=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on____five_events_at_once() throws Exception {
        runTest("event: on(), five events at once");
    }

    /**
     * Test {213=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__on____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: on(), multiple events at once and namespaces");
    }

    /**
     * Test {214=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void event__on____namespace_with_special_add() throws Exception {
        runTest("event: on(), namespace with special add");
    }

    /**
     * Test {215=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on____no_data() throws Exception {
        runTest("event: on(), no data");
    }

    /**
     * Test {216=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__on_one_off_Object_() throws Exception {
        runTest("event: on/one/off(Object)");
    }

    /**
     * Test {217=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__on_off_Object___on_off_Object__String_() throws Exception {
        runTest("event: on/off(Object), on/off(Object, String)");
    }

    /**
     * Test {218=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__on_immediate_propagation() throws Exception {
        runTest("event: on immediate propagation");
    }

    /**
     * Test {219=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__on_bubbling__isDefaultPrevented__stopImmediatePropagation() throws Exception {
        runTest("event: on bubbling, isDefaultPrevented, stopImmediatePropagation");
    }

    /**
     * Test {220=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on____iframes() throws Exception {
        runTest("event: on(), iframes");
    }

    /**
     * Test {221=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__on____trigger_change_on_select() throws Exception {
        runTest("event: on(), trigger change on select");
    }

    /**
     * Test {222=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__on____namespaced_events__cloned_events() throws Exception {
        runTest("event: on(), namespaced events, cloned events");
    }

    /**
     * Test {223=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__on____multi_namespaced_events() throws Exception {
        runTest("event: on(), multi-namespaced events");
    }

    /**
     * Test {224=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__namespace_only_event_binding_is_a_no_op() throws Exception {
        runTest("event: namespace-only event binding is a no-op");
    }

    /**
     * Test {225=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__on____with_same_function() throws Exception {
        runTest("event: on(), with same function");
    }

    /**
     * Test {226=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on____make_sure_order_is_maintained() throws Exception {
        runTest("event: on(), make sure order is maintained");
    }

    /**
     * Test {227=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__on____with_different_this_object() throws Exception {
        runTest("event: on(), with different this object");
    }

    /**
     * Test {228=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__on_name__false___off_name__false_() throws Exception {
        runTest("event: on(name, false), off(name, false)");
    }

    /**
     * Test {229=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__on_name__selector__false___off_name__selector__false_() throws Exception {
        runTest("event: on(name, selector, false), off(name, selector, false)");
    }

    /**
     * Test {230=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__on___trigger___off___on_plain_object() throws Exception {
        runTest("event: on()/trigger()/off() on plain object");
    }

    /**
     * Test {231=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__off_type_() throws Exception {
        runTest("event: off(type)");
    }

    /**
     * Test {232=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__off_eventObject_() throws Exception {
        runTest("event: off(eventObject)");
    }

    /**
     * Test {233=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__hover___mouseenter_mouseleave() throws Exception {
        runTest("event: hover() mouseenter mouseleave");
    }

    /**
     * Test {234=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__mouseover_triggers_mouseenter() throws Exception {
        runTest("event: mouseover triggers mouseenter");
    }

    /**
     * Test {235=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__pointerover_triggers_pointerenter() throws Exception {
        runTest("event: pointerover triggers pointerenter");
    }

    /**
     * Test {236=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__withinElement_implemented_with_jQuery_contains__() throws Exception {
        runTest("event: withinElement implemented with jQuery.contains()");
    }

    /**
     * Test {237=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__mouseenter__mouseleave_don_t_catch_exceptions() throws Exception {
        runTest("event: mouseenter, mouseleave don't catch exceptions");
    }

    /**
     * Test {238=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__trigger___shortcuts() throws Exception {
        runTest("event: trigger() shortcuts");
    }

    /**
     * Test {239=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__trigger___bubbling() throws Exception {
        runTest("event: trigger() bubbling");
    }

    /**
     * Test {240=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__trigger_type___data____fn__() throws Exception {
        runTest("event: trigger(type, [data], [fn])");
    }

    /**
     * Test {241=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__submit_event_bubbles_on_copied_forms___11649_() throws Exception {
        runTest("event: submit event bubbles on copied forms (#11649)");
    }

    /**
     * Test {242=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__change_event_bubbles_on_copied_forms___11796_() throws Exception {
        runTest("event: change event bubbles on copied forms (#11796)");
    }

    /**
     * Test {243=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void event__trigger_eventObject___data____fn__() throws Exception {
        runTest("event: trigger(eventObject, [data], [fn])");
    }

    /**
     * Test {244=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event___trigger___bubbling_on_disconnected_elements___10489_() throws Exception {
        runTest("event: .trigger() bubbling on disconnected elements (#10489)");
    }

    /**
     * Test {245=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___trigger___doesn_t_bubble_load_event___10717_() throws Exception {
        runTest("event: .trigger() doesn't bubble load event (#10717)");
    }

    /**
     * Test {246=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__Delegated_events_in_SVG___10791___13180_() throws Exception {
        runTest("event: Delegated events in SVG (#10791; #13180)");
    }

    /**
     * Test {247=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__Delegated_events_in_forms___10844___11145___8165___11382___11764_() throws Exception {
        runTest("event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764)");
    }

    /**
     * Test {248=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Submit_event_can_be_stopped___11049_() throws Exception {
        runTest("event: Submit event can be stopped (#11049)");
    }

    /**
     * Test {249=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__on_beforeunload_() throws Exception {
        runTest("event: on(beforeunload)");
    }

    /**
     * Test {250=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__jQuery_Event__type__props__() throws Exception {
        runTest("event: jQuery.Event( type, props )");
    }

    /**
     * Test {251=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void event__jQuery_Event_properties() throws Exception {
        runTest("event: jQuery.Event properties");
    }

    /**
     * Test {252=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 65, 65")
    public void event___on____off__() throws Exception {
        runTest("event: .on()/.off()");
    }

    /**
     * Test {253=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_off_using_dispatched_jQuery_Event() throws Exception {
        runTest("event: jQuery.off using dispatched jQuery.Event");
    }

    /**
     * Test {254=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegated_event_with_delegateTarget_relative_selector() throws Exception {
        runTest("event: delegated event with delegateTarget-relative selector");
    }

    /**
     * Test {255=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__delegated_event_with_selector_matching_Object_prototype_property___13203_() throws Exception {
        runTest("event: delegated event with selector matching Object.prototype property (#13203)");
    }

    /**
     * Test {256=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__delegated_event_with_intermediate_DOM_manipulation___13208_() throws Exception {
        runTest("event: delegated event with intermediate DOM manipulation (#13208)");
    }

    /**
     * Test {257=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__stopPropagation___stops_directly_bound_events_on_delegated_target() throws Exception {
        runTest("event: stopPropagation() stops directly-bound events on delegated target");
    }

    /**
     * Test {258=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__off_all_bound_delegated_events() throws Exception {
        runTest("event: off all bound delegated events");
    }

    /**
     * Test {259=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on_with_multiple_delegated_events() throws Exception {
        runTest("event: on with multiple delegated events");
    }

    /**
     * Test {260=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__delegated_on_with_change() throws Exception {
        runTest("event: delegated on with change");
    }

    /**
     * Test {261=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__delegated_on_with_submit() throws Exception {
        runTest("event: delegated on with submit");
    }

    /**
     * Test {262=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__delegated_off___with_only_namespaces() throws Exception {
        runTest("event: delegated off() with only namespaces");
    }

    /**
     * Test {263=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Non_DOM_element_events() throws Exception {
        runTest("event: Non DOM element events");
    }

    /**
     * Test {264=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__inline_handler_returning_false_stops_default() throws Exception {
        runTest("event: inline handler returning false stops default");
    }

    /**
     * Test {265=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__window_resize() throws Exception {
        runTest("event: window resize");
    }

    /**
     * Test {266=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__focusin_bubbles() throws Exception {
        runTest("event: focusin bubbles");
    }

    /**
     * Test {267=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__custom_events_with_colons___3533___8272_() throws Exception {
        runTest("event: custom events with colons (#3533, #8272)");
    }

    /**
     * Test {268=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void event___on_and__off() throws Exception {
        runTest("event: .on and .off");
    }

    /**
     * Test {269=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__special_on_name_mapping() throws Exception {
        runTest("event: special on name mapping");
    }

    /**
     * Test {270=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event___on_and__off__selective_mixed_removal___10705_() throws Exception {
        runTest("event: .on and .off, selective mixed removal (#10705)");
    }

    /**
     * Test {271=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___on__event_map__null_selector__data____11130() throws Exception {
        runTest("event: .on( event-map, null-selector, data ) #11130");
    }

    /**
     * Test {272=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__clone___delegated_events___11076_() throws Exception {
        runTest("event: clone() delegated events (#11076)");
    }

    /**
     * Test {273=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void event__checkbox_state___3827_() throws Exception {
        runTest("event: checkbox state (#3827)");
    }

    /**
     * Test {274=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__hover_event_no_longer_special_since_1_9() throws Exception {
        runTest("event: hover event no longer special since 1.9");
    }

    /**
     * Test {275=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__fixHooks_extensions() throws Exception {
        runTest("event: fixHooks extensions");
    }

    /**
     * Test {276=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__focusin_using_non_element_targets() throws Exception {
        runTest("event: focusin using non-element targets");
    }

    /**
     * Test {277=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__focusin_from_an_iframe() throws Exception {
        runTest("event: focusin from an iframe");
    }

    /**
     * Test {278=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_promise() throws Exception {
        runTest("event: jQuery.ready promise");
    }

    /**
     * Test {279=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Focusing_iframe_element() throws Exception {
        runTest("event: Focusing iframe element");
    }

    /**
     * Test {280=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__triggerHandler_onbeforeunload_() throws Exception {
        runTest("event: triggerHandler(onbeforeunload)");
    }

    /**
     * Test {281=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_synchronous_load_with_long_loading_subresources() throws Exception {
        runTest("event: jQuery.ready synchronous load with long loading subresources");
    }

    /**
     * Test {282=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__change_handler_should_be_detached_from_element() throws Exception {
        runTest("event: change handler should be detached from element");
    }

    /**
     * Test {283=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__trigger_click_on_checkbox__fires_change_event() throws Exception {
        runTest("event: trigger click on checkbox, fires change event");
    }

    /**
     * Test {284=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__Namespace_preserved_when_passed_an_Event___12739_() throws Exception {
        runTest("event: Namespace preserved when passed an Event (#12739)");
    }

    /**
     * Test {285=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__make_sure_events_cloned_correctly() throws Exception {
        runTest("event: make sure events cloned correctly");
    }

    /**
     * Test {286=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__String_prototype_namespace_does_not_cause_trigger___to_throw___13360_() throws Exception {
        runTest("event: String.prototype.namespace does not cause trigger() to throw (#13360)");
    }

    /**
     * Test {287=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Inline_event_result_is_returned___13993_() throws Exception {
        runTest("event: Inline event result is returned (#13993)");
    }

    /**
     * Test {288=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1",
            EDGE = "1, 0, 1")
    @HtmlUnitNYI(CHROME = "0, 2, 2",
            EDGE = "0, 2, 2")
    public void event__Check_order_of_focusin_focusout_events() throws Exception {
        runTest("event: Check order of focusin/focusout events");
    }

    /**
     * Test {288=[FF, FF-ESR], 290=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void selector__element___jQuery_only() throws Exception {
        runTest("selector: element - jQuery only");
    }

    /**
     * Test {289=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 3, 4",
            EDGE = "1, 3, 4")
    @HtmlUnitNYI(CHROME = "0, 5, 5",
            EDGE = "0, 5, 5")
    public void event__focus_blur_order___12868_() throws Exception {
        runTest("event: focus-blur order (#12868)");
    }

    /**
     * Test {289=[FF, FF-ESR], 291=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void selector__id() throws Exception {
        runTest("selector: id");
    }

    /**
     * Test {290=[FF, FF-ESR], 292=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector__class___jQuery_only() throws Exception {
        runTest("selector: class - jQuery only");
    }

    /**
     * Test {291=[FF, FF-ESR], 293=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void selector__name() throws Exception {
        runTest("selector: name");
    }

    /**
     * Test {292=[FF, FF-ESR], 294=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector__selectors_with_comma() throws Exception {
        runTest("selector: selectors with comma");
    }

    /**
     * Test {293=[FF, FF-ESR], 295=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void selector__child_and_adjacent() throws Exception {
        runTest("selector: child and adjacent");
    }

    /**
     * Test {294=[FF, FF-ESR], 296=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 54, 54")
    public void selector__attributes() throws Exception {
        runTest("selector: attributes");
    }

    /**
     * Test {295=[FF, FF-ESR], 297=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void selector__disconnected_nodes() throws Exception {
        runTest("selector: disconnected nodes");
    }

    /**
     * Test {296=[FF, FF-ESR], 298=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void selector__disconnected_nodes___jQuery_only() throws Exception {
        runTest("selector: disconnected nodes - jQuery only");
    }

    /**
     * Test {297=[FF, FF-ESR], 299=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    public void selector__attributes___jQuery_attr() throws Exception {
        runTest("selector: attributes - jQuery.attr");
    }

    /**
     * Test {298=[FF, FF-ESR], 300=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void selector__jQuery_contains() throws Exception {
        runTest("selector: jQuery.contains");
    }

    /**
     * Test {299=[FF, FF-ESR], 301=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void selector__jQuery_unique() throws Exception {
        runTest("selector: jQuery.unique");
    }

    /**
     * Test {300=[FF, FF-ESR], 302=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector__Sizzle_cache_collides_with_multiple_Sizzles_on_a_page() throws Exception {
        runTest("selector: Sizzle cache collides with multiple Sizzles on a page");
    }

    /**
     * Test {301=[FF, FF-ESR], 303=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void selector__Iframe_dispatch_should_not_affect_jQuery___13936_() throws Exception {
        runTest("selector: Iframe dispatch should not affect jQuery (#13936)");
    }

    /**
     * Test {302=[FF, FF-ESR], 304=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__find_String_() throws Exception {
        runTest("traversing: find(String)");
    }

    /**
     * Test {303=[FF, FF-ESR], 305=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__find_String__under_non_elements() throws Exception {
        runTest("traversing: find(String) under non-elements");
    }

    /**
     * Test {304=[FF, FF-ESR], 306=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__find_leading_combinator_() throws Exception {
        runTest("traversing: find(leading combinator)");
    }

    /**
     * Test {305=[FF, FF-ESR], 307=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void traversing__find_node_jQuery_object_() throws Exception {
        runTest("traversing: find(node|jQuery object)");
    }

    /**
     * Test {306=[FF, FF-ESR], 308=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void traversing__is_String_undefined_() throws Exception {
        runTest("traversing: is(String|undefined)");
    }

    /**
     * Test {307=[FF, FF-ESR], 309=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void traversing__is___against_non_elements___10178_() throws Exception {
        runTest("traversing: is() against non-elements (#10178)");
    }

    /**
     * Test {308=[FF, FF-ESR], 310=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void traversing__is_jQuery_() throws Exception {
        runTest("traversing: is(jQuery)");
    }

    /**
     * Test {309=[FF, FF-ESR], 311=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__is___with__has___selectors() throws Exception {
        runTest("traversing: is() with :has() selectors");
    }

    /**
     * Test {310=[FF, FF-ESR], 312=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void traversing__is___with_positional_selectors() throws Exception {
        runTest("traversing: is() with positional selectors");
    }

    /**
     * Test {311=[FF, FF-ESR], 313=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__index__() throws Exception {
        runTest("traversing: index()");
    }

    /**
     * Test {312=[FF, FF-ESR], 314=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__index_Object_String_undefined_() throws Exception {
        runTest("traversing: index(Object|String|undefined)");
    }

    /**
     * Test {313=[FF, FF-ESR], 315=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__filter_Selector_undefined_() throws Exception {
        runTest("traversing: filter(Selector|undefined)");
    }

    /**
     * Test {314=[FF, FF-ESR], 316=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__filter_Function_() throws Exception {
        runTest("traversing: filter(Function)");
    }

    /**
     * Test {315=[FF, FF-ESR], 317=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Element_() throws Exception {
        runTest("traversing: filter(Element)");
    }

    /**
     * Test {316=[FF, FF-ESR], 318=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Array_() throws Exception {
        runTest("traversing: filter(Array)");
    }

    /**
     * Test {317=[FF, FF-ESR], 319=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_jQuery_() throws Exception {
        runTest("traversing: filter(jQuery)");
    }

    /**
     * Test {318=[FF, FF-ESR], 320=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void traversing__filter___with_positional_selectors() throws Exception {
        runTest("traversing: filter() with positional selectors");
    }

    /**
     * Test {319=[FF, FF-ESR], 321=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void traversing__closest__() throws Exception {
        runTest("traversing: closest()");
    }

    /**
     * Test {320=[FF, FF-ESR], 322=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__closest___with_positional_selectors() throws Exception {
        runTest("traversing: closest() with positional selectors");
    }

    /**
     * Test {321=[FF, FF-ESR], 323=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void traversing__closest_jQuery_() throws Exception {
        runTest("traversing: closest(jQuery)");
    }

    /**
     * Test {322=[FF, FF-ESR], 324=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__not_Selector_undefined_() throws Exception {
        runTest("traversing: not(Selector|undefined)");
    }

    /**
     * Test {323=[FF, FF-ESR], 325=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Element_() throws Exception {
        runTest("traversing: not(Element)");
    }

    /**
     * Test {324=[FF, FF-ESR], 326=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Function_() throws Exception {
        runTest("traversing: not(Function)");
    }

    /**
     * Test {325=[FF, FF-ESR], 327=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__not_Array_() throws Exception {
        runTest("traversing: not(Array)");
    }

    /**
     * Test {326=[FF, FF-ESR], 328=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_jQuery_() throws Exception {
        runTest("traversing: not(jQuery)");
    }

    /**
     * Test {327=[FF, FF-ESR], 329=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__has_Element_() throws Exception {
        runTest("traversing: has(Element)");
    }

    /**
     * Test {328=[FF, FF-ESR], 330=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__has_Selector_() throws Exception {
        runTest("traversing: has(Selector)");
    }

    /**
     * Test {329=[FF, FF-ESR], 331=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__has_Arrayish_() throws Exception {
        runTest("traversing: has(Arrayish)");
    }

    /**
     * Test {330=[FF, FF-ESR], 332=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__addBack__() throws Exception {
        runTest("traversing: addBack()");
    }

    /**
     * Test {331=[FF, FF-ESR], 333=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__siblings__String__() throws Exception {
        runTest("traversing: siblings([String])");
    }

    /**
     * Test {332=[FF, FF-ESR], 334=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__siblings__String_____jQuery_only() throws Exception {
        runTest("traversing: siblings([String]) - jQuery only");
    }

    /**
     * Test {333=[FF, FF-ESR], 335=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__children__String__() throws Exception {
        runTest("traversing: children([String])");
    }

    /**
     * Test {334=[FF, FF-ESR], 336=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__children__String_____jQuery_only() throws Exception {
        runTest("traversing: children([String]) - jQuery only");
    }

    /**
     * Test {335=[FF, FF-ESR], 337=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__parent__String__() throws Exception {
        runTest("traversing: parent([String])");
    }

    /**
     * Test {336=[FF, FF-ESR], 338=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__parents__String__() throws Exception {
        runTest("traversing: parents([String])");
    }

    /**
     * Test {337=[FF, FF-ESR], 339=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void traversing__parentsUntil__String__() throws Exception {
        runTest("traversing: parentsUntil([String])");
    }

    /**
     * Test {338=[FF, FF-ESR], 340=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__next__String__() throws Exception {
        runTest("traversing: next([String])");
    }

    /**
     * Test {339=[FF, FF-ESR], 341=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__prev__String__() throws Exception {
        runTest("traversing: prev([String])");
    }

    /**
     * Test {340=[FF, FF-ESR], 342=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__nextAll__String__() throws Exception {
        runTest("traversing: nextAll([String])");
    }

    /**
     * Test {341=[FF, FF-ESR], 343=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__prevAll__String__() throws Exception {
        runTest("traversing: prevAll([String])");
    }

    /**
     * Test {342=[FF, FF-ESR], 344=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__nextUntil__String__() throws Exception {
        runTest("traversing: nextUntil([String])");
    }

    /**
     * Test {343=[FF, FF-ESR], 345=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__prevUntil__String__() throws Exception {
        runTest("traversing: prevUntil([String])");
    }

    /**
     * Test {344=[FF, FF-ESR], 346=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__contents__() throws Exception {
        runTest("traversing: contents()");
    }

    /**
     * Test {345=[FF, FF-ESR], 347=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__sort_direction() throws Exception {
        runTest("traversing: sort direction");
    }

    /**
     * Test {346=[FF, FF-ESR], 348=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__add_String_selector_() throws Exception {
        runTest("traversing: add(String selector)");
    }

    /**
     * Test {347=[FF, FF-ESR], 349=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__add_String_selector__String_context_() throws Exception {
        runTest("traversing: add(String selector, String context)");
    }

    /**
     * Test {348=[FF, FF-ESR], 350=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__add_String_html_() throws Exception {
        runTest("traversing: add(String html)");
    }

    /**
     * Test {349=[FF, FF-ESR], 351=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__add_jQuery_() throws Exception {
        runTest("traversing: add(jQuery)");
    }

    /**
     * Test {350=[FF, FF-ESR], 352=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__add_Element_() throws Exception {
        runTest("traversing: add(Element)");
    }

    /**
     * Test {351=[FF, FF-ESR], 353=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__add_Array_elements_() throws Exception {
        runTest("traversing: add(Array elements)");
    }

    /**
     * Test {352=[FF, FF-ESR], 354=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__add_Window_() throws Exception {
        runTest("traversing: add(Window)");
    }

    /**
     * Test {353=[FF, FF-ESR], 355=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__add_NodeList_undefined_HTMLFormElement_HTMLSelectElement_() throws Exception {
        runTest("traversing: add(NodeList|undefined|HTMLFormElement|HTMLSelectElement)");
    }

    /**
     * Test {354=[FF, FF-ESR], 356=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__add_String__Context_() throws Exception {
        runTest("traversing: add(String, Context)");
    }

    /**
     * Test {355=[FF, FF-ESR], 357=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__eq___1____10616() throws Exception {
        runTest("traversing: eq('-1') #10616");
    }

    /**
     * Test {356=[FF, FF-ESR], 358=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__index_no_arg___10977() throws Exception {
        runTest("traversing: index(no arg) #10977");
    }

    /**
     * Test {357=[FF, FF-ESR], 359=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__traversing_non_elements_with_attribute_filters___12523_() throws Exception {
        runTest("traversing: traversing non-elements with attribute filters (#12523)");
    }

    /**
     * Test {358=[FF, FF-ESR], 360=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__text__() throws Exception {
        runTest("manipulation: text()");
    }

    /**
     * Test {359=[FF, FF-ESR], 361=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__text_undefined_() throws Exception {
        runTest("manipulation: text(undefined)");
    }

    /**
     * Test {360=[FF, FF-ESR], 362=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__text_String_() throws Exception {
        runTest("manipulation: text(String)");
    }

    /**
     * Test {361=[FF, FF-ESR], 363=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__text_Function_() throws Exception {
        runTest("manipulation: text(Function)");
    }

    /**
     * Test {362=[FF, FF-ESR], 364=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__text_Function__with_incoming_value() throws Exception {
        runTest("manipulation: text(Function) with incoming value");
    }

    /**
     * Test {363=[FF, FF-ESR], 365=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 78, 78")
    public void manipulation__append_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {364=[FF, FF-ESR], 366=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 78, 78")
    public void manipulation__append_Function_() throws Exception {
        runTest("manipulation: append(Function)");
    }

    /**
     * Test {365=[FF, FF-ESR], 367=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__append_param__to_object__see__11280() throws Exception {
        runTest("manipulation: append(param) to object, see #11280");
    }

    /**
     * Test {366=[FF, FF-ESR], 368=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__append_Function__returns_String() throws Exception {
        runTest("manipulation: append(Function) returns String");
    }

    /**
     * Test {367=[FF, FF-ESR], 369=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_Function__returns_Element() throws Exception {
        runTest("manipulation: append(Function) returns Element");
    }

    /**
     * Test {368=[FF, FF-ESR], 370=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: append(Function) returns Array<Element>");
    }

    /**
     * Test {369=[FF, FF-ESR], 371=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_Function__returns_jQuery() throws Exception {
        runTest("manipulation: append(Function) returns jQuery");
    }

    /**
     * Test {370=[FF, FF-ESR], 372=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_Function__returns_Number() throws Exception {
        runTest("manipulation: append(Function) returns Number");
    }

    /**
     * Test {371=[FF, FF-ESR], 373=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__XML_DOM_manipulation___9960_() throws Exception {
        runTest("manipulation: XML DOM manipulation (#9960)");
    }

    /**
     * Test {372=[FF, FF-ESR], 374=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_the_same_fragment_with_events__Bug__6997__5566_() throws Exception {
        runTest("manipulation: append the same fragment with events (Bug #6997, 5566)");
    }

    /**
     * Test {373=[FF, FF-ESR], 375=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_HTML5_sectioning_elements__Bug__6485_() throws Exception {
        runTest("manipulation: append HTML5 sectioning elements (Bug #6485)");
    }

    /**
     * Test {374=[FF, FF-ESR], 376=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__HTML5_Elements_inherit_styles_from_style_rules__Bug__10501_() throws Exception {
        runTest("manipulation: HTML5 Elements inherit styles from style rules (Bug #10501)");
    }

    /**
     * Test {375=[FF, FF-ESR], 377=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_String__with_HTML5__Bug__6485_() throws Exception {
        runTest("manipulation: html(String) with HTML5 (Bug #6485)");
    }

    /**
     * Test {376=[FF, FF-ESR], 378=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__IE8_serialization_bug() throws Exception {
        runTest("manipulation: IE8 serialization bug");
    }

    /**
     * Test {377=[FF, FF-ESR], 379=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___object_element__10324() throws Exception {
        runTest("manipulation: html() object element #10324");
    }

    /**
     * Test {378=[FF, FF-ESR], 380=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__append_xml_() throws Exception {
        runTest("manipulation: append(xml)");
    }

    /**
     * Test {379=[FF, FF-ESR], 381=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__appendTo_String_() throws Exception {
        runTest("manipulation: appendTo(String)");
    }

    /**
     * Test {380=[FF, FF-ESR], 382=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__appendTo_Element_Array_Element__() throws Exception {
        runTest("manipulation: appendTo(Element|Array<Element>)");
    }

    /**
     * Test {381=[FF, FF-ESR], 383=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__appendTo_jQuery_() throws Exception {
        runTest("manipulation: appendTo(jQuery)");
    }

    /**
     * Test {382=[FF, FF-ESR], 384=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prepend_String_() throws Exception {
        runTest("manipulation: prepend(String)");
    }

    /**
     * Test {383=[FF, FF-ESR], 385=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prepend_Element_() throws Exception {
        runTest("manipulation: prepend(Element)");
    }

    /**
     * Test {384=[FF, FF-ESR], 386=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prepend_Array_Element__() throws Exception {
        runTest("manipulation: prepend(Array<Element>)");
    }

    /**
     * Test {385=[FF, FF-ESR], 387=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prepend_jQuery_() throws Exception {
        runTest("manipulation: prepend(jQuery)");
    }

    /**
     * Test {386=[FF, FF-ESR], 388=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prepend_Array_jQuery__() throws Exception {
        runTest("manipulation: prepend(Array<jQuery>)");
    }

    /**
     * Test {387=[FF, FF-ESR], 389=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__prepend_Function__with_incoming_value____String() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- String");
    }

    /**
     * Test {388=[FF, FF-ESR], 390=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prepend_Function__with_incoming_value____Element() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Element");
    }

    /**
     * Test {389=[FF, FF-ESR], 391=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prepend_Function__with_incoming_value____Array_Element_() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Array<Element>");
    }

    /**
     * Test {390=[FF, FF-ESR], 392=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prepend_Function__with_incoming_value____jQuery() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- jQuery");
    }

    /**
     * Test {391=[FF, FF-ESR], 393=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prependTo_String_() throws Exception {
        runTest("manipulation: prependTo(String)");
    }

    /**
     * Test {392=[FF, FF-ESR], 394=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prependTo_Element_() throws Exception {
        runTest("manipulation: prependTo(Element)");
    }

    /**
     * Test {393=[FF, FF-ESR], 395=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prependTo_Array_Element__() throws Exception {
        runTest("manipulation: prependTo(Array<Element>)");
    }

    /**
     * Test {394=[FF, FF-ESR], 396=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prependTo_jQuery_() throws Exception {
        runTest("manipulation: prependTo(jQuery)");
    }

    /**
     * Test {395=[FF, FF-ESR], 397=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prependTo_Array_jQuery__() throws Exception {
        runTest("manipulation: prependTo(Array<jQuery>)");
    }

    /**
     * Test {396=[FF, FF-ESR], 398=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_String_() throws Exception {
        runTest("manipulation: before(String)");
    }

    /**
     * Test {397=[FF, FF-ESR], 399=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Element_() throws Exception {
        runTest("manipulation: before(Element)");
    }

    /**
     * Test {398=[FF, FF-ESR], 400=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Array_Element__() throws Exception {
        runTest("manipulation: before(Array<Element>)");
    }

    /**
     * Test {399=[FF, FF-ESR], 401=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_jQuery_() throws Exception {
        runTest("manipulation: before(jQuery)");
    }

    /**
     * Test {400=[FF, FF-ESR], 402=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Array_jQuery__() throws Exception {
        runTest("manipulation: before(Array<jQuery>)");
    }

    /**
     * Test {401=[FF, FF-ESR], 403=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_String() throws Exception {
        runTest("manipulation: before(Function) -- Returns String");
    }

    /**
     * Test {402=[FF, FF-ESR], 404=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_Element() throws Exception {
        runTest("manipulation: before(Function) -- Returns Element");
    }

    /**
     * Test {403=[FF, FF-ESR], 405=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_Array_Element_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<Element>");
    }

    /**
     * Test {404=[FF, FF-ESR], 406=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_jQuery() throws Exception {
        runTest("manipulation: before(Function) -- Returns jQuery");
    }

    /**
     * Test {405=[FF, FF-ESR], 407=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_Array_jQuery_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<jQuery>");
    }

    /**
     * Test {406=[FF, FF-ESR], 408=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_no_op_() throws Exception {
        runTest("manipulation: before(no-op)");
    }

    /**
     * Test {407=[FF, FF-ESR], 409=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_and_after_w__empty_object___10812_() throws Exception {
        runTest("manipulation: before and after w/ empty object (#10812)");
    }

    /**
     * Test {408=[FF, FF-ESR], 410=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation___before___and__after___disconnected_node() throws Exception {
        runTest("manipulation: .before() and .after() disconnected node");
    }

    /**
     * Test {410=[FF, FF-ESR], 412=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insert_with__before___on_disconnected_node_first() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node first");
    }

    /**
     * Test {412=[FF, FF-ESR], 414=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insert_with__before___on_disconnected_node_last() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node last");
    }

    /**
     * Test {413=[FF, FF-ESR], 415=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertBefore_String_() throws Exception {
        runTest("manipulation: insertBefore(String)");
    }

    /**
     * Test {414=[FF, FF-ESR], 416=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertBefore_Element_() throws Exception {
        runTest("manipulation: insertBefore(Element)");
    }

    /**
     * Test {415=[FF, FF-ESR], 417=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertBefore_Array_Element__() throws Exception {
        runTest("manipulation: insertBefore(Array<Element>)");
    }

    /**
     * Test {416=[FF, FF-ESR], 418=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertBefore_jQuery_() throws Exception {
        runTest("manipulation: insertBefore(jQuery)");
    }

    /**
     * Test {417=[FF, FF-ESR], 419=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_String_() throws Exception {
        runTest("manipulation: .after(String)");
    }

    /**
     * Test {418=[FF, FF-ESR], 420=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Element_() throws Exception {
        runTest("manipulation: .after(Element)");
    }

    /**
     * Test {419=[FF, FF-ESR], 421=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Array_Element__() throws Exception {
        runTest("manipulation: .after(Array<Element>)");
    }

    /**
     * Test {420=[FF, FF-ESR], 422=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_jQuery_() throws Exception {
        runTest("manipulation: .after(jQuery)");
    }

    /**
     * Test {421=[FF, FF-ESR], 423=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Function__returns_String() throws Exception {
        runTest("manipulation: .after(Function) returns String");
    }

    /**
     * Test {422=[FF, FF-ESR], 424=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Function__returns_Element() throws Exception {
        runTest("manipulation: .after(Function) returns Element");
    }

    /**
     * Test {423=[FF, FF-ESR], 425=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: .after(Function) returns Array<Element>");
    }

    /**
     * Test {424=[FF, FF-ESR], 426=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Function__returns_jQuery() throws Exception {
        runTest("manipulation: .after(Function) returns jQuery");
    }

    /**
     * Test {425=[FF, FF-ESR], 427=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation___after_disconnected_node_() throws Exception {
        runTest("manipulation: .after(disconnected node)");
    }

    /**
     * Test {426=[FF, FF-ESR], 428=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertAfter_String_() throws Exception {
        runTest("manipulation: insertAfter(String)");
    }

    /**
     * Test {427=[FF, FF-ESR], 429=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertAfter_Element_() throws Exception {
        runTest("manipulation: insertAfter(Element)");
    }

    /**
     * Test {428=[FF, FF-ESR], 430=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertAfter_Array_Element__() throws Exception {
        runTest("manipulation: insertAfter(Array<Element>)");
    }

    /**
     * Test {429=[FF, FF-ESR], 431=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertAfter_jQuery_() throws Exception {
        runTest("manipulation: insertAfter(jQuery)");
    }

    /**
     * Test {430=[FF, FF-ESR], 432=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void manipulation__replaceWith_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {431=[FF, FF-ESR], 433=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    public void manipulation__replaceWith_Function_() throws Exception {
        runTest("manipulation: replaceWith(Function)");
    }

    /**
     * Test {432=[FF, FF-ESR], 434=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceWith_string__for_more_than_one_element() throws Exception {
        runTest("manipulation: replaceWith(string) for more than one element");
    }

    /**
     * Test {433=[FF, FF-ESR], 435=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__Empty_replaceWith___13401___13596_() throws Exception {
        runTest("manipulation: Empty replaceWith (#13401; #13596)");
    }

    /**
     * Test {434=[FF, FF-ESR], 436=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__replaceAll_String_() throws Exception {
        runTest("manipulation: replaceAll(String)");
    }

    /**
     * Test {435=[FF, FF-ESR], 437=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__replaceAll_Element_() throws Exception {
        runTest("manipulation: replaceAll(Element)");
    }

    /**
     * Test {436=[FF, FF-ESR], 438=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceAll_Array_Element__() throws Exception {
        runTest("manipulation: replaceAll(Array<Element>)");
    }

    /**
     * Test {437=[FF, FF-ESR], 439=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceAll_jQuery_() throws Exception {
        runTest("manipulation: replaceAll(jQuery)");
    }

    /**
     * Test {438=[FF, FF-ESR], 440=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery_clone_____8017_() throws Exception {
        runTest("manipulation: jQuery.clone() (#8017)");
    }

    /**
     * Test {439=[FF, FF-ESR], 441=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_to_multiple_elements___8070_() throws Exception {
        runTest("manipulation: append to multiple elements (#8070)");
    }

    /**
     * Test {440=[FF, FF-ESR], 442=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__table_manipulation() throws Exception {
        runTest("manipulation: table manipulation");
    }

    /**
     * Test {441=[FF, FF-ESR], 443=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 45, 45")
    public void manipulation__clone__() throws Exception {
        runTest("manipulation: clone()");
    }

    /**
     * Test {442=[FF, FF-ESR], 444=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__clone_script_type_non_javascript____11359_() throws Exception {
        runTest("manipulation: clone(script type=non-javascript) (#11359)");
    }

    /**
     * Test {443=[FF, FF-ESR], 445=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__clone_form_element___Bug__3879___6655_() throws Exception {
        runTest("manipulation: clone(form element) (Bug #3879, #6655)");
    }

    /**
     * Test {444=[FF, FF-ESR], 446=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__clone_multiple_selected_options___Bug__8129_() throws Exception {
        runTest("manipulation: clone(multiple selected options) (Bug #8129)");
    }

    /**
     * Test {445=[FF, FF-ESR], 447=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_XML_nodes() throws Exception {
        runTest("manipulation: clone() on XML nodes");
    }

    /**
     * Test {446=[FF, FF-ESR], 448=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_local_XML_nodes_with_html5_nodename() throws Exception {
        runTest("manipulation: clone() on local XML nodes with html5 nodename");
    }

    /**
     * Test {447=[FF, FF-ESR], 449=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html_undefined_() throws Exception {
        runTest("manipulation: html(undefined)");
    }

    /**
     * Test {448=[FF, FF-ESR], 450=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___on_empty_set() throws Exception {
        runTest("manipulation: html() on empty set");
    }

    /**
     * Test {449=[FF, FF-ESR], 451=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 39, 39")
    public void manipulation__html_String_Number_() throws Exception {
        runTest("manipulation: html(String|Number)");
    }

    /**
     * Test {450=[FF, FF-ESR], 452=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 39, 39")
    public void manipulation__html_Function_() throws Exception {
        runTest("manipulation: html(Function)");
    }

    /**
     * Test {451=[FF, FF-ESR], 453=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html____text____() throws Exception {
        runTest("manipulation: html( $.text() )");
    }

    /**
     * Test {452=[FF, FF-ESR], 454=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html__fn___returns___text__() throws Exception {
        runTest("manipulation: html( fn ) returns $.text()");
    }

    /**
     * Test {453=[FF, FF-ESR], 455=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__html_Function__with_incoming_value____direct_selection() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- direct selection");
    }

    /**
     * Test {454=[FF, FF-ESR], 456=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void manipulation__html_Function__with_incoming_value____jQuery_contents__() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- jQuery.contents()");
    }

    /**
     * Test {455=[FF, FF-ESR], 457=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___html___don_t_expose_jQuery_Sizzle_expandos___12858_() throws Exception {
        runTest("manipulation: clone()/html() don't expose jQuery/Sizzle expandos (#12858)");
    }

    /**
     * Test {456=[FF, FF-ESR], 458=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__remove___no_filters() throws Exception {
        runTest("manipulation: remove() no filters");
    }

    /**
     * Test {457=[FF, FF-ESR], 459=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__remove___with_filters() throws Exception {
        runTest("manipulation: remove() with filters");
    }

    /**
     * Test {458=[FF, FF-ESR], 460=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__remove___event_cleaning() throws Exception {
        runTest("manipulation: remove() event cleaning");
    }

    /**
     * Test {459=[FF, FF-ESR], 461=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__remove___in_document_order__13779() throws Exception {
        runTest("manipulation: remove() in document order #13779");
    }

    /**
     * Test {460=[FF, FF-ESR], 462=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__detach___no_filters() throws Exception {
        runTest("manipulation: detach() no filters");
    }

    /**
     * Test {461=[FF, FF-ESR], 463=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__detach___with_filters() throws Exception {
        runTest("manipulation: detach() with filters");
    }

    /**
     * Test {462=[FF, FF-ESR], 464=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__detach___event_cleaning() throws Exception {
        runTest("manipulation: detach() event cleaning");
    }

    /**
     * Test {463=[FF, FF-ESR], 465=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__empty__() throws Exception {
        runTest("manipulation: empty()");
    }

    /**
     * Test {464=[FF, FF-ESR], 466=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void manipulation__jQuery_cleanData() throws Exception {
        runTest("manipulation: jQuery.cleanData");
    }

    /**
     * Test {465=[FF, FF-ESR], 467=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___no_plain_text_caching__Bug__6779_() throws Exception {
        runTest("manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779)");
    }

    /**
     * Test {466=[FF, FF-ESR], 468=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__jQuery_html___execute_scripts_escaped_with_html_comment_or_CDATA___9221_() throws Exception {
        runTest("manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221)");
    }

    /**
     * Test {467=[FF, FF-ESR], 469=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___plain_objects_are_not_a_document__8950() throws Exception {
        runTest("manipulation: jQuery.buildFragment - plain objects are not a document #8950");
    }

    /**
     * Test {468=[FF, FF-ESR], 470=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_clone___no_exceptions_for_object_elements__9587() throws Exception {
        runTest("manipulation: jQuery.clone - no exceptions for object elements #9587");
    }

    /**
     * Test {469=[FF, FF-ESR], 471=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__Cloned__detached_HTML5_elems___10667_10670_() throws Exception {
        runTest("manipulation: Cloned, detached HTML5 elems (#10667,10670)");
    }

    /**
     * Test {470=[FF, FF-ESR], 472=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__Guard_against_exceptions_when_clearing_safeChildNodes() throws Exception {
        runTest("manipulation: Guard against exceptions when clearing safeChildNodes");
    }

    /**
     * Test {471=[FF, FF-ESR], 473=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__Ensure_oldIE_creates_a_new_set_on_appendTo___8894_() throws Exception {
        runTest("manipulation: Ensure oldIE creates a new set on appendTo (#8894)");
    }

    /**
     * Test {472=[FF, FF-ESR], 474=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_____script_exceptions_bubble___11743_() throws Exception {
        runTest("manipulation: html() - script exceptions bubble (#11743)");
    }

    /**
     * Test {473=[FF, FF-ESR], 475=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__checked_state_is_cloned_with_clone__() throws Exception {
        runTest("manipulation: checked state is cloned with clone()");
    }

    /**
     * Test {474=[FF, FF-ESR], 476=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__manipulate_mixed_jQuery_and_text___12384___12346_() throws Exception {
        runTest("manipulation: manipulate mixed jQuery and text (#12384, #12346)");
    }

    /**
     * Test {475=[FF, FF-ESR], 477=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__buildFragment_works_even_if_document_0__is_iframe_s_window_object_in_IE9_10___12266_() throws Exception {
        runTest("manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266)");
    }

    /**
     * Test {476=[FF, FF-ESR], 478=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void manipulation__script_evaluation___11795_() throws Exception {
        runTest("manipulation: script evaluation (#11795)");
    }

    /**
     * Test {477=[FF, FF-ESR], 479=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__jQuery__evalUrl___12838_() throws Exception {
        runTest("manipulation: jQuery._evalUrl (#12838)");
    }

    /**
     * Test {478=[FF, FF-ESR], 480=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__insertAfter__insertBefore__etc_do_not_work_when_destination_is_original_element__Element_is_removed___4087_() throws Exception {
        runTest("manipulation: insertAfter, insertBefore, etc do not work when destination is original element. Element is removed (#4087)");
    }

    /**
     * Test {479=[FF, FF-ESR], 481=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__Index_for_function_argument_should_be_received___13094_() throws Exception {
        runTest("manipulation: Index for function argument should be received (#13094)");
    }

    /**
     * Test {480=[FF, FF-ESR], 482=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__Make_sure_jQuery_fn_remove_can_work_on_elements_in_documentFragment() throws Exception {
        runTest("manipulation: Make sure jQuery.fn.remove can work on elements in documentFragment");
    }

    /**
     * Test {481=[FF, FF-ESR], 483=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void wrap__wrap_String_Element_() throws Exception {
        runTest("wrap: wrap(String|Element)");
    }

    /**
     * Test {482=[FF, FF-ESR], 484=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void wrap__wrap_Function_() throws Exception {
        runTest("wrap: wrap(Function)");
    }

    /**
     * Test {483=[FF, FF-ESR], 485=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void wrap__wrap_Function__with_index___10177_() throws Exception {
        runTest("wrap: wrap(Function) with index (#10177)");
    }

    /**
     * Test {484=[FF, FF-ESR], 486=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void wrap__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("wrap: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {485=[FF, FF-ESR], 487=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void wrap__wrapAll_String_() throws Exception {
        runTest("wrap: wrapAll(String)");
    }

    /**
     * Test {486=[FF, FF-ESR], 488=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void wrap__wrapAll_Element_() throws Exception {
        runTest("wrap: wrapAll(Element)");
    }

    /**
     * Test {487=[FF, FF-ESR], 489=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void wrap__wrapInner_String_() throws Exception {
        runTest("wrap: wrapInner(String)");
    }

    /**
     * Test {488=[FF, FF-ESR], 490=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void wrap__wrapInner_Element_() throws Exception {
        runTest("wrap: wrapInner(Element)");
    }

    /**
     * Test {489=[FF, FF-ESR], 491=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void wrap__wrapInner_Function__returns_String() throws Exception {
        runTest("wrap: wrapInner(Function) returns String");
    }

    /**
     * Test {490=[FF, FF-ESR], 492=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void wrap__wrapInner_Function__returns_Element() throws Exception {
        runTest("wrap: wrapInner(Function) returns Element");
    }

    /**
     * Test {491=[FF, FF-ESR], 493=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void wrap__unwrap__() throws Exception {
        runTest("wrap: unwrap()");
    }

    /**
     * Test {492=[FF, FF-ESR], 494=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void wrap__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("wrap: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {493=[FF, FF-ESR], 495=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void wrap__wrapping_scripts___10470_() throws Exception {
        runTest("wrap: wrapping scripts (#10470)");
    }

    /**
     * Test {494=[FF, FF-ESR], 496=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 43, 43")
    public void css__css_String_Hash_() throws Exception {
        runTest("css: css(String|Hash)");
    }

    /**
     * Test {495=[FF, FF-ESR], 497=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void css__css___explicit_and_relative_values() throws Exception {
        runTest("css: css() explicit and relative values");
    }

    /**
     * Test {496=[FF, FF-ESR], 498=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void css__css_String__Object_() throws Exception {
        runTest("css: css(String, Object)");
    }

    /**
     * Test {497=[FF, FF-ESR], 499=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__css_Array_() throws Exception {
        runTest("css: css(Array)");
    }

    /**
     * Test {498=[FF, FF-ESR], 500=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function_() throws Exception {
        runTest("css: css(String, Function)");
    }

    /**
     * Test {499=[FF, FF-ESR], 501=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function__with_incoming_value() throws Exception {
        runTest("css: css(String, Function) with incoming value");
    }

    /**
     * Test {500=[FF, FF-ESR], 502=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions() throws Exception {
        runTest("css: css(Object) where values are Functions");
    }

    /**
     * Test {501=[FF, FF-ESR], 503=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions_with_incoming_values() throws Exception {
        runTest("css: css(Object) where values are Functions with incoming values");
    }

    /**
     * Test {502=[FF, FF-ESR], 504=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__show____hide__() throws Exception {
        runTest("css: show(); hide()");
    }

    /**
     * Test {503=[FF, FF-ESR], 505=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void css__show___() throws Exception {
        runTest("css: show();");
    }

    /**
     * Test {504=[FF, FF-ESR], 506=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void css__show___resolves_correct_default_display__8099() throws Exception {
        runTest("css: show() resolves correct default display #8099");
    }

    /**
     * Test {505=[FF, FF-ESR], 507=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void css__show___resolves_correct_default_display_for_detached_nodes() throws Exception {
        runTest("css: show() resolves correct default display for detached nodes");
    }

    /**
     * Test {506=[FF, FF-ESR], 508=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__show___resolves_correct_default_display__10227() throws Exception {
        runTest("css: show() resolves correct default display #10227");
    }

    /**
     * Test {507=[FF, FF-ESR], 509=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__show___resolves_correct_default_display_when_iframe_display_none__12904() throws Exception {
        runTest("css: show() resolves correct default display when iframe display:none #12904");
    }

    /**
     * Test {508=[FF, FF-ESR], 510=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void css__toggle__() throws Exception {
        runTest("css: toggle()");
    }

    /**
     * Test {509=[FF, FF-ESR], 511=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__hide_hidden_elements__bug__7141_() throws Exception {
        runTest("css: hide hidden elements (bug #7141)");
    }

    /**
     * Test {510=[FF, FF-ESR], 512=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {511=[FF, FF-ESR], 513=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__internal_ref_to_elem_runtimeStyle__bug__7608_() throws Exception {
        runTest("css: internal ref to elem.runtimeStyle (bug #7608)");
    }

    /**
     * Test {512=[FF, FF-ESR], 514=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__marginRight_computed_style__bug__3333_() throws Exception {
        runTest("css: marginRight computed style (bug #3333)");
    }

    /**
     * Test {513=[FF, FF-ESR], 515=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__box_model_properties_incorrectly_returning___instead_of_px__see__10639_and__12088() throws Exception {
        runTest("css: box model properties incorrectly returning % instead of px, see #10639 and #12088");
    }

    /**
     * Test {514=[FF, FF-ESR], 516=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__jQuery_cssProps_behavior___bug__8402_() throws Exception {
        runTest("css: jQuery.cssProps behavior, (bug #8402)");
    }

    /**
     * Test {515=[FF, FF-ESR], 517=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
            EDGE = "0, 2, 2",
            FF = "0, 1, 1",
            FF_ESR = "0, 1, 1")
    public void css__widows___orphans__8936() throws Exception {
        runTest("css: widows & orphans #8936");
    }

    /**
     * Test {516=[FF, FF-ESR], 518=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__can_t_get_css_for_disconnected_in_IE_9__see__10254_and__8388() throws Exception {
        runTest("css: can't get css for disconnected in IE<9, see #10254 and #8388");
    }

    /**
     * Test {517=[FF, FF-ESR], 519=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void css__can_t_get_background_position_in_IE_9__see__10796() throws Exception {
        runTest("css: can't get background-position in IE<9, see #10796");
    }

    /**
     * Test {518=[FF, FF-ESR], 520=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__percentage_properties_for_bottom_and_right_in_IE_9_should_not_be_incorrectly_transformed_to_pixels__see__11311() throws Exception {
        runTest("css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pixels, see #11311");
    }

    /**
     * Test {519=[FF, FF-ESR], 521=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__percentage_properties_for_left_and_top_should_be_transformed_to_pixels__see__9505() throws Exception {
        runTest("css: percentage properties for left and top should be transformed to pixels, see #9505");
    }

    /**
     * Test {520=[FF, FF-ESR], 522=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__Do_not_append_px___9548___12990_() throws Exception {
        runTest("css: Do not append px (#9548, #12990)");
    }

    /**
     * Test {521=[FF, FF-ESR], 523=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__css__width___and_css__height___should_respect_box_sizing__see__11004() throws Exception {
        runTest("css: css('width') and css('height') should respect box-sizing, see #11004");
    }

    /**
     * Test {522=[FF, FF-ESR], 524=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__css__width___should_work_correctly_before_document_ready___14084_() throws Exception {
        runTest("css: css('width') should work correctly before document ready (#14084)");
    }

    /**
     * Test {523=[FF, FF-ESR], 525=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__certain_css_values_of__normal__should_be_convertable_to_a_number__see__8627() throws Exception {
        runTest("css: certain css values of 'normal' should be convertable to a number, see #8627");
    }

    /**
     * Test {524=[FF, FF-ESR], 526=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void css__cssHooks___expand() throws Exception {
        runTest("css: cssHooks - expand");
    }

    /**
     * Test {525=[FF, FF-ESR], 527=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__css_opacity_consistency_across_browsers___12685_() throws Exception {
        runTest("css: css opacity consistency across browsers (#12685)");
    }

    /**
     * Test {526=[FF, FF-ESR], 528=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void css___visible__hidden_selectors() throws Exception {
        runTest("css: :visible/:hidden selectors");
    }

    /**
     * Test {527=[FF, FF-ESR], 529=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__Keep_the_last_style_if_the_new_one_isn_t_recognized_by_the_browser___14836_() throws Exception {
        runTest("css: Keep the last style if the new one isn't recognized by the browser (#14836)");
    }

    /**
     * Test {528=[FF, FF-ESR], 530=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__Reset_the_style_if_set_to_an_empty_string() throws Exception {
        runTest("css: Reset the style if set to an empty string");
    }

    /**
     * Test {529=[FF, FF-ESR], 531=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void css__Clearing_a_Cloned_Element_s_Style_Shouldn_t_Clear_the_Original_Element_s_Style___8908_() throws Exception {
        runTest("css: Clearing a Cloned Element's Style Shouldn't Clear the Original Element's Style (#8908)");
    }

    /**
     * Test {530=[FF, FF-ESR], 532=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__Make_sure_initialized_display_value_for_disconnected_nodes_is_correct___13310_() throws Exception {
        runTest("css: Make sure initialized display value for disconnected nodes is correct (#13310)");
    }

    /**
     * Test {531=[FF, FF-ESR], 533=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__show___after_hide___should_always_set_display_to_initial_value___14750_() throws Exception {
        runTest("css: show() after hide() should always set display to initial value (#14750)");
    }

    /**
     * Test {532=[FF, FF-ESR], 534=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__Don_t_append_px_to_CSS__order__value___14049_() throws Exception {
        runTest("css: Don't append px to CSS \"order\" value (#14049)");
    }

    /**
     * Test {533=[FF, FF-ESR], 535=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__Do_not_throw_on_frame_elements_from_css_method___15098_() throws Exception {
        runTest("css: Do not throw on frame elements from css method (#15098)");
    }

    /**
     * Test {534=[FF, FF-ESR], 536=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void serialize__jQuery_param__() throws Exception {
        runTest("serialize: jQuery.param()");
    }

    /**
     * Test {535=[FF, FF-ESR], 537=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void serialize__jQuery_param___Constructed_prop_values() throws Exception {
        runTest("serialize: jQuery.param() Constructed prop values");
    }

    /**
     * Test {536=[FF, FF-ESR], 538=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void serialize__serialize__() throws Exception {
        runTest("serialize: serialize()");
    }

    /**
     * Test {537=[FF, FF-ESR], 539=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks");
    }

    /**
     * Test {538=[FF, FF-ESR], 540=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks____url__options__syntax() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks - (url, options) syntax");
    }

    /**
     * Test {539=[FF, FF-ESR], 541=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (late binding)");
    }

    /**
     * Test {540=[FF, FF-ESR], 542=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__oncomplete_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (oncomplete binding)");
    }

    /**
     * Test {541=[FF, FF-ESR], 543=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____error_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - error callbacks");
    }

    /**
     * Test {542=[FF, FF-ESR], 544=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_____textStatus_and_errorThrown_values() throws Exception {
        runTest("ajax: jQuery.ajax() - textStatus and errorThrown values");
    }

    /**
     * Test {543=[FF, FF-ESR], 545=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____responseText_on_error() throws Exception {
        runTest("ajax: jQuery.ajax() - responseText on error");
    }

    /**
     * Test {544=[FF, FF-ESR], 546=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: jQuery.ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {545=[FF, FF-ESR], 547=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____headers() throws Exception {
        runTest("ajax: jQuery.ajax() - headers");
    }

    /**
     * Test {546=[FF, FF-ESR], 548=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____Accept_header() throws Exception {
        runTest("ajax: jQuery.ajax() - Accept header");
    }

    /**
     * Test {547=[FF, FF-ESR], 549=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____contentType() throws Exception {
        runTest("ajax: jQuery.ajax() - contentType");
    }

    /**
     * Test {548=[FF, FF-ESR], 550=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: jQuery.ajax() - protocol-less urls");
    }

    /**
     * Test {549=[FF, FF-ESR], 551=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____hash() throws Exception {
        runTest("ajax: jQuery.ajax() - hash");
    }

    /**
     * Test {550=[FF, FF-ESR], 552=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void ajax__jQuery_ajax_____cross_domain_detection() throws Exception {
        runTest("ajax: jQuery.ajax() - cross-domain detection");
    }

    /**
     * Test {551=[FF, FF-ESR], 553=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void ajax__jQuery_ajax_____abort() throws Exception {
        runTest("ajax: jQuery.ajax() - abort");
    }

    /**
     * Test {552=[FF, FF-ESR], 554=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void ajax__jQuery_ajax_____events_with_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events with context");
    }

    /**
     * Test {553=[FF, FF-ESR], 555=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____events_without_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events without context");
    }

    /**
     * Test {554=[FF, FF-ESR], 556=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___15118___jQuery_ajax_____function_without_jQuery_event() throws Exception {
        runTest("ajax: #15118 - jQuery.ajax() - function without jQuery.event");
    }

    /**
     * Test {555=[FF, FF-ESR], 557=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____context_modification() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification");
    }

    /**
     * Test {556=[FF, FF-ESR], 558=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification through ajaxSetup");
    }

    /**
     * Test {557=[FF, FF-ESR], 559=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____disabled_globals() throws Exception {
        runTest("ajax: jQuery.ajax() - disabled globals");
    }

    /**
     * Test {558=[FF, FF-ESR], 560=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {559=[FF, FF-ESR], 561=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {560=[FF, FF-ESR], 562=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - HEAD requests");
    }

    /**
     * Test {561=[FF, FF-ESR], 563=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend");
    }

    /**
     * Test {562=[FF, FF-ESR], 564=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend, cancel request manually");
    }

    /**
     * Test {563=[FF, FF-ESR], 565=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax() - dataType html");
    }

    /**
     * Test {564=[FF, FF-ESR], 566=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____synchronous_request() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request");
    }

    /**
     * Test {565=[FF, FF-ESR], 567=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request with callbacks");
    }

    /**
     * Test {566=[FF, FF-ESR], 568=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax____jQuery_get_Script_JSON_____jQuery_post____pass_through_request_object() throws Exception {
        runTest("ajax: jQuery.ajax(), jQuery.get[Script|JSON](), jQuery.post(), pass-through request object");
    }

    /**
     * Test {567=[FF, FF-ESR], 569=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void ajax__jQuery_ajax_____cache() throws Exception {
        runTest("ajax: jQuery.ajax() - cache");
    }

    /**
     * Test {568=[FF, FF-ESR], 570=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2, 2, 4")
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Same Domain");
    }

    /**
     * Test {569=[FF, FF-ESR], 571=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Same Domain");
    }

    /**
     * Test {570=[FF, FF-ESR], 572=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Same Domain");
    }

    /**
     * Test {571=[FF, FF-ESR], 573=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____JSONP___POST___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Same Domain");
    }

    /**
     * Test {572=[FF, FF-ESR], 574=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____JSONP___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Same Domain");
    }

    /**
     * Test {573=[FF, FF-ESR], 575=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2, 2, 4")
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Cross Domain");
    }

    /**
     * Test {574=[FF, FF-ESR], 576=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Cross Domain");
    }

    /**
     * Test {575=[FF, FF-ESR], 577=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Cross Domain");
    }

    /**
     * Test {576=[FF, FF-ESR], 578=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____JSONP___POST___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Cross Domain");
    }

    /**
     * Test {577=[FF, FF-ESR], 579=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____JSONP___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Cross Domain");
    }

    /**
     * Test {578=[FF, FF-ESR], 580=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote");
    }

    /**
     * Test {579=[FF, FF-ESR], 581=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____script__Remote_with_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with POST");
    }

    /**
     * Test {580=[FF, FF-ESR], 582=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote_with_scheme_less_URL() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with scheme-less URL");
    }

    /**
     * Test {581=[FF, FF-ESR], 583=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____malformed_JSON() throws Exception {
        runTest("ajax: jQuery.ajax() - malformed JSON");
    }

    /**
     * Test {582=[FF, FF-ESR], 584=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - script by content-type");
    }

    /**
     * Test {583=[FF, FF-ESR], 585=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____JSON_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type");
    }

    /**
     * Test {584=[FF, FF-ESR], 586=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax_____JSON_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type disabled with options");
    }

    /**
     * Test {585=[FF, FF-ESR], 587=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____simple_get() throws Exception {
        runTest("ajax: jQuery.ajax() - simple get");
    }

    /**
     * Test {586=[FF, FF-ESR], 588=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____simple_post() throws Exception {
        runTest("ajax: jQuery.ajax() - simple post");
    }

    /**
     * Test {587=[FF, FF-ESR], 589=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____data_option___empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - data option - empty bodies for non-GET requests");
    }

    /**
     * Test {588=[FF, FF-ESR], 590=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (cache)");
    }

    /**
     * Test {589=[FF, FF-ESR], 591=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (cache)");
    }

    /**
     * Test {590=[FF, FF-ESR], 592=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (no cache)");
    }

    /**
     * Test {591=[FF, FF-ESR], 593=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (no cache)");
    }

    /**
     * Test {592=[FF, FF-ESR], 594=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____failing_cross_domain__non_existing_() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain (non-existing)");
    }

    /**
     * Test {593=[FF, FF-ESR], 595=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____failing_cross_domain() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain");
    }

    /**
     * Test {594=[FF, FF-ESR], 596=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____atom_xml() throws Exception {
        runTest("ajax: jQuery.ajax() - atom+xml");
    }

    /**
     * Test {595=[FF, FF-ESR], 597=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____statusText() throws Exception {
        runTest("ajax: jQuery.ajax() - statusText");
    }

    /**
     * Test {596=[FF, FF-ESR], 598=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void ajax__jQuery_ajax_____statusCode() throws Exception {
        runTest("ajax: jQuery.ajax() - statusCode");
    }

    /**
     * Test {597=[FF, FF-ESR], 599=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax() - transitive conversions");
    }

    /**
     * Test {598=[FF, FF-ESR], 600=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax() - overrideMimeType");
    }

    /**
     * Test {599=[FF, FF-ESR], 601=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____empty_json_gets_to_error_callback_instead_of_success_callback_() throws Exception {
        runTest("ajax: jQuery.ajax() - empty json gets to error callback instead of success callback.");
    }

    /**
     * Test {600=[FF, FF-ESR], 602=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___2688___jQuery_ajax_____beforeSend__cancel_request() throws Exception {
        runTest("ajax: #2688 - jQuery.ajax() - beforeSend, cancel request");
    }

    /**
     * Test {601=[FF, FF-ESR], 603=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___2806___jQuery_ajax_____data_option___evaluate_function_values() throws Exception {
        runTest("ajax: #2806 - jQuery.ajax() - data option - evaluate function values");
    }

    /**
     * Test {602=[FF, FF-ESR], 604=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___7531___jQuery_ajax_____Location_object_as_url() throws Exception {
        runTest("ajax: #7531 - jQuery.ajax() - Location object as url");
    }

    /**
     * Test {603=[FF, FF-ESR], 605=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Same_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Same Domain");
    }

    /**
     * Test {604=[FF, FF-ESR], 606=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Cross_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Cross Domain");
    }

    /**
     * Test {605=[FF, FF-ESR], 607=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___8107___jQuery_ajax_____multiple_method_signatures_introduced_in_1_5() throws Exception {
        runTest("ajax: #8107 - jQuery.ajax() - multiple method signatures introduced in 1.5");
    }

    /**
     * Test {606=[FF, FF-ESR], 608=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Same_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Same Domain");
    }

    /**
     * Test {607=[FF, FF-ESR], 609=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Cross_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Cross Domain");
    }

    /**
     * Test {608=[FF, FF-ESR], 610=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___9887___jQuery_ajax_____Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: #9887 - jQuery.ajax() - Context with circular references (#9887)");
    }

    /**
     * Test {609=[FF, FF-ESR], 611=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___10093___jQuery_ajax_____falsy_url_as_argument() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url as argument");
    }

    /**
     * Test {610=[FF, FF-ESR], 612=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___10093___jQuery_ajax_____falsy_url_in_settings_object() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url in settings object");
    }

    /**
     * Test {611=[FF, FF-ESR], 613=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___11151___jQuery_ajax_____parse_error_body() throws Exception {
        runTest("ajax: #11151 - jQuery.ajax() - parse error body");
    }

    /**
     * Test {612=[FF, FF-ESR], 614=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___11426___jQuery_ajax_____loading_binary_data_shouldn_t_throw_an_exception_in_IE() throws Exception {
        runTest("ajax: #11426 - jQuery.ajax() - loading binary data shouldn't throw an exception in IE");
    }

    /**
     * Test {613=[FF, FF-ESR], 615=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___11743___jQuery_ajax_____script__throws_exception() throws Exception {
        runTest("ajax: #11743 - jQuery.ajax() - script, throws exception");
    }

    /**
     * Test {614=[FF, FF-ESR], 616=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___method_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - method set globally");
    }

    /**
     * Test {615=[FF, FF-ESR], 617=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___type_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - type set globally");
    }

    /**
     * Test {616=[FF, FF-ESR], 618=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___13276___jQuery_ajax_____compatibility_between_XML_documents_from_ajax_requests_and_parsed_string() throws Exception {
        runTest("ajax: #13276 - jQuery.ajax() - compatibility between XML documents from ajax requests and parsed string");
    }

    /**
     * Test {617=[FF, FF-ESR], 619=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___13292___jQuery_ajax_____converter_is_bypassed_for_204_requests() throws Exception {
        runTest("ajax: #13292 - jQuery.ajax() - converter is bypassed for 204 requests");
    }

    /**
     * Test {618=[FF, FF-ESR], 620=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___13388___jQuery_ajax_____responseXML() throws Exception {
        runTest("ajax: #13388 - jQuery.ajax() - responseXML");
    }

    /**
     * Test {619=[FF, FF-ESR], 621=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___13922___jQuery_ajax_____converter_is_bypassed_for_HEAD_requests() throws Exception {
        runTest("ajax: #13922 - jQuery.ajax() - converter is bypassed for HEAD requests");
    }

    /**
     * Test {620=[FF, FF-ESR], 622=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___13240___jQuery_ajax_____support_non_RFC2616_methods() throws Exception {
        runTest("ajax: #13240 - jQuery.ajax() - support non-RFC2616 methods");
    }

    /**
     * Test {621=[FF, FF-ESR], 623=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 0, 1",
            EDGE = "1, 0, 1",
            FF = "0, 1, 1",
            FF_ESR = "0, 1, 1")
    @HtmlUnitNYI(CHROME = "0, 1, 1",
            EDGE = "0, 1, 1")
    public void ajax___14379___jQuery_ajax___on_unload() throws Exception {
        runTest("ajax: #14379 - jQuery.ajax() on unload");
    }

    /**
     * Test {622=[FF, FF-ESR], 624=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxPrefilter_____abort() throws Exception {
        runTest("ajax: jQuery.ajaxPrefilter() - abort");
    }

    /**
     * Test {623=[FF, FF-ESR], 625=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxSetup__() throws Exception {
        runTest("ajax: jQuery.ajaxSetup()");
    }

    /**
     * Test {624=[FF, FF-ESR], 626=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @HtmlUnitNYI(CHROME = "2, 0, 2",
            EDGE = "2, 0, 2",
            FF = "2, 0, 2",
            FF_ESR = "2, 0, 2")
    public void ajax__jQuery_ajaxSetup___timeout__Number______with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) - with global timeout");
    }

    /**
     * Test {625=[FF, FF-ESR], 627=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxSetup___timeout__Number____with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) with localtimeout");
    }

    /**
     * Test {626=[FF, FF-ESR], 628=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___11264___jQuery_domManip_____no_side_effect_because_of_ajaxSetup_or_global_events() throws Exception {
        runTest("ajax: #11264 - jQuery.domManip() - no side effect because of ajaxSetup or global events");
    }

    /**
     * Test {627=[FF, FF-ESR], 629=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___11402___jQuery_domManip_____script_in_comments_are_properly_evaluated() throws Exception {
        runTest("ajax: #11402 - jQuery.domManip() - script in comments are properly evaluated");
    }

    /**
     * Test {628=[FF, FF-ESR], 630=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_get__String__Hash__Function_____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get( String, Hash, Function ) - parse xml and use text() on nodes");
    }

    /**
     * Test {629=[FF, FF-ESR], 631=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___8277___jQuery_get__String__Function_____data_in_ajaxSettings() throws Exception {
        runTest("ajax: #8277 - jQuery.get( String, Function ) - data in ajaxSettings");
    }

    /**
     * Test {630=[FF, FF-ESR], 632=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_getJSON__String__Hash__Function_____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Hash, Function ) - JSON array");
    }

    /**
     * Test {631=[FF, FF-ESR], 633=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object");
    }

    /**
     * Test {632=[FF, FF-ESR], 634=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_____Using_Native_JSON() throws Exception {
        runTest("ajax: jQuery.getJSON() - Using Native JSON");
    }

    /**
     * Test {633=[FF, FF-ESR], 635=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object with absolute url to local content");
    }

    /**
     * Test {634=[FF, FF-ESR], 636=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getScript__String__Function_____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - with callback");
    }

    /**
     * Test {635=[FF, FF-ESR], 637=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_getScript__String__Function_____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - no callback");
    }

    /**
     * Test {636=[FF, FF-ESR], 638=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___8082___jQuery_getScript__String__Function_____source_as_responseText() throws Exception {
        runTest("ajax: #8082 - jQuery.getScript( String, Function ) - source as responseText");
    }

    /**
     * Test {637=[FF, FF-ESR], 639=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__() throws Exception {
        runTest("ajax: jQuery.fn.load( String )");
    }

    /**
     * Test {638=[FF, FF-ESR], 640=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_fn_load_____404_error_callbacks() throws Exception {
        runTest("ajax: jQuery.fn.load() - 404 error callbacks");
    }

    /**
     * Test {639=[FF, FF-ESR], 641=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__null__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, null )");
    }

    /**
     * Test {640=[FF, FF-ESR], 642=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__undefined__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, undefined )");
    }

    /**
     * Test {641=[FF, FF-ESR], 643=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_fn_load__URL_SELECTOR__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR )");
    }

    /**
     * Test {642=[FF, FF-ESR], 644=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_fn_load__URL_SELECTOR_with_spaces__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR with spaces )");
    }

    /**
     * Test {643=[FF, FF-ESR], 645=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__Function_____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - simple: inject text into DOM");
    }

    /**
     * Test {644=[FF, FF-ESR], 646=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    @HtmlUnitNYI(CHROME = "1, 8, 9",
            EDGE = "1, 8, 9",
            FF = "1, 8, 9",
            FF_ESR = "1, 8, 9")
    public void ajax__jQuery_fn_load__String__Function_____check_scripts() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check scripts");
    }

    /**
     * Test {645=[FF, FF-ESR], 647=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_fn_load__String__Function_____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check file with only a script tag");
    }

    /**
     * Test {646=[FF, FF-ESR], 648=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__Function_____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - dataFilter in ajaxSettings");
    }

    /**
     * Test {647=[FF, FF-ESR], 649=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__Object__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Object, Function )");
    }

    /**
     * Test {648=[FF, FF-ESR], 650=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__String__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, String, Function )");
    }

    /**
     * Test {649=[FF, FF-ESR], 651=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_fn_load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: jQuery.fn.load() - callbacks get the correct parameters");
    }

    /**
     * Test {650=[FF, FF-ESR], 652=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___2046___jQuery_fn_load__String__Function___with_ajaxSetup_on_dataType_json() throws Exception {
        runTest("ajax: #2046 - jQuery.fn.load( String, Function ) with ajaxSetup on dataType json");
    }

    /**
     * Test {651=[FF, FF-ESR], 653=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___10524___jQuery_fn_load_____data_specified_in_ajaxSettings_is_merged_in() throws Exception {
        runTest("ajax: #10524 - jQuery.fn.load() - data specified in ajaxSettings is merged in");
    }

    /**
     * Test {652=[FF, FF-ESR], 654=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_post_____data() throws Exception {
        runTest("ajax: jQuery.post() - data");
    }

    /**
     * Test {653=[FF, FF-ESR], 655=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_post__String__Hash__Function_____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post( String, Hash, Function ) - simple with xml");
    }

    /**
     * Test {654=[FF, FF-ESR], 656=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_active() throws Exception {
        runTest("ajax: jQuery.active");
    }

    /**
     * Test {655=[FF, FF-ESR], 657=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__sanity_check() throws Exception {
        runTest("effects: sanity check");
    }

    /**
     * Test {656=[FF, FF-ESR], 658=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__show___basic() throws Exception {
        runTest("effects: show() basic");
    }

    /**
     * Test {657=[FF, FF-ESR], 659=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void effects__show__() throws Exception {
        runTest("effects: show()");
    }

    /**
     * Test {658=[FF, FF-ESR], 660=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__show_Number____other_displays() throws Exception {
        runTest("effects: show(Number) - other displays");
    }

    /**
     * Test {659=[FF, FF-ESR], 661=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__Persist_correct_display_value() throws Exception {
        runTest("effects: Persist correct display value");
    }

    /**
     * Test {660=[FF, FF-ESR], 662=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_Hash__Object__Function_() throws Exception {
        runTest("effects: animate(Hash, Object, Function)");
    }

    /**
     * Test {661=[FF, FF-ESR], 663=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void effects__animate_relative_values() throws Exception {
        runTest("effects: animate relative values");
    }

    /**
     * Test {662=[FF, FF-ESR], 664=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_height() throws Exception {
        runTest("effects: animate negative height");
    }

    /**
     * Test {663=[FF, FF-ESR], 665=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin() throws Exception {
        runTest("effects: animate negative margin");
    }

    /**
     * Test {664=[FF, FF-ESR], 666=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin_with_px() throws Exception {
        runTest("effects: animate negative margin with px");
    }

    /**
     * Test {665=[FF, FF-ESR], 667=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @HtmlUnitNYI(CHROME = "1, 0, 1",
            EDGE = "1, 0, 1",
            FF = "1, 0, 1",
            FF_ESR = "1, 0, 1")
    public void effects__animate_negative_padding() throws Exception {
        runTest("effects: animate negative padding");
    }

    /**
     * Test {666=[FF, FF-ESR], 668=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__animate_block_as_inline_width_height() throws Exception {
        runTest("effects: animate block as inline width/height");
    }

    /**
     * Test {667=[FF, FF-ESR], 669=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__animate_native_inline_width_height() throws Exception {
        runTest("effects: animate native inline width/height");
    }

    /**
     * Test {668=[FF, FF-ESR], 670=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__animate_block_width_height() throws Exception {
        runTest("effects: animate block width/height");
    }

    /**
     * Test {669=[FF, FF-ESR], 671=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_table_width_height() throws Exception {
        runTest("effects: animate table width/height");
    }

    /**
     * Test {670=[FF, FF-ESR], 672=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @HtmlUnitNYI(CHROME = "2, 1, 3",
            EDGE = "2, 1, 3",
            FF = "2, 1, 3",
            FF_ESR = "2, 1, 3")
    public void effects__animate_table_row_width_height() throws Exception {
        runTest("effects: animate table-row width/height");
    }

    /**
     * Test {671=[FF, FF-ESR], 673=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @HtmlUnitNYI(CHROME = "2, 1, 3",
            EDGE = "2, 1, 3",
            FF = "2, 1, 3",
            FF_ESR = "2, 1, 3")
    public void effects__animate_table_cell_width_height() throws Exception {
        runTest("effects: animate table-cell width/height");
    }

    /**
     * Test {672=[FF, FF-ESR], 674=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_percentage____on_width_height() throws Exception {
        runTest("effects: animate percentage(%) on width/height");
    }

    /**
     * Test {673=[FF, FF-ESR], 675=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_resets_overflow_x_and_overflow_y_when_finished() throws Exception {
        runTest("effects: animate resets overflow-x and overflow-y when finished");
    }

    /**
     * Test {674=[FF, FF-ESR], 676=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__false__() throws Exception {
        runTest("effects: animate option { queue: false }");
    }

    /**
     * Test {675=[FF, FF-ESR], 677=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__true__() throws Exception {
        runTest("effects: animate option { queue: true }");
    }

    /**
     * Test {676=[FF, FF-ESR], 678=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_option___queue___name___() throws Exception {
        runTest("effects: animate option { queue: 'name' }");
    }

    /**
     * Test {677=[FF, FF-ESR], 679=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_with_no_properties() throws Exception {
        runTest("effects: animate with no properties");
    }

    /**
     * Test {678=[FF, FF-ESR], 680=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_duration_0() throws Exception {
        runTest("effects: animate duration 0");
    }

    /**
     * Test {679=[FF, FF-ESR], 681=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_hyphenated_properties() throws Exception {
        runTest("effects: animate hyphenated properties");
    }

    /**
     * Test {680=[FF, FF-ESR], 682=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_non_element() throws Exception {
        runTest("effects: animate non-element");
    }

    /**
     * Test {681=[FF, FF-ESR], 683=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop__() throws Exception {
        runTest("effects: stop()");
    }

    /**
     * Test {682=[FF, FF-ESR], 684=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__stop_____several_in_queue() throws Exception {
        runTest("effects: stop() - several in queue");
    }

    /**
     * Test {683=[FF, FF-ESR], 685=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop_clearQueue_() throws Exception {
        runTest("effects: stop(clearQueue)");
    }

    /**
     * Test {684=[FF, FF-ESR], 686=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__stop_clearQueue__gotoEnd_() throws Exception {
        runTest("effects: stop(clearQueue, gotoEnd)");
    }

    /**
     * Test {685=[FF, FF-ESR], 687=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop__queue_______________Stop_single_queues() throws Exception {
        runTest("effects: stop( queue, ..., ... ) - Stop single queues");
    }

    /**
     * Test {686=[FF, FF-ESR], 688=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__toggle__() throws Exception {
        runTest("effects: toggle()");
    }

    /**
     * Test {687=[FF, FF-ESR], 689=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1, 6, 7")
    @HtmlUnitNYI(CHROME = "0, 7, 7",
            EDGE = "0, 7, 7",
            FF = "0, 7, 7",
            FF_ESR = "0, 7, 7")
    public void effects__jQuery_fx_prototype_cur______1_8_Back_Compat() throws Exception {
        runTest("effects: jQuery.fx.prototype.cur() - <1.8 Back Compat");
    }

    /**
     * Test {688=[FF, FF-ESR], 690=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__Overflow_and_Display() throws Exception {
        runTest("effects: Overflow and Display");
    }

    /**
     * Test {689=[FF, FF-ESR], 691=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_0() throws Exception {
        runTest("effects: CSS Auto to 0");
    }

    /**
     * Test {690=[FF, FF-ESR], 692=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_50() throws Exception {
        runTest("effects: CSS Auto to 50");
    }

    /**
     * Test {691=[FF, FF-ESR], 693=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_100() throws Exception {
        runTest("effects: CSS Auto to 100");
    }

    /**
     * Test {692=[FF, FF-ESR], 694=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_Auto_to_show() throws Exception {
        runTest("effects: CSS Auto to show");
    }

    /**
     * Test {693=[FF, FF-ESR], 695=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_Auto_to_hide() throws Exception {
        runTest("effects: CSS Auto to hide");
    }

    /**
     * Test {694=[FF, FF-ESR], 696=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_0() throws Exception {
        runTest("effects: JS Auto to 0");
    }

    /**
     * Test {695=[FF, FF-ESR], 697=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_50() throws Exception {
        runTest("effects: JS Auto to 50");
    }

    /**
     * Test {696=[FF, FF-ESR], 698=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_100() throws Exception {
        runTest("effects: JS Auto to 100");
    }

    /**
     * Test {697=[FF, FF-ESR], 699=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_Auto_to_show() throws Exception {
        runTest("effects: JS Auto to show");
    }

    /**
     * Test {698=[FF, FF-ESR], 700=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_Auto_to_hide() throws Exception {
        runTest("effects: JS Auto to hide");
    }

    /**
     * Test {699=[FF, FF-ESR], 701=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_0() throws Exception {
        runTest("effects: CSS 100 to 0");
    }

    /**
     * Test {700=[FF, FF-ESR], 702=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_50() throws Exception {
        runTest("effects: CSS 100 to 50");
    }

    /**
     * Test {701=[FF, FF-ESR], 703=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_100() throws Exception {
        runTest("effects: CSS 100 to 100");
    }

    /**
     * Test {702=[FF, FF-ESR], 704=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_100_to_show() throws Exception {
        runTest("effects: CSS 100 to show");
    }

    /**
     * Test {703=[FF, FF-ESR], 705=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_100_to_hide() throws Exception {
        runTest("effects: CSS 100 to hide");
    }

    /**
     * Test {704=[FF, FF-ESR], 706=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_0() throws Exception {
        runTest("effects: JS 100 to 0");
    }

    /**
     * Test {705=[FF, FF-ESR], 707=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_50() throws Exception {
        runTest("effects: JS 100 to 50");
    }

    /**
     * Test {706=[FF, FF-ESR], 708=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_100() throws Exception {
        runTest("effects: JS 100 to 100");
    }

    /**
     * Test {707=[FF, FF-ESR], 709=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_100_to_show() throws Exception {
        runTest("effects: JS 100 to show");
    }

    /**
     * Test {708=[FF, FF-ESR], 710=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_100_to_hide() throws Exception {
        runTest("effects: JS 100 to hide");
    }

    /**
     * Test {709=[FF, FF-ESR], 711=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_0() throws Exception {
        runTest("effects: CSS 50 to 0");
    }

    /**
     * Test {710=[FF, FF-ESR], 712=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_50() throws Exception {
        runTest("effects: CSS 50 to 50");
    }

    /**
     * Test {711=[FF, FF-ESR], 713=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_100() throws Exception {
        runTest("effects: CSS 50 to 100");
    }

    /**
     * Test {712=[FF, FF-ESR], 714=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_50_to_show() throws Exception {
        runTest("effects: CSS 50 to show");
    }

    /**
     * Test {713=[FF, FF-ESR], 715=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_50_to_hide() throws Exception {
        runTest("effects: CSS 50 to hide");
    }

    /**
     * Test {714=[FF, FF-ESR], 716=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_0() throws Exception {
        runTest("effects: JS 50 to 0");
    }

    /**
     * Test {715=[FF, FF-ESR], 717=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_50() throws Exception {
        runTest("effects: JS 50 to 50");
    }

    /**
     * Test {716=[FF, FF-ESR], 718=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_100() throws Exception {
        runTest("effects: JS 50 to 100");
    }

    /**
     * Test {717=[FF, FF-ESR], 719=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_50_to_show() throws Exception {
        runTest("effects: JS 50 to show");
    }

    /**
     * Test {718=[FF, FF-ESR], 720=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_50_to_hide() throws Exception {
        runTest("effects: JS 50 to hide");
    }

    /**
     * Test {719=[FF, FF-ESR], 721=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_0() throws Exception {
        runTest("effects: CSS 0 to 0");
    }

    /**
     * Test {720=[FF, FF-ESR], 722=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_50() throws Exception {
        runTest("effects: CSS 0 to 50");
    }

    /**
     * Test {721=[FF, FF-ESR], 723=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_100() throws Exception {
        runTest("effects: CSS 0 to 100");
    }

    /**
     * Test {722=[FF, FF-ESR], 724=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_0_to_show() throws Exception {
        runTest("effects: CSS 0 to show");
    }

    /**
     * Test {723=[FF, FF-ESR], 725=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_0_to_hide() throws Exception {
        runTest("effects: CSS 0 to hide");
    }

    /**
     * Test {724=[FF, FF-ESR], 726=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_0() throws Exception {
        runTest("effects: JS 0 to 0");
    }

    /**
     * Test {725=[FF, FF-ESR], 727=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_50() throws Exception {
        runTest("effects: JS 0 to 50");
    }

    /**
     * Test {726=[FF, FF-ESR], 728=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_100() throws Exception {
        runTest("effects: JS 0 to 100");
    }

    /**
     * Test {727=[FF, FF-ESR], 729=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_0_to_show() throws Exception {
        runTest("effects: JS 0 to show");
    }

    /**
     * Test {728=[FF, FF-ESR], 730=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_0_to_hide() throws Exception {
        runTest("effects: JS 0 to hide");
    }

    /**
     * Test {729=[FF, FF-ESR], 731=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void effects__Effects_chaining() throws Exception {
        runTest("effects: Effects chaining");
    }

    /**
     * Test {730=[FF, FF-ESR], 732=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_show__fast___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("effects: jQuery.show('fast') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {731=[FF, FF-ESR], 733=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void effects__interrupt_toggle() throws Exception {
        runTest("effects: interrupt toggle");
    }

    /**
     * Test {732=[FF, FF-ESR], 734=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_with_per_property_easing() throws Exception {
        runTest("effects: animate with per-property easing");
    }

    /**
     * Test {733=[FF, FF-ESR], 735=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_with_CSS_shorthand_properties() throws Exception {
        runTest("effects: animate with CSS shorthand properties");
    }

    /**
     * Test {734=[FF, FF-ESR], 736=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_hidden_elements__with_animation__bug__7141_() throws Exception {
        runTest("effects: hide hidden elements, with animation (bug #7141)");
    }

    /**
     * Test {735=[FF, FF-ESR], 737=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_unit_less_properties___4966_() throws Exception {
        runTest("effects: animate unit-less properties (#4966)");
    }

    /**
     * Test {736=[FF, FF-ESR], 738=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__animate_properties_missing_px_w__opacity_as_last___9074_() throws Exception {
        runTest("effects: animate properties missing px w/ opacity as last (#9074)");
    }

    /**
     * Test {737=[FF, FF-ESR], 739=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__callbacks_should_fire_in_correct_order___9100_() throws Exception {
        runTest("effects: callbacks should fire in correct order (#9100)");
    }

    /**
     * Test {738=[FF, FF-ESR], 740=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__callbacks_that_throw_exceptions_will_be_removed___5684_() throws Exception {
        runTest("effects: callbacks that throw exceptions will be removed (#5684)");
    }

    /**
     * Test {739=[FF, FF-ESR], 741=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_will_scale_margin_properties_individually() throws Exception {
        runTest("effects: animate will scale margin properties individually");
    }

    /**
     * Test {740=[FF, FF-ESR], 742=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("effects: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {741=[FF, FF-ESR], 743=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    @HtmlUnitNYI(CHROME = "8, 4, 12",
            EDGE = "8, 4, 12",
            FF = "8, 4, 12",
            FF_ESR = "8, 4, 12")
    public void effects__line_height_animates_correctly___13855_() throws Exception {
        runTest("effects: line-height animates correctly (#13855)");
    }

    /**
     * Test {742=[FF, FF-ESR], 744=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_Animation__object__props__opts__() throws Exception {
        runTest("effects: jQuery.Animation( object, props, opts )");
    }

    /**
     * Test {743=[FF, FF-ESR], 745=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animate_Option__step__function__percent__tween__() throws Exception {
        runTest("effects: Animate Option: step: function( percent, tween )");
    }

    /**
     * Test {744=[FF, FF-ESR], 746=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__Animate_callbacks_have_correct_context() throws Exception {
        runTest("effects: Animate callbacks have correct context");
    }

    /**
     * Test {745=[FF, FF-ESR], 747=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__User_supplied_callback_called_after_show_when_fx_off___8892_() throws Exception {
        runTest("effects: User supplied callback called after show when fx off (#8892)");
    }

    /**
     * Test {746=[FF, FF-ESR], 748=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void effects__animate_should_set_display_for_disconnected_nodes() throws Exception {
        runTest("effects: animate should set display for disconnected nodes");
    }

    /**
     * Test {747=[FF, FF-ESR], 749=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animation_callback_should_not_show_animated_element_as__animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as :animated (#7157)");
    }

    /**
     * Test {748=[FF, FF-ESR], 750=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Initial_step_callback_should_show_element_as__animated___14623_() throws Exception {
        runTest("effects: Initial step callback should show element as :animated (#14623)");
    }

    /**
     * Test {749=[FF, FF-ESR], 751=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_called_on_element_within_hidden_parent_should_set_display_to_none___10045_() throws Exception {
        runTest("effects: hide called on element within hidden parent should set display to none (#10045)");
    }

    /**
     * Test {750=[FF, FF-ESR], 752=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__hide__fadeOut_and_slideUp_called_on_element_width_height_and_width___0_should_set_display_to_none() throws Exception {
        runTest("effects: hide, fadeOut and slideUp called on element width height and width = 0 should set display to none");
    }

    /**
     * Test {751=[FF, FF-ESR], 753=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__hide_should_not_leave_hidden_inline_elements_visible___14848_() throws Exception {
        runTest("effects: hide should not leave hidden inline elements visible (#14848)");
    }

    /**
     * Test {752=[FF, FF-ESR], 754=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void effects__Handle_queue_false_promises() throws Exception {
        runTest("effects: Handle queue:false promises");
    }

    /**
     * Test {753=[FF, FF-ESR], 755=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__multiple_unqueued_and_promise() throws Exception {
        runTest("effects: multiple unqueued and promise");
    }

    /**
     * Test {754=[FF, FF-ESR], 756=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @HtmlUnitNYI(CHROME = "1, 0, 1",
            EDGE = "1, 0, 1",
            FF = "1, 0, 1",
            FF_ESR = "1, 0, 1")
    public void effects__animate_does_not_change_start_value_for_non_px_animation___7109_() throws Exception {
        runTest("effects: animate does not change start value for non-px animation (#7109)");
    }

    /**
     * Test {755=[FF, FF-ESR], 757=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__non_px_animation_handles_non_numeric_start___11971_() throws Exception {
        runTest("effects: non-px animation handles non-numeric start (#11971)");
    }

    /**
     * Test {756=[FF, FF-ESR], 758=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__Animation_callbacks___11797_() throws Exception {
        runTest("effects: Animation callbacks (#11797)");
    }

    /**
     * Test {757=[FF, FF-ESR], 759=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__Animate_properly_sets_overflow_hidden_when_animating_width_height___12117_() throws Exception {
        runTest("effects: Animate properly sets overflow hidden when animating width/height (#12117)");
    }

    /**
     * Test {758=[FF, FF-ESR], 760=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__Each_tick_of_the_timer_loop_uses_a_fresh_time___12837_() throws Exception {
        runTest("effects: Each tick of the timer loop uses a fresh time (#12837)");
    }

    /**
     * Test {759=[FF, FF-ESR], 761=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animations_with_0_duration_don_t_ease___12273_() throws Exception {
        runTest("effects: Animations with 0 duration don't ease (#12273)");
    }

    /**
     * Test {760=[FF, FF-ESR], 762=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__toggle_state_tests__toggle___8685_() throws Exception {
        runTest("effects: toggle state tests: toggle (#8685)");
    }

    /**
     * Test {761=[FF, FF-ESR], 763=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__toggle_state_tests__slideToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: slideToggle (#8685)");
    }

    /**
     * Test {762=[FF, FF-ESR], 764=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__toggle_state_tests__fadeToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: fadeToggle (#8685)");
    }

    /**
     * Test {763=[FF, FF-ESR], 765=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__jQuery_fx_start___jQuery_fx_stop_hook_points() throws Exception {
        runTest("effects: jQuery.fx.start & jQuery.fx.stop hook points");
    }

    /**
     * Test {764=[FF, FF-ESR], 766=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects___finish___completes_all_queued_animations() throws Exception {
        runTest("effects: .finish() completes all queued animations");
    }

    /**
     * Test {765=[FF, FF-ESR], 767=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void effects___finish__false_____unqueued_animations() throws Exception {
        runTest("effects: .finish( false ) - unqueued animations");
    }

    /**
     * Test {766=[FF, FF-ESR], 768=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects___finish___custom______custom_queue_animations() throws Exception {
        runTest("effects: .finish( \"custom\" ) - custom queue animations");
    }

    /**
     * Test {767=[FF, FF-ESR], 769=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects___finish___calls_finish_of_custom_queue_functions() throws Exception {
        runTest("effects: .finish() calls finish of custom queue functions");
    }

    /**
     * Test {768=[FF, FF-ESR], 770=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects___finish___is_applied_correctly_when_multiple_elements_were_animated___13937_() throws Exception {
        runTest("effects: .finish() is applied correctly when multiple elements were animated (#13937)");
    }

    /**
     * Test {769=[FF, FF-ESR], 771=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__slideDown___after_stop_____13483_() throws Exception {
        runTest("effects: slideDown() after stop() (#13483)");
    }

    /**
     * Test {770=[FF, FF-ESR], 772=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__Respect_display_value_on_inline_elements___14824_() throws Exception {
        runTest("effects: Respect display value on inline elements (#14824)");
    }

    /**
     * Test {771=[FF, FF-ESR], 773=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__empty_set() throws Exception {
        runTest("offset: empty set");
    }

    /**
     * Test {772=[FF, FF-ESR], 774=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__object_without_getBoundingClientRect() throws Exception {
        runTest("offset: object without getBoundingClientRect");
    }

    /**
     * Test {773=[FF, FF-ESR], 775=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__disconnected_node() throws Exception {
        runTest("offset: disconnected node");
    }

    /**
     * Test {775=[FF, FF-ESR], 777=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 178, 178")
    @HtmlUnitNYI(CHROME = "0, 4, 4",
            EDGE = "0, 4, 4",
            FF = "0, 4, 4",
            FF_ESR = "0, 4, 4")
    public void offset__absolute() throws Exception {
        runTest("offset: absolute");
    }

    /**
     * Test {776=[FF, FF-ESR], 778=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 60, 60")
    @HtmlUnitNYI(CHROME = "4, 56, 60",
            EDGE = "4, 56, 60",
            FF = "4, 56, 60",
            FF_ESR = "4, 56, 60")
    public void offset__relative() throws Exception {
        runTest("offset: relative");
    }

    /**
     * Test {777=[FF, FF-ESR], 779=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 80, 80")
    public void offset__static() throws Exception {
        runTest("offset: static");
    }

    /**
     * Test {778=[FF, FF-ESR], 780=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 34, 34")
    public void offset__fixed() throws Exception {
        runTest("offset: fixed");
    }

    /**
     * Test {779=[FF, FF-ESR], 781=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @HtmlUnitNYI(CHROME = "2, 2, 4",
            EDGE = "2, 2, 4",
            FF = "2, 2, 4",
            FF_ESR = "2, 2, 4")
    public void offset__table() throws Exception {
        runTest("offset: table");
    }

    /**
     * Test {780=[FF, FF-ESR], 782=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6, 18, 24",
            EDGE = "6, 18, 24",
            FF = "2, 22, 24",
            FF_ESR = "2, 22, 24")
    @HtmlUnitNYI(CHROME = "3, 21, 24",
            EDGE = "3, 21, 24",
            FF = "3, 21, 24",
            FF_ESR = "3, 21, 24")
    public void offset__scroll() throws Exception {
        runTest("offset: scroll");
    }

    /**
     * Test {781=[FF, FF-ESR], 783=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @HtmlUnitNYI(CHROME = "4, 0, 4",
            EDGE = "4, 0, 4",
            FF = "4, 0, 4",
            FF_ESR = "4, 0, 4")
    public void offset__body() throws Exception {
        runTest("offset: body");
    }

    /**
     * Test {782=[FF, FF-ESR], 784=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void offset__chaining() throws Exception {
        runTest("offset: chaining");
    }

    /**
     * Test {783=[FF, FF-ESR], 785=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void offset__offsetParent() throws Exception {
        runTest("offset: offsetParent");
    }

    /**
     * Test {784=[FF, FF-ESR], 786=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1, 1, 2")
    @HtmlUnitNYI(CHROME = "0, 2, 2",
            EDGE = "0, 2, 2",
            FF = "0, 2, 2",
            FF_ESR = "0, 2, 2")
    public void offset__fractions__see__7730_and__7885_() throws Exception {
        runTest("offset: fractions (see #7730 and #7885)");
    }

    /**
     * Test {785=[FF, FF-ESR], 787=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width__() throws Exception {
        runTest("dimensions: width()");
    }

    /**
     * Test {786=[FF, FF-ESR], 788=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width_Function_() throws Exception {
        runTest("dimensions: width(Function)");
    }

    /**
     * Test {787=[FF, FF-ESR], 789=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__width_Function_args__() throws Exception {
        runTest("dimensions: width(Function(args))");
    }

    /**
     * Test {788=[FF, FF-ESR], 790=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height__() throws Exception {
        runTest("dimensions: height()");
    }

    /**
     * Test {789=[FF, FF-ESR], 791=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height_Function_() throws Exception {
        runTest("dimensions: height(Function)");
    }

    /**
     * Test {790=[FF, FF-ESR], 792=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__height_Function_args__() throws Exception {
        runTest("dimensions: height(Function(args))");
    }

    /**
     * Test {791=[FF, FF-ESR], 793=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerWidth__() throws Exception {
        runTest("dimensions: innerWidth()");
    }

    /**
     * Test {792=[FF, FF-ESR], 794=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerHeight__() throws Exception {
        runTest("dimensions: innerHeight()");
    }

    /**
     * Test {793=[FF, FF-ESR], 795=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerWidth__() throws Exception {
        runTest("dimensions: outerWidth()");
    }

    /**
     * Test {794=[FF, FF-ESR], 796=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__9441__9300() throws Exception {
        runTest("dimensions: child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #9441 #9300");
    }

    /**
     * Test {795=[FF, FF-ESR], 797=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void dimensions__getting_dimensions_shouldn_t_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldn't modify runtimeStyle see #9233");
    }

    /**
     * Test {796=[FF, FF-ESR], 798=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__table_dimensions() throws Exception {
        runTest("dimensions: table dimensions");
    }

    /**
     * Test {797=[FF, FF-ESR], 799=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__box_sizing_border_box_child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__10413() throws Exception {
        runTest("dimensions: box-sizing:border-box child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #10413");
    }

    /**
     * Test {798=[FF, FF-ESR], 800=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerHeight__() throws Exception {
        runTest("dimensions: outerHeight()");
    }

    /**
     * Test {799=[FF, FF-ESR], 801=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void dimensions__passing_undefined_is_a_setter__5571() throws Exception {
        runTest("dimensions: passing undefined is a setter #5571");
    }

    /**
     * Test {800=[FF, FF-ESR], 802=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void dimensions__getters_on_non_elements_should_return_null() throws Exception {
        runTest("dimensions: getters on non elements should return null");
    }

    /**
     * Test {801=[FF, FF-ESR], 803=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void dimensions__setters_with_and_without_box_sizing_border_box() throws Exception {
        runTest("dimensions: setters with and without box-sizing:border-box");
    }

    /**
     * Test {802=[FF, FF-ESR], 804=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void dimensions__window_vs__small_document() throws Exception {
        runTest("dimensions: window vs. small document");
    }

    /**
     * Test {803=[FF, FF-ESR], 805=[CHROME, EDGE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @HtmlUnitNYI(CHROME = "2, 0, 2",
            EDGE = "2, 0, 2",
            FF = "2, 0, 2",
            FF_ESR = "2, 0, 2")
    public void dimensions__window_vs__large_document() throws Exception {
        runTest("dimensions: window vs. large document");
    }
}
