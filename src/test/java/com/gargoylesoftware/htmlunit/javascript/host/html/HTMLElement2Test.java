/*
 * Copyright (c) 2002-2009 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HTMLElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "HTML", "" }, FF = { "undefined", "undefined" })
    public void scopeName() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.body.scopeName);\n"
            + "    alert(document.body.tagUrn);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "30px", "46", "55px", "71", "71", "0", "0" })
    public void offsetWidthAndHeight() throws Exception {
        final String html =
              "<html><head>\n"
            + "<style>\n"
            + ".dontDisplay { display: none } \n"
            + ".hideMe { visibility: hidden } \n"
            + "</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('myDiv');\n"
            + "    e.style.width = 30;\n"
            + "    alert(e.style.width);\n"
            + "    alert(e.offsetWidth);\n"
            + "    e.style.height = 55;\n"
            + "    alert(e.style.height);\n"
            + "    alert(e.offsetHeight);\n"
            + "    e.className = 'hideMe';\n"
            + "    alert(e.offsetHeight);\n"
            + "    e.className = 'dontDisplay';\n"
            + "    alert(e.offsetHeight);\n"
            + "    alert(e.offsetWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myDiv' style='border: 3px solid #fff; padding: 5px;'></div>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("30")
    public void offsetWidth_parentWidthConstrainsChildWidth() throws Exception {
        final String html = "<html><head><style>#a { width: 30px; }</style></head><body>\n"
            + "<div id='a'><div id='b'>foo</div></div>\n"
            + "<script>alert(document.getElementById('b').offsetWidth);</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * When CSS float is set to "right" or "left", the width of an element is related to
     * its content and it doesn't takes the full available width.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "0.5", "true" })
    public void offsetWidth_cssFloat_rightOrLeft() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<div id='withoutFloat1'>hello</div><div>hellohello</div>\n"
            + "<div id='withFloat1' style='float: left'>hello</div><div style='float: left'>hellohello</div>\n"
            + "<script>\n"
            + "var eltWithoutFloat1 = document.getElementById('withoutFloat1');\n"
            + "alert(eltWithoutFloat1.offsetWidth / eltWithoutFloat1.nextSibling.offsetWidth);\n"
            + "var eltWithFloat1 = document.getElementById('withFloat1');\n"
            + "alert(eltWithFloat1.offsetWidth / eltWithFloat1.nextSibling.offsetWidth);\n"
            // we don't make any strong assumption on the screen size here,
            // but expect it to be big enough to show 10 times "hello" on one line
            + "alert(eltWithoutFloat1.offsetWidth > 10 * eltWithFloat1.offsetWidth);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "something", "something" }, FF = { "something", "0" })
    public void textContent_null() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    myTestDiv.textContent = null;\n"
            + "    checkChildren();\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      alert('0');\n"
            + "    else\n"
            + "      alert(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "something", "null" }, FF = { "something", "0" })
    public void innerText_null() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    if (myTestDiv.innerText)\n"
            + "      myTestDiv.innerText = null;\n"
            + "    else\n"
            + "      myTestDiv.textContent = null;\n"
            + "    checkChildren();\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      alert('0');\n"
            + "    else\n"
            + "      alert(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "something", "0" })
    public void innerText_emptyString() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    checkChildren();\n"
            + "    if (myTestDiv.innerText)\n"
            + "      myTestDiv.innerText = '';\n"
            + "    else\n"
            + "      myTestDiv.textContent = '';\n"
            + "    checkChildren();\n"
            + "  }\n"
            + "  function checkChildren() {\n"
            + "    if (myTestDiv.childNodes.length == 0)\n"
            + "      alert('0');\n"
            + "    else\n"
            + "      alert(myTestDiv.childNodes.item(0).data);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='myTestDiv'>something</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Blur isn't fired on DIV elements for instance.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "input handler", "blur input" })
    public void eventHandlerBubble_blur() throws Exception {
        events("blur");
    }

    /**
     * Focus isn't fired on DIV elements for instance.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "input handler", "focus input" })
    public void eventHandlerBubble_focus() throws Exception {
        events("focus");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "input handler", "click input", "div handler", "click div" })
    public void eventHandlerBubble_click() throws Exception {
        events("click");
    }

    private void events(final String type) throws Exception {
        final String html = "<html><head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<div id='div' on" + type + "='log(\"div handler\")'>\n"
            + "<input id='input' on" + type + "='log(\"input handler\")'>\n"
            + "</div>\n"
            + "<textarea id='log'></textarea>\n"
            + "<script>\n"
            + "function log(x) {\n"
            + "  var log = document.getElementById('log');\n"
            + "  log.value += x + '\\n';\n"
            + "}\n"
            + "function addListener(id, event) {\n"
            + "  var handler = function(e) { log(event + ' ' + id) };\n"
            + "  var e = document.getElementById(id);\n"
            + "  if (e.addEventListener) {\n"
            + "    e.addEventListener(event, handler, false)\n"
            + "  } else e.attachEvent('on' + event, handler);\n"
            + "}\n"
            + "var eventType = '" + type + "';\n"
            + "addListener('div', eventType);\n"
            + "addListener('input', eventType);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("input")).click();
        final WebElement log = driver.findElement(By.id("log"));
        log.click();
        final String text = log.getValue().trim().replaceAll("\r", "");
        assertEquals(StringUtils.join(getExpectedAlerts(), "\n"), text);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "null", "klazz" })
    public void setAttributeNode() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var attribute = document.createAttribute('class');\n"
            + "    attribute.nodeValue = 'klazz';\n"
            + "    alert(document.body.setAttributeNode(attribute));\n"
            + "    alert(document.body.getAttributeNode('class').nodeValue);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }
}
