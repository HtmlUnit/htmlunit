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

import static com.gargoylesoftware.htmlunit.BrowserRunner.TestedBrowser.IE;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Unit tests for {@link HtmlElement}.
 *
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Denis N. Antonioli
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HtmlElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void onpropertychange() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    document.getElementById('input1').value = 'New Value';\n"
            + "  }\n"
            + "  function handler() {\n"
            + "    alert(event.propertyName);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='input1' onpropertychange='handler()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true"})
    public void duplicateId() throws Exception {
        final String html
            = "<html>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var value = document.getElementById('duplicateID').innerHTML;\n"
            + "    alert(value.length > 10);\n"
            + "    document.getElementById('duplicateID').style.display = 'block';\n"
            + "    alert(value === document.getElementById('duplicateID').innerHTML);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <fieldset id='duplicateID'><span id='duplicateID'></span></fieldset>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void onpropertychange2() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    document.getElementById('input1').value = 'New Value';\n"
            + "  }\n"
            + "  function handler() {\n"
            + "    alert(1);\n"
            + "    document.getElementById('input1').dir='rtl';\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <input id='input1' onpropertychange='handler()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that cloned node attributes have the same initial values, but changes can be made
     * to the clone without affecting the original node, and that the id attribute is treated the
     * same as all the other attributes. See bug #468.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"false", "true", "a", "a", "b", "b", "b", "c"})
    public void clonedNodeAttributes() throws Exception {
        final String html = "<html><body id='a' title='b'><script>\n"
            + "var x = document.body.cloneNode(true);\n"
            + "alert(document.body == x);\n"
            + "alert(document.getElementById('a') == document.body);\n"
            + "alert(document.body.id);\n"
            + "alert(x.id);\n"
            + "alert(document.body.title);\n"
            + "alert(x.title);\n"
            + "x.title = 'c';\n"
            + "alert(document.body.title);\n"
            + "alert(x.title);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test attribute.text and attribute.xml added for XmlElement attributes
     * are undefined for HtmlElement.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "undefined", "undefined"})
    public void textAndXmlUndefined() throws Exception {
        final String html
            = "<html><head><title>foo</title></head><body>\n"
            + "    <input type='text' id='textfield1' onfocus='alert(1)'>\n"
            + "    <script>\n"
            + "         var node = document.getElementById('textfield1');\n"
            + "         alert(node.attributes[0].nodeName.length > 0);\n"
            + "         alert(node.attributes[0].text);\n"
            + "         alert(node.attributes[0].xml);\n"
            + "    </script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    //TODO: fails because of HTMLElement.getContentEditable doesn't detect DomElement.ATTRIBUTE_VALUE_EMPTY
    // this could be a general attribute issue
    public void contentEditable() throws Exception {
        final String html
            = "<html>\n"
            + "<body contentEditable><p>initial</p></body>\n"
            + "</html>";

        final WebDriver driver = loadPage2(html);
        final WebElement body = driver.findElement(By.xpath("//body"));
        body.clear();
        body.sendKeys("something");
        assertEquals("something", body.getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "down: 16,0 down: 49,0 press: 33,33 up: 49,0 up: 16,0"
                + " down: 16,0 down: 220,0 press: 124,124 up: 220,0 up: 16,0",
            FF = "down: 16,0 down: 49,0 press: 0,33 up: 49,0 up: 16,0"
                + " down: 16,0 down: 220,0 press: 0,124 up: 220,0 up: 16,0",
            FF78 = "down: 16,0 down: 49,0 press: 0,33 up: 49,0 up: 16,0"
                + " down: 16,0 down: 220,0 press: 0,124 up: 220,0 up: 16,0")
    //https://github.com/SeleniumHQ/selenium/issues/639
    @BuggyWebDriver(FF78 = "down: 49,0 press: 33,33 up: 49,0 down: 220,0 press: 124,124 up: 220,0",
                FF = "down: 49,0 press: 33,33 up: 49,0 down: 220,0 press: 124,124 up: 220,0",
                IE = "down: 16,0 down: 49,0 press: 33,33 up: 49,0 up: 16,0 down: 17,0 "
                        + "down: 18,0 down: 226,0 press: 124,124 up: 226,0 up: 17,0 up: 18,0")
    public void shiftKeys() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function appendMessage(message) {\n"
            + "    document.getElementById('result').innerHTML += message + ' ';\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body >\n"
            + "  <input id='input1' onkeyup=\"appendMessage('up: ' + event.keyCode + ',' + event.charCode)\" "
            + "onkeypress=\"appendMessage('press: ' + event.keyCode + ',' + event.charCode)\" "
            + "onkeydown=\"appendMessage('down: ' + event.keyCode + ',' + event.charCode)\"><br>\n"
            + "<p id='result'></p>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("input1"));
        final WebElement result = driver.findElement(By.id("result"));
        input.sendKeys("!|");
        assertEquals(getExpectedAlerts()[0], result.getText());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLInputElement] [object HTMLBodyElement]",
            CHROME = "[object HTMLInputElement] onblur onfocusout [object HTMLBodyElement]",
            EDGE = "[object HTMLInputElement] onblur onfocusout [object HTMLBodyElement]",
            IE = "[object HTMLInputElement] null")
    @NotYetImplemented(IE)
    public void removeActiveElement() throws Exception {
        final String html =
               HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  var elem = document.getElementById('text1');\n"
                + "  elem.focus();\n"
                + "  document.title += ' ' + document.activeElement;\n"
                + "  elem.parentNode.removeChild(elem);\n"
                + "  document.title += ' ' + document.activeElement;\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "<form name='form1'>\n"
                + "  <input id='text1' onblur='document.title += \" onblur\"' "
                        + "onfocusout='document.title += \" onfocusout\"'>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts(DEFAULT = "[object HTMLInputElement] [object HTMLBodyElement]",
            CHROME = "[object HTMLInputElement] onblur1 onfocusout1 [object HTMLBodyElement]",
            EDGE = "[object HTMLInputElement] onblur1 onfocusout1 [object HTMLBodyElement]",
            IE = "[object HTMLInputElement] null")
    @NotYetImplemented(IE)
    public void removeParentOfActiveElement() throws Exception {
        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  var elem = document.getElementById('text1');\n"
                + "  elem.focus();\n"
                + "  document.title += ' ' + document.activeElement;\n"

                + "  var elem = document.getElementById('parent');\n"
                + "  elem.parentNode.removeChild(elem);\n"
                + "  document.title += ' ' + document.activeElement;\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "<form name='form1'>\n"
                + "  <div id='parent'>\n"
                + "    <input id='text1' onblur='document.title += \" onblur1\"' "
                                + "onfocusout='document.title += \" onfocusout1\"'>\n"
                + "    <input id='text2' onblur='document.title += \" onblur2\"' "
                                + "onfocusout='document.title += \" onfocusout2\"'>\n"
                + "  </div>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertTitle(driver, getExpectedAlerts()[0]);
    }

    /**
     * Another nasty trick from one of these trackers.
     *
     * @throws Exception on test failure
     */
    @Test
    @Alerts({"before appendChild;after appendChild;image onload;after removeChild;", "2"})
    // HtmlUnit loads images synchron - because of this the removeChild is called before the
    // node is appended and fails
    @NotYetImplemented
    public void addRemove() throws Exception {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("testfiles/tiny-jpg.img")) {
            final byte[] directBytes = IOUtils.toByteArray(is);
            final URL urlImage = new URL(URL_FIRST, "img.jpg");
            final List<NameValuePair> emptyList = Collections.emptyList();
            getMockWebConnection().setResponse(urlImage, directBytes, 200, "ok", "image/jpg", emptyList);
        }

        final String html =
                HtmlPageTest.STANDARDS_MODE_PREFIX_
                + "<html>\n"
                + "<head>\n"
                + "<script>\n"
                + "function test() {\n"
                + "  var elem = document.createElement('img');\n"
                + "  elem.setAttribute('alt', '');\n"
                + "  elem.setAttribute('src', 'img.jpg');\n"
                + "  elem.style.display = 'none';\n"
                + "  elem.onload = function() {\n"
                + "      document.title += 'image onload;';"
                + "      document.body.removeChild(elem);\n"
                + "      document.title += 'after removeChild;';"
                + "    }\n"
                + "  document.title += 'before appendChild;';"
                + "  document.body.appendChild(elem);\n"
                + "  document.title += 'after appendChild;';"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        final int count = getMockWebConnection().getRequestCount();
        final WebDriver driver = getWebDriver();
        if (driver instanceof HtmlUnitDriver) {
            ((HtmlUnitDriver) driver).setDownloadImages(true);
        }
        loadPage2(html);

        assertTitle(driver, getExpectedAlerts()[0]);
        assertEquals(Integer.parseInt(getExpectedAlerts()[1]), getMockWebConnection().getRequestCount() - count);
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    public void keyPressEventWhenPreventsDefault() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <input id='suppress' onkeydown='event.preventDefault()' onkeypress='alert(\"press\")'>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("suppress")).sendKeys("s");
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception on test failure
     */
    @Test
    @Alerts("press")
    public void keyUpEventWhenPreventsDefault() throws Exception {
        final String html = "<html>\n"
                + "<body>\n"
                + "  <input id='suppress' onkeydown='event.preventDefault()' onkeyup='alert(\"press\")'>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("suppress")).sendKeys("s");
        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object HTMLHtmlElement]", "null"})
    public void detach() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    var xhr = new XMLHttpRequest();\n"
            + "    xhr.onload = function () {\n"
            + "        var xml = xhr.responseXML;\n"
            + "        alert(xml.documentElement);\n"
            + "        xml.removeChild(xml.firstChild);\n"
            + "        alert(xml.documentElement);\n"
            + "    }\n"
            + "    xhr.open('GET', '" + URL_SECOND + "');\n"
            + "    xhr.send();\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>\n";

        final String xml = "<html xmlns=\"http://www.w3.org/1999/xhtml\"></html>";
        getMockWebConnection().setResponse(URL_SECOND, xml, "application/xml");
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello-world")
    @BuggyWebDriver(FF = "-worldHello",
            FF78 = "-worldHello")
    public void typeAtEndOfEditableDiv() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myInput').value);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <input id='myButton' type='button' onclick='test()'>\n"
            + "  <div id='myInput' contenteditable='true'>Hello</div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement div = driver.findElement(By.id("myInput"));
        div.sendKeys("-world");

        assertEquals(getExpectedAlerts()[0], div.getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("Hello-world")
    @BuggyWebDriver(FF = "-worldHello",
            FF78 = "-worldHello")
    public void typeAtEndOfEditableDivWithParagraphInside() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myInput').value);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <input id='myButton' type='button' onclick='test()'>\n"
            + "  <div id='myInput' contenteditable='true'><p>Hello</p></div>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement div = driver.findElement(By.id("myInput"));
        div.sendKeys("-world");

        assertEquals(getExpectedAlerts()[0], div.getText());
    }
}
