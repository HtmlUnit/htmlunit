/*
 * Copyright (c) 2002-2020 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.html;

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlLabel}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlLabelTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLLabelElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<label id='myId'>Item</label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlLabel.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNone() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item</label>\n"
            + "  <input type='text' id='text1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getReferencedElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForEmpty() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for=''>Item</label>\n"
            + "  <input type='text' id='text1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getReferencedElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForUnknown() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='unknwon'>Item</label>\n"
            + "  <input type='text' id='text1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getReferencedElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForNotLabelable() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='div1'>Item</label>\n"
            + "  <div id='div1'></div>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getReferencedElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForButton() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='button1'>Item</label>\n"
            + "  <button id='button1'></button>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("button1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForInput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='text1'>Item</label>\n"
            + "  <input type='text' id='text1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForInputHidden() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='hidden1'>Item</label>\n"
            + "  <input type='hidden' id='hidden1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getReferencedElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForMeter() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='meter1'>Item</label>\n"
            + "  <meter id='meter1'></meter>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            if (getBrowserVersion().isIE()) {
                assertNull(label.getReferencedElement());
            }
            else {
                assertEquals("meter1", label.getReferencedElement().getId());
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForOutput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='output1'>Item</label>\n"
            + "  <output id='output1'></output>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            if (getBrowserVersion().isIE()) {
                assertNull(label.getReferencedElement());
            }
            else {
                assertEquals("output1", label.getReferencedElement().getId());
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForProgress() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='progress1'>Item</label>\n"
            + "  <progress id='progress1'></progress>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("progress1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForSelect() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='select1'>Item</label>\n"
            + "  <select id='select1'><option>Option</option></select>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("select1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForTextArea() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='text1'>Item</label>\n"
            + "  <textarea id='text1'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNestedNotLabelable() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <div id='div1'></div>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getReferencedElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNestedButton() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <button id='button1'></button>\n"
            + "    <button id='button2'></button>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("button1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNestedInput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "    <input type='text' id='text2'>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNestedInputHidden() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <input type='hidden' id='hidden1'>\n"
            + "    <input type='hidden' id='hidden2'>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getReferencedElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNestedMeter() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <meter id='meter1'></meter>\n"
            + "    <meter id='meter2'></meter>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            if (getBrowserVersion().isIE()) {
                assertNull(label.getReferencedElement());
            }
            else {
                assertEquals("meter1", label.getReferencedElement().getId());
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNestedOutput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <output id='output1'></output>\n"
            + "    <output id='output2'></output>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            if (getBrowserVersion().isIE()) {
                assertNull(label.getReferencedElement());
            }
            else {
                assertEquals("output1", label.getReferencedElement().getId());
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNestedProgress() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <progress id='progress1'></progress>\n"
            + "    <progress id='progress2'></progress>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("progress1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNestedSelect() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <select id='select1'><option>Option</option></select>\n"
            + "    <select id='select2'><option>Option</option></select>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("select1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementNestedTextArea() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <textarea id='text1'></textarea>\n"
            + "    <textarea id='text2'></textarea>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text1", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForVersusNested() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='text2'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text2", label.getReferencedElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForUnknownVersusNested() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='unknown'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getReferencedElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getReferencedElementForNotLabelableVersusNested() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='div1'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <div id='div1'></div>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getReferencedElement());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickNone() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' onclick='alert(\"labelclick\")'>Click me</label>\n"
            + "  <input type='text' id='text1' onclick='alert(\"textclick\")'>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickForEmpty() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='' onclick='alert(\"labelclick\")'>Click me</label>\n"
            + "  <input type='text' id='text1' onclick='alert(\"textclick\")'>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickForUnknown() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='unknown' onclick='alert(\"labelclick\")'>Click me</label>\n"
            + "  <input type='text' id='text1' onclick='alert(\"textclick\")'>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickForNotLabelable() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='div1' onclick='alert(\"labelclick\")'>Click me</label>\n"
            + "  <div id='div1' onclick='alert(\"textclick\")'></div>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "textclick"})
    public void clickForInput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='text1' onclick='alert(\"labelclick\")'>Click me</label>\n"
            + "  <input type='text' id='text1' onclick='alert(\"textclick\")'>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "textclick"})
    public void clickForTextArea() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='text1' onclick='alert(\"labelclick\")'>Click me</label>\n"
            + "  <textarea id='text1' onclick='alert(\"textclick\")'></textarea>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "selectclick"})
    public void clickForSelect() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='select1' onclick='alert(\"labelclick\")'>Click me</label>\n"
            + "  <select id='select1' onclick='alert(\"selectclick\")'><option>Option</option></select>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickNestedNotLabelable() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' onclick='alert(\"labelclick\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <div id='div1' onclick='alert(\"textclick\")'></div>\n"
            + "  </label>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "text1click"})
    public void clickNestedInput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' onclick='alert(\"labelclick\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='alert(\"text1click\")'>\n"
            + "    <input type='text' id='text2' onclick='alert(\"text2click\")'>\n"
            + "  </label>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "text1click"})
    public void clickNestedTextArea() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' onclick='alert(\"labelclick\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <textarea id='text1' onclick='alert(\"text1click\")'></textarea>\n"
            + "    <textarea id='text2' onclick='alert(\"text2click\")'></textarea>\n"
            + "  </label>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "select1click"})
    public void clickNestedSelect() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' onclick='alert(\"labelclick\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <select id='select1' onclick='alert(\"select1click\")'><option>Option</option></select>\n"
            + "    <select id='select2' onclick='alert(\"select2click\")'><option>Option</option></select>\n"
            + "  </label>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "text2click"})
    public void clickForVersusNested() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='text2' onclick='alert(\"labelclick\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='alert(\"text1click\")'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2' onclick='alert(\"text2click\")'>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickForUnknownVersusNested() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='unknown' onclick='alert(\"labelclick\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='alert(\"text1click\")'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2' onclick='alert(\"text2click\")'>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickForNotLabelableVersusNested() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='div1' onclick='alert(\"labelclick\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='alert(\"text1click\")'>\n"
            + "  </label>\n"
            + "  <div id='div1' onclick='alert(\"textclick\")'></div>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "labelclick,textfocus,textclick,",
            IE = "labelclick,textclick,textfocus,")
    @NotYetImplemented(IE)
    public void clickForSetFocusToInput() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <label id='label1' for='text1' "
                    + "onclick='debug(\"labelclick\")' onfocus='debug(\"labelfocus\")'>Focus input</label>\n"
            + "  <input type='text' id='text1' onclick='debug(\"textclick\")' onfocus='debug(\"textfocus\")'>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick,")
    public void clickForSetFocusToDisabledInput() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <label id='label1' for='text1' "
                    + "onclick='debug(\"labelclick\")' onfocus='debug(\"labelfocus\")'>Focus input</label>\n"
            + "  <input type='text' id='text1' disabled='disabled' "
                    + "onclick='debug(\"textclick\")' onfocus='debug(\"textfocus\")'>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick,")
    public void clickForSetFocusToDisabledCheckbox() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "  <label id='label1' for='check1' "
                    + "onclick='debug(\"labelclick\")' onfocus='debug(\"labelfocus\")'>Focus input</label>\n"
            + "  <input type='checkbox' id='check1' disabled='disabled' "
                    + "onclick='debug(\"checkclick\")' onfocus='debug(\"checkfocus\")'>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("label1")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click listItem1", "click list"})
    public void triggerRadio() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>Radio 1</label>\n"
            + "    <input id='radio1' name='radios' value='1' type='radio' "
                        + "onclick='alert(\"click radio1\");'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label", "click listItem1", "click list"})
    public void triggerRadioInside() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' onclick='alert(\"click radio1Label\")'>Radio 1\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' "
                          + "onclick='alert(\"click radio1\");'>\n"
            + "    </label>"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label", "click listItem1", "click list"})
    public void triggerRadioForAndInside() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>Radio 1\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' "
                          + "onclick='alert(\"click radio1\");'>\n"
            + "    </label>"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click listItem1", "click list"})
    public void triggerRadioChecked() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>Radio 1</label>\n"
            + "    <input id='radio1' name='radios' value='1' type='radio' checked "
                        + "onclick='alert(\"click radio1\");'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label", "click listItem1", "click list"})
    public void triggerRadioCheckedInside() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' onclick='alert(\"click radio1Label\")'>Radio 1\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' checked "
                          + "onclick='alert(\"click radio1\");'>\n"
            + "    </label>"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label", "click listItem1", "click list"})
    public void triggerRadioCheckedForAndInside() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>Radio 1\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' checked "
                          + "onclick='alert(\"click radio1\");'>\n"
            + "    </label>"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click listItem1", "click list"})
    public void triggerRadioInvisible() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>Radio 1</label>\n"
            + "    <input id='radio1' name='radios' value='1' type='radio' style='display: none;' "
                        + "onclick='alert(\"click radio1\");'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click listItem1", "click list"})
    public void triggerRadioHidden() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>Radio 1</label>\n"
            + "    <input id='radio1' name='radios' value='1' type='radio' hidden "
                        + "onclick='alert(\"click radio1\");'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list"})
    public void triggerRadioComplexCase() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' "
                            + "onclick='alert(\"click radio1\");'>\n"
            + "        <span id='radio1Span' onclick='alert(\"click span1\")'>Radio 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list"})
    public void triggerRadioComplexCaseChecked() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' checked "
                            + "onclick='alert(\"click radio1\");'>\n"
            + "        <span id='radio1Span' onclick='alert(\"click span1\")'>Radio 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list"})
    public void triggerRadioComplexCaseInvisible() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' style='display: none;' "
                            + "onclick='alert(\"click radio1\");'>\n"
            + "        <span id='radio1Span' onclick='alert(\"click span1\")'>Radio 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list"})
    public void triggerRadioComplexCaseHidden() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' hidden "
                            + "onclick='alert(\"click radio1\");'>\n"
            + "        <span id='radio1Span' onclick='alert(\"click span1\")'>Radio 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list"})
    public void triggerRadioComplexCaseDisabled() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' disabled "
                            + "onclick='alert(\"click radio1\");'>\n"
            + "        <span id='radio1Span' onclick='alert(\"click span1\")'>Radio 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "false");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list"})
    public void triggerRadioComplexCaseChildOfLabel() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>Label\n"
            + "      <input id='radio1' name='radios' value='1' type='radio'"
                          + "onclick='alert(\"click radio1\");'>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1", "click radio1Label", "click listItem1", "click list"})
    public void triggerRadioComplexCaseClickRadio() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='radio1Label' for='radio1' onclick='alert(\"click radio1Label\")'>Label\n"
            + "      <input id='radio1' name='radios' value='1' type='radio'"
                          + "onclick='alert(\"click radio1\");'>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"radio1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click listItem1", "click list"})
    public void triggerCheckbox() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>Checkbox 1</label>\n"
            + "    <input id='check1' name='checks' value='1' type='checkbox' "
                        + "onclick='alert(\"click check1\");'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label", "click listItem1", "click list"})
    public void triggerCheckboxInside() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' onclick='alert(\"click check1Label\")'>Checkbox 1\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' "
                          + "onclick='alert(\"click check1\");'>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label", "click listItem1", "click list"})
    public void triggerCheckboxForAndInside() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>Checkbox 1\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' "
                          + "onclick='alert(\"click check1\");'>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click listItem1", "click list"})
    public void triggerCheckboxChecked() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>Checkbox 1</label>\n"
            + "    <input id='check1' name='checks' value='1' type='checkbox' checked "
                        + "onclick='alert(\"click check1\");'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "false");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label", "click listItem1", "click list"})
    public void triggerCheckboxCheckedInside() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' onclick='alert(\"click check1Label\")'>Checkbox 1\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' checked "
                          + "onclick='alert(\"click check1\");'>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "false");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label", "click listItem1", "click list"})
    public void triggerCheckboxCheckedForAndInside() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>Checkbox 1\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' checked "
                          + "onclick='alert(\"click check1\");'>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "false");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click listItem1", "click list"})
    public void triggerCheckboxInvisible() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>Checkbox 1</label>\n"
            + "    <input id='check1' name='checks' value='1' type='checkbox' style='display: none;' "
                        + "onclick='alert(\"click check1\");'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click listItem1", "click list"})
    public void triggerCheckboxHidden() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>Checkbox 1</label>\n"
            + "    <input id='check1' name='checks' value='1' type='checkbox' hidden "
                        + "onclick='alert(\"click check1\");'>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list"})
    public void triggerCheckboxComplexCase() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox' "
                            + "onclick='alert(\"click check1\");'>\n"
            + "        <span id='check1Span' onclick='alert(\"click span1\")'>Checkbox 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list"})
    public void triggerCheckboxComplexCaseChecked() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox' checked "
                            + "onclick='alert(\"click check1\");'>\n"
            + "        <span id='check1Span' onclick='alert(\"click span1\")'>Checkbox 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "false");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list"})
    public void triggerCheckboxComplexCaseInvisible() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox' style='display: none;' "
                            + "onclick='alert(\"click check1\");'>\n"
            + "        <span id='check1Span' onclick='alert(\"click span1\")'>Checkbox 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list"})
    public void triggerCheckboxComplexCaseHidden() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox' hidden "
                            + "onclick='alert(\"click check1\");'>\n"
            + "        <span id='check1Span' onclick='alert(\"click span1\")'>Checkbox 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list"})
    public void triggerCheckboxComplexCaseDisabled() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>\n"
            + "      <span>\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox' disabled "
                            + "onclick='alert(\"click check1\");'>\n"
            + "        <span id='check1Span' onclick='alert(\"click span1\")'>Checkbox 1</span>\n"
            + "      </span>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Span")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "false");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list"})
    public void triggerCheckboxComplexCaseChildOfLabel() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>Label\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox'"
                          + "onclick='alert(\"click check1\");'>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1Label")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1", "click check1Label", "click listItem1", "click list"})
    public void triggerCheckboxComplexCaseClickCheckbox() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <ul onclick='alert(\"click list\")'>\n"
            + "  <li onclick='alert(\"click listItem1\")'>\n"
            + "    <label id='check1Label' for='check1' onclick='alert(\"click check1Label\")'>Label\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox'"
                          + "onclick='alert(\"click check1\");'>\n"
            + "    </label>\n"
            + "  </li>\n"
            + "</ul>\n"
            + "<button id='check' onclick='alert(document.getElementById(\"check1\").checked)'>Check</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("check1")).click();
        verifyAlerts(driver, getExpectedAlerts());

        driver.findElement(By.id("check")).click();
        verifyAlerts(driver, "true");
    }
}
