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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE8;

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
 * Tests for compatibility with version 1.5.0-rc1 of
 * <a href="http://prototype.conio.net/">Prototype JavaScript library</a>.
 *
 * @version $Revision$
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Prototype150rc1Test extends PrototypeTestBase {

    /**
     * @throws Exception if an error occurs
     */
    @BeforeClass
    public static void aaa_startSesrver() throws Exception {
        SERVER_ = WebServerTestCase.createWebServer("src/test/resources/libraries/prototype/1.5.0-rc1/", null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getVersion() {
        return "1.5.0-rc1";
    }

    /**
     * @return the resource base url
     */
    protected String getBaseUrl() {
        return "http://localhost:" + PORT + "/test/unit/";
    }

    @Override
    protected WebElement getSummaryElement(final WebDriver driver) {
        final WebElement status = driver.findElement(By.cssSelector("div#logsummary"));
        return status;
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE11)
    public void ajax() throws Exception {
        test("ajax.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE11)
    public void array() throws Exception {
        test("array.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE11)
    public void base() throws Exception {
        test("base.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented({ IE8, IE11 })
    public void dom() throws Exception {
        test("dom.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE11)
    public void elementMixins() throws Exception {
        test("element_mixins.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE11)
    public void enumerable() throws Exception {
        test("enumerable.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE11)
    public void form() throws Exception {
        test("form.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE11)
    public void hash() throws Exception {
        test("hash.html");
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
    @NotYetImplemented(IE11)
    public void selector() throws Exception {
        test("selector.html");
    }

    /**
     * @throws Exception if test fails
     */
    @Test
    @NotYetImplemented(IE11)
    public void string() throws Exception {
        test("string.html");
    }
}
