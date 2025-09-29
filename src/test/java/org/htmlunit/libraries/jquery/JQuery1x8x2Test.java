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
package org.htmlunit.libraries.jquery;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;

/**
 * Tests for compatibility with web server loading of
 * version 1.8.2 of the <a href="http://jquery.com/">jQuery</a> JavaScript library.
 *
 * All test method inside this class are generated. Please have a look
 * at {@link org.htmlunit.libraries.jquery.JQueryExtractor}.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
public class JQuery1x8x2Test extends JQueryTestBase {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return "1.8.2";
    }

    /**
     * Test {1=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__Unit_Testing_Environment() throws Exception {
        runTest("core: Unit Testing Environment");
    }

    /**
     * Test {2=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__Basic_requirements() throws Exception {
        runTest("core: Basic requirements");
    }

    /**
     * Test {3=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void core__jQuery__() throws Exception {
        runTest("core: jQuery()");
    }

    /**
     * Test {4=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void core__selector_state() throws Exception {
        runTest("core: selector state");
    }

    /**
     * Test {5=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__globalEval() throws Exception {
        runTest("core: globalEval");
    }

    /**
     * Test {6=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__noConflict() throws Exception {
        runTest("core: noConflict");
    }

    /**
     * Test {7=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void core__trim() throws Exception {
        runTest("core: trim");
    }

    /**
     * Test {8=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void core__type() throws Exception {
        runTest("core: type");
    }

    /**
     * Test {9=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void core__isPlainObject() throws Exception {
        runTest("core: isPlainObject");
    }

    /**
     * Test {10=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__isFunction() throws Exception {
        runTest("core: isFunction");
    }

    /**
     * Test {11=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 36, 36")
    public void core__isNumeric() throws Exception {
        runTest("core: isNumeric");
    }

    /**
     * Test {12=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__isXMLDoc___HTML() throws Exception {
        runTest("core: isXMLDoc - HTML");
    }

    /**
     * Test {13=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__XSS_via_location_hash() throws Exception {
        runTest("core: XSS via location.hash");
    }

    /**
     * Test {14=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__isXMLDoc___XML() throws Exception {
        runTest("core: isXMLDoc - XML");
    }

    /**
     * Test {15=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__isWindow() throws Exception {
        runTest("core: isWindow");
    }

    /**
     * Test {16=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery__html__() throws Exception {
        runTest("core: jQuery('html')");
    }

    /**
     * Test {17=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__jQuery__html___context_() throws Exception {
        runTest("core: jQuery('html', context)");
    }

    /**
     * Test {18=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_selector__xml__text_str____Loaded_via_XML_document() throws Exception {
        runTest("core: jQuery(selector, xml).text(str) - Loaded via XML document");
    }

    /**
     * Test {19=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__end__() throws Exception {
        runTest("core: end()");
    }

    /**
     * Test {20=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__length() throws Exception {
        runTest("core: length");
    }

    /**
     * Test {21=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__size__() throws Exception {
        runTest("core: size()");
    }

    /**
     * Test {22=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__get__() throws Exception {
        runTest("core: get()");
    }

    /**
     * Test {23=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__toArray__() throws Exception {
        runTest("core: toArray()");
    }

    /**
     * Test {24=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__inArray__() throws Exception {
        runTest("core: inArray()");
    }

    /**
     * Test {25=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get_Number_() throws Exception {
        runTest("core: get(Number)");
    }

    /**
     * Test {26=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get__Number_() throws Exception {
        runTest("core: get(-Number)");
    }

    /**
     * Test {27=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__each_Function_() throws Exception {
        runTest("core: each(Function)");
    }

    /**
     * Test {28=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__slice__() throws Exception {
        runTest("core: slice()");
    }

    /**
     * Test {29=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__first___last__() throws Exception {
        runTest("core: first()/last()");
    }

    /**
     * Test {30=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__map__() throws Exception {
        runTest("core: map()");
    }

    /**
     * Test {31=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_merge__() throws Exception {
        runTest("core: jQuery.merge()");
    }

    /**
     * Test {32=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__jQuery_extend_Object__Object_() throws Exception {
        runTest("core: jQuery.extend(Object, Object)");
    }

    /**
     * Test {33=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__jQuery_each_Object_Function_() throws Exception {
        runTest("core: jQuery.each(Object,Function)");
    }

    /**
     * Test {34=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void core__jQuery_makeArray() throws Exception {
        runTest("core: jQuery.makeArray");
    }

    /**
     * Test {35=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery_inArray() throws Exception {
        runTest("core: jQuery.inArray");
    }

    /**
     * Test {36=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_isEmptyObject() throws Exception {
        runTest("core: jQuery.isEmptyObject");
    }

    /**
     * Test {37=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_proxy() throws Exception {
        runTest("core: jQuery.proxy");
    }

    /**
     * Test {38=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void core__jQuery_parseHTML() throws Exception {
        runTest("core: jQuery.parseHTML");
    }

    /**
     * Test {39=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseJSON() throws Exception {
        runTest("core: jQuery.parseJSON");
    }

    /**
     * Test {40=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseXML() throws Exception {
        runTest("core: jQuery.parseXML");
    }

    /**
     * Test {41=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery_sub_____Static_Methods() throws Exception {
        runTest("core: jQuery.sub() - Static Methods");
    }

    /**
     * Test {42=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 378, 378")
    public void core__jQuery_sub______fn_Methods() throws Exception {
        runTest("core: jQuery.sub() - .fn Methods");
    }

    /**
     * Test {43=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_camelCase__() throws Exception {
        runTest("core: jQuery.camelCase()");
    }

    /**
     * Test {44=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - no filter");
    }

    /**
     * Test {45=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks__________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - no filter");
    }

    /**
     * Test {46=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - filter");
    }

    /**
     * Test {47=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks__________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - filter");
    }

    /**
     * Test {48=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - no filter");
    }

    /**
     * Test {49=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - no filter");
    }

    /**
     * Test {50=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - filter");
    }

    /**
     * Test {51=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - filter");
    }

    /**
     * Test {52=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - no filter");
    }

    /**
     * Test {53=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - no filter");
    }

    /**
     * Test {54=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - filter");
    }

    /**
     * Test {55=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - filter");
    }

    /**
     * Test {56=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - no filter");
    }

    /**
     * Test {57=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - no filter");
    }

    /**
     * Test {58=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - filter");
    }

    /**
     * Test {59=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - filter");
    }

    /**
     * Test {60=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - no filter");
    }

    /**
     * Test {61=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {62=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - filter");
    }

    /**
     * Test {63=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {64=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - no filter");
    }

    /**
     * Test {65=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - no filter");
    }

    /**
     * Test {66=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - filter");
    }

    /**
     * Test {67=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - filter");
    }

    /**
     * Test {68=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - no filter");
    }

    /**
     * Test {69=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {70=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - filter");
    }

    /**
     * Test {71=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {72=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - no filter");
    }

    /**
     * Test {73=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {74=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - filter");
    }

    /**
     * Test {75=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {76=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - no filter");
    }

    /**
     * Test {77=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {78=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - filter");
    }

    /**
     * Test {79=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {80=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - no filter");
    }

    /**
     * Test {81=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {82=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - filter");
    }

    /**
     * Test {83=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {84=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - no filter");
    }

    /**
     * Test {85=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {86=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - filter");
    }

    /**
     * Test {87=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {88=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks__options_____options_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks( options ) - options are copied");
    }

    /**
     * Test {89=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_fireWith___arguments_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks.fireWith - arguments are copied");
    }

    /**
     * Test {90=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_remove___should_remove_all_instances() throws Exception {
        runTest("callbacks: jQuery.Callbacks.remove - should remove all instances");
    }

    /**
     * Test {91=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_____adding_a_string_doesn_t_cause_a_stack_overflow() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow");
    }

    /**
     * Test {92=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred() throws Exception {
        runTest("deferred: jQuery.Deferred");
    }

    /**
     * Test {93=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred___new_operator() throws Exception {
        runTest("deferred: jQuery.Deferred - new operator");
    }

    /**
     * Test {94=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void deferred__jQuery_Deferred___chainability() throws Exception {
        runTest("deferred: jQuery.Deferred - chainability");
    }

    /**
     * Test {95=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (done)");
    }

    /**
     * Test {96=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (fail)");
    }

    /**
     * Test {97=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___filtering__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (progress)");
    }

    /**
     * Test {98=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (done)");
    }

    /**
     * Test {99=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (fail)");
    }

    /**
     * Test {100=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (progress)");
    }

    /**
     * Test {101=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___context() throws Exception {
        runTest("deferred: jQuery.Deferred.then - context");
    }

    /**
     * Test {102=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 34, 34")
    public void deferred__jQuery_when() throws Exception {
        runTest("deferred: jQuery.when");
    }

    /**
     * Test {103=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 119, 119")
    public void deferred__jQuery_when___joined() throws Exception {
        runTest("deferred: jQuery.when - joined");
    }

    /**
     * Test {104=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__boxModel() throws Exception {
        runTest("support: boxModel");
    }

    /**
     * Test {105=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void support__body_background_is_not_lost_if_set_prior_to_loading_jQuery___9238_() throws Exception {
        runTest("support: body background is not lost if set prior to loading jQuery (#9238)");
    }

    /**
     * Test {106=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__A_background_on_the_testElement_does_not_cause_IE8_to_crash___9823_() throws Exception {
        runTest("support: A background on the testElement does not cause IE8 to crash (#9823)");
    }

    /**
     * Test {107=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__expando() throws Exception {
        runTest("data: expando");
    }

    /**
     * Test {108=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 124, 124")
    public void data__jQuery_data() throws Exception {
        runTest("data: jQuery.data");
    }

    /**
     * Test {109=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void data__jQuery_acceptData() throws Exception {
        runTest("data: jQuery.acceptData");
    }

    /**
     * Test {110=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void data___data__() throws Exception {
        runTest("data: .data()");
    }

    /**
     * Test {111=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void data___data_String__and__data_String__Object_() throws Exception {
        runTest("data: .data(String) and .data(String, Object)");
    }

    /**
     * Test {112=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 40, 40")
    public void data__data___attributes() throws Exception {
        runTest("data: data-* attributes");
    }

    /**
     * Test {113=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data___data_Object_() throws Exception {
        runTest("data: .data(Object)");
    }

    /**
     * Test {114=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_removeData() throws Exception {
        runTest("data: jQuery.removeData");
    }

    /**
     * Test {115=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void data___removeData__() throws Exception {
        runTest("data: .removeData()");
    }

    /**
     * Test {116=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_serialization___8108_() throws Exception {
        runTest("data: JSON serialization (#8108)");
    }

    /**
     * Test {117=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: jQuery.data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {118=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__jQuery_data_should_not_miss_data_with_preset_hyphenated_property_names() throws Exception {
        runTest("data: jQuery.data should not miss data with preset hyphenated property names");
    }

    /**
     * Test {119=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void data__jQuery_data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {120=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void data__jQuery_data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: jQuery.data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {121=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__Triggering_the_removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: Triggering the removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {122=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__Only_check_element_attributes_once_when_calling__data______8909() throws Exception {
        runTest("data: Only check element attributes once when calling .data() - #8909");
    }

    /**
     * Test {123=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_data__attributes_can_have_newlines() throws Exception {
        runTest("data: JSON data- attributes can have newlines");
    }

    /**
     * Test {124=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void queue__queue___with_other_types() throws Exception {
        runTest("queue: queue() with other types");
    }

    /**
     * Test {125=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_name__passes_in_the_next_item_in_the_queue_as_a_parameter() throws Exception {
        runTest("queue: queue(name) passes in the next item in the queue as a parameter");
    }

    /**
     * Test {126=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__queue___passes_in_the_next_item_in_the_queue_as_a_parameter_to_fx_queues() throws Exception {
        runTest("queue: queue() passes in the next item in the queue as a parameter to fx queues");
    }

    /**
     * Test {127=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__callbacks_keep_their_place_in_the_queue() throws Exception {
        runTest("queue: callbacks keep their place in the queue");
    }

    /**
     * Test {128=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__delay__() throws Exception {
        runTest("queue: delay()");
    }

    /**
     * Test {129=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__clearQueue_name__clears_the_queue() throws Exception {
        runTest("queue: clearQueue(name) clears the queue");
    }

    /**
     * Test {130=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void queue__clearQueue___clears_the_fx_queue() throws Exception {
        runTest("queue: clearQueue() clears the fx queue");
    }

    /**
     * Test {131=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__fn_promise_____called_when_fx_queue_is_empty() throws Exception {
        runTest("queue: fn.promise() - called when fx queue is empty");
    }

    /**
     * Test {132=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__fn_promise___queue______called_whenever_last_queue_function_is_dequeued() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued");
    }

    /**
     * Test {133=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__fn_promise___queue______waits_for_animation_to_complete_before_resolving() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving");
    }

    /**
     * Test {134=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue___promise_obj_() throws Exception {
        runTest("queue: .promise(obj)");
    }

    /**
     * Test {135=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__delay___can_be_stopped() throws Exception {
        runTest("queue: delay() can be stopped");
    }

    /**
     * Test {136=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_stop_hooks() throws Exception {
        runTest("queue: queue stop hooks");
    }

    /**
     * Test {137=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__jQuery_propFix_integrity_test() throws Exception {
        runTest("attributes: jQuery.propFix integrity test");
    }

    /**
     * Test {138=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 46, 46")
    public void attributes__attr_String_() throws Exception {
        runTest("attributes: attr(String)");
    }

    /**
     * Test {139=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_String__in_XML_Files() throws Exception {
        runTest("attributes: attr(String) in XML Files");
    }

    /**
     * Test {140=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Function_() throws Exception {
        runTest("attributes: attr(String, Function)");
    }

    /**
     * Test {141=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_Hash_() throws Exception {
        runTest("attributes: attr(Hash)");
    }

    /**
     * Test {142=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 81, 81")
    public void attributes__attr_String__Object_() throws Exception {
        runTest("attributes: attr(String, Object)");
    }

    /**
     * Test {143=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr_jquery_method_() throws Exception {
        runTest("attributes: attr(jquery_method)");
    }

    /**
     * Test {144=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_document() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML document");
    }

    /**
     * Test {145=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__attr__tabindex__() throws Exception {
        runTest("attributes: attr('tabindex')");
    }

    /**
     * Test {146=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr__tabindex___value_() throws Exception {
        runTest("attributes: attr('tabindex', value)");
    }

    /**
     * Test {147=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void attributes__removeAttr_String_() throws Exception {
        runTest("attributes: removeAttr(String)");
    }

    /**
     * Test {148=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeAttr_String__in_XML() throws Exception {
        runTest("attributes: removeAttr(String) in XML");
    }

    /**
     * Test {149=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeAttr_Multi_String__variable_space_width_() throws Exception {
        runTest("attributes: removeAttr(Multi String, variable space width)");
    }

    /**
     * Test {150=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void attributes__prop_String__Object_() throws Exception {
        runTest("attributes: prop(String, Object)");
    }

    /**
     * Test {151=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__prop__tabindex__() throws Exception {
        runTest("attributes: prop('tabindex')");
    }

    /**
     * Test {152=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__prop__tabindex___value_() throws Exception {
        runTest("attributes: prop('tabindex', value)");
    }

    /**
     * Test {153=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void attributes__removeProp_String_() throws Exception {
        runTest("attributes: removeProp(String)");
    }

    /**
     * Test {154=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void attributes__val__() throws Exception {
        runTest("attributes: val()");
    }

    /**
     * Test {155=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__val___respects_numbers_without_exception__Bug__9319_() throws Exception {
        runTest("attributes: val() respects numbers without exception (Bug #9319)");
    }

    /**
     * Test {156=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__val_String_Number_() throws Exception {
        runTest("attributes: val(String/Number)");
    }

    /**
     * Test {157=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__val_Function_() throws Exception {
        runTest("attributes: val(Function)");
    }

    /**
     * Test {158=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__val_Array_of_Numbers___Bug__7123_() throws Exception {
        runTest("attributes: val(Array of Numbers) (Bug #7123)");
    }

    /**
     * Test {159=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void attributes__val_Function__with_incoming_value() throws Exception {
        runTest("attributes: val(Function) with incoming value");
    }

    /**
     * Test {160=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__val_select__after_form_reset____Bug__2551_() throws Exception {
        runTest("attributes: val(select) after form.reset() (Bug #2551)");
    }

    /**
     * Test {161=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_String_() throws Exception {
        runTest("attributes: addClass(String)");
    }

    /**
     * Test {162=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_Function_() throws Exception {
        runTest("attributes: addClass(Function)");
    }

    /**
     * Test {163=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 48, 48")
    public void attributes__addClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: addClass(Function) with incoming value");
    }

    /**
     * Test {164=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeClass_String____simple() throws Exception {
        runTest("attributes: removeClass(String) - simple");
    }

    /**
     * Test {165=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeClass_Function____simple() throws Exception {
        runTest("attributes: removeClass(Function) - simple");
    }

    /**
     * Test {166=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 48, 48")
    public void attributes__removeClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: removeClass(Function) with incoming value");
    }

    /**
     * Test {167=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__removeClass___removes_duplicates() throws Exception {
        runTest("attributes: removeClass() removes duplicates");
    }

    /**
     * Test {168=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_String_boolean_undefined___boolean__() throws Exception {
        runTest("attributes: toggleClass(String|boolean|undefined[, boolean])");
    }

    /**
     * Test {169=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_Function___boolean__() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean])");
    }

    /**
     * Test {170=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void attributes__toggleClass_Fucntion___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Fucntion[, boolean]) with incoming value");
    }

    /**
     * Test {171=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__addClass__removeClass__hasClass() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass");
    }

    /**
     * Test {172=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__contents___hasClass___returns_correct_values() throws Exception {
        runTest("attributes: contents().hasClass() returns correct values");
    }

    /**
     * Test {173=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__coords_returns_correct_values_in_IE6_IE7__see__10828() throws Exception {
        runTest("attributes: coords returns correct values in IE6/IE7, see #10828");
    }

    /**
     * Test {174=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__null_or_undefined_handler() throws Exception {
        runTest("event: null or undefined handler");
    }

    /**
     * Test {175=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__bind___live___delegate___with_non_null_defined_data() throws Exception {
        runTest("event: bind(),live(),delegate() with non-null,defined data");
    }

    /**
     * Test {176=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Handler_changes_and__trigger___order() throws Exception {
        runTest("event: Handler changes and .trigger() order");
    }

    /**
     * Test {177=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_data() throws Exception {
        runTest("event: bind(), with data");
    }

    /**
     * Test {178=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__click____with_data() throws Exception {
        runTest("event: click(), with data");
    }

    /**
     * Test {179=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_data__trigger_with_data() throws Exception {
        runTest("event: bind(), with data, trigger with data");
    }

    /**
     * Test {180=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind____multiple_events_at_once() throws Exception {
        runTest("event: bind(), multiple events at once");
    }

    /**
     * Test {181=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____five_events_at_once() throws Exception {
        runTest("event: bind(), five events at once");
    }

    /**
     * Test {182=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__bind____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: bind(), multiple events at once and namespaces");
    }

    /**
     * Test {183=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void event__bind____namespace_with_special_add() throws Exception {
        runTest("event: bind(), namespace with special add");
    }

    /**
     * Test {184=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____no_data() throws Exception {
        runTest("event: bind(), no data");
    }

    /**
     * Test {185=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__bind_one_unbind_Object_() throws Exception {
        runTest("event: bind/one/unbind(Object)");
    }

    /**
     * Test {186=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__live_die_Object___delegate_undelegate_String__Object_() throws Exception {
        runTest("event: live/die(Object), delegate/undelegate(String, Object)");
    }

    /**
     * Test {187=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__live_delegate_immediate_propagation() throws Exception {
        runTest("event: live/delegate immediate propagation");
    }

    /**
     * Test {188=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind_delegate_bubbling__isDefaultPrevented() throws Exception {
        runTest("event: bind/delegate bubbling, isDefaultPrevented");
    }

    /**
     * Test {189=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____iframes() throws Exception {
        runTest("event: bind(), iframes");
    }

    /**
     * Test {190=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__bind____trigger_change_on_select() throws Exception {
        runTest("event: bind(), trigger change on select");
    }

    /**
     * Test {191=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__bind____namespaced_events__cloned_events() throws Exception {
        runTest("event: bind(), namespaced events, cloned events");
    }

    /**
     * Test {192=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__bind____multi_namespaced_events() throws Exception {
        runTest("event: bind(), multi-namespaced events");
    }

    /**
     * Test {193=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind____with_same_function() throws Exception {
        runTest("event: bind(), with same function");
    }

    /**
     * Test {194=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____make_sure_order_is_maintained() throws Exception {
        runTest("event: bind(), make sure order is maintained");
    }

    /**
     * Test {195=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_different_this_object() throws Exception {
        runTest("event: bind(), with different this object");
    }

    /**
     * Test {196=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__bind_name__false___unbind_name__false_() throws Exception {
        runTest("event: bind(name, false), unbind(name, false)");
    }

    /**
     * Test {197=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__live_name__false___die_name__false_() throws Exception {
        runTest("event: live(name, false), die(name, false)");
    }

    /**
     * Test {198=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegate_selector__name__false___undelegate_selector__name__false_() throws Exception {
        runTest("event: delegate(selector, name, false), undelegate(selector, name, false)");
    }

    /**
     * Test {199=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__bind___trigger___unbind___on_plain_object() throws Exception {
        runTest("event: bind()/trigger()/unbind() on plain object");
    }

    /**
     * Test {200=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__unbind_type_() throws Exception {
        runTest("event: unbind(type)");
    }

    /**
     * Test {201=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__unbind_eventObject_() throws Exception {
        runTest("event: unbind(eventObject)");
    }

    /**
     * Test {202=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__hover___and_hover_pseudo_event() throws Exception {
        runTest("event: hover() and hover pseudo-event");
    }

    /**
     * Test {203=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__mouseover_triggers_mouseenter() throws Exception {
        runTest("event: mouseover triggers mouseenter");
    }

    /**
     * Test {204=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__withinElement_implemented_with_jQuery_contains__() throws Exception {
        runTest("event: withinElement implemented with jQuery.contains()");
    }

    /**
     * Test {205=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__mouseenter__mouseleave_don_t_catch_exceptions() throws Exception {
        runTest("event: mouseenter, mouseleave don't catch exceptions");
    }

    /**
     * Test {206=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__trigger___shortcuts() throws Exception {
        runTest("event: trigger() shortcuts");
    }

    /**
     * Test {207=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__trigger___bubbling() throws Exception {
        runTest("event: trigger() bubbling");
    }

    /**
     * Test {208=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__trigger_type___data____fn__() throws Exception {
        runTest("event: trigger(type, [data], [fn])");
    }

    /**
     * Test {209=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__submit_event_bubbles_on_copied_forms___11649_() throws Exception {
        runTest("event: submit event bubbles on copied forms (#11649)");
    }

    /**
     * Test {210=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__change_event_bubbles_on_copied_forms___11796_() throws Exception {
        runTest("event: change event bubbles on copied forms (#11796)");
    }

    /**
     * Test {211=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void event__trigger_eventObject___data____fn__() throws Exception {
        runTest("event: trigger(eventObject, [data], [fn])");
    }

    /**
     * Test {212=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event___trigger___bubbling_on_disconnected_elements___10489_() throws Exception {
        runTest("event: .trigger() bubbling on disconnected elements (#10489)");
    }

    /**
     * Test {213=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___trigger___doesn_t_bubble_load_event___10717_() throws Exception {
        runTest("event: .trigger() doesn't bubble load event (#10717)");
    }

    /**
     * Test {214=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__Delegated_events_in_SVG___10791_() throws Exception {
        runTest("event: Delegated events in SVG (#10791)");
    }

    /**
     * Test {215=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__Delegated_events_in_forms___10844___11145___8165___11382___11764_() throws Exception {
        runTest("event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764)");
    }

    /**
     * Test {216=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Submit_event_can_be_stopped___11049_() throws Exception {
        runTest("event: Submit event can be stopped (#11049)");
    }

    /**
     * Test {217=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__on_beforeunload__creates_deletes_window_property_instead_of_adding_removing_event_listener() throws Exception {
        runTest("event: on(beforeunload) creates/deletes window property instead of adding/removing event listener");
    }

    /**
     * Test {218=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__jQuery_Event__type__props__() throws Exception {
        runTest("event: jQuery.Event( type, props )");
    }

    /**
     * Test {219=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_Event_currentTarget() throws Exception {
        runTest("event: jQuery.Event.currentTarget");
    }

    /**
     * Test {220=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__toggle_Function__Function______() throws Exception {
        runTest("event: toggle(Function, Function, ...)");
    }

    /**
     * Test {221=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 66, 66")
    public void event___live____die__() throws Exception {
        runTest("event: .live()/.die()");
    }

    /**
     * Test {222=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__die_all_bound_events() throws Exception {
        runTest("event: die all bound events");
    }

    /**
     * Test {223=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__live_with_multiple_events() throws Exception {
        runTest("event: live with multiple events");
    }

    /**
     * Test {224=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void event__live_with_namespaces() throws Exception {
        runTest("event: live with namespaces");
    }

    /**
     * Test {225=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__live_with_change() throws Exception {
        runTest("event: live with change");
    }

    /**
     * Test {226=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__live_with_submit() throws Exception {
        runTest("event: live with submit");
    }

    /**
     * Test {227=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void event__live_with_special_events() throws Exception {
        runTest("event: live with special events");
    }

    /**
     * Test {228=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 65, 65")
    public void event___delegate____undelegate__() throws Exception {
        runTest("event: .delegate()/.undelegate()");
    }

    /**
     * Test {229=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_off_using_dispatched_jQuery_Event() throws Exception {
        runTest("event: jQuery.off using dispatched jQuery.Event");
    }

    /**
     * Test {230=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegated_event_with_delegateTarget_relative_selector() throws Exception {
        runTest("event: delegated event with delegateTarget-relative selector");
    }

    /**
     * Test {231=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__stopPropagation___stops_directly_bound_events_on_delegated_target() throws Exception {
        runTest("event: stopPropagation() stops directly-bound events on delegated target");
    }

    /**
     * Test {232=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__undelegate_all_bound_events() throws Exception {
        runTest("event: undelegate all bound events");
    }

    /**
     * Test {233=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__delegate_with_multiple_events() throws Exception {
        runTest("event: delegate with multiple events");
    }

    /**
     * Test {234=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__delegate_with_change() throws Exception {
        runTest("event: delegate with change");
    }

    /**
     * Test {235=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__delegate_with_submit() throws Exception {
        runTest("event: delegate with submit");
    }

    /**
     * Test {236=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__undelegate___with_only_namespaces() throws Exception {
        runTest("event: undelegate() with only namespaces");
    }

    /**
     * Test {237=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Non_DOM_element_events() throws Exception {
        runTest("event: Non DOM element events");
    }

    /**
     * Test {238=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__inline_handler_returning_false_stops_default() throws Exception {
        runTest("event: inline handler returning false stops default");
    }

    /**
     * Test {239=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__window_resize() throws Exception {
        runTest("event: window resize");
    }

    /**
     * Test {240=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__focusin_bubbles() throws Exception {
        runTest("event: focusin bubbles");
    }

    /**
     * Test {241=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__custom_events_with_colons___3533___8272_() throws Exception {
        runTest("event: custom events with colons (#3533, #8272)");
    }

    /**
     * Test {242=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void event___on_and__off() throws Exception {
        runTest("event: .on and .off");
    }

    /**
     * Test {243=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__special_bind_delegate_name_mapping() throws Exception {
        runTest("event: special bind/delegate name mapping");
    }

    /**
     * Test {244=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event___on_and__off__selective_mixed_removal___10705_() throws Exception {
        runTest("event: .on and .off, selective mixed removal (#10705)");
    }

    /**
     * Test {245=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___on__event_map__null_selector__data____11130() throws Exception {
        runTest("event: .on( event-map, null-selector, data ) #11130");
    }

    /**
     * Test {246=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__clone___delegated_events___11076_() throws Exception {
        runTest("event: clone() delegated events (#11076)");
    }

    /**
     * Test {247=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__fixHooks_extensions() throws Exception {
        runTest("event: fixHooks extensions");
    }

    /**
     * Test {248=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_promise() throws Exception {
        runTest("event: jQuery.ready promise");
    }

    /**
     * Test {249=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_synchronous_load_with_long_loading_subresources() throws Exception {
        runTest("event: jQuery.ready synchronous load with long loading subresources");
    }

    /**
     * Test {250=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_isReady() throws Exception {
        runTest("event: jQuery.isReady");
    }

    /**
     * Test {251=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void event__jQuery_ready() throws Exception {
        runTest("event: jQuery ready");
    }

    /**
     * Test {252=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__change_handler_should_be_detached_from_element() throws Exception {
        runTest("event: change handler should be detached from element");
    }

    /**
     * Test {253=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__trigger_click_on_checkbox__fires_change_event() throws Exception {
        runTest("event: trigger click on checkbox, fires change event");
    }

    /**
     * Test {254=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void selector___jQuery_only__element___jQuery_only() throws Exception {
        runTest("selector - jQuery only: element - jQuery only");
    }

    /**
     * Test {255=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector___jQuery_only__class___jQuery_only() throws Exception {
        runTest("selector - jQuery only: class - jQuery only");
    }

    /**
     * Test {256=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void selector___jQuery_only__attributes___jQuery_only() throws Exception {
        runTest("selector - jQuery only: attributes - jQuery only");
    }

    /**
     * Test {257=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void selector___jQuery_only__pseudo___visibility() throws Exception {
        runTest("selector - jQuery only: pseudo - visibility");
    }

    /**
     * Test {258=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector___jQuery_only__disconnected_nodes() throws Exception {
        runTest("selector - jQuery only: disconnected nodes");
    }

    /**
     * Test {259=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    public void selector___jQuery_only__attributes___jQuery_attr() throws Exception {
        runTest("selector - jQuery only: attributes - jQuery.attr");
    }

    /**
     * Test {260=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void selector___jQuery_only__Sizzle_cache_collides_with_multiple_Sizzles_on_a_page() throws Exception {
        runTest("selector - jQuery only: Sizzle cache collides with multiple Sizzles on a page");
    }

    /**
     * Test {261=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__find_String_() throws Exception {
        runTest("traversing: find(String)");
    }

    /**
     * Test {262=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__find_node_jQuery_object_() throws Exception {
        runTest("traversing: find(node|jQuery object)");
    }

    /**
     * Test {263=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    public void traversing__is_String_undefined_() throws Exception {
        runTest("traversing: is(String|undefined)");
    }

    /**
     * Test {264=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void traversing__is_jQuery_() throws Exception {
        runTest("traversing: is(jQuery)");
    }

    /**
     * Test {265=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void traversing__is___with_positional_selectors() throws Exception {
        runTest("traversing: is() with positional selectors");
    }

    /**
     * Test {266=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__index__() throws Exception {
        runTest("traversing: index()");
    }

    /**
     * Test {267=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__index_Object_String_undefined_() throws Exception {
        runTest("traversing: index(Object|String|undefined)");
    }

    /**
     * Test {268=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__filter_Selector_undefined_() throws Exception {
        runTest("traversing: filter(Selector|undefined)");
    }

    /**
     * Test {269=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__filter_Function_() throws Exception {
        runTest("traversing: filter(Function)");
    }

    /**
     * Test {270=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Element_() throws Exception {
        runTest("traversing: filter(Element)");
    }

    /**
     * Test {271=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Array_() throws Exception {
        runTest("traversing: filter(Array)");
    }

    /**
     * Test {272=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_jQuery_() throws Exception {
        runTest("traversing: filter(jQuery)");
    }

    /**
     * Test {273=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void traversing__filter___with_positional_selectors() throws Exception {
        runTest("traversing: filter() with positional selectors");
    }

    /**
     * Test {274=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void traversing__closest__() throws Exception {
        runTest("traversing: closest()");
    }

    /**
     * Test {275=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void traversing__closest_jQuery_() throws Exception {
        runTest("traversing: closest(jQuery)");
    }

    /**
     * Test {276=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__not_Selector_undefined_() throws Exception {
        runTest("traversing: not(Selector|undefined)");
    }

    /**
     * Test {277=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Element_() throws Exception {
        runTest("traversing: not(Element)");
    }

    /**
     * Test {278=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Function_() throws Exception {
        runTest("traversing: not(Function)");
    }

    /**
     * Test {279=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__not_Array_() throws Exception {
        runTest("traversing: not(Array)");
    }

    /**
     * Test {280=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_jQuery_() throws Exception {
        runTest("traversing: not(jQuery)");
    }

    /**
     * Test {281=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__has_Element_() throws Exception {
        runTest("traversing: has(Element)");
    }

    /**
     * Test {282=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__has_Selector_() throws Exception {
        runTest("traversing: has(Selector)");
    }

    /**
     * Test {283=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__has_Arrayish_() throws Exception {
        runTest("traversing: has(Arrayish)");
    }

    /**
     * Test {284=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__addBack__() throws Exception {
        runTest("traversing: addBack()");
    }

    /**
     * Test {285=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void traversing__siblings__String__() throws Exception {
        runTest("traversing: siblings([String])");
    }

    /**
     * Test {286=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__children__String__() throws Exception {
        runTest("traversing: children([String])");
    }

    /**
     * Test {287=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__parent__String__() throws Exception {
        runTest("traversing: parent([String])");
    }

    /**
     * Test {288=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__parents__String__() throws Exception {
        runTest("traversing: parents([String])");
    }

    /**
     * Test {289=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__parentsUntil__String__() throws Exception {
        runTest("traversing: parentsUntil([String])");
    }

    /**
     * Test {290=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__next__String__() throws Exception {
        runTest("traversing: next([String])");
    }

    /**
     * Test {291=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__prev__String__() throws Exception {
        runTest("traversing: prev([String])");
    }

    /**
     * Test {292=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__nextAll__String__() throws Exception {
        runTest("traversing: nextAll([String])");
    }

    /**
     * Test {293=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__prevAll__String__() throws Exception {
        runTest("traversing: prevAll([String])");
    }

    /**
     * Test {294=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__nextUntil__String__() throws Exception {
        runTest("traversing: nextUntil([String])");
    }

    /**
     * Test {295=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void traversing__prevUntil__String__() throws Exception {
        runTest("traversing: prevUntil([String])");
    }

    /**
     * Test {296=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__contents__() throws Exception {
        runTest("traversing: contents()");
    }

    /**
     * Test {297=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__add_String_Element_Array_undefined_() throws Exception {
        runTest("traversing: add(String|Element|Array|undefined)");
    }

    /**
     * Test {298=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__add_String__Context_() throws Exception {
        runTest("traversing: add(String, Context)");
    }

    /**
     * Test {299=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__eq___1____10616() throws Exception {
        runTest("traversing: eq('-1') #10616");
    }

    /**
     * Test {300=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__text__() throws Exception {
        runTest("manipulation: text()");
    }

    /**
     * Test {301=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__text_undefined_() throws Exception {
        runTest("manipulation: text(undefined)");
    }

    /**
     * Test {302=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__text_String_() throws Exception {
        runTest("manipulation: text(String)");
    }

    /**
     * Test {303=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__text_Function_() throws Exception {
        runTest("manipulation: text(Function)");
    }

    /**
     * Test {304=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__text_Function__with_incoming_value() throws Exception {
        runTest("manipulation: text(Function) with incoming value");
    }

    /**
     * Test {305=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void manipulation__wrap_String_Element_() throws Exception {
        runTest("manipulation: wrap(String|Element)");
    }

    /**
     * Test {306=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void manipulation__wrap_Function_() throws Exception {
        runTest("manipulation: wrap(Function)");
    }

    /**
     * Test {307=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__wrap_Function__with_index___10177_() throws Exception {
        runTest("manipulation: wrap(Function) with index (#10177)");
    }

    /**
     * Test {308=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void manipulation__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("manipulation: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {309=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__wrapAll_String_Element_() throws Exception {
        runTest("manipulation: wrapAll(String|Element)");
    }

    /**
     * Test {310=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void manipulation__wrapInner_String_Element_() throws Exception {
        runTest("manipulation: wrapInner(String|Element)");
    }

    /**
     * Test {311=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void manipulation__wrapInner_Function_() throws Exception {
        runTest("manipulation: wrapInner(Function)");
    }

    /**
     * Test {312=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__unwrap__() throws Exception {
        runTest("manipulation: unwrap()");
    }

    /**
     * Test {313=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 58, 58")
    public void manipulation__append_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {314=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 58, 58")
    public void manipulation__append_Function_() throws Exception {
        runTest("manipulation: append(Function)");
    }

    /**
     * Test {315=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void manipulation__append_Function__with_incoming_value() throws Exception {
        runTest("manipulation: append(Function) with incoming value");
    }

    /**
     * Test {316=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_the_same_fragment_with_events__Bug__6997__5566_() throws Exception {
        runTest("manipulation: append the same fragment with events (Bug #6997, 5566)");
    }

    /**
     * Test {317=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_HTML5_sectioning_elements__Bug__6485_() throws Exception {
        runTest("manipulation: append HTML5 sectioning elements (Bug #6485)");
    }

    /**
     * Test {318=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__HTML5_Elements_inherit_styles_from_style_rules__Bug__10501_() throws Exception {
        runTest("manipulation: HTML5 Elements inherit styles from style rules (Bug #10501)");
    }

    /**
     * Test {319=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html5_clone___cannot_use_the_fragment_cache_in_IE___6485_() throws Exception {
        runTest("manipulation: html5 clone() cannot use the fragment cache in IE (#6485)");
    }

    /**
     * Test {320=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_String__with_HTML5__Bug__6485_() throws Exception {
        runTest("manipulation: html(String) with HTML5 (Bug #6485)");
    }

    /**
     * Test {321=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__IE8_serialization_bug() throws Exception {
        runTest("manipulation: IE8 serialization bug");
    }

    /**
     * Test {322=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___object_element__10324() throws Exception {
        runTest("manipulation: html() object element #10324");
    }

    /**
     * Test {323=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__append_xml_() throws Exception {
        runTest("manipulation: append(xml)");
    }

    /**
     * Test {324=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void manipulation__appendTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: appendTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {325=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prepend_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prepend(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {326=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prepend_Function_() throws Exception {
        runTest("manipulation: prepend(Function)");
    }

    /**
     * Test {327=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__prepend_Function__with_incoming_value() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value");
    }

    /**
     * Test {328=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prependTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prependTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {329=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__before_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: before(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {330=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__before_Function_() throws Exception {
        runTest("manipulation: before(Function)");
    }

    /**
     * Test {331=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_and_after_w__empty_object___10812_() throws Exception {
        runTest("manipulation: before and after w/ empty object (#10812)");
    }

    /**
     * Test {332=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_and_after_on_disconnected_node___10517_() throws Exception {
        runTest("manipulation: before and after on disconnected node (#10517)");
    }

    /**
     * Test {333=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__insertBefore_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertBefore(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {334=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__after_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: after(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {335=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__after_Function_() throws Exception {
        runTest("manipulation: after(Function)");
    }

    /**
     * Test {336=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__insertAfter_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertAfter(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {337=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void manipulation__replaceWith_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {338=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void manipulation__replaceWith_Function_() throws Exception {
        runTest("manipulation: replaceWith(Function)");
    }

    /**
     * Test {339=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceWith_string__for_more_than_one_element() throws Exception {
        runTest("manipulation: replaceWith(string) for more than one element");
    }

    /**
     * Test {340=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__replaceAll_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceAll(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {341=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery_clone_____8017_() throws Exception {
        runTest("manipulation: jQuery.clone() (#8017)");
    }

    /**
     * Test {342=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone_____8070_() throws Exception {
        runTest("manipulation: clone() (#8070)");
    }

    /**
     * Test {343=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 44, 44")
    public void manipulation__clone__() throws Exception {
        runTest("manipulation: clone()");
    }

    /**
     * Test {344=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__clone_script_type_non_javascript____11359_() throws Exception {
        runTest("manipulation: clone(script type=non-javascript) (#11359)");
    }

    /**
     * Test {345=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__clone_form_element___Bug__3879___6655_() throws Exception {
        runTest("manipulation: clone(form element) (Bug #3879, #6655)");
    }

    /**
     * Test {346=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__clone_multiple_selected_options___Bug__8129_() throws Exception {
        runTest("manipulation: clone(multiple selected options) (Bug #8129)");
    }

    /**
     * Test {347=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_XML_nodes() throws Exception {
        runTest("manipulation: clone() on XML nodes");
    }

    /**
     * Test {348=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_local_XML_nodes_with_html5_nodename() throws Exception {
        runTest("manipulation: clone() on local XML nodes with html5 nodename");
    }

    /**
     * Test {349=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html_undefined_() throws Exception {
        runTest("manipulation: html(undefined)");
    }

    /**
     * Test {350=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___on_empty_set() throws Exception {
        runTest("manipulation: html() on empty set");
    }

    /**
     * Test {351=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    public void manipulation__html_String_() throws Exception {
        runTest("manipulation: html(String)");
    }

    /**
     * Test {352=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 37, 37")
    public void manipulation__html_Function_() throws Exception {
        runTest("manipulation: html(Function)");
    }

    /**
     * Test {353=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void manipulation__html_Function__with_incoming_value() throws Exception {
        runTest("manipulation: html(Function) with incoming value");
    }

    /**
     * Test {354=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__remove__() throws Exception {
        runTest("manipulation: remove()");
    }

    /**
     * Test {355=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__detach__() throws Exception {
        runTest("manipulation: detach()");
    }

    /**
     * Test {356=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__empty__() throws Exception {
        runTest("manipulation: empty()");
    }

    /**
     * Test {357=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void manipulation__jQuery_cleanData() throws Exception {
        runTest("manipulation: jQuery.cleanData");
    }

    /**
     * Test {358=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___no_plain_text_caching__Bug__6779_() throws Exception {
        runTest("manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779)");
    }

    /**
     * Test {359=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__jQuery_html___execute_scripts_escaped_with_html_comment_or_CDATA___9221_() throws Exception {
        runTest("manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221)");
    }

    /**
     * Test {360=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___plain_objects_are_not_a_document__8950() throws Exception {
        runTest("manipulation: jQuery.buildFragment - plain objects are not a document #8950");
    }

    /**
     * Test {361=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_clone___no_exceptions_for_object_elements__9587() throws Exception {
        runTest("manipulation: jQuery.clone - no exceptions for object elements #9587");
    }

    /**
     * Test {362=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("manipulation: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {363=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__Cloned__detached_HTML5_elems___10667_10670_() throws Exception {
        runTest("manipulation: Cloned, detached HTML5 elems (#10667,10670)");
    }

    /**
     * Test {364=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__jQuery_fragments_cache_expectations() throws Exception {
        runTest("manipulation: jQuery.fragments cache expectations");
    }

    /**
     * Test {365=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__Guard_against_exceptions_when_clearing_safeChildNodes() throws Exception {
        runTest("manipulation: Guard against exceptions when clearing safeChildNodes");
    }

    /**
     * Test {366=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__Ensure_oldIE_creates_a_new_set_on_appendTo___8894_() throws Exception {
        runTest("manipulation: Ensure oldIE creates a new set on appendTo (#8894)");
    }

    /**
     * Test {367=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_____script_exceptions_bubble___11743_() throws Exception {
        runTest("manipulation: html() - script exceptions bubble (#11743)");
    }

    /**
     * Test {368=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__checked_state_is_cloned_with_clone__() throws Exception {
        runTest("manipulation: checked state is cloned with clone()");
    }

    /**
     * Test {369=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__manipulate_mixed_jQuery_and_text___12384___12346_() throws Exception {
        runTest("manipulation: manipulate mixed jQuery and text (#12384, #12346)");
    }

    /**
     * Test {370=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__buildFragment_works_even_if_document_0__is_iframe_s_window_object_in_IE9_10___12266_() throws Exception {
        runTest("manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266)");
    }

    /**
     * Test {371=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 46, 46")
    public void css__css_String_Hash_() throws Exception {
        runTest("css: css(String|Hash)");
    }

    /**
     * Test {372=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void css__css___explicit_and_relative_values() throws Exception {
        runTest("css: css() explicit and relative values");
    }

    /**
     * Test {373=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void css__css_String__Object_() throws Exception {
        runTest("css: css(String, Object)");
    }

    /**
     * Test {374=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function_() throws Exception {
        runTest("css: css(String, Function)");
    }

    /**
     * Test {375=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function__with_incoming_value() throws Exception {
        runTest("css: css(String, Function) with incoming value");
    }

    /**
     * Test {376=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions() throws Exception {
        runTest("css: css(Object) where values are Functions");
    }

    /**
     * Test {377=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions_with_incoming_values() throws Exception {
        runTest("css: css(Object) where values are Functions with incoming values");
    }

    /**
     * Test {378=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void css__show____hide__() throws Exception {
        runTest("css: show(); hide()");
    }

    /**
     * Test {379=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void css__show___resolves_correct_default_display__8099() throws Exception {
        runTest("css: show() resolves correct default display #8099");
    }

    /**
     * Test {380=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void css__show___resolves_correct_default_display__detached_nodes___10006_() throws Exception {
        runTest("css: show() resolves correct default display, detached nodes (#10006)");
    }

    /**
     * Test {381=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void css__toggle__() throws Exception {
        runTest("css: toggle()");
    }

    /**
     * Test {382=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__hide_hidden_elements__bug__7141_() throws Exception {
        runTest("css: hide hidden elements (bug #7141)");
    }

    /**
     * Test {383=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {384=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css___visible_selector_works_properly_on_table_elements__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on table elements (bug #4512)");
    }

    /**
     * Test {385=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css___visible_selector_works_properly_on_children_with_a_hidden_parent__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on children with a hidden parent (bug #4512)");
    }

    /**
     * Test {386=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__internal_ref_to_elem_runtimeStyle__bug__7608_() throws Exception {
        runTest("css: internal ref to elem.runtimeStyle (bug #7608)");
    }

    /**
     * Test {387=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__marginRight_computed_style__bug__3333_() throws Exception {
        runTest("css: marginRight computed style (bug #3333)");
    }

    /**
     * Test {388=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__box_model_properties_incorrectly_returning___instead_of_px__see__10639_and__12088() throws Exception {
        runTest("css: box model properties incorrectly returning % instead of px, see #10639 and #12088");
    }

    /**
     * Test {389=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__jQuery_cssProps_behavior___bug__8402_() throws Exception {
        runTest("css: jQuery.cssProps behavior, (bug #8402)");
    }

    /**
     * Test {390=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 2, 4",
            EDGE = "2, 2, 4",
            FF = "0, 1, 1",
            FF_ESR = "0, 1, 1")
    public void css__widows___orphans__8936() throws Exception {
        runTest("css: widows & orphans #8936");
    }

    /**
     * Test {391=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__can_t_get_css_for_disconnected_in_IE_9__see__10254_and__8388() throws Exception {
        runTest("css: can't get css for disconnected in IE<9, see #10254 and #8388");
    }

    /**
     * Test {392=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void css__can_t_get_background_position_in_IE_9__see__10796() throws Exception {
        runTest("css: can't get background-position in IE<9, see #10796");
    }

    /**
     * Test {393=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__percentage_properties_for_bottom_and_right_in_IE_9_should_not_be_incorrectly_transformed_to_pixels__see__11311() throws Exception {
        runTest("css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pixels, see #11311");
    }

    /**
     * Test {394=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__percentage_properties_for_left_and_top_should_be_transformed_to_pixels__see__9505() throws Exception {
        runTest("css: percentage properties for left and top should be transformed to pixels, see #9505");
    }

    /**
     * Test {395=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("css: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {396=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__css__width___and_css__height___should_respect_box_sizing__see__11004() throws Exception {
        runTest("css: css('width') and css('height') should respect box-sizing, see #11004");
    }

    /**
     * Test {397=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__certain_css_values_of__normal__should_be_convertable_to_a_number__see__8627() throws Exception {
        runTest("css: certain css values of 'normal' should be convertable to a number, see #8627");
    }

    /**
     * Test {398=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void css__cssHooks___expand() throws Exception {
        runTest("css: cssHooks - expand");
    }

    /**
     * Test {399=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void serialize__jQuery_param__() throws Exception {
        runTest("serialize: jQuery.param()");
    }

    /**
     * Test {400=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void serialize__jQuery_param___Constructed_prop_values() throws Exception {
        runTest("serialize: jQuery.param() Constructed prop values");
    }

    /**
     * Test {401=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void serialize__serialize__() throws Exception {
        runTest("serialize: serialize()");
    }

    /**
     * Test {402=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks");
    }

    /**
     * Test {403=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks____url__options__syntax() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks - (url, options) syntax");
    }

    /**
     * Test {404=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (late binding)");
    }

    /**
     * Test {405=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__oncomplete_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (oncomplete binding)");
    }

    /**
     * Test {406=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__very_late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (very late binding)");
    }

    /**
     * Test {407=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____success_callbacks__order_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (order)");
    }

    /**
     * Test {408=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    @HtmlUnitNYI(CHROME = "2, 6, 8",
            EDGE = "2, 6, 8",
            FF = "2, 6, 8",
            FF_ESR = "2, 6, 8")
    public void ajax__jQuery_ajax_____error_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - error callbacks");
    }

    /**
     * Test {409=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax___multiple_method_signatures_introduced_in_1_5____8107_() throws Exception {
        runTest("ajax: jQuery.ajax - multiple method signatures introduced in 1.5 ( #8107)");
    }

    /**
     * Test {410=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_____textStatus_and_errorThrown_values() throws Exception {
        runTest("ajax: jQuery.ajax() - textStatus and errorThrown values");
    }

    /**
     * Test {411=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____responseText_on_error() throws Exception {
        runTest("ajax: jQuery.ajax() - responseText on error");
    }

    /**
     * Test {412=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: .ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {413=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___ajax_____headers() throws Exception {
        runTest("ajax: .ajax() - headers");
    }

    /**
     * Test {414=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___ajax_____Accept_header() throws Exception {
        runTest("ajax: .ajax() - Accept header");
    }

    /**
     * Test {415=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___ajax_____contentType() throws Exception {
        runTest("ajax: .ajax() - contentType");
    }

    /**
     * Test {416=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: .ajax() - protocol-less urls");
    }

    /**
     * Test {417=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___ajax_____hash() throws Exception {
        runTest("ajax: .ajax() - hash");
    }

    /**
     * Test {418=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax___cross_domain_detection() throws Exception {
        runTest("ajax: jQuery ajax - cross-domain detection");
    }

    /**
     * Test {419=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax___load_____404_error_callbacks() throws Exception {
        runTest("ajax: .load() - 404 error callbacks");
    }

    /**
     * Test {420=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____abort() throws Exception {
        runTest("ajax: jQuery.ajax() - abort");
    }

    /**
     * Test {421=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void ajax__Ajax_events_with_context() throws Exception {
        runTest("ajax: Ajax events with context");
    }

    /**
     * Test {422=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_context_modification() throws Exception {
        runTest("ajax: jQuery.ajax context modification");
    }

    /**
     * Test {423=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax context modification through ajaxSetup");
    }

    /**
     * Test {424=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____disabled_globals() throws Exception {
        runTest("ajax: jQuery.ajax() - disabled globals");
    }

    /**
     * Test {425=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {426=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {427=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax - HEAD requests");
    }

    /**
     * Test {428=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend");
    }

    /**
     * Test {429=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request___2688_() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request (#2688)");
    }

    /**
     * Test {430=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request manually");
    }

    /**
     * Test {431=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax___dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax - dataType html");
    }

    /**
     * Test {432=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__synchronous_request() throws Exception {
        runTest("ajax: synchronous request");
    }

    /**
     * Test {433=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: synchronous request with callbacks");
    }

    /**
     * Test {434=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__pass_through_request_object() throws Exception {
        runTest("ajax: pass-through request object");
    }

    /**
     * Test {435=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void ajax__ajax_cache() throws Exception {
        runTest("ajax: ajax cache");
    }

    /**
     * Test {436=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_() throws Exception {
        runTest("ajax: load(String)");
    }

    /**
     * Test {437=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_null_() throws Exception {
        runTest("ajax: load(String,null)");
    }

    /**
     * Test {438=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_undefined_() throws Exception {
        runTest("ajax: load(String,undefined)");
    }

    /**
     * Test {439=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load__url_selector__() throws Exception {
        runTest("ajax: load('url selector')");
    }

    /**
     * Test {440=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load_String__Function__with_ajaxSetup_on_dataType_json__see__2046() throws Exception {
        runTest("ajax: load(String, Function) with ajaxSetup on dataType json, see #2046");
    }

    /**
     * Test {441=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Function____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: load(String, Function) - simple: inject text into DOM");
    }

    /**
     * Test {442=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void ajax__load_String__Function____check_scripts() throws Exception {
        runTest("ajax: load(String, Function) - check scripts");
    }

    /**
     * Test {443=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__load_String__Function____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: load(String, Function) - check file with only a script tag");
    }

    /**
     * Test {444=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Function____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: load(String, Function) - dataFilter in ajaxSettings");
    }

    /**
     * Test {445=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Object__Function_() throws Exception {
        runTest("ajax: load(String, Object, Function)");
    }

    /**
     * Test {446=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__String__Function_() throws Exception {
        runTest("ajax: load(String, String, Function)");
    }

    /**
     * Test {447=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load_____data_specified_in_ajaxSettings_is_merged_in___10524_() throws Exception {
        runTest("ajax: load() - data specified in ajaxSettings is merged in (#10524)");
    }

    /**
     * Test {448=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: load() - callbacks get the correct parameters");
    }

    /**
     * Test {449=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_get_String__Function____data_in_ajaxSettings___8277_() throws Exception {
        runTest("ajax: jQuery.get(String, Function) - data in ajaxSettings (#8277)");
    }

    /**
     * Test {450=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_get_String__Hash__Function____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get(String, Hash, Function) - parse xml and use text() on nodes");
    }

    /**
     * Test {451=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_getScript_String__Function____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - with callback");
    }

    /**
     * Test {452=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_getScript_String__Function____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - no callback");
    }

    /**
     * Test {453=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void ajax__jQuery_ajax_____JSONP__Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Same Domain");
    }

    /**
     * Test {454=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void ajax__jQuery_ajax_____JSONP__Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Cross Domain");
    }

    /**
     * Test {455=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote");
    }

    /**
     * Test {456=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____script__Remote_with_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with POST");
    }

    /**
     * Test {457=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote_with_scheme_less_URL() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with scheme-less URL");
    }

    /**
     * Test {458=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____malformed_JSON() throws Exception {
        runTest("ajax: jQuery.ajax() - malformed JSON");
    }

    /**
     * Test {459=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____script__throws_exception___11743_() throws Exception {
        runTest("ajax: jQuery.ajax() - script, throws exception (#11743)");
    }

    /**
     * Test {460=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - script by content-type");
    }

    /**
     * Test {461=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____json_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type");
    }

    /**
     * Test {462=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax_____json_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type disabled with options");
    }

    /**
     * Test {463=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_getJSON_String__Hash__Function____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Hash, Function) - JSON array");
    }

    /**
     * Test {464=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object");
    }

    /**
     * Test {465=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON___Using_Native_JSON() throws Exception {
        runTest("ajax: jQuery.getJSON - Using Native JSON");
    }

    /**
     * Test {466=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object with absolute url to local content");
    }

    /**
     * Test {467=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_post___data() throws Exception {
        runTest("ajax: jQuery.post - data");
    }

    /**
     * Test {468=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_post_String__Hash__Function____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post(String, Hash, Function) - simple with xml");
    }

    /**
     * Test {469=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @HtmlUnitNYI(CHROME = "2, 0, 2",
            EDGE = "2, 0, 2",
            FF = "2, 0, 2",
            FF_ESR = "2, 0, 2")
    public void ajax__jQuery_ajaxSetup__timeout__Number_____with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) - with global timeout");
    }

    /**
     * Test {470=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxSetup__timeout__Number___with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) with localtimeout");
    }

    /**
     * Test {471=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___simple_get() throws Exception {
        runTest("ajax: jQuery.ajax - simple get");
    }

    /**
     * Test {472=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___simple_post() throws Exception {
        runTest("ajax: jQuery.ajax - simple post");
    }

    /**
     * Test {473=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__ajaxSetup__() throws Exception {
        runTest("ajax: ajaxSetup()");
    }

    /**
     * Test {474=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__data_option__evaluate_function_values___2806_() throws Exception {
        runTest("ajax: data option: evaluate function values (#2806)");
    }

    /**
     * Test {475=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__data_option__empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: data option: empty bodies for non-GET requests");
    }

    /**
     * Test {476=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (cache)");
    }

    /**
     * Test {477=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (cache)");
    }

    /**
     * Test {478=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (no cache)");
    }

    /**
     * Test {479=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (no cache)");
    }

    /**
     * Test {480=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___failing_cross_domain() throws Exception {
        runTest("ajax: jQuery ajax - failing cross-domain");
    }

    /**
     * Test {481=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___atom_xml() throws Exception {
        runTest("ajax: jQuery ajax - atom+xml");
    }

    /**
     * Test {482=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___Location_object_as_url___7531_() throws Exception {
        runTest("ajax: jQuery.ajax - Location object as url (#7531)");
    }

    /**
     * Test {483=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: jQuery.ajax - Context with circular references (#9887)");
    }

    /**
     * Test {484=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___statusText() throws Exception {
        runTest("ajax: jQuery.ajax - statusText");
    }

    /**
     * Test {485=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void ajax__jQuery_ajax___statusCode() throws Exception {
        runTest("ajax: jQuery.ajax - statusCode");
    }

    /**
     * Test {486=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax___transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax - transitive conversions");
    }

    /**
     * Test {487=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax - overrideMimeType");
    }

    /**
     * Test {488=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___abort_in_prefilter() throws Exception {
        runTest("ajax: jQuery.ajax - abort in prefilter");
    }

    /**
     * Test {489=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___loading_binary_data_shouldn_t_throw_an_exception_in_IE___11426_() throws Exception {
        runTest("ajax: jQuery.ajax - loading binary data shouldn't throw an exception in IE (#11426)");
    }

    /**
     * Test {490=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_domManip___no_side_effect_because_of_ajaxSetup_or_global_events___11264_() throws Exception {
        runTest("ajax: jQuery.domManip - no side effect because of ajaxSetup or global events (#11264)");
    }

    /**
     * Test {491=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_domManip___script_in_comments_are_properly_evaluated___11402_() throws Exception {
        runTest("ajax: jQuery.domManip - script in comments are properly evaluated (#11402)");
    }

    /**
     * Test {492=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___active_counter() throws Exception {
        runTest("ajax: jQuery.ajax - active counter");
    }

    /**
     * Test {493=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__sanity_check() throws Exception {
        runTest("effects: sanity check");
    }

    /**
     * Test {494=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void effects__show__() throws Exception {
        runTest("effects: show()");
    }

    /**
     * Test {495=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__show_Number____other_displays() throws Exception {
        runTest("effects: show(Number) - other displays");
    }

    /**
     * Test {496=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__Persist_correct_display_value() throws Exception {
        runTest("effects: Persist correct display value");
    }

    /**
     * Test {497=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_Hash__Object__Function_() throws Exception {
        runTest("effects: animate(Hash, Object, Function)");
    }

    /**
     * Test {498=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_height() throws Exception {
        runTest("effects: animate negative height");
    }

    /**
     * Test {499=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin() throws Exception {
        runTest("effects: animate negative margin");
    }

    /**
     * Test {500=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin_with_px() throws Exception {
        runTest("effects: animate negative margin with px");
    }

    /**
     * Test {501=[CHROME, EDGE, FF, FF-ESR]}.
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
     * Test {502=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__animate_block_as_inline_width_height() throws Exception {
        runTest("effects: animate block as inline width/height");
    }

    /**
     * Test {503=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__animate_native_inline_width_height() throws Exception {
        runTest("effects: animate native inline width/height");
    }

    /**
     * Test {504=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__animate_block_width_height() throws Exception {
        runTest("effects: animate block width/height");
    }

    /**
     * Test {505=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_table_width_height() throws Exception {
        runTest("effects: animate table width/height");
    }

    /**
     * Test {506=[CHROME, EDGE, FF, FF-ESR]}.
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
     * Test {507=[CHROME, EDGE, FF, FF-ESR]}.
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
     * Test {508=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_percentage____on_width_height() throws Exception {
        runTest("effects: animate percentage(%) on width/height");
    }

    /**
     * Test {509=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_resets_overflow_x_and_overflow_y_when_finished() throws Exception {
        runTest("effects: animate resets overflow-x and overflow-y when finished");
    }

    /**
     * Test {510=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__false__() throws Exception {
        runTest("effects: animate option { queue: false }");
    }

    /**
     * Test {511=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__true__() throws Exception {
        runTest("effects: animate option { queue: true }");
    }

    /**
     * Test {512=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_option___queue___name___() throws Exception {
        runTest("effects: animate option { queue: 'name' }");
    }

    /**
     * Test {513=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_with_no_properties() throws Exception {
        runTest("effects: animate with no properties");
    }

    /**
     * Test {514=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_duration_0() throws Exception {
        runTest("effects: animate duration 0");
    }

    /**
     * Test {515=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_hyphenated_properties() throws Exception {
        runTest("effects: animate hyphenated properties");
    }

    /**
     * Test {516=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_non_element() throws Exception {
        runTest("effects: animate non-element");
    }

    /**
     * Test {517=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop__() throws Exception {
        runTest("effects: stop()");
    }

    /**
     * Test {518=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop_____several_in_queue() throws Exception {
        runTest("effects: stop() - several in queue");
    }

    /**
     * Test {519=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop_clearQueue_() throws Exception {
        runTest("effects: stop(clearQueue)");
    }

    /**
     * Test {520=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__stop_clearQueue__gotoEnd_() throws Exception {
        runTest("effects: stop(clearQueue, gotoEnd)");
    }

    /**
     * Test {521=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop__queue_______________Stop_single_queues() throws Exception {
        runTest("effects: stop( queue, ..., ... ) - Stop single queues");
    }

    /**
     * Test {522=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__toggle__() throws Exception {
        runTest("effects: toggle()");
    }

    /**
     * Test {523=[CHROME, EDGE, FF, FF-ESR]}.
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
     * Test {524=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__JS_Overflow_and_Display() throws Exception {
        runTest("effects: JS Overflow and Display");
    }

    /**
     * Test {525=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__CSS_Overflow_and_Display() throws Exception {
        runTest("effects: CSS Overflow and Display");
    }

    /**
     * Test {526=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_0() throws Exception {
        runTest("effects: CSS Auto to 0");
    }

    /**
     * Test {527=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_50() throws Exception {
        runTest("effects: CSS Auto to 50");
    }

    /**
     * Test {528=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_100() throws Exception {
        runTest("effects: CSS Auto to 100");
    }

    /**
     * Test {529=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_Auto_to_show() throws Exception {
        runTest("effects: CSS Auto to show");
    }

    /**
     * Test {530=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_Auto_to_hide() throws Exception {
        runTest("effects: CSS Auto to hide");
    }

    /**
     * Test {531=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_0() throws Exception {
        runTest("effects: JS Auto to 0");
    }

    /**
     * Test {532=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_50() throws Exception {
        runTest("effects: JS Auto to 50");
    }

    /**
     * Test {533=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_100() throws Exception {
        runTest("effects: JS Auto to 100");
    }

    /**
     * Test {534=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_Auto_to_show() throws Exception {
        runTest("effects: JS Auto to show");
    }

    /**
     * Test {535=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_Auto_to_hide() throws Exception {
        runTest("effects: JS Auto to hide");
    }

    /**
     * Test {536=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_0() throws Exception {
        runTest("effects: CSS 100 to 0");
    }

    /**
     * Test {537=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_50() throws Exception {
        runTest("effects: CSS 100 to 50");
    }

    /**
     * Test {538=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_100() throws Exception {
        runTest("effects: CSS 100 to 100");
    }

    /**
     * Test {539=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_100_to_show() throws Exception {
        runTest("effects: CSS 100 to show");
    }

    /**
     * Test {540=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_100_to_hide() throws Exception {
        runTest("effects: CSS 100 to hide");
    }

    /**
     * Test {541=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_0() throws Exception {
        runTest("effects: JS 100 to 0");
    }

    /**
     * Test {542=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_50() throws Exception {
        runTest("effects: JS 100 to 50");
    }

    /**
     * Test {543=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_100() throws Exception {
        runTest("effects: JS 100 to 100");
    }

    /**
     * Test {544=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_100_to_show() throws Exception {
        runTest("effects: JS 100 to show");
    }

    /**
     * Test {545=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_100_to_hide() throws Exception {
        runTest("effects: JS 100 to hide");
    }

    /**
     * Test {546=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_0() throws Exception {
        runTest("effects: CSS 50 to 0");
    }

    /**
     * Test {547=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_50() throws Exception {
        runTest("effects: CSS 50 to 50");
    }

    /**
     * Test {548=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_100() throws Exception {
        runTest("effects: CSS 50 to 100");
    }

    /**
     * Test {549=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_50_to_show() throws Exception {
        runTest("effects: CSS 50 to show");
    }

    /**
     * Test {550=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_50_to_hide() throws Exception {
        runTest("effects: CSS 50 to hide");
    }

    /**
     * Test {551=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_0() throws Exception {
        runTest("effects: JS 50 to 0");
    }

    /**
     * Test {552=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_50() throws Exception {
        runTest("effects: JS 50 to 50");
    }

    /**
     * Test {553=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_100() throws Exception {
        runTest("effects: JS 50 to 100");
    }

    /**
     * Test {554=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_50_to_show() throws Exception {
        runTest("effects: JS 50 to show");
    }

    /**
     * Test {555=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_50_to_hide() throws Exception {
        runTest("effects: JS 50 to hide");
    }

    /**
     * Test {556=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_0() throws Exception {
        runTest("effects: CSS 0 to 0");
    }

    /**
     * Test {557=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_50() throws Exception {
        runTest("effects: CSS 0 to 50");
    }

    /**
     * Test {558=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_100() throws Exception {
        runTest("effects: CSS 0 to 100");
    }

    /**
     * Test {559=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_0_to_show() throws Exception {
        runTest("effects: CSS 0 to show");
    }

    /**
     * Test {560=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_0_to_hide() throws Exception {
        runTest("effects: CSS 0 to hide");
    }

    /**
     * Test {561=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_0() throws Exception {
        runTest("effects: JS 0 to 0");
    }

    /**
     * Test {562=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_50() throws Exception {
        runTest("effects: JS 0 to 50");
    }

    /**
     * Test {563=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_100() throws Exception {
        runTest("effects: JS 0 to 100");
    }

    /**
     * Test {564=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_0_to_show() throws Exception {
        runTest("effects: JS 0 to show");
    }

    /**
     * Test {565=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_0_to_hide() throws Exception {
        runTest("effects: JS 0 to hide");
    }

    /**
     * Test {566=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeOut_fadeIn() throws Exception {
        runTest("effects: Chain fadeOut fadeIn");
    }

    /**
     * Test {567=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeIn_fadeOut() throws Exception {
        runTest("effects: Chain fadeIn fadeOut");
    }

    /**
     * Test {568=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_hide_show() throws Exception {
        runTest("effects: Chain hide show");
    }

    /**
     * Test {569=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_show_hide() throws Exception {
        runTest("effects: Chain show hide");
    }

    /**
     * Test {570=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_show_hide_with_easing_and_callback() throws Exception {
        runTest("effects: Chain show hide with easing and callback");
    }

    /**
     * Test {571=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_in() throws Exception {
        runTest("effects: Chain toggle in");
    }

    /**
     * Test {572=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_out() throws Exception {
        runTest("effects: Chain toggle out");
    }

    /**
     * Test {573=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_out_with_easing_and_callback() throws Exception {
        runTest("effects: Chain toggle out with easing and callback");
    }

    /**
     * Test {574=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideDown_slideUp() throws Exception {
        runTest("effects: Chain slideDown slideUp");
    }

    /**
     * Test {575=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideUp_slideDown() throws Exception {
        runTest("effects: Chain slideUp slideDown");
    }

    /**
     * Test {576=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideUp_slideDown_with_easing_and_callback() throws Exception {
        runTest("effects: Chain slideUp slideDown with easing and callback");
    }

    /**
     * Test {577=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideToggle_in() throws Exception {
        runTest("effects: Chain slideToggle in");
    }

    /**
     * Test {578=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideToggle_out() throws Exception {
        runTest("effects: Chain slideToggle out");
    }

    /**
     * Test {579=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeToggle_in() throws Exception {
        runTest("effects: Chain fadeToggle in");
    }

    /**
     * Test {580=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeToggle_out() throws Exception {
        runTest("effects: Chain fadeToggle out");
    }

    /**
     * Test {581=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeTo_0_5_1_0_with_easing_and_callback_() throws Exception {
        runTest("effects: Chain fadeTo 0.5 1.0 with easing and callback)");
    }

    /**
     * Test {582=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_show__fast___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("effects: jQuery.show('fast') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {583=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__slideToggle___stop___slideToggle__() throws Exception {
        runTest("effects: slideToggle().stop().slideToggle()");
    }

    /**
     * Test {584=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__fadeToggle___stop___fadeToggle__() throws Exception {
        runTest("effects: fadeToggle().stop().fadeToggle()");
    }

    /**
     * Test {585=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__toggle___stop___toggle__() throws Exception {
        runTest("effects: toggle().stop().toggle()");
    }

    /**
     * Test {586=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_with_per_property_easing() throws Exception {
        runTest("effects: animate with per-property easing");
    }

    /**
     * Test {587=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_with_CSS_shorthand_properties() throws Exception {
        runTest("effects: animate with CSS shorthand properties");
    }

    /**
     * Test {588=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_hidden_elements__with_animation__bug__7141_() throws Exception {
        runTest("effects: hide hidden elements, with animation (bug #7141)");
    }

    /**
     * Test {589=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_unit_less_properties___4966_() throws Exception {
        runTest("effects: animate unit-less properties (#4966)");
    }

    /**
     * Test {590=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__animate_properties_missing_px_w__opacity_as_last___9074_() throws Exception {
        runTest("effects: animate properties missing px w/ opacity as last (#9074)");
    }

    /**
     * Test {591=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__callbacks_should_fire_in_correct_order___9100_() throws Exception {
        runTest("effects: callbacks should fire in correct order (#9100)");
    }

    /**
     * Test {592=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__callbacks_that_throw_exceptions_will_be_removed___5684_() throws Exception {
        runTest("effects: callbacks that throw exceptions will be removed (#5684)");
    }

    /**
     * Test {593=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_will_scale_margin_properties_individually() throws Exception {
        runTest("effects: animate will scale margin properties individually");
    }

    /**
     * Test {594=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("effects: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {595=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_Animation__object__props__opts__() throws Exception {
        runTest("effects: jQuery.Animation( object, props, opts )");
    }

    /**
     * Test {596=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animate_Option__step__function__percent__tween__() throws Exception {
        runTest("effects: Animate Option: step: function( percent, tween )");
    }

    /**
     * Test {597=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__Animate_callbacks_have_correct_context() throws Exception {
        runTest("effects: Animate callbacks have correct context");
    }

    /**
     * Test {598=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__User_supplied_callback_called_after_show_when_fx_off___8892_() throws Exception {
        runTest("effects: User supplied callback called after show when fx off (#8892)");
    }

    /**
     * Test {599=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void effects__animate_should_set_display_for_disconnected_nodes() throws Exception {
        runTest("effects: animate should set display for disconnected nodes");
    }

    /**
     * Test {600=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animation_callback_should_not_show_animated_element_as_animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as animated (#7157)");
    }

    /**
     * Test {601=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_called_on_element_within_hidden_parent_should_set_display_to_none___10045_() throws Exception {
        runTest("effects: hide called on element within hidden parent should set display to none (#10045)");
    }

    /**
     * Test {602=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__hide__fadeOut_and_slideUp_called_on_element_width_height_and_width___0_should_set_display_to_none() throws Exception {
        runTest("effects: hide, fadeOut and slideUp called on element width height and width = 0 should set display to none");
    }

    /**
     * Test {603=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void effects__Handle_queue_false_promises() throws Exception {
        runTest("effects: Handle queue:false promises");
    }

    /**
     * Test {604=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__multiple_unqueued_and_promise() throws Exception {
        runTest("effects: multiple unqueued and promise");
    }

    /**
     * Test {605=[CHROME, EDGE, FF, FF-ESR]}.
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
     * Test {606=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__non_px_animation_handles_non_numeric_start___11971_() throws Exception {
        runTest("effects: non-px animation handles non-numeric start (#11971)");
    }

    /**
     * Test {607=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__Animation_callbacks___11797_() throws Exception {
        runTest("effects: Animation callbacks (#11797)");
    }

    /**
     * Test {608=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__Animate_properly_sets_overflow_hidden_when_animating_width_height___12117_() throws Exception {
        runTest("effects: Animate properly sets overflow hidden when animating width/height (#12117)");
    }

    /**
     * Test {609=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animations_with_0_duration_don_t_ease___12273_() throws Exception {
        runTest("effects: Animations with 0 duration don't ease (#12273)");
    }

    /**
     * Test {610=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__empty_set() throws Exception {
        runTest("offset: empty set");
    }

    /**
     * Test {611=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__object_without_getBoundingClientRect() throws Exception {
        runTest("offset: object without getBoundingClientRect");
    }

    /**
     * Test {612=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__disconnected_node() throws Exception {
        runTest("offset: disconnected node");
    }

    /**
     * Test {614=[CHROME, EDGE, FF, FF-ESR]}.
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
     * Test {615=[CHROME, EDGE, FF, FF-ESR]}.
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
     * Test {616=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 80, 80")
    public void offset__static() throws Exception {
        runTest("offset: static");
    }

    /**
     * Test {617=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    public void offset__fixed() throws Exception {
        runTest("offset: fixed");
    }

    /**
     * Test {618=[CHROME, EDGE, FF, FF-ESR]}.
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
     * Test {619=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "6, 18, 24",
            EDGE = "6, 18, 24",
            FF = "2, 22, 24",
            FF_ESR = "2, 22, 24")
    @HtmlUnitNYI(CHROME = "2, 22, 24",
            EDGE = "2, 22, 24")
    public void offset__scroll() throws Exception {
        runTest("offset: scroll");
    }

    /**
     * Test {620=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__body() throws Exception {
        runTest("offset: body");
    }

    /**
     * Test {621=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void offset__chaining() throws Exception {
        runTest("offset: chaining");
    }

    /**
     * Test {622=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void offset__offsetParent() throws Exception {
        runTest("offset: offsetParent");
    }

    /**
     * Test {623=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "1, 1, 2",
            EDGE = "1, 1, 2",
            FF = "0, 2, 2",
            FF_ESR = "0, 2, 2")
    @HtmlUnitNYI(CHROME = "0, 2, 2",
            EDGE = "0, 2, 2")
    public void offset__fractions__see__7730_and__7885_() throws Exception {
        runTest("offset: fractions (see #7730 and #7885)");
    }

    /**
     * Test {624=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width__() throws Exception {
        runTest("dimensions: width()");
    }

    /**
     * Test {625=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width_Function_() throws Exception {
        runTest("dimensions: width(Function)");
    }

    /**
     * Test {626=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__width_Function_args__() throws Exception {
        runTest("dimensions: width(Function(args))");
    }

    /**
     * Test {627=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height__() throws Exception {
        runTest("dimensions: height()");
    }

    /**
     * Test {628=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height_Function_() throws Exception {
        runTest("dimensions: height(Function)");
    }

    /**
     * Test {629=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__height_Function_args__() throws Exception {
        runTest("dimensions: height(Function(args))");
    }

    /**
     * Test {630=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerWidth__() throws Exception {
        runTest("dimensions: innerWidth()");
    }

    /**
     * Test {631=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerHeight__() throws Exception {
        runTest("dimensions: innerHeight()");
    }

    /**
     * Test {632=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerWidth__() throws Exception {
        runTest("dimensions: outerWidth()");
    }

    /**
     * Test {633=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__9441__9300() throws Exception {
        runTest("dimensions: child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #9441 #9300");
    }

    /**
     * Test {634=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void dimensions__getting_dimensions_shouldnt_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldnt modify runtimeStyle see #9233");
    }

    /**
     * Test {635=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__table_dimensions() throws Exception {
        runTest("dimensions: table dimensions");
    }

    /**
     * Test {636=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__box_sizing_border_box_child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__10413() throws Exception {
        runTest("dimensions: box-sizing:border-box child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #10413");
    }

    /**
     * Test {637=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerHeight__() throws Exception {
        runTest("dimensions: outerHeight()");
    }

    /**
     * Test {638=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void dimensions__passing_undefined_is_a_setter__5571() throws Exception {
        runTest("dimensions: passing undefined is a setter #5571");
    }

    /**
     * Test {639=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void dimensions__getters_on_non_elements_should_return_null() throws Exception {
        runTest("dimensions: getters on non elements should return null");
    }

    /**
     * Test {640=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void dimensions__setters_with_and_without_box_sizing_border_box() throws Exception {
        runTest("dimensions: setters with and without box-sizing:border-box");
    }

    /**
     * Test {641=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void dimensions__window_vs__small_document() throws Exception {
        runTest("dimensions: window vs. small document");
    }

    /**
     * Test {642=[CHROME, EDGE, FF, FF-ESR]}.
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

    /**
     * Test {643=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 544, 544")
    public void deprecated__browser() throws Exception {
        runTest("deprecated: browser");
    }

    /**
     * Test {644=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void exports__amdModule() throws Exception {
        runTest("exports: amdModule");
    }

    /**
     * Test {645=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 36, 36")
    public void Sizzle__selector__element() throws Exception {
        runTest("Sizzle: selector: element");
    }

    /**
     * Test {646=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    @HtmlUnitNYI(CHROME = "4, 6, 10",
            EDGE = "4, 6, 10",
            FF = "4, 6, 10",
            FF_ESR = "4, 6, 10")
    public void Sizzle__selector__XML_Document_Selectors() throws Exception {
        runTest("Sizzle: selector: XML Document Selectors");
    }

    /**
     * Test {647=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void Sizzle__selector__broken() throws Exception {
        runTest("Sizzle: selector: broken");
    }

    /**
     * Test {648=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void Sizzle__selector__id() throws Exception {
        runTest("Sizzle: selector: id");
    }

    /**
     * Test {649=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void Sizzle__selector__class() throws Exception {
        runTest("Sizzle: selector: class");
    }

    /**
     * Test {650=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void Sizzle__selector__name() throws Exception {
        runTest("Sizzle: selector: name");
    }

    /**
     * Test {651=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void Sizzle__selector__multiple() throws Exception {
        runTest("Sizzle: selector: multiple");
    }

    /**
     * Test {652=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    public void Sizzle__selector__child_and_adjacent() throws Exception {
        runTest("Sizzle: selector: child and adjacent");
    }

    /**
     * Test {653=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 62, 62")
    public void Sizzle__selector__attributes() throws Exception {
        runTest("Sizzle: selector: attributes");
    }

    /**
     * Test {654=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    public void Sizzle__selector__pseudo___child() throws Exception {
        runTest("Sizzle: selector: pseudo - child");
    }

    /**
     * Test {655=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    public void Sizzle__selector__pseudo___misc() throws Exception {
        runTest("Sizzle: selector: pseudo - misc");
    }

    /**
     * Test {656=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 43, 43")
    public void Sizzle__selector__pseudo____not() throws Exception {
        runTest("Sizzle: selector: pseudo - :not");
    }

    /**
     * Test {657=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 33, 33")
    public void Sizzle__selector__pseudo___position() throws Exception {
        runTest("Sizzle: selector: pseudo - position");
    }

    /**
     * Test {658=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void Sizzle__selector__pseudo___form() throws Exception {
        runTest("Sizzle: selector: pseudo - form");
    }

    /**
     * Test {659=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void Sizzle__selector__caching() throws Exception {
        runTest("Sizzle: selector: caching");
    }

    /**
     * Test {660=[CHROME, EDGE, FF, FF-ESR]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void Sizzle__selector__Sizzle_contains() throws Exception {
        runTest("Sizzle: selector: Sizzle.contains");
    }
}
