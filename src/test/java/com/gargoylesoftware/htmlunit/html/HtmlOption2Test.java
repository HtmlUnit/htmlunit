/*
 * Copyright (c) 2002-2018 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.FF;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlOption}.
 *
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlOption2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[object HTMLOptionElement]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<select>\n"
            + "  <option id='myId'>test1</option>\n"
            + "  <option id='myId2'>test2</option>\n"
            + "</select>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertTrue(HtmlOption.class.isInstance(page.getHtmlElementById("myId")));
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "oDown,sDown,dDown,oUp,sUp,dUp,",
            IE = "sDown,dDown,sUp,dUp,")
    // there seems to be a bug in selenium; for FF >= 10 this triggers
    // "sDown,dDown,sUp,dUp,oDown,sDown,dDown,oUp,sUp,dUp," but a
    // manual test shows, that this is wrong.
    // for Chrome selenium shows only "sUp,dUp," but again
    // manual test are showing something different
    @BuggyWebDriver({CHROME, FF})
    public void onMouse() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "  <form>\n"
            + "    <div"
                    + " onMouseDown='debug(\"dDown\");'"
                    + " onMouseUp='debug(\"dUp\");'>\n"
            + "    <select name='select1' size='4'"
                        + " onMouseDown='debug(\"sDown\");'"
                        + " onMouseUp='debug(\"sUp\");'>\n"
            + "      <option id='opt1' value='option1'>Option1</option>\n"
            + "      <option id='opt2' value='option2' selected='selected'>Option2</option>\n"
            + "      <option id='opt3' value='option3'"
                        + " onMouseDown='debug(\"oDown\");'"
                        + " onMouseUp='debug(\"oUp\");'>Option3</option>\n"
            + "    </select>\n"
            + "    </div>\n"
            + "  </form>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("opt3")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * Test case for #1864.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void isSelected() throws Exception {
        final String html = "<html><body>"
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
        final String html = "<html><head>\n"
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
