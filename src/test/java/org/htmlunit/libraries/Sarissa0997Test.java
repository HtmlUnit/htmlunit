/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.libraries;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Tests for 0.9.9.3 version of <a href="http://sarissa.sourceforge.net">Sarissa</a>.
 *
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Frank Danek
 * @author Ronald Brill
 */
public class Sarissa0997Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"++++++++++++++++++", "+", "+", "+", "+++", "++", "++++++++"})
    public void sarissa() throws Exception {
        startWebServer("src/test/resources/libraries/sarissa/0.9.9.7", null, null);
        final String url = URL_FIRST + "test/testsarissa.html";

        final WebDriver driver = getWebDriver();
        driver.get(url);

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
     *        for failing tests (see the results in a real browser)
     */
    private static void verify(final WebDriver driver, final String testName, final String expectedResult) {
        final WebElement div =
            driver.findElement(By.xpath("//div[@class='placeholder' and a[@name='#" + testName + "']]"));

        String text = div.getText();
        text = text.substring(0, text.indexOf(String.valueOf(expectedResult.length()))).trim();
        assertEquals(testName + " Results\n" + expectedResult, text);
    }
}
