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
package com.gargoylesoftware.htmlunit.javascript;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF24;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE11;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Same scope as {@link JavaScriptEngineTest} but extending {@link WebDriverTestCase}.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Frank Danek
 * @author Ronald Brill
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
    @Alerts(FF = {"in goo", "in hoo", "foo error" },
            IE = {"in goo", "in hoo", "in foo" })
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
            IE = { "function foo() {}", "function foo() {}" })
    @NotYetImplemented(IE)
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
    @Alerts(DEFAULT = { "[object Window]", "[object Window]", "true",
                "[object HTMLDocument]", "[object HTMLDocument]", "true", "function" },
            FF = { "[object Window]", "[object Window]", "true",
                "function HTMLDocument() {\n    [native code]\n}",
                "function HTMLDocument() {\n    [native code]\n}", "true", "function" },
            IE8 = { "undefined", "exception", "undefined", "exception", "function" })
    @NotYetImplemented(FF24)
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

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "function", "function" },
            IE8 = { "undefined", "undefined" })
    public void inline() throws Exception {
        final String html = "<html><head><script>\n"
                + "alert(typeof Array.prototype.filter);\n"
                + "  function test() {\n"
                + "    alert(typeof Array.prototype.filter);\n"
                + "  }\n"
                + "</script></head><body onload='test()'>\n"
                + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "found",
            IE8 = "")
    public void enumerateMethods() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    for (var x in document) {\n"
            + "      if (x == 'getElementById')\n"
            + "        alert('found');"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
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
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var a = [1, 2, 3];\n"
            + "    for (var i=10; i<20; ++i)\n"
            + "      a[i] = 't' + i;\n"
            + "    var b = [1, 2, 3];\n"
            + "    b.concat(a);\n"
            + "    alert(b.length);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function f() {}")
    @NotYetImplemented
    public void function_toStringValue() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function f() {}\n"
            + "  function test() {\n"
            + "    alert(String(f));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = { "1", "2" })
    @BuggyWebDriver({ IE11, FF24 })
    public void function_object_method() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
                + "  try {\n"
                + "    alert('1');\n"
                + "    function document.onclick() {\n"
                + "      alert('hi');\n"
                + "    }\n"
                + "    alert('2');\n"
                + "  } catch(e) {alert(e)}\n"
                + "</script></head>\n"
                + "<body>\n"
                + "  <div id='myDiv'>Hello there</div>\n"
                + "</body></html>";

        try {
            loadPageWithAlerts2(html);
            if (getExpectedAlerts().length == 0) {
                Assert.fail("WebDriverException expected");
            }
        }
        catch (final WebDriverException e) {
            // at the moment we do not get the syntax exception when running in selenium
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("that's it")
    public void quoteAsUnicodeInString() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "alert('that\\x27s it');\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("error")
    public void recursion() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function recurse(c) {\n"
            + "    try {\n"
            + "      recurse(c++);\n"
            + "    } catch (e) {\n"
            + "      alert('error');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='recurse(1)'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * String value of native functions starts with \n on IE.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(IE)
    @Alerts(FF = { "0", "false", "0" },
            IE = { "1", "true", "1" })
    public void nativeFunction_toStringValue() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(String(window.alert).indexOf('function'));\n"
            + "    alert(String(window.alert).charAt(0) == '\\n');\n"
            + "    alert(String(document.getElementById).indexOf('function'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug https://sf.net/tracker/?func=detail&atid=448266&aid=1609944&group_id=47038.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts()
    @NotYetImplemented
    public void onloadJavascriptFunction() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function onload() {alert('foo');}"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void alert() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "  alert('foo')\n"
            + "</script></head><body>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
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
            + "  var f = new Function('alert(\"foo\")');\n"
            + "  f();\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void scopeOfNestedNewFunction() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  var foo = 'foo';\n"
            + "  var f1 = new Function('f = new Function(\"alert(foo)\"); f()');\n"
            + "  f1();\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
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
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    document.form1.textfield1.value = 1;\n"
            + "    alert(document.form1.textfield1.value)\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<p>hello world</p>\n"
            + "<form name='form1'>\n"
            + "    <input type='text' name='textfield1' id='textfield1' value='foo' />\n"
            + "    <input type='text' name='textfield2' id='textfield2'/>\n"
            + "</form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void javaScriptWrappedInHtmlComments() throws Exception {
        final String html
            = "<html><head><title>foo</title><script language='javascript'><!--\n"
            + "function doTest() {\n"
            + "  alert('foo');\n"
            + "}\n"
            + "-->\n</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void javaScriptWrappedInHtmlComments2() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script><!--\n"
            + " alert('1')\n"
            + "--></script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void javaScriptWrappedInHtmlComments_commentOnOpeningLine() throws Exception {
        final String html
            = "<html><head><title>foo</title><script language='javascript'><!-- Some comment here\n"
            + "function doTest() {\n"
            + " alert('1')\n"
            + "}\n"
            + "-->\n</script></head>\n"
            + "<body onload='doTest()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 1714762.
     * @throws Exception if the test fails
     */
    @Test
    public void javaScriptWrappedInHtmlComments_commentNotClosed() throws Exception {
        final String html
            = "<html><head><title>foo</title>\n"
            + "<script language='javascript'><!-- alert(1);</script>\n"
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
    @Alerts({ "2", "3" })
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
    @Alerts({ "rstlne-rstlne-rstlne", "rstlno-rstlne-rstlne",
            "rstlna-rstlne-rstlne", "rstlne-rstlne-rstlne",
            "rstlni-rstlni-rstlni", "rstlna-rstlna-rstlna" })
    public void regExpSupport() throws Exception {
        final String html = "<html>\n"
            + "  <head>\n"
            + "    <title>test</title>\n"
            + "    <script id='a'>\n"
            + "       var s = new String('rstlne-rstlne-rstlne');\n"
            + "       alert(s);\n"
            + "       s = s.replace('e', 'o');\n"
            + "       alert(s);\n"
            + "       s = s.replace(/o/, 'a');\n"
            + "       alert(s);\n"
            + "       s = s.replace(new RegExp('a'), 'e');\n"
            + "       alert(s);\n"
            + "       s = s.replace(new RegExp('e', 'g'), 'i');\n"
            + "       alert(s);\n"
            + "       s = s.replace(/i/g, 'a');\n"
            + "       alert(s);\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body>abc</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test ECMA reserved keywords... that are accepted by "normal" browsers
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("123")
    public void ecmaReservedKeywords() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "  var o = {float: 123};"
            + "  alert(o.float);"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
