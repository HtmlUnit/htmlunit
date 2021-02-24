/*
 * Copyright (c) 2002-2021 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.libraries;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for 0.9.9.3 version of <a href="http://sarissa.sourceforge.net">Sarissa</a>.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Sarissa0993Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(CHROME = {"+++++++++++FF+++++", "+", "+", "+", "F++", "++", "++++F+++"},
            EDGE = {"+++++++++++FF+++++", "+", "+", "+", "F++", "++", "++++F+++"},
            FF = {"+++++++++++F++++++", "+", "+", "+", "F++", "++", "++++F+++"},
            FF78 = {"+++++++++++F++++++", "+", "+", "+", "F++", "++", "++++F+++"},
            IE = {"+++++++++++F++++++", "+", "+", "+", "FFF", "FF", "FFFFFFFF"})
    // TODO [IE]XML sarissa 0.9.9.3 is not compatible with IE's new XML stuff
    public void sarissa() throws Exception {
        startWebServer("src/test/resources/libraries/sarissa/0.9.9.3", null, null);
        final String url = URL_FIRST + "test/testsarissa.html";

        final WebDriver driver = getWebDriver();
        driver.get(url);

        driver.findElement(By.xpath("//button")).click();

        driver.switchTo().alert().dismiss();

        verify(driver, "SarissaTestCase", getExpectedAlerts()[0]);
        verify(driver, "XmlHttpRequestTestCase", getExpectedAlerts()[1]);
        verify(driver, "XMLSerializerTestCase", getExpectedAlerts()[2]);
        verify(driver, "DOMParserTestCase", getExpectedAlerts()[3]);
        verify(driver, "XMLDocumentTestCase", getExpectedAlerts()[4]);
        verify(driver, "XMLElementTestCase", getExpectedAlerts()[5]);
        verify(driver, "XSLTProcessorTestCase", getExpectedAlerts()[6]);
    }

    /**
     * @param expectedResult empty for successful test or in the form of "+++F+++"
     * for failing tests (see the results in a real browser)
     */
    private static void verify(final WebDriver driver, final String testName, final String expectedResult) {
        final WebElement div =
            driver.findElement(By.xpath("//div[@class='placeholder' and a[@name='#" + testName + "']]"));

        String text = div.getText();
        text = text.substring(0, text.indexOf(String.valueOf(expectedResult.length()))).trim();
        assertEquals(testName + " Results\n" + expectedResult, text);
    }
}
