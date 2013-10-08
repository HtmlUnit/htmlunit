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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link HTMLOptionElement}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 */
@RunWith(BrowserRunner.class)
public class HTMLOptionElement2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("SELECT;")
    @BuggyWebDriver(IE)
    //TODO: WebDriver tests passes even with HtmlUnit direct usage fails!
    public void clickSelect() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
                + "  function log(x) {\n"
                + "    document.getElementById('log_').value += x + '; ';\n"
                + "  }\n"

                + "  function init() {\n"
                + "    var s = document.getElementById('s');\n"
                + "    if (s.addEventListener) {\n"
                + "      s.addEventListener('click', handle, false);\n"
                + "    } else if (s.attachEvent) {\n"
                + "      s.attachEvent('onclick', handle);\n"
                + "    }\n"
                + "  }\n"

                + "  function handle(event) {\n"
                + "    if (event.target) {\n"
                + "      log(event.target.nodeName);\n"
                + "    } else {\n"
                + "      log(event.srcElement.nodeName);\n"
                + "    }\n"
                + "  }\n"
                + "</script></head>\n"

                + "<body onload='init()'>\n"
                + "<form>\n"
                + "  <textarea id='log_' rows='4' cols='50'></textarea>\n"
                + "  <select id='s' size='7'>\n"
                + "    <option value='opt-a'>A</option>\n"
                + "    <option id='opt-b' value='b'>B</option>\n"
                + "    <option value='opt-c'>C</option>\n"
                + "  </select>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("s")).click();

        final List<String> alerts = new LinkedList<String>();
        final WebElement log = driver.findElement(By.id("log_"));
        alerts.add(log.getAttribute("value").trim());
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "opt-a; opt-b",
            CHROME = "opt-b")
    @BuggyWebDriver
    @NotYetImplemented
    //TODO: Needs further investigation of clicking an option without clicking the select
    //See the first comment in http://code.google.com/p/selenium/issues/detail?id=2131#c1
    // Additionally, FF and Chrome drivers look buggy as they don't allow to capture
    // what happens when running the test manually in the browser.
    public void click2() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
                + "  function log(x) {\n"
                + "    document.getElementById('log_').value += x + '; ';\n"
                + "  }\n"

                + "  function init() {\n"
                + "    var s = document.getElementById('s');\n"
                + "    if (s.addEventListener) {\n"
                + "      s.addEventListener('click', handle, false);\n"
                + "    } else if (s.attachEvent) {\n"
                + "      s.attachEvent('onclick', handle);\n"
                + "    }\n"
                + "  }\n"

                + "  function handle(event) {\n"
                + "    log(s.options[s.selectedIndex].value);\n"
                + "  }\n"
                + "</script></head>\n"

                + "<body onload='init()'>\n"
                + "<form>\n"
                + "  <textarea id='log_' rows='4' cols='50'></textarea>\n"
                + "  <select id='s'>\n"
                + "    <option value='opt-a'>A</option>\n"
                + "    <option id='opt-b' value='b'>B</option>\n"
                + "    <option value='opt-c'>C</option>\n"
                + "  </select>\n"
                + "</form>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("s")).click();
        driver.findElement(By.id("opt-b")).click();

        final List<String> alerts = new LinkedList<String>();
        final WebElement log = driver.findElement(By.id("log_"));
        alerts.add(log.getAttribute("value").trim());
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "onchange-select; onclick-option; onclick-select;",
            IE = "onchange-select; onclick-select;")
    @BuggyWebDriver(Browser.FF)
    @NotYetImplemented
    public void clickOptionEventSequence1() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  function log(x) {\n"
                + "    document.getElementById('log_').value += x + '; ';\n"
                + "  }\n"
                + "</script></head>\n"

                + "<body onload='init()'>\n"
                + "<form>\n"
                + "  <textarea id='log_' rows='4' cols='50'></textarea>\n"
                + "  <select id='s' size='2' onclick=\"log('onclick-select')\""
                        + " onchange=\"log('onchange-select')\">\n"
                + "    <option id='clickId' value='a' onclick=\"log('onclick-option')\""
                        + " onchange=\"log('onchange-option')\">A</option>\n"
                + "  </select>\n"
                + "</form>\n"

                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("clickId")).click();

        final List<String> alerts = new LinkedList<String>();
        final WebElement log = driver.findElement(By.id("log_"));
        alerts.add(log.getAttribute("value").trim());
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "change-SELECT; click-OPTION; click-OPTION;",
            IE = "change-SELECT; click-SELECT;")
    @BuggyWebDriver(Browser.FF)
    @NotYetImplemented
    public void clickOptionEventSequence2() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  function log(x) {\n"
                + "    document.getElementById('log_').value += x + '; ';\n"
                + "  }\n"

                + "  function init() {\n"
                + "    var s = document.getElementById('s');\n"
                + "    var o = document.getElementById('clickId');\n"
                + "    if (s.addEventListener) {\n"
                + "      s.addEventListener('click', handle, false);\n"
                + "      s.addEventListener('change', handle, false);\n"
                + "      o.addEventListener('click', handle, false);\n"
                + "      o.addEventListener('change', handle, false);\n"
                + "    } else if (s.attachEvent) {\n"
                + "      s.attachEvent('onclick', handle);\n"
                + "      s.attachEvent('onchange', handle);\n"
                + "      o.attachEvent('onclick', handle);\n"
                + "      o.attachEvent('onchange', handle);\n"
                + "    }\n"
                + "  }\n"

                + "  function handle(event) {\n"
                + "    if (event.target) {\n"
                + "      log(event.type + '-' + event.target.nodeName);\n"
                + "    } else {\n"
                + "      log(event.type + '-' + event.srcElement.nodeName);\n"
                + "    }\n"
                + "  }\n"
                + "</script></head>\n"

                + "<body onload='init()'>\n"
                + "<form>\n"
                + "  <textarea id='log_' rows='4' cols='50'></textarea>\n"
                + "  <select id='s' size='2' >\n"
                + "    <option id='clickId' value='a' >A</option>\n"
                + "  </select>\n"
                + "</form>\n"

                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("clickId")).click();

        final List<String> alerts = new LinkedList<String>();
        final WebElement log = driver.findElement(By.id("log_"));
        alerts.add(log.getAttribute("value").trim());
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * Test for the right event sequence when clicking.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "onchange-select; change-SELECT; onclick-option; click-OPTION; onclick-select; click-OPTION;",
            IE = "onchange-select; change-SELECT; onclick-select; click-SELECT;")
    @BuggyWebDriver(Browser.FF)
    @NotYetImplemented
    public void clickOptionEventSequence3() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  function log(x) {\n"
                + "    document.getElementById('log_').value += x + '; ';\n"
                + "  }\n"

                + "  function init() {\n"
                + "    var s = document.getElementById('s');\n"
                + "    var o = document.getElementById('clickId');\n"
                + "    if (s.addEventListener) {\n"
                + "      s.addEventListener('click', handle, false);\n"
                + "      s.addEventListener('change', handle, false);\n"
                + "      o.addEventListener('click', handle, false);\n"
                + "      o.addEventListener('change', handle, false);\n"
                + "    } else if (s.attachEvent) {\n"
                + "      s.attachEvent('onclick', handle);\n"
                + "      s.attachEvent('onchange', handle);\n"
                + "      o.attachEvent('onclick', handle);\n"
                + "      o.attachEvent('onchange', handle);\n"
                + "    }\n"
                + "  }\n"

                + "  function handle(event) {\n"
                + "    if (event.target) {\n"
                + "      log(event.type + '-' + event.target.nodeName);\n"
                + "    } else {\n"
                + "      log(event.type + '-' + event.srcElement.nodeName);\n"
                + "    }\n"
                + "  }\n"
                + "</script></head>\n"

                + "<body onload='init()'>\n"
                + "<form>\n"
                + "  <textarea id='log_' rows='4' cols='50'></textarea>\n"
                + "  <select id='s' size='2' onclick=\"log('onclick-select')\""
                        + " onchange=\"log('onchange-select')\">\n"
                + "    <option id='clickId' value='a' onclick=\"log('onclick-option')\""
                        + " onchange=\"log('onchange-option')\">A</option>\n"
                + "  </select>\n"
                + "</form>\n"

                + "</body></html>";

        final WebDriver driver = loadPage2(html);

        driver.findElement(By.id("clickId")).click();

        final List<String> alerts = new LinkedList<String>();
        final WebElement log = driver.findElement(By.id("log_"));
        alerts.add(log.getAttribute("value").trim());
        assertEquals(getExpectedAlerts(), alerts);
    }

    /**
     * Regression test for 3171569: unselecting the selected option should select the first one (FF)
     * or have no effect (IE).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "1", "option1", "0" },
            IE6 = { "1", "option2", "1" },
            IE8 = { "1", "option2", "1" })
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "false", "true", "true", "false", "true" })
    public void disabledAttribute() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var test1 = document.getElementById('test1');\n"
            + "        alert(test1.disabled);\n"
            + "        test1.disabled = true;\n"
            + "        alert(test1.disabled);\n"
            + "        test1.disabled = true;\n"
            + "        alert(test1.disabled);\n"
            + "        test1.disabled = false;\n"
            + "        alert(test1.disabled);\n"

            + "        var test2 = document.getElementById('test2');\n"
            + "        alert(test2.disabled);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body onload='test()'>\n"
            + "    <form name='form1'>\n"
            + "      <select>\n"
            + "        <option id='test1' value='option1'>Option1</option>\n"
            + "        <option id='test2' value='option2' disabled>Option2</option>\n"
            + "      </select>\n"
            + "  </form>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
