/*
 * Copyright (c) 2002-2010 Gargoyle Software Inc.
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

import static org.junit.Assert.fail;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HTMLDocument}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLDocumentTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "2", "DIV", "2" })
    public void getElementsByTagName() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function test() {\n"
            + "      alert(document.getElementsByTagName('div').length);\n"
            + "      document.getElementById('myDiv').innerHTML = \"<P><DIV id='secondDiv'></DIV></P>\";\n"
            + "      alert(document.getElementById('secondDiv').nodeName);\n"
            + "      alert(document.getElementsByTagName('div').length);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "<div id='myDiv'>\n"
            + "  <div></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3 = { "function", "div1", "span2", "span3", "2", "1", "1", "0", "0", "0" },
            FF2 = { "undefined", "exception" },
            IE = { "undefined", "exception" })
    public void getElementsByClassName() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(typeof document.getElementsByClassName);\n"
            + "    try {\n"
            + "      var elements = document.getElementsByClassName('foo');\n"
            + "      for (var i=0; i<elements.length; i++) {\n"
            + "        alert(elements[i].id);\n"
            + "      }\n"
            + "      alert(document.getElementsByClassName('red').length);\n"
            + "      alert(document.getElementsByClassName('foo red').length);\n"
            + "      alert(document.getElementsByClassName('red foo').length);\n"
            + "      alert(document.getElementsByClassName('blue foo').length);\n"
            + "      alert(document.getElementsByClassName('*').length);\n"
//            + "      alert(document.getElementsByClassName().length);\n" // exception in FF3
            + "      alert(document.getElementsByClassName(null).length);\n"
            + "    }\n"
            + "    catch (e) { alert('exception') }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div class='foo' id='div1'><span class='c2'>hello</span>\n"
            + "  <span class='foo' id='span2'>World!</span></div>\n"
            + "<span class='foo red' id='span3'>again</span>\n"
            + "<span class='red' id='span4'>bye</span>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BackCompat")
    public void compatMode() throws Exception {
        compatMode("");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("BackCompat")
    public void compatMode_no_url() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_strict() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_strict_40() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "CSS1Compat", FF = "BackCompat")
    public void compatMode_loose_40() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\" "
            + "\"http://www.w3.org/TR/html4/loose.dtd\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_loose() throws Exception {
        compatMode("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" "
            + "\"http://www.w3.org/TR/html4/loose.dtd\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_xhtml_traditional() throws Exception {
        compatMode("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("CSS1Compat")
    public void compatMode_xhtml_strict() throws Exception {
        compatMode("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" "
            + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
    }

    private void compatMode(final String doctype) throws Exception {
        final String html = doctype + "<html>\n"
            + "<head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function test() {\n"
            + "      alert(document.compatMode);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "true", FF = "false")
    public void uniqueID() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <title>Test</title>\n"
            + "    <script>\n"
            + "    function test() {\n"
            + "      alert(document.uniqueID != undefined);\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "[object HTMLDivElement]", "[object HTMLUnknownElement]", "[object Element]" })
    public void createDocumentNS() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<title>Test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var elt = document.createElementNS('http://www.w3.org/1999/xhtml', 'div');\n"
            + "  alert(elt);\n"
            + "  var elt = document.createElementNS('http://www.w3.org/1999/xhtml', 'foo');\n"
            + "  alert(elt);\n"
            + "  elt = document.createElementNS('blabla', 'div');\n"
            + "  alert(elt);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts("Hello")
    public void createDocumentNS_xul() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<title>Test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var inner = document.createElementNS('http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul',"
            + "'label');\n"
            + "  inner.setAttribute('value', 'Hello');\n"
            + "  inner.style['fontFamily'] = 'inherit';\n"
            + "  document.body.appendChild(inner);\n"
            + "  alert(document.body.lastChild.value);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "[object HTMLCollection]", "0" }, IE = { "[object]", "0" })
    public void applets() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<title>Test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.applets);\n"
            + "  alert(document.applets.length);\n"
            + "}\n"
            + "</script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "imported: [object HTMLScriptElement]", IE = "exception")
    @NotYetImplemented(Browser.FF)
    public void importNode_script() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + " try {\n"
            + "  var d = document.implementation.createDocument(null, null, null);\n"
            + "  var xhtml = \"<html xmlns='http://www.w3.org/1999/xhtml'><sc\" "
            + "   + \"ript>alert('o'); _scriptEvaluated=true;</scr\" + \"ipt></html>\";\n"
            + "  var newDoc = (new DOMParser()).parseFromString(xhtml, 'text/xml');\n"
            + "  var theScript = newDoc.getElementsByTagName('script')[0];\n"
            + "  var importedScript = window.document.importNode(theScript, true);\n"
            + "  alert('imported: ' + importedScript);\n"
            + "  var theSpan = document.getElementById('s1');\n"
            + "  document.body.replaceChild(importedScript, theSpan);\n"
            + " } catch (e) { alert('exception') }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <span id='s1'></span>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts("clicked")
    public void dispatchEvent() throws Exception {
        final String html =
            "<html>\n"
            + "<script>\n"
            + "  function doTest() {\n"
            + "    var e = document.createEvent('MouseEvents');\n"
            + "    e.initMouseEvent('click', true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0, null);\n"
            + "    document.dispatchEvent(e);\n"
            + "  }\n"
            + "  function clickListener() {\n"
            + "    alert('clicked');\n"
            + "  }\n"
            + "  document.addEventListener('click', clickListener, true);\n"
            + "</script>\n"
            + "<body onload='doTest()'>foo</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "undefined", "exception" },
            IE = { "[object]", "0", "1", "f", "f", "f", "f", "urn:f", "urn:f", "true" })
    public void namespaces() throws Exception {
        final String html =
              "<body><script>\n"
            + "var ns = document.namespaces;\n"
            + "alert(ns);\n"
            + "try {\n"
            + "  alert(ns.length);\n"
            + "  ns.add('f', 'urn:f');\n"
            + "  alert(ns.length);\n"
            + "  alert(ns.item(0).name);\n"
            + "  alert(ns[0].name);\n"
            + "  alert(ns(0).name);\n"
            + "  alert(ns('f').name);\n"
            + "  alert(ns.item('f').urn);\n"
            + "  alert(ns['f'].urn);\n"
            + "  alert(ns == document.namespaces);\n"
            + "}\n"
            + "catch(e) { alert('exception') }\n"
            + "</script></body>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that we can store document methods and use them from a variable (IE only).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "exception", IE = { "d", "1" })
    public void documentMethodsWithoutDocument() throws Exception {
        final String html
            = "<div id='d' name='d'>d</div>\n"
            + "<script>\n"
            + "try {\n"
            + "  var i = document.getElementById; alert(i('d').id);\n"
            + "  var n = document.getElementsByName; alert(n('d').length);\n"
            + "} catch(e) { alert('exception') }\n"
            + "</script>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "[object]", FF = "null")
    public void getElementById_caseSensitivity() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <script>\n"
            + "    function test() {\n"
            + "      alert(document.getElementById('MYDIV'));\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "<div id='myDiv'>\n"
            + "  <div></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE)
    public void readyState() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "    <script>\n"
            + "    var doc;"
            + "    function test() {\n"
            + "      var iframe = document.createElement('iframe');\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.parentNode.appendChild(iframe);\n"
            + "      doc = iframe.contentWindow.document;\n"
            + "      check();\n"
            + "      setTimeout(check, 100);\n"
            + "    }\n"
            + "    function check() {\n"
            + "      var textarea = document.getElementById('myTextarea');\n"
            + "      textarea.value += doc.readyState + ',' + doc.body + '-';\n"
            + "    }\n"
            + "    </script>\n"
            + "</head>\n"
            + "<body onload='test()'>"
            + "<div>\n"
            + "  <textarea id='myTextarea' cols='80'></textarea>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        final HtmlPage page = loadPageWithAlerts(html);
        final String expected = getBrowserVersion().isIE() ? "loading,null-complete,[object]-"
                : "undefined,[object HTMLBodyElement]-undefined,[object HTMLBodyElement]-";
        page.getWebClient().waitForBackgroundJavaScript(500);
        assertEquals(expected, page.<HtmlTextArea>getHtmlElementById("myTextarea").getText());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = {"#ffffff", "#ffffff", "#0000aa", "#0000aa", "#000000", "#000000" },
            IE = {"#ffffff", "", "#0000aa", "#0000aa", "#000000", "#000000" })
    public void bgColor() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(document.bgColor);\n"
            + "        alert(b.bgColor);\n"
            + "        document.bgColor = '#0000aa';\n"
            + "        alert(document.bgColor);\n"
            + "        alert(b.bgColor);\n"
            + "        document.bgColor = 'x';\n"
            + "        alert(document.bgColor);\n"
            + "        alert(b.bgColor);\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF2 = { "[object HTMLSpanElement]", "undefined" },
            FF3 = { "[object HTMLCollection]", "4", "red" },
            IE = { "[object]", "4", "red" })
    public void identicalIDs() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        alert(document.all['Item']);\n"
            + "        alert(document.all.Item.length);\n"
            + "        if (document.all.Item.length) {\n"
            + "          alert(document.all.Item[1].style.color);\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>\n"
            + "    <span id='Item' style='color:black'></span>\n"
            + "    <span id='Item' style='color:red'></span>\n"
            + "    <span id='Item' style='color:green'></span>\n"
            + "    <span id='Item' style='color:blue'></span>\n"
            + "  </body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "www.gargoylesoftware.com", "gargoylesoftware.com" })
    public void domain() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'gargoylesoftware.com';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html, new URL("http://www.gargoylesoftware.com/"), -1);
    }

  /**
    * @throws Exception if the test fails
    */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "www.gargoylesoftware.com", "gargoylesoftware.com" })
    public void domainMixedCaseNetscape() throws Exception {
        final URL urlGargoyleUpperCase = new URL("http://WWW.GARGOYLESOFTWARE.COM/");

        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'GaRgOyLeSoFtWaRe.CoM';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(new URL("http://www.gargoylesoftware.com/"), html);
        loadPageWithAlerts(html, urlGargoyleUpperCase, -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "www.gargoylesoftware.com", "gargoylesoftware.com" },
            IE = { "www.gargoylesoftware.com", "GaRgOyLeSoFtWaRe.CoM" })
    public void domainMixedCase() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'GaRgOyLeSoFtWaRe.CoM';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html, new URL("http://www.gargoylesoftware.com/"), -1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainLong() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'd4.d3.d2.d1.gargoylesoftware.com';\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'd1.gargoylesoftware.com';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final String[] expectedAlerts =
        {"d4.d3.d2.d1.gargoylesoftware.com", "d4.d3.d2.d1.gargoylesoftware.com", "d1.gargoylesoftware.com"};

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(getWebClient(), html, collectedAlerts, new URL("http://d4.d3.d2.d1.gargoylesoftware.com"));
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainSetSelf() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'localhost';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = {"localhost", "localhost"};

        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(getWebClient(), html, collectedAlerts, new URL("http://localhost"));
        assertEquals(expectedAlerts, collectedAlerts);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void domainTooShort() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.domain);\n"
            + "    document.domain = 'com';\n"
            + "    alert(document.domain);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        final List<String> collectedAlerts = new ArrayList<String>();
        try {
            loadPage(getWebClient(), html, collectedAlerts);
        }
        catch (final ScriptException ex) {
            return;
        }
        fail();
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "www.gargoylesoftware.com", "www.gargoylesoftware.com" },
            IE = { "www.gargoylesoftware.com", "www.gargoylesoftware.com", "exception" })
    public void domain_set_for_about_blank() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "  var domain = document.domain;\n"
            + "  alert(domain);\n"
            + "  var frameDoc = frames[0].document;\n"
            + "  alert(frameDoc.domain);\n"
            + "  try {\n"
            + "    frameDoc.domain = domain;\n"
            + "  } catch (e) { alert('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe src='about:blank'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts(html, new URL("http://www.gargoylesoftware.com/"), -1);
    }

    /**
     * Test that <tt>document.forms.form_name</tt> should be evaluated to <tt>undefined</tt> if the form has a prefix.
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented
    @Alerts("undefined")
    public void prefix() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "    alert(document.forms.fmLogin);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "  <s:form name='fmLogin' action='doLogin' method='POST'>\n"
            + "    <s:hidden name='hdUserID'/>\n"
            + "    <s:hidden name='hdPassword'/>\n"
            + "  </s:form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Property lastModified returns the last modification date of the document.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "string", "Fri, 16 Oct 2009 13:59:47 GMT" })
    public void lastModified() throws Exception {
        final List<NameValuePair> responseHeaders = new ArrayList<NameValuePair>();
        responseHeaders.add(new NameValuePair("Last-Modified", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders);

        // Last-Modified header has priority compared to Date header
        responseHeaders.add(new NameValuePair("Date", "Fri, 17 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders);

        // but Date is taken, if no Last-Modified header is present
        responseHeaders.clear();
        responseHeaders.add(new NameValuePair("Date", "Fri, 16 Oct 2009 13:59:47 GMT"));
        testLastModified(responseHeaders);
    }

    private void testLastModified(final List<NameValuePair> responseHeaders) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "  alert(typeof document.lastModified);\n"
            + "  var d = new Date(document.lastModified);\n"
            + "  alert(d.toGMTString());\n" // to have results not depending on the user's time zone
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        getMockWebConnection().setResponse(getDefaultUrl(), html, 200, "OK", "text/html", responseHeaders);

        loadPageWithAlerts2(getDefaultUrl());
    }

    /**
     * If neither Date header nor Last-Modified header is present, current time is taken.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "true", "true" })
    public void lastModified_noDateHeader() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "  var justBeforeLoading = " + System.currentTimeMillis() + ";\n"
            + "  var d = new Date(document.lastModified);\n"
            + "  alert(d.valueOf() >= justBeforeLoading - 1000);\n" // date string format has no ms, take 1s marge
            + "  alert(d.valueOf() <= new Date().valueOf());\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Regression test for bug 2919853 (format of <tt>document.lastModified</tt> was incorrect).
     * @throws Exception if an error occurs
     */
    @Test
    public void lastModified_format() throws Exception {
        final String html
            = "<html><body onload='document.getElementById(\"i\").value = document.lastModified'>\n"
            + "<input id='i'></input></body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        final String lastModified = driver.findElement(By.id("i")).getValue();

        try {
            new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(lastModified);
        }
        catch (final ParseException e) {
            fail(e.getMessage());
        }
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "1", "[object HTMLBodyElement]" }, IE = "exception")
    public void designMode_selectionRange_empty() throws Exception {
        designMode_selectionRange("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "1", "[object Text]" }, IE = "exception")
    public void designMode_selectionRange_text() throws Exception {
        designMode_selectionRange("hello");
    }

    private void designMode_selectionRange(final String bodyContent) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest(){\n"
            + "  try {\n"
            + "    document.designMode = 'on';\n"
            + "    var s = window.getSelection();\n"
            + "    alert(s.rangeCount);\n"
            + "    alert(s.getRangeAt(0).startContainer);\n"
            + "  } catch(e) {alert('exception'); }\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='doTest()'>" // no \n here!
            + bodyContent
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "not defined",
            IE = { "true", "1", "about:blank", "about:blank" })
    public void frames() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test(){\n"
            + "  if (document.frames)\n"
            + "  {\n"
            + "    alert(document.frames == window.frames);\n"
            + "    alert(document.frames.length);\n"
            + "    alert(document.frames(0).location);\n"
            + "    alert(document.frames('foo').location);\n"
            + "  }\n"
            + "  else\n"
            + "    alert('not defined');\n"
            + "}\n"
            + "</script></head><body onload='test();'>\n"
            + "<iframe src='about:blank' name='foo'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * IE allows document.frameName to access a frame window.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "undefined", "false" },
            IE = { "[object]", "true" })
    public void frameAccessByName() throws Exception {
        final String html = "<html><head><script>\n"
            + "function test(){\n"
            + "  alert(document.foo);\n"
            + "  alert(window.frames[0] == document.foo);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "<iframe src='about:blank' name='foo'></iframe>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "2", "2" }, IE = { "0", "0" })
    public void getElementsByName() throws Exception {
        final String html
            = "<html><head><title>Test</title><script>\n"
            + "function doTest() {\n"
            + "    alert(document.getElementsByName('').length);\n"
            + "    alert(document.getElementsByName(null).length);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div name=''></div>\n"
            + "<div name=''></div>\n"
            + "<div></div>\n"
            + "<div></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

}
