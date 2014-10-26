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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
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
     * Test {1=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__Unit_Testing_Environment() throws Exception {
        runTest("core: Unit Testing Environment");
    }

    /**
     * Test {2=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__Basic_requirements() throws Exception {
        runTest("core: Basic requirements");
    }

    /**
     * Test {3=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void core__jQuery__() throws Exception {
        runTest("core: jQuery()");
    }

    /**
     * Test {4=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void core__selector_state() throws Exception {
        runTest("core: selector state");
    }

    /**
     * Test {5=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__globalEval() throws Exception {
        runTest("core: globalEval");
    }

    /**
     * Test {6=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__noConflict() throws Exception {
        runTest("core: noConflict");
    }

    /**
     * Test {7=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void core__trim() throws Exception {
        runTest("core: trim");
    }

    /**
     * Test {8=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void core__type() throws Exception {
        runTest("core: type");
    }

    /**
     * Test {9=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void core__isPlainObject() throws Exception {
        runTest("core: isPlainObject");
    }

    /**
     * Test {10=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__isFunction() throws Exception {
        runTest("core: isFunction");
    }

    /**
     * Test {11=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 36, 36")
    public void core__isNumeric() throws Exception {
        runTest("core: isNumeric");
    }

    /**
     * Test {12=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__isXMLDoc___HTML() throws Exception {
        runTest("core: isXMLDoc - HTML");
    }

    /**
     * Test {13=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__XSS_via_location_hash() throws Exception {
        runTest("core: XSS via location.hash");
    }

    /**
     * Test {14=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__isXMLDoc___XML() throws Exception {
        runTest("core: isXMLDoc - XML");
    }

    /**
     * Test {15=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__isWindow() throws Exception {
        runTest("core: isWindow");
    }

    /**
     * Test {16=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery__html__() throws Exception {
        runTest("core: jQuery('html')");
    }

    /**
     * Test {17=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__jQuery__html___context_() throws Exception {
        runTest("core: jQuery('html', context)");
    }

    /**
     * Test {18=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_selector__xml__text_str____Loaded_via_XML_document() throws Exception {
        runTest("core: jQuery(selector, xml).text(str) - Loaded via XML document");
    }

    /**
     * Test {19=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__end__() throws Exception {
        runTest("core: end()");
    }

    /**
     * Test {20=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__length() throws Exception {
        runTest("core: length");
    }

    /**
     * Test {21=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__size__() throws Exception {
        runTest("core: size()");
    }

    /**
     * Test {22=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__get__() throws Exception {
        runTest("core: get()");
    }

    /**
     * Test {23=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__toArray__() throws Exception {
        runTest("core: toArray()");
    }

    /**
     * Test {24=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void core__inArray__() throws Exception {
        runTest("core: inArray()");
    }

    /**
     * Test {25=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get_Number_() throws Exception {
        runTest("core: get(Number)");
    }

    /**
     * Test {26=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__get__Number_() throws Exception {
        runTest("core: get(-Number)");
    }

    /**
     * Test {27=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void core__each_Function_() throws Exception {
        runTest("core: each(Function)");
    }

    /**
     * Test {28=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__slice__() throws Exception {
        runTest("core: slice()");
    }

    /**
     * Test {29=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void core__first___last__() throws Exception {
        runTest("core: first()/last()");
    }

    /**
     * Test {30=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__map__() throws Exception {
        runTest("core: map()");
    }

    /**
     * Test {31=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_merge__() throws Exception {
        runTest("core: jQuery.merge()");
    }

    /**
     * Test {32=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void core__jQuery_extend_Object__Object_() throws Exception {
        runTest("core: jQuery.extend(Object, Object)");
    }

    /**
     * Test {33=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void core__jQuery_each_Object_Function_() throws Exception {
        runTest("core: jQuery.each(Object,Function)");
    }

    /**
     * Test {34=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void core__jQuery_makeArray() throws Exception {
        runTest("core: jQuery.makeArray");
    }

    /**
     * Test {35=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void core__jQuery_inArray() throws Exception {
        runTest("core: jQuery.inArray");
    }

    /**
     * Test {36=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void core__jQuery_isEmptyObject() throws Exception {
        runTest("core: jQuery.isEmptyObject");
    }

    /**
     * Test {37=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_proxy() throws Exception {
        runTest("core: jQuery.proxy");
    }

    /**
     * Test {38=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void core__jQuery_parseHTML() throws Exception {
        runTest("core: jQuery.parseHTML");
    }

    /**
     * Test {39=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseJSON() throws Exception {
        runTest("core: jQuery.parseJSON");
    }

    /**
     * Test {40=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void core__jQuery_parseXML() throws Exception {
        runTest("core: jQuery.parseXML");
    }

    /**
     * Test {41=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void core__jQuery_sub_____Static_Methods() throws Exception {
        runTest("core: jQuery.sub() - Static Methods");
    }

    /**
     * Test {42=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 378, 378")
    public void core__jQuery_sub______fn_Methods() throws Exception {
        runTest("core: jQuery.sub() - .fn Methods");
    }

    /**
     * Test {43=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void core__jQuery_camelCase__() throws Exception {
        runTest("core: jQuery.camelCase()");
    }

    /**
     * Test {44=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - no filter");
    }

    /**
     * Test {45=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks__________no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - no filter");
    }

    /**
     * Test {46=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"\" ) - filter");
    }

    /**
     * Test {47=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks__________filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { } ) - filter");
    }

    /**
     * Test {48=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - no filter");
    }

    /**
     * Test {49=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - no filter");
    }

    /**
     * Test {50=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once\" ) - filter");
    }

    /**
     * Test {51=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true } ) - filter");
    }

    /**
     * Test {52=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - no filter");
    }

    /**
     * Test {53=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - no filter");
    }

    /**
     * Test {54=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory\" ) - filter");
    }

    /**
     * Test {55=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true } ) - filter");
    }

    /**
     * Test {56=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - no filter");
    }

    /**
     * Test {57=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - no filter");
    }

    /**
     * Test {58=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique\" ) - filter");
    }

    /**
     * Test {59=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true } ) - filter");
    }

    /**
     * Test {60=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - no filter");
    }

    /**
     * Test {61=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {62=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - filter");
    }

    /**
     * Test {63=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {64=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_memory______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - no filter");
    }

    /**
     * Test {65=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - no filter");
    }

    /**
     * Test {66=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_memory______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once memory\" ) - filter");
    }

    /**
     * Test {67=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___memory___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - filter");
    }

    /**
     * Test {68=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - no filter");
    }

    /**
     * Test {69=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {70=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once unique\" ) - filter");
    }

    /**
     * Test {71=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {72=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - no filter");
    }

    /**
     * Test {73=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {74=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___once_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - filter");
    }

    /**
     * Test {75=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____once___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {76=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_unique______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - no filter");
    }

    /**
     * Test {77=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - no filter");
    }

    /**
     * Test {78=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_unique______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory unique\" ) - filter");
    }

    /**
     * Test {79=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___unique___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - filter");
    }

    /**
     * Test {80=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - no filter");
    }

    /**
     * Test {81=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {82=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___memory_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - filter");
    }

    /**
     * Test {83=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____memory___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {84=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - no filter");
    }

    /**
     * Test {85=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______no_filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - no filter");
    }

    /**
     * Test {86=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks___unique_stopOnFalse______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - filter");
    }

    /**
     * Test {87=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void callbacks__jQuery_Callbacks_____unique___true___stopOnFalse___true_______filter() throws Exception {
        runTest("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - filter");
    }

    /**
     * Test {88=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks__options_____options_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks( options ) - options are copied");
    }

    /**
     * Test {89=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_fireWith___arguments_are_copied() throws Exception {
        runTest("callbacks: jQuery.Callbacks.fireWith - arguments are copied");
    }

    /**
     * Test {90=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_remove___should_remove_all_instances() throws Exception {
        runTest("callbacks: jQuery.Callbacks.remove - should remove all instances");
    }

    /**
     * Test {91=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void callbacks__jQuery_Callbacks_____adding_a_string_doesn_t_cause_a_stack_overflow() throws Exception {
        runTest("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow");
    }

    /**
     * Test {92=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred() throws Exception {
        runTest("deferred: jQuery.Deferred");
    }

    /**
     * Test {93=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void deferred__jQuery_Deferred___new_operator() throws Exception {
        runTest("deferred: jQuery.Deferred - new operator");
    }

    /**
     * Test {94=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void deferred__jQuery_Deferred___chainability() throws Exception {
        runTest("deferred: jQuery.Deferred - chainability");
    }

    /**
     * Test {95=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (done)");
    }

    /**
     * Test {96=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___filtering__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (fail)");
    }

    /**
     * Test {97=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___filtering__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - filtering (progress)");
    }

    /**
     * Test {98=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__done_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (done)");
    }

    /**
     * Test {99=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__fail_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (fail)");
    }

    /**
     * Test {100=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void deferred__jQuery_Deferred_then___deferred__progress_() throws Exception {
        runTest("deferred: jQuery.Deferred.then - deferred (progress)");
    }

    /**
     * Test {101=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void deferred__jQuery_Deferred_then___context() throws Exception {
        runTest("deferred: jQuery.Deferred.then - context");
    }

    /**
     * Test {102=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 34, 34")
    public void deferred__jQuery_when() throws Exception {
        runTest("deferred: jQuery.when");
    }

    /**
     * Test {103=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 119, 119")
    public void deferred__jQuery_when___joined() throws Exception {
        runTest("deferred: jQuery.when - joined");
    }

    /**
     * Test {104=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void support__boxModel() throws Exception {
        runTest("support: boxModel");
    }

    /**
     * Test {105=[CHROME, FF24, FF31, IE11, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF24 = "0, 2, 2",
        FF31 = "0, 2, 2",
        IE11 = "0, 2, 2",
        IE8 = "9, 1, 10")
    @NotYetImplemented(IE8)
    public void support__body_background_is_not_lost_if_set_prior_to_loading_jQuery___9238_() throws Exception {
        runTest("support: body background is not lost if set prior to loading jQuery (#9238)");
    }

    /**
     * Test {106=[CHROME, FF24, FF31, IE11, IE8]}.
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
     * Test {107=[CHROME, FF24, FF31, IE11], 108=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__expando() throws Exception {
        runTest("data: expando");
    }

    /**
     * Test {108=[CHROME, FF24, FF31, IE11], 109=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 124, 124")
    public void data__jQuery_data() throws Exception {
        runTest("data: jQuery.data");
    }

    /**
     * Test {109=[CHROME, FF24, FF31, IE11], 110=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void data__jQuery_acceptData() throws Exception {
        runTest("data: jQuery.acceptData");
    }

    /**
     * Test {110=[CHROME, FF24, FF31, IE11], 111=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void data___data__() throws Exception {
        runTest("data: .data()");
    }

    /**
     * Test {111=[CHROME, FF24, FF31, IE11], 112=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void data___data_String__and__data_String__Object_() throws Exception {
        runTest("data: .data(String) and .data(String, Object)");
    }

    /**
     * Test {112=[CHROME, FF24, FF31, IE11], 113=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 40, 40")
    public void data__data___attributes() throws Exception {
        runTest("data: data-* attributes");
    }

    /**
     * Test {113=[CHROME, FF24, FF31, IE11], 114=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void data___data_Object_() throws Exception {
        runTest("data: .data(Object)");
    }

    /**
     * Test {114=[CHROME, FF24, FF31, IE11], 115=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_removeData() throws Exception {
        runTest("data: jQuery.removeData");
    }

    /**
     * Test {115=[CHROME, FF24, FF31, IE11], 116=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void data___removeData__() throws Exception {
        runTest("data: .removeData()");
    }

    /**
     * Test {116=[CHROME, FF24, FF31, IE11], 117=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_serialization___8108_() throws Exception {
        runTest("data: JSON serialization (#8108)");
    }

    /**
     * Test {117=[CHROME, FF24, FF31, IE11], 118=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void data__jQuery_data_should_follow_html5_specification_regarding_camel_casing() throws Exception {
        runTest("data: jQuery.data should follow html5 specification regarding camel casing");
    }

    /**
     * Test {118=[CHROME, FF24, FF31, IE11], 119=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__jQuery_data_should_not_miss_data_with_preset_hyphenated_property_names() throws Exception {
        runTest("data: jQuery.data should not miss data with preset hyphenated property names");
    }

    /**
     * Test {119=[CHROME, FF24, FF31, IE11], 120=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void data__jQuery_data_supports_interoperable_hyphenated_camelCase_get_set_of_properties_with_arbitrary_non_null_NaN_undefined_values() throws Exception {
        runTest("data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary non-null|NaN|undefined values");
    }

    /**
     * Test {120=[CHROME, FF24, FF31, IE11], 121=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void data__jQuery_data_supports_interoperable_removal_of_hyphenated_camelCase_properties() throws Exception {
        runTest("data: jQuery.data supports interoperable removal of hyphenated/camelCase properties");
    }

    /**
     * Test {121=[CHROME, FF24, FF31, IE11], 122=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__Triggering_the_removeData_should_not_throw_exceptions____10080_() throws Exception {
        runTest("data: Triggering the removeData should not throw exceptions. (#10080)");
    }

    /**
     * Test {122=[CHROME, FF24, FF31, IE11], 123=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void data__Only_check_element_attributes_once_when_calling__data______8909() throws Exception {
        runTest("data: Only check element attributes once when calling .data() - #8909");
    }

    /**
     * Test {123=[CHROME, FF24, FF31, IE11], 124=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void data__JSON_data__attributes_can_have_newlines() throws Exception {
        runTest("data: JSON data- attributes can have newlines");
    }

    /**
     * Test {124=[CHROME, FF24, FF31, IE11], 125=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void queue__queue___with_other_types() throws Exception {
        runTest("queue: queue() with other types");
    }

    /**
     * Test {125=[CHROME, FF24, FF31, IE11], 126=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_name__passes_in_the_next_item_in_the_queue_as_a_parameter() throws Exception {
        runTest("queue: queue(name) passes in the next item in the queue as a parameter");
    }

    /**
     * Test {126=[CHROME, FF24, FF31, IE11], 127=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__queue___passes_in_the_next_item_in_the_queue_as_a_parameter_to_fx_queues() throws Exception {
        runTest("queue: queue() passes in the next item in the queue as a parameter to fx queues");
    }

    /**
     * Test {127=[CHROME, FF24, FF31, IE11], 128=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__callbacks_keep_their_place_in_the_queue() throws Exception {
        runTest("queue: callbacks keep their place in the queue");
    }

    /**
     * Test {128=[CHROME, FF24, FF31, IE11], 129=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__delay__() throws Exception {
        runTest("queue: delay()");
    }

    /**
     * Test {129=[CHROME, FF24, FF31, IE11], 130=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__clearQueue_name__clears_the_queue() throws Exception {
        runTest("queue: clearQueue(name) clears the queue");
    }

    /**
     * Test {130=[CHROME, FF24, FF31, IE11], 131=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void queue__clearQueue___clears_the_fx_queue() throws Exception {
        runTest("queue: clearQueue() clears the fx queue");
    }

    /**
     * Test {131=[CHROME, FF24, FF31, IE11], 132=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__fn_promise_____called_when_fx_queue_is_empty() throws Exception {
        runTest("queue: fn.promise() - called when fx queue is empty");
    }

    /**
     * Test {132=[CHROME, FF24, FF31, IE11], 133=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void queue__fn_promise___queue______called_whenever_last_queue_function_is_dequeued() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued");
    }

    /**
     * Test {133=[CHROME, FF24, FF31, IE11], 134=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__fn_promise___queue______waits_for_animation_to_complete_before_resolving() throws Exception {
        runTest("queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving");
    }

    /**
     * Test {134=[CHROME, FF24, FF31, IE11], 135=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue___promise_obj_() throws Exception {
        runTest("queue: .promise(obj)");
    }

    /**
     * Test {135=[CHROME, FF24, FF31, IE11], 136=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void queue__delay___can_be_stopped() throws Exception {
        runTest("queue: delay() can be stopped");
    }

    /**
     * Test {136=[CHROME, FF24, FF31, IE11], 137=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void queue__queue_stop_hooks() throws Exception {
        runTest("queue: queue stop hooks");
    }

    /**
     * Test {137=[CHROME, FF24, FF31, IE11], 138=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__jQuery_propFix_integrity_test() throws Exception {
        runTest("attributes: jQuery.propFix integrity test");
    }

    /**
     * Test {138=[CHROME, FF24, FF31, IE11], 139=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 46, 46")
    public void attributes__attr_String_() throws Exception {
        runTest("attributes: attr(String)");
    }

    /**
     * Test {139=[CHROME, FF24, FF31, IE11], 140=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_String__in_XML_Files() throws Exception {
        runTest("attributes: attr(String) in XML Files");
    }

    /**
     * Test {140=[CHROME, FF24, FF31, IE11], 141=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Function_() throws Exception {
        runTest("attributes: attr(String, Function)");
    }

    /**
     * Test {141=[CHROME, FF24, FF31, IE11], 142=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__attr_Hash_() throws Exception {
        runTest("attributes: attr(Hash)");
    }

    /**
     * Test {142=[CHROME, FF24, FF31, IE11], 143=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 81, 81")
    public void attributes__attr_String__Object_() throws Exception {
        runTest("attributes: attr(String, Object)");
    }

    /**
     * Test {143=[CHROME, FF24, FF31, IE11], 144=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr_jquery_method_() throws Exception {
        runTest("attributes: attr(jquery_method)");
    }

    /**
     * Test {144=[CHROME, FF24, FF31, IE11], 145=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__attr_String__Object____Loaded_via_XML_document() throws Exception {
        runTest("attributes: attr(String, Object) - Loaded via XML document");
    }

    /**
     * Test {145=[CHROME, FF24, FF31, IE11], 146=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__attr__tabindex__() throws Exception {
        runTest("attributes: attr('tabindex')");
    }

    /**
     * Test {146=[CHROME, FF24, FF31, IE11], 147=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__attr__tabindex___value_() throws Exception {
        runTest("attributes: attr('tabindex', value)");
    }

    /**
     * Test {147=[CHROME, FF24, FF31, IE11], 148=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void attributes__removeAttr_String_() throws Exception {
        runTest("attributes: removeAttr(String)");
    }

    /**
     * Test {148=[CHROME, FF24, FF31, IE11], 149=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeAttr_String__in_XML() throws Exception {
        runTest("attributes: removeAttr(String) in XML");
    }

    /**
     * Test {149=[CHROME, FF24, FF31, IE11], 150=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__removeAttr_Multi_String__variable_space_width_() throws Exception {
        runTest("attributes: removeAttr(Multi String, variable space width)");
    }

    /**
     * Test {150=[CHROME, FF24, FF31, IE11], 151=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void attributes__prop_String__Object_() throws Exception {
        runTest("attributes: prop(String, Object)");
    }

    /**
     * Test {151=[CHROME, FF24, FF31, IE11], 152=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__prop__tabindex__() throws Exception {
        runTest("attributes: prop('tabindex')");
    }

    /**
     * Test {152=[CHROME, FF24, FF31, IE11], 153=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__prop__tabindex___value_() throws Exception {
        runTest("attributes: prop('tabindex', value)");
    }

    /**
     * Test {153=[CHROME, FF24, FF31, IE11], 154=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void attributes__removeProp_String_() throws Exception {
        runTest("attributes: removeProp(String)");
    }

    /**
     * Test {154=[CHROME, FF24, FF31, IE11], 155=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void attributes__val__() throws Exception {
        runTest("attributes: val()");
    }

    /**
     * Test {155=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 4, 4",
        FF24 = "0, 4, 4",
        FF31 = "0, 4, 4")
    public void attributes__val___respects_numbers_without_exception__Bug__9319_() throws Exception {
        runTest("attributes: val() respects numbers without exception (Bug #9319)");
    }

    /**
     * Test {155=[IE11], 156=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__val_String_Number_() throws Exception {
        runTest("attributes: val(String/Number)");
    }

    /**
     * Test {156=[IE11], 157=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void attributes__val_Function_() throws Exception {
        runTest("attributes: val(Function)");
    }

    /**
     * Test {157=[IE11], 158=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void attributes__val_Array_of_Numbers___Bug__7123_() throws Exception {
        runTest("attributes: val(Array of Numbers) (Bug #7123)");
    }

    /**
     * Test {158=[IE11], 159=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void attributes__val_Function__with_incoming_value() throws Exception {
        runTest("attributes: val(Function) with incoming value");
    }

    /**
     * Test {159=[IE11], 160=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void attributes__val_select__after_form_reset____Bug__2551_() throws Exception {
        runTest("attributes: val(select) after form.reset() (Bug #2551)");
    }

    /**
     * Test {160=[IE11], 161=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_String_() throws Exception {
        runTest("attributes: addClass(String)");
    }

    /**
     * Test {161=[IE11], 162=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void attributes__addClass_Function_() throws Exception {
        runTest("attributes: addClass(Function)");
    }

    /**
     * Test {162=[IE11], 163=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 48, 48")
    public void attributes__addClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: addClass(Function) with incoming value");
    }

    /**
     * Test {163=[IE11], 164=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeClass_String____simple() throws Exception {
        runTest("attributes: removeClass(String) - simple");
    }

    /**
     * Test {164=[IE11], 165=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void attributes__removeClass_Function____simple() throws Exception {
        runTest("attributes: removeClass(Function) - simple");
    }

    /**
     * Test {165=[IE11], 166=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 48, 48")
    public void attributes__removeClass_Function__with_incoming_value() throws Exception {
        runTest("attributes: removeClass(Function) with incoming value");
    }

    /**
     * Test {166=[IE11], 167=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void attributes__removeClass___removes_duplicates() throws Exception {
        runTest("attributes: removeClass() removes duplicates");
    }

    /**
     * Test {167=[IE11], 168=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_String_boolean_undefined___boolean__() throws Exception {
        runTest("attributes: toggleClass(String|boolean|undefined[, boolean])");
    }

    /**
     * Test {168=[IE11], 169=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__toggleClass_Function___boolean__() throws Exception {
        runTest("attributes: toggleClass(Function[, boolean])");
    }

    /**
     * Test {169=[IE11], 170=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void attributes__toggleClass_Fucntion___boolean___with_incoming_value() throws Exception {
        runTest("attributes: toggleClass(Fucntion[, boolean]) with incoming value");
    }

    /**
     * Test {170=[IE11], 171=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void attributes__addClass__removeClass__hasClass() throws Exception {
        runTest("attributes: addClass, removeClass, hasClass");
    }

    /**
     * Test {171=[IE11], 172=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__contents___hasClass___returns_correct_values() throws Exception {
        runTest("attributes: contents().hasClass() returns correct values");
    }

    /**
     * Test {172=[IE11], 173=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void attributes__coords_returns_correct_values_in_IE6_IE7__see__10828() throws Exception {
        runTest("attributes: coords returns correct values in IE6/IE7, see #10828");
    }

    /**
     * Test {173=[IE11], 174=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__null_or_undefined_handler() throws Exception {
        runTest("event: null or undefined handler");
    }

    /**
     * Test {174=[IE11], 175=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__bind___live___delegate___with_non_null_defined_data() throws Exception {
        runTest("event: bind(),live(),delegate() with non-null,defined data");
    }

    /**
     * Test {175=[IE11], 176=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Handler_changes_and__trigger___order() throws Exception {
        runTest("event: Handler changes and .trigger() order");
    }

    /**
     * Test {176=[IE11], 177=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_data() throws Exception {
        runTest("event: bind(), with data");
    }

    /**
     * Test {177=[IE11], 178=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__click____with_data() throws Exception {
        runTest("event: click(), with data");
    }

    /**
     * Test {178=[IE11], 179=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_data__trigger_with_data() throws Exception {
        runTest("event: bind(), with data, trigger with data");
    }

    /**
     * Test {179=[IE11], 180=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind____multiple_events_at_once() throws Exception {
        runTest("event: bind(), multiple events at once");
    }

    /**
     * Test {180=[IE11], 181=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____five_events_at_once() throws Exception {
        runTest("event: bind(), five events at once");
    }

    /**
     * Test {181=[IE11], 182=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__bind____multiple_events_at_once_and_namespaces() throws Exception {
        runTest("event: bind(), multiple events at once and namespaces");
    }

    /**
     * Test {182=[IE11], 183=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 27, 27")
    public void event__bind____namespace_with_special_add() throws Exception {
        runTest("event: bind(), namespace with special add");
    }

    /**
     * Test {183=[IE11], 184=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____no_data() throws Exception {
        runTest("event: bind(), no data");
    }

    /**
     * Test {184=[IE11], 185=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__bind_one_unbind_Object_() throws Exception {
        runTest("event: bind/one/unbind(Object)");
    }

    /**
     * Test {185=[IE11], 186=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__live_die_Object___delegate_undelegate_String__Object_() throws Exception {
        runTest("event: live/die(Object), delegate/undelegate(String, Object)");
    }

    /**
     * Test {186=[IE11], 187=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__live_delegate_immediate_propagation() throws Exception {
        runTest("event: live/delegate immediate propagation");
    }

    /**
     * Test {187=[IE11], 188=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind_delegate_bubbling__isDefaultPrevented() throws Exception {
        runTest("event: bind/delegate bubbling, isDefaultPrevented");
    }

    /**
     * Test {188=[IE11], 189=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____iframes() throws Exception {
        runTest("event: bind(), iframes");
    }

    /**
     * Test {189=[IE11], 190=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__bind____trigger_change_on_select() throws Exception {
        runTest("event: bind(), trigger change on select");
    }

    /**
     * Test {190=[IE11], 191=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__bind____namespaced_events__cloned_events() throws Exception {
        runTest("event: bind(), namespaced events, cloned events");
    }

    /**
     * Test {191=[IE11], 192=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__bind____multi_namespaced_events() throws Exception {
        runTest("event: bind(), multi-namespaced events");
    }

    /**
     * Test {192=[IE11], 193=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__bind____with_same_function() throws Exception {
        runTest("event: bind(), with same function");
    }

    /**
     * Test {193=[IE11], 194=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__bind____make_sure_order_is_maintained() throws Exception {
        runTest("event: bind(), make sure order is maintained");
    }

    /**
     * Test {194=[IE11], 195=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__bind____with_different_this_object() throws Exception {
        runTest("event: bind(), with different this object");
    }

    /**
     * Test {195=[IE11], 196=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__bind_name__false___unbind_name__false_() throws Exception {
        runTest("event: bind(name, false), unbind(name, false)");
    }

    /**
     * Test {196=[IE11], 197=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__live_name__false___die_name__false_() throws Exception {
        runTest("event: live(name, false), die(name, false)");
    }

    /**
     * Test {197=[IE11], 198=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegate_selector__name__false___undelegate_selector__name__false_() throws Exception {
        runTest("event: delegate(selector, name, false), undelegate(selector, name, false)");
    }

    /**
     * Test {198=[IE11], 199=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__bind___trigger___unbind___on_plain_object() throws Exception {
        runTest("event: bind()/trigger()/unbind() on plain object");
    }

    /**
     * Test {199=[IE11], 200=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__unbind_type_() throws Exception {
        runTest("event: unbind(type)");
    }

    /**
     * Test {200=[IE11], 201=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void event__unbind_eventObject_() throws Exception {
        runTest("event: unbind(eventObject)");
    }

    /**
     * Test {201=[IE11], 202=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__hover___and_hover_pseudo_event() throws Exception {
        runTest("event: hover() and hover pseudo-event");
    }

    /**
     * Test {202=[IE11], 203=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__mouseover_triggers_mouseenter() throws Exception {
        runTest("event: mouseover triggers mouseenter");
    }

    /**
     * Test {203=[IE11], 204=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__withinElement_implemented_with_jQuery_contains__() throws Exception {
        runTest("event: withinElement implemented with jQuery.contains()");
    }

    /**
     * Test {204=[IE11], 205=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__mouseenter__mouseleave_don_t_catch_exceptions() throws Exception {
        runTest("event: mouseenter, mouseleave don't catch exceptions");
    }

    /**
     * Test {205=[IE11], 206=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void event__trigger___shortcuts() throws Exception {
        runTest("event: trigger() shortcuts");
    }

    /**
     * Test {206=[IE11], 207=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void event__trigger___bubbling() throws Exception {
        runTest("event: trigger() bubbling");
    }

    /**
     * Test {207=[IE11], 208=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__trigger_type___data____fn__() throws Exception {
        runTest("event: trigger(type, [data], [fn])");
    }

    /**
     * Test {208=[IE11], 209=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE8)
    public void event__submit_event_bubbles_on_copied_forms___11649_() throws Exception {
        runTest("event: submit event bubbles on copied forms (#11649)");
    }

    /**
     * Test {209=[IE11], 210=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__change_event_bubbles_on_copied_forms___11796_() throws Exception {
        runTest("event: change event bubbles on copied forms (#11796)");
    }

    /**
     * Test {210=[IE11], 211=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 28, 28")
    public void event__trigger_eventObject___data____fn__() throws Exception {
        runTest("event: trigger(eventObject, [data], [fn])");
    }

    /**
     * Test {211=[IE11], 212=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event___trigger___bubbling_on_disconnected_elements___10489_() throws Exception {
        runTest("event: .trigger() bubbling on disconnected elements (#10489)");
    }

    /**
     * Test {212=[IE11], 213=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___trigger___doesn_t_bubble_load_event___10717_() throws Exception {
        runTest("event: .trigger() doesn't bubble load event (#10717)");
    }

    /**
     * Test {213=[IE11], 214=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented({ FF, IE11 })
    public void event__Delegated_events_in_SVG___10791_() throws Exception {
        runTest("event: Delegated events in SVG (#10791)");
    }

    /**
     * Test {214=[IE11], 215=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__Delegated_events_in_forms___10844___11145___8165___11382___11764_() throws Exception {
        runTest("event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764)");
    }

    /**
     * Test {215=[IE11], 216=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Submit_event_can_be_stopped___11049_() throws Exception {
        runTest("event: Submit event can be stopped (#11049)");
    }

    /**
     * Test {216=[IE11], 217=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__on_beforeunload__creates_deletes_window_property_instead_of_adding_removing_event_listener() throws Exception {
        runTest("event: on(beforeunload) creates/deletes window property instead of adding/removing event listener");
    }

    /**
     * Test {217=[IE11], 218=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void event__jQuery_Event__type__props__() throws Exception {
        runTest("event: jQuery.Event( type, props )");
    }

    /**
     * Test {218=[IE11], 219=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_Event_currentTarget() throws Exception {
        runTest("event: jQuery.Event.currentTarget");
    }

    /**
     * Test {219=[IE11], 220=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void event__toggle_Function__Function______() throws Exception {
        runTest("event: toggle(Function, Function, ...)");
    }

    /**
     * Test {220=[IE11], 221=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 66, 66")
    public void event___live____die__() throws Exception {
        runTest("event: .live()/.die()");
    }

    /**
     * Test {221=[IE11], 222=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__die_all_bound_events() throws Exception {
        runTest("event: die all bound events");
    }

    /**
     * Test {222=[IE11], 223=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__live_with_multiple_events() throws Exception {
        runTest("event: live with multiple events");
    }

    /**
     * Test {223=[IE11], 224=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void event__live_with_namespaces() throws Exception {
        runTest("event: live with namespaces");
    }

    /**
     * Test {224=[IE11], 225=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__live_with_change() throws Exception {
        runTest("event: live with change");
    }

    /**
     * Test {225=[IE11], 226=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__live_with_submit() throws Exception {
        runTest("event: live with submit");
    }

    /**
     * Test {226=[IE11], 227=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 13, 13")
    public void event__live_with_special_events() throws Exception {
        runTest("event: live with special events");
    }

    /**
     * Test {227=[IE11], 228=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 65, 65")
    public void event___delegate____undelegate__() throws Exception {
        runTest("event: .delegate()/.undelegate()");
    }

    /**
     * Test {228=[IE11], 229=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_off_using_dispatched_jQuery_Event() throws Exception {
        runTest("event: jQuery.off using dispatched jQuery.Event");
    }

    /**
     * Test {229=[IE11], 230=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__delegated_event_with_delegateTarget_relative_selector() throws Exception {
        runTest("event: delegated event with delegateTarget-relative selector");
    }

    /**
     * Test {230=[IE11], 231=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__stopPropagation___stops_directly_bound_events_on_delegated_target() throws Exception {
        runTest("event: stopPropagation() stops directly-bound events on delegated target");
    }

    /**
     * Test {231=[IE11], 232=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__undelegate_all_bound_events() throws Exception {
        runTest("event: undelegate all bound events");
    }

    /**
     * Test {232=[IE11], 233=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__delegate_with_multiple_events() throws Exception {
        runTest("event: delegate with multiple events");
    }

    /**
     * Test {233=[IE11], 234=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void event__delegate_with_change() throws Exception {
        runTest("event: delegate with change");
    }

    /**
     * Test {234=[IE11], 235=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__delegate_with_submit() throws Exception {
        runTest("event: delegate with submit");
    }

    /**
     * Test {235=[IE11], 236=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__undelegate___with_only_namespaces() throws Exception {
        runTest("event: undelegate() with only namespaces");
    }

    /**
     * Test {236=[IE11], 237=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__Non_DOM_element_events() throws Exception {
        runTest("event: Non DOM element events");
    }

    /**
     * Test {237=[IE11], 238=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__inline_handler_returning_false_stops_default() throws Exception {
        runTest("event: inline handler returning false stops default");
    }

    /**
     * Test {238=[IE11], 239=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__window_resize() throws Exception {
        runTest("event: window resize");
    }

    /**
     * Test {239=[IE11], 240=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__focusin_bubbles() throws Exception {
        runTest("event: focusin bubbles");
    }

    /**
     * Test {240=[IE11], 241=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__custom_events_with_colons___3533___8272_() throws Exception {
        runTest("event: custom events with colons (#3533, #8272)");
    }

    /**
     * Test {241=[IE11], 242=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void event___on_and__off() throws Exception {
        runTest("event: .on and .off");
    }

    /**
     * Test {242=[IE11], 243=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event__special_bind_delegate_name_mapping() throws Exception {
        runTest("event: special bind/delegate name mapping");
    }

    /**
     * Test {243=[IE11], 244=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void event___on_and__off__selective_mixed_removal___10705_() throws Exception {
        runTest("event: .on and .off, selective mixed removal (#10705)");
    }

    /**
     * Test {244=[IE11], 245=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event___on__event_map__null_selector__data____11130() throws Exception {
        runTest("event: .on( event-map, null-selector, data ) #11130");
    }

    /**
     * Test {245=[IE11], 246=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void event__clone___delegated_events___11076_() throws Exception {
        runTest("event: clone() delegated events (#11076)");
    }

    /**
     * Test {246=[IE11], 247=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__fixHooks_extensions() throws Exception {
        runTest("event: fixHooks extensions");
    }

    /**
     * Test {247=[IE11], 248=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_promise() throws Exception {
        runTest("event: jQuery.ready promise");
    }

    /**
     * Test {248=[IE11], 249=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__jQuery_ready_synchronous_load_with_long_loading_subresources() throws Exception {
        runTest("event: jQuery.ready synchronous load with long loading subresources");
    }

    /**
     * Test {249=[IE11], 250=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__jQuery_isReady() throws Exception {
        runTest("event: jQuery.isReady");
    }

    /**
     * Test {250=[IE11], 251=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void event__jQuery_ready() throws Exception {
        runTest("event: jQuery ready");
    }

    /**
     * Test {251=[IE11], 252=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void event__change_handler_should_be_detached_from_element() throws Exception {
        runTest("event: change handler should be detached from element");
    }

    /**
     * Test {252=[IE11], 253=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void event__trigger_click_on_checkbox__fires_change_event() throws Exception {
        runTest("event: trigger click on checkbox, fires change event");
    }

    /**
     * Test {253=[IE11], 254=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void selector___jQuery_only__element___jQuery_only() throws Exception {
        runTest("selector - jQuery only: element - jQuery only");
    }

    /**
     * Test {254=[IE11], 255=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector___jQuery_only__class___jQuery_only() throws Exception {
        runTest("selector - jQuery only: class - jQuery only");
    }

    /**
     * Test {255=[IE11], 256=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void selector___jQuery_only__attributes___jQuery_only() throws Exception {
        runTest("selector - jQuery only: attributes - jQuery only");
    }

    /**
     * Test {256=[IE11], 257=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void selector___jQuery_only__pseudo___visibility() throws Exception {
        runTest("selector - jQuery only: pseudo - visibility");
    }

    /**
     * Test {257=[IE11], 258=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void selector___jQuery_only__disconnected_nodes() throws Exception {
        runTest("selector - jQuery only: disconnected nodes");
    }

    /**
     * Test {258=[IE11], 259=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    public void selector___jQuery_only__attributes___jQuery_attr() throws Exception {
        runTest("selector - jQuery only: attributes - jQuery.attr");
    }

    /**
     * Test {259=[IE11], 260=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void selector___jQuery_only__Sizzle_cache_collides_with_multiple_Sizzles_on_a_page() throws Exception {
        runTest("selector - jQuery only: Sizzle cache collides with multiple Sizzles on a page");
    }

    /**
     * Test {260=[IE11], 261=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__find_String_() throws Exception {
        runTest("traversing: find(String)");
    }

    /**
     * Test {261=[IE11], 262=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__find_node_jQuery_object_() throws Exception {
        runTest("traversing: find(node|jQuery object)");
    }

    /**
     * Test {262=[IE11], 263=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    public void traversing__is_String_undefined_() throws Exception {
        runTest("traversing: is(String|undefined)");
    }

    /**
     * Test {263=[IE11], 264=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void traversing__is_jQuery_() throws Exception {
        runTest("traversing: is(jQuery)");
    }

    /**
     * Test {264=[IE11], 265=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void traversing__is___with_positional_selectors() throws Exception {
        runTest("traversing: is() with positional selectors");
    }

    /**
     * Test {265=[IE11], 266=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__index__() throws Exception {
        runTest("traversing: index()");
    }

    /**
     * Test {266=[IE11], 267=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__index_Object_String_undefined_() throws Exception {
        runTest("traversing: index(Object|String|undefined)");
    }

    /**
     * Test {267=[IE11], 268=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__filter_Selector_undefined_() throws Exception {
        runTest("traversing: filter(Selector|undefined)");
    }

    /**
     * Test {268=[IE11], 269=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__filter_Function_() throws Exception {
        runTest("traversing: filter(Function)");
    }

    /**
     * Test {269=[IE11], 270=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Element_() throws Exception {
        runTest("traversing: filter(Element)");
    }

    /**
     * Test {270=[IE11], 271=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_Array_() throws Exception {
        runTest("traversing: filter(Array)");
    }

    /**
     * Test {271=[IE11], 272=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__filter_jQuery_() throws Exception {
        runTest("traversing: filter(jQuery)");
    }

    /**
     * Test {272=[IE11], 273=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void traversing__filter___with_positional_selectors() throws Exception {
        runTest("traversing: filter() with positional selectors");
    }

    /**
     * Test {273=[IE11], 274=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void traversing__closest__() throws Exception {
        runTest("traversing: closest()");
    }

    /**
     * Test {274=[IE11], 275=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void traversing__closest_jQuery_() throws Exception {
        runTest("traversing: closest(jQuery)");
    }

    /**
     * Test {275=[IE11], 276=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__not_Selector_undefined_() throws Exception {
        runTest("traversing: not(Selector|undefined)");
    }

    /**
     * Test {276=[IE11], 277=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Element_() throws Exception {
        runTest("traversing: not(Element)");
    }

    /**
     * Test {277=[IE11], 278=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_Function_() throws Exception {
        runTest("traversing: not(Function)");
    }

    /**
     * Test {278=[IE11], 279=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void traversing__not_Array_() throws Exception {
        runTest("traversing: not(Array)");
    }

    /**
     * Test {279=[IE11], 280=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void traversing__not_jQuery_() throws Exception {
        runTest("traversing: not(jQuery)");
    }

    /**
     * Test {280=[IE11], 281=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__has_Element_() throws Exception {
        runTest("traversing: has(Element)");
    }

    /**
     * Test {281=[IE11], 282=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__has_Selector_() throws Exception {
        runTest("traversing: has(Selector)");
    }

    /**
     * Test {282=[IE11], 283=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__has_Arrayish_() throws Exception {
        runTest("traversing: has(Arrayish)");
    }

    /**
     * Test {283=[IE11], 284=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__addBack__() throws Exception {
        runTest("traversing: addBack()");
    }

    /**
     * Test {284=[IE11], 285=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void traversing__siblings__String__() throws Exception {
        runTest("traversing: siblings([String])");
    }

    /**
     * Test {285=[IE11], 286=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__children__String__() throws Exception {
        runTest("traversing: children([String])");
    }

    /**
     * Test {286=[IE11], 287=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__parent__String__() throws Exception {
        runTest("traversing: parent([String])");
    }

    /**
     * Test {287=[IE11], 288=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__parents__String__() throws Exception {
        runTest("traversing: parents([String])");
    }

    /**
     * Test {288=[IE11], 289=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void traversing__parentsUntil__String__() throws Exception {
        runTest("traversing: parentsUntil([String])");
    }

    /**
     * Test {289=[IE11], 290=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void traversing__next__String__() throws Exception {
        runTest("traversing: next([String])");
    }

    /**
     * Test {290=[IE11], 291=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__prev__String__() throws Exception {
        runTest("traversing: prev([String])");
    }

    /**
     * Test {291=[IE11], 292=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__nextAll__String__() throws Exception {
        runTest("traversing: nextAll([String])");
    }

    /**
     * Test {292=[IE11], 293=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void traversing__prevAll__String__() throws Exception {
        runTest("traversing: prevAll([String])");
    }

    /**
     * Test {293=[IE11], 294=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void traversing__nextUntil__String__() throws Exception {
        runTest("traversing: nextUntil([String])");
    }

    /**
     * Test {294=[IE11], 295=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void traversing__prevUntil__String__() throws Exception {
        runTest("traversing: prevUntil([String])");
    }

    /**
     * Test {295=[IE11], 296=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void traversing__contents__() throws Exception {
        runTest("traversing: contents()");
    }

    /**
     * Test {296=[IE11], 297=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void traversing__add_String_Element_Array_undefined_() throws Exception {
        runTest("traversing: add(String|Element|Array|undefined)");
    }

    /**
     * Test {297=[IE11], 298=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void traversing__add_String__Context_() throws Exception {
        runTest("traversing: add(String, Context)");
    }

    /**
     * Test {298=[IE11], 299=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void traversing__eq___1____10616() throws Exception {
        runTest("traversing: eq('-1') #10616");
    }

    /**
     * Test {299=[IE11], 300=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__text__() throws Exception {
        runTest("manipulation: text()");
    }

    /**
     * Test {300=[IE11], 301=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__text_undefined_() throws Exception {
        runTest("manipulation: text(undefined)");
    }

    /**
     * Test {301=[IE11], 302=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__text_String_() throws Exception {
        runTest("manipulation: text(String)");
    }

    /**
     * Test {302=[IE11], 303=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__text_Function_() throws Exception {
        runTest("manipulation: text(Function)");
    }

    /**
     * Test {303=[IE11], 304=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__text_Function__with_incoming_value() throws Exception {
        runTest("manipulation: text(Function) with incoming value");
    }

    /**
     * Test {304=[IE11], 305=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void manipulation__wrap_String_Element_() throws Exception {
        runTest("manipulation: wrap(String|Element)");
    }

    /**
     * Test {305=[IE11], 306=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 19, 19")
    public void manipulation__wrap_Function_() throws Exception {
        runTest("manipulation: wrap(Function)");
    }

    /**
     * Test {306=[IE11], 307=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__wrap_Function__with_index___10177_() throws Exception {
        runTest("manipulation: wrap(Function) with index (#10177)");
    }

    /**
     * Test {307=[IE11], 308=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void manipulation__wrap_String__consecutive_elements___10177_() throws Exception {
        runTest("manipulation: wrap(String) consecutive elements (#10177)");
    }

    /**
     * Test {308=[IE11], 309=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void manipulation__wrapAll_String_Element_() throws Exception {
        runTest("manipulation: wrapAll(String|Element)");
    }

    /**
     * Test {309=[IE11], 310=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void manipulation__wrapInner_String_Element_() throws Exception {
        runTest("manipulation: wrapInner(String|Element)");
    }

    /**
     * Test {310=[IE11], 311=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void manipulation__wrapInner_Function_() throws Exception {
        runTest("manipulation: wrapInner(Function)");
    }

    /**
     * Test {311=[IE11], 312=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__unwrap__() throws Exception {
        runTest("manipulation: unwrap()");
    }

    /**
     * Test {312=[IE11], 313=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 58, 58")
    public void manipulation__append_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: append(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {313=[IE11], 314=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 58, 58")
    public void manipulation__append_Function_() throws Exception {
        runTest("manipulation: append(Function)");
    }

    /**
     * Test {314=[IE11], 315=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void manipulation__append_Function__with_incoming_value() throws Exception {
        runTest("manipulation: append(Function) with incoming value");
    }

    /**
     * Test {315=[IE11], 316=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF24 = "0, 2, 2",
        FF31 = "0, 2, 2",
        IE11 = "0, 2, 2",
        IE8 = "0, 3, 3")
    public void manipulation__append_the_same_fragment_with_events__Bug__6997__5566_() throws Exception {
        runTest("manipulation: append the same fragment with events (Bug #6997, 5566)");
    }

    /**
     * Test {316=[IE11], 317=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__append_HTML5_sectioning_elements__Bug__6485_() throws Exception {
        runTest("manipulation: append HTML5 sectioning elements (Bug #6485)");
    }

    /**
     * Test {317=[IE11], 318=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__HTML5_Elements_inherit_styles_from_style_rules__Bug__10501_() throws Exception {
        runTest("manipulation: HTML5 Elements inherit styles from style rules (Bug #10501)");
    }

    /**
     * Test {318=[IE11], 319=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html5_clone___cannot_use_the_fragment_cache_in_IE___6485_() throws Exception {
        runTest("manipulation: html5 clone() cannot use the fragment cache in IE (#6485)");
    }

    /**
     * Test {319=[IE11], 320=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_String__with_HTML5__Bug__6485_() throws Exception {
        runTest("manipulation: html(String) with HTML5 (Bug #6485)");
    }

    /**
     * Test {320=[IE11], 321=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__IE8_serialization_bug() throws Exception {
        runTest("manipulation: IE8 serialization bug");
    }

    /**
     * Test {321=[IE11], 322=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___object_element__10324() throws Exception {
        runTest("manipulation: html() object element #10324");
    }

    /**
     * Test {322=[IE11], 323=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__append_xml_() throws Exception {
        runTest("manipulation: append(xml)");
    }

    /**
     * Test {323=[IE11], 324=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 17, 17")
    public void manipulation__appendTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: appendTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {324=[IE11], 325=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prepend_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prepend(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {325=[IE11], 326=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prepend_Function_() throws Exception {
        runTest("manipulation: prepend(Function)");
    }

    /**
     * Test {326=[IE11], 327=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__prepend_Function__with_incoming_value() throws Exception {
        runTest("manipulation: prepend(Function) with incoming value");
    }

    /**
     * Test {327=[IE11], 328=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void manipulation__prependTo_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: prependTo(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {328=[IE11], 329=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__before_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: before(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {329=[IE11], 330=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__before_Function_() throws Exception {
        runTest("manipulation: before(Function)");
    }

    /**
     * Test {330=[IE11], 331=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_and_after_w__empty_object___10812_() throws Exception {
        runTest("manipulation: before and after w/ empty object (#10812)");
    }

    /**
     * Test {331=[IE11], 332=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__before_and_after_on_disconnected_node___10517_() throws Exception {
        runTest("manipulation: before and after on disconnected node (#10517)");
    }

    /**
     * Test {332=[IE11], 333=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__insertBefore_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertBefore(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {333=[IE11], 334=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__after_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: after(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {334=[IE11], 335=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__after_Function_() throws Exception {
        runTest("manipulation: after(Function)");
    }

    /**
     * Test {335=[IE11], 336=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void manipulation__insertAfter_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: insertAfter(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {336=[IE11], 337=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void manipulation__replaceWith_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceWith(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {337=[IE11], 338=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 23, 23")
    public void manipulation__replaceWith_Function_() throws Exception {
        runTest("manipulation: replaceWith(Function)");
    }

    /**
     * Test {338=[IE11], 339=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__replaceWith_string__for_more_than_one_element() throws Exception {
        runTest("manipulation: replaceWith(string) for more than one element");
    }

    /**
     * Test {339=[IE11], 340=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__replaceAll_String_Element_Array_lt_Element_gt__jQuery_() throws Exception {
        runTest("manipulation: replaceAll(String|Element|Array&lt;Element&gt;|jQuery)");
    }

    /**
     * Test {340=[IE11], 341=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery_clone_____8017_() throws Exception {
        runTest("manipulation: jQuery.clone() (#8017)");
    }

    /**
     * Test {341=[IE11], 342=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone_____8070_() throws Exception {
        runTest("manipulation: clone() (#8070)");
    }

    /**
     * Test {342=[IE11], 343=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 44, 44")
    public void manipulation__clone__() throws Exception {
        runTest("manipulation: clone()");
    }

    /**
     * Test {343=[IE11], 344=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__clone_script_type_non_javascript____11359_() throws Exception {
        runTest("manipulation: clone(script type=non-javascript) (#11359)");
    }

    /**
     * Test {344=[IE11], 345=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 5, 5",
        FF24 = "0, 5, 5",
        FF31 = "0, 5, 5",
        IE11 = "1, 4, 5",
        IE8 = "0, 5, 5")
    public void manipulation__clone_form_element___Bug__3879___6655_() throws Exception {
        runTest("manipulation: clone(form element) (Bug #3879, #6655)");
    }

    /**
     * Test {345=[IE11], 346=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__clone_multiple_selected_options___Bug__8129_() throws Exception {
        runTest("manipulation: clone(multiple selected options) (Bug #8129)");
    }

    /**
     * Test {346=[IE11], 347=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_XML_nodes() throws Exception {
        runTest("manipulation: clone() on XML nodes");
    }

    /**
     * Test {347=[IE11], 348=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__clone___on_local_XML_nodes_with_html5_nodename() throws Exception {
        runTest("manipulation: clone() on local XML nodes with html5 nodename");
    }

    /**
     * Test {348=[IE11], 349=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html_undefined_() throws Exception {
        runTest("manipulation: html(undefined)");
    }

    /**
     * Test {349=[IE11], 350=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__html___on_empty_set() throws Exception {
        runTest("manipulation: html() on empty set");
    }

    /**
     * Test {350=[IE11], 351=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 35, 35")
    public void manipulation__html_String_() throws Exception {
        runTest("manipulation: html(String)");
    }

    /**
     * Test {351=[IE11], 352=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 37, 37")
    public void manipulation__html_Function_() throws Exception {
        runTest("manipulation: html(Function)");
    }

    /**
     * Test {352=[IE11], 353=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void manipulation__html_Function__with_incoming_value() throws Exception {
        runTest("manipulation: html(Function) with incoming value");
    }

    /**
     * Test {353=[IE11], 354=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__remove__() throws Exception {
        runTest("manipulation: remove()");
    }

    /**
     * Test {354=[IE11], 355=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void manipulation__detach__() throws Exception {
        runTest("manipulation: detach()");
    }

    /**
     * Test {355=[IE11], 356=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__empty__() throws Exception {
        runTest("manipulation: empty()");
    }

    /**
     * Test {356=[IE11], 357=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void manipulation__jQuery_cleanData() throws Exception {
        runTest("manipulation: jQuery.cleanData");
    }

    /**
     * Test {357=[IE11], 358=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___no_plain_text_caching__Bug__6779_() throws Exception {
        runTest("manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779)");
    }

    /**
     * Test {358=[IE11], 359=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void manipulation__jQuery_html___execute_scripts_escaped_with_html_comment_or_CDATA___9221_() throws Exception {
        runTest("manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221)");
    }

    /**
     * Test {359=[IE11], 360=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_buildFragment___plain_objects_are_not_a_document__8950() throws Exception {
        runTest("manipulation: jQuery.buildFragment - plain objects are not a document #8950");
    }

    /**
     * Test {360=[IE11], 361=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__jQuery_clone___no_exceptions_for_object_elements__9587() throws Exception {
        runTest("manipulation: jQuery.clone - no exceptions for object elements #9587");
    }

    /**
     * Test {361=[IE11], 362=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__jQuery__tag_____wrap_Inner_All____handle_unknown_elems___10667_() throws Exception {
        runTest("manipulation: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667)");
    }

    /**
     * Test {362=[IE11], 363=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void manipulation__Cloned__detached_HTML5_elems___10667_10670_() throws Exception {
        runTest("manipulation: Cloned, detached HTML5 elems (#10667,10670)");
    }

    /**
     * Test {363=[IE11], 364=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void manipulation__jQuery_fragments_cache_expectations() throws Exception {
        runTest("manipulation: jQuery.fragments cache expectations");
    }

    /**
     * Test {364=[IE11], 365=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__Guard_against_exceptions_when_clearing_safeChildNodes() throws Exception {
        runTest("manipulation: Guard against exceptions when clearing safeChildNodes");
    }

    /**
     * Test {365=[IE11], 366=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void manipulation__Ensure_oldIE_creates_a_new_set_on_appendTo___8894_() throws Exception {
        runTest("manipulation: Ensure oldIE creates a new set on appendTo (#8894)");
    }

    /**
     * Test {366=[IE11], 367=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__html_____script_exceptions_bubble___11743_() throws Exception {
        runTest("manipulation: html() - script exceptions bubble (#11743)");
    }

    /**
     * Test {367=[IE11], 368=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__checked_state_is_cloned_with_clone__() throws Exception {
        runTest("manipulation: checked state is cloned with clone()");
    }

    /**
     * Test {368=[IE11], 369=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void manipulation__manipulate_mixed_jQuery_and_text___12384___12346_() throws Exception {
        runTest("manipulation: manipulate mixed jQuery and text (#12384, #12346)");
    }

    /**
     * Test {369=[IE11], 370=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void manipulation__buildFragment_works_even_if_document_0__is_iframe_s_window_object_in_IE9_10___12266_() throws Exception {
        runTest("manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266)");
    }

    /**
     * Test {370=[IE11], 371=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 46, 46")
    @NotYetImplemented
    public void css__css_String_Hash_() throws Exception {
        runTest("css: css(String|Hash)");
    }

    /**
     * Test {371=[IE11], 372=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 29, 29")
    public void css__css___explicit_and_relative_values() throws Exception {
        runTest("css: css() explicit and relative values");
    }

    /**
     * Test {372=[IE11], 373=[CHROME, FF24, FF31, IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void css__css_String__Object_() throws Exception {
        runTest("css: css(String, Object)");
    }

    /**
     * Test {373=[IE11], 374=[CHROME, FF24, FF31], 376=[IE8]}.
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
     * Test {374=[IE11], 375=[CHROME, FF24, FF31], 377=[IE8]}.
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
     * Test {375=[IE11], 376=[CHROME, FF24, FF31], 378=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions() throws Exception {
        runTest("css: css(Object) where values are Functions");
    }

    /**
     * Test {376=[IE11], 377=[CHROME, FF24, FF31], 379=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__css_Object__where_values_are_Functions_with_incoming_values() throws Exception {
        runTest("css: css(Object) where values are Functions with incoming values");
    }

    /**
     * Test {377=[IE11], 378=[CHROME, FF24, FF31], 380=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void css__show____hide__() throws Exception {
        runTest("css: show(); hide()");
    }

    /**
     * Test {378=[IE11], 379=[CHROME, FF24, FF31], 381=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 7, 7",
        FF24 = "0, 7, 7",
        FF31 = "0, 7, 7",
        IE11 = "0, 7, 7",
        IE8 = "2, 1, 3")
    @NotYetImplemented({ FF, IE11, IE8 })
    public void css__show___resolves_correct_default_display__8099() throws Exception {
        runTest("css: show() resolves correct default display #8099");
    }

    /**
     * Test {379=[IE11], 380=[CHROME, FF24, FF31], 382=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void css__show___resolves_correct_default_display__detached_nodes___10006_() throws Exception {
        runTest("css: show() resolves correct default display, detached nodes (#10006)");
    }

    /**
     * Test {380=[IE11], 381=[CHROME, FF24, FF31], 383=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void css__toggle__() throws Exception {
        runTest("css: toggle()");
    }

    /**
     * Test {381=[IE11], 382=[CHROME, FF24, FF31], 384=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void css__hide_hidden_elements__bug__7141_() throws Exception {
        runTest("css: hide hidden elements (bug #7141)");
    }

    /**
     * Test {382=[IE11], 383=[CHROME, FF24, FF31], 385=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__jQuery_css_elem___height___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {383=[IE11], 384=[CHROME, FF24, FF31], 386=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css___visible_selector_works_properly_on_table_elements__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on table elements (bug #4512)");
    }

    /**
     * Test {384=[IE11], 385=[CHROME, FF24, FF31], 387=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void css___visible_selector_works_properly_on_children_with_a_hidden_parent__bug__4512_() throws Exception {
        runTest("css: :visible selector works properly on children with a hidden parent (bug #4512)");
    }

    /**
     * Test {385=[IE11], 386=[CHROME, FF24, FF31], 388=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__internal_ref_to_elem_runtimeStyle__bug__7608_() throws Exception {
        runTest("css: internal ref to elem.runtimeStyle (bug #7608)");
    }

    /**
     * Test {386=[IE11], 387=[CHROME, FF24, FF31], 389=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__marginRight_computed_style__bug__3333_() throws Exception {
        runTest("css: marginRight computed style (bug #3333)");
    }

    /**
     * Test {387=[IE11], 388=[CHROME, FF24, FF31], 390=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__box_model_properties_incorrectly_returning___instead_of_px__see__10639_and__12088() throws Exception {
        runTest("css: box model properties incorrectly returning % instead of px, see #10639 and #12088");
    }

    /**
     * Test {388=[IE11], 389=[CHROME, FF24, FF31], 391=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__jQuery_cssProps_behavior___bug__8402_() throws Exception {
        runTest("css: jQuery.cssProps behavior, (bug #8402)");
    }

    /**
     * Test {389=[IE11], 390=[CHROME, FF24, FF31], 392=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "2, 2, 4",
        FF24 = "0, 4, 4",
        FF31 = "0, 4, 4",
        IE11 = "0, 4, 4",
        IE8 = "0, 4, 4")
    public void css__widows___orphans__8936() throws Exception {
        runTest("css: widows & orphans #8936");
    }

    /**
     * Test {390=[IE11], 391=[CHROME, FF24, FF31], 393=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__can_t_get_css_for_disconnected_in_IE_9__see__10254_and__8388() throws Exception {
        runTest("css: can't get css for disconnected in IE<9, see #10254 and #8388");
    }

    /**
     * Test {391=[IE11], 392=[CHROME, FF24, FF31], 394=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void css__can_t_get_background_position_in_IE_9__see__10796() throws Exception {
        runTest("css: can't get background-position in IE<9, see #10796");
    }

    /**
     * Test {392=[IE11], 393=[CHROME, FF24, FF31], 395=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__percentage_properties_for_bottom_and_right_in_IE_9_should_not_be_incorrectly_transformed_to_pixels__see__11311() throws Exception {
        runTest("css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pixels, see #11311");
    }

    /**
     * Test {393=[IE11], 394=[CHROME, FF24, FF31], 396=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented
    public void css__percentage_properties_for_left_and_top_should_be_transformed_to_pixels__see__9505() throws Exception {
        runTest("css: percentage properties for left and top should be transformed to pixels, see #9505");
    }

    /**
     * Test {394=[IE11], 395=[CHROME, FF24, FF31], 397=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void css__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("css: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {395=[IE11], 396=[CHROME, FF24, FF31], 398=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void css__css__width___and_css__height___should_respect_box_sizing__see__11004() throws Exception {
        runTest("css: css('width') and css('height') should respect box-sizing, see #11004");
    }

    /**
     * Test {396=[IE11], 397=[CHROME, FF24, FF31], 399=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void css__certain_css_values_of__normal__should_be_convertable_to_a_number__see__8627() throws Exception {
        runTest("css: certain css values of 'normal' should be convertable to a number, see #8627");
    }

    /**
     * Test {397=[IE11], 398=[CHROME, FF24, FF31], 400=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void css__cssHooks___expand() throws Exception {
        runTest("css: cssHooks - expand");
    }

    /**
     * Test {398=[IE11], 399=[CHROME, FF24, FF31], 401=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 22, 22")
    public void serialize__jQuery_param__() throws Exception {
        runTest("serialize: jQuery.param()");
    }

    /**
     * Test {399=[IE11], 400=[CHROME, FF24, FF31], 402=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void serialize__jQuery_param___Constructed_prop_values() throws Exception {
        runTest("serialize: jQuery.param() Constructed prop values");
    }

    /**
     * Test {400=[IE11], 401=[CHROME, FF24, FF31], 403=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void serialize__serialize__() throws Exception {
        runTest("serialize: serialize()");
    }

    /**
     * Test {401=[IE11], 402=[CHROME, FF24, FF31], 404=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks");
    }

    /**
     * Test {402=[IE11], 403=[CHROME, FF24, FF31], 405=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks____url__options__syntax() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks - (url, options) syntax");
    }

    /**
     * Test {403=[IE11], 404=[CHROME, FF24, FF31], 406=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (late binding)");
    }

    /**
     * Test {404=[IE11], 405=[CHROME, FF24, FF31], 407=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__oncomplete_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (oncomplete binding)");
    }

    /**
     * Test {405=[IE11], 406=[CHROME, FF24, FF31], 408=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____success_callbacks__very_late_binding_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (very late binding)");
    }

    /**
     * Test {406=[IE11], 407=[CHROME, FF24, FF31], 409=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____success_callbacks__order_() throws Exception {
        runTest("ajax: jQuery.ajax() - success callbacks (order)");
    }

    /**
     * Test {407=[IE11], 408=[CHROME, FF24, FF31], 410=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    @NotYetImplemented
    public void ajax__jQuery_ajax_____error_callbacks() throws Exception {
        runTest("ajax: jQuery.ajax() - error callbacks");
    }

    /**
     * Test {408=[IE11], 409=[CHROME, FF24, FF31], 411=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax___multiple_method_signatures_introduced_in_1_5____8107_() throws Exception {
        runTest("ajax: jQuery.ajax - multiple method signatures introduced in 1.5 ( #8107)");
    }

    /**
     * Test {409=[IE11], 410=[CHROME, FF24, FF31], 412=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_____textStatus_and_errorThrown_values() throws Exception {
        runTest("ajax: jQuery.ajax() - textStatus and errorThrown values");
    }

    /**
     * Test {410=[IE11], 411=[CHROME, FF24, FF31], 413=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____responseText_on_error() throws Exception {
        runTest("ajax: jQuery.ajax() - responseText on error");
    }

    /**
     * Test {411=[IE11], 412=[CHROME, FF24, FF31], 414=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___ajax_____retry_with_jQuery_ajax__this__() throws Exception {
        runTest("ajax: .ajax() - retry with jQuery.ajax( this )");
    }

    /**
     * Test {412=[IE11], 413=[CHROME, FF24, FF31], 415=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax___ajax_____headers() throws Exception {
        runTest("ajax: .ajax() - headers");
    }

    /**
     * Test {413=[IE11], 414=[CHROME, FF24, FF31], 416=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___ajax_____Accept_header() throws Exception {
        runTest("ajax: .ajax() - Accept header");
    }

    /**
     * Test {414=[IE11], 415=[CHROME, FF24, FF31], 417=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax___ajax_____contentType() throws Exception {
        runTest("ajax: .ajax() - contentType");
    }

    /**
     * Test {415=[IE11], 416=[CHROME, FF24, FF31], 418=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax___ajax_____protocol_less_urls() throws Exception {
        runTest("ajax: .ajax() - protocol-less urls");
    }

    /**
     * Test {416=[IE11], 417=[CHROME, FF24, FF31], 419=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax___ajax_____hash() throws Exception {
        runTest("ajax: .ajax() - hash");
    }

    /**
     * Test {417=[IE11], 418=[CHROME, FF24, FF31], 420=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax___cross_domain_detection() throws Exception {
        runTest("ajax: jQuery ajax - cross-domain detection");
    }

    /**
     * Test {418=[IE11], 419=[CHROME, FF24, FF31], 421=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax___load_____404_error_callbacks() throws Exception {
        runTest("ajax: .load() - 404 error callbacks");
    }

    /**
     * Test {419=[IE11], 420=[CHROME, FF24, FF31], 422=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax_____abort() throws Exception {
        runTest("ajax: jQuery.ajax() - abort");
    }

    /**
     * Test {420=[IE11], 421=[CHROME, FF24, FF31], 423=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 14, 14")
    public void ajax__Ajax_events_with_context() throws Exception {
        runTest("ajax: Ajax events with context");
    }

    /**
     * Test {421=[IE11], 422=[CHROME, FF24, FF31], 424=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_context_modification() throws Exception {
        runTest("ajax: jQuery.ajax context modification");
    }

    /**
     * Test {422=[IE11], 423=[CHROME, FF24, FF31], 425=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_ajax_context_modification_through_ajaxSetup() throws Exception {
        runTest("ajax: jQuery.ajax context modification through ajaxSetup");
    }

    /**
     * Test {423=[IE11], 424=[CHROME, FF24, FF31], 426=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____disabled_globals() throws Exception {
        runTest("ajax: jQuery.ajax() - disabled globals");
    }

    /**
     * Test {424=[IE11], 425=[CHROME, FF24, FF31], 427=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements");
    }

    /**
     * Test {425=[IE11], 426=[CHROME, FF24, FF31], 428=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___xml__non_namespace_elements_inside_namespaced_elements__over_JSONP_() throws Exception {
        runTest("ajax: jQuery.ajax - xml: non-namespace elements inside namespaced elements (over JSONP)");
    }

    /**
     * Test {426=[IE11], 427=[CHROME, FF24, FF31], 429=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("2, 0, 2")
    public void ajax__jQuery_ajax___HEAD_requests() throws Exception {
        runTest("ajax: jQuery.ajax - HEAD requests");
    }

    /**
     * Test {427=[IE11], 428=[CHROME, FF24, FF31], 430=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___beforeSend() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend");
    }

    /**
     * Test {428=[IE11], 429=[CHROME, FF24, FF31], 431=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request___2688_() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request (#2688)");
    }

    /**
     * Test {429=[IE11], 430=[CHROME, FF24, FF31], 432=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___beforeSend__cancel_request_manually() throws Exception {
        runTest("ajax: jQuery.ajax - beforeSend, cancel request manually");
    }

    /**
     * Test {430=[IE11], 431=[CHROME, FF24, FF31], 433=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax___dataType_html() throws Exception {
        runTest("ajax: jQuery.ajax - dataType html");
    }

    /**
     * Test {431=[IE11], 432=[CHROME, FF24, FF31], 434=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__synchronous_request() throws Exception {
        runTest("ajax: synchronous request");
    }

    /**
     * Test {432=[IE11], 433=[CHROME, FF24, FF31], 435=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__synchronous_request_with_callbacks() throws Exception {
        runTest("ajax: synchronous request with callbacks");
    }

    /**
     * Test {433=[IE11], 434=[CHROME, FF24, FF31], 436=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__pass_through_request_object() throws Exception {
        runTest("ajax: pass-through request object");
    }

    /**
     * Test {434=[IE11], 435=[CHROME, FF24, FF31], 437=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void ajax__ajax_cache() throws Exception {
        runTest("ajax: ajax cache");
    }

    /**
     * Test {435=[IE11], 436=[CHROME, FF24, FF31], 438=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_() throws Exception {
        runTest("ajax: load(String)");
    }

    /**
     * Test {436=[IE11], 437=[CHROME, FF24, FF31], 439=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_null_() throws Exception {
        runTest("ajax: load(String,null)");
    }

    /**
     * Test {437=[IE11], 438=[CHROME, FF24, FF31], 440=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String_undefined_() throws Exception {
        runTest("ajax: load(String,undefined)");
    }

    /**
     * Test {438=[IE11], 439=[CHROME, FF24, FF31], 441=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load__url_selector__() throws Exception {
        runTest("ajax: load('url selector')");
    }

    /**
     * Test {439=[IE11], 440=[CHROME, FF24, FF31], 442=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load_String__Function__with_ajaxSetup_on_dataType_json__see__2046() throws Exception {
        runTest("ajax: load(String, Function) with ajaxSetup on dataType json, see #2046");
    }

    /**
     * Test {440=[IE11], 441=[CHROME, FF24, FF31], 443=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Function____simple__inject_text_into_DOM() throws Exception {
        runTest("ajax: load(String, Function) - simple: inject text into DOM");
    }

    /**
     * Test {441=[IE11], 442=[CHROME, FF24, FF31], 444=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void ajax__load_String__Function____check_scripts() throws Exception {
        runTest("ajax: load(String, Function) - check scripts");
    }

    /**
     * Test {442=[IE11], 443=[CHROME, FF24, FF31], 445=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__load_String__Function____check_file_with_only_a_script_tag() throws Exception {
        runTest("ajax: load(String, Function) - check file with only a script tag");
    }

    /**
     * Test {443=[IE11], 444=[CHROME, FF24, FF31], 446=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Function____dataFilter_in_ajaxSettings() throws Exception {
        runTest("ajax: load(String, Function) - dataFilter in ajaxSettings");
    }

    /**
     * Test {444=[IE11], 445=[CHROME, FF24, FF31], 447=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__Object__Function_() throws Exception {
        runTest("ajax: load(String, Object, Function)");
    }

    /**
     * Test {445=[IE11], 446=[CHROME, FF24, FF31], 448=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__load_String__String__Function_() throws Exception {
        runTest("ajax: load(String, String, Function)");
    }

    /**
     * Test {446=[IE11], 447=[CHROME, FF24, FF31], 449=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__load_____data_specified_in_ajaxSettings_is_merged_in___10524_() throws Exception {
        runTest("ajax: load() - data specified in ajaxSettings is merged in (#10524)");
    }

    /**
     * Test {447=[IE11], 448=[CHROME, FF24, FF31], 450=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__load_____callbacks_get_the_correct_parameters() throws Exception {
        runTest("ajax: load() - callbacks get the correct parameters");
    }

    /**
     * Test {448=[IE11], 449=[CHROME, FF24, FF31], 451=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_get_String__Function____data_in_ajaxSettings___8277_() throws Exception {
        runTest("ajax: jQuery.get(String, Function) - data in ajaxSettings (#8277)");
    }

    /**
     * Test {449=[IE11], 450=[CHROME, FF24, FF31], 452=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_get_String__Hash__Function____parse_xml_and_use_text___on_nodes() throws Exception {
        runTest("ajax: jQuery.get(String, Hash, Function) - parse xml and use text() on nodes");
    }

    /**
     * Test {450=[IE11], 451=[CHROME, FF24, FF31], 453=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_getScript_String__Function____with_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - with callback");
    }

    /**
     * Test {451=[IE11], 452=[CHROME, FF24, FF31], 454=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_getScript_String__Function____no_callback() throws Exception {
        runTest("ajax: jQuery.getScript(String, Function) - no callback");
    }

    /**
     * Test {452=[IE11], 453=[CHROME, FF24, FF31], 455=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void ajax__jQuery_ajax_____JSONP__Same_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Same Domain");
    }

    /**
     * Test {453=[IE11], 454=[CHROME, FF24, FF31], 456=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void ajax__jQuery_ajax_____JSONP__Cross_Domain() throws Exception {
        runTest("ajax: jQuery.ajax() - JSONP, Cross Domain");
    }

    /**
     * Test {454=[IE11], 455=[CHROME, FF24, FF31], 457=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote");
    }

    /**
     * Test {455=[IE11], 456=[CHROME, FF24, FF31], 458=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax_____script__Remote_with_POST() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with POST");
    }

    /**
     * Test {456=[IE11], 457=[CHROME, FF24, FF31], 459=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script__Remote_with_scheme_less_URL() throws Exception {
        runTest("ajax: jQuery.ajax() - script, Remote with scheme-less URL");
    }

    /**
     * Test {457=[IE11], 458=[CHROME, FF24, FF31], 460=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____malformed_JSON() throws Exception {
        runTest("ajax: jQuery.ajax() - malformed JSON");
    }

    /**
     * Test {458=[IE11], 459=[CHROME, FF24, FF31], 461=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax_____script__throws_exception___11743_() throws Exception {
        runTest("ajax: jQuery.ajax() - script, throws exception (#11743)");
    }

    /**
     * Test {459=[IE11], 460=[CHROME, FF24, FF31], 462=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax_____script_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - script by content-type");
    }

    /**
     * Test {460=[IE11], 461=[CHROME, FF24, FF31], 463=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_ajax_____json_by_content_type() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type");
    }

    /**
     * Test {461=[IE11], 462=[CHROME, FF24, FF31], 464=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void ajax__jQuery_ajax_____json_by_content_type_disabled_with_options() throws Exception {
        runTest("ajax: jQuery.ajax() - json by content-type disabled with options");
    }

    /**
     * Test {462=[IE11], 463=[CHROME, FF24, FF31], 465=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void ajax__jQuery_getJSON_String__Hash__Function____JSON_array() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Hash, Function) - JSON array");
    }

    /**
     * Test {463=[IE11], 464=[CHROME, FF24, FF31], 466=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object");
    }

    /**
     * Test {464=[IE11], 465=[CHROME, FF24, FF31], 467=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON___Using_Native_JSON() throws Exception {
        runTest("ajax: jQuery.getJSON - Using Native JSON");
    }

    /**
     * Test {465=[IE11], 466=[CHROME, FF24, FF31], 468=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_getJSON_String__Function____JSON_object_with_absolute_url_to_local_content() throws Exception {
        runTest("ajax: jQuery.getJSON(String, Function) - JSON object with absolute url to local content");
    }

    /**
     * Test {466=[IE11], 467=[CHROME, FF24, FF31], 469=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_post___data() throws Exception {
        runTest("ajax: jQuery.post - data");
    }

    /**
     * Test {467=[IE11], 468=[CHROME, FF24, FF31], 470=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void ajax__jQuery_post_String__Hash__Function____simple_with_xml() throws Exception {
        runTest("ajax: jQuery.post(String, Hash, Function) - simple with xml");
    }

    /**
     * Test {468=[IE11], 469=[CHROME, FF24, FF31], 471=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void ajax__jQuery_ajaxSetup__timeout__Number_____with_global_timeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) - with global timeout");
    }

    /**
     * Test {469=[IE11], 470=[CHROME, FF24, FF31], 472=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajaxSetup__timeout__Number___with_localtimeout() throws Exception {
        runTest("ajax: jQuery.ajaxSetup({timeout: Number}) with localtimeout");
    }

    /**
     * Test {470=[IE11], 471=[CHROME, FF24, FF31], 473=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___simple_get() throws Exception {
        runTest("ajax: jQuery.ajax - simple get");
    }

    /**
     * Test {471=[IE11], 472=[CHROME, FF24, FF31], 474=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___simple_post() throws Exception {
        runTest("ajax: jQuery.ajax - simple post");
    }

    /**
     * Test {472=[IE11], 473=[CHROME, FF24, FF31], 475=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__ajaxSetup__() throws Exception {
        runTest("ajax: ajaxSetup()");
    }

    /**
     * Test {473=[IE11], 474=[CHROME, FF24, FF31], 476=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__data_option__evaluate_function_values___2806_() throws Exception {
        runTest("ajax: data option: evaluate function values (#2806)");
    }

    /**
     * Test {474=[IE11], 475=[CHROME, FF24, FF31], 477=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__data_option__empty_bodies_for_non_GET_requests() throws Exception {
        runTest("ajax: data option: empty bodies for non-GET requests");
    }

    /**
     * Test {475=[IE11], 476=[CHROME, FF24, FF31], 478=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (cache)");
    }

    /**
     * Test {476=[IE11], 477=[CHROME, FF24, FF31], 479=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___Etag_support__cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (cache)");
    }

    /**
     * Test {477=[IE11], 478=[CHROME, FF24, FF31], 480=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___If_Modified_Since_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - If-Modified-Since support (no cache)");
    }

    /**
     * Test {478=[IE11], 479=[CHROME, FF24, FF31], 481=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___Etag_support__no_cache_() throws Exception {
        runTest("ajax: jQuery.ajax - Etag support (no cache)");
    }

    /**
     * Test {479=[IE11], 480=[CHROME, FF24, FF31], 482=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___failing_cross_domain() throws Exception {
        runTest("ajax: jQuery ajax - failing cross-domain");
    }

    /**
     * Test {480=[IE11], 481=[CHROME, FF24, FF31], 483=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___atom_xml() throws Exception {
        runTest("ajax: jQuery ajax - atom+xml");
    }

    /**
     * Test {481=[IE11], 482=[CHROME, FF24, FF31], 484=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___Location_object_as_url___7531_() throws Exception {
        runTest("ajax: jQuery.ajax - Location object as url (#7531)");
    }

    /**
     * Test {482=[IE11], 483=[CHROME, FF24, FF31], 485=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___Context_with_circular_references___9887_() throws Exception {
        runTest("ajax: jQuery.ajax - Context with circular references (#9887)");
    }

    /**
     * Test {483=[IE11], 484=[CHROME, FF24, FF31], 486=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void ajax__jQuery_ajax___statusText() throws Exception {
        runTest("ajax: jQuery.ajax - statusText");
    }

    /**
     * Test {484=[IE11], 485=[CHROME, FF24, FF31], 487=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void ajax__jQuery_ajax___statusCode() throws Exception {
        runTest("ajax: jQuery.ajax - statusCode");
    }

    /**
     * Test {485=[IE11], 486=[CHROME, FF24, FF31], 488=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void ajax__jQuery_ajax___transitive_conversions() throws Exception {
        runTest("ajax: jQuery.ajax - transitive conversions");
    }

    /**
     * Test {486=[IE11], 487=[CHROME, FF24, FF31], 489=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_ajax___overrideMimeType() throws Exception {
        runTest("ajax: jQuery.ajax - overrideMimeType");
    }

    /**
     * Test {487=[IE11], 488=[CHROME, FF24, FF31], 490=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___abort_in_prefilter() throws Exception {
        runTest("ajax: jQuery.ajax - abort in prefilter");
    }

    /**
     * Test {488=[IE11], 489=[CHROME, FF24, FF31], 491=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___loading_binary_data_shouldn_t_throw_an_exception_in_IE___11426_() throws Exception {
        runTest("ajax: jQuery.ajax - loading binary data shouldn't throw an exception in IE (#11426)");
    }

    /**
     * Test {489=[IE11], 490=[CHROME, FF24, FF31], 492=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_domManip___no_side_effect_because_of_ajaxSetup_or_global_events___11264_() throws Exception {
        runTest("ajax: jQuery.domManip - no side effect because of ajaxSetup or global events (#11264)");
    }

    /**
     * Test {490=[IE11], 491=[CHROME, FF24, FF31], 493=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void ajax__jQuery_domManip___script_in_comments_are_properly_evaluated___11402_() throws Exception {
        runTest("ajax: jQuery.domManip - script in comments are properly evaluated (#11402)");
    }

    /**
     * Test {491=[IE11], 492=[CHROME, FF24, FF31], 494=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void ajax__jQuery_ajax___active_counter() throws Exception {
        runTest("ajax: jQuery.ajax - active counter");
    }

    /**
     * Test {492=[IE11], 493=[CHROME, FF24, FF31], 495=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__sanity_check() throws Exception {
        runTest("effects: sanity check");
    }

    /**
     * Test {493=[IE11], 494=[CHROME, FF24, FF31], 496=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 26, 26")
    public void effects__show__() throws Exception {
        runTest("effects: show()");
    }

    /**
     * Test {494=[IE11], 495=[CHROME, FF24, FF31], 497=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__show_Number____other_displays() throws Exception {
        runTest("effects: show(Number) - other displays");
    }

    /**
     * Test {495=[IE11], 496=[CHROME, FF24, FF31], 498=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__Persist_correct_display_value() throws Exception {
        runTest("effects: Persist correct display value");
    }

    /**
     * Test {496=[IE11], 497=[CHROME, FF24, FF31], 499=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_Hash__Object__Function_() throws Exception {
        runTest("effects: animate(Hash, Object, Function)");
    }

    /**
     * Test {497=[IE11], 498=[CHROME, FF24, FF31], 500=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented(IE8)
    public void effects__animate_negative_height() throws Exception {
        runTest("effects: animate negative height");
    }

    /**
     * Test {498=[IE11], 499=[CHROME, FF24, FF31], 501=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin() throws Exception {
        runTest("effects: animate negative margin");
    }

    /**
     * Test {499=[IE11], 500=[CHROME, FF24, FF31], 502=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_negative_margin_with_px() throws Exception {
        runTest("effects: animate negative margin with px");
    }

    /**
     * Test {500=[IE11], 501=[CHROME, FF24, FF31], 503=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void effects__animate_negative_padding() throws Exception {
        runTest("effects: animate negative padding");
    }

    /**
     * Test {501=[IE11], 502=[CHROME, FF24, FF31], 504=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE8)
    public void effects__animate_block_as_inline_width_height() throws Exception {
        runTest("effects: animate block as inline width/height");
    }

    /**
     * Test {502=[IE11], 503=[CHROME, FF24, FF31], 505=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE8)
    public void effects__animate_native_inline_width_height() throws Exception {
        runTest("effects: animate native inline width/height");
    }

    /**
     * Test {503=[IE11], 504=[CHROME, FF24, FF31], 506=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented(IE8)
    public void effects__animate_block_width_height() throws Exception {
        runTest("effects: animate block width/height");
    }

    /**
     * Test {504=[IE11], 505=[CHROME, FF24, FF31], 507=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_table_width_height() throws Exception {
        runTest("effects: animate table width/height");
    }

    /**
     * Test {505=[IE11], 506=[CHROME, FF24, FF31], 508=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void effects__animate_table_row_width_height() throws Exception {
        runTest("effects: animate table-row width/height");
    }

    /**
     * Test {506=[IE11], 507=[CHROME, FF24, FF31], 509=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    @NotYetImplemented
    public void effects__animate_table_cell_width_height() throws Exception {
        runTest("effects: animate table-cell width/height");
    }

    /**
     * Test {507=[IE11], 508=[CHROME, FF24, FF31], 510=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_percentage____on_width_height() throws Exception {
        runTest("effects: animate percentage(%) on width/height");
    }

    /**
     * Test {508=[IE11], 509=[CHROME, FF24, FF31], 511=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_resets_overflow_x_and_overflow_y_when_finished() throws Exception {
        runTest("effects: animate resets overflow-x and overflow-y when finished");
    }

    /**
     * Test {509=[IE11], 510=[CHROME, FF24, FF31], 512=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__false__() throws Exception {
        runTest("effects: animate option { queue: false }");
    }

    /**
     * Test {510=[IE11], 511=[CHROME, FF24, FF31], 513=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_option___queue__true__() throws Exception {
        runTest("effects: animate option { queue: true }");
    }

    /**
     * Test {511=[IE11], 512=[CHROME, FF24, FF31], 514=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_option___queue___name___() throws Exception {
        runTest("effects: animate option { queue: 'name' }");
    }

    /**
     * Test {512=[IE11], 513=[CHROME, FF24, FF31], 515=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_with_no_properties() throws Exception {
        runTest("effects: animate with no properties");
    }

    /**
     * Test {513=[IE11], 514=[CHROME, FF24, FF31], 516=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_duration_0() throws Exception {
        runTest("effects: animate duration 0");
    }

    /**
     * Test {514=[IE11], 515=[CHROME, FF24, FF31], 517=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_hyphenated_properties() throws Exception {
        runTest("effects: animate hyphenated properties");
    }

    /**
     * Test {515=[IE11], 516=[CHROME, FF24, FF31], 518=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__animate_non_element() throws Exception {
        runTest("effects: animate non-element");
    }

    /**
     * Test {516=[IE11], 517=[CHROME, FF24, FF31], 519=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop__() throws Exception {
        runTest("effects: stop()");
    }

    /**
     * Test {517=[IE11], 518=[CHROME, FF24, FF31], 520=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop_____several_in_queue() throws Exception {
        runTest("effects: stop() - several in queue");
    }

    /**
     * Test {518=[IE11], 519=[CHROME, FF24, FF31], 521=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__stop_clearQueue_() throws Exception {
        runTest("effects: stop(clearQueue)");
    }

    /**
     * Test {519=[IE11], 520=[CHROME, FF24, FF31], 522=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__stop_clearQueue__gotoEnd_() throws Exception {
        runTest("effects: stop(clearQueue, gotoEnd)");
    }

    /**
     * Test {520=[IE11], 521=[CHROME, FF24, FF31], 523=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__stop__queue_______________Stop_single_queues() throws Exception {
        runTest("effects: stop( queue, ..., ... ) - Stop single queues");
    }

    /**
     * Test {521=[IE11], 522=[CHROME, FF24, FF31], 524=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__toggle__() throws Exception {
        runTest("effects: toggle()");
    }

    /**
     * Test {522=[IE11], 523=[CHROME, FF24, FF31], 525=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 7, 7")
    public void effects__jQuery_fx_prototype_cur______1_8_Back_Compat() throws Exception {
        runTest("effects: jQuery.fx.prototype.cur() - <1.8 Back Compat");
    }

    /**
     * Test {523=[IE11], 524=[CHROME, FF24, FF31], 526=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__JS_Overflow_and_Display() throws Exception {
        runTest("effects: JS Overflow and Display");
    }

    /**
     * Test {524=[IE11], 525=[CHROME, FF24, FF31], 527=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__CSS_Overflow_and_Display() throws Exception {
        runTest("effects: CSS Overflow and Display");
    }

    /**
     * Test {525=[IE11], 526=[CHROME, FF24, FF31], 532=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_0() throws Exception {
        runTest("effects: CSS Auto to 0");
    }

    /**
     * Test {526=[IE11], 527=[CHROME, FF24, FF31], 531=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_50() throws Exception {
        runTest("effects: CSS Auto to 50");
    }

    /**
     * Test {527=[IE11], 528=[CHROME, FF24, FF31], 530=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_Auto_to_100() throws Exception {
        runTest("effects: CSS Auto to 100");
    }

    /**
     * Test {528=[IE11, IE8], 529=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    @NotYetImplemented
    public void effects__CSS_Auto_to_show() throws Exception {
        runTest("effects: CSS Auto to show");
    }

    /**
     * Test {529=[IE11, IE8], 530=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_Auto_to_hide() throws Exception {
        runTest("effects: CSS Auto to hide");
    }

    /**
     * Test {530=[IE11], 531=[CHROME, FF24, FF31], 537=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_0() throws Exception {
        runTest("effects: JS Auto to 0");
    }

    /**
     * Test {531=[IE11], 532=[CHROME, FF24, FF31], 536=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_50() throws Exception {
        runTest("effects: JS Auto to 50");
    }

    /**
     * Test {532=[IE11], 533=[CHROME, FF24, FF31], 535=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_Auto_to_100() throws Exception {
        runTest("effects: JS Auto to 100");
    }

    /**
     * Test {533=[IE11, IE8], 534=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    @NotYetImplemented
    public void effects__JS_Auto_to_show() throws Exception {
        runTest("effects: JS Auto to show");
    }

    /**
     * Test {534=[IE11, IE8], 535=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_Auto_to_hide() throws Exception {
        runTest("effects: JS Auto to hide");
    }

    /**
     * Test {535=[IE11], 536=[CHROME, FF24, FF31], 542=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_0() throws Exception {
        runTest("effects: CSS 100 to 0");
    }

    /**
     * Test {536=[IE11], 537=[CHROME, FF24, FF31], 541=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_50() throws Exception {
        runTest("effects: CSS 100 to 50");
    }

    /**
     * Test {537=[IE11], 538=[CHROME, FF24, FF31], 540=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_100_to_100() throws Exception {
        runTest("effects: CSS 100 to 100");
    }

    /**
     * Test {538=[IE11, IE8], 539=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_100_to_show() throws Exception {
        runTest("effects: CSS 100 to show");
    }

    /**
     * Test {539=[IE11, IE8], 540=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_100_to_hide() throws Exception {
        runTest("effects: CSS 100 to hide");
    }

    /**
     * Test {540=[IE11], 541=[CHROME, FF24, FF31], 547=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_0() throws Exception {
        runTest("effects: JS 100 to 0");
    }

    /**
     * Test {541=[IE11], 542=[CHROME, FF24, FF31], 546=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_50() throws Exception {
        runTest("effects: JS 100 to 50");
    }

    /**
     * Test {542=[IE11], 543=[CHROME, FF24, FF31], 545=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_100_to_100() throws Exception {
        runTest("effects: JS 100 to 100");
    }

    /**
     * Test {543=[IE11, IE8], 544=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_100_to_show() throws Exception {
        runTest("effects: JS 100 to show");
    }

    /**
     * Test {544=[IE11, IE8], 545=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_100_to_hide() throws Exception {
        runTest("effects: JS 100 to hide");
    }

    /**
     * Test {545=[IE11], 546=[CHROME, FF24, FF31], 552=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_0() throws Exception {
        runTest("effects: CSS 50 to 0");
    }

    /**
     * Test {546=[IE11], 547=[CHROME, FF24, FF31], 551=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_50() throws Exception {
        runTest("effects: CSS 50 to 50");
    }

    /**
     * Test {547=[IE11], 548=[CHROME, FF24, FF31], 550=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_50_to_100() throws Exception {
        runTest("effects: CSS 50 to 100");
    }

    /**
     * Test {548=[IE11, IE8], 549=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_50_to_show() throws Exception {
        runTest("effects: CSS 50 to show");
    }

    /**
     * Test {549=[IE11, IE8], 550=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_50_to_hide() throws Exception {
        runTest("effects: CSS 50 to hide");
    }

    /**
     * Test {550=[IE11], 551=[CHROME, FF24, FF31], 557=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_0() throws Exception {
        runTest("effects: JS 50 to 0");
    }

    /**
     * Test {551=[IE11], 552=[CHROME, FF24, FF31], 556=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_50() throws Exception {
        runTest("effects: JS 50 to 50");
    }

    /**
     * Test {552=[IE11], 553=[CHROME, FF24, FF31], 555=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_50_to_100() throws Exception {
        runTest("effects: JS 50 to 100");
    }

    /**
     * Test {553=[IE11, IE8], 554=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_50_to_show() throws Exception {
        runTest("effects: JS 50 to show");
    }

    /**
     * Test {554=[IE11, IE8], 555=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_50_to_hide() throws Exception {
        runTest("effects: JS 50 to hide");
    }

    /**
     * Test {555=[IE11], 556=[CHROME, FF24, FF31], 562=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_0() throws Exception {
        runTest("effects: CSS 0 to 0");
    }

    /**
     * Test {556=[IE11], 557=[CHROME, FF24, FF31], 561=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_50() throws Exception {
        runTest("effects: CSS 0 to 50");
    }

    /**
     * Test {557=[IE11], 558=[CHROME, FF24, FF31], 560=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__CSS_0_to_100() throws Exception {
        runTest("effects: CSS 0 to 100");
    }

    /**
     * Test {558=[IE11, IE8], 559=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__CSS_0_to_show() throws Exception {
        runTest("effects: CSS 0 to show");
    }

    /**
     * Test {559=[IE11, IE8], 560=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__CSS_0_to_hide() throws Exception {
        runTest("effects: CSS 0 to hide");
    }

    /**
     * Test {560=[IE11], 561=[CHROME, FF24, FF31], 567=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_0() throws Exception {
        runTest("effects: JS 0 to 0");
    }

    /**
     * Test {561=[IE11], 562=[CHROME, FF24, FF31], 566=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_50() throws Exception {
        runTest("effects: JS 0 to 50");
    }

    /**
     * Test {562=[IE11], 563=[CHROME, FF24, FF31], 565=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__JS_0_to_100() throws Exception {
        runTest("effects: JS 0 to 100");
    }

    /**
     * Test {563=[IE11, IE8], 564=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__JS_0_to_show() throws Exception {
        runTest("effects: JS 0 to show");
    }

    /**
     * Test {564=[IE11, IE8], 565=[CHROME, FF24, FF31]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__JS_0_to_hide() throws Exception {
        runTest("effects: JS 0 to hide");
    }

    /**
     * Test {565=[IE11], 566=[CHROME, FF24, FF31], 568=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeOut_fadeIn() throws Exception {
        runTest("effects: Chain fadeOut fadeIn");
    }

    /**
     * Test {566=[IE11], 567=[CHROME, FF24, FF31], 569=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeIn_fadeOut() throws Exception {
        runTest("effects: Chain fadeIn fadeOut");
    }

    /**
     * Test {567=[IE11], 568=[CHROME, FF24, FF31], 570=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_hide_show() throws Exception {
        runTest("effects: Chain hide show");
    }

    /**
     * Test {568=[IE11], 569=[CHROME, FF24, FF31], 571=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_show_hide() throws Exception {
        runTest("effects: Chain show hide");
    }

    /**
     * Test {569=[IE11], 570=[CHROME, FF24, FF31], 572=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_show_hide_with_easing_and_callback() throws Exception {
        runTest("effects: Chain show hide with easing and callback");
    }

    /**
     * Test {570=[IE11], 571=[CHROME, FF24, FF31], 573=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_in() throws Exception {
        runTest("effects: Chain toggle in");
    }

    /**
     * Test {571=[IE11], 572=[CHROME, FF24, FF31], 574=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_out() throws Exception {
        runTest("effects: Chain toggle out");
    }

    /**
     * Test {572=[IE11], 573=[CHROME, FF24, FF31], 575=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_toggle_out_with_easing_and_callback() throws Exception {
        runTest("effects: Chain toggle out with easing and callback");
    }

    /**
     * Test {573=[IE11], 574=[CHROME, FF24, FF31], 576=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideDown_slideUp() throws Exception {
        runTest("effects: Chain slideDown slideUp");
    }

    /**
     * Test {574=[IE11], 575=[CHROME, FF24, FF31], 577=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideUp_slideDown() throws Exception {
        runTest("effects: Chain slideUp slideDown");
    }

    /**
     * Test {575=[IE11], 576=[CHROME, FF24, FF31], 578=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideUp_slideDown_with_easing_and_callback() throws Exception {
        runTest("effects: Chain slideUp slideDown with easing and callback");
    }

    /**
     * Test {576=[IE11], 577=[CHROME, FF24, FF31], 579=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideToggle_in() throws Exception {
        runTest("effects: Chain slideToggle in");
    }

    /**
     * Test {577=[IE11], 578=[CHROME, FF24, FF31], 580=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_slideToggle_out() throws Exception {
        runTest("effects: Chain slideToggle out");
    }

    /**
     * Test {578=[IE11], 579=[CHROME, FF24, FF31], 581=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeToggle_in() throws Exception {
        runTest("effects: Chain fadeToggle in");
    }

    /**
     * Test {579=[IE11], 580=[CHROME, FF24, FF31], 582=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeToggle_out() throws Exception {
        runTest("effects: Chain fadeToggle out");
    }

    /**
     * Test {580=[IE11], 581=[CHROME, FF24, FF31], 583=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__Chain_fadeTo_0_5_1_0_with_easing_and_callback_() throws Exception {
        runTest("effects: Chain fadeTo 0.5 1.0 with easing and callback)");
    }

    /**
     * Test {581=[IE11], 582=[CHROME, FF24, FF31], 584=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_show__fast___doesn_t_clear_radio_buttons__bug__1095_() throws Exception {
        runTest("effects: jQuery.show('fast') doesn't clear radio buttons (bug #1095)");
    }

    /**
     * Test {582=[IE11], 583=[CHROME, FF24, FF31], 585=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    @NotYetImplemented(IE8)
    public void effects__slideToggle___stop___slideToggle__() throws Exception {
        runTest("effects: slideToggle().stop().slideToggle()");
    }

    /**
     * Test {583=[IE11], 584=[CHROME, FF24, FF31], 586=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__fadeToggle___stop___fadeToggle__() throws Exception {
        runTest("effects: fadeToggle().stop().fadeToggle()");
    }

    /**
     * Test {584=[IE11], 585=[CHROME, FF24, FF31], 587=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void effects__toggle___stop___toggle__() throws Exception {
        runTest("effects: toggle().stop().toggle()");
    }

    /**
     * Test {585=[IE11], 586=[CHROME, FF24, FF31], 588=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__animate_with_per_property_easing() throws Exception {
        runTest("effects: animate with per-property easing");
    }

    /**
     * Test {586=[IE11], 587=[CHROME, FF24, FF31], 589=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void effects__animate_with_CSS_shorthand_properties() throws Exception {
        runTest("effects: animate with CSS shorthand properties");
    }

    /**
     * Test {587=[IE11], 588=[CHROME, FF24, FF31], 590=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_hidden_elements__with_animation__bug__7141_() throws Exception {
        runTest("effects: hide hidden elements, with animation (bug #7141)");
    }

    /**
     * Test {588=[IE11], 589=[CHROME, FF24, FF31], 591=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_unit_less_properties___4966_() throws Exception {
        runTest("effects: animate unit-less properties (#4966)");
    }

    /**
     * Test {589=[IE11], 590=[CHROME, FF24, FF31], 592=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void effects__animate_properties_missing_px_w__opacity_as_last___9074_() throws Exception {
        runTest("effects: animate properties missing px w/ opacity as last (#9074)");
    }

    /**
     * Test {590=[IE11], 591=[CHROME, FF24, FF31], 593=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__callbacks_should_fire_in_correct_order___9100_() throws Exception {
        runTest("effects: callbacks should fire in correct order (#9100)");
    }

    /**
     * Test {591=[IE11], 592=[CHROME, FF24, FF31], 594=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__callbacks_that_throw_exceptions_will_be_removed___5684_() throws Exception {
        runTest("effects: callbacks that throw exceptions will be removed (#5684)");
    }

    /**
     * Test {592=[IE11], 593=[CHROME, FF24, FF31], 595=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__animate_will_scale_margin_properties_individually() throws Exception {
        runTest("effects: animate will scale margin properties individually");
    }

    /**
     * Test {593=[IE11], 594=[CHROME, FF24, FF31], 596=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Do_not_append_px_to__fill_opacity___9548() throws Exception {
        runTest("effects: Do not append px to 'fill-opacity' #9548");
    }

    /**
     * Test {594=[IE11], 595=[CHROME, FF24, FF31], 597=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__jQuery_Animation__object__props__opts__() throws Exception {
        runTest("effects: jQuery.Animation( object, props, opts )");
    }

    /**
     * Test {595=[IE11], 596=[CHROME, FF24, FF31], 598=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animate_Option__step__function__percent__tween__() throws Exception {
        runTest("effects: Animate Option: step: function( percent, tween )");
    }

    /**
     * Test {596=[IE11], 597=[CHROME, FF24, FF31], 599=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__Animate_callbacks_have_correct_context() throws Exception {
        runTest("effects: Animate callbacks have correct context");
    }

    /**
     * Test {597=[IE11], 598=[CHROME, FF24, FF31], 600=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void effects__User_supplied_callback_called_after_show_when_fx_off___8892_() throws Exception {
        runTest("effects: User supplied callback called after show when fx off (#8892)");
    }

    /**
     * Test {598=[IE11], 599=[CHROME, FF24, FF31], 601=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 18, 18")
    public void effects__animate_should_set_display_for_disconnected_nodes() throws Exception {
        runTest("effects: animate should set display for disconnected nodes");
    }

    /**
     * Test {599=[IE11], 600=[CHROME, FF24, FF31], 602=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animation_callback_should_not_show_animated_element_as_animated___7157_() throws Exception {
        runTest("effects: Animation callback should not show animated element as animated (#7157)");
    }

    /**
     * Test {600=[IE11], 601=[CHROME, FF24, FF31], 603=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void effects__hide_called_on_element_within_hidden_parent_should_set_display_to_none___10045_() throws Exception {
        runTest("effects: hide called on element within hidden parent should set display to none (#10045)");
    }

    /**
     * Test {601=[IE11], 602=[CHROME, FF24, FF31], 604=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 5, 5")
    public void effects__hide__fadeOut_and_slideUp_called_on_element_width_height_and_width___0_should_set_display_to_none() throws Exception {
        runTest("effects: hide, fadeOut and slideUp called on element width height and width = 0 should set display to none");
    }

    /**
     * Test {602=[IE11], 603=[CHROME, FF24, FF31], 605=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void effects__Handle_queue_false_promises() throws Exception {
        runTest("effects: Handle queue:false promises");
    }

    /**
     * Test {603=[IE11], 604=[CHROME, FF24, FF31], 606=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__multiple_unqueued_and_promise() throws Exception {
        runTest("effects: multiple unqueued and promise");
    }

    /**
     * Test {604=[IE11], 605=[CHROME, FF24, FF31], 607=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void effects__animate_does_not_change_start_value_for_non_px_animation___7109_() throws Exception {
        runTest("effects: animate does not change start value for non-px animation (#7109)");
    }

    /**
     * Test {605=[IE11], 606=[CHROME, FF24, FF31], 608=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = "0, 2, 2",
        FF24 = "0, 1, 1",
        FF31 = "0, 1, 1",
        IE11 = "0, 1, 1",
        IE8 = "0, 2, 2")
    public void effects__non_px_animation_handles_non_numeric_start___11971_() throws Exception {
        runTest("effects: non-px animation handles non-numeric start (#11971)");
    }

    /**
     * Test {606=[IE11], 607=[CHROME, FF24, FF31], 609=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void effects__Animation_callbacks___11797_() throws Exception {
        runTest("effects: Animation callbacks (#11797)");
    }

    /**
     * Test {607=[IE11], 608=[CHROME, FF24, FF31], 610=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void effects__Animate_properly_sets_overflow_hidden_when_animating_width_height___12117_() throws Exception {
        runTest("effects: Animate properly sets overflow hidden when animating width/height (#12117)");
    }

    /**
     * Test {608=[IE11], 609=[CHROME, FF24, FF31], 611=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void effects__Animations_with_0_duration_don_t_ease___12273_() throws Exception {
        runTest("effects: Animations with 0 duration don't ease (#12273)");
    }

    /**
     * Test {609=[IE11], 610=[CHROME, FF24, FF31], 612=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__empty_set() throws Exception {
        runTest("offset: empty set");
    }

    /**
     * Test {610=[IE11], 611=[CHROME, FF24, FF31], 613=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__object_without_getBoundingClientRect() throws Exception {
        runTest("offset: object without getBoundingClientRect");
    }

    /**
     * Test {611=[IE11], 612=[CHROME, FF24, FF31], 614=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__disconnected_node() throws Exception {
        runTest("offset: disconnected node");
    }

    /**
     * Test {613=[IE11], 614=[CHROME, FF24, FF31], 616=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 178, 178")
    @NotYetImplemented
    public void offset__absolute() throws Exception {
        runTest("offset: absolute");
    }

    /**
     * Test {614=[IE11], 615=[CHROME, FF24, FF31], 617=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 60, 60")
    @NotYetImplemented
    public void offset__relative() throws Exception {
        runTest("offset: relative");
    }

    /**
     * Test {615=[IE11], 616=[CHROME, FF24, FF31], 618=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 80, 80")
    @NotYetImplemented(IE)
    public void offset__static() throws Exception {
        runTest("offset: static");
    }

    /**
     * Test {616=[IE11], 617=[CHROME, FF24, FF31], 619=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 30, 30")
    public void offset__fixed() throws Exception {
        runTest("offset: fixed");
    }

    /**
     * Test {617=[IE11], 618=[CHROME, FF24, FF31], 620=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    @NotYetImplemented
    public void offset__table() throws Exception {
        runTest("offset: table");
    }

    /**
     * Test {618=[IE11], 619=[CHROME, FF24, FF31], 621=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    @NotYetImplemented
    public void offset__scroll() throws Exception {
        runTest("offset: scroll");
    }

    /**
     * Test {619=[IE11], 620=[CHROME, FF24, FF31], 622=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    @NotYetImplemented(IE)
    public void offset__body() throws Exception {
        runTest("offset: body");
    }

    /**
     * Test {620=[IE11], 621=[CHROME, FF24, FF31], 623=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 3, 3")
    public void offset__chaining() throws Exception {
        runTest("offset: chaining");
    }

    /**
     * Test {621=[IE11], 622=[CHROME, FF24, FF31], 624=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 12, 12")
    public void offset__offsetParent() throws Exception {
        runTest("offset: offsetParent");
    }

    /**
     * Test {622=[IE11], 623=[CHROME, FF24, FF31], 625=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void offset__fractions__see__7730_and__7885_() throws Exception {
        runTest("offset: fractions (see #7730 and #7885)");
    }

    /**
     * Test {623=[IE11], 624=[CHROME, FF24, FF31], 626=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width__() throws Exception {
        runTest("dimensions: width()");
    }

    /**
     * Test {624=[IE11], 625=[CHROME, FF24, FF31], 627=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__width_Function_() throws Exception {
        runTest("dimensions: width(Function)");
    }

    /**
     * Test {625=[IE11], 626=[CHROME, FF24, FF31], 628=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__width_Function_args__() throws Exception {
        runTest("dimensions: width(Function(args))");
    }

    /**
     * Test {626=[IE11], 627=[CHROME, FF24, FF31], 629=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height__() throws Exception {
        runTest("dimensions: height()");
    }

    /**
     * Test {627=[IE11], 628=[CHROME, FF24, FF31], 630=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 9, 9")
    public void dimensions__height_Function_() throws Exception {
        runTest("dimensions: height(Function)");
    }

    /**
     * Test {628=[IE11], 629=[CHROME, FF24, FF31], 631=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__height_Function_args__() throws Exception {
        runTest("dimensions: height(Function(args))");
    }

    /**
     * Test {629=[IE11], 630=[CHROME, FF24, FF31], 632=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerWidth__() throws Exception {
        runTest("dimensions: innerWidth()");
    }

    /**
     * Test {630=[IE11], 631=[CHROME, FF24, FF31], 633=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void dimensions__innerHeight__() throws Exception {
        runTest("dimensions: innerHeight()");
    }

    /**
     * Test {631=[IE11], 632=[CHROME, FF24, FF31], 634=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerWidth__() throws Exception {
        runTest("dimensions: outerWidth()");
    }

    /**
     * Test {632=[IE11], 633=[CHROME, FF24, FF31], 635=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__9441__9300() throws Exception {
        runTest("dimensions: child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #9441 #9300");
    }

    /**
     * Test {633=[IE11], 634=[CHROME, FF24, FF31], 636=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void dimensions__getting_dimensions_shouldnt_modify_runtimeStyle_see__9233() throws Exception {
        runTest("dimensions: getting dimensions shouldnt modify runtimeStyle see #9233");
    }

    /**
     * Test {634=[IE11], 635=[CHROME, FF24, FF31], 637=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__table_dimensions() throws Exception {
        runTest("dimensions: table dimensions");
    }

    /**
     * Test {635=[IE11], 636=[CHROME, FF24, FF31], 638=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void dimensions__box_sizing_border_box_child_of_a_hidden_elem__or_unconnected_node__has_accurate_inner_outer_Width___Height___see__10413() throws Exception {
        runTest("dimensions: box-sizing:border-box child of a hidden elem (or unconnected node) has accurate inner/outer/Width()/Height() see #10413");
    }

    /**
     * Test {636=[IE11], 637=[CHROME, FF24, FF31], 639=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 11, 11")
    public void dimensions__outerHeight__() throws Exception {
        runTest("dimensions: outerHeight()");
    }

    /**
     * Test {637=[IE11], 638=[CHROME, FF24, FF31], 640=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 4, 4")
    public void dimensions__passing_undefined_is_a_setter__5571() throws Exception {
        runTest("dimensions: passing undefined is a setter #5571");
    }

    /**
     * Test {638=[IE11], 639=[CHROME, FF24, FF31], 641=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 8, 8")
    public void dimensions__getters_on_non_elements_should_return_null() throws Exception {
        runTest("dimensions: getters on non elements should return null");
    }

    /**
     * Test {639=[IE11], 640=[CHROME, FF24, FF31], 642=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 20, 20")
    public void dimensions__setters_with_and_without_box_sizing_border_box() throws Exception {
        runTest("dimensions: setters with and without box-sizing:border-box");
    }

    /**
     * Test {640=[IE11], 641=[CHROME, FF24, FF31], 643=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    @NotYetImplemented
    public void dimensions__window_vs__small_document() throws Exception {
        runTest("dimensions: window vs. small document");
    }

    /**
     * Test {641=[IE11], 642=[CHROME, FF24, FF31], 644=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 2, 2")
    public void dimensions__window_vs__large_document() throws Exception {
        runTest("dimensions: window vs. large document");
    }

    /**
     * Test {642=[IE11], 643=[CHROME, FF24, FF31], 645=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 544, 544")
    public void deprecated__browser() throws Exception {
        runTest("deprecated: browser");
    }

    /**
     * Test {643=[IE11], 644=[CHROME, FF24, FF31], 646=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void exports__amdModule() throws Exception {
        runTest("exports: amdModule");
    }

    /**
     * Test {644=[IE11], 645=[CHROME, FF24, FF31], 647=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 36, 36")
    public void Sizzle__selector__element() throws Exception {
        runTest("Sizzle: selector: element");
    }

    /**
     * Test {645=[IE11], 646=[CHROME, FF24, FF31], 648=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    @NotYetImplemented
    public void Sizzle__selector__XML_Document_Selectors() throws Exception {
        runTest("Sizzle: selector: XML Document Selectors");
    }

    /**
     * Test {646=[IE11], 647=[CHROME, FF24, FF31], 649=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 21, 21")
    public void Sizzle__selector__broken() throws Exception {
        runTest("Sizzle: selector: broken");
    }

    /**
     * Test {647=[IE11], 648=[CHROME, FF24, FF31], 650=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 31, 31")
    public void Sizzle__selector__id() throws Exception {
        runTest("Sizzle: selector: id");
    }

    /**
     * Test {648=[IE11], 649=[CHROME, FF24, FF31], 651=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 24, 24")
    public void Sizzle__selector__class() throws Exception {
        runTest("Sizzle: selector: class");
    }

    /**
     * Test {649=[IE11], 650=[CHROME, FF24, FF31], 652=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 15, 15")
    public void Sizzle__selector__name() throws Exception {
        runTest("Sizzle: selector: name");
    }

    /**
     * Test {650=[IE11], 651=[CHROME, FF24, FF31], 653=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 6, 6")
    public void Sizzle__selector__multiple() throws Exception {
        runTest("Sizzle: selector: multiple");
    }

    /**
     * Test {651=[IE11], 652=[CHROME, FF24, FF31], 654=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    public void Sizzle__selector__child_and_adjacent() throws Exception {
        runTest("Sizzle: selector: child and adjacent");
    }

    /**
     * Test {652=[IE11], 653=[CHROME, FF24, FF31], 655=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 62, 62")
    public void Sizzle__selector__attributes() throws Exception {
        runTest("Sizzle: selector: attributes");
    }

    /**
     * Test {653=[IE11], 654=[CHROME, FF24, FF31], 656=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    public void Sizzle__selector__pseudo___child() throws Exception {
        runTest("Sizzle: selector: pseudo - child");
    }

    /**
     * Test {654=[IE11], 655=[CHROME, FF24, FF31], 657=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 42, 42")
    @NotYetImplemented
    public void Sizzle__selector__pseudo___misc() throws Exception {
        runTest("Sizzle: selector: pseudo - misc");
    }

    /**
     * Test {655=[IE11], 656=[CHROME, FF24, FF31], 658=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 43, 43")
    public void Sizzle__selector__pseudo____not() throws Exception {
        runTest("Sizzle: selector: pseudo - :not");
    }

    /**
     * Test {656=[IE11], 657=[CHROME, FF24, FF31], 659=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 33, 33")
    public void Sizzle__selector__pseudo___position() throws Exception {
        runTest("Sizzle: selector: pseudo - position");
    }

    /**
     * Test {657=[IE11], 658=[CHROME, FF24, FF31], 660=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 10, 10")
    public void Sizzle__selector__pseudo___form() throws Exception {
        runTest("Sizzle: selector: pseudo - form");
    }

    /**
     * Test {658=[IE11], 659=[CHROME, FF24, FF31], 661=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 1, 1")
    public void Sizzle__selector__caching() throws Exception {
        runTest("Sizzle: selector: caching");
    }

    /**
     * Test {659=[IE11], 660=[CHROME, FF24, FF31], 662=[IE8]}.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0, 16, 16")
    public void Sizzle__selector__Sizzle_contains() throws Exception {
        runTest("Sizzle: selector: Sizzle.contains");
    }
}
