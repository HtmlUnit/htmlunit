/*
 * Copyright (c) 2002-2014 Gargoyle Software Inc.
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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;

import java.net.URL;

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
 * Tests for {@link HtmlAnchor}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HtmlAnchor2Test extends WebDriverTestCase {

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "hi", "%28%29" })
    public void href_js_escaping() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function sayHello(text) {\n"
            + "    alert(text);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <a id='myAnchor' href=\"javascript:sayHello%28'hi'%29\">My Link</a>\n"
            + "  <input id='myButton' type=button onclick=\"javascript:sayHello('%28%29')\" value='My Button'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myAnchor")).click();
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "(*%a", "%28%A" })
    public void href_js_escaping2() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function sayHello(text) {\n"
            + "    alert(text);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "  <a id='myAnchor' href=\"javascript:sayHello%28'%28%2a%a'%29\">My Link</a>\n"
            + "  <input id='myButton' type=button onclick=\"javascript:sayHello('%28%A')\" value='My Button'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("myAnchor")).click();
        driver.findElement(By.id("myButton")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clickNestedElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<a href='page2.html'>"
            + "<span id='theSpan'>My Link</span></a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        final WebElement span = driver.findElement(By.id("theSpan"));
        assertEquals("span", span.getTagName());
        span.click();
        assertEquals(new URL(getDefaultUrl(), "page2.html").toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clickNestedButtonElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<a href='page2.html'>"
            + "<button id='theButton'></button></a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        final WebElement button = driver.findElement(By.id("theButton"));
        assertEquals("button", button.getTagName());
        button.click();
        assertEquals(new URL(getDefaultUrl(), "page2.html").toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "page2.html",
            IE11 = "")
    @NotYetImplemented(IE11)
    public void clickNestedCheckboxElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>"
            + "    <input type='checkbox' id='theCheckbox' name='myCheckbox' value='Milk'>\n"
            + "  </a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        final WebElement checkbox = driver.findElement(By.id("theCheckbox"));
        assertEquals("input", checkbox.getTagName());
        checkbox.click();
        assertEquals(new URL(getDefaultUrl(), getExpectedAlerts()[0]).toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clickNestedImageElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<a href='page2.html'>"
            + "<img id='theImage' src='test.png' /></a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        final WebElement img = driver.findElement(By.id("theImage"));
        assertEquals("img", img.getTagName());
        img.click();
        assertEquals(new URL(getDefaultUrl(), "page2.html").toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "page2.html",
            IE11 = "")
    @NotYetImplemented(IE11)
    public void clickNestedInputElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>"
            + "    <input type='image' id='theInput' />\n"
            + "  </a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("theInput"));
        assertEquals("input", input.getTagName());
        input.click();
        assertEquals(new URL(getDefaultUrl(), getExpectedAlerts()[0]).toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clickNestedOptionElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<a href='page2.html'>"
            + "<select><option id='theOption'>test</option></select></a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        final WebElement option = driver.findElement(By.id("theOption"));
        assertEquals("option", option.getTagName());
        option.click();
        assertEquals(new URL(getDefaultUrl(), "page2.html").toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = "page2.html",
            IE11 = "")
    @NotYetImplemented(IE11)
    public void clickNestedRadioElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>"
            + "    <input type='radio' id='theRadio' name='myRadio' value='Milk'>\n"
            + "  </a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        final WebElement radio = driver.findElement(By.id("theRadio"));
        assertEquals("input", radio.getTagName());
        radio.click();
        assertEquals(new URL(getDefaultUrl(), getExpectedAlerts()[0]).toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("page2.html")
    public void clickNestedResetElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<a href='page2.html'>"
            + "<input type='reset' id='theInput' /></a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("theInput"));
        assertEquals("input", input.getTagName());
        input.click();
        assertEquals(new URL(getDefaultUrl(), getExpectedAlerts()[0]).toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("page2.html")
    public void clickNestedSubmitElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "<a href='page2.html'>"
            + "<input type='submit' id='theInput' /></a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPage2(html);
        final WebElement input = driver.findElement(By.id("theInput"));
        assertEquals("input", input.getTagName());
        input.click();
        assertEquals(new URL(getDefaultUrl(), getExpectedAlerts()[0]).toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clickBlankTargetHashOnly() throws Exception {
        final String html =
                "<html>\n"
                        + "<head><title>foo</title></head>\n"
                        + "<body>\n"
                        + "<a id='a' target='_blank' href='#'>Foo</a>\n"
                        + "</body></html>\n";

        final WebDriver driver = loadPage2(html);
        assertEquals(1, driver.getWindowHandles().size());

        final WebElement tester = driver.findElement(By.id("a"));
        tester.click();
        assertEquals(2, driver.getWindowHandles().size());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "My Link", "", "abcd" })
    public void getText() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myAnchor').text);\n"
            + "    alert(document.getElementById('myImgAnchor').text);\n"
            + "    alert(document.getElementById('myImgTxtAnchor').text);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload=test()>\n"
            + "  <a id='myAnchor'>My Link</a>\n"
            + "  <a id='myImgAnchor'><img src='test.png' /></a>\n"
            + "  <a id='myImgTxtAnchor'>ab<img src='test.png' />cd</a>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "My Link 0", "Hello 0", " 1", "Hello 0", "a 2", "Hello 0" })
    public void setText() throws Exception {
        final String html =
              "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      var anchor = document.getElementById('myAnchor');\n"
            + "      alert(anchor.text + ' ' + anchor.children.length);\n"
            + "      anchor.text = 'Hello';\n"
            + "      alert(anchor.text + ' ' + anchor.children.length);\n"

            + "      anchor = document.getElementById('myImgAnchor');\n"
            + "      alert(anchor.text + ' ' + anchor.children.length);\n"
            + "      anchor.text = 'Hello';\n"
            + "      alert(anchor.text + ' ' + anchor.children.length);\n"

            + "      anchor = document.getElementById('myImgTxtAnchor');\n"
            + "      alert(anchor.text + ' ' + anchor.children.length);\n"
            + "      anchor.text = 'Hello';\n"
            + "      alert(anchor.text + ' ' + anchor.children.length);\n"
            + "    } catch (e) { alert('exception' + e) }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload=test()>\n"
            + "  <a id='myAnchor'>My Link</a>\n"
            + "  <a id='myImgAnchor'><img src='test.png' /></a>\n"
            + "  <a id='myImgTxtAnchor'><img src='test.png' />a<img src='test.png' /></a>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Attributes aren't usually quoted in IE, but <tt>href</tt> attributes of anchor elements are.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "<a id=\"a\" href=\"#x\">foo</a>",
            IE8 = "<A id=a href=\"#x\">foo</A>")
    public void innerHtmlHrefQuotedEvenInIE() throws Exception {
        final String html = "<html><body onload='alert(document.getElementById(\"d\").innerHTML)'>"
            + "<div id='d'><a id='a' href='#x'>foo</a></div></body></html>";

        loadPageWithAlerts2(html);
    }
}
