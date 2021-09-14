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
package com.gargoylesoftware.htmlunit.javascript;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.HtmlUnitNYI;
import com.gargoylesoftware.htmlunit.util.MimeType;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Same scope as {@link JavaScriptEngineTest} but extending {@link WebDriverTestCase}.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class JavaScriptEngine2Test extends WebDriverTestCase {

    /**
     * All browsers except Opera seem to have a single JS execution thread for all windows.
     * @throws Exception if the test fails
     */
    @Test
    public void jsRunSingleThreadedBrowserWide() throws Exception {
        final String html = "<html><head><script>\n"
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
    @Alerts({"true", "false", "false", "true"})
    public void functionCaller() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function myFunc() {\n"
            + "  log(myFunc.caller == null);\n"
            + "  log(myFunc.caller == foo);\n"
            + "}\n"
            + "myFunc()\n"
            + "function foo() { myFunc() }\n"
            + "foo()\n"
            + "</script>\n"
            + "</head><body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"in goo", "in hoo", "in foo"})
    public void functionDeclaredForwardInBlock() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  if (true) {\n"
            + "    goo();\n"
            + "    function hoo() { log('in hoo'); }\n"
            + "    try {\n"
            + "      hoo();\n"
            + "      foo();\n"
            + "    } catch (e) {\n"
            + "      log('foo error');\n"
            + "    }\n"
            + "    function foo() { log('in foo'); }\n"
            + "  }\n"
            + "  function goo() { log('in goo'); }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "function foo() {}"},
            IE = {"function foo() {}", "function foo() {}"})
    @HtmlUnitNYI(CHROME = {"function foo() { }", "function foo() { }"},
            EDGE = {"function foo() { }", "function foo() { }"},
            FF = {"function foo() { }", "function foo() { }"},
            FF78 = {"function foo() { }", "function foo() { }"},
            IE = {"function foo() { }", "function foo() { }"})
    public void variableNotDefined() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "if (true) {\n"
            + "  try {\n"
            + "    log(window.foo);\n"
            + "    log(foo);\n"
            + "  } catch (e) {\n"
            + "    log('foo error');\n"
            + "  }\n"
            + "  function foo() {}\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"function Window() { [native code] }", "function Window() { [native code] }", "true",
                       "function HTMLDocument() { [native code] }", "function HTMLDocument() { [native code] }",
                       "true", "function"},
            IE = {"[object Window]", "[object Window]", "true",
                  "[object HTMLDocument]", "[object HTMLDocument]", "true", "function"})
    public void constructor() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try { log(Window); } catch (e) { log('ex window'); }\n"
            + "  log(window.constructor);\n"
            + "  try {\n"
            + "    log(window.constructor === Window);\n"
            + "  } catch (e) {\n"
            + "    log('ex win const');\n"
            + "  }\n"

            + "  try { log(HTMLDocument); } catch (e) { log('ex doc'); }\n"
            + "  log(document.constructor);\n"
            + "  try {\n"
            + "    log(document.constructor === HTMLDocument);\n"
            + "  } catch (e) {\n"
            + "    log('exception doc const');\n"
            + "  }\n"
            + "  log(typeof new Object().constructor);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
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
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(" + object + ");\n"
            + "} catch (e) {\n"
            + "  log('exception');\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function"})
    public void inline() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "log(typeof Array.prototype.filter);\n"
                + "  function test() {\n"
                + "    log(typeof Array.prototype.filter);\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("found")
    public void enumerateMethods() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    for (var x in document) {\n"
            + "      if (x == 'getElementById')\n"
            + "        log('found');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Unit tests for bug 2531218 reported by Rhino as
     * <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=477604">Bug 477604 -
     * Array.concat causes ArrayIndexOutOfBoundException with non dense array</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void array_concat() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var a = [1, 2, 3];\n"
            + "    for (var i = 10; i < 20; i++)\n"
            + "      a[i] = 't' + i;\n"
            + "    var b = [1, 2, 3];\n"
            + "    b.concat(a);\n"
            + "    log(b.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function f() {}")
    @HtmlUnitNYI(CHROME = "function f() { }",
            EDGE = "function f() { }",
            FF = "function f() { }",
            FF78 = "function f() { }",
            IE = "function f() { }")
    public void function_toStringValue() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function f() {}\n"
            + "  function test() {\n"
            + "    log(String(f));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("WebDriverException thrown")
    @BuggyWebDriver("WebDriverException NOT thrown")
    public void function_object_method() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  try {\n"
                + "    log('1');\n"
                + "    function document.onclick() {\n"
                + "      log('hi');\n"
                + "    }\n"
                + "    log('2');\n"
                + "  } catch(e) { log(e); }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        final String[] expected = getExpectedAlerts();

        try {
            setExpectedAlerts();
            loadPageWithAlerts2(html);

            // at the moment we do not get the syntax exception when running in selenium
            assertEquals("WebDriverException NOT thrown", expected[0]);
        }
        catch (final WebDriverException e) {
            assertEquals("WebDriverException thrown", expected[0]);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("that's it")
    public void quoteAsUnicodeInString() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log('that\\x27s it');\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("error")
    public void recursion() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function recurse(c) {\n"
            + LOG_TITLE_FUNCTION
            + "    try {\n"
            + "      recurse(c++);\n"
            + "    } catch (e) {\n"
            + "      log('error');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='recurse(1)'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * String value of native functions starts with \n on IE.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0", "false", "0"},
            IE = {"1", "true", "1"})
    public void nativeFunction_toStringValue() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(String(window.alert).indexOf('function'));\n"
            + "    log(String(window.alert).charAt(0) == '\\n');\n"
            + "    log(String(document.getElementById).indexOf('function'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug https://sf.net/tracker/?func=detail&atid=448266&aid=1609944&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    @HtmlUnitNYI(CHROME = "1",
            EDGE = "1",
            FF = "1",
            FF78 = "1",
            IE = "1")
    public void onloadJavascriptFunction() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + "function onload() { alert('foo'); }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        assertEquals(Integer.parseInt(getExpectedAlerts()[0]), getCollectedAlerts(driver, 1).size());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void alert() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "  alert('foo');\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "  <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Checks that a dynamically compiled function works in the scope of its birth.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void scopeOfNewFunction() throws Exception {
        final String html
            = "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  var f = new Function('log(\"foo\")');\n"
            + "  f();\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void scopeOfNestedNewFunction() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var foo = 'foo';\n"
            + "  var f1 = new Function('f = new Function(\"log(foo)\"); f()');\n"
            + "  f1();\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Sets value on input expects a string. If you pass in a value that isn't a string
     * this used to blow up.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void setValuesThatAreNotStrings() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  document.form1.textfield1.value = 1;\n"
            + "  log(document.form1.textfield1.value);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "  <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "  <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void javaScriptWrappedInHtmlComments() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script language='javascript'><!--\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log('foo');\n"
            + "}\n"
            + "-->\n</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void javaScriptWrappedInHtmlComments2() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "<script><!--\n"
            + " log('1');\n"
            + "--></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void javaScriptWrappedInHtmlComments_commentOnOpeningLine() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script language='javascript'><!-- Some comment here\n"
            + LOG_TITLE_FUNCTION
            + "function doTest() {\n"
            + "  log('1');\n"
            + "}\n"
            + "-->\n</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 1714762.
     * @throws Exception if the test fails
     */
    @Test
    public void javaScriptWrappedInHtmlComments_commentNotClosed() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<script language='javascript'><!-- log(1);</script>\n"
            + "<script language='javascript'><!-- </script>\n"
            + "</head>\n"
            + "<body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void javaScriptWrappedInHtmlComments_allOnOneLine() throws Exception {
        final String html
            = "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script>var test;</script>\n"
            + "    <!-- var test should be undefined since it's on first line -->\n"
            + "    <!-- but there should be no index out of bounds exception  -->\n"
            + "    <script> <!-- test = 'abc'; // --> </script>\n"
            + "  </head>\n"
            + "  <body onload='alert(test)'>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("test")
    public void eventHandlerWithComment() throws Exception {
        final String html = "<html><body onLoad='alert(\"test\"); // xxx'></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "3"})
    public void comment() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script><!-- alert(1);\n"
            + " alert(2);\n"
            + "alert(3)//--></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"rstlne-rstlne-rstlne", "rstlno-rstlne-rstlne",
             "rstlna-rstlne-rstlne", "rstlne-rstlne-rstlne",
             "rstlni-rstlni-rstlni", "rstlna-rstlna-rstlna"})
    public void regExpSupport() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <script id='a'>\n"
            + LOG_TITLE_FUNCTION
            + "       var s = new String('rstlne-rstlne-rstlne');\n"
            + "       log(s);\n"
            + "       s = s.replace('e', 'o');\n"
            + "       log(s);\n"
            + "       s = s.replace(/o/, 'a');\n"
            + "       log(s);\n"
            + "       s = s.replace(new RegExp('a'), 'e');\n"
            + "       log(s);\n"
            + "       s = s.replace(new RegExp('e', 'g'), 'i');\n"
            + "       log(s);\n"
            + "       s = s.replace(/i/g, 'a');\n"
            + "       log(s);\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body>abc</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test ECMA reserved keywords... that are accepted by "normal" browsers
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void ecmaReservedKeywords() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var o = {float: 123};\n"
            + "  log(o.float);\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object Window]",
            IE = {})
    @HtmlUnitNYI(IE = "[object Window]")
    public void boundFunction() throws Exception {
        final String html = "<html><head><script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    if (focusMe.bind) {\n"
                + "      var boundFunction = focusMe.bind(null);\n"
                + "      document.getElementById('myId').addEventListener('focus', boundFunction, true);\n"
                + "    }\n"
                + "  }\n"
                + "  function focusMe() {\n"
                + "    log(this);\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'>\n"
                + "  <button id='myId'>Click me</button>\n"
                + "</body></html>";

        final WebDriver driver = loadPage2(html);
        final String[] expectedAlerts = getExpectedAlerts();

        driver.findElement(By.id("myId")).click();
        verifyTitle2(driver, expectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"t=undefined", "inside"})
    public void functionHasNameOfVar() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log('t=' + t);\n"
                + "    var t = 42;\n"
                + "    ! function t() { log('inside'); } ();\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"outer abc = 1", "inner abc = function"})
    public void functionHasNameOfVarStrictMode() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  var abc = 1;\n"
                + "  var foo = function abc() { log('inner abc = ' + typeof abc); }\n"
                + "  log('outer abc = ' + abc);\n"
                + "  foo()\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"a", "b"})
    public void innerFunctionWithSameName() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  var a = function () {\n"
                + "    var x = (function x () { log('a') });\n"
                + "    return function () { x() };\n"
                + "  }();\n"

                + "  var b = function () {\n"
                + "    var x = (function x () { log('b') });\n"
                + "    return function () { x() };\n"
                + "  }();\n"

                + "  a();\n"
                + "  b();\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a")
    public void innerFunctionWithSameNameAsOutsideStrict() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  var a = function () {\n"
                + "    var x = (function x () { log('a') });\n"
                + "    return function () { x() };\n"
                + "  }();\n"

                + "  var x = function () { log('x') };\n"

                + "  a();\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"functionfunc(){log(norm(func));}", "outer"})
    public void secondFunctionWithSameNameStrict() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION
                + "  function norm(foo) { return ('' + foo).replace(/(\\s)/gm,'') }\n"

                + "  function func () { log('outer'); }\n"

                + "  var x = function func() { log(norm(func)); }\n"

                + "  x();\n"
                + "  func();\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"f1", "f2", "f3", "!f4", "f5", "!f6", "!f7", "!f8", "f10", "f11", "f12", "!f10", "f11", "f12", "f13"})
    @HtmlUnitNYI(CHROME = {"f1", "f2", "f3", "!f4", "f5", "!f6", "!f7", "!f8", "f10", "f11", "f12", "f10", "f11", "f12", "f13"},
            EDGE = {"f1", "f2", "f3", "!f4", "f5", "!f6", "!f7", "!f8", "f10", "f11", "f12", "f10", "f11", "f12", "f13"},
            FF = {"f1", "f2", "f3", "!f4", "f5", "!f6", "!f7", "!f8", "f10", "f11", "f12", "f10", "f11", "f12", "f13"},
            FF78 = {"f1", "f2", "f3", "!f4", "f5", "!f6", "!f7", "!f8", "f10", "f11", "f12", "f10", "f11", "f12", "f13"},
            IE = {"f1", "f2", "f3", "!f4", "f5", "!f6", "!f7", "!f8", "f10", "f11", "f12", "f10", "f11", "f12", "f13"})
    public void functioNamesExceptionsStrict() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + "  'use strict';\n"
                + LOG_TITLE_FUNCTION

                + "  function f1() {"
                + "    log('f1');"
                + "    function f9() { log('f9'); }"
                + "  }\n"

                + "  var f2 = function () { log('f2'); }\n"
                + "  var f3 = function f4() { log('f3'); }\n"
                + "  var f5 = function f5() { log('f5'); }\n"

                + "  !function f6() { log('f6'); };\n"
                + "  (function f7() { log('f7'); });\n"

                + "  void function f8() { log('f8'); }\n"

                + "  try { f1() } catch (e) { log('!f1'); }"
                + "  try { f2() } catch (e) { log('!f2'); }"
                + "  try { f3() } catch (e) { log('!f3'); }"
                + "  try { f4() } catch (e) { log('!f4'); }"
                + "  try { f5() } catch (e) { log('!f5'); }"
                + "  try { f6() } catch (e) { log('!f6'); }"
                + "  try { f7() } catch (e) { log('!f7'); }"
                + "  try { f8() } catch (e) { log('!f8'); }"

                + "  {\n"
                + "    function f10() { log('f10'); }\n"
                + "    var f11 = function () { log('f11'); }\n"
                + "    var f12 = function f12() { log('f12'); }\n"
                + "    f10();\n"
                + "    f11();\n"
                + "    f12();\n"
                + "  }\n"

                + "  try { f10() } catch (e) { log('!f10'); }"
                + "  try { f11() } catch (e) { log('!f11'); }"
                + "  try { f12() } catch (e) { log('!f12'); }"

                + "  function f13() { log('f13') } + 1;"
                + "  try { f13() } catch (e) { log('!f13'); }"

                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void ctorBooleanDocumentAll() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(Boolean(document.all))\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void javaNotAccessable() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  try {\n"
                + "    log(java.lang.Math.PI);\n"
                + "  } catch (e) { log('exception'); }\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("Received: from worker - exception")
    public void javaNotAccessableFromWorker() throws Exception {
        final String html = "<html><body>\n"
            + "<script async>\n"
            + "try {\n"
            + "  var myWorker = new Worker('worker.js');\n"
            + "  myWorker.onmessage = function(e) {\n"
            + "    alert('Received: ' + e.data);\n"
            + "  };\n"
            + "} catch(e) { alert('exception' + e); }\n"
            + "</script></body></html>\n";

        final String workerJs = "var pi = 'from worker';\n"
                + "try {\n"
                + "  pi = pi + ' - ' + java.lang.Math.PI\n"
                + "} catch (e) { pi = pi + ' - ' + 'exception'; }\n"
                + "postMessage(pi);\n";

        getMockWebConnection().setResponse(new URL(URL_FIRST, "worker.js"), workerJs, MimeType.APPLICATION_JAVASCRIPT);

        loadPageWithAlerts2(html, 2000);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"#0", "#1", "2"})
    public void constInLoop() throws Exception {
        final String html = "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "function test() {\n"
                + "  var i;\n"
                + "  for (i = 0; i < 2; i++) {\n"
                + "    const x = '#' + i;\n"
                + "    log(x);\n"
                + "  }\n"
                + "  log(i);\n"
                + "}\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test()'>\n"
                + "</body></html>";

        loadPageVerifyTitle2(html);
    }
}
