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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
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
    public void getLabeledElementNone() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item</label>\n"
            + "  <input type='text' id='text1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getLabeledElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForEmpty() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for=''>Item</label>\n"
            + "  <input type='text' id='text1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getLabeledElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForUnknown() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='unknown'>Item</label>\n"
            + "  <input type='text' id='text1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getLabeledElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForNotLabelable() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='div1'>Item</label>\n"
            + "  <div id='div1'></div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getLabeledElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForButton() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='button1'>Item</label>\n"
            + "  <button id='button1'></button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("button1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForInput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='text1'>Item</label>\n"
            + "  <input type='text' id='text1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForInputHidden() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='hidden1'>Item</label>\n"
            + "  <input type='hidden' id='hidden1'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getLabeledElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForMeter() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='meter1'>Item</label>\n"
            + "  <meter id='meter1'></meter>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            if (getBrowserVersion().isIE()) {
                assertNull(label.getLabeledElement());
            }
            else {
                assertEquals("meter1", label.getLabeledElement().getId());
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForOutput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='output1'>Item</label>\n"
            + "  <output id='output1'></output>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            if (getBrowserVersion().isIE()) {
                assertNull(label.getLabeledElement());
            }
            else {
                assertEquals("output1", label.getLabeledElement().getId());
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForProgress() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='progress1'>Item</label>\n"
            + "  <progress id='progress1'></progress>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("progress1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForSelect() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='select1'>Item</label>\n"
            + "  <select id='select1'><option>Option</option></select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("select1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForTextArea() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='text1'>Item</label>\n"
            + "  <textarea id='text1'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementNestedNotLabelable() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <div id='div1'></div>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getLabeledElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementNestedButton() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <button id='button1'></button>\n"
            + "    <button id='button2'></button>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("button1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementNestedInput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "    <input type='text' id='text2'>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementNestedInputHidden() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <input type='hidden' id='hidden1'>\n"
            + "    <input type='hidden' id='hidden2'>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getLabeledElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementNestedMeter() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <meter id='meter1'></meter>\n"
            + "    <meter id='meter2'></meter>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            if (getBrowserVersion().isIE()) {
                assertNull(label.getLabeledElement());
            }
            else {
                assertEquals("meter1", label.getLabeledElement().getId());
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementNestedOutput() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <output id='output1'></output>\n"
            + "    <output id='output2'></output>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            if (getBrowserVersion().isIE()) {
                assertNull(label.getLabeledElement());
            }
            else {
                assertEquals("output1", label.getLabeledElement().getId());
            }
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementNestedProgress() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <progress id='progress1'></progress>\n"
            + "    <progress id='progress2'></progress>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("progress1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementNestedSelect() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <select id='select1'><option>Option</option></select>\n"
            + "    <select id='select2'><option>Option</option></select>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("select1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementNestedTextArea() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1'>Item\n"
            + "    <textarea id='text1'></textarea>\n"
            + "    <textarea id='text2'></textarea>\n"
            + "  </label>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text1", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForVersusNested() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='text2'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertEquals("text2", label.getLabeledElement().getId());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForUnknownVersusNested() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='unknown'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getLabeledElement());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getLabeledElementForNotLabelableVersusNested() throws Exception {
        final String html = "<html>\n"
            + "<body>\n"
            + "  <label id='label1' for='div1'>Item\n"
            + "    <input type='text' id='text1'>\n"
            + "  </label>\n"
            + "  <div id='div1'></div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            final HtmlLabel label = page.getHtmlElementById("label1");
            assertNull(label.getLabeledElement());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickNone() throws Exception {
        final String html =
              "  <label id='label1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me</label>\n"
            + "  <input type='text' id='text1' onclick='log(\"textclick\")' onfocus='log(\"textfocus\")'>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick:label1", "parentclick:label1"})
    public void clickNoneEventBubbling() throws Exception {
        final String html =
              "  <div onclick='log(\"parentclick:\" + event.target.id)' "
                      + "onfocus='log(\"parentfocus:\" + event.target.id)'>\n"
              + "    <label id='label1' onclick='log(\"labelclick:\" + event.target.id)' "
                      + "onfocus='log(\"labelfocus:\" + event.target.id)'>Click me</label>\n"
              + "    <input type='text' id='text1' onclick='log(\"textclick:\" + event.target.id)' "
                      + "onfocus='log(\"textfocus:\" + event.target.id)'>\n"
              + "  </div>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickForEmpty() throws Exception {
        final String html =
              "  <label id='label1' for='' onclick='log(\"labelclick\")' "
                      + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <input type='text' id='text1' onclick='log(\"textclick\")' onfocus='log(\"textfocus\")'>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickForUnknown() throws Exception {
        final String html =
              "  <label id='label1' for='unknown' onclick='log(\"labelclick\")' "
                          + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <input type='text' id='text1' onclick='log(\"textclick\")' "
                          + "onfocus='log(\"textfocus\")'>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "labelclick",
            IE = "labelclick;divclick")
    @HtmlUnitNYI(IE = "labelclick")
    public void clickForNotLabelable() throws Exception {
        final String html =
              "  <label id='label1' for='div1' onclick='log(\"labelclick\")' "
                          + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <div id='div1' onclick='log(\"divclick\")' "
                          + "onfocus='log(\"divfocus\")'></div>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick", "textfocus", "textclick"},
            IE = {"labelclick", "textclick", "textfocus"})
    @HtmlUnitNYI(IE = {"labelclick", "textfocus", "textclick"})
    public void clickForInput() throws Exception {
        final String html =
              "  <label id='label1' for='text1' onclick='log(\"labelclick\")' "
                          + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <input type='text' id='text1' onclick='log(\"textclick\")' "
                          + "onfocus='log(\"textfocus\")'>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("text1")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick", "selectfocus", "selectclick"},
            IE = {"labelclick", "selectclick", "selectfocus"})
    @HtmlUnitNYI(IE = {"labelclick", "selectfocus", "selectclick"})
    public void clickForSelect() throws Exception {
        final String html =
              "  <label id='label1' for='select1' onclick='log(\"labelclick\")' "
                          + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <select id='select1' onclick='log(\"selectclick\")' "
                          + "onfocus='log(\"selectfocus\")'><option>Option</option></select>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("select1")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick", "textfocus", "textclick"},
            IE = {"labelclick", "textclick", "textfocus"})
    @HtmlUnitNYI(IE = {"labelclick", "textfocus", "textclick"})
    public void clickForTextArea() throws Exception {
        final String html =
              "  <label id='label1' for='text1' onclick='log(\"labelclick\")' "
                          + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <textarea id='text1' onclick='log(\"textclick\")' "
                          + "onfocus='log(\"textfocus\")'></textarea>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("text1")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickForDisabled() throws Exception {
        final String html =
              "  <label id='label1' for='text1' onclick='log(\"labelclick\")' "
                          + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <input type='text' id='text1' onclick='log(\"textclick\")' "
                          + "onfocus='log(\"textfocus\")' disabled>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "textclick"})
    public void clickForNotDisplayed() throws Exception {
        final String html =
              "  <label id='label1' for='text1' onclick='log(\"labelclick\")' "
                          + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <input type='text' id='text1' onclick='log(\"textclick\")' "
                          + "onfocus='log(\"textfocus\")' style='display: none;'>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "textclick"})
    public void clickForNotVisible() throws Exception {
        final String html =
              "  <label id='label1' for='text1' onclick='log(\"labelclick\")' "
                          + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <input type='text' id='text1' onclick='log(\"textclick\")' "
                          + "onfocus='log(\"textfocus\")' style='visibility: hidden;'>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "textclick"})
    public void clickForHidden() throws Exception {
        final String html =
              "  <label id='label1' for='text1' onclick='log(\"labelclick\")' "
                          + "onfocus='log(\"labelfocus\")'>Click me</label>\n"
              + "  <input type='text' id='text1' onclick='log(\"textclick\")' "
                          + "onfocus='log(\"textfocus\")' hidden>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick:label1", "parentclick:label1", "textfocus:text1",
                    "textclick:text1", "parentclick:text1"},
            IE = {"labelclick:label1", "parentclick:label1", "textclick:text1",
                    "parentclick:text1", "textfocus:text1"})
    @HtmlUnitNYI(IE = {"labelclick:label1", "parentclick:label1", "textfocus:text1",
                    "textclick:text1", "parentclick:text1"})
    public void clickForEventBubbling() throws Exception {
        final String html =
              "  <div onclick='log(\"parentclick:\" + event.target.id)' "
                          + "onfocus='log(\"parentfocus:\" + event.target.id)'>\n"
              + "    <label id='label1' for='text1' onclick='log(\"labelclick:\" + event.target.id)' "
                          + "onfocus='log(\"labelfocus:\" + event.target.id)'>Click me</label>\n"
              + "    <input type='text' id='text1' onclick='log(\"textclick:\" + event.target.id)' "
                          + "onfocus='log(\"textfocus:\" + event.target.id)'>\n"
              + "  </div>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("text1")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickNestedNotLabelable() throws Exception {
        final String html =
              "  <label id='label1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <div id='div1' onclick='log(\"divclick\")' onfocus='log(\"divfocus\")'></div>\n"
            + "  </label>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick", "text1focus", "text1click", "labelclick"},
            IE = {"labelclick", "text1click", "labelclick", "text1focus"})
    @HtmlUnitNYI(IE = {"labelclick", "text1focus", "text1click", "labelclick"})
    public void clickNestedInput() throws Exception {
        final String html =
              "  <label id='label1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='log(\"text1click\")' onfocus='log(\"text1focus\")'>\n"
            + "    <input type='text' id='text2' onclick='log(\"text2click\")' onfocus='log(\"text2focus\")'>\n"
            + "  </label>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("text1")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick", "select1focus", "select1click", "labelclick"},
            IE = {"labelclick", "select1click", "labelclick", "select1focus"})
    @HtmlUnitNYI(IE = {"labelclick", "select1focus", "select1click", "labelclick"})
    public void clickNestedSelect() throws Exception {
        final String html =
              "  <label id='label1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
              // we have to add some extra text to make Selenium click the label and not the nested element
              + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
              + "    <select id='select1' onclick='log(\"select1click\")' "
                          + "onfocus='log(\"select1focus\")'><option>Option</option></select>\n"
              + "    <select id='select2' onclick='log(\"select2click\")' "
                          + "onfocus='log(\"select2focus\")'><option>Option</option></select>\n"
              + "  </label>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("select1")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick", "text1focus", "text1click", "labelclick"},
            IE = {"labelclick", "text1click", "labelclick", "text1focus"})
    @HtmlUnitNYI(IE = {"labelclick", "text1focus", "text1click", "labelclick"})
    public void clickNestedTextArea() throws Exception {
        final String html =
              "  <label id='label1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <textarea id='text1' onclick='log(\"text1click\")' onfocus='log(\"text1focus\")'></textarea>\n"
            + "    <textarea id='text2' onclick='log(\"text2click\")' onfocus='log(\"text2focus\")'></textarea>\n"
            + "  </label>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("text1")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "labelclick",
            IE = {"labelclick", "labelclick"})
    @HtmlUnitNYI(IE = "labelclick")
    public void clickNestedDisabled() throws Exception {
        final String html =
              "  <label id='label1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='log(\"text1click\")' "
                        + "onfocus='log(\"text1focus\")' disabled>\n"
            + "    <input type='text' id='text2' onclick='log(\"text2click\")' "
                        + "onfocus='log(\"text2focus\")'>\n"
            + "  </label>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "text1click", "labelclick"})
    public void clickNestedNotDisplayed() throws Exception {
        final String html =
              "  <label id='label1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
              // we have to add some extra text to make Selenium click the label and not the nested element
              + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
              + "    <input type='text' id='text1' onclick='log(\"text1click\")' "
                          + "onfocus='log(\"text1focus\")' style='display: none;'>\n"
              + "    <input type='text' id='text2' onclick='log(\"text2click\")' "
                          + "onfocus='log(\"text2focus\")'>\n"
              + "  </label>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "text1click", "labelclick"})
    public void clickNestedNotVisible() throws Exception {
        final String html =
              "  <label id='label1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='log(\"text1click\")' "
                        + "onfocus='log(\"text1focus\")' style='visibility: hidden;'>\n"
            + "    <input type='text' id='text2' onclick='log(\"text2click\")' onfocus='log(\"text2focus\")'>\n"
            + "  </label>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"labelclick", "text1click", "labelclick"})
    public void clickNestedHidden() throws Exception {
        final String html =
              "  <label id='label1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='log(\"text1click\")' onfocus='log(\"text1focus\")' hidden>\n"
            + "    <input type='text' id='text2' onclick='log(\"text2click\")' onfocus='log(\"text2focus\")'>\n"
            + "  </label>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick:label1", "parentclick:label1", "text1focus:text1",
                        "text1click:text1", "labelclick:text1", "parentclick:text1"},
            IE = {"labelclick:label1", "parentclick:label1", "text1click:text1",
                        "labelclick:text1", "parentclick:text1", "text1focus:text1"})
    @HtmlUnitNYI(IE = {"labelclick:label1", "parentclick:label1", "text1focus:text1",
                        "text1click:text1", "labelclick:text1", "parentclick:text1"})
    public void clickNestedEventBubbling() throws Exception {
        final String html =
              "  <div onclick='log(\"parentclick:\" + event.target.id)' "
                      + "onfocus='log(\"parentfocus:\" + event.target.id)'>\n"
              + "    <label id='label1' onclick='log(\"labelclick:\" + event.target.id)' "
                          + "onfocus='log(\"labelfocus:\" + event.target.id)'>Click me"
              // we have to add some extra text to make Selenium click the label and not the nested element
              + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
              + "      <input type='text' id='text1' onclick='log(\"text1click:\" + event.target.id)' "
                          + "onfocus='log(\"text1focus:\" + event.target.id)'>\n"
              + "      <input type='text' id='text2' onclick='log(\"text2click:\" + event.target.id)' "
                          + "onfocus='log(\"text2focus:\" + event.target.id)'>\n"
              + "    </label>\n"
              + "  </div>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("text1")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick:label1", "parentclick:label1", "text1focus:text1",
                    "text1click:text1", "labelclick:text1", "parentclick:text1"},
            IE = {"labelclick:label1", "parentclick:label1", "text1click:text1",
                    "labelclick:text1", "parentclick:text1", "text1focus:text1"})
    @HtmlUnitNYI(IE = {"labelclick:label1", "parentclick:label1", "text1focus:text1",
                    "text1click:text1", "labelclick:text1", "parentclick:text1"})
    public void clickForAndNestedEventBubbling() throws Exception {
        final String html =
              "  <div onclick='log(\"parentclick:\" + event.target.id)' "
                          + "onfocus='log(\"parentfocus:\" + event.target.id)'>\n"
              + "    <label id='label1' for='text1' onclick='log(\"labelclick:\" + event.target.id)' "
                          + "onfocus='log(\"labelfocus:\" + event.target.id)'>Click me"
              // we have to add some extra text to make Selenium click the label and not the nested element
              + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
              + "      <input type='text' id='text1' onclick='log(\"text1click:\" + event.target.id)' "
                          + "onfocus='log(\"text1focus:\" + event.target.id)'>\n"
              + "      <input type='text' id='text2' onclick='log(\"text2click:\" + event.target.id)' "
                          + "onfocus='log(\"text2focus:\" + event.target.id)'>\n"
              + "    </label>\n"
              + "  </div>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("text1")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"labelclick", "text2focus", "text2click"},
            IE = {"labelclick", "text2click", "text2focus"})
    @HtmlUnitNYI(IE = {"labelclick", "text2focus", "text2click"})
    public void clickForVersusNested() throws Exception {
        final String html =
              "  <label id='label1' for='text2' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='log(\"text1click\")' onfocus='log(\"text1focus\")'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2' onclick='log(\"text2click\")' onfocus='log(\"text2focus\")'>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("text2")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("labelclick")
    public void clickForUnknownVersusNested() throws Exception {
        final String html =
              "  <label id='label1' for='unknown' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='log(\"text1click\")' onfocus='log(\"text1focus\")'>\n"
            + "  </label>\n"
            + "  <input type='text' id='text2' onclick='log(\"text2click\")' onfocus='log(\"text2focus\")'>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "labelclick",
            IE = "labelclick;div1click")
    @HtmlUnitNYI(IE = "labelclick")
    public void clickForNotLabelableVersusNested() throws Exception {
        final String html =
              "  <label id='label1' for='div1' onclick='log(\"labelclick\")' onfocus='log(\"labelfocus\")'>Click me"
            // we have to add some extra text to make Selenium click the label and not the nested element
            + " (we have to add some extra text to make Selenium click the label and not the nested element)\n"
            + "    <input type='text' id='text1' onclick='log(\"text1click\")' onfocus='log(\"text1focus\")'>\n"
            + "  </label>\n"
            + "  <div id='div1' onclick='log(\"div1click\")' onfocus='log(\"div1focus\")'></div>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("label1")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
        assertEquals(driver.findElement(By.id("body")), driver.switchTo().activeElement());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click listItem1", "click list", "true"})
    public void triggerRadioFor() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Radio 1</label>\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' "
                        + "onclick='log(\"click radio1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label", "click listItem1", "click list", "true"})
    public void triggerRadioNested() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' onclick='log(\"click radio1Label\")'>Radio 1\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' "
                          + "onclick='log(\"click radio1\");'>\n"
            + "      </label>"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label", "click listItem1", "click list", "true"})
    public void triggerRadioForAndNested() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Radio 1\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' "
                          + "onclick='log(\"click radio1\");'>\n"
            + "      </label>"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click listItem1", "click list", "true"})
    public void triggerRadioCheckedFor() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Radio 1</label>\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' checked "
                        + "onclick='log(\"click radio1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label", "click listItem1", "click list", "true"})
    public void triggerRadioCheckedNested() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' onclick='log(\"click radio1Label\")'>Radio 1\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' checked "
                          + "onclick='log(\"click radio1\");'>\n"
            + "      </label>"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label", "click listItem1", "click list", "true"})
    public void triggerRadioCheckedForAndNested() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Radio 1\n"
            + "        <input id='radio1' name='radios' value='1' type='radio' checked "
                          + "onclick='log(\"click radio1\");'>\n"
            + "      </label>"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list", "false"})
    public void triggerRadioDisabled() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Radio 1</label>\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' disabled "
                        + "onclick='log(\"click radio1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click listItem1", "click list", "true"})
    public void triggerRadioNotDisplayed() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Radio 1</label>\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' style='display: none;' "
                        + "onclick='log(\"click radio1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click listItem1", "click list", "true"})
    public void triggerRadioNotVisible() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Radio 1</label>\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' style='visibility: hidden;' "
                        + "onclick='log(\"click radio1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click listItem1", "click list", "true"})
    public void triggerRadioHidden() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Radio 1</label>\n"
            + "      <input id='radio1' name='radios' value='1' type='radio' hidden "
                        + "onclick='log(\"click radio1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list", "true"})
    public void triggerRadioComplexCase() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='radio1' name='radios' value='1' type='radio' "
                            + "onclick='log(\"click radio1\");'>\n"
            + "          <span id='radio1Span' onclick='log(\"click span1\")'>Radio 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list", "true"})
    public void triggerRadioComplexCaseChecked() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='radio1' name='radios' value='1' type='radio' checked "
                            + "onclick='log(\"click radio1\");'>\n"
            + "          <span id='radio1Span' onclick='log(\"click span1\")'>Radio 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list", "true"})
    public void triggerRadioComplexCaseInvisible() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='radio1' name='radios' value='1' type='radio' style='display: none;' "
                            + "onclick='log(\"click radio1\");'>\n"
            + "          <span id='radio1Span' onclick='log(\"click span1\")'>Radio 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list", "true"})
    public void triggerRadioComplexCaseHidden() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='radio1' name='radios' value='1' type='radio' hidden "
                            + "onclick='log(\"click radio1\");'>\n"
            + "          <span id='radio1Span' onclick='log(\"click span1\")'>Radio 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click radio1Label", "click listItem1", "click list", "false"})
    public void triggerRadioComplexCaseDisabled() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='radio1' name='radios' value='1' type='radio' disabled "
                            + "onclick='log(\"click radio1\");'>\n"
            + "          <span id='radio1Span' onclick='log(\"click span1\")'>Radio 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1Label", "click listItem1", "click list",
                "click radio1", "click radio1Label",
                "click listItem1", "click list", "true"})
    public void triggerRadioComplexCaseChildOfLabel() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Label\n"
            + "        <input id='radio1' name='radios' value='1' type='radio'"
                          + "onclick='log(\"click radio1\");'>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click radio1", "click radio1Label", "click listItem1", "click list", "true"})
    public void triggerRadioComplexCaseClickRadio() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='radio1Label' for='radio1' onclick='log(\"click radio1Label\")'>Label\n"
            + "        <input id='radio1' name='radios' value='1' type='radio'"
                          + "onclick='log(\"click radio1\");'>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"radio1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("radio1")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click listItem1", "click list", "true"})
    public void triggerCheckboxFor() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Checkbox 1</label>\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' "
                        + "onclick='log(\"click check1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label", "click listItem1", "click list", "true"})
    public void triggerCheckboxNested() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' onclick='log(\"click check1Label\")'>Checkbox 1\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox' "
                          + "onclick='log(\"click check1\");'>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label", "click listItem1", "click list", "true"})
    public void triggerCheckboxForAndNested() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Checkbox 1\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox' "
                          + "onclick='log(\"click check1\");'>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click listItem1", "click list", "false"})
    public void triggerCheckboxCheckedFor() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Checkbox 1</label>\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' checked "
                        + "onclick='log(\"click check1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label", "click listItem1", "click list", "false"})
    public void triggerCheckboxCheckedNested() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' onclick='log(\"click check1Label\")'>Checkbox 1\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox' checked "
                          + "onclick='log(\"click check1\");'>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label", "click listItem1", "click list", "false"})
    public void triggerCheckboxCheckedForAndNested() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Checkbox 1\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox' checked "
                          + "onclick='log(\"click check1\");'>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list", "false"})
    public void triggerCheckboxDisabled() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Checkbox 1</label>\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' disabled "
                        + "onclick='log(\"click check1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click listItem1", "click list", "true"})
    public void triggerCheckboxNotDisplayed() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Checkbox 1</label>\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' style='display: none;' "
                        + "onclick='log(\"click check1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click listItem1", "click list", "true"})
    public void triggerCheckboxNotVisible() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Checkbox 1</label>\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' style='visibility: hidden;' "
                        + "onclick='log(\"click check1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click listItem1", "click list", "true"})
    public void triggerCheckboxHidden() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Checkbox 1</label>\n"
            + "      <input id='check1' name='checks' value='1' type='checkbox' hidden "
                        + "onclick='log(\"click check1\");'>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list", "true"})
    public void triggerCheckboxComplexCase() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='check1' name='checks' value='1' type='checkbox' "
                            + "onclick='log(\"click check1\");'>\n"
            + "          <span id='check1Span' onclick='log(\"click span1\")'>Checkbox 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list", "false"})
    public void triggerCheckboxComplexCaseChecked() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='check1' name='checks' value='1' type='checkbox' checked "
                            + "onclick='log(\"click check1\");'>\n"
            + "          <span id='check1Span' onclick='log(\"click span1\")'>Checkbox 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list", "true"})
    public void triggerCheckboxComplexCaseInvisible() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='check1' name='checks' value='1' type='checkbox' style='display: none;' "
                            + "onclick='log(\"click check1\");'>\n"
            + "          <span id='check1Span' onclick='log(\"click span1\")'>Checkbox 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list", "true"})
    public void triggerCheckboxComplexCaseHidden() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='check1' name='checks' value='1' type='checkbox' hidden "
                            + "onclick='log(\"click check1\");'>\n"
            + "          <span id='check1Span' onclick='log(\"click span1\")'>Checkbox 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click span1", "click check1Label", "click listItem1", "click list", "false"})
    public void triggerCheckboxComplexCaseDisabled() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>\n"
            + "        <span>\n"
            + "          <input id='check1' name='checks' value='1' type='checkbox' disabled "
                            + "onclick='log(\"click check1\");'>\n"
            + "          <span id='check1Span' onclick='log(\"click span1\")'>Checkbox 1</span>\n"
            + "        </span>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Span")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1Label", "click listItem1", "click list",
                "click check1", "click check1Label",
                "click listItem1", "click list", "true"})
    public void triggerCheckboxComplexCaseChildOfLabel() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Label\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox'"
                          + "onclick='log(\"click check1\");'>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1Label")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"click check1", "click check1Label", "click listItem1", "click list", "true"})
    public void triggerCheckboxComplexCaseClickCheckbox() throws Exception {
        final String html =
              "  <ul onclick='log(\"click list\")'>\n"
            + "    <li onclick='log(\"click listItem1\")'>\n"
            + "      <label id='check1Label' for='check1' onclick='log(\"click check1Label\")'>Label\n"
            + "        <input id='check1' name='checks' value='1' type='checkbox'"
                          + "onclick='log(\"click check1\");'>\n"
            + "      </label>\n"
            + "    </li>\n"
            + "  </ul>\n"
            + "  <button id='check' onclick='log(document.getElementById(\"check1\").checked)'>Check</button>\n";
        final WebDriver driver = loadPage2(generatePage(html));
        driver.findElement(By.id("check1")).click();
        driver.findElement(By.id("check")).click();
        assertTitle(driver, String.join(";", getExpectedAlerts()) + ";");
    }

    private String generatePage(final String snippet) {
        return "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function log(x) {\n"
            + "        document.title += x + ';';\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body'>\n"
            + snippet
            + "  </body>\n"
            + "</html>";
    }
}
