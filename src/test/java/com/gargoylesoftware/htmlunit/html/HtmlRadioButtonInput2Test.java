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

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlRadioButtonInput}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Benoit Heinrich
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlRadioButtonInput2Test extends WebDriverTestCase {

    /**
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
            + "      input.type = 'radio';\n"
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
            + "      input.type = 'radio';\n"
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
            + "      input.type = 'radio';\n"
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
            + "      input.type = 'radio';\n"
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
     * Verifies the behavior of 'checked' property on being attached to a page.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" })
    public void checked_appendChild_fromHtml() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var builder = document.createElement('div');\n"
            + "      builder.innerHTML = '<input type=\"radio\" checked>';\n"
            + "      var input = builder.firstChild;\n"
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
    public void notchecked_appendChild_fromHtml() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var builder = document.createElement('div');\n"
            + "      builder.innerHTML = '<input type=\"radio\">';\n"
            + "      var input = builder.firstChild;\n"
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
    public void notchecked_insertBefore_fromHtml() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var builder = document.createElement('div');\n"
            + "      builder.innerHTML = '<input type=\"radio\">';\n"
            + "      var input = builder.firstChild;\n"
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
    @Alerts(DEFAULT = { "true", "true", "true", "true", "true", "true" })
    public void checked_insertBefore_fromHtml() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      var builder = document.createElement('div');\n"
            + "      builder.innerHTML = '<input type=\"radio\" checked>';\n"
            + "      var input = builder.firstChild;\n"
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
     * Regression test for bug 2956588.
     * As of HttmlUnit-2.8-SNAPSHOT on 26.02.10, reading responseXML with xhtml namespace
     * was causing ClassCastException for IE simulation when it contained a checked radio button.
     * @throws Exception if the test fails.
     */
    @Test
    @Alerts({ "send request", "response read" })
    public void checkedOnXmlResponse() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>\n"
            + "  alert('send request');\n"
            + "  var xhr = (window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject('Microsoft.XMLHTTP'));\n"
            + "  xhr.open('GET', 'foo.xml', false);\n"
            + "  xhr.send('');\n"
            + "  var x = xhr.responseXML;\n" // this is what caused the exception
            + "  alert('response read');\n"
            + "</script>\n"
            + "</body></html>";

        final String xml
            = "<html xmlns='http://www.w3.org/1999/xhtml'>\n"
            + "<body>\n"
            + "<input type='radio' name='radio' checked='checked'/>"
            + "</body>\n"
            + "</html>";

        getMockWebConnection().setDefaultResponse(xml, "text/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "foo,change,", IE = "")
    public void onchangeFires() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio'"
            + " onchange='debug(\"foo\");debug(event.type);'>Check me</input>\n"
            + "</form>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "foo,change,boo,blur,", CHROME = "foo,change,")
    public void onchangeFires2() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function debug(string) {\n"
            + "    document.getElementById('myTextarea').value += string + ',';\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio1'"
            + " onChange='debug(\"foo\");debug(event.type);'"
            + " onBlur='debug(\"boo\");debug(event.type);'"
            + ">Check Me</input>\n"
            + "<input type='radio' name='radioGroup' id='radio2'>Or Check Me</input>\n"
            + "</form>\n"
            + "<textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("radio1")).click();
        driver.findElement(By.id("radio2")).click();

        assertEquals(Arrays.asList(getExpectedAlerts()).toString(),
                '[' + driver.findElement(By.id("myTextarea")).getAttribute("value") + ']');
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "First", FF = "Second")
    public void setChecked() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio'"
            + " onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("radio")).click();
        assertEquals(getExpectedAlerts()[0], driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "First", "Second" }, FF = "Second")
    public void setChecked2() throws Exception {
        final String firstHtml
            = "<html><head><title>First</title></head><body>\n"
            + "<form>\n"
            + "<input type='radio' name='radioGroup' id='radio'"
            + " onchange=\"window.location.href='" + URL_SECOND + "'\">\n"
            + "<input id='myInput' type='text'>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondHtml
            = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setDefaultResponse(secondHtml);
        final WebDriver driver = loadPage2(firstHtml);

        driver.findElement(By.id("radio")).click();
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
            + "    document.getElementById('radio1').onclick = handler;\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='init()'>\n"
            + "  <input type='radio' id='radio1' name='radio1' />\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement radio = driver.findElement(By.id("radio1"));
        radio.click();
        assertFalse(radio.isSelected());
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
            + "    <input type='radio' name='radio' id='radio'>Check me</input>\n"
            + "</form></body></html>";
        final WebDriver driver = loadPage2(html);
        final WebElement radio = driver.findElement(By.id("radio"));
        assertFalse(radio.isSelected());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "on", "on", "on", "on" })
    public void defaultValue() throws Exception {
        final String html = "<html><head><title>foo</title>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('rdo').value);\n"

            + "    var input = document.createElement('input');\n"
            + "    input.type = 'radio';\n"
            + "    alert(input.value);\n"

            + "    var builder = document.createElement('div');\n"
            + "    builder.innerHTML = '<input type=\"radio\">';\n"
            + "    var input = builder.firstChild;\n"
            + "    alert(input.value);\n"

            + "    input = input.cloneNode(false);\n"
            + "    alert(input.value);\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "<form>\n"
            + "  <input type='radio' id='rdo'>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
            + "<input type='radio' id='it' onchange='alert(\"changed\")'"
            + "onmousedown='alert(\"down\")' onmouseup='alert(\"up\")' onfocus='alert(\"focused\")'>Check me\n"
            + "<script>\n"
            + "var elt = document.getElementById('it');\n"
            + "elt.click();\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }
}
