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
package org.htmlunit.selenium.htmlunitdriver;

import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.selenium.SeleniumTest;
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
public class AdditionalTypingTest extends SeleniumTest {

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
    @BuggyWebDriver(FF = "HELLO X world", FF_ESR = "HELLO X world")
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
     * Verifies that SHIFT+HOME selects from the caret position back to the line start.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nX345")
    public void textareaShiftHomeSelectsToLineStart() throws Exception {
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
                .sendKeys("line1\n12345")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT) // caret at '3' on line 2
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.HOME)
                .keyUp(Keys.SHIFT)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that HOME without SHIFT collapses any existing selection to the start of the line.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nX12345")
    public void textareaHomeCollapsesSelectionToLineStart() throws Exception {
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
                .sendKeys("line1\n12345")
                .sendKeys(Keys.HOME)
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT) // select 'li'
                .keyUp(Keys.SHIFT)
                .sendKeys(Keys.HOME) // unshifted HOME collapses selection to 0
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

    /**
     * Verifies CTRL+SHIFT+END selects from the current caret position to the
     * end of the input and that typing replaces the selection.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aX")
    public void ctrlShiftEndSelectsToEnd() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // type 'abcd', go to start, move one right, select to end, replace
        new Actions(driver)
                .click(t)
                .sendKeys("abcd")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_RIGHT)
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.END)
                .keyUp(Keys.SHIFT)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies CTRL+SHIFT+END selects from the current caret position to the
     * end of the input and that typing replaces the selection.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aX")
    public void inputCtrlShiftEndSelectsToEnd() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // type 'abcd', go to start, move one right, select to end with Ctrl+Shift+END, replace with 'X'
        new Actions(driver)
                .click(t)
                .sendKeys("abcd")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_RIGHT)
                .keyDown(Keys.CONTROL)
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
     * Verifies SHIFT+END in a textarea selects from the current caret position
     * to the end of the current line only.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcd\\nlX")
    public void textareaShiftEndSelectsToLineEnd() throws Exception {
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
                .sendKeys("abcd\nline2")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_RIGHT)
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.END)
                .keyUp(Keys.SHIFT)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+BACKSPACE in a textarea deletes the preceding word.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nhello\\s")
    public void textareaCtrlBackspaceDeletesWord() throws Exception {
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
                .sendKeys("line1\nhello world")
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .keyUp(Keys.CONTROL)
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+BACKSPACE with trailing spaces removes spaces and the word preceding them.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello\\s")
    public void inputCtrlBackspaceWithTrailingSpaces() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("hello world   ")
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.BACK_SPACE)
                .keyUp(Keys.CONTROL)
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+DELETE deletes the word following the caret.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello\\sworld")
    public void inputCtrlDeleteDeletesNextWord() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("deleteMe world")
                .sendKeys(Keys.HOME)
                .sendKeys("hello ") // position caret right before 'deleteMe'
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .keyUp(Keys.CONTROL)
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that Ctrl+RIGHT moves the caret to the end of the current/next word,
     * without skipping an extra character when the caret is already at a space.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello\\s_world")
    public void inputCtrlRightFromSpace() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    var t = document.getElementById('t');\n"
            + "    log(t.value.substring(0, t.selectionStart) + '_' "
            +          "+ t.value.substring(t.selectionStart));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t' value='hello world'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // place caret after "hello" (position 5), which is on the space
        t.sendKeys(Keys.HOME);
        for (int i = 0; i < 5; i++) {
            t.sendKeys(Keys.ARROW_RIGHT);
        }

        // Ctrl+Right from a space should land at end of "world" (position 11)
        // the off-by-one bug causes it to land at position 10 ("worl_d") instead
        t.sendKeys(Keys.chord(Keys.CONTROL, Keys.ARROW_RIGHT));

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that SHIFT+LEFT in an input extends the selection leftward,
     * and typing replaces the selected text.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcXf")
    public void inputShiftLeftExtendsSelection() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // type "abcdef", move left once (caret before 'f'), then SHIFT+LEFT twice
        // to select "cd", then replace with "X"
        new Actions(driver)
                .click(t)
                .sendKeys("abcdef")
                .sendKeys(Keys.ARROW_LEFT)
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.ARROW_LEFT)
                .sendKeys(Keys.ARROW_LEFT)
                .keyUp(Keys.SHIFT)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that SHIFT+LEFT in a textarea extends the selection leftward,
     * and typing replaces the selected text.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcXf")
    public void textareaShiftLeftExtendsSelection() throws Exception {
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
                .sendKeys("abcdef")
                .sendKeys(Keys.ARROW_LEFT)
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.ARROW_LEFT)
                .sendKeys(Keys.ARROW_LEFT)
                .keyUp(Keys.SHIFT)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+DELETE in an input deletes only the word after the caret,
     * without consuming trailing whitespace after the word.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello\\s\\sworld")
    public void inputCtrlDeleteStopsAtWordBoundary() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        // "hello  delete  world" - caret placed before "delete"
        // CTRL+DELETE should remove "delete" only, leaving both surrounding spaces intact
        new Actions(driver)
                .click(t)
                .sendKeys("hello  delete  world")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_RIGHT) // skip "hello"
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT) // now at "delete"
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .keyUp(Keys.CONTROL)
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+DELETE in a textarea deletes only the word after the caret,
     * without consuming trailing whitespace after the word.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello\\s\\sworld")
    public void textareaCtrlDeleteStopsAtWordBoundary() throws Exception {
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
                .sendKeys("hello  delete  world")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.DELETE)
                .keyUp(Keys.CONTROL)
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+LEFT in a text input moves the caret back to the start of the preceding word.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello\\sXworld")
    public void inputCtrlArrowLeftJumpsWord() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("hello world")
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.LEFT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that multiple CTRL+LEFT key presses jump back word-by-word.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("one\\sXtwo\\sthree")
    public void inputCtrlArrowLeftMultipleJumps() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("one two three")
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.LEFT)
                .sendKeys(Keys.LEFT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+LEFT jumps past trailing spaces to the start of the word.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello\\sXworld\\s\\s\\s")
    public void inputCtrlArrowLeftWithTrailingSpaces() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("hello world   ")
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.LEFT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+LEFT works in a textarea element.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nhello\\sXworld")
    public void textareaCtrlArrowLeftJumpsWord() throws Exception {
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
                .sendKeys("line1\nhello world")
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.LEFT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies CTRL+SHIFT+LEFT selects the preceding word so that typing replaces it.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello\\sX")
    public void inputCtrlShiftArrowLeftSelectsWord() throws Exception {
        final String html ="<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("hello world")
                .keyDown(Keys.CONTROL)
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.LEFT)
                .keyUp(Keys.SHIFT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+RIGHT in a text input jumps forward to the start of the next word.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("one\\sXtwo\\sthree")
    public void sendKeys_inputCtrlArrowRightJumpsWord() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("one two three")
                .sendKeys(Keys.HOME)
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.RIGHT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that multiple CTRL+RIGHT key presses jump forward word-by-word.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("one\\stwo\\sXthree")
    public void inputCtrlArrowRightMultipleJumps() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("one two three")
                .sendKeys(Keys.HOME)
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.RIGHT)
                .sendKeys(Keys.RIGHT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that CTRL+SHIFT+RIGHT selects the next word so that typing replaces it.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Xworld")
    public void inputCtrlShiftArrowRightSelectsWord() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("hello world")
                .sendKeys(Keys.HOME)
                .keyDown(Keys.CONTROL)
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.RIGHT)
                .keyUp(Keys.SHIFT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies CTRL+RIGHT in a textarea across line boundaries.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nthree\\sXfour")
    public void textareaCtrlArrowRightJumpsWord() throws Exception {
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
                .sendKeys("line1\nthree four")
                .sendKeys(Keys.HOME) // moves to start of line 2 ('three')
                .keyDown(Keys.CONTROL)
                .sendKeys(Keys.RIGHT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies CTRL+SHIFT+RIGHT in a textarea selects the next word.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nXfour")
    public void textareaCtrlShiftArrowRightSelectsWord() throws Exception {
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
                .sendKeys("line1\nthree four")
                .sendKeys(Keys.HOME) // moves to start of line 2
                .keyDown(Keys.CONTROL)
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.RIGHT)
                .keyUp(Keys.SHIFT)
                .keyUp(Keys.CONTROL)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that ARROW_UP in a textarea moves the caret to the previous line at the same column position.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abXcde\\n12345")
    public void textareaArrowUpMovesToPreviousLine() throws Exception {
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

        // type 'abcde\n12345', move caret to start of line 2, move 2 right (col 2), UP, type 'X'
        new Actions(driver)
                .click(t)
                .sendKeys("abcde\n12345")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_UP)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that ARROW_UP moves caret to the end of the previous line if that line is shorter.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abX\\n12345")
    public void textareaArrowUpShorterPreviousLine() throws Exception {
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
                .sendKeys("ab\n12345")
                .sendKeys(Keys.ARROW_UP)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that ARROW_UP on the first line moves the caret to index 0.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Xfirst\\sline")
    public void textareaArrowUpFirstLineMovesToStart() throws Exception {
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
                .sendKeys("first line")
                .sendKeys(Keys.ARROW_UP)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that SHIFT+ARROW_UP selects text up to the same column on the previous line.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line1\\nliX345")
    public void textareaShiftArrowUpSelectsLine() throws Exception {
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
                .sendKeys("line1\nline2\n12345")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT) // col 2 on '12345'
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.ARROW_UP)
                .keyUp(Keys.SHIFT)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that ARROW_UP in a single-line input moves the caret to index 0.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Xhello")
    public void inputArrowUpMovesToStart() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("hello")
                .sendKeys(Keys.ARROW_UP)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that ARROW_DOWN in a textarea moves the caret to the next line at the same column position.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12345\\nabXcde")
    public void textareaArrowDownMovesToNextLine() throws Exception {
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

        // type '12345\nabcde', HOME, move 2 right (col 2), DOWN, type 'X'
        new Actions(driver)
                .click(t)
                .sendKeys("12345\nabcde")
                .sendKeys(Keys.HOME) // moves to start of line 2
                .sendKeys(Keys.ARROW_UP) // moves to start of line 1
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT) // column 2 on '12345'
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that ARROW_DOWN moves caret to the end of the next line if that line is shorter.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12345\\nabX")
    public void textareaArrowDownShorterNextLine() throws Exception {
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
                .sendKeys("12345\nab")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_UP) // line 1 start
                .sendKeys(Keys.END) // end of line 1 (col 5)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that ARROW_DOWN on the last line moves the caret to the end of the text.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("last\\slineX")
    public void textareaArrowDownLastLineMovesToEnd() throws Exception {
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
                .sendKeys("last line")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that SHIFT+ARROW_DOWN selects text down to the same column on the next line.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("12Xne2\\nline3")
    public void textareaShiftArrowDownSelectsLine() throws Exception {
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
                .sendKeys("12345\nline2\nline3")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_UP)
                .sendKeys(Keys.ARROW_UP) // start of line 1
                .sendKeys(Keys.ARROW_RIGHT)
                .sendKeys(Keys.ARROW_RIGHT) // col 2 on line 1
                .keyDown(Keys.SHIFT)
                .sendKeys(Keys.ARROW_DOWN)
                .keyUp(Keys.SHIFT)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that ARROW_DOWN in a single-line input moves the caret to the end.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("helloX")
    public void inputArrowDownMovesToEnd() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function test() {\n"
            + "    log(document.getElementById('t').value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='text' id='t'/>\n"
            + "  <button id='clickMe' onclick='test()'>do it</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement t = driver.findElement(By.id("t"));

        new Actions(driver)
                .click(t)
                .sendKeys("hello")
                .sendKeys(Keys.HOME)
                .sendKeys(Keys.ARROW_DOWN)
                .sendKeys("X")
                .perform();

        driver.findElement(By.id("clickMe")).click();
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that pressing SPACE on a checkbox toggles its checked state.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "false"})
    public void spaceTogglesCheckbox() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function logState() {\n"
            + "    log(document.getElementById('c').checked);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='checkbox' id='c' onchange='logState()'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement c = driver.findElement(By.id("c"));

        c.sendKeys(Keys.SPACE); // toggles to true
        c.sendKeys(Keys.SPACE); // toggles to false

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that pressing SPACE on a radio button checks it.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void spaceSelectsRadioButton() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "  function logState() {\n"
            + "    log(document.getElementById('r').checked);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='radio' id='r' name='group' onchange='logState()'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement r = driver.findElement(By.id("r"));

        r.sendKeys(Keys.SPACE); // checks radio button

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that pressing SPACE on a button triggers its click event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clicked")
    public void spaceTriggersButton() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "</script>\n"
            + "</head><body>\n"
            + "  <button id='b' onclick='log(\"clicked\")'>Click me</button>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement b = driver.findElement(By.id("b"));

        b.sendKeys(Keys.SPACE);

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that pressing SPACE on an input button triggers its click event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clicked")
    public void spaceTriggersInputButton() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "</script>\n"
            + "</head><body>\n"
            + "  <input type='button' id='b' value='Click me' onclick='log(\"clicked\")'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement b = driver.findElement(By.id("b"));

        b.sendKeys(Keys.SPACE);

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that pressing SPACE on an input submit triggers its click event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clicked")
    public void spaceTriggersInputSubmit() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "</script>\n"
            + "</head><body>\n"
            + "  <form onsubmit='return false;'>\n"
            + "    <input type='submit' id='b' value='Submit' onclick='log(\"clicked\")'/>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement b = driver.findElement(By.id("b"));

        b.sendKeys(Keys.SPACE);

        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * Verifies that pressing SPACE on an input reset triggers its click event.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("clicked")
    public void spaceTriggersInputReset() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION_NORMALIZE
            + "</script>\n"
            + "</head><body>\n"
            + "  <form>\n"
            + "    <input type='reset' id='b' value='Reset' onclick='log(\"clicked\")'/>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement b = driver.findElement(By.id("b"));

        b.sendKeys(Keys.SPACE);

        verifyTitle2(driver, getExpectedAlerts());
    }
}