/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.selenium;

import static org.hamcrest.core.AnyOf.anyOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Modified from
 * <a href="https://github.com/SeleniumHQ/selenium/blob/master/java/client/test/org/openqa/selenium/TypingTest.java">
 * TypingTest.java</a>.
 *
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class TypingTest extends SeleniumTest {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented
    public void shouldBeAbleToUseArrowKeys() throws Exception {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("tet", Keys.ARROW_LEFT, "s");

        assertThat(keyReporter.getAttribute("value"), is("test"));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void shouldReportKeyCodeOfArrowKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys(Keys.ARROW_DOWN);
        checkRecordedKeySequence(result, 40);

        element.sendKeys(Keys.ARROW_UP);
        checkRecordedKeySequence(result, 38);

        element.sendKeys(Keys.ARROW_LEFT);
        checkRecordedKeySequence(result, 37);

        element.sendKeys(Keys.ARROW_RIGHT);
        checkRecordedKeySequence(result, 39);

        // And leave no rubbish/printable keys in the "keyReporter"
        assertThat(element.getAttribute("value"), is(""));
    }

    private static void checkRecordedKeySequence(final WebElement element, final int expectedKeyCode) {
        assertThat(element.getText().trim(),
                anyOf(is(String.format("down: %1$d press: %1$d up: %1$d", expectedKeyCode)),
                        is(String.format("down: %1$d up: %1$d", expectedKeyCode))));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void shouldReportKeyCodeOfArrowKeysUpDownEvents() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys(Keys.ARROW_DOWN);
        assertThat(result.getText().trim(), containsString("down: 40"));
        assertThat(result.getText().trim(), containsString("up: 40"));

        element.sendKeys(Keys.ARROW_UP);
        assertThat(result.getText().trim(), containsString("down: 38"));
        assertThat(result.getText().trim(), containsString("up: 38"));

        element.sendKeys(Keys.ARROW_LEFT);
        assertThat(result.getText().trim(), containsString("down: 37"));
        assertThat(result.getText().trim(), containsString("up: 37"));

        element.sendKeys(Keys.ARROW_RIGHT);
        assertThat(result.getText().trim(), containsString("down: 39"));
        assertThat(result.getText().trim(), containsString("up: 39"));

        // And leave no rubbish/printable keys in the "keyReporter"
        assertThat(element.getAttribute("value"), is(""));
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

        assertThat(element.getAttribute("value"), is(numericShiftsEtc));
        assertThat(result.getText().trim(), containsString(" up: 16"));
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

        assertThat(element.getAttribute("value"), is(upperAlphas));
        assertThat(result.getText().trim(), containsString(" up: 16"));
    }

    /**
     * A test.
     */
    @Test
    public void testAllPrintableKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String allPrintable =
                "!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFGHIJKLMNO"
                + "PQRSTUVWXYZ [\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        element.sendKeys(allPrintable);

        assertThat(element.getAttribute("value"), is(allPrintable));
        assertThat(result.getText().trim(), containsString(" up: 16"));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void testArrowKeysAndPageUpAndDown() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("a" + Keys.LEFT + "b" + Keys.RIGHT
                + Keys.UP + Keys.DOWN + Keys.PAGE_UP + Keys.PAGE_DOWN + "1");
        assertThat(element.getAttribute("value"), is("ba1"));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void testHomeAndEndAndPageUpAndPageDownKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abc" + Keys.HOME + "0" + Keys.LEFT + Keys.RIGHT
                + Keys.PAGE_UP + Keys.PAGE_DOWN + Keys.END + "1" + Keys.HOME
                + "0" + Keys.PAGE_UP + Keys.END + "111" + Keys.HOME + "00");
        assertThat(element.getAttribute("value"), is("0000abc1111"));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void testDeleteAndBackspaceKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcdefghi");
        assertThat(element.getAttribute("value"), is("abcdefghi"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.DELETE);
        assertThat(element.getAttribute("value"), is("abcdefgi"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.BACK_SPACE);
        assertThat(element.getAttribute("value"), is("abcdfgi"));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void specialSpaceKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcd" + Keys.SPACE + "fgh" + Keys.SPACE + "ij");
        assertThat(element.getAttribute("value"), is("abcd fgh ij"));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void numberpadKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcd" + Keys.MULTIPLY + Keys.SUBTRACT + Keys.ADD
                + Keys.DECIMAL + Keys.SEPARATOR + Keys.NUMPAD0 + Keys.NUMPAD9
                + Keys.ADD + Keys.SEMICOLON + Keys.EQUALS + Keys.DIVIDE
                + Keys.NUMPAD3 + "abcd");
        assertThat(element.getAttribute("value"), is("abcd*-+.,09+;=/3abcd"));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void shiftSelectionDeletes() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcd efgh");
        assertThat(element.getAttribute("value"), is("abcd efgh"));

        element.sendKeys(Keys.SHIFT, Keys.LEFT, Keys.LEFT, Keys.LEFT);
        element.sendKeys(Keys.DELETE);
        assertThat(element.getAttribute("value"), is("abcd e"));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void chordControlHomeShiftEndDelete() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFG");

        element.sendKeys(Keys.HOME);
        element.sendKeys("" + Keys.SHIFT + Keys.END);
        assertThat(result.getText(), containsString(" up: 16"));

        element.sendKeys(Keys.DELETE);
        assertThat(element.getAttribute("value"), is(""));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void chordReveseShiftHomeSelectionDeletes() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("done" + Keys.HOME);
        assertThat(element.getAttribute("value"), is("done"));

        element.sendKeys("" + Keys.SHIFT + "ALL " + Keys.HOME);
        assertThat(element.getAttribute("value"), is("ALL done"));

        element.sendKeys(Keys.DELETE);
        assertThat(element.getAttribute("value"), is("done"));

        element.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME);
        assertThat(element.getAttribute("value"), is("done"));
        // Note: trailing SHIFT up here
        assertThat(result.getText().trim(), containsString(" up: 16"));

        element.sendKeys("" + Keys.DELETE);
        assertThat(element.getAttribute("value"), is(""));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void chordControlCutAndPaste() {
        fail();
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
    public void testGenerateKeyPressEventEvenWhenElementPreventsDefault() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement silent = driver.findElement(By.name("suppress"));
        final WebElement result = driver.findElement(By.id("result"));

        silent.sendKeys("s");
        assertThat(result.getText().trim(), is(""));
    }

    /**
     * A test.
     */
    @Test
    @NotYetImplemented
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
    @Alerts(DEFAULT = { "keydown (target) keyup (target) keyup (body)",
            "keydown (target) keyup (target) keyup (body) keydown (target) a pressed; removing" },
            CHROME = { "keydown (target) keyup (target) keyup (body)",
            "keydown (target) keyup (target) keyup (body) keydown (target) a pressed; removing keyup (body)" })
    public void canSafelyTypeOnElementThatIsRemovedFromTheDomOnKeyPress() {
        final WebDriver driver = getWebDriver("/key_tests/remove_on_keypress.html");

        final WebElement input = driver.findElement(By.id("target"));
        final WebElement log = driver.findElement(By.id("log"));

        assertEquals("", log.getAttribute("value"));

        input.sendKeys("b");
        assertEquals(getExpectedAlerts()[0], getValueText(log).replace('\n', ' '));

        input.sendKeys("a");

        // Some drivers (IE, Firefox) do not always generate the final keyup event since the element
        // is removed from the DOM in response to the keypress (note, this is a product of how events
        // are generated and does not match actual user behavior).
        assertEquals(getExpectedAlerts()[1], getValueText(log).replace('\n', ' '));
    }

    private static String getValueText(final WebElement el) {
        // Standardize on \n and strip any trailing whitespace.
        return el.getAttribute("value").replace("\r\n", "\n").trim();
    }
}
