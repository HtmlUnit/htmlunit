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
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.selenium.SeleniumTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * Tests for the tab key navigation (moving the focus between elements)
 * when typing using the driver.
 *
 * @author Ronald Brill
 */
public class TabNavigationTest extends SeleniumTest {

    /**
     * Logs the focus and blur events of the elements, the keydown and keyup events of the
     * tab key (with the id of the target) and provides report() to log the values of all
     * fields and the id of the active element.
     */
    private static final String LOG_FOCUS = "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function targetName(e) { return e.target.id || e.target.nodeName; }\n"
            + "function handler(e) {\n"
            + "  if (!e.target.tagName) {\n"
            + "    return;\n"
            + "  }\n"
            + "  if (e.type == 'focus' || e.type == 'blur') {\n"
            + "    log(e.type + ':' + targetName(e));\n"
            + "  }\n"
            + "  else if (e.key == 'Tab') {\n"
            + "    log(e.type + ':' + targetName(e) + ',' + e.shiftKey);\n"
            + "  }\n"
            + "}\n"
            + "['focus', 'blur'].forEach(function(t) { document.addEventListener(t, handler, true); });\n"
            + "['keydown', 'keyup'].forEach(function(t) { document.addEventListener(t, handler); });\n"
            + "function report() {\n"
            + "  var fields = document.querySelectorAll('input,textarea');\n"
            + "  for (var i = 0; i < fields.length; i++) {\n"
            + "    var f = fields[i];\n"
            + "    log(f.id + '=' + (f.type == 'checkbox' ? f.checked : f.value));\n"
            + "  }\n"
            + "  var active = document.activeElement;\n"
            + "  log('active:' + (active ? active.id || active.nodeName : null));\n"
            + "}\n"
            + "</script>\n";

    /**
     * Tab moves the focus to the next field; the following keys are typed into that field.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false", "blur:t1", "focus:t2", "keyup:t2,false", "t1=first", "t2=second", "active:t2"})
    public void tabToNextField() throws Exception {
        final WebDriver driver = loadPage("<input id='t1'>\n<input id='t2'>\n");
        driver.findElement(By.id("t1")).sendKeys("first", Keys.TAB, "second");
        verifyReport(driver);
    }

    /**
     * Tab into a field with a value; the value gets selected and is replaced by typing.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false", "blur:t1", "focus:t2", "keyup:t2,false", "t1=", "t2=new", "active:t2"})
    public void tabSelectsValue() throws Exception {
        final WebDriver driver = loadPage("<input id='t1'>\n<input id='t2' value='old'>\n");
        driver.findElement(By.id("t1")).sendKeys(Keys.TAB, "new");
        verifyReport(driver);
    }

    /**
     * Shift+Tab moves the focus to the previous field.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"focus:t2", "keydown:t2,true", "blur:t2", "focus:t1", "keyup:t1,true", "t1=y", "t2=x", "active:t1"},
            FF = {"focus:t2", "keydown:t2,true", "blur:t2", "focus:t1", "keyup:t1,true", "t1=Y", "t2=x", "active:t1"},
            FF_ESR = {"focus:t2", "keydown:t2,true", "blur:t2", "focus:t1", "keyup:t1,true", "t1=Y", "t2=x", "active:t1"})
    @HtmlUnitNYI(FF = {"focus:t2", "keydown:t2,true", "blur:t2", "focus:t1", "keyup:t1,true", "t1=y", "t2=x", "active:t1"},
            FF_ESR = {"focus:t2", "keydown:t2,true", "blur:t2", "focus:t1", "keyup:t1,true", "t1=y", "t2=x", "active:t1"})
    public void shiftTabToPreviousField() throws Exception {
        final WebDriver driver = loadPage("<input id='t1' value='first'>\n<input id='t2'>\n");
        driver.findElement(By.id("t2")).sendKeys("x", Keys.chord(Keys.SHIFT, Keys.TAB), "y");
        verifyReport(driver);
    }

    /**
     * Several tabs in one sendKeys call.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false", "blur:t1", "focus:t2", "keyup:t2,false", "keydown:t2,false",
             "blur:t2", "focus:t3", "keyup:t3,false", "t1=1", "t2=2", "t3=3", "active:t3"})
    public void multipleTabs() throws Exception {
        final WebDriver driver = loadPage("<input id='t1'>\n<input id='t2'>\n<input id='t3'>\n");
        driver.findElement(By.id("t1")).sendKeys("1", Keys.TAB, "2", Keys.TAB, "3");
        verifyReport(driver);
    }

    /**
     * Disabled fields and fields with a negative tabindex are skipped.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false", "blur:t1", "focus:t5", "keyup:t5,false", "t1=", "t2=", "t3=", "t4=", "t5=x", "active:t5"})
    public void tabSkipsDisabledAndNegativeTabIndex() throws Exception {
        final WebDriver driver = loadPage("<input id='t1'>\n"
                + "<input id='t2' disabled>\n"
                + "<input id='t3' tabindex='-1'>\n"
                + "<input id='t4' type='hidden'>\n"
                + "<input id='t5'>\n");
        driver.findElement(By.id("t1")).sendKeys(Keys.TAB, "x");
        verifyReport(driver);
    }

    /**
     * Fields with a positive tabindex come first, in the order of their tabindex.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t3", "keydown:t3,false", "blur:t3", "focus:t1", "keyup:t1,false", "keydown:t1,false",
             "blur:t1", "focus:t2", "keyup:t2,false", "t1=b", "t2=c", "t3=a", "active:t2"})
    public void tabIndexOrder() throws Exception {
        final WebDriver driver = loadPage("<input id='t1' tabindex='2'>\n"
                + "<input id='t2'>\n"
                + "<input id='t3' tabindex='1'>\n");
        driver.findElement(By.id("t3")).sendKeys("a", Keys.TAB, "b", Keys.TAB, "c");
        verifyReport(driver);
    }

    /**
     * Tab from the last field of the page.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t2", "keydown:t2,false", "blur:t2", "keyup:BODY,false", "t1=", "t2=", "active:BODY"})
    public void tabFromLastField() throws Exception {
        final WebDriver driver = loadPage("<input id='t1'>\n<input id='t2'>\n");
        driver.findElement(By.id("t2")).sendKeys(Keys.TAB, "x");
        verifyReport(driver);
    }

    /**
     * Tab into a textarea.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false", "blur:t1", "focus:t2", "keyup:t2,false", "t1=", "t2=newold", "active:t2"})
    public void tabIntoTextArea() throws Exception {
        final WebDriver driver = loadPage("<input id='t1'>\n<textarea id='t2'>old</textarea>\n");
        driver.findElement(By.id("t1")).sendKeys(Keys.TAB, "new");
        verifyReport(driver);
    }

    /**
     * Tab in a textarea moves the focus (no tab char is inserted).
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false", "blur:t1", "focus:t2", "keyup:t2,false", "t1=a", "t2=b", "active:t2"})
    public void tabFromTextArea() throws Exception {
        final WebDriver driver = loadPage("<textarea id='t1'></textarea>\n<input id='t2'>\n");
        driver.findElement(By.id("t1")).sendKeys("a", Keys.TAB, "b");
        verifyReport(driver);
    }

    /**
     * Tab into a checkbox and toggle it using space.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false", "blur:t1", "focus:c1", "keyup:c1,false", "t1=", "c1=true", "active:c1"})
    public void tabIntoCheckbox() throws Exception {
        final WebDriver driver = loadPage("<input id='t1'>\n<input id='c1' type='checkbox'>\n");
        driver.findElement(By.id("t1")).sendKeys(Keys.TAB, Keys.SPACE);
        verifyReport(driver);
    }

    /**
     * The focus does not move if the keydown event of the tab key is canceled.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false§keyup:t1,false§t1=ab§t2=§active:t1"})
    public void tabPrevented() throws Exception {
        final WebDriver driver = loadPage("<input id='t1' "
                + "onkeydown='if (event.key == \"Tab\") event.preventDefault();'>\n"
                + "<input id='t2'>\n");
        driver.findElement(By.id("t1")).sendKeys("a", Keys.TAB, "b");
        verifyReport(driver);
    }

    /**
     * Tab using the Actions API; the following keys are typed into the newly focused field.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false", "blur:t1", "focus:t2", "keyup:t2,false", "t1=a", "t2=b", "active:t2"})
    public void actionsTab() throws Exception {
        final WebDriver driver = loadPage("<input id='t1'>\n<input id='t2'>\n");
        driver.findElement(By.id("t1")).click();
        new Actions(driver).sendKeys("a").keyDown(Keys.TAB).keyUp(Keys.TAB).sendKeys("b").perform();
        verifyReport(driver);
    }

    /**
     * Shift+Tab using the Actions API.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t2", "keydown:t2,true", "blur:t2", "focus:t1", "keyup:t1,true", "t1=x", "t2=", "active:t1"})
    public void actionsShiftTab() throws Exception {
        final WebDriver driver = loadPage("<input id='t1' value='first'>\n<input id='t2'>\n");
        driver.findElement(By.id("t2")).click();
        new Actions(driver).keyDown(Keys.SHIFT).sendKeys(Keys.TAB).keyUp(Keys.SHIFT).sendKeys("x").perform();
        verifyReport(driver);
    }

    /**
     * A second sendKeys call to the original element types into this element again.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"focus:t1", "keydown:t1,false", "blur:t1", "focus:t2", "keyup:t2,false",
             "blur:t2", "focus:t1", "t1=ab", "t2=", "active:t1"})
    public void sendKeysAfterTab() throws Exception {
        final WebDriver driver = loadPage("<input id='t1'>\n<input id='t2'>\n");
        driver.findElement(By.id("t1")).sendKeys("a", Keys.TAB);
        driver.findElement(By.id("t1")).sendKeys("b");
        verifyReport(driver);
    }

    private WebDriver loadPage(final String fields) throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + LOG_FOCUS
                + "</head><body>\n"
                + fields
                + "</body></html>";
        return loadPage2(html);
    }

    private void verifyReport(final WebDriver driver) throws Exception {
        ((JavascriptExecutor) driver).executeScript("report();");
        verifyTitle2(driver, getExpectedAlerts());
    }
}
