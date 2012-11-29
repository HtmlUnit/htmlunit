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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE6;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE7;
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

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: jQuery ready (0, 10, 10)",
    FF10 = "event: change handler should be detached from element (0, 2, 2)",
        CHROME = "event: jQuery ready (0, 10, 10)", IE = "event: jQuery ready (0, 10, 10)")
    public void test_251() throws Exception {
        runTest(251);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: change handler should be detached from element (0, 2, 2)",
        FF10 = "event: trigger click on checkbox, fires change event (0, 1, 1)",
        CHROME = "event: change handler should be detached from element (0, 2, 2)",
        IE = "event: change handler should be detached from element (0, 2, 2)")
    public void test_252() throws Exception {
        runTest(252);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "event: trigger click on checkbox, fires change event (0, 1, 1)",
        FF10 = "selector - jQuery only: element - jQuery only (0, 7, 7)",
        CHROME = "event: trigger click on checkbox, fires change event (0, 1, 1)",
        IE = "event: trigger click on checkbox, fires change event (0, 1, 1)")
    @NotYetImplemented(FF3_6)
    public void test_253() throws Exception {
        runTest(253);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "selector - jQuery only: element - jQuery only (0, 7, 7)",
        FF10 = "selector - jQuery only: class - jQuery only (0, 4, 4)",
        CHROME = "selector - jQuery only: element - jQuery only (0, 7, 7)",
        IE = "selector - jQuery only: element - jQuery only (0, 7, 7)")
    public void test_254() throws Exception {
        runTest(254);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "selector - jQuery only: class - jQuery only (0, 4, 4)",
        FF10 = "selector - jQuery only: attributes - jQuery only (0, 2, 2)",
        CHROME = "selector - jQuery only: class - jQuery only (0, 4, 4)",
        IE = "selector - jQuery only: class - jQuery only (0, 4, 4)")
    public void test_255() throws Exception {
        runTest(255);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "selector - jQuery only: attributes - jQuery only (0, 2, 2)",
        FF10 = "selector - jQuery only: pseudo - visibility (0, 9, 9)",
        CHROME = "selector - jQuery only: attributes - jQuery only (0, 2, 2)",
        IE = "selector - jQuery only: attributes - jQuery only (0, 2, 2)")
    public void test_256() throws Exception {
        runTest(256);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "selector - jQuery only: pseudo - visibility (0, 9, 9)",
        FF10 = "selector - jQuery only: disconnected nodes (0, 4, 4)",
        CHROME = "selector - jQuery only: pseudo - visibility (0, 9, 9)",
        IE = "selector - jQuery only: pseudo - visibility (0, 9, 9)")
    public void test_257() throws Exception {
        runTest(257);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "selector - jQuery only: disconnected nodes (0, 4, 4)",
        FF10 = "selector - jQuery only: attributes - jQuery.attr (0, 35, 35)",
        CHROME = "selector - jQuery only: disconnected nodes (0, 4, 4)",
        IE = "selector - jQuery only: disconnected nodes (0, 4, 4)")
    public void test_258() throws Exception {
        runTest(258);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "selector - jQuery only: attributes - jQuery.attr (0, 35, 35)",
        FF10 = "selector - jQuery only: Sizzle cache collides with multiple Sizzles on a page (0, 3, 3)",
        CHROME = "selector - jQuery only: attributes - jQuery.attr (0, 35, 35)",
        IE = "selector - jQuery only: attributes - jQuery.attr (0, 35, 35)")
    @NotYetImplemented
    // In /test/data/selector/html5_selector.html a reference to non existing iframe.html
    public void test_259() throws Exception {
        runTest(259);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "selector - jQuery only: Sizzle cache collides with multiple Sizzles on a page (0, 3, 3)",
        FF10 = "traversing: find(String) (0, 5, 5)",
        CHROME = "selector - jQuery only: Sizzle cache collides with multiple Sizzles on a page (0, 3, 3)",
        IE = "selector - jQuery only: Sizzle cache collides with multiple Sizzles on a page (0, 3, 3)")
    @NotYetImplemented
    public void test_260() throws Exception {
        runTest(260);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: find(String) (0, 5, 5)", FF10 = "traversing: find(node|jQuery object) (0, 11, 11)",
        CHROME = "traversing: find(String) (0, 5, 5)", IE = "traversing: find(String) (0, 5, 5)")
    public void test_261() throws Exception {
        runTest(261);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: find(node|jQuery object) (0, 11, 11)",
        FF10 = "traversing: is(String|undefined) (0, 30, 30)",
        CHROME = "traversing: find(node|jQuery object) (0, 11, 11)",
        IE = "traversing: find(node|jQuery object) (0, 11, 11)")
    public void test_262() throws Exception {
        runTest(262);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: is(String|undefined) (0, 30, 30)",
        FF10 = "traversing: is(jQuery) (0, 21, 21)", CHROME = "traversing: is(String|undefined) (0, 30, 30)",
        IE = "traversing: is(String|undefined) (0, 30, 30)")
    public void test_263() throws Exception {
        runTest(263);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: is(jQuery) (0, 21, 21)",
        FF10 = "traversing: is() with positional selectors (0, 23, 23)",
        CHROME = "traversing: is(jQuery) (0, 21, 21)", IE = "traversing: is(jQuery) (0, 21, 21)")
    public void test_264() throws Exception {
        runTest(264);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: is() with positional selectors (0, 23, 23)",
        FF10 = "traversing: index() (0, 2, 2)", CHROME = "traversing: is() with positional selectors (0, 23, 23)",
        IE = "traversing: is() with positional selectors (0, 23, 23)")
    public void test_265() throws Exception {
        runTest(265);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: index() (0, 2, 2)", FF10 = "traversing: index(Object|String|undefined) (0, 16, 16)",
        CHROME = "traversing: index() (0, 2, 2)", IE = "traversing: index() (0, 2, 2)")
    public void test_266() throws Exception {
        runTest(266);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: index(Object|String|undefined) (0, 16, 16)",
        FF10 = "traversing: filter(Selector|undefined) (0, 9, 9)",
        CHROME = "traversing: index(Object|String|undefined) (0, 16, 16)",
        IE = "traversing: index(Object|String|undefined) (0, 16, 16)")
    public void test_267() throws Exception {
        runTest(267);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: filter(Selector|undefined) (0, 9, 9)",
        FF10 = "traversing: filter(Function) (0, 2, 2)", CHROME = "traversing: filter(Selector|undefined) (0, 9, 9)",
        IE = "traversing: filter(Selector|undefined) (0, 9, 9)")
    public void test_268() throws Exception {
        runTest(268);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: filter(Function) (0, 2, 2)",
        FF10 = "traversing: filter(Element) (0, 1, 1)", CHROME = "traversing: filter(Function) (0, 2, 2)",
        IE = "traversing: filter(Function) (0, 2, 2)")
    public void test_269() throws Exception {
        runTest(269);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: filter(Element) (0, 1, 1)", FF10 = "traversing: filter(Array) (0, 1, 1)",
        CHROME = "traversing: filter(Element) (0, 1, 1)", IE = "traversing: filter(Element) (0, 1, 1)")
    public void test_270() throws Exception {
        runTest(270);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: filter(Array) (0, 1, 1)", FF10 = "traversing: filter(jQuery) (0, 1, 1)",
        CHROME = "traversing: filter(Array) (0, 1, 1)", IE = "traversing: filter(Array) (0, 1, 1)")
    public void test_271() throws Exception {
        runTest(271);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: filter(jQuery) (0, 1, 1)",
        FF10 = "traversing: filter() with positional selectors (0, 19, 19)",
        CHROME = "traversing: filter(jQuery) (0, 1, 1)", IE = "traversing: filter(jQuery) (0, 1, 1)")
    public void test_272() throws Exception {
        runTest(272);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: filter() with positional selectors (0, 19, 19)",
        FF10 = "traversing: closest() (0, 14, 14)",
        CHROME = "traversing: filter() with positional selectors (0, 19, 19)",
        IE = "traversing: filter() with positional selectors (0, 19, 19)")
    public void test_273() throws Exception {
        runTest(273);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: closest() (0, 14, 14)", FF10 = "traversing: closest(jQuery) (0, 8, 8)",
        CHROME = "traversing: closest() (0, 14, 14)", IE = "traversing: closest() (0, 14, 14)")
    public void test_274() throws Exception {
        runTest(274);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: closest(jQuery) (0, 8, 8)", FF10 = "traversing: not(Selector|undefined) (0, 11, 11)",
        CHROME = "traversing: closest(jQuery) (0, 8, 8)", IE = "traversing: closest(jQuery) (0, 8, 8)")
    public void test_275() throws Exception {
        runTest(275);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: not(Selector|undefined) (0, 11, 11)",
        FF10 = "traversing: not(Element) (0, 1, 1)", CHROME = "traversing: not(Selector|undefined) (0, 11, 11)",
        IE = "traversing: not(Selector|undefined) (0, 11, 11)")
    public void test_276() throws Exception {
        runTest(276);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: not(Element) (0, 1, 1)", FF10 = "traversing: not(Function) (0, 1, 1)",
        CHROME = "traversing: not(Element) (0, 1, 1)", IE = "traversing: not(Element) (0, 1, 1)")
    public void test_277() throws Exception {
        runTest(277);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: not(Function) (0, 1, 1)", FF10 = "traversing: not(Array) (0, 2, 2)",
        CHROME = "traversing: not(Function) (0, 1, 1)", IE = "traversing: not(Function) (0, 1, 1)")
    public void test_278() throws Exception {
        runTest(278);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: not(Array) (0, 2, 2)", FF10 = "traversing: not(jQuery) (0, 1, 1)",
        CHROME = "traversing: not(Array) (0, 2, 2)", IE = "traversing: not(Array) (0, 2, 2)")
    public void test_279() throws Exception {
        runTest(279);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: not(jQuery) (0, 1, 1)", FF10 = "traversing: has(Element) (0, 3, 3)",
        CHROME = "traversing: not(jQuery) (0, 1, 1)", IE = "traversing: not(jQuery) (0, 1, 1)")
    public void test_280() throws Exception {
        runTest(280);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: has(Element) (0, 3, 3)", FF10 = "traversing: has(Selector) (0, 5, 5)",
        CHROME = "traversing: has(Element) (0, 3, 3)", IE = "traversing: has(Element) (0, 3, 3)")
    public void test_281() throws Exception {
        runTest(281);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: has(Selector) (0, 5, 5)", FF10 = "traversing: has(Arrayish) (0, 4, 4)",
        CHROME = "traversing: has(Selector) (0, 5, 5)", IE = "traversing: has(Selector) (0, 5, 5)")
    public void test_282() throws Exception {
        runTest(282);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: has(Arrayish) (0, 4, 4)", FF10 = "traversing: addBack() (0, 5, 5)",
        CHROME = "traversing: has(Arrayish) (0, 4, 4)", IE = "traversing: has(Arrayish) (0, 4, 4)")
    public void test_283() throws Exception {
        runTest(283);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: addBack() (0, 5, 5)", FF10 = "traversing: siblings([String]) (0, 7, 7)",
        CHROME = "traversing: addBack() (0, 5, 5)", IE = "traversing: addBack() (0, 5, 5)")
    public void test_284() throws Exception {
        runTest(284);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: siblings([String]) (0, 7, 7)", FF10 = "traversing: children([String]) (0, 3, 3)",
        CHROME = "traversing: siblings([String]) (0, 7, 7)", IE = "traversing: siblings([String]) (0, 7, 7)")
    public void test_285() throws Exception {
        runTest(285);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: children([String]) (0, 3, 3)", FF10 = "traversing: parent([String]) (0, 5, 5)",
        CHROME = "traversing: children([String]) (0, 3, 3)", IE = "traversing: children([String]) (0, 3, 3)")
    public void test_286() throws Exception {
        runTest(286);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: parent([String]) (0, 5, 5)", FF10 = "traversing: parents([String]) (0, 5, 5)",
        CHROME = "traversing: parent([String]) (0, 5, 5)", IE = "traversing: parent([String]) (0, 5, 5)")
    public void test_287() throws Exception {
        runTest(287);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: parents([String]) (0, 5, 5)", FF10 = "traversing: parentsUntil([String]) (0, 9, 9)",
        CHROME = "traversing: parents([String]) (0, 5, 5)", IE = "traversing: parents([String]) (0, 5, 5)")
    public void test_288() throws Exception {
        runTest(288);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: parentsUntil([String]) (0, 9, 9)",
        FF10 = "traversing: next([String]) (0, 5, 5)", CHROME = "traversing: parentsUntil([String]) (0, 9, 9)",
        IE = "traversing: parentsUntil([String]) (0, 9, 9)")
    public void test_289() throws Exception {
        runTest(289);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: next([String]) (0, 5, 5)", FF10 = "traversing: prev([String]) (0, 4, 4)",
        CHROME = "traversing: next([String]) (0, 5, 5)", IE = "traversing: next([String]) (0, 5, 5)")
    public void test_290() throws Exception {
        runTest(290);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: prev([String]) (0, 4, 4)", FF10 = "traversing: nextAll([String]) (0, 4, 4)",
        CHROME = "traversing: prev([String]) (0, 4, 4)", IE = "traversing: prev([String]) (0, 4, 4)")
    public void test_291() throws Exception {
        runTest(291);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: nextAll([String]) (0, 4, 4)", FF10 = "traversing: prevAll([String]) (0, 4, 4)",
        CHROME = "traversing: nextAll([String]) (0, 4, 4)", IE = "traversing: nextAll([String]) (0, 4, 4)")
    public void test_292() throws Exception {
        runTest(292);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: prevAll([String]) (0, 4, 4)", FF10 = "traversing: nextUntil([String]) (0, 11, 11)",
        CHROME = "traversing: prevAll([String]) (0, 4, 4)", IE = "traversing: prevAll([String]) (0, 4, 4)")
    public void test_293() throws Exception {
        runTest(293);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: nextUntil([String]) (0, 11, 11)",
        FF10 = "traversing: prevUntil([String]) (0, 10, 10)",
        CHROME = "traversing: nextUntil([String]) (0, 11, 11)",
        IE = "traversing: nextUntil([String]) (0, 11, 11)")
    public void test_294() throws Exception {
        runTest(294);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: prevUntil([String]) (0, 10, 10)",
        FF10 = "traversing: contents() (0, 12, 12)", CHROME = "traversing: prevUntil([String]) (0, 10, 10)",
        IE = "traversing: prevUntil([String]) (0, 10, 10)")
    public void test_295() throws Exception {
        runTest(295);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: contents() (0, 12, 12)",
        FF10 = "traversing: add(String|Element|Array|undefined) (0, 16, 16)",
        CHROME = "traversing: contents() (0, 12, 12)", IE = "traversing: contents() (0, 12, 12)")
    public void test_296() throws Exception {
        runTest(296);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: add(String|Element|Array|undefined) (0, 16, 16)",
        FF10 = "traversing: add(String, Context) (0, 6, 6)",
        CHROME = "traversing: add(String|Element|Array|undefined) (0, 16, 16)",
        IE = "traversing: add(String|Element|Array|undefined) (0, 16, 16)")
    public void test_297() throws Exception {
        runTest(297);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: add(String, Context) (0, 6, 6)",
        FF10 = "traversing: eq('-1') #10616 (0, 3, 3)", CHROME = "traversing: add(String, Context) (0, 6, 6)",
        IE = "traversing: add(String, Context) (0, 6, 6)")
    public void test_298() throws Exception {
        runTest(298);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "traversing: eq('-1') #10616 (0, 3, 3)", FF10 = "manipulation: text() (0, 5, 5)",
        CHROME = "traversing: eq('-1') #10616 (0, 3, 3)", IE = "traversing: eq('-1') #10616 (0, 3, 3)")
    public void test_299() throws Exception {
        runTest(299);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: text() (0, 5, 5)", FF10 = "manipulation: text(undefined) (0, 1, 1)",
        CHROME = "manipulation: text() (0, 5, 5)", IE = "manipulation: text() (0, 5, 5)")
    public void test_300() throws Exception {
        runTest(300);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: text(undefined) (0, 1, 1)", FF10 = "manipulation: text(String) (0, 4, 4)",
        CHROME = "manipulation: text(undefined) (0, 1, 1)", IE = "manipulation: text(undefined) (0, 1, 1)")
    public void test_301() throws Exception {
        runTest(301);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: text(String) (0, 4, 4)", FF10 = "manipulation: text(Function) (0, 4, 4)",
        CHROME = "manipulation: text(String) (0, 4, 4)", IE = "manipulation: text(String) (0, 4, 4)")
    public void test_302() throws Exception {
        runTest(302);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: text(Function) (0, 4, 4)",
        FF10 = "manipulation: text(Function) with incoming value (0, 2, 2)",
        CHROME = "manipulation: text(Function) (0, 4, 4)", IE = "manipulation: text(Function) (0, 4, 4)")
    public void test_303() throws Exception {
        runTest(303);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: text(Function) with incoming value (0, 2, 2)",
        FF10 = "manipulation: wrap(String|Element) (0, 19, 19)",
        CHROME = "manipulation: text(Function) with incoming value (0, 2, 2)",
        IE = "manipulation: text(Function) with incoming value (0, 2, 2)")
    public void test_304() throws Exception {
        runTest(304);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: wrap(String|Element) (0, 19, 19)",
        FF10 = "manipulation: wrap(Function) (0, 19, 19)", CHROME = "manipulation: wrap(String|Element) (0, 19, 19)",
        IE = "manipulation: wrap(String|Element) (0, 19, 19)")
    public void test_305() throws Exception {
        runTest(305);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: wrap(Function) (0, 19, 19)",
        FF10 = "manipulation: wrap(Function) with index (#10177) (0, 6, 6)",
        CHROME = "manipulation: wrap(Function) (0, 19, 19)", IE = "manipulation: wrap(Function) (0, 19, 19)")
    public void test_306() throws Exception {
        runTest(306);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: wrap(Function) with index (#10177) (0, 6, 6)",
        FF10 = "manipulation: wrap(String) consecutive elements (#10177) (0, 12, 12)",
        CHROME = "manipulation: wrap(Function) with index (#10177) (0, 6, 6)",
        IE = "manipulation: wrap(Function) with index (#10177) (0, 6, 6)")
    public void test_307() throws Exception {
        runTest(307);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: wrap(String) consecutive elements (#10177) (0, 12, 12)",
        FF10 = "manipulation: wrapAll(String|Element) (0, 8, 8)",
        CHROME = "manipulation: wrap(String) consecutive elements (#10177) (0, 12, 12)",
        IE = "manipulation: wrap(String) consecutive elements (#10177) (0, 12, 12)")
    public void test_308() throws Exception {
        runTest(308);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: wrapAll(String|Element) (0, 8, 8)",
        FF10 = "manipulation: wrapInner(String|Element) (0, 11, 11)",
        CHROME = "manipulation: wrapAll(String|Element) (0, 8, 8)",
        IE = "manipulation: wrapAll(String|Element) (0, 8, 8)")
    public void test_309() throws Exception {
        runTest(309);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: wrapInner(String|Element) (0, 11, 11)",
        FF10 = "manipulation: wrapInner(Function) (0, 11, 11)",
        CHROME = "manipulation: wrapInner(String|Element) (0, 11, 11)",
        IE = "manipulation: wrapInner(String|Element) (0, 11, 11)")
    public void test_310() throws Exception {
        runTest(310);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: wrapInner(Function) (0, 11, 11)",
        FF10 = "manipulation: unwrap() (0, 9, 9)", CHROME = "manipulation: wrapInner(Function) (0, 11, 11)",
        IE = "manipulation: wrapInner(Function) (0, 11, 11)")
    public void test_311() throws Exception {
        runTest(311);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: unwrap() (0, 9, 9)",
        FF10 = "manipulation: append(String|Element|Array&lt;Element&gt;|jQuery) (0, 58, 58)",
        CHROME = "manipulation: unwrap() (0, 9, 9)", IE = "manipulation: unwrap() (0, 9, 9)")
    public void test_312() throws Exception {
        runTest(312);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: append(String|Element|Array&lt;Element&gt;|jQuery) (0, 58, 58)",
        FF10 = "manipulation: append(Function) (0, 58, 58)",
        CHROME = "manipulation: append(String|Element|Array&lt;Element&gt;|jQuery) (0, 58, 58)",
        IE = "manipulation: append(String|Element|Array&lt;Element&gt;|jQuery) (0, 58, 58)")
    public void test_313() throws Exception {
        runTest(313);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: append(Function) (0, 58, 58)",
        FF10 = "manipulation: append(Function) with incoming value (0, 12, 12)",
        CHROME = "manipulation: append(Function) (0, 58, 58)",
        IE = "manipulation: append(Function) (0, 58, 58)")
    public void test_314() throws Exception {
        runTest(314);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: append(Function) with incoming value (0, 12, 12)",
        FF10 = "manipulation: append the same fragment with events (Bug #6997, 5566) (0, 2, 2)",
        CHROME = "manipulation: append(Function) with incoming value (0, 12, 12)",
        IE = "manipulation: append(Function) with incoming value (0, 12, 12)")
    public void test_315() throws Exception {
        runTest(315);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: append the same fragment with events (Bug #6997, 5566) (0, 2, 2)",
        FF10 = "manipulation: append HTML5 sectioning elements (Bug #6485) (0, 2, 2)",
        CHROME = "manipulation: append the same fragment with events (Bug #6997, 5566) (0, 2, 2)",
        IE = "manipulation: append the same fragment with events (Bug #6997, 5566) (0, 3, 3)")
    public void test_316() throws Exception {
        runTest(316);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: append HTML5 sectioning elements (Bug #6485) (0, 2, 2)",
        FF10 = "manipulation: HTML5 Elements inherit styles from style rules (Bug #10501) (0, 1, 1)",
        CHROME = "manipulation: append HTML5 sectioning elements (Bug #6485) (0, 2, 2)",
        IE = "manipulation: append HTML5 sectioning elements (Bug #6485) (0, 2, 2)")
    public void test_317() throws Exception {
        runTest(317);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: HTML5 Elements inherit styles from style rules (Bug #10501) (0, 1, 1)",
        FF10 = "manipulation: html5 clone() cannot use the fragment cache in IE (#6485) (0, 1, 1)",
        CHROME = "manipulation: HTML5 Elements inherit styles from style rules (Bug #10501) (0, 1, 1)",
        IE = "manipulation: HTML5 Elements inherit styles from style rules (Bug #10501) (0, 1, 1)")
    public void test_318() throws Exception {
        runTest(318);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: html5 clone() cannot use the fragment cache in IE (#6485) (0, 1, 1)",
        FF10 = "manipulation: html(String) with HTML5 (Bug #6485) (0, 2, 2)",
        CHROME = "manipulation: html5 clone() cannot use the fragment cache in IE (#6485) (0, 1, 1)",
        IE = "manipulation: html5 clone() cannot use the fragment cache in IE (#6485) (0, 1, 1)")
    public void test_319() throws Exception {
        runTest(319);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: html(String) with HTML5 (Bug #6485) (0, 2, 2)",
        FF10 = "manipulation: IE8 serialization bug (0, 2, 2)",
        CHROME = "manipulation: html(String) with HTML5 (Bug #6485) (0, 2, 2)",
        IE = "manipulation: html(String) with HTML5 (Bug #6485) (0, 2, 2)")
    public void test_320() throws Exception {
        runTest(320);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: IE8 serialization bug (0, 2, 2)",
        FF10 = "manipulation: html() object element #10324 (0, 1, 1)",
        CHROME = "manipulation: IE8 serialization bug (0, 2, 2)",
        IE = "manipulation: IE8 serialization bug (0, 2, 2)")
    public void test_321() throws Exception {
        runTest(321);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: html() object element #10324 (0, 1, 1)",
        FF10 = "manipulation: append(xml) (0, 1, 1)", CHROME = "manipulation: html() object element #10324 (0, 1, 1)",
        IE = "manipulation: html() object element #10324 (0, 1, 1)")
    public void test_322() throws Exception {
        runTest(322);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: append(xml) (0, 1, 1)",
        FF10 = "manipulation: appendTo(String|Element|Array&lt;Element&gt;|jQuery) (0, 17, 17)",
        CHROME = "manipulation: append(xml) (0, 1, 1)", IE = "manipulation: append(xml) (0, 1, 1)")
    public void test_323() throws Exception {
        runTest(323);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: appendTo(String|Element|Array&lt;Element&gt;|jQuery) (0, 17, 17)",
        FF10 = "manipulation: prepend(String|Element|Array&lt;Element&gt;|jQuery) (0, 6, 6)",
        CHROME = "manipulation: appendTo(String|Element|Array&lt;Element&gt;|jQuery) (0, 17, 17)",
        IE = "manipulation: appendTo(String|Element|Array&lt;Element&gt;|jQuery) (0, 17, 17)")
    public void test_324() throws Exception {
        runTest(324);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: prepend(String|Element|Array&lt;Element&gt;|jQuery) (0, 6, 6)",
        FF10 = "manipulation: prepend(Function) (0, 6, 6)",
        CHROME = "manipulation: prepend(String|Element|Array&lt;Element&gt;|jQuery) (0, 6, 6)",
        IE = "manipulation: prepend(String|Element|Array&lt;Element&gt;|jQuery) (0, 6, 6)")
    public void test_325() throws Exception {
        runTest(325);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: prepend(Function) (0, 6, 6)",
        FF10 = "manipulation: prepend(Function) with incoming value (0, 10, 10)",
        CHROME = "manipulation: prepend(Function) (0, 6, 6)",
        IE = "manipulation: prepend(Function) (0, 6, 6)")
    public void test_326() throws Exception {
        runTest(326);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: prepend(Function) with incoming value (0, 10, 10)",
        FF10 = "manipulation: prependTo(String|Element|Array&lt;Element&gt;|jQuery) (0, 6, 6)",
        CHROME = "manipulation: prepend(Function) with incoming value (0, 10, 10)",
        IE = "manipulation: prepend(Function) with incoming value (0, 10, 10)")
    public void test_327() throws Exception {
        runTest(327);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: prependTo(String|Element|Array&lt;Element&gt;|jQuery) (0, 6, 6)",
        FF10 = "manipulation: before(String|Element|Array&lt;Element&gt;|jQuery) (0, 7, 7)",
        CHROME = "manipulation: prependTo(String|Element|Array&lt;Element&gt;|jQuery) (0, 6, 6)",
        IE = "manipulation: prependTo(String|Element|Array&lt;Element&gt;|jQuery) (0, 6, 6)")
    public void test_328() throws Exception {
        runTest(328);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: before(String|Element|Array&lt;Element&gt;|jQuery) (0, 7, 7)",
        FF10 = "manipulation: before(Function) (0, 7, 7)",
        CHROME = "manipulation: before(String|Element|Array&lt;Element&gt;|jQuery) (0, 7, 7)",
        IE = "manipulation: before(String|Element|Array&lt;Element&gt;|jQuery) (0, 7, 7)")
    public void test_329() throws Exception {
        runTest(329);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: before(Function) (0, 7, 7)",
        FF10 = "manipulation: before and after w/ empty object (#10812) (0, 2, 2)",
        CHROME = "manipulation: before(Function) (0, 7, 7)", IE = "manipulation: before(Function) (0, 7, 7)")
    public void test_330() throws Exception {
        runTest(330);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: before and after w/ empty object (#10812) (0, 2, 2)",
        FF10 = "manipulation: before and after on disconnected node (#10517) (0, 2, 2)",
        CHROME = "manipulation: before and after w/ empty object (#10812) (0, 2, 2)",
        IE = "manipulation: before and after w/ empty object (#10812) (0, 2, 2)")
    public void test_331() throws Exception {
        runTest(331);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: before and after on disconnected node (#10517) (0, 2, 2)",
        FF10 = "manipulation: insertBefore(String|Element|Array&lt;Element&gt;|jQuery) (0, 4, 4)",
        CHROME = "manipulation: before and after on disconnected node (#10517) (0, 2, 2)",
        IE = "manipulation: before and after on disconnected node (#10517) (0, 2, 2)")
    public void test_332() throws Exception {
        runTest(332);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: insertBefore(String|Element|Array&lt;Element&gt;|jQuery) (0, 4, 4)",
        FF10 = "manipulation: after(String|Element|Array&lt;Element&gt;|jQuery) (0, 7, 7)",
        CHROME = "manipulation: insertBefore(String|Element|Array&lt;Element&gt;|jQuery) (0, 4, 4)",
        IE = "manipulation: insertBefore(String|Element|Array&lt;Element&gt;|jQuery) (0, 4, 4)")
    public void test_333() throws Exception {
        runTest(333);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: after(String|Element|Array&lt;Element&gt;|jQuery) (0, 7, 7)",
        FF10 = "manipulation: after(Function) (0, 7, 7)",
        CHROME = "manipulation: after(String|Element|Array&lt;Element&gt;|jQuery) (0, 7, 7)",
        IE = "manipulation: after(String|Element|Array&lt;Element&gt;|jQuery) (0, 7, 7)")
    public void test_334() throws Exception {
        runTest(334);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: after(Function) (0, 7, 7)",
        FF10 = "manipulation: insertAfter(String|Element|Array&lt;Element&gt;|jQuery) (0, 4, 4)",
        CHROME = "manipulation: after(Function) (0, 7, 7)", IE = "manipulation: after(Function) (0, 7, 7)")
    public void test_335() throws Exception {
        runTest(335);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: insertAfter(String|Element|Array&lt;Element&gt;|jQuery) (0, 4, 4)",
        FF10 = "manipulation: replaceWith(String|Element|Array&lt;Element&gt;|jQuery) (0, 22, 22)",
        CHROME = "manipulation: insertAfter(String|Element|Array&lt;Element&gt;|jQuery) (0, 4, 4)",
        IE = "manipulation: insertAfter(String|Element|Array&lt;Element&gt;|jQuery) (0, 4, 4)")
    public void test_336() throws Exception {
        runTest(336);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: replaceWith(String|Element|Array&lt;Element&gt;|jQuery) (0, 22, 22)",
        FF10 = "manipulation: replaceWith(Function) (0, 23, 23)",
        CHROME = "manipulation: replaceWith(String|Element|Array&lt;Element&gt;|jQuery) (0, 22, 22)",
        IE = "manipulation: replaceWith(String|Element|Array&lt;Element&gt;|jQuery) (0, 22, 22)")
    public void test_337() throws Exception {
        runTest(337);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: replaceWith(Function) (0, 23, 23)",
        FF10 = "manipulation: replaceWith(string) for more than one element (0, 3, 3)",
        CHROME = "manipulation: replaceWith(Function) (0, 23, 23)",
        IE = "manipulation: replaceWith(Function) (0, 23, 23)")
    public void test_338() throws Exception {
        runTest(338);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: replaceWith(string) for more than one element (0, 3, 3)",
        FF10 = "manipulation: replaceAll(String|Element|Array&lt;Element&gt;|jQuery) (0, 10, 10)",
        CHROME = "manipulation: replaceWith(string) for more than one element (0, 3, 3)",
        IE = "manipulation: replaceWith(string) for more than one element (0, 3, 3)")
    public void test_339() throws Exception {
        runTest(339);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: replaceAll(String|Element|Array&lt;Element&gt;|jQuery) (0, 10, 10)",
        FF10 = "manipulation: jQuery.clone() (#8017) (0, 2, 2)",
        CHROME = "manipulation: replaceAll(String|Element|Array&lt;Element&gt;|jQuery) (0, 10, 10)",
        IE = "manipulation: replaceAll(String|Element|Array&lt;Element&gt;|jQuery) (0, 10, 10)")
    public void test_340() throws Exception {
        runTest(340);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: jQuery.clone() (#8017) (0, 2, 2)",
        FF10 = "manipulation: clone() (#8070) (0, 2, 2)", CHROME = "manipulation: jQuery.clone() (#8017) (0, 2, 2)",
        IE = "manipulation: jQuery.clone() (#8017) (0, 2, 2)")
    public void test_341() throws Exception {
        runTest(341);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: clone() (#8070) (0, 2, 2)",
        FF10 = "manipulation: clone() (0, 44, 44)", CHROME = "manipulation: clone() (#8070) (0, 2, 2)",
        IE = "manipulation: clone() (#8070) (0, 2, 2)")
    public void test_342() throws Exception {
        runTest(342);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: clone() (0, 44, 44)",
        FF10 = "manipulation: clone(script type=non-javascript) (#11359) (0, 3, 3)",
        CHROME = "manipulation: clone() (0, 44, 44)", IE = "manipulation: clone() (0, 44, 44)")
    public void test_343() throws Exception {
        runTest(343);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: clone(script type=non-javascript) (#11359) (0, 3, 3)",
        FF10 = "manipulation: clone(form element) (Bug #3879, #6655) (0, 5, 5)",
        CHROME = "manipulation: clone(script type=non-javascript) (#11359) (0, 3, 3)",
        IE = "manipulation: clone(script type=non-javascript) (#11359) (0, 3, 3)")
    public void test_344() throws Exception {
        runTest(344);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: clone(form element) (Bug #3879, #6655) (0, 5, 5)",
        FF10 = "manipulation: clone(multiple selected options) (Bug #8129) (0, 1, 1)",
        CHROME = "manipulation: clone(form element) (Bug #3879, #6655) (0, 5, 5)",
        IE = "manipulation: clone(form element) (Bug #3879, #6655) (0, 5, 5)")
    public void test_345() throws Exception {
        runTest(345);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: clone(multiple selected options) (Bug #8129) (0, 1, 1)",
        FF10 = "manipulation: clone() on XML nodes (0, 2, 2)",
        CHROME = "manipulation: clone(multiple selected options) (Bug #8129) (0, 1, 1)",
        IE = "manipulation: clone(multiple selected options) (Bug #8129) (0, 1, 1)")
    public void test_346() throws Exception {
        runTest(346);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: clone() on XML nodes (0, 2, 2)",
        FF10 = "manipulation: clone() on local XML nodes with html5 nodename (0, 2, 2)",
        CHROME = "manipulation: clone() on XML nodes (0, 2, 2)",
        IE = "manipulation: clone() on XML nodes (0, 2, 2)")
    public void test_347() throws Exception {
        runTest(347);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: clone() on local XML nodes with html5 nodename (0, 2, 2)",
        FF10 = "manipulation: html(undefined) (0, 1, 1)",
        CHROME = "manipulation: clone() on local XML nodes with html5 nodename (0, 2, 2)",
        IE = "manipulation: clone() on local XML nodes with html5 nodename (0, 2, 2)")
    public void test_348() throws Exception {
        runTest(348);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: html(undefined) (0, 1, 1)", FF10 = "manipulation: html() on empty set (0, 1, 1)",
        CHROME = "manipulation: html(undefined) (0, 1, 1)", IE = "manipulation: html(undefined) (0, 1, 1)")
    public void test_349() throws Exception {
        runTest(349);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: html() on empty set (0, 1, 1)",
        FF10 = "manipulation: html(String) (0, 35, 35)", CHROME = "manipulation: html() on empty set (0, 1, 1)",
        IE = "manipulation: html() on empty set (0, 1, 1)")
    public void test_350() throws Exception {
        runTest(350);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: html(String) (0, 35, 35)", FF10 = "manipulation: html(Function) (0, 37, 37)",
        CHROME = "manipulation: html(String) (0, 35, 35)", IE = "manipulation: html(String) (0, 35, 35)")
    public void test_351() throws Exception {
        runTest(351);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: html(Function) (0, 37, 37)",
        FF10 = "manipulation: html(Function) with incoming value (0, 20, 20)",
        CHROME = "manipulation: html(Function) (0, 37, 37)", IE = "manipulation: html(Function) (0, 37, 37)")
    public void test_352() throws Exception {
        runTest(352);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: html(Function) with incoming value (0, 20, 20)",
        FF10 = "manipulation: remove() (0, 9, 9)",
        CHROME = "manipulation: html(Function) with incoming value (0, 20, 20)",
        IE = "manipulation: html(Function) with incoming value (0, 20, 20)")
    public void test_353() throws Exception {
        runTest(353);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: remove() (0, 9, 9)", FF10 = "manipulation: detach() (0, 9, 9)",
        CHROME = "manipulation: remove() (0, 9, 9)", IE = "manipulation: remove() (0, 9, 9)")
    public void test_354() throws Exception {
        runTest(354);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: detach() (0, 9, 9)", FF10 = "manipulation: empty() (0, 3, 3)",
        CHROME = "manipulation: detach() (0, 9, 9)", IE = "manipulation: detach() (0, 9, 9)")
    public void test_355() throws Exception {
        runTest(355);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: empty() (0, 3, 3)", FF10 = "manipulation: jQuery.cleanData (0, 14, 14)",
        CHROME = "manipulation: empty() (0, 3, 3)", IE = "manipulation: empty() (0, 3, 3)")
    public void test_356() throws Exception {
        runTest(356);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: jQuery.cleanData (0, 14, 14)",
        FF10 = "manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779) (0, 1, 1)",
        CHROME = "manipulation: jQuery.cleanData (0, 14, 14)",
        IE = "manipulation: jQuery.cleanData (0, 14, 14)")
    public void test_357() throws Exception {
        runTest(357);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779) (0, 1, 1)",
        FF10 = "manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221) (0, 3, 3)",
        CHROME = "manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779) (0, 1, 1)",
        IE = "manipulation: jQuery.buildFragment - no plain-text caching (Bug #6779) (0, 1, 1)")
    public void test_358() throws Exception {
        runTest(358);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221) (0, 3, 3)",
        FF10 = "manipulation: jQuery.buildFragment - plain objects are not a document #8950 (0, 1, 1)",
        CHROME = "manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221) (0, 3, 3)",
        IE = "manipulation: jQuery.html - execute scripts escaped with html comment or CDATA (#9221) (0, 3, 3)")
    public void test_359() throws Exception {
        runTest(359);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: jQuery.buildFragment - plain objects are not a document #8950 (0, 1, 1)",
        FF10 = "manipulation: jQuery.clone - no exceptions for object elements #9587 (0, 1, 1)",
        CHROME = "manipulation: jQuery.buildFragment - plain objects are not a document #8950 (0, 1, 1)",
        IE = "manipulation: jQuery.buildFragment - plain objects are not a document #8950 (0, 1, 1)")
    public void test_360() throws Exception {
        runTest(360);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: jQuery.clone - no exceptions for object elements #9587 (0, 1, 1)",
        FF10 = "manipulation: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667) (0, 2, 2)",
        CHROME = "manipulation: jQuery.clone - no exceptions for object elements #9587 (0, 1, 1)",
        IE = "manipulation: jQuery.clone - no exceptions for object elements #9587 (0, 1, 1)")
    public void test_361() throws Exception {
        runTest(361);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667) (0, 2, 2)",
        FF10 = "manipulation: Cloned, detached HTML5 elems (#10667,10670) (0, 7, 7)",
        CHROME = "manipulation: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667) (0, 2, 2)",
        IE = "manipulation: jQuery(<tag>) & wrap[Inner/All]() handle unknown elems (#10667) (0, 2, 2)")
    public void test_362() throws Exception {
        runTest(362);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: Cloned, detached HTML5 elems (#10667,10670) (0, 7, 7)",
        FF10 = "manipulation: jQuery.fragments cache expectations (0, 10, 10)",
        CHROME = "manipulation: Cloned, detached HTML5 elems (#10667,10670) (0, 7, 7)",
        IE = "manipulation: Cloned, detached HTML5 elems (#10667,10670) (0, 7, 7)")
    public void test_363() throws Exception {
        runTest(363);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: jQuery.fragments cache expectations (0, 10, 10)",
        FF10 = "manipulation: Guard against exceptions when clearing safeChildNodes (0, 1, 1)",
        CHROME = "manipulation: jQuery.fragments cache expectations (0, 10, 10)",
        IE = "manipulation: jQuery.fragments cache expectations (0, 10, 10)")
    public void test_364() throws Exception {
        runTest(364);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: Guard against exceptions when clearing safeChildNodes (0, 1, 1)",
        FF10 = "manipulation: Ensure oldIE creates a new set on appendTo (#8894) (0, 5, 5)",
        CHROME = "manipulation: Guard against exceptions when clearing safeChildNodes (0, 1, 1)",
        IE = "manipulation: Guard against exceptions when clearing safeChildNodes (0, 1, 1)")
    public void test_365() throws Exception {
        runTest(365);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: Ensure oldIE creates a new set on appendTo (#8894) (0, 5, 5)",
        FF10 = "manipulation: html() - script exceptions bubble (#11743) (0, 2, 2)",
        CHROME = "manipulation: Ensure oldIE creates a new set on appendTo (#8894) (0, 5, 5)",
        IE = "manipulation: Ensure oldIE creates a new set on appendTo (#8894) (0, 5, 5)")
    public void test_366() throws Exception {
        runTest(366);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: html() - script exceptions bubble (#11743) (0, 2, 2)",
        FF10 = "manipulation: checked state is cloned with clone() (0, 2, 2)",
        CHROME = "manipulation: html() - script exceptions bubble (#11743) (0, 2, 2)",
        IE = "manipulation: html() - script exceptions bubble (#11743) (0, 2, 2)")
    public void test_367() throws Exception {
        runTest(367);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: checked state is cloned with clone() (0, 2, 2)",
        FF10 = "manipulation: manipulate mixed jQuery and text (#12384, #12346) (0, 2, 2)",
        CHROME = "manipulation: checked state is cloned with clone() (0, 2, 2)",
        IE = "manipulation: checked state is cloned with clone() (0, 2, 2)")
    public void test_368() throws Exception {
        runTest(368);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: manipulate mixed jQuery and text (#12384, #12346) (0, 2, 2)",
        FF10 = "manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266) "
            + "(0, 1, 1)",
        CHROME = "manipulation: manipulate mixed jQuery and text (#12384, #12346) (0, 2, 2)",
        IE = "manipulation: manipulate mixed jQuery and text (#12384, #12346) (0, 2, 2)")
    public void test_369() throws Exception {
        runTest(369);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 "
            + "(#12266) (0, 1, 1)",
        FF10 = "css: css(String|Hash) (0, 46, 46)",
        CHROME = "manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266) ("
            + "0, 1, 1)",
        IE = "manipulation: buildFragment works even if document[0] is iframe's window object in IE9/10 (#12266) ("
            + "0, 1, 1)")
    public void test_370() throws Exception {
        runTest(370);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: css(String|Hash) (0, 46, 46)", FF10 = "css: css() explicit and relative values (0, 29, 29)",
        CHROME = "css: css(String|Hash) (0, 46, 46)", IE = "css: css(String|Hash) (0, 46, 46)")
    @NotYetImplemented
    public void test_371() throws Exception {
        runTest(371);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: css() explicit and relative values (0, 29, 29)",
        FF10 = "css: css(String, Object) (0, 22, 22)", CHROME = "css: css() explicit and relative values (0, 29, 29)",
        IE = "css: css() explicit and relative values (0, 29, 29)")
    public void test_372() throws Exception {
        runTest(372);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: css(String, Object) (0, 22, 22)", FF10 = "css: css(String, Function) (0, 3, 3)",
        CHROME = "css: css(String, Object) (0, 22, 22)", IE = "css: css(String, Object) (0, 22, 22)")
    public void test_373() throws Exception {
        runTest(373);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: css(String, Function) (0, 3, 3)",
        FF10 = "css: css(String, Function) with incoming value (0, 3, 3)",
        CHROME = "css: css(String, Function) (0, 3, 3)", IE = "css: css(String, Object) for MSIE (0, 5, 5)")
    public void test_374() throws Exception {
        runTest(374);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: css(String, Function) with incoming value (0, 3, 3)",
        FF10 = "css: css(Object) where values are Functions (0, 3, 3)",
        CHROME = "css: css(String, Function) with incoming value (0, 3, 3)",
        IE = "css: Setting opacity to 1 properly removes filter: style (#6652) (0, 2, 2)")
    @NotYetImplemented(IE)
    public void test_375() throws Exception {
        runTest(375);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: css(Object) where values are Functions (0, 3, 3)",
        FF10 = "css: css(Object) where values are Functions with incoming values (0, 3, 3)",
        CHROME = "css: css(Object) where values are Functions (0, 3, 3)",
        IE = "css: css(String, Function) (0, 3, 3)")
    public void test_376() throws Exception {
        runTest(376);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: css(Object) where values are Functions with incoming values (0, 3, 3)",
        FF10 = "css: show(); hide() (0, 22, 22)",
        CHROME = "css: css(Object) where values are Functions with incoming values (0, 3, 3)",
        IE = "css: css(String, Function) with incoming value (0, 3, 3)")
    public void test_377() throws Exception {
        runTest(377);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: show(); hide() (0, 22, 22)",
        FF10 = "css: show() resolves correct default display #8099 (0, 7, 7)",
        CHROME = "css: show(); hide() (0, 22, 22)", IE = "css: css(Object) where values are Functions (0, 3, 3)")
    public void test_378() throws Exception {
        runTest(378);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: show() resolves correct default display #8099 (3, 4, 7)",
        FF10 = "css: show() resolves correct default display, detached nodes (#10006) (0, 11, 11)",
        CHROME = "css: show() resolves correct default display #8099 (0, 7, 7)",
        IE = "css: css(Object) where values are Functions with incoming values (0, 3, 3)")
    public void test_379() throws Exception {
        runTest(379);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: show() resolves correct default display, detached nodes (#10006) (0, 11, 11)",
        FF10 = "css: toggle() (0, 9, 9)",
        CHROME = "css: show() resolves correct default display, detached nodes (#10006) (0, 11, 11)",
        IE = "css: show(); hide() (0, 22, 22)")
    public void test_380() throws Exception {
        runTest(380);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: toggle() (0, 9, 9)", FF10 = "css: hide hidden elements (bug #7141) (0, 3, 3)",
        CHROME = "css: toggle() (0, 9, 9)", IE = "css: show() resolves correct default display #8099 (2, 1, 3)")
    @NotYetImplemented(IE)
    public void test_381() throws Exception {
        runTest(381);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: hide hidden elements (bug #7141) (0, 3, 3)",
        FF10 = "css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095) (0, 4, 4)",
        CHROME = "css: hide hidden elements (bug #7141) (0, 3, 3)",
        IE = "css: show() resolves correct default display, detached nodes (#10006) (0, 11, 11)")
    public void test_382() throws Exception {
        runTest(382);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095) (0, 4, 4)",
        FF10 = "css: :visible selector works properly on table elements (bug #4512) (0, 1, 1)",
        CHROME = "css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095) (0, 4, 4)",
        IE = "css: toggle() (0, 9, 9)")
    public void test_383() throws Exception {
        runTest(383);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: :visible selector works properly on table elements (bug #4512) (0, 1, 1)",
        FF10 = "css: :visible selector works properly on children with a hidden parent (bug #4512) (0, 1, 1)",
        CHROME = "css: :visible selector works properly on table elements (bug #4512) (0, 1, 1)",
        IE = "css: hide hidden elements (bug #7141) (0, 3, 3)")
    public void test_384() throws Exception {
        runTest(384);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: :visible selector works properly on children with a hidden parent (bug #4512) (0, 1, 1)",
        FF10 = "css: internal ref to elem.runtimeStyle (bug #7608) (0, 1, 1)",
        CHROME = "css: :visible selector works properly on children with a hidden parent (bug #4512) (0, 1, 1)",
        IE = "css: jQuery.css(elem, 'height') doesn't clear radio buttons (bug #1095) (0, 4, 4)")
    @NotYetImplemented(FF)
    public void test_385() throws Exception {
        runTest(385);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: internal ref to elem.runtimeStyle (bug #7608) (0, 1, 1)",
        FF10 = "css: marginRight computed style (bug #3333) (0, 1, 1)",
        CHROME = "css: internal ref to elem.runtimeStyle (bug #7608) (0, 1, 1)",
        IE = "css: :visible selector works properly on table elements (bug #4512) (0, 1, 1)")
    public void test_386() throws Exception {
        runTest(386);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: marginRight computed style (bug #3333) (0, 1, 1)",
        FF10 = "css: box model properties incorrectly returning % instead of px, see #10639 and #12088 (0, 2, 2)",
        CHROME = "css: marginRight computed style (bug #3333) (0, 1, 1)",
        IE = "css: :visible selector works properly on children with a hidden parent (bug #4512) (0, 1, 1)")
    @NotYetImplemented(IE)
    public void test_387() throws Exception {
        runTest(387);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: box model properties incorrectly returning % instead of px, see #10639 and #12088 (0, 2, 2)",
        FF10 = "css: jQuery.cssProps behavior, (bug #8402) (0, 2, 2)",
        CHROME = "css: box model properties incorrectly returning % instead of px, see #10639 and #12088 (0, 2, 2)",
        IE = "css: internal ref to elem.runtimeStyle (bug #7608) (0, 1, 1)")
    @NotYetImplemented(FF)
    public void test_388() throws Exception {
        runTest(388);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: jQuery.cssProps behavior, (bug #8402) (0, 2, 2)",
        FF10 = "css: widows & orphans #8936 (0, 4, 4)",
        CHROME = "css: jQuery.cssProps behavior, (bug #8402) (0, 2, 2)",
        IE = "css: marginRight computed style (bug #3333) (0, 1, 1)")
    public void test_389() throws Exception {
        runTest(389);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: widows & orphans #8936 (0, 4, 4)",
        FF10 = "css: can't get css for disconnected in IE<9, see #10254 and #8388 (0, 2, 2)",
        CHROME = "css: widows & orphans #8936 (0, 4, 4)",
        IE = "css: box model properties incorrectly returning % instead of px, see #10639 and #12088 (0, 2, 2)")
    @NotYetImplemented(IE)
    public void test_390() throws Exception {
        runTest(390);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: can't get css for disconnected in IE<9, see #10254 and #8388 (0, 2, 2)",
        FF10 = "css: can't get background-position in IE<9, see #10796 (0, 8, 8)",
        CHROME = "css: can't get css for disconnected in IE<9, see #10254 and #8388 (0, 2, 2)",
        IE = "css: jQuery.cssProps behavior, (bug #8402) (0, 2, 2)")
    public void test_391() throws Exception {
        runTest(391);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: can't get background-position in IE<9, see #10796 (0, 8, 8)",
        FF10 = "css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pix"
            + "els, see #11311 (0, 1, 1)",
        CHROME = "css: can't get background-position in IE<9, see #10796 (0, 8, 8)",
        IE = "css: widows & orphans #8936 (0, 4, 4)")
    @NotYetImplemented({ IE6, IE7 })
    public void test_392() throws Exception {
        runTest(392);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed "
            + "to pixels, see #11311 (0, 1, 1)",
        FF10 = "css: percentage properties for left and top should be transformed to pixels, see #9505 (0, 2, 2)",
        CHROME = "css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed "
            + "to pixels, see #11311 (0, 1, 1)",
        IE = "css: can't get css for disconnected in IE<9, see #10254 and #8388 (0, 2, 2)")
    public void test_393() throws Exception {
        runTest(393);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: percentage properties for left and top should be transformed to pixels, see #9505 (0, 2, 2)",
        FF10 = "css: Do not append px to 'fill-opacity' #9548 (0, 1, 1)",
        CHROME = "css: percentage properties for left and top should be transformed to pixels, see #9505 (0, 2, 2)",
        IE = "css: can't get background-position in IE<9, see #10796 (0, 8, 8)")
    @NotYetImplemented(FF)
    public void test_394() throws Exception {
        runTest(394);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: Do not append px to 'fill-opacity' #9548 (1, 0, 1)",
        FF10 = "css: css('width') and css('height') should respect box-sizing, see #11004 (0, 4, 4)",
        CHROME = "css: Do not append px to 'fill-opacity' #9548 (0, 1, 1)",
        IE = "css: percentage properties for bottom and right in IE<9 should not be incorrectly transformed to pix"
            + "els, see #11311 (0, 1, 1)")
    public void test_395() throws Exception {
        runTest(395);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: css('width') and css('height') should respect box-sizing, see #11004 (0, 4, 4)",
        FF10 = "css: certain css values of 'normal' should be convertable to a number, see #8627 (0, 2, 2)",
        CHROME = "css: css('width') and css('height') should respect box-sizing, see #11004 (0, 4, 4)",
        IE = "css: percentage properties for left and top should be transformed to pixels, see #9505 (0, 2, 2)")
    @NotYetImplemented(IE)
    public void test_396() throws Exception {
        runTest(396);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: certain css values of 'normal' should be convertable to a number, see #8627 (0, 2, 2)",
        FF10 = "css: cssHooks - expand (0, 15, 15)",
        CHROME = "css: certain css values of 'normal' should be convertable to a number, see #8627 (0, 2, 2)",
        IE = "css: Do not append px to 'fill-opacity' #9548 (0, 1, 1)")
    public void test_397() throws Exception {
        runTest(397);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "css: cssHooks - expand (0, 15, 15)", FF10 = "serialize: jQuery.param() (0, 22, 22)",
        CHROME = "css: cssHooks - expand (0, 15, 15)",
        IE = "css: css('width') and css('height') should respect box-sizing, see #11004 (0, 4, 4)")
    public void test_398() throws Exception {
        runTest(398);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "serialize: jQuery.param() (0, 22, 22)",
        FF10 = "serialize: jQuery.param() Constructed prop values (0, 4, 4)",
        CHROME = "serialize: jQuery.param() (0, 22, 22)",
        IE = "css: certain css values of 'normal' should be convertable to a number, see #8627 (0, 2, 2)")
    public void test_399() throws Exception {
        runTest(399);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = "serialize: jQuery.param() Constructed prop values (0, 4, 4)",
        FF10 = "serialize: serialize() (0, 5, 5)",
        CHROME = "serialize: jQuery.param() Constructed prop values (0, 4, 4)",
        IE = "css: cssHooks - expand (0, 15, 15)")
    public void test_400() throws Exception {
        runTest(400);
    }

}
