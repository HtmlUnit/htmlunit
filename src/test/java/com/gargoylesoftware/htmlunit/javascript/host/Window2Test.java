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
package com.gargoylesoftware.htmlunit.javascript.host;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.CollectingAlertHandler;
import com.gargoylesoftware.htmlunit.DialogWindow;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
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
public class Window2Test extends WebTestCase {

    /**
     * "window.controllers" is used by some JavaScript libraries to determine if the
     * browser is Gecko based or not.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "not found", "hello" }, FF = { "found", "hello" })
    public void FF_controllers() throws Exception {
        final String html
            = "<html><head></head><body>\n"
            + "<script>\n"
            + "if (window.controllers)\n"
            + "  alert('found')\n"
            + "else\n"
            + "  alert('not found')\n"
            + "window.controllers = 'hello';\n"
            + "alert(window.controllers);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * Basic test for the <tt>showModalDialog</tt> method. See bug 2124916.
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers({ Browser.IE, Browser.FF3 })
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
        loadPageWithAlerts(html);
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
        loadPageWithAlerts(html);
    }

    /**
     * In {@link org.mozilla.javascript.ScriptRuntime}, Rhino defines a bunch of properties
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
    @Alerts(FF = { "java: object", "getClass: function" },
            IE = { "java: undefined", "getClass: undefined" })
    public void rhino_lazilyNames2() throws Exception {
        final String[] properties = {"java", "getClass"};
        doTestRhinoLazilyNames(properties);
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
        final String[] properties = {"Packages", "XML", "XMLList", "Namespace", "QName"};
        doTestRhinoLazilyNames(properties);
    }

    private void doTestRhinoLazilyNames(final String[] properties) throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<script>\n"
            + "  var props = ['" + StringUtils.join(properties, "', '") + "'];\n"
            + "  for (var i=0; i<props.length; ++i)\n"
            + "    alert(props[i] + ': ' + typeof(window[props[i]]));\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
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

        final WebClient client = getWebClient();
        client.setThrowExceptionOnScriptError(false);

        final List<String> actual = new ArrayList<String>();
        client.setAlertHandler(new CollectingAlertHandler(actual));

        final MockWebConnection conn = new MockWebConnection();
        conn.setResponse(URL_GARGOYLE, html);
        client.setWebConnection(conn);

        client.getPage(URL_GARGOYLE);
        assertEquals(getExpectedAlerts(), actual);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.IE)
    @Alerts("1")
    public void execScript2() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    window.execScript('alert(1);');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void open_FF() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function performAction() {\n"
            + "    actionwindow = window.open('', '1205399746518', "
            + "'location=no,scrollbars=no,resizable=no,width=200,height=275');\n"
            + "    actionwindow.document.writeln('Please wait while connecting to server...');\n"
            + "    actionwindow.focus();\n"
            + "    actionwindow.close();\n"
            + "  }\n"
            + "</script></head><body>\n"
            + "    <input value='Click Me' type=button onclick='performAction()'>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(getBrowserVersion(), html, null);
        final HtmlButtonInput input = page.getFirstByXPath("//input");
        input.click();
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

        loadPageWithAlerts(html);
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

        loadPageWithAlerts(html);
    }

    /**
     * Test window properties that match Prototypes.
     *
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined" }, FF2 = { "[Node]", "[Element]" },
            FF3 = { "[object Node]", "[object Element]" })
    public void windowProperties() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(window.Node);\n"
            + "    alert(window.Element);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "<form name='myForm'></form>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
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
            + "alert(window.length)\n"
            + "alert(window.frames.length)\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts(html);
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
            + "function test()\n"
            + "{\n"
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

        loadPageWithAlerts(html);
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
            + "function test()\n"
            + "{\n"
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

        loadPageWithAlerts(html);
    }
}
