/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF17;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF3_6;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE6;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE7;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE9;
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
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
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
        final String testResults = loadExpectation("/libraries/jQuery/1.8.2/expectations/results", ".txt");
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
     * Test {1=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__Unit_Testing_Environment() throws Exception {
        runTest("core: Unit Testing Environment");
    }

    /**
     * Test {2=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__Basic_requirements() throws Exception {
        runTest("core: Basic requirements");
    }

    /**
     * Test {3=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void core__jQuery__() throws Exception {
        runTest("core: jQuery()");
    }

    /**
     * Test {4=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void core__selector_state() throws Exception {
        runTest("core: selector state");
    }

    /**
     * Test {5=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__globalEval() throws Exception {
        runTest("core: globalEval");
    }

    /**
     * Test {6=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__noConflict() throws Exception {
        runTest("core: noConflict");
    }

    /**
     * Test {7=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void core__trim() throws Exception {
        runTest("core: trim");
    }

    /**
     * Test {8=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void core__type() throws Exception {
        runTest("core: type");
    }

    /**
     * Test {9=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void core__isPlainObject() throws Exception {
        runTest("core: isPlainObject");
    }

    /**
     * Test {10=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__isFunction() throws Exception {
        runTest("core: isFunction");
    }

    /**
     * Test {11=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 36, 36")
    public void core__isNumeric() throws Exception {
        runTest("core: isNumeric");
    }

    /**
     * Test {12=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__isXMLDoc___HTML() throws Exception {
        runTest("core: isXMLDoc - HTML");
    }

    /**
     * Test {13=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__XSS_via_location_hash() throws Exception {
        runTest("core: XSS via location.hash");
    }

    /**
     * Test {14=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__isXMLDoc___XML() throws Exception {
        runTest("core: isXMLDoc - XML");
    }

    /**
     * Test {15=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__isWindow() throws Exception {
        runTest("core: isWindow");
    }

    /**
     * Test {16=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery__html__() throws Exception {
        runTest("core: jQuery('html')");
    }

    /**
     * Test {17=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__jQuery__html___context_() throws Exception {
        runTest("core: jQuery('html', context)");
    }

    /**
     * Test {18=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_selector__xml__text_str____Loaded_via_XML_document() throws Exception {
        runTest("core: jQuery(selector, xml).text(str) - Loaded via XML document");
    }

    /**
     * Test {19=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__end__() throws Exception {
        runTest("core: end()");
    }

    /**
     * Test {20=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__length() throws Exception {
        runTest("core: length");
    }

    /**
     * Test {21=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__size__() throws Exception {
        runTest("core: size()");
    }

    /**
     * Test {22=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__get__() throws Exception {
        runTest("core: get()");
    }

    /**
     * Test {23=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__toArray__() throws Exception {
        runTest("core: toArray()");
    }

    /**
     * Test {24=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__inArray__() throws Exception {
        runTest("core: inArray()");
    }

    /**
     * Test {25=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get_Number_() throws Exception {
        runTest("core: get(Number)");
    }

    /**
     * Test {26=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get__Number_() throws Exception {
        runTest("core: get(-Number)");
    }

    /**
     * Test {27=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__each_Function_() throws Exception {
        runTest("core: each(Function)");
    }

    /**
     * Test {28=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__slice__() throws Exception {
        runTest("core: slice()");
    }

    /**
     * Test {29=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__first___last__() throws Exception {
        runTest("core: first()/last()");
    }

    /**
     * Test {30=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__map__() throws Exception {
        runTest("core: map()");
    }

    /**
     * Test {31=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_merge__() throws Exception {
        runTest("core: jQuery.merge()");
    }

    /**
     * Test {32=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__jQuery_extend_Object__Object_() throws Exception {
        runTest("core: jQuery.extend(Object, Object)");
    }

    /**
     * Test {33=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__jQuery_each_Object_Function_() throws Exception {
        runTest("core: jQuery.each(Object,Function)");
    }

    /**
     * Test {34=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void core__jQuery_makeArray() throws Exception {
        runTest("core: jQuery.makeArray");
    }

    /**
     * Test {35=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery_inArray() throws Exception {
        runTest("core: jQuery.inArray");
    }

    /**
     * Test {36=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_isEmptyObject() throws Exception {
        runTest("core: jQuery.isEmptyObject");
    }

    /**
     * Test {37=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_proxy() throws Exception {
        runTest("core: jQuery.proxy");
    }

    /**
     * Test {38=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void core__jQuery_parseHTML() throws Exception {
        runTest("core: jQuery.parseHTML");
    }

    /**
     * Test {39=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseJSON() throws Exception {
        runTest("core: jQuery.parseJSON");
    }

    /**
     * Test {40=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseXML() throws Exception {
        runTest("core: jQuery.parseXML");
    }

    /**
     * Test {41=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery_sub_____Static_Methods() throws Exception {
        runTest("core: jQuery.sub() - Static Methods");
    }

    /**
     * Test {42=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 378, 378")
    public void core__jQuery_sub______fn_Methods() throws Exception {
        runTest("core: jQuery.sub() - .fn Methods");
    }

    /**
     * Test {43=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_camelCase__() throws Exception {
        runTest("core: jQuery.camelCase()");
    }

    /**
     * Test {44=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - no filter");
    }

    /**
     * Test {45=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks__________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - no filter");
    }

    /**
     * Test {46=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - filter");
    }

    /**
     * Test {47=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks__________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - filter");
    }

    /**
     * Test {48=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - no filter");
    }

    /**
     * Test {49=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - no filter");
    }

    /**
     * Test {50=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - filter");
    }

    /**
     * Test {51=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - filter");
    }

    /**
     * Test {52=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - no filter");
    }

    /**
     * Test {53=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - no filter");
    }

    /**
     * Test {54=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - filter");
    }

    /**
     * Test {55=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - filter");
    }

    /**
     * Test {56=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - no filter");
    }

    /**
     * Test {57=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - no filter");
    }

    /**
     * Test {58=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - filter");
    }

    /**
     * Test {59=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - filter");
    }

    /**
     * Test {60=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - no filter");
    }

    /**
     * Test {61=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {62=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - filter");
    }

    /**
     * Test {63=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {64=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - no filter");
    }

    /**
     * Test {65=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - no filter");
    }

    /**
     * Test {66=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - filter");
    }

    /**
     * Test {67=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - filter");
    }

    /**
     * Test {68=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - no filter");
    }

    /**
     * Test {69=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {70=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - filter");
    }

    /**
     * Test {71=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {72=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - no filter");
    }

    /**
     * Test {73=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {74=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - filter");
    }

    /**
     * Test {75=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {76=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - no filter");
    }

    /**
     * Test {77=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {78=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - filter");
    }

    /**
     * Test {79=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {80=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - no filter");
    }

    /**
     * Test {81=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {82=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - filter");
    }

    /**
     * Test {83=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {84=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - no filter");
    }

    /**
     * Test {85=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {86=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - filter");
    }

    /**
     * Test {87=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {88=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks__options_____options_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks( options ) - options are copied");
    }

    /**
     * Test {89=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_fireWith___arguments_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks.fireWith - arguments are copied");
    }

    /**
     * Test {90=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_remove___should_remove_all_instances() throws Exception {
        runTest("callbacks: jQuery.Callbacks.remove - should remove all instances");
    }

    /**
     * Test {91=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_____adding_a_string_doesn_t_cause_a_stack_overflow() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow");
    }

    /**
     * Test {92=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred() throws Exception {
        runTest("deferred: jQuery.Deferred");
    }

    /**
     * Test {93=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred___new_operator() throws Exception {
        runTest("deferred: jQuery.Deferred - new operator");
    }

    /**
     * Test {94=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void deferred__jQuery_Deferred___chainability() throws Exception {
        runTest("deferred: jQuery.Deferred - chainability");
    }

    /**
     * Test {95=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (done)");
    }

    /**
     * Test {96=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (fail)");
    }

    /**
     * Test {97=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___filtering__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (progress)");
    }

    /**
     * Test {98=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (done)");
    }

    /**
     * Test {99=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (fail)");
    }

    /**
     * Test {100=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (progress)");
    }

    /**
     * Test {101=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___context() throws Exception {
        runTest("deferred: jQuery.Deferred.then - context");
    }

    /**
     * Test {102=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 34, 34")
    public void deferred__jQuery_when() throws Exception {
        runTest("deferred: jQuery.when");
    }

    /**
     * Test {103=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 119, 119")
    public void deferred__jQuery_when___joined() throws Exception {
        runTest("deferred: jQuery.when - joined");
    }

    /**
     * Test {104=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__boxModel() throws Exception {
        runTest("support: boxModel");
    }

    /**
     * Test {105=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF10 = "0, 2, 2",
        FF17 = "0, 2, 2",
        FF3_6 = "0, 2, 2",
        IE6 = "9, 1, 10",
        IE7 = "9, 1, 10",
        IE8 = "9, 1, 10",
        IE9 = "0, 2, 2")
    @NotYetImplemented({ IE6, IE7, IE8 })
    public void support__body_background_is_not_lost_if_set_prior_to_loading_jQuery___9238_() throws Exception {
        runTest("support: body background is not lost if set prior to loading jQuery (#9238)");
    }

    /**
     * Test {106=[FF10, FF17, FF3_6, IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__A_background_on_the_testElement_does_not_cause_IE8_to_crash___9823_() throws Exception {
        runTest("support: A background on the testElement does not cause IE8 to crash (#9823)");
    }

    /**
     * Test {107=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "0, 30, 30",
        IE6 = "6, 24, 30",
        IE7 = "6, 24, 30",
        IE8 = "6, 24, 30")
    @NotYetImplemented({ IE6, IE7, IE8 })
    public void support__Verify_that_the_support_tests_resolve_as_expected_per_browser() throws Exception {
        runTest("support: Verify that the support tests resolve as expected per browser");
    }

    /**
     * Test {107=[FF10, FF17, IE9], 108=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__expando() throws Exception {
        runTest("data: expando");
    }

    /**
     * Test {108=[FF10, FF17, IE9], 109=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 124, 124")
    public void data__jQuery_data() throws Exception {
        runTest("data: jQuery.data");
    }

    /**
     * Test {109=[FF10, FF17, IE9], 110=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void data__jQuery_acceptData() throws Exception {
        runTest("data: jQuery.acceptData");
    }

    /**
     * Test {110=[FF10, FF17, IE9], 111=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void data___data__() throws Exception {
        runTest("data: .data()");
    }

    /**
     * Test {111=[FF10, FF17, IE9], 112=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void data___data_String__and__data_String__Object_() throws Exception {
        runTest("data: .data(String) and .data(String, Object)");
    }

    /**
     * Test {112=[FF10, FF17, IE9], 113=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 40, 40")
    public void data__data___attributes() throws Exception {
        runTest("data: data-* attributes");
    }

    /**
     * Test {113=[FF10, FF17, IE9], 114=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data___data_Object_() throws Exception {
        runTest("data: .data(Object)");
    }

    /**
     * Test {114=[FF10, FF17, IE9], 115=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_removeData() throws Exception {
        runTest("data: jQuery.removeData");
    }

    /**
     * Test {115=[FF10, FF17, IE9], 116=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void data___removeData__() throws Exception {
        runTest("data: .removeData()");
    }

    /**
     * Test {116=[FF10, FF17, IE9], 117=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_serialization___8108_() throws Exception {
        runTest("data: JSON serialization (#8108)");
    }

    /**
     * Test {117=[FF10, FF17, IE9], 118=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: jQuery.data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {118=[FF10, FF17, IE9], 119=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__jQuery_data_should_not_miss_data_with_preset_hyphenated_property_names() throws Exception {
        runTest("data: jQuery.data should not miss data with preset hyphenated property names");
    }

    /**
     * Test {119=[FF10, FF17, IE9], 120=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void data__jQuery_data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {120=[FF10, FF17, IE9], 121=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void data__jQuery_data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: jQuery.data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {121=[FF10, FF17, IE9], 122=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__Triggering_the_removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: Triggering the removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {122=[FF10, FF17, IE9], 123=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__Only_check_element_attributes_once_when_calling__data______8909() throws Exception {
        runTest("data: Only check element attributes once when calling .data() - #8909");
    }

    /**
     * Test {123=[FF10, FF17, IE9], 124=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_data__attributes_can_have_newlines() throws Exception {
        runTest("data: JSON data- attributes can have newlines");
    }

    /**
     * Test {124=[FF10, FF17, IE9], 125=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void queue__queue___with_other_types() throws Exception {
        runTest("queue: queue() with other types");
    }

    /**
     * Test {125=[FF10, FF17, IE9], 126=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_name__passes_in_the_next_item_in_the_queue_as_a_parameter() throws Exception {
        runTest("queue: queue(name) passes in the next item in the queue as a parameter");
    }

    /**
     * Test {126=[FF10, FF17, IE9], 127=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__queue___passes_in_the_next_item_in_the_queue_as_a_parameter_to_fx_queues() throws Exception {
        runTest("queue: queue() passes in the next item in the queue as a parameter to fx queues");
    }

    /**
     * Test {127=[FF10, FF17, IE9], 128=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__callbacks_keep_their_place_in_the_queue() throws Exception {
        runTest("queue: callbacks keep their place in the queue");
    }

    /**
     * Test {128=[FF10, FF17, IE9], 129=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__delay__() throws Exception {
        runTest("queue: delay()");
    }

    /**
     * Test {129=[FF10, FF17, IE9], 130=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__clearQueue_name__clears_the_queue() throws Exception {
        runTest("queue: clearQueue(name) clears the queue");
    }

    /**
     * Test {130=[FF10, FF17, IE9], 131=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void queue__clearQueue___clears_the_fx_queue() throws Exception {
        runTest("queue: clearQueue() clears the fx queue");
    }

    /**
     * Test {131=[FF10, FF17, IE9], 132=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__fn_promise_____called_when_fx_queue_is_empty() throws Exception {
        runTest("queue: fn.promise() - called when fx queue is empty");
    }

    /**
     * Test {132=[FF10, FF17, IE9], 133=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__fn_promise___queue______called_whenever_last_queue_function_is_dequeued() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued");
    }

    /**
     * Test {133=[FF10, FF17, IE9], 134=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__fn_promise___queue______waits_for_animation_to_complete_before_resolving() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving");
    }

    /**
     * Test {134=[FF10, FF17, IE9], 135=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue___promise_obj_() throws Exception {
        runTest("queue: .promise(obj)");
    }

    /**
     * Test {135=[FF10, FF17, IE9], 136=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF10 = "2, 3, 5",
        FF17 = "0, 3, 3",
        FF3_6 = "2, 3, 5",
        IE6 = "0, 3, 3",
        IE7 = "0, 3, 3",
        IE8 = "0, 3, 3",
        IE9 = "0, 3, 3")
    @NotYetImplemented(FF3_6)
    public void queue__delay___can_be_stopped() throws Exception {
        runTest("queue: delay() can be stopped");
    }

    /**
     * Test {136=[FF10, FF17, IE9], 137=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_stop_hooks() throws Exception {
        runTest("queue: queue stop hooks");
    }

    /**
     * Test {137=[FF10, FF17, IE9], 138=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__jQuery_propFix_integrity_test() throws Exception {
        runTest("attributes: jQuery.propFix integrity test");
    }

    /**
     * Test {138=[FF10, FF17, IE9], 139=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 46, 46")
    public void attributes__attr_String_() throws Exception {
        runTest("attributes: attr(String)");
    }

    /**
     * Test {139=[FF10, FF17, IE9], 140=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_String__in_XML_Files() throws Exception {
        runTest("attributes: attr(String) in XML Files");
    }

    /**
     * Test {140=[FF10, FF17, IE9], 141=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Function_() throws Exception {
        runTest("attributes: attr(String, Function)");
    }

    /**
     * Test {141=[FF10, FF17, IE9], 142=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_Hash_() throws Exception {
        runTest("attributes: attr(Hash)");
    }

    /**
     * Test {142=[FF10, FF17, IE9], 143=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 81, 81")
    public void attributes__attr_String__Object_() throws Exception {
        runTest("attributes: attr(String, Object)");
    }

    /**
     * Test {143=[FF10, FF17, IE9], 144=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr_jquery_method_() throws Exception {
        runTest("attributes: attr(jquery_method)");
    }

    /**
     * Test {144=[FF10, FF17, IE9], 145=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_document() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML document");
    }

    /**
     * Test {145=[FF10, FF17, IE9], 146=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__attr__tabindex__() throws Exception {
        runTest("attributes: attr('tabindex')");
    }

    /**
     * Test {146=[FF10, FF17, IE9], 147=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr__tabindex___value_() throws Exception {
        runTest("attributes: attr('tabindex', value)");
    }

    /**
     * Test {147=[FF10, FF17, IE9], 148=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void attributes__removeAttr_String_() throws Exception {
        runTest("attributes: removeAttr(String)");
    }

    /**
     * Test {148=[FF10, FF17, IE9], 149=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeAttr_String__in_XML() throws Exception {
        runTest("attributes: removeAttr(String) in XML");
    }

    /**
     * Test {149=[FF10, FF17, IE9], 150=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeAttr_Multi_String__variable_space_width_() throws Exception {
        runTest("attributes: removeAttr(Multi String, variable space width)");
    }

    /**
     * Test {150=[FF10, FF17, IE9], 151=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void attributes__prop_String__Object_() throws Exception {
        runTest("attributes: prop(String, Object)");
    }

    /**
     * Test {151=[FF10, FF17, IE9], 152=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__prop__tabindex__() throws Exception {
        runTest("attributes: prop('tabindex')");
    }

    /**
     * Test {152=[FF10, FF17, IE9], 153=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__prop__tabindex___value_() throws Exception {
        runTest("attributes: prop('tabindex', value)");
    }

    /**
     * Test {153=[FF10, FF17, IE9], 154=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void attributes__removeProp_String_() throws Exception {
        runTest("attributes: removeProp(String)");
    }

    /**
     * Test {154=[FF10, FF17, IE9], 155=[FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void attributes__val__() throws Exception {
        runTest("attributes: val()");
    }

    /**
     * Test {155=[FF17]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF17 = "0, 4, 4")
    public void attributes__val___respects_numbers_without_exception__Bug__9319_() throws Exception {
        runTest("attributes: val() respects numbers without exception (Bug #9319)");
    }

    /**
     * Test {155=[FF10, IE9], 156=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__val_String_Number_() throws Exception {
        runTest("attributes: val(String/Number)");
    }

    /**
     * Test {156=[FF10, IE9], 157=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__val_Function_() throws Exception {
        runTest("attributes: val(Function)");
    }

    /**
     * Test {157=[FF10, IE9], 158=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__val_Array_of_Numbers___Bug__7123_() throws Exception {
        runTest("attributes: val(Array of Numbers) (Bug #7123)");
    }

    /**
     * Test {158=[FF10, IE9], 159=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void attributes__val_Function__with_incoming_value() throws Exception {
        runTest("attributes: val(Function) with incoming value");
    }

    /**
     * Test {159=[FF10, IE9], 160=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__val_select__after_form_reset____Bug__2551_() throws Exception {
        runTest("attributes: val(select) after form.reset() (Bug #2551)");
    }

    /**
     * Test {160=[FF10, IE9], 161=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_String_() throws Exception {
        runTest("attributes: addClass(String)");
    }

    /**
     * Test {161=[FF10, IE9], 162=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_Function_() throws Exception {
        runTest("attributes: addClass(Function)");
    }

    /**
     * Test {162=[FF10, IE9], 163=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 48, 48")
    public void attributes__addClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: addClass(Function) with incoming value");
    }

    /**
     * Test {163=[FF10, IE9], 164=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeClass_String____simple() throws Exception {
        runTest("attributes: removeClass(String) - simple");
    }

    /**
     * Test {164=[FF10, IE9], 165=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeClass_Function____simple() throws Exception {
        runTest("attributes: removeClass(Function) - simple");
    }

    /**
     * Test {165=[FF10, IE9], 166=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 48, 48")
    public void attributes__removeClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: removeClass(Function) with incoming value");
    }

    /**
     * Test {166=[FF10, IE9], 167=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__removeClass___removes_duplicates() throws Exception {
        runTest("attributes: removeClass() removes duplicates");
    }

    /**
     * Test {167=[FF10, IE9], 168=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_String_boolean_undefined___boolean__() throws Exception {
        runTest("attributes: toggleClass(String|boolean|undefined[, boolean])");
    }

    /**
     * Test {168=[FF10, IE9], 169=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_Function___boolean__() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean])");
    }

    /**
     * Test {169=[FF10, IE9], 170=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void attributes__toggleClass_Fucntion___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Fucntion[, boolean]) with incoming value");
    }

    /**
     * Test {170=[FF10, IE9], 171=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__addClass__removeClass__hasClass() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass");
    }

    /**
     * Test {171=[FF10, IE9], 172=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__contents___hasClass___returns_correct_values() throws Exception {
        runTest("attributes: contents().hasClass() returns correct values");
    }

    /**
     * Test {172=[FF10, IE9], 173=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__coords_returns_correct_values_in_IE6_IE7__see__10828() throws Exception {
        runTest("attributes: coords returns correct values in IE6/IE7, see #10828");
    }

    /**
     * Test {173=[FF10, IE9], 174=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__null_or_undefined_handler() throws Exception {
        runTest("event: null or undefined handler");
    }

    /**
     * Test {174=[FF10, IE9], 175=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__bind___live___delegate___with_non_null_defined_data() throws Exception {
        runTest("event: bind(),live(),delegate() with non-null,defined data");
    }

    /**
     * Test {175=[FF10, IE9], 176=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Handler_changes_and__trigger___order() throws Exception {
        runTest("event: Handler changes and .trigger() order");
    }

    /**
     * Test {176=[FF10, IE9], 177=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_data() throws Exception {
        runTest("event: bind(), with data");
    }

    /**
     * Test {177=[FF10, IE9], 178=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__click____with_data() throws Exception {
        runTest("event: click(), with data");
    }

    /**
     * Test {178=[FF10, IE9], 179=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_data__trigger_with_data() throws Exception {
        runTest("event: bind(), with data, trigger with data");
    }

    /**
     * Test {179=[FF10, IE9], 180=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind____multiple_events_at_once() throws Exception {
        runTest("event: bind(), multiple events at once");
    }

    /**
     * Test {180=[FF10, IE9], 181=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____five_events_at_once() throws Exception {
        runTest("event: bind(), five events at once");
    }

    /**
     * Test {181=[FF10, IE9], 182=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__bind____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: bind(), multiple events at once and namespaces");
    }

    /**
     * Test {182=[FF10, IE9], 183=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void event__bind____namespace_with_special_add() throws Exception {
        runTest("event: bind(), namespace with special add");
    }

    /**
     * Test {183=[FF10, IE9], 184=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____no_data() throws Exception {
        runTest("event: bind(), no data");
    }

    /**
     * Test {184=[FF10, IE9], 185=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__bind_one_unbind_Object_() throws Exception {
        runTest("event: bind/one/unbind(Object)");
    }

    /**
     * Test {185=[FF10, IE9], 186=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__live_die_Object___delegate_undelegate_String__Object_() throws Exception {
        runTest("event: live/die(Object), delegate/undelegate(String, Object)");
    }

    /**
     * Test {186=[FF10, IE9], 187=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__live_delegate_immediate_propagation() throws Exception {
        runTest("event: live/delegate immediate propagation");
    }

    /**
     * Test {187=[FF10, IE9], 188=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind_delegate_bubbling__isDefaultPrevented() throws Exception {
        runTest("event: bind/delegate bubbling, isDefaultPrevented");
    }

    /**
     * Test {188=[FF10, IE9], 189=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____iframes() throws Exception {
        runTest("event: bind(), iframes");
    }

    /**
     * Test {189=[FF10, IE9], 190=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__bind____trigger_change_on_select() throws Exception {
        runTest("event: bind(), trigger change on select");
    }

    /**
     * Test {190=[FF10, IE9], 191=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__bind____namespaced_events__cloned_events() throws Exception {
        runTest("event: bind(), namespaced events, cloned events");
    }

    /**
     * Test {191=[FF10, IE9], 192=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__bind____multi_namespaced_events() throws Exception {
        runTest("event: bind(), multi-namespaced events");
    }

    /**
     * Test {192=[FF10, IE9], 193=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind____with_same_function() throws Exception {
        runTest("event: bind(), with same function");
    }

    /**
     * Test {193=[FF10, IE9], 194=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____make_sure_order_is_maintained() throws Exception {
        runTest("event: bind(), make sure order is maintained");
    }

    /**
     * Test {194=[FF10, IE9], 195=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_different_this_object() throws Exception {
        runTest("event: bind(), with different this object");
    }

    /**
     * Test {195=[FF10, IE9], 196=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__bind_name__false___unbind_name__false_() throws Exception {
        runTest("event: bind(name, false), unbind(name, false)");
    }

    /**
     * Test {196=[FF10, IE9], 197=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__live_name__false___die_name__false_() throws Exception {
        runTest("event: live(name, false), die(name, false)");
    }

    /**
     * Test {197=[FF10, IE9], 198=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegate_selector__name__false___undelegate_selector__name__false_() throws Exception {
        runTest("event: delegate(selector, name, false), undelegate(selector, name, false)");
    }

    /**
     * Test {198=[FF10, IE9], 199=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__bind___trigger___unbind___on_plain_object() throws Exception {
        runTest("event: bind()/trigger()/unbind() on plain object");
    }

    /**
     * Test {199=[FF10, IE9], 200=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__unbind_type_() throws Exception {
        runTest("event: unbind(type)");
    }

    /**
     * Test {200=[FF10, IE9], 201=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__unbind_eventObject_() throws Exception {
        runTest("event: unbind(eventObject)");
    }

    /**
     * Test {201=[FF10, IE9], 202=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__hover___and_hover_pseudo_event() throws Exception {
        runTest("event: hover() and hover pseudo-event");
    }

    /**
     * Test {202=[FF10, IE9], 203=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__mouseover_triggers_mouseenter() throws Exception {
        runTest("event: mouseover triggers mouseenter");
    }

    /**
     * Test {203=[FF10, IE9], 204=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__withinElement_implemented_with_jQuery_contains__() throws Exception {
        runTest("event: withinElement implemented with jQuery.contains()");
    }

    /**
     * Test {204=[FF10, IE9], 205=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__mouseenter__mouseleave_don_t_catch_exceptions() throws Exception {
        runTest("event: mouseenter, mouseleave don't catch exceptions");
    }

    /**
     * Test {205=[FF10, IE9], 206=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__trigger___shortcuts() throws Exception {
        runTest("event: trigger() shortcuts");
    }

    /**
     * Test {206=[FF10, IE9], 207=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__trigger___bubbling() throws Exception {
        runTest("event: trigger() bubbling");
    }

    /**
     * Test {207=[FF10, IE9], 208=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__trigger_type___data____fn__() throws Exception {
        runTest("event: trigger(type, [data], [fn])");
    }

    /**
     * Test {208=[FF10, IE9], 209=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE)
    public void event__submit_event_bubbles_on_copied_forms___11649_() throws Exception {
        runTest("event: submit event bubbles on copied forms (#11649)");
    }

    /**
     * Test {209=[FF10, IE9], 210=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__change_event_bubbles_on_copied_forms___11796_() throws Exception {
        runTest("event: change event bubbles on copied forms (#11796)");
    }

    /**
     * Test {210=[FF10, IE9], 211=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void event__trigger_eventObject___data____fn__() throws Exception {
        runTest("event: trigger(eventObject, [data], [fn])");
    }

    /**
     * Test {211=[FF10, IE9], 212=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event___trigger___bubbling_on_disconnected_elements___10489_() throws Exception {
        runTest("event: .trigger() bubbling on disconnected elements (#10489)");
    }

    /**
     * Test {212=[FF10, IE9], 213=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___trigger___doesn_t_bubble_load_event___10717_() throws Exception {
        runTest("event: .trigger() doesn't bubble load event (#10717)");
    }

    /**
     * Test {213=[FF10, IE9], 214=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented(FF)
    public void event__Delegated_events_in_SVG___10791_() throws Exception {
        runTest("event: Delegated events in SVG (#10791)");
    }

    /**
     * Test {214=[FF10, IE9], 215=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__Delegated_events_in_forms___10844___11145___8165___11382___11764_() throws Exception {
        runTest("event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764)");
    }

    /**
     * Test {215=[FF10, IE9], 216=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Submit_event_can_be_stopped___11049_() throws Exception {
        runTest("event: Submit event can be stopped (#11049)");
    }

    /**
     * Test {216=[FF10, IE9], 217=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__on_beforeunload__creates_deletes_window_property_instead_of_adding_removing_event_listener() throws Exception {
        runTest("event: on(beforeunload) creates/deletes window property instead of adding/removing event listener");
    }

    /**
     * Test {217=[FF10, IE9], 218=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__jQuery_Event__type__props__() throws Exception {
        runTest("event: jQuery.Event( type, props )");
    }

    /**
     * Test {218=[FF10, IE9], 219=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_Event_currentTarget() throws Exception {
        runTest("event: jQuery.Event.currentTarget");
    }

    /**
     * Test {219=[FF10, IE9], 220=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__toggle_Function__Function______() throws Exception {
        runTest("event: toggle(Function, Function, ...)");
    }

    /**
     * Test {220=[FF10, IE9], 221=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 66, 66")
    public void event___live____die__() throws Exception {
        runTest("event: .live()/.die()");
    }

    /**
     * Test {221=[FF10, IE9], 222=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__die_all_bound_events() throws Exception {
        runTest("event: die all bound events");
    }

    /**
     * Test {222=[FF10, IE9], 223=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__live_with_multiple_events() throws Exception {
        runTest("event: live with multiple events");
    }

    /**
     * Test {223=[FF10, IE9], 224=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void event__live_with_namespaces() throws Exception {
        runTest("event: live with namespaces");
    }

    /**
     * Test {224=[FF10, IE9], 225=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__live_with_change() throws Exception {
        runTest("event: live with change");
    }

    /**
     * Test {225=[FF10, IE9], 226=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__live_with_submit() throws Exception {
        runTest("event: live with submit");
    }

    /**
     * Test {226=[FF10, IE9], 227=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void event__live_with_special_events() throws Exception {
        runTest("event: live with special events");
    }

    /**
     * Test {227=[FF10, IE9], 228=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 65, 65")
    public void event___delegate____undelegate__() throws Exception {
        runTest("event: .delegate()/.undelegate()");
    }

    /**
     * Test {228=[FF10, IE9], 229=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_off_using_dispatched_jQuery_Event() throws Exception {
        runTest("event: jQuery.off using dispatched jQuery.Event");
    }

    /**
     * Test {229=[FF10, IE9], 230=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegated_event_with_delegateTarget_relative_selector() throws Exception {
        runTest("event: delegated event with delegateTarget-relative selector");
    }

    /**
     * Test {230=[FF10, IE9], 231=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__stopPropagation___stops_directly_bound_events_on_delegated_target() throws Exception {
        runTest("event: stopPropagation() stops directly-bound events on delegated target");
    }

    /**
     * Test {231=[FF10, IE9], 232=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__undelegate_all_bound_events() throws Exception {
        runTest("event: undelegate all bound events");
    }

    /**
     * Test {232=[FF10, IE9], 233=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__delegate_with_multiple_events() throws Exception {
        runTest("event: delegate with multiple events");
    }

    /**
     * Test {233=[FF10, IE9], 234=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__delegate_with_change() throws Exception {
        runTest("event: delegate with change");
    }

    /**
     * Test {234=[FF10, IE9], 235=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__delegate_with_submit() throws Exception {
        runTest("event: delegate with submit");
    }

    /**
     * Test {235=[FF10, IE9], 236=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__undelegate___with_only_namespaces() throws Exception {
        runTest("event: undelegate() with only namespaces");
    }

    /**
     * Test {236=[FF10, IE9], 237=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Non_DOM_element_events() throws Exception {
        runTest("event: Non DOM element events");
    }

    /**
     * Test {237=[FF10, IE9], 238=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__inline_handler_returning_false_stops_default() throws Exception {
        runTest("event: inline handler returning false stops default");
    }

    /**
     * Test {238=[FF10, IE9], 239=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__window_resize() throws Exception {
        runTest("event: window resize");
    }

    /**
     * Test {239=[FF10, IE9], 240=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__focusin_bubbles() throws Exception {
        runTest("event: focusin bubbles");
    }

    /**
     * Test {240=[FF10, IE9], 241=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__custom_events_with_colons___3533___8272_() throws Exception {
        runTest("event: custom events with colons (#3533, #8272)");
    }

    /**
     * Test {241=[FF10, IE9], 242=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void event___on_and__off() throws Exception {
        runTest("event: .on and .off");
    }

    /**
     * Test {242=[FF10, IE9], 243=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__special_bind_delegate_name_mapping() throws Exception {
        runTest("event: special bind/delegate name mapping");
    }

    /**
     * Test {243=[FF10, IE9], 244=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event___on_and__off__selective_mixed_removal___10705_() throws Exception {
        runTest("event: .on and .off, selective mixed removal (#10705)");
    }

    /**
     * Test {244=[FF10, IE9], 245=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___on__event_map__null_selector__data____11130() throws Exception {
        runTest("event: .on( event-map, null-selector, data ) #11130");
    }

    /**
     * Test {245=[FF10, IE9], 246=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__clone___delegated_events___11076_() throws Exception {
        runTest("event: clone() delegated events (#11076)");
    }

    /**
     * Test {246=[FF10, IE9], 247=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__fixHooks_extensions() throws Exception {
        runTest("event: fixHooks extensions");
    }

    /**
     * Test {247=[FF10, IE9], 248=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_promise() throws Exception {
        runTest("event: jQuery.ready promise");
    }

    /**
     * Test {248=[FF10, IE9], 249=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_synchronous_load_with_long_loading_subresources() throws Exception {
        runTest("event: jQuery.ready synchronous load with long loading subresources");
    }

    /**
     * Test {249=[FF10, IE9], 250=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_isReady() throws Exception {
        runTest("event: jQuery.isReady");
    }

    /**
     * Test {250=[FF10, IE9], 251=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void event__jQuery_ready() throws Exception {
        runTest("event: jQuery ready");
    }

    /**
     * Test {251=[FF10, IE9], 252=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__change_handler_should_be_detached_from_element() throws Exception {
        runTest("event: change handler should be detached from element");
    }

    /**
     * Test {252=[FF10, IE9], 253=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__trigger_click_on_checkbox__fires_change_event() throws Exception {
        runTest("event: trigger click on checkbox, fires change event");
    }

    /**
     * Test {253=[FF10, IE9], 254=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void selector___jQuery_only__element___jQuery_only() throws Exception {
        runTest("selector - jQuery only: element - jQuery only");
    }

    /**
     * Test {254=[FF10, IE9], 255=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector___jQuery_only__class___jQuery_only() throws Exception {
        runTest("selector - jQuery only: class - jQuery only");
    }

    /**
     * Test {255=[FF10, IE9], 256=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void selector___jQuery_only__attributes___jQuery_only() throws Exception {
        runTest("selector - jQuery only: attributes - jQuery only");
    }

    /**
     * Test {256=[FF10, IE9], 257=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void selector___jQuery_only__pseudo___visibility() throws Exception {
        runTest("selector - jQuery only: pseudo - visibility");
    }

    /**
     * Test {257=[FF10, IE9], 258=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector___jQuery_only__disconnected_nodes() throws Exception {
        runTest("selector - jQuery only: disconnected nodes");
    }

    /**
     * Test {258=[FF10, IE9], 259=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    @NotYetImplemented
    public void selector___jQuery_only__attributes___jQuery_attr() throws Exception {
        runTest("selector - jQuery only: attributes - jQuery.attr");
    }

    /**
     * Test {259=[FF10, IE9], 260=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void selector___jQuery_only__Sizzle_cache_collides_with_multiple_Sizzles_on_a_page() throws Exception {
        runTest("selector - jQuery only: Sizzle cache collides with multiple Sizzles on a page");
    }

    /**
     * Test {260=[FF10, IE9], 261=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__find_String_() throws Exception {
        runTest("traversing: find(String)");
    }

    /**
     * Test {261=[FF10, IE9], 262=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__find_node_jQuery_object_() throws Exception {
        runTest("traversing: find(node|jQuery object)");
    }

    /**
     * Test {262=[FF10, IE9], 263=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    public void traversing__is_String_undefined_() throws Exception {
        runTest("traversing: is(String|undefined)");
    }

    /**
     * Test {263=[FF10, IE9], 264=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void traversing__is_jQuery_() throws Exception {
        runTest("traversing: is(jQuery)");
    }

    /**
     * Test {264=[FF10, IE9], 265=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void traversing__is___with_positional_selectors() throws Exception {
        runTest("traversing: is() with positional selectors");
    }

    /**
     * Test {265=[FF10, IE9], 266=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__index__() throws Exception {
        runTest("traversing: index()");
    }

    /**
     * Test {266=[FF10, IE9], 267=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__index_Object_String_undefined_() throws Exception {
        runTest("traversing: index(Object|String|undefined)");
    }

    /**
     * Test {267=[FF10, IE9], 268=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__filter_Selector_undefined_() throws Exception {
        runTest("traversing: filter(Selector|undefined)");
    }

    /**
     * Test {268=[FF10, IE9], 269=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__filter_Function_() throws Exception {
        runTest("traversing: filter(Function)");
    }

    /**
     * Test {269=[FF10, IE9], 270=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Element_() throws Exception {
        runTest("traversing: filter(Element)");
    }

    /**
     * Test {270=[FF10, IE9], 271=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Array_() throws Exception {
        runTest("traversing: filter(Array)");
    }

    /**
     * Test {271=[FF10, IE9], 272=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_jQuery_() throws Exception {
        runTest("traversing: filter(jQuery)");
    }

    /**
     * Test {272=[FF10, IE9], 273=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void traversing__filter___with_positional_selectors() throws Exception {
        runTest("traversing: filter() with positional selectors");
    }

    /**
     * Test {273=[FF10, IE9], 274=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void traversing__closest__() throws Exception {
        runTest("traversing: closest()");
    }

    /**
     * Test {274=[FF10, IE9], 275=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void traversing__closest_jQuery_() throws Exception {
        runTest("traversing: closest(jQuery)");
    }

    /**
     * Test {275=[FF10, IE9], 276=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__not_Selector_undefined_() throws Exception {
        runTest("traversing: not(Selector|undefined)");
    }

    /**
     * Test {276=[FF10, IE9], 277=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Element_() throws Exception {
        runTest("traversing: not(Element)");
    }

    /**
     * Test {277=[FF10, IE9], 278=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Function_() throws Exception {
        runTest("traversing: not(Function)");
    }

    /**
     * Test {278=[FF10, IE9], 279=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__not_Array_() throws Exception {
        runTest("traversing: not(Array)");
    }

    /**
     * Test {279=[FF10, IE9], 280=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_jQuery_() throws Exception {
        runTest("traversing: not(jQuery)");
    }

    /**
     * Test {280=[FF10, IE9], 281=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__has_Element_() throws Exception {
        runTest("traversing: has(Element)");
    }

    /**
     * Test {281=[FF10, IE9], 282=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__has_Selector_() throws Exception {
        runTest("traversing: has(Selector)");
    }

    /**
     * Test {282=[FF10, IE9], 283=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__has_Arrayish_() throws Exception {
        runTest("traversing: has(Arrayish)");
    }

    /**
     * Test {283=[FF10, IE9], 284=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__addBack__() throws Exception {
        runTest("traversing: addBack()");
    }

    /**
     * Test {284=[FF10, IE9], 285=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void traversing__siblings__String__() throws Exception {
        runTest("traversing: siblings([String])");
    }

    /**
     * Test {285=[FF10, IE9], 286=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__children__String__() throws Exception {
        runTest("traversing: children([String])");
    }

    /**
     * Test {286=[FF10, IE9], 287=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__parent__String__() throws Exception {
        runTest("traversing: parent([String])");
    }

    /**
     * Test {287=[FF10, IE9], 288=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__parents__String__() throws Exception {
        runTest("traversing: parents([String])");
    }

    /**
     * Test {288=[FF10, IE9], 289=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__parentsUntil__String__() throws Exception {
        runTest("traversing: parentsUntil([String])");
    }

    /**
     * Test {289=[FF10, IE9], 290=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__next__String__() throws Exception {
        runTest("traversing: next([String])");
    }

    /**
     * Test {290=[FF10, IE9], 291=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__prev__String__() throws Exception {
        runTest("traversing: prev([String])");
    }

    /**
     * Test {291=[FF10, IE9], 292=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__nextAll__String__() throws Exception {
        runTest("traversing: nextAll([String])");
    }

    /**
     * Test {292=[FF10, IE9], 293=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__prevAll__String__() throws Exception {
        runTest("traversing: prevAll([String])");
    }

    /**
     * Test {293=[FF10, IE9], 294=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__nextUntil__String__() throws Exception {
        runTest("traversing: nextUntil([String])");
    }

    /**
     * Test {294=[FF10, IE9], 295=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void traversing__prevUntil__String__() throws Exception {
        runTest("traversing: prevUntil([String])");
    }

    /**
     * Test {295=[FF10, IE9], 296=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__contents__() throws Exception {
        runTest("traversing: contents()");
    }

    /**
     * Test {296=[FF10, IE9], 297=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__add_String_Element_Array_undefined_() throws Exception {
        runTest("traversing: add(String|Element|Array|undefined)");
    }

    /**
     * Test {297=[FF10, IE9], 298=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__add_String__Context_() throws Exception {
        runTest("traversing: add(String, Context)");
    }

    /**
     * Test {298=[FF10, IE9], 299=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__eq___1____10616() throws Exception {
        runTest("traversing: eq('-1') #10616");
    }

    /**
     * Test {299=[FF10, IE9], 300=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__text__() throws Exception {
        runTest("manipulation: text()");
    }

    /**
     * Test {300=[FF10, IE9], 301=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__text_undefined_() throws Exception {
        runTest("manipulation: text(undefined)");
    }

    /**
     * Test {301=[FF10, IE9], 302=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__text_String_() throws Exception {
        runTest("manipulation: text(String)");
    }

    /**
     * Test {302=[FF10, IE9], 303=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__text_Function_() throws Exception {
        runTest("manipulation: text(Function)");
    }

    /**
     * Test {303=[FF10, IE9], 304=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__text_Function__with_incoming_value() throws Exception {
        runTest("manipulation: text(Function) with incoming value");
    }

    /**
     * Test {304=[FF10, IE9], 305=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void manipulation__wrap_String_Element_() throws Exception {
        runTest("manipulation: wrap(String|Element)");
    }

    /**
     * Test {305=[FF10, IE9], 306=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void manipulation__wrap_Function_() throws Exception {
        runTest("manipulation: wrap(Function)");
    }

    /**
     * Test {306=[FF10, IE9], 307=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__wrap_Function__with_index___10177_() throws Exception {
        runTest("manipulation: wrap(Function) with index (#10177)");
    }

    /**
     * Test {307=[FF10, IE9], 308=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void manipulation__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("manipulation: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {308=[FF10, IE9], 309=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__wrapAll_String_Element_() throws Exception {
        runTest("manipulation: wrapAll(String|Element)");
    }

    /**
     * Test {309=[FF10, IE9], 310=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void manipulation__wrapInner_String_Element_() throws Exception {
        runTest("manipulation: wrapInner(String|Element)");
    }

    /**
     * Test {310=[FF10, IE9], 311=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void manipulation__wrapInner_Function_() throws Exception {
        runTest("manipulation: wrapInner(Function)");
    }

    /**
     * Test {311=[FF10, IE9], 312=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__unwrap__() throws Exception {
        runTest("manipulation: unwrap()");
    }

    /**
     * Test {312=[FF10, IE9], 313=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 58, 58")
    public void manipulation__append_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {313=[FF10, IE9], 314=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 58, 58")
    public void manipulation__append_Function_() throws Exception {
        runTest("manipulation: append(Function)");
    }

    /**
     * Test {314=[FF10, IE9], 315=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void manipulation__append_Function__with_incoming_value() throws Exception {
        runTest("manipulation: append(Function) with incoming value");
    }

    /**
     * Test {315=[FF10, IE9], 316=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF10 = "0, 2, 2",
        FF17 = "0, 2, 2",
        FF3_6 = "0, 2, 2",
        IE6 = "0, 3, 3",
        IE7 = "0, 3, 3",
        IE8 = "0, 3, 3",
        IE9 = "0, 2, 2")
    public void manipulation__append_the_same_fragment_with_events__Bug__6997__5566_() throws Exception {
        runTest("manipulation: append the same fragment with events (Bug #6997, 5566)");
    }

    /**
     * Test {316=[FF10, IE9], 317=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_HTML5_sectioning_elements__Bug__6485_() throws Exception {
        runTest("manipulation: append HTML5 sectioning elements (Bug #6485)");
    }

    /**
     * Test {317=[FF10, IE9], 318=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__HTML5_Elements_inherit_styles_from_style_rules__Bug__10501_() throws Exception {
        runTest("manipulation: HTML5 Elements inherit styles from style rules (Bug #10501)");
    }

    /**
     * Test {318=[FF10, IE9], 319=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html5_clone___cannot_use_the_fragment_cache_in_IE___6485_() throws Exception {
        runTest("manipulation: html5 clone() cannot use the fragment cache in IE (#6485)");
    }

    /**
     * Test {319=[FF10, IE9], 320=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_String__with_HTML5__Bug__6485_() throws Exception {
        runTest("manipulation: html(String) with HTML5 (Bug #6485)");
    }

    /**
     * Test {320=[FF10, IE9], 321=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__IE8_serialization_bug() throws Exception {
        runTest("manipulation: IE8 serialization bug");
    }

    /**
     * Test {321=[FF10, IE9], 322=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___object_element__10324() throws Exception {
        runTest("manipulation: html() object element #10324");
    }

    /**
     * Test {322=[FF10, IE9], 323=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__append_xml_() throws Exception {
        runTest("manipulation: append(xml)");
    }

    /**
     * Test {323=[FF10, IE9], 324=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void manipulation__appendTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: appendTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {324=[FF10, IE9], 325=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prepend_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prepend(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {325=[FF10, IE9], 326=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prepend_Function_() throws Exception {
        runTest("manipulation: prepend(Function)");
    }

    /**
     * Test {326=[FF10, IE9], 327=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__prepend_Function__with_incoming_value() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value");
    }

    /**
     * Test {327=[FF10, IE9], 328=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prependTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prependTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {328=[FF10, IE9], 329=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__before_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: before(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {329=[FF10, IE9], 330=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__before_Function_() throws Exception {
        runTest("manipulation: before(Function)");
    }

    /**
     * Test {330=[FF10, IE9], 331=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_and_after_w__empty_object___10812_() throws Exception {
        runTest("manipulation: before and after w/ empty object (#10812)");
    }

    /**
     * Test {331=[FF10, IE9], 332=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_and_after_on_disconnected_node___10517_() throws Exception {
        runTest("manipulation: before and after on disconnected node (#10517)");
    }

    /**
     * Test {332=[FF10, IE9], 333=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__insertBefore_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertBefore(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {333=[FF10, IE9], 334=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__after_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: after(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {334=[FF10, IE9], 335=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__after_Function_() throws Exception {
        runTest("manipulation: after(Function)");
    }

    /**
     * Test {335=[FF10, IE9], 336=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__insertAfter_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertAfter(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {336=[FF10, IE9], 337=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void manipulation__replaceWith_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {337=[FF10, IE9], 338=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void manipulation__replaceWith_Function_() throws Exception {
        runTest("manipulation: replaceWith(Function)");
    }

    /**
     * Test {338=[FF10, IE9], 339=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceWith_string__for_more_than_one_element() throws Exception {
        runTest("manipulation: replaceWith(string) for more than one element");
    }

    /**
     * Test {339=[FF10, IE9], 340=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__replaceAll_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceAll(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {340=[FF10, IE9], 341=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery_clone_____8017_() throws Exception {
        runTest("manipulation: jQuery.clone() (#8017)");
    }

    /**
     * Test {341=[FF10, IE9], 342=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone_____8070_() throws Exception {
        runTest("manipulation: clone() (#8070)");
    }

    /**
     * Test {342=[FF10, IE9], 343=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 44, 44")
    public void manipulation__clone__() throws Exception {
        runTest("manipulation: clone()");
    }

    /**
     * Test {343=[FF10, IE9], 344=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__clone_script_type_non_javascript____11359_() throws Exception {
        runTest("manipulation: clone(script type=non-javascript) (#11359)");
    }

    /**
     * Test {344=[FF10, IE9], 345=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__clone_form_element___Bug__3879___6655_() throws Exception {
        runTest("manipulation: clone(form element) (Bug #3879, #6655)");
    }

    /**
     * Test {345=[FF10, IE9], 346=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__clone_multiple_selected_options___Bug__8129_() throws Exception {
        runTest("manipulation: clone(multiple selected options) (Bug #8129)");
    }

    /**
     * Test {346=[FF10, IE9], 347=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_XML_nodes() throws Exception {
        runTest("manipulation: clone() on XML nodes");
    }

    /**
     * Test {347=[FF10, IE9], 348=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_local_XML_nodes_with_html5_nodename() throws Exception {
        runTest("manipulation: clone() on local XML nodes with html5 nodename");
    }

    /**
     * Test {348=[FF10, IE9], 349=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html_undefined_() throws Exception {
        runTest("manipulation: html(undefined)");
    }

    /**
     * Test {349=[FF10, IE9], 350=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___on_empty_set() throws Exception {
        runTest("manipulation: html() on empty set");
    }

    /**
     * Test {350=[FF10, IE9], 351=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    public void manipulation__html_String_() throws Exception {
        runTest("manipulation: html(String)");
    }

    /**
     * Test {351=[FF10, IE9], 352=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 37, 37")
    public void manipulation__html_Function_() throws Exception {
        runTest("manipulation: html(Function)");
    }

    /**
     * Test {352=[FF10, IE9], 353=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void manipulation__html_Function__with_incoming_value() throws Exception {
        runTest("manipulation: html(Function) with incoming value");
    }

    /**
     * Test {353=[FF10, IE9], 354=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__remove__() throws Exception {
        runTest("manipulation: remove()");
    }

    /**
     * Test {354=[FF10, IE9], 355=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__detach__() throws Exception {
        runTest("manipulation: detach()");
    }

    /**
     * Test {355=[FF10, IE9], 356=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__empty__() throws Exception {
        runTest("manipulation: empty()");
    }

    /**
     * Test {356=[FF10, IE9], 357=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void manipulation__jQuery_cleanData() throws Exception {
        runTest("manipulation: jQuery.cleanData");
    }

    /**
     * Test {357=[FF10, IE9], 358=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___no_plain_text_caching__Bug__6779_() throws Exception {
        runTest("manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779)");
    }

    /**
     * Test {358=[FF10, IE9], 359=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__jQuery_html___execute_scripts_escaped_with_html_comment_or_CDATA___9221_() throws Exception {
        runTest("manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221)");
    }

    /**
     * Test {359=[FF10, IE9], 360=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___plain_objects_are_not_a_document__8950() throws Exception {
        runTest("manipulation: jQuery.buildFragment - plain objects are not a document #8950");
    }

    /**
     * Test {360=[FF10, IE9], 361=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_clone___no_exceptions_for_object_elements__9587() throws Exception {
        runTest("manipulation: jQuery.clone - no exceptions for object elements #9587");
    }

    /**
     * Test {361=[FF10, IE9], 362=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("manipulation: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {362=[FF10, IE9], 363=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__Cloned__detached_HTML5_elems___10667_10670_() throws Exception {
        runTest("manipulation: Cloned, detached HTML5 elems (#10667,10670)");
    }

    /**
     * Test {363=[FF10, IE9], 364=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__jQuery_fragments_cache_expectations() throws Exception {
        runTest("manipulation: jQuery.fragments cache expectations");
    }

    /**
     * Test {364=[FF10, IE9], 365=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__Guard_against_exceptions_when_clearing_safeChildNodes() throws Exception {
        runTest("manipulation: Guard against exceptions when clearing safeChildNodes");
    }

    /**
     * Test {365=[FF10, IE9], 366=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__Ensure_oldIE_creates_a_new_set_on_appendTo___8894_() throws Exception {
        runTest("manipulation: Ensure oldIE creates a new set on appendTo (#8894)");
    }

    /**
     * Test {366=[FF10, IE9], 367=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_____script_exceptions_bubble___11743_() throws Exception {
        runTest("manipulation: html() - script exceptions bubble (#11743)");
    }

    /**
     * Test {367=[FF10, IE9], 368=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__checked_state_is_cloned_with_clone__() throws Exception {
        runTest("manipulation: checked state is cloned with clone()");
    }

    /**
     * Test {368=[FF10, IE9], 369=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__manipulate_mixed_jQuery_and_text___12384___12346_() throws Exception {
        runTest("manipulation: manipulate mixed jQuery and text (#12384, #12346)");
    }

    /**
     * Test {369=[FF10, IE9], 370=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__buildFragment_works_even_if_document_0__is_iframe_s_window_object_in_IE9_10___12266_() throws Exception {
        runTest("manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266)");
    }

    /**
     * Test {370=[FF10, IE9], 371=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 46, 46")
    @NotYetImplemented
    public void css__css_String_Hash_() throws Exception {
        runTest("css: css(String|Hash)");
    }

    /**
     * Test {371=[FF10, IE9], 372=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void css__css___explicit_and_relative_values() throws Exception {
        runTest("css: css() explicit and relative values");
    }

    /**
     * Test {372=[FF10, IE9], 373=[FF17, FF3_6, IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void css__css_String__Object_() throws Exception {
        runTest("css: css(String, Object)");
    }

    /**
     * Test {373=[FF10, IE9], 374=[FF17, FF3_6], 376=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function_() throws Exception {
        runTest("css: css(String, Function)");
    }

    /**
     * Test {374=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE6 = "0, 5, 5",
        IE7 = "0, 5, 5",
        IE8 = "0, 5, 5")
    public void css__css_String__Object__for_MSIE() throws Exception {
        runTest("css: css(String, Object) for MSIE");
    }

    /**
     * Test {374=[FF10, IE9], 375=[FF17, FF3_6], 377=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_String__Function__with_incoming_value() throws Exception {
        runTest("css: css(String, Function) with incoming value");
    }

    /**
     * Test {375=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE6 = "0, 2, 2",
        IE7 = "0, 2, 2",
        IE8 = "0, 2, 2")
    public void css__Setting_opacity_to_1_properly_removes_filter__style___6652_() throws Exception {
        runTest("css: Setting opacity to 1 properly removes filter: style (#6652)");
    }

    /**
     * Test {375=[FF10, IE9], 376=[FF17, FF3_6], 378=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions() throws Exception {
        runTest("css: css(Object) where values are Functions");
    }

    /**
     * Test {376=[FF10, IE9], 377=[FF17, FF3_6], 379=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions_with_incoming_values() throws Exception {
        runTest("css: css(Object) where values are Functions with incoming values");
    }

    /**
     * Test {377=[FF10, IE9], 378=[FF17, FF3_6], 380=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void css__show____hide__() throws Exception {
        runTest("css: show(); hide()");
    }

    /**
     * Test {378=[FF10, IE9], 379=[FF17, FF3_6], 381=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF10 = "0, 7, 7",
        FF17 = "0, 7, 7",
        FF3_6 = "3, 4, 7",
        IE6 = "2, 1, 3",
        IE7 = "2, 1, 3",
        IE8 = "2, 1, 3",
        IE9 = "0, 7, 7")
    @NotYetImplemented({ IE, FF17 })
    public void css__show___resolves_correct_default_display__8099() throws Exception {
        runTest("css: show() resolves correct default display #8099");
    }

    /**
     * Test {379=[FF10, IE9], 380=[FF17, FF3_6], 382=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void css__show___resolves_correct_default_display__detached_nodes___10006_() throws Exception {
        runTest("css: show() resolves correct default display, detached nodes (#10006)");
    }

    /**
     * Test {380=[FF10, IE9], 381=[FF17, FF3_6], 383=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    @NotYetImplemented(IE9)
    public void css__toggle__() throws Exception {
        runTest("css: toggle()");
    }

    /**
     * Test {381=[FF10, IE9], 382=[FF17, FF3_6], 384=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__hide_hidden_elements__bug__7141_() throws Exception {
        runTest("css: hide hidden elements (bug #7141)");
    }

    /**
     * Test {382=[FF10, IE9], 383=[FF17, FF3_6], 385=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {383=[FF10, IE9], 384=[FF17, FF3_6], 386=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css___visible_selector_works_properly_on_table_elements__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on table elements (bug #4512)");
    }

    /**
     * Test {384=[FF10, IE9], 385=[FF17, FF3_6], 387=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void css___visible_selector_works_properly_on_children_with_a_hidden_parent__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on children with a hidden parent (bug #4512)");
    }

    /**
     * Test {385=[FF10, IE9], 386=[FF17, FF3_6], 388=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__internal_ref_to_elem_runtimeStyle__bug__7608_() throws Exception {
        runTest("css: internal ref to elem.runtimeStyle (bug #7608)");
    }

    /**
     * Test {386=[FF10, IE9], 387=[FF17, FF3_6], 389=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__marginRight_computed_style__bug__3333_() throws Exception {
        runTest("css: marginRight computed style (bug #3333)");
    }

    /**
     * Test {387=[FF10, IE9], 388=[FF17, FF3_6], 390=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__box_model_properties_incorrectly_returning___instead_of_px__see__10639_and__12088() throws Exception {
        runTest("css: box model properties incorrectly returning % instead of px, see #10639 and #12088");
    }

    /**
     * Test {388=[FF10, IE9], 389=[FF17, FF3_6], 391=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__jQuery_cssProps_behavior___bug__8402_() throws Exception {
        runTest("css: jQuery.cssProps behavior, (bug #8402)");
    }

    /**
     * Test {389=[FF10, IE9], 390=[FF17, FF3_6], 392=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__widows___orphans__8936() throws Exception {
        runTest("css: widows & orphans #8936");
    }

    /**
     * Test {390=[FF10, IE9], 391=[FF17, FF3_6], 393=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__can_t_get_css_for_disconnected_in_IE_9__see__10254_and__8388() throws Exception {
        runTest("css: can't get css for disconnected in IE<9, see #10254 and #8388");
    }

    /**
     * Test {391=[FF10, IE9], 392=[FF17, FF3_6], 394=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void css__can_t_get_background_position_in_IE_9__see__10796() throws Exception {
        runTest("css: can't get background-position in IE<9, see #10796");
    }

    /**
     * Test {392=[FF10, IE9], 393=[FF17, FF3_6], 395=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__percentage_properties_for_bottom_and_right_in_IE_9_should_not_be_incorrectly_transformed_to_pixels__see__11311() throws Exception {
        runTest("css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pixels, see #11311");
    }

    /**
     * Test {393=[FF10, IE9], 394=[FF17, FF3_6], 396=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__percentage_properties_for_left_and_top_should_be_transformed_to_pixels__see__9505() throws Exception {
        runTest("css: percentage properties for left and top should be transformed to pixels, see #9505");
    }

    /**
     * Test {394=[FF10, IE9], 395=[FF17, FF3_6], 397=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF10 = "0, 1, 1",
        FF17 = "0, 1, 1",
        FF3_6 = "1, 0, 1",
        IE6 = "0, 1, 1",
        IE7 = "0, 1, 1",
        IE8 = "0, 1, 1",
        IE9 = "0, 1, 1")
    public void css__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("css: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {395=[FF10, IE9], 396=[FF17, FF3_6], 398=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__css__width___and_css__height___should_respect_box_sizing__see__11004() throws Exception {
        runTest("css: css('width') and css('height') should respect box-sizing, see #11004");
    }

    /**
     * Test {396=[FF10, IE9], 397=[FF17, FF3_6], 399=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__certain_css_values_of__normal__should_be_convertable_to_a_number__see__8627() throws Exception {
        runTest("css: certain css values of 'normal' should be convertable to a number, see #8627");
    }

    /**
     * Test {397=[FF10, IE9], 398=[FF17, FF3_6], 400=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void css__cssHooks___expand() throws Exception {
        runTest("css: cssHooks - expand");
    }

    /**
     * Test {398=[FF10, IE9], 399=[FF17, FF3_6], 401=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void serialize__jQuery_param__() throws Exception {
        runTest("serialize: jQuery.param()");
    }

    /**
     * Test {399=[FF10, IE9], 400=[FF17, FF3_6], 402=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void serialize__jQuery_param___Constructed_prop_values() throws Exception {
        runTest("serialize: jQuery.param() Constructed prop values");
    }

    /**
     * Test {400=[FF10, IE9], 401=[FF17, FF3_6], 403=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void serialize__serialize__() throws Exception {
        runTest("serialize: serialize()");
    }

    /**
     * Test {401=[FF10, IE9], 402=[FF17, FF3_6], 404=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks");
    }

    /**
     * Test {402=[FF10, IE9], 403=[FF17, FF3_6], 405=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks____url__options__syntax() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks - (url, options) syntax");
    }

    /**
     * Test {403=[FF10, IE9], 404=[FF17, FF3_6], 406=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (late binding)");
    }

    /**
     * Test {404=[FF10, IE9], 405=[FF17, FF3_6], 407=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__oncomplete_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (oncomplete binding)");
    }

    /**
     * Test {405=[FF10, IE9], 406=[FF17, FF3_6], 408=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__very_late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (very late binding)");
    }

    /**
     * Test {406=[FF10, IE9], 407=[FF17, FF3_6], 409=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____success_callbacks__order_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (order)");
    }

    /**
     * Test {407=[FF10, IE9], 408=[FF17, FF3_6], 410=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____error_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - error callbacks");
    }

    /**
     * Test {408=[FF10, IE9], 409=[FF17, FF3_6], 411=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax___multiple_method_signatures_introduced_in_1_5____8107_() throws Exception {
        runTest("ajax: jQuery.ajax - multiple method signatures introduced in 1.5 ( #8107)");
    }

    /**
     * Test {409=[FF10, IE9], 410=[FF17, FF3_6], 412=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_____textStatus_and_errorThrown_values() throws Exception {
        runTest("ajax: jQuery.ajax() - textStatus and errorThrown values");
    }

    /**
     * Test {410=[FF10, IE9], 411=[FF17, FF3_6], 413=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____responseText_on_error() throws Exception {
        runTest("ajax: jQuery.ajax() - responseText on error");
    }

    /**
     * Test {411=[FF10, IE9], 412=[FF17, FF3_6], 414=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: .ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {412=[FF10, IE9], 413=[FF17, FF3_6], 415=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___ajax_____headers() throws Exception {
        runTest("ajax: .ajax() - headers");
    }

    /**
     * Test {413=[FF10, IE9], 414=[FF17, FF3_6], 416=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___ajax_____Accept_header() throws Exception {
        runTest("ajax: .ajax() - Accept header");
    }

    /**
     * Test {414=[FF10, IE9], 415=[FF17, FF3_6], 417=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___ajax_____contentType() throws Exception {
        runTest("ajax: .ajax() - contentType");
    }

    /**
     * Test {415=[FF10, IE9], 416=[FF17, FF3_6], 418=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: .ajax() - protocol-less urls");
    }

    /**
     * Test {416=[FF10, IE9], 417=[FF17, FF3_6], 419=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___ajax_____hash() throws Exception {
        runTest("ajax: .ajax() - hash");
    }

    /**
     * Test {417=[FF10, IE9], 418=[FF17, FF3_6], 420=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax___cross_domain_detection() throws Exception {
        runTest("ajax: jQuery ajax - cross-domain detection");
    }

    /**
     * Test {418=[FF10, IE9], 419=[FF17, FF3_6], 421=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax___load_____404_error_callbacks() throws Exception {
        runTest("ajax: .load() - 404 error callbacks");
    }

    /**
     * Test {419=[FF10, IE9], 420=[FF17, FF3_6], 422=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____abort() throws Exception {
        runTest("ajax: jQuery.ajax() - abort");
    }

    /**
     * Test {420=[FF10, IE9], 421=[FF17, FF3_6], 423=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void ajax__Ajax_events_with_context() throws Exception {
        runTest("ajax: Ajax events with context");
    }

    /**
     * Test {421=[FF10, IE9], 422=[FF17, FF3_6], 424=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_context_modification() throws Exception {
        runTest("ajax: jQuery.ajax context modification");
    }

    /**
     * Test {422=[FF10, IE9], 423=[FF17, FF3_6], 425=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax context modification through ajaxSetup");
    }

    /**
     * Test {423=[FF10, IE9], 424=[FF17, FF3_6], 426=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____disabled_globals() throws Exception {
        runTest("ajax: jQuery.ajax() - disabled globals");
    }

    /**
     * Test {424=[FF10, IE9], 425=[FF17, FF3_6], 427=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {425=[FF10, IE9], 426=[FF17, FF3_6], 428=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {426=[FF10, IE9], 427=[FF17, FF3_6], 429=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2, 0, 2")
    public void ajax__jQuery_ajax___HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax - HEAD requests");
    }

    /**
     * Test {427=[FF10, IE9], 428=[FF17, FF3_6], 430=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend");
    }

    /**
     * Test {428=[FF10, IE9], 429=[FF17, FF3_6], 431=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request___2688_() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request (#2688)");
    }

    /**
     * Test {429=[FF10, IE9], 430=[FF17, FF3_6], 432=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request manually");
    }

    /**
     * Test {430=[FF10, IE9], 431=[FF17, FF3_6], 433=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax___dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax - dataType html");
    }

    /**
     * Test {431=[FF10, IE9], 432=[FF17, FF3_6], 434=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__synchronous_request() throws Exception {
        runTest("ajax: synchronous request");
    }

    /**
     * Test {432=[FF10, IE9], 433=[FF17, FF3_6], 435=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: synchronous request with callbacks");
    }

    /**
     * Test {433=[FF10, IE9], 434=[FF17, FF3_6], 436=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__pass_through_request_object() throws Exception {
        runTest("ajax: pass-through request object");
    }

    /**
     * Test {434=[FF10, IE9], 435=[FF17, FF3_6], 437=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void ajax__ajax_cache() throws Exception {
        runTest("ajax: ajax cache");
    }

    /**
     * Test {435=[FF10, IE9], 436=[FF17, FF3_6], 438=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_() throws Exception {
        runTest("ajax: load(String)");
    }

    /**
     * Test {436=[FF10, IE9], 437=[FF17, FF3_6], 439=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_null_() throws Exception {
        runTest("ajax: load(String,null)");
    }

    /**
     * Test {437=[FF10, IE9], 438=[FF17, FF3_6], 440=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_undefined_() throws Exception {
        runTest("ajax: load(String,undefined)");
    }

    /**
     * Test {438=[FF10, IE9], 439=[FF17, FF3_6], 441=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load__url_selector__() throws Exception {
        runTest("ajax: load('url selector')");
    }

    /**
     * Test {439=[FF10, IE9], 440=[FF17, FF3_6], 442=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load_String__Function__with_ajaxSetup_on_dataType_json__see__2046() throws Exception {
        runTest("ajax: load(String, Function) with ajaxSetup on dataType json, see #2046");
    }

    /**
     * Test {440=[FF10, IE9], 441=[FF17, FF3_6], 443=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Function____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: load(String, Function) - simple: inject text into DOM");
    }

    /**
     * Test {441=[FF10, IE9], 442=[FF17, FF3_6], 444=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void ajax__load_String__Function____check_scripts() throws Exception {
        runTest("ajax: load(String, Function) - check scripts");
    }

    /**
     * Test {442=[FF10, IE9], 443=[FF17, FF3_6], 445=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__load_String__Function____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: load(String, Function) - check file with only a script tag");
    }

    /**
     * Test {443=[FF10, IE9], 444=[FF17, FF3_6], 446=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Function____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: load(String, Function) - dataFilter in ajaxSettings");
    }

    /**
     * Test {444=[FF10, IE9], 445=[FF17, FF3_6], 447=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Object__Function_() throws Exception {
        runTest("ajax: load(String, Object, Function)");
    }

    /**
     * Test {445=[FF10, IE9], 446=[FF17, FF3_6], 448=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__String__Function_() throws Exception {
        runTest("ajax: load(String, String, Function)");
    }

    /**
     * Test {446=[FF10, IE9], 447=[FF17, FF3_6], 449=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load_____data_specified_in_ajaxSettings_is_merged_in___10524_() throws Exception {
        runTest("ajax: load() - data specified in ajaxSettings is merged in (#10524)");
    }

    /**
     * Test {447=[FF10, IE9], 448=[FF17, FF3_6], 450=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: load() - callbacks get the correct parameters");
    }

    /**
     * Test {448=[FF10, IE9], 449=[FF17, FF3_6], 451=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_get_String__Function____data_in_ajaxSettings___8277_() throws Exception {
        runTest("ajax: jQuery.get(String, Function) - data in ajaxSettings (#8277)");
    }

    /**
     * Test {449=[FF10, IE9], 450=[FF17, FF3_6], 452=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_get_String__Hash__Function____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get(String, Hash, Function) - parse xml and use text() on nodes");
    }

    /**
     * Test {450=[FF10, IE9], 451=[FF17, FF3_6], 453=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_getScript_String__Function____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - with callback");
    }

    /**
     * Test {451=[FF10, IE9], 452=[FF17, FF3_6], 454=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_getScript_String__Function____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - no callback");
    }

    /**
     * Test {452=[FF10, IE9], 453=[FF17, FF3_6], 455=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void ajax__jQuery_ajax_____JSONP__Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Same Domain");
    }

    /**
     * Test {453=[FF10, IE9], 454=[FF17, FF3_6], 456=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void ajax__jQuery_ajax_____JSONP__Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Cross Domain");
    }

    /**
     * Test {454=[FF10, IE9], 455=[FF17, FF3_6], 457=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote");
    }

    /**
     * Test {455=[FF10, IE9], 456=[FF17, FF3_6], 458=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____script__Remote_with_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with POST");
    }

    /**
     * Test {456=[FF10, IE9], 457=[FF17, FF3_6], 459=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote_with_scheme_less_URL() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with scheme-less URL");
    }

    /**
     * Test {457=[FF10, IE9], 458=[FF17, FF3_6], 460=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____malformed_JSON() throws Exception {
        runTest("ajax: jQuery.ajax() - malformed JSON");
    }

    /**
     * Test {458=[FF10, IE9], 459=[FF17, FF3_6], 461=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____script__throws_exception___11743_() throws Exception {
        runTest("ajax: jQuery.ajax() - script, throws exception (#11743)");
    }

    /**
     * Test {459=[FF10, IE9], 460=[FF17, FF3_6], 462=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - script by content-type");
    }

    /**
     * Test {460=[FF10, IE9], 461=[FF17, FF3_6], 463=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____json_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type");
    }

    /**
     * Test {461=[FF10, IE9], 462=[FF17, FF3_6], 464=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax_____json_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type disabled with options");
    }

    /**
     * Test {462=[FF10, IE9], 463=[FF17, FF3_6], 465=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_getJSON_String__Hash__Function____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Hash, Function) - JSON array");
    }

    /**
     * Test {463=[FF10, IE9], 464=[FF17, FF3_6], 466=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object");
    }

    /**
     * Test {464=[FF10, IE9], 465=[FF17, FF3_6], 467=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON___Using_Native_JSON() throws Exception {
        runTest("ajax: jQuery.getJSON - Using Native JSON");
    }

    /**
     * Test {465=[FF10, IE9], 466=[FF17, FF3_6], 468=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object with absolute url to local content");
    }

    /**
     * Test {466=[FF10, IE9], 467=[FF17, FF3_6], 469=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_post___data() throws Exception {
        runTest("ajax: jQuery.post - data");
    }

    /**
     * Test {467=[FF10, IE9], 468=[FF17, FF3_6], 470=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_post_String__Hash__Function____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post(String, Hash, Function) - simple with xml");
    }

    /**
     * Test {468=[FF10, IE9], 469=[FF17, FF3_6], 471=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void ajax__jQuery_ajaxSetup__timeout__Number_____with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) - with global timeout");
    }

    /**
     * Test {469=[FF10, IE9], 470=[FF17, FF3_6], 472=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxSetup__timeout__Number___with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) with localtimeout");
    }

    /**
     * Test {470=[FF10, IE9], 471=[FF17, FF3_6], 473=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___simple_get() throws Exception {
        runTest("ajax: jQuery.ajax - simple get");
    }

    /**
     * Test {471=[FF10, IE9], 472=[FF17, FF3_6], 474=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___simple_post() throws Exception {
        runTest("ajax: jQuery.ajax - simple post");
    }

    /**
     * Test {472=[FF10, IE9], 473=[FF17, FF3_6], 475=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__ajaxSetup__() throws Exception {
        runTest("ajax: ajaxSetup()");
    }

    /**
     * Test {473=[FF10, IE9], 474=[FF17, FF3_6], 476=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__data_option__evaluate_function_values___2806_() throws Exception {
        runTest("ajax: data option: evaluate function values (#2806)");
    }

    /**
     * Test {474=[FF10, IE9], 475=[FF17, FF3_6], 477=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__data_option__empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: data option: empty bodies for non-GET requests");
    }

    /**
     * Test {475=[FF10, IE9], 476=[FF17, FF3_6], 478=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (cache)");
    }

    /**
     * Test {476=[FF10, IE9], 477=[FF17, FF3_6], 479=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (cache)");
    }

    /**
     * Test {477=[FF10, IE9], 478=[FF17, FF3_6], 480=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (no cache)");
    }

    /**
     * Test {478=[FF10, IE9], 479=[FF17, FF3_6], 481=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (no cache)");
    }

    /**
     * Test {479=[FF10, IE9], 480=[FF17, FF3_6], 482=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented(IE)
    public void ajax__jQuery_ajax___failing_cross_domain() throws Exception {
        runTest("ajax: jQuery ajax - failing cross-domain");
    }

    /**
     * Test {480=[FF10, IE9], 481=[FF17, FF3_6], 483=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___atom_xml() throws Exception {
        runTest("ajax: jQuery ajax - atom+xml");
    }

    /**
     * Test {481=[FF10, IE9], 482=[FF17, FF3_6], 484=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___Location_object_as_url___7531_() throws Exception {
        runTest("ajax: jQuery.ajax - Location object as url (#7531)");
    }

    /**
     * Test {482=[FF10, IE9], 483=[FF17, FF3_6], 485=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: jQuery.ajax - Context with circular references (#9887)");
    }

    /**
     * Test {483=[FF10, IE9], 484=[FF17, FF3_6], 486=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___statusText() throws Exception {
        runTest("ajax: jQuery.ajax - statusText");
    }

    /**
     * Test {484=[FF10, IE9], 485=[FF17, FF3_6], 487=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void ajax__jQuery_ajax___statusCode() throws Exception {
        runTest("ajax: jQuery.ajax - statusCode");
    }

    /**
     * Test {485=[FF10, IE9], 486=[FF17, FF3_6], 488=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax___transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax - transitive conversions");
    }

    /**
     * Test {486=[FF10, IE9], 487=[FF17, FF3_6], 489=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax - overrideMimeType");
    }

    /**
     * Test {487=[FF10, IE9], 488=[FF17, FF3_6], 490=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___abort_in_prefilter() throws Exception {
        runTest("ajax: jQuery.ajax - abort in prefilter");
    }

    /**
     * Test {488=[FF10, IE9], 489=[FF17, FF3_6], 491=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___loading_binary_data_shouldn_t_throw_an_exception_in_IE___11426_() throws Exception {
        runTest("ajax: jQuery.ajax - loading binary data shouldn't throw an exception in IE (#11426)");
    }

    /**
     * Test {489=[FF10, IE9], 490=[FF17, FF3_6], 492=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_domManip___no_side_effect_because_of_ajaxSetup_or_global_events___11264_() throws Exception {
        runTest("ajax: jQuery.domManip - no side effect because of ajaxSetup or global events (#11264)");
    }

    /**
     * Test {490=[FF10, IE9], 491=[FF17, FF3_6], 493=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_domManip___script_in_comments_are_properly_evaluated___11402_() throws Exception {
        runTest("ajax: jQuery.domManip - script in comments are properly evaluated (#11402)");
    }

    /**
     * Test {491=[FF10, IE9], 492=[FF17, FF3_6], 494=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___active_counter() throws Exception {
        runTest("ajax: jQuery.ajax - active counter");
    }

    /**
     * Test {492=[FF10, IE9], 493=[FF17, FF3_6], 495=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__sanity_check() throws Exception {
        runTest("effects: sanity check");
    }

    /**
     * Test {493=[FF10, IE9], 494=[FF17, FF3_6], 496=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void effects__show__() throws Exception {
        runTest("effects: show()");
    }

    /**
     * Test {494=[FF10, IE9], 495=[FF17, FF3_6], 497=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__show_Number____other_displays() throws Exception {
        runTest("effects: show(Number) - other displays");
    }

    /**
     * Test {495=[FF10, IE9], 496=[FF17, FF3_6], 498=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__Persist_correct_display_value() throws Exception {
        runTest("effects: Persist correct display value");
    }

    /**
     * Test {496=[FF10, IE9], 497=[FF17, FF3_6], 499=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_Hash__Object__Function_() throws Exception {
        runTest("effects: animate(Hash, Object, Function)");
    }

    /**
     * Test {497=[FF10, IE9], 498=[FF17, FF3_6], 500=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented(IE)
    public void effects__animate_negative_height() throws Exception {
        runTest("effects: animate negative height");
    }

    /**
     * Test {498=[FF10, IE9], 499=[FF17, FF3_6], 501=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin() throws Exception {
        runTest("effects: animate negative margin");
    }

    /**
     * Test {499=[FF10, IE9], 500=[FF17, FF3_6], 502=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin_with_px() throws Exception {
        runTest("effects: animate negative margin with px");
    }

    /**
     * Test {500=[FF10, IE9], 501=[FF17, FF3_6], 503=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void effects__animate_negative_padding() throws Exception {
        runTest("effects: animate negative padding");
    }

    /**
     * Test {501=[FF10, IE9], 502=[FF17, FF3_6], 504=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE)
    public void effects__animate_block_as_inline_width_height() throws Exception {
        runTest("effects: animate block as inline width/height");
    }

    /**
     * Test {502=[FF10, IE9], 503=[FF17, FF3_6], 505=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE)
    public void effects__animate_native_inline_width_height() throws Exception {
        runTest("effects: animate native inline width/height");
    }

    /**
     * Test {503=[FF10, IE9], 504=[FF17, FF3_6], 506=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE)
    public void effects__animate_block_width_height() throws Exception {
        runTest("effects: animate block width/height");
    }

    /**
     * Test {504=[FF10, IE9], 505=[FF17, FF3_6], 507=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_table_width_height() throws Exception {
        runTest("effects: animate table width/height");
    }

    /**
     * Test {505=[FF10, IE9], 506=[FF17, FF3_6], 508=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void effects__animate_table_row_width_height() throws Exception {
        runTest("effects: animate table-row width/height");
    }

    /**
     * Test {506=[FF10, IE9], 507=[FF17, FF3_6], 509=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void effects__animate_table_cell_width_height() throws Exception {
        runTest("effects: animate table-cell width/height");
    }

    /**
     * Test {507=[FF10, IE9], 508=[FF17, FF3_6], 510=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_percentage____on_width_height() throws Exception {
        runTest("effects: animate percentage(%) on width/height");
    }

    /**
     * Test {508=[FF10, IE9], 509=[FF17, FF3_6], 511=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_resets_overflow_x_and_overflow_y_when_finished() throws Exception {
        runTest("effects: animate resets overflow-x and overflow-y when finished");
    }

    /**
     * Test {509=[FF10, IE9], 510=[FF17, FF3_6], 512=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__false__() throws Exception {
        runTest("effects: animate option { queue: false }");
    }

    /**
     * Test {510=[FF10, IE9], 511=[FF17, FF3_6], 513=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__true__() throws Exception {
        runTest("effects: animate option { queue: true }");
    }

    /**
     * Test {511=[FF10, IE9], 512=[FF17, FF3_6], 514=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_option___queue___name___() throws Exception {
        runTest("effects: animate option { queue: 'name' }");
    }

    /**
     * Test {512=[FF10, IE9], 513=[FF17, FF3_6], 515=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_with_no_properties() throws Exception {
        runTest("effects: animate with no properties");
    }

    /**
     * Test {513=[FF10, IE9], 514=[FF17, FF3_6], 516=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_duration_0() throws Exception {
        runTest("effects: animate duration 0");
    }

    /**
     * Test {514=[FF10, IE9], 515=[FF17, FF3_6], 517=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_hyphenated_properties() throws Exception {
        runTest("effects: animate hyphenated properties");
    }

    /**
     * Test {515=[FF10, IE9], 516=[FF17, FF3_6], 518=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_non_element() throws Exception {
        runTest("effects: animate non-element");
    }

    /**
     * Test {516=[FF10, IE9], 517=[FF17, FF3_6], 519=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop__() throws Exception {
        runTest("effects: stop()");
    }

    /**
     * Test {517=[FF10, IE9], 518=[FF17, FF3_6], 520=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop_____several_in_queue() throws Exception {
        runTest("effects: stop() - several in queue");
    }

    /**
     * Test {518=[FF10, IE9], 519=[FF17, FF3_6], 521=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop_clearQueue_() throws Exception {
        runTest("effects: stop(clearQueue)");
    }

    /**
     * Test {519=[FF10, IE9], 520=[FF17, FF3_6], 522=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__stop_clearQueue__gotoEnd_() throws Exception {
        runTest("effects: stop(clearQueue, gotoEnd)");
    }

    /**
     * Test {520=[FF10, IE9], 521=[FF17, FF3_6], 523=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop__queue_______________Stop_single_queues() throws Exception {
        runTest("effects: stop( queue, ..., ... ) - Stop single queues");
    }

    /**
     * Test {521=[FF10, IE9], 522=[FF17, FF3_6], 524=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__toggle__() throws Exception {
        runTest("effects: toggle()");
    }

    /**
     * Test {522=[FF10, IE9], 523=[FF17, FF3_6], 525=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void effects__jQuery_fx_prototype_cur______1_8_Back_Compat() throws Exception {
        runTest("effects: jQuery.fx.prototype.cur() - <1.8 Back Compat");
    }

    /**
     * Test {523=[FF10, IE9], 524=[FF17, FF3_6], 526=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__JS_Overflow_and_Display() throws Exception {
        runTest("effects: JS Overflow and Display");
    }

    /**
     * Test {524=[FF10, IE9], 525=[FF17, FF3_6], 527=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__CSS_Overflow_and_Display() throws Exception {
        runTest("effects: CSS Overflow and Display");
    }

    /**
     * Test {525=[FF10], 526=[FF17, FF3_6], 528=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    @NotYetImplemented
    public void effects__CSS_Auto_to_show() throws Exception {
        runTest("effects: CSS Auto to show");
    }

    /**
     * Test {525=[IE9], 529=[FF10], 530=[FF17, FF3_6], 532=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_0() throws Exception {
        runTest("effects: CSS Auto to 0");
    }

    /**
     * Test {526=[FF10], 527=[FF17, FF3_6], 529=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_Auto_to_hide() throws Exception {
        runTest("effects: CSS Auto to hide");
    }

    /**
     * Test {526=[IE9], 528=[FF10], 529=[FF17, FF3_6], 531=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_50() throws Exception {
        runTest("effects: CSS Auto to 50");
    }

    /**
     * Test {527=[FF10, IE9], 528=[FF17, FF3_6], 530=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_100() throws Exception {
        runTest("effects: CSS Auto to 100");
    }

    /**
     * Test {530=[FF10], 531=[FF17, FF3_6], 533=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    @NotYetImplemented
    public void effects__JS_Auto_to_show() throws Exception {
        runTest("effects: JS Auto to show");
    }

    /**
     * Test {530=[IE9], 534=[FF10], 535=[FF17, FF3_6], 537=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_0() throws Exception {
        runTest("effects: JS Auto to 0");
    }

    /**
     * Test {531=[FF10], 532=[FF17, FF3_6], 534=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_Auto_to_hide() throws Exception {
        runTest("effects: JS Auto to hide");
    }

    /**
     * Test {531=[IE9], 533=[FF10], 534=[FF17, FF3_6], 536=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_50() throws Exception {
        runTest("effects: JS Auto to 50");
    }

    /**
     * Test {532=[FF10, IE9], 533=[FF17, FF3_6], 535=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_100() throws Exception {
        runTest("effects: JS Auto to 100");
    }

    /**
     * Test {535=[FF10], 536=[FF17, FF3_6], 538=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_100_to_show() throws Exception {
        runTest("effects: CSS 100 to show");
    }

    /**
     * Test {535=[IE9], 539=[FF10], 540=[FF17, FF3_6], 542=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_0() throws Exception {
        runTest("effects: CSS 100 to 0");
    }

    /**
     * Test {536=[FF10], 537=[FF17, FF3_6], 539=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_100_to_hide() throws Exception {
        runTest("effects: CSS 100 to hide");
    }

    /**
     * Test {536=[IE9], 538=[FF10], 539=[FF17, FF3_6], 541=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_50() throws Exception {
        runTest("effects: CSS 100 to 50");
    }

    /**
     * Test {537=[FF10, IE9], 538=[FF17, FF3_6], 540=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_100() throws Exception {
        runTest("effects: CSS 100 to 100");
    }

    /**
     * Test {540=[FF10], 541=[FF17, FF3_6], 543=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_100_to_show() throws Exception {
        runTest("effects: JS 100 to show");
    }

    /**
     * Test {540=[IE9], 544=[FF10], 545=[FF17, FF3_6], 547=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_0() throws Exception {
        runTest("effects: JS 100 to 0");
    }

    /**
     * Test {541=[FF10], 542=[FF17, FF3_6], 544=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_100_to_hide() throws Exception {
        runTest("effects: JS 100 to hide");
    }

    /**
     * Test {541=[IE9], 543=[FF10], 544=[FF17, FF3_6], 546=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_50() throws Exception {
        runTest("effects: JS 100 to 50");
    }

    /**
     * Test {542=[FF10, IE9], 543=[FF17, FF3_6], 545=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_100() throws Exception {
        runTest("effects: JS 100 to 100");
    }

    /**
     * Test {545=[FF10], 546=[FF17, FF3_6], 548=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_50_to_show() throws Exception {
        runTest("effects: CSS 50 to show");
    }

    /**
     * Test {545=[IE9], 549=[FF10], 550=[FF17, FF3_6], 552=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_0() throws Exception {
        runTest("effects: CSS 50 to 0");
    }

    /**
     * Test {546=[FF10], 547=[FF17, FF3_6], 549=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_50_to_hide() throws Exception {
        runTest("effects: CSS 50 to hide");
    }

    /**
     * Test {546=[IE9], 548=[FF10], 549=[FF17, FF3_6], 551=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_50() throws Exception {
        runTest("effects: CSS 50 to 50");
    }

    /**
     * Test {547=[FF10, IE9], 548=[FF17, FF3_6], 550=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_100() throws Exception {
        runTest("effects: CSS 50 to 100");
    }

    /**
     * Test {550=[FF10], 551=[FF17, FF3_6], 553=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_50_to_show() throws Exception {
        runTest("effects: JS 50 to show");
    }

    /**
     * Test {550=[IE9], 554=[FF10], 555=[FF17, FF3_6], 557=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_0() throws Exception {
        runTest("effects: JS 50 to 0");
    }

    /**
     * Test {551=[FF10], 552=[FF17, FF3_6], 554=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_50_to_hide() throws Exception {
        runTest("effects: JS 50 to hide");
    }

    /**
     * Test {551=[IE9], 553=[FF10], 554=[FF17, FF3_6], 556=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_50() throws Exception {
        runTest("effects: JS 50 to 50");
    }

    /**
     * Test {552=[FF10, IE9], 553=[FF17, FF3_6], 555=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_100() throws Exception {
        runTest("effects: JS 50 to 100");
    }

    /**
     * Test {555=[FF10], 556=[FF17, FF3_6], 558=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_0_to_show() throws Exception {
        runTest("effects: CSS 0 to show");
    }

    /**
     * Test {555=[IE9], 559=[FF10], 560=[FF17, FF3_6], 562=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_0() throws Exception {
        runTest("effects: CSS 0 to 0");
    }

    /**
     * Test {556=[FF10], 557=[FF17, FF3_6], 559=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_0_to_hide() throws Exception {
        runTest("effects: CSS 0 to hide");
    }

    /**
     * Test {556=[IE9], 558=[FF10], 559=[FF17, FF3_6], 561=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_50() throws Exception {
        runTest("effects: CSS 0 to 50");
    }

    /**
     * Test {557=[FF10, IE9], 558=[FF17, FF3_6], 560=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_100() throws Exception {
        runTest("effects: CSS 0 to 100");
    }

    /**
     * Test {560=[FF10], 561=[FF17, FF3_6], 563=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_0_to_show() throws Exception {
        runTest("effects: JS 0 to show");
    }

    /**
     * Test {560=[IE9], 564=[FF10], 565=[FF17, FF3_6], 567=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_0() throws Exception {
        runTest("effects: JS 0 to 0");
    }

    /**
     * Test {561=[FF10], 562=[FF17, FF3_6], 564=[IE6, IE7, IE8, IE9]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_0_to_hide() throws Exception {
        runTest("effects: JS 0 to hide");
    }

    /**
     * Test {561=[IE9], 563=[FF10], 564=[FF17, FF3_6], 566=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_50() throws Exception {
        runTest("effects: JS 0 to 50");
    }

    /**
     * Test {562=[FF10, IE9], 563=[FF17, FF3_6], 565=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_100() throws Exception {
        runTest("effects: JS 0 to 100");
    }

    /**
     * Test {565=[FF10, IE9], 566=[FF17, FF3_6], 568=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeOut_fadeIn() throws Exception {
        runTest("effects: Chain fadeOut fadeIn");
    }

    /**
     * Test {566=[FF10, IE9], 567=[FF17, FF3_6], 569=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeIn_fadeOut() throws Exception {
        runTest("effects: Chain fadeIn fadeOut");
    }

    /**
     * Test {567=[FF10, IE9], 568=[FF17, FF3_6], 570=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_hide_show() throws Exception {
        runTest("effects: Chain hide show");
    }

    /**
     * Test {568=[FF10, IE9], 569=[FF17, FF3_6], 571=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_show_hide() throws Exception {
        runTest("effects: Chain show hide");
    }

    /**
     * Test {569=[FF10, IE9], 570=[FF17, FF3_6], 572=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_show_hide_with_easing_and_callback() throws Exception {
        runTest("effects: Chain show hide with easing and callback");
    }

    /**
     * Test {570=[FF10, IE9], 571=[FF17, FF3_6], 573=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_in() throws Exception {
        runTest("effects: Chain toggle in");
    }

    /**
     * Test {571=[FF10, IE9], 572=[FF17, FF3_6], 574=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_out() throws Exception {
        runTest("effects: Chain toggle out");
    }

    /**
     * Test {572=[FF10, IE9], 573=[FF17, FF3_6], 575=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_out_with_easing_and_callback() throws Exception {
        runTest("effects: Chain toggle out with easing and callback");
    }

    /**
     * Test {573=[FF10, IE9], 574=[FF17, FF3_6], 576=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideDown_slideUp() throws Exception {
        runTest("effects: Chain slideDown slideUp");
    }

    /**
     * Test {574=[FF10, IE9], 575=[FF17, FF3_6], 577=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideUp_slideDown() throws Exception {
        runTest("effects: Chain slideUp slideDown");
    }

    /**
     * Test {575=[FF10, IE9], 576=[FF17, FF3_6], 578=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideUp_slideDown_with_easing_and_callback() throws Exception {
        runTest("effects: Chain slideUp slideDown with easing and callback");
    }

    /**
     * Test {576=[FF10, IE9], 577=[FF17, FF3_6], 579=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideToggle_in() throws Exception {
        runTest("effects: Chain slideToggle in");
    }

    /**
     * Test {577=[FF10, IE9], 578=[FF17, FF3_6], 580=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideToggle_out() throws Exception {
        runTest("effects: Chain slideToggle out");
    }

    /**
     * Test {578=[FF10, IE9], 579=[FF17, FF3_6], 581=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeToggle_in() throws Exception {
        runTest("effects: Chain fadeToggle in");
    }

    /**
     * Test {579=[FF10, IE9], 580=[FF17, FF3_6], 582=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeToggle_out() throws Exception {
        runTest("effects: Chain fadeToggle out");
    }

    /**
     * Test {580=[FF10, IE9], 581=[FF17, FF3_6], 583=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeTo_0_5_1_0_with_easing_and_callback_() throws Exception {
        runTest("effects: Chain fadeTo 0.5 1.0 with easing and callback)");
    }

    /**
     * Test {581=[FF10, IE9], 582=[FF17, FF3_6], 584=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_show__fast___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("effects: jQuery.show('fast') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {582=[FF10, IE9], 583=[FF17, FF3_6], 585=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    @NotYetImplemented(IE)
    public void effects__slideToggle___stop___slideToggle__() throws Exception {
        runTest("effects: slideToggle().stop().slideToggle()");
    }

    /**
     * Test {583=[FF10, IE9], 584=[FF17, FF3_6], 586=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__fadeToggle___stop___fadeToggle__() throws Exception {
        runTest("effects: fadeToggle().stop().fadeToggle()");
    }

    /**
     * Test {584=[FF10, IE9], 585=[FF17, FF3_6], 587=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__toggle___stop___toggle__() throws Exception {
        runTest("effects: toggle().stop().toggle()");
    }

    /**
     * Test {585=[FF10, IE9], 586=[FF17, FF3_6], 588=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_with_per_property_easing() throws Exception {
        runTest("effects: animate with per-property easing");
    }

    /**
     * Test {586=[FF10, IE9], 587=[FF17, FF3_6], 589=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_with_CSS_shorthand_properties() throws Exception {
        runTest("effects: animate with CSS shorthand properties");
    }

    /**
     * Test {587=[FF10, IE9], 588=[FF17, FF3_6], 590=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_hidden_elements__with_animation__bug__7141_() throws Exception {
        runTest("effects: hide hidden elements, with animation (bug #7141)");
    }

    /**
     * Test {588=[FF10, IE9], 589=[FF17, FF3_6], 591=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_unit_less_properties___4966_() throws Exception {
        runTest("effects: animate unit-less properties (#4966)");
    }

    /**
     * Test {589=[FF10, IE9], 590=[FF17, FF3_6], 592=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__animate_properties_missing_px_w__opacity_as_last___9074_() throws Exception {
        runTest("effects: animate properties missing px w/ opacity as last (#9074)");
    }

    /**
     * Test {590=[FF10, IE9], 591=[FF17, FF3_6], 593=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__callbacks_should_fire_in_correct_order___9100_() throws Exception {
        runTest("effects: callbacks should fire in correct order (#9100)");
    }

    /**
     * Test {591=[FF10, IE9], 592=[FF17, FF3_6], 594=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__callbacks_that_throw_exceptions_will_be_removed___5684_() throws Exception {
        runTest("effects: callbacks that throw exceptions will be removed (#5684)");
    }

    /**
     * Test {592=[FF10, IE9], 593=[FF17, FF3_6], 595=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_will_scale_margin_properties_individually() throws Exception {
        runTest("effects: animate will scale margin properties individually");
    }

    /**
     * Test {593=[FF10, IE9], 594=[FF17, FF3_6], 596=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF10 = "0, 1, 1",
        FF17 = "0, 1, 1",
        FF3_6 = "1, 0, 1",
        IE6 = "0, 1, 1",
        IE7 = "0, 1, 1",
        IE8 = "0, 1, 1",
        IE9 = "0, 1, 1")
    public void effects__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("effects: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {594=[FF10, IE9], 595=[FF17, FF3_6], 597=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_Animation__object__props__opts__() throws Exception {
        runTest("effects: jQuery.Animation( object, props, opts )");
    }

    /**
     * Test {595=[FF10, IE9], 596=[FF17, FF3_6], 598=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animate_Option__step__function__percent__tween__() throws Exception {
        runTest("effects: Animate Option: step: function( percent, tween )");
    }

    /**
     * Test {596=[FF10, IE9], 597=[FF17, FF3_6], 599=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__Animate_callbacks_have_correct_context() throws Exception {
        runTest("effects: Animate callbacks have correct context");
    }

    /**
     * Test {597=[FF10, IE9], 598=[FF17, FF3_6], 600=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__User_supplied_callback_called_after_show_when_fx_off___8892_() throws Exception {
        runTest("effects: User supplied callback called after show when fx off (#8892)");
    }

    /**
     * Test {598=[FF10, IE9], 599=[FF17, FF3_6], 601=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void effects__animate_should_set_display_for_disconnected_nodes() throws Exception {
        runTest("effects: animate should set display for disconnected nodes");
    }

    /**
     * Test {599=[FF10, IE9], 600=[FF17, FF3_6], 602=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animation_callback_should_not_show_animated_element_as_animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as animated (#7157)");
    }

    /**
     * Test {600=[FF10, IE9], 601=[FF17, FF3_6], 603=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_called_on_element_within_hidden_parent_should_set_display_to_none___10045_() throws Exception {
        runTest("effects: hide called on element within hidden parent should set display to none (#10045)");
    }

    /**
     * Test {601=[FF10, IE9], 602=[FF17, FF3_6], 604=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__hide__fadeOut_and_slideUp_called_on_element_width_height_and_width___0_should_set_display_to_none() throws Exception {
        runTest("effects: hide, fadeOut and slideUp called on element width height and width = 0 should set display to none");
    }

    /**
     * Test {602=[FF10, IE9], 603=[FF17, FF3_6], 605=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void effects__Handle_queue_false_promises() throws Exception {
        runTest("effects: Handle queue:false promises");
    }

    /**
     * Test {603=[FF10, IE9], 604=[FF17, FF3_6], 606=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__multiple_unqueued_and_promise() throws Exception {
        runTest("effects: multiple unqueued and promise");
    }

    /**
     * Test {604=[FF10, IE9], 605=[FF17, FF3_6], 607=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void effects__animate_does_not_change_start_value_for_non_px_animation___7109_() throws Exception {
        runTest("effects: animate does not change start value for non-px animation (#7109)");
    }

    /**
     * Test {605=[FF10, IE9], 606=[FF17, FF3_6], 608=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF10 = "0, 1, 1",
        FF17 = "0, 1, 1",
        FF3_6 = "0, 1, 1",
        IE6 = "0, 2, 2",
        IE7 = "0, 2, 2",
        IE8 = "0, 2, 2",
        IE9 = "0, 1, 1")
    public void effects__non_px_animation_handles_non_numeric_start___11971_() throws Exception {
        runTest("effects: non-px animation handles non-numeric start (#11971)");
    }

    /**
     * Test {606=[FF10, IE9], 607=[FF17, FF3_6], 609=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__Animation_callbacks___11797_() throws Exception {
        runTest("effects: Animation callbacks (#11797)");
    }

    /**
     * Test {607=[FF10, IE9], 608=[FF17, FF3_6], 610=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__Animate_properly_sets_overflow_hidden_when_animating_width_height___12117_() throws Exception {
        runTest("effects: Animate properly sets overflow hidden when animating width/height (#12117)");
    }

    /**
     * Test {608=[FF10, IE9], 609=[FF17, FF3_6], 611=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animations_with_0_duration_don_t_ease___12273_() throws Exception {
        runTest("effects: Animations with 0 duration don't ease (#12273)");
    }

    /**
     * Test {609=[FF10, IE9], 610=[FF17, FF3_6], 612=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__empty_set() throws Exception {
        runTest("offset: empty set");
    }

    /**
     * Test {610=[FF10, IE9], 611=[FF17, FF3_6], 613=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF10 = "0, 2, 2",
        FF17 = "0, 2, 2",
        FF3_6 = "2, 0, 2",
        IE6 = "0, 2, 2",
        IE7 = "0, 2, 2",
        IE8 = "0, 2, 2",
        IE9 = "0, 2, 2")
    public void offset__object_without_getBoundingClientRect() throws Exception {
        runTest("offset: object without getBoundingClientRect");
    }

    /**
     * Test {611=[FF10, IE9], 612=[FF17, FF3_6], 614=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__disconnected_node() throws Exception {
        runTest("offset: disconnected node");
    }

    /**
     * Test {613=[FF10, IE9], 614=[FF17, FF3_6], 616=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 178, 178")
    @NotYetImplemented
    public void offset__absolute() throws Exception {
        runTest("offset: absolute");
    }

    /**
     * Test {614=[FF10, IE9], 615=[FF17, FF3_6], 617=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 60, 60")
    @NotYetImplemented
    public void offset__relative() throws Exception {
        runTest("offset: relative");
    }

    /**
     * Test {615=[FF10, IE9], 616=[FF17, FF3_6], 618=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 80, 80")
    @NotYetImplemented
    public void offset__static() throws Exception {
        runTest("offset: static");
    }

    /**
     * Test {616=[FF10, IE9], 617=[FF17, FF3_6], 619=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    @NotYetImplemented
    public void offset__fixed() throws Exception {
        runTest("offset: fixed");
    }

    /**
     * Test {617=[FF10, IE9], 618=[FF17, FF3_6], 620=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @NotYetImplemented
    public void offset__table() throws Exception {
        runTest("offset: table");
    }

    /**
     * Test {618=[FF10, IE9], 619=[FF17, FF3_6], 621=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    @NotYetImplemented
    public void offset__scroll() throws Exception {
        runTest("offset: scroll");
    }

    /**
     * Test {619=[FF10, IE9], 620=[FF17, FF3_6], 622=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void offset__body() throws Exception {
        runTest("offset: body");
    }

    /**
     * Test {620=[FF10, IE9], 621=[FF17, FF3_6], 623=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void offset__chaining() throws Exception {
        runTest("offset: chaining");
    }

    /**
     * Test {621=[FF10, IE9], 622=[FF17, FF3_6], 624=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void offset__offsetParent() throws Exception {
        runTest("offset: offsetParent");
    }

    /**
     * Test {622=[FF10, IE9], 623=[FF17, FF3_6], 625=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__fractions__see__7730_and__7885_() throws Exception {
        runTest("offset: fractions (see #7730 and #7885)");
    }

    /**
     * Test {623=[FF10, IE9], 624=[FF17, FF3_6], 626=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width__() throws Exception {
        runTest("dimensions: width()");
    }

    /**
     * Test {624=[FF10, IE9], 625=[FF17, FF3_6], 627=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width_Function_() throws Exception {
        runTest("dimensions: width(Function)");
    }

    /**
     * Test {625=[FF10, IE9], 626=[FF17, FF3_6], 628=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__width_Function_args__() throws Exception {
        runTest("dimensions: width(Function(args))");
    }

    /**
     * Test {626=[FF10, IE9], 627=[FF17, FF3_6], 629=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    @NotYetImplemented(IE)
    public void dimensions__height__() throws Exception {
        runTest("dimensions: height()");
    }

    /**
     * Test {627=[FF10, IE9], 628=[FF17, FF3_6], 630=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    @NotYetImplemented(IE)
    public void dimensions__height_Function_() throws Exception {
        runTest("dimensions: height(Function)");
    }

    /**
     * Test {628=[FF10, IE9], 629=[FF17, FF3_6], 631=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__height_Function_args__() throws Exception {
        runTest("dimensions: height(Function(args))");
    }

    /**
     * Test {629=[FF10, IE9], 630=[FF17, FF3_6], 632=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    @NotYetImplemented(FF)
    public void dimensions__innerWidth__() throws Exception {
        runTest("dimensions: innerWidth()");
    }

    /**
     * Test {630=[FF10, IE9], 631=[FF17, FF3_6], 633=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    @NotYetImplemented
    public void dimensions__innerHeight__() throws Exception {
        runTest("dimensions: innerHeight()");
    }

    /**
     * Test {631=[FF10, IE9], 632=[FF17, FF3_6], 634=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    @NotYetImplemented(FF)
    public void dimensions__outerWidth__() throws Exception {
        runTest("dimensions: outerWidth()");
    }

    /**
     * Test {632=[FF10, IE9], 633=[FF17, FF3_6], 635=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__9441__9300() throws Exception {
        runTest("dimensions: child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #9441 #9300");
    }

    /**
     * Test {633=[FF10, IE9], 634=[FF17, FF3_6], 636=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void dimensions__getting_dimensions_shouldnt_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldnt modify runtimeStyle see #9233");
    }

    /**
     * Test {634=[FF10, IE9], 635=[FF17, FF3_6], 637=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__table_dimensions() throws Exception {
        runTest("dimensions: table dimensions");
    }

    /**
     * Test {635=[FF10, IE9], 636=[FF17, FF3_6], 638=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__box_sizing_border_box_child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__10413() throws Exception {
        runTest("dimensions: box-sizing:border-box child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #10413");
    }

    /**
     * Test {636=[FF10, IE9], 637=[FF17, FF3_6], 639=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    @NotYetImplemented
    public void dimensions__outerHeight__() throws Exception {
        runTest("dimensions: outerHeight()");
    }

    /**
     * Test {637=[FF10, IE9], 638=[FF17, FF3_6], 640=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void dimensions__passing_undefined_is_a_setter__5571() throws Exception {
        runTest("dimensions: passing undefined is a setter #5571");
    }

    /**
     * Test {638=[FF10, IE9], 639=[FF17, FF3_6], 641=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void dimensions__getters_on_non_elements_should_return_null() throws Exception {
        runTest("dimensions: getters on non elements should return null");
    }

    /**
     * Test {639=[FF10, IE9], 640=[FF17, FF3_6], 642=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void dimensions__setters_with_and_without_box_sizing_border_box() throws Exception {
        runTest("dimensions: setters with and without box-sizing:border-box");
    }

    /**
     * Test {640=[FF10, IE9], 641=[FF17, FF3_6], 643=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void dimensions__window_vs__small_document() throws Exception {
        runTest("dimensions: window vs. small document");
    }

    /**
     * Test {641=[FF10, IE9], 642=[FF17, FF3_6], 644=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void dimensions__window_vs__large_document() throws Exception {
        runTest("dimensions: window vs. large document");
    }

    /**
     * Test {642=[FF10, IE9], 643=[FF17, FF3_6], 645=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 544, 544")
    public void deprecated__browser() throws Exception {
        runTest("deprecated: browser");
    }

    /**
     * Test {643=[FF10, IE9], 644=[FF17, FF3_6], 646=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void exports__amdModule() throws Exception {
        runTest("exports: amdModule");
    }

    /**
     * Test {644=[FF10, IE9], 645=[FF17, FF3_6], 647=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 36, 36")
    public void Sizzle__selector__element() throws Exception {
        runTest("Sizzle: selector: element");
    }

    /**
     * Test {645=[FF10, IE9], 646=[FF17, FF3_6], 648=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    @NotYetImplemented
    public void Sizzle__selector__XML_Document_Selectors() throws Exception {
        runTest("Sizzle: selector: XML Document Selectors");
    }

    /**
     * Test {646=[FF10, IE9], 647=[FF17, FF3_6], 649=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void Sizzle__selector__broken() throws Exception {
        runTest("Sizzle: selector: broken");
    }

    /**
     * Test {647=[FF10, IE9], 648=[FF17, FF3_6], 650=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void Sizzle__selector__id() throws Exception {
        runTest("Sizzle: selector: id");
    }

    /**
     * Test {648=[FF10, IE9], 649=[FF17, FF3_6], 651=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void Sizzle__selector__class() throws Exception {
        runTest("Sizzle: selector: class");
    }

    /**
     * Test {649=[FF10, IE9], 650=[FF17, FF3_6], 652=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void Sizzle__selector__name() throws Exception {
        runTest("Sizzle: selector: name");
    }

    /**
     * Test {650=[FF10, IE9], 651=[FF17, FF3_6], 653=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void Sizzle__selector__multiple() throws Exception {
        runTest("Sizzle: selector: multiple");
    }

    /**
     * Test {651=[FF10, IE9], 652=[FF17, FF3_6], 654=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    @NotYetImplemented(FF)
    public void Sizzle__selector__child_and_adjacent() throws Exception {
        runTest("Sizzle: selector: child and adjacent");
    }

    /**
     * Test {652=[FF10, IE9], 653=[FF17, FF3_6], 655=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 62, 62")
    public void Sizzle__selector__attributes() throws Exception {
        runTest("Sizzle: selector: attributes");
    }

    /**
     * Test {653=[FF10, IE9], 654=[FF17, FF3_6], 656=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    public void Sizzle__selector__pseudo___child() throws Exception {
        runTest("Sizzle: selector: pseudo - child");
    }

    /**
     * Test {654=[FF10, IE9], 655=[FF17, FF3_6], 657=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    @NotYetImplemented
    public void Sizzle__selector__pseudo___misc() throws Exception {
        runTest("Sizzle: selector: pseudo - misc");
    }

    /**
     * Test {655=[FF10, IE9], 656=[FF17, FF3_6], 658=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 43, 43")
    public void Sizzle__selector__pseudo____not() throws Exception {
        runTest("Sizzle: selector: pseudo - :not");
    }

    /**
     * Test {656=[FF10, IE9], 657=[FF17, FF3_6], 659=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 33, 33")
    public void Sizzle__selector__pseudo___position() throws Exception {
        runTest("Sizzle: selector: pseudo - position");
    }

    /**
     * Test {657=[FF10, IE9], 658=[FF17, FF3_6], 660=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void Sizzle__selector__pseudo___form() throws Exception {
        runTest("Sizzle: selector: pseudo - form");
    }

    /**
     * Test {658=[FF10, IE9], 659=[FF17, FF3_6], 661=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void Sizzle__selector__caching() throws Exception {
        runTest("Sizzle: selector: caching");
    }

    /**
     * Test {659=[FF10, IE9], 660=[FF17, FF3_6], 662=[IE6, IE7, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void Sizzle__selector__Sizzle_contains() throws Exception {
        runTest("Sizzle: selector: Sizzle.contains");
    }
}
