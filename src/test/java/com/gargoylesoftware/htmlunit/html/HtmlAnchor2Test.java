/*
 * Copyright (c) 2002-2016 Gargoyle Software Inc.
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
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import java.net.URL;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.interactions.Actions;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.util.NameValuePair;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HtmlAnchor}.
 *
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
        verifyAlerts(driver, getExpectedAlerts());
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

        verifyAlerts(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void clickNestedElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>\n"
            + "    <span id='theSpan'>My Link</span>\n"
            + "  </a>\n"
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
            + "  <a href='page2.html'>\n"
            + "    <button id='theButton'></button>\n"
            + "  </a>\n"
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
            CHROME = "",
            IE = "")
    public void clickNestedCheckboxElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>\n"
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
            + "  <a href='page2.html'>\n"
            + "    <img id='theImage' src='test.png' />\n"
            + "  </a>\n"
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
            IE = "")
    public void clickNestedInputImageElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>\n"
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
    @Alerts(DEFAULT = "page2.html",
            IE = "")
    public void clickNestedInputTextElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>\n"
            + "    <input type='text' id='theInput' />\n"
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
    @Alerts(DEFAULT = "page2.html",
            IE = "")
    public void clickNestedInputPasswordElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>\n"
            + "    <input type='password' id='theInput' />\n"
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
            + "  <a href='page2.html'>\n"
            + "    <select>\n"
            + "      <option id='theOption'>test</option>\n"
            + "    </select>\n"
            + "  </a>\n"
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
            CHROME = "",
            IE = "")
    public void clickNestedRadioElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>\n"
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
            + "  <a href='page2.html'>\n"
            + "    <input type='reset' id='theInput' />\n"
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
    @Alerts("page2.html")
    public void clickNestedSubmitElement() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a href='page2.html'>\n"
            + "    <input type='submit' id='theInput' />\n"
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
    @Alerts("<a id=\"a\" href=\"#x\">foo</a>")
    public void innerHtmlHrefQuotedEvenInIE() throws Exception {
        final String html = "<html><body onload='alert(document.getElementById(\"d\").innerHTML)'>"
            + "<div id='d'><a id='a' href='#x'>foo</a></div></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void click() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "<a href='http://www.foo1.com' id='a1'>link to foo1</a>\n"
            + "<a href='" + URL_SECOND + "' id='a2'>link to foo2</a>\n"
            + "</body></html>";

        final String secondContent
            = "<html><head><title>Second</title></head><body></body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setDefaultResponse(secondContent);

        final WebDriver driver = loadPage2(html);
        assertEquals(1, webConnection.getRequestCount());

        // Test that the correct value is being passed back up to the server
        driver.findElement(By.id("a2")).click();

        assertEquals(URL_SECOND.toExternalForm(), driver.getCurrentUrl());
        assertSame("method", HttpMethod.GET, webConnection.getLastMethod());
        assertTrue(webConnection.getLastParameters().isEmpty());

        assertEquals(2, webConnection.getRequestCount());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void clickAnchorName() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <a href='#clickedAnchor' id='a1'>link to foo1</a>\n"
            + "</body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        final WebDriver driver = loadPage2(html);

        assertEquals(1, webConnection.getRequestCount());

        driver.findElement(By.id("a1")).click();
        assertEquals(1, webConnection.getRequestCount()); // no second server hit
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "#anchor", "#!bang" })
    public void dontReloadHashBang() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <a href='" + URL_FIRST + "test' id='a1'>link1</a>\n"
            + "  <a href='" + URL_FIRST + "test#anchor' id='a2'>link2</a>\n"
            + "  <a href='" + URL_FIRST + "test#!bang' id='a3'>link3</a>\n"
            + "  <script>\n"
            + "    alert(document.getElementById('a1').hash);\n"
            + "    alert(document.getElementById('a2').hash);\n"
            + "    alert(document.getElementById('a3').hash);\n"
            + "  </script>\n"
            + "</body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setDefaultResponse(html);

        final WebDriver driver = loadPageWithAlerts2(html);

        assertEquals(1, webConnection.getRequestCount());

        driver.findElement(By.id("a1")).click();
        assertEquals(2, webConnection.getRequestCount());

        driver.findElement(By.id("a2")).click();
        assertEquals(2, webConnection.getRequestCount());

        driver.findElement(By.id("a3")).click();
        assertEquals(2, webConnection.getRequestCount());
    }

    /**
     * Testcase for issue #1492.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "#!board/WebDev", "#!article/WebDev/35", "#!article/WebDev/35" })
    public void dontReloadHashBang2() throws Exception {
        final String html
            = "<html>\n"
            + "<head><title>foo</title></head>\n"
            + "<body>\n"
            + "  <a href='" + URL_FIRST + "test/#!board/WebDev' id='a1'>link1</a>\n"
            + "  <a href='" + URL_FIRST + "test/#!article/WebDev/35' id='a2'>link2</a>\n"
            + "  <a href='" + URL_FIRST + "test#!article/WebDev/35' id='a3'>link2</a>\n"
            + "  <script>\n"
            + "    alert(document.getElementById('a1').hash);\n"
            + "    alert(document.getElementById('a2').hash);\n"
            + "    alert(document.getElementById('a3').hash);\n"
            + "  </script>\n"
            + "</body></html>";

        final MockWebConnection webConnection = getMockWebConnection();
        webConnection.setDefaultResponse(html);

        final WebDriver driver = loadPageWithAlerts2(html);

        assertEquals(1, webConnection.getRequestCount());

        driver.findElement(By.id("a1")).click();
        assertEquals(2, webConnection.getRequestCount());

        driver.findElement(By.id("a2")).click();
        assertEquals(2, webConnection.getRequestCount());

        driver.findElement(By.id("a3")).click();
        assertEquals(3, webConnection.getRequestCount());
    }

    /**
     * FF behaves is different.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = "click href click doubleClick ",
            CHROME = "click href click href doubleClick ",
            FF = "click href click doubleClick href ")
    @BuggyWebDriver({ FF, CHROME })
    @NotYetImplemented({ FF, IE })
    public void doubleClick() throws Exception {
        final String html =
              "<html>\n"
            + "<body>\n"
            + "  <a id='myAnchor' "
            +       "href=\"javascript:document.getElementById('myTextarea').value+='href ';void(0);\" "
            +       "onClick=\"document.getElementById('myTextarea').value+='click ';\" "
            +       "onDblClick=\"document.getElementById('myTextarea').value+='doubleClick ';\">foo</a>\n"
            + "  <textarea id='myTextarea'></textarea>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        final Actions action = new Actions(driver);
        action.doubleClick(driver.findElement(By.id("myAnchor")));
        action.perform();

        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("myTextarea")).getAttribute("value"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "§§URL§§bug.html?h%F6=G%FCnter", "h\ufffd", "G\ufffdnter" },
            IE = { "§§URL§§bug.html?h\u00F6=G\u00FCnter", "h\ufffd", "G\ufffdnter" })
    public void encoding() throws Exception {
        final String html =
            "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>\n"
            + "</head>\n"
            + "<body>\n"
            + "   <a href='bug.html?h\u00F6=G\u00FCnter' id='myLink'>Click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(html, "text/html", "UTF-8");

        expandExpectedAlertsVariables(URL_FIRST);
        final WebDriver driver = loadPage2(html, URL_FIRST);
        driver.findElement(new ById("myLink")).click();

        assertEquals(getExpectedAlerts()[0], driver.getCurrentUrl());

        final List<NameValuePair> requestedParams = getMockWebConnection().getLastWebRequest().getRequestParameters();
        assertEquals(1, requestedParams.size());
        assertEquals(getExpectedAlerts()[1], requestedParams.get(0).getName());
        assertEquals(getExpectedAlerts()[2], requestedParams.get(0).getValue());
    }
}
