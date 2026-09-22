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

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Modified from
 * <a href="https://github.com/SeleniumHQ/selenium/blob/master/java/client/test/org/openqa/selenium/TypingTest.java">
 * TypingTest.java</a>.
 *
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
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
        assertNull(keyReporter.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
        assertEquals("abcdefghi", element.getDomProperty("value"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.DELETE);
        assertEquals("abcdefgi", element.getAttribute("value"));
        assertNull(element.getDomAttribute("value"));
        assertEquals("abcdefgi", element.getDomProperty("value"));

        element.sendKeys(Keys.LEFT, Keys.LEFT, Keys.BACK_SPACE);
        assertEquals("abcdfgi", element.getAttribute("value"));
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
        assertEquals("abcd efgh", element.getDomProperty("value"));

        element.sendKeys(Keys.SHIFT, Keys.LEFT, Keys.LEFT, Keys.LEFT);
        element.sendKeys(Keys.DELETE);
        assertEquals("abcd e", element.getAttribute("value"));
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
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
        assertNull(element.getDomAttribute("value"));
        assertEquals("done", element.getDomProperty("value"));

        element.sendKeys(Keys.SHIFT + "ALL " + Keys.HOME);
        assertEquals("ALL done", element.getAttribute("value"));
        assertNull(element.getDomAttribute("value"));
        assertEquals("ALL done", element.getDomProperty("value"));

        element.sendKeys(Keys.DELETE);
        assertEquals("done", element.getAttribute("value"));
        assertNull(element.getDomAttribute("value"));
        assertEquals("done", element.getDomProperty("value"));

        element.sendKeys("" + Keys.END + Keys.SHIFT + Keys.HOME);
        assertEquals("done", element.getAttribute("value"));
        assertNull(element.getDomAttribute("value"));
        assertEquals("done", element.getDomProperty("value"));
        // Note: trailing SHIFT up here
        assertTrue(result.getText(), result.getText().trim().contains(" up: 16"));

        element.sendKeys("" + Keys.DELETE);
        assertEquals("", element.getAttribute("value"));
        assertNull(element.getDomAttribute("value"));
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
        assertNull(log.getDomAttribute("value"));
        assertEquals("", log.getDomProperty("value"));

        input.sendKeys("b");
        assertEquals(getExpectedAlerts()[0], getValueText(log).replace('\n', ' '));
        assertNull(getValueDomAttributeText(log));
        assertEquals(getExpectedAlerts()[0], getValueDomPropertyText(log).replace('\n', ' '));
        log.clear();

        input.sendKeys("a");

        assertEquals(getExpectedAlerts()[1], getValueText(log).replace('\n', ' '));
        assertNull(getValueDomAttributeText(log));
        assertEquals(getExpectedAlerts()[1], getValueDomPropertyText(log).replace('\n', ' '));
    }

    private static String getValueText(final WebElement el) {
        // Standardize on \n and strip any trailing whitespace.
        return el.getAttribute("value").replace("\r\n", "\n").trim();
    }

    private static String getValueDomAttributeText(final WebElement el) {
        final String attrib = el.getDomAttribute("value");
        if (attrib == null) {
            return attrib;
        }

        // Standardize on \n and strip any trailing whitespace.
        return attrib.replace("\r\n", "\n").trim();
    }

    private static String getValueDomPropertyText(final WebElement el) {
        // Standardize on \n and strip any trailing whitespace.
        return el.getDomProperty("value").replace("\r\n", "\n").trim();
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
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
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
        assertEquals("'World' should not be appended.", "Hello", input.getDomAttribute("value"));
        assertEquals("'World' should be appended.", "HelloWorld", input.getDomProperty("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABCd")
    @BuggyWebDriver(FF = "ABCD", FF_ESR = "ABCD")
    public void sendKeysChordShiftAndAutoRelease() throws Exception {
        final String html = "<html>\n"
                + "<body><input type='text' id='t'/>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // SHIFT is held down only for "abc", then auto-released so "d" remains lowercase
        t.sendKeys(Keys.chord(Keys.SHIFT, "abc"), "d");

        assertEquals(getExpectedAlerts()[0], t.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Replaced")
    @BuggyWebDriver(FF = "Initial Text", FF_ESR = "Initial Text")
    public void sendKeysChordSelectAllAndReplace() throws Exception {
        final String html = "<html>\n"
                + "<body><input type='text' id='t' value='Initial Text'/>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // CTRL+A selects all text, followed immediately by replacement text
        t.sendKeys(Keys.chord(Keys.CONTROL, "a"), "Replaced");

        assertEquals(getExpectedAlerts()[0], t.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void sendKeysChordSelectAllAndDelete() throws Exception {
        final String html = "<html>\n"
                + "<body><input type='text' id='t' value='Clear Me'/>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // Select all text using CTRL+A chord and hit Backspace
        t.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);

        assertEquals(getExpectedAlerts()[0], t.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("HELLO x WORLD")
    @BuggyWebDriver(FF = "HELLO x world", FF_ESR = "HELLO x world")
    public void sendKeysMultipleChords() throws Exception {
        final String html = "<html>\n"
                + "<body><input type='text' id='t'/>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // Verifies separate chord sequences in a single sendKeys call
        t.sendKeys(Keys.chord(Keys.SHIFT, "hello"), " x ", Keys.chord(Keys.SHIFT, "world"));

        assertEquals(getExpectedAlerts()[0], t.getAttribute("value"));
    }

    /**
     * Verifies that HOME in a textarea moves the caret to the start of the
     * current line, not the start of the whole text.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nXline2")
    public void textareaHomeMovesToLineStart() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <textarea id='t'></textarea>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // type two lines, move to start of second line with HOME, insert 'X'
        t.sendKeys("line1\nline2", Keys.HOME, "X");

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that Shift+HOME in a textarea selects from the caret position
     * back to the start of the current line (typing replaces selection).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nX")
    public void textareaShiftHome() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <textarea id='t'></textarea>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // type two lines, Shift+HOME selects "line2", "X" replaces selection
        t.sendKeys("line1\nline2", Keys.chord(Keys.SHIFT, Keys.HOME), "X");

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies selectionStart and selectionEnd bounds after Shift+HOME in a textarea.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"6", "11"})
    public void textareaShiftHomeSelectionRange() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    log(t.selectionStart);\n"
            + "    log(t.selectionEnd);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <textarea id='t'></textarea>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        t.sendKeys("line1\nline2", Keys.chord(Keys.SHIFT, Keys.HOME));

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that HOME in a textarea handles \r\n line breaks correctly.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "line1\\nXline2",
            FF = "line1\\n\\nXline2",
            FF_ESR = "line1\\n\\nXline2")
    @HtmlUnitNYI(
            CHROME = "line1\\r\\nXline2",
            EDGE = "line1\\r\\nXline2",
            FF = "line1\\r\\nXline2",
            FF_ESR = "line1\\r\\nXline2")
    public void textareaHomeCarriageReturn() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <textarea id='t'></textarea>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        t.sendKeys("line1\r\nline2", Keys.HOME, "X");

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that HOME in a simple text input moves the caret to the start of the field.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Xhello")
    public void inputHome() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input id='t' type='text'>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        t.sendKeys("hello", Keys.HOME, "X");

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that Shift+HOME in a simple text input selects to the start of the field.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("X")
    public void inputShiftHome() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input id='t' type='text'>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // Shift+HOME selects "hello", typing "X" replaces the entire selection
        t.sendKeys("hello", Keys.chord(Keys.SHIFT, Keys.HOME), "X");

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+HOME in a textarea moves the caret to the very start
     * of the text (across all lines).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Xline1\\nline2")
    public void textareaCtrlHomeMovesToDocumentStart() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <textarea id='t'></textarea>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("line1\nline2")
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.HOME)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+END in a textarea moves the caret to the very end
     * of the document.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nline2X")
    public void textareaCtrlEndMovesToDocumentEnd() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <textarea id='t'></textarea>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("line1\nline2")
                .sendKeys(Keys.HOME) // moves to start of line2
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.END)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+SHIFT+HOME in a textarea selects from the current caret position
     * back to the very start of the entire document.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("X")
    public void textareaCtrlShiftHomeSelectsToDocumentStart() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <textarea id='t'></textarea>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("line1\nline2")
                .keyDown(Keys.CONTROL)
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.HOME)
                .keyUp(Keys.SHIFT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+SHIFT+END in a textarea selects from the current caret position
     * to the very end of the entire document.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("X")
    public void textareaCtrlShiftEndSelectsToDocumentEnd() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <textarea id='t'></textarea>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("line1\nline2")
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.HOME) // caret at pos 0
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.END)
                .keyUp(Keys.SHIFT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+HOME in a text input moves the caret to the start of the text.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Xhello")
    public void inputCtrlHomeMovesToStart() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input id='t' type='text'>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("hello")
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.HOME)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+END in a text input moves the caret to the end of the text.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("helloX")
    public void inputCtrlEndMovesToEnd() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input id='t' type='text'>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("hello")
                .sendKeys(Keys.HOME)
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.END)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }
}
