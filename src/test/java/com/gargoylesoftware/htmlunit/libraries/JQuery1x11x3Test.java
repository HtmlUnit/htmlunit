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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;

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
    protected String getVersion() {
        return "1.11.3";
    }

    /**
     * Test {1=[CHROME, FF31, IE11]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        IE11 = "0, 2, 2")
    public void event__jQuery_isReady() throws Exception {
        runTest("event: jQuery.isReady");
    }

    /**
     * Test {1=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void ready__jQuery_isReady() throws Exception {
        runTest("ready: jQuery.isReady");
    }

    /**
     * Test {2=[CHROME, FF31, IE11]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        IE11 = "0, 10, 10")
    public void event__jQuery_ready() throws Exception {
        runTest("event: jQuery ready");
    }

    /**
     * Test {2=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "10")
    public void ready__jQuery_ready() throws Exception {
        runTest("ready: jQuery ready");
    }

    /**
     * Test {3=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "4")
    public void basic__ajax() throws Exception {
        runTest("basic: ajax");
    }

    /**
     * Test {3=[CHROME, FF31, IE11]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        IE11 = "0, 2, 2")
    public void core__Unit_Testing_Environment() throws Exception {
        runTest("core: Unit Testing Environment");
    }

    /**
     * Test {4=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "6")
    public void basic__attributes() throws Exception {
        runTest("basic: attributes");
    }

    /**
     * Test {4=[CHROME, FF31, IE11], 16=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void core__Basic_requirements() throws Exception {
        runTest("core: Basic requirements");
    }

    /**
     * Test {5=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void basic__css() throws Exception {
        runTest("basic: css");
    }

    /**
     * Test {5=[CHROME, FF31, IE11], 17=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 28, 28",
        FF31 = "0, 28, 28",
        FF38 = "30",
        IE11 = "0, 28, 28")
    public void core__jQuery__() throws Exception {
        runTest("core: jQuery()");
    }

    /**
     * Test {6=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "28")
    public void basic__core() throws Exception {
        runTest("basic: core");
    }

    /**
     * Test {6=[CHROME, FF31, IE11], 18=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void core__jQuery_selector__context_() throws Exception {
        runTest("core: jQuery(selector, context)");
    }

    /**
     * Test {7=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "4")
    public void basic__data() throws Exception {
        runTest("basic: data");
    }

    /**
     * Test {7=[CHROME, FF31, IE11], 19=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 18, 18",
        FF31 = "0, 18, 18",
        FF38 = "18",
        IE11 = "0, 18, 18")
    public void core__selector_state() throws Exception {
        runTest("core: selector state");
    }

    /**
     * Test {8=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void basic__dimensions() throws Exception {
        runTest("basic: dimensions");
    }

    /**
     * Test {8=[CHROME, FF31, IE11], 20=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "2",
        IE11 = "0, 3, 3")
    public void core__globalEval() throws Exception {
        runTest("core: globalEval");
    }

    /**
     * Test {9=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void basic__event() throws Exception {
        runTest("basic: event");
    }

    /**
     * Test {9=[CHROME, FF31, IE11], 22=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void core__noConflict() throws Exception {
        runTest("core: noConflict");
    }

    /**
     * Test {10=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "5")
    public void basic__manipulation() throws Exception {
        runTest("basic: manipulation");
    }

    /**
     * Test {10=[CHROME, FF31, IE11], 23=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 13, 13",
        FF31 = "0, 13, 13",
        FF38 = "13",
        IE11 = "0, 13, 13")
    public void core__trim() throws Exception {
        runTest("core: trim");
    }

    /**
     * Test {11=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void basic__offset() throws Exception {
        runTest("basic: offset");
    }

    /**
     * Test {11=[CHROME, FF31, IE11], 24=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 28, 28",
        FF31 = "0, 28, 28",
        FF38 = "28",
        IE11 = "0, 28, 28")
    public void core__type() throws Exception {
        runTest("core: type");
    }

    /**
     * Test {12=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void basic__selector() throws Exception {
        runTest("basic: selector");
    }

    /**
     * Test {12=[CHROME, FF31, IE11], 26=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 16, 16",
        FF31 = "0, 16, 16",
        FF38 = "16",
        IE11 = "0, 16, 16")
    public void core__isPlainObject() throws Exception {
        runTest("core: isPlainObject");
    }

    /**
     * Test {13=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void basic__serialize() throws Exception {
        runTest("basic: serialize");
    }

    /**
     * Test {13=[CHROME, FF31, IE11], 28=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 19, 19",
        FF31 = "0, 19, 19",
        FF38 = "19",
        IE11 = "0, 19, 19")
    public void core__isFunction() throws Exception {
        runTest("core: isFunction");
    }

    /**
     * Test {14=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "12")
    public void basic__traversing() throws Exception {
        runTest("basic: traversing");
    }

    /**
     * Test {14=[CHROME, FF31, IE11], 29=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 38, 38",
        FF31 = "0, 38, 38",
        FF38 = "38",
        IE11 = "0, 38, 38")
    public void core__isNumeric() throws Exception {
        runTest("core: isNumeric");
    }

    /**
     * Test {15=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void basic__wrap() throws Exception {
        runTest("basic: wrap");
    }

    /**
     * Test {15=[CHROME, FF31, IE11], 31=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void core__isXMLDoc___HTML() throws Exception {
        runTest("core: isXMLDoc - HTML");
    }

    /**
     * Test {16=[CHROME, FF31, IE11], 32=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void core__XSS_via_location_hash() throws Exception {
        runTest("core: XSS via location.hash");
    }

    /**
     * Test {17=[CHROME, FF31, IE11], 33=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void core__isXMLDoc___XML() throws Exception {
        runTest("core: isXMLDoc - XML");
    }

    /**
     * Test {18=[CHROME, FF31, IE11], 34=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 14, 14",
        FF31 = "0, 14, 14",
        FF38 = "14",
        IE11 = "0, 14, 14")
    public void core__isWindow() throws Exception {
        runTest("core: isWindow");
    }

    /**
     * Test {19=[CHROME, FF31, IE11], 35=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 18, 18",
        FF31 = "0, 18, 18",
        FF38 = "18",
        IE11 = "0, 18, 18")
    public void core__jQuery__html__() throws Exception {
        runTest("core: jQuery('html')");
    }

    /**
     * Test {20=[CHROME, FF31, IE11], 37=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void core__jQuery__massive_html__7990__() throws Exception {
        runTest("core: jQuery('massive html #7990')");
    }

    /**
     * Test {21=[CHROME, FF31, IE11], 38=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void core__jQuery__html___context_() throws Exception {
        runTest("core: jQuery('html', context)");
    }

    /**
     * Test {22=[CHROME, FF31, IE11], 39=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void core__jQuery_selector__xml__text_str____loaded_via_xml_document() throws Exception {
        runTest("core: jQuery(selector, xml).text(str) - loaded via xml document");
    }

    /**
     * Test {23=[CHROME, FF31, IE11], 40=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void core__end__() throws Exception {
        runTest("core: end()");
    }

    /**
     * Test {24=[CHROME, FF31, IE11], 41=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void core__length() throws Exception {
        runTest("core: length");
    }

    /**
     * Test {25=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void core__type_for__Symbol_() throws Exception {
        runTest("core: type for `Symbol`");
    }

    /**
     * Test {25=[CHROME, FF31, IE11], 42=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void core__get__() throws Exception {
        runTest("core: get()");
    }

    /**
     * Test {26=[CHROME, FF31, IE11], 43=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void core__toArray__() throws Exception {
        runTest("core: toArray()");
    }

    /**
     * Test {27=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void core__isPlainObject_Symbol_() throws Exception {
        runTest("core: isPlainObject(Symbol)");
    }

    /**
     * Test {27=[CHROME, FF31, IE11], 44=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 19, 19",
        FF31 = "0, 19, 19",
        FF38 = "19",
        IE11 = "0, 19, 19")
    public void core__inArray__() throws Exception {
        runTest("core: inArray()");
    }

    /**
     * Test {28=[CHROME, FF31, IE11], 45=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void core__get_Number_() throws Exception {
        runTest("core: get(Number)");
    }

    /**
     * Test {29=[CHROME, FF31, IE11], 46=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void core__get__Number_() throws Exception {
        runTest("core: get(-Number)");
    }

    /**
     * Test {30=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void core__isNumeric_Symbol_() throws Exception {
        runTest("core: isNumeric(Symbol)");
    }

    /**
     * Test {30=[CHROME, FF31, IE11], 47=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void core__each_Function_() throws Exception {
        runTest("core: each(Function)");
    }

    /**
     * Test {31=[CHROME, FF31, IE11], 48=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void core__slice__() throws Exception {
        runTest("core: slice()");
    }

    /**
     * Test {32=[CHROME, FF31, IE11], 49=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void core__first___last__() throws Exception {
        runTest("core: first()/last()");
    }

    /**
     * Test {33=[CHROME, FF31, IE11], 50=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void core__map__() throws Exception {
        runTest("core: map()");
    }

    /**
     * Test {34=[CHROME, FF31, IE11], 51=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 25, 25",
        FF31 = "0, 25, 25",
        FF38 = "25",
        IE11 = "0, 25, 25")
    public void core__jQuery_map() throws Exception {
        runTest("core: jQuery.map");
    }

    /**
     * Test {35=[CHROME, FF31, IE11], 52=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void core__jQuery_merge__() throws Exception {
        runTest("core: jQuery.merge()");
    }

    /**
     * Test {36=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "17")
    public void core__jQuery_tag_hyphenated_elements__gh_1987() throws Exception {
        runTest("core: jQuery(tag-hyphenated elements) gh-1987");
    }

    /**
     * Test {36=[CHROME, FF31, IE11], 53=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void core__jQuery_grep__() throws Exception {
        runTest("core: jQuery.grep()");
    }

    /**
     * Test {37=[CHROME, FF31, IE11], 55=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 28, 28",
        FF31 = "0, 28, 28",
        FF38 = "28",
        IE11 = "0, 28, 28")
    public void core__jQuery_extend_Object__Object_() throws Exception {
        runTest("core: jQuery.extend(Object, Object)");
    }

    /**
     * Test {38=[CHROME, FF31, IE11], 57=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 23, 23",
        FF31 = "0, 23, 23",
        FF38 = "23",
        IE11 = "0, 23, 23")
    public void core__jQuery_each_Object_Function_() throws Exception {
        runTest("core: jQuery.each(Object,Function)");
    }

    /**
     * Test {39=[CHROME, FF31, IE11], 59=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void core__JIT_compilation_does_not_interfere_with_length_retrieval__gh_2145_() throws Exception {
        runTest("core: JIT compilation does not interfere with length retrieval (gh-2145)");
    }

    /**
     * Test {40=[CHROME, FF31, IE11], 60=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 15, 15",
        FF31 = "0, 15, 15",
        FF38 = "15",
        IE11 = "0, 15, 15")
    public void core__jQuery_makeArray() throws Exception {
        runTest("core: jQuery.makeArray");
    }

    /**
     * Test {41=[CHROME, FF31, IE11], 61=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void core__jQuery_inArray() throws Exception {
        runTest("core: jQuery.inArray");
    }

    /**
     * Test {42=[CHROME, FF31, IE11], 62=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void core__jQuery_isEmptyObject() throws Exception {
        runTest("core: jQuery.isEmptyObject");
    }

    /**
     * Test {43=[CHROME, FF31, IE11], 63=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void core__jQuery_proxy() throws Exception {
        runTest("core: jQuery.proxy");
    }

    /**
     * Test {44=[CHROME, FF31, IE11], 65=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 17, 17",
        FF31 = "0, 17, 17",
        FF38 = "1",
        IE11 = "0, 17, 17")
    public void core__jQuery_parseHTML() throws Exception {
        runTest("core: jQuery.parseHTML");
    }

    /**
     * Test {45=[CHROME, FF31, IE11], 66=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 20, 20",
        FF31 = "0, 20, 20",
        FF38 = "20",
        IE11 = "0, 20, 20")
    public void core__jQuery_parseJSON() throws Exception {
        runTest("core: jQuery.parseJSON");
    }

    /**
     * Test {46=[CHROME, FF31, IE11], 67=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    @NotYetImplemented(CHROME)
    public void core__jQuery_parseXML() throws Exception {
        runTest("core: jQuery.parseXML");
    }

    /**
     * Test {47=[CHROME, FF31, IE11], 68=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void core__jQuery_camelCase__() throws Exception {
        runTest("core: jQuery.camelCase()");
    }

    /**
     * Test {48=[CHROME, FF31, IE11], 69=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void core__Conditional_compilation_compatibility___13274_() throws Exception {
        runTest("core: Conditional compilation compatibility (#13274)");
    }

    /**
     * Test {49=[CHROME, FF31, IE11], 70=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void core__document_ready_when_jQuery_loaded_asynchronously___13655_() throws Exception {
        runTest("core: document ready when jQuery loaded asynchronously (#13655)");
    }

    /**
     * Test {50=[CHROME, FF31, IE11], 71=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void core__Tolerating_alias_masked_DOM_properties___14074_() throws Exception {
        runTest("core: Tolerating alias-masked DOM properties (#14074)");
    }

    /**
     * Test {51=[CHROME, FF31, IE11], 72=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void core__Don_t_call_window_onready___14802_() throws Exception {
        runTest("core: Don't call window.onready (#14802)");
    }

    /**
     * Test {52=[CHROME, FF31, IE11], 74=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - no filter");
    }

    /**
     * Test {53=[CHROME, FF31, IE11], 75=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks__________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - no filter");
    }

    /**
     * Test {54=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "7")
    public void core__jQuery_grep_Array_like_() throws Exception {
        runTest("core: jQuery.grep(Array-like)");
    }

    /**
     * Test {54=[CHROME, FF31, IE11], 76=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( '' ) - filter");
    }

    /**
     * Test {55=[CHROME, FF31, IE11], 77=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks__________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - filter");
    }

    /**
     * Test {56=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void core__jQuery_extend_Object__Object__created_with__defineProperties___() throws Exception {
        runTest("core: jQuery.extend(Object, Object {created with \"defineProperties\"})");
    }

    /**
     * Test {56=[CHROME, FF31, IE11], 78=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - no filter");
    }

    /**
     * Test {57=[CHROME, FF31, IE11], 79=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - no filter");
    }

    /**
     * Test {58=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void core__jQuery_each_map_undefined_null_Function_() throws Exception {
        runTest("core: jQuery.each/map(undefined/null,Function)");
    }

    /**
     * Test {58=[CHROME, FF31, IE11], 80=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once' ) - filter");
    }

    /**
     * Test {59=[CHROME, FF31, IE11], 81=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true } ) - filter");
    }

    /**
     * Test {60=[CHROME, FF31, IE11], 82=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - no filter");
    }

    /**
     * Test {61=[CHROME, FF31, IE11], 83=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - no filter");
    }

    /**
     * Test {62=[CHROME, FF31, IE11], 84=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory' ) - filter");
    }

    /**
     * Test {63=[CHROME, FF31, IE11], 85=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true } ) - filter");
    }

    /**
     * Test {64=[CHROME, FF31, IE11], 86=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - no filter");
    }

    /**
     * Test {65=[CHROME, FF31, IE11], 87=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - no filter");
    }

    /**
     * Test {66=[CHROME, FF31, IE11], 88=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique' ) - filter");
    }

    /**
     * Test {67=[CHROME, FF31, IE11], 89=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true } ) - filter");
    }

    /**
     * Test {68=[CHROME, FF31, IE11], 90=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - no filter");
    }

    /**
     * Test {69=[CHROME, FF31, IE11], 91=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {70=[CHROME, FF31, IE11], 92=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'stopOnFalse' ) - filter");
    }

    /**
     * Test {71=[CHROME, FF31, IE11], 93=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {72=[CHROME, FF31, IE11], 94=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - no filter");
    }

    /**
     * Test {73=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void core__Iterability_of_jQuery_objects__gh_1693_() throws Exception {
        runTest("core: Iterability of jQuery objects (gh-1693)");
    }

    /**
     * Test {73=[CHROME, FF31, IE11], 95=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - no filter");
    }

    /**
     * Test {74=[CHROME, FF31, IE11], 96=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once memory' ) - filter");
    }

    /**
     * Test {75=[CHROME, FF31, IE11], 97=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'memory': true } ) - filter");
    }

    /**
     * Test {76=[CHROME, FF31, IE11], 98=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - no filter");
    }

    /**
     * Test {77=[CHROME, FF31, IE11], 99=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {78=[CHROME, FF31, IE11], 100=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once unique' ) - filter");
    }

    /**
     * Test {79=[CHROME, FF31, IE11], 101=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'unique': true } ) - filter");
    }

    /**
     * Test {80=[CHROME, FF31, IE11], 102=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - no filter");
    }

    /**
     * Test {81=[CHROME, FF31, IE11], 103=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {82=[CHROME, FF31, IE11], 104=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'once stopOnFalse' ) - filter");
    }

    /**
     * Test {83=[CHROME, FF31, IE11], 105=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'once': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {84=[CHROME, FF31, IE11], 106=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - no filter");
    }

    /**
     * Test {85=[CHROME, FF31, IE11], 107=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - no filter");
    }

    /**
     * Test {86=[CHROME, FF31, IE11], 108=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory unique' ) - filter");
    }

    /**
     * Test {87=[CHROME, FF31, IE11], 109=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'unique': true } ) - filter");
    }

    /**
     * Test {88=[CHROME, FF31, IE11], 110=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - no filter");
    }

    /**
     * Test {89=[CHROME, FF31, IE11], 111=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {90=[CHROME, FF31, IE11], 112=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'memory stopOnFalse' ) - filter");
    }

    /**
     * Test {91=[CHROME, FF31, IE11], 113=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'memory': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {92=[CHROME, FF31, IE11], 114=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - no filter");
    }

    /**
     * Test {93=[CHROME, FF31, IE11], 115=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - no filter");
    }

    /**
     * Test {94=[CHROME, FF31, IE11], 116=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( 'unique stopOnFalse' ) - filter");
    }

    /**
     * Test {95=[CHROME, FF31, IE11], 117=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 21, 21",
        FF31 = "0, 21, 21",
        FF38 = "28",
        IE11 = "0, 21, 21")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { 'unique': true, 'stopOnFalse': true } ) - filter");
    }

    /**
     * Test {96=[CHROME, FF31, IE11], 118=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void callbacks__jQuery_Callbacks__options_____options_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks( options ) - options are copied");
    }

    /**
     * Test {97=[CHROME, FF31, IE11], 119=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void callbacks__jQuery_Callbacks_fireWith___arguments_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks.fireWith - arguments are copied");
    }

    /**
     * Test {98=[CHROME, FF31, IE11], 120=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void callbacks__jQuery_Callbacks_remove___should_remove_all_instances() throws Exception {
        runTest("callbacks: jQuery.Callbacks.remove - should remove all instances");
    }

    /**
     * Test {99=[CHROME, FF31, IE11], 121=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 13, 13",
        FF31 = "0, 13, 13",
        FF38 = "13",
        IE11 = "0, 13, 13")
    public void callbacks__jQuery_Callbacks_has() throws Exception {
        runTest("callbacks: jQuery.Callbacks.has");
    }

    /**
     * Test {100=[CHROME, FF31, IE11], 122=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void callbacks__jQuery_Callbacks_____adding_a_string_doesn_t_cause_a_stack_overflow() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow");
    }

    /**
     * Test {101=[CHROME, FF31, IE11], 124=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 23, 23",
        FF31 = "0, 23, 23",
        FF38 = "23",
        IE11 = "0, 23, 23")
    public void deferred__jQuery_Deferred() throws Exception {
        runTest("deferred: jQuery.Deferred");
    }

    /**
     * Test {102=[CHROME, FF31, IE11], 125=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 23, 23",
        FF31 = "0, 23, 23",
        FF38 = "23",
        IE11 = "0, 23, 23")
    public void deferred__jQuery_Deferred___new_operator() throws Exception {
        runTest("deferred: jQuery.Deferred - new operator");
    }

    /**
     * Test {103=[CHROME, FF31, IE11], 126=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void deferred__jQuery_Deferred___chainability() throws Exception {
        runTest("deferred: jQuery.Deferred - chainability");
    }

    /**
     * Test {104=[CHROME, FF31, IE11], 127=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (done)");
    }

    /**
     * Test {105=[CHROME, FF31, IE11], 128=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (fail)");
    }

    /**
     * Test {106=[CHROME, FF31, IE11], 129=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void deferred__jQuery_Deferred_then___filtering__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (progress)");
    }

    /**
     * Test {107=[CHROME, FF31, IE11], 130=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (done)");
    }

    /**
     * Test {108=[CHROME, FF31, IE11], 131=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (fail)");
    }

    /**
     * Test {109=[CHROME, FF31, IE11], 132=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (progress)");
    }

    /**
     * Test {110=[CHROME, FF31, IE11], 133=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void deferred__jQuery_Deferred_then___context() throws Exception {
        runTest("deferred: jQuery.Deferred.then - context");
    }

    /**
     * Test {111=[CHROME, FF31, IE11], 134=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 37, 37",
        FF31 = "0, 37, 37",
        FF38 = "37",
        IE11 = "0, 37, 37")
    public void deferred__jQuery_when() throws Exception {
        runTest("deferred: jQuery.when");
    }

    /**
     * Test {112=[CHROME, FF31, IE11], 135=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 119, 119",
        FF31 = "0, 119, 119",
        FF38 = "119",
        IE11 = "0, 119, 119")
    public void deferred__jQuery_when___joined() throws Exception {
        runTest("deferred: jQuery.when - joined");
    }

    /**
     * Test {113=[CHROME, FF31, IE11], 140=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void support__zoom_of_doom___13089_() throws Exception {
        runTest("support: zoom of doom (#13089)");
    }

    /**
     * Test {114=[CHROME, FF31, IE11], 141=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void support__body_background_is_not_lost_if_set_prior_to_loading_jQuery___9239_() throws Exception {
        runTest("support: body background is not lost if set prior to loading jQuery (#9239)");
    }

    /**
     * Test {115=[CHROME, FF31, IE11], 143=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void support__A_background_on_the_testElement_does_not_cause_IE8_to_crash___9823_() throws Exception {
        runTest("support: A background on the testElement does not cause IE8 to crash (#9823)");
    }

    /**
     * Test {116=[CHROME, FF31, IE11], 142=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void support__box_sizing_does_not_affect_jQuery_support_shrinkWrapBlocks() throws Exception {
        runTest("support: box-sizing does not affect jQuery.support.shrinkWrapBlocks");
    }

    /**
     * Test {117=[CHROME, FF31, IE11], 144=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void support__Check_CSP__https___developer_mozilla_org_en_US_docs_Security_CSP__restrictions() throws Exception {
        runTest("support: Check CSP (https://developer.mozilla.org/en-US/docs/Security/CSP) restrictions");
    }

    /**
     * Test {118=[CHROME, FF31, IE11]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 34, 34",
        FF31 = "0, 34, 34",
        IE11 = "0, 34, 34")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void support__Verify_that_the_support_tests_resolve_as_expected_per_browser() throws Exception {
        runTest("support: Verify that the support tests resolve as expected per browser");
    }

    /**
     * Test {119=[CHROME, FF31, IE11], 146=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void data__expando() throws Exception {
        runTest("data: expando");
    }

    /**
     * Test {120=[CHROME, FF31, IE11], 147=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 25, 25",
        FF31 = "0, 25, 25",
        FF38 = "25",
        IE11 = "0, 25, 25")
    public void data__jQuery_data_div_() throws Exception {
        runTest("data: jQuery.data(div)");
    }

    /**
     * Test {121=[CHROME, FF31, IE11], 148=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 25, 25",
        FF31 = "0, 25, 25",
        FF38 = "25",
        IE11 = "0, 25, 25")
    public void data__jQuery_data____() throws Exception {
        runTest("data: jQuery.data({})");
    }

    /**
     * Test {122=[CHROME, FF31, IE11], 149=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 25, 25",
        FF31 = "0, 25, 25",
        FF38 = "25",
        IE11 = "0, 25, 25")
    public void data__jQuery_data_window_() throws Exception {
        runTest("data: jQuery.data(window)");
    }

    /**
     * Test {123=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void callbacks__jQuery_Callbacks_____disabled_callback_doesn_t_fire__gh_1790_() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - disabled callback doesn't fire (gh-1790)");
    }

    /**
     * Test {123=[CHROME, FF31, IE11], 150=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 25, 25",
        FF31 = "0, 25, 25",
        FF38 = "25",
        IE11 = "0, 25, 25")
    public void data__jQuery_data_document_() throws Exception {
        runTest("data: jQuery.data(document)");
    }

    /**
     * Test {124=[CHROME, FF31, IE11], 151=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void data__Expando_cleanup() throws Exception {
        runTest("data: Expando cleanup");
    }

    /**
     * Test {125=[CHROME, FF31, IE11], 152=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void data__Data_is_not_being_set_on_comment_and_text_nodes() throws Exception {
        runTest("data: Data is not being set on comment and text nodes");
    }

    /**
     * Test {126=[CHROME, FF31, IE11]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        IE11 = "0, 10, 10")
    public void data__jQuery_acceptData() throws Exception {
        runTest("data: jQuery.acceptData");
    }

    /**
     * Test {127=[CHROME, FF31, IE11], 154=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void data__jQuery___data_______undefined___14101_() throws Exception {
        runTest("data: jQuery().data() === undefined (#14101)");
    }

    /**
     * Test {128=[CHROME, FF31, IE11], 155=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void data___data__() throws Exception {
        runTest("data: .data()");
    }

    /**
     * Test {129=[CHROME, FF31, IE11], 156=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 18, 18",
        FF31 = "0, 18, 18",
        FF38 = "18",
        IE11 = "0, 18, 18")
    public void data__jQuery_Element__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(Element).data(String, Object).data(String)");
    }

    /**
     * Test {130=[CHROME, FF31, IE11], 157=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 16, 16",
        FF31 = "0, 16, 16",
        FF38 = "16",
        IE11 = "0, 16, 16")
    public void data__jQuery_plain_Object__data_String__Object__data_String_() throws Exception {
        runTest("data: jQuery(plain Object).data(String, Object).data(String)");
    }

    /**
     * Test {131=[CHROME, FF31, IE11], 158=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 43, 43",
        FF31 = "0, 43, 43",
        FF38 = "43",
        IE11 = "0, 43, 43")
    public void data__data___attributes() throws Exception {
        runTest("data: data-* attributes");
    }

    /**
     * Test {132=[CHROME, FF31, IE11], 159=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void data___data_Object_() throws Exception {
        runTest("data: .data(Object)");
    }

    /**
     * Test {133=[CHROME, FF31, IE11], 160=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void data__jQuery_removeData() throws Exception {
        runTest("data: jQuery.removeData");
    }

    /**
     * Test {134=[CHROME, FF31, IE11], 161=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void data___removeData__() throws Exception {
        runTest("data: .removeData()");
    }

    /**
     * Test {135=[CHROME, FF31, IE11], 162=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void data__JSON_serialization___8108_() throws Exception {
        runTest("data: JSON serialization (#8108)");
    }

    /**
     * Test {136=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "6")
    public void deferred__jQuery_when___resolved() throws Exception {
        runTest("deferred: jQuery.when - resolved");
    }

    /**
     * Test {136=[CHROME, FF31, IE11], 163=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void data__jQuery_data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: jQuery.data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {137=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "4")
    public void deprecated__bind_unbind() throws Exception {
        runTest("deprecated: bind/unbind");
    }

    /**
     * Test {137=[CHROME, FF31, IE11], 164=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void data__jQuery_data_should_not_miss_data_with_preset_hyphenated_property_names() throws Exception {
        runTest("data: jQuery.data should not miss data with preset hyphenated property names");
    }

    /**
     * Test {138=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void deprecated__delegate_undelegate() throws Exception {
        runTest("deprecated: delegate/undelegate");
    }

    /**
     * Test {138=[CHROME, FF31, IE11], 166=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 24, 24",
        FF31 = "0, 24, 24",
        FF38 = "24",
        IE11 = "0, 24, 24")
    public void data__jQuery_data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {139=[CHROME, FF31, IE11]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 27, 27",
        FF31 = "0, 27, 27",
        IE11 = "0, 27, 27")
    public void data__jQuery_data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: jQuery.data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {139=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void deprecated__size__() throws Exception {
        runTest("deprecated: size()");
    }

    /**
     * Test {140=[CHROME, FF31, IE11], 167=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void data___removeData_supports_removal_of_hyphenated_properties_via_array___12786_() throws Exception {
        runTest("data: .removeData supports removal of hyphenated properties via array (#12786)");
    }

    /**
     * Test {141=[CHROME, FF31, IE11], 168=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void data__Triggering_the_removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: Triggering the removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {142=[CHROME, FF31, IE11], 169=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void data__Only_check_element_attributes_once_when_calling__data______8909() throws Exception {
        runTest("data: Only check element attributes once when calling .data() - #8909");
    }

    /**
     * Test {143=[CHROME, FF31, IE11], 170=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void data__JSON_data__attributes_can_have_newlines() throws Exception {
        runTest("data: JSON data- attributes can have newlines");
    }

    /**
     * Test {144=[CHROME, FF31, IE11], 171=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void data__enumerate_data_attrs_on_body___14894_() throws Exception {
        runTest("data: enumerate data attrs on body (#14894)");
    }

    /**
     * Test {145=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "38")
    public void support__Verify_that_support_tests_resolve_as_expected_per_browser() throws Exception {
        runTest("support: Verify that support tests resolve as expected per browser");
    }

    /**
     * Test {145=[CHROME, FF31, IE11], 173=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 14, 14",
        FF31 = "0, 14, 14",
        FF38 = "14",
        IE11 = "0, 14, 14")
    public void queue__queue___with_other_types() throws Exception {
        runTest("queue: queue() with other types");
    }

    /**
     * Test {146=[CHROME, FF31, IE11], 174=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void queue__queue_name__passes_in_the_next_item_in_the_queue_as_a_parameter() throws Exception {
        runTest("queue: queue(name) passes in the next item in the queue as a parameter");
    }

    /**
     * Test {147=[CHROME, FF31, IE11], 175=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void queue__queue___passes_in_the_next_item_in_the_queue_as_a_parameter_to_fx_queues() throws Exception {
        runTest("queue: queue() passes in the next item in the queue as a parameter to fx queues");
    }

    /**
     * Test {148=[CHROME, FF31, IE11], 176=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void queue__callbacks_keep_their_place_in_the_queue() throws Exception {
        runTest("queue: callbacks keep their place in the queue");
    }

    /**
     * Test {149=[CHROME, FF31, IE11], 177=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void queue__delay__() throws Exception {
        runTest("queue: delay()");
    }

    /**
     * Test {150=[CHROME, FF31, IE11], 178=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void queue__clearQueue_name__clears_the_queue() throws Exception {
        runTest("queue: clearQueue(name) clears the queue");
    }

    /**
     * Test {151=[CHROME, FF31, IE11], 179=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void queue__clearQueue___clears_the_fx_queue() throws Exception {
        runTest("queue: clearQueue() clears the fx queue");
    }

    /**
     * Test {152=[CHROME, FF31, IE11], 180=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void queue__fn_promise_____called_when_fx_queue_is_empty() throws Exception {
        runTest("queue: fn.promise() - called when fx queue is empty");
    }

    /**
     * Test {153=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "10")
    public void data__acceptData() throws Exception {
        runTest("data: acceptData");
    }

    /**
     * Test {153=[CHROME, FF31, IE11], 181=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void queue__fn_promise___queue______called_whenever_last_queue_function_is_dequeued() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued");
    }

    /**
     * Test {154=[CHROME, FF31, IE11], 182=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void queue__fn_promise___queue______waits_for_animation_to_complete_before_resolving() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving");
    }

    /**
     * Test {155=[CHROME, FF31, IE11], 183=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void queue___promise_obj_() throws Exception {
        runTest("queue: .promise(obj)");
    }

    /**
     * Test {156=[CHROME, FF31, IE11], 184=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void queue__delay___can_be_stopped() throws Exception {
        runTest("queue: delay() can be stopped");
    }

    /**
     * Test {157=[CHROME, FF31, IE11], 185=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void queue__queue_stop_hooks() throws Exception {
        runTest("queue: queue stop hooks");
    }

    /**
     * Test {158=[CHROME, FF31, IE11], 186=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void attributes__jQuery_propFix_integrity_test() throws Exception {
        runTest("attributes: jQuery.propFix integrity test");
    }

    /**
     * Test {159=[CHROME, FF31, IE11], 187=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 50, 50",
        FF31 = "0, 50, 50",
        FF38 = "50",
        IE11 = "0, 50, 50")
    public void attributes__attr_String_() throws Exception {
        runTest("attributes: attr(String)");
    }

    /**
     * Test {160=[CHROME, FF31, IE11], 188=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void attributes__attr_String__on_cloned_elements___9646() throws Exception {
        runTest("attributes: attr(String) on cloned elements, #9646");
    }

    /**
     * Test {161=[CHROME, FF31, IE11], 189=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void attributes__attr_String__in_XML_Files() throws Exception {
        runTest("attributes: attr(String) in XML Files");
    }

    /**
     * Test {162=[CHROME, FF31, IE11], 190=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void attributes__attr_String__Function_() throws Exception {
        runTest("attributes: attr(String, Function)");
    }

    /**
     * Test {163=[CHROME, FF31, IE11], 191=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void attributes__attr_Hash_() throws Exception {
        runTest("attributes: attr(Hash)");
    }

    /**
     * Test {164=[CHROME, FF31, IE11], 192=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 71, 71",
        FF31 = "0, 71, 71",
        FF38 = "71",
        IE11 = "0, 71, 71")
    public void attributes__attr_String__Object_() throws Exception {
        runTest("attributes: attr(String, Object)");
    }

    /**
     * Test {165=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "24")
    public void data___data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: .data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {165=[CHROME, FF31, IE11], 193=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void attributes__attr___extending_the_boolean_attrHandle() throws Exception {
        runTest("attributes: attr - extending the boolean attrHandle");
    }

    /**
     * Test {166=[CHROME, FF31, IE11], 194=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_document() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML document");
    }

    /**
     * Test {167=[CHROME, FF31, IE11], 195=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_fragment() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML fragment");
    }

    /**
     * Test {168=[CHROME, FF31, IE11], 196=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void attributes__attr__tabindex__() throws Exception {
        runTest("attributes: attr('tabindex')");
    }

    /**
     * Test {169=[CHROME, FF31, IE11], 197=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void attributes__attr__tabindex___value_() throws Exception {
        runTest("attributes: attr('tabindex', value)");
    }

    /**
     * Test {170=[CHROME, FF31, IE11], 198=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    public void attributes__removeAttr_String_() throws Exception {
        runTest("attributes: removeAttr(String)");
    }

    /**
     * Test {171=[CHROME, FF31, IE11], 199=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void attributes__removeAttr_String__in_XML() throws Exception {
        runTest("attributes: removeAttr(String) in XML");
    }

    /**
     * Test {172=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void data___data_prop__does_not_create_expando() throws Exception {
        runTest("data: .data(prop) does not create expando");
    }

    /**
     * Test {172=[CHROME, FF31, IE11], 200=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void attributes__removeAttr_Multi_String__variable_space_width_() throws Exception {
        runTest("attributes: removeAttr(Multi String, variable space width)");
    }

    /**
     * Test {173=[CHROME, FF31, IE11], 201=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 17, 17",
        FF31 = "0, 17, 17",
        FF38 = "17",
        IE11 = "0, 17, 17")
    public void attributes__prop_String__Object_() throws Exception {
        runTest("attributes: prop(String, Object)");
    }

    /**
     * Test {174=[CHROME, FF31, IE11], 202=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 14, 14",
        FF31 = "0, 14, 14",
        FF38 = "14",
        IE11 = "0, 14, 14")
    public void attributes__prop_String__Object__on_null_undefined() throws Exception {
        runTest("attributes: prop(String, Object) on null/undefined");
    }

    /**
     * Test {175=[CHROME, FF31, IE11], 203=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 11, 11",
        FF31 = "0, 11, 11",
        FF38 = "11",
        IE11 = "0, 11, 11")
    public void attributes__prop__tabindex__() throws Exception {
        runTest("attributes: prop('tabindex')");
    }

    /**
     * Test {176=[CHROME, FF31, IE11], 205=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void attributes__prop__tabindex___value_() throws Exception {
        runTest("attributes: prop('tabindex', value)");
    }

    /**
     * Test {177=[CHROME, FF31, IE11], 206=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void attributes__removeProp_String_() throws Exception {
        runTest("attributes: removeProp(String)");
    }

    /**
     * Test {178=[CHROME, FF31, IE11], 207=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void attributes__val___after_modification() throws Exception {
        runTest("attributes: val() after modification");
    }

    /**
     * Test {179=[CHROME, FF31, IE11], 208=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 26, 26",
        FF31 = "0, 26, 26",
        FF38 = "26",
        IE11 = "0, 26, 26")
    public void attributes__val__() throws Exception {
        runTest("attributes: val()");
    }

    /**
     * Test {180=[CHROME, FF31, IE11], 209=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void attributes__val___with_non_matching_values_on_dropdown_list() throws Exception {
        runTest("attributes: val() with non-matching values on dropdown list");
    }

    /**
     * Test {181=[CHROME, FF31], 210=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4")
    public void attributes__val___respects_numbers_without_exception__Bug__9319_() throws Exception {
        runTest("attributes: val() respects numbers without exception (Bug #9319)");
    }

    /**
     * Test {181=[IE11], 182=[CHROME, FF31], 211=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void attributes__val_String_Number_() throws Exception {
        runTest("attributes: val(String/Number)");
    }

    /**
     * Test {182=[IE11], 183=[CHROME, FF31], 212=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void attributes__val_Function_() throws Exception {
        runTest("attributes: val(Function)");
    }

    /**
     * Test {183=[IE11], 184=[CHROME, FF31], 213=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void attributes__val_Array_of_Numbers___Bug__7123_() throws Exception {
        runTest("attributes: val(Array of Numbers) (Bug #7123)");
    }

    /**
     * Test {184=[IE11], 185=[CHROME, FF31], 214=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void attributes__val_Function__with_incoming_value() throws Exception {
        runTest("attributes: val(Function) with incoming value");
    }

    /**
     * Test {185=[IE11], 186=[CHROME, FF31], 215=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void attributes__val_select__after_form_reset____Bug__2551_() throws Exception {
        runTest("attributes: val(select) after form.reset() (Bug #2551)");
    }

    /**
     * Test {186=[IE11], 187=[CHROME, FF31], 216=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void attributes__addClass_String_() throws Exception {
        runTest("attributes: addClass(String)");
    }

    /**
     * Test {187=[IE11], 188=[CHROME, FF31], 217=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void attributes__addClass_Function_() throws Exception {
        runTest("attributes: addClass(Function)");
    }

    /**
     * Test {188=[IE11], 189=[CHROME, FF31], 218=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 52, 52",
        FF31 = "0, 52, 52",
        FF38 = "52",
        IE11 = "0, 52, 52")
    public void attributes__addClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: addClass(Function) with incoming value");
    }

    /**
     * Test {189=[IE11], 190=[CHROME, FF31], 219=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void attributes__removeClass_String____simple() throws Exception {
        runTest("attributes: removeClass(String) - simple");
    }

    /**
     * Test {190=[IE11], 191=[CHROME, FF31], 220=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void attributes__removeClass_Function____simple() throws Exception {
        runTest("attributes: removeClass(Function) - simple");
    }

    /**
     * Test {191=[IE11], 192=[CHROME, FF31], 221=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 52, 52",
        FF31 = "0, 52, 52",
        FF38 = "52",
        IE11 = "0, 52, 52")
    public void attributes__removeClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: removeClass(Function) with incoming value");
    }

    /**
     * Test {192=[IE11], 193=[CHROME, FF31], 222=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void attributes__removeClass___removes_duplicates() throws Exception {
        runTest("attributes: removeClass() removes duplicates");
    }

    /**
     * Test {193=[IE11], 194=[CHROME, FF31], 223=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void attributes__removeClass_undefined__is_a_no_op() throws Exception {
        runTest("attributes: removeClass(undefined) is a no-op");
    }

    /**
     * Test {194=[IE11], 195=[CHROME, FF31], 224=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 17, 17",
        FF31 = "0, 17, 17",
        FF38 = "19",
        IE11 = "0, 17, 17")
    public void attributes__toggleClass_String_boolean_undefined___boolean__() throws Exception {
        runTest("attributes: toggleClass(String|boolean|undefined[, boolean])");
    }

    /**
     * Test {195=[IE11], 196=[CHROME, FF31], 225=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 17, 17",
        FF31 = "0, 17, 17",
        FF38 = "19",
        IE11 = "0, 17, 17")
    public void attributes__toggleClass_Function___boolean__() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean])");
    }

    /**
     * Test {196=[IE11], 197=[CHROME, FF31], 226=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 14, 14",
        FF31 = "0, 14, 14",
        FF38 = "14",
        IE11 = "0, 14, 14")
    public void attributes__toggleClass_Function___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean]) with incoming value");
    }

    /**
     * Test {197=[IE11], 198=[CHROME, FF31], 227=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 17, 17",
        FF31 = "0, 17, 17",
        FF38 = "17",
        IE11 = "0, 17, 17")
    public void attributes__addClass__removeClass__hasClass() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass");
    }

    /**
     * Test {198=[IE11], 199=[CHROME, FF31], 228=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 19, 19",
        FF31 = "0, 19, 19",
        FF38 = "19",
        IE11 = "0, 19, 19")
    public void attributes__addClass__removeClass__hasClass_on_many_elements() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass on many elements");
    }

    /**
     * Test {199=[IE11], 200=[CHROME, FF31], 229=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void attributes__contents___hasClass___returns_correct_values() throws Exception {
        runTest("attributes: contents().hasClass() returns correct values");
    }

    /**
     * Test {200=[IE11], 201=[CHROME, FF31], 230=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void attributes__hasClass_correctly_interprets_non_space_separators___13835_() throws Exception {
        runTest("attributes: hasClass correctly interprets non-space separators (#13835)");
    }

    /**
     * Test {201=[IE11], 202=[CHROME, FF31], 231=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void attributes__coords_returns_correct_values_in_IE6_IE7__see__10828() throws Exception {
        runTest("attributes: coords returns correct values in IE6/IE7, see #10828");
    }

    /**
     * Test {202=[IE11], 203=[CHROME, FF31], 232=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void attributes__should_not_throw_at___option__val_____14686_() throws Exception {
        runTest("attributes: should not throw at $(option).val() (#14686)");
    }

    /**
     * Test {203=[IE11], 204=[CHROME, FF31], 234=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void attributes__Insignificant_white_space_returned_for___option__val_____14858_() throws Exception {
        runTest("attributes: Insignificant white space returned for $(option).val() (#14858)");
    }

    /**
     * Test {204=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void attributes__image_prop___tabIndex___() throws Exception {
        runTest("attributes: image.prop( 'tabIndex' )");
    }

    /**
     * Test {204=[IE11], 205=[CHROME, FF31], 236=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__null_or_undefined_handler() throws Exception {
        runTest("event: null or undefined handler");
    }

    /**
     * Test {205=[IE11], 206=[CHROME, FF31], 237=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__on___with_non_null_defined_data() throws Exception {
        runTest("event: on() with non-null,defined data");
    }

    /**
     * Test {206=[IE11], 207=[CHROME, FF31], 238=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__Handler_changes_and__trigger___order() throws Exception {
        runTest("event: Handler changes and .trigger() order");
    }

    /**
     * Test {207=[IE11], 208=[CHROME, FF31], 239=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void event__on____with_data() throws Exception {
        runTest("event: on(), with data");
    }

    /**
     * Test {208=[IE11], 209=[CHROME, FF31], 240=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void event__click____with_data() throws Exception {
        runTest("event: click(), with data");
    }

    /**
     * Test {209=[IE11], 210=[CHROME, FF31], 241=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void event__on____with_data__trigger_with_data() throws Exception {
        runTest("event: on(), with data, trigger with data");
    }

    /**
     * Test {210=[IE11], 211=[CHROME, FF31], 242=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__on____multiple_events_at_once() throws Exception {
        runTest("event: on(), multiple events at once");
    }

    /**
     * Test {211=[IE11], 212=[CHROME, FF31], 243=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__on____five_events_at_once() throws Exception {
        runTest("event: on(), five events at once");
    }

    /**
     * Test {212=[IE11], 213=[CHROME, FF31], 244=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void event__on____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: on(), multiple events at once and namespaces");
    }

    /**
     * Test {213=[IE11], 214=[CHROME, FF31], 245=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 27, 27",
        FF31 = "0, 27, 27",
        FF38 = "27",
        IE11 = "0, 27, 27")
    public void event__on____namespace_with_special_add() throws Exception {
        runTest("event: on(), namespace with special add");
    }

    /**
     * Test {214=[IE11], 215=[CHROME, FF31], 246=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__on____no_data() throws Exception {
        runTest("event: on(), no data");
    }

    /**
     * Test {215=[IE11], 216=[CHROME, FF31], 247=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void event__on_one_off_Object_() throws Exception {
        runTest("event: on/one/off(Object)");
    }

    /**
     * Test {216=[IE11], 217=[CHROME, FF31], 248=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void event__on_off_Object___on_off_Object__String_() throws Exception {
        runTest("event: on/off(Object), on/off(Object, String)");
    }

    /**
     * Test {217=[IE11], 218=[CHROME, FF31], 249=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__on_immediate_propagation() throws Exception {
        runTest("event: on immediate propagation");
    }

    /**
     * Test {218=[IE11], 219=[CHROME, FF31], 250=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void event__on_bubbling__isDefaultPrevented__stopImmediatePropagation() throws Exception {
        runTest("event: on bubbling, isDefaultPrevented, stopImmediatePropagation");
    }

    /**
     * Test {219=[IE11], 220=[CHROME, FF31], 251=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__on____iframes() throws Exception {
        runTest("event: on(), iframes");
    }

    /**
     * Test {220=[IE11], 221=[CHROME, FF31], 252=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void event__on____trigger_change_on_select() throws Exception {
        runTest("event: on(), trigger change on select");
    }

    /**
     * Test {221=[IE11], 222=[CHROME, FF31], 253=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 18, 18",
        FF31 = "0, 18, 18",
        FF38 = "18",
        IE11 = "0, 18, 18")
    public void event__on____namespaced_events__cloned_events() throws Exception {
        runTest("event: on(), namespaced events, cloned events");
    }

    /**
     * Test {222=[IE11], 223=[CHROME, FF31], 254=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void event__on____multi_namespaced_events() throws Exception {
        runTest("event: on(), multi-namespaced events");
    }

    /**
     * Test {223=[IE11], 224=[CHROME, FF31], 255=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__namespace_only_event_binding_is_a_no_op() throws Exception {
        runTest("event: namespace-only event binding is a no-op");
    }

    /**
     * Test {224=[IE11], 225=[CHROME, FF31], 257=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__on____with_same_function() throws Exception {
        runTest("event: on(), with same function");
    }

    /**
     * Test {225=[IE11], 226=[CHROME, FF31], 258=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__on____make_sure_order_is_maintained() throws Exception {
        runTest("event: on(), make sure order is maintained");
    }

    /**
     * Test {226=[IE11], 227=[CHROME, FF31], 259=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void event__on____with_different_this_object() throws Exception {
        runTest("event: on(), with different this object");
    }

    /**
     * Test {227=[IE11], 228=[CHROME, FF31], 260=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void event__on_name__false___off_name__false_() throws Exception {
        runTest("event: on(name, false), off(name, false)");
    }

    /**
     * Test {228=[IE11], 229=[CHROME, FF31], 261=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void event__on_name__selector__false___off_name__selector__false_() throws Exception {
        runTest("event: on(name, selector, false), off(name, selector, false)");
    }

    /**
     * Test {229=[IE11], 230=[CHROME, FF31], 262=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void event__on___trigger___off___on_plain_object() throws Exception {
        runTest("event: on()/trigger()/off() on plain object");
    }

    /**
     * Test {230=[IE11], 231=[CHROME, FF31], 263=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__off_type_() throws Exception {
        runTest("event: off(type)");
    }

    /**
     * Test {231=[IE11], 232=[CHROME, FF31], 264=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void event__off_eventObject_() throws Exception {
        runTest("event: off(eventObject)");
    }

    /**
     * Test {232=[IE11], 233=[CHROME, FF31], 265=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__hover___mouseenter_mouseleave() throws Exception {
        runTest("event: hover() mouseenter mouseleave");
    }

    /**
     * Test {233=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void attributes__option_value_not_trimmed_when_setting_via_parent_select() throws Exception {
        runTest("attributes: option value not trimmed when setting via parent select");
    }

    /**
     * Test {233=[IE11], 234=[CHROME, FF31], 266=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__mouseover_triggers_mouseenter() throws Exception {
        runTest("event: mouseover triggers mouseenter");
    }

    /**
     * Test {234=[IE11], 235=[CHROME, FF31], 267=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__pointerover_triggers_pointerenter() throws Exception {
        runTest("event: pointerover triggers pointerenter");
    }

    /**
     * Test {235=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "12")
    public void attributes__SVG_class_manipulation__gh_2199_() throws Exception {
        runTest("attributes: SVG class manipulation (gh-2199)");
    }

    /**
     * Test {235=[IE11], 236=[CHROME, FF31], 268=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__withinElement_implemented_with_jQuery_contains__() throws Exception {
        runTest("event: withinElement implemented with jQuery.contains()");
    }

    /**
     * Test {236=[IE11], 237=[CHROME, FF31], 269=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__mouseenter__mouseleave_don_t_catch_exceptions() throws Exception {
        runTest("event: mouseenter, mouseleave don't catch exceptions");
    }

    /**
     * Test {237=[IE11], 238=[CHROME, FF31], 270=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void event__trigger___shortcuts() throws Exception {
        runTest("event: trigger() shortcuts");
    }

    /**
     * Test {238=[IE11], 239=[CHROME, FF31], 271=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 18, 18",
        FF31 = "0, 18, 18",
        FF38 = "18",
        IE11 = "0, 18, 18")
    public void event__trigger___bubbling() throws Exception {
        runTest("event: trigger() bubbling");
    }

    /**
     * Test {239=[IE11], 240=[CHROME, FF31], 272=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 16, 16",
        FF31 = "0, 16, 16",
        FF38 = "16",
        IE11 = "0, 16, 16")
    public void event__trigger_type___data____fn__() throws Exception {
        runTest("event: trigger(type, [data], [fn])");
    }

    /**
     * Test {240=[IE11], 241=[CHROME, FF31], 273=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void event__submit_event_bubbles_on_copied_forms___11649_() throws Exception {
        runTest("event: submit event bubbles on copied forms (#11649)");
    }

    /**
     * Test {241=[IE11], 242=[CHROME, FF31], 274=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void event__change_event_bubbles_on_copied_forms___11796_() throws Exception {
        runTest("event: change event bubbles on copied forms (#11796)");
    }

    /**
     * Test {242=[IE11], 243=[CHROME, FF31], 275=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 28, 28",
        FF31 = "0, 28, 28",
        FF38 = "28",
        IE11 = "0, 28, 28")
    public void event__trigger_eventObject___data____fn__() throws Exception {
        runTest("event: trigger(eventObject, [data], [fn])");
    }

    /**
     * Test {243=[IE11], 244=[CHROME, FF31], 276=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event___trigger___bubbling_on_disconnected_elements___10489_() throws Exception {
        runTest("event: .trigger() bubbling on disconnected elements (#10489)");
    }

    /**
     * Test {244=[IE11], 245=[CHROME, FF31], 277=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event___trigger___doesn_t_bubble_load_event___10717_() throws Exception {
        runTest("event: .trigger() doesn't bubble load event (#10717)");
    }

    /**
     * Test {245=[IE11], 246=[CHROME, FF31], 278=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__Delegated_events_in_SVG___10791___13180_() throws Exception {
        runTest("event: Delegated events in SVG (#10791; #13180)");
    }

    /**
     * Test {246=[IE11], 247=[CHROME, FF31], 279=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void event__Delegated_events_in_forms___10844___11145___8165___11382___11764_() throws Exception {
        runTest("event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764)");
    }

    /**
     * Test {247=[IE11], 248=[CHROME, FF31], 280=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__Submit_event_can_be_stopped___11049_() throws Exception {
        runTest("event: Submit event can be stopped (#11049)");
    }

    /**
     * Test {248=[IE11], 249=[CHROME, FF31], 281=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void event__on_beforeunload_() throws Exception {
        runTest("event: on(beforeunload)");
    }

    /**
     * Test {249=[IE11], 250=[CHROME, FF31], 282=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "6",
        IE11 = "0, 5, 5")
    public void event__jQuery_Event__type__props__() throws Exception {
        runTest("event: jQuery.Event( type, props )");
    }

    /**
     * Test {250=[IE11], 251=[CHROME, FF31], 283=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    public void event__jQuery_Event_properties() throws Exception {
        runTest("event: jQuery.Event properties");
    }

    /**
     * Test {251=[IE11], 252=[CHROME, FF31], 284=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 65, 65",
        FF31 = "0, 65, 65",
        FF38 = "65",
        IE11 = "0, 65, 65")
    public void event___on____off__() throws Exception {
        runTest("event: .on()/.off()");
    }

    /**
     * Test {252=[IE11], 253=[CHROME, FF31], 285=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__jQuery_off_using_dispatched_jQuery_Event() throws Exception {
        runTest("event: jQuery.off using dispatched jQuery.Event");
    }

    /**
     * Test {253=[IE11], 254=[CHROME, FF31], 286=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void event__delegated_event_with_delegateTarget_relative_selector() throws Exception {
        runTest("event: delegated event with delegateTarget-relative selector");
    }

    /**
     * Test {254=[IE11], 255=[CHROME, FF31], 287=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__delegated_event_with_selector_matching_Object_prototype_property___13203_() throws Exception {
        runTest("event: delegated event with selector matching Object.prototype property (#13203)");
    }

    /**
     * Test {255=[IE11], 256=[CHROME, FF31], 288=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__delegated_event_with_intermediate_DOM_manipulation___13208_() throws Exception {
        runTest("event: delegated event with intermediate DOM manipulation (#13208)");
    }

    /**
     * Test {256=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void event__Empty_namespace_is_ignored() throws Exception {
        runTest("event: Empty namespace is ignored");
    }

    /**
     * Test {256=[IE11], 257=[CHROME, FF31], 289=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__stopPropagation___stops_directly_bound_events_on_delegated_target() throws Exception {
        runTest("event: stopPropagation() stops directly-bound events on delegated target");
    }

    /**
     * Test {257=[IE11], 258=[CHROME, FF31], 290=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__off_all_bound_delegated_events() throws Exception {
        runTest("event: off all bound delegated events");
    }

    /**
     * Test {258=[IE11], 259=[CHROME, FF31], 291=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__on_with_multiple_delegated_events() throws Exception {
        runTest("event: on with multiple delegated events");
    }

    /**
     * Test {259=[IE11], 260=[CHROME, FF31], 292=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void event__delegated_on_with_change() throws Exception {
        runTest("event: delegated on with change");
    }

    /**
     * Test {260=[IE11], 261=[CHROME, FF31], 293=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__delegated_on_with_submit() throws Exception {
        runTest("event: delegated on with submit");
    }

    /**
     * Test {261=[IE11], 262=[CHROME, FF31], 294=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__delegated_off___with_only_namespaces() throws Exception {
        runTest("event: delegated off() with only namespaces");
    }

    /**
     * Test {262=[IE11], 263=[CHROME, FF31], 295=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__Non_DOM_element_events() throws Exception {
        runTest("event: Non DOM element events");
    }

    /**
     * Test {263=[IE11], 264=[CHROME, FF31], 296=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__inline_handler_returning_false_stops_default() throws Exception {
        runTest("event: inline handler returning false stops default");
    }

    /**
     * Test {264=[IE11], 265=[CHROME, FF31], 297=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__window_resize() throws Exception {
        runTest("event: window resize");
    }

    /**
     * Test {265=[IE11], 266=[CHROME, FF31], 298=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__focusin_bubbles() throws Exception {
        runTest("event: focusin bubbles");
    }

    /**
     * Test {266=[IE11], 267=[CHROME, FF31], 299=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__custom_events_with_colons___3533___8272_() throws Exception {
        runTest("event: custom events with colons (#3533, #8272)");
    }

    /**
     * Test {267=[IE11], 268=[CHROME, FF31], 300=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void event___on_and__off() throws Exception {
        runTest("event: .on and .off");
    }

    /**
     * Test {268=[IE11], 269=[CHROME, FF31], 301=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void event__special_on_name_mapping() throws Exception {
        runTest("event: special on name mapping");
    }

    /**
     * Test {269=[IE11], 270=[CHROME, FF31], 302=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void event___on_and__off__selective_mixed_removal___10705_() throws Exception {
        runTest("event: .on and .off, selective mixed removal (#10705)");
    }

    /**
     * Test {270=[IE11], 271=[CHROME, FF31], 303=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event___on__event_map__null_selector__data____11130() throws Exception {
        runTest("event: .on( event-map, null-selector, data ) #11130");
    }

    /**
     * Test {271=[IE11], 272=[CHROME, FF31], 304=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void event__clone___delegated_events___11076_() throws Exception {
        runTest("event: clone() delegated events (#11076)");
    }

    /**
     * Test {272=[IE11], 273=[CHROME, FF31], 305=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void event__checkbox_state___3827_() throws Exception {
        runTest("event: checkbox state (#3827)");
    }

    /**
     * Test {273=[IE11], 274=[CHROME, FF31], 306=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__hover_event_no_longer_special_since_1_9() throws Exception {
        runTest("event: hover event no longer special since 1.9");
    }

    /**
     * Test {274=[IE11], 275=[CHROME, FF31], 307=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__fixHooks_extensions() throws Exception {
        runTest("event: fixHooks extensions");
    }

    /**
     * Test {275=[IE11], 276=[CHROME, FF31], 309=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__focusin_using_non_element_targets() throws Exception {
        runTest("event: focusin using non-element targets");
    }

    /**
     * Test {276=[IE11], 277=[CHROME, FF31], 310=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__focusin_from_an_iframe() throws Exception {
        runTest("event: focusin from an iframe");
    }

    /**
     * Test {277=[IE11], 278=[CHROME, FF31], 311=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__jQuery_ready_promise() throws Exception {
        runTest("event: jQuery.ready promise");
    }

    /**
     * Test {278=[IE11], 279=[CHROME, FF31], 312=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__Focusing_iframe_element() throws Exception {
        runTest("event: Focusing iframe element");
    }

    /**
     * Test {279=[IE11], 280=[CHROME, FF31], 313=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__triggerHandler_onbeforeunload_() throws Exception {
        runTest("event: triggerHandler(onbeforeunload)");
    }

    /**
     * Test {280=[IE11], 281=[CHROME, FF31], 314=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__jQuery_ready_synchronous_load_with_long_loading_subresources() throws Exception {
        runTest("event: jQuery.ready synchronous load with long loading subresources");
    }

    /**
     * Test {281=[IE11], 282=[CHROME, FF31], 315=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void event__change_handler_should_be_detached_from_element() throws Exception {
        runTest("event: change handler should be detached from element");
    }

    /**
     * Test {282=[IE11], 283=[CHROME, FF31], 316=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__trigger_click_on_checkbox__fires_change_event() throws Exception {
        runTest("event: trigger click on checkbox, fires change event");
    }

    /**
     * Test {283=[IE11], 284=[CHROME, FF31], 317=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void event__Namespace_preserved_when_passed_an_Event___12739_() throws Exception {
        runTest("event: Namespace preserved when passed an Event (#12739)");
    }

    /**
     * Test {284=[IE11], 285=[CHROME, FF31], 318=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 18, 18",
        FF31 = "0, 18, 18",
        FF38 = "18",
        IE11 = "0, 18, 18")
    public void event__make_sure_events_cloned_correctly() throws Exception {
        runTest("event: make sure events cloned correctly");
    }

    /**
     * Test {285=[IE11], 286=[CHROME, FF31], 319=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__String_prototype_namespace_does_not_cause_trigger___to_throw___13360_() throws Exception {
        runTest("event: String.prototype.namespace does not cause trigger() to throw (#13360)");
    }

    /**
     * Test {286=[IE11], 287=[CHROME, FF31], 320=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void event__Inline_event_result_is_returned___13993_() throws Exception {
        runTest("event: Inline event result is returned (#13993)");
    }

    /**
     * Test {287=[IE11], 288=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        IE11 = "0, 2, 2")
    public void event__Check_order_of_focusin_focusout_events() throws Exception {
        runTest("event: Check order of focusin/focusout events");
    }

    /**
     * Test {288=[IE11], 289=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        IE11 = "0, 5, 5")
    public void event__focus_blur_order___12868_() throws Exception {
        runTest("event: focus-blur order (#12868)");
    }

    /**
     * Test {288=[FF31], 289=[IE11], 290=[CHROME], 326=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void selector__element___jQuery_only() throws Exception {
        runTest("selector: element - jQuery only");
    }

    /**
     * Test {289=[FF31], 290=[IE11], 291=[CHROME], 327=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 26, 26",
        FF31 = "0, 26, 26",
        FF38 = "26",
        IE11 = "0, 26, 26")
    public void selector__id() throws Exception {
        runTest("selector: id");
    }

    /**
     * Test {290=[FF31], 291=[IE11], 292=[CHROME], 328=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void selector__class___jQuery_only() throws Exception {
        runTest("selector: class - jQuery only");
    }

    /**
     * Test {291=[FF31], 292=[IE11], 293=[CHROME], 329=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void selector__name() throws Exception {
        runTest("selector: name");
    }

    /**
     * Test {292=[FF31], 293=[IE11], 294=[CHROME], 330=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void selector__selectors_with_comma() throws Exception {
        runTest("selector: selectors with comma");
    }

    /**
     * Test {293=[FF31], 294=[IE11], 295=[CHROME], 331=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 27, 27",
        FF31 = "0, 27, 27",
        FF38 = "27",
        IE11 = "0, 27, 27")
    public void selector__child_and_adjacent() throws Exception {
        runTest("selector: child and adjacent");
    }

    /**
     * Test {294=[FF31], 295=[IE11], 296=[CHROME], 332=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 54, 54",
        FF31 = "0, 54, 54",
        FF38 = "54",
        IE11 = "0, 54, 54")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void selector__attributes() throws Exception {
        runTest("selector: attributes");
    }

    /**
     * Test {295=[FF31], 296=[IE11], 297=[CHROME], 333=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void selector__disconnected_nodes() throws Exception {
        runTest("selector: disconnected nodes");
    }

    /**
     * Test {296=[FF31], 297=[IE11], 298=[CHROME], 334=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void selector__disconnected_nodes___jQuery_only() throws Exception {
        runTest("selector: disconnected nodes - jQuery only");
    }

    /**
     * Test {297=[FF31], 298=[IE11], 299=[CHROME], 335=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 35, 35",
        FF31 = "0, 35, 35",
        FF38 = "35",
        IE11 = "0, 35, 35")
    public void selector__attributes___jQuery_attr() throws Exception {
        runTest("selector: attributes - jQuery.attr");
    }

    /**
     * Test {298=[FF31], 299=[IE11], 300=[CHROME], 336=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 16, 16",
        FF31 = "0, 16, 16",
        FF38 = "16",
        IE11 = "0, 16, 16")
    public void selector__jQuery_contains() throws Exception {
        runTest("selector: jQuery.contains");
    }

    /**
     * Test {299=[FF31], 300=[IE11], 301=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 14, 14",
        FF31 = "0, 14, 14",
        IE11 = "0, 14, 14")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void selector__jQuery_unique() throws Exception {
        runTest("selector: jQuery.unique");
    }

    /**
     * Test {300=[FF31], 301=[IE11], 302=[CHROME], 338=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void selector__Sizzle_cache_collides_with_multiple_Sizzles_on_a_page() throws Exception {
        runTest("selector: Sizzle cache collides with multiple Sizzles on a page");
    }

    /**
     * Test {301=[FF31], 302=[IE11], 303=[CHROME], 339=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void selector__Iframe_dispatch_should_not_affect_jQuery___13936_() throws Exception {
        runTest("selector: Iframe dispatch should not affect jQuery (#13936)");
    }

    /**
     * Test {302=[FF31], 303=[IE11], 304=[CHROME], 340=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__find_String_() throws Exception {
        runTest("traversing: find(String)");
    }

    /**
     * Test {303=[FF31], 304=[IE11], 305=[CHROME], 341=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void traversing__find_String__under_non_elements() throws Exception {
        runTest("traversing: find(String) under non-elements");
    }

    /**
     * Test {304=[FF31], 305=[IE11], 306=[CHROME], 342=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void traversing__find_leading_combinator_() throws Exception {
        runTest("traversing: find(leading combinator)");
    }

    /**
     * Test {305=[FF31], 306=[IE11], 307=[CHROME], 343=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 13, 13",
        FF31 = "0, 13, 13",
        FF38 = "13",
        IE11 = "0, 13, 13")
    public void traversing__find_node_jQuery_object_() throws Exception {
        runTest("traversing: find(node|jQuery object)");
    }

    /**
     * Test {306=[FF31], 307=[IE11], 308=[CHROME], 344=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 23, 23",
        FF31 = "0, 23, 23",
        FF38 = "23",
        IE11 = "0, 23, 23")
    public void traversing__is_String_undefined_() throws Exception {
        runTest("traversing: is(String|undefined)");
    }

    /**
     * Test {307=[FF31], 308=[IE11], 309=[CHROME], 345=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 14, 14",
        FF31 = "0, 14, 14",
        FF38 = "14",
        IE11 = "0, 14, 14")
    public void traversing__is___against_non_elements___10178_() throws Exception {
        runTest("traversing: is() against non-elements (#10178)");
    }

    /**
     * Test {308=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "4")
    public void event__drag_drop_events_copy_mouse_related_event_properties__gh_1925__gh_2009_() throws Exception {
        runTest("event: drag/drop events copy mouse-related event properties (gh-1925, gh-2009)");
    }

    /**
     * Test {308=[FF31], 309=[IE11], 310=[CHROME], 346=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 19, 19",
        FF31 = "0, 19, 19",
        FF38 = "19",
        IE11 = "0, 19, 19")
    public void traversing__is_jQuery_() throws Exception {
        runTest("traversing: is(jQuery)");
    }

    /**
     * Test {309=[FF31], 310=[IE11], 311=[CHROME], 347=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void traversing__is___with__has___selectors() throws Exception {
        runTest("traversing: is() with :has() selectors");
    }

    /**
     * Test {310=[FF31], 311=[IE11], 312=[CHROME], 348=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 27, 27",
        FF31 = "0, 27, 27",
        FF38 = "27",
        IE11 = "0, 27, 27")
    public void traversing__is___with_positional_selectors() throws Exception {
        runTest("traversing: is() with positional selectors");
    }

    /**
     * Test {311=[FF31], 312=[IE11], 313=[CHROME], 349=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void traversing__index__() throws Exception {
        runTest("traversing: index()");
    }

    /**
     * Test {312=[FF31], 313=[IE11], 314=[CHROME], 350=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 16, 16",
        FF31 = "0, 16, 16",
        FF38 = "16",
        IE11 = "0, 16, 16")
    public void traversing__index_Object_String_undefined_() throws Exception {
        runTest("traversing: index(Object|String|undefined)");
    }

    /**
     * Test {313=[FF31], 314=[IE11], 315=[CHROME], 351=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void traversing__filter_Selector_undefined_() throws Exception {
        runTest("traversing: filter(Selector|undefined)");
    }

    /**
     * Test {314=[FF31], 315=[IE11], 316=[CHROME], 352=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void traversing__filter_Function_() throws Exception {
        runTest("traversing: filter(Function)");
    }

    /**
     * Test {315=[FF31], 316=[IE11], 317=[CHROME], 353=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__filter_Element_() throws Exception {
        runTest("traversing: filter(Element)");
    }

    /**
     * Test {316=[FF31], 317=[IE11], 318=[CHROME], 354=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__filter_Array_() throws Exception {
        runTest("traversing: filter(Array)");
    }

    /**
     * Test {317=[FF31], 318=[IE11], 319=[CHROME], 355=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__filter_jQuery_() throws Exception {
        runTest("traversing: filter(jQuery)");
    }

    /**
     * Test {318=[FF31], 319=[IE11], 320=[CHROME], 356=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 19, 19",
        FF31 = "0, 19, 19",
        FF38 = "19",
        IE11 = "0, 19, 19")
    public void traversing__filter___with_positional_selectors() throws Exception {
        runTest("traversing: filter() with positional selectors");
    }

    /**
     * Test {319=[FF31], 320=[IE11], 321=[CHROME], 357=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 13, 13",
        FF31 = "0, 13, 13",
        FF38 = "13",
        IE11 = "0, 13, 13")
    public void traversing__closest__() throws Exception {
        runTest("traversing: closest()");
    }

    /**
     * Test {320=[FF31], 321=[IE11], 322=[CHROME], 358=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "1, 1, 2",
        IE11 = "0, 2, 2")
    public void traversing__closest___with_positional_selectors() throws Exception {
        runTest("traversing: closest() with positional selectors");
    }

    /**
     * Test {321=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void event___off___removes_the_expando_when_there_s_no_more_data() throws Exception {
        runTest("event: .off() removes the expando when there's no more data");
    }

    /**
     * Test {321=[FF31], 322=[IE11], 323=[CHROME], 359=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void traversing__closest_jQuery_() throws Exception {
        runTest("traversing: closest(jQuery)");
    }

    /**
     * Test {322=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void event__preventDefault___on_focusin_does_not_throw_exception() throws Exception {
        runTest("event: preventDefault() on focusin does not throw exception");
    }

    /**
     * Test {322=[FF31], 323=[IE11], 324=[CHROME], 360=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 11, 11",
        FF31 = "0, 11, 11",
        FF38 = "11",
        IE11 = "0, 11, 11")
    public void traversing__not_Selector_undefined_() throws Exception {
        runTest("traversing: not(Selector|undefined)");
    }

    /**
     * Test {323=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "10")
    public void event__Donor_event_interference() throws Exception {
        runTest("event: Donor event interference");
    }

    /**
     * Test {323=[FF31], 324=[IE11], 325=[CHROME], 361=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__not_Element_() throws Exception {
        runTest("traversing: not(Element)");
    }

    /**
     * Test {324=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void event__originalEvent_property_for_IE8() throws Exception {
        runTest("event: originalEvent property for IE8");
    }

    /**
     * Test {324=[FF31], 325=[IE11], 326=[CHROME], 362=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__not_Function_() throws Exception {
        runTest("traversing: not(Function)");
    }

    /**
     * Test {325=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "4")
    public void event__originalEvent_property_for_Chrome__Safari__Fx___Edge_of_simulated_event() throws Exception {
        runTest("event: originalEvent property for Chrome, Safari, Fx & Edge of simulated event");
    }

    /**
     * Test {325=[FF31], 326=[IE11], 327=[CHROME], 363=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void traversing__not_Array_() throws Exception {
        runTest("traversing: not(Array)");
    }

    /**
     * Test {326=[FF31], 327=[IE11], 328=[CHROME], 364=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__not_jQuery_() throws Exception {
        runTest("traversing: not(jQuery)");
    }

    /**
     * Test {327=[FF31], 328=[IE11], 329=[CHROME], 365=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void traversing__has_Element_() throws Exception {
        runTest("traversing: has(Element)");
    }

    /**
     * Test {328=[FF31], 329=[IE11], 330=[CHROME], 366=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void traversing__has_Selector_() throws Exception {
        runTest("traversing: has(Selector)");
    }

    /**
     * Test {329=[FF31], 330=[IE11], 331=[CHROME], 367=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void traversing__has_Arrayish_() throws Exception {
        runTest("traversing: has(Arrayish)");
    }

    /**
     * Test {330=[FF31], 331=[IE11], 332=[CHROME], 368=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void traversing__addBack__() throws Exception {
        runTest("traversing: addBack()");
    }

    /**
     * Test {331=[FF31], 332=[IE11], 333=[CHROME], 369=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void traversing__siblings__String__() throws Exception {
        runTest("traversing: siblings([String])");
    }

    /**
     * Test {332=[FF31], 333=[IE11], 334=[CHROME], 370=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void traversing__siblings__String_____jQuery_only() throws Exception {
        runTest("traversing: siblings([String]) - jQuery only");
    }

    /**
     * Test {333=[FF31], 334=[IE11], 335=[CHROME], 371=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void traversing__children__String__() throws Exception {
        runTest("traversing: children([String])");
    }

    /**
     * Test {334=[FF31], 335=[IE11], 336=[CHROME], 372=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__children__String_____jQuery_only() throws Exception {
        runTest("traversing: children([String]) - jQuery only");
    }

    /**
     * Test {335=[FF31], 336=[IE11], 337=[CHROME], 373=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void traversing__parent__String__() throws Exception {
        runTest("traversing: parent([String])");
    }

    /**
     * Test {336=[FF31], 337=[IE11], 338=[CHROME], 374=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void traversing__parents__String__() throws Exception {
        runTest("traversing: parents([String])");
    }

    /**
     * Test {337=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "15")
    public void selector__jQuery_uniqueSort() throws Exception {
        runTest("selector: jQuery.uniqueSort");
    }

    /**
     * Test {337=[FF31], 338=[IE11], 339=[CHROME], 375=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void traversing__parentsUntil__String__() throws Exception {
        runTest("traversing: parentsUntil([String])");
    }

    /**
     * Test {338=[FF31], 339=[IE11], 340=[CHROME], 376=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void traversing__next__String__() throws Exception {
        runTest("traversing: next([String])");
    }

    /**
     * Test {339=[FF31], 340=[IE11], 341=[CHROME], 377=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void traversing__prev__String__() throws Exception {
        runTest("traversing: prev([String])");
    }

    /**
     * Test {340=[FF31], 341=[IE11], 342=[CHROME], 378=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void traversing__nextAll__String__() throws Exception {
        runTest("traversing: nextAll([String])");
    }

    /**
     * Test {341=[FF31], 342=[IE11], 343=[CHROME], 379=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void traversing__prevAll__String__() throws Exception {
        runTest("traversing: prevAll([String])");
    }

    /**
     * Test {342=[FF31], 343=[IE11], 344=[CHROME], 380=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    public void traversing__nextUntil__String__() throws Exception {
        runTest("traversing: nextUntil([String])");
    }

    /**
     * Test {343=[FF31], 344=[IE11], 345=[CHROME], 381=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 11, 11",
        FF31 = "0, 11, 11",
        FF38 = "11",
        IE11 = "0, 11, 11")
    public void traversing__prevUntil__String__() throws Exception {
        runTest("traversing: prevUntil([String])");
    }

    /**
     * Test {344=[FF31], 345=[IE11], 346=[CHROME], 382=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    public void traversing__contents__() throws Exception {
        runTest("traversing: contents()");
    }

    /**
     * Test {345=[FF31], 346=[IE11], 347=[CHROME], 383=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    public void traversing__sort_direction() throws Exception {
        runTest("traversing: sort direction");
    }

    /**
     * Test {346=[FF31], 347=[IE11], 348=[CHROME], 384=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void traversing__add_String_selector_() throws Exception {
        runTest("traversing: add(String selector)");
    }

    /**
     * Test {347=[FF31], 348=[IE11], 349=[CHROME], 385=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__add_String_selector__String_context_() throws Exception {
        runTest("traversing: add(String selector, String context)");
    }

    /**
     * Test {348=[FF31], 349=[IE11], 350=[CHROME], 386=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void traversing__add_String_html_() throws Exception {
        runTest("traversing: add(String html)");
    }

    /**
     * Test {349=[FF31], 350=[IE11], 351=[CHROME], 387=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void traversing__add_jQuery_() throws Exception {
        runTest("traversing: add(jQuery)");
    }

    /**
     * Test {350=[FF31], 351=[IE11], 352=[CHROME], 388=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void traversing__add_Element_() throws Exception {
        runTest("traversing: add(Element)");
    }

    /**
     * Test {351=[FF31], 352=[IE11], 353=[CHROME], 389=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__add_Array_elements_() throws Exception {
        runTest("traversing: add(Array elements)");
    }

    /**
     * Test {352=[FF31], 353=[IE11], 354=[CHROME], 390=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void traversing__add_Window_() throws Exception {
        runTest("traversing: add(Window)");
    }

    /**
     * Test {353=[FF31], 354=[IE11], 355=[CHROME], 391=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void traversing__add_NodeList_undefined_HTMLFormElement_HTMLSelectElement_() throws Exception {
        runTest("traversing: add(NodeList|undefined|HTMLFormElement|HTMLSelectElement)");
    }

    /**
     * Test {354=[FF31], 355=[IE11], 356=[CHROME], 392=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void traversing__add_String__Context_() throws Exception {
        runTest("traversing: add(String, Context)");
    }

    /**
     * Test {355=[FF31], 356=[IE11], 357=[CHROME], 393=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void traversing__eq___1____10616() throws Exception {
        runTest("traversing: eq('-1') #10616");
    }

    /**
     * Test {356=[FF31], 357=[IE11], 358=[CHROME], 394=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void traversing__index_no_arg___10977() throws Exception {
        runTest("traversing: index(no arg) #10977");
    }

    /**
     * Test {357=[FF31], 358=[IE11], 359=[CHROME], 395=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void traversing__traversing_non_elements_with_attribute_filters___12523_() throws Exception {
        runTest("traversing: traversing non-elements with attribute filters (#12523)");
    }

    /**
     * Test {358=[FF31], 359=[IE11], 360=[CHROME], 396=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void manipulation__text__() throws Exception {
        runTest("manipulation: text()");
    }

    /**
     * Test {359=[FF31], 360=[IE11], 361=[CHROME], 397=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__text_undefined_() throws Exception {
        runTest("manipulation: text(undefined)");
    }

    /**
     * Test {360=[FF31], 361=[IE11], 362=[CHROME], 398=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void manipulation__text_String_() throws Exception {
        runTest("manipulation: text(String)");
    }

    /**
     * Test {361=[FF31], 362=[IE11], 363=[CHROME], 399=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void manipulation__text_Function_() throws Exception {
        runTest("manipulation: text(Function)");
    }

    /**
     * Test {362=[FF31], 363=[IE11], 364=[CHROME], 400=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__text_Function__with_incoming_value() throws Exception {
        runTest("manipulation: text(Function) with incoming value");
    }

    /**
     * Test {363=[FF31], 364=[IE11], 365=[CHROME], 401=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 78, 78",
        FF31 = "0, 78, 78",
        FF38 = "78",
        IE11 = "0, 78, 78")
    @NotYetImplemented(CHROME)
    public void manipulation__append_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {364=[FF31], 365=[IE11], 366=[CHROME], 402=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 78, 78",
        FF31 = "0, 78, 78",
        FF38 = "78",
        IE11 = "0, 78, 78")
    @NotYetImplemented(CHROME)
    public void manipulation__append_Function_() throws Exception {
        runTest("manipulation: append(Function)");
    }

    /**
     * Test {365=[FF31], 366=[IE11], 367=[CHROME], 403=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void manipulation__append_param__to_object__see__11280() throws Exception {
        runTest("manipulation: append(param) to object, see #11280");
    }

    /**
     * Test {366=[FF31], 367=[IE11], 368=[CHROME], 404=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void manipulation__append_Function__returns_String() throws Exception {
        runTest("manipulation: append(Function) returns String");
    }

    /**
     * Test {367=[FF31], 368=[IE11], 369=[CHROME], 405=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__append_Function__returns_Element() throws Exception {
        runTest("manipulation: append(Function) returns Element");
    }

    /**
     * Test {368=[FF31], 369=[IE11], 370=[CHROME], 406=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__append_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: append(Function) returns Array<Element>");
    }

    /**
     * Test {369=[FF31], 370=[IE11], 371=[CHROME], 407=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__append_Function__returns_jQuery() throws Exception {
        runTest("manipulation: append(Function) returns jQuery");
    }

    /**
     * Test {370=[FF31], 371=[IE11], 372=[CHROME], 408=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__append_Function__returns_Number() throws Exception {
        runTest("manipulation: append(Function) returns Number");
    }

    /**
     * Test {371=[FF31], 372=[IE11], 373=[CHROME], 409=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void manipulation__XML_DOM_manipulation___9960_() throws Exception {
        runTest("manipulation: XML DOM manipulation (#9960)");
    }

    /**
     * Test {372=[FF31], 373=[IE11], 374=[CHROME], 410=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__append_the_same_fragment_with_events__Bug__6997__5566_() throws Exception {
        runTest("manipulation: append the same fragment with events (Bug #6997, 5566)");
    }

    /**
     * Test {373=[FF31], 374=[IE11], 375=[CHROME], 411=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__append_HTML5_sectioning_elements__Bug__6485_() throws Exception {
        runTest("manipulation: append HTML5 sectioning elements (Bug #6485)");
    }

    /**
     * Test {374=[FF31], 375=[IE11], 376=[CHROME], 412=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__HTML5_Elements_inherit_styles_from_style_rules__Bug__10501_() throws Exception {
        runTest("manipulation: HTML5 Elements inherit styles from style rules (Bug #10501)");
    }

    /**
     * Test {375=[FF31], 376=[IE11], 377=[CHROME], 413=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__html_String__with_HTML5__Bug__6485_() throws Exception {
        runTest("manipulation: html(String) with HTML5 (Bug #6485)");
    }

    /**
     * Test {376=[FF31], 377=[IE11], 378=[CHROME], 415=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__IE8_serialization_bug() throws Exception {
        runTest("manipulation: IE8 serialization bug");
    }

    /**
     * Test {377=[FF31], 378=[IE11], 379=[CHROME], 416=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__html___object_element__10324() throws Exception {
        runTest("manipulation: html() object element #10324");
    }

    /**
     * Test {378=[FF31], 379=[IE11], 380=[CHROME], 417=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__append_xml_() throws Exception {
        runTest("manipulation: append(xml)");
    }

    /**
     * Test {379=[FF31], 380=[IE11], 381=[CHROME], 418=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void manipulation__appendTo_String_() throws Exception {
        runTest("manipulation: appendTo(String)");
    }

    /**
     * Test {380=[FF31], 381=[IE11], 382=[CHROME], 419=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__appendTo_Element_Array_Element__() throws Exception {
        runTest("manipulation: appendTo(Element|Array<Element>)");
    }

    /**
     * Test {381=[FF31], 382=[IE11], 383=[CHROME], 420=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void manipulation__appendTo_jQuery_() throws Exception {
        runTest("manipulation: appendTo(jQuery)");
    }

    /**
     * Test {382=[FF31], 383=[IE11], 384=[CHROME], 421=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__prepend_String_() throws Exception {
        runTest("manipulation: prepend(String)");
    }

    /**
     * Test {383=[FF31], 384=[IE11], 385=[CHROME], 422=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__prepend_Element_() throws Exception {
        runTest("manipulation: prepend(Element)");
    }

    /**
     * Test {384=[FF31], 385=[IE11], 386=[CHROME], 423=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__prepend_Array_Element__() throws Exception {
        runTest("manipulation: prepend(Array<Element>)");
    }

    /**
     * Test {385=[FF31], 386=[IE11], 387=[CHROME], 424=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__prepend_jQuery_() throws Exception {
        runTest("manipulation: prepend(jQuery)");
    }

    /**
     * Test {386=[FF31], 387=[IE11], 388=[CHROME], 425=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__prepend_Array_jQuery__() throws Exception {
        runTest("manipulation: prepend(Array<jQuery>)");
    }

    /**
     * Test {387=[FF31], 388=[IE11], 389=[CHROME], 426=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void manipulation__prepend_Function__with_incoming_value____String() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- String");
    }

    /**
     * Test {388=[FF31], 389=[IE11], 390=[CHROME], 427=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__prepend_Function__with_incoming_value____Element() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Element");
    }

    /**
     * Test {389=[FF31], 390=[IE11], 391=[CHROME], 428=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__prepend_Function__with_incoming_value____Array_Element_() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- Array<Element>");
    }

    /**
     * Test {390=[FF31], 391=[IE11], 392=[CHROME], 429=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__prepend_Function__with_incoming_value____jQuery() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value -- jQuery");
    }

    /**
     * Test {391=[FF31], 392=[IE11], 393=[CHROME], 430=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__prependTo_String_() throws Exception {
        runTest("manipulation: prependTo(String)");
    }

    /**
     * Test {392=[FF31], 393=[IE11], 394=[CHROME], 431=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__prependTo_Element_() throws Exception {
        runTest("manipulation: prependTo(Element)");
    }

    /**
     * Test {393=[FF31], 394=[IE11], 395=[CHROME], 432=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__prependTo_Array_Element__() throws Exception {
        runTest("manipulation: prependTo(Array<Element>)");
    }

    /**
     * Test {394=[FF31], 395=[IE11], 396=[CHROME], 433=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__prependTo_jQuery_() throws Exception {
        runTest("manipulation: prependTo(jQuery)");
    }

    /**
     * Test {395=[FF31], 396=[IE11], 397=[CHROME], 434=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__prependTo_Array_jQuery__() throws Exception {
        runTest("manipulation: prependTo(Array<jQuery>)");
    }

    /**
     * Test {396=[FF31], 397=[IE11], 398=[CHROME], 435=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_String_() throws Exception {
        runTest("manipulation: before(String)");
    }

    /**
     * Test {397=[FF31], 398=[IE11], 399=[CHROME], 436=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_Element_() throws Exception {
        runTest("manipulation: before(Element)");
    }

    /**
     * Test {398=[FF31], 399=[IE11], 400=[CHROME], 437=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_Array_Element__() throws Exception {
        runTest("manipulation: before(Array<Element>)");
    }

    /**
     * Test {399=[FF31], 400=[IE11], 401=[CHROME], 438=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_jQuery_() throws Exception {
        runTest("manipulation: before(jQuery)");
    }

    /**
     * Test {400=[FF31], 401=[IE11], 402=[CHROME], 439=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_Array_jQuery__() throws Exception {
        runTest("manipulation: before(Array<jQuery>)");
    }

    /**
     * Test {401=[FF31], 402=[IE11], 403=[CHROME], 440=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_Function_____Returns_String() throws Exception {
        runTest("manipulation: before(Function) -- Returns String");
    }

    /**
     * Test {402=[FF31], 403=[IE11], 404=[CHROME], 441=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_Function_____Returns_Element() throws Exception {
        runTest("manipulation: before(Function) -- Returns Element");
    }

    /**
     * Test {403=[FF31], 404=[IE11], 405=[CHROME], 442=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_Function_____Returns_Array_Element_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<Element>");
    }

    /**
     * Test {404=[FF31], 405=[IE11], 406=[CHROME], 443=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_Function_____Returns_jQuery() throws Exception {
        runTest("manipulation: before(Function) -- Returns jQuery");
    }

    /**
     * Test {405=[FF31], 406=[IE11], 407=[CHROME], 444=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_Function_____Returns_Array_jQuery_() throws Exception {
        runTest("manipulation: before(Function) -- Returns Array<jQuery>");
    }

    /**
     * Test {406=[FF31], 407=[IE11], 408=[CHROME], 445=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__before_no_op_() throws Exception {
        runTest("manipulation: before(no-op)");
    }

    /**
     * Test {407=[FF31], 408=[IE11], 409=[CHROME], 446=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__before_and_after_w__empty_object___10812_() throws Exception {
        runTest("manipulation: before and after w/ empty object (#10812)");
    }

    /**
     * Test {408=[FF31], 409=[IE11], 410=[CHROME], 447=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation___before___and__after___disconnected_node() throws Exception {
        runTest("manipulation: .before() and .after() disconnected node");
    }

    /**
     * Test {410=[FF31], 411=[IE11], 412=[CHROME], 449=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insert_with__before___on_disconnected_node_first() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node first");
    }

    /**
     * Test {412=[FF31], 413=[IE11], 414=[CHROME], 451=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insert_with__before___on_disconnected_node_last() throws Exception {
        runTest("manipulation: insert with .before() on disconnected node last");
    }

    /**
     * Test {413=[FF31], 414=[IE11], 415=[CHROME], 452=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insertBefore_String_() throws Exception {
        runTest("manipulation: insertBefore(String)");
    }

    /**
     * Test {414=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "27")
    public void manipulation__html_String__tag_hyphenated_elements__Bug__1987_() throws Exception {
        runTest("manipulation: html(String) tag-hyphenated elements (Bug #1987)");
    }

    /**
     * Test {414=[FF31], 415=[IE11], 416=[CHROME], 453=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insertBefore_Element_() throws Exception {
        runTest("manipulation: insertBefore(Element)");
    }

    /**
     * Test {415=[FF31], 416=[IE11], 417=[CHROME], 454=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insertBefore_Array_Element__() throws Exception {
        runTest("manipulation: insertBefore(Array<Element>)");
    }

    /**
     * Test {416=[FF31], 417=[IE11], 418=[CHROME], 455=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insertBefore_jQuery_() throws Exception {
        runTest("manipulation: insertBefore(jQuery)");
    }

    /**
     * Test {417=[FF31], 418=[IE11], 419=[CHROME], 456=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation___after_String_() throws Exception {
        runTest("manipulation: .after(String)");
    }

    /**
     * Test {418=[FF31], 419=[IE11], 420=[CHROME], 457=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation___after_Element_() throws Exception {
        runTest("manipulation: .after(Element)");
    }

    /**
     * Test {419=[FF31], 420=[IE11], 421=[CHROME], 458=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation___after_Array_Element__() throws Exception {
        runTest("manipulation: .after(Array<Element>)");
    }

    /**
     * Test {420=[FF31], 421=[IE11], 422=[CHROME], 459=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation___after_jQuery_() throws Exception {
        runTest("manipulation: .after(jQuery)");
    }

    /**
     * Test {421=[FF31], 422=[IE11], 423=[CHROME], 460=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation___after_Function__returns_String() throws Exception {
        runTest("manipulation: .after(Function) returns String");
    }

    /**
     * Test {422=[FF31], 423=[IE11], 424=[CHROME], 461=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation___after_Function__returns_Element() throws Exception {
        runTest("manipulation: .after(Function) returns Element");
    }

    /**
     * Test {423=[FF31], 424=[IE11], 425=[CHROME], 462=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation___after_Function__returns_Array_Element_() throws Exception {
        runTest("manipulation: .after(Function) returns Array<Element>");
    }

    /**
     * Test {424=[FF31], 425=[IE11], 426=[CHROME], 463=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation___after_Function__returns_jQuery() throws Exception {
        runTest("manipulation: .after(Function) returns jQuery");
    }

    /**
     * Test {425=[FF31], 426=[IE11], 427=[CHROME], 464=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation___after_disconnected_node_() throws Exception {
        runTest("manipulation: .after(disconnected node)");
    }

    /**
     * Test {426=[FF31], 427=[IE11], 428=[CHROME], 465=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insertAfter_String_() throws Exception {
        runTest("manipulation: insertAfter(String)");
    }

    /**
     * Test {427=[FF31], 428=[IE11], 429=[CHROME], 466=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insertAfter_Element_() throws Exception {
        runTest("manipulation: insertAfter(Element)");
    }

    /**
     * Test {428=[FF31], 429=[IE11], 430=[CHROME], 467=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insertAfter_Array_Element__() throws Exception {
        runTest("manipulation: insertAfter(Array<Element>)");
    }

    /**
     * Test {429=[FF31], 430=[IE11], 431=[CHROME], 468=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__insertAfter_jQuery_() throws Exception {
        runTest("manipulation: insertAfter(jQuery)");
    }

    /**
     * Test {430=[FF31], 431=[IE11], 432=[CHROME], 469=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 29, 29",
        FF31 = "0, 29, 29",
        FF38 = "29",
        IE11 = "0, 29, 29")
    public void manipulation__replaceWith_String_Element_Array_Element__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array<Element>|jQuery)");
    }

    /**
     * Test {431=[FF31], 432=[IE11], 433=[CHROME], 470=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 30, 30",
        FF31 = "0, 30, 30",
        FF38 = "30",
        IE11 = "0, 30, 30")
    public void manipulation__replaceWith_Function_() throws Exception {
        runTest("manipulation: replaceWith(Function)");
    }

    /**
     * Test {432=[FF31], 433=[IE11], 434=[CHROME], 471=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void manipulation__replaceWith_string__for_more_than_one_element() throws Exception {
        runTest("manipulation: replaceWith(string) for more than one element");
    }

    /**
     * Test {433=[FF31], 434=[IE11], 435=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        IE11 = "0, 8, 8")
    public void manipulation__Empty_replaceWith___13401___13596_() throws Exception {
        runTest("manipulation: Empty replaceWith (#13401; #13596)");
    }

    /**
     * Test {434=[FF31], 435=[IE11], 436=[CHROME], 473=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__replaceAll_String_() throws Exception {
        runTest("manipulation: replaceAll(String)");
    }

    /**
     * Test {435=[FF31], 436=[IE11], 437=[CHROME], 474=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__replaceAll_Element_() throws Exception {
        runTest("manipulation: replaceAll(Element)");
    }

    /**
     * Test {436=[FF31], 437=[IE11], 438=[CHROME], 475=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void manipulation__replaceAll_Array_Element__() throws Exception {
        runTest("manipulation: replaceAll(Array<Element>)");
    }

    /**
     * Test {437=[FF31], 438=[IE11], 439=[CHROME], 476=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void manipulation__replaceAll_jQuery_() throws Exception {
        runTest("manipulation: replaceAll(jQuery)");
    }

    /**
     * Test {438=[FF31], 439=[IE11], 440=[CHROME], 477=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__jQuery_clone_____8017_() throws Exception {
        runTest("manipulation: jQuery.clone() (#8017)");
    }

    /**
     * Test {439=[FF31], 440=[IE11], 441=[CHROME], 478=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__append_to_multiple_elements___8070_() throws Exception {
        runTest("manipulation: append to multiple elements (#8070)");
    }

    /**
     * Test {440=[FF31], 441=[IE11], 442=[CHROME], 479=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void manipulation__table_manipulation() throws Exception {
        runTest("manipulation: table manipulation");
    }

    /**
     * Test {441=[FF31], 442=[IE11], 443=[CHROME], 480=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 45, 45",
        FF31 = "0, 45, 45",
        FF38 = "45",
        IE11 = "0, 45, 45")
    public void manipulation__clone__() throws Exception {
        runTest("manipulation: clone()");
    }

    /**
     * Test {442=[FF31], 443=[IE11], 444=[CHROME], 481=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void manipulation__clone_script_type_non_javascript____11359_() throws Exception {
        runTest("manipulation: clone(script type=non-javascript) (#11359)");
    }

    /**
     * Test {443=[FF31], 444=[IE11], 445=[CHROME], 482=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void manipulation__clone_form_element___Bug__3879___6655_() throws Exception {
        runTest("manipulation: clone(form element) (Bug #3879, #6655)");
    }

    /**
     * Test {444=[FF31], 445=[IE11], 446=[CHROME], 483=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__clone_multiple_selected_options___Bug__8129_() throws Exception {
        runTest("manipulation: clone(multiple selected options) (Bug #8129)");
    }

    /**
     * Test {445=[FF31], 446=[IE11], 447=[CHROME], 484=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__clone___on_XML_nodes() throws Exception {
        runTest("manipulation: clone() on XML nodes");
    }

    /**
     * Test {446=[FF31], 447=[IE11], 448=[CHROME], 485=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__clone___on_local_XML_nodes_with_html5_nodename() throws Exception {
        runTest("manipulation: clone() on local XML nodes with html5 nodename");
    }

    /**
     * Test {447=[FF31], 448=[IE11], 449=[CHROME], 486=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__html_undefined_() throws Exception {
        runTest("manipulation: html(undefined)");
    }

    /**
     * Test {448=[FF31], 449=[IE11], 450=[CHROME], 487=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__html___on_empty_set() throws Exception {
        runTest("manipulation: html() on empty set");
    }

    /**
     * Test {449=[FF31], 450=[IE11], 451=[CHROME], 488=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 39, 39",
        FF31 = "0, 39, 39",
        FF38 = "39",
        IE11 = "0, 39, 39")
    public void manipulation__html_String_Number_() throws Exception {
        runTest("manipulation: html(String|Number)");
    }

    /**
     * Test {450=[FF31], 451=[IE11], 452=[CHROME], 489=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 39, 39",
        FF31 = "0, 39, 39",
        FF38 = "39",
        IE11 = "0, 39, 39")
    public void manipulation__html_Function_() throws Exception {
        runTest("manipulation: html(Function)");
    }

    /**
     * Test {451=[FF31], 452=[IE11], 453=[CHROME], 490=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__html____text____() throws Exception {
        runTest("manipulation: html( $.text() )");
    }

    /**
     * Test {452=[FF31], 453=[IE11], 454=[CHROME], 491=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__html__fn___returns___text__() throws Exception {
        runTest("manipulation: html( fn ) returns $.text()");
    }

    /**
     * Test {453=[FF31], 454=[IE11], 455=[CHROME], 492=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void manipulation__html_Function__with_incoming_value____direct_selection() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- direct selection");
    }

    /**
     * Test {454=[FF31], 455=[IE11], 456=[CHROME], 493=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 14, 14",
        FF31 = "0, 14, 14",
        FF38 = "14",
        IE11 = "0, 14, 14")
    public void manipulation__html_Function__with_incoming_value____jQuery_contents__() throws Exception {
        runTest("manipulation: html(Function) with incoming value -- jQuery.contents()");
    }

    /**
     * Test {455=[FF31], 456=[IE11], 457=[CHROME], 494=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__clone___html___don_t_expose_jQuery_Sizzle_expandos___12858_() throws Exception {
        runTest("manipulation: clone()/html() don't expose jQuery/Sizzle expandos (#12858)");
    }

    /**
     * Test {456=[FF31], 457=[IE11], 458=[CHROME], 495=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void manipulation__remove___no_filters() throws Exception {
        runTest("manipulation: remove() no filters");
    }

    /**
     * Test {457=[FF31], 458=[IE11], 459=[CHROME], 496=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void manipulation__remove___with_filters() throws Exception {
        runTest("manipulation: remove() with filters");
    }

    /**
     * Test {458=[FF31], 459=[IE11], 460=[CHROME], 497=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__remove___event_cleaning() throws Exception {
        runTest("manipulation: remove() event cleaning");
    }

    /**
     * Test {459=[FF31], 460=[IE11], 461=[CHROME], 498=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__remove___in_document_order__13779() throws Exception {
        runTest("manipulation: remove() in document order #13779");
    }

    /**
     * Test {460=[FF31], 461=[IE11], 462=[CHROME], 499=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void manipulation__detach___no_filters() throws Exception {
        runTest("manipulation: detach() no filters");
    }

    /**
     * Test {461=[FF31], 462=[IE11], 463=[CHROME], 500=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void manipulation__detach___with_filters() throws Exception {
        runTest("manipulation: detach() with filters");
    }

    /**
     * Test {462=[FF31], 463=[IE11], 464=[CHROME], 501=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__detach___event_cleaning() throws Exception {
        runTest("manipulation: detach() event cleaning");
    }

    /**
     * Test {463=[FF31], 464=[IE11], 465=[CHROME], 502=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void manipulation__empty__() throws Exception {
        runTest("manipulation: empty()");
    }

    /**
     * Test {464=[FF31], 465=[IE11], 466=[CHROME], 503=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 14, 14",
        FF31 = "0, 14, 14",
        FF38 = "14",
        IE11 = "0, 14, 14")
    public void manipulation__jQuery_cleanData() throws Exception {
        runTest("manipulation: jQuery.cleanData");
    }

    /**
     * Test {465=[FF31], 466=[IE11], 467=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        IE11 = "0, 1, 1")
    public void manipulation__jQuery_buildFragment___no_plain_text_caching__Bug__6779_() throws Exception {
        runTest("manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779)");
    }

    /**
     * Test {466=[FF31], 467=[IE11], 468=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        IE11 = "0, 3, 3")
    public void manipulation__jQuery_html___execute_scripts_escaped_with_html_comment_or_CDATA___9221_() throws Exception {
        runTest("manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221)");
    }

    /**
     * Test {467=[FF31], 468=[IE11], 469=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        IE11 = "0, 1, 1")
    public void manipulation__jQuery_buildFragment___plain_objects_are_not_a_document__8950() throws Exception {
        runTest("manipulation: jQuery.buildFragment - plain objects are not a document #8950");
    }

    /**
     * Test {468=[FF31], 469=[IE11], 470=[CHROME], 509=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__jQuery_clone___no_exceptions_for_object_elements__9587() throws Exception {
        runTest("manipulation: jQuery.clone - no exceptions for object elements #9587");
    }

    /**
     * Test {469=[FF31], 470=[IE11], 471=[CHROME], 510=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void manipulation__Cloned__detached_HTML5_elems___10667_10670_() throws Exception {
        runTest("manipulation: Cloned, detached HTML5 elems (#10667,10670)");
    }

    /**
     * Test {470=[FF31], 471=[IE11], 472=[CHROME], 511=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__Guard_against_exceptions_when_clearing_safeChildNodes() throws Exception {
        runTest("manipulation: Guard against exceptions when clearing safeChildNodes");
    }

    /**
     * Test {471=[FF31], 472=[IE11], 473=[CHROME], 512=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void manipulation__Ensure_oldIE_creates_a_new_set_on_appendTo___8894_() throws Exception {
        runTest("manipulation: Ensure oldIE creates a new set on appendTo (#8894)");
    }

    /**
     * Test {472=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "25")
    public void manipulation__Empty_replaceWith__trac_13401__trac_13596__gh_2204_() throws Exception {
        runTest("manipulation: Empty replaceWith (trac-13401; trac-13596; gh-2204)");
    }

    /**
     * Test {472=[FF31], 473=[IE11], 474=[CHROME], 513=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__html_____script_exceptions_bubble___11743_() throws Exception {
        runTest("manipulation: html() - script exceptions bubble (#11743)");
    }

    /**
     * Test {473=[FF31], 474=[IE11], 475=[CHROME], 514=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__checked_state_is_cloned_with_clone__() throws Exception {
        runTest("manipulation: checked state is cloned with clone()");
    }

    /**
     * Test {474=[FF31], 475=[IE11], 476=[CHROME], 515=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__manipulate_mixed_jQuery_and_text___12384___12346_() throws Exception {
        runTest("manipulation: manipulate mixed jQuery and text (#12384, #12346)");
    }

    /**
     * Test {475=[FF31], 476=[IE11], 477=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        IE11 = "0, 1, 1")
    public void manipulation__buildFragment_works_even_if_document_0__is_iframe_s_window_object_in_IE9_10___12266_() throws Exception {
        runTest("manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266)");
    }

    /**
     * Test {476=[FF31], 477=[IE11], 478=[CHROME], 516=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 13, 13",
        FF31 = "0, 13, 13",
        FF38 = "13",
        IE11 = "0, 13, 13")
    public void manipulation__script_evaluation___11795_() throws Exception {
        runTest("manipulation: script evaluation (#11795)");
    }

    /**
     * Test {477=[FF31], 478=[IE11], 479=[CHROME], 517=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void manipulation__jQuery__evalUrl___12838_() throws Exception {
        runTest("manipulation: jQuery._evalUrl (#12838)");
    }

    /**
     * Test {478=[FF31], 479=[IE11], 480=[CHROME], 519=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void manipulation__insertAfter__insertBefore__etc_do_not_work_when_destination_is_original_element__Element_is_removed___4087_() throws Exception {
        runTest("manipulation: insertAfter, insertBefore, etc do not work when destination is original element. Element is removed (#4087)");
    }

    /**
     * Test {479=[FF31], 480=[IE11], 481=[CHROME], 520=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void manipulation__Index_for_function_argument_should_be_received___13094_() throws Exception {
        runTest("manipulation: Index for function argument should be received (#13094)");
    }

    /**
     * Test {480=[FF31], 481=[IE11], 482=[CHROME], 521=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void manipulation__Make_sure_jQuery_fn_remove_can_work_on_elements_in_documentFragment() throws Exception {
        runTest("manipulation: Make sure jQuery.fn.remove can work on elements in documentFragment");
    }

    /**
     * Test {481=[FF31], 482=[IE11], 483=[CHROME], 523=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 19, 19",
        FF31 = "0, 19, 19",
        FF38 = "19",
        IE11 = "0, 19, 19")
    public void wrap__wrap_String_Element_() throws Exception {
        runTest("wrap: wrap(String|Element)");
    }

    /**
     * Test {482=[FF31], 483=[IE11], 484=[CHROME], 524=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 19, 19",
        FF31 = "0, 19, 19",
        FF38 = "19",
        IE11 = "0, 19, 19")
    public void wrap__wrap_Function_() throws Exception {
        runTest("wrap: wrap(Function)");
    }

    /**
     * Test {483=[FF31], 484=[IE11], 485=[CHROME], 525=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void wrap__wrap_Function__with_index___10177_() throws Exception {
        runTest("wrap: wrap(Function) with index (#10177)");
    }

    /**
     * Test {484=[FF31], 485=[IE11], 486=[CHROME], 526=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    public void wrap__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("wrap: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {485=[FF31], 486=[IE11], 487=[CHROME], 527=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void wrap__wrapAll_String_() throws Exception {
        runTest("wrap: wrapAll(String)");
    }

    /**
     * Test {486=[FF31], 487=[IE11], 488=[CHROME], 528=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void wrap__wrapAll_Element_() throws Exception {
        runTest("wrap: wrapAll(Element)");
    }

    /**
     * Test {487=[FF31], 488=[IE11], 489=[CHROME], 529=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void wrap__wrapInner_String_() throws Exception {
        runTest("wrap: wrapInner(String)");
    }

    /**
     * Test {488=[FF31], 489=[IE11], 490=[CHROME], 530=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void wrap__wrapInner_Element_() throws Exception {
        runTest("wrap: wrapInner(Element)");
    }

    /**
     * Test {489=[FF31], 490=[IE11], 491=[CHROME], 531=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void wrap__wrapInner_Function__returns_String() throws Exception {
        runTest("wrap: wrapInner(Function) returns String");
    }

    /**
     * Test {490=[FF31], 491=[IE11], 492=[CHROME], 532=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void wrap__wrapInner_Function__returns_Element() throws Exception {
        runTest("wrap: wrapInner(Function) returns Element");
    }

    /**
     * Test {491=[FF31], 492=[IE11], 493=[CHROME], 533=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void wrap__unwrap__() throws Exception {
        runTest("wrap: unwrap()");
    }

    /**
     * Test {492=[FF31], 493=[IE11], 494=[CHROME], 534=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void wrap__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("wrap: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {493=[FF31], 494=[IE11], 495=[CHROME], 535=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void wrap__wrapping_scripts___10470_() throws Exception {
        runTest("wrap: wrapping scripts (#10470)");
    }

    /**
     * Test {494=[FF31], 495=[IE11], 496=[CHROME], 536=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 43, 43",
        FF31 = "0, 43, 43",
        FF38 = "43",
        IE11 = "0, 43, 43")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__css_String_Hash_() throws Exception {
        runTest("css: css(String|Hash)");
    }

    /**
     * Test {495=[FF31], 496=[IE11], 497=[CHROME], 537=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 29, 29",
        FF31 = "0, 29, 29",
        FF38 = "29",
        IE11 = "0, 29, 29")
    public void css__css___explicit_and_relative_values() throws Exception {
        runTest("css: css() explicit and relative values");
    }

    /**
     * Test {496=[FF31], 497=[IE11], 498=[CHROME], 539=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 20, 20",
        FF31 = "0, 20, 20",
        FF38 = "19",
        IE11 = "0, 20, 20")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__css_String__Object_() throws Exception {
        runTest("css: css(String, Object)");
    }

    /**
     * Test {497=[FF31], 498=[IE11], 499=[CHROME], 541=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void css__css_Array_() throws Exception {
        runTest("css: css(Array)");
    }

    /**
     * Test {498=[FF31], 499=[IE11], 500=[CHROME], 542=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void css__css_String__Function_() throws Exception {
        runTest("css: css(String, Function)");
    }

    /**
     * Test {499=[FF31], 500=[IE11], 501=[CHROME], 543=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void css__css_String__Function__with_incoming_value() throws Exception {
        runTest("css: css(String, Function) with incoming value");
    }

    /**
     * Test {500=[FF31], 501=[IE11], 502=[CHROME], 544=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void css__css_Object__where_values_are_Functions() throws Exception {
        runTest("css: css(Object) where values are Functions");
    }

    /**
     * Test {501=[FF31], 502=[IE11], 503=[CHROME], 545=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void css__css_Object__where_values_are_Functions_with_incoming_values() throws Exception {
        runTest("css: css(Object) where values are Functions with incoming values");
    }

    /**
     * Test {502=[FF31], 503=[IE11], 504=[CHROME], 546=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void css__show____hide__() throws Exception {
        runTest("css: show(); hide()");
    }

    /**
     * Test {503=[FF31], 504=[IE11], 505=[CHROME], 547=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 18, 18",
        FF31 = "0, 18, 18",
        FF38 = "18",
        IE11 = "0, 18, 18")
    public void css__show___() throws Exception {
        runTest("css: show();");
    }

    /**
     * Test {504=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void manipulation__jQuery_cleanData_eliminates_all_private_data__gh_2127_() throws Exception {
        runTest("manipulation: jQuery.cleanData eliminates all private data (gh-2127)");
    }

    /**
     * Test {504=[FF31], 505=[IE11], 506=[CHROME], 548=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__show___resolves_correct_default_display__8099() throws Exception {
        runTest("css: show() resolves correct default display #8099");
    }

    /**
     * Test {505=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void manipulation__jQuery_cleanData_eliminates_all_public_data() throws Exception {
        runTest("manipulation: jQuery.cleanData eliminates all public data");
    }

    /**
     * Test {505=[FF31], 506=[IE11], 507=[CHROME], 549=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 13, 13",
        FF31 = "0, 13, 13",
        FF38 = "13",
        IE11 = "0, 13, 13")
    public void css__show___resolves_correct_default_display_for_detached_nodes() throws Exception {
        runTest("css: show() resolves correct default display for detached nodes");
    }

    /**
     * Test {506=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void manipulation__domManip_plain_text_caching__trac_6779_() throws Exception {
        runTest("manipulation: domManip plain-text caching (trac-6779)");
    }

    /**
     * Test {506=[FF31], 507=[IE11], 508=[CHROME], 550=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__show___resolves_correct_default_display__10227() throws Exception {
        runTest("css: show() resolves correct default display #10227");
    }

    /**
     * Test {507=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void manipulation__domManip_executes_scripts_containing_html_comments_or_CDATA__trac_9221_() throws Exception {
        runTest("manipulation: domManip executes scripts containing html comments or CDATA (trac-9221)");
    }

    /**
     * Test {507=[FF31], 508=[IE11], 509=[CHROME], 551=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__show___resolves_correct_default_display_when_iframe_display_none__12904() throws Exception {
        runTest("css: show() resolves correct default display when iframe display:none #12904");
    }

    /**
     * Test {508=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void manipulation__domManip_tolerates_window_valued_document_0__in_IE9_10__trac_12266_() throws Exception {
        runTest("manipulation: domManip tolerates window-valued document[0] in IE9/10 (trac-12266)");
    }

    /**
     * Test {508=[FF31], 509=[IE11], 510=[CHROME], 552=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void css__toggle__() throws Exception {
        runTest("css: toggle()");
    }

    /**
     * Test {509=[FF31], 510=[IE11], 511=[CHROME], 553=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void css__hide_hidden_elements__bug__7141_() throws Exception {
        runTest("css: hide hidden elements (bug #7141)");
    }

    /**
     * Test {510=[FF31], 511=[IE11], 512=[CHROME], 554=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void css__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {511=[FF31], 512=[IE11], 513=[CHROME], 555=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void css__internal_ref_to_elem_runtimeStyle__bug__7608_() throws Exception {
        runTest("css: internal ref to elem.runtimeStyle (bug #7608)");
    }

    /**
     * Test {512=[FF31], 513=[IE11], 514=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        IE11 = "0, 1, 1")
    public void css__marginRight_computed_style__bug__3333_() throws Exception {
        runTest("css: marginRight computed style (bug #3333)");
    }

    /**
     * Test {513=[FF31], 514=[IE11], 515=[CHROME], 557=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__box_model_properties_incorrectly_returning___instead_of_px__see__10639_and__12088() throws Exception {
        runTest("css: box model properties incorrectly returning % instead of px, see #10639 and #12088");
    }

    /**
     * Test {514=[FF31], 515=[IE11], 516=[CHROME], 558=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void css__jQuery_cssProps_behavior___bug__8402_() throws Exception {
        runTest("css: jQuery.cssProps behavior, (bug #8402)");
    }

    /**
     * Test {515=[FF31], 516=[IE11], 517=[CHROME], 559=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void css__widows___orphans__8936() throws Exception {
        runTest("css: widows & orphans #8936");
    }

    /**
     * Test {516=[FF31], 517=[IE11], 518=[CHROME], 560=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void css__can_t_get_css_for_disconnected_in_IE_9__see__10254_and__8388() throws Exception {
        runTest("css: can't get css for disconnected in IE<9, see #10254 and #8388");
    }

    /**
     * Test {517=[FF31], 518=[IE11], 519=[CHROME], 561=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void css__can_t_get_background_position_in_IE_9__see__10796() throws Exception {
        runTest("css: can't get background-position in IE<9, see #10796");
    }

    /**
     * Test {518=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "5")
    public void manipulation__jQuery_htmlPrefilter__gh_1747_() throws Exception {
        runTest("manipulation: jQuery.htmlPrefilter (gh-1747)");
    }

    /**
     * Test {518=[FF31], 519=[IE11], 520=[CHROME], 562=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void css__percentage_properties_for_bottom_and_right_in_IE_9_should_not_be_incorrectly_transformed_to_pixels__see__11311() throws Exception {
        runTest("css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pixels, see #11311");
    }

    /**
     * Test {519=[FF31], 520=[IE11], 521=[CHROME], 563=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__percentage_properties_for_left_and_top_should_be_transformed_to_pixels__see__9505() throws Exception {
        runTest("css: percentage properties for left and top should be transformed to pixels, see #9505");
    }

    /**
     * Test {520=[FF31], 521=[IE11], 522=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        IE11 = "0, 2, 2")
    @NotYetImplemented({ FF, IE11 })
    public void css__Do_not_append_px___9548___12990_() throws Exception {
        runTest("css: Do not append px (#9548, #12990)");
    }

    /**
     * Test {521=[FF31], 522=[IE11], 523=[CHROME], 565=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__css__width___and_css__height___should_respect_box_sizing__see__11004() throws Exception {
        runTest("css: css('width') and css('height') should respect box-sizing, see #11004");
    }

    /**
     * Test {522=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void manipulation__Insert_script_with_data_URI__gh_1887_() throws Exception {
        runTest("manipulation: Insert script with data-URI (gh-1887)");
    }

    /**
     * Test {522=[FF31], 523=[IE11], 524=[CHROME], 566=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__css__width___should_work_correctly_before_document_ready___14084_() throws Exception {
        runTest("css: css('width') should work correctly before document ready (#14084)");
    }

    /**
     * Test {523=[FF31], 524=[IE11], 525=[CHROME], 567=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void css__certain_css_values_of__normal__should_be_convertable_to_a_number__see__8627() throws Exception {
        runTest("css: certain css values of 'normal' should be convertable to a number, see #8627");
    }

    /**
     * Test {524=[FF31], 525=[IE11], 526=[CHROME], 568=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 15, 15",
        FF31 = "0, 15, 15",
        FF38 = "15",
        IE11 = "0, 15, 15")
    public void css__cssHooks___expand() throws Exception {
        runTest("css: cssHooks - expand");
    }

    /**
     * Test {525=[FF31], 526=[IE11], 527=[CHROME], 569=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void css__css_opacity_consistency_across_browsers___12685_() throws Exception {
        runTest("css: css opacity consistency across browsers (#12685)");
    }

    /**
     * Test {526=[FF31], 527=[IE11], 528=[CHROME], 570=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 13, 13",
        FF31 = "0, 13, 13",
        FF38 = "17",
        IE11 = "0, 13, 13")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css___visible__hidden_selectors() throws Exception {
        runTest("css: :visible/:hidden selectors");
    }

    /**
     * Test {527=[FF31], 528=[IE11], 529=[CHROME], 571=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void css__Keep_the_last_style_if_the_new_one_isn_t_recognized_by_the_browser___14836_() throws Exception {
        runTest("css: Keep the last style if the new one isn't recognized by the browser (#14836)");
    }

    /**
     * Test {528=[FF31], 529=[IE11], 530=[CHROME], 572=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void css__Reset_the_style_if_set_to_an_empty_string() throws Exception {
        runTest("css: Reset the style if set to an empty string");
    }

    /**
     * Test {529=[FF31], 530=[IE11], 531=[CHROME], 573=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 24, 24",
        FF31 = "0, 24, 24",
        FF38 = "24",
        IE11 = "0, 24, 24")
    public void css__Clearing_a_Cloned_Element_s_Style_Shouldn_t_Clear_the_Original_Element_s_Style___8908_() throws Exception {
        runTest("css: Clearing a Cloned Element's Style Shouldn't Clear the Original Element's Style (#8908)");
    }

    /**
     * Test {530=[FF31], 531=[IE11], 532=[CHROME], 575=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void css__Make_sure_initialized_display_value_for_disconnected_nodes_is_correct___13310_() throws Exception {
        runTest("css: Make sure initialized display value for disconnected nodes is correct (#13310)");
    }

    /**
     * Test {531=[FF31], 532=[IE11], 533=[CHROME], 576=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void css__show___after_hide___should_always_set_display_to_initial_value___14750_() throws Exception {
        runTest("css: show() after hide() should always set display to initial value (#14750)");
    }

    /**
     * Test {532=[FF31], 533=[IE11], 534=[CHROME], 577=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void css__Don_t_append_px_to_CSS__order__value___14049_() throws Exception {
        runTest("css: Don't append px to CSS \"order\" value (#14049)");
    }

    /**
     * Test {533=[FF31], 534=[IE11], 535=[CHROME], 578=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void css__Do_not_throw_on_frame_elements_from_css_method___15098_() throws Exception {
        runTest("css: Do not throw on frame elements from css method (#15098)");
    }

    /**
     * Test {534=[FF31], 535=[IE11], 536=[CHROME], 582=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 22, 22",
        FF31 = "0, 22, 22",
        FF38 = "23",
        IE11 = "0, 22, 22")
    public void serialize__jQuery_param__() throws Exception {
        runTest("serialize: jQuery.param()");
    }

    /**
     * Test {535=[FF31], 536=[IE11], 537=[CHROME], 583=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void serialize__jQuery_param___Constructed_prop_values() throws Exception {
        runTest("serialize: jQuery.param() Constructed prop values");
    }

    /**
     * Test {536=[FF31], 537=[IE11], 538=[CHROME], 584=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "6",
        IE11 = "0, 5, 5")
    public void serialize__serialize__() throws Exception {
        runTest("serialize: serialize()");
    }

    /**
     * Test {537=[FF31], 538=[IE11], 539=[CHROME], 587=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks");
    }

    /**
     * Test {538=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "16")
    public void css__css___non_px_relative_values__gh_1711_() throws Exception {
        runTest("css: css() non-px relative values (gh-1711)");
    }

    /**
     * Test {538=[FF31], 539=[IE11], 540=[CHROME], 588=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks____url__options__syntax() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks - (url, options) syntax");
    }

    /**
     * Test {539=[FF31], 540=[IE11], 541=[CHROME], 591=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (late binding)");
    }

    /**
     * Test {540=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "4")
    public void css__css_String__Object__with_negative_values() throws Exception {
        runTest("css: css(String, Object) with negative values");
    }

    /**
     * Test {540=[FF31], 541=[IE11], 542=[CHROME], 592=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__oncomplete_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (oncomplete binding)");
    }

    /**
     * Test {541=[FF31], 542=[IE11], 543=[CHROME], 593=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void ajax__jQuery_ajax_____error_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - error callbacks");
    }

    /**
     * Test {542=[FF31], 543=[IE11], 544=[CHROME], 594=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void ajax__jQuery_ajax_____textStatus_and_errorThrown_values() throws Exception {
        runTest("ajax: jQuery.ajax() - textStatus and errorThrown values");
    }

    /**
     * Test {543=[FF31], 544=[IE11], 545=[CHROME], 595=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____responseText_on_error() throws Exception {
        runTest("ajax: jQuery.ajax() - responseText on error");
    }

    /**
     * Test {544=[FF31], 545=[IE11], 546=[CHROME], 596=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: jQuery.ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {545=[FF31], 546=[IE11], 547=[CHROME], 597=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void ajax__jQuery_ajax_____headers() throws Exception {
        runTest("ajax: jQuery.ajax() - headers");
    }

    /**
     * Test {546=[FF31], 547=[IE11], 548=[CHROME], 598=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____Accept_header() throws Exception {
        runTest("ajax: jQuery.ajax() - Accept header");
    }

    /**
     * Test {547=[FF31], 548=[IE11], 549=[CHROME], 599=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____contentType() throws Exception {
        runTest("ajax: jQuery.ajax() - contentType");
    }

    /**
     * Test {548=[FF31], 549=[IE11], 550=[CHROME], 600=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: jQuery.ajax() - protocol-less urls");
    }

    /**
     * Test {549=[FF31], 550=[IE11], 551=[CHROME], 601=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____hash() throws Exception {
        runTest("ajax: jQuery.ajax() - hash");
    }

    /**
     * Test {550=[FF31], 551=[IE11], 552=[CHROME], 602=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void ajax__jQuery_ajax_____cross_domain_detection() throws Exception {
        runTest("ajax: jQuery.ajax() - cross-domain detection");
    }

    /**
     * Test {551=[FF31], 552=[IE11], 553=[CHROME], 603=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void ajax__jQuery_ajax_____abort() throws Exception {
        runTest("ajax: jQuery.ajax() - abort");
    }

    /**
     * Test {552=[FF31], 553=[IE11], 554=[CHROME], 604=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    public void ajax__jQuery_ajax_____events_with_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events with context");
    }

    /**
     * Test {553=[FF31], 554=[IE11], 555=[CHROME], 605=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____events_without_context() throws Exception {
        runTest("ajax: jQuery.ajax() - events without context");
    }

    /**
     * Test {554=[FF31], 555=[IE11], 556=[CHROME], 606=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___15118___jQuery_ajax_____function_without_jQuery_event() throws Exception {
        runTest("ajax: #15118 - jQuery.ajax() - function without jQuery.event");
    }

    /**
     * Test {555=[FF31], 556=[IE11], 557=[CHROME], 608=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____context_modification() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification");
    }

    /**
     * Test {556=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void css__computed_margins__trac_3333__gh_2237_() throws Exception {
        runTest("css: computed margins (trac-3333; gh-2237)");
    }

    /**
     * Test {556=[FF31], 557=[IE11], 558=[CHROME], 609=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax() - context modification through ajaxSetup");
    }

    /**
     * Test {557=[FF31], 558=[IE11], 559=[CHROME], 610=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____disabled_globals() throws Exception {
        runTest("ajax: jQuery.ajax() - disabled globals");
    }

    /**
     * Test {558=[FF31], 559=[IE11], 560=[CHROME], 611=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {559=[FF31], 560=[IE11], 561=[CHROME], 612=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax() - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {560=[FF31], 561=[IE11], 562=[CHROME], 613=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - HEAD requests");
    }

    /**
     * Test {561=[FF31], 562=[IE11], 563=[CHROME], 614=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend");
    }

    /**
     * Test {562=[FF31], 563=[IE11], 564=[CHROME], 615=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax() - beforeSend, cancel request manually");
    }

    /**
     * Test {563=[FF31], 564=[IE11], 565=[CHROME], 616=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void ajax__jQuery_ajax_____dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax() - dataType html");
    }

    /**
     * Test {564=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void css__Do_not_append_px___9548___12990___2792_() throws Exception {
        runTest("css: Do not append px (#9548, #12990, #2792)");
    }

    /**
     * Test {564=[FF31], 565=[IE11], 566=[CHROME], 617=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____synchronous_request() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request");
    }

    /**
     * Test {565=[FF31], 566=[IE11], 567=[CHROME], 618=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - synchronous request with callbacks");
    }

    /**
     * Test {566=[FF31], 567=[IE11], 568=[CHROME], 619=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void ajax__jQuery_ajax____jQuery_get_Script_JSON_____jQuery_post____pass_through_request_object() throws Exception {
        runTest("ajax: jQuery.ajax(), jQuery.get[Script|JSON](), jQuery.post(), pass-through request object");
    }

    /**
     * Test {567=[FF31], 568=[IE11], 569=[CHROME], 620=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    public void ajax__jQuery_ajax_____cache() throws Exception {
        runTest("ajax: jQuery.ajax() - cache");
    }

    /**
     * Test {568=[FF31], 569=[IE11], 570=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        IE11 = "0, 4, 4")
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Same Domain");
    }

    /**
     * Test {569=[FF31], 570=[IE11], 571=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        IE11 = "0, 9, 9")
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Same Domain");
    }

    /**
     * Test {570=[FF31], 571=[IE11], 572=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Same Domain");
    }

    /**
     * Test {571=[FF31], 572=[IE11], 573=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____JSONP___POST___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Same Domain");
    }

    /**
     * Test {572=[FF31], 573=[IE11], 574=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____JSONP___Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Same Domain");
    }

    /**
     * Test {573=[FF31], 574=[IE11], 575=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        IE11 = "0, 4, 4")
    @NotYetImplemented(IE11)
    public void ajax__jQuery_ajax_____JSONP___Query_String___n____Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Query String (?n) - Cross Domain");
    }

    /**
     * Test {574=[FF31], 575=[IE11], 576=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        IE11 = "0, 9, 9")
    @NotYetImplemented(IE11)
    public void ajax__jQuery_ajax_____JSONP___Explicit_callback_param___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Explicit callback param - Cross Domain");
    }

    /**
     * Test {575=[FF31], 576=[IE11], 577=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        IE11 = "0, 2, 2")
    @NotYetImplemented(IE11)
    public void ajax__jQuery_ajax_____JSONP___Callback_in_data___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Callback in data - Cross Domain");
    }

    /**
     * Test {576=[FF31], 577=[IE11], 578=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        IE11 = "0, 3, 3")
    @NotYetImplemented(IE11)
    public void ajax__jQuery_ajax_____JSONP___POST___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - POST - Cross Domain");
    }

    /**
     * Test {577=[FF31], 578=[IE11], 579=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        IE11 = "0, 3, 3")
    @NotYetImplemented(IE11)
    public void ajax__jQuery_ajax_____JSONP___Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP - Cross Domain");
    }

    /**
     * Test {578=[FF31], 579=[IE11], 580=[CHROME], 623=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote");
    }

    /**
     * Test {579=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "0")
    public void skippedcss__get_upper_case_alpha_opacity_in_IE8() throws Exception {
        runTest("skippedcss: get upper case alpha opacity in IE8");
    }

    /**
     * Test {579=[FF31], 580=[IE11], 581=[CHROME], 624=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____script__Remote_with_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with POST");
    }

    /**
     * Test {580=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void css__Don_t_default_to_a_cached_previously_used_wrong_prefixed_name__gh_2015_() throws Exception {
        runTest("css: Don't default to a cached previously used wrong prefixed name (gh-2015)");
    }

    /**
     * Test {580=[FF31], 581=[IE11], 582=[CHROME], 625=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote_with_scheme_less_URL() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with scheme-less URL");
    }

    /**
     * Test {581=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void css__Don_t_detect_fake_set_properties_on_a_node_when_caching_the_prefixed_version() throws Exception {
        runTest("css: Don't detect fake set properties on a node when caching the prefixed version");
    }

    /**
     * Test {581=[FF31], 582=[IE11], 583=[CHROME], 626=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____malformed_JSON() throws Exception {
        runTest("ajax: jQuery.ajax() - malformed JSON");
    }

    /**
     * Test {582=[FF31], 583=[IE11], 584=[CHROME], 627=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____script_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - script by content-type");
    }

    /**
     * Test {583=[FF31], 584=[IE11], 585=[CHROME], 628=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void ajax__jQuery_ajax_____JSON_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type");
    }

    /**
     * Test {584=[FF31], 585=[IE11], 586=[CHROME], 629=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void ajax__jQuery_ajax_____JSON_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - JSON by content-type disabled with options");
    }

    /**
     * Test {585=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void ajax__Unit_Testing_Environment() throws Exception {
        runTest("ajax: Unit Testing Environment");
    }

    /**
     * Test {585=[FF31], 586=[IE11], 587=[CHROME], 630=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____simple_get() throws Exception {
        runTest("ajax: jQuery.ajax() - simple get");
    }

    /**
     * Test {586=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void ajax__XMLHttpRequest___Attempt_to_block_tests_because_of_dangling_XHR_requests__IE_() throws Exception {
        runTest("ajax: XMLHttpRequest - Attempt to block tests because of dangling XHR requests (IE)");
    }

    /**
     * Test {586=[FF31], 587=[IE11], 588=[CHROME], 631=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____simple_post() throws Exception {
        runTest("ajax: jQuery.ajax() - simple post");
    }

    /**
     * Test {587=[FF31], 588=[IE11], 589=[CHROME], 632=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____data_option___empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: jQuery.ajax() - data option - empty bodies for non-GET requests");
    }

    /**
     * Test {588=[FF31], 589=[IE11], 590=[CHROME], 633=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "4",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (cache)");
    }

    /**
     * Test {589=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void ajax__jQuery_ajax_____execute_js_for_crossOrigin_when_dataType_option_is_provided() throws Exception {
        runTest("ajax: jQuery.ajax() - execute js for crossOrigin when dataType option is provided");
    }

    /**
     * Test {589=[FF31], 590=[IE11], 591=[CHROME], 634=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "4",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (cache)");
    }

    /**
     * Test {590=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void ajax__jQuery_ajax_____do_not_execute_js__crossOrigin_() throws Exception {
        runTest("ajax: jQuery.ajax() - do not execute js (crossOrigin)");
    }

    /**
     * Test {590=[FF31], 591=[IE11], 592=[CHROME], 635=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "4",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - If-Modified-Since support (no cache)");
    }

    /**
     * Test {591=[FF31], 592=[IE11], 593=[CHROME], 636=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "4",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax() - Etag support (no cache)");
    }

    /**
     * Test {592=[FF31], 593=[IE11], 594=[CHROME], 637=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____failing_cross_domain__non_existing_() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain (non-existing)");
    }

    /**
     * Test {593=[FF31], 594=[IE11], 595=[CHROME], 638=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____failing_cross_domain() throws Exception {
        runTest("ajax: jQuery.ajax() - failing cross-domain");
    }

    /**
     * Test {594=[FF31], 595=[IE11], 596=[CHROME], 639=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____atom_xml() throws Exception {
        runTest("ajax: jQuery.ajax() - atom+xml");
    }

    /**
     * Test {595=[FF31], 596=[IE11], 597=[CHROME], 640=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_ajax_____statusText() throws Exception {
        runTest("ajax: jQuery.ajax() - statusText");
    }

    /**
     * Test {596=[FF31], 597=[IE11], 598=[CHROME], 641=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 20, 20",
        FF31 = "0, 20, 20",
        FF38 = "20",
        IE11 = "0, 20, 20")
    public void ajax__jQuery_ajax_____statusCode() throws Exception {
        runTest("ajax: jQuery.ajax() - statusCode");
    }

    /**
     * Test {597=[FF31], 598=[IE11], 599=[CHROME], 642=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void ajax__jQuery_ajax_____transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax() - transitive conversions");
    }

    /**
     * Test {598=[FF31], 599=[IE11], 600=[CHROME], 643=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_ajax_____overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax() - overrideMimeType");
    }

    /**
     * Test {599=[FF31], 600=[IE11], 601=[CHROME], 644=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajax_____empty_json_gets_to_error_callback_instead_of_success_callback_() throws Exception {
        runTest("ajax: jQuery.ajax() - empty json gets to error callback instead of success callback.");
    }

    /**
     * Test {600=[FF31], 601=[IE11], 602=[CHROME], 645=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax___2688___jQuery_ajax_____beforeSend__cancel_request() throws Exception {
        runTest("ajax: #2688 - jQuery.ajax() - beforeSend, cancel request");
    }

    /**
     * Test {601=[FF31], 602=[IE11], 603=[CHROME], 646=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___2806___jQuery_ajax_____data_option___evaluate_function_values() throws Exception {
        runTest("ajax: #2806 - jQuery.ajax() - data option - evaluate function values");
    }

    /**
     * Test {602=[FF31], 603=[IE11], 604=[CHROME], 647=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___7531___jQuery_ajax_____Location_object_as_url() throws Exception {
        runTest("ajax: #7531 - jQuery.ajax() - Location object as url");
    }

    /**
     * Test {603=[FF31], 604=[IE11], 605=[CHROME], 648=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Same_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Same Domain");
    }

    /**
     * Test {604=[FF31], 605=[IE11], 606=[CHROME], 649=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___7578___jQuery_ajax_____JSONP___default_for_cache_option___Cross_Domain() throws Exception {
        runTest("ajax: #7578 - jQuery.ajax() - JSONP - default for cache option - Cross Domain");
    }

    /**
     * Test {605=[FF31], 606=[IE11], 607=[CHROME], 650=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void ajax___8107___jQuery_ajax_____multiple_method_signatures_introduced_in_1_5() throws Exception {
        runTest("ajax: #8107 - jQuery.ajax() - multiple method signatures introduced in 1.5");
    }

    /**
     * Test {606=[FF31], 607=[IE11], 608=[CHROME], 621=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "4",
        IE11 = "0, 2, 2")
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Same_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Same Domain");
    }

    /**
     * Test {607=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "3")
    public void ajax___15160___jQuery_ajax_____request_manually_aborted_in_ajaxSend() throws Exception {
        runTest("ajax: #15160 - jQuery.ajax() - request manually aborted in ajaxSend");
    }

    /**
     * Test {607=[FF31], 608=[IE11], 609=[CHROME], 622=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "4",
        IE11 = "0, 2, 2")
    @NotYetImplemented(IE11)
    public void ajax___8205___jQuery_ajax_____JSONP___re_use_callbacks_name___Cross_Domain() throws Exception {
        runTest("ajax: #8205 - jQuery.ajax() - JSONP - re-use callbacks name - Cross Domain");
    }

    /**
     * Test {608=[FF31], 609=[IE11], 610=[CHROME], 651=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax___9887___jQuery_ajax_____Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: #9887 - jQuery.ajax() - Context with circular references (#9887)");
    }

    /**
     * Test {609=[FF31], 610=[IE11], 611=[CHROME], 652=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void ajax___10093___jQuery_ajax_____falsy_url_as_argument() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url as argument");
    }

    /**
     * Test {610=[FF31], 611=[IE11], 612=[CHROME], 653=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void ajax___10093___jQuery_ajax_____falsy_url_in_settings_object() throws Exception {
        runTest("ajax: #10093 - jQuery.ajax() - falsy url in settings object");
    }

    /**
     * Test {611=[FF31], 612=[IE11], 613=[CHROME], 654=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax___11151___jQuery_ajax_____parse_error_body() throws Exception {
        runTest("ajax: #11151 - jQuery.ajax() - parse error body");
    }

    /**
     * Test {612=[FF31], 613=[IE11], 614=[CHROME], 655=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___11426___jQuery_ajax_____loading_binary_data_shouldn_t_throw_an_exception_in_IE() throws Exception {
        runTest("ajax: #11426 - jQuery.ajax() - loading binary data shouldn't throw an exception in IE");
    }

    /**
     * Test {613=[FF31], 614=[IE11], 615=[CHROME], 656=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___11743___jQuery_ajax_____script__throws_exception() throws Exception {
        runTest("ajax: #11743 - jQuery.ajax() - script, throws exception");
    }

    /**
     * Test {614=[FF31], 615=[IE11], 616=[CHROME], 657=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___method_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - method set globally");
    }

    /**
     * Test {615=[FF31], 616=[IE11], 617=[CHROME], 658=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax___12004___jQuery_ajax_____method_is_an_alias_of_type___type_set_globally() throws Exception {
        runTest("ajax: #12004 - jQuery.ajax() - method is an alias of type - type set globally");
    }

    /**
     * Test {616=[FF31], 617=[IE11], 618=[CHROME], 659=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___13276___jQuery_ajax_____compatibility_between_XML_documents_from_ajax_requests_and_parsed_string() throws Exception {
        runTest("ajax: #13276 - jQuery.ajax() - compatibility between XML documents from ajax requests and parsed string");
    }

    /**
     * Test {617=[FF31], 618=[IE11], 619=[CHROME], 660=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax___13292___jQuery_ajax_____converter_is_bypassed_for_204_requests() throws Exception {
        runTest("ajax: #13292 - jQuery.ajax() - converter is bypassed for 204 requests");
    }

    /**
     * Test {618=[FF31], 619=[IE11], 620=[CHROME], 661=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax___13388___jQuery_ajax_____responseXML() throws Exception {
        runTest("ajax: #13388 - jQuery.ajax() - responseXML");
    }

    /**
     * Test {619=[FF31], 620=[IE11], 621=[CHROME], 662=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax___13922___jQuery_ajax_____converter_is_bypassed_for_HEAD_requests() throws Exception {
        runTest("ajax: #13922 - jQuery.ajax() - converter is bypassed for HEAD requests");
    }

    /**
     * Test {620=[FF31], 621=[IE11], 622=[CHROME], 664=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("1, 0, 1")
    public void ajax___13240___jQuery_ajax_____support_non_RFC2616_methods() throws Exception {
        runTest("ajax: #13240 - jQuery.ajax() - support non-RFC2616 methods");
    }

    /**
     * Test {621=[FF31], 622=[IE11], 623=[CHROME], 663=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___14379___jQuery_ajax___on_unload() throws Exception {
        runTest("ajax: #14379 - jQuery.ajax() on unload");
    }

    /**
     * Test {622=[FF31], 623=[IE11], 624=[CHROME], 672=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajaxPrefilter_____abort() throws Exception {
        runTest("ajax: jQuery.ajaxPrefilter() - abort");
    }

    /**
     * Test {623=[FF31], 624=[IE11], 625=[CHROME], 673=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajaxSetup__() throws Exception {
        runTest("ajax: jQuery.ajaxSetup()");
    }

    /**
     * Test {624=[FF31], 625=[IE11], 626=[CHROME], 674=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void ajax__jQuery_ajaxSetup___timeout__Number______with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) - with global timeout");
    }

    /**
     * Test {625=[FF31], 626=[IE11], 627=[CHROME], 675=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_ajaxSetup___timeout__Number____with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({ timeout: Number }) with localtimeout");
    }

    /**
     * Test {626=[FF31], 627=[IE11], 628=[CHROME], 676=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___11264___jQuery_domManip_____no_side_effect_because_of_ajaxSetup_or_global_events() throws Exception {
        runTest("ajax: #11264 - jQuery.domManip() - no side effect because of ajaxSetup or global events");
    }

    /**
     * Test {627=[FF31], 628=[IE11], 629=[CHROME], 678=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax___11402___jQuery_domManip_____script_in_comments_are_properly_evaluated() throws Exception {
        runTest("ajax: #11402 - jQuery.domManip() - script in comments are properly evaluated");
    }

    /**
     * Test {628=[FF31], 629=[IE11], 630=[CHROME], 679=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_get__String__Hash__Function_____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get( String, Hash, Function ) - parse xml and use text() on nodes");
    }

    /**
     * Test {629=[FF31], 630=[IE11], 631=[CHROME], 680=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___8277___jQuery_get__String__Function_____data_in_ajaxSettings() throws Exception {
        runTest("ajax: #8277 - jQuery.get( String, Function ) - data in ajaxSettings");
    }

    /**
     * Test {630=[FF31], 631=[IE11], 632=[CHROME], 681=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void ajax__jQuery_getJSON__String__Hash__Function_____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Hash, Function ) - JSON array");
    }

    /**
     * Test {631=[FF31], 632=[IE11], 633=[CHROME], 682=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object");
    }

    /**
     * Test {632=[FF31], 633=[IE11], 634=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_getJSON_____Using_Native_JSON() throws Exception {
        runTest("ajax: jQuery.getJSON() - Using Native JSON");
    }

    /**
     * Test {633=[FF31], 634=[IE11], 635=[CHROME], 683=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_getJSON__String__Function_____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON( String, Function ) - JSON object with absolute url to local content");
    }

    /**
     * Test {634=[FF31], 635=[IE11], 636=[CHROME], 684=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_getScript__String__Function_____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - with callback");
    }

    /**
     * Test {635=[FF31], 636=[IE11], 637=[CHROME], 685=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_getScript__String__Function_____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript( String, Function ) - no callback");
    }

    /**
     * Test {636=[FF31], 637=[IE11], 638=[CHROME], 686=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax___8082___jQuery_getScript__String__Function_____source_as_responseText() throws Exception {
        runTest("ajax: #8082 - jQuery.getScript( String, Function ) - source as responseText");
    }

    /**
     * Test {637=[FF31], 638=[IE11], 639=[CHROME], 687=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_fn_load__String__() throws Exception {
        runTest("ajax: jQuery.fn.load( String )");
    }

    /**
     * Test {638=[FF31], 639=[IE11], 640=[CHROME], 688=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void ajax__jQuery_fn_load_____404_error_callbacks() throws Exception {
        runTest("ajax: jQuery.fn.load() - 404 error callbacks");
    }

    /**
     * Test {639=[FF31], 640=[IE11], 641=[CHROME], 689=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_fn_load__String__null__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, null )");
    }

    /**
     * Test {640=[FF31], 641=[IE11], 642=[CHROME], 690=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_fn_load__String__undefined__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, undefined )");
    }

    /**
     * Test {641=[FF31], 642=[IE11], 643=[CHROME], 691=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_fn_load__URL_SELECTOR__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR )");
    }

    /**
     * Test {642=[FF31], 643=[IE11], 644=[CHROME], 692=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_fn_load__URL_SELECTOR_with_spaces__() throws Exception {
        runTest("ajax: jQuery.fn.load( URL_SELECTOR with spaces )");
    }

    /**
     * Test {643=[FF31], 644=[IE11], 645=[CHROME], 693=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_fn_load__String__Function_____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - simple: inject text into DOM");
    }

    /**
     * Test {644=[FF31], 645=[IE11], 646=[CHROME], 694=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void ajax__jQuery_fn_load__String__Function_____check_scripts() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check scripts");
    }

    /**
     * Test {645=[FF31], 646=[IE11], 647=[CHROME], 695=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_fn_load__String__Function_____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - check file with only a script tag");
    }

    /**
     * Test {646=[FF31], 647=[IE11], 648=[CHROME], 696=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_fn_load__String__Function_____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Function ) - dataFilter in ajaxSettings");
    }

    /**
     * Test {647=[FF31], 648=[IE11], 649=[CHROME], 697=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_fn_load__String__Object__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, Object, Function )");
    }

    /**
     * Test {648=[FF31], 649=[IE11], 650=[CHROME], 698=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void ajax__jQuery_fn_load__String__String__Function__() throws Exception {
        runTest("ajax: jQuery.fn.load( String, String, Function )");
    }

    /**
     * Test {649=[FF31], 650=[IE11], 651=[CHROME], 699=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void ajax__jQuery_fn_load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: jQuery.fn.load() - callbacks get the correct parameters");
    }

    /**
     * Test {650=[FF31], 651=[IE11], 652=[CHROME], 700=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___2046___jQuery_fn_load__String__Function___with_ajaxSetup_on_dataType_json() throws Exception {
        runTest("ajax: #2046 - jQuery.fn.load( String, Function ) with ajaxSetup on dataType json");
    }

    /**
     * Test {651=[FF31], 652=[IE11], 653=[CHROME], 701=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax___10524___jQuery_fn_load_____data_specified_in_ajaxSettings_is_merged_in() throws Exception {
        runTest("ajax: #10524 - jQuery.fn.load() - data specified in ajaxSettings is merged in");
    }

    /**
     * Test {652=[FF31], 653=[IE11], 654=[CHROME], 702=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void ajax__jQuery_post_____data() throws Exception {
        runTest("ajax: jQuery.post() - data");
    }

    /**
     * Test {653=[FF31], 654=[IE11], 655=[CHROME], 703=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void ajax__jQuery_post__String__Hash__Function_____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post( String, Hash, Function ) - simple with xml");
    }

    /**
     * Test {654=[FF31], 655=[IE11], 656=[CHROME], 705=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void ajax__jQuery_active() throws Exception {
        runTest("ajax: jQuery.active");
    }

    /**
     * Test {655=[FF31], 656=[IE11], 657=[CHROME], 706=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__sanity_check() throws Exception {
        runTest("effects: sanity check");
    }

    /**
     * Test {656=[FF31], 657=[IE11], 658=[CHROME], 707=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__show___basic() throws Exception {
        runTest("effects: show() basic");
    }

    /**
     * Test {657=[FF31], 658=[IE11], 659=[CHROME], 708=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 27, 27",
        FF31 = "0, 27, 27",
        FF38 = "27",
        IE11 = "0, 27, 27")
    public void effects__show__() throws Exception {
        runTest("effects: show()");
    }

    /**
     * Test {658=[FF31], 659=[IE11], 660=[CHROME], 709=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 15, 15",
        FF31 = "0, 15, 15",
        FF38 = "15",
        IE11 = "0, 15, 15")
    public void effects__show_Number____other_displays() throws Exception {
        runTest("effects: show(Number) - other displays");
    }

    /**
     * Test {659=[FF31], 660=[IE11], 661=[CHROME], 710=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects__Persist_correct_display_value() throws Exception {
        runTest("effects: Persist correct display value");
    }

    /**
     * Test {660=[FF31], 661=[IE11], 662=[CHROME], 711=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__animate_Hash__Object__Function_() throws Exception {
        runTest("effects: animate(Hash, Object, Function)");
    }

    /**
     * Test {661=[FF31], 662=[IE11], 663=[CHROME], 712=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void effects__animate_relative_values() throws Exception {
        runTest("effects: animate relative values");
    }

    /**
     * Test {662=[FF31], 663=[IE11], 664=[CHROME], 713=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__animate_negative_height() throws Exception {
        runTest("effects: animate negative height");
    }

    /**
     * Test {663=[FF31], 664=[IE11], 665=[CHROME], 714=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__animate_negative_margin() throws Exception {
        runTest("effects: animate negative margin");
    }

    /**
     * Test {664=[FF31], 665=[IE11], 666=[CHROME], 715=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__animate_negative_margin_with_px() throws Exception {
        runTest("effects: animate negative margin with px");
    }

    /**
     * Test {665=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "4")
    public void ajax___14683___jQuery_ajax_____Exceptions_thrown_synchronously_by_xhr_send_should_be_caught() throws Exception {
        runTest("ajax: #14683 - jQuery.ajax() - Exceptions thrown synchronously by xhr.send should be caught");
    }

    /**
     * Test {665=[FF31], 666=[IE11], 667=[CHROME], 716=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void effects__animate_negative_padding() throws Exception {
        runTest("effects: animate negative padding");
    }

    /**
     * Test {666=[FF31], 667=[IE11], 668=[CHROME], 717=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects__animate_block_as_inline_width_height() throws Exception {
        runTest("effects: animate block as inline width/height");
    }

    /**
     * Test {667=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void ajax__gh_2587___when_content_type_not_xml__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not xml, but looks like one");
    }

    /**
     * Test {667=[FF31], 668=[IE11], 669=[CHROME], 718=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects__animate_native_inline_width_height() throws Exception {
        runTest("effects: animate native inline width/height");
    }

    /**
     * Test {668=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void ajax__gh_2587___when_content_type_not_json__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not json, but looks like one");
    }

    /**
     * Test {668=[FF31], 669=[IE11], 670=[CHROME], 719=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects__animate_block_width_height() throws Exception {
        runTest("effects: animate block width/height");
    }

    /**
     * Test {669=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void ajax__gh_2587___when_content_type_not_html__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not html, but looks like one");
    }

    /**
     * Test {669=[FF31], 670=[IE11], 671=[CHROME], 720=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__animate_table_width_height() throws Exception {
        runTest("effects: animate table width/height");
    }

    /**
     * Test {670=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void ajax__gh_2587___when_content_type_not_javascript__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not javascript, but looks like one");
    }

    /**
     * Test {670=[FF31], 671=[IE11], 672=[CHROME], 721=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void effects__animate_table_row_width_height() throws Exception {
        runTest("effects: animate table-row width/height");
    }

    /**
     * Test {671=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void ajax__gh_2587___when_content_type_not_ecmascript__but_looks_like_one() throws Exception {
        runTest("ajax: gh-2587 - when content-type not ecmascript, but looks like one");
    }

    /**
     * Test {671=[FF31], 672=[IE11], 673=[CHROME], 722=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void effects__animate_table_cell_width_height() throws Exception {
        runTest("effects: animate table-cell width/height");
    }

    /**
     * Test {672=[FF31], 673=[IE11], 674=[CHROME], 723=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__animate_percentage____on_width_height() throws Exception {
        runTest("effects: animate percentage(%) on width/height");
    }

    /**
     * Test {673=[FF31], 674=[IE11], 675=[CHROME], 724=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__animate_resets_overflow_x_and_overflow_y_when_finished() throws Exception {
        runTest("effects: animate resets overflow-x and overflow-y when finished");
    }

    /**
     * Test {674=[FF31], 675=[IE11], 676=[CHROME], 725=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__animate_option___queue__false__() throws Exception {
        runTest("effects: animate option { queue: false }");
    }

    /**
     * Test {675=[FF31], 676=[IE11], 677=[CHROME], 726=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__animate_option___queue__true__() throws Exception {
        runTest("effects: animate option { queue: true }");
    }

    /**
     * Test {676=[FF31], 677=[IE11], 678=[CHROME], 727=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__animate_option___queue___name___() throws Exception {
        runTest("effects: animate option { queue: 'name' }");
    }

    /**
     * Test {677=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void ajax__jQuery_load_____always_use_GET_method_even_if_it_overrided_through_ajaxSetup___11264_() throws Exception {
        runTest("ajax: jQuery#load() - always use GET method even if it overrided through ajaxSetup (#11264)");
    }

    /**
     * Test {677=[FF31], 678=[IE11], 679=[CHROME], 728=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__animate_with_no_properties() throws Exception {
        runTest("effects: animate with no properties");
    }

    /**
     * Test {678=[FF31], 679=[IE11], 680=[CHROME], 729=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 11, 11",
        FF31 = "0, 11, 11",
        FF38 = "11",
        IE11 = "0, 11, 11")
    public void effects__animate_duration_0() throws Exception {
        runTest("effects: animate duration 0");
    }

    /**
     * Test {679=[FF31], 680=[IE11], 681=[CHROME], 730=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__animate_hyphenated_properties() throws Exception {
        runTest("effects: animate hyphenated properties");
    }

    /**
     * Test {680=[FF31], 681=[IE11], 682=[CHROME], 731=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__animate_non_element() throws Exception {
        runTest("effects: animate non-element");
    }

    /**
     * Test {681=[FF31], 682=[IE11], 683=[CHROME], 732=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__stop__() throws Exception {
        runTest("effects: stop()");
    }

    /**
     * Test {682=[FF31], 683=[IE11], 684=[CHROME], 733=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__stop_____several_in_queue() throws Exception {
        runTest("effects: stop() - several in queue");
    }

    /**
     * Test {683=[FF31], 684=[IE11], 685=[CHROME], 734=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__stop_clearQueue_() throws Exception {
        runTest("effects: stop(clearQueue)");
    }

    /**
     * Test {684=[FF31], 685=[IE11], 686=[CHROME], 735=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__stop_clearQueue__gotoEnd_() throws Exception {
        runTest("effects: stop(clearQueue, gotoEnd)");
    }

    /**
     * Test {685=[FF31], 686=[IE11], 687=[CHROME], 736=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects__stop__queue_______________Stop_single_queues() throws Exception {
        runTest("effects: stop( queue, ..., ... ) - Stop single queues");
    }

    /**
     * Test {686=[FF31], 687=[IE11], 688=[CHROME], 737=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__toggle__() throws Exception {
        runTest("effects: toggle()");
    }

    /**
     * Test {687=[FF31], 688=[IE11], 689=[CHROME], 738=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF31 = "0, 7, 7",
        FF38 = "7",
        IE11 = "0, 7, 7")
    public void effects__jQuery_fx_prototype_cur______1_8_Back_Compat() throws Exception {
        runTest("effects: jQuery.fx.prototype.cur() - <1.8 Back Compat");
    }

    /**
     * Test {688=[FF31], 689=[IE11], 690=[CHROME], 739=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__Overflow_and_Display() throws Exception {
        runTest("effects: Overflow and Display");
    }

    /**
     * Test {689=[FF31], 690=[IE11], 691=[CHROME], 740=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_Auto_to_0() throws Exception {
        runTest("effects: CSS Auto to 0");
    }

    /**
     * Test {690=[FF31], 691=[IE11], 692=[CHROME], 741=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_Auto_to_50() throws Exception {
        runTest("effects: CSS Auto to 50");
    }

    /**
     * Test {691=[FF31], 692=[IE11], 693=[CHROME], 742=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_Auto_to_100() throws Exception {
        runTest("effects: CSS Auto to 100");
    }

    /**
     * Test {692=[FF31], 693=[IE11], 694=[CHROME], 743=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void effects__CSS_Auto_to_show() throws Exception {
        runTest("effects: CSS Auto to show");
    }

    /**
     * Test {693=[FF31], 694=[IE11], 695=[CHROME], 744=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__CSS_Auto_to_hide() throws Exception {
        runTest("effects: CSS Auto to hide");
    }

    /**
     * Test {694=[FF31], 695=[IE11], 696=[CHROME], 745=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_Auto_to_0() throws Exception {
        runTest("effects: JS Auto to 0");
    }

    /**
     * Test {695=[FF31], 696=[IE11], 697=[CHROME], 746=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_Auto_to_50() throws Exception {
        runTest("effects: JS Auto to 50");
    }

    /**
     * Test {696=[FF31], 697=[IE11], 698=[CHROME], 747=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_Auto_to_100() throws Exception {
        runTest("effects: JS Auto to 100");
    }

    /**
     * Test {697=[FF31], 698=[IE11], 699=[CHROME], 748=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void effects__JS_Auto_to_show() throws Exception {
        runTest("effects: JS Auto to show");
    }

    /**
     * Test {698=[FF31], 699=[IE11], 700=[CHROME], 749=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__JS_Auto_to_hide() throws Exception {
        runTest("effects: JS Auto to hide");
    }

    /**
     * Test {699=[FF31], 700=[IE11], 701=[CHROME], 750=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_100_to_0() throws Exception {
        runTest("effects: CSS 100 to 0");
    }

    /**
     * Test {700=[FF31], 701=[IE11], 702=[CHROME], 751=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_100_to_50() throws Exception {
        runTest("effects: CSS 100 to 50");
    }

    /**
     * Test {701=[FF31], 702=[IE11], 703=[CHROME], 752=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_100_to_100() throws Exception {
        runTest("effects: CSS 100 to 100");
    }

    /**
     * Test {702=[FF31], 703=[IE11], 704=[CHROME], 753=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__CSS_100_to_show() throws Exception {
        runTest("effects: CSS 100 to show");
    }

    /**
     * Test {703=[FF31], 704=[IE11], 705=[CHROME], 754=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__CSS_100_to_hide() throws Exception {
        runTest("effects: CSS 100 to hide");
    }

    /**
     * Test {704=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void ajax__jQuery_get_post___options_____simple_with_xml() throws Exception {
        runTest("ajax: jQuery[get|post]( options ) - simple with xml");
    }

    /**
     * Test {704=[FF31], 705=[IE11], 706=[CHROME], 755=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_100_to_0() throws Exception {
        runTest("effects: JS 100 to 0");
    }

    /**
     * Test {705=[FF31], 706=[IE11], 707=[CHROME], 756=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_100_to_50() throws Exception {
        runTest("effects: JS 100 to 50");
    }

    /**
     * Test {706=[FF31], 707=[IE11], 708=[CHROME], 757=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_100_to_100() throws Exception {
        runTest("effects: JS 100 to 100");
    }

    /**
     * Test {707=[FF31], 708=[IE11], 709=[CHROME], 758=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__JS_100_to_show() throws Exception {
        runTest("effects: JS 100 to show");
    }

    /**
     * Test {708=[FF31], 709=[IE11], 710=[CHROME], 759=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__JS_100_to_hide() throws Exception {
        runTest("effects: JS 100 to hide");
    }

    /**
     * Test {709=[FF31], 710=[IE11], 711=[CHROME], 760=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_50_to_0() throws Exception {
        runTest("effects: CSS 50 to 0");
    }

    /**
     * Test {710=[FF31], 711=[IE11], 712=[CHROME], 761=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_50_to_50() throws Exception {
        runTest("effects: CSS 50 to 50");
    }

    /**
     * Test {711=[FF31], 712=[IE11], 713=[CHROME], 762=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_50_to_100() throws Exception {
        runTest("effects: CSS 50 to 100");
    }

    /**
     * Test {712=[FF31], 713=[IE11], 714=[CHROME], 763=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__CSS_50_to_show() throws Exception {
        runTest("effects: CSS 50 to show");
    }

    /**
     * Test {713=[FF31], 714=[IE11], 715=[CHROME], 764=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__CSS_50_to_hide() throws Exception {
        runTest("effects: CSS 50 to hide");
    }

    /**
     * Test {714=[FF31], 715=[IE11], 716=[CHROME], 765=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_50_to_0() throws Exception {
        runTest("effects: JS 50 to 0");
    }

    /**
     * Test {715=[FF31], 716=[IE11], 717=[CHROME], 766=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_50_to_50() throws Exception {
        runTest("effects: JS 50 to 50");
    }

    /**
     * Test {716=[FF31], 717=[IE11], 718=[CHROME], 767=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_50_to_100() throws Exception {
        runTest("effects: JS 50 to 100");
    }

    /**
     * Test {717=[FF31], 718=[IE11], 719=[CHROME], 768=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__JS_50_to_show() throws Exception {
        runTest("effects: JS 50 to show");
    }

    /**
     * Test {718=[FF31], 719=[IE11], 720=[CHROME], 769=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__JS_50_to_hide() throws Exception {
        runTest("effects: JS 50 to hide");
    }

    /**
     * Test {719=[FF31], 720=[IE11], 721=[CHROME], 770=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_0_to_0() throws Exception {
        runTest("effects: CSS 0 to 0");
    }

    /**
     * Test {720=[FF31], 721=[IE11], 722=[CHROME], 771=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_0_to_50() throws Exception {
        runTest("effects: CSS 0 to 50");
    }

    /**
     * Test {721=[FF31], 722=[IE11], 723=[CHROME], 772=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__CSS_0_to_100() throws Exception {
        runTest("effects: CSS 0 to 100");
    }

    /**
     * Test {722=[FF31], 723=[IE11], 724=[CHROME], 773=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__CSS_0_to_show() throws Exception {
        runTest("effects: CSS 0 to show");
    }

    /**
     * Test {723=[FF31], 724=[IE11], 725=[CHROME], 774=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__CSS_0_to_hide() throws Exception {
        runTest("effects: CSS 0 to hide");
    }

    /**
     * Test {724=[FF31], 725=[IE11], 726=[CHROME], 775=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_0_to_0() throws Exception {
        runTest("effects: JS 0 to 0");
    }

    /**
     * Test {725=[FF31], 726=[IE11], 727=[CHROME], 776=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_0_to_50() throws Exception {
        runTest("effects: JS 0 to 50");
    }

    /**
     * Test {726=[FF31], 727=[IE11], 728=[CHROME], 777=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__JS_0_to_100() throws Exception {
        runTest("effects: JS 0 to 100");
    }

    /**
     * Test {727=[FF31], 728=[IE11], 729=[CHROME], 778=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__JS_0_to_show() throws Exception {
        runTest("effects: JS 0 to show");
    }

    /**
     * Test {728=[FF31], 729=[IE11], 730=[CHROME], 779=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__JS_0_to_hide() throws Exception {
        runTest("effects: JS 0 to hide");
    }

    /**
     * Test {729=[FF31], 730=[IE11], 731=[CHROME], 780=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 16, 16",
        FF31 = "0, 16, 16",
        FF38 = "16",
        IE11 = "0, 16, 16")
    public void effects__Effects_chaining() throws Exception {
        runTest("effects: Effects chaining");
    }

    /**
     * Test {730=[FF31], 731=[IE11], 732=[CHROME], 781=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__jQuery_show__fast___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("effects: jQuery.show('fast') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {731=[FF31], 732=[IE11], 733=[CHROME], 782=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 24, 24",
        FF31 = "0, 24, 24",
        FF38 = "24",
        IE11 = "0, 24, 24")
    public void effects__interrupt_toggle() throws Exception {
        runTest("effects: interrupt toggle");
    }

    /**
     * Test {732=[FF31], 733=[IE11], 734=[CHROME], 783=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__animate_with_per_property_easing() throws Exception {
        runTest("effects: animate with per-property easing");
    }

    /**
     * Test {733=[FF31], 734=[IE11], 735=[CHROME], 784=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 11, 11",
        FF31 = "0, 11, 11",
        FF38 = "11",
        IE11 = "0, 11, 11")
    public void effects__animate_with_CSS_shorthand_properties() throws Exception {
        runTest("effects: animate with CSS shorthand properties");
    }

    /**
     * Test {734=[FF31], 735=[IE11], 736=[CHROME], 785=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects__hide_hidden_elements__with_animation__bug__7141_() throws Exception {
        runTest("effects: hide hidden elements, with animation (bug #7141)");
    }

    /**
     * Test {735=[FF31], 736=[IE11], 737=[CHROME], 786=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__animate_unit_less_properties___4966_() throws Exception {
        runTest("effects: animate unit-less properties (#4966)");
    }

    /**
     * Test {736=[FF31], 737=[IE11], 738=[CHROME], 787=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects__animate_properties_missing_px_w__opacity_as_last___9074_() throws Exception {
        runTest("effects: animate properties missing px w/ opacity as last (#9074)");
    }

    /**
     * Test {737=[FF31], 738=[IE11], 739=[CHROME], 788=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__callbacks_should_fire_in_correct_order___9100_() throws Exception {
        runTest("effects: callbacks should fire in correct order (#9100)");
    }

    /**
     * Test {738=[FF31], 739=[IE11], 740=[CHROME], 789=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__callbacks_that_throw_exceptions_will_be_removed___5684_() throws Exception {
        runTest("effects: callbacks that throw exceptions will be removed (#5684)");
    }

    /**
     * Test {739=[FF31], 740=[IE11], 741=[CHROME], 790=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__animate_will_scale_margin_properties_individually() throws Exception {
        runTest("effects: animate will scale margin properties individually");
    }

    /**
     * Test {740=[FF31], 741=[IE11], 742=[CHROME], 791=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("effects: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {741=[FF31], 742=[IE11], 743=[CHROME], 792=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 12, 12",
        FF31 = "0, 12, 12",
        FF38 = "12",
        IE11 = "0, 12, 12")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void effects__line_height_animates_correctly___13855_() throws Exception {
        runTest("effects: line-height animates correctly (#13855)");
    }

    /**
     * Test {742=[FF31], 743=[IE11], 744=[CHROME], 793=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__jQuery_Animation__object__props__opts__() throws Exception {
        runTest("effects: jQuery.Animation( object, props, opts )");
    }

    /**
     * Test {743=[FF31], 744=[IE11], 745=[CHROME], 794=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__Animate_Option__step__function__percent__tween__() throws Exception {
        runTest("effects: Animate Option: step: function( percent, tween )");
    }

    /**
     * Test {744=[FF31], 745=[IE11], 746=[CHROME], 795=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__Animate_callbacks_have_correct_context() throws Exception {
        runTest("effects: Animate callbacks have correct context");
    }

    /**
     * Test {745=[FF31], 746=[IE11], 747=[CHROME], 796=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__User_supplied_callback_called_after_show_when_fx_off___8892_() throws Exception {
        runTest("effects: User supplied callback called after show when fx off (#8892)");
    }

    /**
     * Test {746=[FF31], 747=[IE11], 748=[CHROME], 797=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 18, 18",
        FF31 = "0, 18, 18",
        FF38 = "18",
        IE11 = "0, 18, 18")
    public void effects__animate_should_set_display_for_disconnected_nodes() throws Exception {
        runTest("effects: animate should set display for disconnected nodes");
    }

    /**
     * Test {747=[FF31], 748=[IE11], 749=[CHROME], 798=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__Animation_callback_should_not_show_animated_element_as__animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as :animated (#7157)");
    }

    /**
     * Test {748=[FF31], 749=[IE11], 750=[CHROME], 799=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__Initial_step_callback_should_show_element_as__animated___14623_() throws Exception {
        runTest("effects: Initial step callback should show element as :animated (#14623)");
    }

    /**
     * Test {749=[FF31], 750=[IE11], 751=[CHROME], 800=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects__hide_called_on_element_within_hidden_parent_should_set_display_to_none___10045_() throws Exception {
        runTest("effects: hide called on element within hidden parent should set display to none (#10045)");
    }

    /**
     * Test {750=[FF31], 751=[IE11], 752=[CHROME], 801=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF31 = "0, 5, 5",
        FF38 = "5",
        IE11 = "0, 5, 5")
    public void effects__hide__fadeOut_and_slideUp_called_on_element_width_height_and_width___0_should_set_display_to_none() throws Exception {
        runTest("effects: hide, fadeOut and slideUp called on element width height and width = 0 should set display to none");
    }

    /**
     * Test {751=[FF31], 752=[IE11], 753=[CHROME], 802=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__hide_should_not_leave_hidden_inline_elements_visible___14848_() throws Exception {
        runTest("effects: hide should not leave hidden inline elements visible (#14848)");
    }

    /**
     * Test {752=[FF31], 753=[IE11], 754=[CHROME], 803=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void effects__Handle_queue_false_promises() throws Exception {
        runTest("effects: Handle queue:false promises");
    }

    /**
     * Test {753=[FF31], 754=[IE11], 755=[CHROME], 804=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__multiple_unqueued_and_promise() throws Exception {
        runTest("effects: multiple unqueued and promise");
    }

    /**
     * Test {754=[FF31], 755=[IE11], 756=[CHROME], 805=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void effects__animate_does_not_change_start_value_for_non_px_animation___7109_() throws Exception {
        runTest("effects: animate does not change start value for non-px animation (#7109)");
    }

    /**
     * Test {755=[FF31], 756=[IE11], 757=[CHROME], 806=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    @NotYetImplemented({ CHROME, IE11 })
    public void effects__non_px_animation_handles_non_numeric_start___11971_() throws Exception {
        runTest("effects: non-px animation handles non-numeric start (#11971)");
    }

    /**
     * Test {756=[FF31], 757=[IE11], 758=[CHROME], 807=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 15, 15",
        FF31 = "0, 15, 15",
        FF38 = "16",
        IE11 = "0, 15, 15")
    public void effects__Animation_callbacks___11797_() throws Exception {
        runTest("effects: Animation callbacks (#11797)");
    }

    /**
     * Test {757=[FF31], 758=[IE11], 759=[CHROME], 808=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void effects__Animate_properly_sets_overflow_hidden_when_animating_width_height___12117_() throws Exception {
        runTest("effects: Animate properly sets overflow hidden when animating width/height (#12117)");
    }

    /**
     * Test {758=[FF31], 759=[IE11], 760=[CHROME], 809=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects__Each_tick_of_the_timer_loop_uses_a_fresh_time___12837_() throws Exception {
        runTest("effects: Each tick of the timer loop uses a fresh time (#12837)");
    }

    /**
     * Test {759=[FF31], 760=[IE11], 761=[CHROME], 810=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void effects__Animations_with_0_duration_don_t_ease___12273_() throws Exception {
        runTest("effects: Animations with 0 duration don't ease (#12273)");
    }

    /**
     * Test {760=[FF31], 761=[IE11], 762=[CHROME], 811=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__toggle_state_tests__toggle___8685_() throws Exception {
        runTest("effects: toggle state tests: toggle (#8685)");
    }

    /**
     * Test {761=[FF31], 762=[IE11], 763=[CHROME], 812=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__toggle_state_tests__slideToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: slideToggle (#8685)");
    }

    /**
     * Test {762=[FF31], 763=[IE11], 764=[CHROME], 813=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void effects__toggle_state_tests__fadeToggle___8685_() throws Exception {
        runTest("effects: toggle state tests: fadeToggle (#8685)");
    }

    /**
     * Test {763=[FF31], 764=[IE11], 765=[CHROME], 814=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects__jQuery_fx_start___jQuery_fx_stop_hook_points() throws Exception {
        runTest("effects: jQuery.fx.start & jQuery.fx.stop hook points");
    }

    /**
     * Test {764=[FF31], 765=[IE11], 766=[CHROME], 815=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 11, 11",
        FF31 = "0, 11, 11",
        FF38 = "11",
        IE11 = "0, 11, 11")
    public void effects___finish___completes_all_queued_animations() throws Exception {
        runTest("effects: .finish() completes all queued animations");
    }

    /**
     * Test {765=[FF31], 766=[IE11], 767=[CHROME], 816=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 10, 10",
        FF31 = "0, 10, 10",
        FF38 = "10",
        IE11 = "0, 10, 10")
    public void effects___finish__false_____unqueued_animations() throws Exception {
        runTest("effects: .finish( false ) - unqueued animations");
    }

    /**
     * Test {766=[FF31], 767=[IE11], 768=[CHROME], 817=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 11, 11",
        FF31 = "0, 11, 11",
        FF38 = "11",
        IE11 = "0, 11, 11")
    public void effects___finish___custom______custom_queue_animations() throws Exception {
        runTest("effects: .finish( \"custom\" ) - custom queue animations");
    }

    /**
     * Test {767=[FF31], 768=[IE11], 769=[CHROME], 818=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void effects___finish___calls_finish_of_custom_queue_functions() throws Exception {
        runTest("effects: .finish() calls finish of custom queue functions");
    }

    /**
     * Test {768=[FF31], 769=[IE11], 770=[CHROME], 819=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void effects___finish___is_applied_correctly_when_multiple_elements_were_animated___13937_() throws Exception {
        runTest("effects: .finish() is applied correctly when multiple elements were animated (#13937)");
    }

    /**
     * Test {769=[FF31], 770=[IE11], 771=[CHROME], 820=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__slideDown___after_stop_____13483_() throws Exception {
        runTest("effects: slideDown() after stop() (#13483)");
    }

    /**
     * Test {770=[FF31], 771=[IE11], 772=[CHROME], 821=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void effects__Respect_display_value_on_inline_elements___14824_() throws Exception {
        runTest("effects: Respect display value on inline elements (#14824)");
    }

    /**
     * Test {771=[FF31], 772=[IE11], 773=[CHROME], 822=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void offset__empty_set() throws Exception {
        runTest("offset: empty set");
    }

    /**
     * Test {772=[FF31], 773=[IE11], 774=[CHROME], 823=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void offset__object_without_getBoundingClientRect() throws Exception {
        runTest("offset: object without getBoundingClientRect");
    }

    /**
     * Test {773=[FF31], 774=[IE11], 775=[CHROME]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        IE11 = "0, 2, 2")
    public void offset__disconnected_node() throws Exception {
        runTest("offset: disconnected node");
    }

    /**
     * Test {775=[FF31], 776=[IE11], 777=[CHROME], 826=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 178, 178",
        FF31 = "0, 178, 178",
        FF38 = "178",
        IE11 = "0, 178, 178")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void offset__absolute() throws Exception {
        runTest("offset: absolute");
    }

    /**
     * Test {776=[FF31], 777=[IE11], 778=[CHROME], 827=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 60, 60",
        FF31 = "0, 60, 60",
        FF38 = "60",
        IE11 = "0, 60, 60")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void offset__relative() throws Exception {
        runTest("offset: relative");
    }

    /**
     * Test {777=[FF31], 778=[IE11], 779=[CHROME], 828=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 80, 80",
        FF31 = "0, 80, 80",
        FF38 = "80",
        IE11 = "0, 80, 80")
    @NotYetImplemented(IE11)
    public void offset__static() throws Exception {
        runTest("offset: static");
    }

    /**
     * Test {778=[FF31], 779=[IE11], 780=[CHROME], 829=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 34, 34",
        FF31 = "0, 34, 34",
        FF38 = "34",
        IE11 = "0, 34, 34")
    public void offset__fixed() throws Exception {
        runTest("offset: fixed");
    }

    /**
     * Test {779=[FF31], 780=[IE11], 781=[CHROME], 830=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void offset__table() throws Exception {
        runTest("offset: table");
    }

    /**
     * Test {780=[FF31], 781=[IE11], 782=[CHROME], 831=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 24, 24",
        FF31 = "0, 24, 24",
        FF38 = "28",
        IE11 = "0, 24, 24")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void offset__scroll() throws Exception {
        runTest("offset: scroll");
    }

    /**
     * Test {781=[FF31], 782=[IE11], 783=[CHROME], 832=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void offset__body() throws Exception {
        runTest("offset: body");
    }

    /**
     * Test {782=[FF31], 783=[IE11], 784=[CHROME], 833=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 3, 3",
        FF31 = "0, 3, 3",
        FF38 = "3",
        IE11 = "0, 3, 3")
    public void offset__chaining() throws Exception {
        runTest("offset: chaining");
    }

    /**
     * Test {783=[FF31], 784=[IE11], 785=[CHROME], 834=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 13, 13",
        FF31 = "0, 13, 13",
        FF38 = "13",
        IE11 = "0, 13, 13")
    public void offset__offsetParent() throws Exception {
        runTest("offset: offsetParent");
    }

    /**
     * Test {784=[FF31], 785=[IE11], 786=[CHROME], 835=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void offset__fractions__see__7730_and__7885_() throws Exception {
        runTest("offset: fractions (see #7730 and #7885)");
    }

    /**
     * Test {785=[FF31], 786=[IE11], 787=[CHROME], 837=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void dimensions__width__() throws Exception {
        runTest("dimensions: width()");
    }

    /**
     * Test {786=[FF31], 787=[IE11], 788=[CHROME], 838=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void dimensions__width_Function_() throws Exception {
        runTest("dimensions: width(Function)");
    }

    /**
     * Test {787=[FF31], 788=[IE11], 789=[CHROME], 839=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void dimensions__width_Function_args__() throws Exception {
        runTest("dimensions: width(Function(args))");
    }

    /**
     * Test {788=[FF31], 789=[IE11], 790=[CHROME], 840=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void dimensions__height__() throws Exception {
        runTest("dimensions: height()");
    }

    /**
     * Test {789=[FF31], 790=[IE11], 791=[CHROME], 841=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 9, 9",
        FF31 = "0, 9, 9",
        FF38 = "9",
        IE11 = "0, 9, 9")
    public void dimensions__height_Function_() throws Exception {
        runTest("dimensions: height(Function)");
    }

    /**
     * Test {790=[FF31], 791=[IE11], 792=[CHROME], 842=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void dimensions__height_Function_args__() throws Exception {
        runTest("dimensions: height(Function(args))");
    }

    /**
     * Test {791=[FF31], 792=[IE11], 793=[CHROME], 843=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void dimensions__innerWidth__() throws Exception {
        runTest("dimensions: innerWidth()");
    }

    /**
     * Test {792=[FF31], 793=[IE11], 794=[CHROME], 844=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 6, 6",
        FF31 = "0, 6, 6",
        FF38 = "6",
        IE11 = "0, 6, 6")
    public void dimensions__innerHeight__() throws Exception {
        runTest("dimensions: innerHeight()");
    }

    /**
     * Test {793=[FF31], 794=[IE11], 795=[CHROME], 845=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 11, 11",
        FF31 = "0, 11, 11",
        FF38 = "11",
        IE11 = "0, 11, 11")
    public void dimensions__outerWidth__() throws Exception {
        runTest("dimensions: outerWidth()");
    }

    /**
     * Test {794=[FF31], 795=[IE11], 796=[CHROME], 846=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 16, 16",
        FF31 = "0, 16, 16",
        FF38 = "16",
        IE11 = "0, 16, 16")
    public void dimensions__child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__9441__9300() throws Exception {
        runTest("dimensions: child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #9441 #9300");
    }

    /**
     * Test {795=[FF31], 796=[IE11], 797=[CHROME], 847=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    public void dimensions__getting_dimensions_shouldn_t_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldn't modify runtimeStyle see #9233");
    }

    /**
     * Test {796=[FF31], 797=[IE11], 798=[CHROME], 848=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void dimensions__table_dimensions() throws Exception {
        runTest("dimensions: table dimensions");
    }

    /**
     * Test {797=[FF31], 798=[IE11], 799=[CHROME], 849=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 16, 16",
        FF31 = "0, 16, 16",
        FF38 = "16",
        IE11 = "0, 16, 16")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void dimensions__box_sizing_border_box_child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__10413() throws Exception {
        runTest("dimensions: box-sizing:border-box child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #10413");
    }

    /**
     * Test {798=[FF31], 799=[IE11], 800=[CHROME], 850=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 11, 11",
        FF31 = "0, 11, 11",
        FF38 = "11",
        IE11 = "0, 11, 11")
    public void dimensions__outerHeight__() throws Exception {
        runTest("dimensions: outerHeight()");
    }

    /**
     * Test {799=[FF31], 800=[IE11], 801=[CHROME], 851=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF31 = "0, 4, 4",
        FF38 = "4",
        IE11 = "0, 4, 4")
    public void dimensions__passing_undefined_is_a_setter__5571() throws Exception {
        runTest("dimensions: passing undefined is a setter #5571");
    }

    /**
     * Test {800=[FF31], 801=[IE11], 802=[CHROME], 852=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 8, 8",
        FF31 = "0, 8, 8",
        FF38 = "8",
        IE11 = "0, 8, 8")
    public void dimensions__getters_on_non_elements_should_return_null() throws Exception {
        runTest("dimensions: getters on non elements should return null");
    }

    /**
     * Test {801=[FF31], 802=[IE11], 803=[CHROME], 853=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 20, 20",
        FF31 = "0, 20, 20",
        FF38 = "20",
        IE11 = "0, 20, 20")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void dimensions__setters_with_and_without_box_sizing_border_box() throws Exception {
        runTest("dimensions: setters with and without box-sizing:border-box");
    }

    /**
     * Test {802=[FF31], 803=[IE11], 804=[CHROME], 854=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 1, 1",
        FF31 = "0, 1, 1",
        FF38 = "1",
        IE11 = "0, 1, 1")
    @NotYetImplemented({ CHROME, FF, IE11 })
    public void dimensions__window_vs__small_document() throws Exception {
        runTest("dimensions: window vs. small document");
    }

    /**
     * Test {803=[FF31], 804=[IE11], 805=[CHROME], 855=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF31 = "0, 2, 2",
        FF38 = "2",
        IE11 = "0, 2, 2")
    public void dimensions__window_vs__large_document() throws Exception {
        runTest("dimensions: window vs. large document");
    }

    /**
     * Test {824=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void offset__disconnected_element() throws Exception {
        runTest("offset: disconnected element");
    }

    /**
     * Test {836=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "2")
    public void offset__iframe_scrollTop_Left__see_gh_1945_() throws Exception {
        runTest("offset: iframe scrollTop/Left (see gh-1945)");
    }

    /**
     * Test {856=[FF38]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF38 = "1")
    public void dimensions__allow_modification_of_coordinates_argument__gh_1848_() throws Exception {
        runTest("dimensions: allow modification of coordinates argument (gh-1848)");
    }
}
