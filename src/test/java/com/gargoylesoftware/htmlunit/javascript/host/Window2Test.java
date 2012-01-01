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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.DialogWindow;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.OnbeforeunloadHandler;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.HtmlButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

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
            FF = { "found", "exception", "false" })
    @NotYetImplemented(Browser.FF)
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
     * Basic test for the <tt>showModalDialog</tt> method. See bug 2124916.
     * @throws Exception if an error occurs
     */
    @Test
    public void showModalDialog() throws Exception {
        final String html1
            = "<html><head><script>\n"
            + "  function test() {\n"
            + "    alert(window.returnValue);\n"
            + "    var o = new Object();\n"
            + "    o.firstName = f.elements.firstName.value;\n"
            + "    o.lastName = f.elements.lastName.value;\n"
            + "    var ret = showModalDialog('myDialog.html', o, 'dialogHeight:300px; dialogLeft:200px;');\n"
            + "    alert(ret);\n"
            + "    alert('finished');\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "  <button onclick='test()' id='b'>Test</button>\n"
            + "  <form id='f'>\n"
            + "    First Name: <input type='text' name='firstName' value='Jane'><br>\n"
            + "    Last Name: <input type='text' name='lastName' value='Smith'>\n"
            + "  </form>\n"
            + "</body></html>";

        final String html2
            = "<html><head><script>\n"
            + "  var o = window.dialogArguments;\n"
            + "  alert(o.firstName);\n"
            + "  alert(o.lastName);\n"
            + "  window.returnValue = 'sdg';\n"
            + "</script></head>\n"
            + "<body>foo</body></html>";

        final String[] expected = {"undefined", "Jane", "Smith", "sdg", "finished"};

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(new URL(URL_FIRST.toExternalForm() + "myDialog.html"), html2);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlElement button = page.getHtmlElementById("b");
        final HtmlPage dialogPage = button.click();
        final DialogWindow dialog = (DialogWindow) dialogPage.getEnclosingWindow();

        dialog.close();
        assertEquals(expected, actual);
    }

    /**
     * Basic test for the <tt>showModelessDialog</tt> method. See bug 2124916.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.IE)
    public void showModelessDialog() throws Exception {
        final String html1
            = "<html><head><script>\n"
            + "  var userName = '';\n"
            + "  function test() {\n"
            + "    var newWindow = showModelessDialog('myDialog.html', window, 'status:false');\n"
            + "    alert(newWindow);\n"
            + "  }\n"
            + "  function update() { alert(userName); }\n"
            + "</script></head><body>\n"
            + "  <input type='button' id='b' value='Test' onclick='test()'>\n"
            + "</body></html>";

        final String html2
            = "<html><head><script>\n"
            + "function update() {\n"
            + "  var w = dialogArguments;\n"
            + "  w.userName = document.getElementById('name').value;\n"
            + "  w.update();\n"
            + "}\n"
            + "</script></head><body>\n"
            + "  Name: <input id='name'><input value='OK' id='b' type='button' onclick='update()'>\n"
            + "</body></html>";

        final String[] expected = {"[object]", "a"};

        final WebClient client = getWebClient();
        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_FIRST, html1);
        conn.setResponse(new URL(URL_FIRST.toExternalForm() + "myDialog.html"), html2);
        client.setWebConnection(conn);

        final HtmlPage page = client.getPage(URL_FIRST);
        final HtmlElement button = page.getHtmlElementById("b");
        final HtmlPage dialogPage = button.click();

        final HtmlInput input = dialogPage.getHtmlElementById("name");
        input.setValueAttribute("a");

        final HtmlButtonInput button2 = (HtmlButtonInput) dialogPage.getHtmlElementById("b");
        button2.click();

        assertEquals(expected, actual);
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
    @Browsers(Browser.FF)
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
    @NotYetImplemented(Browser.FF)
    @Alerts(FF3 = { "java: object", "getClass: undefined" },
            FF3_6 = { "java: undefined", "getClass: undefined" },
            IE = { "java: undefined", "getClass: undefined" })
    public void rhino_lazilyNames2() throws Exception {
        doTestRhinoLazilyNames("java", "getClass");
    }

    /**
     * The same as in {@link #rhino_lazilyNames()} but for properties where it doesn't work yet.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.FF)
    @Alerts(FF = { "Packages: object", "XML: function", "XMLList: function",
            "Namespace: function", "QName: function" },
            IE = { "Packages: undefined", "XML: undefined", "XMLList: undefined",
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
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "undefined", "function", "3" },
            IE = { "null", "function", "3" })
    public void onError() throws Exception {
        final String html
            = "<script>\n"
            + "alert(window.onerror);\n"
            + "window.onerror=function(){alert(arguments.length);};\n"
            + "alert(typeof window.onerror);\n"
            + "try { alert(undef); } catch(e) { /* caught, so won't trigger onerror */ }\n"
            + "alert(undef);\n"
            + "</script>";

        getWebClientWithMockWebConnection().setThrowExceptionOnScriptError(false);
        loadPageWithAlerts(html);
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
    public void overwriteFunctions_alert() throws Exception {
        final String html = "<html><head><script language='JavaScript'>\n"
            + "function alert(x) {\n"
            + "  document.title = x;\n"
            + "}\n"
            + "alert('hello');\n"
            + "</script></head><body></body></html>";

        final HtmlPage page = loadPageWithAlerts(html);
        assertEquals("hello", page.getTitleText());
    }

    /**
     * Regression test for https://sf.net/tracker/index.php?func=detail&aid=1153708&group_id=47038&atid=448266
     * and https://bugzilla.mozilla.org/show_bug.cgi?id=443491.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = "hello")
    public void overwriteFunctions_navigator() throws Exception {
        final String html = "<html><head><script language='JavaScript'>\n"
            + "try {\n"
            + "  function navigator()\n"
            + "  {\n"
            + "    alert('hello');\n"
            + "  }\n"
            + "  navigator();\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></head><body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "123", "123" })
    public void overwriteProperty_top() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "  alert(window.top == this);\n"
            + "  var top = 123;\n"
            + "  alert(top);\n"
            + "  alert(window.top);\n"
            + "</script></body></html>";
        // this can't be tested using WebDriver currently (i.e. using loadPageWithAlerts2)
        // because the hack currently used to capture alerts needs reference to property "top".
        loadPageWithAlerts(html);
    }

    /**
     * Regression test for bug 2808901.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("x")
    public void onbeforeunload_setToFunction() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "  window.onbeforeunload = function() { alert('x'); return 'x'; };\n"
            + "  window.location = 'about:blank';\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Download of next page is done first after onbeforeunload is done.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("x")
    public void onbeforeunload_calledBeforeDownload() throws Exception {
        final String html
            = "<html><body><script>\n"
            + "  window.onbeforeunload = function() { alert('x'); return 'hello'; };\n"
            + "  window.location = 'foo.html';\n"
            + "</script></body></html>";

        final WebClient webClient = getWebClientWithMockWebConnection();
        getMockWebConnection().setDefaultResponse("");

        final OnbeforeunloadHandler handler = new OnbeforeunloadHandler() {
            public boolean handleEvent(final Page page, final String returnValue) {
                final String[] expectedRequests = {""};
                assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(getDefaultUrl()));
                return true;
            }
        };
        webClient.setOnbeforeunloadHandler(handler);
        loadPageWithAlerts(html);

        final String[] expectedRequests = {"", "foo.html"};
        assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(getDefaultUrl()));
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
        loadPageWithAlerts(html);
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
    @NotYetImplemented(Browser.FF)
    @Alerts(IE = { "true", "true", "object" }, FF = { "false", "false", "undefined" })
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
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void serialization() throws Exception {
        final String html = "<html><head></head><body><iframe></iframe><script>window.frames</script></body></html>";
        final HtmlPage page = loadPageWithAlerts(html);
        clone(page.getEnclosingWindow());
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
     * For FF, window's opener can't be set unless to its current value.
     * Note: this test causes problems to WebDriver + FF2
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
    @Browsers(Browser.FF)
    @Alerts(FF = { "true", "true", "true", "true", "true", "true" })
    public void heightsAndWidths() throws Exception {
        final String html
            = "<html><body onload='test()'><script>\n"
            + "function test() {\n"
            + "  alert(window.innerHeight > 0);\n"
            + "  alert(window.innerHeight == document.body.clientHeight);\n"
            + "  alert(window.outerHeight == window.innerHeight + 150);\n"
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
}
