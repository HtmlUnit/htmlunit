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
package org.htmlunit.html;

import java.util.List;

import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.BuggyWebDriver;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link HtmlOption}.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
public class HtmlOption2Test extends WebDriverTestCase {

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"option1", "", "Number Three", "Number 4",
                       "option1\nNumber Three\nNumber 4"},
            CHROME = {"option1", "", "Number Three", "Number 4",
                      "      option1\n       Number Three\n      Number 4\n    "},
            EDGE = {"option1", "", "Number Three", "Number 4",
                    "      option1\n       Number Three\n      Number 4\n    "})
    @HtmlUnitNYI(CHROME = {"option1", "", "Number Three", "Number 4",
                           "option1\nNumber Three\nNumber 4"},
            EDGE = {"option1", "", "Number Three", "Number 4",
                    "option1\nNumber Three\nNumber 4"})
    public void getVisibleText() throws Exception {
        final String htmlContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body id='tester'>\n"
            + "  <form>\n"
            + "    <select>\n"
            + "      <option id='option1'>option1</option>\n"
            + "      <option id='option2' label='Number Two'/>\n"
            + "      <option id='option3' label='overridden'>Number Three</option>\n"
            + "      <option id='option4'>Number&nbsp;4</option>\n"
            + "    </select>\n"
            + "  </form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        String text = driver.findElement(By.id("option1")).getText();
        assertEquals(getExpectedAlerts()[0], text);
        text = driver.findElement(By.id("option2")).getText();
        assertEquals(getExpectedAlerts()[1], text);
        text = driver.findElement(By.id("option3")).getText();
        assertEquals(getExpectedAlerts()[2], text);
        text = driver.findElement(By.id("option4")).getText();
        assertEquals(getExpectedAlerts()[3], text);
        text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[4], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertEquals(getExpectedAlerts()[4], page.getElementById("tester").getVisibleText());
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLOptionElement]")
    public void simpleScriptable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<select>\n"
            + "  <option id='myId'>test1</option>\n"
            + "  <option id='myId2'>test2</option>\n"
            + "</select>\n"
            + "</body></html>";

        final WebDriver driver = loadPageVerifyTitle2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getEnclosedPage();
            assertTrue(page.getHtmlElementById("myId") instanceof HtmlOption);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"oDown", "sDown", "dDown", "oUp", "sUp", "dUp"})
    // there seems to be a bug in selenium; for FF >= 10 this triggers
    // "sDown,dDown,sUp,dUp,oDown,sDown,dDown,oUp,sUp,dUp," but a
    // manual test shows, that this is wrong.
    // for Chrome selenium shows only "sUp,dUp," but again
    // manual test are showing something different
    @BuggyWebDriver(CHROME = {"sUp", "dUp"},
                    EDGE = {"sUp", "dUp"},
                    FF = {"sDown", "dDown", "sUp", "dUp"},
                    FF_ESR = {"sDown", "dDown", "sUp", "dUp"})
    public void onMouse() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><title>foo</title>\n"
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "</script>\n"
            + "</head><body>\n"
            + "  <form>\n"
            + "    <div"
                    + " onMouseDown='log(\"dDown\");'"
                    + " onMouseUp='log(\"dUp\");'>\n"
            + "    <select name='select1' size='4'"
                        + " onMouseDown='log(\"sDown\");'"
                        + " onMouseUp='log(\"sUp\");'>\n"
            + "      <option id='opt1' value='option1'>Option1</option>\n"
            + "      <option id='opt2' value='option2' selected='selected'>Option2</option>\n"
            + "      <option id='opt3' value='option3'"
                        + " onMouseDown='log(\"oDown\");'"
                        + " onMouseUp='log(\"oUp\");'>Option3</option>\n"
            + "    </select>\n"
            + "    </div>\n"
            + "  </form>\n"
            + LOG_TEXTAREA
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("opt3")).click();

        verifyTextArea2(driver, getExpectedAlerts());
    }

    /**
     * Test case for #1864.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void isSelected() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><body>"
                + "  <select multiple><option value='a'>a</option><option value='b'>b</option></select>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement select = driver.findElement(By.tagName("select"));

        final List<WebElement> options = select.findElements(By.tagName("option"));
        for (final WebElement option : options) {
            option.click();
        }

        for (final WebElement option : options) {
            assertTrue(option.isSelected());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void isSelectedJavaScript() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + "  function test() {\n"
                + "    var s = document.getElementsByTagName('select').item(0);\n"
                + "    var options = s.options;\n"
                + "    for (var i = 0; i < options.length; i++) {\n"
                + "      options[i].selected = true;\n"
                + "    }\n"
                + "  }\n"
                + "</script>\n"
                + "</head><body onload='test()'>\n"
                + "  <select multiple><option value='a'>a</option><option value='b'>b</option></select>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final WebElement select = driver.findElement(By.tagName("select"));

        final List<WebElement> options = select.findElements(By.tagName("option"));

        for (final WebElement option : options) {
            assertTrue(option.isSelected());
        }
    }
}
