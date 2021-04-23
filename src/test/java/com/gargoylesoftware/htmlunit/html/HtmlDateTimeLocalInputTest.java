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
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlDateTimeLocalInput}.
 *
 * @author Ronald Brill
 * @author Anton Demydenko
 */
@RunWith(BrowserRunner.class)
public class HtmlDateTimeLocalInputTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"--null", "--null", "--null"},
            IE = {"--null", "exception", "--null"})
    public void defaultValues() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('text1');\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    try {\n"
            + "      input = document.createElement('input');\n"
            + "      input.type = 'datetime-local';\n"
            + "      alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "    } catch(e)  { alert('exception'); }\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"datetime-local\">';\n"
            + "    input = builder.firstChild;\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='datetime-local' id='text1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"--null", "--null", "--null"},
            IE = {"--null", "exception", "--null"})
    public void defaultValuesAfterClone() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('text1');\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"

            + "    try{\n"
            + "      input = document.createElement('input');\n"
            + "      input.type = 'datetime-local';\n"
            + "      input = input.cloneNode(false);\n"
            + "      alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "    } catch(e)  { alert('exception'); }\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"datetime-local\">';\n"
            + "    input = builder.firstChild;\n"
            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value + '-' + input.defaultValue + '-' + input.getAttribute('value'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='datetime-local' id='text1'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies getVisibleText().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void getVisibleText() throws Exception {
        final String htmlContent
            = "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<form id='form1'>\n"
            + "  <input type='datetime-local' name='tester' id='tester' value='2018-06-12T19:30' "
                                    + "min='2018-06-07T00:00' max='2018-06-14T00:00'>\n"
            + "</form>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final String text = driver.findElement(By.id("tester")).getText();
        assertEquals(getExpectedAlerts()[0], text);

        if (driver instanceof HtmlUnitDriver) {
            final HtmlPage page = (HtmlPage) getWebWindowOf((HtmlUnitDriver) driver).getEnclosedPage();
            assertEquals(getExpectedAlerts()[0], page.getBody().getVisibleText());
        }
    }

    /**
     * Verifies clear().
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void clearInput() throws Exception {
        final String htmlContent
                = "<html>\n"
                + "<head></head>\n"
                + "<body>\n"
                + "<form id='form1'>\n"
                + "  <input type='datetime-local' name='tester' id='tester' value='2018-06-12T19:30'>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(htmlContent);
        final WebElement element = driver.findElement(By.id("tester"));

        element.clear();
        assertEquals(getExpectedAlerts()[0], element.getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("--")
    public void minMaxStep() throws Exception {
        final String html
            = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var input = document.getElementById('tester');\n"
            + "    alert(input.min + '-' + input.max + '-' + input.step);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='datetime-local' id='tester'>\n"
            + "</form>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "true-true",
            CHROME = "false-true",
            EDGE = "false-true")
    public void maxValidation() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var foo = document.getElementById('foo');\n"
            + "    var bar = document.getElementById('bar');\n"
            + "    alert(foo.checkValidity() + '-' + bar.checkValidity() );\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='datetime-local' max='2018-12-01T00:00' id='foo' value='2018-12-11T00:00'>\n"
            + "  <input type='datetime-local' max='2018-12-01T00:00' id='bar' value='2018-11-01T00:00'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    @Test
    @Alerts(DEFAULT = "true-true",
            CHROME = "false-true",
            EDGE = "false-true")
    public void minValidation() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var foo = document.getElementById('foo');\n"
            + "    var bar = document.getElementById('bar');\n"
            + "    alert(foo.checkValidity() + '-' + bar.checkValidity() );\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <input type='datetime-local' min='2018-12-01T00:00' id='foo' value='2018-11-01T00:00'>\n"
            + "  <input type='datetime-local' min='2018-12-01T00:00' id='bar' value='2018-12-01T01:00'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }
}
