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

import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.FF3_6;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.IE;
import static com.gargoylesoftware.htmlunit.BrowserRunner.Browser.NONE;
import static com.gargoylesoftware.htmlunit.javascript.host.html.HTMLDocument.EMPTY_COOKIE_NAME;
import static com.gargoylesoftware.htmlunit.util.StringUtils.parseHttpDate;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;
import com.gargoylesoftware.htmlunit.BrowserRunner.BuggyWebDriver;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.WebWindow;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPageTest;
import com.gargoylesoftware.htmlunit.util.Cookie;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Tests for {@link HTMLDocument}.
 *
 * @version $Revision$
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class HTMLDocumentTest extends WebDriverTestCase {
    /** jQuery selectors that aren't CSS selectors. */
    public static final String[] JQUERY_CUSTOM_SELECTORS = {"div.submenu-last:last",
        "*#__sizzle__ div.submenu-last:last", "div:animated",  "div:animated", "*:button", "*:checkbox", "div:even",
        "*:file", "div:first", "td:gt(4)", "div:has(p)", ":header", ":hidden", ":image", ":input", "td:lt(4)",
        ":odd", ":password", ":radio", ":reset", ":selected", ":submit", ":text", ":visible"
    };

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
            + "<body onload='test()'>\n"
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
    @Alerts(FF = { "function", "div1", "span2", "span3", "2", "1", "1", "0", "0", "0" },
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

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final HtmlUnitDriver huDriver = (HtmlUnitDriver) driver;
            final Field field = HtmlUnitDriver.class.getDeclaredField("currentWindow");
            field.setAccessible(true);
            final WebWindow webWindow = (WebWindow) field.get(huDriver);
            final HtmlPage page = (HtmlPage) webWindow.getEnclosedPage();
            assertEquals("BackCompat".equals(getExpectedAlerts()[0]), page.isQuirksMode());
        }
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
    @Browsers(FF)
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
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "[object SVGSVGElement]", IE = "exception")
    public void createDocumentNS_svg() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  var elt = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
            + "  alert(elt);\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception", FF3_6 = "Hello", FF = "exception")
    public void createDocumentNS_xul() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  var inner = document.createElementNS('http://www.mozilla.org/keymaster/gatekeeper/there.is.only.xul',"
            + "'label');\n"
            + "  inner.setAttribute('value', 'Hello');\n"
            + "  inner.style['fontFamily'] = 'inherit';\n"
            + "  document.body.appendChild(inner);\n"
            + "  alert(document.body.lastChild.value);\n"
            + "}\n"
            + "catch (e) { alert('exception'); }\n"
            + "</script>\n"
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
            + "<body onload='test()'>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "imported: [object HTMLScriptElement]", "replaced" },
            IE = "exception")
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
            + "  alert('replaced');\n"
            + " } catch (e) { alert('exception') }\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <span id='s1'></span>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * This one is like {@link #importNode_script()}, but the script is
     * a child of the imported node.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "imported: [object HTMLDivElement]", "replaced" },
            IE = "exception")
    public void importNode_scriptChild() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "function test() {\n"
            + " try {\n"
            + "  var d = document.implementation.createDocument(null, null, null);\n"
            + "  var xhtml = \"<html xmlns='http://www.w3.org/1999/xhtml'><div id='myDiv'><sc\" "
            + "   + \"ript>alert('o'); _scriptEvaluated=true;</scr\" + \"ipt></div></html>\";\n"
            + "  var newDoc = (new DOMParser()).parseFromString(xhtml, 'text/xml');\n"
            + "  var theDiv = newDoc.getElementById('myDiv');\n"
            + "  var importedDiv = window.document.importNode(theDiv, true);\n"
            + "  alert('imported: ' + importedDiv);\n"
            + "  var theSpan = document.getElementById('s1');\n"
            + "  document.body.replaceChild(importedDiv, theSpan);\n"
            + "  alert('replaced');\n"
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
    @Browsers(FF)
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
            + "<body onload='test()'>\n"
            + "<div id='myDiv'>\n"
            + "  <div></div>\n"
            + "</div>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = {"#ffffff", "", "#0000aa", "#0000aa", "#000000", "#000000" },
//            IE9 = {"#ffffff", "", "#0000aa", "#0000aa", "#000000", "#0" },
            FF3_6 = {"", "", "#0000aa", "#0000aa", "#000000", "#000000" },
            DEFAULT = {"", "", "#0000aa", "#0000aa", "x", "x" })
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
    @Alerts(FF = { "[object HTMLCollection]", "4", "red" },
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

        // for some strange reasons, the selenium driven browser is in an invalid
        // state after this test
        shutDownAll();
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
        final String lastModified = driver.findElement(By.id("i")).getAttribute("value");

        try {
            new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").parse(lastModified);
        }
        catch (final ParseException e) {
            fail(e.getMessage());
        }
    }

    /**
     * Warning: this test works fine in real FF8 when started manually but fails through WebDriver.
     * @throws Exception if an error occurs
     */
    @Test
    @BuggyWebDriver // tested with FF8, FF17, FF18
    @Alerts(FF = { "1", "[object HTMLBodyElement]" }, CHROME = { "0", "exception" }, IE = "exception")
    public void designMode_selectionRange_empty() throws Exception {
        designMode_selectionRange("");
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @BuggyWebDriver // tested with FF8, FF17, FF18
    @Alerts(FF = { "1", "[object Text]" }, CHROME = { "0", "exception" }, IE = "exception")
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

    /**
     * Test having &lt; and &gt; in attribute values.
     */
    @Test
    @Browsers(NONE)
    public void canAlreadyBeParsed() {
        assertTrue(HTMLDocument.canAlreadyBeParsed("<p>hallo</p>"));
        assertTrue(HTMLDocument.canAlreadyBeParsed("<img src='foo' alt=\"<'>\"></img>"));

        // double close is ok
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script></script></script>"));

        // check for correct string quoting in script
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc';</script>"));
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script>var test =\"abc\";</script>"));
        assertFalse(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc\";</script>"));
        assertFalse(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc;</script>"));
        assertFalse(HTMLDocument.canAlreadyBeParsed("<script>var test =\"abc;</script>"));

        // check quoting only inside script tags
        assertTrue(HTMLDocument.canAlreadyBeParsed("<script>var test ='abc';</script><p>it's fun</p>"));
    }

    /**
     * Regression test for a bug introduced by the document proxy and detected by the Dojo JavaScript library tests.
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts("true")
    public void equalityViaDifferentPaths() throws Exception {
        final String html
            = "<html><body>\n"
            + "<script>alert(document.body.parentNode.parentNode === document)</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception")
    public void getBoxObjectFor() throws Exception {
        final String html = "<html><head><title>Test</title><script>\n"
            + "function doTest() {\n"
            + "  var e = document.getElementById('log');\n"
            + "  try {\n"
            + "    var a = document.getBoxObjectFor(e);\n"
            + "    alert(a);\n"
            + "    alert(a === document.getBoxObjectFor(e));\n"
            + "    alert(a.screenX > 0);\n"
            + "    alert(a.screenY > 0);\n"
            + "  } catch (e) { alert('exception') }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='log'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = { "exception", "0 commands supported" },
            FF17 = { "32 commands supported", "not supported: foo, 123" },
            IE = { "32 commands supported", "not supported: foo, 123" })
    public void queryCommandSupported_common() throws Exception {
        final String[] commands = {"BackColor", "Bold",
            "Copy", "CreateLink", "Cut", "Delete",
            "FontName", "FontSize", "ForeColor", "FormatBlock",
            "Indent", "InsertHorizontalRule", "InsertImage", "InsertOrderedList",
            "InsertParagraph", "InsertUnorderedList", "Italic",
            "JustifyCenter", "JustifyFull", "JustifyLeft",  "JustifyRight",
            "Outdent", "Paste", "Redo", "RemoveFormat",
            "SelectAll", "StrikeThrough", "Subscript", "Superscript",
            "Underline", "Undo", "Unlink",
            "foo", "123" };
        queryCommandSupported(commands);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3_6 = { "exception", "0 commands supported" },
            DEFAULT = { "0 commands supported" },
            IE = { "46 commands supported" })
    public void queryCommandSupported_disctinct() throws Exception {
        final String[] commands = {"2D-Position", "AbsolutePosition",
            "BlockDirLTR", "BlockDirRTL", "BrowseMode",
            "ClearAuthenticationCache", "CreateBookmark",
            "DirLTR", "DirRTL", "EditMode",
            "InlineDirLTR", "InlineDirRTL", "InsertButton", "InsertFieldset",
            "InsertIFrame", "InsertInputButton", "InsertInputCheckbox", "InsertInputFileUpload",
            "InsertInputHidden", "InsertInputImage", "InsertInputPassword", "InsertInputRadio",
            "InsertInputReset", "InsertInputSubmit", "InsertInputText", "InsertMarquee",
            "InsertSelectDropdown", "InsertSelectListbox", "InsertTextArea",
            "JustifyNone",
            "LiveResize", "MultipleSelection", "Open", "OverWrite",
            "PlayImage", "Print", "Refresh", "RemoveParaFormat",
            "SaveAs", "SizeToControl", "SizeToControlHeight", "SizeToControlWidth", "Stop", "StopImage",
            "UnBookmark", "Unselect"};

        queryCommandSupported(commands);
    }

    private void queryCommandSupported(final String... commands) throws Exception {
        final String jsCommandArray = "['" + StringUtils.join(commands, "', '") + "']";
        final String html = "<html><head><title>Test</title><script>\n"
            + "function doTest() {\n"
            + "  var cmds = " + jsCommandArray + ";\n"
            + "  var nbSupported = 0;\n"
            + "  var cmdsNotSupported = [];\n"
            + "  try {\n"
            + "    for (var i=0; i<cmds.length; ++i) {\n"
            + "      var cmd = cmds[i];"
            + "      var b = document.queryCommandSupported(cmd);"
            + "      if (b)\n"
            + "        nbSupported++;\n"
            + "      else\n"
            + "        cmdsNotSupported[cmdsNotSupported.length] = cmd;\n"
            + "    }"
            + "  } catch (e) { alert('exception'); }\n"
            + "  alert(nbSupported + ' commands supported');\n"
            + "  if (nbSupported != 0 && cmdsNotSupported.length > 0)\n"
            + "    alert('not supported: ' + cmdsNotSupported.join(', '));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "<div id='log'></div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "3", "div1" }, IE8 = "undefined")
    public void querySelectorAll() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>Test</title>\n"
            + "<style>\n"
            + "  .red   {color:#FF0000;}\n"
            + "  .green {color:#00FF00;}\n"
            + "  .blue  {color:#0000FF;}\n"
            + "</style>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if(document.querySelectorAll) {\n"
            + "    var redTags = document.querySelectorAll('.green,.red');\n"
            + "    alert(redTags.length);\n"
            + "    alert(redTags.item(0).id);\n"
            + "  }\n"
            + "  else\n"
            + "    alert('undefined');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1' class='red'>First</div>\n"
            + "  <div id='div2' class='red'>Second</div>\n"
            + "  <div id='div3' class='green'>Third</div>\n"
            + "  <div id='div4' class='blue'>Fourth</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void querySelectorAll_badSelector() throws Exception {
        for (final String selector : JQUERY_CUSTOM_SELECTORS) {
            doTestQuerySelectorAll_badSelector(selector);
        }
    }

    private void doTestQuerySelectorAll_badSelector(final String selector) throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  document.querySelectorAll('" + selector + "');\n"
            + "  alert('working');\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("exception")
    public void querySelector_badSelector() throws Exception {
        for (final String selector : JQUERY_CUSTOM_SELECTORS) {
            doTestQuerySelector_badSelector(selector);
        }
    }

    private void doTestQuerySelector_badSelector(final String selector) throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  document.querySelector('" + selector + "');\n"
            + "  alert('working: " + selector + "');\n"
            + "} catch(e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "3", "div1" },
            IE = "undefined")
    public void querySelectorAll_quirks() throws Exception {
        final String html = "<html><head><title>Test</title>\n"
            + "<style>\n"
            + "  .red   {color:#FF0000;}\n"
            + "  .green {color:#00FF00;}\n"
            + "  .blue  {color:#0000FF;}\n"
            + "</style>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if(document.querySelectorAll) {\n"
            + "    var redTags = document.querySelectorAll('.red,.green');\n"
            + "    alert(redTags.length);\n"
            + "    alert(redTags.item(0).id);\n"
            + "  }\n"
            + "  else\n"
            + "    alert('undefined');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1' class='red'>First</div>\n"
            + "  <div id='div2' class='red'>Second</div>\n"
            + "  <div id='div3' class='green'>Third</div>\n"
            + "  <div id='div4' class='blue'>Fourth</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "3", IE = "undefined")
    public void querySelectorAll_implicitAttribute() throws Exception {
        final String html = "<html><head><title>Test</title>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if(document.querySelectorAll) {\n"
            + "    var result = document.querySelectorAll('[disabled]');\n"
            + "    alert(result.length);\n"
            + "  }\n"
            + "  else\n"
            + "    alert('undefined');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <select name='select4' id='select4' multiple='multiple'>\n"
            + "    <optgroup disabled='disabled'>\n"
            + "      <option id='option4a' class='emptyopt' value=''>Nothing</option>\n"
            + "      <option id='option4b' disabled='disabled' selected='selected' value='1'>1</option>\n"
            + "      <option id='option4c' selected='selected' value='2'>2</option>\n"
            + "    </optgroup>\n"
            + "    <option selected='selected' disabled='disabled' id='option4d' value='3'>3</option>\n"
            + "    <option id='option4e'>no value</option>\n"
            + "    </select>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = { "div1", "null" },
            IE6 = "undefined", IE7 = "undefined", IE8 = "undefined")
    public void querySelector() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html><head><title>Test</title>\n"
            + "<style>\n"
            + "  .red   {color:#FF0000;}\n"
            + "  .green {color:#00FF00;}\n"
            + "  .blue  {color:#0000FF;}\n"
            + "</style>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  if(document.querySelector) {\n"
            + "    alert(document.querySelector('.green,.red').id);\n"
            + "    alert(document.querySelector('.orange'));\n"
            + "  }\n"
            + "  else\n"
            + "    alert('undefined');\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='div1' class='red'>First</div>\n"
            + "  <div id='div2' class='red'>Second</div>\n"
            + "  <div id='div3' class='green'>Third</div>\n"
            + "  <div id='div4' class='blue'>Fourth</div>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "1", "0" }, IE = { "0", "1" })
    public void getElementsByTagName2() throws Exception {
        final String html = "<html xmlns:ns1='http://example.com'>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.getElementsByTagName('ns1:ele').length);\n"
            + "      alert(document.getElementsByTagName('ele').length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <ns1:ele>&nbsp;</ns1:ele>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "0" })
    @NotYetImplemented(IE)
    public void getElementsByTagName3() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.getElementsByTagName('ns1:ele').length);\n"
            + "      alert(document.getElementsByTagName('ele').length);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'>\n"
            + "  <ns1:ele>&nbsp;</ns1:ele>\n"
            + "</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Even if clear() does nothing, it was missing until HtmlUnit-2.8 and this test was failing.
     * @throws Exception if the test fails
     */
    @Test
    public void clear() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "document.clear();\n"
            + "</script>\n"
            + "</head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "true", "", "foo=bar", "foo=hello world" })
    public void cookie_write_cookiesEnabled() throws Exception {
        loadPageWithAlerts2(getCookieWriteHtmlCode());
    }

    static String getCookieWriteHtmlCode() {
        final String html =
              "<html><head><script>\n"
            + "  alert(navigator.cookieEnabled);\n"
            + "  alert(document.cookie);\n"
            + "  document.cookie = 'foo=bar';\n"
            + "  alert(document.cookie);\n"
            + "  document.cookie = 'foo=hello world';\n"
            + "  alert(document.cookie);\n"
            + "</script>\n"
            + "</head>\n"
            + "<body>abc</body>\n"
            + "</html>";
        return html;
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "", "a", "", "b", "" })
    public void cookie_write2() throws Exception {
        final String html =
              "<html>\n"
            + "    <head>\n"
            + "        <script>\n"
            + "            alert(document.cookie);\n"
            + "            document.cookie = 'a';\n"
            + "            alert(document.cookie);\n"
            + "            document.cookie = '';\n"
            + "            alert(document.cookie);\n"
            + "            document.cookie = 'b';\n"
            + "            alert(document.cookie);\n"
            + "            document.cookie = '';\n"
            + "            alert(document.cookie);\n"
            + "        </script>\n"
            + "    </head>\n"
            + "    <body>abc</body>\n"
            + "</html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(NONE)
    public void buildCookie() throws Exception {
        final String domain = URL_FIRST.getHost();
        checkCookie(HTMLDocument.buildCookie("", URL_FIRST), EMPTY_COOKIE_NAME, "", "/", domain, false, null);
        checkCookie(HTMLDocument.buildCookie("toto", URL_FIRST), EMPTY_COOKIE_NAME, "toto", "/", domain, false, null);
        checkCookie(HTMLDocument.buildCookie("toto=", URL_FIRST), "toto", "", "/", domain, false, null);
        checkCookie(HTMLDocument.buildCookie("toto=foo", URL_FIRST), "toto", "foo", "/", domain, false, null);
        checkCookie(HTMLDocument.buildCookie("toto=foo;secure", URL_FIRST), "toto", "foo", "/", domain, true, null);
        checkCookie(HTMLDocument.buildCookie("toto=foo;path=/myPath;secure", URL_FIRST),
                "toto", "foo", "/myPath", domain, true, null);

        // Check that leading and trailing whitespaces are ignored
        checkCookie(HTMLDocument.buildCookie("   toto=foo;  path=/myPath  ; secure  ", URL_FIRST),
                "toto", "foo", "/myPath", domain, true, null);

        // Check that we accept reserved attribute names (e.g expires, domain) in any case
        checkCookie(HTMLDocument.buildCookie("toto=foo; PATH=/myPath; SeCURE", URL_FIRST),
                "toto", "foo", "/myPath", domain, true, null);

        // Check that we are able to parse and set the expiration date correctly
        final String dateString = "Fri, 21 Jul 2006 20:47:11 UTC";
        final Date date = parseHttpDate(dateString);
        checkCookie(HTMLDocument.buildCookie("toto=foo; expires=" + dateString, URL_FIRST),
                "toto", "foo", "/", domain, false, date);
    }

    private void checkCookie(final Cookie cookie, final String name, final String value,
            final String path, final String domain, final boolean secure, final Date date) {
        assertEquals(name, cookie.getName());
        assertEquals(value, cookie.getValue());
        assertEquals(path, cookie.getPath());
        assertEquals(domain, cookie.getDomain());
        assertEquals(secure, cookie.isSecure());
        assertEquals(date, cookie.getExpires());
    }

    /**
     * Regression test for bug 3030247: expired cookie was saved.
     * http://sourceforge.net/tracker/?func=detail&aid=3030247&group_id=47038&atid=448266
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "", "test2=1", "" })
    public void writeCookieExpired() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "alert(document.cookie);\n"
            + "document.cookie = 'test2=1';\n"
            + "alert(document.cookie);\n"
            + "document.cookie = 'test2=;expires=Fri, 02-Jan-1970 00:00:00 GMT';\n"
            + "alert(document.cookie);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Only IE accepts more than the tag name.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "exception", IE = "INPUT")
    public void createElement_notOnlyTagName() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "try {\n"
            + "  var t = document.createElement('<input name=x>');\n"
            + "  alert(t.tagName);\n"
            + "} catch(e) {"
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
    @Alerts("null")
    public void getElementById_strict() throws Exception {
        getElementById_strict(true);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(DEFAULT = "null", IE = "")
    public void getElementById_quirks() throws Exception {
        getElementById_strict(false);
    }

    private void getElementById_strict(final boolean xhtml) throws Exception {
        final String header = xhtml ? "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" "
                + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" : "";
        final String html = header + "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload=test()>\n"
            + "  <a name='myId'/>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLHeadElement]",
            FF3_6 = "undefined",
            IE = "undefined")
    public void head() throws Exception {
        final String html = "<html><body>\n"
            + "<script>\n"
            + "  alert(document.head);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(IE = {"#0000ff", "", "#0000aa", "#0000aa", "#000000", "#000000" },
//            IE9 = {"#0000ff", "", "#0000aa", "#0000aa", "#000000", "#0" },
            FF3_6 = {"", "", "#0000aa", "#0000aa", "#000000", "#000000" },
            FF = {"", "", "#0000aa", "#0000aa", "x", "x" })
    public void alinkColor() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(document.alinkColor);\n"
            + "        alert(b.aLink);\n"
            + "        document.alinkColor = '#0000aa';\n"
            + "        alert(document.alinkColor);\n"
            + "        alert(b.aLink);\n"
            + "        document.alinkColor = 'x';\n"
            + "        alert(document.alinkColor);\n"
            + "        alert(b.aLink);\n"
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
    @Alerts(IE = {"#0000ff", "", "#0000aa", "#0000aa", "#000000", "#000000" },
//            IE9 = {"#0000ff", "", "#0000aa", "#0000aa", "#000000", "#0" },
            FF3_6 = {"", "", "#0000aa", "#0000aa", "#000000", "#000000" },
            FF = {"", "", "#0000aa", "#0000aa", "x", "x" })
    public void linkColor() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(document.linkColor);\n"
            + "        alert(b.link);\n"
            + "        document.linkColor = '#0000aa';\n"
            + "        alert(document.linkColor);\n"
            + "        alert(b.link);\n"
            + "        document.linkColor = 'x';\n"
            + "        alert(document.linkColor);\n"
            + "        alert(b.link);\n"
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
    @Alerts(IE = {"#800080", "", "#0000aa", "#0000aa", "#000000", "#000000" },
//            IE9 = {"#800080", "", "#0000aa", "#0000aa", "#000000", "#0" },
            FF3_6 = {"", "", "#0000aa", "#0000aa", "#000000", "#000000" },
            FF10 = {"#800080", "", "#0000aa", "#0000aa", "x", "x" },
            FF17 = {"", "", "#0000aa", "#0000aa", "x", "x" })
    public void vlinkColor() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(document.vlinkColor);\n"
            + "        alert(b.vLink);\n"
            + "        document.vlinkColor = '#0000aa';\n"
            + "        alert(document.vlinkColor);\n"
            + "        alert(b.vLink);\n"
            + "        document.vlinkColor = 'x';\n"
            + "        alert(document.vlinkColor);\n"
            + "        alert(b.vLink);\n"
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
    @Alerts(IE = {"#000000", "", "#0000aa", "#0000aa", "#000000", "#000000" },
//            IE9 = {"#000000", "", "#0000aa", "#0000aa", "#000000", "#0" },
            FF3_6 = {"", "", "#0000aa", "#0000aa", "#000000", "#000000" },
            FF = {"", "", "#0000aa", "#0000aa", "x", "x" })
    public void fgColor() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        var b = document.getElementById('body');\n"
            + "        alert(document.fgColor);\n"
            + "        alert(b.text);\n"
            + "        document.fgColor = '#0000aa';\n"
            + "        alert(document.fgColor);\n"
            + "        alert(b.text);\n"
            + "        document.fgColor = 'x';\n"
            + "        alert(document.fgColor);\n"
            + "        alert(b.text);\n"
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
    @Alerts(FF = { "", "true" }, FF3_6 = { "", "false" }, IE = { })
    @NotYetImplemented(FF3_6)
//            IE9 = {"", "true" }
    public void getSelection() throws Exception {
        final String html =
            "<html>\n"
            + "  <head>\n"
            + "    <script>\n"
            + "      function test() {\n"
            + "        if (document.getSelection) {\n"
            + "          alert(document.getSelection());\n"
            + "          alert(document.getSelection() === window.getSelection());\n"
            + "        }\n"
            + "      }\n"
            + "    </script>\n"
            + "  </head>\n"
            + "  <body id='body' onload='test()'>blah</body>\n"
            + "</html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "true", "[object]", "true" },
            DEFAULT = { "true", "undefined", "false" })
    public void document_xxx_formAccess() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.foo == document.forms.foo);\n"
            + "      alert(document.blah);\n"
            + "      alert(document.blah == document.forms.foo)\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='foo'>the div 1</div>\n"
            + "  <form name='foo' id='blah'>\n"
            + "    <input name='foo'>\n"
            + "  </form>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined", "iso-8859-1", "windows-1252" },
            FF = { "ISO-8859-1", "ISO-8859-1", "undefined", "undefined" },
            CHROME = { "ISO-8859-1", "ISO-8859-1", "ISO-8859-1", "ISO-8859-1" })
    public void encoding() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.inputEncoding);\n"
            + "      alert(document.characterSet);\n"
            + "      alert(document.charset);\n"
            + "      alert(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined", "iso-8859-1", "windows-1252" },
            FF = { "ISO-8859-1", "ISO-8859-1", "undefined", "undefined" },
            CHROME = { "ISO-8859-1", "ISO-8859-1", "ISO-8859-1", "ISO-8859-1" })
    public void encoding2() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=ISO-8859-1'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.inputEncoding);\n"
            + "      alert(document.characterSet);\n"
            + "      alert(document.charset);\n"
            + "      alert(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined", "iso-8859-1", "windows-1252" },
            FF = { "ISO-8859-1", "ISO-8859-1", "undefined", "undefined" },
            CHROME = { "ISO-8859-1", "ISO-8859-1", "ISO-8859-1", "ISO-8859-1" })
    public void encoding3() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.inputEncoding);\n"
            + "      alert(document.characterSet);\n"
            + "      alert(document.charset);\n"
            + "      alert(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html", "ISO-8859-1");
        verifyAlerts(DEFAULT_WAIT_TIME, expectedAlerts, driver);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined", "utf-8", "windows-1252" },
            FF = { "UTF-8", "UTF-8", "undefined", "undefined" },
            CHROME = { "UTF-8", "UTF-8", "UTF-8", "ISO-8859-1" })
    public void encoding4() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.inputEncoding);\n"
            + "      alert(document.characterSet);\n"
            + "      alert(document.charset);\n"
            + "      alert(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=UTF-8", "ISO-8859-1");
        verifyAlerts(DEFAULT_WAIT_TIME, expectedAlerts, driver);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined", "utf-8", "windows-1252" },
            FF = { "UTF-8", "UTF-8", "undefined", "undefined" },
            CHROME = { "UTF-8", "UTF-8", "UTF-8", "ISO-8859-1" })
    public void encoding5() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='Content-Type' content='text/html; charset=iso-8859-1'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.inputEncoding);\n"
            + "      alert(document.characterSet);\n"
            + "      alert(document.charset);\n"
            + "      alert(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html;charset=utf-8", "ISO-8859-1");
        verifyAlerts(DEFAULT_WAIT_TIME, expectedAlerts, driver);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "undefined", "undefined", "utf-8", "windows-1252" },
            FF = { "UTF-8", "UTF-8", "undefined", "undefined" },
            CHROME = { "UTF-8", "UTF-8", "UTF-8", "ISO-8859-1" })
    public void encoding6() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <meta charset='UTF-8'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.inputEncoding);\n"
            + "      alert(document.characterSet);\n"
            + "      alert(document.charset);\n"
            + "      alert(document.defaultCharset);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "  <a id='myId' href='test?è=è'>test</a>"
            + "</body></html>";

        final String[] expectedAlerts = getExpectedAlerts();
        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html", "UTF-8");
        verifyAlerts(DEFAULT_WAIT_TIME, expectedAlerts, driver);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void encoding7() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "<meta charset='UTF-8'>\n"
            + "</head><body>\n"
            + "  <a id='myId' href='test?\u00E8=\u00E8'>test</a>"
            + "</body></html>";

        final WebDriver driver = loadPage2(html, URL_FIRST, "text/html", "UTF-8");
        driver.findElement(By.id("myId")).click();
        String actualQuery = driver.getCurrentUrl();
        actualQuery = actualQuery.substring(actualQuery.indexOf('?'));
        final String expectedQuery;
        if (getBrowserVersion().isIE()) {
            expectedQuery = "?\u00E8=\u00E8";
        }
        else {
            expectedQuery = "?%C3%A8=%C3%A8";
        }
        assertTrue(actualQuery.endsWith(expectedQuery));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "5", "BackCompat", "undefined", "undefined" },
            DEFAULT = { "undefined", "BackCompat", "function", "function" })
    public void documentMode() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.documentMode);\n"
            + "      alert(document.compatMode);\n"
            + "      alert(typeof document.querySelectorAll);\n"
            + "      alert(typeof document.createElement('div').querySelector);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = { "7", "CSS1Compat", "undefined", "undefined" },
            IE9 = { "9", "CSS1Compat", "function", "function" },
            DEFAULT = { "undefined", "CSS1Compat", "function", "function" })
    public void documentMode_standards() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.documentMode);\n"
            + "      alert(document.compatMode);\n"
            + "      alert(typeof document.querySelectorAll);\n"
            + "      alert(typeof document.createElement('div').querySelector);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = { "8", "CSS1Compat", "false", "false" },
            IE9 = { "9", "CSS1Compat", "false", "false" },
            DEFAULT = { "undefined", "BackCompat", "false", "false" })
    public void documentMode_standards_8() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='X-UA-Compatible' content='IE=8'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.documentMode);\n"
            + "      alert(document.compatMode);\n"
            + "      alert(!document.querySelectorAll);\n"
            + "      alert(!document.createElement('div').querySelector);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = { "8", "CSS1Compat", "false", "false" },
            IE9 = { "9", "CSS1Compat", "false", "false" },
            DEFAULT = { "undefined", "CSS1Compat", "false", "false" })
    public void documentMode_standards_8_prefix() throws Exception {
        final String html = HtmlPageTest.STANDARDS_MODE_PREFIX_ + "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='X-UA-Compatible' content='IE=8'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.documentMode);\n"
            + "      alert(document.compatMode);\n"
            + "      alert(!document.querySelectorAll);\n"
            + "      alert(!document.createElement('div').querySelector);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = { "8", "CSS1Compat", "false", "false" },
            IE9 = { "9", "CSS1Compat", "false", "false" },
            DEFAULT = { "undefined", "BackCompat", "false", "false" })
    public void documentMode_standards_9() throws Exception {
        final String html = "<html>\n"
            + "<head>\n"
            + "  <meta http-equiv='X-UA-Compatible' content='IE=9'>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.documentMode);\n"
            + "      alert(document.compatMode);\n"
            + "      alert(!document.querySelectorAll);\n"
            + "      alert(!document.createElement('div').querySelector);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE8 = { "7", "CSS1Compat", "undefined", "undefined" },
            IE9 = { "9", "CSS1Compat", "function", "function" },
            DEFAULT = { "undefined", "CSS1Compat", "function", "function" })
    @NotYetImplemented
    public void documentMode_html5() throws Exception {
        final String html = "<!DOCTYPE html>\n"
            + "<html>\n"
            + "<head>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert(document.documentMode);\n"
            + "      alert(document.compatMode);\n"
            + "      alert(typeof document.querySelectorAll);\n"
            + "      alert(typeof document.createElement('div').querySelector);\n"
            + "    }\n"
            + "  </script>\n"
            + "</head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
