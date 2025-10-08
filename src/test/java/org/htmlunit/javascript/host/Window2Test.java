/*
 * Copyright (c) 2002-2025 Gargoyle Software Inc.
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
package org.htmlunit.javascript.host;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.FileUtils;
import org.htmlunit.CookieManager4Test;
import org.htmlunit.WebDriverTestCase;
import org.htmlunit.junit.annotation.Alerts;
import org.htmlunit.junit.annotation.HtmlUnitNYI;
import org.htmlunit.util.MimeType;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Tests for {@link Window}. The only difference with {@link WindowTest} is that these
 * tests already run with BrowserRunner.
 *
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @author Ronald Brill
 * @author Frank Danek
 * @author Carsten Steul
 * @author Colin Alworth
 * @author Christoph Burgmer
 */
public class Window2Test extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Window]", "ReferenceError", "undefined", "undefined", "hello", "hello", "world", "world"})
    public void thisIsWindow() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(this);\n"
            + "  try {\n"
            + "    log(abc);\n"
            + "  } catch(e) { logEx(e) }\n"
            + "  log(this.abc);\n"
            + "  log(this.def);\n"
            + "  this.abc = 'hello';\n"
            + "  def = 'world';\n"
            + "  log(abc);\n"
            + "  log(this.abc);\n"
            + "  log(def);\n"
            + "  log(this.def);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function"})
    public void thisIsWindow2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function hello() {\n"
            + "    var x = 1;\n"
            + "  } \n"
            + "  log(typeof hello);\n"
            + "  log(typeof window.hello);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * "window.controllers" is used by some JavaScript libraries to determine if the browser is Gecko based or not.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"not found", "true"},
            FF = {"found", "true"},
            FF_ESR = {"found", "true"})
    public void FF_controllers() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "if (window.controllers)\n"
            + "  log('found');\n"
            + "else\n"
            + "  log('not found');\n"
            + "window.controllers = 'hello';\n"
            + "log(window.controllers == 'hello');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void FF_controllers_set() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  window.controllers = 'hello';\n"
            + "  log(window.controllers == 'hello');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that properties added to <tt>Function.prototype</tt> are visible on <tt>window.onload</tt>.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"a", "1"})
    public void onload_prototype() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "</script>\n"
            + "</head>"
            + "<body onload='log(1)'>\n"
            + "<script>Function.prototype.x='a'; log(window.onload.x);</script>\n"
            + "</body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SGVsbG8gV29ybGQh", "Hello World!"})
    public void atob() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var data = window.btoa('Hello World!');\n"
            + "  log(data);\n"
            + "  log(window.atob(data));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SGVsbG8gV29ybGQh", "Hello World!"})
    public void atobTrailingWhitespace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var data = window.btoa('Hello World!');\n"
            + "  log(data);\n"
            + "  try {\n"
            + "    log(window.atob(data + ' \\t\\r\\n\\x0C'));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SGVsbG8gV29ybGQh", "Hello World!"})
    public void atobLeadingWhitespace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var data = window.btoa('Hello World!');\n"
            + "  log(data);\n"
            + "  try {\n"
            + "    log(window.atob(' \\t\\r\\n\\x0C' + data));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SGVsbG8gV29ybGQh", "Hello World!"})
    public void atobWhitespace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var data = window.btoa('Hello World!');\n"
            + "  log(data);\n"
            + "  try {\n"
            + "    log(window.atob(data.substr(0, 2) + '  ' + data.substr(2)));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SGVsbG8gV29ybGQh", "InvalidCharacterError/DOMException",
             "InvalidCharacterError/DOMException", "InvalidCharacterError/DOMException"})
    public void atobNbsp() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var data = window.btoa('Hello World!');\n"
            + "  log(data);\n"
            + "  try {\n"
            + "    log(window.atob('\\xA0' + data));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "  try {\n"
            + "    log(window.atob(data + '\\xA0'));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "  try {\n"
            + "    log(window.atob(data.substr(0, 2) + '\\xA0' + data.substr(2)));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"SGVsbG8gV29ybGQh", "InvalidCharacterError/DOMException"})
    public void atobInvalid() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var data = window.btoa('Hello World!');\n"
            + "  log(data);\n"
            + "  try {\n"
            + "    log(window.atob(data.substr(0, 2) + '!' + data.substr(2)));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("InvalidCharacterError/DOMException")
    public void atobMalformedInput() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    window.atob('b');\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("InvalidCharacterError/DOMException")
    public void atobEmptyInput() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    window.atob('b');\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"InvalidCharacterError/DOMException", "InvalidCharacterError/DOMException"})
    public void atobUnicode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    window.btoa('I \\u2661 Unicode!');\n"
            + "  } catch(e) { logEx(e) }\n"
            + "  try {\n"
            + "    window.atob('I \\u2661 Unicode!');\n"
            + "  } catch(e) { logEx(e) }\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"M8OuwqY=", "3\u00C3\u00AE\u00C2\u00A6"})
    public void atobUnicodeOutput() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + "  var data = window.btoa('3\u00C3\u00AE\u00C2\u00A6');\n"
            + "  var dataAtob = window.atob(data);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        verifyJsVariable(driver, "data", getExpectedAlerts()[0]);
        verifyJsVariable(driver, "dataAtob", getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"CSAe", "\t \u001e"})
    public void atobControlChar() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + "  var data = window.btoa('\\t \\u001e');\n"
            + "  var dataAtob = window.atob(data);\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);

        verifyJsVariable(driver, "data", getExpectedAlerts()[0]);
        verifyJsVariable(driver, "dataAtob", getExpectedAlerts()[1]);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"bnVsbA==", "null"})
    public void atobNull() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var data = window.btoa(null);\n"
            + "  log(data);\n"
            + "  log(window.atob(data));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"dW5kZWZpbmVk", "undefined"})
    public void atobUndefined() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var data = window.btoa(undefined);\n"
            + "  log(data);\n"
            + "  log(window.atob(data));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Rhino and Nashorn define some properties in the top scope (see ScriptRuntime and lazilyNames in Rhino),
     * and we don't need them.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"getClass: undefined,undefined", "java: undefined,undefined", "javax: undefined,undefined",
             "javafx: undefined,undefined", "org: undefined,undefined", "com: undefined,undefined",
             "edu: undefined,undefined", "net: undefined,undefined", "JavaAdapter: undefined,undefined",
             "JavaImporter: undefined,undefined", "Continuation: undefined,undefined", "Packages: undefined,undefined",
             "XML: undefined,undefined", "XMLList: undefined,undefined", "Namespace: undefined,undefined",
             "QName: undefined,undefined", "arguments: undefined,undefined", "load: undefined,undefined",
             "loadWithNewGlobal: undefined,undefined", "exit: undefined,undefined", "quit: undefined,undefined",
             "__FILE__: undefined,undefined", "__DIR__: undefined,undefined", "__LINE__: undefined,undefined",
             "context: undefined,undefined", "engine: undefined,undefined", "__noSuchProperty__: undefined,undefined",
             "Java: undefined,undefined", "JSAdapter: undefined,undefined",
             "NaN: number,number", "Infinity: number,number", "eval: function,function", "print: function,function",
             "parseInt: function,function", "parseFloat: function,function",
             "isNaN: function,function", "isFinite: function,function", "encodeURI: function,function",
             "encodeURIComponent: function,function", "decodeURI: function,function",
             "decodeURIComponent: function,function", "escape: function,function", "unescape: function,function"})
    public void topLevelProperties() throws Exception {
        final String[] properties = {
            "getClass", "java", "javax", "javafx", "org", "com", "edu", "net", "JavaAdapter",
            "JavaImporter", "Continuation", "Packages", "XML", "XMLList", "Namespace", "QName", "arguments", "load",
            "loadWithNewGlobal", "exit", "quit", "__FILE__", "__DIR__", "__LINE__", "context", "engine",
            "__noSuchProperty__", "Java", "JSAdapter",
            "NaN", "Infinity", "eval", "print", "parseInt", "parseFloat", "isNaN", "isFinite", "encodeURI",
            "encodeURIComponent", "decodeURI", "decodeURIComponent", "escape", "unescape"};

        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var props = ['" + String.join("', '", properties) + "'];\n"
            + "  for (var i = 0; i < props.length; i++)\n"
            + "    log(props[i] + ': ' + typeof(window[props[i]]) + ',' + typeof(eval('this.' + props[i])));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("javax.script.filename: undefined")
    public void topLevelPropertiesWithDot() throws Exception {
        final String[] properties = {"javax.script.filename"};

        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var props = ['" + String.join("', '", properties) + "'];\n"
            + "  for (var i = 0; i < props.length; i++)\n"
            + "    log(props[i] + ': ' + typeof(window[props[i]]));\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void execScript2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      window.execScript('log(1);');\n"
            + "    }\n"
            + "    catch(e) { logEx(e) }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("TypeError")
    public void execScript_returnValue() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try {\n"
            + "  log(window.execScript('1') === undefined);\n"
            + "}\n"
            + "catch(e) { logEx(e) }\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void collectGarbage() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(typeof CollectGarbage);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"original", "changed"})
    public void eval_localVariable() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var f = document.getElementById('testForm1');\n"
            + "    log(f.text1.value);\n"
            + "    eval('f.text_' + 1).value = 'changed';\n"
            + "    log(f.text1.value);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form id='testForm1'>\n"
            + "    <input id='text1' type='text' name='text_1' value='original'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test window properties that match Prototypes.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function Node() { [native code] }", "function Element() { [native code] }"})
    public void windowProperties() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(window.Node);\n"
            + "    log(window.Element);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test that length of frames collection is retrieved.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0"})
    public void framesLengthZero() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(window.length);\n"
            + "log(window.frames.length);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test that length of frames collection is retrieved when there are frames.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"2", "2", "frame1", "frame2"})
    public void framesLengthAndFrameAccess() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.length);\n"
            + "  log(window.frames.length);\n"
            + "  log(window.frames[0].name);\n"
            + "  log(window.frames.frame2.name);\n"
            + "}\n"
            + "</script>\n"
            + "<frameset rows='50,*' onload='test()'>\n"
            + "<frame name='frame1' src='about:blank'/>\n"
            + "<frame name='frame2' src='about:blank'/>\n"
            + "</frameset>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "2", "2", "2", "true"})
    public void windowFramesLive() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "log(window.length);\n"
            + "var oFrames = window.frames;\n"
            + "log(oFrames.length);\n"
            + "function test() {\n"
            + "  log(oFrames.length);\n"
            + "  log(window.length);\n"
            + "  log(window.frames.length);\n"
            + "  log(oFrames == window.frames);\n"
            + "}\n"
            + "</script>\n"
            + "<frameset rows='50,*' onload='test()'>\n"
            + "<frame src='about:blank'/>\n"
            + "<frame src='about:blank'/>\n"
            + "</frameset>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for http://sourceforge.net/p/htmlunit/bugs/234/
     * and https://bugzilla.mozilla.org/show_bug.cgi?id=443491.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello")
    public void overwriteFunctions_navigator() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    function navigator() {\n"
            + "      log('hello');\n"
            + "    }\n"
            + "    navigator();\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2808901.
     * @throws Exception if an error occurs
     */
    @Test
    public void onbeforeunload_setToString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "</script>\n"
            + "</head>"
            + "<body><script>\n"
            + "  window.onbeforeunload = \"log('x')\";\n"
            + "  window.location = 'about:blank';\n"
            + "</script></body></html>";

        loadPage2(html);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * Regression test for bug 2808901.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "function"})
    public void onbeforeunload_defined() throws Exception {
        onbeforeunload("onbeforeunload", "var x;");
    }

    /**
     * Regression test for bug 2808901.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "object"})
    public void onbeforeunload_notDefined() throws Exception {
        onbeforeunload("onbeforeunload", null);
    }

    private void onbeforeunload(final String name, final String js) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body" + (js != null ? " " + name + "='" + js + "'" : "") + "><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('" + name + "' in window);\n"
            + "  var x = false;\n"
            + "  for(var p in window) { if(p == '" + name + "') { x = true; break; } }\n"
            + "  log(x);\n"
            + "  log(typeof window." + name + ");\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that <tt>window.frames</tt> basically returns a reference to the window.
     * Regression test for bug 2824436.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Window]", "[object Window]", "[object Window]", "1", "true", "true",
             "[object Window]", "true", "true", "no function", "undefined", "true", "true",
             "[object History]", "true", "true", "[object Window]", "true", "true"})
    public void framesAreWindows() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><iframe name='f'></iframe><script>\n"
            + LOG_TITLE_FUNCTION
            + "log(window.frames);\n"
            + "log(window.f);\n"
            + "log(window.frames.f);\n"
            + "log(window.length);\n"
            + "log(window.length == window.frames.length);\n"
            + "log(window.length == window.frames.frames.length);\n"
            + "log(window[0]);\n"
            + "log(window[0] == window.frames[0]);\n"
            + "log(window[0] == window.frames.frames[0]);\n"
            + "try {\n"
            + "  log(window(0));\n"
            + "  log(window(0) == window.frames(0));\n"
            + "  log(window(0) == window.frames.frames(0));\n"
            + "} catch(e) {\n"
            + "  log('no function');\n"
            + "}\n"
            + "log(window[1]);\n"
            + "log(window[1] == window.frames[1]);\n"
            + "log(window[1] == window.frames.frames[1]);\n"
            + "log(window.history);\n"
            + "log(window.history == window.frames.history);\n"
            + "log(window.history == window.frames.frames.history);\n"
            + "log(window.self);\n"
            + "log(window.self == window.frames.self);\n"
            + "log(window.self == window.frames.frames.self);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Tests window.open().
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"Hello window", ""})
    public void open() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  window.open('" + URL_SECOND + "');\n"
            + "</script>\n"
            + "</body></html>";
        final String windowContent = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  window.opener.log('Hello window');\n"
                + "  window.opener.log(window.name);\n"
                + "</script>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse(windowContent);
        loadPageVerifyTitle2(html);

        // for unknown reason, the selenium driven browser is in an invalid state after this test
        releaseResources();
        shutDownAll();
    }

    /**
     * Tests window.open(...) with some params.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"Hello window", "New window"})
    public void openWindowParams() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  window.open('" + URL_SECOND + "', 'New window', 'width=200,height=100');\n"
            + "</script>\n"
            + "</body></html>";
        final String windowContent = DOCTYPE_HTML
                + "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  window.opener.log('Hello window');\n"
                + "  window.opener.log(window.name);\n"
                + "</script>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse(windowContent);
        loadPageVerifyTitle2(html);

        // for unknown reason, the selenium driven browser is in an invalid state after this test
        releaseResources();
        shutDownAll();
    }

    /**
     * Tests window.open(...) with replace param.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("window1window2")
    public void openWindowParamReplace() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + "  function info(msg) {\n"
            + "    document.title += msg;\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  window.open('" + URL_SECOND + "', 'window1', 'width=200,height=100', true);\n"
            + "  window.open('" + URL_SECOND + "', 'window2', 'width=200,height=100', 'true');\n"
            + "</script>\n"
            + "</body></html>";
        final String windowContent = DOCTYPE_HTML
            + "<html><head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  window.opener.info(window.name);\n"
            + "</script>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse(windowContent);
        final WebDriver driver = loadPage2(html);

        Thread.sleep(400);
        assertEquals(getExpectedAlerts()[0], driver.getTitle());

        // for unknown reason, the selenium driven browser is in an invalid state after this test
        releaseResources();
        shutDownAll();
    }

    /**
     * For FF, window's opener can't be set unless to its current value.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Window]", "[object Window] (true)", "1234 (true)", "null (true)", "undefined (true)",
             "[object Window] (true)", "[object Window] (true)", "[object Window] (true)"})
    public void set_opener() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "var otherWindow = window.open('about:blank');\n"
            + "function trySetOpener1(_win, value) {\n"
            + "  try {\n"
            + "    _win.opener = value;\n"
            + "    log(_win.opener + ' (' + (_win.opener === value) + ')');\n"
            + "  }\n"
            + "  catch(e) { logEx(e) }\n"
            + "}\n"
            + "function trySetOpener(_win) {\n"
            + "  var originalValue = _win.opener;\n"
            + "  log(originalValue);\n"
            + "  trySetOpener1(_win, _win.opener);\n"
            + "  trySetOpener1(_win, 1234);\n"
            + "  trySetOpener1(_win, null);\n"
            + "  trySetOpener1(_win, undefined);\n"
            + "  trySetOpener1(_win, _win);\n"
            + "  trySetOpener1(_win, otherWindow);\n"
            + "  trySetOpener1(_win, originalValue);\n"
            + "}\n"
            + "function doTest() {\n"
            + "  trySetOpener(window.open('about:blank'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
        // for some reason, the selenium driven browser is in an invalid state after this test
        releaseResources();
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"ReferenceError", "ReferenceError", "ReferenceError", "ReferenceError"})
    public void IEScriptEngineXxx() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head><script>\n"
            + LOG_TITLE_FUNCTION
            + "try { log(ScriptEngine()); } catch(e) { logEx(e) }\n"
            + "try { log(ScriptEngineMajorVersion()); } catch(e) { logEx(e) }\n"
            + "try { log(ScriptEngineMinorVersion()); } catch(e) { logEx(e) }\n"
            + "try { log(typeof ScriptEngineBuildVersion()); } catch(e) { logEx(e) }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2897473.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"true", "621", "147", "true", "16", "16"},
            EDGE = {"true", "630", "138", "true", "16", "24"},
            FF = {"true", "675", "93", "true", "16", "16"},
            FF_ESR = {"true", "675", "93", "true", "16", "16"})
    @HtmlUnitNYI(CHROME = {"true", "605", "147", "true", "0", "16"},
            EDGE = {"true", "605", "138", "true", "0", "24"},
            FF = {"true", "605", "93", "true", "0", "16"},
            FF_ESR = {"true", "605", "93", "true", "0", "16"})
    public void heightsAndWidths() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.innerHeight > 0);\n"
            + "  log(window.innerHeight - document.body.clientHeight);\n"
            + "  log(window.outerHeight - window.innerHeight);\n"
            + "  log(window.innerWidth > 0);\n"
            + "  log(window.innerWidth - document.body.clientWidth);\n"
            + "  log(window.outerWidth - window.innerWidth);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2897473.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"true", "0", "147", "true", "true", "16"},
            EDGE = {"true", "0", "138", "true", "true", "24"},
            FF = {"true", "0", "93", "true", "true", "16"},
            FF_ESR = {"true", "0", "93", "true", "true", "16"})
    public void heightsAndWidthsQuirks() throws Exception {
        final String html =
            "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.innerHeight > 0);\n"
            + "  log(window.innerHeight - document.body.clientHeight);\n"
            + "  log(window.outerHeight - window.innerHeight);\n"
            + "  log(window.innerWidth > 0);\n"
            + "  log(window.innerWidth == document.body.clientWidth);\n"
            + "  log(window.outerWidth - window.innerWidth);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "1234"})
    public void setInnerWidth() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.innerWidth > 0);\n"
            + "  window.innerWidth = 1234;\n"
            + "  log(window.innerWidth);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "1234"})
    public void setInnerHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.innerHeight > 0);\n"
            + "  window.innerHeight = 1234;\n"
            + "  log(window.innerHeight);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "1234"})
    public void setOuterWidth() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.outerWidth > 0);\n"
            + "  window.outerWidth = 1234;\n"
            + "  log(window.outerWidth);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "1234"})
    public void setOuterHeight() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.outerHeight > 0);\n"
            + "  window.outerHeight = 1234;\n"
            + "  log(window.outerHeight);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2944261.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"0", "1240", "100", "1240"},
            EDGE = {"0", "1232", "100", "1232"},
            FF = {"0", "1240", "100", "1240"},
            FF_ESR = {"0", "1240", "100", "1240"})
    @HtmlUnitNYI(CHROME = {"0", "1256", "100", "1256"},
            EDGE = {"0", "1256", "100", "1256"},
            FF = {"0", "1256", "100", "1256"},
            FF_ESR = {"0", "1256", "100", "1256"})
    // TODO width and height calculation needs to be reworked in HtmlUnit
    // but as the calculation might be effected by e.g. current windows style it is not that simple
    public void changeHeightsAndWidths() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script language='javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var oldHeight = document.body.clientHeight;\n"
            + "    var oldWidth = document.body.clientWidth;\n"
            + "    log(document.body.clientHeight);\n"
            + "    log(document.body.clientWidth);\n"
            + "    newDiv = document.createElement('div');\n"
            + "    document.body.appendChild(newDiv);\n"
            + "    newDiv.style['height'] = oldHeight + 100 + 'px';\n"
            + "    newDiv.style['width'] = oldWidth + 100 + 'px';\n"
            + "    log(document.body.clientHeight);\n"
            + "    log(document.body.clientWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2944261.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(CHROME = {"621", "1256", "606", "1241"},
            EDGE = {"630", "1248", "615", "1233"},
            FF = {"675", "1256", "658", "1239"},
            FF_ESR = {"675", "1256", "658", "1239"})
    // TODO width and height calculation needs to be reworked in HtmlUnit
    // but as the calculation might be effected by e.g. current windows style it is not that simple
    @HtmlUnitNYI(CHROME = {"605", "1256", "705", "1256"},
            EDGE = {"605", "1256", "705", "1256"},
            FF = {"605", "1256", "705", "1256"},
            FF_ESR = {"605", "1256", "705", "1256"})
    public void changeHeightsAndWidthsQuirks() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script language='javascript'>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var oldHeight = document.body.clientHeight;\n"
            + "    var oldWidth = document.body.clientWidth;\n"
            + "    log(document.body.clientHeight);\n"
            + "    log(document.body.clientWidth);\n"
            + "    newDiv = document.createElement('div');\n"
            + "    document.body.appendChild(newDiv);\n"
            + "    newDiv.style['height'] = oldHeight + 100 + 'px';\n"
            + "    newDiv.style['width'] = oldWidth + 100 + 'px';\n"
            + "    log(document.body.clientHeight);\n"
            + "    log(document.body.clientWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2897457.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "0,0", "0,0", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()"},
            FF = {"0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0"},
            FF_ESR = {"0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0"})
    @HtmlUnitNYI(CHROME = {"0,0", "100,200", "110,230", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()"},
            EDGE = {"0,0", "100,200", "110,230", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()"},
            FF = {"0,0", "100,200", "110,230", "0,0", "0,95", "0,0", "0,1210"},
            FF_ESR = {"0,0", "100,200", "110,230", "0,0", "0,95", "0,0", "0,1210"})
    public void scrolling1() throws Exception {
        scrolling(true);
    }

    /**
     * Regression test for bug 2897457.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "0,0", "0,0", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()"},
            FF = {"0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0"},
            FF_ESR = {"0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0"})
    public void scrolling2() throws Exception {
        scrolling(false);
    }

    private void scrolling(final boolean addHugeDiv) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'>\n"
            + (addHugeDiv ? "<div id='d' style='width:10000px;height:10000px;background-color:blue;'></div>\n" : "")
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var b = document.body;\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  window.scrollTo(100, 200);\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  window.scrollBy(10, 30);\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  window.scrollTo(-5, -20);\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  if(window.scrollByLines) {\n"
            + "    window.scrollByLines(5);\n"
            + "    log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    log('no scrollByLines()');\n"
            + "  }\n"
            + "  window.scroll(0, 0);\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  if(window.scrollByPages) {\n"
            + "    window.scrollByPages(2);\n"
            + "    log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    log('no scrollByPages()');\n"
            + "  }\n"
            + "}\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Regression test for bug 2897457.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "0,0", "0,0", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()"},
            FF = {"0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0"},
            FF_ESR = {"0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0"})
    @HtmlUnitNYI(CHROME = {"0,0", "100,200", "110,230", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()"},
            EDGE = {"0,0", "100,200", "110,230", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()"},
            FF = {"0,0", "100,200", "110,230", "0,0", "0,95", "0,0", "0,1210"},
            FF_ESR = {"0,0", "100,200", "110,230", "0,0", "0,95", "0,0", "0,1210"})
    public void scrollingOptions1() throws Exception {
        scrollingOptions(true);
    }

    /**
     * Regression test for bug 2897457.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"0,0", "0,0", "0,0", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()"},
            FF = {"0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0"},
            FF_ESR = {"0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0"})
    public void scrollingOptions2() throws Exception {
        scrollingOptions(false);
    }

    private void scrollingOptions(final boolean addHugeDiv) throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'>\n"
            + (addHugeDiv ? "<div id='d' style='width:10000px;height:10000px;background-color:blue;'></div>\n" : "")
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var b = document.body;\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  window.scrollTo({left: 100, top: 200});\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  window.scrollBy({left: 10, top: 30});\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  window.scrollTo({left: -5, top: -20});\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  if(window.scrollByLines) {\n"
            + "    window.scrollByLines(5);\n"
            + "    log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    log('no scrollByLines()');\n"
            + "  }\n"
            + "  window.scroll({left: 0, top: 0});\n"
            + "  log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  if(window.scrollByPages) {\n"
            + "    window.scrollByPages(2);\n"
            + "    log(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    log('no scrollByPages()');\n"
            + "  }\n"
            + "}\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"0", "0", "0", "0"})
    public void pageXOffset() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  window.scrollBy(5, 10);\n"
            + "  log(window.pageXOffset);\n"
            + "  log(window.pageYOffset);\n"
            + "  log(window.scrollX);\n"
            + "  log(window.scrollY);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("object")
    public void typeof() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof window);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = {"undefined", "undefined"},
            FF = {"12", "89"},
            FF_ESR = {"12", "89"})
    public void mozInnerScreen() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.mozInnerScreenX);\n"
            + "  log(window.mozInnerScreenY);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void mozPaintCount() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(typeof window.mozPaintCount);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"ReferenceError", "ReferenceError", "Success"})
    public void eval() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body onload='test()'><script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var x = new Object();\n"
            + "  x.a = 'Success';\n"
            + "  try {\n"
            + "    log(window['eval']('x.a'));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "  try {\n"
            + "    log(window.eval('x.a'));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "  try {\n"
            + "    log(eval('x.a'));\n"
            + "  } catch(e) { logEx(e) }\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     * @see org.htmlunit.javascript.host.event.EventTest#firedEvent_equals_original_event()
     */
    @Test
    @Alerts({"true", "I was here"})
    public void firedEvent_equals_original_event() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  var myEvent;\n"
            + "  var listener = function(x) {\n"
            + "    log(x == myEvent);\n"
            + "    x.foo = 'I was here';\n"
            + "  }\n"
            + "  window.addEventListener('click', listener, false);\n"
            + "  myEvent = document.createEvent('HTMLEvents');\n"
            + "  myEvent.initEvent('click', true, true);\n"
            + "  window.dispatchEvent(myEvent);\n"
            + "  log(myEvent.foo);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "true", "true", "true"})
    public void thisEquals() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(this === window);\n"
            + "  log(window === this);\n"
            + "  log(this == window);\n"
            + "  log(window == this);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "function", "null", "null"})
    public void onbeforeunload() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.onbeforeunload);\n"
            + "  var handle = function() {};\n"
            + "  window.onbeforeunload = handle;\n"
            + "  log(typeof window.onbeforeunload);\n"
            + "  window.onbeforeunload = null;\n"
            + "  log(window.onbeforeunload);\n"
            + "  window.onbeforeunload = undefined;\n"
            + "  log(window.onbeforeunload);\n"
            + "  \n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Verifies that <tt>this.arguments</tt> works from within a method invocation, in a
     * function defined on the Function prototype object. This usage is required by the
     * Ajax.NET Professional JavaScript library.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "0", "2", "2", "null"})
    public void functionPrototypeArguments() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload='test()'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + " \n"
            + "    Function.prototype.doAlerts = function() {\n"
            + "      log(this == o.f);\n"
            + "      log(arguments ? arguments.length : 'null');\n"
            + "      log(this.arguments ? this.arguments.length : 'null');\n"
            + "    }\n"
            + " \n"
            + "    var o = function() {};\n"
            + "    o.f = function(x, y, z) {\n"
            + "      this.f.doAlerts();\n"
            + "      log(arguments ? arguments.length : 'null');\n"
            + "      log(this.arguments ? this.arguments.length : 'null');\n"
            + "    }\n"
            + "    o.f('a', 'b');\n"
            + "  }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "[object Arguments]", "null", "true", "[object Arguments]", "[object Arguments]"})
    public void functionPrototypeArguments2() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload='test()'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + " \n"
            + "    Function.prototype.doAlerts = function() {\n"
            + "      log(this == o.f);\n"
            + "      log(arguments);\n"
            + "      log(this.arguments);\n"
            + "    }\n"
            + " \n"
            + "    var o = function() {};\n"
            + "    o.f = function(x, y, z) {\n"
            + "      log(this == o);\n"
            + "      log(arguments);\n"
            + "      log(this.arguments);\n"
            + "      this.f.doAlerts();\n"
            + "    }\n"
            + "    o.f('a', 'b');\n"
            + "  }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "[object Arguments]", "null", "true", "[object Arguments]", "null"})
    public void functionPrototypeArguments3() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body onload='test()'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var o = function() {};\n"
            + "    o.x = function() {\n"
            + "      log(this == o);\n"
            + "      log(arguments);\n"
            + "      log(this.arguments);\n"
            + "    }\n"
            + "    o.f = function(x, y, z) {\n"
            + "      log(this == o);\n"
            + "      log(arguments);\n"
            + "      log(this.arguments);\n"
            + "      this.x();\n"
            + "    }\n"
            + "    o.f('a', 'b');\n"
            + "  }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"null", "function", "5"})
    public void onError() throws Exception {
        final String html
            = "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(window.onerror);\n"
            + "  window.onerror = function() { log(arguments.length); };\n"
            + "  log(typeof window.onerror);\n"
            + "  try { log(undef); } catch(e) { /* caught, so won't trigger onerror */ }\n"
            + "  log(undef);\n"
            + "</script>";

        if (getWebDriver() instanceof HtmlUnitDriver) {
            getWebClient().getOptions().setThrowExceptionOnScriptError(false);
        }
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"string string 26 number string",
                "string string 27 number object"})
    public void onErrorExceptionInstance() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  window.onerror = function(messageOrEvent, source, lineno, colno, error) {\n"
                + "    log(typeof messageOrEvent + ' ' + typeof source + ' '"
                                + " + lineno + ' ' + typeof colno + ' ' + typeof error);\n"
                + "  };\n"
                + "</script>\n"
                + "<script>throw 'string';</script>\n"
                + "<script>throw {'object':'property'};</script>\n"
                + "</html>";

        if (getWebDriver() instanceof HtmlUnitDriver) {
            getWebClient().getOptions().setThrowExceptionOnScriptError(false);
        }
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"string string 26 number object", "string string 1 number object"})
    public void onErrorExceptionInstance2() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  window.onerror = function(messageOrEvent, source, lineno, colno, error) {\n"
                + "    log(typeof messageOrEvent + ' ' + typeof source + ' '"
                                + " + lineno + ' ' + typeof colno + ' ' + typeof error);\n"
                + "  };\n"
                + "</script>\n"
                + "<script>does.not.exist();</script>\n"
                + "<script>eval('syntax[error');</script>\n"
                + "</html>";

        if (getWebDriver() instanceof HtmlUnitDriver) {
            getWebClient().getOptions().setThrowExceptionOnScriptError(false);
        }
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("success")
    public void onErrorModifyObject() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  window.onerror = function(messageOrEvent, source, lineno, colno, error) {\n"
                + "    error.property = 'success'\n"
                + "    log(error.property);\n"
                + "  };\n"
                + "</script>\n"
                + "<script>throw {};</script>\n"
                + "</html>";

        if (getWebDriver() instanceof HtmlUnitDriver) {
            getWebClient().getOptions().setThrowExceptionOnScriptError(false);
        }
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(0, 0, 0)")
    public void getComputedStyle() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<div id='myDiv'></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var e = document.getElementById('myDiv');\n"
            + "  log(window.getComputedStyle(e, null).color);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("rgb(255, 0, 0)")
    public void getComputedStyle_WithComputedColor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "  <head>\n"
            + "    <style>div.x { color: red; }</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var e = document.getElementById('d');\n"
            + "    log(window.getComputedStyle(e, '').color);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='d' class='x'>foo bar</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * JS code was throwing an exception due to the incorrect signature of getComputedStyle.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("rgb(0, 0, 0)")
    public void getComputedStyle_svg() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'></svg>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var e = document.getElementById('myId');\n"
            + "  log(window.getComputedStyle(e, null).color);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void getComputedStyleCached() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<div id='myDiv'></div>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var e = document.getElementById('myDiv');\n"
            + "  var cs1 = window.getComputedStyle(e, null);\n"
            + "  var cs2 = window.getComputedStyle(e, null);\n"
            + "  log(cs1 === cs2);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * The reason was that "top" evaluate to WindowProxy and "Object(top)" was setting the top scope as parentScope
     * of the WindowProxy which was setting it on the Window where it should always be null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void hangingObjectCallOnWindowProxy() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<iframe id='it'></iframe>;\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  Object(top);\n"
            + "  log(window.foo);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Was producing "TypeError: Object's getDefaultValue() method returned an object" due to Delegator not delegating
     * getDefaultValue(hint) to delegee when hint is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void equalsString() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('foo' == window);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Was producing "TypeError: Object's getDefaultValue() method returned an object" due to Delegator not delegating
     * getDefaultValue(hint) to delegee when hint is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void equalsInt() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  var i = 0;\n"
            + "  log(i == window);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"number", "done", "result"})
    public void setTimeout() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      var id = window.setTimeout( function() { log('result'); }, 20);\n"
            + "      log(typeof id);\n"
            + "      log('done');\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"number", "done", "42"})
    public void setTimeoutWithParams() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      var id = window.setTimeout( function(p1) { log(p1); }, 20, 42);\n"
            + "      log(typeof id);\n"
            + "      log('done');\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"done 2", "7"})
    public void setTimeoutCode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      try{\n"
            + "        var id = window.setTimeout('log(7)');\n"
            + "        log('done 2');\n"
            + "      } catch(e) { log(e); }\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void setTimeoutWrongParams() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      try{\n"
            + "        window.setTimeout();\n"
            + "        log('done');\n"
            + "      } catch(e) { log(e instanceof TypeError); }\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * As of 19.02.2013, a task started by setTimeout in an event handler could be executed before
     * all events handlers have been executed due to a missing synchronization.
     * @throws Exception if the test fails
     */
    @Test
    public void setTimeoutShouldNotBeExecutedBeforeHandlers() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + "function stop() {\n"
            + "  window.stopIt = true;\n"
            + "}\n"
            + "for (var i = 0; i < 1000; i++) {\n"
            + "  var handler = function(e) {\n"
            + "    if (window.stopIt) {\n"
            + "      e.preventDefault ?  e.preventDefault() : e.returnValue = false;\n"
            + "    }\n"
            + "  }\n"
            + "  window.addEventListener('click', handler, false);\n"
            + "}\n"
            + "</script>\n"
            + "<form action='page2' method='post'>\n"
            + "<input id='it' type='submit' onclick='setTimeout(stop, 0)'>\n"
            + "</form>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("it")).click();

        assertEquals(URL_FIRST + "page2", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"number", "done", "result"})
    public void setInterval() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    var id;\n"
            + "    function test() {\n"
            + "      id = window.setInterval( function() { log('result'); clearInterval(id); }, 20);\n"
            + "      log(typeof id);\n"
            + "      log('done');\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"number", "done", "42"})
    public void setIntervalWithParams() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    var id;\n"
            + "    function test() {\n"
            + "      id = window.setInterval( function(p1) { log(p1); clearInterval(id); }, 20, 42);\n"
            + "      log(typeof id);\n"
            + "      log('done');\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"done 2", "7"})
    public void setIntervalCode() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    var id;\n"
            + "    function test() {\n"
            + "      try{\n"
            + "        id = window.setInterval('log(7); clearInterval(id);' );\n"
            + "        log('done 2');\n"
            + "      } catch(e) { log(e); }\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void setIntervalWrongParams() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + LOG_TEXTAREA_FUNCTION
            + "    function test() {\n"
            + "      try{\n"
            + "        window.setInterval();\n"
            + "        log('done');\n"
            + "      } catch(e) { log(e instanceof TypeError); }\n"
            + "    }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + LOG_TEXTAREA
            + "</body>\n"
            + "</html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * As of 19.02.2013, a task started by setInterval in an event handler could be executed before
     * all events handlers have been executed due to a missing synchronization.
     * @throws Exception if the test fails
     */
    @Test
    public void setIntervalShouldNotBeExecutedBeforeHandlers() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body>\n"
            + "<script>\n"
            + "  var id;\n"

            + "  function stop() {\n"
            + "    window.stopIt = true;\n"
            + "    clearInterval(id);\n"
            + "  }\n"

            + "  for (var i = 0; i < 1000; i++) {\n"
            + "    var handler = function(e) {\n"
            + "      if (window.stopIt) {\n"
            + "        e.preventDefault ?  e.preventDefault() : e.returnValue = false;\n"
            + "      }\n"
            + "    }\n"
            + "    window.addEventListener('click', handler, false);\n"
            + "  }\n"
            + "</script>\n"
            + "<form action='page2' method='post'>\n"
            + "  <input id='it' type='submit' onclick='id = setInterval(stop, 0)'>\n"
            + "</form>\n"
            + "</body></html>";
        getMockWebConnection().setDefaultResponse("Error: not found", 404, "Not Found", MimeType.TEXT_HTML);

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("it")).click();

        assertEquals(URL_FIRST + "page2", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "null"})
    public void onchange_noHandler() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('onchange' in window);\n"
            + "  log(window.onchange);\n"
            + "</script></body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("changed")
    public void onchange_withHandler() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "<script>\n"
            + LOG_WINDOW_NAME_FUNCTION
            + "</script>\n"
            + "</head>"
            + "<body>\n"
            + "<input id='it'/>\n"
            + "<div id='tester'>Tester</div>\n"
            + "<script>\n"
            + "  window.onchange = function() {\n"
            + "    log('changed');\n"
            + "  }\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("it")).sendKeys("X");
        driver.findElement(By.id("tester")).click();

        verifyWindowName2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "null"})
    public void onsubmit_noHandler() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log('onsubmit' in window);\n"
            + "  log(window.onsubmit);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-onsubmit-")
    public void onsubmit_withHandler() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <title>Title</title>\n"
            + "</head>\n"
            + "<body>\n"
            + "<form>\n"
            + "  <input type='submit' id='it' value='submit' />\n"
            + "</form>\n"
            + "<script>\n"
            + "  window.onsubmit = function() {\n"
            + "    window.name = window.name + '-onsubmit-' + '\\u00a7';\n" // hack
            + "  }\n"
            + "</script>\n"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("it")).click();

        verifyWindowName2(driver, getExpectedAlerts()[0]);
    }

    /**
     * Regression test to reproduce a known bug.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("about:blank")
    public void openWindow_emptyUrl() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var w = window.open('');\n"
            + "log(w ? w.document.location : w);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"true", "true", "true"})
    public void location() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(location === window.location);\n"
            + "  log(location === document.location);\n"
            + "  log(window.location === document.location);\n"
            + "</script></head>\n"
            + "<body></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setLocation() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>First</title></head>\n"
            + "<body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='location=\"" + URL_SECOND + "\";'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        assertTitle(driver, "First");
        assertEquals(1, driver.getWindowHandles().size());

        driver.findElement(By.id("link")).click();
        assertTitle(driver, "Second");

        assertEquals(1, driver.getWindowHandles().size());
        assertEquals(new String[] {"", "second/"}, getMockWebConnection().getRequestedUrls(URL_FIRST));
        assertEquals(URL_SECOND.toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setWindowLocation() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>First</title></head>\n"
            + "<body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='window.location=\"" + URL_SECOND + "\";'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        assertTitle(driver, "First");
        assertEquals(1, driver.getWindowHandles().size());

        driver.findElement(By.id("link")).click();
        assertTitle(driver, "Second");

        assertEquals(1, driver.getWindowHandles().size());
        assertEquals(new String[] {"", "second/"}, getMockWebConnection().getRequestedUrls(URL_FIRST));
        assertEquals(URL_SECOND.toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void setDocumentLocation() throws Exception {
        final String firstContent = DOCTYPE_HTML
            + "<html>\n"
            + "<head><title>First</title></head>\n"
            + "<body>\n"
            + "<form name='form1'>\n"
            + "  <a id='link' onClick='document.location=\"" + URL_SECOND + "\";'>Click me</a>\n"
            + "</form>\n"
            + "</body></html>";
        final String secondContent = DOCTYPE_HTML
            + "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent);
        assertTitle(driver, "First");
        assertEquals(1, driver.getWindowHandles().size());

        driver.findElement(By.id("link")).click();
        assertTitle(driver, "Second");

        assertEquals(1, driver.getWindowHandles().size());
        assertEquals(new String[] {"", "second/"}, getMockWebConnection().getRequestedUrls(URL_FIRST));
        assertEquals(URL_SECOND.toString(), driver.getCurrentUrl());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"[object Window]", "function Window() { [native code] }",
                       "TEMPORARY, PERSISTENT, "},
            FF = {"[object Window]", "function Window() { [native code] }", ""},
            FF_ESR = {"[object Window]", "function Window() { [native code] }", ""})
    public void enumeratedProperties() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var str = '';\n"
            + "    log(window);\n"
            + "    log(Window);\n"
            + "    var str = '';\n"
            + "    for (var i in Window) {\n"
            + "      str += i + ', ';\n"
            + "    }\n"
            + "    log(str);\n"
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
    @Alerts(DEFAULT = "undefined",
            FF = "function",
            FF_ESR = "function")
    public void dump() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof window.dump);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function"})
    public void requestAnimationFrame() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof window.requestAnimationFrame);\n"
            + "  log(typeof window.cancelAnimationFrame);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void showModalDialog() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof window.showModalDialog);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void showModelessDialog() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof window.showModelessDialog);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Another strange browser detection trick.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("function")
    public void find() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(typeof window.find);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for Bug #1765.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\"0.33\"")
    public void getComputedStylePseudo() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <style>\n"
            + "    #mydiv:before {\n"
            + "      content: '0.33';\n"
            + "    }\n"
            + "  </style>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var div = document.getElementById('mydiv');\n"
            + "      log(window.getComputedStyle(div, ':before').getPropertyValue('content'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * Test for Bug #1768.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("\"0.33\"")
    public void getComputedStylePseudoCache() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "  <style>\n"
            + "    #mydiv:before {\n"
            + "      content: '0.33';\n"
            + "    }\n"
            + "  </style>\n"
            + "  <script>\n"
            + LOG_TITLE_FUNCTION
            + "    function test() {\n"
            + "      var div = document.getElementById('mydiv');\n"
            + "      div.getBoundingClientRect();\n"
            + "      log(window.getComputedStyle(div, ':before').getPropertyValue('content'));\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <div id='mydiv'></div>\n"
            + "</body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("inline")
    public void getComputedStyleElementDocument() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<style>\n"
            + "  tt { display: none; }\n"
            + "</style>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var iframe = document.createElement('iframe');\n"
            + "    document.body.appendChild(iframe);\n"
            + "\n"
            + "    var doc = iframe.contentWindow.document;\n"
            + "    var tt = doc.createElement('tt');\n"
            + "    doc.body.appendChild(tt);\n"
            + "    log(window.getComputedStyle(tt, null).display);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>\n";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"1", "false", "true", "false", "false"})
    public void in() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "function test() {\n"
            + "  log(window.length);\n"
            + "  log(-1 in window);\n"
            + "  log(0 in window);\n"
            + "  log(1 in window);\n"
            + "  log(42 in window);\n"
            + "}\n"
            + "</script>\n"
            + "<frameset rows='50,*' onload='test()'>\n"
            + "<frame name='frame1' src='about:blank'/>\n"
            + "</frameset>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"a", "b"})
    public void calledTwice() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  log('a');\n"
            + "  log('b');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("TypeError")
    public void constructor() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      log(new Window());\n"
            + "    } catch(e) { logEx(e); }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * Test case for <a href="https://github.com/HtmlUnit/htmlunit/issues/482">
     * https://github.com/HtmlUnit/htmlunit/issues/482</a>.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("0")
    public void constructorError() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      var divs = document.querySelectorAll('div');\n"
            + "      var a = Array.from.call(window, divs);\n"
            + "      log(a.length);\n"
            + "    } catch(e) { logEx(e) }\n"
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
    @Alerts({"false", "false", "test2 alert"})
    public void objectCallOnFrameWindow() throws Exception {
        final String firstContent = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_WINDOW_NAME_FUNCTION
                + "  function test1() {\n"
                + "    log(window.frames[0].test2 === undefined);\n"
                + "    Object(window.frames[0]);\n"
                + "  }\n"
                + "</script>\n"
                + "</head>\n"
                + "<body>\n"
                + "  <iframe src='" + URL_SECOND + "'></iframe>\n"
                + "</body></html>\n";
        final String secondContent = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_WINDOW_NAME_FUNCTION
                + "  function test2() {\n"
                + "    log('test2 alert');\n"
                + "  }\n"
                + "  window.top.test1();\n"
                + "  log(test2 === undefined);\n"
                + "</script>\n"
                + "</head>\n"
                + "<body onload='test2()'>\n"
                + "</body></html>\n";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        loadPage2(firstContent);
        verifyWindowName2(getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Window]", "[object WindowProperties]", "[object EventTarget]", "[object Object]"})
    @HtmlUnitNYI(CHROME = {"[object Window]", "[object EventTarget]", "[object Object]"},
            EDGE = {"[object Window]", "[object EventTarget]", "[object Object]"},
            FF = {"[object Window]", "[object EventTarget]", "[object Object]"},
            FF_ESR = {"[object Window]", "[object EventTarget]", "[object Object]"})
    public void test__proto__() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    try {\n"
            + "      for (var p = this.__proto__; p != null; p = p.__proto__) {\n"
            + "        log(p);\n"
            + "      }\n"
            + "    } catch(e) { logEx(e) }\n"
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
    @Alerts({"false", "false", "false", "false"})
    public void xmlNotInWindow() throws Exception {
        final String html
            = "<html xmlns='http://www.w3.org/1999/xhtml' xmlns:me='http://mysite'>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log('XML' in window);\n"
            + "    log('XMLList' in window);\n"
            + "    log('Namespace' in window);\n"
            + "    log('QName' in window);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <app:dIv xmlns='http://anotherURL'></app:dIv>\n"
            + "</body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"[object Window]", "true", "true"})
    public void globalThis() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head></head><body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  try {\n"
            + "    log(globalThis);\n"
            + "    log(window === globalThis);\n"
            + "    log(self === globalThis);\n"
            + "  } catch(e) { log('globalThis is undefined'); }"
            + "</script>\n"
            + "</body></html>";
        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function"})
    public void defineGetterSetter() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(typeof window.__defineGetter__);\n"
            + "    log(typeof window.__lookupGetter__);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"function", "function"})
    public void defineGetterSetter_standards() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    log(typeof window.__defineGetter__);\n"
            + "    log(typeof window.__lookupGetter__);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello")
    public void delegatorAnd__defineGetter__() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    window.__defineGetter__('foo', function() { return 'hello' });\n"
                + "    log(window.foo);\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'></body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("worldworld")
    public void delegatorAnd__defineSetter__() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    window.__defineSetter__('foo', function(a) { document.title = a; });\n"
                + "    window.foo = 'world';\n"
                + "    log(document.title);\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'></body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void userDefinedProperty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    window.count = 0;\n"
                + "    window.count++;\n"
                + "    log(window.count);\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'></body>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"undefined", "{\"enumerable\":true,\"configurable\":true}",
             "[object Event]", "{\"enumerable\":true,\"configurable\":true}"})
    public void eventProperty() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(window.event);\n"
                + "    log(JSON.stringify(Object.getOwnPropertyDescriptor(window, 'event')));\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'></body>\n"
                + "<script>\n"
                + "  log(window.event);\n"
                + "  log(JSON.stringify(Object.getOwnPropertyDescriptor(window, 'event')));\n"
                + "</script>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("{\"value\":7,\"writable\":true,\"enumerable\":true,\"configurable\":true}")
    public void eventPropertyReplaced() throws Exception {
        final String html = DOCTYPE_HTML
                + "<html><head>\n"
                + "<script>\n"
                + LOG_TITLE_FUNCTION
                + "  function test() {\n"
                + "    log(JSON.stringify(Object.getOwnPropertyDescriptor(window, 'event')));\n"
                + "  }\n"
                + "</script></head>\n"
                + "<body onload='test()'></body>\n"
                + "<script>\n"
                + "  event = 7;\n"
                + "</script>\n"
                + "</html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void isSecureContextLocalhost() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(window.hasOwnProperty('isSecureContext') ? isSecureContext : 'not available');\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void isSecureContextHttp() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(window.hasOwnProperty('isSecureContext') ? isSecureContext : 'not available');\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html, new URL(CookieManager4Test.URL_HOST1));
        verifyTitle2(driver, getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void isSecureContextHttpS() throws Exception {
        final WebDriver driver = loadPage2(new URL("https://www.wetator.org/HtmlUnit"), StandardCharsets.UTF_8);

        final String script = "return window.isSecureContext";
        final Object result = ((JavascriptExecutor) driver).executeScript(script);
        assertEquals(getExpectedAlerts()[0], "" + result);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void isSecureContextFile() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  log(window.hasOwnProperty('isSecureContext') ? isSecureContext : 'not available');\n"
            + "</script></body></html>";

        final File currentDirectory = new File((new File("")).getAbsolutePath());
        final File tmpFile = File.createTempFile("isSecureContext", ".html", currentDirectory);
        tmpFile.deleteOnExit();
        final String encoding = (new OutputStreamWriter(new ByteArrayOutputStream())).getEncoding();
        FileUtils.writeStringToFile(tmpFile, html, encoding);

        final WebDriver driver = getWebDriver();
        driver.get("file://" + tmpFile.getCanonicalPath());

        final String script = "return window.isSecureContext";
        final Object result = ((JavascriptExecutor) driver).executeScript(script);
        assertEquals(getExpectedAlerts()[0], "" + result);

        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("false")
    public void isSecureContextAboutBlank() throws Exception {
        final WebDriver driver = getWebDriver();
        driver.get("about:blank");

        final String script = "return window.isSecureContext";
        final Object result = ((JavascriptExecutor) driver).executeScript(script);
        assertEquals(getExpectedAlerts()[0], "" + result);

        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("inline")
    public void getComputedStyleShouldLoadOnlyStylesheets() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"

            + "<link rel='stylesheet' href='imp.css'>\n"
            + "<link rel='alternate' href='alternate.css'>\n"

            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "  function test() {\n"
            + "    var tt = document.getElementById('tt');\n"
            + "    log(window.getComputedStyle(tt, null).display);\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <p id='tt'>abcd</p>\n"
            + "</body></html>\n";

        String css = "p { display: inline };";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "imp.css"), css, MimeType.TEXT_CSS);

        css = "p { display: none };";
        getMockWebConnection().setResponse(new URL(URL_FIRST, "alternate.css"), css, MimeType.TEXT_CSS);

        final int requestCount = getMockWebConnection().getRequestCount();
        loadPageVerifyTitle2(html);

        assertEquals(2, getMockWebConnection().getRequestCount() - requestCount);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("length[GSCE]")
    public void lengthProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  let property = 'length';\n"
            + "  let desc = Object.getOwnPropertyDescriptor(window, property);\n"
            + "  property += '[';\n"
            + "  if (desc.get != undefined) property += 'G';\n"
            + "  if (desc.set != undefined) property += 'S';\n"
            + "  if (desc.writable) property += 'W';\n"
            + "  if (desc.configurable) property += 'C';\n"
            + "  if (desc.enumerable) property += 'E';\n"
            + "  property += ']'\n"
            + "  log(property);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }


    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"1", "two", "undefined"})
    public void lengthPropertyEdit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<iframe></iframe>"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  log(window.length);\n"

            + "  window.length = 'two';\n"
            + "  log(window.length);\n"

            + "  delete window.length;\n"
            + "  log(window.length);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("self[GSCE]")
    public void selfProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  let property = 'self';\n"
            + "  let desc = Object.getOwnPropertyDescriptor(window, property);\n"
            + "  property += '[';\n"
            + "  if (desc.get != undefined) property += 'G';\n"
            + "  if (desc.set != undefined) property += 'S';\n"
            + "  if (desc.writable) property += 'W';\n"
            + "  if (desc.configurable) property += 'C';\n"
            + "  if (desc.enumerable) property += 'E';\n"
            + "  property += ']'\n"
            + "  log(property);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Window]", "tester", "two", "undefined"})
    public void selfPropertyEdit() throws Exception {
        final String html = "<html><head>\n"
            + "<title>tester</title>"
            + "</head>\n"
            + "<body>\n"
            + LOG_TEXTAREA
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION

            + "  log(window.self);\n"
            + "  log(window.self.document.title);\n"

            + "  window.self = 'two';\n"
            + "  log(window.self);\n"

            + "  delete window.self;\n"
            + "  log(window.self);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("frames[GSCE]")
    public void framesProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html>\n"
            + "<head></head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  let property = 'frames';\n"
            + "  let desc = Object.getOwnPropertyDescriptor(window, property);\n"
            + "  property += '[';\n"
            + "  if (desc.get != undefined) property += 'G';\n"
            + "  if (desc.set != undefined) property += 'S';\n"
            + "  if (desc.writable) property += 'W';\n"
            + "  if (desc.configurable) property += 'C';\n"
            + "  if (desc.enumerable) property += 'E';\n"
            + "  property += ']'\n"
            + "  log(property);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Window]", "tester", "1", "two", "undefined"})
    public void framesPropertyEdit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<title>tester</title>"
            + "</head>\n"
            + "<body>\n"
            + "<iframe></iframe>"
            + LOG_TEXTAREA
            + "<script>\n"
            + LOG_TEXTAREA_FUNCTION

            + "  log(window.frames);\n"
            + "  log(window.frames.document.title);\n"
            + "  log(window.frames.length);\n"

            + "  window.frames = 'two';\n"
            + "  log(window.frames);\n"

            + "  delete window.frames;\n"
            + "  log(window.frames);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTextArea2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("parent[GSCE]")
    public void parentProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  let property = 'parent';\n"
            + "  let desc = Object.getOwnPropertyDescriptor(window, property);\n"
            + "  property += '[';\n"
            + "  if (desc.get != undefined) property += 'G';\n"
            + "  if (desc.set != undefined) property += 'S';\n"
            + "  if (desc.writable) property += 'W';\n"
            + "  if (desc.configurable) property += 'C';\n"
            + "  if (desc.enumerable) property += 'E';\n"
            + "  property += ']'\n"
            + "  log(property);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Window]", "two", "undefined"})
    public void parentPropertyEdit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  log(window.parent);\n"

            + "  window.parent = 'two';\n"
            + "  log(window.parent);\n"

            + "  delete window.parent;\n"
            + "  log(window.parent);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"clientInformation[GSCE]", "undefined"})
    public void clientInformationProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  let property = 'clientInformation';\n"

            + "  let desc = Object.getOwnPropertyDescriptor(window, property);\n"
            + "  property += '[';\n"
            + "  if (desc.get != undefined) property += 'G';\n"
            + "  if (desc.set != undefined) property += 'S';\n"
            + "  if (desc.writable) property += 'W';\n"
            + "  if (desc.configurable) property += 'C';\n"
            + "  if (desc.enumerable) property += 'E';\n"
            + "  property += ']'\n"
            + "  log(property);\n"

            + "  delete window.clientInformation;\n"

            + "  desc = Object.getOwnPropertyDescriptor(window, property);\n"
            + "  log(desc);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Navigator]", "two", "undefined"})
    @HtmlUnitNYI(CHROME = {"[object Navigator]", "two", "two"},
            EDGE = {"[object Navigator]", "two", "two"},
            FF = {"[object Navigator]", "two", "two"},
            FF_ESR = {"[object Navigator]", "two", "two"})
    public void clientInformationPropertyEdit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  log(window.clientInformation);\n"

            + "  window.clientInformation = 'two';\n"
            + "  log(window.clientInformation);\n"

            + "  delete window.clientInformation;\n"
            + "  log(window.clientInformation);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"performance[GSCE]", "undefined"})
    public void performanceProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  let property = 'performance';\n"

            + "  let desc = Object.getOwnPropertyDescriptor(window, property);\n"
            + "  property += '[';\n"
            + "  if (desc.get != undefined) property += 'G';\n"
            + "  if (desc.set != undefined) property += 'S';\n"
            + "  if (desc.writable) property += 'W';\n"
            + "  if (desc.configurable) property += 'C';\n"
            + "  if (desc.enumerable) property += 'E';\n"
            + "  property += ']'\n"
            + "  log(property);\n"

            + "  delete window.performance;\n"

            + "  desc = Object.getOwnPropertyDescriptor(window, property);\n"
            + "  log(desc);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"[object Performance]", "two", "undefined"})
    @HtmlUnitNYI(CHROME = {"[object Performance]", "two", "two"},
            EDGE = {"[object Performance]", "two", "two"},
            FF = {"[object Performance]", "two", "two"},
            FF_ESR = {"[object Performance]", "two", "two"})
    public void performancePropertyEdit() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION

            + "  log(window.performance);\n"

            + "  window.performance = 'two';\n"
            + "  log(window.performance);\n"

            + "  delete window.performance;\n"
            + "  log(window.performance);\n"

            + "</script>\n"
            + "</body></html>\n";

        loadPageVerifyTitle2(html);
    }

    /**
     * The setInterval execution is not stopped if the callback throws an exception.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({"c0", "c1", "c2", "c3", "c4", "cancelled"})
    public void setIntervallProceeds() throws Exception {
        final String content = DOCTYPE_HTML
            + "<html>\n"
            + "<head>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + LOG_TITLE_FUNCTION
            + "var intervalID = setInterval(myCallback, 50);\n"
            + "var count = 0;\n"
            + "function myCallback() {\n"
            + "  log('c' + count);\n"
            + "  if (count == 4) {\n"
            + "    clearInterval(intervalID);\r\n"
            + "    log('cancelled');\r\n"
            + "  }\n"
            + "  count++;\n"
            + "  test.hide();\n"
            + "}\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPage2(content);
        verifyTitle2(DEFAULT_WAIT_TIME, getWebDriver(), getExpectedAlerts());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true",
                       "function get opener() { [native code] }",
                       "function set opener() { [native code] }",
                       "undefined", "true", "true"},
            FF = {"true",
                  "function opener() { [native code] }",
                  "function opener() { [native code] }",
                  "undefined", "true", "true"},
            FF_ESR = {"true",
                      "function opener() { [native code] }",
                      "function opener() { [native code] }",
                      "undefined", "true", "true"})
    @HtmlUnitNYI(
            CHROME = {"false",
                      "function opener() { [native code] }", "function opener() { [native code] }",
                      "undefined", "true", "true"},
            EDGE = {"false",
                    "function opener() { [native code] }", "function opener() { [native code] }",
                    "undefined", "true", "true"},
            FF = {"false",
                  "function opener() { [native code] }", "function opener() { [native code] }",
                  "undefined", "true", "true"},
            FF_ESR = {"false",
                      "function opener() { [native code] }", "function opener() { [native code] }",
                      "undefined", "true", "true"})
    public void openerProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  let desc = Object.getOwnPropertyDescriptor(Window.prototype, 'opener');\n"
            + "  log(desc === undefined);\n"

            + "  desc = Object.getOwnPropertyDescriptor(window, 'opener');\n"
            + "  log(desc.get);\n"
            + "  log(desc.set);\n"
            + "  log(desc.writable);\n"
            + "  log(desc.configurable);\n"
            + "  log(desc.enumerable);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = {"true", "function get top() { [native code] }", "undefined", "undefined", "false", "true"},
            FF = {"true", "function top() { [native code] }", "undefined", "undefined", "false", "true"},
            FF_ESR = {"true", "function top() { [native code] }", "undefined", "undefined", "false", "true"})
    @HtmlUnitNYI(
            CHROME = {"false", "function top() { [native code] }", "undefined", "undefined", "true", "true"},
            EDGE = {"false", "function top() { [native code] }", "undefined", "undefined", "true", "true"},
            FF = {"false", "function top() { [native code] }", "undefined", "undefined", "true", "true"},
            FF_ESR = {"false", "function top() { [native code] }", "undefined", "undefined", "true", "true"})
    public void topProperty() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_TITLE_FUNCTION
            + "  let desc = Object.getOwnPropertyDescriptor(Window.prototype, 'top');\n"
            + "  log(desc === undefined);\n"

            + "  desc = Object.getOwnPropertyDescriptor(window, 'top');\n"
            + "  log(desc.get);\n"
            + "  log(desc.set);\n"
            + "  log(desc.writable);\n"
            + "  log(desc.configurable);\n"
            + "  log(desc.enumerable);\n"
            + "</script></body></html>";

        loadPageVerifyTitle2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({"true", "[object Window]", "[object Window]", "[object Window]", "[object Window]"})
    public void overwriteProperty_top() throws Exception {
        final String html = DOCTYPE_HTML
            + "<html><body><script>\n"
            + LOG_SESSION_STORAGE_FUNCTION
            + "  log(window.top === this);\n"
            + "  var top = 123;\n"
            + "  log(top);\n"
            + "  log(window.top);\n"

            + "  window.top = 123;\n"
            + "  log(top);\n"
            + "  log(window.top);\n"
            + "</script></body></html>";

        final WebDriver driver = loadPage2(html);
        verifySessionStorage2(driver, getExpectedAlerts());
    }
}
