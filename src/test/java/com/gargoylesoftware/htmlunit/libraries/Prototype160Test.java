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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebServerTestCase;

/**
 * Tests for compatibility with version 1.6.0 of
 * <a href="http://www.prototypejs.org/">Prototype JavaScript library</a>.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Prototype160Test extends PrototypeTestBase {

    /**
     * @throws Exception if an error occurs
     */
    @BeforeClass
    public static void aaa_startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/prototype/1.6.0/", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getVersion() {
        return "1.6.0";
    }

    /**
     * @return the resource base url
     */
    protected String getBaseUrl() {
        return "http://localhost:" + PORT + "/test/unit/";
    }

    @Override
    protected boolean testFinished(final WebDriver driver) {
        final List<WebElement> status = driver.findElements(By.cssSelector("div#logsummary"));
        for (WebElement webElement : status) {
            if (!webElement.getText().contains("errors")) {
                return false;
            }
        }
        return true;
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void ajax() throws Exception {
        test("ajax.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void array() throws Exception {
        test("array.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void base() throws Exception {
        test("base.html");
    }

    /**
     * Note: <tt>testElementGetDimensions:</tt>, <tt>testElementGetStyle</tt>, <tt>testElementGetHeight</tt>,
     *       <tt>testElementScrollTo:</tt>, <tt>testPositionedOffset</tt>, <tt>testViewportOffset</tt>,
     *       <tt>testViewportDimensions</tt>, <tt>testViewportScrollOffsets</tt>
     *       and <tt>testElementGetWidth</tt> are expected to fail with HtmlUnit,
     *       as they need calculating width and height of all elements.
     *
     * Other tests succeed.
     *
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void dom() throws Exception {
        test("dom.html");
    }

    /**
     * Depends on {@link com.gargoylesoftware.htmlunit.javascript.SimpleScriptableTest#parentProtoFeature()}.
     *
     * @throws Exception if test fails
     */
    @Test
    public void elementMixins() throws Exception {
        test("element_mixins.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void enumerable() throws Exception {
        test("enumerable.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE11)
    public void event() throws Exception {
        test("event.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented({ FF, IE11, CHROME })
    public void form() throws Exception {
        test("form.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void hash() throws Exception {
        test("hash.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void number() throws Exception {
        test("number.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented
    public void position() throws Exception {
        test("position.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void range() throws Exception {
        test("range.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void selector() throws Exception {
        test("selector.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    public void string() throws Exception {
        test("string.html");
    }

    /**
     * 1 expected failure is because the server port is other than 4711.
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented({ FF, IE11, CHROME })
    public void unitTests() throws Exception {
        test("unit_tests.html");
    }
}
