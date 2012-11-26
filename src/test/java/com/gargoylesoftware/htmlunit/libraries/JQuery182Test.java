/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF3_6;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static org.junit.Assert.fail;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.ComparisonFailure;
import org.junit.Ignore;
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
 */
@RunWith(BrowserRunner.class)
public class JQuery182Test extends WebDriverTestCase {

    private static Server SERVER_;
    private static WebDriver WEBDRIVER_;

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
     * @param testNumber the test number
     * @throws Exception if an error occurs
     */
    protected void runTest(final int testNumber) throws Exception {
        final long runTime = 60 * DEFAULT_WAIT_TIME;
        final long endTime = System.currentTimeMillis() + runTime;

        try {
            WEBDRIVER_.get("http://localhost:" + PORT + "/jquery/test/index.html?testNumber="
                    + testNumber);

            final WebElement status = WEBDRIVER_.findElement(By.id("qunit-testresult"));
            while (!status.getText().startsWith("Tests completed")) {
                Thread.sleep(100);

                if (System.currentTimeMillis() > endTime) {
                    fail("Test #" + testNumber + " runs too long (longer than " + runTime / 1000 + "s)");
                }
            }

            final WebElement output = WEBDRIVER_.findElement(By.id("qunit-test-output0"));
            String result = output.getText();
            result = result.substring(0, result.indexOf("Rerun")).trim();
            final String expected = getExpectedAlerts()[0];
            if (!expected.contains(result)) {
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

    /**
     * This MUST be named 'aaa' so it runs at the very beginning. It initiates web client.
     * @throws Exception if an error occurs
     */
    @Test
    public void aaa_startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/jQuery/" + getVersion(), null);
        WEBDRIVER_ = getWebDriver();
    }

    /**
     * This MUST have 'zzz' so it runs at the very end. It closes the server and web client.
     * @throws Exception if an error occurs
     */
    @Test
    public void zzz_stopServer() throws Exception {
        SERVER_.stop();
        WEBDRIVER_.close();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: Unit Testing Environment (0, 2, 2)")
    public void test_1() throws Exception {
        runTest(1);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: Basic requirements (0, 7, 7)")
    public void test_2() throws Exception {
        runTest(2);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery() (0, 31, 31)")
    public void test_3() throws Exception {
        runTest(3);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: selector state (0, 31, 31)")
    public void test_4() throws Exception {
        runTest(4);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: globalEval (0, 3, 3)")
    public void test_5() throws Exception {
        runTest(5);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: noConflict (0, 7, 7)")
    public void test_6() throws Exception {
        runTest(6);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: trim (0, 13, 13)")
    public void test_7() throws Exception {
        runTest(7);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: type (0, 23, 23)")
    public void test_8() throws Exception {
        runTest(8);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: isPlainObject (0, 15, 15)")
    public void test_9() throws Exception {
        runTest(9);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: isFunction (0, 19, 19)")
    public void test_10() throws Exception {
        runTest(10);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: isNumeric (0, 36, 36)")
    public void test_11() throws Exception {
        runTest(11);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: isXMLDoc - HTML (0, 4, 4)")
    public void test_12() throws Exception {
        runTest(12);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: XSS via location.hash (0, 1, 1)")
    public void test_13() throws Exception {
        runTest(13);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: isXMLDoc - XML (0, 3, 3)")
    public void test_14() throws Exception {
        runTest(14);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: isWindow (0, 14, 14)")
    public void test_15() throws Exception {
        runTest(15);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery('html') (0, 18, 18)")
    public void test_16() throws Exception {
        runTest(16);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery('html', context) (0, 1, 1)")
    public void test_17() throws Exception {
        runTest(17);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery(selector, xml).text(str) - Loaded via XML document (0, 2, 2)")
    public void test_18() throws Exception {
        runTest(18);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: end() (0, 3, 3)")
    public void test_19() throws Exception {
        runTest(19);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: length (0, 1, 1)")
    public void test_20() throws Exception {
        runTest(20);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: size() (0, 1, 1)")
    public void test_21() throws Exception {
        runTest(21);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: get() (0, 1, 1)")
    public void test_22() throws Exception {
        runTest(22);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: toArray() (0, 1, 1)")
    public void test_23() throws Exception {
        runTest(23);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: inArray() (0, 19, 19)")
    public void test_24() throws Exception {
        runTest(24);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: get(Number) (0, 2, 2)")
    public void test_25() throws Exception {
        runTest(25);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: get(-Number) (0, 2, 2)")
    public void test_26() throws Exception {
        runTest(26);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: each(Function) (0, 1, 1)")
    public void test_27() throws Exception {
        runTest(27);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: slice() (0, 7, 7)")
    public void test_28() throws Exception {
        runTest(28);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: first()/last() (0, 4, 4)")
    public void test_29() throws Exception {
        runTest(29);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: map() (0, 8, 8)")
    public void test_30() throws Exception {
        runTest(30);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.merge() (0, 8, 8)")
    public void test_31() throws Exception {
        runTest(31);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.extend(Object, Object) (0, 28, 28)")
    public void test_32() throws Exception {
        runTest(32);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.each(Object,Function) (0, 14, 14)")
    public void test_33() throws Exception {
        runTest(33);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.makeArray (0, 17, 17)")
    public void test_34() throws Exception {
        runTest(34);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.inArray (0, 3, 3)")
    public void test_35() throws Exception {
        runTest(35);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.isEmptyObject (0, 2, 2)")
    public void test_36() throws Exception {
        runTest(36);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.proxy (0, 7, 7)")
    public void test_37() throws Exception {
        runTest(37);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.parseHTML (0, 11, 11)")
    public void test_38() throws Exception {
        runTest(38);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.parseJSON (0, 8, 8)")
    public void test_39() throws Exception {
        runTest(39);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.parseXML (0, 8, 8)")
    public void test_40() throws Exception {
        runTest(40);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.sub() - Static Methods (0, 18, 18)")
    public void test_41() throws Exception {
        runTest(41);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.sub() - .fn Methods (0, 378, 378)")
    public void test_42() throws Exception {
        runTest(42);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("core: jQuery.camelCase() (0, 7, 7)")
    public void test_43() throws Exception {
        runTest(43);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"\" ) - no filter (0, 20, 20)")
    public void test_44() throws Exception {
        runTest(44);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { } ) - no filter (0, 20, 20)")
    public void test_45() throws Exception {
        runTest(45);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"\" ) - filter (0, 20, 20)")
    public void test_46() throws Exception {
        runTest(46);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { } ) - filter (0, 20, 20)")
    public void test_47() throws Exception {
        runTest(47);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"once\" ) - no filter (0, 20, 20)")
    public void test_48() throws Exception {
        runTest(48);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"once\": true } ) - no filter (0, 20, 20)")
    public void test_49() throws Exception {
        runTest(49);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"once\" ) - filter (0, 20, 20)")
    public void test_50() throws Exception {
        runTest(50);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"once\": true } ) - filter (0, 20, 20)")
    public void test_51() throws Exception {
        runTest(51);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"memory\" ) - no filter (0, 20, 20)")
    public void test_52() throws Exception {
        runTest(52);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"memory\": true } ) - no filter (0, 20, 20)")
    public void test_53() throws Exception {
        runTest(53);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"memory\" ) - filter (0, 20, 20)")
    public void test_54() throws Exception {
        runTest(54);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"memory\": true } ) - filter (0, 20, 20)")
    public void test_55() throws Exception {
        runTest(55);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"unique\" ) - no filter (0, 20, 20)")
    public void test_56() throws Exception {
        runTest(56);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"unique\": true } ) - no filter (0, 20, 20)")
    public void test_57() throws Exception {
        runTest(57);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"unique\" ) - filter (0, 20, 20)")
    public void test_58() throws Exception {
        runTest(58);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"unique\": true } ) - filter (0, 20, 20)")
    public void test_59() throws Exception {
        runTest(59);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - no filter (0, 20, 20)")
    public void test_60() throws Exception {
        runTest(60);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - no filter (0, 20, 20)")
    public void test_61() throws Exception {
        runTest(61);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"stopOnFalse\" ) - filter (0, 20, 20)")
    public void test_62() throws Exception {
        runTest(62);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"stopOnFalse\": true } ) - filter (0, 20, 20)")
    public void test_63() throws Exception {
        runTest(63);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"once memory\" ) - no filter (0, 20, 20)")
    public void test_64() throws Exception {
        runTest(64);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - no filter (0, 20, 20)")
    public void test_65() throws Exception {
        runTest(65);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"once memory\" ) - filter (0, 20, 20)")
    public void test_66() throws Exception {
        runTest(66);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"once\": true, \"memory\": true } ) - filter (0, 20, 20)")
    public void test_67() throws Exception {
        runTest(67);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"once unique\" ) - no filter (0, 20, 20)")
    public void test_68() throws Exception {
        runTest(68);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - no filter (0, 20, 20)")
    public void test_69() throws Exception {
        runTest(69);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"once unique\" ) - filter (0, 20, 20)")
    public void test_70() throws Exception {
        runTest(70);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"once\": true, \"unique\": true } ) - filter (0, 20, 20)")
    public void test_71() throws Exception {
        runTest(71);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - no filter (0, 20, 20)")
    public void test_72() throws Exception {
        runTest(72);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - no filter (0, 20, 20)")
    public void test_73() throws Exception {
        runTest(73);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"once stopOnFalse\" ) - filter (0, 20, 20)")
    public void test_74() throws Exception {
        runTest(74);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"once\": true, \"stopOnFalse\": true } ) - filter (0, 20, 20)")
    public void test_75() throws Exception {
        runTest(75);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"memory unique\" ) - no filter (0, 20, 20)")
    public void test_76() throws Exception {
        runTest(76);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - no filter (0, 20, 20)")
    public void test_77() throws Exception {
        runTest(77);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"memory unique\" ) - filter (0, 20, 20)")
    public void test_78() throws Exception {
        runTest(78);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"memory\": true, \"unique\": true } ) - filter (0, 20, 20)")
    public void test_79() throws Exception {
        runTest(79);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - no filter (0, 20, 20)")
    public void test_80() throws Exception {
        runTest(80);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - no filter (0, 20, 20)")
    public void test_81() throws Exception {
        runTest(81);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"memory stopOnFalse\" ) - filter (0, 20, 20)")
    public void test_82() throws Exception {
        runTest(82);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"memory\": true, \"stopOnFalse\": true } ) - filter (0, 20, 20)")
    public void test_83() throws Exception {
        runTest(83);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - no filter (0, 20, 20)")
    public void test_84() throws Exception {
        runTest(84);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - no filter (0, 20, 20)")
    public void test_85() throws Exception {
        runTest(85);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( \"unique stopOnFalse\" ) - filter (0, 20, 20)")
    public void test_86() throws Exception {
        runTest(86);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( { \"unique\": true, \"stopOnFalse\": true } ) - filter (0, 20, 20)")
    public void test_87() throws Exception {
        runTest(87);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks( options ) - options are copied (0, 1, 1)")
    public void test_88() throws Exception {
        runTest(88);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks.fireWith - arguments are copied (0, 1, 1)")
    public void test_89() throws Exception {
        runTest(89);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks.remove - should remove all instances (0, 1, 1)")
    public void test_90() throws Exception {
        runTest(90);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("callbacks: jQuery.Callbacks() - adding a string doesn't cause a stack overflow (0, 1, 1)")
    public void test_91() throws Exception {
        runTest(91);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred (0, 23, 23)")
    public void test_92() throws Exception {
        runTest(92);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred - new operator (0, 23, 23)")
    public void test_93() throws Exception {
        runTest(93);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred - chainability (0, 10, 10)")
    public void test_94() throws Exception {
        runTest(94);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred.then - filtering (done) (0, 4, 4)")
    public void test_95() throws Exception {
        runTest(95);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred.then - filtering (fail) (0, 4, 4)")
    public void test_96() throws Exception {
        runTest(96);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred.then - filtering (progress) (0, 3, 3)")
    public void test_97() throws Exception {
        runTest(97);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred.then - deferred (done) (0, 3, 3)")
    public void test_98() throws Exception {
        runTest(98);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred.then - deferred (fail) (0, 3, 3)")
    public void test_99() throws Exception {
        runTest(99);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred.then - deferred (progress) (0, 3, 3)")
    public void test_100() throws Exception {
        runTest(100);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.Deferred.then - context (0, 4, 4)")
    public void test_101() throws Exception {
        runTest(101);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.when (0, 34, 34)")
    public void test_102() throws Exception {
        runTest(102);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("deferred: jQuery.when - joined (0, 119, 119)")
    public void test_103() throws Exception {
        runTest(103);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("support: boxModel (0, 1, 1)")
    public void test_104() throws Exception {
        runTest(104);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "support: body background is not lost if set prior to loading jQuery (#9238) (0, 2, 2)",
        IE = "support: body background is not lost if set prior to loading jQuery (#9238) (9, 1, 10)")
    @NotYetImplemented(IE)
    // Real IE9 is same as DEFAULT, and HtmlUnit succeeds with it
    public void test_105() throws Exception {
        runTest(105);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("support: A background on the testElement does not cause IE8 to crash (#9823) (0, 1, 1)")
    public void test_106() throws Exception {
        runTest(106);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "support: Verify that the support tests resolve as expected per browser (0, 30, 30)",
        FF10 = "data: expando (0, 1, 1)", CHROME = "data: expando (0, 1, 1)",
        IE = "support: Verify that the support tests resolve as expected per browser (6, 24, 30)")
    @NotYetImplemented
    public void test_107() throws Exception {
        runTest(107);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: expando (0, 1, 1)", FF10 = "data: jQuery.data (0, 124, 124)",
        CHROME = "data: jQuery.data (0, 124, 124)", IE = "data: expando (0, 1, 1)")
    public void test_108() throws Exception {
        runTest(108);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: jQuery.data (0, 124, 124)", FF10 = "data: jQuery.acceptData (0, 7, 7)",
        CHROME = "data: jQuery.acceptData (0, 7, 7)", IE = "data: jQuery.data (0, 124, 124)")
    public void test_109() throws Exception {
        runTest(109);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: jQuery.acceptData (0, 7, 7)", FF10 = "data: .data() (0, 5, 5)",
        CHROME = "data: .data() (0, 5, 5)", IE = "data: jQuery.acceptData (0, 7, 7)")
    public void test_110() throws Exception {
        runTest(110);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: .data() (0, 5, 5)", FF10 = "data: .data(String) and .data(String, Object) (0, 29, 29)",
        CHROME = "data: .data(String) and .data(String, Object) (0, 29, 29)",
        IE = "data: .data() (0, 5, 5)")
    public void test_111() throws Exception {
        runTest(111);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: .data(String) and .data(String, Object) (0, 29, 29)",
        FF10 = "data: data-* attributes (0, 40, 40)", CHROME = "data: data-* attributes (0, 40, 40)",
        IE = "data: .data(String) and .data(String, Object) (0, 29, 29)")
    public void test_112() throws Exception {
        runTest(112);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: data-* attributes (0, 40, 40)", FF10 = "data: .data(Object) (0, 4, 4)",
        CHROME = "data: .data(Object) (0, 4, 4)", IE = "data: data-* attributes (0, 40, 40)")
    public void test_113() throws Exception {
        runTest(113);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: .data(Object) (0, 4, 4)", FF10 = "data: jQuery.removeData (0, 10, 10)",
        CHROME = "data: jQuery.removeData (0, 10, 10)", IE = "data: .data(Object) (0, 4, 4)")
    public void test_114() throws Exception {
        runTest(114);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: jQuery.removeData (0, 10, 10)", FF10 = "data: .removeData() (0, 6, 6)",
        CHROME = "data: .removeData() (0, 6, 6)", IE = "data: jQuery.removeData (0, 10, 10)")
    public void test_115() throws Exception {
        runTest(115);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: .removeData() (0, 6, 6)", FF10 = "data: JSON serialization (#8108) (0, 1, 1)",
        CHROME = "data: JSON serialization (#8108) (0, 1, 1)",
        IE = "data: .removeData() (0, 6, 6)")
    public void test_116() throws Exception {
        runTest(116);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: JSON serialization (#8108) (0, 1, 1)",
        FF10 = "data: jQuery.data should follow html5 specification regarding camel casing (0, 10, 10)",
        CHROME = "data: jQuery.data should follow html5 specification regarding camel casing (0, 10, 10)",
        IE = "data: JSON serialization (#8108) (0, 1, 1)")
    public void test_117() throws Exception {
        runTest(117);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: jQuery.data should follow html5 specification regarding camel casing (0, 10, 10)",
        FF10 = "data: jQuery.data should not miss data with preset hyphenated property names (0, 2, 2)",
        CHROME = "data: jQuery.data should not miss data with preset hyphenated property names (0, 2, 2)",
        IE = "data: jQuery.data should follow html5 specification regarding camel casing (0, 10, 10)")
    public void test_118() throws Exception {
        runTest(118);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: jQuery.data should not miss data with preset hyphenated property names (0, 2, 2)",
        FF10 = "data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary n"
            + "on-null|NaN|undefined values (0, 24, 24)",
        CHROME = "data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary n"
            + "on-null|NaN|undefined values (0, 24, 24)",
        IE = "data: jQuery.data should not miss data with preset hyphenated property names (0, 2, 2)")
    public void test_119() throws Exception {
        runTest(119);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with"
            + " arbitrary non-null|NaN|undefined values (0, 24, 24)",
        FF10 = "data: jQuery.data supports interoperable removal of hyphenated/camelCase properties (0, 27, 27)",
        CHROME = "data: jQuery.data supports interoperable removal of hyphenated/camelCase properties (0, 27, 27)",
        IE = "data: jQuery.data supports interoperable hyphenated/camelCase get/set of properties with arbitrary n"
            + "on-null|NaN|undefined values (0, 24, 24)")
    public void test_120() throws Exception {
        runTest(120);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: jQuery.data supports interoperable removal of hyphenated/camelCase properties (0, 27, 27)",
        FF10 = "data: Triggering the removeData should not throw exceptions. (#10080) (0, 1, 1)",
        CHROME = "data: Triggering the removeData should not throw exceptions. (#10080) (0, 1, 1)",
        IE = "data: jQuery.data supports interoperable removal of hyphenated/camelCase properties (0, 27, 27)")
    public void test_121() throws Exception {
        runTest(121);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: Triggering the removeData should not throw exceptions. (#10080) (0, 1, 1)",
        FF10 = "data: Only check element attributes once when calling .data() - #8909 (0, 2, 2)",
        CHROME = "data: Only check element attributes once when calling .data() - #8909 (0, 2, 2)",
        IE = "data: Triggering the removeData should not throw exceptions. (#10080) (0, 1, 1)")
    @Ignore
    public void test_122() throws Exception {
        runTest(122);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: Only check element attributes once when calling .data() - #8909 (0, 2, 2)",
        FF10 = "data: JSON data- attributes can have newlines (0, 1, 1)",
        CHROME = "data: JSON data- attributes can have newlines (0, 1, 1)",
        IE = "data: Only check element attributes once when calling .data() - #8909 (0, 2, 2)")
    public void test_123() throws Exception {
        runTest(123);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "data: JSON data- attributes can have newlines (0, 1, 1)",
        FF10 = "queue: queue() with other types (0, 14, 14)",
        CHROME = "queue: queue() with other types (0, 14, 14)",
        IE = "data: JSON data- attributes can have newlines (0, 1, 1)")
    public void test_124() throws Exception {
        runTest(124);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: queue() with other types (0, 14, 14)",
        FF10 = "queue: queue(name) passes in the next item in the queue as a parameter (0, 2, 2)",
        CHROME = "queue: queue(name) passes in the next item in the queue as a parameter (0, 2, 2)",
        IE = "queue: queue() with other types (0, 14, 14)")
    public void test_125() throws Exception {
        runTest(125);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: queue(name) passes in the next item in the queue as a parameter (0, 2, 2)",
        FF10 = "queue: queue() passes in the next item in the queue as a parameter to fx queues (0, 3, 3)",
        CHROME = "queue: queue() passes in the next item in the queue as a parameter to fx queues (0, 3, 3)",
        IE = "queue: queue(name) passes in the next item in the queue as a parameter (0, 2, 2)")
    public void test_126() throws Exception {
        runTest(126);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: queue() passes in the next item in the queue as a parameter to fx queues (0, 3, 3)",
        FF10 = "queue: callbacks keep their place in the queue (0, 5, 5)",
        CHROME = "queue: callbacks keep their place in the queue (0, 5, 5)",
        IE = "queue: queue() passes in the next item in the queue as a parameter to fx queues (0, 3, 3)")
    public void test_127() throws Exception {
        runTest(127);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: callbacks keep their place in the queue (0, 5, 5)",
        FF10 = "queue: delay() (0, 2, 2)", CHROME = "queue: delay() (0, 2, 2)",
        IE = "queue: callbacks keep their place in the queue (0, 5, 5)")
    public void test_128() throws Exception {
        runTest(128);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: delay() (0, 2, 2)", FF10 = "queue: clearQueue(name) clears the queue (0, 2, 2)",
        CHROME = "queue: clearQueue(name) clears the queue (0, 2, 2)",
        IE = "queue: delay() (0, 2, 2)")
    public void test_129() throws Exception {
        runTest(129);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: clearQueue(name) clears the queue (0, 2, 2)",
        FF10 = "queue: clearQueue() clears the fx queue (0, 1, 1)",
        CHROME = "queue: clearQueue() clears the fx queue (0, 1, 1)",
        IE = "queue: clearQueue(name) clears the queue (0, 2, 2)")
    public void test_130() throws Exception {
        runTest(130);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: clearQueue() clears the fx queue (0, 1, 1)",
        FF10 = "queue: fn.promise() - called when fx queue is empty (0, 3, 3)",
        CHROME = "queue: fn.promise() - called when fx queue is empty (0, 3, 3)",
        IE = "queue: clearQueue() clears the fx queue (0, 1, 1)")
    public void test_131() throws Exception {
        runTest(131);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: fn.promise() - called when fx queue is empty (0, 3, 3)",
        FF10 = "queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued (0, 5, 5)",
        CHROME = "queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued (0, 5, 5)",
        IE = "queue: fn.promise() - called when fx queue is empty (0, 3, 3)")
    public void test_132() throws Exception {
        runTest(132);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued (0, 5, 5)",
        FF10 = "queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving (0, 2, 2)",
        CHROME = "queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving (0, 2, 2)",
        IE = "queue: fn.promise( \"queue\" ) - called whenever last queue function is dequeued (0, 5, 5)")
    public void test_133() throws Exception {
        runTest(133);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving (0, 2, 2)",
        FF10 = "queue: .promise(obj) (0, 2, 2)", CHROME = "queue: .promise(obj) (0, 2, 2)",
        IE = "queue: fn.promise( \"queue\" ) - waits for animation to complete before resolving (0, 2, 2)")
    public void test_134() throws Exception {
        runTest(134);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: .promise(obj) (0, 2, 2)", FF10 = "queue: delay() can be stopped (0, 3, 3)",
        CHROME = "queue: delay() can be stopped (0, 3, 3)", IE = "queue: .promise(obj) (0, 2, 2)")
    public void test_135() throws Exception {
        runTest(135);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: delay() can be stopped (2, 3, 5)", FF10 = "queue: queue stop hooks (0, 2, 2)",
    CHROME = "queue: queue stop hooks (0, 2, 2)", IE = "queue: delay() can be stopped (0, 3, 3)")
    @NotYetImplemented(FF3_6)
    public void test_136() throws Exception {
        runTest(136);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "queue: queue stop hooks (0, 2, 2)", FF10 = "attributes: jQuery.propFix integrity test (0, 1, 1)",
        CHROME = "attributes: jQuery.propFix integrity test (0, 1, 1)",
        IE = "queue: queue stop hooks (0, 2, 2)")
    public void test_137() throws Exception {
        runTest(137);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: jQuery.propFix integrity test (0, 1, 1)",
        FF10 = "attributes: attr(String) (0, 46, 46)", CHROME = "attributes: attr(String) (0, 46, 46)",
        IE = "attributes: jQuery.propFix integrity test (0, 1, 1)")
    public void test_138() throws Exception {
        runTest(138);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: attr(String) (0, 46, 46)", FF10 = "attributes: attr(String) in XML Files (0, 3, 3)",
        CHROME = "attributes: attr(String) in XML Files (0, 3, 3)",
        IE = "attributes: attr(String) (0, 46, 46)")
    public void test_139() throws Exception {
        runTest(139);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: attr(String) in XML Files (0, 3, 3)",
        FF10 = "attributes: attr(String, Function) (0, 2, 2)",
        CHROME = "attributes: attr(String, Function) (0, 2, 2)",
        IE = "attributes: attr(String) in XML Files (0, 3, 3)")
    public void test_140() throws Exception {
        runTest(140);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: attr(String, Function) (0, 2, 2)", FF10 = "attributes: attr(Hash) (0, 3, 3)",
        CHROME = "attributes: attr(Hash) (0, 3, 3)", IE = "attributes: attr(String, Function) (0, 2, 2)")
    public void test_141() throws Exception {
        runTest(141);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: attr(Hash) (0, 3, 3)", FF10 = "attributes: attr(String, Object) (0, 81, 81)",
        CHROME = "attributes: attr(String, Object) (0, 81, 81)",
        IE = "attributes: attr(Hash) (0, 3, 3)")
    public void test_142() throws Exception {
        runTest(142);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: attr(String, Object) (0, 81, 81)",
        FF10 = "attributes: attr(jquery_method) (0, 9, 9)",
        CHROME = "attributes: attr(jquery_method) (0, 9, 9)",
        IE = "attributes: attr(String, Object) (0, 81, 81)")
    public void test_143() throws Exception {
        runTest(143);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: attr(jquery_method) (0, 9, 9)",
        FF10 = "attributes: attr(String, Object) - Loaded via XML document (0, 2, 2)",
        CHROME = "attributes: attr(String, Object) - Loaded via XML document (0, 2, 2)",
        IE = "attributes: attr(jquery_method) (0, 9, 9)")
    public void test_144() throws Exception {
        runTest(144);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: attr(String, Object) - Loaded via XML document (0, 2, 2)",
        FF10 = "attributes: attr('tabindex') (0, 8, 8)", CHROME = "attributes: attr('tabindex') (0, 8, 8)",
        IE = "attributes: attr(String, Object) - Loaded via XML document (0, 2, 2)")
    public void test_145() throws Exception {
        runTest(145);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: attr('tabindex') (0, 8, 8)", FF10 = "attributes: attr('tabindex', value) (0, 9, 9)",
        CHROME = "attributes: attr('tabindex', value) (0, 9, 9)",
        IE = "attributes: attr('tabindex') (0, 8, 8)")
    public void test_146() throws Exception {
        runTest(146);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: attr('tabindex', value) (0, 9, 9)",
        FF10 = "attributes: removeAttr(String) (0, 12, 12)",
        CHROME = "attributes: removeAttr(String) (0, 12, 12)",
        IE = "attributes: attr('tabindex', value) (0, 9, 9)")
    public void test_147() throws Exception {
        runTest(147);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: removeAttr(String) (0, 12, 12)",
        FF10 = "attributes: removeAttr(String) in XML (0, 7, 7)",
        CHROME = "attributes: removeAttr(String) in XML (0, 7, 7)",
        IE = "attributes: removeAttr(String) (0, 12, 12)")
    public void test_148() throws Exception {
        runTest(148);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: removeAttr(String) in XML (0, 7, 7)",
        FF10 = "attributes: removeAttr(Multi String, variable space width) (0, 8, 8)",
        CHROME = "attributes: removeAttr(Multi String, variable space width) (0, 8, 8)",
        IE = "attributes: removeAttr(String) in XML (0, 7, 7)")
    public void test_149() throws Exception {
        runTest(149);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: removeAttr(Multi String, variable space width) (0, 8, 8)",
        FF10 = "attributes: prop(String, Object) (0, 31, 31)",
        CHROME = "attributes: prop(String, Object) (0, 31, 31)",
        IE = "attributes: removeAttr(Multi String, variable space width) (0, 8, 8)")
    public void test_150() throws Exception {
        runTest(150);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: prop(String, Object) (0, 31, 31)",
        FF10 = "attributes: prop('tabindex') (0, 8, 8)", CHROME = "attributes: prop('tabindex') (0, 8, 8)",
        IE = "attributes: prop(String, Object) (0, 31, 31)")
    public void test_151() throws Exception {
        runTest(151);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: prop('tabindex') (0, 8, 8)", FF10 = "attributes: prop('tabindex', value) (0, 9, 9)",
        CHROME = "attributes: prop('tabindex', value) (0, 9, 9)",
        IE = "attributes: prop('tabindex') (0, 8, 8)")
    public void test_152() throws Exception {
        runTest(152);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: prop('tabindex', value) (0, 9, 9)",
        FF10 = "attributes: removeProp(String) (0, 6, 6)", CHROME = "attributes: removeProp(String) (0, 6, 6)",
        IE = "attributes: prop('tabindex', value) (0, 9, 9)")
    public void test_153() throws Exception {
        runTest(153);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: removeProp(String) (0, 6, 6)", FF10 = "attributes: val() (0, 26, 26)",
        CHROME = "attributes: val() (0, 26, 26)", IE = "attributes: removeProp(String) (0, 6, 6)")
    public void test_154() throws Exception {
        runTest(154);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: val() (0, 26, 26)", FF10 = "attributes: val(String/Number) (0, 8, 8)",
        CHROME = "attributes: val() respects numbers without exception (Bug #9319) (0, 4, 4)",
        IE = "attributes: val() (0, 26, 26)")
    public void test_155() throws Exception {
        runTest(155);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: val(String/Number) (0, 8, 8)", FF10 = "attributes: val(Function) (0, 8, 8)",
        CHROME = "attributes: val(String/Number) (0, 8, 8)", IE = "attributes: val(String/Number) (0, 8, 8)")
    public void test_156() throws Exception {
        runTest(156);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: val(Function) (0, 8, 8)",
        FF10 = "attributes: val(Array of Numbers) (Bug #7123) (0, 4, 4)",
        CHROME = "attributes: val(Function) (0, 8, 8)", IE = "attributes: val(Function) (0, 8, 8)")
    public void test_157() throws Exception {
        runTest(157);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: val(Array of Numbers) (Bug #7123) (0, 4, 4)",
        FF10 = "attributes: val(Function) with incoming value (0, 10, 10)",
        CHROME = "attributes: val(Array of Numbers) (Bug #7123) (0, 4, 4)",
        IE = "attributes: val(Array of Numbers) (Bug #7123) (0, 4, 4)")
    public void test_158() throws Exception {
        runTest(158);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: val(Function) with incoming value (0, 10, 10)",
        FF10 = "attributes: val(select) after form.reset() (Bug #2551) (0, 3, 3)",
        CHROME = "attributes: val(Function) with incoming value (0, 10, 10)",
        IE = "attributes: val(Function) with incoming value (0, 10, 10)")
    public void test_159() throws Exception {
        runTest(159);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: val(select) after form.reset() (Bug #2551) (0, 3, 3)",
        FF10 = "attributes: addClass(String) (0, 9, 9)",
        CHROME = "attributes: val(select) after form.reset() (Bug #2551) (0, 3, 3)",
        IE = "attributes: val(select) after form.reset() (Bug #2551) (0, 3, 3)")
    public void test_160() throws Exception {
        runTest(160);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: addClass(String) (0, 9, 9)", FF10 = "attributes: addClass(Function) (0, 9, 9)",
        CHROME = "attributes: addClass(String) (0, 9, 9)", IE = "attributes: addClass(String) (0, 9, 9)")
    public void test_161() throws Exception {
        runTest(161);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: addClass(Function) (0, 9, 9)",
        FF10 = "attributes: addClass(Function) with incoming value (0, 48, 48)",
        CHROME = "attributes: addClass(Function) (0, 9, 9)", IE = "attributes: addClass(Function) (0, 9, 9)")
    public void test_162() throws Exception {
        runTest(162);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: addClass(Function) with incoming value (0, 48, 48)",
        FF10 = "attributes: removeClass(String) - simple (0, 7, 7)",
        CHROME = "attributes: addClass(Function) with incoming value (0, 48, 48)",
        IE = "attributes: addClass(Function) with incoming value (0, 48, 48)")
    public void test_163() throws Exception {
        runTest(163);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: removeClass(String) - simple (0, 7, 7)",
        FF10 = "attributes: removeClass(Function) - simple (0, 7, 7)",
        CHROME = "attributes: removeClass(String) - simple (0, 7, 7)",
        IE = "attributes: removeClass(String) - simple (0, 7, 7)")
    public void test_164() throws Exception {
        runTest(164);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: removeClass(Function) - simple (0, 7, 7)",
        FF10 = "attributes: removeClass(Function) with incoming value (0, 48, 48)",
        CHROME = "attributes: removeClass(Function) - simple (0, 7, 7)",
        IE = "attributes: removeClass(Function) - simple (0, 7, 7)")
    public void test_165() throws Exception {
        runTest(165);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: removeClass(Function) with incoming value (0, 48, 48)",
        FF10 = "attributes: removeClass() removes duplicates (0, 1, 1)",
        CHROME = "attributes: removeClass(Function) with incoming value (0, 48, 48)",
        IE = "attributes: removeClass(Function) with incoming value (0, 48, 48)")
    public void test_166() throws Exception {
        runTest(166);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: removeClass() removes duplicates (0, 1, 1)",
        FF10 = "attributes: toggleClass(String|boolean|undefined[, boolean]) (0, 17, 17)",
        CHROME = "attributes: removeClass() removes duplicates (0, 1, 1)",
        IE = "attributes: removeClass() removes duplicates (0, 1, 1)")
    public void test_167() throws Exception {
        runTest(167);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: toggleClass(String|boolean|undefined[, boolean]) (0, 17, 17)",
        FF10 = "attributes: toggleClass(Function[, boolean]) (0, 17, 17)",
        CHROME = "attributes: toggleClass(String|boolean|undefined[, boolean]) (0, 17, 17)",
        IE = "attributes: toggleClass(String|boolean|undefined[, boolean]) (0, 17, 17)")
    public void test_168() throws Exception {
        runTest(168);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: toggleClass(Function[, boolean]) (0, 17, 17)",
        FF10 = "attributes: toggleClass(Fucntion[, boolean]) with incoming value (0, 14, 14)",
        CHROME = "attributes: toggleClass(Function[, boolean]) (0, 17, 17)",
        IE = "attributes: toggleClass(Function[, boolean]) (0, 17, 17)")
    public void test_169() throws Exception {
        runTest(169);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: toggleClass(Fucntion[, boolean]) with incoming value (0, 14, 14)",
        FF10 = "attributes: addClass, removeClass, hasClass (0, 17, 17)",
        CHROME = "attributes: toggleClass(Fucntion[, boolean]) with incoming value (0, 14, 14)",
        IE = "attributes: toggleClass(Fucntion[, boolean]) with incoming value (0, 14, 14)")
    public void test_170() throws Exception {
        runTest(170);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: addClass, removeClass, hasClass (0, 17, 17)",
        FF10 = "attributes: contents().hasClass() returns correct values (0, 2, 2)",
        CHROME = "attributes: addClass, removeClass, hasClass (0, 17, 17)",
        IE = "attributes: addClass, removeClass, hasClass (0, 17, 17)")
    public void test_171() throws Exception {
        runTest(171);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: contents().hasClass() returns correct values (0, 2, 2)",
        FF10 = "attributes: coords returns correct values in IE6/IE7, see #10828 (0, 2, 2)",
        CHROME = "attributes: contents().hasClass() returns correct values (0, 2, 2)",
        IE = "attributes: contents().hasClass() returns correct values (0, 2, 2)")
    public void test_172() throws Exception {
        runTest(172);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "attributes: coords returns correct values in IE6/IE7, see #10828 (0, 2, 2)",
        FF10 = "event: null or undefined handler (0, 2, 2)",
        CHROME = "attributes: coords returns correct values in IE6/IE7, see #10828 (0, 2, 2)",
        IE = "attributes: coords returns correct values in IE6/IE7, see #10828 (0, 2, 2)")
    public void test_173() throws Exception {
        runTest(173);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: null or undefined handler (0, 2, 2)",
        FF10 = "event: bind(),live(),delegate() with non-null,defined data (0, 3, 3)",
        CHROME = "event: null or undefined handler (0, 2, 2)",
        IE = "event: null or undefined handler (0, 2, 2)")
    public void test_174() throws Exception {
        runTest(174);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(),live(),delegate() with non-null,defined data (0, 3, 3)",
        FF10 = "event: Handler changes and .trigger() order (0, 1, 1)",
        CHROME = "event: bind(),live(),delegate() with non-null,defined data (0, 3, 3)",
        IE = "event: bind(),live(),delegate() with non-null,defined data (0, 3, 3)")
    public void test_175() throws Exception {
        runTest(175);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: Handler changes and .trigger() order (0, 1, 1)",
        FF10 = "event: bind(), with data (0, 4, 4)", CHROME = "event: Handler changes and .trigger() order (0, 1, 1)",
        IE = "event: Handler changes and .trigger() order (0, 1, 1)")
    public void test_176() throws Exception {
        runTest(176);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), with data (0, 4, 4)", FF10 = "event: click(), with data (0, 3, 3)",
    CHROME = "event: bind(), with data (0, 4, 4)", IE = "event: bind(), with data (0, 4, 4)")
    public void test_177() throws Exception {
        runTest(177);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: click(), with data (0, 3, 3)",
    FF10 = "event: bind(), with data, trigger with data (0, 4, 4)",
        CHROME = "event: click(), with data (0, 3, 3)", IE = "event: click(), with data (0, 3, 3)")
    public void test_178() throws Exception {
        runTest(178);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), with data, trigger with data (0, 4, 4)",
        FF10 = "event: bind(), multiple events at once (0, 2, 2)",
        CHROME = "event: bind(), with data, trigger with data (0, 4, 4)",
        IE = "event: bind(), with data, trigger with data (0, 4, 4)")
    public void test_179() throws Exception {
        runTest(179);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), multiple events at once (0, 2, 2)",
        FF10 = "event: bind(), five events at once (0, 1, 1)",
        CHROME = "event: bind(), multiple events at once (0, 2, 2)",
        IE = "event: bind(), multiple events at once (0, 2, 2)")
    public void test_180() throws Exception {
        runTest(180);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), five events at once (0, 1, 1)",
        FF10 = "event: bind(), multiple events at once and namespaces (0, 7, 7)",
        CHROME = "event: bind(), five events at once (0, 1, 1)",
        IE = "event: bind(), five events at once (0, 1, 1)")
    public void test_181() throws Exception {
        runTest(181);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), multiple events at once and namespaces (0, 7, 7)",
        FF10 = "event: bind(), namespace with special add (0, 27, 27)",
        CHROME = "event: bind(), multiple events at once and namespaces (0, 7, 7)",
        IE = "event: bind(), multiple events at once and namespaces (0, 7, 7)")
    public void test_182() throws Exception {
        runTest(182);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), namespace with special add (0, 27, 27)",
        FF10 = "event: bind(), no data (0, 1, 1)", CHROME = "event: bind(), namespace with special add (0, 27, 27)",
        IE = "event: bind(), namespace with special add (0, 27, 27)")
    public void test_183() throws Exception {
        runTest(183);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), no data (0, 1, 1)", FF10 = "event: bind/one/unbind(Object) (0, 6, 6)",
        CHROME = "event: bind(), no data (0, 1, 1)", IE = "event: bind(), no data (0, 1, 1)")
    public void test_184() throws Exception {
        runTest(184);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind/one/unbind(Object) (0, 6, 6)",
        FF10 = "event: live/die(Object), delegate/undelegate(String, Object) (0, 6, 6)",
        CHROME = "event: bind/one/unbind(Object) (0, 6, 6)", IE = "event: bind/one/unbind(Object) (0, 6, 6)")
    public void test_185() throws Exception {
        runTest(185);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: live/die(Object), delegate/undelegate(String, Object) (0, 6, 6)",
        FF10 = "event: live/delegate immediate propagation (0, 2, 2)",
        CHROME = "event: live/die(Object), delegate/undelegate(String, Object) (0, 6, 6)",
        IE = "event: live/die(Object), delegate/undelegate(String, Object) (0, 6, 6)")
    public void test_186() throws Exception {
        runTest(186);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: live/delegate immediate propagation (0, 2, 2)",
        FF10 = "event: bind/delegate bubbling, isDefaultPrevented (0, 2, 2)",
        CHROME = "event: live/delegate immediate propagation (0, 2, 2)",
        IE = "event: live/delegate immediate propagation (0, 2, 2)")
    public void test_187() throws Exception {
        runTest(187);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind/delegate bubbling, isDefaultPrevented (0, 2, 2)",
        FF10 = "event: bind(), iframes (0, 1, 1)",
        CHROME = "event: bind/delegate bubbling, isDefaultPrevented (0, 2, 2)",
        IE = "event: bind/delegate bubbling, isDefaultPrevented (0, 2, 2)")
    public void test_188() throws Exception {
        runTest(188);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), iframes (0, 1, 1)", FF10 = "event: bind(), trigger change on select (0, 5, 5)",
        CHROME = "event: bind(), iframes (0, 1, 1)", IE = "event: bind(), iframes (0, 1, 1)")
    public void test_189() throws Exception {
        runTest(189);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), trigger change on select (0, 5, 5)",
        FF10 = "event: bind(), namespaced events, cloned events (0, 18, 18)",
        CHROME = "event: bind(), trigger change on select (0, 5, 5)",
        IE = "event: bind(), trigger change on select (0, 5, 5)")
    public void test_190() throws Exception {
        runTest(190);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), namespaced events, cloned events (0, 18, 18)",
        FF10 = "event: bind(), multi-namespaced events (0, 6, 6)",
        CHROME = "event: bind(), namespaced events, cloned events (0, 18, 18)",
        IE = "event: bind(), namespaced events, cloned events (0, 18, 18)")
    public void test_191() throws Exception {
        runTest(191);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), multi-namespaced events (0, 6, 6)",
        FF10 = "event: bind(), with same function (0, 2, 2)",
        CHROME = "event: bind(), multi-namespaced events (0, 6, 6)",
        IE = "event: bind(), multi-namespaced events (0, 6, 6)")
    public void test_192() throws Exception {
        runTest(192);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), with same function (0, 2, 2)",
        FF10 = "event: bind(), make sure order is maintained (0, 1, 1)",
        CHROME = "event: bind(), with same function (0, 2, 2)",
        IE = "event: bind(), with same function (0, 2, 2)")
    public void test_193() throws Exception {
        runTest(193);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), make sure order is maintained (0, 1, 1)",
        FF10 = "event: bind(), with different this object (0, 4, 4)",
        CHROME = "event: bind(), make sure order is maintained (0, 1, 1)",
        IE = "event: bind(), make sure order is maintained (0, 1, 1)")
    public void test_194() throws Exception {
        runTest(194);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(), with different this object (0, 4, 4)",
        FF10 = "event: bind(name, false), unbind(name, false) (0, 3, 3)",
        CHROME = "event: bind(), with different this object (0, 4, 4)",
        IE = "event: bind(), with different this object (0, 4, 4)")
    public void test_195() throws Exception {
        runTest(195);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind(name, false), unbind(name, false) (0, 3, 3)",
        FF10 = "event: live(name, false), die(name, false) (0, 3, 3)",
        CHROME = "event: bind(name, false), unbind(name, false) (0, 3, 3)",
        IE = "event: bind(name, false), unbind(name, false) (0, 3, 3)")
    public void test_196() throws Exception {
        runTest(196);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: live(name, false), die(name, false) (0, 3, 3)",
        FF10 = "event: delegate(selector, name, false), undelegate(selector, name, false) (0, 3, 3)",
        CHROME = "event: live(name, false), die(name, false) (0, 3, 3)",
        IE = "event: live(name, false), die(name, false) (0, 3, 3)")
    public void test_197() throws Exception {
        runTest(197);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: delegate(selector, name, false), undelegate(selector, name, false) (0, 3, 3)",
        FF10 = "event: bind()/trigger()/unbind() on plain object (0, 7, 7)",
        CHROME = "event: delegate(selector, name, false), undelegate(selector, name, false) (0, 3, 3)",
        IE = "event: delegate(selector, name, false), undelegate(selector, name, false) (0, 3, 3)")
    public void test_198() throws Exception {
        runTest(198);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: bind()/trigger()/unbind() on plain object (0, 7, 7)",
        FF10 = "event: unbind(type) (0, 1, 1)", CHROME = "event: bind()/trigger()/unbind() on plain object (0, 7, 7)",
        IE = "event: bind()/trigger()/unbind() on plain object (0, 7, 7)")
    public void test_199() throws Exception {
        runTest(199);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: unbind(type) (0, 1, 1)", FF10 = "event: unbind(eventObject) (0, 4, 4)",
        CHROME = "event: unbind(type) (0, 1, 1)", IE = "event: unbind(type) (0, 1, 1)")
    public void test_200() throws Exception {
        runTest(200);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: unbind(eventObject) (0, 4, 4)", FF10 = "event: hover() and hover pseudo-event (0, 3, 3)",
        CHROME = "event: unbind(eventObject) (0, 4, 4)", IE = "event: unbind(eventObject) (0, 4, 4)")
    public void test_201() throws Exception {
        runTest(201);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: hover() and hover pseudo-event (0, 3, 3)",
        FF10 = "event: mouseover triggers mouseenter (0, 1, 1)",
        CHROME = "event: hover() and hover pseudo-event (0, 3, 3)",
        IE = "event: hover() and hover pseudo-event (0, 3, 3)")
    public void test_202() throws Exception {
        runTest(202);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: mouseover triggers mouseenter (0, 1, 1)",
        FF10 = "event: withinElement implemented with jQuery.contains() (0, 1, 1)",
        CHROME = "event: mouseover triggers mouseenter (0, 1, 1)",
        IE = "event: mouseover triggers mouseenter (0, 1, 1)")
    public void test_203() throws Exception {
        runTest(203);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: withinElement implemented with jQuery.contains() (0, 1, 1)",
        FF10 = "event: mouseenter, mouseleave don't catch exceptions (0, 2, 2)",
        CHROME = "event: withinElement implemented with jQuery.contains() (0, 1, 1)",
        IE = "event: withinElement implemented with jQuery.contains() (0, 1, 1)")
    public void test_204() throws Exception {
        runTest(204);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: mouseenter, mouseleave don't catch exceptions (0, 2, 2)",
        FF10 = "event: trigger() shortcuts (0, 6, 6)",
        CHROME = "event: mouseenter, mouseleave don't catch exceptions (0, 2, 2)",
        IE = "event: mouseenter, mouseleave don't catch exceptions (0, 2, 2)")
    public void test_205() throws Exception {
        runTest(205);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: trigger() shortcuts (0, 6, 6)", FF10 = "event: trigger() bubbling (0, 18, 18)",
        CHROME = "event: trigger() shortcuts (0, 6, 6)", IE = "event: trigger() shortcuts (0, 6, 6)")
    public void test_206() throws Exception {
        runTest(206);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: trigger() bubbling (0, 18, 18)", FF10 = "event: trigger(type, [data], [fn]) (0, 16, 16)",
        CHROME = "event: trigger() bubbling (0, 18, 18)", IE = "event: trigger() bubbling (0, 18, 18)")
    public void test_207() throws Exception {
        runTest(207);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: trigger(type, [data], [fn]) (0, 16, 16)",
        FF10 = "event: submit event bubbles on copied forms (#11649) (0, 3, 3)",
        CHROME = "event: trigger(type, [data], [fn]) (0, 16, 16)",
        IE = "event: trigger(type, [data], [fn]) (0, 16, 16)")
    public void test_208() throws Exception {
        runTest(208);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: submit event bubbles on copied forms (#11649) (0, 3, 3)",
        FF10 = "event: change event bubbles on copied forms (#11796) (0, 3, 3)",
        CHROME = "event: submit event bubbles on copied forms (#11649) (0, 3, 3)",
        IE = "event: submit event bubbles on copied forms (#11649) (0, 3, 3)")
    @NotYetImplemented(IE)
    public void test_209() throws Exception {
        runTest(209);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: change event bubbles on copied forms (#11796) (0, 3, 3)",
        FF10 = "event: trigger(eventObject, [data], [fn]) (0, 28, 28)",
        CHROME = "event: change event bubbles on copied forms (#11796) (0, 3, 3)",
        IE = "event: change event bubbles on copied forms (#11796) (0, 3, 3)")
    public void test_210() throws Exception {
        runTest(210);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: trigger(eventObject, [data], [fn]) (0, 28, 28)",
        FF10 = "event: .trigger() bubbling on disconnected elements (#10489) (0, 2, 2)",
        CHROME = "event: trigger(eventObject, [data], [fn]) (0, 28, 28)",
        IE = "event: trigger(eventObject, [data], [fn]) (0, 28, 28)")
    public void test_211() throws Exception {
        runTest(211);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: .trigger() bubbling on disconnected elements (#10489) (0, 2, 2)",
        FF10 = "event: .trigger() doesn't bubble load event (#10717) (0, 1, 1)",
        CHROME = "event: .trigger() bubbling on disconnected elements (#10489) (0, 2, 2)",
        IE = "event: .trigger() bubbling on disconnected elements (#10489) (0, 2, 2)")
    public void test_212() throws Exception {
        runTest(212);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: .trigger() doesn't bubble load event (#10717) (0, 1, 1)",
        FF10 = "event: Delegated events in SVG (#10791) (0, 2, 2)",
        CHROME = "event: .trigger() doesn't bubble load event (#10717) (0, 1, 1)",
        IE = "event: .trigger() doesn't bubble load event (#10717) (0, 1, 1)")
    public void test_213() throws Exception {
        runTest(213);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: Delegated events in SVG (#10791) (0, 2, 2)",
        FF10 = "event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764) (0, 5, 5)",
        CHROME = "event: Delegated events in SVG (#10791) (0, 2, 2)",
        IE = "event: Delegated events in SVG (#10791) (0, 2, 2)")
    @NotYetImplemented(FF)
    public void test_214() throws Exception {
        runTest(214);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764) (0, 5, 5)",
        FF10 = "event: Submit event can be stopped (#11049) (0, 1, 1)",
        CHROME = "event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764) (0, 5, 5)",
        IE = "event: Delegated events in forms (#10844; #11145; #8165; #11382, #11764) (0, 5, 5)")
    public void test_215() throws Exception {
        runTest(215);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: Submit event can be stopped (#11049) (0, 1, 1)",
        FF10 = "event: on(beforeunload) creates/deletes window property instead of adding/removing event listener (0"
            + ", 3, 3)",
        CHROME = "event: Submit event can be stopped (#11049) (0, 1, 1)",
        IE = "event: Submit event can be stopped (#11049) (0, 1, 1)")
    public void test_216() throws Exception {
        runTest(216);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: on(beforeunload) creates/deletes window property instead of adding/removing event listener "
            + "(0, 3, 3)",
        FF10 = "event: jQuery.Event( type, props ) (0, 5, 5)",
        CHROME = "event: on(beforeunload) creates/deletes window property instead of adding/removing event listener "
            + "(0, 3, 3)",
        IE = "event: on(beforeunload) creates/deletes window property instead of adding/removing event listener "
            + "(0, 3, 3)")
    public void test_217() throws Exception {
        runTest(217);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: jQuery.Event( type, props ) (0, 5, 5)",
        FF10 = "event: jQuery.Event.currentTarget (0, 2, 2)",
        CHROME = "event: jQuery.Event( type, props ) (0, 5, 5)",
        IE = "event: jQuery.Event( type, props ) (0, 5, 5)")
    public void test_218() throws Exception {
        runTest(218);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: jQuery.Event.currentTarget (0, 2, 2)",
        FF10 = "event: toggle(Function, Function, ...) (0, 16, 16)",
        CHROME = "event: jQuery.Event.currentTarget (0, 2, 2)",
        IE = "event: jQuery.Event.currentTarget (0, 2, 2)")
    public void test_219() throws Exception {
        runTest(219);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: toggle(Function, Function, ...) (0, 16, 16)",
        FF10 = "event: .live()/.die() (0, 66, 66)", CHROME = "event: toggle(Function, Function, ...) (0, 16, 16)",
        IE = "event: toggle(Function, Function, ...) (0, 16, 16)")
    public void test_220() throws Exception {
        runTest(220);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: .live()/.die() (0, 66, 66)", FF10 = "event: die all bound events (0, 1, 1)",
        CHROME = "event: .live()/.die() (0, 66, 66)", IE = "event: .live()/.die() (0, 66, 66)")
    public void test_221() throws Exception {
        runTest(221);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: die all bound events (0, 1, 1)", FF10 = "event: live with multiple events (0, 1, 1)",
        CHROME = "event: die all bound events (0, 1, 1)", IE = "event: die all bound events (0, 1, 1)")
    public void test_222() throws Exception {
        runTest(222);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: live with multiple events (0, 1, 1)",
        FF10 = "event: live with namespaces (0, 15, 15)", CHROME = "event: live with multiple events (0, 1, 1)",
        IE = "event: live with multiple events (0, 1, 1)")
    public void test_223() throws Exception {
        runTest(223);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: live with namespaces (0, 15, 15)", FF10 = "event: live with change (0, 8, 8)",
        CHROME = "event: live with namespaces (0, 15, 15)", IE = "event: live with namespaces (0, 15, 15)")
    public void test_224() throws Exception {
        runTest(224);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: live with change (0, 8, 8)", FF10 = "event: live with submit (0, 7, 7)",
        CHROME = "event: live with change (0, 8, 8)", IE = "event: live with change (0, 8, 8)")
    public void test_225() throws Exception {
        runTest(225);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: live with submit (0, 7, 7)", FF10 = "event: live with special events (0, 13, 13)",
        CHROME = "event: live with submit (0, 7, 7)", IE = "event: live with submit (0, 7, 7)")
    public void test_226() throws Exception {
        runTest(226);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: live with special events (0, 13, 13)",
        FF10 = "event: .delegate()/.undelegate() (0, 65, 65)",
        CHROME = "event: live with special events (0, 13, 13)",
        IE = "event: live with special events (0, 13, 13)")
    public void test_227() throws Exception {
        runTest(227);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: .delegate()/.undelegate() (0, 65, 65)",
        FF10 = "event: jQuery.off using dispatched jQuery.Event (0, 1, 1)",
        CHROME = "event: .delegate()/.undelegate() (0, 65, 65)",
        IE = "event: .delegate()/.undelegate() (0, 65, 65)")
    public void test_228() throws Exception {
        runTest(228);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: jQuery.off using dispatched jQuery.Event (0, 1, 1)",
        FF10 = "event: delegated event with delegateTarget-relative selector (0, 3, 3)",
        CHROME = "event: jQuery.off using dispatched jQuery.Event (0, 1, 1)",
        IE = "event: jQuery.off using dispatched jQuery.Event (0, 1, 1)")
    public void test_229() throws Exception {
        runTest(229);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: delegated event with delegateTarget-relative selector (0, 3, 3)",
        FF10 = "event: stopPropagation() stops directly-bound events on delegated target (0, 1, 1)",
        CHROME = "event: delegated event with delegateTarget-relative selector (0, 3, 3)",
        IE = "event: delegated event with delegateTarget-relative selector (0, 3, 3)")
    public void test_230() throws Exception {
        runTest(230);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: stopPropagation() stops directly-bound events on delegated target (0, 1, 1)",
        FF10 = "event: undelegate all bound events (0, 2, 2)",
        CHROME = "event: stopPropagation() stops directly-bound events on delegated target (0, 1, 1)",
        IE = "event: stopPropagation() stops directly-bound events on delegated target (0, 1, 1)")
    public void test_231() throws Exception {
        runTest(231);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: undelegate all bound events (0, 2, 2)",
        FF10 = "event: delegate with multiple events (0, 1, 1)",
        CHROME = "event: undelegate all bound events (0, 2, 2)",
        IE = "event: undelegate all bound events (0, 2, 2)")
    public void test_232() throws Exception {
        runTest(232);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: delegate with multiple events (0, 1, 1)",
        FF10 = "event: delegate with change (0, 8, 8)", CHROME = "event: delegate with multiple events (0, 1, 1)",
        IE = "event: delegate with multiple events (0, 1, 1)")
    public void test_233() throws Exception {
        runTest(233);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: delegate with change (0, 8, 8)", FF10 = "event: delegate with submit (0, 2, 2)",
        CHROME = "event: delegate with change (0, 8, 8)", IE = "event: delegate with change (0, 8, 8)")
    public void test_234() throws Exception {
        runTest(234);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: delegate with submit (0, 2, 2)",
        FF10 = "event: undelegate() with only namespaces (0, 2, 2)",
        CHROME = "event: delegate with submit (0, 2, 2)", IE = "event: delegate with submit (0, 2, 2)")
    public void test_235() throws Exception {
        runTest(235);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: undelegate() with only namespaces (0, 2, 2)",
        FF10 = "event: Non DOM element events (0, 1, 1)",
        CHROME = "event: undelegate() with only namespaces (0, 2, 2)",
        IE = "event: undelegate() with only namespaces (0, 2, 2)")
    public void test_236() throws Exception {
        runTest(236);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: Non DOM element events (0, 1, 1)",
        FF10 = "event: inline handler returning false stops default (0, 1, 1)",
        CHROME = "event: Non DOM element events (0, 1, 1)", IE = "event: Non DOM element events (0, 1, 1)")
    public void test_237() throws Exception {
        runTest(237);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: inline handler returning false stops default (0, 1, 1)",
        FF10 = "event: window resize (0, 2, 2)",
        CHROME = "event: inline handler returning false stops default (0, 1, 1)",
        IE = "event: inline handler returning false stops default (0, 1, 1)")
    public void test_238() throws Exception {
        runTest(238);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: window resize (0, 2, 2)", FF10 = "event: focusin bubbles (0, 2, 2)",
        CHROME = "event: window resize (0, 2, 2)", IE = "event: window resize (0, 2, 2)")
    public void test_239() throws Exception {
        runTest(239);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: focusin bubbles (0, 2, 2)",
        FF10 = "event: custom events with colons (#3533, #8272) (0, 1, 1)",
        CHROME = "event: focusin bubbles (0, 2, 2)", IE = "event: focusin bubbles (0, 2, 2)")
    public void test_240() throws Exception {
        runTest(240);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: custom events with colons (#3533, #8272) (0, 1, 1)",
        FF10 = "event: .on and .off (0, 9, 9)", CHROME = "event: custom events with colons (#3533, #8272) (0, 1, 1)",
        IE = "event: custom events with colons (#3533, #8272) (0, 1, 1)")
    public void test_241() throws Exception {
        runTest(241);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: .on and .off (0, 9, 9)", FF10 = "event: special bind/delegate name mapping (0, 7, 7)",
        CHROME = "event: .on and .off (0, 9, 9)", IE = "event: .on and .off (0, 9, 9)")
    public void test_242() throws Exception {
        runTest(242);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: special bind/delegate name mapping (0, 7, 7)",
        FF10 = "event: .on and .off, selective mixed removal (#10705) (0, 7, 7)",
        CHROME = "event: special bind/delegate name mapping (0, 7, 7)",
        IE = "event: special bind/delegate name mapping (0, 7, 7)")
    public void test_243() throws Exception {
        runTest(243);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: .on and .off, selective mixed removal (#10705) (0, 7, 7)",
        FF10 = "event: .on( event-map, null-selector, data ) #11130 (0, 1, 1)",
        CHROME = "event: .on and .off, selective mixed removal (#10705) (0, 7, 7)",
        IE = "event: .on and .off, selective mixed removal (#10705) (0, 7, 7)")
    public void test_244() throws Exception {
        runTest(244);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: .on( event-map, null-selector, data ) #11130 (0, 1, 1)",
        FF10 = "event: clone() delegated events (#11076) (0, 3, 3)",
        CHROME = "event: .on( event-map, null-selector, data ) #11130 (0, 1, 1)",
        IE = "event: .on( event-map, null-selector, data ) #11130 (0, 1, 1)")
    public void test_245() throws Exception {
        runTest(245);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: clone() delegated events (#11076) (0, 3, 3)",
        FF10 = "event: fixHooks extensions (0, 2, 2)", CHROME = "event: clone() delegated events (#11076) (0, 3, 3)",
        IE = "event: clone() delegated events (#11076) (0, 3, 3)")
    public void test_246() throws Exception {
        runTest(246);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: fixHooks extensions (0, 2, 2)", FF10 = "event: jQuery.ready promise (0, 1, 1)",
        CHROME = "event: fixHooks extensions (0, 2, 2)", IE = "event: fixHooks extensions (0, 2, 2)")
    public void test_247() throws Exception {
        runTest(247);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: jQuery.ready promise (0, 1, 1)",
        FF10 = "event: jQuery.ready synchronous load with long loading subresources (0, 1, 1)",
        CHROME = "event: jQuery.ready promise (0, 1, 1)", IE = "event: jQuery.ready promise (0, 1, 1)")
    public void test_248() throws Exception {
        runTest(248);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: jQuery.ready synchronous load with long loading subresources (0, 1, 1)",
        FF10 = "event: jQuery.isReady (0, 2, 2)",
        CHROME = "event: jQuery.ready synchronous load with long loading subresources (0, 1, 1)",
        IE = "event: jQuery.ready synchronous load with long loading subresources (0, 1, 1)")
    public void test_249() throws Exception {
        runTest(249);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: jQuery.isReady (0, 2, 2)", FF10 = "event: jQuery ready (0, 10, 10)",
        CHROME = "event: jQuery.isReady (0, 2, 2)", IE = "event: jQuery.isReady (0, 2, 2)")
    public void test_250() throws Exception {
        runTest(250);
    }

}
