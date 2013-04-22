/*
 * Copyright (c) 2002-2013 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.CHROME;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlCheckBoxInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlCheckBoxInput2Test extends WebDriverTestCase {

    /**
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" },
            IE = { "true", "false", "false", "false", "false", "false" },
            IE6 = { "true", "false", "false", "false", "true", "true" })
    public void checked_appendChild() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var input = document.createElement('input');\n"
            + "      input.type = 'checkbox';\n"
            + "      input.checked = true;\n"
            + "      alert(input.checked);\n"
            + "      var parent=document.getElementById('myDiv');\n"
            + "      parent.appendChild(input);\n"
            + "      alert(input.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "\n"
            + "      input.defaultChecked = true;\n"
            + "      alert(input.checked);\n"
            + "      parent.appendChild(input);\n"
            + "      alert(input.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form><div id='myDiv'></div></div></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_appendChild() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var input = document.createElement('input');\n"
            + "      input.type = 'checkbox';\n"
            + "      alert(input.checked);\n"
            + "      var parent=document.getElementById('myDiv');\n"
            + "      parent.appendChild(input);\n"
            + "      alert(input.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "\n"
            + "      input.defaultChecked = true;\n"
            + "      alert(input.checked);\n"
            + "      parent.appendChild(input);\n"
            + "      alert(input.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form><div id='myDiv'></div></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "false", "false", "false", "true", "true", "true" },
            IE = { "false", "false", "false", "false", "false", "false" },
            IE6 = { "false", "false", "false", "false", "true", "true" })
    public void notchecked_insertBefore() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var input = document.createElement('input');\n"
            + "      input.type = 'checkbox';\n"
            + "      alert(input.checked);\n"
            + "      var parent=document.getElementById('myDiv');\n"
            + "      var after=document.getElementById('divAfter');\n"
            + "      parent.insertBefore(input, after);\n"
            + "      alert(input.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "\n"
            + "      input.defaultChecked = true;\n"
            + "      alert(input.checked);\n"
            + "      parent.insertBefore(input, after);\n"
            + "      alert(input.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form><div id='myDiv'><div id='divAfter'></div></div></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" },
            IE = { "true", "false", "false", "false", "false", "false" },
            IE6 = { "true", "false", "false", "false", "true", "true" })
    public void checked_insertBefore() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var input = document.createElement('input');\n"
            + "      input.type = 'checkbox';\n"
            + "      input.checked = true;\n"
            + "      alert(input.checked);\n"
            + "      var parent=document.getElementById('myDiv');\n"
            + "      var after=document.getElementById('divAfter');\n"
            + "      parent.insertBefore(input, after);\n"
            + "      alert(input.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "\n"
            + "      input.defaultChecked = true;\n"
            + "      alert(input.checked);\n"
            + "      parent.insertBefore(input, after);\n"
            + "      alert(input.checked);\n"
            + "      parent.removeChild(input);\n"
            + "      alert(input.checked);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <form><div id='myDiv'><div id='divAfter'></div></div></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "foo,change,", IE = { })
    public void onchangeFires() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='checkbox' id='chkbox' onchange='debug(\"foo\");debug(event.type);'>\n"
            + "</form>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("chkbox")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = "foo,change,", DEFAULT = "foo,change,boo,blur,")
    @NotYetImplemented(CHROME)
    public void onchangeFires2() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='checkbox' id='chkbox'"
            + " onChange='debug(\"foo\");debug(event.type);'"
            + " onBlur='debug(\"boo\");debug(event.type);'"
            + ">\n"
            + "<input type='checkbox' id='chkbox2'>\n"
            + "</form>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("chkbox")).click();
        driver.findElement(By.id("chkbox2")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "First", DEFAULT = "Second")
    public void setChecked() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input id='myCheckbox' type='checkbox' onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("myCheckbox")).click();
        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "First", "Second" }, DEFAULT = "Second")
    public void setChecked2() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input id='myCheckbox' type='checkbox' onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "<input id='myInput' type='text'>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("myCheckbox")).click();
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        if (getBrowserVersion().isIE()) {
            driver.findElement(By.id("myInput")).click();
            assertEquals(getExpectedAlerts()[1], driver.getTitle());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void preventDefault() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function handler(e) {\n"
            + "    if (e)\n"
            + "      e.preventDefault();\n"
            + "    else\n"
            + "      return false;\n"
            + "  }\n"
            + "  function init() {\n"
            + "    document.getElementById('checkbox1').onclick = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "<input type='checkbox' id='checkbox1'/>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox1"));
        checkbox.click();
        assertFalse(checkbox.isSelected());
    }

    /**
     * Verifies that a HtmlCheckBox is unchecked by default.
     * The onClick tests make this assumption.
     * @throws Exception if the test fails
     */
    @Test
    public void defaultState() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "<form id='form1'>\n"
            + "    <input type='checkbox' name='checkbox' id='checkbox'>Check me</input>\n"
            + "</form></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("checkbox"));
        assertFalse(checkbox.isSelected());
    }

    /**
     * Call to JS function click() should trigger the onchange handler but neither the onfocus handler
     * nor the mousedown/up handlers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("changed")
    public void clickShouldTriggerOnchange() throws Exception {
        final String html = "<html><body>\n"
            + "<input type='checkbox' id='it' onchange='alert(\"changed\")'"
            + "onmousedown='alert(\"down\")' onmouseup='alert(\"up\")' onfocus='alert(\"focused\")'>Check me\n"
            + "<script>\n"
            + "var elt = document.getElementById('it');\n"
            + "elt.click();\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
