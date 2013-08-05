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
package com.gargoylesoftware.htmlunit.javascript.host;

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF10;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF3_6;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Window}. The only difference with {@link WindowTest} is that these
 * tests already run with BrowserRunner.
 *
 * @version $Revision$
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class Window2Test extends WebDriverTestCase {

    /**
     * "window.controllers" is used by some JavaScript libraries to determine if the
     * browser is Gecko based or not.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "not found", "true" },
            FF3_6 = { "found", "exception", "false" },
            FF = { "found", "true" })
    @NotYetImplemented(FF3_6)
    public void FF_controllers() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "if (window.controllers)\n"
            + "  alert('found')\n"
            + "else\n"
            + "  alert('not found')\n"
            + "try {\n"
            + "  window.controllers = 'hello';\n"
            + "}\n"
            + "catch(e) { alert('exception') }\n"
            + "alert(window.controllers == 'hello');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Very strange: in FF3 it seems that you can set window.controllers if you haven't
     * accessed it before.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void FF_controllers_set() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  window.controllers = 'hello';\n"
            + "}\n"
            + "catch(e) { alert('exception') }\n"
            + "alert(window.controllers == 'hello');\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that properties added to <tt>Function.prototype</tt> are visible on <tt>window.onload</tt>.
     * See bug 2318508.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "a", "1" })
    public void onload_prototype() throws Exception {
        final String html
            = "<html><body onload='alert(1)'>\n"
            + "<script>Function.prototype.x='a'; alert(window.onload.x);</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(FF)
    @Alerts({ "SGVsbG8gV29ybGQh", "Hello World!" })
    public void atob() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "  var data = window.btoa('Hello World!');\n"
            + "  alert(data);\n"
            + "  alert(atob(data));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * In {@link net.sourceforge.htmlunit.corejs.javascript.ScriptRuntime}, Rhino defines a bunch of properties
     * in the top scope (see lazilyNames). Not all make sense for HtmlUnit.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "RegExp: function", "javax: undefined", "org: undefined", "com: undefined", "edu: undefined",
        "net: undefined", "JavaAdapter: undefined", "JavaImporter: undefined", "Continuation: undefined" })
    public void rhino_lazilyNames() throws Exception {
        final String[] properties = {"RegExp", "javax", "org", "com", "edu", "net",
            "JavaAdapter", "JavaImporter", "Continuation"};
        doTestRhinoLazilyNames(properties);
    }

    /**
     * The same as in {@link #rhino_lazilyNames()} but for properties with different expectations for IE and FF.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "java: undefined", "getClass: undefined" },
            FF10 = { "java: function", "getClass: undefined" }, //FF 16 gives 'undefined' for both
            IE = { "java: undefined", "getClass: undefined" })
    @NotYetImplemented(FF10)
    public void rhino_lazilyNames2() throws Exception {
        doTestRhinoLazilyNames("java", "getClass");
    }

    /**
     * The same as in {@link #rhino_lazilyNames()} but for properties where it doesn't work yet.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented({ FF3_6, FF10 })
    @Alerts(FF3_6 = { "Packages: object", "XML: function", "XMLList: function",
                    "Namespace: function", "QName: function" },
            FF10 = { "Packages: function", "XML: function", "XMLList: function",
                            "Namespace: function", "QName: function" },
            DEFAULT = { "Packages: undefined", "XML: undefined", "XMLList: undefined",
            "Namespace: undefined", "QName: undefined" })
    public void rhino_lazilyNames3() throws Exception {
        doTestRhinoLazilyNames("Packages", "XML", "XMLList", "Namespace", "QName");
    }

    private void doTestRhinoLazilyNames(final String... properties) throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<script>\n"
            + "  var props = ['" + StringUtils.join(properties, "', '") + "'];\n"
            + "  for (var i=0; i<props.length; ++i)\n"
            + "    alert(props[i] + ': ' + typeof(window[props[i]]));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = "1")
    public void execScript2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      window.execScript('alert(1);');\n"
            + "    }\n"
            + "    catch(e) { alert('exception') }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = "true")
    public void execScript_returnValue() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "try {\n"
            + "  alert(window.execScript('1') === undefined);\n"
            + "}\n"
            + "catch(e) { alert('exception') }\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "function", FF = "undefined")
    public void collectGarbage() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(typeof CollectGarbage);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "original", "changed" })
    public void eval_localVariable() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var f = document.getElementById('testForm1');\n"
            + "    alert(f.text1.value);\n"
            + "    eval('f.text_' + 1).value = 'changed';\n"
            + "    alert(f.text1.value);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <form id='testForm1'>\n"
            + "    <input id='text1' type='text' name='text_1' value='original'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test window properties that match Prototypes.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined" },
            FF = { "[object Node]", "[object Element]" })
    public void windowProperties() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window.Node);\n"
            + "    alert(window.Element);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test that length of frames collection is retrieved.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0" })
    public void framesLengthZero() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "alert(window.length);\n"
            + "alert(window.frames.length);\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test that length of frames collection is retrieved when there are frames.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "2", "frame1", "frame2" })
    public void framesLengthAndFrameAccess() throws Exception {
        final String html =
            "<html>\n"
            + "<script>\n"
            + "function test() {\n"
            + "    alert(window.length);\n"
            + "    alert(window.frames.length);\n"
            + "    alert(window.frames[0].name);\n"
            + "    alert(window.frames.frame2.name);\n"
            + "}\n"
            + "</script>\n"
            + "<frameset rows='50,*' onload='test()'>\n"
            + "<frame name='frame1' src='about:blank'/>\n"
            + "<frame name='frame2' src='about:blank'/>\n"
            + "</frameset>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "0", "0", "2", "2", "2", "true" })
    public void windowFramesLive() throws Exception {
        final String html =
            "<html>\n"
            + "<script>\n"
            + "alert(window.length);\n"
            + "var oFrames = window.frames;\n"
            + "alert(oFrames.length);\n"
            + "function test() {\n"
            + "    alert(oFrames.length);\n"
            + "    alert(window.length);\n"
            + "    alert(window.frames.length);\n"
            + "    alert(oFrames == window.frames);\n"
            + "}\n"
            + "</script>\n"
            + "<frameset rows='50,*' onload='test()'>\n"
            + "<frame src='about:blank'/>\n"
            + "<frame src='about:blank'/>\n"
            + "</frameset>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for https://sf.net/tracker/index.php?func=detail&aid=1153708&group_id=47038&atid=448266
     * and https://bugzilla.mozilla.org/show_bug.cgi?id=443491.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("hello")
    public void overwriteFunctions_navigator() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      function navigator() {\n"
            + "        alert('hello');\n"
            + "      }\n"
            + "      navigator();\n"
            + "    } catch(e) { alert('exception'); }\n"
            + "  }\n"
            + "</script></head><body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2808901.
     * @throws Exception if an error occurs
     */
    @Test
    public void onbeforeunload_setToString() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "  window.onbeforeunload = \"alert('x')\";\n"
            + "  window.location = 'about:blank';\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2808901.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "true", "function" })
    public void onbeforeunload_defined() throws Exception {
        onbeforeunload("onbeforeunload", "var x;");
    }

    /**
     * Regression test for bug 2808901.
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(FF3_6)
    @Alerts(DEFAULT = { "true", "true", "object" },
            FF3_6 = { "false", "false", "undefined" })
    public void onbeforeunload_notDefined() throws Exception {
        onbeforeunload("onbeforeunload", null);
    }

    private void onbeforeunload(final String name, final String js) throws Exception {
        final String html
            = "<html><body" + (js != null ? " " + name + "='" + js + "'" : "") + "><script>\n"
            + "  alert('" + name + "' in window);\n"
            + "  var x = false;\n"
            + "  for(var p in window) { if(p == '" + name + "') { x = true; break; } }\n"
            + "  alert(x);\n"
            + "  alert(typeof window." + name + ");\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that <tt>window.frames</tt> basically returns a reference to the window.
     * Regression test for bug 2824436.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "[object Window]", "[object Window]", "[object Window]", "1", "true", "true",
                   "[object Window]", "true", "true", "no function", "undefined", "true", "true",
                   "[object History]", "true", "true", "[object Window]", "true", "true" },
            IE = { "[object]", "[object]", "[object]", "1", "true", "true",
                   "[object]", "true", "true", "[object]", "true", "true", "undefined", "true", "true",
                   "[object]", "true", "true", "[object]", "true", "true" })
    public void framesAreWindows() throws Exception {
        final String html = "<html><body><iframe name='f'></iframe><script>\n"
            + "alert(window.frames);\n"
            + "alert(window.f);\n"
            + "alert(window.frames.f);\n"
            + "alert(window.length);\n"
            + "alert(window.length == window.frames.length);\n"
            + "alert(window.length == window.frames.frames.length);\n"
            + "alert(window[0]);\n"
            + "alert(window[0] == window.frames[0]);\n"
            + "alert(window[0] == window.frames.frames[0]);\n"
            + "try {\n"
            + "  alert(window(0));\n"
            + "  alert(window(0) == window.frames(0));\n"
            + "  alert(window(0) == window.frames.frames(0));\n"
            + "} catch(e) {\n"
            + "  alert('no function');\n"
            + "}\n"
            + "alert(window[1]);\n"
            + "alert(window[1] == window.frames[1]);\n"
            + "alert(window[1] == window.frames.frames[1]);\n"
            + "alert(window.history);\n"
            + "alert(window.history == window.frames.history);\n"
            + "alert(window.history == window.frames.frames.history);\n"
            + "alert(window.self);\n"
            + "alert(window.self == window.frames.self);\n"
            + "alert(window.self == window.frames.frames.self);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Tests window.open().
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "Hello window", "" })
    public void open() throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function info(msg) {\n"
            + "    alert(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  try {\n"
            + "    window.open('" + URL_SECOND + "');\n"
            + "  }\n"
            + "  catch(e) { alert('exception') }\n"
            + "</script>\n"
            + "</body></html>";
        final String windowContent = "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  window.opener.info('Hello window');\n"
                + "  window.opener.info(window.name);\n"
                + "</script>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse(windowContent);
        loadPageWithAlerts2(html);
        // for some reason, the selenium driven browser is in an invalid state after this test
        shutDownAll();
    }

    /**
     * Tests window.open(...) with some params.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "Hello window", "New window" })
    public void openWindowParams() throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function info(msg) {\n"
            + "    alert(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  try {\n"
            + "    window.open('" + URL_SECOND + "', 'New window', 'width=200,height=100');\n"
            + "  }\n"
            + "  catch(e) { alert('exception') }\n"
            + "</script>\n"
            + "</body></html>";
        final String windowContent = "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  window.opener.info('Hello window');\n"
                + "  window.opener.info(window.name);\n"
                + "</script>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse(windowContent);
        loadPageWithAlerts2(html);
        // for some reason, the selenium driven browser is in an invalid state after this test
        shutDownAll();
    }

    /**
     * Tests window.open(...) with replace param.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "window1", "window2" })
    public void openWindowParamReplace() throws Exception {
        final String html = "<html><head>"
            + "<script>\n"
            + "  function info(msg) {\n"
            + "    alert(msg);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>\n"
            + "<script>\n"
            + "  try {\n"
            + "    window.open('" + URL_SECOND + "', 'window1', 'width=200,height=100', true);\n"
            + "    window.open('" + URL_SECOND + "', 'window2', 'width=200,height=100', 'true');\n"
            + "  }\n"
            + "  catch(e) { alert('exception') }\n"
            + "</script>\n"
            + "</body></html>";
        final String windowContent = "<html><head></head>\n"
                + "<body>\n"
                + "<script>\n"
                + "  window.opener.info(window.name);\n"
                + "</script>\n"
                + "</body></html>";
        getMockWebConnection().setDefaultResponse(windowContent);
        loadPageWithAlerts2(html);
        // for some reason, the selenium driven browser is in an invalid state after this test
        shutDownAll();
    }

    /**
     * For FF, window's opener can't be set unless to its current value.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "[object Window]", "[object Window] (true)", "exception", "null (true)", "null (false)",
                "null (false)", "null (false)", "null (false)" },
            IE = { "[object]", "[object] (true)", "1234 (true)", "null (true)", "undefined (true)", "[object] (true)",
                "[object] (true)", "[object] (true)" })
    public void set_opener() throws Exception {
        final String html = "<html><head><script>\n"
            + "var otherWindow = window.open('about:blank');\n"
            + "function trySetOpener1(_win, value) {\n"
            + "    try {\n"
            + "        _win.opener = value;\n"
            + "        alert(_win.opener + ' (' + (_win.opener === value) + ')');\n"
            + "    }\n"
            + "    catch(e) { alert('exception') }\n"
            + "}\n"
            + "function trySetOpener(_win) {\n"
            + "    var originalValue = _win.opener;\n"
            + "    alert(originalValue);\n"
            + "    trySetOpener1(_win, _win.opener);\n"
            + "    trySetOpener1(_win, 1234);\n"
            + "    trySetOpener1(_win, null);\n"
            + "    trySetOpener1(_win, undefined);\n"
            + "    trySetOpener1(_win, _win);\n"
            + "    trySetOpener1(_win, otherWindow);\n"
            + "    trySetOpener1(_win, originalValue);\n"
            + "}\n"
            + "function doTest() {\n"
            + "    trySetOpener(window.open('about:blank'));\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
        // for some reason, the selenium driven browser is in an invalid state after this test
        shutDownAll();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "exception", "exception", "exception", "exception" },
            IE6 = { "JScript", "5", "6", "number" },
            IE7 = { "JScript", "5", "7", "number" },
            IE8 = { "JScript", "5", "8", "number" })
    public void IEScriptEngineXxx() throws Exception {
        final String html = "<html><head><script>\n"
            + "try { alert(ScriptEngine()); } catch(e) { alert('exception') }\n"
            + "try { alert(ScriptEngineMajorVersion()); } catch(e) { alert('exception') }\n"
            + "try { alert(ScriptEngineMinorVersion()); } catch(e) { alert('exception') }\n"
            + "try { alert(typeof ScriptEngineBuildVersion()); } catch(e) { alert('exception') }\n"
            + "</script></head>\n"
            + "<body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2897473.
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(FF)
    @Alerts(DEFAULT = { "true", "true", "false", "true", "true", "true", "true" },
            FF3_6 = { "true", "true", "true", "false", "true", "true", "true" })
    public void heightsAndWidths() throws Exception {
        final String html
            = "<html><body onload='test()'><script>\n"
            + "function test() {\n"
            + "  alert(window.innerHeight > 0);\n"
            + "  alert(window.innerHeight == document.body.clientHeight);\n"
            + "  alert(window.outerHeight == window.innerHeight + 166);\n" // FF 3.6
            + "  alert(window.outerHeight == window.innerHeight + 111);\n"
            + "  alert(window.innerWidth > 0);\n"
            + "  alert(window.innerWidth == document.body.clientWidth);\n"
            + "  alert(window.outerWidth == window.innerWidth + 8);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2944261.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "605", "1256", "705", "1256" })
    public void changeHeightsAndWidths() throws Exception {
        final String html
            = "<html><head>\n"
            + "<script language='javascript'>\n"
            + "  function test() {\n"
            + "    var oldHeight = document.body.clientHeight;\n"
            + "    var oldWidth = document.body.clientWidth;\n"
            + "    alert(document.body.clientHeight);\n"
            + "    alert(document.body.clientWidth);\n"
            + "    newDiv = document.createElement('div');\n"
            + "    document.body.appendChild(newDiv);\n"
            + "    newDiv.style['height'] = oldHeight + 100 + 'px';\n"
            + "    newDiv.style['width'] = oldWidth + 100 + 'px';\n"
            + "    alert(document.body.clientHeight);\n"
            + "    alert(document.body.clientWidth);\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2897457.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0,0", "100,200", "110,230", "0,0", "0,95", "0,0", "0,1210" },
            IE = { "0,0", "100,200", "110,230", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()" })
    public void scrolling1() throws Exception {
        scrolling(true);
    }

    /**
     * Regression test for bug 2897457.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0,0", "0,0", "0,0", "0,0", "0,0", "0,0", "0,0" },
            IE = { "0,0", "0,0", "0,0", "0,0", "no scrollByLines()", "0,0", "no scrollByPages()" })
    public void scrolling2() throws Exception {
        scrolling(false);
    }

    private void scrolling(final boolean addHugeDiv) throws Exception {
        final String html
            = "<html><body onload='test()'>\n"
            + (addHugeDiv ? "<div id='d' style='width:10000px;height:10000px;background-color:blue;'></div>\n" : "")
            + "<script>\n"
            + "function test() {\n"
            + "  var b = document.body;\n"
            + "  alert(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  if(window.scrollTo) {\n"
            + "    window.scrollTo(100, 200);\n"
            + "    alert(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    alert('no scrollTo()');\n"
            + "  }\n"
            + "  if(window.scrollBy) {\n"
            + "    window.scrollBy(10, 30);\n"
            + "    alert(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    alert('no scrollBy()');\n"
            + "  }\n"
            + "  if(window.scrollTo) {\n"
            + "    window.scrollTo(-5, -20);\n"
            + "    alert(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    alert('no scrollTo()');\n"
            + "  }\n"
            + "  if(window.scrollByLines) {\n"
            + "    window.scrollByLines(5);\n"
            + "    alert(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    alert('no scrollByLines()');\n"
            + "  }\n"
            + "  if(window.scroll) {\n"
            + "    window.scroll(0, 0);\n"
            + "    alert(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    alert('no scroll()');\n"
            + "  }\n"
            + "  if(window.scrollByPages) {\n"
            + "    window.scrollByPages(2);\n"
            + "    alert(b.scrollLeft + ',' + b.scrollTop);\n"
            + "  } else {\n"
            + "    alert('no scrollByPages()');\n"
            + "  }\n"
            + "}\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0", "0", "0", "0" }, IE = { "undefined", "undefined", "undefined", "undefined" })
    public void pageXOffset() throws Exception {
        final String html
            = "<html><body onload='test()'><script>\n"
            + "function test() {\n"
            + "  window.scrollBy(5, 10);\n"
            + "  alert(window.pageXOffset);\n"
            + "  alert(window.pageYOffset);\n"
            + "  alert(window.scrollX);\n"
            + "  alert(window.scrollY);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("object")
    public void typeof() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "  alert(typeof window);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "0", "82", "23" }, IE = { "undefined", "undefined", "undefined" })
    public void mozInnerScreenX() throws Exception {
        final String html
            = "<html><body onload='test()'><script>\n"
            + "function test() {\n"
            + "  alert(window.mozInnerScreenX);\n"
            + "  alert(window.mozInnerScreenY);\n"
            + "  alert(window.mozPaintCount);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "exception", "exception", "Success" },
            IE = { "Success", "Success", "Success" })
    public void eval() throws Exception {
        final String html
            = "<html><body onload='test()'><script>\n"
            + "function test() {\n"
            + "  var x = new Object();\n"
            + "  x.a = 'Success';\n"
            + "  try {\n"
            + "    alert(window['eval']('x.a'));\n"
            + "  } catch(e) {alert('exception')}\n"
            + "  try {\n"
            + "    alert(window.eval('x.a'));\n"
            + "  } catch(e) {alert('exception')}\n"
            + "  try {\n"
            + "    alert(eval('x.a'));\n"
            + "  } catch(e) {alert('exception')}\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     * @see EventTest#firedEvent_equals_original_event()
     */
    @Test
    @Alerts(FF = { "true", "I was here" }, IE = "undefined")
    public void firedEvent_equals_original_event() throws Exception {
        final String html =
            "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var myEvent;\n"
            + "  var listener = function(x) {\n"
            + "    alert(x == myEvent);\n"
            + "    x.foo = 'I was here'\n"
            + "  }\n"
            + "  \n"
            + "  if (document.createEvent) {\n"
            + "    window.addEventListener('click', listener, false);\n"
            + "    myEvent = document.createEvent('HTMLEvents');\n"
            + "    myEvent.initEvent('click', true, true);\n"
            + "    window.dispatchEvent(myEvent);\n"
            + "  }\n"
            + "  else {\n"
            + "    //window.attachEvent('onclick', listener);\n"
            + "    //myEvent = document.createEventObject();\n"
            + "    //myEvent.eventType = 'onclick';\n"
            + "    //window.fireEvent(myEvent.eventType, myEvent);\n"
            + "    alert(window.fireEvent);\n"
            + "  }\n"
            + "  if (myEvent)\n"
            + "    alert(myEvent.foo);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void thisStrictEquals() throws Exception {
        final String html =
            "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(this === window);\n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(DEFAULT = { "null", "function", "null" }, FF3_6 = { "undefined", "function", "null" })
    @NotYetImplemented(FF3_6)
    public void onbeforeunload() throws Exception {
        final String html =
            "<html><head><title>First</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(window.onbeforeunload);\n"
            + "  var handle = function () {};\n"
            + "  window.onbeforeunload = handle;\n"
            + "  alert(typeof window.onbeforeunload);\n"
            + "  window.onbeforeunload = null;\n"
            + "  alert(window.onbeforeunload);\n"
            + "  \n"
            + "}\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that <tt>this.arguments</tt> works from within a method invocation, in a
     * function defined on the Function prototype object. This usage is required by the
     * Ajax.NET Professional JavaScript library.
     *
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "2" })
    public void functionPrototypeArguments() throws Exception {
        final String html =
              "<html>\n"
            + "<body onload='test()'>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    \n"
            + "    Function.prototype.doAlerts = function() {\n"
            + "      alert(this==o.f);\n"
            + "      alert(this.arguments ? this.arguments.length : 'null');\n"
            + "    }\n"
            + "    \n"
            + "    var o = function() {};\n"
            + "    o.f = function(x, y, z) { this.f.doAlerts(); }\n"
            + "    o.f('a', 'b');\n"
            + "  }\n"
            + "</script>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF3_6 = { "undefined", "function", "3" },
            DEFAULT = { "null", "function", "3" })
    public void onError() throws Exception {
        final String html
            = "<script>\n"
            + "alert(window.onerror);\n"
            + "window.onerror=function(){alert(arguments.length);};\n"
            + "alert(typeof window.onerror);\n"
            + "try { alert(undef); } catch(e) { /* caught, so won't trigger onerror */ }\n"
            + "alert(undef);\n"
            + "</script>";

        if (getWebDriver() instanceof HtmlUnitDriver) {
            getWebWindowOf((HtmlUnitDriver) getWebDriver()).getWebClient()
                .getOptions().setThrowExceptionOnScriptError(false);
        }
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "rgb(0, 0, 0)", IE = "exception")
    public void getComputedStyle() throws Exception {
        final String html = "<html><body>\n"
            + "<div id='myDiv'></div>\n"
            + "<script>\n"
            + "  var e = document.getElementById('myDiv');\n"
            + "  try {\n"
            + "    alert(window.getComputedStyle(e, null).color);\n"
            + "  } catch(e) { alert('exception') }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "rgb(255, 0, 0)", IE = "exception")
    public void getComputedStyle_WithComputedColor() throws Exception {
        final String html =
              "<html>\n"
            + "  <head>\n"
            + "    <style>div.x { color: red; }</style>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    var e = document.getElementById('d');\n"
            + "    try {\n"
            + "    alert(window.getComputedStyle(e, '').color);\n"
            + "    } catch(e) { alert('exception') }\n"
            + "  }\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "    <div id='d' class='x'>foo bar</div>\n"
            + "  </body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * JS code was throwing an exception as of 2.12-SNAPSHOT from 21.01.2013 due to the incorrect signature
     * of getComputedStyle.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "rgb(0, 0, 0)", IE = "exception")
    public void getComputedStyle_svg() throws Exception {
        final String html = "<html><body>\n"
            + "  <svg xmlns='http://www.w3.org/2000/svg' id='myId' version='1.1'></svg>\n"
            + "<script>\n"
            + "  var e = document.getElementById('myId');\n"
            + "  try {\n"
            + "    alert(window.getComputedStyle(e, null).color);\n"
            + "  } catch(e) { alert('exception') }\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * This was causing HtmlUnit to hang as of HtmlUnit-2.12 snapshot from 24.01.2013 and probably since a very long
     * time.
     * The reason was that "top" evaluate to WindowProxy and "Object(top)" was setting the top scope as parentScope
     * of the WindowProxy which was setting it on the Window where it should always be null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("undefined")
    public void hangingObjectCallOnWindowProxy() throws Exception {
        final String html = "<html><body>\n"
            + "<iframe id='it'></iframe>;\n"
            + "<script>\n"
            + "  Object(top);\n"
            + "  alert(window.foo);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Was producing "TypeError: Object's getDefaultValue() method returned an object" due to Delegator not delegating
     * getDefaultValue(hint) to delegee when hint is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void equalsString() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "  alert('foo' == window);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Was producing "TypeError: Object's getDefaultValue() method returned an object" due to Delegator not delegating
     * getDefaultValue(hint) to delegee when hint is null.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void equalsInt() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "  var i = 0;\n"
            + "  alert(i == window);\n"
            + "</script>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * As of 2.12-SNAPSHOT on 19.02.2013, a task started by setTimeout in an event handler could be executed before
     * all events handlers have been executed due to a missing synchronization.
     * @throws Exception if the test fails
     */
    @Test
    public void setTimeoutShouldNotBeExecutedBeforeHandlers() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "function stop() {\n"
            + "  window.stopIt = true;\n"
            + "}\n"
            + "for (var i=0; i<1000; ++i) {\n"
            + "  var handler = function(e) {\n"
            + "    if (window.stopIt) {\n"
            + "      e.preventDefault ?  e.preventDefault() : e.returnValue = false;\n"
            + "    }\n"
            + "  }\n"
            + "  if (window.addEventListener)\n"
            + "    window.addEventListener('click', handler, false);\n"
            + "  else\n"
            + "    window.attachEvent('onclick', handler);\n"
            + "}\n"
            + "</script>\n"
            + "<form action='page2' method='post'>\n"
            + "<input id='it' type='submit' onclick='setTimeout(stop, 0)'>\n"
            + "</form>"
            + "</body></html>";

        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("it")).click();

        assertEquals(getDefaultUrl() + "page2", driver.getCurrentUrl());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "true", "null" },
            FF3_6 = { "false", "undefined" },
            IE = { "false", "undefined" })
    public void onchange_noHandler() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "alert('onchange' in window);\n"
            + "alert(window.onchange);\n"
            + "</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "changed" },
            FF3_6 = { },
            IE = { })
    public void onchange_withHandler() throws Exception {
        final String html
            = "<html><body>\n"
            + "<input id='it'/>\n"
            + "<script>\n"
            + "window.onchange = function() {\n"
            + "  alert('changed');\n"
            + "}\n"
            + "</script></body></html>";
        final WebDriver driver = loadPage2(html);
        driver.findElement(By.id("it")).sendKeys("hello");
        driver.findElement(By.tagName("body")).click();

        assertEquals(getExpectedAlerts(), getCollectedAlerts(driver));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "type: message", "data: hello" },
            IE6 = { }, IE7 = { }, IE8 = { })
    public void postMessage() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "function receiveMessage(event) {\n"
            + "  alert('type: ' + event.type);\n"
            + "  alert('data: ' + event.data);\n"
            + "}\n"
            + "window.addEventListener('message', receiveMessage, false);\n"
            + "</script>\n"
            + "<iframe src='" + URL_SECOND + "'></iframe>\n"
            + "</body></html>";

        final String iframe = "<html><body><script>\n"
            + "top.postMessage('hello', '*');\n"
            + "</script></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, iframe);
        loadPageWithAlerts2(html);
    }
}
