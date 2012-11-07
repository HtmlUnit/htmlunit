/*
 * Copyright (c) 2002-2012 Gargoyle Software Inc.
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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;

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
 * Same scope as {@link JavaScriptEngineTest} but extending {@link WebDriverTestCase}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class JavaScriptEngine2Test extends WebDriverTestCase {

    /**
     * All browsers except Opera seem to have a single JS execution thread for all windows,
     * but it is not the case of HtmlUnit-2.6!
     * Tested browsers (21.09.09):
     * - single JS thread: FF2, FF3.1, FF3.5, IE6 (and for info: Konqueror 4.2.2, Chrome Linux dev build)
     * - multiple JS threads: none of HtmlUnit's simulated browsers (for info Opera 10.00)
     * @throws Exception if the test fails
     */
    @Test
    public void jsRunSingleThreadedBrowserWide() throws Exception {
        final String html = "<html><head><script>"
            + "function test(prefix) {\n"
            + "  parent.document.getElementById('theArea').value += prefix + ' start\\n';\n"
            + "  var end = new Date().valueOf() + 1 * 1000;\n"
            + "  var t = [];\n"
            + "  while (new Date().valueOf() < end) {\n"
            + "    var x = document.createElement('iframe');\n"
            + "    t.push(x);\n"
            + "  }\n"
            + "  parent.document.getElementById('theArea').value += prefix + ' end\\n';\n"
            + "}\n"
            + "function checkResults() {\n"
            + "  var value = document.getElementById('theArea').value;\n"
            + "  var lines = value.split('\\n');\n"
            + "  if (lines.length < 5)\n"
            + "    setTimeout(checkResults, 100); // not yet ready, check later\n"
            + "  value = value.replace(/frame \\d /gi, '').replace(/\\W/gi, '');\n"
            + "  var singleThreaded = (value == 'startendstartend');\n"
            + "  document.getElementById('result').innerHTML = (singleThreaded ? 'single threaded' : 'in parallel');\n"
            + "}\n"
            + "function doTest() {\n"
            + "  parent.document.getElementById('theArea').value = '';\n"
            + "  document.getElementById('frame1').contentWindow.setTimeout(function() {test('frame 1'); }, 10);\n"
            + "  document.getElementById('frame2').contentWindow.setTimeout(function() {test('frame 2'); }, 10);\n"
            + "  setTimeout(checkResults, 1000);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe id='frame1' src='about:blank'></iframe>\n"
            + "<iframe id='frame2' src='about:blank'></iframe>\n"
            + "<textarea id='theArea' rows='5'></textarea>\n"
            + "script execution occured: <span id='result'></span>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final WebElement element = driver.findElement(By.id("result"));

        // give time to the script to execute: normally ~2 seconds when scripts are run sequentially
        int nbWait = 0;
        while (element.getText().isEmpty()) {
            Thread.sleep(100);
            if (nbWait++ > 50) {
                break;
            }
        }

        assertEquals("single threaded", element.getText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "false", "false", "true" })
    public void functionCaller() throws Exception {
        final String html = "<html><head><script>\n"
            + "function myFunc() {\n"
            + "  alert(myFunc.caller == null);\n"
            + "  alert(myFunc.caller == foo);\n"
            + "}\n"
            + "myFunc()\n"
            + "function foo() { myFunc() }\n"
            + "foo()\n"
            + "</script>\n"
            + "</head><body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = {"in goo", "in hoo", "in foo" },
            FF = {"in goo", "in hoo", "foo error" })
    @NotYetImplemented(IE)
    public void functionDeclaredForwardInBlock() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<script>\n"
            + "  if (true) {\n"
            + "    goo();\n"
            + "    function hoo() { alert('in hoo'); };\n"
            + "    try {\n"
            + "      hoo();\n"
            + "      foo();\n"
            + "    } catch (e) {\n"
            + "      alert('foo error');\n"
            + "    }\n"
            + "    function foo() { alert('in foo'); };\n"
            + "  }\n"
            + "  function goo() { alert('in goo'); };\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "undefined", "foo error" },
            IE = {"function foo() {}", "function foo() {}" })
    @NotYetImplemented
    public void variableNotDefined() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<script>\n"
            + "if (true) {\n"
            + "  try {\n"
            + "    alert(window.foo);\n"
            + "    alert(foo);\n"
            + "  } catch (e) {\n"
            + "    alert('foo error');\n"
            + "  }\n"
            + "  function foo() {}\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "[object Window]", "[object Window]", "true",
                "[object HTMLDocument]", "[object HTMLDocument]", "true", "function" },
            IE = { "undefined", "exception", "undefined", "exception", "function" })
    public void constructor() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<script>\n"
            + "alert(window.constructor);\n"
            + "try {\n"
            + "  alert(Window);\n"
            + "  alert(window.constructor === Window);\n"
            + "} catch (e) {\n"
            + "  alert('exception');\n"
            + "}\n"
            + "alert(document.constructor);\n"
            + "try {\n"
            + "  alert(HTMLDocument);\n"
            + "  alert(document.constructor === HTMLDocument);\n"
            + "} catch (e) {\n"
            + "  alert('exception');\n"
            + "}\n"
            + "alert(typeof new Object().constructor);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void packages() throws Exception {
        object("Packages");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void java() throws Exception {
        object("java");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void object_getClass() throws Exception {
        object("window.getClass");
    }

    private void object(final String object) throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  alert(" + object + ");\n"
            + "} catch (e) {\n"
            + "  alert('exception');\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
