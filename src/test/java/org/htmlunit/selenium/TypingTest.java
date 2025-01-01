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
package org.htmlunit.selenium;

import org.htmlunit.junit.BrowserRunner;
import org.htmlunit.junit.BrowserRunner.Alerts;
import org.htmlunit.junit.BrowserRunner.HtmlUnitNYI;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Modified from
 * <a href="https://github.com/SeleniumHQ/selenium/blob/master/java/client/test/org/openqa/selenium/TypingTest.java">
 * TypingTest.java</a>.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class TypingTest extends SeleniumTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void shouldBeAbleToUseArrowKeys() throws Exception {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("tet", Keys.ARROW_LEFT, "s");

        assertEquals("test", keyReporter.getAttribute("value"));
        assertEquals("test", keyReporter.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    @Alerts({"down: 40 up: 40", "down: 38 up: 38", "down: 37 up: 37", "down: 39 up: 39"})
    @HtmlUnitNYI(FF = {"down: 40 press: 40 up: 40", "down: 38 press: 38 up: 38",
                       "down: 37 press: 37 up: 37", "down: 39 press: 39 up: 39"},
            FF_ESR = {"down: 40 press: 40 up: 40", "down: 38 press: 38 up: 38",
                      "down: 37 press: 37 up: 37", "down: 39 press: 39 up: 39"})
    public void shouldReportKeyCodeOfArrowKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys(Keys.ARROW_DOWN);
        assertEquals(getExpectedAlerts()[0], result.getText().trim());

        element.sendKeys(Keys.ARROW_UP);
        assertEquals(getExpectedAlerts()[1], result.getText().trim());

        element.sendKeys(Keys.ARROW_LEFT);
        assertEquals(getExpectedAlerts()[2], result.getText().trim());

        element.sendKeys(Keys.ARROW_RIGHT);
        assertEquals(getExpectedAlerts()[3], result.getText().trim());

        // And leave no rubbish/printable keys in the "keyReporter"
        assertEquals("", element.getAttribute("value"));
        assertEquals("", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void shouldReportKeyCodeOfArrowKeysUpDownEvents() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys(Keys.ARROW_DOWN);
        assertTrue(result.getText().trim().contains("down: 40"));
        assertTrue(result.getText().trim().contains("up: 40"));

        element.sendKeys(Keys.ARROW_UP);
        assertTrue(result.getText().trim().contains("down: 38"));
        assertTrue(result.getText().trim().contains("up: 38"));

        element.sendKeys(Keys.ARROW_LEFT);
        assertTrue(result.getText().trim().contains("down: 37"));
        assertTrue(result.getText().trim().contains("up: 37"));

        element.sendKeys(Keys.ARROW_RIGHT);
        assertTrue(result.getText().trim().contains("down: 39"));
        assertTrue(result.getText().trim().contains("up: 39"));

        // And leave no rubbish/printable keys in the "keyReporter"
        assertEquals("", element.getAttribute("value"));
        assertEquals("", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void numericShiftKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String numericShiftsEtc = "~!@#$%^&*()_+{}:\"<>?|END~";
        element.sendKeys(numericShiftsEtc);

        assertEquals(numericShiftsEtc, element.getAttribute("value"));
        assertEquals(numericShiftsEtc, element.getDomProperty("value"));

        assertTrue(result.getText(), result.getText().trim().contains(" up: 16"));
    }

    /**
     * A test.
     */
    @Test
    public void uppercaseAlphaKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String upperAlphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        element.sendKeys(upperAlphas);

        assertEquals(upperAlphas, element.getAttribute("value"));
        assertEquals(upperAlphas, element.getDomProperty("value"));

        assertTrue(result.getText(), result.getText().trim().contains(" up: 16"));
    }

    /**
     * A test.
     */
    @Test
    public void allPrintableKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String allPrintable =
                "!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFGHIJKLMNO"
                + "PQRSTUVWXYZ [\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        element.sendKeys(allPrintable);

        assertEquals(allPrintable, element.getAttribute("value"));
        assertEquals(allPrintable, element.getDomProperty("value"));

        assertTrue(result.getText(), result.getText().trim().contains(" up: 16"));
    }

    /**
     * A test.
     */
    @Test
    public void testArrowKeysAndPageUpAndDown() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("a" + Keys.LEFT + "b" + Keys.RIGHT
                + Keys.UP + Keys.DOWN + Keys.PAGE_UP + Keys.PAGE_DOWN + "1");

        assertEquals("ba1", element.getAttribute("value"));
        assertEquals("ba1", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void homeAndEndAndPageUpAndPageDownKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abc" + Keys.HOME + "0" + Keys.LEFT + Keys.RIGHT
                + Keys.PAGE_UP + Keys.PAGE_DOWN + Keys.END + "1" + Keys.HOME
                + "0" + Keys.PAGE_UP + Keys.END + "111" + Keys.HOME + "00");

        assertEquals("0000abc1111", element.getAttribute("value"));
        assertEquals("0000abc1111", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void deleteAndBackspaceKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcdefghi");
        assertEquals("abcdefghi", element.getAttribute("value"));
        assertEquals("abcdefghi", element.getDomProperty("value"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.DELETE);
        assertEquals("abcdefgi", element.getAttribute("value"));
        assertEquals("abcdefgi", element.getDomProperty("value"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.BACK_SPACE);
        assertEquals("abcdfgi", element.getAttribute("value"));
        assertEquals("abcdfgi", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void specialSpaceKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcd" + Keys.SPACE + "fgh" + Keys.SPACE + "ij");
        assertEquals("abcd fgh ij", element.getAttribute("value"));
        assertEquals("abcd fgh ij", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void numberpadKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcd" + Keys.MULTIPLY + Keys.SUBTRACT + Keys.ADD
                + Keys.DECIMAL + Keys.SEPARATOR + Keys.NUMPAD0 + Keys.NUMPAD9
                + Keys.ADD + Keys.SEMICOLON + Keys.EQUALS + Keys.DIVIDE
                + Keys.NUMPAD3 + "abcd");
        assertEquals("abcd*-+.,09+;=/3abcd", element.getAttribute("value"));
        assertEquals("abcd*-+.,09+;=/3abcd", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void shiftSelectionDeletes() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcd efgh");
        assertEquals("abcd efgh", element.getAttribute("value"));
        assertEquals("abcd efgh", element.getDomProperty("value"));

        element.sendKeys(Keys.SHIFT, Keys.LEFT, Keys.LEFT, Keys.LEFT);
        element.sendKeys(Keys.DELETE);
        assertEquals("abcd e", element.getAttribute("value"));
        assertEquals("abcd e", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void chordControlHomeShiftEndDelete() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFG");

        element.sendKeys(Keys.HOME);
        element.sendKeys("" + Keys.SHIFT + Keys.END);
        assertTrue(result.getText(), result.getText().contains(" up: 16"));

        element.sendKeys(Keys.DELETE);
        assertEquals("", element.getAttribute("value"));
        assertEquals("", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void chordReveseShiftHomeSelectionDeletes() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("done" + Keys.HOME);
        assertEquals("done", element.getAttribute("value"));
        assertEquals("done", element.getDomProperty("value"));

        element.sendKeys("" + Keys.SHIFT + "ALL " + Keys.HOME);
        assertEquals("ALL done", element.getAttribute("value"));
        assertEquals("ALL done", element.getDomProperty("value"));

        element.sendKeys(Keys.DELETE);
        assertEquals("done", element.getAttribute("value"));
        assertEquals("done", element.getDomProperty("value"));

        element.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME);
        assertEquals("done", element.getAttribute("value"));
        assertEquals("done", element.getDomProperty("value"));
        // Note: trailing SHIFT up here
        assertTrue(result.getText(), result.getText().trim().contains(" up: 16"));

        element.sendKeys("" + Keys.DELETE);
        assertEquals("", element.getAttribute("value"));
        assertEquals("", element.getDomProperty("value"));
    }

    /**
     * A test.
     */
    @Test
    public void generateKeyPressEventEvenWhenElementPreventsDefault() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement silent = driver.findElement(By.name("suppress"));
        final WebElement result = driver.findElement(By.id("result"));

        silent.sendKeys("s");
        assertEquals("", result.getText().trim());
    }

    /**
     * A test.
     */
    @Test
    public void nonPrintableCharactersShouldWorkWithContentEditableOrDesignModeSet() {
        final WebDriver driver = getWebDriver("/rich_text.html");

        driver.switchTo().frame("editFrame");
        final WebElement element = driver.switchTo().activeElement();
        element.sendKeys("Dishy", Keys.BACK_SPACE, Keys.LEFT, Keys.LEFT);
        element.sendKeys(Keys.LEFT, Keys.LEFT, "F", Keys.DELETE, Keys.END, "ee!");

        assertEquals("Fishee!", element.getText());
    }

    /**
     * A test.
     */
    @Test
    @Alerts({"keydown (target) keyup (target) keyup (body)",
             "keydown (target) a pressed; removing keyup (body)"})
    public void canSafelyTypeOnElementThatIsRemovedFromTheDomOnKeyPress() {
        final WebDriver driver = getWebDriver("/key_tests/remove_on_keypress.html");

        final WebElement input = driver.findElement(By.id("target"));
        final WebElement log = driver.findElement(By.id("log"));

        assertEquals("", log.getAttribute("value"));

        input.sendKeys("b");
        assertEquals(getExpectedAlerts()[0], getValueText(log).replace('\n', ' '));
        log.clear();

        input.sendKeys("a");

        assertEquals(getExpectedAlerts()[1], getValueText(log).replace('\n', ' '));
    }

    private static String getValueText(final WebElement el) {
        // Standardize on \n and strip any trailing whitespace.
        return el.getAttribute("value").replace("\r\n", "\n").trim();
    }

    /**
     * If the first typed character is prevented by preventing it's
     * KeyPress-Event, the remaining string should still be appended and NOT
     * prepended.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void typePreventedCharacterFirst() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function stopEnterKey(evt) {\n"
            + "    var evt = evt || window.event;\n"
            + "    if (evt && evt.keyCode === 13)\n"
            + "    {\n"
            + "      evt.preventDefault()\n"
            + "    }\n"
            + "  }\n"
            + "  window.document.onkeypress = stopEnterKey;\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <input id='myInput' type='text' value='Hello'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("myInput"));
        input.sendKeys("World");

        assertEquals("'World' should be appended.", "HelloWorld", input.getAttribute("value"));
    }
}
