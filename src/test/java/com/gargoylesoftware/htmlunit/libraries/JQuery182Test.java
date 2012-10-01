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

import static org.junit.Assert.fail;

import org.eclipse.jetty.server.Server;
import org.junit.After;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for compatibility with web server loading of
 * version 1.8.2 of the <a href="http://jquery.com/">jQuery</a> JavaScript library.
 *
 * @version $Revision$
 * @author Ahmed Ashour
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
        //nothing
    }

    /**
     * Runs the specified test.
     * @param testNumber the test number
     * @throws Exception if an error occurs
     */
    protected void runTest(final int testNumber) throws Exception {
        final long endTime = System.currentTimeMillis() + 60 * 1000;
        try {
            WEBDRIVER_.get("http://localhost:" + PORT + "/jquery/test/index.html?testNumber="
                    + testNumber);
            WebElement element = null;
            do {
                if (element == null) {
                    try {
                        element = WEBDRIVER_.findElement(By.id("qunit-test-output0"));
                    }
                    catch (final Exception e) {
                        //ignore
                    }
                }
                Thread.sleep(100);
            } while (System.currentTimeMillis() < endTime && (element == null || !element.getText().contains(",")));
            String result = element.getText();
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

}
