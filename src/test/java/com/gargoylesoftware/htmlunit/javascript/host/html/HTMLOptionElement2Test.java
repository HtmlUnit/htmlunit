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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLOptionElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HTMLOptionElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SELECT")
    //TODO: WebDriver tests passes even with HtmlUnit direct usage fails!
    public void click() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "  function init() {\n"
            + "    var s = document.getElementById('s');\n"
            + "    if (s.addEventListener) {\n"
            + "      s.addEventListener('click', handle, false);\n"
            + "    } else if (s.attachEvent) {\n"
            + "      s.attachEvent('onclick', handle);\n"
            + "    }\n"
            + "  }\n"
            + "  function handle(event) {\n"
            + "    if (event.target)\n"
            + "      alert(event.target.nodeName);\n"
            + "    else\n"
            + "      alert(event.srcElement.nodeName);\n"
            + "  }\n"
            + "</script></head><body onload='init()'>\n"
            + "  <select id='s'>\n"
            + "    <option value='a'>A</option>\n"
            + "    <option id='opb' value='b'>B</option>\n"
            + "    <option value='c'>C</option>\n"
            + "  </select>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("s")).click();
        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "ab",
            CHROME = "b")
    @NotYetImplemented
    @BuggyWebDriver
    //TODO: Needs further investigation of clicking an option without clicking the select
    //See the first comment in http://code.google.com/p/selenium/issues/detail?id=2131#c1
    // Additionally, FF and Chrome drivers look buggy as they don't allow to capture
    // what happens when running the test manually in the browser.
    public void click2() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "  function init() {\n"
            + "    var s = document.getElementById('s');\n"
            + "    if (s.addEventListener) {\n"
            + "      s.addEventListener('click', handle, false);\n"
            + "    } else if (s.attachEvent) {\n"
            + "      s.attachEvent('onclick', handle);\n"
            + "    }\n"
            + "  }\n"
            + "  function handle(event) {\n"
            + "    document.getElementById('input').value += s.options[s.selectedIndex].value;\n"
            + "  }\n"
            + "</script></head><body onload='init()'>\n"
            + "  <select id='s'>\n"
            + "    <option value='a'>A</option>\n"
            + "    <option id='opb' value='b'>B</option>\n"
            + "    <option value='c'>C</option>\n"
            + "  </select>\n"
            + "  <input id='input'>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("s")).click();
        driver.findElement(By.id("opb")).click();
        assertEquals(getExpectedAlerts()[0], driver.findElement(By.id("input")).getAttribute("value"));
    }

    /**
     * Regression test for 3171569: unselecting the selected option should select the first one (FF)
     * or have no effect (IE).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "option1", "0" }, IE = { "1", "option2", "1" })
    public void unselectResetToFirstOption() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var sel = document.form1.select1;\n"
            + "  alert(sel.selectedIndex);\n"
            + "  sel.options[1].selected = false;\n"
            + "  alert(sel.value);\n"
            + "  alert(sel.selectedIndex);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1'>\n"
            + "        <option value='option1' name='option1'>One</option>\n"
            + "        <option value='option2' name='option2' selected>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void selectFromJSTriggersNoFocusEvent() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var sel = document.form1.select1;\n"
            + "  sel.options[1].selected = true;\n"
            + "  alert(sel.selectedIndex);\n"
            + "}</script></head><body onload='doTest()'>\n"
            + "<form name='form1'>\n"
            + "    <select name='select1' onfocus='alert(\"focus\")'>\n"
            + "        <option value='option1' name='option1'>One</option>\n"
            + "        <option value='option2' name='option2'>Two</option>\n"
            + "    </select>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
