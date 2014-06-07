/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;
import static org.junit.Assert.fail;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for compatibility with web server loading of
 * version 1.8.2 of the <a href="http://jquery.com/">jQuery</a> JavaScript library.
 *
 * All test method inside this class are generated based. Please have a look
 * at {@link com.gargoylesoftware.htmlunit.source.JQueryExtractor}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class JQuery182Test extends WebDriverTestCase {

    private static Server SERVER_;

    /**
     * Returns the jQuery version being tested.
     * @return the jQuery version being tested
     */
    protected static String getVersion() {
        return "1.8.2";
    }

    /**
     * Overridden so it doesn't check for background threads.
     *
     * {@inheritDoc}
     */
    @After
    @Override
    public void releaseResources() {
        // nothing
    }

    /**
     * Runs the specified test.
     * @param testName the test name
     * @throws Exception if an error occurs
     */
    protected void runTest(final String testName) throws Exception {
        final int testNumber = readTestNumber(testName);
        if (testNumber == -1) {
            Assert.assertEquals("Test number not found for: " + testName, 0, getExpectedAlerts().length);
            return;
        }
        final long runTime = 60 * DEFAULT_WAIT_TIME;
        final long endTime = System.currentTimeMillis() + runTime;

        try {
            final WebDriver webdriver = getWebDriver();
            webdriver.get("http://localhost:" + PORT + "/jquery/test/index.html?testNumber="
                    + testNumber);

            final WebElement status = webdriver.findElement(By.id("qunit-testresult"));
            while (!status.getText().startsWith("Tests completed")) {
                Thread.sleep(100);

                if (System.currentTimeMillis() > endTime) {
                    fail("Test #" + testNumber + " runs too long (longer than " + runTime / 1000 + "s)");
                }
            }

            final WebElement output = webdriver.findElement(By.id("qunit-test-output0"));
            String result = output.getText();
            result = result.substring(0, result.indexOf("Rerun")).trim();
            final String expected = testName + " (" + getExpectedAlerts()[0] + ")";
            if (!expected.contains(result)) {
                System.out.println("-> " + webdriver.findElement(By.id("qunit-tests")).getText());
                fail(new ComparisonFailure("", expected, result).getMessage());
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            Throwable t = e;
            while ((t = t.getCause()) != null) {
                t.printStackTrace();
            }
            throw e;
        }
    }

    private int readTestNumber(final String testName) throws Exception {
        final String testResults = loadExpectation("/libraries/jQuery/" + getVersion() + "/expectations/results", ".txt");
        final String[] lines = testResults.split("\n");
        for (int i = 0; i < lines.length; ++i) {
            final String line = lines[i];
            final int pos = line.indexOf(testName);
            if (pos != -1
                    && line.indexOf('(', pos + testName.length() + 3) == -1) {
                return i + 1;
            }
        }

        return -1;
    }

    /**
     * @throws Exception if an error occurs
     */
    @BeforeClass
    public static void aaa_startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/jQuery/" + getVersion(), null);
    }

    /**
     * @throws Exception if an error occurs
     */
    @AfterClass
    public static void zzz_stopServer() throws Exception {
        SERVER_.stop();
    }

    /**
     * Test {1=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__Unit_Testing_Environment() throws Exception {
        runTest("core: Unit Testing Environment");
    }

    /**
     * Test {2=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__Basic_requirements() throws Exception {
        runTest("core: Basic requirements");
    }

    /**
     * Test {3=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void core__jQuery__() throws Exception {
        runTest("core: jQuery()");
    }

    /**
     * Test {4=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void core__selector_state() throws Exception {
        runTest("core: selector state");
    }

    /**
     * Test {5=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__globalEval() throws Exception {
        runTest("core: globalEval");
    }

    /**
     * Test {6=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__noConflict() throws Exception {
        runTest("core: noConflict");
    }

    /**
     * Test {7=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void core__trim() throws Exception {
        runTest("core: trim");
    }

    /**
     * Test {8=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void core__type() throws Exception {
        runTest("core: type");
    }

    /**
     * Test {9=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void core__isPlainObject() throws Exception {
        runTest("core: isPlainObject");
    }

    /**
     * Test {10=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__isFunction() throws Exception {
        runTest("core: isFunction");
    }

    /**
     * Test {11=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 36, 36")
    public void core__isNumeric() throws Exception {
        runTest("core: isNumeric");
    }

    /**
     * Test {12=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__isXMLDoc___HTML() throws Exception {
        runTest("core: isXMLDoc - HTML");
    }

    /**
     * Test {13=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__XSS_via_location_hash() throws Exception {
        runTest("core: XSS via location.hash");
    }

    /**
     * Test {14=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__isXMLDoc___XML() throws Exception {
        runTest("core: isXMLDoc - XML");
    }

    /**
     * Test {15=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__isWindow() throws Exception {
        runTest("core: isWindow");
    }

    /**
     * Test {16=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery__html__() throws Exception {
        runTest("core: jQuery('html')");
    }

    /**
     * Test {17=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__jQuery__html___context_() throws Exception {
        runTest("core: jQuery('html', context)");
    }

    /**
     * Test {18=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_selector__xml__text_str____Loaded_via_XML_document() throws Exception {
        runTest("core: jQuery(selector, xml).text(str) - Loaded via XML document");
    }

    /**
     * Test {19=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__end__() throws Exception {
        runTest("core: end()");
    }

    /**
     * Test {20=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__length() throws Exception {
        runTest("core: length");
    }

    /**
     * Test {21=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__size__() throws Exception {
        runTest("core: size()");
    }

    /**
     * Test {22=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__get__() throws Exception {
        runTest("core: get()");
    }

    /**
     * Test {23=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__toArray__() throws Exception {
        runTest("core: toArray()");
    }

    /**
     * Test {24=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__inArray__() throws Exception {
        runTest("core: inArray()");
    }

    /**
     * Test {25=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get_Number_() throws Exception {
        runTest("core: get(Number)");
    }

    /**
     * Test {26=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get__Number_() throws Exception {
        runTest("core: get(-Number)");
    }

    /**
     * Test {27=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__each_Function_() throws Exception {
        runTest("core: each(Function)");
    }

    /**
     * Test {28=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__slice__() throws Exception {
        runTest("core: slice()");
    }

    /**
     * Test {29=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__first___last__() throws Exception {
        runTest("core: first()/last()");
    }

    /**
     * Test {30=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__map__() throws Exception {
        runTest("core: map()");
    }

    /**
     * Test {31=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_merge__() throws Exception {
        runTest("core: jQuery.merge()");
    }

    /**
     * Test {32=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__jQuery_extend_Object__Object_() throws Exception {
        runTest("core: jQuery.extend(Object, Object)");
    }

    /**
     * Test {33=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__jQuery_each_Object_Function_() throws Exception {
        runTest("core: jQuery.each(Object,Function)");
    }

    /**
     * Test {34=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void core__jQuery_makeArray() throws Exception {
        runTest("core: jQuery.makeArray");
    }

    /**
     * Test {35=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery_inArray() throws Exception {
        runTest("core: jQuery.inArray");
    }

    /**
     * Test {36=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_isEmptyObject() throws Exception {
        runTest("core: jQuery.isEmptyObject");
    }

    /**
     * Test {37=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_proxy() throws Exception {
        runTest("core: jQuery.proxy");
    }

    /**
     * Test {38=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void core__jQuery_parseHTML() throws Exception {
        runTest("core: jQuery.parseHTML");
    }

    /**
     * Test {39=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseJSON() throws Exception {
        runTest("core: jQuery.parseJSON");
    }

    /**
     * Test {40=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseXML() throws Exception {
        runTest("core: jQuery.parseXML");
    }

    /**
     * Test {41=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery_sub_____Static_Methods() throws Exception {
        runTest("core: jQuery.sub() - Static Methods");
    }

    /**
     * Test {42=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 378, 378")
    public void core__jQuery_sub______fn_Methods() throws Exception {
        runTest("core: jQuery.sub() - .fn Methods");
    }

    /**
     * Test {43=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_camelCase__() throws Exception {
        runTest("core: jQuery.camelCase()");
    }

    /**
     * Test {44=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - no filter");
    }

    /**
     * Test {45=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks__________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - no filter");
    }

    /**
     * Test {46=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - filter");
    }

    /**
     * Test {47=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks__________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - filter");
    }

    /**
     * Test {48=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - no filter");
    }

    /**
     * Test {49=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - no filter");
    }

    /**
     * Test {50=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - filter");
    }

    /**
     * Test {51=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - filter");
    }

    /**
     * Test {52=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - no filter");
    }

    /**
     * Test {53=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - no filter");
    }

    /**
     * Test {54=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - filter");
    }

    /**
     * Test {55=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - filter");
    }

    /**
     * Test {56=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - no filter");
    }

    /**
     * Test {57=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - no filter");
    }

    /**
     * Test {58=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - filter");
    }

    /**
     * Test {59=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - filter");
    }

    /**
     * Test {60=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - no filter");
    }

    /**
     * Test {61=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {62=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - filter");
    }

    /**
     * Test {63=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {64=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - no filter");
    }

    /**
     * Test {65=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - no filter");
    }

    /**
     * Test {66=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - filter");
    }

    /**
     * Test {67=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - filter");
    }

    /**
     * Test {68=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - no filter");
    }

    /**
     * Test {69=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {70=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - filter");
    }

    /**
     * Test {71=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {72=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - no filter");
    }

    /**
     * Test {73=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {74=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - filter");
    }

    /**
     * Test {75=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {76=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - no filter");
    }

    /**
     * Test {77=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {78=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - filter");
    }

    /**
     * Test {79=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {80=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - no filter");
    }

    /**
     * Test {81=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {82=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - filter");
    }

    /**
     * Test {83=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {84=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - no filter");
    }

    /**
     * Test {85=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {86=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - filter");
    }

    /**
     * Test {87=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {88=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks__options_____options_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks( options ) - options are copied");
    }

    /**
     * Test {89=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_fireWith___arguments_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks.fireWith - arguments are copied");
    }

    /**
     * Test {90=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_remove___should_remove_all_instances() throws Exception {
        runTest("callbacks: jQuery.Callbacks.remove - should remove all instances");
    }

    /**
     * Test {91=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_____adding_a_string_doesn_t_cause_a_stack_overflow() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow");
    }

    /**
     * Test {92=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred() throws Exception {
        runTest("deferred: jQuery.Deferred");
    }

    /**
     * Test {93=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred___new_operator() throws Exception {
        runTest("deferred: jQuery.Deferred - new operator");
    }

    /**
     * Test {94=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void deferred__jQuery_Deferred___chainability() throws Exception {
        runTest("deferred: jQuery.Deferred - chainability");
    }

    /**
     * Test {95=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (done)");
    }

    /**
     * Test {96=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (fail)");
    }

    /**
     * Test {97=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___filtering__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (progress)");
    }

    /**
     * Test {98=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (done)");
    }

    /**
     * Test {99=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (fail)");
    }

    /**
     * Test {100=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (progress)");
    }

    /**
     * Test {101=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___context() throws Exception {
        runTest("deferred: jQuery.Deferred.then - context");
    }

    /**
     * Test {102=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 34, 34")
    public void deferred__jQuery_when() throws Exception {
        runTest("deferred: jQuery.when");
    }

    /**
     * Test {103=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 119, 119")
    public void deferred__jQuery_when___joined() throws Exception {
        runTest("deferred: jQuery.when - joined");
    }

    /**
     * Test {104=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__boxModel() throws Exception {
        runTest("support: boxModel");
    }

    /**
     * Test {105=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF24 = "0, 2, 2",
        IE11 = "0, 2, 2",
        IE8 = "9, 1, 10")
    @NotYetImplemented(IE8)
    public void support__body_background_is_not_lost_if_set_prior_to_loading_jQuery___9238_() throws Exception {
        runTest("support: body background is not lost if set prior to loading jQuery (#9238)");
    }

    /**
     * Test {106=[FF24, IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__A_background_on_the_testElement_does_not_cause_IE8_to_crash___9823_() throws Exception {
        runTest("support: A background on the testElement does not cause IE8 to crash (#9823)");
    }

    /**
     * Test {107=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE8 = "6, 24, 30")
    @NotYetImplemented(IE8)
    public void support__Verify_that_the_support_tests_resolve_as_expected_per_browser() throws Exception {
        runTest("support: Verify that the support tests resolve as expected per browser");
    }

    /**
     * Test {107=[FF24, IE11, IE9], 108=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__expando() throws Exception {
        runTest("data: expando");
    }

    /**
     * Test {108=[FF24, IE11, IE9], 109=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 124, 124")
    public void data__jQuery_data() throws Exception {
        runTest("data: jQuery.data");
    }

    /**
     * Test {109=[FF24, IE11, IE9], 110=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void data__jQuery_acceptData() throws Exception {
        runTest("data: jQuery.acceptData");
    }

    /**
     * Test {110=[FF24, IE11, IE9], 111=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void data___data__() throws Exception {
        runTest("data: .data()");
    }

    /**
     * Test {111=[FF24, IE11, IE9], 112=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void data___data_String__and__data_String__Object_() throws Exception {
        runTest("data: .data(String) and .data(String, Object)");
    }

    /**
     * Test {112=[FF24, IE11, IE9], 113=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 40, 40")
    public void data__data___attributes() throws Exception {
        runTest("data: data-* attributes");
    }

    /**
     * Test {113=[FF24, IE11, IE9], 114=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data___data_Object_() throws Exception {
        runTest("data: .data(Object)");
    }

    /**
     * Test {114=[FF24, IE11, IE9], 115=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_removeData() throws Exception {
        runTest("data: jQuery.removeData");
    }

    /**
     * Test {115=[FF24, IE11, IE9], 116=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void data___removeData__() throws Exception {
        runTest("data: .removeData()");
    }

    /**
     * Test {116=[FF24, IE11, IE9], 117=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_serialization___8108_() throws Exception {
        runTest("data: JSON serialization (#8108)");
    }

    /**
     * Test {117=[FF24, IE11, IE9], 118=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: jQuery.data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {118=[FF24, IE11, IE9], 119=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__jQuery_data_should_not_miss_data_with_preset_hyphenated_property_names() throws Exception {
        runTest("data: jQuery.data should not miss data with preset hyphenated property names");
    }

    /**
     * Test {119=[FF24, IE11, IE9], 120=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void data__jQuery_data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {120=[FF24, IE11, IE9], 121=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void data__jQuery_data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: jQuery.data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {121=[FF24, IE11, IE9], 122=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__Triggering_the_removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: Triggering the removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {122=[FF24, IE11, IE9], 123=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__Only_check_element_attributes_once_when_calling__data______8909() throws Exception {
        runTest("data: Only check element attributes once when calling .data() - #8909");
    }

    /**
     * Test {123=[FF24, IE11, IE9], 124=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_data__attributes_can_have_newlines() throws Exception {
        runTest("data: JSON data- attributes can have newlines");
    }

    /**
     * Test {124=[FF24, IE11, IE9], 125=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void queue__queue___with_other_types() throws Exception {
        runTest("queue: queue() with other types");
    }

    /**
     * Test {125=[FF24, IE11, IE9], 126=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_name__passes_in_the_next_item_in_the_queue_as_a_parameter() throws Exception {
        runTest("queue: queue(name) passes in the next item in the queue as a parameter");
    }

    /**
     * Test {126=[FF24, IE11, IE9], 127=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__queue___passes_in_the_next_item_in_the_queue_as_a_parameter_to_fx_queues() throws Exception {
        runTest("queue: queue() passes in the next item in the queue as a parameter to fx queues");
    }

    /**
     * Test {127=[FF24, IE11, IE9], 128=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__callbacks_keep_their_place_in_the_queue() throws Exception {
        runTest("queue: callbacks keep their place in the queue");
    }

    /**
     * Test {128=[FF24, IE11, IE9], 129=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__delay__() throws Exception {
        runTest("queue: delay()");
    }

    /**
     * Test {129=[FF24, IE11, IE9], 130=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__clearQueue_name__clears_the_queue() throws Exception {
        runTest("queue: clearQueue(name) clears the queue");
    }

    /**
     * Test {130=[FF24, IE11, IE9], 131=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void queue__clearQueue___clears_the_fx_queue() throws Exception {
        runTest("queue: clearQueue() clears the fx queue");
    }

    /**
     * Test {131=[FF24, IE11, IE9], 132=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__fn_promise_____called_when_fx_queue_is_empty() throws Exception {
        runTest("queue: fn.promise() - called when fx queue is empty");
    }

    /**
     * Test {132=[FF24, IE11, IE9], 133=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__fn_promise___queue______called_whenever_last_queue_function_is_dequeued() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued");
    }

    /**
     * Test {133=[FF24, IE11, IE9], 134=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__fn_promise___queue______waits_for_animation_to_complete_before_resolving() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving");
    }

    /**
     * Test {134=[FF24, IE11, IE9], 135=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue___promise_obj_() throws Exception {
        runTest("queue: .promise(obj)");
    }

    /**
     * Test {135=[FF24, IE11, IE9], 136=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__delay___can_be_stopped() throws Exception {
        runTest("queue: delay() can be stopped");
    }

    /**
     * Test {136=[FF24, IE11, IE9], 137=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_stop_hooks() throws Exception {
        runTest("queue: queue stop hooks");
    }

    /**
     * Test {137=[FF24, IE11, IE9], 138=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__jQuery_propFix_integrity_test() throws Exception {
        runTest("attributes: jQuery.propFix integrity test");
    }

    /**
     * Test {138=[FF24, IE11, IE9], 139=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 46, 46")
    public void attributes__attr_String_() throws Exception {
        runTest("attributes: attr(String)");
    }

    /**
     * Test {139=[FF24, IE11, IE9], 140=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_String__in_XML_Files() throws Exception {
        runTest("attributes: attr(String) in XML Files");
    }

    /**
     * Test {140=[FF24, IE11, IE9], 141=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Function_() throws Exception {
        runTest("attributes: attr(String, Function)");
    }

    /**
     * Test {141=[FF24, IE11, IE9], 142=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_Hash_() throws Exception {
        runTest("attributes: attr(Hash)");
    }

    /**
     * Test {142=[FF24, IE11, IE9], 143=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 81, 81")
    public void attributes__attr_String__Object_() throws Exception {
        runTest("attributes: attr(String, Object)");
    }

    /**
     * Test {143=[FF24, IE11, IE9], 144=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr_jquery_method_() throws Exception {
        runTest("attributes: attr(jquery_method)");
    }

    /**
     * Test {144=[FF24, IE11, IE9], 145=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_document() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML document");
    }

    /**
     * Test {145=[FF24, IE11, IE9], 146=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__attr__tabindex__() throws Exception {
        runTest("attributes: attr('tabindex')");
    }

    /**
     * Test {146=[FF24, IE11, IE9], 147=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr__tabindex___value_() throws Exception {
        runTest("attributes: attr('tabindex', value)");
    }

    /**
     * Test {147=[FF24, IE11, IE9], 148=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void attributes__removeAttr_String_() throws Exception {
        runTest("attributes: removeAttr(String)");
    }

    /**
     * Test {148=[FF24, IE11, IE9], 149=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeAttr_String__in_XML() throws Exception {
        runTest("attributes: removeAttr(String) in XML");
    }

    /**
     * Test {149=[FF24, IE11, IE9], 150=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeAttr_Multi_String__variable_space_width_() throws Exception {
        runTest("attributes: removeAttr(Multi String, variable space width)");
    }

    /**
     * Test {150=[FF24, IE11, IE9], 151=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void attributes__prop_String__Object_() throws Exception {
        runTest("attributes: prop(String, Object)");
    }

    /**
     * Test {151=[FF24, IE11, IE9], 152=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__prop__tabindex__() throws Exception {
        runTest("attributes: prop('tabindex')");
    }

    /**
     * Test {152=[FF24, IE11, IE9], 153=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__prop__tabindex___value_() throws Exception {
        runTest("attributes: prop('tabindex', value)");
    }

    /**
     * Test {153=[FF24, IE11, IE9], 154=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void attributes__removeProp_String_() throws Exception {
        runTest("attributes: removeProp(String)");
    }

    /**
     * Test {154=[FF24, IE11, IE9], 155=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void attributes__val__() throws Exception {
        runTest("attributes: val()");
    }

    /**
     * Test {155=[FF24]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF24 = "0, 4, 4")
    public void attributes__val___respects_numbers_without_exception__Bug__9319_() throws Exception {
        runTest("attributes: val() respects numbers without exception (Bug #9319)");
    }

    /**
     * Test {155=[IE11, IE9], 156=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__val_String_Number_() throws Exception {
        runTest("attributes: val(String/Number)");
    }

    /**
     * Test {156=[IE11, IE9], 157=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__val_Function_() throws Exception {
        runTest("attributes: val(Function)");
    }

    /**
     * Test {157=[IE11, IE9], 158=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__val_Array_of_Numbers___Bug__7123_() throws Exception {
        runTest("attributes: val(Array of Numbers) (Bug #7123)");
    }

    /**
     * Test {158=[IE11, IE9], 159=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void attributes__val_Function__with_incoming_value() throws Exception {
        runTest("attributes: val(Function) with incoming value");
    }

    /**
     * Test {159=[IE11, IE9], 160=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__val_select__after_form_reset____Bug__2551_() throws Exception {
        runTest("attributes: val(select) after form.reset() (Bug #2551)");
    }

    /**
     * Test {160=[IE11, IE9], 161=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_String_() throws Exception {
        runTest("attributes: addClass(String)");
    }

    /**
     * Test {161=[IE11, IE9], 162=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_Function_() throws Exception {
        runTest("attributes: addClass(Function)");
    }

    /**
     * Test {162=[IE11, IE9], 163=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 48, 48")
    public void attributes__addClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: addClass(Function) with incoming value");
    }

    /**
     * Test {163=[IE11, IE9], 164=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeClass_String____simple() throws Exception {
        runTest("attributes: removeClass(String) - simple");
    }

    /**
     * Test {164=[IE11, IE9], 165=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeClass_Function____simple() throws Exception {
        runTest("attributes: removeClass(Function) - simple");
    }

    /**
     * Test {165=[IE11, IE9], 166=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 48, 48")
    public void attributes__removeClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: removeClass(Function) with incoming value");
    }

    /**
     * Test {166=[IE11, IE9], 167=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__removeClass___removes_duplicates() throws Exception {
        runTest("attributes: removeClass() removes duplicates");
    }

    /**
     * Test {167=[IE11, IE9], 168=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_String_boolean_undefined___boolean__() throws Exception {
        runTest("attributes: toggleClass(String|boolean|undefined[, boolean])");
    }

    /**
     * Test {168=[IE11, IE9], 169=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_Function___boolean__() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean])");
    }

    /**
     * Test {169=[IE11, IE9], 170=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void attributes__toggleClass_Fucntion___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Fucntion[, boolean]) with incoming value");
    }

    /**
     * Test {170=[IE11, IE9], 171=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__addClass__removeClass__hasClass() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass");
    }

    /**
     * Test {171=[IE11, IE9], 172=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__contents___hasClass___returns_correct_values() throws Exception {
        runTest("attributes: contents().hasClass() returns correct values");
    }

    /**
     * Test {172=[IE11, IE9], 173=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__coords_returns_correct_values_in_IE6_IE7__see__10828() throws Exception {
        runTest("attributes: coords returns correct values in IE6/IE7, see #10828");
    }

    /**
     * Test {173=[IE11, IE9], 174=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__null_or_undefined_handler() throws Exception {
        runTest("event: null or undefined handler");
    }

    /**
     * Test {174=[IE11, IE9], 175=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__bind___live___delegate___with_non_null_defined_data() throws Exception {
        runTest("event: bind(),live(),delegate() with non-null,defined data");
    }

    /**
     * Test {175=[IE11, IE9], 176=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Handler_changes_and__trigger___order() throws Exception {
        runTest("event: Handler changes and .trigger() order");
    }

    /**
     * Test {176=[IE11, IE9], 177=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_data() throws Exception {
        runTest("event: bind(), with data");
    }

    /**
     * Test {177=[IE11, IE9], 178=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__click____with_data() throws Exception {
        runTest("event: click(), with data");
    }

    /**
     * Test {178=[IE11, IE9], 179=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_data__trigger_with_data() throws Exception {
        runTest("event: bind(), with data, trigger with data");
    }

    /**
     * Test {179=[IE11, IE9], 180=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind____multiple_events_at_once() throws Exception {
        runTest("event: bind(), multiple events at once");
    }

    /**
     * Test {180=[IE11, IE9], 181=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____five_events_at_once() throws Exception {
        runTest("event: bind(), five events at once");
    }

    /**
     * Test {181=[IE11, IE9], 182=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__bind____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: bind(), multiple events at once and namespaces");
    }

    /**
     * Test {182=[IE11, IE9], 183=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void event__bind____namespace_with_special_add() throws Exception {
        runTest("event: bind(), namespace with special add");
    }

    /**
     * Test {183=[IE11, IE9], 184=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____no_data() throws Exception {
        runTest("event: bind(), no data");
    }

    /**
     * Test {184=[IE11, IE9], 185=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__bind_one_unbind_Object_() throws Exception {
        runTest("event: bind/one/unbind(Object)");
    }

    /**
     * Test {185=[IE11, IE9], 186=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__live_die_Object___delegate_undelegate_String__Object_() throws Exception {
        runTest("event: live/die(Object), delegate/undelegate(String, Object)");
    }

    /**
     * Test {186=[IE11, IE9], 187=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__live_delegate_immediate_propagation() throws Exception {
        runTest("event: live/delegate immediate propagation");
    }

    /**
     * Test {187=[IE11, IE9], 188=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind_delegate_bubbling__isDefaultPrevented() throws Exception {
        runTest("event: bind/delegate bubbling, isDefaultPrevented");
    }

    /**
     * Test {188=[IE11, IE9], 189=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____iframes() throws Exception {
        runTest("event: bind(), iframes");
    }

    /**
     * Test {189=[IE11, IE9], 190=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__bind____trigger_change_on_select() throws Exception {
        runTest("event: bind(), trigger change on select");
    }

    /**
     * Test {190=[IE11, IE9], 191=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__bind____namespaced_events__cloned_events() throws Exception {
        runTest("event: bind(), namespaced events, cloned events");
    }

    /**
     * Test {191=[IE11, IE9], 192=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__bind____multi_namespaced_events() throws Exception {
        runTest("event: bind(), multi-namespaced events");
    }

    /**
     * Test {192=[IE11, IE9], 193=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind____with_same_function() throws Exception {
        runTest("event: bind(), with same function");
    }

    /**
     * Test {193=[IE11, IE9], 194=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____make_sure_order_is_maintained() throws Exception {
        runTest("event: bind(), make sure order is maintained");
    }

    /**
     * Test {194=[IE11, IE9], 195=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_different_this_object() throws Exception {
        runTest("event: bind(), with different this object");
    }

    /**
     * Test {195=[IE11, IE9], 196=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__bind_name__false___unbind_name__false_() throws Exception {
        runTest("event: bind(name, false), unbind(name, false)");
    }

    /**
     * Test {196=[IE11, IE9], 197=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__live_name__false___die_name__false_() throws Exception {
        runTest("event: live(name, false), die(name, false)");
    }

    /**
     * Test {197=[IE11, IE9], 198=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegate_selector__name__false___undelegate_selector__name__false_() throws Exception {
        runTest("event: delegate(selector, name, false), undelegate(selector, name, false)");
    }

    /**
     * Test {198=[IE11, IE9], 199=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__bind___trigger___unbind___on_plain_object() throws Exception {
        runTest("event: bind()/trigger()/unbind() on plain object");
    }

    /**
     * Test {199=[IE11, IE9], 200=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__unbind_type_() throws Exception {
        runTest("event: unbind(type)");
    }

    /**
     * Test {200=[IE11, IE9], 201=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__unbind_eventObject_() throws Exception {
        runTest("event: unbind(eventObject)");
    }

    /**
     * Test {201=[IE11, IE9], 202=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__hover___and_hover_pseudo_event() throws Exception {
        runTest("event: hover() and hover pseudo-event");
    }

    /**
     * Test {202=[IE11, IE9], 203=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__mouseover_triggers_mouseenter() throws Exception {
        runTest("event: mouseover triggers mouseenter");
    }

    /**
     * Test {203=[IE11, IE9], 204=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__withinElement_implemented_with_jQuery_contains__() throws Exception {
        runTest("event: withinElement implemented with jQuery.contains()");
    }

    /**
     * Test {204=[IE11, IE9], 205=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__mouseenter__mouseleave_don_t_catch_exceptions() throws Exception {
        runTest("event: mouseenter, mouseleave don't catch exceptions");
    }

    /**
     * Test {205=[IE11, IE9], 206=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__trigger___shortcuts() throws Exception {
        runTest("event: trigger() shortcuts");
    }

    /**
     * Test {206=[IE11, IE9], 207=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__trigger___bubbling() throws Exception {
        runTest("event: trigger() bubbling");
    }

    /**
     * Test {207=[IE11, IE9], 208=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__trigger_type___data____fn__() throws Exception {
        runTest("event: trigger(type, [data], [fn])");
    }

    /**
     * Test {208=[IE11, IE9], 209=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE8)
    public void event__submit_event_bubbles_on_copied_forms___11649_() throws Exception {
        runTest("event: submit event bubbles on copied forms (#11649)");
    }

    /**
     * Test {209=[IE11, IE9], 210=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__change_event_bubbles_on_copied_forms___11796_() throws Exception {
        runTest("event: change event bubbles on copied forms (#11796)");
    }

    /**
     * Test {210=[IE11, IE9], 211=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void event__trigger_eventObject___data____fn__() throws Exception {
        runTest("event: trigger(eventObject, [data], [fn])");
    }

    /**
     * Test {211=[IE11, IE9], 212=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event___trigger___bubbling_on_disconnected_elements___10489_() throws Exception {
        runTest("event: .trigger() bubbling on disconnected elements (#10489)");
    }

    /**
     * Test {212=[IE11, IE9], 213=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___trigger___doesn_t_bubble_load_event___10717_() throws Exception {
        runTest("event: .trigger() doesn't bubble load event (#10717)");
    }

    /**
     * Test {213=[IE11, IE9], 214=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented(FF)
    public void event__Delegated_events_in_SVG___10791_() throws Exception {
        runTest("event: Delegated events in SVG (#10791)");
    }

    /**
     * Test {214=[IE11, IE9], 215=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__Delegated_events_in_forms___10844___11145___8165___11382___11764_() throws Exception {
        runTest("event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764)");
    }

    /**
     * Test {215=[IE11, IE9], 216=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Submit_event_can_be_stopped___11049_() throws Exception {
        runTest("event: Submit event can be stopped (#11049)");
    }

    /**
     * Test {216=[IE11, IE9], 217=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__on_beforeunload__creates_deletes_window_property_instead_of_adding_removing_event_listener() throws Exception {
        runTest("event: on(beforeunload) creates/deletes window property instead of adding/removing event listener");
    }

    /**
     * Test {217=[IE11, IE9], 218=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__jQuery_Event__type__props__() throws Exception {
        runTest("event: jQuery.Event( type, props )");
    }

    /**
     * Test {218=[IE11, IE9], 219=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_Event_currentTarget() throws Exception {
        runTest("event: jQuery.Event.currentTarget");
    }

    /**
     * Test {219=[IE11, IE9], 220=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__toggle_Function__Function______() throws Exception {
        runTest("event: toggle(Function, Function, ...)");
    }

    /**
     * Test {220=[IE11, IE9], 221=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 66, 66")
    public void event___live____die__() throws Exception {
        runTest("event: .live()/.die()");
    }

    /**
     * Test {221=[IE11, IE9], 222=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__die_all_bound_events() throws Exception {
        runTest("event: die all bound events");
    }

    /**
     * Test {222=[IE11, IE9], 223=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__live_with_multiple_events() throws Exception {
        runTest("event: live with multiple events");
    }

    /**
     * Test {223=[IE11, IE9], 224=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void event__live_with_namespaces() throws Exception {
        runTest("event: live with namespaces");
    }

    /**
     * Test {224=[IE11, IE9], 225=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__live_with_change() throws Exception {
        runTest("event: live with change");
    }

    /**
     * Test {225=[IE11, IE9], 226=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__live_with_submit() throws Exception {
        runTest("event: live with submit");
    }

    /**
     * Test {226=[IE11, IE9], 227=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void event__live_with_special_events() throws Exception {
        runTest("event: live with special events");
    }

    /**
     * Test {227=[IE11, IE9], 228=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 65, 65")
    public void event___delegate____undelegate__() throws Exception {
        runTest("event: .delegate()/.undelegate()");
    }

    /**
     * Test {228=[IE11, IE9], 229=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_off_using_dispatched_jQuery_Event() throws Exception {
        runTest("event: jQuery.off using dispatched jQuery.Event");
    }

    /**
     * Test {229=[IE11, IE9], 230=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegated_event_with_delegateTarget_relative_selector() throws Exception {
        runTest("event: delegated event with delegateTarget-relative selector");
    }

    /**
     * Test {230=[IE11, IE9], 231=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__stopPropagation___stops_directly_bound_events_on_delegated_target() throws Exception {
        runTest("event: stopPropagation() stops directly-bound events on delegated target");
    }

    /**
     * Test {231=[IE11, IE9], 232=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__undelegate_all_bound_events() throws Exception {
        runTest("event: undelegate all bound events");
    }

    /**
     * Test {232=[IE11, IE9], 233=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__delegate_with_multiple_events() throws Exception {
        runTest("event: delegate with multiple events");
    }

    /**
     * Test {233=[IE11, IE9], 234=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__delegate_with_change() throws Exception {
        runTest("event: delegate with change");
    }

    /**
     * Test {234=[IE11, IE9], 235=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__delegate_with_submit() throws Exception {
        runTest("event: delegate with submit");
    }

    /**
     * Test {235=[IE11, IE9], 236=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__undelegate___with_only_namespaces() throws Exception {
        runTest("event: undelegate() with only namespaces");
    }

    /**
     * Test {236=[IE11, IE9], 237=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Non_DOM_element_events() throws Exception {
        runTest("event: Non DOM element events");
    }

    /**
     * Test {237=[IE11, IE9], 238=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__inline_handler_returning_false_stops_default() throws Exception {
        runTest("event: inline handler returning false stops default");
    }

    /**
     * Test {238=[IE11, IE9], 239=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__window_resize() throws Exception {
        runTest("event: window resize");
    }

    /**
     * Test {239=[IE11, IE9], 240=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__focusin_bubbles() throws Exception {
        runTest("event: focusin bubbles");
    }

    /**
     * Test {240=[IE11, IE9], 241=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__custom_events_with_colons___3533___8272_() throws Exception {
        runTest("event: custom events with colons (#3533, #8272)");
    }

    /**
     * Test {241=[IE11, IE9], 242=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void event___on_and__off() throws Exception {
        runTest("event: .on and .off");
    }

    /**
     * Test {242=[IE11, IE9], 243=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__special_bind_delegate_name_mapping() throws Exception {
        runTest("event: special bind/delegate name mapping");
    }

    /**
     * Test {243=[IE11, IE9], 244=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event___on_and__off__selective_mixed_removal___10705_() throws Exception {
        runTest("event: .on and .off, selective mixed removal (#10705)");
    }

    /**
     * Test {244=[IE11, IE9], 245=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___on__event_map__null_selector__data____11130() throws Exception {
        runTest("event: .on( event-map, null-selector, data ) #11130");
    }

    /**
     * Test {245=[IE11, IE9], 246=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__clone___delegated_events___11076_() throws Exception {
        runTest("event: clone() delegated events (#11076)");
    }

    /**
     * Test {246=[IE11, IE9], 247=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__fixHooks_extensions() throws Exception {
        runTest("event: fixHooks extensions");
    }

    /**
     * Test {247=[IE11, IE9], 248=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_promise() throws Exception {
        runTest("event: jQuery.ready promise");
    }

    /**
     * Test {248=[IE11, IE9], 249=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_synchronous_load_with_long_loading_subresources() throws Exception {
        runTest("event: jQuery.ready synchronous load with long loading subresources");
    }

    /**
     * Test {249=[IE11, IE9], 250=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_isReady() throws Exception {
        runTest("event: jQuery.isReady");
    }

    /**
     * Test {250=[IE11, IE9], 251=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void event__jQuery_ready() throws Exception {
        runTest("event: jQuery ready");
    }

    /**
     * Test {251=[IE11, IE9], 252=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__change_handler_should_be_detached_from_element() throws Exception {
        runTest("event: change handler should be detached from element");
    }

    /**
     * Test {252=[IE11, IE9], 253=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__trigger_click_on_checkbox__fires_change_event() throws Exception {
        runTest("event: trigger click on checkbox, fires change event");
    }

    /**
     * Test {253=[IE11, IE9], 254=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void selector___jQuery_only__element___jQuery_only() throws Exception {
        runTest("selector - jQuery only: element - jQuery only");
    }

    /**
     * Test {254=[IE11, IE9], 255=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector___jQuery_only__class___jQuery_only() throws Exception {
        runTest("selector - jQuery only: class - jQuery only");
    }

    /**
     * Test {255=[IE11, IE9], 256=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void selector___jQuery_only__attributes___jQuery_only() throws Exception {
        runTest("selector - jQuery only: attributes - jQuery only");
    }

    /**
     * Test {256=[IE11, IE9], 257=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void selector___jQuery_only__pseudo___visibility() throws Exception {
        runTest("selector - jQuery only: pseudo - visibility");
    }

    /**
     * Test {257=[IE11, IE9], 258=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector___jQuery_only__disconnected_nodes() throws Exception {
        runTest("selector - jQuery only: disconnected nodes");
    }

    /**
     * Test {258=[IE11, IE9], 259=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    @NotYetImplemented
    public void selector___jQuery_only__attributes___jQuery_attr() throws Exception {
        runTest("selector - jQuery only: attributes - jQuery.attr");
    }

    /**
     * Test {259=[IE11, IE9], 260=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void selector___jQuery_only__Sizzle_cache_collides_with_multiple_Sizzles_on_a_page() throws Exception {
        runTest("selector - jQuery only: Sizzle cache collides with multiple Sizzles on a page");
    }

    /**
     * Test {260=[IE11, IE9], 261=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__find_String_() throws Exception {
        runTest("traversing: find(String)");
    }

    /**
     * Test {261=[IE11, IE9], 262=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__find_node_jQuery_object_() throws Exception {
        runTest("traversing: find(node|jQuery object)");
    }

    /**
     * Test {262=[IE11, IE9], 263=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    public void traversing__is_String_undefined_() throws Exception {
        runTest("traversing: is(String|undefined)");
    }

    /**
     * Test {263=[IE11, IE9], 264=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void traversing__is_jQuery_() throws Exception {
        runTest("traversing: is(jQuery)");
    }

    /**
     * Test {264=[IE11, IE9], 265=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void traversing__is___with_positional_selectors() throws Exception {
        runTest("traversing: is() with positional selectors");
    }

    /**
     * Test {265=[IE11, IE9], 266=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__index__() throws Exception {
        runTest("traversing: index()");
    }

    /**
     * Test {266=[IE11, IE9], 267=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__index_Object_String_undefined_() throws Exception {
        runTest("traversing: index(Object|String|undefined)");
    }

    /**
     * Test {267=[IE11, IE9], 268=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__filter_Selector_undefined_() throws Exception {
        runTest("traversing: filter(Selector|undefined)");
    }

    /**
     * Test {268=[IE11, IE9], 269=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__filter_Function_() throws Exception {
        runTest("traversing: filter(Function)");
    }

    /**
     * Test {269=[IE11, IE9], 270=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Element_() throws Exception {
        runTest("traversing: filter(Element)");
    }

    /**
     * Test {270=[IE11, IE9], 271=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Array_() throws Exception {
        runTest("traversing: filter(Array)");
    }

    /**
     * Test {271=[IE11, IE9], 272=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_jQuery_() throws Exception {
        runTest("traversing: filter(jQuery)");
    }

    /**
     * Test {272=[IE11, IE9], 273=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void traversing__filter___with_positional_selectors() throws Exception {
        runTest("traversing: filter() with positional selectors");
    }

    /**
     * Test {273=[IE11, IE9], 274=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void traversing__closest__() throws Exception {
        runTest("traversing: closest()");
    }

    /**
     * Test {274=[IE11, IE9], 275=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void traversing__closest_jQuery_() throws Exception {
        runTest("traversing: closest(jQuery)");
    }

    /**
     * Test {275=[IE11, IE9], 276=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__not_Selector_undefined_() throws Exception {
        runTest("traversing: not(Selector|undefined)");
    }

    /**
     * Test {276=[IE11, IE9], 277=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Element_() throws Exception {
        runTest("traversing: not(Element)");
    }

    /**
     * Test {277=[IE11, IE9], 278=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Function_() throws Exception {
        runTest("traversing: not(Function)");
    }

    /**
     * Test {278=[IE11, IE9], 279=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__not_Array_() throws Exception {
        runTest("traversing: not(Array)");
    }

    /**
     * Test {279=[IE11, IE9], 280=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_jQuery_() throws Exception {
        runTest("traversing: not(jQuery)");
    }

    /**
     * Test {280=[IE11, IE9], 281=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__has_Element_() throws Exception {
        runTest("traversing: has(Element)");
    }

    /**
     * Test {281=[IE11, IE9], 282=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__has_Selector_() throws Exception {
        runTest("traversing: has(Selector)");
    }

    /**
     * Test {282=[IE11, IE9], 283=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__has_Arrayish_() throws Exception {
        runTest("traversing: has(Arrayish)");
    }

    /**
     * Test {283=[IE11, IE9], 284=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__addBack__() throws Exception {
        runTest("traversing: addBack()");
    }

    /**
     * Test {284=[IE11, IE9], 285=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void traversing__siblings__String__() throws Exception {
        runTest("traversing: siblings([String])");
    }

    /**
     * Test {285=[IE11, IE9], 286=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__children__String__() throws Exception {
        runTest("traversing: children([String])");
    }

    /**
     * Test {286=[IE11, IE9], 287=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__parent__String__() throws Exception {
        runTest("traversing: parent([String])");
    }

    /**
     * Test {287=[IE11, IE9], 288=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__parents__String__() throws Exception {
        runTest("traversing: parents([String])");
    }

    /**
     * Test {288=[IE11, IE9], 289=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__parentsUntil__String__() throws Exception {
        runTest("traversing: parentsUntil([String])");
    }

    /**
     * Test {289=[IE11, IE9], 290=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__next__String__() throws Exception {
        runTest("traversing: next([String])");
    }

    /**
     * Test {290=[IE11, IE9], 291=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__prev__String__() throws Exception {
        runTest("traversing: prev([String])");
    }

    /**
     * Test {291=[IE11, IE9], 292=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__nextAll__String__() throws Exception {
        runTest("traversing: nextAll([String])");
    }

    /**
     * Test {292=[IE11, IE9], 293=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__prevAll__String__() throws Exception {
        runTest("traversing: prevAll([String])");
    }

    /**
     * Test {293=[IE11, IE9], 294=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__nextUntil__String__() throws Exception {
        runTest("traversing: nextUntil([String])");
    }

    /**
     * Test {294=[IE11, IE9], 295=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void traversing__prevUntil__String__() throws Exception {
        runTest("traversing: prevUntil([String])");
    }

    /**
     * Test {295=[IE11, IE9], 296=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__contents__() throws Exception {
        runTest("traversing: contents()");
    }

    /**
     * Test {296=[IE11, IE9], 297=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__add_String_Element_Array_undefined_() throws Exception {
        runTest("traversing: add(String|Element|Array|undefined)");
    }

    /**
     * Test {297=[IE11, IE9], 298=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__add_String__Context_() throws Exception {
        runTest("traversing: add(String, Context)");
    }

    /**
     * Test {298=[IE11, IE9], 299=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__eq___1____10616() throws Exception {
        runTest("traversing: eq('-1') #10616");
    }

    /**
     * Test {299=[IE11, IE9], 300=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__text__() throws Exception {
        runTest("manipulation: text()");
    }

    /**
     * Test {300=[IE11, IE9], 301=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__text_undefined_() throws Exception {
        runTest("manipulation: text(undefined)");
    }

    /**
     * Test {301=[IE11, IE9], 302=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__text_String_() throws Exception {
        runTest("manipulation: text(String)");
    }

    /**
     * Test {302=[IE11, IE9], 303=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__text_Function_() throws Exception {
        runTest("manipulation: text(Function)");
    }

    /**
     * Test {303=[IE11, IE9], 304=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__text_Function__with_incoming_value() throws Exception {
        runTest("manipulation: text(Function) with incoming value");
    }

    /**
     * Test {304=[IE11, IE9], 305=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void manipulation__wrap_String_Element_() throws Exception {
        runTest("manipulation: wrap(String|Element)");
    }

    /**
     * Test {305=[IE11, IE9], 306=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void manipulation__wrap_Function_() throws Exception {
        runTest("manipulation: wrap(Function)");
    }

    /**
     * Test {306=[IE11, IE9], 307=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__wrap_Function__with_index___10177_() throws Exception {
        runTest("manipulation: wrap(Function) with index (#10177)");
    }

    /**
     * Test {307=[IE11, IE9], 308=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void manipulation__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("manipulation: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {308=[IE11, IE9], 309=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__wrapAll_String_Element_() throws Exception {
        runTest("manipulation: wrapAll(String|Element)");
    }

    /**
     * Test {309=[IE11, IE9], 310=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void manipulation__wrapInner_String_Element_() throws Exception {
        runTest("manipulation: wrapInner(String|Element)");
    }

    /**
     * Test {310=[IE11, IE9], 311=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void manipulation__wrapInner_Function_() throws Exception {
        runTest("manipulation: wrapInner(Function)");
    }

    /**
     * Test {311=[IE11, IE9], 312=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__unwrap__() throws Exception {
        runTest("manipulation: unwrap()");
    }

    /**
     * Test {312=[IE11, IE9], 313=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 58, 58")
    public void manipulation__append_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {313=[IE11, IE9], 314=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 58, 58")
    public void manipulation__append_Function_() throws Exception {
        runTest("manipulation: append(Function)");
    }

    /**
     * Test {314=[IE11, IE9], 315=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void manipulation__append_Function__with_incoming_value() throws Exception {
        runTest("manipulation: append(Function) with incoming value");
    }

    /**
     * Test {315=[IE11, IE9], 316=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF24 = "0, 2, 2",
        IE11 = "0, 2, 2",
        IE8 = "0, 3, 3")
    public void manipulation__append_the_same_fragment_with_events__Bug__6997__5566_() throws Exception {
        runTest("manipulation: append the same fragment with events (Bug #6997, 5566)");
    }

    /**
     * Test {316=[IE11, IE9], 317=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_HTML5_sectioning_elements__Bug__6485_() throws Exception {
        runTest("manipulation: append HTML5 sectioning elements (Bug #6485)");
    }

    /**
     * Test {317=[IE11, IE9], 318=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__HTML5_Elements_inherit_styles_from_style_rules__Bug__10501_() throws Exception {
        runTest("manipulation: HTML5 Elements inherit styles from style rules (Bug #10501)");
    }

    /**
     * Test {318=[IE11, IE9], 319=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html5_clone___cannot_use_the_fragment_cache_in_IE___6485_() throws Exception {
        runTest("manipulation: html5 clone() cannot use the fragment cache in IE (#6485)");
    }

    /**
     * Test {319=[IE11, IE9], 320=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_String__with_HTML5__Bug__6485_() throws Exception {
        runTest("manipulation: html(String) with HTML5 (Bug #6485)");
    }

    /**
     * Test {320=[IE11, IE9], 321=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__IE8_serialization_bug() throws Exception {
        runTest("manipulation: IE8 serialization bug");
    }

    /**
     * Test {321=[IE11, IE9], 322=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___object_element__10324() throws Exception {
        runTest("manipulation: html() object element #10324");
    }

    /**
     * Test {322=[IE11, IE9], 323=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__append_xml_() throws Exception {
        runTest("manipulation: append(xml)");
    }

    /**
     * Test {323=[IE11, IE9], 324=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void manipulation__appendTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: appendTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {324=[IE11, IE9], 325=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prepend_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prepend(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {325=[IE11, IE9], 326=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prepend_Function_() throws Exception {
        runTest("manipulation: prepend(Function)");
    }

    /**
     * Test {326=[IE11, IE9], 327=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__prepend_Function__with_incoming_value() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value");
    }

    /**
     * Test {327=[IE11, IE9], 328=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prependTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prependTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {328=[IE11, IE9], 329=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__before_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: before(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {329=[IE11, IE9], 330=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__before_Function_() throws Exception {
        runTest("manipulation: before(Function)");
    }

    /**
     * Test {330=[IE11, IE9], 331=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_and_after_w__empty_object___10812_() throws Exception {
        runTest("manipulation: before and after w/ empty object (#10812)");
    }

    /**
     * Test {331=[IE11, IE9], 332=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_and_after_on_disconnected_node___10517_() throws Exception {
        runTest("manipulation: before and after on disconnected node (#10517)");
    }

    /**
     * Test {332=[IE11, IE9], 333=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__insertBefore_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertBefore(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {333=[IE11, IE9], 334=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__after_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: after(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {334=[IE11, IE9], 335=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__after_Function_() throws Exception {
        runTest("manipulation: after(Function)");
    }

    /**
     * Test {335=[IE11, IE9], 336=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__insertAfter_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertAfter(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {336=[IE11, IE9], 337=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void manipulation__replaceWith_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {337=[IE11, IE9], 338=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void manipulation__replaceWith_Function_() throws Exception {
        runTest("manipulation: replaceWith(Function)");
    }

    /**
     * Test {338=[IE11, IE9], 339=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceWith_string__for_more_than_one_element() throws Exception {
        runTest("manipulation: replaceWith(string) for more than one element");
    }

    /**
     * Test {339=[IE11, IE9], 340=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__replaceAll_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceAll(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {340=[IE11, IE9], 341=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery_clone_____8017_() throws Exception {
        runTest("manipulation: jQuery.clone() (#8017)");
    }

    /**
     * Test {341=[IE11, IE9], 342=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone_____8070_() throws Exception {
        runTest("manipulation: clone() (#8070)");
    }

    /**
     * Test {342=[IE11, IE9], 343=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 44, 44")
    public void manipulation__clone__() throws Exception {
        runTest("manipulation: clone()");
    }

    /**
     * Test {343=[IE11, IE9], 344=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__clone_script_type_non_javascript____11359_() throws Exception {
        runTest("manipulation: clone(script type=non-javascript) (#11359)");
    }

    /**
     * Test {344=[IE11, IE9], 345=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "0, 5, 5",
            IE11 = "1, 4, 5")
    public void manipulation__clone_form_element___Bug__3879___6655_() throws Exception {
        runTest("manipulation: clone(form element) (Bug #3879, #6655)");
    }

    /**
     * Test {345=[IE11, IE9], 346=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__clone_multiple_selected_options___Bug__8129_() throws Exception {
        runTest("manipulation: clone(multiple selected options) (Bug #8129)");
    }

    /**
     * Test {346=[IE11, IE9], 347=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_XML_nodes() throws Exception {
        runTest("manipulation: clone() on XML nodes");
    }

    /**
     * Test {347=[IE11, IE9], 348=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_local_XML_nodes_with_html5_nodename() throws Exception {
        runTest("manipulation: clone() on local XML nodes with html5 nodename");
    }

    /**
     * Test {348=[IE11, IE9], 349=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html_undefined_() throws Exception {
        runTest("manipulation: html(undefined)");
    }

    /**
     * Test {349=[IE11, IE9], 350=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___on_empty_set() throws Exception {
        runTest("manipulation: html() on empty set");
    }

    /**
     * Test {350=[IE11, IE9], 351=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    public void manipulation__html_String_() throws Exception {
        runTest("manipulation: html(String)");
    }

    /**
     * Test {351=[IE11, IE9], 352=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 37, 37")
    public void manipulation__html_Function_() throws Exception {
        runTest("manipulation: html(Function)");
    }

    /**
     * Test {352=[IE11, IE9], 353=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void manipulation__html_Function__with_incoming_value() throws Exception {
        runTest("manipulation: html(Function) with incoming value");
    }

    /**
     * Test {353=[IE11, IE9], 354=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__remove__() throws Exception {
        runTest("manipulation: remove()");
    }

    /**
     * Test {354=[IE11, IE9], 355=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__detach__() throws Exception {
        runTest("manipulation: detach()");
    }

    /**
     * Test {355=[IE11, IE9], 356=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__empty__() throws Exception {
        runTest("manipulation: empty()");
    }

    /**
     * Test {356=[IE11, IE9], 357=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void manipulation__jQuery_cleanData() throws Exception {
        runTest("manipulation: jQuery.cleanData");
    }

    /**
     * Test {357=[IE11, IE9], 358=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___no_plain_text_caching__Bug__6779_() throws Exception {
        runTest("manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779)");
    }

    /**
     * Test {358=[IE11, IE9], 359=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__jQuery_html___execute_scripts_escaped_with_html_comment_or_CDATA___9221_() throws Exception {
        runTest("manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221)");
    }

    /**
     * Test {359=[IE11, IE9], 360=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___plain_objects_are_not_a_document__8950() throws Exception {
        runTest("manipulation: jQuery.buildFragment - plain objects are not a document #8950");
    }

    /**
     * Test {360=[IE11, IE9], 361=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_clone___no_exceptions_for_object_elements__9587() throws Exception {
        runTest("manipulation: jQuery.clone - no exceptions for object elements #9587");
    }

    /**
     * Test {361=[IE11, IE9], 362=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("manipulation: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {362=[IE11, IE9], 363=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__Cloned__detached_HTML5_elems___10667_10670_() throws Exception {
        runTest("manipulation: Cloned, detached HTML5 elems (#10667,10670)");
    }

    /**
     * Test {363=[IE11, IE9], 364=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__jQuery_fragments_cache_expectations() throws Exception {
        runTest("manipulation: jQuery.fragments cache expectations");
    }

    /**
     * Test {364=[IE11, IE9], 365=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__Guard_against_exceptions_when_clearing_safeChildNodes() throws Exception {
        runTest("manipulation: Guard against exceptions when clearing safeChildNodes");
    }

    /**
     * Test {365=[IE11, IE9], 366=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__Ensure_oldIE_creates_a_new_set_on_appendTo___8894_() throws Exception {
        runTest("manipulation: Ensure oldIE creates a new set on appendTo (#8894)");
    }

    /**
     * Test {366=[IE11, IE9], 367=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_____script_exceptions_bubble___11743_() throws Exception {
        runTest("manipulation: html() - script exceptions bubble (#11743)");
    }

    /**
     * Test {367=[IE11, IE9], 368=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__checked_state_is_cloned_with_clone__() throws Exception {
        runTest("manipulation: checked state is cloned with clone()");
    }

    /**
     * Test {368=[IE11, IE9], 369=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__manipulate_mixed_jQuery_and_text___12384___12346_() throws Exception {
        runTest("manipulation: manipulate mixed jQuery and text (#12384, #12346)");
    }

    /**
     * Test {369=[IE11, IE9], 370=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__buildFragment_works_even_if_document_0__is_iframe_s_window_object_in_IE9_10___12266_() throws Exception {
        runTest("manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266)");
    }

    /**
     * Test {370=[IE11, IE9], 371=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 46, 46")
    @NotYetImplemented
    public void css__css_String_Hash_() throws Exception {
        runTest("css: css(String|Hash)");
    }

    /**
     * Test {371=[IE11, IE9], 372=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void css__css___explicit_and_relative_values() throws Exception {
        runTest("css: css() explicit and relative values");
    }

    /**
     * Test {372=[IE11, IE9], 373=[FF24, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void css__css_String__Object_() throws Exception {
        runTest("css: css(String, Object)");
    }

    /**
     * Test {373=[IE11, IE9], 374=[FF24], 376=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function_() throws Exception {
        runTest("css: css(String, Function)");
    }

    /**
     * Test {374=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE8 = "0, 5, 5")
    public void css__css_String__Object__for_MSIE() throws Exception {
        runTest("css: css(String, Object) for MSIE");
    }

    /**
     * Test {374=[IE11, IE9], 375=[FF24], 377=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function__with_incoming_value() throws Exception {
        runTest("css: css(String, Function) with incoming value");
    }

    /**
     * Test {375=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE8 = "0, 2, 2")
    public void css__Setting_opacity_to_1_properly_removes_filter__style___6652_() throws Exception {
        runTest("css: Setting opacity to 1 properly removes filter: style (#6652)");
    }

    /**
     * Test {375=[IE11, IE9], 376=[FF24], 378=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions() throws Exception {
        runTest("css: css(Object) where values are Functions");
    }

    /**
     * Test {376=[IE11, IE9], 377=[FF24], 379=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions_with_incoming_values() throws Exception {
        runTest("css: css(Object) where values are Functions with incoming values");
    }

    /**
     * Test {377=[IE11, IE9], 378=[FF24], 380=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void css__show____hide__() throws Exception {
        runTest("css: show(); hide()");
    }

    /**
     * Test {378=[IE11, IE9], 379=[FF24], 381=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF24 = "0, 7, 7",
        IE11 = "0, 7, 7",
        IE8 = "2, 1, 3")
    @NotYetImplemented({ IE8, FF24, IE11 })
    public void css__show___resolves_correct_default_display__8099() throws Exception {
        runTest("css: show() resolves correct default display #8099");
    }

    /**
     * Test {379=[IE11, IE9], 380=[FF24], 382=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void css__show___resolves_correct_default_display__detached_nodes___10006_() throws Exception {
        runTest("css: show() resolves correct default display, detached nodes (#10006)");
    }

    /**
     * Test {380=[IE11, IE9], 381=[FF24], 383=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void css__toggle__() throws Exception {
        runTest("css: toggle()");
    }

    /**
     * Test {381=[IE11, IE9], 382=[FF24], 384=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__hide_hidden_elements__bug__7141_() throws Exception {
        runTest("css: hide hidden elements (bug #7141)");
    }

    /**
     * Test {382=[IE11, IE9], 383=[FF24], 385=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {383=[IE11, IE9], 384=[FF24], 386=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css___visible_selector_works_properly_on_table_elements__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on table elements (bug #4512)");
    }

    /**
     * Test {384=[IE11, IE9], 385=[FF24], 387=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void css___visible_selector_works_properly_on_children_with_a_hidden_parent__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on children with a hidden parent (bug #4512)");
    }

    /**
     * Test {385=[IE11, IE9], 386=[FF24], 388=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__internal_ref_to_elem_runtimeStyle__bug__7608_() throws Exception {
        runTest("css: internal ref to elem.runtimeStyle (bug #7608)");
    }

    /**
     * Test {386=[IE11, IE9], 387=[FF24], 389=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__marginRight_computed_style__bug__3333_() throws Exception {
        runTest("css: marginRight computed style (bug #3333)");
    }

    /**
     * Test {387=[IE11, IE9], 388=[FF24], 390=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__box_model_properties_incorrectly_returning___instead_of_px__see__10639_and__12088() throws Exception {
        runTest("css: box model properties incorrectly returning % instead of px, see #10639 and #12088");
    }

    /**
     * Test {388=[IE11, IE9], 389=[FF24], 391=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__jQuery_cssProps_behavior___bug__8402_() throws Exception {
        runTest("css: jQuery.cssProps behavior, (bug #8402)");
    }

    /**
     * Test {389=[IE11, IE9], 390=[FF24], 392=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__widows___orphans__8936() throws Exception {
        runTest("css: widows & orphans #8936");
    }

    /**
     * Test {390=[IE11, IE9], 391=[FF24], 393=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__can_t_get_css_for_disconnected_in_IE_9__see__10254_and__8388() throws Exception {
        runTest("css: can't get css for disconnected in IE<9, see #10254 and #8388");
    }

    /**
     * Test {391=[IE11, IE9], 392=[FF24], 394=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void css__can_t_get_background_position_in_IE_9__see__10796() throws Exception {
        runTest("css: can't get background-position in IE<9, see #10796");
    }

    /**
     * Test {392=[IE11, IE9], 393=[FF24], 395=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__percentage_properties_for_bottom_and_right_in_IE_9_should_not_be_incorrectly_transformed_to_pixels__see__11311() throws Exception {
        runTest("css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pixels, see #11311");
    }

    /**
     * Test {393=[IE11, IE9], 394=[FF24], 396=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__percentage_properties_for_left_and_top_should_be_transformed_to_pixels__see__9505() throws Exception {
        runTest("css: percentage properties for left and top should be transformed to pixels, see #9505");
    }

    /**
     * Test {394=[IE11, IE9], 395=[FF24], 397=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("css: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {395=[IE11, IE9], 396=[FF24], 398=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__css__width___and_css__height___should_respect_box_sizing__see__11004() throws Exception {
        runTest("css: css('width') and css('height') should respect box-sizing, see #11004");
    }

    /**
     * Test {396=[IE11, IE9], 397=[FF24], 399=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__certain_css_values_of__normal__should_be_convertable_to_a_number__see__8627() throws Exception {
        runTest("css: certain css values of 'normal' should be convertable to a number, see #8627");
    }

    /**
     * Test {397=[IE11, IE9], 398=[FF24], 400=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void css__cssHooks___expand() throws Exception {
        runTest("css: cssHooks - expand");
    }

    /**
     * Test {398=[IE11, IE9], 399=[FF24], 401=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void serialize__jQuery_param__() throws Exception {
        runTest("serialize: jQuery.param()");
    }

    /**
     * Test {399=[IE11, IE9], 400=[FF24], 402=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void serialize__jQuery_param___Constructed_prop_values() throws Exception {
        runTest("serialize: jQuery.param() Constructed prop values");
    }

    /**
     * Test {400=[IE11, IE9], 401=[FF24], 403=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void serialize__serialize__() throws Exception {
        runTest("serialize: serialize()");
    }

    /**
     * Test {401=[IE11, IE9], 402=[FF24], 404=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks");
    }

    /**
     * Test {402=[IE11, IE9], 403=[FF24], 405=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks____url__options__syntax() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks - (url, options) syntax");
    }

    /**
     * Test {403=[IE11, IE9], 404=[FF24], 406=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (late binding)");
    }

    /**
     * Test {404=[IE11, IE9], 405=[FF24], 407=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__oncomplete_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (oncomplete binding)");
    }

    /**
     * Test {405=[IE11, IE9], 406=[FF24], 408=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__very_late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (very late binding)");
    }

    /**
     * Test {406=[IE11, IE9], 407=[FF24], 409=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____success_callbacks__order_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (order)");
    }

    /**
     * Test {407=[IE11, IE9], 408=[FF24], 410=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____error_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - error callbacks");
    }

    /**
     * Test {408=[IE11, IE9], 409=[FF24], 411=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax___multiple_method_signatures_introduced_in_1_5____8107_() throws Exception {
        runTest("ajax: jQuery.ajax - multiple method signatures introduced in 1.5 ( #8107)");
    }

    /**
     * Test {409=[IE11, IE9], 410=[FF24], 412=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_____textStatus_and_errorThrown_values() throws Exception {
        runTest("ajax: jQuery.ajax() - textStatus and errorThrown values");
    }

    /**
     * Test {410=[IE11, IE9], 411=[FF24], 413=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____responseText_on_error() throws Exception {
        runTest("ajax: jQuery.ajax() - responseText on error");
    }

    /**
     * Test {411=[IE11, IE9], 412=[FF24], 414=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: .ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {412=[IE11, IE9], 413=[FF24], 415=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___ajax_____headers() throws Exception {
        runTest("ajax: .ajax() - headers");
    }

    /**
     * Test {413=[IE11, IE9], 414=[FF24], 416=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___ajax_____Accept_header() throws Exception {
        runTest("ajax: .ajax() - Accept header");
    }

    /**
     * Test {414=[IE11, IE9], 415=[FF24], 417=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___ajax_____contentType() throws Exception {
        runTest("ajax: .ajax() - contentType");
    }

    /**
     * Test {415=[IE11, IE9], 416=[FF24], 418=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: .ajax() - protocol-less urls");
    }

    /**
     * Test {416=[IE11, IE9], 417=[FF24], 419=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___ajax_____hash() throws Exception {
        runTest("ajax: .ajax() - hash");
    }

    /**
     * Test {417=[IE11, IE9], 418=[FF24], 420=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax___cross_domain_detection() throws Exception {
        runTest("ajax: jQuery ajax - cross-domain detection");
    }

    /**
     * Test {418=[IE11, IE9], 419=[FF24], 421=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax___load_____404_error_callbacks() throws Exception {
        runTest("ajax: .load() - 404 error callbacks");
    }

    /**
     * Test {419=[IE11, IE9], 420=[FF24], 422=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____abort() throws Exception {
        runTest("ajax: jQuery.ajax() - abort");
    }

    /**
     * Test {420=[IE11, IE9], 421=[FF24], 423=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void ajax__Ajax_events_with_context() throws Exception {
        runTest("ajax: Ajax events with context");
    }

    /**
     * Test {421=[IE11, IE9], 422=[FF24], 424=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_context_modification() throws Exception {
        runTest("ajax: jQuery.ajax context modification");
    }

    /**
     * Test {422=[IE11, IE9], 423=[FF24], 425=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax context modification through ajaxSetup");
    }

    /**
     * Test {423=[IE11, IE9], 424=[FF24], 426=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____disabled_globals() throws Exception {
        runTest("ajax: jQuery.ajax() - disabled globals");
    }

    /**
     * Test {424=[IE11, IE9], 425=[FF24], 427=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {425=[IE11, IE9], 426=[FF24], 428=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {426=[IE11, IE9], 427=[FF24], 429=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2, 0, 2")
    public void ajax__jQuery_ajax___HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax - HEAD requests");
    }

    /**
     * Test {427=[IE11, IE9], 428=[FF24], 430=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend");
    }

    /**
     * Test {428=[IE11, IE9], 429=[FF24], 431=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request___2688_() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request (#2688)");
    }

    /**
     * Test {429=[IE11, IE9], 430=[FF24], 432=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request manually");
    }

    /**
     * Test {430=[IE11, IE9], 431=[FF24], 433=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax___dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax - dataType html");
    }

    /**
     * Test {431=[IE11, IE9], 432=[FF24], 434=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__synchronous_request() throws Exception {
        runTest("ajax: synchronous request");
    }

    /**
     * Test {432=[IE11, IE9], 433=[FF24], 435=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: synchronous request with callbacks");
    }

    /**
     * Test {433=[IE11, IE9], 434=[FF24], 436=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__pass_through_request_object() throws Exception {
        runTest("ajax: pass-through request object");
    }

    /**
     * Test {434=[IE11, IE9], 435=[FF24], 437=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void ajax__ajax_cache() throws Exception {
        runTest("ajax: ajax cache");
    }

    /**
     * Test {435=[IE11, IE9], 436=[FF24], 438=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_() throws Exception {
        runTest("ajax: load(String)");
    }

    /**
     * Test {436=[IE11, IE9], 437=[FF24], 439=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_null_() throws Exception {
        runTest("ajax: load(String,null)");
    }

    /**
     * Test {437=[IE11, IE9], 438=[FF24], 440=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_undefined_() throws Exception {
        runTest("ajax: load(String,undefined)");
    }

    /**
     * Test {438=[IE11, IE9], 439=[FF24], 441=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load__url_selector__() throws Exception {
        runTest("ajax: load('url selector')");
    }

    /**
     * Test {439=[IE11, IE9], 440=[FF24], 442=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load_String__Function__with_ajaxSetup_on_dataType_json__see__2046() throws Exception {
        runTest("ajax: load(String, Function) with ajaxSetup on dataType json, see #2046");
    }

    /**
     * Test {440=[IE11, IE9], 441=[FF24], 443=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Function____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: load(String, Function) - simple: inject text into DOM");
    }

    /**
     * Test {441=[IE11, IE9], 442=[FF24], 444=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void ajax__load_String__Function____check_scripts() throws Exception {
        runTest("ajax: load(String, Function) - check scripts");
    }

    /**
     * Test {442=[IE11, IE9], 443=[FF24], 445=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__load_String__Function____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: load(String, Function) - check file with only a script tag");
    }

    /**
     * Test {443=[IE11, IE9], 444=[FF24], 446=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Function____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: load(String, Function) - dataFilter in ajaxSettings");
    }

    /**
     * Test {444=[IE11, IE9], 445=[FF24], 447=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Object__Function_() throws Exception {
        runTest("ajax: load(String, Object, Function)");
    }

    /**
     * Test {445=[IE11, IE9], 446=[FF24], 448=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__String__Function_() throws Exception {
        runTest("ajax: load(String, String, Function)");
    }

    /**
     * Test {446=[IE11, IE9], 447=[FF24], 449=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load_____data_specified_in_ajaxSettings_is_merged_in___10524_() throws Exception {
        runTest("ajax: load() - data specified in ajaxSettings is merged in (#10524)");
    }

    /**
     * Test {447=[IE11, IE9], 448=[FF24], 450=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: load() - callbacks get the correct parameters");
    }

    /**
     * Test {448=[IE11, IE9], 449=[FF24], 451=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_get_String__Function____data_in_ajaxSettings___8277_() throws Exception {
        runTest("ajax: jQuery.get(String, Function) - data in ajaxSettings (#8277)");
    }

    /**
     * Test {449=[IE11, IE9], 450=[FF24], 452=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_get_String__Hash__Function____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get(String, Hash, Function) - parse xml and use text() on nodes");
    }

    /**
     * Test {450=[IE11, IE9], 451=[FF24], 453=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_getScript_String__Function____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - with callback");
    }

    /**
     * Test {451=[IE11, IE9], 452=[FF24], 454=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_getScript_String__Function____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - no callback");
    }

    /**
     * Test {452=[IE11, IE9], 453=[FF24], 455=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void ajax__jQuery_ajax_____JSONP__Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Same Domain");
    }

    /**
     * Test {453=[IE11, IE9], 454=[FF24], 456=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void ajax__jQuery_ajax_____JSONP__Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Cross Domain");
    }

    /**
     * Test {454=[IE11, IE9], 455=[FF24], 457=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote");
    }

    /**
     * Test {455=[IE11, IE9], 456=[FF24], 458=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____script__Remote_with_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with POST");
    }

    /**
     * Test {456=[IE11, IE9], 457=[FF24], 459=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote_with_scheme_less_URL() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with scheme-less URL");
    }

    /**
     * Test {457=[IE11, IE9], 458=[FF24], 460=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____malformed_JSON() throws Exception {
        runTest("ajax: jQuery.ajax() - malformed JSON");
    }

    /**
     * Test {458=[IE11, IE9], 459=[FF24], 461=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____script__throws_exception___11743_() throws Exception {
        runTest("ajax: jQuery.ajax() - script, throws exception (#11743)");
    }

    /**
     * Test {459=[IE11, IE9], 460=[FF24], 462=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - script by content-type");
    }

    /**
     * Test {460=[IE11, IE9], 461=[FF24], 463=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____json_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type");
    }

    /**
     * Test {461=[IE11, IE9], 462=[FF24], 464=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax_____json_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type disabled with options");
    }

    /**
     * Test {462=[IE11, IE9], 463=[FF24], 465=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_getJSON_String__Hash__Function____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Hash, Function) - JSON array");
    }

    /**
     * Test {463=[IE11, IE9], 464=[FF24], 466=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object");
    }

    /**
     * Test {464=[IE11, IE9], 465=[FF24], 467=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON___Using_Native_JSON() throws Exception {
        runTest("ajax: jQuery.getJSON - Using Native JSON");
    }

    /**
     * Test {465=[IE11, IE9], 466=[FF24], 468=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object with absolute url to local content");
    }

    /**
     * Test {466=[IE11, IE9], 467=[FF24], 469=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_post___data() throws Exception {
        runTest("ajax: jQuery.post - data");
    }

    /**
     * Test {467=[IE11, IE9], 468=[FF24], 470=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_post_String__Hash__Function____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post(String, Hash, Function) - simple with xml");
    }

    /**
     * Test {468=[IE11, IE9], 469=[FF24], 471=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void ajax__jQuery_ajaxSetup__timeout__Number_____with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) - with global timeout");
    }

    /**
     * Test {469=[IE11, IE9], 470=[FF24], 472=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxSetup__timeout__Number___with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) with localtimeout");
    }

    /**
     * Test {470=[IE11, IE9], 471=[FF24], 473=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___simple_get() throws Exception {
        runTest("ajax: jQuery.ajax - simple get");
    }

    /**
     * Test {471=[IE11, IE9], 472=[FF24], 474=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___simple_post() throws Exception {
        runTest("ajax: jQuery.ajax - simple post");
    }

    /**
     * Test {472=[IE11, IE9], 473=[FF24], 475=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__ajaxSetup__() throws Exception {
        runTest("ajax: ajaxSetup()");
    }

    /**
     * Test {473=[IE11, IE9], 474=[FF24], 476=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__data_option__evaluate_function_values___2806_() throws Exception {
        runTest("ajax: data option: evaluate function values (#2806)");
    }

    /**
     * Test {474=[IE11, IE9], 475=[FF24], 477=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__data_option__empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: data option: empty bodies for non-GET requests");
    }

    /**
     * Test {475=[IE11, IE9], 476=[FF24], 478=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (cache)");
    }

    /**
     * Test {476=[IE11, IE9], 477=[FF24], 479=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (cache)");
    }

    /**
     * Test {477=[IE11, IE9], 478=[FF24], 480=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (no cache)");
    }

    /**
     * Test {478=[IE11, IE9], 479=[FF24], 481=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (no cache)");
    }

    /**
     * Test {479=[IE11, IE9], 480=[FF24], 482=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___failing_cross_domain() throws Exception {
        runTest("ajax: jQuery ajax - failing cross-domain");
    }

    /**
     * Test {480=[IE11, IE9], 481=[FF24], 483=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___atom_xml() throws Exception {
        runTest("ajax: jQuery ajax - atom+xml");
    }

    /**
     * Test {481=[IE11, IE9], 482=[FF24], 484=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___Location_object_as_url___7531_() throws Exception {
        runTest("ajax: jQuery.ajax - Location object as url (#7531)");
    }

    /**
     * Test {482=[IE11, IE9], 483=[FF24], 485=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: jQuery.ajax - Context with circular references (#9887)");
    }

    /**
     * Test {483=[IE11, IE9], 484=[FF24], 486=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___statusText() throws Exception {
        runTest("ajax: jQuery.ajax - statusText");
    }

    /**
     * Test {484=[IE11, IE9], 485=[FF24], 487=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void ajax__jQuery_ajax___statusCode() throws Exception {
        runTest("ajax: jQuery.ajax - statusCode");
    }

    /**
     * Test {485=[IE11, IE9], 486=[FF24], 488=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax___transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax - transitive conversions");
    }

    /**
     * Test {486=[IE11, IE9], 487=[FF24], 489=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax - overrideMimeType");
    }

    /**
     * Test {487=[IE11, IE9], 488=[FF24], 490=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___abort_in_prefilter() throws Exception {
        runTest("ajax: jQuery.ajax - abort in prefilter");
    }

    /**
     * Test {488=[IE11, IE9], 489=[FF24], 491=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___loading_binary_data_shouldn_t_throw_an_exception_in_IE___11426_() throws Exception {
        runTest("ajax: jQuery.ajax - loading binary data shouldn't throw an exception in IE (#11426)");
    }

    /**
     * Test {489=[IE11, IE9], 490=[FF24], 492=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_domManip___no_side_effect_because_of_ajaxSetup_or_global_events___11264_() throws Exception {
        runTest("ajax: jQuery.domManip - no side effect because of ajaxSetup or global events (#11264)");
    }

    /**
     * Test {490=[IE11, IE9], 491=[FF24], 493=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_domManip___script_in_comments_are_properly_evaluated___11402_() throws Exception {
        runTest("ajax: jQuery.domManip - script in comments are properly evaluated (#11402)");
    }

    /**
     * Test {491=[IE11, IE9], 492=[FF24], 494=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___active_counter() throws Exception {
        runTest("ajax: jQuery.ajax - active counter");
    }

    /**
     * Test {492=[IE11, IE9], 493=[FF24], 495=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__sanity_check() throws Exception {
        runTest("effects: sanity check");
    }

    /**
     * Test {493=[IE11, IE9], 494=[FF24], 496=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void effects__show__() throws Exception {
        runTest("effects: show()");
    }

    /**
     * Test {494=[IE11, IE9], 495=[FF24], 497=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__show_Number____other_displays() throws Exception {
        runTest("effects: show(Number) - other displays");
    }

    /**
     * Test {495=[IE11, IE9], 496=[FF24], 498=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__Persist_correct_display_value() throws Exception {
        runTest("effects: Persist correct display value");
    }

    /**
     * Test {496=[IE11, IE9], 497=[FF24], 499=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_Hash__Object__Function_() throws Exception {
        runTest("effects: animate(Hash, Object, Function)");
    }

    /**
     * Test {497=[IE11, IE9], 498=[FF24], 500=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented(IE8)
    public void effects__animate_negative_height() throws Exception {
        runTest("effects: animate negative height");
    }

    /**
     * Test {498=[IE11, IE9], 499=[FF24], 501=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin() throws Exception {
        runTest("effects: animate negative margin");
    }

    /**
     * Test {499=[IE11, IE9], 500=[FF24], 502=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin_with_px() throws Exception {
        runTest("effects: animate negative margin with px");
    }

    /**
     * Test {500=[IE11, IE9], 501=[FF24], 503=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void effects__animate_negative_padding() throws Exception {
        runTest("effects: animate negative padding");
    }

    /**
     * Test {501=[IE11, IE9], 502=[FF24], 504=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE8)
    public void effects__animate_block_as_inline_width_height() throws Exception {
        runTest("effects: animate block as inline width/height");
    }

    /**
     * Test {502=[IE11, IE9], 503=[FF24], 505=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE8)
    public void effects__animate_native_inline_width_height() throws Exception {
        runTest("effects: animate native inline width/height");
    }

    /**
     * Test {503=[IE11, IE9], 504=[FF24], 506=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE8)
    public void effects__animate_block_width_height() throws Exception {
        runTest("effects: animate block width/height");
    }

    /**
     * Test {504=[IE11, IE9], 505=[FF24], 507=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_table_width_height() throws Exception {
        runTest("effects: animate table width/height");
    }

    /**
     * Test {505=[IE11, IE9], 506=[FF24], 508=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void effects__animate_table_row_width_height() throws Exception {
        runTest("effects: animate table-row width/height");
    }

    /**
     * Test {506=[IE11, IE9], 507=[FF24], 509=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void effects__animate_table_cell_width_height() throws Exception {
        runTest("effects: animate table-cell width/height");
    }

    /**
     * Test {507=[IE11, IE9], 508=[FF24], 510=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_percentage____on_width_height() throws Exception {
        runTest("effects: animate percentage(%) on width/height");
    }

    /**
     * Test {508=[IE11, IE9], 509=[FF24], 511=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_resets_overflow_x_and_overflow_y_when_finished() throws Exception {
        runTest("effects: animate resets overflow-x and overflow-y when finished");
    }

    /**
     * Test {509=[IE11, IE9], 510=[FF24], 512=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__false__() throws Exception {
        runTest("effects: animate option { queue: false }");
    }

    /**
     * Test {510=[IE11, IE9], 511=[FF24], 513=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__true__() throws Exception {
        runTest("effects: animate option { queue: true }");
    }

    /**
     * Test {511=[IE11, IE9], 512=[FF24], 514=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_option___queue___name___() throws Exception {
        runTest("effects: animate option { queue: 'name' }");
    }

    /**
     * Test {512=[IE11, IE9], 513=[FF24], 515=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_with_no_properties() throws Exception {
        runTest("effects: animate with no properties");
    }

    /**
     * Test {513=[IE11, IE9], 514=[FF24], 516=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_duration_0() throws Exception {
        runTest("effects: animate duration 0");
    }

    /**
     * Test {514=[IE11, IE9], 515=[FF24], 517=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_hyphenated_properties() throws Exception {
        runTest("effects: animate hyphenated properties");
    }

    /**
     * Test {515=[IE11, IE9], 516=[FF24], 518=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_non_element() throws Exception {
        runTest("effects: animate non-element");
    }

    /**
     * Test {516=[IE11, IE9], 517=[FF24], 519=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop__() throws Exception {
        runTest("effects: stop()");
    }

    /**
     * Test {517=[IE11, IE9], 518=[FF24], 520=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop_____several_in_queue() throws Exception {
        runTest("effects: stop() - several in queue");
    }

    /**
     * Test {518=[IE11, IE9], 519=[FF24], 521=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop_clearQueue_() throws Exception {
        runTest("effects: stop(clearQueue)");
    }

    /**
     * Test {519=[IE11, IE9], 520=[FF24], 522=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__stop_clearQueue__gotoEnd_() throws Exception {
        runTest("effects: stop(clearQueue, gotoEnd)");
    }

    /**
     * Test {520=[IE11, IE9], 521=[FF24], 523=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop__queue_______________Stop_single_queues() throws Exception {
        runTest("effects: stop( queue, ..., ... ) - Stop single queues");
    }

    /**
     * Test {521=[IE11, IE9], 522=[FF24], 524=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__toggle__() throws Exception {
        runTest("effects: toggle()");
    }

    /**
     * Test {522=[IE11, IE9], 523=[FF24], 525=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void effects__jQuery_fx_prototype_cur______1_8_Back_Compat() throws Exception {
        runTest("effects: jQuery.fx.prototype.cur() - <1.8 Back Compat");
    }

    /**
     * Test {523=[IE11, IE9], 524=[FF24], 526=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__JS_Overflow_and_Display() throws Exception {
        runTest("effects: JS Overflow and Display");
    }

    /**
     * Test {524=[IE11, IE9], 525=[FF24], 527=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__CSS_Overflow_and_Display() throws Exception {
        runTest("effects: CSS Overflow and Display");
    }

    /**
     * Test {525=[IE11, IE9], 530=[FF24], 532=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_0() throws Exception {
        runTest("effects: CSS Auto to 0");
    }

    /**
     * Test {526=[FF24], 528=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    @NotYetImplemented
    public void effects__CSS_Auto_to_show() throws Exception {
        runTest("effects: CSS Auto to show");
    }

    /**
     * Test {526=[IE11, IE9], 529=[FF24], 531=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_50() throws Exception {
        runTest("effects: CSS Auto to 50");
    }

    /**
     * Test {527=[FF24], 529=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_Auto_to_hide() throws Exception {
        runTest("effects: CSS Auto to hide");
    }

    /**
     * Test {527=[IE11, IE9], 528=[FF24], 530=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_100() throws Exception {
        runTest("effects: CSS Auto to 100");
    }

    /**
     * Test {530=[IE11, IE9], 535=[FF24], 537=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_0() throws Exception {
        runTest("effects: JS Auto to 0");
    }

    /**
     * Test {531=[FF24], 533=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    @NotYetImplemented
    public void effects__JS_Auto_to_show() throws Exception {
        runTest("effects: JS Auto to show");
    }

    /**
     * Test {531=[IE11, IE9], 534=[FF24], 536=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_50() throws Exception {
        runTest("effects: JS Auto to 50");
    }

    /**
     * Test {532=[FF24], 534=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_Auto_to_hide() throws Exception {
        runTest("effects: JS Auto to hide");
    }

    /**
     * Test {532=[IE11, IE9], 533=[FF24], 535=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_100() throws Exception {
        runTest("effects: JS Auto to 100");
    }

    /**
     * Test {535=[IE11, IE9], 540=[FF24], 542=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_0() throws Exception {
        runTest("effects: CSS 100 to 0");
    }

    /**
     * Test {536=[FF24], 538=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_100_to_show() throws Exception {
        runTest("effects: CSS 100 to show");
    }

    /**
     * Test {536=[IE11, IE9], 539=[FF24], 541=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_50() throws Exception {
        runTest("effects: CSS 100 to 50");
    }

    /**
     * Test {537=[FF24], 539=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_100_to_hide() throws Exception {
        runTest("effects: CSS 100 to hide");
    }

    /**
     * Test {537=[IE11, IE9], 538=[FF24], 540=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_100() throws Exception {
        runTest("effects: CSS 100 to 100");
    }

    /**
     * Test {540=[IE11, IE9], 545=[FF24], 547=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_0() throws Exception {
        runTest("effects: JS 100 to 0");
    }

    /**
     * Test {541=[FF24], 543=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_100_to_show() throws Exception {
        runTest("effects: JS 100 to show");
    }

    /**
     * Test {541=[IE11, IE9], 544=[FF24], 546=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_50() throws Exception {
        runTest("effects: JS 100 to 50");
    }

    /**
     * Test {542=[FF24], 544=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_100_to_hide() throws Exception {
        runTest("effects: JS 100 to hide");
    }

    /**
     * Test {542=[IE11, IE9], 543=[FF24], 545=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_100() throws Exception {
        runTest("effects: JS 100 to 100");
    }

    /**
     * Test {545=[IE11, IE9], 550=[FF24], 552=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_0() throws Exception {
        runTest("effects: CSS 50 to 0");
    }

    /**
     * Test {546=[FF24], 548=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_50_to_show() throws Exception {
        runTest("effects: CSS 50 to show");
    }

    /**
     * Test {546=[IE11, IE9], 549=[FF24], 551=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_50() throws Exception {
        runTest("effects: CSS 50 to 50");
    }

    /**
     * Test {547=[FF24], 549=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_50_to_hide() throws Exception {
        runTest("effects: CSS 50 to hide");
    }

    /**
     * Test {547=[IE11, IE9], 548=[FF24], 550=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_100() throws Exception {
        runTest("effects: CSS 50 to 100");
    }

    /**
     * Test {550=[IE11, IE9], 555=[FF24], 557=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_0() throws Exception {
        runTest("effects: JS 50 to 0");
    }

    /**
     * Test {551=[FF24], 553=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_50_to_show() throws Exception {
        runTest("effects: JS 50 to show");
    }

    /**
     * Test {551=[IE11, IE9], 554=[FF24], 556=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_50() throws Exception {
        runTest("effects: JS 50 to 50");
    }

    /**
     * Test {552=[FF24], 554=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_50_to_hide() throws Exception {
        runTest("effects: JS 50 to hide");
    }

    /**
     * Test {552=[IE11, IE9], 553=[FF24], 555=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_100() throws Exception {
        runTest("effects: JS 50 to 100");
    }

    /**
     * Test {555=[IE11, IE9], 560=[FF24], 562=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_0() throws Exception {
        runTest("effects: CSS 0 to 0");
    }

    /**
     * Test {556=[FF24], 558=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_0_to_show() throws Exception {
        runTest("effects: CSS 0 to show");
    }

    /**
     * Test {556=[IE11, IE9], 559=[FF24], 561=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_50() throws Exception {
        runTest("effects: CSS 0 to 50");
    }

    /**
     * Test {557=[FF24], 559=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_0_to_hide() throws Exception {
        runTest("effects: CSS 0 to hide");
    }

    /**
     * Test {557=[IE11, IE9], 558=[FF24], 560=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_100() throws Exception {
        runTest("effects: CSS 0 to 100");
    }

    /**
     * Test {560=[IE11, IE9], 565=[FF24], 567=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_0() throws Exception {
        runTest("effects: JS 0 to 0");
    }

    /**
     * Test {561=[FF24], 563=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_0_to_show() throws Exception {
        runTest("effects: JS 0 to show");
    }

    /**
     * Test {561=[IE11, IE9], 564=[FF24], 566=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_50() throws Exception {
        runTest("effects: JS 0 to 50");
    }

    /**
     * Test {562=[FF24], 564=[IE11, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_0_to_hide() throws Exception {
        runTest("effects: JS 0 to hide");
    }

    /**
     * Test {562=[IE11, IE9], 563=[FF24], 565=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_100() throws Exception {
        runTest("effects: JS 0 to 100");
    }

    /**
     * Test {565=[IE11, IE9], 566=[FF24], 568=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeOut_fadeIn() throws Exception {
        runTest("effects: Chain fadeOut fadeIn");
    }

    /**
     * Test {566=[IE11, IE9], 567=[FF24], 569=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeIn_fadeOut() throws Exception {
        runTest("effects: Chain fadeIn fadeOut");
    }

    /**
     * Test {567=[IE11, IE9], 568=[FF24], 570=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_hide_show() throws Exception {
        runTest("effects: Chain hide show");
    }

    /**
     * Test {568=[IE11, IE9], 569=[FF24], 571=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_show_hide() throws Exception {
        runTest("effects: Chain show hide");
    }

    /**
     * Test {569=[IE11, IE9], 570=[FF24], 572=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_show_hide_with_easing_and_callback() throws Exception {
        runTest("effects: Chain show hide with easing and callback");
    }

    /**
     * Test {570=[IE11, IE9], 571=[FF24], 573=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_in() throws Exception {
        runTest("effects: Chain toggle in");
    }

    /**
     * Test {571=[IE11, IE9], 572=[FF24], 574=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_out() throws Exception {
        runTest("effects: Chain toggle out");
    }

    /**
     * Test {572=[IE11, IE9], 573=[FF24], 575=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_out_with_easing_and_callback() throws Exception {
        runTest("effects: Chain toggle out with easing and callback");
    }

    /**
     * Test {573=[IE11, IE9], 574=[FF24], 576=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideDown_slideUp() throws Exception {
        runTest("effects: Chain slideDown slideUp");
    }

    /**
     * Test {574=[IE11, IE9], 575=[FF24], 577=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideUp_slideDown() throws Exception {
        runTest("effects: Chain slideUp slideDown");
    }

    /**
     * Test {575=[IE11, IE9], 576=[FF24], 578=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideUp_slideDown_with_easing_and_callback() throws Exception {
        runTest("effects: Chain slideUp slideDown with easing and callback");
    }

    /**
     * Test {576=[IE11, IE9], 577=[FF24], 579=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideToggle_in() throws Exception {
        runTest("effects: Chain slideToggle in");
    }

    /**
     * Test {577=[IE11, IE9], 578=[FF24], 580=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideToggle_out() throws Exception {
        runTest("effects: Chain slideToggle out");
    }

    /**
     * Test {578=[IE11, IE9], 579=[FF24], 581=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeToggle_in() throws Exception {
        runTest("effects: Chain fadeToggle in");
    }

    /**
     * Test {579=[IE11, IE9], 580=[FF24], 582=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeToggle_out() throws Exception {
        runTest("effects: Chain fadeToggle out");
    }

    /**
     * Test {580=[IE11, IE9], 581=[FF24], 583=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeTo_0_5_1_0_with_easing_and_callback_() throws Exception {
        runTest("effects: Chain fadeTo 0.5 1.0 with easing and callback)");
    }

    /**
     * Test {581=[IE11, IE9], 582=[FF24], 584=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_show__fast___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("effects: jQuery.show('fast') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {582=[IE11, IE9], 583=[FF24], 585=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    @NotYetImplemented(IE8)
    public void effects__slideToggle___stop___slideToggle__() throws Exception {
        runTest("effects: slideToggle().stop().slideToggle()");
    }

    /**
     * Test {583=[IE11, IE9], 584=[FF24], 586=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__fadeToggle___stop___fadeToggle__() throws Exception {
        runTest("effects: fadeToggle().stop().fadeToggle()");
    }

    /**
     * Test {584=[IE11, IE9], 585=[FF24], 587=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__toggle___stop___toggle__() throws Exception {
        runTest("effects: toggle().stop().toggle()");
    }

    /**
     * Test {585=[IE11, IE9], 586=[FF24], 588=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_with_per_property_easing() throws Exception {
        runTest("effects: animate with per-property easing");
    }

    /**
     * Test {586=[IE11, IE9], 587=[FF24], 589=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_with_CSS_shorthand_properties() throws Exception {
        runTest("effects: animate with CSS shorthand properties");
    }

    /**
     * Test {587=[IE11, IE9], 588=[FF24], 590=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_hidden_elements__with_animation__bug__7141_() throws Exception {
        runTest("effects: hide hidden elements, with animation (bug #7141)");
    }

    /**
     * Test {588=[IE11, IE9], 589=[FF24], 591=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_unit_less_properties___4966_() throws Exception {
        runTest("effects: animate unit-less properties (#4966)");
    }

    /**
     * Test {589=[IE11, IE9], 590=[FF24], 592=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__animate_properties_missing_px_w__opacity_as_last___9074_() throws Exception {
        runTest("effects: animate properties missing px w/ opacity as last (#9074)");
    }

    /**
     * Test {590=[IE11, IE9], 591=[FF24], 593=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__callbacks_should_fire_in_correct_order___9100_() throws Exception {
        runTest("effects: callbacks should fire in correct order (#9100)");
    }

    /**
     * Test {591=[IE11, IE9], 592=[FF24], 594=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__callbacks_that_throw_exceptions_will_be_removed___5684_() throws Exception {
        runTest("effects: callbacks that throw exceptions will be removed (#5684)");
    }

    /**
     * Test {592=[IE11, IE9], 593=[FF24], 595=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_will_scale_margin_properties_individually() throws Exception {
        runTest("effects: animate will scale margin properties individually");
    }

    /**
     * Test {593=[IE11, IE9], 594=[FF24], 596=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("effects: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {594=[IE11, IE9], 595=[FF24], 597=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_Animation__object__props__opts__() throws Exception {
        runTest("effects: jQuery.Animation( object, props, opts )");
    }

    /**
     * Test {595=[IE11, IE9], 596=[FF24], 598=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animate_Option__step__function__percent__tween__() throws Exception {
        runTest("effects: Animate Option: step: function( percent, tween )");
    }

    /**
     * Test {596=[IE11, IE9], 597=[FF24], 599=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__Animate_callbacks_have_correct_context() throws Exception {
        runTest("effects: Animate callbacks have correct context");
    }

    /**
     * Test {597=[IE11, IE9], 598=[FF24], 600=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__User_supplied_callback_called_after_show_when_fx_off___8892_() throws Exception {
        runTest("effects: User supplied callback called after show when fx off (#8892)");
    }

    /**
     * Test {598=[IE11, IE9], 599=[FF24], 601=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void effects__animate_should_set_display_for_disconnected_nodes() throws Exception {
        runTest("effects: animate should set display for disconnected nodes");
    }

    /**
     * Test {599=[IE11, IE9], 600=[FF24], 602=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animation_callback_should_not_show_animated_element_as_animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as animated (#7157)");
    }

    /**
     * Test {600=[IE11, IE9], 601=[FF24], 603=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_called_on_element_within_hidden_parent_should_set_display_to_none___10045_() throws Exception {
        runTest("effects: hide called on element within hidden parent should set display to none (#10045)");
    }

    /**
     * Test {601=[IE11, IE9], 602=[FF24], 604=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__hide__fadeOut_and_slideUp_called_on_element_width_height_and_width___0_should_set_display_to_none() throws Exception {
        runTest("effects: hide, fadeOut and slideUp called on element width height and width = 0 should set display to none");
    }

    /**
     * Test {602=[IE11, IE9], 603=[FF24], 605=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void effects__Handle_queue_false_promises() throws Exception {
        runTest("effects: Handle queue:false promises");
    }

    /**
     * Test {603=[IE11, IE9], 604=[FF24], 606=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__multiple_unqueued_and_promise() throws Exception {
        runTest("effects: multiple unqueued and promise");
    }

    /**
     * Test {604=[IE11, IE9], 605=[FF24], 607=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void effects__animate_does_not_change_start_value_for_non_px_animation___7109_() throws Exception {
        runTest("effects: animate does not change start value for non-px animation (#7109)");
    }

    /**
     * Test {605=[IE11, IE9], 606=[FF24], 608=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF24 = "0, 1, 1",
        IE11 = "0, 1, 1",
        IE8 = "0, 2, 2")
    public void effects__non_px_animation_handles_non_numeric_start___11971_() throws Exception {
        runTest("effects: non-px animation handles non-numeric start (#11971)");
    }

    /**
     * Test {606=[IE11, IE9], 607=[FF24], 609=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__Animation_callbacks___11797_() throws Exception {
        runTest("effects: Animation callbacks (#11797)");
    }

    /**
     * Test {607=[IE11, IE9], 608=[FF24], 610=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__Animate_properly_sets_overflow_hidden_when_animating_width_height___12117_() throws Exception {
        runTest("effects: Animate properly sets overflow hidden when animating width/height (#12117)");
    }

    /**
     * Test {608=[IE11, IE9], 609=[FF24], 611=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animations_with_0_duration_don_t_ease___12273_() throws Exception {
        runTest("effects: Animations with 0 duration don't ease (#12273)");
    }

    /**
     * Test {609=[IE11, IE9], 610=[FF24], 612=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__empty_set() throws Exception {
        runTest("offset: empty set");
    }

    /**
     * Test {610=[IE11, IE9], 611=[FF24], 613=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__object_without_getBoundingClientRect() throws Exception {
        runTest("offset: object without getBoundingClientRect");
    }

    /**
     * Test {611=[IE11, IE9], 612=[FF24], 614=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__disconnected_node() throws Exception {
        runTest("offset: disconnected node");
    }

    /**
     * Test {613=[IE11, IE9], 614=[FF24], 616=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "0, 178, 178",
            IE11 = "0, 4, 4")
    @NotYetImplemented
    public void offset__absolute() throws Exception {
        runTest("offset: absolute");
    }

    /**
     * Test {614=[IE11, IE9], 615=[FF24], 617=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 60, 60")
    @NotYetImplemented
    public void offset__relative() throws Exception {
        runTest("offset: relative");
    }

    /**
     * Test {615=[IE11, IE9], 616=[FF24], 618=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 80, 80")
    @NotYetImplemented
    public void offset__static() throws Exception {
        runTest("offset: static");
    }

    /**
     * Test {616=[IE11, IE9], 617=[FF24], 619=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    @NotYetImplemented
    public void offset__fixed() throws Exception {
        runTest("offset: fixed");
    }

    /**
     * Test {617=[IE11, IE9], 618=[FF24], 620=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @NotYetImplemented
    public void offset__table() throws Exception {
        runTest("offset: table");
    }

    /**
     * Test {618=[IE11, IE9], 619=[FF24], 621=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    @NotYetImplemented
    public void offset__scroll() throws Exception {
        runTest("offset: scroll");
    }

    /**
     * Test {619=[IE11, IE9], 620=[FF24], 622=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void offset__body() throws Exception {
        runTest("offset: body");
    }

    /**
     * Test {620=[IE11, IE9], 621=[FF24], 623=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void offset__chaining() throws Exception {
        runTest("offset: chaining");
    }

    /**
     * Test {621=[IE11, IE9], 622=[FF24], 624=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void offset__offsetParent() throws Exception {
        runTest("offset: offsetParent");
    }

    /**
     * Test {622=[IE11, IE9], 623=[FF24], 625=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__fractions__see__7730_and__7885_() throws Exception {
        runTest("offset: fractions (see #7730 and #7885)");
    }

    /**
     * Test {623=[IE11, IE9], 624=[FF24], 626=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width__() throws Exception {
        runTest("dimensions: width()");
    }

    /**
     * Test {624=[IE11, IE9], 625=[FF24], 627=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width_Function_() throws Exception {
        runTest("dimensions: width(Function)");
    }

    /**
     * Test {625=[IE11, IE9], 626=[FF24], 628=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__width_Function_args__() throws Exception {
        runTest("dimensions: width(Function(args))");
    }

    /**
     * Test {626=[IE11, IE9], 627=[FF24], 629=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height__() throws Exception {
        runTest("dimensions: height()");
    }

    /**
     * Test {627=[IE11, IE9], 628=[FF24], 630=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height_Function_() throws Exception {
        runTest("dimensions: height(Function)");
    }

    /**
     * Test {628=[IE11, IE9], 629=[FF24], 631=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__height_Function_args__() throws Exception {
        runTest("dimensions: height(Function(args))");
    }

    /**
     * Test {629=[IE11, IE9], 630=[FF24], 632=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerWidth__() throws Exception {
        runTest("dimensions: innerWidth()");
    }

    /**
     * Test {630=[IE11, IE9], 631=[FF24], 633=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerHeight__() throws Exception {
        runTest("dimensions: innerHeight()");
    }

    /**
     * Test {631=[IE11, IE9], 632=[FF24], 634=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerWidth__() throws Exception {
        runTest("dimensions: outerWidth()");
    }

    /**
     * Test {632=[IE11, IE9], 633=[FF24], 635=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__9441__9300() throws Exception {
        runTest("dimensions: child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #9441 #9300");
    }

    /**
     * Test {633=[IE11, IE9], 634=[FF24], 636=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void dimensions__getting_dimensions_shouldnt_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldnt modify runtimeStyle see #9233");
    }

    /**
     * Test {634=[IE11, IE9], 635=[FF24], 637=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__table_dimensions() throws Exception {
        runTest("dimensions: table dimensions");
    }

    /**
     * Test {635=[IE11, IE9], 636=[FF24], 638=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__box_sizing_border_box_child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__10413() throws Exception {
        runTest("dimensions: box-sizing:border-box child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #10413");
    }

    /**
     * Test {636=[IE11, IE9], 637=[FF24], 639=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerHeight__() throws Exception {
        runTest("dimensions: outerHeight()");
    }

    /**
     * Test {637=[IE11, IE9], 638=[FF24], 640=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void dimensions__passing_undefined_is_a_setter__5571() throws Exception {
        runTest("dimensions: passing undefined is a setter #5571");
    }

    /**
     * Test {638=[IE11, IE9], 639=[FF24], 641=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void dimensions__getters_on_non_elements_should_return_null() throws Exception {
        runTest("dimensions: getters on non elements should return null");
    }

    /**
     * Test {639=[IE11, IE9], 640=[FF24], 642=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void dimensions__setters_with_and_without_box_sizing_border_box() throws Exception {
        runTest("dimensions: setters with and without box-sizing:border-box");
    }

    /**
     * Test {640=[IE11, IE9], 641=[FF24], 643=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void dimensions__window_vs__small_document() throws Exception {
        runTest("dimensions: window vs. small document");
    }

    /**
     * Test {641=[IE11, IE9], 642=[FF24], 644=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void dimensions__window_vs__large_document() throws Exception {
        runTest("dimensions: window vs. large document");
    }

    /**
     * Test {642=[IE11, IE9], 643=[FF24], 645=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 544, 544")
    public void deprecated__browser() throws Exception {
        runTest("deprecated: browser");
    }

    /**
     * Test {643=[IE11, IE9], 644=[FF24], 646=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void exports__amdModule() throws Exception {
        runTest("exports: amdModule");
    }

    /**
     * Test {644=[IE11, IE9], 645=[FF24], 647=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 36, 36")
    public void Sizzle__selector__element() throws Exception {
        runTest("Sizzle: selector: element");
    }

    /**
     * Test {645=[IE11, IE9], 646=[FF24], 648=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    @NotYetImplemented
    public void Sizzle__selector__XML_Document_Selectors() throws Exception {
        runTest("Sizzle: selector: XML Document Selectors");
    }

    /**
     * Test {646=[IE11, IE9], 647=[FF24], 649=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void Sizzle__selector__broken() throws Exception {
        runTest("Sizzle: selector: broken");
    }

    /**
     * Test {647=[IE11, IE9], 648=[FF24], 650=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void Sizzle__selector__id() throws Exception {
        runTest("Sizzle: selector: id");
    }

    /**
     * Test {648=[IE11, IE9], 649=[FF24], 651=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void Sizzle__selector__class() throws Exception {
        runTest("Sizzle: selector: class");
    }

    /**
     * Test {649=[IE11, IE9], 650=[FF24], 652=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void Sizzle__selector__name() throws Exception {
        runTest("Sizzle: selector: name");
    }

    /**
     * Test {650=[IE11, IE9], 651=[FF24], 653=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void Sizzle__selector__multiple() throws Exception {
        runTest("Sizzle: selector: multiple");
    }

    /**
     * Test {651=[IE11, IE9], 652=[FF24], 654=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    public void Sizzle__selector__child_and_adjacent() throws Exception {
        runTest("Sizzle: selector: child and adjacent");
    }

    /**
     * Test {652=[IE11, IE9], 653=[FF24], 655=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 62, 62")
    public void Sizzle__selector__attributes() throws Exception {
        runTest("Sizzle: selector: attributes");
    }

    /**
     * Test {653=[IE11, IE9], 654=[FF24], 656=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    public void Sizzle__selector__pseudo___child() throws Exception {
        runTest("Sizzle: selector: pseudo - child");
    }

    /**
     * Test {654=[IE11, IE9], 655=[FF24], 657=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    @NotYetImplemented
    public void Sizzle__selector__pseudo___misc() throws Exception {
        runTest("Sizzle: selector: pseudo - misc");
    }

    /**
     * Test {655=[IE11, IE9], 656=[FF24], 658=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 43, 43")
    public void Sizzle__selector__pseudo____not() throws Exception {
        runTest("Sizzle: selector: pseudo - :not");
    }

    /**
     * Test {656=[IE11, IE9], 657=[FF24], 659=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 33, 33")
    public void Sizzle__selector__pseudo___position() throws Exception {
        runTest("Sizzle: selector: pseudo - position");
    }

    /**
     * Test {657=[IE11, IE9], 658=[FF24], 660=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void Sizzle__selector__pseudo___form() throws Exception {
        runTest("Sizzle: selector: pseudo - form");
    }

    /**
     * Test {658=[IE11, IE9], 659=[FF24], 661=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void Sizzle__selector__caching() throws Exception {
        runTest("Sizzle: selector: caching");
    }

    /**
     * Test {659=[IE11, IE9], 660=[FF24], 662=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void Sizzle__selector__Sizzle_contains() throws Exception {
        runTest("Sizzle: selector: Sizzle.contains");
    }
}
