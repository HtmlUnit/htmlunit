/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF38;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for compatibility with web server loading of
 * version 1.11.3 of the <a href="http://jquery.com/">jQuery</a> JavaScript library.
 *
 * All test method inside this class are generated. Please have a look
 * at {@link com.gargoylesoftware.htmlunit.source.JQueryExtractor}.
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
     * Test {1=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_isReady() throws Exception {
        runTest("event: jQuery.isReady");
    }

    /**
     * Test {2=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void event__jQuery_ready() throws Exception {
        runTest("event: jQuery ready");
    }

    /**
     * Test {3=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__Unit_Testing_Environment() throws Exception {
        runTest("core: Unit Testing Environment");
    }

    /**
     * Test {4=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__Basic_requirements() throws Exception {
        runTest("core: Basic requirements");
    }

    /**
     * Test {5=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__jQuery__() throws Exception {
        runTest("core: jQuery()");
    }

    /**
     * Test {6=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery_selector__context_() throws Exception {
        runTest("core: jQuery(selector, context)");
    }

    /**
     * Test {7=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__selector_state() throws Exception {
        runTest("core: selector state");
    }

    /**
     * Test {8=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__globalEval() throws Exception {
        runTest("core: globalEval");
    }

    /**
     * Test {9=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__noConflict() throws Exception {
        runTest("core: noConflict");
    }

    /**
     * Test {10=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void core__trim() throws Exception {
        runTest("core: trim");
    }

    /**
     * Test {11=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__type() throws Exception {
        runTest("core: type");
    }

    /**
     * Test {12=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void core__isPlainObject() throws Exception {
        runTest("core: isPlainObject");
    }

    /**
     * Test {13=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__isFunction() throws Exception {
        runTest("core: isFunction");
    }

    /**
     * Test {14=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 38, 38")
    public void core__isNumeric() throws Exception {
        runTest("core: isNumeric");
    }

    /**
     * Test {15=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__isXMLDoc___HTML() throws Exception {
        runTest("core: isXMLDoc - HTML");
    }

    /**
     * Test {16=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__XSS_via_location_hash() throws Exception {
        runTest("core: XSS via location.hash");
    }

    /**
     * Test {17=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__isXMLDoc___XML() throws Exception {
        runTest("core: isXMLDoc - XML");
    }

    /**
     * Test {18=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__isWindow() throws Exception {
        runTest("core: isWindow");
    }

    /**
     * Test {19=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery__html__() throws Exception {
        runTest("core: jQuery('html')");
    }

    /**
     * Test {20=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery__massive_html__7990__() throws Exception {
        runTest("core: jQuery('massive html #7990')");
    }

    /**
     * Test {21=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__jQuery__html___context_() throws Exception {
        runTest("core: jQuery('html', context)");
    }

    /**
     * Test {22=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_selector__xml__text_str____loaded_via_xml_document() throws Exception {
        runTest("core: jQuery(selector, xml).text(str) - loaded via xml document");
    }

    /**
     * Test {23=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__end__() throws Exception {
        runTest("core: end()");
    }

    /**
     * Test {24=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__length() throws Exception {
        runTest("core: length");
    }

    /**
     * Test {25=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__get__() throws Exception {
        runTest("core: get()");
    }

    /**
     * Test {26=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__toArray__() throws Exception {
        runTest("core: toArray()");
    }

    /**
     * Test {27=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__inArray__() throws Exception {
        runTest("core: inArray()");
    }

    /**
     * Test {28=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get_Number_() throws Exception {
        runTest("core: get(Number)");
    }

    /**
     * Test {29=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get__Number_() throws Exception {
        runTest("core: get(-Number)");
    }

    /**
     * Test {30=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__each_Function_() throws Exception {
        runTest("core: each(Function)");
    }

    /**
     * Test {31=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__slice__() throws Exception {
        runTest("core: slice()");
    }

    /**
     * Test {32=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__first___last__() throws Exception {
        runTest("core: first()/last()");
    }

    /**
     * Test {33=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__map__() throws Exception {
        runTest("core: map()");
    }

    /**
     * Test {34=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void core__jQuery_map() throws Exception {
        runTest("core: jQuery.map");
    }

    /**
     * Test {35=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void core__jQuery_merge__() throws Exception {
        runTest("core: jQuery.merge()");
    }

    /**
     * Test {36=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_grep__() throws Exception {
        runTest("core: jQuery.grep()");
    }

    /**
     * Test {37=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__jQuery_extend_Object__Object_() throws Exception {
        runTest("core: jQuery.extend(Object, Object)");
    }

    /**
     * Test {38=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void core__jQuery_each_Object_Function_() throws Exception {
        runTest("core: jQuery.each(Object,Function)");
    }

    /**
     * Test {39=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__JIT_compilation_does_not_interfere_with_length_retrieval__gh_2145_() throws Exception {
        runTest("core: JIT compilation does not interfere with length retrieval (gh-2145)");
    }

    /**
     * Test {40=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void core__jQuery_makeArray() throws Exception {
        runTest("core: jQuery.makeArray");
    }

    /**
     * Test {41=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery_inArray() throws Exception {
        runTest("core: jQuery.inArray");
    }

    /**
     * Test {42=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_isEmptyObject() throws Exception {
        runTest("core: jQuery.isEmptyObject");
    }

    /**
     * Test {43=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void core__jQuery_proxy() throws Exception {
        runTest("core: jQuery.proxy");
    }

    /**
     * Test {44=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void core__jQuery_parseHTML() throws Exception {
        runTest("core: jQuery.parseHTML");
    }

    /**
     * Test {45=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void core__jQuery_parseJSON() throws Exception {
        runTest("core: jQuery.parseJSON");
    }

    /**
     * Test {46=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseXML() throws Exception {
        runTest("core: jQuery.parseXML");
    }

    /**
     * Test {47=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_camelCase__() throws Exception {
        runTest("core: jQuery.camelCase()");
    }

    /**
     * Test {48=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__Conditional_compilation_compatibility___13274_() throws Exception {
        runTest("core: Conditional compilation compatibility (#13274)");
    }

    /**
     * Test {49=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void core__document_ready_when_jQuery_loaded_asynchronously___13655_() throws Exception {
        runTest("core: document ready when jQuery loaded asynchronously (#13655)");
    }

    /**
     * Test {50=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__Tolerating_alias_masked_DOM_properties___14074_() throws Exception {
        runTest("core: Tolerating alias-masked DOM properties (#14074)");
    }

    /**
     * Test {51=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__Don_t_call_window_onready___14802_() throws Exception {
        runTest("core: Don't call window.onready (#14802)");
    }

    /**
     * Test {52=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - no filter");
    }

    /**
     * Test {53=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks__________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - no filter");
    }

    /**
     * Test {54=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - filter");
    }

    /**
     * Test {55=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks__________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - filter");
    }

    /**
     * Test {56=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - no filter");
    }

    /**
     * Test {57=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - no filter");
    }

    /**
     * Test {58=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - filter");
    }

    /**
     * Test {59=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - filter");
    }

    /**
     * Test {60=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - no filter");
    }

    /**
     * Test {61=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - no filter");
    }

    /**
     * Test {62=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - filter");
    }

    /**
     * Test {63=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - filter");
    }

    /**
     * Test {64=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - no filter");
    }

    /**
     * Test {65=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - no filter");
    }

    /**
     * Test {66=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - filter");
    }

    /**
     * Test {67=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - filter");
    }

    /**
     * Test {68=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - no filter");
    }

    /**
     * Test {69=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {70=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - filter");
    }

    /**
     * Test {71=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {72=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - no filter");
    }

    /**
     * Test {73=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - no filter");
    }

    /**
     * Test {74=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - filter");
    }

    /**
     * Test {75=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - filter");
    }

    /**
     * Test {76=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - no filter");
    }

    /**
     * Test {77=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {78=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - filter");
    }

    /**
     * Test {79=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - filter");
    }

    /**
     * Test {80=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - no filter");
    }

    /**
     * Test {81=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {82=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - filter");
    }

    /**
     * Test {83=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {84=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - no filter");
    }

    /**
     * Test {85=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {86=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - filter");
    }

    /**
     * Test {87=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - filter");
    }

    /**
     * Test {88=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - no filter");
    }

    /**
     * Test {89=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {90=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - filter");
    }

    /**
     * Test {91=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {92=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - no filter");
    }

    /**
     * Test {93=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {94=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - filter");
    }

    /**
     * Test {95=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {96=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks__options_____options_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks( options ) - options are copied");
    }

    /**
     * Test {97=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_fireWith___arguments_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks.fireWith - arguments are copied");
    }

    /**
     * Test {98=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_remove___should_remove_all_instances() throws Exception {
        runTest("callbacks: jQuery.Callbacks.remove - should remove all instances");
    }

    /**
     * Test {99=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void callbacks__jQuery_Callbacks_has() throws Exception {
        runTest("callbacks: jQuery.Callbacks.has");
    }

    /**
     * Test {100=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_____adding_a_string_doesn_t_cause_a_stack_overflow() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow");
    }

    /**
     * Test {101=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred() throws Exception {
        runTest("deferred: jQuery.Deferred");
    }

    /**
     * Test {102=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred___new_operator() throws Exception {
        runTest("deferred: jQuery.Deferred - new operator");
    }

    /**
     * Test {103=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void deferred__jQuery_Deferred___chainability() throws Exception {
        runTest("deferred: jQuery.Deferred - chainability");
    }

    /**
     * Test {104=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (done)");
    }

    /**
     * Test {105=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (fail)");
    }

    /**
     * Test {106=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___filtering__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (progress)");
    }

    /**
     * Test {107=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (done)");
    }

    /**
     * Test {108=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (fail)");
    }

    /**
     * Test {109=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (progress)");
    }

    /**
     * Test {110=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void deferred__jQuery_Deferred_then___context() throws Exception {
        runTest("deferred: jQuery.Deferred.then - context");
    }

    /**
     * Test {111=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 37, 37")
    public void deferred__jQuery_when() throws Exception {
        runTest("deferred: jQuery.when");
    }

    /**
     * Test {112=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 119, 119")
    public void deferred__jQuery_when___joined() throws Exception {
        runTest("deferred: jQuery.when - joined");
    }

    /**
     * Test {113=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__zoom_of_doom___13089_() throws Exception {
        runTest("support: zoom of doom (#13089)");
    }

    /**
     * Test {114=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void support__body_background_is_not_lost_if_set_prior_to_loading_jQuery___9239_() throws Exception {
        runTest("support: body background is not lost if set prior to loading jQuery (#9239)");
    }

    /**
     * Test {115=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__A_background_on_the_testElement_does_not_cause_IE8_to_crash___9823_() throws Exception {
        runTest("support: A background on the testElement does not cause IE8 to crash (#9823)");
    }

    /**
     * Test {116=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__box_sizing_does_not_affect_jQuery_support_shrinkWrapBlocks() throws Exception {
        runTest("support: box-sizing does not affect jQuery.support.shrinkWrapBlocks");
    }

    /**
     * Test {117=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "1, 1, 2",
        FF38 = "0, 2, 2",
        IE = "0, 2, 2")
    @NotYetImplemented({ FF38, IE })
    public void support__Check_CSP__https___developer_mozilla_org_en_US_docs_Security_CSP__restrictions() throws Exception {
        runTest("support: Check CSP (https://developer.mozilla.org/en-US/docs/Security/CSP) restrictions");
    }

    /**
     * Test {118=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 34, 34")
    @NotYetImplemented
    public void support__Verify_that_the_support_tests_resolve_as_expected_per_browser() throws Exception {
        runTest("support: Verify that the support tests resolve as expected per browser");
    }

    /**
     * Test {119=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__expando() throws Exception {
        runTest("data: expando");
    }

    /**
     * Test {120=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void data__jQuery_data_div_() throws Exception {
        runTest("data: jQuery.data(div)");
    }

    /**
     * Test {121=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void data__jQuery_data____() throws Exception {
        runTest("data: jQuery.data({})");
    }

    /**
     * Test {122=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void data__jQuery_data_window_() throws Exception {
        runTest("data: jQuery.data(window)");
    }

    /**
     * Test {123=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 25, 25")
    public void data__jQuery_data_document_() throws Exception {
        runTest("data: jQuery.data(document)");
    }

    /**
     * Test {124=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data__Expando_cleanup() throws Exception {
        runTest("data: Expando cleanup");
    }

    /**
     * Test {125=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__Data_is_not_being_set_on_comment_and_text_nodes() throws Exception {
        runTest("data: Data is not being set on comment and text nodes");
    }

    /**
     * Test {126=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_acceptData() throws Exception {
        runTest("data: jQuery.acceptData");
    }

    /**
     * Test {127=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__jQuery___data_______undefined___14101_() throws Exception {
        runTest("data: jQuery().data() === undefined (#14101)");
    }

    /**
     * Test {128=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void data___data__() throws Exception {
        runTest("data: .data()");
    }

    /**
     * Test {129=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void data__jQuery_Element__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(Element).data(String, Object).data(String)");
    }

    /**
     * Test {130=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void data__jQuery_plain_Object__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(plain Object).data(String, Object).data(String)");
    }

    /**
     * Test {131=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 43, 43")
    public void data__data___attributes() throws Exception {
        runTest("data: data-* attributes");
    }

    /**
     * Test {132=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data___data_Object_() throws Exception {
        runTest("data: .data(Object)");
    }

    /**
     * Test {133=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_removeData() throws Exception {
        runTest("data: jQuery.removeData");
    }

    /**
     * Test {134=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void data___removeData__() throws Exception {
        runTest("data: .removeData()");
    }

    /**
     * Test {135=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_serialization___8108_() throws Exception {
        runTest("data: JSON serialization (#8108)");
    }

    /**
     * Test {136=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: jQuery.data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {137=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__jQuery_data_should_not_miss_data_with_preset_hyphenated_property_names() throws Exception {
        runTest("data: jQuery.data should not miss data with preset hyphenated property names");
    }

    /**
     * Test {138=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void data__jQuery_data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {139=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void data__jQuery_data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: jQuery.data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {140=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data___removeData_supports_removal_of_hyphenated_properties_via_array___12786_() throws Exception {
        runTest("data: .removeData supports removal of hyphenated properties via array (#12786)");
    }

    /**
     * Test {141=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__Triggering_the_removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: Triggering the removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {142=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__Only_check_element_attributes_once_when_calling__data______8909() throws Exception {
        runTest("data: Only check element attributes once when calling .data() - #8909");
    }

    /**
     * Test {143=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_data__attributes_can_have_newlines() throws Exception {
        runTest("data: JSON data- attributes can have newlines");
    }

    /**
     * Test {144=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__enumerate_data_attrs_on_body___14894_() throws Exception {
        runTest("data: enumerate data attrs on body (#14894)");
    }

    /**
     * Test {145=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void queue__queue___with_other_types() throws Exception {
        runTest("queue: queue() with other types");
    }

    /**
     * Test {146=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_name__passes_in_the_next_item_in_the_queue_as_a_parameter() throws Exception {
        runTest("queue: queue(name) passes in the next item in the queue as a parameter");
    }

    /**
     * Test {147=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__queue___passes_in_the_next_item_in_the_queue_as_a_parameter_to_fx_queues() throws Exception {
        runTest("queue: queue() passes in the next item in the queue as a parameter to fx queues");
    }

    /**
     * Test {148=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__callbacks_keep_their_place_in_the_queue() throws Exception {
        runTest("queue: callbacks keep their place in the queue");
    }

    /**
     * Test {149=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__delay__() throws Exception {
        runTest("queue: delay()");
    }

    /**
     * Test {150=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__clearQueue_name__clears_the_queue() throws Exception {
        runTest("queue: clearQueue(name) clears the queue");
    }

    /**
     * Test {151=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void queue__clearQueue___clears_the_fx_queue() throws Exception {
        runTest("queue: clearQueue() clears the fx queue");
    }

    /**
     * Test {152=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__fn_promise_____called_when_fx_queue_is_empty() throws Exception {
        runTest("queue: fn.promise() - called when fx queue is empty");
    }

    /**
     * Test {153=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__fn_promise___queue______called_whenever_last_queue_function_is_dequeued() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued");
    }

    /**
     * Test {154=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__fn_promise___queue______waits_for_animation_to_complete_before_resolving() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving");
    }

    /**
     * Test {155=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue___promise_obj_() throws Exception {
        runTest("queue: .promise(obj)");
    }

    /**
     * Test {156=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__delay___can_be_stopped() throws Exception {
        runTest("queue: delay() can be stopped");
    }

    /**
     * Test {157=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_stop_hooks() throws Exception {
        runTest("queue: queue stop hooks");
    }

    /**
     * Test {158=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__jQuery_propFix_integrity_test() throws Exception {
        runTest("attributes: jQuery.propFix integrity test");
    }

    /**
     * Test {159=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 50, 50")
    public void attributes__attr_String_() throws Exception {
        runTest("attributes: attr(String)");
    }

    /**
     * Test {160=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__attr_String__on_cloned_elements___9646() throws Exception {
        runTest("attributes: attr(String) on cloned elements, #9646");
    }

    /**
     * Test {161=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_String__in_XML_Files() throws Exception {
        runTest("attributes: attr(String) in XML Files");
    }

    /**
     * Test {162=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Function_() throws Exception {
        runTest("attributes: attr(String, Function)");
    }

    /**
     * Test {163=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_Hash_() throws Exception {
        runTest("attributes: attr(Hash)");
    }

    /**
     * Test {164=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 71, 71")
    public void attributes__attr_String__Object_() throws Exception {
        runTest("attributes: attr(String, Object)");
    }

    /**
     * Test {165=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__attr___extending_the_boolean_attrHandle() throws Exception {
        runTest("attributes: attr - extending the boolean attrHandle");
    }

    /**
     * Test {166=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_document() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML document");
    }

    /**
     * Test {167=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_fragment() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML fragment");
    }

    /**
     * Test {168=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__attr__tabindex__() throws Exception {
        runTest("attributes: attr('tabindex')");
    }

    /**
     * Test {169=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr__tabindex___value_() throws Exception {
        runTest("attributes: attr('tabindex', value)");
    }

    /**
     * Test {170=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void attributes__removeAttr_String_() throws Exception {
        runTest("attributes: removeAttr(String)");
    }

    /**
     * Test {171=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeAttr_String__in_XML() throws Exception {
        runTest("attributes: removeAttr(String) in XML");
    }

    /**
     * Test {172=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeAttr_Multi_String__variable_space_width_() throws Exception {
        runTest("attributes: removeAttr(Multi String, variable space width)");
    }

    /**
     * Test {173=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__prop_String__Object_() throws Exception {
        runTest("attributes: prop(String, Object)");
    }

    /**
     * Test {174=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void attributes__prop_String__Object__on_null_undefined() throws Exception {
        runTest("attributes: prop(String, Object) on null/undefined");
    }

    /**
     * Test {175=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void attributes__prop__tabindex__() throws Exception {
        runTest("attributes: prop('tabindex')");
    }

    /**
     * Test {176=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void attributes__prop__tabindex___value_() throws Exception {
        runTest("attributes: prop('tabindex', value)");
    }

    /**
     * Test {177=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void attributes__removeProp_String_() throws Exception {
        runTest("attributes: removeProp(String)");
    }

    /**
     * Test {178=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__val___after_modification() throws Exception {
        runTest("attributes: val() after modification");
    }

    /**
     * Test {179=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void attributes__val__() throws Exception {
        runTest("attributes: val()");
    }

    /**
     * Test {180=[CHROME, FF38, FF45, IE]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__val___with_non_matching_values_on_dropdown_list() throws Exception {
        runTest("attributes: val() with non-matching values on dropdown list");
    }

    /**
     * Test {181=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "0, 4, 4",
            IE = {})
    public void attributes__val___respects_numbers_without_exception__Bug__9319_() throws Exception {
        runTest("attributes: val() respects numbers without exception (Bug #9319)");
    }

    /**
     * Test {181=[IE], 182=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__val_String_Number_() throws Exception {
        runTest("attributes: val(String/Number)");
    }

    /**
     * Test {182=[IE], 183=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__val_Function_() throws Exception {
        runTest("attributes: val(Function)");
    }

    /**
     * Test {183=[IE], 184=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__val_Array_of_Numbers___Bug__7123_() throws Exception {
        runTest("attributes: val(Array of Numbers) (Bug #7123)");
    }

    /**
     * Test {184=[IE], 185=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void attributes__val_Function__with_incoming_value() throws Exception {
        runTest("attributes: val(Function) with incoming value");
    }

    /**
     * Test {185=[IE], 186=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__val_select__after_form_reset____Bug__2551_() throws Exception {
        runTest("attributes: val(select) after form.reset() (Bug #2551)");
    }

    /**
     * Test {186=[IE], 187=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_String_() throws Exception {
        runTest("attributes: addClass(String)");
    }

    /**
     * Test {187=[IE], 188=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_Function_() throws Exception {
        runTest("attributes: addClass(Function)");
    }

    /**
     * Test {188=[IE], 189=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 52, 52")
    public void attributes__addClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: addClass(Function) with incoming value");
    }

    /**
     * Test {189=[IE], 190=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeClass_String____simple() throws Exception {
        runTest("attributes: removeClass(String) - simple");
    }

    /**
     * Test {190=[IE], 191=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeClass_Function____simple() throws Exception {
        runTest("attributes: removeClass(Function) - simple");
    }

    /**
     * Test {191=[IE], 192=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 52, 52")
    public void attributes__removeClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: removeClass(Function) with incoming value");
    }

    /**
     * Test {192=[IE], 193=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__removeClass___removes_duplicates() throws Exception {
        runTest("attributes: removeClass() removes duplicates");
    }

    /**
     * Test {193=[IE], 194=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__removeClass_undefined__is_a_no_op() throws Exception {
        runTest("attributes: removeClass(undefined) is a no-op");
    }

    /**
     * Test {194=[IE], 195=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_String_boolean_undefined___boolean__() throws Exception {
        runTest("attributes: toggleClass(String|boolean|undefined[, boolean])");
    }

    /**
     * Test {195=[IE], 196=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_Function___boolean__() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean])");
    }

    /**
     * Test {196=[IE], 197=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void attributes__toggleClass_Function___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean]) with incoming value");
    }

    /**
     * Test {197=[IE], 198=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__addClass__removeClass__hasClass() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass");
    }

    /**
     * Test {198=[IE], 199=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void attributes__addClass__removeClass__hasClass_on_many_elements() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass on many elements");
    }

    /**
     * Test {199=[IE], 200=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__contents___hasClass___returns_correct_values() throws Exception {
        runTest("attributes: contents().hasClass() returns correct values");
    }

    /**
     * Test {200=[IE], 201=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__hasClass_correctly_interprets_non_space_separators___13835_() throws Exception {
        runTest("attributes: hasClass correctly interprets non-space separators (#13835)");
    }

    /**
     * Test {201=[IE], 202=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__coords_returns_correct_values_in_IE6_IE7__see__10828() throws Exception {
        runTest("attributes: coords returns correct values in IE6/IE7, see #10828");
    }

    /**
     * Test {202=[IE], 203=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__should_not_throw_at___option__val_____14686_() throws Exception {
        runTest("attributes: should not throw at $(option).val() (#14686)");
    }

    /**
     * Test {203=[IE], 204=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__Insignificant_white_space_returned_for___option__val_____14858_() throws Exception {
        runTest("attributes: Insignificant white space returned for $(option).val() (#14858)");
    }

    /**
     * Test {204=[IE], 205=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__null_or_undefined_handler() throws Exception {
        runTest("event: null or undefined handler");
    }

    /**
     * Test {205=[IE], 206=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__on___with_non_null_defined_data() throws Exception {
        runTest("event: on() with non-null,defined data");
    }

    /**
     * Test {206=[IE], 207=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Handler_changes_and__trigger___order() throws Exception {
        runTest("event: Handler changes and .trigger() order");
    }

    /**
     * Test {207=[IE], 208=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__on____with_data() throws Exception {
        runTest("event: on(), with data");
    }

    /**
     * Test {208=[IE], 209=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__click____with_data() throws Exception {
        runTest("event: click(), with data");
    }

    /**
     * Test {209=[IE], 210=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__on____with_data__trigger_with_data() throws Exception {
        runTest("event: on(), with data, trigger with data");
    }

    /**
     * Test {210=[IE], 211=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__on____multiple_events_at_once() throws Exception {
        runTest("event: on(), multiple events at once");
    }

    /**
     * Test {211=[IE], 212=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on____five_events_at_once() throws Exception {
        runTest("event: on(), five events at once");
    }

    /**
     * Test {212=[IE], 213=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__on____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: on(), multiple events at once and namespaces");
    }

    /**
     * Test {213=[IE], 214=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void event__on____namespace_with_special_add() throws Exception {
        runTest("event: on(), namespace with special add");
    }

    /**
     * Test {214=[IE], 215=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on____no_data() throws Exception {
        runTest("event: on(), no data");
    }

    /**
     * Test {215=[IE], 216=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__on_one_off_Object_() throws Exception {
        runTest("event: on/one/off(Object)");
    }

    /**
     * Test {216=[IE], 217=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__on_off_Object___on_off_Object__String_() throws Exception {
        runTest("event: on/off(Object), on/off(Object, String)");
    }

    /**
     * Test {217=[IE], 218=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__on_immediate_propagation() throws Exception {
        runTest("event: on immediate propagation");
    }

    /**
     * Test {218=[IE], 219=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void event__on_bubbling__isDefaultPrevented__stopImmediatePropagation() throws Exception {
        runTest("event: on bubbling, isDefaultPrevented, stopImmediatePropagation");
    }

    /**
     * Test {219=[IE], 220=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on____iframes() throws Exception {
        runTest("event: on(), iframes");
    }

    /**
     * Test {220=[IE], 221=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__on____trigger_change_on_select() throws Exception {
        runTest("event: on(), trigger change on select");
    }

    /**
     * Test {221=[IE], 222=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__on____namespaced_events__cloned_events() throws Exception {
        runTest("event: on(), namespaced events, cloned events");
    }

    /**
     * Test {222=[IE], 223=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__on____multi_namespaced_events() throws Exception {
        runTest("event: on(), multi-namespaced events");
    }

    /**
     * Test {223=[IE], 224=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__namespace_only_event_binding_is_a_no_op() throws Exception {
        runTest("event: namespace-only event binding is a no-op");
    }

    /**
     * Test {224=[IE], 225=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__on____with_same_function() throws Exception {
        runTest("event: on(), with same function");
    }

    /**
     * Test {225=[IE], 226=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on____make_sure_order_is_maintained() throws Exception {
        runTest("event: on(), make sure order is maintained");
    }

    /**
     * Test {226=[IE], 227=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__on____with_different_this_object() throws Exception {
        runTest("event: on(), with different this object");
    }

    /**
     * Test {227=[IE], 228=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__on_name__false___off_name__false_() throws Exception {
        runTest("event: on(name, false), off(name, false)");
    }

    /**
     * Test {228=[IE], 229=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__on_name__selector__false___off_name__selector__false_() throws Exception {
        runTest("event: on(name, selector, false), off(name, selector, false)");
    }

    /**
     * Test {229=[IE], 230=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__on___trigger___off___on_plain_object() throws Exception {
        runTest("event: on()/trigger()/off() on plain object");
    }

    /**
     * Test {230=[IE], 231=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__off_type_() throws Exception {
        runTest("event: off(type)");
    }

    /**
     * Test {231=[IE], 232=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__off_eventObject_() throws Exception {
        runTest("event: off(eventObject)");
    }

    /**
     * Test {232=[IE], 233=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__hover___mouseenter_mouseleave() throws Exception {
        runTest("event: hover() mouseenter mouseleave");
    }

    /**
     * Test {233=[IE], 234=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__mouseover_triggers_mouseenter() throws Exception {
        runTest("event: mouseover triggers mouseenter");
    }

    /**
     * Test {234=[IE], 235=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__pointerover_triggers_pointerenter() throws Exception {
        runTest("event: pointerover triggers pointerenter");
    }

    /**
     * Test {235=[IE], 236=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__withinElement_implemented_with_jQuery_contains__() throws Exception {
        runTest("event: withinElement implemented with jQuery.contains()");
    }

    /**
     * Test {236=[IE], 237=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__mouseenter__mouseleave_don_t_catch_exceptions() throws Exception {
        runTest("event: mouseenter, mouseleave don't catch exceptions");
    }

    /**
     * Test {237=[IE], 238=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__trigger___shortcuts() throws Exception {
        runTest("event: trigger() shortcuts");
    }

    /**
     * Test {238=[IE], 239=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__trigger___bubbling() throws Exception {
        runTest("event: trigger() bubbling");
    }

    /**
     * Test {239=[IE], 240=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__trigger_type___data____fn__() throws Exception {
        runTest("event: trigger(type, [data], [fn])");
    }

    /**
     * Test {240=[IE], 241=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__submit_event_bubbles_on_copied_forms___11649_() throws Exception {
        runTest("event: submit event bubbles on copied forms (#11649)");
    }

    /**
     * Test {241=[IE], 242=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__change_event_bubbles_on_copied_forms___11796_() throws Exception {
        runTest("event: change event bubbles on copied forms (#11796)");
    }

    /**
     * Test {242=[IE], 243=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void event__trigger_eventObject___data____fn__() throws Exception {
        runTest("event: trigger(eventObject, [data], [fn])");
    }

    /**
     * Test {243=[IE], 244=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event___trigger___bubbling_on_disconnected_elements___10489_() throws Exception {
        runTest("event: .trigger() bubbling on disconnected elements (#10489)");
    }

    /**
     * Test {244=[IE], 245=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___trigger___doesn_t_bubble_load_event___10717_() throws Exception {
        runTest("event: .trigger() doesn't bubble load event (#10717)");
    }

    /**
     * Test {245=[IE], 246=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__Delegated_events_in_SVG___10791___13180_() throws Exception {
        runTest("event: Delegated events in SVG (#10791; #13180)");
    }

    /**
     * Test {246=[IE], 247=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__Delegated_events_in_forms___10844___11145___8165___11382___11764_() throws Exception {
        runTest("event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764)");
    }

    /**
     * Test {247=[IE], 248=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Submit_event_can_be_stopped___11049_() throws Exception {
        runTest("event: Submit event can be stopped (#11049)");
    }

    /**
     * Test {248=[IE], 249=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @NotYetImplemented
    public void event__on_beforeunload_() throws Exception {
        runTest("event: on(beforeunload)");
    }

    /**
     * Test {249=[IE], 250=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__jQuery_Event__type__props__() throws Exception {
        runTest("event: jQuery.Event( type, props )");
    }

    /**
     * Test {250=[IE], 251=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void event__jQuery_Event_properties() throws Exception {
        runTest("event: jQuery.Event properties");
    }

    /**
     * Test {251=[IE], 252=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 65, 65")
    public void event___on____off__() throws Exception {
        runTest("event: .on()/.off()");
    }

    /**
     * Test {252=[IE], 253=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_off_using_dispatched_jQuery_Event() throws Exception {
        runTest("event: jQuery.off using dispatched jQuery.Event");
    }

    /**
     * Test {253=[IE], 254=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegated_event_with_delegateTarget_relative_selector() throws Exception {
        runTest("event: delegated event with delegateTarget-relative selector");
    }

    /**
     * Test {254=[IE], 255=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__delegated_event_with_selector_matching_Object_prototype_property___13203_() throws Exception {
        runTest("event: delegated event with selector matching Object.prototype property (#13203)");
    }

    /**
     * Test {255=[IE], 256=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__delegated_event_with_intermediate_DOM_manipulation___13208_() throws Exception {
        runTest("event: delegated event with intermediate DOM manipulation (#13208)");
    }

    /**
     * Test {256=[IE], 257=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__stopPropagation___stops_directly_bound_events_on_delegated_target() throws Exception {
        runTest("event: stopPropagation() stops directly-bound events on delegated target");
    }

    /**
     * Test {257=[IE], 258=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__off_all_bound_delegated_events() throws Exception {
        runTest("event: off all bound delegated events");
    }

    /**
     * Test {258=[IE], 259=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__on_with_multiple_delegated_events() throws Exception {
        runTest("event: on with multiple delegated events");
    }

    /**
     * Test {259=[IE], 260=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__delegated_on_with_change() throws Exception {
        runTest("event: delegated on with change");
    }

    /**
     * Test {260=[IE], 261=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__delegated_on_with_submit() throws Exception {
        runTest("event: delegated on with submit");
    }

    /**
     * Test {261=[IE], 262=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__delegated_off___with_only_namespaces() throws Exception {
        runTest("event: delegated off() with only namespaces");
    }

    /**
     * Test {262=[IE], 263=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Non_DOM_element_events() throws Exception {
        runTest("event: Non DOM element events");
    }

    /**
     * Test {263=[IE], 264=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__inline_handler_returning_false_stops_default() throws Exception {
        runTest("event: inline handler returning false stops default");
    }

    /**
     * Test {264=[IE], 265=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__window_resize() throws Exception {
        runTest("event: window resize");
    }

    /**
     * Test {265=[IE], 266=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__focusin_bubbles() throws Exception {
        runTest("event: focusin bubbles");
    }

    /**
     * Test {266=[IE], 267=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__custom_events_with_colons___3533___8272_() throws Exception {
        runTest("event: custom events with colons (#3533, #8272)");
    }

    /**
     * Test {267=[IE], 268=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void event___on_and__off() throws Exception {
        runTest("event: .on and .off");
    }

    /**
     * Test {268=[IE], 269=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__special_on_name_mapping() throws Exception {
        runTest("event: special on name mapping");
    }

    /**
     * Test {269=[IE], 270=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event___on_and__off__selective_mixed_removal___10705_() throws Exception {
        runTest("event: .on and .off, selective mixed removal (#10705)");
    }

    /**
     * Test {270=[IE], 271=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___on__event_map__null_selector__data____11130() throws Exception {
        runTest("event: .on( event-map, null-selector, data ) #11130");
    }

    /**
     * Test {271=[IE], 272=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__clone___delegated_events___11076_() throws Exception {
        runTest("event: clone() delegated events (#11076)");
    }

    /**
     * Test {272=[IE], 273=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void event__checkbox_state___3827_() throws Exception {
        runTest("event: checkbox state (#3827)");
    }

    /**
     * Test {273=[IE], 274=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__hover_event_no_longer_special_since_1_9() throws Exception {
        runTest("event: hover event no longer special since 1.9");
    }

    /**
     * Test {274=[IE], 275=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__fixHooks_extensions() throws Exception {
        runTest("event: fixHooks extensions");
    }

    /**
     * Test {275=[IE], 276=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__focusin_using_non_element_targets() throws Exception {
        runTest("event: focusin using non-element targets");
    }

    /**
     * Test {276=[IE], 277=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__focusin_from_an_iframe() throws Exception {
        runTest("event: focusin from an iframe");
    }

    /**
     * Test {277=[IE], 278=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_promise() throws Exception {
        runTest("event: jQuery.ready promise");
    }

    /**
     * Test {278=[IE], 279=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Focusing_iframe_element() throws Exception {
        runTest("event: Focusing iframe element");
    }

    /**
     * Test {279=[IE], 280=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__triggerHandler_onbeforeunload_() throws Exception {
        runTest("event: triggerHandler(onbeforeunload)");
    }

    /**
     * Test {280=[IE], 281=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_synchronous_load_with_long_loading_subresources() throws Exception {
        runTest("event: jQuery.ready synchronous load with long loading subresources");
    }

    /**
     * Test {281=[IE], 282=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__change_handler_should_be_detached_from_element() throws Exception {
        runTest("event: change handler should be detached from element");
    }

    /**
     * Test {282=[IE], 283=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__trigger_click_on_checkbox__fires_change_event() throws Exception {
        runTest("event: trigger click on checkbox, fires change event");
    }

    /**
     * Test {283=[IE], 284=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__Namespace_preserved_when_passed_an_Event___12739_() throws Exception {
        runTest("event: Namespace preserved when passed an Event (#12739)");
    }

    /**
     * Test {284=[IE], 285=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__make_sure_events_cloned_correctly() throws Exception {
        runTest("event: make sure events cloned correctly");
    }

    /**
     * Test {285=[IE], 286=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__String_prototype_namespace_does_not_cause_trigger___to_throw___13360_() throws Exception {
        runTest("event: String.prototype.namespace does not cause trigger() to throw (#13360)");
    }

    /**
     * Test {286=[IE], 287=[CHROME, FF38, FF45]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Inline_event_result_is_returned___13993_() throws Exception {
        runTest("event: Inline event result is returned (#13993)");
    }

    /**
     * Test {287=[IE], 288=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "0, 2, 2",
        FF = {})
    public void event__Check_order_of_focusin_focusout_events() throws Exception {
        runTest("event: Check order of focusin/focusout events");
    }

    /**
     * Test {288=[IE], 289=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "0, 5, 5",
            FF = {})
    public void event__focus_blur_order___12868_() throws Exception {
        runTest("event: focus-blur order (#12868)");
    }

    /**
     * Test {288=[FF38, FF45], 289=[IE], 290=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void selector__element___jQuery_only() throws Exception {
        runTest("selector: element - jQuery only");
    }

    /**
     * Test {289=[FF38, FF45], 290=[IE], 291=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void selector__id() throws Exception {
        runTest("selector: id");
    }

    /**
     * Test {290=[FF38, FF45], 291=[IE], 292=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector__class___jQuery_only() throws Exception {
        runTest("selector: class - jQuery only");
    }

    /**
     * Test {291=[FF38, FF45], 292=[IE], 293=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void selector__name() throws Exception {
        runTest("selector: name");
    }

    /**
     * Test {292=[FF38, FF45], 293=[IE], 294=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector__selectors_with_comma() throws Exception {
        runTest("selector: selectors with comma");
    }

    /**
     * Test {293=[FF38, FF45], 294=[IE], 295=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void selector__child_and_adjacent() throws Exception {
        runTest("selector: child and adjacent");
    }

    /**
     * Test {294=[FF38, FF45], 295=[IE], 296=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 54, 54")
    @NotYetImplemented
    public void selector__attributes() throws Exception {
        runTest("selector: attributes");
    }

    /**
     * Test {295=[FF38, FF45], 296=[IE], 297=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void selector__disconnected_nodes() throws Exception {
        runTest("selector: disconnected nodes");
    }

    /**
     * Test {296=[FF38, FF45], 297=[IE], 298=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void selector__disconnected_nodes___jQuery_only() throws Exception {
        runTest("selector: disconnected nodes - jQuery only");
    }

    /**
     * Test {297=[FF38, FF45], 298=[IE], 299=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    public void selector__attributes___jQuery_attr() throws Exception {
        runTest("selector: attributes - jQuery.attr");
    }

    /**
     * Test {298=[FF38, FF45], 299=[IE], 300=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void selector__jQuery_contains() throws Exception {
        runTest("selector: jQuery.contains");
    }

    /**
     * Test {299=[FF38, FF45], 300=[IE], 301=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    @NotYetImplemented
    public void selector__jQuery_unique() throws Exception {
        runTest("selector: jQuery.unique");
    }

    /**
     * Test {300=[FF38, FF45], 301=[IE], 302=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector__Sizzle_cache_collides_with_multiple_Sizzles_on_a_page() throws Exception {
        runTest("selector: Sizzle cache collides with multiple Sizzles on a page");
    }

    /**
     * Test {301=[FF38, FF45], 302=[IE], 303=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void selector__Iframe_dispatch_should_not_affect_jQuery___13936_() throws Exception {
        runTest("selector: Iframe dispatch should not affect jQuery (#13936)");
    }

    /**
     * Test {302=[FF38, FF45], 303=[IE], 304=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__find_String_() throws Exception {
        runTest("traversing: find(String)");
    }

    /**
     * Test {303=[FF38, FF45], 304=[IE], 305=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__find_String__under_non_elements() throws Exception {
        runTest("traversing: find(String) under non-elements");
    }

    /**
     * Test {304=[FF38, FF45], 305=[IE], 306=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__find_leading_combinator_() throws Exception {
        runTest("traversing: find(leading combinator)");
    }

    /**
     * Test {305=[FF38, FF45], 306=[IE], 307=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void traversing__find_node_jQuery_object_() throws Exception {
        runTest("traversing: find(node|jQuery object)");
    }

    /**
     * Test {306=[FF38, FF45], 307=[IE], 308=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void traversing__is_String_undefined_() throws Exception {
        runTest("traversing: is(String|undefined)");
    }

    /**
     * Test {307=[FF38, FF45], 308=[IE], 309=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void traversing__is___against_non_elements___10178_() throws Exception {
        runTest("traversing: is() against non-elements (#10178)");
    }

    /**
     * Test {308=[FF38, FF45], 309=[IE], 310=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void traversing__is_jQuery_() throws Exception {
        runTest("traversing: is(jQuery)");
    }

    /**
     * Test {309=[FF38, FF45], 310=[IE], 311=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__is___with__has___selectors() throws Exception {
        runTest("traversing: is() with :has() selectors");
    }

    /**
     * Test {310=[FF38, FF45], 311=[IE], 312=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void traversing__is___with_positional_selectors() throws Exception {
        runTest("traversing: is() with positional selectors");
    }

    /**
     * Test {311=[FF38, FF45], 312=[IE], 313=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__index__() throws Exception {
        runTest("traversing: index()");
    }

    /**
     * Test {312=[FF38, FF45], 313=[IE], 314=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__index_Object_String_undefined_() throws Exception {
        runTest("traversing: index(Object|String|undefined)");
    }

    /**
     * Test {313=[FF38, FF45], 314=[IE], 315=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__filter_Selector_undefined_() throws Exception {
        runTest("traversing: filter(Selector|undefined)");
    }

    /**
     * Test {314=[FF38, FF45], 315=[IE], 316=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__filter_Function_() throws Exception {
        runTest("traversing: filter(Function)");
    }

    /**
     * Test {315=[FF38, FF45], 316=[IE], 317=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Element_() throws Exception {
        runTest("traversing: filter(Element)");
    }

    /**
     * Test {316=[FF38, FF45], 317=[IE], 318=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Array_() throws Exception {
        runTest("traversing: filter(Array)");
    }

    /**
     * Test {317=[FF38, FF45], 318=[IE], 319=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_jQuery_() throws Exception {
        runTest("traversing: filter(jQuery)");
    }

    /**
     * Test {318=[FF38, FF45], 319=[IE], 320=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void traversing__filter___with_positional_selectors() throws Exception {
        runTest("traversing: filter() with positional selectors");
    }

    /**
     * Test {319=[FF38, FF45], 320=[IE], 321=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void traversing__closest__() throws Exception {
        runTest("traversing: closest()");
    }

    /**
     * Test {320=[FF38, FF45], 321=[IE], 322=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__closest___with_positional_selectors() throws Exception {
        runTest("traversing: closest() with positional selectors");
    }

    /**
     * Test {321=[FF38, FF45], 322=[IE], 323=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void traversing__closest_jQuery_() throws Exception {
        runTest("traversing: closest(jQuery)");
    }

    /**
     * Test {322=[FF38, FF45], 323=[IE], 324=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__not_Selector_undefined_() throws Exception {
        runTest("traversing: not(Selector|undefined)");
    }

    /**
     * Test {323=[FF38, FF45], 324=[IE], 325=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Element_() throws Exception {
        runTest("traversing: not(Element)");
    }

    /**
     * Test {324=[FF38, FF45], 325=[IE], 326=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Function_() throws Exception {
        runTest("traversing: not(Function)");
    }

    /**
     * Test {325=[FF38, FF45], 326=[IE], 327=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__not_Array_() throws Exception {
        runTest("traversing: not(Array)");
    }

    /**
     * Test {326=[FF38, FF45], 327=[IE], 328=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_jQuery_() throws Exception {
        runTest("traversing: not(jQuery)");
    }

    /**
     * Test {327=[FF38, FF45], 328=[IE], 329=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__has_Element_() throws Exception {
        runTest("traversing: has(Element)");
    }

    /**
     * Test {328=[FF38, FF45], 329=[IE], 330=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__has_Selector_() throws Exception {
        runTest("traversing: has(Selector)");
    }

    /**
     * Test {329=[FF38, FF45], 330=[IE], 331=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__has_Arrayish_() throws Exception {
        runTest("traversing: has(Arrayish)");
    }

    /**
     * Test {330=[FF38, FF45], 331=[IE], 332=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__addBack__() throws Exception {
        runTest("traversing: addBack()");
    }

    /**
     * Test {331=[FF38, FF45], 332=[IE], 333=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__siblings__String__() throws Exception {
        runTest("traversing: siblings([String])");
    }

    /**
     * Test {332=[FF38, FF45], 333=[IE], 334=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__siblings__String_____jQuery_only() throws Exception {
        runTest("traversing: siblings([String]) - jQuery only");
    }

    /**
     * Test {333=[FF38, FF45], 334=[IE], 335=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__children__String__() throws Exception {
        runTest("traversing: children([String])");
    }

    /**
     * Test {334=[FF38, FF45], 335=[IE], 336=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__children__String_____jQuery_only() throws Exception {
        runTest("traversing: children([String]) - jQuery only");
    }

    /**
     * Test {335=[FF38, FF45], 336=[IE], 337=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__parent__String__() throws Exception {
        runTest("traversing: parent([String])");
    }

    /**
     * Test {336=[FF38, FF45], 337=[IE], 338=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__parents__String__() throws Exception {
        runTest("traversing: parents([String])");
    }

    /**
     * Test {337=[FF38, FF45], 338=[IE], 339=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void traversing__parentsUntil__String__() throws Exception {
        runTest("traversing: parentsUntil([String])");
    }

    /**
     * Test {338=[FF38, FF45], 339=[IE], 340=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__next__String__() throws Exception {
        runTest("traversing: next([String])");
    }

    /**
     * Test {339=[FF38, FF45], 340=[IE], 341=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__prev__String__() throws Exception {
        runTest("traversing: prev([String])");
    }

    /**
     * Test {340=[FF38, FF45], 341=[IE], 342=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__nextAll__String__() throws Exception {
        runTest("traversing: nextAll([String])");
    }

    /**
     * Test {341=[FF38, FF45], 342=[IE], 343=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__prevAll__String__() throws Exception {
        runTest("traversing: prevAll([String])");
    }

    /**
     * Test {342=[FF38, FF45], 343=[IE], 344=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__nextUntil__String__() throws Exception {
        runTest("traversing: nextUntil([String])");
    }

    /**
     * Test {343=[FF38, FF45], 344=[IE], 345=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__prevUntil__String__() throws Exception {
        runTest("traversing: prevUntil([String])");
    }

    /**
     * Test {344=[FF38, FF45], 345=[IE], 346=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__contents__() throws Exception {
        runTest("traversing: contents()");
    }

    /**
     * Test {345=[FF38, FF45], 346=[IE], 347=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__sort_direction() throws Exception {
        runTest("traversing: sort direction");
    }

    /**
     * Test {346=[FF38, FF45], 347=[IE], 348=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__add_String_selector_() throws Exception {
        runTest("traversing: add(String selector)");
    }

    /**
     * Test {347=[FF38, FF45], 348=[IE], 349=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__add_String_selector__String_context_() throws Exception {
        runTest("traversing: add(String selector, String context)");
    }

    /**
     * Test {348=[FF38, FF45], 349=[IE], 350=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__add_String_html_() throws Exception {
        runTest("traversing: add(String html)");
    }

    /**
     * Test {349=[FF38, FF45], 350=[IE], 351=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__add_jQuery_() throws Exception {
        runTest("traversing: add(jQuery)");
    }

    /**
     * Test {350=[FF38, FF45], 351=[IE], 352=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__add_Element_() throws Exception {
        runTest("traversing: add(Element)");
    }

    /**
     * Test {351=[FF38, FF45], 352=[IE], 353=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__add_Array_elements_() throws Exception {
        runTest("traversing: add(Array elements)");
    }

    /**
     * Test {352=[FF38, FF45], 353=[IE], 354=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__add_Window_() throws Exception {
        runTest("traversing: add(Window)");
    }

    /**
     * Test {353=[FF38, FF45], 354=[IE], 355=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__add_NodeList_undefined_HTMLFormElement_HTMLSelectElement_() throws Exception {
        runTest("traversing: add(NodeList|undefined|HTMLFormElement|HTMLSelectElement)");
    }

    /**
     * Test {354=[FF38, FF45], 355=[IE], 356=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__add_String__Context_() throws Exception {
        runTest("traversing: add(String, Context)");
    }

    /**
     * Test {355=[FF38, FF45], 356=[IE], 357=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__eq___1____10616() throws Exception {
        runTest("traversing: eq('-1') #10616");
    }

    /**
     * Test {356=[FF38, FF45], 357=[IE], 358=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__index_no_arg___10977() throws Exception {
        runTest("traversing: index(no arg) #10977");
    }

    /**
     * Test {357=[FF38, FF45], 358=[IE], 359=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__traversing_non_elements_with_attribute_filters___12523_() throws Exception {
        runTest("traversing: traversing non-elements with attribute filters (#12523)");
    }

    /**
     * Test {358=[FF38, FF45], 359=[IE], 360=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__text__() throws Exception {
        runTest("manipulation: text()");
    }

    /**
     * Test {359=[FF38, FF45], 360=[IE], 361=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__text_undefined_() throws Exception {
        runTest("manipulation: text(undefined)");
    }

    /**
     * Test {360=[FF38, FF45], 361=[IE], 362=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__text_String_() throws Exception {
        runTest("manipulation: text(String)");
    }

    /**
     * Test {361=[FF38, FF45], 362=[IE], 363=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__text_Function_() throws Exception {
        runTest("manipulation: text(Function)");
    }

    /**
     * Test {362=[FF38, FF45], 363=[IE], 364=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__text_Function__with_incoming_value() throws Exception {
        runTest("manipulation: text(Function) with incoming value");
    }

    /**
     * Test {363=[FF38, FF45], 364=[IE], 365=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 78, 78")
    public void manipulation__append_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {364=[FF38, FF45], 365=[IE], 366=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 78, 78")
    public void manipulation__append_Function_() throws Exception {
        runTest("manipulation: append(Function)");
    }

    /**
     * Test {365=[FF38, FF45], 366=[IE], 367=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__append_param__to_object__see__11280() throws Exception {
        runTest("manipulation: append(param) to object, see #11280");
    }

    /**
     * Test {366=[FF38, FF45], 367=[IE], 368=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__append_Function__returns_String() throws Exception {
        runTest("manipulation: append(Function) returns String");
    }

    /**
     * Test {367=[FF38, FF45], 368=[IE], 369=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_Function__returns_Element() throws Exception {
        runTest("manipulation: append(Function) returns Element");
    }

    /**
     * Test {368=[FF38, FF45], 369=[IE], 370=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: append(Function) returns Array<Element>");
    }

    /**
     * Test {369=[FF38, FF45], 370=[IE], 371=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_Function__returns_jQuery() throws Exception {
        runTest("manipulation: append(Function) returns jQuery");
    }

    /**
     * Test {370=[FF38, FF45], 371=[IE], 372=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_Function__returns_Number() throws Exception {
        runTest("manipulation: append(Function) returns Number");
    }

    /**
     * Test {371=[FF38, FF45], 372=[IE], 373=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__XML_DOM_manipulation___9960_() throws Exception {
        runTest("manipulation: XML DOM manipulation (#9960)");
    }

    /**
     * Test {372=[FF38, FF45], 373=[IE], 374=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_the_same_fragment_with_events__Bug__6997__5566_() throws Exception {
        runTest("manipulation: append the same fragment with events (Bug #6997, 5566)");
    }

    /**
     * Test {373=[FF38, FF45], 374=[IE], 375=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_HTML5_sectioning_elements__Bug__6485_() throws Exception {
        runTest("manipulation: append HTML5 sectioning elements (Bug #6485)");
    }

    /**
     * Test {374=[FF38, FF45], 375=[IE], 376=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__HTML5_Elements_inherit_styles_from_style_rules__Bug__10501_() throws Exception {
        runTest("manipulation: HTML5 Elements inherit styles from style rules (Bug #10501)");
    }

    /**
     * Test {375=[FF38, FF45], 376=[IE], 377=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_String__with_HTML5__Bug__6485_() throws Exception {
        runTest("manipulation: html(String) with HTML5 (Bug #6485)");
    }

    /**
     * Test {376=[FF38, FF45], 377=[IE], 378=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__IE8_serialization_bug() throws Exception {
        runTest("manipulation: IE8 serialization bug");
    }

    /**
     * Test {377=[FF38, FF45], 378=[IE], 379=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___object_element__10324() throws Exception {
        runTest("manipulation: html() object element #10324");
    }

    /**
     * Test {378=[FF38, FF45], 379=[IE], 380=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__append_xml_() throws Exception {
        runTest("manipulation: append(xml)");
    }

    /**
     * Test {379=[FF38, FF45], 380=[IE], 381=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__appendTo_String_() throws Exception {
        runTest("manipulation: appendTo(String)");
    }

    /**
     * Test {380=[FF38, FF45], 381=[IE], 382=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__appendTo_Element_Array_Element__() throws Exception {
        runTest("manipulation: appendTo(Element|Array<Element>)");
    }

    /**
     * Test {381=[FF38, FF45], 382=[IE], 383=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__appendTo_jQuery_() throws Exception {
        runTest("manipulation: appendTo(jQuery)");
    }

    /**
     * Test {382=[FF38, FF45], 383=[IE], 384=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prepend_String_() throws Exception {
        runTest("manipulation: prepend(String)");
    }

    /**
     * Test {383=[FF38, FF45], 384=[IE], 385=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prepend_Element_() throws Exception {
        runTest("manipulation: prepend(Element)");
    }

    /**
     * Test {384=[FF38, FF45], 385=[IE], 386=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prepend_Array_Element__() throws Exception {
        runTest("manipulation: prepend(Array<Element>)");
    }

    /**
     * Test {385=[FF38, FF45], 386=[IE], 387=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prepend_jQuery_() throws Exception {
        runTest("manipulation: prepend(jQuery)");
    }

    /**
     * Test {386=[FF38, FF45], 387=[IE], 388=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prepend_Array_jQuery__() throws Exception {
        runTest("manipulation: prepend(Array<jQuery>)");
    }

    /**
     * Test {387=[FF38, FF45], 388=[IE], 389=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__prepend_Function__with_incoming_value____String() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- String");
    }

    /**
     * Test {388=[FF38, FF45], 389=[IE], 390=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prepend_Function__with_incoming_value____Element() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Element");
    }

    /**
     * Test {389=[FF38, FF45], 390=[IE], 391=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prepend_Function__with_incoming_value____Array_Element_() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Array<Element>");
    }

    /**
     * Test {390=[FF38, FF45], 391=[IE], 392=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prepend_Function__with_incoming_value____jQuery() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- jQuery");
    }

    /**
     * Test {391=[FF38, FF45], 392=[IE], 393=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__prependTo_String_() throws Exception {
        runTest("manipulation: prependTo(String)");
    }

    /**
     * Test {392=[FF38, FF45], 393=[IE], 394=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prependTo_Element_() throws Exception {
        runTest("manipulation: prependTo(Element)");
    }

    /**
     * Test {393=[FF38, FF45], 394=[IE], 395=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prependTo_Array_Element__() throws Exception {
        runTest("manipulation: prependTo(Array<Element>)");
    }

    /**
     * Test {394=[FF38, FF45], 395=[IE], 396=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prependTo_jQuery_() throws Exception {
        runTest("manipulation: prependTo(jQuery)");
    }

    /**
     * Test {395=[FF38, FF45], 396=[IE], 397=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__prependTo_Array_jQuery__() throws Exception {
        runTest("manipulation: prependTo(Array<jQuery>)");
    }

    /**
     * Test {396=[FF38, FF45], 397=[IE], 398=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_String_() throws Exception {
        runTest("manipulation: before(String)");
    }

    /**
     * Test {397=[FF38, FF45], 398=[IE], 399=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Element_() throws Exception {
        runTest("manipulation: before(Element)");
    }

    /**
     * Test {398=[FF38, FF45], 399=[IE], 400=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Array_Element__() throws Exception {
        runTest("manipulation: before(Array<Element>)");
    }

    /**
     * Test {399=[FF38, FF45], 400=[IE], 401=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_jQuery_() throws Exception {
        runTest("manipulation: before(jQuery)");
    }

    /**
     * Test {400=[FF38, FF45], 401=[IE], 402=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Array_jQuery__() throws Exception {
        runTest("manipulation: before(Array<jQuery>)");
    }

    /**
     * Test {401=[FF38, FF45], 402=[IE], 403=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_String() throws Exception {
        runTest("manipulation: before(Function) -- Returns String");
    }

    /**
     * Test {402=[FF38, FF45], 403=[IE], 404=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_Element() throws Exception {
        runTest("manipulation: before(Function) -- Returns Element");
    }

    /**
     * Test {403=[FF38, FF45], 404=[IE], 405=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_Array_Element_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<Element>");
    }

    /**
     * Test {404=[FF38, FF45], 405=[IE], 406=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_jQuery() throws Exception {
        runTest("manipulation: before(Function) -- Returns jQuery");
    }

    /**
     * Test {405=[FF38, FF45], 406=[IE], 407=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_Function_____Returns_Array_jQuery_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<jQuery>");
    }

    /**
     * Test {406=[FF38, FF45], 407=[IE], 408=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_no_op_() throws Exception {
        runTest("manipulation: before(no-op)");
    }

    /**
     * Test {407=[FF38, FF45], 408=[IE], 409=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__before_and_after_w__empty_object___10812_() throws Exception {
        runTest("manipulation: before and after w/ empty object (#10812)");
    }

    /**
     * Test {408=[FF38, FF45], 409=[IE], 410=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation___before___and__after___disconnected_node() throws Exception {
        runTest("manipulation: .before() and .after() disconnected node");
    }

    /**
     * Test {410=[FF38, FF45], 411=[IE], 412=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insert_with__before___on_disconnected_node_first() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node first");
    }

    /**
     * Test {412=[FF38, FF45], 413=[IE], 414=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insert_with__before___on_disconnected_node_last() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node last");
    }

    /**
     * Test {413=[FF38, FF45], 414=[IE], 415=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertBefore_String_() throws Exception {
        runTest("manipulation: insertBefore(String)");
    }

    /**
     * Test {414=[FF38, FF45], 415=[IE], 416=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertBefore_Element_() throws Exception {
        runTest("manipulation: insertBefore(Element)");
    }

    /**
     * Test {415=[FF38, FF45], 416=[IE], 417=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertBefore_Array_Element__() throws Exception {
        runTest("manipulation: insertBefore(Array<Element>)");
    }

    /**
     * Test {416=[FF38, FF45], 417=[IE], 418=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertBefore_jQuery_() throws Exception {
        runTest("manipulation: insertBefore(jQuery)");
    }

    /**
     * Test {417=[FF38, FF45], 418=[IE], 419=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_String_() throws Exception {
        runTest("manipulation: .after(String)");
    }

    /**
     * Test {418=[FF38, FF45], 419=[IE], 420=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Element_() throws Exception {
        runTest("manipulation: .after(Element)");
    }

    /**
     * Test {419=[FF38, FF45], 420=[IE], 421=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Array_Element__() throws Exception {
        runTest("manipulation: .after(Array<Element>)");
    }

    /**
     * Test {420=[FF38, FF45], 421=[IE], 422=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_jQuery_() throws Exception {
        runTest("manipulation: .after(jQuery)");
    }

    /**
     * Test {421=[FF38, FF45], 422=[IE], 423=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Function__returns_String() throws Exception {
        runTest("manipulation: .after(Function) returns String");
    }

    /**
     * Test {422=[FF38, FF45], 423=[IE], 424=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Function__returns_Element() throws Exception {
        runTest("manipulation: .after(Function) returns Element");
    }

    /**
     * Test {423=[FF38, FF45], 424=[IE], 425=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: .after(Function) returns Array<Element>");
    }

    /**
     * Test {424=[FF38, FF45], 425=[IE], 426=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation___after_Function__returns_jQuery() throws Exception {
        runTest("manipulation: .after(Function) returns jQuery");
    }

    /**
     * Test {425=[FF38, FF45], 426=[IE], 427=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation___after_disconnected_node_() throws Exception {
        runTest("manipulation: .after(disconnected node)");
    }

    /**
     * Test {426=[FF38, FF45], 427=[IE], 428=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertAfter_String_() throws Exception {
        runTest("manipulation: insertAfter(String)");
    }

    /**
     * Test {427=[FF38, FF45], 428=[IE], 429=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertAfter_Element_() throws Exception {
        runTest("manipulation: insertAfter(Element)");
    }

    /**
     * Test {428=[FF38, FF45], 429=[IE], 430=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertAfter_Array_Element__() throws Exception {
        runTest("manipulation: insertAfter(Array<Element>)");
    }

    /**
     * Test {429=[FF38, FF45], 430=[IE], 431=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__insertAfter_jQuery_() throws Exception {
        runTest("manipulation: insertAfter(jQuery)");
    }

    /**
     * Test {430=[FF38, FF45], 431=[IE], 432=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void manipulation__replaceWith_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {431=[FF38, FF45], 432=[IE], 433=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    public void manipulation__replaceWith_Function_() throws Exception {
        runTest("manipulation: replaceWith(Function)");
    }

    /**
     * Test {432=[FF38, FF45], 433=[IE], 434=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceWith_string__for_more_than_one_element() throws Exception {
        runTest("manipulation: replaceWith(string) for more than one element");
    }

    /**
     * Test {433=[FF38, FF45], 434=[IE], 435=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__Empty_replaceWith___13401___13596_() throws Exception {
        runTest("manipulation: Empty replaceWith (#13401; #13596)");
    }

    /**
     * Test {434=[FF38, FF45], 435=[IE], 436=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__replaceAll_String_() throws Exception {
        runTest("manipulation: replaceAll(String)");
    }

    /**
     * Test {435=[FF38, FF45], 436=[IE], 437=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__replaceAll_Element_() throws Exception {
        runTest("manipulation: replaceAll(Element)");
    }

    /**
     * Test {436=[FF38, FF45], 437=[IE], 438=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceAll_Array_Element__() throws Exception {
        runTest("manipulation: replaceAll(Array<Element>)");
    }

    /**
     * Test {437=[FF38, FF45], 438=[IE], 439=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceAll_jQuery_() throws Exception {
        runTest("manipulation: replaceAll(jQuery)");
    }

    /**
     * Test {438=[FF38, FF45], 439=[IE], 440=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery_clone_____8017_() throws Exception {
        runTest("manipulation: jQuery.clone() (#8017)");
    }

    /**
     * Test {439=[FF38, FF45], 440=[IE], 441=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_to_multiple_elements___8070_() throws Exception {
        runTest("manipulation: append to multiple elements (#8070)");
    }

    /**
     * Test {440=[FF38, FF45], 441=[IE], 442=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void manipulation__table_manipulation() throws Exception {
        runTest("manipulation: table manipulation");
    }

    /**
     * Test {441=[FF38, FF45], 442=[IE], 443=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 45, 45")
    public void manipulation__clone__() throws Exception {
        runTest("manipulation: clone()");
    }

    /**
     * Test {442=[FF38, FF45], 443=[IE], 444=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__clone_script_type_non_javascript____11359_() throws Exception {
        runTest("manipulation: clone(script type=non-javascript) (#11359)");
    }

    /**
     * Test {443=[FF38, FF45], 444=[IE], 445=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__clone_form_element___Bug__3879___6655_() throws Exception {
        runTest("manipulation: clone(form element) (Bug #3879, #6655)");
    }

    /**
     * Test {444=[FF38, FF45], 445=[IE], 446=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__clone_multiple_selected_options___Bug__8129_() throws Exception {
        runTest("manipulation: clone(multiple selected options) (Bug #8129)");
    }

    /**
     * Test {445=[FF38, FF45], 446=[IE], 447=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_XML_nodes() throws Exception {
        runTest("manipulation: clone() on XML nodes");
    }

    /**
     * Test {446=[FF38, FF45], 447=[IE], 448=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_local_XML_nodes_with_html5_nodename() throws Exception {
        runTest("manipulation: clone() on local XML nodes with html5 nodename");
    }

    /**
     * Test {447=[FF38, FF45], 448=[IE], 449=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html_undefined_() throws Exception {
        runTest("manipulation: html(undefined)");
    }

    /**
     * Test {448=[FF38, FF45], 449=[IE], 450=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___on_empty_set() throws Exception {
        runTest("manipulation: html() on empty set");
    }

    /**
     * Test {449=[FF38, FF45], 450=[IE], 451=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 39, 39")
    public void manipulation__html_String_Number_() throws Exception {
        runTest("manipulation: html(String|Number)");
    }

    /**
     * Test {450=[FF38, FF45], 451=[IE], 452=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 39, 39")
    public void manipulation__html_Function_() throws Exception {
        runTest("manipulation: html(Function)");
    }

    /**
     * Test {451=[FF38, FF45], 452=[IE], 453=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html____text____() throws Exception {
        runTest("manipulation: html( $.text() )");
    }

    /**
     * Test {452=[FF38, FF45], 453=[IE], 454=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html__fn___returns___text__() throws Exception {
        runTest("manipulation: html( fn ) returns $.text()");
    }

    /**
     * Test {453=[FF38, FF45], 454=[IE], 455=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__html_Function__with_incoming_value____direct_selection() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- direct selection");
    }

    /**
     * Test {454=[FF38, FF45], 455=[IE], 456=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void manipulation__html_Function__with_incoming_value____jQuery_contents__() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- jQuery.contents()");
    }

    /**
     * Test {455=[FF38, FF45], 456=[IE], 457=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___html___don_t_expose_jQuery_Sizzle_expandos___12858_() throws Exception {
        runTest("manipulation: clone()/html() don't expose jQuery/Sizzle expandos (#12858)");
    }

    /**
     * Test {456=[FF38, FF45], 457=[IE], 458=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__remove___no_filters() throws Exception {
        runTest("manipulation: remove() no filters");
    }

    /**
     * Test {457=[FF38, FF45], 458=[IE], 459=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__remove___with_filters() throws Exception {
        runTest("manipulation: remove() with filters");
    }

    /**
     * Test {458=[FF38, FF45], 459=[IE], 460=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__remove___event_cleaning() throws Exception {
        runTest("manipulation: remove() event cleaning");
    }

    /**
     * Test {459=[FF38, FF45], 460=[IE], 461=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__remove___in_document_order__13779() throws Exception {
        runTest("manipulation: remove() in document order #13779");
    }

    /**
     * Test {460=[FF38, FF45], 461=[IE], 462=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__detach___no_filters() throws Exception {
        runTest("manipulation: detach() no filters");
    }

    /**
     * Test {461=[FF38, FF45], 462=[IE], 463=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__detach___with_filters() throws Exception {
        runTest("manipulation: detach() with filters");
    }

    /**
     * Test {462=[FF38, FF45], 463=[IE], 464=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__detach___event_cleaning() throws Exception {
        runTest("manipulation: detach() event cleaning");
    }

    /**
     * Test {463=[FF38, FF45], 464=[IE], 465=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__empty__() throws Exception {
        runTest("manipulation: empty()");
    }

    /**
     * Test {464=[FF38, FF45], 465=[IE], 466=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void manipulation__jQuery_cleanData() throws Exception {
        runTest("manipulation: jQuery.cleanData");
    }

    /**
     * Test {465=[FF38, FF45], 466=[IE], 467=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___no_plain_text_caching__Bug__6779_() throws Exception {
        runTest("manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779)");
    }

    /**
     * Test {466=[FF38, FF45], 467=[IE], 468=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__jQuery_html___execute_scripts_escaped_with_html_comment_or_CDATA___9221_() throws Exception {
        runTest("manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221)");
    }

    /**
     * Test {467=[FF38, FF45], 468=[IE], 469=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___plain_objects_are_not_a_document__8950() throws Exception {
        runTest("manipulation: jQuery.buildFragment - plain objects are not a document #8950");
    }

    /**
     * Test {468=[FF38, FF45], 469=[IE], 470=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_clone___no_exceptions_for_object_elements__9587() throws Exception {
        runTest("manipulation: jQuery.clone - no exceptions for object elements #9587");
    }

    /**
     * Test {469=[FF38, FF45], 470=[IE], 471=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__Cloned__detached_HTML5_elems___10667_10670_() throws Exception {
        runTest("manipulation: Cloned, detached HTML5 elems (#10667,10670)");
    }

    /**
     * Test {470=[FF38, FF45], 471=[IE], 472=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__Guard_against_exceptions_when_clearing_safeChildNodes() throws Exception {
        runTest("manipulation: Guard against exceptions when clearing safeChildNodes");
    }

    /**
     * Test {471=[FF38, FF45], 472=[IE], 473=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__Ensure_oldIE_creates_a_new_set_on_appendTo___8894_() throws Exception {
        runTest("manipulation: Ensure oldIE creates a new set on appendTo (#8894)");
    }

    /**
     * Test {472=[FF38, FF45], 473=[IE], 474=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_____script_exceptions_bubble___11743_() throws Exception {
        runTest("manipulation: html() - script exceptions bubble (#11743)");
    }

    /**
     * Test {473=[FF38, FF45], 474=[IE], 475=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__checked_state_is_cloned_with_clone__() throws Exception {
        runTest("manipulation: checked state is cloned with clone()");
    }

    /**
     * Test {474=[FF38, FF45], 475=[IE], 476=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__manipulate_mixed_jQuery_and_text___12384___12346_() throws Exception {
        runTest("manipulation: manipulate mixed jQuery and text (#12384, #12346)");
    }

    /**
     * Test {475=[FF38, FF45], 476=[IE], 477=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__buildFragment_works_even_if_document_0__is_iframe_s_window_object_in_IE9_10___12266_() throws Exception {
        runTest("manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266)");
    }

    /**
     * Test {476=[FF38, FF45], 477=[IE], 478=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void manipulation__script_evaluation___11795_() throws Exception {
        runTest("manipulation: script evaluation (#11795)");
    }

    /**
     * Test {477=[FF38, FF45], 478=[IE], 479=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__jQuery__evalUrl___12838_() throws Exception {
        runTest("manipulation: jQuery._evalUrl (#12838)");
    }

    /**
     * Test {478=[FF38, FF45], 479=[IE], 480=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__insertAfter__insertBefore__etc_do_not_work_when_destination_is_original_element__Element_is_removed___4087_() throws Exception {
        runTest("manipulation: insertAfter, insertBefore, etc do not work when destination is original element. Element is removed (#4087)");
    }

    /**
     * Test {479=[FF38, FF45], 480=[IE], 481=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__Index_for_function_argument_should_be_received___13094_() throws Exception {
        runTest("manipulation: Index for function argument should be received (#13094)");
    }

    /**
     * Test {480=[FF38, FF45], 481=[IE], 482=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__Make_sure_jQuery_fn_remove_can_work_on_elements_in_documentFragment() throws Exception {
        runTest("manipulation: Make sure jQuery.fn.remove can work on elements in documentFragment");
    }

    /**
     * Test {481=[FF38, FF45], 482=[IE], 483=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void wrap__wrap_String_Element_() throws Exception {
        runTest("wrap: wrap(String|Element)");
    }

    /**
     * Test {482=[FF38, FF45], 483=[IE], 484=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void wrap__wrap_Function_() throws Exception {
        runTest("wrap: wrap(Function)");
    }

    /**
     * Test {483=[FF38, FF45], 484=[IE], 485=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void wrap__wrap_Function__with_index___10177_() throws Exception {
        runTest("wrap: wrap(Function) with index (#10177)");
    }

    /**
     * Test {484=[FF38, FF45], 485=[IE], 486=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void wrap__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("wrap: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {485=[FF38, FF45], 486=[IE], 487=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void wrap__wrapAll_String_() throws Exception {
        runTest("wrap: wrapAll(String)");
    }

    /**
     * Test {486=[FF38, FF45], 487=[IE], 488=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void wrap__wrapAll_Element_() throws Exception {
        runTest("wrap: wrapAll(Element)");
    }

    /**
     * Test {487=[FF38, FF45], 488=[IE], 489=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void wrap__wrapInner_String_() throws Exception {
        runTest("wrap: wrapInner(String)");
    }

    /**
     * Test {488=[FF38, FF45], 489=[IE], 490=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void wrap__wrapInner_Element_() throws Exception {
        runTest("wrap: wrapInner(Element)");
    }

    /**
     * Test {489=[FF38, FF45], 490=[IE], 491=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void wrap__wrapInner_Function__returns_String() throws Exception {
        runTest("wrap: wrapInner(Function) returns String");
    }

    /**
     * Test {490=[FF38, FF45], 491=[IE], 492=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void wrap__wrapInner_Function__returns_Element() throws Exception {
        runTest("wrap: wrapInner(Function) returns Element");
    }

    /**
     * Test {491=[FF38, FF45], 492=[IE], 493=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void wrap__unwrap__() throws Exception {
        runTest("wrap: unwrap()");
    }

    /**
     * Test {492=[FF38, FF45], 493=[IE], 494=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void wrap__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("wrap: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {493=[FF38, FF45], 494=[IE], 495=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void wrap__wrapping_scripts___10470_() throws Exception {
        runTest("wrap: wrapping scripts (#10470)");
    }

    /**
     * Test {494=[FF38, FF45], 495=[IE], 496=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 43, 43")
    public void css__css_String_Hash_() throws Exception {
        runTest("css: css(String|Hash)");
    }

    /**
     * Test {495=[FF38, FF45], 496=[IE], 497=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void css__css___explicit_and_relative_values() throws Exception {
        runTest("css: css() explicit and relative values");
    }

    /**
     * Test {496=[FF38, FF45], 497=[IE], 498=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    @NotYetImplemented
    public void css__css_String__Object_() throws Exception {
        runTest("css: css(String, Object)");
    }

    /**
     * Test {497=[FF38, FF45], 498=[IE], 499=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__css_Array_() throws Exception {
        runTest("css: css(Array)");
    }

    /**
     * Test {498=[FF38, FF45], 499=[IE], 500=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function_() throws Exception {
        runTest("css: css(String, Function)");
    }

    /**
     * Test {499=[FF38, FF45], 500=[IE], 501=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function__with_incoming_value() throws Exception {
        runTest("css: css(String, Function) with incoming value");
    }

    /**
     * Test {500=[FF38, FF45], 501=[IE], 502=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions() throws Exception {
        runTest("css: css(Object) where values are Functions");
    }

    /**
     * Test {501=[FF38, FF45], 502=[IE], 503=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions_with_incoming_values() throws Exception {
        runTest("css: css(Object) where values are Functions with incoming values");
    }

    /**
     * Test {502=[FF38, FF45], 503=[IE], 504=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__show____hide__() throws Exception {
        runTest("css: show(); hide()");
    }

    /**
     * Test {503=[FF38, FF45], 504=[IE], 505=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void css__show___() throws Exception {
        runTest("css: show();");
    }

    /**
     * Test {504=[FF38, FF45], 505=[IE], 506=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    @NotYetImplemented
    public void css__show___resolves_correct_default_display__8099() throws Exception {
        runTest("css: show() resolves correct default display #8099");
    }

    /**
     * Test {505=[FF38, FF45], 506=[IE], 507=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void css__show___resolves_correct_default_display_for_detached_nodes() throws Exception {
        runTest("css: show() resolves correct default display for detached nodes");
    }

    /**
     * Test {506=[FF38, FF45], 507=[IE], 508=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @NotYetImplemented
    public void css__show___resolves_correct_default_display__10227() throws Exception {
        runTest("css: show() resolves correct default display #10227");
    }

    /**
     * Test {507=[FF38, FF45], 508=[IE], 509=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__show___resolves_correct_default_display_when_iframe_display_none__12904() throws Exception {
        runTest("css: show() resolves correct default display when iframe display:none #12904");
    }

    /**
     * Test {508=[FF38, FF45], 509=[IE], 510=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void css__toggle__() throws Exception {
        runTest("css: toggle()");
    }

    /**
     * Test {509=[FF38, FF45], 510=[IE], 511=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__hide_hidden_elements__bug__7141_() throws Exception {
        runTest("css: hide hidden elements (bug #7141)");
    }

    /**
     * Test {510=[FF38, FF45], 511=[IE], 512=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {511=[FF38, FF45], 512=[IE], 513=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__internal_ref_to_elem_runtimeStyle__bug__7608_() throws Exception {
        runTest("css: internal ref to elem.runtimeStyle (bug #7608)");
    }

    /**
     * Test {512=[FF38, FF45], 513=[IE], 514=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__marginRight_computed_style__bug__3333_() throws Exception {
        runTest("css: marginRight computed style (bug #3333)");
    }

    /**
     * Test {513=[FF38, FF45], 514=[IE], 515=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__box_model_properties_incorrectly_returning___instead_of_px__see__10639_and__12088() throws Exception {
        runTest("css: box model properties incorrectly returning % instead of px, see #10639 and #12088");
    }

    /**
     * Test {514=[FF38, FF45], 515=[IE], 516=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__jQuery_cssProps_behavior___bug__8402_() throws Exception {
        runTest("css: jQuery.cssProps behavior, (bug #8402)");
    }

    /**
     * Test {515=[FF38, FF45], 516=[IE], 517=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "0, 2, 2",
        FF45 = "0, 1, 1")
    public void css__widows___orphans__8936() throws Exception {
        runTest("css: widows & orphans #8936");
    }

    /**
     * Test {516=[FF38, FF45], 517=[IE], 518=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__can_t_get_css_for_disconnected_in_IE_9__see__10254_and__8388() throws Exception {
        runTest("css: can't get css for disconnected in IE<9, see #10254 and #8388");
    }

    /**
     * Test {517=[FF38, FF45], 518=[IE], 519=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void css__can_t_get_background_position_in_IE_9__see__10796() throws Exception {
        runTest("css: can't get background-position in IE<9, see #10796");
    }

    /**
     * Test {518=[FF38, FF45], 519=[IE], 520=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__percentage_properties_for_bottom_and_right_in_IE_9_should_not_be_incorrectly_transformed_to_pixels__see__11311() throws Exception {
        runTest("css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pixels, see #11311");
    }

    /**
     * Test {519=[FF38, FF45], 520=[IE], 521=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__percentage_properties_for_left_and_top_should_be_transformed_to_pixels__see__9505() throws Exception {
        runTest("css: percentage properties for left and top should be transformed to pixels, see #9505");
    }

    /**
     * Test {520=[FF38, FF45], 521=[IE], 522=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__Do_not_append_px___9548___12990_() throws Exception {
        runTest("css: Do not append px (#9548, #12990)");
    }

    /**
     * Test {521=[FF38, FF45], 522=[IE], 523=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @NotYetImplemented
    public void css__css__width___and_css__height___should_respect_box_sizing__see__11004() throws Exception {
        runTest("css: css('width') and css('height') should respect box-sizing, see #11004");
    }

    /**
     * Test {522=[FF38, FF45], 523=[IE], 524=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void css__css__width___should_work_correctly_before_document_ready___14084_() throws Exception {
        runTest("css: css('width') should work correctly before document ready (#14084)");
    }

    /**
     * Test {523=[FF38, FF45], 524=[IE], 525=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__certain_css_values_of__normal__should_be_convertable_to_a_number__see__8627() throws Exception {
        runTest("css: certain css values of 'normal' should be convertable to a number, see #8627");
    }

    /**
     * Test {524=[FF38, FF45], 525=[IE], 526=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void css__cssHooks___expand() throws Exception {
        runTest("css: cssHooks - expand");
    }

    /**
     * Test {525=[FF38, FF45], 526=[IE], 527=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__css_opacity_consistency_across_browsers___12685_() throws Exception {
        runTest("css: css opacity consistency across browsers (#12685)");
    }

    /**
     * Test {526=[FF38, FF45], 527=[IE], 528=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    @NotYetImplemented
    public void css___visible__hidden_selectors() throws Exception {
        runTest("css: :visible/:hidden selectors");
    }

    /**
     * Test {527=[FF38, FF45], 528=[IE], 529=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__Keep_the_last_style_if_the_new_one_isn_t_recognized_by_the_browser___14836_() throws Exception {
        runTest("css: Keep the last style if the new one isn't recognized by the browser (#14836)");
    }

    /**
     * Test {528=[FF38, FF45], 529=[IE], 530=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__Reset_the_style_if_set_to_an_empty_string() throws Exception {
        runTest("css: Reset the style if set to an empty string");
    }

    /**
     * Test {529=[FF38, FF45], 530=[IE], 531=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void css__Clearing_a_Cloned_Element_s_Style_Shouldn_t_Clear_the_Original_Element_s_Style___8908_() throws Exception {
        runTest("css: Clearing a Cloned Element's Style Shouldn't Clear the Original Element's Style (#8908)");
    }

    /**
     * Test {530=[FF38, FF45], 531=[IE], 532=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__Make_sure_initialized_display_value_for_disconnected_nodes_is_correct___13310_() throws Exception {
        runTest("css: Make sure initialized display value for disconnected nodes is correct (#13310)");
    }

    /**
     * Test {531=[FF38, FF45], 532=[IE], 533=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__show___after_hide___should_always_set_display_to_initial_value___14750_() throws Exception {
        runTest("css: show() after hide() should always set display to initial value (#14750)");
    }

    /**
     * Test {532=[FF38, FF45], 533=[IE], 534=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__Don_t_append_px_to_CSS__order__value___14049_() throws Exception {
        runTest("css: Don't append px to CSS \"order\" value (#14049)");
    }

    /**
     * Test {533=[FF38, FF45], 534=[IE], 535=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__Do_not_throw_on_frame_elements_from_css_method___15098_() throws Exception {
        runTest("css: Do not throw on frame elements from css method (#15098)");
    }

    /**
     * Test {534=[FF38, FF45], 535=[IE], 536=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void serialize__jQuery_param__() throws Exception {
        runTest("serialize: jQuery.param()");
    }

    /**
     * Test {535=[FF38, FF45], 536=[IE], 537=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void serialize__jQuery_param___Constructed_prop_values() throws Exception {
        runTest("serialize: jQuery.param() Constructed prop values");
    }

    /**
     * Test {536=[FF38, FF45], 537=[IE], 538=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void serialize__serialize__() throws Exception {
        runTest("serialize: serialize()");
    }

    /**
     * Test {537=[FF38, FF45], 538=[IE], 539=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks");
    }

    /**
     * Test {538=[FF38, FF45], 539=[IE], 540=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks____url__options__syntax() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks - (url, options) syntax");
    }

    /**
     * Test {539=[FF38, FF45], 540=[IE], 541=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (late binding)");
    }

    /**
     * Test {540=[FF38, FF45], 541=[IE], 542=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__oncomplete_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (oncomplete binding)");
    }

    /**
     * Test {541=[FF38, FF45], 542=[IE], 543=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____error_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - error callbacks");
    }

    /**
     * Test {542=[FF38, FF45], 543=[IE], 544=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_____textStatus_and_errorThrown_values() throws Exception {
        runTest("ajax: jQuery.ajax() - textStatus and errorThrown values");
    }

    /**
     * Test {543=[FF38, FF45], 544=[IE], 545=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____responseText_on_error() throws Exception {
        runTest("ajax: jQuery.ajax() - responseText on error");
    }

    /**
     * Test {544=[FF38, FF45], 545=[IE], 546=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: jQuery.ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {545=[FF38, FF45], 546=[IE], 547=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____headers() throws Exception {
        runTest("ajax: jQuery.ajax() - headers");
    }

    /**
     * Test {546=[FF38, FF45], 547=[IE], 548=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____Accept_header() throws Exception {
        runTest("ajax: jQuery.ajax() - Accept header");
    }

    /**
     * Test {547=[FF38, FF45], 548=[IE], 549=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____contentType() throws Exception {
        runTest("ajax: jQuery.ajax() - contentType");
    }

    /**
     * Test {548=[FF38, FF45], 549=[IE], 550=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: jQuery.ajax() - protocol-less urls");
    }

    /**
     * Test {549=[FF38, FF45], 550=[IE], 551=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____hash() throws Exception {
        runTest("ajax: jQuery.ajax() - hash");
    }

    /**
     * Test {550=[FF38, FF45], 551=[IE], 552=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void ajax__jQuery_ajax_____cross_domain_detection() throws Exception {
        runTest("ajax: jQuery.ajax() - cross-domain detection");
    }

    /**
     * Test {551=[FF38, FF45], 552=[IE], 553=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void ajax__jQuery_ajax_____abort() throws Exception {
        runTest("ajax: jQuery.ajax() - abort");
    }

    /**
     * Test {552=[FF38, FF45], 553=[IE], 554=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void ajax__jQuery_ajax_____events_with_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events with context");
    }

    /**
     * Test {553=[FF38, FF45], 554=[IE], 555=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____events_without_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events without context");
    }

    /**
     * Test {554=[FF38, FF45], 555=[IE], 556=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___15118___jQuery_ajax_____function_without_jQuery_event() throws Exception {
        runTest("ajax: #15118 - jQuery.ajax() - function without jQuery.event");
    }

    /**
     * Test {555=[FF38, FF45], 556=[IE], 557=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____context_modification() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification");
    }

    /**
     * Test {556=[FF38, FF45], 557=[IE], 558=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification through ajaxSetup");
    }

    /**
     * Test {557=[FF38, FF45], 558=[IE], 559=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____disabled_globals() throws Exception {
        runTest("ajax: jQuery.ajax() - disabled globals");
    }

    /**
     * Test {558=[FF38, FF45], 559=[IE], 560=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {559=[FF38, FF45], 560=[IE], 561=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {560=[FF38, FF45], 561=[IE], 562=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - HEAD requests");
    }

    /**
     * Test {561=[FF38, FF45], 562=[IE], 563=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend");
    }

    /**
     * Test {562=[FF38, FF45], 563=[IE], 564=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend, cancel request manually");
    }

    /**
     * Test {563=[FF38, FF45], 564=[IE], 565=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax() - dataType html");
    }

    /**
     * Test {564=[FF38, FF45], 565=[IE], 566=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____synchronous_request() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request");
    }

    /**
     * Test {565=[FF38, FF45], 566=[IE], 567=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request with callbacks");
    }

    /**
     * Test {566=[FF38, FF45], 567=[IE], 568=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax____jQuery_get_Script_JSON_____jQuery_post____pass_through_request_object() throws Exception {
        runTest("ajax: jQuery.ajax(), jQuery.get[Script|JSON](), jQuery.post(), pass-through request object");
    }

    /**
     * Test {567=[FF38, FF45], 568=[IE], 569=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void ajax__jQuery_ajax_____cache() throws Exception {
        runTest("ajax: jQuery.ajax() - cache");
    }

    /**
     * Test {568=[FF38, FF45], 569=[IE], 570=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Same Domain");
    }

    /**
     * Test {569=[FF38, FF45], 570=[IE], 571=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Same Domain");
    }

    /**
     * Test {570=[FF38, FF45], 571=[IE], 572=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Same Domain");
    }

    /**
     * Test {571=[FF38, FF45], 572=[IE], 573=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____JSONP___POST___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Same Domain");
    }

    /**
     * Test {572=[FF38, FF45], 573=[IE], 574=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____JSONP___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Same Domain");
    }

    /**
     * Test {573=[FF38, FF45], 574=[IE], 575=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @NotYetImplemented(IE)
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Cross Domain");
    }

    /**
     * Test {574=[FF38, FF45], 575=[IE], 576=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    @NotYetImplemented(IE)
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Cross Domain");
    }

    /**
     * Test {575=[FF38, FF45], 576=[IE], 577=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented(IE)
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Cross Domain");
    }

    /**
     * Test {576=[FF38, FF45], 577=[IE], 578=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE)
    public void ajax__jQuery_ajax_____JSONP___POST___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Cross Domain");
    }

    /**
     * Test {577=[FF38, FF45], 578=[IE], 579=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE)
    public void ajax__jQuery_ajax_____JSONP___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Cross Domain");
    }

    /**
     * Test {578=[FF38, FF45], 579=[IE], 580=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote");
    }

    /**
     * Test {579=[FF38, FF45], 580=[IE], 581=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____script__Remote_with_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with POST");
    }

    /**
     * Test {580=[FF38, FF45], 581=[IE], 582=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote_with_scheme_less_URL() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with scheme-less URL");
    }

    /**
     * Test {581=[FF38, FF45], 582=[IE], 583=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____malformed_JSON() throws Exception {
        runTest("ajax: jQuery.ajax() - malformed JSON");
    }

    /**
     * Test {582=[FF38, FF45], 583=[IE], 584=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - script by content-type");
    }

    /**
     * Test {583=[FF38, FF45], 584=[IE], 585=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____JSON_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type");
    }

    /**
     * Test {584=[FF38, FF45], 585=[IE], 586=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax_____JSON_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type disabled with options");
    }

    /**
     * Test {585=[FF38, FF45], 586=[IE], 587=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____simple_get() throws Exception {
        runTest("ajax: jQuery.ajax() - simple get");
    }

    /**
     * Test {586=[FF38, FF45], 587=[IE], 588=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____simple_post() throws Exception {
        runTest("ajax: jQuery.ajax() - simple post");
    }

    /**
     * Test {587=[FF38, FF45], 588=[IE], 589=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____data_option___empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - data option - empty bodies for non-GET requests");
    }

    /**
     * Test {588=[FF38, FF45], 589=[IE], 590=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (cache)");
    }

    /**
     * Test {589=[FF38, FF45], 590=[IE], 591=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (cache)");
    }

    /**
     * Test {590=[FF38, FF45], 591=[IE], 592=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (no cache)");
    }

    /**
     * Test {591=[FF38, FF45], 592=[IE], 593=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (no cache)");
    }

    /**
     * Test {592=[FF38, FF45], 593=[IE], 594=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____failing_cross_domain__non_existing_() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain (non-existing)");
    }

    /**
     * Test {593=[FF38, FF45], 594=[IE], 595=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____failing_cross_domain() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain");
    }

    /**
     * Test {594=[FF38, FF45], 595=[IE], 596=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____atom_xml() throws Exception {
        runTest("ajax: jQuery.ajax() - atom+xml");
    }

    /**
     * Test {595=[FF38, FF45], 596=[IE], 597=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____statusText() throws Exception {
        runTest("ajax: jQuery.ajax() - statusText");
    }

    /**
     * Test {596=[FF38, FF45], 597=[IE], 598=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void ajax__jQuery_ajax_____statusCode() throws Exception {
        runTest("ajax: jQuery.ajax() - statusCode");
    }

    /**
     * Test {597=[FF38, FF45], 598=[IE], 599=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax() - transitive conversions");
    }

    /**
     * Test {598=[FF38, FF45], 599=[IE], 600=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax() - overrideMimeType");
    }

    /**
     * Test {599=[FF38, FF45], 600=[IE], 601=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____empty_json_gets_to_error_callback_instead_of_success_callback_() throws Exception {
        runTest("ajax: jQuery.ajax() - empty json gets to error callback instead of success callback.");
    }

    /**
     * Test {600=[FF38, FF45], 601=[IE], 602=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___2688___jQuery_ajax_____beforeSend__cancel_request() throws Exception {
        runTest("ajax: #2688 - jQuery.ajax() - beforeSend, cancel request");
    }

    /**
     * Test {601=[FF38, FF45], 602=[IE], 603=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___2806___jQuery_ajax_____data_option___evaluate_function_values() throws Exception {
        runTest("ajax: #2806 - jQuery.ajax() - data option - evaluate function values");
    }

    /**
     * Test {602=[FF38, FF45], 603=[IE], 604=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___7531___jQuery_ajax_____Location_object_as_url() throws Exception {
        runTest("ajax: #7531 - jQuery.ajax() - Location object as url");
    }

    /**
     * Test {603=[FF38, FF45], 604=[IE], 605=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Same_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Same Domain");
    }

    /**
     * Test {604=[FF38, FF45], 605=[IE], 606=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Cross_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Cross Domain");
    }

    /**
     * Test {605=[FF38, FF45], 606=[IE], 607=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___8107___jQuery_ajax_____multiple_method_signatures_introduced_in_1_5() throws Exception {
        runTest("ajax: #8107 - jQuery.ajax() - multiple method signatures introduced in 1.5");
    }

    /**
     * Test {606=[FF38, FF45], 607=[IE], 608=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Same_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Same Domain");
    }

    /**
     * Test {607=[FF38, FF45], 608=[IE], 609=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented(IE)
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Cross_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Cross Domain");
    }

    /**
     * Test {608=[FF38, FF45], 609=[IE], 610=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___9887___jQuery_ajax_____Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: #9887 - jQuery.ajax() - Context with circular references (#9887)");
    }

    /**
     * Test {609=[FF38, FF45], 610=[IE], 611=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___10093___jQuery_ajax_____falsy_url_as_argument() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url as argument");
    }

    /**
     * Test {610=[FF38, FF45], 611=[IE], 612=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___10093___jQuery_ajax_____falsy_url_in_settings_object() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url in settings object");
    }

    /**
     * Test {611=[FF38, FF45], 612=[IE], 613=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___11151___jQuery_ajax_____parse_error_body() throws Exception {
        runTest("ajax: #11151 - jQuery.ajax() - parse error body");
    }

    /**
     * Test {612=[FF38, FF45], 613=[IE], 614=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___11426___jQuery_ajax_____loading_binary_data_shouldn_t_throw_an_exception_in_IE() throws Exception {
        runTest("ajax: #11426 - jQuery.ajax() - loading binary data shouldn't throw an exception in IE");
    }

    /**
     * Test {613=[FF38, FF45], 614=[IE], 615=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___11743___jQuery_ajax_____script__throws_exception() throws Exception {
        runTest("ajax: #11743 - jQuery.ajax() - script, throws exception");
    }

    /**
     * Test {614=[FF38, FF45], 615=[IE], 616=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___method_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - method set globally");
    }

    /**
     * Test {615=[FF38, FF45], 616=[IE], 617=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___type_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - type set globally");
    }

    /**
     * Test {616=[FF38, FF45], 617=[IE], 618=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___13276___jQuery_ajax_____compatibility_between_XML_documents_from_ajax_requests_and_parsed_string() throws Exception {
        runTest("ajax: #13276 - jQuery.ajax() - compatibility between XML documents from ajax requests and parsed string");
    }

    /**
     * Test {617=[FF38, FF45], 618=[IE], 619=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___13292___jQuery_ajax_____converter_is_bypassed_for_204_requests() throws Exception {
        runTest("ajax: #13292 - jQuery.ajax() - converter is bypassed for 204 requests");
    }

    /**
     * Test {618=[FF38, FF45], 619=[IE], 620=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___13388___jQuery_ajax_____responseXML() throws Exception {
        runTest("ajax: #13388 - jQuery.ajax() - responseXML");
    }

    /**
     * Test {619=[FF38, FF45], 620=[IE], 621=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___13922___jQuery_ajax_____converter_is_bypassed_for_HEAD_requests() throws Exception {
        runTest("ajax: #13922 - jQuery.ajax() - converter is bypassed for HEAD requests");
    }

    /**
     * Test {620=[FF38, FF45], 621=[IE], 622=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1, 0, 1")
    public void ajax___13240___jQuery_ajax_____support_non_RFC2616_methods() throws Exception {
        runTest("ajax: #13240 - jQuery.ajax() - support non-RFC2616 methods");
    }

    /**
     * Test {621=[FF38, FF45], 622=[IE], 623=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___14379___jQuery_ajax___on_unload() throws Exception {
        runTest("ajax: #14379 - jQuery.ajax() on unload");
    }

    /**
     * Test {622=[FF38, FF45], 623=[IE], 624=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxPrefilter_____abort() throws Exception {
        runTest("ajax: jQuery.ajaxPrefilter() - abort");
    }

    /**
     * Test {623=[FF38, FF45], 624=[IE], 625=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxSetup__() throws Exception {
        runTest("ajax: jQuery.ajaxSetup()");
    }

    /**
     * Test {624=[FF38, FF45], 625=[IE], 626=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void ajax__jQuery_ajaxSetup___timeout__Number______with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) - with global timeout");
    }

    /**
     * Test {625=[FF38, FF45], 626=[IE], 627=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxSetup___timeout__Number____with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) with localtimeout");
    }

    /**
     * Test {626=[FF38, FF45], 627=[IE], 628=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___11264___jQuery_domManip_____no_side_effect_because_of_ajaxSetup_or_global_events() throws Exception {
        runTest("ajax: #11264 - jQuery.domManip() - no side effect because of ajaxSetup or global events");
    }

    /**
     * Test {627=[FF38, FF45], 628=[IE], 629=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___11402___jQuery_domManip_____script_in_comments_are_properly_evaluated() throws Exception {
        runTest("ajax: #11402 - jQuery.domManip() - script in comments are properly evaluated");
    }

    /**
     * Test {628=[FF38, FF45], 629=[IE], 630=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_get__String__Hash__Function_____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get( String, Hash, Function ) - parse xml and use text() on nodes");
    }

    /**
     * Test {629=[FF38, FF45], 630=[IE], 631=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___8277___jQuery_get__String__Function_____data_in_ajaxSettings() throws Exception {
        runTest("ajax: #8277 - jQuery.get( String, Function ) - data in ajaxSettings");
    }

    /**
     * Test {630=[FF38, FF45], 631=[IE], 632=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_getJSON__String__Hash__Function_____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Hash, Function ) - JSON array");
    }

    /**
     * Test {631=[FF38, FF45], 632=[IE], 633=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object");
    }

    /**
     * Test {632=[FF38, FF45], 633=[IE], 634=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_____Using_Native_JSON() throws Exception {
        runTest("ajax: jQuery.getJSON() - Using Native JSON");
    }

    /**
     * Test {633=[FF38, FF45], 634=[IE], 635=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object with absolute url to local content");
    }

    /**
     * Test {634=[FF38, FF45], 635=[IE], 636=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getScript__String__Function_____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - with callback");
    }

    /**
     * Test {635=[FF38, FF45], 636=[IE], 637=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_getScript__String__Function_____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - no callback");
    }

    /**
     * Test {636=[FF38, FF45], 637=[IE], 638=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___8082___jQuery_getScript__String__Function_____source_as_responseText() throws Exception {
        runTest("ajax: #8082 - jQuery.getScript( String, Function ) - source as responseText");
    }

    /**
     * Test {637=[FF38, FF45], 638=[IE], 639=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__() throws Exception {
        runTest("ajax: jQuery.fn.load( String )");
    }

    /**
     * Test {638=[FF38, FF45], 639=[IE], 640=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_fn_load_____404_error_callbacks() throws Exception {
        runTest("ajax: jQuery.fn.load() - 404 error callbacks");
    }

    /**
     * Test {639=[FF38, FF45], 640=[IE], 641=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__null__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, null )");
    }

    /**
     * Test {640=[FF38, FF45], 641=[IE], 642=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__undefined__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, undefined )");
    }

    /**
     * Test {641=[FF38, FF45], 642=[IE], 643=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_fn_load__URL_SELECTOR__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR )");
    }

    /**
     * Test {642=[FF38, FF45], 643=[IE], 644=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_fn_load__URL_SELECTOR_with_spaces__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR with spaces )");
    }

    /**
     * Test {643=[FF38, FF45], 644=[IE], 645=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__Function_____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - simple: inject text into DOM");
    }

    /**
     * Test {644=[FF38, FF45], 645=[IE], 646=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    @NotYetImplemented
    public void ajax__jQuery_fn_load__String__Function_____check_scripts() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check scripts");
    }

    /**
     * Test {645=[FF38, FF45], 646=[IE], 647=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_fn_load__String__Function_____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check file with only a script tag");
    }

    /**
     * Test {646=[FF38, FF45], 647=[IE], 648=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__Function_____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - dataFilter in ajaxSettings");
    }

    /**
     * Test {647=[FF38, FF45], 648=[IE], 649=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__Object__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Object, Function )");
    }

    /**
     * Test {648=[FF38, FF45], 649=[IE], 650=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_fn_load__String__String__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, String, Function )");
    }

    /**
     * Test {649=[FF38, FF45], 650=[IE], 651=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_fn_load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: jQuery.fn.load() - callbacks get the correct parameters");
    }

    /**
     * Test {650=[FF38, FF45], 651=[IE], 652=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___2046___jQuery_fn_load__String__Function___with_ajaxSetup_on_dataType_json() throws Exception {
        runTest("ajax: #2046 - jQuery.fn.load( String, Function ) with ajaxSetup on dataType json");
    }

    /**
     * Test {651=[FF38, FF45], 652=[IE], 653=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___10524___jQuery_fn_load_____data_specified_in_ajaxSettings_is_merged_in() throws Exception {
        runTest("ajax: #10524 - jQuery.fn.load() - data specified in ajaxSettings is merged in");
    }

    /**
     * Test {652=[FF38, FF45], 653=[IE], 654=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_post_____data() throws Exception {
        runTest("ajax: jQuery.post() - data");
    }

    /**
     * Test {653=[FF38, FF45], 654=[IE], 655=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_post__String__Hash__Function_____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post( String, Hash, Function ) - simple with xml");
    }

    /**
     * Test {654=[FF38, FF45], 655=[IE], 656=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_active() throws Exception {
        runTest("ajax: jQuery.active");
    }

    /**
     * Test {655=[FF38, FF45], 656=[IE], 657=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__sanity_check() throws Exception {
        runTest("effects: sanity check");
    }

    /**
     * Test {656=[FF38, FF45], 657=[IE], 658=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__show___basic() throws Exception {
        runTest("effects: show() basic");
    }

    /**
     * Test {657=[FF38, FF45], 658=[IE], 659=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void effects__show__() throws Exception {
        runTest("effects: show()");
    }

    /**
     * Test {658=[FF38, FF45], 659=[IE], 660=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__show_Number____other_displays() throws Exception {
        runTest("effects: show(Number) - other displays");
    }

    /**
     * Test {659=[FF38, FF45], 660=[IE], 661=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__Persist_correct_display_value() throws Exception {
        runTest("effects: Persist correct display value");
    }

    /**
     * Test {660=[FF38, FF45], 661=[IE], 662=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_Hash__Object__Function_() throws Exception {
        runTest("effects: animate(Hash, Object, Function)");
    }

    /**
     * Test {661=[FF38, FF45], 662=[IE], 663=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    @NotYetImplemented
    public void effects__animate_relative_values() throws Exception {
        runTest("effects: animate relative values");
    }

    /**
     * Test {662=[FF38, FF45], 663=[IE], 664=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_height() throws Exception {
        runTest("effects: animate negative height");
    }

    /**
     * Test {663=[FF38, FF45], 664=[IE], 665=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin() throws Exception {
        runTest("effects: animate negative margin");
    }

    /**
     * Test {664=[FF38, FF45], 665=[IE], 666=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin_with_px() throws Exception {
        runTest("effects: animate negative margin with px");
    }

    /**
     * Test {665=[FF38, FF45], 666=[IE], 667=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void effects__animate_negative_padding() throws Exception {
        runTest("effects: animate negative padding");
    }

    /**
     * Test {666=[FF38, FF45], 667=[IE], 668=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__animate_block_as_inline_width_height() throws Exception {
        runTest("effects: animate block as inline width/height");
    }

    /**
     * Test {667=[FF38, FF45], 668=[IE], 669=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__animate_native_inline_width_height() throws Exception {
        runTest("effects: animate native inline width/height");
    }

    /**
     * Test {668=[FF38, FF45], 669=[IE], 670=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__animate_block_width_height() throws Exception {
        runTest("effects: animate block width/height");
    }

    /**
     * Test {669=[FF38, FF45], 670=[IE], 671=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_table_width_height() throws Exception {
        runTest("effects: animate table width/height");
    }

    /**
     * Test {670=[FF38, FF45], 671=[IE], 672=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void effects__animate_table_row_width_height() throws Exception {
        runTest("effects: animate table-row width/height");
    }

    /**
     * Test {671=[FF38, FF45], 672=[IE], 673=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void effects__animate_table_cell_width_height() throws Exception {
        runTest("effects: animate table-cell width/height");
    }

    /**
     * Test {672=[FF38, FF45], 673=[IE], 674=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_percentage____on_width_height() throws Exception {
        runTest("effects: animate percentage(%) on width/height");
    }

    /**
     * Test {673=[FF38, FF45], 674=[IE], 675=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_resets_overflow_x_and_overflow_y_when_finished() throws Exception {
        runTest("effects: animate resets overflow-x and overflow-y when finished");
    }

    /**
     * Test {674=[FF38, FF45], 675=[IE], 676=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__false__() throws Exception {
        runTest("effects: animate option { queue: false }");
    }

    /**
     * Test {675=[FF38, FF45], 676=[IE], 677=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__true__() throws Exception {
        runTest("effects: animate option { queue: true }");
    }

    /**
     * Test {676=[FF38, FF45], 677=[IE], 678=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_option___queue___name___() throws Exception {
        runTest("effects: animate option { queue: 'name' }");
    }

    /**
     * Test {677=[FF38, FF45], 678=[IE], 679=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_with_no_properties() throws Exception {
        runTest("effects: animate with no properties");
    }

    /**
     * Test {678=[FF38, FF45], 679=[IE], 680=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_duration_0() throws Exception {
        runTest("effects: animate duration 0");
    }

    /**
     * Test {679=[FF38, FF45], 680=[IE], 681=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_hyphenated_properties() throws Exception {
        runTest("effects: animate hyphenated properties");
    }

    /**
     * Test {680=[FF38, FF45], 681=[IE], 682=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_non_element() throws Exception {
        runTest("effects: animate non-element");
    }

    /**
     * Test {681=[FF38, FF45], 682=[IE], 683=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop__() throws Exception {
        runTest("effects: stop()");
    }

    /**
     * Test {682=[FF38, FF45], 683=[IE], 684=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__stop_____several_in_queue() throws Exception {
        runTest("effects: stop() - several in queue");
    }

    /**
     * Test {683=[FF38, FF45], 684=[IE], 685=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop_clearQueue_() throws Exception {
        runTest("effects: stop(clearQueue)");
    }

    /**
     * Test {684=[FF38, FF45], 685=[IE], 686=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__stop_clearQueue__gotoEnd_() throws Exception {
        runTest("effects: stop(clearQueue, gotoEnd)");
    }

    /**
     * Test {685=[FF38, FF45], 686=[IE], 687=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop__queue_______________Stop_single_queues() throws Exception {
        runTest("effects: stop( queue, ..., ... ) - Stop single queues");
    }

    /**
     * Test {686=[FF38, FF45], 687=[IE], 688=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__toggle__() throws Exception {
        runTest("effects: toggle()");
    }

    /**
     * Test {687=[FF38, FF45], 688=[IE], 689=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void effects__jQuery_fx_prototype_cur______1_8_Back_Compat() throws Exception {
        runTest("effects: jQuery.fx.prototype.cur() - <1.8 Back Compat");
    }

    /**
     * Test {688=[FF38, FF45], 689=[IE], 690=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__Overflow_and_Display() throws Exception {
        runTest("effects: Overflow and Display");
    }

    /**
     * Test {689=[FF38, FF45], 690=[IE], 691=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_0() throws Exception {
        runTest("effects: CSS Auto to 0");
    }

    /**
     * Test {690=[FF38, FF45], 691=[IE], 692=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_50() throws Exception {
        runTest("effects: CSS Auto to 50");
    }

    /**
     * Test {691=[FF38, FF45], 692=[IE], 693=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_100() throws Exception {
        runTest("effects: CSS Auto to 100");
    }

    /**
     * Test {692=[FF38, FF45], 693=[IE], 694=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    @NotYetImplemented
    public void effects__CSS_Auto_to_show() throws Exception {
        runTest("effects: CSS Auto to show");
    }

    /**
     * Test {693=[FF38, FF45], 694=[IE], 695=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_Auto_to_hide() throws Exception {
        runTest("effects: CSS Auto to hide");
    }

    /**
     * Test {694=[FF38, FF45], 695=[IE], 696=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_0() throws Exception {
        runTest("effects: JS Auto to 0");
    }

    /**
     * Test {695=[FF38, FF45], 696=[IE], 697=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_50() throws Exception {
        runTest("effects: JS Auto to 50");
    }

    /**
     * Test {696=[FF38, FF45], 697=[IE], 698=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_100() throws Exception {
        runTest("effects: JS Auto to 100");
    }

    /**
     * Test {697=[FF38, FF45], 698=[IE], 699=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    @NotYetImplemented
    public void effects__JS_Auto_to_show() throws Exception {
        runTest("effects: JS Auto to show");
    }

    /**
     * Test {698=[FF38, FF45], 699=[IE], 700=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_Auto_to_hide() throws Exception {
        runTest("effects: JS Auto to hide");
    }

    /**
     * Test {699=[FF38, FF45], 700=[IE], 701=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_0() throws Exception {
        runTest("effects: CSS 100 to 0");
    }

    /**
     * Test {700=[FF38, FF45], 701=[IE], 702=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_50() throws Exception {
        runTest("effects: CSS 100 to 50");
    }

    /**
     * Test {701=[FF38, FF45], 702=[IE], 703=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_100() throws Exception {
        runTest("effects: CSS 100 to 100");
    }

    /**
     * Test {702=[FF38, FF45], 703=[IE], 704=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_100_to_show() throws Exception {
        runTest("effects: CSS 100 to show");
    }

    /**
     * Test {703=[FF38, FF45], 704=[IE], 705=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_100_to_hide() throws Exception {
        runTest("effects: CSS 100 to hide");
    }

    /**
     * Test {704=[FF38, FF45], 705=[IE], 706=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_0() throws Exception {
        runTest("effects: JS 100 to 0");
    }

    /**
     * Test {705=[FF38, FF45], 706=[IE], 707=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_50() throws Exception {
        runTest("effects: JS 100 to 50");
    }

    /**
     * Test {706=[FF38, FF45], 707=[IE], 708=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_100() throws Exception {
        runTest("effects: JS 100 to 100");
    }

    /**
     * Test {707=[FF38, FF45], 708=[IE], 709=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_100_to_show() throws Exception {
        runTest("effects: JS 100 to show");
    }

    /**
     * Test {708=[FF38, FF45], 709=[IE], 710=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_100_to_hide() throws Exception {
        runTest("effects: JS 100 to hide");
    }

    /**
     * Test {709=[FF38, FF45], 710=[IE], 711=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_0() throws Exception {
        runTest("effects: CSS 50 to 0");
    }

    /**
     * Test {710=[FF38, FF45], 711=[IE], 712=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_50() throws Exception {
        runTest("effects: CSS 50 to 50");
    }

    /**
     * Test {711=[FF38, FF45], 712=[IE], 713=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_100() throws Exception {
        runTest("effects: CSS 50 to 100");
    }

    /**
     * Test {712=[FF38, FF45], 713=[IE], 714=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_50_to_show() throws Exception {
        runTest("effects: CSS 50 to show");
    }

    /**
     * Test {713=[FF38, FF45], 714=[IE], 715=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_50_to_hide() throws Exception {
        runTest("effects: CSS 50 to hide");
    }

    /**
     * Test {714=[FF38, FF45], 715=[IE], 716=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_0() throws Exception {
        runTest("effects: JS 50 to 0");
    }

    /**
     * Test {715=[FF38, FF45], 716=[IE], 717=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_50() throws Exception {
        runTest("effects: JS 50 to 50");
    }

    /**
     * Test {716=[FF38, FF45], 717=[IE], 718=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_100() throws Exception {
        runTest("effects: JS 50 to 100");
    }

    /**
     * Test {717=[FF38, FF45], 718=[IE], 719=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_50_to_show() throws Exception {
        runTest("effects: JS 50 to show");
    }

    /**
     * Test {718=[FF38, FF45], 719=[IE], 720=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_50_to_hide() throws Exception {
        runTest("effects: JS 50 to hide");
    }

    /**
     * Test {719=[FF38, FF45], 720=[IE], 721=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_0() throws Exception {
        runTest("effects: CSS 0 to 0");
    }

    /**
     * Test {720=[FF38, FF45], 721=[IE], 722=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_50() throws Exception {
        runTest("effects: CSS 0 to 50");
    }

    /**
     * Test {721=[FF38, FF45], 722=[IE], 723=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_100() throws Exception {
        runTest("effects: CSS 0 to 100");
    }

    /**
     * Test {722=[FF38, FF45], 723=[IE], 724=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_0_to_show() throws Exception {
        runTest("effects: CSS 0 to show");
    }

    /**
     * Test {723=[FF38, FF45], 724=[IE], 725=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_0_to_hide() throws Exception {
        runTest("effects: CSS 0 to hide");
    }

    /**
     * Test {724=[FF38, FF45], 725=[IE], 726=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_0() throws Exception {
        runTest("effects: JS 0 to 0");
    }

    /**
     * Test {725=[FF38, FF45], 726=[IE], 727=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_50() throws Exception {
        runTest("effects: JS 0 to 50");
    }

    /**
     * Test {726=[FF38, FF45], 727=[IE], 728=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_100() throws Exception {
        runTest("effects: JS 0 to 100");
    }

    /**
     * Test {727=[FF38, FF45], 728=[IE], 729=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_0_to_show() throws Exception {
        runTest("effects: JS 0 to show");
    }

    /**
     * Test {728=[FF38, FF45], 729=[IE], 730=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_0_to_hide() throws Exception {
        runTest("effects: JS 0 to hide");
    }

    /**
     * Test {729=[FF38, FF45], 730=[IE], 731=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void effects__Effects_chaining() throws Exception {
        runTest("effects: Effects chaining");
    }

    /**
     * Test {730=[FF38, FF45], 731=[IE], 732=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_show__fast___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("effects: jQuery.show('fast') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {731=[FF38, FF45], 732=[IE], 733=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void effects__interrupt_toggle() throws Exception {
        runTest("effects: interrupt toggle");
    }

    /**
     * Test {732=[FF38, FF45], 733=[IE], 734=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_with_per_property_easing() throws Exception {
        runTest("effects: animate with per-property easing");
    }

    /**
     * Test {733=[FF38, FF45], 734=[IE], 735=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_with_CSS_shorthand_properties() throws Exception {
        runTest("effects: animate with CSS shorthand properties");
    }

    /**
     * Test {734=[FF38, FF45], 735=[IE], 736=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_hidden_elements__with_animation__bug__7141_() throws Exception {
        runTest("effects: hide hidden elements, with animation (bug #7141)");
    }

    /**
     * Test {735=[FF38, FF45], 736=[IE], 737=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_unit_less_properties___4966_() throws Exception {
        runTest("effects: animate unit-less properties (#4966)");
    }

    /**
     * Test {736=[FF38, FF45], 737=[IE], 738=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__animate_properties_missing_px_w__opacity_as_last___9074_() throws Exception {
        runTest("effects: animate properties missing px w/ opacity as last (#9074)");
    }

    /**
     * Test {737=[FF38, FF45], 738=[IE], 739=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__callbacks_should_fire_in_correct_order___9100_() throws Exception {
        runTest("effects: callbacks should fire in correct order (#9100)");
    }

    /**
     * Test {738=[FF38, FF45], 739=[IE], 740=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__callbacks_that_throw_exceptions_will_be_removed___5684_() throws Exception {
        runTest("effects: callbacks that throw exceptions will be removed (#5684)");
    }

    /**
     * Test {739=[FF38, FF45], 740=[IE], 741=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_will_scale_margin_properties_individually() throws Exception {
        runTest("effects: animate will scale margin properties individually");
    }

    /**
     * Test {740=[FF38, FF45], 741=[IE], 742=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("effects: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {741=[FF38, FF45], 742=[IE], 743=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    @NotYetImplemented
    public void effects__line_height_animates_correctly___13855_() throws Exception {
        runTest("effects: line-height animates correctly (#13855)");
    }

    /**
     * Test {742=[FF38, FF45], 743=[IE], 744=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_Animation__object__props__opts__() throws Exception {
        runTest("effects: jQuery.Animation( object, props, opts )");
    }

    /**
     * Test {743=[FF38, FF45], 744=[IE], 745=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animate_Option__step__function__percent__tween__() throws Exception {
        runTest("effects: Animate Option: step: function( percent, tween )");
    }

    /**
     * Test {744=[FF38, FF45], 745=[IE], 746=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__Animate_callbacks_have_correct_context() throws Exception {
        runTest("effects: Animate callbacks have correct context");
    }

    /**
     * Test {745=[FF38, FF45], 746=[IE], 747=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__User_supplied_callback_called_after_show_when_fx_off___8892_() throws Exception {
        runTest("effects: User supplied callback called after show when fx off (#8892)");
    }

    /**
     * Test {746=[FF38, FF45], 747=[IE], 748=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void effects__animate_should_set_display_for_disconnected_nodes() throws Exception {
        runTest("effects: animate should set display for disconnected nodes");
    }

    /**
     * Test {747=[FF38, FF45], 748=[IE], 749=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animation_callback_should_not_show_animated_element_as__animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as :animated (#7157)");
    }

    /**
     * Test {748=[FF38, FF45], 749=[IE], 750=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Initial_step_callback_should_show_element_as__animated___14623_() throws Exception {
        runTest("effects: Initial step callback should show element as :animated (#14623)");
    }

    /**
     * Test {749=[FF38, FF45], 750=[IE], 751=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_called_on_element_within_hidden_parent_should_set_display_to_none___10045_() throws Exception {
        runTest("effects: hide called on element within hidden parent should set display to none (#10045)");
    }

    /**
     * Test {750=[FF38, FF45], 751=[IE], 752=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__hide__fadeOut_and_slideUp_called_on_element_width_height_and_width___0_should_set_display_to_none() throws Exception {
        runTest("effects: hide, fadeOut and slideUp called on element width height and width = 0 should set display to none");
    }

    /**
     * Test {751=[FF38, FF45], 752=[IE], 753=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__hide_should_not_leave_hidden_inline_elements_visible___14848_() throws Exception {
        runTest("effects: hide should not leave hidden inline elements visible (#14848)");
    }

    /**
     * Test {752=[FF38, FF45], 753=[IE], 754=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void effects__Handle_queue_false_promises() throws Exception {
        runTest("effects: Handle queue:false promises");
    }

    /**
     * Test {753=[FF38, FF45], 754=[IE], 755=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__multiple_unqueued_and_promise() throws Exception {
        runTest("effects: multiple unqueued and promise");
    }

    /**
     * Test {754=[FF38, FF45], 755=[IE], 756=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void effects__animate_does_not_change_start_value_for_non_px_animation___7109_() throws Exception {
        runTest("effects: animate does not change start value for non-px animation (#7109)");
    }

    /**
     * Test {755=[FF38, FF45], 756=[IE], 757=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "0, 1, 1",
        CHROME = "0, 2, 2")
    @NotYetImplemented(IE)
    public void effects__non_px_animation_handles_non_numeric_start___11971_() throws Exception {
        runTest("effects: non-px animation handles non-numeric start (#11971)");
    }

    /**
     * Test {756=[FF38, FF45], 757=[IE], 758=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__Animation_callbacks___11797_() throws Exception {
        runTest("effects: Animation callbacks (#11797)");
    }

    /**
     * Test {757=[FF38, FF45], 758=[IE], 759=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__Animate_properly_sets_overflow_hidden_when_animating_width_height___12117_() throws Exception {
        runTest("effects: Animate properly sets overflow hidden when animating width/height (#12117)");
    }

    /**
     * Test {758=[FF38, FF45], 759=[IE], 760=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__Each_tick_of_the_timer_loop_uses_a_fresh_time___12837_() throws Exception {
        runTest("effects: Each tick of the timer loop uses a fresh time (#12837)");
    }

    /**
     * Test {759=[FF38, FF45], 760=[IE], 761=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animations_with_0_duration_don_t_ease___12273_() throws Exception {
        runTest("effects: Animations with 0 duration don't ease (#12273)");
    }

    /**
     * Test {760=[FF38, FF45], 761=[IE], 762=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__toggle_state_tests__toggle___8685_() throws Exception {
        runTest("effects: toggle state tests: toggle (#8685)");
    }

    /**
     * Test {761=[FF38, FF45], 762=[IE], 763=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__toggle_state_tests__slideToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: slideToggle (#8685)");
    }

    /**
     * Test {762=[FF38, FF45], 763=[IE], 764=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__toggle_state_tests__fadeToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: fadeToggle (#8685)");
    }

    /**
     * Test {763=[FF38, FF45], 764=[IE], 765=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__jQuery_fx_start___jQuery_fx_stop_hook_points() throws Exception {
        runTest("effects: jQuery.fx.start & jQuery.fx.stop hook points");
    }

    /**
     * Test {764=[FF38, FF45], 765=[IE], 766=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects___finish___completes_all_queued_animations() throws Exception {
        runTest("effects: .finish() completes all queued animations");
    }

    /**
     * Test {765=[FF38, FF45], 766=[IE], 767=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void effects___finish__false_____unqueued_animations() throws Exception {
        runTest("effects: .finish( false ) - unqueued animations");
    }

    /**
     * Test {766=[FF38, FF45], 767=[IE], 768=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects___finish___custom______custom_queue_animations() throws Exception {
        runTest("effects: .finish( \"custom\" ) - custom queue animations");
    }

    /**
     * Test {767=[FF38, FF45], 768=[IE], 769=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects___finish___calls_finish_of_custom_queue_functions() throws Exception {
        runTest("effects: .finish() calls finish of custom queue functions");
    }

    /**
     * Test {768=[FF38, FF45], 769=[IE], 770=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects___finish___is_applied_correctly_when_multiple_elements_were_animated___13937_() throws Exception {
        runTest("effects: .finish() is applied correctly when multiple elements were animated (#13937)");
    }

    /**
     * Test {769=[FF38, FF45], 770=[IE], 771=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__slideDown___after_stop_____13483_() throws Exception {
        runTest("effects: slideDown() after stop() (#13483)");
    }

    /**
     * Test {770=[FF38, FF45], 771=[IE], 772=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__Respect_display_value_on_inline_elements___14824_() throws Exception {
        runTest("effects: Respect display value on inline elements (#14824)");
    }

    /**
     * Test {771=[FF38, FF45], 772=[IE], 773=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__empty_set() throws Exception {
        runTest("offset: empty set");
    }

    /**
     * Test {772=[FF38, FF45], 773=[IE], 774=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__object_without_getBoundingClientRect() throws Exception {
        runTest("offset: object without getBoundingClientRect");
    }

    /**
     * Test {773=[FF38, FF45], 774=[IE], 775=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__disconnected_node() throws Exception {
        runTest("offset: disconnected node");
    }

    /**
     * Test {775=[FF38, FF45], 776=[IE], 777=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 178, 178")
    @NotYetImplemented
    public void offset__absolute() throws Exception {
        runTest("offset: absolute");
    }

    /**
     * Test {776=[FF38, FF45], 777=[IE], 778=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 60, 60")
    @NotYetImplemented
    public void offset__relative() throws Exception {
        runTest("offset: relative");
    }

    /**
     * Test {777=[FF38, FF45], 778=[IE], 779=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 80, 80")
    @NotYetImplemented(IE)
    public void offset__static() throws Exception {
        runTest("offset: static");
    }

    /**
     * Test {778=[FF38, FF45], 779=[IE], 780=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 34, 34")
    public void offset__fixed() throws Exception {
        runTest("offset: fixed");
    }

    /**
     * Test {779=[FF38, FF45], 780=[IE], 781=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @NotYetImplemented
    public void offset__table() throws Exception {
        runTest("offset: table");
    }

    /**
     * Test {780=[FF38, FF45], 781=[IE], 782=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    @NotYetImplemented
    public void offset__scroll() throws Exception {
        runTest("offset: scroll");
    }

    /**
     * Test {781=[FF38, FF45], 782=[IE], 783=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @NotYetImplemented
    public void offset__body() throws Exception {
        runTest("offset: body");
    }

    /**
     * Test {782=[FF38, FF45], 783=[IE], 784=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void offset__chaining() throws Exception {
        runTest("offset: chaining");
    }

    /**
     * Test {783=[FF38, FF45], 784=[IE], 785=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void offset__offsetParent() throws Exception {
        runTest("offset: offsetParent");
    }

    /**
     * Test {784=[FF38, FF45], 785=[IE], 786=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "0, 2, 2",
        CHROME = "1, 1, 2")
    @NotYetImplemented(CHROME)
    // for some reason, real chrome div.getBoundingClientRect().top returns 999.53125
    // not able to reproduce it with simple case
    // According to https://github.com/jquery/jquery/issues/2590, this could be fixed in jQuery 1.12
    public void offset__fractions__see__7730_and__7885_() throws Exception {
        runTest("offset: fractions (see #7730 and #7885)");
    }

    /**
     * Test {785=[FF38, FF45], 786=[IE], 787=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width__() throws Exception {
        runTest("dimensions: width()");
    }

    /**
     * Test {786=[FF38, FF45], 787=[IE], 788=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width_Function_() throws Exception {
        runTest("dimensions: width(Function)");
    }

    /**
     * Test {787=[FF38, FF45], 788=[IE], 789=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__width_Function_args__() throws Exception {
        runTest("dimensions: width(Function(args))");
    }

    /**
     * Test {788=[FF38, FF45], 789=[IE], 790=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height__() throws Exception {
        runTest("dimensions: height()");
    }

    /**
     * Test {789=[FF38, FF45], 790=[IE], 791=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height_Function_() throws Exception {
        runTest("dimensions: height(Function)");
    }

    /**
     * Test {790=[FF38, FF45], 791=[IE], 792=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__height_Function_args__() throws Exception {
        runTest("dimensions: height(Function(args))");
    }

    /**
     * Test {791=[FF38, FF45], 792=[IE], 793=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerWidth__() throws Exception {
        runTest("dimensions: innerWidth()");
    }

    /**
     * Test {792=[FF38, FF45], 793=[IE], 794=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerHeight__() throws Exception {
        runTest("dimensions: innerHeight()");
    }

    /**
     * Test {793=[FF38, FF45], 794=[IE], 795=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerWidth__() throws Exception {
        runTest("dimensions: outerWidth()");
    }

    /**
     * Test {794=[FF38, FF45], 795=[IE], 796=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__9441__9300() throws Exception {
        runTest("dimensions: child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #9441 #9300");
    }

    /**
     * Test {795=[FF38, FF45], 796=[IE], 797=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void dimensions__getting_dimensions_shouldn_t_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldn't modify runtimeStyle see #9233");
    }

    /**
     * Test {796=[FF38, FF45], 797=[IE], 798=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__table_dimensions() throws Exception {
        runTest("dimensions: table dimensions");
    }

    /**
     * Test {797=[FF38, FF45], 798=[IE], 799=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    @NotYetImplemented
    public void dimensions__box_sizing_border_box_child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__10413() throws Exception {
        runTest("dimensions: box-sizing:border-box child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #10413");
    }

    /**
     * Test {798=[FF38, FF45], 799=[IE], 800=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerHeight__() throws Exception {
        runTest("dimensions: outerHeight()");
    }

    /**
     * Test {799=[FF38, FF45], 800=[IE], 801=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void dimensions__passing_undefined_is_a_setter__5571() throws Exception {
        runTest("dimensions: passing undefined is a setter #5571");
    }

    /**
     * Test {800=[FF38, FF45], 801=[IE], 802=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void dimensions__getters_on_non_elements_should_return_null() throws Exception {
        runTest("dimensions: getters on non elements should return null");
    }

    /**
     * Test {801=[FF38, FF45], 802=[IE], 803=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    @NotYetImplemented
    public void dimensions__setters_with_and_without_box_sizing_border_box() throws Exception {
        runTest("dimensions: setters with and without box-sizing:border-box");
    }

    /**
     * Test {802=[FF38, FF45], 803=[IE], 804=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void dimensions__window_vs__small_document() throws Exception {
        runTest("dimensions: window vs. small document");
    }

    /**
     * Test {803=[FF38, FF45], 804=[IE], 805=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__window_vs__large_document() throws Exception {
        runTest("dimensions: window vs. large document");
    }
}
