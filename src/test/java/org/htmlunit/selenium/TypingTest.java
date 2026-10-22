/*
 * Copyright (c) 2002-2026 Gargoyle Software Inc.
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

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Modified from <a href=
 * "https://github.com/SeleniumHQ/selenium/blob/master/java/client/test/org/openqa/selenium/TypingTest.java">
 * TypingTest.java</a>.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
public class TypingTest extends SeleniumTest {

    private static String getValueText(WebElement el) {
        // Standardize on \n and strip any trailing whitespace.
        return el.getAttribute("value").replace("\r\n", "\n").trim();
    }

    @Test
    void testShouldFireKeyPressEvents() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("a");

        final WebElement result = driver.findElement(By.id("result"));
        assertThat(result.getText()).contains("press:");
    }

    @Test
    void testShouldFireKeyDownEvents() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("I");

        final WebElement result = driver.findElement(By.id("result"));
        assertThat(result.getText()).contains("down:");
    }

    @Test
    void testShouldFireKeyUpEvents() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("a");

        final WebElement result = driver.findElement(By.id("result"));
        assertThat(result.getText()).contains("up:");
    }

    @Test
    void testShouldTypeLowerCaseLetters() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("abc def");

        assertThat(keyReporter.getAttribute("value")).isEqualTo("abc def");
    }

    @Test
    void testShouldBeAbleToTypeCapitalLetters() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("ABC DEF");

        assertThat(keyReporter.getAttribute("value")).isEqualTo("ABC DEF");
    }

    @Test
    void testShouldBeAbleToTypeQuoteMarks() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("\"");

        assertThat(keyReporter.getAttribute("value")).isEqualTo("\"");
    }

    @Test
    void testShouldBeAbleToTypeTheAtCharacter() {
        // simon: I tend to use a US/UK or AUS keyboard layout with English
        // as my primary language. There are consistent reports that we're
        // not handling i18nised keyboards properly. This test exposes this
        // in a lightweight manner when my keyboard is set to the DE mapping
        // and we're using IE.

        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("@");

        assertThat(keyReporter.getAttribute("value")).isEqualTo("@");
    }

    @Test
    void testShouldBeAbleToMixUpperAndLowerCaseLetters() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("me@eXample.com");

        assertThat(keyReporter.getAttribute("value")).isEqualTo("me@eXample.com");
    }

    @Test
    void testArrowKeysShouldNotBePrintable() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys(Keys.ARROW_LEFT);

        assertThat(keyReporter.getAttribute("value")).isEmpty();
    }

    @Test
    void testShouldBeAbleToUseArrowKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement keyReporter = driver.findElement(By.id("keyReporter"));
        keyReporter.sendKeys("tet", Keys.ARROW_LEFT, "s");

        assertThat(keyReporter.getAttribute("value")).isEqualTo("test");
    }

    @Test
    public void testWillSimulateAKeyUpWhenEnteringTextIntoInputElements() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyUp"));
        element.sendKeys("I like cheese");

        final WebElement result = driver.findElement(By.id("result"));
        assertThat(result.getText()).isEqualTo("I like cheese");
    }

    @Test
    public void testWillSimulateAKeyDownWhenEnteringTextIntoInputElements() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyDown"));
        element.sendKeys("I like cheese");

        final WebElement result = driver.findElement(By.id("result"));
        // Because the key down gets the result before the input element is
        // filled, we're a letter short here
        assertThat(result.getText()).isEqualTo("I like chees");
    }

    @Test
    public void testWillSimulateAKeyPressWhenEnteringTextIntoInputElements() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyPress"));
        element.sendKeys("I like cheese");

        final WebElement result = driver.findElement(By.id("result"));
        // Because the key down gets the result before the input element is
        // filled, we're a letter short here
        assertThat(result.getText()).isEqualTo("I like chees");
    }

    @Test
    public void testWillSimulateAKeyUpWhenEnteringTextIntoTextAreas() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyUpArea"));
        element.sendKeys("I like cheese");

        final WebElement result = driver.findElement(By.id("result"));
        assertThat(result.getText()).isEqualTo("I like cheese");
    }

    @Test
    public void testWillSimulateAKeyDownWhenEnteringTextIntoTextAreas() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyDownArea"));
        element.sendKeys("I like cheese");

        final WebElement result = driver.findElement(By.id("result"));
        // Because the key down gets the result before the input element is
        // filled, we're a letter short here
        assertThat(result.getText()).isEqualTo("I like chees");
    }

    @Test
    public void testWillSimulateAKeyPressWhenEnteringTextIntoTextAreas() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyPressArea"));
        element.sendKeys("I like cheese");

        final WebElement result = driver.findElement(By.id("result"));
        // Because the key down gets the result before the input element is
        // filled, we're a letter short here
        assertThat(result.getText()).isEqualTo("I like chees");
    }

    @Test
    public void testShouldFireFocusKeyEventsInTheRightOrder() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("theworks"));

        element.sendKeys("a");
        assertThat(result.getText().trim()).isEqualTo("focus keydown keypress keyup");
    }

    @Test
    @Alerts({"down: 40 up: 40", "down: 38 up: 38", "down: 37 up: 37", "down: 39 up: 39"})
    @HtmlUnitNYI(FF = {"down: 40 press: 40 up: 40", "down: 38 press: 38 up: 38",
                       "down: 37 press: 37 up: 37", "down: 39 press: 39 up: 39"},
            FF_ESR = {"down: 40 press: 40 up: 40", "down: 38 press: 38 up: 38",
                      "down: 37 press: 37 up: 37", "down: 39 press: 39 up: 39"})
    void testShouldReportKeyCodeOfArrowKeys() {
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
        assertThat(element.getAttribute("value")).isEmpty();
    }

    @Test
    void testShouldReportKeyCodeOfArrowKeysUpDownEvents() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys(Keys.ARROW_DOWN);
        assertThat(result.getText().trim()).contains("down: 40", "up: 40");

        element.sendKeys(Keys.ARROW_UP);
        assertThat(result.getText().trim()).contains("down: 38", "up: 38");

        element.sendKeys(Keys.ARROW_LEFT);
        assertThat(result.getText().trim()).contains("down: 37", "up: 37");

        element.sendKeys(Keys.ARROW_RIGHT);
        assertThat(result.getText().trim()).contains("down: 39", "up: 39");

        // And leave no rubbish/printable keys in the "keyReporter"
        assertThat(element.getAttribute("value")).isEmpty();
    }

    @Test
    void testNumericNonShiftKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String numericLineCharsNonShifted = "`1234567890-=[]\\;,.'/42";
        element.sendKeys(numericLineCharsNonShifted);

        assertThat(element.getAttribute("value")).isEqualTo(numericLineCharsNonShifted);
    }

    @Test
    @Alerts(DEFAULT = "down: 192 press: 126 up: 192 up: 16",
            FF = "down: 192 press: 126 up: 192",
            FF_ESR = "down: 192 press: 126 up: 192")
    @HtmlUnitNYI(FF = "down: 192 press: 126 up: 192 up: 16",
            FF_ESR = "down: 192 press: 126 up: 192 up: 16")
    public void testNumericShiftKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String numericShiftsEtc = "~!@#$%^&*()_+{}:\"<>?|END~";
        element.sendKeys(numericShiftsEtc);

        assertThat(element.getAttribute("value")).isEqualTo(numericShiftsEtc);
        assertEquals(getExpectedAlerts()[0], result.getText().trim());
    }

    @Test
    void testLowerCaseAlphaKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String lowerAlphas = "abcdefghijklmnopqrstuvwxyz";
        element.sendKeys(lowerAlphas);

        assertThat(element.getAttribute("value")).isEqualTo(lowerAlphas);
    }

    @Test
    @Alerts(DEFAULT = "down: 90 press: 90 up: 90 up: 16",
            FF = "down: 90 press: 90 up: 90",
            FF_ESR = "down: 90 press: 90 up: 90")
    @HtmlUnitNYI(FF = "down: 90 press: 90 up: 90 up: 16",
            FF_ESR = "down: 90 press: 90 up: 90 up: 16")
    public void testUppercaseAlphaKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String upperAlphas = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        element.sendKeys(upperAlphas);

        assertThat(element.getAttribute("value")).isEqualTo(upperAlphas);
        assertEquals(getExpectedAlerts()[0], result.getText().trim());
    }

    @Test
    @Alerts(DEFAULT = "down: 192 press: 126 up: 192 up: 16",
            FF = "down: 192 press: 126 up: 192",
            FF_ESR = "down: 192 press: 126 up: 192")
    @HtmlUnitNYI(FF = "down: 192 press: 126 up: 192 up: 16",
            FF_ESR = "down: 192 press: 126 up: 192 up: 16")
    public void testAllPrintableKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String allPrintable = "!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFGHIJKLMNO"
                + "PQRSTUVWXYZ [\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
        element.sendKeys(allPrintable);

        assertThat(element.getAttribute("value")).isEqualTo(allPrintable);
        assertEquals(getExpectedAlerts()[0], result.getText());
    }

    @Test
    void testArrowKeysAndPageUpAndDown() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys(
                "a" + Keys.LEFT + "b" + Keys.RIGHT + Keys.UP + Keys.DOWN + Keys.PAGE_UP + Keys.PAGE_DOWN + "1");
        assertThat(element.getAttribute("value")).isEqualTo("ba1");
    }

    @Test
    // @Ignore(value = FIREFOX, reason = "Firefox can't type at beginning of field", issue = "https://github.com/mozilla/geckodriver/issues/2015")
    public void testHomeAndEndAndPageUpAndPageDownKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abc" + Keys.HOME + "0" + Keys.LEFT + Keys.RIGHT + Keys.PAGE_UP + Keys.PAGE_DOWN + Keys.END
                + "1" + Keys.HOME + "0" + Keys.PAGE_UP + Keys.END + "111" + Keys.HOME + "00");
        assertThat(element.getAttribute("value")).isEqualTo("0000abc1111");
    }

    @Test
    void testDeleteAndBackspaceKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcdefghi");
        assertThat(element.getAttribute("value")).isEqualTo("abcdefghi");

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.DELETE);
        assertThat(element.getAttribute("value")).isEqualTo("abcdefgi");

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.BACK_SPACE);
        assertThat(element.getAttribute("value")).isEqualTo("abcdfgi");
    }

    @Test
    void testSpecialSpaceKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcd" + Keys.SPACE + "fgh" + Keys.SPACE + "ij");
        assertThat(element.getAttribute("value")).isEqualTo("abcd fgh ij");
    }

    @Test
    public void testNumberPadKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys(
                "abcd" + Keys.MULTIPLY + Keys.SUBTRACT + Keys.ADD + Keys.DECIMAL + Keys.SEPARATOR + Keys.NUMPAD0
                        + Keys.NUMPAD9 + Keys.ADD + Keys.SEMICOLON + Keys.EQUALS + Keys.DIVIDE + Keys.NUMPAD3 + "abcd");
        assertThat(element.getAttribute("value")).isEqualTo("abcd*-+.,09+;=/3abcd");
    }

    @Test
    public void testFunctionKeys() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("FUNCTION" + Keys.F4 + "-KEYS" + Keys.F4);
        element.sendKeys("" + Keys.F4 + "-TOO" + Keys.F4);
        assertThat(element.getAttribute("value")).isEqualTo("FUNCTION-KEYS-TOO");
    }

    @Test
    public void testShiftSelectionDeletes() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("abcd efgh");
        assertThat(element.getAttribute("value")).isEqualTo("abcd efgh");

        element.sendKeys(Keys.SHIFT, Keys.LEFT, Keys.LEFT, Keys.LEFT);
        element.sendKeys(Keys.DELETE);
        assertThat(element.getAttribute("value")).isEqualTo("abcd e");
    }

    @Test
    void testChordControlHomeShiftEndDelete() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFG");

        element.sendKeys(Keys.END);
        element.sendKeys("" + Keys.SHIFT + Keys.HOME);

        element.sendKeys(Keys.DELETE);
        assertThat(element.getAttribute("value")).isEmpty();
    }

    @Test
    @Alerts(DEFAULT = "down: 36 up: 36 up: 16",
            FF = "down: 36 up: 36",
            FF_ESR = "down: 36 up: 36")
    @HtmlUnitNYI(FF = "down: 36 press: 36 up: 36 up: 16",
            FF_ESR = "down: 36 press: 36 up: 36 up: 16")
    public void testChordReverseShiftHomeSelectionDeletes() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement result = driver.findElement(By.id("result"));
        final WebElement element = driver.findElement(By.id("keyReporter"));

        element.sendKeys("done" + Keys.HOME);
        assertThat(element.getAttribute("value")).isEqualTo("done");

        element.sendKeys(Keys.SHIFT + "all " + Keys.HOME);
        assertThat(element.getAttribute("value")).isEqualTo("ALL done");

        element.sendKeys(Keys.DELETE);
        assertThat(element.getAttribute("value")).isEqualTo("done");

        element.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME);
        assertThat(element.getAttribute("value")).isEqualTo("done");
        assertEquals(getExpectedAlerts()[0], result.getText().trim());

        element.sendKeys(Keys.DELETE);
        assertThat(element.getAttribute("value")).isEmpty();
    }

    @Test
    // @Ignore(value = FIREFOX, reason = "Firefox can't type at beginning of field", issue = "https://github.com/mozilla/geckodriver/issues/2015")
    public void testChordControlCutAndPaste() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement element = driver.findElement(By.id("keyReporter"));

        final String paste = "!\"#$%&'()*+,-./0123456789:;<=>?@ ABCDEFG";
        element.sendKeys(paste);
        assertThat(element.getAttribute("value")).isEqualTo(paste);

        element.sendKeys(Keys.HOME);
        element.sendKeys("" + Keys.HOME + Keys.SHIFT + Keys.END);

        element.sendKeys(Keys.CONTROL, "x");
        assertThat(element.getAttribute("value")).isEmpty();

        element.sendKeys(Keys.CONTROL, "v");
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(d -> paste.equals(element.getAttribute("value")));

        // Cut the last 3 letters.
        element.sendKeys("" + Keys.LEFT + Keys.LEFT + Keys.LEFT + Keys.SHIFT + Keys.END);

        element.sendKeys(Keys.CONTROL, "x");
        assertThat(element.getAttribute("value")).isEqualTo(paste.substring(0, paste.length() - 3));

        // Paste the last 3 letters.
        element.sendKeys(Keys.CONTROL, "v");
        assertThat(element.getAttribute("value")).isEqualTo(paste);

        element.sendKeys(Keys.HOME);
        element.sendKeys(Keys.CONTROL, "v");
        element.sendKeys(Keys.CONTROL, "v" + "v");
        element.sendKeys(Keys.CONTROL, "v" + "v" + "v");
        assertThat(element.getAttribute("value")).isEqualTo("EFGEFGEFGEFGEFGEFG" + paste);

        element.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME + Keys.NULL + Keys.DELETE);
        assertThat(element.getAttribute("value")).isEmpty();
    }

    @Test
    void testShouldTypeIntoInputElementsThatHaveNoTypeAttribute() {
        final WebDriver driver = getWebDriver("/formPage.html");

        final WebElement element = driver.findElement(By.id("no-type"));

        element.sendKeys("should say cheese");
        assertThat(element.getAttribute("value")).isEqualTo("should say cheese");
    }

    @Test
    void testShouldNotTypeIntoElementsThatPreventKeyDownEvents() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement silent = driver.findElement(By.name("suppress"));

        silent.sendKeys("s");
        assertThat(silent.getAttribute("value")).isEmpty();
    }

    @Test
    public void testGenerateKeyPressEventEvenWhenElementPreventsDefault() {
        final WebDriver driver = getWebDriver("/javascriptPage.html");

        final WebElement silent = driver.findElement(By.name("suppress"));
        final WebElement result = driver.findElement(By.id("result"));

        silent.sendKeys("s");
        assertThat(result.getText().trim()).isIn("", "mouseover");
    }

    @Test
    void testShouldBeAbleToTypeOnAnEmailInputField() {
        final WebDriver driver = getWebDriver("/formPage.html");
        final WebElement email = driver.findElement(By.id("email"));
        email.sendKeys("foobar");
        assertThat(email.getAttribute("value")).isEqualTo("foobar");
    }

    @Test
    void testShouldBeAbleToTypeOnANumberInputField() {
        final WebDriver driver = getWebDriver("/formPage.html");
        final WebElement email = driver.findElement(By.id("age"));
        email.sendKeys("33");
        assertThat(email.getAttribute("value")).isEqualTo("33");
    }

    @Test
    void testShouldThrowIllegalArgumentException() {
        final WebDriver driver = getWebDriver("/formPage.html");
        final WebElement email = driver.findElement(By.id("age"));
        assertThrows(IllegalArgumentException.class, () -> email.sendKeys((CharSequence[]) null));
    }

    @Test
    void canSafelyTypeOnElementThatIsRemovedFromTheDomOnKeyPress() {
        final WebDriver driver = getWebDriver("/key_tests/remove_on_keypress.html");

        WebElement input = driver.findElement(By.id("target"));
        WebElement log = driver.findElement(By.id("log"));

        assertThat(log.getAttribute("value")).isEmpty();

        input.sendKeys("b");
        assertThat(getValueText(log))
                .isEqualTo(String.join("\n", "keydown (target)", "keyup (target)", "keyup (body)"));

        input.sendKeys("a");

        // Some drivers (IE, Firefox) do not always generate the final keyup event since
        // the element
        // is removed from the DOM in response to the keypress (note, this is a product
        // of how events
        // are generated and does not match actual user behavior).
        String expected = String.join("\n", "keydown (target)", "keyup (target)", "keyup (body)", "keydown (target)",
                "a pressed; removing");
        assertThat(getValueText(log)).isIn(expected, expected + "\nkeyup (body)");
    }

    @Test
    void canClearNumberInputAfterTypingInvalidInput() {
        final WebDriver driver = getWebDriver("/formPage.html");

        WebElement input = driver.findElement(By.id("age"));
        input.sendKeys("e");
        input.clear();
        input.sendKeys("3");
        assertThat(input.getAttribute("value")).isEqualTo("3");
    }

    @Test
    void canTypeSingleNewLineCharacterIntoTextArea() {
        final WebDriver driver = getWebDriver("/formPage.html");

        WebElement element = driver.findElement(By.id("emptyTextArea"));
        element.sendKeys("\n");
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(d -> "\n".equals(element.getAttribute("value")));
    }

    @Test
    void canTypeMultipleNewLineCharactersIntoTextArea() {
        final WebDriver driver = getWebDriver("/formPage.html");

        WebElement element = driver.findElement(By.id("emptyTextArea"));
        element.sendKeys("\n\n\n");
        new WebDriverWait(driver, Duration.ofSeconds(2)).until(d -> "\n\n\n".equals(element.getAttribute("value")));
    }
}
